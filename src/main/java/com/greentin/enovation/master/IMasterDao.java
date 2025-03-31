package com.greentin.enovation.master;

import java.util.List;
import java.util.Set;

import com.greentin.enovation.accesscontrol.ProductOrgConfig;
import com.greentin.enovation.beans.BenefitAnalysisMasterDTO;
import com.greentin.enovation.beans.Branch;
import com.greentin.enovation.beans.DepartmentLevelBean;
import com.greentin.enovation.beans.EmployeeDetailsBean;
import com.greentin.enovation.beans.ExecutiveListDto;
import com.greentin.enovation.beans.MoodIndicatorDto;
import com.greentin.enovation.beans.RolesBean;
import com.greentin.enovation.dto.MasterDTO;
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
import com.greentin.enovation.model.MasterDocument;
import com.greentin.enovation.model.MasterSugUserType;
import com.greentin.enovation.model.MasterUnit;
import com.greentin.enovation.model.MasterZone;
import com.greentin.enovation.model.MoodIndicator;
import com.greentin.enovation.model.OrgStatusPoints;
import com.greentin.enovation.model.OrganizationMaster;
import com.greentin.enovation.model.Particulars;
import com.greentin.enovation.model.ParticularsPoint;
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
import com.greentin.enovation.model.SuggestionEvalAuthoritySetup;
import com.greentin.enovation.model.SuggestionEvalJurySetup;
import com.greentin.enovation.model.SuggestionEvalMstrAssessment;
import com.greentin.enovation.model.SuggestionEvalMstrNomination;
import com.greentin.enovation.model.SuggestionEvalMstrParam;
import com.greentin.enovation.model.SuggestionEvalMstrRatings;
import com.greentin.enovation.model.SuggestionFlowConfig;
import com.greentin.enovation.model.SuggestionOnHoldSetup;
import com.greentin.enovation.model.SuggestionTemplate;
import com.greentin.enovation.model.SuggestionTemplateMaster;
import com.greentin.enovation.model.SuggestionUserSetup;
import com.greentin.enovation.model.TeamtypeMaster;
import com.greentin.enovation.model.ThoughtOfDay;
import com.greentin.enovation.model.ThresholdMaster;
import com.greentin.enovation.model.Vendor;
import com.greentin.enovation.model.dwm.Line;
import com.greentin.enovation.model.dwm.Shifts;
import com.greentin.enovation.response.MasterResponse;

public interface IMasterDao {

	List<DepartmentMaster> getdeptList();

	List<CategoryMaster> getCatagoryList();

	boolean addCategory(CategoryMaster cat);

	boolean addTeamType(TeamtypeMaster teamType);

	List<TeamtypeMaster> getTeamTypeList();

	List<StatusMaster> getStatusMasterList();

	List<OrganizationMaster> getOrgMasterList();

	OrganizationMaster addOrgMaster(OrganizationMaster org);//

	boolean checkDepartmentRecords(DepartmentMaster dept);

	boolean addOrganization(OrganizationMaster org);

	boolean deleteOrganization(OrganizationMaster organ);

	List<PointLevelMaster> getPointLevelList(Integer orgId);

	// List<DesignationMaster> getDesignationList();

	boolean isexitsorg(OrganizationMaster org);

	boolean isexitscat(CategoryMaster cate);

	boolean isexitsdept(DepartmentMaster dept);

	boolean isexitsteamtype(TeamtypeMaster teamType);

	List<BenefitAnalysisMasterDTO> getBenefitAnalysisList(Integer orgId, Integer branchId);

	List<BranchMaster> getbranchlist(Integer orgId);

	boolean addbranch(BranchMaster brnch);

	boolean addbenefitmaster(BenefitAnalysisMaster benefit/* , BenefitAnalysisMaster oldBenefit */);

	List<DepartmentMaster> getdepartmentlistbyorgid(Integer orgId);

	boolean updateExecutive(Executive executive);

	boolean saveExecutive(Executive executive);

	boolean updateDeptlvlExecutive(DepartmentLevel deptexecutive);

	boolean removeExecutive(Integer execId);

	List<ExecutiveListDto> getListOfExecutiveByOrgId(Integer orgId);

	List<DepartmentMaster> getdepartmentlistbybranchid(Integer branchId);

	// List<DesignationMaster> getDesignationListbyorgId(Integer orgId);

	List<CategoryMaster> getCategoryListbyorgId(Integer orgId, Integer branchId);

	// boolean addDesignation(DesignationMaster desig);

	boolean addDept(DepartmentMaster dept);

	// List<DesignationMaster> getDesignationListbybranchId(Integer branchId);

	// List<DesignationMaster> getDesignationListbyBranch(Integer branchId);

	int saveLevelWiseUsers(Set<DepartmentLevel> request);

	List<DepartmentLevelBean> getListofEmployeebyBranchWise(Integer branchId, Integer levelId, Integer deptId,
			Integer roleId);

	boolean updateBranch(BranchMaster brnch);

	boolean updateDepartment(DepartmentMaster dept);

	OrganizationMaster uploadOranizationLogo(OrganizationMaster org);

	boolean updateCategory(CategoryMaster cat);

	boolean saveStatusPoints(OrgStatusPoints orgsts);

	boolean updateStatusPoints(OrgStatusPoints orgsts);

	List<OrgStatusPoints> getListofStatusPoints(Integer branchId);

	List<StatusMaster> getListofStatusForBrowniePoints(Integer roleId);

	List<StatusMaster> getListofStatus();

	List<RolesBean> getRoleList();

	List<Countries> getCountriesList();

	List<CategoryMaster> getMasterCatagoryList();

	List<String> getBenefitAnalysisMasterList();

	public int deleteBenifits(BenefitAnalysisMaster benifits);

	public boolean deleteCategory(CategoryMaster cate);

	public int deleteDepartment(DepartmentMaster dept);

	public boolean disableExecutive(Executive executive);

	public int removeBrowinePoints(OrgStatusPoints orgstatuspoints);

	List<DepartmentMaster> getdepartmentlistbybranchidandDeptname(int branchId, Set<String> checkDeptNameSet);

	boolean updateIsEscalation(ProductOrgConfig request);

	boolean deleteExecutive(Executive executive);

	int updateLevelWiseUsers(EmployeeDetailsBean request);

	List<RoleEscalationSetup> checkRoleEscalationSetup(List<RoleEscalationSetup> request);

	boolean saveRoleEscalationSetup(MasterResponse request);

	boolean updateRoleEscalationSetup(RoleEscalationSetup request);

	boolean removeRoleEscalationSetup(RoleEscalationSetup request);

	List<RoleEscalationSetup> getRoleEscalationSetup(Integer branchId);

	boolean sendEmailsOnRegistration(Integer branchId);

	String getOrgLogo(String alies);

	List<DepartmentMaster> getDepartmentListForSuggestion(Integer branchId);

	List<SuggestionEscalationConfig> getSuggestionEscalationConfig(Integer branchId);

	boolean updateSuggestionEscalationConfig(SuggestionEscalationConfig request);

	boolean removeSuggestionEscalationConfig(SuggestionEscalationConfig request);

	List<BrowniePointsReimbursementCycleType> getBrowniePointsReimbursementCycleTypeList();

	BrowniePointsReimbursementCycle getBrowniePointsReimbursementCycle(Integer branchId);

	boolean updateBrowniePointsReimbursementCycle(BrowniePointsReimbursementCycle request);

	List<SuggestionEscalationConfig> checkBranchInSuggestionEscalationConfig(int branchId);

	boolean createSuggestionEscalationConfig(MasterResponse request);

	List<Particulars> getParticulars(Integer catId);

	List<Particulars> checkCategoryInParticulars(int catId, String string);

	boolean updateParticular(List<Particulars> req);

	boolean createParticular(List<Particulars> req);

	List<String> getParticularsList(int catId);

	int removeParticulars(Particulars particulars);

	List<SavingOptions> getSavingOptionByBranchId(Integer branchId);

	List<SavingOptionsRole> getSavingOptionRole(Integer savingOptionsId);

	List<Currency> getCurrencyList();

	List<SetPointsForSaving> getPointsForSaving(Integer branchId, Integer savingOptionsId, Integer roleId);

	List<SavingOptions> checkSavingOptionsExists(SavingOptions savingOptions);

	boolean createSavingOptions(SavingOptions savingOptions);

	boolean updateSavingOptions(SavingOptions savingOptions);

	List<SavingOptionsRole> checkSavingOptionsExists(SavingOptionsRole savingOptionsRole);

	boolean createSavingOptionsRole(SavingOptionsRole savingOptionsRole);

	boolean updateSavingOptionsRole(SavingOptionsRole savingOptionsRole);

	boolean createSetPointsForSaving(List<SetPointsForSaving> setPointsForSaving);

	boolean updateSetPointsForSaving(List<SetPointsForSaving> setPointsForSaving);

	List<SetPointsForSaving> checkSetPointsForSavingExists(SetPointsForSaving request);

	boolean addbenefitmasternew(BenefitAnalysisMaster benefit, BenefitAnalysisMaster oldBenefit);

	int addOrupdateDocument(MasterDocument document);

	List<SetPointsForSaving> getPointsForSavingByRoleId(Integer branchId, Integer savingOptionsRoleId,
			Integer savingOptionsId, Integer roleId);

	int removeSavingOptionsRole(SavingOptionsRole savingOptionsRole);

	int removeSetPointsForSaving(SetPointsForSaving setPointsForSaving);

	int deleteDocument(MasterDocument document);

	List<ActivityMaster> getActivityList();

	List<Branch> getBranchListNEW(Integer orgId);

	List<Branch> getBranchDetails(Integer branchId);

	List<ProductOrgConfig> getProductOrgConfigByBranchId(int branchId);

	List<LanguageMaster> getLanguageList();

	boolean deleteEscalator(RoleEscalationSetup request);

	boolean updateSuggEscalationConfig(SuggestionEscalationConfig request);

	boolean saveRewards(Rewards rewards);

	List<Rewards> getRewardsList(Integer branchId);

	boolean updateRewards(Rewards reward);

	boolean deleteRewards(Rewards reward);

	boolean saveVendorDetails(Vendor vendor);

	boolean updateVendorDetails(Vendor vendor);

	boolean deleteVendorDetails(Vendor vendor);

	List<Vendor> getVendorDetails(Integer branchId);

	boolean addRewardCategory(RewardCategoryMaster mrc);

	boolean addRewardResponsibleData(RewardResponsibleData rrd);

	boolean updateRewardResponsibleData(RewardResponsibleData rrd);

	boolean deleteRewardResponsibleData(RewardResponsibleData rrd);

	boolean addToCart(Cart cart);

	List<Cart> getCartList(Integer empId);

	boolean deleteFromCart(Cart cart);

	List<ThoughtOfDay> checkThoughtOfDay(ThoughtOfDay th);

	boolean addThought(ThoughtOfDay th);

	List<ThoughtOfDay> getThought(int branchId);

	List<MoodIndicator> checkMoodIndicator(MoodIndicator mo);

	boolean addMoodIndicator(MoodIndicator mo);

	int redeemFromCart(List<RedeemCartRewards> rfc);

	boolean saveRewardResponsibleSetup(RewardResponsibleSetup rrsetup);

	boolean updateRewardResponsibleSetup(RewardResponsibleSetup rrsetup);

	boolean deleteRewardResponsibleSetup(RewardResponsibleSetup rrsetup);

	List<RewardResponsibleSetup> getrewardResponsibleList(Integer branchId);

	boolean addRedeemStatus(RedeemStatus status);

	boolean updateStatus(RedeemStatus status);

	List<RedeemStatus> redeemStatusList();

	boolean updateRedeemFromCartRewards(RedeemCartRewards redeemfrmcart);

	List<RedeemCartRewards> getRedeemFromCartRewardList(Integer empId);

	boolean updateCartDetails(Cart cart);

	List<SocialMediaMaster> getSocialMediaList();

	List<RewardCategoryMaster> getRewardCategoryList(Integer orgId);

	List<ThresholdMaster> getThresholdList();

	boolean updateMoodIndicator(MoodIndicator mo);

	RewardResponsibleSetup checkRewardResponsiblePerson(RewardResponsibleSetup rrsetup);

	/* Incident Management Setup API's */

	List<IncidentManagerType> getManagerTypeList();

	boolean addIncidentOfficer(IncidentOfficerDetails inc);

	boolean updateIncidentOfficer(IncidentOfficerDetails inc);

	List<IncidentOfficerDetails> getIncidentOfficerDetails(Integer branchId);

	boolean addMachineDetails(MachineDetails mach);

	boolean updateMachineDetails(MachineDetails mach);

	boolean deleteMachineDetails(MachineDetails mach);

	List<MachineDetails> getMachineList(Integer branchId);

	// Master Lists
	List<IncidentNature> getIncidentNatureList();

	List<IncidentPriority> getIncidentPriorityList();

	List<IncidentReport> getIncidentReportList();

	List<IncidentSeverity> getIncidentSaverityList();

	boolean isSafetyOfficerExist(IncidentOfficerDetails inc);

	List<IncidentOfficerDetails> getIncidentManagerList(Integer branchId);

	List<SavingOptions> getSavingOptionsList();

	boolean isIncidentManagerExist(IncidentOfficerDetails inc);

	boolean isMachineExist(MachineDetails mach);

	List<Line> getLineNameList(Integer deptId);

	List<Line> checkLineNameExists(Line line);

	boolean addLineName(Line line);

	boolean updateLineName(Line line);

	List<Section> getSectionList(Integer branchId);

	List<Shifts> getShiftList(Shifts request);

	List<MasterZone> getZoneList(Integer branchId);

	int addOrUpdateZone(MasterZone zone);

	int addOrUpdateSection(Section section);

	List<CategoryMaster> getCategoryListByBranchId(Integer branchId);

	boolean addMachineParts(MachineParts parts);

	List<Object[]> getMachinePartsList(Integer machId, Integer branchId);

	boolean updateMachineParts(MachineParts parts);

	int deleteMachineParts(Long id);

	List<MachineDetails> getMachineListByLine(MachineDetails macDet);

	List<StatusMaster> getMasterStatusListByRoleId(List<Integer> roleIds);

	boolean saveSugOnHoldSetup(SuggestionOnHoldSetup onHold);

	boolean deleteSugOnHoldSetup(SuggestionOnHoldSetup onHold);

	boolean isOnHoldPendingActionsExists(SuggestionOnHoldSetup onHold);

	boolean savePasswordPolicy(PasswordPolicy policy);

	boolean updatePasswordPolicy(PasswordPolicy policy);

	PasswordPolicy getPasswordPolicyByOrgId(int orgId);

	boolean deletePasswordPolicy(long id);

	boolean enableDisablePasswordPolicy(PasswordPolicy policy);

	boolean isPasswordPolicyExist(PasswordPolicy policy);

	boolean saveSuggestionJurySetup(List<SuggestionEvalJurySetup> request);

	boolean updateSuggestionJurySetup(SuggestionEvalJurySetup request);

	boolean deleteSuggestionJurySetup(int id);

	List<RegistrationSource> getRegistrationSource();

	List<SuggestionEvalMstrParam> getSuggestionKaizenParameters();

	List<SuggestionEvalMstrRatings> getSuggestionKaizenRatings(int branchId);

	List<SuggestionEvalMstrAssessment> getSuggestionMasterAssesment();

	// To get Suggestion Eval Assesment Inputs by branch id
	List<SuggestionEvalAssessmentInput> getSuggestionAssesmentInputByBranchId(Integer branchId);

	// To save Suggestion Eval Assesment Inputs
	boolean saveSuggestionAssessmentInput(SuggestionEvalAssessmentInput request);

	boolean addSuggestionTemplate(SuggestionTemplate request);

	boolean updateSuggestionTemplate(SuggestionTemplate request);

	List<SuggestionTemplateMaster> suggestionTemplateMasterList();

	boolean deleteSuggestionTemplate(Long id);

	boolean updateSuggestionAssessmentInput(SuggestionEvalAssessmentInput request);

	List<SuggestionEvalMstrNomination> getSugEvalMasterNomination(int branchId);

	boolean addSugEvalAuthorityPhead(SuggestionEvalAuthoritySetup request);

	boolean isPheadExist(SuggestionEvalAuthoritySetup request);

	boolean deleteSugEvalAuthority(int id);

	boolean addSugEvalAuthorityHR(SuggestionEvalAuthoritySetup request);

	boolean updateRewardCategory(RewardCategoryMaster rcm);

	boolean deleteRewardCategory(int id);

	boolean saveRewardAdminSetup(RewardAdminSetup request);

	boolean deleteRewardAdminSetup(RewardAdminSetup request);

	List<RewardAdminSetup> getRewardAdminList(int branchId);

	boolean getRewardAdminListByEmpId(int empId);

	List<RedeemCartRewards> getRedemptionTrackerList(Integer branchId);

	int addOrUpdateSuggConfig(SuggestionFlowConfig config);

	List<SuggestionFlowConfig> getSuggConfigList(Integer orgId);

	List<OrganizationMaster> getOrgDataByAlieas(String alieas);

	boolean removeLineDetails(Line line);

	boolean deactiveLine(Line line);

	boolean updateOptionalJurySetup(SuggestionEvalJurySetup request);

	boolean deleteFromEmpRoles(SuggestionUserSetup request);

	boolean isEmpPendingActionsExists(SuggestionUserSetup request);

	List<Object[]> getEmpUserSetupData(SuggestionUserSetup request);

	boolean deleteSuggestionUserSetup(SuggestionUserSetup request);

	boolean insertIntoEmpRole(SuggestionUserSetup request);

	boolean isEmpRoleExist(SuggestionUserSetup request);

	List<Object[]> getSuggestionUserSetup(Integer branchId, Integer orgId);

	boolean saveSuggestionUserSetup(SuggestionUserSetup request);

	List<MasterSugUserType> getSuggUserTypes();

	boolean addUpdateParticularsPoints(ParticularsPoint particulars);

	List<MasterUnit> getMasterUnits();

	List<MasterDTO> employeeWiseHappinessIndex(MasterDTO request);

	/**
	 * @author rakesh 12 August 2020
	 */
	boolean addOrUpdateCategoryList(MasterDTO request);
	
	public boolean addCWAgencyDetails(CWAgency request);

	public List<CWAgency> getCWAgencyList(Integer orgId);

	boolean addDocument(MasterDocument request);

	boolean updateDocument(MasterDocument request);

	boolean removeDocument(MasterDocument request);

}
