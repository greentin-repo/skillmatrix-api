package com.greentin.enovation.skillmatrix;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.greentin.enovation.dto.SkillMatrixRequest;
import com.greentin.enovation.response.SkillMatrixResponse;

/**
 * @author Sonali L Aug 7, 2023
 * @desc SkillMatrix all APIs dashboard and report level
 */
@RestController
@CrossOrigin
@RequestMapping(value = "/apis/sm")
public class SMAnalyticsController {
	private static final Logger LOGGER = LoggerFactory.getLogger(SMAnalyticsController.class);

	@Autowired
	private SMAnalyticsIService analyticsService;

	/**
	 * @author Rajdeep MD 10-Aug-2023 , 02:00 PM
	 * @param request
	 * @return SkillMatrixResponse
	 * @desc get Org Level graph of Skilling
	 */
	@PostMapping(value = "/getOrgLvlAnalytics")
	public SkillMatrixResponse getOrgLvlAnalytics(@RequestBody SkillMatrixRequest request) {
		LOGGER.info("#In SMAnalyticsController |  INSIDE in getOrgLvlAnalytics ");
		return analyticsService.getOrgLvlAnalytics(request);
	}

	/**
	 * @author Rajdeep MD 10-Aug-2023 , 02:00 PM
	 * @param request
	 * @return SkillMatrixResponse
	 * @desc get Branch Level graph of Skilling
	 */
	@PostMapping(value = "/getBranchLvlAnalytics")
	public SkillMatrixResponse getBranchLvlAnalytics(@RequestBody SkillMatrixRequest request) {
		LOGGER.info("#In SMAnalyticsController |  INSIDE in getBranchLvlAnalytics ");
		return analyticsService.getBranchLvlAnalytics(request);
	}

	/**
	 * @author Sonali L 05-sep-2023
	 * @param request
	 * @return SkillMatrixResponse
	 * @desc
	 */
	@PostMapping(value = "/getDeptLvlAnalytics")
	public SkillMatrixResponse getDeptLvlAnalytics(@RequestBody SkillMatrixRequest request) {
		LOGGER.info("#In SMAnalyticsController |  INSIDE in getDeptLvlAnalytics ");
		return analyticsService.getDeptLvlAnalytics(request);
	}

	/**
	 * @author Anant K 20-sep-2023
	 * @param request
	 * @return SkillMatrixResponse
	 * @desc get Skill Matrix Report
	 */
	@PostMapping(value = "/getSkillMatrixReport")
	public SkillMatrixResponse getSkillMatrixReport(@RequestBody SkillMatrixRequest request) {
		LOGGER.info("#In SMAnalyticsController |  INSIDE in getSkillMatrixReport ");
		return analyticsService.getSkillMatrixReport(request);
	}
}
