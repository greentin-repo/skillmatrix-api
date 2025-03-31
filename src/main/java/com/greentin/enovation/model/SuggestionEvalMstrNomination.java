package com.greentin.enovation.model;

import java.sql.Timestamp;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

/**
 * 
 * @author rakesh
 * @Desc  this tabel store nomination info through scheduler 
 *
 */

@Entity
public class SuggestionEvalMstrNomination {
	
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private int id;
	
	private int month;
	
	private int year;
	
	private Timestamp fromDate;
	
	private Timestamp toDate;
	
	private String status;
	
    private int branchId;
    
    private String nominationType; // SYSTEM_ASSESSMENT or JURY_ASSESSMENT
    
	@CreationTimestamp
	private Timestamp createdDate;
	
	@UpdateTimestamp
	private Timestamp updatedDate;
	
	public SuggestionEvalMstrNomination(){
		
	}
	
    public SuggestionEvalMstrNomination(int month,int year,Timestamp fromDate,Timestamp toDate,String status,int branchId){
		this.month=month;
		this.year=year;
		this.fromDate=fromDate;
		this.toDate=toDate;
		this.status=status;
		this.branchId=branchId;
	}
    
    public SuggestionEvalMstrNomination(int month,int year,String status,int branchId,String nominationType){
		this.month=month;
		this.year=year;
		this.status=status;
		this.branchId=branchId;
		this.nominationType=nominationType;
	}
    
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
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

	public Timestamp getFromDate() {
		return fromDate;
	}

	public void setFromDate(Timestamp fromDate) {
		this.fromDate = fromDate;
	}

	public Timestamp getToDate() {
		return toDate;
	}

	public void setToDate(Timestamp toDate) {
		this.toDate = toDate;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
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

	public String getNominationType() {
		return nominationType;
	}

	public void setNominationType(String nominationType) {
		this.nominationType = nominationType;
	}
}
