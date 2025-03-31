package com.greentin.enovation.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Transient;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
//@javax.persistence.Cacheable
public class Executive {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id", unique = true, nullable = false)
	private int id;

	private String typeOfExecutive;

	@ManyToOne
	@JoinColumn(name = "orgId")
	private OrganizationMaster org;

	@ManyToOne
	@JoinColumn(name = "empId")
	private EmployeeDetails emp;

	@ManyToOne
	@JsonIgnore
	@JoinColumn(name = "createdBy")
	private EmployeeDetails createdById;

	@ManyToOne
	@JsonIgnore
	@JoinColumn(name = "updatedBy")
	private EmployeeDetails updatedById;

	private Date createdDate;

	private Date updatedDate;

	private int isDeactive;

	@Transient
	private int orgId;

	@Transient
	private int empId;

	@Transient
	private int createdBy;
	
	@Transient
	private int updatedBy;

	@Transient
	private int isSetupCompleted;

	@Transient
	private String levelName;

	@Transient
	private String emailId;

	@Transient
	private String firstName;

	@Transient
	private String lastName;

	@Transient
	private String portalLink;

	public Executive() {
		super();
	}

	public Executive(int id) {
		super();
		this.id = id;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getTypeOfExecutive() {
		return typeOfExecutive;
	}

	public EmployeeDetails getCreatedById() {
		return createdById;
	}

	public void setCreatedById(EmployeeDetails createdById) {
		this.createdById = createdById;
	}

	public EmployeeDetails getUpdatedById() {
		return updatedById;
	}

	public void setUpdatedById(EmployeeDetails updatedById) {
		this.updatedById = updatedById;
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

	public void setTypeOfExecutive(String typeOfExecutive) {
		this.typeOfExecutive = typeOfExecutive;
	}

	public OrganizationMaster getOrg() {
		return org;
	}

	public void setOrg(OrganizationMaster org) {
		this.org = org;
	}

	public EmployeeDetails getEmp() {
		return emp;
	}

	public void setEmp(EmployeeDetails emp) {
		this.emp = emp;
	}

	public int getOrgId() {
		return orgId;
	}

	public void setOrgId(int orgId) {
		this.orgId = orgId;
	}

	public int getEmpId() {
		return empId;
	}

	public void setEmpId(int empId) {
		this.empId = empId;
	}

	public Date getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}

	public Date getUpdatedDate() {
		return updatedDate;
	}

	public void setUpdatedDate(Date updatedDate) {
		this.updatedDate = updatedDate;
	}

	public int getIsDeactive() {
		return isDeactive;
	}

	public void setIsDeactive(int isDeactive) {
		this.isDeactive = isDeactive;
	}

	public String getLevelName() {
		return levelName;
	}

	public void setLevelName(String levelName) {
		this.levelName = levelName;
	}

	public String getEmailId() {
		return emailId;
	}

	public void setEmailId(String emailId) {
		this.emailId = emailId;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getPortalLink() {
		return portalLink;
	}

	public void setPortalLink(String portalLink) {
		this.portalLink = portalLink;
	}

	public int getIsSetupCompleted() {
		return isSetupCompleted;
	}

	public void setIsSetupCompleted(int isSetupCompleted) {
		this.isSetupCompleted = isSetupCompleted;
	}

}
