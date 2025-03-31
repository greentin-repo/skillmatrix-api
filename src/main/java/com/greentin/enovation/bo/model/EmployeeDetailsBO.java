package com.greentin.enovation.bo.model;

import java.sql.Timestamp;
import java.util.HashSet;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.greentin.enovation.accesscontrol.Role;
import com.greentin.enovation.beans.MenuData;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class EmployeeDetailsBO {

	private int empId;

	private String firstName;

	private String lastName;

	private String roleName;

	private String cmpyEmpId;

	private String contactNo;

	private String emailId;

	private Timestamp lastActiveDate;

	private DepartmentMasterBO dept;

	private List<MenuData> empAccess;

	private List<Role> roleList;

	HashSet<MenuData> empAccessList;

	private int onBehalfEmpId;

	private String OnBehalfEmpName;

	private String onBehalfCmpyEmpId;

	private int totalSugg;

	private String empName;

	private int points;

	private BranchMasterBO branch;

	public EmployeeDetailsBO() {

	}

	public EmployeeDetailsBO(int empId) {
		this.empId = empId;
	}

	public int getEmpId() {
		return empId;
	}

	public void setEmpId(int empId) {
		this.empId = empId;
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

	public String getRoleName() {
		return roleName;
	}

	public void setRoleName(String roleName) {
		this.roleName = roleName;
	}

	public String getCmpyEmpId() {
		return cmpyEmpId;
	}

	public void setCmpyEmpId(String cmpyEmpId) {
		this.cmpyEmpId = cmpyEmpId;
	}

	public DepartmentMasterBO getDept() {
		return dept;
	}

	public void setDept(DepartmentMasterBO dept) {
		this.dept = dept;
	}

	public String getContactNo() {
		return contactNo;
	}

	public void setContactNo(String contactNo) {
		this.contactNo = contactNo;
	}

	public String getEmailId() {
		return emailId;
	}

	public void setEmailId(String emailId) {
		this.emailId = emailId;
	}

	public Timestamp getLastActiveDate() {
		return lastActiveDate;
	}

	public void setLastActiveDate(Timestamp lastActiveDate) {
		this.lastActiveDate = lastActiveDate;
	}

	public List<MenuData> getEmpAccess() {
		return empAccess;
	}

	public void setEmpAccess(List<MenuData> empAccess) {
		this.empAccess = empAccess;
	}

	public List<Role> getRoleList() {
		return roleList;
	}

	public void setRoleList(List<Role> roleList) {
		this.roleList = roleList;
	}

	public HashSet<MenuData> getEmpAccessList() {
		return empAccessList;
	}

	public void setEmpAccessList(HashSet<MenuData> empAccessList) {
		this.empAccessList = empAccessList;
	}

	public int getOnBehalfEmpId() {
		return onBehalfEmpId;
	}

	public void setOnBehalfEmpId(int onBehalfEmpId) {
		this.onBehalfEmpId = onBehalfEmpId;
	}

	public String getOnBehalfEmpName() {
		return OnBehalfEmpName;
	}

	public void setOnBehalfEmpName(String onBehalfEmpName) {
		OnBehalfEmpName = onBehalfEmpName;
	}

	public String getOnBehalfCmpyEmpId() {
		return onBehalfCmpyEmpId;
	}

	public void setOnBehalfCmpyEmpId(String onBehalfCmpyEmpId) {
		this.onBehalfCmpyEmpId = onBehalfCmpyEmpId;
	}

	public int getTotalSugg() {
		return totalSugg;
	}

	public void setTotalSugg(int totalSugg) {
		this.totalSugg = totalSugg;
	}

	public String getEmpName() {
		return empName;
	}

	public void setEmpName(String empName) {
		this.empName = empName;
	}

	public int getPoints() {
		return points;
	}

	public void setPoints(int points) {
		this.points = points;
	}

	public BranchMasterBO getBranch() {
		return branch;
	}

	public void setBranch(BranchMasterBO branch) {
		this.branch = branch;
	}

}
