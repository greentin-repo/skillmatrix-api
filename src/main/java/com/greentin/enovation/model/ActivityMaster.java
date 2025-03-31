package com.greentin.enovation.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
//@javax.persistence.Cacheable
public class ActivityMaster {

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private int id;

	private String activityName;

	private String symbol;



	public ActivityMaster() {
		super();
	}


	public ActivityMaster(int id) {
		super();
		this.id = id;
	}


	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getActivityName() {
		return activityName;
	}

	public void setActivityName(String activityName) {
		this.activityName = activityName;
	}

	public String getSymbol() {
		return symbol;
	}

	public void setSymbol(String symbol) {
		this.symbol = symbol;
	}



}
