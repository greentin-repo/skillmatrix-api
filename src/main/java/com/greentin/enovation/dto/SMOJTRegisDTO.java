package com.greentin.enovation.dto;

import java.util.HashMap;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import com.greentin.enovation.model.EmployeeDetails;
import com.greentin.enovation.model.skillMatrix.SMSkillLevel;
import com.greentin.enovation.model.skillMatrix.SMWorkstations;

public class SMOJTRegisDTO {

	private long id;

	private int empId;

	private long currentSkillLevelId;

	private long desiredSkillLevelId;

	private String status;

	private int trainerEmpId;

	private long workstationId;

	private int updatedBy;

	private int createdBy;

	private long checkSheetId;

	private int branchId;

	private int deptId;

	private long ojtPlanId;

	private int lineId;

	private String oeEmpName;

	private String workstation;

	private String trainerName;

	private String lineName;

	private String currentSkillLevelName;

	private String desiredSkillLevelName;

	private String action;

	private int dayNo;

	private List<SMOJTRegisDTO> auditPointList;

	private long skillingId;

	private long stageId;

	private long previousAuditId;

	private long skillingChecksheetId;

	private String comment;

	private List<SMOJTRegisDTO> parameterList;

	private String completedDate;

	private String assignedDate;

	private long skillingAuditId;

	private long checksheetId;

	private long levelId;

	private int oeEmpId;

	private String empName;

	private String stage;

	private long ojtRegiId;

	private String actionStatus;

	private long ojtPointId;

	private long ojtChecksheetPointId;

	private String reference;

	private String oestatus;

	private String trstatus;

	private List<HashMap<Object, List<SMOJTRegisDTO>>> dayWiseAuditList;

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

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public int getTrainerEmpId() {
		return trainerEmpId;
	}

	public void setTrainerEmpId(int trainerEmpId) {
		this.trainerEmpId = trainerEmpId;
	}

	public long getWorkstationId() {
		return workstationId;
	}

	public void setWorkstationId(long workstationId) {
		this.workstationId = workstationId;
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

	public long getOjtPlanId() {
		return ojtPlanId;
	}

	public void setOjtPlanId(long ojtPlanId) {
		this.ojtPlanId = ojtPlanId;
	}

	public int getLineId() {
		return lineId;
	}

	public void setLineId(int lineId) {
		this.lineId = lineId;
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

	public String getTrainerName() {
		return trainerName;
	}

	public void setTrainerName(String trainerName) {
		this.trainerName = trainerName;
	}

	public String getLineName() {
		return lineName;
	}

	public void setLineName(String lineName) {
		this.lineName = lineName;
	}

	public String getCurrentSkillLevelName() {
		return currentSkillLevelName;
	}

	public void setCurrentSkillLevelName(String currentSkillLevelName) {
		this.currentSkillLevelName = currentSkillLevelName;
	}

	public String getDesiredSkillLevelName() {
		return desiredSkillLevelName;
	}

	public void setDesiredSkillLevelName(String desiredSkillLevelName) {
		this.desiredSkillLevelName = desiredSkillLevelName;
	}

	public int getDayNo() {
		return dayNo;
	}

	public void setDayNo(int dayNo) {
		this.dayNo = dayNo;
	}

	public List<SMOJTRegisDTO> getAuditPointList() {
		return auditPointList;
	}

	public void setAuditPointList(List<SMOJTRegisDTO> auditPointList) {
		this.auditPointList = auditPointList;
	}

	public long getSkillingId() {
		return skillingId;
	}

	public void setSkillingId(long skillingId) {
		this.skillingId = skillingId;
	}

	public long getStageId() {
		return stageId;
	}

	public void setStageId(long stageId) {
		this.stageId = stageId;
	}

	public long getPreviousAuditId() {
		return previousAuditId;
	}

	public void setPreviousAuditId(long previousAuditId) {
		this.previousAuditId = previousAuditId;
	}

	public long getSkillingChecksheetId() {
		return skillingChecksheetId;
	}

	public void setSkillingChecksheetId(long skillingChecksheetId) {
		this.skillingChecksheetId = skillingChecksheetId;
	}

	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

	public List<SMOJTRegisDTO> getParameterList() {
		return parameterList;
	}

	public void setParameterList(List<SMOJTRegisDTO> parameterList) {
		this.parameterList = parameterList;
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

	public long getSkillingAuditId() {
		return skillingAuditId;
	}

	public void setSkillingAuditId(long skillingAuditId) {
		this.skillingAuditId = skillingAuditId;
	}

	public long getChecksheetId() {
		return checksheetId;
	}

	public void setChecksheetId(long checksheetId) {
		this.checksheetId = checksheetId;
	}

	public long getLevelId() {
		return levelId;
	}

	public void setLevelId(long levelId) {
		this.levelId = levelId;
	}

	public int getOeEmpId() {
		return oeEmpId;
	}

	public void setOeEmpId(int oeEmpId) {
		this.oeEmpId = oeEmpId;
	}

	public String getEmpName() {
		return empName;
	}

	public void setEmpName(String empName) {
		this.empName = empName;
	}

	public String getStage() {
		return stage;
	}

	public void setStage(String stage) {
		this.stage = stage;
	}

	public long getOjtRegiId() {
		return ojtRegiId;
	}

	public void setOjtRegiId(long ojtRegiId) {
		this.ojtRegiId = ojtRegiId;
	}

	public String getActionStatus() {
		return actionStatus;
	}

	public void setActionStatus(String actionStatus) {
		this.actionStatus = actionStatus;
	}

	public long getOjtPointId() {
		return ojtPointId;
	}

	public void setOjtPointId(long ojtPointId) {
		this.ojtPointId = ojtPointId;
	}

	public long getOjtChecksheetPointId() {
		return ojtChecksheetPointId;
	}

	public void setOjtChecksheetPointId(long ojtChecksheetPointId) {
		this.ojtChecksheetPointId = ojtChecksheetPointId;
	}

	public String getReference() {
		return reference;
	}

	public void setReference(String reference) {
		this.reference = reference;
	}

	public String getOestatus() {
		return oestatus;
	}

	public void setOestatus(String oestatus) {
		this.oestatus = oestatus;
	}

	public String getTrstatus() {
		return trstatus;
	}

	public void setTrstatus(String trstatus) {
		this.trstatus = trstatus;
	}

	public List<HashMap<Object, List<SMOJTRegisDTO>>> getDayWiseAuditList() {
		return dayWiseAuditList;
	}

	public void setDayWiseAuditList(List<HashMap<Object, List<SMOJTRegisDTO>>> dayWiseAuditList) {
		this.dayWiseAuditList = dayWiseAuditList;
	}

}
