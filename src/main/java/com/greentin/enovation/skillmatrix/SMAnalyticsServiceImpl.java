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
import com.greentin.enovation.dto.SkillMatrixRequest;
import com.greentin.enovation.response.SkillMatrixResponse;
import com.greentin.enovation.utils.BuildResponse;
import com.greentin.enovation.utils.CommonUtils;
import com.greentin.enovation.utils.EnovationConstants;
import com.greentin.enovation.utils.EnovationException;

/**
 * @author Sonali L Aug 7, 2023
 * @desc Service layer for analytics dashboard and report
 */

@Service
public class SMAnalyticsServiceImpl implements SMAnalyticsIService {
	private static final Logger LOGGER = LoggerFactory.getLogger(SMAnalyticsServiceImpl.class);

	@Autowired
	private SMAnalyticsIDao analyticsDao;

	@Autowired
	CommonUtils commonUtils;

	@Override
	public SkillMatrixResponse getOrgLvlAnalytics(SkillMatrixRequest request) {
		LOGGER.info("#In SMAnalyticsServiceImpl |  INSIDE in getGraphDetails ");
		SkillMatrixResponse response = new SkillMatrixResponse();
		try {
			if (request.getOrgId() > 0) {
				HashMap<String, Object> list = analyticsDao.getOrgLvlAnalytics(request);
				if (!CollectionUtils.isEmpty(list)) {
					BuildResponse.success(response);
					response.setData(list);
				} else {
					BuildResponse.fail(response);
					response.setReason(EnovationConstants.DATANOTFOUND);
				}
			} else {
				BuildResponse.badRequest(response);
			}
		} catch (Exception e) {
			e.printStackTrace();
			BuildResponse.internalServerError(response);
			LOGGER.info("In SMAnalyticsServiceImpl  | getOrgLvlAnalytics  Exception Occured  - {}", e.getMessage());
			String jsonRequest = new Gson().toJson(request);
			sendExceptionEmail(jsonRequest, e);
		}
		return response;
	}

	@Override
	public SkillMatrixResponse getBranchLvlAnalytics(SkillMatrixRequest request) {
		LOGGER.info("#In SMAnalyticsServiceImpl |  INSIDE in getGraphDetails ");
		SkillMatrixResponse response = new SkillMatrixResponse();
		try {
			if (request.getOrgId() > 0) {
				HashMap<String, Object> list = analyticsDao.getBranchLvlAnalytics(request);
				if (!CollectionUtils.isEmpty(list)) {
					BuildResponse.success(response);
					response.setData(list);
				} else {
					BuildResponse.fail(response);
					response.setReason(EnovationConstants.DATANOTFOUND);
				}
			} else {
				BuildResponse.badRequest(response);
			}
		} catch (Exception e) {
			e.printStackTrace();
			BuildResponse.internalServerError(response);
			LOGGER.info("In SMAnalyticsServiceImpl  | getBranchLvlAnalytics  Exception Occured - {} ", e.getMessage());
			String jsonRequest = new Gson().toJson(request);
			sendExceptionEmail(jsonRequest, e);
		}
		return response;
	}

	public SkillMatrixResponse getDeptLvlAnalytics(SkillMatrixRequest request) {
		LOGGER.info("# SMAnalyticsServiceImpl || getDeptLvlAnalytics");
		SkillMatrixResponse response = new SkillMatrixResponse();
		try {
			HashMap<String, Object> list = analyticsDao.getDeptLvlAnalytics(request);
			if (!CollectionUtils.isEmpty(list)) {
				response = BuildResponse.success(response);
				response.setData(list);
			} else {
				response = BuildResponse.fail(response);
				response.setReason(EnovationConstants.DATANOTFOUND);
			}
		} catch (EnovationException e) {
			response = BuildResponse.fail100(response);
			response.setReason(e.getReason());
		} catch (Exception e) {
			e.printStackTrace();
			LOGGER.info("# SMAnalyticsServiceImpl || getDeptLvlAnalytics - {}", e.getMessage());
			response = BuildResponse.internalServerError(response);
			String jsonRequest = new Gson().toJson(request);
			sendExceptionEmail(jsonRequest, e);
		}
		return response;
	}

	@Override
	public SkillMatrixResponse getSkillMatrixReport(SkillMatrixRequest request) {
		LOGGER.info("# SMAnalyticsServiceImpl || getSkillMatrixReport");
		SkillMatrixResponse response = new SkillMatrixResponse();
		boolean isDataFound = false;
		try {
			if (request.getReportCaption() != null) {
				if (request.getReportCaption().equals(SMConstant.TASK_COMPLETION_STATUS)) {
					List<HashMap<String, Object>> list = analyticsDao.getAvgTimeToCompleteReport(request);
					if (!CollectionUtils.isEmpty(list)) {
						isDataFound = true;
						response.setDataList(list);
					}
				} else if (request.getReportCaption().equals(SMConstant.SKILL_GAP_ANALYSIS)) { 
					List<HashMap<String, Object>> list = analyticsDao.getSkillGapCellwise(request);
					if (!CollectionUtils.isEmpty(list)) {
						isDataFound = true;
						response.setDataList(list);
					}
				} else if (request.getReportCaption().equals(SMConstant.COMPLETION_DISCREPANCY_REPORT)) {
					List<HashMap<String, Object>> list = analyticsDao.getOjtPlanAndActualReport(request);
					if (!CollectionUtils.isEmpty(list)) {
						isDataFound = true;
						response.setDataList(list);
					}
				} else if (request.getReportCaption().equals(SMConstant.MULTISKILLING_STATUS_AT_CELL_LEVEL)) {
					List<HashMap<String, Object>> list = analyticsDao.getCellWiseMultiskillingReport(request);
					if (!CollectionUtils.isEmpty(list)) {
						isDataFound = true;
						response.setDataList(list);
					}
				} else if (request.getReportCaption().equals(SMConstant.MULTISKILLING_STATUS_AT_PLANT_LEVEL)) {
					List<HashMap<String, Object>> list = analyticsDao.getPlantWiseMultiskillingReport(request);
					if (!CollectionUtils.isEmpty(list)) {
						isDataFound = true;
						response.setDataList(list);
					}
				} else if (request.getReportCaption().equals(SMConstant.ASSESSMENT_SUCCESS_RATE)) {
					List<HashMap<String, Object>> list = analyticsDao.getAssesmentReport(request);
					if (!CollectionUtils.isEmpty(list)) {
						isDataFound = true;
						response.setDataList(list);
					}
				} else if (request.getReportCaption().equals(SMConstant.EMPLOYEE_PERFORMANCE_ANALYSIS)) {
					List<HashMap<String, Object>> list = analyticsDao.getEmployeeWisePlanReport(request);
					if (!CollectionUtils.isEmpty(list)) {
						isDataFound = true;
						response.setDataList(list);
					}
				} else if (request.getReportCaption().equals(SMConstant.MONTHLY_SKILL_MATRIX_REPORT)) {
					List<HashMap<String, Object>> list = analyticsDao.getSMCellWiseMonthWiseReport(request);
					if (!CollectionUtils.isEmpty(list)) {
						isDataFound = true;
						response.setDataList(list);
					}
				} else if (request.getReportCaption().equals(SMConstant.PLANT_ADHERENCE_STATUS)) {
					List<HashMap<String, Object>> list = analyticsDao.ojtPlanBranchWiseReport(request);
					if (!CollectionUtils.isEmpty(list)) {
						isDataFound = true;
						response.setDataList(list);
					}
				} else if (request.getReportCaption().equals(SMConstant.CELL_ADHERENCE_STATUS)) {
					List<HashMap<String, Object>> list = analyticsDao.ojtPlanDeptWiseReport(request);
					if (!CollectionUtils.isEmpty(list)) {
						isDataFound = true;
						response.setDataList(list);
					}
				}
				if (isDataFound) {
					response = BuildResponse.success(response);
				} else {
					response = BuildResponse.fail(response);
					response.setReason(EnovationConstants.DATANOTFOUND);
				}
			} else {
				BuildResponse.badRequest(response);
			}
		} catch (EnovationException e) {
			response = BuildResponse.fail100(response);
			response.setReason(e.getReason());
		} catch (Exception e) {
			e.printStackTrace();
			LOGGER.info("# SMAnalyticsServiceImpl || getSkillMatrixReport - {}", e.getMessage());
			response = BuildResponse.internalServerError(response);
			String jsonRequest = new Gson().toJson(request);
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

}


