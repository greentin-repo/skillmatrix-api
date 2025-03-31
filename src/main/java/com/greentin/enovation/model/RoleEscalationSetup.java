package com.greentin.enovation.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.validation.constraints.Email;

import com.greentin.enovation.accesscontrol.Role;

@Entity
//@javax.persistence.Cacheable
public class RoleEscalationSetup {
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private int id;

	private int numberOfDays;

	@ManyToOne
	@JoinColumn(name="role_id")
	private Role role;

	@ManyToOne
	@JoinColumn(name="branch_id")
	private BranchMaster  branchMaster;
	
	private int isDeactive;

	@Email
	private String email;

	private String name;

	private int empId;
	
	

	public RoleEscalationSetup() {
		super();
	}

	public RoleEscalationSetup(Role role, BranchMaster branchMaster) {
		super();
		this.role = role;
		this.branchMaster = branchMaster;
	}
	public RoleEscalationSetup(BranchMaster branchMaster,Role role,int numberOfDays) {
		super();
		this.branchMaster = branchMaster;
		this.role = role;
		this.numberOfDays=numberOfDays;
	}


	public RoleEscalationSetup(int id) {
		super();
		this.id = id;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getNumberOfDays() {
		return numberOfDays;
	}

	public void setNumberOfDays(int numberOfDays) {
		this.numberOfDays = numberOfDays;
	}

	public Role getRole() {
		return role;
	}

	public void setRole(Role role) {
		this.role = role;
	}

	public BranchMaster getBranchMaster() {
		return branchMaster;
	}

	public void setBranchMaster(BranchMaster branchMaster) {
		this.branchMaster = branchMaster;
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

	public int getIsDeactive() {
		return isDeactive;
	}

	public void setIsDeactive(int isDeactive) {
		this.isDeactive = isDeactive;
	}

	public int getEmpId() {
		return empId;
	}

	public void setEmpId(int empId) {
		this.empId = empId;
	}
	
	





}
