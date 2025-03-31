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
 * @author Rajdeep MD ,07 Aug 2023 ,02:45 PM
 * @desc
 * @comments
 */

@Entity
@Table(name = "sm_ojt_skilling_audit")
public class SMOJTSkillingAudit {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private long id;

	@ManyToOne
	@JoinColumn(name = "skilling_id")
	private SMOJTSkilling skilling;

	@ManyToOne
	@JoinColumn(name = "stage_id")
	private SMStage stage;

	@ManyToOne
	@JoinColumn(name = "emp_id")
	private EmployeeDetails empDetails;

	@Column(name = "status", length = 250)
	private String status;

	@Column(name = "previous_audit_id")
	private long previousAuditId;

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

	@ManyToOne
	@JoinColumn(name = "skilling_checksheet_id")
	private SMOJTSkillingChecksheet skillingChecksheet;

	@Column(name = "comment", length = 1000)
	private String comment;

	@Column(name = "start_date")
	private Timestamp startDate;
	
	@ManyToOne
	@JoinColumn(name = "ojt_regis_id")
	private SMOJTRegis ojtRegis;
	
	public SMOJTSkillingAudit() {
		super();
	}

	public SMOJTSkillingAudit(long id) {
		super();
		this.id = id;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public SMOJTSkilling getSkilling() {
		return skilling;
	}

	public void setSkilling(SMOJTSkilling skilling) {
		this.skilling = skilling;
	}

	public SMStage getStage() {
		return stage;
	}

	public void setStage(SMStage stage) {
		this.stage = stage;
	}

	public EmployeeDetails getEmpDetails() {
		return empDetails;
	}

	public void setEmpDetails(EmployeeDetails empDetails) {
		this.empDetails = empDetails;
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

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public SMOJTSkillingChecksheet getSkillingChecksheet() {
		return skillingChecksheet;
	}

	public void setSkillingChecksheet(SMOJTSkillingChecksheet skillingChecksheet) {
		this.skillingChecksheet = skillingChecksheet;
	}

	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

	public long getPreviousAuditId() {
		return previousAuditId;
	}

	public void setPreviousAuditId(long previousAuditId) {
		this.previousAuditId = previousAuditId;
	}

	public Timestamp getStartDate() {
		return startDate;
	}

	public void setStartDate(Timestamp startDate) {
		this.startDate = startDate;
	}

	public SMOJTRegis getOjtRegis() {
		return ojtRegis;
	}

	public void setOjtRegis(SMOJTRegis ojtRegis) {
		this.ojtRegis = ojtRegis;
	}

}
