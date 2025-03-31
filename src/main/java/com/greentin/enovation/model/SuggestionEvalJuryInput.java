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
 *
 */
@Entity
public class SuggestionEvalJuryInput {

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private int id;
	
	//Primary key of SuggestionEvalAssignedToJury 
	@ManyToOne
	@JoinColumn(name="sug_assign_to_jury_id")
	private SuggestionEvalAssignedToJury sugAssignedToJury;
	
	//primary key of SuggestionEvalMstrParam
	private int parameterId;
	
	//primary key of SuggestionEvalMstrRatings 
	private int ratingId;
	
	//Jury's given input
	private int juryRating;
	
	@Transient
	private double phRating;
	
	@Transient
	private int phRatingId;
	
	@Transient
	private int isPlantHead;
	
	@CreationTimestamp
	private Timestamp createdDate;
	
	@UpdateTimestamp
	private Timestamp updatedDate;
	
	@Transient
	private double totalRating;
	
	@Transient
	private int browniePoints;
	
	
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public SuggestionEvalAssignedToJury getSugAssignedToJury() {
		return sugAssignedToJury;
	}

	public void setSugAssignedToJury(SuggestionEvalAssignedToJury sugAssignedToJury) {
		this.sugAssignedToJury = sugAssignedToJury;
	}

	public int getParameterId() {
		return parameterId;
	}

	public void setParameterId(int parameterId) {
		this.parameterId = parameterId;
	}

	public int getRatingId() {
		return ratingId;
	}

	public void setRatingId(int ratingId) {
		this.ratingId = ratingId;
	}

	public int getJuryRating() {
		return juryRating;
	}

	public void setJuryRating(int juryRating) {
		this.juryRating = juryRating;
	}

	public double getPhRating() {
		return phRating;
	}

	public void setPhRating(double phRating) {
		this.phRating = phRating;
	}

	public int getPhRatingId() {
		return phRatingId;
	}

	public void setPhRatingId(int phRatingId) {
		this.phRatingId = phRatingId;
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

	public int getIsPlantHead() {
		return isPlantHead;
	}

	public void setIsPlantHead(int isPlantHead) {
		this.isPlantHead = isPlantHead;
	}

	public double getTotalRating() {
		return totalRating;
	}

	public void setTotalRating(double totalRating) {
		this.totalRating = totalRating;
	}

	public int getBrowniePoints() {
		return browniePoints;
	}

	public void setBrowniePoints(int browniePoints) {
		this.browniePoints = browniePoints;
	}


}
