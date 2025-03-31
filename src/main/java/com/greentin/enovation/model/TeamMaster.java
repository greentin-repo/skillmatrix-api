package com.greentin.enovation.model;
import java.sql.Timestamp;
import java.util.Set;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.fasterxml.jackson.annotation.JsonManagedReference;

@Entity
@Table(name="master_team")
public class TeamMaster {
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private int teammasterId;

	private String teamName;

	private int isDeactive;

	private Timestamp createdDate;

	private Timestamp updatedDate;

	@OneToMany(mappedBy="teamDetailsMaster",cascade=CascadeType.ALL)
	@JsonManagedReference
	private Set<TeamDetails> teamList;

	@ManyToOne()
	@JoinColumn(name="org_id")
	private OrganizationMaster org;

	@ManyToOne()
	@JoinColumn(name="branch_id")
	private BranchMaster branch;

	@Transient
	private int orgId;

	@Transient
	private int branchId;

	public TeamMaster() {
		super();
	}


	public TeamMaster(int teammasterId) {
		super();
		this.teammasterId = teammasterId;
	}
	public int getTeammasterId() {
		return teammasterId;
	}
	public void setTeammasterId(int teammasterId) {
		this.teammasterId = teammasterId;
	}
	public String getTeamName() {
		return teamName;
	}
	public void setTeamName(String teamName) {
		this.teamName = teamName;
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

	public Set<TeamDetails> getTeamList() {
		return teamList;
	}
	public void setTeamList(Set<TeamDetails> teamList) {
		this.teamList = teamList;
	}


	public OrganizationMaster getOrg() {
		return org;
	}


	public void setOrg(OrganizationMaster org) {
		this.org = org;
	}


	public BranchMaster getBranch() {
		return branch;
	}


	public void setBranch(BranchMaster branch) {
		this.branch = branch;
	}


	public int getOrgId() {
		return orgId;
	}


	public void setOrgId(int orgId) {
		this.orgId = orgId;
	}


	public int getBranchId() {
		return branchId;
	}


	public void setBranchId(int branchId) {
		this.branchId = branchId;
	}


	public int getIsDeactive() {
		return isDeactive;
	}


	public void setIsDeactive(int isDeactive) {
		this.isDeactive = isDeactive;
	}





}
