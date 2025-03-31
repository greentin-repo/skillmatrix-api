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

import com.greentin.enovation.accesscontrol.Menu;

@Entity
@Table(name = "module_subscription")
public class ModuleSubscription {

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private long id;
	
	@ManyToOne
	@JoinColumn(name = "menu_id")
	private Menu menu;
	
	private int branchId;
	
	private int orgId;
	
	private Timestamp createdDate;
	
	private Timestamp updatedDate;
	
	private int createdBy;
	
	private int updatedBy;
	
	@Column(length = 50)
	private String isActive;
	
	

	public ModuleSubscription(Menu menu, int branchId, int orgId, String isActive) {
		super();
		this.menu = menu;
		this.branchId = branchId;
		this.orgId = orgId;
		this.isActive = isActive;
	}

	public ModuleSubscription(long id) {
		super();
		this.id = id;
	}

	public ModuleSubscription() {
		super();
	}

	public int getBranchId() {
		return branchId;
	}

	public void setBranchId(int branchId) {
		this.branchId = branchId;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public Menu getMenu() {
		return menu;
	}

	public void setMenu(Menu menu) {
		this.menu = menu;
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

	public String getIsActive() {
		return isActive;
	}

	public void setIsActive(String isActive) {
		this.isActive = isActive;
	}
	
	
	
	
	
	
}
