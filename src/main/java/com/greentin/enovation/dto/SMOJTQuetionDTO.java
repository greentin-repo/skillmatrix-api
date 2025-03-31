package com.greentin.enovation.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

/**
 * @author Anant K August 31, 2023
 * @desc SMOJTQuetionDTO
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class SMOJTQuetionDTO {

	private long questionId;

	private long optionId;
	
	private String queType;
	
	private long ojtAssQueId;
	
	private double queMarks;

	public long getQuestionId() {
		return questionId;
	}

	public void setQuestionId(long questionId) {
		this.questionId = questionId;
	}

	public long getOptionId() {
		return optionId;
	}

	public void setOptionId(long optionId) {
		this.optionId = optionId;
	}

	public String getQueType() {
		return queType;
	}

	public void setQueType(String queType) {
		this.queType = queType;
	}

	public long getOjtAssQueId() {
		return ojtAssQueId;
	}

	public void setOjtAssQueId(long ojtAssQueId) {
		this.ojtAssQueId = ojtAssQueId;
	}

	public double getQueMarks() {
		return queMarks;
	}

	public void setQueMarks(double queMarks) {
		this.queMarks = queMarks;
	}
	
	
}
