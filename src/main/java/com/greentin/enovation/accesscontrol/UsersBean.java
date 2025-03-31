package com.greentin.enovation.accesscontrol;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;
import com.greentin.enovation.beans.EmployeeDetailsBean;
import com.greentin.enovation.model.BranchMaster;
import com.greentin.enovation.model.OrganizationMaster;

public class UsersBean {

	private Long id;

	private String name;

	private String username;

	private String email;

	private int isEmailVerified;

	private int isActive;

	private Date regDate;

	private String contactNumber;

	private String designation;

	private Set<Role> roles = new HashSet<>();

	private OrganizationMaster org;

	private BranchMaster branch;

	private EmployeeDetailsBean empDetails;



	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public int getIsEmailVerified() {
		return isEmailVerified;
	}

	public void setIsEmailVerified(int isEmailVerified) {
		this.isEmailVerified = isEmailVerified;
	}

	public int getIsActive() {
		return isActive;
	}

	public void setIsActive(int isActive) {
		this.isActive = isActive;
	}

	public Date getRegDate() {
		return regDate;
	}

	public void setRegDate(Date regDate) {
		this.regDate = regDate;
	}

	public String getContactNumber() {
		return contactNumber;
	}

	public void setContactNumber(String contactNumber) {
		this.contactNumber = contactNumber;
	}

	public String getDesignation() {
		return designation;
	}

	public void setDesignation(String designation) {
		this.designation = designation;
	}

	public Set<Role> getRoles() {
		return roles;
	}

	public void setRoles(Set<Role> roles) {
		this.roles = roles;
	}

	public OrganizationMaster getOrg() {
		return org;
	}

	public void setOrg(OrganizationMaster org) {
		this.org = org;
	}

	public BranchMaster getBranch() {
		return branch;
	}

	public void setBranch(BranchMaster branch) {
		this.branch = branch;
	}

	public EmployeeDetailsBean getEmpDetails() {
		return empDetails;
	}

	public void setEmpDetails(EmployeeDetailsBean empDetails) {
		this.empDetails = empDetails;
	}
    
}
