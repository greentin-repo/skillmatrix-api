package com.greentin.enovation.model;

import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.UpdateTimestamp;

/**
 * 
 * @author rakesh 14 dec 2020
 *
 */
@Entity
@Table(name = "employee_count")
public class EmployeeCount {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id", updatable = false, nullable = false)
	private long id;
	
	@Column(name = "org_id", columnDefinition = "int default 0")
	private int orgId;
	
	@Column(name = "branch_id", columnDefinition = "int default 0")
	private int branchId;
	
	@Column(name = "dept_id", columnDefinition = "int default 0")
	private int deptId;
	
	@Column(name = "line_id", columnDefinition = "int default 0")
	private long lineId;
	
	@Column(name = "count", columnDefinition = "int default 0")
	private int count;
	
	@Column(name = "count_type")
	private String countType;//ORG or BRANCH or DEPT or LINE
	
	@Column(name = "month_value", columnDefinition = "int default 0")
	private int monthValue;

	@Column(name = "year", columnDefinition = "int default 0")
	private int year;
	
	
	@UpdateTimestamp
	@Column(name = "created_date", updatable = false)
	private Timestamp createdDate;

	@UpdateTimestamp
	@Column(name = "udpated_Date")
	private Timestamp updatedDate;

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

	public int getDeptId() {
		return deptId;
	}

	public void setDeptId(int deptId) {
		this.deptId = deptId;
	}

	

	public long getLineId() {
		return lineId;
	}

	public void setLineId(long lineId) {
		this.lineId = lineId;
	}

	public int getCount() {
		return count;
	}

	public void setCount(int count) {
		this.count = count;
	}

	public String getCountType() {
		return countType;
	}

	public void setCountType(String countType) {
		this.countType = countType;
	}

	public int getMonthValue() {
		return monthValue;
	}

	public void setMonthValue(int monthValue) {
		this.monthValue = monthValue;
	}

	public int getYear() {
		return year;
	}

	public void setYear(int year) {
		this.year = year;
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
}
