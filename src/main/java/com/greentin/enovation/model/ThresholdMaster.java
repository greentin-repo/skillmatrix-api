package com.greentin.enovation.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="master_threshold")
public class ThresholdMaster {

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private int id;
	
	private String thresholdKey;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getThresholdKey() {
		return thresholdKey;
	}

	public void setThresholdKey(String thresholdKey) {
		this.thresholdKey = thresholdKey;
	}
	
}
