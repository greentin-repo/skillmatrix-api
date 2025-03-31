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

/**
 * @author Anant K ,21 August 2023 ,03:00 PM
 * @desc
 * @comments
 */

@Entity
@Table(name = "sm_ojt_checksheet_points_audit")
public class SMOJTChecksheetPointsAudit {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private long id;

	@ManyToOne
	@JoinColumn(name = "skilling_audit_id")
	private SMOJTSkillingAudit skillingAudit;

	@ManyToOne
	@JoinColumn(name = "ojt_point_id")
	private SMOJTChecksheetPoints ojtPoint;

	@ManyToOne
	@JoinColumn(name = "checksheet_points_id")
	private SMChecksheetPoints checksheetPoints;

	@Column(name = "status", length = 250)
	private String status;

	@Column(name = "comment", length = 1000)
	private String comment;

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

	public SMOJTChecksheetPointsAudit() {
		super();
	}

	public SMOJTChecksheetPointsAudit(long id) {
		super();
		this.id = id;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public SMOJTSkillingAudit getSkillingAudit() {
		return skillingAudit;
	}

	public void setSkillingAudit(SMOJTSkillingAudit skillingAudit) {
		this.skillingAudit = skillingAudit;
	}
	
	public SMOJTChecksheetPoints getOjtPoint() {
		return ojtPoint;
	}

	public void setOjtPoint(SMOJTChecksheetPoints ojtPoint) {
		this.ojtPoint = ojtPoint;
	}

	public SMChecksheetPoints getChecksheetPoints() {
		return checksheetPoints;
	}

	public void setChecksheetPoints(SMChecksheetPoints checksheetPoints) {
		this.checksheetPoints = checksheetPoints;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
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
