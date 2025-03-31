package com.greentin.enovation.beans;

import java.sql.Timestamp;
import java.util.List;

import com.greentin.enovation.model.EmployeeDetails;
import com.greentin.enovation.model.TeamtypeMaster;

public class TeamDetailsDTO {
	private int teamId; 
	private int status;
	private String teamName;
	private TeamtypeMaster team_type_master;
	private EmployeeDetails created_by_details;
	private EmployeeDetails updated_by_details;
	private Timestamp createdDate,updatedDate;
	private List<EmployeeDetails> TeamMembersDet;

	public int getTeamId() {
		return teamId;
	}
	public void setTeamId(int teamId) {
		this.teamId = teamId;
	}
	public int getStatus() {
		return status;
	}
	public void setStatus(int status) {
		this.status = status;
	}
	public String getTeamName() {
		return teamName;
	}
	public void setTeamName(String teamName) {
		this.teamName = teamName;
	}
	public TeamtypeMaster getTeam_type_master() {
		return team_type_master;
	}
	public void setTeam_type_master(TeamtypeMaster team_type_master) {
		this.team_type_master = team_type_master;
	}
	public EmployeeDetails getCreated_by_details() {
		return created_by_details;
	}
	public void setCreated_by_details(EmployeeDetails created_by_details) {
		this.created_by_details = created_by_details;
	}
	public EmployeeDetails getUpdated_by_details() {
		return updated_by_details;
	}
	public void setUpdated_by_details(EmployeeDetails updated_by_details) {
		this.updated_by_details = updated_by_details;
	}
	public Timestamp getCreatedDate() {
		return createdDate;
	}
	public void setCreatedDate(Timestamp createdDate) {
		this.createdDate = createdDate;
	}
	public Timestamp getUpdatedDate() {
		return updatedDate;
	}
	public void setUpdatedDate(Timestamp updatedDate) {
		this.updatedDate = updatedDate;
	}
	public List<EmployeeDetails> getTeamMembersDet() {
		return TeamMembersDet;
	}
	public void setTeamMembersDet(List<EmployeeDetails> teamMembersDet) {
		TeamMembersDet = teamMembersDet;
	}

}
