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
 * @author Rajdeep 09 Aug 2023 ,12:30 AM
 * @desc this Entity used for auditing SMChecksheet entity records of (delete,
 *       update and deactive)
 * @comments
 */

@Entity
@Table(name = "audit_sm_checksheet_point")
public class AuditSMChecksheetPoint {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private long id;

	@Column(name = "column_name")
	private String columnName;

	@Column(name = "old_value")
	private String oldValue;

	@Column(name = "new_value")
	private String newValue;

	@Column(name = "action_by")
	private int actionBy;

	@Column(name = "action")
	private String action;

	@CreationTimestamp
	@Column(name = "created_date")
	private Timestamp createdDate;

	@Column(name = "check_sheet_point_id")
	private long checkSheetPointId;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getColumnName() {
		return columnName;
	}

	public void setColumnName(String columnName) {
		this.columnName = columnName;
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

	public int getActionBy() {
		return actionBy;
	}

	public void setActionBy(int actionBy) {
		this.actionBy = actionBy;
	}

	public String getAction() {
		return action;
	}

	public void setAction(String action) {
		this.action = action;
	}

	public Timestamp getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(Timestamp createdDate) {
		this.createdDate = createdDate;
	}

	public long getCheckSheetPointId() {
		return checkSheetPointId;
	}

	public void setCheckSheetPointId(long checkSheetPointId) {
		this.checkSheetPointId = checkSheetPointId;
	}

}
