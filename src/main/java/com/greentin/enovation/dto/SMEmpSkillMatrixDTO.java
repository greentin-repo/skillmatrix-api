package com.greentin.enovation.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class SMEmpSkillMatrixDTO {

	private long id;

	private long workstationId;

	private String workstation;

	private long skillLevelId;

	private String levelName;

	private int empId;

	private String empName;

	private String gender;

	private String doj;

	private double experience;

	private String emplevel;

	private String cmpyEmpId;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public long getWorkstationId() {
		return workstationId;
	}

	public void setWorkstationId(long workstationId) {
		this.workstationId = workstationId;
	}

	public long getSkillLevelId() {
		return skillLevelId;
	}

	public void setSkillLevelId(long skillLevelId) {
		this.skillLevelId = skillLevelId;
	}

	public int getEmpId() {
		return empId;
	}

	public void setEmpId(int empId) {
		this.empId = empId;
	}

	public String getWorkstation() {
		return workstation;
	}

	public void setWorkstation(String workstation) {
		this.workstation = workstation;
	}

	public String getEmpName() {
		return empName;
	}

	public void setEmpName(String empName) {
		this.empName = empName;
	}

	public String getLevelName() {
		return levelName;
	}

	public void setLevelName(String levelName) {
		this.levelName = levelName;
	}

	public String getGender() {
		return gender;
	}

	public void setGender(String gender) {
		this.gender = gender;
	}

	public String getDoj() {
		return doj;
	}

	public void setDoj(String doj) {
		this.doj = doj;
	}

	public double getExperience() {
		return experience;
	}

	public void setExperience(double experience) {
		this.experience = experience;
	}

	public String getEmplevel() {
		return emplevel;
	}

	public void setEmplevel(String emplevel) {
		this.emplevel = emplevel;
	}

	public String getCmpyEmpId() {
		return cmpyEmpId;
	}

	public void setCmpyEmpId(String cmpyEmpId) {
		this.cmpyEmpId = cmpyEmpId;
	}

}
