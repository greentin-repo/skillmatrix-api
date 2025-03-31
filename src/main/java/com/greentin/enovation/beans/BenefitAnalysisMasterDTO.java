package com.greentin.enovation.beans;


public class BenefitAnalysisMasterDTO {

	private int benfitId;

	private String benefits;

	private String createdDate;

	private String updatedDate;

	private int isEnable;

	private int orgId;

	private int points;

	public int getBenfitId() {
		return benfitId;
	}

	public void setBenfitId(int benfitId) {
		this.benfitId = benfitId;
	}

	public String getBenefits() {
		return benefits;
	}

	public void setBenefits(String benefits) {
		this.benefits = benefits;
	}



	public int getIsEnable() {
		return isEnable;
	}

	public void setIsEnable(int isEnable) {
		this.isEnable = isEnable;
	}

	public String getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(String createdDate) {
		this.createdDate = createdDate;
	}

	public String getUpdatedDate() {
		return updatedDate;
	}

	public void setUpdatedDate(String updatedDate) {
		this.updatedDate = updatedDate;
	}

	public int getOrgId() {
		return orgId;
	}

	public void setOrgId(int orgId) {
		this.orgId = orgId;
	}

	public int getPoints() {
		return points;
	}

	public void setPoints(int points) {
		this.points = points;
	}





}
