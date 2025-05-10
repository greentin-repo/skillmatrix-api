package com.greentin.enovation.skillmatrix;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.greentin.enovation.dto.SkillMatrixRequest;
import com.greentin.enovation.response.SkillMatrixResponse;

/**
 * @author Sonali L Aug 7, 2023
 * @desc SkillMatrix All Setup APIs
 */
@RestController
@CrossOrigin
@RequestMapping(value = "/apis/sm")
public class SettingController {
	private static final Logger LOGGER = LoggerFactory.getLogger(SettingController.class);

	@Autowired
	private SettingIService settingService;

	/**
	 * @author Rajdeep MD 08-Aug-2023 , 02:00 PM
	 * @param request
	 * @return SkillMatrixResponse
	 * @desc add new assessment
	 */
	@PostMapping(value = "/addAssessment")
	public SkillMatrixResponse addAssessment(@RequestBody SkillMatrixRequest request) {
		LOGGER.info("#In SettingController |  INSIDE in addAssessment ");
		return settingService.addAssessment(request);
	}

	/**
	 * @author Rajdeep MD 08-Aug-2023 , 02:00 PM
	 * @param request
	 * @return SkillMatrixResponse
	 * @desc update Assessment Details
	 */
	@PostMapping(value = "/updateAssessment")
	public SkillMatrixResponse updateAssessment(@RequestBody SkillMatrixRequest request) {
		LOGGER.info("#In SettingController |  INSIDE in updateAssessment ");
		return settingService.updateAssessment(request);
	}

	/**
	 * @author Ananta K. August 08, 2023 02:00:41 PM
	 * @return SkillMatrixResponse
	 * @desc This API Use To Get Master Level List
	 * @comments
	 */
	@GetMapping(value = "/getLevelList")
	public SkillMatrixResponse getLevelList() {
		LOGGER.info("# SettingController || getLevelList ");
		return settingService.getLevelList();
	}

	/**
	 * @author Ananta K. August 08, 2023 02:00:41 PM
	 * @param request
	 * @return SkillMatrixResponse
	 * @desc In This API Add Model Details By BranchId
	 * @comments
	 */
	@PostMapping(value = "/addModelDetails")
	public SkillMatrixResponse addModelDetails(@RequestBody SkillMatrixRequest request) {
		LOGGER.info("# SettingController || addModelDetails ");
		return settingService.addModelDetails(request);
	}

	/**
	 * @author Ananta K. August 08, 2023 02:00:41 PM
	 * @param request
	 * @return SkillMatrixResponse
	 * @desc This API Use To Update Model Details
	 * @comments
	 */
	@PostMapping(value = "/updateModelDetails")
	public SkillMatrixResponse updateModelDetails(@RequestBody SkillMatrixRequest request) {
		LOGGER.info("# SettingController || updateModelDetails ");
		return settingService.updateModelDetails(request);
	}

	/**
	 * @author Ananta K. August 08, 2023 02:00:41 PM
	 * @param request
	 * @return SkillMatrixResponse
	 * @desc In This API Delete Model Details
	 * @comments
	 */
	@PostMapping(value = "/deleteModelDetails")
	public SkillMatrixResponse deleteModelDetails(@RequestBody SkillMatrixRequest request) {
		LOGGER.info("# SettingController || deleteModelDetails ");
		return settingService.deleteModelDetails(request);
	}

	/**
	 * @author Ananta K. August 09, 2023 02:00:41 PM
	 * @return SkillMatrixResponse
	 * @desc This API Use To Get Model List By BranchId
	 * @comments
	 */
	@PostMapping(value = "/getModelList")
	public SkillMatrixResponse getModelList(@RequestBody SkillMatrixRequest request) {
		LOGGER.info("# SettingController || getModelList ");
		return settingService.getModelList(request);
	}

	/**
	 * @author Ananta K. August 09, 2023 02:00:41 PM
	 * @param request
	 * @return SkillMatrixResponse
	 * @desc In This API Save Gap Reason Details By BranchId
	 * @comments
	 */
	@PostMapping(value = "/saveGapReason")
	public SkillMatrixResponse saveGapReason(@RequestBody SkillMatrixRequest request) {
		LOGGER.info("# SettingController || saveGapReason ");
		return settingService.saveGapReason(request);
	}

	/**
	 * @author Ananta K. August 08, 2023 02:00:41 PM
	 * @param request
	 * @return SkillMatrixResponse
	 * @desc In This API Update Gap Reason Details
	 * @comments
	 */
	@PostMapping(value = "/updateGapReason")
	public SkillMatrixResponse updateGapReason(@RequestBody SkillMatrixRequest request) {
		LOGGER.info("# SettingController || updateGapReason ");
		return settingService.updateGapReason(request);
	}

	/**
	 * @author Ananta K. August 08, 2023 02:00:41 PM
	 * @param request
	 * @return SkillMatrixResponse
	 * @desc This API Use To Delete Gap reason
	 * @comments
	 */
	@PostMapping(value = "/deleteGapReason")
	public SkillMatrixResponse deleteGapReason(@RequestBody SkillMatrixRequest request) {
		LOGGER.info("# SettingController || deleteGapReason ");
		return settingService.deleteGapReason(request);
	}

	/**
	 * @author Ananta K. August 09, 2023 02:00:41 PM
	 * @return SkillMatrixResponse
	 * @desc This API Use To Get Gap Reason List By BranchId
	 * @comments
	 */
	@PostMapping(value = "/getGapReasonList")
	public SkillMatrixResponse getGapReasonList(@RequestBody SkillMatrixRequest request) {
		LOGGER.info("# SettingController || getGapReasonList ");
		return settingService.getGapReasonList(request);
	}

	/**
	 * @author Ananta K. August 09, 2023 02:00:41 PM
	 * @param request
	 * @return SkillMatrixResponse
	 * @desc In This API Save Shift Details By BranchId
	 * @comments
	 */
	@PostMapping(value = "/saveShiftDetails")
	public SkillMatrixResponse saveShiftDetails(@RequestBody SkillMatrixRequest request) {
		LOGGER.info("# SettingController || saveShiftDetails ");
		return settingService.saveShiftDetails(request);
	}

	/**
	 * @author Ananta K. August 08, 2023 02:00:41 PM
	 * @param request
	 * @return SkillMatrixResponse
	 * @desc This API Use To Update Shift Details
	 * @comments
	 */
	@PostMapping(value = "/updateShiftDetails")
	public SkillMatrixResponse updateShiftDetails(@RequestBody SkillMatrixRequest request) {
		LOGGER.info("# SettingController || updateShiftDetails ");
		return settingService.updateShiftDetails(request);
	}

	/**
	 * @author Ananta K. August 08, 2023 02:00:41 PM
	 * @param request
	 * @return SkillMatrixResponse
	 * @desc This API Use For Delete Shift Details
	 * @comments
	 */
	@PostMapping(value = "/deleteShiftDetails")
	public SkillMatrixResponse deleteShiftDetails(@RequestBody SkillMatrixRequest request) {
		LOGGER.info("# SettingController || deleteShiftDetails ");
		return settingService.deleteShiftDetails(request);
	}

	/**
	 * @author Ananta K. August 09, 2023 02:00:41 PM
	 * @return SkillMatrixResponse
	 * @desc This API Use To Get Gap Reason List By BranchId
	 * @comments
	 */
	@PostMapping(value = "/getShiftList")
	public SkillMatrixResponse getShiftList(@RequestBody SkillMatrixRequest request) {
		LOGGER.info("# SettingController || getShiftList ");
		return settingService.getShiftList(request);
	}

	/**
	 * @author Ananta K. August 10, 2023 02:00:41 PM
	 * @return SkillMatrixResponse
	 * @desc This API Use To Get Stage List
	 * @comments
	 */
	@GetMapping(value = "/getStageList")
	public SkillMatrixResponse getStageList() {
		LOGGER.info("# SettingController || getStageList ");
		return settingService.getStageList();
	}

	/**
	 * @author Ananta K. August 10, 2023 02:00:41 PM
	 * @param request
	 * @return SkillMatrixResponse
	 * @desc In This API Save Workflow Config By BranchId
	 * @comments
	 */
	@PostMapping(value = "/saveWorkflowConfig")
	public SkillMatrixResponse saveWorkflowConfig(@RequestBody SkillMatrixRequest request) {
		LOGGER.info("# SettingController || saveWorkflowConfig ");
		return settingService.saveWorkflowConfig(request);
	}

	/**
	 * @author Ananta K. August 08, 2023 02:00:41 PM
	 * @param request
	 * @return SkillMatrixResponse
	 * @desc This API Use For Delete Workflow Config
	 * @comments
	 */
	@PostMapping(value = "/deleteWorkflowConfig")
	public SkillMatrixResponse deleteWorkflowConfig(@RequestBody SkillMatrixRequest request) {
		LOGGER.info("# SettingController || deleteWorkflowConfig ");
		return settingService.deleteWorkflowConfig(request);
	}

	/**
	 * @author Ananta K. August 09, 2023 02:00:41 PM
	 * @return SkillMatrixResponse
	 * @desc This API Use To Get Workflow Config List By BranchId
	 * @comments
	 */
	@GetMapping(value = "/getWorkflowConfigList/{branchId}/{levelId}")
	public SkillMatrixResponse getWorkflowConfigList(@PathVariable int branchId, @PathVariable long levelId) {
		LOGGER.info("# SettingController || getWorkflowConfigList ");
		return settingService.getWorkflowConfigList(branchId, levelId);
	}

	/**
	 * @author Rajdeep MD 08-Aug-2023 , 02:00 PM
	 * @param assessmentId
	 * @return SkillMatrixResponse
	 * @desc get Assessment details with Quetions and Options
	 */
	@GetMapping(value = "/getAssessmentDetail/{assessmentId}")
	public SkillMatrixResponse getAssessmentDetail(@PathVariable("assessmentId") long assessmentId) {
		LOGGER.info("#In SettingController |  INSIDE in getAssessmentDetail ");
		return settingService.getAssessmentDetail(assessmentId);
	}

	/**
	 * @author Rajdeep MD 08-Aug-2023 , 02:00 PM
	 * @param request
	 * @return SkillMatrixResponse
	 * @desc get Assessment list by branchId or skillLevelId
	 */
	@PostMapping(value = "/getAssessmentList")
	public SkillMatrixResponse getAssessmentList(@RequestBody SkillMatrixRequest request) {
		LOGGER.info("#In SettingController |  INSIDE in getAssessmentList ");
		return settingService.getAssessmentList(request);
	}

	/**
	 * @author Rajdeep MD 08-Aug-2023 , 02:00 PM
	 * @param request
	 * @return SkillMatrixResponse
	 * @desc add Assessment Questions with Options
	 */
	@PostMapping(value = "/addAssessmentQuestion")
	public SkillMatrixResponse addAssessmentQuestion(@RequestBody SkillMatrixRequest request) {
		LOGGER.info("#In SettingController |  INSIDE in addAssessmentQuestion ");
		return settingService.addAssessmentQuestion(request);
	}

	/**
	 * @author Rajdeep MD 08-Aug-2023 , 02:00 PM
	 * @param request
	 * @return SkillMatrixResponse
	 * @desc update Assessment Questions with Options
	 */
	@PostMapping(value = "/updateAssessmentQuestion")
	public SkillMatrixResponse updateAssessmentQuestion(@RequestBody SkillMatrixRequest request) {
		LOGGER.info("#In SettingController |  INSIDE in updateAssessmentQuestion ");
		return settingService.updateAssessmentQuestion(request);
	}

	/**
	 * @author Rajdeep MD 08-Aug-2023 , 02:00 PM
	 * @param request
	 * @return SkillMatrixResponse
	 * @desc delete or deactive Assessment Questions
	 */
	@PostMapping(value = "/deleteAssessmentQuestion")
	public SkillMatrixResponse deleteAssessmentQuestion(@RequestBody SkillMatrixRequest request) {
		LOGGER.info("#In SettingController |  INSIDE in deleteAssessmentQuestion ");
		return settingService.deleteAssessmentQuestion(request);
	}

	/**
	 * @author Rajdeep MD 08-Aug-2023 , 02:00 PM
	 * @param request
	 * @return SkillMatrixResponse
	 * @desc delete Assessment Questions option
	 */
	@PostMapping(value = "/deleteAssessmentOptions")
	public SkillMatrixResponse deleteAssessmentOptions(@RequestBody SkillMatrixRequest request) {
		LOGGER.info("#In SettingController |  INSIDE in deleteAssessmentOptions ");
		return settingService.deleteAssessmentOptions(request);
	}

	/**
	 * @author Rajdeep MD 09-Aug-2023 , 02:00 PM
	 * @param request
	 * @return SkillMatrixResponse
	 * @desc deActive Assessment
	 */
	@PostMapping(value = "/deActiveAssessment")
	public SkillMatrixResponse deActiveAssessment(@RequestBody SkillMatrixRequest request) {
		LOGGER.info("#In SettingController |  INSIDE in deActiveAssessment ");
		return settingService.deActiveAssessment(request);
	}

	/**
	 * @author Rajdeep MD 10-Aug-2023 , 02:00 PM
	 * @param request
	 * @return SkillMatrixResponse
	 * @desc add new Checksheet
	 */
	@PostMapping(value = "/addChecksheet")
	public SkillMatrixResponse addChecksheet(@RequestBody SkillMatrixRequest request) {
		LOGGER.info("#In SettingController |  INSIDE in addChecksheet ");
		return settingService.addChecksheet(request);
	}

	/**
	 * @author Rajdeep MD 10-Aug-2023 , 02:00 PM
	 * @param request
	 * @return SkillMatrixResponse
	 * @desc get Checksheet details
	 */
	@PostMapping(value = "/getChecksheetList")
	public SkillMatrixResponse getChecksheetList(@RequestBody SkillMatrixRequest request) {
		LOGGER.info("#In SettingController |  INSIDE in getChecksheetList ");
		return settingService.getChecksheetList(request);
	}

	/**
	 * @author Rajdeep MD 10-Aug-2023 , 02:00 PM
	 * @param request
	 * @return SkillMatrixResponse
	 * @desc update Checksheet details
	 */
	@PostMapping(value = "/updateChecksheet")
	public SkillMatrixResponse updateChecksheet(@RequestBody SkillMatrixRequest request) {
		LOGGER.info("#In SettingController |  INSIDE in updateChecksheet ");
		return settingService.updateChecksheet(request);
	}

	/**
	 * @author Rajdeep MD 10-Aug-2023 , 02:00 PM
	 * @param request
	 * @return SkillMatrixResponse
	 * @desc delete or deactive Checksheet details
	 */
	@PostMapping(value = "/deleteChecksheet")
	public SkillMatrixResponse deActiveChecksheet(@RequestBody SkillMatrixRequest request) {
		LOGGER.info("#In SettingController |  INSIDE in deActiveChecksheet ");
		return settingService.deActiveChecksheet(request);
	}

	/**
	 * @author Rajdeep MD 10-Aug-2023 , 02:00 PM
	 * @param request
	 * @return SkillMatrixResponse
	 * @desc add Checksheet Point
	 */
	@PostMapping(value = "/addChecksheetPoint")
	public SkillMatrixResponse addChecksheetPoint(@RequestBody SkillMatrixRequest request) {
		LOGGER.info("#In SettingController |  INSIDE in addChecksheetPoint ");
		return settingService.addChecksheetPoint(request);
	}

	/**
	 * @author Rajdeep MD 10-Aug-2023 , 02:00 PM
	 * @param request
	 * @return SkillMatrixResponse
	 * @desc get Checksheet Point details
	 */
	@PostMapping(value = "/getChecksheetPointList")
	public SkillMatrixResponse getChecksheetPointList(@RequestBody SkillMatrixRequest request) {
		LOGGER.info("#In SettingController |  INSIDE in getChecksheetPoint ");
		return settingService.getChecksheetPointList(request);
	}

	/**
	 * @author Rajdeep MD 10-Aug-2023 , 02:00 PM
	 * @param request
	 * @return SkillMatrixResponse
	 * @desc update Checksheet Point details
	 */
	@PostMapping(value = "/updateChecksheetPoint")
	public SkillMatrixResponse updateChecksheetPoint(@RequestBody SkillMatrixRequest request) {
		LOGGER.info("#In SettingController |  INSIDE in updateChecksheetPoint ");
		return settingService.updateChecksheetPoint(request);
	}

	/**
	 * @author Rajdeep MD 10-Aug-2023 , 02:00 PM
	 * @param request
	 * @return SkillMatrixResponse
	 * @desc delete or deactive Checksheet Point details
	 */
	@PostMapping(value = "/deleteChecksheetPoint")
	public SkillMatrixResponse deActiveChecksheetPoint(@RequestBody SkillMatrixRequest request) {
		LOGGER.info("#In SettingController |  INSIDE in deActiveChecksheetPoint ");
		return settingService.deActiveChecksheetPoint(request);
	}

	/**
	 * @author Sonali L. Aug 08, 2023 02:00:55 PM
	 * @param request
	 * @return SkillMatrixResponse
	 * @desc Add Workstation Details
	 */

	@PostMapping(value = "/saveWorkstation")
	public SkillMatrixResponse saveWorkstation(@RequestBody SkillMatrixRequest request) {
		LOGGER.info("# SettingController || saveWorkstation ");
		return settingService.saveWorkstation(request);
	}

	/**
	 * @author Sonali L. Aug 08, 2023 02:00:55 PM
	 * @param request
	 * @return SkillMatrixResponse
	 * @desc Update Workstation Details
	 */

	@PostMapping(value = "/updateWorkstation")
	public SkillMatrixResponse updateWorkstation(@RequestBody SkillMatrixRequest request) {
		LOGGER.info("# SettingController || updateWorkstation ");
		return settingService.updateWorkstation(request);
	}

	/**
	 * @author Sonali L. Aug 08, 2023 02:00:55 PM
	 * @param request
	 * @return SkillMatrixResponse
	 * @desc Delete or Deactive Workstation details
	 */
	@PostMapping(value = "/deActivateWorkstation")
	public SkillMatrixResponse deActivateWorkstation(@RequestBody SkillMatrixRequest request) {
		LOGGER.info("# SettingController || deActivateWorkstation ");
		return settingService.deActivateWorkstation(request);
	}

	/**
	 * @author Sonali L. Aug 08, 2023 02:30:55 PM
	 * @param request
	 * @return SkillMatrixResponse
	 * @desc Get Workstation Details
	 */

	@PostMapping(value = "/getWorkstationList")
	public SkillMatrixResponse getWorkstationList(@RequestBody SkillMatrixRequest request) {
		LOGGER.info("# SettingController || getWorkstationList ");
		return settingService.getWorkstationList(request);
	}

	@PostMapping(value = "/getWorkstationMappingList/{parentWorkstationId}")
	public SkillMatrixResponse getWorkstationMappingList(@PathVariable long parentWorkstationId) {
		LOGGER.info("# SettingController || getWorkstationList ");
		SkillMatrixRequest request = new SkillMatrixRequest();
		request.setParentWorkstationId(parentWorkstationId);
		return settingService.getWorkstationMappingListByParentWorkstationId(request);
	}

	@GetMapping(value = "/getAllWorkstationMapping")
	public SkillMatrixResponse getAllWorkstationMapping() {
		LOGGER.info("# SettingController || getAllWorkstationMapping");
		return settingService.getAllWorkstationMapping();
	}

	/**
	 * @author Sonali L. Aug 08, 2023 03:00:55 PM
	 * @param request
	 * @return SkillMatrixResponse
	 * @desc Add UserType Details
	 */

	@PostMapping(value = "/saveUserType")
	public SkillMatrixResponse saveUserType(@RequestBody SkillMatrixRequest request) {
		LOGGER.info("# SettingController || saveUserType ");
		return settingService.saveUserType(request);
	}

	/**
	 * @author Sonali L. Aug 22, 2023 11:00:55 PM
	 * @param request
	 * @return SkillMatrixResponse
	 * @desc Update UserType Details
	 */

	@PostMapping(value = "/updateUserType")
	public SkillMatrixResponse updateUserType(@RequestBody SkillMatrixRequest request) {
		LOGGER.info("# SettingController || updateUserType ");
		return settingService.updateUserType(request);
	}

	/**
	 * @author Sonali L. Aug 08, 2023 03:00:55 PM
	 * @param request
	 * @return SkillMatrixResponse
	 * @desc Delete UserType Details
	 */

	@PostMapping(value = "/deleteUserType")
	public SkillMatrixResponse deleteUserType(@RequestBody SkillMatrixRequest request) {
		LOGGER.info("# SettingController || deleteUserType ");
		return settingService.deleteUserType(request);
	}

	/**
	 * @author Sonali L. Aug 08, 2023 03:00:55 PM
	 * @param request
	 * @return SkillMatrixResponse
	 * @desc Get UserType Details
	 */

	@PostMapping(value = "/getUserTypeList")
	public SkillMatrixResponse getUserTypeList(@RequestBody SkillMatrixRequest request) {
		LOGGER.info("# SettingController || getUserTypeList ");
		return settingService.getUserTypeList(request);
	}

	/**
	 * @author Sonali L. Aug 08, 2023 03:00:55 PM
	 * @param request
	 * @return SkillMatrixResponse
	 * @desc Get Master User Type Details
	 */
	@GetMapping(value = "/getMasterUserType")
	public SkillMatrixResponse getMasterUserType() {
		LOGGER.info("# SettingController || getMasterUserType ");
		return settingService.getMasterUserType();
	}

	/**
	 * @author Abhishek G. August 17, 2023 02:00:41 PM
	 * @return SkillMatrixResponse
	 * @desc This API Use To Save Master Certificate
	 * @comments
	 */
	@PostMapping(value = "/saveMasterCertificate")
	public SkillMatrixResponse saveMasterCertificate(@ModelAttribute SkillMatrixRequest request) {
		LOGGER.info("# SettingController || saveMasterCertificate ");
		return settingService.saveMasterCertificate(request);
	}

	/**
	 * @author Abhishek G. August 17, 2023 02:00:41 PM
	 * @return SkillMatrixResponse
	 * @desc This API Use To Update Master Certificate
	 * @comments
	 */
	@PostMapping(value = "/updateMasterCertificate")
	public SkillMatrixResponse updateMasterCertificate(@ModelAttribute SkillMatrixRequest request) {
		LOGGER.info("# SettingController || updateMasterCertificate ");
		return settingService.updateMasterCertificate(request);
	}

	/**
	 * @author Abhishek G. August 17, 2023 02:00:41 PM
	 * @return SkillMatrixResponse
	 * @desc This API Use To Get Master Certificate List
	 * @comments
	 */
	@PostMapping(value = "/getMasterCertificateList")
	public SkillMatrixResponse getMasterCertificateList(@RequestBody SkillMatrixRequest request) {
		LOGGER.info("# SettingController || getMasterCertificateList ");
		return settingService.getMasterCertificateList(request);
	}

	/**
	 * @author Anant K. August 18, 2023 02:00:41 PM
	 * @return SkillMatrixResponse
	 * @desc This API Use To Delete Master Certificate
	 * @comments
	 */
	@PostMapping(value = "/deleteMasterCertificate")
	public SkillMatrixResponse deleteMasterCertificate(@RequestBody SkillMatrixRequest request) {
		LOGGER.info("# SettingController || deleteMasterCertificate ");
		return settingService.deleteMasterCertificate(request);
	}

	/**
	 * @author Rajdeep MD 12-Aug-2023 , 02:00 PM
	 * @param request
	 * @return SkillMatrixResponse
	 * @desc add Checksheet Parameter
	 */
	@PostMapping(value = "/addChecksheetParameter")
	public SkillMatrixResponse addChecksheetParameter(@RequestBody SkillMatrixRequest request) {
		LOGGER.info("#In SettingController |  INSIDE in addChecksheetParameter ");
		return settingService.addChecksheetParameter(request);
	}

	/**
	 * @author Rajdeep MD 12-Aug-2023 , 02:00 PM
	 * @param request
	 * @return SkillMatrixResponse
	 * @desc get Checksheet parameter details
	 */
	@PostMapping(value = "/getChecksheetParameterList")
	public SkillMatrixResponse getChecksheetParameterList(@RequestBody SkillMatrixRequest request) {
		LOGGER.info("#In SettingController |  INSIDE in getChecksheetParameterList ");
		return settingService.getChecksheetParameterList(request);
	}

	/**
	 * @author Rajdeep MD 12-Aug-2023 , 02:00 PM
	 * @param request
	 * @return SkillMatrixResponse
	 * @desc update Checksheet Paramenter details
	 */
	@PostMapping(value = "/updateChecksheetParameter")
	public SkillMatrixResponse updateChecksheetParameter(@RequestBody SkillMatrixRequest request) {
		LOGGER.info("#In SettingController |  INSIDE in updateChecksheetParameter ");
		return settingService.updateChecksheetParameter(request);
	}

	/**
	 * @author Rajdeep MD 12-Aug-2023 , 02:00 PM
	 * @param request
	 * @return SkillMatrixResponse
	 * @desc delete or deactive Checksheet Parameter details
	 */
	@PostMapping(value = "/deleteChecksheetParameter")
	public SkillMatrixResponse deleteChecksheetParameter(@RequestBody SkillMatrixRequest request) {
		LOGGER.info("#In SettingController |  INSIDE in deleteChecksheetParameter ");
		return settingService.deleteChecksheetParameter(request);
	}

	/**
	 * @author Rajdeep MD 13-Aug-2023 , 10:00 AM
	 * @param request
	 * @return SkillMatrixResponse
	 * @desc get parameter Type details
	 */
	@GetMapping(value = "/getParameterTypeList")
	public SkillMatrixResponse getParameterTypeList() {
		LOGGER.info("#In SettingController |  INSIDE in getParameterTypeList ");
		return settingService.getParameterTypeList();
	}

	/**
	 * @author Rajdeep MD 13-Aug-2023 , 10:00 AM
	 * @param request
	 * @return SkillMatrixResponse
	 * @desc Publish Assessment
	 */
	@PostMapping(value = "/publishAssessments")
	public SkillMatrixResponse publishAssessments(@RequestBody SkillMatrixRequest request) {
		LOGGER.info("#In SettingController |  INSIDE in publishAssessments ");
		return settingService.publishAssessments(request);
	}

	/**
	 * @author Rajdeep MD 13-Aug-2023 , 10:00 AM
	 * @param request
	 * @return SkillMatrixResponse
	 * @desc Publi
	 */
	@GetMapping(value = "/getChecksheetDetails/{chechSheetId}")
	public SkillMatrixResponse getChecksheetDetails(@PathVariable long chechSheetId) {
		LOGGER.info("#In SettingController |  INSIDE in getChecksheetDetails ");
		return settingService.getChecksheetDetails(chechSheetId);
	}

	/**
	 * @author Rajdeep MD 15-Sept-2023 , 3:00 PM
	 * @param request
	 * @return SkillMatrixResponse
	 * @desc add assessment with Multiple Questions
	 */
	@PostMapping(value = "/uploadAssessments")
	public SkillMatrixResponse uploadAssessments(@ModelAttribute SkillMatrixRequest request) {
		LOGGER.info("#In SettingController |  INSIDE in uploadAssessments ");
		return settingService.uploadAssessments(request);
	}

	/**
	 * @author Sonali L 19-Sept-2023 , 3:00 PM
	 * @param request
	 * @return SkillMatrixResponse
	 * @desc
	 */
	@PostMapping(value = "/addAssessmentConfig")
	public SkillMatrixResponse addAssessmentConfig(@RequestBody SkillMatrixRequest request) {
		LOGGER.info("#In SettingController |  INSIDE in addAssessmentConfig ");
		return settingService.addAssessmentConfig(request);

	}

	/**
	 * @author Sonali L 19-Sept-2023 , 3:00 PM
	 * @param request
	 * @return SkillMatrixResponse
	 * @desc
	 */
	@PostMapping(value = "/updateAssessmentConfig")
	public SkillMatrixResponse updateAssessmentConfig(@RequestBody SkillMatrixRequest request) {
		LOGGER.info("#In SettingController |  INSIDE in updateAssessmentConfig ");
		return settingService.updateAssessmentConfig(request);

	}

	/**
	 * @author Sonali L 19-Sept-2023 , 3:00 PM
	 * @param request
	 * @return SkillMatrixResponse
	 * @desc
	 */
	@PostMapping(value = "/deleteAssessmentConfig")
	public SkillMatrixResponse deleteAssessmentConfig(@RequestBody SkillMatrixRequest request) {
		LOGGER.info("#In SettingController |  INSIDE in deleteAssessmentConfig ");
		return settingService.deleteAssessmentConfig(request);
	}

	/**
	 * @author Sonali L 19-Sept-2023 , 3:00 PM
	 * @param request
	 * @return SkillMatrixResponse
	 * @desc
	 */
	@PostMapping(value = "/getAssessmentConfigList")
	public SkillMatrixResponse getAssessmentConfigList(@RequestBody SkillMatrixRequest request) {
		LOGGER.info("#In SettingController |  INSIDE in getAssessmentConfigList ");
		return settingService.getAssessmentConfigList(request);
	}

	/**
	 * @author Abhishek G 19-Sept-2023 , 3:00 PM
	 * @param request
	 * @return SkillMatrixResponse
	 * @desc
	 */
	@PostMapping(value = "/getStageLabelList")
	public SkillMatrixResponse getStageLabelList(@RequestBody SkillMatrixRequest request) {
		LOGGER.info("#In SettingController |  INSIDE in getStageLabelList ");
		return settingService.getStageLabelList(request);
	}

	/**
	 * @author Abhishek G 19-Sept-2023 , 3:00 PM
	 * @param request
	 * @return SkillMatrixResponse
	 * @desc
	 */
	@PostMapping(value = "/updateStageLabel")
	public SkillMatrixResponse updateStageLabel(@RequestBody SkillMatrixRequest request) {
		LOGGER.info("#In SettingController |  INSIDE in updateStageLabel ");
		return settingService.updateStageLabel(request);
	}

	/**
	 * @author Abhishek G 05-Oct-2023 , 3:00 PM
	 * @param request
	 * @return SkillMatrixResponse
	 * @desc
	 */

	@PostMapping(value = "/getCellList")
	public SkillMatrixResponse getCellList(@RequestBody SkillMatrixRequest request) {
		LOGGER.info("#In SettingController |  INSIDE getCellList ");
		return settingService.getCellList(request);
	}

	/**
	 * @author Sonali L 10-Oct-2023
	 * @param request
	 * @return SkillMatrixResponse
	 * @desc
	 */
	@PostMapping(value = "/addCategory")
	public SkillMatrixResponse addCategory(@RequestBody SkillMatrixRequest request) {
		LOGGER.info("#In SettingController |  INSIDE in addCategory ");
		return settingService.addCategory(request);

	}

	/**
	 * @author Sonali L 10-Oct-2023
	 * @param request
	 * @return SkillMatrixResponse
	 * @desc
	 */
	@PostMapping(value = "/updateCategory")
	public SkillMatrixResponse updateCategory(@RequestBody SkillMatrixRequest request) {
		LOGGER.info("#In SettingController |  INSIDE in updateCategory ");
		return settingService.updateCategory(request);

	}

	/**
	 * @author Sonali L 10-Oct-2023
	 * @param request
	 * @return SkillMatrixResponse
	 * @desc
	 */
	@PostMapping(value = "/deleteCategory")
	public SkillMatrixResponse deleteCategory(@RequestBody SkillMatrixRequest request) {
		LOGGER.info("#In SettingController |  INSIDE in deleteCategory ");
		return settingService.deleteCategory(request);

	}

	/**
	 * @author Sonali L 10-Oct-2023
	 * @param request
	 * @return SkillMatrixResponse
	 * @desc
	 */
	@PostMapping(value = "/getCategoryList")
	public SkillMatrixResponse getCategoryList(@RequestBody SkillMatrixRequest request) {
		LOGGER.info("#In SettingController |  INSIDE in getCategoryList ");
		return settingService.getCategoryList(request);

	}

	/**
	 * @author Anant K 02-Feb-2024 , 11:30 AM
	 * @param request
	 * @return SkillMatrixResponse
	 * @desc copy Checksheet 
	 */
	@PostMapping(value = "/saveCopyChecksheet")
	public SkillMatrixResponse saveCopyChecksheet(@RequestBody SkillMatrixRequest request) {
		LOGGER.info("#In SettingController |  INSIDE in saveCopyChecksheet ");
		return settingService.saveCopyChecksheet(request);
	}
	
	/**
	 * @author Rajdeep Md 2024-May-29
	 * @param request
	 * @return SkillMatrixResponse
	 * @desc Save Document Name 
	 */
	@PostMapping(value = "/saveDocName")
	public SkillMatrixResponse saveDocName(@RequestBody SkillMatrixRequest request) {
		LOGGER.info("#In SettingController |  INSIDE in saveDocName ");
		return settingService.saveDocName(request);
	}
	
	/**
	 * @author Rajdeep Md 2024-May-29
	 * @param request
	 * @return SkillMatrixResponse
	 * @desc update Document Name 
	 */
	@PostMapping(value = "/updateDocName")
	public SkillMatrixResponse updateDocName(@RequestBody SkillMatrixRequest request) {
		LOGGER.info("#In SettingController |  INSIDE in updateDocName ");
		return settingService.updateDocName(request);
	}
	
	/**
	 * @author Rajdeep Md 2024-May-29
	 * @param request
	 * @return SkillMatrixResponse
	 * @desc delete Document Name 
	 */
	@PostMapping(value = "/deleteDocName")
	public SkillMatrixResponse deleteDocName(@RequestBody SkillMatrixRequest request) {
		LOGGER.info("#In SettingController |  INSIDE in deleteDocName ");
		return settingService.deleteDocName(request);
	}
	
	/**
	 * @author Rajdeep Md 2024-May-29
	 * @param request
	 * @return SkillMatrixResponse
	 * @desc update Document Name 
	 */
	@PostMapping(value = "/getDocName")
	public SkillMatrixResponse v(@RequestBody SkillMatrixRequest request) {
		LOGGER.info("#In SettingController |  INSIDE in getDocName ");
		return settingService.getDocName(request);
	}
}
