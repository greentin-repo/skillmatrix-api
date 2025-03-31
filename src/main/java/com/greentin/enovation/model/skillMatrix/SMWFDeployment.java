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
import com.greentin.enovation.model.EmployeeDetails;
import com.greentin.enovation.model.dwm.Line;

/**
 * @author Anant K. 04 September 2023 ,10:15 AM
 * @desc
 * @comments
 */

@Entity
@Table(name = "sm_wf_deployment")
public class SMWFDeployment {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private long id;

	@ManyToOne
	@JoinColumn(name = "emp_id")
	private EmployeeDetails empDetails;

	@ManyToOne
	@JoinColumn(name = "shift_id")
	private SMShifts shift;

	@ManyToOne
	@JoinColumn(name = "workstations_id")
	private SMWorkstations workstations;

	@Column(name = "from_date")
	private Timestamp fromDate;

	@Column(name = "to_date")
	private Timestamp toDate;

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
	@JoinColumn(name = "dept_id")
	private DepartmentMaster dept;

	@ManyToOne
	@JoinColumn(name = "line_id")
	private Line line;

	public SMWFDeployment() {
		super();
	}

	public SMWFDeployment(long id) {
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

	public SMShifts getShift() {
		return shift;
	}

	public void setShift(SMShifts shift) {
		this.shift = shift;
	}

	public SMWorkstations getWorkstations() {
		return workstations;
	}

	public void setWorkstations(SMWorkstations workstations) {
		this.workstations = workstations;
	}

	public Timestamp getFromDate() {
		return fromDate;
	}

	public void setFromDate(Timestamp fromDate) {
		this.fromDate = fromDate;
	}

	public Timestamp getToDate() {
		return toDate;
	}

	public void setToDate(Timestamp toDate) {
		this.toDate = toDate;
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

}
