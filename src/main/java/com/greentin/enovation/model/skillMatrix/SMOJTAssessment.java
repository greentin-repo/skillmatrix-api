package com.greentin.enovation.model.skillMatrix;

import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

/**
 * @author Rajdeep 07 Aug 2023 ,02:20 PM
 * @desc
 * @comments
 */

@Entity
@Table(name = "sm_ojt_assessment")
public class SMOJTAssessment {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private long id;

	@ManyToOne
	@JoinColumn(name = "ojt_regis_id")
	private SMOJTRegis ojtRegis;

	@ManyToOne
	@JoinColumn(name = "skilling_id")
	private SMOJTSkilling Skilling;

	@ManyToOne
	@JoinColumn(name = "skilling_audit_id")
	private SMOJTSkillingAudit skillingAudit;

	@ManyToOne
	@JoinColumn(name = "assessment_id")
	private SMAssessment assessment;

	@Column(name = "total_marks")
	private Double totalMarks;

	@Column(name = "actual_marks", columnDefinition = "double(16,2) default 0.00")
	private double actualMarks;

	@Column(name = "percentage", columnDefinition = "double(16,2) default 0.00")
	private double percentage;

	@Column(name = "assessment_status")
	private String assessmentStatus;

	@CreationTimestamp
	@Column(name = "created_date", columnDefinition = "datetime default now()", updatable = false)
	private Timestamp createdDate;

	@UpdateTimestamp
	@Column(name = "updated_date", columnDefinition = "datetime default now()")
	private Timestamp updatedDate;

	@Column(name = "created_by", columnDefinition = "int default 0", updatable = false)
	private int createdBy;

	@Column(name = "updated_by", columnDefinition = "int default 0")
	private int updatedBy;

	@Column(name = "passing_Marks")
	private double passingMarks;
	
	@Column(name = "total_assessment_time", columnDefinition = "int default 0")
	private long totalAssessmentTime; // Total Minutes for assessment

	@Column(name = "total_assessed_time", columnDefinition = "int default 0")
	private long totalAssessedTime; // Total spend time for assigned assessment

	public SMOJTAssessment() {
		super();
	}

	public SMOJTAssessment(long id) {
		this.id = id;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public SMOJTRegis getOjtRegis() {
		return ojtRegis;
	}

	public void setOjtRegis(SMOJTRegis ojtRegis) {
		this.ojtRegis = ojtRegis;
	}

	public SMOJTSkilling getSkilling() {
		return Skilling;
	}

	public void setSkilling(SMOJTSkilling skilling) {
		Skilling = skilling;
	}

	public SMOJTSkillingAudit getSkillingAudit() {
		return skillingAudit;
	}

	public void setSkillingAudit(SMOJTSkillingAudit skillingAudit) {
		this.skillingAudit = skillingAudit;
	}

	public SMAssessment getAssessment() {
		return assessment;
	}

	public void setAssessment(SMAssessment assessment) {
		this.assessment = assessment;
	}

	public double getTotalMarks() {
		return totalMarks;
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

	public String getAssessmentStatus() {
		return assessmentStatus;
	}

	public void setAssessmentStatus(String assessmentStatus) {
		this.assessmentStatus = assessmentStatus;
	}

	public Timestamp getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(Timestamp createdDate) {
		this.createdDate = createdDate;
	}

	public Timestamp getUpdatedDate() {
		return updatedDate;
	}

	public void setUpdatedDate(Timestamp updatedDate) {
		this.updatedDate = updatedDate;
	}

	public int getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(int createdBy) {
		this.createdBy = createdBy;
	}

	public int getUpdatedBy() {
		return updatedBy;
	}

	public void setUpdatedBy(int updatedBy) {
		this.updatedBy = updatedBy;
	}

	public void setTotalMarks(Double totalMarks) {
		this.totalMarks = totalMarks;
	}

	public double getPassingMarks() {
		return passingMarks;
	}

	public void setPassingMarks(double passingMarks) {
		this.passingMarks = passingMarks;
	}

	public long getTotalAssessmentTime() {
		return totalAssessmentTime;
	}

	public void setTotalAssessmentTime(long totalAssessmentTime) {
		this.totalAssessmentTime = totalAssessmentTime;
	}

	public long getTotalAssessedTime() {
		return totalAssessedTime;
	}

	public void setTotalAssessedTime(long totalAssessedTime) {
		this.totalAssessedTime = totalAssessedTime;
	}

}
