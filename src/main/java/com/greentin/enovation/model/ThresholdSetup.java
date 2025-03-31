package com.greentin.enovation.model;

import java.sql.Timestamp;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

@Entity
@Table(name="threshold_setup")
public class ThresholdSetup {

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private int id;
		
	private long thresholdValue;
	
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="branch_id")
	private BranchMaster branch;
	
	@ManyToOne
	@JoinColumn(name="threshold_id")
	private ThresholdMaster threshold;
	
	
	@CreationTimestamp
	private Timestamp createdDate;
	
	@UpdateTimestamp
	private Timestamp updatedDate;
	
	@Transient
	private String thresholdKey;
	
	@Transient
	private int branchId;
	
	private int deptId;
	
	@Transient
	private String deptName;
	
	@Transient
	private int thresholdId;
	

	
	public ThresholdSetup() {
		super();
	}
	
	
	public ThresholdSetup(int id) {
		super();
		this.id = id;
	}


	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public long getThresholdValue() {
		return thresholdValue;
	}

	public void setThresholdValue(long thresholdValue) {
		this.thresholdValue = thresholdValue;
	}


	public BranchMaster getBranch() {
		return branch;
	}

	public void setBranch(BranchMaster branch) {
		this.branch = branch;
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

	public int getBranchId() {
		return branchId;
	}

	public void setBranchId(int branchId) {
		this.branchId = branchId;
	}
	
	public int getDeptId() {
		return deptId;
	}

	public void setDeptId(int deptId) {
		this.deptId = deptId;
	}

	public ThresholdMaster getThreshold() {
		return threshold;
	}

	public void setThreshold(ThresholdMaster threshold) {
		this.threshold = threshold;
	}

	public String getThresholdKey() {
		return thresholdKey;
	}

	public void setThresholdKey(String thresholdKey) {
		this.thresholdKey = thresholdKey;
	}

	public String getDeptName() {
		return deptName;
	}
	
	public void setDeptName(String deptName) {
		this.deptName = deptName;
	}


	public int getThresholdId() {
		return thresholdId;
	}


	public void setThresholdId(int thresholdId) {
		this.thresholdId = thresholdId;
	}
	
	
	
}
