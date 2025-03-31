package com.greentin.enovation.model;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.Table;
import javax.persistence.Transient;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.web.multipart.MultipartFile;
import com.fasterxml.jackson.annotation.JsonManagedReference;

@Entity
@Table(name="incident_details")
public class IncidentDetails {

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="ince_id",unique=true, nullable=false)
	private int inceId;

	private String inceTitle;

	private String inceDetails;

	private String area;

	@Lob
	private String correctiveActionDet;

	@Lob
	private String rootCause;

	private int incidentNumber;

	private String rejectionReason;
	
	//@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-mm-dd")
	@Transient
	private String inputInceDate;
	
	private Timestamp inceDate;

	@CreationTimestamp
	private Timestamp reportingDate;

	@UpdateTimestamp
	private Timestamp udpatedDate;

	@ManyToOne()
	@JoinColumn(name="machId")
	private MachineDetails machineDet;

	@ManyToOne()
	@JoinColumn(name="nature_id")
	private IncidentNature inceNature;

	@ManyToOne()
	@JoinColumn(name="severity_id")
	private IncidentSeverity inceSeverity;

	@ManyToOne()
	@JoinColumn(name="priority_id")
	private IncidentPriority incePriority;

	@ManyToOne()
	@JoinColumn(name="dept_id")
	private DepartmentMaster dept;

	@ManyToOne()
	@JoinColumn(name="branch_id")
	private BranchMaster branch;

	@ManyToOne()
	@JoinColumn(name="org_id")
	private OrganizationMaster org;

	@ManyToOne(fetch=FetchType.EAGER)
	@JoinColumn(name="emp_id")
	private EmployeeDetails empDet;

	@ManyToOne(fetch=FetchType.EAGER)
	@JoinColumn(name="sts_id")
	private StatusMaster statusMaster;

	@ManyToOne(fetch=FetchType.EAGER)
	@JoinColumn(name="assigned_to")
	private EmployeeDetails assignedTo;

	@OneToMany(mappedBy="inceDet", cascade=CascadeType.ALL,orphanRemoval=true)
	@JsonManagedReference
	@OrderBy("stsadtId ASC")
	private List<IncidentAuditStatus> incidentStatusAudit = new ArrayList<>();
	
	@OneToMany(mappedBy="incDet", cascade=CascadeType.ALL,orphanRemoval=true)
	@JsonManagedReference
	private List<IncidentDocDetails> incidentDocDetails;

	@OneToMany(mappedBy="incDet", cascade=CascadeType.ALL,orphanRemoval=true)
	@JsonManagedReference
	private List<IncidentEmpDoc> incidentEmpDoc;

	@ManyToOne
	@JoinColumn(name = "report_id")
	private IncidentReport inceReport;
	
	@Transient
	private MultipartFile[] incImg;
	
	@Transient
	private MultipartFile[] afterImg;

	@Transient
	private int statusId;
	
	@Transient
	private int managerTypeId;
	
	private Timestamp targetDate;
	
	
	@Column(name="proposed_action_plan",length = 2000)
	private String proposedActionPlan;

	
	/*
	 * @OneToMany(mappedBy="incDet",cascade=CascadeType.ALL)
	 * 
	 * @JsonManagedReference private Set<IncidentEmpDoc> inceEmpDocDetails;
	 * 
	 * @OneToMany(mappedBy="incDet",cascade=CascadeType.ALL)
	 * 
	 * @JsonManagedReference private Set<IncidentDocDetails> incDocDetails;
	 */
	
	private String sourceType;
	
	@Transient
	private String summary;
	
	public IncidentDetails() {
		super();
	}

	public IncidentDetails(int inceId) {
		super();
		this.inceId = inceId;
	}


	public int getInceId() {
		return inceId;
	}

	public void setInceId(int inceId) {
		this.inceId = inceId;
	}

	public String getInceTitle() {
		return inceTitle;
	}

	public void setInceTitle(String inceTitle) {
		this.inceTitle = inceTitle;
	}

	public String getInceDetails() {
		return inceDetails;
	}

	public void setInceDetails(String inceDetails) {
		this.inceDetails = inceDetails;
	}

	public String getArea() {
		return area;
	}

	public void setArea(String area) {
		this.area = area;
	}

	public String getCorrectiveActionDet() {
		return correctiveActionDet;
	}

	public void setCorrectiveActionDet(String correctiveActionDet) {
		this.correctiveActionDet = correctiveActionDet;
	}

	public String getRootCause() {
		return rootCause;
	}

	public void setRootCause(String rootCause) {
		this.rootCause = rootCause;
	}

	public Timestamp getInceDate() {
		return inceDate;
	}

	public void setInceDate(Timestamp inceDate) {
		this.inceDate = inceDate;
	}

	public Timestamp getReportingDate() {
		return reportingDate;
	}

	public void setReportingDate(Timestamp reportingDate) {
		this.reportingDate = reportingDate;
	}

	public Timestamp getUdpatedDate() {
		return udpatedDate;
	}

	public void setUdpatedDate(Timestamp udpatedDate) {
		this.udpatedDate = udpatedDate;
	}

	public MachineDetails getMachineDet() {
		return machineDet;
	}

	public void setMachineDet(MachineDetails machineDet) {
		this.machineDet = machineDet;
	}

	public IncidentNature getInceNature() {
		return inceNature;
	}

	public void setInceNature(IncidentNature inceNature) {
		this.inceNature = inceNature;
	}

	public IncidentSeverity getInceSeverity() {
		return inceSeverity;
	}

	public void setInceSeverity(IncidentSeverity inceSeverity) {
		this.inceSeverity = inceSeverity;
	}

	public IncidentPriority getIncePriority() {
		return incePriority;
	}

	public void setIncePriority(IncidentPriority incePriority) {
		this.incePriority = incePriority;
	}

	public DepartmentMaster getDept() {
		return dept;
	}

	public void setDept(DepartmentMaster dept) {
		this.dept = dept;
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

	public EmployeeDetails getEmpDet() {
		return empDet;
	}

	public void setEmpDet(EmployeeDetails empDet) {
		this.empDet = empDet;
	}

	public StatusMaster getStatusMaster() {
		return statusMaster;
	}

	public void setStatusMaster(StatusMaster statusMaster) {
		this.statusMaster = statusMaster;
	}

	public EmployeeDetails getAssignedTo() {
		return assignedTo;
	}

	public void setAssignedTo(EmployeeDetails assignedTo) {
		this.assignedTo = assignedTo;
	}

	public int getIncidentNumber() {
		return incidentNumber;
	}

	public void setIncidentNumber(int incidentNumber) {
		this.incidentNumber = incidentNumber;
	}

	public int getStatusId() {
		return statusId;
	}

	public void setStatusId(int statusId) {
		this.statusId = statusId;
	}

	public List<IncidentAuditStatus> getIncidentStatusAudit() {
		return incidentStatusAudit;
	}

	public void setIncidentStatusAudit(List<IncidentAuditStatus> incidentStatusAudit) {
		this.incidentStatusAudit = incidentStatusAudit;
	}

	public IncidentReport getInceReport() {
		return inceReport;
	}

	public void setInceReport(IncidentReport inceReport) {
		this.inceReport = inceReport;
	}

	public String getRejectionReason() {
		return rejectionReason;
	}

	public void setRejectionReason(String rejectionReason) {
		this.rejectionReason = rejectionReason;
	}

	public int getManagerTypeId() {
		return managerTypeId;
	}

	public void setManagerTypeId(int managerTypeId) {
		this.managerTypeId = managerTypeId;
	}

	public MultipartFile[] getIncImg() {
		return incImg;
	}

	public void setIncImg(MultipartFile[] incImg) {
		this.incImg = incImg;
	}

	public String getSourceType() {
		return sourceType;
	}

	public void setSourceType(String sourceType) {
		this.sourceType = sourceType;
	}

	public String getInputInceDate() {
		return inputInceDate;
	}

	public void setInputInceDate(String inputInceDate) {
		this.inputInceDate = inputInceDate;
	}

	public List<IncidentDocDetails> getIncidentDocDetails() {
		return incidentDocDetails;
	}

	public void setIncidentDocDetails(List<IncidentDocDetails> incidentDocDetails) {
		this.incidentDocDetails = incidentDocDetails;
	}

	public List<IncidentEmpDoc> getIncidentEmpDoc() {
		return incidentEmpDoc;
	}

	public void setIncidentEmpDoc(List<IncidentEmpDoc> incidentEmpDoc) {
		this.incidentEmpDoc = incidentEmpDoc;
	}

	public Timestamp getTargetDate() {
		return targetDate;
	}

	public void setTargetDate(Timestamp targetDate) {
		this.targetDate = targetDate;
	}

	public MultipartFile[] getAfterImg() {
		return afterImg;
	}

	public void setAfterImg(MultipartFile[] afterImg) {
		this.afterImg = afterImg;
	}

	public String getSummary() {
		return summary;
	}

	public void setSummary(String summary) {
		this.summary = summary;
	}

	public String getProposedActionPlan() {
		return proposedActionPlan;
	}

	public void setProposedActionPlan(String proposedActionPlan) {
		this.proposedActionPlan = proposedActionPlan;
	}
	
	
	
	/*
	 * public Set<IncidentEmpDoc> getInceEmpDocDetails() { return inceEmpDocDetails;
	 * }
	 * 
	 * public void setInceEmpDocDetails(Set<IncidentEmpDoc> inceEmpDocDetails) {
	 * this.inceEmpDocDetails = inceEmpDocDetails; }
	 * 
	 * public Set<IncidentDocDetails> getIncDocDetails() { return incDocDetails; }
	 * 
	 * public void setIncDocDetails(Set<IncidentDocDetails> incDocDetails) {
	 * this.incDocDetails = incDocDetails; }
	 */
	
	

}
