package com.greentin.enovation.model;
import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

@Entity
@Table(name="status_configration")
//@javax.persistence.Cacheable
public class StatusConfig {

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="id" ,unique=true, nullable=false)
	private int id;

	private String status;

	private String label;

	@ManyToOne()
	@JoinColumn(name = "orgId")
	private OrganizationMaster orgMaster;

	@ManyToOne()
	@JoinColumn(name = "assignToId")
	private EmployeeDetails assignTo;

	@ManyToOne()
	@JoinColumn(name = "assignToTeamId")
	private TeamMaster assignToTeam;
	
	private Timestamp createdDate;
	
	private Timestamp updatedDate;

	@Transient
	private int orgId;

	@Transient
	private int assignToId;

	@Transient
	private int assignToTeamId;


	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getLabel() {
		return label;
	}

	public void setLabel(String label) {
		this.label = label;
	}

	public OrganizationMaster getOrgMaster() {
		return orgMaster;
	}

	public void setOrgMaster(OrganizationMaster orgMaster) {
		this.orgMaster = orgMaster;
	}

	public EmployeeDetails getAssignTo() {
		return assignTo;
	}

	public void setAssignTo(EmployeeDetails assignTo) {
		this.assignTo = assignTo;
	}



	public TeamMaster getAssignToTeam() {
		return assignToTeam;
	}

	public void setAssignToTeam(TeamMaster assignToTeam) {
		this.assignToTeam = assignToTeam;
	}

	public int getOrgId() {
		return orgId;
	}

	public void setOrgId(int orgId) {
		this.orgId = orgId;
	}

	public int getAssignToId() {
		return assignToId;
	}

	public void setAssignToId(int assignToId) {
		this.assignToId = assignToId;
	}

	public int getAssignToTeamId() {
		return assignToTeamId;
	}

	public void setAssignToTeamId(int assignToTeamId) {
		this.assignToTeamId = assignToTeamId;
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



}
