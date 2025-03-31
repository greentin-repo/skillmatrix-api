/**
 * @author Vinay B. May 19, 2022 5:09:39 PM
 * EmployeeTransfer
 */
package com.greentin.enovation.model;

import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

/**
 * @author Vinay B. May 19, 2022 5:09:39 PM
 * @type_name EmployeeTransfer
 */
@Entity
@Table(name = "employee_transfer")
public class EmployeeTransfer {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id", nullable = false)
	private long id;

	@Column(name = "emp_id", nullable = false, columnDefinition = "int(11) default 0")
	private int empId;

	@Column(name = "company_emp_id", length = 100)
	private String companyEmpId;

	@Column(name = "transferred_by", nullable = false, updatable = false, columnDefinition = "int(11) default 0")
	private int transferredBy;

	@Column(name = "created_by", nullable = false, updatable = false, columnDefinition = "int(11) default 0")
	private int createdBy;

	@Column(name = "updated_by", columnDefinition = "int(11) default 0")
	private int updatedBy;

	@Column(name = "transferred_on", length = 50)
	private String transferredOn;

	@Column(name = "transfer_reason", length = 1000)
	private String transferReason;

	@Column(name = "from_branch", nullable = false, updatable = false, columnDefinition = "int(11) default 0")
	private int fromBranch;

	@Column(name = "from_dept", nullable = false, updatable = false, columnDefinition = "int(11) default 0")
	private int fromDept;

	@Column(name = "from_line", nullable = false, updatable = false, columnDefinition = "int(11) default 0")
	private int fromLine;

	@Column(name = "from_emp_level", nullable = false, updatable = false, columnDefinition = "int(11) default 0")
	private int fromEmpLevel;

	@Column(name = "to_branch", nullable = false, updatable = false, columnDefinition = "int(11) default 0")
	private int toBranch;

	@Column(name = "to_dept", nullable = false, updatable = false, columnDefinition = "int(11) default 0")
	private int toDept;

	@Column(name = "to_line", nullable = false, updatable = false, columnDefinition = "int(11) default 0")
	private int toLine;

	@Column(name = "to_emp_level", nullable = false, updatable = false, columnDefinition = "int(11) default 0")
	private int toEmpLevel;

	@CreationTimestamp
	@Column(name = "created_date")
	private Timestamp createdDate;

	@UpdateTimestamp
	@Column(name = "updated_date")
	private Timestamp updatedDate;

	public EmployeeTransfer() {
		super();
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public int getEmpId() {
		return empId;
	}

	public void setEmpId(int empId) {
		this.empId = empId;
	}

	public String getCompanyEmpId() {
		return companyEmpId;
	}

	public void setCompanyEmpId(String companyEmpId) {
		this.companyEmpId = companyEmpId;
	}

	public int getTransferredBy() {
		return transferredBy;
	}

	public void setTransferredBy(int transferredBy) {
		this.transferredBy = transferredBy;
	}

	public String getTransferredOn() {
		return transferredOn;
	}

	public void setTransferredOn(String transferredOn) {
		this.transferredOn = transferredOn;
	}

	public String getTransferReason() {
		return transferReason;
	}

	public void setTransferReason(String transferReason) {
		this.transferReason = transferReason;
	}

	public int getFromBranch() {
		return fromBranch;
	}

	public void setFromBranch(int fromBranch) {
		this.fromBranch = fromBranch;
	}

	public int getFromDept() {
		return fromDept;
	}

	public void setFromDept(int fromDept) {
		this.fromDept = fromDept;
	}

	public int getFromLine() {
		return fromLine;
	}

	public void setFromLine(int fromLine) {
		this.fromLine = fromLine;
	}

	public int getFromEmpLevel() {
		return fromEmpLevel;
	}

	public void setFromEmpLevel(int fromEmpLevel) {
		this.fromEmpLevel = fromEmpLevel;
	}

	public int getToBranch() {
		return toBranch;
	}

	public void setToBranch(int toBranch) {
		this.toBranch = toBranch;
	}

	public int getToDept() {
		return toDept;
	}

	public void setToDept(int toDept) {
		this.toDept = toDept;
	}

	public int getToLine() {
		return toLine;
	}

	public void setToLine(int toLine) {
		this.toLine = toLine;
	}

	public int getToEmpLevel() {
		return toEmpLevel;
	}

	public void setToEmpLevel(int toEmpLevel) {
		this.toEmpLevel = toEmpLevel;
	}

	public Timestamp getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(Timestamp createdDate) {
		this.createdDate = createdDate;
	}

	public Timestamp getUpdatedDate() {
		return updatedDate;
	}

	public void setUpdatedDate(Timestamp updatedDate) {
		this.updatedDate = updatedDate;
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

}
