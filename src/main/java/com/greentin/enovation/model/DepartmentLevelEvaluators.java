package com.greentin.enovation.model;

import java.sql.Timestamp;
import java.util.Date;
import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Transient;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.greentin.enovation.accesscontrol.Role;

@Entity
public class DepartmentLevelEvaluators {

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private int id;

	@ManyToOne()
	@JoinColumn(name="deptId")
	private DepartmentMaster dept;

	@ManyToOne()
	@JoinColumn(name="branch_id")
	private BranchMaster branch;

	@ManyToOne()
	@JoinColumn(name="empId")
	@JsonBackReference
	private EmployeeDetails empDetails;

    @CreationTimestamp
	private Date createdDate;
    
    @UpdateTimestamp
	private Date updatedDate;

	@Transient
	private Timestamp ftime;
	@Transient
	private Timestamp ltime;
	
	
	@Transient
	private int createdBy;
	
	@Transient
	private int updatedBy;
	@Transient
	private int empId;

	@Transient
	private int deptId;

	@Transient
	private int orgId;

	@Transient
	private int branchId;

	@Transient
	private String  emailId;
	@Transient
	private String  firstName;
	@Transient
	private String lastName;


	@Transient
	private Set<Role> roles;

	@Transient
	private int isActive;

	@Transient
	private int isSetupCompleted;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public DepartmentMaster getDept() {
		return dept;
	}

	public void setDept(DepartmentMaster dept) {
		this.dept = dept;
	}

	public BranchMaster getBranch() {
		return branch;
	}

	public void setBranch(BranchMaster branch) {
		this.branch = branch;
	}

	public EmployeeDetails getEmpDetails() {
		return empDetails;
	}

	public void setEmpDetails(EmployeeDetails empDetails) {
		this.empDetails = empDetails;
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

	public Timestamp getFtime() {
		return ftime;
	}

	public void setFtime(Timestamp ftime) {
		this.ftime = ftime;
	}

	public Timestamp getLtime() {
		return ltime;
	}

	public void setLtime(Timestamp ltime) {
		this.ltime = ltime;
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

	public int getEmpId() {
		return empId;
	}

	public void setEmpId(int empId) {
		this.empId = empId;
	}

	public int getDeptId() {
		return deptId;
	}

	public void setDeptId(int deptId) {
		this.deptId = deptId;
	}

	public int getOrgId() {
		return orgId;
	}

	public void setOrgId(int orgId) {
		this.orgId = orgId;
	}

	public int getBranchId() {
		return branchId;
	}

	public void setBranchId(int branchId) {
		this.branchId = branchId;
	}

	public String getEmailId() {
		return emailId;
	}

	public void setEmailId(String emailId) {
		this.emailId = emailId;
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

	public Set<Role> getRoles() {
		return roles;
	}

	public void setRoles(Set<Role> roles) {
		this.roles = roles;
	}

	public int getIsActive() {
		return isActive;
	}

	public void setIsActive(int isActive) {
		this.isActive = isActive;
	}

	public int getIsSetupCompleted() {
		return isSetupCompleted;
	}

	public void setIsSetupCompleted(int isSetupCompleted) {
		this.isSetupCompleted = isSetupCompleted;
	}
	
	
}
