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
public class BenefitMasterAudit {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int auditId;

	private String action;

	@ManyToOne
	@JsonIgnore
	@JoinColumn(name = "actionBy")
	private EmployeeDetails actionBy;

	private Timestamp actionDate;
	private String previousValue;
	private String currentValue;

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

}
