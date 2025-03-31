package com.greentin.enovation.skillmatrix;

import java.util.HashMap;
import java.util.List;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import com.google.gson.Gson;
import com.greentin.enovation.dto.SMAssessmentDTO;
import com.greentin.enovation.dto.SMOJTPlanDTO;
import com.greentin.enovation.dto.SMOJTSkillingAuditDTO;
import com.greentin.enovation.dto.SMOJTSkillingDTO;
import com.greentin.enovation.dto.SMShiftDTO;
import com.greentin.enovation.dto.SkillMatrixRequest;
import com.greentin.enovation.response.SkillMatrixResponse;
import com.greentin.enovation.utils.BuildResponse;
import com.greentin.enovation.utils.CommonUtils;
import com.greentin.enovation.utils.EnovationConstants;
import com.greentin.enovation.utils.EnovationException;

/**
 * @author Sonali L Aug 7, 2023
 * @desc Service layer for workstation
 */
@Service
public class WorkflowServiceImpl implements WorkflowIService {
	private static final Logger LOGGER = LoggerFactory.getLogger(WorkflowServiceImpl.class);

	@Autowired
	private WorkflowIDao workflowDao;

	@Autowired
	CommonUtils commonUtils;

	public SkillMatrixResponse stageFiveSubmission(SkillMatrixRequest request) {
		LOGGER.info("# SettingServiceImpl || stageFiveSubmision");
		SkillMatrixResponse response = new SkillMatrixResponse();
		String jsonRequest = new Gson().toJson(request);
		boolean flag = false;
		try {

			flag = workflowDao.stageFiveSubmission(request);
			if (flag) {
				response = BuildResponse.success(response);
			} else {
				response = BuildResponse.fail(response);
			}
		} catch (EnovationException e) {
			e.printStackTrace();
			response = BuildResponse.fail100(response);
			response.setReason(e.getReason());
		} catch (Exception e) {
			e.printStackTrace();
			LOGGER.info("# SettingServiceImpl || stageFiveSubmission - {}", e.getMessage());
			response = BuildResponse.internalServerError(response);
			sendExceptionEmail(jsonRequest, e);
		}
		return response;
	}

	public SkillMatrixResponse ojtRegistration(SkillMatrixRequest request) {
		LOGGER.info("# WorkflowServiceImpl || ojtRegistration");
		SkillMatrixResponse response = new SkillMatrixResponse();
		String jsonRequest = new Gson().toJson(request);
		try {
			boolean flag = workflowDao.ojtRegistration(request);
			if (flag) {
				response = BuildResponse.success(response);
			} else {
				response = BuildResponse.fail(response);
			}
		} catch (EnovationException e) {
			e.printStackTrace();
			response = BuildResponse.fail100(response);
			response.setReason(e.getReason());
		} catch (Exception e) {
			e.printStackTrace();
			LOGGER.info("# WorkflowServiceImpl || ojtRegistration - {}", e.getMessage());
			response = BuildResponse.internalServerError(response);
			sendExceptionEmail(jsonRequest, e);
		}
		return response;
	}

	@Override
	public SkillMatrixResponse stageOneSubmission(SkillMatrixRequest request) {
		LOGGER.info("# WorkflowServiceImpl || stageOneSubmission");
		SkillMatrixResponse response = new SkillMatrixResponse();
		String jsonRequest = new Gson().toJson(request);
		try {
			boolean flag = workflowDao.stageOneSubmission(request);
			if (flag) {
				response = BuildResponse.success(response);
			} else {
				response = BuildResponse.fail(response);
			}
		} catch (EnovationException e) {
			response = BuildResponse.fail100(response);
			response.setReason(e.getReason());
		} catch (Exception e) {
			e.printStackTrace();
			LOGGER.info("# WorkflowServiceImpl || stageOneSubmission - {}", e.getMessage());
			response = BuildResponse.internalServerError(response);
			sendExceptionEmail(jsonRequest, e);
		}
		return response;
	}

	@Override
	public SkillMatrixResponse stageTwoVerificationSubmission(SkillMatrixRequest request) {
		LOGGER.info("# WorkflowServiceImpl || stageTwoVerificationSubmission");
		SkillMatrixResponse response = new SkillMatrixResponse();
		String jsonRequest = new Gson().toJson(request);
		try {
			boolean flag = workflowDao.stageTwoVerificationSubmission(request);
			if (flag) {
				response = BuildResponse.success(response);
			} else {
				response = BuildResponse.fail(response);
			}
		} catch (EnovationException e) {
			response = BuildResponse.fail100(response);
			response.setReason(e.getReason());
		} catch (Exception e) {
			e.printStackTrace();
			LOGGER.info("# WorkflowServiceImpl || stageTwoVerificationSubmission - {}", e.getMessage());
			response = BuildResponse.internalServerError(response);
			sendExceptionEmail(jsonRequest, e);
		}
		return response;
	}

	@Override
	public SkillMatrixResponse stageThreeSubmission(SkillMatrixRequest request) {
		LOGGER.info("# WorkflowServiceImpl || submittedByStageThree");
		SkillMatrixResponse response = new SkillMatrixResponse();
		String jsonRequest = new Gson().toJson(request);
		try {
			boolean flag = workflowDao.stageThreeSubmission(request);
			if (flag) {
				response = BuildResponse.success(response);
			} else {
				response = BuildResponse.fail(response);
			}
		} catch (EnovationException e) {
			response = BuildResponse.fail100(response);
			response.setReason(e.getReason());
		} catch (Exception e) {
			e.printStackTrace();
			LOGGER.info("# WorkflowServiceImpl || submittedByStageThree - {}", e.getMessage());
			response = BuildResponse.internalServerError(response);
			sendExceptionEmail(jsonRequest, e);
		}
		return response;
	}

	private void sendExceptionEmail(String jsonRequest, Exception e) {
		commonUtils.sendAPIFailedExceptionMail("Exception Occured At - " + this.getClass().getSimpleName(),
				" <p><b> Method Name - </b>" + new Throwable().getStackTrace()[0].getMethodName() + " <br></p> "
						+ "<p><b> Request Body - </b>" + jsonRequest + "<p><b> Exception - </b>"
						+ ExceptionUtils.getStackTrace(e));
	}

	@Override
	public SkillMatrixResponse stageFourSubmission(SkillMatrixRequest request) {
		LOGGER.info("# WorkflowServiceImpl || submittedByStageFour");
		SkillMatrixResponse response = new SkillMatrixResponse();
		String jsonRequest = new Gson().toJson(request);
		try {
			boolean flag = workflowDao.stageFourSubmission(request);
			if (flag) {
				response = BuildResponse.success(response);
			} else {
				response = BuildResponse.fail(response);
			}
		} catch (EnovationException e) {
			response = BuildResponse.fail100(response);
			response.setReason(e.getReason());
		} catch (Exception e) {
			e.printStackTrace();
			LOGGER.info("# WorkflowServiceImpl || submittedByStageThree - {}", e.getMessage());
			response = BuildResponse.internalServerError(response);
			sendExceptionEmail(jsonRequest, e);
		}
		return response;
	}

	@Override
	public SkillMatrixResponse getOJTRegistrationDetails(long oJTRegiId) {
		LOGGER.info("# WorkflowServiceImpl || getOJTRegistrationDetails");
		SkillMatrixResponse response = new SkillMatrixResponse();
		try {
			if (oJTRegiId > 0) {
				workflowDao.getOJTRegistrationDetails(oJTRegiId, response);
				if (response != null) {
					response = BuildResponse.success(response);
				} else {
					response = BuildResponse.fail(response);
				}
//				HashMap<String, Object> data = workflowDao.getOJTRegistrationDetails(oJTRegiId);
//				if (!CollectionUtils.isEmpty(data)) {
//					response = BuildResponse.success(response);
//					response.setData(data);
//				} else {
//					response = BuildResponse.fail(response);
//				}
			} else {
				response = BuildResponse.badRequest(response);
			}
		} catch (EnovationException e) {
			response = BuildResponse.fail100(response);
			response.setReason(e.getReason());
		} catch (Exception e) {
			e.printStackTrace();
			response = BuildResponse.internalServerError(response);
			LOGGER.info("#In WorkflowServiceImpl |  INSIDE in getOJTRegistrationDetails Exception Occured {}",
					e.getMessage());
			String jsonRequest = new Gson().toJson(oJTRegiId);
			sendExceptionEmail(jsonRequest, e);
		}
		return response;
	}

	public SkillMatrixResponse getMyActionList(SkillMatrixRequest request) {
		LOGGER.info("#In WorkflowServiceImpl |  getMyActionList ");
		SkillMatrixResponse response = new SkillMatrixResponse();
		boolean flag = false;
		try {
			if (request.getEmpId() > 0) {
				flag = workflowDao.getMyActionList(request, response);
				if (flag) {
					response = BuildResponse.success(response);
				} else {
					response = BuildResponse.fail(response);
					response.setReason(SMConstant.DATA_NOT_FOUND);
				}
			} else {
				response = BuildResponse.badRequest(response);
			}
		} catch (EnovationException e) {
			response = BuildResponse.fail100(response);
			response.setReason(e.getReason());
		} catch (Exception e) {
			LOGGER.info("#In WorkflowServiceImpl |  getMyActionList Exception Occured - {}", e.getMessage());
			String jsonRequest = new Gson().toJson(request);
			response = BuildResponse.internalServerError(response);
			sendExceptionEmail(jsonRequest, e);
		}
		return response;
	}

	@Override
	public SkillMatrixResponse saveStageTwoSubmission(SkillMatrixRequest request) {
		LOGGER.info("# WorkflowServiceImpl || saveStageTwoSubmission");
		SkillMatrixResponse response = new SkillMatrixResponse();
		String jsonRequest = new Gson().toJson(request);
		try {
			boolean flag = workflowDao.saveStageTwoSubmission(request);
			if (flag) {
				response = BuildResponse.success(response);
			} else {
				response = BuildResponse.fail(response);
			}
		} catch (EnovationException e) {
			e.printStackTrace();
			response = BuildResponse.fail100(response);
			response.setReason(e.getReason());
		} catch (Exception e) {
			e.printStackTrace();
			LOGGER.info("# WorkflowServiceImpl || saveStageTwoSubmission - {}", e.getMessage());
			response = BuildResponse.internalServerError(response);
			sendExceptionEmail(jsonRequest, e);
		}
		return response;
	}

	@Override
	public SkillMatrixResponse getSkillMatrixList(SkillMatrixRequest request) {
		LOGGER.info("# WorkflowServiceImpl || getSkillMatrixList");
		SkillMatrixResponse response = new SkillMatrixResponse();
		String jsonRequest = new Gson().toJson(request);
		try {
			HashMap<String, Object> dataList = workflowDao.getSkillMatrixList(request);
			if (dataList != null) {
				response = BuildResponse.success(response);
				response.setData(dataList);
			} else {
				response = BuildResponse.fail(response);
			}
		} catch (EnovationException e) {
			response = BuildResponse.fail100(response);
			response.setReason(e.getReason());
		} catch (Exception e) {
			e.printStackTrace();
			LOGGER.info("# WorkflowServiceImpl || getSkillMatrixList - {}", e.getMessage());
			response = BuildResponse.internalServerError(response);
			sendExceptionEmail(jsonRequest, e);
		}
		return response;
	}

	@Override
	public SkillMatrixResponse getMyActionDetails(SkillMatrixRequest request) {
		LOGGER.info("#In WorkflowServiceImpl |  getMyActionDetails ");
		SkillMatrixResponse response = new SkillMatrixResponse();
		try {
			if (request.getSkillingAuditId() > 0) {
				workflowDao.getMyActionDetails(request, response);
				if (response != null) {
					response = BuildResponse.success(response);
				} else {
					response = BuildResponse.fail(response);
				}
			} else {
				response = BuildResponse.badRequest(response);
			}
		} catch (EnovationException e) {
			response = BuildResponse.fail100(response);
			response.setReason(e.getReason());
		} catch (Exception e) {
			e.printStackTrace();
			LOGGER.info("#In WorkflowServiceImpl |  getMyActionDetails Exception Occured - {}", e.getMessage());
			String jsonRequest = new Gson().toJson(request);
			response = BuildResponse.internalServerError(response);
			sendExceptionEmail(jsonRequest, e);
		}
		return response;
	}

	@Override
	public SkillMatrixResponse getAssignedAssessmentDetails(long assessmentId) {
		LOGGER.info("# WorkflowServiceImpl || getAssignedAssessmentDetails");
		SkillMatrixResponse response = new SkillMatrixResponse();
		String jsonRequest = new Gson().toJson(assessmentId);
		try {
			SMAssessmentDTO list = workflowDao.getAssignedAssessmentDetails(assessmentId);
			if (list != null) {
				response = BuildResponse.success(response);
				response.setAssessment(list);
			} else {
				response = BuildResponse.fail(response);
			}
		} catch (EnovationException e) {
			response.setReason(e.getReason());
		} catch (Exception e) {
			e.printStackTrace();
			LOGGER.info("# WorkflowServiceImpl || getAssignedAssessmentDetails - {}", e.getMessage());
			response = BuildResponse.internalServerError(response);
			sendExceptionEmail(jsonRequest, e);
		}
		return response;

	}

	@Override
	public SkillMatrixResponse getOJTAssessmentsList(SkillMatrixRequest request) {
		LOGGER.info("# WorkflowServiceImpl || getOJTAssessmentsList");
		SkillMatrixResponse response = new SkillMatrixResponse();
		String jsonRequest = new Gson().toJson(request);
		try {
			List<HashMap<String, Object>> list = workflowDao.getOJTAssessmentsList(request, response);
			if (!CollectionUtils.isEmpty(list)) {
				response = BuildResponse.success(response);
				response.setDataList(list);
			} else {

				response = BuildResponse.fail(response);
			}
		} catch (EnovationException e) {
			response.setReason(e.getReason());
		} catch (Exception e) {
			e.printStackTrace();
			LOGGER.info("# WorkflowServiceImpl || getOJTAssessmentsList - {}", e.getMessage());
			response = BuildResponse.internalServerError(response);
			sendExceptionEmail(jsonRequest, e);
		}
		return response;

	}

	public SkillMatrixResponse getSkillMatrixEmpList(SkillMatrixRequest request) {
		LOGGER.info("# WorkflowServiceImpl || getSkillMatrixEmpList");
		SkillMatrixResponse response = new SkillMatrixResponse();
		String jsonRequest = new Gson().toJson(request);
		try {
			HashMap<String, Object> dataList = workflowDao.getSkillMatrixEmpList(request, response);
			if (dataList != null) {
				response = BuildResponse.success(response);
				response.setData(dataList);
			} else {
				response = BuildResponse.fail(response);
			}
		} catch (EnovationException e) {
			response = BuildResponse.fail100(response);
			response.setReason(e.getReason());
		} catch (Exception e) {
			e.printStackTrace();
			LOGGER.info("# WorkflowServiceImpl || getSkillMatrixEmpList - {}", e.getMessage());
			response = BuildResponse.internalServerError(response);
			sendExceptionEmail(jsonRequest, e);
		}
		return response;
	}

	@Override
	public SkillMatrixResponse submitOJTPlan(SkillMatrixRequest request) {
		LOGGER.info("# WorkflowServiceImpl || submitOJTPlan");
		SkillMatrixResponse response = new SkillMatrixResponse();
		String jsonRequest = new Gson().toJson(request);
		try {
			boolean flag = workflowDao.submitOJTPlan(request);
			if (flag) {
				response = BuildResponse.success(response);
			} else {
				response = BuildResponse.fail(response);
			}
		} catch (EnovationException e) {
			response = BuildResponse.fail100(response);
			response.setReason(e.getReason());
			response.setErrorList(e.getList());
		} catch (Exception e) {
			e.printStackTrace();
			LOGGER.info("# WorkflowServiceImpl || submitOJTPlan - {}", e.getMessage());
			response = BuildResponse.internalServerError(response);
			sendExceptionEmail(jsonRequest, e);
		}
		return response;
	}

	@Override
	public SkillMatrixResponse getMySkillingCertificateList(SkillMatrixRequest request) {
		LOGGER.info("#In WorkflowServiceImpl |  getMySkillingCertificateList ");
		SkillMatrixResponse response = new SkillMatrixResponse();
		try {
			if (request.getBranchId() > 0) {
				List<SMOJTSkillingDTO> list = workflowDao.getMySkillingCertificateList(request);
				if (!CollectionUtils.isEmpty(list)) {
					response = BuildResponse.success(response);
					response.setActionList(list);
				} else {
					response = BuildResponse.fail(response);
				}
			} else {
				response = BuildResponse.badRequest(response);
			}
		} catch (EnovationException e) {
			response = BuildResponse.fail100(response);
			response.setReason(e.getReason());
		} catch (Exception e) {
			e.printStackTrace();
			LOGGER.info(
					"#In WorkflowServiceImpl |  getMySkillingCertificateList Exception Occured - {}" + e.getMessage());
			String jsonRequest = new Gson().toJson(request);
			response = BuildResponse.internalServerError(response);
			sendExceptionEmail(jsonRequest, e);
		}
		return response;
	}

	@Override
	public SkillMatrixResponse getOJTSkillMatrixList(SkillMatrixRequest request) {
		LOGGER.info("# WorkflowServiceImpl || getOJTSkillMatrixList");
		SkillMatrixResponse response = new SkillMatrixResponse();
		String jsonRequest = new Gson().toJson(request);
		try {
			List<HashMap<String, Object>> list = workflowDao.getOJTSkillMatrixList(request, response);
			if (!CollectionUtils.isEmpty(list)) {
				response = BuildResponse.success(response);
				response.setDataList(list);
			} else {

				response = BuildResponse.fail(response);
			}
		} catch (EnovationException e) {
			response.setReason(e.getReason());
		} catch (Exception e) {
			e.printStackTrace();
			LOGGER.info("# WorkflowServiceImpl || getOJTSkillMatrixList - {}", e.getMessage());
			response = BuildResponse.internalServerError(response);
			sendExceptionEmail(jsonRequest, e);
		}
		return response;

	}

	@Override
	public SkillMatrixResponse getCertificateList(SkillMatrixRequest request) {
		LOGGER.info("# WorkflowServiceImpl || getCertificateList");
		SkillMatrixResponse response = new SkillMatrixResponse();
		String jsonRequest = new Gson().toJson(request);
		try {
			List<HashMap<String, Object>> list = workflowDao.getCertificateList(request, response);
			if (!CollectionUtils.isEmpty(list)) {
				response = BuildResponse.success(response);
				response.setDataList(list);
			} else {

				response = BuildResponse.fail(response);
			}
		} catch (EnovationException e) {
			response.setReason(e.getReason());
		} catch (Exception e) {
			e.printStackTrace();
			LOGGER.info("# WorkflowServiceImpl || getCertificateList - {}", e.getMessage());
			response = BuildResponse.internalServerError(response);
			sendExceptionEmail(jsonRequest, e);
		}
		return response;

	}

	@Override
	public SkillMatrixResponse getOJTPlanList(SkillMatrixRequest request) {
		LOGGER.info("#In WorkflowServiceImpl |  getOJTPlanList ");
		SkillMatrixResponse response = new SkillMatrixResponse();
		try {
			if (request.getOrgId() > 0) {
				List<HashMap<String, Object>> list = workflowDao.getOJTPlanList(request, response);
				if (!CollectionUtils.isEmpty(list)) {
					response = BuildResponse.success(response);
					response.setDataList(list);
				} else {
					response = BuildResponse.fail(response);
					response.setReason(EnovationConstants.DATANOTFOUND);
				}
			} else {
				response = BuildResponse.badRequest(response);
			}
		} catch (EnovationException e) {
			response = BuildResponse.fail100(response);
			response.setReason(e.getReason());
		} catch (Exception e) {
			e.printStackTrace();
			LOGGER.info("#In WorkflowServiceImpl |  getOJTPlanList Exception Occured - {}", e.getMessage());
			String jsonRequest = new Gson().toJson(request);
			response = BuildResponse.internalServerError(response);
			sendExceptionEmail(jsonRequest, e);
		}
		return response;
	}

	@Override
	public SkillMatrixResponse getOJTPlanDetails(long ojtPlanId) {
		LOGGER.info("# WorkflowServiceImpl || getOJTPlanDetails");
		SkillMatrixResponse response = new SkillMatrixResponse();
		String jsonRequest = new Gson().toJson(ojtPlanId);
		try {
			SMOJTPlanDTO data = workflowDao.getOJTPlanDetails(ojtPlanId);
			if (data != null) {
				response = BuildResponse.success(response);
				response.setOjtPlan(data);
			} else {
				response = BuildResponse.fail(response);
			}
		} catch (EnovationException e) {
			response.setReason(e.getReason());
		} catch (Exception e) {
			e.printStackTrace();
			LOGGER.info("# WorkflowServiceImpl || getOJTPlanDetails - {}", e.getMessage());
			response = BuildResponse.internalServerError(response);
			sendExceptionEmail(jsonRequest, e);
		}
		return response;
	}

	@Override
	public SkillMatrixResponse deleteOJTPlan(long ojtPlanId) {
		LOGGER.info("# WorkflowServiceImpl || deleteOJTPlan");
		SkillMatrixResponse response = new SkillMatrixResponse();
		String jsonRequest = new Gson().toJson(ojtPlanId);
		try {
			boolean flag = workflowDao.deleteOJTPlan(ojtPlanId);
			if (flag) {
				response = BuildResponse.success(response);
			} else {
				response = BuildResponse.fail(response);
			}
		} catch (EnovationException e) {
			response.setReason(e.getReason());
		} catch (Exception e) {
			e.printStackTrace();
			LOGGER.info("# WorkflowServiceImpl || deleteOJTPlan - {}", e.getMessage());
			response = BuildResponse.internalServerError(response);
			sendExceptionEmail(jsonRequest, e);
		}
		return response;
	}

	@Override
	public SkillMatrixResponse updateOJTPlan(SkillMatrixRequest request) {
		LOGGER.info("# WorkflowServiceImpl || updateOJTPlan");
		SkillMatrixResponse response = new SkillMatrixResponse();
		String jsonRequest = new Gson().toJson(request);
		try {
			boolean flag = workflowDao.updateOJTPlan(request);
			if (flag) {
				response = BuildResponse.success(response);
			} else {
				response = BuildResponse.fail(response);
			}
		} catch (EnovationException e) {
			response = BuildResponse.fail100(response);
			response.setReason(e.getReason());
			response.setErrorList(e.getList());
		} catch (Exception e) {
			e.printStackTrace();
			LOGGER.info("# WorkflowServiceImpl || updateOJTPlan - {}", e.getMessage());
			response = BuildResponse.internalServerError(response);
			sendExceptionEmail(jsonRequest, e);
		}
		return response;
	}

	@Override
	public SkillMatrixResponse getSkillMatrixActionList(SkillMatrixRequest request) {
		LOGGER.info("#In WorkflowServiceImpl |  getSkillMatrixActionList ");
		SkillMatrixResponse response = new SkillMatrixResponse();
		try {
			List<SMOJTPlanDTO> list = workflowDao.getSkillMatrixActionList(request, response);
			if (!CollectionUtils.isEmpty(list)) {
				response = BuildResponse.success(response);
				response.setSmActionList(list);
			} else {
				response = BuildResponse.fail(response);
			}
		} catch (EnovationException e) {
			response = BuildResponse.fail100(response);
			response.setReason(e.getReason());
		} catch (Exception e) {
			e.printStackTrace();
			LOGGER.info("#In WorkflowServiceImpl |  getSkillMatrixActionList Exception Occured - {}", e.getMessage());
			String jsonRequest = new Gson().toJson(request);
			response = BuildResponse.internalServerError(response);
			sendExceptionEmail(jsonRequest, e);
		}
		return response;
	}

	@Override
	public SkillMatrixResponse getOJTAssessmentDetails(long ojtAssessmentId) {
		LOGGER.info("# WorkflowServiceImpl || getOJTAssessmentDetails");
		SkillMatrixResponse response = new SkillMatrixResponse();
		String jsonRequest = new Gson().toJson(ojtAssessmentId);
		try {
			SMAssessmentDTO list = workflowDao.getOJTAssessmentDetails(ojtAssessmentId);
			if (list != null) {
				response = BuildResponse.success(response);
				response.setAssessment(list);
			} else {
				response = BuildResponse.fail(response);
			}
		} catch (EnovationException e) {
			response.setReason(e.getReason());
		} catch (Exception e) {
			e.printStackTrace();
			LOGGER.info("# WorkflowServiceImpl || getOJTAssessmentDetails - {}", e.getMessage());
			response = BuildResponse.internalServerError(response);
			sendExceptionEmail(jsonRequest, e);
		}
		return response;

	}

	@Override
	public SkillMatrixResponse saveWorkForceDeployment(SkillMatrixRequest request) {
		LOGGER.info("# WorkflowServiceImpl || saveWorkForceDeployment");
		SkillMatrixResponse response = new SkillMatrixResponse();
		String jsonRequest = new Gson().toJson(request);
		try {
			boolean flag = workflowDao.saveWorkForceDeployment(request);
			if (flag) {
				response = BuildResponse.success(response);
			} else {
				response = BuildResponse.fail(response);
			}
		} catch (EnovationException e) {
			response = BuildResponse.fail100(response);
			e.printStackTrace();
			response.setReason(e.getReason());
		} catch (Exception e) {
			e.printStackTrace();
			LOGGER.info("# WorkflowServiceImpl || saveWorkForceDeployment - {}", e.getMessage());
			response = BuildResponse.internalServerError(response);
			sendExceptionEmail(jsonRequest, e);
		}
		return response;
	}

	@Override
	public SkillMatrixResponse getWorkForceDeploymentList(SkillMatrixRequest request) {
		LOGGER.info("# WorkflowServiceImpl || getWorkForceDeploymentList");
		SkillMatrixResponse response = new SkillMatrixResponse();
		try {
			List<HashMap<String, Object>> list = workflowDao.getWorkForceDeploymentList(request, response);
			if (!CollectionUtils.isEmpty(list)) {
				response = BuildResponse.success(response);
				response.setDataList(list);
			} else {

				response = BuildResponse.fail(response);
			}

		} catch (EnovationException e) {
			response = BuildResponse.fail100(response);
			response.setReason(e.getReason());
		} catch (Exception e) {
			e.printStackTrace();
			LOGGER.info("# WorkflowServiceImpl || getWorkForceDeploymentList - {}", e.getMessage());
			response = BuildResponse.internalServerError(response);
			sendExceptionEmail("", e);
		}
		return response;
	}

	@Override
	public SkillMatrixResponse getWorkForceDeploymentDetails(SkillMatrixRequest request) {
		LOGGER.info("# WorkflowServiceImpl || getWorkForceDeploymentDetails");
		SkillMatrixResponse response = new SkillMatrixResponse();
		String jsonRequest = new Gson().toJson(request);
		try {
			HashMap<String, Object> dataList = workflowDao.getWorkForceDeploymentDetails(request, response);
			if (dataList != null) {
				response = BuildResponse.success(response);
				response.setData(dataList);
			} else {
				response = BuildResponse.fail(response);
			}
		} catch (EnovationException e) {
			response = BuildResponse.fail100(response);
			response.setReason(e.getReason());
		} catch (Exception e) {
			e.printStackTrace();
			LOGGER.info("# WorkflowServiceImpl || getWorkForceDeploymentDetails - {}", e.getMessage());
			response = BuildResponse.internalServerError(response);
			sendExceptionEmail(jsonRequest, e);
		}
		return response;
	}

	@SuppressWarnings("unused")
	@Override
	public SkillMatrixResponse getOJTRegistrationDetail(long oJTRegiId) {
		LOGGER.info("# WorkflowServiceImpl || getOJTRegistrationDetail");
		SkillMatrixResponse response = new SkillMatrixResponse();
		try {
			if (oJTRegiId > 0) {
				workflowDao.getOJTRegistrationDetail(oJTRegiId, response);
				if (response != null) {
					response = BuildResponse.success(response);
//					response.setData(data);
				} else {
					response = BuildResponse.fail(response);
				}
			} else {
				response = BuildResponse.badRequest(response);
			}
		} catch (EnovationException e) {
			response = BuildResponse.fail100(response);
			response.setReason(e.getReason());
		} catch (Exception e) {
			e.printStackTrace();
			response = BuildResponse.internalServerError(response);
			LOGGER.info("#In WorkflowServiceImpl |  INSIDE in getOJTRegistrationDetail Exception Occured {}",
					e.getMessage());
			String jsonRequest = new Gson().toJson(oJTRegiId);
		}

		return response;
	}

	public SkillMatrixResponse getOJTRegistrationList(SkillMatrixRequest request) {
		LOGGER.info("#In WorkflowServiceImpl |  getOJTRegistrationList ");
		SkillMatrixResponse response = new SkillMatrixResponse();
		try {
			if (request.getOrgId() > 0) {
				List<HashMap<String, Object>> list = workflowDao.getOJTRegistrationList(request, response);
				if (!CollectionUtils.isEmpty(list)) {
					response = BuildResponse.success(response);
					response.setDataList(list);
				} else {
					response = BuildResponse.fail(response);
					response.setReason(EnovationConstants.DATANOTFOUND);
				}
			} else {
				response = BuildResponse.badRequest(response);
			}
		} catch (EnovationException e) {
			response = BuildResponse.fail100(response);
			response.setReason(e.getReason());
		} catch (Exception e) {
			e.printStackTrace();
			LOGGER.info("#In WorkflowServiceImpl |  getOJTRegistrationList Exception Occured - {}", e.getMessage());
			String jsonRequest = new Gson().toJson(request);
			response = BuildResponse.internalServerError(response);
			sendExceptionEmail(jsonRequest, e);
		}
		return response;
	}

	@Override
	public SkillMatrixResponse safteyAssessmentSubmission(SkillMatrixRequest request) {
		LOGGER.info("# SettingServiceImpl || safteyAssessmentSubmission");
		SkillMatrixResponse response = new SkillMatrixResponse();
		String jsonRequest = new Gson().toJson(request);
		boolean flag = false;
		try {

			flag = workflowDao.safteyAssessmentSubmission(request);
			if (flag) {
				response = BuildResponse.success(response);
			} else {
				response = BuildResponse.fail(response);
			}
		} catch (EnovationException e) {
			e.printStackTrace();
			response = BuildResponse.fail100(response);
			response.setReason(e.getReason());
		} catch (Exception e) {
			e.printStackTrace();
			LOGGER.info("# SettingServiceImpl || safteyAssessmentSubmission - {}", e.getMessage());
			response = BuildResponse.internalServerError(response);
			sendExceptionEmail(jsonRequest, e);
		}
		return response;
	}

	@Override
	public SkillMatrixResponse updateAssessmentTime(SkillMatrixRequest request) {
		LOGGER.info("# SettingServiceImpl || updateInterAssessmentTime");
		SkillMatrixResponse response = new SkillMatrixResponse();
		String jsonRequest = new Gson().toJson(request);
		boolean flag = false;
		try {

			flag = workflowDao.updateAssessmentTime(request);
			if (flag) {
				response = BuildResponse.success(response);
			} else {
				response = BuildResponse.fail(response);
			}
		} catch (EnovationException e) {
			response = BuildResponse.fail100(response);
			response.setReason(e.getReason());
		} catch (Exception e) {
			LOGGER.info("# SettingServiceImpl || updateAssessmentTime - {}", e.getMessage());
			response = BuildResponse.internalServerError(response);
			sendExceptionEmail(jsonRequest, e);
		}
		return response;
	}

}