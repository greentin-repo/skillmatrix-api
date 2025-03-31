package com.greentin.enovation.model;

import java.sql.Timestamp;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;

@Entity
@Table(name="master_category")
//@javax.persistence.Cacheable
public class CategoryMaster {

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private int catId;

	private String categoryName;

	private Timestamp createdDate;

	private String impactType; // POSITIVE or NEGATIVE or N/A
	
	private Timestamp updatedDate;

	@ManyToOne()
	@JoinColumn(name="org_id")
	private OrganizationMaster organisation;

	@ManyToOne()
	@JoinColumn(name="branch_id")
	private BranchMaster branch;

	@Transient
	private int createdBy;

	@Transient
	private int updatedBy;

	@ManyToOne
	@JsonIgnore
	@JoinColumn(name = "created_by")
	private EmployeeDetails createdById;

	@ManyToOne
	@JsonIgnore
	@JoinColumn(name = "updated_by")
	private EmployeeDetails updatedById;

	@OneToMany(mappedBy="category",fetch=FetchType.LAZY)
	@JsonManagedReference
	List<Particulars> particulersList;
	
	
	private String isFCVRequired;
	
	
	private String processType;

	
	@Column(name = "is_sus_audit_required")
	private String isSusAuditRequired;
	
	
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

	@Transient
	private int orgId;

	@Transient
	private int branchId;

	public CategoryMaster() {
		super();
	}

	public CategoryMaster(int catId) {
		super();
		this.catId = catId;
	}

	public int getCatId() {
		return catId;
	}
	public void setCatId(int catId) {
		this.catId = catId;
	}
	public String getCategoryName() {
		return categoryName;
	}
	public void setCategoryName(String categoryName) {
		this.categoryName = categoryName;
	}

	public Timestamp getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(Timestamp createdDate) {
		this.createdDate = createdDate;
	}

	public OrganizationMaster getOrganisation() {
		return organisation;
	}

	public void setOrganisation(OrganizationMaster organisation) {
		this.organisation = organisation;
	}

	public int getOrgId() {
		return orgId;
	}

	public void setOrgId(int orgId) {
		this.orgId = orgId;
	}

	public Timestamp getUpdatedDate() {
		return updatedDate;
	}

	public void setUpdatedDate(Timestamp updatedDate) {
		this.updatedDate = updatedDate;
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

	public List<Particulars> getParticulersList() {
		return particulersList;
	}

	public void setParticulersList(List<Particulars> particulersList) {
		this.particulersList = particulersList;
	}

	public String getProcessType() {
		return processType;
	}

	public void setProcessType(String processType) {
		this.processType = processType;
	}

	public String getIsFCVRequired() {
		return isFCVRequired;
	}

	public void setIsFCVRequired(String isFCVRequired) {
		this.isFCVRequired = isFCVRequired;
	}

	public String getIsSusAuditRequired() {
		return isSusAuditRequired;
	}

	public void setIsSusAuditRequired(String isSusAuditRequired) {
		this.isSusAuditRequired = isSusAuditRequired;
	}

	public String getImpactType() {
		return impactType;
	}

	public void setImpactType(String impactType) {
		this.impactType = impactType;
	}
	
	
}
