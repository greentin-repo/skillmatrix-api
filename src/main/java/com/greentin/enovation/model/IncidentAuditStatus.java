package com.greentin.enovation.model;

import java.sql.Timestamp;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import com.fasterxml.jackson.annotation.JsonBackReference;

@Entity
@Table(name="incident_audit_status")
public class IncidentAuditStatus {

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="stsadt_id",unique=true, nullable=false)
	private int stsadtId;

	@CreationTimestamp
	private Timestamp createdDate;

	@Lob
	private String summary;

	@UpdateTimestamp
	private Timestamp udpatedDate;

	@ManyToOne()
	@JoinColumn(name = "emp_id")
	private EmployeeDetails empDet;

	@ManyToOne()
	@JoinColumn(name = "ince_id")
	@JsonBackReference
	private IncidentDetails inceDet;

	@ManyToOne(fetch=FetchType.EAGER)
	@JoinColumn(name="sts_id")
	private StatusMaster statusMaster;

	private String actionStatus;
	
	@Lob
	private String correctiveActionDet;

	@Lob
	private String rootCause;
	
	@Transient
	private int count;
		
	public IncidentAuditStatus() {
		super();
	}
	
	public IncidentAuditStatus(IncidentDetails inceDet,EmployeeDetails empDet, StatusMaster statusMaster,String actionStatus) {
		super();
		this.inceDet=inceDet;
		this.empDet = empDet;
		this.statusMaster = statusMaster;
		this.actionStatus = actionStatus;
	}
	
	public IncidentAuditStatus(IncidentDetails inceDet,EmployeeDetails empDet, StatusMaster statusMaster,
			String actionStatus,String correctiveActionDet,String rootCause) {
		super();
		this.inceDet=inceDet;
		this.empDet = empDet;
		this.statusMaster = statusMaster;
		this.actionStatus = actionStatus;
		this.correctiveActionDet = correctiveActionDet;
		this.rootCause = rootCause;
	}
	
	
	public IncidentAuditStatus(IncidentDetails inceDet,EmployeeDetails empDet, 
			StatusMaster statusMaster,String actionStatus,String summary) {
		super();
		this.inceDet=inceDet;
		this.empDet = empDet;
		this.statusMaster = statusMaster;
		this.actionStatus = actionStatus;
		this.summary = summary;
	}
	

	public IncidentAuditStatus(int stsadtId) {
		super();
		this.stsadtId = stsadtId;
	}

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

	public String getSummary() {
		return summary;
	}

	public void setSummary(String summary) {
		this.summary = summary;
	}

	public Timestamp getUdpatedDate() {
		return udpatedDate;
	}

	public void setUdpatedDate(Timestamp udpatedDate) {
		this.udpatedDate = udpatedDate;
	}

	public EmployeeDetails getEmpDet() {
		return empDet;
	}

	public void setEmpDet(EmployeeDetails empDet) {
		this.empDet = empDet;
	}

	public IncidentDetails getInceDet() {
		return inceDet;
	}

	public void setInceDet(IncidentDetails inceDet) {
		this.inceDet = inceDet;
	}

	public StatusMaster getStatusMaster() {
		return statusMaster;
	}

	public void setStatusMaster(StatusMaster statusMaster) {
		this.statusMaster = statusMaster;
	}

	public String getActionStatus() {
		return actionStatus;
	}

	public void setActionStatus(String actionStatus) {
		this.actionStatus = actionStatus;
	}

	public int getCount() {
		return count;
	}

	public void setCount(int count) {
		this.count = count;
	}

	public String getCorrectiveActionDet() {
		return correctiveActionDet;
	}

	public void setCorrectiveActionDet(String correctiveActionDet) {
		this.correctiveActionDet = correctiveActionDet;
	}

	public String getRootCause() {
		return rootCause;
	}

	public void setRootCause(String rootCause) {
		this.rootCause = rootCause;
	}
	
	
}
