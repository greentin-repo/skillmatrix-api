package com.greentin.enovation.master;

import java.util.HashMap;
import java.util.List;
import java.util.Set;

import com.greentin.enovation.accesscontrol.ProductOrgConfig;
import com.greentin.enovation.beans.EmployeeDetailsBean;
import com.greentin.enovation.dto.MasterDTO;
import com.greentin.enovation.model.BenefitAnalysisMaster;
import com.greentin.enovation.model.BranchMaster;
import com.greentin.enovation.model.BrowniePointsReimbursementCycle;
import com.greentin.enovation.model.CWAgency;
import com.greentin.enovation.model.Cart;
import com.greentin.enovation.model.CategoryMaster;
import com.greentin.enovation.model.DepartmentLevel;
import com.greentin.enovation.model.DepartmentMaster;
import com.greentin.enovation.model.Executive;
import com.greentin.enovation.model.IncidentOfficerDetails;
import com.greentin.enovation.model.MachineDetails;
import com.greentin.enovation.model.MachineParts;
import com.greentin.enovation.model.MasterDocument;
import com.greentin.enovation.model.MasterZone;
import com.greentin.enovation.model.MoodIndicator;
import com.greentin.enovation.model.OrgStatusPoints;
import com.greentin.enovation.model.OrganizationMaster;
import com.greentin.enovation.model.Particulars;
import com.greentin.enovation.model.ParticularsPoint;
import com.greentin.enovation.model.PasswordPolicy;
import com.greentin.enovation.model.RedeemCartRewards;
import com.greentin.enovation.model.RedeemStatus;
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
import com.greentin.enovation.model.SuggestionEscalationConfig;
import com.greentin.enovation.model.SuggestionEvalAssessmentInput;
import com.greentin.enovation.model.SuggestionEvalAuthoritySetup;
import com.greentin.enovation.model.SuggestionEvalJurySetup;
import com.greentin.enovation.model.SuggestionFlowConfig;
import com.greentin.enovation.model.SuggestionOnHoldSetup;
import com.greentin.enovation.model.SuggestionTemplate;
import com.greentin.enovation.model.SuggestionUserSetup;
import com.greentin.enovation.model.TeamtypeMaster;
import com.greentin.enovation.model.ThoughtOfDay;
import com.greentin.enovation.model.Vendor;
import com.greentin.enovation.model.dwm.Line;
import com.greentin.enovation.model.dwm.Shifts;
import com.greentin.enovation.response.MasterResponse;

public interface IMasterService {

	MasterResponse getCatagoryListService();

	MasterResponse addDepartmentService(DepartmentMaster dept);

	/* HashMap<String, Object> addCategoryService(CategoryMaster cat);//delete */

	MasterResponse addTeamTypeService(TeamtypeMaster teamType);

	MasterResponse getTeamTypeListService();

	MasterResponse getStatusMasterListService();

	MasterResponse getOrgMasterListService();

	HashMap<String, Object> addOrgMasterService(OrganizationMaster org);// delete

	MasterResponse addOrganizationService(OrganizationMaster org);

	MasterResponse deleteOrganization(OrganizationMaster organ);

	MasterResponse deleteCategory(CategoryMaster cate);

	MasterResponse getPointLevelListService(Integer orgId);

	MasterResponse deleteDepartment(DepartmentMaster dept);

	// MasterResponse getDesignationListService();

	MasterResponse getdeptListService();

	MasterResponse addCategoryService(CategoryMaster cat);

	MasterResponse getBenefitAnalysisList(Integer orgId, Integer branchId);

	MasterResponse getbranchlistService(Integer orgId);

	MasterResponse addbranch(BranchMaster brnch);

	MasterResponse addbenefitmaster(BenefitAnalysisMaster benefit);

	MasterResponse addbenefitmasternew(BenefitAnalysisMaster newBenefit, BenefitAnalysisMaster oldBenefit);

	MasterResponse getdepartmentlistbyorgid(Integer orgId);

	MasterResponse getListOfExecutiveByOrgId(Integer orgId);

	MasterResponse removeExecutive(Integer execId);

	MasterResponse updateDeptlvlExecutive(DepartmentLevel deptexecutive);

	MasterResponse updateExecutive(Executive executive);

	MasterResponse saveExecutive(Executive executive);

	MasterResponse getdepartmentlistbybranchid(Integer branchId);

	// MasterResponse getDesignationListbyorgId(Integer orgId);

	MasterResponse getCategoryListbyorgId(Integer orgId, Integer branchId);

	// MasterResponse addDesignation(DesignationMaster desig);

	MasterResponse addDepartment(DepartmentMaster dept);

	// MasterResponse getDesignationListbybranchId(Integer branchId);

	// MasterResponse getDesignationListbyBranch(Integer branchId);

	MasterResponse saveLevelWiseUsers(Set<DepartmentLevel> request);

	MasterResponse getListofEmployeebyBranchWise(Integer branchId, Integer levelId, Integer deptId, Integer roleId);

	MasterResponse updateBranch(BranchMaster brnch);

	MasterResponse uploadOranizationLogo(OrganizationMaster org);

	MasterResponse updateCategory(CategoryMaster cat);

	MasterResponse saveStatusPoints(OrgStatusPoints orgsts);

	MasterResponse updateStatusPoints(OrgStatusPoints orgsts);

	MasterResponse getListofStatusPoints(Integer branchId);

	MasterResponse getListofStatusForBrowniePoints(Integer roleId);

	MasterResponse getRoleList();

	MasterResponse getCounteriesList();

	MasterResponse deleteBenefit(BenefitAnalysisMaster cate);

	MasterResponse disableExecutive(Executive executive);

	MasterResponse removeBrowinePoints(OrgStatusPoints orgstatuspoints);

	MasterResponse updateIsEscalation(ProductOrgConfig request);

	MasterResponse deleteExecutive(Executive executive);

	MasterResponse updateLevelWiseUsers(EmployeeDetailsBean request);

	MasterResponse saveRoleEscalationSetup(MasterResponse request);

	MasterResponse updateRoleEscalationSetup(RoleEscalationSetup request);

	MasterResponse removeRoleEscalationSetup(RoleEscalationSetup request);

	MasterResponse getRoleEscalationSetup(Integer branchId);

	MasterResponse sendEmailsOnRegistration(Integer branchId);

	MasterResponse getOrgLogo(String alies);

	MasterResponse getDepartmentListForSuggestion(Integer branchId);

	MasterResponse updateSuggestionEscalationConfig(MasterResponse request);

	MasterResponse removeSuggestionEscalationConfig(SuggestionEscalationConfig request);

	MasterResponse getSuggestionEscalationConfig(Integer branchId);

	MasterResponse getBrowniePointsReimbursementCycleTypeList();

	MasterResponse getBrowniePointsReimbursementCycle(Integer branchId);

	MasterResponse updateBrowniePointsReimbursementCycle(BrowniePointsReimbursementCycle request);

	MasterResponse getParticulars(Integer catId);

	MasterResponse updateParticulars(List<Particulars> request);

	MasterResponse removeParticulars(Particulars particulars);

	MasterResponse getSavingOptionByBranchId(Integer branchId);

	MasterResponse getSavingOptionRole(Integer savingOptionsId);

	MasterResponse getCurrencyList();

	MasterResponse getPointsForSaving(Integer branchId);

	MasterResponse getPointsForSavingBySavingOptionsId(Integer savingOptionsId, Integer roleId);

	MasterResponse createOrUpdateSavingOptions(SavingOptions savingOptions);

	MasterResponse createOrUpdateSavingOptionsRole(SavingOptionsRole savingOptionsRole);

	MasterResponse createOrUpdateSetPointsForSaving(List<SetPointsForSaving> setPointsForSaving);

	MasterResponse addOrupdateDocument(MasterDocument document);

	MasterResponse getPointsForSavingBySavingOptionsRoleId(Integer savingOptionsRoleId, Integer savingOptionsId,
			Integer roleId);

	MasterResponse removeSavingOptionsRole(SavingOptionsRole savingOptionsRole);

	MasterResponse removeSetPointsForSaving(SetPointsForSaving setPointsForSaving);

	MasterResponse deleteDocument(MasterDocument document);

	MasterResponse getActivityList();

	MasterResponse getBranchListNew(Integer orgId);

	MasterResponse getBranchDetails(Integer branchId);

	MasterResponse getLanguageList();

	MasterResponse deleteEscalator(RoleEscalationSetup request);

	MasterResponse saveRewards(Rewards rewards);

	MasterResponse getRewardsList(Integer branchId);

	MasterResponse updateRewards(Rewards rewards);

	MasterResponse deleteRewards(Rewards rewards);

	MasterResponse saveVendorDetails(Vendor vendor);

	MasterResponse updateVendorDetails(Vendor ven);

	MasterResponse deleteVendorDetails(Vendor vendor);

	MasterResponse getVendorDetails(Integer branchId);

	MasterResponse addRewardCategory(RewardCategoryMaster mrc);

	MasterResponse addRewardResponsibleData(RewardResponsibleData rrd);

	MasterResponse updateRewardResponsibleData(RewardResponsibleData rrd);

	MasterResponse deleteRewardResponsibleData(RewardResponsibleData rrd);

	MasterResponse addToCart(Cart cart);

	MasterResponse getCartList(Integer empId);

	MasterResponse deleteFromCart(Cart cart);

	MasterResponse redeemGiftFromCart(List<RedeemCartRewards> redeemGiftList);

	MasterResponse saveRewardResponsibleSetup(RewardResponsibleSetup rrsetup);

	MasterResponse updateRewardResponsibleSetup(RewardResponsibleSetup rrsetup);

	MasterResponse deleteRewardResponsibleSetup(RewardResponsibleSetup rrsetup);

	MasterResponse getRewardResponsibleSetup(Integer branchId);

	MasterResponse addThought(ThoughtOfDay th);

	MasterResponse getThought(int branchId);

	MasterResponse addMoodIndicator(MoodIndicator mo);

	MasterResponse getSocialMediaList();

	MasterResponse addRedeemStatus(RedeemStatus status);

	MasterResponse updateRedeemStatus(RedeemStatus status);

	MasterResponse getRedeemStatus();

	MasterResponse updateRedeemFromCartReward(RedeemCartRewards redeemfrmcart);

	MasterResponse getRedeemFromCartRewardList(Integer empId);

	MasterResponse updateCartDetails(Cart cart);

	MasterResponse getRewardCategoryList(Integer orgId);

	MasterResponse getThresholdList();

	MasterResponse updateMoodIndicator(MoodIndicator mo);

	/* Incident Management Setup API's */

	MasterResponse addIncidentOfficer(IncidentOfficerDetails inc);

	MasterResponse updateIncidentOfficer(IncidentOfficerDetails inc);

	MasterResponse getIncidentOfficerDetails(Integer branchId);

	MasterResponse getManagerTypeList();

	MasterResponse addMachineDetails(MachineDetails mach);

	MasterResponse updateMachineDetails(MachineDetails mach);

	MasterResponse deleteMachineDetails(MachineDetails mach);

	MasterResponse getMachineList(Integer branchId);

	//
	MasterResponse getIncidentReport();

	MasterResponse getIncidentPriority();

	MasterResponse getIncidentSeverity();

	MasterResponse getIncidentNature();

	MasterResponse getIncidentManagerList(Integer branchId);

	MasterResponse getLineNameList(Integer deptId);

	MasterResponse addLineName(Line line);

	MasterResponse getShiftList(Shifts req);

	MasterResponse addOrUpdateZone(MasterZone zone);

	MasterResponse addOrUpdateSection(Section section);

	MasterResponse getCategoryListByBranchId(Integer branchId);

	MasterResponse addMachineParts(MachineParts parts);

	MasterResponse getMachinePartsList(Integer machId, Integer branchId);

	MasterResponse deleteMachineParts(Long id);

	MasterResponse getMachineListByLine(MachineDetails macDet);

	MasterResponse saveSugOnHoldSetup(SuggestionOnHoldSetup onHold);

	MasterResponse deleteSugOnHoldSetup(SuggestionOnHoldSetup onHold);

	MasterResponse savePasswordPolicy(PasswordPolicy policy);

	MasterResponse updatePasswordPolicy(PasswordPolicy policy);

	MasterResponse getPasswordPolicyByOrgId(int orgId);

	MasterResponse deletePasswordPolicy(long id);

	MasterResponse addorupdateSuggestionTemplate(SuggestionTemplate request);

	MasterResponse enableDisablePasswordPolicy(PasswordPolicy policy);

	MasterResponse suggestionTemplateMasterList();

	MasterResponse deleteSuggestionTemplate(Long id);

	MasterResponse saveSuggestionJurySetup(List<SuggestionEvalJurySetup> request);

	MasterResponse updateSuggestionJurySetup(SuggestionEvalJurySetup request);

	MasterResponse deleteSuggestionJurySetup(int id);

	MasterResponse getRegistrationSourceList();

	MasterResponse getSuggestionKaizenParameters();

	MasterResponse getSuggestionKaizenRatings(int branchId);

	MasterResponse getSuggestionMasterAssesment();

	MasterResponse getSuggestionAssesmentInputByBranchId(Integer branchId);

	MasterResponse saveSuggestionAssessmentInput(SuggestionEvalAssessmentInput request);

	MasterResponse updateSuggestionAssessmentInput(SuggestionEvalAssessmentInput request);

	MasterResponse getSugEvalMasterNomination(int branchId);

	MasterResponse addSugEvalAuthorityPhead(SuggestionEvalAuthoritySetup request);

	MasterResponse deleteSugEvalAuthority(int id);

	MasterResponse addSugEvalAuthorityHR(SuggestionEvalAuthoritySetup request);

	MasterResponse updateRewardCategory(RewardCategoryMaster rcm);

	MasterResponse deleteRewardCategory(int id);

	MasterResponse saveRewardAdminSetup(RewardAdminSetup request);

	MasterResponse deleteRewardAdminSetup(RewardAdminSetup request);

	MasterResponse getRewardAdminList(int branchId);

	MasterResponse getRewardAdminListByEmpId(int empId);

	MasterResponse getRedemptionTrackerList(Integer branchId);

	MasterResponse addOrUpdateSuggConfig(SuggestionFlowConfig config);

	MasterResponse getSuggConfigList(Integer orgId);

	MasterResponse getOrgDataByAlieas(String alieas);

	MasterResponse removeLineDetails(Line line);

	MasterResponse updateOptionalJurySetup(SuggestionEvalJurySetup request);

	MasterResponse saveSuggestionUserSetup(SuggestionUserSetup request);

	MasterResponse deleteSuggestionUserSetup(SuggestionUserSetup request);

	MasterResponse getSuggUserTypes();

	MasterResponse addUpdateParticularsPoints(ParticularsPoint particulars);

	MasterResponse getMasterUnits();

	MasterResponse employeeWiseHappinessIndex(MasterDTO request);

	/**
	 * @author rakesh 12 August
	 */
	MasterResponse addOrUpdateCategoryList(MasterDTO request);
	
	MasterResponse addCWAgencyDetails(CWAgency request);

	MasterResponse getCWAgencyList(Integer orgId);

	MasterResponse addDocument(MasterDocument document);

	MasterResponse updateDocument(MasterDocument document);

	MasterResponse removeDocument(MasterDocument document);

}
