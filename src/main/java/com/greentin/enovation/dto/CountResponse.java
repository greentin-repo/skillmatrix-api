package com.greentin.enovation.dto;

public class CountResponse {

	private long id;

	private String label;

	private String name;

	private long count;



	public CountResponse() {
		super();
	}

	public CountResponse(long id, String label, long count) {
		super();
		this.id = id;
		this.label = label;
		this.count = count;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getLabel() {
		return label;
	}

	public void setLabel(String label) {
		this.label = label;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public long getCount() {
		return count;
	}

	public void setCount(long count) {
		this.count = count;
	}


}
