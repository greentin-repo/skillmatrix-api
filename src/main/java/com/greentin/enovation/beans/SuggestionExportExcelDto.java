package com.greentin.enovation.beans;


public class SuggestionExportExcelDto {

	private int sugId;
	
	private String cmpyEmpId;
	
	private String mobile;
	
	private String deptName;
	
	private String role;
	
	private String count;

	public int getSugId() {
		return sugId;
	}

	public void setSugId(int sugId) {
		this.sugId = sugId;
	}

	public String getCmpyEmpId() {
		return cmpyEmpId;
	}

	public void setCmpyEmpId(String cmpyEmpId) {
		this.cmpyEmpId = cmpyEmpId;
	}

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	public String getDeptName() {
		return deptName;
	}

	public void setDeptName(String deptName) {
		this.deptName = deptName;
	}

	public String getRole() {
		return role;
	}

	public void setRole(String role) {
		this.role = role;
	}

	public String getCount() {
		return count;
	}

	public void setCount(String count) {
		this.count = count;
	}
	
}
