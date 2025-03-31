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
 * @author Rajdeep 07 Aug 2023 ,12:30 AM
 * @desc
 * @comments
 */

@Entity
@Table(name = "sm_assessment_ques")
public class SMAssessmentQues {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private long id;

	@ManyToOne
	@JoinColumn(name = "assessment_id")
	private SMAssessment assessment;

	@ManyToOne
	@JoinColumn(name = "ques_type_id")
	private SMQuestionType quesType;

	@ManyToOne
	@JoinColumn(name = "category_id")
	private SMCategory smcategory;

	@Column(name = "qestion", length = 500)
	private String qestion;

	@Column(name = "is_active")
	private Boolean isActive;

	@Column(name = "que_marks", columnDefinition = "double(16,2) default 0.00")
	private double queMarks;

	@Column(name = "created_by", columnDefinition = "int default 0", updatable = false)
	private int createdBy;

	@Column(name = "updated_by", columnDefinition = "int default 0")
	private int updatedBy;

	@CreationTimestamp
	@Column(name = "created_date", columnDefinition = "datetime default now()", updatable = false)
	private Timestamp createdDate;

	@UpdateTimestamp
	@Column(name = "updated_date")
	private Timestamp updatedDate;

	public SMAssessmentQues() {
		super();
	}

	public SMAssessmentQues(long id) {
		super();
		this.id = id;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public SMAssessment getAssessment() {
		return assessment;
	}

	public void setAssessment(SMAssessment assessment) {
		this.assessment = assessment;
	}

	public SMQuestionType getQuesType() {
		return quesType;
	}

	public void setQuesType(SMQuestionType quesType) {
		this.quesType = quesType;
	}

	public String getQestion() {
		return qestion;
	}

	public void setQestion(String qestion) {
		this.qestion = qestion;
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

	public Boolean getIsActive() {
		return isActive;
	}

	public void setIsActive(Boolean isActive) {
		this.isActive = isActive;
	}

	public double getQueMarks() {
		return queMarks;
	}

	public void setQueMarks(double queMarks) {
		this.queMarks = queMarks;
	}

	public SMCategory getSmcategory() {
		return smcategory;
	}

	public void setSmcategory(SMCategory smcategory) {
		this.smcategory = smcategory;
	}

}
