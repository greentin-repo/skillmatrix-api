package com.greentin.enovation.bo.model;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class SuggestionReportBO {
	
	private EmployeeDetailsBO empDetails;
	
	private SuggestionDetailsBO suggDetails;
	
	private int empId;
	
	private String suggestionNumber;
	
	private String suggestionTitle;
	
	private String pendingStatus;
	
	private String assignedDate;
	
	private String pendingSince;
	
	private String empEmailId;
	
	private List<SuggestionReportBO> list = new ArrayList<>();;
	
	private String empName;
	
	private String portalLink;
	
	
	public SuggestionReportBO() {
		super();
	}

	public SuggestionReportBO(int empId, List<SuggestionReportBO> list) {
		super();
		this.empId = empId;
		this.list = list;
	}

	public SuggestionReportBO(int empId, String suggestionNumber, String suggestionTitle, String pendingStatus,
			String assignedDate, String pendingSince, String empEmailId, String empName, String portalLink) {
		super();
		this.empId = empId;
		this.suggestionNumber = suggestionNumber;
		this.suggestionTitle = suggestionTitle;
		this.pendingStatus = pendingStatus;
		this.assignedDate = assignedDate;
		this.pendingSince = pendingSince;
		this.empEmailId = empEmailId;
		this.empName=empName;
		this.portalLink=portalLink;
	}

	
	public String getEmpName() {
		return empName;
	}

	public void setEmpName(String empName) {
		this.empName = empName;
	}

	public EmployeeDetailsBO getEmpDetails() {
		return empDetails;
	}

	public void setEmpDetails(EmployeeDetailsBO empDetails) {
		this.empDetails = empDetails;
	}

	public SuggestionDetailsBO getSuggDetails() {
		return suggDetails;
	}

	public void setSuggDetails(SuggestionDetailsBO suggDetails) {
		this.suggDetails = suggDetails;
	}

	public int getEmpId() {
		return empId;
	}

	public void setEmpId(int empId) {
		this.empId = empId;
	}

	public String getSuggestionNumber() {
		return suggestionNumber;
	}

	public void setSuggestionNumber(String suggestionNumber) {
		this.suggestionNumber = suggestionNumber;
	}

	public String getSuggestionTitle() {
		return suggestionTitle;
	}

	public void setSuggestionTitle(String suggestionTitle) {
		this.suggestionTitle = suggestionTitle;
	}

	public String getPendingStatus() {
		return pendingStatus;
	}

	public void setPendingStatus(String pendingStatus) {
		this.pendingStatus = pendingStatus;
	}

	public String getAssignedDate() {
		return assignedDate;
	}

	public void setAssignedDate(String assignedDate) {
		this.assignedDate = assignedDate;
	}

	public String getPendingSince() {
		return pendingSince;
	}

	public void setPendingSince(String pendingSince) {
		this.pendingSince = pendingSince;
	}

	public String getEmpEmailId() {
		return empEmailId;
	}

	public void setEmpEmailId(String empEmailId) {
		this.empEmailId = empEmailId;
	}

	public List<SuggestionReportBO> getList() {
		return list;
	}

	public void setList(List<SuggestionReportBO> list) {
		this.list = list;
	}

	public String getPortalLink() {
		return portalLink;
	}

	public void setPortalLink(String portalLink) {
		this.portalLink = portalLink;
	}
	
	
}
