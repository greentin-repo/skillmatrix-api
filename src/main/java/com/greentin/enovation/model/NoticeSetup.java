package com.greentin.enovation.model;

import java.sql.Timestamp;
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
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.annotations.CreationTimestamp;

import com.fasterxml.jackson.annotation.JsonManagedReference;

@Entity
@Table(name="tbl_notice_setup")
public class NoticeSetup {

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private long noticeId;
	
	private String title;
	
	@Column(length = 500)
	private String noticeMessage;
	
	private Timestamp noticePeriod;
	
	@CreationTimestamp
	private Timestamp createdDate;
	
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="branch_id")
	private BranchMaster branch;
	
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="org_id")
	private OrganizationMaster org;
	
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="created_by")
	private EmployeeDetails empDetails;
	
	@OneToMany(mappedBy="noticeDetails",cascade=CascadeType.ALL)
	@JsonManagedReference
	private Set<NoticeIntendedFor> noticeIntendedFor;
	

	@Transient
	private String firstName;
	
	@Transient
	private String lastName;
	
	@Transient
	private String notePeriod;
	
	@Transient
	private String creationDate;
	
	@Transient
	private String intendedDepts;
	
	@Transient
	private int deptId;
	
	@Transient
	private String deptName;
	
	public NoticeSetup() {
	}
	
	
	

	public NoticeSetup(long noticeId) {
		super();
		this.noticeId = noticeId;
	}




	public long getNoticeId() {
		return noticeId;
	}

	public void setNoticeId(long noticeId) {
		this.noticeId = noticeId;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getNoticeMessage() {
		return noticeMessage;
	}

	public void setNoticeMessage(String noticeMessage) {
		this.noticeMessage = noticeMessage;
	}

	public Timestamp getNoticePeriod() {
		return noticePeriod;
	}

	public void setNoticePeriod(Timestamp noticePeriod) {
		this.noticePeriod = noticePeriod;
	}

	public Timestamp getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(Timestamp createdDate) {
		this.createdDate = createdDate;
	}

	public BranchMaster getBranch() {
		return branch;
	}

	public void setBranch(BranchMaster branch) {
		this.branch = branch;
	}

	public OrganizationMaster getOrg() {
		return org;
	}

	public void setOrg(OrganizationMaster org) {
		this.org = org;
	}

	public EmployeeDetails getEmpDetails() {
		return empDetails;
	}

	public void setEmpDetails(EmployeeDetails empDetails) {
		this.empDetails = empDetails;
	}

	public Set<NoticeIntendedFor> getNoticeIntendedFor() {
		return noticeIntendedFor;
	}

	public void setNoticeIntendedFor(Set<NoticeIntendedFor> noticeIntendedFor) {
		this.noticeIntendedFor = noticeIntendedFor;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getNotePeriod() {
		return notePeriod;
	}

	public void setNotePeriod(String notePeriod) {
		this.notePeriod = notePeriod;
	}

	public String getCreationDate() {
		return creationDate;
	}

	public void setCreationDate(String creationDate) {
		this.creationDate = creationDate;
	}

	public String getIntendedDepts() {
		return intendedDepts;
	}

	public void setIntendedDepts(String intendedDepts) {
		this.intendedDepts = intendedDepts;
	}

	public int getDeptId() {
		return deptId;
	}

	public void setDeptId(int deptId) {
		this.deptId = deptId;
	}

	public String getDeptName() {
		return deptName;
	}

	public void setDeptName(String deptName) {
		this.deptName = deptName;
	}

	

	
}
