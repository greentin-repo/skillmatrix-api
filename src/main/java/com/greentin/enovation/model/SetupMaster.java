package com.greentin.enovation.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "setupmaster")
//@javax.persistence.Cacheable
public class SetupMaster {

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	@Column(name="setup_id" ,unique=true, nullable=false)
	private int setupId;
	
	@Column(unique=true)
	private String setupName;
	
	private int status;

	public SetupMaster() {
		
	}
	public SetupMaster(int setupId) {
		this.setupId=setupId;
	}

	public int getSetupId() {
		return setupId;
	}

	public void setSetupId(int id) {
		this.setupId = id;
	}

	public String getSetupName() {
		return setupName;
	}

	public void setSetupName(String name) {
		this.setupName = name;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

}
