package com.greentin.enovation.model;

import java.sql.Timestamp;
import javax.persistence.Entity;
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
@Table(name="multiple_branch_access")
public class MultiBranchAccess {

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private int id;
	
	@ManyToOne
	@JoinColumn(name="org_id")
	private OrganizationMaster org;
	
	@ManyToOne
	@JoinColumn(name="branch_id")
	private BranchMaster branch;
	
	@ManyToOne
	@JoinColumn(name="emp_id")
	private EmployeeDetails emp;
	
	@Transient
	private int orgId;
	
	@Transient
	private int empId;
	
	@Transient
	private int branchId;
	
	private int isAccess;
	
	@CreationTimestamp
	private Timestamp createdDate;
	
	@UpdateTimestamp
	private Timestamp updatedDate;
	
	@Transient
	private String location;
	
	@Transient
	private String branchName;
	
	@Transient
	private String firstName;
	
	@Transient
	private String lastName;

	@Transient
	private String email;
	
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
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

	public EmployeeDetails getEmp() {
		return emp;
	}

	public void setEmp(EmployeeDetails emp) {
		this.emp = emp;
	}

	public int getOrgId() {
		return orgId;
	}

	public void setOrgId(int orgId) {
		this.orgId = orgId;
	}

	public int getEmpId() {
		return empId;
	}

	public void setEmpId(int empId) {
		this.empId = empId;
	}

	public int getBranchId() {
		return branchId;
	}

	public void setBranchId(int branchId) {
		this.branchId = branchId;
	}


	public int getIsAccess() {
		return isAccess;
	}

	public void setIsAccess(int isAccess) {
		this.isAccess = isAccess;
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

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	public String getBranchName() {
		return branchName;
	}

	public void setBranchName(String branchName) {
		this.branchName = branchName;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}
	
	
	
}
