package com.greentin.enovation.schedulars;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;

@Entity
public class SchedularAudit {

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private long id;

	private String startTime;

	private String endTime;

	private String name;
	
	@Lob
	private String errorLog;

	public SchedularAudit(String name,String startTime, String endTime, String errorLog) {
		super();
		this.name = name;
		this.startTime = startTime;
		this.endTime = endTime;
		this.errorLog = errorLog;
	}

	public SchedularAudit() {
		super();
	}

	public SchedularAudit(long id) {
		super();
		this.id = id;
	}
	
	

	public SchedularAudit(String name, String startTime, String endTime) {
		super();
		this.startTime = startTime;
		this.endTime = endTime;
		this.name = name;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getStartTime() {
		return startTime;
	}

	public void setStartTime(String startTime) {
		this.startTime = startTime;
	}

	public String getEndTime() {
		return endTime;
	}

	public void setEndTime(String endTime) {
		this.endTime = endTime;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getErrorLog() {
		return errorLog;
	}

	public void setErrorLog(String errorLog) {
		this.errorLog = errorLog;
	}




}
