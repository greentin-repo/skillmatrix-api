package com.greentin.enovation.model;

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

@Entity
@Table(name = "sugg_multiplication_factor")
public class SuggestionMultiplicationFactor {

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name = "id")
	private long id;
	
	@Column(name = "factor_type", updatable=false)
	private String factorType;
	
	@Column(name = "factor", nullable=false, columnDefinition = "int default 0")
	private int factor;
	
/*	@Column(name = "factor", nullable=false, columnDefinition = "int default 0")
	private double factor;*/
	
	@Column(name = "branch_id", updatable=false, columnDefinition = "int default 0")
	private int branchId;
	
	@Column(name = "org_id", updatable=false, columnDefinition = "int default 0")
	private int orgId;
	
	@Column(name = "created_by", updatable=false, columnDefinition = "int default 0")
	private int createdBy;
	
	@Column(name = "updated_by", columnDefinition = "int default 0")
	private int updatedBy;
	
	@CreationTimestamp
	@Column(name = "created_date", updatable=false)
	private Timestamp createdDate;
	
	@UpdateTimestamp
	@Column(name = "updated_date")
	private Timestamp updatedDate;
	
	@ManyToOne
	@JoinColumn(name = "emp_lvl_id",updatable=false)
	private EmployeeHierarchy empLevel;
	

	public SuggestionMultiplicationFactor() {
		super();
	}

	public SuggestionMultiplicationFactor(long id) {
		super();
		this.id = id;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getFactorType() {
		return factorType;
	}

	public void setFactorType(String factorType) {
		this.factorType = factorType;
	}

	public EmployeeHierarchy getEmpLevel() {
		return empLevel;
	}

	public void setEmpLevel(EmployeeHierarchy empLevel) {
		this.empLevel = empLevel;
	}

	public int getFactor() {
		return factor;
	}

	public void setFactor(int factor) {
		this.factor = factor;
	}

	public int getBranchId() {
		return branchId;
	}

	public void setBranchId(int branchId) {
		this.branchId = branchId;
	}

	public int getOrgId() {
		return orgId;
	}

	public void setOrgId(int orgId) {
		this.orgId = orgId;
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
