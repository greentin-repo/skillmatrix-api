package com.greentin.enovation.employee;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Paths;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.regex.Pattern;

import javax.persistence.Tuple;
import javax.transaction.Transactional;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.usermodel.FormulaEvaluator;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.hibernate.Session;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.env.Environment;
import org.springframework.core.task.TaskExecutor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.greentin.enovation.accesscontrol.ProductOrgConfig;
import com.greentin.enovation.accesscontrol.Role;
import com.greentin.enovation.accesscontrol.RoleName;
import com.greentin.enovation.accesscontrol.Users;
import com.greentin.enovation.audit.AuditComponent;
import com.greentin.enovation.audit.EmployeeDetailsAudit;
import com.greentin.enovation.beans.ImportEmployeeErrorList;
import com.greentin.enovation.beans.SaveEmpRolesBean;
import com.greentin.enovation.beans.UpdateAPNKeyFCMKeyBean;
import com.greentin.enovation.communication.ICommunication;
import com.greentin.enovation.config.EnovationConfig;
import com.greentin.enovation.dto.ConcernListDTO;
import com.greentin.enovation.dto.EmployeeTransferDTO;
import com.greentin.enovation.dto.EnovationDTO;
import com.greentin.enovation.dto.MailDTO;
import com.greentin.enovation.dto.ProjectBO;
import com.greentin.enovation.dto.SugessionListDTO;
import com.greentin.enovation.master.MasterDaoImpl;
import com.greentin.enovation.model.BranchMaster;
import com.greentin.enovation.model.DepartmentMaster;
import com.greentin.enovation.model.EmailTemplateMaster;
import com.greentin.enovation.model.EmployeeDetails;
import com.greentin.enovation.model.EmployeeHierarchy;
import com.greentin.enovation.model.EmployeeTransfer;
import com.greentin.enovation.model.Feedback;
import com.greentin.enovation.model.FeedbackType;
import com.greentin.enovation.model.LanguageMaster;
import com.greentin.enovation.model.MoodIndicator;
import com.greentin.enovation.model.MultiBranchAccess;
import com.greentin.enovation.model.NoticeSetup;
import com.greentin.enovation.model.OrganizationMaster;
import com.greentin.enovation.model.SuggestionTemplate;
import com.greentin.enovation.model.TeamtypeMaster;
import com.greentin.enovation.model.dwm.Line;
import com.greentin.enovation.response.MasterResponse;
import com.greentin.enovation.utils.BaseRepository;
import com.greentin.enovation.utils.CommonUtils;
import com.greentin.enovation.utils.EncryptDecryptUtils;
import com.greentin.enovation.utils.EnovationConstants;
import com.greentin.enovation.utils.ExcelUtil;
import com.greentin.enovation.utils.MailUtil;
import com.greentin.enovation.utils.QueryConstants;
import com.greentin.enovation.utils.SmsUtil;
import com.greentin.enovation.utils.WriteFilesUtils;

@Component
@SuppressWarnings("unchecked")
@Transactional
public class EmployeeDaoImple extends BaseRepository implements IEmployeeDao {

	private static final Logger LOGGER = LoggerFactory.getLogger(EmployeeDaoImple.class);

	@Autowired
	AuditComponent audit;

	@Autowired
	MasterDaoImpl md;

	@Autowired
	Environment env;

	@Autowired
	@Qualifier("taskExecutor")
	private TaskExecutor taskExecutor;

	@Autowired
	EnovationConfig enoConfig;

	@Autowired
	PasswordEncoder password;

	@Autowired
	ICommunication communication;

	@Override
	public List<EmployeeDetails> loginApp(EmployeeDetails employeeDetails) {

		LOGGER.info("# INSIDE IN loginApp ");
		List<EmployeeDetails> empList = null;
		if (employeeDetails.getEmailId() != null && employeeDetails.getOrgId() != 0) {
			empList = getCurrentSession().createNativeQuery(
							"select * from tbl_employee_details where email_id=:email_id and org_id=:org_id and branch_id=:branch_id ")
					.setParameter("email_id", employeeDetails.getEmailId())
					// .setParameter("password", employeeDetails.getPassword())
					.setParameter("org_id", employeeDetails.getOrgId())
					.setParameter("branch_id", employeeDetails.getBranchId()).addEntity(EmployeeDetails.class)
					.getResultList();
		} else if (employeeDetails.getOrgId() != 0 && employeeDetails.getBranchId() != 0) {
			empList = getCurrentSession().createNativeQuery(
							"select * from tbl_employee_details where cmpy_emp_id=:cpmy_emp_id and org_id=:org_id and branch_id=:branch_id ")
					.setParameter("cpmy_emp_id", employeeDetails.getCmpyEmpId())
					// .setParameter("password", employeeDetails.getPassword())
					.setParameter("org_id", employeeDetails.getOrgId())
					.setParameter("branch_id", employeeDetails.getBranchId()).addEntity(EmployeeDetails.class)
					.getResultList();
		} else {
			empList = getCurrentSession()
					.createNativeQuery("select * from tbl_employee_details where email_id=:email_id")
					.setParameter("email_id", employeeDetails.getEmpId())
					// .setParameter("password", employeeDetails.getPassword())
					// .setParameter("org_id",employeeDetails.getOrgId())
					// .setParameter("branch_id",employeeDetails.getBranchId())
					.addEntity(EmployeeDetails.class).getResultList();
		}
		return empList;
	}

	@Override
	public List<TeamtypeMaster> checkEmpInWhichTeam(int empId) {
		LOGGER.info("# INSIDE IN checkEmpInWhichTeam ");
		List<TeamtypeMaster> empInTeamType = getCurrentSession().createNativeQuery(
						"Select t.* from tbl_employee_details ed,tbl_team_details td, master_teamtype t where td.emp_id=ed.emp_id AND td.team_type_id=t.team_type_id AND ed.emp_id=:empId  group by td.team_type_id  ")
				.addEntity(TeamtypeMaster.class).setParameter("empId", empId).getResultList();

		return empInTeamType;
	}

	@Override
	public List<EmployeeDetails> getAllEmplyeeDetails() {
		LOGGER.info("# INSIDE IN getAllEmplyeeDetails ");
		List<EmployeeDetails> empList = getCurrentSession().createQuery("from EmployeeDetails ").getResultList();
		return empList;
	}

	@SuppressWarnings("unused")
	@Override
	public HashMap<String, List<ImportEmployeeErrorList>> checkExcelFormat(String orgAlies, MultipartFile empExcel) {
		LOGGER.info("# INSIDE IN CHECKEXCELFORMAT ");
		HashMap<String, List<ImportEmployeeErrorList>> mapofexcel = new HashMap<String, List<ImportEmployeeErrorList>>();

		List<ImportEmployeeErrorList> errorList = new ArrayList<ImportEmployeeErrorList>();
		List<ImportEmployeeErrorList> checkList = new ArrayList<ImportEmployeeErrorList>();
		// System.out.println("initial errorList :" + errorList);

		// System.out.println("initial checkList :" + checkList);
		try {
			FileInputStream file;
			String picPath = null;

			String fileName = "EmployeeUploadFile1";
			/*
			 * WRITE EMPDETAILS EXCEL ON SERVER
			 */
			picPath = writeImportedEmpexcel(orgAlies, empExcel);
			// System.out.println("picpath : " + picPath);
			file = new FileInputStream(new File(picPath));
			/*
			 * File convFile = new File(System.getProperty("java.io.tmpdir")+"/"+fileName);
			 * empExcel.transferTo(convFile);
			 */
			@SuppressWarnings("resource")
			XSSFWorkbook workbook = new XSSFWorkbook(file);
			XSSFSheet sheet = workbook.getSheetAt(0);
			Row row;
			for (int i = 1; i <= sheet.getLastRowNum(); i++) {
				/*
				 * POINTS TO THE STARTING OF EXCEL I.E EXCEL FIRST ROW
				 */
				row = (Row) sheet.getRow(i); // sheet number
				String employeeId = null, firstName = null, lastName = null, department = null, email_id = null;
				String contactNo = null, designation = null;
				if (row.getCell(0) != null) {
					employeeId = row.getCell(0).toString();
				} else {
					employeeId = "";
				}
				if (row.getCell(1) != null) {
					firstName = row.getCell(1).toString();
				} else {
					firstName = "";
				} // else copies cell data to name variable
				if (row.getCell(2) != null) {
					lastName = row.getCell(2).toString();
				} else {
					lastName = "";
				}
				// System.out.println("scan emp :" + employeeId);
				if (row.getCell(3) != null) {
					email_id = row.getCell(3).toString();
				} else {
					email_id = "";
				}
				// System.out.println("scan email :" + email_id);
				if (row.getCell(4) != null) {
					contactNo = row.getCell(4).toString();
				} else {
					contactNo = "";
				}
				if (row.getCell(5) != null && row.getCell(5).toString() != "") {
					department = row.getCell(5).toString();
				}
				if (row.getCell(6) != null) {
					designation = row.getCell(6).toString();
				} else {
					designation = "";
				}
				ImportEmployeeErrorList checkDetails = null;
				/*
				 * System.out.println("employee lenght is :" + employeeId.length());
				 * System.out.println("firstName lenght is :" + firstName.length());
				 * System.out.println("lastName lenght is :" + lastName.length());
				 * System.out.println("email_id lenght is :" + email_id.length());
				 */

				int checkEmailId = email_id.indexOf('@');
				if (((email_id != null && email_id != "" && checkEmailId > 0) || (contactNo != null && contactNo != "")
						|| (employeeId != null && employeeId != "")) && firstName != null && firstName != "") {
					// System.out.println("empId :" + employeeId);
					checkDetails = new ImportEmployeeErrorList(employeeId, firstName, lastName, email_id, contactNo,
							department, designation);
					// System.out.println("checkDetails obj val is :" + checkDetails);
					checkList.add(checkDetails);
					// System.out.println("checkList list val is :" + checkList);

				}
				/*
				 * if (email_id == null || email_id == "") { checkDetails = new
				 * ImportEmployeeErrorList(employeeId, firstName, lastName, email_id, contactNo,
				 * department, EnovationConstants.EMAIL_BLANK, designation);
				 * errorList.add(checkDetails); } if (firstName == null || firstName == "") {
				 * checkDetails = new ImportEmployeeErrorList(employeeId, firstName, lastName,
				 * email_id, contactNo, department, EnovationConstants.FIRST_NAME_BLANK,
				 * designation); errorList.add(checkDetails); } if (contactNo == null ||
				 * contactNo == "") { checkDetails = new ImportEmployeeErrorList(employeeId,
				 * firstName, lastName, email_id, contactNo, department,
				 * EnovationConstants.CONTACT_NUMBER_BLANK, designation);
				 * errorList.add(checkDetails); } if (employeeId.length() >
				 * EnovationConstants.EMPLOYEEID_LENGHT_SIZE) { checkDetails = new
				 * ImportEmployeeErrorList(employeeId, firstName, lastName, email_id, contactNo,
				 * department, EnovationConstants.EMPLOYEEID_LENGHT_EXCEEDS, designation);
				 * errorList.add(checkDetails); }
				 */
				System.out.println("Name =" + firstName);
				System.out.println("department =" + department);
				if (firstName != null || lastName != null || designation != null || department != null) {
					if (firstName.length() > EnovationConstants.IMPORT_DATA_LENGHT_SIZE
							|| lastName.length() > EnovationConstants.IMPORT_DATA_LENGHT_SIZE
							|| designation.length() > EnovationConstants.IMPORT_DATA_LENGHT_SIZE
							|| department.length() > EnovationConstants.IMPORT_DATA_LENGHT_SIZE) {
						checkDetails = new ImportEmployeeErrorList(employeeId, firstName, lastName, email_id, contactNo,
								department, EnovationConstants.IMPORT_DATA_LENGHT_EXCEEDS, designation);
						errorList.add(checkDetails);
					}
				}
				if (contactNo.length() > 4) {
					if (contactNo.length() > EnovationConstants.MOBILE_NUMBER_LENGHT_SIZE
							|| !contactNo.matches("[0-9]+")) {
						checkDetails = new ImportEmployeeErrorList(employeeId, firstName, lastName, email_id, contactNo,
								department, EnovationConstants.INVALID_MOBILE_NUMBER, designation);
						errorList.add(checkDetails);
					}
				}

			}
			file.close();
			// System.out.println("errorList :" + errorList);
			// System.out.println("checkList :" + checkList);
			mapofexcel.put("checkList", checkList);
			mapofexcel.put("errorList", errorList);

		} catch (Exception e) {
			e.printStackTrace();
		}
		return mapofexcel;
	}

	@Override
	public List<EmployeeDetails> retriveAllEmpDetFromExcel(Integer orgId, String orgName, MultipartFile empExcel) {/// pending
		LOGGER.info("# INSIDE IN retriveAllEmpDetFromExcel ");
		List<EmployeeDetails> AllEmpList = new ArrayList<>();

		FileInputStream file;
		try {
			String picPath = null;
			picPath = writeImportedEmpexcel(orgName, empExcel); // write empDetails excel on server
			LOGGER.info("file path : " + picPath);
			File file1 = new File(picPath);
			file = new FileInputStream(file1);
			if (file1.isFile() && file1.exists()) {
				System.out.println("openworkbook.xlsx file open successfully.");
			} else {
				System.out.println("Error to open openworkbook.xlsx file.");
			}
			@SuppressWarnings("resource")
			XSSFWorkbook workbook = new XSSFWorkbook(file);
			XSSFSheet sheet = workbook.getSheetAt(0);
			Row row;
			boolean verifyDeptAndOrg = checkDeptANDOrgfromExcel(sheet);
			if (verifyDeptAndOrg == true) {
				for (int i = 1; i <= sheet.getLastRowNum(); i++) {
					String pass = String.valueOf(CommonUtils.GenerateRandomPassword(8));// points to the starting of
					// excel i.e excel first row
					row = (Row) sheet.getRow(i); // sheet number
					@SuppressWarnings("unused")
					String id = null, firstName = null, lastName = null, designation = null, address = null,
							dept = null, organization = null, email_id = null, branchName = null;
					String contactNo = null;
					id = row.getCell(0).toString();
					firstName = row.getCell(1).toString(); // else copies cell data to name variable
					lastName = row.getCell(2).toString();
					contactNo = row.getCell(3).toString();
					address = row.getCell(4).toString();
					dept = row.getCell(5).toString();
					organization = row.getCell(6).toString();
					email_id = row.getCell(7).toString();
					designation = row.getCell(8).toString();
					branchName = row.getCell(9).toString();
					if (firstName != null || firstName != "") {
						List<Object[]> department = getCurrentSession()
								.createNativeQuery("Select dept_id from master_department Where dept_name=:dept")
								.setParameter("dept", dept).getResultList();
						List<Object[]> oranization = getCurrentSession()
								.createNativeQuery("Select org_id from master_organization Where name=:org")
								.setParameter("org", organization).getResultList();
						// List<Integer> designationList=getCurrentSession().createQuery("Select desigId
						// from DesignationMaster Where desigName=:desigName").setParameter("desigName",
						// designation).getResultList();
						List<Object[]> branchList = getCurrentSession()
								.createNativeQuery("Select branch_id from master_branch Where location=:name")
								.setParameter("name", branchName).getResultList();

						EmployeeDetails std = new EmployeeDetails();
						std.setCmpyEmpId(id);
						std.setContactNo(contactNo);
						std.setFirstName(firstName);
						std.setLastName(lastName);
						std.setEmailId(email_id);
						std.setPassword(pass);
						// std.setDesignation(new DesignationMaster(designationList.get(0)));
						// sendMailToImportedEmployees(email_id,firstName,lastName,pass);
						if (department != null && department.size() > 0) {
							for (Object[] departmnetId : department) {
								std.setDept(new DepartmentMaster(Integer.valueOf(departmnetId[0].toString())));
							}
						}
						if (oranization != null && oranization.size() > 0) {
							for (Object[] oranizationId : oranization) {
								std.setOrganization(
										new OrganizationMaster(Integer.valueOf(oranizationId[0].toString())));
							}
						}

						if (branchList != null && branchList.size() > 0) {
							for (Object[] branchId : branchList) {
								std.setBranch(new BranchMaster(Integer.valueOf(branchId[0].toString())));
							}
						}
						std.setAddress(address);
						AllEmpList.add(std);
					}
					file.close();
				}
			}
		} catch (Exception e) {
			LOGGER.info("# INSIDE IN EXCPETION IN retriveAllEmpDetFromExcel METHOD : " + e.getMessage());
			return AllEmpList;
		}
		return AllEmpList;
	}

	public String writeImportedEmpexcel(String orgName, MultipartFile empExcel) throws IOException {
		LOGGER.info("# INSIDE IN writeImportedEmpexcel ");
		String picPath = null;
		if (empExcel != null) {
			String filenameOriginal = empExcel.getOriginalFilename();
			String filename = filenameOriginal;
			// String directory ="C:\\Users\\Admin\\Documents\\";
			String directory = EnovationConfig.buddyConfig.get("excelUploadPath") + "/" + orgName;
			System.out.println(directory);
			File dire = new File(directory);
			if (!dire.exists()) {
				dire.mkdirs();
			}
			picPath = Paths.get(directory, filename).toString();
			/*
			 * File convFile = new File(picPath); empExcel.transferTo(convFile);
			 */
			BufferedOutputStream stream = new BufferedOutputStream(new FileOutputStream(new File(picPath)));
			stream.write(empExcel.getBytes());
			stream.close();
		}
		return picPath;
	}

	@SuppressWarnings("unused")
	private boolean checkDeptANDOrgfromExcel(XSSFSheet sheet) {
		LOGGER.info("# INSIDE IN checkDeptANDOrgfromExcel ");
		Session session = getCurrentSession();
		try {
			Row row;
			for (int i = 1; i <= sheet.getLastRowNum(); i++) {
				row = (Row) sheet.getRow(i);
				String dept = null, organization = null, designation = null, branchName = null;
				dept = row.getCell(5).toString();
				organization = row.getCell(6).toString();
				designation = row.getCell(8).toString();
				branchName = row.getCell(9).toString();
				List<Object[]> department = getCurrentSession()
						.createNativeQuery("Select dept_id from master_department Where dept_name=:dept")
						.setParameter("dept", dept).getResultList();
				List<Object[]> oranization = getCurrentSession()
						.createNativeQuery("Select org_id from master_organization Where name=:org")
						.setParameter("org", organization).getResultList();
				// List<Integer> designationList=getCurrentSession().createQuery("Select desigId
				// from DesignationMaster Where desigName=:desigName").setParameter("desigName",
				// designation).getResultList();
				List<Object[]> branchList = getCurrentSession()
						.createNativeQuery("Select branch_id from master_branch Where location=:name")
						.setParameter("name", branchName).getResultList();
				if (department != null && department.size() > 0) {
					for (Object[] departmnetId : department) {
						DepartmentMaster deptmstr = new DepartmentMaster();
						deptmstr.setDeptId(Integer.valueOf(departmnetId[0].toString()));
					}
				}

				if (oranization != null && oranization.size() > 0) {
					for (Object[] oranizationId : oranization) {
						OrganizationMaster orgmstr = new OrganizationMaster();
						orgmstr.setOrgId(Integer.valueOf(oranizationId[0].toString()));
					}
				}
				// DesignationMaster desig=new DesignationMaster();
				// desig.setDesigId(designationList.get(0));
				if (branchList != null && branchList.size() > 0) {
					for (Object[] branchId : branchList) {
						BranchMaster brch = new BranchMaster();
						brch.setBranchId(Integer.valueOf(branchId[0].toString()));
					}
				}
			}
			return true;
		} catch (Exception e) {
			LOGGER.info("# INSIDE EXCPTION OCCURED IN checkDeptANDOrgfromExcel " + e.getMessage());
			return false;
		}
	}

	@Override
	public boolean checkEmpIdsfromExcel(List<EmployeeDetails> empAllDetails) {
		LOGGER.info("# INSIDE IN checkDeptANDOrgfromExcel ");
		boolean flag = false;
		List<Object[]> empDet = null;
		for (EmployeeDetails empDetails : empAllDetails) {
			LOGGER.info("# INSIDE IN findEmp ");
			empDet = getCurrentSession()
					.createNativeQuery("select * from employee_details where cmpy_emp_id=:cmpyEmpId ")
					.setParameter("cmpyEmpId", empDetails.getCmpyEmpId()).getResultList();
			if (empDet == null) {
				flag = true;
			} else {
				flag = false;
			}

		}
		// List<EmployeeDetails>
		// emp=getCurrentSession().createNativeQuery("").getResultList()
		// if(emp!=null) {return true;}else {return false;}
		return flag;
	}

	@Override
	public boolean SaveAllEmpExcel(Integer empId, List<EmployeeDetails> empAllDetails) {
		boolean flag = false;
		LOGGER.info("#INSIDE IN SaveAllEmpExcel METHOD : ");
		try {
			for (EmployeeDetails empDet : empAllDetails) {
				sendMailToImportedEmployees(empDet.getEmailId(), empDet.getFirstName(), empDet.getLastName(),
						empDet.getPassword(), EnovationConstants.EMPLOYEE);
				empDet.setPassword(password.encode(empDet.getPassword()));
				empDet.setCreatedBy((int) empId);
				empDet.setCreatedDate(CommonUtils.currentDate());
				getCurrentSession().save(empDet);
			}
			flag = true;
		} catch (Exception e) {
			LOGGER.info("#INSIDE EXCEPTION OCCURED IN SaveAllEmpExcel METHOD : " + e.getMessage());
		}
		return flag;
	}

	public void sendMailToImportedEmployees(String email_id, String firstName, String lastName, String token,
											List<String> rolesList, String CONDITION) {
		LOGGER.info("#INSIDE IN SENDMAILTOIMPORTEDEMPLOYEES");
		String verifyLink = null, roles = null, role = null, newRemove = null;
		EmailTemplateMaster messageContent = null;
		// if(rolesList.size()>0) {roles=StringUtils.join(rolesList, ',');}else
		// {roles="";}
		if (CONDITION == EnovationConstants.SEND_MAIL_TO_CONTROLLER) {
			messageContent = enoConfig.getMessageContent(EnovationConstants.sendEmailNewRegistedEmpSubject);
			role = (RoleName.CONTROLLER).toString();

			newRemove = EnovationConstants.NEW;
			messageContent = enoConfig.getMessageContent(EnovationConstants.SEND_MAIL_TO_CONTROLLER);
		} else if (CONDITION == EnovationConstants.SEND_MAIL_TO_REMOVE_CONTROLLER) {
			messageContent = enoConfig.getMessageContent(EnovationConstants.sendEmailNewRegistedEmpSubject);
			messageContent = enoConfig.getMessageContent(EnovationConstants.SEND_MAIL_TO_CONTROLLER);
			newRemove = EnovationConstants.REMOVE;
		}

		EmailTemplateMaster emailTemplateImg = enoConfig.getMessageContent(EnovationConstants.EmailTemplateImg);

		String mailContent = null, subject = messageContent.getName();
		if (messageContent.getBody() != null && messageContent.getBody().contains("{name}")) {
			mailContent = messageContent.getBody().replaceAll(Pattern.quote("{name}"), firstName + " " + lastName)
					.replaceAll(Pattern.quote("{vlink}"), verifyLink + token)
					.replaceAll(Pattern.quote("{email}"), email_id)
					.replaceAll(Pattern.quote("{bckImg}"),
							String.valueOf(EnovationConfig.buddyConfig.get("ProfilePicPathUrl")
									+ emailTemplateImg.getFcmBody()))
					.replaceAll(Pattern.quote("{roles}"), String.valueOf(roles))
					.replaceAll(Pattern.quote("{role}"), String.valueOf(role))
					.replaceAll(Pattern.quote("{newRemove}"), newRemove)
					.replaceAll(Pattern.quote("{appLink}"), messageContent.getDesc());
			// .replaceAll(Pattern.quote("{playstoreicon}"),String.valueOf(EnovationConfig.buddyConfig.get("ProfilePicPathUrl")+messageContent.getDesc()));
		}
		if (email_id != null) {
			taskExecutor.execute(new MailUtil(email_id, subject, mailContent, communication));
		}
	}

	public void sendMailToImportedEmployees(String email_id, String firstName, String roleNames, String token,
											String CONDITION) {
		LOGGER.info("#INSIDE IN SENDMAILTOIMPORTEDEMPLOYEES ");
		String verifyLink = null, role = null, newRemove = null, superAdmin = "";
		String prtalLink = "";
		EmailTemplateMaster messageContent = null;
		if (CONDITION == EnovationConstants.REGISTER) {
			if (firstName.indexOf(' ') >= 0) {
				firstName = firstName.substring(0, firstName.indexOf(' '));
			}

			verifyLink = String.valueOf(EnovationConfig.buddyConfig.get("verifyEmailLink"));
			prtalLink = EnovationConfig.buddyConfig.get("masterDomain").replaceAll(Pattern.quote("{alies}"), roleNames);
			messageContent = enoConfig.getMessageContent(EnovationConstants.sendEmailNewRegistedEmpSubject);
		} else if (CONDITION == EnovationConstants.EMAIL_CHANGE_VERIFICATION) {
			prtalLink = getSendEmailDomain(email_id);
			verifyLink = prtalLink + String.valueOf(EnovationConfig.buddyConfig.get("verifyEmployeeEmailLink"));
			messageContent = enoConfig.getMessageContent(EnovationConstants.EMAIL_CHANGE_VERIFICATION);
		} else if (CONDITION == EnovationConstants.SEND_MAIL_TO_CONTROLLER) {
			prtalLink = getSendEmailDomain(email_id);
			// messageContent =
			// enoConfig.getMessageContent(EnovationConstants.sendEmailNewRegistedEmpSubject);
			role = (RoleName.CONTROLLER).toString();

			newRemove = EnovationConstants.NEW;
			messageContent = enoConfig.getMessageContent(EnovationConstants.SEND_MAIL_TO_CONTROLLER);
		} else if (CONDITION == EnovationConstants.SEND_MAIL_TO_REMOVE_CONTROLLER) {
			prtalLink = getSendEmailDomain(email_id);
			role = (RoleName.CONTROLLER).toString();
			// messageContent =
			// enoConfig.getMessageContent(EnovationConstants.sendEmailNewRegistedEmpSubject);
			newRemove = EnovationConstants.REMOVE;
			messageContent = enoConfig.getMessageContent(EnovationConstants.SEND_MAIL_TO_CONTROLLER);

		} else if (CONDITION == EnovationConstants.FORGOT_PASSWORD) {
			prtalLink = getSendEmailDomain(email_id);
			verifyLink = prtalLink + String.valueOf(EnovationConfig.buddyConfig.get("verifyEmployeeEmailLink"));
			messageContent = enoConfig.getMessageContent(EnovationConstants.FORGOT_PASSWORD);
		} else if (CONDITION == EnovationConstants.EMPLOYEE_VERIFIED) {
			superAdmin = getSuperAdminName(email_id);
			System.out.println("email ID :" + email_id);
			prtalLink = getSendEmailDomain(email_id);
			// verifyLink =
			// String.valueOf(EnovationConfig.buddyConfig.get("verifyEmployeeEmailLink"));
			messageContent = enoConfig.getMessageContent(EnovationConstants.NEW_REGISTRATION_TEMPLATE_PASSWORD);

		} else { // for employee
			superAdmin = getSuperAdminName(email_id);
			System.out.println("email ID :" + email_id);
			prtalLink = getSendEmailDomain(email_id);
			verifyLink = prtalLink + String.valueOf(EnovationConfig.buddyConfig.get("verifyEmployeeEmailLink"));
			messageContent = enoConfig.getMessageContent(EnovationConstants.sendEmailNewRegistedEmpSubject);
		}

		// String portalLink=
		// EnovationConfig.buddyConfig.get("masterDomain").replaceAll(Pattern.quote("{alies}"),
		// empAlies);
		EmailTemplateMaster emailTemplateImg = enoConfig.getMessageContent(EnovationConstants.EmailTemplateImg);

		String mailContent = null, subject = messageContent.getSubject();
		String fname = "";
		String roleName = "";
		if (firstName != null) {
			fname = firstName;
		}
		if (roleNames != null) {
			roleName = roleNames;
		}
		if (messageContent.getBody() != null && messageContent.getBody().contains("{name}")) {
			mailContent = messageContent.getBody().replaceAll(Pattern.quote("{name}"), fname.trim())
					.replaceAll(Pattern.quote("{vlink}"), verifyLink + token)
					.replaceAll(Pattern.quote("{email}"), email_id)
					.replaceAll(Pattern.quote("{password}"), EnovationConstants.DEFAULT_PASSWORD)
					.replaceAll(Pattern.quote("{bckImg}"),
							String.valueOf(EnovationConfig.buddyConfig.get("ProfilePicPathUrl")
									+ emailTemplateImg.getFcmBody()))
					.replaceAll(Pattern.quote("{role}"), String.valueOf(role))
					.replaceAll(Pattern.quote("{new}"), newRemove)
					.replaceAll(Pattern.quote("{appLink}"), EnovationConfig.buddyConfig.get("appLink"))
					.replaceAll(Pattern.quote("{appLinkIos}"), EnovationConfig.buddyConfig.get("appLinkIos"))
					.replaceAll(Pattern.quote("{superAdminName}"), superAdmin)
					.replaceAll(Pattern.quote("{portalLink}"), prtalLink)
					.replaceAll(Pattern.quote("{roles}"), roleName);

			// .replaceAll(Pattern.quote("{playstoreicon}"),String.valueOf(EnovationConfig.buddyConfig.get("")+messageContent.getDesc()));
		}
		if (email_id != null) {
			System.out.println("mail sending....");
			taskExecutor.execute(new MailUtil(email_id, subject, mailContent, communication));
		}
	}

	public MailDTO sendMailToBulkImportedEmployees(String email_id, String firstName, String roleNames, String token,
												   String CONDITION) {
		LOGGER.info("#INSIDE IN SENDMAILTOIMPORTEDEMPLOYEES ");
		String verifyLink = null, role = null, newRemove = null, superAdmin = "";
		String prtalLink = "";
		EmailTemplateMaster messageContent = null;
		if (CONDITION == EnovationConstants.EMPLOYEE_VERIFIED) {
			System.out.println("email ID :" + email_id);
			if (email_id != null) {
				superAdmin = getSuperAdminName(email_id);
				prtalLink = getSendEmailDomain(email_id);
			}
			// verifyLink =
			// String.valueOf(EnovationConfig.buddyConfig.get("verifyEmployeeEmailLink"));
			messageContent = enoConfig.getMessageContent(EnovationConstants.NEW_REGISTRATION_TEMPLATE_PASSWORD);
		} else {// for employee
			System.out.println("email ID :" + email_id);
			if (email_id != null) {
				superAdmin = getSuperAdminName(email_id);
				prtalLink = getSendEmailDomain(email_id);
			}
			verifyLink = prtalLink + String.valueOf(EnovationConfig.buddyConfig.get("verifyEmployeeEmailLink"));
			messageContent = enoConfig.getMessageContent(EnovationConstants.sendEmailNewRegistedEmpSubject);
		}

		// String portalLink=
		// EnovationConfig.buddyConfig.get("masterDomain").replaceAll(Pattern.quote("{alies}"),
		// empAlies);
		EmailTemplateMaster emailTemplateImg = enoConfig.getMessageContent(EnovationConstants.EmailTemplateImg);

		String mailContent = null, subject = messageContent.getSubject();
		String fname = "";
		String roleName = "";
		if (firstName != null) {
			fname = firstName;
		}
		if (roleNames != null) {
			roleName = roleNames;
		}
		if (email_id != null) {
			if (messageContent.getBody() != null && messageContent.getBody().contains("{name}")) {
				mailContent = messageContent.getBody().replaceAll(Pattern.quote("{name}"), fname.trim())
						.replaceAll(Pattern.quote("{vlink}"), verifyLink + token)
						.replaceAll(Pattern.quote("{email}"), email_id)
						.replaceAll(Pattern.quote("{bckImg}"),
								String.valueOf(EnovationConfig.buddyConfig.get("ProfilePicPathUrl")
										+ emailTemplateImg.getFcmBody()))
						.replaceAll(Pattern.quote("{role}"), String.valueOf(role))
						.replaceAll(Pattern.quote("{new}"), newRemove)
						.replaceAll(Pattern.quote("{password}"), EnovationConstants.DEFAULT_PASSWORD)
						.replaceAll(Pattern.quote("{appLink}"), EnovationConfig.buddyConfig.get("appLink"))
						.replaceAll(Pattern.quote("{appLinkIos}"), EnovationConfig.buddyConfig.get("appLinkIos"))
						.replaceAll(Pattern.quote("{superAdminName}"), superAdmin)
						.replaceAll(Pattern.quote("{portalLink}"), prtalLink)
						.replaceAll(Pattern.quote("{roles}"), roleName);

				// .replaceAll(Pattern.quote("{playstoreicon}"),String.valueOf(EnovationConfig.buddyConfig.get("ProfilePicPathUrl")+messageContent.getDesc()));
			}
		}
		MailDTO mail = null;
		if (email_id != null) {
			mail = new MailDTO(email_id, subject, mailContent);
			// taskExecutor.execute(new MailUtil(email_id, subject, mailContent));
		}
		return mail;
	}

	public String getSuperAdminName(String email_id) {
		List<EmployeeDetails> emailOrg = getCurrentSession().createQuery("from EmployeeDetails where emailId=:email")
				.setParameter("email", email_id).getResultList();
		if (emailOrg != null && emailOrg.size() > 0) {
			if (emailOrg.get(0).getCreatedBy() != null) {
				List<EmployeeDetails> empl = getCurrentSession().createQuery("from EmployeeDetails where empId=:empId")
						.setParameter("empId", emailOrg.get(0).getCreatedBy()).getResultList();
				return empl.get(0).getFirstName();
			}
		}
		return "";
	}

	private String getSendEmailDomain(String email_id) {
		List<EmployeeDetails> emailOrg = getCurrentSession().createQuery("from EmployeeDetails where emailId=:email")
				.setParameter("email", email_id).getResultList();
		System.out.println("list size is :" + emailOrg.size());
		if (emailOrg != null && emailOrg.size() > 0) {
			OrganizationMaster org = (OrganizationMaster) getCurrentSession().get(OrganizationMaster.class,
					emailOrg.get(0).getOrganization().getOrgId());
			System.out.println("Portal Link :" + org.getPortalLink());
			return org.getPortalLink();
		} else {
			return "";
		}
	}

	@SuppressWarnings("unused")
	public void sendMailTo_ROLE_CHANGED_Employees(String email_id, String firstName, String lastName, String roleName,
												  String CONDITION) {

		LOGGER.info("#INSIDE IN SENDMAILTOIMPORTEDEMPLOYEES ");
		String newRemove = null;
		;
		EmailTemplateMaster messageContent = null;
		if (CONDITION == EnovationConstants.ROLE_ADDED) {
			// messageContent =
			// enoConfig.getMessageContent(EnovationConstants.sendEmailNewRegistedEmpSubject);
			newRemove = EnovationConstants.NEW;
			messageContent = enoConfig.getMessageContent(EnovationConstants.SEND_MAIL_TO_CONTROLLER);
		} else if (CONDITION == EnovationConstants.ROLE_REMOVED) {
			newRemove = EnovationConstants.REMOVE;
			messageContent = enoConfig.getMessageContent(EnovationConstants.SEND_MAIL_TO_CONTROLLER);
		}
		EmailTemplateMaster emailTemplateImg = enoConfig.getMessageContent(EnovationConstants.EmailTemplateImg);

		String mailContent = null, subject = messageContent.getSubject();
		String fname = "";
		String lname = "";
		if (firstName != null) {
			fname = firstName;
		}
		if (lastName != null) {
			lname = lastName;
		}
		String prtalLink = getSendEmailDomain(email_id);
		if (messageContent.getBody() != null && messageContent.getBody().contains("{name}")) {
			mailContent = messageContent.getBody().replaceAll(Pattern.quote("{name}"), fname)
					.replaceAll(Pattern.quote("{email}"), email_id)
					.replaceAll(Pattern.quote("{bckImg}"),
							String.valueOf(EnovationConfig.buddyConfig.get("ProfilePicPathUrl")
									+ emailTemplateImg.getFcmBody()))
					.replaceAll(Pattern.quote("{role}"), String.valueOf(roleName))
					.replaceAll(Pattern.quote("{new}"), newRemove)
					.replaceAll(Pattern.quote("{appLink}"), messageContent.getDesc())
					.replaceAll(Pattern.quote("{portalLink}"), prtalLink);
		}
		if (email_id != null) {
			System.out.println("mail sending....");
			taskExecutor.execute(new MailUtil(email_id, subject, mailContent, communication));
		}
	}

	@Override
	public List<EmployeeDetails> getEmpTeamDetails(Integer branchId, Integer teamTypeId) {
		LOGGER.info("# INSIDE IN getEmpTeamDetails ");
		List<EmployeeDetails> empDetTeamWise = null;
		empDetTeamWise = getCurrentSession()
				.createNativeQuery("Select e.* from tbl_employee_details e, tbl_team_details t ,master_team m "
						+ " where t.emp_id=e.emp_id " + " and m.teammaster_id=t.team_master_id "
						+ " and t.team_type_id= :teamTypeId " + " and m.branch_id=:branchId " + " group by t.emp_id ")
				.addEntity(EmployeeDetails.class).setParameter("teamTypeId", teamTypeId)
				.setParameter("branchId", branchId).getResultList();
		return empDetTeamWise;
	}

	@Override
	public List<EmployeeDetails> getEmpTeamList() {
		LOGGER.info("# INSIDE IN getEmpTeamList ");
		List<EmployeeDetails> empDetTeamWise = null;
		empDetTeamWise = getCurrentSession().createNativeQuery(
						"Select e.* from tbl_employee_details e join tbl_team_details t on t.emp_id=e.emp_id and e.is_de_active=:isDeactive group by t.emp_id")
				.addEntity(EmployeeDetails.class).setParameter("isDeactive", EnovationConstants.DESABLE_STATUS)
				.getResultList();
		return empDetTeamWise;
	}

	@Override
	public String updateProfilePic(Integer empId, MultipartFile profilePic) {
		LOGGER.info("# INSIDE IN updateProfilePic ");
		String flag = null;
		try {
			EmployeeDetails empDet = (EmployeeDetails) getCurrentSession().get(EmployeeDetails.class, empId);
			String picPath = null;
			String prevFilePath = null;
			if (empDet.getProfilePic() != null) {
				prevFilePath = EnovationConfig.buddyConfig.get("profilePicRemovePath") + empDet.getProfilePic();
			}
			String picDirectory = EnovationConfig.buddyConfig.get("profilePicUploadPath") + "/" + empId;
			String fileToTrim = String.valueOf(empId);
			picPath = WriteFilesUtils.updateWriteFileOnServer(profilePic, picDirectory, prevFilePath, fileToTrim);
			if (picPath != null) {
				getCurrentSession().createQuery("update EmployeeDetails set profilePic=:picPath where empId=:empId")
						.setParameter("empId", empId).setParameter("picPath", picPath).executeUpdate();
				flag = picPath;
			}
		} catch (Exception e) {
			LOGGER.info("# INSIDE EXCPTION OCCURED IN updateProfilePic " + e.getMessage());

		}
		return flag;
	}

	@Override
	public boolean updateProfileDetails(EmployeeDetails empDetails) {
		LOGGER.info("# INSIDE IN updateProfileDetails ");
		Session session = getCurrentSession();
		try {

			EmployeeDetails empDet = (EmployeeDetails) getCurrentSession().get(EmployeeDetails.class,
					empDetails.getEmpId());
			if (empDetails.getFirstName() != null) {
				empDet.setFirstName(empDetails.getFirstName());
			}
			if (empDetails.getLastName() != null) {
				empDet.setLastName(empDetails.getLastName());
			}
			if (empDetails.getContactNo() != null) {
				empDet.setContactNo(empDetails.getContactNo());
			}
			if (empDetails.getAddress() != null) {
				empDet.setAddress(empDetails.getAddress());
			}
			if (empDetails.getEmailId() != null) {
				empDet.setEmailId(empDetails.getEmailId());
			}
			if (empDetails.getLangId() > 0) {
				empDet.setLangName(new LanguageMaster(empDetails.getLangId()));
			}
			if (empDetails.getSetActiveDate() == EnovationConstants.ONE) {
				empDet.setLastActiveDate(CommonUtils.currentDate());
			}
			if (empDetails.getDOB() != null) {
				empDet.setDOB(empDetails.getDOB());
			}
			if (empDetails.getDOA() != null) {
				empDet.setDOA(empDetails.getDOA());
			}

			if (empDetails.getFcmKey() != null) {
				empDet.setFcmKey(empDetails.getFcmKey());
			}
			empDet.setUpdatedDate(CommonUtils.currentDate());
			session.update(empDet);

			return true;

		} catch (Exception e) {
			LOGGER.info("# INSIDE EXCPTION OCCURED IN updateProfileDetails " + e.getMessage());
			return false;
		}
	}

	@Override
	public boolean updateAPNKey(UpdateAPNKeyFCMKeyBean aPNKeyDet) {
		LOGGER.info("# INSIDE IN updateAPNKey ");
		try {
			getCurrentSession().createQuery("update EmployeeDetails set apnKey=:apnKey where empId=:empId")
					.setParameter("empId", aPNKeyDet.getEmpId()).setParameter("apnKey", aPNKeyDet.getApnKey())
					.executeUpdate();
			return true;
		} catch (Exception e) {
			return false;
		}
	}

	@Override
	public boolean isContactExist(EmployeeDetails empDetails) {
		LOGGER.info("# INSIDE IN isContactExiste ");
		boolean flag = false;
		List<EmployeeDetails> empDet = getCurrentSession()
				.createQuery("from EmployeeDetails where contactNo=:contactNo ")
				.setParameter("contactNo", empDetails.getContactNo()).getResultList();
		if (empDet.size() == 0) {
			flag = true;
		} else {
			flag = false;
		}
		return flag;
	}

	@Override
	public boolean isEmailExist(EmployeeDetails empDetails) {
		LOGGER.info("# INSIDE IN isEmailExist at createEmployee method=" + empDetails.getEmailId());
		boolean flag = false;
		List<Object[]> user = getCurrentSession()
				// .createNativeQuery(" select emp_id,first_name from tbl_employee_details where
				// (email_id =:emailId or contact_no=:contactNo) and is_deactive=0 ")
				.createNativeQuery(
						" select emp_id,first_name from tbl_employee_details where email_id =:emailId  and is_deactive=0 ")
				// .setParameter("contactNo", empDetails.getContactNo())
				.setParameter("emailId", empDetails.getEmailId()).getResultList();
		if (user.isEmpty() && user.size() == 0) {
			flag = true;
		} else {
			flag = false;
		}
		return flag;
	}

	public List<Users> isUserEmailExist(EmployeeDetails empDetails) {
		LOGGER.info("# INSIDE IN FINDEMP IN USERS ");
		List<Users> user = null;
		try {
			user = getCurrentSession().createQuery("from Users where email =:emailId ")
					.setParameter("emailId", empDetails.getEmailId()).getResultList();
		} catch (Exception e) {
			LOGGER.info("#INSIDE EXCEPTION OCCURED  IN FINDEMP IN USERS ");
		}
		return user;
	}

	@Override
	public boolean isEmployeeIDExist(EmployeeDetails empDetails) {
		LOGGER.info("# INSIDE IN isEmployeeIDExist");
		List<EmployeeDetails> empDet = new ArrayList<>();
		boolean flag = false;
		empDet = getCurrentSession().createQuery("from EmployeeDetails where cmpyEmpId=:cmpyEmpId and isDeactive=0")
				.setParameter("cmpyEmpId", empDetails.getCmpyEmpId()).getResultList();
		if (empDet.isEmpty()) {
			flag = true;
		}
		return flag;
	}

	@Override
	public boolean saveEmployee(EmployeeDetails empDetails) {
		LOGGER.info("# INSIDE IN saveEmployee ");
		boolean flag = false;
		final String pass = String.valueOf(CommonUtils.GenerateRandomPassword(8));
		empDetails.setPassword(password.encode(pass));
		empDetails.setCreatedDate(CommonUtils.currentDate());
		empDetails.setEmpType(EnovationConstants.EMPLOYEE);
		Object obj = getCurrentSession().save(empDetails);
		if (obj != null) {
			sendMailToImportedEmployees(empDetails.getEmailId(), empDetails.getFirstName(), empDetails.getLastName(),
					pass, EnovationConstants.EMPLOYEE);
			flag = true;
		} else {
			flag = false;
		}
		return flag;
	}

	@Override
	public EmployeeDetails getEmployeeByEmpId(Integer empId) {

		// Commented By Vinay B. 23.03.2021
		/*
		 * LOGGER.info("# INSIDE IN getEmployeeByEmpId ");
		 *
		 * List<EmployeeDetails> empDet = getCurrentSession()
		 * .createQuery("from EmployeeDetails where empId=:empId and isDeactive=:isDeactive"
		 * ) .setParameter("empId", empId).setParameter("isDeactive",
		 * EnovationConstants.DESABLE_STATUS) .getResultList(); return empDet.get(0);
		 */

		LOGGER.info("# INSIDE IN getEmployeeByEmpId ");
		Session session = getCurrentSession();
		List<EmployeeDetails> empDet = session
				.createQuery("from EmployeeDetails where empId=:empId and isDeactive=:isDeactive")
				.setParameter("empId", empId).setParameter("isDeactive", EnovationConstants.DESABLE_STATUS)
				.getResultList();
		if (empDet != null && !empDet.isEmpty()) {

			// Added by Vinay B. 09-01-2021 - as per PMS requirement

			System.out.println("Employee Id ==> " + empDet.get(0).getEmpId());

			EmployeeDetails ed = empDet.get(0);


			return ed;
		} else {
			EmployeeDetails blank = new EmployeeDetails();
			return blank;
		}
	}

	@Override
	public boolean removeEmployee(EmployeeDetails empdetails) {
		LOGGER.info("# INSIDE IN removeEmployee ");

		boolean flag = false;
		try {
			Session session = getCurrentSession();
			EmployeeDetails empDet = (EmployeeDetails) getCurrentSession().get(EmployeeDetails.class,
					empdetails.getEmpId());
			empDet.setIsDeactive(empdetails.getIsDeactive());

			session.update(empDet);
			flag = true;
		} catch (Exception e) {
			LOGGER.info("# INSIDE EXCPTION OCCURED IN removeEmployee " + e.getMessage());
		}
		return flag;
	}

	@Override
	public boolean isDactiveLogin(EmployeeDetails employeeDetails) {
		LOGGER.info("#INSIDE IN isDactiveLogin ");
		boolean flag = false;
		try {
			List<EmployeeDetails> emp = getCurrentSession()
					.createNativeQuery("select * from tbl_employee_details WHERE  branch_id=:branchId "
							+ " and org_id=:orgId and is_deactive=:isDeactive and (email_id=:emailId or cmpy_emp_id=:cmpyEmpId) ")
					.setParameter("branchId", employeeDetails.getBranchId())
					.setParameter("orgId", employeeDetails.getOrgId())
					.setParameter("emailId", employeeDetails.getEmailId())
					.setParameter("cmpyEmpId", employeeDetails.getCmpyEmpId())
					.setParameter("isDeactive", EnovationConstants.ENABLE_STATUS).getResultList();
			LOGGER.info("#INSIDE IN emp OBJ VAL : " + emp.size());
			if (emp.size() > 0) {
				flag = true;
			}
		} catch (Exception e) {
			LOGGER.info("#INSIDE EXCPTION OCCURED IN isDactiveLogin " + e.getMessage());

		}
		return flag;
	}

	@Override
	public boolean isUserIsExecutive(EmployeeDetails employeeDetails) {
		LOGGER.info("#INSIDE IN isUserIsExecutive : ");
		boolean flag = false;
		Session session = getCurrentSession();
		try {
			Object exec = session.createNativeQuery("Select count(emp_id) from executive where emp_id=:empId")
					.setParameter("empId", employeeDetails.getEmpId()).getResultList();
			if (exec != null) {
				flag = true;
			}

		} catch (Exception e) {
			LOGGER.info("#INSIDE EXCPTION OCCURED IN isUserIsExecutive " + e.getMessage());
		}
		return flag;
	}

	@Override
	public int deactiveEmployee(EmployeeDetails emp) {
		LOGGER.info("#INSIDE IN deactiveEmployee ");
		int flag = 0;
		try {
			Session session = getCurrentSession();
			boolean checkROLE = md.inflightRoleCheck(emp.getEmpId(), EnovationConstants.DEACTIVE_EMPLOYEE, 0);
			if (checkROLE) {
				System.out.println("inside return 2");
				return 2;
			} else {
				boolean status = md.inflightRoleCheck(emp.getEmpId(), EnovationConstants.EMPLOYEE_ROLEID, 0);
				if (status) {
					System.out.println("inside return 3");
					return 3;

				} else {
					EmployeeDetails empDet = (EmployeeDetails) getCurrentSession().get(EmployeeDetails.class,
							emp.getEmpId());
					empDet.setIsDeactive(emp.getIsDeactive());
					empDet.setIsEmailVerified(EnovationConstants.ZERO);
					System.out.println(EncryptDecryptUtils.encrypt(empDet.getEmailId()));
					empDet.setEmailId(EncryptDecryptUtils.encrypt(empDet.getEmailId()));
					empDet.setContactNo(EncryptDecryptUtils.encrypt(empDet.getContactNo()));
					empDet.setCmpyEmpId(EncryptDecryptUtils.encrypt(empDet.getCmpyEmpId()));
					// audit.insertEmpDetailsAudit(session, "Remove Employee", 0/*
					// emp.getCreatedBy() */, "Active","Deactive", emp.getEmpId());
					session.update(empDet);
					flag = 1;
				}
			}
		} catch (Exception e) {
			LOGGER.info("#INSIDE EXCPTION OCCURED IN DEACTIVEEMPLOYEE " + e.getMessage());
			e.printStackTrace();
		}
		return flag;
	}

	@SuppressWarnings("unused")
	@Override
	public int updateEmployeeDetails(EmployeeDetails empdetails) {
		Gson g = new Gson();
		LOGGER.info("# INSIDE IN UPDATE_PROFILE_DETAILS :HEADER REQUEST :" + g.toJson(empdetails));
		int flag = 0;
		try {
			EmployeeDetails empDet = (EmployeeDetails) getCurrentSession().get(EmployeeDetails.class,
					empdetails.getEmpId());
			Session session = getCurrentSession();

			if (empdetails.getFirstName() != null) {
				empDet.setFirstName(empdetails.getFirstName());
			}
			if (empdetails.getLastName() != null) {
				empDet.setLastName(empdetails.getLastName());
			}
			if (empdetails.getMiddleName() != null) {
				empDet.setMiddleName(empdetails.getMiddleName());
			}
			if (empdetails.getContactNo() != null) {
				empDet.setContactNo(empdetails.getContactNo());
			}
			if (empdetails.getAddress() != null) {
				empDet.setAddress(empdetails.getAddress());
			}
			if (empdetails.getBranchId() != 0) {
				empDet.setBranch(new BranchMaster(empdetails.getBranchId()));
			}
			if (empdetails.getDeptId() != 0) {
				empDet.setDept(new DepartmentMaster(empdetails.getDeptId()));
			}
			if (empdetails.getCmpyEmpId() != null) {
				empDet.setCmpyEmpId(empdetails.getCmpyEmpId());
			}
			if (empdetails.getDesignation() != null) {
				empDet.setDesignation(empdetails.getDesignation());
			}
			if (empdetails.getDOA() != null) {
				empDet.setDOA(empdetails.getDOA());
			}
			if (empdetails.getDOB() != null) {
				empDet.setDOB(empdetails.getDOB());
			}
			if (empdetails.getLine() != null) {
				empDet.setLine(empdetails.getLine());
			}
			if (empdetails.getDoj() != null) {
				empDet.setDoj(empdetails.getDoj());
			}
			// Added By Vinay on 11-11-2019
			// Employee level reference -> table Employee Hierarchy table
			if (empdetails.getEmpLevelDetails() != null && empdetails.getEmpLevelDetails().getEmpLvlId() != 0) {
				System.out.println("======================Inside Employee Level =====================================");
				empDet.setEmpLevelDetails(new EmployeeHierarchy(empdetails.getEmpLevelDetails().getEmpLvlId()));
			} else {
				System.out.println("============== When object is null===================");
				empDet.setEmpLevelDetails(null);
				System.out.println("Employee Level value When obj not found ==: " + empDet.getEmpLevelDetails());
			}
			/*
			 * SEND VERIFICATION MAIL ON CHANGE EMPLOYEE EMAIL-ID
			 */
			if (empdetails.getEmailId() != null && !empdetails.getEmailId().isEmpty()
					&& !empdetails.getEmailId().equals(empDet.getEmailId())) {
				boolean isNOTEmailExistStatus = isEmailExist(empdetails);
				System.out.println("isNOTEmailExistStatus val t/f :" + isNOTEmailExistStatus);
				if (isNOTEmailExistStatus) {
					empDet.setEmailId(empdetails.getEmailId());
					System.out.println("IsSetupCompleted val : " + empdetails.getIsSetupCompleted());
					if (empdetails.getIsSetupCompleted() == EnovationConstants.ONE) {
						System.out.println("iscom_empId : " + empDet.getEmpId());
						System.out.println("iscom_emailId : " + empDet.getEmailId());
						System.out.println("iscom_emailId empREQ: " + empdetails.getEmailId());
						if (empdetails.getRegistrationByPass().equalsIgnoreCase(EnovationConstants.Y)) {
							empDet.setIsEmailVerified(EnovationConstants.ONE);
						} else {
							empDet.setToken(String.valueOf(CommonUtils.generateToken(16)));
							empDet.setIsEmailVerified(EnovationConstants.ZERO);
						}
						// empDet.setIsDeactive(EnovationConstants.ONE);
						empDet.setIsSetupCompleted(empdetails.getIsSetupCompleted());
						System.out.println("iscom_empId : " + empDet.getEmpId());
						System.out.println("iscom_emailId : " + empDet.getEmailId());
						/*
						 * List<EmployeeDetails> request=new ArrayList<>(); request.add(empDet);
						 * sendBulkEmailtoEmployee(request);
						 */
						sendMailToImportedEmployees(empDet.getEmailId(), empDet.getFirstName(), empDet.getLastName(),
								empDet.getToken(), EnovationConstants.EMAIL_CHANGE_VERIFICATION);
						System.out.println("DONE!");
					}
				} else {
					return EnovationConstants.TWO;
				}
			} else {
				empDet.setEmailId(empdetails.getEmailId());
			}

			empDet.setUpdatedDate(CommonUtils.currentDate());
			// Gson gson = new Gson();
			// String currentValue = gson.toJson(empdetails);
			int updatedBy = 0;
			if (empdetails.getCreatedBy() != 0) {
				updatedBy = empdetails.getCreatedBy();
			}
			// audit.insertEmpDetailsAudit(session, "Update Employee", updatedBy,
			// j.toString(), currentValue,empdetails.getEmpId());

			if (empdetails.getEmpLevelDetails() != null && empdetails.getEmpLevelDetails().getEmpLvlId() != 0) {
				System.out.println("======================Inside Employee Level =====================================");
				empDet.setEmpLevelDetails(new EmployeeHierarchy(empdetails.getEmpLevelDetails().getEmpLvlId()));
			}
			session.update(empDet);

			flag = EnovationConstants.ONE;

		} catch (Exception e) {
			LOGGER.info("# INSIDE EXCPTION OCCURED IN UPDATE_PROFILE_DETAILS " + e.getMessage());

		}
		return flag;
	}

	@Override
	public List<EmployeeDetails> getAllEmplyeeListByBranch(Integer orgId, Integer branchId, Integer deptId) {
		List<EmployeeDetails> empList = null;
		LOGGER.info("# INSIDE IN GETALL_EMPLYEE_LISTBYBRANCH ");
		Session session = getCurrentSession();
		StringBuffer strString = new StringBuffer("from EmployeeDetails WHERE isDeactive = 0  ");
		Integer id = null;
		if (orgId != 0) {
			strString.append("and organization.orgId=:id");
			id = orgId;
		}
		if (branchId != 0) {
			strString.append("and branch.branchId=:id");
			id = branchId;
		}
		if (deptId != 0) {
			strString.append("and dept.deptId=:id");
			id = deptId;
		}
		if (orgId != 0 && branchId != 0 || orgId != 0 && deptId != 0 && branchId != 0) {
			return null;
		} else {
			empList = session.createQuery(strString.toString()).setParameter("id", id).getResultList();
		}

		return empList;
	}

	@SuppressWarnings("unused")

	@Override
	public List<EmployeeDetails> getAllEmpListForExecutor(Integer branchId) {
		LOGGER.info("#INSIDE IN GETALLEMPLISTFOREXECUTOR ");
		List<EmployeeDetails> emp = null;
		List<Integer> empIds = new ArrayList<>();

		try {
			StringBuffer strString = new StringBuffer("SELECT e.* FROM  tbl_employee_details e , " + " roles r,"
					+ " emp_roles er " + "  where " + " er.emp_id=e.emp_id " + " and er.role_id !=:roleId"
					+ " and e.branch_id=:branchId GROUP BY e.emp_id");
			emp = getCurrentSession().createNativeQuery(strString.toString()).addEntity(EmployeeDetails.class)
					.setParameter("roleId", EnovationConstants.EMPLOYEE_ROLEID).setParameter("branchId", branchId)
					.getResultList();
			LOGGER.info("#INSIDE IN emp OBJ VAL : " + emp.size());

		} catch (Exception e) {
			LOGGER.info("#INSIDE EXCPTION OCCURED IN GETALLEMPLISTFOREXECUTOR " + e.getMessage());
		}
		return emp;
	}

	@SuppressWarnings("unused")
	@Override
	public boolean createEmployee(List<EmployeeDetails> list) {
		Gson g = new GsonBuilder().setPrettyPrinting().create();
		LOGGER.info("# INSIDE IN CREATEEMPLOYEE REQUEST HEADER :" + g.toJson(list));
		boolean flag = false;
		try {
			String action = "Add Employee";
			Session session = getCurrentSession();
			for (EmployeeDetails empDetails : list) {
				Integer empId = 0;
				empId = empDetails.getEmpId();
				final String pass = String.valueOf(CommonUtils.GenerateRandomPassword(8));
				final String token = String.valueOf(CommonUtils.generateToken(16));
				System.out.println("RegistrationByPass FLAG :" + empDetails.getRegistrationByPass());

				empDetails.setCreatedDate(CommonUtils.currentDate());
				empDetails.setIsDeactive(EnovationConstants.ZERO);
				empDetails.setToken(token);
				empDetails.setEmpType(EnovationConstants.EMPLOYEE);
				Role role = isRole((RoleName.EMPLOYEE).toString());
				Object obj = session.save(empDetails);

				if (empDetails.getRegistrationByPass().equalsIgnoreCase(EnovationConstants.Y)) {
					empDetails.setIsEmailVerified(EnovationConstants.ONE);
					empDetails.setPassword(password.encode(EnovationConstants.DEFAULT_PASSWORD));
				} else {
					empDetails.setPassword(password.encode(pass));

				}
				empDetails.setRoles(Collections.singleton(role));
				if (obj != null) {
					if (empId == 0)
						audit.insertEmpDetailsAudit(session, action, empDetails.getCreatedBy(), null,
								(new Gson()).toJson(empDetails), empDetails.getEmpId());
					/*
					 * SEND MAIL TO EMPLOYEE
					 */
					session.save((new EmployeeDetailsAudit(EnovationConstants.UPDATE_PASSWORD_ACTION,
							(empDetails.getUpdatedBy() > 0) ? (new EmployeeDetails(empDetails.getUpdatedBy())) : null,
							(empDetails.getEmpId() > 0) ? new EmployeeDetails(empDetails.getEmpId()) : null,
							(empDetails.getPassword() != null) ? EncryptDecryptUtils.encrypt(empDetails.getPassword())
									: null,
							(empDetails.getPassword() != null) ? EncryptDecryptUtils.encrypt(empDetails.getPassword())
									: null,
							CommonUtils.currentDate())));
					if (empDetails.getIsSetupCompleted() == EnovationConstants.ONE) {
						List<EmployeeDetails> reqList = new ArrayList<>();
						reqList.add(empDetails);
						boolean SentVerifyEmail = sendBulkEmailtoEmployee(reqList);
						if (SentVerifyEmail) {
							flag = true;
						}
					}
					flag = true;
				}
			}
		} catch (Exception e) {
			LOGGER.error("#INSIDE EXCEPTION OCCURED IN CREATE EMPLOYEE API :" + e.getMessage());
		}

		return flag;
	}

	private Role isRole(String rolename) {
		Session session = getCurrentSession();
		List<Role> roleDetails = session.createNativeQuery("select * FROM roles where name=:name").addEntity(Role.class)
				.setParameter("name", rolename).getResultList();

		return roleDetails.get(0);
	}

	@Override
	public EmployeeDetails employeeVerify(String token) {
		LOGGER.info("#INSIDE IN employeeVerify API ");
		Session session = getCurrentSession();
		List<EmployeeDetails> empList = new ArrayList<>();
		try {

			empList = session.createQuery(" FROM EmployeeDetails where token=:token ").setParameter("token", token)
					.getResultList();
			if (empList.size() > 0) {
				String newtoken = String.valueOf(CommonUtils.generateToken(25));
				empList.get(0).setIsDeactive(EnovationConstants.ZERO);
				empList.get(0).setIsEmailVerified(EnovationConstants.ONE);
				empList.get(0).setToken(newtoken);
				session.update(empList.get(0));
				return (EmployeeDetails) session.get(EmployeeDetails.class, empList.get(0).getEmpId());
			}
		} catch (Exception e) {
			LOGGER.error("#INSIDE EXCEPTION OCCURED IN EMPLOYEEVERIFY API:" + e.getMessage());
		}
		return null;
	}

	@Override
	public List<EmployeeDetails> getEmpListRolewise(Integer branchId, Integer roleId) {
		/*
		 * LOGGER.info("# INSIDE IN getAllEmplyeeListByBranch "); StringBuffer strString
		 * = new StringBuffer(
		 * " SELECT e.* FROM  tbl_employee_details e , roles r , emp_roles er  where " +
		 * " er.emp_id=e.emp_id  and er.role_id =r.id and e.branch_id=:branchId ");
		 * List<EmployeeDetails> empList = null; Integer id = null;
		 * strString.append("and r.id=:id"); id = roleId; empList =
		 * getCurrentSession().createNativeQuery(strString.toString()).addEntity(
		 * EmployeeDetails.class) .setParameter("branchId", branchId).setParameter("id",
		 * id).getResultList();
		 */

		LOGGER.info("# INSIDE IN getAllEmplyeeListByBranch ");
		StringBuffer strString = new StringBuffer(
				"select e.emp_id, e.cmpy_emp_id, e.first_name, e.last_name, e.email_id, e.contact_no, "
						+ " e.profile_pic, e.points, e.designation, e.is_email_verified, e.logged_in, "
						+ " e.branch_id, b.name, e.dept_id, d.dept_name, " + " r.name as roleName, er.role_id "
						+ " from tbl_employee_details  e left join master_department d ON d.dept_id=e.dept_id "
						+ " left join master_branch b ON b.branch_id=e.branch_id "
						+ "  left join emp_roles er left join roles r  ON r.id=er.role_id  ON er.emp_id=e.emp_id ");
		strString.append(" WHERE e.is_deactive = 0 ");
		List<EmployeeDetails> empList = new ArrayList<>();
		strString.append(" and e.branch_id=:branchId and r.id=:id");
		List<Object[]> empListObj = getCurrentSession().createNativeQuery(strString.toString())
				.setParameter("branchId", branchId).setParameter("id", roleId).getResultList();
		for (Object[] emp : empListObj) {
			EmployeeDetails employee = new EmployeeDetails();
			if (emp[0] != null) {
				employee.setEmpId(Integer.valueOf(String.valueOf(emp[0])));
			}
			employee.setCmpyEmpId(String.valueOf(emp[1]));
			employee.setFirstName(String.valueOf(emp[2]));
			employee.setLastName(String.valueOf(emp[3]));
			employee.setEmailId(String.valueOf(emp[4]));
			employee.setContactNo(String.valueOf(emp[5]));
			if (emp[6] != null) {
				employee.setProfilePic(String.valueOf(emp[6]));
			}
			if (emp[7] != null) {
				employee.setPoints(Integer.valueOf(String.valueOf(emp[7])));
			}
			employee.setDesignation(String.valueOf(emp[8]));
			if (emp[9] != null) {
				employee.setIsEmailVerified(Integer.valueOf(String.valueOf(emp[9])));
			}
			if (emp[10] != null) {
				employee.setLoggedIn(Integer.valueOf(String.valueOf(emp[10])));
			}
			if (emp[11] != null) {
				employee.setBranchId(Integer.valueOf(String.valueOf(emp[11])));
			}
			employee.setBranchName(String.valueOf(emp[12]));
			if (emp[13] != null) {
				employee.setDeptId(Integer.valueOf(String.valueOf(emp[13])));
			}
			if (emp[14] != null) {
				employee.setDeptName(String.valueOf(emp[14]));
			}
			employee.setRoleId(Long.valueOf(String.valueOf(emp[16])));
			employee.setRoleName(String.valueOf(emp[15]));
			empList.add(employee);
		}

		return empList;
	}

	@Override
	public List<EmployeeDetails> getEmpListRolewiseOrgLevel(Integer orgId, Integer roleId) {
		/*
		 * LOGGER.info("# INSIDE IN getAllEmplyeeListByBranch "); StringBuffer strString
		 * = new StringBuffer(
		 * " SELECT e.* FROM  tbl_employee_details e , roles r , emp_roles er  where " +
		 * " er.emp_id=e.emp_id  and er.role_id =r.id and e.branch_id=:branchId ");
		 * List<EmployeeDetails> empList = null; Integer id = null;
		 * strString.append("and r.id=:id"); id = roleId; empList =
		 * getCurrentSession().createNativeQuery(strString.toString()).addEntity(
		 * EmployeeDetails.class) .setParameter("branchId", branchId).setParameter("id",
		 * id).getResultList();
		 */

		LOGGER.info("# INSIDE IN getEmpListRolewiseOrgLevel ");
		StringBuffer strString = new StringBuffer(
				"select e.emp_id, e.cmpy_emp_id, e.first_name, e.last_name, e.email_id, e.contact_no, "
						+ " e.profile_pic, e.points, e.designation, e.is_email_verified, e.logged_in, "
						+ " e.branch_id, b.name, e.dept_id, d.dept_name, " + " r.name as roleName, er.role_id "
						+ " from tbl_employee_details  e left join master_department d ON d.dept_id=e.dept_id "
						+ " left join master_branch b ON b.branch_id=e.branch_id "
						+ "  left join emp_roles er left join roles r  ON r.id=er.role_id  ON er.emp_id=e.emp_id ");
		strString.append(" WHERE e.is_deactive = 0 ");
		List<EmployeeDetails> empList = new ArrayList<>();
		strString.append(" and e.org_id=:orgId and r.id=:id");
		List<Object[]> empListObj = getCurrentSession().createNativeQuery(strString.toString())
				.setParameter("orgId", orgId).setParameter("id", roleId).getResultList();
		for (Object[] emp : empListObj) {
			EmployeeDetails employee = new EmployeeDetails();
			if (emp[0] != null) {
				employee.setEmpId(Integer.valueOf(String.valueOf(emp[0])));
			}
			employee.setCmpyEmpId(String.valueOf(emp[1]));
			employee.setFirstName(String.valueOf(emp[2]));
			employee.setLastName(String.valueOf(emp[3]));
			employee.setEmailId(String.valueOf(emp[4]));
			employee.setContactNo(String.valueOf(emp[5]));
			if (emp[6] != null) {
				employee.setProfilePic(String.valueOf(emp[6]));
			}
			if (emp[7] != null) {
				employee.setPoints(Integer.valueOf(String.valueOf(emp[7])));
			}
			employee.setDesignation(String.valueOf(emp[8]));
			if (emp[9] != null) {
				employee.setIsEmailVerified(Integer.valueOf(String.valueOf(emp[9])));
			}
			if (emp[10] != null) {
				employee.setLoggedIn(Integer.valueOf(String.valueOf(emp[10])));
			}
			if (emp[11] != null) {
				employee.setBranchId(Integer.valueOf(String.valueOf(emp[11])));
			}
			employee.setBranchName(String.valueOf(emp[12]));
			if (emp[13] != null) {
				employee.setDeptId(Integer.valueOf(String.valueOf(emp[13])));
			}
			if (emp[14] != null) {
				employee.setDeptName(String.valueOf(emp[14]));
			}
			employee.setRoleId(Long.valueOf(String.valueOf(emp[16])));
			employee.setRoleName(String.valueOf(emp[15]));
			empList.add(employee);
		}

		return empList;
	}

	@Override
	public int saveOrUpdateEmpRoles(List<SaveEmpRolesBean> request) {
		LOGGER.info("#INSIDE IN SAVEORUPDATEEMPROLES API ");
		int flag = 0;
		// /Session session = getCurrentSession();

		for (SaveEmpRolesBean req : request) {
			String firstName = "", lastName = "", roleName = "";
			if (req.getFirstName() != null) {
				firstName = req.getFirstName();
			}
			if (req.getLastName() != null) {
				lastName = req.getLastName();
			}
			if (req.getRoleName() != null) {
				roleName = req.getRoleName();
			}
			if (req.getIsDeactive() != EnovationConstants.ONE) {
				/*
				 * Role role = new Role(); role.setId((long) req.getRoleId());
				 */
				boolean insertStatus = md.saveuserrole(req.getEmpId(), req.getRoleId());
				if (insertStatus) {
					if (req.getIsSetupCompleted() > 0) {
						sendMailTo_ROLE_CHANGED_Employees(req.getEmailId(), firstName, lastName, roleName,
								EnovationConstants.ROLE_ADDED);
					}
				}
			} else {
				md.removeuserrole(req.getEmpId(), req.getRoleId());

			}
			flag = 1;
		}
		return flag;
	}

	public boolean sendBulkEmailtoEmployee(List<EmployeeDetails> request) {
		boolean flag = false;
		Session session = getCurrentSession();

		LOGGER.info("#INSIDE IN SENDBULKEMAILTOEMPLOYEE API ");
		try {
			for (EmployeeDetails req : request) {

				if (req.getIsSetupCompleted() == EnovationConstants.ONE) {
					String roles = "";
					List<String> roleName = new ArrayList<>();
					if (req.getRoles() != null && req.getRoles().size() > 0) {
						for (Role r : req.getRoles()) {
							roleName.add((r.getName()).toString());
						}
						roles = StringUtils.join(roleName, ',');
					}
					if (req.getRegistrationByPass().equalsIgnoreCase(EnovationConstants.Y)) {
						if (req.getEmailId() != null && req.getEmailId().length() > 2) {
							sendMailToImportedEmployees(req.getEmailId(), req.getFirstName(), roles, req.getToken(),
									EnovationConstants.EMPLOYEE_VERIFIED);
						}
						if (req.getContactNo() != null && req.getContactNo().length() > 2) {
//							String content = "Dear " + req.getFirstName()
//									+ ",                                                              %0a"
//									+ " Your myeNovation credentials :                                                                     %0a "
//									+ " Login ID :" + req.getContactNo()
//									+ "                                                                  %0a "
//									+ " Password :" + EnovationConstants.DEFAULT_PASSWORD
//									+ "                                                                      %0a "
//									+ " Portal Link : " + req.getOrganization().getPortalLink()
//									+ "                                                                      %0a "
//									// + " Android Link : "+EnovationConfig.buddyConfig.get("appLink") +" %0a "
//									// + " IOS Link : "+EnovationConfig.buddyConfig.get("appLinkIos") +" %0a "
//									+ "                                                                  %0a ";

							String content = "Hii " + req.getFirstName()
									+ ",%nWelcome To myeNovation !%nBelow are your credentials :%nLogin ID :(Your Mobile Number)%n"
									+ "Password :" + EnovationConstants.DEFAULT_PASSWORD + "%n"
									+ "Portal Link : http://" + req.getOrganization().getShortPortalLink()
									+ "/login.html%nGREENTIN SOLUTIONS PRIVATE LIMITED";

							System.out.println("sms content : " + content);

							taskExecutor.execute(
									new SmsUtil(req.getContactNo(), content.replaceAll("[\r\n]+", " "), communication));
							flag = true;
						}

					} else {
						if (req.getEmailId() != null && req.getEmailId().length() > 2) {
							sendMailToImportedEmployees(req.getEmailId(), req.getFirstName(), roles, req.getToken(),
									EnovationConstants.EMPLOYEE);
						}
						if (req.getContactNo() != null && req.getContactNo().length() > 2) {
//							String content = "Dear " + req.getFirstName()
//									+ ",                                                              %0a"
//									+ " Welcome To myeNovation!!                                                                       %0a"
//									+ " Please Activate Your Account by Clicking Link Below                                                                      %0a "
//									+ EnovationConfig.buddyConfig.get("verifyEmployeeEmailLink") + req.getToken()
//									+ " Login ID :" + req.getContactNo()
//									+ "                                                                  %0a "
//									+ " Portal Link : " + req.getOrganization().getPortalLink()
//									+ "                                                                      %0a "
//									+ " Android Link : " + EnovationConfig.buddyConfig.get("appLink")
//									+ "                                                                      %0a "
//									+ " IOS Link : " + EnovationConfig.buddyConfig.get("appLinkIos")
//									+ "                                                                      %0a ";

							String content = "Hii " + req.getFirstName()
									+ ",%nWelcome To myeNovation !%nBelow are your credentials :%nLogin ID :(Your Mobile Number)%n"
									+ "Password :" + EnovationConstants.DEFAULT_PASSWORD + "%n"
									+ "Portal Link : http://" + req.getOrganization().getShortPortalLink()
									+ "/login.html%nGREENTIN SOLUTIONS PRIVATE LIMITED";

							System.out.println("sms content : " + content);
							taskExecutor.execute(
									new SmsUtil(req.getContactNo(), content.replaceAll("[\r\n]+", " "), communication));
							flag = true;
						}
					}
					session.createNativeQuery(
									" update tbl_employee_details set is_notify=:isNotify where emp_id=:empId ")
							.setParameter("isNotify", EnovationConstants.ONE).setParameter("empId", req.getEmpId())
							.executeUpdate();

				}

			}
			flag = true;
		} catch (Exception e) {
			LOGGER.error("#INSIDE EXCEPTION OCCURED IN SENDBULKEMAILTOEMPLOYEE API:" + e.getMessage());
		}
		return flag;
	}

	public boolean sendBulkEmailtoEmployee(List<EmployeeDetails> sendMailEmployeeList, int isSetupCompleted,
										   ProductOrgConfig list) {
		boolean flag = false;
		Session session = getCurrentSession();
		LOGGER.info("#INSIDE IN SENDBULKEMAILTOEMPLOYEE API ");
		List<MailDTO> mailList = new ArrayList<>();
		try {
			for (EmployeeDetails employee : sendMailEmployeeList) {
				if (isSetupCompleted == EnovationConstants.ONE) {
					String roles = "";
					List<String> roleName = new ArrayList<>();
					if (employee.getRoles() != null && employee.getRoles().size() > 0) {
						for (Role r : employee.getRoles()) {
							roleName.add((r.getName()).toString());
						}
						roles = StringUtils.join(roleName, ',');
					}
					MailDTO mail = null;

					if (list.getRegistrationByPass().equalsIgnoreCase(EnovationConstants.Y)) {
						if (employee.getEmailId() != null) {
							mail = sendMailToBulkImportedEmployees(employee.getEmailId(), employee.getFirstName(),
									roles, employee.getToken(), EnovationConstants.EMPLOYEE_VERIFIED);
						}
						System.out.println("employee.getContactNo().." + employee.getContactNo());

						if (employee.getContactNo() != null) {
//							String content = "Dear " + employee.getFirstName()
//									+ ",                                                              %0a"
//									+ " Your myeNovation credentials :                                                                     %0a "
//									+ " Login ID :" + employee.getContactNo()
//									+ "                                                                  %0a "
//									+ " Password :" + EnovationConstants.DEFAULT_PASSWORD
//									+ "                                                                      %0a "
//									+ " Portal Link : " + list.getOrg().getPortalLink()
//									+ "                                                                      %0a "
//									+ " Android Link : " + EnovationConfig.buddyConfig.get("appLink")
//									+ "                                                                      %0a "
//									+ " IOS Link : " + EnovationConfig.buddyConfig.get("appLinkIos")
//									+ "                                                                      %0a "
//									+ "                                                                  %0a ";
//							System.out.println("sms content : " + content);

							String content = "Hii " + employee.getFirstName()
									+ ",%nWelcome To myeNovation !%nBelow are your credentials :%nLogin ID :(Your Mobile Number)%n"
									+ "Password :" + EnovationConstants.DEFAULT_PASSWORD + "%n"
									+ "Portal Link : http://" + list.getOrg().getShortPortalLink()
									+ "/login.html%nGREENTIN SOLUTIONS PRIVATE LIMITED";

							System.out.println("sms content : " + content);

							taskExecutor.execute(new SmsUtil(employee.getContactNo(),
									content.replaceAll("[\r\n]+", " "), communication));
							flag = true;
						}
					} else {
						mail = sendMailToBulkImportedEmployees(employee.getEmailId(), employee.getFirstName(), roles,
								employee.getToken(), EnovationConstants.EMPLOYEE);
						if (employee.getContactNo() != null) {
							String content = "Dear " + employee.getFirstName()
									+ ",                                                              %0a"
									+ " Welcome To myeNovation!!                                                                       %0a"
									+ " Please activate your account by clicking below link                                                                 %0a "
									+ EnovationConfig.buddyConfig.get("verifyEmployeeEmailLink") + employee.getToken()
									+ " Login ID :" + employee.getContactNo()
									+ "                                                                  %0a "
									+ " Portal Link : " + list.getOrg().getPortalLink()
									+ "                                                                      %0a "
									+ " Android Link : " + EnovationConfig.buddyConfig.get("appLink")
									+ "                                                                      %0a "
									+ " IOS Link : " + EnovationConfig.buddyConfig.get("appLinkIos")
									+ "                                                                      %0a ";
							taskExecutor.execute(new SmsUtil(employee.getContactNo(),
									content.replaceAll("[\r\n]+", " "), communication));
							flag = true;
						}
					}
					session.createNativeQuery(
									"update tbl_employee_details set is_notify=:isNotify where emp_id=:empId ")
							.setParameter("isNotify", EnovationConstants.ONE).setParameter("empId", employee.getEmpId())
							.executeUpdate();
					mailList.add(mail);
				} else {
					System.out.println(" in Side Else");
				}
			}
			taskExecutor.execute(new MailUtil(mailList, communication));
			flag = true;
		} catch (Exception e) {
			LOGGER.error("#INSIDE EXCEPTION OCCURED IN SENDBULKEMAILTOEMPLOYEE API:" + e.getMessage());
		}
		return flag;
	}

	@Override
	public boolean isEmailExist(String emailId) {
		LOGGER.info("# INSIDE IN isEmailExist:" + emailId.length());
		List<Object[]> employeeDetails = new ArrayList<>();
		boolean flag = false;
		employeeDetails = getCurrentSession()
				.createNativeQuery("select * from tbl_employee_details where email_id =:emailId and is_deactive = 0 ")
				.setParameter("emailId", emailId).getResultList();
		if (employeeDetails.isEmpty()) {
			return true;
		}

		return flag;
	}

	@Override
	public boolean isMobileNumberExist(String mobileNumber) {
		LOGGER.info("# INSIDE IN isMobileNumberExist:" + mobileNumber.length());
		List<Object[]> employeeDetails = new ArrayList<>();
		boolean flag = false;
		employeeDetails = getCurrentSession()
				.createNativeQuery(
						"select * from tbl_employee_details where contact_no =:mobileNumber and is_deactive = 0")
				.setParameter("mobileNumber", mobileNumber).getResultList();
		if (employeeDetails.isEmpty()) {
			return true;
		}
		return flag;
	}

	@SuppressWarnings("unused")
	@Override
	@Transactional
	public int resetPassword(EmployeeDetails request) {
		Session session = getCurrentSession();
		Integer passwdCheckLimit = 0;
		List<Object[]> list = new ArrayList<>();
		boolean isPwdExists = false;
		Integer flag = 0;
		try {
			// to retrive check last password
			list = session
					.createNativeQuery(
							"SELECT check_last_passwd,org_id from password_policy WHERE org_id=:orgId GROUP BY org_id")
					.setParameter("orgId", request.getOrgId()).getResultList();
			for (Object[] x : list) {
				if (x[0] != null) {
					passwdCheckLimit = Integer.valueOf(String.valueOf(x[0]));
				}
				System.out.println("Last Password Check Limit--->" + passwdCheckLimit);
			}

			if (passwdCheckLimit > 0) {
				List<Object[]> data = session.createNativeQuery(
								"SELECT current_value,audit_id from employee_details_audit WHERE emp_id=:empId and action='Update Password'  ORDER BY action_date DESC LIMIT :limit")
						.setParameter("empId", request.getEmpId()).setParameter("limit", passwdCheckLimit)
						.getResultList();

				System.out.println("LIST SIZE" + data.size());

				if (data != null && data.size() > 0) {
					for (Object[] obj : data) {
						if (obj[0] != null) {

							System.out.println("NEW PASSWORD-->" + request.getNewPassword());
							System.out.println("Current Password from Audit--->"
									+ EncryptDecryptUtils.decrypt(String.valueOf(obj[0])));

							if (request.getNewPassword().equals(EncryptDecryptUtils.decrypt(String.valueOf(obj[0])))) {
								System.out.println("OLD PASSWORD-->" + String.valueOf(obj[0]));
								isPwdExists = true;
							}
						}
					}
					LOGGER.info("CANNOT USE SAME PASSWORD AGAIN:" + isPwdExists);
					if (isPwdExists) {
						// return
						LOGGER.info("CANNOT USE SAME PASSWORD AGAIN");
						flag = 2;

					} else {
						System.out.println("IN ELSE PART OF PASSWORD CHECK");
						/*
						 *
						 * int auditId, String action, EmployeeDetails actionBy, Timestamp actionDate,
						 * EmployeeDetails empId, String previousValue, String currentValue
						 */

						boolean resetPasswdFlag = resetPasswordAmt(session, request);
						EmployeeDetailsAudit audit = new EmployeeDetailsAudit(EnovationConstants.UPDATE_PASSWORD_ACTION,
								new EmployeeDetails(request.getEmpId()), new EmployeeDetails(request.getEmpId()),
								EncryptDecryptUtils.encrypt(request.getPassword()),
								EncryptDecryptUtils.encrypt(request.getNewPassword()), CommonUtils.currentDate());
						session.save(audit);
						flag = 3;

					}
				} else {
					System.out.println("IF THERE IS NO ENTRY IN AUDIT FOR PASSWORD CHANGE ");
					boolean resetFlag = resetPasswordAmt(session, request);
					EmployeeDetailsAudit audit = new EmployeeDetailsAudit(EnovationConstants.UPDATE_PASSWORD_ACTION,
							new EmployeeDetails(request.getEmpId()), new EmployeeDetails(request.getEmpId()),
							EncryptDecryptUtils.encrypt(request.getPassword()),
							EncryptDecryptUtils.encrypt(request.getNewPassword()), CommonUtils.currentDate());
					session.save(audit);
					flag = 3;
				}

			} else {
				System.out.println("IF THERE IS NO SETUP FOR ORG");
				boolean resetFlag = resetPasswordAmt(session, request);
				EmployeeDetailsAudit audit = new EmployeeDetailsAudit(EnovationConstants.UPDATE_PASSWORD_ACTION,
						new EmployeeDetails(request.getEmpId()), new EmployeeDetails(request.getEmpId()),
						EncryptDecryptUtils.encrypt(request.getPassword()),
						EncryptDecryptUtils.encrypt(request.getNewPassword()), CommonUtils.currentDate());
				session.save(audit);
				flag = 3;
			}
		} catch (Exception e) {
			LOGGER.error("#INSIDE EXCEPTION OCCURED IN RESETPASSWORD : " + e.getMessage());
			e.printStackTrace();
		}
		return flag;
	}

	@SuppressWarnings("unused")
	private boolean isRecordFound(Session session, int empId) {
		List<Object[]> empIds = new ArrayList<>();
		empIds = session.createNativeQuery("select emp_id,email_id from tbl_employee_details where emp_id=:empId")
				.setParameter("empId", empId).getResultList();

		return (!empIds.isEmpty()) ? true : false;
	}

	private boolean resetPasswordAmt(Session session, EmployeeDetails request) {
		EmployeeDetails empDet = (EmployeeDetails) session.get(EmployeeDetails.class, request.getEmpId());
		// if (password.matches(request.getPassword(), empDet.getPassword())) {
		empDet.setPassword(password.encode(request.getNewPassword()));
		empDet.setIsPasswordExpired(EnovationConstants.ZERO);
		session.update(empDet);
		return true;
		// }else {
		// return false;
		// }
	}

	@Override
	public boolean removeProfilePic(Integer empId) {
		boolean flag = false;
		Session session = getCurrentSession();
		try {

			EmployeeDetails empDet = (EmployeeDetails) session.get(EmployeeDetails.class, empId);
			if (empDet.getProfilePic() != null) {
				System.out.println("path :" + EnovationConfig.buddyConfig.get("profilePicUploadPath") + "/"
						+ empDet.getProfilePic());
				File fdelete = new File(
						EnovationConfig.buddyConfig.get("profilePicUploadPath") + "/" + empDet.getProfilePic());
				if (fdelete.delete()) {
					LOGGER.info("file Deleted ");
					session.createNativeQuery("UPDATE tbl_employee_details set profile_pic=NULL where  emp_id=:empId")
							.setParameter("empId", empDet.getEmpId()).executeUpdate();
					// empDet.setProfilePic("");
					// session.update(empDet);
					flag = true;
				} else {
					LOGGER.info("file not Deleted ");
				}
			}
		} catch (Exception e) {
			LOGGER.error("#INSIDE EXCEPTION OCCURED IN RESETPASSWORD : " + e.getMessage());
		}
		return flag;
	}

	@SuppressWarnings({ "resource", "unused" })
	@Override
	public int checkExeclFormatOrBlank(String alies, MultipartFile excel) {
		int value = 0;
		XSSFWorkbook workbook;
		String picPath;
		try {
			String requestFileName = excel.getOriginalFilename();
			// System.out.println("file name : "+requestFileName.substring(0,
			// requestFileName.indexOf('.')));
			/*
			 * File convFile = new File(System.getProperty("java.io.tmpdir")+"/"+fileName);
			 * excel.transferTo(convFile);
			 */
			picPath = writeImportedEmpexcel(alies, excel);
			System.out.println("picpath : " + picPath);
			FileInputStream file = new FileInputStream(new File(picPath));
			workbook = new XSSFWorkbook(file);
			XSSFSheet sheet = workbook.getSheetAt(0);
			Row row;
			int count = 1;
			for (int i = 0; i <= sheet.getLastRowNum(); i++) {
				/*
				 * POINTS TO THE STARTING OF EXCEL I.E EXCEL FIRST ROW
				 */
				row = (Row) sheet.getRow(i); // sheet number
				if (count == 1) {
					/*
					 * System.out.println(row.getCell(1).toString());
					 * System.out.println(row.getCell(2).toString());
					 * System.out.println(row.getCell(3).toString());
					 * System.out.println(row.getCell(4).toString());
					 * System.out.println(row.getCell(5).toString());
					 * System.out.println(row.getCell(6).toString());
					 */
					System.out.println(EnovationConstants.EMPLOYEE_ID);
					String employeeId = "";
					if (row.getCell(0) != null) {
						employeeId = row.getCell(0).toString().trim();
					}
					String firstName = "";
					if (row.getCell(1) != null) {
						firstName = row.getCell(1).toString().trim();
					}
					String lastName = "";
					if (row.getCell(2) != null) {
						lastName = row.getCell(2).toString().trim();
					}
					String email = "";
					if (row.getCell(3) != null) {
						email = row.getCell(3).toString().trim();
					}
					String mobileNumber = "";
					if (row.getCell(4) != null) {
						mobileNumber = row.getCell(4).toString().trim();
					}
					String department = "";
					if (row.getCell(5) != null) {
						department = row.getCell(5).toString().trim();
					}
					String designation = "";
					if (row.getCell(6) != null) {
						designation = row.getCell(6).toString().trim();
					}
					if (employeeId.equalsIgnoreCase(EnovationConstants.EMPLOYEE_ID)
							&& lastName.equalsIgnoreCase(EnovationConstants.LAST_NAME)
							&& firstName.equalsIgnoreCase(EnovationConstants.FIRST_NAME)
							&& email.equalsIgnoreCase(EnovationConstants.EMAIL_ID)
							&& mobileNumber.equalsIgnoreCase(EnovationConstants.MOBILE_NUMBER)
							&& department.equalsIgnoreCase(EnovationConstants.DEPARTMENT)
							&& designation.equalsIgnoreCase(EnovationConstants.DESIGNATION)) {
						System.out
								.println("i m from return 3 (Proceed forword for checking records are blank or not )");
						int returnStatement = checkFileIsBlank(sheet);
						// count ++;
						return returnStatement;
					} else {
						System.out.println("i m from return 2(fields not match)");
						return 2;
					}
				}

			}
			file.close();

		} catch (Exception e) {

		}
		return value;
	}

	private int checkFileIsBlank(XSSFSheet sheet) {
		System.out.println("blank file...." + sheet.getLastRowNum());
		int value = 4;
		for (int i = 1; i <= sheet.getLastRowNum(); i++) {
			Row row = (Row) sheet.getRow(i);
			if (row.getCell(1) != null && row.getCell(3) != null && row.getCell(4) != null) {
				System.out.println("format matches in Proper values");
				return 3;
			}
		}
		return value;

	}

	@Override
	public int isTokenVerified(String token) {
		List<EmployeeDetails> emp = getCurrentSession().createQuery("FROM EmployeeDetails WHERE token=:token ")
				.setParameter("token", token).getResultList();
		if (emp != null && emp.size() > 0) {
			return 2;
		} else {
			return 1;
		}

	}

	@Override
	public int isTokenVerifiedForForgotPassword(String token) {
		List<EmployeeDetails> emp = getCurrentSession().createQuery("FROM EmployeeDetails WHERE token=:token ")
				.setParameter("token", token).getResultList();
		if (emp != null && emp.size() > 0) {
			if (emp.get(0).getIsEmailVerified() == 1) {
				return 2;
			} else {
				return 3;
			}
		} else {
			return 1;
		}
	}

	@Override
	public boolean sendRoleUpdateEmails(Integer branchId) {
		boolean flag = false;
		List<EmployeeDetails> empList = getCurrentSession()
				.createQuery("FROM EmployeeDetails WHERE isDeactive =0 and branch.branchId = :branchId")
				.setParameter("branchId", branchId).getResultList();
		if (empList != null && empList.size() > 0) {
			for (EmployeeDetails emp : empList) {
				StringBuffer buf = new StringBuffer("<div>\r\n"
						+ "      <div style=\"background-color:#f2f3f5;padding:20px;\">\r\n"
						+ "          <div style=\"max-width:600px;margin:0 auto;\">\r\n"
						+ "              <div style=\"background:#fff;font:14px sans-serif;color:#686f7a;border-top:4px solid #7245ce;margin-bottom:20px\">\r\n"
						+ "                  <div style=\"border-bottom:1px solid #f2f3f5;padding:20px 30px;\">\r\n"
						+ "                      <img width=\"150\" style=\"max-width:99px;display:block\" src=\"http://enovation.greentinsolutions.com/static-website/images/myenovation.png\" alt=\"myEnovation\">\r\n"
						+ "                  </div>\r\n" + "                  <div style=\"padding:10px 30px;\">\r\n"
						+ "                      <div style=\"font-size:16px;line-height:1.5em;border-bottom:1px solid #f2f3f5;padding-bottom:5px;margin-bottom:10px;\">\r\n"
						+ "                          <p><a style=\"text-decoration:none;color:#000\">\r\n"
						+ "                                          \r\n"
						+ "                                              \r\n"
						+ "                                                  Dear {name},\r\n"
						+ "                                              \r\n"
						+ "                                          \r\n"
						+ "                                      </a></p>\r\n"
						+ "                          <p><a style=\"text-decoration:none;color:#000;font-size: 14px;\">  Welcome to myeNovation !</a></p>\r\n"
						+ "                          <p>\r\n"
						+ "                              <p><a style=\"text-decoration:none;color:#000;font-size: 14px;\">You must have received account verification email. Please verify your account in case it is still not done.\r\n"
						+ "   You have been assigned below role/roles:</a></p>"
						+ " <p> <a style=\"text-decoration:none;color:#000;font-size: 14px\"> <span style=\"font-weight: bold;\"> Employee : </span > <label style=\"color:#000000;font-size:14px;font-family:'Roboto',Sans-serif;\">This role enables you to post suggestion. </label></a> </p>");

				for (Role role : emp.getRoles()) {
					if (String.valueOf(role.getName()) == "CONTROLLER") {
						buf.append(
								" <p>  <a style=\"text-decoration:none;color:#000;font-size: 14px\"> <span style=\"font-weight: bold;\">Controller :</span > <label style=\"color:#000000;font-size:14px;font-family:'Roboto',Sans-serif;\">Suggestion posted for department will be assigned to you for your scruitinization. \r\n"
										+ "  You may Accept, Reject or Reassign suggestions assigned to you. And you can assign it to Evaluation for next level.</label></a>\r\n"
										+ "  </p>");
					}
					if (String.valueOf(role.getName()) == "EVALUATOR") {
						buf.append(
								"<p> <a style=\"text-decoration:none;color:#000;font-size: 14px\"> <span style=\"font-weight: bold;\">Evaluator : </span > <label style=\"color:#000000;font-size:14px;font-family:'Roboto',Sans-serif;\">Suggestion will be assigned to you for an evaluation.\r\n"
										+ "   You may evaluate & assign suggestion for implementation or you may reject it.</label></a>\r\n"
										+ "   </p>");
					}
					if (String.valueOf(role.getName()) == "IMPLEMENTER") {
						buf.append(
								"<p> <a style=\"text-decoration:none;color:#000;font-size: 14px\"> <span style=\"font-weight: bold;\">Implementer : </span > <label style=\"color:#000000;font-size:14px;font-family:'Roboto',Sans-serif;\">Suggestion will be assigned to you for an implementation.</label></a>\r\n"
										+ "</p>");
					}
					if (String.valueOf(role.getName()) == "EXECUTIVE") {
						buf.append(
								" <p><a style=\"text-decoration:none;color:#000;font-size: 14px\"> <span style=\"font-weight: bold;\">Executive :</span > <label style=\"color:#000000;font-size:14px;font-family:'Roboto',Sans-serif;\">Employees may raise concerns to you. Which you will have to review and assign it to someone to take action for closing it.</label></a>\r\n"
										+ " </p>");
					}

				}
				buf.append(
						"<table width='100%' cellpadding='0' cellspacing='0' style=\"border-top:1px solid #f2f3f5;margin-bottom:20px;padding-top:20px\">\r\n"
								+ "                                  <tbody>\r\n"
								+ "                                      <tr>\r\n"
								+ "                                          <td align=\"right\" width=\"50\">\r\n"
								+ "                                              <!-- <img src=\"\" alt=\"\" height=\"50\" width='50' border=\"0\" style=\"border-radius:50%;display:block\" class=\"\"> -->\r\n"
								+ "                                          </td>\r\n"
								+ "                                          <td valign=\"middle\" align=\"left\" style=\"vertical-align:middle;padding-left:10px\">\r\n"
								+ "                                              <!--  <strong></strong> -->\r\n"
								+ "                                          </td>\r\n"
								+ "                                      </tr>\r\n"
								+ "                                  </tbody>\r\n"
								+ "                              </table>\r\n"
								+ "                              <p style=\"font-size:14px;color:#000;opacity: 0.7;\">Best Regards,<br>Team myeNovation\r\n"
								+ "                              </p>\r\n"
								+ "                              <p style=\"font-size:13px;color:#686f7a;\">\r\n"
								+ "                                  This is an auto generated e-mail. Please do not reply.\r\n"
								+ "                              </p>\r\n" + "                      </div>\r\n"
								+ "                      <center>\r\n" + "                          <div>\r\n"
								+ "                              <a href=\"https://www.facebook.com/myeNovation\" target=\"_blank\" style=\"text-decoration:none; \">\r\n"
								+ "                                  <img src=\"http://aihub.greentinsolutions.com/enovation-portal/static-website/assets/images/fb.png \" style=\"height: 25px;width: 25px;margin-top: 0.25rem !important; \">\r\n"
								+ "                              </a>&nbsp;&nbsp;&nbsp;\r\n"
								+ "                              <a href=\"https://twitter.com/EnovationMy \" target=\"_blank\" style=\"text-decoration:none; \">\r\n"
								+ "                                  <img src=\"http://aihub.greentinsolutions.com/enovation-portal/static-website/assets/images/twitter-squared.png \" style=\"height: 25px;width: 25px;margin-top: 0.25rem !important; \">\r\n"
								+ "                              </a>&nbsp;&nbsp;&nbsp;\r\n"
								+ "                              <a href=\"https://www.linkedin.com/company/myenovation/ \" target=\"_blank\" style=\"text-decoration:none; \">\r\n"
								+ "                                  <img src=\"http://aihub.greentinsolutions.com/enovation-portal/static-website/assets/images/Linkedin_icon-512.png \" style=\"height: 25px;width: 25px;margin-top: 0.25rem !important; \">\r\n"
								+ "                              </a>\r\n" + "                          </div>\r\n"
								+ "                      </center>\r\n" + "                      <center>\r\n"
								+ "                          <p style=\"font-size:12px;color:#686f7a\">\r\n"
								+ "                              In case of any query, please contact\r\n"
								+ "                              <a href=\"mailto:support@myenovation.com\" style=\"text-decoration:none;color:#1376d7;text-decoration: underline;\" target=\"_blank\">support@myenovation.com</a>\r\n"
								+ "                          </p>\r\n"
								+ "                          <p style=\"font-size:12px;color:#686f7a\">\r\n"
								+ "                              Visit:\r\n"
								+ "                              <a href=\'" + emp.getOrganization().getPortalLink()
								+ "'/login.html style=\"text-decoration:none;color:#1376d7\" target=\"_blank\">'"
								+ emp.getOrganization().getPortalLink() + "'</a>\r\n"
								+ "                          </p>\r\n"
								+ "                          <p><a style=\"text-decoration:none;color:#ccc;font-size: 12px;\">Copyright ©2018 myeNovation. All rights reserved.</a></p>\r\n"
								+ "                      </center>\r\n" + "                  </div>\r\n"
								+ "              </div>\r\n" + "              <center>\r\n"
								+ "                  <div style=\"font:11px sans-serif;color:#686f7a\">\r\n"
								+ "                      <p style=\"font-size:11px;color:#686f7a\">\r\n"
								+ "                          Usage of the myeNovation service and website is subject to our\r\n"
								+ "                          <a href=\"https://myenovation.com/termsOfService.html\" style=\"text-decoration:none;color:#1376d7\" target=\"_blank\">Terms of Use</a> and <a href=\"https://myenovation.com/privacyPolicy.html\" style=\"text-decoration:none;color:#1376d7\"\r\n"
								+ "                              target=\"_blank\">Privacy Statement</a>.\r\n"
								+ "                      </p>\r\n" + "                  </div>\r\n"
								+ "              </center>\r\n" + "          </div>\r\n" + "      </div> "
								+ "  </div>");
				String content = (buf.toString()).replaceAll(Pattern.quote("{name}"), emp.getFirstName());
				if (emp.getEmailId() != null) {
					taskExecutor.execute(
							new MailUtil(emp.getEmailId(), "myeNovation | Role Update", content, communication));
					System.out.println("mail Sending to :" + emp.getEmailId());

				}
			}
		}
		return flag;

	}

	@Override
	public EmployeeDetails checkLoginEmail(String email) {

		List<EmployeeDetails> emp = getCurrentSession().createQuery(
						"FROM EmployeeDetails WHERE (emailId=:email or contactNo = :email or cmpyEmpId = :email) and isDeactive=0 and empType not in ('CW') ")
				.setParameter("email", email).getResultList();
		if (emp != null && emp.size() > 0) {
			// portalLink = emp.get(0).getOrganization().getPortalLink();
			System.out.println("Printing Emp Det : " + emp.get(0));
			return emp.get(0);
		} else {
			return null;
		}
	}

	@Override
	public List<EmployeeDetails> getAllEmplyeeListByBranchNew(Integer orgId, Integer branchId, Integer deptId) {
		List<Object[]> empList = null;
		List<EmployeeDetails> employeeList = new ArrayList<>();
		LOGGER.info("# INSIDE IN GETALLEMPLYEELISTBYBRANCHNEW ");
		Session session = getCurrentSession();
		StringBuffer strString = new StringBuffer(
				"select e.emp_id, e.cmpy_emp_id, e.first_name, e.last_name, e.email_id, e.contact_no, "
						+ " e.profile_pic, e.points, e.designation, e.is_email_verified, e.logged_in, "
						+ " e.branch_id, b.name, e.dept_id, d.dept_name, "
						+ " r.name as roleName, er.role_id, e.dob, e.doa,line.id as lineid,line.name as linename,"
						+ " eh.emp_lvl_id,eh.level_name,eh.level_type "
						+ " from tbl_employee_details  e left join master_department d ON d.dept_id=e.dept_id "
						+ " left join master_branch b ON b.branch_id=e.branch_id "
						+ " left join emp_roles er ON er.emp_id=e.emp_id left join roles r  ON r.id=er.role_id  "
						+ " left join employee_hierarchy eh on eh.emp_lvl_id=e.emp_level_id "
						+ " left join dwm_line line on line.id=e.line_id");
		strString.append(" WHERE e.is_deactive = 0 ");
		Integer id = null;
		if (orgId != 0) {
			strString.append("and e.org_id=:id");
			id = orgId;
		}
		if (branchId != 0) {
			strString.append("and e.branch_id=:id");
			id = branchId;
		}
		if (deptId != 0) {
			strString.append("and e.dept_id=:id");
			id = deptId;
		}
		if (orgId != 0 && branchId != 0 || orgId != 0 && deptId != 0 && branchId != 0) {
			return null;
		} else {
			empList = session.createNativeQuery(strString.toString()).setParameter("id", id).getResultList();
			for (Object[] emp : empList) {
				EmployeeDetails employee = new EmployeeDetails();
				if (emp[0] != null) {
					employee.setEmpId(Integer.valueOf(String.valueOf(emp[0])));
				}
				if (emp[1] != null)
					employee.setCmpyEmpId(String.valueOf(emp[1]));
				if (emp[2] != null)
					employee.setFirstName(String.valueOf(emp[2]));
				if (emp[3] != null)
					employee.setLastName(String.valueOf(emp[3]));
				if (emp[4] != null)
					employee.setEmailId(String.valueOf(emp[4]));
				if (emp[5] != null)
					employee.setContactNo(String.valueOf(emp[5]));
				if (emp[6] != null) {
					employee.setProfilePic(String.valueOf(emp[6]));
				}
				if (emp[7] != null) {
					employee.setPoints(Integer.valueOf(String.valueOf(emp[7])));
				}
				employee.setDesignation(String.valueOf(emp[8]));
				if (emp[9] != null) {
					employee.setIsEmailVerified(Integer.valueOf(String.valueOf(emp[9])));
				}
				if (emp[10] != null) {
					employee.setLoggedIn(Integer.valueOf(String.valueOf(emp[10])));
				}
				if (emp[11] != null) {
					employee.setBranchId(Integer.valueOf(String.valueOf(emp[11])));
				}
				employee.setBranchName(String.valueOf(emp[12]));
				if (emp[13] != null) {
					employee.setDeptId(Integer.valueOf(String.valueOf(emp[13])));
				}
				if (emp[14] != null) {
					employee.setDeptName(String.valueOf(emp[14]));
				}
				if (emp[16] != null) {
					employee.setRoleId(Long.valueOf(String.valueOf(emp[16])));
				}
				if (emp[15] != null) {
					employee.setRoleName(String.valueOf(emp[15]));
				}
				if (emp[17] != null) {
					employee.setDOB(CommonUtils.convertStringToSqlDate(String.valueOf(emp[17])));
				}
				if (emp[18] != null) {
					employee.setDOA(CommonUtils.convertStringToSqlDate(String.valueOf(emp[18])));
				}
				if (emp[19] != null && emp[20] != null) {
					Line line = new Line(Long.valueOf(String.valueOf(emp[19])), String.valueOf(emp[20]),
							Integer.valueOf(String.valueOf(emp[13])));
					employee.setLine(line);
				}

				if (emp[21] != null && emp[22] != null && emp[23] != null) {
					EmployeeHierarchy empLevelDetails = new EmployeeHierarchy(CommonUtils.objectToInt(emp[21]),
							CommonUtils.objectToString(emp[23]), CommonUtils.objectToString(emp[22]));
					employee.setEmpLevelDetails(empLevelDetails);
				}
				employeeList.add(employee);
			}
		}

		return employeeList;
	}

	@Override
	public boolean createBulkEmployee(List<EmployeeDetails> empListDetails) {
		LOGGER.info("# INSIDE IN CREATEBULKEMPLOYEE ");
		boolean flag = false;
		try {
			String action = "Add Employee";
			Session session = getCurrentSession();
			for (EmployeeDetails empDetails : empListDetails) {
				final String pass = String.valueOf(CommonUtils.GenerateRandomPassword(8));
				final String token = String.valueOf(CommonUtils.generateToken(16));
				if (!String.valueOf(empDetails.getRegistrationByPass()).equalsIgnoreCase(EnovationConstants.Y)) {
					empDetails.setPassword(password.encode(pass));
				}

				empDetails.setCreatedDate(CommonUtils.currentDate());
				empDetails.setIsDeactive(EnovationConstants.ZERO);
				empDetails.setToken(token);
				Role role = isRole((RoleName.EMPLOYEE).toString());
				Object obj = session.save(empDetails);
				Gson current = new Gson();
				String currentValue = current.toJson(empDetails);
				audit.insertEmpDetailsAudit(session, action, empDetails.getCreatedBy(), null, currentValue,
						empDetails.getEmpId());
				session.save((new EmployeeDetailsAudit(EnovationConstants.UPDATE_PASSWORD_ACTION,
						(empDetails.getUpdatedBy() > 0) ? (new EmployeeDetails(empDetails.getUpdatedBy())) : null,
						(empDetails.getEmpId() > 0) ? new EmployeeDetails(empDetails.getEmpId()) : null,
						(empDetails.getPassword() != null) ? EncryptDecryptUtils.encrypt(empDetails.getPassword())
								: null,
						(empDetails.getPassword() != null) ? EncryptDecryptUtils.encrypt(empDetails.getPassword())
								: null,
						CommonUtils.currentDate())));
				empDetails.setRoles(Collections.singleton(role));
				if (obj != null) {
					/*
					 * SEND MAIL TO EMPLOYEE
					 */
					if (empDetails.getIsSetupCompleted() == EnovationConstants.ONE) {
						List<EmployeeDetails> reqList = new ArrayList<>();
						reqList.add(empDetails);
						if (empDetails.getEmailId() != null && empDetails.getEmailId().length() > 2) {
							boolean SentVerifyEmail = sendBulkEmailtoEmployee(reqList);
							if (SentVerifyEmail) {
								flag = true;
							}
						}

					}
					flag = true;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			LOGGER.error("#INSIDE EXCEPTION OCCURED IN CREATE EMPLOYEE API :" + e.getMessage());
		}

		return flag;
	}

	@Override
	@Transactional
	public boolean saveFeedback(Feedback feed) {
		boolean flag = false;
		Session session = getCurrentSession();
		List<MailDTO> mailList = new ArrayList<>();
		int typeId = 0;
		try {
			EmailTemplateMaster messageContent = enoConfig.getMessageContent(EnovationConstants.MASTER_EMAIL_TEMPLATE);
			Object obj = session.save(feed);
			if (obj != null) {
				List<Object[]> data = session.createNativeQuery(
								"select f.feedback,f.created_date,f.ratings,f.feedback_by_id,f.feedback_type_id,ft.feedback_type,name,concat(e.first_name,' ',e.last_name) as 'Employee Name',e.email_id,f.source_type,o.portal_link\r\n"
										+ "from feedback f \r\n"
										+ "inner join feedback_type ft on ft.feedback_type_id=f.feedback_type_id\r\n"
										+ "inner join master_organization o on o.org_id=f.org_id\r\n"
										+ "inner join tbl_employee_details e on e.emp_id=f.feedback_by_id\r\n"
										+ "where f.feedback_id=:feedId")
						.setParameter("feedId", feed.getFeedbackId()).getResultList();
				for (Object[] row : data) {
					String body = null, emailTemplateBody = null;
					String subject = null;
					if (row != null) {
						typeId = Integer.valueOf(String.valueOf(row[4]));
					}
					if (typeId == EnovationConstants.ONE) {
						// *******************Query*****************************//
						body = env.getProperty("QUERY_BODY.body")
								.replaceAll(Pattern.quote("{feedback}"), (row[0] != null) ? String.valueOf(row[0]) : "")
								.replaceAll(Pattern.quote("{name}"), (row[7] != null) ? String.valueOf(row[7]) : "")
								.replaceAll(Pattern.quote("{email}"), (row[8] != null) ? String.valueOf(row[8]) : "")
								.replaceAll(Pattern.quote("{date}"),
										(row[1] != null) ? String.valueOf(row[1]).substring(0, 10) : "")
								.replaceAll(Pattern.quote("{source_type}"),
										(row[9] != null) ? String.valueOf(row[9]) : "")
								.replaceAll(Pattern.quote("{org_name}"),
										(row[6] != null) ? String.valueOf(row[6]) : "");
						subject = env.getProperty("QUERY.subject");
					} else {
						// ******************Feedback**************************//
						body = env.getProperty("FEEDBACK_BODY.body")
								.replaceAll(Pattern.quote("{feedback}"), (row[0] != null) ? String.valueOf(row[0]) : "")
								.replaceAll(Pattern.quote("{name}"), (row[7] != null) ? String.valueOf(row[7]) : "")
								.replaceAll(Pattern.quote("{email}"), (row[8] != null) ? String.valueOf(row[8]) : "")
								.replaceAll(Pattern.quote("{date}"),
										(row[1] != null) ? String.valueOf(row[1]).substring(0, 10) : "")
								.replaceAll(Pattern.quote("{source_type}"),
										(row[9] != null) ? String.valueOf(row[9]) : "")
								.replaceAll(Pattern.quote("{org_name}"), (row[6] != null) ? String.valueOf(row[6]) : "")
								.replaceAll(Pattern.quote("{ratings}"), (row[2] != null) ? String.valueOf(row[2]) : "");
						subject = env.getProperty("FEEDBACK.subject");
					}

					emailTemplateBody = messageContent.getBody()
							.replaceAll(Pattern.quote("{portalLink}"), (row[10] != null) ? String.valueOf(row[10]) : "")
							.replaceAll(Pattern.quote("{body}"), body)
							.replaceAll(Pattern.quote("{name}"), "myEnovation Team");

					MailDTO Mail = new MailDTO("vinay@greentinsolutions.com", subject, emailTemplateBody);
					mailList.add(Mail);
				}
			}
			flag = true;
		} catch (Exception e) {
			e.printStackTrace();
		}
		if (mailList != null && mailList.size() > 0) {
			taskExecutor.execute(new MailUtil(mailList, communication));
		}
		return flag;
	}

	@Override
	public List<FeedbackType> getFeedbackList() {
		LOGGER.info("#INSIDE IN getFeedbackList :");
		List<FeedbackType> list = null;
		Session session = getCurrentSession();
		try {
			list = session.createQuery("FROM FeedbackType ").getResultList();
		} catch (Exception e) {
			LOGGER.error("#INSIDE EXCEPTION OCCURED IN getFeedbackList :" + e.getMessage());
		}
		return list;
	}

	@Override
	@Transactional
	public boolean resetPasswordBySuperadmin(EmployeeDetails empdetails) {
		boolean flag = false;
		Session session = getCurrentSession();
		try {
			EmployeeDetails empDet = (EmployeeDetails) session.get(EmployeeDetails.class, empdetails.getEmpId());
			empDet.setPassword(password.encode(empdetails.getPassword()));
			session.update(empDet);
			flag = true;
		} catch (Exception e) {
			LOGGER.error("#INSIDE EXCEPTION OCCURED IN RESETPASSWORD : " + e.getMessage());
		}
		return flag;
	}

	@Override
	@Transactional
	public List<MultiBranchAccess> getOrgExeBranchList(String email) {
		LOGGER.info("# INSIDE IN GETORGEXECUTIVFEBRANCHLIST ");
		Session session = getCurrentSession();
		List<MultiBranchAccess> list = new ArrayList<>();
		try {

			List<Object[]> branchData = session.createNativeQuery(
							"SELECT ed.emp_id,ed.first_name,ed.last_name,ed.email_id,mb.org_id,mb.branch_id,m.location,m.name as branch_name \r\n"
									+ "FROM multiple_branch_access mb \r\n"
									+ "INNER JOIN tbl_employee_details ed ON ed.emp_id = mb.emp_id\r\n"
									+ "INNER JOIN master_branch m ON m.branch_id = mb.branch_id\r\n"
									+ "INNER JOIN emp_roles er  ON ed.emp_id = er.emp_id\r\n"
									+ "WHERE ed.email_id=:email AND er.role_id=:roleId")
					.setParameter("email", email).setParameter("roleId", EnovationConstants.ORG_EXECUTIVE)
					.getResultList();

			for (Object[] obj : branchData) {

				MultiBranchAccess branchList = new MultiBranchAccess();
				branchList.setEmpId(Integer.parseInt(String.valueOf(obj[0])));
				branchList.setFirstName(String.valueOf(obj[1]));
				branchList.setLastName(String.valueOf(obj[2]));
				branchList.setEmail(String.valueOf(obj[3]));
				branchList.setOrgId(Integer.parseInt(String.valueOf(obj[4])));
				branchList.setBranchId(Integer.parseInt(String.valueOf(obj[5])));
				branchList.setLocation(String.valueOf(obj[6]));
				branchList.setBranchName(String.valueOf(obj[7]));
				list.add(branchList);
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		return list;
	}

	@Override
	public List<MoodIndicator> getMoodRatingforToday(int empId, int branchId) {
		List<Object[]> details = null;
		List<MoodIndicator> det = new ArrayList<>();
		Session session = getCurrentSession();
		try {
			details = session.createNativeQuery("SELECT rating,created_date,id FROM mood_indicator "
							+ " WHERE branch_id=:branchId AND emp_id= :empId AND DATE(created_date)=CURDATE() ORDER BY created_date DESC  ")
					.setParameter("branchId", branchId).setParameter("empId", empId).setMaxResults(1).getResultList();
			for (Object[] obj : details) {
				MoodIndicator mod = new MoodIndicator();
				if (obj[0] != null) {
					mod.setRating(Integer.valueOf(String.valueOf(obj[0])));
				}
				if (obj[1] != null) {
					mod.setCreatedDate(CommonUtils.convertStringToTimestamp(String.valueOf(obj[1])));
				}
				if (obj[2] != null) {
					mod.setId(Long.valueOf(String.valueOf(obj[2])));
				}
				det.add(mod);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return det;
	}

	public List<NoticeSetup> getNoticeDetails(Integer branchId, Integer deptId) {

		LOGGER.info("# INSIDE GET NOTICE LIST API ");
		Session session = getCurrentSession();
		System.out.println("Branch ID :" + branchId);
		System.out.println("Department ID : " + deptId);
		List<NoticeSetup> list = new ArrayList<>();
		try {

			List<Object[]> data = session.createNativeQuery("select s.notice_id,n.intended_for,d.dept_name,s.title,"
							+ "s.notice_message,DATE_FORMAT(s.notice_period, \"%Y-%m-%d\") AS notice_period,\r\n"
							+ "e.first_name,e.last_name,DATE_FORMAT(s.created_date, \"%Y-%m-%d\") as created_date\r\n"
							+ "from notice_intended_for n \r\n"
							+ "INNER JOIN tbl_notice_setup s ON n.notice_id = s.notice_id\r\n"
							+ "INNER JOIN master_department d ON n.intended_for = d.dept_id\r\n"
							+ "INNER JOIN tbl_employee_details e ON e.emp_id = s.created_by \r\n"
							+ "WHERE d.dept_id=:deptId  AND s.branch_id=:branchId "
							+ "AND  DATE_FORMAT(NOW(), \"%Y-%m-%d\") <=  DATE_FORMAT(s.notice_period, \"%Y-%m-%d\");\r\n" + "")
					.setParameter("branchId", branchId).setParameter("deptId", deptId).getResultList();

			for (Object[] obj : data) {
				NoticeSetup notice = new NoticeSetup();
				notice.setNoticeId(Integer.parseInt(String.valueOf(obj[0])));
				notice.setDeptId(Integer.parseInt(String.valueOf(obj[1])));
				notice.setDeptName(String.valueOf(obj[2]));
				notice.setTitle(String.valueOf(obj[3]));
				notice.setNoticeMessage(String.valueOf(obj[4]));
				notice.setNotePeriod(String.valueOf(obj[5]));
				notice.setFirstName(String.valueOf(obj[6]));
				notice.setLastName(String.valueOf(obj[7]));
				notice.setCreationDate(String.valueOf(obj[8]));
				list.add(notice);
			}

		} catch (Exception e) {

			e.printStackTrace();
		}

		return list;
	}

	@Override
	public boolean addSuggetionTemplate(SuggestionTemplate temp) {
		LOGGER.error("# INSIDE ADD SUGGETIONTEMPLATE API DAO:");
		boolean flag = false;
		Session session = getCurrentSession();
		try {
			Object save = session.save(temp);
			if (save != null) {
				flag = true;
			}

		} catch (Exception e) {
			LOGGER.error("# INSIDE EXCEPTION OCCURED IN ADD SUGGETIONTEMPLATE API DAO:" + e.getMessage());
		}
		return flag;
	}

	@Override
	public List<EmployeeDetails> getEmpRolesListNew(EmployeeDetails empdetails) {
		LOGGER.error("# INSIDE ADD SUGGETIONTEMPLATE API DAO:");
		Session session = getCurrentSession();
		List<EmployeeDetails> empList = new ArrayList<>();
		List<Object[]> empListObj = new ArrayList<>();
		try {
			StringBuffer strString = new StringBuffer("SELECT \r\n" + "    e.emp_id,\r\n" + "    e.cmpy_emp_id,\r\n"
					+ "    e.first_name,\r\n" + "    e.last_name,\r\n" + "    e.email_id,\r\n" + "    e.contact_no,\r\n"
					+ "    e.profile_pic,\r\n" + "    e.points,\r\n" + "    e.designation,\r\n"
					+ "    e.is_email_verified,\r\n" + "    e.logged_in,\r\n" + "    e.branch_id,\r\n"
					+ "    b.name,\r\n" + "    e.dept_id,\r\n" + "    d.dept_name,\r\n" + "    r.name AS roleName,\r\n"
					+ "    er.role_id\r\n" + "FROM\r\n" + "    tbl_employee_details e\r\n" + "        LEFT JOIN\r\n"
					+ "    master_department d ON d.dept_id = e.dept_id\r\n" + "        LEFT JOIN\r\n"
					+ "    master_branch b ON b.branch_id = e.branch_id\r\n" + "        LEFT JOIN\r\n"
					+ "    emp_roles er on er.emp_id=e.emp_id\r\n" + "        LEFT JOIN\r\n"
					+ "    roles r ON r.id = er.role_id \r\n" + "WHERE\r\n" + "    er.role_id=:roleId");
			if (empdetails.getIsOrgExecutive() == EnovationConstants.ONE && empdetails.getOrgId() > 0) {
				strString.append(" and e.org_id=orgId");

			} else {
				strString.append(" and e.branch_id in (Ids)");
			}
			empListObj = session
					.createNativeQuery((strString.toString()).replace("Ids", StringUtils.join(empdetails.getIds(), ','))
							.replace("orgId", String.valueOf(empdetails.getOrgId())))
					.setParameter("roleId", empdetails.getRoleId()).getResultList();

			for (Object[] emp : empListObj) {
				EmployeeDetails employee = new EmployeeDetails();
				if (emp[0] != null) {
					employee.setEmpId(Integer.valueOf(String.valueOf(emp[0])));
				}
				employee.setCmpyEmpId(String.valueOf(emp[1]));
				employee.setFirstName(String.valueOf(emp[2]));
				employee.setLastName(String.valueOf(emp[3]));
				employee.setEmailId(String.valueOf(emp[4]));
				employee.setContactNo(String.valueOf(emp[5]));
				if (emp[6] != null) {
					employee.setProfilePic(String.valueOf(emp[6]));
				}
				if (emp[7] != null) {
					employee.setPoints(Integer.valueOf(String.valueOf(emp[7])));
				}
				employee.setDesignation(String.valueOf(emp[8]));
				if (emp[9] != null) {
					employee.setIsEmailVerified(Integer.valueOf(String.valueOf(emp[9])));
				}
				if (emp[10] != null) {
					employee.setLoggedIn(Integer.valueOf(String.valueOf(emp[10])));
				}
				if (emp[11] != null) {
					employee.setBranchId(Integer.valueOf(String.valueOf(emp[11])));
				}
				employee.setBranchName(String.valueOf(emp[12]));
				if (emp[13] != null) {
					employee.setDeptId(Integer.valueOf(String.valueOf(emp[13])));
				}
				if (emp[14] != null) {
					employee.setDeptName(String.valueOf(emp[14]));
				}
				employee.setRoleId(Long.valueOf(String.valueOf(emp[16])));
				employee.setRoleName(String.valueOf(emp[15]));
				empList.add(employee);
			}

		} catch (Exception e) {
			LOGGER.error("# INSIDE EXCEPTION OCCURED IN GET EMPLOYEE ROLES LIST NEW API DAO:" + e.getMessage());
			e.printStackTrace();
		}
		return empList;
	}

	@Override
	public List<EmployeeDetails> getAllEmpListByBrannchIds(EmployeeDTO request) {
		List<Object[]> empList = null;
		List<EmployeeDetails> employeeList = new ArrayList<>();
		LOGGER.info("# INSIDE IN GETALLEMPLYEELISTBYBRANCHNEW ");
		Session session = getCurrentSession();
		StringBuffer strString = new StringBuffer(
				"SELECT e.emp_id, e.cmpy_emp_id, e.first_name, e.last_name, e.email_id, e.contact_no, "
						+ " e.profile_pic, e.points, e.designation, e.is_email_verified, e.logged_in, "
						+ " e.branch_id, b.name, e.dept_id, d.dept_name, "
						+ " r.name as roleName, er.role_id, e.dob, e.doa,line.id as lineid,line.name as linename,"
						+ "  e.emp_level_id as lvl_id, eh.level_name,eh.level_type,e.doj as date_of_joining,e.is_deactive, "
						+ " e.address as address,e.middle_name as middleName "
						+ " FROM tbl_employee_details  e left join master_department d ON d.dept_id=e.dept_id "
						+ " LEFT JOIN master_branch b ON b.branch_id=e.branch_id "
						+ " LEFT JOIN emp_roles er ON er.emp_id=e.emp_id left join roles r  ON r.id=er.role_id   "
						+ " LEFT JOIN employee_hierarchy eh ON eh.emp_lvl_id = e.emp_level_id "
						+ " LEFT JOIN dwm_line line on line.id=e.line_id" + " WHERE");
		if (request.getStatus().equals(EnovationConstants.ALL)) {
			strString.append(" e.branch_id IN (:ID)");
		} else {
			strString.append(" e.branch_id IN (:ID) AND e.is_deactive = 0");
		}
		strString.append("  and e.emp_type='EMPLOYEE' ");

		empList = session.createNativeQuery(strString.toString()).setParameter("ID", request.getBranchIds())
				.getResultList();
		for (Object[] emp : empList) {
			EmployeeDetails employee = new EmployeeDetails();
			if (emp[0] != null) {
				employee.setEmpId(Integer.valueOf(String.valueOf(emp[0])));
			}
			employee.setCmpyEmpId(String.valueOf(emp[1]));
			employee.setFirstName(String.valueOf(emp[2]));
			employee.setLastName(String.valueOf(emp[3]));
			employee.setEmailId(String.valueOf(emp[4]));
			employee.setContactNo(String.valueOf(emp[5]));
			if (emp[6] != null) {
				employee.setProfilePic(String.valueOf(emp[6]));
			}
			if (emp[7] != null) {
				employee.setPoints(Integer.valueOf(String.valueOf(emp[7])));
			}
			employee.setDesignation(String.valueOf(emp[8]));
			if (emp[9] != null) {
				employee.setIsEmailVerified(Integer.valueOf(String.valueOf(emp[9])));
			}
			if (emp[10] != null) {
				employee.setLoggedIn(Integer.valueOf(String.valueOf(emp[10])));
			}
			if (emp[11] != null) {
				employee.setBranchId(Integer.valueOf(String.valueOf(emp[11])));
			}
			employee.setBranchName(String.valueOf(emp[12]));
			if (emp[13] != null) {
				employee.setDeptId(Integer.valueOf(String.valueOf(emp[13])));
			}
			if (emp[14] != null) {
				employee.setDeptName(String.valueOf(emp[14]));
			}
			if (emp[16] != null) {
				employee.setRoleId(Long.valueOf(String.valueOf(emp[16])));
			}
			if (emp[15] != null) {
				employee.setRoleName(String.valueOf(emp[15]));
			}
			if (emp[17] != null) {
				employee.setDOB(CommonUtils.convertStringToSqlDate(String.valueOf(emp[17])));
			}
			if (emp[18] != null) {
				employee.setDOA(CommonUtils.convertStringToSqlDate(String.valueOf(emp[18])));
			}
			if (emp[19] != null && emp[20] != null) {
				Line line = new Line(Long.valueOf(String.valueOf(emp[19])), String.valueOf(emp[20]),
						Integer.valueOf(String.valueOf(emp[13])));
				employee.setLine(line);
			}

			if (emp[21] != null) {
				employee.setEmpLvlId(Integer.parseInt(String.valueOf(emp[21])));
			}
			if (emp[22] != null) {
				employee.setEmpLvlName(String.valueOf(emp[22]));
			}
			if (emp[23] != null) {
				employee.setEmpLvlType(String.valueOf(emp[23]));
			}

			if (emp[24] != null) {
				employee.setDoj(String.valueOf(emp[24]));
			}
			if (emp[25] != null) {
				employee.setIsDeactive(Integer.parseInt(String.valueOf(emp[25])));
			}
			if (emp[26] != null) {
				employee.setAddress(String.valueOf(emp[26]));
			}
			if (emp[27] != null) {
				employee.setMiddleName(String.valueOf(emp[27]));
			}

			employeeList.add(employee);
		}
		return employeeList;
	}

	@Override
	public Map<String, Object> saveLoginFeedback(Feedback feedback) {
		LOGGER.info("#SAVE_LOGIN_FEEDBACK IMPL STARTED");
		Session session = getCurrentSession();
		List<MailDTO> mailList = new ArrayList<>();
		Map<String, Object> res = new HashMap<>();
		res.put("flag", false);
		int actionSize = 0;
		try {
			/*****
			 * Fetch Email Template master body
			 ***/
			EmailTemplateMaster messageContent = enoConfig.getMessageContent(EnovationConstants.MASTER_EMAIL_TEMPLATE);
			actionSize = ((Number) session.createNativeQuery(QueryConstants.saveLoginFeedback_GET_FEEDBACK_COUNT_DATA)
					.getSingleResult()).intValue();
			/*****
			 * CREATE SRN NUMBER USING YEAR+MONTH+FEEDBACK TABLE DATA COUNT
			 ***/
			feedback.setSrn(CommonUtils.leadingWithZeros((Calendar.getInstance().get(Calendar.YEAR)), 4) + "-"
					+ CommonUtils.leadingWithZeros((Calendar.getInstance().get(Calendar.MONTH) + 1), 2) + "-"
					+ CommonUtils.leadingWithZeros(++actionSize, 4));
			res.put("srNumber", feedback.getSrn());
			if (session.save(feedback) != null) {
				/*****
				 * AFTER FEEDBACK DATA SAVING TO DB --> PREPARE DATA FOR SENDING MAILS
				 ***/
				itrateFeedbackDataListWithPrepareEmailBody(
						((List<Object[]>) session.createNativeQuery(QueryConstants.saveLoginFeedback_GET_FEEDBACK_DATA)
								.setParameter("feedId", feedback.getFeedbackId()).getResultList()),
						messageContent, feedback, res, mailList);
				res.put("flag", true);
			}
		} catch (Exception e) {
			LOGGER.error("#INSIDE EXCEPTION OCCURED IN SAVE_LOGIN_FEEDBACK" + ExceptionUtils.getStackTrace(e));
		}
		if (!mailList.isEmpty())
			taskExecutor.execute(new MailUtil(mailList, communication));
		return res;
	}

	/*****
	 * PREPARING DATA( SUBJECT & BODY ) TO SEND EMAILS
	 ***/
	private void itrateFeedbackDataListWithPrepareEmailBody(List<Object[]> data, EmailTemplateMaster messageContent,
															Feedback feedback, Map<String, Object> res, List<MailDTO> mailList) {
		if (data == null || data.isEmpty())
			itrateFeedbackDataList(null, data, messageContent, feedback, res);

		data.stream().forEach(row -> {
			itrateFeedbackDataList(row, data, messageContent, feedback, res);
		});
		Arrays.asList(EnovationConstants.SUPPORT_TEAM_EMAILS).stream().forEach(x -> {
			mailList.add(
					(new MailDTO(x, env.getProperty("QUERY.subject"), String.valueOf(res.get("emailTemplateBody")))));
		});
	}

	/*****
	 * ITERATE FEEDBACK LIST TO PREPARING DATA( SUBJECT & BODY ) TO SEND EMAILS
	 ***/
	private void itrateFeedbackDataList(Object[] row, List<Object[]> data, EmailTemplateMaster messageContent,
										Feedback feedback, Map<String, Object> res) {

		SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");
		// *******************ORG USER QUERY*****************************//
		res.put("emailTemplateBody", messageContent.getBody()
				.replaceAll(Pattern.quote("{portalLink}"),
						(data != null && !data.isEmpty()) ? ((row[10] != null) ? String.valueOf(row[10]) : "") : "")

				.replaceAll(Pattern.quote("{name}"), EnovationConstants.ENOVATION_TEAM)

				.replaceAll(Pattern.quote("{body}"),
						(env.getProperty(((data != null && !data.isEmpty()) ? "LOGIN_QUERY_BODY.body"
								: "LOGIN_SUPPORT_QUERY_BODY.body"))))

				.replaceAll(Pattern.quote("{query_number}"), feedback.getSrn())

				.replaceAll(Pattern.quote("{feedback}"),
						(data != null && !data.isEmpty()) ? ((row[0] != null) ? String.valueOf(row[0]) : "")
								: feedback.getFeedback())

				.replaceAll(Pattern.quote("{name}"),
						(data != null && !data.isEmpty()) ? ((row[7] != null) ? String.valueOf(row[7]) : "")
								: feedback.getUserName())

				.replaceAll(Pattern.quote("{email}"),
						(data != null && !data.isEmpty())
								? ((row[8] != null) ? String.valueOf(row[8]) : feedback.getUserName())
								: "")

				.replaceAll(Pattern.quote("{contact}"),
						(data != null && !data.isEmpty()) ? ((row[11] != null) ? String.valueOf(row[11]) : "") : "")

				.replaceAll(Pattern.quote("{date}"),
						(data != null && !data.isEmpty()) ? ((row[1] != null)
								? formatter.format(CommonUtils.convertStringToUtilDate(String.valueOf(row[1])))
								: "") : formatter.format(feedback.getCreatedDate()))

				.replaceAll(Pattern.quote("{source_type}"),
						(data != null && !data.isEmpty()) ? ((row[9] != null) ? String.valueOf(row[9]) : "")
								: feedback.getSourceType())

				.replaceAll(Pattern.quote("{org_name}"),
						(data != null && !data.isEmpty()) ? ((row[6] != null) ? String.valueOf(row[6]) : "")
								: (feedback.getOrgName() != null) ? feedback.getOrgName() : ""));
	}

	@Override
	public List<EmployeeHierarchy> getEmployeeLevelDetails(int branchId) {
		LOGGER.info("# GET EMPLOYEE LEVEL IMPL ");
		Session session = getCurrentSession();
		List<EmployeeHierarchy> list = new ArrayList<>();
		try {
			list = session.createQuery("FROM EmployeeHierarchy WHERE branchId=:branchId")
					.setParameter("branchId", branchId).getResultList();
		} catch (Exception e) {
			LOGGER.error("#INSIDE EXCEPTION OCCURED IN GET EMPLOYEE LEVEL" + ExceptionUtils.getStackTrace(e));
		}
		return list;
	}

	/**
	 * @author Vinay B. Dec 27, 2021 1:50:54 PM
	 */
	@Override
	public EmployeeDTO getModuleWisePendingActions(int empId) {
		LOGGER.info("# Inside getModuleWisePendingActions Dao - Emp ID -> " + empId);
		// Get all pending action counts & List
		return checkIfEmployeeHasPendingItems(empId, getCurrentSession());

	}

	private EmployeeDTO checkIfEmployeeHasPendingItems(int empId, Session session) {
		LOGGER.info("# Inside getModuleWisePendingActions Dao - Emp ID -> " + empId);
		EmployeeDTO obj = new EmployeeDTO();
		// 1) Suggestion Pending List & Count

		List<HashMap<String, Object>> pendingSuggList = getPendingSuggestionList(empId, session);
		obj.setPendingSuggCount(pendingSuggList != null ? pendingSuggList.size() : 0);
		obj.setPendingSuggList(pendingSuggList != null ? pendingSuggList : null);

		// 2) Near-miss Pending List & Count

		List<HashMap<String, Object>> pendingNearmissList = getPendingNearmissList(empId, session);
		obj.setNearmissCount(pendingNearmissList != null ? pendingNearmissList.size() : 0);
		obj.setPendingNearmissList(pendingNearmissList != null ? pendingNearmissList : null);

		// 3) Skill Book Pending List & Count

		List<HashMap<String, Object>> skillBookPendingList = getPendingSkillBookList(empId, session);
		obj.setSkillBookCount(skillBookPendingList != null ? skillBookPendingList.size() : 0);
		obj.setSkillBookPendingList(skillBookPendingList != null ? skillBookPendingList : null);

		// 4) PMS Pending List & Count

		List<HashMap<String, Object>> pmsPendingList = getPendingPMSList(empId, session);
		obj.setPmsCount(pmsPendingList != null ? pmsPendingList.size() : 0);
		obj.setPmsPendingList(pmsPendingList != null ? pmsPendingList : null);

		// 5) Kuber Pending List & Count

		List<HashMap<String, Object>> kuberPendingList = getPendingKuberProjectList(empId, session);
		obj.setKuberPendingCount(kuberPendingList != null ? kuberPendingList.size() : 0);
		obj.setKuberPendingList(kuberPendingList != null ? kuberPendingList : null);

		// 6) Audit Pending List & Count

		List<HashMap<String, Object>> auditPendingList = getPendingAuditList(empId, session);
		obj.setAuditPendingCount(auditPendingList != null ? auditPendingList.size() : 0);
		obj.setAuditPendingList(auditPendingList != null ? auditPendingList : null);

		// 7) DWM Pending List & Count

		List<HashMap<String, Object>> dwmPendingList = getPendingDwmList(empId, session);
		obj.setDwmPendingCount(dwmPendingList != null ? dwmPendingList.size() : 0);
		obj.setDwmPendingList(dwmPendingList != null ? dwmPendingList : null);

		// 8) TAG Pending List & Count

		List<HashMap<String, Object>> tagPendingList = getPendingTpmList(empId, session);
		obj.setTpmPendingCount(tagPendingList != null ? tagPendingList.size() : 0);
		obj.setTpmPendingList(tagPendingList != null ? tagPendingList : null);

		List<HashMap<String, Object>> concernPendingList = getPendingConcernList(empId, session);
		obj.setConcernPendingCount(concernPendingList != null ? concernPendingList.size() : 0);
		obj.setConcernPendingList(concernPendingList != null ? concernPendingList : null);

		List<HashMap<String, Object>> skillmatrixPendingList = getPendingSkillmatrixList(empId, session);
		obj.setSkillmatrixPendingCount(skillmatrixPendingList != null ? skillmatrixPendingList.size() : 0);
		obj.setSkillmatrixPendingList(skillmatrixPendingList != null ? skillmatrixPendingList : null);

		// int concernCount = getPendingConcernCount(empId, session);

		if (obj.getPendingSuggCount() == 0 && obj.getSkillBookCount() == 0 && obj.getPmsCount() == 0
				&& obj.getAuditPendingCount() == 0 && obj.getKuberPendingCount() == 0 && obj.getTpmPendingCount() == 0
				&& obj.getDwmPendingCount() == 0 && obj.getNearmissCount() == 0 && obj.getConcernPendingCount() == 0) {

			// deactivateEmployee(empId, session);
			obj.setActionPendingWithEmp(false);

		} else {
			obj.setActionPendingWithEmp(true);
		}
		return obj;
	}

	/**
	 * @author Vinay B. May 18, 2022 3:01:26 PM
	 */
	@Override
	public int deactivateEmployee(int empId) {
		LOGGER.info("# Inside deactivateEmployee Dao - Emp ID -> " + empId);
		int flag = 0;
		Session session = getCurrentSession();
		EmployeeDTO obj = checkIfEmployeeHasPendingItems(empId, session);
		LOGGER.info("# Action pending with Employe  ----> " + obj.isActionPendingWithEmp());
		if (obj.isActionPendingWithEmp()) {
			flag = 2;
		} else {
			EmployeeDetails ed = session.load(EmployeeDetails.class, empId);
			if (ed != null) {
				ed.setIsDeactive(1);
				ed.setInactiveFrom(LocalDate.now().toString());
				ed.setDeactivationDate(CommonUtils.convertStringToSqlDate(LocalDate.now().toString()));
				session.update(ed);
				flag = 1;
			}
		}
		return flag;
	}

	/**
	 * @author Vinay B. May 17, 2022 11:47:52 AM
	 */
	private List<HashMap<String, Object>> getPendingConcernList(int empId, Session session) {
		LOGGER.info("# Inside getPendingConcernList Dao - Emp ID -> " + empId);
		List<HashMap<String, Object>> list = new ArrayList<>();
		List<Tuple> tupleList = (List<Tuple>) session.createNativeQuery(
				"SELECT con.concern_id as concernId,concat(ifnull(ed.first_name,''),' ',ifnull(ed.last_name,'')) as empName,\r\n"
						+ " sts.label as concernStatus,\r\n"
						+ " con.concern_title as concernTitle, con.concern_number as concernNumber\r\n"
						+ " FROM concern_details con  \r\n"
						+ " INNER  JOIN   master_status sts ON  con.sts_id = sts.status_id \r\n"
						+ " INNER JOIN  tbl_employee_details ed ON con.emp_id = ed.emp_id  \r\n"
						+ " INNER JOIN master_branch branch ON ed.branch_id = branch.branch_id  \r\n"
						+ " WHERE con.under_action=:empId",
				Tuple.class).setParameter("empId", empId).getResultList();

		if (CollectionUtils.isNotEmpty(tupleList)) {
			LOGGER.info("# Concern list is not empty - size --> " + tupleList.size());
			for (Tuple x : tupleList) {
				HashMap<String, Object> obj = new HashMap<String, Object>();
				obj.put("concernId", CommonUtils.objectToInt(x.get("concernId")));
				obj.put("empName", CommonUtils.objectToString(x.get("empName")));
				obj.put("concernStatus", CommonUtils.objectToString(x.get("concernStatus")));
				obj.put("concernTitle", CommonUtils.objectToString(x.get("concernTitle")));
				obj.put("concernNumber", CommonUtils.objectToString(x.get("concernNumber")));
				list.add(obj);
			}
		} else {
			LOGGER.info("# Concern list is empty - size --> " + tupleList.size());
		}
		return list;
	}

//	private List<HashMap<String, Object>> getPendingSkillmatrixList(int empId, Session session) {
//		LOGGER.info("# Inside getPendingSkillmatrixList Dao - Emp ID -> " + empId);
//		List<HashMap<String, Object>> list = new ArrayList<>();
//		List<Tuple> tupleList = (List<Tuple>) session.createNativeQuery(
//				"SELECT audit.id as auditId, " +
//						"ed.emp_id as empId, " +
//						"concat(ifnull(ed.first_name,''),' ',ifnull(ed.last_name,'')) as empName, " +
//						"md.dept_name as department, " +
//						"l.name as cell, " +
//						"ws.workstation as workstation, " +
//						"audit.skilling_id as skillingId, " +
//						"ojtSkilling.ojt_regis_id as ojtRegisId, " +
//						"audit.status as skillingStatus, " +
//						"ojtSkilling.status as skillingTitle, " +
//						"ojtCheckseet.day_no as skillingNumber, " +
//						"mut.user_type as userType, " +
//						"audit.stage_id as stageId " +
//						"FROM sm_ojt_skilling_audit audit " +
//						"INNER JOIN sm_ojt_skilling ojtSkilling ON ojtSkilling.id = audit.skilling_id " +
//						"INNER JOIN sm_ojt_skilling_checksheet ojtCheckseet ON ojtCheckseet.id = audit.skilling_checksheet_id " +
//						"INNER JOIN sm_ojt_regis regis ON regis.id = ojtSkilling.ojt_regis_id " +
//						"INNER JOIN tbl_employee_details ed ON audit.emp_id = ed.emp_id " +
//						"LEFT JOIN master_department md ON ed.dept_id = md.dept_id " +
//						"LEFT JOIN dwm_line l ON ed.line_id = l.id " +
//						"LEFT JOIN sm_workstations ws ON regis.workstation_id = ws.id " +
//						"LEFT JOIN sm_user_type ut ON ed.emp_id = ut.emp_id AND ut.is_active = 1 " +
//						"LEFT JOIN sm_master_user_type mut ON ut.user_type_id = mut.id " +
//						"WHERE audit.emp_id = :empId AND audit.status = 'PENDING' " +
//						"GROUP BY audit.id, ed.emp_id, ed.first_name, ed.last_name, md.dept_name, l.name, ws.workstation, " +
//						"audit.skilling_id, ojtSkilling.ojt_regis_id, audit.status, ojtSkilling.status, ojtCheckseet.day_no",
//				Tuple.class).setParameter("empId", empId).getResultList();
//
//		if (CollectionUtils.isNotEmpty(tupleList)) {
//			LOGGER.info("# Skill matrix list is not empty - size --> " + tupleList.size());
//			for (Tuple x : tupleList) {
//				HashMap<String, Object> obj = new HashMap<String, Object>();
//				obj.put("empId", CommonUtils.objectToInt(x.get("empId")));
//				obj.put("empName", CommonUtils.objectToString(x.get("empName")));
//				obj.put("department", CommonUtils.objectToString(x.get("department")));
//				obj.put("cell", CommonUtils.objectToString(x.get("cell")));
//				obj.put("workstation", CommonUtils.objectToString(x.get("workstation")));
//				obj.put("skillingId", CommonUtils.objectToInt(x.get("skillingId")));
//				obj.put("ojtRegisId", CommonUtils.objectToInt(x.get("ojtRegisId")));
//				obj.put("skillingStatus", CommonUtils.objectToString(x.get("skillingStatus")));
//				obj.put("skillingTitle", CommonUtils.objectToString(x.get("skillingTitle")));
//				obj.put("skillingNumber", CommonUtils.objectToString(x.get("skillingNumber")));
//				obj.put("userType", CommonUtils.objectToString(x.get("userType")));
//				obj.put("stageId", CommonUtils.objectToString(x.get("stageId")));
//				list.add(obj);
//			}
//		} else {
//			LOGGER.info("# Skill matrix list is empty - size --> " + tupleList.size());
//		}
//		return list;
//	}

	private List<HashMap<String, Object>> getPendingSkillmatrixList(int empId, Session session) {
		LOGGER.info("# Inside getPendingSkillmatrixList Dao - Emp ID -> " + empId);

		List<HashMap<String, Object>> resultList = new ArrayList<>();

		String sql =
				"SELECT * FROM ( " +
						"    SELECT " +
						"        audit.id AS auditId, " +
						"        ed.emp_id AS empId, " +
						"        ed.cmpy_emp_id AS cmpy_emp_id, " +
						"        CONCAT(IFNULL(ed.first_name, ''), ' ', IFNULL(ed.last_name, '')) AS empName, " +
						"        md.dept_name AS department, " +
						"        l.name AS cell, " +
						"        ws.workstation AS workstation, " +
						"        audit.skilling_id AS skillingId, " +
						"        ojtSkilling.ojt_regis_id AS ojtRegisId, " +
						"        audit.status AS skillingStatus, " +
						"        ojtSkilling.status AS skillingTitle, " +
						"        ojtChecksheet.day_no AS skillingNumber, " +
						"        mut.user_type AS userType, " +
						"        audit.stage_id AS stageId, " +
						"        skill_level.level_name AS level, " +
						"		 stage_label.stage_label AS activity, " +
						"        CASE " +
						"            WHEN stage.stage_name = 'Stage 1' THEN 'OE' " +
						"            WHEN stage.stage_name IN ('Stage 2', 'Stage2 verification') THEN 'Trainer' " +
						"            WHEN stage.stage_name = 'Stage 3' THEN 'QA' " +
						"            WHEN stage.stage_name = 'Stage 4' THEN 'TL' " +
						"            WHEN stage.stage_name = 'Stage 5' THEN 'Assessment' " +
						"            ELSE stage.stage_name " +
						"        END AS role, " +
						"        ROW_NUMBER() OVER ( " +
						"            PARTITION BY ed.emp_id, audit.stage_id, audit.skilling_id, ojtSkilling.ojt_regis_id " +
						"            ORDER BY audit.id DESC " +
						"        ) AS rn " +
						"    FROM sm_ojt_skilling_audit audit " +
						"    INNER JOIN sm_ojt_skilling ojtSkilling ON ojtSkilling.id = audit.skilling_id " +
						"    INNER JOIN sm_ojt_skilling_checksheet ojtChecksheet ON ojtChecksheet.id = audit.skilling_checksheet_id " +
						"    INNER JOIN sm_ojt_regis regis ON regis.id = ojtSkilling.ojt_regis_id " +
						"    INNER JOIN tbl_employee_details ed ON regis.emp_id = ed.emp_id " +
						"    LEFT JOIN master_department md ON ed.dept_id = md.dept_id " +
						"    LEFT JOIN dwm_line l ON ed.line_id = l.id " +
						"    LEFT JOIN sm_workstations ws ON regis.workstation_id = ws.id " +
						"    LEFT JOIN sm_user_type ut ON ed.emp_id = ut.emp_id AND ut.is_active = 1 " +
						"    LEFT JOIN sm_master_user_type mut ON ut.user_type_id = mut.id " +
						"    LEFT JOIN sm_stage stage ON stage.id = audit.stage_id " +
						"    LEFT JOIN sm_skill_level skill_level ON skill_level.id = regis.desired_skill_level_id " +
						"    LEFT JOIN sm_stage_label stage_label ON stage_label.id = audit.stage_id " +
						"    WHERE audit.emp_id = :empId AND audit.status = 'PENDING' " +
						") t " +
						"WHERE t.rn = 1";

		List<Tuple> tuples = session.createNativeQuery(sql, Tuple.class)
				.setParameter("empId", empId)
				.getResultList();

		if (CollectionUtils.isNotEmpty(tuples)) {
			LOGGER.info("# Skill matrix list found - size --> " + tuples.size());
			for (Tuple row : tuples) {
				HashMap<String, Object> record = new HashMap<>();
				record.put("emp_id", CommonUtils.objectToInt(row.get("empId")));
				record.put("cmpy_emp_id", CommonUtils.objectToString(row.get("cmpy_emp_id")));
				record.put("empName", CommonUtils.objectToString(row.get("empName")));
				record.put("department", CommonUtils.objectToString(row.get("department")));
				record.put("cell", CommonUtils.objectToString(row.get("cell")));
				record.put("workstation", CommonUtils.objectToString(row.get("workstation")));
				record.put("skillingId", CommonUtils.objectToInt(row.get("skillingId")));
				record.put("ojtRegisId", CommonUtils.objectToInt(row.get("ojtRegisId")));
				record.put("skillingStatus", CommonUtils.objectToString(row.get("skillingStatus")));
				record.put("skillingTitle", CommonUtils.objectToString(row.get("skillingTitle")));
				record.put("skillingNumber", CommonUtils.objectToString(row.get("skillingNumber")));
				record.put("userType", CommonUtils.objectToString(row.get("userType")));
				record.put("stageId", CommonUtils.objectToString(row.get("stageId")));
				record.put("level", CommonUtils.objectToString(row.get("level")));
				record.put("activity", CommonUtils.objectToString(row.get("activity")));
				record.put("role", CommonUtils.objectToString(row.get("role")));
				resultList.add(record);
			}
		} else {
			LOGGER.info("# Skill matrix list is empty for empId " + empId);
		}

		return resultList;
	}


	/**
	 * @author Vinay B. May 17, 2022 11:47:29 AM
	 */
	private List<HashMap<String, Object>> getPendingTpmList(int empId, Session session) {
		LOGGER.info("# Inside getPendingTpmList Dao - Emp ID -> " + empId);
		List<HashMap<String, Object>> list = new ArrayList<>();
		List<Tuple> tupleList = (List<Tuple>) session
				.createNativeQuery("select td.id as tagId,td.tag_number as tagNum,\r\n"
						+ " tt.type as tagType,md.dept_name as deptName,ms.label as tagStatus\r\n"
						+ " from tag_status_audit tsa\r\n" + "INNER JOIN tag_details td on td.id = tsa.tag_id\r\n"
						+ " LEFT JOIN tpm_tag tt on tt.id = td.tag_type_id\r\n"
						+ " INNER JOIN master_status ms on ms.status_id = tsa.sts_id\r\n"
						+ " INNER JOIN master_department md on md.dept_id = td.dept_id\r\n"
						+ " where tsa.emp_id=:empId and tsa.action_status='PENDING'", Tuple.class)
				.setParameter("empId", empId).getResultList();

		if (CollectionUtils.isNotEmpty(tupleList)) {
			LOGGER.info("# Tag list is not empty - size --> " + tupleList.size());
			for (Tuple x : tupleList) {
				HashMap<String, Object> obj = new HashMap<String, Object>();
				obj.put("tagId", CommonUtils.objectToInt(x.get("tagId")));
				obj.put("tagNum", CommonUtils.objectToInt(x.get("tagNum")));
				obj.put("tagType", CommonUtils.objectToString(x.get("tagType")));
				obj.put("deptName", CommonUtils.objectToString(x.get("deptName")));
				obj.put("tagStatus", CommonUtils.objectToString(x.get("tagStatus")));
				list.add(obj);
			}
		} else {
			LOGGER.info("# Tag list is empty - size --> " + tupleList.size());
		}

		return list;

		/*
		 * return ((Number) session.createNativeQuery(
		 * "select coalesce(count(id),0) as tpmCount from tag_status_audit where emp_id=:empId and action_status='PENDING'"
		 * ) .setParameter("empId", empId).getSingleResult()).intValue();
		 */
	}

	/**
	 * @author Vinay B. May 17, 2022 11:47:10 AM
	 */
	private List<HashMap<String, Object>> getPendingDwmList(int empId, Session session) {
		LOGGER.info("# Inside getPendingDwmList Dao - Emp ID -> " + empId);
		List<HashMap<String, Object>> list = new ArrayList<>();
		List<Tuple> tupleList = (List<Tuple>) session
				.createNativeQuery("select dd.id as dwmId,dd.dwm_number as issueId,md.dept_name as deptName,\r\n"
						+ " dl.name as lineName,dt.name as topicName,ms.label as dwmStatus\r\n"
						+ " from dwm_status_audit dsa\r\n" + "INNER JOIN dwm_details dd on dd.id = dsa.dwm_id\r\n"
						+ " INNER JOIN master_status ms on ms.status_id = dsa.sts_id\r\n"
						+ " LEFT JOIN dwm_topic dt on dt.id = dd.topic_id\r\n"
						+ " LEFT JOIN dwm_line dl on dl.id = dd.line_id\r\n"
						+ " LEFT JOIN master_department md on md.dept_id = dl.dept_id\r\n"
						+ " where dsa.emp_id=:empId and dsa.action_status='PENDING'", Tuple.class)
				.setParameter("empId", empId).getResultList();

		if (CollectionUtils.isNotEmpty(tupleList)) {
			LOGGER.info("# DWM list is not empty - size --> " + tupleList.size());
			for (Tuple x : tupleList) {
				HashMap<String, Object> obj = new HashMap<String, Object>();
				obj.put("dwmId", CommonUtils.objectToInt(x.get("dwmId")));
				obj.put("issueId", CommonUtils.objectToString(x.get("issueId")));
				obj.put("deptName", CommonUtils.objectToString(x.get("deptName")));
				obj.put("lineName", CommonUtils.objectToString(x.get("lineName")));
				obj.put("topicName", CommonUtils.objectToString(x.get("topicName")));
				obj.put("dwmStatus", CommonUtils.objectToString(x.get("dwmStatus")));
				list.add(obj);
			}
		} else {
			LOGGER.info("# DWM list is empty - size --> " + tupleList.size());
		}

		return list;

		/*
		 * return ((Number) session.createNativeQuery(
		 * "select coalesce(count(id),0) as dwnCount from dwm_status_audit where emp_id=:empId and action_status='PENDING'"
		 * ) .setParameter("empId", empId).getSingleResult()).intValue();
		 */
	}

	/**
	 * @author Vinay B. May 17, 2022 11:46:50 AM
	 */
	private List<HashMap<String, Object>> getPendingAuditList(int empId, Session session) {
		LOGGER.info("# Inside getPendingAuditList Dao - Emp ID -> " + empId);
		List<HashMap<String, Object>> list = new ArrayList<>();
		List<Tuple> tupleList = (List<Tuple>) session
				.createNativeQuery("select aas.inspection_id as auditId,\r\n"
						+ " ate.title as auditTitle,aty.audit_type as auditType,\r\n" + "ms.label as auditStatus\r\n"
						+ " from audit_action_status aas\r\n"
						+ " INNER JOIN audit_inspection_details ai on ai.id = aas.inspection_id\r\n"
						+ " INNER JOIN audit_template ate on ate.id = ai.template_id\r\n"
						+ " INNER JOIN audit_type aty on aty.id = ate.audit_type_id\r\n"
						+ " INNER JOIN master_status ms on ms.status_id = aas.sts_id\r\n"
						+ " where aas.emp_id=:empId and aas.action_status='PENDING'", Tuple.class)
				.setParameter("empId", empId).getResultList();

		if (CollectionUtils.isNotEmpty(tupleList)) {
			LOGGER.info("# Audit list is not empty - size --> " + tupleList.size());
			for (Tuple x : tupleList) {
				HashMap<String, Object> obj = new HashMap<String, Object>();
				obj.put("auditId", CommonUtils.objectToInt(x.get("auditId")));
				obj.put("auditTitle", CommonUtils.objectToString(x.get("auditTitle")));
				obj.put("auditType", CommonUtils.objectToString(x.get("auditType")));
				obj.put("auditStatus", CommonUtils.objectToString(x.get("auditStatus")));
				list.add(obj);
			}
		} else {
			LOGGER.info("# Audit list is empty - size --> " + tupleList.size());
		}

		return list;

		/*
		 * return ((Number) session.createNativeQuery(
		 * "select coalesce(count(id),0) as auditCount from audit_action_status where emp_id=:empId and action_status='PENDING'"
		 * ) .setParameter("empId", empId).getSingleResult()).intValue();
		 */
	}

	/**
	 * @author Vinay B. May 17, 2022 11:46:12 AM
	 */
	private List<HashMap<String, Object>> getPendingKuberProjectList(int empId, Session session) {
		LOGGER.info("# Inside getPendingKuberProjectList Dao - Emp ID -> " + empId);
		List<HashMap<String, Object>> list = new ArrayList<>();
		List<Tuple> tupleList = (List<Tuple>) session
				.createNativeQuery("select kp.project_name as projName,kp.unique_project_num as projNum,\r\n"
						+ " ms.label as projStatus,kp.proj_id as projId \r\n" + "from kuber_status_audit ksa\r\n"
						+ " INNER JOIN kuber_project kp on kp.proj_id = ksa.project_id\r\n"
						+ " INNER JOIN master_status ms on ms.status_id = ksa.sts_id\r\n"
						+ " where ksa.emp_id=:empId and ksa.action_status='PENDING'", Tuple.class)
				.setParameter("empId", empId).getResultList();

		if (CollectionUtils.isNotEmpty(tupleList)) {
			LOGGER.info("# Kuber list is not empty - size --> " + tupleList.size());
			for (Tuple x : tupleList) {
				HashMap<String, Object> obj = new HashMap<String, Object>();
				obj.put("projNum", CommonUtils.objectToString(x.get("projNum")));
				obj.put("projName", CommonUtils.objectToString(x.get("projName")));
				obj.put("projStatus", CommonUtils.objectToString(x.get("projStatus")));
				obj.put("projId", CommonUtils.objectToLong(x.get("projId")));
				list.add(obj);
			}
		} else {
			LOGGER.info("# Kuber list is empty - size --> " + tupleList.size());
		}

		return list;

		/*
		 * return ((Number) session.createNativeQuery(
		 * "select coalesce(count(id),0) as projCount from kuber_status_audit where emp_id=:empId and action_status='PENDING'"
		 * ) .setParameter("empId", empId).getSingleResult()).intValue();
		 */
	}

	/**
	 * @author Vinay B. Dec 27, 2021 2:01:28 PM
	 */
	private List<HashMap<String, Object>> getPendingNearmissList(int empId, Session session) {
		LOGGER.info("# Inside getPendingNearmissList Dao - Emp ID -> " + empId);
		List<HashMap<String, Object>> list = new ArrayList<>();
		List<Tuple> tupleList = (List<Tuple>) session
				.createNativeQuery("select id.ince_id as inceId,md.dept_name as deptName,\r\n"
						+ " ms.label as inceStatus,mis.severity_type as inceSeverity\r\n"
						+ " from incident_audit_status ias\r\n"
						+ " INNER JOIN incident_details id on id.ince_id = ias.ince_id\r\n"
						+ " LEFT JOIN master_department md on md.dept_id = id.dept_id\r\n"
						+ " LEFT JOIN master_status ms on ms.status_id = ias.sts_id\r\n"
						+ " LEFT JOIN master_incident_severity mis on mis.id = id.severity_id\r\n"
						+ " where ias.action_status='PENDING' and ias.emp_id=:empId", Tuple.class)
				.setParameter("empId", empId).getResultList();
		if (CollectionUtils.isNotEmpty(tupleList)) {
			LOGGER.info("# Nearmiss list is not empty - size --> " + tupleList.size());
			for (Tuple x : tupleList) {
				HashMap<String, Object> obj = new HashMap<String, Object>();
				obj.put("inceId", CommonUtils.objectToInt(x.get("inceId")));
				obj.put("deptName", CommonUtils.objectToString(x.get("deptName")));
				obj.put("inceStatus", CommonUtils.objectToString(x.get("inceStatus")));
				obj.put("inceSeverity", CommonUtils.objectToString(x.get("inceSeverity")));
				list.add(obj);
			}
		} else {
			LOGGER.info("# Nearmiss list is empty - size --> " + tupleList.size());
		}

		return list;

		/*
		 * return ((Number) session
		 * .createNativeQuery("select coalesce(count(ince_id),0) as inc_count \r\n" +
		 * "from incident_audit_status where action_status='PENDING' and emp_id=:empId")
		 * .setParameter("empId", empId).getSingleResult()).intValue();
		 */
	}

	/**
	 * @author Vinay B. Dec 27, 2021 1:58:04 PM
	 */
	private List<HashMap<String, Object>> getPendingPMSList(int empId, Session session) {
		LOGGER.info("# Inside getPendingPMSList Dao - Emp ID -> " + empId);
		List<HashMap<String, Object>> list = new ArrayList<>();
		List<Tuple> tupleList = (List<Tuple>) session.createNativeQuery(
				"select ps.id as appraisalId,ed.cmpy_emp_id as empNum,concat(ifnull(ed.first_name,''),' ',ifnull(ed.last_name,'')) as empName,\r\n"
						+ " left(monthname(date_format(concat('2020','-',ps.month_value,'-','01'),'%Y-%m-%d')),3) as monthName,\r\n"
						+ " ps.year_value as yearValue,\r\n" + "ms.label as appraisalStatus\r\n"
						+ " from pms_yearly_appraisal_form_sts_audit psa\r\n"
						+ " INNER JOIN pms_yearly_appraisal_form_det ps on ps.id = psa.appraisal_form_id\r\n"
						+ " LEFT JOIN master_status ms on ms.status_id = psa.sts_id\r\n"
						+ " LEFT JOIN tbl_employee_details ed on ed.emp_id = ps.emp_id\r\n"
						+ " where psa.action_status='PENDING' and psa.emp_id=:empId",
				Tuple.class).setParameter("empId", empId).getResultList();

		if (CollectionUtils.isNotEmpty(tupleList)) {
			LOGGER.info("# PMS list is not empty - size --> " + tupleList.size());
			for (Tuple x : tupleList) {
				HashMap<String, Object> obj = new HashMap<String, Object>();
				obj.put("appraisalId", CommonUtils.objectToLong(x.get("appraisalId")));
				obj.put("empNum", CommonUtils.objectToString(x.get("empNum")));
				obj.put("empName", CommonUtils.objectToString(x.get("empName")));
				obj.put("monthName", CommonUtils.objectToString(x.get("monthName")));
				obj.put("yearValue", CommonUtils.objectToInt(x.get("yearValue")));
				obj.put("appraisalStatus", CommonUtils.objectToString(x.get("appraisalStatus")));
				list.add(obj);
			}
		} else {
			LOGGER.info("# PMS list is empty - size --> " + tupleList.size());
		}
		return list;

		/*
		 * return ((Number) session
		 * .createNativeQuery("select coalesce(count(appraisal_form_id),0) as apprail_pending_count \r\n"
		 * +
		 * "from pms_yearly_appraisal_form_sts_audit where action_status='PENDING' and emp_id=:empId"
		 * ) .setParameter("empId", empId).getSingleResult()).intValue();
		 */
	}

	/**
	 * @author Vinay B. Dec 27, 2021 1:56:15 PM
	 */
	private List<HashMap<String, Object>> getPendingSkillBookList(int empId, Session session) {
		LOGGER.info("# Inside getPendingSkillBookList Dao - Emp ID -> " + empId);
		List<HashMap<String, Object>> list = new ArrayList<>();
		List<Tuple> tupleList = (List<Tuple>) session.createNativeQuery(
				"select ps.id as skillBookId,ed.cmpy_emp_id as empNum,concat(ifnull(ed.first_name,''),' ',ifnull(ed.last_name,'')) as empName,\r\n"
						+ " left(monthname(date_format(concat('2020','-',ps.month_value,'-','01'),'%Y-%m-%d')),3) as monthName,\r\n"
						+ " ps.year_value as yearValue,\r\n" + "ms.label as skillBookStatus\r\n"
						+ " from pms_monthly_perf_sts_audit psa\r\n"
						+ " INNER JOIN pms_monthly_performance_sheet ps on ps.id = psa.perf_sheet_id\r\n"
						+ " LEFT JOIN master_status ms on ms.status_id = psa.sts_id\r\n"
						+ " LEFT JOIN tbl_employee_details ed on ed.emp_id = ps.emp_id\r\n"
						+ " where psa.action_status='PENDING' and psa.emp_id=:empId",
				Tuple.class).setParameter("empId", empId).getResultList();

		if (CollectionUtils.isNotEmpty(tupleList)) {
			LOGGER.info("# SkillBook list is not empty - size --> " + tupleList.size());
			for (Tuple x : tupleList) {
				HashMap<String, Object> obj = new HashMap<String, Object>();
				obj.put("skillBookId", CommonUtils.objectToInt(x.get("skillBookId")));
				obj.put("empNum", CommonUtils.objectToString(x.get("empNum")));
				obj.put("empName", CommonUtils.objectToString(x.get("empName")));
				obj.put("monthName", CommonUtils.objectToString(x.get("monthName")));
				obj.put("yearValue", CommonUtils.objectToInt(x.get("yearValue")));
				obj.put("skillBookStatus", CommonUtils.objectToString(x.get("skillBookStatus")));
				list.add(obj);
			}
		} else {
			LOGGER.info("# SkillBook list is empty - size --> " + tupleList.size());
		}
		return list;

		/*
		 * return ((Number) session
		 * .createNativeQuery("select coalesce(count(perf_sheet_id),0) as skill_book_count \r\n"
		 * +
		 * "from pms_monthly_perf_sts_audit where action_status='PENDING' and emp_id=:empId"
		 * ) .setParameter("empId", empId).getSingleResult()).intValue();
		 */
	}

	/**
	 * @author Vinay B. Dec 27, 2021 1:52:34 PM
	 * @param session
	 */
	private List<HashMap<String, Object>> getPendingSuggestionList(int empId, Session session) {
		LOGGER.info("# Inside getPendingSuggestionList Dao - Emp ID -> " + empId);
		List<HashMap<String, Object>> list = new ArrayList<>();
		List<Tuple> tupleList = (List<Tuple>) session
				.createNativeQuery(
						"select sd.sug_id as sugId,sd.suggestion_number as sugNumber,ms.label as sugStatus,\r\n"
								+ " mc.category_name as sugCategory,md.dept_name as deptName,\r\n"
								+ " dl.name as lineName \r\n" + " from sugesstion_status_audit ssa\r\n"
								+ " INNER JOIN tbl_sugession_details sd on sd.sug_id = ssa.sug_id\r\n"
								+ " LEFT JOIN master_status ms on ms.status_id = ssa.sts_id\r\n"
								+ " LEFT JOIN master_category mc on mc.cat_id = sd.cat_id\r\n"
								+ " LEFT JOIN master_department md on md.dept_id = sd.dept_id\r\n"
								+ " LEFT JOIN dwm_line dl on dl.id = sd.line_id\r\n"
								+ " where ssa.action_status='PENDING' and ssa.emp_id=:empId",
						Tuple.class)
				.setParameter("empId", empId).getResultList();

		if (CollectionUtils.isNotEmpty(tupleList)) {
			LOGGER.info("# Suggestion list is not empty - size --> " + tupleList.size());
			for (Tuple x : tupleList) {
				HashMap<String, Object> obj = new HashMap<String, Object>();
				obj.put("sugId", CommonUtils.objectToInt(x.get("sugId")));
				obj.put("sugNumber", CommonUtils.objectToString(x.get("sugNumber")));
				obj.put("sugStatus", CommonUtils.objectToString(x.get("sugNumber")));
				obj.put("sugCategory", CommonUtils.objectToString(x.get("sugCategory")));
				obj.put("deptName", CommonUtils.objectToString(x.get("deptName")));
				obj.put("lineName", CommonUtils.objectToString(x.get("lineName")));
				obj.put("sugStatus", CommonUtils.objectToString(x.get("sugStatus")));
				list.add(obj);
			}
		} else {
			LOGGER.info("# Suggestion list is empty - size --> " + tupleList.size());
		}

		return list;

		/*
		 * return ((Number) session
		 * .createNativeQuery("select coalesce(count(sug_id),0) as count from sugesstion_status_audit \r\n"
		 * + "where action_status='PENDING' and emp_id=:empId") .setParameter("empId",
		 * empId).getSingleResult()).intValue();
		 */
	}

	/**
	 * @author Vinay B. Dec 27, 2021 3:06:51 PM
	 */
	@Override
	public boolean transferModuleWisePendingActions(EnovationDTO request) {
		LOGGER.info("# Inside transferModuleWisePendingActions Dao ");
		boolean flag = false;
		Session session = getCurrentSession();
		LOGGER.info("# Transfer module wise action size --> " + request.getData().size());
		for (HashMap<String, List<EnovationDTO>> d : request.getData()) {
			for (Entry<String, List<EnovationDTO>> e : d.entrySet()) {
				LOGGER.info("# ::: Extracting map entries :::::");
				LOGGER.info("# Module Name -- > " + e.getKey());
				if (e.getKey() != null) {
					if (e.getKey().equalsIgnoreCase(EnovationConstants.MODULE_SUGGESTION)) {
						LOGGER.info("# Transfer Action List Size --> " + e.getValue().size());
						if (CollectionUtils.isNotEmpty(e.getValue())) {
							e.getValue().stream().forEach(x -> {
								transferSuggestionPendingAction(session, x.getEmpId(), x.getTransferTo(), x.getId());
							});
							flag = true;
						}
					} else if (e.getKey().equalsIgnoreCase(EnovationConstants.MODULE_NEARMISS)) {
						LOGGER.info("# Transfer Action List Size --> " + e.getValue().size());
						if (CollectionUtils.isNotEmpty(e.getValue())) {
							e.getValue().stream().forEach(x -> {
								transferNearmissPendingAction(session, x.getEmpId(), x.getTransferTo(), x.getId());
							});
							flag = true;
						}
					} else if (e.getKey().equalsIgnoreCase(EnovationConstants.MODULE_SKILLBOOK)) {
						LOGGER.info("# Transfer Action List Size --> " + e.getValue().size());
						if (CollectionUtils.isNotEmpty(e.getValue())) {
							e.getValue().stream().forEach(x -> {
								transferSkillBookPendingAction(session, x.getEmpId(), x.getTransferTo(), x.getId());
							});
							flag = true;
						}
					} else if (e.getKey().equalsIgnoreCase(EnovationConstants.MODULE_PMS)) {
						LOGGER.info("# Transfer Action List Size --> " + e.getValue().size());
						if (CollectionUtils.isNotEmpty(e.getValue())) {
							e.getValue().stream().forEach(x -> {
								transferPmsPendingAction(session, x.getEmpId(), x.getTransferTo(), x.getId());
							});
							flag = true;
						}
					} else if (e.getKey().equalsIgnoreCase(EnovationConstants.MODULE_AUDIT)) {
						LOGGER.info("# Transfer Action List Size --> " + e.getValue().size());
						if (CollectionUtils.isNotEmpty(e.getValue())) {
							e.getValue().stream().forEach(x -> {
								transferAuditPendingAction(session, x.getEmpId(), x.getTransferTo(), x.getId());
							});
							flag = true;
						}
					} else if (e.getKey().equalsIgnoreCase(EnovationConstants.MODULE_KUBER)) {
						LOGGER.info("# Transfer Action List Size --> " + e.getValue().size());
						if (CollectionUtils.isNotEmpty(e.getValue())) {
							e.getValue().stream().forEach(x -> {
								transferKuberPendingAction(session, x.getEmpId(), x.getTransferTo(), x.getId());
							});
							flag = true;
						}
					} else if (e.getKey().equalsIgnoreCase(EnovationConstants.MODULE_TPM)) {
						LOGGER.info("# Transfer Action List Size --> " + e.getValue().size());
						if (CollectionUtils.isNotEmpty(e.getValue())) {
							e.getValue().stream().forEach(x -> {
								transferTPMPendingAction(session, x.getEmpId(), x.getTransferTo(), x.getId());
							});
							flag = true;
						}
					} else if (e.getKey().equalsIgnoreCase(EnovationConstants.MODULE_DWM)) {
						LOGGER.info("# Transfer Action List Size --> " + e.getValue().size());
						if (CollectionUtils.isNotEmpty(e.getValue())) {
							e.getValue().stream().forEach(x -> {
								transferDWMPendingAction(session, x.getEmpId(), x.getTransferTo(), x.getId());
							});
							flag = true;
						}
					} else if (e.getKey().equalsIgnoreCase(EnovationConstants.MODULE_CONCERN)) {
						LOGGER.info("# Transfer Action List Size --> " + e.getValue().size());
						if (CollectionUtils.isNotEmpty(e.getValue())) {
							e.getValue().stream().forEach(x -> {
								transferConcernPendingAction(session, x.getEmpId(), x.getTransferTo(), x.getId());
							});
							flag = true;
						}
					}else if (e.getKey().equalsIgnoreCase(EnovationConstants.SKILLMATRIX)) {
						LOGGER.info("# Transfer Action List Size --> " + e.getValue().size());
						if (CollectionUtils.isNotEmpty(e.getValue())) {
							e.getValue().stream().forEach(x -> {
								transferSkillMatrixPendingAction(session, x.getEmpId(), x.getTransferTo(), x.getSkillingId(),x.getStageId(),x.getOjtRegistrationId());
							});
							flag = true;
						}
					}
				}

			}
		}

		/*
		 * if (CollectionUtils.isNotEmpty(req.getDtoList())) { for (EmployeeDTO x :
		 * req.getDtoList()) { if (x.getModuleName() != null) { if
		 * (x.getModuleName().equalsIgnoreCase(EnovationConstants.MODULE_SUGGESTION)) {
		 * transferSuggestionPendingAction(session, x); flag = true; } else if
		 * (x.getModuleName().equalsIgnoreCase(EnovationConstants.MODULE_NEARMISS)) {
		 * transferNearmissPendingAction(session, x); flag = true; } else if
		 * (x.getModuleName().equalsIgnoreCase(EnovationConstants.MODULE_SKILLBOOK)) {
		 * transferSkillBookPendingAction(session, x); flag = true; } else if
		 * (x.getModuleName().equalsIgnoreCase(EnovationConstants.MODULE_PMS)) {
		 * transferPmsPendingAction(session, x); flag = true; } else if
		 * (x.getModuleName().equalsIgnoreCase(EnovationConstants.MODULE_AUDIT)) {
		 * transferAuditPendingAction(session, x); flag = true; } } } }
		 */

		return flag;
	}

	/**
	 * @author Vinay B. May 24, 2022 2:22:43 PM
	 */
	private void transferConcernPendingAction(Session session, int empId, int transferTo, int concernId) {
		LOGGER.info("# Inside transferConcernPendingAction Dao ");
		int concernRowAffected = session.createNativeQuery(
						"update concern_details set under_action=:transferTo where under_action=:empId and concern_id=:concernId")
				.setParameter("concernId", concernId).setParameter("empId", empId)
				.setParameter("transferTo", transferTo).executeUpdate();
		LOGGER.info("# concern_details | Row affected - " + concernRowAffected);

	}

	private void transferSkillMatrixPendingAction(Session session, int empId, int transferTo, int skillingId, int stageId, int ojtRegistrationId) {
		LOGGER.info("# Inside transferSkillMatrixPendingAction Dao");

		// Step 1: Update emp_id in sm_ojt_skilling_audit for PENDING entries
		int skillingAuditRowAffected = session.createNativeQuery(
						"UPDATE sm_ojt_skilling_audit " +
								"SET emp_id = :transferTo " +
								"WHERE emp_id = :empId " +
								"AND skilling_id = :skillingId " +
								"AND status = :status " +
								"AND stage_id = :stageId " +
								"AND ojt_regis_id = :ojtRegistrationId")
				.setParameter("empId", empId)
				.setParameter("transferTo", transferTo)
				.setParameter("skillingId", skillingId)
				.setParameter("status", "PENDING")
				.setParameter("stageId", stageId)
				.setParameter("ojtRegistrationId", ojtRegistrationId)
				.executeUpdate();

		LOGGER.info("# Skill matrix audit rows affected -> " + skillingAuditRowAffected);

		// Step 2: If stage is 2 or 3, also update trainer_emp_id in sm_ojt_regis
		if (stageId == 2 || stageId == 3) {
			int regisUpdateCount = session.createNativeQuery(
							"UPDATE sm_ojt_regis " +
									"SET trainer_emp_id = :empId " +
									"WHERE id = :ojtRegistrationId")
					.setParameter("empId", empId)
					.setParameter("ojtRegistrationId", ojtRegistrationId)
					.executeUpdate();

			LOGGER.info("# OJT registration trainer updated rows -> " + regisUpdateCount);
		}
	}

	/**
	 * @author Vinay B. May 18, 2022 10:08:46 PM
	 */
	private void transferDWMPendingAction(Session session, int empId, int transferTo, int dwmId) {
		LOGGER.info("# Inside transferDWMPendingAction Dao ");
		int dwmActStsAdtRowAffected = session.createNativeQuery(
						"update dwm_status_audit set emp_id=:transferTo where action_status='PENDING' AND emp_id=:empId and dwm_id=:dwmId")
				.setParameter("dwmId", dwmId).setParameter("empId", empId).setParameter("transferTo", transferTo)
				.executeUpdate();
		LOGGER.info("# dwm_status_audit | Row affected - " + dwmActStsAdtRowAffected);

	}

	/**
	 * @author Vinay B. May 18, 2022 10:06:08 PM
	 */
	private void transferTPMPendingAction(Session session, int empId, int transferTo, int tpmId) {
		LOGGER.info("# Inside transferTPMPendingAction Dao ");
		int tpmActStsAdtRowAffected = session.createNativeQuery(
						"update tag_status_audit set emp_id=:transferTo where action_status='PENDING' AND emp_id=:empId and tag_id=:tpmId")
				.setParameter("tpmId", tpmId).setParameter("empId", empId).setParameter("transferTo", transferTo)
				.executeUpdate();
		LOGGER.info("# tag_status_audit | Row affected - " + tpmActStsAdtRowAffected);

	}

	/**
	 * @author Vinay B. May 18, 2022 10:01:20 PM
	 */
	private void transferKuberPendingAction(Session session, int empId, int transferTo, int projectId) {
		LOGGER.info("# Inside transferKuberPendingAction Dao ");
		int kuberActStsAdtRowAffected = session.createNativeQuery(
						"update kuber_status_audit set emp_id=:transferTo where action_status='PENDING' AND emp_id=:empId and project_id=:projectId")
				.setParameter("projectId", projectId).setParameter("empId", empId)
				.setParameter("transferTo", transferTo).executeUpdate();
		LOGGER.info("# kuber_status_audit | Row affected - " + kuberActStsAdtRowAffected);
		/*
		 * int insDetRowAffected = session
		 * .createNativeQuery("update audit_inspection_details ai\r\n" +
		 * "INNER JOIN audit_action_status ats ON ats.inspection_id = ai.id\r\n" +
		 * "SET  ai.auditee_emp_id=:transferTo\r\n" +
		 * "where ats.action_status='PENDING' AND  ai.auditee_emp_id=:empId and ai.id=:auditId"
		 * ) .setParameter("auditId", auditId).setParameter("empId",
		 * empId).setParameter("transferTo", transferTo) .executeUpdate();
		 * LOGGER.info("# audit_inspection_details | Row affected - " +
		 * insDetRowAffected);
		 */

	}

	/**
	 * @author Vinay B. Dec 30, 2021 10:58:05 AM
	 */
	private void transferAuditPendingAction(Session session, int empId, int transferTo, int auditId) {
		LOGGER.info("# Inside transferAuditPendingAction Dao ");
		int auditActStsAdtRowAffected = session.createNativeQuery(
						"update audit_action_status set emp_id=:transferTo where action_status = 'PENDING' AND emp_id=:empId and inspection_id=:auditId")
				.setParameter("auditId", auditId).setParameter("empId", empId).setParameter("transferTo", transferTo)
				.executeUpdate();
		LOGGER.info("# audit_action_status | Row affected - " + auditActStsAdtRowAffected);
		int insDetRowAffected = session
				.createNativeQuery("update audit_inspection_details ai\r\n"
						+ "INNER JOIN audit_action_status ats ON ats.inspection_id = ai.id\r\n"
						+ "SET  ai.auditee_emp_id=:transferTo\r\n"
						+ "where ats.action_status='PENDING' AND  ai.auditee_emp_id=:empId and ai.id=:auditId")
				.setParameter("auditId", auditId).setParameter("empId", empId).setParameter("transferTo", transferTo)
				.executeUpdate();
		LOGGER.info("# audit_inspection_details | Row affected - " + insDetRowAffected);

	}

	/**
	 * @author Vinay B. Dec 27, 2021 3:58:36 PM
	 */
	private void transferPmsPendingAction(Session session, int empId, int transferTo, int pmsId) {
		LOGGER.info("# Inside transferPmsPendingAction Dao ");
		int pmsStsAdtRowAffected = session.createNativeQuery(
						"UPDATE pms_yearly_appraisal_form_sts_audit SET emp_id=:transferTo WHERE emp_id=:empId and action_status = 'PENDING' and appraisal_form_id=:pmsId")
				.setParameter("pmsId", pmsId).setParameter("empId", empId).setParameter("transferTo", transferTo)
				.executeUpdate();
		LOGGER.info("# pms_yearly_appraisal_form_sts_audit | Row affected - " + pmsStsAdtRowAffected);
		int pmsRowAffected = session.createNativeQuery("UPDATE pms_yearly_appraisal_form_det ps \r\n"
						+ "inner join pms_yearly_appraisal_form_sts_audit psa on ps.id = psa.appraisal_form_id\r\n"
						+ "set ps.assinged_to_id=:transferTo WHERE ps.assinged_to_id=:empId and psa.action_status = 'PENDING' and ps.id=:pmsId")
				.setParameter("pmsId", pmsId).setParameter("empId", empId).setParameter("transferTo", transferTo)
				.executeUpdate();
		LOGGER.info("# pms_yearly_appraisal_form_det | Row affected - " + pmsRowAffected);
	}

	/**
	 * @author Vinay B. Dec 27, 2021 3:58:33 PM
	 */
	private void transferSkillBookPendingAction(Session session, int empId, int transferTo, int perfSheetId) {
		LOGGER.info("# Inside transferSkillBookPendingAction Dao ");
		int psheetStsAdtRowAffected = session.createNativeQuery(
						"UPDATE pms_monthly_perf_sts_audit SET emp_id=:transferTo WHERE emp_id=:empId and action_status='PENDING' and perf_sheet_id=:perfSheetId")
				.setParameter("perfSheetId", perfSheetId).setParameter("empId", empId)
				.setParameter("transferTo", transferTo).executeUpdate();
		LOGGER.info("# pms_monthly_perf_sts_audit | Row affected - " + psheetStsAdtRowAffected);
		int psRowAffected = session.createNativeQuery("UPDATE pms_monthly_performance_sheet ps \r\n"
						+ "inner join pms_monthly_perf_sts_audit psa on ps.id = psa.perf_sheet_id\r\n"
						+ "set ps.assinged_to_id=:transferTo WHERE ps.assinged_to_id=:empId and psa.action_status = 'PENDING' and ps.id=:perfSheetId")
				.setParameter("perfSheetId", perfSheetId).setParameter("empId", empId)
				.setParameter("transferTo", transferTo).executeUpdate();
		LOGGER.info("# pms_monthly_performance_sheet | Row affected - " + psRowAffected);
	}

	/**
	 * @author Vinay B. Dec 27, 2021 3:58:29 PM
	 */
	private void transferNearmissPendingAction(Session session, int empId, int transferTo, int incidentId) {
		LOGGER.info("# Inside transferNearmissPendingAction Dao ");
		int incStsAdtRowAffected = session
				.createNativeQuery("UPDATE incident_audit_status SET emp_id=:transferTo \r\n"
						+ " WHERE emp_id=:empId and action_status = 'PENDING' and ince_id=:incidentId ")
				.setParameter("incidentId", incidentId).setParameter("empId", empId)
				.setParameter("transferTo", transferTo).executeUpdate();
		LOGGER.info("# incident_audit_status | Row affected - " + incStsAdtRowAffected);
		int incRowAffected = session.createNativeQuery("UPDATE incident_details id \r\n"
						+ "inner join incident_audit_status ia on ia.ince_id = id.ince_id\r\n"
						+ "set id.assigned_to=:transferTo WHERE id.assigned_to=:empId and ia.action_status = 'PENDING' and  id.ince_id=:incidentId")
				.setParameter("incidentId", incidentId).setParameter("empId", empId)
				.setParameter("transferTo", transferTo).executeUpdate();
		LOGGER.info("# incident_details | Row affected - " + incRowAffected);

	}

	/**
	 * @author Vinay B. Dec 27, 2021 3:56:11 PM
	 * @param 
	 */
	private void transferSuggestionPendingAction(Session session, int empId, int transferTo, int sugId) {
		LOGGER.info("# Inside transferSuggestionPendingAction Dao ");

		// Update sugg tracker table - if sugg is pending with role Evaluator
		int trackerRowAffectedEval = session.createNativeQuery("UPDATE tbl_sugession_tracker_details track\r\n"
						+ " inner join sugesstion_status_audit ssa on (track.sug_id=ssa.sug_id and  track.team_type_id=1)\r\n"
						+ " inner join master_status msts  on msts.status_id=ssa.sts_id\r\n"
						+ " SET track.emp_id=:transferTo \r\n"
						+ " where track.sug_id=:sugId and ssa.emp_id=:empId  and ssa.action_status='PENDING'\r\n"
						+ " and msts.role_id in (8)").setParameter("empId", empId).setParameter("sugId", sugId)
				.setParameter("transferTo", transferTo).executeUpdate();

		LOGGER.info("# tbl_sugession_tracker_details | Row affected (Evaluator) - " + trackerRowAffectedEval);

		// Update sugg tracker table - if sugg is pending with role Implementer
		int trackerRowAffectedImpl = session.createNativeQuery(" UPDATE tbl_sugession_tracker_details track\r\n"
						+ " inner join sugesstion_status_audit ssa on (track.sug_id=ssa.sug_id and  track.team_type_id=2)\r\n"
						+ " inner join master_status msts  on msts.status_id=ssa.sts_id\r\n"
						+ " SET track.emp_id=:transferTo \r\n"
						+ " where track.sug_id=:sugId and ssa.emp_id=:empId  and ssa.action_status='PENDING'\r\n"
						+ " and msts.role_id in (9)").setParameter("empId", empId).setParameter("transferTo", transferTo)
				.setParameter("sugId", sugId).executeUpdate();
		LOGGER.info("# tbl_sugession_tracker_details | Row affected (Implementer) - " + trackerRowAffectedImpl);

		// Update sugg sts audit tble
		int stsAdtRowAffected = session
				.createNativeQuery("UPDATE sugesstion_status_audit  SET emp_id=:transferTo \r\n"
						+ "where action_status='PENDING' and sug_id=:sugId and emp_id=:empId")
				.setParameter("empId", empId).setParameter("sugId", sugId).setParameter("transferTo", transferTo)
				.executeUpdate();
		LOGGER.info("# sugesstion_status_audit | Row affected - " + stsAdtRowAffected);

		/*
		 * int sugTrackerRowAffected = session
		 * .createNativeQuery("UPDATE sugesstion_status_audit  SET emp_id=:transferTo \r\n"
		 * + "where action_status='PENDING' and emp_id=:empId") .setParameter("empId",
		 * x.getEmpId()).setParameter("transferTo", x.getActionTransferTo())
		 * .executeUpdate();
		 */

		/*
		 * select count(st.sug_id) as sugCount,group_concat(st.sug_id),
		 * group_concat(suggtrack_id) as trackerId from tbl_sugession_tracker_details st
		 * INNER JOIN ( select sug_id as sugId,sts_id as stsId from
		 * sugesstion_status_audit where action_status = 'PENDING' and emp_id = 5 ) as x
		 * on x.sugId = st.sug_id where st.emp_id = 5 GROUP BY st.sug_id
		 */
		// LOGGER.info("# sugesstion_status_audit | Row affected - " +
		// sugTrackerRowAffected);
	}

	/**
	 * @author Vinay B. May 19, 2022 11:07:49 PM
	 */
	@Override
	public boolean employeeTransfer(EmployeeTransferDTO request) {
		LOGGER.info("# Inside employeeTransfer Dao ");
		boolean flag = false;
		if (request.getEmpId() > 0) {
			LOGGER.info("# employeeTransfer | Transfer Emp ID -> " + request.getEmpId());
			LOGGER.info("# To Branch ID --> " + request.getToBranch());
			LOGGER.info("# To Dept Id --> " + request.getToDept());
			LOGGER.info("# To Line Id --> " + request.getToLine());
			Session session = getCurrentSession();
			EmployeeTransfer et = new EmployeeTransfer();
			EmployeeDetails ed = session.load(EmployeeDetails.class, request.getEmpId());
			if (ed != null) {
				if (ed.getBranch() != null) {
					et.setFromBranch(ed.getBranch().getBranchId());
				}
				if (ed.getDept() != null) {
					et.setFromDept(ed.getDept().getDeptId());
				}
				if (ed.getLine() != null) {
					et.setFromLine((int) ed.getLine().getId());
				}
				if (ed.getEmpLevelDetails() != null) {
					et.setFromEmpLevel(ed.getEmpLevelDetails().getEmpLvlId());
				}
				if (ed.getCmpyEmpId() != null) {
					et.setCompanyEmpId(ed.getCmpyEmpId());
				}
			}
			et.setEmpId(request.getEmpId());
			if (request.getToBranch() > 0) {
				et.setToBranch(request.getToBranch());
				ed.setBranch(new BranchMaster(request.getToBranch()));
			}
			if (request.getToDept() > 0) {
				et.setToDept(request.getToDept());
				ed.setDept(new DepartmentMaster(request.getToDept()));
			}
			if (request.getToLine() > 0) {
				et.setToLine(request.getToLine());
				ed.setLine(new Line(request.getToLine()));
			}
			if (request.getToEmpLevel() > 0) {
				et.setToEmpLevel(request.getToEmpLevel());
				ed.setEmpLevelDetails(new EmployeeHierarchy(request.getToEmpLevel()));
			}
			et.setTransferredBy(request.getTransferredBy());
			et.setCreatedBy(request.getCreatedBy());
			et.setUpdatedBy(request.getUpdatedBy());
			et.setTransferredOn(request.getTransferredOn());
			if (request.getTransferReason() != null) {
				et.setTransferReason(request.getTransferReason());
			}
			// save employee transfer entry
			session.save(et);

			// update transfer details into employee table
			ed.setUpdatedBy(request.getUpdatedBy());
			session.update(ed);

			flag = true;

		}
		return flag;
	}

	@Override
	@Transactional
	public boolean addUpdateEmployeeLevel(EmployeeHierarchy request) {
		LOGGER.info("# ADD UPDATE EMPLOYEE LEVEL IMPL ");
		boolean flag = false;
		try {
			getCurrentSession().saveOrUpdate(request);
			flag = true;
		} catch (Exception e) {
			LOGGER.error("#INSIDE EXCEPTION OCCURED IN ADD UPDATE EMPLOYEE LEVEL" + ExceptionUtils.getStackTrace(e));
		}
		return flag;
	}

	@Override
	public boolean sendMailToSuperAdminForPasswordReset(EmployeeDTO dto) {
		LOGGER.info(
				"Inside EmployeeDaoImpl | sendMailToSuperAdminForPasswordReset Method in isemailverify/{email} API For Reset Password");
		Session session = getCurrentSession();
		List<MailDTO> mailList = new ArrayList<>();
		String mailContent = null;
		String mailContentToSend = null;
		boolean flag = false;
		int branchId = 0;
		try {
			EmailTemplateMaster messageContent = enoConfig.getMessageContent(EnovationConstants.MASTER_EMAIL_TEMPLATE);
			List<Object[]> empDetails = session.createNativeQuery(
							"select e.emp_id,e.email_id,CONCAT(e.first_name,' ',e.last_name) as employeeName,e.branch_id,e.cmpy_emp_id,contact_no from tbl_employee_details e where e.email_id=:userCred or e.cmpy_emp_id=:userCred or e.contact_no=:userCred")
					.setParameter("userCred", dto.getUserCred()).getResultList();

			for (Object obj[] : empDetails) {
				branchId = (obj[3] != null) ? Integer.valueOf(String.valueOf(obj[3])) : 0;
				System.out.println("Branch Id------->" + branchId);
				if (messageContent.getBody() != null) {
					mailContent = String.valueOf(env.getProperty("RESET_PASWORD_TO_SUPERADMIN.body"))
							.replaceAll(Pattern.quote("{Username}"), String.valueOf(obj[2]))
							.replaceAll(Pattern.quote("{UserEmailId}"), String.valueOf(obj[1]))
							.replaceAll(Pattern.quote("{empId}"), String.valueOf(obj[4]))
							.replaceAll(Pattern.quote("{mobile_no}"), String.valueOf(obj[5]));
				}
				List<Object[]> superAdmin = session.createNativeQuery(
								"select CONCAT(e.first_name,' ',e.last_name) as superAdminName,e.email_id,o.portal_link from tbl_employee_details e\r\n"
										+ "inner join emp_roles er on er.emp_id=e.emp_id\r\n"
										+ "inner join roles r on (r.id=er.role_id and er.role_id=:superAdminRoleId)\r\n"
										+ "inner join master_branch b on b.branch_id=e.branch_id\r\n"
										+ "inner join master_organization o on o.org_id=b.org_id\r\n"
										+ "where b.branch_id=:branchId")
						.setParameter("branchId", branchId)
						.setParameter("superAdminRoleId", EnovationConstants.SUPER_ADMIN).getResultList();

				if (superAdmin != null && !superAdmin.isEmpty()) {
					for (Object row[] : superAdmin) {
						mailContentToSend = messageContent.getBody()
								.replaceAll(Pattern.quote("{name}"), (row[0] != null) ? String.valueOf(row[0]) : " ")
								.replaceAll(Pattern.quote("{body}"), mailContent).replaceAll(
										Pattern.quote("{portalLink}"), (row[2] != null) ? String.valueOf(row[2]) : "");

						MailDTO maildData = new MailDTO(
								String.valueOf(String.valueOf(row[1])) /* "sonali.b@greentinsolutions.com" */,
								(env.getProperty("RESET_PASWORD_TO_SUPERADMIN.subject")
										.replaceAll(Pattern.quote("{empName}"), String.valueOf(obj[2]))),
								mailContentToSend);

						System.out.println("To Mail-------> " + maildData.getToMail());

						mailList.add(maildData);
					}

					/*
					 * NotificationProcess ntp = new NotificationProcess(
					 * String.valueOf(String.valueOf(row[1]))
					 * "sonali.b@greentinsolutions.com","EM",EnovationConstants.
					 * PROCESS_TYPE_TRANSACTIONAL); emService.initMail(ntp);
					 */

				}
			}
			flag = true;
		} catch (Exception e) {
			e.printStackTrace();
		} /*
		 * finally { taskExecutor.execute(new MailUtil(mailList, communication)); }
		 */

		return flag;

	}

	/**
	 * @author Rakesh 07 sept 2020
	 * @param request
	 * @return
	 * @throws IOException
	 * @throws InvalidFormatException
	 */
	@Override
	public String bulkEmployeeUpload(EmployeeDTO request, MasterResponse response)
			throws InvalidFormatException, IOException {
		LOGGER.info("IN Dao | bulkEmployeeUpload ");
		Session session = getCurrentSession();

		String flag = "";
		List<EmployeeDetails> empList = new ArrayList<>();
		List<EmployeeHierarchy> empLevel = new ArrayList<>();
		List<DepartmentMaster> deptMasterList = new ArrayList<>();
		List<Line> lineMasterList = new ArrayList<>();
		deptMasterList = getDeptMasterDetails(session, request.getOrgId(), request.getBranchId());
		lineMasterList = getLineMasterDetails(session, request.getOrgId(), request.getBranchId());
		empLevel = getEmpLevelMaster(session, request.getOrgId(), request.getBranchId());

		String SAMPLE_XLSX_FILE_PATH = writeFileNdGetPath(request);
		Workbook workbook = WorkbookFactory.create(new File(SAMPLE_XLSX_FILE_PATH));
		final FormulaEvaluator evaluator = workbook.getCreationHelper().createFormulaEvaluator();
		DataFormatter formatter = new DataFormatter();
		Sheet sheet = null;
		for (int i = 0; i < workbook.getNumberOfSheets(); i++) {
			System.out.println("Sheet name:" + workbook.getSheetName(i));
			if (workbook.getSheetName(i).toString() != null
					&& workbook.getSheetName(i).toString().equals("EmployeeList")) {
				sheet = workbook.getSheetAt(i);
				break;
			}
		}

		if (sheet != null) {
			if (ExcelUtil.IsExcelFormatValid(sheet, formatter)) {
				if (!ExcelUtil.IsExcelBlank(formatter, sheet)) {
					empList = readNDWriteToEmployeeList(formatter, sheet, evaluator);
					if (empList != null && !empList.isEmpty()) {
						List<EmployeeDetails> errorList = validateEmployeeDetails(session, empList, request,
								deptMasterList, lineMasterList, empLevel);
						if (errorList == null || errorList.isEmpty()) {
							saveEmployeeDetails(request, empList, session, deptMasterList);
							flag = EnovationConstants.SUCCESS;
						} else {
							flag = EnovationConstants.EXCEL_ERROR_MSG;
							response.setEmpErrorList(errorList);
						}
					} else {
						flag = EnovationConstants.FILE_IS_EMPTY;
						response.setReason(EnovationConstants.FILE_IS_EMPTY);
					}
				} else {
					flag = EnovationConstants.FILE_IS_EMPTY;
					response.setReason(EnovationConstants.FILE_IS_EMPTY);
				}
			} else {
				flag = EnovationConstants.CHECK_EXCEL_FORMAT;
				response.setReason(EnovationConstants.CHECK_EXCEL_FORMAT);
			}
		} else {
			flag = EnovationConstants.INVALID_FILE;
			response.setReason(EnovationConstants.INVALID_FILE);
		}
		return flag;
	}

	/**
	 * @author Aditi V. 17 feb 2021
	 */
	@Override
	public EmployeeDTO importBulkEmployeeData(EmployeeDTO request) {
		LOGGER.info("In Employee Dao | importBulkEmployeeData ");
		EmployeeDTO dto = new EmployeeDTO();
		Session session = getCurrentSession();

		String SAMPLE_XLSX_FILE_PATH = writeFileNdGetPath(request);

		/** Creating a Workbook from an Excel file (.xls or .xlsx) */

		Workbook workbook = null;
		try {
			workbook = WorkbookFactory.create(new File(SAMPLE_XLSX_FILE_PATH));
		} catch (InvalidFormatException | IOException e) {
			e.printStackTrace();
		}

		/** FormulaEvalutor object helps to read values computed by formula function. */

		final FormulaEvaluator evaluator = workbook.getCreationHelper().createFormulaEvaluator();

		/**
		 * Dataformat object is used to read any type(such as text,number,date,formula)
		 * of data from excel.
		 */

		DataFormatter formatter = new DataFormatter();

		for (int i = 0; i < workbook.getNumberOfSheets(); i++) {
			System.out.println(workbook.getSheetName(i));
		}

		/* Sheet sheet = workbook.getSheetAt(0); */

		Sheet sheet = null;
		boolean flag = false;
		for (int i = 0; i < workbook.getNumberOfSheets(); i++) {

			System.out.println("Sheet name: " + workbook.getSheetName(i));
			if (workbook.getSheetName(i).toString() != null
					&& workbook.getSheetName(i).toString().trim().equals(EnovationConstants.EMPLOYEE_LIST)) {
				sheet = workbook.getSheetAt(i);
				flag = processEmpData(request, sheet, formatter, evaluator, session, dto);

			}
		}
		return dto;

	}

	private List<DepartmentMaster> getDeptMasterDetails(Session session, int orgId, int branchId) {
		LOGGER.info("IN Dao | getDeptMasterDetails ");
		List<DepartmentMaster> deptList = new ArrayList<>();
		List<Object[]> objList = session
				.createNativeQuery(
						"select dept_id,dept_name from master_department where org_id=:orgId AND branch_id=:branchId")
				.setParameter("orgId", orgId).setParameter("branchId", branchId).getResultList();

		for (Object[] obj : objList) {
			DepartmentMaster dept = new DepartmentMaster();
			dept.setDeptId(CommonUtils.objectToInt(obj[0]));
			dept.setDeptName(CommonUtils.objectToString(obj[1]));
			deptList.add(dept);
		}

		return deptList;
	}

	private List<Line> getLineMasterDetails(Session session, int orgId, int branchId) {
		LOGGER.info("IN Dao | getLineMasterDetails ");
		List<Line> lineList = new ArrayList<>();

		List<Object[]> objList = session.createNativeQuery(
						" select line.id,line.name,md.dept_id from dwm_line line inner join  master_department md on md.dept_id=line.dept_id "
								+ " where md.org_id=:orgId AND md.branch_id=:branchId ")
				.setParameter("orgId", orgId).setParameter("branchId", branchId).getResultList();

		if (objList != null && !objList.isEmpty()) {
			for (Object[] obj : objList) {
				Line tmp = new Line();
				tmp.setId(CommonUtils.objectToLong(obj[0]));
				tmp.setName(CommonUtils.objectToString(obj[1]));
				tmp.setDeptId(CommonUtils.objectToInt(obj[2]));
				lineList.add(tmp);
			}
		}
		return lineList;
	}

	private List<EmployeeHierarchy> getEmpLevelMaster(Session session, int orgId, int branchId) {
		LOGGER.info("IN Dao | getEmpLevelMaster ");
		List<EmployeeHierarchy> levelList = new ArrayList<>();

		levelList = session.createQuery(" from EmployeeHierarchy where branchId=:branchId and isActive='Y'")
				.setParameter("branchId", branchId).getResultList();

		return levelList;

	}

	private String writeFileNdGetPath(EmployeeDTO request) {
		LOGGER.info("IN Dao | writeFileNdGetPath ");
		String filePathToTrim = null;
		String picpathcut = null;

		if (request != null && request.getUploadEmployeeFile() != null) {

			MultipartFile file = request.getUploadEmployeeFile();

			String docDirctory = EnovationConfig.buddyConfig.get("excelUploadPath") + "/"
					+ EnovationConstants.EMPLOYEE_UPLOAD + "/" + request.getBranchId() + "/"
					+ EnovationConstants.UPLOADED_ON + java.time.LocalDate.now();

			filePathToTrim = EnovationConstants.EMPLOYEE_UPLOAD + "/" + request.getBranchId() + "/"
					+ EnovationConstants.UPLOADED_ON + java.time.LocalDate.now();
			picpathcut = WriteFilesUtils.generateFileNameNDwriteFile(file, docDirctory, filePathToTrim);
		}
		return EnovationConfig.buddyConfig.get("excelUploadPath") + "/" + picpathcut;

	}

	private List<EmployeeDetails> readNDWriteToEmployeeList(DataFormatter formatter, Sheet sheet,
															FormulaEvaluator evaluator) {
		LOGGER.info("IN Dao | readNDWriteToEmployeeList ");
		List<EmployeeDetails> empList = new ArrayList<>();
		for (int j = 0; j < sheet.getLastRowNum() + 1; j++) {

			String cmpnyEmpId = null, firstName = null, lastName = null, email = null, contactNo = null,
					designation = null, department = null, line = null, doj = null, empLevel = null, middleName = null,
					address = null;

			Row row = sheet.getRow(j);
			Row header = sheet.getRow(0);

			if (row.getRowNum() > 0 && ExcelUtil.excelRowChecker(formatter, row)) {
				EmployeeDetails emp = new EmployeeDetails();

				for (int k = header.getFirstCellNum(); k < header.getLastCellNum() + 1; k++) {

					Cell cellHeader = header.getCell(k);
					if (!formatter.formatCellValue(cellHeader).equals("")
							&& formatter.formatCellValue(cellHeader).trim().length() != 0) {

						if (formatter.formatCellValue(cellHeader).equals(EnovationConstants.EMPLOYEE_ID)) {
							boolean flag = ExcelUtil.validateCellData(formatter, row, k);
							if (flag) {
								cmpnyEmpId = formatter.formatCellValue(row.getCell(k), evaluator);
								emp.setCmpyEmpId(cmpnyEmpId);
							}
						}
						if (formatter.formatCellValue(cellHeader).equals(EnovationConstants.FIRST_NAME)) {
							boolean flag = ExcelUtil.validateCellData(formatter, row, k);
							if (flag) {
								firstName = formatter.formatCellValue(row.getCell(k), evaluator);
								emp.setFirstName(firstName);
							}
						} else if (formatter.formatCellValue(cellHeader).equals(EnovationConstants.LAST_NAME)) {
							boolean flag = ExcelUtil.validateCellData(formatter, row, k);
							if (flag) {
								lastName = formatter.formatCellValue(row.getCell(k), evaluator);
								emp.setLastName(lastName);
							}
						} else if (formatter.formatCellValue(cellHeader).equals(EnovationConstants.EMAIL_ID)) {
							boolean flag = ExcelUtil.validateCellData(formatter, row, k);
							if (flag) {
								email = formatter.formatCellValue(row.getCell(k), evaluator);
								emp.setEmailId(email);
							}
						} else if (formatter.formatCellValue(cellHeader).equals(EnovationConstants.MOBILE_NUMBER)) {
							boolean flag = ExcelUtil.validateCellData(formatter, row, k);
							if (flag) {
								contactNo = formatter.formatCellValue(row.getCell(k), evaluator);
								emp.setContactNo(contactNo);
							}
						} else if (formatter.formatCellValue(cellHeader).equals(EnovationConstants.DEPARTMENT)) {
							boolean flag = ExcelUtil.validateCellData(formatter, row, k);
							if (flag) {
								department = formatter.formatCellValue(row.getCell(k), evaluator);
								emp.setDeptName(department);
							}
						} else if (formatter.formatCellValue(cellHeader).equals(EnovationConstants.DESIGNATION)) {
							boolean flag = ExcelUtil.validateCellData(formatter, row, k);
							if (flag) {
								designation = formatter.formatCellValue(row.getCell(k), evaluator);
								emp.setDesignation(designation);
							}
						} else if (formatter.formatCellValue(cellHeader).equals(EnovationConstants.LINE)) {
							boolean flag = ExcelUtil.validateCellData(formatter, row, k);
							if (flag) {
								line = formatter.formatCellValue(row.getCell(k), evaluator);
								emp.setLineName(line);
							}
						} else if (formatter.formatCellValue(cellHeader).equals(EnovationConstants.DOJ)) {
							boolean flag = ExcelUtil.validateCellData(formatter, row, k);
							if (flag) {
								doj = formatter.formatCellValue(row.getCell(k), evaluator);
								emp.setDoj(doj);
							}
						} else if (formatter.formatCellValue(cellHeader).equals(EnovationConstants.EMP_LEVEL)) {
							boolean flag = ExcelUtil.validateCellData(formatter, row, k);
							if (flag) {
								empLevel = formatter.formatCellValue(row.getCell(k), evaluator);
								emp.setEmpLevel(empLevel);
							}
						} else if (formatter.formatCellValue(cellHeader).equals(EnovationConstants.MIDDLE_NAME)) {
							boolean flag = ExcelUtil.validateCellData(formatter, row, k);
							if (flag) {
								middleName = formatter.formatCellValue(row.getCell(k), evaluator);
								emp.setMiddleName(middleName);

							}
						} else if (formatter.formatCellValue(cellHeader).equals(EnovationConstants.ADDRESS)) {
							boolean flag = ExcelUtil.validateCellData(formatter, row, k);
							if (flag) {
								address = formatter.formatCellValue(row.getCell(k), evaluator);
								emp.setAddress(address);

							}
						}
					}
				}
				empList.add(emp);
			}

		}
		return empList;

	}

	private List<EmployeeDetails> validateEmployeeDetails(Session session, List<EmployeeDetails> empList,
														  EmployeeDTO request, List<DepartmentMaster> deptMasterList, List<Line> lineMasterList,
														  List<EmployeeHierarchy> empLevel) {
		LOGGER.info("IN Dao | validateEmployeeDetails ");
		List<EmployeeDetails> errorList = new ArrayList<>();

		empList.stream().forEach(x -> {
			boolean isErrorFound = false;
			StringBuilder sb = new StringBuilder();

			if (x.getCmpyEmpId() != null && !x.getCmpyEmpId().isEmpty()) {
				int isCompanyEmpIdExist = checkIfCompanyEmpIdExist(empList, x.getCmpyEmpId());
				if (isCompanyEmpIdExist > 0) {
					LOGGER.info("In Employee Dao Impl | validateEmployeeDetails, Company employee id Exist =>"
							+ x.getCmpyEmpId());
					sb.append("Company employee Id already exist ");
					sb.append(",");
					isErrorFound = true;
				}
			}

			if (x.getFirstName() == null || x.getFirstName().isEmpty()) {
				LOGGER.info("In Employee Dao Impl | validateEmployeeDetails, First Name Is Mandatory");
				isErrorFound = true;
				sb.append("First Name is mandatory");
				sb.append(",");
			}

//			if (x.getAddress() == null || x.getAddress().isEmpty()) {
//				LOGGER.info("In Employee Dao Impl | validateEmployeeDetails, Address Is Mandatory");
//				isErrorFound = true;
//				sb.append("Address is mandatory");
//				sb.append(",");
//			}

			if ((x.getEmailId() == null || x.getEmailId().isEmpty())
					&& (x.getContactNo() == null || x.getContactNo().isEmpty())
					&& (x.getCmpyEmpId() == null || x.getCmpyEmpId().isEmpty())) {
				LOGGER.info(
						"In Employee Dao Impl | validateEmployeeDetails, Email Id/ Contact No/ Employee Id is Mandatory");
				isErrorFound = true;
				sb.append("Email Id or Contact No or Employee Id   is mandatory");
				sb.append(",");
			}

			if (x.getDesignation() == null || x.getDesignation().isEmpty()) {
				LOGGER.info("In Employee Dao Impl | validateEmployeeDetails, Designation is mandatory");
				isErrorFound = true;
				sb.append("Designation is mandatory");
				sb.append(",");
			}

			if (x.getEmailId() != null && !x.getEmailId().isEmpty()) {
				int isEmailIdExist = checkIfEmailIdExist(session, empList, x.getEmailId());
				if (isEmailIdExist > 0) {
					LOGGER.info("In Employee Dao Impl | validateEmployeeDetails, Email Id exist =>" + x.getEmailId());
					sb.append("Email Id already exist");
					sb.append(",");
					isErrorFound = true;
				}
			}

			if (x.getContactNo() != null && !x.getContactNo().isEmpty()) {
				int isContactExist = checkIfContactExist(session, empList, x.getContactNo());
				if (isContactExist > 0) {
					LOGGER.info(
							"In Employee Dao Impl | validateEmployeeDetails, Contact No exist => " + x.getContactNo());
					sb.append("Contact No already exist");
					sb.append(",");
					isErrorFound = true;
				}
			}

			if (x.getDeptName() == null || x.getDeptName().isEmpty()) {
				LOGGER.info("In Employee Dao Impl | validateEmployeeDetails, Department name is mandatory");
				isErrorFound = true;
				sb.append("Department name is mandatory");
				sb.append(",");
			} else {
				boolean matchDeptName = checkDeptName(deptMasterList, x.getDeptName(), x);
				if (!matchDeptName) {
					LOGGER.info("In Employee Dao Impl | validateEmployeeDetails, DepartmentnName not found => "
							+ x.getDeptName());
					isErrorFound = true;
					sb.append("Department name not found");
					sb.append(",");
				}
			}

			if (x.getLineName() != null && !x.getLineName().isEmpty()) {
				boolean matchLineName = checkLineName(lineMasterList, x.getLineName(), x.getDeptId(), x);
				if (!matchLineName) {
					LOGGER.info("In Employee Dao Impl | validateEmployeeDetails, Line name not found => "
							+ x.getLineName());
					isErrorFound = true;
					sb.append("Line name not found");
					sb.append(",");
				}
			}

			if (x.getDoj() == null || x.getDoj().isEmpty()) {
				LOGGER.info("In Employee Dao Impl | validateEmployeeDetails, DOJ is mandatory");
				isErrorFound = true;
				sb.append("DOJ is mandatory");
				sb.append(",");
			} else {
				boolean matchDateFormat = CommonUtils.isDateValidFormat("yyyy-MM-dd", x.getDoj());
				if (!matchDateFormat) {
					LOGGER.info("In Employee Dao Impl | validateEmployeeDetails, DOJ invalid format  => " + x.getDoj());
					isErrorFound = true;
					sb.append(" DOJ invalid format");
					sb.append(",");
				}
			}

			if (x.getEmpLevel() == null || x.getEmpLevel().isEmpty()) {
				LOGGER.info("In Employee Dao Impl | validateEmployeeDetails, EmpLevel is mandatory");
				isErrorFound = true;
				sb.append("EMP_LEVEL is mandatory");
				sb.append(",");
			} else {

				boolean matchLevelName = checkEmpLevelName(empLevel, x.getEmpLevel(), x);
				if (!matchLevelName) {
					LOGGER.info(
							"In Employee Dao Impl | validateEmployeeDetails, Emp Level not found  => " + x.getDoj());
					isErrorFound = true;
					sb.append(" Employee level name not found");
					sb.append(",");
				}

			}

			if (isErrorFound) {
				x.setErrorMsg(sb.toString());
				errorList.add(x);
			}
		});
		return errorList;
	}

	private boolean checkEmpLevelName(List<EmployeeHierarchy> empLevel, String levelName, EmployeeDetails emp) {
		boolean flag = false;
		for (EmployeeHierarchy level : empLevel) {
			if (level.getLevelName().equalsIgnoreCase(levelName)) {
				emp.setEmpLevelDetails(level);
				flag = true;
			}
		}
		return flag;
	}

	private boolean checkLineName(List<Line> lineList, String lineName, int deptId, EmployeeDetails emp) {

		boolean flag = false;
		for (Line line : lineList) {
			if (line.getName().equalsIgnoreCase(lineName) && line.getDeptId() == deptId) {
				emp.setLine(line);
				flag = true;
			}
		}
		return flag;
	}

	private boolean saveEmployeeDetails(EmployeeDTO request, List<EmployeeDetails> empList, Session session,
										List<DepartmentMaster> deptMasterList) {
		LOGGER.info("IN Dao | saveEmployeeDetails ");
		boolean flag = false;

		String action = "Add Employee";
		for (EmployeeDetails emp : empList) {

			emp.setOrganization(new OrganizationMaster(request.getOrgId()));
			emp.setBranch(new BranchMaster(request.getBranchId()));
			emp.setDept(new DepartmentMaster(getDepartmentId(deptMasterList, emp.getDeptName())));
			emp.setIsSetupCompleted(request.getIsSetupCompleted());
			emp.setCreatedBy(request.getCreatedBy());
			if (request.getRegistrationByPass() != null) {
				emp.setRegistrationByPass(request.getRegistrationByPass());
			}
			if (request.getRegistrationByPass() != null
					&& request.getRegistrationByPass().equalsIgnoreCase(EnovationConstants.Y)) {
				emp.setIsEmailVerified(EnovationConstants.ONE);
				emp.setPassword(password.encode(EnovationConstants.DEFAULT_PASSWORD));
			} else {
				emp.setPassword(password.encode(String.valueOf(CommonUtils.GenerateRandomPassword(8))));
				emp.setToken(String.valueOf(CommonUtils.generateToken(16)));
			}
			emp.setCreatedDate(CommonUtils.currentDate());
			emp.setIsDeactive(EnovationConstants.ZERO);
			emp.setEmpType(EnovationConstants.EMPLOYEE);
			Role role = getRoleDetailByName(session, (RoleName.EMPLOYEE).toString());
			session.save(emp);

			audit.insertEmpDetailsAudit(session, action, emp.getCreatedBy(), null, null, emp.getEmpId());
			session.save((new EmployeeDetailsAudit(EnovationConstants.UPDATE_PASSWORD_ACTION,
					(emp.getUpdatedBy() > 0) ? (new EmployeeDetails(emp.getUpdatedBy())) : null,
					(emp.getEmpId() > 0) ? new EmployeeDetails(emp.getEmpId()) : null,
					(emp.getPassword() != null) ? EncryptDecryptUtils.encrypt(emp.getPassword()) : null,
					(emp.getPassword() != null) ? EncryptDecryptUtils.encrypt(emp.getPassword()) : null,
					CommonUtils.currentDate())));
			emp.setRoles(Collections.singleton(role));
			/**
			 * Notify SMS and Email
			 */
			notifyToCreatedUser(session, emp, request);
		}
		flag = true;

		return flag;
	}

	private boolean processEmpData(EmployeeDTO request, Sheet sheet, DataFormatter formatter,
								   FormulaEvaluator evaluator, Session session, EmployeeDTO dto) {
		boolean flag = false;
		int empListFlag = validateEmpListSheet(sheet);
		EmployeeDTO empObj = new EmployeeDTO();
		List<DepartmentMaster> deptMasterList = new ArrayList<>();
		deptMasterList = getDeptMasterDetails(session, request.getOrgId(), request.getBranchId());

		if (empListFlag == 0) {

			readEmpListData(request, sheet, formatter, evaluator, session, dto, empObj, deptMasterList);
			if (empObj.getErrorLists() != null && !empObj.getErrorLists().isEmpty()) {
				dto.setErrorInSheet(true);
				dto.setEmpData(empObj);
				empObj.setEmployeeList(null);
			} else {
				if (empObj.getEmployeeList() != null && !empObj.getEmployeeList().isEmpty()) {

					saveOrUpdateEmpListData(session, request, empObj, deptMasterList);

				}
				flag = true;

			}

		} else {
			empObj.setReason("Invalid file format, please download sample file format and upload the data");
			empObj.setErrorInSheet(true);
			dto.setEmpData(empObj);
			dto.setErrorInSheet(true);

		}
		return flag;
	}

	private void readEmpListData(EmployeeDTO request, Sheet sheet, DataFormatter formatter, FormulaEvaluator evaluator,
								 Session session, EmployeeDTO dto, EmployeeDTO empObj, List<DepartmentMaster> deptMasterList) {
		List<EmployeeDetails> empList = new ArrayList<>();
		List<EmployeeDTO> errorLists = new ArrayList<>();

		for (int j = 0; j < sheet.getLastRowNum() + 1; j++) {
			Row row = sheet.getRow(j);
			Row header = sheet.getRow(0);
			if (row.getRowNum() > 0 && ExcelUtil.excelRowChecker(formatter, row)) {
				EmployeeDetails emp = new EmployeeDetails();

				StringBuilder sb = new StringBuilder();
				String cmpnyEmpId = null, firstName = null, lastName = null, email = null, contactNo = null,
						designation = null, department = null;
				for (int k = header.getFirstCellNum(); k < header.getLastCellNum() + 1; k++) {

					Cell cellHeader = header.getCell(k);
					if (!formatter.formatCellValue(cellHeader).equals("")
							&& formatter.formatCellValue(cellHeader).trim().length() != 0) {

						if (formatter.formatCellValue(cellHeader).equals(EnovationConstants.EMPLOYEE_ID)) {
							boolean flag = ExcelUtil.validateCellData(formatter, row, k);
							if (flag) {
								cmpnyEmpId = formatter.formatCellValue(row.getCell(k), evaluator);
								if (cmpnyEmpId != null) {
									emp.setCmpyEmpId(cmpnyEmpId);
								}

							} else {
								LOGGER.info("In Employee Dao Impl | readEmpListData, Employee Id Is Mandatory");
								sb.append("Employee Id  is mandatory");
								sb.append(",");

							}
						}

						if (formatter.formatCellValue(cellHeader).equals(EnovationConstants.FIRST_NAME)) {
							boolean flag = ExcelUtil.validateCellData(formatter, row, k);
							if (flag) {
								firstName = formatter.formatCellValue(row.getCell(k), evaluator);
								if (firstName != null) {
									emp.setFirstName(firstName);
								}

							} else {
								LOGGER.info("In Employee Dao Impl | readEmpListData, First Name Is Mandatory");
								sb.append("First Name is mandatory");
								sb.append(",");
							}
						} else if (formatter.formatCellValue(cellHeader).equals(EnovationConstants.LAST_NAME)) {
							boolean flag = ExcelUtil.validateCellData(formatter, row, k);
							if (flag) {
								lastName = formatter.formatCellValue(row.getCell(k), evaluator);
								emp.setLastName(lastName);
							}
						} else if (formatter.formatCellValue(cellHeader).equals(EnovationConstants.EMAIL_ID)) {
							boolean flag = ExcelUtil.validateCellData(formatter, row, k);
							if (flag) {
								email = formatter.formatCellValue(row.getCell(k), evaluator);
								if (email != null) {
									emp.setEmailId(email);
								}

							} else {
								LOGGER.info("In Employee Dao Impl | readEmpListData, Email Id Is Mandatory");
								sb.append("Email Id is mandatory");
								sb.append(",");
							}

						}

						else if (formatter.formatCellValue(cellHeader).equals(EnovationConstants.MOBILE_NUMBER)) {
							boolean flag = ExcelUtil.validateCellData(formatter, row, k);
							if (flag) {
								contactNo = formatter.formatCellValue(row.getCell(k), evaluator);
								if (contactNo != null) {
									emp.setContactNo(contactNo);

								}
							} else {
								LOGGER.info("In Employee Dao Impl | readEmpListData, Contact No Is Mandatory");
								sb.append("Contact No is mandatory");
								sb.append(",");
							}
						} else if (formatter.formatCellValue(cellHeader).equals(EnovationConstants.DEPARTMENT)) {
							boolean flag = ExcelUtil.validateCellData(formatter, row, k);
							if (flag) {
								department = formatter.formatCellValue(row.getCell(k), evaluator);
								if (department != null) {
									boolean matchDeptName = checkDeptName(deptMasterList, department, emp);
									if (!matchDeptName) {
										LOGGER.info(
												"In Employee Dao Impl | readEmpListData, Department Name not found => "
														+ department);
										sb.append("Department Name not found");
										sb.append(",");
									} else {
										emp.setDeptName(department);
									}
								}

							} else {
								LOGGER.info("In Employee Dao Impl | readEmpListData,department Is Mandatory");
								sb.append("department is mandatory");
								sb.append(",");
							}

						} else if (formatter.formatCellValue(cellHeader).equals(EnovationConstants.DESIGNATION)) {
							boolean flag = ExcelUtil.validateCellData(formatter, row, k);
							if (flag) {
								designation = formatter.formatCellValue(row.getCell(k), evaluator);
								if (designation != null) {
									emp.setDesignation(designation);
								}

							} else {
								LOGGER.info("In Employee Dao Impl | readEmpListData,designation Is Mandatory");
								sb.append("designation is mandatory");
								sb.append(",");
							}
						}
					}
				}

				empList.add(emp);
				if (sb != null && sb.length() > 0) {
					EmployeeDTO error = new EmployeeDTO();
					error.setCmpyEmpId(cmpnyEmpId);
					error.setFirstName(firstName);
					error.setLastName(lastName);
					error.setEmail(email);
					error.setContactNo(contactNo);
					error.setDesignation(designation);
					error.setDepartment(department);
					error.setMessage(sb);
					errorLists.add(error);
				}

			}
		}

		empObj.setErrorLists(errorLists);
		empObj.setEmployeeList(empList);

	}

	private boolean checkDeptName(List<DepartmentMaster> deptMasterList, String deptName, EmployeeDetails emp) {

		boolean flag = false;
		for (DepartmentMaster dept : deptMasterList) {
			if (dept.getDeptName().equalsIgnoreCase(deptName)) {
				emp.setDeptId(dept.getDeptId());
				flag = true;
			}
		}
		return flag;
	}

	private void saveOrUpdateEmpListData(Session session, EmployeeDTO request, EmployeeDTO empObj,
										 List<DepartmentMaster> deptMasterList) {
		LOGGER.info("In Employee Dao | saveOrUpdateEmpListData ");

		List<EmployeeDetails> existingEmpData = session
				.createQuery("FROM EmployeeDetails  WHERE branch.branchId=:branchId AND organization.orgId =:orgId")
				.setParameter("branchId", request.getBranchId()).setParameter("orgId", request.getOrgId())
				.getResultList();

		empObj.getEmployeeList().stream().forEach(x -> {

			EmployeeDetails obj = checkIfDataExist(x, existingEmpData, request);

			if (obj != null) {

				if (x.getFirstName() != null) {
					obj.setFirstName(x.getFirstName());
				}
				if (x.getLastName() != null) {
					obj.setLastName(x.getLastName());
				}
				if (x.getEmailId() != null) {
					obj.setEmailId(x.getEmailId());
				}
				if (x.getContactNo() != null) {
					obj.setContactNo(x.getContactNo());
				}
				if (x.getDeptName() != null) {
					obj.setDept(new DepartmentMaster(getDepartmentId(deptMasterList, x.getDeptName())));
				}
				if (x.getDesignation() != null) {
					obj.setDesignation(x.getDesignation());
				}
				session.update(obj);
				String action = "Update Employee";
				audit.insertEmpDetailsAudit(session, action, obj.getCreatedBy(), null, null, obj.getEmpId());
				session.save((new EmployeeDetailsAudit(EnovationConstants.UPDATE_PASSWORD_ACTION,
						(obj.getUpdatedBy() > 0) ? (new EmployeeDetails(obj.getUpdatedBy())) : null,
						(obj.getEmpId() > 0) ? new EmployeeDetails(obj.getEmpId()) : null,
						(obj.getPassword() != null) ? EncryptDecryptUtils.encrypt(obj.getPassword()) : null,
						(obj.getPassword() != null) ? EncryptDecryptUtils.encrypt(obj.getPassword()) : null,
						CommonUtils.currentDate())));

			} else {
				String action = "Add Employee";
				x.setOrganization(new OrganizationMaster(request.getOrgId()));
				x.setBranch(new BranchMaster(request.getBranchId()));
				x.setDept(new DepartmentMaster(getDepartmentId(deptMasterList, x.getDeptName())));
				x.setIsSetupCompleted(request.getIsSetupCompleted());
				x.setCreatedBy(request.getCreatedBy());
				if (request.getRegistrationByPass() != null) {
					x.setRegistrationByPass(request.getRegistrationByPass());
				}
				if (request.getRegistrationByPass() != null
						&& request.getRegistrationByPass().equalsIgnoreCase(EnovationConstants.Y)) {
					x.setIsEmailVerified(EnovationConstants.ONE);
					x.setPassword(password.encode(EnovationConstants.DEFAULT_PASSWORD));
				} else {
					x.setPassword(password.encode(String.valueOf(CommonUtils.GenerateRandomPassword(8))));
					x.setToken(String.valueOf(CommonUtils.generateToken(16)));
				}
				x.setCreatedDate(CommonUtils.currentDate());
				x.setIsDeactive(EnovationConstants.ZERO);

				Role role = getRoleDetailByName(session, (RoleName.EMPLOYEE).toString());
				session.save(x);

				audit.insertEmpDetailsAudit(session, action, x.getCreatedBy(), null, null, x.getEmpId());
				session.save((new EmployeeDetailsAudit(EnovationConstants.UPDATE_PASSWORD_ACTION,
						(x.getUpdatedBy() > 0) ? (new EmployeeDetails(x.getUpdatedBy())) : null,
						(x.getEmpId() > 0) ? new EmployeeDetails(x.getEmpId()) : null,
						(x.getPassword() != null) ? EncryptDecryptUtils.encrypt(x.getPassword()) : null,
						(x.getPassword() != null) ? EncryptDecryptUtils.encrypt(x.getPassword()) : null,
						CommonUtils.currentDate())));
				x.setRoles(Collections.singleton(role));
				/**
				 * Notify SMS and Email
				 */
				notifyToCreatedUser(session, x, request);

			}

		});

	}

	private EmployeeDetails checkIfDataExist(EmployeeDetails x, List<EmployeeDetails> existingEmpData,
											 EmployeeDTO request) {
		EmployeeDetails empObj = null;
		if (existingEmpData != null && !existingEmpData.isEmpty()) {

			for (EmployeeDetails obj : existingEmpData) {
				if (request.getBranchId() == obj.getBranch().getBranchId()
						&& x.getCmpyEmpId().equals(obj.getCmpyEmpId())
						&& request.getOrgId() == obj.getOrganization().getOrgId()) {
					empObj = obj;
					break;
				}
			}
		}

		return empObj;

	}

	private int checkIfCompanyEmpIdExist(List<EmployeeDetails> empList, String cmpyEmpId) {
		Session session = getCurrentSession();
		int len = 0;

		List<Object[]> objList = session
				.createNativeQuery("select * from tbl_employee_details where cmpy_emp_id=:cmpyEmpId and is_deactive=0")
				.setParameter("cmpyEmpId", cmpyEmpId).getResultList();
		len = objList.size();
		System.out.println("list Size=>" + len);

		return len;
	}

	private int checkIfEmailIdExist(Session session, List<EmployeeDetails> empList, String emailId) {

		int length = 0;

		List<Object[]> objList = session.createNativeQuery("select * from tbl_employee_details where email_id=:emailId  and is_deactive=0 ")
				.setParameter("emailId", emailId).getResultList();

		if (objList != null && !objList.isEmpty()) {
			LOGGER.info("EmployeeDaoImple | checkIfEmailIdExist, list size " + length);
			length = objList.size();
		} else {
			LOGGER.info("EmployeeDaoImple | checkIfEmailIdExist, list size 0");
		}

		return length;
	}

	private int getDepartmentId(List<DepartmentMaster> deptMasterList, String deptName) {
		int deptId = 0;
		if (deptMasterList != null && !deptMasterList.isEmpty()) {
			for (DepartmentMaster dept : deptMasterList) {
				if (dept.getDeptName().equalsIgnoreCase(deptName)) {
					deptId = dept.getDeptId();
				}
			}
		}
		return deptId;

	}

	private int checkIfContactExist(Session session, List<EmployeeDetails> empList, String contactNo) {

		int length = 0;
		List<Object[]> objList = session
				.createNativeQuery("select * from tbl_employee_details where contact_no=:contactNo  and is_deactive=0 ")
				.setParameter("contactNo", contactNo).getResultList();

		if (objList != null && !objList.isEmpty()) {
			LOGGER.info("EmployeeDaoImple | checkIfContactExist, list size " + length);
			length = objList.size();
		} else {
			LOGGER.info("EmployeeDaoImple | checkIfContactExist, list size 0");
		}

		return length;

	}

	private Role getRoleDetailByName(Session session, String rolename) {

		List<Role> roleDetails = session.createNativeQuery("select * FROM roles where name=:name").addEntity(Role.class)
				.setParameter("name", rolename).getResultList();

		return roleDetails.get(0);
	}

	private int validateEmpListSheet(Sheet sheet) {
		// TODO Auto-generated method stubLOGGER.info("# In PMS Dao |
		// validateOtherParametersSheet");
		int flag = 0;
		Row header = sheet.getRow(0);
		List<String> headersFromExcel = new ArrayList<>();
		List<String> mandatoryheaders = new ArrayList<>();
		mandatoryheaders.add(EnovationConstants.EMPLOYEE_ID);
		mandatoryheaders.add(EnovationConstants.FIRST_NAME);
		mandatoryheaders.add(EnovationConstants.LAST_NAME);
		mandatoryheaders.add(EnovationConstants.EMAIL_ID);
		mandatoryheaders.add(EnovationConstants.MOBILE_NUMBER);
		mandatoryheaders.add(EnovationConstants.DEPARTMENT);
		mandatoryheaders.add(EnovationConstants.DESIGNATION);

		for (int k = header.getFirstCellNum(); k < header.getLastCellNum() + 1; k++) {
			Cell cellHeader = header.getCell(k);
			if (cellHeader != null) {
				headersFromExcel.add(cellHeader.toString().trim());
			}
		}

		if (headersFromExcel != null && !headersFromExcel.isEmpty()) {
			if (headersFromExcel.containsAll(mandatoryheaders)) {
				flag = 0;
			} else {
				flag = 2;
			}
		} else {
			flag = 2;
		}

		return flag;

	}

	/**
	 * @author rakesh 07 sept 2020
	 * @param session
	 * @param emp
	 * @param request
	 * @desc for SMS
	 * @return
	 */
	private boolean notifyToCreatedUser(Session session, EmployeeDetails emp, EmployeeDTO request) {

		boolean flag = false;
		String shortPortalLink = "";
		if (request.getOrgId() > 0) {
			OrganizationMaster org = session.get(OrganizationMaster.class, request.getOrgId());
			shortPortalLink = org.getShortPortalLink();
		}
		if (request.getIsSetupCompleted() == EnovationConstants.ONE) {

			if (request.getRegistrationByPass() != null
					&& request.getRegistrationByPass().equalsIgnoreCase(EnovationConstants.Y)) {
				if (emp.getContactNo() != null && emp.getContactNo().length() > 0) {

					String content1 = "Dear " + emp.getFirstName() + ",\n" + "Your myeNovation credentials :\n"
							+ "Login ID : " + emp.getContactNo() + "\n" + "Password : "
							+ EnovationConstants.DEFAULT_PASSWORD + "\n" + "Portal Link : " + shortPortalLink + "\n"
							+ "Android Link: " + EnovationConfig.buddyConfig.get("appLink") + " \n" + "IOS Link: "
							+ EnovationConfig.buddyConfig.get("appLinkIos") + " - GREENTIN SOLUTIONS PRIVATE LIMITED";

					System.out.println("sms content : " + content1);
					taskExecutor.execute(new SmsUtil(emp.getContactNo(), content1, communication));
					flag = true;
				}

			} else {
				if (emp.getContactNo() != null && emp.getContactNo().length() > 2) {
					String content = "Dear " + emp.getFirstName()
							+ ",                                                              %0a"
							+ " Welcome To myeNovation!!                                                                       %0a"
							+ " Please Activate Your Account by Clicking Link Below                                                                      %0a "
							+ EnovationConfig.buddyConfig.get("verifyEmployeeEmailLink") + emp.getToken()
							+ " Login ID :" + emp.getContactNo()
							+ "                                                                  %0a "
							+ " Portal Link : " + request.getPortalLink()
							+ "                                                                      %0a "
							+ " Android Link : " + EnovationConfig.buddyConfig.get("appLink")
							+ "                                                                      %0a "
							+ " IOS Link : " + EnovationConfig.buddyConfig.get("appLinkIos")
							+ "                                                                      %0a ";
					taskExecutor.execute(
							new SmsUtil(emp.getContactNo(), content.replaceAll("[\r\n]+", " "), communication));
					flag = true;
				}
			}
			if (emp.getEmailId() != null && !emp.getEmailId().isEmpty()) {
				notifyEmailToCreatedUser(session, emp, request);
			}
			session.createNativeQuery(" update tbl_employee_details set is_notify=:isNotify where emp_id=:empId ")
					.setParameter("isNotify", EnovationConstants.ONE).setParameter("empId", emp.getEmpId())
					.executeUpdate();
		}
		return flag;
	}

	/**
	 * @author rakesh 07 sept 2020
	 * @param session
	 * @param emp
	 * @param request
	 * @desc For mail sending
	 * @return
	 */
	private boolean notifyEmailToCreatedUser(Session session, EmployeeDetails emp, EmployeeDTO request) {
		boolean flag = false;
		EmailTemplateMaster messageContent = null;
		String verifyLink = "";
		String portalLink = "";
		String superAdmin = "";

		String roles = "";
		List<String> roleName = new ArrayList<>();
		if (emp.getRoles() != null && emp.getRoles().size() > 0) {
			for (Role r : emp.getRoles()) {
				roleName.add((r.getName()).toString());
			}
			roles = StringUtils.join(roleName, ',');
		}

		if (request.getPortalLink() != null) {
			portalLink = request.getPortalLink();
		}

		if (request.getCreatedByName() != null) {
			superAdmin = request.getCreatedByName();
		}

		if (request.getIsSetupCompleted() == EnovationConstants.ONE) {
			if (request.getRegistrationByPass() != null
					&& request.getRegistrationByPass().equals(EnovationConstants.Y)) {

				messageContent = enoConfig.getMessageContent(EnovationConstants.NEW_REGISTRATION_TEMPLATE_PASSWORD);
			} else {
				verifyLink = portalLink + String.valueOf(EnovationConfig.buddyConfig.get("verifyEmployeeEmailLink"));
				messageContent = enoConfig.getMessageContent(EnovationConstants.sendEmailNewRegistedEmpSubject);
			}
			EmailTemplateMaster emailTemplateImg = enoConfig.getMessageContent(EnovationConstants.EmailTemplateImg);

			String mailContent = null, subject = messageContent.getSubject();
			String fname = "";
			if (emp.getFirstName() != null) {
				fname = emp.getFirstName();
			}

			if (messageContent.getBody() != null && messageContent.getBody().contains("{name}")) {
				mailContent = messageContent.getBody().replaceAll(Pattern.quote("{name}"), fname.trim())
						.replaceAll(Pattern.quote("{vlink}"), verifyLink + emp.getToken())
						.replaceAll(Pattern.quote("{email}"), (emp.getEmailId() != null ? emp.getEmailId() : ""))
						.replaceAll(Pattern.quote("{password}"), EnovationConstants.DEFAULT_PASSWORD)
						.replaceAll(Pattern.quote("{bckImg}"),
								String.valueOf(EnovationConfig.buddyConfig.get("ProfilePicPathUrl")
										+ emailTemplateImg.getFcmBody()))

						.replaceAll(Pattern.quote("{appLink}"), EnovationConfig.buddyConfig.get("appLink"))
						.replaceAll(Pattern.quote("{appLinkIos}"), EnovationConfig.buddyConfig.get("appLinkIos"))
						.replaceAll(Pattern.quote("{superAdminName}"), superAdmin)
						.replaceAll(Pattern.quote("{portalLink}"), portalLink)
						.replaceAll(Pattern.quote("{roles}"), roles);
			}
			if (emp.getEmailId() != null) {
				taskExecutor.execute(new MailUtil(emp.getEmailId(), subject, mailContent, communication));
			}
		}
		return flag;
	}

	@Override
	public boolean reActivationEmployee(EmployeeDTO request) {
		LOGGER.info("# Inside Employee DAO IMPL | sendLoginDetSMS");
		Session session = getCurrentSession();

		return session.createNativeQuery("UPDATE tbl_employee_details SET is_deactive=:isActive WHERE emp_id=:empId ")
				.setParameter("isActive", String.valueOf(EnovationConstants.ZERO))
				.setParameter("empId", request.getEmpId()).executeUpdate() != 0 ? true : false;
	}

}
