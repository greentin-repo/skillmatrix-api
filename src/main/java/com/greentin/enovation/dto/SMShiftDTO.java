package com.greentin.enovation.dto;

import java.util.HashMap;
import java.util.List;

public class SMShiftDTO {

	private long id;

	private long shiftId;
	
	private String shiftName;
	
	private int branchId;

	private int deptId;
	
	List<HashMap<String, Object>> empList;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public long getShiftId() {
		return shiftId;
	}

	public void setShiftId(long shiftId) {
		this.shiftId = shiftId;
	}

	public String getShiftName() {
		return shiftName;
	}

	public void setShiftName(String shiftName) {
		this.shiftName = shiftName;
	}

	public List<HashMap<String, Object>> getEmpList() {
		return empList;
	}

	public void setEmpList(List<HashMap<String, Object>> empList) {
		this.empList = empList;
	}

	public int getBranchId() {
		return branchId;
	}

	public void setBranchId(int branchId) {
		this.branchId = branchId;
	}

	public int getDeptId() {
		return deptId;
	}

	public void setDeptId(int deptId) {
		this.deptId = deptId;
	}
	
}
