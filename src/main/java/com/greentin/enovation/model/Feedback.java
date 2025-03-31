package com.greentin.enovation.model;

import java.util.Date;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Transient;
import org.hibernate.annotations.CreationTimestamp;
import com.fasterxml.jackson.annotation.JsonBackReference;

@Entity
public class Feedback {
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private int feedbackId;

	private String feedback;
	private int ratings;
	
	@ManyToOne()
	@JoinColumn(name="feedbackTypeId")
	@JsonBackReference
	private FeedbackType feedbackType;
	
	private int feedbackById;
	private int branchId;
	private int orgId;	
	@CreationTimestamp
	private Date createdDate;
	
	@Transient
	private int feedbackTypeId;
	
	private String userName;
	
	private String sourceType;
	
	private String orgName;
	
	private String srn;
	
	public Feedback() {
	}
	public Feedback(int feedbackTypeId) {
	super();
	this.feedbackTypeId = feedbackTypeId;
}


	public int getFeedbackTypeId() {
		return feedbackTypeId;
	}

	public void setFeedbackTypeId(int feedbackTypeId) {
		this.feedbackTypeId = feedbackTypeId;
	}
	public int getRatings() {
		return ratings;
	}
	public void setRatings(int ratings) {
		this.ratings = ratings;
	}
	
	public FeedbackType getFeedbackType() {
		return feedbackType;
	}
	public void setFeedbackType(FeedbackType feedbackType) {
		this.feedbackType = feedbackType;
	}
	public int getFeedbackById() {
		return feedbackById;
	}
	public void setFeedbackById(int feedbackById) {
		this.feedbackById = feedbackById;
	}
	public int getBranchId() {
		return branchId;
	}
	public void setBranchId(int branchId) {
		this.branchId = branchId;
	}
	public int getOrgId() {
		return orgId;
	}
	public void setOrgId(int orgId) {
		this.orgId = orgId;
	}
	public Date getCreatedDate() {
		return createdDate;
	}
	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}
	public int getFeedbackId() {
		return feedbackId;
	}
	public void setFeedbackId(int feedbackId) {
		this.feedbackId = feedbackId;
	}
	public String getFeedback() {
		return feedback;
	}
	public void setFeedback(String feedback) {
		this.feedback = feedback;
	}
	public String getSourceType() {
		return sourceType;
	}
	public void setSourceType(String sourceType) {
		this.sourceType = sourceType;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public String getOrgName() {
		return orgName;
	}
	public void setOrgName(String orgName) {
		this.orgName = orgName;
	}
	public String getSrn() {
		return srn;
	}
	public void setSrn(String srn) {
		this.srn = srn;
	}


}
