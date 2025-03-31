package com.greentin.enovation.dto;

import java.sql.Timestamp;

public class DWMReportsDto {

	private Timestamp reportingDate;

	private String lineName;

	private String problem;

	private int repeatDefect;

	private int rejectedQty;

	private String rootCause;

	private String correctiveAction;

	private String sysDocUpdated;

	private Timestamp targetDate;

	private Timestamp updatedDate;

	private String statusLabel;

	private int totalDefect;

	private int totalDefectOELvl;

	private int totalDefectSPLvl;

	private int totalDefectPPLvl;

	private String responsibleDeptName;

	private String topic;

	private String modelName;

	private Timestamp completionsDate;

	private int totalOpenDefects;

	private long dwmId;

	private String dwmNumber;

	private String empName;

	private int openDefectOELvl;

	private int openDefectSPLvl;

	private int openDefectPPLvl;

	private String dwmOverallPerformance;

	private int downTime;

	private String topicName;

	//, dept.dept_name, dd.topic, dd.model_name, dsa.created_date

	public DWMReportsDto(Timestamp reportingDate, String lineName, String problem, int repeatDefect, int rejectedQty,
			String rootCause, String correctiveAction, String sysDocUpdated, Timestamp targetDate,Timestamp updatedDate, String statusLabel,
			String responsibleDeptName, String topic, String modelName,  Timestamp completionsDate,long dwmId, String dwmNumber, String empName, int downTime, String topicName ) {
		super();
		this.reportingDate = reportingDate;
		this.lineName = lineName;
		this.problem = problem;
		this.repeatDefect = repeatDefect;
		this.rejectedQty = rejectedQty;
		this.rootCause = rootCause;
		this.correctiveAction = correctiveAction;
		this.sysDocUpdated = sysDocUpdated;
		this.targetDate = targetDate;
		this.updatedDate = updatedDate;
		this.statusLabel = statusLabel;
		this.responsibleDeptName=responsibleDeptName;
		this.topic=topic;
		this.modelName=modelName;
		this.completionsDate=completionsDate;
		this.dwmId=dwmId;
		this.empName=empName;
		this.dwmNumber=dwmNumber;
		this.downTime=downTime;
		this.topicName=topicName;
	}

	public DWMReportsDto(int totalDefect, int totalDefectOELvl, int totalDefectSPLvl, int totalDefectPPLvl, int totalOpenDefects
			, int openDefectOELvl,int openDefectSPLvl, int openDefectPPLvl) {
		super();
		this.totalDefect = totalDefect;
		this.totalDefectOELvl = totalDefectOELvl;
		this.totalDefectSPLvl = totalDefectSPLvl;
		this.totalDefectPPLvl = totalDefectPPLvl;
		this.totalOpenDefects=totalOpenDefects;
		this.openDefectOELvl=openDefectOELvl;
		this.openDefectSPLvl=openDefectSPLvl;
		this.openDefectPPLvl=openDefectPPLvl;
	}

	public int getTotalDefect() {
		return totalDefect;
	}

	public void setTotalDefect(int totalDefect) {
		this.totalDefect = totalDefect;
	}

	public int getTotalDefectOELvl() {
		return totalDefectOELvl;
	}

	public void setTotalDefectOELvl(int totalDefectOELvl) {
		this.totalDefectOELvl = totalDefectOELvl;
	}

	public int getTotalDefectSPLvl() {
		return totalDefectSPLvl;
	}

	public void setTotalDefectSPLvl(int totalDefectSPLvl) {
		this.totalDefectSPLvl = totalDefectSPLvl;
	}

	public int getTotalDefectPPLvl() {
		return totalDefectPPLvl;
	}

	public void setTotalDefectPPLvl(int totalDefectPPLvl) {
		this.totalDefectPPLvl = totalDefectPPLvl;
	}

	public Timestamp getReportingDate() {
		return reportingDate;
	}

	public void setReportingDate(Timestamp reportingDate) {
		this.reportingDate = reportingDate;
	}

	public String getLineName() {
		return lineName;
	}

	public void setLineName(String lineName) {
		this.lineName = lineName;
	}

	public String getProblem() {
		return problem;
	}

	public void setProblem(String problem) {
		this.problem = problem;
	}

	public int getRepeatDefect() {
		return repeatDefect;
	}

	public void setRepeatDefect(int repeatDefect) {
		this.repeatDefect = repeatDefect;
	}

	public int getRejectedQty() {
		return rejectedQty;
	}

	public void setRejectedQty(int rejectedQty) {
		this.rejectedQty = rejectedQty;
	}

	public String getRootCause() {
		return rootCause;
	}

	public void setRootCause(String rootCause) {
		this.rootCause = rootCause;
	}

	public String getCorrectiveAction() {
		return correctiveAction;
	}

	public void setCorrectiveAction(String correctiveAction) {
		this.correctiveAction = correctiveAction;
	}

	public String getSysDocUpdated() {
		return sysDocUpdated;
	}

	public void setSysDocUpdated(String sysDocUpdated) {
		this.sysDocUpdated = sysDocUpdated;
	}

	public Timestamp getTargetDate() {
		return targetDate;
	}

	public void setTargetDate(Timestamp targetDate) {
		this.targetDate = targetDate;
	}

	public Timestamp getUpdatedDate() {
		return updatedDate;
	}

	public void setUpdatedDate(Timestamp updatedDate) {
		this.updatedDate = updatedDate;
	}

	public String getStatusLabel() {
		return statusLabel;
	}

	public void setStatusLabel(String statusLabel) {
		this.statusLabel = statusLabel;
	}



	public String getResponsibleDeptName() {
		return responsibleDeptName;
	}



	public void setResponsibleDeptName(String responsibleDeptName) {
		this.responsibleDeptName = responsibleDeptName;
	}



	public String getTopic() {
		return topic;
	}



	public void setTopic(String topic) {
		this.topic = topic;
	}



	public String getModelName() {
		return modelName;
	}



	public void setModelName(String modelName) {
		this.modelName = modelName;
	}



	public Timestamp getCompletionsDate() {
		return completionsDate;
	}



	public void setCompletionsDate(Timestamp completionsDate) {
		this.completionsDate = completionsDate;
	}


	public int getTotalOpenDefects() {
		return totalOpenDefects;
	}


	public void setTotalOpenDefects(int totalOpenDefects) {
		this.totalOpenDefects = totalOpenDefects;
	}


	public long getDwmId() {
		return dwmId;
	}


	public void setDwmId(long dwmId) {
		this.dwmId = dwmId;
	}


	public String getDwmNumber() {
		return dwmNumber;
	}


	public void setDwmNumber(String dwmNumber) {
		this.dwmNumber = dwmNumber;
	}


	public String getEmpName() {
		return empName;
	}


	public void setEmpName(String empName) {
		this.empName = empName;
	}

	public int getOpenDefectOELvl() {
		return openDefectOELvl;
	}

	public void setOpenDefectOELvl(int openDefectOELvl) {
		this.openDefectOELvl = openDefectOELvl;
	}

	public int getOpenDefectSPLvl() {
		return openDefectSPLvl;
	}

	public void setOpenDefectSPLvl(int openDefectSPLvl) {
		this.openDefectSPLvl = openDefectSPLvl;
	}

	public int getOpenDefectPPLvl() {
		return openDefectPPLvl;
	}

	public void setOpenDefectPPLvl(int openDefectPPLvl) {
		this.openDefectPPLvl = openDefectPPLvl;
	}

	public String getDwmOverallPerformance() {
		return dwmOverallPerformance;
	}

	public void setDwmOverallPerformance(String dwmOverallPerformance) {
		this.dwmOverallPerformance = dwmOverallPerformance;
	}

	public int getDownTime() {
		return downTime;
	}

	public void setDownTime(int downTime) {
		this.downTime = downTime;
	}

	public String getTopicName() {
		return topicName;
	}

	public void setTopicName(String topicName) {
		this.topicName = topicName;
	}



}
