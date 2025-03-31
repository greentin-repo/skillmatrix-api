package com.greentin.enovation.dto;

/**
 * @author Anant K August 24, 2023
 * @desc SkillingChecksheet  
 */
public class SMSkillingCheckDTO {

	private int noOfDays;
	
	private String itemName;
	
	private int dayNo;
	
	private long checksheeId;
	
	private int createdBy;
	
	private int updatedBy;

	public int getNoOfDays() {
		return noOfDays;
	}

	public void setNoOfDays(int noOfDays) {
		this.noOfDays = noOfDays;
	}

	public String getItemName() {
		return itemName;
	}

	public void setItemName(String itemName) {
		this.itemName = itemName;
	}

	public int getDayNo() {
		return dayNo;
	}

	public void setDayNo(int dayNo) {
		this.dayNo = dayNo;
	}

	public long getChecksheeId() {
		return checksheeId;
	}

	public void setChecksheeId(long checksheeId) {
		this.checksheeId = checksheeId;
	}

	public int getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(int createdBy) {
		this.createdBy = createdBy;
	}

	public int getUpdatedBy() {
		return updatedBy;
	}

	public void setUpdatedBy(int updatedBy) {
		this.updatedBy = updatedBy;
	}
	
	
}
