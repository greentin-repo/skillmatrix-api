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
@Table(name = "master_department")
// @javax.persistence.Cacheable
public class DepartmentMaster {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int deptId;

	private String deptName;

	private int isDeptForMaintenance;

	private int isStaffPerson;

	private Timestamp createdDate;

	private Timestamp updatedDate;

	@ManyToOne()
	@JoinColumn(name = "org_id")
	private OrganizationMaster organisation;

	@ManyToOne()
	@JoinColumn(name = "branch_id")
	private BranchMaster branch;

	@Column(name = "dept_code")
	private String deptCode;

	@Transient
	private int orgId;

	@Transient
	private int branchId;

	@Transient
	private int createdBy;

	@Transient
	private int updatedBy;

	private int createdById;

	private int updatedById;

	public DepartmentMaster() {

	}

	public DepartmentMaster(int deptId, String deptName) {
		super();
		this.deptId = deptId;
		this.deptName = deptName;
	}

	public DepartmentMaster(int deptId) {
		super();
		this.deptId = deptId;
	}

	public int getDeptId() {
		return deptId;
	}

	public void setDeptId(int deptId) {
		this.deptId = deptId;
	}

	public String getDeptName() {
		return deptName;
	}

	public void setDeptName(String deptName) {
		this.deptName = deptName;
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

	public int getOrgId() {
		return orgId;
	}

	public void setOrgId(int orgId) {
		this.orgId = orgId;
	}

	public OrganizationMaster getOrganisation() {
		return organisation;
	}

	public void setOrganisation(OrganizationMaster organisation) {
		this.organisation = organisation;
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

	public int getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(int createdBy) {
		this.createdBy = createdBy;
	}

	public int getUpdatedBy() {
		return updatedBy;
	}

	public void setUpdatedBy(int updatedBy) {
		this.updatedBy = updatedBy;
	}

	public int getCreatedById() {
		return createdById;
	}

	public void setCreatedById(int createdById) {
		this.createdById = createdById;
	}

	public int getUpdatedById() {
		return updatedById;
	}

	public void setUpdatedById(int updatedById) {
		this.updatedById = updatedById;
	}

	public int getIsDeptForMaintenance() {
		return isDeptForMaintenance;
	}

	public void setIsDeptForMaintenance(int isDeptForMaintenance) {
		this.isDeptForMaintenance = isDeptForMaintenance;
	}

	public int getIsStaffPerson() {
		return isStaffPerson;
	}

	public void setIsStaffPerson(int isStaffPerson) {
		this.isStaffPerson = isStaffPerson;
	}

	public String getDeptCode() {
		return deptCode;
	}

	public void setDeptCode(String deptCode) {
		this.deptCode = deptCode;
	}

}
