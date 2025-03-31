package com.greentin.enovation.dto;

import java.util.List;

public class SMOJTAssessmentDTO {

	private long ojtAssId;

	private Double actualMarks;

	private String assessmentStatus;

	private double percentage;

	private Double totalMarks;

	private long skillingId;

	private long ojtRegisId;

	private long assessmentId;

	private long skillingAuditId;
	
	private String assignedToName;


	public long getOjtAssId() {
		return ojtAssId;
	}

	public void setOjtAssId(long ojtAssId) {
		this.ojtAssId = ojtAssId;
	}

	public String getAssessmentStatus() {
		return assessmentStatus;
	}

	public void setAssessmentStatus(String assessmentStatus) {
		this.assessmentStatus = assessmentStatus;
	}

	public double getPercentage() {
		return percentage;
	}

	public void setPercentage(double percentage) {
		this.percentage = percentage;
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

	public long getAssessmentId() {
		return assessmentId;
	}

	public void setAssessmentId(long assessmentId) {
		this.assessmentId = assessmentId;
	}

	public long getSkillingAuditId() {
		return skillingAuditId;
	}

	public void setSkillingAuditId(long skillingAuditId) {
		this.skillingAuditId = skillingAuditId;
	}

	public Double getActualMarks() {
		return actualMarks;
	}

	public void setActualMarks(Double actualMarks) {
		this.actualMarks = actualMarks;
	}

	public Double getTotalMarks() {
		return totalMarks;
	}

	public void setTotalMarks(Double totalMarks) {
		this.totalMarks = totalMarks;
	}

	public String getAssignedToName() {
		return assignedToName;
	}

	public void setAssignedToName(String assignedToName) {
		this.assignedToName = assignedToName;
	}
	
}
