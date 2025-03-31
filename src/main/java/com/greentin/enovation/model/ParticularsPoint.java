package com.greentin.enovation.model;

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

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.greentin.enovation.accesscontrol.Role;

@Entity
@Table(name = "particulars_point")
public class ParticularsPoint {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "point_id")
	private int pointId;

	@Column(name = "points", columnDefinition = "int default  0")
	private int points;

	@Column(name = "created_by", updatable = false)
	private int createdBy;

	@Column(name = "updated_by")
	private int updatedBy;

	@ManyToOne
	@JoinColumn(name = "role_id", updatable = false)
	private Role role;

	@CreationTimestamp
	@Column(name = "created_date", updatable = false)
	private Timestamp createdDate;

	@UpdateTimestamp
	@Column(name = "updated_date")
	private Timestamp updatedDate;

	@ManyToOne
	@JoinColumn(name = "particular_id", updatable = false)
	@JsonBackReference
	private Particulars particulars;

	@Column(name = "branch_id", updatable = false)
	private int branchId;

	@Column(name = "is_hd_applicable", columnDefinition = "varchar(20) default 'N'")
	private String isHdApplicable;

	@Column(name = "unit")
	private String unit;

	@Column(name = "value")
	private String value;

	public ParticularsPoint(int pointId) {
		super();
		this.pointId = pointId;
	}

	public ParticularsPoint() {
		super();
	}

	public int getPointId() {
		return pointId;
	}

	public void setPointId(int pointId) {
		this.pointId = pointId;
	}

	public int getPoints() {
		return points;
	}

	public void setPoints(int points) {
		this.points = points;
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

	public Role getRole() {
		return role;
	}

	public void setRole(Role role) {
		this.role = role;
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

	public Particulars getParticulars() {
		return particulars;
	}

	public void setParticulars(Particulars particulars) {
		this.particulars = particulars;
	}

	public int getBranchId() {
		return branchId;
	}

	public void setBranchId(int branchId) {
		this.branchId = branchId;
	}

	public String getIsHdApplicable() {
		return isHdApplicable;
	}

	public void setIsHdApplicable(String isHdApplicable) {
		this.isHdApplicable = isHdApplicable;
	}

	public String getUnit() {
		return unit;
	}

	public void setUnit(String unit) {
		this.unit = unit;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

}
