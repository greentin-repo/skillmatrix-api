package com.greentin.enovation.model;

import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Transient;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

/**
 * 
 * @author rakesh
 * @desc  this table contain store  info , when employee will nominate self for  sugg
 *
 */


@Entity
public class SuggestionEvalNomination {
	
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private int id;
	
	//primary key of SuggestionEvalMstrNomination table
	private int nominationId;
	
	private int sugId;
	
	private int empId;
	
	private int branchId;
	
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
	
	@Transient
	private String inputType;
		
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

	public int getEmpId() {
		return empId;
	}

	public void setEmpId(int empId) {
		this.empId = empId;
	}

	public int getBranchId() {
		return branchId;
	}

	public void setBranchId(int branchId) {
		this.branchId = branchId;
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

	public String getInputType() {
		return inputType;
	}

	public void setInputType(String inputType) {
		this.inputType = inputType;
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
}
