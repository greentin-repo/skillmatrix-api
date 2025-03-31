package com.greentin.enovation.utils;

import java.util.List;

import com.greentin.enovation.dto.SkillMatrixRequest;

public class EnovationException extends RuntimeException {

	private long id;

	private String reason;
	
	private List<SkillMatrixRequest> list;

	public EnovationException() {
		super();
	}

	public EnovationException(String reason) {
		super();
		this.reason = reason;
	}
	
	public EnovationException(List<SkillMatrixRequest> list) {
		super();
		this.list = list;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getReason() {
		return reason;
	}

	public void setReason(String reason) {
		this.reason = reason;
	}

	public List<SkillMatrixRequest> getList() {
		return list;
	}

	public void setList(List<SkillMatrixRequest> list) {
		this.list = list;
	}

}
