/**
 * @author Vinay B. May 19, 2022 11:04:02 PM
 * EmployeeTransferDTO
 */
package com.greentin.enovation.dto;

import javax.persistence.Column;

/**
 * @author Vinay B. May 19, 2022 11:04:02 PM
 * @type_name EmployeeTransferDTO
 */
public class EmployeeTransferDTO {

	private int empId;

	private int transferredBy;

	private String transferredOn;

	private String transferReason;

	private int toBranch;

	private int toDept;

	private int toLine;

	private int toEmpLevel;

	private int createdBy;

	private int updatedBy;

	public int getEmpId() {
		return empId;
	}

	public void setEmpId(int empId) {
		this.empId = empId;
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
