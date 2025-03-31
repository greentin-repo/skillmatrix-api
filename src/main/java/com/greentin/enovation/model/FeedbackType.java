package com.greentin.enovation.model;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class FeedbackType {

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private int feedbackTypeId;

	private String feedbackType;
	

	public FeedbackType(int feedbackTypeId) {
		super();
		this.feedbackTypeId = feedbackTypeId;
	}

	public FeedbackType() {
		
	}
	
	public int getFeedbackTypeId() {
		return feedbackTypeId;
	}

	public void setFeedbackTypeId(int feedbackTypeId) {
		this.feedbackTypeId = feedbackTypeId;
	}

	public String getFeedbackType() {
		return feedbackType;
	}

	public void setFeedbackType(String feedbackType) {
		this.feedbackType = feedbackType;
	}
	
	
}
