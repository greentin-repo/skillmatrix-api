package com.greentin.enovation.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class RegistrationSource {

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private int id;
	
	private String name;
	
	private String partnerEmailId;
	
	public RegistrationSource() {
		super();
	}

	public RegistrationSource(int id) {
		this.id=id;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPartnerEmailId() {
		return partnerEmailId;
	}

	public void setPartnerEmailId(String partnerEmailId) {
		this.partnerEmailId = partnerEmailId;
	}
	
	
}
