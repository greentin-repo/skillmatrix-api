package com.greentin.enovation.master;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.regex.Pattern;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.Tuple;
import javax.transaction.Transactional;

import org.apache.commons.collections.CollectionUtils;
import org.hibernate.Session;
import org.hibernate.TransactionException;
import org.hibernate.query.Query;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.env.Environment;
import org.springframework.core.task.TaskExecutor;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import com.google.gson.Gson;
import com.greentin.enovation.accesscontrol.ProductOrgConfig;
import com.greentin.enovation.accesscontrol.Role;
import com.greentin.enovation.accesscontrol.RoleName;
import com.greentin.enovation.audit.AuditComponent;
import com.greentin.enovation.beans.BenefitAnalysisMasterDTO;
import com.greentin.enovation.beans.Branch;
import com.greentin.enovation.beans.DepartmentLevelBean;
import com.greentin.enovation.beans.EmployeeDetailsBean;
import com.greentin.enovation.beans.ExecutiveListDto;
import com.greentin.enovation.beans.MoodIndicatorDto;
import com.greentin.enovation.beans.RolesBean;
import com.greentin.enovation.communication.ICommunication;
import com.greentin.enovation.config.EnovationConfig;
import com.greentin.enovation.dto.MailDTO;
import com.greentin.enovation.dto.MasterDTO;
import com.greentin.enovation.employee.EmployeeDaoImple;
import com.greentin.enovation.model.ActivityMaster;
import com.greentin.enovation.model.BenefitAnalysisMaster;
import com.greentin.enovation.model.BranchMaster;
import com.greentin.enovation.model.BrowniePointsReimbursementCycle;
import com.greentin.enovation.model.BrowniePointsReimbursementCycleType;
import com.greentin.enovation.model.CWAgency;
import com.greentin.enovation.model.Cart;
import com.greentin.enovation.model.CategoryMaster;
import com.greentin.enovation.model.ContentSetupExists;
import com.greentin.enovation.model.Countries;
import com.greentin.enovation.model.Currency;
import com.greentin.enovation.model.DepartmentLevel;
import com.greentin.enovation.model.DepartmentLevelEvaluators;
import com.greentin.enovation.model.DepartmentMaster;
import com.greentin.enovation.model.EmailTemplateMaster;
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
import com.greentin.enovation.setup.SetupConfigDao;
import com.greentin.enovation.utils.BaseRepository;
import com.greentin.enovation.utils.CommonUtils;
import com.greentin.enovation.utils.DBUtils;
import com.greentin.enovation.utils.EnovationConstants;
import com.greentin.enovation.utils.MailUtil;
import com.greentin.enovation.utils.WriteFilesUtils;

@Component
@Transactional
@SuppressWarnings("unchecked")
public class MasterDaoImpl extends BaseRepository implements IMasterDao {
	private static final Logger LOGGER = LoggerFactory.getLogger(MasterDaoImpl.class);

	@Autowired
	Environment env;

	@Autowired
	AuditComponent audit;

	@Autowired
	@Qualifier("taskExecutor")
	private TaskExecutor taskExecutor;

	@Autowired
	EnovationConfig enoConfig;

	@Autowired
	EmployeeDaoImple employeeDaoImple;

	/*
	 * @Autowired UserDao usereDaoImple;
	 */

	@Autowired
	EntityManager entityManager;

	@Autowired
	IMasterDao iMasterDao;

	@Autowired
	SetupConfigDao setupConfigDaoInterface;

	@Autowired
	private ICommunication communication;
	
	@Autowired
	DBUtils dbUtils;

	public static String getBenefitList = "SELECT ben.benfit_id,ben.benefits,ben.created_date,ben.updated_date,ben.is_enable,ben.org_id "
			+ " FROM master_benefit_analysis ben  WHERE (ben.org_id=:orgId or ben.branch_id=:branchId) and ben.is_enable=:isEnable";
	public static String GET_EXECUTIVELIST_BY_ORGID_APP = "SELECT exe.id, exe.type_of_executive, e.first_name, e.last_name, exe.emp_id, e.designation "
			+ " from executive exe,tbl_employee_details e " + "  where exe.emp_id=e.emp_id "
			+ "    AND exe.org_id= :orgId " + "    AND exe.is_deactive= :isDeactive  group by exe.id; ";

	@Override
	public List<DepartmentMaster> getdeptList() {
		LOGGER.info("# INSIDE in getdeptList ");
		return getCurrentSession().createQuery("from DepartmentMaster").getResultList();
	}

	@Override
	public List<CategoryMaster> getCatagoryList() {
		LOGGER.info("# INSIDE in getCatagoryList ");
		return getCurrentSession().createQuery("from CategoryMaster").getResultList();
	}

	@Override
	public boolean updateDepartment(DepartmentMaster dept) {
		boolean flag = false;
		LOGGER.info("#INSIDE IN UPDATEDEPARTMENT ");
		if (dept.getOrgId() != 0)
			dept.setOrganisation(new OrganizationMaster(dept.getOrgId()));
		if (dept.getBranchId() != 0)
			dept.setBranch(new BranchMaster(dept.getBranchId()));
		Session session = getCurrentSession();
		try {
			String action = "Update Department";
			String previous = null;
			String current = null;
			List<Object[]> obj = session
					.createNativeQuery("select dept_name ,created_date from master_department where dept_id=:deptId")
					.setParameter("deptId", dept.getDeptId()).getResultList();
			if (obj != null & obj.size() > 0) {
				for (Object[] row : obj) {
					previous = String.valueOf(row[0].toString());
				}
			}
			int updatedBy = 0;
			current = dept.getDeptName();
			if (dept.getUpdatedBy() != 0) {
				dept.setUpdatedById(dept.getUpdatedBy());
				updatedBy = dept.getUpdatedBy();
			}
			dept.setUpdatedDate(CommonUtils.currentDate());
			audit.insertDepartmentMasterAudit(session, action, updatedBy, previous, current);
			session.update(dept);
			flag = true;
		} catch (Exception e) {
			LOGGER.error("#INSIDE EXCEPTION OCCURED IN UPDATEDEPARTMENT " + e.getMessage());
			e.printStackTrace();
		}
		return flag;
	}

	@Override
	public boolean addTeamType(TeamtypeMaster teamType) {
		boolean flag = false;
		LOGGER.info("# INSIDE in addTeamType ");
		Session session = getCurrentSession();
		try {
			teamType.setCreatedDate(CommonUtils.currentDate());
			session.saveOrUpdate(teamType);
			flag = true;
		} catch (Exception e) {
			LOGGER.error("# INSIDE Exception Occured in addTeamType " + e.getMessage());
		}
		return flag;
	}

	@Override
	public List<TeamtypeMaster> getTeamTypeList() {
		LOGGER.info("#INSIDE IN GETTEAMTYPELIST ");
		return getCurrentSession().createQuery("from TeamtypeMaster").getResultList();
	}

	@Override
	public List<StatusMaster> getStatusMasterList() {
		LOGGER.info("# INSIDE in getStatusMasterList ");
		return getCurrentSession().createQuery("from StatusMaster").getResultList();
	}

	@Override
	public List<OrganizationMaster> getOrgMasterList() {
		LOGGER.info("# INSIDE in getOrgMasterList ");
		return getCurrentSession().createQuery("from OrganizationMaster").getResultList();
	}

	@Override
	public OrganizationMaster addOrgMaster(OrganizationMaster org) {
		Session session = getCurrentSession();
		org.setCreatedDate(CommonUtils.currentDate());
		session.saveOrUpdate(org);
		return org;
	}

	@Override
	public boolean checkDepartmentRecords(DepartmentMaster dept) {
		LOGGER.info("# INSIDE in checkDepartmentRecords ");
		List<DepartmentMaster> deptList = getCurrentSession()
				.createQuery("from DepartmentMaster where deptName=:deptname")
				.setParameter("deptname", dept.getDeptName()).getResultList();
		if (deptList.size() > 0) {
			return true;
		} else {
			return false;
		}
	}

	@Override
	public boolean addOrganization(OrganizationMaster org) {
		boolean flag = false;
		LOGGER.info("# INSIDE in addOrganization ");
		Session session = getCurrentSession();
		try {
			org.setCreatedDate(CommonUtils.currentDate());
			session.saveOrUpdate(org);
			flag = true;
		} catch (Exception e) {
			LOGGER.info("# INSIDE Exception Occured  in addOrganization " + e.getMessage());
		}
		return flag;
	}

	@Override
	public boolean deleteOrganization(OrganizationMaster organizationMaster) {
		boolean flag = false;
		LOGGER.info("# INSIDE in deleteOrganization ");
		Session session = getCurrentSession();
		try {
			session.remove(session.merge(organizationMaster));
			flag = true;
		} catch (Exception e) {
			LOGGER.info("# INSIDE Exception Occured  in deleteOrganization " + e.getMessage());
		}
		return flag;
	}

	@Override
	public boolean deleteCategory(CategoryMaster cate) {
		boolean flag = false;
		LOGGER.info("# INSIDE in deleteCategory ");
		Session session = getCurrentSession();
		try {
			String action = "Delete Category";
			String previous = null;
			List<Object[]> obj = session
					.createNativeQuery("select category_name from master_category where cat_id=:catId")
					.setParameter("catId", cate.getCatId()).getResultList();
			if (obj != null && obj.size() > 0) {
				for (Object row : obj) {
					previous = String.valueOf(row.toString());

				}
			}
			int deletedBy = 0;
			if (cate.getUpdatedBy() != 0) {
				deletedBy = cate.getUpdatedBy();
			}
			audit.insertCategoryMasterAudit(session, action, deletedBy, previous, " ");
			session.createNativeQuery("delete from master_category where cat_id=:catId")
					.setParameter("catId", cate.getCatId()).executeUpdate();
			flag = true;
		} catch (Exception e) {
			LOGGER.error("# INSIDE Exception Occured in deleteCategory " + e.getMessage());
		}
		return flag;
	}

	/*
	 * @Override public List<DesignationMaster> getDesignationList() {
	 * LOGGER.info("# INSIDE in getDesignationList "); List<DesignationMaster>
	 * designationList=getCurrentSession().createQuery("from DesignationMaster").
	 * getResultList(); return designationList; }
	 */

	@Override
	public int deleteDepartment(DepartmentMaster dept) {
		int value = 0;
		LOGGER.info("# INSIDE in deleteDepartment ");
		Session session = getCurrentSession();
		try {
			String action = "Delete Department";
			String previous = null;
			List<Object[]> obj = session
					.createNativeQuery("select dept_name from master_department where dept_id=:deptId")
					.setParameter("deptId", dept.getDeptId()).getResultList();
			if (obj != null && obj.size() > 0) {
				for (Object row : obj) {
					previous = String.valueOf(row.toString());
				}
			}
			int deletedBy = 0;
			if (dept.getUpdatedBy() != 0) {
				deletedBy = dept.getUpdatedBy();
			}
			audit.insertDepartmentMasterAudit(session, action, deletedBy, previous, " ");
			int result = session.createQuery("delete DepartmentMaster where deptId = :ID")
					.setParameter("ID", dept.getDeptId()).executeUpdate();
			if (result > 0)
				value = 1;
		} catch (Exception e) {
			value = 2;
			LOGGER.info("# INSIDE INSIDE in deleteDepartment " + e.getMessage());
		}
		return value;
	}

	@Override
	public List<PointLevelMaster> getPointLevelList(Integer orgId) {
		LOGGER.info("# INSIDE in getPointLevelList ");
		return getCurrentSession().createQuery("from PointLevelMaster WHERE org_id=:orgId").setParameter("orgId", orgId)
				.getResultList();
	}

	@Override
	public boolean addCategory(CategoryMaster cat) {
		boolean flag = false;
		Session session = getCurrentSession();
		try {
			String action = "";
			String previous = "";
			int actionBy = 0;
			LOGGER.info("#INSIDE IN ADDCATEGORY ");
			if (cat.getOrgId() != 0) {
				cat.setOrganisation(new OrganizationMaster(cat.getOrgId()));
			}
			if (cat.getBranchId() != 0) {
				cat.setBranch(new BranchMaster(cat.getBranchId()));
			}
			if (cat.getCatId() != 0) {
				action = "Update Category";
				List<Object[]> obj = session.createNativeQuery(
						"select category_name ,created_date, created_by from master_category where cat_id=:catId")
						.setParameter("catId", cat.getCatId()).getResultList();
				if (obj != null && obj.size() > 0) {
					for (Object[] row : obj) {
						previous = String.valueOf(row[0].toString());
						if (row[1] != null) {
							cat.setCreatedDate(Timestamp.valueOf(row[1].toString()));
						}
						if (row[2] != null) {
							cat.setCreatedById(new EmployeeDetails(Integer.valueOf(row[2].toString())));
						}
					}
				}
				if (cat.getUpdatedBy() != 0) {
					cat.setUpdatedById(new EmployeeDetails(cat.getUpdatedBy()));
					actionBy = cat.getUpdatedBy();
				}
				cat.setUpdatedDate(CommonUtils.currentDate());

			} else {
				action = "Add Category";
				if (cat.getCreatedBy() != 0) {
					cat.setCreatedById(new EmployeeDetails(cat.getCreatedBy()));
					actionBy = cat.getCreatedBy();
				}
				cat.setCreatedDate(CommonUtils.currentDate());
				actionBy = cat.getCreatedBy();
			}
			audit.insertCategoryMasterAudit(session, action, actionBy, previous, cat.getCategoryName());
			session.saveOrUpdate(cat);
			flag = true;
		} catch (Exception e) {
			LOGGER.error("#INSIDE EXCEPTION OCCURED IN ADDCATEGORY " + e.getMessage());
			e.printStackTrace();
		}
		return flag;
	}

	@Override
	public boolean isexitsorg(OrganizationMaster org) {
		boolean flag = false;
		LOGGER.info("# INSIDE in isexitsorg ");
		List<OrganizationMaster> orgniz = getCurrentSession().createQuery("from OrganizationMaster where name=:name")
				.setParameter("name", org.getName()).getResultList();
		if (orgniz.size() == 0) {
			flag = true;
		} else {
			flag = false;
		}
		return flag;

	}

	@Override
	public boolean isexitscat(CategoryMaster cate) {
		boolean flag = false;

		LOGGER.info("#INSIDE IN ISEXITSCAT ");
		List<CategoryMaster> category = getCurrentSession()
				.createQuery("from CategoryMaster where categoryName=:categoryName and  branch.branchId=:branchId")

				.setParameter("categoryName", cate.getCategoryName())
				.setParameter("branchId", cate.getBranch().getBranchId()).getResultList();
		if (category.size() == 0) {
			flag = true;
		} else {
			flag = false;
		}
		return flag;
	}

	@Override
	public boolean isexitsdept(DepartmentMaster dept) {
		boolean flag = false;
		LOGGER.info("# INSIDE in isexitsdept ");
		List<DepartmentMaster> deptobj = getCurrentSession()
				.createQuery("from DepartmentMaster where deptName=:deptName")
				.setParameter("deptName", dept.getDeptName()).getResultList();
		if (deptobj.size() == 0) {
			flag = true;
		} else {
			flag = false;
		}
		return flag;
	}

	@Override
	public boolean isexitsteamtype(TeamtypeMaster teamType) {
		boolean flag = false;
		LOGGER.info("# INSIDE in isexitsteamtype ");
		List<CategoryMaster> team = getCurrentSession().createQuery("from TeamtypeMaster where type=:type")
				.setParameter("type", teamType.getType()).getResultList();
		if (team.size() == 0) {
			flag = true;
		} else {
			flag = false;
		}
		return flag;
	}

	@Override
	public List<StatusMaster> getMasterStatusListByRoleId(List<Integer> roleIds) {
		LOGGER.info("####INSIDE IN getMasterStatusList");
		List<StatusMaster> list = new ArrayList<>();
		try {
			list = getCurrentSession().createQuery("from StatusMaster where roleId IN :roleIds")
					.setParameter("roleIds", roleIds).getResultList();
		} catch (Exception e) {
			LOGGER.info("####INSIDE IN getMasterStatusList Exception=" + e.getMessage());
		}
		return list;
	}

	@Override
	public List<BenefitAnalysisMasterDTO> getBenefitAnalysisList(Integer orgId, Integer branchId) {
		LOGGER.info("#INSIDE IN GETBENEFITANALYSISLIST ");
		// return getCurrentSession().createQuery("from
		// BenefitAnalysisMaster").getResultList();
		List<BenefitAnalysisMasterDTO> benefitArrList = new ArrayList<>();
		try {
			List<Object[]> benefitList = getCurrentSession().createNativeQuery(
					"SELECT ben.benfit_id,ben.benefits,ben.created_date,ben.updated_date,ben.is_enable,ben.org_id,ben.points "
							+ " FROM master_benefit_analysis ben  WHERE (ben.org_id=:orgId or ben.branch_id=:branchId)")
					.setParameter("orgId", orgId).setParameter("branchId", branchId).getResultList();
			for (Object[] benObj : benefitList) {
				BenefitAnalysisMasterDTO dto = new BenefitAnalysisMasterDTO();
				if (benObj[0] != null) {
					dto.setBenfitId(Integer.valueOf(String.valueOf(benObj[0])));
				}
				if (benObj[1] != null) {
					dto.setBenefits(String.valueOf(benObj[1]));
				}
				if (benObj[2] != null) {
					dto.setCreatedDate(String.valueOf(benObj[2]).substring(0, 10));
				}
				if (benObj[3] != null) {
					dto.setUpdatedDate(String.valueOf(benObj[3]).substring(0, 10));
				}
				if (benObj[4] != null) {
					dto.setIsEnable(Integer.valueOf(String.valueOf(benObj[4])));
				}
				if (benObj[5] != null) {
					dto.setOrgId(Integer.valueOf(String.valueOf(benObj[5])));
				}
				if (benObj[6] != null) {
					dto.setPoints(Integer.valueOf(String.valueOf(benObj[6])));
				}
				benefitArrList.add(dto);
			}
		} catch (Exception e) {
			LOGGER.error("#INSIDE EXCEPTION OCCURED IN  GETBENEFITANALYSISLIST :" + e.getMessage());
		}
		return benefitArrList;
	}

	@Override
	public List<BranchMaster> getbranchlist(Integer orgId) {
		LOGGER.info("# INSIDE in getbranchlist ");
		return getCurrentSession().createQuery("FROM BranchMaster WHERE org.orgId=:orgId").setParameter("orgId", orgId)
				.getResultList();
	}

	@SuppressWarnings("unused")
	@Override
	public boolean addbranch(BranchMaster brnch) {
		boolean flag = false;
		LOGGER.info("#INSIDE IN ADDBRANCH ");
		Session session = getCurrentSession();
		try {
			/*
			 * String action = "Add Branch"; String previousValue = "  ";
			 * brnch.setCreatedById(new EmployeeDetails(0));// brnch.getCreatedBy()));
			 * brnch.setOrg(new OrganizationMaster(brnch.getOrgId()));
			 * brnch.setCreatedDate(CommonUtils.currentDate()); session.save(brnch); String
			 * current = brnch.getName().concat("," + brnch.getLocation()); int createdBy =
			 * 0; if (brnch.getCreatedBy() != 0) { createdBy = brnch.getCreatedBy(); }
			 * audit.insertBranchMasterAudit(session, action, createdBy, previousValue,
			 * current);
			 */
			/*
			 * BranchMaster addbranch=new BranchMaster();
			 * addbranch.setLocation(brnch.getLocation());
			 * addbranch.setName(brnch.getName()); addbranch.setOrg(new
			 * OrganizationMaster(brnch.getOrgId()));
			 * addbranch.setCreatedDate(CommonUtils.currentDate()); Object obj=
			 * session.save(addbranch); Role role =isRole((RoleName.SUPERADMIN).toString());
			 * if(obj!=null) { //add entry in product-org config table to map product with
			 * org ProductOrgConfig prorg=new ProductOrgConfig(); prorg.setOrg(new
			 * OrganizationMaster(brnch.getOrgId())); prorg.setBranch(new
			 * BranchMaster(addbranch.getBranchId())); prorg.setProduct(new
			 * Product(brnch.getProductId())); // get From Data Base
			 * prorg.setSubscriptionPlan(new SubscriptionPlan(brnch.getSubscripId()));
			 * prorg.setDeactivationDate(CommonUtils.deactivateDate(15)); // Need to Replace
			 * Dynamic session.save(prorg);
			 * 
			 * 
			 * ADDED BRANCH SETUP CONFIG ENTRY WHILE CREATING A NEW BRANCH
			 * 
			 * List<SetupMaster>setupMasterList=setupConfigDaoInterface.listofSetupConfig();
			 * if(setupMasterList != null && setupMasterList.size()>0) { for(SetupMaster
			 * details:setupMasterList) { BranchSetupConfig config=new BranchSetupConfig();
			 * config.setBranchMaster(new BranchMaster(addbranch.getBranchId()));
			 * config.setSetupMaster(new SetupMaster(details.getSetupId()));
			 * session.save(config); }
			 * 
			 * }
			 * 
			 * 
			 * 
			 * Role roleEmployee=new Role(); roleEmployee.setId((long)
			 * EnovationConstants.EMPLOYEE_ROLEID);
			 * //masterDaoImpl.saveuserrole(empDetails.getEmpId(),roleEmployee);
			 * 
			 * List<String> masterBenifitList = iMasterDao.getBenefitAnalysisMasterList();
			 * if(masterBenifitList!=null && masterBenifitList.size() > 0) { for (String
			 * benefitAnalysisMaster : masterBenifitList) { BenefitAnalysisMaster abcd=new
			 * BenefitAnalysisMaster(); abcd.setBenefits(benefitAnalysisMaster);
			 * abcd.setOrg(new OrganizationMaster(brnch.getOrgId())); abcd.setBranch(new
			 * BranchMaster(addbranch.getBranchId())); abcd.setEmpDetails(new
			 * EmployeeDetails(brnch.getCreatedBy())); session.save(abcd);
			 * //iMasterDao.addbenefitmaster(benefitAnalysisMaster); } }
			 * List<CategoryMaster> categoryList = iMasterDao.getMasterCatagoryList();
			 * if(categoryList!=null && categoryList.size() > 0) { for (CategoryMaster
			 * categoryMaster : categoryList) { CategoryMaster cat=new CategoryMaster();
			 * cat.setCategoryName(categoryMaster.getCategoryName());
			 * cat.setOrganisation(new OrganizationMaster(brnch.getOrgId()));
			 * cat.setBranch(new BranchMaster(addbranch.getBranchId())); session.save(cat);
			 * List<String> particularsList =
			 * iMasterDao.getParticularsList(categoryMaster.getCatId());
			 * if(particularsList!=null && particularsList.size() > 0) { for (String
			 * particulars : particularsList) { Particulars par=new Particulars();
			 * par.setCategory(new CategoryMaster(cat.getCatId()));
			 * par.setParticulars(particulars); par.setCreatedById(brnch.getCreatedBy());
			 * session.save(par); } } } } boolean result=
			 * usereDaoImple.isRoleExist(addbranch.getBranchId(),brnch.getOrgId(),role.getId
			 * ());
			 * 
			 * if superadmin role is already exist in data base
			 * 
			 * if(!result) { Set<RoleMenuAccess> roleData
			 * =usereDaoImple.getRoleMenuAccess(brnch.getProductId(),addbranch.getBranchId()
			 * ,brnch.getOrgId(),role.getId()); if(roleData!=null&&roleData.size() >0 ) {
			 * usereDaoImple.saveRoleAccessList(roleData); }
			 * 
			 * Set<RoleMenuAccess> roleDataEmployee =
			 * usereDaoImple.getRoleMenuAccessEmployee(brnch.getProductId(),addbranch.
			 * getBranchId(),brnch.getOrgId(),EnovationConstants.EMPLOYEE_ROLEID);
			 * if(roleDataEmployee!=null&&roleDataEmployee.size() >0 ) {
			 * usereDaoImple.saveRoleAccessList(roleDataEmployee); }
			 * 
			 * ADD ESCALATION ALERT
			 * 
			 * for(Long roleIds:EnovationConstants.ESCALATION_ROLE_LIST) {
			 * RoleEscalationSetup setup=new RoleEscalationSetup(new Role(roleIds),new
			 * BranchMaster(addbranch.getBranchId())); setup.setRole(new Role(roleIds));
			 * setup.setBranchMaster(new BranchMaster(branch.getBranchId()));
			 * setup.setNumberOfDays(EnovationConstants.ZERO); session.save(setup);
			 * SuggestionEscalationConfig sEConfig=new SuggestionEscalationConfig( new
			 * BranchMaster(addbranch.getBranchId())); session.save(sEConfig);
			 * BrowniePointsReimbursementCycle browniePointConfig = new
			 * BrowniePointsReimbursementCycle(new
			 * BrowniePointsReimbursementCycleType(EnovationConstants.BROWNIE_POINTS_NONE),
			 * new BranchMaster(addbranch.getBranchId())); session.save(browniePointConfig);
			 * } }
			 * 
			 * }
			 */
			flag = true;
		} catch (Exception e) {
			LOGGER.error("#INSIDE EXCEPTION OCCURED IN ADDBRANCH " + e.getMessage());
		}
		return flag;
	}

	@Override
	public boolean addbenefitmasternew(BenefitAnalysisMaster benefit, BenefitAnalysisMaster oldBenefit) {
		boolean flag = false;
		LOGGER.info("#INSIDE IN ADDBENEFITMASTER ");
		Session session = getCurrentSession();
		try {
			if (benefit.getCreatedById() != 0) {
				benefit.setEmpDetails(new EmployeeDetails(benefit.getCreatedById()));
			}
			benefit.setOrg(new OrganizationMaster(benefit.getOrgId()));
			benefit.setBranch(new BranchMaster(benefit.getBranchId()));
			benefit.setCreatedDate(CommonUtils.currentDate());
			Gson gson = new Gson();
			String currentValue = gson.toJson(benefit);
			String previousValue = null;
			if (oldBenefit != null) {
				Gson oldgson = new Gson();
				previousValue = oldgson.toJson(oldBenefit);
			}

			if (oldBenefit.getBenfitId() != 0) {
				int updatedBy = 0;
				if (benefit.getUpdatedBy() != 0) {
					updatedBy = benefit.getUpdatedBy();
				}
				audit.insertBenefitAudit(session, "Update Benefit", updatedBy, previousValue, currentValue);
			} else {
				int createdBy = 0;
				if (benefit.getCreatedBy() != 0) {
					createdBy = benefit.getUpdatedBy();
				}
				audit.insertBenefitAudit(session, "Add Benefit", createdBy, " ", currentValue);
			}
			session.saveOrUpdate(benefit);
			flag = true;
		} catch (Exception e) {
			LOGGER.error("# INSIDE Exception Occured in addbenefitmaster " + e.getMessage());
		}
		return flag;
	}

	@Override
	public boolean addbenefitmaster(BenefitAnalysisMaster benefit) {
		boolean flag = false;
		LOGGER.info("#INSIDE IN ADDBENEFITMASTER ");
		Session session = getCurrentSession();
		try {
			if (benefit.getCreatedById() != 0) {
				benefit.setEmpDetails(new EmployeeDetails(benefit.getCreatedById()));
			}
			benefit.setOrg(new OrganizationMaster(benefit.getOrgId()));
			benefit.setBranch(new BranchMaster(benefit.getBranchId()));
			benefit.setCreatedDate(CommonUtils.currentDate());
			session.saveOrUpdate(benefit);
			flag = true;
		} catch (Exception e) {
			LOGGER.error("# INSIDE Exception Occured in addbenefitmaster " + e.getMessage());
		}
		return flag;
	}

	@Override
	public List<DepartmentMaster> getdepartmentlistbyorgid(Integer orgId) {
		LOGGER.info("#INSIDE IN GETDEPARTMENTLISTBYORGID ");
		return getCurrentSession().createNativeQuery("SELECT * from master_department d  WHERE d.org_id=:orgId")
				.addEntity(DepartmentMaster.class).setParameter("orgId", orgId).getResultList();

	}

	@Override
	@Transactional
	public boolean saveExecutive(Executive executive) {
		LOGGER.info("#INSIDE IN saveExecutive ");
		boolean flag = false;
		Session session = getCurrentSession();
		try {
			String action = "Add Executive";
			executive.setOrg(new OrganizationMaster(executive.getOrgId()));
			executive.setEmp(new EmployeeDetails(executive.getEmpId()));
			executive.setCreatedDate(CommonUtils.currentDate());
			int createdBy = 0;
			if (executive.getCreatedBy() != 0) {
				executive.setCreatedById(new EmployeeDetails(executive.getCreatedBy()));
				createdBy = executive.getCreatedBy();
			}
			audit.insertEmpRolesAudit(session, action, createdBy, 0, executive.getEmpId(), 0);
			Object obj = session.save(executive);
			if (obj != null) {
				// Role role = isRole((RoleName.EXECUTIVE).toString());
				boolean insertStatus = saveuserrole(executive.getEmpId(), EnovationConstants.EXECUTIVE);
				if (insertStatus) {
					if (executive.getIsSetupCompleted() > 0) {
						employeeDaoImple.sendMailTo_ROLE_CHANGED_Employees(executive.getEmailId(),
								executive.getFirstName(), executive.getLastName(), (RoleName.EXECUTIVE).toString(),
								EnovationConstants.ROLE_ADDED);
					}
				}
			}

			/*
			 * if(executiveObj!=null) { DepartmentLevel deptlvl=new DepartmentLevel();
			 * deptlvl.setIsExecutive(EnovationConstants.ENABLE_STATUS);
			 * deptlvl.setEmpDetails(new EmployeeDetails(executive.getcEmpId()));
			 * deptlvl.setLevel(EnovationConstants.ENABLE_STATUS);
			 * if(executive.getLevelName()!=null)
			 * {deptlvl.setLevelName(executive.getLevelName());} deptlvl.setExecutive(new
			 * Executive(executive.getId())); session.save(deptlvl); flag=true; }
			 */
			flag = true;
			System.out.println("execId " + executive.getId());
			// System.out.println("orgId "+executive.getOrg().getOrgId());
			// if(emp!=null) {
			// }
		} catch (Exception e) {
			LOGGER.info("#INSIDE EXCPTION OCCURED IN saveExecutive " + e.getMessage());

		}
		return flag;
	}

	@Override
	@Transactional
	public boolean updateExecutive(Executive executive) {
		LOGGER.info("#INSIDE IN updateExecutive ");
		boolean flag = false;
		Session session = getCurrentSession();
		try {
			String action = "Update Executive";
			Executive executiveUpdate = (Executive) session.get(Executive.class, executive.getId());
			int previous = executiveUpdate.getEmpId();
			if (executive.getEmpId() != 0) {
				executiveUpdate.setEmp(new EmployeeDetails(executive.getEmpId()));
			}
			if (executive.getTypeOfExecutive() != null) {
				executiveUpdate.setTypeOfExecutive(executive.getTypeOfExecutive());
			}
			executiveUpdate.setUpdatedDate(CommonUtils.currentDate());
			int updatedBy = 0;

			if (executive.getUpdatedBy() != 0) {
				executiveUpdate.setUpdatedById(new EmployeeDetails(executive.getUpdatedBy()));
				updatedBy = executive.getUpdatedBy();
			}
			audit.insertEmpRolesAudit(session, action, updatedBy, previous, executive.getEmpId(), 0);
			session.update(executiveUpdate);
			flag = true;

		} catch (Exception e) {
			LOGGER.info("#INSIDE EXCPTION OCCURED IN saveExecutive " + e.getMessage());

		}
		return flag;
	}

	@Override
	@Transactional
	public boolean updateDeptlvlExecutive(DepartmentLevel deptexecutive) {
		LOGGER.info("#INSIDE IN updateDeptlvlExecutive ");
		boolean flag = false;
		Session session = getCurrentSession();
		try {
			DepartmentLevel deptexecutiveUpdate = (DepartmentLevel) session.get(DepartmentLevel.class,
					deptexecutive.getId());
			if (deptexecutive.getLevelName() != null) {
				deptexecutiveUpdate.setLevelName(deptexecutive.getLevelName());
			}
			if (deptexecutive.getEmpId() != 0) {
				deptexecutiveUpdate.setEmpDetails(new EmployeeDetails(deptexecutive.getEmpId()));
			}
			// if(deptexecutive.getExecId()!=0) {deptexecutiveUpdate.setExecutive(new
			// Executive(deptexecutive.getExecId()));}
			deptexecutiveUpdate.setUpdatedDate(CommonUtils.currentDate());
			session.update(deptexecutiveUpdate);
			flag = true;

		} catch (Exception e) {
			LOGGER.info("#INSIDE EXCPTION OCCURED IN updateDeptlvlExecutive " + e.getMessage());

		}
		return flag;
	}

	@Override
	public boolean removeExecutive(Integer execId/* ,Integer empId */) {
		LOGGER.info("#INSIDE IN removeExecutive ");
		boolean flag = false;
		Session session = getCurrentSession();
		try {
			String action = "Remove Executive";
			Executive executiveUpdate = (Executive) session.get(Executive.class, execId);
			int previous = executiveUpdate.getEmp().getEmpId();
			Role role = isRole((RoleName.EXECUTIVE).toString());
			boolean status = inflightRoleCheck(executiveUpdate.getEmp().getEmpId(), role.getId(), 0);
			if (status) {
				return flag;
			} else {
				audit.insertEmpRolesAudit(session, action, 0/* empId */, previous, 0, 0);
				session.createNativeQuery("delete from executive where exec_id=:execId").setParameter("execId", execId)
						.executeUpdate();
				removeuserrole(executiveUpdate.getEmp().getEmpId(), role.getId());
				flag = true;
			}
		} catch (Exception e) {
			LOGGER.info("#INSIDE EXCPTION OCCURED IN REMOVE_EXECUTIVE " + e.getMessage());

		}
		return flag;
	}

	@Override
	public List<ExecutiveListDto> getListOfExecutiveByOrgId(Integer orgId) {
		LOGGER.info("# INSIDE in getListOfExecutiveByOrgId ");
		// return getCurrentSession().createQuery("from
		// BenefitAnalysisMaster").getResultList();
		List<ExecutiveListDto> ListOfExecutiveArr = null;
		try {
			List<Object[]> ExecutiveList = getCurrentSession().createNativeQuery(GET_EXECUTIVELIST_BY_ORGID_APP)
					.setParameter("orgId", orgId)
					// .setParameter("branchId", branchId)
					.setParameter("isDeactive", EnovationConstants.DESABLE_STATUS).getResultList();
			ListOfExecutiveArr = new ArrayList<>();
			for (Object[] execObj : ExecutiveList) {
				ExecutiveListDto dto = new ExecutiveListDto();
				if (execObj[0] != null) {
					dto.setId(Integer.valueOf(String.valueOf(execObj[0])));
				}
				if (execObj[1] != null) {
					dto.setTypeOfExecutive(String.valueOf(execObj[1]));
				}
				if (execObj[2] != null && execObj[3] == null) {
					dto.setExecName(String.valueOf(execObj[2]));
				} else if (execObj[2] != null && execObj[3] != null) {
					dto.setExecName(String.valueOf(execObj[2]) + " " + String.valueOf(execObj[3]));
				}
				if (execObj[4] != null) {
					dto.setExecId(Integer.valueOf(String.valueOf(execObj[4])));
				}
				if (execObj[5] != null) {
					dto.setDesignation(String.valueOf(execObj[5]));
				}
				ListOfExecutiveArr.add(dto);
			}
		} catch (Exception e) {
			LOGGER.error("#INSIDE EXCEPTION OCCURED IN  GETLISTOFEXECUTIVEBYORGID" + e.getMessage());
		}
		return ListOfExecutiveArr;
	}

	@Override
	public List<DepartmentMaster> getdepartmentlistbybranchid(Integer branchId) {
		LOGGER.info("# INSIDE in getdepartmentlistbybranchid ");
		return getCurrentSession().createNativeQuery("SELECT d.* from master_department d WHERE d.branch_id=:branchId ")
				.addEntity(DepartmentMaster.class).setParameter("branchId", branchId).getResultList();
	}

	@Override
	public List<DepartmentMaster> getDepartmentListForSuggestion(Integer branchId) {
		LOGGER.info("# INSIDE IN GETDEPARTMENTLISTFORSUGGESTION ");
		return getCurrentSession().createNativeQuery(
				"SELECT d.* from master_department d, dept_level dl WHERE d.dept_id = dl.dept_id and  d.branch_id=:branchId group by dl.dept_id")
				.addEntity(DepartmentMaster.class).setParameter("branchId", branchId).getResultList();
	}

	@Override
	public List<CategoryMaster> getCategoryListbyorgId(Integer orgId, Integer branchId) {
		LOGGER.info("#INSIDE IN GETCATEGORYLISTBYORGID ");
		StringBuffer buf = new StringBuffer("SELECT * from master_category d WHERE ");
		if (orgId > 0 && branchId > 0) {
			buf.append("d.org_id=orgId AND d.branch_id=branchId");
		} else if (orgId > 0 && branchId == 0) {
			buf.append("d.org_id=orgId ");
		} else if (orgId == 0 && branchId > 0) {
			buf.append(" d.branch_id=branchId");
		}

		return getCurrentSession()
				.createNativeQuery(
						(buf.toString()).replace("orgId", orgId.toString()).replace("branchId", branchId.toString()))
				.addEntity(CategoryMaster.class).getResultList();
	}
	
	@Override
	public boolean addDept(DepartmentMaster dept) {
		boolean flag = false;
		LOGGER.info("#INSIDE IN ADDDEPARTMENT ");
		Session session = getCurrentSession();
		try {
			int createdBy = 0;
			String action = "Add Department";
			if (dept.getCreatedBy() != 0) {
				dept.setCreatedById(dept.getCreatedBy());
				createdBy = dept.getCreatedBy();
			}
			dept.setBranch(new BranchMaster(dept.getBranchId()));
			dept.setOrganisation(new OrganizationMaster(dept.getOrgId()));
			dept.setCreatedDate(CommonUtils.currentDate());
			audit.insertDepartmentMasterAudit(session, action, createdBy, " ", dept.getDeptName());
			session.save(dept);
			flag = true;
		} catch (Exception e) {
			LOGGER.error("#INSIDE EXCEPTION OCCURED IN ADDDEPARTMENT " + e.getMessage());
		}
		return flag;
	}

	@Override
	public int saveLevelWiseUsers(Set<DepartmentLevel> requestList) {
		LOGGER.info("#INSIDE IN SAVELEVELWISEUSERS ");
		int flag = 0;
		Session session = getCurrentSession();
		try {
			for (DepartmentLevel request : requestList) {

				request.setEmpDetails(new EmployeeDetails(request.getEmpId()));
				request.setBranch(new BranchMaster(request.getBranchId()));
				request.setDept(new DepartmentMaster(request.getDeptId()));

				// Role role = isRole((RoleName.CONTROLLER).toString());

				if (request.getRoleId() == EnovationConstants.CONTROLLER) {

					System.out.println("employee email id : " + request.getEmailId());
					System.out.println("first name  id : " + request.getFirstName());
					System.out.println("last name id : " + request.getLastName());
					System.out.println("is Active id : " + request.getIsActive());
					request.setCreatedDate(CommonUtils.currentDate());

					System.out.println("if loop is Active id : " + request.getIsActive());
					// TimeUnit.SECONDS.sleep(40);
					System.out.println("if loop is Active id sleep: " + request.getIsActive());

					Object obj = session.save(request);
					if (obj != null) {
						List<DepartmentLevel> list = session
								.createQuery("from DepartmentLevel where empDetails.empId = :empId")
								.setParameter("empId", request.getEmpId()).getResultList();
						if (list.size() == EnovationConstants.ONE) {
							audit.insertEmpRolesAudit(session, "Add controller", request.getCreatedBy(), 0,
									request.getEmpId(), list.get(0).getId());

							boolean insertStatus = saveuserrole(request.getEmpId(), EnovationConstants.CONTROLLER);
							if (insertStatus) {
								if (request.getIsSetupCompleted() > 0) {
									employeeDaoImple.sendMailToImportedEmployees(request.getEmailId(),
											request.getFirstName(), request.getLastName(), "",
											EnovationConstants.SEND_MAIL_TO_CONTROLLER);
								}
							}
						}
						if (list.size() > 1) {
							if (request.getIsSetupCompleted() > 0) {
								employeeDaoImple.sendMailToImportedEmployees(request.getEmailId(),
										request.getFirstName(), request.getLastName(), "",
										EnovationConstants.SEND_MAIL_TO_CONTROLLER);
							}
						}
					}
					flag = 1;
					return flag;
				} else if (request.getRoleId() == EnovationConstants.EVALUATOR) {
					String roleName = "";
					DepartmentLevelEvaluators eval = new DepartmentLevelEvaluators();
					BeanUtils.copyProperties(request, eval);
					System.out.println("eval employee email id : " + eval.getEmailId());
					System.out.println("eval first name  id : " + eval.getFirstName());
					System.out.println("eval last name id : " + eval.getLastName());
					System.out.println("eval is Active id : " + eval.getIsActive());
					if (request.getRoleName() != null) {
						roleName = request.getRoleName();
					}
					System.out.println("if loop is Active id : " + request.getIsActive());
					// TimeUnit.SECONDS.sleep(40);
					System.out.println("if loop is Active id sleep: " + request.getIsActive());

					Object obj = session.save(eval);
					if (obj != null) {
						List<DepartmentLevelEvaluators> list = session
								.createQuery("from DepartmentLevelEvaluators where empDetails.empId = :empId")
								.setParameter("empId", request.getEmpId()).getResultList();
						if (list.size() == EnovationConstants.ONE) {
							audit.insertEmpRolesAudit(session, "Add controller", request.getCreatedBy(), 0,
									request.getEmpId(), list.get(0).getId());

							boolean insertStatus = saveuserrole(eval.getEmpId(), EnovationConstants.EVALUATOR);
							if (insertStatus) {
								if (eval.getIsSetupCompleted() > 0) {
									employeeDaoImple.sendMailTo_ROLE_CHANGED_Employees(eval.getEmailId(),
											eval.getFirstName(), eval.getLastName(), roleName,
											EnovationConstants.ROLE_ADDED);
								}
							}
						}
						if (list.size() > 1) {
							if (eval.getIsSetupCompleted() > 0) {
								employeeDaoImple.sendMailTo_ROLE_CHANGED_Employees(eval.getEmailId(),
										eval.getFirstName(), eval.getLastName(), roleName,
										EnovationConstants.ROLE_ADDED);
							}
						}
					}
					flag = 1;
					return flag;
				}

			}
		} catch (Exception e) {
			LOGGER.error("#INSIDE EXCEPTION OCCURED IN SAVELEVELWISEUSERS " + e.getMessage());
			e.printStackTrace();
		}
		return flag;
	}

	/*
	 * #######################VVVIMP METHODS########################################
	 */
	public boolean saveuserrole(int empId, int role) {
		LOGGER.info("#INSIDE IN SAVEEMPROLE");
		boolean flag = false;
		Session session = getCurrentSession();
		try {
			int checkEmpRoles = 0;
			try {
				checkEmpRoles = (int) session
						.createNativeQuery("select emp_id  from emp_roles where emp_id=:empId and role_id=:roleId")
						.setParameter("empId", empId).setParameter("roleId", role).getSingleResult();
			} catch (NoResultException e) {

			}
			if (checkEmpRoles > 0) {
				return false;
			} else {
				int status = session.createNativeQuery("insert into emp_roles(emp_id,role_id) values(:empId,:roleId)")

						.setParameter("empId", empId).setParameter("roleId", role).executeUpdate();
				System.out.println("role push to emproles successfully : " + status);
				if (status > 0) {
					flag = true;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			LOGGER.info("#INSIDE IN CATCH SAVEEMPROLE" + e.getMessage());
		}
		return flag;
	}

	public void removeControllerUser(int empId, int id) {
		LOGGER.info("#REMOVE IN REMOVEEMPROLE");
		Session session = getCurrentSession();
		try {
			session.createNativeQuery("delete from dept_level where emp_id=:empId and id=:id").setParameter("id", id)
					.setParameter("empId", empId).executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
			LOGGER.info("#INSIDE IN CATCH REMOVEEMPROLE" + e.getMessage());
		}
	}

	public boolean removeuserrole(int empId, long role) {
		LOGGER.info("#REMOVE IN REMOVEEMPROLE");
		boolean flag = false;
		System.out.println("empId" + empId);
		System.out.println("roleId" + role);
		Session session = getCurrentSession();
		try {
			int deleteStatus = session
					.createNativeQuery("delete from emp_roles where emp_id=:empId and role_id=:roleId")
					.setParameter("empId", empId).setParameter("roleId", role).executeUpdate();
			if (deleteStatus > 0) {
				flag = true;
			}
		} catch (Exception e) {
			e.printStackTrace();
			LOGGER.info("#INSIDE IN CATCH REMOVEEMPROLE" + e.getMessage());
		}
		return flag;
	}

	public Role isRole(String rolename) {
		Session session = getCurrentSession();
		List<Role> roleDetails = session.createNativeQuery("select * FROM roles where name=:name").addEntity(Role.class)
				.setParameter("name", rolename).getResultList();

		return roleDetails.get(0);
	}

	@Override
	public List<DepartmentLevelBean> getListofEmployeebyBranchWise(Integer branchId, Integer levelId, Integer deptId,
			Integer roleId) {
		LOGGER.info("# INSIDE IN GETLISTOFEMPLOYEEBYBRANCHWISE ");
		List<DepartmentLevelBean> deptList = new ArrayList<>();
		List<Object[]> list = null;
		try {
			if (branchId != 0 && roleId == EnovationConstants.EVALUATOR) {
				StringBuffer evalQuery = new StringBuffer(
						" SELECT d.id,e.emp_id,e.first_name,e.last_name,d.dept_id,dept.dept_name,d.branch_id, "
								+ " e.cmpy_emp_id, e.email_id, e.contact_no, b.name, e.designation "
								+ " FROM department_level_evaluators d, " + " tbl_employee_details e,"
								+ " master_department dept, " + " master_branch b "
								+ " where d.dept_id=dept.dept_id AND e.is_deactive=0 \r\n ");
				if (deptId != 0) {
					evalQuery.append(" and d.dept_id={deptId} ");
				}

				evalQuery.append(
						" and d.emp_id=e.emp_id " + " and d.branch_id=b.branch_id " + " and d.branch_id=:branchId ");
				list = getCurrentSession()
						.createNativeQuery(
								(evalQuery.toString()).replaceAll(Pattern.quote("{deptId}"), String.valueOf(deptId)))
						.setParameter("branchId", branchId).getResultList();
			} else// All Level Employee Data ; Controller
			if (branchId != 0 && levelId == 0 && deptId == 0) {
				list = getCurrentSession().createNativeQuery(
						"SELECT d.id,e.emp_id,e.first_name,e.last_name,d.dept_id,dept.dept_name,d.branch_id,d.is_esclate,d.level,e.designation,e.email_id,e.contact_no,l.name,d.line_id FROM dept_level d  \r\n"
								+ "inner join tbl_employee_details e on d.emp_id=e.emp_id \r\n"
								+ "inner join master_department dept on d.dept_id=dept.dept_id\r\n"
								+ "left join dwm_line l on d.line_id=l.id where \r\n"
								+ "d.branch_id=:branchId and d.level=:levelId AND  e.is_deactive=0 \r\n")
						.setParameter("branchId", branchId).setParameter("levelId", levelId).getResultList();

				// Level Wise Data except Controller
			} else if (branchId != 0 && deptId == 0 && levelId != 0) {
				list = getCurrentSession().createNativeQuery(
						"SELECT d.id,e.emp_id,e.first_name,e.last_name,d.dept_id,dept.dept_name,d.branch_id,d.is_esclate,d.level,e.designation,e.email_id,e.contact_no,l.name,d.line_id FROM dept_level d "
								+ " inner join tbl_employee_details e on d.emp_id=e.emp_id inner join master_department dept on  d.dept_id=dept.dept_id"
								+ " left join  dwm_line l on d.line_id=l.id "
								+ " where  d.branch_id=:branchId  and  d.level>=:levelId and  e.is_deactive=0 \r\n")
						.setParameter("branchId", branchId).setParameter("levelId", EnovationConstants.ENABLE_STATUS)
						.getResultList();
				// Level Wise , Branch Wise , Dept Wise Data
			} else if (branchId != 0 && deptId != 0) {
				list = getCurrentSession().createNativeQuery(
						"SELECT d.id,e.emp_id,e.first_name,e.last_name,d.dept_id,dept.dept_name,d.branch_id,d.is_esclate,d.level,e.designation,e.email_id,e.contact_no,l.name,d.line_id FROM dept_level d "
								+ " inner join tbl_employee_details e on d.emp_id=e.emp_id inner join master_department dept on d.dept_id=dept.dept_id "
								+ " left join dwm_line l on d.line_id=l.id"
								+ " where   d.dept_id=:deptId    and d.branch_id=:branchId  and  d.level>=:levelId AND  e.is_deactive=0 \r\n ")
						.setParameter("branchId", branchId).setParameter("deptId", deptId)
						.setParameter("levelId", EnovationConstants.ZERO).getResultList();
			}

			for (Object[] obj : list) {
				DepartmentLevelBean bean = new DepartmentLevelBean();
				if (obj[0] != null) {
					bean.setId(Integer.valueOf(String.valueOf(obj[0])));
				}
				if (obj[1] != null) {
					bean.setEmpId(Integer.valueOf(String.valueOf(obj[1])));
				}
				if (obj[2] != null && obj[3] == null) {
					bean.setEmpName(String.valueOf(obj[2]));
				} else if (obj[2] != null && obj[3] != null) {
					bean.setEmpName(String.valueOf(obj[2] + " " + obj[3]));
				}
				// if(obj[2]!=null) {bean.setId(Integer.valueOf(String.valueOf(obj[2])));}
				if (obj[4] != null) {
					bean.setDeptId(Integer.valueOf(String.valueOf(obj[4])));
				}
				if (obj[5] != null) {
					bean.setDeptName(String.valueOf(obj[5]));
				}
				if (obj[6] != null) {
					bean.setBranchId(Integer.valueOf(String.valueOf(obj[6])));
				}
				if (roleId != EnovationConstants.EVALUATOR) {
					if (obj[7] != null) {
						bean.setIsEsclate(Integer.valueOf(String.valueOf(obj[7])));
					}
					if (obj[8] != null) {
						bean.setLevel(Integer.valueOf(String.valueOf(obj[8])));
					}

					if (obj[9] != null) {
						bean.setDesignation(String.valueOf(obj[9]));
					}
					if (obj[10] != null) {
						bean.setEmailId(String.valueOf(obj[10]));
					}
					if (obj[11] != null) {
						bean.setContactNo(String.valueOf(obj[11]));
					}
					if (obj[12] != null) {
						bean.setLineName(String.valueOf(obj[12]));
					}
					if (obj[13] != null) {
						bean.setLineId(Integer.valueOf(String.valueOf(obj[13])));
					}
				}
				if (roleId == EnovationConstants.EVALUATOR) {
					if (obj[7] != null) {
						bean.setCmpyEmpId(String.valueOf(obj[7]));
					}
					if (obj[8] != null) {
						bean.setEmailId(String.valueOf(obj[8]));
					}
					if (obj[9] != null) {
						bean.setContactNo(String.valueOf(obj[9]));
					}
					if (obj[10] != null) {
						bean.setBranchName(String.valueOf(obj[10]));
					}
					if (obj[11] != null) {
						bean.setDesignation(String.valueOf(obj[11]));
					}
				}

				deptList.add(bean);
			}

		} catch (Exception e) {
			LOGGER.error("#INSIDE EXCEPTION OCCURED IN GETLISTOFEMPLOYEEBYBRANCHWISE :" + e.getMessage());
		}
		return deptList;
	}

	@Override
	public boolean updateBranch(BranchMaster brnch) {
		boolean flag = false;
		LOGGER.info("#INSIDE IN UPDATEBRANCH ");
		Session session = getCurrentSession();
		try {
			// String action = "Update Branch";
			BranchMaster brnchInfo = (BranchMaster) session.get(BranchMaster.class, brnch.getBranchId());
			// String previousName = "";
			// String previousLocation = "";
			// String previous = null;
			// String currentName = null;
			// String currentLocation = null;
			// String current = null;
			/*
			 * if (brnch.getName().equals(brnchInfo.getName()) == false) { previousName =
			 * brnchInfo.getName().toString(); currentName = brnch.getName().toString(); }
			 * if (brnch.getLocation().equals(brnchInfo.getLocation()) == false) {
			 * previousLocation = brnchInfo.getLocation().toString(); currentLocation =
			 * brnch.getLocation().toString(); } if (previousName != "" && previousLocation
			 * != "") { previous = previousName + "," + previousLocation; current =
			 * currentName + "," + currentLocation; } else if (currentName != "" &&
			 * previousLocation == "") { previous = previousName; current = currentName; }
			 * else { previous = previousLocation; current = currentLocation; } int
			 * updatedBy = 0; if (brnch.getUpdatedBy() != 0) { updatedBy =
			 * brnch.getUpdatedBy(); brnchInfo.setUpdatedById(new
			 * EmployeeDetails(brnch.getUpdatedBy())); }
			 */

			// brnchInfo.setUpdatedDate(CommonUtils.currentDate());
			// audit.insertBranchMasterAudit(session, action, updatedBy, previous, current);
			if (brnch.getLocation() != null) {
				brnchInfo.setLocation(brnch.getLocation());
			}
			if (brnch.getName() != null) {
				brnchInfo.setName(brnch.getName());
			}
			brnchInfo.setUpdatedDate(CommonUtils.currentDate());
			session.update(brnchInfo);
			flag = true;
		} catch (Exception e) {
			e.printStackTrace();
			LOGGER.error("#INSIDE EXCEPTION OCCURED IN UPDATEBRANCH " + e.getMessage());
		}
		return flag;
	}

	@Override
	public OrganizationMaster uploadOranizationLogo(OrganizationMaster org) {
		LOGGER.info("#INSIDE IN UPLOADORANIZATIONLOGO ");
		Session session = getCurrentSession();
		OrganizationMaster orgInfo = null;
		try {
			orgInfo = (OrganizationMaster) session.get(OrganizationMaster.class, org.getOrgId());
			if (org.getOrgLogo() != null) {
				String docDirctory = EnovationConfig.buddyConfig.get(EnovationConstants.UPLOAD_ORG_LOGO_PATH) + "/"
						+ orgInfo.getAlies() + "/";
				/*
				 * String prevFilePath=docDirctory+org.getLogo(); String
				 * pathLogo=WriteFilesUtils.updateWriteFileOnServer(org.getOrgLogo(),
				 * docDirctory, prevFilePath);
				 */
				String filePathToTrim = orgInfo.getAlies();
				String pathLogo = WriteFilesUtils.writeFileOnServer(org.getOrgLogo(), docDirctory, filePathToTrim);
				System.out.println();
				orgInfo.setLogo(pathLogo);
				orgInfo.setUpdatedDate(CommonUtils.currentDate());
				session.update(orgInfo);
			}

		} catch (Exception e) {
			e.printStackTrace();
			LOGGER.error("#INSIDE EXCEPTION OCCURED IN UPLOADORANIZATIONLOGO " + e.getMessage());
		}
		return orgInfo;
	}

	@Override
	public boolean updateCategory(CategoryMaster cat) {
		LOGGER.info("#INSIDE IN UPDATECATEGORY ");
		boolean flag = false;
		Session session = getCurrentSession();
		try {
			String action = "Update Category";
			CategoryMaster catInfo = (CategoryMaster) getCurrentSession().get(CategoryMaster.class, cat.getCatId());
			if (cat.getOrgId() != 0) {
				catInfo.setOrganisation(new OrganizationMaster(cat.getOrgId()));
			}
			if (cat.getBranchId() != 0) {
				catInfo.setBranch(new BranchMaster(cat.getBranchId()));
			}
			cat.setUpdatedDate(CommonUtils.currentDate());
			audit.insertCategoryMasterAudit(session, action, 0/* cat.getUpdatedBy() */, catInfo.getCategoryName(),
					cat.getCategoryName());
			session.update(cat);
			flag = true;
		} catch (Exception e) {
			LOGGER.error("#INSIDE EXCEPTION OCCURED IN UPDATECATEGORY " + e.getMessage());
		}
		return flag;
	}

	@Override
	public boolean saveStatusPoints(OrgStatusPoints orgsts) {
		LOGGER.info("#INSIDE IN SAVESTATUSPOINTS ");
		boolean flag = false;
		Session session = getCurrentSession();
		try {
			if (orgsts.getOrgId() != 0) {
				orgsts.setOrg(new OrganizationMaster(orgsts.getOrgId()));
			}
			if (orgsts.getStatusId() != 0) {
				orgsts.setStatus(new StatusMaster(orgsts.getStatusId()));
			}
			if (orgsts.getBranchId() != 0) {
				orgsts.setBranch(new BranchMaster(orgsts.getBranchId()));
			}
			if (orgsts.getRoleId() != 0) {
				orgsts.setRoleId(orgsts.getRoleId());
			}
			orgsts.setCreatedDate(CommonUtils.currentDate());
			session.save(orgsts);
			flag = true;
		} catch (Exception e) {
			LOGGER.error("#INSIDE EXCEPTION OCCURED IN SAVESTATUSPOINTS " + e.getMessage());
		}
		return flag;
	}

	@Override
	public boolean updateStatusPoints(OrgStatusPoints orgsts) {
		LOGGER.info("#INSIDE IN UPDATESTATUSPOINTS ");
		boolean flag = false;
		Session session = getCurrentSession();
		try {
			OrgStatusPoints points = (OrgStatusPoints) session.get(OrgStatusPoints.class, orgsts.getId());
			if (orgsts.getPoints() != 0) {
				points.setPoints(orgsts.getPoints());
			}
			if (orgsts.getOrgId() != 0) {
				points.setOrg(new OrganizationMaster(orgsts.getOrgId()));
			}
			if (orgsts.getBranchId() != 0) {
				points.setBranch(new BranchMaster(orgsts.getBranchId()));
			}
			if (orgsts.getStatusId() != 0) {
				points.setStatus(new StatusMaster(orgsts.getStatusId()));
			}

			if (orgsts.getRoleId() != 0) {
				orgsts.setRoleId(orgsts.getRoleId());
			}
			points.setUpdatedDate(CommonUtils.currentDate());
			session.update(points);
			flag = true;
		} catch (Exception e) {
			LOGGER.error("#INSIDE EXCEPTION OCCURED IN UPDATESTATUSPOINTS " + e.getMessage());
		}
		return flag;
	}

	@Override
	public List<OrgStatusPoints> getListofStatusPoints(Integer branchId) {
		LOGGER.info("#INSIDE IN GETLISTOFSTATUSPOINTS ");
		List<OrgStatusPoints> list = null;
		Session session = getCurrentSession();
		try {
			list = session.createQuery("FROM OrgStatusPoints WHERE branch.branchId=:branchId")
					.setParameter("branchId", branchId).getResultList();
		} catch (Exception e) {
			LOGGER.error("#INSIDE EXCEPTION OCCURED IN GETLISTOFSTATUSPOINTS " + e.getMessage());
		}
		return list;
	}

	@Override
	public List<StatusMaster> getListofStatusForBrowniePoints(Integer roleId) {
		LOGGER.info("#INSIDE IN GETLISTOFSTATUSFORBROWNIEPOINTS ");
		List<StatusMaster> list = null;
		Session session = getCurrentSession();
		try {
			if (roleId > 0) {
				StringBuffer stringBuf = new StringBuffer("FROM StatusMaster WHERE");

				if (EnovationConstants.EMPLOYEE_ROLEID == roleId) {
					stringBuf.append(" pointsInflight ={id} ");
				} else {
					stringBuf.append(" roleId={roleId} OR statusId = {statusId}");
				}
				list = session.createQuery(
						(stringBuf.toString()).replaceAll(Pattern.quote("{id}"), String.valueOf(EnovationConstants.ONE))
								.replaceAll(Pattern.quote("{statusId}"),
										String.valueOf(EnovationConstants.IMPLINITIALINPUTSIMPLEMENTEDSTATUS))
								.replaceAll(Pattern.quote("{roleId}"), String.valueOf(roleId)))
						.getResultList();
			}
		} catch (Exception e) {
			LOGGER.error("#INSIDE EXCEPTION OCCURED IN GETLISTOFSTATUSFORBROWNIEPOINTS " + e.getMessage());
		}
		return list;
	}

	@Override
	public List<StatusMaster> getListofStatus() {

		LOGGER.info("#INSIDE IN GETLISTOFSTATUS ");
		List<StatusMaster> list = null;
		Session session = getCurrentSession();
		try {
			list = session.createQuery("FROM StatusMaster").getResultList();
		} catch (Exception e) {
			LOGGER.error("#INSIDE EXCEPTION OCCURED IN GETLISTOFSTATUS " + e.getMessage());
		}
		return list;
	}

	@Override
	public List<RolesBean> getRoleList() {
		LOGGER.info("#INSIDE IN GETROLELIST ");
		List<RolesBean> list = new ArrayList<>();
		Session session = getCurrentSession();
		try {
			List<Object[]> ObjectList = session.createNativeQuery("Select id,name from roles").getResultList();
			for (Object[] obj : ObjectList) {
				RolesBean role = new RolesBean();
				if (obj[0] != null) {
					role.setId(Integer.valueOf(String.valueOf(obj[0])));
				}
				if (obj[1] != null) {
					role.setRoleName(String.valueOf(obj[1]));
				}
				list.add(role);
			}

		} catch (Exception e) {
			LOGGER.error("#INSIDE EXCEPTION OCCURED IN GETROLELIST " + e.getMessage());
		}
		return list;
	}

	@Override
	public List<Countries> getCountriesList() {
		return getCurrentSession().createQuery("from Countries").getResultList();
	}

	@Override
	public List<CategoryMaster> getMasterCatagoryList() {

		List<CategoryMaster> categoryList = new ArrayList<CategoryMaster>();
		try {
			LOGGER.info("#INSIDE IN GETCATEGORYLISTBYORGID ");
			categoryList = getCurrentSession()
					.createNativeQuery(
							"SELECT c.* from master_category c WHERE c.org_id is null and c.branch_id is null")
					.addEntity(CategoryMaster.class).getResultList();
		} catch (Exception e) {
			LOGGER.info("#INSIDE Exception getMasterCatagoryList " + e.getMessage());
		}
		return categoryList;
	}

	@Override
	public List<String> getBenefitAnalysisMasterList() {

		List<String> benifitList = new ArrayList<String>();
		List<Object[]> list = null;
		try {
			LOGGER.info("#INSIDE IN getBenefitAnalysisMasterList ");
			list = getCurrentSession().createNativeQuery(
					"SELECT b.benefits , b.branch_id from master_benefit_analysis b where b.branch_id is null and b.org_id is null and b.is_enable = 1")
					.getResultList();
			for (Object[] obj : list) {
				benifitList.add(obj[0].toString());
				System.out.println(obj[0].toString());
			}

		} catch (Exception e) {
			LOGGER.info("#INSIDE Exception getBenefitAnalysisMasterList " + e.getMessage());
		}

		return benifitList;
	}

	@SuppressWarnings("rawtypes")
	@Override
	public int deleteBenifits(BenefitAnalysisMaster benifits) {
		int value = 0;
		LOGGER.info("# INSIDE IN DELETEBENIFITS ");
		Session session = getCurrentSession();
		try {
			Query query = session.createQuery("delete BenefitAnalysisMaster where benfitId = :ID");
			query.setParameter("ID", new Integer(benifits.getBenfitId()));
			int result = query.executeUpdate();
			if (result > 0)
				value = 1;
		} catch (TransactionException e) {
			value = 2;
			LOGGER.info("# INSIDE DELETEBENIFITS EXCEPTION OCCURED " + e.getMessage());
		}
		return value;
	}

	@Override
	public boolean disableExecutive(Executive cate) {
		boolean flag = false;
		LOGGER.info("UPDATE ENABLE DISABLE");
		Session session = getCurrentSession();
		try {
			session.createQuery("UPDATE Executive SET isDeactive=:is_deactive WHERE id=:id ")
					.setParameter("id", cate.getId()).setParameter("is_deactive", cate.getIsDeactive()).executeUpdate();
			flag = true;
		} catch (Exception e) {
			LOGGER.info("DISABLEEXECUTIVE :ERROR INSIDE UPDATE" + e.getMessage());
		}
		return flag;
	}

	@SuppressWarnings("rawtypes")
	@Override
	public int removeBrowinePoints(OrgStatusPoints orgstatuspoints) {
		int value = 0;
		LOGGER.info("# INSIDE IN REMOVEBROWINEPOINTS ");
		Session session = getCurrentSession();
		try {
			Query query = session.createQuery("DELETE OrgStatusPoints WHERE id = :ID");
			query.setParameter("ID", new Integer(orgstatuspoints.getId()));
			int result = query.executeUpdate();
			if (result > 0)
				value = 1;
		} catch (TransactionException e) {
			value = 2;
			LOGGER.info("# EXCEPTION OCCURED IN REMOVEBROWINEPOINTS " + e.getMessage());
		}
		return value;
	}

	@Override
	public List<DepartmentMaster> getdepartmentlistbybranchidandDeptname(int branchId, Set<String> deptName) {
		LOGGER.info("# INSIDE in GETDEPARTMENTLISTBYBRANCHIDANDDEPTNAME ");
		return getCurrentSession()
				.createNativeQuery(
						"SELECT * from master_department d WHERE d.branch_id=:branchId and dept_name IN(:deptName)")
				.addEntity(DepartmentMaster.class).setParameter("branchId", branchId).setParameter("deptName", deptName)
				.getResultList();

	}

	@Override
	public boolean updateIsEscalation(ProductOrgConfig request) {
		boolean flag = false;
		LOGGER.info("# INSIDE IN UPDATEISESCALATION ");
		Session session = getCurrentSession();
		try {
			ProductOrgConfig poc = (ProductOrgConfig) session.get(ProductOrgConfig.class, request.getId());
			poc.setIsEscalation(request.getIsEscalation());
			session.update(poc);
			flag = true;
		} catch (TransactionException e) {
			LOGGER.info("# EXCEPTION OCCURED IN UPDATEISESCALATION " + e.getMessage());
		}
		return flag;
	}

	@Override
	public boolean deleteExecutive(Executive executive) {
		LOGGER.info("#INSIDE IN removeExecutive ");
		boolean flag = false;
		Session session = getCurrentSession();
		try {
			int previous = executive.getEmpId();
			String action = "Remove Executive";
			Role role = isRole((RoleName.EXECUTIVE).toString());
			boolean status = inflightRoleCheck(executive.getEmpId(), role.getId(), 0);
			if (status) {
				return flag;
			} else {
				audit.insertEmpRolesAudit(session, action, executive.getUpdatedBy(), previous, 0, 0);
				int result = session.createNativeQuery("delete from executive where id=:execId")
						.setParameter("execId", executive.getId()).executeUpdate();
				if (result > 0) {
					removeuserrole(executive.getEmpId(), role.getId());
					if (executive.getIsSetupCompleted() > 0) {
						employeeDaoImple.sendMailTo_ROLE_CHANGED_Employees(executive.getEmailId(),
								executive.getFirstName(), executive.getLastName(), (RoleName.EXECUTIVE).toString(),
								EnovationConstants.ROLE_REMOVED);
					}
					flag = true;
				}
			}
		} catch (Exception e) {
			LOGGER.info("#INSIDE EXCPTION OCCURED IN REMOVE_EXECUTIVE " + e.getMessage());

		}
		return flag;

		/*
		 * boolean value = false; LOGGER.info("# INSIDE IN DELETEEXECUTIVE "); Session
		 * session=getCurrentSession(); try { Query query =
		 * session.createQuery("delete Executive where id = :ID");
		 * query.setParameter("ID", executive.getId()); int result =
		 * query.executeUpdate(); if(result > 0) value =true; if(value) { Role
		 * role=isRole((RoleName.EXECUTIVE).toString());
		 * removeuserrole(executive.getEmpId(),role.getId()); }
		 * }catch(TransactionException e){
		 * LOGGER.info("# INSIDE DELETEEXECUTIVE EXCEPTION OCCURED "+e.getMessage()); }
		 * return value;
		 */
	}

	// @Scheduled(fixedRate=20000)
	public boolean inflightRoleCheck(int empId, long roleId, long deptId) {
		boolean value = false;
		LOGGER.info("#INSIDE IN inflightRoleCheck ");
		List<Object[]> list = null;
		List<Integer> status = null;
		try {
			/*
			 * CHECK INFLIGHT ROLE FOR EXECUTIVE, EVALUATOR, IMPLEMENTER, CONTROLLER
			 */
			if (roleId == EnovationConstants.EXECUTIVE) {
				status = entityManager
						.createQuery(" SELECT statusId FROM StatusMaster WHERE executiveInflight=:enable ")
						.setParameter("enable", EnovationConstants.ONE).getResultList();

				System.out.println("size :" + status.size());
				for (Integer in : status) {
					System.out.println(in);
				}
			} else if (roleId == EnovationConstants.EVALUATOR || roleId == EnovationConstants.IMPLEMENTER) {
				if (roleId == EnovationConstants.EVALUATOR) {
					status = entityManager
							.createQuery(" SELECT statusId FROM StatusMaster WHERE evaluatorInflight=:enable ")
							.setParameter("enable", EnovationConstants.ONE).getResultList();
				} else {
					status = entityManager
							.createQuery(" SELECT statusId FROM StatusMaster WHERE implementorInflight=:enable ")
							.setParameter("enable", EnovationConstants.ONE).getResultList();
				}

			} else if (roleId == EnovationConstants.CONTROLLER) {

				status = entityManager
						.createQuery(" SELECT statusId FROM StatusMaster WHERE controllerInflight=:enable ")
						.setParameter("enable", EnovationConstants.ONE).getResultList();
				System.out.println("status list : " + status.toString());
			} else if (roleId == EnovationConstants.EMPLOYEE_ROLEID) {
				status = entityManager.createQuery(" SELECT statusId FROM StatusMaster WHERE statusId=:enable ")
						.setParameter("enable", EnovationConstants.ASSIGNED_FOR_EDIT_STATUS).getResultList();
			}
			if (roleId == EnovationConstants.DEACTIVE_EMPLOYEE) {
				List<Object[]> checkEmpAlignedList = entityManager
						.createNativeQuery(
								"select * from emp_roles where emp_id=:empId and role_id  NOT IN (:empRoleId)")
						.setParameter("empId", empId).setParameter("empRoleId", EnovationConstants.EMPLOYEE_ROLEID)
						.getResultList();
				if (checkEmpAlignedList != null && checkEmpAlignedList.size() > 0) {
					value = true;
					System.out.println("inflightRoleCheck : return true :(EMPLOYEE) IN PENDING WITH ANOTHER ROLE");
				} else {
					System.out.println("inflightRoleCheck : return true :(EMPLOYEE) NOT PENDING WITH ANOTHER ROLE");
				}

			}
			if (roleId == EnovationConstants.AUDITOR) {
				List<Object[]> checkEmpAlignedList = entityManager
						.createNativeQuery(
								"SELECT * FROM audit_inspection_details WHERE inspected_by=:empId AND sts_id=:stsId")
						.setParameter("empId", empId).setParameter("stsId", EnovationConstants.AUDIT_IN_PROGRESS)
						.getResultList();
				if (checkEmpAlignedList != null && checkEmpAlignedList.size() > 0) {
					value = true;
					System.out.println("inflightRoleCheck : return true :(EMPLOYEE) IN PENDING WITH ANOTHER ROLE");
				} else {
					System.out.println("inflightRoleCheck : return true :(EMPLOYEE) NOT PENDING WITH ANOTHER ROLE");
				}
			} else {
				if ((roleId == EnovationConstants.CONTROLLER || roleId == EnovationConstants.EVALUATOR)
						&& deptId != 0) {
					System.out.println("under action :" + empId);
					System.out.println("deptId is:" + deptId);
					list = entityManager.createNativeQuery(
							"SELECT * FROM  tbl_sugession_details sd  WHERE sd.under_action=:empId and sd.sts_id in(:statusList) and sd.dept_id=:deptId ")
							.setParameter("empId", empId).setParameter("statusList", status)
							.setParameter("deptId", deptId).getResultList();
				} else {
					list = entityManager.createNativeQuery(
							"SELECT * FROM  tbl_sugession_details sd  WHERE sd.under_action=:empId and sts_id in(:statusList) ")
							.setParameter("empId", empId).setParameter("statusList", status).getResultList();
				}

				System.out.println("list size :" + list.size());
				if (list != null && list.size() > 0) {
					value = true;
					System.out.println("inflightRoleCheck : return true :IN PENDING");
				} else {
					System.out.println("inflightRoleCheck : return false :NOT IN PENDING");
				}
			}

		} catch (Exception e) {
			LOGGER.info("#INSIDE ADDROLE EXCEPTION OCCURED " + e.getMessage());
		} finally {
			entityManager.close();
		}
		return value;
	}
	
	private int removeAuditor(int empId, long roleId) {
		return getCurrentSession().createNativeQuery("delete from auditor where emp_id=:empId ")
				.setParameter("empId", empId).executeUpdate();
	}

	@SuppressWarnings("null")
	@Override
	public int updateLevelWiseUsers(EmployeeDetailsBean request) {
		LOGGER.info("#INSIDE IN UPDATELEVELWISEUSERS ");
		int flag = 0;
		Session session = getCurrentSession();
		int deleteStatus = 0;
	
		if (request.getRemoveController().getRoleId() == EnovationConstants.CONTROLLER) {
			System.out.println("dept remove status :" + deleteStatus);
			List<DepartmentLevel> list = session
					.createQuery("from DepartmentLevel where empDetails.empId = :empId")
					.setParameter("empId", request.getRemoveController().getEmpId()).getResultList();
			if (list.size() == EnovationConstants.ONE) {
				deleteStatus = session.createNativeQuery(
						"delete from dept_level where emp_id=:empId and level = :level and dept_id=:deptId")
						.setParameter("empId", request.getRemoveController().getEmpId())
						.setParameter("level", request.getRemoveController().getLevel())
						.setParameter("deptId", request.getRemoveController().getDeptId()).executeUpdate();
				if (deleteStatus > 0) {
					session.createNativeQuery("delete from emp_roles where emp_id=:empId and role_id=:roleId")
							.setParameter("empId", request.getRemoveController().getEmpId())
							.setParameter("roleId", request.getRemoveController().getRoleId()).executeUpdate();
				}
				audit.insertEmpRolesAudit(session, "Remove Controller", request.getCreatedBy(),
						request.getRemoveController().getEmpId(), 0, request.getRemoveController().getId());
			} else {
				deleteStatus = session.createNativeQuery(
						"delete from dept_level where emp_id=:empId and level = :level and dept_id=:deptId")
						.setParameter("empId", request.getRemoveController().getEmpId())
						.setParameter("level", request.getRemoveController().getLevel())
						.setParameter("deptId", request.getRemoveController().getDeptId()).executeUpdate();
			}
			flag = 1;
		} else if (request.getRemoveController().getRoleId() == EnovationConstants.EVALUATOR) {
			System.out.println("dept remove status :" + deleteStatus);
			List<DepartmentLevelEvaluators> list = session
					.createQuery("from DepartmentLevelEvaluators where empDetails.empId = :empId")
					.setParameter("empId", request.getRemoveController().getEmpId()).getResultList();
			if (list.size() == EnovationConstants.ONE) {
				deleteStatus = session.createNativeQuery(
						"delete from department_level_evaluators where emp_id=:empId and dept_id=:deptId")
						.setParameter("empId", request.getRemoveController().getEmpId())
						.setParameter("deptId", request.getRemoveController().getDeptId()).executeUpdate();
				if (deleteStatus > 0) {
					session.createNativeQuery("delete from emp_roles where emp_id=:empId and role_id=:roleId")
							.setParameter("empId", request.getRemoveController().getEmpId())
							.setParameter("roleId", request.getRemoveController().getRoleId()).executeUpdate();
				}
				audit.insertEmpRolesAudit(session, "Remove Evaluator", request.getCreatedBy(),
						request.getRemoveController().getEmpId(), 0, request.getRemoveController().getId());
			} else {
				deleteStatus = session.createNativeQuery(
						"delete from department_level_evaluators where emp_id=:empId and dept_id=:deptId")
						.setParameter("empId", request.getRemoveController().getEmpId())
						.setParameter("deptId", request.getRemoveController().getDeptId()).executeUpdate();
			}
			flag = 1;
		}

		return flag;
	}

	@Override
	public List<RoleEscalationSetup> checkRoleEscalationSetup(List<RoleEscalationSetup> request) {
		List<RoleEscalationSetup> list = null;
		List<RoleEscalationSetup> errorlist = new ArrayList<>();
		Session session = getCurrentSession();
		try {
			if (request != null && request.size() > 0) {
				for (RoleEscalationSetup x : request) {

					list = session.createNativeQuery(
							"select * from role_escalation_setup where branch_id=:branchId and role_id=:roleId and emp_id =:empId")
							.setParameter("branchId", x.getBranchMaster().getBranchId())
							.setParameter("roleId", x.getRole().getId()).setParameter("empId", x.getEmpId())
							.addEntity(RoleEscalationSetup.class).getResultList();

					if (list != null && list.size() > 0) {
						errorlist.addAll(list);
					}
				}
				// updateRoleEscalation(request);
			}

			/*
			 * list = getCurrentSession() .createNativeQuery(
			 * "select * from role_escalation_setup where branch_id=:branchId and role_id=:roleId"
			 * ) .setParameter("branchId", request.getBranchMaster().getBranchId())
			 * .setParameter("roleId", request.getRole().getId()).getResultList(); if (list
			 * != null && list.size() > 0) { flag = true; }
			 */
		} catch (Exception e) {
			LOGGER.error("#INSIDE EXCEPTION OCCURED IN CHECKROLEESCALATIONSETUP " + e.getMessage());
			e.printStackTrace();
		}
		return errorlist;
	}

	@Override
	@Transactional
	public boolean saveRoleEscalationSetup(MasterResponse request) {

		boolean flag = false;
		Session session = getCurrentSession();

		try {
			if (request.getRoleEscalationList() != null && request.getRoleEscalationList().size() > 0) {
				request.getRoleEscalationList().stream().forEach(x -> {
					session.save(x);

				});

				System.out.println("Role Escalator Inserted Successfully");
				System.out.println("Printing Number of Days : " + request.getNumberOfDays());
				if (request.getNumberOfDays() > 0) {
					// updateRoleEscalation(request);
					updateRoleEscalationSetup(new RoleEscalationSetup(new BranchMaster(request.getBranchId()),
							new Role((long) request.getRoleId()), request.getNumberOfDays()));
				}

			} else {
				if (request.getNumberOfDays() > 0) {
					updateRoleEscalationSetup(new RoleEscalationSetup(new BranchMaster(request.getBranchId()),
							new Role((long) request.getRoleId()), request.getNumberOfDays()));
				}
			}

			flag = true;
		} catch (Exception e) {
			LOGGER.error("#INSIDE EXCEPTION OCCURED IN SAVEROLEESCALATIONSETUP " + e.getMessage());
			e.printStackTrace();
		}
		return flag;
	}

	@Override
	@Transactional
	public boolean deleteEscalator(RoleEscalationSetup request) {
		boolean flag = false;
		Session session = getCurrentSession();
		try {
			RoleEscalationSetup removeRole = (RoleEscalationSetup) session.get(RoleEscalationSetup.class,
					request.getId());
			session.remove(removeRole);
			flag = true;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return flag;
	}

	@Override
	public boolean removeRoleEscalationSetup(RoleEscalationSetup request) {
		boolean flag = false;
		// Session session = getCurrentSession();
		// try {
		// session.createNativeQuery("UPDATE role_escalation_setup SET
		// number_of_days=:numberOfDays WHERE role_id =:roleId AND branch_id
		// =:branchId")
		// .setParameter("numberOfDays", request.getNumberOfDays())
		// .setParameter("roleId", request.getRole().getId())
		// .setParameter("branchId", request.getBranchMaster().getBranchId())
		// .executeUpdate();
		//
		// flag = true;
		// }catch(Exception e) {
		// e.printStackTrace();
		// }
		return flag;
	}

	@Override
	public List<RoleEscalationSetup> getRoleEscalationSetup(Integer branchId) {
		List<RoleEscalationSetup> resList = new ArrayList<>();
		try {
			resList = getCurrentSession().createQuery("from RoleEscalationSetup where branch_id=:id")
					.setParameter("id", branchId).getResultList();

		} catch (Exception e) {
			LOGGER.error("#INSIDE EXCEPTION OCCURED IN GETROLEESCALATIONSETUP " + e.getMessage());
			e.printStackTrace();
		}
		return resList;
	}

	@Override
	public boolean sendEmailsOnRegistration(Integer branchId) {
		boolean flag = false;
		EmailTemplateMaster messageContent = null;
		List<EmployeeDetails> empList = entityManager
				.createQuery("from EmployeeDetails where branch.branchId=:branchId and isDeactive=0 ")
				.setParameter("branchId", branchId).getResultList();
		messageContent = enoConfig.getMessageContent("sampleRegistration");
		String mailContent = "", subject = "";
		;
		for (EmployeeDetails empDet : empList) {
			if (empDet.getCreatedBy() != null) {
				EmployeeDetails emp = (EmployeeDetails) entityManager.find(EmployeeDetails.class,
						empDet.getCreatedBy());
				if (messageContent.getBody() != null && messageContent.getBody().contains("{name}")) {

					mailContent = messageContent.getBody().replaceAll(Pattern.quote("{name}"), empDet.getFirstName())
							.replaceAll(Pattern.quote("{password}"), "123")
							.replaceAll(Pattern.quote("{email}"), empDet.getEmailId())
							.replaceAll(Pattern.quote("{appLink}"), "https://goo.gl/wzexQ9")
							.replaceAll(Pattern.quote("{portalLink}"), empDet.getOrganization().getPortalLink())
							.replaceAll(Pattern.quote("{superAdminName}"), emp.getFirstName());
				}
				subject = messageContent.getSubject();
				System.out.println("portal link:" + empDet.getOrganization().getPortalLink());
				if (empDet.getEmailId() != null) {
					taskExecutor.execute(new MailUtil(empDet.getEmailId(), subject, mailContent, communication));
					flag = true;
				}
			}
		}
		return flag;

	}

	@Override
	public String getOrgLogo(String alies) {
		String logo = null;

		List<OrganizationMaster> orglogo = entityManager.createQuery(" from OrganizationMaster where alies = :alies ")
				.setParameter("alies", alies).getResultList();
		if (orglogo != null && orglogo.size() > 0) {
			logo = orglogo.get(0).getLogo();
		}

		return logo;
	}

	@Override
	public List<SuggestionEscalationConfig> getSuggestionEscalationConfig(Integer branchId) {
		List<SuggestionEscalationConfig> resList = new ArrayList<>();
		try {

			resList = getCurrentSession().createQuery("from SuggestionEscalationConfig where branch_id=:id")
					.setParameter("id", branchId).getResultList();

		} catch (Exception e) {
			LOGGER.error("#INSIDE EXCEPTION OCCURED IN GET SUGGESTIONESCALATIONCONFIG " + e.getMessage());
			e.printStackTrace();
		}
		return resList;
	}

	@Override
	public boolean updateSuggestionEscalationConfig(SuggestionEscalationConfig request) {
		boolean flag = false;
		Session session = getCurrentSession();
		LOGGER.info("#INSIDE IN updateSuggestionEscalationConfig  ");
		try {
			session.update(request);
			flag = true;

		} catch (Exception e) {
			LOGGER.error("#INSIDE EXCEPTION OCCURED IN UPDATESUGGESTIONESCALATIONCONFIG " + e.getMessage());
			e.printStackTrace();
		}
		return flag;
	}

	@Override
	public boolean updateRoleEscalationSetup(RoleEscalationSetup request) {
		boolean flag = false;
		Session session = getCurrentSession();
		try {
			session.createNativeQuery(
					"UPDATE role_escalation_setup SET number_of_days=:numberOfDays WHERE role_id =:roleId AND branch_id =:branchId")
					.setParameter("numberOfDays", request.getNumberOfDays())
					.setParameter("roleId", request.getRole().getId())
					.setParameter("branchId", request.getBranchMaster().getBranchId()).executeUpdate();
			flag = true;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return flag;
	}

	@Override
	public boolean removeSuggestionEscalationConfig(SuggestionEscalationConfig request) {
		boolean flag = false;
		Session session = getCurrentSession();
		try {
			SuggestionEscalationConfig req = session.get(SuggestionEscalationConfig.class, request.getId());
			if (req != null) {
				session.remove(req);
				System.out.println("Suggestion Escation Removed Successfull");
				flag = true;
			}
		} catch (Exception e) {
			LOGGER.error("#INSIDE EXCEPTION OCCURED IN REMOVESUGGESTIONESCALATIONCONFIG " + e.getMessage());
			e.printStackTrace();
		}
		return flag;
	}

	@Override
	public List<BrowniePointsReimbursementCycleType> getBrowniePointsReimbursementCycleTypeList() {
		List<BrowniePointsReimbursementCycleType> resList = new ArrayList<>();
		try {

			resList = getCurrentSession().createQuery("from BrowniePointsReimbursementCycleType ").getResultList();

		} catch (Exception e) {
			LOGGER.error("#INSIDE EXCEPTION OCCURED IN GET BROWNIEPOINTSREIMBURSEMENTCYCLETYPE " + e.getMessage());
			e.printStackTrace();
		}
		return resList;
	}

	@Override
	public BrowniePointsReimbursementCycle getBrowniePointsReimbursementCycle(Integer branchId) {
		List<BrowniePointsReimbursementCycle> resList = new ArrayList<>();
		BrowniePointsReimbursementCycle resDetails = null;
		try {

			resList = getCurrentSession()
					.createQuery("from BrowniePointsReimbursementCycle where branch.branchId = :branchId ")
					.setParameter("branchId", branchId).getResultList();
			if (resList != null && resList.size() > 0) {
				resDetails = resList.get(0);
			}

		} catch (Exception e) {
			LOGGER.error("#INSIDE EXCEPTION OCCURED IN GET GETBROWNIEPOINTSREIMBURSEMENTCYCLE " + e.getMessage());
			e.printStackTrace();
		}
		return resDetails;
	}

	@Override
	public boolean updateBrowniePointsReimbursementCycle(BrowniePointsReimbursementCycle request) {
		LOGGER.info("#INSIDE  IN UPDATEBROWNIEPOINTSREIMBURSEMENTCYCLE ");
		boolean flag = false;
		Session session = getCurrentSession();
		try {
			BrowniePointsReimbursementCycle cycle = (BrowniePointsReimbursementCycle) session
					.get(BrowniePointsReimbursementCycle.class, request.getId());
			if (request.getTypeId() != 0) {
				cycle.setType(new BrowniePointsReimbursementCycleType(request.getTypeId()));
			}
			session.update(cycle);
			flag = true;
		} catch (Exception e) {
			LOGGER.error("#INSIDE EXCEPTION OCCURED IN UPDATEBROWNIEPOINTSREIMBURSEMENTCYCLE " + e.getMessage());
			e.printStackTrace();
		}
		return flag;
	}

	@Override
	public List<SuggestionEscalationConfig> checkBranchInSuggestionEscalationConfig(int branchId) {
		List<SuggestionEscalationConfig> list = null;
		LOGGER.info("#INSIDE  IN CHECKBRANCHINSUGGESTIONESCALATIONCONFIG ");
		try {
			list = getCurrentSession()
					.createNativeQuery("select * from suggestion_escalation_config where branch_id = :branchId")
					.addEntity(SuggestionEscalationConfig.class).setParameter("branchId", branchId).getResultList();
		} catch (Exception e) {
			LOGGER.error("#INSIDE EXCEPTION OCCURED IN checkBranchInSuggestionEscalationConfig " + e.getMessage());
		}
		return list;
	}

	@Override
	@Transactional
	public boolean createSuggestionEscalationConfig(MasterResponse request) {
		boolean flag = false;
		Session session = getCurrentSession();
		LOGGER.info("#INSIDE IN CREATESUGGESTIONESCALATIONCONFIG  ");
		System.out.println(request.getNumberOfSuggestion());

		try {

			if (request.getSuggestionEscalationConfigList() != null
					&& request.getSuggestionEscalationConfigList().size() > 0) {

				request.getSuggestionEscalationConfigList().stream().forEach(x -> {
					session.save(x);

				});

			}
			System.out.println("Role Suggestion Escalation Inserted Successfully");
			System.out.println("Printing Nuber of Suggestion : " + request.getNumberOfSuggestion());
			if (request.getNumberOfSuggestion() > 0) {
				System.out.println("\nCalling Update Sugg Escalation Config : ");
				updateSuggEscalationConfig(new SuggestionEscalationConfig(new BranchMaster(request.getBranchId()),
						request.getNumberOfSuggestion()));

			} else {
				if (request.getNumberOfSuggestion() > 0) {
					updateSuggEscalationConfig(new SuggestionEscalationConfig(new BranchMaster(request.getBranchId()),
							request.getNumberOfSuggestion()));
				}
			}
			flag = true;
		} catch (Exception e) {
			LOGGER.error("#INSIDE EXCEPTION OCCURED IN CREATESUGGESTIONESCALATIONCONFIG " + e.getMessage());
			e.printStackTrace();
		}
		return flag;
	}

	@Override
	@Transactional
	public boolean updateSuggEscalationConfig(SuggestionEscalationConfig request) {
		boolean flag = false;
		Session session = getCurrentSession();
		try {
			System.out.println("Inside Number of Suggestion Method : ");
			session.createNativeQuery(
					"UPDATE suggestion_escalation_config SET number_of_suggestions=:numberOfSuggestions WHERE branch_id =:branchId")
					.setParameter("numberOfSuggestions", request.getNumberOfSuggestions())
					.setParameter("branchId", request.getBranchMaster().getBranchId()).executeUpdate();
			flag = true;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return flag;
	}

	@Override
	public List<Particulars> getParticulars(Integer catId) {
		LOGGER.info("#INSIDE  IN getParticulars ");
		List<Particulars> list = null;
		Session session = getCurrentSession();
		try {
			list = session.createNativeQuery("select * from particulars where cat_id = :catId")
					.addEntity(Particulars.class).setParameter("catId", catId).getResultList();
		} catch (Exception e) {
			LOGGER.error("#INSIDE EXCEPTION OCCURED IN getParticulars " + e.getMessage());
			e.printStackTrace();
		}
		return list;
	}

	@Override
	public List<Particulars> checkCategoryInParticulars(int catId, String particular) {
		LOGGER.info("#INSIDE  IN CHECKCATEGORYINPARTICULARS ");
		List<Particulars> list = null;
		Session session = getCurrentSession();
		try {
			list = session
					.createNativeQuery("select * from particulars where cat_id = :catId and particulars = :particular")
					.addEntity(Particulars.class).setParameter("catId", catId).setParameter("particular", particular)
					.getResultList();

		} catch (Exception e) {
			LOGGER.error("#INSIDE EXCEPTION OCCURED IN CHECKCATEGORYINPARTICULARS " + e.getMessage());
			e.printStackTrace();
		}
		return list;
	}

	@Override
	public boolean updateParticular(List<Particulars> request) {
		boolean flag = false;
		Session session = getCurrentSession();
		LOGGER.info("#INSIDE IN updateParticular  ");
		try {
			for (Particulars req : request) {
				session.update(req);
			}
			flag = true;

		} catch (Exception e) {
			LOGGER.error("#INSIDE EXCEPTION OCCURED IN UPDATEPARTICULAR " + e.getMessage());
			e.printStackTrace();
		}
		return flag;
	}

	@Override
	public boolean createParticular(List<Particulars> request) {
		boolean flag = false;
		Session session = getCurrentSession();
		LOGGER.info("#INSIDE IN CREATEPARTICULAR  ");
		try {
			for (Particulars req : request) {
				session.save(req);
			}
			flag = true;

		} catch (Exception e) {
			LOGGER.error("#INSIDE EXCEPTION OCCURED IN CREATEPARTICULAR " + e.getMessage());
			e.printStackTrace();
		}
		return flag;
	}

	@Override
	public List<String> getParticularsList(int catId) {
		List<String> particularsList = new ArrayList<String>();
		List<Object[]> list = null;
		try {
			LOGGER.info("#INSIDE IN GETPARTICULARSLIST ");
			list = getCurrentSession()
					.createNativeQuery("SELECT p.particulars ,p.id from particulars p WHERE p.cat_id  =:catId ")
					.setParameter("catId", catId).getResultList();
			for (Object[] obj : list) {
				particularsList.add(obj[0].toString());
				System.out.println(obj[0].toString());
			}

		} catch (Exception e) {
			LOGGER.info("#INSIDE EXCEPTION GETPARTICULARSLIST " + e.getMessage());
		}
		return particularsList;
	}

	@SuppressWarnings("rawtypes")
	@Override
	public int removeParticulars(Particulars particulars) {
		int value = 0;
		LOGGER.info("# INSIDE IN REMOVEPARTICULARS ");
		Session session = getCurrentSession();
		try {
			Query query = session.createQuery("delete Particulars where id = :ID");
			query.setParameter("ID", new Integer(particulars.getId()));
			int result = query.executeUpdate();
			if (result > 0)
				value = 1;
		} catch (TransactionException e) {
			value = 2;
			LOGGER.info("# INSIDE INSIDE IN REMOVEPARTICULARS " + e.getMessage());
		}
		return value;
	}

	@Override
	public List<SavingOptions> getSavingOptionByBranchId(Integer branchId) {
		LOGGER.info("#INSIDE  IN GETSAVINGOPTIONBYBRANCHID ");
		List<SavingOptions> list = null;
		Session session = getCurrentSession();
		try {
			list = session.createNativeQuery("select * from saving_options where branch_id = :branchId")
					.addEntity(SavingOptions.class).setParameter("branchId", branchId).getResultList();
		} catch (Exception e) {
			LOGGER.error("#INSIDE EXCEPTION OCCURED IN GETSAVINGOPTIONBYBRANCHID " + e.getMessage());
			e.printStackTrace();
		}
		return list;
	}

	@Override
	public List<SavingOptionsRole> getSavingOptionRole(Integer savingOptionsId) {
		LOGGER.info("#INSIDE  IN GETSAVINGOPTIONROLE ");
		List<SavingOptionsRole> list = null;
		Session session = getCurrentSession();
		try {
			list = session
					.createNativeQuery("select * from saving_options_role where saving_options_id = :savingOptionsId")
					.addEntity(SavingOptionsRole.class).setParameter("savingOptionsId", savingOptionsId)
					.getResultList();
		} catch (Exception e) {
			LOGGER.error("#INSIDE EXCEPTION OCCURED IN GETSAVINGOPTIONROLE " + e.getMessage());
			e.printStackTrace();
		}
		return list;
	}

	@Override
	public List<Currency> getCurrencyList() {
		LOGGER.info("#INSIDE  IN GETCURRENCYLIST ");
		List<Currency> list = null;
		Session session = getCurrentSession();
		try {
			list = session.createQuery("From Currency ").getResultList();
		} catch (Exception e) {
			LOGGER.error("#INSIDE EXCEPTION OCCURED IN GETCURRENCYLIST " + e.getMessage());
		}
		return list;
	}

	@Override
	public List<SetPointsForSaving> getPointsForSaving(Integer branchId, Integer savingOptionsId, Integer roleId) {
		LOGGER.info("#INSIDE  IN GETPOINTSFORSAVING ");
		List<SetPointsForSaving> list = null;
		Session session = getCurrentSession();
		try {
			StringBuffer query = new StringBuffer("select * from set_points_for_saving ");
			if (branchId != 0 && savingOptionsId == 0) {
				query.append(" where branch_id = {branchId}");
			} else if (savingOptionsId != 0 && branchId == 0) {
				query.append(" where saving_options_id = {savingOptionsId} AND role_id= {roleId}");
			}
			System.out.println(query.toString());
			list = session
					.createNativeQuery(
							query.toString().replaceAll(Pattern.quote("{branchId}"), String.valueOf(branchId))
									.replaceAll(Pattern.quote("{savingOptionsId}"), String.valueOf(savingOptionsId))
									.replaceAll(Pattern.quote("{roleId}"), String.valueOf(roleId)))
					.addEntity(SetPointsForSaving.class).getResultList();
			System.out.println(query.toString().replaceAll(Pattern.quote("{branchId}"), String.valueOf(branchId))
					.replaceAll(Pattern.quote("{savingOptionsId}"), String.valueOf(savingOptionsId)));
		} catch (Exception e) {
			LOGGER.error("#INSIDE EXCEPTION OCCURED IN GETPOINTSFORSAVING " + e.getMessage());
		}
		return list;
	}

	@Override
	public List<SetPointsForSaving> getPointsForSavingByRoleId(Integer branchId, Integer savingOptionsRoleId,
			Integer savingOptionsId, Integer roleId) {
		LOGGER.info("#INSIDE  IN GETPOINTSFORSAVING Role Id");
		List<SetPointsForSaving> list = null;
		Session session = getCurrentSession();
		try {
			StringBuffer query = new StringBuffer("select * from set_points_for_saving ");
			if (branchId != 0 && savingOptionsRoleId == 0) {
				query.append(" where branch_id = {branchId}");
			} else if (savingOptionsRoleId != 0 && branchId == 0) {
				query.append(
						" where saving_options_role_id = {savingOptionsRoleId} and saving_options_id = {savingOptionsId}  ");
				if (roleId > 0) {
					query.append(" and role_id={roleId} ");// and role_id={roleId}
				}
			}
			list = session
					.createNativeQuery(
							query.toString().replaceAll(Pattern.quote("{branchId}"), String.valueOf(branchId))
									.replaceAll(Pattern.quote("{savingOptionsId}"), String.valueOf(savingOptionsId))
									.replaceAll(Pattern.quote("{savingOptionsRoleId}"),
											String.valueOf(savingOptionsRoleId))
									.replaceAll(Pattern.quote("{roleId}"), String.valueOf(roleId)))
					.addEntity(SetPointsForSaving.class).getResultList();
			System.out.println(query.toString().replaceAll(Pattern.quote("{branchId}"), String.valueOf(branchId))
					.replaceAll(Pattern.quote("{savingOptionsRoleId}"), String.valueOf(savingOptionsRoleId)));
		} catch (Exception e) {
			LOGGER.error("#INSIDE EXCEPTION OCCURED IN GETPOINTSFORSAVING " + e.getMessage());
			e.printStackTrace();
		}
		return list;
	}

	@Override
	public List<SavingOptions> checkSavingOptionsExists(SavingOptions savingOptions) {
		LOGGER.info("#INSIDE  IN CHECKSAVINGOPTIONSEXISTS ");
		List<SavingOptions> list = null;
		Session session = getCurrentSession();
		try {
			list = session
					.createNativeQuery("select * from saving_options where name = :name and branch_id = :branchId")
					.addEntity(SavingOptions.class).setParameter("name", savingOptions.getName())
					.setParameter("branchId", savingOptions.getBranch().getBranchId()).getResultList();
		} catch (Exception e) {
			LOGGER.error("#INSIDE EXCEPTION OCCURED IN CHECKSAVINGOPTIONSEXISTS " + e.getMessage());
			e.printStackTrace();
		}
		return list;
	}

	@Override
	public boolean createSavingOptions(SavingOptions savingOptions) {
		boolean flag = false;
		Session session = getCurrentSession();
		LOGGER.info("#INSIDE IN CREATESAVINGOPTIONS  ");
		try {
			session.save(savingOptions);
			flag = true;
		} catch (Exception e) {
			LOGGER.error("#INSIDE EXCEPTION OCCURED IN CREATESAVINGOPTIONS " + e.getMessage());
			e.printStackTrace();
		}
		return flag;
	}

	@Override
	public boolean updateSavingOptions(SavingOptions savingOptions) {
		boolean flag = false;
		Session session = getCurrentSession();
		LOGGER.info("#INSIDE IN UPDATESAVINGOPTIONS  ");
		try {
			session.update(savingOptions);
			flag = true;
		} catch (Exception e) {
			LOGGER.error("#INSIDE EXCEPTION OCCURED IN UPDATESAVINGOPTIONS " + e.getMessage());
			e.printStackTrace();
		}
		return flag;
	}

	@Override
	public List<SavingOptionsRole> checkSavingOptionsExists(SavingOptionsRole savingOptionsRole) {
		LOGGER.info("#INSIDE  IN CHECKSAVINGOPTIONSEXISTS ");
		List<SavingOptionsRole> list = null;
		Session session = getCurrentSession();
		try {
			list = session.createNativeQuery(
					"select * from saving_options_role where name = :name and saving_options_id = :savingOptionsId")
					.addEntity(SavingOptionsRole.class).setParameter("name", savingOptionsRole.getName())
					.setParameter("savingOptionsId", savingOptionsRole.getSavingOptions().getId()).getResultList();
		} catch (Exception e) {
			LOGGER.error("#INSIDE EXCEPTION OCCURED IN CHECKSAVINGOPTIONSEXISTS " + e.getMessage());
			e.printStackTrace();
		}
		return list;
	}

	@Override
	public boolean createSavingOptionsRole(SavingOptionsRole savingOptionsRole) {
		boolean flag = false;
		Session session = getCurrentSession();
		LOGGER.info("#INSIDE IN CREATESAVINGOPTIONSROLE  ");
		try {
			session.save(savingOptionsRole);
			flag = true;
		} catch (Exception e) {
			LOGGER.error("#INSIDE EXCEPTION OCCURED IN CREATESAVINGOPTIONSROLE " + e.getMessage());
			e.printStackTrace();
		}
		return flag;
	}

	@Override
	public boolean updateSavingOptionsRole(SavingOptionsRole savingOptionsRole) {
		boolean flag = false;
		Session session = getCurrentSession();
		LOGGER.info("#INSIDE IN UPDATESAVINGOPTIONSROLE  ");
		try {
			session.update(savingOptionsRole);
			flag = true;
		} catch (Exception e) {
			LOGGER.error("#INSIDE EXCEPTION OCCURED IN UPDATESAVINGOPTIONSROLE " + e.getMessage());
			e.printStackTrace();
		}
		return flag;
	}

	@Override
	public boolean createSetPointsForSaving(List<SetPointsForSaving> setPointsForSaving) {
		boolean flag = false;
		Session session = getCurrentSession();
		LOGGER.info("#INSIDE IN CREATESETPOINTSFORSAVING  ");
		try {
			for (SetPointsForSaving saveObj : setPointsForSaving) {
				session.save(saveObj);
				flag = true;
			}

		} catch (Exception e) {
			LOGGER.error("#INSIDE EXCEPTION OCCURED IN CREATESETPOINTSFORSAVING " + e.getMessage());
			e.printStackTrace();
		}
		return flag;
	}

	@Override
	public boolean updateSetPointsForSaving(List<SetPointsForSaving> setPointsForSaving) {
		boolean flag = false;
		Session session = getCurrentSession();
		LOGGER.info("#INSIDE IN UPDATESETPOINTSFORSAVING  ");
		try {
			for (SetPointsForSaving updateObj : setPointsForSaving) {
				session.update(updateObj);
				flag = true;
			}

		} catch (Exception e) {
			LOGGER.error("#INSIDE EXCEPTION OCCURED IN UPDATESETPOINTSFORSAVING " + e.getMessage());
			e.printStackTrace();
		}
		return flag;
	}

	@Override
	public List<SetPointsForSaving> checkSetPointsForSavingExists(SetPointsForSaving request) {
		LOGGER.info("#INSIDE  IN CHECKSETPOINTSFORSAVINGEXISTS ");
		List<SetPointsForSaving> list = null;
		Session session = getCurrentSession();
		try {
			list = session.createNativeQuery(
					" select * from set_points_for_saving where (amount_upto = :amountUpto and minutes = :minutes) and branch_id= :branchId and saving_options_id = :savingOptionsId and saving_options_role_id = :savingOptionsRoleId")
					.addEntity(SetPointsForSaving.class).setParameter("amountUpto", request.getAmountUpto())
					.setParameter("branchId", request.getBranch().getBranchId())
					.setParameter("savingOptionsId", request.getSavingOptions().getId())
					.setParameter("minutes", request.getMinutes())
					.setParameter("savingOptionsRoleId", request.getSavingOptionsRole().getId()).getResultList();
		} catch (Exception e) {
			LOGGER.error("#INSIDE EXCEPTION OCCURED IN CHECKSETPOINTSFORSAVINGEXISTS " + e.getMessage());
			e.printStackTrace();
		}
		return list;
	}

	@Override
	public int addOrupdateDocument(MasterDocument document) {
		LOGGER.info("INSIDE ADDORUPDATEDOCUMENT API");
		int flag = 0;
		Session session = getCurrentSession();
		try {
			document.setOwner(new EmployeeDetails(document.getOwnerId()));
			document.setBranch(new BranchMaster(document.getBranchId()));
			if (document.getId() == EnovationConstants.ZERO) {
				document.setCreatedBy(document.getCreatedBy());
				document.setCreatedDate(CommonUtils.currentDate());
				session.save(document);
			} else {
				MasterDocument doc = session.get(MasterDocument.class, document.getId());
				if (document.getOwnerId() != doc.getOwner().getEmpId()) {
					List<Object[]> sugList = session.createNativeQuery("select  * "
							+ "from tbl_sugession_details sug where sug.under_action=:under_action and sug.sts_id=:pendingdocupdate")
							.setParameter("under_action", doc.getOwner().getEmpId())
							.setParameter("pendingdocupdate", EnovationConstants.PENDING_DOCUMENT_UPDATE_ID)
							.getResultList();
					if (sugList.size() > 0) {
						flag = 1;
					} else {
						if (document.getUpdatedBy() > 0)
							doc.setUpdatedBy(document.getUpdatedBy());
						doc.setUpdatedDate(CommonUtils.currentDate());
						doc.setOwner(new EmployeeDetails(document.getOwnerId()));
						doc.setDocumentName(document.getDocumentName());
						if (document.getLine() != null)
							doc.setLine(document.getLine());
						if (document.getDept() != null)
							doc.setDept(document.getDept());
						session.update(doc);
					}
				} else {
					if (document.getUpdatedBy() > 0)
						doc.setUpdatedBy(document.getUpdatedBy());
					doc.setUpdatedDate(CommonUtils.currentDate());
					doc.setOwner(new EmployeeDetails(document.getOwnerId()));
					doc.setDocumentName(document.getDocumentName());
					if (document.getLine() != null)
						doc.setLine(document.getLine());
					if (document.getDept() != null)
						doc.setDept(document.getDept());
					session.update(doc);
				}
			}
		} catch (Exception e) {
			LOGGER.info("EXCEPTION INSIDE ADDORUPDATEDOCUMENT API" + e.getMessage());
			e.printStackTrace();
			flag = 2;
		}
		return flag;
	}

	@Override
	public int deleteDocument(MasterDocument document) {

		LOGGER.info("INSIDE deleteDocument DaoImpl");
		int flag = 0;
		Session session = getCurrentSession();
		try {
			List<Object[]> sugList = session
					.createNativeQuery("SELECT * FROM suggestion_document WHERE document_id=:documentid")
					.setParameter("documentid", document.getId()).getResultList();
			if (sugList.size() == EnovationConstants.ZERO) {
				session.createNativeQuery("delete FROM master_document WHERE id=:documentid")
						.setParameter("documentid", document.getId()).executeUpdate();
			} else {
				flag = 1;
			}
		} catch (Exception e) {
			LOGGER.info("INSIDE deleteDocument DaoImpl exception=" + e.getMessage());
			e.printStackTrace();
			flag = 2;
		}
		return flag;
	}

	@Override
	public int removeSavingOptionsRole(SavingOptionsRole savingOptionsRole) {
		int value = 0;
		LOGGER.info("# INSIDE IN REMOVESAVINGOPTIONSROLE ");
		Session session = getCurrentSession();
		try {
			int result = session.createQuery("delete SavingOptionsRole where id = :ID")
					.setParameter("ID", savingOptionsRole.getId()).executeUpdate();
			if (result > 0) {
				value = 1;
			}
		} catch (TransactionException e) {
			value = 2;
			LOGGER.info("# INSIDE INSIDE IN REMOVESAVINGOPTIONSROLE " + e.getMessage());
		}
		return value;
	}

	@Override
	public int removeSetPointsForSaving(SetPointsForSaving setPointsForSaving) {
		int value = 0;
		LOGGER.info("# INSIDE IN REMOVESETPOINTSFORSAVING ");
		Session session = getCurrentSession();
		try {
			int result = session.createQuery("delete SetPointsForSaving where id = :ID")
					.setParameter("ID", setPointsForSaving.getId()).executeUpdate();
			if (result > 0) {
				value = 1;
			}
		} catch (TransactionException e) {
			value = 2;
			LOGGER.info("# INSIDE INSIDE IN REMOVESETPOINTSFORSAVING " + e.getMessage());
		}
		return value;
	}

	@Override
	public List<ActivityMaster> getActivityList() {
		LOGGER.info("INSIDE GETACTIVITYLIST DAOIMPL");
		List<ActivityMaster> list = null;
		Session session = getCurrentSession();
		try {
			list = session.createQuery("FROM ActivityMaster").getResultList();

		} catch (Exception e) {
			LOGGER.info("INSIDE GETACTIVITYLIST DAOIMPL EXCEPTION=" + e.getMessage());
		}
		return list;
	}

	@Override
	public List<Branch> getBranchListNEW(Integer orgId) {
		LOGGER.info("INSIDE GETBRANCHLISTNEW API");
		Session session = getCurrentSession();
		List<Branch> list = new ArrayList<>();
		try {

			List<Object[]> branchList = session
					.createNativeQuery(" select b.branch_id,b.name,b.location,d.dept_id,d.dept_name "
							+ " from master_branch b left join  master_department d ON d.branch_id=b.branch_id "
							+ " where b.org_id =:orgId ")
					.setParameter("orgId", orgId).getResultList();
			for (Object[] branch : branchList) {
				Branch doc = new Branch();
				if (branch[0] != null) {
					doc.setBranchId(Integer.valueOf(String.valueOf(branch[0])));
				}
				if (branch[1] != null) {
					doc.setName(String.valueOf(branch[1]));
				}
				if (branch[2] != null) {
					doc.setLocation(String.valueOf(branch[2]));
				}
				if (branch[3] != null) {
					doc.setDeptId(Integer.valueOf(String.valueOf(branch[3])));
				}
				if (branch[4] != null) {
					doc.setDeptName(String.valueOf(branch[4]));
				}
				list.add(doc);

			}

		} catch (Exception e) {
			LOGGER.info("EXCEPTION INSIDE GETBRANCHLISTNEW" + e.getMessage());
			e.printStackTrace();
		}
		return list;
	}

	@Override
	public List<Branch> getBranchDetails(Integer branchId) {
		LOGGER.info("INSIDE GETBRANCHDETAILS API");
		Session session = getCurrentSession();
		List<Branch> list = new ArrayList<>();
		try {

			List<Object[]> branchList = session
					.createNativeQuery(" select b.branch_id,b.name,b.location,d.dept_id,d.dept_name "
							+ " from master_branch b left join  master_department d ON d.branch_id=b.branch_id "
							+ " where b.branch_id =:branchId ")
					.setParameter("branchId", branchId).getResultList();
			for (Object[] branch : branchList) {
				Branch doc = new Branch();
				if (branch[0] != null) {
					doc.setBranchId(Integer.valueOf(String.valueOf(branch[0])));
				}
				if (branch[1] != null) {
					doc.setName(String.valueOf(branch[1]));
				}
				if (branch[2] != null) {
					doc.setLocation(String.valueOf(branch[2]));
				}
				if (branch[3] != null) {
					doc.setDeptId(Integer.valueOf(String.valueOf(branch[3])));
				}
				if (branch[4] != null) {
					doc.setDeptName(String.valueOf(branch[4]));
				}
				list.add(doc);

			}

		} catch (Exception e) {
			LOGGER.info("EXCEPTION INSIDE GETBRANCHDETAILS" + e.getMessage());
			e.printStackTrace();
		}
		return list;
	}

	@Override
	public List<ProductOrgConfig> getProductOrgConfigByBranchId(int branchId) {
		List<ProductOrgConfig> list = new ArrayList<>();
		Session session = getCurrentSession();
		LOGGER.info("#INSIDE IN getProductOrgConfigByBranchId :");
		try {
			list = session.createQuery("from ProductOrgConfig where branch.branchId =:branchId")
					.setParameter("branchId", branchId).getResultList();
		} catch (Exception e) {
			LOGGER.error("#INSIDE EXCEPTION OCCURED IN getProductOrgConfigByBranchId :" + e.getMessage());
		}
		return list;
	}

	@Override
	public List<LanguageMaster> getLanguageList() {

		LOGGER.info("# INSIDE  IN GETLANGUGAELIST ");
		Session session = getCurrentSession();
		List<LanguageMaster> langList = null;
		try {
			langList = session.createQuery("from LanguageMaster").getResultList();
			System.out.println("/n Printing Size of GetLang list : " + langList.size());

		} catch (Exception e) {
			LOGGER.info("#INSIDE EXCEPTION OCCURED IN getLanguageList DAO : " + e.getMessage());
		}
		return langList;
	}

	@Override
	public boolean saveRewards(Rewards rewards) {
		LOGGER.info("INSIDE SAVE_REWARDS API");
		Session session = getCurrentSession();
		boolean flag = false;
		try {
			rewards.setGiftCoupen(EnovationConstants.GiftCoupen);
			String picpathcut = null;
			if (rewards.getRewardImage() != null && rewards.getRewardImage().length > 0) {

				String imgDirctory = EnovationConfig.buddyConfig.get(EnovationConstants.RewardsImgPath) + "/"
						+ EnovationConstants.RewardsFolderName + "/" + rewards.getOrg().getOrgId() + "/"
						+ rewards.getBranch().getBranchId();

				String filePathToTrim = EnovationConstants.RewardsFolderName + "/" + rewards.getOrg().getOrgId() + "/"
						+ rewards.getBranch().getBranchId();
				for (MultipartFile sugDoc : rewards.getRewardImage()) {
					picpathcut = WriteFilesUtils.writeFileOnServer(sugDoc, imgDirctory, filePathToTrim);
				}
			}
			if (picpathcut != null) {
				rewards.setUrl(picpathcut);
			}
			rewards.setIsEnable(EnovationConstants.ONE);
			session.save(rewards);
			String giftCode = rewards.getGiftCoupen() + String.valueOf(rewards.getId())
					+ String.valueOf(rewards.getBranch().getBranchId()) + String.valueOf(rewards.getOrg().getOrgId());
			int status = session.createNativeQuery("UPDATE rewards Set gift_code=:giftCode where id=:id")
					.setParameter("id", rewards.getId()).setParameter("giftCode", giftCode).executeUpdate();
			if (status > 0) {
				flag = true;
			}
		} catch (Exception e) {
			e.printStackTrace();
			LOGGER.error("#INSIDE EXCEPTION OCCURED IN SAVE_REWARDS API" + e.getMessage());
		}
		return flag;
	}

	@Override
	public List<Rewards> getRewardsList(Integer branchId) {
		LOGGER.info("INSIDE LIST_OF_REWARDS API");
		Session session = getCurrentSession();
		List<Rewards> list = new ArrayList<>();
		try {
			List<Object[]> rows = session.createNativeQuery(
					"SELECT r.id, r.gift_code,r.description,r.points,r.url,r.is_enable,c.id as cat_id,c.category_name,r.amount from rewards r, reward_category_master c where r.cat_id=c.id and r.branch_id =:branchId and r.is_enable=1 order by r.id desc;")
					.setParameter("branchId", branchId).getResultList();

			Rewards rewards = null;
			for (Object[] row : rows) {
				rewards = new Rewards();
				if (row[0] != null) {
					rewards.setId(Integer.parseInt(row[0].toString()));
				}
				if (row[1] != null) {
					rewards.setGiftCode(String.valueOf(row[1]));
				}
				if (row[2] != null) {
					rewards.setDescription(String.valueOf(row[2]));
				}
				if (row[3] != null) {
					rewards.setPoints(Integer.parseInt(row[3].toString()));
				}
				if (row[4] != null) {
					rewards.setUrl(String.valueOf(row[4]));
				}
				if (row[5] != null) {
					rewards.setIsEnable(Integer.parseInt(row[5].toString()));
				}
				RewardCategoryMaster rcm = new RewardCategoryMaster();
				if (row[6] != null) {
					rcm.setId(Integer.parseInt(row[6].toString()));
				}
				if (row[7] != null) {
					rcm.setCategoryName(String.valueOf(row[7]));
				}
				if (row[8] != null) {
					rewards.setAmount(Integer.parseInt(row[8].toString()));
				}
				rewards.setCategory(rcm);
				list.add(rewards);
			}
		} catch (Exception e) {
			LOGGER.error("#INSIDE EXCEPTION OCCURED IN LIST_OF_REWARDS API" + e.getMessage());
		}
		return list;
	}

	@Override
	public boolean updateRewards(Rewards reward) {
		LOGGER.info("INSIDE UPDATE_REWARD API");
		Session session = getCurrentSession();
		boolean flag = false;
		try {
			String picpathcut = null;
			Rewards rew = (Rewards) session.get(Rewards.class, reward.getId());

			if (reward.getPoints() > 0) {
				rew.setPoints(reward.getPoints());
			}
			if (reward.getAmount() > 0) {
				rew.setAmount(reward.getAmount());
			}
			if (reward.getDescription() != null) {
				rew.setDescription(reward.getDescription());
			}
			if (reward.getCategory() != null) {
				rew.setCategory(new RewardCategoryMaster(reward.getCategory().getId()));
			}

			if (reward.getRewardImage() != null) {
				String imgDirctory = EnovationConfig.buddyConfig.get(EnovationConstants.RewardsImgPath) + "/"
						+ EnovationConstants.RewardsFolderName + "/" + reward.getOrg().getOrgId() + "/"
						+ reward.getBranch().getBranchId();

				String filePathToTrim = EnovationConstants.RewardsFolderName + "/" + reward.getOrg().getOrgId() + "/"
						+ reward.getBranch().getBranchId();

				for (MultipartFile img : reward.getRewardImage()) {
					picpathcut = WriteFilesUtils.writeFileOnServer(img, imgDirctory, filePathToTrim);
					rew.setUrl(picpathcut);
				}
			}
			session.update(rew);
			flag = true;
		} catch (Exception e) {
			e.printStackTrace();
			LOGGER.error("Exception occured in UPDATE_REWARDS_API" + e.getMessage());
		}
		return flag;
	}

	@Override
	public boolean deleteRewards(Rewards reward) {
		LOGGER.info("Inside DELETE_REWARDS_API");
		Session session = getCurrentSession();
		boolean flag = false;
		try {
			int status = session.createNativeQuery("update rewards set is_enable=:isEnable where id=:Id")
					.setParameter("Id", reward.getId()).setParameter("isEnable", reward.getIsEnable()).executeUpdate();
			if (status > 0) {
				flag = true;
			}
		} catch (Exception e) {
			LOGGER.error("Exception occured in DELETE_REWARDS_API" + e.getMessage());
		}
		return flag;
	}

	@Override
	public boolean saveVendorDetails(Vendor vendor) {
		LOGGER.info("INSIDE SAVE_VENDOR_DETAILS API");
		Session session = getCurrentSession();
		boolean flag = false;
		try {
			vendor.setIsEnable(EnovationConstants.ONE);
			session.save(vendor);
			flag = true;
		} catch (Exception e) {
			LOGGER.error("Exception occured inside SAVE_VENDOR API" + e.getMessage());
		}
		return flag;
	}

	@Override
	public boolean updateVendorDetails(Vendor vendor) {
		LOGGER.info("INSIDE UPDATE_VENDOR_DETAILS API");
		Session session = getCurrentSession();
		boolean flag = false;
		try {
			Vendor ven = (Vendor) session.get(Vendor.class, vendor.getId());

			if (vendor.getName() != null) {
				ven.setName(vendor.getName());
			}
			if (vendor.getEmailId() != null) {
				ven.setEmailId(vendor.getEmailId());
			}
			if (vendor.getContactNo() != 0) {
				ven.setContactNo(vendor.getContactNo());
			}
			session.update(ven);
			flag = true;
		} catch (Exception e) {
			LOGGER.error("Exception occured inside UPDATE_VENDOR_DETAILS API" + e.getMessage());
		}
		return flag;
	}

	@Override
	public boolean deleteVendorDetails(Vendor vendor) {
		LOGGER.info("Inside DELETE_VENDOR_DETAILS_API");
		Session session = getCurrentSession();
		boolean flag = false;
		try {
			int status = session.createNativeQuery("update vendor set is_enable=:isEnable where id=:Id")
					.setParameter("Id", vendor.getId()).setParameter("isEnable", vendor.getIsEnable()).executeUpdate();
			if (status > 0)
				flag = true;
		} catch (Exception e) {
			LOGGER.error("Exception occured in DELETE_VENDOR_DETAILS_API" + e.getMessage());
		}
		return flag;
	}

	@Override
	public List<Vendor> getVendorDetails(Integer branchId) {
		LOGGER.info("INSIDE LIST_OF_VENDOR API");
		Session session = getCurrentSession();
		List<Vendor> list = new ArrayList<>();
		try {
			List<Object[]> rows = session.createNativeQuery(
					"select v.id,v.name,v.email_id,v.contact_no,v.is_enable from vendor v where v.branch_id=:branchId and v.is_enable=1 order by v.id desc")
					.setParameter("branchId", branchId).getResultList();
			Vendor vendor = null;
			for (Object[] row : rows) {
				vendor = new Vendor();
				if (row[0] != null) {
					vendor.setId(Integer.parseInt(row[0].toString()));
				}
				if (row[1] != null) {
					vendor.setName(String.valueOf(row[1]));
				}
				if (row[2] != null) {
					vendor.setEmailId(String.valueOf(row[2]));
				}
				if (row[3] != null) {
					vendor.setContactNo(Long.valueOf(row[3].toString()));
				}
				if (row[4] != null) {
					vendor.setIsEnable(Integer.parseInt(row[4].toString()));
				}
				list.add(vendor);
			}
		} catch (Exception e) {
			LOGGER.error("#INSIDE EXCEPTION OCCURED IN LIST_OF_VENDORS API" + e.getMessage());
		}
		return list;
	}

	@Override
	public boolean addRewardCategory(RewardCategoryMaster mrc) {
		LOGGER.info("INSIDE ADD_REWARD_CATEGORY_API");
		Session session = getCurrentSession();
		boolean flag = false;
		try {
			mrc.setIsEnable(EnovationConstants.ONE);
			Object obj = session.save(mrc);
			if (obj != null) {
				flag = true;
			}
		} catch (Exception e) {
			LOGGER.error("INSIDE EXCEPTION OCCURED IN ADD_REWARD_CATEGORY_API" + e.getMessage());
		}
		return flag;
	}

	@Override
	public boolean addRewardResponsibleData(RewardResponsibleData rrd) {
		LOGGER.info("INSIDE ADD_REWARD_RRESPONSIBLE_PERSON_DATA_API");
		Session session = getCurrentSession();
		boolean flag = false;
		try {
			rrd.setIsEnable(EnovationConstants.ONE);
			session.save(rrd);
			flag = true;
		} catch (Exception e) {
			LOGGER.error("#INSIDE EXCEPTION OCCURED IN ADD_REWARD_RRESPONSIBLE_PERSON_DATA_API" + e.getMessage());
		}
		return flag;
	}

	@Transactional
	@Override
	public boolean updateRewardResponsibleData(RewardResponsibleData rrd) {
		LOGGER.info("INSIDE UPDATE_REWARD_RRESPONSIBLE_PERSON_DATA API");
		boolean flag = false;
		Session session = getCurrentSession();
		try {
			RewardResponsibleData r = (RewardResponsibleData) session.get(RewardResponsibleData.class, rrd.getId());
			if (rrd.getRewardAdmin() != null) {
				r.setRewardAdmin(new RewardAdminSetup(rrd.getRewardAdmin().getId()));
			}
			if (rrd.getVendor() != null) {
				r.setVendor(new Vendor(rrd.getVendor().getId()));
			}
			session.update(r);
			flag = true;
		} catch (Exception e) {
			e.printStackTrace();
			LOGGER.error("#INSIDE EXCEPTION OCCURED IN UPDATE_REWARD_RRESPONSIBLE_PERSON_DATA" + e.getMessage());
		}
		return flag;
	}

	@Override
	public boolean deleteRewardResponsibleData(RewardResponsibleData rrd) {
		LOGGER.info("INSIDE DELETE_REWARD_RRESPONSIBLE_PERSON_DATA_API");
		Session session = getCurrentSession();
		boolean flag = false;
		try {
			int status = session
					.createNativeQuery("update reward_responsible_data set is_enable=:isEnable where id=:ID")
					.setParameter("ID", rrd.getId()).setParameter("isEnable", rrd.getIsEnable()).executeUpdate();
			if (status > 0)
				flag = true;
		} catch (Exception e) {
			LOGGER.error("#INSIDE EXCEPTION OCCURED IN DELETE_REWARD_RRESPONSIBLE_PERSON_DATA_API" + e.getMessage());
		}
		return flag;
	}
	
	@Override
	public boolean addToCart(Cart cart) {
		LOGGER.info("INSIDE ADD_TO_CART_API");
		Session session = getCurrentSession();
		boolean flag = false;
		try {
			Object obj = session.save(cart);
			if (obj != null)
				flag = true;
		} catch (Exception e) {
			LOGGER.error("#INSIDE EXCEPTION OCCURED IN ADD_TO_CART_API API" + e.getMessage());
		}
		return flag;
	}

	@Override
	public List<Cart> getCartList(Integer empId) {
		LOGGER.info("Inside CART_LIST_API");
		Session session = getCurrentSession();
		List<Cart> list = new ArrayList<>();
		try {
			List<Object[]> rows = session.createNativeQuery("SELECT \r\n"
					+ "    c.id, c.reward_resp_id, c.vendor_id, r.gift_code, r.description,r.url, r.points, r.id as reward_id, c.quantity,r.amount\r\n"
					+ "FROM\r\n" + "    cart c\r\n" + "        INNER JOIN\r\n"
					+ "    rewards r ON r.id = c.reward_id\r\n" + "        LEFT JOIN\r\n"
					+ "    tbl_employee_details e ON c.emp_id = e.emp_id\r\n" + "WHERE\r\n" + " c.emp_id=:empId"
					+ " and c.is_order=0 order by c.id desc\r\n").setParameter("empId", empId).getResultList();

			Cart cartList = null;
			for (Object[] row : rows) {
				cartList = new Cart();
				if (row[0] != null) {
					cartList.setId(Integer.parseInt(row[0].toString()));
				}
				RewardResponsibleData resdata = new RewardResponsibleData();
				if (row[1] != null) {
					resdata.setId(Integer.parseInt(row[1].toString()));
				}
				cartList.setRewardRespData(resdata);
				Vendor v = new Vendor();
				if (row[2] != null) {
					v.setId(Integer.parseInt(row[2].toString()));
				}
				cartList.setVendor(v);
				Rewards r = new Rewards();
				if (row[3] != null) {
					r.setGiftCode(String.valueOf(row[3]));
				}
				if (row[4] != null) {
					r.setDescription(String.valueOf(row[4]));
				}
				if (row[5] != null) {
					r.setUrl(String.valueOf(row[5]));
				}
				if (row[6] != null) {
					r.setPoints(Integer.parseInt(row[6].toString()));
				}
				if (row[7] != null) {
					r.setId(Integer.parseInt(row[7].toString()));
				}
				if (row[9] != null) {
					r.setAmount(Integer.parseInt(row[9].toString()));
				}
				cartList.setRewards(r);
				if (row[8] != null) {
					cartList.setQuantity(Integer.parseInt(row[8].toString()));
				}

				list.add(cartList);
			}
		} catch (Exception e) {
			LOGGER.error("#INSIDE EXCEPTION OCCURED IN CART_LIST_API " + e.getMessage());
		}
		return list;
	}

	@Override
	public boolean deleteFromCart(Cart cart) {
		LOGGER.info("INSIDE DELETE_FROM_CART_API");
		Session session = getCurrentSession();
		boolean flag = false;
		try {
			int status = session.createNativeQuery("delete from cart where id=:ID").setParameter("ID", cart.getId())
					.executeUpdate();
			if (status > 0)
				flag = true;
		} catch (Exception e) {
			e.printStackTrace();
			LOGGER.info("#INSIDE EXCEPTION OCCURED IN DELETE_FROM_CART_API" + e.getMessage());
		}
		return flag;
	}

	@Override
	public int redeemFromCart(List<RedeemCartRewards> redeemGiftList) {
		LOGGER.info("Inside REDEEM_FROM_REWARDS_API");
		int flag = 0;
		List<MailDTO> mailList = new ArrayList<>();
		Session session = getCurrentSession();

		try {
			EmailTemplateMaster messageContent = enoConfig.getMessageContent(EnovationConstants.MASTER_EMAIL_TEMPLATE);
			EmployeeDetails emp = (EmployeeDetails) session.get(EmployeeDetails.class,
					redeemGiftList.get(0).getEmpId());

			if (emp.getPoints() != 0 && redeemGiftList.get(0).getTotalPoint() <= emp.getPoints()) {
				System.out.println("request list size :" + redeemGiftList.size());
				System.out.println("Total Points: " + redeemGiftList.get(0).getTotalPoint());
				for (RedeemCartRewards x : redeemGiftList) {
					String mailContent = null;
					String vendorMailContent = null;
					String mailContentToSend = null;
					String vendorMailContentToSend = null;
					x.setEmpdetails(new EmployeeDetails(x.getEmpId()));
					Object obj = session.save(x);

					if (obj != null) {
						List<Object[]> list = session.createNativeQuery(
								"SELECT ed.email_id as adminemail, ed.first_name as admin_name,e.cmpy_emp_id, e.first_name,e.last_name,d.dept_id,d.dept_name,rs.total_point,rs.product_point,      \r\n"
										+ "rw.gift_code,o.portal_link,v.name,v.email_id FROM     \r\n"
										+ "rewards rw,     \r\n" + "redeem_cart_rewards rs,     \r\n"
										+ "reward_responsible_data rd,     \r\n" + "reward_admin_setup r,     \r\n"
										+ "tbl_employee_details e,     \r\n" + "master_department d  ,   \r\n"
										+ "tbl_employee_details ed,\r\n" + "master_organization o,\r\n" + "vendor v\r\n"
										+ "WHERE     \r\n"
										+ "rs.reward_resp_person_id = rd.id   AND rd.admin_id = r.id     \r\n"
										+ "AND rs.emp_id = e.emp_id   AND rd.reward_id = rw.id     \r\n"
										+ "And d.dept_id=e.dept_id  AND ed.emp_id=r.emp_id \r\n"
										+ "and v.id=rd.vendor_id\r\n" + "and e.org_id=o.org_id\r\n" + "AND rs.id=:Id")
								.setParameter("Id", x.getId()).getResultList();
						System.out.println("List Size==>" + list.size());
						System.out.println("x.getId()==>" + x.getId());
						String email = null;
						if (list != null && list.size() > 0) {
							for (Object[] objc : list) {
								if (messageContent.getBody() != null) {
									email = String.valueOf(objc[0]);
									mailContent = String.valueOf(env.getProperty("SEND_MAIL_ON_REWARD_REDEEM.body"))
											.replaceAll(Pattern.quote("{empId}"),
													(objc[2] != null) ? String.valueOf(objc[2]) : "")
											.replaceAll(Pattern.quote("{firstName}"),
													(objc[3] != null) ? String.valueOf(objc[3]) : "")
											.replaceAll(Pattern.quote("{lastName}"),
													(objc[4] != null) ? String.valueOf(objc[4]) : "")
											.replaceAll(Pattern.quote("{deptName}"),
													(objc[6] != null) ? String.valueOf(objc[6]) : "")
											.replaceAll(Pattern.quote("{giftInput}"),
													EnovationConstants.REDEEM_POINTS_STRING)
											.replaceAll(Pattern.quote("{points}"),
													(objc[8] != null) ? String.valueOf(objc[8]) : "")
											.replaceAll(Pattern.quote("{giftCode}"),
													(objc[9] != null) ? String.valueOf(objc[9]) : "");
									mailContentToSend = messageContent.getBody()
											.replaceAll(Pattern.quote("{name}"),
													(objc[1] != null) ? String.valueOf(objc[1]) : "")
											.replaceAll(Pattern.quote("{body}"), mailContent)
											.replaceAll(Pattern.quote("{portalLink}"),
													(objc[10] != null) ? String.valueOf(objc[10]) : "");

									vendorMailContent = String
											.valueOf(env.getProperty("SEND_MAIL_ON_REWARD_REDEEM.body"))
											.replaceAll(Pattern.quote("{empId}"),
													(objc[2] != null) ? String.valueOf(objc[2]) : "")
											.replaceAll(Pattern.quote("{firstName}"),
													(objc[3] != null) ? String.valueOf(objc[3]) : "")
											.replaceAll(Pattern.quote("{lastName}"),
													(objc[4] != null) ? String.valueOf(objc[4]) : "")
											.replaceAll(Pattern.quote("{deptName}"),
													(objc[6] != null) ? String.valueOf(objc[6]) : "")
											.replaceAll(Pattern.quote("{giftInput}"),
													EnovationConstants.REDEEM_POINTS_STRING)
											.replaceAll(Pattern.quote("{points}"),
													(objc[8] != null) ? String.valueOf(objc[8]) : "")
											.replaceAll(Pattern.quote("{giftCode}"),
													(objc[9] != null) ? String.valueOf(objc[9]) : "");
									vendorMailContentToSend = messageContent.getBody()
											.replaceAll(Pattern.quote("{name}"),
													(objc[11] != null) ? String.valueOf(objc[11]) : "")
											.replaceAll(Pattern.quote("{body}"), vendorMailContent)
											.replaceAll(Pattern.quote("{portalLink}"),
													(objc[10] != null) ? String.valueOf(objc[10]) : "");

								}

								if (objc[0] != null) {
									LOGGER.info("#EMAIL SEND");
									System.out.println(email);
									MailDTO adminMail = new MailDTO(
											String.valueOf(objc[0]) /* "sonali.b@greentinsolutions.com" */ ,
											env.getProperty("SEND_MAIL_ON_REWARD_REDEEM.subject"), mailContentToSend);
									MailDTO VendorMail = new MailDTO(
											String.valueOf(objc[12]) /* "sonalibhosaleppr@gmail.com" */,
											env.getProperty("SEND_MAIL_ON_REWARD_REDEEM.subject"),
											vendorMailContentToSend);
									mailList.add(adminMail);
									mailList.add(VendorMail);
								}
							}
						}

						session.createNativeQuery(
								" update cart set cart_status='Order Placed',is_order=1 where emp_id=:empId")
								.setParameter("empId", x.getEmpId()).executeUpdate();

						flag = 1;
					} // obj closed
				} // for each closed
				emp.setPoints(emp.getPoints() - redeemGiftList.get(0).getTotalPoint());
				session.update(emp);
				taskExecutor.execute(new MailUtil(mailList, communication));
			} else {// Insufficient fund
				flag = 2;
			}
		} catch (Exception e) {
			e.printStackTrace();
			LOGGER.error("#INSIDE EXCEPTION OCCURED IN REDEEM_FROM_CART_API" + e.getMessage());
		}
		return flag;
	}

	@Override
	public boolean saveRewardResponsibleSetup(RewardResponsibleSetup rrsetup) {
		LOGGER.info("INSIDE SAVE REWARD_RESPONSIBLE_SETUP_API");
		Session session = getCurrentSession();
		boolean flag = false;
		try {
			rrsetup.setIsEnable(EnovationConstants.ONE);
			Object obj = session.save(rrsetup);

			if (obj != null)
				flag = true;

		} catch (Exception e) {
			LOGGER.error("#INSIDE EXCEPTION OCCURED IN SAVE_REWARD_RESPONSIBLE_SETUP_API" + e.getMessage());
		}

		return flag;
	}

	@Override
	public boolean updateRewardResponsibleSetup(RewardResponsibleSetup rrsetup) {
		LOGGER.info("INSIDE UPDATE_REWARD_RESPONSIBLE_SETUP_API");
		Session session = getCurrentSession();
		boolean flag = false;
		try {
			RewardResponsibleSetup rres = (RewardResponsibleSetup) session.get(RewardResponsibleSetup.class,
					rrsetup.getId());

			if (rrsetup.getName() != null) {
				rres.setName(rrsetup.getName());
			}
			if (rrsetup.getEmailId() != null) {
				rres.setEmailId(rrsetup.getEmailId());
			}
			if (rrsetup.getContactNo() != null) {
				rres.setContactNo(rrsetup.getContactNo());
			}
			session.update(rres);
			flag = true;
		} catch (Exception e) {
			LOGGER.error("#INSIDE EXCEPTION OCCURED IN UPDATE_REWARD_RESPONSIBLE_SETUP_API" + e.getMessage());
		}
		return flag;
	}

	@Override
	public List<ThoughtOfDay> checkThoughtOfDay(ThoughtOfDay th) {
		List<ThoughtOfDay> details = null;
		Session session = getCurrentSession();
		try {
			details = session.createQuery(
					"FROM ThoughtOfDay WHERE DATE_FORMAT(createdDate,'yyyy-MM-dd')=DATE_FORMAT(now(),'yyyy-MM-dd') AND branch.branchId=:branchId AND  thought=:th ")
					.setParameter("branchId", th.getBranch().getBranchId()).setParameter("th", th.getThought())
					.getResultList();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return details;
	}

	@Override
	public boolean addThought(ThoughtOfDay th) {
		LOGGER.info("INSIDE ADDTHOUGHT");
		boolean flag = false;
		Session session = getCurrentSession();
		try {
			session.save(th);
			flag = true;
		} catch (Exception e) {
			e.printStackTrace();
			LOGGER.error("INSIDE ADDTHOUGHT API" + e.getMessage());
		}
		return flag;
	}

	@Override
	public List<ThoughtOfDay> getThought(int branchId) {
		List<Object[]> details = null;
		List<ThoughtOfDay> det = new ArrayList<>();
		Session session = getCurrentSession();
		try {
			details = session.createNativeQuery(
					"SELECT id, thought FROM thought_of_day WHERE branch_id=:branchId ORDER BY created_date DESC  ")
					.setParameter("branchId", branchId).setMaxResults(1).getResultList();
			for (Object[] obj : details) {
				ThoughtOfDay t = new ThoughtOfDay();
				if (obj[0] != null) {
					t.setId(Long.valueOf(String.valueOf(obj[0])));
				}
				if (obj[1] != null) {
					t.setThought(String.valueOf(obj[1]));
				}
				det.add(t);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return det;
	}

	@Override
	public List<MoodIndicator> checkMoodIndicator(MoodIndicator mo) {
		List<MoodIndicator> details = null;
		Session session = getCurrentSession();
		try {
			details = session.createQuery(
					"FROM MoodIndicator WHERE DATE(createdDate)=CURDATE() AND branch.branchId=:branchId AND  empDetails.empId=:empId AND rating=:rating")
					.setParameter("branchId", mo.getBranch().getBranchId())
					.setParameter("empId", mo.getEmpDetails().getEmpId()).setParameter("rating", mo.getRating())
					.getResultList();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return details;
	}

	@Override
	public boolean addMoodIndicator(MoodIndicator mo) {
		LOGGER.info("INSIDE ADDMOODINDICATOR");
		boolean flag = false;
		Session session = getCurrentSession();
		try {
			session.save(mo);
			flag = true;
		} catch (Exception e) {
			e.printStackTrace();
			LOGGER.error("INSIDE ADDMOODINDICATOR API" + e.getMessage());
		}
		return flag;
	}

	public boolean deleteRewardResponsibleSetup(RewardResponsibleSetup rrsetup) {
		LOGGER.info("INSIDE DELETE REWARD_RESPONSIBLE_SETUP_API");
		Session session = getCurrentSession();
		boolean flag = true;
		try {
			int status = session
					.createNativeQuery("update reward_responsible_setup set is_enable=:isEnable where id=:Id")
					.setParameter("Id", rrsetup.getId()).setParameter("isEnable", rrsetup.getIsEnable())
					.executeUpdate();
			if (status > 0)
				flag = true;
		} catch (Exception e) {
			LOGGER.error("#INSIDE EXCEPTION OCCURED IN DELETE_REWARD_RESPONSIBLE_SETUP_API" + e.getMessage());
		}
		return flag;
	}

	@Override
	public List<RewardResponsibleSetup> getrewardResponsibleList(Integer branchId) {
		LOGGER.info("INSIDE LIST_OF_REWARD_RESPONSIBLE API");
		Session session = getCurrentSession();
		List<RewardResponsibleSetup> list = new ArrayList<>();
		try {
			List<Object[]> rows = session.createNativeQuery(
					"select r.id,r.name,r.email_id,r.contact_no from reward_responsible_setup r where r.branch_id=:branchId and r.is_enable=1 order by r.id desc")
					.setParameter("branchId", branchId).getResultList();
			RewardResponsibleSetup rewardRes = null;
			for (Object[] row : rows) {
				rewardRes = new RewardResponsibleSetup();
				if (row[0] != null) {
					rewardRes.setId(Integer.parseInt(row[0].toString()));
				}
				if (row[1] != null) {
					rewardRes.setName(String.valueOf(row[1]));
				}
				if (row[2] != null) {
					rewardRes.setEmailId(String.valueOf(row[2]));
				}
				if (row[3] != null) {
					rewardRes.setContactNo(row[3].toString());
				}
				list.add(rewardRes);
			}
		} catch (Exception e) {
			LOGGER.error("#INSIDE EXCEPTION OCCURED IN LIST_OF_REWARD_RESPONSIBLE API" + e.getMessage());
		}
		return list;

	}

	@Override
	public boolean addRedeemStatus(RedeemStatus status) {
		LOGGER.info("INSIDE ADD_STATUS_API");
		Session session = getCurrentSession();
		boolean flag = false;
		try {
			Object obj = session.save(status);
			if (obj != null) {
				flag = true;
			}

		} catch (Exception e) {
			LOGGER.error("#INSIDE EXCEPTION OCCURED IN ADD_STATUS_API" + e.getMessage());
		}
		return flag;
	}

	@Override
	public boolean updateStatus(RedeemStatus status) {
		LOGGER.info("INSIDE UPDATE_STATUS");
		Session session = getCurrentSession();
		boolean flag = false;
		try {
			int stat = session.createNativeQuery("update redeem_status set status=:status where id=:id")
					.setParameter("status", status.getStatus()).setParameter("id", status.getId()).executeUpdate();
			if (stat > 0) {
				flag = true;
			}
		} catch (Exception e) {
			LOGGER.info("#INSIDE EXCEPTION OCCURED IN UPDATE_STATUS_API" + e.getMessage());
		}
		return flag;
	}

	@Override
	public List<RedeemStatus> redeemStatusList() {
		LOGGER.info("INSIDE LIST_OF_REDEEM_STATUS API");
		Session session = getCurrentSession();
		List<RedeemStatus> list = new ArrayList<>();
		try {
			List<Object[]> rows = session.createNativeQuery("select r.id, r.status from redeem_status r")
					.getResultList();
			RedeemStatus sts = null;
			for (Object[] row : rows) {
				sts = new RedeemStatus();
				if (row[0] != null) {
					sts.setId(Integer.parseInt(row[0].toString()));
				}
				if (row[0] != null) {
					sts.setStatus(String.valueOf(row[1]));
				}
				list.add(sts);
			}
		} catch (Exception e) {
			LOGGER.error("#INSIDE EXCEPTION OCCURED IN LIST_OF_REDEEM_STATUS API" + e.getMessage());
		}
		return list;
	}

	@Override
	public boolean updateRedeemFromCartRewards(RedeemCartRewards redeemfrmcart) {
		LOGGER.info("INSIDE LIST_OF_REDEEM_STATUS API");
		Session session = getCurrentSession();
		boolean flag = false;
		try {
			int stat = session.createNativeQuery("update redeem_cart_rewards set status_id=:status where id=:id")
					.setParameter("status", redeemfrmcart.getStatus().getId()).setParameter("id", redeemfrmcart.getId())
					.executeUpdate();
			if (stat > 0) {
				flag = true;
			}
		} catch (Exception e) {
			LOGGER.error("#INSIDE EXCEPTION OCCURED IN LIST_OF_REDEEM_STATUS API" + e.getMessage());
		}
		return flag;
	}

	@Override
	public List<RedeemCartRewards> getRedeemFromCartRewardList(Integer empId) {
		LOGGER.info("INSIDE LIST_OF_REDEEM_STATUS API");
		List<RedeemCartRewards> list = new ArrayList<>();
		Session session = getCurrentSession();
		try {
			List<Object[]> resultList = session
					.createNativeQuery("select r.description,rc.updated_date,rc.product_point,rc.quantity,s.status\r\n"
							+ " from\r\n" + " redeem_cart_rewards rc\r\n" + " inner join \r\n"
							+ " cart c on rc.cart_id=c.id\r\n" + " inner join \r\n"
							+ " rewards r on c.reward_id=r.id\r\n" + " inner join \r\n"
							+ " redeem_status s on rc.status_id=s.id\r\n" + " where  \r\n"
							+ " rc.emp_id=:empId group by rc.id order by rc.updated_date DESC")
					.setParameter("empId", empId).getResultList();
			for (Object[] obj : resultList) {
				RedeemCartRewards redeem = new RedeemCartRewards();
				if (obj[0] != null) {
					redeem.setDescription(String.valueOf(obj[0]));
				}
				if (obj[1] != null) {
					redeem.setUpdatedDate(CommonUtils.convertStringToTimestamp(String.valueOf(obj[1])));
				}
				if (obj[2] != null) {
					redeem.setProductPoint(Integer.parseInt(obj[2].toString()));
				}
				if (obj[3] != null) {
					redeem.setQuantity(Integer.parseInt(obj[3].toString()));
				}
				RedeemStatus s = new RedeemStatus();
				if (obj[4] != null) {
					s.setStatus(String.valueOf(obj[4]));
				}
				redeem.setStatus(s);
				list.add(redeem);
			}
		} catch (Exception e) {
			LOGGER.error("#INSIDE EXCEPTION OCCURED IN LIST_OF_REDEEM_STATUS API" + e.getMessage());
		}
		return list;
	}

	@Override
	public boolean updateCartDetails(Cart cart) {
		LOGGER.info("INSIDE UPDATE_CART_API");
		Session session = getCurrentSession();
		boolean flag = false;
		try {
			int status = session.createNativeQuery("update cart set quantity=:quantity where id=:id ")
					.setParameter("quantity", cart.getQuantity()).setParameter("id", cart.getId()).executeUpdate();

			if (status > 0) {
				flag = true;
			}

		} catch (Exception e) {
			LOGGER.error("Inside UPDATE_CART_API" + e.getMessage());
		}
		return flag;
	}

	@Override
	public List<SocialMediaMaster> getSocialMediaList() {

		LOGGER.info("# INSIDE LIST OF GET SOCIAL MEDIA LIST API ");
		List<SocialMediaMaster> list = null;
		Session session = getCurrentSession();
		try {

			list = session.createQuery("from SocialMediaMaster").getResultList();

		} catch (Exception e) {
			LOGGER.error("#INSIDE EXCEPTION OCCURED IN GET SOCIAL MEDIA LIST" + e.getMessage());
		}
		return list;
	}

	@Override
	public List<RewardCategoryMaster> getRewardCategoryList(Integer orgId) {
		LOGGER.info("# INSIDE LIST OF GET REWARD CATEGORY LIST API ");
		List<RewardCategoryMaster> list = new ArrayList<>();
		Session session = getCurrentSession();
		try {

			List<Object[]> catList = session.createNativeQuery(
					"select c.id,c.category_name,c.description from reward_category_master c where org_id=:orgId and c.is_enable=:isEnable")
					.setParameter("orgId", orgId).setParameter("isEnable", EnovationConstants.ONE).getResultList();
			for (Object[] objc : catList) {
				RewardCategoryMaster rewardCategory = new RewardCategoryMaster();
				if (objc[0] != null) {
					rewardCategory.setId(Integer.parseInt(objc[0].toString()));
				}
				if (objc[1] != null) {
					rewardCategory.setCategoryName(String.valueOf(objc[1]));
				}
				if (objc[2] != null) {
					rewardCategory.setDescription(String.valueOf(objc[2]));
				}
				list.add(rewardCategory);
			}
		} catch (Exception e) {
			LOGGER.error("#INSIDE EXCEPTION OCCURED IN UPDATE_CART_API" + e.getMessage());

		}
		return list;
	}

	@Override
	public List<ThresholdMaster> getThresholdList() {

		Session session = getCurrentSession();
		List<ThresholdMaster> list = null;
		try {
			list = session.createQuery("FROM ThresholdMaster").getResultList();

		} catch (Exception e) {
			LOGGER.error("#INSIDE EXCEPTION OCCURED IN GET Threshold LIST" + e.getMessage());
		}
		return list;
	}

	@Override
	public boolean updateMoodIndicator(MoodIndicator mo) {
		LOGGER.info("INSIDE UPDATEMOODINDICATOR");
		boolean flag = false;
		Session session = getCurrentSession();
		try {
			MoodIndicator updateObject = (MoodIndicator) session.get(MoodIndicator.class, mo.getId());
			if (mo.getRating() > 0) {
				updateObject.setRating(mo.getRating());
				updateObject.setCreatedDate(CommonUtils.currentDate());
			}
			flag = true;
		} catch (Exception e) {
			LOGGER.error("INSIDE UPDATEMOODINDICATOR API" + e.getMessage());
		}
		return flag;
	}

	@Override
	public RewardResponsibleSetup checkRewardResponsiblePerson(RewardResponsibleSetup rrsetup) {
		List<RewardResponsibleSetup> details = null;
		RewardResponsibleSetup response = null;
		Session session = getCurrentSession();
		try {
			// if(rrsetup.getEmailId()==null) {rrsetup.setEmailId("");}
			// if(rrsetup.getContactNo()==null) {rrsetup.setContactNo("0000000000");}
			details = session.createQuery("FROM RewardResponsibleSetup WHERE emailId=:email OR contactNo=:contactNo ")
					.setParameter("email", rrsetup.getEmailId()).setParameter("contactNo", rrsetup.getContactNo())
					.getResultList();
			if (details != null && details.size() > 0) {
				response = details.get(0);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return response;
	}

	public boolean addIncidentOfficer(IncidentOfficerDetails inc) {
		LOGGER.info("INSIDE ADD_INCIDENT_OFFICER");
		boolean flag = false;
		Session session = getCurrentSession();
		try {

			session.save(inc);
			flag = true;
		} catch (Exception e) {
			LOGGER.error("INSIDE ADD_INCIDENT_OFFICER API" + e.getMessage());
		}
		return flag;
	}

	@Override
	public boolean updateIncidentOfficer(IncidentOfficerDetails inc) {
		LOGGER.info("INSIDE UPDATE_INCIDENT_OFFICER");
		Session session = getCurrentSession();
		boolean flag = false;
		try {
			int stat = session.createNativeQuery(
					"update incident_officer_details set emp_id=:empId ,manager_type_id=:managerId where id=:id")
					.setParameter("empId", inc.getEmp().getEmpId())
					.setParameter("managerId", inc.getIncidentManagerType().getId()).setParameter("id", inc.getId())
					.executeUpdate();
			if (stat > 0) {
				flag = true;
			}
		} catch (Exception e) {
			LOGGER.info("#INSIDE EXCEPTION OCCURED IN UPDATE_INCIDENT_OFFICER" + e.getMessage());
		}
		return flag;
	}

	@Override
	public List<IncidentOfficerDetails> getIncidentOfficerDetails(Integer branchId) {
		LOGGER.info("# INSIDE LIST OF GET INCIDENT OFFICER DETAILS API ");
		List<IncidentOfficerDetails> list = new ArrayList<>();
		Session session = getCurrentSession();
		try {

			List<Object[]> catList = session.createNativeQuery(
					"select i.ince_id,e.emp_id,e.first_name,e.last_name,e.contact_no,e.designation,e.email_id,d.dept_name,m.id as manager_id,m.manager_type \r\n"
							+ "from \r\n" + "incident_officer_details i\r\n" + "inner join \r\n"
							+ "tbl_employee_details e\r\n" + "on i.emp_id=e.emp_id\r\n" + "inner join\r\n"
							+ "master_department d\r\n" + "on \r\n" + "e.dept_id=d.dept_id\r\n" + "inner join \r\n"
							+ "incident_manager_type m\r\n" + "on\r\n" + "i.manager_type_id=m.id\r\n" + "where\r\n"
							+ "e.branch_id=:branchId")
					.setParameter("branchId", branchId).getResultList();
			for (Object[] objc : catList) {
				IncidentOfficerDetails incidentOfficer = new IncidentOfficerDetails();
				if (objc[0] != null) {
					incidentOfficer.setId(Integer.parseInt(objc[0].toString()));
				}
				EmployeeDetails emp = new EmployeeDetails();
				if (objc[1] != null) {
					emp.setEmpId(Integer.parseInt(objc[1].toString()));
				}
				if (objc[2] != null) {
					emp.setFirstName(String.valueOf(objc[2]));
				}
				if (objc[3] != null) {
					emp.setLastName(String.valueOf(objc[3]));
				}
				if (objc[4] != null) {
					emp.setContactNo(String.valueOf(objc[4]));
				}
				if (objc[5] != null) {
					emp.setDesignation(String.valueOf(objc[5]));
				}
				if (objc[6] != null) {
					emp.setEmailId(String.valueOf(objc[6]));
				}
				incidentOfficer.setEmp(emp);
				if (objc[7] != null) {
					incidentOfficer.setDeptName(String.valueOf(objc[7]));
				}
				IncidentManagerType imt = new IncidentManagerType();
				if (objc[8] != null) {
					imt.setId(Integer.parseInt(objc[8].toString()));
				}
				if (objc[9] != null) {
					imt.setManagerType(String.valueOf(objc[9]));
				}
				incidentOfficer.setIncidentManagerType(imt);
				list.add(incidentOfficer);
			}
		} catch (Exception e) {
			LOGGER.error("#INSIDE EXCEPTION OCCURED GET INCIDENT OFFICER DETAILS" + e.getMessage());
		}
		return list;
	}

	@Override
	public boolean addMachineDetails(MachineDetails mach) {
		LOGGER.info("INSIDE ADD_MACHINE_DETAILS");
		boolean flag = false;
		Session session = getCurrentSession();
		String picpathcut = null;
		try {
			mach.setIsEnable(EnovationConstants.ONE);
			if (mach.getMachineImg() != null) {
				String docDirctory = EnovationConfig.buddyConfig.get(EnovationConstants.SugesstionImgPath) + "/"
						+ EnovationConstants.MACHINE_FOLDER_NAME + mach.getBranch().getBranchId();
				String filePathToTrim = EnovationConstants.MACHINE_FOLDER_NAME + mach.getBranch().getBranchId();
				picpathcut = WriteFilesUtils.generateFileNameNDwriteFile(mach.getMachineImg(), docDirctory,
						filePathToTrim);
			}
			if (picpathcut != null)
				mach.setImgUrl(picpathcut);
			session.save(mach);
			flag = true;
		} catch (Exception e) {
			LOGGER.error("INSIDE ADD_MACHINE_DETAILS API" + e.getMessage());
		}
		return flag;
	}

	@Override
	public boolean updateMachineDetails(MachineDetails mach) {
		LOGGER.info("INSIDE UPDATE_MACHINE_DETAILS");
		Session session = getCurrentSession();
		boolean flag = false;
		try {
			MachineDetails detUpdate = (MachineDetails) session.get(MachineDetails.class, mach.getMachId());
			if (mach.getMachName() != null)
				detUpdate.setMachName(mach.getMachName());
			if (mach.getMachNumber() != null)
				detUpdate.setMachNumber(mach.getMachNumber());
			if (mach.getLine() != null)
				detUpdate.setLine(mach.getLine());
			String picpathcut = null;
			if (mach.getMachineImg() != null) {
				String docDirctory = EnovationConfig.buddyConfig.get(EnovationConstants.SugesstionImgPath) + "/"
						+ EnovationConstants.MACHINE_FOLDER_NAME + mach.getBranch().getBranchId();
				String filePathToTrim = EnovationConstants.MACHINE_FOLDER_NAME + mach.getBranch().getBranchId();
				picpathcut = WriteFilesUtils.generateFileNameNDwriteFile(mach.getMachineImg(), docDirctory,
						filePathToTrim);
			}
			if (mach.getMachineImg() != null)
				detUpdate.setImgUrl(picpathcut);
			session.update(detUpdate);
			flag = true;
		} catch (Exception e) {
			LOGGER.info("#INSIDE EXCEPTION OCCURED IN UPDATE_MACHINE_DETAILS" + e.getMessage());
		}
		return flag;
	}

	@Override
	public boolean deleteMachineDetails(MachineDetails mach) {
		LOGGER.info("INSIDE DELETE_MACHINE_DETAILS");
		Session session = getCurrentSession();
		boolean flag = false;
		try {

			int status = session
					.createNativeQuery("update master_machine_details set is_enable=:isEnable where mach_id=:ID")
					.setParameter("ID", mach.getMachId()).setParameter("isEnable", mach.getIsEnable()).executeUpdate();
			if (status > 0)
				flag = true;
		} catch (Exception e) {
			e.printStackTrace();
			LOGGER.info("#INSIDE EXCEPTION OCCURED IN DELETE_MACHINE_DETAILS" + e.getMessage());
		}
		return flag;
	}

	@Override
	public List<IncidentManagerType> getManagerTypeList() {
		LOGGER.info("# INSIDE LIST OF GET MANAGER TYPE LIST API ");
		List<IncidentManagerType> list = new ArrayList<>();
		Session session = getCurrentSession();
		try {
			List<Object[]> managerList = session
					.createNativeQuery("select m.id,m.manager_type from incident_manager_type m").getResultList();
			for (Object[] obj : managerList) {
				IncidentManagerType imt = new IncidentManagerType();
				if (obj[0] != null) {
					imt.setId(Integer.parseInt(obj[0].toString()));
				}
				if (obj[1] != null) {
					imt.setManagerType(String.valueOf(obj[1]));
				}
				list.add(imt);
			}
		} catch (Exception e) {
			LOGGER.info("#INSIDE EXCEPTION OCCURED IN GET MANAGER TYPE LIST" + e.getMessage());
		}
		return list;
	}

	/*
	 * Updated By AKSHAY 23 May 2019
	 */
	@Override
	public List<MachineDetails> getMachineList(Integer branchId) {
		LOGGER.info("# INSIDE LIST OF GET MACHINE DETAILS LIST API ");
		List<MachineDetails> list = new ArrayList<>();
		Session session = getCurrentSession();
		try {
			List<Object[]> machineList = session.createNativeQuery(
					"SELECT m.mach_id,m.mach_name,m.mach_number,m.branch_id,m.dept_id,d.dept_name,CONCAT(m.mach_name,'-',m.mach_number) as machine_name,is_enable, img_url ,dl.name as lineName, dl.id as machinelineId\r\n"
							+ "from master_machine_details m \r\n"
							+ "inner join master_department d on m.dept_id=d.dept_id \r\n"
							+ "LEFT JOIN dwm_line dl ON dl.id=m.line_id\r\n"
							+ "where m.branch_id=:branchId and is_enable=:isEnable")
					.setParameter("branchId", branchId).setParameter("isEnable", EnovationConstants.ONE)
					.getResultList();
			machineList.stream().forEach(obj -> {
				list.add(new MachineDetails((obj[0] != null) ? Integer.parseInt(obj[0].toString()) : 0,
						(obj[2] != null) ? String.valueOf(obj[2]) : null,
						(obj[1] != null) ? String.valueOf(obj[1]) : null,
						(obj[3] != null) ? new BranchMaster(Integer.parseInt(obj[3].toString())) : null,
						(obj[4] != null)
								? new DepartmentMaster(Integer.parseInt(obj[4].toString()),
										(obj[5] != null) ? obj[5].toString() : null)
								: null,
						(obj[6] != null) ? String.valueOf(obj[6]) : null,
						(obj[7] != null) ? Integer.valueOf(obj[7].toString()) : 0,
						(obj[8] != null) ? obj[8].toString() : null, (obj[9] != null) ? obj[9].toString() : null,
						(obj[10] != null) ? Long.valueOf(obj[10].toString()) : 0));
			});
		} catch (Exception e) {
			LOGGER.info("#INSIDE EXCEPTION OCCURED IN GET MACHINE LIST" + e.getMessage());
		}
		return list;
	}

	/* MASTER SETUP LISTS */
	@Override
	public List<IncidentNature> getIncidentNatureList() {
		LOGGER.info("# INSIDE LIST OF INCIDENT NATURE TYPE LIST API ");
		List<IncidentNature> list = new ArrayList<>();
		Session session = getCurrentSession();
		try {
			List<Object[]> managerList = session
					.createNativeQuery("select m.id,m.nature_type from master_incident_nature m").getResultList();
			for (Object[] obj : managerList) {
				IncidentNature imt = new IncidentNature();
				if (obj[0] != null) {
					imt.setId(Integer.parseInt(obj[0].toString()));
				}
				if (obj[1] != null) {
					imt.setNatureType(String.valueOf(obj[1]));
				}
				list.add(imt);
			}
		} catch (Exception e) {
			LOGGER.info("#INSIDE EXCEPTION OCCURED IN GET INCIDENT NATURE TYPE LIST" + e.getMessage());
		}
		return list;
	}

	@Override
	public List<IncidentPriority> getIncidentPriorityList() {
		LOGGER.info("# INSIDE LIST OF INCIDENT PRIORITY TYPE LIST API ");
		List<IncidentPriority> list = new ArrayList<>();
		Session session = getCurrentSession();
		try {
			List<Object[]> managerList = session
					.createNativeQuery("select m.id,m.priority from master_incident_priority m").getResultList();
			for (Object[] obj : managerList) {
				IncidentPriority imt = new IncidentPriority();
				if (obj[0] != null) {
					imt.setId(Integer.parseInt(obj[0].toString()));
				}
				if (obj[1] != null) {
					imt.setPriority(String.valueOf(obj[1]));
				}
				list.add(imt);
			}
		} catch (Exception e) {
			LOGGER.info("#INSIDE EXCEPTION OCCURED IN GET INCIDENT PRIORITY TYPE LIST" + e.getMessage());
		}
		return list;
	}

	@Override
	public List<IncidentReport> getIncidentReportList() {
		LOGGER.info("# INSIDE LIST OF INCIDENT REPORT TYPE LIST API ");
		List<IncidentReport> list = new ArrayList<>();
		Session session = getCurrentSession();
		try {
			List<Object[]> managerList = session
					.createNativeQuery("select m.id,m.report_type from master_incident_report m").getResultList();
			for (Object[] obj : managerList) {
				IncidentReport imt = new IncidentReport();
				if (obj[0] != null) {
					imt.setId(Integer.parseInt(obj[0].toString()));
				}
				if (obj[1] != null) {
					imt.setReportType(String.valueOf(obj[1]));
				}
				list.add(imt);
			}
		} catch (Exception e) {
			LOGGER.info("#INSIDE EXCEPTION OCCURED IN GET INCIDENT REPORT TYPE LIST" + e.getMessage());
		}
		return list;
	}

	@Override
	public List<IncidentSeverity> getIncidentSaverityList() {
		LOGGER.info("# INSIDE LIST OF INCIDENT SEVERITY TYPE LIST API ");
		List<IncidentSeverity> list = new ArrayList<>();
		Session session = getCurrentSession();
		try {
			List<Object[]> managerList = session
					.createNativeQuery("select m.id,m.severity_type from master_incident_severity m").getResultList();
			for (Object[] obj : managerList) {
				IncidentSeverity imt = new IncidentSeverity();
				if (obj[0] != null) {
					imt.setId(Integer.parseInt(obj[0].toString()));
				}
				if (obj[1] != null) {
					imt.setSeverityType(String.valueOf(obj[1]));
				}
				list.add(imt);
			}
		} catch (Exception e) {
			LOGGER.info("#INSIDE EXCEPTION OCCURED IN GET INCIDENT SEVERITY TYPE LIST" + e.getMessage());
		}
		return list;
	}

	@Override
	public boolean isSafetyOfficerExist(IncidentOfficerDetails inc) {
		boolean flag = false;
		Session session = getCurrentSession();
		List<Object[]> incidentOfficerList = new ArrayList<>();
		try {
			if (inc.getIncidentManagerType().getId() == EnovationConstants.ONE) {
				incidentOfficerList = session
						.createNativeQuery("SELECT io.*\r\n" + "FROM incident_officer_details io \r\n"
								+ "INNER JOIN tbl_employee_details ed on ed.emp_id = io.emp_id\r\n"
								+ "WHERE io.manager_type_id=:managerId AND ed.branch_id=:branchId")
						.setParameter("managerId", inc.getIncidentManagerType().getId())
						.setParameter("branchId", inc.getBranchId()).getResultList();
				flag = ((!incidentOfficerList.isEmpty()) ? true : false);
			} else if (inc.getIncidentManagerType().getId() == EnovationConstants.TWO) {

				incidentOfficerList = session.createNativeQuery(
						"SELECT * FROM incident_officer_details WHERE manager_type_id=:manageId and emp_id=:empId")
						.setParameter("manageId", inc.getIncidentManagerType().getId())
						.setParameter("emp_id", inc.getEmp().getEmpId()).getResultList();
				flag = ((!incidentOfficerList.isEmpty()) ? true : false);
			}

		} catch (Exception e) {
			LOGGER.error("#EXCEPTION INSIDE IS SAFETY OFFICER EXIST" + e.getMessage());
		}
		return flag;
	}

	@Override
	public List<IncidentOfficerDetails> getIncidentManagerList(Integer branchId) {
		LOGGER.info("# INSIDE LIST OF GET INCIDENT MANGER LIST API ");
		List<IncidentOfficerDetails> list = new ArrayList<>();
		Session session = getCurrentSession();
		try {

			List<Object[]> catList = session.createNativeQuery(
					"select i.ince_id,e.emp_id,e.first_name,e.last_name,e.contact_no,e.designation,e.email_id,d.dept_name,m.id as manager_id,m.manager_type \r\n"
							+ "from\r\n" + "incident_officer_details i\r\n" + "inner join \r\n"
							+ "tbl_employee_details e\r\n" + "on i.emp_id=e.emp_id\r\n" + "inner join\r\n"
							+ "master_department d\r\n" + "on \r\n" + "e.dept_id=d.dept_id\r\n" + "inner join \r\n"
							+ "incident_manager_type m\r\n" + "on\r\n" + "i.manager_type_id=m.id\r\n" + "where\r\n"
							+ "i.manager_type_id=2 and e.branch_id=:branchId")
					.setParameter("branchId", branchId).getResultList();
			for (Object[] objc : catList) {
				IncidentOfficerDetails incidentOfficer = new IncidentOfficerDetails();
				if (objc[0] != null) {
					incidentOfficer.setId(Integer.parseInt(objc[0].toString()));
				}
				EmployeeDetails emp = new EmployeeDetails();
				if (objc[1] != null) {
					emp.setEmpId(Integer.parseInt(objc[1].toString()));
				}
				if (objc[2] != null) {
					emp.setFirstName(String.valueOf(objc[2]));
				}
				if (objc[3] != null) {
					emp.setLastName(String.valueOf(objc[3]));
				}
				if (objc[4] != null) {
					emp.setContactNo(String.valueOf(objc[4]));
				}
				if (objc[5] != null) {
					emp.setDesignation(String.valueOf(objc[5]));
				}
				if (objc[6] != null) {
					emp.setEmailId(String.valueOf(objc[6]));
				}
				incidentOfficer.setEmp(emp);
				if (objc[7] != null) {
					incidentOfficer.setDeptName(String.valueOf(objc[7]));
				}
				IncidentManagerType imt = new IncidentManagerType();
				if (objc[8] != null) {
					imt.setId(Integer.parseInt(objc[8].toString()));
				}
				if (objc[9] != null) {
					imt.setManagerType(String.valueOf(objc[9]));
				}
				incidentOfficer.setIncidentManagerType(imt);
				list.add(incidentOfficer);
			}
		} catch (Exception e) {
			LOGGER.error("#INSIDE EXCEPTION OCCURED GET INCIDENT MANGER LIST" + e.getMessage());
		}
		return list;
	}

	@Override
	public List<SavingOptions> getSavingOptionsList() {
		List<SavingOptions> list = new ArrayList<>();
		try {
			LOGGER.info("#INSIDE IN GETSAVINGOPTIONSLIST ");
			list = getCurrentSession().createNativeQuery("SELECT c.* from saving_options c WHERE  c.branch_id is null")
					.addEntity(SavingOptions.class).getResultList();
		} catch (Exception e) {
			LOGGER.info("#INSIDE Exception GETSAVINGOPTIONSLIST " + e.getMessage());
		}
		return list;
	}

	@Override
	public boolean isIncidentManagerExist(IncidentOfficerDetails inc) {
		boolean flag = false;
		Session session = getCurrentSession();
		System.out.println("Inside Incident Officer Details");
		List<Object[]> incidentManagerList = new ArrayList<>();
		try {
			incidentManagerList = session.createNativeQuery(
					"select emp_id,manager_type_id from incident_officer_details where emp_id=:empId and manager_type_id=2;")
					.setParameter("empId", inc.getEmp().getEmpId()).getResultList();
			flag = ((!incidentManagerList.isEmpty()) ? true : false);

		} catch (Exception e) {
			e.printStackTrace();
			LOGGER.info("#EXCEPTION INSIDE IS INCIDENT MANAGER EXIST" + e.getMessage());
		}
		return flag;
	}

	@Override
	public boolean isMachineExist(MachineDetails mach) {
		boolean flag = false;
		Session session = getCurrentSession();
		System.out.println("Inside Machine Details");
		List<Object[]> pendingMachine = new ArrayList<>();
		try {
			pendingMachine = session.createNativeQuery(
					"select i.ince_id,ias.sts_id, ias.action_status,ias.emp_id,i.emp_id as emp,i.mach_id\r\n"
							+ "from incident_details i\r\n" + "inner join \r\n" + "incident_audit_status ias \r\n"
							+ "on i.ince_id=ias.ince_id\r\n" + "and ias.action_status IN ('PENDING')\r\n"
							+ "where i.mach_id=:machId group by ias.ince_id ")
					.setParameter("machId", mach.getMachId()).getResultList();
			flag = ((!pendingMachine.isEmpty()) ? true : false);

		} catch (Exception e) {
			e.printStackTrace();
			LOGGER.info("#EXCEPTION INSIDE IS MACHINE ACTION PENDING EXIST" + e.getMessage());
		}
		return flag;
	}

	@Override
	public List<Line> getLineNameList(Integer deptId) {
		List<Line> list = new ArrayList<>();
		List<Object[]> objList = new ArrayList<>();
		objList = getCurrentSession()
				.createNativeQuery("select id,name,dept_id,is_active from dwm_line where dept_id=:deptId")
				.setParameter("deptId", deptId).getResultList();
		if (!objList.isEmpty()) {
			objList.stream().forEach(x -> {
				Line line = new Line((x[0] != null) ? Long.valueOf(x[0].toString()) : 0,
						(x[1] != null) ? x[1].toString() : null, (x[2] != null) ? Integer.valueOf(x[2].toString()) : 0,
						(x[3] != null) ? x[3].toString() : null);
				list.add(line);
			});
		}

		return list;
	}

	@Override
	public List<Line> checkLineNameExists(Line lineInput) {
		List<Line> list = new ArrayList<>();
		List<Object[]> objList = new ArrayList<>();
		objList = getCurrentSession()
				.createNativeQuery("select id,name,dept_id from dwm_line where dept_id=:deptId AND name=:name")
				.setParameter("deptId", lineInput.getDept().getDeptId()).setParameter("name", lineInput.getName())
				.getResultList();
		if (!objList.isEmpty()) {
			objList.stream().forEach(x -> {
				Line line = new Line((x[0] != null) ? Long.valueOf(x[0].toString()) : 0,
						(x[1] != null) ? x[1].toString() : null, (x[2] != null) ? Integer.valueOf(x[2].toString()) : 0);
				list.add(line);
			});
		}
		return list;
	}

	@Override
	@Transactional
	public boolean addLineName(Line line) {
		line.setIsActive("Y");
		return (getCurrentSession().save(line)) != null ? true : false;
	}

	@Override
	public boolean updateLineName(Line line) {
		Line lineUpdate = getCurrentSession().find(Line.class, line.getId());
		if (line.getName() != null) {
			lineUpdate.setName(line.getName());
		}
		return (getCurrentSession().merge(lineUpdate)) != null;
	}

	@Override
	public List<Section> getSectionList(Integer branchId) {
		List<Section> list = new ArrayList<>();
		try {
			list = getCurrentSession().createQuery("FROM Section WHERE branch_id=:branchId")
					.setParameter("branchId", branchId).getResultList();
		} catch (Exception e) {
			e.printStackTrace();
			LOGGER.info("#EXCEPTION INSIDE GET SECTION API " + e.getMessage());
		}
		return list;
	}

	@Override
	public List<Shifts> getShiftList(Shifts request) {
		List<Shifts> list = new ArrayList<>();
		try {
			list = getCurrentSession().createQuery("FROM Shifts").getResultList();

		} catch (Exception e) {
			e.printStackTrace();
			LOGGER.info("#EXCEPTION INSIDE GET SHIFTS API " + e.getMessage());
		}
		return list;
	}

	@Override
	public List<MasterZone> getZoneList(Integer branchId) {
		LOGGER.info("## INSIDE GET ZONE LIST ");
		List<MasterZone> list = new ArrayList<>();
		try {
			list = getCurrentSession().createQuery("FROM MasterZone WHERE branch_id=:branchId")
					.setParameter("branchId", branchId).getResultList();
		} catch (Exception e) {
			e.printStackTrace();
			LOGGER.info("#EXCEPTION INSIDE GET ZONE API " + e.getMessage());
		}
		return list;
	}

	@Override
	public int addOrUpdateZone(MasterZone zone) {
		LOGGER.info("## INSIDE ADD OR UPDATE ZONE API ");
		int flag = 0;
		try {
			if (zone.getId() > 0) {
				getCurrentSession().update(zone);
				flag = 1;
			} else {
				getCurrentSession().save(zone);
				flag = 2;
			}
		} catch (Exception e) {
			e.printStackTrace();
			LOGGER.info("#EXCEPTION INSIDE ADD ZONE API " + e.getMessage());
		}
		return flag;
	}

	@Override
	public int addOrUpdateSection(Section section) {
		int flag = 0;
		LOGGER.info("#  INSIDE ADD OR UPDATE SECTION API  ");
		try {
			if (section.getId() > 0) {
				getCurrentSession().update(section);
				flag = 1;
			} else {
				getCurrentSession().save(section);
				flag = 2;
			}
		} catch (Exception e) {
			e.printStackTrace();
			LOGGER.info("#EXCEPTION INSIDE ADD SECTION API " + e.getMessage());
		}
		return flag;
	}

	public List<CategoryMaster> getCategoryListByBranchId(Integer branchId) {
		return getCurrentSession().createQuery("FROM CategoryMaster WHERE branch.branchId=:branchId")
				.setParameter("branchId", branchId).getResultList();
	}

	@Override
	public boolean addMachineParts(MachineParts parts) {
		boolean flag = false;
		Session session = getCurrentSession();
		Gson g = new Gson();
		System.out.println("MACHINE PART DATA :" + g.toJson(parts));
		try {
			String picpathcut = null;
			if (parts.getMachImg() != null) {
				String docDirctory = EnovationConfig.buddyConfig.get(EnovationConstants.SugesstionImgPath) + "/"
						+ EnovationConstants.MACHINE_PART_FOLDER_NAME + parts.getPartName().trim();
				String filePathToTrim = EnovationConstants.MACHINE_PART_FOLDER_NAME + parts.getPartName().trim();
				picpathcut = WriteFilesUtils.writeFileOnServer(parts.getMachImg(), docDirctory, filePathToTrim);
			}
			if (picpathcut != null)
				parts.setImgUrl(picpathcut);
			session.saveOrUpdate(parts);
			flag = true;
		} catch (Exception e) {
			e.printStackTrace();
			LOGGER.info("#EXCEPTION INSIDE ADDMACHINEPARTS API " + e.getMessage());
		}
		return flag;
	}

	@Override
	public List<Object[]> getMachinePartsList(Integer machId, Integer branchId) {
		StringBuffer query = new StringBuffer(
				"SELECT p.id, p.part_number, p.part_name, p.created_date, p.img_url,l.name,l.id as lineId,d.dept_name,mm.dept_id,mm.mach_id,mm.mach_name,mm.mach_number from machine_parts p INNER JOIN master_machine_details mm ON mm.mach_id = p.machine_id "
						+ "inner join dwm_line l on l.id=mm.line_id inner join master_department d on d.dept_id=mm.dept_id where ");
		if (machId > 0) {
			query.append(" p.machine_id={machId}");
		} else {
			query.append(" mm.branch_id={branchId}");
		}
		return getCurrentSession()
				.createNativeQuery((query.toString()).replaceAll(Pattern.quote("{machId}"), String.valueOf(machId))
						.replaceAll(Pattern.quote("{branchId}"), String.valueOf(branchId)))
				.getResultList();
	}

	@Override
	public boolean updateMachineParts(MachineParts parts) {
		boolean flag = false;
		Session session = getCurrentSession();
		try {
			MachineParts partsUpdate = (MachineParts) session.get(MachineParts.class, parts.getId());
			if (parts.getPartName() != null)
				partsUpdate.setPartName(parts.getPartName());
			if (parts.getPartNumber() != null)
				partsUpdate.setPartNumber(parts.getPartNumber());
			String picpathcut = null;
			if (parts.getMachImg() != null) {
				String docDirctory = EnovationConfig.buddyConfig.get(EnovationConstants.SugesstionImgPath) + "/"
						+ EnovationConstants.MACHINE_PART_FOLDER_NAME + parts.getPartName().trim();
				String filePathToTrim = EnovationConstants.MACHINE_PART_FOLDER_NAME + parts.getPartName().trim();
				picpathcut = WriteFilesUtils.writeFileOnServer(parts.getMachImg(), docDirctory, filePathToTrim);
			}
			if (parts.getMachImg() != null)
				partsUpdate.setImgUrl(picpathcut);
			session.update(partsUpdate);
			flag = true;
		} catch (Exception e) {
			e.printStackTrace();
			LOGGER.info("#EXCEPTION INSIDE UPDATEMACHINEPARTS API " + e.getMessage());
		}
		return flag;
	}

	@Override
	public int deleteMachineParts(Long id) {
		LOGGER.info("##INSIDE GET DELETE AUDIT CONFIG DETAILS " + id);
		Session session = getCurrentSession();
		try {
			session.delete((MachineParts) session.get(MachineParts.class, id));
			return 1;
		} catch (Exception e) {
			return 2;
		}
	}

	@Override
	public List<MachineDetails> getMachineListByLine(MachineDetails macDet) {
		LOGGER.info("# INSIDE LIST OF GET MACHINE LIST BY LINE API ");
		List<MachineDetails> list = new ArrayList<>();
		Session session = getCurrentSession();
		try {
			List<Object[]> managerList = session.createNativeQuery("SELECT \r\n"
					+ "    m.mach_id,m.mach_name,m.mach_number,m.branch_id,m.dept_id,d.dept_name,\r\n"
					+ "    CONCAT(m.mach_name, '-', m.mach_number) AS machine_name,is_enable,img_url,dl.name AS lineName,dl.id AS machinelineId\r\n"
					+ "FROM\r\n" + "    master_machine_details m,\r\n" + "    master_department d,\r\n"
					+ "    dwm_line dl\r\n" + "WHERE\r\n" + "    m.dept_id = d.dept_id\r\n"
					+ "	AND dl.id = m.line_id\r\n" + "	AND m.dept_id =:deptId\r\n" + "	AND m.line_id =:lineId\r\n"
					+ "	AND m.is_enable =:isEnable").setParameter("deptId", macDet.getDeptId())
					.setParameter("lineId", macDet.getLineId()).setParameter("isEnable", EnovationConstants.ONE)
					.getResultList();
			managerList.stream().forEach(obj -> {
				list.add(new MachineDetails((obj[0] != null) ? Integer.parseInt(obj[0].toString()) : 0,
						(obj[2] != null) ? String.valueOf(obj[2]) : null,
						(obj[1] != null) ? String.valueOf(obj[1]) : null,
						(obj[3] != null) ? new BranchMaster(Integer.parseInt(obj[3].toString())) : null,
						(obj[4] != null)
								? new DepartmentMaster(Integer.parseInt(obj[4].toString()),
										(obj[5] != null) ? obj[5].toString() : null)
								: null,
						(obj[6] != null) ? String.valueOf(obj[6]) : null,
						(obj[7] != null) ? Integer.valueOf(obj[7].toString()) : 0,
						(obj[8] != null) ? obj[8].toString() : null, (obj[9] != null) ? obj[9].toString() : null,
						(obj[10] != null) ? Long.valueOf(obj[10].toString()) : 0));
			});
		} catch (Exception e) {
			LOGGER.info("#INSIDE EXCEPTION OCCURED IN GET MACHINE LIST" + e.getMessage());
		}
		return list;
	}

	@Override
	public boolean saveSugOnHoldSetup(SuggestionOnHoldSetup onHold) {
		LOGGER.info("# INSIDE SAVE SUGGESTION ON HOLD SETUP API ");
		Session session = getCurrentSession();
		boolean flag = false;
		try {
			session.save(onHold);
			flag = true;

		} catch (Exception e) {
			LOGGER.info("#INSIDE EXCEPTION OCCURED IN SAVE SUGGESTION ON HOLD SETUP API" + e.getMessage());
		}
		return flag;
	}

	@Override
	public boolean deleteSugOnHoldSetup(SuggestionOnHoldSetup onHold) {
		LOGGER.info("# INSIDE DELETE SUGGESTION ON HOLD SETUP API ");
		Session session = getCurrentSession();
		boolean flag = false;
		try {
			session.createNativeQuery("delete from suggestion_on_hold_setup where id=:Id")
					.setParameter("Id", onHold.getId()).executeUpdate();
			flag = true;

		} catch (Exception e) {
			LOGGER.info("#INSIDE EXCEPTION OCCURED IN DELETE SUGGESTION ON HOLD SETUP API" + e.getMessage());
		}
		return flag;
	}
	
	@Override
	public boolean isOnHoldPendingActionsExists(SuggestionOnHoldSetup onHold) {
		boolean flag = false;
		Session session = getCurrentSession();
		System.out.println("Inside On Hold Implementor Pending Actions Exists");
		List<Object[]> pendingActionList = new ArrayList<>();
		try {
			pendingActionList = session.createNativeQuery(
					"select * from sugesstion_status_audit where sts_id=:stsId and emp_id=:empId and action_status=:pendigAction")
					.setParameter("stsId", EnovationConstants.ON_HOLD_SUGGESTIONS_ID)
					.setParameter("empId", onHold.getEmp().getEmpId())
					.setParameter("pendigAction", EnovationConstants.PENDING_STRING).getResultList();
			flag = ((!pendingActionList.isEmpty()) ? true : false);

		} catch (Exception e) {
			e.printStackTrace();
			LOGGER.info("#EXCEPTION INSIDE IS ON HOLD IMPLEMENTOR PENDING ACTIONS EXIST" + e.getMessage());
		}
		return flag;
	}

	@Override
	public boolean savePasswordPolicy(PasswordPolicy policy) {
		LOGGER.info("# INSIDE SAVE PASSWORD POLICY SETUP API ");
		Session session = getCurrentSession();
		boolean flag = false;
		try {
			session.save(policy);
			flag = true;
		} catch (Exception e) {
			LOGGER.info("#INSIDE EXCEPTION OCCURED IN SAVE PASSWORD POLICY SETUP API" + e.getMessage());
		}
		return flag;
	}

	@Override
	public boolean updatePasswordPolicy(PasswordPolicy policy) {
		LOGGER.info("# INSIDE UPDATE PASSWORD POLICY SETUP API ");
		boolean flag = false;
		Session session = getCurrentSession();
		try {
			PasswordPolicy paswdPolicy = (PasswordPolicy) session.get(PasswordPolicy.class, policy.getId());
			if (policy.getCheckLastPasswd() != 0 || policy.getCheckLastPasswd() <= 0)
				paswdPolicy.setCheckLastPasswd(policy.getCheckLastPasswd());
			if (policy.getMaxLifeTimeInDays() != 0 || policy.getMaxLifeTimeInDays() <= 0)
				paswdPolicy.setMaxLifeTimeInDays(policy.getMaxLifeTimeInDays());
			if (policy.getMinAlphabetic() != 0 || policy.getMinAlphabetic() <= 0)
				paswdPolicy.setMinAlphabetic(policy.getMinAlphabetic());
			if (policy.getMinNumeric() != 0 || policy.getMinNumeric() <= 0)
				paswdPolicy.setMinNumeric(policy.getMinNumeric());
			if (policy.getMinPasswdLength() != 0 || policy.getMinPasswdLength() <= 0)
				paswdPolicy.setMinPasswdLength(policy.getMinPasswdLength());
			if (policy.getMinSpecialChar() != 0 || policy.getMinSpecialChar() <= 0)
				paswdPolicy.setMinSpecialChar(policy.getMinSpecialChar());
			if (policy.getPolicyType() != null)
				paswdPolicy.setPolicyType(policy.getPolicyType());
			session.update(paswdPolicy);
			flag = true;
		} catch (Exception e) {
			e.printStackTrace();
			LOGGER.info("#EXCEPTION INSIDE UPDATE PASSWORD POLICY API " + e.getMessage());
		}
		return flag;
	}

	@Override
	public PasswordPolicy getPasswordPolicyByOrgId(int orgId) {
		LOGGER.info("# INSIDE LIST OF PASSWORD POLICY API ");
		List<PasswordPolicy> list = new ArrayList<>();
		Session session = getCurrentSession();
		try {
			List<Object[]> passwordPolicyList = session.createNativeQuery("SELECT \r\n" + "    p.id,\r\n"
					+ "    p.check_last_passwd,\r\n" + "    p.max_life_time_in_days,\r\n" + "    p.min_alphabetic,\r\n"
					+ "    p.min_numeric,\r\n" + "    p.min_passwd_length,\r\n" + "    p.min_special_char,\r\n"
					+ "    p.org_id,\r\n" + "    p.policy_type,\r\n" + "    o.name,p.is_enable\r\n" + "FROM\r\n"
					+ "    password_policy p\r\n" + "        INNER JOIN\r\n"
					+ "    master_organization o ON o.org_id = p.org_id\r\n" + "WHERE\r\n" + "    p.org_id =:orgId")
					.setParameter("orgId", orgId).getResultList();

			passwordPolicyList.stream().forEach(x -> {
				PasswordPolicy adt = new PasswordPolicy((x[0] != null) ? Integer.parseInt(String.valueOf(x[0])) : 0,
						(x[1] != null) ? Integer.parseInt(String.valueOf(x[1])) : 0,
						(x[2] != null) ? Integer.parseInt(String.valueOf(x[2])) : 0,
						(x[3] != null) ? Integer.parseInt(String.valueOf(x[3])) : 0,
						(x[4] != null) ? Integer.parseInt(String.valueOf(x[4])) : 0,
						(x[5] != null) ? Integer.parseInt(String.valueOf(x[5])) : 0,
						(x[6] != null) ? Integer.parseInt(String.valueOf(x[6])) : 0,
						(x[0] != null) ? Integer.parseInt(String.valueOf(x[7])) : 0,
						(x[8] != null) ? String.valueOf(x[8]) : null, (x[9] != null) ? String.valueOf(x[9]) : null,
						(x[10] != null) ? String.valueOf(x[10]) : null);
				list.add(adt);
			});

		} catch (Exception e) {
			LOGGER.info("#INSIDE EXCEPTION OCCURED IN GET PASSWORD POLICY LIST" + e.getMessage());
		}
		return (!list.isEmpty()) ? list.get(0) : null;
	}

	@Override
	public boolean deletePasswordPolicy(long id) {
		LOGGER.info("# INSIDE DELETE PASSWORD POLICY API ");
		Session session = getCurrentSession();
		boolean flag = false;
		try {
			session.createNativeQuery("delete from password_policy where id=:Id").setParameter("Id", id)
					.executeUpdate();
			flag = true;
		} catch (Exception e) {
			e.printStackTrace();
			LOGGER.info("#INSIDE EXCEPTION OCCURED IN DELETE PASSWORD POLICY API" + e.getMessage());
		}
		return flag;
	}

	@Override
	public boolean enableDisablePasswordPolicy(PasswordPolicy policy) {
		LOGGER.info("# INSIDE ENABLE DISABLE PASSWORD POLICY API ");
		Session session = getCurrentSession();
		List<Object[]> list = new ArrayList<>();
		boolean flag = false;
		try {
			list = session.createNativeQuery("select * from password_policy where org_id=:orgId")
					.setParameter("orgId", policy.getOrgId()).getResultList();

			if (list.size() > 0) {
				session.createNativeQuery("update password_policy set is_enable=:isEnable where org_id=:orgId")
						.setParameter("orgId", policy.getOrgId()).setParameter("isEnable", policy.getIsEnable())
						.executeUpdate();
				flag = true;
			} else {
				PasswordPolicy pwd = new PasswordPolicy(EnovationConstants.Y, policy.getOrgId());
				session.save(pwd);
				flag = true;
			}
		} catch (Exception e) {
			e.printStackTrace();
			LOGGER.info("#INSIDE EXCEPTION OCCURED IN ENABLE DISABLE PASSWORD POLICY API" + e.getMessage());
		}
		return flag;
	}

	@Override
	public boolean isPasswordPolicyExist(PasswordPolicy policy) {
		boolean flag = false;
		Session session = getCurrentSession();
		System.out.println("Inside is_password_policy_Exists");
		List<Object[]> passwordPolicy = new ArrayList<>();
		try {
			passwordPolicy = session.createNativeQuery("select * from password_policy where org_id=:orgId")
					.setParameter("orgId", policy.getOrgId()).getResultList();
			flag = ((!passwordPolicy.isEmpty()) ? true : false);

		} catch (Exception e) {
			e.printStackTrace();
			LOGGER.info("#EXCEPTION INSIDE IS PASSWORD POLICY EXIST" + e.getMessage());
		}
		return flag;
	}

	@Override
	public boolean addSuggestionTemplate(SuggestionTemplate request) {
		LOGGER.info("#INSIDE SAVE SUGGESTION TEMPLATE API ");
		Session session = getCurrentSession();
		boolean flag = false;
		try {
			session.save(request);
			flag = true;
		} catch (Exception e) {
			LOGGER.info("#INSIDE EXCEPTION OCCURED IN SAVE SUGGESTION TEMPLATE API" + e.getMessage());
		}
		return flag;
	}

	@Override
	public boolean saveSuggestionJurySetup(List<SuggestionEvalJurySetup> request) {
		LOGGER.info("# INSIDE SAVE PASSWORD POLICY SETUP API ");
		Session session = getCurrentSession();
		boolean flag = false;
		try {
			request.stream().forEach(x -> {
				session.save(x);
			});
			flag = true;
		} catch (Exception e) {
			LOGGER.info("#INSIDE EXCEPTION OCCURED IN SAVE SUGGESTION JURY SETUP API" + e.getMessage());
		}
		return flag;
	}

	@Override
	public boolean updateSuggestionTemplate(SuggestionTemplate request) {
		LOGGER.info("#INSIDE UPDATE SUGGESTION TEMPLATE API ");
		Session session = getCurrentSession();
		boolean flag = false;
		try {
			SuggestionTemplate sugTempl = (SuggestionTemplate) session.get(SuggestionTemplate.class, request.getId());
			if (request.getTemplate().getId() > 0)
				sugTempl.setTemplate(request.getTemplate());
			session.update(sugTempl);
			flag = true;
		} catch (Exception e) {
			LOGGER.info("#INSIDE EXCEPTION OCCURED IN UPDATE SUGGESTION TEMPLATE API" + e.getMessage());
		}
		return flag;
	}

	@Override
	public boolean updateSuggestionJurySetup(SuggestionEvalJurySetup request) {
		LOGGER.info("# INSIDE UPDATE SUGGESTION JURY SETUP API ");
		boolean flag = false;
		Session session = getCurrentSession();
		try {
			session.createNativeQuery("update suggestion_jury_setup set emp_id=:empId where id=:Id")
					.setParameter("empId", request.getEmpId()).setParameter("Id", request.getId()).executeUpdate();
			flag = true;

		} catch (Exception e) {
			LOGGER.info("#INSIDE EXCEPTION OCCURED IN UPDATE SUGGESTION JURY SETUP API" + e.getMessage());
		}
		return flag;
	}

	@Override
	public List<SuggestionTemplateMaster> suggestionTemplateMasterList() {
		return getCurrentSession().createQuery("FROM SuggestionTemplateMaster").getResultList();
	}

	@Override
	public boolean deleteSuggestionTemplate(Long id) {
		LOGGER.info("#INSIDE DELETE SUGGESTION TEMPLATE API ");
		Session session = getCurrentSession();
		boolean flag = false;
		try {
			SuggestionTemplate sugTempl = (SuggestionTemplate) session.get(SuggestionTemplate.class, id);
			session.delete(sugTempl);
			flag = true;
		} catch (Exception e) {
			LOGGER.info("#INSIDE EXCEPTION OCCURED IN DELETE SUGGESTION TEMPLATE API" + e.getMessage());
		}
		return flag;
	}

	@Override
	public boolean deleteSuggestionJurySetup(int id) {
		LOGGER.info("# INSIDE DELETE SUGGESTION JURY SETUP API ");
		Session session = getCurrentSession();
		boolean flag = false;
		try {
			session.createNativeQuery("delete from suggestion_eval_jury_setup where id=:Id").setParameter("Id", id)
					.executeUpdate();
			flag = true;
		} catch (Exception e) {
			e.printStackTrace();
			LOGGER.info("#INSIDE EXCEPTION OCCURED IN DELETE SUGGESTION JURY SETUP API" + e.getMessage());
		}
		return flag;
	}

	@Override
	public List<RegistrationSource> getRegistrationSource() {
		LOGGER.info("##  INSIDE GET REGISTRATION SOURCE");
		List<RegistrationSource> list = new ArrayList<>();
		try {
			list = getCurrentSession().createQuery("FROM RegistrationSource").getResultList();
		} catch (Exception e) {
			e.printStackTrace();
			LOGGER.info("#EXCEPTION INSIDE GET REGISTRATION SOURCE API " + e.getMessage());
		}
		return list;
	}

	@Override
	public List<SuggestionEvalMstrParam> getSuggestionKaizenParameters() {
		LOGGER.info("##  INSIDE GET SUGGESTION KAIZEN PARAMETERS");
		List<SuggestionEvalMstrParam> list = new ArrayList<>();
		try {
			list = getCurrentSession().createQuery("FROM SuggestionEvalMstrParam").getResultList();
		} catch (Exception e) {
			e.printStackTrace();
			LOGGER.info("#EXCEPTION INSIDE GET SUGGESTION KAIZEN PARAMETERS API " + e.getMessage());
		}
		return list;
	}

	@Override
	public List<SuggestionEvalMstrRatings> getSuggestionKaizenRatings(int branchId) {
		LOGGER.info("##  INSIDE GET SUGGESTION KAIZEN RATINGS");
		List<SuggestionEvalMstrRatings> list = new ArrayList<>();
		try {
			list = getCurrentSession().createQuery("FROM SuggestionEvalMstrRatings where branchId=:branchID")
					.setParameter("branchID", branchId).getResultList();
		} catch (Exception e) {
			e.printStackTrace();
			LOGGER.info("#EXCEPTION INSIDE GET SUGGESTION KAIZEN RATINGS API " + e.getMessage());
		}
		return list;
	}

	@Override
	public List<SuggestionEvalMstrAssessment> getSuggestionMasterAssesment() {
		LOGGER.info("##  INSIDE GET SUGGESTION MASTER ASSESMENT");
		List<SuggestionEvalMstrAssessment> list = new ArrayList<>();
		try {
			list = getCurrentSession().createQuery("FROM SuggestionEvalMstrAssessment").getResultList();
		} catch (Exception e) {
			e.printStackTrace();
			LOGGER.info("#EXCEPTION INSIDE GET SUGGESTION MASTER ASSESMENT API " + e.getMessage());
		}
		return list;
	}

	@Override
	public List<SuggestionEvalAssessmentInput> getSuggestionAssesmentInputByBranchId(Integer branchId) {
		LOGGER.info("##  INSIDE GET SUGGESTION MASTER ASSESMENT INPUT BY BRANCHID");
		List<SuggestionEvalAssessmentInput> list = new ArrayList<>();
		Session session = getCurrentSession();
		try {
			List<Object[]> assesInputList = session
					.createNativeQuery("SELECT \r\n" + "    i.id AS assInputId,\r\n" + "    i.branch_id,\r\n"
							+ "    i.day,\r\n" + "    i.sug_master_asses_id,\r\n" + "    ass.id,\r\n"
							+ "    ass.description,\r\n" + "    ass.name\r\n" + "FROM\r\n"
							+ "    suggestion_eval_assessment_input i\r\n" + "        INNER JOIN\r\n"
							+ "    suggestion_eval_mstr_assessment ass ON i.sug_master_asses_id = ass.id\r\n"
							+ "WHERE\r\n" + "    i.branch_id =:branchId")
					.setParameter("branchId", branchId).getResultList();

			for (Object[] x : assesInputList) {
				SuggestionEvalAssessmentInput assesInput = new SuggestionEvalAssessmentInput();
				if (x[0] != null)
					assesInput.setId(Integer.valueOf(String.valueOf(x[0])));
				if (x[1] != null)
					assesInput.setBranchId(Integer.valueOf(String.valueOf(x[1])));
				if (x[2] != null)
					assesInput.setDay(Integer.valueOf(String.valueOf(x[2])));
				if (x[3] != null)
					assesInput.setSugMasterAssesId(Integer.valueOf(String.valueOf(x[3])));
				if (x[4] != null)
					assesInput.setAssesId(Integer.valueOf(String.valueOf(x[4])));
				if (x[5] != null)
					assesInput.setDescription(String.valueOf(x[5]));
				if (x[6] != null)
					assesInput.setName(String.valueOf(x[6]));
				list.add(assesInput);
			}
		} catch (Exception e) {
			e.printStackTrace();
			LOGGER.info("#EXCEPTION INSIDE GET SUGGESTION MASTER ASSESMENT INPUT BY BRANCHID API " + e.getMessage());
		}
		return list;
	}

	@Override
	public boolean saveSuggestionAssessmentInput(SuggestionEvalAssessmentInput request) {
		LOGGER.info("# INSIDE SAVE SUGGESTION ASSESSMENT INPUT API ");
		Session session = getCurrentSession();
		boolean flag = false;
		try {
			session.save(request);
			flag = true;
		} catch (Exception e) {
			LOGGER.info("#INSIDE EXCEPTION OCCURED IN SAVE SUGGESTION ASSESSMENT INPUT API" + e.getMessage());
		}
		return flag;
	}

	@Override
	public boolean updateSuggestionAssessmentInput(SuggestionEvalAssessmentInput request) {
		LOGGER.info("# INSIDE UPDATE SUGGESTION ASSESSMENT INPUT API ");
		boolean flag = false;
		Session session = getCurrentSession();
		try {
			System.out.println("Days====>" + request.getDay());
			System.out.println("ID====>" + request.getId());
			int status = session.createQuery("update SuggestionEvalAssessmentInput set day=:days where id=:Id")
					.setParameter("days", request.getDay()).setParameter("Id", request.getId()).executeUpdate();
			if (status > 0)
				flag = true;
		} catch (Exception e) {
			e.printStackTrace();
			LOGGER.info("#EXCEPTION INSIDE UPDATE SUGGESTION ASSESSMENT INPUT API " + e.getMessage());
		}
		return flag;
	}

	@Override
	public List<SuggestionEvalMstrNomination> getSugEvalMasterNomination(int branchId) {
		LOGGER.info("##  INSIDE GET SUGGESTION EVAL MASTER NOMINATION");
		List<SuggestionEvalMstrNomination> list = new ArrayList<>();
		try {
			list = getCurrentSession()
					.createQuery("FROM SuggestionEvalMstrNomination where branchId=:branchID and status='ACTIVE'")
					.setParameter("branchID", branchId).getResultList();
		} catch (Exception e) {
			e.printStackTrace();
			LOGGER.info("#EXCEPTION INSIDE GET SUGGESTION EVAL MASTER NOMINATION API " + e.getMessage());
		}
		return list;
	}

	@Override
	public boolean addSugEvalAuthorityPhead(SuggestionEvalAuthoritySetup request) {
		LOGGER.info("# INSIDE ADD SUG EVAL AUTHORITY PHEAD API ");
		Session session = getCurrentSession();
		boolean flag = false;
		try {
			session.save(request);
			flag = true;
		} catch (Exception e) {
			LOGGER.info("#INSIDE EXCEPTION OCCURED IN ADD SUG EVAL AUTHORITY PHEAD API" + e.getMessage());
		}
		return flag;
	}

	@Override
	public boolean deleteSugEvalAuthority(int id) {
		LOGGER.info("# INSIDE SUG EVAL AUTHORITY HR API ");
		Session session = getCurrentSession();
		boolean flag = false;
		try {
			session.createNativeQuery("delete from suggestion_eval_authority_setup where id=:Id").setParameter("Id", id)
					.executeUpdate();
			flag = true;
		} catch (Exception e) {
			e.printStackTrace();
			LOGGER.info("#INSIDE EXCEPTION OCCURED IN DELETE SUG EVAL AUTHORITY HR API" + e.getMessage());
		}
		return flag;
	}

	@Override
	public boolean addSugEvalAuthorityHR(SuggestionEvalAuthoritySetup request) {
		LOGGER.info("# INSIDE ADD SUG EVAL AUTHORITY HR API ");
		Session session = getCurrentSession();
		boolean flag = false;
		try {
			session.save(request);
			flag = true;
		} catch (Exception e) {
			LOGGER.info("#INSIDE EXCEPTION OCCURED IN ADD SUG EVAL AUTHORITY HR API" + e.getMessage());
		}
		return flag;
	}

	@Override
	public boolean isPheadExist(SuggestionEvalAuthoritySetup request) {
		boolean flag = false;
		Session session = getCurrentSession();
		System.out.println("Inside isPheadExist");
		List<Object[]> pheadExist = new ArrayList<>();
		try {
			pheadExist = session.createNativeQuery(
					"select * from suggestion_eval_authority_setup where branch_id=:branchId and user_type=:userType")
					.setParameter("branchId", request.getBranchId())
					.setParameter("userType", EnovationConstants.PRODUCTION_HEAD_STRING).getResultList();
			flag = ((!pheadExist.isEmpty()) ? true : false);

		} catch (Exception e) {
			e.printStackTrace();
			LOGGER.info("#EXCEPTION INSIDE IS PHEAD EXIST" + e.getMessage());
		}
		return flag;
	}

	@Override
	public boolean updateRewardCategory(RewardCategoryMaster rcm) {
		LOGGER.info("#INSIDE UPDATE REWARD CATEGORY API ");
		Session session = getCurrentSession();
		boolean flag = false;
		try {
			RewardCategoryMaster rc = (RewardCategoryMaster) session.get(RewardCategoryMaster.class, rcm.getId());
			if (rcm.getCategoryName() != null)
				rc.setCategoryName(rcm.getCategoryName());
			if (rcm.getDescription() != null)
				rc.setDescription(rcm.getDescription());
			session.update(rc);
			flag = true;
		} catch (Exception e) {
			LOGGER.info("#INSIDE EXCEPTION OCCURED IN UPDATE REWARD CATEGORY API" + e.getMessage());
		}
		return flag;
	}

	@Override
	public boolean deleteRewardCategory(int id) {
		boolean flag = false;
		Session session = getCurrentSession();
		try {
			Object obj = session.createNativeQuery("update reward_category_master set is_enable=:isEnable where id=:Id")
					.setParameter("Id", id).setParameter("isEnable", EnovationConstants.ZERO).executeUpdate();
			if (obj != null)
				flag = true;
		} catch (Exception e) {
			e.printStackTrace();
			LOGGER.info("#EXCEPTION INSIDE DELETE REWARD CATEGORY" + e.getMessage());
		}
		return flag;
	}

	@Override
	public boolean saveRewardAdminSetup(RewardAdminSetup request) {
		LOGGER.info("# INSIDE SAVE REWARD ADMIN SETUP API ");
		Session session = getCurrentSession();
		boolean flag = false;
		try {
			request.setIsEnable(EnovationConstants.ONE);
			session.save(request);
			flag = true;
		} catch (Exception e) {
			LOGGER.info("#INSIDE EXCEPTION OCCURED IN SAVE REWARD ADMIN SETUP API" + e.getMessage());
		}
		return flag;
	}

	@Override
	public boolean deleteRewardAdminSetup(RewardAdminSetup request) {
		LOGGER.info("INSIDE DELETE REWARD ADMIN SETUP API");
		Session session = getCurrentSession();
		boolean flag = true;
		try {
			int status = session.createNativeQuery("update reward_admin_setup set is_enable=:isEnable where id=:Id")
					.setParameter("Id", request.getId()).setParameter("isEnable", EnovationConstants.ZERO)
					.executeUpdate();
			if (status > 0)
				flag = true;
		} catch (Exception e) {
			LOGGER.error("#INSIDE EXCEPTION OCCURED IN DELETE REWARD ADMIN SETUP API" + e.getMessage());
		}
		return flag;
	}

	@Override
	public List<RewardAdminSetup> getRewardAdminList(int branchId) {
		LOGGER.info("##  INSIDE GET REWARD ADMIN LIST BY BRANCHID");
		List<RewardAdminSetup> list = new ArrayList<>();
		Session session = getCurrentSession();
		try {
			List<Object[]> assesInputList = session.createNativeQuery(
					"select a.id,a.emp_id,a.branch_id,a.org_id,a.created_date,a.updated_date,e.first_name,e.last_name,e.contact_no,\r\n"
							+ "e.cmpy_emp_id,e.designation,d.dept_name,e.email_id,b.name,CONCAT(e.first_name,' ',e.last_name)  \r\n"
							+ "from reward_admin_setup a   \r\n"
							+ "inner join tbl_employee_details e on e.emp_id=a.emp_id  \r\n"
							+ "inner join master_department d on d.dept_id=e.dept_id  \r\n"
							+ "inner join master_branch b on b.branch_id=e.branch_id\r\n"
							+ "where a.is_enable=1 and  a.branch_id=:branchId")
					.setParameter("branchId", branchId).getResultList();

			for (Object[] x : assesInputList) {
				RewardAdminSetup dto = new RewardAdminSetup();
				if (x[0] != null)
					dto.setId(Integer.valueOf(String.valueOf(x[0])));
				if (x[1] != null)
					dto.setEmpId(Integer.valueOf(String.valueOf(x[1])));
				if (x[2] != null)
					dto.setBranchId(Integer.valueOf(String.valueOf(x[2])));
				if (x[3] != null)
					dto.setOrgId(Integer.valueOf(String.valueOf(x[3])));
				if (x[4] != null)
					dto.setCreatedDate(CommonUtils.convertStringToTimestamp(String.valueOf(x[4])));
				if (x[5] != null)
					dto.setUpdatedDate(CommonUtils.convertStringToTimestamp(String.valueOf(x[5])));
				if (x[6] != null)
					dto.setFirstName(String.valueOf(x[6]));
				if (x[7] != null)
					dto.setLastName(String.valueOf(x[7]));
				if (x[8] != null)
					dto.setContactNo(String.valueOf(x[8]));
				if (x[9] != null)
					dto.setCmpyEmpId(String.valueOf(x[9]));
				if (x[10] != null)
					dto.setDesignation(String.valueOf(x[10]));
				if (x[11] != null)
					dto.setDeptName(String.valueOf(x[11]));
				if (x[12] != null)
					dto.setEmailId(String.valueOf(x[12]));
				if (x[13] != null)
					dto.setBranchName(String.valueOf(x[13]));
				if (x[14] != null)
					dto.setName(String.valueOf(x[14]));
				list.add(dto);

			}
		} catch (Exception e) {
			e.printStackTrace();
			LOGGER.info("#EXCEPTION INSIDE GET REWARD ADMIN LIST BY BRANCHID API " + e.getMessage());
		}
		return list;
	}

	@Override
	public boolean getRewardAdminListByEmpId(int empId) {
		LOGGER.info("##  INSIDE GET REWARD ADMIN LIST BY EMPID");
		boolean flag = false;
		List<Object[]> assesInputList = new ArrayList<>();
		Session session = getCurrentSession();
		try {
			assesInputList = session.createNativeQuery("select * from reward_admin_setup where emp_id=:empId")
					.setParameter("empId", empId).getResultList();

			flag = ((!assesInputList.isEmpty()) ? true : false);
		} catch (Exception e) {
			e.printStackTrace();
			LOGGER.info("#EXCEPTION INSIDE GET REWARD ADMIN LIST BY EMPID API " + e.getMessage());
		}
		return flag;
	}

	@Override
	public List<RedeemCartRewards> getRedemptionTrackerList(Integer branchId) {
		LOGGER.info("INSIDE REDEMPTION_TRACKER_LIST API");
		List<RedeemCartRewards> list = new ArrayList<>();
		Session session = getCurrentSession();
		try {
			List<Object[]> resultList = session.createNativeQuery(
					"select rc.id,r.description,rc.updated_date,rc.product_point,rc.total_point,rc.quantity,s.status,r.gift_code,"
							+ " rc.emp_id,concat(e.first_name,' ',e.last_name),e.cmpy_emp_id,rc.status_id \r\n"
							+ "		from\r\n" + "		redeem_cart_rewards rc\r\n" + "		inner join  \r\n"
							+ "		cart c on rc.cart_id=c.id\r\n" + "		inner join \r\n"
							+ "		rewards r on c.reward_id=r.id\r\n" + "		inner join  \r\n"
							+ "		redeem_status s on rc.status_id=s.id\r\n"
							+ "     inner join tbl_employee_details e on e.emp_id=rc.emp_id\r\n" + "		where  \r\n"
							+ "		rc.branch_id=:branchId group by rc.id order by rc.updated_date DESC")
					.setParameter("branchId", branchId).getResultList();
			for (Object[] obj : resultList) {
				RedeemCartRewards redeem = new RedeemCartRewards();
				if (obj[0] != null) {
					redeem.setId(Integer.parseInt(obj[0].toString()));
				}
				if (obj[1] != null) {
					redeem.setDescription(String.valueOf(obj[1]));
				}
				if (obj[2] != null) {
					redeem.setUpdatedDate(CommonUtils.convertStringToTimestamp(String.valueOf(obj[2])));
				}
				if (obj[3] != null) {
					redeem.setProductPoint(Integer.parseInt(obj[3].toString()));
				}
				if (obj[4] != null) {
					redeem.setTotalPoint(Integer.parseInt(obj[4].toString()));
				}
				if (obj[5] != null) {
					redeem.setQuantity(Integer.parseInt(obj[5].toString()));
				}
				RedeemStatus s = new RedeemStatus();
				if (obj[6] != null) {
					s.setStatus(String.valueOf(obj[6]));
					s.setId(CommonUtils.objectToInt(obj[11]));
				}
				redeem.setStatus(s);
				redeem.setGiftCode(CommonUtils.objectToString(obj[7].toString()));
				redeem.setEmpId(CommonUtils.objectToInt(obj[8]));
				redeem.setName(CommonUtils.objectToString(obj[9]));
				redeem.setCmpEmpId(CommonUtils.objectToString(obj[10]));

				list.add(redeem);
			}
		} catch (Exception e) {
			LOGGER.error("#INSIDE EXCEPTION OCCURED IN REDEMPTION_TRACKER_LIST API" + e.getMessage());
		}
		return list;
	}

	@Override
	@Transactional
	public int addOrUpdateSuggConfig(SuggestionFlowConfig config) {
		LOGGER.info("## INSIDE ADD OR UPDATE SUGG CONFIG API ");
		int flag = 0;
		try {
			if (config.getFlowConfId() > 0) {
				getCurrentSession().update(config);
				flag = EnovationConstants.ONE;
			} else {
				getCurrentSession().save(config);
				flag = EnovationConstants.TWO;
			}
		} catch (Exception e) {
			e.printStackTrace();
			LOGGER.info("#EXCEPTION INSIDE ADD OR UPDATE SUGG CONFIG API " + e.getMessage());
		}
		return flag;
	}

	@Override
	public List<SuggestionFlowConfig> getSuggConfigList(Integer orgId) {
		LOGGER.info("## INSIDE GET SUGG CONFIG API ");
		List<SuggestionFlowConfig> list = new ArrayList<>();
		try {
			list = getCurrentSession().createQuery("FROM SuggestionFlowConfig WHERE orgId=:orgId")
					.setParameter("orgId", orgId).getResultList();
		} catch (Exception e) {
			e.printStackTrace();
			LOGGER.info("#EXCEPTION INSIDE GET SUGG CONFIG API " + e.getMessage());
		}
		return list;
	}

	@Override
	public List<OrganizationMaster> getOrgDataByAlieas(String alieas) {
		LOGGER.info("##  INSIDE GET REWARD ADMIN LIST BY EMPID");
		List<OrganizationMaster> orgMaster = new ArrayList<>();
		Session session = getCurrentSession();
		try {
			orgMaster = session.createQuery("from OrganizationMaster where alies=:alieas")
					.setParameter("alieas", alieas).getResultList();
		} catch (Exception e) {
			e.printStackTrace();
			LOGGER.info("#EXCEPTION INSIDE GET REWARD ADMIN LIST BY EMPID API " + e.getMessage());
		}
		return orgMaster;
	}

	@Override
	public boolean removeLineDetails(Line line) {
		LOGGER.info("##  REMOVE LINE DEATILS API");
		Session session = getCurrentSession();
		boolean flag = false;
		Line lineDet = null;
		System.out.println("=============== Inside Delete Line ==============================");
		// lineDet = session.get(Line.class, line.getId());
		// session.delete(lineDet);

		session.createNativeQuery("DELETE FROM dwm_line WHERE id=:id").setParameter("id", line.getId()).executeUpdate();
		flag = true;

		return flag;
	}

	@Override
	@Transactional
	public boolean deactiveLine(Line line) {
		LOGGER.info("##  DEACTIVE LINE DEATILS API");
		Session session = getCurrentSession();
		boolean flag = false;
		Line lineDet = null;
		try {
			System.out.println("Line ID ===> " + line.getId());
			lineDet = session.get(Line.class, line.getId());
			lineDet.setIsActive("N");
			session.update(lineDet);
			flag = true;
		} catch (Exception e) {
			LOGGER.info("#EXCEPTION INSIDE DEACTIVE LINE DETAILS API " + e.getMessage());
		}
		return flag;
	}

	@Override
	public boolean updateOptionalJurySetup(SuggestionEvalJurySetup request) {
		boolean flag = false;
		LOGGER.info("In MasterDaoImple | updateOptionalJurySetup");
		Session session = getCurrentSession();
		session.createNativeQuery(
				"update suggestion_eval_jury_setup set month=:month where branch_id=:branchId and dept_id=:deptId")
				.setParameter("branchId", request.getBranchId()).setParameter("deptId", request.getDeptId())
				.setParameter("month", request.getMonth()).executeUpdate();
		flag = true;
		return flag;
	}

	@Override
	public boolean saveSuggestionUserSetup(SuggestionUserSetup request) {
		boolean flag = false;
		Session session = getCurrentSession();
		session.save(request);
		flag = true;
		return flag;
	}

	@Override
	public List<Object[]> getSuggestionUserSetup(Integer branchId, Integer orgId) {
		return getCurrentSession().createNativeQuery(
				"select sus.id,sus.user_type_id,sus.branch_id,sus.org_id,b.name as branchName,u.caption,  \r\n"
						+ "sus.emp_id,e.cmpy_emp_id,concat(e.first_name,' ',e.last_name) as empName,e.email_id,e.contact_no  \r\n"
						+ ",e.designation  \r\n" + "from suggestion_user_setup sus   \r\n"
						+ "inner join master_sug_user_type u on u.id=sus.user_type_id  \r\n"
						+ "inner join tbl_employee_details e on e.emp_id=sus.emp_id  \r\n"
						+ "inner join master_branch b on b.branch_id=e.branch_id  \r\n"
						+ "where sus.branch_id=:branchId and sus.org_id=:orgId")
				.setParameter("branchId", branchId).setParameter("orgId", orgId).getResultList();
	}

	@Override
	public boolean isEmpRoleExist(SuggestionUserSetup request) {
		boolean flag = false;
		Session session = getCurrentSession();
		List<Object[]> empRoleList = new ArrayList<>();

		empRoleList = session.createNativeQuery("select * from emp_roles where role_id=10 and emp_id=:empId")
				.setParameter("empId", request.getEmp().getEmpId()).getResultList();
		flag = ((!empRoleList.isEmpty()) ? true : false);
		return flag;
	}

	@Override
	public boolean insertIntoEmpRole(SuggestionUserSetup request) {
		boolean flag = false;
		Session session = getCurrentSession();
		int status = session.createNativeQuery("insert into emp_roles(emp_id,role_id) values (:empId,10)")
				.setParameter("empId", request.getEmp().getEmpId()).executeUpdate();
		if (status > 0) {
			flag = true;
		}
		return flag;
	}

	@Override
	public boolean deleteSuggestionUserSetup(SuggestionUserSetup request) {
		boolean flag = false;
		Session session = getCurrentSession();
		session.createNativeQuery("delete from suggestion_user_setup where id=:Id").setParameter("Id", request.getId())
				.executeUpdate();
		flag = true;
		return flag;
	}

	@Override
	public List<Object[]> getEmpUserSetupData(SuggestionUserSetup request) {
		List<Object[]> resultList = new ArrayList<>();
		Session session = getCurrentSession();
		resultList = session.createNativeQuery("select * from suggestion_user_setup where emp_id=:empId")
				.setParameter("empId", request.getEmp().getEmpId()).getResultList();
		return resultList;
	}

	@Override
	public boolean isEmpPendingActionsExists(SuggestionUserSetup request) {
		boolean flag = false;
		Session session = getCurrentSession();
		List<Object[]> empPending = new ArrayList<>();
		empPending = session
				.createNativeQuery("select ssa.* from sugesstion_status_audit ssa \r\n"
						+ "inner join master_status s on s.status_id=ssa.sts_id\r\n"
						+ "where ssa.emp_id=:empId and ssa.action_status=:pendingSts and s.role_id = 10")
				.setParameter("empId", request.getEmp().getEmpId())
				.setParameter("pendingSts", EnovationConstants.PENDING_STRING).getResultList();
		flag = ((!empPending.isEmpty()) ? true : false);
		return flag;
	}

	@Override
	public boolean deleteFromEmpRoles(SuggestionUserSetup request) {
		boolean flag = false;
		Session session = getCurrentSession();
		session.createNativeQuery("delete from emp_roles where emp_id=:empId and role_id=10")
				.setParameter("empId", request.getEmp().getEmpId()).executeUpdate();
		flag = true;
		return flag;
	}

	@Override
	public List<MasterSugUserType> getSuggUserTypes() {
		return getCurrentSession().createQuery("FROM MasterSugUserType").getResultList();
	}

	@Override
	public boolean addUpdateParticularsPoints(ParticularsPoint particulars) {
		boolean flag = false;
		getCurrentSession().saveOrUpdate(particulars);
		flag = true;
		return flag;
	}

	/**
	 * @author Vinay B. Nov 24, 2020 2:44:29 PM
	 * @return
	 */
	@Override
	public List<MasterUnit> getMasterUnits() {
		return getCurrentSession().createQuery("FROM MasterUnit").getResultList();
	}

	public void addEmpRole(int empId, Session session, int roleId) {
		LOGGER.info("# Add emp role - Emp ID ->  " + empId + " Role ID - " + roleId);
		int count = isRoleExist(empId, session, roleId);

		LOGGER.info("# Role Exist Count - " + count);

		if (count == 0) {
			session.createNativeQuery(" insert into emp_roles(emp_id,role_id) values(:empId,:roleId)")
					.setParameter("empId", empId).setParameter("roleId", roleId).executeUpdate();
		}
	}

	@SuppressWarnings("unused")
	// @Override
	public void deleteEmpRole(int empId, Session session, int roleId) {
		LOGGER.info("# Delete emp role - Emp ID ->  " + empId + " Role ID - " + roleId);
		int count = isRoleExist(empId, session, roleId);

		LOGGER.info("# Role Exist Count - " + count);

		if (count == 0) {
			session.createNativeQuery(" delete from emp_roles where emp_id=:empId and role_id=:roleId ")
					.setParameter("empId", empId).setParameter("roleId", roleId).executeUpdate();
		}
	}

	public int isRoleExist(int empId, Session session, int roleId) {
		int count = ((Number) session
				.createNativeQuery(" select count(emp_id) as empIdCount from emp_roles \r\n "
						+ " where emp_id=:empId and role_id=:roleId ")
				.setParameter("empId", empId).setParameter("roleId", roleId).getSingleResult()).intValue();
		return count;
	}

	/**
	 * @author Vinay B. May 4, 2022 1:43:53 PM
	 */
	public int getRoleId(long id, Session session) {

		return ((Number) session
				.createNativeQuery("select role_id as roleId from pms_master_user_types where id=:pmsUserTypeId")
				.setParameter("pmsUserTypeId", id).getSingleResult()).intValue();

	}

	@Override
	public List<MasterDTO> employeeWiseHappinessIndex(MasterDTO request) {
		LOGGER.info("# MasterDaoImpl | employeeWiseHappinessIndex");
		List<MasterDTO> list = new ArrayList<>();
		Session session = getCurrentSession();

		// Prepare happiness index query to fetch data
		StringBuilder sb = getHappineesIndexQry(request);

		// Set Query Parameters
		String sql = sb.toString().replaceAll(Pattern.quote("{branchId}"), String.valueOf(request.getBranchId()))
				.replaceAll(Pattern.quote("{deptId}"), String.valueOf(request.getDeptId()))
				.replaceAll(Pattern.quote("{limit}"), String.valueOf(request.getLimit()))
				.replaceAll(Pattern.quote("{offset}"), String.valueOf(request.getOffset()))
				.replaceAll(Pattern.quote("{fromDate}"), String.valueOf(request.getFromDt()))
				.replaceAll(Pattern.quote("{toDate}"), String.valueOf(request.getToDt()));

		// Fetch data from database
		List<Tuple> data = session.createNativeQuery(sql, Tuple.class).getResultList();

		// Parse fetched data
		parseMoodIndicatorDetails(list, data);

		return list;
	}

	private StringBuilder getHappineesIndexQry(MasterDTO request) {
		LOGGER.info("# Build Happiness Index Query");
		StringBuilder sb = new StringBuilder(
				"SELECT mi.rating as rating,date_format(mi.created_date,'%Y-%m-%d') as moodIndicatorDate, \r\n "
						+ " concat(ifnull(ted.first_name,''),' ',ifnull(ted.last_name,'')) as empName, ted.cmpy_emp_id as cmpEmpId, \r\n"
						+ " md.dept_name as deptName, mb.name as branchName FROM mood_indicator mi \r\n"
						+ " LEFT JOIN tbl_employee_details ted ON mi.emp_id = ted.emp_id \r\n"
						+ " LEFT JOIN master_department md ON ted.dept_id = md.dept_id \r\n"
						+ " LEFT JOIN master_branch mb ON mb.branch_id = ted.branch_id WHERE  ted.is_deactive = 0 AND  ");

		if (request.getBranchId() > 0) {
			sb.append("  ted.branch_id={branchId} ");
		}
		if (request.getDeptId() > 0) {
			sb.append("  ted.dept_id={deptId} ");
		}
		if (request.getFromDt() != null && request.getToDt() != null) {
			sb.append(" AND mi.created_date BETWEEN '{fromDate}' AND '{toDate}' ");
		}

		if (request.getLimit() > 0) {
			sb.append(" limit {limit} offset {offset} ");
		}
		return sb;
	}

	private void parseMoodIndicatorDetails(List<MasterDTO> list, List<Tuple> data) {
		LOGGER.info("# MasterDaoImpl | parseMoodIndicatorDetails");
		if (CollectionUtils.isNotEmpty(data)) {
			LOGGER.info("# List is not empty - size -> " + data.size());
			data.stream().forEach(x -> {
				MasterDTO obj = new MasterDTO();
				obj.setRating(CommonUtils.objectToInt(String.valueOf(x.get("rating"))));
				obj.setMoodIndicatorDate(CommonUtils.objectToString(String.valueOf(x.get("moodIndicatorDate"))));
				obj.setEmpName(CommonUtils.objectToString(String.valueOf(x.get("empName"))));
				obj.setCmpEmpId(CommonUtils.objectToString(String.valueOf(x.get("cmpEmpId"))));
				obj.setDeptName(CommonUtils.objectToString(String.valueOf(x.get("deptName"))));
				obj.setBranchName(CommonUtils.objectToString(String.valueOf(x.get("branchName"))));
				list.add(obj);
			});
		} else {
			LOGGER.info("# List is empty - size -> " + data.size());
		}
	}

	/**
	 * @author rakesh 12 August 2020
	 */
	@Override
	public boolean addOrUpdateCategoryList(MasterDTO request) {
		boolean flag = false;
		Session session = getCurrentSession();

		for (CategoryMaster category : request.getCategoryList()) {
			if (category.getCatId() > 0) {
				String fcRequired = "", sapRequired = "", susAuditRequired = "";

				StringBuilder builder = new StringBuilder("update master_category set category_name=:catName ");
				if (category.getIsFCVRequired() != null) {
					fcRequired = category.getIsFCVRequired();
					builder.append(",isfcvrequired='{fvRequired}'");
				}

				if (category.getIsSusAuditRequired() != null) {
					susAuditRequired = category.getIsSusAuditRequired();
					builder.append(",is_sus_audit_required='{isSusAuditRequired}'");
				}

				builder.append(" where branch_id=:branchId and cat_id=:catId");
				String query = (builder.toString()).replace("{fvRequired}", fcRequired)
						.replace("{sapCodeRequired}", sapRequired).replace("{isSusAuditRequired}", susAuditRequired);

				session.createNativeQuery(query).setParameter("branchId", category.getBranchId())
						.setParameter("catId", category.getCatId()).setParameter("catName", category.getCategoryName())
						.executeUpdate();
			} else {
				session.save(category);
			}
		}

		flag = true;
		return flag;
	}

	@Override
	public boolean addCWAgencyDetails(CWAgency request) {
		LOGGER.info("Inside IMasterDao | addCWAgencyDetails ");
		boolean flag = false;
		Session session = getCurrentSession();

		CWAgency cwobj = new CWAgency();
		cwobj.setName(request.getName());
		cwobj.setMasterOrg(new OrganizationMaster(request.getOrgId()));
		// cwobj.setMasterBranch(new BranchMaster(request.getBranchId()));

		session.save(cwobj);
		flag = true;

		return flag;
	}

	@Override
	public List<CWAgency> getCWAgencyList(Integer orgId) {
		LOGGER.info("Inside IMasterDao | getCWAgencyList ");
		Session session = getCurrentSession();
		List<CWAgency> cwList = new ArrayList<CWAgency>();

		List<Tuple> list = session
				.createNativeQuery("select id as cwId ,name as name from cw_agency where org_id=:orgId", Tuple.class)
				.setParameter("orgId", orgId).getResultList();

		for (Tuple obj : list) {
			CWAgency cwobj = new CWAgency();

			cwobj.setId(CommonUtils.objectToLong(obj.get("cwId")));
			cwobj.setName(CommonUtils.objectToString(obj.get("name")));

			cwList.add(cwobj);
		}
		return cwList;
	}

	@Override
	public boolean addDocument(MasterDocument request) {
		LOGGER.info("Inside IMasterDao | getCWAgencyList ");
		Session session = getCurrentSession();
		boolean flag = false;
		MasterDocument obj=new MasterDocument();
		BeanUtils.copyProperties(request, obj);
		if(request.getOwnerId()>0) {
			obj.setOwner(new EmployeeDetails(request.getOwnerId()));
		}
		if(request.getBranchId()>0) {
			obj.setBranch(new BranchMaster(request.getBranchId()));
		}
		session.save(obj);
		dbUtils.checkAndAddEmpRole(session, obj.getOwnerId(), EnovationConstants.DOCUMENT_OWNER);
		flag=true;
		return flag;
	}

	@Override
	public boolean updateDocument(MasterDocument request) {
		LOGGER.info("Inside IMasterDao | getCWAgencyList ");
		Session session = getCurrentSession();
		boolean flag = false;
		MasterDocument obj=(MasterDocument) session.get(MasterDocument.class, request.getId());
		if(obj!=null) {
			int ownerId=0;
			
			if(request.getUpdatedBy()>0) {
				obj.setUpdatedBy(request.getUpdatedBy());
			}
			if(request.getDocumentName()!=null) {
				obj.setDocumentName(request.getDocumentName());
			}
			if(request.getOwnerId()!=obj.getOwner().getEmpId()) {	
				ownerId=obj.getOwner().getEmpId();
				obj.setOwner(new EmployeeDetails(request.getOwnerId()));	
			}
			if(request.getDept()!=null && request.getDept().getDeptId()>0) {
				obj.setDept(new DepartmentMaster(request.getDept().getDeptId()));
			}
			if(request.getLine()!=null && request.getLine().getId()>0) {
				obj.setLine(new Line(request.getLine().getId()));
			}
			
			session.update(obj);
			flag=true;
			
			if(ownerId>0) {
				checkAndRemoveEmpRole(session,ownerId);	
				dbUtils.checkAndAddEmpRole(session, request.getOwnerId(), EnovationConstants.DOCUMENT_OWNER);		
			}
			
		}

		return flag;
	}

	private void checkAndRemoveEmpRole(Session session, int empId) {
		LOGGER.info("Inside IMasterDao | checkAndRemoveEmpRole ");
		List<Tuple> list=session.createNativeQuery("select id from master_document where owner_id=:ownerId ",Tuple.class)
		.setParameter("ownerId", empId).getResultList();
		
		if(CollectionUtils.isEmpty(list)) {
			dbUtils.removeEmpRole(session, empId, EnovationConstants.DOCUMENT_OWNER);
		}
		
	}

	@Override
	public boolean removeDocument(MasterDocument request) {
		LOGGER.info("Inside IMasterDao | getCWAgencyList ");
		Session session = getCurrentSession();
		boolean flag = false;

		List<Tuple> list=session.createNativeQuery("select owner_id as empId from master_document where id=:id",Tuple.class)
		.setParameter("id", request.getId()).getResultList();
		
		if(!CollectionUtils.isEmpty(list)) {
			Tuple obj=list.get(0);
			
			flag=session.createNativeQuery("delete from master_document where id=:id ").setParameter("id", request.getId()).executeUpdate()>0?true:flag;
			
			if(flag) {
				checkAndRemoveEmpRole(session,CommonUtils.objectToInt(obj.get("empId")));
			}
			
		}
		return flag;
	}

}