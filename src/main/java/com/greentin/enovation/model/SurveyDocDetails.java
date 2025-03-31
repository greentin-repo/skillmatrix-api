package com.greentin.enovation.model;

import java.sql.Timestamp;
import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

/*@Entity
@Table*/
public class SurveyDocDetails {

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private int sevDocId;
	private String url;
	@Column(name="description")
	private String desc;
	private Timestamp createdDate;
	private Timestamp updatedDate;

	@ManyToOne()
	@JoinColumn(name = "teamTypeId")
	private TeamtypeMaster teamType;

	@ManyToOne()
	@JoinColumn(name = "updatedBy")
	private EmployeeDetails updatedBy;

	public int getSevDocId() {
		return sevDocId;
	}

	public void setSevDocId(int sevDocId) {
		this.sevDocId = sevDocId;
	}

	public String getUrl() {
		return url;
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

	public String getDesc() {
		return desc;
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}

	public TeamtypeMaster getTeamType() {
		return teamType;
	}

	public void setTeamType(TeamtypeMaster teamType) {
		this.teamType = teamType;
	}

	public EmployeeDetails getUpdatedBy() {
		return updatedBy;
	}

	public void setUpdatedBy(EmployeeDetails updatedBy) {
		this.updatedBy = updatedBy;
	}


}
