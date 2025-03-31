package com.greentin.enovation.model;

import java.sql.Timestamp;
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

import com.greentin.enovation.model.dwm.Line;

@Entity
@Table
//@javax.persistence.Cacheable
public class MasterDocument {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;

	private String documentName;

	@ManyToOne
	@JoinColumn(name = "branch_id")
	private BranchMaster branch;

	@ManyToOne
	@JoinColumn(name = "owner_id")
	private EmployeeDetails owner;

	private int createdBy;

	private int updatedBy;

	@CreationTimestamp
	private Timestamp createdDate;

	@UpdateTimestamp
	private Timestamp updatedDate;

	@Transient
	private int ownerId;

	@Transient
	private int branchId;

	@ManyToOne
	@JoinColumn(name = "dept_id")
	private DepartmentMaster dept;
	
	@ManyToOne
	@JoinColumn(name = "line_id")
	private Line line;

	public MasterDocument() {
		super();
	}

	public MasterDocument(int id) {
		super();
		this.id = id;
	}

	public int getBranchId() {
		return branchId;
	}

	public void setBranchId(int branchId) {
		this.branchId = branchId;
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

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getDocumentName() {
		return documentName;
	}

	public void setDocumentName(String documentName) {
		this.documentName = documentName;
	}

	public BranchMaster getBranch() {
		return branch;
	}

	public void setBranch(BranchMaster branch) {
		this.branch = branch;
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

	public EmployeeDetails getOwner() {
		return owner;
	}

	public void setOwner(EmployeeDetails owner) {
		this.owner = owner;
	}

	public int getOwnerId() {
		return ownerId;
	}

	public void setOwnerId(int ownerId) {
		this.ownerId = ownerId;
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
