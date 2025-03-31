package com.greentin.enovation.skillmatrix;

import com.greentin.enovation.dto.SkillMatrixRequest;
import com.greentin.enovation.response.SkillMatrixResponse;

public interface WorkflowIService {

	SkillMatrixResponse ojtRegistration(SkillMatrixRequest request);

	SkillMatrixResponse stageOneSubmission(SkillMatrixRequest request);

	SkillMatrixResponse saveStageTwoSubmission(SkillMatrixRequest request);

	SkillMatrixResponse stageThreeSubmission(SkillMatrixRequest request);

	SkillMatrixResponse stageFourSubmission(SkillMatrixRequest request);

	SkillMatrixResponse stageFiveSubmission(SkillMatrixRequest request);

	SkillMatrixResponse getOJTRegistrationDetails(long oJTRegiId);

	SkillMatrixResponse getAssignedAssessmentDetails(long assessmentId);

	SkillMatrixResponse getMyActionList(SkillMatrixRequest request);

	SkillMatrixResponse getSkillMatrixList(SkillMatrixRequest request);

	SkillMatrixResponse getMyActionDetails(SkillMatrixRequest request);

	SkillMatrixResponse getOJTAssessmentsList(SkillMatrixRequest request);

	SkillMatrixResponse getSkillMatrixEmpList(SkillMatrixRequest request);

	SkillMatrixResponse submitOJTPlan(SkillMatrixRequest request);

	SkillMatrixResponse getMySkillingCertificateList(SkillMatrixRequest request);

	SkillMatrixResponse getOJTSkillMatrixList(SkillMatrixRequest request);

	SkillMatrixResponse getCertificateList(SkillMatrixRequest request);

	SkillMatrixResponse stageTwoVerificationSubmission(SkillMatrixRequest request);

	SkillMatrixResponse getOJTPlanList(SkillMatrixRequest request);

	SkillMatrixResponse getOJTPlanDetails(long ojtPlanId);

	SkillMatrixResponse deleteOJTPlan(long ojtPlanId);

	SkillMatrixResponse updateOJTPlan(SkillMatrixRequest request);

	SkillMatrixResponse getSkillMatrixActionList(SkillMatrixRequest request);

	SkillMatrixResponse getOJTAssessmentDetails(long ojtAssessmentId);
	
	SkillMatrixResponse saveWorkForceDeployment(SkillMatrixRequest request);

	SkillMatrixResponse getWorkForceDeploymentList(SkillMatrixRequest request);

	SkillMatrixResponse getWorkForceDeploymentDetails(SkillMatrixRequest request);

	SkillMatrixResponse getOJTRegistrationDetail(long oJTRegiId);
	
	SkillMatrixResponse getOJTRegistrationList(SkillMatrixRequest request);

	SkillMatrixResponse safteyAssessmentSubmission(SkillMatrixRequest request);

	SkillMatrixResponse updateAssessmentTime(SkillMatrixRequest request);

}
