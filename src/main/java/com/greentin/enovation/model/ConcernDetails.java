package com.greentin.enovation.model;

import java.sql.Timestamp;
import java.util.Set;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.Transient;
import org.hibernate.annotations.Formula;
import org.springframework.web.multipart.MultipartFile;
import com.fasterxml.jackson.annotation.JsonManagedReference;

@Entity
public class ConcernDetails {

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="concern_id" ,unique=true, nullable=false)
	private int concernId;
    
	private String concernTitle;
	
	private String sourceType;
	
	private String concern;

    private String closedComment;

	private String problemAnalysis;
	
	@ManyToOne()
	@JoinColumn(name = "emp_id")
	private EmployeeDetails emp_details;
	
	@ManyToOne()
	@JoinColumn(name = "cat_id")
	private CategoryMaster category;

	@ManyToOne()
	@JoinColumn(name = "sts_id")
	private StatusMaster status_master;

	@OneToMany(mappedBy="concernDetails",cascade=CascadeType.ALL)
	@JsonManagedReference
	private Set<ConcernEmpDoc> concernEmpDocDetails;

	@OneToMany(mappedBy="concern_details",cascade=CascadeType.ALL)
	@JsonManagedReference
	@OrderBy("stsadtId ASC")
	private Set<ConcernStatusAudit> concernStatusAudit;

	@ManyToOne()
	@JoinColumn(name="branch_id")
	private BranchMaster branch;
	
	@ManyToOne()
	@JoinColumn(name = "orgId")
	private OrganizationMaster organization;

	@ManyToOne()
	@JoinColumn(name = "assignedToId")
	private EmployeeDetails assignedToExecutive;

    @ManyToOne()
	@JoinColumn(name = "under_action")
	private EmployeeDetails underAction;

	private int isAnonymous;
	
	private Timestamp createdDate;

	private Timestamp updatedDate;	
	
	
    // Transient column names
	@Transient
	private String status;
    @Formula("concern_number")
	private String concernNumber;
	@Transient
	private int branchId;
	@Transient
	private int catId;
	@Transient
	private int empId;
	@Transient
	private int orgId;
	@Transient
	private Integer statusId;
	@Transient
	private int assignedToId;
	@Transient
	private Integer assignedById;
	@Transient
	private int assignBy;
	@Transient
	private String summary;
	@Transient
	private MultipartFile [] concernImg;
	
	@Transient
	private String closureTime;

	@Formula("closure_time")
	public String getClosureTime() {
		return closureTime;
	}
	public void setClosureTime(String closureTime) {
		this.closureTime = closureTime;
	}
	
	public ConcernDetails() {

	}
	public ConcernDetails(int concernId) {
		this.concernId = concernId;
	}
	public ConcernDetails(int concernId, Integer statusId, Integer empId) {
		super();
		this.concernId = concernId;
		this.statusId = statusId;
		this.empId = empId;
	}
	public ConcernDetails(int concernId, Integer statusId, Integer empId,Integer assignBy) {
		super();
		this.concernId = concernId;
		this.statusId = statusId;
		this.empId=empId;
		this.assignBy = assignBy;
	}
	
	public int getConcernId() {
		return concernId;
	}

	public void setConcernId(int concernId) {
		this.concernId = concernId;
	}

	public Timestamp getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(Timestamp createdDate) {
		this.createdDate = createdDate;
	}

	
	public EmployeeDetails getAssignedToExecutive() {
		return assignedToExecutive;
	}
	public void setAssignedToExecutive(EmployeeDetails assignedToExecutive) {
		this.assignedToExecutive = assignedToExecutive;
	}

	public String getConcern() {
		return concern;
	}
	public void setConcern(String concern) {
		this.concern = concern;
	}

	public OrganizationMaster getOrganization() {
		return organization;
	}
	public void setOrganization(OrganizationMaster organization) {
		this.organization = organization;
	}


	public StatusMaster getStatus_master() {
		return status_master;
	}

	public void setStatus_master(StatusMaster status_master) {
		this.status_master = status_master;
	}

	
	
	public Set<ConcernStatusAudit> getConcernStatusAudit() {
		return concernStatusAudit;
	}

	public void setConcernStatusAudit(Set<ConcernStatusAudit> concernStatusAudit) {
		this.concernStatusAudit = concernStatusAudit;
	}
	
	
	public Set<ConcernEmpDoc> getConcernEmpDocDetails() {
		return concernEmpDocDetails;
	}

	public void setConcernEmpDocDetails(Set<ConcernEmpDoc> concernEmpDocDetails) {
		this.concernEmpDocDetails = concernEmpDocDetails;
	}
	
	public EmployeeDetails getEmp_details() {
		return emp_details;
	}

	
	public EmployeeDetails getUnderAction() {
		return underAction;
	}

	public void setUnderAction(EmployeeDetails underAction) {
		this.underAction = underAction;
	}

	public void setEmp_details(EmployeeDetails emp_details) {
		this.emp_details = emp_details;
	}
	
	
	public CategoryMaster getCategory() {
		return category;
	}

	public void setCategory(CategoryMaster category) {
		this.category = category;
	}
	
	public Timestamp getUpdatedDate() {
		return updatedDate;
	}

	public void setUpdatedDate(Timestamp updatedDate) {
		this.updatedDate = updatedDate;
	}

	public String getClosedComment() {
		return closedComment;
	}

	public void setClosedComment(String closedComment) {
		this.closedComment = closedComment;
	}

	public String getConcernTitle() {
		return concernTitle;
	}

	public void setConcernTitle(String concernTitle) {
		this.concernTitle = concernTitle;
	}
	
	public String getProblemAnalysis() {
		return problemAnalysis;
	}

	public void setProblemAnalysis(String problemAnalysis) {
		this.problemAnalysis = problemAnalysis;
	}

	public int getIsAnonymous() {
		return isAnonymous;
	}
	public void setIsAnonymous(int isAnonymous) {
		this.isAnonymous = isAnonymous;
	}

	public BranchMaster getBranch() {
		return branch;
	}
	public void setBranch(BranchMaster branch) {
		this.branch = branch;
	}
	
	public String getSourceType() {
		return sourceType;
	}
	public void setSourceType(String sourceType) {
		this.sourceType = sourceType;
	}
	
	// Transient column get and set
	

	public int getBranchId() {
		return branchId;
	}
	public void setBranchId(int branchId) {
		this.branchId = branchId;
	}

	public Integer getStatusId() {
		return statusId;
	}

	public void setStatusId(Integer statusId) {
		this.statusId = statusId;
	}

	public Integer getAssignedById() {
		return assignedById;
	}
	public void setAssignedById(Integer assignedById) {
		this.assignedById = assignedById;
	}

	public int getCatId() {
		return catId;
	}
	public void setCatId(int catId) {
		this.catId = catId;
	}

	public int getEmpId() {
		return empId;
	}
	public void setEmpId(int empId) {
		this.empId = empId;
	}

	public MultipartFile[] getConcernImg() {
		return concernImg;
	}
	public void setConcernImg(MultipartFile[] concernImg) {
		this.concernImg = concernImg;
	}

	public int getOrgId() {
		return orgId;
	}
	public void setOrgId(int orgId) {
		this.orgId = orgId;
	}

	public int getAssignedToId() {
		return assignedToId;
	}
	public void setAssignedToId(int assignedToId) {
		this.assignedToId = assignedToId;
	}

	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getSummary() {
		return summary;
	}
	public void setSummary(String summary) {
		this.summary = summary;
	}
	public Integer getAssignBy() {
		return assignBy;
	}
	public void setAssignBy(Integer assignBy) {
		this.assignBy = assignBy;
	}
	@Transient
	public String getConcernNumber() {
		return concernNumber;
	}
	public void setConcernNumber(String ConcernNumber) {
		this.concernNumber = ConcernNumber;
	}
}

