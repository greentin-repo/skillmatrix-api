package com.greentin.enovation.skillmatrix;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.stream.Collectors;

import javax.persistence.Query;
import javax.persistence.Tuple;
import javax.persistence.TypedQuery;
import javax.transaction.Transactional;

import org.apache.commons.lang3.tuple.Pair;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.usermodel.FormulaEvaluator;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.hibernate.Session;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.springframework.web.multipart.MultipartFile;

import com.greentin.enovation.audit.SkillMatrixAudit;
import com.greentin.enovation.config.EnovationConfig;
import com.greentin.enovation.dto.SMAssessmentDTO;
import com.greentin.enovation.dto.SkillMatrixRequest;
import com.greentin.enovation.model.BranchMaster;
import com.greentin.enovation.model.skillMatrix.SMGapReason;
import com.greentin.enovation.model.skillMatrix.SMMasterCertificate;
import com.greentin.enovation.model.skillMatrix.SMModel;
import com.greentin.enovation.model.skillMatrix.SMParameterType;
import com.greentin.enovation.model.skillMatrix.SMShifts;
import com.greentin.enovation.model.skillMatrix.SMSkillLevel;
import com.greentin.enovation.model.skillMatrix.SMStage;
import com.greentin.enovation.model.skillMatrix.SMStageLabel;
import com.greentin.enovation.model.skillMatrix.SMWorkflowConfig;
import com.greentin.enovation.model.DepartmentMaster;
import com.greentin.enovation.model.skillMatrix.SMAssessment;
import com.greentin.enovation.model.skillMatrix.SMAssessmentConfig;
import com.greentin.enovation.model.skillMatrix.SMAssessmentOptions;
import com.greentin.enovation.model.skillMatrix.SMAssessmentQues;
import com.greentin.enovation.model.skillMatrix.SMCategory;
import com.greentin.enovation.model.skillMatrix.SMChecksheet;
import com.greentin.enovation.model.skillMatrix.SMChecksheetParameter;
import com.greentin.enovation.model.skillMatrix.SMChecksheetPoints;
import com.greentin.enovation.model.skillMatrix.SMDocName;
import com.greentin.enovation.model.skillMatrix.SMQuestionType;
import com.greentin.enovation.model.EmployeeDetails;
import com.greentin.enovation.model.dwm.Line;
import com.greentin.enovation.model.skillMatrix.SMMasterUserType;
import com.greentin.enovation.model.skillMatrix.SMUserType;
import com.greentin.enovation.model.skillMatrix.SMWorkstations;
import com.greentin.enovation.response.SkillMatrixResponse;
import com.greentin.enovation.utils.BaseRepository;
import com.greentin.enovation.utils.ColumnAscDescConstants;
import com.greentin.enovation.utils.CommonUtils;
import com.greentin.enovation.utils.EnovationConstants;
import com.greentin.enovation.utils.EnovationException;
import com.greentin.enovation.utils.WriteFilesUtils;

@Component
@Transactional
public class SettingDaoImpl extends BaseRepository implements SettingIDao {
	private static final Logger LOGGER = LoggerFactory.getLogger(SettingDaoImpl.class);

	@Autowired
	SkillMatrixAudit smAudit;

	@Autowired
	SkillMatrixWorker smWorker;

	@Autowired
	SkillMatrixUtils smUtils;

	@Autowired
	ReadWriteFileImpl readWritefile;

	@Override
	public SkillMatrixRequest addAssessment(SkillMatrixRequest request) {
		LOGGER.info("#In SettingDaoImpl |  INSIDE in addAssessment ");
		Session session = getCurrentSession();
		SkillMatrixRequest dto = new SkillMatrixRequest();
		SMAssessment obj = new SMAssessment();
		smWorker.setSMAssessmentParam(request, session, obj);
		if (request.getCreatedBy() > 0) {
			obj.setCreatedBy(request.getCreatedBy());
		}
		obj.setIsActive(true);
		obj.setStatus(SMConstant.ASSESSMENT_CREATED);
		session.save(obj);
		dto.setAssessmentId(obj.getId());
		return dto;

	}

	public List<HashMap<String, Object>> getLevelList() {
		LOGGER.info("# SettingDaoImpl || getLevelList");

		Session session = getCurrentSession();

		StringBuilder sb = new StringBuilder(
				"select sl.id as id, sl.level_name as levelName,sl.created_date as createdDate from sm_skill_level sl ");

		TypedQuery<Tuple> query = session.createNativeQuery(sb.toString(), Tuple.class);

		List<Tuple> tupleList = query.getResultList();

		if (!CollectionUtils.isEmpty(tupleList)) {
			return CommonUtils.parseTupleList(tupleList);
		}
		return null;
	}

	@Override
	public boolean addModelDetails(SkillMatrixRequest request) {
		LOGGER.info("# SettingDaoImpl | addModelDetails ");
		boolean flag = false;
		Session session = getCurrentSession();

		if (request.getBranchId() > 0) {
			if (!isModelExist(request, session)) {
				throw new EnovationException("Model is already exist. ");
			}

			SMModel obj = new SMModel();

			if (request.getModelName() != null) {
				obj.setModelName(request.getModelName());
			}

			obj.setBranch(new BranchMaster(request.getBranchId()));

			if (request.getIsActive() != null) {
				obj.setIsActive(request.getIsActive());
			}
			if (request.getCreatedBy() > 0) {
				obj.setCreatedBy(request.getCreatedBy());
			}
			session.save(obj);
			flag = true;
		} else {
			throw new EnovationException("Branch ID is mandatory. ");
		}
		return flag;
	}

	private boolean isModelExist(SkillMatrixRequest request, Session session) {
		LOGGER.info("# SkillMatrixUtils | isModelExist");
		StringBuilder sb = new StringBuilder(
				"select ifnull((select coalesce(count(id),0) from sm_model where is_active=1 and branch_id=:branchId "
						+ " and model_name=:model  ");

		if (request.getModelId() > 0) {
			LOGGER.info("# id{},", request.getModelId());
			sb.append(" and id !=:id ");
		}
		sb.append(" ),0) as workFlowCnt  ");
		TypedQuery<Tuple> query = session.createNativeQuery(sb.toString());

		if (request.getModelId() > 0) {
			query.setParameter("id", request.getModelId());
		}

		return ((Number) query.setParameter("branchId", request.getBranchId())
				.setParameter("model", request.getModelName()).getSingleResult()).intValue() == 0;
	}

	@Override
	public boolean updateModelDetails(SkillMatrixRequest request) {
		LOGGER.info("# SettingDaoImpl | updateModelDetails ");
		boolean flag = false;
		Session session = getCurrentSession();

		if (request.getModelId() > 0) {
			if (!isModelExist(request, session)) {
				throw new EnovationException("Model is already exist. ");
			}

			SMModel obj = session.get(SMModel.class, request.getModelId());

			Tuple tupleObj = session.createNativeQuery(
					"select smm.id as id,smm.model_name as modelName,smm.branch_id as branchId from sm_model smm where smm.id=:modelId ",
					Tuple.class).setParameter("modelId", request.getModelId()).getResultList().stream().findFirst()
					.orElse(null);

			if (request.getModelName() != null) {
				obj.setModelName(request.getModelName());
			}
			if (request.getBranchId() > 0) {
				obj.setBranch(new BranchMaster(request.getBranchId()));
			}
			if (request.getUpdatedBy() > 0) {
				obj.setUpdatedBy(request.getUpdatedBy());
			}
			session.update(obj);
			if (tupleObj != null) {
				smAudit.updateModelDetailsAudit(session, tupleObj, request);
			}
			flag = true;
		} else {
			throw new EnovationException("Model ID is mandatory. ");
		}
		return flag;
	}

	@Override
	public boolean deleteModelDetails(SkillMatrixRequest request) {
		LOGGER.info("# SettingDaoImpl || deleteModelDetails");
		boolean flag = false;
		boolean result = false;
		Session session = getCurrentSession();

		if (request.getModelId() > 0) {
			SMModel obj = session.get(SMModel.class, request.getModelId());

			result = session.createNativeQuery("delete from sm_model where id =:modelId")
					.setParameter("modelId", request.getModelId()).executeUpdate() != 0 ? true : false;
			if (result) {
				smAudit.deleteModelDetailsAudit(session, obj, request);
			}
			flag = true;
		} else {
			throw new EnovationException("Model ID is mandatory. ");
		}
		return flag;
	}

	@Override
	public List<HashMap<String, Object>> getModelList(SkillMatrixRequest request, SkillMatrixResponse response) {
		LOGGER.info("# SettingDaoImpl || getModelList");
		Session session = getCurrentSession();

		if (request.getOffset() == 0) {
			response.setTotalCount(getModelCount(session, request));
		}

		StringBuilder sb = new StringBuilder(
				" select smm.id as id,smm.model_name as modelName,smm.branch_id as branchId,mb.name as branchName,smm.created_date as createdDate \r\n"
						+ " from sm_model smm \r\n" + " left join master_branch mb on mb.branch_id=smm.branch_id\r\n"
						+ " where smm.branch_id=:branchId ");

		if (request.getSearch() != null) {
			sb.append(" and (smm.model_name like '%{search}%' or mb.name like '%{search}%' )");
		}
		if (request.getColName() != null && request.getOrderType() != null) {
			sb.append(" order by " + ColumnAscDescConstants.smModelColName.get(request.getColName()) + " "
					+ request.getOrderType());
		} else {
			sb.append(" order by smm.id DESC ");
		}

		String tmpQuery = sb.toString().replace("{search}", String.valueOf(request.getSearch()));

		TypedQuery<Tuple> query = session.createNativeQuery(tmpQuery, Tuple.class).setParameter("branchId",
				request.getBranchId());

		if (request.getLimit() > 0) {
			query.setFirstResult(request.getOffset()).setMaxResults(request.getLimit());
		}

		List<Tuple> tupleList = query.getResultList();

		if (!CollectionUtils.isEmpty(tupleList)) {
			return CommonUtils.parseTupleList(tupleList);
		}
		return null;
	}

	private int getModelCount(Session session, SkillMatrixRequest request) {
		LOGGER.info("# SettingDaoImpl || getModelCount");
		StringBuilder sb = new StringBuilder(" select COUNT(smm.id) as totalCount from sm_model smm "
				+ " left join master_branch mb on mb.branch_id=smm.branch_id  where smm.branch_id=:branchId ");

		if (request.getSearch() != null) {
			sb.append(" and (smm.model_name like '%{search}%' or mb.name like '%{search}%' )");
		}

		String tmpQuery = sb.toString().replace("{search}", String.valueOf(request.getSearch()));

		TypedQuery<Tuple> query = session.createNativeQuery(tmpQuery, Tuple.class).setParameter("branchId",
				request.getBranchId());

		List<Tuple> objList = query.getResultList();
		if (!CollectionUtils.isEmpty(objList)) {
			return CommonUtils.objectToInt(objList.get(0).get("totalCount"));
		}
		return 0;
	}

	@Override
	public boolean saveGapReason(SkillMatrixRequest request) {
		LOGGER.info("# SettingDaoImpl || saveGapReason");
		boolean flag = false;
		Session session = getCurrentSession();

		if (request.getBranchId() > 0) {

			if (smUtils.isReasonExist(session, request)) {
				SMGapReason obj = new SMGapReason();
				obj.setReason(request.getReason());

				obj.setBranch(new BranchMaster(request.getBranchId()));

				if (request.getIsActive() != null) {
					obj.setIsActive(request.getIsActive());
				}
				if (request.getCreatedBy() > 0) {
					obj.setCreatedBy(request.getCreatedBy());
				}
				session.save(obj);
				flag = true;
			} else {
				throw new EnovationException("Gap Reason is already exist. ");
			}
		} else {
			throw new EnovationException("Branch ID is mandatory. ");
		}
		return flag;
	}

	@Override
	public boolean updateGapReason(SkillMatrixRequest request) {
		LOGGER.info("# SettingDaoImpl || updateGapReason");
		boolean flag = false;
		Session session = getCurrentSession();

		if (request.getReasonId() > 0) {
			SMGapReason obj = session.get(SMGapReason.class, request.getReasonId());

			Tuple tupleObj = session
					.createNativeQuery("select gr.id as id,gr.reason as reason,gr.branch_id as branchId\r\n"
							+ "from sm_gap_reason gr where gr.id=:reasonId ", Tuple.class)
					.setParameter("reasonId", request.getReasonId()).getResultList().stream().findFirst().orElse(null);

			if (request.getReason() != null) {
				if (smUtils.isReasonExist(session, request)) {
					obj.setReason(request.getReason());
				} else {
					throw new EnovationException("Gap Reason is already exist. ");
				}
			}
			if (request.getBranchId() > 0) {
				obj.setBranch(new BranchMaster(request.getBranchId()));
			}
			if (request.getUpdatedBy() > 0) {
				obj.setUpdatedBy(request.getUpdatedBy());
			}
			session.update(obj);
			if (tupleObj != null) {
				smAudit.updateGapReasonAudit(session, tupleObj, request);
			}
			flag = true;
		} else {
			throw new EnovationException("Reason ID is mandatory. ");
		}
		return flag;
	}

	@Override
	public boolean deleteGapReason(SkillMatrixRequest request) {
		LOGGER.info("# SettingDaoImpl || deleteGapReason");
		boolean flag = false;
		boolean result = false;
		Session session = getCurrentSession();

		if (request.getReasonId() > 0) {
			SMGapReason obj = session.get(SMGapReason.class, request.getReasonId());

			result = session.createNativeQuery("delete from sm_gap_reason where id =:reasonId")
					.setParameter("reasonId", request.getReasonId()).executeUpdate() != 0 ? true : false;
			if (result) {
				smAudit.deleteGapReasonAudit(session, obj, request);
			}
			flag = true;
		} else {
			throw new EnovationException("Reason ID is mandatory. ");
		}
		return flag;
	}

	@Override
	public List<HashMap<String, Object>> getGapReasonList(SkillMatrixRequest request, SkillMatrixResponse response) {
		LOGGER.info("# SettingDaoImpl || getGapReasonList");
		Session session = getCurrentSession();

		if (request.getOffset() == 0) {
			response.setTotalCount(getGapReasonCount(session, request));
		}

		StringBuilder sb = new StringBuilder(
				" select gr.id as id,gr.reason as reason,gr.branch_id as branchId,mb.name as branchName,gr.created_date as createdDate \r\n"
						+ " from sm_gap_reason gr left join master_branch mb on mb.branch_id=gr.branch_id\r\n"
						+ " where gr.branch_id=:branchId ");
		if (request.getSearch() != null) {
			sb.append(" and (gr.reason like '%{search}%' or mb.name like '%{search}%' )");
		}
		if (request.getColName() != null && request.getOrderType() != null) {
			sb.append(" order by " + ColumnAscDescConstants.smGapReasonColName.get(request.getColName()) + " "
					+ request.getOrderType());
		} else {
			sb.append(" order by gr.id DESC ");
		}

		String tmpQuery = sb.toString().replace("{search}", (String.valueOf(request.getSearch())));

		TypedQuery<Tuple> query = session.createNativeQuery(tmpQuery, Tuple.class).setParameter("branchId",
				request.getBranchId());

		if (request.getLimit() > 0) {
			query.setFirstResult(request.getOffset()).setMaxResults(request.getLimit());
		}

		List<Tuple> tupleList = query.getResultList();

		if (!CollectionUtils.isEmpty(tupleList)) {
			return CommonUtils.parseTupleList(tupleList);
		}
		return null;
	}

	private int getGapReasonCount(Session session, SkillMatrixRequest request) {
		LOGGER.info("# SettingDaoImpl || getGapReasonCount");
		StringBuilder sb = new StringBuilder(" select count(gr.id) as totalCount from sm_gap_reason gr "
				+ " left join master_branch mb on mb.branch_id=gr.branch_id where gr.branch_id=:branchId ");

		if (request.getSearch() != null) {
			sb.append(" and (gr.reason like '%{search}%' or mb.name like '%{search}%' )");
		}

		String tmpQuery = sb.toString().replace("{search}", (String.valueOf(request.getSearch())));

		TypedQuery<Tuple> query = session.createNativeQuery(tmpQuery, Tuple.class).setParameter("branchId",
				request.getBranchId());

		List<Tuple> objList = query.getResultList();
		if (!CollectionUtils.isEmpty(objList)) {
			return CommonUtils.objectToInt(objList.get(0).get("totalCount"));
		}
		return 0;
	}

	@Override
	public boolean saveShiftDetails(SkillMatrixRequest request) {
		LOGGER.info("# SettingDaoImpl || saveGapReason");
		boolean flag = false;
		Session session = getCurrentSession();

		if (request.getBranchId() > 0) {

			if (smUtils.isShiftExist(session, request)) {

				SMShifts obj = new SMShifts();

				obj.setShiftName(request.getShiftName());

				obj.setBranch(new BranchMaster(request.getBranchId()));

				if (request.getIsActive() != null) {
					obj.setIsActive(request.getIsActive());
				}
				if (request.getCreatedBy() > 0) {
					obj.setCreatedBy(request.getCreatedBy());
				}
				session.save(obj);
				flag = true;
			} else {
				throw new EnovationException("Shift is already exist. ");
			}

		} else {
			throw new EnovationException("Branch ID is mandatory. ");
		}
		return flag;
	}

	@Override
	public boolean updateShiftDetails(SkillMatrixRequest request) {
		LOGGER.info("# SettingDaoImpl || updateShiftDetails");
		boolean flag = false;
		Session session = getCurrentSession();

		if (request.getShiftId() > 0) {
			SMShifts obj = session.get(SMShifts.class, request.getShiftId());

			Tuple tupleObj = session
					.createNativeQuery("select sms.id as id,sms.shift_name as shiftName,sms.branch_id as branchId\r\n"
							+ "from sm_shifts sms where sms.id=:shiftId", Tuple.class)
					.setParameter("shiftId", request.getShiftId()).getResultList().stream().findFirst().orElse(null);

			if (request.getShiftName() != null) {
				if (smUtils.isShiftExist(session, request)) {
					obj.setShiftName(request.getShiftName());
				} else {
					throw new EnovationException("Shift is already exist. ");
				}
			}
			if (request.getBranchId() > 0) {
				obj.setBranch(new BranchMaster(request.getBranchId()));
			}
			if (request.getUpdatedBy() > 0) {
				obj.setUpdatedBy(request.getUpdatedBy());
			}
			session.update(obj);
			if (tupleObj != null) {
				smAudit.updateShiftDetailsAudit(session, tupleObj, request);
			}
			flag = true;
		} else

		{
			throw new EnovationException("Shift ID is mandatory. ");
		}
		return flag;
	}

	@Override
	public boolean deleteShiftDetails(SkillMatrixRequest request) {
		LOGGER.info("# SettingDaoImpl || deleteShiftDetails");
		boolean flag = false;
		boolean result = false;
		Session session = getCurrentSession();

		if (request.getShiftId() > 0) {
			SMShifts obj = session.get(SMShifts.class, request.getShiftId());

			result = session.createNativeQuery("delete from sm_shifts where id =:shiftId")
					.setParameter("shiftId", request.getShiftId()).executeUpdate() != 0 ? true : false;

			if (result) {
				smAudit.deleteShiftDetailsAudit(session, obj, request);
			}
			flag = true;
		} else {
			throw new EnovationException("Shift ID is mandatory. ");
		}
		return flag;
	}

	@Override
	public List<HashMap<String, Object>> getShiftList(SkillMatrixRequest request, SkillMatrixResponse response) {
		LOGGER.info("# SettingDaoImpl || getShiftList");
		Session session = getCurrentSession();

		if (request.getOffset() == 0) {
			response.setTotalCount(getShiftCount(session, request));
		}

		StringBuilder sb = new StringBuilder(
				" select sms.id as id,sms.shift_name as shiftName,sms.branch_id as branchId,mb.name as branchName,\r\n"
						+ "sms.created_date as createdDate,sms.is_active as isActive  from sm_shifts sms \r\n"
						+ "left join master_branch mb on mb.branch_id=sms.branch_id  where sms.branch_id=:branchId ");

		if (request.getSearch() != null) {
			sb.append(" and (sms.shift_name like '%{search}%' or mb.name like '%{search}%' )");
		}

		if (request.getColName() != null && request.getOrderType() != null) {
			sb.append(" order by " + ColumnAscDescConstants.smShiftColName.get(request.getColName()) + " "
					+ request.getOrderType());
		} else {
			sb.append(" order by sms.id DESC ");
		}

		String tmpQuery = sb.toString().replace("{search}", (String.valueOf(request.getSearch())));

		TypedQuery<Tuple> query = session.createNativeQuery(tmpQuery, Tuple.class).setParameter("branchId",
				request.getBranchId());

		if (request.getLimit() > 0) {
			query.setFirstResult(request.getOffset()).setMaxResults(request.getLimit());
		}

		List<Tuple> tupleList = query.getResultList();

		if (!CollectionUtils.isEmpty(tupleList)) {
			return CommonUtils.parseTupleList(tupleList);
		}
		return null;
	}

	private int getShiftCount(Session session, SkillMatrixRequest request) {
		LOGGER.info("# SettingDaoImpl || getShiftCount");

		StringBuilder sb = new StringBuilder(" select COUNT(sms.id) AS totalCount  from sm_shifts sms \r\n"
				+ "left join master_branch mb on mb.branch_id=sms.branch_id  where sms.branch_id=:branchId ");

		if (request.getSearch() != null) {
			sb.append(" and (sms.shift_name like '%{search}%' or mb.name like '%{search}%' )");
		}

		String tmpQuery = sb.toString().replace("{search}", (String.valueOf(request.getSearch())));

		TypedQuery<Tuple> query = session.createNativeQuery(tmpQuery, Tuple.class).setParameter("branchId",
				request.getBranchId());

		List<Tuple> objList = query.getResultList();
		if (!CollectionUtils.isEmpty(objList)) {
			return CommonUtils.objectToInt(objList.get(0).get("totalCount"));
		}
		return 0;
	}

	@Override
	public boolean deActiveShiftDetails(SkillMatrixRequest request) {
		LOGGER.info("# SettingDaoImpl || deActiveShiftDetails");
		boolean flag = false;
		boolean result = false;
		Session session = getCurrentSession();
		if (request.getShiftId() > 0) {
			SMShifts obj = session.get(SMShifts.class, request.getShiftId());

			result = session.createQuery(" update sm_shifts set is_active=:isActive where id.id=:id ")
					.setParameter("isActive", EnovationConstants.DE_ACTIVE).setParameter("id", request.getShiftId())
					.executeUpdate() != 0 ? true : false;

			if (result) {
				smAudit.deActiveShiftDetailsAudit(session, obj, request);
			}
			flag = true;
		} else {
			throw new EnovationException("Shift ID is mandatory. ");
		}
		return flag;
	}

	@Override
	public List<HashMap<String, Object>> getStageList() {
		LOGGER.info("# SettingDaoImpl || getStageList");
		Session session = getCurrentSession();

		StringBuilder sb = new StringBuilder(
				"select sms.id as id,sms.stage_caption as stageCaption,sms.stage_name as stageName,sms.created_date as createdDate \r\n"
						+ " from sm_stage sms");

		TypedQuery<Tuple> query = session.createNativeQuery(sb.toString(), Tuple.class);

		List<Tuple> tupleList = query.getResultList();

		if (!CollectionUtils.isEmpty(tupleList)) {
			return CommonUtils.parseTupleList(tupleList);
		}
		return null;
	}

	@Override
	public boolean saveWorkflowConfig(SkillMatrixRequest request) {
		LOGGER.info("# SettingDaoImpl || saveWorkflowConfig");
		boolean flag = false;
		Session session = getCurrentSession();
		if (!CollectionUtils.isEmpty(request.getWorkflowConfigList())) {
			for (SkillMatrixRequest listObj : request.getWorkflowConfigList()) {
				if (listObj.getWorkflowId() > 0) {
					SMWorkflowConfig obj = session.get(SMWorkflowConfig.class, listObj.getWorkflowId());
					if (obj != null) {
						insertWorkflowConfig(listObj, request, obj);
						session.save(obj);
						flag = true;
					}
				} else {
					SMWorkflowConfig obj = new SMWorkflowConfig();
					insertWorkflowConfig(listObj, request, obj);
					session.save(obj);
					flag = true;
				}
			}
		}
		return flag;
	}

	private void insertWorkflowConfig(SkillMatrixRequest listObj, SkillMatrixRequest request, SMWorkflowConfig obj) {
		if (request.getBranchId() > 0) {
			obj.setBranch(new BranchMaster(request.getBranchId()));
		}
		if (listObj.getStageId() > 0) {
			obj.setStage(new SMStage(listObj.getStageId()));
		}
		if (listObj.getSkillLevelId() > 0) {
			obj.setSkillLevel(new SMSkillLevel(listObj.getSkillLevelId()));
		}
		if (listObj.getIsActive() != null) {
			obj.setIsActive(listObj.getIsActive());
		}
		if (request.getCreatedBy() > 0) {
			obj.setCreatedBy(request.getCreatedBy());
		}
		if (request.getUpdatedBy() > 0) {
			obj.setCreatedBy(request.getUpdatedBy());
		}
	}

	@Override
	public boolean deleteWorkflowConfig(SkillMatrixRequest request) {
		LOGGER.info("# SettingDaoImpl || deleteShiftDetails");
		boolean flag = false;
		boolean result = false;
		Session session = getCurrentSession();

		if (request.getWorkflowId() > 0) {
			SMWorkflowConfig obj = session.get(SMWorkflowConfig.class, request.getWorkflowId());

			result = session.createNativeQuery("delete from sm_workflow_config where id =:workflowId")
					.setParameter("workflowId", request.getWorkflowId()).executeUpdate() != 0 ? true : false;

			if (result) {
				smAudit.deleteWorkflowConfigAudit(session, obj, request);
			}
			flag = true;
		} else {
			throw new EnovationException("workflow ID is mandatory. ");
		}
		return flag;
	}

	@Override
	public boolean deActiveWorkflowConfig(SkillMatrixRequest request) {
		LOGGER.info("# SettingDaoImpl || deActiveWorkflowConfig");
		boolean flag = false;
		boolean result = false;
		Session session = getCurrentSession();
		if (request.getWorkflowId() > 0) {
			SMWorkflowConfig obj = session.get(SMWorkflowConfig.class, request.getWorkflowId());

			result = session.createQuery(" update sm_workflow_config set is_active=:isActive where id.id=:workflowId ")
					.setParameter("isActive", EnovationConstants.DE_ACTIVE)
					.setParameter("workflowId", request.getWorkflowId()).executeUpdate() != 0 ? true : false;

			if (result) {
				smAudit.deActiveWorkflowConfigAudit(session, obj, request);
			}
			flag = true;
		} else {
			throw new EnovationException("workflow ID is mandatory. ");
		}
		return flag;
	}

	@Override
	public List<HashMap<String, Object>> getWorkflowConfigList(int branchId, long levelId) {
		LOGGER.info("# SettingDaoImpl || getWorkflowConfigList");
		Session session = getCurrentSession();

		StringBuilder sb = new StringBuilder(
				" select wfc.id as id,wfc.is_active as isActive,wfc.branch_id as branchId,mb.name as branchName,sms.id as stageId,\r\n"
						+ " sms.stage_name as stageName,wfc.skill_level_id as skillLevelId,sl.level_name as levelName,sml.id as labelId,\r\n"
						+ " (case when sml.stage_label is null then sms.stage_name else sml.stage_label end ) as stageLabelName,\r\n"
						+ " sms.stage_caption as stageCaption from  sm_stage sms \r\n"
						+ " left join sm_workflow_config wfc on sms.id=wfc.stage_id AND wfc.branch_id=:branchId and wfc.skill_level_id=:skillLevelId \r\n"
						+ " left join sm_stage_label sml on sml.stage_id=sms.id  AND sml.branch_id=:branchId \r\n"
						+ " left join master_branch mb on mb.branch_id=wfc.branch_id\r\n"
						+ " left join sm_skill_level sl on sl.id =wfc.skill_level_id");

		TypedQuery<Tuple> query = session.createNativeQuery(sb.toString(), Tuple.class)
				.setParameter("branchId", branchId).setParameter("skillLevelId", levelId);

		List<Tuple> tupleList = query.getResultList();

		if (!CollectionUtils.isEmpty(tupleList)) {
			return CommonUtils.parseTupleList(tupleList);
		}
		return null;
	}

	@Override
	public SMAssessmentDTO getAssessmentDetail(long assessmentId) {
		LOGGER.info("# SettingDaoImpl || getAssessmentDetail");
		Session session = getCurrentSession();
//		HashMap<String, Object> list = new HashMap<>();
		SMAssessmentDTO list = new SMAssessmentDTO();
		TypedQuery<Tuple> query = smUtils.getAssessmentDetails(assessmentId, session);
		List<Tuple> tupleList = query.getResultList();

		List<SkillMatrixRequest> dtoList = new ArrayList<>();
		smUtils.setDtoParameters(tupleList, dtoList);

		LOGGER.info("# tupleList size :{}", tupleList.size());
//		Map<Long, List<SkillMatrixRequest>> groupedByQuetionId = dtoList.stream()
//				.collect(Collectors.groupingBy(SkillMatrixRequest::getAssessmentQueId));

		Map<Long, List<SkillMatrixRequest>> groupedByQuetionId = dtoList.stream()
				.collect(Collectors.groupingBy(SkillMatrixRequest::getAssessmentQueId, LinkedHashMap::new, // Ensures
																											// insertion
																											// order
						Collectors.toList()));

		smWorker.groupingQueOptList(list, groupedByQuetionId, session);
		return list;

	}

	@Override
	public boolean addAssessmentQuestion(SkillMatrixRequest request) {
		LOGGER.info("# SettingDaoImpl || addAssessmentQuestion");
		boolean flag = false;
		Session session = getCurrentSession();
		SMAssessmentQues obj = new SMAssessmentQues();
		if (request.getAssessmentId() > 0) {
			obj.setAssessment(new SMAssessment(request.getAssessmentId()));
		}
		if (request.getQuestionTypeId() > 0) {
			obj.setQuesType(new SMQuestionType(request.getQuestionTypeId()));
		}
		if (request.getCategoryId() > 0) {
			obj.setSmcategory(new SMCategory(request.getCategoryId()));
		}
		if (request.getQuestion() != null) {
			if (smUtils.isQueNotExist(session, request)) {
				obj.setQestion(request.getQuestion());
			} else {
				throw new EnovationException("Quetion is already exist");
			}
		}
		obj.setQueMarks(request.getQueMark());
		obj.setIsActive(true);
		session.save(obj);
		if (!CollectionUtils.isEmpty(request.getOptList())) {
			request.getOptList().stream().forEach(x -> {
				SMAssessmentOptions opt = new SMAssessmentOptions();
				opt.setOption(x.getOption());
				opt.setIsRightAns(x.getIsRightAns());
				opt.setQues(obj);
				session.save(opt);
			});
		}

		flag = true;
		smUtils.calculateTotalMark(session, request.getAssessmentId());
		return flag;

	}

	// Added by Sonali L. Jan 15 2024 - added field assessmentType
	@Override
	public List<HashMap<String, Object>> getAssessmentList(SkillMatrixRequest request, SkillMatrixResponse response) {
		LOGGER.info("# SettingDaoImpl || getAssessmentList");
		Session session = getCurrentSession();
		List<HashMap<String, Object>> list = new ArrayList<>();
		if (request.getOffset() == 0) {
			response.setTotalCount(getAssessmentListCount(request, session));
		}

		StringBuilder sb = new StringBuilder(
				"select sa.id as assessmentId, sa.title as title, sa.skill_level_id as skillLevelId , sa.branch_id as branchId, sa.time as time , sa.passing_marks as passingMarks,"
						+ " sa.created_date as createdDate, sa.created_by as createdBy ,concat(ifnull(e.first_name,''),' ',ifnull(e.last_name,'')) as createdByName ,"
						+ " b.name as branchName,sl.level_name as skillLevel ,sa.status as status, sa.total_marks as totalMark,"
						+ " l.name as lineName,l.id as lineId,\r\n"
						+ " d.dept_id as deptId,d.dept_name as deptName,sw.id as workstationId , sw.workstation as workstation, \r\n"
						+ " sa.assessment_type as assessmentType "
						+ " from  sm_assessment sa left join master_branch b on b.branch_id=sa.branch_id left join sm_skill_level sl on sl.id=sa.skill_level_id \r\n"
						+ " left join master_organization o on o.org_id=b.org_id"
						+ " left join tbl_employee_details e on e.emp_id=sa.created_by"
						+ " left join master_department d on d.dept_id=sa.dept_id\r\n"
						+ " left join dwm_line l on l.id=sa.line_id\r\n"
						+ " left join sm_workstations sw on sw.id = sa.workstation_id "
						+ " where sa.is_active=1 and o.org_id=:orgId ");

		if (request.getBranchId() > 0) {
			sb.append(" and  sa.branch_id=:branchId ");
		}
		if (request.getSkillLvlId() > 0) {
			sb.append(" and  sa.skill_level_id=:skillLvlId ");
		}

		if (request.getDeptId() > 0) {
			sb.append(" and sa.dept_id in (:deptId) ");
		}
		if (!CollectionUtils.isEmpty(request.getLineIds())) {
			sb.append(" and sa.line_id in (:lineIds) ");
		}
		if (!CollectionUtils.isEmpty(request.getWorkstationIds())) {
			sb.append(" and sa.workstation_id in (:workstationIds) ");
		}

		if (request.getSearch() != null) {
			sb.append(
					" and (sa.title like '%{search}%' or sl.level_name like '%{search}%' or sw.workstation like '%{search}%'  or l.name like '%{search}%' or sa.assessment_type like '%{search}%')");
		}

		if (request.getColName() != null && request.getOrderType() != null) {
			sb.append(" order by " + ColumnAscDescConstants.smAssessmentColName.get(request.getColName()) + " "
					+ request.getOrderType());
		} else {
			sb.append(" order by sa.id DESC ");
		}

		String tmpQuery = sb.toString().replace("{search}", (String.valueOf(request.getSearch())));

		TypedQuery<Tuple> query = session.createNativeQuery(tmpQuery, Tuple.class).setParameter("orgId",
				request.getOrgId());

		if (request.getBranchId() > 0) {
			query.setParameter("branchId", request.getBranchId());
		}
		if (request.getSkillLvlId() > 0) {
			query.setParameter("skillLvlId", request.getSkillLvlId());
		}
		if (request.getDeptId() > 0) {
			query.setParameter("deptId", request.getDeptId());
		}
		if (!CollectionUtils.isEmpty(request.getLineIds())) {
			query.setParameter("lineIds", request.getLineIds());
		}
		if (!CollectionUtils.isEmpty(request.getWorkstationIds())) {
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
		return list;
	}

	private int getAssessmentListCount(SkillMatrixRequest request, Session session) {
		StringBuilder sb = new StringBuilder(" select count(sa.id) as totalCount from  sm_assessment sa \r\n"
				+ " left join master_branch b on b.branch_id=sa.branch_id \r\n"
				+ "  left join sm_skill_level sl on sl.id=sa.skill_level_id \r\n"
				+ "	 inner join master_organization o on o.org_id=b.org_id\r\n"
				+ "	 left join tbl_employee_details e on e.emp_id=sa.created_by\r\n"
				+ "	 left join dwm_line l on l.id=sa.line_id\r\n"
				+ "	 left join sm_workstations sw on sw.id = sa.workstation_id \r\n"
				+ "	 where sa.is_active=1 and o.org_id=:orgId ");

		if (request.getBranchId() > 0) {
			sb.append(" and  sa.branch_id=:branchId ");
		}
		if (request.getSkillLvlId() > 0) {
			sb.append(" and  sa.skill_level_id=:skillLvlId ");
		}
		if (request.getDeptId() > 0) {
			sb.append(" and sa.dept_id in (:deptId) ");
		}
		if (!CollectionUtils.isEmpty(request.getLineIds())) {
			sb.append(" and sa.line_id in (:lineIds) ");
		}
		if (!CollectionUtils.isEmpty(request.getWorkstationIds())) {
			sb.append(" and sa.workstation_id in (:workstationIds) ");
		}
		if (request.getSearch() != null) {
			sb.append(
					" and (sa.title like '%{search}%' or sl.level_name like '%{search}%' or sw.workstation like '%{search}%'  or l.name like '%{search}%' or sa.assessment_type like '%{search}%' )");
		}

		String tmpQuery = sb.toString().replace("{search}", (String.valueOf(request.getSearch())));

		TypedQuery<Tuple> query = session.createNativeQuery(tmpQuery, Tuple.class).setParameter("orgId",
				request.getOrgId());

		if (request.getBranchId() > 0) {
			query.setParameter("branchId", request.getBranchId());
		}
		if (request.getSkillLvlId() > 0) {
			query.setParameter("skillLvlId", request.getSkillLvlId());
		}
		if (request.getDeptId() > 0) {
			query.setParameter("deptId", request.getDeptId());
		}
		if (!CollectionUtils.isEmpty(request.getLineIds())) {
			query.setParameter("lineIds", request.getLineIds());
		}
		if (!CollectionUtils.isEmpty(request.getWorkstationIds())) {
			query.setParameter("workstationIds", request.getWorkstationIds());
		}
		List<Tuple> tupleList = query.getResultList();

		if (!CollectionUtils.isEmpty(tupleList)) {
			return CommonUtils.objectToInt(tupleList.get(0).get("totalCount"));
		}
		return 0;
	}

	@Override
	public boolean updateAssessment(SkillMatrixRequest request) {
		LOGGER.info("#In SettingDaoImpl |  INSIDE in updateAssessment ");
		Session session = getCurrentSession();

		Tuple tupleList = smUtils.getAssesmentDetails(request, session);

		SMAssessment obj = session.load(SMAssessment.class, request.getAssessmentId());
		if (obj != null) {
			smWorker.setSMAssessmentParam(request, session, obj);
			if (request.getUpdatedBy() > 0) {
				obj.setUpdatedBy(request.getUpdatedBy());
			}
			session.update(obj);
			if (tupleList != null) {
				smAudit.updateSMAssessmentAudit(session, obj, tupleList);
			}
			return true;
		}
		return false;
	}

	@Override
	public boolean updateAssessmentQuestion(SkillMatrixRequest request) {
		LOGGER.info("#In SettingDaoImpl |  INSIDE in updateAssessmentQuestion ");
		boolean flag = false;
		Session session = getCurrentSession();
		SMAssessmentQues obj = session.get(SMAssessmentQues.class, request.getAssessmentQueId());
		if (obj != null) {
			Tuple que = smUtils.getQuestionDetails(request, session);
			smWorker.updateAssessmentQue(request, session, obj);
			smUtils.calculateTotalMark(session, request.getAssessmentId());
			if (que != null) {
				smAudit.updateSMAssessmentQuesAudit(session, obj, que, EnovationConstants.UPDATE_RECORD, request);
			}
			flag = true;
		}
		return flag;
	}

	@Override
	public boolean saveWorkstation(SkillMatrixRequest request) {
		LOGGER.info("# SettingDaoImpl || saveWorkstation");
		Session session = getCurrentSession();
		boolean flag = false;

		if (smUtils.checkWorkstationAlreadyExit(request, session)) {
			throw new EnovationException("Workstation already exists");
		}

		SMWorkstations work = new SMWorkstations();
		work.setWorkstation(request.getWorkstation());
		work.setIsActive(request.getIsActive());

		if (request.getMachineIndex() > 0) {
			work.setMachineIndex(request.getMachineIndex());
		}

		if (request.getRequiredWorkforce() > 0) {
			work.setRequiredWorkforce(request.getRequiredWorkforce());
		}
		if (request.getCreatedBy() > 0) {
			work.setCreatedBy(request.getCreatedBy());
		}
		if (request.getBranchId() <= 0) {
			throw new EnovationException("Branch ID is required");
		}
		work.setBranch(new BranchMaster(request.getBranchId()));

		if (request.getDeptId() <= 0) {
			throw new EnovationException("Department ID is required");
		}
		work.setDept(new DepartmentMaster(request.getDeptId()));

		if (request.getLineId() <= 0) {
			throw new EnovationException("Line ID is required");
		}
		work.setLine(new Line(request.getLineId()));

		if (request.getReqSkillLevelId() > 0) {
			work.setReqSkillLevel(new SMSkillLevel(request.getReqSkillLevelId()));
		}

		if (request.getMachineCount() > 0) {
			work.setMachineCount(request.getMachineCount());
		}

		session.save(work);
		flag = true;

		return flag;
	}

	@Override
	public boolean updateWorkstation(SkillMatrixRequest request) {
		LOGGER.info("# SettingDaoImpl || updateWorkstation");
		Session session = getCurrentSession();

		Tuple tupleObj = smUtils.getWorkstationDetails(session, request);

		SMWorkstations work = session.get(SMWorkstations.class, request.getId());

		if (work != null) {
			if (request.getWorkstation() != null) {
				if (smUtils.checkWorkstationAlreadyExitId(request, session)) {
					work.setWorkstation(request.getWorkstation());
				} else {
					throw new EnovationException("workstation is already exist");
				}
			}

			if (request.getMachineIndex() > 0) {
				work.setMachineIndex(request.getMachineIndex());
			}

			if (request.getRequiredWorkforce() > 0) {
				work.setRequiredWorkforce(request.getRequiredWorkforce());
			}
			if (request.getUpdatedBy() > 0) {
				work.setUpdatedBy(request.getUpdatedBy());
			}
			if (request.getIsActive() != null) {
				work.setIsActive(request.getIsActive());
			}
			if (request.getBranchId() <= 0) {
				throw new EnovationException("Branch ID is required");
			}
			work.setBranch(new BranchMaster(request.getBranchId()));

			if (request.getDeptId() <= 0) {
				throw new EnovationException("Department ID is required");
			}
			work.setDept(new DepartmentMaster(request.getDeptId()));

			if (request.getLineId() <= 0) {
				throw new EnovationException("Line ID is required");
			}
			work.setLine(new Line(request.getLineId()));

			if (request.getReqSkillLevelId() > 0) {
				work.setReqSkillLevel(new SMSkillLevel(request.getReqSkillLevelId()));
			}

			if (request.getMachineCount() > 0) {
				work.setMachineCount(request.getMachineCount());
			}
			session.update(work);

			if (tupleObj != null) {
				smAudit.masterWorkstation(session, request, tupleObj);
				return true;
			}
		}
		return false;
	}

	@Override
	public boolean deleteWorkstation(SkillMatrixRequest request) {
		LOGGER.info("# SettingDaoImpl | deleteWorkstation");
		Session session = getCurrentSession();
		boolean flag = false;

		Tuple obj = smUtils.getWorkstationDetails(request, session);

		session.createNativeQuery("delete from sm_workstations where id=:id").setParameter("id", request.getId())
				.executeUpdate();
		if (obj != null) {
			smAudit.deleteWorkstation(session, obj, request);
			flag = true;
		}
		return flag;

	}

	@Override
	public boolean deleteAssessmentQuestion(SkillMatrixRequest request) {
		LOGGER.info("#In SettingDaoImpl |  INSIDE in deleteAssessmentQuestion ");
		Session session = getCurrentSession();
		Tuple que = smUtils.getQuestionDetails(request, session);

		session.createNativeQuery("delete from sm_assessment_ques where id=:id")
				.setParameter("id", request.getAssessmentQueId()).executeUpdate();

		smUtils.calculateTotalMark(session, request.getAssessmentId());

		smAudit.deleteSMAssessmentQuesAudit(session, que, EnovationConstants.DELETE_RECORD, request);
		return true;
	}

	public boolean deActiveQuetion(SkillMatrixRequest request) {
		LOGGER.info("#In SettingDaoImpl |  INSIDE in deActiveQuetion ");
		int count;
		Session session = getCurrentSession();
		count = session.createNativeQuery(" update sm_assessment_ques set is_active =:isActive where id=:id ")
				.setParameter("isActive", false).setParameter("id", request.getAssessmentQueId()).executeUpdate();
		smUtils.calculateTotalMark(session, request.getAssessmentId());
		if (count > 0) {
			smAudit.deActiveSMAssessmentQuesAudit(session, request.getAssessmentQueId(),
					EnovationConstants.DEACTIVE_STRING, request);
		}
		return true;
	}

	@Override
	public boolean deleteAssessmentOptions(SkillMatrixRequest request) {
		LOGGER.info("#In SettingDaoImpl |  INSIDE in deleteAssessmentOptions ");
		Session session = getCurrentSession();
		Tuple optObj = smUtils.getOptionDetails(session, request.getAssessmentQueOptId());
		session.createNativeQuery("delete from sm_assessment_options where id=:Id")
				.setParameter("Id", request.getAssessmentQueOptId()).executeUpdate();
		smAudit.deleteSMAssessmentOptionsAudit(session, optObj, request);
		return true;
	}

	@Override
	public boolean deActiveAssessment(SkillMatrixRequest request) {
		LOGGER.info("#In SettingDaoImpl |  INSIDE in deActiveAssessment ");
		Session session = getCurrentSession();

		session.createNativeQuery("update sm_assessment set is_active=:isActive where id=:assessmentId ")
				.setParameter("isActive", false).setParameter("assessmentId", request.getAssessmentId())
				.executeUpdate();
		smAudit.deActiveSMAssessmentAudit(session, request);
		return true;

	}

	@Override
	public SkillMatrixRequest addChecksheet(SkillMatrixRequest request) {
		LOGGER.info("# SettingDaoImpl || addChecksheet");
		Session session = getCurrentSession();
		return createChecksheet(request, session);

	}

	private SkillMatrixRequest createChecksheet(SkillMatrixRequest request, Session session) {
		LOGGER.info("# SettingDaoImpl || createChecksheet");
		SMChecksheet obj = new SMChecksheet();
		SkillMatrixRequest dto = new SkillMatrixRequest();
		if (request.getTitle() != null) {
			if (smUtils.isTitleExist(session, request)) {
				obj.setTitle(request.getTitle());
			} else {

				throw new EnovationException(SMConstant.CHECKSHEET_ALREADY_EXIST);
			}
		}
		obj.setBranch(new BranchMaster(request.getBranchId()));
		obj.setCreatedBy(request.getCreatedBy());
		obj.setNoOfDays(request.getNoOfDays());
		obj.setSkillLevel(new SMSkillLevel(request.getSkillLvlId()));
		obj.setIsActive(true);
		obj.setProductionPlan(request.getProductionPlan());
		obj.setCyclePlan(request.getCyclePlan());
		if (request.getDeptId() > 0) {
			obj.setDept(new DepartmentMaster(request.getDeptId()));
		}
		if (request.getLineId() > 0) {
			obj.setLine(new Line(request.getLineId()));
		}
		if (request.getWorkstationId() > 0) {
			obj.setWorkstation(new SMWorkstations(request.getWorkstationId()));
		}
		session.save(obj);
		dto.setCheckSheetId(obj.getId());
		dto.setNoOfDays(obj.getNoOfDays());
		return dto;
	}

	@Override
	public List<HashMap<String, Object>> getChecksheetList(SkillMatrixRequest request, SkillMatrixResponse response) {
		LOGGER.info("# SettingDaoImpl || getChecksheetList");
		Session session = getCurrentSession();
		List<HashMap<String, Object>> list = new ArrayList<>();
		if (request.getOffset() == 0) {
			response.setTotalCount(getCheckSheetListCount(request, session));
		}

		StringBuilder sb = new StringBuilder(
				"select sc.id as id, sc.title as title, sc.skill_level_id as skillLevelId , sc.branch_id as branchId, \r\n"
						+ "	 sc.no_of_days as noOfDays , sc.created_by as createdBy ,b.name as branchName, \r\n"
						+ "	 sl.level_name as skillLevel,sc.cycle_plan as cyclePlan ,sc.production_plan as productionPlan,"
						+ "  l.name as lineName,l.id as lineId,d.dept_id as deptId,d.dept_name as deptName,sw.id as workstationId , sw.workstation as workstation \r\n"
						+ "   from  sm_checksheet sc inner join master_branch b on b.branch_id=sc.branch_id \r\n"
						+ "	 inner join sm_skill_level sl on sl.id=sc.skill_level_id \r\n"
						+ "	 inner join master_organization o on o.org_id=b.org_id "
						+ "  inner join master_department d on d.dept_id=sc.dept_id\r\n"
						+ "  inner join dwm_line l on l.id=sc.line_id\r\n"
						+ "  inner join sm_workstations sw on sw.id = sc.workstation_id \r\n"
						+ "	 where sc.is_active=1 and o.org_id=:orgId ");

		if (request.getCheckSheetId() > 0) {
			sb.append(" and  sc.id=:id ");
		}
		if (request.getBranchId() > 0) {
			sb.append(" and  sc.branch_id=:branchId ");
		}
		if (request.getSkillLvlId() > 0) {
			sb.append(" and  sc.skill_level_id=:skillLvlId ");
		}
		if (request.getDeptId() > 0) {
			sb.append(" and sc.dept_id in (:deptId) ");
		}
		if (!CollectionUtils.isEmpty(request.getLineIds())) {
			sb.append(" and sc.line_id in (:lineIds) ");
		}
		if (!CollectionUtils.isEmpty(request.getWorkstationIds())) {
			sb.append(" and sc.workstation_id in (:workstationIds) ");
		}

		if (request.getSearch() != null) {
			sb.append(" and (sc.title like '%{search}%' or sl.level_name like '%{search}%' or  sw.workstation like '%{search}%')");
		}
		if (request.getColName() != null && request.getOrderType() != null) {
			sb.append(" order by " + ColumnAscDescConstants.smCheckSheetColName.get(request.getColName()) + " "
					+ request.getOrderType());
		} else {
			sb.append(" order by sc.id DESC ");
		}

		String tmpQuery = sb.toString().replace("{search}", (String.valueOf(request.getSearch())));

		TypedQuery<Tuple> query = session.createNativeQuery(tmpQuery, Tuple.class).setParameter("orgId",
				request.getOrgId());
		if (request.getCheckSheetId() > 0) {
			query.setParameter("id", request.getCheckSheetId());
		}
		if (request.getBranchId() > 0) {
			query.setParameter("branchId", request.getBranchId());
		}
		if (request.getSkillLvlId() > 0) {
			query.setParameter("skillLvlId", request.getSkillLvlId());
		}
		if (request.getDeptId() > 0) {
			query.setParameter("deptId", request.getDeptId());
		}
		if (!CollectionUtils.isEmpty(request.getLineIds())) {
			query.setParameter("lineIds", request.getLineIds());
		}
		if (!CollectionUtils.isEmpty(request.getWorkstationIds())) {
			query.setParameter("workstationIds", request.getWorkstationIds());
		}
		if (request.getLimit() > 0) {
			query.setFirstResult(request.getOffset()).setMaxResults(request.getLimit());
		}
		List<Tuple> tupleList = query.getResultList();
		LOGGER.info("# tupleList size : {}", tupleList.size());
		if (!CollectionUtils.isEmpty(tupleList)) {
			return CommonUtils.parseTupleList(tupleList);
		}
		return list;
	}

	private int getCheckSheetListCount(SkillMatrixRequest request, Session session) {
		StringBuilder sb = new StringBuilder(
				"select count(sc.id) as totalCount from  sm_checksheet sc inner join master_branch b on b.branch_id=sc.branch_id \r\n"
						+ " inner join sm_skill_level sl on sl.id=sc.skill_level_id "
						+ " inner join master_organization o on o.org_id=b.org_id "
						+ " inner join master_department d on d.dept_id=sc.dept_id\r\n"
						+ " inner join dwm_line l on l.id=sc.line_id\r\n"
						+ " inner join sm_workstations sw on sw.id = sc.workstation_id "
						+ " where sc.is_active=1 and o.org_id=:orgId");

		if (request.getBranchId() > 0) {
			sb.append(" and  sc.branch_id=:branchId ");
		}
		if (request.getSkillLvlId() > 0) {
			sb.append(" and  sc.skill_level_id=:skillLvlId ");
		}
		if (request.getDeptId() > 0) {
			sb.append(" and sc.dept_id in (:deptId) ");
		}
		if (!CollectionUtils.isEmpty(request.getLineIds())) {
			sb.append(" and sc.line_id in (:lineIds) ");
		}
		if (!CollectionUtils.isEmpty(request.getWorkstationIds())) {
			sb.append(" and sc.workstation_id in (:workstationIds) ");
		}

		if (request.getSearch() != null) {
			sb.append(" and (sc.title like '%{search}%' or sl.level_name like '%{search}%' )");
		}
		String tmpQuery = sb.toString().replace("{search}", (String.valueOf(request.getSearch())));

		TypedQuery<Tuple> query = session.createNativeQuery(tmpQuery, Tuple.class).setParameter("orgId",
				request.getOrgId());

		if (request.getBranchId() > 0) {
			query.setParameter("branchId", request.getBranchId());
		}
		if (request.getSkillLvlId() > 0) {
			query.setParameter("skillLvlId", request.getSkillLvlId());
		}
		if (request.getDeptId() > 0) {
			query.setParameter("deptId", request.getDeptId());
		}
		if (!CollectionUtils.isEmpty(request.getLineIds())) {
			query.setParameter("lineIds", request.getLineIds());
		}
		if (!CollectionUtils.isEmpty(request.getWorkstationIds())) {
			query.setParameter("workstationIds", request.getWorkstationIds());
		}
		List<Tuple> tupleList = query.getResultList();

		if (!CollectionUtils.isEmpty(tupleList)) {
			return CommonUtils.objectToInt(tupleList.get(0).get("totalCount"));
		}
		return 0;
	}

	@Override
	public boolean updateChecksheet(SkillMatrixRequest request) {
		LOGGER.info("# SettingDaoImpl || updateChecksheet");
		Session session = getCurrentSession();

		Tuple tupleList = smUtils.getChecksheetForAudit(request, session);

		SMChecksheet obj = session.get(SMChecksheet.class, request.getCheckSheetId());
		if (obj != null) {
			if (request.getTitle() != null) {
				if (smUtils.isTitleExist(session, request)) {
					obj.setTitle(request.getTitle());
				} else {
					throw new EnovationException(SMConstant.CHECKSHEET_ALREADY_EXIST);
				}
			}
			if (request.getNoOfDays() > 0) {
				obj.setNoOfDays(request.getNoOfDays());
			}
			if (request.getSkillLvlId() > 0) {
				obj.setSkillLevel(new SMSkillLevel(request.getSkillLvlId()));
			}
			if (request.getUpdatedBy() > 0) {
				obj.setUpdatedBy(request.getUpdatedBy());
			}
			if (request.getProductionPlan() > 0) {
				obj.setProductionPlan(request.getProductionPlan());
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
			obj.setCyclePlan(request.getCyclePlan());

			session.update(obj);
			smAudit.updateSMChecksheetAudit(session, tupleList, request, obj);
			return true;
		}
		return false;
	}

	public List<HashMap<String, Object>> getWorkstationList(SkillMatrixRequest request, SkillMatrixResponse response) {
		LOGGER.info("# SettingDaoImpl | getWorkstationList");
		Session session = getCurrentSession();

		if (request.getOffset() == 0) {
			response.setTotalCount(getWorkstationDetailsCount(session, request));
		}
		StringBuilder sql = new StringBuilder(
				"Select sw.id as id,sw.created_by as createdBy,sw.created_date as createdDate,sw.machine_index as machineIndex,\r\n"
						+ "sw.branch_id as branchId,b.name as branchName,\r\n"
						+ "sw.required_workforce as requiredWorkforce,sw.updated_by as updatedBy,sw.updated_date\r\n"
						+ "as updatedDate,sw.workstation as workstation, \r\n"
						+ "sw.dept_id as deptId,o.org_id as orgId ,o.name as orgName,sw.line_id as lineId,l.name as lineName,\r\n"
						+ "d.dept_name as deptName,sw.req_skill_level_id as reqSkillLevelId,sl.level_name as levelName,\r\n"
						+ "sw.is_active as isActive,sw.machine_count as machineCount " + "from sm_workstations sw\r\n"
						+ "inner join master_department d on d.dept_id=sw.dept_id\r\n"
						+ "inner join master_branch b on b.branch_id=sw.branch_id\r\n"
						+ "inner join sm_skill_level sl on sl.id=sw.req_skill_level_id\r\n"
						+ "inner join master_organization o on o.org_id=b.org_id\r\n"
						+ "left join dwm_line l on l.id=sw.line_id where  sw.is_active =1 and o.org_id=:orgId ");

		if (request.getBranchId() > 0) {
			sql.append(" AND sw.branch_id=:branchId ");
		}
		if (request.getDeptId() > 0) {
			sql.append(" AND sw.dept_id =:deptId ");
		}
		if (!CollectionUtils.isEmpty(request.getLineIds())) {
			sql.append("AND sw.line_id IN (:lineIds) ");
		}
		if (request.getSearch() != null) {
			sql.append(" and ( sw.workstation like '%{search}%' or l.name like '%{search}%' ) ");
		}
		if (request.getColName() != null && request.getOrderType() != null) {
			sql.append(" order by " + ColumnAscDescConstants.WorkstationColName.get(request.getColName())
					+ request.getOrderType());
		} else {

			sql.append(" order by sw.id DESC ");
		}
		String tmpQuery = sql.toString().replace("{search}", (String.valueOf(request.getSearch())));

		TypedQuery<Tuple> query = session.createNativeQuery(tmpQuery, Tuple.class).setParameter("orgId",
				request.getOrgId());

		if (request.getBranchId() > 0) {
			query.setParameter("branchId", request.getBranchId());
		}

		if (request.getDeptId() > 0) {
			query.setParameter("deptId", request.getDeptId());
		}
		if (!CollectionUtils.isEmpty(request.getLineIds())) {
			query.setParameter("lineIds", request.getLineIds());
		}
		if (request.getLimit() > 0) {
			query.setFirstResult(request.getOffset()).setMaxResults(request.getLimit());
		}

		List<Tuple> tuplelist = query.getResultList();

		if (!CollectionUtils.isEmpty(tuplelist)) {
			return CommonUtils.parseTupleList(tuplelist);
		}

		return null;
	}

	private int getWorkstationDetailsCount(Session session, SkillMatrixRequest request) {
		LOGGER.info("# SettingDaoImpl || getWorkstatuonDetailsCount");
		StringBuilder sql = new StringBuilder(
				" Select count(sw.id ) as totalCount,sw.workstation as workstation,d.dept_name as deptName from sm_workstations sw\r\n"
						+ "   left join master_department d on d.dept_id=sw.dept_id \r\n"
						+ "   left join master_branch b on b.branch_id=sw.branch_id \r\n"
						+ "	  left join sm_skill_level sl on sl.id=sw.req_skill_level_id \r\n"
						+ "	  inner join master_organization o on o.org_id=b.org_id \r\n"
						+ "   left join dwm_line l on l.id=sw.line_id "
						+ "   where sw.is_active =1 and o.org_id=:orgId ");

		if (request.getBranchId() > 0) {
			sql.append(" AND sw.branch_id=:branchId ");
		}
		if (request.getDeptId() > 0) {
			sql.append(" AND sw.dept_id =:deptId ");
		}
		if (!CollectionUtils.isEmpty(request.getLineIds())) {
			sql.append(" AND sw.line_id IN (:lineIds) ");
		}
		if (request.getSearch() != null) {
			sql.append(" and ( sw.workstation like '%{search}%' or d.dept_name like '%{search}%' )");
		}

		String tmpQuery = sql.toString().replace("{search}", (String.valueOf(request.getSearch())));

		TypedQuery<Tuple> query = session.createNativeQuery(tmpQuery.toString(), Tuple.class).setParameter("orgId",
				request.getOrgId());

		if (request.getBranchId() > 0) {
			query.setParameter("branchId", request.getBranchId());
		}

		if (request.getDeptId() > 0) {
			query.setParameter("deptId", request.getDeptId());
		}
		if (!CollectionUtils.isEmpty(request.getLineIds())) {
			query.setParameter("lineIds", request.getLineIds());
		}
		List<Tuple> objList = query.getResultList();
		if (!CollectionUtils.isEmpty(objList)) {
			return CommonUtils.objectToInt(objList.get(0).get("totalCount"));
		}
		return 0;
	}

	@Override
	public boolean saveUserType(SkillMatrixRequest request) {
		LOGGER.info("# SettingDaoImpl || saveUserType");
		Session session = getCurrentSession();
		boolean flag = false;

		if (smUtils.checkUserTypeAlreadyExit(request, session)) {

			SMUserType user = new SMUserType();

			user.setIsActive(request.getIsActive());
			if (request.getCreatedBy() > 0) {
				user.setCreatedBy(request.getCreatedBy());
			}
			if (request.getUserTypeId() > 0) {
				user.setUserType(new SMMasterUserType(request.getUserTypeId()));
			}
			if (request.getBranchId() > 0) {
				user.setBranch(new BranchMaster(request.getBranchId()));
			}
			if (request.getDeptId() > 0) {
				user.setDept(new DepartmentMaster(request.getDeptId()));
			}
			if (request.getEmpId() > 0) {
				user.setEmpDetails(new EmployeeDetails(request.getEmpId()));
			}
			if (request.getLineId() > 0) {
				user.setLine(new Line(request.getLineId()));
			}
			session.save(user);
			flag = true;

			return flag;
		}
		throw new EnovationException(" Role already exists");
	}

	@Override
	public boolean deleteUserType(SkillMatrixRequest request) {
		LOGGER.info("# SettingDaoImpl | deleteUserType");
		Session session = getCurrentSession();
		boolean flag = false;

		Tuple tupleObj = smUtils.getUserTypeDetails(request, session);
		if (request.getId() > 0) {
			session.createNativeQuery(" delete from sm_user_type where id=:id ").setParameter("id", request.getId())
					.executeUpdate();

			if (tupleObj != null) {
				smAudit.deleteUserType(session, tupleObj, request);
				flag = true;
			}
		}
		return flag;

	}

	@Override
	public List<HashMap<String, Object>> getUserTypeList(SkillMatrixRequest request, SkillMatrixResponse response) {
		LOGGER.info("# SettingDaoImpl | getUserTypeList");
		Session session = getCurrentSession();

		if (request.getOffset() == 0) {
			response.setTotalCount(getUserDetailsCount(session, request));
		}

		StringBuilder sb = new StringBuilder(
				" SELECT t.id AS id, t.created_by AS createdBy, t.created_date AS createdDate, t.updated_by AS updatedBy, t.updated_date AS updatedDate, \r\n"
						+ "	t.branch_id AS branchId, b.name AS branchName, t.emp_id AS empId, t.user_type_id AS  userTypeId, CONCAT(IFNULL(e.first_name, ''), ' ', IFNULL(e.last_name, '')) as empName, \r\n"
						+ "	sm.user_type AS userType, t.is_active AS isActive, t.dept_id AS deptId, d.dept_name AS deptName ,e.cmpy_emp_id as companyEmpId,\r\n"
						+ " o.org_id as orgId ,o.name as orgName,t.line_id as lineId,l.name as lineName \r\n"
						+ "	FROM sm_user_type t  LEFT JOIN master_branch b ON b.branch_id = t.branch_id \r\n"
						+ "	LEFT JOIN sm_master_user_type sm ON sm.id = t.user_type_id \r\n"
						+ "	LEFT JOIN tbl_employee_details e ON e.emp_id = t.emp_id \r\n"
						+ "	LEFT JOIN master_department d ON d.dept_id = t.dept_id  \r\n"
						+ "	INNER join master_organization o on o.org_id=b.org_id \r\n"
						+ "	LEFT join dwm_line l on l.id=t.line_id  \r\n"
						+ "	where t.is_active =1 and o.org_id=:orgId ");

		if (request.getEmpId() > 0) {
			sb.append(" AND t.emp_id=:empId ");
		}

		if (request.getBranchId() > 0) {
			sb.append(" AND t.branch_id=:branchId ");
		}

		if (request.getDeptId() > 0) {
			sb.append(" AND t.dept_id=:deptId ");
		}
		if (request.getLineIds() != null) {
			sb.append(" AND t.line_id IN (:lineIds) ");
		}

		if (request.getSearch() != null) {
			sb.append(" and (TRIM(CONCAT(IFNULL(e.first_name, ''), ' ', IFNULL(e.last_name, ''))) like '%"
					+ request.getSearch() + "%' or e.cmpy_emp_id like '%" + request.getSearch() + "%')");
		}

		if (request.getColName() != null && request.getOrderType() != null) {
			sb.append(" order by " + ColumnAscDescConstants.StakeholderColName.get(request.getColName())
					+ request.getOrderType());
		} else {

			sb.append(" order by t.id  DESC ");
		}
		String tmpQuery = sb.toString().replace("{search}", (String.valueOf(request.getSearch())));

		TypedQuery<Tuple> query = session.createNativeQuery(tmpQuery.toString(), Tuple.class).setParameter("orgId",
				request.getOrgId());

		if (request.getEmpId() > 0) {
			query.setParameter("empId", request.getEmpId());
		}

		if (request.getBranchId() > 0) {
			query.setParameter("branchId", request.getBranchId());
		}

		if (request.getDeptId() > 0) {
			query.setParameter("deptId", request.getDeptId());
		}
		if (request.getLineIds() != null) {
			query.setParameter("lineIds", request.getLineIds());
		}
		if (request.getLimit() > 0) {
			query.setFirstResult(request.getOffset()).setMaxResults(request.getLimit());
		}

		List<Tuple> tuplelist = query.getResultList();

		if (!CollectionUtils.isEmpty(tuplelist)) {
			return CommonUtils.parseTupleList(tuplelist);
		}

		return null;
	}

	private int getUserDetailsCount(Session session, SkillMatrixRequest request) {
		LOGGER.info("# SettingDaoImpl || getUserDetailsCount");

		StringBuilder sb = new StringBuilder(
				"   Select count(t.id ) as totalCount,e.cmpy_emp_id as companyEmpId,CONCAT(IFNULL(e.first_name, ''), ' ',                 IFNULL(e.last_name, '')) as empName\r\n"
						+ " from sm_user_type t LEFT JOIN master_branch b ON b.branch_id = t.branch_id \r\n"
						+ "	 LEFT JOIN sm_master_user_type sm ON sm.id = t.user_type_id \r\n"
						+ "	 LEFT JOIN master_department d ON d.dept_id = t.dept_id  \r\n"
						+ "	 LEFT JOIN tbl_employee_details e ON e.emp_id = t.emp_id \r\n"
						+ "	 inner join master_organization o on o.org_id=b.org_id\r\n"
						+ "  LEFT join dwm_line l on l.id=t.line_id  \r\n"
						+ " where  t.is_active =1 and o.org_id=:orgId ");
		if (request.getBranchId() > 0) {
			sb.append(" AND t.branch_id=:branchId ");
		}

		if (request.getDeptId() > 0) {
			sb.append(" AND t.dept_id=:deptId ");
		}
		if (request.getLineIds() != null) {
			sb.append(" AND t.line_id IN (:lineIds) ");
		}
		if (request.getSearch() != null) {
			sb.append(" and (TRIM(CONCAT(IFNULL(e.first_name, ''), ' ', IFNULL(e.last_name, ''))) like '%"
					+ request.getSearch() + "%' or e.cmpy_emp_id like '%" + request.getSearch() + "%')");
		}

		String tmpQuery = sb.toString().replace("{search}", (String.valueOf(request.getSearch())));

		TypedQuery<Tuple> query = session.createNativeQuery(tmpQuery.toString(), Tuple.class).setParameter("orgId",
				request.getOrgId());

		if (request.getBranchId() > 0) {
			query.setParameter("branchId", request.getBranchId());
		}

		if (request.getDeptId() > 0) {
			query.setParameter("deptId", request.getDeptId());
		}
		if (request.getLineIds() != null) {
			query.setParameter("lineIds", request.getLineIds());
		}
		List<Tuple> objList = query.getResultList();
		if (!CollectionUtils.isEmpty(objList)) {
			return CommonUtils.objectToInt(objList.get(0).get("totalCount"));
		}
		return 0;
	}

	public List<HashMap<String, Object>> getMasterUserType() {
		LOGGER.info("# SettingDaoImpl | getMasterUserType");
		Session session = getCurrentSession();

		StringBuilder sb = new StringBuilder(
				"Select ut.id as id,ut.created_by as createdBy,ut.created_date as createdDate,ut.updated_by as updatedBy,\r\n"
						+ "	ut.updated_date as updatedDate,ut.user_type as userType,user_type_caption as userTypeCaption\r\n"
						+ "	from sm_master_user_type ut ");

		TypedQuery<Tuple> query = session.createNativeQuery(sb.toString(), Tuple.class);
		List<Tuple> tuplelist = query.getResultList();

		if (!CollectionUtils.isEmpty(tuplelist)) {
			return CommonUtils.parseTupleList(tuplelist);
		}

		return null;
	}

	@Override
	public boolean deActivateWorkstation(SkillMatrixRequest request) {
		Session session = getCurrentSession();
		boolean flag = false;
		Tuple obj = smUtils.getWorkstationDetails(request, session);

		session.createNativeQuery(" update sm_workstations set is_active=:isActive where id=:id ")
				.setParameter("isActive", EnovationConstants.FALSE).setParameter("id", request.getId()).executeUpdate();
		if (obj != null) {
			smAudit.deActivateWorkstation(session, obj, request);
			flag = true;
		}
		return flag;

	}

	@Override
	public boolean deActiveChecksheet(SkillMatrixRequest request) {
		LOGGER.info("#In SettingDaoImpl |  INSIDE in deActiveChecksheet ");
		Session session = getCurrentSession();

		session.createNativeQuery("update sm_checksheet set is_active=:isActive where id=:id ")
				.setParameter("isActive", false).setParameter("id", request.getCheckSheetId()).executeUpdate();
		smAudit.deActiveSMChecksheetAudit(session, request);
		return true;

	}

	@Override
	public boolean deleteChecksheet(SkillMatrixRequest request) {
		LOGGER.info("#In SettingDaoImpl |  INSIDE in deActiveAssessment ");
		Session session = getCurrentSession();
		Tuple tupleList = smUtils.getChecksheetForAudit(request, session);
		session.createNativeQuery("delete from sm_checksheet where id=:Id")
				.setParameter("Id", request.getCheckSheetId()).executeUpdate();

		smAudit.deleteSMChecksheetAudit(session, tupleList, request);
		return true;
	}

	@Override
	public boolean addChecksheetPoint(SkillMatrixRequest request) {
		LOGGER.info("# SettingDaoImpl || addChecksheetPoint");
		Session session = getCurrentSession();
		return saveChecksheetPoint(request, session);
	}

	private boolean saveChecksheetPoint(SkillMatrixRequest request, Session session) {
		LOGGER.info("# SettingDaoImpl || saveChecksheetPoint");
		boolean flag = false;

		if (!CollectionUtils.isEmpty(request.getDaysList())) {
			for (SkillMatrixRequest day : request.getDaysList()) {
				if (!CollectionUtils.isEmpty(day.getPointList())) {
					for (SkillMatrixRequest point : day.getPointList()) {

						if (point.getAction() != null && point.getAction().equals(SMConstant.ADD)) {
							LOGGER.info("# Action : {}", point.getAction());
							SMChecksheetPoints checksheetPoint = new SMChecksheetPoints();
							checksheetPoint.setDayNo(day.getDayNo());

							if (point.getItemName() != null) {
								if (smUtils.isItemNameExist(session, point, day, request)) {
									checksheetPoint.setItemName(point.getItemName());
								} else {
									throw new EnovationException("Checksheet Point is already exist");
								}
							}
							checksheetPoint.setItemName(point.getItemName());
							checksheetPoint.setReference(point.getReference());
							checksheetPoint.setChecksheet(new SMChecksheet(request.getCheckSheetId()));
							checksheetPoint.setCreatedBy(request.getCreatedBy());
							checksheetPoint.setIsActive(true);
							session.save(checksheetPoint);
							flag = true;
						} else if (point.getAction() != null && point.getAction().equals(SMConstant.UPDATE)) {
							LOGGER.info("# Action : {}", point.getAction());
							Tuple tupleList = smUtils.getChecksheetPointDetails(point, session);
							SMChecksheetPoints checksheetPoint = session.get(SMChecksheetPoints.class, point.getId());
							if (day.getDayNo() > 0) {
								checksheetPoint.setDayNo(day.getDayNo());
							}

							if (point.getItemName() != null && smUtils.isItemNameExist(session, point, day, request)) {
								checksheetPoint.setItemName(point.getItemName());
							} else {
								throw new EnovationException("Checksheet Point is already exist");
							}
							if (point.getReference() != null) {
								checksheetPoint.setReference(point.getReference());
							}
							checksheetPoint.setUpdatedBy(request.getUpdatedBy());
//							checksheetPoint.setIsActive(true);

							session.update(checksheetPoint);
							flag = true;

							smAudit.updateSMChecksheetPointAudit(session, tupleList, request, point, day);
						} else if (point.getAction() != null && point.getAction().equals(SMConstant.DELETE)) {
							LOGGER.info("# Action : {} and {} ", point.getAction(), point.getId());
							Tuple tupleList = smUtils.getChecksheetPointDetails(request, session);
							LOGGER.info("# defore");
							session.createNativeQuery(" delete from sm_checksheet_points where id=:id ")
									.setParameter("id", point.getId()).executeUpdate();
							LOGGER.info("# after");
							if (tupleList != null) {
								smAudit.deleteSMChecksheetPointAudit(session, tupleList, request);
							}
							flag = true;
						}
					}
				}
			}
		}
		return flag;
	}

	@Override
	public boolean updateChecksheetPoint(SkillMatrixRequest request) {
		LOGGER.info("# SettingDaoImpl || updateChecksheetPoint");
		Session session = getCurrentSession();
		boolean flag = false;

		if (request.getDaysList() != null) {
			for (SkillMatrixRequest day : request.getDaysList()) {
				if (day.getPointList() != null) {
					for (SkillMatrixRequest point : day.getPointList()) {
						Tuple tupleList = smUtils.getChecksheetPointDetails(point, session);
						SMChecksheetPoints checksheetPoint = session.get(SMChecksheetPoints.class, point.getId());
						checksheetPoint.setDayNo(day.getDayNo());

						if (point.getItemName() != null && smUtils.isItemNameExist(session, point, day, request)) {
							checksheetPoint.setItemName(point.getItemName());
						} else {
							throw new EnovationException("Checksheet Point is already exist");
						}

						checksheetPoint.setReference(point.getReference());
						checksheetPoint.setChecksheet(new SMChecksheet(request.getCheckSheetId()));
						checksheetPoint.setUpdatedBy(request.getUpdatedBy());
						checksheetPoint.setIsActive(true);

						session.update(checksheetPoint);

						smAudit.updateSMChecksheetPointAudit(session, tupleList, request, point, day);
						flag = true;
					}
				}
			}
		}

		return flag;
	}

	@Override
	public List<HashMap<String, Object>> getChecksheetPointList(SkillMatrixRequest request) {
		LOGGER.info("# SettingDaoImpl || getChecksheet");
		Session session = getCurrentSession();
		List<HashMap<String, Object>> list = new ArrayList<>();
		StringBuilder sb = new StringBuilder(
				"select scp.id as checksheetPointId,scp.day_no as dayNo,scp.item_name as itemName , sc.id as checksheetId, sc.title as title, sc.skill_level_id as skillLevelId , sc.branch_id as branchId, \r\n"
						+ "	 sc.no_of_days as noOfDays , sc.created_by as createdBy ,b.name as branchName,scp.reference as reference,\r\n"
						+ "	 sl.level_name as skillLevel \r\n"
						+ "  from sm_checksheet_points scp inner join sm_checksheet sc on sc.id=scp.checksheet_id \r\n"
						+ "	 inner join master_branch b on b.branch_id=sc.branch_id \r\n"
						+ "	 inner join sm_skill_level sl on sl.id=sc.skill_level_id where scp.is_active=1 ");

		if (request.getId() > 0) {
			sb.append(" and  scp.id=:id ");
		}
		if (request.getBranchId() > 0) {
			sb.append(" and  sc.branch_id=:branchId ");
		}
		if (request.getSkillLvlId() > 0) {
			sb.append(" and  sc.skill_level_id=:skillLvlId ");
		}
		TypedQuery<Tuple> query = session.createNativeQuery(sb.toString(), Tuple.class);

		if (request.getId() > 0) {
			query.setParameter("id", request.getId());
		}
		if (request.getBranchId() > 0) {
			query.setParameter("branchId", request.getBranchId());
		}
		if (request.getSkillLvlId() > 0) {
			query.setParameter("skillLvlId", request.getSkillLvlId());
		}
		List<Tuple> tupleList = query.getResultList();
		LOGGER.info("# tupleList size : {}", tupleList.size());
		if (!CollectionUtils.isEmpty(tupleList)) {
			return CommonUtils.parseTupleList(tupleList);
		}
		return list;
	}

	@Override
	public boolean deleteChecksheetPoint(SkillMatrixRequest request) {
		LOGGER.info("#In SettingDaoImpl |  INSIDE in deleteChecksheetPoint ");
		Session session = getCurrentSession();
		Tuple tupleList = smUtils.getChecksheetPointDetails(request, session);
		StringBuilder sb = new StringBuilder(" delete from sm_checksheet_points where ");

		if (request.getDayNo() > 0 && request.getCheckSheetId() > 0) {
			sb.append(" day_no=:dayNo and checksheet_id=:checkSheetId ");
		}
		if (request.getId() > 0) {
			sb.append(" id=:id ");
		}

		Query query = session.createNativeQuery(sb.toString());

		if (request.getId() > 0) {
			query.setParameter("id", request.getId());
		}
		if (request.getDayNo() > 0 && request.getCheckSheetId() > 0) {
			query.setParameter("dayNo", request.getDayNo());
			query.setParameter("checkSheetId", request.getCheckSheetId());
		}

		query.executeUpdate();

		if (tupleList != null) {
			smAudit.deleteSMChecksheetPointAudit(session, tupleList, request);
		}
		return true;
	}

	@Override
	public boolean deActiveChecksheetPoint(SkillMatrixRequest request) {
		LOGGER.info("#In SettingDaoImpl |  INSIDE in deActiveChecksheetPoint ");
		Session session = getCurrentSession();

		StringBuilder sb = new StringBuilder(" update sm_checksheet_points set is_active=:isActive where ");

		if (request.getDayNo() > 0 && request.getCheckSheetId() > 0) {
			sb.append(" day_no=:dayNo and checksheet_id=:checkSheetId ");
		}

		if (request.getId() > 0) {
			sb.append("id=:id ");
		}

		Query query = session.createNativeQuery(sb.toString()).setParameter("isActive", false);

		if (request.getId() > 0) {
			query.setParameter("id", request.getId());
		}
		if (request.getDayNo() > 0 && request.getCheckSheetId() > 0) {
			query.setParameter("dayNo", request.getDayNo());
			query.setParameter("checkSheetId", request.getCheckSheetId());
		}

		query.executeUpdate();

		smAudit.deActiveSMChecksheetPointAudit(session, request);
		return true;
	}

	@Override
	public boolean saveMasterCertificate(SkillMatrixRequest request) {
		LOGGER.info("# SettingDaoImpl || saveMasterCertificate");
		boolean flag = false;
		Session session = getCurrentSession();

		SMMasterCertificate obj = new SMMasterCertificate();
		LOGGER.info("# SettingDaoImpl || saveMasterCertificate");

		if (request.getSkillLevelId() > 0) {
			smWorker.makeCertificateInavtive(session, request);
		}
		if (request.getCertificateName() != null) {
			obj.setCertificateName(request.getCertificateName());
		}
		if (request.getCertificateCaption() != null) {
			obj.setCertificateCaption(request.getCertificateCaption());
		}
		if (request.getSkillLevelId() > 0) {
			obj.setSkillLevel(new SMSkillLevel(request.getSkillLevelId()));
		}
		if (request.getBranchId() > 0) {
			obj.setBranch(new BranchMaster(request.getBranchId()));
		}
		if (request.getCreatedBy() > 0) {
			obj.setCreatedBy(request.getCreatedBy());
		}

		obj.setStatus(EnovationConstants.ACTIVE);
		if (request.getCertificatePath() != null) {
			obj.setCertificatePath(smUtils.savePathOnServer(request, obj));
		}
		session.save(obj);
		flag = true;

		return flag;
	}

	public boolean updateMasterCertificate(SkillMatrixRequest request) {
		LOGGER.info("# SettingDaoImpl || updateMasterCertificate");
		boolean flag = false;
		Session session = getCurrentSession();
		String path = "";

		SMMasterCertificate obj = session.get(SMMasterCertificate.class, request.getCertificateId());

		Tuple tupleObj = session
				.createNativeQuery("SELECT smmcer.id AS id,smmcer.certificate_caption AS certificateCaption,"
						+ "smmcer.certificate_name AS certificateName,smmcer.certificate_path AS path,smmcer.status AS status FROM sm_master_certificate smmcer"
						+ " WHERE smmcer.id=:certificateId", Tuple.class)
				.setParameter("certificateId", request.getCertificateId()).getResultList().stream().findFirst()
				.orElse(null);

		if (request.getCertificateName() != null) {
			obj.setCertificateName(request.getCertificateName());
		}

		if (request.getCertificateCaption() != null) {
			obj.setCertificateCaption(request.getCertificateCaption());
		}

		if (request.getUpdatedBy() > 0) {
			obj.setUpdatedBy(request.getUpdatedBy());
		}

		if (request.getStatus() != null) {
			obj.setStatus(request.getStatus());
		}
		if (request.getSkillLevelId() > 0) {
			obj.setSkillLevel(new SMSkillLevel(request.getSkillLevelId()));
		}
		if (request.getCertificatePath() != null) {
			path = smUtils.savePathOnServer(request, obj);
			obj.setCertificatePath(path);
		}
		session.update(obj);

		smAudit.updateMasterCertificateAudit(session, request, tupleObj, path);
		flag = true;

		return flag;
	}

	public List<HashMap<String, Object>> getMasterCertificateList(SkillMatrixRequest request,
			SkillMatrixResponse response) {
		LOGGER.info("# SettingDaoImpl || getMasterCertificateList");
		Session session = getCurrentSession();

		if (request.getOffset() == 0) {
			response.setTotalCount(getCertificateCount(session, request));
		}

		StringBuilder sb = new StringBuilder(
				"SELECT smmcer.id AS certificateId,smmcer.certificate_caption AS certificateCaption,smmcer.certificate_name AS certificateName,\r\n"
						+ " concat((SELECT config_value FROM system_config WHERE config_name = 'ProfilePicPathUrl'),smmcer.certificate_path ) AS certificatePath,\r\n"
						+ " smmcer.status AS status,sl.level_name AS skillLevelName,mb.branch_id AS branchId,mb.name AS branchName\r\n"
						+ " FROM sm_master_certificate smmcer\r\n"
						+ " INNER JOIN sm_skill_level sl ON sl.id=smmcer.skill_level_id\r\n"
						+ " LEFT JOIN master_branch mb ON mb.branch_id=smmcer.branch_id");

		if (request.getSearch() != null) {
			sb.append(
					" where (smmcer.certificate_name like '%{search}%' or smmcer.status like '{search}' or sl.level_name like '%{search}%' )");
		}

		if (request.getColName() != null && request.getOrderType() != null) {
			sb.append(" order by " + ColumnAscDescConstants.smCertificateColName.get(request.getColName()) + " "
					+ request.getOrderType());
		} else {
			sb.append(" order by smmcer.id DESC ");
		}

		String tmpQuery = sb.toString().replace("{search}", (String.valueOf(request.getSearch())));

		TypedQuery<Tuple> query = session.createNativeQuery(tmpQuery, Tuple.class);

		if (request.getLimit() > 0) {
			query.setFirstResult(request.getOffset()).setMaxResults(request.getLimit());
		}

		List<Tuple> tupleList = query.getResultList();

		if (!CollectionUtils.isEmpty(tupleList)) {
			return CommonUtils.parseTupleList(tupleList);
		}
		return null;
	}

	private int getCertificateCount(Session session, SkillMatrixRequest request) {
		LOGGER.info("#In SettingDaoImpl | getCertificateCount ");

		StringBuilder sb = new StringBuilder(
				"SELECT COUNT(smmcer.id) AS totalCount FROM sm_master_certificate smmcer \r\n"
						+ " inner join sm_skill_level sl on sl.id=smmcer.skill_level_id");

		if (request.getSearch() != null) {
			sb.append(
					" where (smmcer.certificate_name like '%{search}%' or smmcer.status like '{search}' or sl.level_name like '%{search}%' )");
		}

		String tmpQuery = sb.toString().replace("{search}", (String.valueOf(request.getSearch())));

		TypedQuery<Tuple> query = session.createNativeQuery(tmpQuery, Tuple.class);

		List<Tuple> objList = query.getResultList();
		if (!CollectionUtils.isEmpty(objList)) {
			return CommonUtils.objectToInt(objList.get(0).get("totalCount"));
		}
		return 0;
	}

	@Override
	public boolean deleteMasterCertificate(SkillMatrixRequest request) {
		LOGGER.info("#In SettingDaoImpl | deleteMasterCertificate ");
		Session session = getCurrentSession();
		Tuple tupleList = smUtils.getMasterCertificateDetails(request.getCertificateId(), session);
		session.createNativeQuery("delete from sm_master_certificate where id=:Id")
				.setParameter("Id", request.getCertificateId()).executeUpdate();
		if (tupleList != null) {
			smAudit.deleteMasterCertificateAudit(session, tupleList, request);
		}
		return true;
	}

	public boolean addChecksheetParameter(SkillMatrixRequest request) {
		LOGGER.info("# SettingDaoImpl || addChecksheetParameter");
		Session session = getCurrentSession();
		return saveChecksheetParameter(request, session);
	}

	private boolean saveChecksheetParameter(SkillMatrixRequest request, Session session) {
		LOGGER.info("# SettingDaoImpl || saveChecksheetParameter");
		boolean flag = false;
		if (!CollectionUtils.isEmpty(request.getParameterList())) {
			for (SkillMatrixRequest para : request.getParameterList()) {
				SMChecksheetParameter obj = new SMChecksheetParameter();
				obj.setChecksheet(new SMChecksheet(request.getCheckSheetId()));
				para.setCheckSheetId(request.getCheckSheetId());
				if (para.getParameter() != null) {
					if (smUtils.isSmParameterExist(session, para)) {
						obj.setParameter(para.getParameter());
					} else {
						throw new EnovationException("Checksheet Parameter is already exist");
					}
				}
				obj.setCreatedBy(request.getCreatedBy());
				obj.setParameterType(new SMParameterType(para.getParameterTypeId()));
				obj.setIsActive(true);
				obj.setCycleValue(para.getCycleValue());
				session.save(obj);
				flag = true;
			}
		}
		return flag;
	}

	@Override
	public List<HashMap<String, Object>> getChecksheetParameterList(SkillMatrixRequest request) {
		LOGGER.info("# SettingDaoImpl || getChecksheetParameterList");
		Session session = getCurrentSession();
		List<HashMap<String, Object>> list = new ArrayList<>();

		StringBuilder sb = new StringBuilder(
				"select id as id , parameter as parameter,parameter_type_id as parameterTypeId,checksheet_id as checksheetId,\r\n"
						+ "created_by as createdBy,updated_by as updatedBy from sm_checksheet_parameter where is_active=1 ");

		if (request.getParameterId() > 0) {
			sb.append(" and id=:id ");
		}
		if (request.getCheckSheetId() > 0) {
			sb.append(" and checksheet_id=:checksheetId ");
		}
		if (request.getParameterTypeId() > 0) {
			sb.append(" and parameter_type_id=:parameterTypeId ");
		}
		TypedQuery<Tuple> query = session.createNativeQuery(sb.toString(), Tuple.class);

		if (request.getParameterId() > 0) {
			query.setParameter("id", request.getParameterId());
		}
		if (request.getCheckSheetId() > 0) {
			query.setParameter("checksheetId", request.getCheckSheetId());
		}
		if (request.getParameterTypeId() > 0) {
			query.setParameter("parameterTypeId", request.getParameterTypeId());
		}

		List<Tuple> tupleList = query.getResultList();
		LOGGER.info("# tupleList size : {} ", tupleList.size());
		if (!CollectionUtils.isEmpty(tupleList)) {
			return CommonUtils.parseTupleList(tupleList);
		}
		return list;
	}

	@Override
	public boolean updateChecksheetParameter(SkillMatrixRequest request) {
		LOGGER.info("# SettingDaoImpl || updateChecksheetParameter");
		Session session = getCurrentSession();

		Tuple tupleList = smUtils.getChecksheetParameterDetails(request, session);

		SMChecksheetParameter obj = session.get(SMChecksheetParameter.class, request.getParameterId());
		if (obj != null) {
			if (request.getParameter() != null) {
				if (smUtils.isSmParameterExist(session, request)) {
					obj.setParameter(request.getParameter());
				} else {
					throw new EnovationException("Checksheet Parameter is already exist");
				}
			}
			if (request.getCheckSheetId() > 0) {
				obj.setChecksheet(new SMChecksheet(request.getCheckSheetId()));
			}
			if (request.getParameterTypeId() > 0) {
				obj.setParameterType(new SMParameterType(request.getParameterTypeId()));
			}
			obj.setUpdatedBy(request.getUpdatedBy());
			session.update(obj);
			smAudit.updateSMChecksheetParameterAudit(session, tupleList, obj);
			return true;
		}
		return false;
	}

	@Override
	public boolean deleteChecksheetParameter(SkillMatrixRequest request) {
		LOGGER.info("#In SettingDaoImpl |  INSIDE in deleteChecksheetParameter ");
		Session session = getCurrentSession();
		Tuple tupleList = smUtils.getChecksheetParameterDetails(request, session);
		int count = session.createNativeQuery("delete from sm_checksheet_parameter where id=:Id")
				.setParameter("Id", request.getParameterId()).executeUpdate();
		if (tupleList != null && count > 0) {
			smAudit.deleteSMChecksheetParameterAudit(session, tupleList, request);
		}
		return true;
	}

	@Override
	public boolean deActiveChecksheetParameter(SkillMatrixRequest request) {
		LOGGER.info("#In SettingDaoImpl |  INSIDE in deActiveChecksheetParameter ");
		Session session = getCurrentSession();
		session.createNativeQuery("update sm_checksheet_parameter set is_active=:isActive where id=:id ")
				.setParameter("isActive", false).setParameter("id", request.getParameterId()).executeUpdate();
		smAudit.deActiveSMChecksheetParameterAudit(session, request);
		return true;
	}

	@Override
	public List<HashMap<String, Object>> getParameterTypeList() {
		LOGGER.info("# SettingDaoImpl || getParameterTypeList");
		Session session = getCurrentSession();
		List<HashMap<String, Object>> list = new ArrayList<>();

		StringBuilder sb = new StringBuilder("select id as id , type_caption as typeCaption,type_name as typeName, "
				+ " created_by as createdBy,updated_by as updatedBy from sm_parameter_type ");

		TypedQuery<Tuple> query = session.createNativeQuery(sb.toString(), Tuple.class);

		List<Tuple> tupleList = query.getResultList();
		LOGGER.info("# tupleList size : {}", tupleList.size());
		if (!CollectionUtils.isEmpty(tupleList)) {
			return CommonUtils.parseTupleList(tupleList);
		}
		return list;
	}

	@Override
	public HashMap<String, Object> getChecksheetDetails(long checkSheetId) {
		LOGGER.info("# SettingDaoImpl || getChecksheetDetails");
		Session session = getCurrentSession();
		HashMap<String, Object> list = new HashMap<>();
		StringBuilder sb = new StringBuilder(
				"select sc.id as id, sc.title as title, sc.skill_level_id as skillLevelId , sc.branch_id as branchId, \r\n"
						+ " sc.no_of_days as noOfDays , sc.created_by as createdBy ,b.name as branchName,\r\n"
						+ " sl.level_name as skillLevel,"
						+ "  l.name as lineName,l.id as lineId,d.dept_id as deptId,d.dept_name as deptName,sw.id as workstationId , sw.workstation as workstation "
						+ " from  sm_checksheet sc inner join master_branch b on b.branch_id=sc.branch_id \r\n"
						+ " inner join sm_skill_level sl on sl.id=sc.skill_level_id "
						+ " inner join master_department d on d.dept_id=sc.dept_id\r\n"
						+ " inner join dwm_line l on l.id=sc.line_id\r\n"
						+ "  inner join sm_workstations sw on sw.id = sc.workstation_id\r\n"
						+ " where sc.is_active=1 and sc.id=:id ");

		TypedQuery<Tuple> query = session.createNativeQuery(sb.toString(), Tuple.class).setParameter("id",
				checkSheetId);

		Tuple tupleList = query.getResultList().stream().findFirst().orElse(null);
		if (tupleList != null) {
			LOGGER.info("# tupleList not null");
			list = CommonUtils.parseSingleTupleRecord(tupleList);

			List<HashMap<String, Object>> parameterList = smUtils.getParameters(session, checkSheetId);
			List<HashMap<String, Object>> pointList = smUtils.getPoints(session, checkSheetId);
			if (!CollectionUtils.isEmpty(parameterList)) {
				list.put("parameterList", parameterList);
			}
			if (!CollectionUtils.isEmpty(pointList)) {
				list.put("pointList", pointList);
			}
		}
		return list;
	}

	@Override
	public boolean publishAssessments(SkillMatrixRequest request) {
		LOGGER.info("#In SettingDaoImpl |  INSIDE in publishAssessments ");
		Session session = getCurrentSession();

		session.createNativeQuery(" update sm_assessment set status=:published where id=:assessmentId ")
				.setParameter("published", SMConstant.ASSESSMENT_PUBLISHED)
				.setParameter("assessmentId", request.getAssessmentId()).executeUpdate();
		smAudit.deActiveSMAssessmentAudit(session, request);
		return true;
	}

	@Override
	public boolean updateUserType(SkillMatrixRequest request) {
		LOGGER.info("# SettingDaoImpl || updateUserType");
		Session session = getCurrentSession();

		Tuple tupleObj = smUtils.getUserTypeDetails(session, request);

		SMUserType user = session.get(SMUserType.class, request.getId());

		if (user != null) {
			if (request.getUserTypeId() > 0) {
				if (smUtils.checkUserTypeAlreadyExitId(request, session)) {
					user.setUserType(new SMMasterUserType(request.getUserTypeId()));
				} else {
					throw new EnovationException("Role is already exist");
				}
			}

			if (request.getIsActive() != null) {
				user.setIsActive(request.getIsActive());
			}
			if (request.getUpdatedBy() > 0) {
				user.setUpdatedBy(request.getUpdatedBy());
			}
			if (request.getBranchId() > 0) {
				user.setBranch(new BranchMaster(request.getBranchId()));
			}
			if (request.getDeptId() > 0) {
				user.setDept(new DepartmentMaster(request.getDeptId()));
			}
			if (request.getEmpId() > 0) {
				user.setEmpDetails(new EmployeeDetails(request.getEmpId()));
			}
			if (request.getLineId() > 0) {
				user.setLine(new Line(request.getLineId()));
			}
			session.update(user);
			if (tupleObj != null) {
				smAudit.updateUserType(session, request, tupleObj);
				return true;

			}
		}
		return false;
	}

	@Override
	public SkillMatrixRequest uploadAssessments(SkillMatrixRequest request) {
		LOGGER.info("In InterventionDaoImpl | uploadAssessmentDetails");
		Session session = getCurrentSession();
		request.setTitle(SMConstant.ASSESSMENT);
		String excelFilePath = readWritefile.writeFileAndGetPath(request);
		return readWritefile.uploadAssessmentDetails(session, request, excelFilePath);
	}

	@Override
	public boolean addAssessmentConfig(SkillMatrixRequest request) {
		LOGGER.info("# SettingDaoImpl || addAssessmentConfig");
		boolean flag = false;
		Session session = getCurrentSession();
		SMAssessmentConfig obj = new SMAssessmentConfig();

		if (request.getSkillLevelId() > 0) {
			if (smUtils.levelAlreadyExist(session, request)) {
				obj.setSkillLevel(new SMSkillLevel(request.getSkillLevelId()));

			} else {
				throw new EnovationException("Asseement Configuration already exist");
			}
		}
		if (request.getBranchId() > 0) {
			obj.setBranch(new BranchMaster(request.getBranchId()));
		}
		if (request.getCreatedBy() > 0) {
			obj.setCreatedBy(request.getCreatedBy());
		}
		if (request.getNoOfDays() > 0) {
			obj.setNoOfDays(request.getNoOfDays());
		}
		session.save(obj);
		flag = true;
		return flag;

	}

	@Override
	public boolean updateAssessmentConfig(SkillMatrixRequest request) {
		LOGGER.info("# SettingDaoImpl || updateAssessmentConfig");
		boolean flag = false;
		Session session = getCurrentSession();

		Tuple tupleList = smUtils.getSMConfigDetails(request, session);

		System.out.print("tupleList ..." + tupleList);

		SMAssessmentConfig obj = session.get(SMAssessmentConfig.class, request.getId());
		if (obj != null) {
			if (request.getSkillLevelId() > 0) {

				if (smUtils.levelAlreadyExist(session, request)) {
					obj.setSkillLevel(new SMSkillLevel(request.getSkillLevelId()));

				} else {

					throw new EnovationException("Level name already exist");
				}
			}
			if (request.getBranchId() > 0) {
				obj.setBranch(new BranchMaster(request.getBranchId()));
			}
			if (request.getUpdatedBy() > 0) {
				obj.setUpdatedBy(request.getUpdatedBy());
			}
			if (request.getNoOfDays() > 0) {
				obj.setNoOfDays(request.getNoOfDays());
			}
			session.update(obj);
			smAudit.updateSMConfigAudit(session, tupleList, request);
			flag = true;
		}

		return flag;
	}

	@Override
	public boolean deleteAssessmentConfig(SkillMatrixRequest request) {
		LOGGER.info("# SettingDaoImpl | deleteAssessmentConfig");
		Session session = getCurrentSession();

		Tuple tuple = smUtils.getSMConfigDetail(request, session);

		int count = session.createNativeQuery("delete from sm_assessment_config where id=:Id")
				.setParameter("Id", request.getId()).executeUpdate();
		if (tuple != null && count > 0) {
			smAudit.deleteSMConfigAudit(session, tuple, request);
		}
		return true;
	}

	public List<HashMap<String, Object>> getAssessmentConfigList(SkillMatrixRequest request,
			SkillMatrixResponse response) {
		LOGGER.info("# SettingDaoImpl || getAssessmentConfigList");
		Session session = getCurrentSession();

		if (request.getOffset() == 0) {
			response.setTotalCount(getAssessmentConfigCount(session, request));
		}

		StringBuilder sb = new StringBuilder(
				"  SELECT sc.id AS id,sc.created_by AS createdBy,sc.created_date AS createdDate,sc.no_of_days AS noOfDays,\r\n"
						+ "	 sc.updated_by AS updatedBy,sc.updated_date AS updatedDate,sc.skill_level_id AS skillLevelId,sl.level_name AS levelName,  \r\n"
						+ "	  sc.branch_id AS branchId,o.name AS orgName,o.org_id AS orgId,b.name AS branchName\r\n"
						+ "	  FROM sm_assessment_config sc  \r\n"
						+ "	  INNER JOIN sm_skill_level sl ON sl.id = sc.skill_level_id  \r\n"
						+ "	  INNER JOIN master_branch b ON b.branch_id = sc.branch_id  \r\n"
						+ "	  INNER JOIN master_organization o ON o.org_id = b.org_id  \r\n"
						+ "	  WHERE o.org_id =:orgId");

		if (request.getBranchId() > 0) {
			sb.append(" AND sc.branch_id =:branchId");
		}
		if (request.getColName() != null && request.getOrderType() != null) {
			sb.append(" order by " + ColumnAscDescConstants.AssessmentConfigColName.get(request.getColName())
					+ request.getOrderType());
		} else {

			sb.append(" order by sc.id  DESC ");
		}

		TypedQuery<Tuple> query = session.createNativeQuery(sb.toString(), Tuple.class).setParameter("orgId",
				request.getOrgId());

		if (request.getBranchId() > 0) {
			query.setParameter("branchId", request.getBranchId());
		}
		if (request.getLimit() > 0) {
			query.setFirstResult(request.getOffset()).setMaxResults(request.getLimit());
		}

		List<Tuple> tupleList = query.getResultList();

		if (!CollectionUtils.isEmpty(tupleList)) {
			return CommonUtils.parseTupleList(tupleList);
		}

		return null;
	}

	private int getAssessmentConfigCount(Session session, SkillMatrixRequest request) {
		LOGGER.info("# SettingDaoImpl || getAssessmentConfigList");
		StringBuilder sb = new StringBuilder(
				"SELECT  count(sc.id) as totalCount,sc.no_of_days AS noOfDays,sc.skill_level_id AS skillLevelId,sl.level_name AS levelName,  \r\n"
						+ "	 sc.branch_id AS branchId,o.name AS orgName,o.org_id AS orgId,b.name AS branchName\r\n"
						+ "	 FROM sm_assessment_config sc  \r\n"
						+ "	 left JOIN sm_skill_level sl ON sl.id = sc.skill_level_id  \r\n"
						+ "	 left JOIN master_branch b ON b.branch_id = sc.branch_id  \r\n"
						+ "	 INNER JOIN master_organization o ON o.org_id = b.org_id  \r\n"
						+ "	 WHERE o.org_id =:orgId");

		if (request.getBranchId() > 0) {
			sb.append(" AND sc.branch_id =:branchId");
		}

		TypedQuery<Tuple> query = session.createNativeQuery(sb.toString(), Tuple.class).setParameter("orgId",
				request.getOrgId());

		if (request.getBranchId() > 0) {
			query.setParameter("branchId", request.getBranchId());
		}

		List<Tuple> objList = query.getResultList();
		if (!CollectionUtils.isEmpty(objList)) {
			return CommonUtils.objectToInt(objList.get(0).get("totalCount"));
		}
		return 0;

	}

	@Override
	public List<HashMap<String, Object>> getStageLabelList(SkillMatrixRequest request) {
		LOGGER.info("# SettingDaoImpl || getStageLabelList");

		Session session = getCurrentSession();
		StringBuilder sb = new StringBuilder(
				" SELECT DISTINCT stl.id AS stageLabelId, stl.stage_label AS stageLabel, stl.branch_id AS branchId, sm.id AS stageId, \r\n"
						+ " sm.stage_name AS stageName, sm.stage_caption AS stageCaption FROM  sm_stage sm\r\n"
						+ " LEFT JOIN sm_stage_label stl ON stl.stage_id = sm.id  AND stl.branch_id =:branchId\r\n"
						+ " ORDER BY sm.id ASC");

		TypedQuery<Tuple> query = session.createNativeQuery(sb.toString(), Tuple.class).setParameter("branchId",
				request.getBranchId());

		List<Tuple> tupleList = query.getResultList();

		if (!CollectionUtils.isEmpty(tupleList)) {
			return CommonUtils.parseTupleList(tupleList);
		}
		return null;
	}

	public boolean updateStageLabel(SkillMatrixRequest request) {
		LOGGER.info("# SettingDaoImpl || updateStageLabel");
		Session session = getCurrentSession();
		boolean flag = false;

		if (request.getId() > 0) {

			Tuple tupleObj = smUtils.getSMStageLabelDetails(session, request.getId());

			SMStageLabel label = session.get(SMStageLabel.class, request.getId());

			smWorker.setStageLabelDetails(session, request, label);

			session.update(label);

			smAudit.updateStageLabelAudit(session, request, tupleObj);
			flag = true;
		} else {
			SMStageLabel label = new SMStageLabel();
			smWorker.setStageLabelDetails(session, request, label);

			session.save(label);
			flag = true;
		}
		return flag;
	}

	@Override
	public List<HashMap<String, Object>> getCellList(SkillMatrixRequest request) {
		LOGGER.info("# SettingDaoImpl || getCellList");

		Session session = getCurrentSession();

		StringBuilder sb = new StringBuilder(
				"SELECT ln.id AS lineId, ln.dept_id AS deptId, ln.name AS lineName, dp.dept_name AS deptName "
						+ "FROM dwm_line ln " + "INNER JOIN master_department dp ON dp.dept_id = ln.dept_id "
						+ "WHERE dp.branch_id=:branchId AND ln.is_active='Y' ");
		if (request.getDeptId() > 0) {
			sb.append("AND ln.dept_id=:deptId");
		}

		TypedQuery<Tuple> query = session.createNativeQuery(sb.toString(), Tuple.class).setParameter("branchId",
				request.getBranchId());
		if (request.getDeptId() > 0) {
			query.setParameter("deptId", request.getDeptId());
		}

		List<Tuple> tupleList = query.getResultList();

		if (!CollectionUtils.isEmpty(tupleList)) {
			return CommonUtils.parseTupleList(tupleList);
		}
		return null;
	}

	@Override
	public boolean addCategory(SkillMatrixRequest request) {
		LOGGER.info("# SettingDaoImpl || addCategory");
		Session session = getCurrentSession();
		boolean flag = false;
		SMCategory category = new SMCategory();

		if (request.getCategoryName() != null) {
			category.setCategoryName(request.getCategoryName());
		}
		if (request.getCreatedBy() > 0) {
			category.setCreatedBy(request.getCreatedBy());
		}
		if (request.getAssessmentId() > 0) {
			category.setAssessment(new SMAssessment(request.getAssessmentId()));
		}
		session.save(category);
		flag = true;
		return flag;
	}

	@Override
	public boolean updateCategory(SkillMatrixRequest request) {
		LOGGER.info("# SettingDaoImpl || updateCategory");
		Session session = getCurrentSession();
		boolean flag = false;

		Tuple tupleObj = smUtils.getCategoryDetails(session, request);

		if (request.getId() > 0) {
			SMCategory category = session.get(SMCategory.class, request.getId());

			if (request.getCategoryName() != null) {
				category.setCategoryName(request.getCategoryName());
			}
			if (request.getUpdatedBy() > 0) {
				category.setUpdatedBy(request.getUpdatedBy());
			}
			if (request.getAssessmentId() > 0) {
				category.setAssessment(new SMAssessment(request.getAssessmentId()));
			}

			session.update(category);
			smAudit.updateCategoryAudit(session, request, tupleObj);
			flag = true;

		}

		return flag;
	}

	@Override
	public boolean deleteCategory(SkillMatrixRequest request) {
		LOGGER.info("# SettingDaoImpl | deleteCategory");
		Session session = getCurrentSession();
		boolean flag = false;
		Tuple tupleObj = smUtils.getCategoryDetails(session, request);
		if (request.getId() > 0) {
			session.createNativeQuery("delete from sm_category where id=:id ").setParameter("id", request.getId())
					.executeUpdate();

			if (tupleObj != null) {
				smAudit.deleteCategory(session, tupleObj, request);
				flag = true;
			}

		}
		return flag;

	}

	@Override
	public List<HashMap<String, Object>> getCategoryList(SkillMatrixRequest request) {
		LOGGER.info("# SettingDaoImpl | getCategoryList");
		Session session = getCurrentSession();

		StringBuilder sb = new StringBuilder(" Select c.id as id,c.category_name as categoryName,\r\n"
				+ " c.assessment_id as assessmentId  from sm_category c  where c.assessment_id =:assessmentId ");

		TypedQuery<Tuple> query = session.createNativeQuery(sb.toString(), Tuple.class).setParameter("assessmentId",
				request.getAssessmentId());
		List<Tuple> tuplelist = query.getResultList();

		if (!CollectionUtils.isEmpty(tuplelist)) {
			return CommonUtils.parseTupleList(tuplelist);
		}

		return null;
	}

	// modify By rajdeep checksheer id returned
	@Override
	public SkillMatrixRequest saveCopyChecksheet(SkillMatrixRequest request) {
		LOGGER.info("# SettingDaoImpl || addCategory");
		Session session = getCurrentSession();

		// Save Checksheet
		SkillMatrixRequest obj = createChecksheet(request, session);
		if (obj != null && obj.getCheckSheetId() > 0) {
			request.setCheckSheetId(obj.getCheckSheetId());
			// request.setNoOfDays(obj.getNoOfDays());

			// Save Checksheet Key Point
			saveChecksheetPoint(request, session);

			// Save Checksheet Parameter
			saveChecksheetParameter(request, session);
			return obj;
		}
		return null;
	}

	@Override
	public boolean saveDocName(SkillMatrixRequest request) {
		LOGGER.info("# SettingDaoImpl | saveDocName");
		Session session = getCurrentSession();

		if (smUtils.isDocNameExist(session, request) > 0) {
			throw new EnovationException("Document name is already added");
		}

		SMDocName obj = new SMDocName();
		obj.setBranch(new BranchMaster(request.getBranchId()));
		obj.setDocName(request.getDocName());
		obj.setCreatedBy(request.getCreatedBy());
		session.save(obj);
		return true;
	}

	@Override
	public boolean updateDocName(SkillMatrixRequest request) {
		LOGGER.info("# SettingDaoImpl | saveDocName");
		Session session = getCurrentSession();

		Tuple tupleObj = smUtils.getDocNameDetailsById(session, request);

		SMDocName obj = session.get(SMDocName.class, request.getId());
		if (obj != null) {
			if (request.getDocName() != null) {
				obj.setDocName(request.getDocName());
			}
			obj.setUpdatedBy(request.getUpdatedBy());
			session.save(obj);
			if (obj != null) {
				smAudit.docNameAudit(session, tupleObj, obj, request, SMConstant.UPDATE);
			}
			return true;
		}
		return false;
	}

	@Override
	public boolean deleteDocName(SkillMatrixRequest request) {
		LOGGER.info("# SettingDaoImpl | deleteDocName");
		Session session = getCurrentSession();
		Tuple tupleObj = smUtils.getDocNameDetailsById(session, request);
		SMDocName obj = session.get(SMDocName.class, request.getId());
		session.delete(obj);
		if (obj != null) {
			smAudit.docNameAudit(session, tupleObj, obj, request, SMConstant.DELETE);
		}
		return true;
	}

	@Override
	public HashMap<String, Object> getDocName(SkillMatrixRequest request) {
		LOGGER.info("# SettingDaoImpl | getDocName");
		Session session = getCurrentSession();
		Tuple tupleList = smUtils.getDocNameByBranchId(request, session);
		if (tupleList != null) {
			return CommonUtils.parseSingleTupleRecord(tupleList);
		}
		return null;
	}

	public List<HashMap<String, Object>> getWorkstationMappingListByParentWorkstationId(SkillMatrixRequest request, SkillMatrixResponse response) {
		LOGGER.info("# SettingDaoImpl | getWorkstationMappingList");
		Session session = getCurrentSession();

		StringBuilder sql = new StringBuilder(
			"SELECT wm.id as id, wm.parent_workstation_id as parentWorkstationId, " +
			"parent.workstation as parentWorkstationName, " +
			"wm.child_workstation_id as childWorkstationId, " +
			"child.workstation as childWorkstationName, " +
			"wm.branch_id as branchId, b.name as branchName, " +
			"wm.dept_id as deptId, d.dept_name as deptName, " +
			"wm.line_id as lineId, l.name as lineName, " +
			"wm.is_active as isActive, " +
			"wm.created_by as createdBy, wm.created_date as createdDate, " +
			"wm.updated_by as updatedBy, wm.updated_date as updatedDate " +
			"FROM sm_workstation_mapping wm " +
			"INNER JOIN sm_workstations parent ON parent.id = wm.parent_workstation_id " +
			"INNER JOIN sm_workstations child ON child.id = wm.child_workstation_id " +
			"INNER JOIN master_branch b ON b.branch_id = wm.branch_id " +
			"INNER JOIN master_department d ON d.dept_id = wm.dept_id " +
			"LEFT JOIN dwm_line l ON l.id = wm.line_id " +
			"WHERE wm.is_active = 1 ");

		if (request.getParentWorkstationId() > 0) {
			sql.append(" AND wm.parent_workstation_id = :parentWorkstationId ");
		}
		if (request.getBranchId() > 0) {
			sql.append(" AND wm.branch_id = :branchId ");
		}
		if (request.getDeptId() > 0) {
			sql.append(" AND wm.dept_id = :deptId ");
		}
		if (request.getLineId() > 0) {
			sql.append(" AND wm.line_id = :lineId ");
		}
		if (request.getSearch() != null) {
			sql.append(" AND (parent.workstation LIKE :search OR child.workstation LIKE :search) ");
		}
		
		sql.append(" ORDER BY wm.id DESC");

		TypedQuery<Tuple> query = session.createNativeQuery(sql.toString(), Tuple.class);

		if (request.getParentWorkstationId() > 0) {
			query.setParameter("parentWorkstationId", request.getParentWorkstationId());
		}
		if (request.getBranchId() > 0) {
			query.setParameter("branchId", request.getBranchId());
		}
		if (request.getDeptId() > 0) {
			query.setParameter("deptId", request.getDeptId());
		}
		if (request.getLineId() > 0) {
			query.setParameter("lineId", request.getLineId());
		}
		if (request.getSearch() != null) {
			query.setParameter("search", "%" + request.getSearch() + "%");
		}

		if (request.getLimit() > 0) {
			query.setFirstResult(request.getOffset()).setMaxResults(request.getLimit());
		}

		List<Tuple> tupleList = query.getResultList();
		if (!CollectionUtils.isEmpty(tupleList)) {
			return CommonUtils.parseTupleList(tupleList);
		}

		return null;
	}

	@Override
	public List<HashMap<String, Object>> getAllWorkstationMapping() {
		LOGGER.info("# SettingDaoImpl | getAllWorkstationMapping");
		Session session = getCurrentSession();
	
		StringBuilder sql = new StringBuilder(
			"SELECT " +
			"  wm.branch_id AS branchId, b.name AS branchName, " +
			"  wm.dept_id AS deptId, d.dept_name AS deptName, " +
			"  wm.line_id AS lineId, l.name AS lineName, " +
			"  wm.parent_workstation_id AS parentWorkstationId, " +
			"  parent.workstation AS parentWorkstationName, " +
			"  MAX(wm.created_by) AS createdBy, MAX(wm.created_date) AS createdDate, " +
			"  MAX(wm.updated_by) AS updatedBy, MAX(wm.updated_date) AS updatedDate, " +
			"  wm.is_active AS isActive, " +
			"  CAST(JSON_ARRAYAGG( " +
			"    JSON_OBJECT( " +
			"      'childWorkstationId', wm.child_workstation_id, " +
			"      'childWorkstationName', child.workstation " +
			"    ) " +
			"  ) AS CHAR) AS childWorkstations " +  // Cast to CHAR to avoid issues in Hibernate Tuple
			"FROM sm_workstation_mapping wm " +
			"JOIN sm_workstations parent ON parent.id = wm.parent_workstation_id " +
			"JOIN sm_workstations child ON child.id = wm.child_workstation_id " +
			"JOIN master_branch b ON b.branch_id = wm.branch_id " +
			"JOIN master_department d ON d.dept_id = wm.dept_id " +
			"JOIN dwm_line l ON l.id = wm.line_id " +
			"WHERE wm.is_active = 1 " +
			"GROUP BY wm.branch_id, b.name, wm.dept_id, d.dept_name, " +
			"  wm.line_id, l.name, wm.parent_workstation_id, parent.workstation, wm.is_active " +
			"ORDER BY wm.branch_id, wm.parent_workstation_id DESC"
		);
		
	
		Query query = session.createNativeQuery(sql.toString(), Tuple.class);
		List<Tuple> tupleList = query.getResultList();
	
		return CollectionUtils.isEmpty(tupleList) ? null : CommonUtils.parseTupleList(tupleList);
	}	

}
