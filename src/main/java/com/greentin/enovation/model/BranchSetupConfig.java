package com.greentin.enovation.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonBackReference;

@Entity
@Table(name="branch_setup_config")
//@javax.persistence.Cacheable
public class BranchSetupConfig {

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="id" ,unique=true, nullable=false)
	private int id;
	private int isSetupCompleted;
	private int isMandatory;
	
	@ManyToOne
	@JoinColumn(name="branchId")
	@JsonBackReference
	private BranchMaster branchMaster;
	
	
	@ManyToOne
	@JoinColumn(name="setupId")
	private SetupMaster setupMaster;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getIsSetupCompleted() {
		return isSetupCompleted;
	}

	public void setIsSetupCompleted(int isSetupCompleted) {
		this.isSetupCompleted = isSetupCompleted;
	}

	public int getIsMandatory() {
		return isMandatory;
	}

	public void setIsMandatory(int isMandatory) {
		this.isMandatory = isMandatory;
	}

	
	public BranchMaster getBranchMaster() {
		return branchMaster;
	}

	public void setBranchMaster(BranchMaster branchMaster) {
		this.branchMaster = branchMaster;
	}

	public SetupMaster getSetupMaster() {
		return setupMaster;
	}

	public void setSetupMaster(SetupMaster setupMaster) {
		this.setupMaster = setupMaster;
	}
	
	
	
}
