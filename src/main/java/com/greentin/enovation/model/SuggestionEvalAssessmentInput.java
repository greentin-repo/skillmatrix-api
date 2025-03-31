package com.greentin.enovation.model;

import java.sql.Timestamp;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Transient;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

/**
 * 
 * @author rakesh
 * @desc  this table store info about process branch wise like nomination date of branch1,jury assign date of of branch2,jury input date of of branch3
 */
@Entity
public class SuggestionEvalAssessmentInput {
	
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private int id;
	
	@ManyToOne
	@JoinColumn(name="sug_master_asses_id")
	private SuggestionEvalMstrAssessment sugMasterAss;
	
	private int day;
	
	private int branchId;
	
	@Transient
	private String name;
	
	@Transient
	private String description;
	
	@Transient
	private int assesId;
	
	@Transient
	private int sugMasterAssesId;
	
	@CreationTimestamp
	private Timestamp createdDate;
	
	@UpdateTimestamp
	private Timestamp updatedDate;


	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public SuggestionEvalMstrAssessment getSugMasterAss() {
		return sugMasterAss;
	}

	public void setSugMasterAss(SuggestionEvalMstrAssessment sugMasterAss) {
		this.sugMasterAss = sugMasterAss;
	}

	public int getDay() {
		return day;
	}

	public void setDay(int day) {
		this.day = day;
	}

	public int getBranchId() {
		return branchId;
	}
    
	public void setBranchId(int branchId) {
		this.branchId = branchId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public int getAssesId() {
		return assesId;
	}

	public void setAssesId(int assesId) {
		this.assesId = assesId;
	}

	public int getSugMasterAssesId() {
		return sugMasterAssesId;
	}

	public void setSugMasterAssesId(int sugMasterAssesId) {
		this.sugMasterAssesId = sugMasterAssesId;
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
	

}
