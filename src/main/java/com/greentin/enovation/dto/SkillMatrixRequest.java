package com.greentin.enovation.dto;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.greentin.enovation.model.skillMatrix.SMAssessmentOptions;
import com.greentin.enovation.model.skillMatrix.SMChecksheetParameter;
import com.greentin.enovation.model.skillMatrix.SMChecksheetPoints;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.time.LocalDate;

/**
 * @author Rajde
 *
 */

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class SkillMatrixRequest {

	private int branchId;

	private int deptId;

	private String fromDt;

	private String toDt;

	private int updatedBy;

	private int createdBy;

	private long id;

	private String modelName;

	private Boolean isActive;

	private long modelId;

	private String reason;

	private long reasonId;

	private String shiftName;

	private long shiftId;

	private long stageId;

	private long skillLevelId;

	private long workflowId;

	private String workstation;

	private double machineIndex;

	private double requiredWorkforce;

	private long reqSkillLevelId;

	private List<Integer> deptIds;

	private List<Integer> userTypeIds;

	private long userTypeId;

	private int empId;

	private List<Integer> branchIds;

	private String actionBy;

	private Timestamp updatedDate;

	private String title;

	private int time;

	private long skillLvlId;

	private double passingMark;

	private long assessmentId;

	private long assessmentQueId;

	private long assessmentQueOptId;

	private long questionTypeId;

	private String question;

	private double queMark;

	private String option;

	private String branchName;

	private double totalMarks;

	private boolean isRightAns;

	private String skillLevel;

	private String assessmentTitle;

	private long checkSheetId;

	private List<SMAssessmentOptions> optList;

	private int noOfDays;

	private String itemName;

	private Timestamp createdDate;

	private int dayNo;

	private long currentSkillLevelId;

	private long desiredSkillLevelId;

	private long ojtRegiId;

	private int trainerEmpId;

	private long currentActivityId;

	private long skillingId;

	private String comments;

	private long checksheetPointsId;

	private MultipartFile certificatePath;

	private String certificateName;

	private String certificateCaption;

	private long certificateId;

	private String status;

	private String parameter;

	private long parameterTypeId;

	private long parameterId;

	private String userType;

	private long checksheetPointId;

	private String comment;

	private List<SMChecksheetParameter> smParameters;

	private int orgId;

	private long workstationId;

	private List<SkillMatrixRequest> workflowConfigList;

	private long ojtAuditId;

	private List<SkillMatrixRequest> list;

	private long skillingChecksheetId;

	private long ojtAssId;

	private long ojtAssQuesId;

	private String answer;

	private double obtainedMarks;

	private List<SMAssessmetDTO> queList;

	private List<SMAssessmetDTO> queOptionList;

	private long questionId;

	private long optionId;

	private long smPointId;

	private long smPointAuditId;

	private int limit;

	private int offset;

	private String colName;

	private String orderType;

	private String search;

	private long selectedId;

	private List<Integer> skillLevelIds;

	private long skillingChecksheetPointId;

	private List<SMChecksheetPoints> dayPointList;

	private List<SMOJTPlanDTO> ojtRegisList;

	private List<SMOJTCSPointsAuditDTO> csPointAuditList;

	private List<SMOJTSkillingParameterDTO> skillingParamList;

	private long skillingAuditId;

	private int yearValue;

	private int monthValue;

	private String startDate;

	private List<Integer> workstationIds;

	private List<Integer> empIds;

	private int totalCount;

	private long ojtPlanId;

	private int cycleValue;

	private int oeEmpId;

	private int productionPlan;

	private String cyclePlan;

	private String reference;

	private List<SkillMatrixRequest> daysList;

	private List<SkillMatrixRequest> pointList;

	private MultipartFile[] excel;

	private String fileName;

	private String dbUploadPath;

	private String correctAnswer;

	private String optionOne;

	private String optionTwo;

	private String optionThree;

	private String optionFour;

	private String optionFive;

	private String errorMessage;

	private List<SkillMatrixRequest> assessmentList;

	private List<SkillMatrixRequest> errorList;

	private int statusCode;

	private boolean errorInSheet;

	private String sheetType;

	private String reportCaption;

	private List<SMOJTPlanDTO> cyclePlanList;

	private List<SkillMatrixRequest> empList;

	private String cmpyEmpId;

	private String action;

	private List<SkillMatrixRequest> parameterList;

	private String stageLabel;

	private long lineId;

	private List<Long> lineIds;

	private long wFDId;

	private String categoryName;

	private long categoryId;

	private String empName;

	private String emailId;

	private String lineName;

	private String deptName;

	private String trainerName;

	private String trainerEmailId;

	private String portalLink;

	private String logo;

	private String category;

	private String assignedBy;

	private String assignedDate;

	private String assignedTo;

	private String desiredSkillLevelName;

	private int tlEmpId;

	private int shiftNo;

	private String assessmentType;

	private LocalDate assessmentDueDate;

	private String currentSkillLevelName;

	private String docName;

	private int assessmentTimeInterval;

	private String assessmentStatus;

	private String assignedByEmail;
	
	private int machineCount;

	public int getBranchId() {
		return branchId;
	}

	public void setBranchId(int branchId) {
		this.branchId = branchId;
	}

	public int getDeptId() {
		return deptId;
	}

	public void setDeptId(int deptId) {
		this.deptId = deptId;
	}

	public String getFromDt() {
		return fromDt;
	}

	public void setFromDt(String fromDt) {
		this.fromDt = fromDt;
	}

	public String getToDt() {
		return toDt;
	}

	public void setToDt(String toDt) {
		this.toDt = toDt;
	}

	public int getUpdatedBy() {
		return updatedBy;
	}

	public void setUpdatedBy(int updatedBy) {
		this.updatedBy = updatedBy;
	}

	public int getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(int createdBy) {
		this.createdBy = createdBy;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getModelName() {
		return modelName;
	}

	public void setModelName(String modelName) {
		this.modelName = modelName;
	}

	public String getWorkstation() {
		return workstation;
	}

	public void setWorkstation(String workstation) {
		this.workstation = workstation;
	}

	public double getRequiredWorkforce() {
		return requiredWorkforce;
	}

	public void setRequiredWorkforce(double requiredWorkforce) {
		this.requiredWorkforce = requiredWorkforce;
	}

	public long getReqSkillLevelId() {
		return reqSkillLevelId;
	}

	public void setReqSkillLevelId(long reqSkillLevelId) {
		this.reqSkillLevelId = reqSkillLevelId;
	}

	public Boolean getIsActive() {
		return isActive;
	}

	public void setIsActive(Boolean isActive) {
		this.isActive = isActive;
	}

	public long getModelId() {
		return modelId;
	}

	public void setModelId(long modelId) {
		this.modelId = modelId;
	}

	public String getReason() {
		return reason;
	}

	public void setReason(String reason) {
		this.reason = reason;
	}

	public long getReasonId() {
		return reasonId;
	}

	public void setReasonId(long reasonId) {
		this.reasonId = reasonId;
	}

	public String getShiftName() {
		return shiftName;
	}

	public void setShiftName(String shiftName) {
		this.shiftName = shiftName;
	}

	public long getShiftId() {
		return shiftId;
	}

	public void setShiftId(long shiftId) {
		this.shiftId = shiftId;
	}

	public long getStageId() {
		return stageId;
	}

	public void setStageId(long stageId) {
		this.stageId = stageId;
	}

	public long getSkillLevelId() {
		return skillLevelId;
	}

	public void setSkillLevelId(long skillLevelId) {
		this.skillLevelId = skillLevelId;
	}

	public long getWorkflowId() {
		return workflowId;
	}

	public void setWorkflowId(long workflowId) {
		this.workflowId = workflowId;
	}

	public List<Integer> getDeptIds() {
		return deptIds;
	}

	public void setDeptIds(List<Integer> deptIds) {
		this.deptIds = deptIds;
	}

	public long getUserTypeId() {
		return userTypeId;
	}

	public void setUserTypeId(long userTypeId) {
		this.userTypeId = userTypeId;
	}

	public int getEmpId() {
		return empId;
	}

	public void setEmpId(int empId) {
		this.empId = empId;
	}

	public List<Integer> getBranchIds() {
		return branchIds;
	}

	public void setBranchIds(List<Integer> branchIds) {
		this.branchIds = branchIds;
	}

	public List<Integer> getUserTypeIds() {
		return userTypeIds;
	}

	public void setUserTypeIds(List<Integer> userTypeIds) {
		this.userTypeIds = userTypeIds;
	}

	public String getActionBy() {
		return actionBy;
	}

	public void setActionBy(String actionBy) {
		this.actionBy = actionBy;
	}

	public Timestamp getUpdatedDate() {
		return updatedDate;
	}

	public void setUpdatedDate(Timestamp updatedDate) {
		this.updatedDate = updatedDate;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public int getTime() {
		return time;
	}

	public void setTime(int time) {
		this.time = time;
	}

	public long getSkillLvlId() {
		return skillLvlId;
	}

	public void setSkillLvlId(long skillLvlId) {
		this.skillLvlId = skillLvlId;
	}

	public double getPassingMark() {
		return passingMark;
	}

	public void setPassingMark(double passingMark) {
		this.passingMark = passingMark;
	}

	public long getAssessmentId() {
		return assessmentId;
	}

	public void setAssessmentId(long assessmentId) {
		this.assessmentId = assessmentId;
	}

	public long getAssessmentQueId() {
		return assessmentQueId;
	}

	public void setAssessmentQueId(long assessmentQueId) {
		this.assessmentQueId = assessmentQueId;
	}

	public long getAssessmentQueOptId() {
		return assessmentQueOptId;
	}

	public void setAssessmentQueOptId(long assessmentQueOptId) {
		this.assessmentQueOptId = assessmentQueOptId;
	}

	public long getQuestionTypeId() {
		return questionTypeId;
	}

	public void setQuestionTypeId(long questionTypeId) {
		this.questionTypeId = questionTypeId;
	}

	public String getQuestion() {
		return question;
	}

	public void setQuestion(String question) {
		this.question = question;
	}

	public List<SMAssessmentOptions> getOptList() {
		return optList;
	}

	public void setOptList(List<SMAssessmentOptions> optList) {
		this.optList = optList;
	}

	public double getQueMark() {
		return queMark;
	}

	public void setQueMark(double queMark) {
		this.queMark = queMark;
	}

	public String getOption() {
		return option;
	}

	public void setOption(String option) {
		this.option = option;
	}

	public String getBranchName() {
		return branchName;
	}

	public void setBranchName(String branchName) {
		this.branchName = branchName;
	}

	public double getTotalMarks() {
		return totalMarks;
	}

	public void setTotalMarks(double totalMarks) {
		this.totalMarks = totalMarks;
	}

	public boolean isRightAns() {
		return isRightAns;
	}

	public void setRightAns(boolean isRightAns) {
		this.isRightAns = isRightAns;
	}

	public String getSkillLevel() {
		return skillLevel;
	}

	public void setSkillLevel(String skillLevel) {
		this.skillLevel = skillLevel;
	}

	public int getNoOfDays() {
		return noOfDays;
	}

	public void setNoOfDays(int noOfDays) {
		this.noOfDays = noOfDays;
	}

	public long getCheckSheetId() {
		return checkSheetId;
	}

	public void setCheckSheetId(long checkSheetId) {
		this.checkSheetId = checkSheetId;
	}

	public String getItemName() {
		return itemName;
	}

	public void setItemName(String itemName) {
		this.itemName = itemName;
	}

	public int getDayNo() {
		return dayNo;
	}

	public void setDayNo(int dayNo) {
		this.dayNo = dayNo;
	}

	public Timestamp getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(Timestamp createdDate) {
		this.createdDate = createdDate;
	}

	public String getAnswer() {
		return answer;
	}

	public void setAnswer(String answer) {
		this.answer = answer;
	}

	public double getObtainedMarks() {
		return obtainedMarks;
	}

	public void setObtainedMarks(double obtainedMarks) {
		this.obtainedMarks = obtainedMarks;
	}

	public List<SMAssessmetDTO> getQueList() {
		return queList;
	}

	public void setQueList(List<SMAssessmetDTO> queList) {
		this.queList = queList;
	}

	public long getQuestionId() {
		return questionId;
	}

	public void setQuestionId(long questionId) {
		this.questionId = questionId;
	}

	public List<SMAssessmetDTO> getQueOptionList() {
		return queOptionList;
	}

	public void setQueOptionList(List<SMAssessmetDTO> queOptionList) {
		this.queOptionList = queOptionList;
	}

	public long getOptionId() {
		return optionId;
	}

	public void setOptionId(long optionId) {
		this.optionId = optionId;
	}

	public long getOjtAssId() {
		return ojtAssId;
	}

	public void setOjtAssId(long ojtAssId) {
		this.ojtAssId = ojtAssId;
	}

	public long getOjtAssQuesId() {
		return ojtAssQuesId;
	}

	public void setOjtAssQuesId(long ojtAssQuesId) {
		this.ojtAssQuesId = ojtAssQuesId;
	}

	public long getCurrentSkillLevelId() {
		return currentSkillLevelId;
	}

	public void setCurrentSkillLevelId(long currentSkillLevelId) {
		this.currentSkillLevelId = currentSkillLevelId;
	}

	public long getDesiredSkillLevelId() {
		return desiredSkillLevelId;
	}

	public void setDesiredSkillLevelId(long desiredSkillLevelId) {
		this.desiredSkillLevelId = desiredSkillLevelId;
	}

	public long getOjtRegiId() {
		return ojtRegiId;
	}

	public void setOjtRegiId(long ojtRegiId) {
		this.ojtRegiId = ojtRegiId;
	}

	public int getTrainerEmpId() {
		return trainerEmpId;
	}

	public void setTrainerEmpId(int trainerEmpId) {
		this.trainerEmpId = trainerEmpId;
	}

	public long getCurrentActivityId() {
		return currentActivityId;
	}

	public void setCurrentActivityId(long currentActivityId) {
		this.currentActivityId = currentActivityId;
	}

	public String getComments() {
		return comments;
	}

	public void setComments(String comments) {
		this.comments = comments;
	}

	public long getChecksheetPointsId() {
		return checksheetPointsId;
	}

	public void setChecksheetPointsId(long checksheetPointsId) {
		this.checksheetPointsId = checksheetPointsId;
	}

	public MultipartFile getCertificatePath() {
		return certificatePath;
	}

	public void setCertificatePath(MultipartFile certificatePath) {
		this.certificatePath = certificatePath;
	}

	public String getCertificateName() {
		return certificateName;
	}

	public void setCertificateName(String certificateName) {
		this.certificateName = certificateName;
	}

	public String getCertificateCaption() {
		return certificateCaption;
	}

	public void setCertificateCaption(String certificateCaption) {
		this.certificateCaption = certificateCaption;
	}

	public long getCertificateId() {
		return certificateId;
	}

	public void setCertificateId(long certificateId) {
		this.certificateId = certificateId;
	}

	public String getParameter() {
		return parameter;
	}

	public void setParameter(String parameter) {
		this.parameter = parameter;
	}

	public long getParameterTypeId() {
		return parameterTypeId;
	}

	public void setParameterTypeId(long parameterTypeId) {
		this.parameterTypeId = parameterTypeId;
	}

	public long getParameterId() {
		return parameterId;
	}

	public void setParameterId(long parameterId) {
		this.parameterId = parameterId;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getUserType() {
		return userType;
	}

	public void setUserType(String userType) {
		this.userType = userType;
	}

	public long getChecksheetPointId() {
		return checksheetPointId;
	}

	public void setChecksheetPointId(long checksheetPointId) {
		this.checksheetPointId = checksheetPointId;
	}

	public long getSkillingId() {
		return skillingId;
	}

	public void setSkillingId(long skillingId) {
		this.skillingId = skillingId;
	}

	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

	public List<SMChecksheetParameter> getSmParameters() {
		return smParameters;
	}

	public void setSmParameters(List<SMChecksheetParameter> smParameters) {
		this.smParameters = smParameters;
	}

	public int getOrgId() {
		return orgId;
	}

	public void setOrgId(int orgId) {
		this.orgId = orgId;
	}

	public long getWorkstationId() {
		return workstationId;
	}

	public void setWorkstationId(long workstationId) {
		this.workstationId = workstationId;
	}

	public List<SkillMatrixRequest> getWorkflowConfigList() {
		return workflowConfigList;
	}

	public void setWorkflowConfigList(List<SkillMatrixRequest> workflowConfigList) {
		this.workflowConfigList = workflowConfigList;
	}

	public long getOjtAuditId() {
		return ojtAuditId;
	}

	public void setOjtAuditId(long ojtAuditId) {
		this.ojtAuditId = ojtAuditId;
	}

	public List<SkillMatrixRequest> getList() {
		return list;
	}

	public void setList(List<SkillMatrixRequest> list) {
		this.list = list;
	}

	public long getSkillingChecksheetId() {
		return skillingChecksheetId;
	}

	public void setSkillingChecksheetId(long skillingChecksheetId) {
		this.skillingChecksheetId = skillingChecksheetId;
	}

	public long getSmPointId() {
		return smPointId;
	}

	public void setSmPointId(long smPointId) {
		this.smPointId = smPointId;
	}

	public long getSmPointAuditId() {
		return smPointAuditId;
	}

	public void setSmPointAuditId(long smPointAuditId) {
		this.smPointAuditId = smPointAuditId;
	}

	public int getLimit() {
		return limit;
	}

	public void setLimit(int limit) {
		this.limit = limit;
	}

	public int getOffset() {
		return offset;
	}

	public void setOffset(int offset) {
		this.offset = offset;
	}

	public String getColName() {
		return colName;
	}

	public void setColName(String colName) {
		this.colName = colName;
	}

	public String getOrderType() {
		return orderType;
	}

	public void setOrderType(String orderType) {
		this.orderType = orderType;
	}

	public String getSearch() {
		return search;
	}

	public void setSearch(String search) {
		this.search = search;
	}

	public long getSelectedId() {
		return selectedId;
	}

	public void setSelectedId(long selectedId) {
		this.selectedId = selectedId;
	}

	public List<SMOJTPlanDTO> getOjtRegisList() {
		return ojtRegisList;
	}

	public void setOjtRegisList(List<SMOJTPlanDTO> ojtRegisList) {
		this.ojtRegisList = ojtRegisList;
	}

	public List<Integer> getSkillLevelIds() {
		return skillLevelIds;
	}

	public void setSkillLevelIds(List<Integer> skillLevelIds) {
		this.skillLevelIds = skillLevelIds;
	}

	public List<Integer> getWorkstationIds() {
		return workstationIds;
	}

	public void setWorkstationIds(List<Integer> workstationIds) {
		this.workstationIds = workstationIds;
	}

	public long getSkillingChecksheetPointId() {
		return skillingChecksheetPointId;
	}

	public void setSkillingChecksheetPointId(long skillingChecksheetPointId) {
		this.skillingChecksheetPointId = skillingChecksheetPointId;
	}

	public List<SMChecksheetPoints> getDayPointList() {
		return dayPointList;
	}

	public void setDayPointList(List<SMChecksheetPoints> dayPointList) {
		this.dayPointList = dayPointList;
	}

	public long getSkillingAuditId() {
		return skillingAuditId;
	}

	public void setSkillingAuditId(long skillingAuditId) {
		this.skillingAuditId = skillingAuditId;
	}

	public int getYearValue() {
		return yearValue;
	}

	public void setYearValue(int yearValue) {
		this.yearValue = yearValue;
	}

	public int getMonthValue() {
		return monthValue;
	}

	public void setMonthValue(int monthValue) {
		this.monthValue = monthValue;
	}

	public String getStartDate() {
		return startDate;
	}

	public void setStartDate(String startDate) {
		this.startDate = startDate;
	}

	public List<SMOJTCSPointsAuditDTO> getCsPointAuditList() {
		return csPointAuditList;
	}

	public void setCsPointAuditList(List<SMOJTCSPointsAuditDTO> csPointAuditList) {
		this.csPointAuditList = csPointAuditList;
	}

	public List<SMOJTSkillingParameterDTO> getSkillingParamList() {
		return skillingParamList;
	}

	public void setSkillingParamList(List<SMOJTSkillingParameterDTO> skillingParamList) {
		this.skillingParamList = skillingParamList;
	}

	public List<Integer> getEmpIds() {
		return empIds;
	}

	public void setEmpIds(List<Integer> empIds) {
		this.empIds = empIds;
	}

	public int getTotalCount() {
		return totalCount;
	}

	public void setTotalCount(int totalCount) {
		this.totalCount = totalCount;
	}

	public long getOjtPlanId() {
		return ojtPlanId;
	}

	public int getOeEmpId() {
		return oeEmpId;
	}

	public void setOeEmpId(int oeEmpId) {
		this.oeEmpId = oeEmpId;
	}

	public void setOjtPlanId(long ojtPlanId) {
		this.ojtPlanId = ojtPlanId;
	}

	public int getCycleValue() {
		return cycleValue;
	}

	public void setCycleValue(int cycleValue) {
		this.cycleValue = cycleValue;
	}

	public int getProductionPlan() {
		return productionPlan;
	}

	public void setProductionPlan(int productionPlan) {
		this.productionPlan = productionPlan;
	}

	public String getReference() {
		return reference;
	}

	public void setReference(String reference) {
		this.reference = reference;
	}

	public List<SkillMatrixRequest> getDaysList() {
		return daysList;
	}

	public void setDaysList(List<SkillMatrixRequest> daysList) {
		this.daysList = daysList;
	}

	public List<SkillMatrixRequest> getPointList() {
		return pointList;
	}

	public void setPointList(List<SkillMatrixRequest> pointList) {
		this.pointList = pointList;
	}

	public String getCyclePlan() {
		return cyclePlan;
	}

	public void setCyclePlan(String cyclePlan) {
		this.cyclePlan = cyclePlan;
	}

	public MultipartFile[] getExcel() {
		return excel;
	}

	public void setExcel(MultipartFile[] excel) {
		this.excel = excel;
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public String getDbUploadPath() {
		return dbUploadPath;
	}

	public void setDbUploadPath(String dbUploadPath) {
		this.dbUploadPath = dbUploadPath;
	}

	public List<SkillMatrixRequest> getAssessmentList() {
		return assessmentList;
	}

	public void setAssessmentList(List<SkillMatrixRequest> assessmentList) {
		this.assessmentList = assessmentList;
	}

	public String getAssessmentTitle() {
		return assessmentTitle;
	}

	public void setAssessmentTitle(String assessmentTitle) {
		this.assessmentTitle = assessmentTitle;
	}

	public String getCorrectAnswer() {
		return correctAnswer;
	}

	public void setCorrectAnswer(String correctAnswer) {
		this.correctAnswer = correctAnswer;
	}

	public String getOptionOne() {
		return optionOne;
	}

	public void setOptionOne(String optionOne) {
		this.optionOne = optionOne;
	}

	public String getOptionTwo() {
		return optionTwo;
	}

	public void setOptionTwo(String optionTwo) {
		this.optionTwo = optionTwo;
	}

	public String getOptionThree() {
		return optionThree;
	}

	public void setOptionThree(String optionThree) {
		this.optionThree = optionThree;
	}

	public String getOptionFour() {
		return optionFour;
	}

	public void setOptionFour(String optionFour) {
		this.optionFour = optionFour;
	}

	public String getOptionFive() {
		return optionFive;
	}

	public void setOptionFive(String optionFive) {
		this.optionFive = optionFive;
	}

	public String getErrorMessage() {
		return errorMessage;
	}

	public void setErrorMessage(String errorMessage) {
		this.errorMessage = errorMessage;
	}

	public List<SkillMatrixRequest> getErrorList() {
		return errorList;
	}

	public void setErrorList(List<SkillMatrixRequest> errorList) {
		this.errorList = errorList;
	}

	public boolean isErrorInSheet() {
		return errorInSheet;
	}

	public void setErrorInSheet(boolean errorInSheet) {
		this.errorInSheet = errorInSheet;
	}

	public String getSheetType() {
		return sheetType;
	}

	public void setSheetType(String sheetType) {
		this.sheetType = sheetType;
	}

	public int getStatusCode() {
		return statusCode;
	}

	public void setStatusCode(int statusCode) {
		this.statusCode = statusCode;
	}

	public String getReportCaption() {
		return reportCaption;
	}

	public void setReportCaption(String reportCaption) {
		this.reportCaption = reportCaption;
	}

	public List<SMOJTPlanDTO> getCyclePlanList() {
		return cyclePlanList;
	}

	public void setCyclePlanList(List<SMOJTPlanDTO> cyclePlanList) {
		this.cyclePlanList = cyclePlanList;
	}

	public List<SkillMatrixRequest> getEmpList() {
		return empList;
	}

	public void setEmpList(List<SkillMatrixRequest> empList) {
		this.empList = empList;
	}

	public String getCmpyEmpId() {
		return cmpyEmpId;
	}

	public void setCmpyEmpId(String cmpyEmpId) {
		this.cmpyEmpId = cmpyEmpId;
	}

	public String getAction() {
		return action;
	}

	public void setAction(String action) {
		this.action = action;
	}

	public List<SkillMatrixRequest> getParameterList() {
		return parameterList;
	}

	public void setParameterList(List<SkillMatrixRequest> parameterList) {
		this.parameterList = parameterList;
	}

	public String getStageLabel() {
		return stageLabel;
	}

	public void setStageLabel(String stageLabel) {
		this.stageLabel = stageLabel;
	}

	public long getLineId() {
		return lineId;
	}

	public void setLineId(long lineId) {
		this.lineId = lineId;
	}

	public long getwFDId() {
		return wFDId;
	}

	public void setwFDId(long wFDId) {
		this.wFDId = wFDId;
	}

	public List<Long> getLineIds() {
		return lineIds;
	}

	public void setLineIds(List<Long> lineIds) {
		this.lineIds = lineIds;
	}

	public long getCategoryId() {
		return categoryId;
	}

	public void setCategoryId(long categoryId) {
		this.categoryId = categoryId;
	}

	public String getCategoryName() {
		return categoryName;
	}

	public void setCategoryName(String categoryName) {
		this.categoryName = categoryName;
	}

	public double getMachineIndex() {
		return machineIndex;
	}

	public void setMachineIndex(double machineIndex) {
		this.machineIndex = machineIndex;
	}

	public String getEmpName() {
		return empName;
	}

	public void setEmpName(String empName) {
		this.empName = empName;
	}

	public String getEmailId() {
		return emailId;
	}

	public void setEmailId(String emailId) {
		this.emailId = emailId;
	}

	public String getLineName() {
		return lineName;
	}

	public void setLineName(String lineName) {
		this.lineName = lineName;
	}

	public String getDeptName() {
		return deptName;
	}

	public void setDeptName(String deptName) {
		this.deptName = deptName;
	}

	public String getTrainerName() {
		return trainerName;
	}

	public void setTrainerName(String trainerName) {
		this.trainerName = trainerName;
	}

	public String getTrainerEmailId() {
		return trainerEmailId;
	}

	public void setTrainerEmailId(String trainerEmailId) {
		this.trainerEmailId = trainerEmailId;
	}

	public String getPortalLink() {
		return portalLink;
	}

	public void setPortalLink(String portalLink) {
		this.portalLink = portalLink;
	}

	public String getLogo() {
		return logo;
	}

	public void setLogo(String logo) {
		this.logo = logo;
	}

	public String getAssignedBy() {
		return assignedBy;
	}

	public void setAssignedBy(String assignedBy) {
		this.assignedBy = assignedBy;
	}

	public String getAssignedDate() {
		return assignedDate;
	}

	public void setAssignedDate(String assignedDate) {
		this.assignedDate = assignedDate;
	}

	public String getAssignedTo() {
		return assignedTo;
	}

	public void setAssignedTo(String assignedTo) {
		this.assignedTo = assignedTo;
	}

	public String getDesiredSkillLevelName() {
		return desiredSkillLevelName;
	}

	public void setDesiredSkillLevelName(String desiredSkillLevelName) {
		this.desiredSkillLevelName = desiredSkillLevelName;
	}

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	public int getTlEmpId() {
		return tlEmpId;
	}

	public void setTlEmpId(int tlEmpId) {
		this.tlEmpId = tlEmpId;
	}

	public int getShiftNo() {
		return shiftNo;
	}

	public void setShiftNo(int shiftNo) {
		this.shiftNo = shiftNo;
	}

	public String getAssessmentType() {
		return assessmentType;
	}

	public void setAssessmentType(String assessmentType) {
		this.assessmentType = assessmentType;
	}

	public LocalDate getAssessmentDueDate() {
		return assessmentDueDate;
	}

	public void setAssessmentDueDate(LocalDate assessmentDueDate) {
		this.assessmentDueDate = assessmentDueDate;
	}

	public String getCurrentSkillLevelName() {
		return currentSkillLevelName;
	}

	public void setCurrentSkillLevelName(String currentSkillLevelName) {
		this.currentSkillLevelName = currentSkillLevelName;
	}

	public String getDocName() {
		return docName;
	}

	public void setDocName(String docName) {
		this.docName = docName;
	}

	public int getAssessmentTimeInterval() {
		return assessmentTimeInterval;
	}

	public void setAssessmentTimeInterval(int assessmentTimeInterval) {
		this.assessmentTimeInterval = assessmentTimeInterval;
	}

	public String getAssessmentStatus() {
		return assessmentStatus;
	}

	public void setAssessmentStatus(String assessmentStatus) {
		this.assessmentStatus = assessmentStatus;
	}

	public String getAssignedByEmail() {
		return assignedByEmail;
	}

	public void setAssignedByEmail(String assignedByEmail) {
		this.assignedByEmail = assignedByEmail;
	}

	public int getMachineCount() {
		return machineCount;
	}

	public void setMachineCount(int machineCount) {
		this.machineCount = machineCount;
	}

}
