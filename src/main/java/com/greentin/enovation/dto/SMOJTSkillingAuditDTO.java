package com.greentin.enovation.dto;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class SMOJTSkillingAuditDTO {

	private long id;

	private long skillingId;

	private long stageId;

	private long empId;

	private String status;

	private long previousAuditId;

	private long skillingChecksheetId;

	private String comment;

	private List<SMOJTCSPointsAuditDTO> auditPointList;

	private List<SMOJTSkillingParameterDTO> parameterList;

	private String completedDate;

	private String assignedDate;

	private long skillingAuditId;

	private long checksheetId;

	private int dayNo;

	private long levelId;

	private int branchId;

	private int oeEmpId;

	private String empName;

	private String stage;

	private int deptId;

	private long ojtRegiId;

	private long lineId;

	private int trainerEmpId;

	private List<SMOJTAssessmentDTO> assessmentList;

	private List<SMOJTSkillingAuditDTO> skillList;

	private long checksheetPointsId;

	private List<SMOJTAssessmentDTO> assessmentsList;

	private int createBy;

	private String assignedToName;

	private String assignedTo;

	private double actualMark;

	private double percentage;

	private double totalMark;

	private String title;

	private long assessmentId;

	private long workstationId;

	private String workstation;

	private String lineName;

	private String deptName;

	private int tlEmpId;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
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

	public long getEmpId() {
		return empId;
	}

	public void setEmpId(long empId) {
		this.empId = empId;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
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

	public List<SMOJTCSPointsAuditDTO> getAuditPointList() {
		return auditPointList;
	}

	public void setAuditPointList(List<SMOJTCSPointsAuditDTO> auditPointList) {
		this.auditPointList = auditPointList;
	}

	public List<SMOJTSkillingParameterDTO> getParameterList() {
		return parameterList;
	}

	public void setParameterList(List<SMOJTSkillingParameterDTO> parameterList) {
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

	public int getDayNo() {
		return dayNo;
	}

	public void setDayNo(int dayNo) {
		this.dayNo = dayNo;
	}

	public long getLevelId() {
		return levelId;
	}

	public void setLevelId(long levelId) {
		this.levelId = levelId;
	}

	public int getBranchId() {
		return branchId;
	}

	public void setBranchId(int branchId) {
		this.branchId = branchId;
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

	public int getDeptId() {
		return deptId;
	}

	public void setDeptId(int deptId) {
		this.deptId = deptId;
	}

	public long getOjtRegiId() {
		return ojtRegiId;
	}

	public void setOjtRegiId(long ojtRegiId) {
		this.ojtRegiId = ojtRegiId;
	}

	public long getLineId() {
		return lineId;
	}

	public void setLineId(long lineId) {
		this.lineId = lineId;
	}

	public int getTrainerEmpId() {
		return trainerEmpId;
	}

	public void setTrainerEmpId(int trainerEmpId) {
		this.trainerEmpId = trainerEmpId;
	}

	public double getActualMark() {
		return actualMark;
	}

	public void setActualMark(double actualMark) {
		this.actualMark = actualMark;
	}

	public double getPercentage() {
		return percentage;
	}

	public void setPercentage(double percentage) {
		this.percentage = percentage;
	}

	public double getTotalMark() {
		return totalMark;
	}

	public void setTotalMark(double totalMark) {
		this.totalMark = totalMark;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public long getAssessmentId() {
		return assessmentId;
	}

	public void setAssessmentId(long assessmentId) {
		this.assessmentId = assessmentId;
	}

	public List<SMOJTAssessmentDTO> getAssessmentList() {
		return assessmentList;
	}

	public void setAssessmentList(List<SMOJTAssessmentDTO> assessmentList) {
		this.assessmentList = assessmentList;
	}

	public List<SMOJTSkillingAuditDTO> getSkillList() {
		return skillList;
	}

	public void setSkillList(List<SMOJTSkillingAuditDTO> skillList) {
		this.skillList = skillList;
	}

	public long getChecksheetPointsId() {
		return checksheetPointsId;
	}

	public void setChecksheetPointsId(long checksheetPointsId) {
		this.checksheetPointsId = checksheetPointsId;
	}

	public int getCreateBy() {
		return createBy;
	}

	public void setCreateBy(int createBy) {
		this.createBy = createBy;
	}

	public String getAssignedToName() {
		return assignedToName;
	}

	public void setAssignedToName(String assignedToName) {
		this.assignedToName = assignedToName;
	}

	public String getAssignedTo() {
		return assignedTo;
	}

	public void setAssignedTo(String assignedTo) {
		this.assignedTo = assignedTo;
	}

	public List<SMOJTAssessmentDTO> getAssessmentsList() {
		return assessmentsList;
	}

	public void setAssessmentsList(List<SMOJTAssessmentDTO> assessmentsList) {
		this.assessmentsList = assessmentsList;
	}

	public long getWorkstationId() {
		return workstationId;
	}

	public void setWorkstationId(long workstationId) {
		this.workstationId = workstationId;
	}

	public String getWorkstation() {
		return workstation;
	}

	public void setWorkstation(String workstation) {
		this.workstation = workstation;
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

	public int getTlEmpId() {
		return tlEmpId;
	}

	public void setTlEmpId(int tlEmpId) {
		this.tlEmpId = tlEmpId;
	}

}
