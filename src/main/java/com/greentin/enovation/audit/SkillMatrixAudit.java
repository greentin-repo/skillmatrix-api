package com.greentin.enovation.audit;

import java.util.List;

import javax.persistence.Tuple;

import org.hibernate.Session;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import com.greentin.enovation.audit.AuditTable.AuditCategory;
import com.greentin.enovation.dto.SMOJTPlanDTO;
import com.greentin.enovation.dto.SkillMatrixRequest;
import com.greentin.enovation.model.skillMatrix.AuditSMGapReason;
import com.greentin.enovation.model.skillMatrix.AuditSMMasterCertificate;
import com.greentin.enovation.model.skillMatrix.AuditSMModel;
import com.greentin.enovation.model.skillMatrix.AuditSMOJTPlan;
import com.greentin.enovation.model.skillMatrix.AuditSMOJTRegis;
import com.greentin.enovation.model.skillMatrix.AuditSMShifts;
import com.greentin.enovation.model.skillMatrix.AuditSMStageLabel;
import com.greentin.enovation.model.skillMatrix.AuditSMWorkflowConfig;
import com.greentin.enovation.model.skillMatrix.SMGapReason;
import com.greentin.enovation.model.skillMatrix.SMMasterCertificate;
import com.greentin.enovation.model.skillMatrix.SMModel;
import com.greentin.enovation.model.skillMatrix.SMShifts;
import com.greentin.enovation.model.skillMatrix.SMWorkflowConfig;
import com.greentin.enovation.model.skillMatrix.AuditSMAssessment;
import com.greentin.enovation.model.skillMatrix.AuditSMAssessmentOptions;
import com.greentin.enovation.model.skillMatrix.AuditSMAssessmentQuestions;
import com.greentin.enovation.model.skillMatrix.AuditSMCategory;
import com.greentin.enovation.model.skillMatrix.AuditSMChecksheet;
import com.greentin.enovation.model.skillMatrix.AuditSMChecksheetParameter;
import com.greentin.enovation.model.skillMatrix.AuditSMChecksheetPoint;
import com.greentin.enovation.model.skillMatrix.AuditSMConfig;
import com.greentin.enovation.model.skillMatrix.AuditSMDocName;
import com.greentin.enovation.model.skillMatrix.SMAssessment;
import com.greentin.enovation.model.skillMatrix.SMAssessmentOptions;
import com.greentin.enovation.model.skillMatrix.SMAssessmentQues;
import com.greentin.enovation.model.skillMatrix.SMChecksheet;
import com.greentin.enovation.model.skillMatrix.SMChecksheetParameter;
import com.greentin.enovation.model.skillMatrix.SMChecksheetPoints;
import com.greentin.enovation.model.skillMatrix.SMDocName;
import com.greentin.enovation.model.skillMatrix.AuditSMUserType;
import com.greentin.enovation.model.skillMatrix.AuditSMWorkstation;
import com.greentin.enovation.model.skillMatrix.SMWorkstations;
import com.greentin.enovation.utils.BaseRepository;
import com.greentin.enovation.utils.CommonUtils;
import com.greentin.enovation.utils.EnovationConstants;

@Component
public class SkillMatrixAudit extends BaseRepository {
	private static final Logger LOGGER = LoggerFactory.getLogger(SkillMatrixAudit.class);

	public void updateModelDetailsAudit(Session session, Tuple tuple, SkillMatrixRequest request) {
		LOGGER.info("# SkillMatrixAudit || updateModelDetailsAudit");

		AuditSMModel obj = new AuditSMModel();
		if (!request.getModelName().equals(CommonUtils.objectToString(tuple.get("modelName")))) {
			obj.setColName(AuditTable.AuditSMModel.MODEL_NAME);
			obj.setBranchId(CommonUtils.objectToInt(tuple.get("branchId")));
			obj.setAction(EnovationConstants.ACTION_UPDATE);
			obj.setNewValue(request.getModelName());
			obj.setOldValue(CommonUtils.objectToString(tuple.get("modelName")));
			obj.setActionBy(request.getUpdatedBy());

			session.save(obj);
		}
	}

	// Added by Sonali L. Jan 15 2024 - added field assessmentType
	public void updateSMAssessmentAudit(Session session, SMAssessment obj, Tuple tupleList) {

		LOGGER.info("#In SkillMatrixAudit |  INSIDE in SMAssessment ");
		if (obj.getTitle() != null && !obj.getTitle().equals(CommonUtils.objectToString(tupleList.get("title")))) {
			saveSMAssessmentAudit(session, obj, EnovationConstants.UPDATE_RECORD, AuditTable.AuditSMAssessment.TITLE,
					CommonUtils.objectToString(obj.getTitle()), CommonUtils.objectToString(tupleList.get("title")));
		}
		if (obj.getSkillLevel() != null && tupleList.get("skillLvlId") != null) {
			if ((obj.getSkillLevel().getId()) != (CommonUtils.objectToInt(tupleList.get("skillLvlId")))) {
				saveSMAssessmentAudit(session, obj, EnovationConstants.UPDATE_RECORD,
						AuditTable.AuditSMAssessment.SKIILLLEVEL,
						CommonUtils.objectToString(obj.getSkillLevel().getId()),
						CommonUtils.objectToString(tupleList.get("skillLvlId")));
			}
		}
		if (obj.getTime() != (CommonUtils.objectToInt(tupleList.get("time")))) {
			saveSMAssessmentAudit(session, obj, EnovationConstants.UPDATE_RECORD, AuditTable.AuditSMAssessment.TIME,
					CommonUtils.objectToString(obj.getTime()), CommonUtils.objectToString(tupleList.get("time")));
		}
		if (obj.getPassingMarks() != (CommonUtils.objectToDouble(tupleList.get("passingMarks")))) {
			saveSMAssessmentAudit(session, obj, EnovationConstants.UPDATE_RECORD,
					AuditTable.AuditSMAssessment.PASSINGMARKS, CommonUtils.objectToString(obj.getPassingMarks()),
					CommonUtils.objectToString(tupleList.get("passingMarks")));
		}
		if (obj.getAssessmentType() != null
				&& !obj.getAssessmentType().equals(CommonUtils.objectToString(tupleList.get("assessmentType")))) {
			saveSMAssessmentAudit(session, obj, EnovationConstants.UPDATE_RECORD,
					AuditTable.AuditSMAssessment.ASSESSMENTTYPE, CommonUtils.objectToString(obj.getAssessmentType()),
					CommonUtils.objectToString(tupleList.get("assessmentType")));
		}
	}

	private void saveSMAssessmentAudit(Session session, SMAssessment obj, String action, String columnName,
			String newData, String oldData) {
		LOGGER.info("#In SkillMatrixAudit |  INSIDE in saveSMAssessmentAudit ");
		AuditSMAssessment sa = new AuditSMAssessment();
		sa.setAssessmentId(obj.getId());
		sa.setAction(action);
		sa.setColumnName(columnName);
		sa.setNewValue(newData);
		sa.setOldValue(oldData);
		sa.setActionBy(obj.getUpdatedBy());
		session.save(sa);
	}

	public void updateSMAssessmentOptionsAudit(Session session, SMAssessmentOptions opt, Tuple optObj) {
		LOGGER.info("#In SkillMatrixAudit |  INSIDE in SMAssessmentOptionsAudit ");
		if (opt.getOption() != null && !opt.getOption().equals(CommonUtils.objectToString(optObj.get("opt")))) {
			saveSMAssessmentOptionsAudit(session, opt, EnovationConstants.UPDATE_RECORD,
					AuditTable.AuditSMAssessmentOptions.OPTION, CommonUtils.objectToString(opt.getOption()),
					CommonUtils.objectToString(optObj.get("opt")));
		}
		if (opt.getOption() != null
				&& !opt.getIsRightAns() == ((CommonUtils.objectToString(optObj.get("isRightAns"))).equals("false")
						? false
						: true)) {
			saveSMAssessmentOptionsAudit(session, opt, EnovationConstants.UPDATE_RECORD,
					AuditTable.AuditSMAssessmentOptions.ISRIGHTANS, CommonUtils.objectToString(opt.getIsRightAns()),
					CommonUtils.objectToString(optObj.get("isRightAns")));
		}

	}

	public void deleteModelDetailsAudit(Session session, SMModel oldObj, SkillMatrixRequest request) {
		LOGGER.info("# SkillMatrixAudit || deleteModelDetails");
		AuditSMModel obj = new AuditSMModel();

		obj.setBranchId(CommonUtils.objectToInt(oldObj.getBranch().getBranchId()));
		obj.setAction(EnovationConstants.ACTION_DELETE);
		obj.setOldValue(CommonUtils.objectToString(oldObj.getModelName()));
		obj.setActionBy(request.getUpdatedBy());

		session.save(obj);
	}

	public void updateGapReasonAudit(Session session, Tuple tupleObj, SkillMatrixRequest request) {
		LOGGER.info("# SkillMatrixAudit || updateGapReasonAudit");
		AuditSMGapReason obj = new AuditSMGapReason();

		if (!request.getReason().equals(CommonUtils.objectToString(tupleObj.get("reason")))) {
			obj.setColName(AuditTable.AuditSMGapReason.REASON);
			obj.setBranchId(CommonUtils.objectToInt(tupleObj.get("branchId")));
			obj.setAction(EnovationConstants.ACTION_UPDATE);
			obj.setNewValue(request.getReason());
			obj.setOldValue(CommonUtils.objectToString(tupleObj.get("reason")));
			obj.setActionBy(request.getUpdatedBy());

			session.save(obj);
		}
	}

	public void deleteGapReasonAudit(Session session, SMGapReason oldObj, SkillMatrixRequest request) {
		LOGGER.info("# SkillMatrixAudit || deleteGapReason");
		AuditSMGapReason obj = new AuditSMGapReason();

		obj.setBranchId(CommonUtils.objectToInt(oldObj.getBranch().getBranchId()));
		obj.setAction(EnovationConstants.ACTION_DELETE);
		obj.setOldValue(CommonUtils.objectToString(oldObj.getReason()));
		obj.setActionBy(request.getUpdatedBy());

		session.save(obj);
	}

	public void updateShiftDetailsAudit(Session session, Tuple tupleObj, SkillMatrixRequest request) {
		LOGGER.info("# SkillMatrixAudit || updateShiftDetailsAudit");
		AuditSMShifts obj = new AuditSMShifts();

		if (!request.getShiftName().equals(CommonUtils.objectToString(tupleObj.get("shiftName")))) {
			obj.setColName(AuditTable.AuditSMShifts.SHIFT_NAME);
			obj.setBranchId(CommonUtils.objectToInt(tupleObj.get("branchId")));
			obj.setAction(EnovationConstants.ACTION_UPDATE);
			obj.setNewValue(request.getShiftName());
			obj.setOldValue(CommonUtils.objectToString(tupleObj.get("shiftName")));
			obj.setActionBy(request.getUpdatedBy());

			session.save(obj);
		}
	}

	public void deleteShiftDetailsAudit(Session session, SMShifts oldObj, SkillMatrixRequest request) {
		LOGGER.info("# SkillMatrixAudit || deleteShiftDetailsAudit");
		AuditSMShifts obj = new AuditSMShifts();

		obj.setBranchId(CommonUtils.objectToInt(oldObj.getBranch().getBranchId()));
		obj.setAction(EnovationConstants.ACTION_DELETE);
		obj.setOldValue(CommonUtils.objectToString(oldObj.getShiftName()));
		obj.setActionBy(request.getUpdatedBy());

		session.save(obj);
	}

	public void masterWorkstation(Session session, SkillMatrixRequest request, Tuple tupleObj) {
		LOGGER.info("Inside SkillMatrixAudit | masterWorkstation ");

		if (!request.getWorkstation().equals(CommonUtils.objectToString(tupleObj.get("workstation")))) {
			insertIntoWorkstationAudit(session, AuditTable.SMWorkstation.WORKSTATION, request.getWorkstation(),
					CommonUtils.objectToString(tupleObj.get("workstation")), request.getUpdatedBy(),
					request.getDeptId());
		}
		if (request.getMachineIndex() != (CommonUtils.objectToDouble(tupleObj.get("machineIndex")))) {
			insertIntoWorkstationAudit(session, AuditTable.SMWorkstation.MACHINE_INDEX, request.getMachineIndex(),
					CommonUtils.objectToString(tupleObj.get("machineIndex")), request.getUpdatedBy(),
					request.getDeptId());
		}

		if ((request.getReqSkillLevelId() != (CommonUtils.objectToLong(tupleObj.get("reqSkillLevelId"))))) {
			insertIntoWorkstationAudit(session, AuditTable.SMWorkstation.REQ_SKILL_LEVELID,
					request.getReqSkillLevelId(), CommonUtils.objectToString(tupleObj.get("reqSkillLevelId")),
					request.getUpdatedBy(), request.getDeptId());
		}
		if ((request.getRequiredWorkforce() != (CommonUtils.objectToDouble(tupleObj.get("requiredWorkforce"))))) {
			insertIntoWorkstationAudit(session, AuditTable.SMWorkstation.REQUIRED_WORKFORCE,
					request.getRequiredWorkforce(), CommonUtils.objectToString(tupleObj.get("requiredWorkforce")),
					request.getUpdatedBy(), request.getDeptId());
		}
		if ((request.getBranchId() != (CommonUtils.objectToInt(tupleObj.get("branchId"))))) {
			insertIntoWorkstationAudit(session, AuditTable.SMWorkstation.BRANCH, request.getBranchId(),
					CommonUtils.objectToString(tupleObj.get("branchId")), request.getUpdatedBy(), request.getDeptId());
		}
		if ((request.getDeptId() != (CommonUtils.objectToInt(tupleObj.get("deptId"))))) {
			insertIntoWorkstationAudit(session, AuditTable.SMWorkstation.DEPT, request.getDeptId(),
					CommonUtils.objectToString(tupleObj.get("deptId")), request.getUpdatedBy(), request.getDeptId());
		}
		if ((request.getLineId() != (CommonUtils.objectToInt(tupleObj.get("lineId"))))) {
			insertIntoWorkstationAudit(session, AuditTable.SMWorkstation.LINE, request.getLineId(),
					CommonUtils.objectToString(tupleObj.get("lineId")), request.getUpdatedBy(), request.getDeptId());
		}
		if (request.getIsActive() != (CommonUtils.objectToString(tupleObj.get("isActive")).equals("false") ? true
				: false)) {

//			AuditSMWorkstation obj = new AuditSMWorkstation();
//			obj.setColName(AuditTable.SMWorkstation.IS_ACTIVE);
//			obj.setNewValue(CommonUtils.objectToString(request.getIsActive()));
//			obj.setOldValue(CommonUtils.objectToString(tupleObj.get("isActive")));
//			obj.setUpdatedBy(request.getUpdatedBy());
//			obj.setAction(EnovationConstants.UPDATE_STRING);
//			session.save(obj);
			insertIntoWorkstationAudit(session, AuditTable.SMWorkstation.IS_ACTIVE, CommonUtils.objectToString(request.getIsActive()),
					CommonUtils.objectToString(tupleObj.get("isActive")), request.getUpdatedBy(), request.getDeptId());
		

		}
		if ((request.getMachineCount() != (CommonUtils.objectToInt(tupleObj.get("machineCount"))))) {
			insertIntoWorkstationAudit(session, AuditTable.SMWorkstation.MACHINE_COUNT, request.getMachineCount(),
					CommonUtils.objectToString(tupleObj.get("machineCount")), request.getUpdatedBy(), request.getDeptId());
		}
		
	}

	private void insertIntoWorkstationAudit(Session session, String columnName, double newData, String oldData,
			int updatedBy, int deptId) {

		AuditSMWorkstation obj = new AuditSMWorkstation();
		obj.setColName(columnName);
		obj.setNewValue(CommonUtils.objectToString(newData));
		obj.setOldValue(oldData);
		obj.setUpdatedBy(updatedBy);
		obj.setAction(EnovationConstants.UPDATE_STRING);
		session.save(obj);

	}

	private void saveSMAssessmentOptionsAudit(Session session, SMAssessmentOptions opt, String action, String colName,
			String newData, String oldData) {
		LOGGER.info("#In SkillMatrixAudit |  INSIDE in saveSMAssessmentOptionsAudit ");

		AuditSMAssessmentOptions obj = new AuditSMAssessmentOptions();
		obj.setOptionId(opt.getId());
		obj.setAction(action);
		obj.setColumnName(colName);
		obj.setNewValue(newData);
		obj.setOldValue(oldData);
		obj.setActionBy(opt.getUpdatedBy());
		obj.setQuetionId(opt.getQues().getId());
		session.save(obj);
	}

	public void updateSMAssessmentQuesAudit(Session session, SMAssessmentQues obj, Tuple que, String action,
			SkillMatrixRequest request) {
		LOGGER.info("#In SkillMatrixAudit |  INSIDE in SMAssessmentQuesAudit ");
		if (obj != null) {
			if (obj.getQestion() != null && !obj.getQestion().equals(CommonUtils.objectToString(que.get("qestion")))) {
				updateSMAssessmentQuesAudit(session, action, AuditTable.AuditSMAssessmentQuestions.QUESTION,
						CommonUtils.objectToString(obj.getQestion()), CommonUtils.objectToString(que.get("qestion")),
						request.getUpdatedBy(), obj);
			}

			if (obj.getQestion() != null
					&& obj.getQuesType().getId() != (CommonUtils.objectToInt(que.get("queType")))) {
				updateSMAssessmentQuesAudit(session, action, AuditTable.AuditSMAssessmentQuestions.QUESTIONTYPEID,
						CommonUtils.objectToString(obj.getQuesType().getId()),
						CommonUtils.objectToString(que.get("queType")), request.getUpdatedBy(), obj);
			}
		}
	}

	public void deleteSMAssessmentQuesAudit(Session session, Tuple que, String action, SkillMatrixRequest request) {

		if (que != null) {
			long id = CommonUtils.objectToLong(que.get("id"));
			long assessmentId = CommonUtils.objectToLong(que.get("assessmentId"));

			deleteSMAssessmentQuesAudit(session, action, AuditTable.AuditSMAssessmentQuestions.QUESTION,
					CommonUtils.objectToString(que.get("qestion")), id, request.getUpdatedBy(), assessmentId);

			deleteSMAssessmentQuesAudit(session, action, AuditTable.AuditSMAssessmentQuestions.QUESTIONTYPEID,
					CommonUtils.objectToString(que.get("queType")), id, request.getUpdatedBy(), assessmentId);
		}
	}

	private void deleteSMAssessmentQuesAudit(Session session, String action, String colName, String oldData, long id,
			int actionBy, long assessmentId) {
		LOGGER.info("#In SkillMatrixAudit |  INSIDE in updateSMAssessmentQuesAudit ");

		AuditSMAssessmentQuestions que = new AuditSMAssessmentQuestions();
		que.setQuetionId(id);
		que.setAction(action);
		que.setColumnName(colName);
		que.setOldValue(oldData);
		que.setActionBy(actionBy);
		que.setAssessmentId(assessmentId);

		session.save(que);

	}

	public void deActiveSMAssessmentQuesAudit(Session session, long id, String action, SkillMatrixRequest request) {
		AuditSMAssessmentQuestions queObj = new AuditSMAssessmentQuestions();
		queObj.setQuetionId(id);
		queObj.setAction(action);
		queObj.setColumnName(AuditTable.AuditSMAssessmentQuestions.ISACTIVE);
		queObj.setNewValue("0");
		queObj.setOldValue("1");
		queObj.setActionBy(request.getCreatedBy());

		session.save(queObj);
	}

	private void updateSMAssessmentQuesAudit(Session session, String action, String colName, String newData,
			String oldData, int actionBy, SMAssessmentQues obj) {
		LOGGER.info("#In SkillMatrixAudit |  INSIDE in updateSMAssessmentQuesAudit ");

		AuditSMAssessmentQuestions que = new AuditSMAssessmentQuestions();
		que.setQuetionId(obj.getId());
		que.setAction(action);
		que.setColumnName(colName);
		que.setNewValue(newData);
		que.setOldValue(oldData);
		que.setActionBy(actionBy);
		que.setAssessmentId(obj.getAssessment().getId());

		session.save(que);

	}

	public void deleteSMAssessmentOptionsAudit(Session session, Tuple optObj, SkillMatrixRequest request) {
		LOGGER.info("#In SkillMatrixAudit |  INSIDE in deleteSMAssessmentOptionsAudit ");

		AuditSMAssessmentOptions obj = new AuditSMAssessmentOptions();
		obj.setOptionId(request.getAssessmentQueOptId());
		obj.setAction(EnovationConstants.DELETE_RECORD);
		obj.setColumnName("option");
		obj.setOldValue(CommonUtils.objectToString(optObj.get("opt")));
		obj.setActionBy(request.getCreatedBy());
		obj.setQuetionId(CommonUtils.objectToLong(optObj.get("questionId")));
		session.save(obj);
	}

	public void deActiveSMAssessmentAudit(Session session, SkillMatrixRequest request) {
		LOGGER.info("#In SkillMatrixAudit |  INSIDE in deActiveSMAssessmentAudit ");
		AuditSMAssessment sa = new AuditSMAssessment();
		sa.setAssessmentId(request.getAssessmentId());
		sa.setAction(EnovationConstants.DEACTIVE_STRING);
		sa.setColumnName(AuditTable.AuditSMAssessment.ISACTIVE);
		sa.setNewValue("1");
		sa.setOldValue("0");
		sa.setActionBy(request.getCreatedBy());
		session.save(sa);
	}

	public void updateSMChecksheetAudit(Session session, Tuple tupleList, SkillMatrixRequest obj, SMChecksheet obj2) {
		LOGGER.info("# SkillMatrixAudit | updateSMChecksheetAudit");
		if (!CommonUtils.objectToString(tupleList.get("title")).equals(obj2.getTitle())) {
			updateSMChecksheetAudit(session, obj.getUpdatedBy(), obj2.getTitle(),
					CommonUtils.objectToString(tupleList.get("title")), EnovationConstants.UPDATE_RECORD,
					AuditTable.AuditSMChecksheet.TITLE, obj.getCheckSheetId());
		}

		if (CommonUtils.objectToInt(tupleList.get("noOfDays")) != (obj2.getNoOfDays())) {
			updateSMChecksheetAudit(session, obj.getUpdatedBy(), CommonUtils.objectToString(obj2.getNoOfDays()),
					CommonUtils.objectToString(tupleList.get("noOfDays")), EnovationConstants.UPDATE_RECORD,
					AuditTable.AuditSMChecksheet.NOOFDAYS, obj.getCheckSheetId());
		}

		if (CommonUtils.objectToInt(tupleList.get("skillLvlId")) != (obj2.getSkillLevel().getId())) {
			updateSMChecksheetAudit(session, obj.getUpdatedBy(),
					CommonUtils.objectToString(obj2.getSkillLevel().getId()),
					CommonUtils.objectToString(tupleList.get("skillLvlId")), EnovationConstants.UPDATE_RECORD,
					AuditTable.AuditSMChecksheet.SKIILLLEVEL, obj.getCheckSheetId());
		}

	}

	private void updateSMChecksheetAudit(Session session, int updatedBy, String newValue, String oldValue,
			String updateRecord, String title, long id) {
		LOGGER.info("# SkillMatrixAudit | updateSMChecksheetAudit");
		// TODO Auto-generated method stub

		AuditSMChecksheet sm = new AuditSMChecksheet();
		sm.setAction(updateRecord);
		sm.setActionBy(updatedBy);
		sm.setCheckSheetId(id);
		sm.setOldValue(oldValue);
		sm.setNewValue(newValue);
		sm.setColumnName(title);
		session.save(sm);

	}

	public void deActiveSMChecksheetAudit(Session session, SkillMatrixRequest request) {
		LOGGER.info("# SkillMatrixAudit | deActiveSMChecksheetAudit");
		AuditSMChecksheet sm = new AuditSMChecksheet();
		sm.setAction(EnovationConstants.DEACTIVE_STRING);
		sm.setActionBy(request.getUpdatedBy());
		sm.setCheckSheetId(request.getCheckSheetId());
		sm.setOldValue("0");
		sm.setNewValue("1");
		sm.setColumnName(AuditTable.AuditSMChecksheet.ISACTIVE);
		session.save(sm);

	}

	public void deleteSMChecksheetAudit(Session session, Tuple tupleList, SkillMatrixRequest request) {
		LOGGER.info("# SkillMatrixAudit | deleteSMChecksheetAudit");

		deleteSMChecksheetAudit(session, request.getUpdatedBy(), CommonUtils.objectToString(tupleList.get("title")),
				EnovationConstants.DELETE_STRING, AuditTable.AuditSMChecksheet.TITLE, request.getCheckSheetId());

		deleteSMChecksheetAudit(session, request.getUpdatedBy(), CommonUtils.objectToString(tupleList.get("noOfDays")),
				EnovationConstants.DELETE_STRING, AuditTable.AuditSMChecksheet.NOOFDAYS, request.getCheckSheetId());

		deleteSMChecksheetAudit(session, request.getUpdatedBy(),
				CommonUtils.objectToString(tupleList.get("skillLvlId")), EnovationConstants.DELETE_STRING,
				AuditTable.AuditSMChecksheet.SKIILLLEVEL, request.getCheckSheetId());

	}

	private void deleteSMChecksheetAudit(Session session, int updatedBy, String oldData, String action, String colName,
			long checkSheetId) {
		LOGGER.info("# SkillMatrixAudit | deleteSMChecksheetAudit");

		AuditSMChecksheet sm = new AuditSMChecksheet();
		sm.setAction(action);
		sm.setActionBy(updatedBy);
		sm.setCheckSheetId(checkSheetId);
		sm.setOldValue(oldData);
		sm.setColumnName(colName);
		session.save(sm);
	}

	public void updateSMChecksheetPointAudit(Session session, Tuple tupleList, SkillMatrixRequest request,
			SkillMatrixRequest point, SkillMatrixRequest day) {
		LOGGER.info("# SkillMatrixAudit | updateSMChecksheetPointAudit");
		if (tupleList != null) {
			if (!point.getItemName().equals(CommonUtils.objectToString(tupleList.get("itemName")))) {
				updateSMChecksheetPointAudit(session, AuditTable.AuditSMChecksheetPoint.ITEMNAME,
						EnovationConstants.UPDATE_RECORD, request.getUpdatedBy(), point.getId(), point.getItemName(),
						CommonUtils.objectToString(tupleList.get("itemName")));
			}
			if (tupleList != null) {
				if (!point.getReference().equals(CommonUtils.objectToString(tupleList.get("reference")))) {
					updateSMChecksheetPointAudit(session, AuditTable.AuditSMChecksheetPoint.REFERENCE,
							EnovationConstants.UPDATE_RECORD, request.getUpdatedBy(), point.getId(),
							point.getReference(), CommonUtils.objectToString(tupleList.get("reference")));
				}

				if (day.getDayNo() != (CommonUtils.objectToInt(tupleList.get("dayNo")))) {
					updateSMChecksheetPointAudit(session, AuditTable.AuditSMChecksheetPoint.DAYNO,
							EnovationConstants.UPDATE_RECORD, request.getUpdatedBy(), point.getId(),
							CommonUtils.objectToString(day.getDayNo()),
							CommonUtils.objectToString(tupleList.get("dayNo")));
				}
			}
		}

	}

	private void updateSMChecksheetPointAudit(Session session, String colName, String action, int updatedBy, long id,
			String newData, String oldData) {
		LOGGER.info("# SkillMatrixAudit | updateSMChecksheetPointAudit | save");
		AuditSMChecksheetPoint obj = new AuditSMChecksheetPoint();
		obj.setAction(action);
		obj.setActionBy(updatedBy);
		obj.setColumnName(colName);
		obj.setNewValue(newData);
		obj.setOldValue(oldData);
		obj.setCheckSheetPointId(id);
		session.save(obj);

	}

	public void deleteSMChecksheetPointAudit(Session session, Tuple tupleList, SkillMatrixRequest request) {
		LOGGER.info("# SkillMatrixAudit | deleteSMChecksheetPointAudit");

		deleteSMChecksheetPointAudit(session, AuditTable.AuditSMChecksheetPoint.ITEMNAME,
				EnovationConstants.DELETE_STRING, request.getUpdatedBy(), request.getId(),
				CommonUtils.objectToString(tupleList.get("itemName")));

		deleteSMChecksheetPointAudit(session, AuditTable.AuditSMChecksheetPoint.DAYNO, EnovationConstants.DELETE_STRING,
				request.getUpdatedBy(), request.getId(), CommonUtils.objectToString(tupleList.get("dayNo")));
	}

	private void deleteSMChecksheetPointAudit(Session session, String colName, String action, int updatedBy, long id,
			String oldData) {
		LOGGER.info("# SkillMatrixAudit | deleteSMChecksheetPointAudit");
		AuditSMChecksheetPoint obj = new AuditSMChecksheetPoint();
		obj.setAction(action);
		obj.setActionBy(updatedBy);
		obj.setColumnName(colName);
		obj.setOldValue(oldData);
		obj.setCheckSheetPointId(id);
		session.save(obj);
	}

	private void insertIntoWorkstationAudit(Session session, String columnName, long newValue, String oldValue,
			int updatedBy, int deptId) {
		LOGGER.info("Inside SkillMatrixAudit | insertIntoWorkstationAudit");

		AuditSMWorkstation obj = new AuditSMWorkstation();
		obj.setColName(columnName);
		obj.setNewValue(CommonUtils.objectToString(newValue));
		obj.setOldValue(oldValue);
		obj.setUpdatedBy(updatedBy);
		obj.setAction(EnovationConstants.UPDATE_STRING);
		session.save(obj);
	}

	public void deActiveSMChecksheetPointAudit(Session session, SkillMatrixRequest request) {
		LOGGER.info("# SkillMatrixAudit | deActiveSMChecksheetPointAudit");
		AuditSMChecksheetPoint obj = new AuditSMChecksheetPoint();
		obj.setAction(EnovationConstants.DEACTIVE_STRING);
		obj.setActionBy(request.getUpdatedBy());
		obj.setColumnName(AuditTable.AuditSMChecksheet.ISACTIVE);
		obj.setOldValue("1");
		obj.setNewValue("0");
		obj.setCheckSheetPointId(request.getId());
		session.save(obj);
	}

	private void insertIntoWorkstationAudit(Session session, String columnName, String newValue, String oldValue,
			int actionBy, int deptId) {
		LOGGER.info("Inside SkillMatrixAudit | insertIntoWorkstationAudit");

		AuditSMWorkstation obj = new AuditSMWorkstation();
		obj.setColName(columnName);
		obj.setNewValue(newValue);
		obj.setOldValue(oldValue);
		obj.setUpdatedBy(actionBy);
		obj.setAction(EnovationConstants.UPDATE_STRING);
		session.save(obj);

	}

	public void deActiveShiftDetailsAudit(Session session, SMShifts oldObj, SkillMatrixRequest request) {
		LOGGER.info("# SkillMatrixAudit || deActiveShiftDetailsAudit");
		AuditSMShifts obj = new AuditSMShifts();

		obj.setColName(AuditTable.AuditSMShifts.IS_ACTIVE);
		obj.setBranchId(CommonUtils.objectToInt(oldObj.getBranch().getBranchId()));
		obj.setAction(EnovationConstants.ACTION_DELETE);
		obj.setOldValue(CommonUtils.objectToString(oldObj.getIsActive()));
		obj.setActionBy(request.getUpdatedBy());

		session.save(obj);

	}

	public void deleteWorkflowConfigAudit(Session session, SMWorkflowConfig oldObj, SkillMatrixRequest request) {
		LOGGER.info("# SkillMatrixAudit || deleteWorkflowConfigAudit");
		AuditSMWorkflowConfig obj = new AuditSMWorkflowConfig();

		obj.setBranchId(CommonUtils.objectToInt(oldObj.getBranch().getBranchId()));
		obj.setAction(EnovationConstants.ACTION_DELETE);
		obj.setOldValue(CommonUtils.objectToString(oldObj.getStage().getId()));
		obj.setActionBy(request.getUpdatedBy());

		session.save(obj);

	}

	public void deActiveWorkflowConfigAudit(Session session, SMWorkflowConfig oldObj, SkillMatrixRequest request) {
		LOGGER.info("# SkillMatrixAudit || deActiveWorkflowConfigAudit");
		AuditSMWorkflowConfig obj = new AuditSMWorkflowConfig();

		obj.setColName(AuditTable.AuditSMWorkflowConfig.IS_ACTIVE);
		obj.setBranchId(CommonUtils.objectToInt(oldObj.getBranch().getBranchId()));
		obj.setAction(EnovationConstants.ACTION_DELETE);
		obj.setOldValue(CommonUtils.objectToString(oldObj.getIsActive()));
		obj.setActionBy(request.getUpdatedBy());

		session.save(obj);
	}

	public void deleteUserType(Session session, Tuple tupleObj, SkillMatrixRequest request) {
		AuditSMUserType object = new AuditSMUserType();

		object.setBranchId(CommonUtils.objectToLong(tupleObj.get("branchId")));
		object.setOldValue(CommonUtils.objectToString((tupleObj).get("userTypeId")));
		object.setAction(EnovationConstants.DELETE_STRING);
		object.setUpdatedBy(request.getUpdatedBy());
		session.save(object);

	}

	public void deleteWorkstation(Session session, Tuple obj, SkillMatrixRequest request) {

		LOGGER.info("Inside SkillMatrixAudit | deleteWorkstation");

		AuditSMWorkstation work = new AuditSMWorkstation();

		work.setReqSkillLevelId(CommonUtils.objectToLong(obj.get("reqSkillLevelId")));
		work.setAction(EnovationConstants.DELETE_STRING);
		work.setOldValue(CommonUtils.objectToString(obj.get("workstation")));
		work.setUpdatedBy(request.getUpdatedBy());

		session.save(work);

	}

	public void deActivateWorkstation(Session session, Tuple obj, SkillMatrixRequest request) {
		LOGGER.info("Inside SkillMatrixAudit | deActivateWorkstation");

		AuditSMWorkstation object = new AuditSMWorkstation();
		object.setColName(AuditTable.SMWorkstation.IS_ACTIVE);
		object.setAction(EnovationConstants.DEACTIVE_STRING);
		object.setNewValue("0");
		object.setOldValue("1");
		object.setActionBy(request.getUpdatedBy());
		session.save(object);

	}

	public void updateSMChecksheetParameterAudit(Session session, Tuple tupleList, SMChecksheetParameter obj) {
		LOGGER.info("# SkillMatrixAudit | updateSMChecksheetPointAudit");
		if (tupleList != null) {
			if (obj != null && !obj.getParameter().equals(CommonUtils.objectToString(tupleList.get("parameter")))) {
				setSMChecksheetParameterAudit(session, obj.getParameter(),
						CommonUtils.objectToString(tupleList.get("parameter")), obj.getUpdatedBy(),
						AuditTable.SMChecksheetParameter.PARAMETER, EnovationConstants.UPDATE_STRING);
			}
			if (obj != null && (obj.getParameterType() != null
					? obj.getParameterType().getId() != (CommonUtils.objectToInt(tupleList.get("parameterTypeId")))
					: false)) {
				setSMChecksheetParameterAudit(session, CommonUtils.objectToString(obj.getParameterType().getId()),
						CommonUtils.objectToString(tupleList.get("parameterTypeId")), obj.getUpdatedBy(),
						AuditTable.SMChecksheetParameter.PARAMETERTYPE, EnovationConstants.UPDATE_STRING);
			}
		}
	}

	private void setSMChecksheetParameterAudit(Session session, String newValue, String oldValue, int updatedBy,
			String colName, String action) {
		LOGGER.info("# SkillMatrixAudit | setSMChecksheetParameterAudit");
		AuditSMChecksheetParameter obj = new AuditSMChecksheetParameter();
		obj.setColumnName(colName);
		obj.setAction(action);
		obj.setActionBy(updatedBy);
		obj.setNewValue(newValue);
		obj.setOldValue(oldValue);
		session.save(obj);

	}

	public void deActiveSMChecksheetParameterAudit(Session session, SkillMatrixRequest request) {
		LOGGER.info("# SkillMatrixAudit | deActiveSMChecksheetParameterAudit");
		AuditSMChecksheetParameter object = new AuditSMChecksheetParameter();
		object.setColumnName(AuditTable.SMWorkstation.IS_ACTIVE);
		object.setNewValue("0");
		object.setOldValue("1");
		object.setAction(EnovationConstants.DEACTIVE_STRING);
		object.setActionBy(request.getUpdatedBy());
		session.save(object);
	}

	public void deleteMasterCertificateAudit(Session session, Tuple tupleList, SkillMatrixRequest request) {
		LOGGER.info("# SkillMatrixAudit | deleteMasterCertificateAudit");

		AuditSMMasterCertificate obj = new AuditSMMasterCertificate();

		obj.setAction(EnovationConstants.ACTION_DELETE);
		obj.setOldValue(CommonUtils.objectToString(tupleList.get("certificatePath")));
		obj.setActionBy(request.getUpdatedBy());
		session.save(obj);
	}

	public void updateMasterCertificateAudit(Session session, SkillMatrixRequest request, Tuple tupleObj, String path) {
		LOGGER.info("# SkillMatrixAudit | updateMasterCertificateAudit");

		if (request.getCertificateCaption() != null && (!request.getCertificateCaption()
				.equals(CommonUtils.objectToString(tupleObj.get("certificateCaption"))))) {

			insertIntoSMMasterCertificateAudit(session, AuditTable.AuditSMMasterCertificate.CERTIFICATE_CAPTION,
					request.getCertificateCaption(), CommonUtils.objectToString(tupleObj.get("certificateCaption")),
					request.getUpdatedBy());
		}

		if (request.getCertificateName() != null && (!request.getCertificateName()
				.equals(CommonUtils.objectToString(tupleObj.get("certificateName"))))) {

			insertIntoSMMasterCertificateAudit(session, AuditTable.AuditSMMasterCertificate.CERTIFICATE_NAME,
					request.getCertificateName(), CommonUtils.objectToString(tupleObj.get("certificateName")),
					request.getUpdatedBy());
		}

		if (request.getCertificatePath() != null && (!path.equals(CommonUtils.objectToString(tupleObj.get("path"))))) {

			insertIntoSMMasterCertificateAudit(session, AuditTable.AuditSMMasterCertificate.CERTIFICATE_PATH, path,
					CommonUtils.objectToString(tupleObj.get("path")), request.getUpdatedBy());
		}

		if (request.getStatus() != null
				&& (!request.getStatus().equals(CommonUtils.objectToString(tupleObj.get("status"))))) {

			insertIntoSMMasterCertificateAudit(session, AuditTable.AuditSMMasterCertificate.STATUS, request.getStatus(),
					CommonUtils.objectToString(tupleObj.get("status")), request.getUpdatedBy());
		}
	}

	private void insertIntoSMMasterCertificateAudit(Session session, String columnName, String newValue,
			String oldValue, int actionBy) {
		LOGGER.info("# SkillMatrixAudit || insertIntoSMMasterCertificateAudit");

		AuditSMMasterCertificate obj = new AuditSMMasterCertificate();

		obj.setAction(EnovationConstants.ACTION_UPDATE);
		obj.setColumnName(columnName);
		obj.setOldValue(oldValue);
		obj.setNewValue(newValue);
		obj.setActionBy(actionBy);

		session.save(obj);

	}

	public void deleteSMChecksheetParameterAudit(Session session, Tuple tupleList, SkillMatrixRequest request) {
		LOGGER.info("# SkillMatrixAudit | deleteSMChecksheetParameterAudit");

		setDeleteSMChecksheetParameterAudit(session, CommonUtils.objectToString(tupleList.get("parameter")),
				request.getUpdatedBy(), AuditTable.SMChecksheetParameter.PARAMETER, EnovationConstants.DELETE_STRING);

		setDeleteSMChecksheetParameterAudit(session, CommonUtils.objectToString(tupleList.get("parameterTypeId")),
				request.getUpdatedBy(), AuditTable.SMChecksheetParameter.PARAMETERTYPE,
				EnovationConstants.UPDATE_STRING);
	}

	private void setDeleteSMChecksheetParameterAudit(Session session, String oldValue, int updatedBy, String colName,
			String action) {
		LOGGER.info("# SkillMatrixAudit | setDeleteSMChecksheetParameterAudit");
		AuditSMChecksheetParameter obj = new AuditSMChecksheetParameter();
		obj.setColumnName(colName);
		obj.setAction(action);
		obj.setActionBy(updatedBy);
		obj.setOldValue(oldValue);
		session.save(obj);
	}

	public void updateUserType(Session session, SkillMatrixRequest request, Tuple tupleObj) {
		LOGGER.info("# SkillMatrixAudit || updateUserType");

		if ((request.getBranchId() != (CommonUtils.objectToInt(tupleObj.get("branchId"))))) {
			insertIntoUserTypeAudit(session, AuditTable.AuditSMUserType.BRANCH, request.getBranchId(),
					CommonUtils.objectToString(tupleObj.get("branchId")), request.getUpdatedBy(), request.getId());
		}
		if ((request.getDeptId() != (CommonUtils.objectToInt(tupleObj.get("deptId"))))) {
			insertIntoUserTypeAudit(session, AuditTable.AuditSMUserType.DEPT, request.getDeptId(),
					CommonUtils.objectToString(tupleObj.get("deptId")), request.getUpdatedBy(), request.getId());
		}
		if ((request.getLineId() != (CommonUtils.objectToInt(tupleObj.get("lineId"))))) {
			insertIntoUserTypeAudit(session, AuditTable.AuditSMUserType.LINE, request.getLineId(),
					CommonUtils.objectToString(tupleObj.get("lineId")), request.getUpdatedBy(), request.getId());
		}
		if (request.getIsActive() != (CommonUtils.objectToString(tupleObj.get("isActive")).equals("false") ? true
				: false)) {

			AuditSMUserType obj = new AuditSMUserType();

			obj.setColName(AuditTable.AuditSMUserType.ISACTIVE);
			obj.setAction(EnovationConstants.UPDATE_RECORD);
			obj.setNewValue(CommonUtils.objectToString(request.getIsActive()));
			obj.setOldValue(CommonUtils.objectToString(tupleObj.get("isActive")));
			obj.setUpdatedBy(request.getUpdatedBy());

			session.save(obj);
		}

	}

	private void insertIntoUserTypeAudit(Session session, String columnName, long l, String oldValue, int updatedBy,
			long id) {
		LOGGER.info("Inside SkillMatrixAudit | insertIntoWorkstationAudit");

		AuditSMUserType obj = new AuditSMUserType();
		obj.setColName(columnName);
		obj.setNewValue(CommonUtils.objectToString(l));
		obj.setOldValue(oldValue);
		obj.setUpdatedBy(updatedBy);
		obj.setAction(EnovationConstants.UPDATE_STRING);
		session.save(obj);
	}

	public void deleteSMOJTPlanAudit(Session session, long planId, Tuple tupleList) {
		LOGGER.info("# SkillMatrixAudit | updateSMChecksheetPointAudit");
		if (tupleList != null) {
			smOJTPlanAudit(session, planId, CommonUtils.objectToString(tupleList.get("branchId")), 0,
					AuditTable.SMOJTPlan.BRANCH, EnovationConstants.DELETE_STRING);
			smOJTPlanAudit(session, planId, CommonUtils.objectToString(tupleList.get("deptId")), 0,
					AuditTable.SMOJTPlan.DEPT, EnovationConstants.DELETE_STRING);
			smOJTPlanAudit(session, planId, CommonUtils.objectToString(tupleList.get("monthValue")), 0,
					AuditTable.SMOJTPlan.MONTHVALUE, EnovationConstants.DELETE_STRING);
			smOJTPlanAudit(session, planId, CommonUtils.objectToString(tupleList.get("yearValue")), 0,
					AuditTable.SMOJTPlan.YEARVALUE, EnovationConstants.DELETE_STRING);
			smOJTPlanAudit(session, planId, CommonUtils.objectToString(tupleList.get("startDate")), 0,
					AuditTable.SMOJTPlan.STARTDATE, EnovationConstants.DELETE_STRING);
			smOJTPlanAudit(session, planId, CommonUtils.objectToString(tupleList.get("status")), 0,
					AuditTable.SMOJTPlan.STATUS, EnovationConstants.DELETE_STRING);

		}
	}

	private void smOJTPlanAudit(Session session, long planId, String oldValue, int updatedBy, String colName,
			String action) {
		LOGGER.info("# SkillMatrixAudit | setSMChecksheetParameterAudit");
		AuditSMOJTPlan obj = new AuditSMOJTPlan();
		obj.setColumnName(colName);
		obj.setAction(action);
		obj.setActionBy(updatedBy);
		obj.setOldValue(oldValue);
		obj.setPlanId(planId);
		session.save(obj);

	}

	private void smOJTPlanAudit(Session session, long planId, String oldValue, String newValue, int updatedBy,
			String colName, String action) {
		LOGGER.info("# SkillMatrixAudit | setSMChecksheetParameterAudit");
		AuditSMOJTPlan obj = new AuditSMOJTPlan();
		obj.setColumnName(colName);
		obj.setAction(action);
		obj.setActionBy(updatedBy);
		obj.setOldValue(oldValue);
		obj.setNewValue(newValue);
		obj.setPlanId(planId);
		session.save(obj);

	}

	public void updateSMOJTPlanAudit(Session session, SkillMatrixRequest request, Tuple tupleList) {
		if (tupleList != null) {
			if (request.getStartDate() != null
					&& (!request.getStartDate().equals(CommonUtils.objectToString(tupleList.get("startDate"))))) {
				smOJTPlanAudit(session, request.getOjtPlanId(), CommonUtils.objectToString(tupleList.get("startDate")),
						CommonUtils.objectToString(request.getStartDate()), request.getUpdatedBy(),
						AuditTable.SMOJTPlan.STARTDATE, EnovationConstants.UPDATE_STRING);
			}
			if (request.getMonthValue() != (CommonUtils.objectToInt(tupleList.get("monthValue")))) {
				smOJTPlanAudit(session, request.getOjtPlanId(), CommonUtils.objectToString(tupleList.get("monthValue")),
						CommonUtils.objectToString(request.getMonthValue()), request.getUpdatedBy(),
						AuditTable.SMOJTPlan.MONTHVALUE, EnovationConstants.UPDATE_STRING);
			}
			if (request.getYearValue() != CommonUtils.objectToInt(tupleList.get("yearValue"))) {
				smOJTPlanAudit(session, request.getOjtPlanId(), CommonUtils.objectToString(tupleList.get("yearValue")),
						CommonUtils.objectToString(request.getYearValue()), request.getUpdatedBy(),
						AuditTable.SMOJTPlan.YEARVALUE, EnovationConstants.UPDATE_STRING);
			}
			if (request.getStatus() != null
					&& (!request.getStatus().equals(CommonUtils.objectToString(tupleList.get("status"))))) {
				smOJTPlanAudit(session, request.getOjtPlanId(), CommonUtils.objectToString(tupleList.get("status")),
						CommonUtils.objectToString(request.getStatus()), request.getUpdatedBy(),
						AuditTable.SMOJTPlan.STATUS, EnovationConstants.UPDATE_STRING);
			}
		}
	}

	public void updateSMOJTRegisAudit(Session session, SMOJTPlanDTO request, List<Tuple> tupleList) {
		if (!CollectionUtils.isEmpty(tupleList)) {
			for (Tuple tuple : tupleList) {
				if (request.getStatus() != null
						&& request.getStatus().equals(CommonUtils.objectToString(tuple.get("status")))) {
					smOJTRegisAudit(session, request.getOjtRegisId(), CommonUtils.objectToString(tuple.get("status")),
							CommonUtils.objectToString(request.getStatus()), request.getUpdatedBy(),
							AuditTable.SMOJTRegis.STATUS, EnovationConstants.UPDATE_STRING);
				}
				if (request.getTrainerEmpId() != CommonUtils.objectToInt(tuple.get("trainerId"))) {
					smOJTRegisAudit(session, request.getOjtRegisId(),
							CommonUtils.objectToString(tuple.get("trainerId")),
							CommonUtils.objectToString(request.getTrainerEmpId()), request.getUpdatedBy(),
							AuditTable.SMOJTRegis.TRAINEREMPID, EnovationConstants.UPDATE_STRING);
				}
				if (request.getDesiredSkillLevelId() != CommonUtils.objectToLong(tuple.get("desiredLvl"))) {
					smOJTRegisAudit(session, request.getOjtRegisId(),
							CommonUtils.objectToString(tuple.get("desiredLvl")),
							CommonUtils.objectToString(request.getDesiredSkillLevelId()), request.getUpdatedBy(),
							AuditTable.SMOJTRegis.DESIREDLVLID, EnovationConstants.UPDATE_STRING);
				}
				if (request.getWorkstationId() != CommonUtils.objectToLong(tuple.get("workstationId"))) {
					smOJTRegisAudit(session, request.getOjtRegisId(),
							CommonUtils.objectToString(tuple.get("workstationId")),
							CommonUtils.objectToString(request.getWorkstationId()), request.getUpdatedBy(),
							AuditTable.SMOJTRegis.WORKSTATIONID, EnovationConstants.UPDATE_STRING);
				}
				if (request.getOjtPlanId() != CommonUtils.objectToLong(tuple.get("planId"))) {
					smOJTRegisAudit(session, request.getOjtRegisId(), CommonUtils.objectToString(tuple.get("planId")),
							CommonUtils.objectToString(request.getOjtPlanId()), request.getUpdatedBy(),
							AuditTable.SMOJTRegis.PLANID, EnovationConstants.UPDATE_STRING);
				}
				if (request.getEmpId() != CommonUtils.objectToInt(tuple.get("empId"))) {
					smOJTRegisAudit(session, request.getOjtRegisId(), CommonUtils.objectToString(tuple.get("empId")),
							CommonUtils.objectToString(request.getEmpId()), request.getUpdatedBy(),
							AuditTable.SMOJTRegis.EMPID, EnovationConstants.UPDATE_STRING);
				}
			}
		}
	}

	private void smOJTRegisAudit(Session session, long regisId, String oldValue, String newValue, int updatedBy,
			String colName, String action) {
		LOGGER.info("# SkillMatrixAudit | setSMChecksheetParameterAudit");
		AuditSMOJTRegis obj = new AuditSMOJTRegis();
		obj.setColumnName(colName);
		obj.setAction(action);
		obj.setActionBy(updatedBy);
		obj.setOldValue(oldValue);
		obj.setNewValue(newValue);
		obj.setRegisId(regisId);
		session.save(obj);

	}

	public void deleteSMOJTRegisAudit(Session session, SkillMatrixRequest request, List<Tuple> tupleList) {
		if (!CollectionUtils.isEmpty(tupleList)) {
			for (Tuple tuple : tupleList) {
				smOJTRegisAudit(session, request.getOjtRegiId(), CommonUtils.objectToString(tuple.get("status")),
						CommonUtils.objectToString(request.getStatus()), request.getUpdatedBy(),
						AuditTable.SMOJTRegis.STATUS, EnovationConstants.DELETE_STRING);
				smOJTRegisAudit(session, request.getOjtRegiId(), CommonUtils.objectToString(tuple.get("trainerId")),
						CommonUtils.objectToString(request.getTrainerEmpId()), request.getUpdatedBy(),
						AuditTable.SMOJTRegis.TRAINEREMPID, EnovationConstants.DELETE_STRING);
				smOJTRegisAudit(session, request.getOjtRegiId(), CommonUtils.objectToString(tuple.get("desiredLvl")),
						CommonUtils.objectToString(request.getDesiredSkillLevelId()), request.getUpdatedBy(),
						AuditTable.SMOJTRegis.DESIREDLVLID, EnovationConstants.DELETE_STRING);
				smOJTRegisAudit(session, request.getOjtRegiId(), CommonUtils.objectToString(tuple.get("workstationId")),
						CommonUtils.objectToString(request.getWorkstationId()), request.getUpdatedBy(),
						AuditTable.SMOJTRegis.WORKSTATIONID, EnovationConstants.DELETE_STRING);
				smOJTRegisAudit(session, request.getOjtRegiId(), CommonUtils.objectToString(tuple.get("planId")),
						CommonUtils.objectToString(request.getOjtPlanId()), request.getUpdatedBy(),
						AuditTable.SMOJTRegis.PLANID, EnovationConstants.DELETE_STRING);
				smOJTRegisAudit(session, request.getOjtRegiId(), CommonUtils.objectToString(tuple.get("empId")),
						CommonUtils.objectToString(request.getEmpId()), request.getUpdatedBy(),
						AuditTable.SMOJTRegis.EMPID, EnovationConstants.DELETE_STRING);
			}
		}
	}

	public void deleteSMOJTRegisAudit(Session session, long regisId, List<Tuple> tupleList) {
		if (!CollectionUtils.isEmpty(tupleList)) {
			for (Tuple tuple : tupleList) {
				smOJTRegisAudit(session, CommonUtils.objectToLong(tuple.get("id")),
						CommonUtils.objectToString(tuple.get("status")), "", 0, AuditTable.SMOJTRegis.STATUS,
						EnovationConstants.DELETE_STRING);
				smOJTRegisAudit(session, CommonUtils.objectToLong(tuple.get("id")),
						CommonUtils.objectToString(tuple.get("trainerId")), 0, AuditTable.SMOJTRegis.TRAINEREMPID,
						EnovationConstants.DELETE_STRING);
				smOJTRegisAudit(session, CommonUtils.objectToLong(tuple.get("id")),
						CommonUtils.objectToString(tuple.get("desiredLvl")), 0, AuditTable.SMOJTRegis.DESIREDLVLID,
						EnovationConstants.DELETE_STRING);
				smOJTRegisAudit(session, CommonUtils.objectToLong(tuple.get("id")),
						CommonUtils.objectToString(tuple.get("workstationId")), 0, AuditTable.SMOJTRegis.WORKSTATIONID,
						EnovationConstants.DELETE_STRING);
				smOJTRegisAudit(session, CommonUtils.objectToLong(tuple.get("id")),
						CommonUtils.objectToString(tuple.get("planId")), 0, AuditTable.SMOJTRegis.PLANID,
						EnovationConstants.DELETE_STRING);
				smOJTRegisAudit(session, CommonUtils.objectToLong(tuple.get("id")),
						CommonUtils.objectToString(tuple.get("empId")), 0, AuditTable.SMOJTRegis.EMPID,
						EnovationConstants.DELETE_STRING);
			}
		}
	}

	private void smOJTRegisAudit(Session session, long regisId, String oldValue, int updatedBy, String colName,
			String action) {
		LOGGER.info("# SkillMatrixAudit | setSMChecksheetParameterAudit");
		AuditSMOJTRegis obj = new AuditSMOJTRegis();
		obj.setColumnName(colName);
		obj.setAction(action);
		obj.setActionBy(updatedBy);
		obj.setOldValue(oldValue);
		obj.setRegisId(regisId);
		session.save(obj);

	}

	public void updateSMConfigAudit(Session session, Tuple tupleList, SkillMatrixRequest request) {
		LOGGER.info("# SkillMatrixAudit | updateSMConfigAudit");

		if (request.getNoOfDays() != (CommonUtils.objectToInt(tupleList.get("noOfDays")))) {
			insertIntoSMConfig(session, AuditTable.AuditSMConfig.NOOFDAYS, request.getNoOfDays(),
					CommonUtils.objectToString(tupleList.get("noOfDays")), request.getUpdatedBy(), request.getId());
		}
		if (request.getSkillLevelId() != (CommonUtils.objectToLong(tupleList.get("skillLevelId")))) {
			insertIntoSMConfig(session, AuditTable.AuditSMConfig.SKILLLEVELID, request.getSkillLevelId(),
					CommonUtils.objectToString(tupleList.get("skillLevelId")), request.getUpdatedBy(), request.getId());
		}

	}

	private void insertIntoSMConfig(Session session, String colName, long newValue, String oldValue, int updatedBy,
			long id) {
		LOGGER.info("# SkillMatrixAudit | insertIntoSMConfig");
		AuditSMConfig obj = new AuditSMConfig();
		obj.setColName(colName);
		obj.setNewValue(CommonUtils.objectToString(newValue));
		obj.setOldValue(oldValue);
		obj.setUpdatedBy(updatedBy);
		obj.setAction(EnovationConstants.UPDATE_STRING);
		session.save(obj);

	}

	public void deleteSMConfigAudit(Session session, Tuple tuple, SkillMatrixRequest request) {
		LOGGER.info("# SkillMatrixAudit | deleteSMConfigAudit");
		AuditSMConfig object = new AuditSMConfig();
		object.setNoOfDays(CommonUtils.objectToInt(tuple.get("noOfDays")));
		object.setOldValue(CommonUtils.objectToString((tuple).get("skillLevelId")));
		object.setAction(EnovationConstants.DELETE_STRING);
		object.setUpdatedBy(request.getUpdatedBy());
		session.save(object);
	}

	public void updateStageLabelAudit(Session session, SkillMatrixRequest request, Tuple tupleObj) {
		LOGGER.info("# SkillMatrixAudit | updateStageLabelAudit");
		if (request.getStageLabel() != null
				&& (!request.getStageLabel().equals(CommonUtils.objectToString(tupleObj.get("stageLabel"))))) {

			saveStageLabelAudit(session, AuditTable.AuditSMStageLabel.STAGE_LABEL, request.getStageLabel(),
					CommonUtils.objectToString(tupleObj.get("stageLabel")), request.getUpdatedBy());
		}

		if (request.getStageId() > 0 && (request.getStageId() != (CommonUtils.objectToLong(tupleObj.get("stageId"))))) {

			saveStageLabelAudit(session, AuditTable.AuditSMStageLabel.STAGE_ID, Long.toString(request.getStageId()),
					CommonUtils.objectToString(tupleObj.get("stageId")), request.getUpdatedBy());
		}

		if (request.getBranchId() > 0
				&& (request.getBranchId() != (CommonUtils.objectToLong(tupleObj.get("branchId"))))) {

			saveStageLabelAudit(session, AuditTable.AuditSMStageLabel.BRANCH_ID,
					Integer.toString(request.getBranchId()), CommonUtils.objectToString(tupleObj.get("branchId")),
					request.getUpdatedBy());
		}

	}

	private void saveStageLabelAudit(Session session, String columnName, String newValue, String oldValue,
			int actionBy) {
		LOGGER.info("# SkillMatrixAudit | saveStageLabelAudit");
		AuditSMStageLabel obj = new AuditSMStageLabel();

		obj.setColumnName(columnName);
		obj.setOldValue(oldValue);
		obj.setNewValue(newValue);
		obj.setAction(EnovationConstants.ACTION_UPDATE);
		obj.setActionBy(actionBy);

		session.save(obj);
	}

	public void updateCategoryAudit(Session session, SkillMatrixRequest request, Tuple tupleObj) {
		LOGGER.info("# SkillMatrixAudit || updateCategoryAudit");

		if ((!request.getCategoryName().equals(CommonUtils.objectToString(tupleObj.get("categoryName"))))) {
			insertIntoCategoryAudit(session, AuditTable.AuditCategory.CATEGORYNAME, request.getCategoryName(),
					CommonUtils.objectToString(tupleObj.get("categoryName")), request.getUpdatedBy());
		}
		if ((request.getAssessmentId() != (CommonUtils.objectToLong(tupleObj.get("assessmentId"))))) {
			insertIntoSMCategoryAudit(session, AuditTable.AuditCategory.ASSESSMENTID, request.getAssessmentId(),
					CommonUtils.objectToString(tupleObj.get("assessmentId")), request.getUpdatedBy());
		}
	}

	private void insertIntoSMCategoryAudit(Session session, String columnName, long newValue, String oldValue,
			int updatedBy) {
		LOGGER.info("# SkillMatrixAudit | insertIntoCategoryAudit");
		AuditSMCategory obj = new AuditSMCategory();

		obj.setColName(columnName);
		obj.setOldValue(oldValue);
		obj.setNewValue(CommonUtils.objectToString(newValue));
		obj.setAction(EnovationConstants.ACTION_UPDATE);
		obj.setUpdatedBy(updatedBy);

		session.save(obj);
	}

	private void insertIntoCategoryAudit(Session session, String columnName, String newValue, String oldValue,
			int updatedBy) {
		LOGGER.info("# SkillMatrixAudit | insertIntoCategoryAudit");
		AuditSMCategory obj = new AuditSMCategory();

		obj.setColName(columnName);
		obj.setOldValue(oldValue);
		obj.setNewValue(newValue);
		obj.setAction(EnovationConstants.ACTION_UPDATE);
		obj.setUpdatedBy(updatedBy);

		session.save(obj);
	}

	public void deleteCategory(Session session, Tuple tupleObj, SkillMatrixRequest request) {
		LOGGER.info("# SkillMatrixAudit | deleteCategory");
		AuditSMCategory obj = new AuditSMCategory();

		obj.setOldValue(CommonUtils.objectToString((tupleObj).get("categoryName")));
		obj.setAssessmentId(CommonUtils.objectToLong(tupleObj.get("assessmentId")));
		obj.setAction(EnovationConstants.DELETE_STRING);
		obj.setUpdatedBy(request.getUpdatedBy());
		session.save(obj);

	}

	public void docNameAudit(Session session, Tuple tupleObj, SMDocName doc, SkillMatrixRequest request,
			String action) {
		LOGGER.info("# SkillMatrixAudit | docNameAudit");
		LOGGER.info("# "+CommonUtils.objectToString(tupleObj.get("docName")));
		LOGGER.info("# "+CommonUtils.objectToString(request.getDocName()));
		if (!CommonUtils.objectToString(request.getDocName())
				.equals(CommonUtils.objectToString(tupleObj.get("docName")))) {
			AuditSMDocName obj = new AuditSMDocName();
			obj.setOldValue(CommonUtils.objectToString(doc.getDocName()));
			obj.setNewValue(CommonUtils.objectToString(tupleObj.get("docName")));
			obj.setAction(action);
			obj.setUpdatedBy(request.getUpdatedBy());
			obj.setBranchId(request.getBranchId());
			session.save(obj);
		}
	}
}
