
package com.greentin.enovation.model;

import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

@Entity
@Table(name = "employee_hierarchy")
public class EmployeeHierarchy {
	
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name = "emp_lvl_id")
	private int empLvlId;
	
	@Column(name="level_type")
	private String levelType;
	
	@Column(name ="level_name")
	private String levelName;
	
	@Column(name="branch_id")
	private int branchId;
	
	@Column(name = "created_by")
	private int createdBy;
	
	@Column(name="updated_by")
	private int updatedBy;
	
	@CreationTimestamp
	@Column(updatable = false)
	private Timestamp createdDate;
	
	@UpdateTimestamp
	private Timestamp updatedDate;
	
	@Column(name = "is_active")
	private String isActive;

	public EmployeeHierarchy() {
		super();
	}

	public EmployeeHierarchy(int empLvlId) {
		super();
		this.empLvlId = empLvlId;
	}

	
	
	public EmployeeHierarchy(int empLvlId, String levelType, String levelName) {
		super();
		this.empLvlId = empLvlId;
		this.levelType = levelType;
		this.levelName = levelName;
	}

	public int getEmpLvlId() {
		return empLvlId;
	}

	public void setEmpLvlId(int empLvlId) {
		this.empLvlId = empLvlId;
	}

	public String getLevelType() {
		return levelType;
	}

	public void setLevelType(String levelType) {
		this.levelType = levelType;
	}

	public String getLevelName() {
		return levelName;
	}

	public void setLevelName(String levelName) {
		this.levelName = levelName;
	}

	public int getBranchId() {
		return branchId;
	}

	public void setBranchId(int branchId) {
		this.branchId = branchId;
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

	public String getIsActive() {
		return isActive;
	}

	public void setIsActive(String isActive) {
		this.isActive = isActive;
	}

}
