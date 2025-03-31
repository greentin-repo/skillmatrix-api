package com.greentin.enovation.dto;

public class ConcernCount {

	private int count;
	private String DeptName;
	private String catName;
	private String year;
	private String month;
	
	public ConcernCount() {
		
	}
	
	public ConcernCount(int count, String deptName, String catName, String year, String month) {
		this.count = count;
		this.catName = catName;
		this.year = year;
		this.month = month;
	}
	
	public ConcernCount(int count, String catName, String month) {
		this.count = count;
		this.catName = catName;
		this.month = month;
	}

	public int getCount() {
		return count;
	}
	public void setCount(int count) {
		this.count = count;
	}
	public String getDeptName() {
		return DeptName;
	}
	public void setDeptName(String deptName) {
		DeptName = deptName;
	}
	public String getCatName() {
		return catName;
	}
	public void setCatName(String catName) {
		this.catName = catName;
	}
	public String getYear() {
		return year;
	}
	public void setYear(String year) {
		this.year = year;
	}
	public String getMonth() {
		return month;
	}
	public void setMonth(String month) {
		this.month = month;
	}
	
}
