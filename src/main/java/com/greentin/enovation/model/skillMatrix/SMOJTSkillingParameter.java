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
 * @author Rajdeep 16 Aug 2023 ,12:30 AM
 * @desc skill Matrix OJT parameter record saving
 * @comments
 */

@Entity
@Table(name = "sm_ojt_skilling_parameter")
public class SMOJTSkillingParameter {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private long id;

	@Column(name = "parameter_value", length = 250)
	private String parameterValue;

	@ManyToOne
	@JoinColumn(name = "skilling_audit_id")
	private SMOJTSkillingAudit skillingAudit;

	@ManyToOne
	@JoinColumn(name = "checksheet_parameter_id")
	private SMChecksheetParameter checksheetParameter;

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

	public SMOJTSkillingParameter() {
		super();
	}

	public SMOJTSkillingParameter(long id) {
		super();
		this.id = id;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getParameterValue() {
		return parameterValue;
	}

	public void setParameterValue(String parameterValue) {
		this.parameterValue = parameterValue;
	}

	public SMOJTSkillingAudit getSkillingAudit() {
		return skillingAudit;
	}

	public void setSkillingAudit(SMOJTSkillingAudit skillingAudit) {
		this.skillingAudit = skillingAudit;
	}

	public SMChecksheetParameter getChecksheetParameter() {
		return checksheetParameter;
	}

	public void setChecksheetParameter(SMChecksheetParameter checksheetParameter) {
		this.checksheetParameter = checksheetParameter;
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

}
