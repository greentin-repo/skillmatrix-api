package com.greentin.enovation.model;

import java.sql.Timestamp;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;


@Entity
public class SuggestionTemplate {

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private long id;
	
	@ManyToOne()
	@JoinColumn(name="template_id")
	private SuggestionTemplateMaster template;

	@CreationTimestamp
	private Timestamp createdDate;

	@UpdateTimestamp
	private Timestamp updatedDate;

	private int isEnable;

	@ManyToOne()
	@JoinColumn(name="branch_id")
	private BranchMaster branch;


	public SuggestionTemplate() {
		super();
	}


	public SuggestionTemplate(long id) {
		super();
		this.id = id;
	}


	public long getId() {
		return id;
	}


	public void setId(long id) {
		this.id = id;
	}


	public SuggestionTemplateMaster getTemplate() {
		return template;
	}


	public void setTemplate(SuggestionTemplateMaster template) {
		this.template = template;
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


	public int getIsEnable() {
		return isEnable;
	}


	public void setIsEnable(int isEnable) {
		this.isEnable = isEnable;
	}


	public BranchMaster getBranch() {
		return branch;
	}


	public void setBranch(BranchMaster branch) {
		this.branch = branch;
	}
}
