package com.greentin.enovation.bo.model;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class BranchMasterBO {

	private int branchId;
	
	private String name;
	
	private String location;
    
	public BranchMasterBO() {
		
	}
	
	public BranchMasterBO(int branchId,String name){
		this.branchId=branchId;
		this.name=name;
	}

	public int getBranchId() {
		return branchId;
	}


	public void setBranchId(int branchId) {
		this.branchId = branchId;
	}


	public String getName() {
		return name;
	}


	public void setName(String name) {
		this.name = name;
	}

	public String getLocation() {
		return location;
	}


	public void setLocation(String location) {
		this.location = location;
	}
	
	

}
