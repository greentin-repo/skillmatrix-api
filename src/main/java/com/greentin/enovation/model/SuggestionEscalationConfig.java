package com.greentin.enovation.model;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.validation.constraints.Email;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
@Entity
//@javax.persistence.Cacheable
public class SuggestionEscalationConfig {
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private int id;
	
	@ManyToOne
	@JoinColumn(name="branch_id")
	private BranchMaster  branchMaster;

	private int numberOfSuggestions;

	private int isDeactive;

	@Email
	private String email;

	private String name;

	@CreationTimestamp
	private Date createdDate;

	@UpdateTimestamp
	private Date updatedDate;

	private int empId;


	public SuggestionEscalationConfig() {
		super();
	}

	public SuggestionEscalationConfig(BranchMaster branchMaster) {
		super();
		this.branchMaster = branchMaster;

	}
	
	

	public SuggestionEscalationConfig(BranchMaster branchMaster, int numberOfSuggestions) {
		super();
		this.branchMaster = branchMaster;
		this.numberOfSuggestions = numberOfSuggestions;
	}

	
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public BranchMaster getBranchMaster() {
		return branchMaster;
	}

	public void setBranchMaster(BranchMaster branchMaster) {
		this.branchMaster = branchMaster;
	}

	public int getNumberOfSuggestions() {
		return numberOfSuggestions;
	}

	public void setNumberOfSuggestions(int numberOfSuggestions) {
		this.numberOfSuggestions = numberOfSuggestions;
	}

	public int getIsDeactive() {
		return isDeactive;
	}

	public void setIsDeactive(int isDeactive) {
		this.isDeactive = isDeactive;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Date getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}

	public Date getUpdatedDate() {
		return updatedDate;
	}

	public void setUpdatedDate(Date updatedDate) {
		this.updatedDate = updatedDate;
	}

	public int getEmpId() {
		return empId;
	}

	public void setEmpId(int empId) {
		this.empId = empId;
	}
	
	



}
