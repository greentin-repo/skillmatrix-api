package com.greentin.enovation.model;

import java.sql.Timestamp;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

@Entity
public class SuggestionEvalAuthoritySetup {
	
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private int id;
	
	private int empId;
	
	private int branchId;
	
	private String userType;
	
	@CreationTimestamp
	private Timestamp createdDate;
	
	@UpdateTimestamp
	private Timestamp updatedDate;
	
	/*
	 * a.id,a.branch_id,a.created_date,a.emp_id,a.org_id,a.updated_date,a.user_type,
	 * e.first_name,e.last_name,d.dept_name,b.name,e.contact_no,e.cmpy_emp_id,e.
	 * email_id
	 */


	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getEmpId() {
		return empId;
	}

	public void setEmpId(int empId) {
		this.empId = empId;
	}

	public int getBranchId() {
		return branchId;
	}

	public void setBranchId(int branchId) {
		this.branchId = branchId;
	}

	public String getUserType() {
		return userType;
	}

	public void setUserType(String userType) {
		this.userType = userType;
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
