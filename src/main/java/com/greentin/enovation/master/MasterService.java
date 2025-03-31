package com.greentin.enovation.master;

import static java.util.Comparator.comparingInt;
import static java.util.stream.Collectors.collectingAndThen;
import static java.util.stream.Collectors.toCollection;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.hibernate.exception.ConstraintViolationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.google.gson.Gson;
import com.greentin.enovation.accesscontrol.ProductOrgConfig;
import com.greentin.enovation.beans.BenefitAnalysisMasterDTO;
import com.greentin.enovation.beans.Branch;
import com.greentin.enovation.beans.DepartmentLevelBean;
import com.greentin.enovation.beans.DepartmentList;
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
import com.greentin.enovation.utils.BuildResponse;
import com.greentin.enovation.utils.CommonUtils;
import com.greentin.enovation.utils.EnovationConstants;

@Service
public class MasterService implements IMasterService {

	private static final Logger LOGGER = LoggerFactory.getLogger(MasterService.class);

	@Autowired
	IMasterDao iMasterDao;

	@Autowired
	CommonUtils commonUtils;

	/*
	 * @Override public HashMap<String, Object> getdeptListService1() {
	 * HashMap<String, Object> status=new HashMap<>(); try { List<DepartmentMaster>
	 * deptList=iMasterDao.getdeptList(); status.put("deptList", deptList);
	 * status.put(EnovationConstants.status,EnovationConstants.statusSuccess);
	 * }catch(Exception e) {
	 * status.put(EnovationConstants.status,EnovationConstants.statusFail);
	 * e.printStackTrace();
	 * LOGGER.info("Exception occured while getdeptListService()  : " +
	 * e.getMessage()); } return status; }
	 */

	public MasterResponse getdeptListService() {
		MasterResponse mstrList = null;
		try {
			List<DepartmentMaster> deptList = iMasterDao.getdeptList();
			if (!deptList.isEmpty()) {
				mstrList = new MasterResponse();
				mstrList.setStatus(EnovationConstants.statusSuccess);
				mstrList.setStatusCode(EnovationConstants.Code200);
				mstrList.setResult(EnovationConstants.ResultTrue);
				mstrList.setReason(EnovationConstants.RECORDSFETCHSUCCESS);
				mstrList.setDeptList(deptList);
				return mstrList;
			} else {

				mstrList = new MasterResponse();
				mstrList.setStatus(EnovationConstants.statusSuccess);
				mstrList.setStatusCode(EnovationConstants.Code200);
				mstrList.setResult(EnovationConstants.ResultFalse);
				mstrList.setReason(EnovationConstants.RecordsDoesNotExist);
				return mstrList;
			}

		} catch (Exception e) {
			mstrList = new MasterResponse();
			mstrList.setStatus(EnovationConstants.statusFail);
			mstrList.setStatusCode(EnovationConstants.Code500);
			mstrList.setResult(EnovationConstants.ResultFalse);
			mstrList.setReason("Exception Occured in designation category Api :" + e.getMessage());
			return mstrList;
		}
	}

	@Override
	public MasterResponse getdepartmentlistbyorgid(Integer orgId) {
		MasterResponse mstrList = null;
		try {
			List<DepartmentMaster> deptListbyOrgId = iMasterDao.getdepartmentlistbyorgid(orgId);
			if (!deptListbyOrgId.isEmpty()) {
				mstrList = new MasterResponse();
				mstrList.setStatus(EnovationConstants.statusSuccess);
				mstrList.setStatusCode(EnovationConstants.Code200);
				mstrList.setResult(EnovationConstants.ResultTrue);
				mstrList.setReason(EnovationConstants.RECORDSFETCHSUCCESS);
				mstrList.setDeptList(deptListbyOrgId);
				return mstrList;
			} else {

				mstrList = new MasterResponse();
				mstrList.setStatus(EnovationConstants.statusSuccess);
				mstrList.setStatusCode(EnovationConstants.Code200);
				mstrList.setResult(EnovationConstants.ResultFalse);
				mstrList.setReason(EnovationConstants.RecordsDoesNotExist);
				return mstrList;
			}

		} catch (Exception e) {
			mstrList = new MasterResponse();
			mstrList.setStatus(EnovationConstants.statusFail);
			mstrList.setStatusCode(EnovationConstants.Code500);
			mstrList.setResult(EnovationConstants.ResultFalse);
			mstrList.setReason("Exception Occured in designation category Api :" + e.getMessage());
			return mstrList;
		}
	}
	/*
	 * @Override public HashMap<String, Object> getCatagoryListService() {
	 * HashMap<String, Object> status=new HashMap<>(); try { List<CategoryMaster>
	 * catList=iMasterDao.getCatagoryList(); status.put("catagoryList", catList);
	 * status.put(EnovationConstants.status,EnovationConstants.statusSuccess);
	 * }catch(Exception e) {
	 * status.put(EnovationConstants.status,EnovationConstants.statusFail);
	 * e.printStackTrace(); } return status; }
	 */

	@Override
	public MasterResponse getCatagoryListService() {
		MasterResponse mstrList = null;
		try {
			List<CategoryMaster> catList = iMasterDao.getCatagoryList();
			if (!catList.isEmpty()) {
				mstrList = new MasterResponse();
				mstrList.setStatus(EnovationConstants.statusSuccess);
				mstrList.setStatusCode(EnovationConstants.Code200);
				mstrList.setResult(EnovationConstants.ResultTrue);
				mstrList.setReason(EnovationConstants.RECORDSFETCHSUCCESS);
				mstrList.setCatagoryList(catList);
				return mstrList;
			} else {

				mstrList = new MasterResponse();
				mstrList.setStatus(EnovationConstants.statusSuccess);
				mstrList.setStatusCode(EnovationConstants.Code200);
				mstrList.setResult(EnovationConstants.ResultFalse);
				mstrList.setReason(EnovationConstants.RecordsDoesNotExist);
				return mstrList;
			}

		} catch (Exception e) {
			mstrList = new MasterResponse();
			mstrList.setStatus(EnovationConstants.statusFail);
			mstrList.setStatusCode(EnovationConstants.Code500);
			mstrList.setResult(EnovationConstants.ResultFalse);
			mstrList.setReason("Exception Occured in category Api :" + e.getMessage());
			return mstrList;
		}
	}

	@Override
	public MasterResponse addDepartmentService(DepartmentMaster dept) {
		MasterResponse mstrList = null;
		try {

			boolean department = iMasterDao.updateDepartment(dept);
			LOGGER.info("save" + department);
			if (department) {
				mstrList = new MasterResponse();
				mstrList.setStatus(EnovationConstants.statusSuccess);
				mstrList.setStatusCode(EnovationConstants.Code200);
				mstrList.setResult(EnovationConstants.resultTrue);
				return mstrList;
			}

		} catch (Exception e) {
			mstrList = new MasterResponse();
			mstrList.setStatus(EnovationConstants.statusFail);
			mstrList.setStatusCode(EnovationConstants.Code500);
			mstrList.setResult(EnovationConstants.ResultFalse);
			mstrList.setReason("Exception Occured in addorupdateDepartment Api :" + e.getMessage());
			return mstrList;
		}
		return mstrList;
	}

	@Override
	public MasterResponse addOrganizationService(OrganizationMaster org) {
		MasterResponse mstrList = new MasterResponse();
		try {
			boolean isexit = iMasterDao.isexitsorg(org);
			if (isexit) {
				boolean oraganization = iMasterDao.addOrganization(org);
				System.out.println("save" + oraganization);
				if (oraganization) {
					mstrList.setStatus(EnovationConstants.statusSuccess);
					mstrList.setStatusCode(EnovationConstants.Code200);
					mstrList.setResult(EnovationConstants.ResultTrue);
					mstrList.setReason("Oraganization record inserted");
					return mstrList;
				} else {
					mstrList.setStatus(EnovationConstants.statusFail);
					mstrList.setStatusCode(EnovationConstants.Code200);
					mstrList.setResult(EnovationConstants.ResultFalse);
					mstrList.setReason("Failed to insert record");
					return mstrList;

				}
			} else {
				mstrList.setStatus(EnovationConstants.statusFail);
				mstrList.setStatusCode(EnovationConstants.Code200);
				mstrList.setResult(EnovationConstants.ResultFalse);
				mstrList.setReason("Oraganization Records Already Exists");
				return mstrList;
			}
		} catch (Exception e) {
			mstrList.setStatus(EnovationConstants.statusFail);
			mstrList.setStatusCode(EnovationConstants.Code500);
			mstrList.setResult(EnovationConstants.ResultFalse);
			mstrList.setReason("Exception Occured in addorupdateOraganization Api :" + e.getMessage());
			return mstrList;
		}

	}

	/*
	 * @Override public HashMap<String, Object> addCategoryService(CategoryMaster
	 * cat) { HashMap<String, Object> status=new HashMap<>(); try { CategoryMaster
	 * category=iMasterDao.addCategory(cat);
	 * LOGGER.info("addCategoryService  : "+category);
	 * status.put(EnovationConstants.status,EnovationConstants.statusSuccess);
	 * }catch(Exception e) {
	 * status.put(EnovationConstants.status,EnovationConstants.statusFail);
	 * e.printStackTrace(); } return status; }
	 */

	@Override
	public MasterResponse addCategoryService(CategoryMaster cat) {
		MasterResponse mstrList = new MasterResponse();
		try {
			boolean isexit = false;
			if (cat.getCatId() == 0) {
				isexit = iMasterDao.isexitscat(cat);
			} else {
				isexit = true;
			}
			if (isexit) {
				boolean category = iMasterDao.addCategory(cat);
				if (category) {
					mstrList.setStatus(EnovationConstants.statusSuccess);
					mstrList.setStatusCode(EnovationConstants.Code200);
					mstrList.setResult(EnovationConstants.ResultTrue);
					mstrList.setReason("Category record inserted");
					return mstrList;
				} else {
					mstrList.setStatus(EnovationConstants.statusFail);
					mstrList.setResult(EnovationConstants.ResultFalse);
					mstrList.setStatusCode(EnovationConstants.Code200);
					mstrList.setReason("Failed to insert record");
					return mstrList;
				}
			} else {
				mstrList.setStatus(EnovationConstants.statusFail);
				mstrList.setStatusCode(EnovationConstants.Code200);
				mstrList.setResult(EnovationConstants.ResultFalse);
				mstrList.setReason("Category Records Already Exists");
				return mstrList;
			}
		} catch (Exception e) {
			mstrList.setStatus(EnovationConstants.statusFail);
			mstrList.setStatusCode(EnovationConstants.Code500);
			mstrList.setResult(EnovationConstants.ResultFalse);
			mstrList.setReason("Exception Occured in addorupdateCategory Api :" + e.getMessage());
			return mstrList;
		}

	}

	/*
	 * @Override public HashMap<String, Object> addTeamTypeService(TeamtypeMaster
	 * teamType) { HashMap<String, Object> status=new HashMap<>(); try {
	 * TeamtypeMaster team_type=iMasterDao.addTeamType(teamType);
	 * LOGGER.info("addTeamTypeService  : "+team_type);
	 * status.put(EnovationConstants.status,EnovationConstants.statusSuccess);
	 * }catch(Exception e) {
	 * status.put(EnovationConstants.status,EnovationConstants.statusFail);
	 * e.printStackTrace(); } return status; }
	 */

	@Override
	public MasterResponse addTeamTypeService(TeamtypeMaster teamType) {
		MasterResponse mstrList = new MasterResponse();
		try {
			boolean isexit = iMasterDao.isexitsteamtype(teamType);
			if (isexit) {
				boolean team_type = iMasterDao.addTeamType(teamType);
				if (team_type) {
					mstrList.setStatus(EnovationConstants.statusSuccess);
					mstrList.setStatusCode(EnovationConstants.Code200);
					mstrList.setResult(EnovationConstants.ResultTrue);
					mstrList.setReason("Teamtype record inserted");
					return mstrList;
				} else {
					mstrList.setStatus(EnovationConstants.statusFail);
					mstrList.setResult(EnovationConstants.ResultFalse);
					mstrList.setStatusCode(EnovationConstants.Code200);
					mstrList.setReason("Failed to insert record");
					return mstrList;
				}
			} else {
				mstrList.setStatus(EnovationConstants.statusFail);
				mstrList.setStatusCode(EnovationConstants.Code200);
				mstrList.setResult(EnovationConstants.ResultFalse);
				mstrList.setReason("TeamType Records Already Exists");
				return mstrList;
			}
		} catch (Exception e) {
			mstrList.setStatus(EnovationConstants.statusFail);
			mstrList.setStatusCode(EnovationConstants.Code500);
			mstrList.setResult(EnovationConstants.ResultFalse);
			mstrList.setReason("Exception Occured in addorupdateteamtype Api :" + e.getMessage());
			return mstrList;
		}

	}

	/*
	 * @Override public HashMap<String, Object> getTeamTypeListService() {
	 * HashMap<String, Object> status=new HashMap<>(); try { List<TeamtypeMaster>
	 * teamTypeList=iMasterDao.getTeamTypeList(); status.put("teamTypeList",
	 * teamTypeList);
	 * status.put(EnovationConstants.status,EnovationConstants.statusSuccess);
	 * }catch(Exception e) {
	 * status.put(EnovationConstants.status,EnovationConstants.statusFail);
	 * e.printStackTrace(); } return status; }
	 */

	@Override
	public MasterResponse getTeamTypeListService() {
		MasterResponse mstrList = null;
		try {
			List<TeamtypeMaster> teamTypeList = iMasterDao.getTeamTypeList();
			if (!teamTypeList.isEmpty()) {
				mstrList = new MasterResponse();
				mstrList.setStatus(EnovationConstants.statusSuccess);
				mstrList.setStatusCode(EnovationConstants.Code200);
				mstrList.setResult(EnovationConstants.ResultTrue);
				mstrList.setReason(EnovationConstants.RECORDSFETCHSUCCESS);
				mstrList.setTeamtypemaster(teamTypeList);
				return mstrList;
			} else {
				mstrList = new MasterResponse();
				mstrList.setStatus(EnovationConstants.statusSuccess);
				mstrList.setStatusCode(EnovationConstants.Code200);
				mstrList.setResult(EnovationConstants.ResultFalse);
				mstrList.setReason(EnovationConstants.RecordsDoesNotExist);
				return mstrList;
			}

		} catch (Exception e) {
			mstrList = new MasterResponse();
			mstrList.setStatus(EnovationConstants.statusFail);
			mstrList.setStatusCode(EnovationConstants.Code500);
			mstrList.setResult(EnovationConstants.ResultFalse);
			mstrList.setReason("Exception Occured in category Api :" + e.getMessage());
			return mstrList;
		}
	}

	/*
	 * @Override public HashMap<String, Object> getStatusMasterListService() {
	 * HashMap<String, Object> status=new HashMap<>(); try { List<StatusMaster>
	 * statusList=iMasterDao.getStatusMasterList(); status.put("statusList",
	 * statusList);
	 * status.put(EnovationConstants.status,EnovationConstants.statusSuccess);
	 * }catch(Exception e) {
	 * status.put(EnovationConstants.status,EnovationConstants.statusFail);
	 * e.printStackTrace(); } return status; }
	 */

	@Override
	public MasterResponse getStatusMasterListService() {
		MasterResponse mstrList = null;
		try {
			List<StatusMaster> statusList = iMasterDao.getStatusMasterList();
			if (!statusList.isEmpty()) {
				mstrList = new MasterResponse();
				mstrList.setStatus(EnovationConstants.statusSuccess);
				mstrList.setStatusCode(EnovationConstants.Code200);
				mstrList.setResult(EnovationConstants.ResultTrue);
				mstrList.setReason(EnovationConstants.RECORDSFETCHSUCCESS);
				mstrList.setStatusList(statusList);
				return mstrList;
			} else {
				mstrList = new MasterResponse();
				mstrList.setStatus(EnovationConstants.statusSuccess);
				mstrList.setStatusCode(EnovationConstants.Code200);
				mstrList.setResult(EnovationConstants.ResultFalse);
				mstrList.setReason(EnovationConstants.RecordsDoesNotExist);
				return mstrList;
			}

		} catch (Exception e) {
			mstrList = new MasterResponse();
			mstrList.setStatus(EnovationConstants.statusFail);
			mstrList.setStatusCode(EnovationConstants.Code500);
			mstrList.setResult(EnovationConstants.ResultFalse);
			mstrList.setReason("Exception Occured in status Api :" + e.getMessage());
			return mstrList;
		}
	}

	/*
	 * @Override public HashMap<String, Object> getOrgMasterListService() {
	 * HashMap<String, Object> status=new HashMap<>(); try {
	 * List<OrganizationMaster> orgList=iMasterDao.getOrgMasterList();
	 * status.put("orgList", orgList);
	 * status.put(EnovationConstants.status,EnovationConstants.statusSuccess);
	 * }catch(Exception e) {
	 * status.put(EnovationConstants.status,EnovationConstants.statusFail);
	 * e.printStackTrace(); } return status; }
	 */

	@Override
	public MasterResponse getOrgMasterListService() {
		MasterResponse mstrList = null;
		try {
			List<OrganizationMaster> orgList = iMasterDao.getOrgMasterList();
			if (!orgList.isEmpty()) {
				mstrList = new MasterResponse();
				mstrList.setStatus(EnovationConstants.statusSuccess);
				mstrList.setStatusCode(EnovationConstants.Code200);
				mstrList.setResult(EnovationConstants.ResultTrue);
				mstrList.setReason(EnovationConstants.RECORDSFETCHSUCCESS);
				mstrList.setOrgList(orgList);
				return mstrList;
			} else {
				mstrList = new MasterResponse();
				mstrList.setStatus(EnovationConstants.statusSuccess);
				mstrList.setStatusCode(EnovationConstants.Code200);
				mstrList.setResult(EnovationConstants.ResultFalse);
				mstrList.setReason(EnovationConstants.RecordsDoesNotExist);
				return mstrList;
			}

		} catch (Exception e) {
			mstrList = new MasterResponse();
			mstrList.setStatus(EnovationConstants.statusFail);
			mstrList.setStatusCode(EnovationConstants.Code500);
			mstrList.setResult(EnovationConstants.ResultFalse);
			mstrList.setReason("Exception Occured in status Api :" + e.getMessage());
			return mstrList;
		}
	}

	@Override
	public HashMap<String, Object> addOrgMasterService(OrganizationMaster org) {
		HashMap<String, Object> status = new HashMap<>();
		try {
			LOGGER.info("# INSIDE in addOrgMasterService ");
			OrganizationMaster org_master = iMasterDao.addOrgMaster(org);
			LOGGER.info("addOrgMasterService  : " + org_master);
			status.put(EnovationConstants.status, EnovationConstants.statusSuccess);
		} catch (Exception e) {
			status.put(EnovationConstants.status, EnovationConstants.statusFail);
			e.printStackTrace();
		}
		return status;
	}

	@Override
	public MasterResponse getPointLevelListService(Integer orgId) {
		MasterResponse mstrList = null;
		try {
			List<PointLevelMaster> pointLevelList = iMasterDao.getPointLevelList(orgId);
			if (!pointLevelList.isEmpty()) {
				mstrList = new MasterResponse();
				mstrList.setStatus(EnovationConstants.statusSuccess);
				mstrList.setStatusCode(EnovationConstants.Code200);
				mstrList.setResult(EnovationConstants.ResultTrue);
				mstrList.setReason(EnovationConstants.RECORDSFETCHSUCCESS);
				mstrList.setPointmaster(pointLevelList);
				return mstrList;
			} else {
				mstrList = new MasterResponse();
				mstrList.setStatus(EnovationConstants.statusSuccess);
				mstrList.setStatusCode(EnovationConstants.Code200);
				mstrList.setResult(EnovationConstants.resultFalse);
				mstrList.setReason(EnovationConstants.RecordsDoesNotExist);
				return mstrList;
			}

		} catch (Exception e) {
			mstrList = new MasterResponse();
			mstrList.setStatus(EnovationConstants.statusFail);
			mstrList.setStatusCode(EnovationConstants.Code500);
			mstrList.setResult(EnovationConstants.resultFalse);
			mstrList.setReason("Exception Occured in getpointlevel Api :" + e.getMessage());
			return mstrList;
		}
	}

	@Override
	public MasterResponse deleteOrganization(OrganizationMaster organ) {
		MasterResponse mstrList = null;
		try {

			boolean oraganization = iMasterDao.deleteOrganization(organ);
			System.out.println("save" + oraganization);
			if (oraganization) {
				mstrList = new MasterResponse();
				mstrList.setStatus(EnovationConstants.statusSuccess);
				mstrList.setStatusCode(EnovationConstants.Code200);
				mstrList.setResult(EnovationConstants.resultTrue);
				return mstrList;
			} else {
				mstrList = new MasterResponse();
				mstrList.setStatus(EnovationConstants.statusFail);
				mstrList.setStatusCode(EnovationConstants.Code200);
				mstrList.setResult(EnovationConstants.resultFalse);
				return mstrList;
			}
		} catch (Exception e) {
			mstrList = new MasterResponse();
			mstrList.setStatus(EnovationConstants.statusFail);
			mstrList.setStatusCode(EnovationConstants.Code500);
			mstrList.setResult(EnovationConstants.ResultFalse);
			mstrList.setReason("Exception Occured in addorupdateDepartment Api :" + e.getMessage());
			return mstrList;
		}

	}

	@Override
	public MasterResponse deleteCategory(CategoryMaster cate) {
		MasterResponse mstrList = null;
		try {

			boolean oraganization = iMasterDao.deleteCategory(cate);
			if (oraganization) {
				mstrList = new MasterResponse();
				mstrList.setStatus(EnovationConstants.statusSuccess);
				mstrList.setStatusCode(EnovationConstants.Code200);
				mstrList.setResult(EnovationConstants.resultTrue);
				return mstrList;
			} else {
				mstrList = new MasterResponse();
				mstrList.setStatus(EnovationConstants.statusFail);
				mstrList.setStatusCode(EnovationConstants.Code200);
				mstrList.setResult(EnovationConstants.resultFalse);
				return mstrList;
			}
		} catch (Exception e) {
			mstrList = new MasterResponse();
			mstrList.setStatus(EnovationConstants.cantdeleteFail);
			mstrList.setStatusCode(EnovationConstants.Code100);
			mstrList.setResult(EnovationConstants.ResultFalse);
			mstrList.setReason(EnovationConstants.REMOVE_CATEGORY);
			return mstrList;
		}
	}

	@Override
	public MasterResponse deleteDepartment(DepartmentMaster dept) {
		MasterResponse mstrList = null;
		try {
			int value = iMasterDao.deleteDepartment(dept);
			if (value == 1) {
				mstrList = new MasterResponse();
				mstrList.setStatus(EnovationConstants.statusSuccess);
				mstrList.setResult(EnovationConstants.resultTrue);
				mstrList.setStatusCode(EnovationConstants.Code200);
				return mstrList;
			} else {
				mstrList = new MasterResponse();
				mstrList.setStatus(EnovationConstants.statusFail);
				mstrList.setStatusCode(EnovationConstants.Code200);
				return mstrList;
			}
		} catch (Exception e) {
			mstrList = new MasterResponse();
			mstrList.setStatus(EnovationConstants.statusFail);
			mstrList.setStatusCode(EnovationConstants.Code500);
			return mstrList;
		}
	}

	/*
	 * public MasterResponse getDesignationListService() { MasterResponse
	 * mstrList=null; try { List<DesignationMaster>
	 * designationList=iMasterDao.getDesignationList();
	 * if(!designationList.isEmpty()){ mstrList=new MasterResponse();
	 * mstrList.setStatus(EnovationConstants.statusSuccess);
	 * mstrList.setStatusCode(EnovationConstants.Code200);
	 * mstrList.setReason(EnovationConstants.RECORDSFETCHSUCCESS);
	 * mstrList.setResult(EnovationConstants.ResultTrue);
	 * mstrList.setDesignation(designationList); return mstrList; }else {
	 * 
	 * mstrList=new MasterResponse();
	 * mstrList.setStatus(EnovationConstants.statusSuccess);
	 * mstrList.setStatusCode(EnovationConstants.Code200);
	 * mstrList.setResult(EnovationConstants.ResultFalse);
	 * mstrList.setReason(EnovationConstants.RecordsDoesNotExist); return mstrList;
	 * }
	 * 
	 * }catch(Exception e) { mstrList=new MasterResponse();
	 * mstrList.setStatus(EnovationConstants.statusFail);
	 * mstrList.setStatusCode(EnovationConstants.Code500);
	 * mstrList.setResult(EnovationConstants.ResultFalse);
	 * mstrList.setReason("Exception Occured in designation category Api :" +
	 * e.getMessage()); return mstrList; } }
	 */
	@Override
	public MasterResponse getBenefitAnalysisList(Integer orgId, Integer branchId) {
		MasterResponse mstrList = null;
		try {
			if (branchId != 0) {
				List<BenefitAnalysisMasterDTO> benefitAnalysis = iMasterDao.getBenefitAnalysisList(orgId, branchId);
				if (!benefitAnalysis.isEmpty()) {
					mstrList = new MasterResponse();
					mstrList.setStatus(EnovationConstants.statusSuccess);
					mstrList.setStatusCode(EnovationConstants.Code200);
					mstrList.setResult(EnovationConstants.ResultTrue);
					mstrList.setReason(EnovationConstants.RECORDSFETCHSUCCESS);
					mstrList.setBenefitAnalysisList(benefitAnalysis);
					return mstrList;
				} else {
					mstrList = new MasterResponse();
					mstrList.setStatus(EnovationConstants.statusSuccess);
					mstrList.setStatusCode(EnovationConstants.Code200);
					mstrList.setResult(EnovationConstants.ResultFalse);
					mstrList.setReason(EnovationConstants.RecordsDoesNotExist);
					return mstrList;
				}
			} else {
				mstrList = new MasterResponse();
				mstrList.setStatus(EnovationConstants.statusSuccess);
				mstrList.setStatusCode(EnovationConstants.CODE400);
				mstrList.setResult(EnovationConstants.ResultFalse);
				mstrList.setReason(EnovationConstants.BAD_REQUEST);
				return mstrList;
			}

		} catch (Exception e) {
			mstrList = new MasterResponse();
			mstrList.setStatus(EnovationConstants.statusFail);
			mstrList.setStatusCode(EnovationConstants.Code500);
			mstrList.setResult(EnovationConstants.ResultFalse);
			mstrList.setReason("Exception Occured in getBenefitAnalysisList Api :" + e.getMessage());
			return mstrList;
		}
	}

	@Override
	public MasterResponse getbranchlistService(Integer orgId) {
		MasterResponse mstrList = null;
		try {
			if (orgId != 0) {
				List<BranchMaster> branchList = iMasterDao.getbranchlist(orgId);
				if (!branchList.isEmpty()) {
					mstrList = new MasterResponse();
					mstrList.setStatus(EnovationConstants.statusSuccess);
					mstrList.setStatusCode(EnovationConstants.Code200);
					mstrList.setResult(EnovationConstants.ResultTrue);
					mstrList.setReason(EnovationConstants.RECORDSFETCHSUCCESS);
					mstrList.setBranchList(branchList);
					return mstrList;
				} else {
					mstrList = new MasterResponse();
					mstrList.setStatus(EnovationConstants.statusSuccess);
					mstrList.setStatusCode(EnovationConstants.Code200);
					mstrList.setReason(EnovationConstants.RecordsDoesNotExist);
					return mstrList;
				}
			} else {
				mstrList = new MasterResponse();
				mstrList.setStatus(EnovationConstants.statusSuccess);
				mstrList.setStatusCode(EnovationConstants.CODE400);
				mstrList.setReason(EnovationConstants.BAD_REQUEST);
				return mstrList;
			}

		} catch (Exception e) {
			mstrList = new MasterResponse();
			mstrList.setStatus(EnovationConstants.statusFail);
			mstrList.setStatusCode(EnovationConstants.Code500);
			mstrList.setResult(EnovationConstants.ResultFalse);
			mstrList.setReason("Exception Occured in getBenefitAnalysisList Api :" + e.getMessage());
			return mstrList;
		}
	}

	@Override
	public MasterResponse addbranch(BranchMaster brnch) {
		MasterResponse mstrList = null;
		try {
			boolean department = iMasterDao.addbranch(brnch);
			if (department) {
				mstrList = new MasterResponse();
				mstrList.setStatus(EnovationConstants.statusSuccess);
				mstrList.setStatusCode(EnovationConstants.Code200);
				mstrList.setResult(EnovationConstants.ResultTrue);
				return mstrList;
			}

		} catch (Exception e) {
			mstrList = new MasterResponse();
			mstrList.setStatus(EnovationConstants.statusFail);
			mstrList.setStatusCode(EnovationConstants.Code500);
			mstrList.setResult(EnovationConstants.ResultFalse);
			mstrList.setReason("Exception Occured in addorupdateDepartment Api :" + e.getMessage());
			return mstrList;
		}
		return mstrList;
	}

	@Override
	public MasterResponse addbenefitmaster(BenefitAnalysisMaster benefit) {
		MasterResponse mstrList = null;
		try {
			boolean benefitMaster = iMasterDao.addbenefitmaster(benefit);
			if (benefitMaster) {
				mstrList = new MasterResponse();
				mstrList.setStatus(EnovationConstants.statusSuccess);
				mstrList.setStatusCode(EnovationConstants.Code200);
				mstrList.setResult(EnovationConstants.ResultTrue);
				return mstrList;
			}

		} catch (Exception e) {
			mstrList = new MasterResponse();
			mstrList.setStatus(EnovationConstants.statusFail);
			mstrList.setStatusCode(EnovationConstants.Code500);
			mstrList.setResult(EnovationConstants.ResultFalse);
			mstrList.setReason("Exception Occured in addbenefitmaster Api :" + e.getMessage());
			return mstrList;
		}
		return mstrList;
	}

	@Override
	public MasterResponse addbenefitmasternew(BenefitAnalysisMaster newBenefit, BenefitAnalysisMaster oldBenefit) {
		MasterResponse mstrList = null;
		try {
			boolean benefitMaster = iMasterDao.addbenefitmasternew(newBenefit, oldBenefit);
			if (benefitMaster) {
				mstrList = new MasterResponse();
				mstrList.setStatus(EnovationConstants.statusSuccess);
				mstrList.setStatusCode(EnovationConstants.Code200);
				mstrList.setResult(EnovationConstants.ResultTrue);
				return mstrList;
			}

		} catch (Exception e) {
			mstrList = new MasterResponse();
			mstrList.setStatus(EnovationConstants.statusFail);
			mstrList.setStatusCode(EnovationConstants.Code500);
			mstrList.setResult(EnovationConstants.ResultFalse);
			mstrList.setReason("Exception Occured in addbenefitmaster Api :" + e.getMessage());
			return mstrList;
		}
		return mstrList;
	}

	@Override
	public MasterResponse saveExecutive(Executive executive) {
		LOGGER.info("# INSIDE IN saveExecutive ");
		MasterResponse mstrDTO = new MasterResponse();
		try {
			boolean saveExecutive = iMasterDao.saveExecutive(executive);
			if (saveExecutive) {
				mstrDTO.setStatus(EnovationConstants.statusSuccess);
				mstrDTO.setStatusCode(EnovationConstants.Code200);
				mstrDTO.setResult(EnovationConstants.ResultTrue);
				mstrDTO.setReason(" saveExecutive");
				return mstrDTO;
			} else {
				mstrDTO.setStatus(EnovationConstants.statusFail);
				mstrDTO.setStatusCode(EnovationConstants.Code200);
				mstrDTO.setResult(EnovationConstants.ResultFalse);
				mstrDTO.setReason("NOT saveExecutive");
				return mstrDTO;
			}

		} catch (Exception e) {
			mstrDTO.setStatus(EnovationConstants.statusFail);
			mstrDTO.setStatusCode(EnovationConstants.Code500);
			mstrDTO.setResult(EnovationConstants.ResultFalse);
			mstrDTO.setReason("EXCEPTION OCCURED IN saveExecutive API : " + e.getMessage());
			LOGGER.error("# INSIDE EXCEPTION OCCERED IN saveExecutive  " + e.getMessage());
			return mstrDTO;
		}
	}

	@Override
	public MasterResponse updateExecutive(Executive executive) {
		LOGGER.info("# INSIDE IN updateExecutive ");
		MasterResponse mstrDTO = new MasterResponse();
		try {
			boolean updateExecutive = iMasterDao.updateExecutive(executive);
			if (updateExecutive) {
				mstrDTO.setStatus(EnovationConstants.statusSuccess);
				mstrDTO.setStatusCode(EnovationConstants.Code200);
				mstrDTO.setResult(EnovationConstants.ResultTrue);
				mstrDTO.setReason("updateExecutive Sucess");
				return mstrDTO;
			} else {
				mstrDTO.setStatus(EnovationConstants.statusFail);
				mstrDTO.setStatusCode(EnovationConstants.Code200);
				mstrDTO.setResult(EnovationConstants.ResultFalse);
				mstrDTO.setReason("NOT updateExecutive");
				return mstrDTO;
			}

		} catch (Exception e) {
			mstrDTO.setStatus(EnovationConstants.statusFail);
			mstrDTO.setStatusCode(EnovationConstants.Code500);
			mstrDTO.setResult(EnovationConstants.ResultFalse);
			mstrDTO.setReason("EXCEPTION OCCURED IN updateExecutive API : " + e.getMessage());
			LOGGER.error("# INSIDE EXCEPTION OCCERED IN updateExecutive  " + e.getMessage());
			return mstrDTO;
		}
	}

	@Override
	public MasterResponse updateDeptlvlExecutive(DepartmentLevel deptexecutive) {
		LOGGER.info("# INSIDE IN updateDeptlvlExecutive ");
		MasterResponse mstrDTO = new MasterResponse();
		try {
			boolean updateExecutive = iMasterDao.updateDeptlvlExecutive(deptexecutive);
			if (updateExecutive) {
				mstrDTO.setStatus(EnovationConstants.statusSuccess);
				mstrDTO.setStatusCode(EnovationConstants.Code200);
				mstrDTO.setResult(EnovationConstants.ResultTrue);
				mstrDTO.setReason("updateDeptlvlExecutive Sucess");
				return mstrDTO;
			} else {
				mstrDTO.setStatus(EnovationConstants.statusFail);
				mstrDTO.setStatusCode(EnovationConstants.Code200);
				mstrDTO.setResult(EnovationConstants.ResultFalse);
				mstrDTO.setReason("NOT updateDeptlvlExecutive");
				return mstrDTO;
			}

		} catch (Exception e) {
			mstrDTO.setStatus(EnovationConstants.statusFail);
			mstrDTO.setStatusCode(EnovationConstants.Code500);
			mstrDTO.setResult(EnovationConstants.ResultFalse);
			mstrDTO.setReason("EXCEPTION OCCURED IN updateDeptlvlExecutive API : " + e.getMessage());
			LOGGER.error("# INSIDE EXCEPTION OCCERED IN updateDeptlvlExecutive  " + e.getMessage());
			return mstrDTO;
		}
	}

	@Override
	public MasterResponse removeExecutive(Integer execId) {
		LOGGER.info("# INSIDE IN removeExecutive ");
		MasterResponse mstrDTO = new MasterResponse();
		try {
			boolean removeExecutive = iMasterDao.removeExecutive(execId);
			if (removeExecutive) {
				mstrDTO.setStatus(EnovationConstants.statusSuccess);
				mstrDTO.setStatusCode(EnovationConstants.Code200);
				mstrDTO.setResult(EnovationConstants.ResultTrue);
				mstrDTO.setReason("removeExecutive Sucess");
				return mstrDTO;
			} else {
				mstrDTO.setStatus(EnovationConstants.statusFail);
				mstrDTO.setStatusCode(EnovationConstants.Code200);
				mstrDTO.setResult(EnovationConstants.ResultFalse);
				mstrDTO.setReason("NOT removeExecutive");
				return mstrDTO;
			}

		} catch (Exception e) {
			mstrDTO.setStatus(EnovationConstants.statusFail);
			mstrDTO.setStatusCode(EnovationConstants.Code500);
			mstrDTO.setResult(EnovationConstants.ResultFalse);
			mstrDTO.setReason("EXCEPTION OCCURED IN removeExecutive API : " + e.getMessage());
			LOGGER.error("# INSIDE EXCEPTION OCCERED IN removeExecutive  " + e.getMessage());
			return mstrDTO;
		}
	}

	@Override
	public MasterResponse getListOfExecutiveByOrgId(Integer orgId) {
		MasterResponse mstrList = null;
		try {
			if (orgId != 0) {
				List<ExecutiveListDto> executiveList = iMasterDao.getListOfExecutiveByOrgId(orgId);
				if (executiveList != null && executiveList.size() > 0) {
					mstrList = new MasterResponse();
					mstrList.setStatus(EnovationConstants.statusSuccess);
					mstrList.setStatusCode(EnovationConstants.Code200);
					mstrList.setResult(EnovationConstants.ResultTrue);
					mstrList.setReason(EnovationConstants.RECORDSFETCHSUCCESS);
					mstrList.setExecutiveList(executiveList);
					return mstrList;
				} else {
					mstrList = new MasterResponse();
					mstrList.setStatus(EnovationConstants.statusSuccess);
					mstrList.setStatusCode(EnovationConstants.Code200);
					mstrList.setResult(EnovationConstants.ResultFalse);
					mstrList.setReason(EnovationConstants.RecordsDoesNotExist);
					return mstrList;
				}
			} else {
				mstrList = new MasterResponse();
				mstrList.setStatus(EnovationConstants.statusSuccess);
				mstrList.setStatusCode(EnovationConstants.CODE400);
				mstrList.setReason(EnovationConstants.BAD_REQUEST);
				return mstrList;
			}

		} catch (Exception e) {
			mstrList = new MasterResponse();
			mstrList.setStatus(EnovationConstants.statusFail);
			mstrList.setStatusCode(EnovationConstants.Code500);
			mstrList.setResult(EnovationConstants.ResultFalse);
			mstrList.setReason("Exception Occured in getListOfExecutiveByOrgId Api :" + e.getMessage());
			return mstrList;
		}
	}

	@Override
	public MasterResponse getdepartmentlistbybranchid(Integer branchId) {
		MasterResponse mstrList = null;
		try {
			if (branchId != 0) {
				List<DepartmentMaster> deptList = iMasterDao.getdepartmentlistbybranchid(branchId);
				if (deptList != null) {
					mstrList = new MasterResponse();
					mstrList.setStatus(EnovationConstants.statusSuccess);
					mstrList.setStatusCode(EnovationConstants.Code200);
					mstrList.setResult(EnovationConstants.ResultTrue);
					mstrList.setReason(EnovationConstants.RECORDSFETCHSUCCESS);
					mstrList.setDeptList(deptList);
					return mstrList;
				} else {
					mstrList = new MasterResponse();
					mstrList.setStatus(EnovationConstants.statusSuccess);
					mstrList.setStatusCode(EnovationConstants.Code200);
					mstrList.setResult(EnovationConstants.ResultFalse);
					mstrList.setReason(EnovationConstants.RecordsDoesNotExist);
					return mstrList;
				}
			} else {
				mstrList = new MasterResponse();
				mstrList.setStatus(EnovationConstants.statusSuccess);
				mstrList.setStatusCode(EnovationConstants.CODE400);
				mstrList.setReason(EnovationConstants.BAD_REQUEST);
				return mstrList;
			}

		} catch (Exception e) {
			mstrList = new MasterResponse();
			mstrList.setStatus(EnovationConstants.statusFail);
			mstrList.setStatusCode(EnovationConstants.Code500);
			mstrList.setResult(EnovationConstants.ResultFalse);
			mstrList.setReason("Exception Occured in getdepartmentlistbybranchid Api :" + e.getMessage());
			return mstrList;
		}
	}

	/*
	 * @Override public MasterResponse getDesignationListbyorgId(Integer orgId) {
	 * MasterResponse mstrList=null; try { List<DesignationMaster>
	 * desigList=iMasterDao.getDesignationListbyorgId(orgId); if(desigList!=null){
	 * mstrList=new MasterResponse();
	 * mstrList.setStatus(EnovationConstants.statusSuccess);
	 * mstrList.setStatusCode(EnovationConstants.Code200);
	 * mstrList.setResult(EnovationConstants.ResultTrue);
	 * mstrList.setReason(EnovationConstants.RECORDSFETCHSUCCESS);
	 * mstrList.setDesignation(desigList); return mstrList; }else { mstrList=new
	 * MasterResponse(); mstrList.setStatus(EnovationConstants.statusSuccess);
	 * mstrList.setStatusCode(EnovationConstants.Code200);
	 * mstrList.setResult(EnovationConstants.ResultFalse);
	 * mstrList.setReason(EnovationConstants.RecordsDoesNotExist); return mstrList;
	 * }
	 * 
	 * }catch(Exception e) { mstrList=new MasterResponse();
	 * mstrList.setStatus(EnovationConstants.statusFail);
	 * mstrList.setStatusCode(EnovationConstants.Code500);
	 * mstrList.setResult(EnovationConstants.ResultFalse);
	 * mstrList.setReason("Exception Occured in getDesignationListbyorgId Api :" +
	 * e.getMessage()); return mstrList; } }
	 */
	@Override
	public MasterResponse getCategoryListbyorgId(Integer orgId, Integer branchId) {
		MasterResponse mstrList = null;
		try {
			if (branchId != 0) {
				List<CategoryMaster> catList = iMasterDao.getCategoryListbyorgId(orgId, branchId);
				if (catList != null && catList.size() > 0) {
					mstrList = new MasterResponse();
					mstrList.setStatus(EnovationConstants.statusSuccess);
					mstrList.setStatusCode(EnovationConstants.Code200);
					mstrList.setResult(EnovationConstants.ResultTrue);
					mstrList.setReason(EnovationConstants.RECORDSFETCHSUCCESS);
					mstrList.setCatagoryList(catList);
					return mstrList;
				} else {
					mstrList = new MasterResponse();
					mstrList.setStatus(EnovationConstants.statusSuccess);
					mstrList.setStatusCode(EnovationConstants.Code200);
					mstrList.setResult(EnovationConstants.ResultFalse);
					mstrList.setReason(EnovationConstants.RecordsDoesNotExist);
					return mstrList;
				}
			} else {
				mstrList = new MasterResponse();
				mstrList.setStatus(EnovationConstants.statusSuccess);
				mstrList.setStatusCode(EnovationConstants.CODE400);
				mstrList.setReason(EnovationConstants.BAD_REQUEST);
				return mstrList;
			}

		} catch (Exception e) {
			mstrList = new MasterResponse();
			mstrList.setStatus(EnovationConstants.statusFail);
			mstrList.setStatusCode(EnovationConstants.Code500);
			mstrList.setResult(EnovationConstants.ResultFalse);
			mstrList.setReason("Exception Occured in getCategoryListbyorgId Api :" + e.getMessage());
			return mstrList;
		}
	}
	
	/*
	 * @Override public MasterResponse addDesignation(DesignationMaster desig) {
	 * LOGGER.info("# INSIDE IN ADDDESIGNATION "); MasterResponse mstrDTO=new
	 * MasterResponse(); try { boolean
	 * addDesignation=iMasterDao.addDesignation(desig); if(addDesignation){
	 * mstrDTO.setStatus(EnovationConstants.statusSuccess);
	 * mstrDTO.setStatusCode(EnovationConstants.Code200);
	 * mstrDTO.setResult(EnovationConstants.ResultTrue);
	 * mstrDTO.setReason("ADDDESIGNATION"); return mstrDTO; }else {
	 * mstrDTO.setStatus(EnovationConstants.statusFail);
	 * mstrDTO.setStatusCode(EnovationConstants.Code200);
	 * mstrDTO.setResult(EnovationConstants.ResultFalse);
	 * mstrDTO.setReason("NOT ADDDESIGNATION"); return mstrDTO; }
	 * 
	 * }catch(Exception e) { mstrDTO.setStatus(EnovationConstants.statusFail);
	 * mstrDTO.setStatusCode(EnovationConstants.Code500);
	 * mstrDTO.setResult(EnovationConstants.ResultFalse);
	 * mstrDTO.setReason("EXCEPTION OCCURED IN ADDDESIGNATION API : " +
	 * e.getMessage());
	 * LOGGER.error("# INSIDE EXCEPTION OCCERED IN ADDDESIGNATION  "+e.getMessage())
	 * ; return mstrDTO; }
	 * 
	 * }
	 */
	@Override
	public MasterResponse addDepartment(DepartmentMaster dept) {
		MasterResponse mstrList = null;
		try {

			boolean department = iMasterDao.addDept(dept);
			LOGGER.info("save" + department);
			if (department) {
				mstrList = new MasterResponse();
				mstrList.setStatus(EnovationConstants.statusSuccess);
				mstrList.setStatusCode(EnovationConstants.Code200);
				mstrList.setResult(EnovationConstants.resultTrue);
				return mstrList;
			}

		} catch (Exception e) {
			mstrList = new MasterResponse();
			mstrList.setStatus(EnovationConstants.statusFail);
			mstrList.setStatusCode(EnovationConstants.Code500);
			mstrList.setResult(EnovationConstants.ResultFalse);
			mstrList.setReason("Exception Occured in ADDDEPARTMENT Api :" + e.getMessage());
			return mstrList;
		}
		return mstrList;
	}

	/*
	 * @Override public MasterResponse getDesignationListbybranchId(Integer
	 * branchId) { MasterResponse mstrList=null; try { List<DesignationMaster>
	 * desigList=iMasterDao.getDesignationListbybranchId(branchId);
	 * if(desigList!=null){ mstrList=new MasterResponse();
	 * mstrList.setStatus(EnovationConstants.statusSuccess);
	 * mstrList.setStatusCode(EnovationConstants.Code200);
	 * mstrList.setResult(EnovationConstants.ResultTrue);
	 * mstrList.setReason(EnovationConstants.RECORDSFETCHSUCCESS);
	 * mstrList.setDesignation(desigList); return mstrList; }else { mstrList=new
	 * MasterResponse(); mstrList.setStatus(EnovationConstants.statusSuccess);
	 * mstrList.setStatusCode(EnovationConstants.Code200);
	 * mstrList.setResult(EnovationConstants.ResultFalse);
	 * mstrList.setReason(EnovationConstants.RecordsDoesNotExist); return mstrList;
	 * }
	 * 
	 * }catch(Exception e) { mstrList=new MasterResponse();
	 * mstrList.setStatus(EnovationConstants.statusFail);
	 * mstrList.setStatusCode(EnovationConstants.Code500);
	 * mstrList.setResult(EnovationConstants.ResultFalse);
	 * mstrList.setReason("EXCEPTION OCCURED IN GETDESIGNATIONLISTBYBRANCHID API :"
	 * + e.getMessage()); return mstrList; } }
	 */

	/*
	 * @Override public MasterResponse getDesignationListbyBranch(Integer branchId)
	 * { MasterResponse mstrList=null; try { List<DesignationMaster>
	 * desigList=iMasterDao.getDesignationListbybranchId(branchId);
	 * if(desigList!=null){ mstrList=new MasterResponse();
	 * mstrList.setStatus(EnovationConstants.statusSuccess);
	 * mstrList.setStatusCode(EnovationConstants.Code200);
	 * mstrList.setResult(EnovationConstants.ResultTrue);
	 * mstrList.setReason(EnovationConstants.RECORDSFETCHSUCCESS);
	 * mstrList.setDesignation(desigList); return mstrList; }else { mstrList=new
	 * MasterResponse(); mstrList.setStatus(EnovationConstants.statusSuccess);
	 * mstrList.setStatusCode(EnovationConstants.Code200);
	 * mstrList.setResult(EnovationConstants.ResultFalse);
	 * mstrList.setReason(EnovationConstants.RecordsDoesNotExist); return mstrList;
	 * }
	 * 
	 * }catch(Exception e) { mstrList=new MasterResponse();
	 * mstrList.setStatus(EnovationConstants.statusFail);
	 * mstrList.setStatusCode(EnovationConstants.Code500);
	 * mstrList.setResult(EnovationConstants.ResultFalse);
	 * mstrList.setReason("Exception Occured in getDesignationListbyorgId Api :" +
	 * e.getMessage()); return mstrList; } }
	 */
	@Override
	public MasterResponse saveLevelWiseUsers(Set<DepartmentLevel> request) {
		MasterResponse mstrList = null;
		try {
			int saveLevelWiseUsers = iMasterDao.saveLevelWiseUsers(request);
			LOGGER.info("save" + saveLevelWiseUsers);
			if (saveLevelWiseUsers == 1) {
				mstrList = new MasterResponse();
				mstrList.setStatus(EnovationConstants.statusSuccess);
				mstrList.setStatusCode(EnovationConstants.Code200);
				mstrList.setResult(EnovationConstants.resultTrue);
				return mstrList;

			} else if (saveLevelWiseUsers == 2) {
				mstrList = new MasterResponse();
				mstrList.setStatus(EnovationConstants.statusFail);
				mstrList.setStatusCode(EnovationConstants.Code100);
				mstrList.setResult(EnovationConstants.ResultFalse);
				mstrList.setReason(EnovationConstants.CONTROLLER_ALIGNED);
				return mstrList;
			}

		} catch (Exception e) {
			mstrList = new MasterResponse();
			mstrList.setStatus(EnovationConstants.statusFail);
			mstrList.setStatusCode(EnovationConstants.Code500);
			mstrList.setResult(EnovationConstants.ResultFalse);
			mstrList.setReason(EnovationConstants.SERVER_EORROR);
			return mstrList;
		}
		return mstrList;

	}

	@Override
	public MasterResponse getListofEmployeebyBranchWise(Integer branchId, Integer levelId, Integer deptId,
			Integer roleId) {
		MasterResponse mstrList = null;
		LOGGER.info("#INSIDE IN  GETLISTOFEMPLOYEEBYBRANCHWISE API ");
		try {
			if (branchId != 0) {
				List<DepartmentLevelBean> deptLvlList = iMasterDao.getListofEmployeebyBranchWise(branchId, levelId,
						deptId, roleId);
				if (deptLvlList != null && deptLvlList.size() > 0) {
					mstrList = new MasterResponse();
					mstrList.setStatus(EnovationConstants.statusSuccess);
					mstrList.setStatusCode(EnovationConstants.Code200);
					mstrList.setResult(EnovationConstants.ResultTrue);
					mstrList.setReason(EnovationConstants.RECORDSFETCHSUCCESS);
					mstrList.setDeptlvlusersList(deptLvlList);
					return mstrList;
				} else {
					mstrList = new MasterResponse();
					mstrList.setStatus(EnovationConstants.statusSuccess);
					mstrList.setStatusCode(EnovationConstants.Code200);
					mstrList.setResult(EnovationConstants.ResultFalse);
					mstrList.setReason(EnovationConstants.RecordsDoesNotExist);
					return mstrList;
				}
			} else {
				mstrList = new MasterResponse();
				mstrList.setStatus(EnovationConstants.statusSuccess);
				mstrList.setStatusCode(EnovationConstants.CODE400);
				mstrList.setReason(EnovationConstants.BAD_REQUEST);
				return mstrList;
			}

		} catch (Exception e) {
			LOGGER.error("#INSIDE EXCEPTION OCCURED IN  GETLISTOFEMPLOYEEBYBRANCHWISE API :" + e.getMessage());
			mstrList = new MasterResponse();
			mstrList.setStatus(EnovationConstants.statusFail);
			mstrList.setStatusCode(EnovationConstants.Code500);
			mstrList.setResult(EnovationConstants.ResultFalse);
			mstrList.setReason("EXCEPTION OCCURED IN GETLISTOFEMPLOYEEBYBRANCHWISE API :" + e.getMessage());
			return mstrList;
		}
	}

	@Override
	public MasterResponse updateBranch(BranchMaster brnch) {
		MasterResponse mstrList = null;
		LOGGER.info("#INSIDE IN  UPDATEBRANCH API ");
		try {
			boolean updateBranch = iMasterDao.updateBranch(brnch);
			if (updateBranch) {
				mstrList = new MasterResponse();
				mstrList.setStatus(EnovationConstants.statusSuccess);
				mstrList.setStatusCode(EnovationConstants.Code200);
				mstrList.setResult(EnovationConstants.ResultTrue);
				return mstrList;
			} else {
				mstrList = new MasterResponse();
				mstrList.setStatus(EnovationConstants.statusSuccess);
				mstrList.setStatusCode(EnovationConstants.Code200);
				mstrList.setResult(EnovationConstants.ResultFalse);
				mstrList.setReason(EnovationConstants.RecordsDoesNotExist);
				return mstrList;
			}

		} catch (Exception e) {
			LOGGER.error("#INSIDE EXCEPTION OCCURED IN  UPDATEBRANCH API :" + e.getMessage());
			mstrList = new MasterResponse();
			mstrList.setStatus(EnovationConstants.statusFail);
			mstrList.setStatusCode(EnovationConstants.Code500);
			mstrList.setResult(EnovationConstants.ResultFalse);
			mstrList.setReason("EXCEPTION OCCURED IN UPDATEBRANCH API :" + e.getMessage());
			return mstrList;
		}
	}

	@Override
	public MasterResponse uploadOranizationLogo(OrganizationMaster org) {
		MasterResponse mstrList = null;
		LOGGER.info("#INSIDE IN  UPLOADORANIZATIONLOGO API ");
		try {
			OrganizationMaster orgMaster = iMasterDao.uploadOranizationLogo(org);
			if (orgMaster != null) {
				mstrList = new MasterResponse();
				mstrList.setLogoPath(orgMaster.getLogo());
				mstrList.setStatus(EnovationConstants.statusSuccess);
				mstrList.setStatusCode(EnovationConstants.Code200);
				mstrList.setResult(EnovationConstants.ResultTrue);
				return mstrList;
			} else {
				mstrList = new MasterResponse();
				mstrList.setStatus(EnovationConstants.statusSuccess);
				mstrList.setStatusCode(EnovationConstants.Code200);
				mstrList.setResult(EnovationConstants.ResultFalse);
				mstrList.setReason(EnovationConstants.RecordsDoesNotExist);
				return mstrList;
			}

		} catch (Exception e) {
			LOGGER.error("#INSIDE EXCEPTION OCCURED IN  UPLOADORANIZATIONLOGO API :" + e.getMessage());
			mstrList = new MasterResponse();
			mstrList.setStatus(EnovationConstants.statusFail);
			mstrList.setStatusCode(EnovationConstants.Code500);
			mstrList.setResult(EnovationConstants.ResultFalse);
			mstrList.setReason("EXCEPTION OCCURED IN UPLOADORANIZATIONLOGO API :" + e.getMessage());
			return mstrList;
		}
	}

	@Override
	public MasterResponse updateCategory(CategoryMaster cat) {
		MasterResponse mstrList = new MasterResponse();
		try {
			boolean category = iMasterDao.updateCategory(cat);
			if (category) {
				mstrList.setStatus(EnovationConstants.statusSuccess);
				mstrList.setStatusCode(EnovationConstants.Code200);
				mstrList.setResult(EnovationConstants.ResultTrue);
				mstrList.setReason("CATEGORY RECORD UPDATED");
				return mstrList;
			} else {
				mstrList.setStatus(EnovationConstants.statusFail);
				mstrList.setResult(EnovationConstants.ResultFalse);
				mstrList.setStatusCode(EnovationConstants.Code200);
				mstrList.setReason("FAILED TO UPDATE RECORD");
				return mstrList;
			}

		} catch (Exception e) {
			mstrList.setStatus(EnovationConstants.statusFail);
			mstrList.setStatusCode(EnovationConstants.Code500);
			mstrList.setResult(EnovationConstants.ResultFalse);
			mstrList.setReason("EXCEPTION OCCURED IN UPDATECATEGORY API :" + e.getMessage());
			return mstrList;
		}
	}

	@Override
	public MasterResponse saveStatusPoints(OrgStatusPoints orgsts) {
		MasterResponse mstrList = new MasterResponse();
		try {
			boolean saveStatusPoints = iMasterDao.saveStatusPoints(orgsts);
			if (saveStatusPoints) {
				mstrList.setStatus(EnovationConstants.statusSuccess);
				mstrList.setStatusCode(EnovationConstants.Code200);
				mstrList.setResult(EnovationConstants.ResultTrue);
				mstrList.setReason("SAVESTATUSPOINTS");
				return mstrList;
			} else {
				mstrList.setStatus(EnovationConstants.statusFail);
				mstrList.setResult(EnovationConstants.ResultFalse);
				mstrList.setStatusCode(EnovationConstants.Code200);
				mstrList.setReason("FAILED TO SAVESTATUSPOINTS");
				return mstrList;
			}

		} catch (Exception e) {
			mstrList.setStatus(EnovationConstants.statusFail);
			mstrList.setStatusCode(EnovationConstants.Code500);
			mstrList.setResult(EnovationConstants.ResultFalse);
			mstrList.setReason("EXCEPTION OCCURED IN SAVESTATUSPOINTS API :" + e.getMessage());
			return mstrList;
		}
	}

	@Override
	public MasterResponse updateStatusPoints(OrgStatusPoints orgsts) {
		MasterResponse mstrList = new MasterResponse();
		try {
			boolean updateStatusPoints = iMasterDao.updateStatusPoints(orgsts);
			if (updateStatusPoints) {
				mstrList.setStatus(EnovationConstants.statusSuccess);
				mstrList.setStatusCode(EnovationConstants.Code200);
				mstrList.setResult(EnovationConstants.ResultTrue);
				mstrList.setReason("UPDATESTATUSPOINTS");
				return mstrList;
			} else {
				mstrList.setStatus(EnovationConstants.statusFail);
				mstrList.setResult(EnovationConstants.ResultFalse);
				mstrList.setStatusCode(EnovationConstants.Code200);
				mstrList.setReason("FAILED TO UPDATESTATUSPOINTS");
				return mstrList;
			}

		} catch (Exception e) {
			mstrList.setStatus(EnovationConstants.statusFail);
			mstrList.setStatusCode(EnovationConstants.Code500);
			mstrList.setResult(EnovationConstants.ResultFalse);
			mstrList.setReason("EXCEPTION OCCURED IN UPDATESTATUSPOINTS API :" + e.getMessage());
			return mstrList;
		}
	}

	@Override
	public MasterResponse getListofStatusPoints(Integer branchId) {
		MasterResponse mstrList = null;
		LOGGER.info("#INSIDE IN  GETLISTOFSTATUSPOINTS API ");
		try {
			if (branchId != 0) {
				List<OrgStatusPoints> list = iMasterDao.getListofStatusPoints(branchId);
				if (list != null && list.size() > 0) {
					mstrList = new MasterResponse();
					mstrList.setStatus(EnovationConstants.statusSuccess);
					mstrList.setStatusCode(EnovationConstants.Code200);
					mstrList.setResult(EnovationConstants.ResultTrue);
					mstrList.setReason(EnovationConstants.RECORDSFETCHSUCCESS);
					mstrList.setOrgStatusList(list);
					return mstrList;
				} else {
					mstrList = new MasterResponse();
					mstrList.setStatus(EnovationConstants.statusSuccess);
					mstrList.setStatusCode(EnovationConstants.Code200);
					mstrList.setResult(EnovationConstants.ResultFalse);
					mstrList.setReason(EnovationConstants.RecordsDoesNotExist);
					return mstrList;
				}
			} else {
				mstrList = new MasterResponse();
				mstrList.setStatus(EnovationConstants.statusSuccess);
				mstrList.setStatusCode(EnovationConstants.CODE400);
				mstrList.setReason(EnovationConstants.BAD_REQUEST);
				return mstrList;
			}

		} catch (Exception e) {
			LOGGER.error("#INSIDE EXCEPTION OCCURED IN  GETLISTOFEMPLOYEEBYBRANCHWISE API :" + e.getMessage());
			mstrList = new MasterResponse();
			mstrList.setStatus(EnovationConstants.statusFail);
			mstrList.setStatusCode(EnovationConstants.Code500);
			mstrList.setResult(EnovationConstants.ResultFalse);
			mstrList.setReason("EXCEPTION OCCURED IN GETLISTOFEMPLOYEEBYBRANCHWISE API :" + e.getMessage());
			return mstrList;
		}
	}

	@Override
	public MasterResponse getListofStatusForBrowniePoints(Integer roleId) {
		MasterResponse mstrList = null;
		LOGGER.info("#INSIDE IN  GETLISTOFSTATUSFORBROWNIEPOINTS API ");
		try {
			List<StatusMaster> list = iMasterDao.getListofStatusForBrowniePoints(roleId);
			if (list != null && list.size() > 1) {
				mstrList = new MasterResponse();
				mstrList.setStatus(EnovationConstants.statusSuccess);
				mstrList.setStatusCode(EnovationConstants.Code200);
				mstrList.setResult(EnovationConstants.ResultTrue);
				mstrList.setReason(EnovationConstants.RECORDSFETCHSUCCESS);
				mstrList.setStatusList(list);
				return mstrList;
			} else {
				mstrList = new MasterResponse();
				mstrList.setStatus(EnovationConstants.statusSuccess);
				mstrList.setStatusCode(EnovationConstants.Code200);
				mstrList.setResult(EnovationConstants.ResultFalse);
				mstrList.setReason(EnovationConstants.RecordsDoesNotExist);
				return mstrList;
			}

		} catch (Exception e) {
			LOGGER.error("#INSIDE EXCEPTION OCCURED IN  GETLISTOFSTATUSFORBROWNIEPOINTS API :" + e.getMessage());
			mstrList = new MasterResponse();
			mstrList.setStatus(EnovationConstants.statusFail);
			mstrList.setStatusCode(EnovationConstants.Code500);
			mstrList.setResult(EnovationConstants.ResultFalse);
			mstrList.setReason("EXCEPTION OCCURED IN GETLISTOFSTATUSFORBROWNIEPOINTS API :" + e.getMessage());
			return mstrList;
		}
	}

	@Override
	public MasterResponse getRoleList() {
		MasterResponse mstrList = null;
		LOGGER.info("#INSIDE IN  getRoleList API ");
		try {
			List<RolesBean> list = iMasterDao.getRoleList();
			if (list != null && list.size() > 0) {
				mstrList = new MasterResponse();
				mstrList.setStatus(EnovationConstants.statusSuccess);
				mstrList.setStatusCode(EnovationConstants.Code200);
				mstrList.setResult(EnovationConstants.ResultTrue);
				mstrList.setReason(EnovationConstants.RECORDSFETCHSUCCESS);
				mstrList.setRoleList(list);
				return mstrList;
			} else {
				mstrList = new MasterResponse();
				mstrList.setStatus(EnovationConstants.statusSuccess);
				mstrList.setStatusCode(EnovationConstants.Code200);
				mstrList.setResult(EnovationConstants.ResultFalse);
				mstrList.setReason(EnovationConstants.RecordsDoesNotExist);
				return mstrList;
			}

		} catch (Exception e) {
			LOGGER.error("#INSIDE EXCEPTION OCCURED IN  getRoleList API :" + e.getMessage());
			mstrList = new MasterResponse();
			mstrList.setStatus(EnovationConstants.statusFail);
			mstrList.setStatusCode(EnovationConstants.Code500);
			mstrList.setResult(EnovationConstants.ResultFalse);
			mstrList.setReason("EXCEPTION OCCURED IN getRoleList API :" + e.getMessage());
			return mstrList;
		}
	}

	@Override
	public MasterResponse getCounteriesList() {
		MasterResponse mstrList = null;
		try {
			List<Countries> catList = iMasterDao.getCountriesList();
			if (!catList.isEmpty()) {
				mstrList = new MasterResponse();
				mstrList.setStatus(EnovationConstants.statusSuccess);
				mstrList.setStatusCode(EnovationConstants.Code200);
				mstrList.setResult(EnovationConstants.ResultTrue);
				mstrList.setReason(EnovationConstants.RECORDSFETCHSUCCESS);
				mstrList.setCounteryList(catList);
				return mstrList;
			} else {
				mstrList = new MasterResponse();
				mstrList.setStatus(EnovationConstants.statusSuccess);
				mstrList.setStatusCode(EnovationConstants.Code200);
				mstrList.setResult(EnovationConstants.ResultFalse);
				mstrList.setReason(EnovationConstants.RecordsDoesNotExist);
				return mstrList;
			}
		} catch (Exception e) {
			mstrList = new MasterResponse();
			mstrList.setStatus(EnovationConstants.statusFail);
			mstrList.setStatusCode(EnovationConstants.Code500);
			mstrList.setResult(EnovationConstants.ResultFalse);
			mstrList.setReason("Exception Occured in category Api :" + e.getMessage());
			return mstrList;
		}
	}

	@Override
	public MasterResponse deleteBenefit(BenefitAnalysisMaster benifit) {
		MasterResponse response = null;
		try {
			int value = iMasterDao.deleteBenifits(benifit);
			if (value == 1) {
				response = new MasterResponse();
				response.setStatus(EnovationConstants.statusSuccess);
				response.setStatusCode(EnovationConstants.Code200);
				response.setResult(EnovationConstants.ResultTrue);
				response.setReason(EnovationConstants.RECORDSFETCHSUCCESS);
			} else if (value == 2) {
				response = new MasterResponse();
				response.setStatus(EnovationConstants.statusSuccess);
				response.setStatusCode(EnovationConstants.Code200);
				response.setResult(EnovationConstants.ResultFalse);
				response.setReason(EnovationConstants.REMOVE_BENEFITS);
			}
		} catch (Exception e) {
			response = new MasterResponse();
			response.setStatus(EnovationConstants.statusFail);
			response.setStatusCode(EnovationConstants.Code100);
			response.setResult(EnovationConstants.ResultFalse);
			response.setReason("Record Already in Used, You cant Delete");
		}
		return response;

	}

	@Override
	public MasterResponse disableExecutive(Executive executive) {
		MasterResponse response = null;
		try {
			boolean value = iMasterDao.disableExecutive(executive);
			if (value) {
				response = new MasterResponse();
				response.setStatus(EnovationConstants.statusSuccess);
				response.setStatusCode(EnovationConstants.Code200);
				response.setResult(EnovationConstants.ResultTrue);
				response.setReason(EnovationConstants.RECORD_UPDATED);
			} else {
				response = new MasterResponse();
				response.setStatus(EnovationConstants.statusFail);
				response.setStatusCode(EnovationConstants.Code500);
				response.setResult(EnovationConstants.ResultFalse);
				response.setReason("Not Able to Disable");
			}
		} catch (Exception e) {
			response = new MasterResponse();
			response.setStatus(EnovationConstants.statusFail);
			response.setStatusCode(EnovationConstants.Code500);
			response.setResult(EnovationConstants.ResultFalse);
			response.setReason("Exception :" + e.getMessage());
		}
		return response;

	}

	@Override
	public MasterResponse removeBrowinePoints(OrgStatusPoints orgstatuspoints) {
		MasterResponse response = null;
		try {
			int value = iMasterDao.removeBrowinePoints(orgstatuspoints);
			if (value == 1) {
				response = new MasterResponse();
				response.setStatus(EnovationConstants.statusSuccess);
				response.setStatusCode(EnovationConstants.Code200);
				response.setResult(EnovationConstants.ResultTrue);
				response.setReason(EnovationConstants.RECORD_UPDATED);
			} else {
				response = new MasterResponse();
				response.setStatus(EnovationConstants.statusFail);
				response.setStatusCode(EnovationConstants.Code500);
				response.setResult(EnovationConstants.ResultFalse);
				response.setReason("NOT ABLE TO REMOVE");
			}
		} catch (Exception e) {
			response = new MasterResponse();
			response.setStatus(EnovationConstants.statusFail);
			response.setStatusCode(EnovationConstants.Code500);
			response.setResult(EnovationConstants.ResultFalse);
			response.setReason("EXCEPTION :" + e.getMessage().toUpperCase());
		}
		return response;
	}

	@Override
	public MasterResponse updateIsEscalation(ProductOrgConfig request) {
		MasterResponse response = null;
		try {
			boolean value = iMasterDao.updateIsEscalation(request);
			if (value) {
				response = new MasterResponse();
				response.setStatus(EnovationConstants.statusSuccess);
				response.setStatusCode(EnovationConstants.Code200);
				response.setResult(EnovationConstants.ResultTrue);
				response.setReason(EnovationConstants.RECORD_UPDATED);
			} else {
				response = new MasterResponse();
				response.setStatus(EnovationConstants.statusFail);
				response.setStatusCode(EnovationConstants.Code500);
				response.setResult(EnovationConstants.ResultFalse);
				response.setReason("NOT ABLE TO UPDATE");
			}
		} catch (Exception e) {
			response = new MasterResponse();
			response.setStatus(EnovationConstants.statusFail);
			response.setStatusCode(EnovationConstants.Code500);
			response.setResult(EnovationConstants.ResultFalse);
			response.setReason("EXCEPTION :" + e.getMessage().toUpperCase());
		}
		return response;
	}

	@Override
	public MasterResponse deleteExecutive(Executive executive) {
		MasterResponse response = null;
		try {
			boolean value = iMasterDao.deleteExecutive(executive);
			if (value) {
				response = new MasterResponse();
				response.setStatus(EnovationConstants.statusSuccess);
				response.setStatusCode(EnovationConstants.Code200);
				response.setResult(EnovationConstants.ResultTrue);
				response.setReason(EnovationConstants.RECORD_DELETED);
			} else {
				response = new MasterResponse();
				response.setStatus(EnovationConstants.statusFail);
				response.setStatusCode(EnovationConstants.Code100);
				response.setResult(EnovationConstants.ResultFalse);
				response.setReason(EnovationConstants.ALREADY_ALIGNED);
			}
		} catch (Exception e) {
			response = new MasterResponse();
			response.setStatus(EnovationConstants.statusFail);
			response.setStatusCode(EnovationConstants.Code500);
			response.setResult(EnovationConstants.ResultFalse);
			response.setReason("#INSIDE EXCEPTION OCCURED IN DELETEEXECUTIVE METHOD :" + e.getMessage());
		}
		return response;
	}

	@Override
	public MasterResponse updateLevelWiseUsers(EmployeeDetailsBean request) {
		MasterResponse mstrList = null;
		try {
			int updateLevelWiseUsers = iMasterDao.updateLevelWiseUsers(request);
			LOGGER.info("save" + updateLevelWiseUsers);
			if (updateLevelWiseUsers == 1) {
				mstrList = new MasterResponse();
				mstrList.setStatus(EnovationConstants.statusSuccess);
				mstrList.setStatusCode(EnovationConstants.Code200);
				mstrList.setResult(EnovationConstants.resultTrue);
				return mstrList;

			} else if (updateLevelWiseUsers == 2) {
				mstrList = new MasterResponse();
				mstrList.setStatus(EnovationConstants.statusFail);
				mstrList.setStatusCode(EnovationConstants.Code100);
				mstrList.setResult(EnovationConstants.ResultFalse);
				mstrList.setReason(EnovationConstants.CONTROLLER_ALIGNED);
				return mstrList;
			}

		} catch (Exception e) {
			mstrList = new MasterResponse();
			mstrList.setStatus(EnovationConstants.statusFail);
			mstrList.setStatusCode(EnovationConstants.Code500);
			mstrList.setResult(EnovationConstants.ResultFalse);
			mstrList.setReason(EnovationConstants.SERVER_EORROR);
			return mstrList;
		}
		return mstrList;

	}

	@Override
	public MasterResponse saveRoleEscalationSetup(MasterResponse request) {
		MasterResponse mstrList = null;
		try {

			// List<RoleEscalationSetup> errorList =
			// iMasterDao.checkRoleEscalationSetup(request);
			// if (errorList.isEmpty()) {
			boolean saveRoleEscalationSetup = iMasterDao.saveRoleEscalationSetup(request);

			LOGGER.info("save" + saveRoleEscalationSetup);
			if (saveRoleEscalationSetup) {
				mstrList = new MasterResponse();
				mstrList.setStatus(EnovationConstants.statusSuccess);
				mstrList.setStatusCode(EnovationConstants.Code200);
				mstrList.setResult(EnovationConstants.resultTrue);
				return mstrList;

			} else {

				mstrList = new MasterResponse();
				mstrList.setStatus(EnovationConstants.statusFail);
				mstrList.setStatusCode(EnovationConstants.CODE400);
				mstrList.setResult(EnovationConstants.ResultFalse);
				mstrList.setReason(EnovationConstants.BAD_REQUEST);
				return mstrList;
			}
			// } else {
			// mstrList = new MasterResponse();
			// mstrList.setStatus(EnovationConstants.statusFail);
			// mstrList.setStatusCode(EnovationConstants.Code100);
			// mstrList.setResult(EnovationConstants.ResultFalse);
			// mstrList.setReason("role escalation already exists");
			// mstrList.setRoleEscalationSetupList(errorList);
			// return mstrList;
			// }

		} catch (Exception e) {
			mstrList = new MasterResponse();
			mstrList.setStatus(EnovationConstants.statusFail);
			mstrList.setStatusCode(EnovationConstants.Code500);
			mstrList.setResult(EnovationConstants.ResultFalse);
			mstrList.setReason(EnovationConstants.SERVER_EORROR);
			return mstrList;
		}
	}

	@Override
	public MasterResponse updateRoleEscalationSetup(RoleEscalationSetup request) {
		MasterResponse mstrList = null;
		try {
			System.out.println("from save role escalation ***");
			boolean updateRoleEscalationSetup = iMasterDao.updateRoleEscalationSetup(request);
			LOGGER.info("save" + updateRoleEscalationSetup);
			if (updateRoleEscalationSetup) {
				mstrList = new MasterResponse();
				mstrList.setStatus(EnovationConstants.statusSuccess);
				mstrList.setStatusCode(EnovationConstants.Code200);
				mstrList.setResult(EnovationConstants.resultTrue);
				return mstrList;
			}

		} catch (Exception e) {
			mstrList = new MasterResponse();
			mstrList.setStatus(EnovationConstants.statusFail);
			mstrList.setStatusCode(EnovationConstants.Code500);
			mstrList.setResult(EnovationConstants.ResultFalse);
			mstrList.setReason(EnovationConstants.SERVER_EORROR);
			return mstrList;
		}
		return mstrList;
	}

	@Override
	public MasterResponse removeRoleEscalationSetup(RoleEscalationSetup request) {
		MasterResponse mstrList = null;
		try {
			boolean removeRoleEscalationSetup = iMasterDao.removeRoleEscalationSetup(request);
			LOGGER.info("removeRoleEscalationSetup : " + removeRoleEscalationSetup);
			if (removeRoleEscalationSetup) {
				mstrList = new MasterResponse();
				mstrList.setStatus(EnovationConstants.statusSuccess);
				mstrList.setStatusCode(EnovationConstants.Code200);
				mstrList.setResult(EnovationConstants.resultTrue);
				return mstrList;

			}

		} catch (Exception e) {
			mstrList = new MasterResponse();
			mstrList.setStatus(EnovationConstants.statusFail);
			mstrList.setStatusCode(EnovationConstants.Code500);
			mstrList.setResult(EnovationConstants.ResultFalse);
			mstrList.setReason(EnovationConstants.SERVER_EORROR);
			return mstrList;
		}
		return mstrList;
	}

	@Override
	public MasterResponse getRoleEscalationSetup(Integer branchId) {
		MasterResponse mstrList = null;
		try {
			if (branchId != 0) {
				List<RoleEscalationSetup> RoleEscalationSetupList = iMasterDao.getRoleEscalationSetup(branchId);
				LOGGER.info("removeRoleEscalationSetup" + RoleEscalationSetupList);
				if (RoleEscalationSetupList != null && RoleEscalationSetupList.size() > 0) {
					mstrList = new MasterResponse();
					mstrList.setStatus(EnovationConstants.statusSuccess);
					mstrList.setStatusCode(EnovationConstants.Code200);
					mstrList.setResult(EnovationConstants.resultTrue);
					mstrList.setRoleEscalationSetupList(RoleEscalationSetupList);
					return mstrList;

				} else {
					mstrList = new MasterResponse();
					mstrList.setStatus(EnovationConstants.statusFail);
					mstrList.setStatusCode(EnovationConstants.Code200);
					mstrList.setReason(EnovationConstants.RecordsDoesNotExist);
					mstrList.setResult(EnovationConstants.resultFalse);
					return mstrList;
				}
			} else {
				mstrList = new MasterResponse();
				mstrList.setStatus(EnovationConstants.statusFail);
				mstrList.setStatusCode(EnovationConstants.CODE400);
				mstrList.setReason(EnovationConstants.BAD_REQUEST);
				return mstrList;
			}

		} catch (Exception e) {
			mstrList = new MasterResponse();
			mstrList.setStatus(EnovationConstants.statusFail);
			mstrList.setStatusCode(EnovationConstants.Code500);
			mstrList.setResult(EnovationConstants.ResultFalse);
			mstrList.setReason(EnovationConstants.SERVER_EORROR);
			return mstrList;
		}
	}

	@Override
	public MasterResponse sendEmailsOnRegistration(Integer branchId) {
		MasterResponse mstrList = null;
		try {
			boolean sendEmailsOnRegistration = iMasterDao.sendEmailsOnRegistration(branchId);
			if (sendEmailsOnRegistration) {
				mstrList = new MasterResponse();
				mstrList.setStatus(EnovationConstants.statusSuccess);
				mstrList.setStatusCode(EnovationConstants.Code200);
				mstrList.setResult(EnovationConstants.resultTrue);
				return mstrList;

			} else {
				mstrList = new MasterResponse();
				mstrList.setStatus(EnovationConstants.statusFail);
				mstrList.setStatusCode(EnovationConstants.Code200);
				mstrList.setResult(EnovationConstants.resultFalse);
				return mstrList;
			}

		} catch (Exception e) {
			mstrList = new MasterResponse();
			mstrList.setStatus(EnovationConstants.statusFail);
			mstrList.setStatusCode(EnovationConstants.Code500);
			mstrList.setResult(EnovationConstants.ResultFalse);
			mstrList.setReason(EnovationConstants.SERVER_EORROR);
			return mstrList;
		}
	}

	@SuppressWarnings("unused")
	@Override
	public MasterResponse getOrgLogo(String alies) {
		MasterResponse mstrList = null;
		try {
			if (alies != null || alies != "") {
				String logo = iMasterDao.getOrgLogo(alies);
				if (logo != null) {
					mstrList = new MasterResponse();
					mstrList.setStatus(EnovationConstants.statusSuccess);
					mstrList.setStatusCode(EnovationConstants.Code200);
					mstrList.setResult(EnovationConstants.resultTrue);
					mstrList.setLogoPath(logo);
					return mstrList;

				} else {
					mstrList = new MasterResponse();
					mstrList.setStatus(EnovationConstants.statusFail);
					mstrList.setStatusCode(EnovationConstants.Code200);
					return mstrList;
				}
			} else {
				mstrList = new MasterResponse();
				mstrList.setStatus(EnovationConstants.statusSuccess);
				mstrList.setStatusCode(EnovationConstants.CODE400);
				mstrList.setReason(EnovationConstants.BAD_REQUEST);
				return mstrList;
			}

		} catch (Exception e) {
			mstrList = new MasterResponse();
			mstrList.setStatus(EnovationConstants.statusFail);
			mstrList.setStatusCode(EnovationConstants.Code500);
			mstrList.setReason(EnovationConstants.SERVER_EORROR);
			return mstrList;
		}
	}

	@Override
	public MasterResponse getDepartmentListForSuggestion(Integer branchId) {
		MasterResponse mstrList = null;
		try {
			if (branchId != 0) {
				List<DepartmentMaster> deptList = iMasterDao.getDepartmentListForSuggestion(branchId);
				if (deptList != null) {
					mstrList = new MasterResponse();
					mstrList.setStatus(EnovationConstants.statusSuccess);
					mstrList.setStatusCode(EnovationConstants.Code200);
					mstrList.setResult(EnovationConstants.ResultTrue);
					mstrList.setReason(EnovationConstants.RECORDSFETCHSUCCESS);
					mstrList.setDeptList(deptList);
					return mstrList;
				} else {
					mstrList = new MasterResponse();
					mstrList.setStatus(EnovationConstants.statusSuccess);
					mstrList.setStatusCode(EnovationConstants.Code200);
					mstrList.setResult(EnovationConstants.ResultFalse);
					mstrList.setReason(EnovationConstants.RecordsDoesNotExist);
					return mstrList;
				}
			} else {
				mstrList = new MasterResponse();
				mstrList.setStatus(EnovationConstants.statusSuccess);
				mstrList.setStatusCode(EnovationConstants.CODE400);
				mstrList.setReason(EnovationConstants.BAD_REQUEST);
				return mstrList;
			}

		} catch (Exception e) {
			mstrList = new MasterResponse();
			mstrList.setStatus(EnovationConstants.statusFail);
			mstrList.setStatusCode(EnovationConstants.Code500);
			mstrList.setResult(EnovationConstants.ResultFalse);
			mstrList.setReason("Exception Occured in getdepartmentlistforsuggestion Api :" + e.getMessage());
			return mstrList;
		}
	}

	@Override
	public MasterResponse updateSuggestionEscalationConfig(MasterResponse request) {
		MasterResponse mstrList = null;

		System.out.println("Printing Number of Suggestion from Service : " + request.getNumberOfSuggestion());
		System.out.println("Printing brnach ID from Service : " + request.getBranchId());
		try {

			boolean saveSuggestionEscalationSetup = iMasterDao.createSuggestionEscalationConfig(request);

			LOGGER.info("Inside AddSuggestionEscalation : " + saveSuggestionEscalationSetup);
			if (saveSuggestionEscalationSetup) {
				mstrList = new MasterResponse();
				mstrList.setStatus(EnovationConstants.statusSuccess);
				mstrList.setStatusCode(EnovationConstants.Code200);
				mstrList.setResult(EnovationConstants.resultTrue);
				return mstrList;

			} else {

				mstrList = new MasterResponse();
				mstrList.setStatus(EnovationConstants.statusFail);
				mstrList.setStatusCode(EnovationConstants.CODE400);
				mstrList.setResult(EnovationConstants.ResultFalse);
				mstrList.setReason(EnovationConstants.BAD_REQUEST);
				return mstrList;
			}

		} catch (Exception e) {
			mstrList = new MasterResponse();
			mstrList.setStatus(EnovationConstants.statusFail);
			mstrList.setStatusCode(EnovationConstants.Code500);
			mstrList.setResult(EnovationConstants.ResultFalse);
			mstrList.setReason(EnovationConstants.SERVER_EORROR);
			return mstrList;
		}

		// MasterResponse mstrList = null;
		// try {
		// if (request.getId() == 0) {
		// if (request.getBranchMaster() != null) {
		// List<SuggestionEscalationConfig> checkBranch = iMasterDao
		// .checkBranchInSuggestionEscalationConfig(request.getBranchMaster().getBranchId());
		// if (checkBranch != null && checkBranch.size() > 0) {
		// mstrList = new MasterResponse();
		// mstrList.setStatus(EnovationConstants.statusFail);
		// mstrList.setStatusCode(EnovationConstants.Code100);
		// mstrList.setResult(EnovationConstants.resultFalse);
		// mstrList.setReason(EnovationConstants.ALREADY_EXITS);
		// return mstrList;
		// } else {
		// MasterResponse response = createSuggestionEscalationConfig(request);
		// return response;
		// }
		// } else {
		// mstrList = new MasterResponse();
		// mstrList.setStatus(EnovationConstants.statusFail);
		// mstrList.setStatusCode(EnovationConstants.CODE400);
		// mstrList.setResult(EnovationConstants.resultFalse);
		// mstrList.setReason(EnovationConstants.BAD_REQUEST);
		// return mstrList;
		// }
		// } else {
		// boolean update = iMasterDao.updateSuggestionEscalationConfig(request);
		// LOGGER.info("update" + update);
		// if (update) {
		// mstrList = new MasterResponse();
		// mstrList.setStatus(EnovationConstants.statusSuccess);
		// mstrList.setStatusCode(EnovationConstants.Code200);
		// mstrList.setResult(EnovationConstants.resultTrue);
		// mstrList.setReason(EnovationConstants.RECORD_UPDATED);
		// return mstrList;
		//
		// } else {
		// mstrList = new MasterResponse();
		// mstrList.setStatus(EnovationConstants.statusFail);
		// mstrList.setStatusCode(EnovationConstants.Code200);
		// return mstrList;
		// }
		// }
		//
		// } catch (Exception e) {
		// mstrList = new MasterResponse();
		// mstrList.setStatus(EnovationConstants.statusFail);
		// mstrList.setStatusCode(EnovationConstants.Code500);
		// mstrList.setReason(EnovationConstants.SERVER_EORROR);
		// return mstrList;
		// }
		//
		// }
		//
		// public MasterResponse
		// createSuggestionEscalationConfig(SuggestionEscalationConfig request) {
		// MasterResponse mstrList = null;
		// boolean update = iMasterDao.createSuggestionEscalationConfig(request);
		// LOGGER.info("update" + update);
		// if (update) {
		// mstrList = new MasterResponse();
		// mstrList.setStatus(EnovationConstants.statusSuccess);
		// mstrList.setStatusCode(EnovationConstants.Code200);
		// mstrList.setResult(EnovationConstants.resultTrue);
		// mstrList.setReason(EnovationConstants.RECORD_ADDED);
		// return mstrList;
		//
		// } else {
		// mstrList = new MasterResponse();
		// mstrList.setStatus(EnovationConstants.statusFail);
		// mstrList.setStatusCode(EnovationConstants.Code200);
		// return mstrList;
		// }
	}

	@Override
	public MasterResponse removeSuggestionEscalationConfig(SuggestionEscalationConfig request) {
		MasterResponse mstrList = null;
		try {
			boolean remove = iMasterDao.removeSuggestionEscalationConfig(request);
			LOGGER.info("removeSuggestionEscalationConfig" + remove);
			if (remove) {
				mstrList = new MasterResponse();
				mstrList.setStatus(EnovationConstants.statusSuccess);
				mstrList.setStatusCode(EnovationConstants.Code200);
				mstrList.setResult(EnovationConstants.resultTrue);
				mstrList.setReason(EnovationConstants.RECORDS_REMOVED);
				return mstrList;

			} else {
				mstrList = new MasterResponse();
				mstrList.setStatus(EnovationConstants.statusFail);
				mstrList.setStatusCode(EnovationConstants.Code200);
				return mstrList;
			}

		} catch (Exception e) {
			mstrList = new MasterResponse();
			mstrList.setStatus(EnovationConstants.statusFail);
			mstrList.setStatusCode(EnovationConstants.Code500);
			mstrList.setResult(EnovationConstants.ResultFalse);
			mstrList.setReason(EnovationConstants.SERVER_EORROR);
			return mstrList;
		}
	}

	@Override
	public MasterResponse getSuggestionEscalationConfig(Integer branchId) {
		MasterResponse mstrList = null;
		try {
			if (branchId != 0) {
				List<SuggestionEscalationConfig> SuggestionEscalationConfig = iMasterDao
						.getSuggestionEscalationConfig(branchId);
				LOGGER.info("GETSUGGESTIONESCALATIONCONFIG : " + SuggestionEscalationConfig);
				if (SuggestionEscalationConfig != null && SuggestionEscalationConfig.size() > 0) {
					mstrList = new MasterResponse();
					mstrList.setStatus(EnovationConstants.statusSuccess);
					mstrList.setStatusCode(EnovationConstants.Code200);
					mstrList.setResult(EnovationConstants.resultTrue);
					mstrList.setReason(EnovationConstants.RECORDSFETCHSUCCESS);
					mstrList.setSuggestionEscalationConfigList(SuggestionEscalationConfig);
					return mstrList;

				} else {
					mstrList = new MasterResponse();
					mstrList.setStatus(EnovationConstants.statusFail);
					mstrList.setStatusCode(EnovationConstants.Code200);
					mstrList.setReason(EnovationConstants.RecordsDoesNotExist);
					mstrList.setResult(EnovationConstants.resultFalse);
					return mstrList;
				}
			} else {
				mstrList = new MasterResponse();
				mstrList.setStatus(EnovationConstants.statusFail);
				mstrList.setStatusCode(EnovationConstants.CODE400);
				mstrList.setReason(EnovationConstants.BAD_REQUEST);
				return mstrList;
			}

		} catch (Exception e) {
			mstrList = new MasterResponse();
			mstrList.setStatus(EnovationConstants.statusFail);
			mstrList.setStatusCode(EnovationConstants.Code500);
			mstrList.setResult(EnovationConstants.ResultFalse);
			mstrList.setReason(EnovationConstants.SERVER_EORROR);
			return mstrList;
		}
	}

	@Override
	public MasterResponse getBrowniePointsReimbursementCycleTypeList() {
		MasterResponse mstrList = null;
		try {
			List<BrowniePointsReimbursementCycleType> BrowniePointsReimbursementCycleTypeList = iMasterDao
					.getBrowniePointsReimbursementCycleTypeList();
			LOGGER.info("BrowniePointsReimbursementCycleTypeList : " + BrowniePointsReimbursementCycleTypeList);
			if (BrowniePointsReimbursementCycleTypeList != null && BrowniePointsReimbursementCycleTypeList.size() > 0) {
				mstrList = new MasterResponse();
				mstrList.setStatus(EnovationConstants.statusSuccess);
				mstrList.setStatusCode(EnovationConstants.Code200);
				mstrList.setResult(EnovationConstants.resultTrue);
				mstrList.setReason(EnovationConstants.RECORDSFETCHSUCCESS);
				mstrList.setBrowniePointsReimbursementCycleTypeList(BrowniePointsReimbursementCycleTypeList);
				return mstrList;

			} else {
				mstrList = new MasterResponse();
				mstrList.setStatus(EnovationConstants.statusFail);
				mstrList.setStatusCode(EnovationConstants.Code200);
				mstrList.setReason(EnovationConstants.RecordsDoesNotExist);
				mstrList.setResult(EnovationConstants.resultFalse);
				return mstrList;
			}

		} catch (Exception e) {
			mstrList = new MasterResponse();
			mstrList.setStatus(EnovationConstants.statusFail);
			mstrList.setStatusCode(EnovationConstants.Code500);
			mstrList.setResult(EnovationConstants.ResultFalse);
			mstrList.setReason(EnovationConstants.SERVER_EORROR);
			return mstrList;
		}
	}

	@Override
	public MasterResponse getBrowniePointsReimbursementCycle(Integer branchId) {
		MasterResponse mstrList = null;
		try {
			if (branchId != 0) {
				BrowniePointsReimbursementCycle BrowniePointsReimbursementCycle = iMasterDao
						.getBrowniePointsReimbursementCycle(branchId);
				LOGGER.info("BrowniePointsReimbursementCycle : " + BrowniePointsReimbursementCycle);
				if (BrowniePointsReimbursementCycle != null) {
					mstrList = new MasterResponse();
					mstrList.setStatus(EnovationConstants.statusSuccess);
					mstrList.setStatusCode(EnovationConstants.Code200);
					mstrList.setResult(EnovationConstants.resultTrue);
					mstrList.setReason(EnovationConstants.RECORDSFETCHSUCCESS);
					mstrList.setBrowniePointsReimbursementCycleDetails(BrowniePointsReimbursementCycle);
					return mstrList;

				} else {
					mstrList = new MasterResponse();
					mstrList.setStatus(EnovationConstants.statusFail);
					mstrList.setStatusCode(EnovationConstants.Code200);
					mstrList.setReason(EnovationConstants.RecordsDoesNotExist);
					mstrList.setResult(EnovationConstants.resultFalse);
					return mstrList;
				}
			} else {
				mstrList = new MasterResponse();
				mstrList.setStatus(EnovationConstants.statusSuccess);
				mstrList.setStatusCode(EnovationConstants.CODE400);
				mstrList.setReason(EnovationConstants.BAD_REQUEST);
				return mstrList;
			}

		} catch (Exception e) {
			mstrList = new MasterResponse();
			mstrList.setStatus(EnovationConstants.statusFail);
			mstrList.setStatusCode(EnovationConstants.Code500);
			mstrList.setResult(EnovationConstants.ResultFalse);
			mstrList.setReason(EnovationConstants.SERVER_EORROR);
			return mstrList;
		}
	}

	@Override
	public MasterResponse updateBrowniePointsReimbursementCycle(BrowniePointsReimbursementCycle request) {
		MasterResponse mstrList = null;
		try {
			boolean remove = iMasterDao.updateBrowniePointsReimbursementCycle(request);
			LOGGER.info("UPDATEBROWNIEPOINTSREIMBURSEMENTCYCLE" + remove);
			if (remove) {
				mstrList = new MasterResponse();
				mstrList.setStatus(EnovationConstants.statusSuccess);
				mstrList.setStatusCode(EnovationConstants.Code200);
				mstrList.setResult(EnovationConstants.resultTrue);
				mstrList.setReason(EnovationConstants.RECORD_UPDATED);
				return mstrList;

			} else {
				mstrList = new MasterResponse();
				mstrList.setStatus(EnovationConstants.statusFail);
				mstrList.setStatusCode(EnovationConstants.Code200);
				return mstrList;
			}

		} catch (Exception e) {
			mstrList = new MasterResponse();
			mstrList.setStatus(EnovationConstants.statusFail);
			mstrList.setStatusCode(EnovationConstants.Code500);
			mstrList.setResult(EnovationConstants.ResultFalse);
			mstrList.setReason(EnovationConstants.SERVER_EORROR);
			return mstrList;
		}
	}

	@Override
	public MasterResponse getParticulars(Integer catId) {
		MasterResponse mstrList = null;
		try {
			if (catId != 0) {
				List<Particulars> particulars = iMasterDao.getParticulars(catId);
				LOGGER.info("particulars : " + particulars);
				if (particulars != null && particulars.size() > 0) {
					mstrList = new MasterResponse();
					mstrList.setStatus(EnovationConstants.statusSuccess);
					mstrList.setStatusCode(EnovationConstants.Code200);
					mstrList.setResult(EnovationConstants.resultTrue);
					mstrList.setReason(EnovationConstants.RECORDSFETCHSUCCESS);
					mstrList.setParticulars(particulars);
					return mstrList;

				} else {
					mstrList = new MasterResponse();
					mstrList.setStatus(EnovationConstants.statusFail);
					mstrList.setStatusCode(EnovationConstants.Code200);
					mstrList.setReason(EnovationConstants.RecordsDoesNotExist);
					mstrList.setResult(EnovationConstants.resultFalse);
					return mstrList;
				}
			} else {
				mstrList = new MasterResponse();
				mstrList.setStatus(EnovationConstants.statusSuccess);
				mstrList.setStatusCode(EnovationConstants.CODE400);
				mstrList.setReason(EnovationConstants.BAD_REQUEST);
				return mstrList;
			}

		} catch (Exception e) {
			mstrList = new MasterResponse();
			mstrList.setStatus(EnovationConstants.statusFail);
			mstrList.setStatusCode(EnovationConstants.Code500);
			mstrList.setResult(EnovationConstants.ResultFalse);
			mstrList.setReason(EnovationConstants.SERVER_EORROR);
			return mstrList;
		}
	}

	@Override
	public MasterResponse updateParticulars(List<Particulars> req) {
		MasterResponse mstrList = null;
		try {
			ArrayList<Particulars> errorList = new ArrayList<Particulars>();
			System.out.println("list in  : " + errorList.size());
			/**
			 * for (Particulars request : req) { if (request.getId() == 0) { if
			 * (request.getCategory() != null) { List<Particulars> checkCategory =
			 * iMasterDao .checkCategoryInParticulars(request.getCategory().getCatId(),
			 * request.getParticulars()); errorList.addAll(checkCategory); } else { mstrList
			 * = new MasterResponse(); mstrList.setStatus(EnovationConstants.statusFail);
			 * mstrList.setStatusCode(EnovationConstants.CODE400);
			 * mstrList.setResult(EnovationConstants.resultFalse);
			 * mstrList.setReason(EnovationConstants.BAD_REQUEST); return mstrList; } } }
			 **/
			System.out.println("size : " + errorList.size());
			if (errorList != null && errorList.size() > 0) {
				mstrList = new MasterResponse();
				mstrList.setStatus(EnovationConstants.statusFail);
				mstrList.setStatusCode(EnovationConstants.Code100);
				mstrList.setResult(EnovationConstants.resultFalse);
				mstrList.setReason(EnovationConstants.ALREADY_EXITS);
				mstrList.setParticulars(errorList);
				return mstrList;
			} else {
				if (req.get(0).getId() != 0) {
					boolean update = iMasterDao.updateParticular(req);
					LOGGER.info("update" + update);
					if (update) {
						mstrList = new MasterResponse();
						mstrList.setStatus(EnovationConstants.statusSuccess);
						mstrList.setStatusCode(EnovationConstants.Code200);
						mstrList.setResult(EnovationConstants.resultTrue);
						mstrList.setReason(EnovationConstants.RECORD_UPDATED);
						return mstrList;

					} else {
						mstrList = new MasterResponse();
						mstrList.setStatus(EnovationConstants.statusFail);
						mstrList.setStatusCode(EnovationConstants.Code200);
						return mstrList;
					}

				} else {
					MasterResponse response = createParticular(req);
					return response;
				}

			}

		} catch (Exception e) {
			mstrList = new MasterResponse();
			mstrList.setStatus(EnovationConstants.statusFail);
			mstrList.setStatusCode(EnovationConstants.Code500);
			mstrList.setReason(EnovationConstants.SERVER_EORROR);
			return mstrList;
		}
	}

	private MasterResponse createParticular(List<Particulars> req) {
		MasterResponse mstrList = null;
		boolean createParticular = iMasterDao.createParticular(req);
		LOGGER.info("createParticular " + createParticular);
		if (createParticular) {
			mstrList = new MasterResponse();
			mstrList.setStatus(EnovationConstants.statusSuccess);
			mstrList.setStatusCode(EnovationConstants.Code200);
			mstrList.setResult(EnovationConstants.resultTrue);
			mstrList.setReason(EnovationConstants.RECORD_ADDED);
			return mstrList;

		} else {
			mstrList = new MasterResponse();
			mstrList.setStatus(EnovationConstants.statusFail);
			mstrList.setStatusCode(EnovationConstants.Code200);
			return mstrList;
		}
	}

	@Override
	public MasterResponse removeParticulars(Particulars particulars) {
		MasterResponse mstrList = null;
		try {
			int value = iMasterDao.removeParticulars(particulars);
			if (value == 1) {
				mstrList = new MasterResponse();
				mstrList.setStatus(EnovationConstants.statusSuccess);
				mstrList.setResult(EnovationConstants.resultTrue);
				mstrList.setStatusCode(EnovationConstants.Code200);
				return mstrList;
			} else {
				mstrList = new MasterResponse();
				mstrList.setStatus(EnovationConstants.statusFail);
				mstrList.setStatusCode(EnovationConstants.Code200);
				mstrList.setResult(EnovationConstants.resultFalse);
				return mstrList;
			}
		} catch (Exception e) {
			mstrList = new MasterResponse();
			mstrList.setStatus(EnovationConstants.cantdeleteFail);
			mstrList.setStatusCode(EnovationConstants.Code100);
			mstrList.setResult(EnovationConstants.ResultFalse);
			mstrList.setReason(EnovationConstants.PARTICULARS_ALREADY_IN_USE);
			return mstrList;
		}
	}

	@Override
	public MasterResponse getSavingOptionByBranchId(Integer branchId) {
		MasterResponse mstrList = null;
		try {
			if (branchId != 0) {
				List<SavingOptions> savingOption = iMasterDao.getSavingOptionByBranchId(branchId);
				LOGGER.info("savingOption : " + savingOption);
				if (savingOption != null && savingOption.size() > 0) {
					mstrList = new MasterResponse();
					mstrList.setStatus(EnovationConstants.statusSuccess);
					mstrList.setStatusCode(EnovationConstants.Code200);
					mstrList.setResult(EnovationConstants.resultTrue);
					mstrList.setReason(EnovationConstants.RECORDSFETCHSUCCESS);
					mstrList.setSavingOption(savingOption);
					return mstrList;

				} else {
					mstrList = new MasterResponse();
					mstrList.setStatus(EnovationConstants.statusFail);
					mstrList.setStatusCode(EnovationConstants.Code200);
					mstrList.setReason(EnovationConstants.RecordsDoesNotExist);
					mstrList.setResult(EnovationConstants.resultFalse);
					return mstrList;
				}
			} else {
				mstrList = new MasterResponse();
				mstrList.setStatus(EnovationConstants.statusSuccess);
				mstrList.setStatusCode(EnovationConstants.CODE400);
				mstrList.setReason(EnovationConstants.BAD_REQUEST);
				return mstrList;
			}

		} catch (Exception e) {
			mstrList = new MasterResponse();
			mstrList.setStatus(EnovationConstants.statusFail);
			mstrList.setStatusCode(EnovationConstants.Code500);
			mstrList.setResult(EnovationConstants.ResultFalse);
			mstrList.setReason(EnovationConstants.SERVER_EORROR);
			return mstrList;
		}
	}

	@Override
	public MasterResponse getSavingOptionRole(Integer savingOptionsId) {
		MasterResponse mstrList = null;
		try {
			if (savingOptionsId != 0) {
				List<SavingOptionsRole> savingOptionRole = iMasterDao.getSavingOptionRole(savingOptionsId);
				LOGGER.info("savingOptionRole : " + savingOptionRole);
				if (savingOptionRole != null && savingOptionRole.size() > 0) {
					mstrList = new MasterResponse();
					mstrList.setStatus(EnovationConstants.statusSuccess);
					mstrList.setStatusCode(EnovationConstants.Code200);
					mstrList.setResult(EnovationConstants.resultTrue);
					mstrList.setReason(EnovationConstants.RECORDSFETCHSUCCESS);
					mstrList.setSavingOptionRole(savingOptionRole);
					return mstrList;

				} else {
					mstrList = new MasterResponse();
					mstrList.setStatus(EnovationConstants.statusFail);
					mstrList.setStatusCode(EnovationConstants.Code200);
					mstrList.setReason(EnovationConstants.RecordsDoesNotExist);
					mstrList.setResult(EnovationConstants.resultFalse);
					return mstrList;
				}
			} else {
				mstrList = new MasterResponse();
				mstrList.setStatus(EnovationConstants.statusSuccess);
				mstrList.setStatusCode(EnovationConstants.CODE400);
				mstrList.setReason(EnovationConstants.BAD_REQUEST);
				return mstrList;
			}

		} catch (Exception e) {
			mstrList = new MasterResponse();
			mstrList.setStatus(EnovationConstants.statusFail);
			mstrList.setStatusCode(EnovationConstants.Code500);
			mstrList.setResult(EnovationConstants.ResultFalse);
			mstrList.setReason(EnovationConstants.SERVER_EORROR);
			return mstrList;
		}
	}

	@Override
	public MasterResponse getCurrencyList() {
		MasterResponse mstrList = null;
		try {
			List<Currency> currencyList = iMasterDao.getCurrencyList();
			LOGGER.info("currencyList : " + currencyList);
			if (currencyList != null && currencyList.size() > 0) {
				mstrList = new MasterResponse();
				mstrList.setStatus(EnovationConstants.statusSuccess);
				mstrList.setStatusCode(EnovationConstants.Code200);
				mstrList.setResult(EnovationConstants.resultTrue);
				mstrList.setReason(EnovationConstants.RECORDSFETCHSUCCESS);
				mstrList.setCurrencyList(currencyList);
				return mstrList;

			} else {
				mstrList = new MasterResponse();
				mstrList.setStatus(EnovationConstants.statusFail);
				mstrList.setStatusCode(EnovationConstants.Code200);
				mstrList.setReason(EnovationConstants.RecordsDoesNotExist);
				mstrList.setResult(EnovationConstants.resultFalse);
				return mstrList;
			}

		} catch (Exception e) {
			mstrList = new MasterResponse();
			mstrList.setStatus(EnovationConstants.statusFail);
			mstrList.setStatusCode(EnovationConstants.Code500);
			mstrList.setResult(EnovationConstants.ResultFalse);
			mstrList.setReason(EnovationConstants.SERVER_EORROR);
			return mstrList;
		}
	}

	@Override
	public MasterResponse getPointsForSaving(Integer branchId) {
		MasterResponse mstrList = null;
		try {
			if (branchId != 0) {
				List<SetPointsForSaving> setPointsForSavingList = iMasterDao.getPointsForSaving(branchId, 0, 0);
				LOGGER.info("setPointsForSavingListByBranch : " + setPointsForSavingList);
				if (setPointsForSavingList != null && setPointsForSavingList.size() > 0) {
					mstrList = new MasterResponse();
					mstrList.setStatus(EnovationConstants.statusSuccess);
					mstrList.setStatusCode(EnovationConstants.Code200);
					mstrList.setResult(EnovationConstants.resultTrue);
					mstrList.setReason(EnovationConstants.RECORDSFETCHSUCCESS);
					mstrList.setSetPointsForSavingList(setPointsForSavingList);
					return mstrList;

				} else {
					mstrList = new MasterResponse();
					mstrList.setStatus(EnovationConstants.statusFail);
					mstrList.setStatusCode(EnovationConstants.Code200);
					mstrList.setReason(EnovationConstants.RecordsDoesNotExist);
					mstrList.setResult(EnovationConstants.resultFalse);
					return mstrList;
				}
			} else {
				mstrList = new MasterResponse();
				mstrList.setStatus(EnovationConstants.statusSuccess);
				mstrList.setStatusCode(EnovationConstants.CODE400);
				mstrList.setReason(EnovationConstants.BAD_REQUEST);
				return mstrList;
			}

		} catch (Exception e) {
			mstrList = new MasterResponse();
			mstrList.setStatus(EnovationConstants.statusFail);
			mstrList.setStatusCode(EnovationConstants.Code500);
			mstrList.setResult(EnovationConstants.ResultFalse);
			mstrList.setReason(EnovationConstants.SERVER_EORROR);
			return mstrList;
		}
	}

	@Override
	public MasterResponse getPointsForSavingBySavingOptionsId(Integer savingOptionsId, Integer roleId) {
		MasterResponse mstrList = null;
		try {
			if (savingOptionsId != 0) {
				List<SetPointsForSaving> setPointsForSavingList = iMasterDao.getPointsForSaving(0, savingOptionsId,
						roleId);
				LOGGER.info("setPointsForSavingListBySavingOptionsId : " + setPointsForSavingList);
				if (setPointsForSavingList != null && setPointsForSavingList.size() > 0) {
					mstrList = new MasterResponse();
					mstrList.setStatus(EnovationConstants.statusSuccess);
					mstrList.setStatusCode(EnovationConstants.Code200);
					mstrList.setResult(EnovationConstants.resultTrue);
					mstrList.setReason(EnovationConstants.RECORDSFETCHSUCCESS);
					mstrList.setSetPointsForSavingList(setPointsForSavingList);
					return mstrList;

				} else {
					mstrList = new MasterResponse();
					mstrList.setStatus(EnovationConstants.statusFail);
					mstrList.setStatusCode(EnovationConstants.Code200);
					mstrList.setReason(EnovationConstants.RecordsDoesNotExist);
					mstrList.setResult(EnovationConstants.resultFalse);
					return mstrList;
				}
			} else {
				mstrList = new MasterResponse();
				mstrList.setStatus(EnovationConstants.statusSuccess);
				mstrList.setStatusCode(EnovationConstants.CODE400);
				mstrList.setReason(EnovationConstants.BAD_REQUEST);
				return mstrList;
			}

		} catch (Exception e) {
			mstrList = new MasterResponse();
			mstrList.setStatus(EnovationConstants.statusFail);
			mstrList.setStatusCode(EnovationConstants.Code500);
			mstrList.setResult(EnovationConstants.ResultFalse);
			mstrList.setReason(EnovationConstants.SERVER_EORROR);
			return mstrList;
		}
	}

	@Override
	public MasterResponse getPointsForSavingBySavingOptionsRoleId(Integer savingOptionsRoleId, Integer savingOptionsId,
			Integer roleId) {
		MasterResponse mstrList = null;
		try {
			if (savingOptionsRoleId != 0) {
				List<SetPointsForSaving> setPointsForSavingList = iMasterDao.getPointsForSavingByRoleId(0,
						savingOptionsRoleId, savingOptionsId, roleId);
				LOGGER.info("setPointsForSavingListBySavingOptionsRoleId : " + setPointsForSavingList);
				if (setPointsForSavingList != null && setPointsForSavingList.size() > 0) {
					mstrList = new MasterResponse();
					mstrList.setStatus(EnovationConstants.statusSuccess);
					mstrList.setStatusCode(EnovationConstants.Code200);
					mstrList.setResult(EnovationConstants.resultTrue);
					mstrList.setReason(EnovationConstants.RECORDSFETCHSUCCESS);
					mstrList.setSetPointsForSavingList(setPointsForSavingList);
					return mstrList;

				} else {
					mstrList = new MasterResponse();
					mstrList.setStatus(EnovationConstants.statusFail);
					mstrList.setStatusCode(EnovationConstants.Code200);
					mstrList.setReason(EnovationConstants.RecordsDoesNotExist);
					mstrList.setResult(EnovationConstants.resultFalse);
					return mstrList;
				}
			} else {
				mstrList = new MasterResponse();
				mstrList.setStatus(EnovationConstants.statusSuccess);
				mstrList.setStatusCode(EnovationConstants.CODE400);
				mstrList.setReason(EnovationConstants.BAD_REQUEST);
				return mstrList;
			}

		} catch (Exception e) {
			mstrList = new MasterResponse();
			mstrList.setStatus(EnovationConstants.statusFail);
			mstrList.setStatusCode(EnovationConstants.Code500);
			mstrList.setResult(EnovationConstants.ResultFalse);
			mstrList.setReason(EnovationConstants.SERVER_EORROR);
			return mstrList;
		}
	}

	@Override
	public MasterResponse createOrUpdateSavingOptions(SavingOptions savingOptions) {
		MasterResponse mstrList = null;
		try {
			if (savingOptions.getId() != 0) {
				boolean update = iMasterDao.updateSavingOptions(savingOptions);
				LOGGER.info("update" + update);
				if (update) {
					mstrList = new MasterResponse();
					mstrList.setStatus(EnovationConstants.statusSuccess);
					mstrList.setStatusCode(EnovationConstants.Code200);
					mstrList.setResult(EnovationConstants.resultTrue);
					mstrList.setReason(EnovationConstants.RECORD_UPDATED);
					return mstrList;

				} else {
					mstrList = new MasterResponse();
					mstrList.setStatus(EnovationConstants.statusFail);
					mstrList.setStatusCode(EnovationConstants.CODE400);
					mstrList.setReason(EnovationConstants.BAD_REQUEST);
					return mstrList;
				}

			} else {
				List<SavingOptions> checkSavingOptionsExists = iMasterDao.checkSavingOptionsExists(savingOptions);
				if (checkSavingOptionsExists != null && checkSavingOptionsExists.size() > 0) {
					mstrList = new MasterResponse();
					mstrList.setStatus(EnovationConstants.statusFail);
					mstrList.setStatusCode(EnovationConstants.Code100);
					mstrList.setResult(EnovationConstants.resultFalse);
					mstrList.setReason(EnovationConstants.ALREADY_EXITS);
					mstrList.setSavingOption(checkSavingOptionsExists);
					return mstrList;
				} else {
					MasterResponse response = createSavingOptions(savingOptions);
					return response;
				}
			}

		} catch (Exception e) {
			mstrList = new MasterResponse();
			mstrList.setStatus(EnovationConstants.statusFail);
			mstrList.setStatusCode(EnovationConstants.Code500);
			mstrList.setReason(EnovationConstants.SERVER_EORROR);
			return mstrList;
		}
	}

	private MasterResponse createSavingOptions(SavingOptions savingOptions) {
		MasterResponse mstrList = null;
		boolean createSavingOptions = iMasterDao.createSavingOptions(savingOptions);
		LOGGER.info("savingOptions " + createSavingOptions);
		if (createSavingOptions) {
			mstrList = new MasterResponse();
			mstrList.setStatus(EnovationConstants.statusSuccess);
			mstrList.setStatusCode(EnovationConstants.Code200);
			mstrList.setResult(EnovationConstants.resultTrue);
			mstrList.setReason(EnovationConstants.RECORD_ADDED);
			return mstrList;
		} else {
			mstrList = new MasterResponse();
			mstrList.setStatus(EnovationConstants.statusFail);
			mstrList.setStatusCode(EnovationConstants.Code200);
			return mstrList;
		}
	}

	@Override
	public MasterResponse createOrUpdateSavingOptionsRole(SavingOptionsRole savingOptionsRole) {
		MasterResponse mstrList = null;
		try {
			if (savingOptionsRole.getId() != 0) {
				boolean update = iMasterDao.updateSavingOptionsRole(savingOptionsRole);
				LOGGER.info("updateSavingOptionsRole " + update);
				if (update) {
					mstrList = new MasterResponse();
					mstrList.setStatus(EnovationConstants.statusSuccess);
					mstrList.setStatusCode(EnovationConstants.Code200);
					mstrList.setResult(EnovationConstants.resultTrue);
					mstrList.setReason(EnovationConstants.RECORD_UPDATED);
					return mstrList;

				} else {
					mstrList = new MasterResponse();
					mstrList.setStatus(EnovationConstants.statusFail);
					mstrList.setStatusCode(EnovationConstants.CODE400);
					mstrList.setReason(EnovationConstants.BAD_REQUEST);
					return mstrList;
				}

			} else {
				List<SavingOptionsRole> checkSavingOptionsExists = iMasterDao
						.checkSavingOptionsExists(savingOptionsRole);
				if (checkSavingOptionsExists != null && checkSavingOptionsExists.size() > 0) {
					mstrList = new MasterResponse();
					mstrList.setStatus(EnovationConstants.statusFail);
					mstrList.setStatusCode(EnovationConstants.Code100);
					mstrList.setResult(EnovationConstants.resultFalse);
					mstrList.setReason(EnovationConstants.ALREADY_EXITS);
					mstrList.setSavingOptionRole(checkSavingOptionsExists);
					return mstrList;
				} else {
					MasterResponse response = createSavingOptionsRole(savingOptionsRole);
					return response;
				}
			}

		} catch (Exception e) {
			mstrList = new MasterResponse();
			mstrList.setStatus(EnovationConstants.statusFail);
			mstrList.setStatusCode(EnovationConstants.Code500);
			mstrList.setReason(EnovationConstants.SERVER_EORROR);
			return mstrList;
		}
	}

	private MasterResponse createSavingOptionsRole(SavingOptionsRole savingOptionsRole) {
		MasterResponse mstrList = null;
		boolean createSavingOptionsRole = iMasterDao.createSavingOptionsRole(savingOptionsRole);
		LOGGER.info("savingOptionsRole " + createSavingOptionsRole);
		if (createSavingOptionsRole) {
			mstrList = new MasterResponse();
			mstrList.setStatus(EnovationConstants.statusSuccess);
			mstrList.setStatusCode(EnovationConstants.Code200);
			mstrList.setResult(EnovationConstants.resultTrue);
			mstrList.setReason(EnovationConstants.RECORD_ADDED);
			return mstrList;
		} else {
			mstrList = new MasterResponse();
			mstrList.setStatus(EnovationConstants.statusFail);
			mstrList.setStatusCode(EnovationConstants.Code200);
			return mstrList;
		}
	}

	@Override
	public MasterResponse createOrUpdateSetPointsForSaving(List<SetPointsForSaving> setPointsForSaving) {
		MasterResponse mstrList = null;
		try {
			ArrayList<SetPointsForSaving> errorList = new ArrayList<SetPointsForSaving>();
			System.out.println("list in  : " + errorList.size());
			for (SetPointsForSaving request : setPointsForSaving) {
				if (request.getId() == 0) {
					if (request.getSavingOptions() != null && request.getSavingOptionsRole() != null
							&& request.getBranch() != null) {
						List<SetPointsForSaving> checkSetPointsForSaving = iMasterDao
								.checkSetPointsForSavingExists(request);
						errorList.addAll(checkSetPointsForSaving);
					} else {
						mstrList = new MasterResponse();
						mstrList.setStatus(EnovationConstants.statusFail);
						mstrList.setStatusCode(EnovationConstants.CODE400);
						mstrList.setResult(EnovationConstants.resultFalse);
						mstrList.setReason(EnovationConstants.BAD_REQUEST);
						return mstrList;
					}
				}
			}
			System.out.println("size : " + errorList.size());
			if (errorList != null && errorList.size() > 0) {
				mstrList = new MasterResponse();
				mstrList.setStatus(EnovationConstants.statusFail);
				mstrList.setStatusCode(EnovationConstants.Code100);
				mstrList.setResult(EnovationConstants.resultFalse);
				mstrList.setReason(EnovationConstants.ALREADY_EXITS);
				mstrList.setSetPointsForSavingList(errorList);
				return mstrList;
			} else {
				if (setPointsForSaving.get(0).getId() != 0) {
					boolean update = iMasterDao.updateSetPointsForSaving(setPointsForSaving);
					LOGGER.info("update" + update);
					if (update) {
						mstrList = new MasterResponse();
						mstrList.setStatus(EnovationConstants.statusSuccess);
						mstrList.setStatusCode(EnovationConstants.Code200);
						mstrList.setResult(EnovationConstants.resultTrue);
						mstrList.setReason(EnovationConstants.RECORD_UPDATED);
						return mstrList;

					} else {
						mstrList = new MasterResponse();
						mstrList.setStatus(EnovationConstants.statusFail);
						mstrList.setStatusCode(EnovationConstants.Code200);
						return mstrList;
					}

				} else {
					MasterResponse response = createSetPointsForSaving(setPointsForSaving);
					return response;
				}

			}

		} catch (Exception e) {
			mstrList = new MasterResponse();
			mstrList.setStatus(EnovationConstants.statusFail);
			mstrList.setStatusCode(EnovationConstants.Code500);
			mstrList.setReason(EnovationConstants.SERVER_EORROR);
			return mstrList;
		}
	}

	private MasterResponse createSetPointsForSaving(List<SetPointsForSaving> setPointsForSaving) {
		MasterResponse mstrList = null;
		boolean createSetPointsForSaving = iMasterDao.createSetPointsForSaving(setPointsForSaving);
		LOGGER.info("savingOptionsRole " + createSetPointsForSaving);
		if (createSetPointsForSaving) {
			mstrList = new MasterResponse();
			mstrList.setStatus(EnovationConstants.statusSuccess);
			mstrList.setStatusCode(EnovationConstants.Code200);
			mstrList.setResult(EnovationConstants.resultTrue);
			mstrList.setReason(EnovationConstants.RECORD_ADDED);
			return mstrList;
		} else {
			mstrList = new MasterResponse();
			mstrList.setStatus(EnovationConstants.statusFail);
			mstrList.setStatusCode(EnovationConstants.Code200);
			return mstrList;
		}
	}

	@Override
	public MasterResponse addOrupdateDocument(MasterDocument document) {
		MasterResponse mstrList = null;
		int save = iMasterDao.addOrupdateDocument(document);
		try {
			if (save == EnovationConstants.ZERO) {
				mstrList = new MasterResponse();
				mstrList.setStatus(EnovationConstants.statusSuccess);
				mstrList.setStatusCode(EnovationConstants.Code200);
				mstrList.setResult(EnovationConstants.resultTrue);
				mstrList.setReason(EnovationConstants.RECORD_ADDED);
				return mstrList;
			} else if (save == EnovationConstants.ONE) {
				mstrList = new MasterResponse();
				mstrList.setStatus(EnovationConstants.statusFail);
				mstrList.setStatusCode(EnovationConstants.Code100);
				mstrList.setResult(EnovationConstants.ResultFalse);
				mstrList.setReason(EnovationConstants.DOCUMENT_PENDING_ALIGNED);
				return mstrList;
			} else {
				mstrList = new MasterResponse();
				mstrList.setStatus(EnovationConstants.statusFail);
				mstrList.setStatusCode(EnovationConstants.Code200);
				mstrList.setResult(EnovationConstants.ResultFalse);
				mstrList.setReason(EnovationConstants.RECORD_NOT_ADDED);
				return mstrList;
			}
		} catch (Exception e) {
			mstrList = new MasterResponse();
			mstrList.setStatus(EnovationConstants.statusFail);
			mstrList.setStatusCode(EnovationConstants.Code500);
			mstrList.setResult(EnovationConstants.ResultFalse);
			mstrList.setReason("Exception Occured in addOrupdateDocument  Api :" + e.getMessage());
			return mstrList;
		}
	}

	@Override
	public MasterResponse deleteDocument(MasterDocument document) {
		MasterResponse mstrList = null;
		int delete = iMasterDao.deleteDocument(document);
		try {
			if (delete == EnovationConstants.ZERO) {
				mstrList = new MasterResponse();
				mstrList.setStatus(EnovationConstants.statusSuccess);
				mstrList.setStatusCode(EnovationConstants.Code200);
				mstrList.setResult(EnovationConstants.resultTrue);
				mstrList.setReason(EnovationConstants.RECORD_DELETED);
				return mstrList;
			} else if (delete == EnovationConstants.ONE) {
				mstrList = new MasterResponse();
				mstrList.setStatus(EnovationConstants.statusFail);
				mstrList.setStatusCode(EnovationConstants.Code100);
				mstrList.setResult(EnovationConstants.ResultFalse);
				mstrList.setReason(EnovationConstants.REMOVE_DOCUMENT);
				return mstrList;
			} else {
				mstrList = new MasterResponse();
				mstrList.setStatus(EnovationConstants.statusFail);
				mstrList.setStatusCode(EnovationConstants.Code200);
				mstrList.setResult(EnovationConstants.ResultFalse);
				mstrList.setReason(EnovationConstants.statusFail);
				return mstrList;
			}
		} catch (Exception e) {
			mstrList = new MasterResponse();
			mstrList.setStatus(EnovationConstants.statusFail);
			mstrList.setStatusCode(EnovationConstants.Code500);
			mstrList.setResult(EnovationConstants.ResultFalse);
			mstrList.setReason("Exception Occured in addOrupdateDocument  Api :" + e.getMessage());
			return mstrList;
		}

	}

	@Override
	public MasterResponse removeSavingOptionsRole(SavingOptionsRole savingOptionsRole) {
		MasterResponse mstrList = null;
		try {
			int value = iMasterDao.removeSavingOptionsRole(savingOptionsRole);
			if (value == 1) {
				mstrList = new MasterResponse();
				mstrList.setStatus(EnovationConstants.statusSuccess);
				mstrList.setResult(EnovationConstants.resultTrue);
				mstrList.setStatusCode(EnovationConstants.Code200);
				return mstrList;
			} else {
				mstrList = new MasterResponse();
				mstrList.setStatus(EnovationConstants.statusFail);
				mstrList.setStatusCode(EnovationConstants.Code200);
				mstrList.setResult(EnovationConstants.resultFalse);
				return mstrList;
			}
		} catch (Exception e) {
			mstrList = new MasterResponse();
			mstrList.setStatus(EnovationConstants.cantdeleteFail);
			mstrList.setStatusCode(EnovationConstants.Code100);
			mstrList.setResult(EnovationConstants.ResultFalse);
			mstrList.setReason(EnovationConstants.PROCESS_ALREADY_IN_USE);
			return mstrList;
		}
	}

	@Override
	public MasterResponse removeSetPointsForSaving(SetPointsForSaving setPointsForSaving) {
		MasterResponse mstrList = null;
		try {
			int value = iMasterDao.removeSetPointsForSaving(setPointsForSaving);
			if (value == 1) {
				mstrList = new MasterResponse();
				mstrList.setStatus(EnovationConstants.statusSuccess);
				mstrList.setResult(EnovationConstants.resultTrue);
				mstrList.setStatusCode(EnovationConstants.Code200);
				return mstrList;
			} else {
				mstrList = new MasterResponse();
				mstrList.setStatus(EnovationConstants.statusFail);
				mstrList.setStatusCode(EnovationConstants.Code200);
				mstrList.setResult(EnovationConstants.resultFalse);
				return mstrList;
			}
		} catch (Exception e) {
			mstrList = new MasterResponse();
			mstrList.setStatus(EnovationConstants.cantdeleteFail);
			mstrList.setStatusCode(EnovationConstants.Code100);
			mstrList.setResult(EnovationConstants.ResultFalse);
			mstrList.setReason(EnovationConstants.PROCESS_ALREADY_IN_USE);
			return mstrList;
		}
	}

	@Override
	public MasterResponse getActivityList() {
		MasterResponse mstrList = null;
		List<ActivityMaster> activity = iMasterDao.getActivityList();
		try {
			if (activity != null && activity.size() > 0) {
				mstrList = new MasterResponse();
				mstrList.setStatus(EnovationConstants.statusSuccess);
				mstrList.setStatusCode(EnovationConstants.Code200);
				mstrList.setResult(EnovationConstants.ResultTrue);
				mstrList.setReason(EnovationConstants.RECORDSFETCHSUCCESS);
				mstrList.setActivityList(activity);
				return mstrList;
			} else {

				mstrList = new MasterResponse();
				mstrList.setStatus(EnovationConstants.statusSuccess);
				mstrList.setStatusCode(EnovationConstants.Code200);
				mstrList.setResult(EnovationConstants.ResultFalse);
				mstrList.setReason(EnovationConstants.RecordsDoesNotExist);
				return mstrList;
			}

		} catch (Exception e) {
			mstrList = new MasterResponse();
			mstrList.setStatus(EnovationConstants.statusFail);
			mstrList.setStatusCode(EnovationConstants.Code500);
			mstrList.setResult(EnovationConstants.ResultFalse);
			mstrList.setReason("Exception Occured in getDocumentListByBranchId  Api :" + e.getMessage());
			return mstrList;
		}
	}

	@Override
	public MasterResponse getBranchListNew(Integer orgId) {
		MasterResponse mstrList = null;
		List<Branch> branch = iMasterDao.getBranchListNEW(orgId);
		try {
			if (branch != null && branch.size() > 0) {
				branch.stream().forEach(b -> {
					branch.stream().forEach(y -> {
						if (y.getBranchId() == b.getBranchId()) {
							if (b.getBranchId() > 0 && b.getDeptId() > 0 && b.getDeptName() != null) {
								DepartmentList d = new DepartmentList(b.getDeptId(), b.getDeptName());
								y.getDepartmentList().add(d);
							}
						}
					});
				});
				List<Branch> list = branch.stream().collect(collectingAndThen(
						toCollection(() -> new TreeSet<>(comparingInt(Branch::getBranchId))), ArrayList::new));
				mstrList = new MasterResponse();
				mstrList.setStatus(EnovationConstants.statusSuccess);
				mstrList.setStatusCode(EnovationConstants.Code200);
				mstrList.setResult(EnovationConstants.ResultTrue);
				mstrList.setReason(EnovationConstants.RECORDSFETCHSUCCESS);
				mstrList.setBranch(list);
				return mstrList;
			} else {

				mstrList = new MasterResponse();
				mstrList.setStatus(EnovationConstants.statusSuccess);
				mstrList.setStatusCode(EnovationConstants.Code200);
				mstrList.setResult(EnovationConstants.ResultFalse);
				mstrList.setReason(EnovationConstants.RecordsDoesNotExist);
				return mstrList;
			}

		} catch (Exception e) {
			e.printStackTrace();
			mstrList = new MasterResponse();
			mstrList.setStatus(EnovationConstants.statusFail);
			mstrList.setStatusCode(EnovationConstants.Code500);
			mstrList.setResult(EnovationConstants.ResultFalse);
			mstrList.setReason("Exception Occured in getDocumentListByBranchId  Api :" + e.getMessage());
			return mstrList;
		}
	}

	@Override
	public MasterResponse getBranchDetails(Integer branchId) {
		MasterResponse mstrList = null;
		List<Branch> branch = iMasterDao.getBranchDetails(branchId);
		try {
			if (branch != null && branch.size() > 0) {
				branch.stream().forEach(b -> {
					branch.stream().forEach(y -> {
						if (y.getBranchId() == b.getBranchId()) {
							if (b.getBranchId() > 0 && b.getDeptId() > 0 && b.getDeptName() != null) {
								DepartmentList d = new DepartmentList(b.getDeptId(), b.getDeptName());
								y.getDepartmentList().add(d);
							}
						}
					});
				});
				List<Branch> list = branch.stream().collect(collectingAndThen(
						toCollection(() -> new TreeSet<>(comparingInt(Branch::getBranchId))), ArrayList::new));
				mstrList = new MasterResponse();
				mstrList.setStatus(EnovationConstants.statusSuccess);
				mstrList.setStatusCode(EnovationConstants.Code200);
				mstrList.setResult(EnovationConstants.ResultTrue);
				mstrList.setReason(EnovationConstants.RECORDSFETCHSUCCESS);
				mstrList.setBranch(list);
				return mstrList;
			} else {

				mstrList = new MasterResponse();
				mstrList.setStatus(EnovationConstants.statusSuccess);
				mstrList.setStatusCode(EnovationConstants.Code200);
				mstrList.setResult(EnovationConstants.ResultFalse);
				mstrList.setReason(EnovationConstants.RecordsDoesNotExist);
				return mstrList;
			}

		} catch (Exception e) {
			e.printStackTrace();
			mstrList = new MasterResponse();
			mstrList.setStatus(EnovationConstants.statusFail);
			mstrList.setStatusCode(EnovationConstants.Code500);
			mstrList.setResult(EnovationConstants.ResultFalse);
			mstrList.setReason("Exception Occured in getDocumentListByBranchId  Api :" + e.getMessage());
			return mstrList;
		}
	}
	
	@Override
	public MasterResponse getLanguageList() {
		MasterResponse mstrList = null;
		// List<ActivityMaster> activity = iMasterDao.getActivityList();
		try {

			List<LanguageMaster> langList = iMasterDao.getLanguageList();
			if (langList != null && langList.size() > 0) {
				mstrList = new MasterResponse();
				mstrList.setStatus(EnovationConstants.statusSuccess);
				mstrList.setStatusCode(EnovationConstants.Code200);
				mstrList.setResult(EnovationConstants.ResultTrue);
				mstrList.setReason(EnovationConstants.RECORDSFETCHSUCCESS);
				mstrList.setLangList(langList);
				return mstrList;
			} else {

				mstrList = new MasterResponse();
				mstrList.setStatus(EnovationConstants.statusSuccess);
				mstrList.setStatusCode(EnovationConstants.Code200);
				mstrList.setResult(EnovationConstants.ResultFalse);
				mstrList.setReason(EnovationConstants.RecordsDoesNotExist);
				return mstrList;
			}

		} catch (Exception e) {
			mstrList = new MasterResponse();
			mstrList.setStatus(EnovationConstants.statusFail);
			mstrList.setStatusCode(EnovationConstants.Code500);
			mstrList.setResult(EnovationConstants.ResultFalse);
			mstrList.setReason("Exception Occured in getGetLanguage  Api :" + e.getMessage());
			return mstrList;
		}
	}

	@Override
	public MasterResponse deleteEscalator(RoleEscalationSetup request) {

		MasterResponse mstrList = null;
		try {

			boolean escalator = iMasterDao.deleteEscalator(request);
			// System.out.println("save" + oraganization);
			if (escalator) {
				mstrList = new MasterResponse();
				mstrList.setStatus(EnovationConstants.statusSuccess);
				mstrList.setStatusCode(EnovationConstants.Code200);
				mstrList.setResult(EnovationConstants.resultTrue);
				return mstrList;
			} else {
				mstrList = new MasterResponse();
				mstrList.setStatus(EnovationConstants.statusFail);
				mstrList.setStatusCode(EnovationConstants.Code200);
				mstrList.setResult(EnovationConstants.resultFalse);
				return mstrList;
			}
		} catch (Exception e) {
			mstrList = new MasterResponse();
			mstrList.setStatus(EnovationConstants.statusFail);
			mstrList.setStatusCode(EnovationConstants.Code500);
			mstrList.setResult(EnovationConstants.ResultFalse);
			mstrList.setReason("Exception Occured in Delete Escalator Api :" + e.getMessage());
			return mstrList;
		}
	}

	@Override
	public MasterResponse saveRewards(Rewards rewards) {
		MasterResponse mstrList = null;
		try {
			boolean isSave = iMasterDao.saveRewards(rewards);
			if (isSave) {
				mstrList = new MasterResponse();
				mstrList.setStatus(EnovationConstants.statusSuccess);
				mstrList.setStatusCode(EnovationConstants.Code200);
				mstrList.setResult(EnovationConstants.resultTrue);
				return mstrList;
			} else {
				mstrList = new MasterResponse();
				mstrList.setStatus(EnovationConstants.statusFail);
				mstrList.setStatusCode(EnovationConstants.Code200);
				mstrList.setResult(EnovationConstants.resultFalse);
				return mstrList;
			}
		} catch (Exception e) {
			mstrList = new MasterResponse();
			mstrList.setStatus(EnovationConstants.statusFail);
			mstrList.setStatusCode(EnovationConstants.Code500);
			mstrList.setResult(EnovationConstants.ResultFalse);
			mstrList.setReason("Exception Occured in save Rewards Api :" + e.getMessage());
			return mstrList;
		}

	}

	@Override
	public MasterResponse getRewardsList(Integer branchId) {
		MasterResponse mstrList = null;
		try {
			List<Rewards> rewardList = iMasterDao.getRewardsList(branchId);
			if (!rewardList.isEmpty()) {
				mstrList = new MasterResponse();
				mstrList.setStatus(EnovationConstants.statusSuccess);
				mstrList.setStatusCode(EnovationConstants.Code200);
				mstrList.setResult(EnovationConstants.ResultTrue);
				mstrList.setReason(EnovationConstants.RECORDSFETCHSUCCESS);
				mstrList.setRewardsList(rewardList);
				return mstrList;
			} else {

				mstrList = new MasterResponse();
				mstrList.setStatus(EnovationConstants.statusSuccess);
				mstrList.setStatusCode(EnovationConstants.Code200);
				mstrList.setResult(EnovationConstants.ResultFalse);
				mstrList.setReason(EnovationConstants.RecordsDoesNotExist);
				return mstrList;
			}

		} catch (Exception e) {
			mstrList = new MasterResponse();
			mstrList.setStatus(EnovationConstants.statusFail);
			mstrList.setStatusCode(EnovationConstants.Code500);
			mstrList.setResult(EnovationConstants.ResultFalse);
			mstrList.setReason("Exception Occured in get rewards list Api :" + e.getMessage());
			return mstrList;
		}

	}

	@Override
	public MasterResponse updateRewards(Rewards rewards) {
		MasterResponse mstrList = null;
		try {
			boolean isSave = iMasterDao.updateRewards(rewards);
			if (isSave) {
				mstrList = new MasterResponse();
				mstrList.setStatus(EnovationConstants.statusSuccess);
				mstrList.setStatusCode(EnovationConstants.Code200);
				mstrList.setResult(EnovationConstants.resultTrue);
				return mstrList;
			} else {
				mstrList = new MasterResponse();
				mstrList.setStatus(EnovationConstants.statusSuccess);
				mstrList.setStatusCode(EnovationConstants.Code200);
				mstrList.setResult(EnovationConstants.resultFalse);
				return mstrList;
			}
		} catch (Exception e) {
			mstrList = new MasterResponse();
			mstrList.setStatus(EnovationConstants.statusFail);
			mstrList.setStatusCode(EnovationConstants.Code500);
			mstrList.setResult(EnovationConstants.ResultFalse);
			mstrList.setReason("Exception Occured in update Rewards Api :" + e.getMessage());
			return mstrList;
		}

	}

	@Override
	public MasterResponse deleteRewards(Rewards rewards) {
		MasterResponse mstrList = null;
		try {
			boolean isDelete = iMasterDao.deleteRewards(rewards);
			if (isDelete) {
				mstrList = new MasterResponse();
				mstrList.setStatus(EnovationConstants.statusSuccess);
				mstrList.setStatusCode(EnovationConstants.Code200);
				mstrList.setResult(EnovationConstants.resultTrue);
				return mstrList;
			} else {
				mstrList = new MasterResponse();
				mstrList.setStatus(EnovationConstants.statusSuccess);
				mstrList.setStatusCode(EnovationConstants.Code200);
				mstrList.setResult(EnovationConstants.resultFalse);
				return mstrList;
			}
		} catch (Exception e) {
			mstrList = new MasterResponse();
			mstrList.setStatus(EnovationConstants.statusFail);
			mstrList.setStatusCode(EnovationConstants.Code500);
			mstrList.setResult(EnovationConstants.ResultFalse);
			mstrList.setReason("Exception Occured in delete Rewards Api :" + e.getMessage());
			return mstrList;
		}

	}

	@Override
	public MasterResponse saveVendorDetails(Vendor vendor) {
		MasterResponse mstrList = null;
		try {
			boolean isSave = iMasterDao.saveVendorDetails(vendor);
			if (isSave) {
				mstrList = new MasterResponse();
				mstrList.setStatus(EnovationConstants.statusSuccess);
				mstrList.setStatusCode(EnovationConstants.Code200);
				mstrList.setResult(EnovationConstants.resultTrue);
				return mstrList;
			} else {
				mstrList = new MasterResponse();
				mstrList.setStatus(EnovationConstants.statusSuccess);
				mstrList.setStatusCode(EnovationConstants.Code200);
				mstrList.setResult(EnovationConstants.resultFalse);
				return mstrList;
			}
		} catch (Exception e) {
			mstrList = new MasterResponse();
			mstrList.setStatus(EnovationConstants.statusFail);
			mstrList.setStatusCode(EnovationConstants.Code500);
			mstrList.setResult(EnovationConstants.ResultFalse);
			mstrList.setReason("Exception Occured in save vendor Details Api :" + e.getMessage());
			return mstrList;
		}

	}

	@Override
	public MasterResponse updateVendorDetails(Vendor ven) {
		MasterResponse mstrList = null;
		try {
			boolean isUpdate = iMasterDao.updateVendorDetails(ven);
			if (isUpdate) {
				mstrList = new MasterResponse();
				mstrList.setStatus(EnovationConstants.statusSuccess);
				mstrList.setStatusCode(EnovationConstants.Code200);
				mstrList.setResult(EnovationConstants.resultTrue);
				return mstrList;
			} else {
				mstrList = new MasterResponse();
				mstrList.setStatus(EnovationConstants.statusSuccess);
				mstrList.setStatusCode(EnovationConstants.Code200);
				mstrList.setResult(EnovationConstants.resultFalse);
				return mstrList;
			}
		} catch (Exception e) {
			mstrList = new MasterResponse();
			mstrList.setStatus(EnovationConstants.statusFail);
			mstrList.setStatusCode(EnovationConstants.Code500);
			mstrList.setResult(EnovationConstants.ResultFalse);
			mstrList.setReason("Exception Occured in update vendor details Api :" + e.getMessage());
			return mstrList;
		}

	}

	@Override
	public MasterResponse deleteVendorDetails(Vendor vendor) {
		MasterResponse mstrList = null;
		try {
			boolean isDelete = iMasterDao.deleteVendorDetails(vendor);
			if (isDelete) {
				mstrList = new MasterResponse();
				mstrList.setStatus(EnovationConstants.statusSuccess);
				mstrList.setStatusCode(EnovationConstants.Code200);
				mstrList.setResult(EnovationConstants.resultTrue);
				return mstrList;
			} else {
				mstrList = new MasterResponse();
				mstrList.setStatus(EnovationConstants.statusSuccess);
				mstrList.setStatusCode(EnovationConstants.Code200);
				mstrList.setResult(EnovationConstants.resultFalse);
				return mstrList;
			}
		} catch (Exception e) {
			mstrList = new MasterResponse();
			mstrList.setStatus(EnovationConstants.statusFail);
			mstrList.setStatusCode(EnovationConstants.Code500);
			mstrList.setResult(EnovationConstants.ResultFalse);
			mstrList.setReason("EXCEPTION OCCURED IN DELETE VENDOR DETAILS API :" + e.getMessage());
			return mstrList;
		}
	}

	@Override
	public MasterResponse getVendorDetails(Integer branchId) {
		MasterResponse mstrList = null;
		try {
			List<Vendor> vendorList = iMasterDao.getVendorDetails(branchId);
			if (!vendorList.isEmpty()) {
				mstrList = new MasterResponse();
				mstrList.setStatus(EnovationConstants.statusSuccess);
				mstrList.setStatusCode(EnovationConstants.Code200);
				mstrList.setResult(EnovationConstants.ResultTrue);
				mstrList.setReason(EnovationConstants.RECORDSFETCHSUCCESS);
				mstrList.setVendorDetails(vendorList);
				return mstrList;
			} else {

				mstrList = new MasterResponse();
				mstrList.setStatus(EnovationConstants.statusSuccess);
				mstrList.setStatusCode(EnovationConstants.Code200);
				mstrList.setResult(EnovationConstants.ResultFalse);
				mstrList.setReason(EnovationConstants.RecordsDoesNotExist);
				return mstrList;
			}

		} catch (Exception e) {
			mstrList = new MasterResponse();
			mstrList.setStatus(EnovationConstants.statusFail);
			mstrList.setStatusCode(EnovationConstants.Code500);
			mstrList.setResult(EnovationConstants.ResultFalse);
			mstrList.setReason("Exception Occured in get vendor Details list Api :" + e.getMessage());
			return mstrList;
		}
	}

	@Override
	public MasterResponse addRewardCategory(RewardCategoryMaster mrc) {
		MasterResponse mstrList = null;
		try {
			boolean isSave = iMasterDao.addRewardCategory(mrc);
			if (isSave) {
				mstrList = new MasterResponse();
				mstrList.setStatus(EnovationConstants.statusSuccess);
				mstrList.setStatusCode(EnovationConstants.Code200);
				mstrList.setResult(EnovationConstants.resultTrue);
				return mstrList;
			} else {
				mstrList = new MasterResponse();
				mstrList.setStatus(EnovationConstants.statusSuccess);
				mstrList.setStatusCode(EnovationConstants.Code200);
				mstrList.setResult(EnovationConstants.resultFalse);
				return mstrList;
			}
		} catch (Exception e) {
			mstrList = new MasterResponse();
			mstrList.setStatus(EnovationConstants.statusFail);
			mstrList.setStatusCode(EnovationConstants.Code500);
			mstrList.setResult(EnovationConstants.ResultFalse);
			mstrList.setReason("EXCEPTION OCCURED IN ADD REWARD CATEGORY API :" + e.getMessage());
			return mstrList;
		}
	}

	@Override
	public MasterResponse addRewardResponsibleData(RewardResponsibleData rrd) {
		MasterResponse mstrList = null;
		try {
			boolean isSave = iMasterDao.addRewardResponsibleData(rrd);
			if (isSave) {
				mstrList = new MasterResponse();
				mstrList.setStatus(EnovationConstants.statusSuccess);
				mstrList.setStatusCode(EnovationConstants.Code200);
				mstrList.setResult(EnovationConstants.resultTrue);
				return mstrList;
			} else {
				mstrList = new MasterResponse();
				mstrList.setStatus(EnovationConstants.statusSuccess);
				mstrList.setStatusCode(EnovationConstants.Code200);
				mstrList.setResult(EnovationConstants.resultFalse);
				return mstrList;
			}
		} catch (Exception e) {
			mstrList = new MasterResponse();
			mstrList.setStatus(EnovationConstants.statusFail);
			mstrList.setStatusCode(EnovationConstants.Code500);
			mstrList.setResult(EnovationConstants.ResultFalse);
			mstrList.setReason("EXCEPTION OCCURED IN ADD REWARD RESPONSIBLE DATA API :" + e.getMessage());
			return mstrList;
		}
	}

	@Override
	public MasterResponse updateRewardResponsibleData(RewardResponsibleData rrd) {
		MasterResponse mstrList = null;
		try {
			boolean isSave = iMasterDao.updateRewardResponsibleData(rrd);
			if (isSave) {
				mstrList = new MasterResponse();
				mstrList.setStatus(EnovationConstants.statusSuccess);
				mstrList.setStatusCode(EnovationConstants.Code200);
				mstrList.setResult(EnovationConstants.resultTrue);
				return mstrList;
			} else {
				mstrList = new MasterResponse();
				mstrList.setStatus(EnovationConstants.statusFail);
				mstrList.setStatusCode(EnovationConstants.Code200);
				mstrList.setResult(EnovationConstants.resultFalse);
				return mstrList;
			}
		} catch (Exception e) {
			mstrList = new MasterResponse();
			mstrList.setStatus(EnovationConstants.statusFail);
			mstrList.setStatusCode(EnovationConstants.Code500);
			mstrList.setResult(EnovationConstants.ResultFalse);
			mstrList.setReason("EXCEPTION OCCURED IN UPDATE REWARD RESPONSIBLE DATA API :" + e.getMessage());
			return mstrList;
		}
	}

	@Override
	public MasterResponse deleteRewardResponsibleData(RewardResponsibleData rrd) {
		MasterResponse mstrList = null;
		try {
			boolean isSave = iMasterDao.deleteRewardResponsibleData(rrd);
			if (isSave) {
				mstrList = new MasterResponse();
				mstrList.setStatus(EnovationConstants.statusSuccess);
				mstrList.setStatusCode(EnovationConstants.Code200);
				mstrList.setResult(EnovationConstants.resultTrue);
				return mstrList;
			} else {
				mstrList = new MasterResponse();
				mstrList.setStatus(EnovationConstants.statusSuccess);
				mstrList.setStatusCode(EnovationConstants.Code200);
				mstrList.setResult(EnovationConstants.resultFalse);
				return mstrList;
			}
		} catch (Exception e) {
			mstrList = new MasterResponse();
			mstrList.setStatus(EnovationConstants.statusFail);
			mstrList.setStatusCode(EnovationConstants.Code500);
			mstrList.setResult(EnovationConstants.ResultFalse);
			mstrList.setReason("EXCEPTION OCCURED IN DELETE REWARD RESPONSIBLE DATA API :" + e.getMessage());
			return mstrList;
		}
	}
	
	@Override
	public MasterResponse addToCart(Cart cart) {
		MasterResponse mstrList = null;
		try {
			boolean isSave = iMasterDao.addToCart(cart);
			if (isSave) {
				mstrList = new MasterResponse();
				mstrList.setStatus(EnovationConstants.statusSuccess);
				mstrList.setStatusCode(EnovationConstants.Code200);
				mstrList.setResult(EnovationConstants.resultTrue);
				return mstrList;
			} else {
				mstrList = new MasterResponse();
				mstrList.setStatus(EnovationConstants.statusSuccess);
				mstrList.setStatusCode(EnovationConstants.Code200);
				mstrList.setResult(EnovationConstants.resultFalse);
				return mstrList;
			}
		} catch (Exception e) {
			mstrList = new MasterResponse();
			mstrList.setStatus(EnovationConstants.statusFail);
			mstrList.setStatusCode(EnovationConstants.Code500);
			mstrList.setResult(EnovationConstants.ResultFalse);
			mstrList.setReason("EXCEPTION OCCURED IN ADD TO CART API :" + e.getMessage());
			return mstrList;
		}
	}

	@Override
	public MasterResponse getCartList(Integer empId) {
		MasterResponse mstrList = null;
		try {
			List<Cart> cartList = iMasterDao.getCartList(empId);
			if (!cartList.isEmpty()) {
				mstrList = new MasterResponse();
				mstrList.setStatus(EnovationConstants.statusSuccess);
				mstrList.setStatusCode(EnovationConstants.Code200);
				mstrList.setResult(EnovationConstants.ResultTrue);
				mstrList.setReason(EnovationConstants.RECORDSFETCHSUCCESS);
				mstrList.setCartList(cartList);
				return mstrList;
			} else {

				mstrList = new MasterResponse();
				mstrList.setStatus(EnovationConstants.statusSuccess);
				mstrList.setStatusCode(EnovationConstants.Code200);
				mstrList.setResult(EnovationConstants.ResultFalse);
				mstrList.setReason(EnovationConstants.RecordsDoesNotExist);
				return mstrList;
			}

		} catch (Exception e) {
			mstrList = new MasterResponse();
			mstrList.setStatus(EnovationConstants.statusFail);
			mstrList.setStatusCode(EnovationConstants.Code500);
			mstrList.setResult(EnovationConstants.ResultFalse);
			mstrList.setReason("EXCEPTION OCCURED IN GET CART LIST API :" + e.getMessage());
			return mstrList;
		}
	}

	@Override
	public MasterResponse deleteFromCart(Cart cart) {
		MasterResponse mstrList = null;
		try {
			boolean isDelete = iMasterDao.deleteFromCart(cart);
			if (isDelete) {
				mstrList = new MasterResponse();
				mstrList.setStatus(EnovationConstants.statusSuccess);
				mstrList.setStatusCode(EnovationConstants.Code200);
				mstrList.setResult(EnovationConstants.resultTrue);
				return mstrList;
			} else {
				mstrList = new MasterResponse();
				mstrList.setStatus(EnovationConstants.statusSuccess);
				mstrList.setStatusCode(EnovationConstants.Code200);
				mstrList.setResult(EnovationConstants.resultFalse);
				return mstrList;
			}
		} catch (Exception e) {
			mstrList = new MasterResponse();
			mstrList.setStatus(EnovationConstants.statusFail);
			mstrList.setStatusCode(EnovationConstants.Code500);
			mstrList.setResult(EnovationConstants.ResultFalse);
			mstrList.setReason("EXCEPTION OCCURED IN DELETE FROM CART API :" + e.getMessage());
			return mstrList;
		}
	}

	@Override
	public MasterResponse redeemGiftFromCart(List<RedeemCartRewards> redeemGiftList) {
		MasterResponse mstrList = null;
		try {
			int isRedeem = iMasterDao.redeemFromCart(redeemGiftList);
			if (isRedeem == EnovationConstants.ONE) {
				mstrList = new MasterResponse();
				mstrList.setStatus(EnovationConstants.statusSuccess);
				mstrList.setStatusCode(EnovationConstants.Code200);
				mstrList.setResult(EnovationConstants.resultTrue);
				return mstrList;
			} else if (isRedeem == EnovationConstants.TWO) {
				mstrList = new MasterResponse();
				mstrList.setStatus(EnovationConstants.statusFail);
				mstrList.setStatusCode(EnovationConstants.Code200);
				mstrList.setResult(EnovationConstants.resultFalse);
				mstrList.setReason("Insufficient Points");
				return mstrList;
			} else {
				mstrList = new MasterResponse();
				mstrList.setStatus(EnovationConstants.statusFail);
				mstrList.setStatusCode(EnovationConstants.CODE400);
				mstrList.setResult(EnovationConstants.resultFalse);
				mstrList.setReason(EnovationConstants.BAD_REQUEST);
				return mstrList;
			}
		} catch (Exception e) {
			mstrList = new MasterResponse();
			mstrList.setStatus(EnovationConstants.statusFail);
			mstrList.setStatusCode(EnovationConstants.Code500);
			mstrList.setResult(EnovationConstants.ResultFalse);
			mstrList.setReason("EXCEPTION OCCURED IN REDEEM GIFT FROM CART API :" + e.getMessage());
			return mstrList;
		}
	}

	@Override
	public MasterResponse saveRewardResponsibleSetup(RewardResponsibleSetup rrsetup) {
		MasterResponse mstrList = null;
		boolean isRedeem = false;
		try {
			RewardResponsibleSetup updateRecord = iMasterDao.checkRewardResponsiblePerson(rrsetup);
			if (updateRecord != null) {
				updateRecord.setIsEnable(EnovationConstants.ONE);
				isRedeem = iMasterDao.deleteRewardResponsibleSetup(updateRecord);
			} else {
				isRedeem = iMasterDao.saveRewardResponsibleSetup(rrsetup);
			}
			if (isRedeem) {
				mstrList = new MasterResponse();
				mstrList.setStatus(EnovationConstants.statusSuccess);
				mstrList.setStatusCode(EnovationConstants.Code200);
				mstrList.setResult(EnovationConstants.resultTrue);
				return mstrList;
			} else {
				mstrList = new MasterResponse();
				mstrList.setStatus(EnovationConstants.statusSuccess);
				mstrList.setStatusCode(EnovationConstants.Code200);
				mstrList.setResult(EnovationConstants.resultFalse);
				return mstrList;
			}
		} catch (Exception e) {
			mstrList = new MasterResponse();
			mstrList.setStatus(EnovationConstants.statusFail);
			mstrList.setStatusCode(EnovationConstants.Code500);
			mstrList.setResult(EnovationConstants.ResultFalse);
			mstrList.setReason("EXCEPTION OCCURED IN SAVE REWARD RESPONSIBLE SETUP API :" + e.getMessage());
			return mstrList;
		}
	}

	@Override
	public MasterResponse updateRewardResponsibleSetup(RewardResponsibleSetup rrsetup) {
		MasterResponse mstrList = null;
		try {
			boolean isRedeem = iMasterDao.updateRewardResponsibleSetup(rrsetup);
			if (isRedeem) {
				mstrList = new MasterResponse();
				mstrList.setStatus(EnovationConstants.statusSuccess);
				mstrList.setStatusCode(EnovationConstants.Code200);
				mstrList.setResult(EnovationConstants.resultTrue);
				return mstrList;
			} else {
				mstrList = new MasterResponse();
				mstrList.setStatus(EnovationConstants.statusSuccess);
				mstrList.setStatusCode(EnovationConstants.Code200);
				mstrList.setResult(EnovationConstants.resultFalse);
				return mstrList;
			}
		} catch (Exception e) {
			mstrList = new MasterResponse();
			mstrList.setStatus(EnovationConstants.statusFail);
			mstrList.setStatusCode(EnovationConstants.Code500);
			mstrList.setResult(EnovationConstants.ResultFalse);
			mstrList.setReason("EXCEPTION OCCURED IN SAVE UPDATE RESPONSIBLE SETUP API :" + e.getMessage());
			return mstrList;
		}
	}

	@Override
	public MasterResponse deleteRewardResponsibleSetup(RewardResponsibleSetup rrsetup) {
		MasterResponse mstrList = null;
		try {
			boolean isRedeem = iMasterDao.deleteRewardResponsibleSetup(rrsetup);
			if (isRedeem) {
				mstrList = new MasterResponse();
				mstrList.setStatus(EnovationConstants.statusSuccess);
				mstrList.setStatusCode(EnovationConstants.Code200);
				mstrList.setResult(EnovationConstants.resultTrue);
				return mstrList;
			} else {
				mstrList = new MasterResponse();
				mstrList.setStatus(EnovationConstants.statusSuccess);
				mstrList.setStatusCode(EnovationConstants.Code200);
				mstrList.setResult(EnovationConstants.resultFalse);
				return mstrList;
			}
		} catch (Exception e) {
			mstrList = new MasterResponse();
			mstrList.setStatus(EnovationConstants.statusFail);
			mstrList.setStatusCode(EnovationConstants.Code500);
			mstrList.setResult(EnovationConstants.ResultFalse);
			mstrList.setReason("EXCEPTION OCCURED IN SAVE DELETE RESPONSIBLE SETUP API :" + e.getMessage());
			return mstrList;
		}
	}

	@Override
	public MasterResponse addThought(ThoughtOfDay th) {

		MasterResponse mstrList = null;
		Gson g = new Gson();
		LOGGER.info("INSIDE ADDTHOUGHT SERVICE : HEADER REQUEST :" + g.toJson(th));
		try {
			List<ThoughtOfDay> checkThoughtOfDay = iMasterDao.checkThoughtOfDay(th);
			if (checkThoughtOfDay != null && checkThoughtOfDay.size() > 0) {
				mstrList = new MasterResponse();
				mstrList.setStatus(EnovationConstants.statusSuccess);
				mstrList.setStatusCode(EnovationConstants.Code100);
				mstrList.setResult(EnovationConstants.resultFalse);
				mstrList.setReason(EnovationConstants.THOUGHT_EXISTS);
				return mstrList;
			} else {
				boolean isSave = iMasterDao.addThought(th);
				if (isSave) {
					mstrList = new MasterResponse();
					mstrList.setStatus(EnovationConstants.statusSuccess);
					mstrList.setStatusCode(EnovationConstants.Code200);
					mstrList.setResult(EnovationConstants.resultTrue);
					return mstrList;
				} else {
					mstrList = new MasterResponse();
					mstrList.setStatus(EnovationConstants.statusSuccess);
					mstrList.setStatusCode(EnovationConstants.Code200);
					mstrList.setResult(EnovationConstants.resultFalse);
					return mstrList;
				}
			}
		} catch (Exception e) {
			mstrList = new MasterResponse();
			mstrList.setStatus(EnovationConstants.statusFail);
			mstrList.setStatusCode(EnovationConstants.Code500);
			mstrList.setResult(EnovationConstants.ResultFalse);
			mstrList.setReason("EXCEPTION OCCURED IN ADD THOUGHT API :" + e.getMessage());
			return mstrList;
		}

	}

	public MasterResponse getRewardResponsibleSetup(Integer branchId) {
		List<RewardResponsibleSetup> rewarResList = iMasterDao.getrewardResponsibleList(branchId);
		MasterResponse mstrList = null;
		try {
			if (!rewarResList.isEmpty()) {
				mstrList = new MasterResponse();
				mstrList.setStatus(EnovationConstants.statusSuccess);
				mstrList.setStatusCode(EnovationConstants.Code200);
				mstrList.setResult(EnovationConstants.ResultTrue);
				mstrList.setReason(EnovationConstants.RECORDSFETCHSUCCESS);
				mstrList.setRewardResponsibleSetupList(rewarResList);
				return mstrList;
			} else {

				mstrList = new MasterResponse();
				mstrList.setStatus(EnovationConstants.statusSuccess);
				mstrList.setStatusCode(EnovationConstants.Code200);
				mstrList.setResult(EnovationConstants.ResultFalse);
				mstrList.setReason(EnovationConstants.RecordsDoesNotExist);
				return mstrList;
			}

		} catch (Exception e) {
			mstrList = new MasterResponse();
			mstrList.setStatus(EnovationConstants.statusFail);
			mstrList.setStatusCode(EnovationConstants.Code500);
			mstrList.setResult(EnovationConstants.ResultFalse);
			mstrList.setReason("EXCEPTION OCCURED IN GETREWARDRESPONSIBLESETUP  API :" + e.getMessage());
			return mstrList;
		}
	}

	@Override
	public MasterResponse addMoodIndicator(MoodIndicator mo) {
		MasterResponse mstrList = null;
		Gson g = new Gson();
		LOGGER.info("INSIDE ADD MOODINDICATOR SERVICE : HEADER REQUEST :" + g.toJson(mo));
		try {
			List<MoodIndicator> moCheck = iMasterDao.checkMoodIndicator(mo);
			if (moCheck != null && moCheck.size() > 0) {
				mstrList = new MasterResponse();
				mstrList.setStatus(EnovationConstants.statusSuccess);
				mstrList.setStatusCode(EnovationConstants.Code100);
				mstrList.setResult(EnovationConstants.resultFalse);
				mstrList.setReason(EnovationConstants.THOUGHT_EXISTS);
				return mstrList;
			} else {
				boolean isSave = iMasterDao.addMoodIndicator(mo);
				if (isSave) {
					mstrList = new MasterResponse();
					mstrList.setStatus(EnovationConstants.statusSuccess);
					mstrList.setStatusCode(EnovationConstants.Code200);
					mstrList.setResult(EnovationConstants.resultTrue);
					return mstrList;
				} else {
					mstrList = new MasterResponse();
					mstrList.setStatus(EnovationConstants.statusSuccess);
					mstrList.setStatusCode(EnovationConstants.Code200);
					mstrList.setResult(EnovationConstants.resultFalse);
					return mstrList;
				}
			}
		} catch (Exception e) {
			mstrList = new MasterResponse();
			mstrList.setStatus(EnovationConstants.statusFail);
			mstrList.setStatusCode(EnovationConstants.Code500);
			mstrList.setResult(EnovationConstants.ResultFalse);
			mstrList.setReason("EXCEPTION OCCURED IN ADD MOOD INDICATOR API :" + e.getMessage());
			return mstrList;
		}

	}

	@Override
	public MasterResponse addRedeemStatus(RedeemStatus status) {
		MasterResponse mstrList = null;
		try {
			boolean isRedeem = iMasterDao.addRedeemStatus(status);
			if (isRedeem) {
				mstrList = new MasterResponse();
				mstrList.setStatus(EnovationConstants.statusSuccess);
				mstrList.setStatusCode(EnovationConstants.Code200);
				mstrList.setResult(EnovationConstants.resultTrue);
				return mstrList;
			} else {
				mstrList = new MasterResponse();
				mstrList.setStatus(EnovationConstants.statusSuccess);
				mstrList.setStatusCode(EnovationConstants.Code200);
				mstrList.setResult(EnovationConstants.resultFalse);
				return mstrList;
			}
		} catch (Exception e) {
			mstrList = new MasterResponse();
			mstrList.setStatus(EnovationConstants.statusFail);
			mstrList.setStatusCode(EnovationConstants.Code500);
			mstrList.setResult(EnovationConstants.ResultFalse);
			mstrList.setReason("EXCEPTION OCCURED IN ADD STATUS API :" + e.getMessage());
			return mstrList;
		}
	}

	@Override
	public MasterResponse updateRedeemStatus(RedeemStatus status) {
		MasterResponse mstrList = null;
		try {
			boolean isUpdate = iMasterDao.updateStatus(status);
			if (isUpdate) {
				mstrList = new MasterResponse();
				mstrList.setStatus(EnovationConstants.statusSuccess);
				mstrList.setStatusCode(EnovationConstants.Code200);
				mstrList.setResult(EnovationConstants.resultTrue);
				return mstrList;
			} else {
				mstrList = new MasterResponse();
				mstrList.setStatus(EnovationConstants.statusSuccess);
				mstrList.setStatusCode(EnovationConstants.Code200);
				mstrList.setResult(EnovationConstants.resultFalse);
				return mstrList;
			}
		} catch (Exception e) {
			mstrList = new MasterResponse();
			mstrList.setStatus(EnovationConstants.statusFail);
			mstrList.setStatusCode(EnovationConstants.Code500);
			mstrList.setResult(EnovationConstants.ResultFalse);
			mstrList.setReason("EXCEPTION OCCURED IN UPDATE STATUS API :" + e.getMessage());
			return mstrList;
		}
	}

	@Override
	public MasterResponse updateRedeemFromCartReward(RedeemCartRewards redeemfrmcart) {
		MasterResponse mstrList = null;
		try {
			boolean isUpdate = iMasterDao.updateRedeemFromCartRewards(redeemfrmcart);
			if (isUpdate) {
				mstrList = new MasterResponse();
				mstrList.setStatus(EnovationConstants.statusSuccess);
				mstrList.setStatusCode(EnovationConstants.Code200);
				mstrList.setResult(EnovationConstants.resultTrue);
				return mstrList;
			} else {
				mstrList = new MasterResponse();
				mstrList.setStatus(EnovationConstants.statusSuccess);
				mstrList.setStatusCode(EnovationConstants.Code200);
				mstrList.setResult(EnovationConstants.resultFalse);
				return mstrList;
			}
		} catch (Exception e) {
			mstrList = new MasterResponse();
			mstrList.setStatus(EnovationConstants.statusFail);
			mstrList.setStatusCode(EnovationConstants.Code500);
			mstrList.setResult(EnovationConstants.ResultFalse);
			mstrList.setReason("EXCEPTION OCCURED IN UPDATE STATUS OF REDEEMED REWARDS API :" + e.getMessage());
			return mstrList;
		}
	}

	@Override
	public MasterResponse getRedeemFromCartRewardList(Integer empId) {
		MasterResponse mstrList = null;
		try {
			List<RedeemCartRewards> redeemCartRewards = iMasterDao.getRedeemFromCartRewardList(empId);
			if (!redeemCartRewards.isEmpty()) {
				mstrList = new MasterResponse();
				mstrList.setStatus(EnovationConstants.statusSuccess);
				mstrList.setStatusCode(EnovationConstants.Code200);
				mstrList.setResult(EnovationConstants.ResultTrue);
				mstrList.setReason(EnovationConstants.RECORDSFETCHSUCCESS);
				mstrList.setRedeemCartList(redeemCartRewards);
				return mstrList;
			} else {

				mstrList = new MasterResponse();
				mstrList.setStatus(EnovationConstants.statusSuccess);
				mstrList.setStatusCode(EnovationConstants.Code200);
				mstrList.setResult(EnovationConstants.ResultFalse);
				mstrList.setReason(EnovationConstants.RecordsDoesNotExist);
				return mstrList;
			}

		} catch (Exception e) {
			mstrList = new MasterResponse();
			mstrList.setStatus(EnovationConstants.statusFail);
			mstrList.setStatusCode(EnovationConstants.Code500);
			mstrList.setResult(EnovationConstants.ResultFalse);
			mstrList.setReason("EXCEPTION OCCURED IN GET REDEEM CART REWARD API :" + e.getMessage());
			return mstrList;
		}
	}

	@Override
	public MasterResponse updateCartDetails(Cart cart) {
		MasterResponse mstrList = null;
		try {
			boolean isUpdate = iMasterDao.updateCartDetails(cart);
			if (isUpdate) {
				mstrList = new MasterResponse();
				mstrList.setStatus(EnovationConstants.statusSuccess);
				mstrList.setStatusCode(EnovationConstants.Code200);
				mstrList.setResult(EnovationConstants.resultTrue);
				return mstrList;
			} else {
				mstrList = new MasterResponse();
				mstrList.setStatus(EnovationConstants.statusSuccess);
				mstrList.setStatusCode(EnovationConstants.Code200);
				mstrList.setResult(EnovationConstants.resultFalse);
				return mstrList;
			}
		} catch (Exception e) {
			mstrList = new MasterResponse();
			mstrList.setStatus(EnovationConstants.statusFail);
			mstrList.setStatusCode(EnovationConstants.Code500);
			mstrList.setResult(EnovationConstants.ResultFalse);
			mstrList.setReason("EXCEPTION OCCURED IN UPDATE CART DETAILS API :" + e.getMessage());
			return mstrList;
		}
	}

	@Override
	public MasterResponse getRedeemStatus() {
		MasterResponse mstrList = null;
		try {
			List<RedeemStatus> redeemStatusList = iMasterDao.redeemStatusList();
			if (!redeemStatusList.isEmpty()) {
				mstrList = new MasterResponse();
				mstrList.setStatus(EnovationConstants.statusSuccess);
				mstrList.setStatusCode(EnovationConstants.Code200);
				mstrList.setResult(EnovationConstants.resultTrue);
				mstrList.setRedeemStatusList(redeemStatusList);
				return mstrList;
			} else {
				mstrList = new MasterResponse();
				mstrList.setStatus(EnovationConstants.statusSuccess);
				mstrList.setStatusCode(EnovationConstants.Code200);
				mstrList.setResult(EnovationConstants.ResultFalse);
				mstrList.setReason(EnovationConstants.RecordsDoesNotExist);
				return mstrList;
			}
		} catch (Exception e) {
			mstrList = new MasterResponse();
			mstrList.setStatus(EnovationConstants.statusFail);
			mstrList.setStatusCode(EnovationConstants.Code500);
			mstrList.setResult(EnovationConstants.ResultFalse);
			mstrList.setReason("EXCEPTION OCCURED IN GET STATUS LIST  API :" + e.getMessage());
			return mstrList;
		}
	}

	@Override
	public MasterResponse getSocialMediaList() {

		MasterResponse mstrList = null;
		try {

			List<SocialMediaMaster> mediaList = iMasterDao.getSocialMediaList();
			if (mediaList != null && !mediaList.isEmpty()) {
				mstrList = new MasterResponse();
				mstrList.setStatus(EnovationConstants.statusSuccess);
				mstrList.setStatusCode(EnovationConstants.Code200);
				mstrList.setResult(EnovationConstants.ResultTrue);
				mstrList.setReason(EnovationConstants.RECORDSFETCHSUCCESS);
				mstrList.setSocialMediaList(mediaList);
				return mstrList;
			} else {

				mstrList = new MasterResponse();
				mstrList.setStatus(EnovationConstants.statusSuccess);
				mstrList.setStatusCode(EnovationConstants.Code200);
				mstrList.setResult(EnovationConstants.ResultFalse);
				mstrList.setReason(EnovationConstants.RecordsDoesNotExist);
				return mstrList;
			}

		} catch (Exception e) {
			mstrList = new MasterResponse();
			mstrList.setStatus(EnovationConstants.statusFail);
			mstrList.setStatusCode(EnovationConstants.Code500);
			mstrList.setResult(EnovationConstants.ResultFalse);
			mstrList.setReason("Exception Occured in GETSOCIALMEDIAMASTER  Api :" + e.getMessage());
			return mstrList;
		}
	}

	@Override
	public MasterResponse getThought(int branchId) {
		List<ThoughtOfDay> list = iMasterDao.getThought(branchId);
		MasterResponse mstrList = null;
		try {
			if (!list.isEmpty()) {
				mstrList = new MasterResponse();
				mstrList.setStatus(EnovationConstants.statusSuccess);
				mstrList.setStatusCode(EnovationConstants.Code200);
				mstrList.setResult(EnovationConstants.ResultTrue);
				mstrList.setReason(EnovationConstants.RECORDSFETCHSUCCESS);
				mstrList.setThoughtList(list);
				return mstrList;
			} else {

				mstrList = new MasterResponse();
				mstrList.setStatus(EnovationConstants.statusSuccess);
				mstrList.setStatusCode(EnovationConstants.Code200);
				mstrList.setResult(EnovationConstants.ResultFalse);
				mstrList.setReason(EnovationConstants.RecordsDoesNotExist);
				return mstrList;
			}

		} catch (Exception e) {
			mstrList = new MasterResponse();
			mstrList.setStatus(EnovationConstants.statusFail);
			mstrList.setStatusCode(EnovationConstants.Code500);
			mstrList.setResult(EnovationConstants.ResultFalse);
			mstrList.setReason("EXCEPTION OCCURED IN GETTHOUGHT  API :" + e.getMessage());
			return mstrList;
		}

	}

	@Override
	public MasterResponse getRewardCategoryList(Integer branchId) {
		List<RewardCategoryMaster> list = iMasterDao.getRewardCategoryList(branchId);
		MasterResponse mstrList = null;
		try {
			if (!list.isEmpty()) {
				mstrList = new MasterResponse();
				mstrList.setStatus(EnovationConstants.statusSuccess);
				mstrList.setStatusCode(EnovationConstants.Code200);
				mstrList.setResult(EnovationConstants.ResultTrue);
				mstrList.setReason(EnovationConstants.RECORDSFETCHSUCCESS);
				mstrList.setRewardCategoryList(list);
				return mstrList;
			} else {

				mstrList = new MasterResponse();
				mstrList.setStatus(EnovationConstants.statusSuccess);
				mstrList.setStatusCode(EnovationConstants.Code200);
				mstrList.setResult(EnovationConstants.ResultFalse);
				mstrList.setReason(EnovationConstants.RecordsDoesNotExist);
				return mstrList;
			}

		} catch (Exception e) {
			mstrList = new MasterResponse();
			mstrList.setStatus(EnovationConstants.statusFail);
			mstrList.setStatusCode(EnovationConstants.Code500);
			mstrList.setResult(EnovationConstants.ResultFalse);
			mstrList.setReason("EXCEPTION OCCURED IN GET REWARD CATEGORY LIST  API :" + e.getMessage());
			return mstrList;
		}
	}

	@Override
	public MasterResponse getThresholdList() {

		MasterResponse response = new MasterResponse();

		try {
			List<ThresholdMaster> list = iMasterDao.getThresholdList();
			if (list != null && !list.isEmpty()) {
				response.setStatus(EnovationConstants.statusSuccess);
				response.setStatusCode(EnovationConstants.Code200);
				response.setResult(EnovationConstants.ResultTrue);
				response.setReason(EnovationConstants.RECORDSFETCHSUCCESS);
				response.setThreshold(list);
				return response;
			} else {

				response = new MasterResponse();
				response.setStatus(EnovationConstants.statusSuccess);
				response.setStatusCode(EnovationConstants.Code200);
				response.setResult(EnovationConstants.ResultFalse);
				response.setReason(EnovationConstants.RecordsDoesNotExist);
				return response;
			}

		} catch (Exception e) {
			response = new MasterResponse();
			response.setStatus(EnovationConstants.statusFail);
			response.setStatusCode(EnovationConstants.Code500);
			response.setResult(EnovationConstants.ResultFalse);
			response.setReason("Exception Occured in GETSOCIALMEDIAMASTER  Api :" + e.getMessage());
			return response;
		}

	}

	@Override
	public MasterResponse updateMoodIndicator(MoodIndicator mo) {
		MasterResponse mstrList = null;
		Gson g = new Gson();
		LOGGER.info("INSIDE UPDATE MOODINDICATOR SERVICE : HEADER REQUEST :" + g.toJson(mo));
		try {
			boolean isUpdate = iMasterDao.updateMoodIndicator(mo);
			if (isUpdate) {
				mstrList = new MasterResponse();
				mstrList.setStatus(EnovationConstants.statusSuccess);
				mstrList.setStatusCode(EnovationConstants.Code200);
				mstrList.setResult(EnovationConstants.resultTrue);
				return mstrList;
			} else {
				mstrList = new MasterResponse();
				mstrList.setStatus(EnovationConstants.statusSuccess);
				mstrList.setStatusCode(EnovationConstants.CODE400);
				mstrList.setResult(EnovationConstants.resultFalse);
				return mstrList;
			}

		} catch (Exception e) {
			mstrList = new MasterResponse();
			mstrList.setStatus(EnovationConstants.statusFail);
			mstrList.setStatusCode(EnovationConstants.Code500);
			mstrList.setResult(EnovationConstants.ResultFalse);
			mstrList.setReason("EXCEPTION OCCURED IN UPDATE MOOD INDICATOR API:" + e.getMessage());
			return mstrList;
		}

	}

	@Override
	public MasterResponse addIncidentOfficer(IncidentOfficerDetails inc) {
		MasterResponse mstrList = null;
		Gson g = new Gson();
		LOGGER.info("INSIDE ADD_INCIDENT_OFFICER : HEADER REQUEST :" + g.toJson(inc));
		boolean isSafetyOfficerExist = iMasterDao.isSafetyOfficerExist(inc);
		boolean isIncidentManagerExist = iMasterDao.isIncidentManagerExist(inc);
		if (isSafetyOfficerExist) {
			mstrList = new MasterResponse();
			mstrList.setReason("Safety Officer Already Exist");
			mstrList.setResult(EnovationConstants.ResultFalse);
			mstrList.setStatus(EnovationConstants.statusFail);
			mstrList.setStatusCode(EnovationConstants.Code500);
		} else {
			if (isIncidentManagerExist) {
				mstrList = new MasterResponse();
				mstrList.setReason("Incident Manager Already Exist");
				mstrList.setResult(EnovationConstants.ResultFalse);
				mstrList.setStatus(EnovationConstants.statusFail);
				mstrList.setStatusCode(EnovationConstants.Code500);
			} else {
				boolean result = iMasterDao.addIncidentOfficer(inc);
				if (result) {
					mstrList = new MasterResponse();
					mstrList.setReason("Incident Officer added successfully");
					mstrList.setStatus(EnovationConstants.statusSuccess);
					mstrList.setStatusCode(EnovationConstants.Code200);
					mstrList.setResult(EnovationConstants.resultTrue);
				} else {
					mstrList = new MasterResponse();
					mstrList.setReason("Failed to add Incident Officer");
					mstrList.setResult(EnovationConstants.ResultFalse);
					mstrList.setStatus(EnovationConstants.statusFail);
					mstrList.setStatusCode(EnovationConstants.Code500);
				}
			}
		}
		return mstrList;
	}

	@Override
	public MasterResponse updateIncidentOfficer(IncidentOfficerDetails inc) {
		MasterResponse mstrList = null;
		Gson g = new Gson();
		LOGGER.info("INSIDE UPDATE_INCIDENT_OFFICER : HEADER REQUEST :" + g.toJson(inc));
		try {
			boolean isUpdate = iMasterDao.updateIncidentOfficer(inc);
			if (isUpdate) {
				mstrList = new MasterResponse();
				mstrList.setStatus(EnovationConstants.statusSuccess);
				mstrList.setStatusCode(EnovationConstants.Code200);
				mstrList.setResult(EnovationConstants.resultTrue);
				return mstrList;
			} else {
				mstrList = new MasterResponse();
				mstrList.setStatus(EnovationConstants.statusSuccess);
				mstrList.setStatusCode(EnovationConstants.CODE400);
				mstrList.setResult(EnovationConstants.resultFalse);
				return mstrList;
			}

		} catch (Exception e) {
			mstrList = new MasterResponse();
			mstrList.setStatus(EnovationConstants.statusFail);
			mstrList.setStatusCode(EnovationConstants.Code500);
			mstrList.setResult(EnovationConstants.ResultFalse);
			mstrList.setReason("EXCEPTION OCCURED IN UPDATE INCIDENT OFFICER API:" + e.getMessage());
			return mstrList;
		}

	}

	@Override
	public MasterResponse getIncidentOfficerDetails(Integer branchId) {
		MasterResponse response = new MasterResponse();
		try {
			List<IncidentOfficerDetails> list = iMasterDao.getIncidentOfficerDetails(branchId);
			if (list != null && !list.isEmpty()) {
				response.setStatus(EnovationConstants.statusSuccess);
				response.setStatusCode(EnovationConstants.Code200);
				response.setResult(EnovationConstants.ResultTrue);
				response.setReason(EnovationConstants.RECORDSFETCHSUCCESS);
				response.setIncidentOfficerDetails(list);
				return response;
			} else {

				response = new MasterResponse();
				response.setStatus(EnovationConstants.statusSuccess);
				response.setStatusCode(EnovationConstants.Code200);
				response.setResult(EnovationConstants.ResultFalse);
				response.setReason(EnovationConstants.RecordsDoesNotExist);
				return response;
			}

		} catch (Exception e) {
			response = new MasterResponse();
			response.setStatus(EnovationConstants.statusFail);
			response.setStatusCode(EnovationConstants.Code500);
			response.setResult(EnovationConstants.ResultFalse);
			response.setReason("Exception Occured in GET_INCIDENT_OFFICER_DETAILS  Api :" + e.getMessage());
			return response;
		}
	}

	@Override
	public MasterResponse getManagerTypeList() {
		MasterResponse response = new MasterResponse();
		try {
			List<IncidentManagerType> list = iMasterDao.getManagerTypeList();
			if (list != null && !list.isEmpty()) {
				response.setStatus(EnovationConstants.statusSuccess);
				response.setStatusCode(EnovationConstants.Code200);
				response.setResult(EnovationConstants.ResultTrue);
				response.setReason(EnovationConstants.RECORDSFETCHSUCCESS);
				response.setManagerTypeList(list);
				return response;
			} else {

				response = new MasterResponse();
				response.setStatus(EnovationConstants.statusSuccess);
				response.setStatusCode(EnovationConstants.Code200);
				response.setResult(EnovationConstants.ResultFalse);
				response.setReason(EnovationConstants.RecordsDoesNotExist);
				return response;
			}

		} catch (Exception e) {
			response = new MasterResponse();
			response.setStatus(EnovationConstants.statusFail);
			response.setStatusCode(EnovationConstants.Code500);
			response.setResult(EnovationConstants.ResultFalse);
			response.setReason("Exception Occured in GET_MANAGER_TYPE_LIST  Api :" + e.getMessage());
			return response;
		}
	}

	@Override
	public MasterResponse addMachineDetails(MachineDetails mach) {
		MasterResponse mstrList = null;
		Gson g = new Gson();
		LOGGER.info("INSIDE ADD MACHINE DETAILS : HEADER REQUEST :" + g.toJson(mach));
		try {
			boolean isSave = iMasterDao.addMachineDetails(mach);
			if (isSave) {
				mstrList = new MasterResponse();
				mstrList.setStatus(EnovationConstants.statusSuccess);
				mstrList.setStatusCode(EnovationConstants.Code200);
				mstrList.setResult(EnovationConstants.resultTrue);
				return mstrList;
			} else {
				mstrList = new MasterResponse();
				mstrList.setStatus(EnovationConstants.statusSuccess);
				mstrList.setStatusCode(EnovationConstants.CODE400);
				mstrList.setResult(EnovationConstants.resultFalse);
				return mstrList;
			}

		} catch (Exception e) {
			mstrList = new MasterResponse();
			mstrList.setStatus(EnovationConstants.statusFail);
			mstrList.setStatusCode(EnovationConstants.Code500);
			mstrList.setResult(EnovationConstants.ResultFalse);
			mstrList.setReason("EXCEPTION OCCURED IN ADD MACHINE DETAILS API:" + e.getMessage());
			return mstrList;
		}
	}

	@Override
	public MasterResponse updateMachineDetails(MachineDetails mach) {
		MasterResponse mstrList = null;
		Gson g = new Gson();
		LOGGER.info("INSIDE UPDATE MACHINE DETAILS : HEADER REQUEST :" + g.toJson(mach));
		try {
			boolean isUpdate = iMasterDao.updateMachineDetails(mach);
			if (isUpdate) {
				mstrList = new MasterResponse();
				mstrList.setStatus(EnovationConstants.statusSuccess);
				mstrList.setStatusCode(EnovationConstants.Code200);
				mstrList.setResult(EnovationConstants.resultTrue);
				return mstrList;
			} else {
				mstrList = new MasterResponse();
				mstrList.setStatus(EnovationConstants.statusSuccess);
				mstrList.setStatusCode(EnovationConstants.CODE400);
				mstrList.setResult(EnovationConstants.resultFalse);
				return mstrList;
			}

		} catch (Exception e) {
			mstrList = new MasterResponse();
			mstrList.setStatus(EnovationConstants.statusFail);
			mstrList.setStatusCode(EnovationConstants.Code500);
			mstrList.setResult(EnovationConstants.ResultFalse);
			mstrList.setReason("EXCEPTION OCCURED IN UPDATE MACHINE DETAILS API:" + e.getMessage());
			return mstrList;
		}
	}

	@Override
	public MasterResponse deleteMachineDetails(MachineDetails mach) {
		MasterResponse mstrList = null;
		Gson g = new Gson();
		LOGGER.info("INSIDE DELETE_INCIDENT_OFFICER : HEADER REQUEST :" + g.toJson(mach));
		try {
			boolean isMachinePendingExits = iMasterDao.isMachineExist(mach);
			if (isMachinePendingExits) {
				mstrList = new MasterResponse();
				mstrList.setReason("Cannot delete this Machine because it has some pending Actions");
				mstrList.setStatus(EnovationConstants.statusFail);
				mstrList.setStatusCode(EnovationConstants.Code500);
				mstrList.setResult(EnovationConstants.resultFalse);
				return mstrList;
			} else {
				boolean result = iMasterDao.deleteMachineDetails(mach);
				if (result) {
					mstrList = new MasterResponse();
					mstrList.setReason("Machine delete succesfully");
					mstrList.setStatus(EnovationConstants.statusSuccess);
					mstrList.setStatusCode(EnovationConstants.Code200);
					mstrList.setResult(EnovationConstants.resultTrue);
					return mstrList;
				} else {
					mstrList = new MasterResponse();
					mstrList.setReason("Failed to delete Machine");
					mstrList.setStatus(EnovationConstants.statusFail);
					mstrList.setStatusCode(EnovationConstants.Code500);
					mstrList.setResult(EnovationConstants.resultFalse);
				}
			}

		} catch (Exception e) {
			mstrList = new MasterResponse();
			mstrList.setStatus(EnovationConstants.statusFail);
			mstrList.setStatusCode(EnovationConstants.Code500);
			mstrList.setResult(EnovationConstants.ResultFalse);
			mstrList.setReason("EXCEPTION OCCURED IN DELETE MACHINE API:" + e.getMessage());
			return mstrList;
		}
		return mstrList;
	}

	@Override
	public MasterResponse getMachineList(Integer branchId) {
		MasterResponse response = new MasterResponse();
		try {
			List<MachineDetails> list = iMasterDao.getMachineList(branchId);
			if (list != null && !list.isEmpty()) {
				response.setStatus(EnovationConstants.statusSuccess);
				response.setStatusCode(EnovationConstants.Code200);
				response.setResult(EnovationConstants.ResultTrue);
				response.setReason(EnovationConstants.RECORDSFETCHSUCCESS);
				response.setMachineList(list);
				return response;
			} else {

				response = new MasterResponse();
				response.setStatus(EnovationConstants.statusSuccess);
				response.setStatusCode(EnovationConstants.Code200);
				response.setResult(EnovationConstants.ResultFalse);
				response.setReason(EnovationConstants.RecordsDoesNotExist);
				return response;
			}

		} catch (Exception e) {
			response = new MasterResponse();
			response.setStatus(EnovationConstants.statusFail);
			response.setStatusCode(EnovationConstants.Code500);
			response.setResult(EnovationConstants.ResultFalse);
			response.setReason("Exception Occured in GET_MACHINE_LIST  Api :" + e.getMessage());
			return response;
		}
	}

	@Override
	public MasterResponse getIncidentReport() {
		MasterResponse response = new MasterResponse();
		try {
			List<IncidentReport> list = iMasterDao.getIncidentReportList();
			if (list != null && !list.isEmpty()) {
				response.setStatus(EnovationConstants.statusSuccess);
				response.setStatusCode(EnovationConstants.Code200);
				response.setResult(EnovationConstants.ResultTrue);
				response.setReason(EnovationConstants.RECORDSFETCHSUCCESS);
				response.setIncidentReport(list);
				return response;
			} else {

				response = new MasterResponse();
				response.setStatus(EnovationConstants.statusSuccess);
				response.setStatusCode(EnovationConstants.Code200);
				response.setResult(EnovationConstants.ResultFalse);
				response.setReason(EnovationConstants.RecordsDoesNotExist);
				return response;
			}

		} catch (Exception e) {
			response = new MasterResponse();
			response.setStatus(EnovationConstants.statusFail);
			response.setStatusCode(EnovationConstants.Code500);
			response.setResult(EnovationConstants.ResultFalse);
			response.setReason("Exception Occured in GET_INCIDENT_REPORT_LIST  Api :" + e.getMessage());
			return response;
		}
	}

	@Override
	public MasterResponse getIncidentPriority() {
		MasterResponse response = new MasterResponse();
		try {
			List<IncidentPriority> list = iMasterDao.getIncidentPriorityList();
			if (list != null && !list.isEmpty()) {
				response.setStatus(EnovationConstants.statusSuccess);
				response.setStatusCode(EnovationConstants.Code200);
				response.setResult(EnovationConstants.ResultTrue);
				response.setReason(EnovationConstants.RECORDSFETCHSUCCESS);
				response.setIncidentPriority(list);
				return response;
			} else {

				response = new MasterResponse();
				response.setStatus(EnovationConstants.statusSuccess);
				response.setStatusCode(EnovationConstants.Code200);
				response.setResult(EnovationConstants.ResultFalse);
				response.setReason(EnovationConstants.RecordsDoesNotExist);
				return response;
			}

		} catch (Exception e) {
			response = new MasterResponse();
			response.setStatus(EnovationConstants.statusFail);
			response.setStatusCode(EnovationConstants.Code500);
			response.setResult(EnovationConstants.ResultFalse);
			response.setReason("Exception Occured in GET_INCIDENT_PRIORITY_LIST  Api :" + e.getMessage());
			return response;
		}
	}

	@Override
	public MasterResponse getIncidentSeverity() {
		MasterResponse response = new MasterResponse();
		try {
			List<IncidentSeverity> list = iMasterDao.getIncidentSaverityList();
			if (list != null && !list.isEmpty()) {
				response.setStatus(EnovationConstants.statusSuccess);
				response.setStatusCode(EnovationConstants.Code200);
				response.setResult(EnovationConstants.ResultTrue);
				response.setReason(EnovationConstants.RECORDSFETCHSUCCESS);
				response.setIncidentSaverity(list);
				return response;
			} else {

				response = new MasterResponse();
				response.setStatus(EnovationConstants.statusSuccess);
				response.setStatusCode(EnovationConstants.Code200);
				response.setResult(EnovationConstants.ResultFalse);
				response.setReason(EnovationConstants.RecordsDoesNotExist);
				return response;
			}

		} catch (Exception e) {
			response = new MasterResponse();
			response.setStatus(EnovationConstants.statusFail);
			response.setStatusCode(EnovationConstants.Code500);
			response.setResult(EnovationConstants.ResultFalse);
			response.setReason("Exception Occured in GET_INCIDENT_SAVERITY_LIST  Api :" + e.getMessage());
			return response;
		}
	}

	@Override
	public MasterResponse getIncidentNature() {
		MasterResponse response = new MasterResponse();
		try {
			List<IncidentNature> list = iMasterDao.getIncidentNatureList();
			if (list != null && !list.isEmpty()) {
				response.setStatus(EnovationConstants.statusSuccess);
				response.setStatusCode(EnovationConstants.Code200);
				response.setResult(EnovationConstants.ResultTrue);
				response.setReason(EnovationConstants.RECORDSFETCHSUCCESS);
				response.setIncidentNature(list);
				return response;
			} else {

				response = new MasterResponse();
				response.setStatus(EnovationConstants.statusSuccess);
				response.setStatusCode(EnovationConstants.Code200);
				response.setResult(EnovationConstants.ResultFalse);
				response.setReason(EnovationConstants.RecordsDoesNotExist);
				return response;
			}

		} catch (Exception e) {
			response = new MasterResponse();
			response.setStatus(EnovationConstants.statusFail);
			response.setStatusCode(EnovationConstants.Code500);
			response.setResult(EnovationConstants.ResultFalse);
			response.setReason("Exception Occured in GET_INCIDENT_NATURE_LIST  Api :" + e.getMessage());
			return response;
		}
	}

	@Override
	public MasterResponse getIncidentManagerList(Integer branchId) {
		MasterResponse response = new MasterResponse();
		try {
			List<IncidentOfficerDetails> list = iMasterDao.getIncidentManagerList(branchId);
			if (list != null && !list.isEmpty()) {
				response.setStatus(EnovationConstants.statusSuccess);
				response.setStatusCode(EnovationConstants.Code200);
				response.setResult(EnovationConstants.ResultTrue);
				response.setReason(EnovationConstants.RECORDSFETCHSUCCESS);
				response.setIncidentManagerList(list);
				return response;
			} else {

				response = new MasterResponse();
				response.setStatus(EnovationConstants.statusSuccess);
				response.setStatusCode(EnovationConstants.Code200);
				response.setResult(EnovationConstants.ResultFalse);
				response.setReason(EnovationConstants.RecordsDoesNotExist);
				return response;
			}

		} catch (Exception e) {
			response = new MasterResponse();
			response.setStatus(EnovationConstants.statusFail);
			response.setStatusCode(EnovationConstants.Code500);
			response.setResult(EnovationConstants.ResultFalse);
			response.setReason("Exception Occured in GET_INCIDENT_MANAGER_LIST  Api :" + e.getMessage());
			return response;
		}
	}

	@Override
	public MasterResponse getLineNameList(Integer deptId) {
		MasterResponse response = new MasterResponse();
		try {
			List<Line> list = iMasterDao.getLineNameList(deptId);
			if (list != null && !list.isEmpty()) {
				response.setStatus(EnovationConstants.statusSuccess);
				response.setStatusCode(EnovationConstants.Code200);
				response.setResult(EnovationConstants.ResultTrue);
				response.setReason(EnovationConstants.RECORDSFETCHSUCCESS);
				response.setLineList(list);
				return response;
			} else {

				response = new MasterResponse();
				response.setStatus(EnovationConstants.statusSuccess);
				response.setStatusCode(EnovationConstants.Code200);
				response.setResult(EnovationConstants.ResultFalse);
				response.setReason(EnovationConstants.RecordsDoesNotExist);
				return response;
			}

		} catch (Exception e) {
			response = new MasterResponse();
			response.setStatus(EnovationConstants.statusFail);
			response.setStatusCode(EnovationConstants.Code500);
			response.setResult(EnovationConstants.ResultFalse);
			response.setReason("EXCEPTION OCCURED IN GET LINE NAME LIST  API :" + e.getMessage());
			return response;
		}
	}

	@Override
	public MasterResponse addLineName(Line line) {
		MasterResponse response = new MasterResponse();
		List<Line> lineExists = new ArrayList<>();

		try {
			System.out.println("id is :" + line.getId());
			if (line.getId() > 0) {
				boolean result = iMasterDao.updateLineName(line);
				if (result) {
					response.setStatus(EnovationConstants.statusSuccess);
					response.setStatusCode(EnovationConstants.Code200);
					response.setResult(EnovationConstants.ResultTrue);
					response.setReason("LINENAME UPDATED SUCCESSFULLY");
				} else {
					response.setStatus(EnovationConstants.statusSuccess);
					response.setStatusCode(EnovationConstants.CODE400);
					response.setReason(EnovationConstants.BAD_REQUEST);
				}
			} else {
				lineExists = iMasterDao.checkLineNameExists(line);
				if (lineExists.isEmpty()) {
					boolean result = iMasterDao.addLineName(line);
					if (result) {
						response.setStatus(EnovationConstants.statusSuccess);
						response.setStatusCode(EnovationConstants.Code200);
						response.setResult(EnovationConstants.ResultTrue);
						response.setReason("LINENAME ADDDED SUCCESSFULLY");
					} else {
						response.setStatus(EnovationConstants.statusSuccess);
						response.setStatusCode(EnovationConstants.CODE400);
						response.setReason(EnovationConstants.BAD_REQUEST);
					}
				} else {
					response.setStatus(EnovationConstants.statusSuccess);
					response.setStatusCode(EnovationConstants.Code100);
					response.setReason(EnovationConstants.ALREADY_EXITS);
				}
			}

		} catch (Exception e) {
			response = new MasterResponse();
			response.setStatus(EnovationConstants.statusFail);
			response.setStatusCode(EnovationConstants.Code500);
			response.setReason("EXCEPTION OCCURED IN ADD LINENAME  API :" + e.getMessage());
		}
		return response;
	}

	@Override
	public MasterResponse getShiftList(Shifts request) {
		MasterResponse response = new MasterResponse();
		try {
			List<Shifts> list = iMasterDao.getShiftList(request);
			if (list != null && !list.isEmpty()) {
				response.setStatus(EnovationConstants.statusSuccess);
				response.setStatusCode(EnovationConstants.Code200);
				response.setResult(EnovationConstants.ResultTrue);
				response.setReason(EnovationConstants.RECORDSFETCHSUCCESS);
				response.setShiftsList(list);
				return response;
			} else {

				response = new MasterResponse();
				response.setStatus(EnovationConstants.statusSuccess);
				response.setStatusCode(EnovationConstants.Code200);
				response.setResult(EnovationConstants.ResultFalse);
				response.setReason(EnovationConstants.RecordsDoesNotExist);
				return response;
			}

		} catch (Exception e) {
			response = new MasterResponse();
			response.setStatus(EnovationConstants.statusFail);
			response.setStatusCode(EnovationConstants.Code500);
			response.setResult(EnovationConstants.ResultFalse);
			response.setReason("EXCEPTION OCCURED IN  GETSHIFTLIST  API :" + e.getMessage());
			return response;
		}
	}
	
	@Override
	public MasterResponse addOrUpdateZone(MasterZone zone) {
		MasterResponse response = new MasterResponse();
		try {
			int result = iMasterDao.addOrUpdateZone(zone);
			if (result > 0) {
				response.setStatus(EnovationConstants.statusSuccess);
				response.setStatusCode(EnovationConstants.Code200);
				response.setResult(EnovationConstants.ResultTrue);
				if (result == 1) {
					response.setReason("ZONE UPDATED SUCCESSFULLY");
				} else {
					response.setReason("ZONE INSERTED SUCCESSFULLY");
				}

			} else {
				response.setStatus(EnovationConstants.statusSuccess);
				response.setStatusCode(EnovationConstants.CODE400);
				response.setReason(EnovationConstants.BAD_REQUEST);
			}
		} catch (Exception e) {
			response = new MasterResponse();
			response.setStatus(EnovationConstants.statusFail);
			response.setStatusCode(EnovationConstants.Code500);
			response.setReason("EXCEPTION OCCURED IN ADD ZONE  API :" + e.getMessage());
		}
		return response;
	}

	@Override
	public MasterResponse getCategoryListByBranchId(Integer branchId) {
		MasterResponse response = new MasterResponse();
		try {
			List<CategoryMaster> list = iMasterDao.getCategoryListByBranchId(branchId);
			if (list != null && !list.isEmpty()) {
				response.setStatus(EnovationConstants.statusSuccess);
				response.setStatusCode(EnovationConstants.Code200);
				response.setResult(EnovationConstants.ResultTrue);
				response.setReason(EnovationConstants.RECORDSFETCHSUCCESS);
				response.setCatagoryList(list);
				return response;
			} else {

				response = new MasterResponse();
				response.setStatus(EnovationConstants.statusSuccess);
				response.setStatusCode(EnovationConstants.Code200);
				response.setResult(EnovationConstants.ResultFalse);
				response.setReason(EnovationConstants.RecordsDoesNotExist);
				return response;
			}

		} catch (Exception e) {
			response = new MasterResponse();
			response.setStatus(EnovationConstants.statusFail);
			response.setStatusCode(EnovationConstants.Code500);
			response.setResult(EnovationConstants.ResultFalse);
			response.setReason("EXCEPTION OCCURED IN GET ZONE LIST  API :" + e.getMessage());
			return response;
		}
	}

	@Override
	public MasterResponse addOrUpdateSection(Section section) {
		MasterResponse response = new MasterResponse();
		try {
			int result = iMasterDao.addOrUpdateSection(section);
			if (result > 0) {
				response.setStatus(EnovationConstants.statusSuccess);
				response.setStatusCode(EnovationConstants.Code200);
				response.setResult(EnovationConstants.ResultTrue);
				if (result == 1) {
					response.setReason("SECTION UPDATED SUCCESSFULLY");
				} else {
					response.setReason("SECTION INSERTED SUCCESSFULLY");
				}

			} else {
				response.setStatus(EnovationConstants.statusSuccess);
				response.setStatusCode(EnovationConstants.CODE400);
				response.setReason(EnovationConstants.BAD_REQUEST);
			}
		} catch (Exception e) {
			response = new MasterResponse();
			response.setStatus(EnovationConstants.statusFail);
			response.setStatusCode(EnovationConstants.Code500);
			response.setReason("EXCEPTION OCCURED IN ADD SECTION  API :" + e.getMessage());
		}
		return response;
	}
	
	@Override
	public MasterResponse addMachineParts(MachineParts parts) {
		LOGGER.info(" INSIDE ADD OR UPDATE MachineParts SERVICE IMPL");
		MasterResponse mstrList = null;
		boolean result = false;
		try {
			if (parts.getId() > EnovationConstants.ONE) {
				result = iMasterDao.updateMachineParts(parts);
			} else {
				result = iMasterDao.addMachineParts(parts);
			}
			if (result) {
				mstrList = new MasterResponse();
				mstrList.setStatus(EnovationConstants.statusSuccess);
				mstrList.setStatusCode(EnovationConstants.Code200);
				mstrList.setResult(EnovationConstants.ResultTrue);
				mstrList.setReason(EnovationConstants.RECORD_DELETED);
				return mstrList;
			} else {
				mstrList = new MasterResponse();
				mstrList.setStatus(EnovationConstants.statusSuccess);
				mstrList.setStatusCode(EnovationConstants.Code200);
				mstrList.setReason(EnovationConstants.RecordsDoesNotExist);
				return mstrList;
			}
		} catch (Exception e) {
			mstrList = new MasterResponse();
			mstrList.setStatus(EnovationConstants.statusFail);
			mstrList.setStatusCode(EnovationConstants.Code500);
			mstrList.setReason("EXCEPTION OCCURED ADD OR UPDATE ADDMACHINEPARTS :" + e.getMessage());
		}

		return mstrList;
	}

	@Override
	public MasterResponse getMachinePartsList(Integer machId, Integer branchId) {
		MasterResponse response = new MasterResponse();
		List<MachineParts> parList = new ArrayList<>();
		try {
			List<Object[]> machinePartList = iMasterDao.getMachinePartsList(machId, branchId);
			if (machinePartList != null && !machinePartList.isEmpty()) {
				machinePartList.stream().forEach(x -> {
					MachineDetails machineDetails = new MachineDetails();
					if (x[9] != null) {
						machineDetails.setMachId(Integer.valueOf(String.valueOf(x[9])));
					}
					if (x[10] != null) {
						machineDetails.setMachName(String.valueOf(x[10]));
					}
					if (x[11] != null) {
						machineDetails.setMachNumber(String.valueOf(x[11]));
					}
					parList.add(new MachineParts((x[0] != null) ? Long.valueOf(String.valueOf(x[0])) : 0,
							(x[1] != null) ? String.valueOf(x[1]) : null, (x[2] != null) ? String.valueOf(x[2]) : null,
							(x[3] != null) ? CommonUtils.convertStringToTimestamp(String.valueOf(x[3])) : null,
							(x[4] != null) ? String.valueOf(x[4]) : null, (x[5] != null) ? String.valueOf(x[5]) : null,
							(x[6] != null) ? Long.valueOf(String.valueOf(x[6])) : 0,
							(x[7] != null) ? String.valueOf(x[7]) : null,
							(x[8] != null) ? Integer.valueOf(String.valueOf(x[8])) : 0, machineDetails));
				});
				response.setStatus(EnovationConstants.statusSuccess);
				response.setStatusCode(EnovationConstants.Code200);
				response.setResult(EnovationConstants.ResultTrue);
				response.setReason(EnovationConstants.RECORDSFETCHSUCCESS);
				response.setMachinePartsList(parList);
				return response;
			} else {

				response = new MasterResponse();
				response.setStatus(EnovationConstants.statusSuccess);
				response.setStatusCode(EnovationConstants.Code200);
				response.setReason(EnovationConstants.RecordsDoesNotExist);
				return response;
			}

		} catch (Exception e) {
			response = new MasterResponse();
			response.setStatus(EnovationConstants.statusFail);
			response.setStatusCode(EnovationConstants.Code500);
			response.setReason("EXCEPTION OCCURED IN GETMACHINEPARTSLIST  API :" + e.getMessage());
			return response;
		}
	}

	@Override
	public MasterResponse deleteMachineParts(Long id) {
		LOGGER.info(" DELETE MACHINE PARTS SERVICE IMPL");
		MasterResponse mstrList = null;
		try {
			int result = iMasterDao.deleteMachineParts(id);
			if (result == 1) {
				mstrList = new MasterResponse();
				mstrList.setStatus(EnovationConstants.statusSuccess);
				mstrList.setStatusCode(EnovationConstants.Code200);
				mstrList.setResult(EnovationConstants.ResultTrue);
				mstrList.setReason(EnovationConstants.RECORD_DELETED);
				return mstrList;
			} else {
				mstrList = new MasterResponse();
				mstrList.setStatus(EnovationConstants.statusSuccess);
				mstrList.setStatusCode(EnovationConstants.Code100);
				mstrList.setReason(EnovationConstants.MACHINE_ALREADY_ALIGNED);
				return mstrList;
			}
		} catch (Exception e) {
			mstrList = new MasterResponse();
			mstrList.setStatus(EnovationConstants.statusSuccess);
			mstrList.setStatusCode(EnovationConstants.Code100);
			mstrList.setReason(EnovationConstants.MACHINE_ALREADY_ALIGNED);
			return mstrList;
		}
	}

	@Override
	public MasterResponse getMachineListByLine(MachineDetails macDet) {
		MasterResponse response = new MasterResponse();
		try {
			List<MachineDetails> list = iMasterDao.getMachineListByLine(macDet);
			if (list != null && !list.isEmpty()) {
				response.setStatus(EnovationConstants.statusSuccess);
				response.setStatusCode(EnovationConstants.Code200);
				response.setReason(EnovationConstants.RECORDSFETCHSUCCESS);
				response.setMachineList(list);
				return response;
			} else {

				response = new MasterResponse();
				response.setStatus(EnovationConstants.statusSuccess);
				response.setStatusCode(EnovationConstants.Code200);
				response.setReason(EnovationConstants.RecordsDoesNotExist);
				return response;
			}

		} catch (Exception e) {
			response = new MasterResponse();
			response.setStatus(EnovationConstants.statusFail);
			response.setStatusCode(EnovationConstants.Code500);
			response.setReason("Exception Occured in GETMACHINELISTBYLINE  Api :" + e.getMessage());
			return response;
		}
	}

	@Override
	public MasterResponse saveSugOnHoldSetup(SuggestionOnHoldSetup onHold) {
		LOGGER.info("SAVE SUGGESTION ON HOLD SETUP IMPL");
		MasterResponse mstrList = null;
		try {
			boolean result = iMasterDao.saveSugOnHoldSetup(onHold);
			if (result == true) {
				mstrList = new MasterResponse();
				mstrList.setStatus(EnovationConstants.statusSuccess);
				mstrList.setStatusCode(EnovationConstants.Code200);
				mstrList.setResult(EnovationConstants.ResultTrue);
				mstrList.setReason(EnovationConstants.RECORD_ADDED);
				return mstrList;
			} else {
				mstrList = new MasterResponse();
				mstrList.setStatus(EnovationConstants.statusSuccess);
				mstrList.setStatusCode(EnovationConstants.Code100);
				mstrList.setReason(EnovationConstants.RECORD_NOT_ADDED);
				return mstrList;
			}
		} catch (Exception e) {
			mstrList = new MasterResponse();
			mstrList.setStatus(EnovationConstants.statusSuccess);
			mstrList.setStatusCode(EnovationConstants.Code100);
			mstrList.setReason("Exception Occured Inside Save On Hold Suggestion Steup");
			return mstrList;
		}
	}

	@Override
	public MasterResponse deleteSugOnHoldSetup(SuggestionOnHoldSetup onHold) {
		LOGGER.info("DELETE SUGGESTION ON HOLD SETUP IMPL");
		MasterResponse mstrList = null;
		try {
			boolean result = iMasterDao.isOnHoldPendingActionsExists(onHold);

			if (result == true) {
				mstrList = new MasterResponse();
				mstrList.setStatus(EnovationConstants.statusFail);
				mstrList.setStatusCode(EnovationConstants.Code100);
				mstrList.setResult(EnovationConstants.ResultFalse);
				mstrList.setReason("Cannot delete this employee as he/she has some pending Actions");
				return mstrList;
			} else {
				iMasterDao.deleteSugOnHoldSetup(onHold);
				mstrList = new MasterResponse();
				mstrList.setStatus(EnovationConstants.statusSuccess);
				mstrList.setStatusCode(EnovationConstants.Code200);
				mstrList.setReason(EnovationConstants.RECORD_DELETED);
				mstrList.setResult(EnovationConstants.ResultTrue);
				return mstrList;
			}
		} catch (Exception e) {
			mstrList = new MasterResponse();
			mstrList.setStatus(EnovationConstants.statusFail);
			mstrList.setStatusCode(EnovationConstants.Code500);
			mstrList.setReason("Exception Occured Inside delete On Hold Suggestion Steup");
			return mstrList;
		}
	}

	@Override
	public MasterResponse savePasswordPolicy(PasswordPolicy policy) {
		MasterResponse mstrList = null;
		try {
			boolean isExist = iMasterDao.isPasswordPolicyExist(policy);
			if (isExist) {
				mstrList = new MasterResponse();
				mstrList.setStatus(EnovationConstants.statusFail);
				mstrList.setStatusCode(EnovationConstants.Code100);
				mstrList.setResult(EnovationConstants.resultFalse);
				mstrList.setReason(
						"Cannot add new password policy as password policy already exist for this organization");
				return mstrList;
			} else {
				iMasterDao.savePasswordPolicy(policy);
				mstrList = new MasterResponse();
				mstrList.setStatus(EnovationConstants.statusSuccess);
				mstrList.setStatusCode(EnovationConstants.Code200);
				mstrList.setResult(EnovationConstants.resultTrue);
				return mstrList;
			}
		} catch (Exception e) {
			mstrList = new MasterResponse();
			mstrList.setStatus(EnovationConstants.statusFail);
			mstrList.setStatusCode(EnovationConstants.Code500);
			mstrList.setResult(EnovationConstants.ResultFalse);
			mstrList.setReason("Exception Occured in save password policy Api :" + e.getMessage());
			return mstrList;
		}

	}

	@Override
	public MasterResponse updatePasswordPolicy(PasswordPolicy policy) {
		MasterResponse mstrList = null;
		try {
			boolean isSave = iMasterDao.updatePasswordPolicy(policy);
			if (isSave) {
				mstrList = new MasterResponse();
				mstrList.setStatus(EnovationConstants.statusSuccess);
				mstrList.setStatusCode(EnovationConstants.Code200);
				mstrList.setResult(EnovationConstants.resultTrue);
				return mstrList;
			} else {
				mstrList = new MasterResponse();
				mstrList.setStatus(EnovationConstants.statusSuccess);
				mstrList.setStatusCode(EnovationConstants.Code200);
				mstrList.setResult(EnovationConstants.resultFalse);
				return mstrList;
			}
		} catch (Exception e) {
			mstrList = new MasterResponse();
			mstrList.setStatus(EnovationConstants.statusFail);
			mstrList.setStatusCode(EnovationConstants.Code500);
			mstrList.setResult(EnovationConstants.ResultFalse);
			mstrList.setReason("Exception Occured in update password policy Api :" + e.getMessage());
			return mstrList;
		}

	}

	@Override
	public MasterResponse getPasswordPolicyByOrgId(int orgId) {
		MasterResponse response = new MasterResponse();
		try {
			PasswordPolicy policyDet = iMasterDao.getPasswordPolicyByOrgId(orgId);
			if (policyDet != null) {
				response.setStatus(EnovationConstants.statusSuccess);
				response.setStatusCode(EnovationConstants.Code200);
				response.setReason(EnovationConstants.RECORDSFETCHSUCCESS);
				response.setPasswordPolicy(policyDet);
				response.setResult(EnovationConstants.resultTrue);
				return response;
			} else {
				response = new MasterResponse();
				response.setStatus(EnovationConstants.statusSuccess);
				response.setStatusCode(EnovationConstants.Code200);
				response.setReason(EnovationConstants.RecordsDoesNotExist);
				response.setResult(EnovationConstants.resultFalse);
				return response;
			}
		} catch (Exception e) {
			response = new MasterResponse();
			response.setStatus(EnovationConstants.statusFail);
			response.setStatusCode(EnovationConstants.Code500);
			response.setReason("Exception Occured in get Password policy by org Id Api :" + e.getMessage());
			return response;
		}
	}

	@Override
	public MasterResponse deletePasswordPolicy(long id) {
		MasterResponse mstrList = null;
		try {
			boolean isDelete = iMasterDao.deletePasswordPolicy(id);
			if (isDelete) {
				mstrList = new MasterResponse();
				mstrList.setStatus(EnovationConstants.statusSuccess);
				mstrList.setStatusCode(EnovationConstants.Code200);
				mstrList.setResult(EnovationConstants.resultTrue);
				return mstrList;
			} else {
				mstrList = new MasterResponse();
				mstrList.setStatus(EnovationConstants.statusSuccess);
				mstrList.setStatusCode(EnovationConstants.Code200);
				mstrList.setResult(EnovationConstants.resultFalse);
				return mstrList;
			}
		} catch (Exception e) {
			mstrList = new MasterResponse();
			mstrList.setStatus(EnovationConstants.statusFail);
			mstrList.setStatusCode(EnovationConstants.Code500);
			mstrList.setResult(EnovationConstants.ResultFalse);
			mstrList.setReason("Exception Occured in delete password policy Api :" + e.getMessage());
			return mstrList;
		}
	}

	@Override
	public MasterResponse enableDisablePasswordPolicy(PasswordPolicy policy) {
		MasterResponse mstrList = null;
		try {
			boolean isDelete = iMasterDao.enableDisablePasswordPolicy(policy);
			if (isDelete) {
				mstrList = new MasterResponse();
				mstrList.setStatus(EnovationConstants.statusSuccess);
				mstrList.setStatusCode(EnovationConstants.Code200);
				mstrList.setResult(EnovationConstants.resultTrue);
				return mstrList;
			} else {
				mstrList = new MasterResponse();
				mstrList.setStatus(EnovationConstants.statusSuccess);
				mstrList.setStatusCode(EnovationConstants.Code200);
				mstrList.setResult(EnovationConstants.resultFalse);
				return mstrList;
			}
		} catch (Exception e) {
			mstrList = new MasterResponse();
			mstrList.setStatus(EnovationConstants.statusFail);
			mstrList.setStatusCode(EnovationConstants.Code500);
			mstrList.setResult(EnovationConstants.ResultFalse);
			mstrList.setReason("Exception Occured in enable disable password policy Api :" + e.getMessage());
			return mstrList;
		}
	}

	@Override
	public MasterResponse addorupdateSuggestionTemplate(SuggestionTemplate request) {
		MasterResponse mstrList = null;
		boolean isSaveOrUpdate = false;
		try {
			if (request.getId() > 0) {
				isSaveOrUpdate = iMasterDao.updateSuggestionTemplate(request);
			} else {
				isSaveOrUpdate = iMasterDao.addSuggestionTemplate(request);
			}
			if (isSaveOrUpdate) {
				mstrList = new MasterResponse();
				mstrList.setStatus(EnovationConstants.statusSuccess);
				mstrList.setStatusCode(EnovationConstants.Code200);
				mstrList.setResult(EnovationConstants.resultTrue);
				return mstrList;
			} else {
				mstrList = new MasterResponse();
				mstrList.setStatus(EnovationConstants.statusSuccess);
				mstrList.setStatusCode(EnovationConstants.Code200);
				mstrList.setResult(EnovationConstants.resultFalse);
				return mstrList;
			}
		} catch (Exception e) {
			mstrList = new MasterResponse();
			mstrList.setStatus(EnovationConstants.statusFail);
			mstrList.setStatusCode(EnovationConstants.Code500);
			mstrList.setResult(EnovationConstants.ResultFalse);
			mstrList.setReason("EXCEPTION OCCURED IN ADD OR UPDATE SUGGESTION TEMPLATE API:" + e.getMessage());
			return mstrList;
		}
	}

	@Override
	public MasterResponse suggestionTemplateMasterList() {
		MasterResponse mstrList = null;
		List<SuggestionTemplateMaster> list = new ArrayList<>();
		try {
			list = iMasterDao.suggestionTemplateMasterList();
			if (!list.isEmpty()) {
				mstrList = new MasterResponse();
				mstrList.setSuggestionTemplateMasterList(list);
				mstrList.setStatus(EnovationConstants.statusSuccess);
				mstrList.setStatusCode(EnovationConstants.Code200);
				mstrList.setResult(EnovationConstants.resultTrue);
				return mstrList;
			} else {
				mstrList = new MasterResponse();
				mstrList.setStatus(EnovationConstants.statusSuccess);
				mstrList.setStatusCode(EnovationConstants.Code200);
				mstrList.setResult(EnovationConstants.resultFalse);
				return mstrList;
			}
		} catch (Exception e) {
			mstrList = new MasterResponse();
			mstrList.setStatus(EnovationConstants.statusFail);
			mstrList.setStatusCode(EnovationConstants.Code500);
			mstrList.setResult(EnovationConstants.ResultFalse);
			mstrList.setReason("EXCEPTION OCCURED IN SUGGESTION TEMPLATE MASTERLIST API:" + e.getMessage());
			return mstrList;
		}

	}

	@Override
	public MasterResponse saveSuggestionJurySetup(List<SuggestionEvalJurySetup> request) {
		MasterResponse mstrList = null;
		boolean isSave = false;
		try {
			isSave = iMasterDao.saveSuggestionJurySetup(request);
			if (isSave) {
				mstrList = new MasterResponse();
				mstrList.setStatus(EnovationConstants.statusSuccess);
				mstrList.setStatusCode(EnovationConstants.Code200);
				mstrList.setResult(EnovationConstants.resultTrue);
				return mstrList;
			} else {
				mstrList = new MasterResponse();
				mstrList.setStatus(EnovationConstants.statusSuccess);
				mstrList.setStatusCode(EnovationConstants.Code200);
				mstrList.setResult(EnovationConstants.resultFalse);
				return mstrList;
			}
		} catch (Exception e) {
			mstrList = new MasterResponse();
			mstrList.setStatus(EnovationConstants.statusFail);
			mstrList.setStatusCode(EnovationConstants.Code500);
			mstrList.setReason("#Exception Occured in save Suggestion Jury Setup Api " + e.getMessage());
			return mstrList;
		}
	}

	@Override
	public MasterResponse updateSuggestionJurySetup(SuggestionEvalJurySetup request) {
		MasterResponse mstrList = null;
		try {
			boolean isSave = iMasterDao.updateSuggestionJurySetup(request);
			if (isSave) {
				mstrList = new MasterResponse();
				mstrList.setStatus(EnovationConstants.statusSuccess);
				mstrList.setStatusCode(EnovationConstants.Code200);
				mstrList.setResult(EnovationConstants.resultTrue);
				return mstrList;
			} else {
				mstrList = new MasterResponse();
				mstrList.setStatus(EnovationConstants.statusSuccess);
				mstrList.setStatusCode(EnovationConstants.Code200);
				mstrList.setResult(EnovationConstants.resultFalse);
				return mstrList;
			}
		} catch (Exception e) {
			mstrList = new MasterResponse();
			mstrList.setStatus(EnovationConstants.statusFail);
			mstrList.setStatusCode(EnovationConstants.Code500);
			mstrList.setReason("#EXCEPTION OCCURED IN SAVE SUGGESTION JURY SETUP API:" + e.getMessage());
			return mstrList;
		}
	}

	@Override
	public MasterResponse deleteSuggestionTemplate(Long id) {
		MasterResponse mstrList = null;
		try {
			boolean isDelete = iMasterDao.deleteSuggestionTemplate(id);
			if (isDelete) {
				mstrList = new MasterResponse();
				mstrList.setStatus(EnovationConstants.statusSuccess);
				mstrList.setStatusCode(EnovationConstants.Code200);
				mstrList.setResult(EnovationConstants.resultTrue);
				return mstrList;
			} else {
				mstrList = new MasterResponse();
				mstrList.setStatus(EnovationConstants.statusSuccess);
				mstrList.setStatusCode(EnovationConstants.Code200);
				mstrList.setResult(EnovationConstants.resultFalse);
				return mstrList;
			}
		} catch (Exception e) {
			mstrList = new MasterResponse();
			mstrList.setStatus(EnovationConstants.statusFail);
			mstrList.setStatusCode(EnovationConstants.Code500);
			mstrList.setReason("#EXCEPTION OCCURED IN DELETESUGGESTIONTEMPLATE API:" + e.getMessage());
			return mstrList;
		}

	}

	@Override
	public MasterResponse deleteSuggestionJurySetup(int id) {
		MasterResponse mstrList = null;
		try {
			boolean isSave = iMasterDao.deleteSuggestionJurySetup(id);
			if (isSave) {
				mstrList = new MasterResponse();
				mstrList.setStatus(EnovationConstants.statusSuccess);
				mstrList.setStatusCode(EnovationConstants.Code200);
				mstrList.setResult(EnovationConstants.resultTrue);
				return mstrList;
			} else {
				mstrList = new MasterResponse();
				mstrList.setStatus(EnovationConstants.statusSuccess);
				mstrList.setStatusCode(EnovationConstants.Code200);
				mstrList.setResult(EnovationConstants.resultFalse);
				return mstrList;
			}
		} catch (Exception e) {
			mstrList = new MasterResponse();
			mstrList.setStatus(EnovationConstants.statusFail);
			mstrList.setStatusCode(EnovationConstants.Code500);
			mstrList.setResult(EnovationConstants.ResultFalse);
			mstrList.setReason("Exception Occured in delete Suggestion Jury Setup Api :" + e.getMessage());
			return mstrList;
		}
	}
	
	@Override
	public MasterResponse getRegistrationSourceList() {
		MasterResponse response = new MasterResponse();
		try {
			List<RegistrationSource> list = iMasterDao.getRegistrationSource();
			if (!list.isEmpty()) {
				response.setStatus(EnovationConstants.statusSuccess);
				response.setStatusCode(EnovationConstants.Code200);
				response.setReason(EnovationConstants.RECORDSFETCHSUCCESS);
				response.setResult(EnovationConstants.ResultTrue);
				response.setRegSource(list);
				return response;
			} else {
				response = new MasterResponse();
				response.setStatus(EnovationConstants.statusSuccess);
				response.setStatusCode(EnovationConstants.Code200);
				response.setReason(EnovationConstants.RecordsDoesNotExist);
				response.setResult(EnovationConstants.ResultFalse);
				return response;
			}

		} catch (Exception e) {
			response = new MasterResponse();
			response.setStatus(EnovationConstants.statusFail);
			response.setStatusCode(EnovationConstants.Code500);
			response.setReason("Exception Occured in get Registration Source  Api :" + e.getMessage());
			return response;
		}
	}

	@Override
	public MasterResponse getSuggestionKaizenParameters() {
		MasterResponse response = new MasterResponse();
		try {
			List<SuggestionEvalMstrParam> list = iMasterDao.getSuggestionKaizenParameters();
			if (!list.isEmpty()) {
				response.setStatus(EnovationConstants.statusSuccess);
				response.setStatusCode(EnovationConstants.Code200);
				response.setReason(EnovationConstants.RECORDSFETCHSUCCESS);
				response.setResult(EnovationConstants.ResultTrue);
				response.setSuggestionKaizenParameter(list);
				return response;
			} else {
				response = new MasterResponse();
				response.setStatus(EnovationConstants.statusSuccess);
				response.setStatusCode(EnovationConstants.Code200);
				response.setReason(EnovationConstants.RecordsDoesNotExist);
				response.setResult(EnovationConstants.ResultFalse);
				return response;
			}

		} catch (Exception e) {
			response = new MasterResponse();
			response.setStatus(EnovationConstants.statusFail);
			response.setStatusCode(EnovationConstants.Code500);
			response.setReason("Exception Occured in get Suggestion Kaizen Parameters Api :" + e.getMessage());
			return response;
		}
	}

	@Override
	public MasterResponse getSuggestionKaizenRatings(int branchId) {
		MasterResponse response = new MasterResponse();
		try {
			List<SuggestionEvalMstrRatings> list = iMasterDao.getSuggestionKaizenRatings(branchId);
			if (!list.isEmpty()) {
				response.setStatus(EnovationConstants.statusSuccess);
				response.setStatusCode(EnovationConstants.Code200);
				response.setReason(EnovationConstants.RECORDSFETCHSUCCESS);
				response.setResult(EnovationConstants.ResultTrue);
				response.setSuggestionKaizenRating(list);
				return response;
			} else {
				response = new MasterResponse();
				response.setStatus(EnovationConstants.statusSuccess);
				response.setStatusCode(EnovationConstants.Code200);
				response.setReason(EnovationConstants.RecordsDoesNotExist);
				response.setResult(EnovationConstants.ResultFalse);
				return response;
			}

		} catch (Exception e) {
			response = new MasterResponse();
			response.setStatus(EnovationConstants.statusFail);
			response.setStatusCode(EnovationConstants.Code500);
			response.setReason("Exception Occured in get Suggestion Kaizen Ratings Api :" + e.getMessage());
			return response;
		}
	}

	@Override
	public MasterResponse getSuggestionMasterAssesment() {
		MasterResponse response = new MasterResponse();
		try {
			List<SuggestionEvalMstrAssessment> list = iMasterDao.getSuggestionMasterAssesment();
			if (!list.isEmpty()) {
				response.setStatus(EnovationConstants.statusSuccess);
				response.setStatusCode(EnovationConstants.Code200);
				response.setReason(EnovationConstants.RECORDSFETCHSUCCESS);
				response.setResult(EnovationConstants.ResultTrue);
				response.setSuggMasterAssessment(list);
				return response;
			} else {
				response = new MasterResponse();
				response.setStatus(EnovationConstants.statusSuccess);
				response.setStatusCode(EnovationConstants.Code200);
				response.setReason(EnovationConstants.RecordsDoesNotExist);
				response.setResult(EnovationConstants.ResultFalse);
				return response;
			}

		} catch (Exception e) {
			response = new MasterResponse();
			response.setStatus(EnovationConstants.statusFail);
			response.setStatusCode(EnovationConstants.Code500);
			response.setReason("Exception Occured in get Suggestion Master Assesment Api :" + e.getMessage());
			return response;
		}
	}

	@Override
	public MasterResponse getSuggestionAssesmentInputByBranchId(Integer branchId) {
		MasterResponse response = new MasterResponse();
		try {
			List<SuggestionEvalAssessmentInput> list = iMasterDao.getSuggestionAssesmentInputByBranchId(branchId);
			if (!list.isEmpty()) {
				response.setStatus(EnovationConstants.statusSuccess);
				response.setStatusCode(EnovationConstants.Code200);
				response.setReason(EnovationConstants.RECORDSFETCHSUCCESS);
				response.setResult(EnovationConstants.ResultTrue);
				response.setSuggMasterAssessmentInput(list);
				return response;
			} else {
				response = new MasterResponse();
				response.setStatus(EnovationConstants.statusSuccess);
				response.setStatusCode(EnovationConstants.Code200);
				response.setReason(EnovationConstants.RecordsDoesNotExist);
				response.setResult(EnovationConstants.ResultFalse);
				return response;
			}

		} catch (Exception e) {
			response = new MasterResponse();
			response.setStatus(EnovationConstants.statusFail);
			response.setStatusCode(EnovationConstants.Code500);
			response.setReason("Exception Occured in get Suggestion Master Assesment Api :" + e.getMessage());
			return response;
		}
	}

	@Override
	public MasterResponse saveSuggestionAssessmentInput(SuggestionEvalAssessmentInput request) {
		MasterResponse mstrList = null;
		try {
			boolean isSave = iMasterDao.saveSuggestionAssessmentInput(request);
			if (isSave) {
				mstrList = new MasterResponse();
				mstrList.setStatus(EnovationConstants.statusSuccess);
				mstrList.setStatusCode(EnovationConstants.Code200);
				mstrList.setResult(EnovationConstants.resultTrue);
				return mstrList;
			} else {
				mstrList = new MasterResponse();
				mstrList.setStatus(EnovationConstants.statusSuccess);
				mstrList.setStatusCode(EnovationConstants.Code200);
				mstrList.setResult(EnovationConstants.resultFalse);
				return mstrList;
			}
		} catch (Exception e) {
			mstrList = new MasterResponse();
			mstrList.setStatus(EnovationConstants.statusFail);
			mstrList.setStatusCode(EnovationConstants.Code500);
			mstrList.setResult(EnovationConstants.ResultFalse);
			mstrList.setReason("Exception Occured in save Suggestion Assesment Inputs Setup Api :" + e.getMessage());
			return mstrList;
		}
	}

	@Override
	public MasterResponse updateSuggestionAssessmentInput(SuggestionEvalAssessmentInput request) {
		MasterResponse mstrList = null;
		try {
			boolean idUpdate = iMasterDao.updateSuggestionAssessmentInput(request);
			if (idUpdate) {
				mstrList = new MasterResponse();
				mstrList.setStatus(EnovationConstants.statusSuccess);
				mstrList.setStatusCode(EnovationConstants.Code200);
				mstrList.setResult(EnovationConstants.resultTrue);

				return mstrList;
			} else {
				mstrList = new MasterResponse();
				mstrList.setStatus(EnovationConstants.statusSuccess);
				mstrList.setStatusCode(EnovationConstants.Code200);
				mstrList.setResult(EnovationConstants.resultFalse);
				return mstrList;
			}
		} catch (Exception e) {
			mstrList = new MasterResponse();
			mstrList.setStatus(EnovationConstants.statusFail);
			mstrList.setStatusCode(EnovationConstants.Code500);
			mstrList.setResult(EnovationConstants.ResultFalse);
			mstrList.setReason("Exception Occured in update Suggestion Assesment Inputs Setup Api :" + e.getMessage());
			return mstrList;
		}
	}

	@Override
	public MasterResponse getSugEvalMasterNomination(int branchId) {
		MasterResponse response = new MasterResponse();
		try {
			List<SuggestionEvalMstrNomination> list = iMasterDao.getSugEvalMasterNomination(branchId);
			if (!list.isEmpty()) {
				response.setStatus(EnovationConstants.statusSuccess);
				response.setStatusCode(EnovationConstants.Code200);
				response.setReason(EnovationConstants.RECORDSFETCHSUCCESS);
				response.setResult(EnovationConstants.ResultTrue);
				response.setSuggestionEvalMstrNomination(list);
				return response;
			} else {
				response = new MasterResponse();
				response.setStatus(EnovationConstants.statusSuccess);
				response.setStatusCode(EnovationConstants.Code200);
				response.setReason(EnovationConstants.RecordsDoesNotExist);
				response.setResult(EnovationConstants.ResultFalse);
				return response;
			}

		} catch (Exception e) {
			response = new MasterResponse();
			response.setStatus(EnovationConstants.statusFail);
			response.setStatusCode(EnovationConstants.Code500);
			response.setReason("Exception Occured in get Suggestion Eval Mstr Nomination Api :" + e.getMessage());
			return response;
		}
	}

	@Override
	public MasterResponse addSugEvalAuthorityPhead(SuggestionEvalAuthoritySetup request) {
		MasterResponse mstrList = null;
		try {
			boolean isExist = iMasterDao.isPheadExist(request);
			if (isExist) {
				mstrList = new MasterResponse();
				mstrList.setStatus(EnovationConstants.statusFail);
				mstrList.setStatusCode(EnovationConstants.Code500);
				mstrList.setResult(EnovationConstants.ResultFalse);
				mstrList.setReason("Cannot add multiple Plant Head for same branch");
			} else {
				iMasterDao.addSugEvalAuthorityPhead(request);
				mstrList = new MasterResponse();
				mstrList.setStatus(EnovationConstants.statusSuccess);
				mstrList.setStatusCode(EnovationConstants.Code200);
				mstrList.setResult(EnovationConstants.resultTrue);
				mstrList.setReason("Plant Head Added Successfully");

			}
		} catch (Exception e) {
			mstrList = new MasterResponse();
			mstrList.setStatus(EnovationConstants.statusFail);
			mstrList.setStatusCode(EnovationConstants.Code500);
			mstrList.setResult(EnovationConstants.ResultFalse);
			mstrList.setReason("Exception Occured in add Suggestion Eval Authority Phead Api :" + e.getMessage());
			return mstrList;
		}
		return mstrList;
	}

	@Override
	public MasterResponse deleteSugEvalAuthority(int id) {
		MasterResponse mstrList = null;
		try {
			boolean isSave = iMasterDao.deleteSugEvalAuthority(id);
			if (isSave) {
				mstrList = new MasterResponse();
				mstrList.setStatus(EnovationConstants.statusSuccess);
				mstrList.setStatusCode(EnovationConstants.Code200);
				mstrList.setResult(EnovationConstants.resultTrue);
				return mstrList;
			} else {
				mstrList = new MasterResponse();
				mstrList.setStatus(EnovationConstants.statusSuccess);
				mstrList.setStatusCode(EnovationConstants.Code200);
				mstrList.setResult(EnovationConstants.resultFalse);
				return mstrList;
			}
		} catch (Exception e) {
			mstrList = new MasterResponse();
			mstrList.setStatus(EnovationConstants.statusFail);
			mstrList.setStatusCode(EnovationConstants.Code500);
			mstrList.setResult(EnovationConstants.ResultFalse);
			mstrList.setReason("Exception Occured in delete Sug Eval Authority Api :" + e.getMessage());
			return mstrList;
		}
	}

	@Override
	public MasterResponse addSugEvalAuthorityHR(SuggestionEvalAuthoritySetup request) {
		MasterResponse mstrList = null;
		try {
			boolean isSave = iMasterDao.addSugEvalAuthorityHR(request);
			if (isSave) {
				mstrList = new MasterResponse();
				mstrList.setStatus(EnovationConstants.statusSuccess);
				mstrList.setStatusCode(EnovationConstants.Code200);
				mstrList.setResult(EnovationConstants.resultTrue);

				return mstrList;
			} else {
				mstrList = new MasterResponse();
				mstrList.setStatus(EnovationConstants.statusSuccess);
				mstrList.setStatusCode(EnovationConstants.Code200);
				mstrList.setResult(EnovationConstants.resultFalse);
				return mstrList;
			}
		} catch (Exception e) {
			mstrList = new MasterResponse();
			mstrList.setStatus(EnovationConstants.statusFail);
			mstrList.setStatusCode(EnovationConstants.Code500);
			mstrList.setResult(EnovationConstants.ResultFalse);
			mstrList.setReason("Exception Occured in add Suggestion Eval Authority HR Api :" + e.getMessage());
			return mstrList;
		}
	}

	@Override
	public MasterResponse updateRewardCategory(RewardCategoryMaster rcm) {
		MasterResponse mstrList = null;
		try {
			boolean isUpdate = iMasterDao.updateRewardCategory(rcm);
			if (isUpdate) {
				mstrList = new MasterResponse();
				mstrList.setStatus(EnovationConstants.statusSuccess);
				mstrList.setStatusCode(EnovationConstants.Code200);
				mstrList.setResult(EnovationConstants.resultTrue);

				return mstrList;
			} else {
				mstrList = new MasterResponse();
				mstrList.setStatus(EnovationConstants.statusSuccess);
				mstrList.setStatusCode(EnovationConstants.Code200);
				mstrList.setResult(EnovationConstants.resultFalse);
				return mstrList;
			}
		} catch (Exception e) {
			mstrList = new MasterResponse();
			mstrList.setStatus(EnovationConstants.statusFail);
			mstrList.setStatusCode(EnovationConstants.Code500);
			mstrList.setResult(EnovationConstants.ResultFalse);
			mstrList.setReason("Exception Occured in update Reward Category Api :" + e.getMessage());
			return mstrList;
		}
	}

	@Override
	public MasterResponse deleteRewardCategory(int id) {
		MasterResponse mstrList = null;
		try {
			boolean isDelete = iMasterDao.deleteRewardCategory(id);
			if (isDelete) {
				mstrList = new MasterResponse();
				mstrList.setStatus(EnovationConstants.statusSuccess);
				mstrList.setStatusCode(EnovationConstants.Code200);
				mstrList.setResult(EnovationConstants.resultTrue);

				return mstrList;
			} else {
				mstrList = new MasterResponse();
				mstrList.setStatus(EnovationConstants.statusSuccess);
				mstrList.setStatusCode(EnovationConstants.Code200);
				mstrList.setResult(EnovationConstants.resultFalse);
				return mstrList;
			}
		} catch (Exception e) {
			mstrList = new MasterResponse();
			mstrList.setStatus(EnovationConstants.statusFail);
			mstrList.setStatusCode(EnovationConstants.Code500);
			mstrList.setResult(EnovationConstants.ResultFalse);
			mstrList.setReason("Exception Occured in delete reward category Api :" + e.getMessage());
			return mstrList;
		}
	}

	@Override
	public MasterResponse saveRewardAdminSetup(RewardAdminSetup request) {
		MasterResponse mstrList = null;
		try {
			boolean isSave = iMasterDao.saveRewardAdminSetup(request);
			if (isSave) {
				mstrList = new MasterResponse();
				mstrList.setStatus(EnovationConstants.statusSuccess);
				mstrList.setStatusCode(EnovationConstants.Code200);
				mstrList.setResult(EnovationConstants.resultTrue);

				return mstrList;
			} else {
				mstrList = new MasterResponse();
				mstrList.setStatus(EnovationConstants.statusSuccess);
				mstrList.setStatusCode(EnovationConstants.Code200);
				mstrList.setResult(EnovationConstants.resultFalse);
				return mstrList;
			}
		} catch (Exception e) {
			mstrList = new MasterResponse();
			mstrList.setStatus(EnovationConstants.statusFail);
			mstrList.setStatusCode(EnovationConstants.Code500);
			mstrList.setResult(EnovationConstants.ResultFalse);
			mstrList.setReason("Exception Occured in save Reward Admin Setup Api :" + e.getMessage());
			return mstrList;
		}
	}

	@Override
	public MasterResponse deleteRewardAdminSetup(RewardAdminSetup request) {
		MasterResponse mstrList = null;
		try {
			boolean isDelete = iMasterDao.deleteRewardAdminSetup(request);
			if (isDelete) {
				mstrList = new MasterResponse();
				mstrList.setStatus(EnovationConstants.statusSuccess);
				mstrList.setStatusCode(EnovationConstants.Code200);
				mstrList.setResult(EnovationConstants.resultTrue);

				return mstrList;
			} else {
				mstrList = new MasterResponse();
				mstrList.setStatus(EnovationConstants.statusSuccess);
				mstrList.setStatusCode(EnovationConstants.Code200);
				mstrList.setResult(EnovationConstants.resultFalse);
				return mstrList;
			}
		} catch (Exception e) {
			mstrList = new MasterResponse();
			mstrList.setStatus(EnovationConstants.statusFail);
			mstrList.setStatusCode(EnovationConstants.Code500);
			mstrList.setResult(EnovationConstants.ResultFalse);
			mstrList.setReason("Exception Occured in delete Reward Admin Setup Api :" + e.getMessage());
			return mstrList;
		}
	}

	@Override
	public MasterResponse getRewardAdminList(int branchId) {
		MasterResponse mstrList = null;
		try {
			List<RewardAdminSetup> list = iMasterDao.getRewardAdminList(branchId);
			if (!list.isEmpty()) {
				mstrList = new MasterResponse();
				mstrList.setStatus(EnovationConstants.statusSuccess);
				mstrList.setStatusCode(EnovationConstants.Code200);
				mstrList.setResult(EnovationConstants.resultTrue);
				mstrList.setRewardAdminList(list);
				return mstrList;
			} else {
				mstrList = new MasterResponse();
				mstrList.setStatus(EnovationConstants.statusSuccess);
				mstrList.setStatusCode(EnovationConstants.Code200);
				mstrList.setResult(EnovationConstants.resultFalse);
				return mstrList;
			}
		} catch (Exception e) {
			mstrList = new MasterResponse();
			mstrList.setStatus(EnovationConstants.statusFail);
			mstrList.setStatusCode(EnovationConstants.Code500);
			mstrList.setResult(EnovationConstants.ResultFalse);
			mstrList.setReason("Exception Occured in delete Reward Admin Setup Api :" + e.getMessage());
			return mstrList;
		}
	}

	@Override
	public MasterResponse getRewardAdminListByEmpId(int empId) {
		MasterResponse mstrList = null;
		try {
			boolean isExist = iMasterDao.getRewardAdminListByEmpId(empId);
			if (isExist) {
				mstrList = new MasterResponse();
				mstrList.setStatus(EnovationConstants.statusSuccess);
				mstrList.setStatusCode(EnovationConstants.Code200);
				mstrList.setResult(EnovationConstants.resultTrue);
				return mstrList;
			} else {
				mstrList = new MasterResponse();
				mstrList.setStatus(EnovationConstants.statusSuccess);
				mstrList.setStatusCode(EnovationConstants.Code200);
				mstrList.setResult(EnovationConstants.resultFalse);
				return mstrList;
			}
		} catch (Exception e) {
			mstrList = new MasterResponse();
			mstrList.setStatus(EnovationConstants.statusFail);
			mstrList.setStatusCode(EnovationConstants.Code500);
			mstrList.setResult(EnovationConstants.ResultFalse);
			mstrList.setReason("Exception Occured in get reward admin list by empId Api :" + e.getMessage());
			return mstrList;
		}
	}

	@Override
	public MasterResponse getRedemptionTrackerList(Integer branchId) {
		MasterResponse mstrList = null;
		try {
			List<RedeemCartRewards> list = iMasterDao.getRedemptionTrackerList(branchId);
			if (!list.isEmpty()) {
				mstrList = new MasterResponse();
				mstrList.setStatus(EnovationConstants.statusSuccess);
				mstrList.setStatusCode(EnovationConstants.Code200);
				mstrList.setResult(EnovationConstants.resultTrue);
				mstrList.setRedeemCartList(list);
				return mstrList;
			} else {
				mstrList = new MasterResponse();
				mstrList.setStatus(EnovationConstants.statusSuccess);
				mstrList.setStatusCode(EnovationConstants.Code200);
				mstrList.setResult(EnovationConstants.resultFalse);
				return mstrList;
			}
		} catch (Exception e) {
			mstrList = new MasterResponse();
			mstrList.setStatus(EnovationConstants.statusFail);
			mstrList.setStatusCode(EnovationConstants.Code500);
			mstrList.setResult(EnovationConstants.ResultFalse);
			mstrList.setReason("Exception Occured in get Redemption Tracker List Api :" + e.getMessage());
			return mstrList;
		}
	}

	@Override
	public MasterResponse addOrUpdateSuggConfig(SuggestionFlowConfig config) {
		MasterResponse response = new MasterResponse();
		try {
			int result = iMasterDao.addOrUpdateSuggConfig(config);
			if (result > 0) {
				response.setStatus(EnovationConstants.statusSuccess);
				response.setStatusCode(EnovationConstants.Code200);
				response.setResult(EnovationConstants.ResultTrue);
				if (result == EnovationConstants.ONE) {
					response.setReason("CONFIG UPDATED SUCCESSFULLY");
				} else {
					response.setReason("CONFIG ADDED SUCCESSFULLY");
				}

			} else {
				response.setStatus(EnovationConstants.statusSuccess);
				response.setStatusCode(EnovationConstants.CODE400);
				response.setReason(EnovationConstants.BAD_REQUEST);
			}
		} catch (Exception e) {
			response = new MasterResponse();
			response.setStatus(EnovationConstants.statusFail);
			response.setStatusCode(EnovationConstants.Code500);
			response.setReason("EXCEPTION OCCURED IN ADD OR UPDATE SUGG CONFIG  API :" + e.getMessage());
		}
		return response;
	}

	@Override
	public MasterResponse getSuggConfigList(Integer orgId) {
		MasterResponse response = new MasterResponse();
		try {
			List<SuggestionFlowConfig> list = iMasterDao.getSuggConfigList(orgId);
			if (list != null && !list.isEmpty()) {
				response.setStatus(EnovationConstants.statusSuccess);
				response.setStatusCode(EnovationConstants.Code200);
				response.setResult(EnovationConstants.ResultTrue);
				response.setReason(EnovationConstants.RECORDSFETCHSUCCESS);
				response.setSuggFlowConfig(list);
				return response;
			} else {

				response = new MasterResponse();
				response.setStatus(EnovationConstants.statusSuccess);
				response.setStatusCode(EnovationConstants.Code200);
				response.setResult(EnovationConstants.ResultFalse);
				response.setReason(EnovationConstants.RecordsDoesNotExist);
				return response;
			}

		} catch (Exception e) {
			response = new MasterResponse();
			response.setStatus(EnovationConstants.statusFail);
			response.setStatusCode(EnovationConstants.Code500);
			response.setResult(EnovationConstants.ResultFalse);
			response.setReason("EXCEPTION OCCURED IN GET SUGGESSTION CONFIG LIST  API :" + e.getMessage());
			return response;
		}
	}
	
	@Override
	public MasterResponse getOrgDataByAlieas(String alieas) {
		MasterResponse response = new MasterResponse();
		try {
			List<OrganizationMaster> list = iMasterDao.getOrgDataByAlieas(alieas);
			if (list != null && !list.isEmpty()) {
				response.setStatus(EnovationConstants.statusSuccess);
				response.setStatusCode(EnovationConstants.Code200);
				response.setResult(EnovationConstants.ResultTrue);
				response.setReason(EnovationConstants.RECORDSFETCHSUCCESS);
				response.setOrgList(list);
				return response;
			} else {
				response = new MasterResponse();
				response.setStatus(EnovationConstants.statusSuccess);
				response.setStatusCode(EnovationConstants.Code200);
				response.setResult(EnovationConstants.ResultFalse);
				response.setReason(EnovationConstants.RecordsDoesNotExist);
				return response;
			}
		} catch (Exception e) {
			response = new MasterResponse();
			response.setStatus(EnovationConstants.statusFail);
			response.setStatusCode(EnovationConstants.Code500);
			response.setResult(EnovationConstants.ResultFalse);
			response.setReason("EXCEPTION OCCURED IN GET ORG DATA BY ALIEAS  API :" + e.getMessage());
			commonUtils.sendAPIFailedExceptionMail("Exception Occured At - " + this.getClass().getSimpleName(),
					" <p><b> Method Name - </b>" + new Throwable().getStackTrace()[0].getMethodName() + " <br></p> "
							+ "<p><b> Path Variable - </b>" + alieas + "<p><b> Exception - </b>"
							+ ExceptionUtils.getStackTrace(e));
			return response;
		}
	}

	@Override
	public MasterResponse removeLineDetails(Line line) {
		MasterResponse mstrList = null;
		try {
			boolean result = iMasterDao.removeLineDetails(line);
			System.out.println("save" + result);
			if (result) {
				System.out.println("Inside remove line if block");
				mstrList = new MasterResponse();
				mstrList.setStatus(EnovationConstants.statusSuccess);
				mstrList.setStatusCode(EnovationConstants.Code200);
				mstrList.setResult(EnovationConstants.resultTrue);
				return mstrList;
			} else {
				System.out.println("Inside remove line else block");
				mstrList = new MasterResponse();
				mstrList.setStatus(EnovationConstants.statusFail);
				mstrList.setStatusCode(EnovationConstants.Code200);
				mstrList.setResult(EnovationConstants.resultFalse);
				return mstrList;
			}

		} catch (Exception e) {
			mstrList = new MasterResponse();
			Throwable t = e.getCause();
			System.out.println("Cause ==> " + e.getCause());
			while ((t != null) && !(t instanceof ConstraintViolationException)) {
				t = t.getCause();
			}
			if (t instanceof ConstraintViolationException) {

				System.out.println(
						"Inside Constraint Violation Exception Service Impl ==========================>>>>>>>>>>>>>>>>>>>>>>>>>>>."
								+ e.getCause());

				boolean flag = iMasterDao.deactiveLine(line);
				if (flag) {
					System.out.println("Inside deactive line if block");
					mstrList.setStatus(EnovationConstants.statusSuccess);
					mstrList.setStatusCode(EnovationConstants.Code200);
					mstrList.setResult(EnovationConstants.ResultTrue);

				} else {
					System.out.println("Inside deactive line else block");
					mstrList.setStatus(EnovationConstants.statusFail);
					mstrList.setStatusCode(EnovationConstants.Code200);
					mstrList.setResult(EnovationConstants.ResultFalse);
				}

			} else {
				System.out.println("Exception Occured");
				mstrList.setStatus(EnovationConstants.statusFail);
				mstrList.setStatusCode(EnovationConstants.Code500);
				mstrList.setResult(EnovationConstants.ResultFalse);
				mstrList.setReason("#INSIDE EXCEPTION OCCURED DELETE LINE API : " + e.getMessage());

			}
			Gson gson = new Gson();
			String json = gson.toJson(line);
			commonUtils.sendAPIFailedExceptionMail("Exception Occured At - " + this.getClass().getSimpleName(),
					" <p><b> Method Name - </b>" + new Throwable().getStackTrace()[0].getMethodName() + " <br></p> "
							+ "<p><b> Request Body - </b>" + json + "<p><b> Exception - </b>"
							+ ExceptionUtils.getStackTrace(e));
		}
		return mstrList;
	}

	@Override
	public MasterResponse updateOptionalJurySetup(SuggestionEvalJurySetup request) {

		MasterResponse mstrList = null;
		try {
			if (request != null) {
				boolean result = iMasterDao.updateOptionalJurySetup(request);
				if (result) {
					mstrList = new MasterResponse();
					mstrList.setStatus(EnovationConstants.statusSuccess);
					mstrList.setStatusCode(EnovationConstants.Code200);
					mstrList.setResult(EnovationConstants.resultTrue);
					return mstrList;
				} else {
					mstrList = new MasterResponse();
					mstrList.setStatus(EnovationConstants.statusSuccess);
					mstrList.setStatusCode(EnovationConstants.Code200);
					mstrList.setResult(EnovationConstants.resultFalse);
					return mstrList;
				}
			} else {
				mstrList = new MasterResponse();
				mstrList.setStatus(EnovationConstants.statusFail);
				mstrList.setStatusCode(EnovationConstants.CODE400);
				mstrList.setResult(EnovationConstants.resultFalse);
				mstrList.setReason(EnovationConstants.BAD_REQUEST);
				return mstrList;
			}
		} catch (Exception e) {
			e.printStackTrace();
			mstrList = new MasterResponse();
			mstrList.setStatus(EnovationConstants.statusFail);
			mstrList.setStatusCode(EnovationConstants.Code500);
			mstrList.setResult(EnovationConstants.ResultFalse);
			mstrList.setReason(EnovationConstants.SERVER_EORROR);
			Gson gson = new Gson();
			String json = gson.toJson(request);
			commonUtils.sendAPIFailedExceptionMail("Exception Occured At - " + this.getClass().getSimpleName(),
					" <p><b> Method Name - </b>" + new Throwable().getStackTrace()[0].getMethodName() + " <br></p> "
							+ "<p><b> Request Body - </b>" + json + "<p><b> Exception - </b>"
							+ ExceptionUtils.getStackTrace(e));

			return mstrList;
		}
	}

	@Override
	public MasterResponse saveSuggestionUserSetup(SuggestionUserSetup request) {
		MasterResponse mstrResponse = new MasterResponse();
		boolean isRoleExist = false;
		try {
			if (request.getUserType().getId() == EnovationConstants.ONE) {
				isRoleExist = iMasterDao.isEmpRoleExist(request);
				if (isRoleExist) {
					iMasterDao.saveSuggestionUserSetup(request);
					return BuildResponse.success(mstrResponse);
				} else {
					iMasterDao.insertIntoEmpRole(request);
					iMasterDao.saveSuggestionUserSetup(request);
					return BuildResponse.success(mstrResponse);
				}
			} else {
				iMasterDao.saveSuggestionUserSetup(request);
				return BuildResponse.success(mstrResponse);
			}
		} catch (Exception e) {
			e.printStackTrace();
			Gson gson = new Gson();
			String json = gson.toJson(request);
			commonUtils.sendAPIFailedExceptionMail("Exception Occured At - " + this.getClass().getSimpleName(),
					" <p><b> Method Name - </b>" + new Throwable().getStackTrace()[0].getMethodName() + " <br></p> "
							+ "<p><b> Request Body - </b>" + json + "<p><b> Exception - </b>"
							+ ExceptionUtils.getStackTrace(e));
			return BuildResponse.internalServerError(mstrResponse);
		}
	}
	
	@Override
	public MasterResponse deleteSuggestionUserSetup(SuggestionUserSetup request) {
		MasterResponse mstrResponse = new MasterResponse();
		List<Object[]> existCount = new ArrayList<>();
		boolean isPendingActionsExists = false;
		boolean isRoleExist = false;
		try {
			isPendingActionsExists = iMasterDao.isEmpPendingActionsExists(request);
			if (isPendingActionsExists) {
				mstrResponse.setReason("");
				return BuildResponse.failWithCode100PendingActions(mstrResponse);
			} else {
				existCount = iMasterDao.getEmpUserSetupData(request);
				if (existCount.size() == EnovationConstants.ONE) {
					iMasterDao.deleteSuggestionUserSetup(request);
					isRoleExist = iMasterDao.isEmpRoleExist(request);
					if (isRoleExist) {
						iMasterDao.deleteFromEmpRoles(request);
					}
					return BuildResponse.success(mstrResponse);
				} else {
					iMasterDao.deleteSuggestionUserSetup(request);
					return BuildResponse.success(mstrResponse);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			Gson gson = new Gson();
			String json = gson.toJson(request);
			commonUtils.sendAPIFailedExceptionMail("Exception Occured At - " + this.getClass().getSimpleName(),
					" <p><b> Method Name - </b>" + new Throwable().getStackTrace()[0].getMethodName() + " <br></p> "
							+ "<p><b> Request Body - </b>" + json + "<p><b> Exception - </b>"
							+ ExceptionUtils.getStackTrace(e));
			return BuildResponse.internalServerError(mstrResponse);
		}

	}

	@Override
	public MasterResponse getSuggUserTypes() {
		MasterResponse mstrList = new MasterResponse();
		try {
			List<MasterSugUserType> userTypes = iMasterDao.getSuggUserTypes();
			if (userTypes != null && !userTypes.isEmpty()) {
				mstrList.setSugUserTypes(userTypes);
				return BuildResponse.success(mstrList);
			} else {
				return BuildResponse.fail(mstrList);
			}
		} catch (Exception e) {
			return BuildResponse.internalServerError(mstrList);
		}
	}

	@Override
	public MasterResponse addUpdateParticularsPoints(ParticularsPoint particulars) {
		MasterResponse mstrList = null;
		try {
			boolean result = iMasterDao.addUpdateParticularsPoints(particulars);
			if (particulars != null) {
				if (result) {
					mstrList = new MasterResponse();
					mstrList.setStatus(EnovationConstants.statusSuccess);
					mstrList.setStatusCode(EnovationConstants.Code200);
					mstrList.setResult(EnovationConstants.resultTrue);
					return mstrList;
				} else {
					mstrList = new MasterResponse();
					mstrList.setStatus(EnovationConstants.statusSuccess);
					mstrList.setStatusCode(EnovationConstants.Code200);
					mstrList.setResult(EnovationConstants.resultFalse);
					return mstrList;
				}
			} else {
				mstrList = new MasterResponse();
				mstrList.setStatus(EnovationConstants.statusFail);
				mstrList.setStatusCode(EnovationConstants.CODE400);
				mstrList.setResult(EnovationConstants.resultFalse);
				mstrList.setReason(EnovationConstants.BAD_REQUEST);
				return mstrList;
			}
		} catch (Exception e) {
			mstrList = new MasterResponse();
			mstrList.setStatus(EnovationConstants.statusFail);
			mstrList.setStatusCode(EnovationConstants.Code500);
			mstrList.setResult(EnovationConstants.ResultFalse);
			mstrList.setReason(EnovationConstants.SERVER_EORROR);
			Gson gson = new Gson();
			String json = gson.toJson(particulars);
			commonUtils.sendAPIFailedExceptionMail("Exception Occured At - " + this.getClass().getSimpleName(),
					" <p><b> Method Name - </b>" + new Throwable().getStackTrace()[0].getMethodName() + " <br></p> "
							+ "<p><b> Request Body - </b>" + json + "<p><b> Exception - </b>"
							+ ExceptionUtils.getStackTrace(e));

			return mstrList;
		}
	}

	/**
	 * @author Vinay B. Nov 24, 2020 2:40:17 PM
	 * @return
	 */
	@Override
	public MasterResponse getMasterUnits() {
		MasterResponse res = new MasterResponse();
		try {
			List<MasterUnit> unitList = iMasterDao.getMasterUnits();
			if (unitList != null && !unitList.isEmpty()) {
				res.setUnitList(unitList);
				BuildResponse.success(res);
			} else {
				BuildResponse.fail(res);
			}
		} catch (Exception e) {
			BuildResponse.internalServerError(res);
		}

		return res;
	}


	@Override
	public MasterResponse employeeWiseHappinessIndex(MasterDTO request) {
		LOGGER.info("In Master Service | employeeWiseHappinessIndex ");
		MasterResponse res = new MasterResponse();
		try {
			List<MasterDTO> happinessList = iMasterDao.employeeWiseHappinessIndex(request);
			if (CollectionUtils.isNotEmpty(happinessList)) {
				res.setHappinessList(happinessList);
				BuildResponse.success(res);
			} else {
				BuildResponse.fail(res);
			}
		} catch (Exception e) {
			BuildResponse.internalServerError(res);
		}
		return res;
	}
	
	/**
	 * @author rakesh 12 August
	 */
	@Override
	public MasterResponse addOrUpdateCategoryList(MasterDTO request) {
		MasterResponse res = new MasterResponse();

		try {
			boolean result = iMasterDao.addOrUpdateCategoryList(request);
			if (result) {
				BuildResponse.success(res);
			} else {
				BuildResponse.fail(res);
			}
		} catch (Exception e) {
			BuildResponse.internalServerError(res);
		}

		return res;
	}
	
	@Override
	public MasterResponse addCWAgencyDetails(CWAgency request) {
		LOGGER.info("Inside MasterService | addCWAgencyDetails ");
		MasterResponse mstrList = new MasterResponse();
		try {
			boolean flag = iMasterDao.addCWAgencyDetails(request);
			if (flag) {
				BuildResponse.success(mstrList);
				mstrList.setReason(EnovationConstants.RECORD_ADDED);
			} else {
				BuildResponse.fail(mstrList);
				mstrList.setReason(EnovationConstants.RECORD_NOT_ADDED);
			}
		} catch (Exception e) {
			BuildResponse.internalServerError(mstrList);
			LOGGER.info("Inside MasterService | addCWAgencyDetails  Exception Occured " + e.getMessage());
			Gson gson = new Gson();
			String json = gson.toJson(request);
			commonUtils.sendAPIFailedExceptionMail("Exception Occured At - " + this.getClass().getSimpleName(),
					" <p><b> Method Name - </b>" + new Throwable().getStackTrace()[0].getMethodName() + " <br></p> "
							+ "<p><b> Request Body - </b>" + json + "<p><b> Exception - </b>"
							+ ExceptionUtils.getStackTrace(e));
		}
		return mstrList;
	}

	@Override
	public MasterResponse getCWAgencyList(Integer orgId) {
		LOGGER.info("Inside MasterService | getAllCWAgencyList ");
		MasterResponse mstrList = new MasterResponse();
		try {
			List<CWAgency> list = iMasterDao.getCWAgencyList(orgId);
			if (!CollectionUtils.isEmpty(list)) {
				BuildResponse.success(mstrList);
				mstrList.setReason(EnovationConstants.RECORDSFETCHSUCCESS);
				mstrList.setCwAgencyList(list);
			} else {
				BuildResponse.fail(mstrList);
				mstrList.setReason(EnovationConstants.RecordsDoesNotExist);
			}
		} catch (Exception e) {
			BuildResponse.internalServerError(mstrList);
			LOGGER.info("Inside MasterService | getAllCWAgencyList  Exception Occured " + e.getMessage());
			Gson gson = new Gson();
			String json = gson.toJson(orgId);
			commonUtils.sendAPIFailedExceptionMail("Exception Occured At - " + this.getClass().getSimpleName(),
					" <p><b> Method Name - </b>" + new Throwable().getStackTrace()[0].getMethodName() + " <br></p> "
							+ "<p><b> Request Body - </b>" + json + "<p><b> Exception - </b>"
							+ ExceptionUtils.getStackTrace(e));
		}
		return mstrList;
	}

	@Override
	public MasterResponse addDocument(MasterDocument request) {
		LOGGER.info("Inside MasterService | addDocument ");
		MasterResponse res = new MasterResponse();
		try {
			boolean flag = iMasterDao.addDocument(request);
			if (flag) {
				BuildResponse.success(res);
				res.setReason(EnovationConstants.RECORD_ADDED);
			} else {
				BuildResponse.fail(res);
			}
		} catch (Exception e) {
			e.printStackTrace();
			BuildResponse.internalServerError(res);
			LOGGER.info("Inside MasterService | addDocument  Exception Occured " + e.getMessage());
			Gson gson = new Gson();
			String json = gson.toJson(request);
			commonUtils.sendAPIFailedExceptionMail("Exception Occured At - " + this.getClass().getSimpleName(),
					" <p><b> Method Name - </b>" + new Throwable().getStackTrace()[0].getMethodName() + " <br></p> "
							+ "<p><b> Request Body - </b>" + json + "<p><b> Exception - </b>"
							+ ExceptionUtils.getStackTrace(e));
		}
		return res;
	}

	@Override
	public MasterResponse updateDocument(MasterDocument request) {
		LOGGER.info("Inside MasterService | updateDocument ");
		MasterResponse res = new MasterResponse();
		try {
			boolean flag = iMasterDao.updateDocument(request);
			if (flag) {
				BuildResponse.success(res);
				res.setReason(EnovationConstants.RECORD_UPDATED);
			} else {
				BuildResponse.fail(res);
				res.setReason(EnovationConstants.RecordsDoesNotExist);
			}
		} catch (Exception e) {
			BuildResponse.internalServerError(res);
			LOGGER.info("Inside MasterService | updateDocument  Exception Occured " + e.getMessage());
			Gson gson = new Gson();
			String json = gson.toJson(request);
			commonUtils.sendAPIFailedExceptionMail("Exception Occured At - " + this.getClass().getSimpleName(),
					" <p><b> Method Name - </b>" + new Throwable().getStackTrace()[0].getMethodName() + " <br></p> "
							+ "<p><b> Request Body - </b>" + json + "<p><b> Exception - </b>"
							+ ExceptionUtils.getStackTrace(e));
		}
		return res;
	}

	@Override
	public MasterResponse removeDocument(MasterDocument request) {
		LOGGER.info("Inside MasterService | updateDocument ");
		MasterResponse res = new MasterResponse();
		try {
			boolean flag = iMasterDao.removeDocument(request);
			if (flag) {
				BuildResponse.success(res);
				res.setReason(EnovationConstants.RECORD_DELETED);
			} else {
				BuildResponse.fail(res);
				res.setReason(EnovationConstants.RecordsDoesNotExist);
			}
		} catch (Exception e) {
			boolean flag = CommonUtils.isConstraintViolationException(e);
			if (flag) {
				res.setReason(EnovationConstants.ALREADY_IN_USE);
				BuildResponse.fail100(res);
			} else {
			BuildResponse.internalServerError(res);
			LOGGER.info("Inside MasterService | removeDocument  Exception Occured " + e.getMessage());
			Gson gson = new Gson();
			String json = gson.toJson(request);
			commonUtils.sendAPIFailedExceptionMail("Exception Occured At - " + this.getClass().getSimpleName(),
					" <p><b> Method Name - </b>" + new Throwable().getStackTrace()[0].getMethodName() + " <br></p> "
							+ "<p><b> Request Body - </b>" + json + "<p><b> Exception - </b>"
							+ ExceptionUtils.getStackTrace(e));
			}
		}
		return res;
	}

}
