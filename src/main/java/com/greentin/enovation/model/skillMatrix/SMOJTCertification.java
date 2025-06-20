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

import com.greentin.enovation.model.EmployeeDetails;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

/**
 * @author Rajdeep 07 Aug 2023 ,02:25 PM
 * @desc
 * @comments
 */

@Entity
@Table(name = "sm_ojt_certification")
public class SMOJTCertification {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private long id;

	@ManyToOne
	@JoinColumn(name = "ojt_regis_id")
	private SMOJTRegis ojtRegis;

	@ManyToOne
	@JoinColumn(name = "emp_id")
	private EmployeeDetails employeeDetails;

	@ManyToOne
	@JoinColumn(name = "workstation_id")
	private SMWorkstations smWorkstations;

	@ManyToOne
	@JoinColumn(name = "certificate_id")
	private SMMasterCertificate certificate;

	@CreationTimestamp
	@Column(name = "created_date", columnDefinition = "datetime default now()", updatable = false)
	private Timestamp createdDate;

	@UpdateTimestamp
	@Column(name = "updated_date", columnDefinition = "datetime default now()")
	private Timestamp updatedDate;

	@Column(name = "created_by", columnDefinition = "int default 0", updatable = false)
	private int createdBy;

	@Column(name = "updated_by", columnDefinition = "int default 0")
	private int updatedBy;

	public SMOJTCertification() {
		super();
	}

	public SMOJTCertification(long id) {
		super();
		this.id = id;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public SMOJTRegis getOjtRegis() {
		return ojtRegis;
	}

	public void setOjtRegis(SMOJTRegis ojtRegis) {
		this.ojtRegis = ojtRegis;
	}

	public SMMasterCertificate getCertificate() {
		return certificate;
	}

	public void setCertificate(SMMasterCertificate certificate) {
		this.certificate = certificate;
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

	public EmployeeDetails getEmployeeDetails() {
		return employeeDetails;
	}

	public void setEmployeeDetails(EmployeeDetails employeeDetails) {
		this.employeeDetails = employeeDetails;
	}

	public SMWorkstations getSmWorkstations() {
		return smWorkstations;
	}

	public void setSmWorkstations(SMWorkstations smWorkstations) {
		this.smWorkstations = smWorkstations;
	}
}

