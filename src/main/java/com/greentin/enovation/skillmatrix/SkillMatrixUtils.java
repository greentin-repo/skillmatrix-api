package com.greentin.enovation.skillmatrix;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;

import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
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
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import com.greentin.enovation.config.EnovationConfig;
import com.greentin.enovation.dto.MailDTO;
import com.greentin.enovation.dto.SMAssessmentDTO;
import com.greentin.enovation.dto.SMAssessmetDTO;
import com.greentin.enovation.dto.SMOJTAssessmentDTO;
import com.greentin.enovation.dto.SMOJTPlanDTO;
import com.greentin.enovation.dto.SMOJTCSPointsAuditDTO;
import com.greentin.enovation.dto.SMOJTChecksheetDTO;
import com.greentin.enovation.dto.SMOJTQuetionDTO;
import com.greentin.enovation.dto.SMOJTRegisDTO;
import com.greentin.enovation.dto.SMOJTSkillingAuditDTO;
import com.greentin.enovation.dto.SMOJTSkillingParameterDTO;
import com.greentin.enovation.dto.SMShiftDTO;
import com.greentin.enovation.dto.SkillMatrixRequest;
import com.greentin.enovation.model.BranchMaster;
import com.greentin.enovation.model.DepartmentMaster;
import com.greentin.enovation.model.EmailTemplateMaster;
import com.greentin.enovation.model.EmployeeDetails;
import com.greentin.enovation.model.dwm.Line;
import com.greentin.enovation.response.SkillMatrixResponse;
import com.greentin.enovation.utils.ColumnAscDescConstants;
import com.greentin.enovation.utils.CommonUtils;
import com.greentin.enovation.utils.EnovationConstants;
import com.greentin.enovation.utils.EnovationException;
import com.greentin.enovation.utils.WriteFilesUtils;

@Component
public class SkillMatrixUtils {

	@Autowired
	SkillMatrixWorker smWorker;

	private static final Logger LOGGER = LoggerFactory.getLogger(SkillMatrixUtils.class);

	public Tuple getQuestionDetails(SkillMatrixRequest request, Session session) {
		LOGGER.info("#In SkillMatrixUtils |  INSIDE in getQuestionDetails ");
		return session.createNativeQuery(
				"  select saq.qestion as qestion , saq.ques_type_id as queType, saq.id as id , saq.assessment_id as assessmentId \r\n"
						+ "	 from sm_assessment_ques saq where saq.id=:id ",
				Tuple.class).setParameter("id", request.getAssessmentQueId()).getResultList().stream().findFirst()
				.orElse(null);
	}

	public Tuple getOptionDetails(Session session, long id) {
		LOGGER.info("#In SkillMatrixUtils |  INSIDE in getOptionDetails ");
		return session.createNativeQuery(
				"select sao.is_right_ans as isRightAns,sao.opt as opt,sao.ques_id as questionId from sm_assessment_options sao where sao.id=:id ",
				Tuple.class).setParameter("id", id).getResultList().stream().findFirst().orElse(null);
	}

	public void calculateTotalMark(Session session, long assessmentId) {
		LOGGER.info("# SkillMatrixUtils | calculateTotalMark");
		int totalScore = ((Number) session.createNativeQuery(
				"select ifnull(sum(que_marks),0) from sm_assessment_ques where is_active=1 and assessment_id =:assessId")
				.setParameter("assessId", assessmentId).getSingleResult()).intValue();

		// update SMAssessment
		session.createNativeQuery("update sm_assessment set total_marks=:totalMark  where id=:assessId")
				.setParameter("totalMark", totalScore).setParameter("assessId", assessmentId).executeUpdate();
	}

	@SuppressWarnings("unchecked")
	public boolean isQueNotExist(Session session, SkillMatrixRequest request) {
		LOGGER.info("# SkillMatrixUtils | isNotQueExist");

		LOGGER.info("# SkillMatrixUtils | isTitleExist");
		StringBuilder sb = new StringBuilder(
				"select ifnull((select coalesce(count(id),0) from sm_assessment_ques where is_active=1 and assessment_id=:assessId "
						+ "and ques_type_id=:queType and qestion=:question ");

		if (request.getAssessmentQueId() > 0) {
			sb.append(" and id !=:id ");
		}
		sb.append(" ),0) as count ");

		TypedQuery<Tuple> query = session.createNativeQuery(sb.toString());

		if (request.getAssessmentQueId() > 0) {
			query.setParameter("id", request.getAssessmentQueId());
		}

		return ((Number) query.setParameter("assessId", request.getAssessmentId())
				.setParameter("queType", request.getQuestionTypeId()).setParameter("question", request.getQuestion())
				.getSingleResult()).intValue() == 0;
	}

	public Tuple getAssesmentDetails(SkillMatrixRequest request, Session session) {
		LOGGER.info("# SkillMatrixUtils | getAssesmentDetails");
		return session.createNativeQuery(
				"select id as id , passing_marks as passingMarks , time as time , title as title, updated_by as updatedBy, branch_id as branchId \r\n"
						+ " ,skill_level_id as skillLvlId ,assessment_type as assessmentType \r\n"
						+ " from sm_assessment where id=:id ",
				Tuple.class).setParameter("id", request.getAssessmentId()).getResultList().stream().findFirst()
				.orElse(null);
	}

	// Added by Sonali L. Jan 15 2024 - added field assessmentType
	public void setDtoParameters(List<Tuple> tupleList, List<SkillMatrixRequest> dtoList) {
		LOGGER.info("# SkillMatrixUtils | setDtoParameters");
		for (Tuple x : tupleList) {
			SkillMatrixRequest dto = new SkillMatrixRequest();
			dto.setAssessmentId(CommonUtils.objectToLong(x.get("id")));
			dto.setAssessmentQueId(CommonUtils.objectToLong(x.get("quetionId")));
			dto.setAssessmentQueOptId(CommonUtils.objectToLong(x.get("optionId")));
			dto.setBranchId(CommonUtils.objectToInt(x.get("branchId")));
			dto.setCreatedBy(CommonUtils.objectToInt(x.get("createdBy")));
			dto.setPassingMark(CommonUtils.objectToDouble(x.get("passingMarks")));
			dto.setTime(CommonUtils.objectToInt(x.get("time")));
			dto.setQuestion(CommonUtils.objectToString(x.get("qestion")));
			dto.setOption(CommonUtils.objectToString(x.get("opt")));
			dto.setBranchName(CommonUtils.objectToString(x.get("branchName")));
			dto.setTotalMarks(CommonUtils.objectToDouble(x.get("totalMark")));
			dto.setQueMark(CommonUtils.objectToDouble(x.get("questionMark")));
			dto.setCategoryId(CommonUtils.objectToLong(x.get("categoryId")));
			dto.setCategoryName(CommonUtils.objectToString(x.get("categoryName")));
			dto.setRightAns(CommonUtils.objectToString(x.get("isRightAns")).equals("true"));
			dto.setTitle(CommonUtils.objectToString(x.get("title")));
			dto.setSkillLvlId(CommonUtils.objectToLong(x.get("skillLevelId")));
			dto.setSkillLevel(CommonUtils.objectToString(x.get("skillLevel")));
			dto.setStatus(CommonUtils.objectToString(x.get("status")));
			dto.setDeptName(CommonUtils.objectToString(x.get("deptName")));
			dto.setDeptId(CommonUtils.objectToInt(x.get("deptId")));
			dto.setLineName(CommonUtils.objectToString(x.get("lineName")));
			dto.setLineId(CommonUtils.objectToLong(x.get("lineId")));
			dto.setWorkstation(CommonUtils.objectToString(x.get("workstation")));
			dto.setWorkstationId(CommonUtils.objectToLong(x.get("workstationId")));
			dto.setAssessmentType(CommonUtils.objectToString(x.get("assessmentType")));
			dtoList.add(dto);
		}
	}

	public Tuple getChecksheetForAudit(SkillMatrixRequest request, Session session) {
		LOGGER.info("# SkillMatrixUtils | getChecksheetForAudit");
		return session
				.createNativeQuery(
						"select id as id , no_of_days as noOfDays, title as title, branch_id as branchId, \r\n"
								+ " skill_level_id as skillLvlId from sm_checksheet where id=:id",
						Tuple.class)
				.setParameter("id", request.getCheckSheetId()).getResultList().stream().findFirst().orElse(null);
	}

	@SuppressWarnings("unchecked")
	public boolean isTitleExist(Session session, SkillMatrixRequest request) {
		LOGGER.info("# SkillMatrixUtils | isTitleExist");
		StringBuilder sb = new StringBuilder(
				"select ifnull((select coalesce(count(id),0) from sm_checksheet where is_active=1 and branch_id=:branchId "
						+ "and skill_level_id=:skillId and dept_id=:deptId and line_id=:lineId and workstation_id =:workstationId ");

		if (request.getCheckSheetId() > 0) {
			sb.append(" and id !=:id ");
		}
		sb.append(" ),0) as workFlowCnt  ");

		TypedQuery<Tuple> query = session.createNativeQuery(sb.toString());

		if (request.getCheckSheetId() > 0) {
			query.setParameter("id", request.getCheckSheetId());
		}

		return ((Number) query.setParameter("branchId", request.getBranchId())
				.setParameter("skillId", request.getSkillLvlId()).setParameter("deptId", request.getDeptId())
				.setParameter("lineId", request.getLineId()).setParameter("workstationId", request.getWorkstationId())
				.getSingleResult()).intValue() == 0;

	}

	public Tuple getChecksheetPointDetails(SkillMatrixRequest point, Session session) {
		LOGGER.info("# SkillMatrixUtils | getChecksheetPointDetails");
		return session.createNativeQuery(
				"select id as id , day_no as dayNo, item_name as itemName,reference as reference, checksheet_id as checksheetId from sm_checksheet_points where id=:id",
				Tuple.class).setParameter("id", point.getId()).getResultList().stream().findFirst().orElse(null);
	}

	@SuppressWarnings("unchecked")
	public boolean isItemNameExist(Session session, SkillMatrixRequest point, SkillMatrixRequest day,
			SkillMatrixRequest request) {
		LOGGER.info("# SkillMatrixUtils | isItemNameExist");

		StringBuilder sb = new StringBuilder(
				"select ifnull((select coalesce(count(id),0) from sm_checksheet_points where is_active=1  and reference=:reference and "
						+ "checksheet_id=:checksheetId and item_name=:itemName and day_no=:dayNo ");

		if (point.getId() > 0) {
			sb.append(" and  id !=:pointId ");
		}
		sb.append(" ),0) as chechsheet ");

		TypedQuery<Tuple> query = session.createNativeQuery(sb.toString());

		if (point.getId() > 0) {
			query.setParameter("pointId", point.getId());
		}

		return ((Number) query.setParameter("checksheetId", request.getCheckSheetId())
				.setParameter("itemName", point.getItemName()).setParameter("dayNo", day.getDayNo())
				.setParameter("reference", point.getReference()).getSingleResult()).intValue() == 0;
	}

	// Added by Sonali L. Jan 15 2024 - added field assessmentType
	public TypedQuery<Tuple> getAssessmentDetails(long assessmentId, Session session) {
		LOGGER.info("# SkillMatrixUtils | getAssessmentDetails");

		StringBuilder sb = new StringBuilder(
				"select sa.id as id, sa.title as title, sa.skill_level_id as skillLevelId , sa.branch_id as branchId, \r\n"
						+ " sa.time as time , sa.passing_marks as passingMarks,sa.total_marks as totalMark, \r\n"
						+ " sa.created_by as createdBy,sa.created_date as createdDate ,b.name as branchName,sl.level_name as skillLevel ,saq.id as quetionId,\r\n"
						+ "	saq.qestion as qestion,saq.que_marks as questionMark ,saq.category_id as categoryId,c.category_name as categoryName,\r\n"
						+ " sao.id as optionId,sao.opt as opt\r\n"
						+ "	,sao.is_right_ans as isRightAns,sa.status as status,l.name as lineName,l.id as lineId,\r\n"
						+ " d.dept_id as deptId,d.dept_name as deptName,sw.id as workstationId , sw.workstation as workstation,sa.assessment_type as assessmentType \r\n"
						+ "  from  sm_assessment sa \r\n"
						+ "  left join master_branch b on b.branch_id=sa.branch_id \r\n"
						+ "  left join sm_skill_level sl on sl.id=sa.skill_level_id\r\n"
						+ "	 left join sm_assessment_ques saq on sa.id=saq.assessment_id and saq.is_active=1 \r\n"
						+ "	 left join sm_category c on c.id=saq.category_id\r\n"
						+ "  left join sm_assessment_options sao on saq.id = sao.ques_id"
						+ "  left join master_department d on d.dept_id=sa.dept_id\r\n"
						+ "  left join dwm_line l on l.id=sa.line_id\r\n"
						+ "  left join sm_workstations sw on sw.id = sa.workstation_id \r\n"
						+ "	 where sa.is_active=1 and sa.id=:id");
		return session.createNativeQuery(sb.toString(), Tuple.class).setParameter("id", assessmentId);
	}

	public long saveOjtRegistrationObj(Session session, SkillMatrixRequest request) {
		LOGGER.info("# SkillMatrixUtils || saveOjtRegistrationObj");

		SMOJTRegis obj = new SMOJTRegis();

		if (request.getCreatedBy() > 0) {
			obj.setCreatedBy(request.getCreatedBy());
		}

		if (request.getEmpId() > 0) {
			obj.setEmpDetails(new EmployeeDetails(request.getEmpId()));
		}

		if (request.getTrainerEmpId() > 0) {
			obj.setTrainerDetails(new EmployeeDetails(request.getTrainerEmpId()));
		}

		if (request.getCurrentSkillLevelId() > 0) {
			obj.setCurrentSkillLevel(new SMSkillLevel(request.getCurrentSkillLevelId()));
		}

		if (request.getDesiredSkillLevelId() > 0) {
			obj.setDesiredSkillLevel(new SMSkillLevel(request.getDesiredSkillLevelId()));
		}

		if (request.getWorkstationId() > 0) {
			obj.setWorkstation(new SMWorkstations(request.getWorkstationId()));
		}
		obj.setStatus(EnovationConstants.COMPLETED_STRING);

		session.save(obj);

		return obj.getId();

	}

	public long saveOjtSkillingObj(Session session, SkillMatrixRequest request, long regisId) {
		LOGGER.info("# SkillMatrixUtils || saveOjtSkillingObj");

		SMOJTSkilling obj = new SMOJTSkilling();

		obj.setOjtRegis(new SMOJTRegis(regisId));

		if (request.getCheckSheetId() > 0) {
			obj.setChecksheet(new SMChecksheet(request.getCheckSheetId()));
		}
		obj.setStatus(EnovationConstants.PENDING_STRING);

		if (request.getCreatedBy() > 0) {
			obj.setCreatedBy(request.getCreatedBy());
		}
		session.save(obj);

		return obj.getId();
	}

	public List<Tuple> getChecksheetPointsId(Session session, long checksheetId, int dayNo) {
		LOGGER.info("# SkillMatrixUtils || getChecksheetPointsId");
		return session
				.createNativeQuery("select id as checksheetPointsId,day_no as dayNo from sm_checksheet_points \r\n"
						+ "where checksheet_id=:checksheetId and day_no=:dayNo and is_active=1 ", Tuple.class)
				.setParameter("checksheetId", checksheetId).setParameter("dayNo", dayNo).getResultList();
	}

	public String savePathOnServer(SkillMatrixRequest request, SMMasterCertificate obj) {
		LOGGER.info("# SkillMatrixUtils || savePathOnServer");

		String docDirectory = EnovationConfig.buddyConfig.get(EnovationConstants.SugesstionImgPath) + "/"
				+ EnovationConstants.SKILLMATRIX_FOLDER_NAME;
		String filePathToTrim = EnovationConstants.SKILLMATRIX_FOLDER_NAME;

		String path = WriteFilesUtils.writeFileOnServer(request.getCertificatePath(), docDirectory, filePathToTrim);

		return path;
	}

	public Tuple getMasterCertificateDetails(long certificateId, Session session) {
		LOGGER.info("# SkillMatrixUtils || getMasterCertificateDetails");
		return session.createNativeQuery(
				"SELECT smmcer.id AS certificateId,smmcer.certificate_caption AS certificateCaption,smmcer.certificate_name AS certificateName,\r\n"
						+ "smmcer.certificate_path AS certificatePath,smmcer.status AS status FROM sm_master_certificate smmcer where id=:cretiId ",
				Tuple.class).setParameter("cretiId", certificateId).getResultList().stream().findFirst().orElse(null);
	}

	@SuppressWarnings("unchecked")
	public boolean isSmParameterExist(Session session, SkillMatrixRequest request) {
		LOGGER.info("# SkillMatrixUtils | isSmParameterExist");

		LOGGER.info("# SkillMatrixUtils | isItemNameExist");
		StringBuilder sb = new StringBuilder(
				"select ifnull((select coalesce(count(id),0) from sm_checksheet_parameter where is_active=1 and "
						+ " checksheet_id=:checksheetId and parameter=:parameter and parameter_type_id=:parameterType ");

		if (request.getParameterId() > 0) {
			sb.append(" and  id != :paramId ");
		}
		sb.append(" ),0) as parameter ");

		TypedQuery<Tuple> query = session.createNativeQuery(sb.toString());

		if (request.getParameterId() > 0) {
			query.setParameter("paramId", request.getParameterId());
		}

		return ((Number) query.setParameter("checksheetId", request.getCheckSheetId())
				.setParameter("parameterType", request.getParameterTypeId())
				.setParameter("parameter", request.getParameter()).getSingleResult()).intValue() == 0;
	}

	public Tuple getChecksheetParameterDetails(SkillMatrixRequest request, Session session) {
		LOGGER.info("# SkillMatrixUtils | getChecksheetPointDetails");
		return session.createNativeQuery(
				"select id as id , parameter as parameter,parameter_type_id as parameterTypeId,checksheet_id as checksheetId,\r\n"
						+ " created_by as createdBy,updated_by as updatedBy from sm_checksheet_parameter where id=:id ",
				Tuple.class).setParameter("id", request.getParameterId()).getResultList().stream().findFirst()
				.orElse(null);
	}

	public List<HashMap<String, Object>> getPoints(Session session, long checkSheetId) {
		LOGGER.info("# SkillMatrixUtils || getPoints");
		List<HashMap<String, Object>> list = new ArrayList<>();

		StringBuilder sb = new StringBuilder(
				"select scp.id as checksheetPointId,scp.day_no as dayNo,scp.item_name as itemName , scp.checksheet_id as checksheetId,scp.reference as reference,\r\n"
						+ "	scp.created_by as createdBy from sm_checksheet_points scp"
						+ " where scp.is_active=1 and scp.checksheet_id=:checksheetId ");
		TypedQuery<Tuple> query = session.createNativeQuery(sb.toString(), Tuple.class).setParameter("checksheetId",
				checkSheetId);

		List<Tuple> tupleList = query.getResultList();
		LOGGER.info("# tupleList size : {}", tupleList.size());
		if (!CollectionUtils.isEmpty(tupleList)) {
			return CommonUtils.parseTupleList(tupleList);
		}
		return list;
	}

	public List<HashMap<String, Object>> getParameters(Session session, long checkSheetId) {
		LOGGER.info("# SkillMatrixUtils | getParameters ");
		List<HashMap<String, Object>> list = new ArrayList<>();
		StringBuilder sb = new StringBuilder(
				"  select scp.id as id , scp.parameter as parameter,scp.parameter_type_id as parameterTypeId,scp.checksheet_id as checksheetId,\r\n"
						+ "	scp.created_by as createdBy,scp.updated_by as updatedBy,spt.type_name as parameterType,scp.cycle_value as cycleValue"
						+ " from sm_checksheet_parameter scp left join sm_parameter_type spt \r\n"
						+ " on spt.id=scp.parameter_type_id  where scp.is_active=1 and scp.checksheet_id=:checksheetId ");

		TypedQuery<Tuple> query = session.createNativeQuery(sb.toString(), Tuple.class).setParameter("checksheetId",
				checkSheetId);

		List<Tuple> tupleList = query.getResultList();
		LOGGER.info("# tupleList size : {}", tupleList.size());
		if (!CollectionUtils.isEmpty(tupleList)) {
			return CommonUtils.parseTupleList(tupleList);
		}
		return list;
	}

	public Tuple getWorkstationDetails(SkillMatrixRequest request, Session session) {
		LOGGER.info("#In SkillMatrixUtils |  INSIDE in getWorkstationDetails ");
		Tuple obj = session.createNativeQuery(" Select sw.id as id,sw.machine_index as machineIndex,\r\n"
				+ "	sw.required_workforce as requiredWorkforce, \r\n"
				+ "	sw.workstation as workstation,sw.dept_id as deptId,sw.req_skill_level_id as reqSkillLevelId,\r\n"
				+ "	sw.is_active as isActive,sw.branch_id as branchId,sw.line_id as lineId from sm_workstations sw where id=:id ",
				Tuple.class).setParameter("id", request.getId()).getResultList().stream().findFirst().orElse(null);

		return obj;
	}

	public Tuple getUserTypeDetails(SkillMatrixRequest request, Session session) {
		LOGGER.info("#In SkillMatrixUtils |  INSIDE in getUserTypeDetails ");

		Tuple obj = session.createNativeQuery(
				"  Select w.id as id,w.branch_id as branchId,w.emp_id as empId,w.user_type_id as userTypeId,w.is_active as isActive,\r\n"
						+ " dept_id as deptId,w .line_id as lineId from sm_user_type w where w.id=:id",
				Tuple.class).setParameter("id", request.getId()).getResultList().stream().findFirst().orElse(null);
		return obj;
	}

	public Timestamp convertStringToTimestamp(String strDate) {
		LOGGER.info("#In SkillMatrixUtils |  INSIDE in convertStringToTimestamp ");
		try {
			DateFormat formatter;
			formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
			Date date = formatter.parse(strDate);
			return new Timestamp(date.getTime());
		} catch (ParseException e) {
			LOGGER.info("# Excepyion : {}", e.getMessage());
			return null;
		}
	}

	public int getOEId(SkillMatrixRequest request, SMOJTSkillingAudit smAudi) {
		LOGGER.info("#In SkillMatrixUtils |  INSIDE in getOEId ");
		int empId = 0;
		if (smAudi.getSkilling() != null && smAudi.getSkilling().getOjtRegis() != null
				&& smAudi.getSkilling().getOjtRegis().getEmpDetails() != null) {
			empId = smAudi.getSkilling().getOjtRegis().getEmpDetails().getEmpId();
			request.setEmpId(empId);
		}
		return empId;
	}

	public void getOELevelId(Session session, SkillMatrixRequest request) {
		LOGGER.info("#In SkillMatrixUtils |  INSIDE in getOELevelId ");
		Tuple tupleList = session.createNativeQuery(
				"select ojt.desired_skill_level_id as desiredLvlId ,ojt.current_skill_level_id as currentLvlId "
						+ "from sm_ojt_skilling s inner join sm_ojt_regis ojt on s.ojt_regis_id=ojt.id where s.ojt_regis_id =:ojtRegiId",
				Tuple.class).setParameter("ojtRegiId", request.getOjtRegiId()).getResultList().stream().findFirst()
				.orElse(null);

		if (tupleList != null) {
			request.setCurrentSkillLevelId(CommonUtils.objectToLong(tupleList.get("currentLvlId")));
			request.setDesiredSkillLevelId(CommonUtils.objectToLong(tupleList.get("desiredLvlId")));
		}
	}

	public boolean getSkillingLvlId(Session session, long ojtRegiId) {
		LOGGER.info("#In SkillMatrixUtils |  INSIDE in getSkillingLvlId ");
		return ((Number) session
				.createNativeQuery("select ifnull(count(s.id),0) as count from sm_ojt_skilling s inner join sm_ojt_regis ojt on ojt.id = s.ojt_regis_id where s.ojt_regis_id =:ojtRegiId limit 1 ")
				.setParameter("ojtRegiId", ojtRegiId).getSingleResult()).intValue()> 0;
	}
	
	public boolean checkIsOjtPlanIsAlreadyPublished(Session session, SkillMatrixRequest request) {
		LOGGER.info("# checkIsOjtPlanIsAlreadyPublished ");
		boolean flag = ((Number) session.createNativeQuery(
				" select ifnull(COUNT(*),0) as count from sm_ojt_regis ojt inner join sm_ojt_plan plan on plan.id=ojt.ojt_plan_id\r\n"
						+ "where scheduler_status='PUBLISHED' and timestampdiff(SECOND,plan.start_date,current_timestamp())>0 and ojt.id=:ojtRegiId")
				.setParameter("ojtRegiId", request.getOjtRegiId()).getSingleResult()).intValue() > 0;
		return flag;
	}

//	public long getAssessmentId(Session session, int branchId, long desiredSLId) {
//		LOGGER.info("#In SkillMatrixUtils |  INSIDE in getAssessmentByDesiredLevel ");
//		return ((Number) session.createNativeQuery(
//				"select sa.id as assessmentId from sm_assessment sa  where sa.is_active=1 and sa.branch_id=:branchId and sa.skill_level_id=:desiredSLId and sa.status='COMPLETED' limit 1")
//				.setParameter("branchId", branchId).setParameter("desiredSLId", desiredSLId).getSingleResult())
//				.intValue();
//	}

	public SMAssessmentDTO getAssessmentId(Session session, SkillMatrixRequest request) {
		LOGGER.info("#In SkillMatrixUtils |  INSIDE in getAssessmentByDesiredLevel ");
//		return ((Number) session.createNativeQuery(
//				"select sa.id as assessmentId,sa.total_marks as totalMark from sm_assessment sa  where sa.is_active=1 and sa.branch_id=:branchId and sa.skill_level_id=:desiredSLId and sa.status='COMPLETED' limit 1")
//				.setParameter("branchId", branchId).setParameter("desiredSLId", desiredSLId).getSingleResult())
//				.intValue();

		SMAssessmentDTO assessment = new SMAssessmentDTO();
		Tuple tupleList = session.createNativeQuery(
				"select sa.id as assessmentId,sa.total_marks as totalMark from sm_assessment sa  where sa.is_active=1 and "
						+ " sa.branch_id=:branchId and sa.skill_level_id=:desiredSLId and sa.status=:published "
						+ " and sa.dept_id=:deptId and sa.line_id=:lineId  and sa.workstation_id=:workstationId "
						+ " and sa.assessment_type=:type limit 1 ",
				Tuple.class).setParameter("branchId", request.getBranchId())
				.setParameter("desiredSLId", request.getDesiredSkillLevelId())
				.setParameter("published", SMConstant.ASSESSMENT_PUBLISHED).setParameter("deptId", request.getDeptId())
				.setParameter("lineId", request.getLineId()).setParameter("workstationId", request.getWorkstationId())
				.setParameter("type", SMConstant.LEVEL_ASSESSMENT).getResultList().stream().findFirst().orElse(null);

		if (tupleList != null) {
			LOGGER.info("AssessmentId: {}", CommonUtils.objectToLong(tupleList.get("assessmentId")));
			assessment.setAssessmentId(CommonUtils.objectToLong(tupleList.get("assessmentId")));
			assessment.setTotalMark(CommonUtils.objectToDouble(tupleList.get("totalMark")));
		}
		return assessment;
	}

	public void updateSkilling(SkillMatrixRequest request, Session session) {
		LOGGER.info("#In SkillMatrixUtils |  INSIDE in updateSkilling ");
		session.createNativeQuery(
				"update sm_ojt_skilling set status=:status,updated_by=:updatedBy where id=:skillingId")
				.setParameter("status", EnovationConstants.REJECTED_STRING)
				.setParameter("updatedBy", request.getUpdatedBy()).setParameter("skillingId", request.getSkillingId())
				.executeUpdate();
	}

	public int getChecksheetDayOfNo(SkillMatrixRequest request, Session session) {
		LOGGER.info("#In SkillMatrixUtils |  INSIDE in getChecksheetDayOfNo ");
		return ((Number) session.createNativeQuery("select ifnull(no_of_days,0) from sm_checksheet where id =:id")
				.setParameter("id", request.getCheckSheetId()).getSingleResult()).intValue();
	}

	public SMAssessmentDTO assessmentDetails(Session session, long assessmentId, SMAssessmentDTO data) {

		Tuple tuple = session.createNativeQuery(
				"Select s.id as assessmentId,s.actual_marks as actualMarks,s.assessment_status as assessmentStatus,s.created_by as createdBy,s.created_date as createdDate,s.percentage as percentage,\r\n"
						+ "s.total_marks as totalMarks,s.updated_by as updatedBy,s.updated_date as updatedDate,s.skilling_id as skillingId,s.assessment_id as assId,\r\n"
						+ "s.ojt_regis_id as ojtRegisId,s.skilling_audit_id as skillingAuditId,concat(e.first_name,' ',e.last_name) as empName,\r\n"
						+ "r.emp_id as empId,r.workstation_id as workstationId,sl.level_name as levelName,sa.branch_id as branchId,r.dept_id as deptId,\r\n"
						+ "osa.status as status,osa.stage_id as stageId,l.id as lineId , l.name as lineName,r.created_by as tlEmpId,sa.assessment_type as assessmentType,s.total_assessed_time as totAssessedTime \r\n"
						+ " from sm_ojt_assessment s\r\n" + "left join sm_ojt_regis r on r.id=s.ojt_regis_id\r\n"
						+ " left join tbl_employee_details e on e.emp_id=r.emp_id\r\n"
						+ " left join sm_skill_level sl on sl.id=r.desired_skill_level_id "
						+ " left join sm_assessment sa on sa.id=s.assessment_id\r\n"
						+ " left join sm_ojt_skilling_audit osa on osa.id=s.skilling_audit_id "
						+ " left join dwm_line l on r.line_id \r\n" + "where s.id=:assessmentId",
				Tuple.class).setParameter("assessmentId", assessmentId).getResultList().stream().findFirst()
				.orElse(null);
		SMAssessmentDTO obj = new SMAssessmentDTO();

		if (tuple != null) {

			obj.setoJTAssessmentId(CommonUtils.objectToLong(tuple.get("assessmentId")));
			obj.setActualMarks(CommonUtils.objectToDouble(tuple.get("actualMarks")));
			obj.setCreatedBy(CommonUtils.objectToInt(tuple.get("createdBy")));
			obj.setCreatedDate(CommonUtils.objectToString(tuple.get("createdDate")));
			obj.setPercentage(CommonUtils.objectToDouble(tuple.get("percentage")));
			obj.setTotalMark(CommonUtils.objectToDouble(tuple.get("totalMarks")));
			obj.setUpdatedBy(CommonUtils.objectToInt(tuple.get("updatedBy")));
			obj.setUpdatedDate(CommonUtils.objectToString(tuple.get("updatedDate")));
			obj.setSkillingId(CommonUtils.objectToLong(tuple.get("skillingId")));
			obj.setAssessmentId(CommonUtils.objectToLong(tuple.get("assId")));// ***********************
			obj.setOjtRegisId(CommonUtils.objectToLong(tuple.get("ojtRegisId")));
			obj.setSkillingAuditId(CommonUtils.objectToLong(tuple.get("skillingAuditId")));
			obj.setEmpId(CommonUtils.objectToInt(tuple.get("empId")));
			obj.setWorkstationId(CommonUtils.objectToLong(tuple.get("workstationId")));
			obj.setLevelName(CommonUtils.objectToString(tuple.get("levelName")));
			obj.setBranchId(CommonUtils.objectToInt(tuple.get("branchId")));
			obj.setDeptId(CommonUtils.objectToInt(tuple.get("deptId")));
			obj.setStageId(CommonUtils.objectToLong(tuple.get("stageId")));
			obj.setStatus(CommonUtils.objectToString(tuple.get("status")));
			obj.setAssessmentStatus(CommonUtils.objectToString(tuple.get("status")));
			obj.setLineId(CommonUtils.objectToLong(tuple.get("lineId")));
			obj.setLineName(CommonUtils.objectToString(tuple.get("lineName")));
			obj.setTlEmpId(CommonUtils.objectToInt(tuple.get("tlEmpId")));
			obj.setAssessmentType(CommonUtils.objectToString(tuple.get("assessmentType")));
			obj.setTotalAssessedTime(CommonUtils.objectToInt(tuple.get("totAssessedTime")));

		}

		return obj;

	}

	public List<Tuple> getSkillingList(Session session, long oJTRegisId) {
		LOGGER.info("# SkillMatrixUtils | getSkillingList ");
		StringBuilder sb = new StringBuilder(
				" select sos.id as skillingId, sos.created_by as createdBy,concat(ifnull(e.first_name,''),' ',ifnull(e.last_name,'')) as createdByName ,\r\n"
						+ "sos.created_date as createdDate,sos.status as status,sos.checksheet_id as checksheetId,sos.ojt_regis_id as ojtRegisId\r\n"
						+ ",sos.assessment_due_date as assessmentDate,sos.assessment_id as assessmentId\r\n"
						+ " from sm_ojt_skilling sos left join tbl_employee_details e on e.emp_id=sos.created_by  where sos.ojt_regis_id=:oJTRegisId ");
		TypedQuery<Tuple> query = session.createNativeQuery(sb.toString(), Tuple.class).setParameter("oJTRegisId",
				oJTRegisId);
		return query.getResultList();
	}

	public SMChecksheet getChecksheetDetails(Session session, SMOJTRegis request) {
		LOGGER.info("#In SkillMatrixUtils |  INSIDE in getChecksheetDetails ");
		SMChecksheet checksheet = null;
		LOGGER.info("branch: " + request.getBranch().getBranchId() + "deLevel: "
				+ request.getDesiredSkillLevel().getId() + "dept: " + request.getDept().getDeptId() + "line: "
				+ request.getLine().getId() + "Work: " + request.getWorkstation().getId());
		TypedQuery<Tuple> query = session.createNativeQuery(
				"select smc.id as checksheetId,smc.title as title,smc.no_of_days as noOfDays from  sm_checksheet smc\r\n"
						+ " where smc.skill_level_id=:levelId and smc.branch_id=:branchId and smc.is_active=1 "
						+ " and smc.dept_id=:deptId and smc.line_id=:lineId and smc.workstation_id=:workstationId ",
				Tuple.class).setParameter("levelId", request.getDesiredSkillLevel().getId())
				.setParameter("branchId", request.getBranch().getBranchId())
				.setParameter("deptId", request.getDept().getDeptId()).setParameter("lineId", request.getLine().getId())
				.setParameter("workstationId", request.getWorkstation().getId());

		List<Tuple> objList = query.getResultList();

		if (!CollectionUtils.isEmpty(objList)) {
			checksheet = new SMChecksheet();
			Tuple obj = objList.get(0);
			checksheet.setId(CommonUtils.objectToLong(obj.get("checksheetId")));
			checksheet.setTitle(CommonUtils.objectToString(obj.get("title")));
			checksheet.setNoOfDays(CommonUtils.objectToInt(obj.get("noOfDays")));
		} else {
			throw new EnovationException(SMConstant.CHECKSHEET_NOT_FOUND);
		}

		return checksheet;
	}

	public List<SMChecksheetPoints> getChecksheetPointList(Session session, long checksheetId) {
		LOGGER.info("#In SkillMatrixUtils |  INSIDE in getChecksheetDetails ");
		List<SMChecksheetPoints> pointList = null;
		LOGGER.info("checksheetId: " + checksheetId);
		TypedQuery<Tuple> query = session
				.createNativeQuery(
						" select smcp.id as checksheetPointId,smcp.item_name as pointName,smcp.day_no as dayNo\r\n"
								+ " from  sm_checksheet_points smcp where smcp.checksheet_id=:checksheetId ",
						Tuple.class)
				.setParameter("checksheetId", checksheetId);

		List<Tuple> objList = query.getResultList();

		if (!CollectionUtils.isEmpty(objList)) {
			pointList = new ArrayList<>();
			for (Tuple obj : objList) {
				SMChecksheetPoints point = new SMChecksheetPoints();
				point.setId(CommonUtils.objectToLong(obj.get("checksheetPointId")));
				point.setItemName(CommonUtils.objectToString(obj.get("pointName")));
				point.setDayNo(CommonUtils.objectToInt(obj.get("dayNo")));
				pointList.add(point);
			}
		} else {
			throw new EnovationException(SMConstant.CHECKSHEET_POINT_NOT_FOUND);
		}

		return pointList;
	}

	public List<SMChecksheetParameter> getChecksheetParameterList(Session session, long checksheetId) {
		LOGGER.info("#In SkillMatrixUtils |  INSIDE in getChecksheetParameterList ");
		List<SMChecksheetParameter> paramList = null;
		TypedQuery<Tuple> query = session.createNativeQuery(
				" select smpm.id as checkParamId,smpm.parameter_type_id as paramTypeId,smpm.parameter as param, "
						+ "smpm.cycle_value as cycleValue from  sm_checksheet_parameter smpm  where smpm.is_active=1 and smpm.checksheet_id=:checksheetId ",
				Tuple.class).setParameter("checksheetId", checksheetId);

		List<Tuple> objList = query.getResultList();

		if (!CollectionUtils.isEmpty(objList)) {
			paramList = new ArrayList<>();
			for (Tuple obj : objList) {
				SMChecksheetParameter param = new SMChecksheetParameter();
				param.setId(CommonUtils.objectToLong(obj.get("checkParamId")));
				param.setParameterType(new SMParameterType(CommonUtils.objectToLong(obj.get("paramTypeId"))));
				param.setParameter(CommonUtils.objectToString(obj.get("param")));
				param.setCycleValue(CommonUtils.objectToInt(obj.get("cycleValue")));
				paramList.add(param);
			}
		}

		return paramList;
	}

	public List<SMChecksheetPoints> getChecksheetPointListDayWise(List<SMChecksheetPoints> pointList, int dayNo) {

		List<SMChecksheetPoints> fPointList = pointList.stream().filter(x -> x.getDayNo() == dayNo)
				.collect(Collectors.toList());
		if (fPointList == null) {
			throw new EnovationException(dayNo + " " + SMConstant.CHECKSHEET_POINT_NOT_FOUND);
		}
		return fPointList;

	}

	public List<SMOJTChecksheetDTO> getPendingSkillingChecksheet(Session session, long skillingId) {
		LOGGER.info(" In SkillMatrixUtils | getPendingSkillingChecksheet ");
		List<SMOJTChecksheetDTO> smChecksheetList = new ArrayList<>();
		TypedQuery<Tuple> query = session.createNativeQuery(
				"select skc.id as skillingChecksheetId,skc.day_no as dayNo from  sm_ojt_skilling_checksheet skc\r\n"
						+ " where skc.skilling_id=:skillingId and status=:status order by  skc.day_no asc ",
				Tuple.class).setParameter("skillingId", skillingId)
				.setParameter("status", EnovationConstants.PENDING_STRING);

		List<Tuple> objList = query.getResultList();

		if (!CollectionUtils.isEmpty(objList)) {
			for (Tuple obj : objList) {
				SMOJTChecksheetDTO checksheet = new SMOJTChecksheetDTO();
				checksheet.setId(CommonUtils.objectToLong(obj.get("skillingChecksheetId")));
				checksheet.setDayNo(CommonUtils.objectToInt(obj.get("dayNo")));
				smChecksheetList.add(checksheet);
			}
		}
		return smChecksheetList;
	}

	public List<SMOJTChecksheetPoints> getSkillingChecksheetPoint(Session session, long skillingChecksheetId) {
		LOGGER.info(" In SkillMatrixUtils | getSkillingChecksheetPoint{} ", skillingChecksheetId);
		List<SMOJTChecksheetPoints> skillingPointList = null;
		TypedQuery<Tuple> query = session.createNativeQuery(
				"select skc.id as skillingPointId,skc.skilling_checksheet_id as skillingChecksheetId,skc.checksheet_points_id as checksheetPointId "
						+ " from  sm_ojt_checksheet_points skc\r\n"
						+ " where skc.skilling_checksheet_id=:skillingChecksheetId ",
				Tuple.class).setParameter("skillingChecksheetId", skillingChecksheetId);

		List<Tuple> objList = query.getResultList();

		if (!CollectionUtils.isEmpty(objList)) {
			skillingPointList = new ArrayList<>();
			for (Tuple obj : objList) {
				SMOJTChecksheetPoints checksheetPoint = new SMOJTChecksheetPoints();
				checksheetPoint.setId(CommonUtils.objectToLong(obj.get("skillingPointId")));
				checksheetPoint.setChecksheetPoints(
						new SMChecksheetPoints(CommonUtils.objectToLong(obj.get("checksheetPointId"))));
				checksheetPoint.setSkillingChecksheet(
						new SMOJTSkillingChecksheet(CommonUtils.objectToLong(obj.get("skillingChecksheetId"))));
				skillingPointList.add(checksheetPoint);
			}
		}
		return skillingPointList;
	}

	public int getUserTypeEmpId(Session session, int branchId, int deptId, long userTypeId) {
		TypedQuery<Tuple> query = session.createNativeQuery("select sm_usr.emp_id as  empId ,sm_usr.id as Id"
				+ " from  sm_user_type sm_usr " + " inner join tbl_employee_details emp on sm_usr.emp_id=emp.emp_id "
				+ " where  sm_usr.branch_id=:branchId and sm_usr.dept_id=:deptId and sm_usr.user_type_id=:usrTypeId  and emp.is_deactive=0",
				Tuple.class).setParameter("branchId", branchId).setParameter("deptId", deptId)
				.setParameter("userTypeId", userTypeId);

		List<Tuple> objList = query.getResultList();

		if (!CollectionUtils.isEmpty(objList)) {
			Tuple obj = objList.get(0);
			return CommonUtils.objectToInt(obj.get("empId"));
		} else {
			throw new EnovationException(SMConstant.USER_NOT_FOUND);
		}

	}

	public int checkAllDaysTaskComplete(SkillMatrixRequest request, Session session) {
		LOGGER.info("# SkillMatrixUtils || checkAllDaysTaskComplete ");
		return ((Number) session
				.createNativeQuery(" select ifnull(count(*),0) from sm_ojt_skilling_audit sa  inner join \r\n"
						+ "  sm_ojt_checksheet_points_audit smp on smp.skilling_audit_id=sa.id and sa.stage_id=:stageId \r\n"
						+ " right join sm_checksheet_points sc  on sc.id=smp.checksheet_points_id\r\n"
						+ "where sc.checksheet_id=:checksheetId  and (sa.status is null or sa.status <> :status)")
				.setParameter("checksheetId", request.getCheckSheetId()).setParameter("stageId", request.getStageId())
				.setParameter("status", EnovationConstants.COMPLETED_STRING).getSingleResult()).intValue();
	}

	public List<SMWorkstations> getWorkstationList(Session session, int branchId, int deptId, long lineId) {

		StringBuilder sb = new StringBuilder(
			" select sw.id as Id,sw.workstation as workstation,sw.req_skill_level_id as levelId ,sl.level_name as levelName,"
		  + " sw.machine_index as machineIndex,sw.required_workforce as reqWorkforce, sw.machine_count as machineCount \r\n"
		  + " from sm_workstations sw left join sm_skill_level sl on sl.id=sw.req_skill_level_id"
		  + " where branch_id=:branchId  and is_active=1 ");

		if (deptId > 0) {
			sb.append(" and dept_id=:deptId ");
		}
		if (lineId > 0) {
			sb.append(" and line_id=:lineId ");
		}

		TypedQuery<Tuple> query = session.createNativeQuery(sb.toString(), Tuple.class).setParameter("branchId",
				branchId);

		if (lineId > 0) {
			query.setParameter("lineId", lineId);
		}
		if (deptId > 0) {
			query.setParameter("deptId", deptId);
		}

		List<Tuple> objList = query.getResultList();
		List<SMWorkstations> list = new ArrayList<>();

//		List<Tuple> objList = session.createNativeQuery(
//				" select sw.id as Id,sw.workstation as workstation,sw.req_skill_level_id as levelId ,sl.level_name as levelName,sw.machine_index as machineIndex,sw.required_workforce as reqWorkforce \r\n"
//						+ " from sm_workstations sw left join sm_skill_level sl on sl.id=sw.req_skill_level_id where branch_id=:branchId and dept_id=:deptId",
//				Tuple.class).setParameter("branchId", branchId).setParameter("deptId", deptId).getResultList();

		if (!CollectionUtils.isEmpty(objList)) {
			for (Tuple obj : objList) {
				SMWorkstations workstation = new SMWorkstations();
				workstation.setId(CommonUtils.objectToLong(obj.get("Id")));
				workstation.setWorkstation(CommonUtils.objectToString(obj.get("workstation")));
				workstation.setReqSkillLevel(new SMSkillLevel(CommonUtils.objectToLong(obj.get("levelId")),
						CommonUtils.objectToString(obj.get("levelName"))));
				workstation.setMachineIndex(CommonUtils.objectToDouble(obj.get("machineIndex")));
				workstation.setRequiredWorkforce(CommonUtils.objectToDouble(obj.get("reqWorkforce")));
				workstation.setMachineCount(CommonUtils.objectToInt(obj.get("machineCount")));
				
				list.add(workstation);
			}
		}
		return list;

	}

	public List<Tuple> getParameterList(Session session, long auditId) {
		LOGGER.info("# SkillMatrixUtils | getSkillingAuditList");
		StringBuilder sb = new StringBuilder(
				" select sp.id as skillingParameterId,sp.created_by as createBy,sp.created_date as createdDate,sp.skilling_audit_id as skillingAuditId,\r\n"
						+ "sp.checksheet_parameter_id as checksheetParameterId,sp.parameter_value as parameterValue,concat(ifnull(e.first_name,''),' ',ifnull(e.last_name,'')) as createdByName\r\n"
						+ " from sm_ojt_skilling_parameter sp inner join sm_ojt_skilling_audit osa on sp.skilling_audit_id = osa.id\r\n"
						+ " left join tbl_employee_details e on e.emp_id=osa.created_by where sp.skilling_audit_id=:id ");
		TypedQuery<Tuple> query = session.createNativeQuery(sb.toString(), Tuple.class).setParameter("id", auditId);

		return query.getResultList();
	}

	public List<Tuple> getcheckSheetPointAuditList(Session session, long auditId) {
		LOGGER.info("# SkillMatrixUtils | getSkillingAuditList ");
		StringBuilder sb = new StringBuilder(
				" select ocpa.id as pointAuditId ,ocpa.created_by as createBy,ocpa.created_date as createdDate,ocpa.status as status,ocpa.comment as comment,\r\n"
						+ "ocpa.checksheet_points_id as checksheetPointsId ,ocpa.skilling_audit_id as skillingAuditId,ocpa.ojt_point_id as skillingPointsId,\r\n"
						+ "concat(ifnull(e.first_name,''),' ',ifnull(e.last_name,'')) as createdByName \r\n"
						+ " from  sm_ojt_checksheet_points_audit ocpa left join sm_ojt_skilling_audit osa on ocpa.skilling_audit_id=osa.id\r\n"
						+ " left join tbl_employee_details e on e.emp_id=osa.created_by where ocpa.skilling_audit_id=:id ");
		TypedQuery<Tuple> query = session.createNativeQuery(sb.toString(), Tuple.class).setParameter("id", auditId);

		return query.getResultList();
	}

	public List<Tuple> getSkillingAuditList(Session session, long skillingId) {
		LOGGER.info("# SkillMatrixUtils | getSkillingAudit ");
		StringBuilder sb = new StringBuilder(
				" select osa.id as auditId ,osa.created_by as createBy,osa.created_date as createdDate,osa.status as status,osa.emp_id as assignedTo,\r\n"
						+ "osa.skilling_id as skillingId,concat(ifnull(e.first_name,''),' ',ifnull(e.last_name,'')) as assignedToName ,osa.comment as comment,\r\n"
						+ "osa.skilling_checksheet_id as skillingChecksheetId,osa.stage_id as stageId \r\n"
						+ "from sm_ojt_skilling_audit osa left join  sm_ojt_skilling sos on sos.id= osa.skilling_id \r\n"
						+ "inner join tbl_employee_details e on e.emp_id=osa.emp_id where osa.skilling_id=:id ");
		TypedQuery<Tuple> query = session.createNativeQuery(sb.toString(), Tuple.class).setParameter("id", skillingId);
		return query.getResultList();
	}

	public List<Tuple> getcheckSheetPointList(Session session, long ojtSkillingSheetId) {
		LOGGER.info("# SkillMatrixUtils | getcheckSheetList ");
		StringBuilder sb = new StringBuilder(
				" select  cp.id as ojtCheckSheetPointId,cp.created_by as createBy ,cp.created_date as createdDate,cp.status as status,cp.checksheet_points_id as checksheetPointsId,\r\n"
						+ "cp.skilling_checksheet_id as skillingChecksheetId,concat(ifnull(e.first_name,''),' ',ifnull(e.last_name,'')) as createdByName  \r\n"
						+ "from sm_ojt_checksheet_points cp \r\n"
						+ "inner join sm_ojt_skilling_checksheet sc on cp.skilling_checksheet_id = sc.id\r\n"
						+ "left join tbl_employee_details e on e.emp_id=sc.created_by where cp.skilling_checksheet_id=:id ");
		TypedQuery<Tuple> query = session.createNativeQuery(sb.toString(), Tuple.class).setParameter("id",
				ojtSkillingSheetId);
		return query.getResultList();
	}

	public List<Tuple> getcheckSheetList(Session session, long skillingId) {
		LOGGER.info("# SkillMatrixUtils | getcheckSheetList ");
		StringBuilder sb = new StringBuilder(
				" select sc.id as ojtSkillingSheetId,sc.created_date as createdDate,sc.day_no as dayNO,sc.status as status,sc.comments as comments,\r\n"
						+ "sc.created_by as createdBy,concat(ifnull(e.first_name,''),' ',ifnull(e.last_name,'')) as createdByName \r\n"
						+ " from sm_ojt_skilling_checksheet sc left join sm_ojt_skilling sos on sos.id=sc.skilling_id \r\n"
						+ " left join tbl_employee_details e on e.emp_id=sc.created_by  where sc.skilling_id =:skillingId");
		TypedQuery<Tuple> query = session.createNativeQuery(sb.toString(), Tuple.class).setParameter("skillingId",
				skillingId);

		List<Tuple> tupleList = query.getResultList();
		LOGGER.info("# tupleList size : {}", tupleList.size());
		return tupleList;
	}

	public Tuple getOJTRegisDetails(long oJTRegisId, Session session) {
		LOGGER.info("# SkillMatrixUtils | getOJTRegisDetails ");
		StringBuilder sb = new StringBuilder(
				"select sor.id as ojtRegiId,sor.created_by as createdBy,concat(ifnull(e.first_name,''),' ',ifnull(e.last_name,'')) as createdByName ,\r\n"
						+ " sor.created_date as createdDate,sor.status as status,sor.current_skill_level_id as currentSkillLvlId,sor.desired_skill_level_id as desiredSkillLvlId,\r\n"
						+ " sor.emp_id as empIdOfOE,sor.trainer_emp_id as trainerEmpId,concat(ifnull(ed.first_name,''),' ',ifnull(ed.last_name,'')) as trainerName,\r\n"
						+ " sor.workstation_id as workStationId,sw.workstation as workstation,\r\n"
						+ " sw.required_workforce as requiredWorkforce,sw.machine_index as machineIndex,sw.dept_id as deptId from sm_ojt_regis sor\r\n"
						+ " inner join tbl_employee_details e on e.emp_id=sor.created_by inner join sm_workstations sw on sw.id=sor.workstation_id\r\n"
						+ " inner join tbl_employee_details ed on ed.emp_id=sor.trainer_emp_id where sor.id=:oJTRegisId");
		TypedQuery<Tuple> query = session.createNativeQuery(sb.toString(), Tuple.class).setParameter("oJTRegisId",
				oJTRegisId);
		return query.getResultList().stream().findFirst().orElse(null);
	}

	/**
	 * @author Rajdeep MD 25-Aug-2023 , 03:00 AM
	 * @desc
	 */
	public void setOEEmpId(SkillMatrixRequest request, Session session) {
		int empId = ((Number) session.createNativeQuery("select ifnull(emp_id,0) from sm_ojt_regis where id=:ojtRegiId")
				.setParameter("ojtRegiId", request.getOjtRegiId()).getSingleResult()).intValue();
		request.setEmpId(empId);
	}

	/**
	 * @author Rajdeep MD 25-Aug-2023 , 03:00 AM
	 * @desc get pending Action for given PointID
	 */
	public int getSkillingSheetDayNo(SMOJTSkillingAudit smAudi) {
		LOGGER.info("# SkillMatrixUtils | getSkillingSheetDayNo ");
		if (smAudi.getSkillingChecksheet() != null && smAudi.getSkillingChecksheet().getDayNo() > 0) {
			return smAudi.getSkillingChecksheet().getDayNo();
		}
		return 0;
	}

	/**
	 * @author Rajdeep MD 25-Aug-2023 , 03:00 AM
	 * @desc get ChechSheetId by using skilling checksheet Audit Object
	 */
	public long getChechSheetId(SMOJTSkillingAudit smAudi) {
		LOGGER.info("# SkillMatrixUtils | getChechSheetId ");
		if (smAudi.getSkilling() != null && smAudi.getSkilling().getChecksheet() != null
				&& smAudi.getSkilling().getChecksheet().getId() > 0) {
			return smAudi.getSkilling().getChecksheet().getId();
		}
		return 0;
	}

	/**
	 * @author Rajdeep MD 25-Aug-2023 , 03:00 AM
	 * @desc get SkillingId by using skilling checksheet Audit Object
	 */
	public long getSkillingId(SMOJTSkillingAudit smAudi) {
		LOGGER.info("# SkillMatrixUtils | getSkillingId ");
		if (smAudi.getSkilling() != null && smAudi.getSkilling().getId() > 0) {
			return smAudi.getSkilling().getId();
		}
		return 0;
	}

	/**
	 * @author Rajdeep MD 25-Aug-2023 , 03:00 AM
	 * @desc
	 */
	public SMOJTSkillingChecksheet saveSkillingCheckSheet(Session session, SkillMatrixRequest request) {
		LOGGER.info("# SkillMatrixUtils | saveSkillingCheckSheet ");
		SMOJTSkillingChecksheet skillingSheet = new SMOJTSkillingChecksheet();
		skillingSheet.setSkilling(new SMOJTSkilling(request.getSkillingId()));
		skillingSheet.setDayNo(request.getDayNo());
		skillingSheet.setStatus(EnovationConstants.PENDING_STRING);
		session.save(skillingSheet);
		return skillingSheet;

	}

	public int getCSPendingDaysCount(Session session, SkillMatrixRequest request) {
		return ((Number) session
				.createNativeQuery("select ifnull(count(*),0) from sm_ojt_skilling_checksheet sc "
						+ "  where sc.status=:status and sc.skilling_id=:skillingId ")
				.setParameter("status", EnovationConstants.PENDING_STRING)
				.setParameter("skillingId", request.getSkillingId()).getSingleResult()).intValue();
	}

	public TypedQuery<Tuple> getAssessmentOptionList(long assessmentId, Session session) {
		LOGGER.info("# SkillMatrixUtils | getAssessmentDetails");
		StringBuilder sb = new StringBuilder(
				" select sa.id as id, sa.title as title, sa.skill_level_id as skillLevelId , sa.branch_id as branchId,\r\n"
						+ "	sa.time as time , sa.passing_marks as passingMarks,sa.total_marks as totalMark,\r\n"
						+ "	sa.created_by as createdBy,sa.created_date as createdDate ,b.name as branchName,sl.level_name as skillLevel ,saq.id as quetionId,\r\n"
						+ "	saq.qestion as qestion,saq.que_marks as questionMark ,sao.id as optionId,sao.opt as opt\r\n"
						+ "	,sao.is_right_ans as isRightAns,sa.status as status ,s.id selectedId,s.opt_id as selectedOptionId,\r\n"
						+ "	 c.id as categoryId ,c.category_name as categoryName,l.name as lineName,l.id as lineId, "
						+ "	 d.dept_id as deptId,d.dept_name as deptName,sw.id as workstationId , sw.workstation as workstation, \r\n"
						+ "	 sa.assessment_type as assessmentType from sm_ojt_assessment oa \r\n"
						+ "   left join sm_ojt_assessment_ques aq on aq.ojt_assessment_id=oa.id\r\n"
						+ "	  left join sm_assessment_ques saq on aq.ques_id=saq.id\r\n"
						+ "	  left join sm_assessment sa on sa.id=saq.assessment_id and saq.is_active=1\r\n"
						+ "   left join master_branch b on b.branch_id=sa.branch_id \r\n"
						+ "	  left join sm_skill_level sl on sl.id=sa.skill_level_id\r\n"
						+ "	  left join sm_assessment_options sao on saq.id = sao.ques_id\r\n"
						+ "	  left join sm_ojt_assessment_opt s on sao.id= s.opt_id \r\n"
						+ "	  left join sm_category c on c.id=saq.category_id "
						+ " left join master_department d on d.dept_id=sa.dept_id "
						+ "	left join dwm_line l on l.id=sa.line_id "
						+ "	left join sm_workstations sw on sw.id = sa.workstation_id " + " where oa.id=:id ");
		return session.createNativeQuery(sb.toString(), Tuple.class).setParameter("id", assessmentId);

	}

	public List<Tuple> getOJTAssQuestionId(Session session, SMAssessmetDTO queDto) {

		return session.createNativeQuery(
				" select id as ojtAssQueId,ques_id as questionId from sm_ojt_assessment_ques where ques_id=:questionId ",
				Tuple.class).setParameter("questionId", queDto.getQuestionId()).getResultList();
	}

	public List<Tuple> getChechSheetIdBySkillingId(SkillMatrixRequest request, Session session) {
		LOGGER.info("# SkillMatrixUtils | getChechSheetIdBySkillingId");
		TypedQuery<Tuple> query = session.createNativeQuery(
				"select id as skillingCheckSheetId,day_no as dayNo from sm_ojt_skilling_checksheet where skilling_id =:skillingId",
				Tuple.class).setParameter("skillingId", request.getSkillingId());
		List<Tuple> tupleList = query.getResultList();
		LOGGER.info("# tupleList size : {}", tupleList.size());
		return tupleList;
	}

	public Tuple getWorkstationDetails(Session session, SkillMatrixRequest request) {
		Tuple tupleObj = session
				.createNativeQuery(" Select sw.id as id,sw.machine_index as machineIndex,\r\n"
						+ "sw.required_workforce as requiredWorkforce, \r\n"
						+ "sw.workstation as workstation,sw.dept_id as deptId,sw.req_skill_level_id as reqSkillLevelId,"
						+ "sw.is_active as isActive,sw.branch_id as branchId,sw.line_id as lineId,"
						+ "sw.machine_count as machineCount \r\n"
						+ "from sm_workstations sw where sw.id=:id ", Tuple.class)
				.setParameter("id", request.getId()).getResultList().stream().findFirst().orElse(null);
		return tupleObj;
	}

	public Tuple getUserTypeDetails(Session session, SkillMatrixRequest request) {
		Tuple tupleObj = session.createNativeQuery(
				" Select w.id as id,w.branch_id as branchId,w.emp_id as empId,w.user_type_id as userTypeId,w.is_active as isActive,\r\n"
						+ " dept_id as deptId,w .line_id as lineId from sm_user_type w where w.id=:id",
				Tuple.class).setParameter("id", request.getId()).getResultList().stream().findFirst().orElse(null);
		return tupleObj;
	}

	public List<SMWorkstations> getWorkstationListLevelWise(SkillMatrixRequest request, Session session) {
		LOGGER.info("# SkillMatrixUtils | getWorkstationListLevelWise");
		List<SMWorkstations> workstationList = new ArrayList<>();

		StringBuilder sb = new StringBuilder(
				" select sw.id as Id,sw.workstation as workstation,sw.req_skill_level_id as reqSkillLevel , sl.level_name as level "
						+ " from sm_workstations sw \r\n"
						+ " inner join sm_skill_level sl on sl.id =sw.req_skill_level_id"
						+ " where sw.is_active=1 and sw.branch_id=:branchId and sw.dept_id=:deptId ");

		if (request.getLineId() > 0) {
			sb.append(" and sw.line_id =:lineId ");
		}

		TypedQuery<Tuple> query = session.createNativeQuery(sb.toString(), Tuple.class)
				.setParameter("branchId", request.getBranchId()).setParameter("deptId", request.getDeptId());

		if (request.getLineId() > 0) {
			query.setParameter("lineId", request.getLineId());
		}

		List<Tuple> objList = query.getResultList();

//		List<SMWorkstations> list = new ArrayList<>();
//		List<Tuple> objList = session.createNativeQuery(
//				" select sw.id as Id,sw.workstation as workstation,sw.req_skill_level_id as reqSkillLevel , sl.level_name as level  from sm_workstations sw\r\n"
//						+ " inner join sm_skill_level sl on sl.id =sw.req_skill_level_id where sw.is_active=1 and sw.branch_id=:branchId and sw.dept_id=:deptId",
//				Tuple.class).setParameter("branchId", request.getBranchId()).setParameter("deptId", request.getDeptId())
//				.getResultList();
//
		if (!CollectionUtils.isEmpty(objList)) {
			for (Tuple obj : objList) {
				SMWorkstations workstation = new SMWorkstations();
				workstation.setId(CommonUtils.objectToLong(obj.get("Id")));
				workstation.setWorkstation(CommonUtils.objectToString(obj.get("workstation")));
				workstation.setReqSkillLevel(new SMSkillLevel(CommonUtils.objectToLong(obj.get("reqSkillLevel")),
						CommonUtils.objectToString(obj.get("level"))));
				workstationList.add(workstation);
			}
		}
		return workstationList;
	}

	public HashMap<String, Object> getEmpList(SkillMatrixRequest request, Session session,
			HashMap<String, Object> response, List<HashMap<String, Object>> dataList,
			List<SMWorkstations> workstationList) {
		LOGGER.info("# SkillMatrixUtils | getEmpList");
		StringBuilder sb = new StringBuilder(
				"select e.emp_id as empId,e.cmpy_emp_id as cmpyEmpId, concat(ifnull(e.first_name,''),' ',ifnull(e.last_name,'')) as empName,\r\n"
						+ "sw.workstation as workstation,sl.level_name as currentLvl,sl.id as currentSkillLvlId\r\n"
						+ ",slr.level_name as desiredLvl,slr.id as desiredSkillLvlId,e.line_id as lineId,"
						+ " (case when r.status='PENDING' then 'Y' else 'N' end)  as oJTPending \r\n"
						+ " from tbl_employee_details e \r\n"
						+ " left join sm_ojt_regis r on r.emp_id = e.emp_id and r.status='PENDING'\r\n"
						+ " left join sm_skill_level sl on r.current_skill_level_id=sl.id\r\n"
						+ " left join sm_workstations sw on r.workstation_id= sw.id\r\n"
						+ " left join sm_skill_level slr on sw.req_skill_level_id=slr.id\r\n"
						+ " left join employee_hierarchy eh on eh.emp_lvl_id=e.emp_level_id "
						+ " where e.is_deactive=0 and e.branch_id=:branchId and e.dept_id=:deptId and eh.level_name='OE' "
						+ "	and e.emp_id NOT in (select su.emp_id from sm_user_type su  "
						+ " left join sm_master_user_type smu on su.user_type_id=smu.id "
						+ "	where user_type_caption='TL' and is_active=1) ");

		if (request.getLineId() > 0) {
			sb.append(" and e.line_id=:lineId ");
		}
		sb.append(" group by e.emp_id  ");

		if (request.getColName() != null && request.getOrderType() != null) {
			sb.append(" order by " + ColumnAscDescConstants.smOJTEmpListColName.get(request.getColName()) + " "
					+ request.getOrderType());
		} else {
			sb.append(" order by e.emp_id ASC ");
		}

		TypedQuery<Tuple> query = session.createNativeQuery(sb.toString(), Tuple.class)
				.setParameter("branchId", request.getBranchId()).setParameter("deptId", request.getDeptId());

		if (request.getLineId() > 0) {
			query.setParameter("lineId", request.getLineId());
		}
		if (request.getLimit() > 0) {
			query.setFirstResult(request.getOffset()).setMaxResults(request.getLimit());
		}
		List<Tuple> empList = query.getResultList();
		LOGGER.info("# empList Size : {}", empList.size());
		if (!CollectionUtils.isEmpty(empList)) {
			response = new HashMap<>();

			for (Tuple obj : empList) {
				HashMap<String, Object> map = new HashMap<>();

				map.put("empId", CommonUtils.objectToInt(obj.get("empId")));
				map.put("cmpyEmpId", CommonUtils.objectToString(obj.get("cmpyEmpId")));
				map.put("empName", CommonUtils.objectToString(obj.get("empName")));
				map.put("currentSkillLvlId", (CommonUtils.objectToLong(obj.get("currentSkillLvlId"))));
				map.put("currentLvl", (CommonUtils.objectToString(obj.get("currentLvl"))));
				map.put("desiredSkillLvlId", (CommonUtils.objectToLong(obj.get("desiredSkillLvlId"))));
				map.put("desiredLvl", (CommonUtils.objectToString(obj.get("desiredLvl"))));
				map.put("workstation", (CommonUtils.objectToString(obj.get("workstation"))));
				map.put("lineId", CommonUtils.objectToLong(obj.get("lineId")));
				map.put("oJTPending", (CommonUtils.objectToString(obj.get("oJTPending"))));

				List<Tuple> workList = empWorkStationList(session, CommonUtils.objectToInt(obj.get("empId")));
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

					map.put(workstation.getWorkstation(), dto);
				}

//				for (SMWorkstations workstation : workstationList) {
//					map.put(workstation.getWorkstation(), workstation.getReqSkillLevel().getLevelName());
//				}
//				List<Tuple> workList = empWorkStationList(session, CommonUtils.objectToInt(obj.get("empId")));
//				map.put("workstationList", CommonUtils.parseTupleList(workList));
				dataList.add(map);
			}
		}
		return response;
	}

	public List<Tuple> empWorkStationList(Session session, int empId) {
		StringBuilder work = new StringBuilder(
				"select sm.skill_level_id as empLevelId,sl.level_name as empLevel,sw.id as workstationId,sw.workstation as workstation "
						+ " from sm_emp_skill_matrix sm " + "	left join sm_skill_level sl on sl.id=sm.skill_level_id"
						+ " left join sm_workstations sw on sw.id=sm.workstations_id where sm.emp_id=:empId");
		TypedQuery<Tuple> wQuery = session.createNativeQuery(work.toString(), Tuple.class).setParameter("empId", empId);
		List<Tuple> workList = wQuery.getResultList();
		return workList;
	}

	public SMOJTSkillingAuditDTO getSMOJTAuditDetails(Session session, SkillMatrixRequest request) {
		LOGGER.info("# SkillMatrixUtils | getSMOJTAuditList");
		SMOJTSkillingAuditDTO skillingAudit = null;
		List<Tuple> objList = session.createNativeQuery(
				"SELECT audit.id as id,audit.stage_id as stageId,audit.status as status,audit.created_date as createdDate,audit.updated_date as updatedDate,\r\n"
						+ " audit.emp_id as empId,audit.comment as comment,ojtSkilling.checksheet_id as checksheetId,audit.skilling_id as skillingId,\r\n"
						+ " audit.skilling_checksheet_id as skillingChecksheetId,ojtCheckseet.day_no as dayNo,r.desired_skill_level_id as levelId, "
						+ " r.branch_id as branchId ,r.emp_id as oeEmpId,r.id as ojtRegiId,r.line_id as lineId ,r.dept_id as deptId,r.trainer_emp_id as trainerId,audit.previous_audit_id as previousAuditId ,"
						+ " l.name as lineName,d.dept_name as deptName,sw.id as workstationId , sw.workstation as workstation,r.created_by as tlEmpId "
						+ " FROM sm_ojt_skilling_audit audit \r\n"
						+ " inner join sm_ojt_skilling ojtSkilling on ojtSkilling.id=audit.skilling_id\r\n"
						+ " left join sm_ojt_skilling_checksheet ojtCheckseet on ojtCheckseet.id=audit.skilling_checksheet_id "
						+ " left join sm_ojt_regis r on r.id=ojtSkilling.ojt_regis_id "
						+ " inner join master_department d on d.dept_id=r.dept_id\r\n"
						+ " inner join dwm_line l on l.id=r.line_id \r\n"
						+ " inner join sm_workstations sw on sw.id = r.workstation_id "
						+ " where audit.id=:auditId and audit.skilling_id=:skillingId ",
				Tuple.class).setParameter("auditId", request.getSkillingAuditId())
				.setParameter("skillingId", request.getSkillingId()).getResultList();

		if (!CollectionUtils.isEmpty(objList)) {
			skillingAudit = new SMOJTSkillingAuditDTO();
			Tuple obj = objList.get(0);
			skillingAudit.setSkillingAuditId(CommonUtils.objectToLong(obj.get("id")));
			skillingAudit.setStageId(CommonUtils.objectToLong(obj.get("stageId")));
			skillingAudit.setStatus(CommonUtils.objectToString(obj.get("status")));
			skillingAudit.setAssignedDate(CommonUtils.objectToString(obj.get("createdDate")));
			skillingAudit.setCompletedDate(CommonUtils.objectToString(obj.get("updatedDate")));
			skillingAudit.setEmpId(CommonUtils.objectToLong(obj.get("empId")));
			skillingAudit.setComment(CommonUtils.objectToString(obj.get("comment")));
			skillingAudit.setSkillingId(CommonUtils.objectToLong(obj.get("skillingId")));
			skillingAudit.setChecksheetId(CommonUtils.objectToLong(obj.get("checksheetId")));
			skillingAudit.setSkillingChecksheetId(CommonUtils.objectToLong(obj.get("skillingChecksheetId")));
			skillingAudit.setDayNo(CommonUtils.objectToInt(obj.get("dayNo")));
			skillingAudit.setBranchId(CommonUtils.objectToInt(obj.get("branchId")));
			skillingAudit.setLevelId(CommonUtils.objectToLong(obj.get("levelId")));
			skillingAudit.setOeEmpId(CommonUtils.objectToInt(obj.get("oeEmpId")));
			skillingAudit.setOjtRegiId(CommonUtils.objectToLong(obj.get("ojtRegiId")));
			skillingAudit.setDeptId(CommonUtils.objectToInt(obj.get("deptId")));
			skillingAudit.setLineId(CommonUtils.objectToLong(obj.get("lineId")));
			skillingAudit.setTrainerEmpId(CommonUtils.objectToInt(obj.get("trainerId")));
			skillingAudit.setPreviousAuditId(CommonUtils.objectToLong(obj.get("previousAuditId")));
			skillingAudit.setDeptName(CommonUtils.objectToString(obj.get("deptName")));
			skillingAudit.setLineName(CommonUtils.objectToString(obj.get("lineName")));
			skillingAudit.setWorkstationId(CommonUtils.objectToLong(obj.get("workstationId")));
			skillingAudit.setWorkstation(CommonUtils.objectToString(obj.get("workstation")));
			skillingAudit.setTlEmpId(CommonUtils.objectToInt(obj.get("tlEmpId")));
		}
		return skillingAudit;
	}

	public List<SMOJTSkillingAuditDTO> getAuditList(Session session, SkillMatrixRequest request) {
		LOGGER.info("# SkillMatrixUtils | getAuditList");
		List<SMOJTSkillingAuditDTO> auditList = new ArrayList<>();

		List<Tuple> objList = session.createNativeQuery(
				"SELECT audit.id as id,audit.stage_id as stageId,audit.status as status,audit.created_date as createdDate,audit.updated_date as updatedDate,\r\n"
						+ " audit.emp_id as empId,audit.comment as comment,ojtSkilling.checksheet_id as checksheetId,audit.skilling_id as skillingId,\r\n"
						+ " audit.skilling_checksheet_id as skillingChecksheetId,ojtCheckseet.day_no as dayNo,r.desired_skill_level_id as levelId,"
						+ " r.branch_id as branchId,r.dept_id as deptId ,concat(ifnull(e.first_name,''),' ',ifnull(e.last_name,'')) as createdByName, "
						+ " (case when sl.stage_label is null then sg.stage_name else sl.stage_label end) stage,r.id as ojtRegiId,r.emp_id as oeEmpId,"
						+ "  l.name as lineName,l.id as lineId,d.dept_name as deptName,sw.id as workstationId , sw.workstation as workstation "
						+ " FROM sm_ojt_skilling_audit audit \r\n"
						+ " inner join sm_ojt_skilling ojtSkilling on ojtSkilling.id=audit.skilling_id\r\n"
						+ " inner join sm_ojt_skilling_checksheet ojtCheckseet on ojtCheckseet.id=audit.skilling_checksheet_id "
						+ " left join sm_ojt_regis r on r.id=ojtSkilling.ojt_regis_id "
						+ " left join tbl_employee_details e on e.emp_id=audit.emp_id"
						+ " left join sm_stage_label sl on sl.stage_id= audit.stage_id and sl.branch_id =r.branch_id "
						+ " left join sm_stage sg on sg.id= audit.stage_id "
						+ " left join master_department d on d.dept_id=r.dept_id\r\n"
						+ " left join dwm_line l on l.id=r.line_id\r\n"
						+ " left join sm_workstations sw on sw.id = r.workstation_id "
						+ " where  audit.skilling_id=:skillingId ",
				Tuple.class).setParameter("skillingId", request.getSkillingId()).getResultList();

		if (!CollectionUtils.isEmpty(objList)) {
			for (Tuple obj : objList) {
				SMOJTSkillingAuditDTO skillingAudit = new SMOJTSkillingAuditDTO();
				skillingAudit.setSkillingAuditId(CommonUtils.objectToLong(obj.get("id")));
				skillingAudit.setStageId(CommonUtils.objectToLong(obj.get("stageId")));
				skillingAudit.setStatus(CommonUtils.objectToString(obj.get("status")));
				skillingAudit.setAssignedDate(CommonUtils.objectToString(obj.get("createdDate")));
				skillingAudit.setCompletedDate(CommonUtils.objectToString(obj.get("updatedDate")));
				skillingAudit.setEmpId(CommonUtils.objectToLong(obj.get("empId")));
				skillingAudit.setComment(CommonUtils.objectToString(obj.get("comment")));
				skillingAudit.setSkillingId(CommonUtils.objectToLong(obj.get("skillingId")));
				skillingAudit.setChecksheetId(CommonUtils.objectToLong(obj.get("checksheetId")));
				skillingAudit.setSkillingChecksheetId(CommonUtils.objectToLong(obj.get("skillingChecksheetId")));
				skillingAudit.setDayNo(CommonUtils.objectToInt(obj.get("dayNo")));
				skillingAudit.setBranchId(CommonUtils.objectToInt(obj.get("branchId")));
				skillingAudit.setLevelId(CommonUtils.objectToLong(obj.get("levelId")));
				skillingAudit.setEmpName(CommonUtils.objectToString(obj.get("createdByName")));
				skillingAudit.setStage(CommonUtils.objectToString(obj.get("stage")));
				skillingAudit.setDeptId(CommonUtils.objectToInt(obj.get("deptId")));
				skillingAudit.setOeEmpId(CommonUtils.objectToInt(obj.get("oeEmpId")));
				skillingAudit.setOjtRegiId(CommonUtils.objectToLong(obj.get("ojtRegiId")));

				skillingAudit.setDeptName(CommonUtils.objectToString(obj.get("deptName")));
				skillingAudit.setLineId(CommonUtils.objectToLong(obj.get("lineId")));
				skillingAudit.setLineName(CommonUtils.objectToString(obj.get("lineName")));
				skillingAudit.setWorkstationId(CommonUtils.objectToLong(obj.get("workstationId")));
				skillingAudit.setWorkstation(CommonUtils.objectToString(obj.get("workstation")));

				auditList.add(skillingAudit);
			}

		}
		return auditList;
	}

	// Changed By Rajdeep 11-June-2024 Start Date Added Day wise
	public List<SMOJTCSPointsAuditDTO> getSMOJTAuditPointDetails(Session session, SkillMatrixRequest request) {
		LOGGER.info("# SkillMatrixUtils | getSMOJTAuditPointDetails " + request.getSkillingAuditId());
		List<SMOJTCSPointsAuditDTO> auditPointList = null;
		List<Tuple> objList = session.createNativeQuery(
				"select skillingChecksheet.day_no as dayNo,case when (skillingAudit.start_date is NOT null and skillingAudit.stage_id=1 ) then skillingAudit.start_date else skillingAudit.created_date end as createdDate,skillingAudit.updated_date as updatedDate,\r\n"
						+ " pointAudit.id as pointAuditId,cheksheetPoint.item_name as keyPoint,pointAudit.status as actionStatus,pointAudit.ojt_point_id as ojtPointId,\r\n"
						+ " pointAudit.checksheet_points_id as ojtChecksheetPointId,skillingAudit.id as skillingAuditId,cheksheetPoint.reference as reference,"
						+ " oeStatus.status as oestatus ,trainerStatus.status as trstatus,skillingAudit.comment,oeStatus.comment as oeComment,trainerActStatus.comment as trainerActComment, "
						+ " trainerStatus.comment as trainerComment" + " from sm_ojt_skilling_audit skillingAudit \r\n"
						+ " inner join sm_ojt_checksheet_points_audit pointAudit on pointAudit.skilling_audit_id=skillingAudit.id\r\n"
						+ " inner join sm_ojt_checksheet_points ojtChecksheetPoint on ojtChecksheetPoint.id=pointAudit.ojt_point_id \r\n"
						+ " inner join sm_ojt_skilling_checksheet skillingChecksheet on skillingChecksheet.id=ojtChecksheetPoint.skilling_checksheet_id\r\n"
						+ " left join sm_checksheet_points cheksheetPoint on ojtChecksheetPoint.checksheet_points_id=cheksheetPoint.id "
						+ " left join sm_ojt_skilling_audit oeStatus on oeStatus.skilling_id=skillingAudit.skilling_id and oeStatus.stage_id=1 and skillingChecksheet.id=oeStatus.skilling_checksheet_id\r\n"
						+ " left join sm_ojt_skilling_audit trainerStatus on trainerStatus.skilling_id=skillingAudit.skilling_id and trainerStatus.stage_id=3 and skillingAudit.previous_audit_id=trainerStatus.id \r\n"
						+ " left join sm_ojt_skilling_audit trainerActStatus on trainerActStatus.skilling_id=skillingAudit.skilling_id and trainerActStatus.stage_id=2 and skillingAudit.skilling_checksheet_id=trainerActStatus.skilling_checksheet_id "
						+ " where skillingAudit.id=:auditId and skillingAudit.skilling_id=:skillingId "
						+ " group by pointAudit.checksheet_points_id ",
				Tuple.class).setParameter("auditId", request.getSkillingAuditId())
				.setParameter("skillingId", request.getSkillingId()).getResultList();

		if (!CollectionUtils.isEmpty(objList)) {
			auditPointList = new ArrayList<>();
			for (Tuple obj : objList) {
				SMOJTCSPointsAuditDTO pointDtoObj = new SMOJTCSPointsAuditDTO();
				pointDtoObj.setPointAuditId(CommonUtils.objectToLong(obj.get("pointAuditId")));
				pointDtoObj.setAssignedDate(CommonUtils.objectToString(obj.get("createdDate")));
				pointDtoObj.setCompletedDate(CommonUtils.objectToString(obj.get("updatedDate")));
				pointDtoObj.setDayNo(CommonUtils.objectToInt(obj.get("dayNo")));
				pointDtoObj.setKeyPoint(CommonUtils.objectToString(obj.get("keyPoint")));
				pointDtoObj.setChecksheetPointId(CommonUtils.objectToLong(obj.get("ojtChecksheetPointId")));
				pointDtoObj.setStatus(CommonUtils.objectToString(obj.get("actionStatus")));
				pointDtoObj.setOjtPointId(CommonUtils.objectToLong(obj.get("ojtPointId")));
				pointDtoObj.setSkillingAuditId(CommonUtils.objectToLong(obj.get("skillingAuditId")));
				pointDtoObj.setReference(CommonUtils.objectToString(obj.get("reference")));
				pointDtoObj.setOeStatus(CommonUtils.objectToString(obj.get("oestatus")));
				pointDtoObj.setTrainerStatus(CommonUtils.objectToString(obj.get("trstatus")));
				pointDtoObj.setComment(CommonUtils.objectToString(obj.get("comment")));
				pointDtoObj.setOeComment(CommonUtils.objectToString(obj.get("oeComment")));
				pointDtoObj.setTrainerActComment(CommonUtils.objectToString(obj.get("trainerActComment")));
				pointDtoObj.setTrainerProdComment(CommonUtils.objectToString(obj.get("trainerComment")));
				auditPointList.add(pointDtoObj);
			}
		}
		return auditPointList;
	}

	// changed BY Rajdeep 2024-04-11 order by param Id
	public List<SMOJTSkillingParameterDTO> getSMOJTSkillingParameterList(Session session, SkillMatrixRequest request) {
		LOGGER.info("# SkillMatrixUtils | getSMOJTSkillingParameterList");

		List<SMOJTSkillingParameterDTO> paramList = null;
		List<Tuple> objList = session.createNativeQuery(
				"select skillingAudit.created_date as createdDate,skillingAudit.updated_date as updatedDate\r\n"
						+ " ,pointAudit.status as status,pointAudit.status as oEStatus ,skillingAudit.comment as comment,\r\n"
						+ " ojtParam.id as ojtParameterId,ojtParam.skilling_audit_id as skillingAuditId,ojtParam.checksheet_parameter_id as checksheetParameterId,\r\n"
						+ " checkParam.parameter as parameter,pType.type_name as paramType,ojtParam.parameter_value as parameterValue,checkParam.cycle_value as cycleValue "
						+ " from  sm_ojt_skilling_parameter ojtParam \r\n"
						+ " inner join sm_ojt_skilling_audit skillingAudit on skillingAudit.id=ojtParam.skilling_audit_id\r\n"
						+ " inner join sm_ojt_checksheet_points_audit pointAudit on pointAudit.skilling_audit_id=skillingAudit.id\r\n"
						+ " inner join sm_checksheet_parameter checkParam on checkParam.id=ojtParam.checksheet_parameter_id\r\n"
						+ " inner join sm_parameter_type pType on pType.id=checkParam.parameter_type_id"
						+ " where skillingAudit.id=:auditId and skillingAudit.skilling_id=:skillingId group by ojtParam.id order by ojtParam.id asc ",
				Tuple.class).setParameter("auditId", request.getSkillingAuditId())
				.setParameter("skillingId", request.getSkillingId()).getResultList();
		if (!CollectionUtils.isEmpty(objList)) {
			LOGGER.info("SMOJTSkillingParameterList Size: {}", objList.size());
			paramList = new ArrayList<>();
			for (Tuple obj : objList) {
				SMOJTSkillingParameterDTO paramDTO = new SMOJTSkillingParameterDTO();
				paramDTO.setId(CommonUtils.objectToLong(obj.get("ojtParameterId")));
				paramDTO.setChecksheetParameterId(CommonUtils.objectToLong(obj.get("checksheetParameterId")));
				paramDTO.setParameter(CommonUtils.objectToString(obj.get("parameter")));
				paramDTO.setSkillingAuditId(CommonUtils.objectToLong(obj.get("skillingAuditId")));
				paramDTO.setComment(CommonUtils.objectToString(obj.get("comment")));
				paramDTO.setOEStatus(CommonUtils.objectToString(obj.get("oEStatus")));
				paramDTO.setStatus(CommonUtils.objectToString(obj.get("status")));
				paramDTO.setParameterValue(CommonUtils.objectToString(obj.get("parameterValue")));
				paramDTO.setParameterType(CommonUtils.objectToString(obj.get("paramType")));
				paramDTO.setLabel(CommonUtils.objectToString(obj.get("parameter")));

				paramDTO.setCycleValue(CommonUtils.objectToInt(obj.get("cycleValue")));
				List<SMOJTPlanDTO> planList = getSMOJTCyclePlanList(session, paramDTO, request);
				paramDTO.setCyclePlanList(planList);

				paramList.add(paramDTO);
			}
		}
		return paramList;
	}

	public List<SMOJTPlanDTO> getSMOJTCyclePlanList(Session session, SMOJTSkillingParameterDTO paramDTO,
			SkillMatrixRequest request) {
		LOGGER.info("# SkillMatrixUtils | getSMOJTCyclePlanList");
		List<SMOJTPlanDTO> planList = null;
		List<Tuple> objList = session.createNativeQuery(
				"select cyclePlan.created_date as createdDate,cyclePlan.updated_date as updatedDate,cyclePlan.id as cyclePlanId,\r\n"
						+ " cyclePlan.actual_value as actualValue,cyclePlan.expected_value as expectedValue,cyclePlan.cycle_no as cycleNo\r\n"
						+ " from sm_ojt_cycle_plan_parameter cyclePlan\r\n"
						+ " inner join sm_ojt_skilling_parameter ojtParam on ojtParam.id=cyclePlan.skilling_parameter_id\r\n"
						+ " where cyclePlan.skilling_parameter_id=:skillingParamId ",
				Tuple.class).setParameter("skillingParamId", paramDTO.getId()).getResultList();

		if (!CollectionUtils.isEmpty(objList)) {
			planList = new ArrayList<>();
			for (Tuple obj : objList) {
				SMOJTPlanDTO planDTO = new SMOJTPlanDTO();
				planDTO.setCyclePlanId(CommonUtils.objectToInt(obj.get("cyclePlanId")));
				planDTO.setCycleNo(CommonUtils.objectToInt(obj.get("cycleNo")));
				planDTO.setActualValue(CommonUtils.objectToString(obj.get("actualValue")));
				planDTO.setExpectedValue(CommonUtils.objectToString(obj.get("expectedValue")));
				planDTO.setCreatedDate(CommonUtils.objectToString(obj.get("createdDate")));
				planDTO.setUpdatedDate(CommonUtils.objectToString(obj.get("updatedDate")));

				List<SMOJTSkillingAuditDTO> skillList = getSMOJTskillList(session, request);
				planDTO.setSkillingAuditList(skillList);

				planList.add(planDTO);
			}
		}
		return planList;
	}

	private List<SMOJTSkillingAuditDTO> getSMOJTskillList(Session session, SkillMatrixRequest request) {
		LOGGER.info("# SkillMatrixUtils | getSMOJTskillList");
		List<SMOJTSkillingAuditDTO> planList = null;
		List<Tuple> objList = session.createNativeQuery(
				" select osa.id as skillingAuditId ,osa.created_by as createBy,osa.created_date as createdDate,osa.status as status,osa.emp_id as assignedTo,\r\n"
						+ "	osa.skilling_id as skillingId,concat(ifnull(e.first_name,''),' ',ifnull(e.last_name,'')) as assignedToName ,osa.comment as comment,\r\n"
						+ "	osa.skilling_checksheet_id as skillingChecksheetId,osa.stage_id as stageId \r\n"
						+ "	from sm_ojt_skilling_audit osa left join  sm_ojt_skilling sos on sos.id= osa.skilling_id \r\n"
						+ "	inner join tbl_employee_details e on e.emp_id=osa.emp_id where osa.skilling_id=:skillingId",
				Tuple.class).setParameter("skillingId", request.getSkillingId()).getResultList();

		if (!CollectionUtils.isEmpty(objList)) {
			planList = new ArrayList<>();
			for (Tuple obj : objList) {
				SMOJTSkillingAuditDTO skillDTO = new SMOJTSkillingAuditDTO();
				skillDTO.setSkillingAuditId(CommonUtils.objectToLong(obj.get("skillingAuditId")));
				skillDTO.setCreateBy(CommonUtils.objectToInt(obj.get("createBy")));
				skillDTO.setStatus(CommonUtils.objectToString(obj.get("status")));
				skillDTO.setAssignedTo(CommonUtils.objectToString(obj.get("assignedTo")));
				skillDTO.setSkillingId(CommonUtils.objectToLong(obj.get("skillingId")));
				skillDTO.setAssignedToName(CommonUtils.objectToString(obj.get("assignedToName")));
				skillDTO.setComment(CommonUtils.objectToString(obj.get("comment")));
				skillDTO.setSkillingChecksheetId(CommonUtils.objectToLong(obj.get("skillingChecksheetId")));
				skillDTO.setStageId(CommonUtils.objectToLong(obj.get("stageId")));

				List<SMOJTAssessmentDTO> assessmentList = getSMOJTAssessmentList(session, request);
				skillDTO.setAssessmentList(assessmentList);

				planList.add(skillDTO);
			}
		}
		return planList;
	}

	private List<SMOJTAssessmentDTO> getSMOJTAssessmentList(Session session, SkillMatrixRequest request) {
		List<SMOJTAssessmentDTO> assessmentList = null;
		List<Tuple> objList = session.createNativeQuery(
				"  Select  a.id as ojtAssid,a.actual_marks as actualMarks,a.assessment_status as assessmentStatus,a.percentage as percentage,a.total_marks as totalMarks,\r\n"
						+ "	a.skilling_id as skillingId,a.ojt_regis_id as oJTRegiId,a.assessment_id as assessmentId,a.skilling_audit_id as skillingAuditId,\r\n"
						+ " concat(ifnull(e.first_name,''),' ',ifnull(e.last_name,'')) as assignedToName\r\n"
						+ "	 from sm_ojt_assessment a\r\n" + "  left join sm_ojt_regis sr on sr.id= a.ojt_regis_id\r\n"
						+ "  left join tbl_employee_details e on e.emp_id =sr.emp_id\r\n"
						+ "  where skilling_id=:skillingId",
				Tuple.class).setParameter("skillingId", request.getSkillingId()).getResultList();

		if (!CollectionUtils.isEmpty(objList)) {
			LOGGER.info("SMOJTSkillingParameterList Size: {}", objList.size());
			assessmentList = new ArrayList<>();

			for (Tuple obj : objList) {
				SMOJTAssessmentDTO assDTO = new SMOJTAssessmentDTO();
				assDTO.setOjtAssId(CommonUtils.objectToLong(obj.get("ojtAssid")));
				assDTO.setActualMarks(CommonUtils.objectToDouble(obj.get("actualMarks")));
				assDTO.setAssessmentStatus(CommonUtils.objectToString(obj.get("assessmentStatus")));
				assDTO.setPercentage(CommonUtils.objectToDouble(obj.get("percentage")));
				assDTO.setTotalMarks(CommonUtils.objectToDouble(obj.get("totalMarks")));
				assDTO.setSkillingId(CommonUtils.objectToLong(obj.get("skillingId")));
				assDTO.setOjtRegisId(CommonUtils.objectToLong(obj.get("oJTRegiId")));
				assDTO.setAssessmentId(CommonUtils.objectToLong(obj.get("assessmentId")));
				assDTO.setSkillingAuditId(CommonUtils.objectToLong(obj.get("skillingAuditId")));
				assDTO.setAssignedToName(CommonUtils.objectToString(obj.get("assignedToName")));

				assessmentList.add(assDTO);
			}
		}

		return assessmentList;

	}

	public SMOJTAssessment getOJTAssessmentId(Session session, SkillMatrixRequest request) {
		LOGGER.info("#In SkillMatrixUtils |  INSIDE in getOJTAssessmentId ");
		SMOJTAssessment ojtAssObj = null;
		TypedQuery<Tuple> query = session
				.createNativeQuery("select assess.id as ojtAssessId from  sm_ojt_assessment assess\r\n"
						+ " where assess.ojt_regis_id=:ojtRegiId and assess.assessment_id=:assessId  and assess.assessment_status=:status ",
						Tuple.class)
				.setParameter("ojtRegiId", request.getOjtRegiId()).setParameter("assessId", request.getAssessmentId())
				.setParameter("status", SMConstant.ASSESSMENT_PENDING);

		List<Tuple> objList = query.getResultList();
		if (!CollectionUtils.isEmpty(objList)) {
			Tuple obj = objList.get(0);
			ojtAssObj = new SMOJTAssessment();
			ojtAssObj.setId(CommonUtils.objectToLong(obj.get("ojtAssessId")));
		} else {
			throw new EnovationException(SMConstant.ASSESSMENT_NOT_FOUND);
		}
		return ojtAssObj;
	}

	public SMOJTQuetionDTO getOJTAssessmentQuesId(Session session, SMAssessmetDTO queDto, SkillMatrixRequest request) {
		LOGGER.info("#In SkillMatrixUtils |  INSIDE in getOJTAssessmentQuesId ");
		SMOJTQuetionDTO ojtAssObj = null;
		TypedQuery<Tuple> query = session
				.createNativeQuery(
						"select soaq.id as ojtAssQueId,soaq.ques_id as questionId,qutp.type_caption as queType,\r\n"
								+ " assQue.que_marks as score from sm_ojt_assessment_ques soaq \r\n"
								+ " inner join sm_assessment_ques assQue on assQue.id = soaq.ques_id \r\n"
								+ " inner join sm_question_type qutp on qutp.id = assQue.ques_type_id"
								+ " where soaq.ques_id=:questionId AND soaq.ojt_assessment_id =:ojtAssId ",
						Tuple.class)
				.setParameter("questionId", queDto.getQuestionId()).setParameter("ojtAssId", request.getOjtAssId());

		List<Tuple> objList = query.getResultList();
		if (!CollectionUtils.isEmpty(objList)) {
			Tuple obj = objList.get(0);
			ojtAssObj = new SMOJTQuetionDTO();
			ojtAssObj.setOjtAssQueId(CommonUtils.objectToLong(obj.get("ojtAssQueId")));
			ojtAssObj.setQueType(CommonUtils.objectToString(obj.get("queType")));
			ojtAssObj.setQueMarks(CommonUtils.objectToDouble(obj.get("score")));
		} else {
			throw new EnovationException(SMConstant.QUESTION_NOT_FOUND);
		}
		return ojtAssObj;

	}

	public SMAssessmetDTO getPassingMark(Session session, SkillMatrixRequest request) {
		LOGGER.info("#In SkillMatrixUtils |  INSIDE in getPassingMark ");
		SMAssessmetDTO ojtAssObj = null;

		TypedQuery<Tuple> query = session.createNativeQuery(
				" select ojtAsses.id as ojtAssId,ojtAsses.percentage as percentage,ojtAsses.passing_marks as passingMarks,"
						+ " date(ojtAsses.created_date) as assessmentAssignDate from sm_ojt_assessment ojtAsses where ojtAsses.id=:ojtAssId ",
				Tuple.class).setParameter("ojtAssId", request.getOjtAssId());

		List<Tuple> objList = query.getResultList();
		if (!CollectionUtils.isEmpty(objList)) {
			Tuple obj = objList.get(0);
			ojtAssObj = new SMAssessmetDTO();	
			ojtAssObj.setOjtAssesmentId(CommonUtils.objectToLong(obj.get("ojtAssId")));
			ojtAssObj.setPassingMarks(CommonUtils.objectToDouble(obj.get("passingMarks")));
			ojtAssObj.setPercentage(CommonUtils.objectToDouble(obj.get("percentage")));
			ojtAssObj.setAssessmentStartDate(CommonUtils.objectToString(obj.get("assessmentAssignDate")));
		}
		return ojtAssObj;
	}

	public SMAssessmetDTO getCertificateId(Session session, SkillMatrixRequest request) {
		LOGGER.info("#In SkillMatrixUtils |  INSIDE in getCertificateId ");
		SMAssessmetDTO ojtAssObj = null;
		TypedQuery<Tuple> query = session.createNativeQuery(
				"SELECT smc.id as certificateId,smc.certificate_path as path FROM sm_master_certificate smc "
						+ " LEFT JOIN sm_ojt_regis sor ON sor.desired_skill_level_id=smc.skill_level_id and smc.branch_id=sor.branch_id "
						+ " WHERE sor.id=:ojtRegisId  and smc.status='Active'",
				Tuple.class).setParameter("ojtRegisId", request.getOjtRegiId());

		List<Tuple> objList = query.getResultList();
		if (!CollectionUtils.isEmpty(objList)) {
			Tuple obj = objList.get(0);
			ojtAssObj = new SMAssessmetDTO();
			ojtAssObj.setCertificateId(CommonUtils.objectToLong(obj.get("certificateId")));
			ojtAssObj.setPath(
					EnovationConfig.buddyConfig.get("ProfilePicPathUrl") + CommonUtils.objectToString(obj.get("path")));
		}
		return ojtAssObj;
	}

	public SMAssessmetDTO getDesiredLevelId(Session session, SkillMatrixRequest request) {
		LOGGER.info("#In SkillMatrixUtils |  INSIDE in getDesiredLevelId ");
		SMAssessmetDTO ojtAssObj = null;
		TypedQuery<Tuple> query = session.createNativeQuery(
				"SELECT sor.emp_id as empId,sor.desired_skill_level_id as desiredLevelId,\r\n"
						+ " sor.workstation_id as workstationId FROM sm_ojt_regis sor WHERE sor.id=:ojtRegisId ",
				Tuple.class).setParameter("ojtRegisId", request.getOjtRegiId());

		List<Tuple> objList = query.getResultList();
		if (!CollectionUtils.isEmpty(objList)) {
			Tuple obj = objList.get(0);
			ojtAssObj = new SMAssessmetDTO();
			ojtAssObj.setEmpId(CommonUtils.objectToInt(obj.get("empId")));
			ojtAssObj.setDesiredSkillLevelId(CommonUtils.objectToLong(obj.get("desiredLevelId")));
			ojtAssObj.setWorkstationId(CommonUtils.objectToLong(obj.get("workstationId")));
		}
		return ojtAssObj;
	}

	public Tuple getOjtPlanDetailsForAudit(long ojtPlanId, Session session) {
		LOGGER.info("#In SkillMatrixUtils |  INSIDE in getOjtPlanDetailsForAudit ");
		return session.createNativeQuery(
				" select created_by as createdBy,month_value as monthValue ,start_date as startDate,year_value as yearValue,branch_id as branchId ,dept_id as deptId "
						+ ",status as status from sm_ojt_plan where id=:ojtPlanId ",
				Tuple.class).setParameter("ojtPlanId", ojtPlanId).getResultList().stream().findFirst().orElse(null);
	}

	public List<Tuple> getSMOJTRegisDetailForAudit(Session session, SMOJTPlanDTO regisObj) {
		LOGGER.info("#In SkillMatrixUtils |  INSIDE in getSMOJTRegisDetailForAudit ");
		return session.createNativeQuery(
				"  select id as id,desired_skill_level_id as desiredLvl,workstation_id as workstationId,ojt_plan_id as planId,"
						+ " trainer_emp_id as trainerId,emp_id as empId,status as status from sm_ojt_regis where id=:oJTRegisId ",
				Tuple.class).setParameter("oJTRegisId", regisObj.getOjtRegisId()).getResultList();
	}

	public List<Tuple> getSMOJTRegisDetailForAuditByPlanId(Session session, long planId) {
		LOGGER.info("#In SkillMatrixUtils |  INSIDE in getOJTAssessmentId ");
		return session.createNativeQuery(
				"  select id as id,desired_skill_level_id as desiredLvl,workstation_id as workstationId,ojt_plan_id as planId,"
						+ " trainer_emp_id as trainerId,emp_id as empId,status as status from sm_ojt_regis where ojt_plan_id=:planId ",
				Tuple.class).setParameter("planId", planId).getResultList();
	}

	public List<Tuple> getSkillMatrixActionTupleList(SkillMatrixRequest request, Session session) {
		LOGGER.info("#In SkillMatrixUtils |  INSIDE in getSkillMatrixActionTupleList ");
		StringBuilder sb = new StringBuilder(
				"select ojtsa.id as auditId,mb.name as branchName,md.dept_name as deptName,md.dept_id as deptId,ojtsa.created_date as activityDate,ojtr.id as regisId,mb.branch_id as branchId\r\n"
						+ " ,smw.workstation as workstation, (case when sml.stage_label is null then stage.stage_name else sml.stage_label end ) as activity,ojtsa.status as status,slvl.level_name as levelName,ojtsa.emp_id as empId,\r\n"
						+ " concat(IFNULL(ed.first_name,''),' ',IFNULL(ed.last_name,'')) as empName,ed.cmpy_emp_id as cmpyEmpId,ojtsa.updated_date as completedDate,\r\n"
						+ " l.id as lineId ,l.name as lineName,concat(IFNULL(e.first_name,''),' ',IFNULL(e.last_name,'')) as assignedEmpName,e.cmpy_emp_id as assignedEmpId \r\n"
						+ " from sm_ojt_skilling_audit ojtsa\r\n"
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
						+ " left join tbl_employee_details e on e.emp_id=ojtsa.emp_id  "
						+ "  left join sm_stage_label sml on sml.stage_id=stage.id AND sml.branch_id=ojtr.branch_id  "
						+ " where mb.org_id=:orgId ");

		if (request.getSearch() != null && request.getSearch().equalsIgnoreCase(EnovationConstants.SEARCH)) {
			smWorker.setFilterSearchQueryForMyPending(sb, request);
		}

		if (request.getBranchId() > 0) {
			sb.append(" and mb.branch_id=:branchId ");
		}

		if (request.getDeptId() > 0) {
			sb.append(" and md.dept_id=:deptId ");
		}
		if (request.getLineId() > 0) {
			sb.append(" and ojtr.line_id=:lineId");
		}
		if (request.getSkillLevelId() > 0) {
			sb.append(" and ojtr.desired_skill_level_id=:levelId");
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

		if (request.getColName() != null && request.getOrderType() != null) {
			sb.append(" order by " + ColumnAscDescConstants.sMActionListColName.get(request.getColName()) + " "
					+ request.getOrderType());
		} else {
			sb.append(" order by ojtsa.id DESC ");
		}
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

		if (request.getLineId() > 0) {
			query.setParameter("lineId", request.getLineId());
		}
		if (request.getSkillLevelId() > 0) {
			query.setParameter("levelId", request.getSkillLevelId());
		}
		if (request.getLimit() > 0) {
			query.setFirstResult(request.getOffset()).setMaxResults(request.getLimit());
		}

		return query.getResultList();
	}

	public boolean checkWorkstationAlreadyExit(SkillMatrixRequest request, Session session) {
		LOGGER.info("# SkillMatrixUtils | checkWorkstationAlreadyExit");

		return ((Number) session
				.createNativeQuery(
						"SELECT COUNT(*) FROM sm_workstations WHERE workstation=:workstation and is_active=1 "
								+ " and branch_id=:branchId and dept_id=:deptId")
				.setParameter("workstation", request.getWorkstation()).setParameter("branchId", request.getBranchId())
				.setParameter("deptId", request.getDeptId()).getSingleResult()).intValue() > 0;
	}

	public boolean checkUserTypeAlreadyExit(SkillMatrixRequest request, Session session) {
		long userTypeId = request.getUserTypeId();

		if ((request.getUserTypeId() == SMConstant.TRAINER_USER_TYPE_ID
				|| request.getUserTypeId() == SMConstant.TL_USER_TYPE_ID)) {

			Tuple obj = session.createNativeQuery(
					"SELECT COUNT(*) as tCount FROM sm_user_type WHERE is_active=1 and user_type_id=:userTypeId\r\n"
							+ " and dept_id=:deptId and line_id=:lineId and emp_id=:empId",
					Tuple.class).setParameter("userTypeId", userTypeId).setParameter("lineId", request.getLineId())
					.setParameter("deptId", request.getDeptId()).setParameter("empId", request.getEmpId())
					.getResultList().stream().findFirst().orElse(null);
			if (obj != null && CommonUtils.objectToInt(obj.get("tCount")) > 0) {
				throw new EnovationException("Employee is already exists for this Role");
			}
			return true;
		} else {
//			Number count = (Number) session
//					.createNativeQuery(
//							"SELECT COUNT(*) FROM sm_user_type WHERE is_active=1 and user_type_id=:userTypeId\r\n"
//									+ " and dept_id=:deptId and line_id=:lineId")
//					.setParameter("userTypeId", userTypeId).setParameter("lineId", request.getLineId())
//					.setParameter("deptId", request.getDeptId()).getSingleResult();
//
//			int countValue = count.intValue();

//			return countValue == 0;

			Tuple obj = session.createNativeQuery(
					"SELECT COUNT(*) as tCount FROM sm_user_type WHERE is_active=1 and user_type_id=:userTypeId\r\n"
							+ " and dept_id=:deptId and line_id=:lineId",
					Tuple.class).setParameter("userTypeId", userTypeId).setParameter("lineId", request.getLineId())
					.setParameter("deptId", request.getDeptId()).getResultList().stream().findFirst().orElse(null);
			if (obj != null) {
				return !(CommonUtils.objectToInt(obj.get("tCount")) > 0);
			}
			return true;

		}
	}

	public SMOJTRegis getRegistrationDetails(Session session, SkillMatrixRequest request) {
		LOGGER.info("#In SkillMatrixUtils |  INSIDE in getRegistrationDetails ");
		SMOJTRegis regis = null;

		TypedQuery<Tuple> query = session.createNativeQuery(

				"SELECT sor.created_by as createdBy, sor.emp_id as empId, sor.trainer_emp_id as trEmpId, \r\n"
						+ " sor.workstation_id as wrkId, sor.branch_id as brnId, sor.current_skill_level_id as crtLvl, \r\n"
						+ " sor.desired_skill_level_id as dsrLvl, sor.dept_id as deptId,sor.line_id as lineId  \r\n"
						+ " FROM sm_ojt_regis sor WHERE sor.id=:smOjtRegiId ",
				Tuple.class).setParameter("smOjtRegiId", request.getOjtRegiId());

		List<Tuple> objList = query.getResultList();

		if (!CollectionUtils.isEmpty(objList)) {
			Tuple obj = objList.get(0);

			regis = new SMOJTRegis();
			regis.setId(request.getOjtRegiId());
			regis.setCreatedBy(CommonUtils.objectToInt(obj.get("createdBy")));
			if (obj.get("empId") != null) {
				regis.setEmpDetails(new EmployeeDetails(CommonUtils.objectToInt(obj.get("empId"))));
			}
			if (obj.get("wrkId") != null) {
				regis.setWorkstation(new SMWorkstations(CommonUtils.objectToLong(obj.get("wrkId"))));
			}
			if (obj.get("brnId") != null) {
				regis.setBranch(new BranchMaster(CommonUtils.objectToInt(obj.get("brnId"))));
			}
			if (obj.get("crtLvl") != null) {
				regis.setCurrentSkillLevel(new SMSkillLevel(CommonUtils.objectToLong(obj.get("crtLvl"))));
			}
			if (obj.get("dsrLvl") != null) {
				regis.setDesiredSkillLevel(new SMSkillLevel(CommonUtils.objectToLong(obj.get("dsrLvl"))));
			}
			if (obj.get("deptId") != null) {
				regis.setDept(new DepartmentMaster(CommonUtils.objectToInt(obj.get("deptId"))));
			}
			if (obj.get("trEmpId") != null) {
				regis.setTrainerDetails(new EmployeeDetails(CommonUtils.objectToInt(obj.get("trEmpId"))));
			}
			if (obj.get("lineId") != null) {
				regis.setLine(new Line(CommonUtils.objectToLong(obj.get("lineId"))));
			}
			if (obj.get("brnId") != null) {
				regis.setBranch(new BranchMaster(CommonUtils.objectToInt(obj.get("brnId"))));
			}
		}
		return regis;
	}

	public List<HashMap<String, Object>> getEmpDetails(Session session, SMShiftDTO shiftDto,
			SkillMatrixRequest request) {
		LOGGER.info("#In SkillMatrixUtils |  INSIDE in getEmpDetails ");
		List<HashMap<String, Object>> list = new ArrayList<>();
		StringBuilder sb = new StringBuilder(
				" select wfd.id as id , wfd.from_date as fromDate,wfd.to_date as toDate,wfd.emp_id as empId,\r\n"
						+ " CONCAT(ifnull(ed.first_name, ''),' ',ifnull(ed.last_name, '')) empName,wfd.workstations_id as workstationId,"
						+ " ws.workstation as workstationName,wfd.created_by as createdBy,lvl.level_name as levelName\r\n"
						+ " from sm_wf_deployment wfd \r\n"
						+ " inner join sm_workstations ws on ws.id=wfd.workstations_id\r\n"
						+ " inner join sm_shifts s on s.id=wfd.shift_id \r\n"
						+ " inner join sm_emp_skill_matrix empsm on empsm.emp_id=wfd.emp_id\r\n"
						+ " left join sm_skill_level lvl on lvl.id=empsm.skill_level_id\r\n"
						+ " left join tbl_employee_details ed on ed.emp_id=wfd.emp_id\r\n"
						+ " where s.branch_id=:branchId and wfd.shift_id=:shiftId ");

		if (request.getSearch() != null) {
			sb.append(" and (CONCAT(ifnull(ed.first_name, ''),' ',ifnull(ed.last_name, '')) like '%{search}%'"
					+ " or ws.workstation like '%{search}%' or lvl.level_name like '%{search}%' or s.shift_name like '%{search}%' )");
		}
		if (request.getFromDt() != null && request.getToDt() != null) {
			sb.append(" and wfd.from_date=:fromDt and wfd.to_date=:toDt ");
		}
		if (request.getColName() != null && request.getOrderType() != null) {
			sb.append(" order by " + ColumnAscDescConstants.smDeployementColName.get(request.getColName()) + " "
					+ request.getOrderType());
		} else {
			sb.append(" order by wfd.id DESC ");
		}

		String tmpQuery = sb.toString().replace("{search}", (String.valueOf(request.getSearch())));

		TypedQuery<Tuple> query = session.createNativeQuery(tmpQuery, Tuple.class)
				.setParameter("branchId", shiftDto.getBranchId()).setParameter("shiftId", shiftDto.getShiftId());

		if (request.getLimit() > 0) {
			query.setFirstResult(request.getOffset()).setMaxResults(request.getLimit());
		}

		if (request.getFromDt() != null && request.getToDt() != null) {
			query.setParameter("fromDt", request.getFromDt());
			query.setParameter("toDt", request.getToDt());
		}

		List<Tuple> tupleList = query.getResultList();
		LOGGER.info("# tupleList size : {}", tupleList.size());
		if (!CollectionUtils.isEmpty(tupleList)) {
			list = CommonUtils.parseTupleList(tupleList);
		}
		return list;
	}

	public boolean checkWorkstationAlreadyExitId(SkillMatrixRequest request, Session session) {
		LOGGER.info("# SkillMatrixUtils | checkWorkstationAlreadyExitId");

		StringBuilder sb = new StringBuilder(
				"select ifnull((select coalesce(count(id),0) FROM sm_workstations smw WHERE smw.is_active=1 AND smw.workstation=:workstation \r\n"
						+ "  AND smw.branch_id=:branchId AND smw.dept_id=:deptId ");

		if (request.getId() > 0) {
			sb.append(" and  smw.id != :workId ");
		}
		sb.append(" ),0) as workstations ");

		TypedQuery<Tuple> query = session.createNativeQuery(sb.toString());

		if (request.getId() > 0) {
			query.setParameter("workId", request.getId());
		}

		return ((Number) query.setParameter("workstation", request.getWorkstation())
				.setParameter("branchId", request.getBranchId()).setParameter("deptId", request.getDeptId())
				.getSingleResult()).intValue() == 0;
	}

	public boolean checkUserTypeAlreadyExitId(SkillMatrixRequest request, Session session) {
		LOGGER.info("# SkillMatrixUtils | checkUserTypeAlreadyExitId");

		StringBuilder sb = new StringBuilder(
				"SELECT  ifnull((select coalesce(count(id),0) FROM sm_user_type WHERE user_type_id=:userTypeId  and dept_id=:deptId");

		if (request.getId() > 0) {
			sb.append(" and  id != :UserTypeId ");
		}
		if ((request.getUserTypeId() == SMConstant.TRAINER_USER_TYPE_ID
				|| request.getUserTypeId() == SMConstant.TL_USER_TYPE_ID) && request.getEmpId() > 0) {
			sb.append(" and  emp_id=:empId ");
		}
		sb.append(" ),0) as UserType ");

		TypedQuery<Tuple> query = session.createNativeQuery(sb.toString());

		if (request.getId() > 0) {
			query.setParameter("UserTypeId", request.getId());
		}
		if (request.getUserTypeId() == 1 && request.getEmpId() > 0) {
			query.setParameter("empId", request.getEmpId());
		}

		return ((Number) query.setParameter("userTypeId", request.getUserTypeId())
				.setParameter("deptId", request.getDeptId()).getSingleResult()).intValue() == 0;
	}

	public boolean isReasonExist(Session session, SkillMatrixRequest request) {
		LOGGER.info("# SkillMatrixUtils | isReasonExist");
		StringBuilder sb = new StringBuilder(
				"select ifnull((select coalesce(count(id),0) from sm_gap_reason where is_active=1 and branch_id=:branchId "
						+ "and reason=:reason  ");

		if (request.getReasonId() > 0) {
			LOGGER.info("# id{},", request.getReasonId());
			sb.append(" and id !=:id ");
		}
		sb.append(" ),0) as workFlowCnt  ");
		LOGGER.info("sb: {}", sb);
		TypedQuery<Tuple> query = session.createNativeQuery(sb.toString());

		if (request.getReasonId() > 0) {
			query.setParameter("id", request.getReasonId());
		}

		return ((Number) query.setParameter("branchId", request.getBranchId())
				.setParameter("reason", request.getReason()).getSingleResult()).intValue() == 0;

	}

	public boolean isShiftExist(Session session, SkillMatrixRequest request) {
		LOGGER.info("# SkillMatrixUtils | isTitleExist");
		StringBuilder sb = new StringBuilder(
				"select ifnull((select coalesce(count(id),0) from sm_shifts where is_active=1 and branch_id=:branchId "
						+ "and shift_name=:shift  ");

		if (request.getShiftId() > 0) {
			sb.append(" and id !=:id ");
		}
		sb.append(" ),0) as workFlowCnt  ");

		TypedQuery<Tuple> query = session.createNativeQuery(sb.toString());

		if (request.getShiftId() > 0) {
			query.setParameter("id", request.getShiftId());
		}

		return ((Number) query.setParameter("branchId", request.getBranchId())
				.setParameter("shift", request.getShiftName()).getSingleResult()).intValue() == 0;

	}

	public int getEmpAlreadyAddedInOJT(Session session, SMOJTPlanDTO regisObj) {
		LOGGER.info("# SkillMatrixUtils | getEmpAlreadyAddedInOJT");
		return ((Number) session
				.createNativeQuery("select ifnull(count(*),0) as count from sm_ojt_regis ojt "
						+ " where ojt.status=:status and ojt.emp_id=:empId")
				.setParameter("status", EnovationConstants.PENDING_STRING).setParameter("empId", regisObj.getEmpId())
				.getSingleResult()).intValue();

	}

	public List<SkillMatrixRequest> getSkillLevelList(Session session) {
		LOGGER.info("In ReadWriteFileImpl | getInterList");
		List<SkillMatrixRequest> levelList = new ArrayList<>();
		List<Tuple> list = session
				.createNativeQuery("select id as id ,level_name as levelName from sm_skill_level ", Tuple.class)
				.getResultList();

		if (!CollectionUtils.isEmpty(list)) {
			list.stream().forEach(o -> {
				SkillMatrixRequest data = new SkillMatrixRequest();
				data.setSkillLevelId(CommonUtils.objectToLong(o.get("id")));
				data.setSkillLevel(CommonUtils.objectToString(o.get("levelName")));
				levelList.add(data);
			});
		}
		return levelList;
	}

	public List<BranchMaster> getBranchList(Session session) {
		LOGGER.info("In ReadWriteFileImpl | getBranchList");
		List<BranchMaster> branch = new ArrayList<>();
		List<Tuple> list = session.createNativeQuery(
				"select branch_id as branchId,name as branchName from master_branch where is_active='Y'", Tuple.class)
				.getResultList();

		if (!CollectionUtils.isEmpty(list)) {
			list.stream().forEach(o -> {
				BranchMaster data = new BranchMaster();
				data.setBranchId(CommonUtils.objectToInt(o.get("branchId")));
				data.setName(CommonUtils.objectToString(o.get("branchName")));
				branch.add(data);
			});
		}
		return branch;
	}

	public Tuple getSMConfigDetails(SkillMatrixRequest request, Session session) {
		Tuple tupleObj = session.createNativeQuery(
				"Select s.id as id,s.no_of_days as noOfDays,s.skill_level_id  as skillLevelId,s.created_by as createdBy,s.created_date as createdDate,\r\n"
						+ "s.updated_by as updatedBy,s.updated_date as updatedDate  from sm_assessment_config s where s.id=:id",
				Tuple.class).setParameter("id", request.getId()).getResultList().stream().findFirst().orElse(null);
		return tupleObj;
	}

	public Tuple getSMConfigDetail(SkillMatrixRequest request, Session session) {
		LOGGER.info("# SkillMatrixUtils | getSMConfigDetail");
		return session.createNativeQuery(
				"Select id as id, no_of_days as noOfDays, skill_level_id as skillLevelId, created_by as createdBy, created_date as createdDate,\r\n"
						+ "updated_by as updatedBy, updated_date as updatedDate\r\n" + " from sm_config where id=:id ",
				Tuple.class).setParameter("id", request.getId()).getResultList().stream().findFirst().orElse(null);
	}

	public int getAssessmentDays(Session session, int branchId, long levelId) {
		LOGGER.info("#In SkillMatrixUtils |  INSIDE in getAssessmentDays ");
		return ((Number) session.createNativeQuery(
				"select sa.no_of_days as noOfDay from sm_assessment_config sa  where sa.branch_id=:branchId and sa.skill_level_id=:levelId limit 1")
				.setParameter("branchId", branchId).setParameter("levelId", levelId).getSingleResult()).intValue();
	}

	public boolean levelAlreadyExist(Session session, SkillMatrixRequest request) {
		LOGGER.info("# SkillMatrixUtils | levelAlreadyExist");

		StringBuilder sb = new StringBuilder(
				"select ifnull((select coalesce(count(id),0) from sm_assessment_config where  branch_id =:branchId \r\n"
						+ "	and skill_level_id =:skillLevelId ");

//		StringBuilder sb = new StringBuilder(
//				"select ifnull((select coalesce(count(id),0) from sm_assessment_config where  branch_id =:branchId \r\n"
//						+ "	and skill_level_id =:skillLevelId  "
//						+ "and dept_id=:deptId and line_id=:lineId  and workstation_id=:workstationId ");

		if (request.getId() > 0) {
			sb.append(" and id !=:id ");
		}
		sb.append(" ),0) as assessmentCount ");
		TypedQuery<Tuple> query = session.createNativeQuery(sb.toString());

		if (request.getId() > 0) {
			query.setParameter("id", request.getId());
		}

		return ((Number) query.setParameter("branchId", request.getBranchId())
				.setParameter("skillLevelId", request.getSkillLevelId()).getSingleResult()).intValue() == 0;

//		return ((Number) query.setParameter("branchId", request.getBranchId())
//				.setParameter("skillLevelId", request.getSkillLevelId())
//				.setParameter("deptId", request.getDeptId()).setParameter("lineId", request.getLineId())
//				.setParameter("workstationId", request.getWorkstationId()).getSingleResult()).intValue() == 0;
	}

	public Tuple getSMStageLabelDetails(Session session, long id) {
		LOGGER.info("# SkillMatrixUtils | getSMStageLabelDetails");

		return session.createNativeQuery(
				"SELECT stl.id AS stageLabelId, stl.stage_label AS stageLabel, stl.branch_id AS branchId, stl.stage_id AS stageId "
						+ "FROM sm_stage_label stl WHERE stl.id=:stageLabelId",
				Tuple.class).setParameter("stageLabelId", id).getResultList().stream().findFirst().orElse(null);
	}

	public boolean stageIdExist(Session session, SkillMatrixRequest request) {
		LOGGER.info("# SkillMatrixUtils | doesStageIdExist");
		StringBuilder sb = new StringBuilder(
				"SELECT ifnull((SELECT coalesce(count(id),0) FROM sm_stage_label WHERE branch_id=:branchId "
						+ "AND stage_id=:stageId  ");

		if (request.getId() > 0) {
			sb.append(" AND id !=:id ");
		}
		sb.append(" ),0) AS workFlowCnt  ");

		TypedQuery<Tuple> query = session.createNativeQuery(sb.toString());

		if (request.getId() > 0) {
			query.setParameter("id", request.getId());
		}

		return ((Number) query.setParameter("branchId", request.getBranchId())
				.setParameter("stageId", request.getStageId()).getSingleResult()).intValue() > 0;

	}

	public boolean stageLabelExist(Session session, SkillMatrixRequest request) {
		LOGGER.info("# SkillMatrixUtils | doesStageLabelExist");
		StringBuilder sb = new StringBuilder(
				"SELECT ifnull((SELECT coalesce(count(id),0) FROM sm_stage_label WHERE branch_id=:branchId "
						+ "AND stage_label=:stageLabel ");

		if (request.getId() > 0) {
			sb.append(" AND id !=:id ");
		}
		sb.append(" ),0) AS workFlowCnt  ");

		TypedQuery<Tuple> query = session.createNativeQuery(sb.toString());

		if (request.getId() > 0) {
			query.setParameter("id", request.getId());
		}

		return ((Number) query.setParameter("branchId", request.getBranchId())
				.setParameter("stageLabel", request.getStageLabel()).getSingleResult()).intValue() > 0;

	}

	public Tuple getCategoryDetails(Session session, SkillMatrixRequest request) {
		Tuple tupleObj = session
				.createNativeQuery(" Select id as id,category_name as categoryName,assessment_id as assessmentId"
						+ " from sm_category where id=:id", Tuple.class)
				.setParameter("id", request.getId()).getResultList().stream().findFirst().orElse(null);
		return tupleObj;
	}

	public HashMap<String, Object> getRegistrationList(Session session, long oJTRegiId) {

		HashMap<String, Object> regisMap = new HashMap<>();

		Tuple tuple = (Tuple) session.createNativeQuery(
				" Select sor.id as ojtRegiId ,sor.created_by as createdBy,sor.created_date as createdDate,sor.status as status,\r\n"
						+ " sor.updated_by as updatedBy,sor.updated_date as updatedDate,sor.current_skill_level_id as currentSkillLvlId,sor.desired_skill_level_id as desiredSkillLvlId,\r\n"
						+ "  sor.emp_id as empId,sor.trainer_emp_id as trainerEmpId,sor.workstation_id workstationId,sor.branch_id as branchId,sor.dept_id as deptId,sor.ojt_plan_id as ojtPlanId,\r\n"
						+ "  sor.line_id as lineId,concat(ifnull(e.first_name,''),' ',ifnull(e.last_name,'')) as oeEmpName,sw.workstation as workstation,\r\n"
						+ "  concat(ifnull(ed.first_name,''),' ',ifnull(ed.last_name,'')) as trainerName,ln.name lineName,rln.level_name as currentSkillLevelName, \r\n"
						+ "  dln.level_name as desiredSkillLevelName\r\n" + "  from sm_ojt_regis sor\r\n"
						+ "	 left join tbl_employee_details e on e.emp_id=sor.emp_id\r\n"
						+ "	 left join sm_workstations sw on sw.id=sor.workstation_id\r\n"
						+ "	 left join tbl_employee_details ed on ed.emp_id=sor.trainer_emp_id\r\n"
						+ "	 left join dwm_line ln on sor.line_id \r\n"
						+ "	 left join sm_skill_level dln on dln.id=sor.desired_skill_level_id \r\n"
						+ "	 left join sm_skill_level rln on rln.id=sor.current_skill_level_id \r\n"
						+ "	 where sor.id=:oJTRegisId ",
				Tuple.class).setParameter("oJTRegisId", oJTRegiId).getSingleResult();

		if (tuple != null) {
			regisMap.put("ojtRegiId", CommonUtils.objectToLong(tuple.get("ojtRegiId")));
			regisMap.put("createdBy", CommonUtils.objectToInt(tuple.get("createdBy")));
			regisMap.put("createdDate", (Timestamp) tuple.get("createdDate"));
			regisMap.put("status", CommonUtils.objectToString(tuple.get("status")));
			regisMap.put("updatedBy", CommonUtils.objectToInt(tuple.get("updatedBy")));
			regisMap.put("updatedDate", (Timestamp) tuple.get("updatedDate"));
			regisMap.put("currentSkillLvlId", CommonUtils.objectToLong(tuple.get("currentSkillLvlId")));
			regisMap.put("desiredSkillLvlId", CommonUtils.objectToLong(tuple.get("desiredSkillLvlId")));
			regisMap.put("empId", CommonUtils.objectToInt(tuple.get("empId")));
			regisMap.put("trainerEmpId", CommonUtils.objectToInt(tuple.get("trainerEmpId")));
			regisMap.put("workstationId", CommonUtils.objectToLong(tuple.get("workstationId")));
			regisMap.put("branchId", CommonUtils.objectToInt(tuple.get("branchId")));
			regisMap.put("deptId", CommonUtils.objectToInt(tuple.get("deptId")));
			regisMap.put("ojtPlanId", CommonUtils.objectToLong(tuple.get("ojtPlanId")));
			regisMap.put("lineId", CommonUtils.objectToInt(tuple.get("lineId")));
			regisMap.put("oeEmpName", CommonUtils.objectToString(tuple.get("oeEmpName")));
			regisMap.put("workstation", CommonUtils.objectToString(tuple.get("workstation")));
			regisMap.put("trainerName", CommonUtils.objectToString(tuple.get("trainerName")));
			regisMap.put("lineName", CommonUtils.objectToString(tuple.get("lineName")));
			regisMap.put("currentSkillLevelName", CommonUtils.objectToString(tuple.get("currentSkillLevelName")));
			regisMap.put("desiredSkillLevelName", CommonUtils.objectToString(tuple.get("desiredSkillLevelName")));
		}

		return regisMap;
	}

	public List<Tuple> getSkillList(Session session, long oJTRegiId) {
		List<HashMap<String, Object>> resultList = new ArrayList<>();
		List<Tuple> tuplelist = null;
		tuplelist = session.createNativeQuery(
				"select sos.id as skillingId, sos.created_by as createdBy,concat(ifnull(e.first_name,''),' ',ifnull(e.last_name,'')) as createdByName ,\r\n"
						+ "	sos.created_date as createdDate,sos.status as status,sos.checksheet_id as checksheetId,sos.ojt_regis_id as ojtRegisId\r\n"
						+ "	,sos.assessment_due_date as assessmentDueDate,sos.assessment_id as assessmentId\r\n"
						+ "	from sm_ojt_skilling sos left join tbl_employee_details e on e.emp_id=sos.created_by  where sos.ojt_regis_id=:oJTRegiId ",
				Tuple.class).setParameter("oJTRegiId", oJTRegiId).getResultList();

		return tuplelist;
	}

	public boolean assessmentAlreadyExist(Session session, SkillMatrixRequest request) {
		LOGGER.info("# SkillMatrixUtils | assessmentAlreadyExist");

		StringBuilder sb = new StringBuilder(
				"select ifnull((select coalesce(count(id),0) from sm_assessment where is_active=1 and branch_id=:branchId and skill_level_id=:skillId "
						+ " and dept_id=:deptId and line_id=:LineId and workstation_id =:workstationId ");

		if (request.getAssessmentId() > 0) {
			sb.append(" and id !=:id ");
		}
		sb.append(" ),0) as workFlowCnt  ");

		TypedQuery<Tuple> query = session.createNativeQuery(sb.toString());

		if (request.getAssessmentId() > 0) {
			query.setParameter("id", request.getAssessmentId());
		}

		return ((Number) query.setParameter("branchId", request.getBranchId())
				.setParameter("skillId", request.getSkillLvlId()).setParameter("deptId", request.getDeptId())
				.setParameter("LineId", request.getLineId()).setParameter("workstationId", request.getWorkstationId())
				.getSingleResult()).intValue() == 0;
	}

	@SuppressWarnings("unchecked")
	public boolean isTitleNotExist(Session session, SkillMatrixRequest request) {
		LOGGER.info("# SkillMatrixUtils | isTitleNotExist");

		StringBuilder sb = new StringBuilder(
				"select ifnull((select coalesce(count(id),0) from sm_assessment where is_active=1 and branch_id=:branchId and skill_level_id=:skillId and title=:title ");

		if (request.getAssessmentId() > 0) {
			sb.append(" and id !=:id ");
		}
		sb.append(" ),0) as workFlowCnt  ");

		TypedQuery<Tuple> query = session.createNativeQuery(sb.toString());

		if (request.getAssessmentId() > 0) {
			query.setParameter("id", request.getAssessmentId());
		}

		return ((Number) query.setParameter("branchId", request.getBranchId())
				.setParameter("skillId", request.getSkillLvlId()).setParameter("title", request.getTitle())
				.getSingleResult()).intValue() == 0;
	}

	public SkillMatrixRequest getStageWiseDataForMail(Session session, SkillMatrixRequest request) {

		SkillMatrixRequest emailData = new SkillMatrixRequest();
		TypedQuery<Tuple> query = session.createNativeQuery(
				"SELECT line.name AS lineName,md.dept_name AS deptName,smw.workstation AS workstation,now() AS assignedDate,\r\n"
						+ "concat(coalesce(ed.first_name,''),' ',coalesce(ed.last_name,'')) AS assignedBy,concat(coalesce(e.first_name,''),' ',coalesce(e.last_name,'')) AS assignedTo, \r\n"
						+ "e.email_id as emailId,(case when sml.stage_label is null then stage.stage_name else sml.stage_label end ) as status,\r\n"
						+ "o.portal_link as portalLink,o.logo as logo\r\n" + "FROM sm_ojt_skilling_audit audit \r\n"
						+ "INNER JOIN sm_ojt_skilling skilling ON audit.skilling_id=skilling.id  \r\n"
						+ "INNER JOIN sm_ojt_regis regi ON skilling.ojt_regis_id=regi.id \r\n"
						+ "INNER JOIN sm_workstations smw ON smw.id=regi.workstation_id\r\n"
						+ "left JOIN master_department md ON md.dept_id=regi.dept_id\r\n"
						+ "left JOIN dwm_line line ON line.id=regi.line_id\r\n"
						+ "left join sm_stage stage on stage.id=:stageId \r\n"
						+ "left join sm_stage_label sml on sml.stage_id=stage.id \r\n"
						+ "LEFT JOIN tbl_employee_details ed ON ed.emp_id=audit.emp_id\r\n"
						+ "LEFT JOIN tbl_employee_details e ON e.emp_id=:empId \r\n"
						+ "left join master_branch mb on mb.branch_id=regi.branch_id\r\n"
						+ "left join master_organization o on o.org_id=mb.org_id\r\n"
						+ "WHERE audit.id=:skillingAuditId ",
				Tuple.class).setParameter("skillingAuditId", request.getSkillingAuditId())
				.setParameter("empId", request.getEmpId()).setParameter("stageId", request.getStageId());

		List<Tuple> objList = query.getResultList();
		if (!CollectionUtils.isEmpty(objList)) {
			Tuple obj = objList.get(0);

			emailData.setAssignedBy(CommonUtils.objectToString(obj.get("assignedBy")));
			emailData.setEmailId(CommonUtils.objectToString(obj.get("emailId")));
			emailData.setLineName(CommonUtils.objectToString(obj.get("lineName")));
			emailData.setWorkstation(CommonUtils.objectToString(obj.get("workstation")));
			emailData.setAssignedDate(CommonUtils.objectToString(obj.get("assignedDate")));
			emailData.setAssignedTo(CommonUtils.objectToString(obj.get("assignedTo")));
			emailData.setStatus(CommonUtils.objectToString(obj.get("status")));
			emailData.setPortalLink(CommonUtils.objectToString(obj.get("portalLink")));
			emailData.setLogo(CommonUtils.objectToString(obj.get("logo")));
		}
		return emailData;
	}

	public void getDataForMail(Session session, SMOJTRegis regis, SkillMatrixRequest emailDto) {
		LOGGER.info("#In SkillMatrixWorker |  INSIDE in getDataForMail ");
		TypedQuery<Tuple> tupleList = session.createNativeQuery(
				" select ojt.id as regisId,ojt.status as status,ojt.current_skill_level_id as currentLvlId,ojt.created_date as startDate,\r\n"
						+ "	ojt.desired_skill_level_id as desiredLvlId,ojt.emp_id as empId,ojt.trainer_emp_id as trainerId,\r\n"
						+ "	ojt.workstation_id as workStationId,ojt.branch_id as branchId,ojt.dept_id as deptId,\r\n"
						+ "	 ojt.ojt_plan_id as planId,concat(ifnull(e.first_name,''),' ',ifnull(e.last_name,'')) as empName,\r\n"
						+ "	 e.email_id as emailId,ojt.line_id as lineId,l.name as line,sw.workstation as workstation,\r\n"
						+ "	 d.dept_name as deptName,sl.level_name as level ,concat(ifnull(et.first_name,''),' ',ifnull(et.last_name,'')) as trainerName,\r\n"
						+ "	 et.email_id as trainerEmailId,b.name as branchName,o.logo as orgLogo,o.portal_link as portalLink ,o.org_id as orgId  \r\n"
						+ "  from sm_ojt_regis ojt \r\n"
						+ "	 left join tbl_employee_details e on e.emp_id=ojt.emp_id\r\n"
						+ "	 left join sm_workstations sw on sw.id=ojt.workstation_id \r\n"
						+ "	 left join dwm_line l on l.id=ojt.line_id \r\n"
						+ "	 left join master_department d on d.dept_id=ojt.dept_id \r\n"
						+ "	 left join sm_skill_level sl on sl.id=ojt.desired_skill_level_id \r\n"
						+ "	 left join tbl_employee_details et on et.emp_id=ojt.trainer_emp_id  \r\n"
						+ "	 inner join master_branch b on ojt.branch_id =b.branch_id \r\n"
						+ "	 inner join master_organization o on o.org_id = b.org_id \r\n"
						+ "	 where ojt.id=:ojtRegisId",
				Tuple.class).setParameter("ojtRegisId", regis.getId());

		List<Tuple> objList = tupleList.getResultList();

		LOGGER.info("objList size :" + objList.size());
		if (!CollectionUtils.isEmpty(objList)) {
			Tuple obj = objList.get(0);
			emailDto.setOjtRegiId(CommonUtils.objectToLong(obj.get("regisId")));
			emailDto.setEmpName(CommonUtils.objectToString(obj.get("empName")));
			emailDto.setEmailId(CommonUtils.objectToString(obj.get("emailId")));
			emailDto.setLineName(CommonUtils.objectToString(obj.get("line")));
			emailDto.setWorkstation(CommonUtils.objectToString(obj.get("workstation")));
			emailDto.setSkillLevel(CommonUtils.objectToString(obj.get("level")));
			emailDto.setDesiredSkillLevelName(CommonUtils.objectToString(obj.get("level")));
			emailDto.setEmpId(CommonUtils.objectToInt(obj.get("empId")));
			emailDto.setDeptId(CommonUtils.objectToInt(obj.get("deptId")));
			emailDto.setTrainerEmailId(CommonUtils.objectToString(obj.get("trainerEmailId")));
			emailDto.setTrainerName(CommonUtils.objectToString(obj.get("trainerName")));
			emailDto.setDeptName(CommonUtils.objectToString(obj.get("deptName")));
			emailDto.setCurrentSkillLevelId(CommonUtils.objectToLong(obj.get("currentLvlId")));
			emailDto.setDesiredSkillLevelId(CommonUtils.objectToLong(obj.get("desiredLvlId")));
			emailDto.setCreatedDate((Timestamp) obj.get("startDate"));
			emailDto.setLogo(EnovationConfig.buddyConfig.get("ProfilePicPathUrl")
					+ CommonUtils.objectToString(obj.get("orgLogo")));
			emailDto.setPortalLink(CommonUtils.objectToString(obj.get("portalLink")));
		}

	}

	public List<SkillMatrixRequest> getDeptList(Session session, int branchId) {
		LOGGER.info("In ReadWriteFileImpl | getDeptList");
		List<SkillMatrixRequest> deptList = new ArrayList<>();
		List<Tuple> list = session.createNativeQuery(
				"select d.dept_id as deptId,d.dept_name as deptName from master_department d where d.branch_id=:branchId ",
				Tuple.class).setParameter("branchId", branchId).getResultList();

		if (!CollectionUtils.isEmpty(list)) {
			list.stream().forEach(o -> {
				SkillMatrixRequest data = new SkillMatrixRequest();
				data.setDeptId(CommonUtils.objectToInt(o.get("deptId")));
				data.setDeptName(CommonUtils.objectToString(o.get("deptName")));
				deptList.add(data);
			});
		}
		return deptList;
	}

	public List<SkillMatrixRequest> getLineList(Session session, int deptId) {
		LOGGER.info("In ReadWriteFileImpl | getLineList");
		List<SkillMatrixRequest> lineList = new ArrayList<>();
		List<Tuple> list = session.createNativeQuery(
				"select l.id as lineId ,l.name as lineName from dwm_line l where l.is_active='Y' and l.dept_id=:deptId ",
				Tuple.class).setParameter("deptId", deptId).getResultList();

		if (!CollectionUtils.isEmpty(list)) {
			list.stream().forEach(o -> {
				SkillMatrixRequest data = new SkillMatrixRequest();
				data.setLineId(CommonUtils.objectToLong(o.get("lineId")));
				data.setLineName(CommonUtils.objectToString(o.get("lineName")));
				lineList.add(data);
			});
		}
		return lineList;
	}

	public List<SkillMatrixRequest> getWorkstationList(Session session, long lineId) {
		LOGGER.info("In ReadWriteFileImpl | getWorkstationList");
		List<SkillMatrixRequest> WorkList = new ArrayList<>();
		List<Tuple> list = session.createNativeQuery(
				" select sw.id as workstationId,sw.workstation as workstation from sm_workstations sw where sw.is_active=1 and sw.line_id=:lineId",
				Tuple.class).setParameter("lineId", lineId).getResultList();

		if (!CollectionUtils.isEmpty(list)) {
			list.stream().forEach(o -> {
				SkillMatrixRequest data = new SkillMatrixRequest();
				data.setWorkstationId(CommonUtils.objectToLong(o.get("workstationId")));
				data.setWorkstation(CommonUtils.objectToString(o.get("workstation")));
				WorkList.add(data);
			});
		}
		return WorkList;
	}

	// Added by Sonali L. Jan 17 2024 - added assessmentTypeAlreadyExist
	public boolean assessmentTypeAlreadyExist(Session session, SkillMatrixRequest request) {
		LOGGER.info("# SkillMatrixUtils | assessmentAlreadyExist");

		StringBuilder sb = new StringBuilder(
				"select ifnull((select coalesce(count(id),0) from sm_assessment where is_active=1 and branch_id=:branchId "
						+ " and dept_id=:deptId and line_id=:LineId and workstation_id =:workstationId and assessment_type=:assessmentType");

		if (request.getAssessmentId() > 0) {
			sb.append(" and id !=:id ");
		}
		sb.append(" ),0) as assessmentTypeCnt  ");

		TypedQuery<Tuple> query = session.createNativeQuery(sb.toString());

		if (request.getAssessmentId() > 0) {
			query.setParameter("id", request.getAssessmentId());
		}

		return ((Number) query.setParameter("branchId", request.getBranchId())
				.setParameter("deptId", request.getDeptId()).setParameter("assessmentType", request.getAssessmentType())
				.setParameter("LineId", request.getLineId()).setParameter("workstationId", request.getWorkstationId())
				.getSingleResult()).intValue() == 0;
	}

	public SMAssessmetDTO getPassingMarks(Session session, SkillMatrixRequest request) {
		LOGGER.info("#In SkillMatrixUtils |  INSIDE in getPassingMark ");
		SMAssessmetDTO ojtAssObj = null;

		TypedQuery<Tuple> query = session.createNativeQuery(
				"select ojtAsses.id as ojtAssId,ojtAsses.percentage as percentage,asses.passing_marks as passingMarks"
						+ " from sm_ojt_assessment ojtAsses"
						+ " inner join sm_assessment asses on asses.id = ojtAsses.assessment_id "
						+ " where ojtAsses.id=:ojtassId AND ojtAsses.assessment_id=:assessId ",
				Tuple.class).setParameter("ojtassId", request.getOjtAssId())
				.setParameter("assessId", request.getAssessmentId());

		List<Tuple> objList = query.getResultList();
		if (!CollectionUtils.isEmpty(objList)) {
			Tuple obj = objList.get(0);
			ojtAssObj = new SMAssessmetDTO();
			ojtAssObj.setOjtAssesmentId(CommonUtils.objectToLong(obj.get("ojtAssId")));
			ojtAssObj.setPassingMarks(CommonUtils.objectToDouble(obj.get("passingMarks")));
			ojtAssObj.setPercentage(CommonUtils.objectToDouble(obj.get("percentage")));
		}
		return ojtAssObj;
	}

	public int isDocNameExist(Session session, SkillMatrixRequest request) {
		int branchId = ((Number) session.createNativeQuery(
				"select coalesce((select COALESCE(branch_id ,0) as branchId from sm_doc_name where branch_id=:branchId),0) as branchId")
				.setParameter("branchId", request.getBranchId()).getSingleResult()).intValue();
		return branchId;
	}

	public Tuple getDocNameDetailsById(Session session, SkillMatrixRequest request) {
		StringBuilder sb = new StringBuilder(
				"select id as id , doc_name as docName, branch_id as branchId ,created_by as createdBy,"
						+ " updated_by as updatedBy,created_date as createdDate, updated_date as updatedDate "
						+ " from sm_doc_name where id=:id ");

		return session.createNativeQuery(sb.toString(), Tuple.class).setParameter("id", request.getId()).getResultList()
				.stream().findFirst().orElse(null);

	}

	public Tuple getDocNameByBranchId(SkillMatrixRequest request, Session session) {
		StringBuilder sb = new StringBuilder(
				"select id as id , doc_name as docName, branch_id as branchId ,created_by as createdBy,"
						+ " updated_by as updatedBy,created_date as createdDate, updated_date as updatedDate ,"
						+ " current_timestamp() as currentDate " + " from sm_doc_name where branch_id=:branchId ");

		Tuple tupleList = session.createNativeQuery(sb.toString(), Tuple.class)
				.setParameter("branchId", request.getBranchId()).getResultList().stream().findFirst().orElse(null);
		return tupleList;
	}

	public boolean updateTotalAssessedTime(Session session, long skillingAuditId, long assessmentTimeInterval) {
		LOGGER.info("In SkillMatrixUtils |Inside updateTotalAssessedTime");
		boolean flag = false;
		long currentTime = 0;

		TypedQuery<Tuple> query = session
				.createNativeQuery(" select total_assessed_time as totalAssessedTime from sm_ojt_assessment where \r\n"
						+ " skilling_audit_id=:skillingAuditId LIMIT 1", Tuple.class);
		query.setParameter("skillingAuditId", skillingAuditId);

		List<Tuple> objList = query.getResultList();

		if (!CollectionUtils.isEmpty(objList)) {
			Tuple obj = objList.get(0);
			currentTime = CommonUtils.objectToLong(obj.get("totalAssessedTime"));

			LOGGER.info("SkillMatrixUtils | currentTime {}", currentTime);
		}
		assessmentTimeInterval = assessmentTimeInterval + currentTime;

		LOGGER.info("SkillMatrixUtils | assessmentTimeInterval {}", assessmentTimeInterval);

		session.createNativeQuery(
				"	update sm_ojt_assessment set total_assessed_time=:totalTime  where skilling_audit_id=:skillingAuditId ")
				.setParameter("skillingAuditId", skillingAuditId).setParameter("totalTime", assessmentTimeInterval)
				.executeUpdate();

		flag = true;
		return flag;

	}

	public HashMap<String, Object> getskillMatrixDetails(Session session, int branchId, int deptId, long lineId) {
		Tuple obj = session.createNativeQuery("select mo.name as orgName,mb.name as branchName,md.dept_name as deptName,l.name as lineName\r\n"
				+ "from master_organization mo inner join master_branch mb on mb.org_id=mo.org_id \r\n"
				+ "inner join master_department md on md.branch_id=mb.branch_id\r\n"
				+ "inner join dwm_line l on l.dept_id=md.dept_id where mb.branch_id=:branchId and md.dept_id=:deptId"
				+ " and l.id=:lineId limit 1",
				Tuple.class).setParameter("branchId", branchId).setParameter("deptId", deptId)
				.setParameter("lineId", lineId).getResultList().stream()
				.findFirst().orElse(null);
		if(obj!=null) {
			return CommonUtils.parseSingleTupleRecord(obj);
		}
		return null;
	}

	public List<com.greentin.enovation.model.skillMatrix.SMWorkstationMapping> findLinkedWorkstations(Session session, long workstationId) {
		LOGGER.info("#In SkillMatrixUtils | Finding linked workstations for workstation: {}", workstationId);
		List<SMWorkstationMapping> linkedWorkstations = session.createNativeQuery(
						"SELECT * FROM sm_workstation_mapping WHERE source_workstation_id = :workstationId AND is_active = 1",
						SMWorkstationMapping.class)
				.setParameter("workstationId", workstationId)
				.getResultList();
		return linkedWorkstations;
	}
}