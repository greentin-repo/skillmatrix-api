package com.greentin.enovation.utils;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Ananta Khole 22 August 2023 04:33:31 PM
 * @type_name ColumnAscDescConstants
 */
public class ColumnAscDescConstants {
	private static final Logger LOGGER = LoggerFactory.getLogger(ColumnAscDescConstants.class);

	// My Skilling : --> Actions : Column Name For ASC/DESC Order
	public static Map<String, String> pendingColHeder;
	static {
		LOGGER.info("ColumnAscDescConstants || pendingColHeder ");

		pendingColHeder = new HashMap<>();
		pendingColHeder.put("BranchName", "mb.name ");
		pendingColHeder.put("DeptName", "md.name ");
		pendingColHeder.put("ActivityDate", "ojtsa.created_date ");
		pendingColHeder.put("Workstation", "smw.workstation ");
		pendingColHeder.put("Activity", "stage.stage_name ");
		pendingColHeder.put("status", "ojtsa.status ");
		pendingColHeder.put("LevelName", " slvl.level_name ");
		pendingColHeder.put("EmpName", "empName "); 
		pendingColHeder.put("Line", " l.name as lineName  "); 
		pendingColHeder.put("CmpyEmpId", " ed.cmpy_emp_id  "); 
	}

	// My Skilling : --> AssessmentsDetails : Column Name For ASC/DESC Order
	public static Map<String, String> AssessmentsColName;
	static {
		LOGGER.info("ColumnAscDescConstants || AssessmentsDetails ");

		AssessmentsColName = new HashMap<>();
		AssessmentsColName.put("BranchName", "b.name ");
		AssessmentsColName.put("DeptName", "d.dept_name ");
		AssessmentsColName.put("Workstation", "w.workstation ");
		AssessmentsColName.put("LevelName", "l.level_name ");
		AssessmentsColName.put("AssessmentDate", "sk.assessment_due_date ");
		AssessmentsColName.put("Score", "a.actual_marks ");
		AssessmentsColName.put("Status", "a.assessment_status ");
		AssessmentsColName.put("AssessmentTitle", "sa.title ");
		AssessmentsColName.put("EmpName", "empName ");
		AssessmentsColName.put("AssessmentType", "sa.assessment_type");

	}

	// My Skilling : --> SkillMatrixDetails : Column Name For ASC/DESC Order
	public static Map<String, String> SkillMatrixColName;
	static {
		LOGGER.info("ColumnAscDescConstants || SkillMatrixDetails ");

		SkillMatrixColName = new HashMap<>();
		SkillMatrixColName.put("BranchName", "b.name ");
		SkillMatrixColName.put("DeptName", "d.dept_name ");
		SkillMatrixColName.put("Workstation", "w.workstation ");
		SkillMatrixColName.put("Status", "r.status ");
		SkillMatrixColName.put("LevelName", "l.level_name ");
		SkillMatrixColName.put("EmpName", "empName ");
	}
	// My Skilling : --> CertificateDetails : Column Name For ASC/DESC Order
	public static Map<String, String> CertificateColName;
	static {
		LOGGER.info("ColumnAscDescConstants || CertificateDetails ");

		CertificateColName = new HashMap<>();
		CertificateColName.put("BranchName", " b.name ");
		CertificateColName.put("DeptName", " d.dept_name ");
		CertificateColName.put("Workstation", " w.workstation ");
		CertificateColName.put("Status", "r.status ");
		CertificateColName.put("LevelName", " l.level_name ");
		CertificateColName.put("EmpName", " empName ");
	}

	public static Map<String, String> sMActionListColName;
	static {
		LOGGER.info("ColumnAscDescConstants || pendingColHeder ");

		sMActionListColName = new HashMap<>();
		sMActionListColName.put("BranchName", "mb.name ");
		sMActionListColName.put("DeptName", "md.dept_name ");
		sMActionListColName.put("ActivityDate", "ojtsa.created_date ");
		sMActionListColName.put("Workstation", "smw.workstation ");
		sMActionListColName.put("Activity", "stage.stage_name ");
		sMActionListColName.put("status", "ojtsa.status ");
		sMActionListColName.put("LevelName", "slvl.level ");
		sMActionListColName.put("EmpName", "empName ");
	}
	public static Map<String, String> WorkstationColName;
	static {
		LOGGER.info("ColumnAscDescConstants || WorkstationDetails ");

		WorkstationColName = new HashMap<>();
		WorkstationColName.put("BranchName", " b.name ");
		WorkstationColName.put("Line", " l.name ");
		WorkstationColName.put("DeptName", " d.dept_name ");
		WorkstationColName.put("Workstation", " sw.workstation ");
		WorkstationColName.put("LevelName", " sl.level_name ");
		WorkstationColName.put("MachineIndex", " sw.machine_index ");
		WorkstationColName.put("ReqSkillLevelId", " sw.req_skill_level_id  ");
		WorkstationColName.put("RequiredWorkforce", " sw.required_workforce ");
	}

	public static Map<String, String> StakeholderColName;
	static {
		LOGGER.info("ColumnAscDescConstants || StakeholderDetails ");

		StakeholderColName = new HashMap<>();
		StakeholderColName.put("BranchName", " b.name ");
		StakeholderColName.put("Line", " l.name ");
		StakeholderColName.put("DeptName", " d.dept_name ");
		StakeholderColName.put("EmpId", " t.emp_id ");
		StakeholderColName.put("EmpName", " empName ");
		StakeholderColName.put("UserType", " sm.user_type ");

	}

	public static Map<String, String> smCheckSheetColName;
	static {
		LOGGER.info("ColumnAscDescConstants || pendingColHeder ");

		smCheckSheetColName = new HashMap<>();
		smCheckSheetColName.put("BranchName", " b.name ");
		smCheckSheetColName.put("Level", " sc.skill_level_id ");
		smCheckSheetColName.put("Title", " sc.title ");
		smCheckSheetColName.put("NoOfDay", " sc.no_of_days ");
	}

	public static Map<String, String> smAssessmentColName;
	static {
		LOGGER.info("smAssessmentColName || pendingColHeder ");

		smAssessmentColName = new HashMap<>();
		smAssessmentColName.put("BranchName", " b.name ");
		smAssessmentColName.put("Level", " sa.skill_level_id ");
		smAssessmentColName.put("Title", "  sa.title ");
		smAssessmentColName.put("Time", " sa.time ");
		smAssessmentColName.put("PassingMarks", " sa.passing_marks ");
		smAssessmentColName.put("Status", " sa.status ");
		smAssessmentColName.put("CreatedDate", " sa.created_date ");
		smAssessmentColName.put("TotalMarks", " sa.total_marks ");
		smAssessmentColName.put("Workstation", " sw.workstation ");
		smAssessmentColName.put("AssessmentType", " sa.assessment_type ");
		
	}

	public static Map<String, String> smOJTPlanColName;
	static {
		LOGGER.info("smOJTPlanColName || pendingColHeder ");

		smOJTPlanColName = new HashMap<>();
		smOJTPlanColName.put("BranchName", " b.name ");
		smOJTPlanColName.put("Title", "  sa.title ");
		smOJTPlanColName.put("Status", "  ojt.status ");
		smOJTPlanColName.put("CreatedDate", "  ojt.created_date ");
		smOJTPlanColName.put("MonthValue", "  ojt.month_value ");
		smOJTPlanColName.put("StartDate", "  ojt.start_date ");
		smOJTPlanColName.put("YearValue", "  ojt.year_value ");
		smOJTPlanColName.put("DeptName", "  md.dept_name ");
	}

	public static Map<String, String> smOJTEmpListColName;
	static {
		LOGGER.info("smOJTEmpListColName || pendingColHeder ");

		smOJTEmpListColName = new HashMap<>();
		smOJTEmpListColName.put("Workstaion", " sw.workstation ");
		smOJTEmpListColName.put("Level", "  sm.skill_level_id ");
		smOJTPlanColName.put("CmpyEmpId", "  e.cmpy_emp_id ");
	}

	public static Map<String, String> smModelColName;
	static {
		LOGGER.info("ColumnAscDescConstants || getModelList || smModelColName ");

		smModelColName = new HashMap<>();
		smModelColName.put("ModelName", "smm.model_name");
		smModelColName.put("BranchId", "smm.branch_id ");
		smModelColName.put("BranchName", "mb.name ");
		smModelColName.put("CreatedDate", "smm.created_date ");

	}

	public static Map<String, String> smGapReasonColName;
	static {
		LOGGER.info("ColumnAscDescConstants || getGapReasonList || smGapReasonColName ");

		smGapReasonColName = new HashMap<>();
		smGapReasonColName.put("Reason", "gr.reason");
		smGapReasonColName.put("BranchId", "gr.branch_id ");
		smGapReasonColName.put("BranchName", "mb.name ");
		smGapReasonColName.put("CreatedDate", "gr.created_date ");

	}

	public static Map<String, String> smShiftColName;
	static {
		LOGGER.info("ColumnAscDescConstants || getShiftList || smShiftColName ");

		smShiftColName = new HashMap<>();
		smShiftColName.put("ShiftName", "sms.shift_name");
		smShiftColName.put("BranchId", "sms.branch_id ");
		smShiftColName.put("BranchName", "mb.name ");
		smShiftColName.put("CreatedDate", "sms.created_date ");
		smShiftColName.put("IsActive", "sms.is_active ");
	}

	public static Map<String, String> smCertificateColName;
	static {
		LOGGER.info("ColumnAscDescConstants || smCertificateColName ");

		smCertificateColName = new HashMap<>();
		smCertificateColName.put("CertificateName", "smmcer.certificate_name ");
		smCertificateColName.put("Status", "smmcer.status ");
		smCertificateColName.put("SkillLevelName", "sl.level_name ");

	}

	public static Map<String, String> smDeployementColName;
	static {
		LOGGER.info("ColumnAscDescConstants || getEmpDetails || smDeployementColName ");

		smDeployementColName = new HashMap<>();
		smDeployementColName.put("FromDate", "wfd.from_date ");
		smDeployementColName.put("ToDate", "wfd.to_date ");
		smDeployementColName.put("EmpName", "empName ");
		smDeployementColName.put("WorkstationName", "ws.workstation ");
		smDeployementColName.put("LevelName", "lvl.level_name ");
	}

	public static Map<String, String> AssesmentReportColHeader;

	static {
		LOGGER.info("ColumnAscDescConstants || getAssesmentReport || AssesmentReportColHeader ");

		AssesmentReportColHeader = new HashMap<>();
		AssesmentReportColHeader.put("TotalCount", "totalCount");
		AssesmentReportColHeader.put("LevelName", "smsl.level_name ");
		AssesmentReportColHeader.put("LevelId", "sor.current_skill_level_id ");
		AssesmentReportColHeader.put("PassPercentage", "passPercentage ");
		AssesmentReportColHeader.put("PassCount", "passCount ");
		AssesmentReportColHeader.put("FailPercentage", "failPercentage ");
		AssesmentReportColHeader.put("FailCount", "failCount ");
		AssesmentReportColHeader.put("DeptId", "sor.dept_id ");
		AssesmentReportColHeader.put("DeptName", " md.dept_name ");
		AssesmentReportColHeader.put("LineId", " sor.line_id ");
		AssesmentReportColHeader.put("LineName", " ln.name ");
		AssesmentReportColHeader.put("BranchName", " mb.name ");

	}

	public static Map<String, String> AvgTimeReportColName;
	static {
		LOGGER.info("ColumnAscDescConstants || AvgTimeReportColName ");

		AvgTimeReportColName = new HashMap<>();
		AvgTimeReportColName.put("BranchName", " mb.name  ");
		AvgTimeReportColName.put("DeptName", " d.dept_name ");
		AvgTimeReportColName.put("LevelName", " sl.level_name ");
		AvgTimeReportColName.put("AvgTime", " avgTime ");
		AvgTimeReportColName.put("LineId", " ojtr.line_id ");
		AvgTimeReportColName.put("LineName", " ln.name ");

	}

	public static Map<String, String> skillGapReportColName;
	static {
		LOGGER.info("ColumnAscDescConstants || skillGapReportColName ");

		skillGapReportColName = new HashMap<>();
		skillGapReportColName.put("BranchName", " branch  ");
		skillGapReportColName.put("DeptName", " deptName ");
		skillGapReportColName.put("LevelName", " sl.level_name ");
		skillGapReportColName.put("GapAvg", " avgCount ");
		skillGapReportColName.put("GapCount", " totalGap ");
		skillGapReportColName.put("LineId", " ln.id ");
		skillGapReportColName.put("LineName", " ln.name ");
		skillGapReportColName.put("LevelId", " sw.req_skill_level_id ");
		skillGapReportColName.put("ActualCount", " actualCount ");


	}

	public static Map<String, String> PlanAndActualReportColName;
	static {
		LOGGER.info("ColumnAscDescConstants || PlanAndActualReportColName ");

		PlanAndActualReportColName = new HashMap<>();
		PlanAndActualReportColName.put("TotalPlan", " totalOjtCount  ");
		PlanAndActualReportColName.put("ActualCompletion", " OjtCompleteCount ");
		PlanAndActualReportColName.put("CompletionPercentage", " OjtCompletePercentage ");
		PlanAndActualReportColName.put("LevelName", " smsl.level_name ");
		PlanAndActualReportColName.put("DeptName", " md.dept_name");
		PlanAndActualReportColName.put("BranchId", " sor.branch_id");
		PlanAndActualReportColName.put("BranchName", " mb.name");
		PlanAndActualReportColName.put("LineId", " sor.line_id");
		PlanAndActualReportColName.put("LineName", " ln.name");

	}

	public static Map<String, String> CellMultiReportColName;
	static {
		LOGGER.info("ColumnAscDescConstants || CellMultiReportColName ");

		CellMultiReportColName = new HashMap<>();
		CellMultiReportColName.put("EmpMultiskillingCount", " employeeCount  ");
		CellMultiReportColName.put("TotalEmp", "totalEmployees");
		CellMultiReportColName.put("MultiskillingPercentage", " employeeCountPerc ");
		CellMultiReportColName.put("DeptName", " lineCounts.deptName ");
		CellMultiReportColName.put("LineName", " lineName ");
		CellMultiReportColName.put("LineId", " lineId ");
		CellMultiReportColName.put("BranchName", " lineCounts.branchName ");

	}

	public static Map<String, String> PlantMultiReportColName;
	static {
		LOGGER.info("ColumnAscDescConstants || PlantMultiReportColName ");

		PlantMultiReportColName = new HashMap<>();
		PlantMultiReportColName.put("EmpMultiskillingCount", " employeeCount  ");
		PlantMultiReportColName.put("TotalEmp", " totalEmployees ");
		PlantMultiReportColName.put("MultiskillingPercentage", " employeeCountPerc ");
		PlantMultiReportColName.put("BranchName", " branchName ");

	}

	public static Map<String, String> EmployeePlanColHeader;

	static {
		LOGGER.info("ColumnAscDescConstants || getEmployeeWisePlanReport || EmployeePlanColHeader ");

		EmployeePlanColHeader = new HashMap<>();
		EmployeePlanColHeader.put("DeptName", "data.deptName");
		EmployeePlanColHeader.put("LineName", "data.name");
		EmployeePlanColHeader.put("LineId", "data.lineId");
		EmployeePlanColHeader.put("Plan", "plan ");
		EmployeePlanColHeader.put("Actual", "actual ");
		EmployeePlanColHeader.put("PassCount", "passCount ");
		EmployeePlanColHeader.put("FailCount", "failCount ");
		EmployeePlanColHeader.put("LevelId", "data.levelId ");
		EmployeePlanColHeader.put("LevelName", "data.levelName");
		EmployeePlanColHeader.put("BranchName", "data.branchName");



	}

	public static Map<String, String> CellWiseReportColHeader;

	static {
		LOGGER.info("ColumnAscDescConstants || getSMCellWiseMonthWiseReport || CellWiseReportColHeader ");

		CellWiseReportColHeader = new HashMap<>();
		CellWiseReportColHeader.put("DeptName", "md.dept_name");
		CellWiseReportColHeader.put("LevelName", "slvl.level_name ");
		CellWiseReportColHeader.put("WorkstationName", "ws.workstation ");
		CellWiseReportColHeader.put("ActualCount", "actualCount ");
		CellWiseReportColHeader.put("MonthYear", "monthYear ");
		CellWiseReportColHeader.put("PlanCount", "planCount ");
		CellWiseReportColHeader.put("Percentage", "percentage ");
		CellWiseReportColHeader.put("LineName", "ln.name");
		CellWiseReportColHeader.put("LineId", "r.line_id");
		CellWiseReportColHeader.put("LevelId", "r.desired_skill_level_id");
		CellWiseReportColHeader.put("BranchName", "b.name");



	}

	public static Map<String, String> OJTPlanBranchColHeader;

	static {
		LOGGER.info("ColumnAscDescConstants || ojtPlanBranchWiseReport || OJTPlanBranchColHeader ");

		OJTPlanBranchColHeader = new HashMap<>();
		OJTPlanBranchColHeader.put("BranchName", " b.name");
		OJTPlanBranchColHeader.put("DeptId", "r.dept_id  ");
		OJTPlanBranchColHeader.put("DeptName", "d.dept_name ");
		OJTPlanBranchColHeader.put("LevelName", "sl.level_name ");
		OJTPlanBranchColHeader.put("Plan", "plan ");
		OJTPlanBranchColHeader.put("PendingCount", "pendingCount ");
		OJTPlanBranchColHeader.put("CompleteCount", "completeCount ");
		OJTPlanBranchColHeader.put("PendingPercentage", "pendingPercentage ");
		OJTPlanBranchColHeader.put("CompletePercentage", "completePercentage ");
		OJTPlanBranchColHeader.put("LineName", "lineName ");
		OJTPlanBranchColHeader.put("LineId", "lineId ");
		OJTPlanBranchColHeader.put("LevelId", "r.current_skill_level_id ");


	}

	public static Map<String, String> OJTPlanDeptColHeader;

	static {
		LOGGER.info("ColumnAscDescConstants || ojtPlanDeptWiseReport || OJTPlanDeptColHeader ");

		OJTPlanDeptColHeader = new HashMap<>();
		OJTPlanDeptColHeader.put("DeptName", " d.dept_name");
		OJTPlanDeptColHeader.put("LineName", " ln.name");
		OJTPlanDeptColHeader.put("LineId", " r.line_id");
		OJTPlanDeptColHeader.put("WorkstationId", "r.workstation_id ");
		OJTPlanDeptColHeader.put("Workstation", " w.workstation ");
		OJTPlanDeptColHeader.put("LevelName", "sl.level_name ");
		OJTPlanDeptColHeader.put("Plan", "plan ");
		OJTPlanDeptColHeader.put("PendingCount", "pendingCount ");
		OJTPlanDeptColHeader.put("CompleteCount", "completeCount ");
		OJTPlanDeptColHeader.put("PendingPercentage", "pendingPercentage ");
		OJTPlanDeptColHeader.put("CompletePercentage", "completePercentage ");
		OJTPlanDeptColHeader.put("LevelId", " sl.id");
		OJTPlanDeptColHeader.put("BranchName", " b.name");


	}
	
	public static Map<String, String> AssessmentConfigColName;
	static {
		LOGGER.info("ColumnAscDescConstants || AssessmentConfigColName ");

		AssessmentConfigColName = new HashMap<>();
		AssessmentConfigColName.put("BranchName", " b.name ");
		AssessmentConfigColName.put("LevelName", " sl.level_name ");
		AssessmentConfigColName.put("CreatedDate", " sc.created_date  ");
		AssessmentConfigColName.put("NoOfDays", " sc.no_of_days ");
	
	}
	
	public static Map<String, String> smOJTRegisListColName;
	
	static {
		LOGGER.info("ColumnAscDescConstants || smOJTRegisListColName ");
		
		smOJTRegisListColName = new HashMap<>();
		smOJTRegisListColName.put("OjtRegisId", " sor.id ");
		smOJTRegisListColName.put("EmpName", " empName ");
		smOJTRegisListColName.put("RegisteredDate", " sor.created_date ");
		smOJTRegisListColName.put("Status", " sor.status ");
		smOJTRegisListColName.put("LevelId", " sor.desired_skill_level_id ");
		smOJTRegisListColName.put("Level", " sl.level_name ");
		smOJTRegisListColName.put("BranchId", " sor.branch_id ");
		smOJTRegisListColName.put("DeptId", " sor.dept_id ");
		smOJTRegisListColName.put("LineId", " sor.line_id ");
		smOJTRegisListColName.put("WorkStationId", " sor.workstation_id ");
		smOJTRegisListColName.put("EmpId", " sor.emp_id ");
		smOJTRegisListColName.put("CompanyEmpId", " e.cmpy_emp_id ");
		smOJTRegisListColName.put("BranchName", " mb.name ");
		smOJTRegisListColName.put("DeptName", " md.dept_name ");
		smOJTRegisListColName.put("LineName", " ln.name ");
		smOJTRegisListColName.put("WorkstationName", " sw.workstation ");
		smOJTRegisListColName.put("AssessmentStatus", " sa.assessment_status ");

	}
	
	public static Map<String, String> wFDeployMentColName;
	static {
		LOGGER.info("ColumnAscDescConstants || wFDeployMentColName ");

		wFDeployMentColName = new HashMap<>();
		wFDeployMentColName.put("Department", "d.dept_name  ");
		wFDeployMentColName.put("Line", "l.name ");
		wFDeployMentColName.put("Shift", "ss.shift_name ");
		wFDeployMentColName.put("FromDate", "sd.from_date ");
		wFDeployMentColName.put("ToDate", "sd.to_date ");
	}

}
