package com.greentin.enovation.dto;

import java.util.List;

import com.greentin.enovation.model.CategoryMaster;
import com.greentin.enovation.model.DepartmentMaster;

public class MasterDTO {

	private List<CategoryMaster> categoryList;

	private int branchId;

	private int deptId;

	private String fromDt;

	private String toDt;

	private int rating;

	private String moodIndicatorDate;

	private String empName;

	private String cmpEmpId;

	private String deptName;

	private String branchName;

	private int limit;

	private int offset;
	
	private String isSusAuditRequired;

	List<DepartmentMaster> deptList;

	public List<DepartmentMaster> getDeptList() {
		return deptList;
	}

	public void setDeptList(List<DepartmentMaster> deptList) {
		this.deptList = deptList;
	}

	public List<CategoryMaster> getCategoryList() {
		return categoryList;
	}

	public void setCategoryList(List<CategoryMaster> categoryList) {
		this.categoryList = categoryList;
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

	public String getFromDt() {
		return fromDt;
	}

	public void setFromDt(String fromDt) {
		this.fromDt = fromDt;
	}

	public String getToDt() {
		return toDt;
	}

	public void setToDt(String toDt) {
		this.toDt = toDt;
	}

	public int getRating() {
		return rating;
	}

	public void setRating(int rating) {
		this.rating = rating;
	}

	public String getMoodIndicatorDate() {
		return moodIndicatorDate;
	}

	public void setMoodIndicatorDate(String moodIndicatorDate) {
		this.moodIndicatorDate = moodIndicatorDate;
	}

	public String getEmpName() {
		return empName;
	}

	public void setEmpName(String empName) {
		this.empName = empName;
	}

	public String getCmpEmpId() {
		return cmpEmpId;
	}

	public void setCmpEmpId(String cmpEmpId) {
		this.cmpEmpId = cmpEmpId;
	}

	public String getDeptName() {
		return deptName;
	}

	public void setDeptName(String deptName) {
		this.deptName = deptName;
	}

	public String getBranchName() {
		return branchName;
	}

	public void setBranchName(String branchName) {
		this.branchName = branchName;
	}

	public int getLimit() {
		return limit;
	}

	public void setLimit(int limit) {
		this.limit = limit;
	}

	public int getOffset() {
		return offset;
	}

	public void setOffset(int offset) {
		this.offset = offset;
	}

	public String getIsSusAuditRequired() {
		return isSusAuditRequired;
	}

	public void setIsSusAuditRequired(String isSusAuditRequired) {
		this.isSusAuditRequired = isSusAuditRequired;
	}
	

}
