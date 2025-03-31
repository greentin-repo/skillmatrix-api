package com.greentin.enovation.beans;

import java.util.List;

public class TeamMembersEmp {

	private String teamName;
	private Integer createdById;
	private String createdBy;
	private Integer updatedById;
	private String updatedBy;
	private String cretedDate;
	private String updatedDate;
	private List<TeamMembers> teamMemberList;


	public List<TeamMembers> getTeamMemberList() {
		return teamMemberList;
	}

	public void setTeamMemberList(List<TeamMembers> teamMemberList) {
		this.teamMemberList = teamMemberList;
	}

	public String getTeamName() {
		return teamName;
	}

	public void setTeamName(String teamName) {
		this.teamName = teamName;
	}

	public String getCretedDate() {
		return cretedDate;
	}

	public void setCretedDate(String cretedDate) {
		this.cretedDate = cretedDate;
	}

	public String getUpdatedDate() {
		return updatedDate;
	}

	public void setUpdatedDate(String updatedDate) {
		this.updatedDate = updatedDate;
	}

	public String getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}

	public String getUpdatedBy() {
		return updatedBy;
	}

	public void setUpdatedBy(String updatedBy) {
		this.updatedBy = updatedBy;
	}

	public Integer getCreatedById() {
		return createdById;
	}

	public void setCreatedById(Integer createdById) {
		this.createdById = createdById;
	}

	public Integer getUpdatedById() {
		return updatedById;
	}

	public void setUpdatedById(Integer updatedById) {
		this.updatedById = updatedById;
	}


}
