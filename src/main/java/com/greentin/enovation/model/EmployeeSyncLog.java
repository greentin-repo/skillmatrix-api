/**
 * 
 */
package com.greentin.enovation.model;

import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.Table;

import org.hibernate.annotations.CreationTimestamp;

/**
 * @author Vinay B
 * @date May 28, 2020 1:07:15 PM
 */
@Entity
@Table(name = "employee_sync_log")
public class EmployeeSyncLog {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id", nullable = false, updatable = false)
	private long id;

	@Column(name = "org_id", nullable = false, columnDefinition = "int default 0")
	private int orgId;

	@Column(name = "branch_id", nullable = false, columnDefinition = "int default 0")
	private int branchId;

	@Column(name = "addition_count", nullable = false, columnDefinition = "int default 0")
	private int additionCount;

	@Column(name = "deletion_count", nullable = false, columnDefinition = "int default 0")
	private int deletionCount;

	@Column(name = "update_count", nullable = false, columnDefinition = "int default 0")
	private int updationCount;

	@Column(name = "total_count", nullable = false, columnDefinition = "int default 0")
	private int totalCount;

	@Column(name = "status")
	private String status;

	@Lob
	@Column(name = "error_log")
	private String errorLog;

	@CreationTimestamp
	@Column(name = "created_date")
	private Timestamp createdDate;

	@Lob
	@Column(name = "token")
	private String token;
	
	@Lob
	@Column(name = "message")
	private String message;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public int getOrgId() {
		return orgId;
	}

	public void setOrgId(int orgId) {
		this.orgId = orgId;
	}

	public int getBranchId() {
		return branchId;
	}

	public void setBranchId(int branchId) {
		this.branchId = branchId;
	}

	public int getAdditionCount() {
		return additionCount;
	}

	public void setAdditionCount(int additionCount) {
		this.additionCount = additionCount;
	}

	public int getDeletionCount() {
		return deletionCount;
	}

	public void setDeletionCount(int deletionCount) {
		this.deletionCount = deletionCount;
	}

	public int getUpdationCount() {
		return updationCount;
	}

	public void setUpdationCount(int updationCount) {
		this.updationCount = updationCount;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getErrorLog() {
		return errorLog;
	}

	public void setErrorLog(String errorLog) {
		this.errorLog = errorLog;
	}

	public Timestamp getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(Timestamp createdDate) {
		this.createdDate = createdDate;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public int getTotalCount() {
		return totalCount;
	}

	public void setTotalCount(int totalCount) {
		this.totalCount = totalCount;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}
	

}
