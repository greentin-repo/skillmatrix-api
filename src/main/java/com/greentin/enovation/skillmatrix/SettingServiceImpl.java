package com.greentin.enovation.skillmatrix;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.greentin.enovation.dto.SMAssessmentDTO;
import com.greentin.enovation.dto.SkillMatrixRequest;
import com.greentin.enovation.response.SkillMatrixResponse;
import com.greentin.enovation.utils.BuildResponse;
import com.greentin.enovation.utils.CommonUtils;
import com.greentin.enovation.utils.EnovationConstants;
import com.greentin.enovation.utils.EnovationException;
import com.jcraft.jsch.Session;
import org.springframework.web.bind.annotation.RequestBody;

/**
 * @author Sonali L Aug 7, 2023
 * @desc Service layer for Setup
 */
@Service
public class SettingServiceImpl implements SettingIService {
	private static final Logger LOGGER = LoggerFactory.getLogger(SettingServiceImpl.class);

	@Autowired
	private SettingIDao settingDao;

	@Autowired
	CommonUtils commonUtils;

	@Override
	public SkillMatrixResponse addAssessment(SkillMatrixRequest request) {
		LOGGER.info("#In SettingServiceImpl |  INSIDE in addAssessment ");
		SkillMatrixResponse response = new SkillMatrixResponse();
		String jsonRequest = new Gson().toJson(request);
		try {
			if (request != null) {
				SkillMatrixRequest obj = settingDao.addAssessment(request);
				if (obj != null) {
					response = BuildResponse.success(response);
					response.setResponseData(obj);
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
			LOGGER.info("#In SettingServiceImpl |  INSIDE in addAssessment Exception Occured {}", e.getMessage());
			response = BuildResponse.internalServerError(response);
			sendExceptionEmail(jsonRequest, e);
		}
		return response;
	}

	@Override
	public SkillMatrixResponse getLevelList() {
		LOGGER.info("# SettingServiceImpl || getLevelList");
		SkillMatrixResponse response = new SkillMatrixResponse();
		String jsonRequest = new Gson().toJson("");
		try {
			List<HashMap<String, Object>> list = settingDao.getLevelList();
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
			LOGGER.info("# SettingServiceImpl || getLevelList - {}" + e.getMessage());
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
	public SkillMatrixResponse addModelDetails(SkillMatrixRequest request) {
		LOGGER.info("# SettingServiceImpl || addModelDetails");
		SkillMatrixResponse response = new SkillMatrixResponse();
		String jsonRequest = new Gson().toJson(request);
		try {
			boolean flag = settingDao.addModelDetails(request);
			if (flag) {
				response = BuildResponse.success(response);
			} else {
				response = BuildResponse.fail(response);
			}
		} catch (EnovationException e) {
			response = BuildResponse.fail100(response);
			response.setReason(e.getReason());
		} catch (Exception e) {
			LOGGER.info("# SettingServiceImpl || addModelDetails - {}" + e.getMessage());
			response = BuildResponse.internalServerError(response);
			sendExceptionEmail(jsonRequest, e);
		}
		return response;
	}

	@Override
	public SkillMatrixResponse updateModelDetails(SkillMatrixRequest request) {
		LOGGER.info("# SettingServiceImpl || updateModelDetails");
		SkillMatrixResponse response = new SkillMatrixResponse();
		String jsonRequest = new Gson().toJson(request);
		try {
			boolean flag = settingDao.updateModelDetails(request);
			if (flag) {
				response = BuildResponse.success(response);
			} else {
				response = BuildResponse.fail(response);
			}
		} catch (EnovationException e) {
			response = BuildResponse.fail100(response);
			response.setReason(e.getReason());
		} catch (Exception e) {
			LOGGER.info("# SettingServiceImpl || updateModelDetails - {}" + e.getMessage());
			response = BuildResponse.internalServerError(response);
			sendExceptionEmail(jsonRequest, e);
		}
		return response;
	}

	@Override
	public SkillMatrixResponse deleteModelDetails(SkillMatrixRequest request) {
		LOGGER.info("# SettingServiceImpl || deleteModelDetails");
		SkillMatrixResponse response = new SkillMatrixResponse();
		String jsonRequest = new Gson().toJson(request);
		try {
			boolean flag = settingDao.deleteModelDetails(request);
			if (flag) {
				response = BuildResponse.success(response);
			} else {
				response = BuildResponse.fail(response);
			}
		} catch (EnovationException e) {
			response = BuildResponse.fail100(response);
			response.setReason(e.getReason());
		} catch (Exception e) {
			boolean flag = CommonUtils.isConstraintViolationException(e);
			if (flag) {
				response.setReason(EnovationConstants.ALREADY_IN_USE);
				response = BuildResponse.fail100(response);
			} else {
				LOGGER.info("# SettingServiceImpl || deleteModelDetails - {}" + e.getMessage());
				response = BuildResponse.internalServerError(response);
			}
			sendExceptionEmail(jsonRequest, e);
		}
		return response;
	}

	@Override
	public SkillMatrixResponse getModelList(SkillMatrixRequest request) {
		LOGGER.info("# SettingServiceImpl || getModelList");
		SkillMatrixResponse response = new SkillMatrixResponse();
		String jsonRequest = new Gson().toJson(request);
		try {
			List<HashMap<String, Object>> list = settingDao.getModelList(request, response);
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
			LOGGER.info("# SettingServiceImpl || getModelList - {}" + e.getMessage());
			response = BuildResponse.internalServerError(response);
			sendExceptionEmail(jsonRequest, e);
		}
		return response;
	}

	@Override
	public SkillMatrixResponse saveGapReason(SkillMatrixRequest request) {
		LOGGER.info("# SettingServiceImpl || saveGapReason");
		SkillMatrixResponse response = new SkillMatrixResponse();
		String jsonRequest = new Gson().toJson(request);
		try {
			boolean flag = settingDao.saveGapReason(request);
			if (flag) {
				response = BuildResponse.success(response);
			} else {
				response = BuildResponse.fail(response);
			}
		} catch (EnovationException e) {
			response = BuildResponse.fail100(response);
			response.setReason(e.getReason());
		} catch (Exception e) {
			LOGGER.info("# SettingServiceImpl || saveGapReason - {}" + e.getMessage());
			response = BuildResponse.internalServerError(response);
			sendExceptionEmail(jsonRequest, e);
		}
		return response;
	}

	@Override
	public SkillMatrixResponse updateGapReason(SkillMatrixRequest request) {
		LOGGER.info("# SettingServiceImpl || updateGapReason");
		SkillMatrixResponse response = new SkillMatrixResponse();
		String jsonRequest = new Gson().toJson(request);
		try {
			boolean flag = settingDao.updateGapReason(request);
			if (flag) {
				response = BuildResponse.success(response);
			} else {
				response = BuildResponse.fail(response);
			}
		} catch (EnovationException e) {
			response = BuildResponse.fail100(response);
			response.setReason(e.getReason());
		} catch (Exception e) {
			LOGGER.info("# SettingServiceImpl || updateGapReason - {}" + e.getMessage());
			response = BuildResponse.internalServerError(response);
			sendExceptionEmail(jsonRequest, e);
		}
		return response;
	}

	@Override
	public SkillMatrixResponse deleteGapReason(SkillMatrixRequest request) {
		LOGGER.info("# SettingServiceImpl || deleteGapReason");
		SkillMatrixResponse response = new SkillMatrixResponse();
		String jsonRequest = new Gson().toJson(request);
		try {
			boolean flag = settingDao.deleteGapReason(request);
			if (flag) {
				response = BuildResponse.success(response);
			} else {
				response = BuildResponse.fail(response);
			}
		} catch (EnovationException e) {
			response = BuildResponse.fail100(response);
			response.setReason(e.getReason());
		} catch (Exception e) {
			boolean flag = CommonUtils.isConstraintViolationException(e);
			if (flag) {
				response.setReason(EnovationConstants.ALREADY_IN_USE);
				response = BuildResponse.fail100(response);
			} else {
				LOGGER.info("# SettingServiceImpl || deleteGapReason - {}" + e.getMessage());
				response = BuildResponse.internalServerError(response);
			}
			sendExceptionEmail(jsonRequest, e);
		}
		return response;
	}

	@Override
	public SkillMatrixResponse getGapReasonList(SkillMatrixRequest request) {
		LOGGER.info("# SettingServiceImpl || getGapReasonList");
		SkillMatrixResponse response = new SkillMatrixResponse();
		String jsonRequest = new Gson().toJson(request);
		try {
			List<HashMap<String, Object>> list = settingDao.getGapReasonList(request, response);
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
			LOGGER.info("# SettingServiceImpl || getGapReasonList - {}", e.getMessage());
			response = BuildResponse.internalServerError(response);
			sendExceptionEmail(jsonRequest, e);
		}
		return response;
	}

	@Override
	public SkillMatrixResponse saveShiftDetails(SkillMatrixRequest request) {
		LOGGER.info("# SettingServiceImpl || saveShiftDetails");
		SkillMatrixResponse response = new SkillMatrixResponse();
		String jsonRequest = new Gson().toJson(request);
		try {
			boolean flag = settingDao.saveShiftDetails(request);
			if (flag) {
				response = BuildResponse.success(response);
			} else {
				response = BuildResponse.fail(response);
			}
		} catch (EnovationException e) {
			response = BuildResponse.fail100(response);
			response.setReason(e.getReason());
		} catch (Exception e) {
			LOGGER.info("# SettingServiceImpl || saveShiftDetails - {}" + e.getMessage());
			response = BuildResponse.internalServerError(response);
			sendExceptionEmail(jsonRequest, e);
		}
		return response;
	}

	@Override
	public SkillMatrixResponse updateShiftDetails(SkillMatrixRequest request) {
		LOGGER.info("# SettingServiceImpl || updateShiftDetails");
		SkillMatrixResponse response = new SkillMatrixResponse();
		String jsonRequest = new Gson().toJson(request);
		try {
			boolean flag = settingDao.updateShiftDetails(request);
			if (flag) {
				response = BuildResponse.success(response);
			} else {
				response = BuildResponse.fail(response);
			}
		} catch (EnovationException e) {
			response = BuildResponse.fail100(response);
			response.setReason(e.getReason());
		} catch (Exception e) {
			LOGGER.info("# SettingServiceImpl || updateShiftDetails - {}" + e.getMessage());
			response = BuildResponse.internalServerError(response);
			sendExceptionEmail(jsonRequest, e);
		}
		return response;
	}

	@Override
	public SkillMatrixResponse deleteShiftDetails(SkillMatrixRequest request) {
		LOGGER.info("# SettingServiceImpl || deleteShiftDetails");
		SkillMatrixResponse response = new SkillMatrixResponse();
		String jsonRequest = new Gson().toJson(request);
		try {
			boolean flag = settingDao.deleteShiftDetails(request);
			if (flag) {
				response = BuildResponse.success(response);
			} else {
				response = BuildResponse.fail(response);
			}
		} catch (EnovationException e) {
			response = BuildResponse.fail100(response);
			response.setReason(e.getReason());
		} catch (Exception e) {

			if (CommonUtils.isConstraintViolationException(e)) {
				boolean result = settingDao.deActiveShiftDetails(request);
				if (result) {
					response = BuildResponse.success(response);
				} else {
					response = BuildResponse.fail(response);
				}
			} else {
				LOGGER.info("# SettingServiceImpl || deleteShiftDetails - {}" + e.getMessage());
				response = BuildResponse.internalServerError(response);
				sendExceptionEmail(jsonRequest, e);
			}
		}
		return response;
	}

	@Override
	public SkillMatrixResponse getShiftList(SkillMatrixRequest request) {
		LOGGER.info("# SettingServiceImpl || getShiftList");
		SkillMatrixResponse response = new SkillMatrixResponse();
		String jsonRequest = new Gson().toJson(request);
		try {
			List<HashMap<String, Object>> list = settingDao.getShiftList(request, response);
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
			LOGGER.info("# SettingServiceImpl || getShiftList - {}" + e.getMessage());
			response = BuildResponse.internalServerError(response);
			sendExceptionEmail(jsonRequest, e);
		}
		return response;
	}

	@Override
	public SkillMatrixResponse getStageList() {
		LOGGER.info("# SettingServiceImpl || getStageList");
		SkillMatrixResponse response = new SkillMatrixResponse();
		String jsonRequest = new Gson().toJson("");
		try {
			List<HashMap<String, Object>> list = settingDao.getStageList();
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
			LOGGER.info("# SettingServiceImpl || getStageList - {}" + e.getMessage());
			response = BuildResponse.internalServerError(response);
			sendExceptionEmail(jsonRequest, e);
		}
		return response;
	}

	@Override
	public SkillMatrixResponse saveWorkflowConfig(SkillMatrixRequest request) {
		LOGGER.info("# SettingServiceImpl || saveWorkflowConfig");
		SkillMatrixResponse response = new SkillMatrixResponse();
		String jsonRequest = new Gson().toJson(request);
		try {
			boolean flag = settingDao.saveWorkflowConfig(request);
			if (flag) {
				response = BuildResponse.success(response);
			} else {
				response = BuildResponse.fail(response);
			}
		} catch (EnovationException e) {
			response.setReason(e.getReason());
		} catch (Exception e) {
			LOGGER.info("# SettingServiceImpl || saveWorkflowConfig - {}" + e.getMessage());
			response = BuildResponse.internalServerError(response);
			sendExceptionEmail(jsonRequest, e);
		}
		return response;
	}

	@Override
	public SkillMatrixResponse deleteWorkflowConfig(SkillMatrixRequest request) {
		LOGGER.info("# SettingServiceImpl || deleteWorkflowConfig");
		SkillMatrixResponse response = new SkillMatrixResponse();
		String jsonRequest = new Gson().toJson(request);
		try {
			boolean flag = settingDao.deleteWorkflowConfig(request);
			if (flag) {
				response = BuildResponse.success(response);
			} else {
				response = BuildResponse.fail(response);
			}
		} catch (EnovationException e) {
			response = BuildResponse.fail100(response);
			response.setReason(e.getReason());
		} catch (Exception e) {

			if (CommonUtils.isConstraintViolationException(e)) {
				boolean result = settingDao.deActiveWorkflowConfig(request);
				if (result) {
					response = BuildResponse.success(response);
				} else {
					response = BuildResponse.fail(response);
				}
			} else {
				LOGGER.info("# SettingServiceImpl || deleteWorkflowConfig - {}" + e.getMessage());
				response = BuildResponse.internalServerError(response);
				sendExceptionEmail(jsonRequest, e);
			}
		}
		return response;
	}

	@Override
	public SkillMatrixResponse getWorkflowConfigList(int branchId, long levelId) {
		LOGGER.info("# SettingServiceImpl || getWorkflowConfigList");
		SkillMatrixResponse response = new SkillMatrixResponse();
		String jsonRequest = new Gson().toJson("");
		try {
			List<HashMap<String, Object>> list = settingDao.getWorkflowConfigList(branchId, levelId);
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
			LOGGER.info("# SettingServiceImpl || getWorkflowConfigList - {}" + e.getMessage());
			response = BuildResponse.internalServerError(response);
			sendExceptionEmail(jsonRequest, e);
		}
		return response;
	}

	@Override
	public SkillMatrixResponse getAssessmentDetail(long assessmentId) {
		LOGGER.info("#In SettingServiceImpl |  INSIDE in addAssessment ");
		SkillMatrixResponse response = new SkillMatrixResponse();
		try {
			if (assessmentId > 0) {
				SMAssessmentDTO list = settingDao.getAssessmentDetail(assessmentId);
				if (list != null) {
					response = BuildResponse.success(response);
					response.setAssessment(list);
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
			LOGGER.info("#In SettingServiceImpl |  INSIDE in addAssessment Exception Occured {}", e.getMessage());
			String jsonRequest = new Gson().toJson(assessmentId);
			response = BuildResponse.internalServerError(response);
			sendExceptionEmail(jsonRequest, e);
		}
		return response;
	}

	@Override
	public SkillMatrixResponse addAssessmentQuestion(SkillMatrixRequest request) {
		LOGGER.info("#In SettingServiceImpl |  INSIDE in addAssessmentQuestion ");
		SkillMatrixResponse response = new SkillMatrixResponse();
		String jsonRequest = new Gson().toJson(request);
		try {
			if (request != null) {
				boolean flag = settingDao.addAssessmentQuestion(request);
				if (flag) {
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
			LOGGER.info("#In SettingServiceImpl |  INSIDE in addAssessmentQuestion Exception Occured {}",
					e.getMessage());
			response = BuildResponse.internalServerError(response);
			sendExceptionEmail(jsonRequest, e);
		}
		return response;
	}

	@Override
	public SkillMatrixResponse getAssessmentList(SkillMatrixRequest request) {
		LOGGER.info("# SettingServiceImpl || getAssessmentList");
		SkillMatrixResponse response = new SkillMatrixResponse();
		String jsonRequest = new Gson().toJson(request);
		try {
			List<HashMap<String, Object>> list = settingDao.getAssessmentList(request, response);
			if (!CollectionUtils.isEmpty(list)) {
				response = BuildResponse.success(response);
				response.setDataList(list);
			} else {
				response = BuildResponse.fail(response);
				response.setReason(EnovationConstants.DATANOTFOUND);
			}
		} catch (EnovationException e) {
			response = BuildResponse.fail100(response);
			response.setReason(e.getReason());
		} catch (Exception e) {
			e.printStackTrace();
			LOGGER.info("# SettingServiceImpl || getAssessmentList - {}", e.getMessage());
			response = BuildResponse.internalServerError(response);
			sendExceptionEmail(jsonRequest, e);
		}
		return response;
	}

	@Override
	public SkillMatrixResponse updateAssessment(SkillMatrixRequest request) {
		LOGGER.info("#In SettingServiceImpl |  INSIDE in updateAssessment ");
		SkillMatrixResponse response = new SkillMatrixResponse();
		String jsonRequest = new Gson().toJson(request);
		try {
			if (request.getAssessmentId() > 0) {
				boolean flag = settingDao.updateAssessment(request);
				if (flag) {
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
			LOGGER.info("#In SettingServiceImpl |  INSIDE in updateAssessment Exception Occured {}", e.getMessage());
			response = BuildResponse.internalServerError(response);
			sendExceptionEmail(jsonRequest, e);
		}
		return response;
	}

	@Override
	public SkillMatrixResponse updateAssessmentQuestion(SkillMatrixRequest request) {
		LOGGER.info("#In SettingServiceImpl |  INSIDE in updateAssessmentQuestion ");
		SkillMatrixResponse response = new SkillMatrixResponse();
		String jsonRequest = new Gson().toJson(request);
		try {
			if (request.getAssessmentQueId() > 0) {
				boolean flag = settingDao.updateAssessmentQuestion(request);
				if (flag) {
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
			BuildResponse.internalServerError(response);
			LOGGER.info("#In SettingServiceImpl |  INSIDE in updateAssessmentQuestion Exception Occured {}",
					e.getMessage());
			response = BuildResponse.internalServerError(response);
			sendExceptionEmail(jsonRequest, e);
		}
		return response;
	}

	@Override
	public SkillMatrixResponse deleteAssessmentQuestion(SkillMatrixRequest request) {
		LOGGER.info("# SettingServiceImpl | deleteAssessmentQuestion");
		SkillMatrixResponse response = new SkillMatrixResponse();
		String jsonRequest = new Gson().toJson(request);
		try {
			boolean flag = settingDao.deleteAssessmentQuestion(request);
			if (flag) {
				response = BuildResponse.success(response);
			} else {
				response = BuildResponse.fail(response);
			}
		} catch (EnovationException e) {
			response = BuildResponse.fail100(response);
			response.setReason(e.getReason());
		} catch (Exception e) {
			if (CommonUtils.isConstraintViolationException(e)) {
				if (settingDao.deActiveQuetion(request)) {
					response = BuildResponse.success(response);
				} else {
					response = BuildResponse.fail100(response);
				}
			} else {
				LOGGER.info("# SettingServiceImpl | deleteAssessmentQuestion - {}", e.getMessage());
				response = BuildResponse.internalServerError(response);
				sendExceptionEmail(jsonRequest, e);
			}
		}
		return response;
	}

	public SkillMatrixResponse saveWorkstation(SkillMatrixRequest request) {
		LOGGER.info("# SettingServiceImpl || saveWorkstation");
		SkillMatrixResponse response = new SkillMatrixResponse();
		String jsonRequest = new Gson().toJson(request);
		try {
			boolean flag = settingDao.saveWorkstation(request);
			if (flag) {
				response = BuildResponse.success(response);
			} else {
				response = BuildResponse.fail(response);
			}
		} catch (EnovationException e) {
			response = BuildResponse.badRequest(response);
			response.setReason(e.getReason());
		} catch (Exception e) {
			e.printStackTrace();
			LOGGER.info("# SettingServiceImpl || saveWorkstation - {}", e.getMessage());
			response = BuildResponse.internalServerError(response);
			sendExceptionEmail(jsonRequest, e);
		}
		return response;
	}

	@Override
	public SkillMatrixResponse updateWorkstation(SkillMatrixRequest request) {
		LOGGER.info("# SettingServiceImpl || updateWorkstation");
		SkillMatrixResponse response = new SkillMatrixResponse();
		String jsonRequest = new Gson().toJson(request);
		try {
			boolean flag = settingDao.updateWorkstation(request);
			if (flag) {
				response = BuildResponse.success(response);
			} else {
				response = BuildResponse.fail(response);
			}
		} catch (EnovationException e) {
			response = BuildResponse.fail100(response);
			response.setReason(e.getReason());
		} catch (Exception e) {
			LOGGER.info("# SettingServiceImpl || updateWorkstation - {}", e.getMessage());
			response = BuildResponse.internalServerError(response);
			sendExceptionEmail(jsonRequest, e);
		}
		return response;
	}

	@Override
	public SkillMatrixResponse deActivateWorkstation(SkillMatrixRequest request) {
		LOGGER.info("# SettingServiceImpl || deActivateWorkstation");
		SkillMatrixResponse response = new SkillMatrixResponse();
		String jsonRequest = new Gson().toJson(request);
		try {
			boolean flag = settingDao.deleteWorkstation(request);
			if (flag) {
				response = BuildResponse.success(response);
			} else {
				response = BuildResponse.fail(response);
			}
		} catch (EnovationException e) {
			response = BuildResponse.fail100(response);
			response.setReason(e.getReason());
		} catch (Exception e) {
			if (CommonUtils.isConstraintViolationException(e)) {
				if (settingDao.deActivateWorkstation(request)) {
					response = BuildResponse.success(response);
				} else {

					response = BuildResponse.fail(response);
				}
			} else {
				LOGGER.info("# SettingServiceImpl || deActivateWorkstation - {}", e.getMessage());
				response = BuildResponse.internalServerError(response);
				sendExceptionEmail(jsonRequest, e);
			}
		}
		return response;
	}

	@Override
	public SkillMatrixResponse deleteAssessmentOptions(SkillMatrixRequest request) {
		LOGGER.info("# SettingServiceImpl | deleteAssessmentOptions");
		SkillMatrixResponse response = new SkillMatrixResponse();
		String jsonRequest = new Gson().toJson(request);
		try {
			boolean flag = settingDao.deleteAssessmentOptions(request);
			if (flag) {
				response = BuildResponse.success(response);
			} else {
				response = BuildResponse.fail(response);
			}
		} catch (EnovationException e) {
			response = BuildResponse.fail100(response);
			response.setReason(e.getReason());
		} catch (Exception e) {
			LOGGER.info("# SettingServiceImpl | deleteAssessmentOptions - {}", e.getMessage());
			response = BuildResponse.internalServerError(response);
			sendExceptionEmail(jsonRequest, e);
		}
		return response;
	}

	@Override
	public SkillMatrixResponse deActiveAssessment(SkillMatrixRequest request) {
		LOGGER.info("# SettingServiceImpl | deActiveAssessment");
		SkillMatrixResponse response = new SkillMatrixResponse();
		String jsonRequest = new Gson().toJson(request);
		try {
			boolean flag = settingDao.deActiveAssessment(request);
			if (flag) {
				response = BuildResponse.success(response);
			} else {
				response = BuildResponse.fail(response);
			}
		} catch (EnovationException e) {
			response = BuildResponse.fail100(response);
			response.setReason(e.getReason());
		} catch (Exception e) {
			LOGGER.info("# SettingServiceImpl | deActiveAssessment - {}", e.getMessage());
			response = BuildResponse.internalServerError(response);
			sendExceptionEmail(jsonRequest, e);
		}
		return response;

	}

	@Override
	public SkillMatrixResponse addChecksheet(SkillMatrixRequest request) {
		LOGGER.info("# SettingServiceImpl | addChecksheet");
		SkillMatrixResponse response = new SkillMatrixResponse();
		String jsonRequest = new Gson().toJson(request);
		try {
			SkillMatrixRequest obj = settingDao.addChecksheet(request);
			if (obj != null) {
				response = BuildResponse.success(response);
				response.setResponseData(obj);
			} else {
				response = BuildResponse.fail(response);
			}
		} catch (EnovationException e) {
			response = BuildResponse.fail100(response);
			response.setReason(e.getReason());
		} catch (Exception e) {
			LOGGER.info("# SettingServiceImpl | addChecksheet - {}", e.getMessage());
			response = BuildResponse.internalServerError(response);
			sendExceptionEmail(jsonRequest, e);
		}
		return response;
	}

	@Override
	public SkillMatrixResponse getChecksheetList(SkillMatrixRequest request) {
		LOGGER.info("# SettingServiceImpl || getChecksheetList");
		SkillMatrixResponse response = new SkillMatrixResponse();
		String jsonRequest = new Gson().toJson(request);
		try {
			List<HashMap<String, Object>> list = settingDao.getChecksheetList(request, response);
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
			LOGGER.info("# SettingServiceImpl || getChecksheetList - {}", e.getMessage());
			response = BuildResponse.internalServerError(response);
			sendExceptionEmail(jsonRequest, e);
		}
		return response;
	}

	@Override
	public SkillMatrixResponse updateChecksheet(SkillMatrixRequest request) {
		LOGGER.info("# SettingServiceImpl || updateChecksheet");
		SkillMatrixResponse response = new SkillMatrixResponse();
		String jsonRequest = new Gson().toJson(request);
		try {
			boolean falg = settingDao.updateChecksheet(request);
			if (falg) {
				response = BuildResponse.success(response);

			} else {
				response = BuildResponse.fail(response);
			}
		} catch (EnovationException e) {
			response = BuildResponse.fail100(response);
			response.setReason(e.getReason());
		} catch (Exception e) {
			LOGGER.info("# SettingServiceImpl || updateChecksheet - {}", e.getMessage());
			response = BuildResponse.internalServerError(response);
			sendExceptionEmail(jsonRequest, e);
		}
		return response;
	}

	@Override
	public SkillMatrixResponse deActiveChecksheet(SkillMatrixRequest request) {
		LOGGER.info("# SettingServiceImpl | deActiveChecksheet");
		SkillMatrixResponse response = new SkillMatrixResponse();
		String jsonRequest = new Gson().toJson(request);
		try {
			boolean flag = settingDao.deleteChecksheet(request);
			if (flag) {
				response = BuildResponse.success(response);
			} else {
				response = BuildResponse.fail(response);
			}
		} catch (EnovationException e) {
			response = BuildResponse.fail100(response);
			response.setReason(e.getReason());
		} catch (Exception e) {
			if (CommonUtils.isConstraintViolationException(e)) {
				if (settingDao.deActiveChecksheet(request)) {
					response = BuildResponse.success(response);
				} else {
					response = BuildResponse.fail(response);
				}
			} else {
				response = BuildResponse.internalServerError(response);
				sendExceptionEmail(jsonRequest, e);
			}
			LOGGER.info("# SettingServiceImpl | deActiveChecksheet -{} ", e.getMessage());
		}
		return response;
	}

	public SkillMatrixResponse getWorkstationList(SkillMatrixRequest request) {
		LOGGER.info("# SettingServiceImpl || getWorkstationList");
		SkillMatrixResponse response = new SkillMatrixResponse();
		String jsonRequest = new Gson().toJson(request);
		try {
			List<HashMap<String, Object>> list = settingDao.getWorkstationList(request, response);
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

			LOGGER.info("# SettingServiceImpl || getWorkstationList - {}", e.getMessage());
			response = BuildResponse.internalServerError(response);
			sendExceptionEmail(jsonRequest, e);
		}
		return response;
	}

	public SkillMatrixResponse getWorkstationMappingListByParentWorkstationId(SkillMatrixRequest request) {
		LOGGER.info("# SettingServiceImpl || getWorkstationList");
		SkillMatrixResponse response = new SkillMatrixResponse();
		String jsonRequest = new Gson().toJson(request);
		try {
			List<HashMap<String, Object>> list = settingDao.getWorkstationMappingListByParentWorkstationId(request, response);
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

			LOGGER.info("# SettingServiceImpl || getWorkstationList - {}", e.getMessage());
			response = BuildResponse.internalServerError(response);
			sendExceptionEmail(jsonRequest, e);
		}
		return response;
	}

	@Override
	public SkillMatrixResponse addChecksheetPoint(SkillMatrixRequest request) {
		LOGGER.info("# SettingServiceImpl | addChecksheetPoint");
		SkillMatrixResponse response = new SkillMatrixResponse();
		String jsonRequest = new Gson().toJson(request);
		try {
			boolean flag = settingDao.addChecksheetPoint(request);

			if (flag) {
				response = BuildResponse.success(response);
			} else {
				response = BuildResponse.fail(response);
			}
		} catch (EnovationException e) {
			response = BuildResponse.fail100(response);
			response.setReason(e.getReason());
			e.printStackTrace();
		} catch (Exception e) {
			LOGGER.info("# SettingServiceImpl | addChecksheetPoint - {}", e.getMessage());
			response = BuildResponse.internalServerError(response);
			sendExceptionEmail(jsonRequest, e);
			e.printStackTrace();
		}
		return response;
	}

	public SkillMatrixResponse saveUserType(SkillMatrixRequest request) {
		LOGGER.info("# SettingServiceImpl || saveUserType");
		SkillMatrixResponse response = new SkillMatrixResponse();
		String jsonRequest = new Gson().toJson(request);
		try {
			boolean flag = settingDao.saveUserType(request);
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
			LOGGER.info("# SettingServiceImpl || saveUserType - {}", e.getMessage());
			response = BuildResponse.internalServerError(response);
			sendExceptionEmail(jsonRequest, e);
		}
		return response;
	}

	@Override
	public SkillMatrixResponse updateChecksheetPoint(SkillMatrixRequest request) {
		LOGGER.info("# SettingServiceImpl || updateChecksheetPoint");
		SkillMatrixResponse response = new SkillMatrixResponse();
		String jsonRequest = new Gson().toJson(request);
		try {
			boolean falg = settingDao.updateChecksheetPoint(request);
			if (falg) {

				response = BuildResponse.success(response);

			} else {
				response = BuildResponse.fail(response);
			}
		} catch (EnovationException e) {
			response = BuildResponse.fail100(response);
			response.setReason(e.getReason());
		} catch (Exception e) {
			LOGGER.info("# SettingServiceImpl || updateChecksheetPoint - {}", e.getMessage());
			response = BuildResponse.internalServerError(response);
			sendExceptionEmail(jsonRequest, e);
		}
		return response;
	}

	@Override
	public SkillMatrixResponse deActiveChecksheetPoint(SkillMatrixRequest request) {
		LOGGER.info("# SettingServiceImpl | deActiveChecksheetPoint");
		SkillMatrixResponse response = new SkillMatrixResponse();
		String jsonRequest = new Gson().toJson(request);
		try {
			if ((request.getDayNo() > 0 && request.getCheckSheetId() > 0) || request.getId() > 0) {
				boolean flag = settingDao.deleteChecksheetPoint(request);
				if (flag) {
					response = BuildResponse.success(response);
				} else {
					response = BuildResponse.fail(response);
				}
			} else {
				response = BuildResponse.badRequest(response);
			}
		} catch (EnovationException e) {
			response.setReason(e.getReason());
			response = BuildResponse.fail100(response);
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
			if (CommonUtils.isConstraintViolationException(e)) {
				if (settingDao.deActiveChecksheetPoint(request)) {
					response = BuildResponse.success(response);
				} else {
					response = BuildResponse.fail(response);
				}
			} else {
				response = BuildResponse.internalServerError(response);
				sendExceptionEmail(jsonRequest, e);
			}
			LOGGER.info("# SettingServiceImpl | deActiveChecksheet - {}", e.getMessage());
			e.printStackTrace();
		}
		return response;
	}

	public SkillMatrixResponse deleteUserType(SkillMatrixRequest request) {
		LOGGER.info("# SettingServiceImpl || deleteUserType");
		SkillMatrixResponse response = new SkillMatrixResponse();
		String jsonRequest = new Gson().toJson(request);
		try {
			boolean flag = settingDao.deleteUserType(request);
			if (flag) {
				response = BuildResponse.success(response);
			} else {

				response = BuildResponse.fail(response);
			}
		} catch (EnovationException e) {
			response = BuildResponse.fail100(response);
			response.setReason(e.getReason());
		} catch (Exception e) {
			LOGGER.info("# SettingServiceImpl || deleteUserType - {} ", e.getMessage());
			response = BuildResponse.internalServerError(response);
			sendExceptionEmail(jsonRequest, e);
		}
		return response;

	}

	@Override
	public SkillMatrixResponse getChecksheetPointList(SkillMatrixRequest request) {
		LOGGER.info("# SettingServiceImpl || getChecksheetPointList");
		SkillMatrixResponse response = new SkillMatrixResponse();
		String jsonRequest = new Gson().toJson(request);
		try {
			List<HashMap<String, Object>> list = settingDao.getChecksheetPointList(request);
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
			LOGGER.info("# SettingServiceImpl || getChecksheetPointList -{} ", e.getMessage());
			response = BuildResponse.internalServerError(response);
			sendExceptionEmail(jsonRequest, e);
		}
		return response;
	}

	public SkillMatrixResponse getUserTypeList(SkillMatrixRequest request) {
		LOGGER.info("# SettingServiceImpl || getUserTypeList");
		SkillMatrixResponse response = new SkillMatrixResponse();
		String jsonRequest = new Gson().toJson(request);
		try {
			List<HashMap<String, Object>> list = settingDao.getUserTypeList(request, response);
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
			LOGGER.info("# SettingServiceImpl || getUserTypeList - {}", e.getMessage());
			response = BuildResponse.internalServerError(response);
			sendExceptionEmail(jsonRequest, e);
		}
		return response;

	}

	@Override
	public SkillMatrixResponse getMasterUserType() {
		LOGGER.info("# SettingServiceImpl || getMasterUserType");
		SkillMatrixResponse response = new SkillMatrixResponse();
		String jsonRequest = new Gson().toJson(" ");
		try {
			List<HashMap<String, Object>> list = settingDao.getMasterUserType();
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
			LOGGER.info("# SettingServiceImpl || getMasterUserType - {}", e.getMessage());
			response = BuildResponse.internalServerError(response);
			sendExceptionEmail(jsonRequest, e);
		}
		return response;
	}

	@Override
	public SkillMatrixResponse saveMasterCertificate(SkillMatrixRequest request) {
		LOGGER.info("# SettingServiceImpl || saveMasterCertificate");
		SkillMatrixResponse response = new SkillMatrixResponse();
		String jsonRequest = new Gson().toJson(request);
		try {
			boolean flag = settingDao.saveMasterCertificate(request);
			if (flag) {
				response = BuildResponse.success(response);
			} else {
				response = BuildResponse.fail(response);
			}
		} catch (EnovationException e) {
			response = BuildResponse.fail100(response);
			response.setReason(e.getReason());
		} catch (Exception e) {
			LOGGER.info("# SettingServiceImpl || saveMasterCertificate - {}" + e.getMessage());
			response = BuildResponse.internalServerError(response);
			sendExceptionEmail(jsonRequest, e);
		}
		return response;
	}

	@Override
	public SkillMatrixResponse addChecksheetParameter(SkillMatrixRequest request) {
		LOGGER.info("# SettingServiceImpl | addChecksheetParameter ");
		SkillMatrixResponse response = new SkillMatrixResponse();
		String jsonRequest = new Gson().toJson(request);
		try {
			boolean flag = settingDao.addChecksheetParameter(request);
			if (flag) {
				response = BuildResponse.success(response);
			} else {
				response = BuildResponse.fail(response);
			}
		} catch (EnovationException e) {
			response = BuildResponse.fail100(response);
			response.setReason(e.getReason());
		} catch (Exception e) {
			LOGGER.info("# SettingServiceImpl | addChecksheetParameter - {}", e.getMessage());
			response = BuildResponse.internalServerError(response);
			sendExceptionEmail(jsonRequest, e);
		}
		return response;
	}

	@Override
	public SkillMatrixResponse updateMasterCertificate(SkillMatrixRequest request) {
		LOGGER.info("# SettingServiceImpl || updateMasterCertificate");
		SkillMatrixResponse response = new SkillMatrixResponse();
		String jsonRequest = new Gson().toJson(request);
		try {
			boolean flag = settingDao.updateMasterCertificate(request);
			if (flag) {
				response = BuildResponse.success(response);
			} else {
				response = BuildResponse.fail(response);
			}
		} catch (EnovationException e) {
			response = BuildResponse.fail100(response);
			response.setReason(e.getReason());
		} catch (Exception e) {
			LOGGER.info("# SettingServiceImpl || updateMasterCertificate - {}" + e.getMessage());
			response = BuildResponse.internalServerError(response);
			sendExceptionEmail(jsonRequest, e);
		}
		return response;
	}

	@Override
	public SkillMatrixResponse getMasterCertificateList(SkillMatrixRequest request) {
		LOGGER.info("# SettingServiceImpl || getMasterCertificateList");
		SkillMatrixResponse response = new SkillMatrixResponse();
		String jsonRequest = new Gson().toJson("");
		try {
			List<HashMap<String, Object>> list = settingDao.getMasterCertificateList(request, response);
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
			LOGGER.info("# SettingServiceImpl || getMasterCertificateList - {}" + e.getMessage());
			response = BuildResponse.internalServerError(response);
			sendExceptionEmail(jsonRequest, e);
		}
		return response;
	}

	@Override
	public SkillMatrixResponse getChecksheetParameterList(SkillMatrixRequest request) {
		LOGGER.info("# SettingServiceImpl || getChecksheetParameterList");
		SkillMatrixResponse response = new SkillMatrixResponse();
		String jsonRequest = new Gson().toJson(request);
		try {
			List<HashMap<String, Object>> list = settingDao.getChecksheetParameterList(request);
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
			LOGGER.info("# SettingServiceImpl || getChecksheetParameterList - {}", e.getMessage());
			response = BuildResponse.internalServerError(response);
			sendExceptionEmail(jsonRequest, e);
		}
		return response;
	}

	@Override
	public SkillMatrixResponse deleteMasterCertificate(SkillMatrixRequest request) {
		LOGGER.info("# SettingServiceImpl || deleteMasterCertificate");
		SkillMatrixResponse response = new SkillMatrixResponse();
		String jsonRequest = new Gson().toJson(request);
		try {
			boolean flag = settingDao.deleteMasterCertificate(request);
			if (flag) {
				response = BuildResponse.success(response);
			} else {
				response = BuildResponse.fail(response);
			}
		} catch (EnovationException e) {
			response = BuildResponse.fail100(response);
			response.setReason(e.getReason());
		} catch (Exception e) {
			if (CommonUtils.isConstraintViolationException(e)) {
				response = BuildResponse.success(response);
				response.setReason("Certificate " + SMConstant.ALREADY_IN_USE);
			} else {
				e.printStackTrace();
				LOGGER.info("# SettingServiceImpl || deleteMasterCertificate - {}" + e.getMessage());
				response = BuildResponse.internalServerError(response);
				sendExceptionEmail(jsonRequest, e);
			}

		}
		return response;
	}

	public SkillMatrixResponse updateChecksheetParameter(SkillMatrixRequest request) {
		LOGGER.info("# SettingServiceImpl || updateChecksheetParameter");
		SkillMatrixResponse response = new SkillMatrixResponse();
		String jsonRequest = new Gson().toJson(request);
		try {
			boolean falg = settingDao.updateChecksheetParameter(request);
			if (falg) {
				response = BuildResponse.success(response);

			} else {
				response = BuildResponse.fail(response);
			}
		} catch (EnovationException e) {
			response = BuildResponse.fail100(response);
			response.setReason(e.getReason());
		} catch (Exception e) {
			e.printStackTrace();
			LOGGER.info("# SettingServiceImpl || updateChecksheetParameter - {}" + e.getMessage());
			response = BuildResponse.internalServerError(response);
			sendExceptionEmail(jsonRequest, e);
		}
		return response;
	}

	@Override
	public SkillMatrixResponse deleteChecksheetParameter(SkillMatrixRequest request) {
		LOGGER.info("# SettingServiceImpl | deleteChecksheetParameter");
		SkillMatrixResponse response = new SkillMatrixResponse();
		String jsonRequest = new Gson().toJson(request);
		try {
			boolean flag = settingDao.deleteChecksheetParameter(request);
			if (flag) {
				response = BuildResponse.success(response);
			} else {
				response = BuildResponse.fail(response);
			}
		} catch (EnovationException e) {
			response = BuildResponse.fail100(response);
			response.setReason(e.getReason());
		} catch (Exception e) {
			if (CommonUtils.isConstraintViolationException(e)) {
				if (settingDao.deActiveChecksheetParameter(request)) {
					response = BuildResponse.success(response);
				} else {
					response = BuildResponse.fail(response);
				}
			} else {
				LOGGER.info("# SettingServiceImpl | deleteChecksheetParameter - {}", e.getMessage());
				response = BuildResponse.internalServerError(response);
				sendExceptionEmail(jsonRequest, e);
			}
		}
		return response;
	}

	@Override
	public SkillMatrixResponse getParameterTypeList() {
		LOGGER.info("# SettingServiceImpl || getParameterTypeList");
		SkillMatrixResponse response = new SkillMatrixResponse();
		try {
			List<HashMap<String, Object>> list = settingDao.getParameterTypeList();
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
			LOGGER.info("# SettingServiceImpl || getParameterTypeList - {}", e.getMessage());
			response = BuildResponse.internalServerError(response);
			sendExceptionEmail("", e);
		}
		return response;
	}

	@Override
	public SkillMatrixResponse getChecksheetDetails(long chechSheetId) {
		LOGGER.info("#In SettingServiceImpl |  INSIDE in getChecksheetDetails ");
		SkillMatrixResponse response = new SkillMatrixResponse();
		try {
			if (chechSheetId > 0) {
				HashMap<String, Object> list = settingDao.getChecksheetDetails(chechSheetId);
				if (!CollectionUtils.isEmpty(list)) {
					response = BuildResponse.success(response);
					response.setData(list);
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
			LOGGER.info("#In SettingServiceImpl |  INSIDE in getChecksheetDetails Exception Occured {}",
					e.getMessage());
			String jsonRequest = new Gson().toJson(chechSheetId);
			response = BuildResponse.internalServerError(response);
			sendExceptionEmail(jsonRequest, e);
		}
		return response;
	}

	@Override
	public SkillMatrixResponse publishAssessments(SkillMatrixRequest request) {
		LOGGER.info("# SettingServiceImpl | publishAssessments");
		SkillMatrixResponse response = new SkillMatrixResponse();
		String jsonRequest = new Gson().toJson(request);
		try {
			boolean flag = settingDao.publishAssessments(request);
			if (flag) {
				response = BuildResponse.success(response);
			} else {
				response = BuildResponse.fail(response);
			}
		} catch (EnovationException e) {
			response.setReason(e.getReason());
			response = BuildResponse.fail100(response);
		} catch (Exception e) {
			if (CommonUtils.isConstraintViolationException(e)) {
				if (settingDao.deActiveChecksheetParameter(request)) {
					response = BuildResponse.success(response);
				} else {
					response = BuildResponse.fail(response);
				}
			} else {
				LOGGER.info("# SettingServiceImpl | publishAssessments - {}", e.getMessage());
				response = BuildResponse.internalServerError(response);
				sendExceptionEmail(jsonRequest, e);
			}
		}
		return response;
	}

	@Override
	public SkillMatrixResponse updateUserType(SkillMatrixRequest request) {
		LOGGER.info("# SettingServiceImpl || updateUserType");
		SkillMatrixResponse response = new SkillMatrixResponse();
		String jsonRequest = new Gson().toJson(request);
		try {
			boolean flag = settingDao.updateUserType(request);
			if (flag) {
				response = BuildResponse.success(response);
			} else {
				response = BuildResponse.fail(response);
			}
		} catch (EnovationException e) {
			response = BuildResponse.fail100(response);
			response.setReason(e.getReason());
		} catch (Exception e) {

			LOGGER.info("# SettingServiceImpl || updateUserType - {}", e.getMessage());
			response = BuildResponse.internalServerError(response);
			sendExceptionEmail(jsonRequest, e);
		}
		return response;

	}

	@Override
	public SkillMatrixResponse uploadAssessments(SkillMatrixRequest request) {

		LOGGER.info("#In SettingServiceImpl |  INSIDE in getChecksheetDetails ");
		SkillMatrixResponse response = new SkillMatrixResponse();
		try {
			SkillMatrixRequest obj = settingDao.uploadAssessments(request);
			if (obj != null && !obj.isErrorInSheet()) {
				response = BuildResponse.success(response);
			} else {
				response = BuildResponse.fail(response);
				response.setResponseData(obj);
				response.setStatusCode(EnovationConstants.Code100);
			}
		} catch (EnovationException e) {
			e.printStackTrace();
			response = BuildResponse.fail100(response);
			response.setReason(e.getReason());
		} catch (Exception e) {
			e.printStackTrace();
			LOGGER.info("#In SettingServiceImpl |  INSIDE in getChecksheetDetails Exception Occured {}",
					e.getMessage());
			String jsonRequest = new Gson().toJson(request);
			response = BuildResponse.internalServerError(response);
			sendExceptionEmail(jsonRequest, e);
		}
		return response;
	}

	@Override
	public SkillMatrixResponse deleteAssessmentConfig(SkillMatrixRequest request) {
		LOGGER.info("# SettingServiceImpl || deleteAssessmentConfig");
		SkillMatrixResponse response = new SkillMatrixResponse();
		String jsonRequest = new Gson().toJson(request);
		try {
			boolean flag = settingDao.deleteAssessmentConfig(request);
			if (flag) {
				response = BuildResponse.success(response);
			} else {
				response = BuildResponse.fail(response);
			}
		} catch (EnovationException e) {
			response = BuildResponse.fail100(response);
			response.setReason(e.getReason());
		} catch (Exception e) {

			LOGGER.info("# SettingServiceImpl || deleteAssessmentConfig - {}", e.getMessage());
			response = BuildResponse.internalServerError(response);
			sendExceptionEmail(jsonRequest, e);
		}
		return response;

	}

	@Override

	public SkillMatrixResponse addAssessmentConfig(SkillMatrixRequest request) {
		LOGGER.info("# SettingServiceImpl | addAssessmentConfig ");
		SkillMatrixResponse response = new SkillMatrixResponse();
		String jsonRequest = new Gson().toJson(request);
		try {
			boolean flag = settingDao.addAssessmentConfig(request);
			if (flag) {
				response = BuildResponse.success(response);
			} else {
				response = BuildResponse.fail(response);
			}
		} catch (EnovationException e) {
			response = BuildResponse.fail100(response);
			response.setReason(e.getReason());
		} catch (Exception e) {
			LOGGER.info("# SettingServiceImpl | addAssessmentConfig - {}", e.getMessage());
			response = BuildResponse.internalServerError(response);
			sendExceptionEmail(jsonRequest, e);
		}
		return response;
	}

	@Override
	public SkillMatrixResponse updateAssessmentConfig(SkillMatrixRequest request) {
		LOGGER.info("# SettingServiceImpl | updateAssessmentConfig ");
		SkillMatrixResponse response = new SkillMatrixResponse();
		String jsonRequest = new Gson().toJson(request);
		try {
			boolean flag = settingDao.updateAssessmentConfig(request);
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
			LOGGER.info("# SettingServiceImpl | updateAssessmentConfig - {}", e.getMessage());
			response = BuildResponse.internalServerError(response);
			sendExceptionEmail(jsonRequest, e);
		}
		return response;
	}

	@Override
	public SkillMatrixResponse getAssessmentConfigList(SkillMatrixRequest request) {
		LOGGER.info("# SettingServiceImpl | getAssessmentConfigList ");
		SkillMatrixResponse response = new SkillMatrixResponse();
		String jsonRequest = new Gson().toJson(request);
		try {
			List<HashMap<String, Object>> list = settingDao.getAssessmentConfigList(request, response);
			if (!CollectionUtils.isEmpty(list)) {
				response = BuildResponse.success(response);
				response.setDataList(list);
			} else {
				response = BuildResponse.fail(response);
			}
		} catch (EnovationException e) {
			e.printStackTrace();
			response = BuildResponse.fail100(response);
			response.setReason(e.getReason());
		} catch (Exception e) {
			LOGGER.info("# SettingServiceImpl | getAssessmentConfigList - {}", e.getMessage());
			response = BuildResponse.internalServerError(response);
			sendExceptionEmail(jsonRequest, e);
		}
		return response;
	}

	@Override
	public SkillMatrixResponse getStageLabelList(SkillMatrixRequest request) {
		LOGGER.info("# SettingServiceImpl | getStageLabelList ");
		SkillMatrixResponse response = new SkillMatrixResponse();
		String jsonRequest = new Gson().toJson(request);
		try {

			List<HashMap<String, Object>> list = settingDao.getStageLabelList(request);
			if (!CollectionUtils.isEmpty(list)) {
				response = BuildResponse.success(response);
				response.setDataList(list);
			} else {
				response = BuildResponse.fail(response);
			}
		} catch (EnovationException e) {
			e.printStackTrace();
			response = BuildResponse.fail100(response);
			response.setReason(e.getReason());
		} catch (Exception e) {
			LOGGER.info("# SettingServiceImpl | getStageLabelList - {}", e.getMessage());
			response = BuildResponse.internalServerError(response);
			sendExceptionEmail(jsonRequest, e);
		}
		return response;
	}

	@Override
	public SkillMatrixResponse updateStageLabel(SkillMatrixRequest request) {
		LOGGER.info("#In SettingServiceImpl |  INSIDE in updateStageLabel ");
		SkillMatrixResponse response = new SkillMatrixResponse();
		String jsonRequest = new Gson().toJson(request);
		try {
			if (request.getBranchId() > 0) {
				boolean flag = settingDao.updateStageLabel(request);
				if (flag) {
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
			LOGGER.info("#In SettingServiceImpl |  INSIDE updateStageLabel Exception Occured {}", e.getMessage());
			response = BuildResponse.internalServerError(response);
			sendExceptionEmail(jsonRequest, e);
		}
		return response;
	}

	@Override
	public SkillMatrixResponse getCellList(SkillMatrixRequest request) {
		LOGGER.info("# SettingServiceImpl | getCellList ");
		SkillMatrixResponse response = new SkillMatrixResponse();
		String jsonRequest = new Gson().toJson(request);
		try {
			List<HashMap<String, Object>> list = settingDao.getCellList(request);
			if (!CollectionUtils.isEmpty(list)) {
				response = BuildResponse.success(response);
				response.setDataList(list);
			} else {
				response = BuildResponse.fail(response);
			}
		} catch (EnovationException e) {
			e.printStackTrace();
			response = BuildResponse.fail100(response);
			response.setReason(e.getReason());
		} catch (Exception e) {
			LOGGER.info("# SettingServiceImpl | getCellList - {}", e.getMessage());
			e.printStackTrace();
			response = BuildResponse.internalServerError(response);
			sendExceptionEmail(jsonRequest, e);
		}
		return response;
	}

	@Override
	public SkillMatrixResponse addCategory(SkillMatrixRequest request) {
		LOGGER.info("#In SettingServiceImpl |  INSIDE in addCategory ");
		SkillMatrixResponse response = new SkillMatrixResponse();
		String jsonRequest = new Gson().toJson(request);
		try {
			boolean flag = settingDao.addCategory(request);
			if (flag) {
				response = BuildResponse.success(response);
			} else {
				response = BuildResponse.fail(response);
			}
		} catch (EnovationException e) {
			response = BuildResponse.fail100(response);
			response.setReason(e.getReason());
		} catch (Exception e) {
			LOGGER.info("#In SettingServiceImpl |  INSIDE addCategory Exception Occured {}", e.getMessage());
			response = BuildResponse.internalServerError(response);
			sendExceptionEmail(jsonRequest, e);
		}
		return response;
	}

	@Override
	public SkillMatrixResponse updateCategory(SkillMatrixRequest request) {
		LOGGER.info("#In SettingServiceImpl |  INSIDE in updateCategory ");
		SkillMatrixResponse response = new SkillMatrixResponse();
		String jsonRequest = new Gson().toJson(request);
		try {
			if (request.getId() > 0) {
				boolean flag = settingDao.updateCategory(request);
				if (flag) {
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
			LOGGER.info("#In SettingServiceImpl |  INSIDE updateCategory Exception Occured {}", e.getMessage());
			response = BuildResponse.internalServerError(response);
			sendExceptionEmail(jsonRequest, e);
		}
		return response;
	}

	@Override
	public SkillMatrixResponse deleteCategory(SkillMatrixRequest request) {
		LOGGER.info("#In SettingServiceImpl |  INSIDE in deleteCategory ");
		SkillMatrixResponse response = new SkillMatrixResponse();
		String jsonRequest = new Gson().toJson(request);
		try {
			if (request.getId() > 0) {
				boolean flag = settingDao.deleteCategory(request);
				if (flag) {
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
			LOGGER.info("#In SettingServiceImpl |  INSIDE deleteCategory Exception Occured {}", e.getMessage());
			response = BuildResponse.internalServerError(response);
			sendExceptionEmail(jsonRequest, e);
		}
		return response;
	}

	@Override
	public SkillMatrixResponse getCategoryList(SkillMatrixRequest request) {
		LOGGER.info("#In SettingServiceImpl |  INSIDE in getCategoryList ");
		SkillMatrixResponse response = new SkillMatrixResponse();
		String jsonRequest = new Gson().toJson(request);
		try {

			List<HashMap<String, Object>> list = settingDao.getCategoryList(request);
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
			LOGGER.info("#In SettingServiceImpl |  INSIDE getCategoryList Exception Occured {}", e.getMessage());
			response = BuildResponse.internalServerError(response);
			sendExceptionEmail(jsonRequest, e);
		}
		return response;
	}

	@Override
	public SkillMatrixResponse saveCopyChecksheet(SkillMatrixRequest request) {
		LOGGER.info("#In SettingServiceImpl |  INSIDE in saveCopyChecksheet ");
		SkillMatrixResponse response = new SkillMatrixResponse();
		String jsonRequest = new Gson().toJson(request);
		try {

			SkillMatrixRequest obj = settingDao.saveCopyChecksheet(request);
			if (obj != null) {
				response = BuildResponse.success(response);
				response.setResponseData(obj);
			} else {
				response = BuildResponse.fail(response);
			}
		} catch (EnovationException e) {
			response = BuildResponse.fail100(response);
			response.setReason(e.getReason());
		} catch (Exception e) {
			LOGGER.info("#In SettingServiceImpl |  INSIDE saveCopyChecksheet Exception Occured {}", e.getMessage());
			response = BuildResponse.internalServerError(response);
			sendExceptionEmail(jsonRequest, e);
		}
		return response;
	}

	@Override
	public SkillMatrixResponse saveDocName(SkillMatrixRequest request) {
		LOGGER.info("#In SettingServiceImpl |  INSIDE in saveDocName ");
		SkillMatrixResponse response = new SkillMatrixResponse();
		try {
			if (request != null && request.getBranchId() > 0) {
				boolean flag = settingDao.saveDocName(request);
				if (flag) {
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
			String jsonRequest = new Gson().toJson(request);
			LOGGER.info("#In SettingServiceImpl |  INSIDE saveDocName Exception Occured {}", e.getMessage());
			response = BuildResponse.internalServerError(response);
			sendExceptionEmail(jsonRequest, e);
		}
		return response;
	}

	@Override
	public SkillMatrixResponse updateDocName(SkillMatrixRequest request) {
		LOGGER.info("#In SettingServiceImpl |  INSIDE in updateDocName ");
		SkillMatrixResponse response = new SkillMatrixResponse();
		try {
			if (request != null && request.getBranchId() > 0) {
				boolean flag = settingDao.updateDocName(request);
				if (flag) {
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
			String jsonRequest = new Gson().toJson(request);
			LOGGER.info("#In SettingServiceImpl |  INSIDE updateDocName Exception Occured {}", e.getMessage());
			response = BuildResponse.internalServerError(response);
			sendExceptionEmail(jsonRequest, e);
		}
		return response;
	}

	@Override
	public SkillMatrixResponse deleteDocName(SkillMatrixRequest request) {
		LOGGER.info("#In SettingServiceImpl |  INSIDE in deleteDocName ");
		SkillMatrixResponse response = new SkillMatrixResponse();
		try {
			if (request != null && request.getBranchId() > 0) {
				boolean flag = settingDao.deleteDocName(request);
				if (flag) {
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
			String jsonRequest = new Gson().toJson(request);
			LOGGER.info("#In SettingServiceImpl |  INSIDE deleteDocName Exception Occured {}", e.getMessage());
			response = BuildResponse.internalServerError(response);
			sendExceptionEmail(jsonRequest, e);
		}
		return response;
	}

	@Override
	public SkillMatrixResponse getDocName(SkillMatrixRequest request) {
		LOGGER.info("# SettingServiceImpl || getDocName");
		SkillMatrixResponse response = new SkillMatrixResponse();
		String jsonRequest = new Gson().toJson("");
		try {
			if (request != null && request.getBranchId() > 0) {
				HashMap<String, Object> data = settingDao.getDocName(request);
				if (data != null) {
					response = BuildResponse.success(response);
					response.setData(data);
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
			LOGGER.info("# SettingServiceImpl || getDocName - {}" + e.getMessage());
			response = BuildResponse.internalServerError(response);
			sendExceptionEmail(jsonRequest, e);
		}
		return response;
	}

	@Override
	public SkillMatrixResponse getAllWorkstationMapping(SkillMatrixRequest request) {
		LOGGER.info("# SettingServiceImpl | getAllWorkstationMapping");
		SkillMatrixResponse response = new SkillMatrixResponse();
		try {
			List<HashMap<String, Object>> mappingList = settingDao.getAllWorkstationMapping(request,response);
			if (CollectionUtils.isEmpty(mappingList)) {
				response = BuildResponse.fail(response);
				return response;
			}

			// Parse the JSON array of child workstations for each mapping
			for (HashMap<String, Object> mapping : mappingList) {
				Object childWorkstationsObj = mapping.get("childWorkstations");
				if (childWorkstationsObj != null) {
					try {
						String childWorkstationsJson;
						if (childWorkstationsObj instanceof char[]) {
							childWorkstationsJson = new String((char[]) childWorkstationsObj);
						} else {
							childWorkstationsJson = childWorkstationsObj.toString();
						}
						ObjectMapper mapper = new ObjectMapper();
						List<Map<String, Object>> childWorkstations = mapper.readValue(childWorkstationsJson, List.class);
						mapping.put("childWorkstations", childWorkstations);
					} catch (Exception e) {
						LOGGER.error("Error parsing child workstations JSON: {}", e.getMessage());
						mapping.put("childWorkstations", new ArrayList<>());
					}
				} else {
					mapping.put("childWorkstations", new ArrayList<>());
				}
			}

			response = BuildResponse.success(response);
			response.setDataList(mappingList);
		} catch (Exception e) {
			LOGGER.error("Error in getAllWorkstationMapping: {}", e.getMessage());
			response = BuildResponse.internalServerError(response);
			sendExceptionEmail("getAllWorkstationMapping", e);
		}
		return response;
	}

}
