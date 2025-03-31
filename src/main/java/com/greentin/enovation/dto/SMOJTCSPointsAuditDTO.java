package com.greentin.enovation.dto;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class SMOJTCSPointsAuditDTO {

	private long id;

	private long skillingAuditId;

	private long checksheetPointId;

	private long ojtPointId;

	private String status;

	private String completedDate;

	private String assignedDate;

	private int dayNo;

	private String keyPoint;

	private String comment;

	private long pointAuditId;

	private String reference;

	private String trainerStatus;

	private String oeStatus;
	
	private String oeComment;
	
	private String TrainerActComment;
	
	private String TrainerProdComment;
	

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public long getSkillingAuditId() {
		return skillingAuditId;
	}

	public void setSkillingAuditId(long skillingAuditId) {
		this.skillingAuditId = skillingAuditId;
	}

	public long getChecksheetPointId() {
		return checksheetPointId;
	}

	public void setChecksheetPointId(long checksheetPointId) {
		this.checksheetPointId = checksheetPointId;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getCompletedDate() {
		return completedDate;
	}

	public void setCompletedDate(String completedDate) {
		this.completedDate = completedDate;
	}

	public String getAssignedDate() {
		return assignedDate;
	}

	public void setAssignedDate(String assignedDate) {
		this.assignedDate = assignedDate;
	}

	public int getDayNo() {
		return dayNo;
	}

	public void setDayNo(int dayNo) {
		this.dayNo = dayNo;
	}

	public String getKeyPoint() {
		return keyPoint;
	}

	public void setKeyPoint(String keyPoint) {
		this.keyPoint = keyPoint;
	}

	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

	public long getOjtPointId() {
		return ojtPointId;
	}

	public void setOjtPointId(long ojtPointId) {
		this.ojtPointId = ojtPointId;
	}

	public long getPointAuditId() {
		return pointAuditId;
	}

	public void setPointAuditId(long pointAuditId) {
		this.pointAuditId = pointAuditId;
	}

	public String getReference() {
		return reference;
	}

	public void setReference(String reference) {
		this.reference = reference;
	}

	public String getTrainerStatus() {
		return trainerStatus;
	}

	public void setTrainerStatus(String trainerStatus) {
		this.trainerStatus = trainerStatus;
	}

	public String getOeStatus() {
		return oeStatus;
	}

	public void setOeStatus(String oeStatus) {
		this.oeStatus = oeStatus;
	}

	public String getOeComment() {
		return oeComment;
	}

	public void setOeComment(String oeComment) {
		this.oeComment = oeComment;
	}

	public String getTrainerActComment() {
		return TrainerActComment;
	}

	public void setTrainerActComment(String trainerActComment) {
		TrainerActComment = trainerActComment;
	}

	public String getTrainerProdComment() {
		return TrainerProdComment;
	}

	public void setTrainerProdComment(String trainerProdComment) {
		TrainerProdComment = trainerProdComment;
	}
}
