package com.greentin.enovation.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="master_survey_status")
public class SurveyStatusMaster {
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private int id;
	private String status;
	private String label;
	
	
	public SurveyStatusMaster() {
		super();
	}
	public SurveyStatusMaster(int id) {
		super();
		this.id = id;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getLabel() {
		return label;
	}
	public void setLabel(String label) {
		this.label = label;
	}
	

}
