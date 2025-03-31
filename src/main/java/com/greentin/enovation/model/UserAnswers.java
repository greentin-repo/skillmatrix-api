package com.greentin.enovation.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Transient;
import com.fasterxml.jackson.annotation.JsonBackReference;

@Entity
public class UserAnswers {

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private int userAnswerId;

	private int isDeactive;

	@ManyToOne()
	@JoinColumn(name = "userSurveyId" )
	@JsonBackReference
	private UserSurveyDetails empSurveyAnswer;

	@ManyToOne()
	@JoinColumn(name = "optId ")
	private SurveyQuestionOptions surveyQueOpt;

	@ManyToOne
	@JoinColumn(name="quesId")
	private SurveyQuestion surveyQuestion;

	private String optText;

	@Transient
	private int empAnswerId;

	@Transient
	private int optId;

	@Transient
	private int quesId;

	private String ratings;

	public int getIsDeactive() {
		return isDeactive;
	}

	public void setIsDeactive(int isDeactive) {
		this.isDeactive = isDeactive;
	}

	public int getUserAnswerId() {
		return userAnswerId;
	}

	public void setUserAnswerId(int empSurveyAnsOptId) {
		this.userAnswerId = empSurveyAnsOptId;
	}

	public UserSurveyDetails getEmpSurveyAnswer() {
		return empSurveyAnswer;
	}

	public void setEmpSurveyAnswer(UserSurveyDetails empSurveyAnswer) {
		this.empSurveyAnswer = empSurveyAnswer;
	}

	public SurveyQuestionOptions getSurveyQueOpt() {
		return surveyQueOpt;
	}

	public void setSurveyQueOpt(SurveyQuestionOptions surveyQueOpt) {
		this.surveyQueOpt = surveyQueOpt;
	}

	public SurveyQuestion getSurveyQuestion() {
		return surveyQuestion;
	}

	public void setSurveyQuestion(SurveyQuestion surveyQuestion) {
		this.surveyQuestion = surveyQuestion;
	}

	public String getOptText() {
		return optText;
	}

	public void setOptText(String optText) {
		this.optText = optText;
	}

	public int getEmpAnswerId() {
		return empAnswerId;
	}

	public void setEmpAnswerId(int empAnswerId) {
		this.empAnswerId = empAnswerId;
	}

	public int getOptId() {
		return optId;
	}

	public void setOptId(int optId) {
		this.optId = optId;
	}

	public int getQuesId() {
		return quesId;
	}

	public void setQuesId(int quesId) {
		this.quesId = quesId;
	}

	public String getRatings() {
		return ratings;
	}

	public void setRatings(String ratings) {
		this.ratings = ratings;
	}

}
