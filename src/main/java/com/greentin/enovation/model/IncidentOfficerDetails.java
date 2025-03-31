package com.greentin.enovation.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Transient;

@Entity
public class IncidentOfficerDetails {

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="ince_id",unique=true, nullable=false)
	private int id;

	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="emp_id")
	private EmployeeDetails emp;

	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="manager_type_id")
	private IncidentManagerType incidentManagerType;

	@Transient
	private String deptName;
	
	@Transient
	private int branchId;
	

	public IncidentOfficerDetails() {
		super();
	}

	public IncidentOfficerDetails(int id) {
		super();
		this.id = id;
	}
	public int getId() {
		return id;
	}
	public IncidentOfficerDetails( EmployeeDetails emp) {
		super();
		this.emp = emp;	
	}
	public void setId(int id) {
		this.id = id;
	}

	public EmployeeDetails getEmp() {
		return emp;
	}

	public void setEmp(EmployeeDetails emp) {
		this.emp = emp;
	}

	public IncidentManagerType getIncidentManagerType() {
		return incidentManagerType;
	}

	public void setIncidentManagerType(IncidentManagerType incidentManagerType) {
		this.incidentManagerType = incidentManagerType;
	}

	public String getDeptName() {
		return deptName;
	}

	public void setDeptName(String deptName) {
		this.deptName = deptName;
	}

	public int getBranchId() {
		return branchId;
	}

	public void setBranchId(int branchId) {
		this.branchId = branchId;
	}

}
