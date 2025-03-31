package com.greentin.enovation.dto;

import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

import com.greentin.enovation.config.EnovationConfig;
import com.greentin.enovation.model.EmployeeDetails;
import com.greentin.enovation.model.SuggestionEvalJuryInput;

/*@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)*/
public class SugessionListDTO {

	private int sugId;

	private String fromDt;

	private String toDt;

	private int branchId;

	private String profilePic;

	private int concernId;

	private String concernTitle;

	private String concern;

	private String concernNumber;

	private String suggestionNumber;

	private String suggestionTitle;

	private String suggestion;

	private String catName;

	private String deptName;

	private String status;

	private String empName;

	private String label;

	private int typeId;

	private int points;

	private int statusId;

	// private int empPoints;

	private String createdDate;

	private String updatedDate;

	private int isAnonymous;

	private int isExecutive;

	private int isConcern;

	private String branchName;

	private int noOfDaysBetween;

	private String assignedBy;

	private String empDeptName;

	private Integer winnerListCount;

	private String cmpyEmpId;

	private String assignToEmpName;

	private String asignToEmpDept;

	private int meanTime;

	private int isSelfImplemented;

	private int empId;

	private int totalCount;

	private String monthName;

	private int month;

	private int year;

	private String abnormality;

	private String dwmNumber;

	private int dwmId;

	private Timestamp reportingDate;

	private Timestamp issueDate;

	private int defectQty;

	private int repeatDefect;

	private long lineId;

	private String lineName;

	private Timestamp creDate;

	private int tagTypeId;
	private Timestamp dateOfDetection;
	private String detector;
	private String eqptName;
	private String linkedWithWhichLoss;
	private int isIdeal;
	private long id;
	private String tagNumber;
	private int isRepeat;
	private String machName;
	private String machNumber;
	private String partName;
	private String partNumber;
	private int downTime;
	private String topicName;

	private String firstName;

	private String lastName;

	private double jTotalRating;

	private int jAssinedToId;

	private String jStatus;

	private String jUpdatedDate;

	private String juryName;

	private int juryEmpId;

	private int phRatingId;

	private double phRating;

	private String inputType;

	private String totalRatingCount;

	private String assignedDate;

	private List<SuggestionEvalJuryInput> SuggestionEvalJuryInput;

	private int sugNominationId;

	private String controllerName;

	private String problemAnalysis;

	
    private double browniePoints;
	
	private double totalJuryScore;
	
	private double totalPoints;
	


	private String iskaizenExist;

	private String kaizenVerificationDate;

	private String susAuditorName;

	private String isSuggOnBehalf;

	private EmployeeDetails createdBy;
	
	private String groupName;
	
	private double claimByInit;
	private double claimByEval;
	private double claimByImpl;
	private double claimByAuditor;
	private Date lastConfirmationDate;
	
	private String particulars;
	
	private String unitName;
	
	private String implementerName;
	
	private String sapCode;

	public SugessionListDTO() {
		super();
	}

	public SugessionListDTO(int sugId, String suggestion, String deptName, String catName, String status,
			String empName, String label, int typeId, int points, String suggestionTitle, int statusId,
			String createdDate, String updatedDate, int isAnonymous, int isExecutive, String suggestionNumber,
			String profilePic, String branchName, String assignedBy, String empDeptName, String cmpyEmpId,
			double jTotalRating, int jAssinedToId, String jStatus, String jUpdatedDate, String juryName, int juryEmpId,
			int phRatingId, double phRating, String inputType, List<SuggestionEvalJuryInput> SuggestionEvalJuryInput,
			int sugNominationId) {
		super();
		this.sugId = sugId;
		this.points = points;
		this.profilePic = profilePic;
		this.suggestionNumber = suggestionNumber;
		this.suggestionTitle = suggestionTitle;
		this.suggestion = suggestion;
		this.catName = catName;
		this.deptName = deptName;
		this.status = status;
		this.empName = empName;
		this.label = label;
		this.typeId = typeId;
		this.statusId = statusId;
		this.createdDate = createdDate;
		this.updatedDate = updatedDate;
		this.isAnonymous = isAnonymous;
		this.isExecutive = isExecutive;
		this.branchName = branchName;
		this.assignedBy = assignedBy;
		this.empDeptName = empDeptName;
		this.cmpyEmpId = cmpyEmpId;
		this.jTotalRating = jTotalRating;
		this.jAssinedToId = jAssinedToId;
		this.jStatus = jStatus;
		this.jUpdatedDate = jUpdatedDate;
		this.juryName = juryName;
		this.SuggestionEvalJuryInput = SuggestionEvalJuryInput;
		this.juryEmpId = juryEmpId;
		this.phRatingId = phRatingId;
		this.phRating = phRating;
		this.inputType = inputType;
		this.sugNominationId = sugNominationId;

	}

	public SugessionListDTO(int sugId, String suggestion, String deptName, String catName, String status,
			String empName, String label, int typeId, int points, String suggestionTitle, int statusId,
			String createdDate, String updatedDate, int isAnonymous, int isExecutive, String suggestionNumber,
			String profilePic, String branchName, String assignedBy, String empDeptName, String cmpyEmpId,
			String assignToEmpName, String asignToEmpDept, int meanTime, int isSelfImplemented, String assignedDate,
			String lineName, String controllerName) {
		super();
		this.sugId = sugId;
		this.points = points;
		this.profilePic = profilePic;
		this.suggestionNumber = suggestionNumber;
		this.suggestionTitle = suggestionTitle;
		this.suggestion = suggestion;
		this.catName = catName;
		this.deptName = deptName;
		this.status = status;
		this.empName = empName;
		this.label = label;
		this.typeId = typeId;
		this.statusId = statusId;
		this.createdDate = createdDate;
		this.updatedDate = updatedDate;
		this.isAnonymous = isAnonymous;
		this.isExecutive = isExecutive;
		this.branchName = branchName;
		this.assignedBy = assignedBy;
		this.empDeptName = empDeptName;
		this.cmpyEmpId = cmpyEmpId;
		this.assignToEmpName = assignToEmpName;
		this.asignToEmpDept = asignToEmpDept;
		this.meanTime = meanTime;
		this.isSelfImplemented = isSelfImplemented;
		this.assignedDate = assignedDate;
		this.lineName = lineName;
		this.controllerName = controllerName;

	}

	public SugessionListDTO(String tagNumber, long id, String deptName, String label, String abnormality,
			Timestamp creDate, int tagTypeId, Timestamp dateOfDetection, String detector, String eqptName,
			String linkedWithWhichLoss, int isIdeal, String empName, int isRepeat, String machNumber, String machName,
			String partNumber, String partName, String lineName) {
		super();
		this.tagNumber = tagNumber;
		this.id = id;
		this.deptName = deptName;
		this.empName = empName;
		this.label = label;
		this.abnormality = abnormality;
		this.creDate = creDate;
		this.tagTypeId = tagTypeId;
		this.dateOfDetection = dateOfDetection;
		this.detector = detector;
		this.eqptName = eqptName;
		this.linkedWithWhichLoss = linkedWithWhichLoss;
		this.isIdeal = isIdeal;
		this.isRepeat = isRepeat;
		this.machNumber = machNumber;
		this.machName = machName;
		this.partNumber = partNumber;
		this.partName = partName;
		this.lineName = lineName;
	}

	public SugessionListDTO(String dwmNumber, String empName, String profilePic, int statusId, String label,
			String abnormality, Timestamp creDate, int dwmId, Timestamp issueDate, int defectQty, int repeatDefect,
			long lineId, String lineName, String deptName, int downTime, String topicName) {
		super();
		this.profilePic = profilePic;
		this.empName = empName;
		this.label = label;
		this.statusId = statusId;
		this.abnormality = abnormality;
		this.dwmNumber = dwmNumber;
		this.creDate = creDate;
		this.dwmId = dwmId;
		this.issueDate = issueDate;
		this.defectQty = defectQty;
		this.repeatDefect = repeatDefect;
		this.lineId = lineId;
		this.lineName = lineName;
		this.deptName = deptName;
		this.downTime = downTime;
		this.topicName = topicName;
	}

	public int getSugNominationId() {
		return sugNominationId;
	}

	public void setSugNominationId(int sugNominationId) {
		this.sugNominationId = sugNominationId;
	}

	public int getSugId() {
		return sugId;
	}

	public void setSugId(int sugId) {
		this.sugId = sugId;
	}

	public String getSuggestion() {
		return suggestion;
	}

	public void setSuggestion(String suggestion) {
		this.suggestion = suggestion;
	}

	public String getCatName() {
		return catName;
	}

	public void setCatName(String catName) {
		this.catName = catName;
	}

	public String getDeptName() {
		return deptName;
	}

	public void setDeptName(String deptName) {
		this.deptName = deptName;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getEmpName() {
		return empName;
	}

	public void setEmpName(String empName) {
		this.empName = empName;
	}

	public String getLabel() {
		return label;
	}

	public void setLabel(String label) {
		this.label = label;
	}

	public int getTypeId() {
		return typeId;
	}

	public void setTypeId(int typeId) {
		this.typeId = typeId;
	}

	public int getPoints() {
		return points;
	}

	public void setPoints(int points) {
		this.points = points;
	}

	public String getSuggestionTitle() {
		return suggestionTitle;
	}

	public void setSuggestionTitle(String suggestionTitle) {
		this.suggestionTitle = suggestionTitle;
	}

	public int getStatusId() {
		return statusId;
	}

	public void setStatusId(int statusId) {
		this.statusId = statusId;
	}

	public String getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(String createdDate) {
		this.createdDate = createdDate;
	}

	/*
	 * public int getEmpPoints() { return empPoints; }
	 * 
	 * public void setEmpPoints(int empPoints) { this.empPoints = empPoints; }
	 */
	public String getUpdatedDate() {
		return updatedDate;
	}

	public void setUpdatedDate(String updatedDate) {
		this.updatedDate = updatedDate;
	}

	public int getIsAnonymous() {
		return isAnonymous;
	}

	public void setIsAnonymous(int isAnonymous) {
		this.isAnonymous = isAnonymous;
	}

	public int getIsExecutive() {
		return isExecutive;
	}

	public void setIsExecutive(int isExecutive) {
		this.isExecutive = isExecutive;
	}

	public int getIsConcern() {
		return isConcern;
	}

	public void setIsConcern(int isConcern) {
		this.isConcern = isConcern;
	}

	public String getSuggestionNumber() {
		return suggestionNumber;
	}

	public void setSuggestionNumber(String suggestionNumber) {
		this.suggestionNumber = suggestionNumber;
	}

	public String getProfilePic() {
		if (profilePic == null) {
			return profilePic;
		} else {
			return EnovationConfig.buddyConfig.get("ProfilePicPathUrl") + profilePic;
		}
	}

	public void setProfilePic(String profilePic) {
		this.profilePic = profilePic;
	}

	public String getBranchName() {
		return branchName;
	}

	public void setBranchName(String branchName) {
		this.branchName = branchName;
	}

	public int getConcernId() {
		return concernId;
	}

	public void setConcernId(int concernId) {
		this.concernId = concernId;
	}

	public String getConcernTitle() {
		return concernTitle;
	}

	public void setConcernTitle(String concernTitle) {
		this.concernTitle = concernTitle;
	}

	public String getConcern() {
		return concern;
	}

	public void setConcern(String concern) {
		this.concern = concern;
	}

	public String getConcernNumber() {
		return concernNumber;
	}

	public void setConcernNumber(String concernNumber) {
		this.concernNumber = concernNumber;
	}

	public int getNoOfDaysBetween() {
		return noOfDaysBetween;
	}

	public void setNoOfDaysBetween(int noOfDaysBetween) {
		this.noOfDaysBetween = noOfDaysBetween;
	}

	public String getAssignedBy() {
		return assignedBy;
	}

	public void setAssignedBy(String assignedBy) {
		this.assignedBy = assignedBy;
	}

	public String getEmpDeptName() {
		return empDeptName;
	}

	public void setEmpDeptName(String empDeptName) {
		this.empDeptName = empDeptName;
	}

	public Integer getWinnerListCount() {
		return winnerListCount;
	}

	public void setWinnerListCount(Integer winnerListCount) {
		this.winnerListCount = winnerListCount;
	}

	public String getCmpyEmpId() {
		return cmpyEmpId;
	}

	public void setCmpyEmpId(String cmpyEmpId) {
		this.cmpyEmpId = cmpyEmpId;
	}

	public String getAssignToEmpName() {
		return assignToEmpName;
	}

	public void setAssignToEmpName(String assignToEmpName) {
		this.assignToEmpName = assignToEmpName;
	}

	public String getAsignToEmpDept() {
		return asignToEmpDept;
	}

	public void setAsignToEmpDept(String asignToEmpDept) {
		this.asignToEmpDept = asignToEmpDept;
	}

	public int getMeanTime() {
		return meanTime;
	}

	public void setMeanTime(int meanTime) {
		this.meanTime = meanTime;
	}

	public int getIsSelfImplemented() {
		return isSelfImplemented;
	}

	public void setIsSelfImplemented(int isSelfImplemented) {
		this.isSelfImplemented = isSelfImplemented;
	}

	public int getEmpId() {
		return empId;
	}

	public void setEmpId(int empId) {
		this.empId = empId;
	}

	public int getTotalCount() {
		return totalCount;
	}

	public void setTotalCount(int totalCount) {
		this.totalCount = totalCount;
	}

	public String getMonthName() {
		return monthName;
	}

	public void setMonthName(String monthName) {
		this.monthName = monthName;
	}

	public int getMonth() {
		return month;
	}

	public void setMonth(int month) {
		this.month = month;
	}

	public int getYear() {
		return year;
	}

	public void setYear(int year) {
		this.year = year;
	}

	public String getFromDt() {
		return fromDt;
	}

	public void setFromDt(String fromDt) {
		this.fromDt = fromDt;
	}

	public String getToDt() {
		return toDt;
	}

	public void setToDt(String toDt) {
		this.toDt = toDt;
	}

	public int getBranchId() {
		return branchId;
	}

	public void setBranchId(int branchId) {
		this.branchId = branchId;
	}

	public String getAbnormality() {
		return abnormality;
	}

	public void setAbnormality(String abnormality) {
		this.abnormality = abnormality;
	}

	public String getDwmNumber() {
		return dwmNumber;
	}

	public void setDwmNumber(String dwmNumber) {
		this.dwmNumber = dwmNumber;
	}

	public int getDwmId() {
		return dwmId;
	}

	public void setDwmId(int dwmId) {
		this.dwmId = dwmId;
	}

	public Timestamp getReportingDate() {
		return reportingDate;
	}

	public void setReportingDate(Timestamp reportingDate) {
		this.reportingDate = reportingDate;
	}

	public Timestamp getIssueDate() {
		return issueDate;
	}

	public void setIssueDate(Timestamp issueDate) {
		this.issueDate = issueDate;
	}

	public int getDefectQty() {
		return defectQty;
	}

	public void setDefectQty(int defectQty) {
		this.defectQty = defectQty;
	}

	public int getRepeatDefect() {
		return repeatDefect;
	}

	public void setRepeatDefect(int repeatDefect) {
		this.repeatDefect = repeatDefect;
	}

	public long getLineId() {
		return lineId;
	}

	public void setLineId(long lineId) {
		this.lineId = lineId;
	}

	public String getLineName() {
		return lineName;
	}

	public void setLineName(String lineName) {
		this.lineName = lineName;
	}

	public Timestamp getCreDate() {
		return creDate;
	}

	public void setCreDate(Timestamp creDate) {
		this.creDate = creDate;
	}

	public int getTagTypeId() {
		return tagTypeId;
	}

	public void setTagTypeId(int tagTypeId) {
		this.tagTypeId = tagTypeId;
	}

	public Timestamp getDateOfDetection() {
		return dateOfDetection;
	}

	public void setDateOfDetection(Timestamp dateOfDetection) {
		this.dateOfDetection = dateOfDetection;
	}

	public String getDetector() {
		return detector;
	}

	public void setDetector(String detector) {
		this.detector = detector;
	}

	public String getEqptName() {
		return eqptName;
	}

	public void setEqptName(String eqptName) {
		this.eqptName = eqptName;
	}

	public String getLinkedWithWhichLoss() {
		return linkedWithWhichLoss;
	}

	public void setLinkedWithWhichLoss(String linkedWithWhichLoss) {
		this.linkedWithWhichLoss = linkedWithWhichLoss;
	}

	public int getIsIdeal() {
		return isIdeal;
	}

	public void setIsIdeal(int isIdeal) {
		this.isIdeal = isIdeal;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getTagNumber() {
		return tagNumber;
	}

	public void setTagNumber(String tagNumber) {
		this.tagNumber = tagNumber;
	}

	public int getIsRepeat() {
		return isRepeat;
	}

	public void setIsRepeat(int isRepeat) {
		this.isRepeat = isRepeat;
	}

	public String getMachName() {
		return machName;
	}

	public void setMachName(String machName) {
		this.machName = machName;
	}

	public String getMachNumber() {
		return machNumber;
	}

	public void setMachNumber(String machNumber) {
		this.machNumber = machNumber;
	}

	public String getPartName() {
		return partName;
	}

	public void setPartName(String partName) {
		this.partName = partName;
	}

	public String getPartNumber() {
		return partNumber;
	}

	public void setPartNumber(String partNumber) {
		this.partNumber = partNumber;
	}

	public int getDownTime() {
		return downTime;
	}

	public void setDownTime(int downTime) {
		this.downTime = downTime;
	}

	public String getTopicName() {
		return topicName;
	}

	public void setTopicName(String topicName) {
		this.topicName = topicName;
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

	public double getjTotalRating() {
		return jTotalRating;
	}

	public void setjTotalRating(double jTotalRating) {
		this.jTotalRating = jTotalRating;
	}

	public int getPhRatingId() {
		return phRatingId;
	}

	public void setPhRatingId(int phRatingId) {
		this.phRatingId = phRatingId;
	}

	public double getPhRating() {
		return phRating;
	}

	public void setPhRating(double phRating) {
		this.phRating = phRating;
	}

	public String getInputType() {
		return inputType;
	}

	public void setInputType(String inputType) {
		this.inputType = inputType;
	}

	public int getjAssinedToId() {
		return jAssinedToId;
	}

	public void setjAssinedToId(int jAssinedToId) {
		this.jAssinedToId = jAssinedToId;
	}

	public String getjStatus() {
		return jStatus;
	}

	public void setjStatus(String jStatus) {
		this.jStatus = jStatus;
	}

	public List<SuggestionEvalJuryInput> getSuggestionEvalJuryInput() {
		return SuggestionEvalJuryInput;
	}

	public void setSuggestionEvalJuryInput(List<SuggestionEvalJuryInput> suggestionEvalJuryInput) {
		SuggestionEvalJuryInput = suggestionEvalJuryInput;
	}

	public String getjUpdatedDate() {
		return jUpdatedDate;
	}

	public void setjUpdatedDate(String jUpdatedDate) {
		this.jUpdatedDate = jUpdatedDate;
	}

	public String getJuryName() {
		return juryName;
	}

	public void setJuryName(String juryName) {
		this.juryName = juryName;
	}

	public int getJuryEmpId() {
		return juryEmpId;
	}

	public void setJuryEmpId(int juryEmpId) {
		this.juryEmpId = juryEmpId;
	}

	public String getTotalRatingCount() {
		return totalRatingCount;
	}

	public void setTotalRatingCount(String totalRatingCount) {
		this.totalRatingCount = totalRatingCount;
	}

	public String getAssignedDate() {
		return assignedDate;
	}

	public void setAssignedDate(String assignedDate) {
		this.assignedDate = assignedDate;
	}

	public String getControllerName() {
		return controllerName;
	}

	public void setControllerName(String controllerName) {
		this.controllerName = controllerName;
	}

	public String getProblemAnalysis() {
		return problemAnalysis;
	}

	public void setProblemAnalysis(String problemAnalysis) {
		this.problemAnalysis = problemAnalysis;
	}


	public double getBrowniePoints() {
		return browniePoints;
	}

	public void setBrowniePoints(double browniePoints) {
		this.browniePoints = browniePoints;
	}

	public double getTotalJuryScore() {
		return totalJuryScore;
	}

	public void setTotalJuryScore(double totalJuryScore) {
		this.totalJuryScore = totalJuryScore;
	}

	public double getTotalPoints() {
		return totalPoints;
	}

	public void setTotalPoints(double totalPoints) {
		this.totalPoints = totalPoints;
	}	

	public String getIskaizenExist() {
		return iskaizenExist;
	}

	public void setIskaizenExist(String iskaizenExist) {
		this.iskaizenExist = iskaizenExist;
	}

	public String getKaizenVerificationDate() {
		return kaizenVerificationDate;
	}

	public void setKaizenVerificationDate(String kaizenVerificationDate) {
		this.kaizenVerificationDate = kaizenVerificationDate;
	}

	public String getSusAuditorName() {
		return susAuditorName;
	}

	public void setSusAuditorName(String susAuditorName) {
		this.susAuditorName = susAuditorName;
	}

	public String getIsSuggOnBehalf() {
		return isSuggOnBehalf;
	}

	public void setIsSuggOnBehalf(String isSuggOnBehalf) {
		this.isSuggOnBehalf = isSuggOnBehalf;
	}

	public EmployeeDetails getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(EmployeeDetails createdBy) {
		this.createdBy = createdBy;
	}

	public String getGroupName() {
		return groupName;
	}

	public void setGroupName(String groupName) {
		this.groupName = groupName;
	}

	public double getClaimByInit() {
		return claimByInit;
	}

	public void setClaimByInit(double claimByInit) {
		this.claimByInit = claimByInit;
	}

	public double getClaimByEval() {
		return claimByEval;
	}

	public void setClaimByEval(double claimByEval) {
		this.claimByEval = claimByEval;
	}

	public double getClaimByImpl() {
		return claimByImpl;
	}

	public void setClaimByImpl(double claimByImpl) {
		this.claimByImpl = claimByImpl;
	}

	public double getClaimByAuditor() {
		return claimByAuditor;
	}

	public void setClaimByAuditor(double claimByAuditor) {
		this.claimByAuditor = claimByAuditor;
	}

	public Date getLastConfirmationDate() {
		return lastConfirmationDate;
	}

	public void setLastConfirmationDate(Date lastConfirmationDate) {
		this.lastConfirmationDate = lastConfirmationDate;
	}

	public String getParticulars() {
		return particulars;
	}

	public void setParticulars(String particulars) {
		this.particulars = particulars;
	}

	public String getUnitName() {
		return unitName;
	}

	public void setUnitName(String unitName) {
		this.unitName = unitName;
	}

	public String getImplementerName() {
		return implementerName;
	}

	public void setImplementerName(String implementerName) {
		this.implementerName = implementerName;
	}

	public String getSapCode() {
		return sapCode;
	}

	public void setSapCode(String sapCode) {
		this.sapCode = sapCode;
	}	
	
}
