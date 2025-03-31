package com.greentin.enovation.model;

import java.sql.Timestamp;
import java.util.List;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonManagedReference;

@Entity
@Table
public class SurveyDetails {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int surveyId;

	private String title;

	@Column(name = "description")
	private String desc;
	/*
	private int status;
*/
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
	private Timestamp startDate;

	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
	private Timestamp endDate;

	private Timestamp createdDate;

	private Timestamp updatedDate;

	private int surveyNumber;
	
	private int isTemplate;

	@ManyToOne()
	@JoinColumn(name = "createdBy")
	private EmployeeDetails createdBy;
	
	@ManyToOne()
	@JoinColumn(name = "statusId")
	private SurveyStatusMaster status;


	@ManyToOne()
	@JoinColumn(name = "updatedBy")
	private EmployeeDetails updatedBy;

	@ManyToOne()
	@JoinColumn(name = "deptId")
	private DepartmentMaster dept;
	
	@ManyToOne()
	@JoinColumn(name = "orgId")
	private OrganizationMaster organizationMaster;

	@ManyToOne()
	@JoinColumn(name = "branchId")
	private BranchMaster branchMaster;
	
	@OneToMany(mappedBy = "survayDetails",cascade=CascadeType.ALL,fetch = FetchType.LAZY)
	@JsonManagedReference
	@OrderBy("quesId ASC")
	private Set<SurveyQuestion> surveyQuestions;

	@OneToMany(mappedBy="surveyDetails",cascade=CascadeType.ALL)
	@JsonManagedReference
	private List<SurveyIntendedFor> surveyIntendedfor;	
	
	public List<SurveyIntendedFor> getSurveyIntendedfor() {
		return surveyIntendedfor;
	}

	public void setSurveyIntendedfor(List<SurveyIntendedFor> surveyIntendedfor) {
		this.surveyIntendedfor = surveyIntendedfor;
	}

	public SurveyDetails() {

	}

	public SurveyDetails(int surveyId) {
		this.surveyId = surveyId;
	}

	public int getSurveyId() {
		return surveyId;
	}

	public void setSurveyId(int surveyId) {
		this.surveyId = surveyId;
	}

	public String getTitle() {
		return title;
	}

	/*public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}*/

	public void setTitle(String title) {
		this.title = title;
	}

	public String getDesc() {
		return desc;
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}

	public Timestamp getStartDate() {
		return startDate;
	}

	public void setStartDate(Timestamp publishDate) {
		this.startDate = publishDate;
	}

	public Timestamp getEndDate() {
		return endDate;
	}

	public void setEndDate(Timestamp expiryDate) {
		this.endDate = expiryDate;
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

	public EmployeeDetails getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(EmployeeDetails createdBy) {
		this.createdBy = createdBy;
	}

	public EmployeeDetails getUpdatedBy() {
		return updatedBy;
	}

	public void setUpdatedBy(EmployeeDetails updatedBy) {
		this.updatedBy = updatedBy;
	}

	public DepartmentMaster getDept() {
		return dept;
	}

	public void setDept(DepartmentMaster dept) {
		this.dept = dept;
	}


	public OrganizationMaster getOrganizationMaster() {
		return organizationMaster;
	}

	public void setOrganizationMaster(OrganizationMaster organizationMaster) {
		this.organizationMaster = organizationMaster;
	}


	public Set<SurveyQuestion> getSurveyQuestions() {
		return surveyQuestions;
	}

	public void setSurveyQuestions(Set<SurveyQuestion> surveyQuestions) {
		this.surveyQuestions = surveyQuestions;
	}

	public BranchMaster getBranchMaster() {
		return branchMaster;
	}

	public void setBranchMaster(BranchMaster branchMaster) {
		this.branchMaster = branchMaster;
	}

	public SurveyStatusMaster getStatus() {
		return status;
	}

	public void setStatus(SurveyStatusMaster status) {
		this.status = status;
	}

	public int getSurveyNumber() {
		return surveyNumber;
	}

	public void setSurveyNumber(int surveyNumber) {
		this.surveyNumber = surveyNumber;
	}

	public int getIsTemplate() {
		return isTemplate;
	}

	public void setIsTemplate(int isTemplate) {
		this.isTemplate = isTemplate;
	}
	
}
