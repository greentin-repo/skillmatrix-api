package com.greentin.enovation.model;

import java.sql.Timestamp;
import java.util.Set;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;

@Entity
@Table(name = "master_branch")
// @javax.persistence.Cacheable
public class BranchMaster {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "branch_id", unique = true, nullable = false)
	private int branchId;

	private String name;

	private String location;

	private Timestamp createdDate;

	private Timestamp updatedDate;

	@Transient
	private int orgId;

	@Transient
	private int subscripId;

	@Transient
	private int productId;

	@ManyToOne()
	@JoinColumn(name = "org_id")
	private OrganizationMaster org;

	@OneToMany(mappedBy = "branchMaster")
	@JsonManagedReference
	private Set<BranchSetupConfig> setupConfigList;

	@Column(name = "location_code")
	private String locationCode;

	@Transient
	private int createdBy;

	@Transient
	private int updatedBy;

	@ManyToOne
	@JoinColumn(name = "created_by")
	@JsonBackReference
	private EmployeeDetails createdById;

	@ManyToOne
	@JsonIgnore
	@JoinColumn(name = "updated_by")
	private EmployeeDetails updatedById;

	public BranchMaster() {

	}

	public BranchMaster(int branchId, String name) {
		super();
		this.branchId = branchId;
		this.name = name;
	}

	public BranchMaster(int branchId) {
		super();
		this.branchId = branchId;
	}

	public int getBranchId() {
		return branchId;
	}

	public void setBranchId(int branchId) {
		this.branchId = branchId;
	}

	/*
	 * public String getName() { return name; }
	 * 
	 * public void setName(String name) { this.name = name; }
	 */

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
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

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Set<BranchSetupConfig> getSetupConfigList() {
		return setupConfigList;
	}

	public void setSetupConfigList(Set<BranchSetupConfig> setupConfigList) {
		this.setupConfigList = setupConfigList;
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

	public EmployeeDetails getCreatedById() {
		return createdById;
	}

	public void setCreatedById(EmployeeDetails createdById) {
		this.createdById = createdById;
	}

	public EmployeeDetails getUpdatedById() {
		return updatedById;
	}

	public void setUpdatedById(EmployeeDetails updatedById) {
		this.updatedById = updatedById;
	}

	public int getSubscripId() {
		return subscripId;
	}

	public void setSubscripId(int subscripId) {
		this.subscripId = subscripId;
	}

	public int getProductId() {
		return productId;
	}

	public void setProductId(int productId) {
		this.productId = productId;
	}

	public String getLocationCode() {
		return locationCode;
	}

	public void setLocationCode(String locationCode) {
		this.locationCode = locationCode;
	}
	

}
