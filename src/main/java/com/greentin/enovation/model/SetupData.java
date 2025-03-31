package com.greentin.enovation.model;

public class SetupData {
	
	private String sideMenuName;
	private int isSetupCompleted;
	private int isMandatory;
	private int branchId;
	private int branch_setupId;
	
	public int getBranchId() {
		return branchId;
	}
	public void setBranchId(int branchId) {
		this.branchId = branchId;
	}
	public String getSideMenuName() {
		return sideMenuName;
	}
	public void setSideMenuName(String sideMenuName) {
		this.sideMenuName = sideMenuName;
	}
	public int getIsSetupCompleted() {
		return isSetupCompleted;
	}
	public void setIsSetupCompleted(int isSetupCompleted) {
		this.isSetupCompleted = isSetupCompleted;
	}
	public int getIsMandatory() {
		return isMandatory;
	}
	public void setIsMandatory(int isMandatory) {
		this.isMandatory = isMandatory;
	}
	public int getBranch_setupId() {
		return branch_setupId;
	}
	public void setBranch_setupId(int branch_setupId) {
		this.branch_setupId = branch_setupId;
	}
	
	

}
