package com.greentin.enovation.model.skillMatrix;

import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.UpdateTimestamp;

/**
 * @author Rajdeep 09 Aug 2023 ,12:30 AM
 * @desc this Entity used for auditing smOJTRegis entity records of (delete,
 *       update and deactive)
 * @comments
 */

@Entity
@Table(name = "audit_sm_ojt_regis")
public class AuditSMOJTRegis {
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

	@UpdateTimestamp
	@Column(name = "updated_date")
	private Timestamp updatedDate;

	@Column(name = "regis_id")
	private long regisId;

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

	public Timestamp getUpdatedDate() {
		return updatedDate;
	}

	public void setUpdatedDate(Timestamp updatedDate) {
		this.updatedDate = updatedDate;
	}

	public long getRegisId() {
		return regisId;
	}

	public void setRegisId(long regisId) {
		this.regisId = regisId;
	}

}
