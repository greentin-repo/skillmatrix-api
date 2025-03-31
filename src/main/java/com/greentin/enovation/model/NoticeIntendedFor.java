package com.greentin.enovation.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonBackReference;

@Entity
@Table
public class NoticeIntendedFor {

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private int id;
	
	@ManyToOne
	@JoinColumn(name="noticeId")
	@JsonBackReference
	private NoticeSetup noticeDetails;
	
	@ManyToOne
	@JoinColumn(name="intendedFor")
	private DepartmentMaster intendedFor;

	
	public NoticeIntendedFor() {
		super();
	}

	public NoticeIntendedFor(int id, NoticeSetup noticeDetails, DepartmentMaster intendedFor) {
		super();
		this.id = id;
		this.noticeDetails = noticeDetails;
		this.intendedFor = intendedFor;
	}

	public long getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}
	public NoticeSetup getNoticeDetails() {
		return noticeDetails;
	}

	public void setNoticeDetails(NoticeSetup noticeDetails) {
		this.noticeDetails = noticeDetails;
	}

	public DepartmentMaster getIntendedFor() {
		return intendedFor;
	}

	public void setIntendedFor(DepartmentMaster intendedFor) {
		this.intendedFor = intendedFor;
	}
	
}
