package com.greentin.enovation.bo.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class IncidentInfoBO {

	private EmployeeDetailsBO empDetails;
	
	private int deptId;
	
	private long managerTypeId;
	
	private String fromDt;
	
	private String managerType;

	private String toDt;
	
	private String deptName;
	
	private int orgId;
	
	private int branchId;
	
	private String firstName;
	
	private String lastName;
	
	private String empName;

	private int pendingCount;
	
	private int closedCount;
	
	private int rejectCount;
	
	private int submittedCount;
	
	private int assignedToIncidentManCount;
	
	private int totalCount;
	
	private int empId;
	
	private String reportCaption; 
	
	private int incidentCount;
	
	private String month;
	
	private String year;
	
	private String statusLabel;
	
	private String natureType;

	
	public String getReportCaption() {
		return reportCaption;
	}

	public void setReportCaption(String reportCaption) {
		this.reportCaption = reportCaption;
	}

	public String getManagerType() {
		return managerType;
	}

	public void setManagerType(String managerType) {
		this.managerType = managerType;
	}

	public EmployeeDetailsBO getEmpDetails() {
		return empDetails;
	}

	public void setEmpDetails(EmployeeDetailsBO empDetails) {
		this.empDetails = empDetails;
	}

	public int getDeptId() {
		return deptId;
	}

	public void setDeptId(int deptId) {
		this.deptId = deptId;
	}

	public long getManagerTypeId() {
		return managerTypeId;
	}

	public void setManagerTypeId(long managerTypeId) {
		this.managerTypeId = managerTypeId;
	}

	public String getFromDt() {
		return fromDt;
	}

	public void setFromDt(String fromDt) {
		this.fromDt = fromDt;
	}

	public String getToDt() {
		return toDt;
	}

	public void setToDt(String toDt) {
		this.toDt = toDt;
	}

	public String getDeptName() {
		return deptName;
	}

	public void setDeptName(String deptName) {
		this.deptName = deptName;
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

	public String getEmpName() {
		return empName;
	}

	public void setEmpName(String empName) {
		this.empName = empName;
	}

	public int getPendingCount() {
		return pendingCount;
	}

	public void setPendingCount(int pendingCount) {
		this.pendingCount = pendingCount;
	}

	public int getClosedCount() {
		return closedCount;
	}

	public void setClosedCount(int closedCount) {
		this.closedCount = closedCount;
	}

	public int getRejectCount() {
		return rejectCount;
	}

	public void setRejectCount(int rejectCount) {
		this.rejectCount = rejectCount;
	}

	public int getSubmittedCount() {
		return submittedCount;
	}

	public void setSubmittedCount(int submittedCount) {
		this.submittedCount = submittedCount;
	}

	public int getAssignedToIncidentManCount() {
		return assignedToIncidentManCount;
	}

	public void setAssignedToIncidentManCount(int assignedToIncidentManCount) {
		this.assignedToIncidentManCount = assignedToIncidentManCount;
	}

	public int getTotalCount() {
		return totalCount;
	}

	public void setTotalCount(int totalCount) {
		this.totalCount = totalCount;
	}

	public int getEmpId() {
		return empId;
	}

	public void setEmpId(int empId) {
		this.empId = empId;
	}

	public int getIncidentCount() {
		return incidentCount;
	}

	public void setIncidentCount(int incidentCount) {
		this.incidentCount = incidentCount;
	}

	public String getMonth() {
		return month;
	}

	public void setMonth(String month) {
		this.month = month;
	}

	public String getYear() {
		return year;
	}

	public void setYear(String year) {
		this.year = year;
	}

	public String getStatusLabel() {
		return statusLabel;
	}

	public void setStatusLabel(String statusLabel) {
		this.statusLabel = statusLabel;
	}

	public String getNatureType() {
		return natureType;
	}

	public void setNatureType(String natureType) {
		this.natureType = natureType;
	}
	
	

}
