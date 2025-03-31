package com.greentin.enovation.beans;

import java.util.ArrayList;
import java.util.List;

public class Branch {
	private Integer branchId;
	private String name;
	private String location;
	List<DepartmentList> departmentList = new ArrayList<>();
	private int deptId;
	private String deptName; 


	public Branch() {
		super();
	}
	public Branch(Integer branchId, String name, String location) {
		super();
		this.branchId = branchId;
		this.name = name;
		this.location = location;
	}
	public Integer getBranchId() {
		return branchId;
	}
	public void setBranchId(Integer branchId) {
		this.branchId = branchId;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getLocation() {
		return location;
	}
	public void setLocation(String location) {
		this.location = location;
	}

	public List<DepartmentList> getDepartmentList() {
		return departmentList;
	}
	public void setDepartmentList(List<DepartmentList> departmentList) {
		this.departmentList = departmentList;
	}

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

}
