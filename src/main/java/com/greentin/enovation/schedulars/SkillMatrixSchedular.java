package com.greentin.enovation.schedulars;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.request;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import javax.persistence.Tuple;
import javax.transaction.Transactional;

import org.apache.commons.lang.exception.ExceptionUtils;
import org.hibernate.Session;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.env.Environment;
import org.springframework.core.task.TaskExecutor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import com.greentin.enovation.communication.ICommunication;
import com.greentin.enovation.config.EnovationConfig;
import com.greentin.enovation.dto.MailDTO;
import com.greentin.enovation.dto.SMEmpSkillMatrixDTO;
import com.greentin.enovation.dto.SkillMatrixRequest;
import com.greentin.enovation.model.BranchMaster;
import com.greentin.enovation.model.DepartmentMaster;
import com.greentin.enovation.model.EmailTemplateMaster;
import com.greentin.enovation.model.EmployeeDetails;
import com.greentin.enovation.model.dwm.Line;
import com.greentin.enovation.model.skillMatrix.SMAssessment;
import com.greentin.enovation.model.skillMatrix.SMAssessmentQues;
import com.greentin.enovation.model.skillMatrix.SMChecksheet;
import com.greentin.enovation.model.skillMatrix.SMOJTAssessment;
import com.greentin.enovation.model.skillMatrix.SMOJTAssessmentQues;
import com.greentin.enovation.model.skillMatrix.SMOJTRegis;
import com.greentin.enovation.model.skillMatrix.SMOJTSkilling;
import com.greentin.enovation.model.skillMatrix.SMOJTSkillingAudit;
import com.greentin.enovation.model.skillMatrix.SMSkillLevel;
import com.greentin.enovation.model.skillMatrix.SMStage;
import com.greentin.enovation.model.skillMatrix.SMWorkstations;
import com.greentin.enovation.skillmatrix.SMConstant;
import com.greentin.enovation.skillmatrix.SkillMatrixUtils;
import com.greentin.enovation.skillmatrix.SkillMatrixWorker;
import com.greentin.enovation.utils.BaseRepository;
import com.greentin.enovation.utils.CommonUtils;
import com.greentin.enovation.utils.EnovationConstants;
import com.greentin.enovation.utils.EnovationException;
import com.greentin.enovation.utils.MailUtil;

@Transactional
@Component
public class SkillMatrixSchedular extends BaseRepository {

	private static final Logger logger = LoggerFactory.getLogger(SkillMatrixSchedular.class);

	@Autowired
	Environment env;

	@Autowired
	EnovationConfig enoConfig;

	@Autowired
	@Qualifier("taskExecutor")
	private TaskExecutor taskExecutor;

	@Autowired
	CommonUtils commonUtils;

	@Autowired
	EnovationConfig evonationConfig;

	@Autowired
	private ICommunication communication;

	@Autowired
	SkillMatrixWorker smWorker;

	@Autowired
	SkillMatrixUtils smUtils;

	@SuppressWarnings({ "unchecked", "static-access" })
//	@Scheduled(cron = "0 0 * * 7")
	@Scheduled(cron = "0 0 0 * * ?")
	public void assignPreAssessmentScheduler() {
		logger.info("ASSESSMENT SCHEDULER");
		Session session = getCurrentSession();
		LocalDate currentDate = LocalDate.now();
		String startDate = String.valueOf(new Date());
		String errorLog = null;
		List<MailDTO> mailList = new ArrayList<>();
		try {
			List<SkillMatrixRequest> assList = fetchAssessmentData(session);
			if (!CollectionUtils.isEmpty(assList)) {
				for (SkillMatrixRequest dotObj : assList) {
					if (dotObj != null && dotObj.getAssessmentId() > 0 && dotObj.getAssessmentDueDate() != null
							&& dotObj.getAssessmentDueDate().equals(currentDate)) {
						processAssessment(session, dotObj,mailList);
					}
				}
			}
		} catch (

		Exception e) {
			e.printStackTrace();
			errorLog = String.valueOf(ExceptionUtils.getStackTrace(e));

			commonUtils.sendAPIFailedExceptionMail("Exception Occured At - " + this.getClass().getSimpleName(),
					" <p><b> Method Name - </b>" + new Throwable().getStackTrace()[0].getMethodName() + " <br></p> "
							+ "<p><b> Exception - </b>" + ExceptionUtils.getStackTrace(e));

		} finally {

			Session hSession = getCurrentSession();
			SchedularAudit audit = new SchedularAudit("assignAssessment", startDate, String.valueOf(new Date()));

			if (errorLog != null) {
				audit.setErrorLog(errorLog);
			}
			if (!CollectionUtils.isEmpty(mailList)) {
				taskExecutor.execute(new MailUtil(mailList, communication));
			}
			hSession.save(audit);

		}
	}

	public void processAssessment(Session session, SkillMatrixRequest dotObj, List<MailDTO> mailList) {
		logger.info("assignPreAssessmentScheduler | Inside processAssessment");
		SMOJTSkillingAudit savedSkillAudit = employeeAssessmentSkillAudit(session, dotObj, SMConstant.STAGE5_ID);
		SMOJTAssessment ojtAssessment = employeeAssessmentAssign(session, dotObj, savedSkillAudit);
		List<Tuple> tupleObj = fetchQuestionData(session, dotObj.getAssessmentId());

		for (Tuple questionTuple : tupleObj) {
			SMOJTAssessmentQues savedAssQue = saveOJTAssessmentQue(session, ojtAssessment, questionTuple);
		}
		SkillMatrixRequest request = new SkillMatrixRequest();
		request.setSkillingId(dotObj.getSkillingId());
		sendAssessmentAssignedEmailToOE(session, mailList, request);

	}

	private void sendAssessmentAssignedEmailToOE(Session session, List<MailDTO> mailList, SkillMatrixRequest request) {
		logger.info("assignPreAssessmentScheduler | Inside sendAssessmentAssignedEmailToOE");
		request = smWorker.getEmployeeDetailsForEmail(session, request);

		if (request.getEmailId() != null && request.getEmailId().length() > 0) {
			LocalDate date = LocalDate.now();
			EmailTemplateMaster messageContent = null;
			messageContent = evonationConfig.getMessageContent(EnovationConstants.SKILLMATRIX_EMAIL_TEMPLATE);
			String subject = messageContent.getSubject().replaceAll(Pattern.quote("{subject}"),
					CommonUtils.objectToString("Reminder notification"));
			String body = env.getProperty("Assessment_Assign_TO_OE.body");
			if (messageContent != null) {

				String content = messageContent.getBody().replaceAll(Pattern.quote("{body}"), body)
						.replaceAll(Pattern.quote("{AssignedBy}"), CommonUtils.objectToString(request.getAssignedBy()))
						.replaceAll(Pattern.quote("{name}"), CommonUtils.objectToString(request.getEmpName()))
						.replaceAll(Pattern.quote("{subject}"), CommonUtils.objectToString("Reminder notification"))
						.replaceAll(Pattern.quote("{portalLink}"), CommonUtils.objectToString(request.getPortalLink()))
						.replaceAll(Pattern.quote("{thisYear}"), (String.valueOf(LocalDate.now().getYear())))
						.replaceAll(Pattern.quote("{cell}"), CommonUtils.objectToString(request.getLineName()))
						.replaceAll(Pattern.quote("{workstation}"), // paste workstation from emailData
								CommonUtils.objectToString(request.getWorkstation()))
						.replaceAll(Pattern.quote("{assessmentDate}"), CommonUtils.objectToString(date))
						.replaceAll(Pattern.quote("{assessmentStartDate}"),
								CommonUtils.objectToString(request.getStartDate()))
						.replaceAll(Pattern.quote("{RequiredLevel}"), request.getSkillLevel())
						.replaceAll(Pattern.quote("{assessmentStatus}"), request.getStatus())
						.replaceAll(Pattern.quote("{empName}"), request.getEmpName())
						.replaceAll(Pattern.quote("{companyLogo}"),
								(request.getLogo() != null && request.getLogo().length() > 0)
										? EnovationConfig.buddyConfig.get("ProfilePicPathUrl") + request.getLogo()
										: EnovationConstants.myeNovationLogoPath);

				MailDTO mail = new MailDTO(request.getEmailId(), subject, content);
				mailList.add(mail);
			}
		}
	}

	public List<Tuple> fetchQuestionData(Session session, long assessmentId) {
		logger.info("ASSESSMENT | Inside fetchQuestionData");
		return session.createNativeQuery(
				"SELECT id as questionId FROM sm_assessment_ques WHERE is_active=1 and  assessment_id =:assessmentId",
				Tuple.class).setParameter("assessmentId", assessmentId).getResultList();
	}

	private List<SkillMatrixRequest> fetchAssessmentData(Session session) {
		logger.info("ASSESSMENT | Inside fetchAssessmentData");
		List<SkillMatrixRequest> list = new ArrayList<>();

		List<Tuple> tupleList = session.createNativeQuery(
				"  Select distinct s.id as skillingId,s.status as status ,s.assessment_due_date as assessmentDueDate,\r\n"
						+ "	 r.id as regisId,r.emp_id as regempId,concat(e.first_name ,' ',e.last_name)  as empName,\r\n"
						+ "	 r.current_skill_level_id as currentSkillLevelId, e.emp_id as empId ,\r\n"
						+ "	 r.desired_skill_level_id as desiredSkillLevelId ,r.status as regstatus, a.total_marks as totalMarks,\r\n"
						+ "	 a.id as assessmentId,a.skill_level_id as skillLevelId,a.passing_marks as passingMark,a.time as time \r\n"
						+ "	 from sm_ojt_skilling  s  inner join sm_ojt_regis r on r.id=s.ojt_regis_id\r\n"
						+ "	 INNER join tbl_employee_details e on e.emp_id=r.emp_id\r\n"
						+ "	 INNER JOIN sm_assessment a ON a.id = s.assessment_id\r\n" + "	 where s.status=:statuses ",
				Tuple.class).setParameter("statuses", SMConstant.ASSESSMENT_PENDING).getResultList();
		if (!CollectionUtils.isEmpty(tupleList)) {
			for (Tuple obj : tupleList) {
				SkillMatrixRequest dtoObj = null;
				dtoObj = new SkillMatrixRequest();

				dtoObj.setEmpId(CommonUtils.objectToInt(obj.get("empId")));
				dtoObj.setAssessmentId(CommonUtils.objectToLong(obj.get("assessmentId")));
				dtoObj.setAssessmentDueDate(convertTupleToAssessmentDueDate(obj));
				dtoObj.setStatus(CommonUtils.objectToString(obj.get("status")));
				dtoObj.setSkillingId(CommonUtils.objectToLong(obj.get("skillingId")));
				dtoObj.setOjtRegiId(CommonUtils.objectToLong(obj.get("regisId")));
				dtoObj.setTotalMarks(CommonUtils.objectToDouble(obj.get("totalMarks")));
				dtoObj.setPassingMark(CommonUtils.objectToDouble(obj.get("passingMark")));
				dtoObj.setTime(CommonUtils.objectToInt(obj.get("time")));
				list.add(dtoObj);
			}
		}
		return list;
	}

	private LocalDate convertTupleToAssessmentDueDate(Tuple tuple) {
		logger.info("ASSESSMENT | Inside convertTupleToAssessmentDueDate");
		if (tuple.get("assessmentDueDate") != null) {
			java.sql.Date sqlDate = CommonUtils.convertStringToSqlDate(tuple.get("assessmentDueDate").toString());
			return sqlDate.toLocalDate();
		} else {
			return null;
		}
	}

	private SMOJTAssessment employeeAssessmentAssign(Session session, SkillMatrixRequest dotObj,
			SMOJTSkillingAudit skillAudit) {
		logger.info("ASSESSMENT | Inside employeeAssessmentAssign");
		SMOJTAssessment assignment = new SMOJTAssessment();

		if (dotObj.getOjtRegiId() > 0) {
			assignment.setOjtRegis(new SMOJTRegis(dotObj.getOjtRegiId()));
		}
		if (dotObj.getAssessmentId() > 0) {
			assignment.setAssessment(new SMAssessment(dotObj.getAssessmentId()));
		}
		if (dotObj.getSkillingId() > 0) {
			assignment.setSkilling(new SMOJTSkilling(dotObj.getSkillingId()));
		}
		if (dotObj.getTotalMarks() > 0) {
			assignment.setTotalMarks(dotObj.getTotalMarks());
		}
		if (dotObj.getPassingMark() > 0) {
			assignment.setPassingMarks(dotObj.getPassingMark());
		}
		assignment.setTotalAssessmentTime(dotObj.getTime());
		assignment.setSkillingAudit(skillAudit);

		session.save(assignment);
		return assignment;
	}

	private SMOJTSkillingAudit employeeAssessmentSkillAudit(Session session, SkillMatrixRequest dotObj, long stageId) {
		logger.info("ASSESSMENT | Inside employeeAssessmentSkillAudit");
		SMOJTSkillingAudit skill = new SMOJTSkillingAudit();
		if (dotObj.getSkillingId() > 0) {
			skill.setSkilling(new SMOJTSkilling(dotObj.getSkillingId()));
		}
		if (dotObj.getEmpId() > 0) {
			skill.setEmpDetails(new EmployeeDetails(dotObj.getEmpId()));
		}
		if (dotObj.getOjtRegiId() > 0) {
			skill.setOjtRegis(new SMOJTRegis(dotObj.getOjtRegiId()));
		}

		skill.setStage(new SMStage(stageId));
		skill.setStatus(EnovationConstants.PENDING_STRING);

		session.save(skill);

		return skill;
	}

	private SMOJTAssessmentQues saveOJTAssessmentQue(Session session, SMOJTAssessment ojtAssessment,
			Tuple questionTuple) {
		logger.info("ASSESSMENT | Inside saveOJTAssessmentQue");

		SMOJTAssessmentQues assessment = new SMOJTAssessmentQues();
		assessment.setOjtAssessment(ojtAssessment);
		assessment.setQues(new SMAssessmentQues(CommonUtils.objectToInt(questionTuple.get("questionId"))));
		session.save(assessment);

		return assessment;

	}

	// changed By rajdeep saftey pass or fail check code chaged 2024-04-29
	@SuppressWarnings("unchecked")
	@Scheduled(cron = "0 0 1 * * *")
//	@Scheduled(cron = "0 30 9 * * *")
	public void publishOJT() {
		logger.info("Inside SkillMatrixSchedular | publishOJT ");
		Session session = getCurrentSession();
		String errorLog = "";
		String status = "";
		LocalDate currentDate = LocalDate.now();
		String startDate = String.valueOf(currentDate);
		String excDate = String.valueOf(new Date());

		try {

			logger.info("Inside SuggestionScheduler | getBranchList " + "startDate: " + startDate);
			List<Tuple> branchList = session.createNativeQuery(
					"select b.branch_id as branchId,b.name as branch,o.logo as orgLogo,o.portal_link as portalLink ,o.org_id as orgId\r\n"
							+ " from master_branch b left join master_organization o on o.org_id=b.org_id where b.is_active='Y' ",
					Tuple.class).getResultList();

			if (!CollectionUtils.isEmpty(branchList)) {
				for (Tuple branch : branchList) {

					int branchId = CommonUtils.objectToInt(branch.get("branchId"));
					String orgLogo = CommonUtils.objectToString(branch.get("orgLogo"));
					String portalLink = CommonUtils.objectToString(branch.get("portalLink"));

					if (branchId > 0) {
						List<Tuple> planList = getOJTPlanList(session, branchId, startDate);
						if (!CollectionUtils.isEmpty(planList)) {
							for (Tuple plan : planList) {
								long planId = CommonUtils.objectToLong(plan.get("planId"));
								logger.info("planId: " + planId);
								if (planId > 0) {
									long ojtRegisId = 0;
									try {
										List<MailDTO> mailList = new ArrayList<>();
										logger.info("Inside SuggestionScheduler | planId: {} ", planId);
										List<Tuple> ojtRegisList = getRegisList(session, branchId, planId);
										if (!CollectionUtils.isEmpty(ojtRegisList)) {

											for (Tuple objRegis : ojtRegisList) {
												logger.info(
														"Inside SkillMatrixSchedular | publishOJT | objRegisId : {} ",
														CommonUtils.objectToString(objRegis.get("regisId")));
												SkillMatrixRequest emailObj = new SkillMatrixRequest();
												String safetyAssessStatus = null;
												emailObj.setBranchName(
														CommonUtils.objectToString(branch.get("branch")));

												setOjtRegiParam(emailObj, objRegis);

												safetyAssessStatus = smWorker.checkSafetyAssessResult(session,
														emailObj.getEmpId());

												if (safetyAssessStatus.equals(SMConstant.PASS)) {
													logger.info(
															" Inside SkillMatrixSchedular | publishOJT  | regisId: {} ",
															emailObj.getOjtRegiId());

													if (emailObj.getOjtRegiId() > 0 && emailObj.getEmpId() > 0
															&& emailObj.getLineId() > 0 && emailObj.getDeptId() > 0) {
														logger.info(
																" Inside SkillMatrixSchedular | publishOJT  | regisId: {} ",
																emailObj.getOjtRegiId());
														SMOJTRegis regis = new SMOJTRegis();
														regis.setId(emailObj.getOjtRegiId());
														ojtRegisId = emailObj.getOjtRegiId();
														if (emailObj.getEmpId() > 0) {
															regis.setEmpDetails(
																	new EmployeeDetails(emailObj.getEmpId()));
														}
														regis.setBranch(new BranchMaster(branchId));

														if (emailObj.getDeptId() > 0) {
															regis.setDept(new DepartmentMaster(emailObj.getDeptId()));
														}
														regis.setDesiredSkillLevel(new SMSkillLevel(CommonUtils
																.objectToLong(objRegis.get("desiredLvlId"))));

														regis.setLine(new Line(emailObj.getLineId()));
														regis.setWorkstation(
																new SMWorkstations(emailObj.getWorkstationId()));
														/*
														 * save OJT registration
														 */
														smWorker.saveOJTSkillingDetails(session, regis);

														if (emailObj.getEmailId() != null
																&& emailObj.getEmailId().length() > 0) {

															/*
															 * send Email to OE
															 */
															smWorker.sendEmailToOE(portalLink, mailList, emailObj,
																	orgLogo);
															smWorker.sendEmailToTrainer(portalLink, mailList, emailObj,
																	orgLogo);
														} else {
															logger.info(
																	"Inside SuggestionScheduler | emailId Not Found");
														}
													} else {
														errorLog += "planId: " + planId + ", OjtRegisId: "
																+ emailObj.getOjtRegiId() + ", empId: "
																+ emailObj.getEmpId() + ", LineId: "
																+ emailObj.getLineId() + ", deptId: "
																+ emailObj.getDeptId() + "\n";
													}
												} else {
													errorLog += "Safety Assessment Result Not Found";
												}
											}
										}
										if (!CollectionUtils.isEmpty(mailList)) {
											logger.info("#EMail sended");
											taskExecutor.execute(new MailUtil(mailList, communication));
										}
										session.createNativeQuery(
												" update sm_ojt_plan set scheduler_status='PUBLISHED' where id =:planId")
												.setParameter("planId", planId).executeUpdate();
									} catch (EnovationException e) {
										errorLog += "planId: " + planId + ", OjtRegisId: " + ojtRegisId + ", error: "
												+ e.getReason() + "\n";
										session.createNativeQuery(
												" update sm_ojt_plan set scheduler_status='FAIL' where id =:planId")
												.setParameter("planId", planId).executeUpdate();
									} catch (Exception e) {
										errorLog += "planId: " + planId + ", OjtRegisId: " + ojtRegisId + ", error: "
												+ String.valueOf(ExceptionUtils.getStackTrace(e)) + "\n";
										session.createNativeQuery(
												" update sm_ojt_plan set scheduler_status='FAIL' where id =:planId")
												.setParameter("planId", planId).executeUpdate();
									}
								}
							}
						} else {
							logger.info("No OJT Plan For Current Date : " + startDate);
//							errorLog = "No OJT Plan For Current Date : " + startDate;
						}
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			status = EnovationConstants.FAIL;
			errorLog += String.valueOf(ExceptionUtils.getStackTrace(e)) + "\n";

			commonUtils.sendAPIFailedExceptionMail("Exception Occured At - " + this.getClass().getSimpleName(),
					" <p><b> Method Name - </b>" + new Throwable().getStackTrace()[0].getMethodName() + " <br></p> "
							+ "<p><b> Exception - </b>" + ExceptionUtils.getStackTrace(e));

		} finally {

			Session hSession = getCurrentSession();
			SchedularAudit audit = new SchedularAudit("SMOJTPlanPublish", excDate, String.valueOf(new Date()));

			if (errorLog != null && errorLog.length() > 0) {
				audit.setErrorLog(errorLog);
			}
			hSession.save(audit);
		}
	}

	private void setOjtRegiParam(SkillMatrixRequest emailObj, Tuple objRegis) {
		emailObj.setOjtRegiId(CommonUtils.objectToLong(objRegis.get("regisId")));
		emailObj.setEmpId(CommonUtils.objectToInt(objRegis.get("empId")));
		emailObj.setDeptId(CommonUtils.objectToInt(objRegis.get("deptId")));
		emailObj.setEmailId(CommonUtils.objectToString(objRegis.get("emailId")));
		emailObj.setEmpName(CommonUtils.objectToString(objRegis.get("empName")));
		emailObj.setTrainerEmailId(CommonUtils.objectToString(objRegis.get("trainerEmailId")));
		emailObj.setTrainerName(CommonUtils.objectToString(objRegis.get("trainerName")));
		emailObj.setWorkstation(CommonUtils.objectToString(objRegis.get("workstation")));
		emailObj.setLineName(CommonUtils.objectToString(objRegis.get("line")));
		emailObj.setDeptName(CommonUtils.objectToString(objRegis.get("deptName")));
		emailObj.setSkillLevel(CommonUtils.objectToString(objRegis.get("level")));

		emailObj.setLineId(CommonUtils.objectToLong(objRegis.get("lineId")));
		emailObj.setWorkstationId(CommonUtils.objectToLong(objRegis.get("workStationId")));
		emailObj.setDesiredSkillLevelId(CommonUtils.objectToLong(objRegis.get("desiredLvlId")));
		emailObj.setCurrentSkillLevelId(CommonUtils.objectToLong(objRegis.get("currentLvlId")));
	}

	private List<Tuple> getOJTPlanList(Session session, int branchId, String startDate) {
		logger.info("Inside getOJTPlanList | branchId: {} ", branchId);
		return session
				.createNativeQuery("select op.id as planId,op.month_value as monthValue ,op.year_value as yearValue,"
						+ " op.dept_id as deptId,op.status as status,op.start_date as start_date from sm_ojt_plan op "
						+ " where op.branch_id=:branchId and op.status=:publish and op.scheduler_status<>'PUBLISHED' and :startDate >= op.start_date ",
						Tuple.class)
				.setParameter("publish", SMConstant.PUBLISH).setParameter("startDate", startDate)
				.setParameter("branchId", branchId).getResultList();
	}

	private List<Tuple> getRegisList(Session session, int branchId, long planId) {
		return session.createNativeQuery(
				" select ojt.id as regisId,ojt.status as status,ojt.current_skill_level_id as currentLvlId,\r\n"
						+ " ojt.desired_skill_level_id as desiredLvlId,ojt.emp_id as empId,ojt.trainer_emp_id as trainerId,\r\n"
						+ " ojt.workstation_id as workStationId,ojt.branch_id as branchId,ojt.dept_id as deptId,\r\n"
						+ " ojt.ojt_plan_id as planId,concat(ifnull(e.first_name,''),' ',ifnull(e.last_name,'')) as empName,\r\n"
						+ " e.email_id as emailId,ojt.line_id as lineId,l.name as line,sw.workstation as workstation,"
						+ " d.dept_name as deptName,sl.level_name as level ,concat(ifnull(et.first_name,''),' ',ifnull(et.last_name,'')) as trainerName,\r\n"
						+ " et.email_id as trainerEmailId \r\n" + " from sm_ojt_regis ojt \r\n"
						+ " left join tbl_employee_details e on e.emp_id=ojt.emp_id\r\n"
						+ " left join sm_workstations sw on sw.id=ojt.workstation_id \r\n"
						+ " left join dwm_line l on l.id=ojt.line_id "
						+ " left join master_department d on d.dept_id=ojt.dept_id "
						+ " left join sm_skill_level sl on sl.id=ojt.desired_skill_level_id "
						+ " left join tbl_employee_details et on et.emp_id=ojt.trainer_emp_id  \r\n"
						+ " where ojt.ojt_plan_id=:planId and ojt.branch_id=:branchId group by ojt.id ",
				Tuple.class).setParameter("planId", planId).setParameter("branchId", branchId).getResultList();
	}

}