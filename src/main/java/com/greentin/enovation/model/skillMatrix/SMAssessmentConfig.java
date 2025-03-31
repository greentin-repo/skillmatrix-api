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

import com.greentin.enovation.model.BranchMaster;
import com.greentin.enovation.model.DepartmentMaster;
import com.greentin.enovation.model.dwm.Line;

@Entity
@Table(name = "sm_assessment_config	")
public class SMAssessmentConfig {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private long id;

	@ManyToOne
	@JoinColumn(name = "skill_level_id")
	private SMSkillLevel skillLevel;

	@ManyToOne
	@JoinColumn(name = "branch_id")
	private BranchMaster branch;

	@Column(name = "no_of_days")
	private int noOfDays;

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

//	@ManyToOne
//	@JoinColumn(name = "dept_id")
//	private DepartmentMaster dept;
//
//	@ManyToOne
//	@JoinColumn(name = "line_id")
//	private Line line;
//
//	@ManyToOne
//	@JoinColumn(name = "workstation_id")
//	private SMWorkstations workstation;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public SMSkillLevel getSkillLevel() {
		return skillLevel;
	}

	public void setSkillLevel(SMSkillLevel skillLevel) {
		this.skillLevel = skillLevel;
	}

	public int getNoOfDays() {
		return noOfDays;
	}

	public void setNoOfDays(int noOfDays) {
		this.noOfDays = noOfDays;
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

	public BranchMaster getBranch() {
		return branch;
	}

	public void setBranch(BranchMaster branch) {
		this.branch = branch;
	}

//	public DepartmentMaster getDept() {
//		return dept;
//	}
//
//	public void setDept(DepartmentMaster dept) {
//		this.dept = dept;
//	}
//
//	public Line getLine() {
//		return line;
//	}
//
//	public void setLine(Line line) {
//		this.line = line;
//	}
//
//	public SMWorkstations getWorkstation() {
//		return workstation;
//	}
//
//	public void setWorkstation(SMWorkstations workstation) {
//		this.workstation = workstation;
//	}

}