package com.greentin.enovation.dto;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

/**
 * @author Anant K August 26, 2023
 * @desc SkillingChecksheet
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class SMOJTSkillingDTO {

	private long ojtRegisId;

	private long workflowId;

	private long stageId;

	private long skillLevelId;

	private long skillingId;

	private String comments;

	private long skillingCheckSheetId;

	private int dayNo;

	private String completedDate;

	private String assignedDate;

	private String keyPoint;

	private String status;

	private String empName;

	private String levelName;

	private String certificatePath;

	private String workstation;
	
	private String oEStatus;
	
	private String parameterValue;
	
	private String actualValue;
	
	private String expectedValue;
	
	private int cycleNo;
	
	private long pointAuditId;

	private long ojtParamId;

	private long cyclePlanId;
	
	private int empId;
	
	private long skillingAuditId;
	
	private SMOJTSkillingAuditDTO auditDetails;
	
	private String trainerStatus;
	
	private long checksheetParameterId;

	public long getOjtRegisId() {
		return ojtRegisId;
	}

	public void setOjtRegisId(long ojtRegisId) {
		this.ojtRegisId = ojtRegisId;
	}

	public long getWorkflowId() {
		return workflowId;
	}

	public void setWorkflowId(long workflowId) {
		this.workflowId = workflowId;
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

	public long getSkillingId() {
		return skillingId;
	}

	public void setSkillingId(long skillingId) {
		this.skillingId = skillingId;
	}

	public String getComments() {
		return comments;
	}

	public void setComments(String comments) {
		this.comments = comments;
	}

	public long getSkillingCheckSheetId() {
		return skillingCheckSheetId;
	}

	public void setSkillingCheckSheetId(long skillingCheckSheetId) {
		this.skillingCheckSheetId = skillingCheckSheetId;
	}

	public int getDayNo() {
		return dayNo;
	}

	public void setDayNo(int dayNo) {
		this.dayNo = dayNo;
	}

	public String getCompletedDate() {
		return completedDate;
	}

	public void setCompletedDate(String completedDate) {
		this.completedDate = completedDate;
	}

	public String getAssignedDate() {
		return assignedDate;
	}

	public void setAssignedDate(String assignedDate) {
		this.assignedDate = assignedDate;
	}

	public String getKeyPoint() {
		return keyPoint;
	}

	public void setKeyPoint(String keyPoint) {
		this.keyPoint = keyPoint;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getEmpName() {
		return empName;
	}

	public void setEmpName(String empName) {
		this.empName = empName;
	}

	public String getLevelName() {
		return levelName;
	}

	public void setLevelName(String levelName) {
		this.levelName = levelName;
	}

	public String getCertificatePath() {
		return certificatePath;
	}

	public void setCertificatePath(String certificatePath) {
		this.certificatePath = certificatePath;
	}

	public String getWorkstation() {
		return workstation;
	}

	public void setWorkstation(String workstation) {
		this.workstation = workstation;
	}

	public String getoEStatus() {
		return oEStatus;
	}

	public void setoEStatus(String oEStatus) {
		this.oEStatus = oEStatus;
	}

	public String getParameterValue() {
		return parameterValue;
	}

	public void setParameterValue(String parameterValue) {
		this.parameterValue = parameterValue;
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

	public int getCycleNo() {
		return cycleNo;
	}

	public void setCycleNo(int cycleNo) {
		this.cycleNo = cycleNo;
	}

	public long getPointAuditId() {
		return pointAuditId;
	}

	public void setPointAuditId(long pointAuditId) {
		this.pointAuditId = pointAuditId;
	}

	public long getOjtParamId() {
		return ojtParamId;
	}

	public void setOjtParamId(long ojtParamId) {
		this.ojtParamId = ojtParamId;
	}

	public long getCyclePlanId() {
		return cyclePlanId;
	}

	public void setCyclePlanId(long cyclePlanId) {
		this.cyclePlanId = cyclePlanId;
	}

	public int getEmpId() {
		return empId;
	}

	public void setEmpId(int empId) {
		this.empId = empId;
	}

	public long getSkillingAuditId() {
		return skillingAuditId;
	}

	public void setSkillingAuditId(long skillingAuditId) {
		this.skillingAuditId = skillingAuditId;
	}

	public long getChecksheetParameterId() {
		return checksheetParameterId;
	}

	public void setChecksheetParameterId(long checksheetParameterId) {
		this.checksheetParameterId = checksheetParameterId;
	}

	public String getTrainerStatus() {
		return trainerStatus;
	}

	public void setTrainerStatus(String trainerStatus) {
		this.trainerStatus = trainerStatus;
	}

	public SMOJTSkillingAuditDTO getAuditDetails() {
		return auditDetails;
	}

	public void setAuditDetails(SMOJTSkillingAuditDTO auditDetails) {
		this.auditDetails = auditDetails;
	}

}
