package com.greentin.enovation.dto;

public class ConcernFilterCount {
	private int noOfConcern;
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

	public int getNoOfConcern() {
		return noOfConcern;
	}

	public void setNoOfConcern(int noOfConcern) {
		this.noOfConcern = noOfConcern;
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

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "TotalNoOfSugesstionCountDto [noOfSugesstion=" + noOfConcern + ", filterId=" + filterId
				+ ", filterName=" + filterName + ", count=" + count + ", id=" + id + ", name=" + name + ", label="
				+ label + ", statusLabel=" + statusLabel + ", typeId=" + typeId + ", status=" + status + "]";
	}

}
