package com.greentin.enovation.dto;

import java.sql.Timestamp;

public class AuditScheduleDto {
	private long id;
	private long templateId;
	private int assigneeId;
	private int adminId;
	private String templateNumber;
	private String branchName;
	private String deptName;
	private String lineName;
	private String zoneName;
	private String zoneCode;
	private String recurrenceName;
	private String statusLabel;
	private String insp_desc;
	private Timestamp startDate;
	private Timestamp dueDate;
	private String templateDesc;
	private String tempalteTitle;
	private String assigneeName;
	private String assigneeEmail;
	private String assigneeConatct;
	private String adminName;
	private String adminEmail;
	private String adminContact;
	public AuditScheduleDto() {
		super();
	}
	public AuditScheduleDto(long id) {
		super();
		this.id = id;
	}
	public AuditScheduleDto(long id, long templateId, int assigneeId, int adminId, String templateNumber,
			String branchName, String deptName, String lineName, String zoneName, String zoneCode, String recurrenceName,
			String statusLabel, String insp_desc, Timestamp startDate, Timestamp dueDate, String templateDesc,
			String tempalteTitle, String assigneeName, String assigneeEmail, String assigneeConatct, String adminName,
			String adminEmail, String adminContact) {
		super();
		this.id = id;
		this.templateId = templateId;
		this.assigneeId = assigneeId;
		this.adminId = adminId;
		this.templateNumber = templateNumber;
		this.branchName = branchName;
		this.deptName = deptName;
		this.lineName = lineName;
		this.zoneName = zoneName;
		this.zoneCode=zoneCode;
		this.recurrenceName = recurrenceName;
		this.statusLabel = statusLabel;
		this.insp_desc = insp_desc;
		this.startDate = startDate;
		this.dueDate = dueDate;
		this.templateDesc = templateDesc;
		this.tempalteTitle = tempalteTitle;
		this.assigneeName = assigneeName;
		this.assigneeEmail = assigneeEmail;
		this.assigneeConatct = assigneeConatct;
		this.adminName = adminName;
		this.adminEmail = adminEmail;
		this.adminContact = adminContact;
	}
	
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public long getTemplateId() {
		return templateId;
	}
	public void setTemplateId(long templateId) {
		this.templateId = templateId;
	}
	public int getAssigneeId() {
		return assigneeId;
	}
	public void setAssigneeId(int assigneeId) {
		this.assigneeId = assigneeId;
	}
	public int getAdminId() {
		return adminId;
	}
	public void setAdminId(int adminId) {
		this.adminId = adminId;
	}
	public String getTemplateNumber() {
		return templateNumber;
	}
	public void setTemplateNumber(String templateNumber) {
		this.templateNumber = templateNumber;
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
	public String getLineName() {
		return lineName;
	}
	public void setLineName(String lineName) {
		this.lineName = lineName;
	}
	public String getZoneName() {
		return zoneName;
	}
	public void setZoneName(String zoneName) {
		this.zoneName = zoneName;
	}
	public String getRecurrenceName() {
		return recurrenceName;
	}
	public void setRecurrenceName(String recurrenceName) {
		this.recurrenceName = recurrenceName;
	}
	public String getStatusLabel() {
		return statusLabel;
	}
	public void setStatusLabel(String statusLabel) {
		this.statusLabel = statusLabel;
	}
	public String getInsp_desc() {
		return insp_desc;
	}
	public void setInsp_desc(String insp_desc) {
		this.insp_desc = insp_desc;
	}
	public Timestamp getStartDate() {
		return startDate;
	}
	public void setStartDate(Timestamp startDate) {
		this.startDate = startDate;
	}
	public Timestamp getDueDate() {
		return dueDate;
	}
	public void setDueDate(Timestamp dueDate) {
		this.dueDate = dueDate;
	}
	public String getTemplateDesc() {
		return templateDesc;
	}
	public void setTemplateDesc(String templateDesc) {
		this.templateDesc = templateDesc;
	}
	public String getTempalteTitle() {
		return tempalteTitle;
	}
	public void setTempalteTitle(String tempalteTitle) {
		this.tempalteTitle = tempalteTitle;
	}
	public String getAssigneeName() {
		return assigneeName;
	}
	public void setAssigneeName(String assigneeName) {
		this.assigneeName = assigneeName;
	}
	public String getAssigneeEmail() {
		return assigneeEmail;
	}
	public void setAssigneeEmail(String assigneeEmail) {
		this.assigneeEmail = assigneeEmail;
	}
	public String getAssigneeConatct() {
		return assigneeConatct;
	}
	public void setAssigneeConatct(String assigneeConatct) {
		this.assigneeConatct = assigneeConatct;
	}
	public String getAdminName() {
		return adminName;
	}
	public void setAdminName(String adminName) {
		this.adminName = adminName;
	}
	public String getAdminEmail() {
		return adminEmail;
	}
	public void setAdminEmail(String adminEmail) {
		this.adminEmail = adminEmail;
	}
	public String getAdminContact() {
		return adminContact;
	}
	public void setAdminContact(String adminContact) {
		this.adminContact = adminContact;
	}
	public String getZoneCode() {
		return zoneCode;
	}
	public void setZoneCode(String zoneCode) {
		this.zoneCode = zoneCode;
	}



}
