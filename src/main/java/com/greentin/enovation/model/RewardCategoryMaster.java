package com.greentin.enovation.model;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

@Entity
public class RewardCategoryMaster {
	
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private int id;
	
	private String categoryName;
	
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="org_id")
	private OrganizationMaster org;
	
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="branch_id")
	private BranchMaster branch;
	
	private String description;
	
	private int isEnable;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getCategoryName() {
		return categoryName;
	}

	public void setCategoryName(String categoryName) {
		this.categoryName = categoryName;
	}

	public RewardCategoryMaster() {
		super();
	}

	public RewardCategoryMaster(int id) {
		super();
		this.id = id;
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

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public int getIsEnable() {
		return isEnable;
	}

	public void setIsEnable(int isEnable) {
		this.isEnable = isEnable;
	}
	
	

}
