package com.greentin.enovation.skillmatrix;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.greentin.enovation.dto.SkillMatrixRequest;
import com.greentin.enovation.response.SkillMatrixResponse;
import com.greentin.enovation.schedulars.SkillMatrixSchedular;

/**
 * @author Sonali L Aug 7, 2023
 * @desc SkillMatrix for all APIs workstation
 */
@RestController
@CrossOrigin
@RequestMapping(value = "/apis/sm")
public class WorkflowController {
	private static final Logger LOGGER = LoggerFactory.getLogger(WorkflowController.class);

	@Autowired
	private WorkflowIService workflowService;

	@Autowired
	private SkillMatrixSchedular skillSche;


	@GetMapping(value = "/test/publishOJT")
	public void publishOJT() {
		LOGGER.info("#In WorkflowController |  INSIDE in publishOJT ");
		skillSche.publishOJT();
	}

	@GetMapping(value = "/test/assignPreAssessmentScheduler")
	public void assignPreAssessmentScheduler() {
		LOGGER.info("#In WorkflowController |  INSIDE in assignPreAssessmentScheduler ");
		skillSche.assignPreAssessmentScheduler();
	}
	/**
	 * @author Ananta K. August 16, 2023 02:00:41 PM
	 * @param request
	 * @return SkillMatrixResponse
	 * @desc In This API Save OJT Registration Details
	 * @comments
	 */
	@PostMapping(value = "/ojtRegistration")
	public SkillMatrixResponse ojtRegistration(@RequestBody SkillMatrixRequest request) {
		return workflowService.ojtRegistration(request);
	}

	/**
	 * @author Rajdeep MD 23-Aug-2023 , 10:00 AM
	 * @param request
	 * @return SkillMatrixResponse
	 * @desc
	 */
	@GetMapping(value = "/getOJTRegistrationDetails/{oJTRegiId}")
	public SkillMatrixResponse oJTRegistrationDetails(@PathVariable("oJTRegiId") long oJTRegiId) {
		LOGGER.info("#In WorkflowController |  INSIDE in getOJTRegistrationDetails ");
		return workflowService.getOJTRegistrationDetails(oJTRegiId);
	}

	/**
	 * @author Sonali L 19-OCT-2023
	 * @param request
	 * @return SkillMatrixResponse
	 * @desc In My Skilling Activity get OJT registration details by OJT
	 *       registration Id
	 */

	@GetMapping(value = "/getOJTRegistrationDetail/{oJTRegiId}")
	public SkillMatrixResponse getOJTRegistrationDetail(@PathVariable("oJTRegiId") long oJTRegiId) {
		LOGGER.info("#In WorkflowController |  INSIDE in getOJTRegistrationDetail ");
		return workflowService.getOJTRegistrationDetail(oJTRegiId);
	}

	/**
	 * @author Ananta K. August 22, 2023 02:00:41 PM
	 * @param request
	 * @return SkillMatrixResponse
	 * @desc In This API Get My Action List
	 * @comments
	 */
	@PostMapping(value = "/getMyActionList")
	public SkillMatrixResponse getMyActionList(@RequestBody SkillMatrixRequest request) {
		LOGGER.info("# WorkflowController || getMyActionList ");
		return workflowService.getMyActionList(request);
	}

	/**
	 * @author Ananta K. August 16, 2023 02:00:41 PM
	 * @param request
	 * @return SkillMatrixResponse
	 * @desc In This API Save Stage One Submission Details
	 * @comments
	 */
	@PostMapping(value = "/stageOneSubmission")
	public SkillMatrixResponse stageOneSubmission(@RequestBody SkillMatrixRequest request) {
		LOGGER.info("# WorkflowController || stageOneSubmission ");
		return workflowService.stageOneSubmission(request);
	}

	/**
	 * @author Ananta K. August 23, 2023 02:00:41 PM
	 * @param request
	 * @return SkillMatrixResponse
	 * @desc In This API Stage 2 Submission Details
	 * @comments
	 */
	@PostMapping(value = "/stageTwoSubmission")
	public SkillMatrixResponse saveStageTwoSubmission(@RequestBody SkillMatrixRequest request) {
		LOGGER.info("# WorkflowController || saveStageTwoSubmission ");
		return workflowService.saveStageTwoSubmission(request);
	}

	/**
	 * @author rakeshkumarchoudhary
	 * @param request
	 * @return
	 */
	@PostMapping(value = "/stageTwoVerificationSubmission")
	public SkillMatrixResponse stageTwoVerificationSubmission(@RequestBody SkillMatrixRequest request) {
		LOGGER.info("# WorkflowController || stageTwoVerificationSubmission ");
		return workflowService.stageTwoVerificationSubmission(request);
	}

	/**
	 * @author Rajdeep MD 23-Aug-2023 , 10:00 AM
	 * @param request
	 * @return SkillMatrixResponse
	 * @desc stage three submission
	 */
	@PostMapping(value = "/stageThreeSubmission")
	public SkillMatrixResponse stageThreeSubmission(@RequestBody SkillMatrixRequest request) {
		LOGGER.info("# WorkflowController || submittedByStageThree ");
		return workflowService.stageThreeSubmission(request);
	}

	/**
	 * @author Rajdeep MD 23-Aug-2023 , 10:00 AM
	 * @param request
	 * @return SkillMatrixResponse
	 * @desc stage four submission
	 */
	@PostMapping(value = "/stageFourSubmission")
	public SkillMatrixResponse stageFourSubmission(@RequestBody SkillMatrixRequest request) {
		LOGGER.info("# WorkflowController || submittedByStageFour ");
		return workflowService.stageFourSubmission(request);
	}

	/**
	 * @author Anant K. September 01, 2023
	 * @param request
	 * @return SkillMatrixResponse
	 * @desc
	 */
	@PostMapping(value = "/stageFiveSubmission")
	public SkillMatrixResponse stageFiveSubmission(@RequestBody SkillMatrixRequest request) {
		LOGGER.info("# SettingController || stageFiveSubmission ");
		return workflowService.stageFiveSubmission(request);
	}

	/**
	 * @author Rakesh Aug 24, 2023
	 * @param request
	 * @return SkillMatrixResponse
	 * @desc This API will fetch skill
	 */
	@PostMapping(value = "/getSkillMatrixList")
	public SkillMatrixResponse getSkillMatrixList(@RequestBody SkillMatrixRequest request) {
		return workflowService.getSkillMatrixList(request);
	}

	/**
	 * @author Ananta K. August 25, 2023 02:00:41 AM
	 * @param request
	 * @return SkillMatrixResponse
	 * @desc In This API Get My Action Details
	 * @comments
	 */
	@PostMapping(value = "/getMyActionDetails")
	public SkillMatrixResponse getMyActionDetails(@RequestBody SkillMatrixRequest request) {
		LOGGER.info("# WorkflowController || getMyActionDetails ");
		return workflowService.getMyActionDetails(request);
	}

	/**
	 * @author Sonali L. Aug 25, 2023
	 * @param request
	 * @return SkillMatrixResponse
	 * @desc This API fetch Assigned Assessment Details
	 */

	@GetMapping(value = "/getAssignedAssessmentDetails/{assessmentId}")
	public SkillMatrixResponse getAssignedAssessmentDetails(@PathVariable("assessmentId") long assessmentId) {
		LOGGER.info("# SettingController || getAssignedAssessmentDetails ");
		return workflowService.getAssignedAssessmentDetails(assessmentId);

	}

	/**
	 * @author Sonali L. Aug 28, 2023
	 * @param request
	 * @return SkillMatrixResponse
	 * @desc This API fetch Assessment List
	 */

	@PostMapping(value = "/getOJTAssessmentsList")
	public SkillMatrixResponse getOJTAssessmentsList(@RequestBody SkillMatrixRequest request) {
		LOGGER.info("# SettingController || getOJTAssessmentsList ");
		return workflowService.getOJTAssessmentsList(request);

	}

	/**
	 * @author Sonali L. Aug 28, 2023
	 * @param request
	 * @return SkillMatrixResponse
	 * @desc This API fetch SkillMatrix List
	 */

	@PostMapping(value = "/getOJTSkillMatrixList")
	public SkillMatrixResponse getOJTSkillMatrixList(@RequestBody SkillMatrixRequest request) {
		LOGGER.info("# SettingController || getOJTSkillMatrixList ");
		return workflowService.getOJTSkillMatrixList(request);

	}

	/**
	 * @author Sonali L. Aug 28, 2023
	 * @param request
	 * @return SkillMatrixResponse
	 * @desc This API fetch Certificate List
	 */

	@PostMapping(value = "/getCertificateList")
	public SkillMatrixResponse getCertificateList(@RequestBody SkillMatrixRequest request) {
		LOGGER.info("# SettingController || getCertificateList ");
		return workflowService.getCertificateList(request);
	}

	/**
	 * @author Rajdeep MD 28-Aug-2023 , 10:00 AM
	 * @param request
	 * @return SkillMatrixResponse
	 * @desc get Skill Matrix OJT Plan list
	 */
	@PostMapping(value = "/getSkillMatrixEmpList")
	public SkillMatrixResponse getSkillMatrixEmpList(@RequestBody SkillMatrixRequest request) {
		LOGGER.info("# WorkflowController || getSkillMatrixEmpList ");
		return workflowService.getSkillMatrixEmpList(request);
	}

	/**
	 * @author Rajdeep MD 28-Aug-2023 , 10:00 AM
	 * @param request
	 * @return SkillMatrixResponse
	 * @desc get Skill Matrix OJT Plan list
	 */
	@PostMapping(value = "/submitOJTPlan")
	public SkillMatrixResponse submitOJTPlan(@RequestBody SkillMatrixRequest request) {
		LOGGER.info("# WorkflowController || submitOJTPlan ");
		return workflowService.submitOJTPlan(request);
	}

	/**
	 * @author Rajdeep MD 28-Aug-2023 , 10:00 AM
	 * @param request
	 * @return SkillMatrixResponse
	 * @desc get my skilling cetificate List
	 */
	@PostMapping(value = "/getMySkillingCertificateList")
	public SkillMatrixResponse getMySkillingCertificateList(@RequestBody SkillMatrixRequest request) {
		LOGGER.info("# WorkflowController || getMySkillingCertificateList ");
		return workflowService.getMySkillingCertificateList(request);
	}

	/**
	 * @author Rajdeep MD 28-Aug-2023 , 10:00 AM
	 * @param request
	 * @return SkillMatrixResponse
	 * @desc get ojt plan List
	 */
	@PostMapping(value = "/getOJTPlanList")
	public SkillMatrixResponse getOJTPlanList(@RequestBody SkillMatrixRequest request) {
		LOGGER.info("# WorkflowController || getOJTPlanList ");
		return workflowService.getOJTPlanList(request);
	}

	/**
	 * @author Rajdeep MD 30-Aug-2023 , 10:00 AM
	 * @param request
	 * @return SkillMatrixResponse
	 * @desc get ojt plan details
	 */
	@GetMapping(value = "/getOJTPlanDetails/{ojtPlanId}")
	public SkillMatrixResponse getOJTPlanDetails(@PathVariable("ojtPlanId") long ojtPlanId) {
		LOGGER.info("# WorkflowController || getOJTPlanDetails ");
		return workflowService.getOJTPlanDetails(ojtPlanId);
	}

	/**
	 * @author Rajdeep MD 30-Aug-2023 , 10:00 AM
	 * @param request
	 * @return SkillMatrixResponse
	 * @desc delete ojt plan
	 */
	@GetMapping(value = "/deleteOJTPlan/{ojtPlanId}")
	public SkillMatrixResponse deleteOJTPlan(@PathVariable("ojtPlanId") long ojtPlanId) {
		LOGGER.info("# WorkflowController || deleteOJTPlan ");
		return workflowService.deleteOJTPlan(ojtPlanId);
	}

	/**
	 * @author Rajdeep MD 30-Aug-2023 , 10:00 AM
	 * @param request
	 * @return SkillMatrixResponse
	 * @desc update ojt plan
	 */
	@PostMapping(value = "/updateOJTPlan")
	public SkillMatrixResponse updateOJTPlan(@RequestBody SkillMatrixRequest request) {
		LOGGER.info("# WorkflowController || updateOJTPlan ");
		return workflowService.updateOJTPlan(request);
	}

	/**
	 * @author Rajdeep MD 30-Aug-2023 , 10:00 AM
	 * @param request
	 * @return SkillMatrixResponse
	 * @desc get Skill Matrix Action List
	 */
	@PostMapping(value = "/getSkillMatrixActionList")
	public SkillMatrixResponse getSkillMatrixActionList(@RequestBody SkillMatrixRequest request) {
		LOGGER.info("# WorkflowController || getSkillMatrixActionList ");
		return workflowService.getSkillMatrixActionList(request);
	}

	/**
	 * @author Rajdeep MD 30-Aug-2023 , 10:00 AM
	 * @param request
	 * @return SkillMatrixResponse
	 * @desc get employee Assessment Details submitted or pending by ojtAssessmentId
	 */
	@GetMapping(value = "/getOJTAssessmentDetails/{ojtAssessmentId}")
	public SkillMatrixResponse getOJTAssessmentDetails(@PathVariable("ojtAssessmentId") long ojtAssessmentId) {
		LOGGER.info("# WorkflowController || getOJTAssessmentDetails ");
		return workflowService.getOJTAssessmentDetails(ojtAssessmentId);
	}

	/**
	 * @author Anant K. September 04, 2023 11:00:41 AM
	 * @param request
	 * @return SkillMatrixResponse
	 * @desc In This API Save Work Force Deployment Details
	 * @comments
	 */
	@PostMapping(value = "/saveWorkForceDeployment")
	public SkillMatrixResponse saveWorkForceDeployment(@RequestBody SkillMatrixRequest request) {
		LOGGER.info("# WorkflowController || saveWorkForceDeployment ");
		return workflowService.saveWorkForceDeployment(request);
	}

	/**
	 * @author Anant K. September 04, 2023 11:00:41 AM
	 * @param request
	 * @return SkillMatrixResponse
	 * @desc In This API Get Work Force Deployment List
	 * @comments
	 */
	@PostMapping(value = "/getWorkForceDeploymentList")
	public SkillMatrixResponse getWorkForceDeploymentList(@RequestBody SkillMatrixRequest request) {
		LOGGER.info("# WorkflowController || getWorkForceDeploymentList ");
		return workflowService.getWorkForceDeploymentList(request);
	}

	@PostMapping(value = "/getWorkForceDeploymentDetails")
	public SkillMatrixResponse getWorkForceDeploymentDetails(@RequestBody SkillMatrixRequest request) {
		LOGGER.info("# WorkflowController || getWorkForceDeploymentDetails ");
		return workflowService.getWorkForceDeploymentDetails(request);
	}

	@PostMapping(value = "/getOJTRegistrationList")
	public SkillMatrixResponse getOJTRegistrationList(@RequestBody SkillMatrixRequest request) {
		LOGGER.info("# WorkflowController || getOJTRegistrationList ");
		return workflowService.getOJTRegistrationList(request);
	}

	@PostMapping(value = "/safteyAssessmentSubmission")
	public SkillMatrixResponse safteyAssessmentSubmission(@RequestBody SkillMatrixRequest request) {
		LOGGER.info("# WorkflowController || safteyAssessmentSubmission ");
		return workflowService.safteyAssessmentSubmission(request);
	}

	/**
	 * @author Sonali L. june 10, 2024
	 * @param request
	 * @return SkillMatrixResponse
	 * @desc 
	 * @comments
	 */
	@PostMapping(value = "/updateAssessmentTime")
	public SkillMatrixResponse updateAssessmentTime(@RequestBody SkillMatrixRequest request) {
		return workflowService.updateAssessmentTime(request);
	}

}
