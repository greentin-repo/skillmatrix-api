package com.greentin.enovation.dto;

import java.util.List;

public class SuggestionCount {

	private double count;
	private String DeptName;
	private String catName;
	private String year;
	private String month;
	private String statusLabel;
	private String label;
	private double noOfSugesstion;
	private double implementationCost;
	private double savingCost;
	private String branchName;
	List<SuggestionCount> list;
	private String monthYear;

	public String getStatusLabel() {
		return statusLabel;
	}

	public void setStatusLabel(String statusLabel) {
		this.statusLabel = statusLabel;
	}

	public SuggestionCount() {
		super();
	}

	public SuggestionCount(double count, List<SuggestionCount> list) {
		super();
		this.count = count;
		this.list = list;
	}

	public SuggestionCount(double count, String deptName, String catName, String year, String month) {
		this.count = count;
		this.catName = catName;
		this.year = year;
		this.month = month;
	}

	public SuggestionCount(double count, String catName, String month) {
		this.count = count;
		this.catName = catName;
		this.month = month;
	}

	public double getCount() {
		return count;
	}

	public void setCount(double count) {
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

	public String getLabel() {
		return label;
	}

	public void setLabel(String label) {
		this.label = label;
	}

	public double getNoOfSugesstion() {
		return noOfSugesstion;
	}

	public void setNoOfSugesstion(double noOfSugesstion) {
		this.noOfSugesstion = noOfSugesstion;
	}

	public double getImplementationCost() {
		return implementationCost;
	}

	public void setImplementationCost(double implementationCost) {
		this.implementationCost = implementationCost;
	}

	public double getSavingCost() {
		return savingCost;
	}

	public void setSavingCost(double savingCost) {
		this.savingCost = savingCost;
	}

	public String getBranchName() {
		return branchName;
	}

	public void setBranchName(String branchName) {
		this.branchName = branchName;
	}

	public List<SuggestionCount> getList() {
		return list;
	}

	public void setList(List<SuggestionCount> list) {
		this.list = list;
	}

	public String getMonthYear() {
		return monthYear;
	}

	public void setMonthYear(String monthYear) {
		this.monthYear = monthYear;
	}

}
