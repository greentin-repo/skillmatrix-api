package com.greentin.enovation.dto;

import java.sql.Timestamp;

public class ProjectBO {
	
	private long projId;
	
	private String theme;
		
	private int  stsId;
	
	private String statusLabel;
	
	private long teamId;
	
	private String teamName;
	
	private Timestamp createdDate;
	
	private int count;
	
	private int filterId;
	
	private Timestamp finishDate;
	
	private String projectName;

	private String plantName;
	
	private String uniqueProjectNum;
	
	private String projDesc;
	
	private String projectStartDate;
	
	private String projectCompletionDate;
	
	private String projectClosureDate;
	
	private Double estimatedCost;
	
	private Double estimatedInvestment;
	
	private Double rtnOnInvestment;
	
	private Double benchMark;
	
	private Double target;
	
	private String savingFreq;
	
	private String costSaving;
	
	private String teamMembers;
	
	private String teamLeader;
	
	private int review;
	
	private int subReview;
	private String assignedToName;
	
	private String assignedToDate;
	
	public ProjectBO() {
		
	}
	
    public ProjectBO(int count, int filterId) {
		this.count=count;
		this.filterId=filterId;
	}
    
    


	public ProjectBO(String projDesc, String projectStartDate, String projectCompletionDate, String projectClosureDate,
			Double estimatedCost, Double estimatedInvestment, Double rtnOnInvestment, Double benchMark, Double target,
			String savingFreq, String costSaving, String teamMembers, String teamLeader, String assignedToName, String assignedToDate) {
		super();
		this.projDesc = projDesc;
		this.projectStartDate = projectStartDate;
		this.projectCompletionDate = projectCompletionDate;
		this.projectClosureDate = projectClosureDate;
		this.estimatedCost = estimatedCost;
		this.estimatedInvestment = estimatedInvestment;
		this.rtnOnInvestment = rtnOnInvestment;
		this.benchMark = benchMark;
		this.target = target;
		this.savingFreq = savingFreq;
		this.costSaving = costSaving;
		this.teamMembers = teamMembers;
		this.teamLeader = teamLeader;
		this.assignedToName=assignedToName;
		this.assignedToDate=assignedToDate;
	}

	public int getCount() {
		return count;
	}

	public void setCount(int count) {
		this.count = count;
	}

	public int getFilterId() {
		return filterId;
	}

	public void setFilterId(int filterId) {
		this.filterId = filterId;
	}

	public String getTheme() {
		return theme;
	}

	public void setTheme(String theme) {
		this.theme = theme;
	}
	
	public String getStatusLabel() {
		return statusLabel;
	}

	public void setStatusLabel(String statusLabel) {
		this.statusLabel = statusLabel;
	}

	public Timestamp getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(Timestamp createdDate) {
		this.createdDate = createdDate;
	}

	public long getProjId() {
		return projId;
	}

	public void setProjId(long projId) {
		this.projId = projId;
	}

	public int getStsId() {
		return stsId;
	}

	public void setStsId(int stsId) {
		this.stsId = stsId;
	}

	public long getTeamId() {
		return teamId;
	}

	public void setTeamId(long teamId) {
		this.teamId = teamId;
	}

	public String getTeamName() {
		return teamName;
	}

	public void setTeamName(String teamName) {
		this.teamName = teamName;
	}

	public String getProjectName() {
		return projectName;
	}

	public void setProjectName(String projectName) {
		this.projectName = projectName;
	}

	public String getPlantName() {
		return plantName;
	}

	public void setPlantName(String plantName) {
		this.plantName = plantName;
	}

	public Timestamp getFinishDate() {
		return finishDate;
	}

	public void setFinishDate(Timestamp finishDate) {
		this.finishDate = finishDate;
	}

	public String getUniqueProjectNum() {
		return uniqueProjectNum;
	}

	public void setUniqueProjectNum(String uniqueProjectNum) {
		this.uniqueProjectNum = uniqueProjectNum;
	}

	public String getProjDesc() {
		return projDesc;
	}

	public void setProjDesc(String projDesc) {
		this.projDesc = projDesc;
	}

	public String getProjectStartDate() {
		return projectStartDate;
	}

	public void setProjectStartDate(String projectStartDate) {
		this.projectStartDate = projectStartDate;
	}

	public String getProjectCompletionDate() {
		return projectCompletionDate;
	}

	public void setProjectCompletionDate(String projectCompletionDate) {
		this.projectCompletionDate = projectCompletionDate;
	}

	public String getProjectClosureDate() {
		return projectClosureDate;
	}

	public void setProjectClosureDate(String projectClosureDate) {
		this.projectClosureDate = projectClosureDate;
	}

	public Double getEstimatedCost() {
		return estimatedCost;
	}

	public void setEstimatedCost(Double estimatedCost) {
		this.estimatedCost = estimatedCost;
	}

	public Double getEstimatedInvestment() {
		return estimatedInvestment;
	}

	public void setEstimatedInvestment(Double estimatedInvestment) {
		this.estimatedInvestment = estimatedInvestment;
	}

	public Double getRtnOnInvestment() {
		return rtnOnInvestment;
	}

	public void setRtnOnInvestment(Double rtnOnInvestment) {
		this.rtnOnInvestment = rtnOnInvestment;
	}

	public Double getBenchMark() {
		return benchMark;
	}

	public void setBenchMark(Double benchMark) {
		this.benchMark = benchMark;
	}

	public Double getTarget() {
		return target;
	}

	public void setTarget(Double target) {
		this.target = target;
	}

	public String getSavingFreq() {
		return savingFreq;
	}

	public void setSavingFreq(String savingFreq) {
		this.savingFreq = savingFreq;
	}

	public String getCostSaving() {
		return costSaving;
	}

	public void setCostSaving(String costSaving) {
		this.costSaving = costSaving;
	}

	public String getTeamMembers() {
		return teamMembers;
	}

	public void setTeamMembers(String teamMembers) {
		this.teamMembers = teamMembers;
	}

	public String getTeamLeader() {
		return teamLeader;
	}

	public void setTeamLeader(String teamLeader) {
		this.teamLeader = teamLeader;
	}

	public int getReview() {
		return review;
	}

	public void setReview(int review) {
		this.review = review;
	}

	public int getSubReview() {
		return subReview;
	}

	public void setSubReview(int subReview) {
		this.subReview = subReview;
	}

	public String getAssignedToName() {
		return assignedToName;
	}

	public void setAssignedToName(String assignedToName) {
		this.assignedToName = assignedToName;
	}

	public String getAssignedToDate() {
		return assignedToDate;
	}

	public void setAssignedToDate(String assignedToDate) {
		this.assignedToDate = assignedToDate;
	}


	
}
