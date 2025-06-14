package com.greentin.enovation.skillmatrix;


import com.greentin.enovation.dto.SkillMatrixRequest;
import com.greentin.enovation.response.SkillMatrixResponse;
import org.springframework.web.bind.annotation.RequestBody;

/**
 * @author Sonali L Aug 7, 2023
 * @desc
 */
public interface SettingIService {

	SkillMatrixResponse addAssessment(SkillMatrixRequest request);

	SkillMatrixResponse getLevelList();

	SkillMatrixResponse addModelDetails(SkillMatrixRequest request);

	SkillMatrixResponse updateModelDetails(SkillMatrixRequest request);

	SkillMatrixResponse deleteModelDetails(SkillMatrixRequest request);

	SkillMatrixResponse getModelList(SkillMatrixRequest request);

	SkillMatrixResponse saveGapReason(SkillMatrixRequest request);

	SkillMatrixResponse updateGapReason(SkillMatrixRequest request);

	SkillMatrixResponse deleteGapReason(SkillMatrixRequest request);

	SkillMatrixResponse getGapReasonList(SkillMatrixRequest request);

	SkillMatrixResponse saveShiftDetails(SkillMatrixRequest request);

	SkillMatrixResponse updateShiftDetails(SkillMatrixRequest request);

	SkillMatrixResponse deleteShiftDetails(SkillMatrixRequest request);

	SkillMatrixResponse getShiftList(SkillMatrixRequest request);

	SkillMatrixResponse getStageList();

	SkillMatrixResponse saveWorkflowConfig(SkillMatrixRequest request);

	SkillMatrixResponse deleteWorkflowConfig(SkillMatrixRequest request);

	SkillMatrixResponse getWorkflowConfigList(int branchId, long levelId);

	SkillMatrixResponse getAssessmentDetail(long assessmentId);

	SkillMatrixResponse addAssessmentQuestion(SkillMatrixRequest request);

	SkillMatrixResponse getAssessmentList(SkillMatrixRequest request);

	SkillMatrixResponse updateAssessment(SkillMatrixRequest request);

	SkillMatrixResponse updateAssessmentQuestion(SkillMatrixRequest request);

	SkillMatrixResponse deleteAssessmentQuestion(SkillMatrixRequest request);

	SkillMatrixResponse deleteAssessmentOptions(SkillMatrixRequest request);

	SkillMatrixResponse deActiveAssessment(SkillMatrixRequest request);

	SkillMatrixResponse addChecksheet(SkillMatrixRequest request);

	SkillMatrixResponse getChecksheetList(SkillMatrixRequest request);

	SkillMatrixResponse updateChecksheet(SkillMatrixRequest request);

	SkillMatrixResponse deActiveChecksheet(SkillMatrixRequest request);

	SkillMatrixResponse addChecksheetPoint(SkillMatrixRequest request);

	SkillMatrixResponse updateChecksheetPoint(SkillMatrixRequest request);

	SkillMatrixResponse deActiveChecksheetPoint(SkillMatrixRequest request);

	SkillMatrixResponse getChecksheetPointList(SkillMatrixRequest request);

	SkillMatrixResponse saveWorkstation(SkillMatrixRequest request);

	SkillMatrixResponse updateWorkstation(SkillMatrixRequest request);

	SkillMatrixResponse deActivateWorkstation(SkillMatrixRequest request);

	SkillMatrixResponse getWorkstationList(SkillMatrixRequest request);

	SkillMatrixResponse getWorkstationMappingListByParentWorkstationId(SkillMatrixRequest request);

	SkillMatrixResponse getAllWorkstationMapping(SkillMatrixRequest request);

	SkillMatrixResponse saveUserType(SkillMatrixRequest request);

	SkillMatrixResponse deleteUserType(SkillMatrixRequest request);

	SkillMatrixResponse getUserTypeList(SkillMatrixRequest request);

	SkillMatrixResponse getMasterUserType();

	SkillMatrixResponse saveMasterCertificate(SkillMatrixRequest request);

	SkillMatrixResponse updateMasterCertificate(SkillMatrixRequest request);

	SkillMatrixResponse getMasterCertificateList(SkillMatrixRequest request);

	SkillMatrixResponse deleteMasterCertificate(SkillMatrixRequest request);

	SkillMatrixResponse addChecksheetParameter(SkillMatrixRequest request);

	SkillMatrixResponse getChecksheetParameterList(SkillMatrixRequest request);

	SkillMatrixResponse updateChecksheetParameter(SkillMatrixRequest request);

	SkillMatrixResponse deleteChecksheetParameter(SkillMatrixRequest request);

	SkillMatrixResponse getParameterTypeList();

	SkillMatrixResponse getChecksheetDetails(long chechSheetId);

	SkillMatrixResponse publishAssessments(SkillMatrixRequest request);

	SkillMatrixResponse updateUserType(SkillMatrixRequest request);

	SkillMatrixResponse uploadAssessments(SkillMatrixRequest request);

	SkillMatrixResponse deleteAssessmentConfig(SkillMatrixRequest request);

	SkillMatrixResponse addAssessmentConfig(SkillMatrixRequest request);

	SkillMatrixResponse updateAssessmentConfig(SkillMatrixRequest request);

	SkillMatrixResponse getAssessmentConfigList(SkillMatrixRequest request);

	SkillMatrixResponse getStageLabelList(SkillMatrixRequest request);

	SkillMatrixResponse updateStageLabel(SkillMatrixRequest request);

	SkillMatrixResponse getCellList(SkillMatrixRequest request);

	SkillMatrixResponse addCategory(SkillMatrixRequest request);

	SkillMatrixResponse updateCategory(SkillMatrixRequest request);

	SkillMatrixResponse deleteCategory(SkillMatrixRequest request);

	SkillMatrixResponse getCategoryList(SkillMatrixRequest request);

	SkillMatrixResponse saveCopyChecksheet(SkillMatrixRequest request);

	SkillMatrixResponse saveDocName(SkillMatrixRequest request);

	SkillMatrixResponse updateDocName(SkillMatrixRequest request);

	SkillMatrixResponse deleteDocName(SkillMatrixRequest request);

	SkillMatrixResponse getDocName(SkillMatrixRequest request);

}
