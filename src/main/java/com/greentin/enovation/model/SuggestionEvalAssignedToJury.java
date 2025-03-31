package com.greentin.enovation.model;



import java.sql.Timestamp;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

/**
 * 
 * @author rakesh
 * @desc sugg will assign to jury through scheduler
 */
@Entity
public class SuggestionEvalAssignedToJury {
	
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private int id;
	
	//jury empId 
	private int empId;
	
	//primary key of SuggestionEvalNomination
	private int sugNominationId;
	
	//primary key of SuggestionEvalMstrNomination
	private int nominationId;
	
	private int sugId;
	
	private double totalRating;
	
	private String status;
	
	private int phRatingId;
	
	private double phRating;
	
	private String inputType;
	
	@Lob
	private String ptpSummary;

	@CreationTimestamp
	private Timestamp createdDate;
	
	@UpdateTimestamp
	private Timestamp updatedDate;

	
	public SuggestionEvalAssignedToJury(){
		
	}
	
    public SuggestionEvalAssignedToJury(int empId,int sugNominationId,int nominationId,int sugId,String status,String inputType){
		this.empId=empId;
		this.sugNominationId=sugNominationId;
		this.nominationId=nominationId;
		this.sugId=sugId;
		this.status=status;
		this.inputType=inputType;
	}
	
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	

	public int getEmpId() {
		return empId;
	}

	public void setEmpId(int empId) {
		this.empId = empId;
	}

	public int getSugNominationId() {
		return sugNominationId;
	}

	public void setSugNominationId(int sugNominationId) {
		this.sugNominationId = sugNominationId;
	}

	public int getSugId() {
		return sugId;
	}

	public void setSugId(int sugId) {
		this.sugId = sugId;
	}

	public int getNominationId() {
		return nominationId;
	}

	public void setNominationId(int nominationId) {
		this.nominationId = nominationId;
	}

	
	public double getTotalRating() {
		return totalRating;
	}

	public void setTotalRating(double totalRating) {
		this.totalRating = totalRating;
	}

	public int getPhRatingId() {
		return phRatingId;
	}

	public void setPhRatingId(int phRatingId) {
		this.phRatingId = phRatingId;
	}

	public double getPhRating() {
		return phRating;
	}

	public void setPhRating(double phRating) {
		this.phRating = phRating;
	}

	public String getInputType() {
		return inputType;
	}

	public void setInputType(String inputType) {
		this.inputType = inputType;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
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

	public String getPtpSummary() {
		return ptpSummary;
	}

	public void setPtpSummary(String ptpSummary) {
		this.ptpSummary = ptpSummary;
	}
}
