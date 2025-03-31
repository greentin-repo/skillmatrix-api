package com.greentin.enovation.model;

import java.util.Date;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Transient;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
//@javax.persistence.Cacheable
public class BrowniePointsReimbursementCycle {

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private int id;

	@ManyToOne
	@JoinColumn(name="type_id")
	private BrowniePointsReimbursementCycleType type;

	@ManyToOne
	@JoinColumn(name="branch_id")
	@JsonIgnore
	private BranchMaster branch;



	@Transient
	private int typeId;


	@CreationTimestamp
	private Date createdDate;

	@UpdateTimestamp
	private Date updatedDate;



	public BrowniePointsReimbursementCycle() {
		super();
	}

	public BrowniePointsReimbursementCycle(BrowniePointsReimbursementCycleType type, BranchMaster branch) {
		super();
		this.type = type;
		this.branch = branch;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public BrowniePointsReimbursementCycleType getType() {
		return type;
	}

	public void setType(BrowniePointsReimbursementCycleType type) {
		this.type = type;
	}

	public BranchMaster getBranch() {
		return branch;
	}

	public void setBranch(BranchMaster branch) {
		this.branch = branch;
	}

	public Date getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}

	public Date getUpdatedDate() {
		return updatedDate;
	}

	public void setUpdatedDate(Date updatedDate) {
		this.updatedDate = updatedDate;
	}

	public int getTypeId() {
		return typeId;
	}

	public void setTypeId(int typeId) {
		this.typeId = typeId;
	}


}
