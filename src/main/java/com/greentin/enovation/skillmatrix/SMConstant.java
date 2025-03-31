package com.greentin.enovation.skillmatrix;

public class SMConstant {

	public static final String CHECKSHEET_NOT_FOUND = "Checksheet not found";
	public static final String CHECKSHEET_POINT_NOT_FOUND = "Checksheet Point not found";
	public static final String ASSESSMENT_NOT_FOUND = "Assessment not found";
	public static final String DEPARTMENTID_NOT_FOUND = "Department not found";
	public static final String BRANCHID_NOT_FOUND = "Branch not found,";
	public static final String USER_NOT_FOUND = "User not ound";
	public static final String ASSESSMENT_DAYS_FOUND = "Assessment Days not found";
	public static final String CERTIFICATE_NOT_FOUND = "Certificate setup not found";
	public static final String CHECKSHEET_ALREADY_EXIST ="Checksheet is already exist";
	public static final Integer STAGE1_ID = 1;
	public static final Integer STAGE2_ID = 2;
	public static final Integer STAGE2_VERIFICATION_ID = 3;
	public static final Integer STAGE3_ID = 4;
	public static final Integer STAGE4_ID = 5;
	public static final Integer STAGE5_ID = 6;
	public static final Integer STAGE7_ID = 7;
	public static final long TRAINER_USER_TYPE_ID = 1;
	public static final long TL_USER_TYPE_ID = 3;
	public static final Integer GAPTYPEID = 2;
	public static final String ASSESSMENT_PENDING = "ASSESSMENT_PENDING";
	public static final String SAFETY_ASSESSMENT = "SAFETY";
	public static final String PUBLISHED_ASSESSMENT = "PUBLISHED";
	public static final String REJECTED = "REJECTED";
	public static final String PENDING = "PENDING";
	public static final String LEVEL_ASSESSMENT = "LEVEL";

	public static final String[] COLUMNS_FIELDS = { "empId", "cmpyEmpId", "empName" };
	public static final String[] COLUMNS_HEADING = { "empId", "Employee Id", "Name" };
	public static final String PUBLISH = "PUBLISH";
	public static final String USER_TYPE_TRAINER = "TRAINER";
	public static final String UPDATE = "UPDATE";
	public static final String DELETE = "DELETE";
	public static final String ADD = "ADD";

	public static final String QUESTION_NOT_FOUND = "Question not found";
	public static final String QUESTION_TYPE = "SINGLE";

	public static final String ASSESSMENT_PASS = "PASS";
	public static final String ASSESSMENT_FAIL = "FAIL";
	public static final String SAFETY_FAIL = "SAFETY FAIL";
	public static final String COMPLETED_STRING = "COMPLETED";

	public static final String DATA_NOT_FOUND = "Data Not Found";
	public static final String ALREADY_IN_USE = "Already in use, it can not be removed.";
	

	public static final long CYCLE_PLAN = 4;
	public static final String INACTIVE = "Inactive";
	public static final String ASSESSMENT_PUBLISHED = "PUBLISHED";
	public static final String ASSESSMENT_CREATED = "CREATED";

	

	/*
	 * Assessment Excel Constant start here
	 */
	public static final String EXCEL_UPLOAD = "EXCEL_UPLOAD";
	public static final String UPLOADED_ON = "UPLOADED_ON";
	public static final String EMPLOYEE = "EMPLOYEE";
	public static final String ASSESSMENT_DETAILS = "assessmentDetails";
	public static final String[] ASSESSMENT_UPLOAD_MANDATORY_HEADER = { "Assessment Level (Mandatory)",
			"Plant Name (Mandatory)","Department (Mandatory)","Line (Mandatory)","Workstation (Mandatory)","Assessment Title (Mandatory)", "Question (Mandatory)",
			"Correct Answer (Mandatory for MCQ)", "Question Mark (Mandatory)", "Assessment Duration(Mandatory)",
			"Passing Mark(Mandatory)","Option 1 (Mandatory)" };
//	public static final String[] ASSESSMENT_UPLOAD_MANDATORY_HEADER = { "Assessment Level (Mandatory)",
//			"Plant Name (Mandatory)","Assessment Title (Mandatory)", "Question (Mandatory)",
//			"Correct Answer (Mandatory for MCQ)", "Question Mark (Mandatory)", "Assessment Duration(Mandatory)",
//			"Passing Mark(Mandatory)" };
	public static final String LABEL_CORRECT_ANSWER = "Correct Answer (Mandatory for MCQ)";
	public static final String LABEL_OPTION_ONE = "Option 1 (Mandatory)";
	public static final String LABEL_OPTION_TWO = "Option 2 (Optional)";
	public static final String LABEL_OPTION_THREE = "Option 3 (Optional)";
	public static final String LABEL_OPTION_FOUR = "Option 4 (Optional)";
	public static final String LABEL_OPTION_FIVE = "Option 5 (Optional)";
	public static final String LABEL_QUESTION = "Question (Mandatory)";
	public static final String PLANT_NAME = "Plant Name (Mandatory)";
	public static final String ASSESSMENT_LEVEL = "Assessment Level (Mandatory)";
	public static final String QUE_MARK = "Question Mark (Mandatory)";
	public static final String ASSESSMENT_TITLE = "Assessment Title (Mandatory)";
	public static final String LABEL_ASSESSMENT_DURATION = "Assessment Duration(Mandatory)";
	public static final String ASSESSMENT = "ASSESSMENT";
	public static final String PASSING_MARK = "Passing Mark(Mandatory)";
	
	public static final String DEPT_NAME = "Department (Mandatory)";
	public static final String CATEGORY_NAME = "Category (Optional)";
	public static final String LINE_NAME = "Line (Mandatory)";
	public static final String WORKSTATION_NAME = "Workstation (Mandatory)";
	public static final String ASSESSMENT_TYPE = "Assessment Type (Mandatory)";

	// error constant
	public static final String SKILL_LEVEL_NOT_EXIST = " Skill Level not exist,";
	public static final String ASSESSMENT_TITLE_IS_MANDATORY = " Assessment Title is mandatory,";
	public static final String ASSESSMENT_DURATION_IS_MANDATORY = " Assessment Time is mandatory,";
	public static final String QUESTION_IS_MANDATORY = " Question is mandatory,";
	public static final String CORRECT_OPTION_IS_MANDATORY = " Correct Option is mandatory,";
	public static final String PASSING_MARK_IS_MANDATORY = " Passing Mark is mandatory,";
	public static final String QUESTION_MARK_IS_MANDATORY = " Question Mark is mandatory,";
	public static final String EXCEL_NOT_IN_VALID_FORMAT = " Invalid file format, please download sample file format and upload the data";
	public static final String OPTION_IS_MANDATORY = " Option 1 is mandatory,";
	public static final String ASSESSMENT_TYPE_IS_MANDATORY = " AssessmentType is mandatory,";
	public static final int ONE = 1;

	/*
	 * Assessment Excel Constant end here
	 */

	public static final String PASS = "PASS";
	public static final String EMP_ALREADY_HAVE_OJT = "Employee already have OJT";
	
	public static final String TASK_COMPLETION_STATUS = "Task_Completion_Status";
	public static final String SKILL_GAP_ANALYSIS = "Skill_Gap_Analysis";
	public static final String COMPLETION_DISCREPANCY_REPORT = "Completion_Discrepancy_Report";
	public static final String MULTISKILLING_STATUS_AT_CELL_LEVEL = "Multiskilling_Status_At_Cell_Level";
	public static final String MULTISKILLING_STATUS_AT_PLANT_LEVEL = "Multiskilling_Status_At_Plant_Level";
	public static final String ASSESSMENT_SUCCESS_RATE = "Assessment_Success_Rate";
	public static final String PLANT_ADHERENCE_STATUS = "Plant_Adherence_Status";
	public static final String CELL_ADHERENCE_STATUS = "Cell_Adherence_Status";
	public static final String EMPLOYEE_PERFORMANCE_ANALYSIS = "Employee_Performance_Analysis";
	public static final String MONTHLY_SKILL_MATRIX_REPORT = "Monthly_Skill_Matrix_Report";
	
	public static final String[] COLUMNS_SM_FIELDS = { "empId", "empName", "gender","experience","empLevel" };
	public static final String[] COLUMNS_SM_HEADING = { "Emp Id", "Name", "M/F","Exp","Employee Level" };
	
	// added for Skill matrix Home screen
	public static final int L1_SKILL_LEVEL_MARK = 1;
	public static final int L2_SKILL_LEVEL_MARK = 2;
	public static final int L3_SKILL_LEVEL_MARK = 3;
	public static final int L4_SKILL_LEVEL_MARK = 4;
	
	public static final String L1_SKILL_LEVEL = "L1";
	public static final String L2_SKILL_LEVEL = "L2";
	public static final String L3_SKILL_LEVEL = "L3";
	public static final String L4_SKILL_LEVEL = "L4";
	
	public static final double REQUIRED_NO_TRAINED = 1.2;	
	public static final int L1_ID = 1;
	public static final int L2_ID  = 2;
	public static final int L3_ID  = 3;
	public static final int L4_ID  = 4;
	public static final String TL = "TL";
	
	public static final String SAVE = "SAVE";
}
