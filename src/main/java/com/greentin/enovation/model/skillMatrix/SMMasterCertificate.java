package com.greentin.enovation.model.skillMatrix;

import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import com.greentin.enovation.model.BranchMaster;

/**
 * @author Rajdeep 07 Aug 2023 ,02:05 PM
 * @desc
 * @comments
 */

@Entity
@Table(name = "sm_master_certificate")
public class SMMasterCertificate {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private long id;

	@Column(name = "certificate_name", length = 250)
	private String certificateName;

	@Column(name = "certificate_caption", length = 250)
	private String certificateCaption;

	@Column(name = "status", length = 50)
	private String status;

	@Column(name = "certificate_path", length = 250)
	private String certificatePath;

	@ManyToOne
	@JoinColumn(name = "skill_level_id")
	private SMSkillLevel skillLevel;

	@ManyToOne
	@JoinColumn(name = "branch_id")
	private BranchMaster branch;
	
	@Column(name = "created_by", columnDefinition = "int default 0", updatable = false)
	private int createdBy;

	@Column(name = "updated_by", columnDefinition = "int default 0")
	private int updatedBy;

	@CreationTimestamp
	@Column(name = "created_date", columnDefinition = "datetime default now()", updatable = false)
	private Timestamp createdDate;

	@UpdateTimestamp
	@Column(name = "updated_date")
	private Timestamp updatedDate;

	public SMMasterCertificate() {
		super();
	}

	public SMMasterCertificate(long id) {
		super();
		this.id = id;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getCertificateName() {
		return certificateName;
	}

	public void setCertificateName(String certificateName) {
		this.certificateName = certificateName;
	}

	public String getCertificateCaption() {
		return certificateCaption;
	}

	public void setCertificateCaption(String certificateCaption) {
		this.certificateCaption = certificateCaption;
	}

	public int getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(int createdBy) {
		this.createdBy = createdBy;
	}

	public int getUpdatedBy() {
		return updatedBy;
	}

	public void setUpdatedBy(int updatedBy) {
		this.updatedBy = updatedBy;
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

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getCertificatePath() {
		return certificatePath;
	}

	public void setCertificatePath(String certificatePath) {
		this.certificatePath = certificatePath;
	}

	public SMSkillLevel getSkillLevel() {
		return skillLevel;
	}

	public void setSkillLevel(SMSkillLevel skillLevel) {
		this.skillLevel = skillLevel;
	}

	public BranchMaster getBranch() {
		return branch;
	}

	public void setBranch(BranchMaster branch) {
		this.branch = branch;
	}

}
