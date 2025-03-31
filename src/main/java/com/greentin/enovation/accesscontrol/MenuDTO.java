package com.greentin.enovation.accesscontrol;

import java.util.List;

import com.greentin.enovation.model.UserMenuAccess;

public class MenuDTO {
		
	
	private int id;

	private String menuName;

	private Integer status;
	
	private List<UserMenuAccess> userAccessList;
	

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getMenuName() {
		return menuName;
	}

	public void setMenuName(String menuName) {
		this.menuName = menuName;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public List<UserMenuAccess> getUserAccessList() {
		return userAccessList;
	}

	public void setUserAccessList(List<UserMenuAccess> userAccessList) {
		this.userAccessList = userAccessList;
	}
	
	
}
