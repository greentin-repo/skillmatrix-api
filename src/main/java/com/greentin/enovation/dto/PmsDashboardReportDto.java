/**
 * @author Aditi V Feb 10, 2021 8:45:22 PM
 */
package com.greentin.enovation.dto;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class PmsDashboardReportDto {

	private String fromDt;

	private String toDt;

	private List<Integer> branchIds;

	private String graphType;

	private String reportCaption;

	private int empId;

	private String empNum;

	private String empName;

	private String dOB;

	private String dOJ;

	private String machineName;

	private long skillLevel;

	private String lastUpdatedDate;

	private String deptName;

	private String email;

	private long contactNo;

	private String line;

	private int branchId;

	private String trainingName;

	private String applicationOfTraining;

	private String trainingType;

	private String strengthWeakness;

	private String type;

	private String otherTrainingName;

	private String Strengths;

	private String Weaknesses;

	private int roleId;

	private String role;

	private int total;

	private int completed;

	private int supervisor;

	private int oeLead;

	private int productionHead;

	private int hrHead;

	private int oe;

	private int plantHead;

	private int appraisalId;

	private String assignedDate;

	private int pendingDays;

	private String actionStatus;

	private String status;

	private String experience;

	private int outstanding;

	private int veryGood;

	private int good;

	private int unstisfactory;

	private int oeCount;

	private int currentYear;

	private int nextYear;

	private double beforeCtc;

	private double nextCtc;

	private String monthYear;

	private String monthValue;

	private int yearValue;

	private int month;

	private double percentage;

	private int belowFifteen;

	private int asPerDoj;

	private int betweenFifteenToThirty;

	private int aboveThirty;

	private double ctcCurrentYear;

	private double ctcNextYear;

	private Integer key;
	private double sumAmount;
	private double sumPrice;

	private int totPerfSheetCount;

	private int pendingWithOe;

	private int pendingWithSup;

	private int pendingWithOeLead;

	private int perfSheetCompleted;

	private String assignedToName;

	private String assignedToEmpNum;

	private int assignedToEmpId;

	private String monthName;

	private int stsId;

	private long perfSheetId;

	private String pendingWith;

	private int pendingSince;

	private double empScore;

	private double supScore;

	private double empPercentage;

	private double supPercentage;

	private String lineName;

	private String location;

	private String wef;

	private String letterGeneratedDate;

	private String revisedDesignation;

	private String flowStatus;

	private double appraisalScore;

	private double appraiaslScoreInPer;

	private String rating;

	private String companyEmpNum;

	private double basic;

	private double hra;

	private double eduAll;

	private double conAll;

	private double medAll;

	private double attAll;

	private double stipend;

	private double lta;

	private double serviceAward;

	private double bonus;

	private double pf;

	private double esic;

	private double gratuity;

	private double tldpAll;

	private double btechAll;

	private double knowAll;

	private double stoeStaffAll;

	private double stoeShopFloorAll;

	private double grossSal;
	
	private double specialAlowance;

	private double retentionBonuns;

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	public String getWef() {
		return wef;
	}

	public void setWef(String wef) {
		this.wef = wef;
	}

	public String getLetterGeneratedDate() {
		return letterGeneratedDate;
	}

	public void setLetterGeneratedDate(String letterGeneratedDate) {
		this.letterGeneratedDate = letterGeneratedDate;
	}

	public String getRevisedDesignation() {
		return revisedDesignation;
	}

	public void setRevisedDesignation(String revisedDesignation) {
		this.revisedDesignation = revisedDesignation;
	}

	public String getRating() {
		return rating;
	}

	public void setRating(String rating) {
		this.rating = rating;
	}

	public double getAppraiaslScoreInPer() {
		return appraiaslScoreInPer;
	}

	public void setAppraiaslScoreInPer(double appraiaslScoreInPer) {
		this.appraiaslScoreInPer = appraiaslScoreInPer;
	}

	public double getAppraisalScore() {
		return appraisalScore;
	}

	public void setAppraisalScore(double appraisalScore) {
		this.appraisalScore = appraisalScore;
	}

	public String getFlowStatus() {
		return flowStatus;
	}

	public void setFlowStatus(String flowStatus) {
		this.flowStatus = flowStatus;
	}

	public PmsDashboardReportDto() {
		super();
	}

	public PmsDashboardReportDto(Integer key, double sumAmount, double sumPrice) {

		this.key = key;
		this.sumAmount = sumAmount;
		this.sumPrice = sumPrice;

	}

	public int getAppraisalId() {
		return appraisalId;
	}

	public void setAppraisalId(int appraisalId) {
		this.appraisalId = appraisalId;
	}

	public String getAssignedDate() {
		return assignedDate;
	}

	public void setAssignedDate(String assignedDate) {
		this.assignedDate = assignedDate;
	}

	public int getPendingDays() {
		return pendingDays;
	}

	public void setPendingDays(int pendingDays) {
		this.pendingDays = pendingDays;
	}

	public String getActionStatus() {
		return actionStatus;
	}

	public void setActionStatus(String actionStatus) {
		this.actionStatus = actionStatus;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public int getSupervisor() {
		return supervisor;
	}

	public void setSupervisor(int supervisor) {
		this.supervisor = supervisor;
	}

	public int getOeLead() {
		return oeLead;
	}

	public void setOeLead(int oeLead) {
		this.oeLead = oeLead;
	}

	public int getProductionHead() {
		return productionHead;
	}

	public void setProductionHead(int productionHead) {
		this.productionHead = productionHead;
	}

	public int getHrHead() {
		return hrHead;
	}

	public void setHrHead(int hrHead) {
		this.hrHead = hrHead;
	}

	public int getOe() {
		return oe;
	}

	public void setOe(int oe) {
		this.oe = oe;
	}

	public int getPlantHead() {
		return plantHead;
	}

	public void setPlantHead(int plantHead) {
		this.plantHead = plantHead;
	}

	public String getRole() {
		return role;
	}

	public void setRole(String role) {
		this.role = role;
	}

	public int getTotal() {
		return total;
	}

	public void setTotal(int total) {
		this.total = total;
	}

	public int getCompleted() {
		return completed;
	}

	public void setCompleted(int completed) {
		this.completed = completed;
	}

	public int getRoleId() {
		return roleId;
	}

	public void setRoleId(int roleId) {
		this.roleId = roleId;
	}

	public String getStrengths() {
		return Strengths;
	}

	public void setStrengths(String strengths) {
		Strengths = strengths;
	}

	public String getWeaknesses() {
		return Weaknesses;
	}

	public void setWeaknesses(String weaknesses) {
		Weaknesses = weaknesses;
	}

	public String getdOB() {
		return dOB;
	}

	public void setdOB(String dOB) {
		this.dOB = dOB;
	}

	public String getdOJ() {
		return dOJ;
	}

	public void setdOJ(String dOJ) {
		this.dOJ = dOJ;
	}

	public String getOtherTrainingName() {
		return otherTrainingName;
	}

	public void setOtherTrainingName(String otherTrainingName) {
		this.otherTrainingName = otherTrainingName;
	}

	public String getStrengthWeakness() {
		return strengthWeakness;
	}

	public void setStrengthWeakness(String strengthWeakness) {
		this.strengthWeakness = strengthWeakness;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getTrainingName() {
		return trainingName;
	}

	public void setTrainingName(String trainingName) {
		this.trainingName = trainingName;
	}

	public String getApplicationOfTraining() {
		return applicationOfTraining;
	}

	public void setApplicationOfTraining(String applicationOfTraining) {
		this.applicationOfTraining = applicationOfTraining;
	}

	public String getTrainingType() {
		return trainingType;
	}

	public void setTrainingType(String trainingType) {
		this.trainingType = trainingType;
	}

	public String getGraphType() {
		return graphType;
	}

	public List<Integer> getBranchIds() {
		return branchIds;
	}

	public void setBranchIds(List<Integer> branchIds) {
		this.branchIds = branchIds;
	}

	public int getBranchId() {
		return branchId;
	}

	public void setBranchId(int branchId) {
		this.branchId = branchId;
	}

	public void setGraphType(String graphType) {
		this.graphType = graphType;
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

	public String getEmpNum() {
		return empNum;
	}

	public void setEmpNum(String empNum) {
		this.empNum = empNum;
	}

	public String getEmpName() {
		return empName;
	}

	public void setEmpName(String empName) {
		this.empName = empName;
	}

	public String getMachineName() {
		return machineName;
	}

	public void setMachineName(String machineName) {
		this.machineName = machineName;
	}

	public long getSkillLevel() {
		return skillLevel;
	}

	public void setSkillLevel(long skillLevel) {
		this.skillLevel = skillLevel;
	}

	public String getLastUpdatedDate() {
		return lastUpdatedDate;
	}

	public void setLastUpdatedDate(String lastUpdatedDate) {
		this.lastUpdatedDate = lastUpdatedDate;
	}

	public String getDeptName() {
		return deptName;
	}

	public void setDeptName(String deptName) {
		this.deptName = deptName;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public long getContactNo() {
		return contactNo;
	}

	public void setContactNo(long contactNo) {
		this.contactNo = contactNo;
	}

	public String getLine() {
		return line;
	}

	public void setLine(String line) {
		this.line = line;
	}

	public int getEmpId() {
		return empId;
	}

	public void setEmpId(int empId) {
		this.empId = empId;
	}

	public String getReportCaption() {
		return reportCaption;
	}

	public void setReportCaption(String reportCaption) {
		this.reportCaption = reportCaption;
	}

	public String getExperience() {
		return experience;
	}

	public void setExperience(String experience) {
		this.experience = experience;
	}

	public int getOutstanding() {
		return outstanding;
	}

	public void setOutstanding(int outstanding) {
		this.outstanding = outstanding;
	}

	public int getVeryGood() {
		return veryGood;
	}

	public void setVeryGood(int veryGood) {
		this.veryGood = veryGood;
	}

	public int getGood() {
		return good;
	}

	public void setGood(int good) {
		this.good = good;
	}

	public int getUnstisfactory() {
		return unstisfactory;
	}

	public void setUnstisfactory(int unstisfactory) {
		this.unstisfactory = unstisfactory;
	}

	public int getOeCount() {
		return oeCount;
	}

	public void setOeCount(int oeCount) {
		this.oeCount = oeCount;
	}

	public int getCurrentYear() {
		return currentYear;
	}

	public void setCurrentYear(int currentYear) {
		this.currentYear = currentYear;
	}

	public int getNextYear() {
		return nextYear;
	}

	public void setNextYear(int nextYear) {
		this.nextYear = nextYear;
	}

	public double getBeforeCtc() {
		return beforeCtc;
	}

	public void setBeforeCtc(double beforeCtc) {
		this.beforeCtc = beforeCtc;
	}

	public double getNextCtc() {
		return nextCtc;
	}

	public void setNextCtc(double nextCtc) {
		this.nextCtc = nextCtc;
	}

	public String getMonthYear() {
		return monthYear;
	}

	public void setMonthYear(String monthYear) {
		this.monthYear = monthYear;
	}

	public String getMonthValue() {
		return monthValue;
	}

	public void setMonthValue(String monthValue) {
		this.monthValue = monthValue;
	}

	public int getYearValue() {
		return yearValue;
	}

	public void setYearValue(int yearValue) {
		this.yearValue = yearValue;
	}

	public double getPercentage() {
		return percentage;
	}

	public void setPercentage(double percentage) {
		this.percentage = percentage;
	}

	public int getAsPerDoj() {
		return asPerDoj;
	}

	public void setAsPerDoj(int asPerDoj) {
		this.asPerDoj = asPerDoj;
	}

	public int getBetweenFifteenToThirty() {
		return betweenFifteenToThirty;
	}

	public void setBetweenFifteenToThirty(int betweenFifteenToThirty) {
		this.betweenFifteenToThirty = betweenFifteenToThirty;
	}

	public int getAboveThirty() {
		return aboveThirty;
	}

	public void setAboveThirty(int aboveThirty) {
		this.aboveThirty = aboveThirty;
	}

	public int getBelowFifteen() {
		return belowFifteen;
	}

	public void setBelowFifteen(int belowFifteen) {
		this.belowFifteen = belowFifteen;
	}

	public double getCtcCurrentYear() {
		return ctcCurrentYear;
	}

	public void setCtcCurrentYear(double ctcCurrentYear) {
		this.ctcCurrentYear = ctcCurrentYear;
	}

	public double getCtcNextYear() {
		return ctcNextYear;
	}

	public void setCtcNextYear(double ctcNextYear) {
		this.ctcNextYear = ctcNextYear;
	}

	public Integer getKey() {
		return key;
	}

	public void setKey(Integer key) {
		this.key = key;
	}

	public double getSumAmount() {
		return sumAmount;
	}

	public void setSumAmount(double sumAmount) {
		this.sumAmount = sumAmount;
	}

	public double getSumPrice() {
		return sumPrice;
	}

	public void setSumPrice(double sumPrice) {
		this.sumPrice = sumPrice;
	}

	public int getMonth() {
		return month;
	}

	public void setMonth(int month) {
		this.month = month;
	}

	public int getTotPerfSheetCount() {
		return totPerfSheetCount;
	}

	public void setTotPerfSheetCount(int totPerfSheetCount) {
		this.totPerfSheetCount = totPerfSheetCount;
	}

	public int getPendingWithSup() {
		return pendingWithSup;
	}

	public void setPendingWithSup(int pendingWithSup) {
		this.pendingWithSup = pendingWithSup;
	}

	public int getPendingWithOeLead() {
		return pendingWithOeLead;
	}

	public void setPendingWithOeLead(int pendingWithOeLead) {
		this.pendingWithOeLead = pendingWithOeLead;
	}

	public int getPendingWithOe() {
		return pendingWithOe;
	}

	public void setPendingWithOe(int pendingWithOe) {
		this.pendingWithOe = pendingWithOe;
	}

	public int getPerfSheetCompleted() {
		return perfSheetCompleted;
	}

	public void setPerfSheetCompleted(int perfSheetCompleted) {
		this.perfSheetCompleted = perfSheetCompleted;
	}

	public String getAssignedToName() {
		return assignedToName;
	}

	public void setAssignedToName(String assignedToName) {
		this.assignedToName = assignedToName;
	}

	public String getAssignedToEmpNum() {
		return assignedToEmpNum;
	}

	public void setAssignedToEmpNum(String assignedToEmpNum) {
		this.assignedToEmpNum = assignedToEmpNum;
	}

	public int getAssignedToEmpId() {
		return assignedToEmpId;
	}

	public void setAssignedToEmpId(int assignedToEmpId) {
		this.assignedToEmpId = assignedToEmpId;
	}

	public String getMonthName() {
		return monthName;
	}

	public void setMonthName(String monthName) {
		this.monthName = monthName;
	}

	public int getStsId() {
		return stsId;
	}

	public void setStsId(int stsId) {
		this.stsId = stsId;
	}

	public long getPerfSheetId() {
		return perfSheetId;
	}

	public void setPerfSheetId(long perfSheetId) {
		this.perfSheetId = perfSheetId;
	}

	public int getPendingSince() {
		return pendingSince;
	}

	public void setPendingSince(int pendingSince) {
		this.pendingSince = pendingSince;
	}

	public double getEmpScore() {
		return empScore;
	}

	public void setEmpScore(double empScore) {
		this.empScore = empScore;
	}

	public double getSupScore() {
		return supScore;
	}

	public void setSupScore(double supScore) {
		this.supScore = supScore;
	}

	public double getEmpPercentage() {
		return empPercentage;
	}

	public void setEmpPercentage(double empPercentage) {
		this.empPercentage = empPercentage;
	}

	public double getSupPercentage() {
		return supPercentage;
	}

	public void setSupPercentage(double supPercentage) {
		this.supPercentage = supPercentage;
	}

	public String getLineName() {
		return lineName;
	}

	public void setLineName(String lineName) {
		this.lineName = lineName;
	}

	public String getPendingWith() {
		return pendingWith;
	}

	public void setPendingWith(String pendingWith) {
		this.pendingWith = pendingWith;
	}

	public String getCompanyEmpNum() {
		return companyEmpNum;
	}

	public void setCompanyEmpNum(String companyEmpNum) {
		this.companyEmpNum = companyEmpNum;
	}

	public double getBasic() {
		return basic;
	}

	public void setBasic(double basic) {
		this.basic = basic;
	}

	public double getHra() {
		return hra;
	}

	public void setHra(double hra) {
		this.hra = hra;
	}

	public double getEduAll() {
		return eduAll;
	}

	public void setEduAll(double eduAll) {
		this.eduAll = eduAll;
	}

	public double getConAll() {
		return conAll;
	}

	public void setConAll(double conAll) {
		this.conAll = conAll;
	}

	public double getMedAll() {
		return medAll;
	}

	public void setMedAll(double medAll) {
		this.medAll = medAll;
	}

	public double getAttAll() {
		return attAll;
	}

	public void setAttAll(double attAll) {
		this.attAll = attAll;
	}

	public double getStipend() {
		return stipend;
	}

	public void setStipend(double stipend) {
		this.stipend = stipend;
	}

	public double getLta() {
		return lta;
	}

	public void setLta(double lta) {
		this.lta = lta;
	}

	public double getServiceAward() {
		return serviceAward;
	}

	public void setServiceAward(double serviceAward) {
		this.serviceAward = serviceAward;
	}

	public double getBonus() {
		return bonus;
	}

	public void setBonus(double bonus) {
		this.bonus = bonus;
	}

	public double getPf() {
		return pf;
	}

	public void setPf(double pf) {
		this.pf = pf;
	}

	public double getEsic() {
		return esic;
	}

	public void setEsic(double esic) {
		this.esic = esic;
	}

	public double getGratuity() {
		return gratuity;
	}

	public void setGratuity(double gratuity) {
		this.gratuity = gratuity;
	}

	public double getTldpAll() {
		return tldpAll;
	}

	public void setTldpAll(double tldpAll) {
		this.tldpAll = tldpAll;
	}

	public double getBtechAll() {
		return btechAll;
	}

	public void setBtechAll(double btechAll) {
		this.btechAll = btechAll;
	}

	public double getKnowAll() {
		return knowAll;
	}

	public void setKnowAll(double knowAll) {
		this.knowAll = knowAll;
	}

	public double getStoeStaffAll() {
		return stoeStaffAll;
	}

	public void setStoeStaffAll(double stoeStaffAll) {
		this.stoeStaffAll = stoeStaffAll;
	}

	public double getStoeShopFloorAll() {
		return stoeShopFloorAll;
	}

	public void setStoeShopFloorAll(double stoeShopFloorAll) {
		this.stoeShopFloorAll = stoeShopFloorAll;
	}

	public double getGrossSal() {
		return grossSal;
	}

	public void setGrossSal(double grossSal) {
		this.grossSal = grossSal;
	}

	public double getSpecialAlowance() {
		return specialAlowance;
	}

	public void setSpecialAlowance(double specialAlowance) {
		this.specialAlowance = specialAlowance;
	}

	public double getRetentionBonuns() {
		return retentionBonuns;
	}

	public void setRetentionBonuns(double retentionBonuns) {
		this.retentionBonuns = retentionBonuns;
	}
	

}
