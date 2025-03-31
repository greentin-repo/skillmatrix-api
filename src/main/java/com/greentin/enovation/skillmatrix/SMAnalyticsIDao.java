package com.greentin.enovation.skillmatrix;

import java.util.HashMap;
import java.util.List;

import com.greentin.enovation.dto.SkillMatrixRequest;
import com.greentin.enovation.response.SkillMatrixResponse;

/**
 * @author Sonali L Aug 7, 2023
 * @desc
 */
public interface SMAnalyticsIDao {

	HashMap<String, Object> getDeptLvlAnalytics(SkillMatrixRequest request);

	HashMap<String, Object> getOrgLvlAnalytics(SkillMatrixRequest request);

	HashMap<String, Object> getBranchLvlAnalytics(SkillMatrixRequest request);

	List<HashMap<String, Object>> getAvgTimeToCompleteReport(SkillMatrixRequest request);

	List<HashMap<String, Object>> getSkillGapCellwise(SkillMatrixRequest request);

	List<HashMap<String, Object>> getOjtPlanAndActualReport(SkillMatrixRequest request);

	List<HashMap<String, Object>> getCellWiseMultiskillingReport(SkillMatrixRequest request);

	List<HashMap<String, Object>> getPlantWiseMultiskillingReport(SkillMatrixRequest request);

	List<HashMap<String, Object>> getAssesmentReport(SkillMatrixRequest request);

	List<HashMap<String, Object>> getEmployeeWisePlanReport(SkillMatrixRequest request);

	List<HashMap<String, Object>> getSMCellWiseMonthWiseReport(SkillMatrixRequest request);

	List<HashMap<String, Object>> ojtPlanBranchWiseReport(SkillMatrixRequest request);

	List<HashMap<String, Object>> ojtPlanDeptWiseReport(SkillMatrixRequest request);

}
