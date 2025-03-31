package com.greentin.enovation.beans;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.Transient;

import com.greentin.enovation.accesscontrol.ProductOrgConfig;
import com.greentin.enovation.accesscontrol.Role;
import com.greentin.enovation.model.BranchMaster;
import com.greentin.enovation.model.DepartmentLevel;
import com.greentin.enovation.model.DepartmentMaster;
import com.greentin.enovation.model.EmployeeHierarchy;
import com.greentin.enovation.model.LanguageMaster;
import com.greentin.enovation.model.MoodIndicator;
import com.greentin.enovation.model.OrganizationMaster;
import com.greentin.enovation.model.TeamtypeMaster;
import com.greentin.enovation.model.dwm.Line;

public class EmployeeDetailsBean {

	private int empId;

	private String cmpyEmpId;

	private String firstName;

	private String lastName;

	private String contactNo;

	private String address;

	private int loggedIn;

	private int isEmailVerified;

	private String emailId;

	private Integer createdBy;

	private int points;

	private int isDeactive;

	private String profilePic;

	private int noOfDaysLeft;
	// private DesignationMaster designation;

	private DepartmentMaster dept;

	private OrganizationMaster organization;

	private BranchMaster branch;

	private BranchMaster branchDetailsNew;;

	private Timestamp createdDate;

	private Timestamp updatedDate;

	private java.sql.Date DOB;

	private java.sql.Date DOA;

	private Line line;

	private String role;

	private int isPasswordExpired;

	private Set<DepartmentLevel> departmentLevel;

	private List<TeamtypeMaster> teamDetails;

	private List<MoodIndicator> moodIndicator;

	private Set<Role> roles = new HashSet<>();

	private ProductOrgConfig productOrgConfigDet;

	@Transient
	private DepartmentLevel addController;

	@Transient
	private DepartmentLevel removeController;

	private String designation;

	@Transient
	private List<Role> emproles = new ArrayList<>();

	private LanguageMaster langName;

	private String doj;

	private String middleName;

	// Vinay B. 23.02.2021 PMS - Ends Here

	private EmployeeHierarchy empLevelDetails;

	public EmployeeDetailsBean() {
		super();
	}

	public EmployeeDetailsBean(int empId) {
		this.empId = empId;
	}

	public String getCmpyEmpId() {
		return cmpyEmpId;
	}

	public void setCmpyEmpId(String cmpyEmpId) {
		this.cmpyEmpId = cmpyEmpId;
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

	public String getContactNo() {
		return contactNo;
	}

	public void setContactNo(String contactNo) {
		this.contactNo = contactNo;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getEmailId() {
		return emailId;
	}

	public void setEmailId(String emailId) {
		this.emailId = emailId;
	}

	public Integer getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(Integer createdBy) {
		this.createdBy = createdBy;
	}

	public int getPoints() {
		return points;
	}

	public void setPoints(int points) {
		this.points = points;
	}

	public String getProfilePic() {
		return profilePic;
	}

	public void setProfilePic(String profilePic) {
		this.profilePic = profilePic;
	}

	public DepartmentMaster getDept() {
		return dept;
	}

	public void setDept(DepartmentMaster dept) {
		this.dept = dept;
	}

	public OrganizationMaster getOrganization() {
		return organization;
	}

	public void setOrganization(OrganizationMaster organization) {
		this.organization = organization;
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

	public List<TeamtypeMaster> getTeamDetails() {
		return teamDetails;
	}

	public void setTeamDetails(List<TeamtypeMaster> teamDetails) {
		this.teamDetails = teamDetails;
	}

	// @OrderBy("isActive DESC")

	public Set<DepartmentLevel> getDepartmentLevel() {
		return departmentLevel;
	}

	public void setDepartmentLevel(Set<DepartmentLevel> departmentLevel) {
		this.departmentLevel = departmentLevel;
	}

	public String getRole() {
		return role;
	}

	public void setRole(String role) {
		this.role = role;
	}

	public BranchMaster getBranch() {
		return branch;
	}

	public void setBranch(BranchMaster branch) {
		this.branch = branch;
	}

	public int getIsDeactive() {
		return isDeactive;
	}

	public void setIsDeactive(int isDeactive) {
		this.isDeactive = isDeactive;
	}

	public Set<Role> getRoles() {
		return roles;
	}

	public void setRoles(Set<Role> roles) {
		this.roles = roles;
	}

	public ProductOrgConfig getProductOrgConfigDet() {
		return productOrgConfigDet;
	}

	public void setProductOrgConfigDet(ProductOrgConfig productOrgConfigDet) {
		this.productOrgConfigDet = productOrgConfigDet;
	}

	public String getDesignation() {
		return designation;
	}

	public void setDesignation(String designation) {
		this.designation = designation;
	}

	public DepartmentLevel getAddController() {
		return addController;
	}

	public void setAddController(DepartmentLevel addController) {
		this.addController = addController;
	}

	public DepartmentLevel getRemoveController() {
		return removeController;
	}

	public void setRemoveController(DepartmentLevel removeController) {
		this.removeController = removeController;
	}

	public int getLoggedIn() {
		return loggedIn;
	}

	public void setLoggedIn(int loggedIn) {
		this.loggedIn = loggedIn;
	}

	public int getIsEmailVerified() {
		return isEmailVerified;
	}

	public void setIsEmailVerified(int isEmailVerified) {
		this.isEmailVerified = isEmailVerified;
	}

	public int getNoOfDaysLeft() {
		return noOfDaysLeft;
	}

	public void setNoOfDaysLeft(int noOfDaysLeft) {
		this.noOfDaysLeft = noOfDaysLeft;
	}

	public List<Role> getEmproles() {
		return emproles;
	}

	public void setEmproles(List<Role> emproles) {
		this.emproles = emproles;
	}

	public LanguageMaster getLangName() {
		return langName;
	}

	public void setLangName(LanguageMaster langName) {
		this.langName = langName;
	}

	public BranchMaster getBranchDetailsNew() {
		return branchDetailsNew;
	}

	public void setBranchDetailsNew(BranchMaster branchDetailsNew) {
		this.branchDetailsNew = branchDetailsNew;
	}

	public List<MoodIndicator> getMoodIndicator() {
		return moodIndicator;
	}

	public void setMoodIndicator(List<MoodIndicator> moodIndicator) {
		this.moodIndicator = moodIndicator;
	}

	public Date getDOB() {
		return DOB;
	}

	public void setDOB(java.sql.Date dOB) {
		DOB = dOB;
	}

	public Date getDOA() {
		return DOA;
	}

	public void setDOA(java.sql.Date dOA) {
		DOA = dOA;
	}

	public Line getLine() {
		return line;
	}

	public void setLine(Line line) {
		this.line = line;
	}

	public int getIsPasswordExpired() {
		return isPasswordExpired;
	}

	public void setIsPasswordExpired(int isPasswordExpired) {
		this.isPasswordExpired = isPasswordExpired;
	}

	public String getDoj() {
		return doj;
	}

	public void setDoj(String doj) {
		this.doj = doj;
	}

	public EmployeeHierarchy getEmpLevelDetails() {
		return empLevelDetails;
	}

	public void setEmpLevelDetails(EmployeeHierarchy empLevelDetails) {
		this.empLevelDetails = empLevelDetails;
	}

	public String getMiddleName() {
		return middleName;
	}

	public void setMiddleName(String middleName) {
		this.middleName = middleName;
	}

}
