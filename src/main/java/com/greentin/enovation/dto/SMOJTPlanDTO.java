package com.greentin.enovation.dto;

import java.util.HashMap;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

/**
 * @author Rajdeep MD August 30, 2023
 * @desc SkillingChecksheet
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_DEFAULT)
public class SMOJTPlanDTO {

	private long id;

	private int empId;

	private long currentSkillLevelId;

	private long desiredSkillLevelId;

	private int trainerEmpId;

	private long checkSheetId;

	private String action;

	private String status;

	private String fromDt;

	private String toDt;

	private int updatedBy;

	private int createdBy;

	private String createdDate;

	private String updatedDate;

	private long ojtRegisId;

	private String branchName;

	private int yearValue;

	private int monthValue;

	private String startDate;

	private String empName;

	private long ojtPlanId;

	private int branchId;

	private int deptId;

	private int oeEmpId;

	private String oeEmpName;

	private String deptName;

	private String workstation;

	private long workstationId;

	private List<HashMap<String, Object>> ojtRegiList;

	private int trainerId;

	private String trainer;

	private long auditId;

	private String activityDate;

	private String activity;

	private long activityId;

	private String completedDate;
	private String actualValue;

	private String expectedValue;

	private long cyclePlanId;

	private String trainerStatus;

	private int cycleNo;

	private long oJTCyclePlanId;

	private String cmpyEmpId;

	private String currentSkillLevel;

	private String desiredSkillLevel;

	private String requireSkillLevel;

	private long requireSkillLevelId;

	private long checksheetParameterId;

	private long skillingParameterId;

	private List<HashMap<String, Object>> column;

	private String lineName;

	private long lineId;

	private String assignedEmpId;

	private String assignedEmpName;

	private List<SMOJTSkillingAuditDTO> skillingAuditList;
	
	private long assessmentId;
	
	private long skillingId;
	
	private double totalMarks;
	
	private double passingMarks;

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
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

	public String getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(String createdDate) {
		this.createdDate = createdDate;
	}

	public String getUpdatedDate() {
		return updatedDate;
	}

	public void setUpdatedDate(String updatedDate) {
		this.updatedDate = updatedDate;
	}

	public long getOjtRegisId() {
		return ojtRegisId;
	}

	public void setOjtRegisId(long ojtRegisId) {
		this.ojtRegisId = ojtRegisId;
	}

	public String getBranchName() {
		return branchName;
	}

	public void setBranchName(String branchName) {
		this.branchName = branchName;
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

	public long getOjtPlanId() {
		return ojtPlanId;
	}

	public void setOjtPlanId(long ojtPlanId) {
		this.ojtPlanId = ojtPlanId;
	}

	public String getEmpName() {
		return empName;
	}

	public void setEmpName(String empName) {
		this.empName = empName;
	}

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

	public String getDeptName() {
		return deptName;
	}

	public void setDeptName(String deptName) {
		this.deptName = deptName;
	}

	public int getOeEmpId() {
		return oeEmpId;
	}

	public void setOeEmpId(int oeEmpId) {
		this.oeEmpId = oeEmpId;
	}

	public String getOeEmpName() {
		return oeEmpName;
	}

	public void setOeEmpName(String oeEmpName) {
		this.oeEmpName = oeEmpName;
	}

	public String getWorkstation() {
		return workstation;
	}

	public void setWorkstation(String workstation) {
		this.workstation = workstation;
	}

	public long getWorkstationId() {
		return workstationId;
	}

	public void setWorkstationId(long workstationId) {
		this.workstationId = workstationId;
	}

	public List<HashMap<String, Object>> getOjtRegiList() {
		return ojtRegiList;
	}

	public void setOjtRegiList(List<HashMap<String, Object>> ojtRegiList) {
		this.ojtRegiList = ojtRegiList;
	}

	public int getTrainerId() {
		return trainerId;
	}

	public void setTrainerId(int trainerId) {
		this.trainerId = trainerId;
	}

	public String getTrainer() {
		return trainer;
	}

	public void setTrainer(String trainer) {
		this.trainer = trainer;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public int getEmpId() {
		return empId;
	}

	public void setEmpId(int empId) {
		this.empId = empId;
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

	public int getTrainerEmpId() {
		return trainerEmpId;
	}

	public void setTrainerEmpId(int trainerEmpId) {
		this.trainerEmpId = trainerEmpId;
	}

	public long getCheckSheetId() {
		return checkSheetId;
	}

	public void setCheckSheetId(long checkSheetId) {
		this.checkSheetId = checkSheetId;
	}

	public String getAction() {
		return action;
	}

	public void setAction(String action) {
		this.action = action;
	}

	public long getAuditId() {
		return auditId;
	}

	public void setAuditId(long auditId) {
		this.auditId = auditId;
	}

	public String getActivityDate() {
		return activityDate;
	}

	public void setActivityDate(String activityDate) {
		this.activityDate = activityDate;
	}

	public String getActivity() {
		return activity;
	}

	public void setActivity(String activity) {
		this.activity = activity;
	}

	public long getActivityId() {
		return activityId;
	}

	public void setActivityId(long activityId) {
		this.activityId = activityId;
	}

	public String getCompletedDate() {
		return completedDate;
	}

	public void setCompletedDate(String completedDate) {
		this.completedDate = completedDate;
	}

	public String getActualValue() {
		return actualValue;
	}

	public void setActualValue(String actualValue) {
		this.actualValue = actualValue;
	}

	public String getExpectedValue() {
		return expectedValue;
	}

	public void setExpectedValue(String expectedValue) {
		this.expectedValue = expectedValue;
	}

	public long getCyclePlanId() {
		return cyclePlanId;
	}

	public void setCyclePlanId(long cyclePlanId) {
		this.cyclePlanId = cyclePlanId;
	}

	public String getTrainerStatus() {
		return trainerStatus;
	}

	public void setTrainerStatus(String trainerStatus) {
		this.trainerStatus = trainerStatus;
	}

	public int getCycleNo() {
		return cycleNo;
	}

	public void setCycleNo(int cycleNo) {
		this.cycleNo = cycleNo;
	}

	public long getoJTCyclePlanId() {
		return oJTCyclePlanId;
	}

	public void setoJTCyclePlanId(long oJTCyclePlanId) {
		this.oJTCyclePlanId = oJTCyclePlanId;
	}

	public List<HashMap<String, Object>> getColumn() {
		return column;
	}

	public void setColumn(List<HashMap<String, Object>> column) {
		this.column = column;
	}

	public String getCmpyEmpId() {
		return cmpyEmpId;
	}

	public void setCmpyEmpId(String cmpyEmpId) {
		this.cmpyEmpId = cmpyEmpId;
	}

	public String getCurrentSkillLevel() {
		return currentSkillLevel;
	}

	public void setCurrentSkillLevel(String currentSkillLevel) {
		this.currentSkillLevel = currentSkillLevel;
	}

	public String getDesiredSkillLevel() {
		return desiredSkillLevel;
	}

	public void setDesiredSkillLevel(String desiredSkillLevel) {
		this.desiredSkillLevel = desiredSkillLevel;
	}

	public String getRequireSkillLevel() {
		return requireSkillLevel;
	}

	public void setRequireSkillLevel(String requireSkillLevel) {
		this.requireSkillLevel = requireSkillLevel;
	}

	public long getRequireSkillLevelId() {
		return requireSkillLevelId;
	}

	public void setRequireSkillLevelId(long requireSkillLevelId) {
		this.requireSkillLevelId = requireSkillLevelId;
	}

	public long getChecksheetParameterId() {
		return checksheetParameterId;
	}

	public void setChecksheetParameterId(long checksheetParameterId) {
		this.checksheetParameterId = checksheetParameterId;
	}

	public long getSkillingParameterId() {
		return skillingParameterId;
	}

	public void setSkillingParameterId(long skillingParameterId) {
		this.skillingParameterId = skillingParameterId;
	}

	public String getLineName() {
		return lineName;
	}

	public void setLineName(String lineName) {
		this.lineName = lineName;
	}

	public long getLineId() {
		return lineId;
	}

	public void setLineId(long lineId) {
		this.lineId = lineId;
	}

	public String getAssignedEmpId() {
		return assignedEmpId;
	}

	public void setAssignedEmpId(String assignedEmpId) {
		this.assignedEmpId = assignedEmpId;
	}

	public String getAssignedEmpName() {
		return assignedEmpName;
	}

	public void setAssignedEmpName(String assignedEmpName) {
		this.assignedEmpName = assignedEmpName;
	}

	public List<SMOJTSkillingAuditDTO> getSkillingAuditList() {
		return skillingAuditList;
	}

	public void setSkillingAuditList(List<SMOJTSkillingAuditDTO> skillingAuditList) {
		this.skillingAuditList = skillingAuditList;
	}

	public long getAssessmentId() {
		return assessmentId;
	}

	public void setAssessmentId(long assessmentId) {
		this.assessmentId = assessmentId;
	}

	public long getSkillingId() {
		return skillingId;
	}

	public void setSkillingId(long skillingId) {
		this.skillingId = skillingId;
	}

	public double getTotalMarks() {
		return totalMarks;
	}

	public void setTotalMarks(double totalMarks) {
		this.totalMarks = totalMarks;
	}

	public double getPassingMarks() {
		return passingMarks;
	}

	public void setPassingMarks(double passingMarks) {
		this.passingMarks = passingMarks;
	}

}
