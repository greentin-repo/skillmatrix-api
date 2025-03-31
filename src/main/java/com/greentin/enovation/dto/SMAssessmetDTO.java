package com.greentin.enovation.dto;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

/**
 * @author Anant K August 31, 2023
 * @desc SMAssessmetDTO
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class SMAssessmetDTO {

	private long ojtRegisId;

	private long workflowId;

	private long stageId;

	private long skillLevelId;

	private long skillingId;

	private int empId;

	private long skillingAuditId;

	private long pointAuditId;

	private long questionId;

	private long optionId;

	private List<SMAssessmetDTO> queOptionList;

	private String answer;

	private long ojtAssessmentId;

	private double passingMarks;

	private double percentage;

	private long ojtAssesmentId;

	private long certificateId;

	private long desiredSkillLevelId;

	private long workstationId;

	private String path;
	
	private String assessmentStartDate;
	

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

	public long getPointAuditId() {
		return pointAuditId;
	}

	public void setPointAuditId(long pointAuditId) {
		this.pointAuditId = pointAuditId;
	}

	public long getQuestionId() {
		return questionId;
	}

	public void setQuestionId(long questionId) {
		this.questionId = questionId;
	}

	public long getOptionId() {
		return optionId;
	}

	public void setOptionId(long optionId) {
		this.optionId = optionId;
	}

	public List<SMAssessmetDTO> getQueOptionList() {
		return queOptionList;
	}

	public void setQueOptionList(List<SMAssessmetDTO> queOptionList) {
		this.queOptionList = queOptionList;
	}

	public String getAnswer() {
		return answer;
	}

	public void setAnswer(String answer) {
		this.answer = answer;
	}

	public long getOjtAssessmentId() {
		return ojtAssessmentId;
	}

	public void setOjtAssessmentId(long ojtAssessmentId) {
		this.ojtAssessmentId = ojtAssessmentId;
	}

	public double getPassingMarks() {
		return passingMarks;
	}

	public void setPassingMarks(double passingMarks) {
		this.passingMarks = passingMarks;
	}

	public double getPercentage() {
		return percentage;
	}

	public void setPercentage(double percentage) {
		this.percentage = percentage;
	}

	public long getOjtAssesmentId() {
		return ojtAssesmentId;
	}

	public void setOjtAssesmentId(long ojtAssesmentId) {
		this.ojtAssesmentId = ojtAssesmentId;
	}

	public long getCertificateId() {
		return certificateId;
	}

	public void setCertificateId(long certificateId) {
		this.certificateId = certificateId;
	}

	public long getDesiredSkillLevelId() {
		return desiredSkillLevelId;
	}

	public void setDesiredSkillLevelId(long desiredSkillLevelId) {
		this.desiredSkillLevelId = desiredSkillLevelId;
	}

	public long getWorkstationId() {
		return workstationId;
	}

	public void setWorkstationId(long workstationId) {
		this.workstationId = workstationId;
	}

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	public String getAssessmentStartDate() {
		return assessmentStartDate;
	}

	public void setAssessmentStartDate(String assessmentStartDate) {
		this.assessmentStartDate = assessmentStartDate;
	}

}
