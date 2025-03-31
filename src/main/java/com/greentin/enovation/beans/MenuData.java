package com.greentin.enovation.beans;

import java.util.List;

public class MenuData {
	
	
	private int menuId;
	private String menuName;
	private List<MyMenu> subMenuList;
	
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
	public List<MyMenu> getSubMenuList() {
		return subMenuList;
	}
	public void setSubMenuList(List<MyMenu> subMenuList) {
		this.subMenuList = subMenuList;
	}
}
