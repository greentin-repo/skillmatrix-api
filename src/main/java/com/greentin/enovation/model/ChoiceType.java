package com.greentin.enovation.model;

import java.sql.Timestamp;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
//@javax.persistence.Cacheable
public class ChoiceType {
	@Id 
	@GeneratedValue(strategy=GenerationType.SEQUENCE)
	private int choiceId;     

	private String choiceType;

	private Timestamp createdDate;

	private Timestamp updatedDate;

	public ChoiceType() {

	}
	public ChoiceType(int choiceId) {
		this.choiceId = choiceId;
	}

	public int getChoiceId() {
		return choiceId;
	}

	public void setChoiceId(int choiceId) {
		this.choiceId = choiceId;
	}

	public String getChoiceType() {
		return choiceType;
	}

	public void setChoiceType(String choiceType) {
		this.choiceType = choiceType;
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
