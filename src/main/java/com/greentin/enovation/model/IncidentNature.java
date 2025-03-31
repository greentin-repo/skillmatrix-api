package com.greentin.enovation.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="master_incident_nature")
public class IncidentNature {

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="id",unique=true, nullable=false)
	private int id;
	
	private String natureType;

	public IncidentNature() {
		super();
	}

	public IncidentNature(int id) {
		super();
		this.id = id;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getNatureType() {
		return natureType;
	}

	public void setNatureType(String natureType) {
		this.natureType = natureType;
	}
	
	
	
	
}
