package com.greentin.enovation.model;



import java.sql.Timestamp;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import javax.persistence.Table;
import javax.persistence.Transient;


@Entity
@Table(name="master_teamtype")
//@javax.persistence.Cacheable
public class TeamtypeMaster {

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="team_type_id" ,unique=true, nullable=false)
	private int teamTypeId;

	private String type;

	private Timestamp createdDate;

	private Timestamp updatedDate;

	@Transient
	private String fromDate;

	@Transient
	private String toDate;

	@Transient
	private int orgId;

	@Transient
	private int  branchId;

	@Transient
	private int  deptId;
	
	@Transient
	private int  lineId;

	@Transient
	private List<Integer> statusIds;


	@Transient
	private List<Integer> implStatusIds;

	public List<Integer> getImplStatusIds() {
		return implStatusIds;
	}
	public void setImplStatusIds(List<Integer> implStatusIds) {
		this.implStatusIds = implStatusIds;
	}
	public void setStatusIds(List<Integer> statusIds) {
		this.statusIds = statusIds;
	}
	public TeamtypeMaster() {
		super();
	}
	public TeamtypeMaster(int teamTypeId) {
		super();
		this.teamTypeId = teamTypeId;
	}

	public int getTeamTypeId() {
		return teamTypeId;
	}
	public void setTeamTypeId(int teamTypeId) {
		this.teamTypeId = teamTypeId;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
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
	public String getFromDate() {
		return fromDate;
	}
	public void setFromDate(String fromDate) {
		this.fromDate = fromDate;
	}
	public String getToDate() {
		return toDate;
	}
	public void setToDate(String toDate) {
		this.toDate = toDate;
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

	public List<Integer> getStatusIds() {
		return statusIds;
	}
	public int getDeptId() {
		return deptId;
	}
	public void setDeptId(int deptId) {
		this.deptId = deptId;
	}
	public int getLineId() {
		return lineId;
	}
	public void setLineId(int lineId) {
		this.lineId = lineId;
	}
}
