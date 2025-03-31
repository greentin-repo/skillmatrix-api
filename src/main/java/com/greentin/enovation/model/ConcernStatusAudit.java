package com.greentin.enovation.model;

import java.sql.Timestamp;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import com.fasterxml.jackson.annotation.JsonBackReference;

@Entity
public class ConcernStatusAudit {
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="stsadt_id" ,unique=true, nullable=false)
	private int stsadtId;
	
	@CreationTimestamp
	@Column( name="created_date") 
	private Timestamp createdDate;
	
	
	@UpdateTimestamp
	@Column(name="updated_date")
	private Timestamp updatedDate;

	@ManyToOne()
	@JoinColumn(name = "concern_id")
	@JsonBackReference
	private ConcernDetails concern_details;

	@ManyToOne()
	@JoinColumn(name = "emp_id")
	private EmployeeDetails emp_details;

	@ManyToOne()
	@JoinColumn(name = "assign_by")
	private EmployeeDetails assign_by;

	
	@ManyToOne()
	@JoinColumn(name = "sts_id")
	private StatusMaster status_master;

	private String summary;

	public int getStsadtId() {
		return stsadtId;
	}

	public void setStsadtId(int stsadtId) {
		this.stsadtId = stsadtId;
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

	public ConcernDetails getConcern_details() {
		return concern_details;
	}

	public void setConcern_details(ConcernDetails concern_details) {
		this.concern_details = concern_details;
	}

	public EmployeeDetails getEmp_details() {
		return emp_details;
	}

	public void setEmp_details(EmployeeDetails emp_details) {
		this.emp_details = emp_details;
	}
    
	
	public EmployeeDetails getAssign_by() {
		return assign_by;
	}

	public void setAssign_by(EmployeeDetails assign_by) {
		this.assign_by = assign_by;
	}

	public StatusMaster getStatus_master() {
		return status_master;
	}

	public void setStatus_master(StatusMaster status_master) {
		this.status_master = status_master;
	}

	public String getSummary() {
		return summary;
	}

	public void setSummary(String summary) {
		this.summary = summary;
	}
	
	
}
