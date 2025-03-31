package com.greentin.enovation.model.skillMatrix;

import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.CreationTimestamp;

/**
 * @author Anant K. 09 August 2023 ,02:15 PM
 * @desc This Entity Use For Auditing SMModel Entity Records Of (delete, update)
 * @comments
 */
@Entity
@Table(name = "audit_sm_model")
public class AuditSMModel {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;
	
	@Column(name = "branch_id")
	private int branchId;

	@Column(name = "col_name")
	private String colName;
	
	@Column(name = "old_value")
	private String oldValue;
	
	@Column(name = "new_value")
	private String newValue;
	
	@Column(name = "action")
	private String action;
	
	@Column(name = "action_by")
	private int actionBy;
	
	@CreationTimestamp
	@Column(name = "created_date", updatable = false, columnDefinition = "datetime default now()")
	private Timestamp createdDate;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public int getBranchId() {
		return branchId;
	}

	public void setBranchId(int branchId) {
		this.branchId = branchId;
	}

	public String getColName() {
		return colName;
	}

	public void setColName(String colName) {
		this.colName = colName;
	}

	public String getOldValue() {
		return oldValue;
	}

	public void setOldValue(String oldValue) {
		this.oldValue = oldValue;
	}

	public String getNewValue() {
		return newValue;
	}

	public void setNewValue(String newValue) {
		this.newValue = newValue;
	}

	public String getAction() {
		return action;
	}

	public void setAction(String action) {
		this.action = action;
	}

	public int getActionBy() {
		return actionBy;
	}

	public void setActionBy(int actionBy) {
		this.actionBy = actionBy;
	}

	public Timestamp getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(Timestamp createdDate) {
		this.createdDate = createdDate;
	}
}
