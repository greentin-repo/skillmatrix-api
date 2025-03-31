package com.greentin.enovation.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonBackReference;

@Entity
@Table
public class SurveyIntendedFor {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;

	private int isDeactive;
	
	@ManyToOne()
	@JoinColumn(name = "surveyId")
	@JsonBackReference
	private SurveyDetails surveyDetails;

	@ManyToOne()
	@JoinColumn(name = "intendedFor")
	private DepartmentMaster intendedFor;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public SurveyDetails getSurveyDetails() {
		return surveyDetails;
	}

	public void setSurveyDetails(SurveyDetails surveyDetails) {
		this.surveyDetails = surveyDetails;
	}

	public DepartmentMaster getIntendedFor() {
		return intendedFor;
	}

	public void setIntendedFor(DepartmentMaster intendedFor) {
		this.intendedFor = intendedFor;
	}

	public int getIsDeactive() {
		return isDeactive;
	}

	public void setIsDeactive(int isDeactive) {
		this.isDeactive = isDeactive;
	}

	
}
