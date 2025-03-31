package com.greentin.enovation.skillmatrix;

import java.util.HashMap;
import java.util.List;

import com.greentin.enovation.dto.SMAssessmentDTO;
import com.greentin.enovation.dto.SMOJTPlanDTO;
import com.greentin.enovation.dto.SMOJTSkillingAuditDTO;
import com.greentin.enovation.dto.SMOJTSkillingDTO;
import com.greentin.enovation.dto.SMShiftDTO;
import com.greentin.enovation.dto.SkillMatrixRequest;
import com.greentin.enovation.response.SkillMatrixResponse;

public interface WorkflowIDao {

	boolean stageFiveSubmission(SkillMatrixRequest request);

	boolean stageOneSubmission(SkillMatrixRequest request);

	boolean stageThreeSubmission(SkillMatrixRequest request);

	boolean stageFourSubmission(SkillMatrixRequest request);

//	HashMap<String, Object> getOJTRegistrationDetails(long oJTRegiId);

	boolean saveStageTwoSubmission(SkillMatrixRequest request);

	SMAssessmentDTO getAssignedAssessmentDetails(long assessmentId);

	boolean getMyActionList(SkillMatrixRequest request,SkillMatrixResponse response);
	
	void getMyActionDetails(SkillMatrixRequest request,SkillMatrixResponse response);

	boolean ojtRegistration(SkillMatrixRequest request);

	HashMap<String, Object> getSkillMatrixList(SkillMatrixRequest request);

	HashMap<String, Object> getSkillMatrixEmpList(SkillMatrixRequest request, SkillMatrixResponse response);

	boolean submitOJTPlan(SkillMatrixRequest request);

	List<SMOJTSkillingDTO> getMySkillingCertificateList(SkillMatrixRequest request);

	boolean stageTwoVerificationSubmission(SkillMatrixRequest request);

	List<HashMap<String, Object>> getOJTAssessmentsList(SkillMatrixRequest request, SkillMatrixResponse response);

	List<HashMap<String, Object>> getOJTSkillMatrixList(SkillMatrixRequest request, SkillMatrixResponse response);

	List<HashMap<String, Object>> getCertificateList(SkillMatrixRequest request, SkillMatrixResponse response);

	List<HashMap<String, Object>> getOJTPlanList(SkillMatrixRequest request, SkillMatrixResponse response);

	SMOJTPlanDTO getOJTPlanDetails(long ojtPlanId);

	boolean deleteOJTPlan(long ojtPlanId);

	boolean updateOJTPlan(SkillMatrixRequest request);

	List<SMOJTPlanDTO> getSkillMatrixActionList(SkillMatrixRequest request, SkillMatrixResponse response);

	SMAssessmentDTO getOJTAssessmentDetails(long ojtAssessmentId);
	
	boolean saveWorkForceDeployment(SkillMatrixRequest request);

	List<HashMap<String, Object>> getWorkForceDeploymentList(SkillMatrixRequest request, SkillMatrixResponse response);

	HashMap<String, Object> getWorkForceDeploymentDetails(SkillMatrixRequest request, SkillMatrixResponse response);

	SkillMatrixResponse getOJTRegistrationDetail(long oJTRegiId, SkillMatrixResponse response);
	
	List<HashMap<String, Object>> getOJTRegistrationList(SkillMatrixRequest request, SkillMatrixResponse response);

	void getOJTRegistrationDetails(long oJTRegiId, SkillMatrixResponse response);

	boolean safteyAssessmentSubmission(SkillMatrixRequest request);

	boolean updateAssessmentTime(SkillMatrixRequest request);

}
