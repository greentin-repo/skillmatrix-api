package com.greentin.enovation.model;

import java.sql.Timestamp;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import com.greentin.enovation.accesscontrol.Role;

@Entity
//@javax.persistence.Cacheable
public class SetPointsForSaving {

	@Id
	@GeneratedValue(strategy =GenerationType.IDENTITY)
	private long id;

	private int amountUpto;

	private int noOfEmployees;

	private long minutes;

	private int points;

	private int createdById;

	private int updatedById;

	@CreationTimestamp
	private Timestamp createdDate;

	@UpdateTimestamp
	private Timestamp updatedDate;

	@ManyToOne
	@JoinColumn(name = "currency_id")
	private Currency currency;

	@ManyToOne
	@JoinColumn(name = "saving_options_id")
	private SavingOptions savingOptions;

	@ManyToOne
	@JoinColumn(name = "saving_options_role_id")
	private SavingOptionsRole savingOptionsRole;

	@ManyToOne
	@JoinColumn(name = "branch_id")
	private BranchMaster branch;

	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name = "role_id")
	private Role role;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public int getAmountUpto() {
		return amountUpto;
	}

	public void setAmountUpto(int amountUpto) {
		this.amountUpto = amountUpto;
	}

	public int getNoOfEmployees() {
		return noOfEmployees;
	}

	public void setNoOfEmployees(int noOfEmployees) {
		this.noOfEmployees = noOfEmployees;
	}

	public long getMinutes() {
		return minutes;
	}

	public void setMinutes(long minutes) {
		this.minutes = minutes;
	}

	public int getPoints() {
		return points;
	}

	public void setPoints(int points) {
		this.points = points;
	}

	public int getCreatedById() {
		return createdById;
	}

	public void setCreatedById(int createdById) {
		this.createdById = createdById;
	}

	public int getUpdatedById() {
		return updatedById;
	}

	public void setUpdatedById(int updatedById) {
		this.updatedById = updatedById;
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

	public Currency getCurrency() {
		return currency;
	}

	public void setCurrency(Currency currency) {
		this.currency = currency;
	}

	public SavingOptions getSavingOptions() {
		return savingOptions;
	}

	public void setSavingOptions(SavingOptions savingOptions) {
		this.savingOptions = savingOptions;
	}

	public SavingOptionsRole getSavingOptionsRole() {
		return savingOptionsRole;
	}

	public void setSavingOptionsRole(SavingOptionsRole savingOptionsRole) {
		this.savingOptionsRole = savingOptionsRole;
	}

	public BranchMaster getBranch() {
		return branch;
	}

	public void setBranch(BranchMaster branch) {
		this.branch = branch;
	}

	public Role getRole() {
		return role;
	}

	public void setRole(Role role) {
		this.role = role;
	}



}
