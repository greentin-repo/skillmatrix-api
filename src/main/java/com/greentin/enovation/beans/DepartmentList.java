package com.greentin.enovation.beans;

public class DepartmentList {
	private int deptId;
	private String deptName;
	public int getDeptId() {
		return deptId;
	}
	public void setDeptId(int deptId) {
		this.deptId = deptId;
	}
	public String getDeptName() {
		return deptName;
	}
	public void setDeptName(String deptName) {
		this.deptName = deptName;
	}

	public DepartmentList() {
		super();
	}
	public DepartmentList(int deptId, String deptName) {
		super();
		this.deptId = deptId;
		this.deptName = deptName;
	}

}
