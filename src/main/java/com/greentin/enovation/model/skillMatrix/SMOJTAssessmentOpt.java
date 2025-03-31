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
 * @author Rajdeep 07 Aug 2023 ,02:25 PM
 * @desc
 * @comments
 */

@Entity
@Table(name = "sm_ojt_assessment_opt")
public class SMOJTAssessmentOpt {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private long id;

	@ManyToOne
	@JoinColumn(name = "ojt_ass_ques_Id")
	private SMOJTAssessmentQues ojtAssessmentQues;

	@ManyToOne
	@JoinColumn(name = "ques_id")
	private SMAssessmentQues ques;

	@ManyToOne
	@JoinColumn(name = "opt_id")
	private SMAssessmentOptions opt;

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

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public SMAssessmentQues getQues() {
		return ques;
	}

	public void setQues(SMAssessmentQues ques) {
		this.ques = ques;
	}

	public SMAssessmentOptions getOpt() {
		return opt;
	}

	public void setOpt(SMAssessmentOptions opt) {
		this.opt = opt;
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

	public SMOJTAssessmentQues getOjtAssessmentQues() {
		return ojtAssessmentQues;
	}

	public void setOjtAssessmentQues(SMOJTAssessmentQues ojtAssessmentQues) {
		this.ojtAssessmentQues = ojtAssessmentQues;
	}

}
