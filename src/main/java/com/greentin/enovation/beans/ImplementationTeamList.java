package com.greentin.enovation.beans;

import com.greentin.enovation.config.EnovationConfig;

public class ImplementationTeamList {

	private String implProfilePic;

	private String assignedByFirstName;

	private String assignedByLastName;

	private Integer sugTrackId;

	private Integer costEffective;

	private Integer improvement;

	private String evaluationInputs;

	private String startDate;

	private String endDate;

	private String createdDate;

	private String updatedDate;

	private Integer empId;

	private String empName;

	private String empEmailId;

	private Integer assigedById;

	private String assigedByName;

	private String assigedByEmailId;

	private Integer stsId;

	private String stsName; 

	private Integer teamTypeId;

	private String teamType;

	private Integer estimatedCost;

	private Integer implementedCost;

	private String implInputs;

	private String summary;

	private String summaryRejectionImpl;

	private int statusAuditId;


	public Integer getSugTrackId() {
		return sugTrackId;
	}
	public void setSugTrackId(Integer sugTrackId) {
		this.sugTrackId = sugTrackId;
	}
	public Integer getCostEffective() {
		return costEffective;
	}
	public void setCostEffective(Integer costEffective) {
		this.costEffective = costEffective;
	}
	public Integer getImprovement() {
		return improvement;
	}
	public void setImprovement(Integer improvement) {
		this.improvement = improvement;
	}
	public String getEvaluationInputs() {
		return evaluationInputs;
	}
	public void setEvaluationInputs(String evaluationInputs) {
		this.evaluationInputs = evaluationInputs;
	}
	public String getStartDate() {
		return startDate;
	}
	public void setStartDate(String startDate) {
		this.startDate = startDate;
	}
	public String getEndDate() {
		return endDate;
	}
	public void setEndDate(String endDate) {
		this.endDate = endDate;
	}
	public String getCreatedDate() {
		return createdDate;
	}
	public void setCreatedDate(String createdDate) {
		this.createdDate = createdDate;
	}
	public String getUpdatedDate() {
		return updatedDate;
	}
	public void setUpdatedDate(String updatedDate) {
		this.updatedDate = updatedDate;
	}
	public Integer getEmpId() {
		return empId;
	}
	public void setEmpId(Integer empId) {
		this.empId = empId;
	}
	public String getEmpName() {
		return empName;
	}
	public void setEmpName(String empName) {
		this.empName = empName;
	}
	public String getEmpEmailId() {
		return empEmailId;
	}
	public void setEmpEmailId(String empEmailId) {
		this.empEmailId = empEmailId;
	}
	public Integer getAssigedById() {
		return assigedById;
	}
	public void setAssigedById(Integer assigedById) {
		this.assigedById = assigedById;
	}
	public String getAssigedByName() {
		return assigedByName;
	}
	public void setAssigedByName(String assigedByName) {
		this.assigedByName = assigedByName;
	}
	public String getAssigedByEmailId() {
		return assigedByEmailId;
	}
	public void setAssigedByEmailId(String assigedByEmailId) {
		this.assigedByEmailId = assigedByEmailId;
	}
	public Integer getStsId() {
		return stsId;
	}
	public void setStsId(Integer stsId) {
		this.stsId = stsId;
	}
	public String getStsName() {
		return stsName;
	}
	public void setStsName(String stsName) {
		this.stsName = stsName;
	}
	public Integer getTeamTypeId() {
		return teamTypeId;
	}
	public void setTeamTypeId(Integer teamTypeId) {
		this.teamTypeId = teamTypeId;
	}
	public String getTeamType() {
		return teamType;
	}
	public void setTeamType(String teamType) {
		this.teamType = teamType;
	}
	public Integer getEstimatedCost() {
		return estimatedCost;
	}
	public void setEstimatedCost(Integer estimatedCost) {
		this.estimatedCost = estimatedCost;
	}
	public Integer getImplementedCost() {
		return implementedCost;
	}
	public void setImplementedCost(Integer implementedCost) {
		this.implementedCost = implementedCost;
	}
	public String getImplInputs() {
		return implInputs;
	}
	public void setImplInputs(String implInputs) {
		this.implInputs = implInputs;
	}
	public String getSummary() {
		return summary;
	}
	public void setSummary(String summary) {
		this.summary = summary;
	}
	public String getSummaryRejectionImpl() {
		return summaryRejectionImpl;
	}
	public void setSummaryRejectionImpl(String summaryRejectionImpl) {
		this.summaryRejectionImpl = summaryRejectionImpl;
	}
	public int getStatusAuditId() {
		return statusAuditId;
	}
	public void setStatusAuditId(int statusAuditId) {
		this.statusAuditId = statusAuditId;
	}
	public String getAssignedByFirstName() {
		return assignedByFirstName;
	}
	public void setAssignedByFirstName(String assignedByFirstName) {
		this.assignedByFirstName = assignedByFirstName;
	}
	public String getAssignedByLastName() {
		return assignedByLastName;
	}
	public void setAssignedByLastName(String assignedByLastName) {
		this.assignedByLastName = assignedByLastName;
	}
	public String getImplProfilePic() {
		if(implProfilePic==null) {
			return implProfilePic;	
		}else {
			return	EnovationConfig.buddyConfig.get("ProfilePicPathUrl")+implProfilePic;
		}
	}
	public void setImplProfilePic(String implProfilePic) {
		this.implProfilePic = implProfilePic;
	}

}
