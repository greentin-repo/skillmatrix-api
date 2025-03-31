package com.greentin.enovation.dto;

public class ChartData {

	private String data;
	private String categoryName;
	private Double value;
	private int statusId;
	private int totalEmployes;
	private int participatedEmployes;
    private int deptId;

    private String Series;

	private String label;

	public ChartData() {
		super();
	}
	public ChartData(String data, String category, Double value) {
		this.data = data;
		this.categoryName = category;
		this.value = value;
	}
	public ChartData(String data, String category, Double value, int totalEmployes, int participatedEmployes,int deptId) {
		this.data = data;
		this.categoryName = category;
		this.value = value;
		this.totalEmployes=totalEmployes;
		this.participatedEmployes=participatedEmployes;
		this.deptId=deptId;
	}
	public ChartData(String category, Double value) {
		this.categoryName = category;
		this.value = value;
	}

	public ChartData(String categoryName) {
		super();
		this.categoryName = categoryName;
	}
	
	public ChartData(String data, String categoryName, Double value, int statusId) {
		super();
		this.data = data;
		this.categoryName = categoryName;
		this.value = value;
		this.statusId = statusId;
	}
	/**
	 * @return the data
	 */
	public String getData() {
		return data;
	}

	/**
	 * @param data the data to set
	 */
	public void setData(String data) {
		this.data = data;
	}

	/**
	 * @return the categoryName
	 */
	public String getCategoryName() {
		return categoryName;
	}

	/**
	 * @param categoryName the categoryName to set
	 */
	public void setCategoryName(String categoryName) {
		this.categoryName = categoryName;
	}

	/**
	 * @return the value
	 */
	public Double getValue() {
		return value;
	}

	/**
	 * @param value the value to set
	 */
	public void setValue(Double value) {
		this.value = value;
	}
	public int getStatusId() {
		return statusId;
	}
	public void setStatusId(int statusId) {
		this.statusId = statusId;
	}
	public int getTotalEmployes() {
		return totalEmployes;
	}
	public void setTotalEmployes(int totalEmployes) {
		this.totalEmployes = totalEmployes;
	}
	public int getParticipatedEmployes() {
		return participatedEmployes;
	}
	public void setParticipatedEmployes(int participatedEmployes) {
		this.participatedEmployes = participatedEmployes;
	}
	public int getDeptId() {
		return deptId;
	}
	public void setDeptId(int deptId) {
		this.deptId = deptId;
	}
	public String getSeries() {
		return Series;
	}
	public void setSeries(String series) {
		Series = series;
	}
	public String getLabel() {
		return label;
	}
	public void setLabel(String label) {
		this.label = label;
	}

}
