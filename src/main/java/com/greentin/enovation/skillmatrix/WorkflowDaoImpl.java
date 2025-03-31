package com.greentin.enovation.skillmatrix;

import javax.persistence.Tuple;
import javax.persistence.TypedQuery;
import javax.transaction.Transactional;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

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
import com.greentin.enovation.dto.SMAssessmetDTO;
import com.greentin.enovation.dto.MailDTO;
import com.greentin.enovation.dto.SMAssessmentDTO;
import com.greentin.enovation.dto.SMEmpSkillMatrixDTO;
import com.greentin.enovation.dto.SMOJTAssessmentDTO;
import com.greentin.enovation.dto.SMOJTQuetionDTO;
import com.greentin.enovation.dto.SMOJTRegisDTO;
import com.greentin.enovation.dto.SMOJTCSPointsAuditDTO;
import com.greentin.enovation.dto.SMOJTPlanDTO;
import com.greentin.enovation.dto.SMOJTSkillingAuditDTO;
import com.greentin.enovation.dto.SMOJTSkillingDTO;
import com.greentin.enovation.dto.SMOJTSkillingParameterDTO;
import com.greentin.enovation.dto.SMShiftDTO;
import com.greentin.enovation.dto.SkillMatrixRequest;
import com.greentin.enovation.model.skillMatrix.SMAssessment;
import com.greentin.enovation.model.skillMatrix.SMAssessmentOptions;
import com.greentin.enovation.model.skillMatrix.SMAssessmentQues;
import com.greentin.enovation.model.skillMatrix.SMOJTAssessment;
import com.greentin.enovation.model.skillMatrix.SMOJTAssessmentOpt;
import com.greentin.enovation.model.skillMatrix.SMOJTAssessmentQues;
import com.greentin.enovation.model.skillMatrix.SMOJTPlan;
import com.greentin.enovation.model.skillMatrix.SMOJTRegis;
import com.greentin.enovation.model.skillMatrix.SMOJTSkilling;
import com.greentin.enovation.model.skillMatrix.SMOJTSkillingAudit;
import com.greentin.enovation.model.BranchMaster;
import com.greentin.enovation.model.DepartmentMaster;
import com.greentin.enovation.model.EmailTemplateMaster;
import com.greentin.enovation.model.EmployeeDetails;
import com.greentin.enovation.model.dwm.Line;
import com.greentin.enovation.model.skillMatrix.SMShifts;
import com.greentin.enovation.model.skillMatrix.SMSkillLevel;
import com.greentin.enovation.model.skillMatrix.SMWFDeployment;
import com.greentin.enovation.model.skillMatrix.SMWorkstations;
import com.greentin.enovation.response.SkillMatrixResponse;
import com.greentin.enovation.schedulars.SkillMatrixSchedular;
import com.greentin.enovation.utils.BaseRepository;
import com.greentin.enovation.utils.ColumnAscDescConstants;
import com.greentin.enovation.utils.CommonUtils;
import com.greentin.enovation.utils.EnovationConstants;
import com.greentin.enovation.utils.EnovationException;
import com.greentin.enovation.utils.MailUtil;
import com.itextpdf.text.pdf.PdfStructTreeController.returnType;

/**
 * @author Sonali L Aug 7, 2023
 * @desc
 */
@Component
@Transactional
public class WorkflowDaoImpl extends BaseRepository implements WorkflowIDao {
	private static final Logger LOGGER = LoggerFactory.getLogger(WorkflowDaoImpl.class);

	@Autowired
	SkillMatrixAudit smAudit;

	@Autowired
	SkillMatrixWorker smWorker;

	@Autowired
	SkillMatrixUtils smUtils;

	@Autowired
	SkillMatrixSchedular smScheduler;

	@Autowired
	EnovationConfig evonationConfig;

	@Autowired
	private ICommunication communication;

	@Autowired
	Environment env;

	@Autowired
	@Qualifier("taskExecutor")
	private TaskExecutor taskExecutor;

	@Override
	public boolean stageFiveSubmission(SkillMatrixRequest request) {
		LOGGER.info("# WorkflowDaoImpl || stageFiveSubmission");
		Session session = getCurrentSession();
		boolean flag = false;

//		SMOJTAssessment assObj = new SMOJTAssessment();
//		assObj = smUtils.getOJTAssessmentId(session, request);
		smWorker.updateSkillingAudit(session, request);
		if (!CollectionUtils.isEmpty(request.getQueList())) {

			for (SMAssessmetDTO queDto : request.getQueList()) {

				SMOJTQuetionDTO ojtAsseQue = null;
				ojtAsseQue = smUtils.getOJTAssessmentQuesId(session, queDto, request);

				if (!CollectionUtils.isEmpty(queDto.getQueOptionList())
						&& ojtAsseQue.getQueType().equals(SMConstant.QUESTION_TYPE)) {
					for (SMAssessmetDTO optionDto : queDto.getQueOptionList()) {
						smWorker.saveQueAnsCalculateMarks(session, optionDto, ojtAsseQue, request, queDto);
					}
				} else {
					smWorker.updateQuestionAnswer(session, queDto, ojtAsseQue);
				}
			}
		}
		smWorker.saveAssesmentResult(session, request);
		flag = true;

		return flag;
	}

	@Override
	public boolean ojtRegistration(SkillMatrixRequest request) {
		LOGGER.info("# WorkflowDaoImpl || ojtRegistration");
		boolean flag = false;
		Session session = getCurrentSession();

		for (SMOJTPlanDTO regisObj : request.getOjtRegisList()) {

			SMOJTRegis regis = new SMOJTRegis();

			if (request.getCreatedBy() > 0) {
				regis.setCreatedBy(regisObj.getCreatedBy());
			}
			if (regisObj.getEmpId() > 0) {
				regis.setEmpDetails(new EmployeeDetails(regisObj.getEmpId()));
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

			// Save Registration Entry
			session.save(regis);

			// Save OJTSkilling Entry
			smWorker.saveOJTSkillingDetails(session, regis);

			List<MailDTO> mailList = new ArrayList<>();

			SkillMatrixRequest emailDto = new SkillMatrixRequest();

			sendEmail(session, regis, mailList, emailDto);

		}
		flag = true;

		return flag;
	}

	private void sendEmail(Session session, SMOJTRegis regis, List<MailDTO> mailList, SkillMatrixRequest emailDto) {
		smUtils.getDataForMail(session, regis, emailDto);

		smWorker.sendEmailToRegisOE(mailList, emailDto);
		smWorker.sendEmailTooTrainer(mailList, emailDto);
		taskExecutor.execute(new MailUtil(mailList, communication));
	}

	@Override
	public boolean stageOneSubmission(SkillMatrixRequest request) {
		LOGGER.info("# WorkflowDaoImpl | stageOneSubmission");
		Session session = getCurrentSession();
		boolean flag = false;
		smWorker.updateSkillingAudit(session, request);

		smWorker.updateSkillingPointAudit(session, request);
		// Assign To Stage Two Stage
//		smWorker.assignToStage2(session, request);

		request.setStageId(SMConstant.STAGE1_ID);
		smWorker.setNextStageId(session, request);

		request.setEmpId(request.getOeEmpId());
//		 smWorker.assignNextDayPointToStage1(session, request);
		flag = true;

		return flag;
	}

	@Override
	public boolean saveStageTwoSubmission(SkillMatrixRequest request) {
		LOGGER.info("# WorkflowDaoImpl || saveStageTwoSubmission");
		boolean flag = false;
		Session session = getCurrentSession();

		smWorker.updateSkillingAudit(session, request);

		// update Points Audit Table
		smWorker.updateSkillingPointAudit(session, request);

		if (request.getStatus().equals(EnovationConstants.COMPLETED_STRING)) {

			// Assign To Verification
//			smWorker.assignToStage2Verification(session, request);
			smWorker.setNextStageId(session, request);
			flag = true;
		} else {
			request.setEmpId(request.getOeEmpId());
			smWorker.assignToStage1(session, request);
			flag = true;
		}
		return flag;
	}

	@Override
	public boolean stageTwoVerificationSubmission(SkillMatrixRequest request) {
		LOGGER.info("# WorkflowDaoImpl || stageTwoVerificationSubmission");
		boolean flag = false;
		Session session = getCurrentSession();

		smWorker.updateSkillingAudit(session, request);
		smWorker.updateSkillingPointAudit(session, request);
		smWorker.updateOJTSkillingParameter(session, request);

		if (request.getStatus().equals(EnovationConstants.COMPLETED_STRING)) {
			smWorker.setNextStageId(session, request);
//			smWorker.assignToStage3(session, request);
			flag = true;
		} else {
			request.setEmpId(request.getOeEmpId());
			smWorker.assignToStage1(session, request);
			flag = true;
		}
		return flag;
	}

	/**
	 * @author Rajdeep M.D. 16-Aug-2023
	 * @desc Stage Three Submition assing To QA
	 */
	@Override
	public boolean stageThreeSubmission(SkillMatrixRequest request) {
		LOGGER.info("# WorkflowDaoImpl | submittedByStageThree");
		Session session = getCurrentSession();

		smWorker.updateSkillingAudit(session, request);

		smWorker.updateSkillingPoint(session, request);
		// update Points Audit Table
		smWorker.updateSkillingPointAudit(session, request);

		if (request.getStatus().equals(EnovationConstants.COMPLETED_STRING)) {

			smWorker.updateSkillingChecksheetDay(session, request);
			if (smUtils.getCSPendingDaysCount(session, request) == 0) {
//				smWorker.assignToStage4(session, request);
				smWorker.setNextStageId(session, request);
			}
		} else {
			request.setEmpId(request.getOeEmpId());
			smWorker.assignToStage1(session, request);

		}
		return true;
	}

	/**
	 * @author Rajdeep M.D. 16-Aug-2023
	 * @desc Stage Four Submition assing To QA
	 */
	@Override
	public boolean stageFourSubmission(SkillMatrixRequest request) {
		LOGGER.info("# WorkflowDaoImpl | submittedByStageThree ");
		Session session = getCurrentSession();

		smWorker.updateSkillingAudit(session, request);

		if (request.getStatus().equals(EnovationConstants.COMPLETED_STRING)) {

			request.setStageId(SMConstant.STAGE4_ID);
			request.setSkillLevelId(request.getDesiredSkillLevelId());
			boolean flag = smWorker.setNextStageId(session, request);

			if (flag) {
				SMAssessmentDTO obj = smUtils.getAssessmentId(session, request);
				if (obj.getAssessmentId() == 0) {
					throw new EnovationException(SMConstant.ASSESSMENT_NOT_FOUND);
				}
				int days = smUtils.getAssessmentDays(session, request.getBranchId(), request.getDesiredSkillLevelId());
				if (days == 0) {
					throw new EnovationException(SMConstant.ASSESSMENT_DAYS_FOUND);
				}
				LocalDate currentDate = LocalDate.now();
				LOGGER.info("# currentDate:{} ", currentDate);
				LocalDate newDate = currentDate.plusDays(days);
				smWorker.updateSkillingAssessmentPending(session, request.getSkillingId(), obj.getAssessmentId(),
						smUtils.convertStringToTimestamp((newDate + " 00:00:00.0 ")));

				// stage 5 assign Assessmnet direcly
//				assignAssessmentDirectly(request, session, obj);
			} else {
				smWorker.updateOJTSkillingStatus(session, request, SMConstant.COMPLETED_STRING);
			}

		} else {
//			smUtils.setOEEmpId(request, session);
			smWorker.updateOJTSkillingStatus(session, request, SMConstant.REJECTED);
			request.setEmpId(request.getOeEmpId());
			SMOJTRegis regis = new SMOJTRegis();
			regis.setId(request.getOjtRegiId());
			regis.setEmpDetails(new EmployeeDetails(request.getEmpId()));
			regis.setBranch(new BranchMaster(request.getBranchId()));
			regis.setDesiredSkillLevel(new SMSkillLevel(request.getDesiredSkillLevelId()));
			if (request.getDeptId() > 0) {
				regis.setDept(new DepartmentMaster(request.getDeptId()));
			}
			if (request.getLineId() > 0) {
				regis.setLine(new Line(request.getLineId()));
			}
			if (request.getWorkstationId() > 0) {
				regis.setWorkstation(new SMWorkstations(request.getWorkstationId()));
			}
			regis.setCreatedBy(request.getCreatedBy());
			smWorker.saveOJTSkillingDetails(session, regis);
		}
		return true;
	}

	private void assignAssessmentDirectly(SkillMatrixRequest request, Session session, SMAssessmentDTO obj) {
		LOGGER.info("ASSESSMENT | Inside assignAssessmentDirectly");
		request.setStageId(SMConstant.STAGE5_ID);
		request.setEmpId(request.getOeEmpId());
		smWorker.saveOJTSkillingAudit(session, request);
		LOGGER.info("ASSESSMENT | Inside employeeAssessmentAssign");
		SMOJTAssessment assignment = new SMOJTAssessment();
		assignment.setOjtRegis(new SMOJTRegis(request.getOjtRegiId()));
		assignment.setAssessment(new SMAssessment(obj.getAssessmentId()));
		assignment.setSkilling(new SMOJTSkilling(request.getSkillingId()));
		assignment.setTotalMarks(obj.getTotalMark());
		assignment.setSkillingAudit(new SMOJTSkillingAudit(request.getSkillingAuditId()));
		assignment.setAssessmentStatus(EnovationConstants.PENDING_STRING);
		session.save(assignment);

		List<Tuple> tupleObj = smScheduler.fetchQuestionData(session, obj.getAssessmentId());

		for (Tuple questionTuple : tupleObj) {
			SMOJTAssessmentQues ojtAssessment = new SMOJTAssessmentQues();
			ojtAssessment.setOjtAssessment(assignment);
			ojtAssessment.setQues(new SMAssessmentQues(CommonUtils.objectToInt(questionTuple.get("questionId"))));
			session.save(ojtAssessment);
		}
	}

	@Override
	public void getOJTRegistrationDetails(long oJTRegiId, SkillMatrixResponse response) {
		LOGGER.info("# WorkflowDaoImpl || getOJTRegistrationDetails");
		Session session = getCurrentSession();
		HashMap<String, Object> data = new HashMap<>();
		StringBuilder sb = new StringBuilder(
				"select sor.id as ojtRegiId,sor.created_by as createdBy,concat(ifnull(e.first_name,''),' ',ifnull(e.last_name,'')) as oeEmpName ,\r\n"
						+ " sor.created_date as createdDate,sor.status as status,sor.current_skill_level_id as currentSkillLvlId,rln.level_name as currentSkillLevel,sor.desired_skill_level_id as desiredSkillLevelId,dln.level_name as desiredSkillLevel, \r\n"
						+ " sor.emp_id as empIdOfOE,sor.trainer_emp_id as trainerEmpId,concat(ifnull(ed.first_name,''),' ',ifnull(ed.last_name,'')) as trainerName,\r\n"
						+ " sor.workstation_id as workStationId,sw.workstation as workstation,\r\n"
						+ " sw.required_workforce as requiredWorkforce,sw.machine_index as machineIndex,sw.dept_id as deptId,ln.id as lineId ,ln.name lineName "
						+ " from sm_ojt_regis sor\r\n"
						+ " left join tbl_employee_details e on e.emp_id=sor.emp_id inner join sm_workstations sw on sw.id=sor.workstation_id\r\n"
						+ " left join tbl_employee_details ed on ed.emp_id=sor.trainer_emp_id "
						+ " left join dwm_line ln on sor.line_id "
						+ " left join sm_skill_level dln on dln.id=sor.desired_skill_level_id "
						+ " left join sm_skill_level rln on rln.id=sor.current_skill_level_id "
						+ " where sor.id=:oJTRegisId limit 1");
		TypedQuery<Tuple> query = session.createNativeQuery(sb.toString(), Tuple.class).setParameter("oJTRegisId",
				oJTRegiId);
		Tuple tupleList = query.getResultList().stream().findFirst().orElse(null);
		if (tupleList != null) {
			LOGGER.info("# tupleList not null");
			data.put("ojtRegiId", CommonUtils.objectToLong(tupleList.get("ojtRegiId")));
			data.put("createdBy", CommonUtils.objectToInt(tupleList.get("createdBy")));
			data.put("oeEmpName", CommonUtils.objectToString(tupleList.get("oeEmpName")));
			data.put("currentSkillLvlId", CommonUtils.objectToLong(tupleList.get("currentSkillLvlId")));
			data.put("desiredSkillLvlId", CommonUtils.objectToLong(tupleList.get("desiredSkillLevelId")));
			data.put("oeEmpId", CommonUtils.objectToInt(tupleList.get("empIdOfOE")));
			data.put("trainerEmpId", CommonUtils.objectToInt(tupleList.get("trainerEmpId")));
			data.put("trainerName", CommonUtils.objectToString(tupleList.get("trainerName")));
			data.put("workStationId", CommonUtils.objectToLong(tupleList.get("workStationId")));
			data.put("workstation", CommonUtils.objectToString(tupleList.get("workstation")));
			data.put("requiredWorkforce", CommonUtils.objectToDouble(tupleList.get("requiredWorkforce")));
			data.put("deptId", CommonUtils.objectToInt(tupleList.get("deptId")));

			List<Tuple> skillList = session.createNativeQuery(
					"select id as slkillingId,status as status,checksheet_id as checksheetId,"
							+ " ojt_regis_id as ojtRisId from sm_ojt_skilling where ojt_regis_id=:oJTRegisId",
					Tuple.class).setParameter("oJTRegisId", oJTRegiId).getResultList();

			if (!CollectionUtils.isEmpty(skillList)) {
				List<HashMap<Object, List<HashMap<Object, List<SMOJTSkillingAuditDTO>>>>> skillingList = new ArrayList<>();
				for (Tuple obj : skillList) {

					HashMap<Object, List<HashMap<Object, List<SMOJTSkillingAuditDTO>>>> Skilling = new HashMap<>();
					SkillMatrixRequest request = new SkillMatrixRequest();
					request.setSkillingId(CommonUtils.objectToLong(obj.get("slkillingId")));

					if (request.getSkillingId() > 0) {

						List<HashMap<Object, List<SMOJTSkillingAuditDTO>>> daywiswList = new ArrayList<>();
						List<SMOJTSkillingAuditDTO> auditList = smUtils.getAuditList(session, request);

						Map<Integer, List<SMOJTSkillingAuditDTO>> groupDayAudit = auditList.stream()
								.collect(Collectors.groupingBy(SMOJTSkillingAuditDTO::getDayNo));

						HashMap<Object, List<SMOJTSkillingAuditDTO>> tLAudit = new HashMap<>();
						HashMap<Object, List<SMOJTSkillingAuditDTO>> assessmentAudit = new HashMap<>();
						for (Map.Entry<Integer, List<SMOJTSkillingAuditDTO>> entry : groupDayAudit.entrySet()) {

							HashMap<Object, List<SMOJTSkillingAuditDTO>> dayWiseSkilling = new HashMap<>();

							for (SMOJTSkillingAuditDTO skillingAudit : entry.getValue()) {

								SkillMatrixRequest req = new SkillMatrixRequest();

								req.setSkillingAuditId(skillingAudit.getSkillingAuditId());
								req.setSkillingId(skillingAudit.getSkillingId());

								List<SMOJTCSPointsAuditDTO> auditPointList = smUtils.getSMOJTAuditPointDetails(session,
										req);
								if (!CollectionUtils.isEmpty(auditPointList)) {
									skillingAudit.setAuditPointList(auditPointList);
								}
								List<SMOJTSkillingParameterDTO> paramList = smUtils
										.getSMOJTSkillingParameterList(session, req);
								if (!CollectionUtils.isEmpty(paramList)) {
									skillingAudit.setParameterList(paramList);
								}
							}
							dayWiseSkilling.put("dayAudit", entry.getValue());
							daywiswList.add(dayWiseSkilling);
						}
						getTrainerDetail(session, request, daywiswList, tLAudit);

						getAssessmentDetails(oJTRegiId, session, request, daywiswList, assessmentAudit);

						Skilling.put("dayWiseAuditList", daywiswList);
					}
					skillingList.add(Skilling);
				}
				data.put("skillingList", skillingList);
				response.setData(data);
			}
		}

	}

	private void getAssessmentDetails(long oJTRegiId, Session session, SkillMatrixRequest request,
			List<HashMap<Object, List<SMOJTSkillingAuditDTO>>> daywiswList,
			HashMap<Object, List<SMOJTSkillingAuditDTO>> assessmentAudit) {
		Tuple assessment = session.createNativeQuery(
				"select sa.id as id, sa.actual_marks as actualMark, sa.assessment_status as assessmentStatus, sa.created_date assignDate, \r\n"
						+ "sa.percentage as percentage, sa.total_marks as totalMark,sa.skilling_id as skillingId,a.title as title,\r\n"
						+ "sa.assessment_id as assessmentId, sa.ojt_regis_id as ojtRegiId, sa.skilling_audit_id as skillingAuditId,\r\n"
						+ "CONCAT(IFNULL(e.first_name, ''), ' ', IFNULL(e.last_name, '')) AS empName\r\n"
						+ "from sm_ojt_assessment sa left join sm_assessment a on a.id=sa.assessment_id \r\n"
						+ " inner join sm_ojt_regis sor on sor.id=sa.ojt_regis_id \r\n"
						+ " INNER JOIN tbl_employee_details e ON e.emp_id = sor.emp_id "
						+ " where sa.skilling_id=:skillingId and sa.ojt_regis_id=:oJTRegisId limit 1 ",
				Tuple.class).setParameter("skillingId", request.getSkillingId()).setParameter("oJTRegisId", oJTRegiId)
				.getResultList().stream().findFirst().orElse(null);
		if (assessment != null) {
			List<SMOJTSkillingAuditDTO> tlalist = new ArrayList<>();
			SMOJTSkillingAuditDTO tla = new SMOJTSkillingAuditDTO();
			tla.setActualMark(CommonUtils.objectToDouble(assessment.get("actualMark")));
			tla.setAssignedDate(CommonUtils.objectToString(assessment.get("assignDate")));
			tla.setPercentage(CommonUtils.objectToDouble(assessment.get("percentage")));
			tla.setTotalMark(CommonUtils.objectToDouble(assessment.get("totalMark")));
			tla.setTitle(CommonUtils.objectToString(assessment.get("title")));
			tla.setAssessmentId(CommonUtils.objectToLong(assessment.get("assessmentId")));
			tla.setSkillingId(CommonUtils.objectToLong(assessment.get("skillingId")));
			tla.setStatus(CommonUtils.objectToString(assessment.get("assessmentStatus")));
			tla.setSkillingAuditId(CommonUtils.objectToLong(assessment.get("skillingAuditId")));
			tla.setOjtRegiId(CommonUtils.objectToLong(assessment.get("ojtRegiId")));
			tla.setEmpName(CommonUtils.objectToString(assessment.get("empName")));
//							tla.setStage(CommonUtils.objectToString(assessment.get("stage")));
//							tla.setEmpName(CommonUtils.objectToString(assessment.get("empName")));
			tlalist.add(tla);
			assessmentAudit.put("assessmentAudit", tlalist);
			daywiswList.add(assessmentAudit);
		}
	}

	private void getTrainerDetail(Session session, SkillMatrixRequest request,
			List<HashMap<Object, List<SMOJTSkillingAuditDTO>>> daywiswList,
			HashMap<Object, List<SMOJTSkillingAuditDTO>> tLAudit) {
		Tuple tl = session.createNativeQuery(
				"select a.id as auditId,a.status as status, a.emp_id as empId ,a.skilling_id as skillingId,a.stage_id as stageId,\r\n"
						+ "(case when sl.stage_label is null then sg.stage_name else sl.stage_label end) stage,\r\n"
						+ "concat(ifnull(e.first_name,''),' ',ifnull(e.last_name,'')) as empName,a.comment as comment \r\n"
						+ "from sm_ojt_skilling_audit a left join sm_stage sg on sg.id=a.stage_id \r\n"
						+ "left join sm_stage_label sl on sl.stage_id= sg.id\r\n"
						+ "left join tbl_employee_details e on e.emp_id=a.emp_id where a.skilling_id=:skillingId and a.stage_id=:stageId limit 1",
				Tuple.class).setParameter("skillingId", request.getSkillingId())
				.setParameter("stageId", SMConstant.STAGE4_ID).getResultList().stream().findFirst().orElse(null);
		if (tl != null) {
			List<SMOJTSkillingAuditDTO> tlalist = new ArrayList<>();
			SMOJTSkillingAuditDTO tla = new SMOJTSkillingAuditDTO();
			tla.setSkillingId(CommonUtils.objectToLong(tl.get("skillingId")));
			tla.setStatus(CommonUtils.objectToString(tl.get("status")));
			tla.setSkillingAuditId(CommonUtils.objectToLong(tl.get("auditId")));
			tla.setStageId(CommonUtils.objectToLong(tl.get("stageId")));
			tla.setStage(CommonUtils.objectToString(tl.get("stage")));
			tla.setEmpName(CommonUtils.objectToString(tl.get("empName")));
			tla.setComment(CommonUtils.objectToString(tl.get("comment")));
			tlalist.add(tla);
			tLAudit.put("tlAudit", tlalist);
			daywiswList.add(tLAudit);
		}
	}

	public boolean getMyActionList(SkillMatrixRequest request, SkillMatrixResponse response) {
		LOGGER.info("# WorkflowDaoImpl || getMyPendingList ");
		Session session = getCurrentSession();
		boolean flag = false;
		if (request.getOffset() == 0) {
			response.setTotalCount(getMyActionCount(session, request));
		}

		StringBuilder sb = new StringBuilder(
				"select ojtsa.id as auditId,mb.name as branchName,md.dept_name as deptName,"
						+ " case when (ojtsa.start_date is NOT null and ojtsa.stage_id=1 ) then ojtsa.start_date else ojtsa.created_date end  as activityDate,"
						+ " ojtr.id as regisId,smw.workstation as workstation,(case when sml.stage_label is null then stage.stage_name else sml.stage_label end ) as activity,ojtsa.status as status,slvl.level_name as levelName,ojtsa.emp_id as empId,\r\n"
						+ " concat(IFNULL(ed.first_name,''),' ',IFNULL(ed.last_name,'')) as empName,ed.cmpy_emp_id as cmpyEmpId,ojtsa.updated_date as completedDate,stage.id as stageId,\r\n"
						+ " stage.stage_caption asstageCaption,(case when sml.stage_label is null then stage.stage_name else sml.stage_label end ) as stageName,\r\n"
						+ " ojtsa.skilling_id as skillingId,ojtr.branch_id as branchId,\r\n"
						+ " ojtr.dept_id as deptId,mb.org_id as orgId,ojtsc.day_no as dayNo,ojtsa.start_date as startDate,\r\n"
						+ " (case when smojta.id > 0 then smojta.id \r\n"
						+ " else smOJTAsses.id end )as oJTAssessmentId,\r\n"
						+ " l.id as lineId ,l.name as lineName,(case when smojta.skilling_id  > 0 then 'Level' else sma.assessment_type end) as assessmentType "
						+ " from sm_ojt_skilling_audit ojtsa\r\n"
						+ " left join sm_ojt_skilling_checksheet ojtsc on ojtsc.id=ojtsa.skilling_checksheet_id\r\n"
						+ " left join sm_ojt_skilling ojts on ojts.id=ojtsa.skilling_id\r\n"
						+ " inner join sm_ojt_regis ojtr on ojtr.id=ojtsa.ojt_regis_id\r\n"
						+ " left join sm_checksheet smc on smc.id=ojts.checksheet_id\r\n"
						+ " inner join master_branch mb on mb.branch_id=ojtr.branch_id\r\n"
						+ " inner join master_department md on md.dept_id=ojtr.dept_id\r\n"
						+ " left join sm_workstations smw on smw.id=ojtr.workstation_id\r\n"
						+ " left join sm_stage stage on stage.id=ojtsa.stage_id\r\n"
						+ " left join sm_skill_level slvl on slvl.id=ojtr.desired_skill_level_id\r\n"
						+ " left join tbl_employee_details ed on ed.emp_id=ojtr.emp_id \r\n"
						+ " left join sm_ojt_assessment smojta on ojtr.id = smojta.ojt_regis_id and ojts.id=smojta.skilling_id \r\n"
						+ " left join sm_stage_label sml on sml.stage_id=stage.id and sml.branch_id=ojtr.branch_id \r\n"
						+ " inner join dwm_line l on l.id=ojtr.line_id\r\n"
						+ " left join sm_ojt_assessment smOJTAsses on ojtr.id = smOJTAsses.ojt_regis_id\r\n"
						+ " left JOIN sm_assessment sma ON smOJTAsses.assessment_id=sma.id"
						+ " where ojtsa.emp_id=:empId ");

		if (request.getBranchId() > 0) {
			sb.append(" and ojtr.branch_id={branchId} ");
		}
		if (request.getDeptId() > 0) {
			sb.append("  and ojtr.dept_id={deptId} ");
		}
		if (request.getLineId() > 0) {
			sb.append("  and ojtr.line_id ={lineId} ");
		}
		if (!CollectionUtils.isEmpty(request.getLineIds())) {
			sb.append(" and ojtr.line_id in (:lineIds) ");
		}

		if (request.getStatus() != null && request.getStatus().equals("PENDING")) {
			sb.append(" and ojtsa.status = 'PENDING' ");
		} else {
			sb.append(" and ojtsa.status <> 'PENDING' ");
		}

		if (request.getSearch() != null) {
			sb.append(" and (smw.workstation like '%{search}%' or stage.stage_name like '%{search}%'"
					+ " or concat(IFNULL(ed.first_name,''),' ',IFNULL(ed.last_name,'')) like '%{search}%' "
					+ " or slvl.level_name like '%{search}%' or ojtsc.day_no like '{search}' or ojtsa.status like '{search}' or l.name like '%{search}%' "
					+ " or (case when sml.stage_label is null then stage.stage_name else sml.stage_label end ) like '{search}' )");
		}

		if (request.getFromDt() != null && request.getToDt() != null) {
			sb.append(" and ojtsa.updated_date between '{fromDate} 00:00:00' and '{toDate} 23:59:59' ");
		}
		sb.append(" group by ojtsa.id  ");

		if (request.getColName() != null && request.getOrderType() != null) {
			sb.append(" order by " + ColumnAscDescConstants.pendingColHeder.get(request.getColName()) + " "
					+ request.getOrderType());
		} else {
			sb.append(" order by ojtsa.id DESC ");
		}

		String tmpQuery = (sb.toString()).replace("{fromDate}", String.valueOf(request.getFromDt()))
				.replace("{toDate}", String.valueOf(request.getToDt()))
				.replace("{search}", String.valueOf(request.getSearch()))

				.replace("{branchId}", String.valueOf(request.getBranchId()))
				.replace("{deptId}", String.valueOf(request.getDeptId()))
				.replace("{lineId}", String.valueOf(request.getLineId()))
				.replace("{status}", String.valueOf(request.getStatus()));

		TypedQuery<Tuple> query = session.createNativeQuery(tmpQuery, Tuple.class)
//				.setParameter("assessmentType", SMConstant.SAFETY_ASSESSMENT)
				.setParameter("empId", request.getEmpId());

		if (!CollectionUtils.isEmpty(request.getLineIds())) {
			query.setParameter("lineIds", request.getLineIds());
		}

		if (request.getLimit() > 0) {
			query.setFirstResult(request.getOffset()).setMaxResults(request.getLimit());
		}

		List<Tuple> tupleList = query.getResultList();

		LOGGER.info("# tupleList size : {}", tupleList.size());
		if (!CollectionUtils.isEmpty(tupleList)) {
			response.setDataList(CommonUtils.parseTupleList(tupleList));
			flag = true;
		}

		return flag;
	}

	private int getMyActionCount(Session session, SkillMatrixRequest request) {
		LOGGER.info("# WorkflowDaoImpl || getMyActionCount ");
		StringBuilder sb = new StringBuilder("select ojtsa.id as totalCount " + " from sm_ojt_skilling_audit ojtsa\r\n"
				+ " left join sm_ojt_skilling_checksheet ojtsc on ojtsc.id=ojtsa.skilling_checksheet_id\r\n"
				+ " inner join sm_ojt_skilling ojts on ojts.id=ojtsa.skilling_id\r\n"
				+ " inner join sm_ojt_regis ojtr on ojtr.id=ojts.ojt_regis_id\r\n"
				+ " left join sm_checksheet smc on smc.id=ojts.checksheet_id\r\n"
				+ " inner join master_branch mb on mb.branch_id=ojtr.branch_id\r\n"
				+ " inner join master_department md on md.dept_id=ojtr.dept_id\r\n"
				+ " left join sm_workstations smw on smw.id=ojtr.workstation_id\r\n"
				+ " left join sm_stage stage on stage.id=ojtsa.stage_id\r\n"
				+ " left join sm_skill_level slvl on slvl.id=ojtr.desired_skill_level_id"
				+ " left join tbl_employee_details ed on ed.emp_id=ojtr.emp_id "
				+ " left join sm_ojt_assessment smojta on ojtr.id = smojta.ojt_regis_id and ojts.id=smojta.skilling_id "
				+ " left join sm_stage_label sml on sml.stage_id=stage.id AND sml.branch_id=ojtr.branch_id "
				+ "  inner join dwm_line l on l.id=ojtr.line_id " + " where ojtsa.emp_id=:empId ");

		if (request.getBranchId() > 0) {
			sb.append(" and ojtr.branch_id={branchId} ");
		}
		if (request.getDeptId() > 0) {
			sb.append("  and ojtr.dept_id={deptId} ");
		}
		if (request.getLineId() > 0) {
			sb.append("  and ojtr.line_id ={lineId} ");
		}
		if (!CollectionUtils.isEmpty(request.getLineIds())) {
			sb.append(" and ojtr.line_id in (:lineIds) ");
		}
		if (request.getStatus() != null && request.getStatus().equals("PENDING")) {
			sb.append(" and ojtsa.status = 'PENDING' ");
		} else {
			sb.append(" and ojtsa.status <> 'PENDING' ");
		}

		if (request.getSearch() != null) {
			sb.append(" and (smw.workstation like '%{search}%' or stage.stage_name like '%{search}%'"
					+ " or concat(IFNULL(ed.first_name,''),' ',IFNULL(ed.last_name,'')) like '%{search}%' "
					+ " or slvl.level_name like '%{search}%' or ojtsc.day_no like '{search}' or ojtsa.status like '{search}' or l.name like '%{search}%' "
					+ " or (case when sml.stage_label is null then stage.stage_name else sml.stage_label end ) like '{search}' )");
		}

		if (request.getFromDt() != null && request.getToDt() != null) {
			sb.append(" and ojtsa.updated_date between '{fromDate} 00:00:00' and '{toDate} 23:59:59' ");
		}
		sb.append(" group by ojtsa.id  ");

		String tmpQuery = (sb.toString()).replace("{fromDate}", String.valueOf(request.getFromDt()))
				.replace("{toDate}", String.valueOf(request.getToDt()))
				.replace("{search}", String.valueOf(request.getSearch()))

				.replace("{branchId}", String.valueOf(request.getBranchId()))
				.replace("{deptId}", String.valueOf(request.getDeptId()))
				.replace("{lineId}", String.valueOf(request.getLineId()))
				.replace("{status}", String.valueOf(request.getStatus()));

		TypedQuery<Tuple> query = session.createNativeQuery(tmpQuery, Tuple.class).setParameter("empId",
				request.getEmpId());

		if (!CollectionUtils.isEmpty(request.getLineIds())) {
			query.setParameter("lineIds", request.getLineIds());
		}
		List<Tuple> objList = query.getResultList();

		if (!CollectionUtils.isEmpty(objList)) {
			return CommonUtils.objectToInt(objList.size());
		}
		return 0;
	}

	public SMAssessmentDTO getAssignedAssessmentDetails(long assessmentId) {
		LOGGER.info("# WorkflowDaoImpl || getAssignedAssessmentDetails");
		Session session = getCurrentSession();
		SMAssessmentDTO data = new SMAssessmentDTO();

		data = smUtils.assessmentDetails(session, assessmentId, data);

		TypedQuery<Tuple> query = smUtils.getAssessmentOptionList(assessmentId, session);
		List<Tuple> tupleList = query.getResultList();
		System.out.println("size : " + tupleList.size());
		List<SkillMatrixRequest> dtoList = new ArrayList<>();
		smUtils.setDtoParameters(tupleList, dtoList);

//		Map<Long, List<SkillMatrixRequest>> groupedByQuetionId = dtoList.stream()
//				.collect(Collectors.groupingBy(SkillMatrixRequest::getAssessmentQueId));
		
		Map<Long, List<SkillMatrixRequest>> groupedByQuetionId = dtoList.stream()
			    .collect(Collectors.groupingBy(
			        SkillMatrixRequest::getAssessmentQueId,
			        LinkedHashMap::new,              // Ensures insertion order is preserved
			        Collectors.toList()
			    ));

		smWorker.groupingQueOptList(data, groupedByQuetionId, session);
		return data;
	}

	// changed By rajdeep 2024-02-13 arks, percentage added
	@Override
	public HashMap<String, Object> getSkillMatrixList(SkillMatrixRequest request) {

		Session session = getCurrentSession();
		HashMap<String, Object> response = null;
		List<HashMap<String, Object>> dataList = new ArrayList<>();
		List<HashMap<String, Object>> columnList = new ArrayList<>();
		List<HashMap<String, Object>> levelList = new ArrayList<>();
		HashMap<String, Object> skillMatrixDetails = smUtils.getskillMatrixDetails(session, request.getBranchId(),
				request.getDeptId(), request.getLineId());
		
		// get Workstation List of line
		List<SMWorkstations> workstationList = smUtils.getWorkstationList(session, request.getBranchId(),
				request.getDeptId(), request.getLineId());

		// get skilled employee details
		StringBuilder builder = new StringBuilder(
				"select matrix.emp_id as empId, matrix.workstations_id as workstationId,"
						+ " matrix.skill_level_id as skillLevelId,level.level_name as levelName "
						+ " ,concat(ifnull(e.first_name,''),' ',ifnull(e.last_name,'')) as empName,e.gender as gender,\r\n"
						+ " e.doj as doj,ifnull((TIMESTAMPDIFF(YEAR, e.doj, CURDATE()))+round((TIMESTAMPDIFF(month, e.doj, CURDATE())%12/100),2),0) as experience,"
						+ " (case when mst.user_type_caption is NOT NULL then  mst.user_type_caption else eh.level_name end) as empLevel,e.cmpy_emp_id as cmpyEmpId "
						+ " from sm_emp_skill_matrix matrix "
						+ " inner join sm_workstations wrks on matrix.workstations_id=wrks.id"
						+ " inner join sm_skill_level level on level.id=matrix.skill_level_id"
						+ " inner join master_department  dept  on  dept.dept_id=wrks.dept_id "
						+ " inner join tbl_employee_details e on e.emp_id= matrix.emp_id "
						+ " left join employee_hierarchy eh on e.emp_level_id=eh.emp_lvl_id "
						+ " left join sm_user_type st on st.emp_id=e.emp_id and st.user_type_id=:userType "
						+ " left join sm_master_user_type mst on mst.id=st.user_type_id \r\n"
						+ " where dept.branch_id=:branchId  and e.is_deactive=0  and wrks.is_active=1 ");

		if (request.getDeptId() > 0) {
			builder.append(" and wrks.dept_id=:deptId ");
		}
		if (request.getLineId() > 0) {
			builder.append(" and wrks.line_id=:lineId ");
		}
		if (request.getEmpId() > 0) {
			builder.append(" and matrix.emp_id=:empId ");
		}
		builder.append(" GROUP by matrix.id order by experience desc ");

		TypedQuery<Tuple> query = session.createNativeQuery(builder.toString(), Tuple.class)
				.setParameter("branchId", request.getBranchId()).setParameter("userType", SMConstant.TL_USER_TYPE_ID);

		if (request.getDeptId() > 0) {
			query.setParameter("deptId", request.getDeptId());
		}
		if (request.getLineId() > 0) {
			query.setParameter("lineId", request.getLineId());
		}
		if (request.getEmpId() > 0) {
			query.setParameter("empId", request.getEmpId());
		}
		List<Tuple> objList = query.getResultList();

		if (!CollectionUtils.isEmpty(objList)) {
			response = new HashMap<>();
			List<SMEmpSkillMatrixDTO> matrixList = new ArrayList<>();
			double totalWorkSt = 0;
			if (!CollectionUtils.isEmpty(workstationList)) {
				totalWorkSt = workstationList.size();
			}
			for (Tuple obj : objList) {
				SMEmpSkillMatrixDTO matrix = new SMEmpSkillMatrixDTO();
				matrix.setEmpId(CommonUtils.objectToInt(obj.get("empId")));
				matrix.setWorkstationId(CommonUtils.objectToLong(obj.get("workstationId")));
				matrix.setSkillLevelId(CommonUtils.objectToLong(obj.get("skillLevelId")));
				matrix.setLevelName(CommonUtils.objectToString(obj.get("levelName")));
				matrix.setEmpName(CommonUtils.objectToString(obj.get("empName")));
				matrix.setGender(CommonUtils.objectToString(obj.get("gender")));
				matrix.setDoj(CommonUtils.objectToString(obj.get("doj")));
				matrix.setExperience(CommonUtils.objectToDouble(obj.get("experience")));
				matrix.setEmplevel(CommonUtils.objectToString(obj.get("empLevel")));
				matrix.setCmpyEmpId(CommonUtils.objectToString(obj.get("cmpyEmpId")));
				matrixList.add(matrix);
			}

			int l1Count = 0, l2Count = 0, l3Count = 0, l4Count = 0;
			int l1ActuCount = 0, l2ActuCount = 0, l3ActuCount = 0, l4ActuCount = 0;

			List<HashMap<String, Object>> workLvl = new ArrayList<>();
			for (SMWorkstations wst : workstationList) {
				if (!CollectionUtils.isEmpty(matrixList)) {
					HashMap<String, Object> wh = new HashMap<>();
					int count = 0;
					for (SMEmpSkillMatrixDTO obj : matrixList) {
						if (wst.getId() > 0 && obj.getWorkstationId() > 0 && wst.getId() == obj.getWorkstationId()
								&& wst.getReqSkillLevel() != null
								&& wst.getReqSkillLevel().getId() <= obj.getSkillLevelId()) {
							if (obj.getEmplevel() != null && obj.getEmplevel().equals(SMConstant.TL)) {
								LOGGER.info("TL Found");
							} else {
								count = count + SMConstant.ONE;
							}

						}
					}
					LOGGER.info("WorkstatuionId: " + wst.getId() + " count: " + count + " levelId: "
							+ wst.getReqSkillLevel().getId());
					wh.put("workstation", wst.getWorkstation());
					wh.put("workstationId", wst.getId());
					wh.put("level", wst.getReqSkillLevel() != null ? wst.getReqSkillLevel().getId() : 0);
					wh.put("totalCount", count);
					workLvl.add(wh);

					if (wst.getReqSkillLevel() != null && wst.getReqSkillLevel().getId() == SMConstant.L1_ID) {
						l1Count = l1Count + count;
//						l1ActuCount = l1ActuCount + SMConstant.ONE;
						l1ActuCount = l1ActuCount + wst.getMachineCount();

					} else if (wst.getReqSkillLevel() != null && wst.getReqSkillLevel().getId() == SMConstant.L2_ID) {
						l2Count = l2Count + count;
						l2ActuCount = l2ActuCount + wst.getMachineCount();
					} else if (wst.getReqSkillLevel() != null && wst.getReqSkillLevel().getId() == SMConstant.L3_ID) {
						l3Count = l3Count + count;
						l3ActuCount = l3ActuCount + wst.getMachineCount();
					} else if (wst.getReqSkillLevel() != null && wst.getReqSkillLevel().getId() == SMConstant.L4_ID) {
						l4Count = l4Count + count;
						l4ActuCount = l4ActuCount + wst.getMachineCount();
					}
				}
			}

			setLevelSummary(request, levelList, l1Count, l1ActuCount, SMConstant.L1_ID, SMConstant.L1_SKILL_LEVEL);
			setLevelSummary(request, levelList, l2Count, l2ActuCount, SMConstant.L2_ID, SMConstant.L2_SKILL_LEVEL);
			setLevelSummary(request, levelList, l3Count, l3ActuCount, SMConstant.L3_ID, SMConstant.L3_SKILL_LEVEL);
			setLevelSummary(request, levelList, l4Count, l4ActuCount, SMConstant.L4_ID, SMConstant.L4_SKILL_LEVEL);

			response.put("levelSummary", levelList);

			response.put("workstationLvlCount", workLvl);

			Map<Integer, List<SMEmpSkillMatrixDTO>> groupEmpSM = matrixList.stream()
					.collect(Collectors.groupingBy(SMEmpSkillMatrixDTO::getEmpId));

			for (Map.Entry<Integer, List<SMEmpSkillMatrixDTO>> entry : groupEmpSM.entrySet()) {
				HashMap<String, Object> map = new HashMap<>();
				map.put("empId", entry.getValue().get(0).getCmpyEmpId());
				map.put("empName", entry.getValue().get(0).getEmpName());
				map.put("gender", entry.getValue().get(0).getGender());
				map.put("doj", entry.getValue().get(0).getDoj());
				map.put("experience", entry.getValue().get(0).getExperience());
				map.put("empLevel", entry.getValue().get(0).getEmplevel());
				int marks = 0;
				double skillingPer = 0;
				double skillinfCOunt = 0;
				String skillLevel = "";
				long lvlId = 0;
				for (SMWorkstations workstation : workstationList) {

					SMOJTPlanDTO dto = new SMOJTPlanDTO();
					getSkillLevel(entry.getValue(), workstation.getId(), dto);
					dto.setRequireSkillLevel(workstation.getReqSkillLevel().getLevelName());
					dto.setRequireSkillLevelId(workstation.getReqSkillLevel().getId());

					if (dto.getRequireSkillLevelId() > 0 && dto.getCurrentSkillLevelId() > 0
							&& dto.getRequireSkillLevelId() <= dto.getCurrentSkillLevelId()) {
						skillinfCOunt = skillinfCOunt + 1;
					}
					if (dto.getCurrentSkillLevelId() > 0 && lvlId < dto.getCurrentSkillLevelId()) {
						lvlId = dto.getCurrentSkillLevelId();
						skillLevel = dto.getCurrentSkillLevel();
					}

					map.put(workstation.getWorkstation(), dto);
					marks = calculateMarks(dto, marks);
					LOGGER.info("skillinfCOunt: " + skillinfCOunt);
//					dto.setCurrentSkillLevelId(CommonUtils.objectToLong(getSkillLevel(entry.getValue(), workstation.getReqSkillLevel().getId())));
//					map.put(workstation.getWorkstation(), getSkillLevel(entry.getValue(), workstation.getId()));
				}

				if (skillinfCOunt > 0 && totalWorkSt > 0) {
					skillingPer = (Math.round((skillinfCOunt / totalWorkSt) * 100));
				}

				LOGGER.info("skillingPer: " + skillingPer);
				LOGGER.info("skillinfCOunt: " + skillinfCOunt + " totalWorkSt: " + totalWorkSt);
				map.put("marks", marks);
				map.put("skillLevel", skillLevel);
				map.put("skillingPer", skillingPer);
				dataList.add(map);
			}
			for (int i = 0; i < SMConstant.COLUMNS_SM_FIELDS.length; i++) {
				HashMap<String, Object> empIdMap = new HashMap<>();
				empIdMap.put("field", SMConstant.COLUMNS_SM_FIELDS[i]);
				empIdMap.put("heading", SMConstant.COLUMNS_SM_HEADING[i]);
				columnList.add(empIdMap);
			}

			for (SMWorkstations workstation : workstationList) {
				HashMap<String, Object> columnMap = new HashMap<>();
				columnMap.put("heading", workstation.getWorkstation());
				columnMap.put("field", workstation.getWorkstation());
				columnMap.put("levelId", workstation.getReqSkillLevel().getId());
				columnMap.put("levelName", workstation.getReqSkillLevel().getLevelName());
				columnMap.put("machineIndex", workstation.getMachineIndex());
				columnMap.put("requiredWorkforce",
						request.getShiftNo() > 0
								? Math.round(request.getShiftNo() * workstation.getMachineCount() * SMConstant.REQUIRED_NO_TRAINED * 100.0) / 100.0
								: 1 * workstation.getMachineCount() * SMConstant.REQUIRED_NO_TRAINED);
				columnList.add(columnMap);
			}

			/*
			 * Level Summary Required and Actual Count
			 */
//			String qur = "";
//			StringBuilder levelBuilder = new StringBuilder(
//					"select reqData.levelId as levelId,reqData.levelName as levelName,reqData.requiredCount as requiredCount,actualData.actualCount as actualCount from"
//							+ " (   SELECT sw.req_skill_level_id as levelId,level.level_name as levelName,sum(sw.required_workforce) as requiredCount "
//							+ "    FROM sm_workstations sw inner join sm_skill_level level on sw.req_skill_level_id=level.id where sw.branch_id=:branchId "
//							+ " {query} group by sw.req_skill_level_id ) reqData " + " left join "
//							+ "(  SELECT emp.skill_level_id as levelId,count(emp.emp_id)as actualCount "
//							+ "   FROM sm_emp_skill_matrix emp INNER join sm_workstations work on emp.workstations_id=work.id "
//							+ "   inner join tbl_employee_details e on e.emp_id=emp.emp_id and e.is_deactive=0 "
//							+ "   where work.branch_id=:branchId ");
//
//			if (request.getDeptId() > 0) {
//				qur += " and sw.dept_id=:deptId ";
//				levelBuilder.append(" and work.dept_id=:deptId ");
//			}
//			if (request.getLineId() > 0) {
//				qur += " and sw.line_id=:lineId ";
//				levelBuilder.append(" and work.line_id=:lineId ");
//			}
//			if (request.getEmpId() > 0) {
//				levelBuilder.append(" and emp.emp_id=:empId ");
//			}
//			levelBuilder.append(" GROUP BY emp.skill_level_id order by emp.skill_level_id )actualData "
//					+ " on (reqData.levelId=actualData.levelId) ");
//
//			String tmpQuery = (levelBuilder.toString()).replace("{query}", qur);
//
//			TypedQuery<Tuple> levelQuery = session.createNativeQuery(tmpQuery, Tuple.class).setParameter("branchId",
//					request.getBranchId());
//
//			if (request.getDeptId() > 0) {
//				levelQuery.setParameter("deptId", request.getDeptId());
//			}
//			if (request.getLineId() > 0) {
//				levelQuery.setParameter("lineId", request.getLineId());
//			}
//			if (request.getEmpId() > 0) {
//				levelQuery.setParameter("empId", request.getEmpId());
//			}
//
//			List<Tuple> levelObjList = levelQuery.getResultList();
//
//			if (!CollectionUtils.isEmpty(levelObjList)) {
//				for (Tuple obj : levelObjList) {
//					HashMap<String, Object> levelMap = new HashMap<>();
//					levelMap.put("levelId", CommonUtils.objectToLong(obj.get("levelId")));
//					levelMap.put("levelName", CommonUtils.objectToString(obj.get("levelName")));
//					levelMap.put("requiredCount", CommonUtils.objectToDouble(obj.get("requiredCount")));
//					levelMap.put("actualCount", CommonUtils.objectToInt(obj.get("actualCount")));
//					levelList.add(levelMap);
//				}
//			}

			Tuple tupleList = smUtils.getDocNameByBranchId(request, session);
			if (tupleList != null) {
				response.put("documentName", CommonUtils.parseSingleTupleRecord(tupleList));

			}
			response.put("skillMatrixDetails", skillMatrixDetails);
			response.put("columns", columnList);
			response.put("tableData", dataList);
//			response.put("levelSummary", levelList);
		}
		return response;
	}

	// changed BY rajdeep 2024-04-27 workstion skill count calculation changed
	private void setLevelSummary(SkillMatrixRequest request, List<HashMap<String, Object>> levelList, int l1Count,
			int l1ActuCount, int leveld, String skillLevel) {
		LOGGER.info("l1ActuCount: " + l1ActuCount + " Level: " + skillLevel + " actual count: " + l1Count);
		HashMap<String, Object> levelMap = new HashMap<>();
		levelMap.put("levelId", leveld);
		levelMap.put("levelName", skillLevel);
		levelMap.put("requiredCount",
				(int) Math.ceil(
						request.getShiftNo() > 0 ? request.getShiftNo() * l1ActuCount * SMConstant.REQUIRED_NO_TRAINED
								: 1 * l1ActuCount * SMConstant.REQUIRED_NO_TRAINED));
		levelMap.put("actualCount", l1Count);
		levelList.add(levelMap);
	}

	private int calculateMarks(SMOJTPlanDTO dto, int marks) {
		System.out.println("Skill Level " + dto.getCurrentSkillLevel());
		if (dto.getCurrentSkillLevel() != null) {
			if (dto.getCurrentSkillLevel().equals(SMConstant.L1_SKILL_LEVEL)) {
				marks = marks + SMConstant.L1_SKILL_LEVEL_MARK;
			} else if (dto.getCurrentSkillLevel().equals(SMConstant.L2_SKILL_LEVEL)) {
				marks = marks + SMConstant.L2_SKILL_LEVEL_MARK;
			} else if (dto.getCurrentSkillLevel().equals(SMConstant.L3_SKILL_LEVEL)) {
				marks = marks + SMConstant.L3_SKILL_LEVEL_MARK;
			} else if (dto.getCurrentSkillLevel().equals(SMConstant.L4_SKILL_LEVEL)) {
				marks = marks + SMConstant.L4_SKILL_LEVEL_MARK;
			}
		}
		return marks;
	}

	private String getSkillLevel(List<SMEmpSkillMatrixDTO> smList, long workstationId, SMOJTPlanDTO dto) {

		for (SMEmpSkillMatrixDTO matrix : smList) {
			if (matrix.getWorkstationId() == workstationId) {
				dto.setCurrentSkillLevel(CommonUtils.objectToString(matrix.getLevelName()));
				dto.setCurrentSkillLevelId(CommonUtils.objectToLong(matrix.getSkillLevelId()));
				return "";
			}
		}
		return null;
	}

	@Override
	public void getMyActionDetails(SkillMatrixRequest request, SkillMatrixResponse response) {
		LOGGER.info("# WorkflowDaoImpl || getMyActionDetails");
		Session session = getCurrentSession();

		if (request.getStageId() == SMConstant.STAGE4_ID) {

			List<HashMap<Object, List<SMOJTSkillingAuditDTO>>> daywiswList = new ArrayList<>();
			List<SMOJTSkillingAuditDTO> auditList = smUtils.getAuditList(session, request);

			Map<Integer, List<SMOJTSkillingAuditDTO>> groupDayAudit = auditList.stream()
					.collect(Collectors.groupingBy(SMOJTSkillingAuditDTO::getDayNo));

			for (Map.Entry<Integer, List<SMOJTSkillingAuditDTO>> entry : groupDayAudit.entrySet()) {

				HashMap<Object, List<SMOJTSkillingAuditDTO>> dayWiseSkilling = new HashMap<>();

				for (SMOJTSkillingAuditDTO skillingAudit : entry.getValue()) {

					SkillMatrixRequest req = new SkillMatrixRequest();

					req.setSkillingAuditId(skillingAudit.getSkillingAuditId());
					req.setSkillingId(skillingAudit.getSkillingId());

					List<SMOJTCSPointsAuditDTO> auditPointList = smUtils.getSMOJTAuditPointDetails(session, req);
					if (!CollectionUtils.isEmpty(auditPointList)) {
						skillingAudit.setAuditPointList(auditPointList);
					}
					List<SMOJTSkillingParameterDTO> paramList = smUtils.getSMOJTSkillingParameterList(session, req);
					if (!CollectionUtils.isEmpty(paramList)) {
						skillingAudit.setParameterList(paramList);
					}
				}
				dayWiseSkilling.put("dayAudit", entry.getValue());
				daywiswList.add(dayWiseSkilling);
			}
			response.setDayWiseAuditList(daywiswList);
		} else {
			SMOJTSkillingAuditDTO auditObj = smUtils.getSMOJTAuditDetails(session, request);

			List<SMOJTCSPointsAuditDTO> auditPointList = smUtils.getSMOJTAuditPointDetails(session, request);
			if (!CollectionUtils.isEmpty(auditPointList)) {
				auditObj.setAuditPointList(auditPointList);
			}

			if (auditObj != null && auditObj.getStageId() == SMConstant.STAGE3_ID) {
				request.setSkillingAuditId(auditObj.getPreviousAuditId());
			}

			List<SMOJTSkillingParameterDTO> paramList = smUtils.getSMOJTSkillingParameterList(session, request);
			if (!CollectionUtils.isEmpty(paramList)) {
				auditObj.setParameterList(paramList);
			}
			response.setActionDetails(auditObj);
		}

	}

	@Override
	public HashMap<String, Object> getSkillMatrixEmpList(SkillMatrixRequest request, SkillMatrixResponse smResponse) {
		LOGGER.info("# WorkflowDaoImpl || getSkillMatrixEmpList");
		Session session = getCurrentSession();
		HashMap<String, Object> response = new HashMap<>();
		List<HashMap<String, Object>> dataList = new ArrayList<>();
		List<HashMap<String, Object>> columnList = new ArrayList<>();
		List<SMWorkstations> workstationList = smUtils.getWorkstationListLevelWise(request, session);
		if (!CollectionUtils.isEmpty(workstationList)) {
			if (request.getOffset() == 0) {
				smResponse.setTotalCount(getSkillMatrixEmpListCount(request, session));
			}

			response = smUtils.getEmpList(request, session, response, dataList, workstationList);

			for (int i = 0; i < SMConstant.COLUMNS_FIELDS.length; i++) {
				HashMap<String, Object> empIdMap = new HashMap<>();
				empIdMap.put("field", SMConstant.COLUMNS_FIELDS[i]);
				empIdMap.put("heading", SMConstant.COLUMNS_HEADING[i]);
				columnList.add(empIdMap);
			}
			for (SMWorkstations workstation : workstationList) {
				HashMap<String, Object> columnMap = new HashMap<>();
				columnMap.put("heading", workstation.getWorkstation());
				columnMap.put("field", workstation.getWorkstation());
				columnMap.put("id", workstation.getId());
				columnMap.put("levelId", workstation.getReqSkillLevel().getId());
				columnMap.put("levelName", workstation.getReqSkillLevel().getLevelName());

				columnList.add(columnMap);
			}
			response.put("columns", columnList);
			response.put("tableData", dataList);
		}
		return response;
	}

	private int getSkillMatrixEmpListCount(SkillMatrixRequest request, Session session) {
		List<Tuple> empList = session
				.createNativeQuery(
						"select count(e.emp_id) as totalCount from tbl_employee_details e \r\n"
								+ "	where e.is_deactive=0 and e.branch_id=:branchId and e.dept_id=:deptId ",
						Tuple.class)
				.setParameter("branchId", request.getBranchId()).setParameter("deptId", request.getDeptId())
				.getResultList();

		if (!CollectionUtils.isEmpty(empList)) {
			return CommonUtils.objectToInt(empList.get(0).get("totalCount"));
		}
		return 0;

	}

	@Override
	public boolean submitOJTPlan(SkillMatrixRequest request) {
		LOGGER.info("# WorkflowDaoImpl || submitOJTPlan");
		Session session = getCurrentSession();
		SMOJTPlan plan = new SMOJTPlan();
		if (request.getBranchId() > 0) {
			plan.setBranch(new BranchMaster(request.getBranchId()));
		} else {
			throw new EnovationException(SMConstant.BRANCHID_NOT_FOUND);
		}
		if (request.getDeptId() > 0) {
			plan.setDept(new DepartmentMaster(request.getDeptId()));
		} else {
			throw new EnovationException(SMConstant.DEPARTMENTID_NOT_FOUND);
		}
		if (request.getLineId() > 0) {
			plan.setLine(new Line(request.getLineId()));
		}
		plan.setYearValue(request.getYearValue());
		plan.setMonthValue(request.getMonthValue());
		plan.setStartDate(smUtils.convertStringToTimestamp(request.getStartDate() + " 00:00:00.0 "));
		plan.setStatus(request.getStatus());
		plan.setCreatedBy(request.getCreatedBy());
		plan.setSchedulerStatus(SMConstant.PENDING);

		// Added By Rajdeep 21-04-2024 Validating OJT Plan Data
		smWorker.validateOjtPlanData(request, session);

		session.save(plan);

		if (!CollectionUtils.isEmpty(request.getOjtRegisList())) {
			for (SMOJTPlanDTO regisObj : request.getOjtRegisList()) {

				smWorker.saveOJTPlan(session, regisObj, plan.getId(), request);

				if (!(smWorker.getSafetyAssessResult(session, regisObj.getEmpId()).equals(SMConstant.PASS))
						&& request.getStatus() != null && request.getStatus().equals(SMConstant.PUBLISH)) {

					// Assign Safety Assessment To OE SMConstant.PASS
					smWorker.assignSafetyAssessment(session, regisObj);
				}
			}
		}
		return true;
	}

	@Override
	public List<SMOJTSkillingDTO> getMySkillingCertificateList(SkillMatrixRequest request) {
		Session session = getCurrentSession();
		List<SMOJTSkillingDTO> smCertificate = new ArrayList<>();

		StringBuilder builder = new StringBuilder(
				" select concat(ifnull(e.first_name,''),' ',ifnull(e.last_name,'')) as empName,sl.level_name as levelName,ojt.id as oJTRegiId,\r\n"
						+ " mc.certificate_path as certificatePath,sw.workstation as workstation\r\n"
						+ " from  sm_ojt_certification c inner join sm_master_certificate mc on mc.id =c.certificate_id \r\n"
						+ " inner join sm_ojt_regis ojt on ojt.id=c.ojt_regis_id \r\n"
						+ " inner join tbl_employee_details e on e.emp_id=ojt.emp_id\r\n"
						+ " inner join sm_skill_level sl on sl.id =mc.skill_level_id\r\n"
						+ " inner join sm_workstations sw on ojt.workstation_id = sw.id\r\n"
						+ " where ojt.emp_id =:empId and ojt.status=:status ");

		TypedQuery<Tuple> query = session.createNativeQuery(builder.toString(), Tuple.class)
				.setParameter("empId", request.getEmpId()).setParameter("status", EnovationConstants.COMPLETED_STRING);

		List<Tuple> objList = query.getResultList();
		if (!CollectionUtils.isEmpty(objList)) {
			for (Tuple obj : objList) {
				SMOJTSkillingDTO dto = new SMOJTSkillingDTO();
				dto.setEmpName(CommonUtils.objectToString(obj.get("empName")));
				dto.setLevelName(CommonUtils.objectToString(obj.get("levelName")));
				dto.setOjtRegisId(CommonUtils.objectToLong(obj.get("oJTRegiId")));
				dto.setCertificatePath(EnovationConfig.buddyConfig.get("ProfilePicPathUrl")
						+ CommonUtils.objectToString(obj.get("certificatePath")));
				dto.setWorkstation(CommonUtils.objectToString(obj.get("workstation")));
				smCertificate.add(dto);
			}
		}
		return smCertificate;
	}

	public List<HashMap<String, Object>> getOJTAssessmentsList(SkillMatrixRequest request,
			SkillMatrixResponse response) {
		LOGGER.info("# WorkflowDaoImpl || getOJTAssessmentsList");
		Session session = getCurrentSession();
		if (request.getOffset() == 0) {
			response.setTotalCount(getAssessmentsDetailsCount(session, request));
		}
		StringBuilder sb = new StringBuilder(
				" Select a.id as ojtAssessmentId,a.actual_marks as score,a.assessment_status as status,a.created_by as createdBy,\r\n"
						+ "	 a.created_date as createdDate,a.percentage as percentage,a.total_marks as totalMarks,\r\n"
						+ "	 a.updated_by as updatedBy,a.updated_date as updatedDate,a.skilling_id as skillingId,a.assessment_id as assessmentId,\r\n"
						+ "	 a.ojt_regis_id as regisId,a.skilling_audit_id  as skillingAuditId,sa.title as title,a.created_date as assessmentDate,\r\n"
						+ "	 r.emp_id as empId,concat(ifnull(e.first_name , ' '),' ',ifnull(e.last_name,' ')) as empName,w.id as workstationId, w.workstation as workstationName,\r\n"
						+ "	 b.branch_id as branchId,b.name as branchName,o.org_id as orgId ,o.name as orgName,e.cmpy_emp_id as cmpEmpId ,\r\n"
						+ "	 d.dept_id as deptId,d.dept_name as deptName,l.id as levelId,l.level_name as levelName,dl.id as lineId,dl.name as lineName,"
						+ " sk.created_date as ojtStartDate,sa.assessment_type as assessmentType  from  sm_ojt_assessment a\r\n"
						+ "  left join sm_assessment sa on sa.id=a.assessment_id\r\n"
						+ "	 left join sm_ojt_skilling sk on sk.id=a.skilling_id\r\n"
						+ "	 left join sm_ojt_regis r on r.id=a.ojt_regis_id\r\n"
						+ "	 left join tbl_employee_details e on e.emp_id=r.emp_id\r\n"
						+ "	 left join sm_workstations w on w.id=r.workstation_id\r\n"
						+ "	 left join master_branch b on b.branch_id=r.branch_id\r\n"
						+ "  left join master_department d on d.dept_id=r.dept_id\r\n"
						+ "  left join dwm_line dl on dl.id=d.dept_id\r\n"
						+ "	 left join sm_skill_level l on l.id=r.desired_skill_level_id\r\n"
						+ "	 inner join master_organization o on o.org_id=b.org_id\r\n" + "  where o.org_id=:orgId ");

		if (request.getBranchId() > 0) {
			sb.append(" and b.branch_id=:branchId");
		}
		if (!CollectionUtils.isEmpty(request.getDeptIds())) {
			sb.append(" and d.dept_id in (:deptIds)");
		}
		if (request.getDeptId() > 0) {
			sb.append(" and d.dept_id in (:deptId)");
		}
		if (request.getEmpId() > 0) {
			sb.append(" and r.emp_id=:empId ");
		}
		if (request.getWorkstationIds() != null) {
			sb.append(" and w.id in (:workstationIds)");
		}
		if (request.getSearch() != null) {
			sb.append(" and (w.workstation like '%{search}%' or sa.title like '%{search}%' "
					+ "or concat(ifnull(e.first_name , ' '),' ',ifnull(e.last_name,' ')) like '%{search}%' "
					+ "or l.level_name like '%{search}%'  )");
		}
		if (request.getColName() != null && request.getOrderType() != null) {
			sb.append(" order by " + ColumnAscDescConstants.AssessmentsColName.get(request.getColName()) + " "
					+ request.getOrderType());
		} else {
			sb.append(" order by a.assessment_status DESC ");
		}

		String tmpQuery = sb.toString().replace("{search}", (String.valueOf(request.getSearch())));

		TypedQuery<Tuple> query = session.createNativeQuery(tmpQuery, Tuple.class).setParameter("orgId",
				request.getOrgId());

		if (request.getBranchId() > 0) {
			query.setParameter("branchId", request.getBranchId());
		}

		if (!CollectionUtils.isEmpty(request.getDeptIds())) {
			query.setParameter("deptIds", request.getDeptIds());
		}
		if (request.getDeptId() > 0) {
			query.setParameter("deptId", request.getDeptId());
		}
		if (request.getEmpId() > 0) {
			query.setParameter("empId", request.getEmpId());
		}
		if (request.getWorkstationIds() != null) {
			query.setParameter("workstationIds", request.getWorkstationIds());
		}

		if (request.getLimit() > 0) {
			query.setFirstResult(request.getOffset()).setMaxResults(request.getLimit());
		}

		List<Tuple> tupleList = query.getResultList();
		LOGGER.info("# tupleList size :{}", tupleList.size());
		if (!CollectionUtils.isEmpty(tupleList)) {
			return CommonUtils.parseTupleList(tupleList);
		}
		return null;
	}

	private int getAssessmentsDetailsCount(Session session, SkillMatrixRequest request) {
		LOGGER.info("# WorkflowDaoImpl || getAssessmentsDetailsCount");
		StringBuilder sb = new StringBuilder(" \r\n"
				+ "select count(a.id) as totalCount,w.workstation as workstationName,d.dept_name as deptName,sa.title as title\r\n"
				+ "	 from  sm_ojt_assessment a \r\n" + "  left join sm_assessment sa on sa.id=a.assessment_id\r\n"
				+ "	 left join sm_ojt_skilling sk on sk.id=a.skilling_id\r\n"
				+ "	 left join sm_ojt_regis r on r.id=a.ojt_regis_id\r\n"
				+ "	 left join tbl_employee_details e on e.emp_id=r.emp_id\r\n"
				+ "	 left join sm_workstations w on w.id=r.workstation_id\r\n"
				+ "	 left join master_branch b on b.branch_id=r.branch_id\r\n"
				+ "	 left join master_department d on d.dept_id=r.dept_id\r\n"
				+ "  left join dwm_line dl on dl.id=d.dept_id "
				+ "	 left join sm_skill_level l on l.id=r.current_skill_level_id\r\n"
				+ "	 inner join master_organization o on o.org_id=b.org_id  where o.org_id=:orgId ");

		if (request.getBranchId() > 0) {
			sb.append(" and b.branch_id=:branchId");
		}

		if (request.getDeptIds() != null) {
			sb.append(" and d.dept_id in (:deptIds)");
		}
		if (request.getDeptId() > 0) {
			sb.append(" and d.dept_id in (:deptId)");
		}
		if (request.getEmpId() > 0) {
			sb.append(" and r.emp_id=:empId ");
		}
		if (request.getWorkstationIds() != null) {
			sb.append(" and w.id in (:workstationIds)");
		}
		if (request.getSearch() != null) {
			sb.append(" and (w.workstation like '%{search}%' or sa.title like '%{search}%' )");
		}

		String tmpQuery = sb.toString().replace("{search}", (String.valueOf(request.getSearch())));

		TypedQuery<Tuple> query = session.createNativeQuery(tmpQuery, Tuple.class).setParameter("orgId",
				request.getOrgId());

		if (request.getBranchId() > 0) {
			query.setParameter("branchId", request.getBranchId());
		}

		if (request.getDeptIds() != null) {
			query.setParameter("deptIds", request.getDeptIds());
		}
		if (request.getDeptId() > 0) {
			query.setParameter("deptId", request.getDeptId());
		}
		if (request.getEmpId() > 0) {
			query.setParameter("empId", request.getEmpId());
		}
		if (request.getWorkstationIds() != null) {
			query.setParameter("workstationIds", request.getWorkstationIds());
		}

		List<Tuple> objList = query.getResultList();
		if (!CollectionUtils.isEmpty(objList)) {
			return CommonUtils.objectToInt(objList.get(0).get("totalCount"));
		}
		return 0;
	}

	@Override
	public List<HashMap<String, Object>> getOJTSkillMatrixList(SkillMatrixRequest request,
			SkillMatrixResponse response) {
		LOGGER.info("# WorkflowDaoImpl || getOJTSkillMatrixList");
		Session session = getCurrentSession();
		if (request.getOffset() == 0) {
			response.setTotalCount(getSkillMatrixDetailsCount(session, request));
		}

		StringBuilder sb = new StringBuilder(
				" Select sm.id as skillMatrixId,sm.created_by as createdBy,sm.created_date as createdDate,sm.updated_by as updatedBy,e.cmpy_emp_id as cmpEmpId ,\r\n"
						+ "	sm.updated_date as updatedDate,sm.emp_id as empId ,sm.skill_level_id  as skillLevelId,sm.workstations_id as workstationId, dl.id as lineId,dl.name  as lineName,\r\n"
						+ "	w.workstation as workstationName,b.name as branchName,d.dept_name as deptName,l.level_name as levelName,o.org_id as orgId ,o.name as orgName ,\r\n"
						+ "	r.emp_id as regEmpId,concat(ifnull(e.first_name , ' '),' ',ifnull(e.last_name,' ')) as empName,w.dept_id as deptId,w.branch_id as branchId,r.status as status \r\n"
						+ "	from sm_emp_skill_matrix sm \r\n"
						+ " left join sm_workstations w on w.id=sm.workstations_id\r\n"
						+ "	left join sm_ojt_regis r on r.emp_id=sm.emp_id\r\n"
						+ "	left join master_branch b on b.branch_id=r.branch_id\r\n"
						+ "	left join master_department d on d.dept_id=r.dept_id\r\n"
						+ " left join dwm_line dl on dl.id=d. dept_id\r\n"
						+ "	left join sm_skill_level l on l.id=sm.skill_level_id\r\n"
						+ "	left join tbl_employee_details e on e.emp_id=r.emp_id\r\n"
						+ " inner join master_organization o on o.org_id=b.org_id where o.org_id=:orgId ");

		if (request.getBranchId() > 0) {
			sb.append(" and  b.branch_id=:branchId");
		}

		if (request.getDeptId() > 0) {
			sb.append(" and  d.dept_id =:deptId ");
		}
		if (request.getSkillLevelId() > 0) {
			sb.append(" and sm.skill_level_id=:skillLevelId ");
		}
		if (request.getWorkstationIds() != null) {
			sb.append("and sm.workstations_id in (:workstationIds)");
		}
		if (request.getEmpId() > 0) {
			sb.append(" and sm.emp_id=:empId ");
		}
		if (request.getSearch() != null) {
			sb.append(" and ( w.workstation like '%{search}%' or d.dept_name like '%{search}%' )");
		}

		if (request.getColName() != null && request.getOrderType() != null) {
			sb.append(" ORDER BY " + ColumnAscDescConstants.SkillMatrixColName.get(request.getColName()) + " "
					+ request.getOrderType());
		} else {
			sb.append(" ORDER BY sm.id DESC");
		}

		String tmpQuery = sb.toString().replace("{search}", (String.valueOf(request.getSearch())));

		TypedQuery<Tuple> query = session.createNativeQuery(tmpQuery, Tuple.class).setParameter("orgId",
				request.getOrgId());

		if (request.getBranchId() > 0) {
			query.setParameter("branchId", request.getBranchId());
		}

		if (request.getDeptId() > 0) {
			query.setParameter("deptId", request.getDeptId());
		}
		if (request.getSkillLevelId() > 0) {
			query.setParameter("skillLevelId", request.getSkillLevelId());
		}
		if (request.getWorkstationIds() != null) {
			query.setParameter("workstationIds", request.getWorkstationIds());
		}
		if (request.getEmpId() > 0) {
			query.setParameter("empId", request.getEmpId());
		}

		if (request.getLimit() > 0) {
			query.setFirstResult(request.getOffset()).setMaxResults(request.getLimit());
		}

		List<Tuple> tupleList = query.getResultList();
		LOGGER.info("# tupleList size :{}", tupleList.size());
		if (!CollectionUtils.isEmpty(tupleList)) {
			return CommonUtils.parseTupleList(tupleList);
		}
		return null;
	}

	private int getSkillMatrixDetailsCount(Session session, SkillMatrixRequest request) {
		LOGGER.info("# WorkflowDaoImpl || getSkillMatrixDetailsCount");

		StringBuilder sb = new StringBuilder(
				"    Select count(sm.id) as totalCount,w.workstation as workstation,d.dept_name as deptName \r\n"
						+ " from sm_emp_skill_matrix sm  \r\n"
						+ " left join sm_workstations w on w.id=sm.workstations_id\r\n"
						+ "	 left join sm_ojt_regis r on r.emp_id=sm.emp_id\r\n"
						+ "	 left join master_branch b on b.branch_id=r.branch_id\r\n"
						+ "	 left join master_department d on d.dept_id=r.dept_id\r\n"
						+ "  left join dwm_line dl on dl.id=d. dept_id "
						+ "	 left join sm_skill_level l on l.id=sm.skill_level_id\r\n"
						+ "  left join tbl_employee_details e on e.emp_id=r.emp_id\r\n"
						+ "	 inner join master_organization o on o.org_id=b.org_id\r\n" + "  where o.org_id=:orgId");

		if (request.getBranchId() > 0) {
			sb.append(" and b.branch_id=:branchId");
		}

		if (request.getDeptId() > 0) {
			sb.append(" and d.dept_id =:deptId");
		}

		if (request.getEmpId() > 0) {
			sb.append(" and e.emp_id=:empId ");
		}
		if (request.getSkillLevelId() > 0) {
			sb.append(" and sm.skill_level_id=:skillLevelId ");
		}
		if (request.getWorkstationIds() != null) {
			sb.append(" and w.id in (:workstationIds)");
		}
		if (request.getSearch() != null) {
			sb.append(" and ( w.workstation like '%{search}%' or d.dept_name like '%{search}%' )");
		}

		String tmpQuery = sb.toString().replace("{search}", (String.valueOf(request.getSearch())));

		TypedQuery<Tuple> query = session.createNativeQuery(tmpQuery, Tuple.class).setParameter("orgId",
				request.getOrgId());

		if (request.getBranchId() > 0) {
			query.setParameter("branchId", request.getBranchId());
		}

		if (request.getDeptId() > 0) {
			query.setParameter("deptId", request.getDeptId());
		}
		if (request.getEmpId() > 0) {
			query.setParameter("empId", request.getEmpId());
		}
		if (request.getSkillLevelId() > 0) {
			query.setParameter("skillLevelId", request.getSkillLevelId());
		}
		if (request.getWorkstationIds() != null) {
			query.setParameter("workstationIds", request.getWorkstationIds());
		}

		List<Tuple> objList = query.getResultList();
		if (!CollectionUtils.isEmpty(objList)) {
			return CommonUtils.objectToInt(objList.get(0).get("totalCount"));
		}
		return 0;
	}

	@Override
	public List<HashMap<String, Object>> getCertificateList(SkillMatrixRequest request, SkillMatrixResponse response) {
		LOGGER.info("# WorkflowDaoImpl || getCertificateList");
		Session session = getCurrentSession();
		if (request.getOffset() == 0) {
			response.setTotalCount(getCertificateDetailsCount(session, request));
		}
		String path = EnovationConfig.buddyConfig.get("ProfilePicPathUrl");
		StringBuilder sb = new StringBuilder(
				"  Select c.id as certificateId,c.created_by as createdBy,c.created_date as createdDate,c.updated_by as updatedBy,dl.id as lineId,dl.name  as lineName,\r\n"
						+ " c.updated_date as updatedDate,c.certificate_id as masterCerId ,c.ojt_regis_id as regId,r.status as status,o.org_id as orgId ,o.name as orgName , \r\n"
						+ " mc.certificate_name as certificateName,concat('" + path
						+ "', mc.certificate_path) as certificatePath,w.id as workstationId, w.workstation as workstationName,b.branch_id as branchId,b.name as branchName,\r\n"
						+ " d.dept_id as deptId,d.dept_name as deptName,l.level_name as levelName,e.cmpy_emp_id as companyEmpId,r.emp_id as empId,concat(ifnull(e.first_name , ' '),' ',ifnull(e.last_name,' ')) as empName\r\n"
						+ " from sm_ojt_certification c \r\n"
						+ " left join sm_master_certificate mc on mc.id=c.certificate_id\r\n"
						+ " left join sm_ojt_regis r on r.id =c.ojt_regis_id\r\n"
						+ " left join tbl_employee_details e on e.emp_id=r.emp_id\r\n"
						+ " left join sm_workstations w on w.id=r.workstation_id\r\n"
						+ " left join master_branch b on b.branch_id=r.branch_id\r\n"
						+ " left join master_department d on d.dept_id=r.dept_id\r\n"
						+ " left join dwm_line dl on dl.id=r.line_id "
						+ " left join sm_skill_level l on l.id=mc.skill_level_id\r\n"
						+ " inner join master_organization o on o.org_id=b.org_id "
						+ " where o.org_id=:orgId and r.status =:status ");

		if (request.getBranchId() > 0) {
			sb.append(" and  b.branch_id=:branchId");
		}

		if (!CollectionUtils.isEmpty(request.getDeptIds())) {
			sb.append(" and  d.dept_id in (:deptIds)");
		}
		if (request.getWorkstationIds() != null) {
			sb.append(" and w.id in (:workstationIds)");
		}
		if (request.getEmpId() > 0) {
			sb.append(" and r.emp_id=:empId ");
		}
		if (request.getSearch() != null) {
			sb.append(" and (w.workstation like '%{search}%' or d.dept_name like '%{search}%' or "
					+ "concat(ifnull(e.first_name , ' '),' ',ifnull(e.last_name,' ')) like '%{search}%' or b.name like '%{search}%') ");
		}

		if (request.getColName() != null && request.getOrderType() != null) {
			sb.append(" ORDER BY " + ColumnAscDescConstants.CertificateColName.get(request.getColName()) + " "
					+ request.getOrderType());
		} else {
			sb.append(" ORDER BY c.id DESC");
		}

		String tmpQuery = sb.toString().replace("{search}", (String.valueOf(request.getSearch())));

		TypedQuery<Tuple> query = session.createNativeQuery(tmpQuery, Tuple.class)
				.setParameter("orgId", request.getOrgId()).setParameter("status", EnovationConstants.COMPLETED_STRING);

		if (request.getBranchId() > 0) {
			query.setParameter("branchId", request.getBranchId());
		}

		if (!CollectionUtils.isEmpty(request.getDeptIds())) {
			query.setParameter("deptIds", request.getDeptIds());
		}
		if (request.getWorkstationIds() != null) {
			query.setParameter("workstationIds", request.getWorkstationIds());
		}
		if (request.getEmpId() > 0) {
			query.setParameter("empId", request.getEmpId());
		}

		if (request.getLimit() > 0) {
			query.setFirstResult(request.getOffset()).setMaxResults(request.getLimit());
		}

		List<Tuple> tupleList = query.getResultList();
		LOGGER.info("# tupleList size :{}", tupleList.size());
		if (!CollectionUtils.isEmpty(tupleList)) {
			return CommonUtils.parseTupleList(tupleList);
		}
		return null;
	}

	private int getCertificateDetailsCount(Session session, SkillMatrixRequest request) {
		LOGGER.info("# WorkflowDaoImpl || getCertificateDetailsCount");

		StringBuilder sb = new StringBuilder(
				"	select  count(c.id) as totalCount,w.workstation as workstation,d.dept_name as deptName \r\n"
						+ "   from sm_ojt_certification c\r\n"
						+ "	  left join sm_ojt_regis r on r.id=c.ojt_regis_id\r\n"
						+ "	  left join tbl_employee_details e on e.emp_id=r.emp_id\r\n"
						+ "	  left join sm_workstations w on w.id=r.workstation_id\r\n"
						+ "	  left join master_branch b on b.branch_id=r.branch_id\r\n"
						+ "	  left join master_department d on d.dept_id=r.dept_id\r\n"
						+ "    left join dwm_line dl on dl.id=d. dept_id "
						+ "	  inner join master_organization o on o.org_id=b.org_id where  o.org_id=:orgId ");

		if (request.getBranchId() > 0) {
			sb.append(" and b.branch_id=:branchId");
		}

		if (!CollectionUtils.isEmpty(request.getDeptIds())) {
			sb.append(" and d.dept_id in (:deptIds)");
		}

		if (request.getEmpId() > 0) {
			sb.append(" and e.emp_id=:empId ");
		}
		if (request.getWorkstationIds() != null) {
			sb.append(" and w.id in (:workstationIds)");
		}
		if (request.getSearch() != null) {
			sb.append(" and ( w.workstation like '%{search}%' or d.dept_name like '%{search}%' )");
		}
		String tmpQuery = sb.toString().replace("{search}", (String.valueOf(request.getSearch())));

		TypedQuery<Tuple> query = session.createNativeQuery(tmpQuery, Tuple.class).setParameter("orgId",
				request.getOrgId());

		if (request.getBranchId() > 0) {
			query.setParameter("branchId", request.getBranchId());
		}

		if (!CollectionUtils.isEmpty(request.getDeptIds())) {
			query.setParameter("deptIds", request.getDeptIds());
		}
		if (request.getEmpId() > 0) {
			query.setParameter("empId", request.getEmpId());
		}
		if (request.getWorkstationIds() != null) {
			query.setParameter("workstationIds", request.getWorkstationIds());
		}

		List<Tuple> objList = query.getResultList();
		if (!CollectionUtils.isEmpty(objList)) {
			return CommonUtils.objectToInt(objList.get(0).get("totalCount"));
		}
		return 0;
	}

	@Override
	public List<HashMap<String, Object>> getOJTPlanList(SkillMatrixRequest request, SkillMatrixResponse response) {
		LOGGER.info("# WorkflowDaoImpl || getOJTPlanList");
		Session session = getCurrentSession();
		if (request.getOffset() == 0) {
			response.setTotalCount(getOJTPlanListCount(session, request));
		}
		StringBuilder builder = new StringBuilder(
				"  select ojt.id as planId, ojt.created_by as planedBy, ojt.created_date as createdDate, ojt.month_value as monthValue,\r\n"
						+ " ojt.start_date as staretDate, ojt.status as status, ojt.updated_by as updatedBy, ojt.updated_date as updatedDate, ojt.year_value as yearValue,\r\n"
						+ " ojt.branch_id as branchId, ojt.dept_id as deptId ,b.name as branch,md.dept_name as deptName,"
						+ "  concat(ifnull(ed.first_name,''),' ',ifnull(ed.last_name,'')) as planedByName,l.name as lineName,l.id as lineId \r\n"
						+ " from  sm_ojt_plan ojt inner join master_branch b on b.branch_id=ojt.branch_id "
						+ " inner join \r\n" + " master_department md on md.dept_id=ojt.dept_id "
						+ " left join tbl_employee_details ed on ed.emp_id = ojt.created_by "
						+ " left join dwm_line l on l.id = ojt.line_id where b.org_id=:orgId");

		if (request.getDeptId() > 0) {
			builder.append(" and ojt.dept_id =:deptId");
		}
		if (!CollectionUtils.isEmpty(request.getDeptIds())) {
			builder.append(" and ojt.dept_id  in (:deptIds) ");
		}

		if (request.getBranchId() > 0) {
			builder.append(" and ojt.branch_id=:branchId");
		}
		if (request.getLineId() > 0) {
			builder.append(" and ojt.line_id=:lineId");
		}
		if (!CollectionUtils.isEmpty(request.getLineIds())) {
			builder.append(" and ojt.line_id in (:lineIds) ");
		}
		if (request.getSearch() != null) {
			builder.append(
					" and (concat(ifnull(ed.first_name,''),' ',ifnull(ed.last_name,'')) like '%{search}%' or md.dept_name like '%{search}%' "
							+ " or ojt.status like '%{search}%' or ojt.start_date like '%{search}%' or l.name like '%{search}%' )");
		}
		if (request.getColName() != null && request.getOrderType() != null) {
			builder.append(" order by " + ColumnAscDescConstants.smOJTPlanColName.get(request.getColName()) + " "
					+ request.getOrderType());
		} else {
			builder.append(" order by ojt.id DESC ");
		}

		String tmpQuery = builder.toString().replace("{search}", (String.valueOf(request.getSearch())));

		TypedQuery<Tuple> query = session.createNativeQuery(tmpQuery, Tuple.class).setParameter("orgId",
				request.getOrgId());

		if (request.getDeptId() > 0) {
			query.setParameter("deptId", request.getDeptId());
		}
		if (!CollectionUtils.isEmpty(request.getDeptIds())) {
			query.setParameter("deptIds", request.getDeptIds());
		}
		if (request.getBranchId() > 0) {
			query.setParameter("branchId", request.getBranchId());
		}
		if (request.getLineId() > 0) {
			query.setParameter("lineId", request.getLineId());
		}
		if (!CollectionUtils.isEmpty(request.getLineIds())) {
			query.setParameter("lineIds", request.getLineIds());
		}
		if (request.getLimit() > 0) {
			query.setFirstResult(request.getOffset()).setMaxResults(request.getLimit());
		}
		if (request.getSearch() != null && request.getSearch().equalsIgnoreCase(EnovationConstants.SEARCH)) {
			smWorker.setParameterInQuery(query, request);
		}
		List<Tuple> objList = query.getResultList();
		if (!CollectionUtils.isEmpty(objList)) {
			LOGGER.info("# WorkflowDaoImpl || sttr Date: {}",
					CommonUtils.objectToString(objList.get(0).get("staretDate")));
			return CommonUtils.parseTupleList(objList);
		} else {
			return null;
		}
	}

	private int getOJTPlanListCount(Session session, SkillMatrixRequest request) {
		StringBuilder builder = new StringBuilder(
				"  select count(ojt.id) as totalCount from  sm_ojt_plan ojt inner join master_branch b on b.branch_id=ojt.branch_id "
						+ " inner join \r\n" + " master_department md on md.dept_id=ojt.dept_id "
						+ " left join tbl_employee_details ed on ed.emp_id = ojt.created_by "
						+ "left join dwm_line l on l.id = ojt.line_id  where b.org_id=:orgId");

		if (request.getDeptId() > 0) {
			builder.append(" and ojt.dept_id =:deptId");
		}

		if (request.getBranchId() > 0) {
			builder.append(" and ojt.branch_id=:branchId");
		}
		if (request.getLineId() > 0) {
			builder.append(" and ojt.line_id=:lineId");
		}
		if (request.getSearch() != null) {
			builder.append(
					" and (concat(ifnull(ed.first_name,''),' ',ifnull(ed.last_name,'')) like '%{search}%' or md.dept_name like '%{search}%' "
							+ " or ojt.status like '%{search}%' or ojt.start_date like '%{search}%' or ojt.year_value like '%{search}%' )");
		}
		String tmpQuery = builder.toString().replace("{search}", (String.valueOf(request.getSearch())));

		TypedQuery<Tuple> query = session.createNativeQuery(tmpQuery, Tuple.class).setParameter("orgId",
				request.getOrgId());

		if (request.getDeptId() > 0) {
			query.setParameter("deptId", request.getDeptId());
		}
		if (request.getBranchId() > 0) {
			query.setParameter("branchId", request.getBranchId());
		}
		if (request.getLineId() > 0) {
			query.setParameter("lineId", request.getLineId());
		}
		List<Tuple> objList = query.getResultList();
		if (!CollectionUtils.isEmpty(objList)) {
			return CommonUtils.objectToInt(objList.get(0).get("totalCount"));
		}
		return 0;
	}

	@Override
	public SMOJTPlanDTO getOJTPlanDetails(long ojtPlanId) {
		LOGGER.info("# WorkflowDaoImpl || getOJTPlanDetails");
		Session session = getCurrentSession();
		StringBuilder builder = new StringBuilder(
				" select op.id as planId, op.created_by as createdBy, op.created_date as createdDate, op.month_value as monthValue, op.start_date as startDate, op.status as status\r\n"
						+ ", op.updated_by as updatedBy, op.updated_date as updatedDate, op.year_value as yearValue, op.branch_id as branchId, op.dept_id as deptId  ,\r\n"
						+ "concat(ifnull(e.first_name,''),' ',ifnull(e.last_name,'')) as createdByName,b.name as branch,d.dept_name as deptName,l.id as lineId,l.name as lineName "
						+ " from  sm_ojt_plan op\r\n" + "left join master_branch b on b.branch_id=op.branch_id\r\n"
						+ "left join master_department d on d.dept_id=op.dept_id\r\n"
						+ "left join tbl_employee_details e on e.emp_id=op.created_by "
						+ " left join dwm_line l on l.id=op.line_id where op.id=:ojtPlanId ");
		Tuple obj = session.createNativeQuery(builder.toString(), Tuple.class).setParameter("ojtPlanId", ojtPlanId)
				.getResultList().stream().findFirst().orElse(null);

		SMOJTPlanDTO data = new SMOJTPlanDTO();
		if (obj != null) {
			data.setOjtPlanId(CommonUtils.objectToLong(obj.get("planId")));
			data.setCreatedBy(CommonUtils.objectToInt(obj.get("createdBy")));
			data.setCreatedDate(CommonUtils.objectToString(obj.get("createdDate")));
			data.setMonthValue(CommonUtils.objectToInt(obj.get("monthValue")));
			data.setStartDate(CommonUtils.objectToString(obj.get("startDate")));
			data.setStatus(CommonUtils.objectToString(obj.get("status")));
			data.setUpdatedBy(CommonUtils.objectToInt(obj.get("updatedBy")));
			data.setUpdatedDate(CommonUtils.objectToString(obj.get("updatedDate")));
			data.setYearValue(CommonUtils.objectToInt(obj.get("yearValue")));
			data.setBranchId(CommonUtils.objectToInt(obj.get("branchId")));
			data.setDeptId(CommonUtils.objectToInt(obj.get("deptId")));
			data.setBranchName(CommonUtils.objectToString(obj.get("branch")));
			data.setDeptName(CommonUtils.objectToString(obj.get("deptName")));
			data.setLineId(CommonUtils.objectToLong(obj.get("lineId")));
			data.setLineName(CommonUtils.objectToString(obj.get("lineName")));

			SkillMatrixRequest request = new SkillMatrixRequest();
			request.setDeptId(CommonUtils.objectToInt(obj.get("deptId")));
			request.setBranchId(CommonUtils.objectToInt(obj.get("branchId")));

			List<SMWorkstations> workstationList = smUtils.getWorkstationListLevelWise(request, session);

			List<HashMap<String, Object>> dataList = getOjtPlanRegiList(ojtPlanId, session, workstationList);
			data.setOjtRegiList(dataList);

			List<HashMap<String, Object>> columnList = new ArrayList<>();
			for (SMWorkstations workstation : workstationList) {
				HashMap<String, Object> columnMap = new HashMap<>();
				columnMap.put("heading", workstation.getWorkstation());
				columnMap.put("field", workstation.getWorkstation());
				columnMap.put("id", workstation.getId());
				columnList.add(columnMap);
			}
			for (int i = 0; i < SMConstant.COLUMNS_FIELDS.length; i++) {
				HashMap<String, Object> empIdMap = new HashMap<>();
				empIdMap.put("field", SMConstant.COLUMNS_FIELDS[i]);
				empIdMap.put("heading", SMConstant.COLUMNS_HEADING[i]);
				columnList.add(empIdMap);
			}
			data.setColumn(columnList);
		}
		return data;
	}

	private List<HashMap<String, Object>> getOjtPlanRegiList(long ojtPlanId, Session session,
			List<SMWorkstations> workstationList) {
		LOGGER.info("# WorkflowDaoImpl || getOjtPlanRegiList");
		StringBuilder ojtRegi = new StringBuilder(
				"  select r.id as ojtRegiId , r.created_by as createdBy, r.created_date as createdDate, r.status as status, \r\n"
						+ "r.updated_by as updatedBy, r.updated_date as updatedDate, r.current_skill_level_id as currentSkillLvlId, r.desired_skill_level_id as desiredSkillLvlId,\r\n"
						+ " r.emp_id as oeEmpId, r.trainer_emp_id as trainerEmpId, r.workstation_id as workstationId, r.branch_id as branchId, r.dept_id as deptId,\r\n"
						+ " r.ojt_plan_id as ojtPlanId ,concat(ifnull(e.first_name,''),' ',ifnull(e.last_name,'')) as eoName,\r\n"
						+ " concat(ifnull(ed.first_name,''),' ',ifnull(ed.last_name,'')) as trainerName,b.name as branch,d.dept_name as deptName ,\r\n"
						+ " w.workstation as workstation,sl.level_name as currentLvl,sd.level_name as desiredLvl,e.cmpy_emp_id as cmpyEmpId ,"
						+ " l.id as lineId,l.name as lineName,"
						+ " soa.assessment_status as assessmentStatus,soa.created_date as assessmentCreatedDate,\r\n"
						+ " (case when soa.assessment_status is null then null else soa.updated_date end) assessmentUpdatedDate,"
						+ " smsk.created_date as actualDate,sa.assessment_type as assessmentType "
						+ " from sm_ojt_regis r left join sm_ojt_skilling smsk on smsk.ojt_regis_id =r.id "
						+ " left join master_branch b on b.branch_id=r.branch_id\r\n"
						+ " left join master_department d on d.dept_id=r.dept_id\r\n"
						+ " left join tbl_employee_details e on e.emp_id= r.emp_id\r\n"
						+ " left join tbl_employee_details ed on ed.emp_id= r.trainer_emp_id\r\n"
						+ " left join sm_workstations w on w.id=r.workstation_id\r\n"
						+ " left join sm_skill_level sl on sl.id=r.current_skill_level_id\r\n"
						+ " left join sm_skill_level sd on sd.id=r.desired_skill_level_id "
						+ " left join dwm_line l on l.id=r.line_id "
						+ " left join sm_ojt_assessment soa on soa.ojt_regis_id=r.id "
						+ " left join sm_assessment sa on sa.id=soa.assessment_id "
						+ " where  r.ojt_plan_id=:ojtPlanId");
		TypedQuery<Tuple> query = session.createNativeQuery(ojtRegi.toString(), Tuple.class).setParameter("ojtPlanId",
				ojtPlanId);
		List<Tuple> objList = query.getResultList();
		List<HashMap<String, Object>> dataList = new ArrayList<>();
		if (!CollectionUtils.isEmpty(objList)) {
			for (Tuple tuple : objList) {
				HashMap<String, Object> reg = new HashMap<>();
				reg.put("branchId", (CommonUtils.objectToInt(tuple.get("branchId"))));
				reg.put("deptId", (CommonUtils.objectToInt(tuple.get("deptId"))));
				reg.put("branch", (CommonUtils.objectToString(tuple.get("branch"))));
				reg.put("deptName", (CommonUtils.objectToString(tuple.get("deptName"))));
				reg.put("oeEmpId", (CommonUtils.objectToInt(tuple.get("oeEmpId"))));
				reg.put("empName", (CommonUtils.objectToString(tuple.get("eoName"))));
				reg.put("currentSkillLvlId", (CommonUtils.objectToLong(tuple.get("currentSkillLvlId"))));
				reg.put("currentLvl", (CommonUtils.objectToString(tuple.get("currentLvl"))));
				reg.put("desiredSkillLvlId", (CommonUtils.objectToLong(tuple.get("desiredSkillLvlId"))));
				reg.put("desiredLvl", (CommonUtils.objectToString(tuple.get("desiredLvl"))));
				reg.put("workstation", (CommonUtils.objectToString(tuple.get("workstation"))));
				reg.put("workstationId", (CommonUtils.objectToLong(tuple.get("workstationId"))));
				reg.put("trainerEmpId", (CommonUtils.objectToInt(tuple.get("trainerEmpId"))));
				reg.put("trainerName", (CommonUtils.objectToString(tuple.get("trainerName"))));
				reg.put("status", (CommonUtils.objectToString(tuple.get("status"))));
				reg.put("cmpyEmpId", (CommonUtils.objectToString(tuple.get("cmpyEmpId"))));
				reg.put("lineId", (CommonUtils.objectToString(tuple.get("lineId"))));
				reg.put("lineName", (CommonUtils.objectToString(tuple.get("lineName"))));
				reg.put("ojtRegiId", (CommonUtils.objectToLong(tuple.get("ojtRegiId"))));
				reg.put("assessmentStatus", (CommonUtils.objectToString(tuple.get("assessmentStatus"))));
				reg.put("assessmentCreatedDate", (CommonUtils.objectToString(tuple.get("assessmentCreatedDate"))));
				reg.put("assessmentUpdatedDate", (CommonUtils.objectToString(tuple.get("assessmentUpdatedDate"))));
				reg.put("actualOJTDate", (CommonUtils.objectToString(tuple.get("actualDate"))));
				reg.put("assessmentType", (CommonUtils.objectToString(tuple.get("assessmentType"))));

				List<Tuple> workList = smUtils.empWorkStationList(session,
						CommonUtils.objectToInt(tuple.get("oeEmpId")));
				for (SMWorkstations workstation : workstationList) {
					Tuple empObj = workList.stream().filter(x -> CommonUtils.objectToString(x.get("workstation"))
							.toUpperCase().trim().equals(workstation.getWorkstation().toUpperCase().trim())).findAny()
							.orElse(null);
					SMOJTPlanDTO dto = new SMOJTPlanDTO();
					if (empObj != null) {
						dto.setCurrentSkillLevel(CommonUtils.objectToString(empObj.get("empLevel")));
						dto.setCurrentSkillLevelId(CommonUtils.objectToLong(empObj.get("empLevelId")));
					}
					dto.setRequireSkillLevel(workstation.getReqSkillLevel().getLevelName());
					dto.setRequireSkillLevelId(workstation.getReqSkillLevel().getId());

					reg.put(workstation.getWorkstation(), dto);
				}
				dataList.add(reg);
			}
		}
		return dataList;
	}

	@Override
	public boolean deleteOJTPlan(long ojtPlanId) {
		LOGGER.info("# WorkflowDaoImpl || deleteOJTPlan");
		Session session = getCurrentSession();
		List<Tuple> List = smUtils.getSMOJTRegisDetailForAuditByPlanId(session, ojtPlanId);
		session.createNativeQuery(" delete from sm_ojt_regis where ojt_plan_id =:ojtPlanId")
				.setParameter("ojtPlanId", ojtPlanId).executeUpdate();

		smAudit.deleteSMOJTRegisAudit(session, ojtPlanId, List);
		Tuple tuple = smUtils.getOjtPlanDetailsForAudit(ojtPlanId, session);
		session.createNativeQuery(" delete from sm_ojt_plan where id=:ojtPlanId").setParameter("ojtPlanId", ojtPlanId)
				.executeUpdate();

		smAudit.deleteSMOJTPlanAudit(session, ojtPlanId, tuple);
		return true;

	}

	@Override
	public boolean updateOJTPlan(SkillMatrixRequest request) {
		LOGGER.info("# WorkflowDaoImpl || updateOJTPlan");
		Session session = getCurrentSession();
		// Added By Rajdeep 21-04-2024 Validating OJT Plan Data
		smWorker.validateOjtPlanData(request, session);
		SMOJTPlan plan = session.get(SMOJTPlan.class, request.getOjtPlanId());
		Tuple tupleList = smUtils.getOjtPlanDetailsForAudit(request.getOjtPlanId(), session);

		if (request.getYearValue() > 0) {
			plan.setYearValue(request.getYearValue());
		}
		if (request.getMonthValue() > 0) {
			plan.setMonthValue(request.getMonthValue());
		}
		if (request.getStartDate() != null) {
			plan.setStartDate(smUtils.convertStringToTimestamp(request.getStartDate() + " 00:00:00.0 "));
		}
		if (request.getStatus() != null) {
			plan.setStatus(request.getStatus());
		}
		plan.setUpdatedBy(request.getUpdatedBy());
		session.update(plan);
		if (!CollectionUtils.isEmpty(request.getOjtRegisList())) {
			updateOjtRegis(request, session, plan);
		}
		smAudit.updateSMOJTPlanAudit(session, request, tupleList);
		return true;
	}

	private void updateOjtRegis(SkillMatrixRequest request, Session session, SMOJTPlan plan) {
		LOGGER.info("# WorkflowDaoImpl || updateOjtRegis");
		for (SMOJTPlanDTO regisObj : request.getOjtRegisList()) {
			if (regisObj.getAction() != null && regisObj.getAction().equals(SMConstant.UPDATE)) {

				List<Tuple> tupleList = smUtils.getSMOJTRegisDetailForAudit(session, regisObj);
				SMOJTRegis regis = updateEmpOjtRegiDetails(request, session, plan, regisObj);
				smAudit.updateSMOJTRegisAudit(session, regisObj, tupleList);

				session.save(regis);
			} else if (regisObj.getAction() != null && regisObj.getAction().equals(SMConstant.DELETE)) {
				if (regisObj.getOjtRegisId() > 0) {
					List<Tuple> tupleList = smUtils.getSMOJTRegisDetailForAudit(session, regisObj);
					LOGGER.info("# WorkflowDaoImpl || deleteOjtPlan");
					session.createNativeQuery(" delete from sm_ojt_regis where id=:ojtPlanId")
							.setParameter("ojtPlanId", regisObj.getOjtRegisId()).executeUpdate();
					smAudit.updateSMOJTRegisAudit(session, regisObj, tupleList);
				}
			} else if (regisObj.getAction() != null && regisObj.getAction().equals(SMConstant.ADD)) {
				request.setCreatedBy(request.getUpdatedBy());
				smWorker.saveOJTPlan(session, regisObj, plan.getId(), request);
			}

			// Assign Safety Assignment
			if (regisObj.getAction() != null && regisObj.getAction().equals(SMConstant.ADD)
					|| regisObj.getAction().equals(SMConstant.UPDATE)) {
				if (!(smWorker.getSafetyAssessResult(session, regisObj.getEmpId()).equals(SMConstant.PASS))
						&& request.getStatus() != null && request.getStatus().equals(SMConstant.PUBLISH)) {

					// Assign Safety Assessment To OE SMConstant.PASS
					smWorker.assignSafetyAssessment(session, regisObj);
				}
			}
		}
	}

	private SMOJTRegis updateEmpOjtRegiDetails(SkillMatrixRequest request, Session session, SMOJTPlan plan,
			SMOJTPlanDTO regisObj) {
		LOGGER.info("# WorkflowDaoImpl || updateEmpOjtRegiDetails");

		SMOJTRegis regis = session.get(SMOJTRegis.class, regisObj.getOjtRegisId());

		if (regisObj.getTrainerEmpId() > 0) {
			regis.setTrainerDetails(new EmployeeDetails(regisObj.getTrainerEmpId()));
		}
		if (regisObj.getDesiredSkillLevelId() > 0) {
			regis.setDesiredSkillLevel(new SMSkillLevel(regisObj.getDesiredSkillLevelId()));
		}
		if (regisObj.getWorkstationId() > 0) {
			regis.setWorkstation(new SMWorkstations(regisObj.getWorkstationId()));
		}

		regis.setSmOjtPlan(plan);
		return regis;
	}

	@Override
	public List<SMOJTPlanDTO> getSkillMatrixActionList(SkillMatrixRequest request, SkillMatrixResponse response) {
		Session session = getCurrentSession();
		List<SMOJTPlanDTO> list = new ArrayList<>();
		LOGGER.info("# WorkflowDaoImpl || getMyPendingList ");
		if (request.getOffset() == 0) {
			response.setTotalCount(getSkillMatrixActionCount(session, request));
		}

		List<Tuple> tupleList = smUtils.getSkillMatrixActionTupleList(request, session);

		LOGGER.info("# tupleList size : {}", tupleList.size());
		if (!CollectionUtils.isEmpty(tupleList)) {
			for (Tuple tuple : tupleList) {
				SMOJTPlanDTO reg = new SMOJTPlanDTO();
				reg.setActivityDate(CommonUtils.objectToString(tuple.get("activityDate")));
				reg.setActivity(CommonUtils.objectToString(tuple.get("activity")));
				reg.setOjtRegisId(CommonUtils.objectToLong(tuple.get("regisId")));
				reg.setBranchId(CommonUtils.objectToInt(tuple.get("branchId")));
				reg.setDeptId(CommonUtils.objectToInt(tuple.get("deptId")));
				reg.setBranchName(CommonUtils.objectToString(tuple.get("branchName")));
				reg.setDeptName(CommonUtils.objectToString(tuple.get("deptName")));
				reg.setOeEmpId(CommonUtils.objectToInt(tuple.get("empId")));
				reg.setOeEmpName(CommonUtils.objectToString(tuple.get("empName")));
				reg.setCmpyEmpId(CommonUtils.objectToString(tuple.get("cmpyEmpId")));
				reg.setCurrentSkillLevel(CommonUtils.objectToString(tuple.get("levelName")));
				reg.setWorkstation(CommonUtils.objectToString(tuple.get("workstation")));
				reg.setStatus(CommonUtils.objectToString(tuple.get("status")));
				reg.setCompletedDate(CommonUtils.objectToString(tuple.get("completedDate")));
				reg.setLineName(CommonUtils.objectToString(tuple.get("lineName")));
				reg.setLineId(CommonUtils.objectToLong(tuple.get("lineId")));
				reg.setAssignedEmpId(CommonUtils.objectToString(tuple.get("assignedEmpId")));
				reg.setAssignedEmpName(CommonUtils.objectToString(tuple.get("assignedEmpName")));

				if (reg != null) {
					list.add(reg);
				}
			}
		}
		return list;
	}

	private int getSkillMatrixActionCount(Session session, SkillMatrixRequest request) {
		StringBuilder sb = new StringBuilder(
				"select count(ojtsa.id) as totalCount " + "from sm_ojt_skilling_audit ojtsa\r\n"
						+ " inner join sm_ojt_skilling_checksheet ojtsc on ojtsc.id=ojtsa.skilling_checksheet_id\r\n"
						+ " inner join sm_ojt_skilling ojts on ojts.id=ojtsa.skilling_id\r\n"
						+ " inner join sm_ojt_regis ojtr on ojtr.id=ojts.ojt_regis_id\r\n"
						+ " left join sm_checksheet smc on smc.id=ojts.checksheet_id\r\n"
						+ " left join master_branch mb on mb.branch_id=ojtr.branch_id\r\n"
						+ " left join master_department md on md.dept_id=ojtr.dept_id \r\n"
						+ " left join sm_workstations smw on smw.id=ojtr.workstation_id\r\n"
						+ " left join sm_stage stage on stage.id=ojtsa.stage_id\r\n"
						+ " left join sm_skill_level slvl on slvl.id=ojtr.desired_skill_level_id\r\n"
						+ " left join tbl_employee_details ed on ed.emp_id=ojtr.emp_id  \r\n"
						+ " left join dwm_line l on l.id=ojtr.line_id\r\n "
						+ " left join sm_ojt_skilling_audit au on au.previous_audit_id = ojtsa.id\r\n"
						+ " left join tbl_employee_details e on e.emp_id=au.emp_id  "
						+ "  left join sm_stage_label sml on sml.stage_id=stage.id AND sml.branch_id=ojtr.branch_id  "
						+ " where mb.org_id=:orgId ");

		if (request.getBranchId() > 0) {
			sb.append(" and mb.branch_id=:branchId ");
		}

		if (request.getDeptId() > 0) {
			sb.append(" and md.dept_id=:deptId ");
		}

		if (request.getFromDt() != null && request.getToDt() != null) {
			sb.append(" and ojtsa.created_date between '{fromDate} 00:00:00' and '{toDate} 23:59:59' ");

		}
		if (request.getSearch() != null) {
			sb.append(
					" and (concat(IFNULL(ed.first_name,''),' ',IFNULL(ed.last_name,'')) like '%{search}%' or smw.workstation like '%{search}%' "
							+ " or md.dept_name like '%{search}%' or slvl.level_name like '%{search}%' or stage.stage_name like '%{search}%' )");
		}
		sb.append(" group by ojtsa.id ");

		String tmpQuery = (sb.toString()).replace("{fromDate}", String.valueOf(request.getFromDt()))
				.replace("{toDate}", String.valueOf(request.getToDt()))
				.replace("{search}", String.valueOf(request.getSearch()));

		TypedQuery<Tuple> query = session.createNativeQuery(tmpQuery, Tuple.class).setParameter("orgId",
				request.getOrgId());

		if (request.getBranchId() > 0) {
			query.setParameter("branchId", request.getBranchId());
		}

		if (request.getDeptId() > 0) {
			query.setParameter("deptId", request.getDeptId());
		}
		List<Tuple> tupleList = query.getResultList();

		if (!CollectionUtils.isEmpty(tupleList)) {
//			return CommonUtils.objectToInt(tupleList.get(0).get("totalCount"));
			return tupleList.size();
		}
		return 0;
	}

	@Override
	public SMAssessmentDTO getOJTAssessmentDetails(long ojtAssessmentId) {
		LOGGER.info("# WorkflowDaoImpl || getOJTAssessmentDetails");
		Session session = getCurrentSession();
		SMAssessmentDTO data = new SMAssessmentDTO();

		data = smUtils.assessmentDetails(session, ojtAssessmentId, data);
		if (data != null) {
			TypedQuery<Tuple> query = smUtils.getAssessmentOptionList(ojtAssessmentId, session);
			List<Tuple> tupleList = query.getResultList();
			if (!CollectionUtils.isEmpty(tupleList)) {

				List<SkillMatrixRequest> dtoList = new ArrayList<>();
				smUtils.setDtoParameters(tupleList, dtoList);
				Map<Long, List<SkillMatrixRequest>> groupedByQuetionId = dtoList.stream()
						.collect(Collectors.groupingBy(SkillMatrixRequest::getAssessmentQueId));

				smWorker.groupingQueOptList(data, groupedByQuetionId, session);
			}
		}
		return data;
	}

	@Override
	public boolean saveWorkForceDeployment(SkillMatrixRequest request) {
		LOGGER.info("# WorkflowDaoImpl || saveWorkForceDeployment");
		Session session = getCurrentSession();
		boolean flag = false;

		for (SkillMatrixRequest empobj : request.getEmpList()) {
			SMWFDeployment wfdObj = new SMWFDeployment();
			if (empobj.getEmpId() > 0) {
				wfdObj.setEmpDetails(new EmployeeDetails(empobj.getEmpId()));
			}
			if (empobj.getWorkstationId() > 0) {
				wfdObj.setWorkstations(new SMWorkstations(empobj.getWorkstationId()));
			}
			if (empobj.getShiftId() > 0) {
				wfdObj.setShift(new SMShifts(empobj.getShiftId()));
			}
			if (request.getCreatedBy() > 0) {
				wfdObj.setCreatedBy(request.getCreatedBy());
			}
			if (empobj.getFromDt() != null) {
				wfdObj.setFromDate(smUtils.convertStringToTimestamp(empobj.getFromDt() + " 00:00:00.0 "));
			}
			if (empobj.getToDt() != null) {
				wfdObj.setToDate(smUtils.convertStringToTimestamp(empobj.getToDt() + " 00:00:00.0 "));
			}

			if (empobj.getDeptId() > 0) {
				wfdObj.setDept(new DepartmentMaster(empobj.getDeptId()));
			}
			if (empobj.getLineId() > 0) {
				wfdObj.setLine(new Line(empobj.getLineId()));
			}
			session.save(wfdObj);

//			Tuple emp = session.createNativeQuery(
//					" select e.emp_id as empId,e.cmpy_emp_id as compEmpId, concat(ifnull(e.first_name,''),' ',ifnull(e.last_name,'')) as empName,\r\n"
//							+ " b.name as branchName ,d.dept_name as deptName,l.name as lineName,o.portal_link as portalLink,o.logo as logo\r\n"
//							+ " from tbl_employee_details e \r\n"
//							+ " inner join master_branch b on b.branch_id =e.branch_id \r\n"
//							+ " left join master_department d on d.dept_id=e.dept_id \r\n"
//							+ " inner join dwm_line l on e.line_id =l.id \r\n"
//							+ " inner join master_organization o on o.org_id=b.org_id where e.emp_id=:empId limit 1",
//					Tuple.class).setParameter("empId", empobj.getEmpId()).getResultList().stream().findFirst()
//					.orElse(null);
//			if (emp != null && (CommonUtils.objectToString(emp.get("skillingId"))).length() > 0) {
//
//				List<MailDTO> mailList = new ArrayList<>();
//				LocalDate date = LocalDate.now();
//				EmailTemplateMaster messageContent = null;
//				messageContent = evonationConfig.getMessageContent(EnovationConstants.SKILLMATRIX_EMAIL_TEMPLATE);
//
//				if (messageContent != null) {
//					LOGGER.info("#messageContent added");
//
//					String subject = messageContent.getSubject().replaceAll(Pattern.quote("{subject}"),
//							CommonUtils.objectToString("OJT Initiative Commencing Today"));
//					String emailCon = env.getProperty("OJT_EMIAL_Body.body").replaceAll(Pattern.quote("{startDate}"),
//							date.toString());
//					String content = messageContent.getBody().replaceAll(Pattern.quote("{name}"),CommonUtils.objectToString(emp.get("empName")))
//							.replaceAll(Pattern.quote("{body}"), emailCon)
//							.replaceAll(Pattern.quote("{portalLink}"),
//									CommonUtils.objectToString(emp.get("portalLink")))
//							.replaceAll(Pattern.quote("{thisYear}"), (String.valueOf(LocalDate.now().getYear())))
//							.replaceAll(Pattern.quote("{plant}"), CommonUtils.objectToString(emp.get("branchName")))
//							.replaceAll(Pattern.quote("{dept}"), CommonUtils.objectToString(emp.get("deptName")))
//							.replaceAll(Pattern.quote("{cell}"), CommonUtils.objectToString(emp.get("skillingId")))
//							.replaceAll(Pattern.quote("{workstation}"),
//									CommonUtils.objectToString(emp.get("skillingId")))
//							.replaceAll(Pattern.quote("{level}"), CommonUtils.objectToString(emp.get("skillingId")))
//							.replaceAll(Pattern.quote("{companyLogo}"),
//									CommonUtils.objectToString(emp.get("logo")).length() > 0
//											? EnovationConfig.buddyConfig.get("ProfilePicPathUrl")
//													+ CommonUtils.objectToString(emp.get("logo"))
//											: EnovationConstants.myeNovationLogoPath);
//					MailDTO mail = new MailDTO(request.getEmailId(), subject, content);
//					mailList.add(mail);
//				}
//
//				if (!CollectionUtils.isEmpty(mailList)) {
//					LOGGER.info("#EMail sended");
//					taskExecutor.execute(new MailUtil(mailList, communication));
//				}
//			}
		}
		flag = true;

		return flag;
	}

	@Override
	public List<HashMap<String, Object>> getWorkForceDeploymentList(SkillMatrixRequest request,
			SkillMatrixResponse response) {
		LOGGER.info("# WorkflowDaoImpl || getWorkForceDeploymentList");
		Session session = getCurrentSession();

		StringBuilder sb = new StringBuilder("select sd.id as id, sd.from_date as fromDate, sd.to_date as toDate\r\n"
				+ " , sd.shift_id as shiftId, sd.workstations_id as workstation_id,\r\n"
				+ " ss.shift_name as shiftName ,mb.branch_id as branchId,sd.dept_id as deptId,d.dept_name as deptName,mb.name as branchName,l.id as lineId,\r\n"
				+ " l.name as lineName\r\n"
				+ " from sm_wf_deployment sd inner join sm_shifts ss on ss.id=sd.shift_id\r\n"
				+ " left join dwm_line l on l.id = sd.line_id\r\n"
				+ " inner join master_department d on d.dept_id = sd.dept_id\r\n"
				+ " inner join master_branch mb on mb.branch_id=d.branch_id where d.branch_id=:branchId \r\n");

		if (request.getDeptId() > 0) {
			sb.append(" and sd.dept_id=:deptId");
		}
		if (request.getLineId() > 0) {
			sb.append(" and  sd.line_id=:lineId ");
		}
		if (!CollectionUtils.isEmpty(request.getLineIds())) {
			sb.append(" AND sd.line_id IN (:lineIds) ");
		}
		if (request.getSearch() != null) {
			sb.append(" and (d.dept_name like '%{search}%' "
					+ " or l.name like '%{search}%' or ss.shift_name like '%{search}%' or sd.from_date like '%{search}%' or sd.to_date like '%{search}%' )");
		}

		sb.append(" group by sd.shift_id,d.dept_id,l.id,sd.from_date ");

		if (request.getColName() != null && request.getOrderType() != null) {
			sb.append(" order by " + ColumnAscDescConstants.wFDeployMentColName.get(request.getColName()) + " "
					+ request.getOrderType());
		} else {
			sb.append(" order by sd.id DESC ");
		}

		String tmpQuery = (sb.toString()).replace("{search}", String.valueOf(request.getSearch()));

		TypedQuery<Tuple> query = session.createNativeQuery(tmpQuery, Tuple.class).setParameter("branchId",
				request.getBranchId());
		if (request.getDeptId() > 0) {
			query.setParameter("deptId", request.getDeptId());
		}
		if (request.getLineId() > 0) {
			query.setParameter("lineId", request.getLineId());
		}
		if (!CollectionUtils.isEmpty(request.getLineIds())) {
			query.setParameter("lineIds", request.getLineIds());
		}
		List<Tuple> tupleList = query.getResultList();
		if (!CollectionUtils.isEmpty(tupleList)) {
			return CommonUtils.parseTupleList(tupleList);
		}
		return null;
	}

	@Override
	public HashMap<String, Object> getWorkForceDeploymentDetails(SkillMatrixRequest request,
			SkillMatrixResponse smResponse) {
		LOGGER.info("# WorkflowDaoImpl || getWorkForceDeploymentDetails");
		Session session = getCurrentSession();
		HashMap<String, Object> response = new HashMap<>();
		List<HashMap<String, Object>> dataList = new ArrayList<>();
		List<HashMap<String, Object>> columnList = new ArrayList<>();
		if (request.getOffset() == 0) {
			smResponse.setTotalCount(getSkillMatrixEmpListCount(request, session));
		}
		List<SMWorkstations> workstationList = smUtils.getWorkstationListLevelWise(request, session);
		List<HashMap<String, Object>> list = getRegiEmpList(request, session);
		if (!CollectionUtils.isEmpty(list)) {
			response.put("empList", list);
		}
		for (SMWorkstations workstation : workstationList) {
			HashMap<String, Object> columnMap = new HashMap<>();
			columnMap.put("heading", workstation.getWorkstation());
			columnMap.put("field", workstation.getWorkstation());
			columnMap.put("workstationId", workstation.getId());
			columnMap.put("level", workstation.getReqSkillLevel().getLevelName());
			columnList.add(columnMap);
		}
		response.put("columns", columnList);
//		response.put("tableData", dataList);
		return response;
	}

	private List<HashMap<String, Object>> getRegiEmpList(SkillMatrixRequest request, Session session) {
		StringBuilder sb = new StringBuilder("  select sd.workstations_id as workstationId,sd.shift_id as shiftId,\r\n"
				+ " concat(ifnull(e.first_name,''),' ',ifnull(e.last_name,'')) as empName,sd.emp_id as empId,e.cmpy_emp_id AS companyEmpId,\r\n"
				+ " sw.workstation as workstationName,sl.level_name as currentSkillLevel,sl.id as currentSkillLevelId,"
				+ " sd.from_date as fromDate ,sd.to_date as toDate,l.id as lineId,l.name as line \r\n"
				+ " from sm_wf_deployment sd \r\n" + " inner join master_department d on d.dept_id = sd.dept_id \r\n"
				+ " inner join master_branch mb on mb.branch_id=d.branch_id \r\n"
				+ " inner join tbl_employee_details e on e.emp_id=sd.emp_id \r\n"
				+ " inner join sm_workstations sw on sw.id=sd.workstations_id \r\n"
				+ " left join sm_emp_skill_matrix sm on sm.emp_id=sd.emp_id and sm.workstations_id=sd.workstations_id\r\n"
				+ " left join sm_skill_level sl on sl.id=sm.skill_level_id "
				+ " left join dwm_line l on l.id = sd.line_id  \r\n"
				+ " where d.branch_id=:branchId and sd.shift_id=:shiftId ");

		if (request.getDeptId() > 0) {
			sb.append(" and sd.dept_id=:deptId");
		}
		if (request.getLineId() > 0) {
			sb.append(" and sd.line_id=:lineId ");
		}
		if (request.getFromDt() != null && request.getToDt() != null) {
			sb.append(" and sd.from_date=:fromDate and sd.to_date=:toDate ");
//			sb.append(" and sd.from_date=':fromDate 00:00:00' and sd.to_date=':toDate 23:59:59' ");

		}

		TypedQuery<Tuple> query = session.createNativeQuery(sb.toString(), Tuple.class)
				.setParameter("branchId", request.getBranchId()).setParameter("shiftId", request.getShiftId());

		if (request.getDeptId() > 0) {
			query.setParameter("deptId", request.getDeptId());
		}
		if (request.getLineId() > 0) {
			query.setParameter("lineId", request.getLineId());
		}
		if (request.getFromDt() != null && request.getToDt() != null) {
			query.setParameter("fromDate", request.getFromDt());
			query.setParameter("toDate", request.getToDt());
		}

		List<Tuple> tupleList = query.getResultList();
		if (!CollectionUtils.isEmpty(tupleList)) {
			return CommonUtils.parseTupleList(tupleList);
		}
		return null;
	}

	@Override
	public SkillMatrixResponse getOJTRegistrationDetail(long oJTRegiId, SkillMatrixResponse response) {
		LOGGER.info("# WorkflowDaoImpl || getOJTRegistrationDetail");
		Session session = getCurrentSession();

		HashMap<String, Object> regiMap = smUtils.getRegistrationList(session, oJTRegiId);
		List<Tuple> skilling = smUtils.getSkillList(session, oJTRegiId);

		List<Map<String, Object>> skillingList = new ArrayList<>();

		for (Tuple skill : skilling) {
			Map<String, Object> skillMap = new HashMap<>();
			skillMap.put("skillingId", CommonUtils.objectToLong(skill.get("skillingId")));
			skillMap.put("createdBy", CommonUtils.objectToInt(skill.get("createdBy")));
			skillMap.put("status", skill.get("status"));
			skillMap.put("createdByName", CommonUtils.objectToString(skill.get("createdByName")));
			skillMap.put("checksheetId", CommonUtils.objectToLong(skill.get("checksheetId")));
			skillMap.put("ojtRegisId", CommonUtils.objectToLong(skill.get("ojtRegisId")));
			skillMap.put("assessmentDueDate", skill.get("assessmentDueDate"));

			skillMap.put("assessmentId", CommonUtils.objectToLong(skill.get("assessmentId")));
			skillingList.add(skillMap);

			List<HashMap<Object, List<SMOJTSkillingAuditDTO>>> daywiswList = new ArrayList<>();
			SkillMatrixRequest request = new SkillMatrixRequest();
			request.setSkillingId(CommonUtils.objectToLong(skill.get("skillingId")));

			List<SMOJTSkillingAuditDTO> auditList = smUtils.getAuditList(session, request);

			Map<Integer, List<SMOJTSkillingAuditDTO>> groupDayAudit = auditList.stream()
					.collect(Collectors.groupingBy(SMOJTSkillingAuditDTO::getDayNo));

			for (Map.Entry<Integer, List<SMOJTSkillingAuditDTO>> entry : groupDayAudit.entrySet()) {
				HashMap<Object, List<SMOJTSkillingAuditDTO>> dayWiseSkilling = new HashMap();

				for (SMOJTSkillingAuditDTO skillingAudit : entry.getValue()) {
					SkillMatrixRequest req = new SkillMatrixRequest();

					req.setSkillingAuditId(skillingAudit.getSkillingAuditId());
					req.setSkillingId(skillingAudit.getSkillingId());

					List<SMOJTCSPointsAuditDTO> auditPointList = smUtils.getSMOJTAuditPointDetails(session, req);
					if (!CollectionUtils.isEmpty(auditPointList)) {
						skillingAudit.setAuditPointList(auditPointList);
					}

					List<SMOJTSkillingParameterDTO> paramList = smUtils.getSMOJTSkillingParameterList(session, req);
					if (!CollectionUtils.isEmpty(paramList)) {
						skillingAudit.setParameterList(paramList);
					}

					dayWiseSkilling.put("dayAudit", entry.getValue());
					daywiswList.add(dayWiseSkilling);

				}
				skillMap.put("daywiswList", daywiswList);
//	        response.setDayWiseAuditList(daywiswList);

				regiMap.put("skilling", skillingList);
				response.setRegiList(regiMap);
			}
		}

		return response;
	}

	public List<HashMap<String, Object>> getOJTRegistrationList(SkillMatrixRequest request,
			SkillMatrixResponse response) {
		LOGGER.info("# WorkflowDaoImpl || getOJTRegistrationList");
		Session session = getCurrentSession();
		if (request.getOffset() == 0) {
			response.setTotalCount(getOJTRegistrationListCount(session, request));
		}
		StringBuilder builder = new StringBuilder("SELECT sor.id AS ojtRegisId,\r\n"
				+ "CONCAT(IFNULL(e.first_name, ''), ' ', IFNULL(e.last_name, '')) AS empName,CONCAT(IFNULL(ed.first_name, ''), ' ', IFNULL(ed.last_name, '')) AS tlName,\r\n"
				+ "smos.created_date AS registeredDate,sor.status AS status,sa.assessment_status AS assessmentStatus,sor.desired_skill_level_id AS levelId,\r\n"
				+ "sl.level_name AS level,sor.branch_id AS branchId,sor.dept_id AS deptId,\r\n"
				+ "sor.line_id AS lineId,sor.workstation_id AS workStationId,sor.emp_id AS empId,\r\n"
				+ "e.cmpy_emp_id AS companyEmpId,ed.cmpy_emp_id AS tlEmpId,\r\n"
				+ "mb.name AS branchName, md.dept_name AS deptName,ln.name AS lineName,\r\n"
				+ "sw.workstation AS workstationName,sop.start_date as ojtStartDate,sjp.id as ojtPlanId  FROM sm_ojt_regis sor\r\n"
				+ "Inner join sm_ojt_skilling smos on smos.ojt_regis_id = sor.id\r\n"
				+ "left join sm_ojt_plan sop on sop.id=sor.ojt_plan_id\r\n"
				+ "INNER JOIN master_branch mb ON mb.branch_id = sor.branch_id\r\n"
				+ "INNER JOIN master_department md ON md.dept_id = sor.dept_id\r\n"
				+ "INNER JOIN dwm_line ln ON ln.id=sor.line_id\r\n"
				+ "INNER JOIN sm_workstations sw ON sw.id = sor.workstation_id\r\n"
				+ "INNER JOIN sm_skill_level sl ON sl.id = sor.desired_skill_level_id \r\n"
				+ "INNER JOIN tbl_employee_details e ON e.emp_id = sor.emp_id \r\n"
				+ "LEFT JOIN sm_ojt_assessment sa ON sa.ojt_regis_id=sor.id\r\n"
				+ "left join sm_ojt_plan sjp on sjp.id = sor.ojt_plan_id\r\n"
				+ "left JOIN tbl_employee_details ed ON ed.emp_id = sjp.created_by   \r\n"
				+ "WHERE mb.org_id =:orgId ");

		if (request.getBranchId() > 0) {
			builder.append(" and sor.branch_id = :branchId ");
		}
		if (!CollectionUtils.isEmpty(request.getDeptIds())) {
			builder.append(" and sor.dept_id IN (:deptIds)");
		}
		if (request.getDeptId() > 0) {
			builder.append(" and sor.dept_id IN (:deptId) ");
		}
		if (!CollectionUtils.isEmpty(request.getLineIds())) {
			builder.append(" and sor.line_id IN (:lineIds) ");
		}
		if (!CollectionUtils.isEmpty(request.getWorkstationIds())) {
			builder.append(" and sor.workstation_id IN (:workstationIds) ");
		}
		if (request.getEmpId() > 0) {
			builder.append(" and sor.emp_id = :empId ");
		}
		if (request.getSkillLevelId() > 0) {
			builder.append(" and sor.desired_skill_level_id = :levelId ");
		}
		if (request.getSearch() != null) {
			builder.append(
					" and (concat(ifnull(e.first_name,''),' ',ifnull(e.last_name,'')) like '%{search}%' or mb.name like '%{search}%' or md.dept_name like '%{search}%' "
							+ " or sor.status like '%{search}%' or sor.created_date like '%{search}%' or sa.assessment_status like '{search}' or e.cmpy_emp_id like '{search}' ) ");
		}
		builder.append(" group by sor.id ");
		if (request.getColName() != null && request.getOrderType() != null) {
			builder.append(" order by " + ColumnAscDescConstants.smOJTRegisListColName.get(request.getColName()) + " "
					+ request.getOrderType());
		} else {
			builder.append(" order by sor.id DESC ");
		}

		String tmpQuery = builder.toString().replace("{search}", (String.valueOf(request.getSearch())));

		TypedQuery<Tuple> query = session.createNativeQuery(tmpQuery, Tuple.class).setParameter("orgId",
				request.getOrgId());

		if (request.getBranchId() > 0) {
			query.setParameter("branchId", request.getBranchId());
		}
		if (request.getDeptId() > 0) {
			query.setParameter("deptId", request.getDeptId());
		}
		if (!CollectionUtils.isEmpty(request.getDeptIds())) {
			query.setParameter("deptIds", request.getDeptIds());
		}
		if (!CollectionUtils.isEmpty(request.getLineIds())) {
			query.setParameter("lineIds", request.getLineIds());
		}
		if (request.getEmpId() > 0) {
			query.setParameter("empId", request.getEmpId());
		}
		if (!CollectionUtils.isEmpty(request.getWorkstationIds())) {
			query.setParameter("workstationIds", request.getWorkstationIds());
		}
		if (request.getSkillLevelId() > 0) {
			query.setParameter("levelId", request.getSkillLevelId());
		}
		if (request.getLimit() > 0) {
			query.setFirstResult(request.getOffset()).setMaxResults(request.getLimit());
		}

		List<Tuple> objList = query.getResultList();
		if (!CollectionUtils.isEmpty(objList)) {
			return CommonUtils.parseTupleList(objList);
		} else {
			return null;
		}
	}

	private int getOJTRegistrationListCount(Session session, SkillMatrixRequest request) {
		LOGGER.info("# WorkflowDaoImpl || getOJTRegistrationListCount");
		StringBuilder builder = new StringBuilder("SELECT sor.id AS ojtRegisId FROM sm_ojt_regis sor\r\n"
				+ "Inner join sm_ojt_skilling smos on smos.ojt_regis_id = sor.id\r\n"
				+ "left join sm_ojt_plan sop on sop.id=sor.ojt_plan_id\r\n"
				+ "INNER JOIN master_branch mb ON mb.branch_id = sor.branch_id\r\n"
				+ "INNER JOIN master_department md ON md.dept_id = sor.dept_id\r\n"
				+ "INNER JOIN dwm_line ln ON ln.id=sor.line_id\r\n"
				+ "INNER JOIN sm_workstations sw ON sw.id = sor.workstation_id\r\n"
				+ "INNER JOIN sm_skill_level sl ON sl.id = sor.desired_skill_level_id \r\n"
				+ "INNER JOIN tbl_employee_details e ON e.emp_id = sor.emp_id \r\n"
				+ "LEFT JOIN sm_ojt_assessment sa ON sa.ojt_regis_id=sor.id\r\n"
				+ "left join sm_ojt_plan sjp on sjp.id = sor.ojt_plan_id\r\n"
				+ "left JOIN tbl_employee_details ed ON ed.emp_id = sjp.created_by   \r\n"
				+ "WHERE mb.org_id =:orgId ");

		if (request.getBranchId() > 0) {
			builder.append(" and sor.branch_id =:branchId ");
		}
		if (!CollectionUtils.isEmpty(request.getDeptIds())) {
			builder.append(" and sor.dept_id IN (:deptIds)");
		}
		if (request.getDeptId() > 0) {
			builder.append(" and sor.dept_id IN (:deptId) ");
		}
		if (!CollectionUtils.isEmpty(request.getLineIds())) {
			builder.append(" and sor.line_id IN (:lineIds) ");
		}
		if (!CollectionUtils.isEmpty(request.getWorkstationIds())) {
			builder.append(" and sor.workstation_id IN (:workstationIds) ");
		}
		if (request.getEmpId() > 0) {
			builder.append(" and sor.emp_id = :empId ");
		}
		if (request.getSkillLevelId() > 0) {
			builder.append(" and sor.desired_skill_level_id = :levelId ");
		}
		if (request.getSearch() != null) {
			builder.append(
					" and (concat(ifnull(e.first_name,''),' ',ifnull(e.last_name,'')) like '%{search}%' or mb.name like '%{search}%' or md.dept_name like '%{search}%' "
							+ " or sor.status like '%{search}%' or sor.created_date like '%{search}%' or sa.assessment_status like '{search}' or e.cmpy_emp_id like '{search}' ) ");
		}
		builder.append(" group by sor.id ");
		String tmpQuery = builder.toString().replace("{search}", (String.valueOf(request.getSearch())));

		TypedQuery<Tuple> query = session.createNativeQuery(tmpQuery, Tuple.class).setParameter("orgId",
				request.getOrgId());

		if (request.getBranchId() > 0) {
			query.setParameter("branchId", request.getBranchId());
		}
		if (request.getDeptId() > 0) {
			query.setParameter("deptId", request.getDeptId());
		}
		if (!CollectionUtils.isEmpty(request.getDeptIds())) {
			query.setParameter("deptIds", request.getDeptIds());
		}
		if (!CollectionUtils.isEmpty(request.getLineIds())) {
			query.setParameter("lineIds", request.getLineIds());
		}
		if (request.getEmpId() > 0) {
			query.setParameter("empId", request.getEmpId());
		}
		if (!CollectionUtils.isEmpty(request.getWorkstationIds())) {
			query.setParameter("workstationIds", request.getWorkstationIds());
		}
		if (request.getSkillLevelId() > 0) {
			query.setParameter("levelId", request.getSkillLevelId());
		}

		List<Tuple> objList = query.getResultList();
		if (!CollectionUtils.isEmpty(objList)) {
			return objList.size();
		}
		return 0;
	}

	@Override
	public boolean safteyAssessmentSubmission(SkillMatrixRequest request) {
		LOGGER.info("# WorkflowDaoImpl || safteyAssessmentSubmission");
		Session session = getCurrentSession();
		boolean flag = false;

		if (!CollectionUtils.isEmpty(request.getQueList())) {

			for (SMAssessmetDTO queDto : request.getQueList()) {

				SMOJTQuetionDTO ojtAsseQue = null;
				ojtAsseQue = smUtils.getOJTAssessmentQuesId(session, queDto, request);

				if (!CollectionUtils.isEmpty(queDto.getQueOptionList())
						&& ojtAsseQue.getQueType().equals(SMConstant.QUESTION_TYPE)) {
					for (SMAssessmetDTO optionDto : queDto.getQueOptionList()) {
						smWorker.saveQueAnsCalculateMarks(session, optionDto, ojtAsseQue, request, queDto);
					}
				} else {
					smWorker.updateQuestionAnswer(session, queDto, ojtAsseQue);
				}
			}
		}
		smWorker.saveAssesmentResultDetails(session, request);
		flag = true;

		return flag;
	}

	@SuppressWarnings("unused")
	@Override
	public boolean updateAssessmentTime(SkillMatrixRequest request) {
		boolean flag = false;
		Session session = getCurrentSession();

		return smUtils.updateTotalAssessedTime(session, request.getSkillingAuditId(),
				request.getAssessmentTimeInterval());
	}

}