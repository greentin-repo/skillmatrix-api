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
 * @author Anant 30 August 2023 ,09:30 AM
 * @desc OJT Cycle Plan Parameter record saving
 * @comments
 */

@Entity
@Table(name = "sm_ojt_cycle_plan_parameter")
public class SMOJTCyclePlanParameter {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private long id;

	@Column(name = "expected_value", length = 250)
	private String expectedValue;
	
	@Column(name = "actual_value", length = 250)
	private String actualValue;
	
	@Column(name = "cycle_no")
	private int cycleNo;

	@ManyToOne
	@JoinColumn(name = "skilling_parameter_id")
	private SMOJTSkillingParameter skillingParameter;
	
	@ManyToOne
	@JoinColumn(name = "skilling_audit_id")
	private SMOJTSkillingAudit skillingAudit;

	@ManyToOne
	@JoinColumn(name = "checksheet_parameter_id")
	private SMChecksheetParameter checksheetParameter;
	
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
	
	public SMOJTCyclePlanParameter() {
		super();
	}
	
	public SMOJTCyclePlanParameter(long id) {
		super();
		this.id=id;
	}


	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getExpectedValue() {
		return expectedValue;
	}

	public void setExpectedValue(String expectedValue) {
		this.expectedValue = expectedValue;
	}

	public String getActualValue() {
		return actualValue;
	}

	public void setActualValue(String actualValue) {
		this.actualValue = actualValue;
	}

	public int getCycleNo() {
		return cycleNo;
	}

	public void setCycleNo(int cycleNo) {
		this.cycleNo = cycleNo;
	}

	public SMOJTSkillingParameter getSkillingParameter() {
		return skillingParameter;
	}

	public void setSkillingParameter(SMOJTSkillingParameter skillingParameter) {
		this.skillingParameter = skillingParameter;
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
	
}
