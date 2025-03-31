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

/**
 * @author Rajdeep 07 Aug 2023 ,12:30 AM
 * @desc
 * @comments
 */

@Entity
@Table(name = "sm_assessment")
public class SMAssessment {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private long id;

	@Column(name = "title", length = 250)
	private String title;

	@ManyToOne
	@JoinColumn(name = "branch_id")
	private BranchMaster branch;

	@ManyToOne
	@JoinColumn(name = "dept_id")
	private DepartmentMaster dept;

	@ManyToOne
	@JoinColumn(name = "skill_level_id")
	private SMSkillLevel skillLevel;

	@Column(name = "time", columnDefinition = "int default 0")
	private int time;

	@Column(name = "passing_marks", columnDefinition = "double(16,2) default 0.00")
	private double passingMarks;

	@Column(name = "total_marks", columnDefinition = "double(16,2) default 0.00")
	private double TotalMarks;

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

	@Column(name = "status", length = 250)
	private String status;

	@ManyToOne
	@JoinColumn(name = "line_id")
	private Line line;

	@ManyToOne
	@JoinColumn(name = "workstation_id")
	private SMWorkstations workstation;

	// Added by Sonali L. Jan 15 2024 - added field assessmentType 
	@Column(name = "assessment_type", length = 250)
	private String assessmentType;

	public SMAssessment() {
		super();
	}

	public SMAssessment(long id) {
		this.id = id;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
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

	public SMSkillLevel getSkillLevel() {
		return skillLevel;
	}

	public void setSkillLevel(SMSkillLevel skillLevel) {
		this.skillLevel = skillLevel;
	}

	public int getTime() {
		return time;
	}

	public void setTime(int time) {
		this.time = time;
	}

	public double getPassingMarks() {
		return passingMarks;
	}

	public void setPassingMarks(double passingMarks) {
		this.passingMarks = passingMarks;
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

	public Boolean getIsActive() {
		return isActive;
	}

	public void setIsActive(Boolean isActive) {
		this.isActive = isActive;
	}

	public double getTotalMarks() {
		return TotalMarks;
	}

	public void setTotalMarks(double totalMarks) {
		TotalMarks = totalMarks;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
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

	public String getAssessmentType() {
		return assessmentType;
	}

	public void setAssessmentType(String assessmentType) {
		this.assessmentType = assessmentType;
	}

}
