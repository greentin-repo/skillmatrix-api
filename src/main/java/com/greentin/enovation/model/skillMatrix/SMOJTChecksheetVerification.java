package com.greentin.enovation.model.skillMatrix;

import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import com.greentin.enovation.model.EmployeeDetails;

/**
 * @author Rajdeep 07 Aug 2023 ,02:35 PM
 * @desc 
 * @comments
 */


@Entity
@Table(name = "sm_ojt_checksheet_verification")
public class SMOJTChecksheetVerification {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private long id;

	@ManyToOne
	@JoinColumn(name = "skilling_checksheet_id")
	private SMOJTSkillingChecksheet skillingChecksheet;

	@ManyToOne
	@JoinColumn(name = "emp_id")
	private EmployeeDetails empDetails;

	@ManyToOne
	@JoinColumn(name = "user_type_id")
	private SMUserType userType;

	@Column(name = "status", length = 250)
	private String status;

	@Column(name = "comments", length = 1000)
	private String comments;

	@CreationTimestamp
	@Column(name = "created_date", columnDefinition = "datetime default now()", updatable = false)
	private Timestamp createdDate;

	@UpdateTimestamp
	@Column(name = "updated_date", columnDefinition = "datetime default now()")
	private Timestamp updatedDate;

	@Column(name = "created_by", columnDefinition = "int default 0", updatable = false)
	private int createdBy;

	@Column(name = "updated_by", columnDefinition = "int default 0")
	private int updatedBy;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public SMOJTSkillingChecksheet getSkillingChecksheet() {
		return skillingChecksheet;
	}

	public void setSkillingChecksheet(SMOJTSkillingChecksheet skillingChecksheet) {
		this.skillingChecksheet = skillingChecksheet;
	}

	public EmployeeDetails getEmpDetails() {
		return empDetails;
	}

	public void setEmpDetails(EmployeeDetails empDetails) {
		this.empDetails = empDetails;
	}

	public SMUserType getUserType() {
		return userType;
	}

	public void setUserType(SMUserType userType) {
		this.userType = userType;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getComments() {
		return comments;
	}

	public void setComments(String comments) {
		this.comments = comments;
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
