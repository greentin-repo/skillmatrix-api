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
 * @author Rajdeep 12 Aug 2023 ,12:30 AM
 * @desc skill Matrix checksheet parameter Table
 * @comments
 */
@Entity
@Table(name = "sm_checksheet_parameter")
public class SMChecksheetParameter {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private long id;

	@Column(name = "parameter", length = 250)
	private String parameter;

	@ManyToOne
	@JoinColumn(name = "parameter_type_id")
	private SMParameterType parameterType;

	@ManyToOne
	@JoinColumn(name = "checksheet_id")
	private SMChecksheet checksheet;

	@Column(name = "is_active")
	private Boolean isActive;
	
	@Column(name = "cycle_value")
	private int cycleValue;

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

	public SMChecksheetParameter() {
		super();
	}

	public SMChecksheetParameter(long id) {
		super();
		this.id = id;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getParameter() {
		return parameter;
	}

	public void setParameter(String parameter) {
		this.parameter = parameter;
	}

	public SMParameterType getParameterType() {
		return parameterType;
	}

	public void setParameterType(SMParameterType parameterType) {
		this.parameterType = parameterType;
	}

	public SMChecksheet getChecksheet() {
		return checksheet;
	}

	public void setChecksheet(SMChecksheet checksheet) {
		this.checksheet = checksheet;
	}

	public Boolean getIsActive() {
		return isActive;
	}

	public void setIsActive(Boolean isActive) {
		this.isActive = isActive;
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

	public int getCycleValue() {
		return cycleValue;
	}

	public void setCycleValue(int cycleValue) {
		this.cycleValue = cycleValue;
	}

}
