package com.greentin.enovation.model.skillMatrix;

import java.math.BigDecimal;
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
 * @author Rajdeep MD ,07 Aug 2023 ,03:20 PM
 * @desc
 * @comments
 */

@Entity
@Table(name = "sm_workstations")
public class SMWorkstations {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private long id;

	@ManyToOne
	@JoinColumn(name = "branch_id")
	private BranchMaster branch;

	@ManyToOne
	@JoinColumn(name = "dept_id")
	private DepartmentMaster dept;

	@ManyToOne
	@JoinColumn(name = "line_id")
	private Line line;

	@Column(name = "workstation", length = 250)
	private String workstation;

	@Column(name = "machine_index", precision = 16, scale = 2)
	private double machineIndex;

	@ManyToOne
	@JoinColumn(name = "req_skill_level_id")
	private SMSkillLevel reqSkillLevel;

	@Column(name = "required_workforce", precision = 16, scale = 2)
	private double requiredWorkforce;

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

	@Column(name = "is_active")
	private Boolean isActive;
	
	@Column(name = "machine_count", columnDefinition = "int default 1")
	private int machineCount;

	public SMWorkstations() {
		super();
	}

	public SMWorkstations(long id) {
		super();
		this.id = id;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public DepartmentMaster getDept() {
		return dept;
	}

	public void setDept(DepartmentMaster dept) {
		this.dept = dept;
	}

	public String getWorkstation() {
		return workstation;
	}

	public void setWorkstation(String workstation) {
		this.workstation = workstation;
	}

	public SMSkillLevel getReqSkillLevel() {
		return reqSkillLevel;
	}

	public void setReqSkillLevel(SMSkillLevel reqSkillLevel) {
		this.reqSkillLevel = reqSkillLevel;
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

	public BranchMaster getBranch() {
		return branch;
	}

	public void setBranch(BranchMaster branch) {
		this.branch = branch;
	}

	public Line getLine() {
		return line;
	}

	public void setLine(Line line) {
		this.line = line;
	}

	public double getRequiredWorkforce() {
		return requiredWorkforce;
	}

	public void setRequiredWorkforce(double requiredWorkforce) {
		this.requiredWorkforce = requiredWorkforce;
	}

	public double getMachineIndex() {
		return machineIndex;
	}

	public void setMachineIndex(double machineIndex) {
		this.machineIndex = machineIndex;
	}

	public int getMachineCount() {
		return machineCount;
	}

	public void setMachineCount(int machineCount) {
		this.machineCount = machineCount;
	}

}
