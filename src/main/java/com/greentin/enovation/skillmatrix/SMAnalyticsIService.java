package com.greentin.enovation.skillmatrix;

import com.greentin.enovation.dto.SkillMatrixRequest;
import com.greentin.enovation.response.SkillMatrixResponse;

/**
 * @author Sonali L Aug 7, 2023
 * @desc
 */
public interface SMAnalyticsIService {

	SkillMatrixResponse getDeptLvlAnalytics(SkillMatrixRequest request);

	SkillMatrixResponse getOrgLvlAnalytics(SkillMatrixRequest request);

	SkillMatrixResponse getBranchLvlAnalytics(SkillMatrixRequest request);

	SkillMatrixResponse getSkillMatrixReport(SkillMatrixRequest request);

}
