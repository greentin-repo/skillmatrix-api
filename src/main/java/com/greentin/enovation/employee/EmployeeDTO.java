package com.greentin.enovation.employee;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.web.multipart.MultipartFile;

import com.greentin.enovation.beans.EmployeeDetailsBean;
import com.greentin.enovation.beans.ImportEmployeeErrorList;
import com.greentin.enovation.config.EnovationConfig;
import com.greentin.enovation.model.EmployeeDetails;
import com.greentin.enovation.model.EmployeeHierarchy;
import com.greentin.enovation.model.FeedbackType;
import com.greentin.enovation.model.MultiBranchAccess;
import com.greentin.enovation.model.NoticeSetup;
import com.greentin.enovation.model.PasswordPolicy;

public class EmployeeDTO {

	private String status;
	private boolean result;
	private Integer statusCode;
	private String reason;
	private String email;
	private String empProfilePic;
	private String portalLink;
	private String token;
	private List<String> emailIdExist;
	private Set<String> deptExist;
	private EmployeeDetailsBean empDetails;
	private List<EmployeeDetailsBean> allEmpDetails;
	private List<EmployeeDetails> employeeList;
	private List<ImportEmployeeErrorList> errorList;
	private Set<EmployeeDetailsBean> abc;
	private List<FeedbackType> feedBackTypeList;
	private List<MultiBranchAccess> multiBranchList;
	private List<NoticeSetup> noticeList;
	private PasswordPolicy passwordPolicy;
	private String password;
	private List<EmployeeDetails> list;
	private int roleId;
	private List<Integer> ids;
	private List<Integer> branchIds = new ArrayList<>();
	private int orgId;
	private String srNumber;
	private List<EmployeeHierarchy> empLevelDetails;
	private String userCred;
	private int empId;
	private String name;
	private int branchId;
	private String contactNo;
	private String emailId;
	private String cmpyEmpId;
	private String superAdminName;
	private String superAdminEmailId;
	private String type;
	private int createdBy;
	private String createdByName;
	private MultipartFile uploadEmployeeFile;
	Map<String, List<ImportEmployeeErrorList>> errorlist1;
	private int isSetupCompleted;
	private String registrationByPass;
	private String isTrialExtended;
	private long noOfDaysExtended;
	private MultipartFile[] excelUpload;
	private boolean errorInSheet;
	private List<EmployeeDetails> empList;
	private String firstName;
	private String lastName;
	private String designation;
	private String department;
	private StringBuilder message;
	private List<EmployeeDTO> errorLists;
	private EmployeeDTO empData;
	private int isDeactive;
	private List<EmployeeDetails> empRoles;
	private java.sql.Date DOB;
	private java.sql.Date DOA;
	private int empLvlId;

	private int pendingSuggCount;

	private int skillBookCount;

	private int pmsCount;

	private int nearmissCount;

	private List<HashMap<String, Object>> pendingSuggList;

	private List<HashMap<String, Object>> pendingNearmissList;

	private List<HashMap<String, Object>> skillBookPendingList;

	private List<HashMap<String, Object>> pmsPendingList;

	private List<HashMap<String, Object>> kuberPendingList;

	private List<HashMap<String, Object>> auditPendingList;

	private List<HashMap<String, Object>> dwmPendingList;

	private List<HashMap<String, Object>> tpmPendingList;

	List<HashMap<String, Object>> concernPendingList;

	private List<HashMap<String, Object>> skillmatrixPendingList;

	private int concernPendingCount;

	private int skillmatrixPendingCount;

	private int tpmPendingCount;

	private int dwmPendingCount;

	private int auditPendingCount;

	private int kuberPendingCount;

	private List<EmployeeDTO> dtoList;

	private int actionTransferTo;

	private String moduleName;

	private boolean actionPendingWithEmp;

	private String empLevelName;

	private int monthValue;

	private int yearValue;

	private int totEmpCount;

	private int activeEmpCount;

	private int inactiveEmpCount;

	private int deptId;

	private int lineId;

	private String middleName;
	
	private String address;

	public EmployeeDTO getEmpData() {
		return empData;
	}

	public void setEmpData(EmployeeDTO empData) {
		this.empData = empData;
	}

	public List<EmployeeDTO> getErrorLists() {
		return errorLists;
	}

	public void setErrorLists(List<EmployeeDTO> errorLists) {
		this.errorLists = errorLists;
	}

	public StringBuilder getMessage() {
		return message;
	}

	public void setMessage(StringBuilder message) {
		this.message = message;
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

	public String getDesignation() {
		return designation;
	}

	public void setDesignation(String designation) {
		this.designation = designation;
	}

	public String getDepartment() {
		return department;
	}

	public void setDepartment(String department) {
		this.department = department;
	}

	public boolean isErrorInSheet() {
		return errorInSheet;
	}

	public List<EmployeeDetails> getEmpList() {
		return empList;
	}

	public void setEmpList(List<EmployeeDetails> empList) {
		this.empList = empList;
	}

	public void setErrorInSheet(boolean errorInSheet) {
		this.errorInSheet = errorInSheet;
	}

	public MultipartFile[] getExcelUpload() {
		return excelUpload;
	}

	public void setExcelUpload(MultipartFile[] excelUpload) {
		this.excelUpload = excelUpload;
	}

	public Integer getStatusCode() {
		return statusCode;
	}

	public void setStatusCode(Integer statusCode) {
		this.statusCode = statusCode;
	}

	public String getReason() {
		return reason;
	}

	public void setReason(String reason) {
		this.reason = reason;
	}

	public boolean isResult() {
		return result;
	}

	public void setResult(boolean result) {
		this.result = result;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public EmployeeDetailsBean getEmpDetails() {
		return empDetails;
	}

	public void setEmpDetails(EmployeeDetailsBean empDetails) {
		this.empDetails = empDetails;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public List<ImportEmployeeErrorList> getErrorList() {
		return errorList;
	}

	public void setErrorList(List<ImportEmployeeErrorList> errorList) {
		this.errorList = errorList;
	}

	public List<String> getEmailIdExist() {
		return emailIdExist;
	}

	public void setEmailIdExist(List<String> emailIdExist) {
		this.emailIdExist = emailIdExist;
	}

	public Set<String> getDeptExist() {
		return deptExist;
	}

	public void setDeptExist(Set<String> deptExist) {
		this.deptExist = deptExist;
	}

	public String getEmpProfilePic() {
		if (empProfilePic == null) {
			return empProfilePic;
		} else {
			return EnovationConfig.buddyConfig.get("ProfilePicPathUrl") + empProfilePic;
		}

	}

	public void setEmpProfilePic(String empProfilePic) {
		this.empProfilePic = empProfilePic;
	}

	public String getPortalLink() {
		return portalLink;
	}

	public void setPortalLink(String portalLink) {
		this.portalLink = portalLink;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public List<EmployeeDetailsBean> getAllEmpDetails() {
		return allEmpDetails;
	}

	public void setAllEmpDetails(List<EmployeeDetailsBean> allEmpDetails) {
		this.allEmpDetails = allEmpDetails;
	}

	public List<EmployeeDetails> getEmployeeList() {
		return employeeList;
	}

	public void setEmployeeList(List<EmployeeDetails> employeeList) {
		this.employeeList = employeeList;
	}

	public Set<EmployeeDetailsBean> getAbc() {
		return abc;
	}

	public void setAbc(Set<EmployeeDetailsBean> abc) {
		this.abc = abc;
	}

	public List<FeedbackType> getFeedBackTypeList() {
		return feedBackTypeList;
	}

	public void setFeedBackTypeList(List<FeedbackType> feedBackTypeList) {
		this.feedBackTypeList = feedBackTypeList;
	}

	public List<MultiBranchAccess> getMultiBranchList() {
		return multiBranchList;
	}

	public void setMultiBranchList(List<MultiBranchAccess> multiBranchList) {
		this.multiBranchList = multiBranchList;
	}

	public List<NoticeSetup> getNoticeList() {
		return noticeList;
	}

	public void setNoticeList(List<NoticeSetup> noticeList) {
		this.noticeList = noticeList;
	}

	public Map<String, List<ImportEmployeeErrorList>> getErrorlist1() {
		return errorlist1;
	}

	public void setErrorlist1(Map<String, List<ImportEmployeeErrorList>> errorlist1) {
		this.errorlist1 = errorlist1;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public PasswordPolicy getPasswordPolicy() {
		return passwordPolicy;
	}

	public void setPasswordPolicy(PasswordPolicy passwordPolicy) {
		this.passwordPolicy = passwordPolicy;
	}

	public List<EmployeeDetails> getList() {
		return list;
	}

	public void setList(List<EmployeeDetails> list) {
		this.list = list;
	}

	public int getRoleId() {
		return roleId;
	}

	public void setRoleId(int roleId) {
		this.roleId = roleId;
	}

	public List<Integer> getIds() {
		return ids;
	}

	public void setIds(List<Integer> ids) {
		this.ids = ids;
	}

	public int getOrgId() {
		return orgId;
	}

	public void setOrgId(int orgId) {
		this.orgId = orgId;
	}

	public List<Integer> getBranchIds() {
		return branchIds;
	}

	public void setBranchIds(List<Integer> branchIds) {
		this.branchIds = branchIds;
	}

	public String getSrNumber() {
		return srNumber;
	}

	public void setSrNumber(String srNumber) {
		this.srNumber = srNumber;
	}

	public List<EmployeeHierarchy> getEmpLevelDetails() {
		return empLevelDetails;
	}

	public void setEmpLevelDetails(List<EmployeeHierarchy> empLevelDetails) {
		this.empLevelDetails = empLevelDetails;
	}

	public String getUserCred() {
		return userCred;
	}

	public void setUserCred(String userCred) {
		this.userCred = userCred;
	}

	public int getEmpId() {
		return empId;
	}

	public void setEmpId(int empId) {
		this.empId = empId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getBranchId() {
		return branchId;
	}

	public void setBranchId(int branchId) {
		this.branchId = branchId;
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

	public String getCmpyEmpId() {
		return cmpyEmpId;
	}

	public void setCmpyEmpId(String cmpyEmpId) {
		this.cmpyEmpId = cmpyEmpId;
	}

	public String getSuperAdminName() {
		return superAdminName;
	}

	public void setSuperAdminName(String superAdminName) {
		this.superAdminName = superAdminName;
	}

	public String getSuperAdminEmailId() {
		return superAdminEmailId;
	}

	public void setSuperAdminEmailId(String superAdminEmailId) {
		this.superAdminEmailId = superAdminEmailId;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public int getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(int createdBy) {
		this.createdBy = createdBy;
	}

	public int getIsSetupCompleted() {
		return isSetupCompleted;
	}

	public void setIsSetupCompleted(int isSetupCompleted) {
		this.isSetupCompleted = isSetupCompleted;
	}

	public String getRegistrationByPass() {
		return registrationByPass;
	}

	public void setRegistrationByPass(String registrationByPass) {
		this.registrationByPass = registrationByPass;
	}

	public String getCreatedByName() {
		return createdByName;
	}

	public void setCreatedByName(String createdByName) {
		this.createdByName = createdByName;
	}

	public MultipartFile getUploadEmployeeFile() {
		return uploadEmployeeFile;
	}

	public void setUploadEmployeeFile(MultipartFile uploadEmployeeFile) {
		this.uploadEmployeeFile = uploadEmployeeFile;
	}

	public String getIsTrialExtended() {
		return isTrialExtended;
	}

	public void setIsTrialExtended(String isTrialExtended) {
		this.isTrialExtended = isTrialExtended;
	}

	public long getNoOfDaysExtended() {
		return noOfDaysExtended;
	}

	public void setNoOfDaysExtended(long noOfDaysExtended) {
		this.noOfDaysExtended = noOfDaysExtended;
	}

	public int getIsDeactive() {
		return isDeactive;
	}

	public void setIsDeactive(int isDeactive) {
		this.isDeactive = isDeactive;
	}

	public List<EmployeeDetails> getEmpRoles() {
		return empRoles;
	}

	public void setEmpRoles(List<EmployeeDetails> empRoles) {
		this.empRoles = empRoles;
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

	public int getEmpLvlId() {
		return empLvlId;
	}

	public void setEmpLvlId(int empLvlId) {
		this.empLvlId = empLvlId;
	}

	public int getSkillBookCount() {
		return skillBookCount;
	}

	public void setSkillBookCount(int skillBookCount) {
		this.skillBookCount = skillBookCount;
	}

	public int getPmsCount() {
		return pmsCount;
	}

	public void setPmsCount(int pmsCount) {
		this.pmsCount = pmsCount;
	}

	public int getNearmissCount() {
		return nearmissCount;
	}

	public void setNearmissCount(int nearmissCount) {
		this.nearmissCount = nearmissCount;
	}

	public List<EmployeeDTO> getDtoList() {
		return dtoList;
	}

	public void setDtoList(List<EmployeeDTO> dtoList) {
		this.dtoList = dtoList;
	}

	public int getActionTransferTo() {
		return actionTransferTo;
	}

	public void setActionTransferTo(int actionTransferTo) {
		this.actionTransferTo = actionTransferTo;
	}

	public String getModuleName() {
		return moduleName;
	}

	public void setModuleName(String moduleName) {
		this.moduleName = moduleName;
	}

	public int getPendingSuggCount() {
		return pendingSuggCount;
	}

	public void setPendingSuggCount(int pendingSuggCount) {
		this.pendingSuggCount = pendingSuggCount;
	}

	public List<HashMap<String, Object>> getPendingSuggList() {
		return pendingSuggList;
	}

	public void setPendingSuggList(List<HashMap<String, Object>> pendingSuggList) {
		this.pendingSuggList = pendingSuggList;
	}

	public List<HashMap<String, Object>> getPendingNearmissList() {
		return pendingNearmissList;
	}

	public void setPendingNearmissList(List<HashMap<String, Object>> pendingNearmissList) {
		this.pendingNearmissList = pendingNearmissList;
	}

	public List<HashMap<String, Object>> getSkillBookPendingList() {
		return skillBookPendingList;
	}

	public void setSkillBookPendingList(List<HashMap<String, Object>> skillBookPendingList) {
		this.skillBookPendingList = skillBookPendingList;
	}

	public List<HashMap<String, Object>> getPmsPendingList() {
		return pmsPendingList;
	}

	public void setPmsPendingList(List<HashMap<String, Object>> pmsPendingList) {
		this.pmsPendingList = pmsPendingList;
	}

	public List<HashMap<String, Object>> getKuberPendingList() {
		return kuberPendingList;
	}

	public void setKuberPendingList(List<HashMap<String, Object>> kuberPendingList) {
		this.kuberPendingList = kuberPendingList;
	}

	public List<HashMap<String, Object>> getAuditPendingList() {
		return auditPendingList;
	}

	public void setAuditPendingList(List<HashMap<String, Object>> auditPendingList) {
		this.auditPendingList = auditPendingList;
	}

	public List<HashMap<String, Object>> getDwmPendingList() {
		return dwmPendingList;
	}

	public void setDwmPendingList(List<HashMap<String, Object>> dwmPendingList) {
		this.dwmPendingList = dwmPendingList;
	}

	public List<HashMap<String, Object>> getTpmPendingList() {
		return tpmPendingList;
	}

	public void setTpmPendingList(List<HashMap<String, Object>> tpmPendingList) {
		this.tpmPendingList = tpmPendingList;
	}

	public List<HashMap<String, Object>> getConcernPendingList() {
		return concernPendingList;
	}

	public void setConcernPendingList(List<HashMap<String, Object>> concernPendingList) {
		this.concernPendingList = concernPendingList;
	}

	public int getConcernPendingCount() {
		return concernPendingCount;
	}

	public void setConcernPendingCount(int concernPendingCount) {
		this.concernPendingCount = concernPendingCount;
	}

	public int getTpmPendingCount() {
		return tpmPendingCount;
	}

	public void setTpmPendingCount(int tpmPendingCount) {
		this.tpmPendingCount = tpmPendingCount;
	}

	public int getDwmPendingCount() {
		return dwmPendingCount;
	}

	public void setDwmPendingCount(int dwmPendingCount) {
		this.dwmPendingCount = dwmPendingCount;
	}

	public int getAuditPendingCount() {
		return auditPendingCount;
	}

	public void setAuditPendingCount(int auditPendingCount) {
		this.auditPendingCount = auditPendingCount;
	}

	public int getKuberPendingCount() {
		return kuberPendingCount;
	}

	public void setKuberPendingCount(int kuberPendingCount) {
		this.kuberPendingCount = kuberPendingCount;
	}

	public boolean isActionPendingWithEmp() {
		return actionPendingWithEmp;
	}

	public void setActionPendingWithEmp(boolean actionPendingWithEmp) {
		this.actionPendingWithEmp = actionPendingWithEmp;
	}

	public String getEmpLevelName() {
		return empLevelName;
	}

	public void setEmpLevelName(String empLevelName) {
		this.empLevelName = empLevelName;
	}

	public int getMonthValue() {
		return monthValue;
	}

	public void setMonthValue(int monthValue) {
		this.monthValue = monthValue;
	}

	public int getYearValue() {
		return yearValue;
	}

	public void setYearValue(int yearValue) {
		this.yearValue = yearValue;
	}

	public int getTotEmpCount() {
		return totEmpCount;
	}

	public void setTotEmpCount(int totEmpCount) {
		this.totEmpCount = totEmpCount;
	}

	public int getActiveEmpCount() {
		return activeEmpCount;
	}

	public void setActiveEmpCount(int activeEmpCount) {
		this.activeEmpCount = activeEmpCount;
	}

	public int getInactiveEmpCount() {
		return inactiveEmpCount;
	}

	public void setInactiveEmpCount(int inactiveEmpCount) {
		this.inactiveEmpCount = inactiveEmpCount;
	}

	public int getDeptId() {
		return deptId;
	}

	public void setDeptId(int deptId) {
		this.deptId = deptId;
	}

	public int getLineId() {
		return lineId;
	}

	public void setLineId(int lineId) {
		this.lineId = lineId;
	}

	public String getMiddleName() {
		return middleName;
	}

	public void setMiddleName(String middleName) {
		this.middleName = middleName;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public List<HashMap<String, Object>> getSkillmatrixPendingList() {
		return skillmatrixPendingList;
	}

	public void setSkillmatrixPendingList(List<HashMap<String, Object>> skillmatrixPendingList) {
		this.skillmatrixPendingList = skillmatrixPendingList;
	}

	public int getSkillmatrixPendingCount() {
		return skillmatrixPendingCount;
	}

	public void setSkillmatrixPendingCount(int skillmatrixPendingCount) {
		this.skillmatrixPendingCount = skillmatrixPendingCount;
	}
}