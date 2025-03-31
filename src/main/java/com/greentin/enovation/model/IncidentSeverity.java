package com.greentin.enovation.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="master_incident_severity")
public class IncidentSeverity {

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="id",unique=true, nullable=false)
	private int id;
	
	private String severityType;

	
	public IncidentSeverity() {
		super();
	}

	public IncidentSeverity(int id) {
		super();
		this.id = id;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getSeverityType() {
		return severityType;
	}

	public void setSeverityType(String severityType) {
		this.severityType = severityType;
	}
	
	
	
	
	
}
