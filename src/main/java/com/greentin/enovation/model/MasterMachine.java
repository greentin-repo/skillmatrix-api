/**
 * 
 */
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

/**
 * @author Vinay B
 * @date Dec 31, 2019 2:52:25 PM
 */
@Entity
@Table(name = "master_machine")
public class MasterMachine {

	// Members

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;

	@Column(name = "machine_name")
	private String machineName;

	@Column(name = "created_by", updatable = false, nullable = false, columnDefinition = "int default 0")
	private int createdBy;

	@Column(name = "updated_by", nullable = false, columnDefinition = "int default 0")
	private int updatedBy;

	@CreationTimestamp
	@Column(name = "created_date", updatable = false)
	private Timestamp createdDate;

	@UpdateTimestamp
	@Column(name = "updated_date")
	private Timestamp updatedDate;

	@Column(name = "branch_id", updatable = false, nullable = false, columnDefinition = "int default 0")
	private int branchId;

	@Column(name = "is_active", length = 10, columnDefinition = "varchar(20) default 'Y'")
	private String isActive;

	@Column(nullable = false, columnDefinition = "int default 0")
	private long stageId;

	@Column(name = "product_family_id", columnDefinition = "int default 0")
	private long productFamilyId;

	public MasterMachine(long id) {
		super();
		this.id = id;
	}

	public MasterMachine(long id, String machineName) {
		super();
		this.id = id;
		this.machineName = machineName;
	}

	// Setters & Getters

	public long getId() {
		return id;
	}

	public MasterMachine() {
		super();
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getMachineName() {
		return machineName;
	}

	public void setMachineName(String machineName) {
		this.machineName = machineName;
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

	public int getBranchId() {
		return branchId;
	}

	public void setBranchId(int branchId) {
		this.branchId = branchId;
	}

	public String getIsActive() {
		return isActive;
	}

	public void setIsActive(String isActive) {
		this.isActive = isActive;
	}

	public long getStageId() {
		return stageId;
	}

	public void setStageId(long stageId) {
		this.stageId = stageId;
	}

	public long getProductFamilyId() {
		return productFamilyId;
	}

	public void setProductFamilyId(long productFamilyId) {
		this.productFamilyId = productFamilyId;
	}

}
