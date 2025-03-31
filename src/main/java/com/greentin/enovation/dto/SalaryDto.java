/**
 * @author Vinay B. Jan 26, 2021 12:34:15 AM
 */
package com.greentin.enovation.dto;

import java.sql.Timestamp;

/**
 * @author Vinay B. Jan 26, 2021 12:34:16 AM
 */
public class SalaryDto {

	private long revisionId;

	private long oldRevisionId;

	private long ctcId;

	private long branchId;

	private long empId;

	private long orgId;

	private String componentName;

	private String componentType;

	private long updatedBy;

	private long createdBy;

	private String isActive;

	private double monthly;

	private double yearly;

	private double monthlyCtc;

	private double annualCtc;

	private long salaryCompId;

	private String caption;

	private String frequency;
	
	private long oldAppraisalId;

	public long getRevisionId() {
		return revisionId;
	}

	public void setRevisionId(long revisionId) {
		this.revisionId = revisionId;
	}

	public long getOldRevisionId() {
		return oldRevisionId;
	}

	public void setOldRevisionId(long oldRevisionId) {
		this.oldRevisionId = oldRevisionId;
	}

	public long getCtcId() {
		return ctcId;
	}

	public void setCtcId(long ctcId) {
		this.ctcId = ctcId;
	}

	public long getBranchId() {
		return branchId;
	}

	public void setBranchId(long branchId) {
		this.branchId = branchId;
	}

	public long getEmpId() {
		return empId;
	}

	public void setEmpId(long empId) {
		this.empId = empId;
	}

	public long getOrgId() {
		return orgId;
	}

	public void setOrgId(long orgId) {
		this.orgId = orgId;
	}

	public String getComponentName() {
		return componentName;
	}

	public void setComponentName(String componentName) {
		this.componentName = componentName;
	}

	public String getComponentType() {
		return componentType;
	}

	public void setComponentType(String componentType) {
		this.componentType = componentType;
	}

	public long getUpdatedBy() {
		return updatedBy;
	}

	public void setUpdatedBy(long updatedBy) {
		this.updatedBy = updatedBy;
	}

	public long getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(long createdBy) {
		this.createdBy = createdBy;
	}

	public String getIsActive() {
		return isActive;
	}

	public void setIsActive(String isActive) {
		this.isActive = isActive;
	}

	public double getMonthly() {
		return monthly;
	}

	public void setMonthly(double monthly) {
		this.monthly = monthly;
	}

	public double getYearly() {
		return yearly;
	}

	public void setYearly(double yearly) {
		this.yearly = yearly;
	}

	public double getMonthlyCtc() {
		return monthlyCtc;
	}

	public void setMonthlyCtc(double monthlyCtc) {
		this.monthlyCtc = monthlyCtc;
	}

	public double getAnnualCtc() {
		return annualCtc;
	}

	public void setAnnualCtc(double annualCtc) {
		this.annualCtc = annualCtc;
	}

	public long getSalaryCompId() {
		return salaryCompId;
	}

	public void setSalaryCompId(long salaryCompId) {
		this.salaryCompId = salaryCompId;
	}

	public String getCaption() {
		return caption;
	}

	public void setCaption(String caption) {
		this.caption = caption;
	}

	public String getFrequency() {
		return frequency;
	}

	public void setFrequency(String frequency) {
		this.frequency = frequency;
	}

	public long getOldAppraisalId() {
		return oldAppraisalId;
	}

	public void setOldAppraisalId(long oldAppraisalId) {
		this.oldAppraisalId = oldAppraisalId;
	}

}
