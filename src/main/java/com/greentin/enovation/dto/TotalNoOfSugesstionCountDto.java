package com.greentin.enovation.dto;

public class TotalNoOfSugesstionCountDto {

	private double noOfSugesstion;

	private int filterId;

	private String filterName;

	private int count;

	private int id;

	private String name;

	private String label;

	private String statusLabel;

	private int typeId;

	private String status;

	private int noOfConcerns;

	private Integer sugPercentage;

	private String month;

	private String year;

	private int roleId;

	private String createdDate;
	
	private int nominationId;
	

	public TotalNoOfSugesstionCountDto() {
		super();
	}


	public TotalNoOfSugesstionCountDto(double noOfSugesstion, int filterId, String filterName) {
		super();
		this.noOfSugesstion = noOfSugesstion;
		this.filterId = filterId;
		this.filterName = filterName;
	}
	public TotalNoOfSugesstionCountDto(double noOfSugesstion, int filterId, String filterName, int nominationId) {
		super();
		this.noOfSugesstion = noOfSugesstion;
		this.filterId = filterId;
		this.filterName = filterName;
		this.nominationId=nominationId;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public double getNoOfSugesstion() {
		return noOfSugesstion;
	}

	public void setNoOfSugesstion(double noOfSugesstion) {
		this.noOfSugesstion = noOfSugesstion;
	}

	public String getLabel() {
		return label;
	}

	public void setLabel(String label) {
		this.label = label;
	}

	public String getStatusLabel() {
		return statusLabel;
	}

	public void setStatusLabel(String statusLabel) {
		this.statusLabel = statusLabel;
	}

	public int getTypeId() {
		return typeId;
	}

	public void setTypeId(int typeId) {
		this.typeId = typeId;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getFilterName() {
		return filterName;
	}

	public void setFilterName(String filterName) {
		this.filterName = filterName;
	}

	public int getFilterId() {
		return filterId;
	}

	public void setFilterId(int filterId) {
		this.filterId = filterId;
	}

	public int getCount() {
		return count;
	}

	public void setCount(int count) {
		this.count = count;
	}

	public int getNoOfConcerns() {
		return noOfConcerns;
	}

	public void setNoOfConcerns(int noOfConcerns) {
		this.noOfConcerns = noOfConcerns;
	}

	public Integer getSugPercentage() {
		return sugPercentage;
	}

	public void setSugPercentage(Integer sugPercentage) {
		this.sugPercentage = sugPercentage;
	}

	public String getMonth() {
		return month;
	}

	public void setMonth(String month) {
		this.month = month;
	}

	public String getYear() {
		return year;
	}

	public void setYear(String year) {
		this.year = year;
	}

	public int getRoleId() {
		return roleId;
	}

	public void setRoleId(int roleId) {
		this.roleId = roleId;
	}



	public String getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(String createdDate) {
		this.createdDate = createdDate;
	}

	public int getNominationId() {
		return nominationId;
	}


	public void setNominationId(int nominationId) {
		this.nominationId = nominationId;
	}


	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "TotalNoOfSugesstionCountDto [noOfSugesstion=" + noOfSugesstion + ", filterId=" + filterId
				+ ", filterName=" + filterName + ", count=" + count + ", id=" + id + ", name=" + name + ", label="
				+ label + ", statusLabel=" + statusLabel + ", typeId=" + typeId + ", status=" + status + "]";
	}

}
