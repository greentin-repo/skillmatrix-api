package com.greentin.enovation.dto;


public class KuberReportsDTO {

	private String totalProjects;

	private String totalInProgressProjects;

	private String totalClosedProjects;

	private String totalTerminatedProjects;

	private Double overallSavings;

	private Double totalInvestment;

	private Double totalBenefits;
	
	private String projectsInPipeline;
	
	private Double estimatedSaving;

	private Double estimatedInvestment;
	
	private String usersParticipation;
	
	private String registerUsers;
	
	private String activeUsers;

	public KuberReportsDTO(String totalProjects, String totalInProgressProjects, String totalClosedProjects,
			String totalTerminatedProjects, Double overallSavings, Double totalInvestment, Double totalBenefits,String projectsInPipeline,
			Double estimatedSaving, Double estimatedInvestment, String usersParticipation, String registerUsers, String activeUsers) {
		super();
		this.totalProjects = totalProjects;
		this.totalInProgressProjects = totalInProgressProjects;
		this.totalClosedProjects = totalClosedProjects;
		this.totalTerminatedProjects = totalTerminatedProjects;
		this.overallSavings = overallSavings;
		this.totalInvestment = totalInvestment;
		this.totalBenefits = totalBenefits;
		this.projectsInPipeline=projectsInPipeline;
		this.estimatedSaving=estimatedSaving;
		this.estimatedInvestment=estimatedInvestment;
		this.usersParticipation=usersParticipation;
		this.registerUsers=registerUsers;
		this.activeUsers=activeUsers;
	}



	public KuberReportsDTO() {
		super();
	}



	public String getTotalProjects() {
		return totalProjects;
	}



	public void setTotalProjects(String totalProjects) {
		this.totalProjects = totalProjects;
	}



	public String getTotalInProgressProjects() {
		return totalInProgressProjects;
	}



	public void setTotalInProgressProjects(String totalInProgressProjects) {
		this.totalInProgressProjects = totalInProgressProjects;
	}



	public String getTotalClosedProjects() {
		return totalClosedProjects;
	}



	public void setTotalClosedProjects(String totalClosedProjects) {
		this.totalClosedProjects = totalClosedProjects;
	}



	public String getTotalTerminatedProjects() {
		return totalTerminatedProjects;
	}



	public void setTotalTerminatedProjects(String totalTerminatedProjects) {
		this.totalTerminatedProjects = totalTerminatedProjects;
	}



	public Double getOverallSavings() {
		return overallSavings;
	}



	public void setOverallSavings(Double overallSavings) {
		this.overallSavings = overallSavings;
	}



	public Double getTotalInvestment() {
		return totalInvestment;
	}



	public void setTotalInvestment(Double totalInvestment) {
		this.totalInvestment = totalInvestment;
	}



	public Double getTotalBenefits() {
		return totalBenefits;
	}



	public void setTotalBenefits(Double totalBenefits) {
		this.totalBenefits = totalBenefits;
	}



	public String getProjectsInPipeline() {
		return projectsInPipeline;
	}



	public void setProjectsInPipeline(String projectsInPipeline) {
		this.projectsInPipeline = projectsInPipeline;
	}



	public Double getEstimatedSaving() {
		return estimatedSaving;
	}



	public void setEstimatedSaving(Double estimatedSaving) {
		this.estimatedSaving = estimatedSaving;
	}



	public Double getEstimatedInvestment() {
		return estimatedInvestment;
	}



	public void setEstimatedInvestment(Double estimatedInvestment) {
		this.estimatedInvestment = estimatedInvestment;
	}



	public String getUsersParticipation() {
		return usersParticipation;
	}



	public void setUsersParticipation(String usersParticipation) {
		this.usersParticipation = usersParticipation;
	}



	public String getRegisterUsers() {
		return registerUsers;
	}



	public void setRegisterUsers(String registerUsers) {
		this.registerUsers = registerUsers;
	}



	public String getActiveUsers() {
		return activeUsers;
	}



	public void setActiveUsers(String activeUsers) {
		this.activeUsers = activeUsers;
	}




}
