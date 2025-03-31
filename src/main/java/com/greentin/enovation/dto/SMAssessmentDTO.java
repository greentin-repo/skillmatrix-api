package com.greentin.enovation.dto;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

/**
 * @author Rajdeep MD August 30, 2023
 * @desc SkillingChecksheet
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)

public class SMAssessmentDTO {

	private long assessmentId;

	private long oJTAssessmentId;

	private String levelName;

	private int empId;

	private double actualMarks;

	private double percentage;

	private double totalMark;

	private int branchId;

	private int deptId;

	private String fromDt;

	private String toDt;

	private int updatedBy;

	private int createdBy;

	private String createdDate;

	private String updatedDate;

	private long skillingId;

	private long ojtRegisId;

	private long skillingAuditId;

	private long workstationId;

	private String status;

	private long stageId;

	private double passingMark;

	private long quetionId;

	private long assessmentQueOptId;

	private long questionTypeId;

	private String question;

	private double questionMark;

	private String option;

	private String branchName;

	private double totalMarks;

	private boolean isRightAns;

	private String skillLevel;

	private int time;

	private String title;

	private long skillLvlId;

	private long categoryId;

	private String categoryName;

	private List<SMAssessmentDTO> optList;

	private List<SMAssessmentDTO> quesList;

	private boolean isSelected;

	private String assessmentStatus;

	private String workstation;

	private String lineName;

	private long lineId;

	private int tlEmpId;

	private String deptName;

	private String assessmentType;
	
	private int totalAssessedTime;

	public long getAssessmentId() {
		return assessmentId;
	}

	public void setAssessmentId(long assessmentId) {
		this.assessmentId = assessmentId;
	}

	public String getLevelName() {
		return levelName;
	}

	public void setLevelName(String levelName) {
		this.levelName = levelName;
	}

	public int getEmpId() {
		return empId;
	}

	public void setEmpId(int empId) {
		this.empId = empId;
	}

	public double getActualMarks() {
		return actualMarks;
	}

	public void setActualMarks(double actualMarks) {
		this.actualMarks = actualMarks;
	}

	public double getPercentage() {
		return percentage;
	}

	public void setPercentage(double percentage) {
		this.percentage = percentage;
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

	public long getSkillingId() {
		return skillingId;
	}

	public void setSkillingId(long skillingId) {
		this.skillingId = skillingId;
	}

	public long getOjtRegisId() {
		return ojtRegisId;
	}

	public void setOjtRegisId(long ojtRegisId) {
		this.ojtRegisId = ojtRegisId;
	}

	public long getSkillingAuditId() {
		return skillingAuditId;
	}

	public void setSkillingAuditId(long skillingAuditId) {
		this.skillingAuditId = skillingAuditId;
	}

	public long getWorkstationId() {
		return workstationId;
	}

	public void setWorkstationId(long workstationId) {
		this.workstationId = workstationId;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public long getStageId() {
		return stageId;
	}

	public void setStageId(long stageId) {
		this.stageId = stageId;
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

	public double getTotalMark() {
		return totalMark;
	}

	public void setTotalMark(double totalMark) {
		this.totalMark = totalMark;
	}

	public double getPassingMark() {
		return passingMark;
	}

	public void setPassingMark(double passingMark) {
		this.passingMark = passingMark;
	}

	public long getQuetionId() {
		return quetionId;
	}

	public void setQuetionId(long quetionId) {
		this.quetionId = quetionId;
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

	public double getQuestionMark() {
		return questionMark;
	}

	public void setQuestionMark(double questionMark) {
		this.questionMark = questionMark;
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

	public int getTime() {
		return time;
	}

	public void setTime(int time) {
		this.time = time;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public long getSkillLvlId() {
		return skillLvlId;
	}

	public void setSkillLvlId(long skillLvlId) {
		this.skillLvlId = skillLvlId;
	}

	public List<SMAssessmentDTO> getOptList() {
		return optList;
	}

	public void setOptList(List<SMAssessmentDTO> optList) {
		this.optList = optList;
	}

	public List<SMAssessmentDTO> getQuesList() {
		return quesList;
	}

	public void setQuesList(List<SMAssessmentDTO> quesList) {
		this.quesList = quesList;
	}

	public long getoJTAssessmentId() {
		return oJTAssessmentId;
	}

	public void setoJTAssessmentId(long oJTAssessmentId) {
		this.oJTAssessmentId = oJTAssessmentId;
	}

	public boolean isSelected() {
		return isSelected;
	}

	public void setSelected(boolean isSelected) {
		this.isSelected = isSelected;
	}

	public String getAssessmentStatus() {
		return assessmentStatus;
	}

	public void setAssessmentStatus(String assessmentStatus) {
		this.assessmentStatus = assessmentStatus;
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

	public long getLineId() {
		return lineId;
	}

	public void setLineId(long lineId) {
		this.lineId = lineId;
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

	public String getAssessmentType() {
		return assessmentType;
	}

	public void setAssessmentType(String assessmentType) {
		this.assessmentType = assessmentType;
	}

	public int getTotalAssessedTime() {
		return totalAssessedTime;
	}

	public void setTotalAssessedTime(int totalAssessedTime) {
		this.totalAssessedTime = totalAssessedTime;
	}

}
