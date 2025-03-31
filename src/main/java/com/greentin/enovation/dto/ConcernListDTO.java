package com.greentin.enovation.dto;

import com.greentin.enovation.config.EnovationConfig;

public class ConcernListDTO {

	private int concernId;
	
	private String profilePic;

	private String concernNumber;

	private String concernTitle;

	private String concern;

	private String catName;

	private String status;

	private String empName;

	private String label;

	private int typeId;

	private int points;

	private int statusId;

	private int empPoints;

	private String createdDate;

	private String updatedDate;

	private int isAnonymous;
	
	private String branchName;
	
	private String assignedTo;
	
	private String closure_time;
	
	private String summary;
	
	private String assignedDate;

	public int getConcernId() {
		return concernId;
	}

	public void setConcernId(int concernId) {
		this.concernId = concernId;
	}

	public String getProfilePic() {
		if(profilePic==null) {
			return profilePic;	
		}else {
			return EnovationConfig.buddyConfig.get("ProfilePicPathUrl")+profilePic;
		}
	}

	public void setProfilePic(String profilePic) {
		this.profilePic = profilePic;
	}

	public String getConcernNumber() {
		return concernNumber;
	}

	public void setConcernNumber(String concernNumber) {
		this.concernNumber = concernNumber;
	}

	public String getConcernTitle() {
		return concernTitle;
	}

	public void setConcernTitle(String concernTitle) {
		this.concernTitle = concernTitle;
	}

	public String getConcern() {
		return concern;
	}

	public void setConcern(String concern) {
		this.concern = concern;
	}

	public String getCatName() {
		return catName;
	}

	public void setCatName(String catName) {
		this.catName = catName;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getEmpName() {
		return empName;
	}

	public void setEmpName(String empName) {
		this.empName = empName;
	}

	public String getLabel() {
		return label;
	}

	public void setLabel(String label) {
		this.label = label;
	}

	public int getTypeId() {
		return typeId;
	}

	public void setTypeId(int typeId) {
		this.typeId = typeId;
	}

	public int getPoints() {
		return points;
	}

	public void setPoints(int points) {
		this.points = points;
	}

	public int getStatusId() {
		return statusId;
	}

	public void setStatusId(int statusId) {
		this.statusId = statusId;
	}

	public int getEmpPoints() {
		return empPoints;
	}

	public void setEmpPoints(int empPoints) {
		this.empPoints = empPoints;
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

	public int getIsAnonymous() {
		return isAnonymous;
	}

	public void setIsAnonymous(int isAnonymous) {
		this.isAnonymous = isAnonymous;
	}

	public String getBranchName() {
		return branchName;
	}

	public void setBranchName(String branchName) {
		this.branchName = branchName;
	}

	
	public String getAssignedTo() {
		return assignedTo;
	}

	public void setAssignedTo(String assignedTo) {
		this.assignedTo = assignedTo;
	}

	public String getClosure_time() {
		return closure_time;
	}

	public void setClosure_time(String closure_time) {
		this.closure_time = closure_time;
	}

	public String getSummary() {
		return summary;
	}

	public void setSummary(String summary) {
		this.summary = summary;
	}

	public String getAssignedDate() {
		return assignedDate;
	}

	public void setAssignedDate(String assignedDate) {
		this.assignedDate = assignedDate;
	}
	
	
}
