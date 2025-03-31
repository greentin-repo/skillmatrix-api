package com.greentin.enovation.model;

import java.sql.Timestamp;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;
import com.fasterxml.jackson.annotation.JsonBackReference;

@Entity
@Table
public class SurveyQuestionOptions {

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private int optId;

	@Column(length=2400)
	private String optText;
	
	@ManyToOne
	@JoinColumn(name="quesId")
	@JsonBackReference
	private SurveyQuestion surveyQuestion;

	@ManyToOne()
	@JoinColumn(name="choiceTypeId")
	private ChoiceType choiceType;

	@ManyToOne()
	@JoinColumn(name="createdBy")
	private EmployeeDetails createdBy;  

	@ManyToOne()
	@JoinColumn(name="updatedBy")
	private EmployeeDetails updatedBy ;

	private Timestamp createdDate;

	private Timestamp  updatedDate;
	
	private int ratingScale;
	
	@Transient
	private int createdById;
	
	@Transient
	private int updatedById;
	
	@Transient
	private int questionId;
	
	@Transient
	private int choiceTypeId;


	public int getOptId() {
		return optId;
	}
	public void setOptId(int optId) {
		this.optId = optId;
	}
	public String getOptText() {
		return optText;
	}
	public void setOptText(String optText) {
		this.optText = optText;
	}
	public SurveyQuestion getSurveyQuestion() {
		return surveyQuestion;
	}
	public void setSurveyQuestion(SurveyQuestion surveyQuestion) {
		this.surveyQuestion = surveyQuestion;
	}
	public ChoiceType getChoiceType() {
		return choiceType;
	}
	public void setChoiceType(ChoiceType choiceType) {
		this.choiceType = choiceType;
	}
	public EmployeeDetails getCreatedBy() {
		return createdBy;
	}
	public void setCreatedBy(EmployeeDetails createdBy) {
		this.createdBy = createdBy;
	}
	public EmployeeDetails getUpdatedBy() {
		return updatedBy;
	}
	public void setUpdatedBy(EmployeeDetails updatedBy) {
		this.updatedBy = updatedBy;
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
	public int getCreatedById() {
		return createdById;
	}
	public void setCreatedById(int createdById) {
		this.createdById = createdById;
	}
	public int getUpdatedById() {
		return updatedById;
	}
	public void setUpdatedById(int updatedById) {
		this.updatedById = updatedById;
	}
	public int getQuestionId() {
		return questionId;
	}
	public void setQuestionId(int questionId) {
		this.questionId = questionId;
	}
	public int getChoiceTypeId() {
		return choiceTypeId;
	}
	public void setChoiceTypeId(int choiceTypeId) {
		this.choiceTypeId = choiceTypeId;
	}
	
	public int getRatingScale() {
		return ratingScale;
	}
	public void setRatingScale(int ratingScale) {
		this.ratingScale = ratingScale;
	}
	
    


}
