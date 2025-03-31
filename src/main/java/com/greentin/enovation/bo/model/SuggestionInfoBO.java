package com.greentin.enovation.bo.model;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.greentin.enovation.model.ColumnSearch;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class SuggestionInfoBO {

	private EmployeeDetailsBO empDetails;

	private CategoryMasterBO category;

	private int deptId;

	private long roleId;

	private String fromDt;

	private String toDt;

	private String deptName;

	private int orgId;

	private int branchId;

	private String roleName;

	private String cmpEmpId;

	private String firstName;

	private String lastName;

	private String empName;

	private String monthName;

	private int pendingCount;

	private int approvedCount;

	private int rejectCount;

	private int inTimeCount;

	private int delayedTimeCount;

	private int totalCount;

	private int implementedCount;

	private int onHoldCount;

	private int empId;

	private int controllerPending;

	private int evalutorPending;

	private int implementerPending;

	private int fcPending;

	private int documentPending;

	private int initiatorPending;

	private String reportCaption;

	private int totalEmpCount;

	List<Integer> deptIds;

	private int manpowerAsOn;

	private int targetedSuggestion;

	private int participationOfManpower;

	private int actualSuggestion;

	private String date;

	private int ideasGiven;

	private int manpowerForMonth;

	private int participationOfOe;

	private String perOfParticipation;

	private String suggestionSourceName;

	private String totalIdeas;

	private String totalInprogressCount;

	private String totalOnHoldSuggestionCount;

	private String totalImplementedCount;

	private String totalRejectedCount;

	private int typeId;

	private List<ColumnSearch> columnLike;

	private int limit;

	private int offset;

	private int sugNumber;

	private String sugTitle;

	private long costEffective;

	private long implementedCost;

	private String createdDate;

	private int nomId;

	private int sugId;

	private String createdSugNumber;

	private String suggestion;

	private int documentId;

	private String documentName;

	private String status;

	private String name;

	private String emailId;

	private String contact;

	private String points;

	private String evalName;

	private String modifiedBy;
	
	private int onBehalfEmpId;
	
	private String sugProcessType;
	
	private String catName;
	
	public SuggestionInfoBO() {
		super();
	}

	public SuggestionInfoBO(String suggestionSourceName, String totalIdeas, String totalInprogressCount,
			String totalOnHoldSuggestionCount, String totalImplementedCount, String totalRejectedCount) {
		super();
		this.suggestionSourceName = suggestionSourceName;
		this.totalIdeas = totalIdeas;
		this.totalInprogressCount = totalInprogressCount;
		this.totalOnHoldSuggestionCount = totalOnHoldSuggestionCount;
		this.totalImplementedCount = totalImplementedCount;
		this.totalRejectedCount = totalRejectedCount;
	}

	public String getSuggestionSourceName() {
		return suggestionSourceName;
	}

	public void setSuggestionSourceName(String suggestionSourceName) {
		this.suggestionSourceName = suggestionSourceName;
	}

	public String getTotalIdeas() {
		return totalIdeas;
	}

	public void setTotalIdeas(String totalIdeas) {
		this.totalIdeas = totalIdeas;
	}

	public String getTotalInprogressCount() {
		return totalInprogressCount;
	}

	public void setTotalInprogressCount(String totalInprogressCount) {
		this.totalInprogressCount = totalInprogressCount;
	}

	public String getTotalOnHoldSuggestionCount() {
		return totalOnHoldSuggestionCount;
	}

	public void setTotalOnHoldSuggestionCount(String totalOnHoldSuggestionCount) {
		this.totalOnHoldSuggestionCount = totalOnHoldSuggestionCount;
	}

	public String getTotalImplementedCount() {
		return totalImplementedCount;
	}

	public void setTotalImplementedCount(String totalImplementedCount) {
		this.totalImplementedCount = totalImplementedCount;
	}

	public String getTotalRejectedCount() {
		return totalRejectedCount;
	}

	public void setTotalRejectedCount(String totalRejectedCount) {
		this.totalRejectedCount = totalRejectedCount;
	}

	public int getDeptId() {
		return deptId;
	}

	public void setDeptId(int deptId) {
		this.deptId = deptId;
	}

	public long getRoleId() {
		return roleId;
	}

	public void setRoleId(long roleId) {
		this.roleId = roleId;
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

	public int getBranchId() {
		return branchId;
	}

	public void setBranchId(int branchId) {
		this.branchId = branchId;
	}

	public String getRoleName() {
		return roleName;
	}

	public void setRoleName(String roleName) {
		this.roleName = roleName;
	}

	public String getCmpEmpId() {
		return cmpEmpId;
	}

	public void setCmpEmpId(String cmpEmpId) {
		this.cmpEmpId = cmpEmpId;
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

	public int getPendingCount() {
		return pendingCount;
	}

	public void setPendingCount(int pendingCount) {
		this.pendingCount = pendingCount;
	}

	public int getApprovedCount() {
		return approvedCount;
	}

	public void setApprovedCount(int approvedCount) {
		this.approvedCount = approvedCount;
	}

	public int getRejectCount() {
		return rejectCount;
	}

	public void setRejectCount(int rejectCount) {
		this.rejectCount = rejectCount;
	}

	public int getInTimeCount() {
		return inTimeCount;
	}

	public void setInTimeCount(int inTimeCount) {
		this.inTimeCount = inTimeCount;
	}

	public int getDelayedTimeCount() {
		return delayedTimeCount;
	}

	public void setDelayedTimeCount(int delayedTimeCount) {
		this.delayedTimeCount = delayedTimeCount;
	}

	public int getTotalCount() {
		return totalCount;
	}

	public void setTotalCount(int totalCount) {
		this.totalCount = totalCount;
	}

	public int getImplementedCount() {
		return implementedCount;
	}

	public void setImplementedCount(int implementedCount) {
		this.implementedCount = implementedCount;
	}

	public int getOnHoldCount() {
		return onHoldCount;
	}

	public void setOnHoldCount(int onHoldCount) {
		this.onHoldCount = onHoldCount;
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

	public int getEmpId() {
		return empId;
	}

	public void setEmpId(int empId) {
		this.empId = empId;
	}

	public String getReportCaption() {
		return reportCaption;
	}

	public void setReportCaption(String reportCaption) {
		this.reportCaption = reportCaption;
	}

	public int getControllerPending() {
		return controllerPending;
	}

	public void setControllerPending(int controllerPending) {
		this.controllerPending = controllerPending;
	}

	public int getEvalutorPending() {
		return evalutorPending;
	}

	public void setEvalutorPending(int evalutorPending) {
		this.evalutorPending = evalutorPending;
	}

	public int getImplementerPending() {
		return implementerPending;
	}

	public void setImplementerPending(int implementerPending) {
		this.implementerPending = implementerPending;
	}

	public int getFcPending() {
		return fcPending;
	}

	public void setFcPending(int fcPending) {
		this.fcPending = fcPending;
	}

	public int getDocumentPending() {
		return documentPending;
	}

	public void setDocumentPending(int documentPending) {
		this.documentPending = documentPending;
	}

	public int getInitiatorPending() {
		return initiatorPending;
	}

	public void setInitiatorPending(int initiatorPending) {
		this.initiatorPending = initiatorPending;
	}

	public EmployeeDetailsBO getEmpDetails() {
		return empDetails;
	}

	public void setEmpDetails(EmployeeDetailsBO empDetails) {
		this.empDetails = empDetails;
	}

	public String getEmpName() {
		return empName;
	}

	public void setEmpName(String empName) {
		this.empName = empName;
	}

	public String getMonthName() {
		return monthName;
	}

	public void setMonthName(String monthName) {
		this.monthName = monthName;
	}

	public int getTotalEmpCount() {
		return totalEmpCount;
	}

	public void setTotalEmpCount(int totalEmpCount) {
		this.totalEmpCount = totalEmpCount;
	}

	public CategoryMasterBO getCategory() {
		return category;
	}

	public void setCategory(CategoryMasterBO category) {
		this.category = category;
	}

	public List<Integer> getDeptIds() {
		return deptIds;
	}

	public void setDeptIds(List<Integer> deptIds) {
		this.deptIds = deptIds;
	}

	public int getManpowerAsOn() {
		return manpowerAsOn;
	}

	public void setManpowerAsOn(int manpowerAsOn) {
		this.manpowerAsOn = manpowerAsOn;
	}

	public int getTargetedSuggestion() {
		return targetedSuggestion;
	}

	public void setTargetedSuggestion(int targetedSuggestion) {
		this.targetedSuggestion = targetedSuggestion;
	}

	public int getParticipationOfManpower() {
		return participationOfManpower;
	}

	public void setParticipationOfManpower(int participationOfManpower) {
		this.participationOfManpower = participationOfManpower;
	}

	public int getActualSuggestion() {
		return actualSuggestion;
	}

	public void setActualSuggestion(int actualSuggestion) {
		this.actualSuggestion = actualSuggestion;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public int getIdeasGiven() {
		return ideasGiven;
	}

	public void setIdeasGiven(int ideasGiven) {
		this.ideasGiven = ideasGiven;
	}

	public int getManpowerForMonth() {
		return manpowerForMonth;
	}

	public void setManpowerForMonth(int manpowerForMonth) {
		this.manpowerForMonth = manpowerForMonth;
	}

	public int getParticipationOfOe() {
		return participationOfOe;
	}

	public void setParticipationOfOe(int participationOfOe) {
		this.participationOfOe = participationOfOe;
	}

	public String getPerOfParticipation() {
		return perOfParticipation;
	}

	public void setPerOfParticipation(String perOfParticipation) {
		this.perOfParticipation = perOfParticipation;
	}

	public int getTypeId() {
		return typeId;
	}

	public void setTypeId(int typeId) {
		this.typeId = typeId;
	}

	public List<ColumnSearch> getColumnLike() {
		return columnLike;
	}

	public void setColumnLike(List<ColumnSearch> columnLike) {
		this.columnLike = columnLike;
	}

	public int getLimit() {
		return limit;
	}

	public void setLimit(int limit) {
		this.limit = limit;
	}

	public int getOffset() {
		return offset;
	}

	public void setOffset(int offset) {
		this.offset = offset;
	}

	public int getSugNumber() {
		return sugNumber;
	}

	public void setSugNumber(int sugNumber) {
		this.sugNumber = sugNumber;
	}

	public String getSugTitle() {
		return sugTitle;
	}

	public void setSugTitle(String sugTitle) {
		this.sugTitle = sugTitle;
	}

	public long getCostEffective() {
		return costEffective;
	}

	public void setCostEffective(long costEffective) {
		this.costEffective = costEffective;
	}

	public long getImplementedCost() {
		return implementedCost;
	}

	public void setImplementedCost(long implementedCost) {
		this.implementedCost = implementedCost;
	}

	public String getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(String createdDate) {
		this.createdDate = createdDate;
	}

	public int getNomId() {
		return nomId;
	}

	public void setNomId(int nomId) {
		this.nomId = nomId;
	}

	public int getSugId() {
		return sugId;
	}

	public void setSugId(int sugId) {
		this.sugId = sugId;
	}

	public String getCreatedSugNumber() {
		return createdSugNumber;
	}

	public void setCreatedSugNumber(String createdSugNumber) {
		this.createdSugNumber = createdSugNumber;
	}

	public String getSuggestion() {
		return suggestion;
	}

	public void setSuggestion(String suggestion) {
		this.suggestion = suggestion;
	}

	public int getDocumentId() {
		return documentId;
	}

	public void setDocumentId(int documentId) {
		this.documentId = documentId;
	}

	public String getDocumentName() {
		return documentName;
	}

	public void setDocumentName(String documentName) {
		this.documentName = documentName;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getEmailId() {
		return emailId;
	}

	public void setEmailId(String emailId) {
		this.emailId = emailId;
	}

	public String getContact() {
		return contact;
	}

	public void setContact(String contact) {
		this.contact = contact;
	}

	public String getPoints() {
		return points;
	}

	public void setPoints(String points) {
		this.points = points;
	}

	public String getEvalName() {
		return evalName;
	}

	public void setEvalName(String evalName) {
		this.evalName = evalName;
	}

	public String getModifiedBy() {
		return modifiedBy;
	}

	public void setModifiedBy(String modifiedBy) {
		this.modifiedBy = modifiedBy;
	}

	public int getOnBehalfEmpId() {
		return onBehalfEmpId;
	}

	public void setOnBehalfEmpId(int onBehalfEmpId) {
		this.onBehalfEmpId = onBehalfEmpId;
	}

	public String getSugProcessType() {
		return sugProcessType;
	}

	public void setSugProcessType(String sugProcessType) {
		this.sugProcessType = sugProcessType;
	}

	public String getCatName() {
		return catName;
	}

	public void setCatName(String catName) {
		this.catName = catName;
	}
	
	

}
