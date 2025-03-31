package com.greentin.enovation.model;

import java.sql.Date;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.Lob;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.greentin.enovation.accesscontrol.Role;
import com.greentin.enovation.config.EnovationConfig;
import com.greentin.enovation.model.dwm.Line;

@Entity
@Table(name = "tbl_employee_details")
// @javax.persistence.Cacheable
@Component
public class EmployeeDetails {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int empId;

	private String firstName;

	private String lastName;

	private String contactNo;

	private String address;

	private String emailId;

	private String password;

	private int loggedIn;

	private Integer createdBy;

	private int points;

	private String profilePic;

	private String fcmKey;

	private String apnKey;

	private String cmpyEmpId;

	private Timestamp lastActiveDate;

	private java.sql.Date DOB;

	private java.sql.Date DOA;

	@Column(name = "inactive_reason")
	private String inactiveReason;

	@Column(name = "inactive_from")
	private String inactiveFrom;

	@Column(name = "source")
	private String source;

	@Column(name = "registration_id", nullable = false, columnDefinition = "int default 0")
	private long registrationId;

	@Transient
	private int setActiveDate;

	@CreationTimestamp
	private Timestamp createdDate;

	@UpdateTimestamp
	private Timestamp updatedDate;

	private int isDeactive;
	
	@Transient
	private int id;// id will contain empId for app

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "line_id")
	private Line line;

	// Added by vinay - 05-01-2020 PMS Requirement

	@Column(name = "doj")
	private String doj; // PMS Requirement

	@Column(name = "emp_type")
	private String empType; // PMS Requirement // CW or EMPLOYEE

	@Column(name = "gender")
	private String gender; // ADVIK Requirement

	@Transient
	private int noOfDaysLeft;

	@Transient
	private int transactionId;

	@Transient
	private String branchName;

	@Transient
	private String deptName;

	@Transient
	private String lineName;

	@Transient
	private String empLevel;

	@Transient
	private long roleId;

	@Transient
	private String roleName;

	private int isEmailVerified;

	@Transient
	private String registrationByPass;

	@Transient
	private int updatedBy;

	@Transient
	private List<Integer> ids = new ArrayList<>();

	private int isPasswordExpired;
	
	@Column(name = "middle_name")
	private String middleName;

	// @Formula("designation")
	public int getNoOfDaysLeft() {
		// System.out.println("inside formula : "+noOfDaysLeft);
		return noOfDaysLeft;
	}

	public void setNoOfDaysLeft(int noOfDaysLeft) {
		this.noOfDaysLeft = noOfDaysLeft;
	}

	private String designation;

	private int isNotify;

	@Lob
	@Column
	private String token;

	@Transient
	private int orgId;

	@Transient
	private String newPassword;

	@Transient
	private int deptId;

	@Transient
	private int branchId;

	@Transient
	private int desigId;

	@Transient
	private String orgName;

	@Transient
	private String orgAlies;

	@Transient
	private MultipartFile excel;

	@Transient
	private int isSetupCompleted;

	@Transient
	private int langId;

	@Transient
	private int createdByEmpId;

	@Transient
	private int isOrgExecutive;

	@ManyToOne()
	@JoinColumn(name = "dept_id")
	private DepartmentMaster dept;

	/*
	 * @ManyToOne()
	 * 
	 * @JoinColumn(name = "desigId") private DesignationMaster designation;
	 */
	@ManyToOne()
	@JoinColumn(name = "org_id")
	private OrganizationMaster organization;

	// one to many relationship with suggestion details
	@OneToMany(mappedBy = "empDetails")
	@JsonManagedReference
	@OrderBy("level ASC")
	private Set<DepartmentLevel> departmentLevel;
	/*
	 * @OneToMany(mappedBy="createdBy",cascade=CascadeType.ALL) private
	 * Set<SurveyDetails> survayDetCreatedBy;
	 * 
	 * @OneToMany(mappedBy="updatedBy",cascade=CascadeType.ALL) private
	 * Set<SurveyDetails> survayDetUpdatedBy;
	 */
	@ManyToOne()
	@JoinColumn(name = "branch_id")
	private BranchMaster branch;

	@ManyToOne
	@JoinColumn(name = "lang_id")
	private LanguageMaster langName;

	@ManyToMany(fetch = FetchType.LAZY)
	@JoinTable(name = "emp_roles", joinColumns = @JoinColumn(name = "emp_id"), inverseJoinColumns = @JoinColumn(name = "role_id"))
	private Set<Role> roles = new HashSet<>();

	@ManyToOne
	@JoinColumn(name = "emp_level_id")
	private EmployeeHierarchy empLevelDetails;

	@Column(name = "deactivation_date")
	private Date deactivationDate;

	@Transient
	private int empLvlId;

	@Transient
	private String empLvlType;

	@Transient
	private String empLvlName;

	@Transient
	private String errorMsg;

	@ManyToOne()
	@JoinColumn(name = "agency_id")
	private CWAgency cwAgency;

	public EmployeeDetails() {
		super();
	}

	public EmployeeDetails(int empId) {
		super();
		this.empId = empId;
	}

	public EmployeeDetails(int empId, String firstName, String lastName, String contactNo, String emailId, Date dOB) {
		super();
		this.empId = empId;
		this.firstName = firstName;
		this.lastName = lastName;
		this.contactNo = contactNo;
		this.emailId = emailId;
		DOB = dOB;
	}

	public EmployeeDetails(String cmpyEmpId) {
		super();
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

	public int getPoints() {
		return points;
	}

	public void setPoints(int points) {
		this.points = points;
	}

	public String getEmailId() {
		return emailId;
	}

	public void setEmailId(String emailId) {
		this.emailId = emailId;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
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

	public Integer getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(Integer createdBy) {
		this.createdBy = createdBy;
	}

	public String getProfilePic() {
		if (profilePic == null) {

			return profilePic;
		} else {
			return EnovationConfig.buddyConfig.get("ProfilePicPathUrl") + profilePic;
		}

	}

	public String getActualProfilePicPath() {

		String path = "";
		if (profilePic != null) {

			path = profilePic;

		}
		return path;
	}

	public void setProfilePic(String profilePic) {
		this.profilePic = profilePic;
	}

	public String getFcmKey() {
		return fcmKey;
	}

	public void setFcmKey(String fcmKey) {
		this.fcmKey = fcmKey;
	}

	public String getApnKey() {
		return apnKey;
	}

	public void setApnKey(String apnKey) {
		this.apnKey = apnKey;
	}

	public String getCmpyEmpId() {
		return cmpyEmpId;
	}

	public void setCmpyEmpId(String cmpyEmpId) {
		this.cmpyEmpId = cmpyEmpId;
	}

	/*
	 * public List<DepartmentLevel> getDepartmentLevel() { return departmentLevel; }
	 * public void setDepartmentLevel(List<DepartmentLevel> departmentLevel) {
	 * this.departmentLevel = departmentLevel; }
	 */
	public int getOrgId() {
		return orgId;
	}

	public void setOrgId(int orgId) {
		this.orgId = orgId;
	}

	public int getDeptId() {
		return deptId;
	}

	public void setDeptId(int deptId) {
		this.deptId = deptId;
	}

	public BranchMaster getBranch() {
		return branch;
	}

	public void setBranch(BranchMaster branch) {
		this.branch = branch;
	}

	public int getBranchId() {
		return branchId;
	}

	public void setBranchId(int branchId) {
		this.branchId = branchId;
	}

	public int getIsDeactive() {
		return isDeactive;
	}

	public void setIsDeactive(int isDeactive) {
		this.isDeactive = isDeactive;
	}

	public String getOrgName() {
		return orgName;
	}

	public void setOrgName(String orgName) {
		this.orgName = orgName;
	}

	public MultipartFile getExcel() {
		return excel;
	}

	public void setExcel(MultipartFile excel) {
		this.excel = excel;
	}

	public Set<DepartmentLevel> getDepartmentLevel() {
		return departmentLevel;
	}

	public void setDepartmentLevel(Set<DepartmentLevel> departmentLevel) {
		this.departmentLevel = departmentLevel;
	}

	public int getDesigId() {
		return desigId;
	}

	public void setDesigId(int desigId) {
		this.desigId = desigId;
	}

	public Set<Role> getRoles() {
		return roles;
	}

	public void setRoles(Set<Role> roles) {
		this.roles = roles;
	}

	public int getIsEmailVerified() {
		return isEmailVerified;
	}

	public void setIsEmailVerified(int isEmailVerified) {
		this.isEmailVerified = isEmailVerified;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public String getOrgAlies() {
		return orgAlies;
	}

	public void setOrgAlies(String orgAlies) {
		this.orgAlies = orgAlies;
	}

	public int getIsSetupCompleted() {
		return isSetupCompleted;
	}

	public void setIsSetupCompleted(int isSetupCompleted) {
		this.isSetupCompleted = isSetupCompleted;
	}

	public String getDesignation() {
		return designation;

	}

	public void setDesignation(String designation) {
		this.designation = designation;
	}

	public int getIsNotify() {
		return isNotify;
	}

	public void setIsNotify(int isNotify) {
		this.isNotify = isNotify;
	}

	public String getNewPassword() {
		return newPassword;
	}

	public void setNewPassword(String newPassword) {
		this.newPassword = newPassword;
	}

	public int getLoggedIn() {
		return loggedIn;
	}

	public void setLoggedIn(int loggedIn) {
		this.loggedIn = loggedIn;
	}

	public int getTransactionId() {
		return transactionId;
	}

	public void setTransactionId(int transactionId) {
		this.transactionId = transactionId;
	}

	public String getBranchName() {
		return branchName;
	}

	public void setBranchName(String branchName) {
		this.branchName = branchName;
	}

	public String getDeptName() {
		return deptName;
	}

	public void setDeptName(String deptName) {
		this.deptName = deptName;
	}

	public long getRoleId() {
		return roleId;
	}

	public void setRoleId(long roleId) {
		this.roleId = roleId;
	}

	public String getRoleName() {
		return roleName;
	}

	public void setRoleName(String roleName) {
		this.roleName = roleName;
	}

	@Transient
	private List<Role> emproles = new ArrayList<>();

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

	public int getLangId() {
		return langId;
	}

	public void setLangId(int langId) {
		this.langId = langId;
	}

	public String getRegistrationByPass() {
		return registrationByPass;
	}

	public void setRegistrationByPass(String registrationByPass) {
		this.registrationByPass = registrationByPass;
	}

	public int getCreatedByEmpId() {
		return createdByEmpId;
	}

	public void setCreatedByEmpId(int createdByEmpId) {
		this.createdByEmpId = createdByEmpId;
	}

	public Timestamp getLastActiveDate() {
		return lastActiveDate;
	}

	public void setLastActiveDate(Timestamp lastActiveDate) {
		this.lastActiveDate = lastActiveDate;
	}

	public int getSetActiveDate() {
		return setActiveDate;
	}

	public void setSetActiveDate(int setActiveDate) {
		this.setActiveDate = setActiveDate;
	}

	public java.sql.Date getDOB() {
		return DOB;
	}

	public void setDOB(java.sql.Date dOB) {
		DOB = dOB;
	}

	public java.sql.Date getDOA() {
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

	public List<Integer> getIds() {
		return ids;
	}

	public void setIds(List<Integer> ids) {
		this.ids = ids;
	}

	public int getIsOrgExecutive() {
		return isOrgExecutive;
	}

	public void setIsOrgExecutive(int isOrgExecutive) {
		this.isOrgExecutive = isOrgExecutive;
	}

	public int getUpdatedBy() {
		return updatedBy;
	}

	public void setUpdatedBy(int updatedBy) {
		this.updatedBy = updatedBy;
	}

	public EmployeeHierarchy getEmpLevelDetails() {
		return empLevelDetails;
	}

	public void setEmpLevelDetails(EmployeeHierarchy empLevelDetails) {
		this.empLevelDetails = empLevelDetails;
	}

	public int getEmpLvlId() {
		return empLvlId;
	}

	public void setEmpLvlId(int empLvlId) {
		this.empLvlId = empLvlId;
	}

	public String getEmpLvlType() {
		return empLvlType;
	}

	public void setEmpLvlType(String empLvlType) {
		this.empLvlType = empLvlType;
	}

	public String getEmpLvlName() {
		return empLvlName;
	}

	public void setEmpLvlName(String empLvlName) {
		this.empLvlName = empLvlName;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getInactiveReason() {
		return inactiveReason;
	}

	public void setInactiveReason(String inactiveReason) {
		this.inactiveReason = inactiveReason;
	}

	public String getSource() {
		return source;
	}

	public void setSource(String source) {
		this.source = source;
	}

	public String getInactiveFrom() {
		return inactiveFrom;
	}

	public void setInactiveFrom(String inactiveFrom) {
		this.inactiveFrom = inactiveFrom;
	}

	public String getErrorMsg() {
		return errorMsg;
	}

	public void setErrorMsg(String errorMsg) {
		this.errorMsg = errorMsg;
	}

	public long getRegistrationId() {
		return registrationId;
	}

	public void setRegistrationId(long registrationId) {
		this.registrationId = registrationId;
	}

	public String getDoj() {
		return doj;
	}

	public void setDoj(String doj) {
		this.doj = doj;
	}

	public String getEmpType() {
		return empType;
	}

	public void setEmpType(String empType) {
		this.empType = empType;
	}

	public String getLineName() {
		return lineName;
	}

	public void setLineName(String lineName) {
		this.lineName = lineName;
	}

	public String getEmpLevel() {
		return empLevel;
	}

	public void setEmpLevel(String empLevel) {
		this.empLevel = empLevel;
	}

	public String getGender() {
		return gender;
	}

	public void setGender(String gender) {
		this.gender = gender;
	}

	public Date getDeactivationDate() {
		return deactivationDate;
	}

	public void setDeactivationDate(Date deactivationDate) {
		this.deactivationDate = deactivationDate;
	}

	public CWAgency getCwAgency() {
		return cwAgency;
	}

	public void setCwAgency(CWAgency cwAgency) {
		this.cwAgency = cwAgency;
	}

	public String getMiddleName() {
		return middleName;
	}

	public void setMiddleName(String middleName) {
		this.middleName = middleName;
	}
	
}
