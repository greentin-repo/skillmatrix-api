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
import javax.persistence.Transient;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import com.greentin.enovation.model.BranchMaster;
import com.greentin.enovation.model.DepartmentMaster;
import com.greentin.enovation.model.EmployeeDetails;
import com.greentin.enovation.model.dwm.Line;

/**
 * @author Rajdeep 07 Aug 2023 ,02:35 PM
 * @desc
 * @comments
 */

@Entity
@Table(name = "sm_ojt_regis")
public class SMOJTRegis {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private long id;

	@ManyToOne
	@JoinColumn(name = "emp_id")
	private EmployeeDetails empDetails;

	@ManyToOne
	@JoinColumn(name = "current_skill_level_id")
	private SMSkillLevel currentSkillLevel;

	@ManyToOne
	@JoinColumn(name = "branch_id")
	private BranchMaster branch;

	@ManyToOne
	@JoinColumn(name = "dept_id")
	private DepartmentMaster dept;

	@ManyToOne
	@JoinColumn(name = "desired_skill_level_id")
	private SMSkillLevel desiredSkillLevel;

	@Column(name = "status", length = 250)
	private String status;

	@ManyToOne
	@JoinColumn(name = "trainer_emp_id")
	private EmployeeDetails trainerDetails;

	@ManyToOne
	@JoinColumn(name = "workstation_id")
	private SMWorkstations workstation;

	@ManyToOne
	@JoinColumn(name = "ojt_plan_id")
	private SMOJTPlan smOjtPlan;

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

	@ManyToOne
	@JoinColumn(name = "line_id")
	private Line line;

	public SMOJTRegis() {
		super();
	}

	public SMOJTRegis(long id) {
		super();
		this.id = id;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public EmployeeDetails getEmpDetails() {
		return empDetails;
	}

	public void setEmpDetails(EmployeeDetails empDetails) {
		this.empDetails = empDetails;
	}

	public SMSkillLevel getCurrentSkillLevel() {
		return currentSkillLevel;
	}

	public void setCurrentSkillLevel(SMSkillLevel currentSkillLevel) {
		this.currentSkillLevel = currentSkillLevel;
	}

	public SMSkillLevel getDesiredSkillLevel() {
		return desiredSkillLevel;
	}

	public void setDesiredSkillLevel(SMSkillLevel desiredSkillLevel) {
		this.desiredSkillLevel = desiredSkillLevel;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
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

	public EmployeeDetails getTrainerDetails() {
		return trainerDetails;
	}

	public void setTrainerDetails(EmployeeDetails trainerDetails) {
		this.trainerDetails = trainerDetails;
	}

	public SMWorkstations getWorkstation() {
		return workstation;
	}

	public void setWorkstation(SMWorkstations workstation) {
		this.workstation = workstation;
	}

	public BranchMaster getBranch() {
		return branch;
	}

	public void setBranch(BranchMaster branch) {
		this.branch = branch;
	}

	public DepartmentMaster getDept() {
		return dept;
	}

	public void setDept(DepartmentMaster dept) {
		this.dept = dept;
	}

	public SMOJTPlan getSmOjtPlan() {
		return smOjtPlan;
	}

	public void setSmOjtPlan(SMOJTPlan smOjtPlan) {
		this.smOjtPlan = smOjtPlan;
	}

	public Line getLine() {
		return line;
	}

	public void setLine(Line line) {
		this.line = line;
	}

}
