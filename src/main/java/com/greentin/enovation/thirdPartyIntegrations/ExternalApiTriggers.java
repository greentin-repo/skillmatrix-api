/**
 * 
 */
package com.greentin.enovation.thirdPartyIntegrations;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.regex.Pattern;

import org.hibernate.Session;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.annotation.PropertySources;
import org.springframework.core.env.Environment;
import org.springframework.core.task.TaskExecutor;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import com.google.gson.Gson;
import com.greentin.enovation.communication.ICommunication;
import com.greentin.enovation.communication.NotificationConstants;
import com.greentin.enovation.communication.NotificationMessage;
import com.greentin.enovation.config.EnovationConfig;
import com.greentin.enovation.dto.MailDTO;
import com.greentin.enovation.model.BranchMaster;
import com.greentin.enovation.model.DepartmentMaster;
import com.greentin.enovation.model.EmailTemplateMaster;
import com.greentin.enovation.model.EmployeeDetails;
import com.greentin.enovation.model.EmployeeHierarchy;
import com.greentin.enovation.model.EmployeeSyncLog;
import com.greentin.enovation.model.OrganizationMaster;
import com.greentin.enovation.utils.BaseRepository;
import com.greentin.enovation.utils.CommonUtils;
import com.greentin.enovation.utils.EnovationConstants;
import com.greentin.enovation.utils.MailUtil;

/**
 * @author Vinay B
 * @date May 28, 2020 1:28:55 PM
 */
@Component
@Transactional
@CrossOrigin
@RestController
@PropertySources({ @PropertySource("classpath:application.properties"),
		@PropertySource("classpath:email_template_contents.properties") })
public class ExternalApiTriggers extends BaseRepository {

	private static final Logger LOGGER = LoggerFactory.getLogger(ExternalApiTriggers.class);

	@Autowired
	PasswordEncoder password;

	@Autowired
	EnovationConfig enoConfig;

	@Autowired
	@Qualifier("taskExecutor")
	private TaskExecutor taskExecutor;

	@Autowired
	private ICommunication communication;

	@Autowired
	Environment env;

	// Both RestTemplate and URI instances can be cached
	private static final RestTemplate restTemplate = new RestTemplate();

	public String authenticateUser() throws JSONException {

		// String uri = EnovationConfig.buddyConfig.get("authenticateUser");

		String uri = "http://10.37.61.247/AscentIS/Api/AuthenticateUser";

		HttpHeaders headers = getHttpHeader();

		JSONObject user = new JSONObject();
		user.put("username", EnovationConfig.buddyConfig.get("ascentUserName"));
		user.put("password", EnovationConfig.buddyConfig.get("ascentPassword"));

		LOGGER.info("Json ==> " + user);
		LOGGER.info(" # Header " + headers);
		HttpEntity<Object> request = new HttpEntity<>(user.toString(), headers);

		LOGGER.info(" Inside authenticate user | request url " + uri);
		LOGGER.info(" Inside authenticate user | request body " + user);

		String token = restTemplate.postForObject(uri, request, String.class);

		return token;

	}

	private HttpHeaders getHttpHeader() {
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
		return headers;
	}

	@Transactional(rollbackFor = { Exception.class, JSONException.class })
	// @GetMapping(value = "connector/externalApiTesting")
	@Scheduled(cron = "0 1 1 ? * *")
	public ApiResponse getEmployeeDetails() {
		LOGGER.info("# External API Trigger | getEmployeeDetails");
		EmployeeSyncLog log = new EmployeeSyncLog();
		ApiResponse res = null;
		Session session = getCurrentSession();
		boolean isSchedulerFlagOn = checkSchedulerStatus(session, EnovationConstants.ASCENT_EMPLOYEE_SYNC);
		LOGGER.info("# Scheduler Status --->  " + isSchedulerFlagOn);
		try {
			if (isSchedulerFlagOn) {
				String token = authenticateUser();
				if (token != null) {

					log.setToken(token);

					LOGGER.info("Token is not null ==> " + token);

					LOGGER.info(" Inside get employee response | token details " + token);

					String uri = EnovationConfig.buddyConfig.get("ascentEmployeeSyncURI");

					LOGGER.info(" Inside get employee response | request url " + uri);

					HttpHeaders headers = getHttpHeader();
					String trimmedToken = trimQuotes(token);

					LOGGER.info("Token Detail, After trimming quotes ==> " + trimmedToken);
					headers.add("Auth_Token", trimmedToken);

					LOGGER.info("# Header " + headers);

					JSONObject body = createEmployeeSyncRequest();

					HttpEntity<Object> request = new HttpEntity<>(body.toString(), headers);

					LOGGER.info("Json ==> " + body);

					LOGGER.info(" Inside get employee response | request body " + body);

					String apiRes = restTemplate.postForObject(uri, request, String.class);

					LOGGER.info("API Response ==> " + apiRes);

					Gson g = new Gson();
					res = g.fromJson(apiRes, ApiResponse.class);
					if (res != null) {
						boolean flag = employeeOperations(res, session, log);
						if (flag) {
							log.setStatus(EnovationConstants.statusSuccess);
						}
					} else {
						LOGGER.info(" Inside get employee response | emp detail not found ");
					}
				} else {
					LOGGER.info(" Inside get employee response | token details not received ");
					log.setStatus(EnovationConstants.statusFail);
					log.setErrorLog("TOKEN NOT FOUND.");
				}
			} else {
				log.setStatus(EnovationConstants.statusFail);
				log.setErrorLog("EMPLOYEE SYNC SCHEDULER IS OFF.");
			}

		} catch (Exception e) {
			StringWriter errors = new StringWriter();
			e.printStackTrace(new PrintWriter(errors));
			e.printStackTrace();
			System.err.println("Printing Error Message (stack trace string writer) ==> " + errors.toString());
			log.setStatus(EnovationConstants.statusFail);
			log.setErrorLog(errors.toString());
			LOGGER.info(" Inside get employee response | Exception Details " + e.getMessage());
		} finally {

			if (log.getStatus() == null || log.getStatus().equals(EnovationConstants.statusFail)) {
				String body = env.getProperty("myeNovation_ASCENT_SYNC_STATUS.body");

				DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
				LocalDateTime now = LocalDateTime.now();
				String date = dtf.format(now);
				String subject = "Error |  myeNovation-Ascent Integration " + date;
				// String content=body.replaceAll(Pattern.quote("{body}")," myeNovation - Ascent
				// Sync Status : "+log.getStatus());
				taskExecutor.execute(new MailUtil(EnovationConfig.buddyConfig.get("ascentStatusMail"), subject, body,
						communication));
			}

			session.save(log);
		}
		return res;

	}

	private JSONObject createEmployeeSyncRequest() throws JSONException {
		JSONObject obj = new JSONObject();
		JSONArray serviceParam = new JSONArray();

		JSONObject body = new JSONObject();
		body.put("serviceName", "GETEMPLOYEES");
		body.put("serviceParameters", serviceParam);
		body.put("auxiliaryParamters", obj);
		return body;
	}

	private boolean checkSchedulerStatus(Session session, String label) {
		LOGGER.info("# checkSchedulerStatus - Label -----> " + label);
		return ((String) session.createNativeQuery("select config_value FROM system_config WHERE config_name=:label")
				.setParameter("label", label).getSingleResult()).toString().equalsIgnoreCase(EnovationConstants.Y)
						? true
						: false;
	}

	private String trimQuotes(String token) {

		if (token.startsWith("\"")) {
			token = token.substring(1, token.length() - 1);
		}
		if (token.endsWith("\"")) {
			token = token.substring(0, token.length() - 1);
		}

		return token;
	}

	/**
	 * @author Vinay B May 28, 2020 2:08:55 PM
	 * @param res
	 * @param session
	 * @param log
	 * @return
	 */
	private boolean employeeOperations(ApiResponse res, Session session, EmployeeSyncLog log) throws Exception {
		boolean empOpFlag = false;
		if (res != null && res.getResponseData() != null && !res.getResponseData().isEmpty()) {

			log.setMessage(EnovationConstants.RESPONSE_IS_NOT_EMPTY);

			List<BranchMaster> branchDetails = getMasterBranchDetails(session);
			List<DepartmentMaster> deptDetails = getMasterDepartmentDetails(session);
			List<EmployeeDetails> empList = getEmpList(session);

			int additionCount = 0, deletionCount = 0, updationCount = 0, totalCount = 0;
			totalCount = res.getResponseData().size();
			log.setTotalCount(totalCount);
			for (ResponseData x : res.getResponseData()) {
				LOGGER.info("Size of List data to ==> " + res.getResponseData().size());
				if (x.getLocationCode() != null && x.getLocationCode().equals("03")) {

					LOGGER.info("Current Location Code is  " + x.getLocationCode());
					x.setLocationCode("02");
					LOGGER.info("New Location Code is  " + x.getLocationCode());

				} else {
					LOGGER.info("Location Code " + x.getLocationCode());
				}

				int branchId = getBranchIdByLocationCode(x, branchDetails);
				System.err.println("Printing BrachId ==> " + branchId);
				int deptId = getdeptIdByDepartmentCode(x, deptDetails);
				System.err.println("Printing deptId ==> " + deptId);
				EmployeeDetails emp = getEmpObject(empList, x, branchId);

				if (x.getInActiveFrom() != null) {

				}

				if (deactivateEmpRecord(x)) {
					LOGGER.info("# Deactive the record ");
					LOGGER.info("# Inactive id : " + x.getInactiveID());
					if (emp != null && emp.getEmpId() > 0) {
						LOGGER.info(" Inside employeeOperations | delete employee - emp id" + emp.getEmpId()
								+ " Emp Number -> " + x.getEmpNo());
						boolean flag = deleteEmployeeDetails(emp, x, session);
						if (flag) {
							deletionCount++;
						}
					}
				} else {
					LOGGER.info("# Add / Update Employee Record ");
					int empLevelId = getEmployeeLevel(session, x, branchId);
					if (emp != null && emp.getEmpId() > 0) {
						LOGGER.info(" Inside employeeOperations | update employee - emp id " + emp.getEmpId()
								+ " Emp Number -> " + x.getEmpNo());
						boolean flag = udpateEmployeeDetails(emp, x, session, deptId, branchId, empLevelId);
						if (flag) {
							updationCount++;
						}
					} else {
						LOGGER.info(" Inside employeeOperations | add employee -  Emp Number -> " + x.getEmpNo());
						boolean flag = addEmployeeDetails(x, session, deptId, branchId, empLevelId);
						if (flag) {
							additionCount++;
						}
					}
				}
			}
			empOpFlag = true;
			log.setAdditionCount(additionCount);
			log.setDeletionCount(deletionCount);
			log.setUpdationCount(updationCount);
		} else {
			empOpFlag = true;
			log.setMessage(EnovationConstants.RESPONSE_IS_EMPTY);
		}

		return empOpFlag;
	}

	private boolean deactivateEmpRecord(ResponseData x) {
		LOGGER.info(" Inside employeeOperations | deactivateEmpRecord");

		if (x.getInActiveFrom() != null && x.getInActiveFrom().length() > 0) {
			LOGGER.info("# Inactivate From Date : " + x.getInActiveFrom());
			DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
			// convert String to LocalDate
			LocalDate inactiveFromDate = LocalDate.parse(x.getInActiveFrom(), formatter);
			LocalDate now = LocalDate.now();

			if (x.getInActiveFrom() != null && ((now.isAfter(inactiveFromDate)) || now.equals(inactiveFromDate))) {
				LOGGER.info("# Current date is greater than inactive date");
				if (x.getInactiveID() > 0) {
					return true;
				} else {
					LOGGER.info("# Inactive Id : " + x.getInactiveID());
				}
			} else {
				LOGGER.info("# Current date is smaller than inactive date");
			}
		} else {
			LOGGER.info("# Inactive From date does not found");
		}
		return false;
	}

	private int getEmployeeLevel(Session session, ResponseData x, int branchId) {
		LOGGER.info(" Inside employeeOperations | getEmployeeLevel  -- Category Code as Level -> " + x.getLevel());

		String levelType = "";
		int levelId = 0;

		if (x.getLevel() != null && x.getLevel().length() > 0) {
			if (x.getLevel().equalsIgnoreCase(EnovationConstants.LEVEL_CODE_01)) {
				levelType = EnovationConstants.EMP_TYPE_OE;
			} else if (x.getLevel().equalsIgnoreCase(EnovationConstants.LEVEL_CODE_10)) {
				levelType = EnovationConstants.EMP_TYPE_OE;
			} else {
				levelType = EnovationConstants.EMP_TYPE_STAFF;
			}

			if (levelType != null && levelType.length() > 0) {
				levelId = ((Number) session.createNativeQuery(
						"select coalesce(emp_lvl_id,0) as levelId from employee_hierarchy WHERE level_type=:levelType and branch_id=:branchId")
						.setParameter("levelType", levelType).setParameter("branchId", branchId).getSingleResult())
						.intValue();
			} else {
				LOGGER.info("# Level Type Value is null or empty - " + levelType);
			}
		} else {
			LOGGER.info("# Employee Level is null  - " + x.getLevel());
		}
		return levelId;
	}

	/**
	 * @author Vinay B May 28, 2020 5:25:31 PM
	 * @param x
	 * @param deptDetails
	 * @return
	 */
	private int getdeptIdByDepartmentCode(ResponseData res, List<DepartmentMaster> deptDetails) {
		int deptId = 0;
		if (deptDetails != null && !deptDetails.isEmpty()) {

			LOGGER.info(" Department list is not empty | dept list size -->  " + deptDetails.size());

			DepartmentMaster dept = deptDetails
					.stream().filter(Objects::nonNull).filter(x -> x.getDeptCode() != null
							&& res.getDepartmentCode() != null && x.getDeptCode().equals(res.getDepartmentCode()))
					.findAny().orElse(null);

			if (dept != null) {
				deptId = dept.getDeptId();
			}
		}
		return deptId;
	}

	/**
	 * @author Vinay B May 28, 2020 4:57:33 PM
	 * @param x
	 * @param session
	 * @param deptId
	 * @param branchId
	 * @param empLevelId
	 * @return
	 */
	private boolean addEmployeeDetails(ResponseData x, Session session, int deptId, int branchId, int empLevelId) {
		LOGGER.info("INSIDE addEmployeeDetails");
		EmployeeDetails emp = new EmployeeDetails();
		SimpleDateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
		String pass = String.valueOf(CommonUtils.GenerateRandomPassword(8));// points to the starting of
		boolean flag = false;
		if (branchId > 0) {
			LOGGER.info("Printing branch Id ==> " + branchId);
			emp.setBranch(new BranchMaster(branchId));

			if (x.getDesignation() != null) {
				emp.setDesignation(x.getDesignation());
			}
			if (x.getEmail() != null) {
				emp.setEmailId(x.getEmail());
			}
			if (x.getMobile() != null) {
				emp.setContactNo(x.getMobile());
			}
			if (deptId > 0) {
				emp.setDept(new DepartmentMaster(deptId));
			}
			if (x.getEmpFirstName() != null) {
				emp.setFirstName(x.getEmpFirstName());
			}
			if (x.getEmpLastName() != null) {
				emp.setLastName(x.getEmpLastName());
			}
			if (x.getEmpMiddleName() != null) {
				emp.setMiddleName(x.getEmpMiddleName());
			}

			if (x.getBirthDate() != null) {
				LOGGER.info("Date of Birth ==> " + x.getBirthDate());
				boolean dateCheckFlag = false;
				java.sql.Date date = null;
				try {
					java.util.Date parsed = inputFormat.parse(x.getBirthDate());
					date = new java.sql.Date(parsed.getTime());
					dateCheckFlag = true;
				} catch (ParseException e) {
					dateCheckFlag = false;
					LOGGER.info(" Add employee | Birthday | incorrect date format " + x.getBirthDate());
				}
				if (dateCheckFlag && date != null) {
					emp.setDOB(date);
				}

			}
			if (x.getAnniversary() != null && x.getAnniversary().length() > 0) {
				LOGGER.info("Date of Anniversary ==> " + x.getAnniversary());
				boolean dateCheckFlag = false;
				java.sql.Date date = null;
				try {
					java.util.Date parsed = inputFormat.parse(x.getAnniversary());
					date = new java.sql.Date(parsed.getTime());
					dateCheckFlag = true;
				} catch (ParseException e) {
					dateCheckFlag = false;
					LOGGER.info(" Add employee | Anniversary | incorrect date format " + x.getAnniversary());
				}

				if (dateCheckFlag && date != null) {
					emp.setDOA(date);
				}

			}
			if (x.getJoinedOn() != null) {
				LOGGER.info("Joined On ==> " + x.getJoinedOn());
				boolean dateCheckFlag = false;
				java.sql.Date date = null;
				try {
					java.util.Date parsed = inputFormat.parse(x.getJoinedOn());
					date = new java.sql.Date(parsed.getTime());
					dateCheckFlag = true;
				} catch (ParseException e) {
					LOGGER.info(" Add employee | Joining Date |incorrect date format " + x.getBirthDate());
					dateCheckFlag = false;
				}
				if (dateCheckFlag && date != null) {
					emp.setDoj(date.toString());
				}

			}
			if (x.getInActiveFrom() != null) {
				emp.setInactiveFrom(x.getInActiveFrom());
			}

			if (x.getEmpNo() != null) {
				emp.setCmpyEmpId(x.getEmpNo());
			}
			if (empLevelId > 0) {
				emp.setEmpLevelDetails(new EmployeeHierarchy(empLevelId));
			}
			emp.setIsDeactive(EnovationConstants.ZERO);
			emp.setPassword(pass);
			emp.setIsEmailVerified(EnovationConstants.ONE);
			emp.setPassword(password.encode(EnovationConstants.DEFAULT_PASSWORD));
			emp.setSource(EnovationConstants.EXTERNAL);
			emp.setOrganization(new OrganizationMaster(EnovationConstants.ONE));
			emp.setEmpType(EnovationConstants.EMPLOYEE);
			session.save(emp);
			LOGGER.info("middle name: "+emp.getMiddleName());

			session.createNativeQuery("INSERT INTO emp_roles (emp_id,role_id) VALUES (:empId,:roleId)")
					.setParameter("empId", emp.getEmpId()).setParameter("roleId", EnovationConstants.EMPLOYEE_ROLE)
					.executeUpdate();

			flag = true;
		}
		return flag;
	}

	/**
	 * @author Vinay B May 28, 2020 4:57:30 PM
	 * @param emp
	 * @param x
	 * @param session
	 * @param deptId
	 * @param branchId
	 * @param empLevelId
	 * @return
	 */
	private boolean udpateEmployeeDetails(EmployeeDetails emp, ResponseData x, Session session, int deptId,
			int branchId, int empLevelId) {
		boolean flag = false;
		SimpleDateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
		if (branchId > 0) {
			emp.setBranch(new BranchMaster(branchId));
			if (x.getDesignation() != null) {
				emp.setDesignation(x.getDesignation());
			}

			if (deptId > 0) {
				emp.setDept(new DepartmentMaster(deptId));
			}
			if (x.getEmail() != null) {
				emp.setEmailId(x.getEmail());
			}
			if (x.getMobile() != null) {
				emp.setContactNo(x.getMobile());
			}
			if (x.getEmpFirstName() != null) {
				emp.setFirstName(x.getEmpFirstName());
			}
			if (x.getEmpLastName() != null) {
				emp.setLastName(x.getEmpLastName());
			}
			if (x.getEmpMiddleName() != null) {
				emp.setMiddleName(x.getEmpMiddleName());
			}
			if (x.getBirthDate() != null) {
				System.out.println("Date of Birth ==> " + x.getBirthDate());
				boolean dateCheckFlag = false;
				java.sql.Date date = null;
				try {
					java.util.Date parsed = inputFormat.parse(x.getBirthDate());
					date = new java.sql.Date(parsed.getTime());
					dateCheckFlag = true;
				} catch (ParseException e) {
					LOGGER.info(" Update employee | birthday |incorrect date format " + x.getBirthDate());
					dateCheckFlag = false;
				}
				if (dateCheckFlag && date != null) {
					emp.setDOB(date);
				}

			}
			if (x.getAnniversary() != null && x.getAnniversary().length() > 0) {
				System.out.println("Date of Anniversary ==> " + x.getAnniversary());
				boolean dateCheckFlag = false;
				java.sql.Date date = null;
				try {
					java.util.Date parsed = inputFormat.parse(x.getAnniversary());
					date = new java.sql.Date(parsed.getTime());
					dateCheckFlag = true;
				} catch (ParseException e) {
					LOGGER.info(" Update employee | Anniversary | incorrect date format " + x.getAnniversary());
					dateCheckFlag = false;
				}

				if (dateCheckFlag && date != null) {
					emp.setDOA(date);
				}

			}
			if (x.getJoinedOn() != null) {
				LOGGER.info("Joined On ==> " + x.getJoinedOn());
				boolean dateCheckFlag = false;
				java.sql.Date date = null;
				try {
					java.util.Date parsed = inputFormat.parse(x.getJoinedOn());
					date = new java.sql.Date(parsed.getTime());
					dateCheckFlag = true;
				} catch (ParseException e) {
					LOGGER.info(" Update employee | Joining Date |incorrect date format " + x.getBirthDate());
					dateCheckFlag = false;
				}
				if (dateCheckFlag && date != null) {
					emp.setDoj(date.toString());
				}

			}
			if (empLevelId > 0) {
				emp.setEmpLevelDetails(new EmployeeHierarchy(empLevelId));
			}
			session.update(emp);
			flag = true;
		}
		return flag;
	}

	/**
	 * @author Vinay B May 28, 2020 4:35:02 PM
	 * @param empList
	 * @param x
	 * @param branchId
	 * @return
	 */
	private EmployeeDetails getEmpObject(List<EmployeeDetails> empList, ResponseData x, int branchId) {
		EmployeeDetails emp = null;
		if (empList != null && !empList.isEmpty()) {

			LOGGER.info(" Employee list is not empty | list size -->  " + empList.size());

			/*
			 * emp = empList.stream().filter(Objects::nonNull).filter(obj ->
			 * obj.getCmpyEmpId() != null && obj.getCmpyEmpId().trim().equals(x.getEmpNo())
			 * && obj.getBranch().getBranchId() == branchId) .findAny().orElse(null);
			 */

			for (EmployeeDetails empObj : empList) {
				if (empObj != null && empObj.getBranch() != null && empObj.getCmpyEmpId() != null
						&& x.getEmpNo() != null && empObj.getBranch().getBranchId() > 0) {
					if (empObj.getCmpyEmpId().trim().equals(x.getEmpNo())
							&& empObj.getBranch().getBranchId() == branchId) {
						emp = empObj;
					}
				}
			}
		}
		return emp;
	}

	/**
	 * @author Vinay B May 28, 2020 4:25:27 PM
	 * @param session
	 * @return
	 */
	@SuppressWarnings("unchecked")
	private List<EmployeeDetails> getEmpList(Session session) {
		return session.createQuery("FROM EmployeeDetails").getResultList();
	}

	/**
	 * @author Vinay B May 28, 2020 2:32:37 PM
	 * @param emp
	 * @param x
	 * @param x
	 * @param session
	 * @param branchId
	 * @return
	 */
	private boolean deleteEmployeeDetails(EmployeeDetails emp, ResponseData x, Session session) {
		boolean flag = false;
		if (emp != null && emp.getEmpId() > 0) {

			// Inactive reason set to Default - CESSATION per Mahendra Magdum (DANA) request
			emp.setInactiveReason(EnovationConstants.INACTIVE_TYPE_CESSATION);
			emp.setIsDeactive(EnovationConstants.ONE);
			emp.setInactiveFrom(x.getInActiveFrom());
			session.update(emp);
			flag = true;

			/*
			 * String inactiveReason = getInactiveReason(x.getInactiveID()); if
			 * (inactiveReason != null) { emp.setInactiveReason(inactiveReason);
			 * emp.setIsDeactive(EnovationConstants.ONE);
			 * emp.setInactiveFrom(x.getInActiveFrom()); session.update(emp); flag = true; }
			 */
		}
		return flag;
	}

	/**
	 * @author Vinay B May 28, 2020 3:45:19 PM
	 * @param inactiveID
	 * @return
	 */
	private String getInactiveReason(int inactiveID) {
		String reason = null;
		if (inactiveID == EnovationConstants.INACTIVE_CODE_1) {
			reason = EnovationConstants.INACTIVE_TYPE_SUPERANNUATION;
		} else if (inactiveID == EnovationConstants.INACTIVE_CODE_2) {
			reason = EnovationConstants.INACTIVE_TYPE_RETIRED;
		} else if (inactiveID == EnovationConstants.INACTIVE_CODE_3) {
			reason = EnovationConstants.INACTIVE_TYPE_CESSATION;
		} else if (inactiveID == EnovationConstants.INACTIVE_CODE_4) {
			reason = EnovationConstants.INACTIVE_TYPE_DEATH;
		} else if (inactiveID == EnovationConstants.INACTIVE_CODE_5) {
			reason = EnovationConstants.INACTIVE_TYPE_PERMANENT_DISABILITY;
		}
		return reason;
	}

	/**
	 * @author Vinay B May 28, 2020 2:33:54 PM
	 * @param x
	 * @param branchDetails
	 * @return
	 */
	private int getBranchIdByLocationCode(ResponseData res, List<BranchMaster> branchDetails) {
		int branchId = 0;
		if (branchDetails != null && !branchDetails.isEmpty()) {

			LOGGER.info(" Branch list is not empty | branch list size -->  " + branchDetails.size());

			BranchMaster branch = branchDetails
					.stream().filter(Objects::nonNull).filter(x -> x.getLocationCode() != null
							&& res.getLocationCode() != null && x.getLocationCode().equals(res.getLocationCode()))
					.findAny().orElse(null);

			if (branch != null) {
				branchId = branch.getBranchId();
			}
		}
		return branchId;
	}

	/**
	 * @author Vinay B May 28, 2020 2:20:28 PM
	 * @param session
	 * @return
	 */
	private List<DepartmentMaster> getMasterDepartmentDetails(Session session) {
		return session.createQuery("FROM DepartmentMaster").getResultList();
	}

	/**
	 * @author Vinay B May 28, 2020 2:20:23 PM
	 * @param session
	 * @return
	 */
	private List<BranchMaster> getMasterBranchDetails(Session session) {
		return session.createQuery("FROM BranchMaster").getResultList();
	}

}
