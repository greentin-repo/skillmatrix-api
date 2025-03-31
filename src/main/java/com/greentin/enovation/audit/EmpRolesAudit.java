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
public class EmpRolesAudit {

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
	@JoinColumn(name = "previousValue")
	private EmployeeDetails previousValue;

	@ManyToOne
	@JsonIgnore
	@JoinColumn(name = "currentValue")
	private EmployeeDetails currentValue;

	private int levelId;

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

	public EmployeeDetails getPreviousValue() {
		return previousValue;
	}

	public void setPreviousValue(EmployeeDetails previousValue) {
		this.previousValue = previousValue;
	}

	public EmployeeDetails getCurrentValue() {
		return currentValue;
	}

	public void setCurrentValue(EmployeeDetails currentValue) {
		this.currentValue = currentValue;
	}

	public int getLevelId() {
		return levelId;
	}

	public void setLevelId(int levelId) {
		this.levelId = levelId;
	}

}