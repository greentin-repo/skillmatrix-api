package com.greentin.enovation.master;

import java.util.HashMap;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

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
import com.greentin.enovation.utils.EnovationConstants;

@RestController
@CrossOrigin
public class MasterController {

	@Autowired
	IMasterService iMasterService;

	@Autowired
	PasswordEncoder pass;

	@CrossOrigin
	@GetMapping(value = "/test")
	public HashMap<String, Object> getempList() {
		HashMap<String, Object> status = new HashMap<String, Object>();
		String password = pass.encode("1234");
		// status.put(EnovationConstants.status,password);
		// boolean
		// matches(pass.encode("akshay"),"$2a$10$WvJnmvy3bMGU6OwdlVL7JeLKqu1fjOoO0rTgLqM7hZNJyX.G0FRp.");
		if (pass.matches("1234", "$2a$10$FGKbbKrdwwBGcpj.wnyTIOQNKMmQlknKILOeLz.XKzESocnNz4Dmi")) {
			status.put(EnovationConstants.status, password);
		} else {
			System.out.println("so :" + password.toString());
			status.put(EnovationConstants.status, "fail");
		}
		/*
		 * JSONObject js=new JSONObject();
		 * 
		 * FCMNotificationUtil.sendSingleNotification(
		 * "df7K3X7IxNQ:APA91bHuclIV0S-XH2sIwpDS66iIgJ9fdebkasG7uqMQ8tD_szvfIAFVLb5Cfsc5_gNzb3DP4M0p4ygqqJ_QQOqQd4-ajEwqFt-sVFeJMZvYD_qHISslPIuvjgnqxGzqwzkjyEIqjKI4",
		 * "Hello Shashi", "fcc",js);
		 */return status;
	}

	@CrossOrigin
	@PostMapping(value = "/updatedepartment")
	public MasterResponse addUpdateDepartment(@RequestBody DepartmentMaster dept) {
		return iMasterService.addDepartmentService(dept);
	}

	@CrossOrigin
	@PostMapping(value = "/adddepartment")
	public MasterResponse addDepartment(@RequestBody DepartmentMaster dept) {
		return iMasterService.addDepartment(dept);
	}

	@CrossOrigin
	@PostMapping(value = "/addOrganization")
	public MasterResponse addOrganization(@RequestBody OrganizationMaster org) {
		return iMasterService.addOrganizationService(org);
	}

	@CrossOrigin
	@PostMapping(value = "/addorUpdateCategory")
	public MasterResponse addCategory(@RequestBody CategoryMaster cat) {
		return iMasterService.addCategoryService(cat);
	}

	@CrossOrigin
	@PostMapping(value = "/updatecategory")
	public MasterResponse updateCategory(@RequestBody CategoryMaster cat) {
		return iMasterService.updateCategory(cat);
	}

	@CrossOrigin
	@GetMapping(value = "/getDepartmentList") // depricated
	public MasterResponse getdeptList() {
		return iMasterService.getdeptListService();
	}

	@CrossOrigin
	@GetMapping(value = "/getdepartmentlistbyorgid/{orgId}") // depricated
	public MasterResponse getdepartmentlistbyorgid(@PathVariable("orgId") Integer orgId) {
		return iMasterService.getdepartmentlistbyorgid(orgId);
	}

	@CrossOrigin
	@GetMapping(value = "/getbenefitanalysislist/{orgId}/{branchId}")
	public MasterResponse getBenefitAnalysisList(@PathVariable(name = "orgId") Integer orgId,
			@PathVariable(name = "branchId") Integer branchId) {
		if (branchId == null) {
			branchId = 0;
		}
		return iMasterService.getBenefitAnalysisList(orgId, branchId);
	}

	@CrossOrigin
	@GetMapping(value = "/getCategoryList") // depricated
	public MasterResponse getCatagoryList() {
		return iMasterService.getCatagoryListService();
	}

	@PostMapping(value = "/addorUpdateTeamType")
	public MasterResponse addTeamType(@RequestBody TeamtypeMaster teamType) {
		return iMasterService.addTeamTypeService(teamType);
	}

	@CrossOrigin
	@GetMapping(value = "/getrolelist")
	public MasterResponse getRoleList() {
		return iMasterService.getRoleList();
	}

	@CrossOrigin
	@GetMapping(value = "/getTeamTypeList") // depricated
	public MasterResponse getTeamTypeList() {
		return iMasterService.getTeamTypeListService();
	}

	@CrossOrigin
	@GetMapping(value = "/getStatusMasterList") // depricated
	public MasterResponse getStatusMasterList() {
		return iMasterService.getStatusMasterListService();
	}

	@CrossOrigin
	@GetMapping(value = "/getOrgMasterList") // depricated
	public MasterResponse getOrgMasterList() {
		return iMasterService.getOrgMasterListService();
	}

	@CrossOrigin
	@GetMapping(value = "/getpointlevellistbyorgid/{orgid}") // depricated
	public MasterResponse getPointLevelList(@PathVariable(name = "orgid") Integer orgId) {
		return iMasterService.getPointLevelListService(orgId);
	}

	@CrossOrigin
	@PostMapping(value = "/deleteOrganization")
	public MasterResponse deleteOrganization(@RequestBody OrganizationMaster organ) {
		return iMasterService.deleteOrganization(organ);
	}

	/*
	 * @CrossOrigin
	 * 
	 * @GetMapping(value ="/getDesignationList") public MasterResponse
	 * getDesignationList(){ return iMasterService.getDesignationListService(); }
	 */

	@CrossOrigin
	@GetMapping(value = "/getbranchlistbyorgid/{orgId}")
	public MasterResponse getbranchlist(@PathVariable("orgId") Integer orgId) {
		if (orgId == null) {
			orgId = 0;
		}
		return iMasterService.getbranchlistService(orgId);
	}

	@CrossOrigin
	@PostMapping(value = "/addbranch")
	public MasterResponse addbranch(@RequestBody BranchMaster brnch) {
		return iMasterService.addbranch(brnch);
	}

	@CrossOrigin
	@PostMapping(value = "/updatebranch")
	public MasterResponse updateBranch(@RequestBody BranchMaster brnch) {
		return iMasterService.updateBranch(brnch);
	}

	/*
	 * @CrossOrigin
	 * 
	 * @PostMapping(value ="/adddesignation") public MasterResponse
	 * addDesignation(@RequestBody DesignationMaster desig){ return
	 * iMasterService.addDesignation(desig); }
	 */

	@CrossOrigin
	@PostMapping(value = "/addbenefitmasternew")
	public MasterResponse addbenefitmaster(@RequestBody MasterResponse request) {
		return iMasterService.addbenefitmasternew(request.getNewBenefits(), request.getOldBenefits());
	}

	@CrossOrigin
	@PostMapping(value = "/addbenefitmaster")
	public MasterResponse addbenefitmaster(@RequestBody BenefitAnalysisMaster benefit) {
		return iMasterService.addbenefitmaster(benefit);
	}

	@CrossOrigin
	@PostMapping(value = "/saveexecutive")
	public MasterResponse saveExecutive(@RequestBody Executive executive) {
		return iMasterService.saveExecutive(executive);
	}

	@CrossOrigin
	@PostMapping(value = "/updateexecutive")
	public MasterResponse updateExecutive(@RequestBody Executive executive) {
		return iMasterService.updateExecutive(executive);
	}

	@CrossOrigin
	@PostMapping(value = "/updatedeptlvlexecutive")
	public MasterResponse updateDeptlvlExecutive(@RequestBody DepartmentLevel deptexecutive) {
		return iMasterService.updateDeptlvlExecutive(deptexecutive);
	}

	@CrossOrigin
	@GetMapping(value = "/removeexecutive/{id}")
	public MasterResponse removeExecutive(@PathVariable(name = "id") Integer execId) {
		return iMasterService.removeExecutive(execId);
	}

	@CrossOrigin
	@GetMapping(value = "/getlistOfexecutivebyorgidapp/{orgId}")
	public MasterResponse getListOfExecutiveByOrgId(
			@PathVariable(name = "orgId") Integer orgId/* ,@PathVariable(name="branchId") Integer branchId */) {
		if (orgId == null) {
			orgId = 0;
		}
		// if(branchId == null) { branchId = 0;}
		return iMasterService.getListOfExecutiveByOrgId(orgId);
	}

	@CrossOrigin
	@GetMapping(value = "/getdepartmentlistbybranchid/{branchId}")
	public MasterResponse getdepartmentlistbybranchid(@PathVariable("branchId") Integer branchId) {
		if (branchId == null) {
			branchId = 0;
		}
		return iMasterService.getdepartmentlistbybranchid(branchId);
	}

	/*
	 * @CrossOrigin
	 * 
	 * @GetMapping(value ="/getdesignationlistbyorgid/{orgId}") public
	 * MasterResponse getDesignationListbyorgId(@PathVariable("orgId") Integer orgId
	 * ){ return iMasterService.getDesignationListbyorgId(orgId); }
	 */

	/*
	 * @CrossOrigin
	 * 
	 * @GetMapping(value ="/getdesignationlistbybranch/{branchId}") public
	 * MasterResponse getDesignationListbyBranch(@PathVariable("branchId") Integer
	 * branchId ){ return iMasterService.getDesignationListbyBranch(branchId); }
	 */

	@CrossOrigin
	@GetMapping(value = "/getcategorylistbyorgid/{orgId}/{branchId}")
	public MasterResponse getCategoryListbyorgId(@PathVariable("orgId") Integer orgId,
			@PathVariable("branchId") Integer branchId) {
		if (orgId == null) {
			orgId = 0;
		}
		if (branchId == null) {
			branchId = 0;
		}
		return iMasterService.getCategoryListbyorgId(orgId, branchId);
	}

	/*
	 * @CrossOrigin
	 * 
	 * @GetMapping(value ="/getdesignationlistbybranchid/{branchId}") public
	 * MasterResponse getDesignationListbybranchId(@PathVariable("branchId") Integer
	 * branchId ){ return iMasterService.getDesignationListbybranchId(branchId); }
	 */

	@CrossOrigin
	@PostMapping(value = "/savelevelwiseusers")
	public MasterResponse saveLevelWiseUsers(@RequestBody EmployeeDetailsBean request) {
		return iMasterService.saveLevelWiseUsers(request.getDepartmentLevel());
	}

	@CrossOrigin
	@PostMapping(value = "/updatelevelwiseusers")
	public MasterResponse updateLevelWiseUsers(@RequestBody EmployeeDetailsBean request) {
		return iMasterService.updateLevelWiseUsers(request);
	}

	// retrive records from dept level table(Escalation flow)
	@CrossOrigin
	@GetMapping(value = "/getlistofusersbranchwise/{branchId}/{levelId}/{deptId}/{roleId}")
	public MasterResponse getListofUsersBranchWise(@PathVariable("branchId") Integer branchId,
			@PathVariable("levelId") Integer levelId, @PathVariable("deptId") Integer deptId,
			@PathVariable("roleId") Integer roleId) {
		if (deptId == null) {
			deptId = 0;
		}
		if (branchId == null) {
			branchId = 0;
		}
		if (levelId == null) {
			levelId = 0;
		}
		if (roleId == null) {
			roleId = 0;
		}
		return iMasterService.getListofEmployeebyBranchWise(branchId, levelId, deptId, roleId);
	}

	@CrossOrigin
	@PostMapping(value = "/uploadoranizationlogo")
	public MasterResponse uploadOranizationLogo(@ModelAttribute OrganizationMaster org) {
		return iMasterService.uploadOranizationLogo(org);
	}

	@CrossOrigin
	@PostMapping(value = "/savestatuspoints")
	public MasterResponse saveStatusPoints(@RequestBody OrgStatusPoints orgsts) {
		return iMasterService.saveStatusPoints(orgsts);
	}

	@CrossOrigin
	@PostMapping(value = "/updatestatuspoints")
	public MasterResponse updateStatusPoints(@RequestBody OrgStatusPoints orgsts) {
		return iMasterService.updateStatusPoints(orgsts);
	}

	@CrossOrigin
	@GetMapping(value = "/getlistofstatuspoints/{branchId}")
	public MasterResponse getListofStatusPoints(@PathVariable("branchId") Integer branchId) {
		if (branchId == null) {
			branchId = 0;
		}
		return iMasterService.getListofStatusPoints(branchId);
	}

	@CrossOrigin
	@GetMapping(value = "/getlistofstatusforbrowniepoints/{roleId}")
	public MasterResponse getListofStatusForBrowniePoints(@PathVariable("roleId") Integer roleId) {
		if (roleId == null) {
			roleId = 0;
		}
		return iMasterService.getListofStatusForBrowniePoints(roleId);
	}

	@CrossOrigin
	@GetMapping(value = "/getcounterylist")
	public MasterResponse getCounteriesList() {
		return iMasterService.getCounteriesList();
	}

	@CrossOrigin
	@PostMapping(value = "/removebenifits")
	public MasterResponse deleteBenifits(@RequestBody BenefitAnalysisMaster organ) {
		return iMasterService.deleteBenefit(organ);
	}

	@CrossOrigin
	@PostMapping(value = "/removedepartment")
	public MasterResponse deleteDepartment(@RequestBody DepartmentMaster dept) {
		return iMasterService.deleteDepartment(dept);
	}

	@CrossOrigin
	@PostMapping(value = "/removecategory")
	public MasterResponse deleteCategory(@RequestBody CategoryMaster cate) {
		return iMasterService.deleteCategory(cate);
	}

	@CrossOrigin
	@PostMapping(value = "/disableexecutive") // Deprecated
	public MasterResponse disable(@RequestBody Executive cate) {
		return iMasterService.disableExecutive(cate);
	}

	@CrossOrigin
	@PostMapping(value = "/removeexecutive")
	public MasterResponse removeExecutive(@RequestBody Executive exe) {
		return iMasterService.deleteExecutive(exe);
	}

	@CrossOrigin
	@PostMapping(value = "/removebrowinepoints")
	public MasterResponse removeBrowinePoints(@RequestBody OrgStatusPoints orgstatuspoints) {
		return iMasterService.removeBrowinePoints(orgstatuspoints);
	}

	@CrossOrigin
	@PostMapping(value = "/updateisescalation")
	public MasterResponse updateIsEscalation(@RequestBody ProductOrgConfig request) {
		return iMasterService.updateIsEscalation(request);
	}

	@CrossOrigin
	@PostMapping(value = "/saveroleescalationsetup")
	public MasterResponse saveRoleEscalationSetup(@RequestBody MasterResponse request) {
		return iMasterService.saveRoleEscalationSetup(request);
	}

	@CrossOrigin
	@PostMapping(value = "/updateroleescalationsetup")
	public MasterResponse updateRoleEscalationSetup(@RequestBody RoleEscalationSetup request) {
		return iMasterService.updateRoleEscalationSetup(request);
	}

	@CrossOrigin
	@PostMapping(value = "/removeroleescalationsetup")
	public MasterResponse removeRoleEscalationSetup(@RequestBody RoleEscalationSetup request) {
		return iMasterService.removeRoleEscalationSetup(request);
	}

	@CrossOrigin
	@PostMapping(value = "/deleteescalator")
	public MasterResponse deleteescalator(@RequestBody RoleEscalationSetup request) {
		return iMasterService.deleteEscalator(request);
	}

	@CrossOrigin
	@GetMapping(value = "/getroleescalationsetup/{branchId}")
	public MasterResponse getRoleEscalationSetup(@PathVariable Integer branchId) {
		if (branchId == null) {
			branchId = 0;
		}
		return iMasterService.getRoleEscalationSetup(branchId);
	}

	@CrossOrigin
	@PostMapping(value = "/updatesuggestionescalationconfig")
	public MasterResponse updateSuggestionEscalationConfig(@RequestBody MasterResponse request) {
		return iMasterService.updateSuggestionEscalationConfig(request);
	}

	@CrossOrigin
	@PostMapping(value = "/removesuggestionescalationconfig")
	public MasterResponse removeSuggestionEscalationConfig(@RequestBody SuggestionEscalationConfig request) {
		return iMasterService.removeSuggestionEscalationConfig(request);
	}

	@CrossOrigin
	@GetMapping(value = "/getsuggestionescalationconfig/{branchId}")
	public MasterResponse getSuggestionEscalationConfig(@PathVariable Integer branchId) {
		if (branchId == null) {
			branchId = 0;
		}
		return iMasterService.getSuggestionEscalationConfig(branchId);
	}

	@CrossOrigin
	@GetMapping(value = "/sendEmailsOnRegistration/{branchId}")
	public MasterResponse sendEmailsOnRegistration(@PathVariable Integer branchId) {
		if (branchId == null) {
			branchId = 0;
		}
		return iMasterService.sendEmailsOnRegistration(branchId);
	}

	@CrossOrigin
	@GetMapping(value = "/getorglogo/{alies}")
	public MasterResponse getOrgLogo(@PathVariable String alies) {
		return iMasterService.getOrgLogo(alies);
	}

	@CrossOrigin
	@GetMapping(value = "/getDepartmentListForSuggestion/{branchId}")
	public MasterResponse getDepartmentListForSuggestion(@PathVariable(name = "branchId") Integer branchId) {
		if (branchId == null) {
			branchId = 0;
		}
		return iMasterService.getDepartmentListForSuggestion(branchId);
	}

	@CrossOrigin
	@GetMapping(value = "/getBrowniePointsReimbursementCycleTypeList")
	public MasterResponse getBrowniePointsReimbursementCycleTypeList() {
		return iMasterService.getBrowniePointsReimbursementCycleTypeList();
	}

	@CrossOrigin
	@GetMapping(value = "/getBrowniePointsReimbursementCycle/{branchId}")
	public MasterResponse getBrowniePointsReimbursementCycle(@PathVariable(name = "branchId") Integer branchId) {
		if (branchId == null) {
			branchId = 0;
		}
		return iMasterService.getBrowniePointsReimbursementCycle(branchId);
	}

	@CrossOrigin
	@PostMapping(value = "/updateBrowniePointsReimbursementCycle")
	public MasterResponse updateBrowniePointsReimbursementCycle(@RequestBody BrowniePointsReimbursementCycle request) {
		return iMasterService.updateBrowniePointsReimbursementCycle(request);
	}

	@CrossOrigin
	@GetMapping(value = "/getParticulars/{catId}")
	public MasterResponse getParticulars(@PathVariable(name = "catId") Integer catId) {
		if (catId == null) {
			catId = 0;
		}
		return iMasterService.getParticulars(catId);
	}

	@CrossOrigin
	@PostMapping(value = "/updateParticulars")
	public MasterResponse updateParticulars(@RequestBody List<Particulars> request) {
		return iMasterService.updateParticulars(request);
	}

	@CrossOrigin
	@PostMapping(value = "/removeParticulars")
	public MasterResponse removeParticulars(@RequestBody Particulars particulars) {
		return iMasterService.removeParticulars(particulars);
	}

	@CrossOrigin
	@GetMapping(value = "/getSavingOptionByBranchId/{branchId}")
	public MasterResponse getSavingOptionByBranchId(@PathVariable(name = "branchId") Integer branchId) {
		if (branchId == null) {
			branchId = 0;
		}
		return iMasterService.getSavingOptionByBranchId(branchId);
	}

	@CrossOrigin
	@GetMapping(value = "/getSavingOptionRole/{savingOptionsId}")
	public MasterResponse getSavingOptionRole(@PathVariable(name = "savingOptionsId") Integer savingOptionsId) {
		if (savingOptionsId == null) {
			savingOptionsId = 0;
		}
		return iMasterService.getSavingOptionRole(savingOptionsId);
	}

	@CrossOrigin
	@GetMapping(value = "/getCurrencyList")
	public MasterResponse getCurrencyList() {
		return iMasterService.getCurrencyList();
	}

	@CrossOrigin
	@GetMapping(value = "/getPointsForSaving/{branchId}")
	public MasterResponse getPointsForSaving(@PathVariable(name = "branchId") Integer branchId) {
		if (branchId == null) {
			branchId = 0;
		}
		return iMasterService.getPointsForSaving(branchId);
	}

	@CrossOrigin
	@GetMapping(value = "/getPointsForSavingBySavingOptionsId/{savingOptionsId}/{roleId}") // depricated
	public MasterResponse getPointsForSavingBySavingOptionsId(
			@PathVariable(name = "savingOptionsId") Integer savingOptionsId,
			@PathVariable(name = "roleId") Integer roleId) {
		if (savingOptionsId == null) {
			savingOptionsId = 0;
		}
		if (roleId == null) {
			roleId = 0;
		}
		return iMasterService.getPointsForSavingBySavingOptionsId(savingOptionsId, roleId);
	}

	@CrossOrigin
	@PostMapping(value = "/createOrUpdateSavingOptions")
	public MasterResponse createOrUpdateSavingOptions(@RequestBody SavingOptions savingOptions) {
		return iMasterService.createOrUpdateSavingOptions(savingOptions);
	}

	@CrossOrigin
	@PostMapping(value = "/createOrUpdateSavingOptionsRole")
	public MasterResponse createOrUpdateSavingOptionsRole(@RequestBody SavingOptionsRole savingOptionsRole) {
		return iMasterService.createOrUpdateSavingOptionsRole(savingOptionsRole);
	}

	@CrossOrigin
	@PostMapping(value = "/createOrUpdateSetPointsForSaving")
	public MasterResponse createOrUpdateSetPointsForSaving(@RequestBody List<SetPointsForSaving> setPointsForSaving) {
		return iMasterService.createOrUpdateSetPointsForSaving(setPointsForSaving);
	}

	@CrossOrigin
	@PostMapping(value = "/removeSavingOptionsRole")
	public MasterResponse removeSavingOptionsRole(@RequestBody SavingOptionsRole savingOptionsRole) {
		return iMasterService.removeSavingOptionsRole(savingOptionsRole);
	}

	@CrossOrigin
	@PostMapping(value = "/removeSetPointsForSaving")
	public MasterResponse removeSetPointsForSaving(@RequestBody SetPointsForSaving setPointsForSaving) {
		return iMasterService.removeSetPointsForSaving(setPointsForSaving);
	}

	@CrossOrigin
	@PostMapping(value = "/addorupdatedocument")
	public MasterResponse addOrupdateDocument(@RequestBody MasterDocument document) {
		return iMasterService.addOrupdateDocument(document);
	}

	@CrossOrigin
	@PostMapping(value = "/deletedocument")
	public MasterResponse deleteDocument(@RequestBody MasterDocument document) {
		return iMasterService.deleteDocument(document);
	}

	@CrossOrigin
	@GetMapping(value = "/getPointsForSavingBySavingOptionsRoleId/{savingOptionsRoleId}/{savingOptionsId}/{roleId}") /// {roleId}
	public MasterResponse getPointsForSavingBySavingOptionsRoleId(
			@PathVariable(name = "savingOptionsRoleId") Integer savingOptionsRoleId,
			@PathVariable(name = "savingOptionsId") Integer savingOptionsId,
			@PathVariable(name = "roleId") Integer roleId) {
		if (savingOptionsRoleId == null) {
			savingOptionsRoleId = 0;
		}
		if (savingOptionsId == null) {
			savingOptionsId = 0;
		}
		if (roleId == null) {
			roleId = 0;
		}
		return iMasterService.getPointsForSavingBySavingOptionsRoleId(savingOptionsRoleId, savingOptionsId, roleId);
	}

	@CrossOrigin
	@GetMapping(value = "/getActivityList")
	public MasterResponse getActivityList() {
		return iMasterService.getActivityList();
	}

	@CrossOrigin
	@GetMapping(value = "/getBranchListNew/{orgId}")
	public MasterResponse getBranchListNew(@PathVariable("orgId") Integer orgId) {
		if (orgId == null) {
			orgId = 0;
		}
		return iMasterService.getBranchListNew(orgId);

	}

	@CrossOrigin
	@GetMapping(value = "/getBranchDetailsNew/{branchId}")
	public MasterResponse getBranchDetails(@PathVariable("branchId") Integer branchId) {
		if (branchId == null) {
			branchId = 0;
		}
		return iMasterService.getBranchDetails(branchId);
	}

	@CrossOrigin
	@GetMapping(value = "/getlanguagelist")
	public MasterResponse getLanguageList() {
		return iMasterService.getLanguageList();
	}

	@CrossOrigin
	@PostMapping(value = "/saveRewards")
	public MasterResponse saveRewards(@ModelAttribute Rewards rewards) {
		return iMasterService.saveRewards(rewards);
	}

	@CrossOrigin
	@GetMapping(value = "/getRewardsList/{branchId}")
	public MasterResponse getRewardsList(@PathVariable("branchId") Integer branchId) {
		return iMasterService.getRewardsList(branchId);
	}

	@CrossOrigin
	@PostMapping(value = "/updateRewards")
	public MasterResponse updateRewards(@ModelAttribute Rewards rewards) {
		return iMasterService.updateRewards(rewards);
	}

	@CrossOrigin
	@PostMapping(value = "/deleteRewards")
	public MasterResponse deleteRewards(@RequestBody Rewards rewards) {
		return iMasterService.deleteRewards(rewards);
	}

	@CrossOrigin
	@PostMapping(value = "/saveVendorDetails")
	public MasterResponse saveVendorDetails(@RequestBody Vendor vendor) {
		return iMasterService.saveVendorDetails(vendor);
	}

	@CrossOrigin
	@PostMapping(value = "/updateVendorDetails")
	public MasterResponse updateVendorDetails(@RequestBody Vendor vendor) {
		return iMasterService.updateVendorDetails(vendor);
	}

	@CrossOrigin
	@PostMapping(value = "/deleteVendorDetails")
	public MasterResponse deleteVendorDetails(@RequestBody Vendor vendor) {
		return iMasterService.deleteVendorDetails(vendor);
	}

	@CrossOrigin
	@GetMapping(value = "/getVendorDetails/{branchId}")
	public MasterResponse getVendorDetails(@PathVariable("branchId") Integer branchId) {
		return iMasterService.getVendorDetails(branchId);
	}

	@CrossOrigin
	@PostMapping(value = "/addRewardCategory")
	public MasterResponse addRewardCategory(@RequestBody RewardCategoryMaster mrc) {
		return iMasterService.addRewardCategory(mrc);
	}

	@CrossOrigin
	@PostMapping(value = "/addRewardResponsibleData")
	public MasterResponse addRewardResponsibleData(@RequestBody RewardResponsibleData rrd) {
		return iMasterService.addRewardResponsibleData(rrd);
	}

	@CrossOrigin
	@PostMapping(value = "/updateRewardResponsibleData")
	public MasterResponse updateRewardResponsibleData(@RequestBody RewardResponsibleData rrd) {
		return iMasterService.updateRewardResponsibleData(rrd);
	}

	@CrossOrigin
	@PostMapping(value = "/deleteRewardResponsibleData")
	public MasterResponse deleteRewardResponsibleData(@RequestBody RewardResponsibleData rrd) {
		return iMasterService.deleteRewardResponsibleData(rrd);
	}

	@CrossOrigin
	@PostMapping(value = "/addThought")
	public MasterResponse addThought(@RequestBody ThoughtOfDay th) {
		return iMasterService.addThought(th);
	}

	@CrossOrigin
	@GetMapping(value = "/getThought/{branchId}")
	public MasterResponse getThought(@PathVariable(name = "branchId") int branchId) {
		return iMasterService.getThought(branchId);
	}

	@CrossOrigin
	@PostMapping(value = "/addMoodIndicator")
	public MasterResponse addMoodIndicator(@RequestBody MoodIndicator mo) {
		return iMasterService.addMoodIndicator(mo);
	}

	@CrossOrigin
	@PostMapping(value = "/updateMoodIndicator")
	public MasterResponse updateMoodIndicator(@RequestBody MoodIndicator mo) {
		return iMasterService.updateMoodIndicator(mo);
	}

	@CrossOrigin
	@PostMapping(value = "/addToCart")
	public MasterResponse addToCart(@RequestBody Cart cart) {
		return iMasterService.addToCart(cart);
	}

	@CrossOrigin
	@GetMapping(value = "/getCartList/{empId}")
	public MasterResponse getCartList(@PathVariable("empId") Integer empId) {
		return iMasterService.getCartList(empId);
	}

	@CrossOrigin
	@PostMapping(value = "/deleteFromCart")
	public MasterResponse deleteFromCart(@RequestBody Cart cart) {
		return iMasterService.deleteFromCart(cart);
	}

	@CrossOrigin
	@PostMapping(value = "/redeemGiftFromCart")
	public MasterResponse redeemGiftFromCart(@RequestBody List<RedeemCartRewards> redeemGiftList) {
		return iMasterService.redeemGiftFromCart(redeemGiftList);
	}

	@CrossOrigin
	@PostMapping(value = "/saveRewardResponsibleSetup")
	public MasterResponse saveRewardResponsibleSetup(@RequestBody RewardResponsibleSetup rrsetup) {
		return iMasterService.saveRewardResponsibleSetup(rrsetup);
	}

	@CrossOrigin
	@PostMapping(value = "/updateRewardResponsibleSetup")
	public MasterResponse updateRewardResponsibleSetup(@RequestBody RewardResponsibleSetup rrsetup) {
		return iMasterService.updateRewardResponsibleSetup(rrsetup);
	}

	@CrossOrigin
	@PostMapping(value = "/deleteRewardResponsibleSetup")
	public MasterResponse deleteRewardResponsibleSetup(@RequestBody RewardResponsibleSetup rrsetup) {
		return iMasterService.deleteRewardResponsibleSetup(rrsetup);
	}

	@CrossOrigin
	@GetMapping(value = "/getRewardResponsibleSetupList/{branchId}")
	public MasterResponse getRewardResponsibleSetupList(@PathVariable("branchId") Integer branchId) {
		return iMasterService.getRewardResponsibleSetup(branchId);
	}

	@CrossOrigin
	@PostMapping(value = "/addStatus")
	public MasterResponse addStatus(@RequestBody RedeemStatus status) {
		return iMasterService.addRedeemStatus(status);
	}

	@CrossOrigin
	@PostMapping(value = "/updateStatus")
	public MasterResponse updateStatus(@RequestBody RedeemStatus status) {
		return iMasterService.updateRedeemStatus(status);
	}

	@CrossOrigin
	@GetMapping(value = "/getStatusList")
	public MasterResponse getRedeemStatusList() {
		return iMasterService.getRedeemStatus();
	}

	@CrossOrigin
	@GetMapping(value = "/getRedeemFromCartList/{empId}")
	public MasterResponse getRedeemCartList(@PathVariable("empId") Integer empId) {
		return iMasterService.getRedeemFromCartRewardList(empId);
	}

	@CrossOrigin
	@PostMapping(value = "/updateRedeemFromCartStatus")
	public MasterResponse updateRedeemFromCartStatus(@RequestBody RedeemCartRewards redeemfrmcart) {
		return iMasterService.updateRedeemFromCartReward(redeemfrmcart);
	}

	@CrossOrigin
	@PostMapping(value = "/updateCartDetails")
	public MasterResponse updateCartDetails(@RequestBody Cart cart) {
		return iMasterService.updateCartDetails(cart);
	}

	@CrossOrigin
	@GetMapping(value = "/getSocialMediaList")
	public MasterResponse getSocialMediaList() {
		return iMasterService.getSocialMediaList();
	}

	@CrossOrigin
	@GetMapping(value = "/getRewardCategoryList/{orgId}")
	public MasterResponse getRewardCategoryList(@PathVariable("orgId") Integer orgId) {
		return iMasterService.getRewardCategoryList(orgId);
	}

	@GetMapping(value = "/getThresholdList")
	public MasterResponse getThresholdList() {
		return iMasterService.getThresholdList();
	}

	/* Incident Management Setup API's */

	@CrossOrigin
	@PostMapping(value = "/addIncidentOfficer")
	public MasterResponse addIncidentOfficer(@RequestBody IncidentOfficerDetails inc) {
		return iMasterService.addIncidentOfficer(inc);
	}

	@CrossOrigin
	@PostMapping(value = "/updateIncidentOfficer")
	public MasterResponse updateIncidentOfficer(@RequestBody IncidentOfficerDetails inc) {
		return iMasterService.updateIncidentOfficer(inc);
	}

	@CrossOrigin
	@GetMapping(value = "/getIncidentOfficerDetails/{branchId}")
	public MasterResponse getIncidentOfficerDetails(@PathVariable("branchId") Integer branchId) {
		return iMasterService.getIncidentOfficerDetails(branchId);
	}

	@CrossOrigin
	@GetMapping(value = "/getManagerTypeList")
	public MasterResponse getManagerTypeList() {
		return iMasterService.getManagerTypeList();
	}

	@CrossOrigin
	@PostMapping(value = "/addMachineDetails")
	public MasterResponse addMachineDetails(@ModelAttribute MachineDetails mach) {
		return iMasterService.addMachineDetails(mach);
	}

	@CrossOrigin
	@PostMapping(value = "/addMachineParts")
	public MasterResponse addMachineParts(@ModelAttribute MachineParts parts) {
		return iMasterService.addMachineParts(parts);
	}

	@CrossOrigin
	@PostMapping(value = "/updateMachineDetails")
	public MasterResponse updateMachineDetails(@ModelAttribute MachineDetails mach) {
		return iMasterService.updateMachineDetails(mach);
	}

	@CrossOrigin
	@PostMapping(value = "/deleteMachineDetails")
	public MasterResponse deleteMachineDetails(@RequestBody MachineDetails mach) {
		return iMasterService.deleteMachineDetails(mach);
	}

	@CrossOrigin
	@GetMapping(value = "/deleteMachineParts/{id}")
	public MasterResponse deleteMachineParts(@PathVariable(name = "id") Long id) {
		return iMasterService.deleteMachineParts(id);
	}

	@CrossOrigin
	@GetMapping(value = "/getMachineList/{branchId}")
	public MasterResponse getMachineList(@PathVariable("branchId") Integer branchId) {
		return iMasterService.getMachineList(branchId);
	}

	@PostMapping(value = "/getMachineListByLine")
	public MasterResponse getMachineListByLine(@RequestBody MachineDetails macDet) {
		return iMasterService.getMachineListByLine(macDet);
	}

	@CrossOrigin
	@GetMapping(value = "/getMachinePartsList/{machId}/{branchId}")
	public MasterResponse getMachinePartsList(@PathVariable("machId") Integer machId,
			@PathVariable("branchId") Integer branchId) {
		return iMasterService.getMachinePartsList(machId, branchId);
	}

	@CrossOrigin
	@GetMapping(value = "/getIncidentReport")
	public MasterResponse getIncidentReport() {
		return iMasterService.getIncidentReport();
	}

	@CrossOrigin
	@GetMapping(value = "/getIncidentNature")
	public MasterResponse getIncidentNature() {
		return iMasterService.getIncidentNature();
	}

	@CrossOrigin
	@GetMapping(value = "/getIncidentSeverity")
	public MasterResponse getIncidentSeverity() {
		return iMasterService.getIncidentSeverity();
	}

	@CrossOrigin
	@GetMapping(value = "/getIncidentPriority")
	public MasterResponse getIncidentPriority() {
		return iMasterService.getIncidentPriority();
	}

	@CrossOrigin
	@GetMapping(value = "/getIncidentManagerList/{branchId}")
	public MasterResponse getIncidentManagerList(@PathVariable("branchId") Integer branchId) {
		return iMasterService.getIncidentManagerList(branchId);
	}


	/*
	 * Audit Setup Ends
	 * 
	 */

	@GetMapping(value = "/getLineNameList/{deptId}")
	public MasterResponse getLineNameList(@PathVariable("deptId") Integer deptId) {
		return iMasterService.getLineNameList(deptId);
	}

	@PostMapping(value = "/addLineName")
	public MasterResponse addLineName(@RequestBody Line line) {
		return iMasterService.addLineName(line);
	}

	@CrossOrigin
	@PostMapping(value = "/getShiftList")
	public MasterResponse getShiftList(@RequestBody Shifts req) {
		return iMasterService.getShiftList(req);
	}

	@PostMapping(value = "/addOrUpdateZone")
	public MasterResponse addOrUpdateZone(@RequestBody MasterZone zone) {
		return iMasterService.addOrUpdateZone(zone);
	}

	@PostMapping(value = "/addOrUpdateSection")
	public MasterResponse addOrUpdateSection(@RequestBody Section section) {
		return iMasterService.addOrUpdateSection(section);
	}

	@GetMapping(value = "/getCategoryListByBranchId/{branchId}")
	public MasterResponse getCategoryListByBranchId(@PathVariable("branchId") Integer branchId) {
		return iMasterService.getCategoryListByBranchId(branchId);
	}

	@PostMapping(value = "/saveSugOnHoldSetup")
	public MasterResponse saveSugOnHoldSetup(@RequestBody SuggestionOnHoldSetup onHold) {
		return iMasterService.saveSugOnHoldSetup(onHold);
	}

	@PostMapping(value = "/deleteSugOnHoldSetup")
	public MasterResponse deleteSugOnHoldSetup(@RequestBody SuggestionOnHoldSetup onHold) {
		return iMasterService.deleteSugOnHoldSetup(onHold);
	}

	@PostMapping(value = "/savePasswordPolicy")
	public MasterResponse savePasswordPolicy(@RequestBody PasswordPolicy policy) {
		return iMasterService.savePasswordPolicy(policy);
	}

	@PostMapping(value = "/updatePasswordPolicy")
	public MasterResponse updatePasswordPolicy(@RequestBody PasswordPolicy policy) {
		return iMasterService.updatePasswordPolicy(policy);
	}

	@GetMapping(value = "/deletePasswordPolicy/{id}")
	public MasterResponse deletePasswordPloicy(@PathVariable("id") long id) {
		return iMasterService.deletePasswordPolicy(id);
	}

	@GetMapping(value = "/getPasswordPolicy/{orgId}")
	public MasterResponse getPasswordPolicyByOrgId(@PathVariable("orgId") Integer orgId) {
		return iMasterService.getPasswordPolicyByOrgId(orgId);
	}

	@PostMapping(value = "/enableDisablePasswordPolicy")
	public MasterResponse enableDisablePasswordPolicy(@RequestBody PasswordPolicy policy) {
		return iMasterService.enableDisablePasswordPolicy(policy);
	}

	// SUGGESTION JURY SETUP
	@PostMapping(value = "/saveSuggestionJurySetup")
	public MasterResponse saveSuggestionJurySetup(@RequestBody List<SuggestionEvalJurySetup> request) {
		return iMasterService.saveSuggestionJurySetup(request);
	}

	@PostMapping(value = "/updateSuggestionJurySetup")
	public MasterResponse updateSuggestionJurySetup(@RequestBody SuggestionEvalJurySetup request) {
		return iMasterService.updateSuggestionJurySetup(request);
	}

	@GetMapping(value = "/deleteSuggestionJurySetup/{id}")
	public MasterResponse deleteSuggestionJurySetup(@PathVariable("id") int id) {
		return iMasterService.deleteSuggestionJurySetup(id);
	}

	@PostMapping(value = "/addorupdateSuggestionTemplate")
	public MasterResponse addorupdateSuggestionTemplate(@RequestBody SuggestionTemplate request) {
		return iMasterService.addorupdateSuggestionTemplate(request);
	}

	@GetMapping(value = "/suggestionTemplateMasterList")
	public MasterResponse suggestionTemplateMasterList() {
		return iMasterService.suggestionTemplateMasterList();
	}

	@GetMapping(value = "/deleteSuggestionTemplate/{id}")
	public MasterResponse deleteSuggestionTemplate(@PathVariable("id") Long id) {
		return iMasterService.deleteSuggestionTemplate(id);
	}

	// ****************************************************************************************
	// DON'T REMOVE test from /getRegistrationSourceList API
	@GetMapping(value = "test/getRegistrationSourceList")
	public MasterResponse getRegistrationSourceList() {
		return iMasterService.getRegistrationSourceList();
	}
	// ****************************************************************************************

	/* For Suggestion Top n kaizen */

	@GetMapping(value = "/getSuggestionEvalParameters")
	public MasterResponse getSuggestionKaizenParameters() {
		return iMasterService.getSuggestionKaizenParameters();
	}

	@GetMapping(value = "/getSuggestionEvalRatings/{branchId}")
	public MasterResponse getSuggestionKaizenRatings(@PathVariable("branchId") Integer branchId) {
		return iMasterService.getSuggestionKaizenRatings(branchId);
	}

	@GetMapping(value = "/getSuggestionMasterAssesment")
	public MasterResponse getSuggestionMasterAssesment() {
		return iMasterService.getSuggestionMasterAssesment();
	}

	@GetMapping(value = "/getSuggestionAssesmentInputByBranchId/{branchId}")
	public MasterResponse getSuggestionAssesmentInput(@PathVariable("branchId") Integer branchId) {
		return iMasterService.getSuggestionAssesmentInputByBranchId(branchId);
	}

	@PostMapping(value = "/saveSugAssesmentSetup")
	public MasterResponse saveSuggestionAssessmentInput(@RequestBody SuggestionEvalAssessmentInput request) {
		return iMasterService.saveSuggestionAssessmentInput(request);
	}

	@PostMapping(value = "/updateSugAssesmentSetup")
	public MasterResponse updateSuggestionAssessmentInput(@RequestBody SuggestionEvalAssessmentInput request) {
		return iMasterService.updateSuggestionAssessmentInput(request);
	}

	@GetMapping(value = "/getSuggestionEvalMstrNomination/{branchId}")
	public MasterResponse getSuggestionEvalMstrNomination(@PathVariable("branchId") Integer branchId) {
		return iMasterService.getSugEvalMasterNomination(branchId);
	}

	@PostMapping(value = "/addSugAuthorityProductionHead")
	public MasterResponse addSugAuthorityPlantHead(@RequestBody SuggestionEvalAuthoritySetup request) {
		return iMasterService.addSugEvalAuthorityPhead(request);
	}

	@GetMapping(value = "/deleteSugAuthority/{id}")
	public MasterResponse deleteSugEvalAuthorityPhead(@PathVariable("id") Integer id) {
		return iMasterService.deleteSugEvalAuthority(id);
	}

	@PostMapping(value = "/addSugAuthorityHR")
	public MasterResponse addSugAuthorityHR(@RequestBody SuggestionEvalAuthoritySetup request) {
		return iMasterService.addSugEvalAuthorityHR(request);
	}

	@PostMapping(value = "/updateRewardCategory")
	public MasterResponse updateRewardCategory(@RequestBody RewardCategoryMaster mrc) {
		return iMasterService.updateRewardCategory(mrc);
	}

	@GetMapping(value = "/deleteRewardCategory/{id}")
	public MasterResponse deleteRewardCategory(@PathVariable("id") Integer id) {
		return iMasterService.deleteRewardCategory(id);
	}

	@PostMapping(value = "/saveRewardAdminSetup")
	public MasterResponse saveRewardAdminSetup(@RequestBody RewardAdminSetup request) {
		return iMasterService.saveRewardAdminSetup(request);
	}

	@PostMapping(value = "/deleteRewardAdminSetup")
	public MasterResponse deleteRewardAdminSetup(@RequestBody RewardAdminSetup request) {
		return iMasterService.deleteRewardAdminSetup(request);
	}

	@GetMapping(value = "/getRewardAdminSetupList/{branchId}")
	public MasterResponse getRewardAdminSetupList(@PathVariable("branchId") Integer branchId) {
		return iMasterService.getRewardAdminList(branchId);
	}

	@GetMapping(value = "/getRewardAdminSetupListByEmpId/{empId}")
	public MasterResponse getRewardAdminSetupListByEmpId(@PathVariable("empId") Integer empId) {
		return iMasterService.getRewardAdminListByEmpId(empId);
	}

	@CrossOrigin
	@GetMapping(value = "/getRedemptionTrackerList/{branchId}")
	public MasterResponse getRedemptionTrackerList(@PathVariable("branchId") Integer branchId) {
		return iMasterService.getRedemptionTrackerList(branchId);
	}

	// Suggestion Flow Config API - Vinay 09/09/2019

	@PostMapping(value = "/addOrUpdateSuggConfig")
	public MasterResponse addOrUpdateSuggConfig(@RequestBody SuggestionFlowConfig config) {
		return iMasterService.addOrUpdateSuggConfig(config);
	}

	@CrossOrigin
	@GetMapping(value = "/getSuggConfigList/{orgId}")
	public MasterResponse getSuggConfigList(@PathVariable("orgId") Integer orgId) {
		return iMasterService.getSuggConfigList(orgId);
	}

	//////////////////// DONT REMOVE TEST FROM BELOW
	//////////////////// API////////////////////////////////////////
	@CrossOrigin
	@GetMapping(value = "test/getOrgDataByAlieas/{Alieas}")
	public MasterResponse getOrgDataByAlieas(@PathVariable("Alieas") String Alieas) {
		return iMasterService.getOrgDataByAlieas(Alieas);
	}
	////////////////////////////////////////////////////////////////////////////////////////////

	@PostMapping(value = "/removeLineDetails")
	public MasterResponse removeLineDetails(@RequestBody Line line) {
		return iMasterService.removeLineDetails(line);
	}

	@PostMapping(value = "/updateOptionalJurySetup")
	public MasterResponse updateOptionalJurySetup(@RequestBody SuggestionEvalJurySetup request) {
		return iMasterService.updateOptionalJurySetup(request);
	}

	/**
	 * @author Sonali Bhosale Jan 14, 2020 12:19:23 AM
	 * @param request
	 * @return
	 */
	@PostMapping(value = "/saveSuggestionUserSetup")
	public MasterResponse saveSuggestionUserSetup(@RequestBody SuggestionUserSetup request) {
		return iMasterService.saveSuggestionUserSetup(request);
	}

	@PostMapping(value = "/deleteSuggestionUserSetup")
	public MasterResponse deleteSuggestionUserSetup(@RequestBody SuggestionUserSetup request) {
		return iMasterService.deleteSuggestionUserSetup(request);
	}

	@GetMapping(value = "/getSuggUserTypes")
	public MasterResponse getSuggUserTypes() {
		return iMasterService.getSuggUserTypes();
	}

	/**
	 * @author => Vinay B
	 * @Desc => Add Or Update Particulars Points Details
	 * @Date => 06-Dec-2019
	 */
	@CrossOrigin
	@PostMapping(value = "/addUpdateParticularsPoints")
	public MasterResponse addUpdateParticularsPoints(@RequestBody ParticularsPoint particulars) {
		return iMasterService.addUpdateParticularsPoints(particulars);
	}

	// New API for Portal

	/**
	 * @author Vinay B. Nov 24, 2020 2:39:22 PM
	 * @return MasterResponse
	 * @comments
	 */
	@CrossOrigin
	@GetMapping(value = "/getMasterUnits")
	public MasterResponse getMasterUnits() {
		return iMasterService.getMasterUnits();
	}

	/**
	 * @author Shital H. Mar 8, 2021 11:20:57 AM
	 * @return MasterResponse
	 * @comments
	 */
	@CrossOrigin
	@PostMapping(value = "/getemployeeWiseHappinessIndex")
	public MasterResponse employeeWiseHappinessIndex(@RequestBody MasterDTO request) {
		return iMasterService.employeeWiseHappinessIndex(request);
	}
	
	/**
	 * @author rakesh 12 August 2020
	 * @param request
	 * @return
	 */
	@PostMapping(value = "/addOrUpdateCategoryList")
	public MasterResponse addOrUpdateCategoryList(@RequestBody MasterDTO request) {
		return iMasterService.addOrUpdateCategoryList(request);
	}
	
	@PostMapping(value = "//addCWAgencyDetails")
	public MasterResponse addCWAgencyDetails(@RequestBody CWAgency request) {
		return iMasterService.addCWAgencyDetails(request);
	}

	@GetMapping(value = "/getCWAgencyList/{orgId}")
	public MasterResponse getCWAgencyList(@PathVariable("orgId") Integer orgId) {
		return iMasterService.getCWAgencyList(orgId);
	}
	
	@PostMapping(value = "/addDocument")
	public MasterResponse addDocument(@RequestBody MasterDocument document) {
		return iMasterService.addDocument(document);
	}

	@PostMapping(value = "/updateDocument")
	public MasterResponse updateDocument(@RequestBody MasterDocument document) {
		return iMasterService.updateDocument(document);
	}
	
	@PostMapping(value = "/removeDocument")
	public MasterResponse removeDocument(@RequestBody MasterDocument document) {
		return iMasterService.removeDocument(document);
	}
	
}
