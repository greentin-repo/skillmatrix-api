package com.greentin.enovation.model.skillMatrix;

import java.sql.Timestamp;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.greentin.enovation.model.BranchMaster;
import com.greentin.enovation.model.DepartmentMaster;
import com.greentin.enovation.model.dwm.Line;

/**
 * @author Rajdeep 07 Aug 2023 ,12:30 AM
 * @desc
 * @comments
 */

@Entity
@Table(name = "sm_checksheet")
public class SMChecksheet {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private long id;

	@ManyToOne
	@JoinColumn(name = "branch_id")
	private BranchMaster branch;

	@ManyToOne
	@JoinColumn(name = "skill_level_id")
	private SMSkillLevel skillLevel;

	@Column(name = "title", length = 250)
	private String title;

	@Column(name = "no_of_days")
	private int noOfDays;

	@Column(name = "is_active")
	private Boolean isActive;

	@Column(name = "created_by", columnDefinition = "int default 0", updatable = false)
	private int createdBy;

	@Column(name = "updated_by", columnDefinition = "int default 0")
	private int updatedBy;

	@CreationTimestamp
	@Column(name = "created_date", columnDefinition = "datetime default now()", updatable = false)
	private Timestamp createdDate;

	@UpdateTimestamp
	@Column(name = "updated_date")
	private Timestamp updatedDate;

	@Column(name = "production_Plan")
	private int productionPlan;

	@Column(name = "cycle_Plan")
	private String cyclePlan;

	@ManyToOne
	@JoinColumn(name = "dept_id")
	private DepartmentMaster dept;

	@ManyToOne
	@JoinColumn(name = "line_id")
	private Line line;

	@ManyToOne
	@JoinColumn(name = "workstation_id")
	private SMWorkstations workstation;

	public SMChecksheet() {
		super();
	}

	public SMChecksheet(long id) {
		this.id = id;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public BranchMaster getBranch() {
		return branch;
	}

	public void setBranch(BranchMaster branch) {
		this.branch = branch;
	}

	public SMSkillLevel getSkillLevel() {
		return skillLevel;
	}

	public void setSkillLevel(SMSkillLevel skillLevel) {
		this.skillLevel = skillLevel;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public int getNoOfDays() {
		return noOfDays;
	}

	public void setNoOfDays(int noOfDays) {
		this.noOfDays = noOfDays;
	}

	public Boolean getIsActive() {
		return isActive;
	}

	public void setIsActive(Boolean isActive) {
		this.isActive = isActive;
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

	public int getProductionPlan() {
		return productionPlan;
	}

	public void setProductionPlan(int productionPlan) {
		this.productionPlan = productionPlan;
	}

	public String getCyclePlan() {
		return cyclePlan;
	}

	public void setCyclePlan(String cyclePlan) {
		this.cyclePlan = cyclePlan;
	}

	public DepartmentMaster getDept() {
		return dept;
	}

	public void setDept(DepartmentMaster dept) {
		this.dept = dept;
	}

	public Line getLine() {
		return line;
	}

	public void setLine(Line line) {
		this.line = line;
	}

	public SMWorkstations getWorkstation() {
		return workstation;
	}

	public void setWorkstation(SMWorkstations workstation) {
		this.workstation = workstation;
	}

}
