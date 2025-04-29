package com.greentin.enovation.skillmatrix;

import java.util.HashMap;
import java.util.List;

import com.greentin.enovation.dto.SMAssessmentDTO;
import com.greentin.enovation.dto.SkillMatrixRequest;
import com.greentin.enovation.response.SkillMatrixResponse;

/**
 * @author Sonali L Aug 7, 2023
 * @desc
 */
public interface SettingIDao {

	SkillMatrixRequest addAssessment(SkillMatrixRequest request);

	List<HashMap<String, Object>> getLevelList();

	boolean addModelDetails(SkillMatrixRequest request);

	boolean updateModelDetails(SkillMatrixRequest request);

	boolean deleteModelDetails(SkillMatrixRequest request);

	List<HashMap<String, Object>> getModelList(SkillMatrixRequest request, SkillMatrixResponse response);

	boolean saveGapReason(SkillMatrixRequest request);

	boolean updateGapReason(SkillMatrixRequest request);

	boolean deleteGapReason(SkillMatrixRequest request);

	List<HashMap<String, Object>> getGapReasonList(SkillMatrixRequest request, SkillMatrixResponse response);

	boolean saveShiftDetails(SkillMatrixRequest request);

	boolean updateShiftDetails(SkillMatrixRequest request);

	boolean deleteShiftDetails(SkillMatrixRequest request);

	List<HashMap<String, Object>> getShiftList(SkillMatrixRequest request, SkillMatrixResponse response);

	boolean deActiveShiftDetails(SkillMatrixRequest request);

	List<HashMap<String, Object>> getStageList();

	boolean saveWorkflowConfig(SkillMatrixRequest request);

	boolean deleteWorkflowConfig(SkillMatrixRequest request);

	boolean deActiveWorkflowConfig(SkillMatrixRequest request);

	List<HashMap<String, Object>> getWorkflowConfigList(int branchId, long levelId);

	SMAssessmentDTO getAssessmentDetail(long assessmentId);

	boolean addAssessmentQuestion(SkillMatrixRequest request);

	List<HashMap<String, Object>> getAssessmentList(SkillMatrixRequest request, SkillMatrixResponse response);

	boolean updateAssessment(SkillMatrixRequest request);

	boolean updateAssessmentQuestion(SkillMatrixRequest request);

	boolean deleteAssessmentQuestion(SkillMatrixRequest request);

	boolean deleteAssessmentOptions(SkillMatrixRequest request);

	boolean deActiveQuetion(SkillMatrixRequest request);

	boolean deActiveAssessment(SkillMatrixRequest request);

	SkillMatrixRequest addChecksheet(SkillMatrixRequest request);

	List<HashMap<String, Object>> getChecksheetList(SkillMatrixRequest request, SkillMatrixResponse response);

	boolean updateChecksheet(SkillMatrixRequest request);

	boolean deActiveChecksheet(SkillMatrixRequest request);

	boolean deleteChecksheet(SkillMatrixRequest request);

	boolean addChecksheetPoint(SkillMatrixRequest request);

	boolean updateChecksheetPoint(SkillMatrixRequest request);

	boolean deleteChecksheetPoint(SkillMatrixRequest request);

	List<HashMap<String, Object>> getChecksheetPointList(SkillMatrixRequest request);

	boolean deActiveChecksheetPoint(SkillMatrixRequest request);

	boolean saveWorkstation(SkillMatrixRequest request);

	boolean updateWorkstation(SkillMatrixRequest request);

	boolean deleteWorkstation(SkillMatrixRequest request);

	List<HashMap<String, Object>> getWorkstationList(SkillMatrixRequest request, SkillMatrixResponse response);

	boolean saveUserType(SkillMatrixRequest request);

	boolean deleteUserType(SkillMatrixRequest request);

	List<HashMap<String, Object>> getUserTypeList(SkillMatrixRequest request, SkillMatrixResponse response);

	List<HashMap<String, Object>> getMasterUserType();

	boolean deActivateWorkstation(SkillMatrixRequest request);

	boolean saveMasterCertificate(SkillMatrixRequest request);

	boolean updateMasterCertificate(SkillMatrixRequest request);

	List<HashMap<String, Object>> getMasterCertificateList(SkillMatrixRequest request, SkillMatrixResponse response);

	boolean deleteMasterCertificate(SkillMatrixRequest request);

	boolean addChecksheetParameter(SkillMatrixRequest request);

	List<HashMap<String, Object>> getChecksheetParameterList(SkillMatrixRequest request);

	boolean updateChecksheetParameter(SkillMatrixRequest request);

	boolean deleteChecksheetParameter(SkillMatrixRequest request);

	boolean deActiveChecksheetParameter(SkillMatrixRequest request);

	List<HashMap<String, Object>> getParameterTypeList();

	HashMap<String, Object> getChecksheetDetails(long chechSheetId);

	boolean publishAssessments(SkillMatrixRequest request);

	boolean updateUserType(SkillMatrixRequest request);

	SkillMatrixRequest uploadAssessments(SkillMatrixRequest request);

	boolean deleteAssessmentConfig(SkillMatrixRequest request);

	boolean addAssessmentConfig(SkillMatrixRequest request);

	boolean updateAssessmentConfig(SkillMatrixRequest request);

	List<HashMap<String, Object>> getAssessmentConfigList(SkillMatrixRequest request, SkillMatrixResponse response);

	List<HashMap<String, Object>> getStageLabelList(SkillMatrixRequest request);

	boolean updateStageLabel(SkillMatrixRequest request);

	List<HashMap<String, Object>> getCellList(SkillMatrixRequest request);

	boolean addCategory(SkillMatrixRequest request);

	boolean updateCategory(SkillMatrixRequest request);

	boolean deleteCategory(SkillMatrixRequest request);

	List<HashMap<String, Object>> getCategoryList(SkillMatrixRequest request);

	SkillMatrixRequest saveCopyChecksheet(SkillMatrixRequest request);

	boolean saveDocName(SkillMatrixRequest request);

	boolean updateDocName(SkillMatrixRequest request);

	boolean deleteDocName(SkillMatrixRequest request);

	HashMap<String, Object> getDocName(SkillMatrixRequest request);

	List<HashMap<String, Object>> getWorkstationMappingListByParentWorkstationId(SkillMatrixRequest request, SkillMatrixResponse response);

	List<HashMap<String, Object>> getAllWorkstationMapping();

}
