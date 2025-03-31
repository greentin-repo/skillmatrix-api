package com.greentin.enovation.model;

import java.sql.Timestamp;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Transient;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

@Entity
public class PasswordPolicy {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	
	private int minPasswdLength;
	
	private int minAlphabetic;
	
	private int minNumeric;
	
	private int minSpecialChar;
	
	private int maxLifeTimeInDays;
	
	private int checkLastPasswd;
	
	private String policyType; 
	
	private int orgId;
	
	private int createdBy;
	
	private int updatedBy;
	
	@CreationTimestamp
	private Timestamp createdDate;

	@UpdateTimestamp
	private Timestamp updatedDate;
	
	private String isEnable;
	
	@Transient
	private String orgName;
	
	
	/*
	 * p.id, p.check_last_passwd, p.max_life_time_in_days, p.min_alphabetic,
	 * p.min_numeric, p.min_passwd_length, p.min_special_char, p.org_id,
	 * p.policy_type, o.name
	 */
	
	
	
	public PasswordPolicy(String isEnable,int orgId) {
		super();
		this.isEnable = isEnable;
		this.orgId = orgId;
	}

	public int getId() {
		return id;
	}

	public PasswordPolicy(int id, int checkLastPasswd, int maxLifeTimeInDays, int minAlphabetic,int minNumeric,int minPasswdLength,int minSpecialChar,
			int orgId,String policyType,String orgName,String isEnable) {
		super();
		this.id = id;
		this.checkLastPasswd = checkLastPasswd;
		this.maxLifeTimeInDays = maxLifeTimeInDays;
		this.minAlphabetic = minAlphabetic;
		this.minNumeric = minNumeric;
		this.minPasswdLength = minPasswdLength;
		this.minSpecialChar = minSpecialChar;
		this.orgId = orgId;
		this.policyType = policyType;
		this.orgName = orgName;
		this.isEnable = isEnable;

	}

	public PasswordPolicy() {
		super();
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getMinPasswdLength() {
		return minPasswdLength;
	}

	public void setMinPasswdLength(int minPasswdLength) {
		this.minPasswdLength = minPasswdLength;
	}

	public int getMinAlphabetic() {
		return minAlphabetic;
	}

	public void setMinAlphabetic(int minAlphabetic) {
		this.minAlphabetic = minAlphabetic;
	}

	public int getMinNumeric() {
		return minNumeric;
	}

	public void setMinNumeric(int minNumeric) {
		this.minNumeric = minNumeric;
	}

	public int getMinSpecialChar() {
		return minSpecialChar;
	}

	public void setMinSpecialChar(int minSpecialChar) {
		this.minSpecialChar = minSpecialChar;
	}

	public int getMaxLifeTimeInDays() {
		return maxLifeTimeInDays;
	}

	public void setMaxLifeTimeInDays(int maxLifeTimeInDays) {
		this.maxLifeTimeInDays = maxLifeTimeInDays;
	}

	public int getCheckLastPasswd() {
		return checkLastPasswd;
	}

	public void setCheckLastPasswd(int checkLastPasswd) {
		this.checkLastPasswd = checkLastPasswd;
	}

	public String getPolicyType() {
		return policyType;
	}

	public void setPolicyType(String policyType) {
		this.policyType = policyType;
	}

	public int getOrgId() {
		return orgId;
	}

	public void setOrgId(int orgId) {
		this.orgId = orgId;
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

	public String getIsEnable() {
		return isEnable;
	}

	public void setIsEnable(String isEnable) {
		this.isEnable = isEnable;
	}

	public String getOrgName() {
		return orgName;
	}

	public void setOrgName(String orgName) {
		this.orgName = orgName;
	}



}
