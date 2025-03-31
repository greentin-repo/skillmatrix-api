package com.greentin.enovation.model;
import java.sql.Date;
import java.sql.Timestamp;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;
import com.fasterxml.jackson.annotation.JsonBackReference;

@Entity
@Table(name="tbl_team_details")
public class TeamDetails {
	//@CreationTimestamp 
	  
	
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private int teamId; 

	private int status;

	private String teamName;

	private Date deactivatedDate;

	private int deactivatedBy;

	private int isActive;

	@ManyToOne()
	@JoinColumn(name = "team_type_id")
	private TeamtypeMaster teamMaster;

	@ManyToOne()
	@JoinColumn(name = "emp_id")

	private EmployeeDetails empDetails;

	@ManyToOne()
	@JoinColumn(name = "created_by")
	private EmployeeDetails created_by_details;

	@ManyToOne(cascade=CascadeType.ALL,fetch=FetchType.EAGER)
	@JoinColumn(name = "updated_by")
	private EmployeeDetails upDatedByDetails;

	private Timestamp createdDate;

	private Timestamp updatedDate;


	@ManyToOne()
	@JoinColumn(name = "teamMasterId")
	@JsonBackReference
	private TeamMaster teamDetailsMaster;

	@Transient
	List<EmployeeDetails> teamMembersList;

	public int getStatus() {
		return status;
	}
	public void setStatus(int status) {
		this.status = status;
	}
	public int getTeamId() {
		return teamId;
	}
	public void setTeamId(int teamId) {
		this.teamId = teamId;
	}

	public String getTeamName() {
		return teamName;
	}
	public void setTeamName(String teamName) {
		this.teamName = teamName;
	}

	/*public TeamtypeMaster getTeam_type_master() {
		return team_type_master;
	}
	public void setTeam_type_master(TeamtypeMaster team_type_master) {
		this.team_type_master = team_type_master;
	}

	public EmployeeDetails getEmp_details() {
		return emp_details;
	}
	public void setEmp_details(EmployeeDetails emp_details) {
		this.emp_details = emp_details;
	}*/
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
	public EmployeeDetails getCreated_by_details() {
		return created_by_details;
	}
	public void setCreated_by_details(EmployeeDetails created_by_details) {
		this.created_by_details = created_by_details;
	}
	/*public EmployeeDetails getUpdated_by_details() {
		return updated_by_details;
	}
	public void setUpdated_by_details(EmployeeDetails updated_by_details) {
		this.updated_by_details = updated_by_details;
	}*/
	public TeamMaster getTeamDetailsMaster() {
		return teamDetailsMaster;
	}
	public void setTeamDetailsMaster(TeamMaster teamDetailsMaster) {
		this.teamDetailsMaster = teamDetailsMaster;
	}
	public TeamtypeMaster getTeamMaster() {
		return teamMaster;
	}
	public void setTeamMaster(TeamtypeMaster teamMaster) {
		this.teamMaster = teamMaster;
	}
	public EmployeeDetails getEmpDetails() {
		return empDetails;
	}
	public void setEmpDetails(EmployeeDetails empDetails) {
		this.empDetails = empDetails;
	}
	public EmployeeDetails getUpDatedByDetails() {
		return upDatedByDetails;
	}
	public void setUpDatedByDetails(EmployeeDetails upDatedByDetails) {
		this.upDatedByDetails = upDatedByDetails;
	}
	public Date getDeactivatedDate() {
		return deactivatedDate;
	}
	public void setDeactivatedDate(Date deactivatedDate) {
		this.deactivatedDate = deactivatedDate;
	}
	public int getDeactivatedBy() {
		return deactivatedBy;
	}
	public void setDeactivatedBy(int deactivatedBy) {
		this.deactivatedBy = deactivatedBy;
	}
	public int getIsActive() {
		return isActive;
	}
	public void setIsActive(int isActive) {
		this.isActive = isActive;
	}



}
