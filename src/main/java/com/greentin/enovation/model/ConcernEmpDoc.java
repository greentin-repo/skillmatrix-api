package com.greentin.enovation.model;

import java.sql.Timestamp;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.greentin.enovation.config.EnovationConfig;

@Entity
public class ConcernEmpDoc{
	
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="concern_doc_id" ,unique=true, nullable=false)
	private int concernDocId;
	
	private String url;
	
	@CreationTimestamp
	private Timestamp createdDate;
	
	@UpdateTimestamp
	private Timestamp updatedDate;
	
	@ManyToOne()
	@JoinColumn(name = "concern_id")
	@JsonBackReference
	private ConcernDetails concernDetails;
	
	

	public int getConcernDocId() {
		return concernDocId;
	}

	public void setConcernDocId(int concernDocId) {
		this.concernDocId = concernDocId;
	}
	
	public String getUrl() {
		
		if(url!=null){ 
			return EnovationConfig.buddyConfig.get("ProfilePicPathUrl")+url;	
		}else {
			return url;
		}
	}

	public void setUrl(String url) {
		this.url = url;
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

	public ConcernDetails getConcernDetails() {
		return concernDetails;
	}

	public void setConcernDetails(ConcernDetails concernDetails) {
		this.concernDetails = concernDetails;
	}
}
