package com.greentin.enovation.model;

import java.sql.Timestamp;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

@Entity
public class SuggestionEvalWinner {
	
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private long id;
	
	//primary key of SuggestionEvalMstrNomination table
	private int nominationId;
	
	private int sugId;
	
	private int branchId;
	
	private int month;
	
	private int year;
	
	@Column(name = "total_jury_score", columnDefinition = "decimal(16,2) default 0.00")
	private double totalJuryScore;
	
	@Column(name = "brownie_points", columnDefinition = "decimal(16,2) default 0.00")
	private double browniePoints;
	
	@Column(name = "total_points", columnDefinition = "decimal(16,2) default 0.00")
	private double totalPoints;
	
	@Column(name = "impact_per", columnDefinition = "decimal(16,2) default 0.00")
	private double impactPer; 
	
	@Column(name="group_name")
	private String groupName;
	
	@Column(name="created_by")
	private int createdBy;
	
	@Column(name = "assessment_created_date")
	private Date assessmentCreatedDate;
	
	@CreationTimestamp
	private Timestamp createdDate;
	
	@UpdateTimestamp
	private Timestamp updatedDate;

	public SuggestionEvalWinner() {
		
	}
	
	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public int getNominationId() {
		return nominationId;
	}

	public void setNominationId(int nominationId) {
		this.nominationId = nominationId;
	}

	public int getSugId() {
		return sugId;
	}

	public void setSugId(int sugId) {
		this.sugId = sugId;
	}

	public int getBranchId() {
		return branchId;
	}

	public void setBranchId(int branchId) {
		this.branchId = branchId;
	}

	public int getMonth() {
		return month;
	}

	public void setMonth(int month) {
		this.month = month;
	}

	public int getYear() {
		return year;
	}

	public void setYear(int year) {
		this.year = year;
	}

	public double getTotalJuryScore() {
		return totalJuryScore;
	}

	public void setTotalJuryScore(double totalJuryScore) {
		this.totalJuryScore = totalJuryScore;
	}

	public double getBrowniePoints() {
		return browniePoints;
	}

	public void setBrowniePoints(double browniePoints) {
		this.browniePoints = browniePoints;
	}

	public double getTotalPoints() {
		return totalPoints;
	}

	public void setTotalPoints(double totalPoints) {
		this.totalPoints = totalPoints;
	}

	public double getImpactPer() {
		return impactPer;
	}

	public void setImpactPer(double impactPer) {
		this.impactPer = impactPer;
	}

	public String getGroupName() {
		return groupName;
	}

	public void setGroupName(String groupName) {
		this.groupName = groupName;
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

	public int getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(int createdBy) {
		this.createdBy = createdBy;
	}

	public Date getAssessmentCreatedDate() {
		return assessmentCreatedDate;
	}

	public void setAssessmentCreatedDate(Date assessmentCreatedDate) {
		this.assessmentCreatedDate = assessmentCreatedDate;
	}
}

