package com.greentin.enovation.bo.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;


@JsonIgnoreProperties(ignoreUnknown = true)
//@JsonInclude(JsonInclude.Include.NON_NULL)
public class SuggestionDetailsBO {

	private int sugId;
	
	private String suggestionNumber;

	private String suggestionTitle;
	
	private String suggestion;
	
    private String assignDate;
	
	private String actionDate;
	
	private String status;
	
	private String createdDate;
	
	private String implementedDate;

	public int getSugId() {
		return sugId;
	}

	public void setSugId(int sugId) {
		this.sugId = sugId;
	}

	public String getSuggestionNumber() {
		return suggestionNumber;
	}

	public void setSuggestionNumber(String suggestionNumber) {
		this.suggestionNumber = suggestionNumber;
	}

	public String getSuggestionTitle() {
		return suggestionTitle;
	}

	public void setSuggestionTitle(String suggestionTitle) {
		this.suggestionTitle = suggestionTitle;
	}

	public String getSuggestion() {
		return suggestion;
	}

	public void setSuggestion(String suggestion) {
		this.suggestion = suggestion;
	}

	public String getAssignDate() {
		return assignDate;
	}

	public void setAssignDate(String assignDate) {
		this.assignDate = assignDate;
	}

	public String getActionDate() {
		return actionDate;
	}

	public void setActionDate(String actionDate) {
		this.actionDate = actionDate;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(String createdDate) {
		this.createdDate = createdDate;
	}

	public String getImplementedDate() {
		return implementedDate;
	}

	public void setImplementedDate(String implementedDate) {
		this.implementedDate = implementedDate;
	}

	public SuggestionDetailsBO() {
		super();
	}

	public SuggestionDetailsBO(int sugId, String suggestionNumber, String suggestionTitle,
			String createdDate, String implementedDate, String status) {
		super();
		this.sugId = sugId;
		this.suggestionNumber = suggestionNumber;
		this.suggestionTitle = suggestionTitle;
		this.status = status;
		this.createdDate = createdDate;
		this.implementedDate = implementedDate;
	}

	
	
}
