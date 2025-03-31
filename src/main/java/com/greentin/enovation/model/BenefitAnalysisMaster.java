package com.greentin.enovation.model;

import java.sql.Timestamp;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

@Entity
@Table(name="master_benefit_analysis")
//@javax.persistence.Cacheable
public class BenefitAnalysisMaster {

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private int benfitId;
	
	private String benefits;

	private Timestamp createdDate;

	private Timestamp updatedDate;
	
	private int points;
	
	private int isEnable;

	@ManyToOne()
	@JoinColumn(name="orgId")
	private OrganizationMaster org;

	@ManyToOne()
	@JoinColumn(name="branchId")
	private BranchMaster branch;

	@ManyToOne()
	@JoinColumn(name="createdById")
	private EmployeeDetails empDetails;

	@Transient
	private int createdById;
	
	@Transient
	private int orgId;
	
	@Transient
	private int branchId;

	@Transient
	private int createdBy;
	
	@Transient
	private int updatedBy;
	
	public BenefitAnalysisMaster() {
		super();
	}

	public BenefitAnalysisMaster(int benfitId) {
		super();
		this.benfitId = benfitId;
	}

	public int getBenfitId() {
		return benfitId;
	}

	public void setBenfitId(int benfitId) {
		this.benfitId = benfitId;
	}

	public String getBenefits() {
		return benefits;
	}

	public void setBenefits(String benefits) {
		this.benefits = benefits;
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

	public EmployeeDetails getEmpDetails() {
		return empDetails;
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

	public void setEmpDetails(EmployeeDetails empDetails) {
		this.empDetails = empDetails;
	}

	public int getCreatedById() {
		return createdById;
	}

	public void setCreatedById(int createdById) {
		this.createdById = createdById;
	}

	public OrganizationMaster getOrg() {
		return org;
	}

	public void setOrg(OrganizationMaster org) {
		this.org = org;
	}

	public int getOrgId() {
		return orgId;
	}

	public void setOrgId(int orgId) {
		this.orgId = orgId;
	}

	public int getIsEnable() {
		return isEnable;
	}

	public void setIsEnable(int isEnable) {
		this.isEnable = isEnable;
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

	public int getPoints() {
		return points;
	}

	public void setPoints(int points) {
		this.points = points;
	}



}
