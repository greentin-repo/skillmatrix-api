package com.greentin.enovation.response;

import java.util.HashMap;
import java.util.List;

import com.greentin.enovation.accesscontrol.ProductOrgConfig;
import com.greentin.enovation.beans.BenefitAnalysisMasterDTO;
import com.greentin.enovation.beans.Branch;
import com.greentin.enovation.beans.DepartmentLevelBean;
import com.greentin.enovation.beans.ExecutiveListDto;
import com.greentin.enovation.beans.MoodIndicatorDto;
import com.greentin.enovation.beans.RolesBean;
import com.greentin.enovation.dto.MasterDTO;
import com.greentin.enovation.employee.EmployeeDTO;
import com.greentin.enovation.model.ActivityMaster;
import com.greentin.enovation.model.BenefitAnalysisMaster;
import com.greentin.enovation.model.BranchMaster;
import com.greentin.enovation.model.BrowniePointsReimbursementCycle;
import com.greentin.enovation.model.BrowniePointsReimbursementCycleType;
import com.greentin.enovation.model.CWAgency;
import com.greentin.enovation.model.Cart;
import com.greentin.enovation.model.CategoryMaster;
import com.greentin.enovation.model.Countries;
import com.greentin.enovation.model.Currency;
import com.greentin.enovation.model.DepartmentLevel;
import com.greentin.enovation.model.DepartmentMaster;
import com.greentin.enovation.model.EmployeeDetails;
import com.greentin.enovation.model.Executive;
import com.greentin.enovation.model.IncidentManagerType;
import com.greentin.enovation.model.IncidentNature;
import com.greentin.enovation.model.IncidentOfficerDetails;
import com.greentin.enovation.model.IncidentPriority;
import com.greentin.enovation.model.IncidentReport;
import com.greentin.enovation.model.IncidentSeverity;
import com.greentin.enovation.model.LanguageMaster;
import com.greentin.enovation.model.MachineDetails;
import com.greentin.enovation.model.MachineParts;
import com.greentin.enovation.model.MasterSugUserType;
import com.greentin.enovation.model.MasterUnit;
import com.greentin.enovation.model.MasterZone;
import com.greentin.enovation.model.MoodIndicator;
import com.greentin.enovation.model.OrgStatusPoints;
import com.greentin.enovation.model.OrganizationMaster;
import com.greentin.enovation.model.Particulars;
import com.greentin.enovation.model.PasswordPolicy;
import com.greentin.enovation.model.PointLevelMaster;
import com.greentin.enovation.model.RedeemCartRewards;
import com.greentin.enovation.model.RedeemStatus;
import com.greentin.enovation.model.RegistrationSource;
import com.greentin.enovation.model.RewardAdminSetup;
import com.greentin.enovation.model.RewardCategoryMaster;
import com.greentin.enovation.model.RewardResponsibleData;
import com.greentin.enovation.model.RewardResponsibleSetup;
import com.greentin.enovation.model.Rewards;
import com.greentin.enovation.model.RoleEscalationSetup;
import com.greentin.enovation.model.SavingOptions;
import com.greentin.enovation.model.SavingOptionsRole;
import com.greentin.enovation.model.Section;
import com.greentin.enovation.model.SetPointsForSaving;
import com.greentin.enovation.model.SocialMediaMaster;
import com.greentin.enovation.model.StatusMaster;
import com.greentin.enovation.model.SuggestionEscalationConfig;
import com.greentin.enovation.model.SuggestionEvalAssessmentInput;
import com.greentin.enovation.model.SuggestionEvalMstrAssessment;
import com.greentin.enovation.model.SuggestionEvalMstrNomination;
import com.greentin.enovation.model.SuggestionEvalMstrParam;
import com.greentin.enovation.model.SuggestionEvalMstrRatings;
import com.greentin.enovation.model.SuggestionFlowConfig;
import com.greentin.enovation.model.SuggestionTemplate;
import com.greentin.enovation.model.SuggestionTemplateMaster;
import com.greentin.enovation.model.TeamtypeMaster;
import com.greentin.enovation.model.ThoughtOfDay;
import com.greentin.enovation.model.ThresholdMaster;
import com.greentin.enovation.model.Vendor;
import com.greentin.enovation.model.dwm.Line;
import com.greentin.enovation.model.dwm.Shifts;

public class MasterResponse {

	private String status;

	private Integer statusCode;

	private String reason;

	private boolean result;

	private String logoPath;

	private int numberOfDays;

	List<EmployeeDetails> empErrorList;

	HashMap<String, Object> pendingList;

	private int numberOfSuggestion;

	private int roleId;

	MoodIndicator moodIndicator;

	BenefitAnalysisMaster newBenefits;

	BenefitAnalysisMaster oldBenefits;

	BrowniePointsReimbursementCycle BrowniePointsReimbursementCycleDetails;

	List<ActivityMaster> activityList;

	List<SuggestionTemplate> suggestionTemplate;

	List<ProductOrgConfig> producOrgConfigList;

	List<Particulars> particulars;

	List<BranchMaster> branchList;

	List<DepartmentMaster> deptList;

	List<DepartmentMaster> deptListForSugg;

	List<RewardResponsibleData> rewardResponsibleList;

	List<CategoryMaster> catagoryList;

	List<MasterSugUserType> sugUserTypes;

	List<StatusMaster> statusList;

	List<OrganizationMaster> orgList;

	List<DepartmentLevel> deptlvlList;;

	List<DepartmentLevel> escalationAlreadyAlignedList;

	List<DepartmentLevelBean> deptlvlusersList;;

	List<PointLevelMaster> pointmaster;

	List<TeamtypeMaster> teamtypemaster;

	List<RoleEscalationSetup> roleEscalationSetupList;

	List<SuggestionEscalationConfig> suggestionEscalationConfigList;

	List<Branch> branch;

	List<ThoughtOfDay> thoughtList;

	List<MoodIndicatorDto> moodEmojiList;

	List<Shifts> shiftsList;

	List<MachineParts> machinePartsList;

	List<SuggestionTemplateMaster> suggestionTemplateMasterList;

	/*
	 * List<DesignationMaster> designationmaster;
	 * 
	 * List<DesignationMaster> designation;
	 */

	List<PointLevelMaster> Pointmaster;

	List<ExecutiveListDto> executiveList;

	List<Executive> executiveDetailsList;

	List<OrgStatusPoints> orgStatusList;

	List<BenefitAnalysisMasterDTO> benefitAnalysisList;

	List<RolesBean> roleList;

	List<Countries> counteryList;

	List<BrowniePointsReimbursementCycleType> BrowniePointsReimbursementCycleTypeList;

	List<SavingOptions> savingOption;

	List<SavingOptionsRole> savingOptionRole;

	List<Currency> currencyList;

	List<SetPointsForSaving> setPointsForSavingList;

	List<LanguageMaster> langList;

	List<RoleEscalationSetup> roleEscalationList;

	List<Rewards> rewardsList;

	List<Vendor> vendorDetails;

	List<Line> lineList;

	private int branchId;

	private int orgId;

	private int savingOptionsId;

	private EmployeeDTO excelUploadError;

	List<Cart> cartList;

	List<RewardResponsibleSetup> rewardResponsibleSetupList;

	List<RedeemStatus> redeemStatusList;

	List<RedeemCartRewards> redeemCartList;

	List<RewardResponsibleData> rewardResponsibleDataList;

	List<RewardCategoryMaster> rewardCategoryList;

	List<ThresholdMaster> threshold;

	List<IncidentOfficerDetails> incidentOfficerDetails;

	List<IncidentManagerType> ManagerTypeList;

	List<MachineDetails> machineList;

	List<IncidentPriority> incidentPriority;

	List<IncidentReport> incidentReport;

	List<IncidentNature> incidentNature;

	List<IncidentSeverity> incidentSaverity;

	List<IncidentOfficerDetails> incidentManagerList;

	private List<Integer> roleIds;

	PasswordPolicy passwordPolicy;

	List<RegistrationSource> regSource;

	List<SuggestionEvalMstrParam> suggestionKaizenParameter;

	List<SuggestionEvalMstrRatings> suggestionKaizenRating;

	List<SuggestionEvalMstrAssessment> suggMasterAssessment;

	List<SuggestionEvalAssessmentInput> suggMasterAssessmentInput;

	List<SuggestionEvalMstrNomination> suggestionEvalMstrNomination;

	List<RewardAdminSetup> rewardAdminList;

	private int isEnable;

	private List<SuggestionFlowConfig> SuggFlowConfig;

	private List<MasterUnit> unitList;

	private List<MasterDTO> happinessList;

	private List<CWAgency> cwAgencyList;

	public List<IncidentOfficerDetails> getIncidentManagerList() {
		return incidentManagerList;
	}

	public void setIncidentManagerList(List<IncidentOfficerDetails> incidentManagerList) {
		this.incidentManagerList = incidentManagerList;
	}

	public List<IncidentPriority> getIncidentPriority() {
		return incidentPriority;
	}

	public void setIncidentPriority(List<IncidentPriority> incidentPriority) {
		this.incidentPriority = incidentPriority;
	}

	public List<IncidentReport> getIncidentReport() {
		return incidentReport;
	}

	public void setIncidentReport(List<IncidentReport> incidentReport) {
		this.incidentReport = incidentReport;
	}

	public List<IncidentNature> getIncidentNature() {
		return incidentNature;
	}

	public void setIncidentNature(List<IncidentNature> incidentNature) {
		this.incidentNature = incidentNature;
	}

	public List<IncidentSeverity> getIncidentSaverity() {
		return incidentSaverity;
	}

	public void setIncidentSaverity(List<IncidentSeverity> incidentSaverity) {
		this.incidentSaverity = incidentSaverity;
	}

	public List<MachineDetails> getMachineList() {
		return machineList;
	}

	public void setMachineList(List<MachineDetails> machineList) {
		this.machineList = machineList;
	}

	public List<IncidentManagerType> getManagerTypeList() {
		return ManagerTypeList;
	}

	public void setManagerTypeList(List<IncidentManagerType> managerTypeList) {
		ManagerTypeList = managerTypeList;
	}

	public List<IncidentOfficerDetails> getIncidentOfficerDetails() {
		return incidentOfficerDetails;
	}

	public void setIncidentOfficerDetails(List<IncidentOfficerDetails> incidentOfficerDetails) {
		this.incidentOfficerDetails = incidentOfficerDetails;
	}

	public List<RewardCategoryMaster> getRewardCategoryList() {
		return rewardCategoryList;
	}

	public void setRewardCategoryList(List<RewardCategoryMaster> rewardCategoryList) {
		this.rewardCategoryList = rewardCategoryList;
	}

	public List<RedeemCartRewards> getRedeemCartList() {
		return redeemCartList;
	}

	public void setRedeemCartList(List<RedeemCartRewards> redeemCartList) {
		this.redeemCartList = redeemCartList;
	}

	public List<RedeemStatus> getRedeemStatusList() {
		return redeemStatusList;
	}

	public void setRedeemStatusList(List<RedeemStatus> redeemStatusList) {
		this.redeemStatusList = redeemStatusList;
	}

	List<SocialMediaMaster> socialMediaList;

	public List<TeamtypeMaster> getTeamtypemaster() {
		return teamtypemaster;
	}

	public void setTeamtypemaster(List<TeamtypeMaster> teamtypemaster) {
		this.teamtypemaster = teamtypemaster;
	}

	public boolean isResult() {
		return result;
	}

	public void setResult(boolean result) {
		this.result = result;
	}

	public String getReason() {
		return reason;
	}

	public void setReason(String reason) {
		this.reason = reason;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public Integer getStatusCode() {
		return statusCode;
	}

	public void setStatusCode(Integer statusCode) {
		this.statusCode = statusCode;
	}

	public List<DepartmentMaster> getDeptList() {
		return deptList;
	}

	public void setDeptList(List<DepartmentMaster> deptList) {
		this.deptList = deptList;
	}

	public List<CategoryMaster> getCatagoryList() {
		return catagoryList;
	}

	public void setCatagoryList(List<CategoryMaster> catagoryList) {
		this.catagoryList = catagoryList;
	}

	public List<OrganizationMaster> getOrgList() {
		return orgList;
	}

	public void setOrgList(List<OrganizationMaster> orgList) {
		this.orgList = orgList;
	}

	public List<StatusMaster> getStatusList() {
		return statusList;
	}

	public void setStatusList(List<StatusMaster> statusList) {
		this.statusList = statusList;
	}

	/*
	 * public List<DesignationMaster> getDesignation() { return designation; }
	 * public void setDesignation(List<DesignationMaster> designation) {
	 * this.designation = designation; }
	 */
	public List<PointLevelMaster> getPointmaster() {
		return Pointmaster;
	}

	public void setPointmaster(List<PointLevelMaster> pointmaster) {
		Pointmaster = pointmaster;
	}

	public List<BenefitAnalysisMasterDTO> getBenefitAnalysisList() {
		return benefitAnalysisList;
	}

	public void setBenefitAnalysisList(List<BenefitAnalysisMasterDTO> benefitAnalysisList) {
		this.benefitAnalysisList = benefitAnalysisList;
	}

	public List<BranchMaster> getBranchList() {
		return branchList;
	}

	public void setBranchList(List<BranchMaster> branchList) {
		this.branchList = branchList;
	}

	public List<ExecutiveListDto> getExecutiveList() {
		return executiveList;
	}

	public void setExecutiveList(List<ExecutiveListDto> executiveList) {
		this.executiveList = executiveList;
	}

	public List<Executive> getExecutiveDetailsList() {
		return executiveDetailsList;
	}

	public void setExecutiveDetailsList(List<Executive> executiveDetailsList) {
		this.executiveDetailsList = executiveDetailsList;
	}

	public List<DepartmentLevel> getDeptlvlList() {
		return deptlvlList;
	}

	public void setDeptlvlList(List<DepartmentLevel> deptlvlList) {
		this.deptlvlList = deptlvlList;
	}

	/*
	 * public List<DesignationMaster> getDesignationmaster() { return
	 * designationmaster; } public void setDesignationmaster(List<DesignationMaster>
	 * designationmaster) { this.designationmaster = designationmaster; }
	 */
	public List<OrgStatusPoints> getOrgStatusList() {
		return orgStatusList;
	}

	public void setOrgStatusList(List<OrgStatusPoints> orgStatusList) {
		this.orgStatusList = orgStatusList;
	}

	public String getLogoPath() {
		return logoPath;
	}

	public void setLogoPath(String logoPath) {
		this.logoPath = logoPath;
	}

	public List<DepartmentLevelBean> getDeptlvlusersList() {
		return deptlvlusersList;
	}

	public void setDeptlvlusersList(List<DepartmentLevelBean> deptlvlusersList) {
		this.deptlvlusersList = deptlvlusersList;
	}

	public List<RolesBean> getRoleList() {
		return roleList;
	}

	public void setRoleList(List<RolesBean> roleList) {
		this.roleList = roleList;
	}

	public List<Countries> getCounteryList() {
		return counteryList;
	}

	public void setCounteryList(List<Countries> counteryList) {
		this.counteryList = counteryList;
	}

	public List<DepartmentLevel> getEscalationAlreadyAlignedList() {
		return escalationAlreadyAlignedList;
	}

	public void setEscalationAlreadyAlignedList(List<DepartmentLevel> escalationAlreadyAlignedList) {
		this.escalationAlreadyAlignedList = escalationAlreadyAlignedList;
	}

	public List<RoleEscalationSetup> getRoleEscalationSetupList() {
		return roleEscalationSetupList;
	}

	public void setRoleEscalationSetupList(List<RoleEscalationSetup> roleEscalationSetupList) {
		this.roleEscalationSetupList = roleEscalationSetupList;
	}

	public BrowniePointsReimbursementCycle getBrowniePointsReimbursementCycleDetails() {
		return BrowniePointsReimbursementCycleDetails;
	}

	public void setBrowniePointsReimbursementCycleDetails(
			BrowniePointsReimbursementCycle browniePointsReimbursementCycleDetails) {
		BrowniePointsReimbursementCycleDetails = browniePointsReimbursementCycleDetails;
	}

	public List<BrowniePointsReimbursementCycleType> getBrowniePointsReimbursementCycleTypeList() {
		return BrowniePointsReimbursementCycleTypeList;
	}

	public void setBrowniePointsReimbursementCycleTypeList(
			List<BrowniePointsReimbursementCycleType> browniePointsReimbursementCycleTypeList) {
		BrowniePointsReimbursementCycleTypeList = browniePointsReimbursementCycleTypeList;
	}

	public List<SuggestionEscalationConfig> getSuggestionEscalationConfigList() {
		return suggestionEscalationConfigList;
	}

	public void setSuggestionEscalationConfigList(List<SuggestionEscalationConfig> suggestionEscalationConfigList) {
		this.suggestionEscalationConfigList = suggestionEscalationConfigList;
	}

	public List<Particulars> getParticulars() {
		return particulars;
	}

	public void setParticulars(List<Particulars> particulars) {
		this.particulars = particulars;
	}

	public List<SavingOptions> getSavingOption() {
		return savingOption;
	}

	public void setSavingOption(List<SavingOptions> savingOption) {
		this.savingOption = savingOption;
	}

	public List<SavingOptionsRole> getSavingOptionRole() {
		return savingOptionRole;
	}

	public void setSavingOptionRole(List<SavingOptionsRole> savingOptionRole) {
		this.savingOptionRole = savingOptionRole;
	}

	public List<Currency> getCurrencyList() {
		return currencyList;
	}

	public void setCurrencyList(List<Currency> currencyList) {
		this.currencyList = currencyList;
	}

	public List<SetPointsForSaving> getSetPointsForSavingList() {
		return setPointsForSavingList;
	}

	public void setSetPointsForSavingList(List<SetPointsForSaving> setPointsForSavingList) {
		this.setPointsForSavingList = setPointsForSavingList;
	}

	public BenefitAnalysisMaster getNewBenefits() {
		return newBenefits;
	}

	public void setNewBenefits(BenefitAnalysisMaster newBenefits) {
		this.newBenefits = newBenefits;
	}

	public BenefitAnalysisMaster getOldBenefits() {
		return oldBenefits;
	}

	public void setOldBenefits(BenefitAnalysisMaster oldBenefits) {
		this.oldBenefits = oldBenefits;
	}

	public List<ActivityMaster> getActivityList() {
		return activityList;
	}

	public void setActivityList(List<ActivityMaster> activityList) {
		this.activityList = activityList;
	}

	public List<Branch> getBranch() {
		return branch;
	}

	public void setBranch(List<Branch> branch) {
		this.branch = branch;
	}

	public int getBranchId() {
		return branchId;
	}

	public void setBranchId(int branchId) {
		this.branchId = branchId;
	}

	public int getOrgId() {
		return orgId;
	}

	public void setOrgId(int orgId) {
		this.orgId = orgId;
	}

	public int getSavingOptionsId() {
		return savingOptionsId;
	}

	public void setSavingOptionsId(int savingOptionsId) {
		this.savingOptionsId = savingOptionsId;
	}

	public List<DepartmentMaster> getDeptListForSugg() {
		return deptListForSugg;
	}

	public void setDeptListForSugg(List<DepartmentMaster> deptListForSugg) {
		this.deptListForSugg = deptListForSugg;
	}

	public List<ProductOrgConfig> getProducOrgConfigList() {
		return producOrgConfigList;
	}

	public void setProducOrgConfigList(List<ProductOrgConfig> producOrgConfigList) {
		this.producOrgConfigList = producOrgConfigList;
	}

	public List<LanguageMaster> getLangList() {
		return langList;
	}

	public void setLangList(List<LanguageMaster> langList) {
		this.langList = langList;
	}

	public int getNumberOfDays() {
		return numberOfDays;
	}

	public void setNumberOfDays(int numberOfDays) {
		this.numberOfDays = numberOfDays;
	}

	public int getNumberOfSuggestion() {
		return numberOfSuggestion;
	}

	public void setNumberOfSuggestion(int numberOfSuggestion) {
		this.numberOfSuggestion = numberOfSuggestion;
	}

	public int getRoleId() {
		return roleId;
	}

	public void setRoleId(int roleId) {
		this.roleId = roleId;
	}

	public List<RoleEscalationSetup> getRoleEscalationList() {
		return roleEscalationList;
	}

	public void setRoleEscalationList(List<RoleEscalationSetup> roleEscalationList) {
		this.roleEscalationList = roleEscalationList;
	}

	public List<Rewards> getRewardsList() {
		return rewardsList;
	}

	public void setRewardsList(List<Rewards> rewardsList) {
		this.rewardsList = rewardsList;
	}

	public List<Vendor> getVendorDetails() {
		return vendorDetails;
	}

	public void setVendorDetails(List<Vendor> vendorDetails) {
		this.vendorDetails = vendorDetails;
	}

	public List<ThoughtOfDay> getThoughtList() {
		return thoughtList;
	}

	public void setThoughtList(List<ThoughtOfDay> thoughtList) {
		this.thoughtList = thoughtList;
	}

	public MoodIndicator getMoodIndicator() {
		return moodIndicator;
	}

	public void setMoodIndicator(MoodIndicator moodIndicator) {
		this.moodIndicator = moodIndicator;
	}

	public List<RewardResponsibleData> getRewardResponsibleList() {
		return rewardResponsibleList;
	}

	public void setRewardResponsibleList(List<RewardResponsibleData> rewardResponsibleList) {
		this.rewardResponsibleList = rewardResponsibleList;
	}

	public List<Cart> getCartList() {
		return cartList;
	}

	public void setCartList(List<Cart> cartList) {
		this.cartList = cartList;
	}

	public List<RewardResponsibleSetup> getRewardResponsibleSetupList() {
		return rewardResponsibleSetupList;
	}

	public void setRewardResponsibleSetupList(List<RewardResponsibleSetup> rewardResponsibleSetupList) {
		this.rewardResponsibleSetupList = rewardResponsibleSetupList;
	}

	public List<SocialMediaMaster> getSocialMediaList() {
		return socialMediaList;
	}

	public void setSocialMediaList(List<SocialMediaMaster> socialMediaList) {
		this.socialMediaList = socialMediaList;
	}

	public List<MoodIndicatorDto> getMoodEmojiList() {
		return moodEmojiList;
	}

	public void setMoodEmojiList(List<MoodIndicatorDto> moodEmojiList) {
		this.moodEmojiList = moodEmojiList;
	}

	public List<RewardResponsibleData> getRewardResponsibleDataList() {
		return rewardResponsibleDataList;
	}

	public void setRewardResponsibleDataList(List<RewardResponsibleData> rewardResponsibleDataList) {
		this.rewardResponsibleDataList = rewardResponsibleDataList;
	}

	public List<ThresholdMaster> getThreshold() {
		return threshold;
	}

	public void setThreshold(List<ThresholdMaster> threshold) {
		this.threshold = threshold;
	}

	public List<Line> getLineList() {
		return lineList;
	}

	public void setLineList(List<Line> lineList) {
		this.lineList = lineList;
	}

	public List<Shifts> getShiftsList() {
		return shiftsList;
	}

	public void setShiftsList(List<Shifts> shiftsList) {
		this.shiftsList = shiftsList;
	}

	public List<MachineParts> getMachinePartsList() {
		return machinePartsList;
	}

	public void setMachinePartsList(List<MachineParts> machinePartsList) {
		this.machinePartsList = machinePartsList;
	}

	public List<Integer> getRoleIds() {
		return roleIds;
	}

	public void setRoleIds(List<Integer> roleIds) {
		this.roleIds = roleIds;
	}

	public PasswordPolicy getPasswordPolicy() {
		return passwordPolicy;
	}

	public void setPasswordPolicy(PasswordPolicy passwordPolicy) {
		this.passwordPolicy = passwordPolicy;
	}

	public List<SuggestionTemplate> getSuggestionTemplate() {
		return suggestionTemplate;
	}

	public void setSuggestionTemplate(List<SuggestionTemplate> suggestionTemplate) {
		this.suggestionTemplate = suggestionTemplate;
	}

	public List<SuggestionTemplateMaster> getSuggestionTemplateMasterList() {
		return suggestionTemplateMasterList;
	}

	public void setSuggestionTemplateMasterList(List<SuggestionTemplateMaster> suggestionTemplateMasterList) {
		this.suggestionTemplateMasterList = suggestionTemplateMasterList;
	}

	public List<RegistrationSource> getRegSource() {
		return regSource;
	}

	public void setRegSource(List<RegistrationSource> regSource) {
		this.regSource = regSource;
	}

	public List<SuggestionEvalMstrParam> getSuggestionKaizenParameter() {
		return suggestionKaizenParameter;
	}

	public void setSuggestionKaizenParameter(List<SuggestionEvalMstrParam> suggestionKaizenParameter) {
		this.suggestionKaizenParameter = suggestionKaizenParameter;
	}

	public List<SuggestionEvalMstrRatings> getSuggestionKaizenRating() {
		return suggestionKaizenRating;
	}

	public void setSuggestionKaizenRating(List<SuggestionEvalMstrRatings> suggestionKaizenRating) {
		this.suggestionKaizenRating = suggestionKaizenRating;
	}

	public List<SuggestionEvalMstrAssessment> getSuggMasterAssessment() {
		return suggMasterAssessment;
	}

	public void setSuggMasterAssessment(List<SuggestionEvalMstrAssessment> suggMasterAssessment) {
		this.suggMasterAssessment = suggMasterAssessment;
	}

	public List<SuggestionEvalAssessmentInput> getSuggMasterAssessmentInput() {
		return suggMasterAssessmentInput;
	}

	public void setSuggMasterAssessmentInput(List<SuggestionEvalAssessmentInput> suggMasterAssessmentInput) {
		this.suggMasterAssessmentInput = suggMasterAssessmentInput;
	}

	public List<SuggestionEvalMstrNomination> getSuggestionEvalMstrNomination() {
		return suggestionEvalMstrNomination;
	}

	public void setSuggestionEvalMstrNomination(List<SuggestionEvalMstrNomination> suggestionEvalMstrNomination) {
		this.suggestionEvalMstrNomination = suggestionEvalMstrNomination;
	}

	public int getIsEnable() {
		return isEnable;
	}

	public void setIsEnable(int isEnable) {
		this.isEnable = isEnable;
	}

	public List<RewardAdminSetup> getRewardAdminList() {
		return rewardAdminList;
	}

	public void setRewardAdminList(List<RewardAdminSetup> rewardAdminList) {
		this.rewardAdminList = rewardAdminList;
	}

	public List<SuggestionFlowConfig> getSuggFlowConfig() {
		return SuggFlowConfig;
	}

	public void setSuggFlowConfig(List<SuggestionFlowConfig> suggFlowConfig) {
		SuggFlowConfig = suggFlowConfig;
	}

	public List<MasterSugUserType> getSugUserTypes() {
		return sugUserTypes;
	}

	public void setSugUserTypes(List<MasterSugUserType> sugUserTypes) {
		this.sugUserTypes = sugUserTypes;
	}

	public List<MasterUnit> getUnitList() {
		return unitList;
	}

	public void setUnitList(List<MasterUnit> unitList) {
		this.unitList = unitList;
	}

	public List<MasterDTO> getHappinessList() {
		return happinessList;
	}

	public void setHappinessList(List<MasterDTO> happinessList) {
		this.happinessList = happinessList;
	}

	public EmployeeDTO getExcelUploadError() {
		return excelUploadError;
	}

	public void setExcelUploadError(EmployeeDTO excelUploadError) {
		this.excelUploadError = excelUploadError;
	}

	public List<EmployeeDetails> getEmpErrorList() {
		return empErrorList;
	}

	public void setEmpErrorList(List<EmployeeDetails> empErrorList) {
		this.empErrorList = empErrorList;
	}

	public HashMap<String, Object> getPendingList() {
		return pendingList;
	}

	public void setPendingList(HashMap<String, Object> pendingList) {
		this.pendingList = pendingList;
	}

	public List<CWAgency> getCwAgencyList() {
		return cwAgencyList;
	}

	public void setCwAgencyList(List<CWAgency> cwAgencyList) {
		this.cwAgencyList = cwAgencyList;
	}

}