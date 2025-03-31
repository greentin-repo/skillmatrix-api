package com.greentin.enovation.beans;

import com.greentin.enovation.bo.model.EmployeeDetailsBO;

public class MyMenu {

	private int menuId;
	private String menuName;
	private int subMenuId;
	private String subMenuName;
	private int statusId;

	private int roleAccessId;
	private int empId;
	private long userAccessId;
	
	
	private String accessType;  //ROLE or USER
	
	private long roleId;
	
	private EmployeeDetailsBO emp;
	
   public int getMenuId() {
		return menuId;
	}
	public void setMenuId(int menuId) {
		this.menuId = menuId;
	}
	public String getMenuName() {
		return menuName;
	}
	public void setMenuName(String menuName) {
		this.menuName = menuName;
	}
	public int getSubMenuId() {
		return subMenuId;
	}
	public void setSubMenuId(int subMenuId) {
		this.subMenuId = subMenuId;
	}
	public String getSubMenuName() {
		return subMenuName;
	}
	public void setSubMenuName(String subMenuName) {
		this.subMenuName = subMenuName;
	}
	public int getStatusId() {
		return statusId;
	}
	public void setStatusId(int statusId) {
		this.statusId = statusId;
	}

	public int getRoleAccessId() {
		return roleAccessId;
	}
	public void setRoleAccessId(int roleAccessId) {
		this.roleAccessId = roleAccessId;
	}
	public EmployeeDetailsBO getEmp() {
		return emp;
	}
	public void setEmp(EmployeeDetailsBO emp) {
		this.emp = emp;
	}
	public int getEmpId() {
		return empId;
	}
	public void setEmpId(int empId) {
		this.empId = empId;
	}
	public long getUserAccessId() {
		return userAccessId;
	}
	public void setUserAccessId(long userAccessId) {
		this.userAccessId = userAccessId;
	}
	public long getRoleId() {
		return roleId;
	}
	public void setRoleId(long roleId) {
		this.roleId = roleId;
	}
	public String getAccessType() {
		return accessType;
	}
	public void setAccessType(String accessType) {
		this.accessType = accessType;
	}	

}
