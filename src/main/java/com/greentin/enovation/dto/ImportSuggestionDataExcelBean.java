package com.greentin.enovation.dto;

import javax.persistence.Transient;

import org.springframework.web.multipart.MultipartFile;

public class ImportSuggestionDataExcelBean {

	private String suggestionNumber;
	private String employeeId;
	private String employeeName;
	private String employeeEmailId;
	private String employeeContact;
	private String suggestionDepartment;
	private String suggestionCategory;
	private String suggestionTitle;
	private String currentProblem;
	private String suggestion;
	private String suggestionCreatedDate;
	private String suggestionStatus;
	private String supervisorEmailId;
	private String supervisorContactNumber;
	private String supervisorAssignedDate;
	private String supervisorRejectionComment;
	private String supervisorRejectionDate;
	private int sugId;
	private int empId;
	private int statusId;
	private int supervisorid;
	private int deptId;
	private int catId;
	@Transient
	private int orgId;
	@Transient
	private int branchId;
	@Transient
	private MultipartFile file;
	public ImportSuggestionDataExcelBean(String suggestionNumber, String employeeId, String employeeName,
			String employeeEmailId,String employeeContact, String suggestionDepartment, String suggestionCategory, String suggestionTitle,
			String currentProblem, String suggestion, String suggestionCreatedDate, String suggestionStatus,
			String supervisorEmailId,String supervisorContactNumber, String supervisorAssignedDate, String supervisorRejectionComment,
			String supervisorRejectionDate) {
		super();
		this.suggestionNumber = suggestionNumber;
		this.employeeId = employeeId;
		this.employeeName = employeeName;
		this.employeeEmailId = employeeEmailId;
		this.employeeContact = employeeContact;
		this.suggestionDepartment = suggestionDepartment;
		this.suggestionCategory = suggestionCategory;
		this.suggestionTitle = suggestionTitle;
		this.currentProblem = currentProblem;
		this.suggestion = suggestion;
		this.suggestionCreatedDate = suggestionCreatedDate;
		this.suggestionStatus = suggestionStatus;
		this.supervisorEmailId = supervisorEmailId;
		this.supervisorContactNumber = supervisorContactNumber;
		this.supervisorAssignedDate = supervisorAssignedDate;
		this.supervisorRejectionComment = supervisorRejectionComment;
		this.supervisorRejectionDate = supervisorRejectionDate;
	}
	public String getSuggestionNumber() {
		return suggestionNumber;
	}
	public void setSuggestionNumber(String suggestionNumber) {
		this.suggestionNumber = suggestionNumber;
	}
	public String getEmployeeId() {
		return employeeId;
	}
	public void setEmployeeId(String employeeId) {
		this.employeeId = employeeId;
	}
	public String getEmployeeName() {
		return employeeName;
	}
	public void setEmployeeName(String employeeName) {
		this.employeeName = employeeName;
	}
	public String getEmployeeEmailId() {
		return employeeEmailId;
	}
	public void setEmployeeEmailId(String employeeEmailId) {
		this.employeeEmailId = employeeEmailId;
	}
	public String getEmployeeContact() {
		return employeeContact;
	}
	public void setEmployeeContact(String employeeContact) {
		this.employeeContact = employeeContact;
	}
	public String getSuggestionDepartment() {
		return suggestionDepartment;
	}
	public void setSuggestionDepartment(String suggestionDepartment) {
		this.suggestionDepartment = suggestionDepartment;
	}
	public String getSuggestionCategory() {
		return suggestionCategory;
	}
	public void setSuggestionCategory(String suggestionCategory) {
		this.suggestionCategory = suggestionCategory;
	}
	public String getSuggestionTitle() {
		return suggestionTitle;
	}
	public void setSuggestionTitle(String suggestionTitle) {
		this.suggestionTitle = suggestionTitle;
	}
	public String getCurrentProblem() {
		return currentProblem;
	}
	public void setCurrentProblem(String currentProblem) {
		this.currentProblem = currentProblem;
	}
	public String getSuggestion() {
		return suggestion;
	}
	public void setSuggestion(String suggestion) {
		this.suggestion = suggestion;
	}
	public String getSuggestionCreatedDate() {
		return suggestionCreatedDate;
	}
	public void setSuggestionCreatedDate(String suggestionCreatedDate) {
		this.suggestionCreatedDate = suggestionCreatedDate;
	}
	public String getSuggestionStatus() {
		return suggestionStatus;
	}
	public void setSuggestionStatus(String suggestionStatus) {
		this.suggestionStatus = suggestionStatus;
	}
	public String getSupervisorEmailId() {
		return supervisorEmailId;
	}
	public void setSupervisorEmailId(String supervisorEmailId) {
		this.supervisorEmailId = supervisorEmailId;
	}
	public String getSupervisorContactNumber() {
		return supervisorContactNumber;
	}
	public void setSupervisorContactNumber(String supervisorContactNumber) {
		this.supervisorContactNumber = supervisorContactNumber;
	}
	public String getSupervisorAssignedDate() {
		return supervisorAssignedDate;
	}
	public void setSupervisorAssignedDate(String supervisorAssignedDate) {
		this.supervisorAssignedDate = supervisorAssignedDate;
	}
	public String getSupervisorRejectionComment() {
		return supervisorRejectionComment;
	}
	public void setSupervisorRejectionComment(String supervisorRejectionComment) {
		this.supervisorRejectionComment = supervisorRejectionComment;
	}
	public String getSupervisorRejectionDate() {
		return supervisorRejectionDate;
	}
	public void setSupervisorRejectionDate(String supervisorRejectionDate) {
		this.supervisorRejectionDate = supervisorRejectionDate;
	}
	public int getSugId() {
		return sugId;
	}
	public void setSugId(int sugId) {
		this.sugId = sugId;
	}
	public int getEmpId() {
		return empId;
	}
	public void setEmpId(int empId) {
		this.empId = empId;
	}
	public int getStatusId() {
		return statusId;
	}
	public void setStatusId(int statusId) {
		this.statusId = statusId;
	}
	public int getSupervisorid() {
		return supervisorid;
	}
	public void setSupervisorid(int supervisorid) {
		this.supervisorid = supervisorid;
	}
	public int getDeptId() {
		return deptId;
	}
	public void setDeptId(int deptId) {
		this.deptId = deptId;
	}
	public int getCatId() {
		return catId;
	}
	public void setCatId(int catId) {
		this.catId = catId;
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
	public MultipartFile getFile() {
		return file;
	}
	public void setFile(MultipartFile file) {
		this.file = file;
	}
}
