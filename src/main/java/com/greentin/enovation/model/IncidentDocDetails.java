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

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.greentin.enovation.config.EnovationConfig;

@Entity
@Table(name="incident_doc_details")
public class IncidentDocDetails {

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="inc_doc_det_id",unique=true,nullable=false)
	private int incDocDetId;
	
	private String url;
	
	
	private Timestamp createdDate;
	
	private Timestamp updatedDate;
	
	
	@ManyToOne
	@JoinColumn(name="ince_id")
	@JsonBackReference
	private IncidentDetails incDet;
	
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="audit_sts_id")
	private IncidentAuditStatus ias;

	
	public IncidentDocDetails() {
		super();
	}
	

	public IncidentDocDetails(int incDocDetId) {
		super();
		this.incDocDetId = incDocDetId;
	}



	public int getIncDocDetId() {
		return incDocDetId;
	}

	public void setIncDocDetId(int incDocDetId) {
		this.incDocDetId = incDocDetId;
	}

	public String getUrl() {
		if(url!=null) { 
			return EnovationConfig.buddyConfig.get("ProfilePicPathUrl")+url;	
			}	
		else {
			return url;
		}
	}

	public void setUrl(String url) {
		this.url = url;
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

	public IncidentDetails getIncDet() {
		return incDet;
	}

	public void setIncDet(IncidentDetails incDet) {
		this.incDet = incDet;
	}


	public IncidentAuditStatus getIas() {
		return ias;
	}


	public void setIas(IncidentAuditStatus ias) {
		this.ias = ias;
	}

	
	
	
	
}
