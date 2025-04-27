package com.greentin.enovation.skillmatrix;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import javax.persistence.Query;
import javax.persistence.Tuple;
import javax.persistence.TypedQuery;

import com.greentin.enovation.model.skillMatrix.*;
import com.greentin.enovation.model.skillMatrix.SMWorkstationMapping;
import org.hibernate.Session;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.env.Environment;
import org.springframework.core.task.TaskExecutor;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import com.greentin.enovation.audit.SkillMatrixAudit;
import com.greentin.enovation.communication.ICommunication;
import com.greentin.enovation.config.EnovationConfig;
import com.greentin.enovation.dto.MailDTO;
import com.greentin.enovation.dto.SMAssessmentDTO;
import com.greentin.enovation.dto.SMAssessmetDTO;
import com.greentin.enovation.dto.SMOJTCSPointsAuditDTO;
import com.greentin.enovation.dto.SMOJTChecksheetDTO;
import com.greentin.enovation.dto.SMOJTQuetionDTO;
import com.greentin.enovation.dto.SMOJTPlanDTO;
import com.greentin.enovation.dto.SMOJTSkillingDTO;
import com.greentin.enovation.dto.SMOJTSkillingParameterDTO;
import com.greentin.enovation.dto.SkillMatrixRequest;
import com.greentin.enovation.model.BranchMaster;
import com.greentin.enovation.model.DepartmentMaster;
import com.greentin.enovation.model.EmailTemplateMaster;
import com.greentin.enovation.model.EmployeeDetails;
import com.greentin.enovation.model.dwm.Line;
import com.greentin.enovation.utils.CommonUtils;
import com.greentin.enovation.utils.EnovationConstants;
import com.greentin.enovation.utils.EnovationException;
import com.greentin.enovation.utils.MailUtil;

@Component
public class SkillMatrixWorker {

	@Autowired
	SkillMatrixUtils smUtils;

	@Autowired
	SkillMatrixAudit smAudit;

	@Autowired
	Environment env;

	@Autowired
	EnovationConfig evonationConfig;

	@Autowired
	@Qualifier("taskExecutor")
	private TaskExecutor taskExecutor;

	@Autowired
	private ICommunication communication;

	private static final Logger LOGGER = LoggerFactory.getLogger(SkillMatrixWorker.class);

	public void saveOJTSkillingDetails(Session session, SMOJTRegis regis) {
		LOGGER.info("# SkillMatrixWorker || saveOjtRegistration");

		long skillingId = saveOJTSkilling(session, regis);

		SkillMatrixRequest request = new SkillMatrixRequest();
		request.setSkillingId(skillingId);
		request.setEmpId(regis.getEmpDetails().getEmpId());

		List<SMOJTChecksheetDTO> smChecksheetList = smUtils.getPendingSkillingChecksheet(session,
				request.getSkillingId());
		int date = 0;
		for (SMOJTChecksheetDTO checksheet : smChecksheetList) {
			LocalDate toDate;
			LocalDate startDate = LocalDate.now();
			toDate = startDate.plusDays(date);
			request.setStartDate(toDate.toString());
			LOGGER.info("smChecksheetId = {}", checksheet.getId());

			request.setSkillingChecksheetId(checksheet.getId());
			if (regis.getId() > 0) {
				request.setOjtRegiId(regis.getId());
			}

			assignToStage1(session, request);
			date++;
		}
	}

	public long saveOJTSkilling(Session session, SMOJTRegis request) {
		LOGGER.info("# SkillMatrixWorker || saveOJTSkilling");

		SMOJTSkilling sklilling = new SMOJTSkilling();

		// fetch checksheetdetail
		SMChecksheet checksheet = smUtils.getChecksheetDetails(session, request);
		sklilling.setOjtRegis(new SMOJTRegis(request.getId()));
		sklilling.setChecksheet(checksheet);
		sklilling.setStatus(EnovationConstants.PENDING_STRING);
		if (request.getCreatedBy() > 0) {
			sklilling.setCreatedBy(request.getCreatedBy());
		}
		session.save(sklilling);

		// save Skilling Checksheet Details
		saveSkillingChecksheet(session, sklilling.getId(), checksheet);

		return sklilling.getId();
	}

	public void saveSkillingChecksheet(Session session, long skillingId, SMChecksheet checksheet) {
		LOGGER.info("# SkillMatrixWorker || saveSkillingChecksheet || checksheet.getId() " + checksheet.getId());
		List<SMChecksheetPoints> pointList = smUtils.getChecksheetPointList(session, checksheet.getId());
		for (int dayNo = 1; dayNo <= checksheet.getNoOfDays(); dayNo++) {
			SMOJTSkillingChecksheet ojtChecksheet = new SMOJTSkillingChecksheet();
			ojtChecksheet.setSkilling(new SMOJTSkilling(skillingId));
			ojtChecksheet.setDayNo(dayNo);
			ojtChecksheet.setStatus(EnovationConstants.PENDING_STRING);
			session.save(ojtChecksheet);
			List<SMChecksheetPoints> dayPointList = smUtils.getChecksheetPointListDayWise(pointList, dayNo);
			saveSkillingChecksheetPoint(session, ojtChecksheet.getId(), dayPointList);
		}
	}

	//
	public void saveSkillingChecksheetPoint(Session session, long skillingChecksheetId,
			List<SMChecksheetPoints> dayPointList) {
		LOGGER.info("# SkillMatrixWorker || saveSkillingChecksheetPoint");
		for (SMChecksheetPoints point : dayPointList) {
			SMOJTChecksheetPoints pointObj = new SMOJTChecksheetPoints();
			pointObj.setSkillingChecksheet(new SMOJTSkillingChecksheet(skillingChecksheetId));
			pointObj.setChecksheetPoints(new SMChecksheetPoints(point.getId()));
			pointObj.setStatus(EnovationConstants.PENDING_STRING);
			session.save(pointObj);
		}
	}

	public void saveOJTSkillingAudit(Session session, SkillMatrixRequest request) {
		LOGGER.info("# SkillMatrixWorker || saveOJTSkillingAudit" + request.getEmpId());

		SMOJTSkillingAudit skillingAudit = new SMOJTSkillingAudit();
		skillingAudit.setEmpDetails(new EmployeeDetails(request.getEmpId()));
		skillingAudit.setSkilling(new SMOJTSkilling(request.getSkillingId()));
		skillingAudit.setStage(new SMStage(request.getStageId()));
		skillingAudit.setStatus(EnovationConstants.PENDING_STRING);
		skillingAudit.setPreviousAuditId(request.getSkillingAuditId());
		if (request.getSkillingChecksheetId() > 0) {
			skillingAudit.setSkillingChecksheet(new SMOJTSkillingChecksheet(request.getSkillingChecksheetId()));
		}
		if (request.getOjtRegiId() > 0) {
			skillingAudit.setOjtRegis(new SMOJTRegis(request.getOjtRegiId()));
		}
		skillingAudit.setStartDate(smUtils.convertStringToTimestamp(request.getStartDate() + " 00:00:00.0 "));
		LOGGER.info("# StartDate: {}", smUtils.convertStringToTimestamp(request.getStartDate() + " 00:00:00.0 "));
		session.save(skillingAudit);
		request.setSkillingAuditId(skillingAudit.getId());
	}

	private void saveOJTSKillingPointAudit(Session session, long skillingAuditId, long ojtPointId, long checkPointId) {
		LOGGER.info("# SkillMatrixWorker || saveOJTSKillingPointAudit");
		SMOJTChecksheetPointsAudit pointAudit = new SMOJTChecksheetPointsAudit();
		pointAudit.setSkillingAudit(new SMOJTSkillingAudit(skillingAuditId));
		pointAudit.setOjtPoint(new SMOJTChecksheetPoints(ojtPointId));
		pointAudit.setChecksheetPoints(new SMChecksheetPoints(checkPointId));
		pointAudit.setStatus(EnovationConstants.PENDING_STRING);
		session.save(pointAudit);
	}

	private void saveOJTSKillingParameterAudit(Session session, long skillingAuditId, SMChecksheetParameter param) {
		LOGGER.info("# SkillMatrixWorker || saveOJTSKillingParameterAudit");
		SMOJTSkillingParameter paramAudit = new SMOJTSkillingParameter();
		paramAudit.setSkillingAudit(new SMOJTSkillingAudit(skillingAuditId));
		paramAudit.setChecksheetParameter(new SMChecksheetParameter(param.getId()));
		session.save(paramAudit);

//		if (param.getParameterType() != null && param.getParameterType().getId() == SMConstant.CYCLE_PLAN) {
//			for (int cycleNo = 1; cycleNo <= param.getCycleValue(); cycleNo++) {
//				saveOJTCyclePlanParameter(session, skillingAuditId, param.getId(), paramAudit.getId(), cycleNo);
//			}
//		}
	}

	private void saveOJTCyclePlanParameter(Session session, SMOJTPlanDTO obj, SkillMatrixRequest request) {
		LOGGER.info("# SkillMatrixWorker || saveOJTCyclePlanParameter");
		SMOJTCyclePlanParameter cyclePlan = new SMOJTCyclePlanParameter();

		if (request.getSkillingAuditId() > 0) {
			cyclePlan.setSkillingAudit(new SMOJTSkillingAudit(request.getSkillingAuditId()));
		}
		if (request.getSkillingAuditId() > 0) {
			cyclePlan.setChecksheetParameter(new SMChecksheetParameter(obj.getChecksheetParameterId()));
		}
		if (request.getSkillingAuditId() > 0) {
			cyclePlan.setSkillingParameter(new SMOJTSkillingParameter(obj.getSkillingParameterId()));
		}
		cyclePlan.setCycleNo(obj.getCycleNo());
		cyclePlan.setExpectedValue(obj.getExpectedValue());
		cyclePlan.setActualValue(obj.getActualValue());
		session.save(cyclePlan);
	}

	public void updateSkillingAssessmentPending(Session session, long skillingId, long assessmentId,
			Timestamp assessmentDueDate) {
		LOGGER.info("# SkillMatrixWorker || updateSkillingAssessmentPending");

		session.createNativeQuery(
				" update sm_ojt_skilling set assessment_due_date=:assessmentDueDate ,status=:status,assessment_id=:assessmentId where id=:skillingId ")
				.setParameter("assessmentDueDate", assessmentDueDate).setParameter("skillingId", skillingId)
				.setParameter("status", SMConstant.ASSESSMENT_PENDING).setParameter("assessmentId", assessmentId)
				.executeUpdate();

	}

	public void updateSkillingAudit(Session session, SkillMatrixRequest request) {
		LOGGER.info("# SkillMatrixWorker || updateSkillingAudit");

		session.createNativeQuery(
				" update sm_ojt_skilling_audit set updated_date=now(), comment=:comment ,status=:status where id=:auditId ")
				.setParameter("comment", request.getComment())
				.setParameter(EnovationConstants.AUDIT_ID, request.getSkillingAuditId())
				.setParameter("status", request.getStatus()).executeUpdate();

	}

	public void updateSkillingChecksheetDay(Session session, SkillMatrixRequest request) {
		LOGGER.info("# SkillMatrixWorker || updateSkillingChecksheetDay");

		session.createNativeQuery(
				" update sm_ojt_skilling_checksheet set status=:status where skilling_id=:skillingId and day_no=:dayNo ")
				.setParameter("dayNo", request.getDayNo()).setParameter("skillingId", request.getSkillingId())
				.setParameter("status", request.getStatus()).executeUpdate();

	}

	public void updateSkillingPointAudit(Session session, SkillMatrixRequest request) {
		LOGGER.info("# SkillMatrixWorker || updatePointAuditEntry ");
		if (!CollectionUtils.isEmpty(request.getCsPointAuditList())) {
			for (SMOJTCSPointsAuditDTO obj : request.getCsPointAuditList()) {
				session.createNativeQuery(
						" update sm_ojt_checksheet_points_audit set comment=:comment ,status=:status where skilling_audit_id=:auditId and id=:id")

						.setParameter("comment", request.getComment()).setParameter("id", obj.getId())
						.setParameter(EnovationConstants.AUDIT_ID, request.getSkillingAuditId())
						.setParameter("status", obj.getStatus()).executeUpdate();
			}
		}
	}

	public void updateOJTSkillingParameter(Session session, SkillMatrixRequest request) {
		LOGGER.info("# SkillMatrixWorker || updateOJTSkillingParameter ");
		if (!CollectionUtils.isEmpty(request.getSkillingParamList())) {
			for (SMOJTSkillingParameterDTO obj : request.getSkillingParamList()) {
				LOGGER.info("# SkillMatrixWorker || obj.getId {}", obj.getId());
				session.createNativeQuery(
						" update sm_ojt_skilling_parameter set parameter_value=:paramValue  where skilling_audit_id=:auditId and id=:id")
						.setParameter("paramValue", obj.getParameterValue()).setParameter("id", obj.getId())
						.setParameter(EnovationConstants.AUDIT_ID, obj.getSkillingAuditId()).executeUpdate();

//				updateOJTCyclePlanParam(session, obj);
				if (!CollectionUtils.isEmpty(obj.getCyclePlanList())) {
					for (SMOJTPlanDTO cycle : obj.getCyclePlanList()) {
						saveOJTCyclePlanParameter(session, cycle, request);
					}
				}
			}
		}
	}

	private void updateOJTCyclePlanParam(Session session, SMOJTSkillingParameterDTO obj) {
		LOGGER.info("# SkillMatrixWorker | updateOJTCyclePlanParam ");
		if (!CollectionUtils.isEmpty(obj.getCyclePlanList())) {
			for (SMOJTPlanDTO cycle : obj.getCyclePlanList()) {
				LOGGER.info("# SkillMatrixWorker || cycle.getCyclePlanId {}", cycle.getCyclePlanId());
				session.createNativeQuery(
						" update sm_ojt_cyclePlan_parameter set expected_value=:expectedValue,actual_value=:actualValue where id=:id")
						.setParameter("expectedValue", cycle.getExpectedValue())
						.setParameter("actualValue", cycle.getActualValue()).setParameter("id", cycle.getCyclePlanId())
						.executeUpdate();
			}
		}
	}

	public void assignNextDayPointsToStage1(Session session, SkillMatrixRequest request) {
		LOGGER.info("# SkillMatrixWorker || assignNextDayPointsToStage1");

	}

	public void assignToStage1(Session session, SkillMatrixRequest request) {
		LOGGER.info("# SkillMatrixWorker || assignToStage1");

		request.setStageId(SMConstant.STAGE1_ID);
		saveOJTSkillingAudit(session, request);

		List<SMOJTChecksheetPoints> skillingPointList = smUtils.getSkillingChecksheetPoint(session,
				request.getSkillingChecksheetId());
		if (!CollectionUtils.isEmpty(skillingPointList)) {
			for (SMOJTChecksheetPoints point : skillingPointList) {
				saveOJTSKillingPointAudit(session, request.getSkillingAuditId(), point.getId(),
						point.getChecksheetPoints().getId());
			}
		} else {
			LOGGER.info("# SkillMatrixWorker || assignToStage1");
			throw new EnovationException(SMConstant.CHECKSHEET_POINT_NOT_FOUND);
		}
	}

	private int getNextStageAuthority(Session session, SkillMatrixRequest request) {
		setUserType(request);

//		setUserType(request);
		LOGGER.info("# UserType: {}", request.getUserType());
		StringBuilder sb = new StringBuilder(
				"select ifnull(su.emp_id,0) as empId from sm_master_user_type st inner join sm_user_type su on su.user_type_id=st.id \r\n"
						+ " where user_type=:userType and su.branch_id=:branchId and su.is_active=1 ");

		if (request.getDeptId() > 0) {
			sb.append(" and su.dept_id=:deptId ");
		}
		if (request.getLineId() > 0) {
			sb.append(" and su.line_id=:lineId ");
		}
		sb.append(" limit 1 ");

		TypedQuery<Tuple> query = session.createNativeQuery(sb.toString(), Tuple.class)
				.setParameter("userType", request.getUserType()).setParameter("branchId", request.getBranchId());

		if (request.getDeptId() > 0) {
			query.setParameter("deptId", request.getDeptId());
		}
		if (request.getLineId() > 0) {
			query.setParameter("lineId", request.getLineId());
		}

		int empId = 0;
		Tuple tupleList = query.getResultList().stream().findFirst().orElse(null);
		if (tupleList != null) {
			empId = CommonUtils.objectToInt(tupleList.get("empId"));

		}
		if (empId > 0) {
			return empId;
		} else {
			throw new EnovationException(request.getUserType() + " setup not found");
		}
	}

//		int empId = ((Number) session.createNativeQuery(
//				"select ifnull(su.emp_id,0) as empId from sm_master_user_type st inner join sm_user_type su on su.user_type_id=st.id \r\n"
//						+ "where user_type_caption=:userType and su.branch_id=:branchId and su.is_active=1 limit 1")
//				.setParameter("userType", request.getUserType()).setParameter("branchId", request.getBranchId())
//				.getSingleResult()).intValue();
//		if (empId > 0) {
//			return empId;
//		} else {
//			throw new EnovationException(request.getUserType() + " setup not found");
//		}
//	}

	/**
	 * 
	 * @param session
	 * @param request, ojtRegisId,skillingId,skillingAuditId,branchId,deptId
	 * 
	 */
	public void assignToStage2(Session session, SkillMatrixRequest request) {
		LOGGER.info("# SkillMatrixWorker || assignToStage2");
		// fetch stage2 emp id
		List<MailDTO> mailList = new ArrayList<>();
		request.setStageId(SMConstant.STAGE2_ID);
//		request.setEmpId(getNextStageAuthority(session, request));
		request.setEmpId(request.getTrainerEmpId());
		SkillMatrixRequest emailData = smUtils.getStageWiseDataForMail(session, request);
		saveOJTSkillingAudit(session, request);
		// saveOJTSKillingPointAudit
		if (!CollectionUtils.isEmpty(request.getCsPointAuditList())) {
			for (SMOJTCSPointsAuditDTO point : request.getCsPointAuditList()) {
				saveOJTSKillingPointAudit(session, request.getSkillingAuditId(), point.getOjtPointId(),
						point.getChecksheetPointId());
			}
		}

		sendEmailToStakeholder(mailList, emailData, request);

	}

	// use this Method for Assessment Status update to OE only
	public SkillMatrixRequest getEmployeeDetailsForEmail(Session session, SkillMatrixRequest request) {
		LOGGER.info("# SkillMatrixWorker || getEmployeeDetailsForEmail");
		TypedQuery<Tuple> query = session.createNativeQuery(
				"SELECT line.name AS lineName,md.dept_name AS deptName,smw.workstation AS workstation,now() AS assignedDate,\r\n"
						+ "concat(ifnull(ed.first_name,''),' ',ifnull(ed.last_name,'')) AS oeName, sl.level_name as level,\r\n"
						+ "ed.email_id as emailId,o.portal_link as portalLink,o.logo as logo,\r\n"
						+ "concat(ifnull(e.first_name,''),' ',ifnull(e.last_name,'')) AS createdBy,e.email_id as createdByEmail\r\n"
						+ " from sm_ojt_skilling as skilling \r\n"
						+ "Inner join sm_ojt_regis as regi ON skilling.ojt_regis_id=regi.id \r\n"
						+ "INNER JOIN sm_workstations as smw ON smw.id=regi.workstation_id\r\n"
						+ "left JOIN master_department as md ON md.dept_id=regi.dept_id\r\n"
						+ "left JOIN dwm_line as line ON line.id=regi.line_id\r\n"
						+ "LEFT JOIN tbl_employee_details as ed ON ed.emp_id=regi.emp_id\r\n"
						+ "left join master_branch as mb on mb.branch_id=regi.branch_id\r\n"
						+ "left join master_organization as o on o.org_id=mb.org_id\r\n"
						+ "left join sm_skill_level as sl on sl.id =regi.desired_skill_level_id\r\n"
						+ "LEFT JOIN tbl_employee_details as e ON e.emp_id=regi.created_by \r\n"
						+ "WHERE skilling.id=:skillingId",
				Tuple.class).setParameter("skillingId", request.getSkillingId());

		List<Tuple> objList = query.getResultList();
		if (!CollectionUtils.isEmpty(objList)) {
			Tuple obj = objList.get(0);

			request.setEmailId(CommonUtils.objectToString(obj.get("emailId")));
			request.setLineName(CommonUtils.objectToString(obj.get("lineName")));
			request.setWorkstation(CommonUtils.objectToString(obj.get("workstation")));
			request.setAssignedDate(CommonUtils.objectToString(obj.get("assignedDate")));
			request.setPortalLink(CommonUtils.objectToString(obj.get("portalLink")));
			request.setLogo(CommonUtils.objectToString(obj.get("logo")));
			request.setSkillLevel(CommonUtils.objectToString(obj.get("level")));
			request.setEmpName(CommonUtils.objectToString(obj.get("oeName")));
			request.setAssignedBy(CommonUtils.objectToString(obj.get("createdBy")));
			request.setAssignedByEmail(CommonUtils.objectToString(obj.get("createdByEmail")));
		}
		return request;
	}

	private void sendEmailToStakeholder(List<MailDTO> mailList, SkillMatrixRequest emailData,
			SkillMatrixRequest request) {
		LOGGER.info("#In SkillMatrixWorker |  INSIDE in sendEmailToStakeholder ");
		if (emailData.getEmailId() != null && emailData.getEmailId().length() > 0) {
			LocalDate date = LocalDate.now();
			EmailTemplateMaster messageContent = null;
			messageContent = evonationConfig.getMessageContent(EnovationConstants.SKILLMATRIX_EMAIL_TEMPLATE);

			if (messageContent != null) {
				LOGGER.info("#messageContent added");

				String subject = messageContent.getSubject().replaceAll(Pattern.quote("{subject}"),
						CommonUtils.objectToString("Reminder notification"));
				String emailCon = env.getProperty("ASSIGN_TO_NEXT_STAGE_EMAIL_Body.body");

				String content = messageContent.getBody().replaceAll(Pattern.quote("{body}"), emailCon)
						.replaceAll(Pattern.quote("{AssignedBy}"),
								CommonUtils.objectToString(emailData.getAssignedBy()))
						.replaceAll(Pattern.quote("{name}"), CommonUtils.objectToString(emailData.getAssignedTo()))
						.replaceAll(Pattern.quote("{subject}"), CommonUtils.objectToString("Reminder notification"))
						.replaceAll(Pattern.quote("{portalLink}"),
								CommonUtils.objectToString(emailData.getPortalLink()))
						.replaceAll(Pattern.quote("{thisYear}"), (String.valueOf(LocalDate.now().getYear())))
						.replaceAll(Pattern.quote("{cell}"), CommonUtils.objectToString(emailData.getLineName()))
						.replaceAll(Pattern.quote("{workstation}"), // paste workstation from emailData
								CommonUtils.objectToString(emailData.getWorkstation()))
						.replaceAll(Pattern.quote("{assessmentDate}"), CommonUtils.objectToString(date))
						.replaceAll(Pattern.quote("{status}"), emailData.getStatus())
						.replaceAll(Pattern.quote("{companyLogo}"),
								(emailData.getLogo() != null && emailData.getLogo().length() > 0)
										? EnovationConfig.buddyConfig.get("ProfilePicPathUrl") + emailData.getLogo()
										: EnovationConstants.myeNovationLogoPath);

				MailDTO mail = new MailDTO(emailData.getEmailId(), subject, content);
				mailList.add(mail);
				if (!CollectionUtils.isEmpty(mailList)) {
					taskExecutor.execute(new MailUtil(mailList, communication));
				}
			}
		}

	}

	public void assignToStage2Verification(Session session, SkillMatrixRequest request) {
		LOGGER.info("# SkillMatrixWorker || assignToStage2Verification");

		List<MailDTO> mailList = new ArrayList<>();
		request.setStageId(SMConstant.STAGE2_VERIFICATION_ID);
//		request.setEmpId(getNextStageAuthority(session, request));
		request.setEmpId(request.getTrainerEmpId());
		SkillMatrixRequest emailData = smUtils.getStageWiseDataForMail(session, request);

		saveOJTSkillingAudit(session, request);
		if (!CollectionUtils.isEmpty(request.getCsPointAuditList())) {
			for (SMOJTCSPointsAuditDTO point : request.getCsPointAuditList()) {
				saveOJTSKillingPointAudit(session, request.getSkillingAuditId(), point.getOjtPointId(),
						point.getChecksheetPointId());
			}
		}
		List<SMChecksheetParameter> paramList = smUtils.getChecksheetParameterList(session, request.getCheckSheetId());

		if (!CollectionUtils.isEmpty(paramList)) {
			for (SMChecksheetParameter param : paramList) {
				saveOJTSKillingParameterAudit(session, request.getSkillingAuditId(), param);
			}
		}
		sendEmailToStakeholder(mailList, emailData, request);
	}

	public void assignToStage3(Session session, SkillMatrixRequest request) {
		LOGGER.info("# SkillMatrixWorker || assignToStage3");
		List<MailDTO> mailList = new ArrayList<>();
		request.setStageId(SMConstant.STAGE3_ID);
		request.setEmpId(getNextStageAuthority(session, request));
		SkillMatrixRequest emailData = smUtils.getStageWiseDataForMail(session, request);
		saveOJTSkillingAudit(session, request);
		for (SMOJTCSPointsAuditDTO point : request.getCsPointAuditList()) {
			saveOJTSKillingPointAudit(session, request.getSkillingAuditId(), point.getOjtPointId(),
					point.getChecksheetPointId());
		}
		sendEmailToStakeholder(mailList, emailData, request);
	}

	public void assignToStage4(Session session, SkillMatrixRequest request) {
		LOGGER.info("# SkillMatrixWorker || saveOJTSkilling");
		List<MailDTO> mailList = new ArrayList<>();

		request.setStageId(SMConstant.STAGE4_ID);
//		request.setEmpId(getNextStageAuthority(session, request));
		request.setEmpId(request.getTlEmpId());
		SkillMatrixRequest emailData = smUtils.getStageWiseDataForMail(session, request);
		saveOJTSkillingAudit(session, request);
		sendEmailToStakeholder(mailList, emailData, request);

	}

	public void setSMAssessmentParam(SkillMatrixRequest request, Session session, SMAssessment obj) {
		LOGGER.info("#In SkillMatrixWorker |  INSIDE in setSMAssessmentParam ");

		if (smUtils.assessmentAlreadyExist(session, request)) {

			if (request.getTitle() != null) {
				obj.setTitle(request.getTitle());
			}
			if (request.getBranchId() > 0) {
				obj.setBranch(new BranchMaster(request.getBranchId()));
			}
			if (request.getSkillLvlId() > 0) {
				obj.setSkillLevel(new SMSkillLevel(request.getSkillLvlId()));
			}
			if (request.getTime() > 0) {
				obj.setTime(request.getTime());
			}
			if (request.getPassingMark() > 0) {
				obj.setPassingMarks(request.getPassingMark());
			}
			if (request.getDeptId() > 0) {
				obj.setDept(new DepartmentMaster(request.getDeptId()));
			}
			if (request.getLineId() > 0) {
				obj.setLine(new Line(request.getLineId()));
			}
			if (request.getWorkstationId() > 0) {
				obj.setWorkstation(new SMWorkstations(request.getWorkstationId()));
			}
			if (request.getAssessmentType() != null && request.getAssessmentType().equals(EnovationConstants.SAFETY)) {
				if (smUtils.assessmentTypeAlreadyExist(session, request)) {
					if (request.getAssessmentType() != null) {
						obj.setAssessmentType(request.getAssessmentType());
					}
				} else {
					throw new EnovationException("Safety Assessment  already exit");

				}
			} else {
				if (request.getAssessmentType() != null) {
					obj.setAssessmentType(request.getAssessmentType());
				}

			}

		} else {
			throw new EnovationException("Assessment already exit");
		}

	}

	// Added by Sonali L. Jan 15 2024 - added field assessmentType
	public void groupingQueOptList(SMAssessmentDTO data, Map<Long, List<SkillMatrixRequest>> groupedByQuetionId,
			Session session) {

		LOGGER.info("#In SkillMatrixWorker |  INSIDE in groupingQueOptList ");
		for (Entry<Long, List<SkillMatrixRequest>> obj : groupedByQuetionId.entrySet()) {
			LOGGER.info("#In SkillMatrixWorker |  for : INSIDE in groupingQueOptList ");
			List<SMAssessmentDTO> quetionlist = new ArrayList<>();
			data.setBranchId(obj.getValue().get(0).getBranchId());
			data.setCreatedBy(obj.getValue().get(0).getCreatedBy());
			data.setTotalMarks(obj.getValue().get(0).getTotalMarks());
			data.setPassingMark(obj.getValue().get(0).getPassingMark());
			data.setBranchName(obj.getValue().get(0).getBranchName());
			data.setAssessmentId(obj.getValue().get(0).getAssessmentId());
			data.setTime(obj.getValue().get(0).getTime());
			data.setTitle(obj.getValue().get(0).getTitle());
			data.setSkillLvlId(obj.getValue().get(0).getSkillLvlId());
			data.setSkillLevel(obj.getValue().get(0).getSkillLevel());
			data.setStatus(obj.getValue().get(0).getStatus());
			data.setCategoryId(obj.getValue().get(0).getCategoryId());
			data.setCategoryName(obj.getValue().get(0).getCategoryName());
			data.setDeptId(obj.getValue().get(0).getDeptId());
			data.setDeptName(obj.getValue().get(0).getDeptName());
			data.setLineId(obj.getValue().get(0).getLineId());
			data.setLineName(obj.getValue().get(0).getLineName());
			data.setWorkstation(obj.getValue().get(0).getWorkstation());
			data.setWorkstationId(obj.getValue().get(0).getWorkstationId());
			data.setAssessmentType(obj.getValue().get(0).getAssessmentType());

			if (!CollectionUtils.isEmpty(groupedByQuetionId)) {
				setQuetion(data, groupedByQuetionId, quetionlist, session);
			}
		}
	}

	private void setQuetion(SMAssessmentDTO data2, Map<Long, List<SkillMatrixRequest>> groupedByQuetionId,
			List<SMAssessmentDTO> quetionlist, Session session) {
		LOGGER.info("# SkillMatrixWorker || setQuetion ");
		for (Entry<Long, List<SkillMatrixRequest>> que : groupedByQuetionId.entrySet()) {
			List<SMAssessmentDTO> optionlist = new ArrayList<>();
			SMAssessmentDTO queObj = new SMAssessmentDTO();
			if ((que.getValue().get(0).getAssessmentQueId()) > 0) {
				LOGGER.info("# SkillMatrixWorker || getAssessmentQueId ");
				queObj.setQuetionId(que.getValue().get(0).getAssessmentQueId());
				queObj.setCategoryId(que.getValue().get(0).getCategoryId());
				queObj.setCategoryName(que.getValue().get(0).getCategoryName());
				queObj.setQuestion(que.getValue().get(0).getQuestion());
				queObj.setQuestionMark(que.getValue().get(0).getQueMark());
				queObj.setAssessmentType(que.getValue().get(0).getAssessmentType());

				List<Tuple> selectedOpt = session
						.createNativeQuery("select id as id,opt_id as optId,ques_id as quesId "
								+ " from sm_ojt_assessment_opt where ques_id=:queId ".toString(), Tuple.class)
						.setParameter("queId", queObj.getQuetionId()).getResultList();
//				long potId =0;
//				if (selectedOpt != null) {
//					potId = CommonUtils.objectToLong(selectedOpt.get("optId"));
//				}

				setOption(data2, quetionlist, que, optionlist, queObj, selectedOpt);
			}
		}
	}

	private void setOption(SMAssessmentDTO data2, List<SMAssessmentDTO> quetionlist,
			Entry<Long, List<SkillMatrixRequest>> que, List<SMAssessmentDTO> optionlist, SMAssessmentDTO queObj,
			List<Tuple> selectedOpt) {
		LOGGER.info("# SkillMatrixWorker || setOption ");
//		Map<Long, List<SkillMatrixRequest>> groupedByOptId = que.getValue().stream()
//				.collect(Collectors.groupingBy(SkillMatrixRequest::getAssessmentQueOptId));
		Map<Long, List<SkillMatrixRequest>> groupedByOptId = que.getValue().stream()
				.collect(Collectors.groupingBy(SkillMatrixRequest::getAssessmentQueOptId, LinkedHashMap::new, // Preserves
																												// insertion
																												// order
						Collectors.toList()));
		if (!CollectionUtils.isEmpty(groupedByOptId)) {
			addOptions(optionlist, groupedByOptId, selectedOpt);
			if (!CollectionUtils.isEmpty(optionlist)) {
				queObj.setOptList(optionlist);
			}
			if (queObj != null) {
				quetionlist.add(queObj);
			}
			if (!CollectionUtils.isEmpty(quetionlist)) {
				data2.setQuesList(quetionlist);
			}
		}
	}

	private void addOptions(List<SMAssessmentDTO> optionlist, Map<Long, List<SkillMatrixRequest>> groupedByOptId,
			List<Tuple> selectedOpt) {
		LOGGER.info("# SkillMatrixWorker || addOptions ");
		for (Entry<Long, List<SkillMatrixRequest>> opt : groupedByOptId.entrySet()) {
			SMAssessmentDTO optObj = new SMAssessmentDTO();

			if (opt.getValue().get(0).getAssessmentQueOptId() > 0) {

				optObj.setAssessmentQueOptId(opt.getValue().get(0).getAssessmentQueOptId());
				optObj.setOption(opt.getValue().get(0).getOption());
				optObj.setRightAns(opt.getValue().get(0).isRightAns());
				if (!CollectionUtils.isEmpty(selectedOpt)) {
					for (Tuple obj : selectedOpt) {
						if (optObj.getAssessmentQueOptId() == CommonUtils.objectToLong(obj.get("optId"))) {
							optObj.setSelected(true);
						}
					}
				}
				if (optObj != null) {
					optionlist.add(optObj);
				}
			}
		}

	}

	public void setUserType(SkillMatrixRequest request) {
		LOGGER.info("# SkillMatrixWorker || setUserType ");
		if (request != null) {
			if (request.getStageId() == SMConstant.STAGE1_ID) {
				request.setUserType(EnovationConstants.OE);
			} else if (request.getStageId() == SMConstant.STAGE2_ID) {
				request.setUserType(EnovationConstants.TRAINER);
			} else if (request.getStageId() == SMConstant.STAGE2_VERIFICATION_ID) {
				request.setUserType(EnovationConstants.TRAINER);
			} else if (request.getStageId() == SMConstant.STAGE3_ID) {
				request.setUserType(EnovationConstants.QA);
			} else if (request.getStageId() == SMConstant.STAGE4_ID) {
				request.setUserType(EnovationConstants.TL);
			} else {
				throw new EnovationException("StageId is not valid");
			}
		}
	}

	private List<Tuple> getPointAuditId(SkillMatrixRequest request, Session session, long auditId) {
		LOGGER.info("# SkillMatrixWorker || getPointAuditId ");
		StringBuilder sb = new StringBuilder(
				"select ojtcpa.id as pointAuditId,ojtsa.checksheet_points_id as checksheetPointId from sm_ojt_checksheet_points_audit ojtcpa\r\n"
						+ "inner join sm_ojt_checksheet_points ojtsc on ojtcp.id=ojtcpa.checksheet_points_id\r\n"
						+ "where ojtcpa.skilling_audit_id=:auditId and ojtcpa.status=:status");

		TypedQuery<Tuple> query = session.createNativeQuery(sb.toString(), Tuple.class).setParameter("auditId", auditId)
				.setParameter("status", EnovationConstants.PENDING_STRING);

		return query.getResultList();
	}

	public void setFilterSearchQueryForMyPending(StringBuilder sb, SkillMatrixRequest request) {
		LOGGER.info("# SkillMatrixWorker || setFilterSearchQueryForMyPending ");
	}

	public void setParameterInQuery(TypedQuery<Tuple> query, SkillMatrixRequest request) {
		LOGGER.info("# SkillMatrixWorker || setParameterInQuery ");

	}

	public void updateAssessmentQue(SkillMatrixRequest request, Session session, SMAssessmentQues obj) {
		LOGGER.info("# SkillMatrixWorker || updateAssessmentQue ");
		if (request.getAssessmentId() > 0) {
			obj.setAssessment(new SMAssessment(request.getAssessmentId()));
		}
		if (request.getQuestionTypeId() > 0) {
			obj.setQuesType(new SMQuestionType(request.getQuestionTypeId()));
		}

		if (request.getQuestion() != null) {
			if (smUtils.isQueNotExist(session, request)) {
				obj.setQestion(request.getQuestion());
			} else {
				throw new EnovationException("Question is already exist");
			}
		}
		if (request.getQuestionTypeId() > 0) {
			obj.setQuesType(new SMQuestionType(request.getQuestionTypeId()));
		}
		if (request.getQueMark() > 0) {
			obj.setQueMarks(request.getQueMark());
		}
		if (!CollectionUtils.isEmpty(request.getOptList())) {
			saveOptionList(request, session, obj);
		}
		if (request.getCategoryId() > 0) {
			obj.setSmcategory(new SMCategory(request.getCategoryId()));
		}
		session.update(obj);
	}

	public void saveOptionList(SkillMatrixRequest request, Session session, SMAssessmentQues obj) {
		LOGGER.info("# SkillMatrixWorker || saveOptionList ");
		request.getOptList().stream().forEach(x -> {
			if (x.getId() > 0) {
				updateAssessmentOptions(session, x);
			} else {
				SMAssessmentOptions optObj = new SMAssessmentOptions();
				optObj.setOption(x.getOption());
				optObj.setIsRightAns(x.getIsRightAns());
				optObj.setQues(obj);
				session.save(optObj);
			}
		});
	}

	private void updateAssessmentOptions(Session session, SMAssessmentOptions x) {
		LOGGER.info("# SkillMatrixWorker || updateAssessmentOptions ");
		SMAssessmentOptions opt = session.get(SMAssessmentOptions.class, x.getId());
		Tuple optObj = smUtils.getOptionDetails(session, x.getId());
		if (opt != null) {
			if (x.getOption() != null) {
				opt.setOption(x.getOption());
			}
			if (x.getIsRightAns() != null && !x.getIsRightAns()) {
				opt.setIsRightAns(false);
			} else if (x.getIsRightAns() != null) {
				opt.setIsRightAns(x.getIsRightAns());
			}
			session.update(opt);
			smAudit.updateSMAssessmentOptionsAudit(session, opt, optObj);
		}
	}

	public void setParametersInDto(List<Tuple> tupleList, List<SkillMatrixRequest> dtoList) {
		LOGGER.info("# SkillMatrixWorker | setDtoParameters");
		for (Tuple x : tupleList) {
			SkillMatrixRequest dto = new SkillMatrixRequest();
			dto.setStatus(CommonUtils.objectToString(x.get("status")));
			dtoList.add(dto);
		}
	}

	/**
	 * @author Rajdeep MD 25-Aug-2023 , 03:00 AM
	 * @desc get ojt Registration Details by registration Id
	 */
	public void getSetOJTRegistrationDetails(long oJTRegisId, Session session, Map<String, Object> data,
			Tuple tupleList) {
		LOGGER.info("# SkillMatrixWorker || getSetOJTRegistrationDetails ");
		data.put("ojtRegiId", CommonUtils.objectToLong(tupleList.get("ojtRegiId")));
		data.put("createdBy", CommonUtils.objectToInt(tupleList.get("createdBy")));
		data.put("oeEmpName", CommonUtils.objectToString(tupleList.get("oeEmpName")));
		data.put("currentSkillLvlId", CommonUtils.objectToLong(tupleList.get("currentSkillLvlId")));
		data.put("desiredSkillLvlId", CommonUtils.objectToLong(tupleList.get("desiredSkillLvlId")));
		data.put("oeEmpId", CommonUtils.objectToInt(tupleList.get("empIdOfOE")));
		data.put("trainerEmpId", CommonUtils.objectToInt(tupleList.get("trainerEmpId")));
		data.put("trainerName", CommonUtils.objectToString(tupleList.get("trainerName")));
		data.put("workStationId", CommonUtils.objectToLong(tupleList.get("workStationId")));
		data.put("workstation", CommonUtils.objectToString(tupleList.get("workstation")));
		data.put("requiredWorkforce", CommonUtils.objectToDouble(tupleList.get("requiredWorkforce")));
		data.put("deptId", CommonUtils.objectToInt(tupleList.get("deptId")));
		LOGGER.info("# tupleList not null");

		List<Tuple> skillList = smUtils.getSkillingList(session, oJTRegisId);
		if (!CollectionUtils.isEmpty(skillList)) {
			List<HashMap<String, Object>> skillingList = new ArrayList<>();

			for (Tuple skill : skillList) {

				HashMap<String, Object> skillObj = CommonUtils.parseSingleTupleRecord(skill);

				getSkillingCheekSheetList(session, skill, skillObj);

				List<HashMap<String, Object>> skillingAuditList = new ArrayList<>();

				getSkillingAudit(session, skillingList, skill, skillObj, skillingAuditList);
			}
			data.put("skillingList", skillingList);
		}
	}

	/**
	 * @author Rajdeep MD 25-Aug-2023 , 03:00 AM
	 * @desc get and set ojt Skilling Audit data by registration Id
	 */
	private void getSkillingAudit(Session session, List<HashMap<String, Object>> skillingList, Tuple skill,
			HashMap<String, Object> skillObj, List<HashMap<String, Object>> skillingAuditList) {
		LOGGER.info("# SkillMatrixWorker || getSetOJTRegistrationDetails ");
		List<Tuple> skillingAudit = smUtils.getSkillingAuditList(session,
				CommonUtils.objectToLong(skill.get("skillingId")));

		for (Tuple point : skillingAudit) {
			HashMap<String, Object> audit = CommonUtils.parseSingleTupleRecord(point);
			List<Tuple> pointAudit = smUtils.getcheckSheetPointAuditList(session,
					CommonUtils.objectToLong(point.get("auditId")));
			audit.put("pointAuditList", CommonUtils.parseTupleList(pointAudit));

			List<Tuple> paraList = smUtils.getParameterList(session, CommonUtils.objectToLong(point.get("auditId")));
			audit.put("parameterList", CommonUtils.parseTupleList(paraList));
			skillingAuditList.add(audit);
		}
		skillObj.put("skillingAuditList", skillingAuditList);
		skillingList.add(skillObj);
	}

	/**
	 * @author Rajdeep MD 25-Aug-2023 , 03:00 AM
	 * @desc get and set ojt Skilling CheekSheet details by skillingId
	 */
	private void getSkillingCheekSheetList(Session session, Tuple skill, HashMap<String, Object> skillObj) {
		LOGGER.info("# SkillMatrixWorker || getSkillingCheekSheetList ");
		List<Tuple> checkSheet = smUtils.getcheckSheetList(session, CommonUtils.objectToLong(skill.get("skillingId")));

		List<HashMap<String, Object>> checksheetList = new ArrayList<>();

		for (Tuple sheet : checkSheet) {
			HashMap<String, Object> checkSheetObj = CommonUtils.parseSingleTupleRecord(sheet);
			List<Tuple> sheetPoint = smUtils.getcheckSheetPointList(session,
					CommonUtils.objectToLong(sheet.get("ojtSkillingSheetId")));

			checkSheetObj.put("checksheetPointList", CommonUtils.parseTupleList(sheetPoint));

			checksheetList.add(checkSheetObj);
		}
		skillObj.put("skillingChecksheetList", checksheetList);
	}

	public void rejectedByStageFour(SkillMatrixRequest request, Session session) {
		LOGGER.info("# SkillMatrixWorker || rejectedByStageFour ");
		/*
		 * update Reject Status in Audit table
		 */

		smUtils.updateSkilling(request, session);

		updateSkillingAudit(session, request);

		smUtils.setOEEmpId(request, session);
		/*
		 * Reassign assign Current Day Task To OE
		 */

		List<SMOJTChecksheetDTO> smChecksheetList = smUtils.getPendingSkillingChecksheet(session,
				request.getSkillingId());

		for (SMOJTChecksheetDTO checksheet : smChecksheetList) {

			System.err.println("smChecksheetId = " + checksheet.getId());

			request.setSkillingChecksheetId(checksheet.getId());

			assignToStage1(session, request);
		}
	}

	public void saveOJTPlan(Session session, SMOJTPlanDTO regisObj, long planId, SkillMatrixRequest request) {
		LOGGER.info("# SkillMatrixWorker || saveOJTPlan");

		SMOJTRegis regis = new SMOJTRegis();

		regis.setEmpDetails(new EmployeeDetails(regisObj.getEmpId()));
		if (request.getCreatedBy() > 0) {
			regis.setCreatedBy(request.getCreatedBy());
		}
		if (regisObj.getTrainerEmpId() > 0) {
			regis.setTrainerDetails(new EmployeeDetails(regisObj.getTrainerEmpId()));
		}
		if (regisObj.getCurrentSkillLevelId() > 0) {
			regis.setCurrentSkillLevel(new SMSkillLevel(regisObj.getCurrentSkillLevelId()));
		}
		if (regisObj.getDesiredSkillLevelId() > 0) {
			regis.setDesiredSkillLevel(new SMSkillLevel(regisObj.getDesiredSkillLevelId()));
		}
		if (regisObj.getWorkstationId() > 0) {
			regis.setWorkstation(new SMWorkstations(regisObj.getWorkstationId()));
		}
		if (request.getBranchId() > 0) {
			regis.setBranch(new BranchMaster(request.getBranchId()));
		}
		if (request.getDeptId() > 0) {
			regis.setDept(new DepartmentMaster(request.getDeptId()));
		}
		if (regisObj.getLineId() > 0) {
			regis.setLine(new Line(regisObj.getLineId()));
		}
		regis.setStatus(EnovationConstants.PENDING_STRING);

		regis.setSmOjtPlan(new SMOJTPlan(planId));

		session.save(regis);

		regisObj.setOjtRegisId(regis.getId());
	}

	public void setDataInList(List<SMOJTSkillingDTO> actionList, List<Tuple> list, SkillMatrixRequest request) {
		LOGGER.info("# SkillMatrixWorker || setDataInList ");
		for (Tuple obj : list) {
			SMOJTSkillingDTO dtoObj = new SMOJTSkillingDTO();
			dtoObj.setDayNo(CommonUtils.objectToInt(obj.get("dayNo")));
			dtoObj.setAssignedDate(CommonUtils.objectToString(obj.get("createdDate")));
			dtoObj.setCompletedDate(CommonUtils.objectToString(obj.get("updatedDate")));
			dtoObj.setKeyPoint(CommonUtils.objectToString(obj.get("keyPoint")));
			dtoObj.setStatus(CommonUtils.objectToString(obj.get("actionStatus")));
			dtoObj.setoEStatus(CommonUtils.objectToString(obj.get("completedByOe")));
			dtoObj.setParameterValue(CommonUtils.objectToString(obj.get("paramValue")));
			dtoObj.setCycleNo(CommonUtils.objectToInt(obj.get("cycleNo")));
			dtoObj.setExpectedValue(CommonUtils.objectToString(obj.get("expectedValue")));
			dtoObj.setActualValue(CommonUtils.objectToString(obj.get("actualValue")));
			dtoObj.setCyclePlanId(CommonUtils.objectToLong(obj.get("")));
			dtoObj.setOjtParamId(CommonUtils.objectToLong(obj.get("")));
			dtoObj.setPointAuditId(CommonUtils.objectToLong(obj.get("")));
		}
	}

	public void saveQueAnsCalculateMarks(Session session, SMAssessmetDTO optionDto, SMOJTQuetionDTO ojtAsseQue,
			SkillMatrixRequest request, SMAssessmetDTO queDto) {
		LOGGER.info("# SkillMatrixWorker || saveQueAnsCalculateMarks ");
		SMOJTAssessmentOpt option = new SMOJTAssessmentOpt();
		option.setQues(new SMAssessmentQues(queDto.getQuestionId()));
		option.setOpt(new SMAssessmentOptions(optionDto.getOptionId()));
		option.setOjtAssessmentQues(new SMOJTAssessmentQues(ojtAsseQue.getOjtAssQueId()));
		session.save(option);

		updateObtainedMarks(session, queDto, ojtAsseQue, request, optionDto);
	}

	private void updateObtainedMarks(Session session, SMAssessmetDTO queDto, SMOJTQuetionDTO ojtAsseQue,
			SkillMatrixRequest request, SMAssessmetDTO optionDto) {
		LOGGER.info("# SkillMatrixWorker || updateObtainedMarks ");
		boolean flag = checkIsRightAns(session, queDto, request, optionDto);
		if (flag) {
			session.createNativeQuery(
					"UPDATE sm_ojt_assessment_ques soaq SET soaq.obtained_marks=:marksObtained WHERE soaq.id=:ojtAssQueId AND soaq.ques_id=:questionId")
					.setParameter("marksObtained", ojtAsseQue.getQueMarks())
					.setParameter("ojtAssQueId", ojtAsseQue.getOjtAssQueId())
					.setParameter("questionId", queDto.getQuestionId()).executeUpdate();
		}
	}

	private boolean checkIsRightAns(Session session, SMAssessmetDTO queDto, SkillMatrixRequest request,
			SMAssessmetDTO optionDto) {
		LOGGER.info("# SkillMatrixWorker || checkIsRightAns ");
		List<Tuple> list = session.createNativeQuery(
				"SELECT saopt.id as optionId  FROM sm_assessment_ques saque INNER JOIN  sm_assessment_options saopt ON saque.id = saopt.ques_id "
						+ "WHERE saopt.ques_id=:questionId AND saopt.id=:optionId AND saque.assessment_id=:assessmentId and saopt.is_right_ans=1",
				Tuple.class).setParameter("optionId", optionDto.getOptionId())
				.setParameter("questionId", queDto.getQuestionId())
				.setParameter("assessmentId", request.getAssessmentId()).getResultList();
		return !CollectionUtils.isEmpty(list) ? true : false;
	}

	@SuppressWarnings("unused")
	public void updateQuestionAnswer(Session session, SMAssessmetDTO queDto, SMOJTQuetionDTO ojtAsseQue) {
		LOGGER.info("# SkillMatrixWorker || updateQuestionAnswer ");
		session.createNamedQuery(
				"UPDATE sm_ojt_assessment_ques soaq SET soaq.ans=:ans WHERE soaq.id=:ojtAssQueId AND soaq.ques_id=:questionId")
				.setParameter("ans", queDto.getAnswer()).setParameter("ojtAssQueId", ojtAsseQue.getOjtAssQueId())
				.setParameter("questionId", queDto.getQuestionId());
	}

	public void saveAssesmentResult(Session session, SkillMatrixRequest request) {
		LOGGER.info("# SkillMatrixWorker || saveAssesmentResult ");
		List<MailDTO> mailList = new ArrayList<>();
		updateOJTSkillingStatus(session, request, SMConstant.COMPLETED_STRING);

		SkillMatrixRequest emailData = new SkillMatrixRequest();

		getDataForMail(session, request, emailData);

		saveActualMarkAndPerc(session, request);

		SMAssessmetDTO perObj = smUtils.getPassingMark(session, request);

		if (perObj.getPercentage() >= perObj.getPassingMarks()) {

			updateAssesmentStatus(session, request, SMConstant.ASSESSMENT_PASS);

			SMAssessmetDTO certiObj = smUtils.getCertificateId(session, request);

			if (certiObj == null) {
				throw new EnovationException(SMConstant.CERTIFICATE_NOT_FOUND);
			}
			saveOJTCertification(session, request, certiObj);
			emailData.setDbUploadPath(certiObj.getPath());
			updateOjtRegiStatus(session, request, SMConstant.COMPLETED_STRING);

			SMAssessmetDTO slObj = smUtils.getDesiredLevelId(session, request);

			if (checkSMAlreadyExit(slObj, session)) {
				LOGGER.info("employee update in Skill Matrix");
				updateSMEmpSkillMatrix(session, slObj);
			} else {
				addSMEmpSkillMatrix(session, request, slObj);
			}

			// Update skill levels for linked workstations only if assessment is passed
			if (slObj.getSkillLevelId() > 0) {
				List<com.greentin.enovation.model.skillMatrix.SMWorkstationMapping> linkedWorkstations = smUtils.findLinkedWorkstations(session, request.getWorkstationId());
				if (!CollectionUtils.isEmpty(linkedWorkstations)) {
					LOGGER.info("Updating skill levels for {} linked workstations", linkedWorkstations.size());
					for (SMWorkstationMapping mapping : linkedWorkstations) {
						SMAssessmentDTO linkedSlObj = new SMAssessmentDTO();
						linkedSlObj.setEmpId(request.getEmpId());
						linkedSlObj.setWorkstationId(mapping.getChildWorkstation().getId());
						linkedSlObj.setSkillLvlId(slObj.getSkillLevelId());
						linkedSlObj.setBranchId(request.getBranchId());
						linkedSlObj.setDeptId(request.getDeptId());
						linkedSlObj.setLineId(request.getLineId());

						// Check if skill matrix entry exists for the linked workstation
						if (checkSMAlreadyExit(linkedSlObj, session)) {
							// Update existing skill level
							updateSMEmpSkillMatrix(session, linkedSlObj);
							LOGGER.info("Updated skill level for linked workstation ID: {}", mapping.getChildWorkstation().getId());
						} else {
							// Create new skill matrix entry
							addSMEmpSkillMatrix(session, request, linkedSlObj);
							LOGGER.info("Created new skill level entry for linked workstation ID: {}", mapping.getChildWorkstation().getId());
						}
					}
				}
			}

			sendEmailToOEAsessmentPass(mailList, emailData);
			request.setAssessmentStatus(SMConstant.ASSESSMENT_PASS);

		} else {
			updateAssesmentStatus(session, request, SMConstant.ASSESSMENT_FAIL);

			SMOJTRegis regis = smUtils.getRegistrationDetails(session, request);

			saveOJTSkillingDetails(session, regis);
			request.setAssessmentStatus(SMConstant.ASSESSMENT_FAIL);
		}
		if (!CollectionUtils.isEmpty(mailList)) {
			LOGGER.info("#EMail sended");
			taskExecutor.execute(new MailUtil(mailList, communication));
		}
		emailData.setAssessmentStatus(request.getAssessmentStatus());
		sendEmailToOEAsessmentComplete(mailList, emailData);
		request = getEmployeeDetailsForEmail(session, request);
		request.setStartDate(perObj.getAssessmentStartDate());
		String body = env.getProperty("ASSESSMENT_PASS_FAIL_TO_ASSIGNEE.body");
		sendEmailOfAssessment(session, request, body, EnovationConstants.ASSESSMENT_RESULT_TO_AUTHORITY);

	}

	private void getDataForMail(Session session, SkillMatrixRequest request, SkillMatrixRequest emailData) {
		LOGGER.info("#In SkillMatrixWorker |  INSIDE in getDataForMail ");
		TypedQuery<Tuple> query = session.createNativeQuery(
				"select e.emp_id as empId,e.cmpy_emp_id as compEmpId, concat(ifnull(e.first_name,''),' ',ifnull(e.last_name,'')) as empName,\r\n"
						+ " e.email_id as emailId,dl.name as line,sw.workstation as workstation,l.level_name as level,et.email_id as tLEmailId,\r\n"
						+ " sml.certificate_path as path,o.portal_link as portalLink,o.logo as logo "
						+ " from sm_ojt_skilling_audit ssa \r\n"
						+ " inner join sm_ojt_skilling sos on ssa.skilling_id=sos.id\r\n"
						+ " inner join sm_ojt_regis r on r.id = sos.ojt_regis_id\r\n"
						+ " left join tbl_employee_details e on e.emp_id = r.emp_id\r\n"
						+ " left join sm_workstations sw on sw.id = r.workstation_id\r\n"
						+ " left join dwm_line dl on r.line_id=dl.id\r\n"
						+ " left join sm_skill_level l on l.id = r.desired_skill_level_id\r\n"
						+ " left join sm_ojt_skilling_audit ssat on ssat.skilling_id= sos.id and ssat.stage_id=5 \r\n"
						+ " left join tbl_employee_details et on et.emp_id =ssat.emp_id \r\n"
						+ " left join sm_master_certificate sml on sml.skill_level_id=r.desired_skill_level_id and r.branch_id=sml.branch_id and sml.status='Active' "
						+ " inner join master_branch b on r.branch_id =b.branch_id \r\n"
						+ " inner join master_organization o on o.org_id = b.org_id \r\n" + " where ssa.id=:auditId ",
				Tuple.class).setParameter("auditId", request.getSkillingAuditId());

		List<Tuple> objList = query.getResultList();
		if (!CollectionUtils.isEmpty(objList)) {
			Tuple obj = objList.get(0);
			emailData.setEmpName(CommonUtils.objectToString(obj.get("empName")));
			emailData.setEmailId(CommonUtils.objectToString(obj.get("emailId")));
			emailData.setLineName(CommonUtils.objectToString(obj.get("line")));
			emailData.setWorkstation(CommonUtils.objectToString(obj.get("workstation")));
			emailData.setSkillLevel(CommonUtils.objectToString(obj.get("level")));
			emailData.setTrainerEmailId(CommonUtils.objectToString(obj.get("tLEmailId")));
			emailData.setDbUploadPath(
					EnovationConfig.buddyConfig.get("ProfilePicPathUrl") + CommonUtils.objectToString(obj.get("path")));
			emailData.setLogo(
					EnovationConfig.buddyConfig.get("ProfilePicPathUrl") + CommonUtils.objectToString(obj.get("logo")));
			emailData.setPortalLink(CommonUtils.objectToString(obj.get("portalLink")));
		}
	}

	public void addSMEmpSkillMatrix(Session session, SkillMatrixRequest request, SMAssessmetDTO slObj) {
		LOGGER.info("employee add in Skill Matrix");
		SMEmpSkillMatrix smSkill = new SMEmpSkillMatrix();
		smSkill.setCreatedBy(request.getCreatedBy());
		if (slObj.getEmpId() > 0) {
			smSkill.setEmpDetails(new EmployeeDetails(slObj.getEmpId()));
		}
		if (slObj.getWorkstationId() > 0) {
			smSkill.setWorkstations(new SMWorkstations(slObj.getWorkstationId()));
		}
		if (slObj.getDesiredSkillLevelId() > 0) {
			smSkill.setSkillLevel(new SMSkillLevel(slObj.getDesiredSkillLevelId()));
		}
		session.save(smSkill);
	}

	public boolean checkSMAlreadyExit(SMAssessmetDTO slObj, Session session) {
		LOGGER.info("# SkillMatrixUtils | checkSMAlreadyExit");

		return ((Number) session.createNativeQuery(
				"SELECT ifnull(COUNT(*),0) FROM sm_emp_skill_matrix WHERE emp_id=:empId and workstations_id=:workStationId ")
				.setParameter("empId", slObj.getEmpId()).setParameter("workStationId", slObj.getWorkstationId())
				.getSingleResult()).intValue() > 0;
	}

	private void updateOjtRegiStatus(Session session, SkillMatrixRequest request, String status) {
		LOGGER.info("# SkillMatrixWorker || updateOjtRegiStatus ");
		session.createNativeQuery("UPDATE sm_ojt_regis sor SET sor.status=:status WHERE sor.id=:ojtRegiId ")
				.setParameter("ojtRegiId", request.getOjtRegiId()).setParameter("status", status).executeUpdate();
	}

	public void updateOJTSkillingStatus(Session session, SkillMatrixRequest request, String status) {
		LOGGER.info("# SkillMatrixWorker || updateOJTSkillingStatus ");
		session.createNativeQuery("UPDATE sm_ojt_skilling smsk SET smsk.status=:status WHERE smsk.id=:skillingId ")
				.setParameter("skillingId", request.getSkillingId()).setParameter("status", status).executeUpdate();
	}

	private void updateSMEmpSkillMatrix(Session session, SMAssessmetDTO slObj) {
		LOGGER.info("# SkillMatrixWorker || updateSMEmpSkillMatrix ");
		session.createNativeQuery(
				"UPDATE sm_emp_skill_matrix sm SET sm.skill_level_id=:levelId WHERE sm.emp_id=:empId AND sm.workstations_id=:workstationId ")
				.setParameter("empId", slObj.getEmpId()).setParameter("levelId", slObj.getDesiredSkillLevelId())
				.setParameter("workstationId", slObj.getWorkstationId()).executeUpdate();
	}

	private void saveOJTCertification(Session session, SkillMatrixRequest request, SMAssessmetDTO certiObj) {
		LOGGER.info("# SkillMatrixWorker || saveOJTCertification ");
		SMOJTCertification cerObj = new SMOJTCertification();
		cerObj.setOjtRegis(new SMOJTRegis(request.getOjtRegiId()));
		cerObj.setCertificate(new SMMasterCertificate(certiObj.getCertificateId()));
		session.save(cerObj);
	}

	private void updateAssesmentStatus(Session session, SkillMatrixRequest request, String assessmentPass) {
		LOGGER.info("# SkillMatrixWorker || updateAssesmentStatus ");
		session.createNativeQuery(
				"UPDATE sm_ojt_assessment soass SET soass.assessment_status=:status WHERE soass.id=:ojtAssId ")
				.setParameter("ojtAssId", request.getOjtAssId()).setParameter("status", assessmentPass).executeUpdate();
	}

	private void saveActualMarkAndPerc(Session session, SkillMatrixRequest request) {
		LOGGER.info("# SkillMatrixWorker || saveActualMarkAndPerc ");
		session.createNativeQuery(
				"UPDATE sm_ojt_assessment soass SET soass.actual_marks = ( SELECT SUM(soaque.obtained_marks) as actulaMark\r\n"
						+ " FROM sm_ojt_assessment_ques soaque WHERE soaque.ojt_assessment_id=:ojtAssId ), \r\n"
						+ " soass.percentage = (SELECT (SUM(soaque.obtained_marks) / (soass.total_marks) * 100) as percentage\r\n"
						+ " FROM sm_ojt_assessment_ques soaque WHERE soaque.ojt_assessment_id=:ojtAssId )\r\n"
						+ " WHERE soass.id=:ojtAssId ")
				.setParameter("ojtAssId", request.getOjtAssId()).executeUpdate();
	}

	public void updateSkillingPoint(Session session, SkillMatrixRequest request) {
		LOGGER.info("# SkillMatrixWorker || updateSkillingPoint ");
		session.createNativeQuery(
				" update sm_ojt_checksheet_points set status=:status where skilling_checksheet_id=:skillingCheckSheetId ")
				.setParameter("skillingCheckSheetId", request.getSkillingChecksheetId())
				.setParameter("status", request.getStatus()).executeUpdate();

	}

	public void updateObtainedMarks(Session session, Long parentQuestionId, SkillMatrixRequest optionDto,
			SkillMatrixRequest request) {
		LOGGER.info("# SkillMatrixWorker || updateObtainedMarks ");
		boolean result = (boolean) session.createNativeQuery(
				"SELECT saopt.is_right_ans FROM sm_assessment_options saopt LEFT JOIN sm_assessment_ques saque ON saque.id = saopt.ques_id WHERE saopt.ques_id = :currentQuestionId AND saopt.id = :optionId AND saque.assessment_id = :assessmentId")
				.setParameter("optionId", optionDto.getOptionId()).setParameter("currentQuestionId", parentQuestionId)
				.setParameter("assessmentId", request.getAssessmentId()).getSingleResult();

		if (result) {
			Query query = session.createNativeQuery(
					"SELECT saque.que_marks FROM sm_assessment_ques saque WHERE saque.id = :currentQuestionId AND saque.assessment_id = :assessmentId")
					.setParameter("currentQuestionId", parentQuestionId)
					.setParameter("assessmentId", request.getAssessmentId());
			Object resultObj = query.getSingleResult();

			if (resultObj != null) {
				int marksObtained = ((Number) resultObj).intValue();

				session.createNativeQuery(
						"UPDATE sm_ojt_assessment_ques soaq SET soaq.obtained_marks = obtained_marks + :marksObtained WHERE soaq.ojt_assessment_id = :ojtAssId AND soaq.ques_id = :currentQuestionId")
						.setParameter("marksObtained", marksObtained).setParameter("ojtAssId", request.getOjtAssId())
						.setParameter("currentQuestionId", parentQuestionId).executeUpdate();
			}
		}
	}

	public void setFilterSearchQueryForAssessmentsDetail(StringBuilder sb, SkillMatrixRequest request) {
		LOGGER.info("# SkillMatrixWorker || setFilterSearchQueryForAssessmentsDetail ");
		StringBuilder string = new StringBuilder(" ");
	}

	public void setParameterQuery(TypedQuery<Tuple> query, SkillMatrixRequest request) {
		LOGGER.info("# SkillMatrixWorker || setParameterQuery ");

	}

	public void assignNextDayPointToStage1(Session session, SkillMatrixRequest request) {
		LOGGER.info("# SkillMatrixWorker || assignNextDayPointToStage1 ");

		SMOJTSkillingChecksheet checksheet = null;
		TypedQuery<Tuple> query = session.createNativeQuery(
				"select ojtSC.id as skillingChecksheetId,ojtSC.day_no as dayNo from sm_ojt_skilling_checksheet ojtSC\r\n"
						+ " left join sm_ojt_skilling_audit audit on audit.skilling_checksheet_id=ojtSC.id and stage_id=:stageId\r\n"
						+ " where ojtSC.skilling_id=:skillingId and (audit.status <>:status or audit.status is null) order by ojtSC.day_no limit 1 ",
				Tuple.class).setParameter("skillingId", request.getSkillingId())
				.setParameter("status", SMConstant.COMPLETED_STRING).setParameter("stageId", SMConstant.STAGE1_ID);

		List<Tuple> objList = query.getResultList();

		if (!CollectionUtils.isEmpty(objList)) {
			checksheet = new SMOJTSkillingChecksheet();
			Tuple obj = objList.get(0);
			checksheet.setId(CommonUtils.objectToLong(obj.get("skillingChecksheetId")));
			checksheet.setDayNo(CommonUtils.objectToInt(obj.get("dayNo")));
		}
		if (checksheet != null) {
			request.setSkillingChecksheetId(checksheet.getId());
			assignToStage1(session, request);
		} else {
			LOGGER.info("# SkillMatrixWorker || Next Day Point Not Found");
		}
	}

	public void makeCertificateInavtive(Session session, SkillMatrixRequest request) {
		LOGGER.info("#In SkillMatrixWorker |  INSIDE makeCertificateInavtive ");

		session.createNativeQuery(
				"UPDATE sm_master_certificate SET status =:certStatus WHERE skill_level_id =:skillLevelId AND branch_id=:branchId ")
				.setParameter("certStatus", SMConstant.INACTIVE).setParameter("skillLevelId", request.getSkillLevelId())
				.setParameter("branchId", request.getBranchId()).executeUpdate();
	}

	public boolean setNextStageId(Session session, SkillMatrixRequest request) {
		LOGGER.info("# SkillMatrixWorker || setUserType ");
		boolean flag = false;
		LOGGER.info(" Current StageId: {} and branchId: {}", request.getStageId(), request.getBranchId());
		LOGGER.info("# levelID: {} ", request.getSkillLevelId());
		if (request.getStageId() <= SMConstant.STAGE1_ID
				&& (getWorkFLowConfig(session, request, SMConstant.STAGE2_ID) == SMConstant.STAGE2_ID)) {
			assignToStage2(session, request);

		} else if (request.getStageId() <= SMConstant.STAGE2_ID && (getWorkFLowConfig(session, request,
				SMConstant.STAGE2_VERIFICATION_ID) == SMConstant.STAGE2_VERIFICATION_ID)) {
			assignToStage2Verification(session, request);

		} else if (request.getStageId() <= SMConstant.STAGE2_VERIFICATION_ID
				&& (getWorkFLowConfig(session, request, SMConstant.STAGE3_ID) == SMConstant.STAGE3_ID)) {
			assignToStage3(session, request);

		} else if (request.getStageId() <= SMConstant.STAGE3_ID
				&& getWorkFLowConfig(session, request, SMConstant.STAGE4_ID) == SMConstant.STAGE4_ID) {

			updateSkillingAudit(session, request);

			updateSkillingPoint(session, request);
			// update Points Audit Table
			updateSkillingPointAudit(session, request);

			updateSkillingChecksheetDay(session, request);
			if (smUtils.getCSPendingDaysCount(session, request) == 0) {
				request.setSkillingChecksheetId(0);
				assignToStage4(session, request);
			}
		} else if (request.getStageId() <= SMConstant.STAGE4_ID
				&& (getWorkFLowConfig(session, request, SMConstant.STAGE5_ID) == SMConstant.STAGE5_ID)) {
			LOGGER.info("# Satge 5 is enable");
			flag = true;
		} else {
			LOGGER.info("# Next Stage not found");
//			throw new EnovationException("Next Stage not found");
		}
		return flag;
	}

	public long getWorkFLowConfig(Session session, SkillMatrixRequest request, Integer stageId) {
		LOGGER.info("# SkillMatrixWorker || getWorkFLowConfig ");
		LOGGER.info("Checking workFlow for stageId: {}", stageId);

		TypedQuery<Tuple> configList = session
				.createNativeQuery(
						" select sc.stage_id as stageId,sc.branch_id as branchId,sc.skill_level_id as levelId\r\n"
								+ " from sm_workflow_config sc where sc.is_active=1 and sc.branch_id=:branchId and "
								+ " sc.skill_level_id=:levelId and sc.stage_id=:stageId limit 1",
						Tuple.class)
				.setParameter("branchId", request.getBranchId()).setParameter("levelId", request.getSkillLevelId())
				.setParameter("stageId", stageId);

		List<Tuple> config = configList.getResultList();
		LOGGER.info("# config Size : {}", config.size());
		if (!CollectionUtils.isEmpty(config)) {
			for (Tuple obj : config) {
				if (CommonUtils.objectToLong(obj.get("stageId")) > 0) {
					return CommonUtils.objectToLong(obj.get("stageId"));
				}
			}
		}

		return 0;
	}

	public void setStageLabelDetails(Session session, SkillMatrixRequest request, SMStageLabel label) {
		LOGGER.info("# SkillMatrixWorker || saveOrUpdateStageLabelDetails ");
		label.setBranch(new BranchMaster(request.getBranchId()));

		if (request.getStageId() != 0) {

			if (smUtils.stageIdExist(session, request)) {
				throw new EnovationException("Stage ID already exists in the branch.");
			} else
				label.setStage(new SMStage(request.getStageId()));
		}
		if (request.getStageLabel() != null) {
			if (smUtils.stageLabelExist(session, request)) {
				throw new EnovationException("Stage label already exists in the branch.");
			} else
				label.setStageLabel(request.getStageLabel());
		}
		if (request.getUpdatedBy() > 0) {
			label.setUpdatedBy(request.getUpdatedBy());
		}

		if (request.getCreatedBy() > 0) {
			label.setCreatedBy(request.getCreatedBy());
		}
	}

	public void sendEmailToOE(String portalLink, List<MailDTO> mailList, SkillMatrixRequest request, String orgLogo) {
		LocalDate date = LocalDate.now();
		EmailTemplateMaster messageContent = null;
		messageContent = evonationConfig.getMessageContent(EnovationConstants.SKILLMATRIX_EMAIL_TEMPLATE);

		if (messageContent != null && request != null && request.getEmailId() != null
				&& request.getEmailId().length() > 0) {
			LOGGER.info("#messageContent added");

			String subject = messageContent.getSubject().replaceAll(Pattern.quote("{subject}"),
					CommonUtils.objectToString("OJT Initiative Commencing Today"));
			String emailCon = env.getProperty("OJT_OE_EMIAL_Body.body")
					.replaceAll(Pattern.quote("{startDate}"), date.toString())
					.replaceAll(Pattern.quote("{name}"), request.getEmpName());

			String content = setEmailParameter(portalLink, request, orgLogo, messageContent, emailCon,
					CommonUtils.objectToString(request.getEmpName()));

			MailDTO mail = new MailDTO(request.getEmailId(), subject, content);
			mailList.add(mail);
		}
	}

	public void sendEmailToTrainer(String portalLink, List<MailDTO> mailList, SkillMatrixRequest request,
			String orgLogo) {
		LocalDate date = LocalDate.now();
		EmailTemplateMaster messageContent = null;
		messageContent = evonationConfig.getMessageContent(EnovationConstants.SKILLMATRIX_EMAIL_TEMPLATE);

		if (messageContent != null && request != null && request.getTrainerEmailId() != null
				&& request.getTrainerEmailId().length() > 0) {
			LOGGER.info("#messageContent added");

			String subject = messageContent.getSubject().replaceAll(Pattern.quote("{subject}"),
					CommonUtils.objectToString("OJT Initiative Commencing Today"));
			String emailCon = env.getProperty("OJT_TRAINER_EMIAL_Body.body")
					.replaceAll(Pattern.quote("{startDate}"), date.toString())
					.replaceAll(Pattern.quote("{name}"), CommonUtils.objectToString(request.getTrainerName()));

			String content = setEmailParameter(portalLink, request, orgLogo, messageContent, emailCon,
					CommonUtils.objectToString(request.getTrainerName()));

			MailDTO mail = new MailDTO(request.getTrainerEmailId(), subject, content);
			mailList.add(mail);
		}
	}

	public String setEmailParameter(String portalLink, SkillMatrixRequest request, String orgLogo,
			EmailTemplateMaster messageContent, String emailCon, String empName) {
		String content = messageContent.getBody().replaceAll(Pattern.quote("{body}"), emailCon)
				.replaceAll(Pattern.quote("{portalLink}"), portalLink)
				.replaceAll(Pattern.quote("{thisYear}"), (String.valueOf(LocalDate.now().getYear())))
				.replaceAll(Pattern.quote("{plant}"), CommonUtils.objectToString(request.getBranchName()))
				.replaceAll(Pattern.quote("{dept}"), CommonUtils.objectToString(request.getDeptName()))
				.replaceAll(Pattern.quote("{cell}"), CommonUtils.objectToString(request.getLineName()))
				.replaceAll(Pattern.quote("{workstation}"), CommonUtils.objectToString(request.getWorkstation()))
				.replaceAll(Pattern.quote("{OEName}"), CommonUtils.objectToString(request.getEmpName()))
				.replaceAll(Pattern.quote("{level}"), CommonUtils.objectToString(request.getSkillLevel()))
				.replaceAll(Pattern.quote("{companyLogo}"),
						orgLogo != null ? EnovationConfig.buddyConfig.get("ProfilePicPathUrl") + orgLogo
								: EnovationConstants.myeNovationLogoPath)
				.replaceAll(Pattern.quote("{name}"), empName);
		return content;
	}

	public void sendEmailToOEAsessmentComplete(List<MailDTO> mailList, SkillMatrixRequest request) {
		LOGGER.info("#In SkillMatrixWorker |  INSIDE in sendEmailToOEAsessmentComplete ");
		if (request.getEmailId() != null && request.getEmailId().length() > 0) {
			LocalDate date = LocalDate.now();
			EmailTemplateMaster messageContent = null;
			messageContent = evonationConfig.getMessageContent(EnovationConstants.SKILLMATRIX_EMAIL_TEMPLATE);

			if (messageContent != null) {
				LOGGER.info("#messageContent added");

				String subject = messageContent.getSubject().replaceAll(Pattern.quote("{subject}"),
						CommonUtils.objectToString("Assessment Status"));
				String emailCon = env.getProperty("ASSESSMENT_COMPLETE_EMIAL_Body.body")
						.replaceAll(Pattern.quote("{startDate}"), date.toString())
						.replaceAll(Pattern.quote("{name}"), CommonUtils.objectToString(request.getEmpName()));

				String content = messageContent.getBody().replaceAll(Pattern.quote("{body}"), emailCon)
						.replaceAll(Pattern.quote("{name}"), request.getEmpName())
						.replaceAll(Pattern.quote("{portalLink}"), CommonUtils.objectToString(request.getPortalLink()))
						.replaceAll(Pattern.quote("{thisYear}"), (String.valueOf(LocalDate.now().getYear())))
						.replaceAll(Pattern.quote("{cell}"), CommonUtils.objectToString(request.getLineName()))
						.replaceAll(Pattern.quote("{workstation}"),
								CommonUtils.objectToString(request.getWorkstation()))
						.replaceAll(Pattern.quote("{assessmentDate}"), CommonUtils.objectToString(date))
						.replaceAll(Pattern.quote("{status}"),
								CommonUtils.objectToString(request.getAssessmentStatus()))
						.replaceAll(Pattern.quote("{companyLogo}"),
								(request.getLogo() != null && request.getLogo().length() > 0)
										? EnovationConfig.buddyConfig.get("ProfilePicPathUrl") + request.getLogo()
										: EnovationConstants.myeNovationLogoPath);

				MailDTO mail = new MailDTO(request.getEmailId(), subject, content);
				mailList.add(mail);
			}
		}
	}

	public void sendEmailToOEAsessmentPass(List<MailDTO> mailList, SkillMatrixRequest request) {
		LocalDate date = LocalDate.now();
		LOGGER.info("#In SkillMatrixWorker |  INSIDE in sendEmailToOEAsessmentPass ");
		if (request.getEmailId() != null && request.getEmailId().length() > 0) {
			EmailTemplateMaster messageContent = null;
			messageContent = evonationConfig.getMessageContent(EnovationConstants.SKILLMATRIX_EMAIL_TEMPLATE);

			if (messageContent != null) {
				LOGGER.info("#messageContent not null");

				String subject = messageContent.getSubject().replaceAll(Pattern.quote("{subject}"),
						CommonUtils.objectToString("Assessment Status"));
				String emailCon = env.getProperty("ASSESSMENT_PASS_EMIAL_Body.body")
						.replaceAll(Pattern.quote("{startDate}"), date.toString())
						.replaceAll(Pattern.quote("{name}"), request.getEmpName());

				String content = messageContent.getBody().replaceAll(Pattern.quote("{body}"), emailCon)
						.replaceAll(Pattern.quote("{name}"), request.getEmpName())
						.replaceAll(Pattern.quote("{portalLink}"), CommonUtils.objectToString(request.getPortalLink()))
						.replaceAll(Pattern.quote("{thisYear}"), (String.valueOf(LocalDate.now().getYear())))
						.replaceAll(Pattern.quote("{plant}"), CommonUtils.objectToString(request.getBranchName()))
						.replaceAll(Pattern.quote("{dept}"), CommonUtils.objectToString(request.getDeptName()))
						.replaceAll(Pattern.quote("{cell}"), CommonUtils.objectToString(request.getLineName()))
						.replaceAll(Pattern.quote("{workstation}"),
								CommonUtils.objectToString(request.getWorkstation()))
						.replaceAll(Pattern.quote("{OEName}"), CommonUtils.objectToString(request.getEmpName()))
						.replaceAll(Pattern.quote("{level}"), CommonUtils.objectToString(request.getSkillLevel()))
						.replaceAll(Pattern.quote("{path}"), CommonUtils.objectToString(request.getDbUploadPath()))
						.replaceAll(Pattern.quote("{companyLogo}"),
								request != null ? EnovationConfig.buddyConfig.get("ProfilePicPathUrl") + request
										: EnovationConstants.myeNovationLogoPath);

				MailDTO mail = new MailDTO(request.getEmailId(), subject, content);
				mailList.add(mail);
			}
		}
	}

	public void sendEmailToRegisOE(List<MailDTO> mailList, SkillMatrixRequest request) {
		LOGGER.info("#In SkillMatrixWorker |  INSIDE in sendEmailToOE ");
		LocalDate date = LocalDate.now();
		EmailTemplateMaster messageContent = null;
		messageContent = evonationConfig.getMessageContent(EnovationConstants.SKILLMATRIX_EMAIL_TEMPLATE);

		if (messageContent != null && request != null && request.getEmailId() != null
				&& request.getEmailId().length() > 0) {

			String subject = messageContent.getSubject().replaceAll(Pattern.quote("{subject}"),
					CommonUtils.objectToString("OJT Initiative Commencing Today"));
			String emailCon = env.getProperty("OJT_OE_EMIAL_Body.body")
					.replaceAll(Pattern.quote("{startDate}"), date.toString())
					.replaceAll(Pattern.quote("{name}"), request.getEmpName());

			String content = messageContent.getBody().replaceAll(Pattern.quote("{body}"), emailCon)
					.replaceAll(Pattern.quote("{portalLink}"), CommonUtils.objectToString(request.getPortalLink()))
					.replaceAll(Pattern.quote("{thisYear}"), (String.valueOf(LocalDate.now().getYear())))
					.replaceAll(Pattern.quote("{cell}"), CommonUtils.objectToString(request.getLineName()))
					.replaceAll(Pattern.quote("{workstation}"), CommonUtils.objectToString(request.getWorkstation()))
					.replaceAll(Pattern.quote("{startDate}"), CommonUtils.objectToString(date))
					.replaceAll(Pattern.quote("{level}"),
							CommonUtils.objectToString(request.getDesiredSkillLevelName()))
					.replaceAll(Pattern.quote("{name}"), request.getEmpName())
					.replaceAll(Pattern.quote("{companyLogo}"),
							(request.getLogo() != null && request.getLogo().length() > 0)
									? EnovationConfig.buddyConfig.get("ProfilePicPathUrl") + request.getLogo()
									: EnovationConstants.myeNovationLogoPath);

			MailDTO mail = new MailDTO(request.getEmailId(), subject, content);
			mailList.add(mail);
		}
	}

	public void sendEmailTooTrainer(List<MailDTO> mailList, SkillMatrixRequest request) {
		LOGGER.info("#In SkillMatrixWorker |  INSIDE in sendEmailToTrainer ");
		LocalDate date = LocalDate.now();
		EmailTemplateMaster messageContent = null;

		System.out.print("request.getLineName()....." + request.getLineName());
		messageContent = evonationConfig.getMessageContent(EnovationConstants.SKILLMATRIX_EMAIL_TEMPLATE);

		if (messageContent != null && request != null && request.getTrainerEmailId() != null
				&& request.getTrainerEmailId().length() > 0) {
			LOGGER.info("#messageContent added");

			String subject = messageContent.getSubject().replaceAll(Pattern.quote("{subject}"),
					CommonUtils.objectToString("OJT Initiative Commencing Today"));
			String emailCon = env.getProperty("OJT_TRAINER_EMIAL_Body.body")
					.replaceAll(Pattern.quote("{startDate}"), date.toString())
					.replaceAll(Pattern.quote("{name}"), CommonUtils.objectToString(request.getTrainerName()));

			String content = messageContent.getBody().replaceAll(Pattern.quote("{body}"), emailCon)
					.replaceAll(Pattern.quote("{portalLink}"), CommonUtils.objectToString(request.getPortalLink()))
					.replaceAll(Pattern.quote("{thisYear}"), (String.valueOf(LocalDate.now().getYear())))
					.replaceAll(Pattern.quote("{cell}"), CommonUtils.objectToString(request.getLineName()))
					.replaceAll(Pattern.quote("{workstation}"), CommonUtils.objectToString(request.getWorkstation()))
					.replaceAll(Pattern.quote("{startDate}"), CommonUtils.objectToString(date))
					.replaceAll(Pattern.quote("{level}"),
							CommonUtils.objectToString(request.getDesiredSkillLevelName()))
					.replaceAll(Pattern.quote("{OEName}"), CommonUtils.objectToString(request.getEmpName()))
					.replaceAll(Pattern.quote("{name}"), CommonUtils.objectToString(request.getTrainerName()))
					.replaceAll(Pattern.quote("{companyLogo}"),
							(request.getLogo() != null && request.getLogo().length() > 0)
									? EnovationConfig.buddyConfig.get("ProfilePicPathUrl") + request.getLogo()
									: EnovationConstants.myeNovationLogoPath);

			MailDTO mail = new MailDTO(request.getTrainerEmailId(), subject, content);
			mailList.add(mail);

		}
	}

	public void assignSafetyAssessment(Session session, SMOJTPlanDTO dtoObj) {
		LOGGER.info("#In SkillMatrixWorker |  INSIDE in assgineSafetyAssessment ");

		SMOJTPlanDTO oldAssData = getAssignedAssessment(session, dtoObj);
		getAssessmentData(session, dtoObj);
		if (oldAssData != null && oldAssData.getAssessmentId() == dtoObj.getAssessmentId()) {
			LOGGER.info("Assessment Already Assigned: " + oldAssData.getAssessmentId());
		} else {
			if (deleteSafetyAssessment(session, oldAssData)) {
				if (dtoObj.getAssessmentId() > 0) {
					assgineAssessment(session, dtoObj);
				} else {
					throw new EnovationException("Safety Assessment Not Found. ");
				}

			}
		}
	}

	private SMOJTPlanDTO getAssignedAssessment(Session session, SMOJTPlanDTO dtoObj) {
		SMOJTPlanDTO oldAssData = new SMOJTPlanDTO();
		LOGGER.info("#In SkillMatrixWorker |  INSIDE in getAssessmentData ");
		List<Tuple> tupleList = session.createNativeQuery(
				" select id as id,assessment_id as assessmentId , assessment_status as regstatus from sm_ojt_assessment where ojt_regis_id=:regisId",
				Tuple.class).setParameter("regisId", dtoObj.getOjtRegisId()).getResultList();
		if (!CollectionUtils.isEmpty(tupleList)) {
			for (Tuple obj : tupleList) {
				oldAssData.setId(CommonUtils.objectToLong(obj.get("id")));
				oldAssData.setAssessmentId(CommonUtils.objectToLong(obj.get("assessmentId")));
				oldAssData.setStatus(CommonUtils.objectToString(obj.get("regstatus")));
				oldAssData.setOjtRegisId(dtoObj.getOjtRegisId());
			}
		}
		return oldAssData;
	}

	private boolean deleteSafetyAssessment(Session session, SMOJTPlanDTO dtoObj) {
		LOGGER.info("deleteSafetyAssessment");
		try {
			session.createNativeQuery(" delete from sm_ojt_assessment_ques where ojt_assessment_id =:id")
					.setParameter("id", dtoObj.getId()).executeUpdate();

			session.createNativeQuery(" delete from sm_ojt_assessment where id =:id").setParameter("id", dtoObj.getId())
					.executeUpdate();

			session.createNativeQuery(
					" delete from sm_ojt_skilling_audit where stage_id =:stageId and ojt_regis_id=:ojtRegisId "
							+ " and status='PENDING' ")
					.setParameter("id", dtoObj.getId()).setParameter("ojtRegisId", dtoObj.getOjtRegisId())
					.setParameter("stageId", SMConstant.STAGE7_ID).executeUpdate();

			return true;
		} catch (Exception e) {
			if (CommonUtils.isConstraintViolationException(e)) {
				return false;
			} else {
				LOGGER.info("Exception In deleteSafetyAssessment");
				e.printStackTrace();
				return true;
			}
		}
	}

	private SMOJTPlanDTO getAssessmentData(Session session, SMOJTPlanDTO dtoObj) {
		LOGGER.info("#In SkillMatrixWorker |  INSIDE in getAssessmentData ");
		List<Tuple> tupleList = session.createNativeQuery(
				" select distinct r.id as regisId,r.emp_id as regempId,concat(e.first_name ,' ',e.last_name)  as empName,\r\n"
						+ " r.current_skill_level_id as currentSkillLevelId, e.emp_id as empId ,\r\n"
						+ " r.desired_skill_level_id as desiredSkillLevelId ,r.status as regstatus, a.total_marks as totalMarks,\r\n"
						+ " a.id as assessmentId,a.skill_level_id as skillLevelId,sos.id as skillingId,a.passing_marks as passingMarks "
						+ " from sm_ojt_regis r\r\n" + " INNER join tbl_employee_details e on e.emp_id=r.emp_id\r\n"
						+ " INNER JOIN sm_assessment a ON r.workstation_id=a.workstation_id and a.assessment_type=:statuses\r\n"
						+ " left join sm_ojt_skilling sos on sos.ojt_regis_id=r.id \r\n"
						+ " left join sm_ojt_assessment soa on soa.ojt_regis_id=r.id "
						+ " where r.id=:regisId and a.is_active=1 and a.status=:assessStatus order by a.id desc limit 1",
				Tuple.class).setParameter("statuses", SMConstant.SAFETY_ASSESSMENT)
				.setParameter("assessStatus", SMConstant.PUBLISHED_ASSESSMENT)
				.setParameter("regisId", dtoObj.getOjtRegisId()).getResultList();
		if (!CollectionUtils.isEmpty(tupleList)) {
			for (Tuple obj : tupleList) {
				dtoObj.setEmpId(CommonUtils.objectToInt(obj.get("empId")));
				dtoObj.setAssessmentId(CommonUtils.objectToLong(obj.get("assessmentId")));
				dtoObj.setStatus(CommonUtils.objectToString(obj.get("regstatus")));
				dtoObj.setSkillingId(CommonUtils.objectToLong(obj.get("skillingId")));
				dtoObj.setTotalMarks(CommonUtils.objectToDouble(obj.get("totalMarks")));
				dtoObj.setPassingMarks(CommonUtils.objectToDouble(obj.get("passingMarks")));
			}
		}
		return dtoObj;
	}

	private void assgineAssessment(Session session, SMOJTPlanDTO dotObj) {
		LOGGER.info("#In SkillMatrixWorker |  INSIDE in assgineAssessment ");

		SMOJTSkillingAudit savedSkillAudit = employeeAssessmentSkillAudit(session, dotObj, SMConstant.STAGE7_ID);
		SMOJTAssessment ojtAssessment = employeeAssessmentAssign(session, dotObj, savedSkillAudit);
		List<Tuple> tupleObj = fetchQuestionData(session, dotObj.getAssessmentId());
		if (!CollectionUtils.isEmpty(tupleObj)) {
			for (Tuple questionTuple : tupleObj) {
				saveOJTAssessmentQue(session, ojtAssessment, questionTuple);
			}
		}

		SkillMatrixRequest request = new SkillMatrixRequest();
		request.setSkillingId(dotObj.getSkillingId());

		request.setOjtRegiId(dotObj.getOjtRegisId());
		request = getEmployeeDetailsByRegisIdForEmail(session, request);
		String body = env.getProperty("Safty_Assessment_Assign_TO_OE.body");
		sendEmailOfAssessment(session, request, body, EnovationConstants.ASSESSMENT_ASSIGN_TO_OE);

	}

	private void sendEmailOfAssessment(Session session, SkillMatrixRequest request, String body, String lable) {

		// send Email to WHO Created OJT Plan
		if (CommonUtils.isStringNotNull(request.getAssignedByEmail())
				&& lable.equalsIgnoreCase(EnovationConstants.ASSESSMENT_RESULT_TO_AUTHORITY)) {
			sendEmailToStakeholderAssessment(session, request, body, request.getAssignedByEmail(),
					request.getAssignedBy());
		}
		// send Email to OE
		if (CommonUtils.isStringNotNull(request.getEmailId())
				&& lable.equalsIgnoreCase(EnovationConstants.ASSESSMENT_ASSIGN_TO_OE)) {
			sendEmailToStakeholderAssessment(session, request, body, request.getEmailId(), request.getEmpName());
		}

	}

	public void sendEmailToStakeholderAssessment(Session session, SkillMatrixRequest request, String body,
			String emailId, String name) {
		LOGGER.info("#In SkillMatrixWorker |  INSIDE in sendEmailToStakeholderAssessment ");
		List<MailDTO> mailList = new ArrayList<>();

		if (request.getEmailId() != null && request.getEmailId().length() > 0) {
			LocalDate date = LocalDate.now();
			EmailTemplateMaster messageContent = null;
			messageContent = evonationConfig.getMessageContent(EnovationConstants.SKILLMATRIX_EMAIL_TEMPLATE);
			String subject = messageContent.getSubject().replaceAll(Pattern.quote("{subject}"),
					CommonUtils.objectToString("Reminder notification"));
			if (messageContent != null) {
				LOGGER.info("#messageContent added");

				String content = messageContent.getBody().replaceAll(Pattern.quote("{body}"), body)
						.replaceAll(Pattern.quote("{AssignedBy}"), CommonUtils.objectToString(request.getAssignedBy()))
						.replaceAll(Pattern.quote("{name}"), name)
						.replaceAll(Pattern.quote("{subject}"), CommonUtils.objectToString("Reminder notification"))
						.replaceAll(Pattern.quote("{portalLink}"), CommonUtils.objectToString(request.getPortalLink()))
						.replaceAll(Pattern.quote("{thisYear}"), (String.valueOf(LocalDate.now().getYear())))
						.replaceAll(Pattern.quote("{cell}"), CommonUtils.objectToString(request.getLineName()))
						.replaceAll(Pattern.quote("{workstation}"),
								CommonUtils.objectToString(request.getWorkstation()))
						.replaceAll(Pattern.quote("{assessmentDate}"), CommonUtils.objectToString(date))
						.replaceAll(Pattern.quote("{assessmentStartDate}"),
								CommonUtils.objectToString(request.getStartDate()))
						.replaceAll(Pattern.quote("{RequiredLevel}"), request.getSkillLevel())
						.replaceAll(Pattern.quote("{assessmentStatus}"), request.getAssessmentStatus())
						.replaceAll(Pattern.quote("{empName}"), request.getEmpName())
						.replaceAll(Pattern.quote("{companyLogo}"),
								(request.getLogo() != null && request.getLogo().length() > 0)
										? EnovationConfig.buddyConfig.get("ProfilePicPathUrl") + request.getLogo()
										: EnovationConstants.myeNovationLogoPath);

				MailDTO mail = new MailDTO(emailId, subject, content);
				mailList.add(mail);
				if (!CollectionUtils.isEmpty(mailList)) {
					taskExecutor.execute(new MailUtil(mailList, communication));
				}
			}
		}

	}

	private SMOJTSkillingAudit employeeAssessmentSkillAudit(Session session, SMOJTPlanDTO dotObj, long stageId) {
		LOGGER.info("#In SkillMatrixWorker |  INSIDE in employeeAssessmentSkillAudit ");
		SMOJTSkillingAudit skill = new SMOJTSkillingAudit();
		if (dotObj.getSkillingId() > 0) {
			skill.setSkilling(new SMOJTSkilling(dotObj.getSkillingId()));
		}
		if (dotObj.getEmpId() > 0) {
			skill.setEmpDetails(new EmployeeDetails(dotObj.getEmpId()));
		}
		if (dotObj.getOjtRegisId() > 0) {
			skill.setOjtRegis(new SMOJTRegis(dotObj.getOjtRegisId()));
		}
		skill.setStage(new SMStage(stageId));
		skill.setStatus(EnovationConstants.PENDING_STRING);

		session.save(skill);

		return skill;
	}

	private SMOJTAssessment employeeAssessmentAssign(Session session, SMOJTPlanDTO dotObj,
			SMOJTSkillingAudit skillAudit) {
		LOGGER.info("#In SkillMatrixWorker |  INSIDE in employeeAssessmentAssign ");
		SMOJTAssessment assignment = new SMOJTAssessment();

		if (dotObj.getOjtRegisId() > 0) {
			assignment.setOjtRegis(new SMOJTRegis(dotObj.getOjtRegisId()));
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
		if (dotObj.getPassingMarks() > 0) {
			assignment.setPassingMarks(dotObj.getPassingMarks());
		}
		assignment.setSkillingAudit(skillAudit);

		session.save(assignment);
		return assignment;
	}

	public List<Tuple> fetchQuestionData(Session session, long assessmentId) {
		LOGGER.info("#In SkillMatrixWorker |  INSIDE in fetchQuestionData ");
		return session.createNativeQuery(
				"SELECT id as questionId FROM sm_assessment_ques WHERE is_active=1 and  assessment_id =:assessmentId",
				Tuple.class).setParameter("assessmentId", assessmentId).getResultList();
	}

	private SMOJTAssessmentQues saveOJTAssessmentQue(Session session, SMOJTAssessment ojtAssessment,
			Tuple questionTuple) {
		LOGGER.info("#In SkillMatrixWorker |  INSIDE in saveOJTAssessmentQue ");

		SMOJTAssessmentQues assessment = new SMOJTAssessmentQues();
		assessment.setOjtAssessment(ojtAssessment);
		assessment.setQues(new SMAssessmentQues(CommonUtils.objectToInt(questionTuple.get("questionId"))));
		session.save(assessment);

		return assessment;
	}

	public String getSafetyAssessResult(Session session, int empId) {
		LOGGER.info("#In SkillMatrixWorker |  INSIDE in getSafetyAssessResult ");
		String assessStatus = "";
		List<Tuple> tupleList = session
				.createNativeQuery(" SELECT id AS id,emp_id AS empId,skill_level_id AS skillLevelId "
						+ " FROM sm_emp_skill_matrix WHERE emp_id=:empId \r\n", Tuple.class)
				.setParameter("empId", empId).getResultList();
		if (!CollectionUtils.isEmpty(tupleList)) {
			assessStatus = SMConstant.PASS;
		}
		return assessStatus;
	}

	public String checkSafetyAssessResult(Session session, int empId) {
		LOGGER.info("#In SkillMatrixWorker |  INSIDE in checkSafetyAssessResult ");
		String assessStatus = "";
		List<Tuple> tupleList = session.createNativeQuery(" SELECT regi.id AS id,regi.emp_id AS empId \r\n"
				+ "FROM sm_ojt_regis regi\r\n"
				+ "INNER JOIN sm_ojt_assessment smOjtAss ON regi.id=smOjtAss.ojt_regis_id\r\n"
				+ "INNER JOIN sm_assessment smAss ON smAss.id=smOjtAss.assessment_id AND smAss.assessment_type='SAFETY'\r\n"
				+ "WHERE regi.emp_id=:empId and smOjtAss.assessment_status=:assessStatus \r\n", Tuple.class)
				.setParameter("empId", empId).setParameter("assessStatus", SMConstant.PASS).getResultList();
		if (!CollectionUtils.isEmpty(tupleList)) {
			assessStatus = SMConstant.PASS;
		} else {
			assessStatus = getSafetyAssessResult(session, empId);
		}
		return assessStatus;
	}

	public void saveAssesmentResultDetails(Session session, SkillMatrixRequest request) {
		LOGGER.info("# SkillMatrixWorker || saveAssesmentResultDetails ");
		List<MailDTO> mailList = new ArrayList<>();

		saveActualMarkAndPerc(session, request);

		SMAssessmetDTO perObj = smUtils.getPassingMark(session, request);

		if (perObj.getPercentage() >= perObj.getPassingMarks()) {

			updateAssesmentStatus(session, request, SMConstant.ASSESSMENT_PASS);
//			updateOjtRegiStatus(session, request);

			updateOJTSkillingAuditStatus(session, request, SMConstant.COMPLETED_STRING);
			request.setAssessmentStatus(SMConstant.ASSESSMENT_PASS);

			LOGGER.info("# SkillMatrixUtils | checkSMAlreadyExit");

			if (smUtils.checkIsOjtPlanIsAlreadyPublished(session, request)
					&& !(smUtils.getSkillingLvlId(session, request.getOjtRegiId()))) {
				LOGGER.info("# OJT already published");
				assignOjtIfOEMissed(session, request, mailList);
			}
		} else {
			updateAssesmentStatus(session, request, SMConstant.ASSESSMENT_FAIL);

			updateOjtRegiStatus(session, request, SMConstant.SAFETY_FAIL);
			updateOJTSkillingAuditStatus(session, request, SMConstant.COMPLETED_STRING);
			request.setAssessmentStatus(SMConstant.ASSESSMENT_FAIL);

		}

		request = getEmployeeDetailsByRegisIdForEmail(session, request);
		String body = env.getProperty("SAFTY_ASSESSMENT_PASS_FAIL_TO_ASSIGNEE.body");
		request.setStartDate(perObj.getAssessmentStartDate());
		sendEmailOfAssessment(session, request, body, EnovationConstants.ASSESSMENT_RESULT_TO_AUTHORITY);

	}

	private void assignOjtIfOEMissed(Session session, SkillMatrixRequest request, List<MailDTO> mailList) {
		LOGGER.info("# assignOjtIfOEMissed ");
		
		Tuple objRegis = getRegisDetailsForPlan(session, request.getOjtRegiId());

		SkillMatrixRequest emailObj = new SkillMatrixRequest();

		setOjtRegiParam(emailObj, objRegis);

		if (emailObj.getOjtRegiId() > 0 && emailObj.getEmpId() > 0 && emailObj.getLineId() > 0
				&& emailObj.getDeptId() > 0) {
			LOGGER.info(" Inside SkillMatrixSchedular | publishOJT  | regisId: {} ", emailObj.getOjtRegiId());

			SMOJTRegis regis = new SMOJTRegis();
			regis.setId(emailObj.getOjtRegiId());
			if (emailObj.getEmpId() > 0) {
				regis.setEmpDetails(new EmployeeDetails(emailObj.getEmpId()));
			}
			regis.setBranch(new BranchMaster(emailObj.getBranchId()));

			if (emailObj.getDeptId() > 0) {
				regis.setDept(new DepartmentMaster(emailObj.getDeptId()));
			}
			regis.setDesiredSkillLevel(
					new SMSkillLevel(CommonUtils.objectToLong(objRegis.get("desiredLvlId"))));

			regis.setLine(new Line(emailObj.getLineId()));
			regis.setWorkstation(new SMWorkstations(emailObj.getWorkstationId()));
			/*
			 * save OJT registration
			 */
			saveOJTSkillingDetails(session, regis);

			if (emailObj.getEmailId() != null && emailObj.getEmailId().length() > 0) {

				/*
				 * send Email to OE and trainer
				 */
				sendEmailToOE(emailObj.getPortalLink(), mailList, emailObj, emailObj.getLogo());
				sendEmailToTrainer(emailObj.getLogo(), mailList, emailObj, emailObj.getLogo());
			} else {
				LOGGER.info("Inside SuggestionScheduler | emailId Not Found");
			}
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

		emailObj.setBranchName(CommonUtils.objectToString(objRegis.get("branchName")));
		emailObj.setBranchId(CommonUtils.objectToInt(objRegis.get("branchId")));
		emailObj.setLogo(CommonUtils.objectToString(objRegis.get("orgLogo")));
		emailObj.setPortalLink(CommonUtils.objectToString(objRegis.get("portalLink")));
	}

	private Tuple getRegisDetailsForPlan(Session session, long ojtPlanId) {
		return session.createNativeQuery(
				" select ojt.id as regisId,ojt.status as status,ojt.current_skill_level_id as currentLvlId,\r\n"
						+ " ojt.desired_skill_level_id as desiredLvlId,ojt.emp_id as empId,ojt.trainer_emp_id as trainerId,\r\n"
						+ " ojt.workstation_id as workStationId,ojt.branch_id as branchId,ojt.dept_id as deptId,\r\n"
						+ " ojt.ojt_plan_id as planId,concat(ifnull(e.first_name,''),' ',ifnull(e.last_name,'')) as empName,\r\n"
						+ " e.email_id as emailId,ojt.line_id as lineId,l.name as line,sw.workstation as workstation,"
						+ " d.dept_name as deptName,sl.level_name as level ,concat(ifnull(et.first_name,''),' ',ifnull(et.last_name,'')) as trainerName,\r\n"
						+ " et.email_id as trainerEmailId,mo.logo as orgLogo,mo.portal_link as portalLink ,"
						+ " mb.name as branchName " + " from sm_ojt_regis ojt \r\n"
						+ " left join tbl_employee_details e on e.emp_id=ojt.emp_id\r\n"
						+ " left join sm_workstations sw on sw.id=ojt.workstation_id \r\n"
						+ " left join dwm_line l on l.id=ojt.line_id "
						+ " left join master_department d on d.dept_id=ojt.dept_id "
						+ " left join sm_skill_level sl on sl.id=ojt.desired_skill_level_id "
						+ " left join tbl_employee_details et on et.emp_id=ojt.trainer_emp_id "
						+ " inner join master_branch mb on ojt.branch_id = mb.branch_id "
						+ " inner join master_organization mo on mo.org_id=mb.org_id "
						+ " where ojt.id=:ojtRegiId group by ojt.id ",
				Tuple.class).setParameter("ojtRegiId", ojtPlanId).getSingleResult();
	}

	public SkillMatrixRequest getEmployeeDetailsByRegisIdForEmail(Session session, SkillMatrixRequest request) {
		LOGGER.info("# SkillMatrixWorker || getEmployeeDetailsByRegisIdForEmail");
		TypedQuery<Tuple> query = session.createNativeQuery(
				"SELECT line.name AS lineName,md.dept_name AS deptName,smw.workstation AS workstation,now() AS assignedDate,\r\n"
						+ "concat(ifnull(ed.first_name,''),' ',ifnull(ed.last_name,'')) AS oeName, sl.level_name as level,\r\n"
						+ "ed.email_id as emailId,o.portal_link as portalLink,o.logo as logo,\r\n"
						+ "concat(ifnull(e.first_name,''),' ',ifnull(e.last_name,'')) AS createdBy,e.email_id as createdByEmail\r\n"
						+ "from sm_ojt_regis as regi \r\n"
						+ "INNER JOIN sm_workstations as smw ON smw.id=regi.workstation_id\r\n"
						+ "left JOIN master_department as md ON md.dept_id=regi.dept_id\r\n"
						+ "left JOIN dwm_line as line ON line.id=regi.line_id\r\n"
						+ "LEFT JOIN tbl_employee_details as ed ON ed.emp_id=regi.emp_id\r\n"
						+ "left join master_branch as mb on mb.branch_id=regi.branch_id\r\n"
						+ "left join master_organization as o on o.org_id=mb.org_id\r\n"
						+ "left join sm_skill_level as sl on sl.id =regi.desired_skill_level_id\r\n"
						+ "LEFT JOIN tbl_employee_details as e ON e.emp_id=regi.created_by \r\n"
						+ "WHERE regi.id=:ojtRegistId",
				Tuple.class).setParameter("ojtRegistId", request.getOjtRegiId());

		List<Tuple> objList = query.getResultList();
		if (!CollectionUtils.isEmpty(objList)) {
			Tuple obj = objList.get(0);

			request.setEmailId(CommonUtils.objectToString(obj.get("emailId")));
			request.setLineName(CommonUtils.objectToString(obj.get("lineName")));
			request.setWorkstation(CommonUtils.objectToString(obj.get("workstation")));
			request.setAssignedDate(CommonUtils.objectToString(obj.get("assignedDate")));
			request.setPortalLink(CommonUtils.objectToString(obj.get("portalLink")));
			request.setLogo(CommonUtils.objectToString(obj.get("logo")));
			request.setSkillLevel(CommonUtils.objectToString(obj.get("level")));
			request.setEmpName(CommonUtils.objectToString(obj.get("oeName")));
			request.setAssignedBy(CommonUtils.objectToString(obj.get("createdBy")));
			request.setAssignedByEmail(CommonUtils.objectToString(obj.get("createdByEmail")));
		}
		return request;
	}

	private void updateOJTSkillingAuditStatus(Session session, SkillMatrixRequest request, String status) {
		LOGGER.info("# SkillMatrixWorker || updateOJTSkillingAuditStatus ");
		session.createNativeQuery(
				"UPDATE sm_ojt_skilling_audit smsk SET updated_date=now(),smsk.status=:status WHERE smsk.id=:skillingAuditId ")
				.setParameter("skillingAuditId", request.getSkillingAuditId()).setParameter("status", status)
				.executeUpdate();
	}

	public void validateOjtPlanData(SkillMatrixRequest request, Session session) {
		List<Tuple> empList = session
				.createNativeQuery(
						"select cmpy_emp_id as companyEmpId,emp_id as empId, "
								+ " concat(ifnull(first_name,''),' ',ifnull(last_name,'')) as empName "
								+ " from tbl_employee_details where branch_id=:branchId and dept_id =:deptId ",
						Tuple.class)
				.setParameter("branchId", request.getBranchId()).setParameter("deptId", request.getDeptId())
				.getResultList();

		List<Tuple> workstationList = session.createNativeQuery(
				"select id as workstationId,workstation as workstation from sm_workstations where branch_id=:branchId and dept_id =:deptId  ",
				Tuple.class).setParameter("branchId", request.getBranchId()).setParameter("deptId", request.getDeptId())
				.getResultList();

		List<Tuple> levelList = session
				.createNativeQuery("select id as levelId,level_name as level from sm_skill_level ", Tuple.class)
				.getResultList();

		List<Tuple> checkShiltList = session.createNativeQuery(
				"select smc.id as checksheetId,smc.title as title,smc.no_of_days as noOfDays,smc.workstation_id as workstationId, "
						+ " smc.skill_level_id as levelId from  sm_checksheet smc\r\n"
						+ " where  smc.branch_id=:branchId and smc.is_active=1 "
						+ " and smc.dept_id=:deptId and smc.line_id=:lineId  ",
				Tuple.class).setParameter("branchId", request.getBranchId()).setParameter("deptId", request.getDeptId())
				.setParameter("lineId", request.getLineId()).getResultList();

		List<Tuple> assessmentList = session.createNativeQuery(
				" select id as assessmentId,skill_level_id as levelId,workstation_id as workstationId "
						+ "  from sm_assessment where is_active=1 and assessment_type='LEVEL' \r\n"
						+ " and status=:asStatus and branch_id=:branchId and dept_id=:deptId and line_id=:lineId ",
				Tuple.class).setParameter("branchId", request.getBranchId()).setParameter("deptId", request.getDeptId())
				.setParameter("lineId", request.getLineId()).setParameter("asStatus", SMConstant.ASSESSMENT_PUBLISHED)
				.getResultList();

		List<Tuple> safetyAssessmentList = session.createNativeQuery(
				" select id as assessmentId,skill_level_id as levelId,workstation_id as workstationId "
						+ "  from sm_assessment where is_active=1 and assessment_type='SAFETY' \r\n"
						+ " and status=:asStatus and branch_id=:branchId and dept_id=:deptId and line_id=:lineId ",
				Tuple.class).setParameter("branchId", request.getBranchId()).setParameter("deptId", request.getDeptId())
				.setParameter("lineId", request.getLineId()).setParameter("asStatus", SMConstant.ASSESSMENT_PUBLISHED)
				.getResultList();

		// ########################
		List<SkillMatrixRequest> errorList = new ArrayList<SkillMatrixRequest>();
		for (SMOJTPlanDTO regisObj : request.getOjtRegisList()) {
			boolean flag = false;
			StringBuilder sb = new StringBuilder();

			SkillMatrixRequest checkRequest = new SkillMatrixRequest();
			checkRequest.setBranchId(request.getBranchId());
			checkRequest.setDeptId(request.getDeptId());
			checkRequest.setLineId(request.getLineId());
			checkRequest.setWorkstationId(regisObj.getWorkstationId());
			checkRequest.setDesiredSkillLevelId(regisObj.getDesiredSkillLevelId());

			if (!CollectionUtils.isEmpty(empList)) {
				if (!CollectionUtils.isEmpty(workstationList)) {
					if (!CollectionUtils.isEmpty(levelList)) {

						if (!isAssessmentFound(assessmentList, regisObj)) {
							flag = true;
							sb.append("Level Assessment Not Found, ");
						}

						if (!(getSafetyAssessResult(session, regisObj.getEmpId()).equals(SMConstant.PASS))) {
							if (!isSafetyAssessmentFound(safetyAssessmentList, regisObj)) {
								flag = true;
								sb.append("Safety Assessment Not Found, ");
							}
						}

						if (!isChecksheetFound(checkShiltList, regisObj)) {
							flag = true;
							sb.append("Checksheet Not Found, ");
						}
					} else {
						flag = true;
						sb.append("Level Not Found, ");
					}

				} else {
					flag = true;
					sb.append("Workstion Not Found, ");
				}
			} else {
				flag = true;
				sb.append("Employee Not Found, ");
			}

			if (flag) {

				regisObj.getEmpId();
				for (Tuple emp : empList) {
					if (CommonUtils.objectToInt(emp.get("empId")) == (regisObj.getEmpId())) {
						checkRequest.setEmpName(CommonUtils.objectToString(emp.get("empName")));
						checkRequest.setCmpyEmpId(CommonUtils.objectToString(emp.get("companyEmpId")));
						checkRequest.setEmpId(regisObj.getEmpId());
					}
				}
				for (Tuple ws : workstationList) {
					if (CommonUtils.objectToInt(ws.get("workstationId")) == (regisObj.getWorkstationId())) {
						checkRequest.setWorkstation(CommonUtils.objectToString(ws.get("workstation")));
						checkRequest.setWorkflowId(regisObj.getWorkstationId());
					}
				}
				for (Tuple l : levelList) {
					if (CommonUtils.objectToInt(l.get("levelId")) == (regisObj.getDesiredSkillLevelId())) {
						checkRequest.setDesiredSkillLevelName(CommonUtils.objectToString(l.get("level")));
						checkRequest.setDesiredSkillLevelId(regisObj.getDesiredSkillLevelId());
					}
					if (CommonUtils.objectToInt(l.get("levelId")) == (regisObj.getCurrentSkillLevelId())) {
						checkRequest.setCurrentSkillLevelId(regisObj.getCurrentSkillLevelId());
						checkRequest.setCurrentSkillLevelName(CommonUtils.objectToString(l.get("level")));
					}
				}
				checkRequest.setErrorMessage(removeLastCommaFromStr(sb));
				errorList.add(checkRequest);
			}
		}

		if (!CollectionUtils.isEmpty(errorList)) {
			throw new EnovationException(errorList);
		}
	}

	private boolean isChecksheetFound(List<Tuple> checkShiltList, SMOJTPlanDTO regisObj) {
		for (Tuple ch : checkShiltList) {
			if (regisObj.getWorkstationId() == CommonUtils.objectToLong(ch.get("workstationId"))
					&& regisObj.getDesiredSkillLevelId() == CommonUtils.objectToLong(ch.get("levelId"))) {
				return true;
			}
		}
		return false;
	}

	private boolean isAssessmentFound(List<Tuple> AssessmentList, SMOJTPlanDTO regisObj) {
		return isChecksheetFound(AssessmentList, regisObj);
	}

	private boolean isSafetyAssessmentFound(List<Tuple> checkShiltList, SMOJTPlanDTO regisObj) {
		for (Tuple ch : checkShiltList) {
			if (regisObj.getWorkstationId() == CommonUtils.objectToLong(ch.get("workstationId"))) {
				return true;
			}
		}
		return false;
	}

	// removing last commat from string
	private String removeLastCommaFromStr(StringBuilder sb) {
		LOGGER.info("# Inside removeLastCommaFromStr ");
		String str = sb.toString();
		LOGGER.info("error List sb : " + str);

		// Find the last occurrence of a comma
		int lastCommaIndex = str.lastIndexOf(',');

		// Check if a comma exists in the string
		if (lastCommaIndex != -1) {
			// Remove the last comma and space
			str = str.substring(0, lastCommaIndex) + str.substring(lastCommaIndex + 1).trim();
		}
		System.out.println(str);
		return str;
	}

	private boolean checkSMAlreadyExit(SMAssessmentDTO slObj, Session session) {
		LOGGER.info("#In SkillMatrixWorker |  INSIDE in checkSMAlreadyExit ");
		List<Tuple> tupleList = session.createNativeQuery(
						"select id from sm_emp_skill_matrix where emp_id=:empId and workstation_id=:workstationId and branch_id=:branchId and dept_id=:deptId and line_id=:lineId",
						Tuple.class)
				.setParameter("empId", slObj.getEmpId())
				.setParameter("workstationId", slObj.getWorkstationId())
				.setParameter("branchId", slObj.getBranchId())
				.setParameter("deptId", slObj.getDeptId())
				.setParameter("lineId", slObj.getLineId())
				.getResultList();
		return !CollectionUtils.isEmpty(tupleList);
	}

	private void updateSMEmpSkillMatrix(Session session, SMAssessmentDTO slObj) {
		LOGGER.info("#In SkillMatrixWorker |  INSIDE in updateSMEmpSkillMatrix ");
		session.createNativeQuery(
						"update sm_emp_skill_matrix set skill_level_id=:skillLevelId where emp_id=:empId and workstation_id=:workstationId and branch_id=:branchId and dept_id=:deptId and line_id=:lineId")
				.setParameter("skillLevelId", slObj.getSkillLvlId())
				.setParameter("empId", slObj.getEmpId())
				.setParameter("workstationId", slObj.getWorkstationId())
				.setParameter("branchId", slObj.getBranchId())
				.setParameter("deptId", slObj.getDeptId())
				.setParameter("lineId", slObj.getLineId())
				.executeUpdate();
	}

	private void addSMEmpSkillMatrix(Session session, SkillMatrixRequest request, SMAssessmentDTO slObj) {
		LOGGER.info("#In SkillMatrixWorker |  INSIDE in addSMEmpSkillMatrix ");
		session.createNativeQuery(
						"insert into sm_emp_skill_matrix (emp_id,workstation_id,branch_id,dept_id,line_id,skill_level_id) values (:empId,:workstationId,:branchId,:deptId,:lineId,:skillLevelId)")
				.setParameter("empId", slObj.getEmpId())
				.setParameter("workstationId", slObj.getWorkstationId())
				.setParameter("branchId", slObj.getBranchId())
				.setParameter("deptId", slObj.getDeptId())
				.setParameter("lineId", slObj.getLineId())
				.setParameter("skillLevelId", slObj.getSkillLvlId())
				.executeUpdate();
	}

}
