package com.greentin.enovation.audit;

import java.sql.Timestamp;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.greentin.enovation.model.EmployeeDetails;

@Entity
@Table
public class EmployeeDetailsAudit {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int auditId;

	private String action;

	@ManyToOne
	@JsonIgnore
	@JoinColumn(name = "actionBy")
	private EmployeeDetails actionBy;

	private Timestamp actionDate;

	@ManyToOne
	@JsonIgnore
	@JoinColumn(name = "empId")
	private EmployeeDetails empId;

	private String previousValue;
	private String currentValue;
	
	
	
	public EmployeeDetailsAudit() {
		super();
	}

	public EmployeeDetailsAudit(String action, EmployeeDetails actionBy,
			EmployeeDetails empId, String previousValue, String currentValue,Timestamp actionDate) {
		super();
		this.action = action;
		this.actionBy = actionBy;
		this.empId = empId;
		this.previousValue = previousValue;
		this.currentValue = currentValue;
		this.actionDate = actionDate;
	}

	public int getAuditId() {
		return auditId;
	}

	public void setAuditId(int auditId) {
		this.auditId = auditId;
	}

	public String getAction() {
		return action;
	}

	public void setAction(String action) {
		this.action = action;
	}

	public EmployeeDetails getActionBy() {
		return actionBy;
	}

	public void setActionBy(EmployeeDetails actionBy) {
		this.actionBy = actionBy;
	}

	public Timestamp getActionDate() {
		return actionDate;
	}

	public void setActionDate(Timestamp actionDate) {
		this.actionDate = actionDate;
	}

	public String getPreviousValue() {
		return previousValue;
	}

	public void setPreviousValue(String previousValue) {
		this.previousValue = previousValue;
	}

	public String getCurrentValue() {
		return currentValue;
	}

	public void setCurrentValue(String currentValue) {
		this.currentValue = currentValue;
	}

	public EmployeeDetails getEmpId() {
		return empId;
	}

	public void setEmpId(EmployeeDetails empId) {
		this.empId = empId;
	}

}
