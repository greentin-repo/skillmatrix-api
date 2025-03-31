package com.greentin.enovation.accesscontrol;

import java.sql.Timestamp;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.Formula;
import org.hibernate.annotations.UpdateTimestamp;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.greentin.enovation.model.BranchMaster;
import com.greentin.enovation.model.OrganizationMaster;

@Entity
@Table
public class ProductOrgConfig {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@ManyToOne
	@JoinColumn(name = "org_id")
	@JsonIgnore
	private OrganizationMaster org;

	@ManyToOne
	@JoinColumn(name = "product_id")
	private Product product;

	@ManyToOne
	@JoinColumn(name = "branch_id")
	@JsonIgnore
	private BranchMaster branch;

	@ManyToOne
	@JoinColumn(name = "subscrip_id")
	private SubscriptionPlan subscriptionPlan;

	private int isSetupCompleted;

	private int isEscalation;

	private int isDeactivated;

	private int isDocChangeOn;

	private int isActivityOn;

	private String registrationByPass;

	private int isHappinessOn;

	private int isScoreMandatory;

	private int isThemeCostMandate;

	@Column(name = "is_tier_one_self_impl", nullable = false, columnDefinition = "int default 0")
	private int isTierOneSelfImpl;

	@Column(name = "is_sustenance_audit_required", nullable = false, columnDefinition = "int default 0")
	private int isSustenanceAuditRequired;

	@Column(name = "is_sus_audit_required", nullable = false, columnDefinition = "int default 0")
	private int isSusAuditRequired;

	@Column(name = "tier_two_self_impl", nullable = false, columnDefinition = "int default 0")
	private int tierTwoSelfImpl;
	
	@CreationTimestamp
	@Column(name = "created_date")
	private Timestamp createdDate;

	@UpdateTimestamp
	@Column(name = "updated_date")
	private Timestamp updatedDate;

	@Column(name = "created_by", columnDefinition = "int default 0")
	private int createdBy;

	@Column(name = "updated_by", columnDefinition = "int default 0")
	private int updatedBy;

	public String getRegistrationByPass() {
		return registrationByPass;
	}

	public void setRegistrationByPass(String registrationByPass) {
		this.registrationByPass = registrationByPass;
	}

	@CreationTimestamp
	private Date regDate;

	@UpdateTimestamp
	private Date subscriptionDate;

	private Date deactivationDate;

	private int isBeforeUploadImageOn;

	private int isAfterUploadImageOn;

	private int isKuberSchedularOn;

	@Transient
	private int branchId;

	@Formula("datediff(deactivation_date,now())")
	private int remainingDays;

	@Transient
	public int getRemainingDays() {
		return remainingDays;
	}

	public void setRemainingDays(int remainingDays) {
		this.remainingDays = remainingDays;
	}

	public SubscriptionPlan getSubscriptionPlan() {
		return subscriptionPlan;
	}

	public void setSubscriptionPlan(SubscriptionPlan subscriptionPlan) {
		this.subscriptionPlan = subscriptionPlan;
	}

	public Date getSubscriptionDate() {
		return subscriptionDate;
	}

	public void setSubscriptionDate(Date subscriptionDate) {
		this.subscriptionDate = subscriptionDate;
	}

	public Date getDeactivationDate() {
		return deactivationDate;
	}

	public void setDeactivationDate(Date deactivationDate) {
		this.deactivationDate = deactivationDate;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public OrganizationMaster getOrg() {
		return org;
	}

	public void setOrg(OrganizationMaster org) {
		this.org = org;
	}

	public Product getProduct() {
		return product;
	}

	public void setProduct(Product product) {
		this.product = product;
	}

	public Date getRegDate() {
		return regDate;
	}

	public void setRegDate(Date regDate) {
		this.regDate = regDate;
	}

	public int getIsSetupCompleted() {
		return isSetupCompleted;
	}

	public void setIsSetupCompleted(int isSetupCompleted) {
		this.isSetupCompleted = isSetupCompleted;
	}

	public BranchMaster getBranch() {
		return branch;
	}

	public void setBranch(BranchMaster branch) {
		this.branch = branch;
	}

	public int getIsEscalation() {
		return isEscalation;
	}

	public void setIsEscalation(int isEscalation) {
		this.isEscalation = isEscalation;
	}

	public int getIsDeactivated() {
		return isDeactivated;
	}

	public void setIsDeactivated(int isDeactivated) {
		this.isDeactivated = isDeactivated;
	}

	public int getIsDocChangeOn() {
		return isDocChangeOn;
	}

	public void setIsDocChangeOn(int isDocChangeOn) {
		this.isDocChangeOn = isDocChangeOn;
	}

	public int getIsActivityOn() {
		return isActivityOn;
	}

	public void setIsActivityOn(int isActivityOn) {
		this.isActivityOn = isActivityOn;
	}

	public int getBranchId() {
		return branchId;
	}

	public void setBranchId(int branchId) {
		this.branchId = branchId;
	}

	public int getIsBeforeUploadImageOn() {
		return isBeforeUploadImageOn;
	}

	public void setIsBeforeUploadImageOn(int isBeforeUploadImageOn) {
		this.isBeforeUploadImageOn = isBeforeUploadImageOn;
	}

	public int getIsAfterUploadImageOn() {
		return isAfterUploadImageOn;
	}

	public void setIsAfterUploadImageOn(int isAfterUploadImageOn) {
		this.isAfterUploadImageOn = isAfterUploadImageOn;
	}

	public int getIsHappinessOn() {
		return isHappinessOn;
	}

	public void setIsHappinessOn(int isHappinessOn) {
		this.isHappinessOn = isHappinessOn;
	}

	public int getIsScoreMandatory() {
		return isScoreMandatory;
	}

	public void setIsScoreMandatory(int isScoreMandatory) {
		this.isScoreMandatory = isScoreMandatory;
	}

	public int getIsKuberSchedularOn() {
		return isKuberSchedularOn;
	}

	public void setIsKuberSchedularOn(int isKuberSchedularOn) {
		this.isKuberSchedularOn = isKuberSchedularOn;
	}

	public int getIsThemeCostMandate() {
		return isThemeCostMandate;
	}

	public void setIsThemeCostMandate(int isThemeCostMandate) {
		this.isThemeCostMandate = isThemeCostMandate;
	}

	public int getIsTierOneSelfImpl() {
		return isTierOneSelfImpl;
	}

	public void setIsTierOneSelfImpl(int isTierOneSelfImpl) {
		this.isTierOneSelfImpl = isTierOneSelfImpl;
	}

	public int getIsSustenanceAuditRequired() {
		return isSustenanceAuditRequired;
	}

	public void setIsSustenanceAuditRequired(int isSustenanceAuditRequired) {
		this.isSustenanceAuditRequired = isSustenanceAuditRequired;
	}

	public int getIsSusAuditRequired() {
		return isSusAuditRequired;
	}

	public void setIsSusAuditRequired(int isSusAuditRequired) {
		this.isSusAuditRequired = isSusAuditRequired;
	}

	public int getTierTwoSelfImpl() {
		return tierTwoSelfImpl;
	}

	public void setTierTwoSelfImpl(int tierTwoSelfImpl) {
		this.tierTwoSelfImpl = tierTwoSelfImpl;
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

}
