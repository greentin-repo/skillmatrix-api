package com.greentin.enovation.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

@Entity
@Table
//@javax.persistence.Cacheable
public class OrgStatusPoints {

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="id" ,unique=true, nullable=false)
	private int id;


	@ManyToOne()
	@JoinColumn(name="org_id")
	private OrganizationMaster org;

	@ManyToOne()
	@JoinColumn(name="branch_id")
	private BranchMaster branch;

	@ManyToOne()
	@JoinColumn(name="status_id")
	private StatusMaster status;


	private int roleId;

	private Integer points;

	@CreationTimestamp
	private Date createdDate;

	@UpdateTimestamp
	private Date updatedDate;

	@Transient
	private int orgId;

	@Transient
	private int branchId;

	@Transient
	private int statusId;

	@Transient
	private int subTypeId;

	public OrgStatusPoints(int id) {
		super();
		this.id = id;
	}

	public OrgStatusPoints() {
		super();
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public OrganizationMaster getOrg() {
		return org;
	}

	public void setOrg(OrganizationMaster org) {
		this.org = org;
	}

	public StatusMaster getStatus() {
		return status;
	}

	public void setStatus(StatusMaster status) {
		this.status = status;
	}

	public Integer getPoints() {
		return points;
	}

	public void setPoints(Integer points) {
		this.points = points;
	}

	public Date getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}

	public int getOrgId() {
		return orgId;
	}

	public void setOrgId(int orgId) {
		this.orgId = orgId;
	}

	public int getStatusId() {
		return statusId;
	}

	public void setStatusId(int statusId) {
		this.statusId = statusId;
	}

	public Date getUpdatedDate() {
		return updatedDate;
	}

	public void setUpdatedDate(Date updatedDate) {
		this.updatedDate = updatedDate;
	}


	public int getSubTypeId() {
		return subTypeId;
	}

	public void setSubTypeId(int subTypeId) {
		this.subTypeId = subTypeId;
	}

	public BranchMaster getBranch() {
		return branch;
	}

	public void setBranch(BranchMaster branch) {
		this.branch = branch;
	}

	public int getBranchId() {
		return branchId;
	}

	public void setBranchId(int branchId) {
		this.branchId = branchId;
	}

	public int getRoleId() {
		return roleId;
	}

	public void setRoleId(int roleId) {
		this.roleId = roleId;
	}







}
