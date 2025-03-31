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
 * @author Rajdeep MD ,07 Aug 2023 ,02:45 PM
 * @desc
 * @comments
 */

@Entity
@Table(name = "sm_ojt_skilling")
public class SMOJTSkilling {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private long id;

	@Column(name = "assessment_due_date")
	private Timestamp assessmentDueDate;

	@ManyToOne
	@JoinColumn(name = "ojt_regis_id")
	private SMOJTRegis ojtRegis;

	@ManyToOne
	@JoinColumn(name = "checksheet_id")
	private SMChecksheet checksheet;

	@ManyToOne
	@JoinColumn(name = "assessment_id")
	private SMAssessment assessment;

	@Column(name = "status", length = 250)
	private String status;

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

	public SMOJTSkilling() {
		super();
	}

	public SMOJTSkilling(long id) {
		super();
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

	public SMChecksheet getChecksheet() {
		return checksheet;
	}

	public void setChecksheet(SMChecksheet checksheet) {
		this.checksheet = checksheet;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
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

	public Timestamp getAssessmentDueDate() {
		return assessmentDueDate;
	}

	public void setAssessmentDueDate(Timestamp assessmentDueDate) {
		this.assessmentDueDate = assessmentDueDate;
	}

	public SMAssessment getAssessment() {
		return assessment;
	}

	public void setAssessment(SMAssessment assessment) {
		this.assessment = assessment;
	}

}
