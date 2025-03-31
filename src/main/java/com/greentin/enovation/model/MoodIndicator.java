package com.greentin.enovation.model;

import java.sql.Timestamp;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Transient;
import org.hibernate.annotations.CreationTimestamp;

@Entity
public class MoodIndicator {

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private long id;

	private int rating;

	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="emp_id")
	private EmployeeDetails empDetails;


	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="branch_id")
	private BranchMaster branch;

	@CreationTimestamp
	private Timestamp createdDate;


	@Transient
	private int branchId;

	@Transient
	private int deptId;

	@Transient
	private int emoji;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public int getRating() {
		return rating;
	}

	public void setRating(int rating) {
		this.rating = rating;
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

	public BranchMaster getBranch() {
		return branch;
	}

	public void setBranch(BranchMaster branch) {
		this.branch = branch;
	}

	public int getEmoji() {
		return emoji;
	}

	public void setEmoji(int emoji) {
		this.emoji = emoji;
	}


}
