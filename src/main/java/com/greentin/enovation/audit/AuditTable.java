package com.greentin.enovation.audit;

public class AuditTable {

	public static abstract class CWAgency {
		public static final String NAME = "Name";
	}

	public static abstract class AuditSMModel {
		public static final String MODEL_NAME = "modelName";
	}

	public static abstract class AuditSMGapReason {
		public static final String REASON = "reason";
	}

	public static abstract class AuditSMShifts {
		public static final String SHIFT_NAME = "shiftName";
		public static final String IS_ACTIVE = "isActive";
	}

	public static abstract class AuditSMWorkflowConfig {
		public static final String IS_ACTIVE = "isActive";
	}

	public static abstract class AuditSMAssessment {
		public static final String TITLE = "title";
		public static final String TIME = "time";
		public static final String PASSINGMARKS = "passingMarks";
		public static final String SKIILLLEVEL = "skillLevel";
		public static final String ISACTIVE = "IsActive";
		public static final String ASSESSMENTTYPE = "assessmentType";
	}

	public static abstract class AuditSMAssessmentOptions {
		public static final String OPTION = "option";
		public static final String ISRIGHTANS = "isRightAns";

	}

	public static abstract class AuditSMAssessmentQuestions {
		public static final String QUESTION = "qestion";
		public static final String QUESTIONTYPEID = "questionTypeId";
		public static final String ISACTIVE = "IsActive";
	}

	public static abstract class AuditSMChecksheet {
		public static final String TITLE = "title";
		public static final String NOOFDAYS = "noOfDays";
		public static final String SKIILLLEVEL = "skillLevel";
		public static final String ISACTIVE = "IsActive";
	}

	public static abstract class AuditSMChecksheetPoint {
		public static final String ITEMNAME = "itemName";
		public static final String REFERENCE = "reference";
		public static final String DAYNO = "DayNO";
		public static final String ISACTIVE = "IsActive";
	}

	public static abstract class SMWorkstation {
		public static final String WORKSTATION = "workstation";
		public static final String MACHINE_INDEX = "machineIndex";
		public static final String REQ_SKILL_LEVELID = "reqSkillLevelId";
		public static final String IS_ACTIVE = "isActive";
		public static final String BRANCH = "branch";
		public static final String DEPT = "dept";
		public static final String LINE = "line";
		public static final String REQUIRED_WORKFORCE = "requiredWorkforce";
		public static final String MACHINE_COUNT = "machineCount";
	}

	public static abstract class AuditSMUserType {
		public static final String ID = "id";
		public static final String BRANCH = "branchId";
		public static final String DEPT = "dept";
		public static final String ISACTIVE = "isactive";
		public static final String LINE = "lineId";

	}

	public static abstract class SMChecksheetParameter {
		public static final String ID = "id";
		public static final String ISACTIVE = "IsActive";
		public static final String PARAMETER = "parameter";
		public static final String PARAMETERTYPE = "parameterType";
		public static final String CHECKSHEET = "checksheet";
	}

	public static abstract class AuditSMMasterCertificate {
		public static final String STATUS = "status";
		public static final String ISACTIVE = "IsActive";
		public static final String CERTIFICATE_CAPTION = "certificateCaption";
		public static final String CERTIFICATE_NAME = "certificateName";
		public static final String CERTIFICATE_PATH = "certificatePath";
	}

	public static abstract class SMOJTPlan {
		public static final String ID = "id";
		public static final String BRANCH = "branch";
		public static final String DEPT = "dept";
		public static final String YEARVALUE = "yearValue";
		public static final String MONTHVALUE = "monthValue";
		public static final String STARTDATE = "startDate";
		public static final String STATUS = "status";
	}

	public static abstract class SMOJTRegis {
		public static final String EMPID = "empId";
		public static final String DESIREDLVLID = "desiredSkillLevelId";
		public static final String PLANID = "planId";
		public static final String TRAINEREMPID = "trainerId";
		public static final String WORKSTATIONID = "workstationId";
		public static final String STATUS = "status";
	}

	public static abstract class AuditSMConfig {
		public static final String NOOFDAYS = "noOfDays";
		public static final String SKILLLEVELID = "skillLevelId";

	}

	public static abstract class AuditSMStageLabel {
		public static final String STAGE_ID = "stageId";
		public static final String BRANCH_ID = "branchId";
		public static final String STAGE_LABEL = "stageLabel";

	}

	public static abstract class AuditCategory {
		public static final String ID = "id";
		public static final String CATEGORYNAME = "categoryName";
		public static final String ASSESSMENTID = "assessmnetId";

	}
	
	public static abstract class ProductOrgConfig {

		public static final String Activity = "Activity";
		public static final String Change_Document = "ChangeDocument";
		public static final String Before_Image = "BeforeImage";
		public static final String After_Image = "AfterImage";
		public static final String Happiness_Index = "HappinessIndex";
		public static final String Document_Verify = "DocumentVerify";
		public static final String Tier1_Self_Impl = "Tier1SelfImpl";
		public static final String Particular = "Particular";
		public static final String Benifits = "Benifits";
		public static final String Sustainance_Audit = "SustainanceAudit";
		public static final String HD_Points = "HD_Points";
		public static final String Kaizen_Sustainance_Audit = "KaizenSustainanceAudit";
		public static final String SAP_Code_Mandatory = "SAPCodeMandatory";
		public static final String Sustenance_Audit_Mandatory = "SustenanceAuditMandatory";
		public static final String SkipTierProcess = "SkipTierProcess";
		public static final String IS_IMPL_PRIORITY_ON = "isImplmentationPriorityOn";
		public static final String iS_TARGET_MANDATORY = "isTargetMandatory";
		public static final String IS_BENCHMARK_MANDATORY = "isBenchmarkMandatory";
		public static final String tierTwoSelfImpl ="tierTwoSelfImpl";
		public static final String isThemeCostMandate ="isThemeCostMandate";

	}

}
