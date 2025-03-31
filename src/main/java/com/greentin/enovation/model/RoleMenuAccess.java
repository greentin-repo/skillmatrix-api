package com.greentin.enovation.model;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.greentin.enovation.accesscontrol.Menu;
import com.greentin.enovation.accesscontrol.Role;
import com.greentin.enovation.accesscontrol.SubMenu;

@Entity
@Table
//@javax.persistence.Cacheable
public class RoleMenuAccess {

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private int id;

	
	private int isActive;

	@ManyToOne()
	@JoinColumn(name="org_id")
	@JsonIgnore
	private OrganizationMaster org;

	@ManyToOne()
	@JoinColumn(name="branch_id")
	@JsonIgnore
	private BranchMaster branch;

	@ManyToOne()
	@JoinColumn(name="role_id")
	private Role role;

	@ManyToOne()
	@JoinColumn(name="menu_id")
	private Menu  menu;

	@ManyToOne()
	@JoinColumn(name="submenu_id")
	private SubMenu submenu;

	private Date createdDate;

	private Date updatedDate;

	@Transient
	private int orgId;


	@Transient
	private int branchId;

	@Transient
	private int roleId;

	@Transient
	private int menuId;

	@Transient
	private int submenuId;
	

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getIsActive() {
		return isActive;
	}

	public void setIsActive(int isActive) {
		this.isActive = isActive;
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

	public Role getRole() {
		return role;
	}

	public void setRole(Role role) {
		this.role = role;
	}

	public Menu getMenu() {
		return menu;
	}

	public void setMenu(Menu menu) {
		this.menu = menu;
	}

	public SubMenu getSubmenu() {
		return submenu;
	}

	public void setSubmenu(SubMenu submenu) {
		this.submenu = submenu;
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

	public int getRoleId() {
		return roleId;
	}

	public void setRoleId(int roleId) {
		this.roleId = roleId;
	}

	public int getMenuId() {
		return menuId;
	}

	public void setMenuId(int menuId) {
		this.menuId = menuId;
	}

	public int getSubmenuId() {
		return submenuId;
	}

	public void setSubmenuId(int submenuId) {
		this.submenuId = submenuId;
	}

	public Date getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}

	public Date getUpdatedDate() {
		return updatedDate;
	}

	public void setUpdatedDate(Date updatedDate) {
		this.updatedDate = updatedDate;
	}



}
