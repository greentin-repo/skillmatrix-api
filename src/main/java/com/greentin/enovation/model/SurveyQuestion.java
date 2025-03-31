package com.greentin.enovation.model;

import java.sql.Timestamp;
import java.util.List;
import java.util.Set;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.Table;
import javax.persistence.Transient;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;

@Entity
@Table(name = "survey_question")
public class SurveyQuestion {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int quesId;

	@Column(length = 1800)
	private String question;

	@ManyToOne(cascade = CascadeType.REMOVE)
	@JoinColumn(name = "surveyId")
	@JsonBackReference
	private SurveyDetails survayDetails;

	@ManyToOne()
	@JoinColumn(name = "choiceTypeId")
	private ChoiceType choiceType;

	@ManyToOne()
	@JoinColumn(name = "createdBy")
	private EmployeeDetails createdBy;

	@ManyToOne()
	@JoinColumn(name = "updatedBy")
	private EmployeeDetails updatedBy;

	private Timestamp createdDate;

	private Timestamp updatedDate;
	
	@Transient
	private int scale;

	@OneToMany(mappedBy = "surveyQuestion",cascade=CascadeType.ALL,fetch = FetchType.LAZY)
	@JsonManagedReference
	@OrderBy("optId ASC")
	private Set<SurveyQuestionOptions> surveyOptions;

	@Transient
	private int createdById;

	@Transient
	private int updatedById;

	@Transient
	private int surveyId;

	@Transient
	private int choiceTypeId;

	@Transient
	private List<SurveyQuestionOptions> optionsList;

	private int isMandatory;

	public SurveyQuestion() {

	}

	public SurveyQuestion(int quesId) {
		this.quesId = quesId;
	}

	public int getQuesId() {
		return quesId;
	}

	public void setQuesId(int quesId) {
		this.quesId = quesId;
	}

	public String getQuestion() {
		return question;
	}

	public void setQuestion(String question) {
		this.question = question;
	}

	public SurveyDetails getSurvayDetails() {
		return survayDetails;
	}

	public void setSurvayDetails(SurveyDetails survayDetails) {
		this.survayDetails = survayDetails;
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

	public Set<SurveyQuestionOptions> getSurveyOptions() {
		return surveyOptions;
	}

	public void setSurveyOptions(Set<SurveyQuestionOptions> surveyOptions) {
		this.surveyOptions = surveyOptions;
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

	public int getSurveyId() {
		return surveyId;
	}

	public void setSurveyId(int surveyId) {
		this.surveyId = surveyId;
	}

	public int getChoiceTypeId() {
		return choiceTypeId;
	}

	public void setChoiceTypeId(int choiceTypeId) {
		this.choiceTypeId = choiceTypeId;
	}

	public List<SurveyQuestionOptions> getOptionsList() {
		return optionsList;
	}

	public void setOptionsList(List<SurveyQuestionOptions> optionsList) {
		this.optionsList = optionsList;
	}

	public int getIsMandatory() {
		return isMandatory;
	}

	public void setIsMandatory(int isMandatory) {
		this.isMandatory = isMandatory;
	}

	public int getScale() {
		return scale;
	}

	public void setScale(int scale) {
		this.scale = scale;
	}

}
