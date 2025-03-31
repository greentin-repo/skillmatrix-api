package com.greentin.enovation.accesscontrol;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import javax.persistence.Query;
import javax.transaction.Transactional;

import org.hibernate.Session;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.task.TaskExecutor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

import com.greentin.enovation.audit.AuditComponent;
import com.greentin.enovation.beans.MenuData;
import com.greentin.enovation.beans.MyMenu;
import com.greentin.enovation.bo.model.EmployeeDetailsBO;
import com.greentin.enovation.communication.ICommunication;
import com.greentin.enovation.config.EnovationConfig;
import com.greentin.enovation.employee.EmployeeDaoImple;
import com.greentin.enovation.master.IMasterDao;
import com.greentin.enovation.master.MasterDaoImpl;
import com.greentin.enovation.model.BenefitAnalysisMaster;
import com.greentin.enovation.model.BranchAccessSetup;
import com.greentin.enovation.model.BranchMaster;
import com.greentin.enovation.model.BranchSetupConfig;
import com.greentin.enovation.model.BrowniePointsReimbursementCycle;
import com.greentin.enovation.model.BrowniePointsReimbursementCycleType;
import com.greentin.enovation.model.CategoryMaster;
import com.greentin.enovation.model.EmailTemplateMaster;
import com.greentin.enovation.model.EmployeeDetails;
import com.greentin.enovation.model.ModuleSubscription;
import com.greentin.enovation.model.MultiBranchAccess;
import com.greentin.enovation.model.OrganizationMaster;
import com.greentin.enovation.model.Particulars;
import com.greentin.enovation.model.RegistrationSource;
import com.greentin.enovation.model.RoleEscalationSetup;
import com.greentin.enovation.model.RoleMenuAccess;
import com.greentin.enovation.model.SavingOptions;
import com.greentin.enovation.model.SetupMaster;
import com.greentin.enovation.model.SubscriptionType;
import com.greentin.enovation.model.SuggestionEscalationConfig;
import com.greentin.enovation.model.UserMenuAccess;
import com.greentin.enovation.security.ExecuteApache;
import com.greentin.enovation.setup.SetupConfigDao;
import com.greentin.enovation.utils.BaseRepository;
import com.greentin.enovation.utils.CommonUtils;
import com.greentin.enovation.utils.EnovationConstants;
import com.greentin.enovation.utils.MailUtil;

@SuppressWarnings("unchecked")
@Repository
@Component
@Transactional
public class UserDao extends BaseRepository implements UserDaoInterface {

	private static final Logger LOGGER = LoggerFactory.getLogger(UserDao.class);

	@Autowired
	AuditComponent audit;

	@Autowired
	EnovationConfig enoConfig;

	@Autowired
	PasswordEncoder pass;

	@Autowired
	IMasterDao iMasterDao;

	@Autowired
	@Qualifier("taskExecutor")
	private TaskExecutor taskExecutor;

	@Autowired
	EmployeeDaoImple emd;

	@Autowired
	MasterDaoImpl masterDaoImpl;

	@Autowired
	SetupConfigDao SetupConfigDaoInterface;

	@Autowired
	ExecuteApache execute;

	@Autowired
	private ICommunication communication;

	@Override
	public void updateFCMKey(int empId, String fcmKey) {
		LOGGER.info("employeeDetails . empId : : " + empId + "  " + fcmKey);
		List<EmployeeDetails> empList = getCurrentSession().createQuery("FROM EmployeeDetails WHERE empId=:empId")
				.setParameter("empId", empId).getResultList();
		if (empList != null && empList.size() > 0) {
			if (fcmKey != null) {
				empList.get(0).setFcmKey(fcmKey);
			}
			empList.get(0).setLoggedIn(EnovationConstants.ONE);
			getCurrentSession().update(empList.get(0));
		}
		/*
		 * getCurrentSession().
		 * createQuery("update EmployeeDetails set fcmKey=:fcmKey and loggedIn=1 where empId=:empId "
		 * ) .setParameter("empId", empId) .setParameter("fcmKey", fcmKey)
		 * .executeUpdate();
		 */
	}

	@Override
	public boolean saveProduct(Product product) {
		boolean flag = false;
		LOGGER.info("#INSIDE IN SAVEPRODUCT ");
		Session session = getCurrentSession();
		try {
			product.setStatus(EnovationConstants.ENABLE_STATUS);
			product.setCreateDate(CommonUtils.currentDate());
			session.save(product);
			flag = true;
		} catch (Exception e) {
			LOGGER.error("#INSIDE EXCEPTION OCCURED IN SAVEPRODUCT " + e.getMessage());
		}
		return flag;
	}

	@Override
	public boolean isProductExists(Product product) {
		boolean flag = false;
		LOGGER.info("#INSIDE IN isProductExists ");
		Session session = getCurrentSession();
		try {
			List<Object> object = session.createQuery("from Product WHERE name=:name")
					.setParameter("name", product.getName()).getResultList();
			if (object != null && object.size() > 0) {
				flag = true;
			}

		} catch (Exception e) {
			LOGGER.error("#INSIDE EXCEPTION OCCURED IN isProductExists " + e.getMessage());
		}
		return flag;
	}

	@Override
	public boolean updateProduct(Product product) {
		boolean flag = false;
		LOGGER.info("#INSIDE IN UPDATEPRODUCT ");
		Session session = getCurrentSession();
		try {
			Product updateProduct = (Product) session.get(Product.class, product.getId());
			if (product.getName() != null) {
				updateProduct.setName(product.getName());
			}
			if (product.getDescription() != null) {
				updateProduct.setDescription(product.getDescription());
			}
			if (product.getStatus() != 0) {
				updateProduct.setStatus(product.getStatus());
			}
			updateProduct.setUpdateDate(CommonUtils.currentDate());
			session.update(product);
			flag = true;
		} catch (Exception e) {
			LOGGER.error("#INSIDE EXCEPTION OCCURED IN UPDATEPRODUCT " + e.getMessage());
		}
		return flag;
	}

	@Override
	public List<Product> getProductList() {
		LOGGER.info("#INSIDE IN GETPRODUCTLIST ");
		List<Product> productList = null;
		try {
			productList = getCurrentSession().createQuery("from Product").getResultList();

		} catch (Exception e) {
			LOGGER.error("#INSIDE EXCEPTION OCCURED IN  GETPRODUCTLIST" + e.getMessage());
		}
		return productList;
	}

	@Override
	public List<Product> getActiveProductList() {
		LOGGER.info("#INSIDE IN GETACTIVEPRODUCTLIST ");
		List<Product> productList = null;
		try {
			productList = getCurrentSession().createQuery("from Product WHERE status=:status")
					.setParameter("status", EnovationConstants.ENABLE_STATUS).getResultList();

		} catch (Exception e) {
			LOGGER.error("#INSIDE EXCEPTION OCCURED IN  GETACTIVEPRODUCTLIST" + e.getMessage());
		}
		return productList;
	}

	@Override
	public List<Menu> getMenuListByProductId(Integer productId) {
		LOGGER.info("#INSIDE IN GETMENULISTBYPRODUCTID ");
		List<Menu> menuList = null;
		try {
			menuList = getCurrentSession().createQuery("from Menu WHERE product_id=:productId")
					.setParameter("productId", productId).getResultList();

		} catch (Exception e) {
			LOGGER.error("#INSIDE EXCEPTION OCCURED IN  GETMENULISTBYPRODUCTID" + e.getMessage());
		}
		return menuList;
	}

	@Override
	public boolean isMenuExists(Menu menu) {
		boolean flag = false;
		LOGGER.info("#INSIDE IN ISMENUEXISTS ");
		Session session = getCurrentSession();
		try {
			List<Object> object = session.createQuery("from Menu WHERE menuName=:name")
					.setParameter("name", menu.getMenuName()).getResultList();
			if (object != null && object.size() > 0) {
				flag = true;
			}

		} catch (Exception e) {
			LOGGER.error("#INSIDE EXCEPTION OCCURED IN ISMENUEXISTS " + e.getMessage());
		}
		return flag;
	}

	@Override
	public boolean saveMenu(Menu menu) {
		boolean flag = false;
		LOGGER.info("#INSIDE IN SAVEPRODUCT ");
		Session session = getCurrentSession();
		try {
			menu.setProduct(new Product(menu.getProductId()));
			menu.setCreateDate(CommonUtils.currentDate());
			session.save(menu);
			flag = true;
		} catch (Exception e) {
			LOGGER.error("#INSIDE EXCEPTION OCCURED IN SAVEPRODUCT " + e.getMessage());
		}
		return flag;
	}

	@Override
	public boolean updateMenu(Menu menu) {
		boolean flag = false;
		LOGGER.info("#INSIDE IN UPDATEMENU ");
		Session session = getCurrentSession();
		try {
			Menu updateMenu = (Menu) session.get(Menu.class, menu.getId());
			if (menu.getMenuName() != null) {
				updateMenu.setMenuName(menu.getMenuName());
			}
			// if(menu.get!=null) {updateMenu.setDescription(menu.getDescription());}
			if (menu.getStatus() != 0) {
				updateMenu.setStatus(menu.getStatus());
			}
			updateMenu.setUpdateDate(CommonUtils.currentDate());
			session.update(updateMenu);
			flag = true;
		} catch (Exception e) {
			LOGGER.error("#INSIDE EXCEPTION OCCURED IN UPDATEPRODUCT " + e.getMessage());
		}
		return flag;
	}

	@Override
	public boolean isSubMenuExists(SubMenu submenu) {
		boolean flag = false;
		LOGGER.info("#INSIDE IN ISSUBMENUEXISTS ");
		Session session = getCurrentSession();
		try {
			List<Object> object = session.createQuery("from SubMenu WHERE subMenuName=:name")
					.setParameter("name", submenu.getSubMenuName()).getResultList();
			if (object != null && object.size() > 0) {
				flag = true;
			}

		} catch (Exception e) {
			LOGGER.error("#INSIDE EXCEPTION OCCURED IN ISSUBMENUEXISTS " + e.getMessage());
		}
		return flag;
	}

	@Override
	@Transactional
	public List<EmployeeDetails> isUseremailExists(String email) {
		LOGGER.info("#INSIDE IN ISUSEREMAILEXISTS ");
		Session session = getCurrentSession();
		List<EmployeeDetails> object = null;
		try {
			object = session
					.createQuery("from EmployeeDetails WHERE (emailId=:email or cmpyEmpId=:email) and isDeactive=0 ")
					.setParameter("email", email).getResultList();
			System.out.println("size :" + object.size());
			if (object != null && object.size() > 0) {
				String token = String.valueOf(CommonUtils.generateToken(25));
				object.get(0).setToken(token);
				session.update(object.get(0));
				emd.sendMailToImportedEmployees(object.get(0).getEmailId(), object.get(0).getFirstName(),
						object.get(0).getLastName(), token, EnovationConstants.FORGOT_PASSWORD);
			}
		} catch (Exception e) {
			LOGGER.error("#INSIDE EXCEPTION OCCURED IN ISUSEREMAILEXISTS " + e.getMessage());
		}
		return object;
	}

	@Override
	public boolean saveSubMenu(SubMenu submenu) {
		boolean flag = false;
		LOGGER.info("#INSIDE IN SAVESUBMENU ");
		Session session = getCurrentSession();
		try {
			submenu.setStatus(EnovationConstants.ENABLE_STATUS);
			submenu.setMenu(new Menu(submenu.getMenuId()));
			submenu.setCreateDate(CommonUtils.currentDate());
			session.save(submenu);
			flag = true;
		} catch (Exception e) {
			LOGGER.error("#INSIDE EXCEPTION OCCURED IN SAVEPRODUCT " + e.getMessage());
		}
		return flag;
	}

	@Override
	public boolean updateSubMenu(SubMenu submenu) {
		boolean flag = false;
		LOGGER.info("#INSIDE IN UPDATESUBMENU ");
		Session session = getCurrentSession();
		try {
			SubMenu updateSubMenu = (SubMenu) session.get(SubMenu.class, submenu.getId());
			if (submenu.getSubMenuName() != null) {
				updateSubMenu.setSubMenuName(submenu.getSubMenuName());
			}
			// if(menu.get!=null) {updateMenu.setDescription(menu.getDescription());}
			updateSubMenu.setStatus(submenu.getStatus());
			if (submenu.getMenuId() != 0) {
				updateSubMenu.setMenu(new Menu(submenu.getMenuId()));
			}
			updateSubMenu.setUpdateDate(CommonUtils.currentDate());
			session.update(updateSubMenu);
			flag = true;
		} catch (Exception e) {
			LOGGER.error("#INSIDE EXCEPTION OCCURED IN UPDATESUBMENU " + e.getMessage());
		}
		return flag;
	}

	@Override
	public boolean disableSubMenu(SubMenu submenu) {
		boolean flag = false;
		LOGGER.info("#INSIDE IN UPDATE SUBMENU ");
		Session session = getCurrentSession();
		try {
			SubMenu disableSubMenu = (SubMenu) session.get(SubMenu.class, submenu.getId());
			disableSubMenu.setStatus(submenu.getStatus());
			disableSubMenu.setUpdateDate(CommonUtils.currentDate());
			session.update(disableSubMenu);
			flag = true;
		} catch (Exception e) {
			LOGGER.error("#INSIDE EXCEPTION OCCURED IN UPDATE SUBMENU " + e.getMessage());
		}
		return flag;
	}

	@Override
	public boolean disableProduct(Product product) {
		boolean flag = false;
		LOGGER.info("#INSIDE IN DISABLE PRODUCT");
		Session session = getCurrentSession();
		try {
			SubMenu disableSubMenu = (SubMenu) session.get(SubMenu.class, product.getId());
			disableSubMenu.setStatus(product.getStatus());
			disableSubMenu.setUpdateDate(CommonUtils.currentDate());
			session.update(disableSubMenu);
			flag = true;
		} catch (Exception e) {
			LOGGER.error("#INSIDE EXCEPTION OCCURED IN UPDATE SUBMENU " + e.getMessage());
		}
		return flag;
	}

	@Override
	public boolean registration(Registration user) {
		boolean flag = false;
		LOGGER.info("#INSIDE IN REGISTRATION ");
		System.out.println("Alies name is :" + user.getAlies());
		Session session = getCurrentSession();
		// String token=String.valueOf(CommonUtils.generateToken(16));
		try {

			/*
			 * add organization in content setup
			 */

			OrganizationMaster org = new OrganizationMaster();
			org.setName(user.getOrgName());
			org.setOrgDomain(user.getOrgDomain());
			org.setAlies(user.getAlies());
			if (user.getAlies() != null) {
				org.setPortalLink((EnovationConfig.buddyConfig.get("masterDomain")).replaceAll(Pattern.quote("{alies}"),
						user.getAlies()));
			}
			if (user.getAlies() != null) {
				org.setServerDomainName((EnovationConfig.buddyConfig.get("serverDomain"))
						.replaceAll(Pattern.quote("{alies}"), user.getAlies()));
			}
			org.setCreatedDate(CommonUtils.currentDate());
			session.save(org);

			/*
			 * add branch in content setup
			 */

			BranchMaster branch = new BranchMaster();
			branch.setLocation(user.getLocation());
			branch.setOrg(new OrganizationMaster(org.getOrgId()));
			branch.setCreatedDate(CommonUtils.currentDate());
			session.save(branch);

			/*
			 * add Designation in content setup
			 */
			/*
			 * DesignationMaster desig=new DesignationMaster();
			 * desig.setDesigName(user.getDesignation()); desig.setBranch(new
			 * BranchMaster(branch.getBranchId())); session.save(desig);
			 */

			/*
			 * add entry in product-org config table to map product with org
			 */
			ProductOrgConfig prorg = new ProductOrgConfig();
			prorg.setOrg(new OrganizationMaster(org.getOrgId()));
			prorg.setBranch(new BranchMaster(branch.getBranchId()));
			prorg.setProduct(new Product(user.getProductId())); // get From Data Base
			prorg.setSubscriptionPlan(new SubscriptionPlan(user.getSubscripId()));
			prorg.setDeactivationDate(CommonUtils.deactivateDate(15)); // Need to Replace Dynamic
			session.save(prorg);

			/*
			 * ADDED BRANCH SETUP CONFIG ENTRY WHILE CREATING A NEW BRANCH
			 */
			List<SetupMaster> setupMasterList = SetupConfigDaoInterface.listofSetupConfig();
			if (setupMasterList != null && setupMasterList.size() > 0) {
				for (SetupMaster details : setupMasterList) {
					BranchSetupConfig config = new BranchSetupConfig();
					config.setBranchMaster(new BranchMaster(branch.getBranchId()));
					config.setSetupMaster(new SetupMaster(details.getSetupId()));
					session.save(config);
				}

			}

			/*
			 * Author : Vinay B
			 * 
			 * Data & TimeStamp : 25/06/2019 03:00 PM
			 * 
			 * Description : Add menu details inside module subscription.
			 * 
			 * 
			 */

			List<Object[]> menuList = session.createNativeQuery("SELECT id,menu_name FROM menu").getResultList();

			menuList.stream().forEach(x -> {
				ModuleSubscription data = new ModuleSubscription(new Menu(Integer.parseInt(String.valueOf(x[0]))),
						branch.getBranchId(), org.getOrgId(), "Y");
				session.save(data);
			});

			EmployeeDetails empDetails = new EmployeeDetails();
			empDetails.setFirstName(user.getName());
			empDetails.setOrganization(new OrganizationMaster(org.getOrgId()));
			empDetails.setBranch(new BranchMaster(branch.getBranchId()));
			// empDetails.setDesignation(new DesignationMaster(desig.getDesigId()));
			empDetails.setEmailId(user.getEmail());
			if (user.getDesignation() != null) {
				empDetails.setDesignation(user.getDesignation());
			}
			/*
			 * added flag for email verified successfully
			 */
			empDetails.setIsEmailVerified(EnovationConstants.ONE);
			if (user.getContactNumber() != null) {
				empDetails.setContactNo(user.getContactNumber());
			}
			empDetails.setCreatedDate(CommonUtils.currentDate());
			Role role = isRole((RoleName.SUPERADMIN).toString());
			empDetails.setRoles(Collections.singleton(role));
			empDetails.setToken(user.getToken());
			empDetails.setPassword(user.getPassword());
			empDetails.setIsNotify(EnovationConstants.ONE);
			@SuppressWarnings("unused")
			Object obj = session.save(empDetails);

			/*
			 * Role roleEmployee=new Role(); roleEmployee.setId((long)
			 * EnovationConstants.EMPLOYEE_ROLEID);
			 */
			masterDaoImpl.saveuserrole(empDetails.getEmpId(), EnovationConstants.EMPLOYEE_ROLEID);

			List<String> masterBenifitList = iMasterDao.getBenefitAnalysisMasterList();
			if (masterBenifitList != null && masterBenifitList.size() > 0) {
				for (String benefitAnalysisMaster : masterBenifitList) {
					BenefitAnalysisMaster abcd = new BenefitAnalysisMaster();
					abcd.setBenefits(benefitAnalysisMaster);
					abcd.setOrg(new OrganizationMaster(org.getOrgId()));
					abcd.setBranch(new BranchMaster(branch.getBranchId()));
					abcd.setEmpDetails(new EmployeeDetails(empDetails.getEmpId()));
					session.save(abcd);
					// iMasterDao.addbenefitmaster(benefitAnalysisMaster);
				}
			}
			List<CategoryMaster> categoryList = iMasterDao.getMasterCatagoryList();
			if (categoryList != null && categoryList.size() > 0) {
				for (CategoryMaster categoryMaster : categoryList) {
					CategoryMaster cat = new CategoryMaster();
					cat.setCategoryName(categoryMaster.getCategoryName());
					cat.setOrganisation(new OrganizationMaster(org.getOrgId()));
					cat.setBranch(new BranchMaster(branch.getBranchId()));
					session.save(cat);
					List<String> particularsList = iMasterDao.getParticularsList(categoryMaster.getCatId());
					if (particularsList != null && particularsList.size() > 0) {
						for (String particulars : particularsList) {
							Particulars par = new Particulars();
							par.setCategory(new CategoryMaster(cat.getCatId()));
							par.setParticulars(particulars);
							par.setCreatedById(empDetails.getEmpId());
							session.save(par);
						}
					}
				}
			}
			List<SavingOptions> savingOptList = iMasterDao.getSavingOptionsList();
			if (savingOptList != null && savingOptList.size() > 0) {
				for (SavingOptions opt : savingOptList) {
					SavingOptions save_savingOpt = new SavingOptions(opt.getName(), empDetails.getEmpId(),
							new BranchMaster(branch.getBranchId()));
					session.save(save_savingOpt);
				}
			}

			boolean status = execute.WriteConfigFileOnServer(org.getServerDomainName());
			if (status) {
				execute.executeFile(EnovationConfig.buddyConfig.get("apache_command"));
			}

			boolean result = isRoleExist(branch.getBranchId(), org.getOrgId(), role.getId());
			/*
			 * if superadmin role is already exist in data base
			 */
			if (!result) {
				Set<RoleMenuAccess> roleData = getRoleMenuAccess(user.getProductId(), branch.getBranchId(),
						org.getOrgId(), role.getId());
				if (roleData != null && roleData.size() > 0) {
					saveRoleAccessList(roleData);
				}

				Set<RoleMenuAccess> roleDataEmployee = getRoleMenuAccessEmployee(user.getProductId(),
						branch.getBranchId(), org.getOrgId(), EnovationConstants.EMPLOYEE_ROLEID);
				if (roleDataEmployee != null && roleDataEmployee.size() > 0) {
					saveRoleAccessList(roleDataEmployee);
				}
				/*
				 * ADD ESCALATION ALERT
				 */
				for (Long roleIds : EnovationConstants.ESCALATION_ROLE_LIST) {
					RoleEscalationSetup setup = new RoleEscalationSetup(new Role(roleIds),
							new BranchMaster(branch.getBranchId()));
					/*
					 * setup.setRole(new Role(roleIds)); setup.setBranchMaster(new
					 * BranchMaster(branch.getBranchId()));
					 * setup.setNumberOfDays(EnovationConstants.ZERO);
					 */
					session.save(setup);
					SuggestionEscalationConfig sEConfig = new SuggestionEscalationConfig(
							new BranchMaster(branch.getBranchId()));
					session.save(sEConfig);
					BrowniePointsReimbursementCycle browniePointConfig = new BrowniePointsReimbursementCycle(
							new BrowniePointsReimbursementCycleType(EnovationConstants.BROWNIE_POINTS_NONE),
							new BranchMaster(branch.getBranchId()));
					session.save(browniePointConfig);
				}
			}
			user.setIsActive(EnovationConstants.ONE);
			user.setIsEmailVerified(EnovationConstants.ONE);
			session.update(user);
			// emd.sendMailToImportedEmployees(empDetails.getEmailId(),empDetails.getFirstName(),EnovationConstants.REGISTER,token);
			flag = true;
		} catch (Exception e) {
			e.printStackTrace();
			LOGGER.error("#INSIDE EXCEPTION OCCURED IN REGISTER USER " + e.getMessage());
		}
		return flag;
	}

	public Set<RoleMenuAccess> getRoleMenuAccessEmployee(int productId, int branchId, int orgId, int eMPLOYEE_ROLEID) {
		Set<RoleMenuAccess> roleSet = new HashSet<RoleMenuAccess>();
		RoleMenuAccess roleAccess = null;
		StringBuffer str = new StringBuffer(
				"SELECT sub_menu.menu_id,sub_menu.id FROM menu LEFT JOIN sub_menu ON menu.id = sub_menu.menu_id ");
		str.append(" WHERE sub_menu.sub_menu_name =:name and sub_menu.status =1");
		Session session = getCurrentSession();
		try {
			Query query = session.createNativeQuery(str.toString()).setParameter("name", EnovationConstants.SUGGESTION);
			List<Object[]> rows = query.getResultList();
			for (Object[] row : rows) {
				roleAccess = new RoleMenuAccess();
				roleAccess.setBranchId(branchId);
				roleAccess.setOrgId(orgId);
				roleAccess.setRoleId((int) eMPLOYEE_ROLEID);
				roleAccess.setIsActive(EnovationConstants.ENABLE_STATUS);
				roleAccess.setMenuId(Integer.parseInt(row[0].toString()));
				roleAccess.setSubmenuId(Integer.parseInt(row[1].toString()));
				roleSet.add(roleAccess);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return roleSet;
	}

	public boolean isRoleExist(int branchId, int orgId, long roleId) {
		StringBuffer str = new StringBuffer(
				"select * from role_menu_access where role_id = :roleId and branch_id = :branchId and org_id = :orgId");
		Session session = getCurrentSession();
		boolean result = false;
		try {
			Query query = session.createNativeQuery(str.toString()).setParameter("branchId", branchId)
					.setParameter("orgId", orgId).setParameter("roleId", roleId);
			List<Object[]> rows = query.getResultList();
			if (rows != null && rows.size() > 0) {
				result = true;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}

	public Set<RoleMenuAccess> getRoleMenuAccess(int productId, int branchId, int orgId, long roleId) {
		Set<RoleMenuAccess> roleSet = new HashSet<RoleMenuAccess>();
		RoleMenuAccess roleAccess = null;
		StringBuffer str = new StringBuffer(
				"SELECT sub_menu.menu_id,sub_menu.id FROM menu LEFT JOIN sub_menu ON menu.id = sub_menu.menu_id ");
		str.append(" WHERE menu.product_id = :productId and menu.status =1 and sub_menu.status =1");
		Session session = getCurrentSession();
		try {
			Query query = session.createNativeQuery(str.toString()).setParameter("productId", productId);
			List<Object[]> rows = query.getResultList();
			for (Object[] row : rows) {
				roleAccess = new RoleMenuAccess();
				roleAccess.setBranchId(branchId);
				roleAccess.setOrgId(orgId);
				roleAccess.setRoleId((int) roleId);
				roleAccess.setIsActive(EnovationConstants.ENABLE_STATUS);
				roleAccess.setMenuId(Integer.parseInt(row[0].toString()));
				roleAccess.setSubmenuId(Integer.parseInt(row[1].toString()));
				roleSet.add(roleAccess);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return roleSet;
	}

	private Role isRole(String rolename) {
		Session session = getCurrentSession();
		List<Role> roleDetails = session.createNativeQuery("SELECT * FROM roles WHERE name=:name").addEntity(Role.class)
				.setParameter("name", rolename).getResultList();
		return roleDetails.get(0);
	}

	@SuppressWarnings("unused")
	@Override
	public Registration verifyEmail(Registration user) {
		String email = null;
		Session session = getCurrentSession();
		List<Registration> registrationList = new ArrayList<>();
		try {
			registrationList = session
					.createQuery(" FROM Registration WHERE token=:token and isActive=0 and isEmailVerified=0  ")
					.setParameter("token", user.getToken()).getResultList();

			if (registrationList != null && registrationList.size() > 0) {
				registrationList.get(0).setIsEmailVerified(EnovationConstants.ENABLE_STATUS);
				registrationList.get(0).setIsActive(EnovationConstants.ENABLE_STATUS);
				session.update(registrationList.get(0));
				/*
				 * SET PORATAL LINK FOR RESPONSE REDIRCT URL
				 */
				String portalLink = (EnovationConfig.buddyConfig.get("masterDomain"))
						.replaceAll(Pattern.quote("{alies}"), registrationList.get(0).getAlies());
				registrationList.get(0).setPortalLink(portalLink);

				// check orgDain is exists or not in organization table
				boolean isOrgDomaimExists = isOrgDomaimExists(registrationList.get(0));
				if (!isOrgDomaimExists) {
					// if orgDomain is not exists then =>

					boolean flag = registration(registrationList.get(0));
				} else {
					// check email is exists or not in employee table
					List<EmployeeDetails> list = isEmployeeEmailExists(email);
					if (list != null && list.size() > 0) {
						// --yes email already exist please check your mails or login with your cred.
						taskExecutor.execute(new MailUtil(registrationList.get(0).getEmail(), "Already Register User",
								"please check your mails or login with your cred.", communication));
					} else {
						// get org id and branch (records) from employee table to this particular org.
						List<EmployeeDetails> empList = getEmpListByOrg(registrationList.get(0).getOrgDomain());
						if (empList != null && empList.size() > 0) {
							System.out.println("INSIDE empList");
							// if org exists and email not exist condition true =>
							// save user as a role is superadmin
							boolean saveRegUser = saveRegUser(empList.get(0), registrationList.get(0));
							System.out.println("true /f :" + saveRegUser);
						}
					}
				}
				return registrationList.get(0);
			}
		} catch (Exception e) {
			e.printStackTrace();
			LOGGER.error("#INSIDE EXCEPTION OCCURED IN VERIFYEMAIL " + e.getMessage());
		}
		return null;
	}

	@Override
	public boolean saveUser(Users user) {
		boolean flag = false;
		LOGGER.info("#INSIDE IN USER ");
		Session session = getCurrentSession();
		String password = String.valueOf(CommonUtils.GenerateRandomPassword(8));
		String token = String.valueOf(CommonUtils.generateToken(16));
		try {
			// add user in users table
			user.setRegDate(CommonUtils.currentDate());
			// add role for this employee //
			Role role = isRole(user.getRoleName());
			LOGGER.info("ROLE OBJ VAL IS :" + role);
			// user.setRoles(Collections.singleton(role));
			user.setToken(token);
			user.setPassword(pass.encode(password));
			session.save(user);
			System.out.println("NEW PASSWORD FGOR USER: " + password);
			user.setPassword(password);
			flag = true;
		} catch (Exception e) {
			e.printStackTrace();
			LOGGER.error("#INSIDE EXCEPTION OCCURED IN USER1 " + e.getMessage());
		}
		return flag;
	}

	@Override
	public List<Users> getRegisterUserList() {

		return getCurrentSession().createNativeQuery("SELECT * FROM users where org_id >= :id").addEntity(Users.class)
				.setParameter("id", EnovationConstants.ENABLE_STATUS).getResultList();
	}

	@Override
	public List<Users> getGreentinUserList() {
		return getCurrentSession().createNativeQuery("SELECT * FROM users where org_id is null ").addEntity(Users.class)
				.getResultList();
	}

	@Override
	public boolean checkEmailVerified(Registration user) {
		LOGGER.info("#INSIDE IN CHECKEMAILVERIFIED");
		boolean flag = false;
		Object obj = getCurrentSession().createQuery("FROM Registration WHERE token=:token")
				.setParameter("token", user.getToken()).getResultList();
		if (obj != null) {
			flag = true;
		}
		return flag;
	}

	@Override
	public List<Users> login(Users user) {
		List<Users> list = null;
		LOGGER.info("#INSIDE IN LOGIN");
		Session session = getCurrentSession();
		try {
			list = session.createQuery("FROM Users WHERE email=:email").setParameter("email", user.getEmail())
					.getResultList();

		} catch (Exception e) {
			LOGGER.error("#INSIDE EXCEPTION OCCURED IN LOGIN " + e.getMessage());
		}
		return list;
	}

	@Override
	public boolean isEmailverified(Users user) {
		boolean flag = false;
		LOGGER.info("#INSIDE IN ISEMAILVERIFIED");
		Session session = getCurrentSession();
		try {
			List<Users> list = session.createQuery("FROM Users WHERE email=:email")
					.setParameter("email", user.getEmail()).getResultList();
			if (list != null && list.size() > 0) {
				if (list.get(0).getIsEmailVerified() == EnovationConstants.ENABLE_STATUS) {
					flag = true;
				}
			}

		} catch (Exception e) {
			LOGGER.error("#INSIDE EXCEPTION OCCURED IN ISEMAILVERIFIED " + e.getMessage());
		}
		return flag;
	}

	@Override
	public List<Role> getListofRole() {
		List<Role> list = null;
		LOGGER.info("#INSIDE IN GETLISTOFROLE");
		Session session = getCurrentSession();
		try {
			list = session.createQuery("FROM Role").getResultList();
		} catch (Exception e) {
			LOGGER.error("#INSIDE EXCEPTION OCCURED IN GETLISTOFROLE " + e.getMessage());
		}
		return list;
	}

	@Override
	public boolean saveRoleMenuAccess(RoleMenuAccess role) {
		boolean flag = false;
		LOGGER.info("#INSIDE IN SAVEROLEMENUACCESS");
		Session session = getCurrentSession();
		try {
			role.setIsActive(EnovationConstants.ENABLE_STATUS);
			role.setOrg(new OrganizationMaster(role.getOrgId()));
			role.setBranch(new BranchMaster(role.getBranchId()));
			role.setRole(new Role((long) role.getRoleId()));
			role.setMenu(new Menu(role.getMenuId()));
			role.setSubmenu(new SubMenu(role.getSubmenuId()));
			role.setCreatedDate(CommonUtils.currentDate());
			session.save(role);
			flag = true;
		} catch (Exception e) {
			LOGGER.error("#INSIDE EXCEPTION OCCURED IN SAVEROLEMENUACCESS " + e.getMessage());
		}
		return flag;
	}

	@Override
	public boolean updateRoleMenuAccess(RoleMenuAccess role) {
		boolean flag = false;
		LOGGER.info("#INSIDE IN UPDATEROLEMENUACCESS");
		Session session = getCurrentSession();
		try {
			System.out.println("isActive val is :" + role.getIsActive());
			System.out.println("role id && isActive val is :" + role.getRoleId() + " " + role.getIsActive());
			// RoleMenuAccess roleinfo=(RoleMenuAccess)session.get(RoleMenuAccess.class,
			// role.getId());
			role.setRole(new Role((long) role.getRoleId()));
			role.setMenu(new Menu(role.getMenuId()));
			role.setSubmenu(new SubMenu(role.getSubmenuId()));
			role.setIsActive(role.getIsActive());
			role.setOrg(new OrganizationMaster(role.getOrgId()));
			role.setBranch(new BranchMaster(role.getBranchId()));
			session.saveOrUpdate(role);
			flag = true;
		} catch (Exception e) {
			LOGGER.error("#INSIDE EXCEPTION OCCURED IN UPDATEROLEMENUACCESS " + e.getMessage());
		}
		return flag;
	}

	@Override
	public List<RoleMenuAccess> getListofRoleAccess(Integer orgId, Integer branchId, Integer roleId) {
		List<RoleMenuAccess> list = null;
		LOGGER.info("#INSIDE IN GETLISTOFROLEACCESS");
		Session session = getCurrentSession();
		// getMenuListByRoleId(orgId,branchId,roleId);
		try {
			if (orgId != 0 && branchId != 0 && roleId != 0) {
				list = session.createQuery(
						"FROM RoleMenuAccess WHERE org.orgId=:orgId and branch.branchId=:branchId and role.id =:roleId")
						.setParameter("orgId", orgId).setParameter("branchId", branchId)
						.setParameter("roleId", (long) roleId).getResultList();
			} else if (orgId != 0) {
				list = session.createQuery("FROM RoleMenuAccess WHERE org.orgId=:orgId").setParameter("orgId", orgId)
						.getResultList();
			} else if (branchId != 0) {
				list = session.createQuery("FROM RoleMenuAccess WHERE branch.branchId=:branchId")
						.setParameter("branchId", branchId).getResultList();
			}

		} catch (Exception e) {
			LOGGER.error("#INSIDE EXCEPTION OCCURED IN GETLISTOFROLEACCESS " + e.getMessage());
		}
		getMenuListByRoleId(orgId, branchId, roleId);
		return list;
	}

	public List<MenuData> getMenuListByRoleId(Integer orgId, Integer branchId, Integer roleId) {
		StringBuffer str = new StringBuffer(
				"select a.is_active as statusId,sub.id as subMenuId,sub.sub_menu_name as subMenuName,m.menu_name AS menuName,m.id as menuId,a.id as arid");
		str.append(
				" from role_menu_access  a,sub_menu sub,menu m where sub.id=  a.submenu_id and m.id=  a.menu_id and ");
		str.append(" a.org_id = :orgId and a.branch_id = :branchId and a.role_id = :roleId ");
		System.out.println(str.toString());

		Session session = getCurrentSession();
		List<MyMenu> list = new ArrayList<MyMenu>();
		List<MenuData> menuList = null;
		try {
			Query query = session.createNativeQuery(str.toString()).setParameter("orgId", orgId)
					.setParameter("branchId", branchId).setParameter("roleId", roleId);
			List<Object[]> rows = query.getResultList();
			MyMenu data = null;
			for (Object[] row : rows) {
				data = new MyMenu();
				data.setStatusId(Integer.parseInt(row[0].toString()));
				data.setSubMenuId(Integer.parseInt(row[1].toString()));
				data.setSubMenuName(row[2].toString());
				data.setMenuName(row[3].toString());
				data.setMenuId(Integer.parseInt(row[4].toString()));
				data.setRoleAccessId(Integer.parseInt(row[5].toString()));
				list.add(data);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			// session.close();
		}
		// Map<Integer, Integer> choiceVotesMap =
		// list.stream().collect(Collectors.toMap(MenuData::getMenuId,
		// MenuData::getSubMenuId));
		// System.out.println(choiceVotesMap);
		if (list != null && list.size() > 0) {
			Map<Integer, List<MyMenu>> map = convertListToMap(list);
			menuList = convertMaptoMenuList(map);
		}
		return menuList;

	}

	@Override
	public List<MenuData> getMenuListByRoleIdNew(Integer orgId, Integer branchId, Integer roleId, Integer productId) {

		StringBuffer str = new StringBuffer("select  a.is_active as statusId,a.menu_id,a.submenu_id,a.id as accessId");
		str.append(" from role_menu_access  a where  ");
		str.append(" a.org_id = :orgId and a.branch_id = :branchId and a.role_id = :roleId");
		Session session = getCurrentSession();
		List<MenuData> roleListData = new ArrayList<MenuData>();
		try {
			// List<Menu> productMenuList=getMenuListByProductId(productId);//deprecated,now
			// we r checking menu with module

			List<Menu> productMenuList = getMenuSubsriptionList(branchId, orgId, session);
			System.out.println("Master Menu Size=" + productMenuList.size());
			Query query = session.createNativeQuery(str.toString()).setParameter("orgId", orgId)
					.setParameter("branchId", branchId).setParameter("roleId", roleId);
			List<Object[]> rows = query.getResultList();
			for (int i = 0; i < productMenuList.size(); i++) {
				Menu menu = productMenuList.get(i);
				MenuData menuData = new MenuData();
				menuData.setMenuId(menu.getId());
				menuData.setMenuName(menu.getMenuName());
				List<MyMenu> list1 = new ArrayList<MyMenu>();
				List<SubMenu> subMenuList = menu.getSubmenuList();
				for (int j = 0; j < subMenuList.size(); j++) {
					SubMenu tmpSubMenu = subMenuList.get(j);
					MyMenu myMenu = new MyMenu();
					myMenu.setMenuId(menu.getId());
					myMenu.setMenuName(menu.getMenuName());
					myMenu.setSubMenuId(tmpSubMenu.getId());
					myMenu.setSubMenuName(tmpSubMenu.getSubMenuName());
					for (Object[] row : rows) {
						if (menu.getId() == Integer.valueOf(String.valueOf(row[1]))
								&& tmpSubMenu.getId() == Integer.valueOf(String.valueOf(row[2]))) {
							myMenu.setStatusId(Integer.parseInt(row[0].toString()));
							myMenu.setRoleAccessId(Integer.parseInt(row[3].toString()));
							break;
						} else {
							myMenu.setStatusId(EnovationConstants.DESABLE_STATUS);
							myMenu.setRoleAccessId(EnovationConstants.ID_ZERO);
						}
					}
					list1.add(myMenu);
				}
				menuData.setSubMenuList(list1);
				roleListData.add(menuData);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			// session.close();
		}
		return roleListData;
	}

	public List<Menu> getMenuSubsriptionList(Integer branchId, Integer orgId, Session session) {
		LOGGER.info("#INSIDE IN getMenuSubsriptionList ");
		List<Menu> menuList = null;
		try {
			menuList = session.createNativeQuery(
					"select m.* from menu m INNER JOIN module_subscription ms on (ms.menu_id= m.id and ms.branch_id=:branchId) "
							+ " WHERE (ms.branch_id=:branchId or ms.org_id=:orgId) and ms.is_active='Y'")
					.addEntity(Menu.class).setParameter("branchId", branchId).setParameter("orgId", orgId)
					.getResultList();
		} catch (Exception e) {
			LOGGER.error("#INSIDE EXCEPTION OCCURED IN  getMenuSubsriptionList" + e.getMessage());
		}
		return menuList;
	}

	@SuppressWarnings("unused")
	public void accessControlMapToMenu(List<Menu> productMenuList, List<MenuData> accessData) {

		List<MenuData> roleListData = new ArrayList<MenuData>();
		for (int i = 0; i <= productMenuList.size(); i++) {
			Menu menu = productMenuList.get(i);
			MenuData menuData = new MenuData();
			menuData.setMenuId(menu.getId());
			menuData.setMenuName(menu.getMenuName());
			List<MyMenu> list = new ArrayList<MyMenu>();
			List<SubMenu> subMenuList = menu.getSubmenuList();
			for (int j = 0; j <= subMenuList.size(); j++) {

			}
		}
	}

	// Convert Map into Required JSON Format
	public List<MenuData> convertMaptoMenuList(Map<Integer, List<MyMenu>> mapObject) {
		List<MenuData> menuList = new ArrayList<MenuData>();
		MenuData menudata = null;
		for (Map.Entry<Integer, List<MyMenu>> entry : mapObject.entrySet()) {
			menudata = new MenuData();
			menudata.setMenuId(entry.getValue().get(0).getMenuId());
			menudata.setMenuName(entry.getValue().get(0).getMenuName());
			menudata.setSubMenuList(entry.getValue());
			menuList.add(menudata);
		}
		return menuList;
	}

	// Convert user List in Map
	public Map<Integer, List<MyMenu>> convertListToMap(List<MyMenu> list) {
		Map<Integer, List<MyMenu>> map = new HashMap<Integer, List<MyMenu>>();
		for (MyMenu menu : list) {
			if (map.containsKey(menu.getMenuId())) {
				List<MyMenu> menuList = map.get(menu.getMenuId());
				menuList.add(menu);
				map.put(menu.getMenuId(), menuList);
			} else {
				List<MyMenu> menuList = new ArrayList<MyMenu>();
				menuList.add(menu);
				map.put(menu.getMenuId(), menuList);
			}
		}
		return map;
	}

	@Override
	public boolean saveRoleAccessList(Set<RoleMenuAccess> roleAccess) {
		boolean flag = false;
		LOGGER.info("#INSIDE IN SAVEROLEMENUACCESS");
		Session session = getCurrentSession();
		try {
			for (RoleMenuAccess role : roleAccess) {

				role.setIsActive(role.getIsActive());
				role.setOrg(new OrganizationMaster(role.getOrgId()));
				role.setBranch(new BranchMaster(role.getBranchId()));
				role.setRole(new Role((long) role.getRoleId()));
				role.setMenu(new Menu(role.getMenuId()));
				role.setSubmenu(new SubMenu(role.getSubmenuId()));
				role.setCreatedDate(CommonUtils.currentDate());
				session.saveOrUpdate(role);
			}
			flag = true;
		} catch (Exception e) {
			LOGGER.error("#INSIDE EXCEPTION OCCURED IN SAVEROLEMENUACCESS " + e.getMessage());
		}
		return flag;
	}

	@Override
	public List<SubscriptionType> getListofSubscriptionType() {
		List<SubscriptionType> list = null;
		LOGGER.info("#INSIDE IN GETLISTOFROLE");
		Session session = getCurrentSession();
		try {
			list = session.createQuery("FROM SubscriptionType").getResultList();
		} catch (Exception e) {
			LOGGER.error("#INSIDE EXCEPTION OCCURED IN SubscriptionType " + e.getMessage());
		}
		return list;
	}

	@Override
	public boolean updateSetupComleted(ProductOrgConfig request) {
		boolean flag = false;
		LOGGER.info("#INSIDE IN UPDATESETUPCOMLETED");
		Session session = getCurrentSession();
		try {
			ProductOrgConfig list = (ProductOrgConfig) session.get(ProductOrgConfig.class, request.getId());
			list.setIsSetupCompleted(request.getIsSetupCompleted());
			session.update(list);
			System.out.println("RegistrationByPass FLAG :" + list.getRegistrationByPass());
			/*
			 * SEND VERIFICATION MAILS WHILE SETUP IS COMPLETED (SEND VERIFYCATION EMAIL TO
			 * EMPLOYEE)
			 */
			if (request.getIsSetupCompleted() == EnovationConstants.ONE) {
				List<EmployeeDetails> sendMailEmployeeList = session.createQuery(
						"FROM EmployeeDetails WHERE isNotify=:notify and branch.branchId=:branchId and isDeactive=0 ")
						.setParameter("notify", EnovationConstants.ZERO)
						.setParameter("branchId", list.getBranch().getBranchId()).getResultList();
				if (sendMailEmployeeList != null && sendMailEmployeeList.size() > 0) {
					boolean status = emd.sendBulkEmailtoEmployee(sendMailEmployeeList, request.getIsSetupCompleted(),
							list);
					if (status) {
						flag = true;
					}
				}

			}

		} catch (Exception e) {
			LOGGER.error("#INSIDE EXCEPTION OCCURED IN UPDATESETUPCOMLETED " + e.getMessage());
		}
		return flag;
	}

	@Override
	public List<MenuData> getMenuListByMultipleRoleId(int orgId, int branchId, Set<Role> roles) {

		StringBuffer str = new StringBuffer(
				"select  distinct sub.sub_menu_name as subMenuName , a.is_active as statusId,sub.id as subMenuId,m.menu_name AS menuName,m.id as menuId");
		str.append(
				" from role_menu_access  a,sub_menu sub,menu m where sub.id=  a.submenu_id and m.id=  a.menu_id and a.is_active=1 and ");
		str.append(" a.org_id = :orgId and a.branch_id = :branchId and a.role_id IN(:roleId) ");

		Session session = getCurrentSession();
		List<MyMenu> list = new ArrayList<MyMenu>();
		List<MenuData> menuList = null;
		try {
			Query query = session.createNativeQuery(str.toString()).setParameter("orgId", orgId)
					.setParameter("branchId", branchId).setParameter("roleId", roles);
			List<Object[]> rows = query.getResultList();
			MyMenu data = null;
			for (Object[] row : rows) {
				data = new MyMenu();
				data.setSubMenuName(row[0].toString());
				data.setStatusId(Integer.parseInt(row[1].toString()));
				data.setSubMenuId(Integer.parseInt(row[2].toString()));
				data.setMenuName(row[3].toString());
				data.setMenuId(Integer.parseInt(row[4].toString()));
				// data.setRoleAccessId(Integer.parseInt(row[5].toString()));
				list.add(data);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		if (list != null && list.size() > 0) {
			Map<Integer, List<MyMenu>> map = convertListToMap(list);
			menuList = convertMaptoMenuList(map);
		}
		return menuList;
	}

	/////////////////////////////////////////////////////////////////////////////////////////////////
	//
	// Function Name : getMenuListByModuleSubscription
	//
	// Input Parameter : int orgId, int branchId, Set<Role> roles
	//
	// Return Type : List<MenuData>
	//
	// Author & Time Stamp : Vinay B.
	//
	// Modified By & Time : 25/06/2019 01:00 PM
	//
	// Description : This method fetches list of menus as per modules subscribed by
	///////////////////////////////////////////////////////////////////////////////////////////////// the
	///////////////////////////////////////////////////////////////////////////////////////////////// user.
	//
	//////////////////////////////////////////////////////////////////////////////////////////////////

	@Override
	public List<MenuData> getMenuListByModuleSubscription(int orgId, int branchId, Set<Role> roles,
			EmployeeDetails emp) {

		StringBuffer str = new StringBuffer(
				"select  distinct sub.sub_menu_name as subMenuName , a.is_active as statusId,sub.id as subMenuId,m.menu_name AS menuName,m.id as menuId");
		str.append(
				" from role_menu_access  a,sub_menu sub,menu m INNER JOIN module_subscription ms ON ms.menu_id = m.id where sub.id=  a.submenu_id and m.id=  a.menu_id and a.is_active=1 and ");
		str.append(
				" a.org_id = :orgId and a.branch_id = :branchId and a.role_id IN(:roleId) AND ms.is_active = 'Y' and (ms.branch_id=:branchId and ms.org_id=:orgId)");

		Session session = getCurrentSession();
		List<MyMenu> list = new ArrayList<MyMenu>();
		List<MenuData> menuList = null;
		try {
			Query query = session.createNativeQuery(str.toString()).setParameter("orgId", orgId)
					.setParameter("branchId", branchId).setParameter("roleId", roles);
			List<Object[]> rows = query.getResultList();
			MyMenu data = null;
			for (Object[] row : rows) {
				data = new MyMenu();
				data.setSubMenuName(row[0].toString());
				data.setStatusId(Integer.parseInt(row[1].toString()));
				data.setSubMenuId(Integer.parseInt(row[2].toString()));
				data.setMenuName(row[3].toString());
				data.setMenuId(Integer.parseInt(row[4].toString()));
				// data.setRoleAccessId(Integer.parseInt(row[5].toString()));
				list.add(data);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		if (list != null && list.size() > 0) {
			Map<Integer, List<MyMenu>> map = convertListToMap(list);
			menuList = convertMaptoMenuList(map);
		}

		List<MenuData> userAccessList = getLoginUserAccessList(orgId, branchId, emp);

		/**
		 * merge role access & user access
		 * 
		 */
		if (userAccessList != null && !userAccessList.isEmpty()) {
			for (MenuData accessData : userAccessList) {
				boolean menuFlag = false;
				for (MenuData roleData : menuList) {
					if (roleData.getMenuId() == accessData.getMenuId()) {
						menuFlag = true;
						for (MyMenu accessMymenu : accessData.getSubMenuList()) {
							boolean submenuFlag = false;
							for (MyMenu roleMymenu : roleData.getSubMenuList()) {
								if (roleMymenu.getSubMenuId() == accessMymenu.getSubMenuId()) {
									submenuFlag = true;
								}
							}
							if (!submenuFlag) {
								roleData.getSubMenuList().add(accessMymenu);
							}
						}
					}
				}
				if (!menuFlag) {
					menuList.add(accessData);
				}
			}
		}

		return menuList;
	}

	/**
	 * @author rakesh 12 june 2020
	 * @param orgId
	 * @param branchId
	 * @param emp
	 * @return
	 */
	public List<MenuData> getLoginUserAccessList(int orgId, int branchId, EmployeeDetails emp) {

		StringBuilder str = new StringBuilder("SELECT \n" + "  sub.sub_menu_name AS subMenuName,\n"
				+ "  uma.access AS statusId,\n" + "  sub.id AS subMenuId,\n" + "  m.menu_name AS menuName,\n"
				+ "  m.id AS menuId,uma.emp_id\n" + "FROM\n" + "  user_menu_access uma\n"
				+ " INNER JOIN sub_menu sub ON sub.id = uma.sub_menu_id\n"
				+ " INNER JOIN menu m ON m.id = uma.menu_id\n"
				+ " INNER JOIN tbl_employee_details ed ON ed.emp_id = uma.emp_id\n"
				+ "WHERE uma.access = 'Y' AND uma.org_id =:orgId AND uma.branch_id =:branchId and uma.emp_id=:empId ");

		Session session = getCurrentSession();
		List<MyMenu> list = new ArrayList<>();
		List<MenuData> empAccessList = new ArrayList<>();

		Query query = session.createNativeQuery(str.toString()).setParameter("orgId", orgId)
				.setParameter("branchId", branchId).setParameter("empId", emp.getEmpId());
		List<Object[]> rows = query.getResultList();

		for (Object[] row : rows) {

			list.add(loginUserAccessListParser(row));
		}
		if (list != null && list.size() > 0) {

			/**
			 * Group By Using MenuId
			 */
			Map<Integer, List<MyMenu>> groupByTmpAccessList = list.stream()
					.collect(Collectors.groupingBy(MyMenu::getMenuId));

			empAccessList = loginUserAccessMaptoMenuList(groupByTmpAccessList);

		}
		return empAccessList;
	}

	/**
	 * @author rakesh 12 june 2020
	 */
	public List<MenuData> loginUserAccessMaptoMenuList(Map<Integer, List<MyMenu>> mapObject) {
		List<MenuData> menuList = new ArrayList<MenuData>();
		MenuData menudata = null;
		for (Map.Entry<Integer, List<MyMenu>> entry : mapObject.entrySet()) {
			menudata = new MenuData();
			menudata.setMenuId(entry.getValue().get(0).getMenuId());
			menudata.setMenuName(entry.getValue().get(0).getMenuName());
			menudata.setSubMenuList(entry.getValue());
			menuList.add(menudata);
		}
		return menuList;
	}

	/**
	 * @author rakesh 12 june 2020
	 * @param row
	 * @return
	 */
	private MyMenu loginUserAccessListParser(Object[] row) {

		MyMenu data = new MyMenu();
		EmployeeDetailsBO emp = new EmployeeDetailsBO();
		data.setSubMenuName(CommonUtils.objectToString(row[0]));

		int statusId = 0;
		if (row[1] != null) {
			statusId = ((row[1].toString()).equals("Y") ? 1 : 0);
			System.out.println("Status Id in if =>" + statusId);
		}
		System.out.println("Status Id =>" + row[1].toString());
		data.setStatusId(statusId);
		data.setSubMenuId(CommonUtils.objectToInt(row[2]));
		data.setMenuName(CommonUtils.objectToString(row[3]));
		data.setMenuId(CommonUtils.objectToInt(row[4].toString()));
		data.setEmpId(CommonUtils.objectToInt(row[5]));

		return data;
	}

	@Override
	public String forgotPassword(EmployeeDetails request) {
		String portallink = null;
		LOGGER.info("#INSIDE IN FORGOTPASSWORD ");
		Session session = getCurrentSession();
		try {
			List<EmployeeDetails> object = session
					.createQuery("from EmployeeDetails WHERE emailId=:email and isDeactive=0")
					.setParameter("email", request.getEmailId()).getResultList();
			if (object != null && object.size() > 0) {
				System.out.println(
						"Request token :" + request.getToken() + " == " + object.get(0).getToken() + " : DB TOKEN");
				if (request.getToken().equalsIgnoreCase(object.get(0).getToken())) {
					object.get(0).setPassword(pass.encode(request.getPassword()));
					session.update(object.get(0));
					portallink = object.get(0).getOrganization().getPortalLink();
					LOGGER.info("Portal Link=" + portallink);
				}
			}
		} catch (Exception e) {
			LOGGER.error("#INSIDE EXCEPTION OCCURED IN FORGOTPASSWORD " + e.getMessage());
		}
		return portallink;
	}

	@Override
	public boolean saveDemoRequest(DemoRequest request) {
		boolean flag = false;
		LOGGER.info("#INSIDE IN SAVEDEMOREQUEST ");
		Session session = getCurrentSession();
		try {
			Object obj = session.save(request);
			if (obj != null) {
				flag = true;
			}
		} catch (Exception e) {
			LOGGER.error("#INSIDE EXCEPTION OCCURED IN SAVEDEMOREQUEST " + e.getMessage());
		}
		return flag;
	}

	@Override
	public List<DemoRequest> demoRequestList() {
		List<DemoRequest> list = null;
		LOGGER.info("#INSIDE IN DEMOREQUESTLIST");
		Session session = getCurrentSession();
		try {
			list = session.createQuery("FROM DemoRequest").getResultList();
		} catch (Exception e) {
			LOGGER.error("#INSIDE EXCEPTION OCCURED IN DEMOREQUESTLIST " + e.getMessage());
		}
		return list;
	}

	@Override
	public boolean isDemoRequestEmailExists(String email) {
		boolean flag = false;
		List<DemoRequest> list = null;
		LOGGER.info("#INSIDE IN isDemoRequestEmailExists");
		Session session = getCurrentSession();
		try {
			list = session.createQuery("FROM DemoRequest WHERE email=:emailId").setParameter("emailId", email)
					.getResultList();
			if (list != null && list.size() > 0) {
				flag = true;
			}
		} catch (Exception e) {
			LOGGER.error("#INSIDE EXCEPTION OCCURED IN isDemoRequestEmailExists " + e.getMessage());
		}
		return flag;
	}

	@Override
	public boolean isOrgDomaimExists(Registration user) {
		boolean flag = false;
		Session session = getCurrentSession();
		List<OrganizationMaster> orgList = null;
		try {
			orgList = session.createQuery("FROM OrganizationMaster WHERE orgDomain=:orgDomain")
					.setParameter("orgDomain", user.getOrgDomain()).getResultList();
			if (orgList != null && orgList.size() > 0) {
				flag = true;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return flag;
	}

	@Override
	public boolean isRegistrationemailExists(String email) {
		boolean flag = false;
		LOGGER.info("#INSIDE IN isRegistrationemailExists ");
		Session session = getCurrentSession();
		try {
			List<Object> object = session.createQuery("from Registration WHERE email=:email")
					.setParameter("email", email).getResultList();
			System.out.println("size :" + object.size());
			if (object != null && object.size() > 0) {
				flag = true;
			}

		} catch (Exception e) {
			LOGGER.error("#INSIDE EXCEPTION OCCURED IN isRegistrationemailExists " + e.getMessage());
		}
		return flag;
	}

	@Override
	public boolean saveRegistration(Registration request) {
		boolean flag = false;
		LOGGER.info("#INSIDE IN SAVEREGISTRATION ");
		Session session = getCurrentSession();
		String token = String.valueOf(CommonUtils.generateToken(16));
		List<Object[]> data = new ArrayList<>();
		int count = 1;
		try {
			request.setToken(token);
			request.setCreatedDate(CommonUtils.currentDate());
			request.setPassword(pass.encode(request.getPassword()));
			// to enter registration source
			request.setRegSource(new RegistrationSource(request.getRegistrationSource()));
			@SuppressWarnings("unused")
			int regSource = request.getRegistrationSource();
			Object obj = session.save(request);
			if (obj != null) {
				emd.sendMailToImportedEmployees(request.getEmail(), request.getName(), request.getAlies(), token,
						EnovationConstants.REGISTER);
			}
			List<Registration> regCount = session.createQuery("from Registration").getResultList();
			if (regCount != null && regCount.size() > 0) {
				count = regCount.size();
				count++;
			}

			data = session.createNativeQuery("SELECT * FROM registration_source where id=:sourceId")
					.setParameter("sourceId", request.getRegistrationSource()).getResultList();

			EmailTemplateMaster messageContent = enoConfig.getMessageContent("DemoEscalation");
			String mailContent = "";
			if (messageContent.getBody() != null && messageContent.getBody().contains("{name}")) {

				mailContent = messageContent.getBody().replaceAll(Pattern.quote("{name}"), request.getName())
						.replaceAll(Pattern.quote("{contactNo}"), request.getContactNumber())
						.replaceAll(Pattern.quote("{country}"), request.getCountry())
						.replaceAll(Pattern.quote("{email}"), request.getEmail())
						.replaceAll(Pattern.quote("{designation}"), request.getDesignation())
						.replaceAll(Pattern.quote("{details}"), "New Registration")
						.replaceAll(Pattern.quote("{domain}"), request.getOrgDomain()).replaceAll(
								Pattern.quote("{regDate}"), String.valueOf(new Timestamp(System.currentTimeMillis())));
			}
			for (Object[] object : data) {
				taskExecutor.execute(new MailUtil(String.valueOf(object[2]),
						EnovationConstants.REGISTRATION_SUBJECT_NEW, mailContent, communication));
			}
			taskExecutor.execute(new MailUtil(EnovationConstants.RAJ_DUBAL,
					count + EnovationConstants.REGISTRATION_SUBJECT, mailContent, communication));
			taskExecutor.execute(new MailUtil(EnovationConstants.SACHIN_SHINDE,
					count + EnovationConstants.REGISTRATION_SUBJECT, mailContent, communication));
			taskExecutor.execute(new MailUtil(EnovationConstants.VINAY_B,
					count + EnovationConstants.REGISTRATION_SUBJECT, mailContent, communication));

			flag = true;
		} catch (Exception e) {
			LOGGER.error("#INSIDE EXCEPTION OCCURED IN  SAVEREGISTRATION " + e.getMessage());
			e.printStackTrace();
		}
		return flag;
	}

	@Override
	public List<EmployeeDetails> isEmployeeEmailExists(String email) {
		LOGGER.info("#INSIDE IN isEmployeeEmailExists ");
		Session session = getCurrentSession();
		List<EmployeeDetails> object = null;
		try {

			object = session.createQuery("from EmployeeDetails WHERE emailId=:email and isDeactive=0")
					.setParameter("email", email).getResultList();
			System.out.println("size :" + object.size());

		} catch (Exception e) {
			LOGGER.error("#INSIDE EXCEPTION OCCURED IN isEmployeeEmailExists " + e.getMessage());
		}
		return object;
	}

	@SuppressWarnings("unused")
	@Override
	public boolean saveRegUser(EmployeeDetails emp, Registration user) {

		LOGGER.info("#INSIDE IN USER ");
		boolean flag = false;
		Session session = getCurrentSession();
		// String token=String.valueOf(CommonUtils.generateToken(16));
		try {
			int branchId = 0;
			System.out.println("orgId :" + emp.getOrganization().getOrgId());
			System.out.println("user location :" + user.getLocation());
			// verify branch exists

			int branchIdInfo = verifyBranchInExistingOrg(emp.getOrganization().getOrgId(), user);
			branchId = branchIdInfo;
			// add branch in content setup
			// int
			// branchIdInfo=verifyBranchInExistingOrg(emp.getOrganization().getOrgId(),user);
			// Below Code is only applicable for P
			if (branchIdInfo != 0) {
				System.out.println("@INSIDE IN IF 1");
				branchId = branchIdInfo;
				System.out.println("branchId if 1 :" + branchId);
			} else {
				System.out.println("@INSIDE IN IF 2");
				BranchMaster addbranch = new BranchMaster();
				addbranch.setLocation(user.getLocation());
				addbranch.setOrg(new OrganizationMaster(emp.getOrganization().getOrgId()));
				addbranch.setCreatedDate(CommonUtils.currentDate());
				Object obj = session.save(addbranch);
				if (obj != null) {
					// add entry in product-org config table to map product with org
					ProductOrgConfig prorg = new ProductOrgConfig();
					prorg.setOrg(new OrganizationMaster(emp.getOrganization().getOrgId()));
					prorg.setBranch(new BranchMaster(addbranch.getBranchId()));
					prorg.setProduct(new Product(user.getProductId())); // get From Data Base
					prorg.setSubscriptionPlan(new SubscriptionPlan(user.getSubscripId()));
					prorg.setDeactivationDate(CommonUtils.deactivateDate(15)); // Need to Replace Dynamic
					session.save(prorg);

					/*
					 * ADDED BRANCH SETUP CONFIG ENTRY WHILE CREATING A NEW BRANCH
					 */
					List<SetupMaster> setupMasterList = SetupConfigDaoInterface.listofSetupConfig();
					if (setupMasterList != null && setupMasterList.size() > 0) {
						for (SetupMaster details : setupMasterList) {
							BranchSetupConfig config = new BranchSetupConfig();
							config.setBranchMaster(new BranchMaster(addbranch.getBranchId()));
							config.setSetupMaster(new SetupMaster(details.getSetupId()));
							session.save(config);
						}

					}

				}

				branchId = addbranch.getBranchId();
				System.out.println("branchId if 2 :" + branchId);
			}

			EmployeeDetails empDetails = new EmployeeDetails();
			empDetails.setFirstName(user.getName());
			empDetails.setOrganization(new OrganizationMaster(emp.getOrganization().getOrgId()));
			/*
			 * added flag for email verified successfully
			 */
			empDetails.setIsNotify(EnovationConstants.ONE);
			empDetails.setIsEmailVerified(EnovationConstants.ONE);
			empDetails.setBranch(new BranchMaster(branchId));
			empDetails.setEmailId(user.getEmail());
			if (user.getContactNumber() != null) {
				empDetails.setContactNo(user.getContactNumber());
			}
			empDetails.setCreatedDate(CommonUtils.currentDate());
			if (user.getDesignation() != null) {
				empDetails.setDesignation(user.getDesignation());
			}
			Role role = isRole((RoleName.SUPERADMIN).toString());
			empDetails.setRoles(Collections.singleton(role));
			empDetails.setPassword(user.getPassword());
			Object obj = session.save(empDetails);

			/*
			 * Role roleEmployee=new Role(); roleEmployee.setId((long)
			 * EnovationConstants.EMPLOYEE_ROLEID);
			 */
			masterDaoImpl.saveuserrole(empDetails.getEmpId(), EnovationConstants.EMPLOYEE_ROLEID);

			List<String> masterBenifitList = iMasterDao.getBenefitAnalysisMasterList();
			if (masterBenifitList != null && masterBenifitList.size() > 0) {
				for (String benefitAnalysisMaster : masterBenifitList) {
					BenefitAnalysisMaster abcd = new BenefitAnalysisMaster();
					abcd.setBenefits(benefitAnalysisMaster);
					abcd.setOrg(new OrganizationMaster(emp.getOrganization().getOrgId()));
					abcd.setBranch(new BranchMaster(branchId));
					abcd.setEmpDetails(new EmployeeDetails(empDetails.getEmpId()));
					session.save(abcd);
					// iMasterDao.addbenefitmaster(benefitAnalysisMaster);
				}
			}
			List<CategoryMaster> categoryList = iMasterDao.getMasterCatagoryList();
			if (categoryList != null && categoryList.size() > 0) {
				for (CategoryMaster categoryMaster : categoryList) {
					CategoryMaster cat = new CategoryMaster();
					cat.setCategoryName(categoryMaster.getCategoryName());
					cat.setOrganisation(new OrganizationMaster(emp.getOrganization().getOrgId()));
					cat.setBranch(new BranchMaster(branchId));
					session.save(cat);
					List<String> particularsList = iMasterDao.getParticularsList(categoryMaster.getCatId());
					if (particularsList != null && particularsList.size() > 0) {
						for (String particulars : particularsList) {
							Particulars par = new Particulars();
							par.setCategory(new CategoryMaster(cat.getCatId()));
							par.setParticulars(particulars);
							par.setCreatedById(empDetails.getEmpId());
							session.save(par);
						}
					}
				}
			}
			boolean result = isRoleExist(branchId, emp.getOrganization().getOrgId(), role.getId());
			/*
			 * if superadmin role is already exist in data base
			 */
			if (!result) {
				Set<RoleMenuAccess> roleData = getRoleMenuAccess(user.getProductId(), branchId,
						emp.getOrganization().getOrgId(), role.getId());
				if (roleData != null && roleData.size() > 0) {
					saveRoleAccessList(roleData);
				}

				Set<RoleMenuAccess> roleDataEmployee = getRoleMenuAccessEmployee(user.getProductId(), branchId,
						emp.getOrganization().getOrgId(), EnovationConstants.EMPLOYEE_ROLEID);
				if (roleDataEmployee != null && roleDataEmployee.size() > 0) {
					saveRoleAccessList(roleDataEmployee);
				}
				/*
				 * ADD ESCALATION ALERT
				 */
				for (Long roleIds : EnovationConstants.ESCALATION_ROLE_LIST) {
					RoleEscalationSetup setup = new RoleEscalationSetup(new Role(roleIds), new BranchMaster(branchId));
					/*
					 * setup.setRole(new Role(roleIds)); setup.setBranchMaster(new
					 * BranchMaster(branch.getBranchId()));
					 * setup.setNumberOfDays(EnovationConstants.ZERO);
					 */
					session.save(setup);
					SuggestionEscalationConfig sEConfig = new SuggestionEscalationConfig(new BranchMaster(branchId));
					session.save(sEConfig);
					BrowniePointsReimbursementCycle browniePointConfig = new BrowniePointsReimbursementCycle(
							new BrowniePointsReimbursementCycleType(EnovationConstants.BROWNIE_POINTS_NONE),
							new BranchMaster(branchId));
					session.save(browniePointConfig);
				}
			}
			user.setIsActive(EnovationConstants.ONE);
			user.setIsEmailVerified(EnovationConstants.ONE);
			session.update(user);

			// emd.sendMailToImportedEmployees(empDetails.getEmailId(),empDetails.getFirstName(),EnovationConstants.REGISTER,token);
			flag = true;
		} catch (Exception e) {
			e.printStackTrace();
			LOGGER.error("#INSIDE EXCEPTION OCCURED IN REGISTER USER " + e.getMessage());
		}
		return flag;
	}

	public int verifyBranchInExistingOrg(int orgId, Registration user) {
		LOGGER.info("#INSIDE IN VERIFYBRANCHINEXISTINGORG");
		int branchId = 0;
		List<Object[]> branchList = null;
		Session session = getCurrentSession();

		StringBuffer str = new StringBuffer("select location, branch_id from master_branch where org_id=:orgId");
		/*
		 * if(user.getLocation()!= null) { str.append(" and  location=:loc"); }
		 */
		try {
			branchList = session.createNativeQuery(str.toString()).setParameter("orgId", orgId).getResultList();
			/*
			 * if(user.getLocation()!= null) {
			 * branchList=session.createQuery(str.toString()).setParameter("orgId",
			 * orgId).setParameter("loc", user.getLocation()).getResultList(); }else {
			 * branchList=session.createQuery(str.toString()).setParameter("orgId",
			 * orgId).getResultList(); }
			 */
			if (branchList != null && branchList.size() > 0) {
				for (Object[] obj : branchList) {
					if (obj[1] != null && obj[0] != null) {
						if (obj[0].toString() == user.getLocation()) {
							branchId = Integer.valueOf(obj[1].toString());
						}
					}
				}

			}
		} catch (Exception e) {
			LOGGER.error("#INSIDE EXCEPTION OCCURED IN VERIFYBRANCHINEXISTINGORG" + e.getMessage());
		}

		return branchId;
	}

	@Override
	public List<EmployeeDetails> getEmpListByOrg(String orgDomain) {
		LOGGER.info("#INSIDE IN getEmpListByOrg ");
		Session session = getCurrentSession();
		List<EmployeeDetails> object = new ArrayList<>();
		try {
			object = session.createNativeQuery(
					"select e.* from tbl_employee_details e,master_organization o  WHERE e.org_id = o.org_id and o.org_domain=:orgDomain and e.is_deactive=0")
					.addEntity(EmployeeDetails.class).setParameter("orgDomain", orgDomain).getResultList();
			System.out.println("size :" + object.size());
		} catch (Exception e) {
			e.printStackTrace();
			LOGGER.error("#INSIDE EXCEPTION OCCURED IN getEmpListByOrg " + e.getMessage());
		}
		return object;
	}

	@Override
	public ProductOrgConfig getProductOrgConfigDet(EmployeeDetails login) {
		LOGGER.info("#INSIDE IN getProductOrgConfigDet ");
		Session session = getCurrentSession();
		ProductOrgConfig productconfigDet = null;
		try {
			List<ProductOrgConfig> productOrgConfigList = session
					.createQuery("FROM ProductOrgConfig WHERE org.orgId=:orgId and branch.branchId=:branchId")
					.setParameter("orgId", login.getOrganization().getOrgId())
					.setParameter("branchId", login.getBranch().getBranchId()).getResultList();
			if (productOrgConfigList.size() > 0) {
				productconfigDet = productOrgConfigList.get(0);
			}
		} catch (Exception e) {
			LOGGER.error("#INSIDE EXCEPTION IN getProductOrgConfigDet : " + e.getMessage());
		}
		return productconfigDet;
	}

	@Override
	public String getNoOfDaysRemaining(long id, int branchId) {
		LOGGER.info("#INSIDE IN getNoOfDaysRemaining ");
		Session session = getCurrentSession();
		String data = null;
		try {
			Object object = session.createNativeQuery(
					"select datediff(productConf.deactivation_date, now()) as remainingDays from product_org_config productConf where productConf.id =:id and productConf.branch_id =:branchId")
					.setParameter("id", id).setParameter("branchId", branchId).getSingleResult();

			if (object != null) {
				data = object.toString();
			}
		} catch (Exception e) {
			e.printStackTrace();
			LOGGER.error("#INSIDE EXCEPTION OCCURED IN getNoOfDaysRemaining " + e.getMessage());
		}
		return data;
	}

	@Override
	public Registration checkLoginRegisdtration(EmployeeDetails employee) {
		LOGGER.info("#INSIDE IN checkLoginRegisdtration ");
		Session session = getCurrentSession();
		Registration registration = null;
		try {
			List<Registration> registrationList = session
					.createQuery("FROM Registration WHERE email=:email or contactNumber =:email")
					.setParameter("email", employee.getEmailId()).getResultList();
			if (registrationList.size() > 0) {

				// if(pass.matches(employee.getPassword(),
				// registrationList.get(0).getPassword())){
				registration = registrationList.get(0);
				// }
			}
		} catch (Exception e) {
			LOGGER.error("#INSIDE EXCEPTION IN checkLoginRegisdtration : " + e.getMessage());
		}

		return registration;
	}

	@Override
	public EmployeeDetails checkLoginEmployee(EmployeeDetails employee) {
		LOGGER.info("#INSIDE IN checkLoginEmployee ");
		Session session = getCurrentSession();
		EmployeeDetails employeeObj = null;
		try {
			List<EmployeeDetails> employeeList = session.createQuery(
					"FROM EmployeeDetails e WHERE (e.emailId=:email or e.contactNo=:email or e.cmpyEmpId =:email) and e.isDeactive=0")
					.setParameter("email", employee.getEmailId()).getResultList();

			if (employeeList.size() > 0) {

				System.out.println("Printing Employee list ===========> " + employeeList);
				// if(pass.matches(employee.getPassword(), employeeList.get(0).getPassword())){
				employeeObj = employeeList.get(0);
				System.out.println("Printing Employee Object of (0) ===========> " + employeeObj);
				// }
			}
		} catch (Exception e) {
			LOGGER.error("#INSIDE EXCEPTION IN checkLoginEmployee : " + e.getMessage());
		}

		return employeeObj;
	}

	@Override
	public List<EmployeeDetails> getEmpListByOrg(String orgDomain, String emailId) {
		LOGGER.info("#INSIDE IN getEmpListByOrg ");
		Session session = getCurrentSession();
		List<EmployeeDetails> object = new ArrayList<>();
		// boolean isExist = false;
		try {
			object = session.createNativeQuery(
					"select e.* from tbl_employee_details e,master_organization o  WHERE e.org_id = o.org_id and o.org_domain=:orgDomain and  e.email_id = :emailId and e.is_deactive=0")
					.addEntity(EmployeeDetails.class).setParameter("orgDomain", orgDomain)
					.setParameter("emailId", emailId).getResultList();
			System.out.println("size :" + object.size());
			if (object != null && object.size() > 0) {
				// isExist = true;
			}
		} catch (Exception e) {
			e.printStackTrace();
			LOGGER.error("#INSIDE EXCEPTION OCCURED IN getEmpListByOrg " + e.getMessage());
		}
		return object;
	}

	@Override
	public boolean reSendMail(Registration request) {
		boolean flag = false;
		LOGGER.info("#INSIDE IN RESENDMAIL ");
		Session session = getCurrentSession();
		String token = String.valueOf(CommonUtils.generateToken(16));
		try {
			request.setToken(token);
			request.setCreatedDate(CommonUtils.currentDate());
			session.update(request);
			emd.sendMailToImportedEmployees(request.getEmail(), request.getName(), "", token,
					EnovationConstants.REGISTER);
			flag = true;
		} catch (Exception e) {
			LOGGER.error("#INSIDE EXCEPTION OCCURED IN  RESENDMAIL " + e.getMessage());
		}
		return flag;
	}

	@Override
	public List<Registration> isRegisterEmail(String email) {
		LOGGER.info("#INSIDE IN ISREGISTEREMAIL ");
		Session session = getCurrentSession();
		List<Registration> register = null;
		try {
			register = session.createQuery("from Registration WHERE email=:email").setParameter("email", email)
					.getResultList();
		} catch (Exception e) {
			LOGGER.error("#INSIDE EXCEPTION OCCURED IN ISREGISTEREMAIL " + e.getMessage());
		}
		return register;
	}

	@Override
	public boolean reSendMailToEmployee(EmployeeDetails employee) {
		boolean flag = false;
		LOGGER.info("#INSIDE IN RESENDMAIL TO EMPLOYEE");
		Session session = getCurrentSession();
		String token = String.valueOf(CommonUtils.generateToken(16));
		try {
			employee.setToken(token);
			employee.setCreatedDate(CommonUtils.currentDate());
			session.update(employee);
			emd.sendMailToImportedEmployees(employee.getEmailId(),
					employee.getFirstName() + " " + employee.getLastName(), "", token, EnovationConstants.REGISTER);
			flag = true;
		} catch (Exception e) {
			LOGGER.error("#INSIDE EXCEPTION OCCURED IN  RESENDMAIL EMPLOYEE " + e.getMessage());
		}
		return flag;

	}

	@Override
	public boolean logout(EmployeeDetails emp/* ,HttpServletRequest request */) {
		boolean flag = false;
		LOGGER.info("INSIDE LOGOUT API");
		try {
			String username = null;
			if (emp.getEmailId() != null) {
				username = emp.getEmailId();
			} else {
				username = emp.getContactNo();
			}

			String responseString = "";

			audit.insertLoginAudit(emp.getTransactionId(), emp.getEmpId(), null, username,
					EnovationConstants.LOGOUT_SUCCESS, null, emp.getBranchId(), emp.getOrgId(), responseString);
			flag = true;
		} catch (Exception e) {
			LOGGER.error("#INSIDE EXCEPTION OCCURED IN  LOGOUT API " + e.getMessage());
			e.printStackTrace();
		}
		return flag;
	}

	@Override
	public BranchMaster getBranchDetails(int branchId) {
		BranchMaster branch = null;
		Session session = getCurrentSession();
		LOGGER.info("INSIDE GETBRANCHDETAILS API");
		try {
			branch = (BranchMaster) session.get(BranchMaster.class, branchId);
		} catch (Exception e) {
			LOGGER.error("#INSIDE EXCEPTION OCCURED IN  GETBRANCHDETAILS API " + e.getMessage());
		}
		return branch;
	}

	@Override
	public List<MultiBranchAccess> getOrgExeBranchList(String email) {
		LOGGER.info("# INSIDE IN GETORGEXECUTIVFEBRANCHLIST ");
		Session session = getCurrentSession();
		List<MultiBranchAccess> list = new ArrayList<>();
		try {

			List<Object[]> branchData = session
					.createNativeQuery(
							"SELECT ed.emp_id,ed.first_name,ed.last_name,ed.email_id,mb.org_id,mb.branch_id,\r\n"
									+ "mb.is_access,m.location,m.name as branch_name \r\n"
									+ "FROM multiple_branch_access mb \r\n"
									+ "INNER JOIN tbl_employee_details ed ON ed.emp_id = mb.emp_id\r\n"
									+ "INNER JOIN master_branch m ON m.branch_id = mb.branch_id\r\n"
									+ "INNER JOIN emp_roles er  ON ed.emp_id = er.emp_id\r\n"
									+ "WHERE ed.email_id=:email AND mb.is_access=:isAccess AND er.role_id=:roleId")
					.setParameter("email", email).setParameter("roleId", EnovationConstants.ORG_EXECUTIVE)
					.setParameter("isAccess", EnovationConstants.ONE).getResultList();

			for (Object[] obj : branchData) {

				MultiBranchAccess branchList = new MultiBranchAccess();
				branchList.setEmpId(Integer.parseInt(String.valueOf(obj[0])));
				branchList.setFirstName(String.valueOf(obj[1]));
				branchList.setLastName(String.valueOf(obj[2]));
				branchList.setEmail(String.valueOf(obj[3]));
				branchList.setOrgId(Integer.parseInt(String.valueOf(obj[4])));
				branchList.setBranchId(Integer.parseInt(String.valueOf(obj[5])));
				branchList.setIsAccess(Integer.parseInt(String.valueOf(obj[6])));
				branchList.setLocation(String.valueOf(obj[7]));
				branchList.setBranchName(String.valueOf(obj[8]));
				list.add(branchList);
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		return list;
	}

	@Override
	public List<ModuleSubscription> getMenuListByBranchIdOrOrgId(Integer branchId, Integer orgId) {
		LOGGER.info("#INSIDE IN GET MENU LIST BY SUBSCRIPTION  API ");
		List<ModuleSubscription> menuList = null;
		try {
			menuList = getCurrentSession()
					.createQuery(
							"from ModuleSubscription WHERE (branch_id=:branchId OR org_id=:orgId) AND is_active='Y'")
					.setParameter("branchId", branchId).setParameter("orgId", orgId).getResultList();

		} catch (Exception e) {
			LOGGER.error("#INSIDE EXCEPTION OCCURED IN  GET MENU LIST BY GET MENU LIST BY SUBSCRIPTION  API"
					+ e.getMessage());
		}
		return menuList;
	}

	@Override
	public boolean saveBranchAccessSetup(BranchAccessSetup request) {
		LOGGER.info("INSIDE SAVE BRANCH ACCESS SETUP API");
		Session session = getCurrentSession();
		boolean flag = false;
		try {
			if (request != null) {
				session.save(request);
				flag = true;
			}
		} catch (Exception e) {
			LOGGER.error("#INSIDE EXCEPTION OCCURED IN SAVE BRANCH ACCESS SETUP API" + e.getMessage());
		}
		return flag;
	}

	@Override
	public List<BranchAccessSetup> getBranchAccessSetup(int orgId) {
		LOGGER.info("#INSIDE IN GET BRANCH ACCESS SETUP API");
		List<BranchAccessSetup> accessList = null;
		try {
			accessList = getCurrentSession().createQuery("from BranchAccessSetup WHERE org_id=:orgId AND is_access='Y'")
					.setParameter("orgId", orgId).getResultList();

		} catch (Exception e) {
			LOGGER.error("#INSIDE EXCEPTION OCCURED IN GET BRANCH ACCESS SETUP API" + e.getMessage());
		}
		return accessList;
	}

	@Override
	public boolean deleteBranchAccessSetup(int id) {
		LOGGER.info("INSIDE DELETE BRANCH ACCESS SETUP API");
		Session session = getCurrentSession();
		boolean flag = false;
		try {
			session.createNativeQuery("update branch_access_setup set is_access='N' where id=:id ")
					.setParameter("id", id).executeUpdate();
			flag = true;

		} catch (Exception e) {
			LOGGER.error("#INSIDE EXCEPTION OCCURED IN DELETE BRANCH ACCESS SETUP API" + e.getMessage());
		}
		return flag;
	}

	@Override
	public List<BranchAccessSetup> getBranchAccessSetupByEmpId(int orgId, int empId) {
		LOGGER.info("#INSIDE IN GET BRANCH ACCESS SETUP BY EMP ID API");
		List<Object[]> list = new ArrayList<>();
		List<BranchAccessSetup> branchAccessLit = new ArrayList<>();

		try {
			list = getCurrentSession().createNativeQuery(
					"select ba.id,ba.branch_id,ba.emp_id,ba.is_access,ba.org_id,ba.created_date,ba.updated_date,b.name,concat(e.first_name,\" \",e.last_name) as empname,b.location from \r\n"
							+ "branch_access_setup ba \r\n"
							+ "inner join master_branch b on b.branch_id=ba.branch_id\r\n"
							+ "inner join tbl_employee_details e on e.emp_id=ba.emp_id\r\n"
							+ "where ba.org_id=:orgId and ba.emp_id=:empId and ba.is_access='Y' ")
					.setParameter("empId", empId).setParameter("orgId", orgId).getResultList();

			for (Object[] obj : list) {
				BranchAccessSetup branchAccess = new BranchAccessSetup();
				if (obj[0] != null) {
					branchAccess.setId(Integer.valueOf(String.valueOf(obj[0])));
				}
				if (obj[1] != null) {
					branchAccess.setBranchId(Integer.valueOf(String.valueOf(obj[1])));
				}
				if (obj[2] != null) {
					branchAccess.setEmpId(Integer.valueOf(String.valueOf(obj[2])));
				}
				if (obj[3] != null) {
					branchAccess.setIsAccess(String.valueOf(obj[3]));
				}
				if (obj[4] != null) {
					branchAccess.setOrgId(Integer.valueOf(String.valueOf(obj[4])));
				}
				if (obj[5] != null) {
					branchAccess.setCreatedDate(CommonUtils.convertStringToTimestamp(String.valueOf(obj[5])));
				}
				if (obj[6] != null) {
					branchAccess.setUpdatedDate(CommonUtils.convertStringToTimestamp(String.valueOf(obj[6])));
				}
				if (obj[7] != null) {
					branchAccess.setBranchName(String.valueOf(obj[7]));
				}
				if (obj[8] != null) {
					branchAccess.setEmployeeName(String.valueOf(obj[8]));
				}
				if (obj[9] != null) {
					branchAccess.setLocation(String.valueOf(obj[9]));
				}

				branchAccessLit.add(branchAccess);
			}

		} catch (Exception e) {
			LOGGER.error("#INSIDE EXCEPTION OCCURED IN GET BRANCH ACCESS SETUP BY EMP ID API" + e.getMessage());
		}
		return branchAccessLit;
	}

	/**
	 * @author rakesh 12 june 2020
	 */
	@Override
	public List<EmployeeDetailsBO> getUserAccessList(UserMenuAccess request) {

		StringBuilder str = new StringBuilder("SELECT \n" + "  sub.sub_menu_name AS subMenuName,\n"
				+ "  uma.access AS statusId,\n" + "  sub.id AS subMenuId,\n" + "  m.menu_name AS menuName,\n"
				+ "  m.id AS menuId,ed.emp_id,ed.first_name,ed.last_name,ed.cmpy_emp_id,ed.email_id,contact_no,uma.id as userAccessId\n"
				+ "FROM\n" + "  user_menu_access uma\n" + " INNER JOIN sub_menu sub ON sub.id = uma.sub_menu_id\n"
				+ " INNER JOIN menu m ON m.id = uma.menu_id\n"
				+ " INNER JOIN tbl_employee_details ed ON ed.emp_id = uma.emp_id\n"
				+ "WHERE uma.access = 'Y' AND uma.org_id =:orgId AND uma.branch_id =:branchId ");

		if (request.getEmpId() > 0) {
			str.append(" and uma.emp_id={empId}");
		}
		Session session = getCurrentSession();
		List<MyMenu> list = new ArrayList<>();
		List<EmployeeDetailsBO> empAccessList = new ArrayList<>();

		Map<Long, List<MenuData>> roleAccessList = new HashMap<Long, List<MenuData>>();

		Query query = session.createNativeQuery(str.toString().replace("{empId}", String.valueOf(request.getEmpId())))
				.setParameter("orgId", request.getOrgId()).setParameter("branchId", request.getBranchId());
		List<Object[]> rows = query.getResultList();

		for (Object[] row : rows) {

			list.add(userAccessListParser(row));
		}

		if (list != null && list.size() > 0) {

			/**
			 * Group By Using EmpId
			 */
			Map<Integer, List<MyMenu>> groupByEmpList = list.stream().collect(Collectors.groupingBy(MyMenu::getEmpId));

			for (Map.Entry<Integer, List<MyMenu>> entry : groupByEmpList.entrySet()) {

				EmployeeDetailsBO tmpEmp = new EmployeeDetailsBO();
				BeanUtils.copyProperties(entry.getValue().get(0).getEmp(), tmpEmp);

				List<MyMenu> tmpAccessList = entry.getValue();

				for (MyMenu menu : tmpAccessList) {
					menu.setEmp(null);
				}
				/**
				 * Group By Using MenuId
				 */
				Map<Integer, List<MyMenu>> groupByTmpAccessList = tmpAccessList.stream()
						.collect(Collectors.groupingBy(MyMenu::getMenuId));

				List<MenuData> accessList = userAccessConvertMaptoMenuList(groupByTmpAccessList);
				tmpEmp.setEmpAccess(accessList);

				empAccessList.add(tmpEmp);
			}
		}

		/**
		 * if emp don't have user level access then check role access
		 */
		if (request.getEmpId() > 0 && (empAccessList == null || empAccessList.isEmpty())) {
			empAccessList.add(new EmployeeDetailsBO(request.getEmpId()));
		}

		setBranchRoleAccess(request, session, roleAccessList);

		/**
		 * Set Employee Role & merge their role access & User access
		 */
		if (empAccessList != null && !empAccessList.isEmpty()) {
			for (EmployeeDetailsBO empDao : empAccessList) {
				setEmployeeRole(session, empDao, roleAccessList);
			}
		}

		return empAccessList;
	}

	/**
	 * @author rakesh 12 june 2020
	 * @param row
	 * @return
	 */
	private MyMenu userAccessListParser(Object[] row) {

		MyMenu data = new MyMenu();
		EmployeeDetailsBO emp = new EmployeeDetailsBO();
		data.setSubMenuName(CommonUtils.objectToString(row[0]));

		int statusId = 0;
		if (row[1] != null) {
			statusId = ((row[1].toString()).equals("Y") ? 1 : 0);
			System.out.println("Status Id in if =>" + statusId);
		}
		System.out.println("Status Id =>" + row[1].toString());
		data.setStatusId(statusId);
		data.setSubMenuId(CommonUtils.objectToInt(row[2]));
		data.setMenuName(CommonUtils.objectToString(row[3]));
		data.setMenuId(CommonUtils.objectToInt(row[4].toString()));
		data.setEmpId(CommonUtils.objectToInt(row[5]));
		data.setUserAccessId(CommonUtils.objectToLong(row[11]));
		data.setAccessType("USER");

		emp.setEmpId(CommonUtils.objectToInt(row[5]));
		emp.setFirstName(CommonUtils.objectToString(row[6]));
		emp.setLastName(CommonUtils.objectToString(row[7]));
		emp.setCmpyEmpId(CommonUtils.objectToString(row[8]));
		emp.setEmailId(CommonUtils.objectToString(row[9]));
		emp.setContactNo(CommonUtils.objectToString(row[10]));

		data.setEmp(emp);
		return data;
	}

	/**
	 * @author rakesh 12 june 2020
	 */
	public List<MenuData> userAccessConvertMaptoMenuList(Map<Integer, List<MyMenu>> mapObject) {
		List<MenuData> menuList = new ArrayList<MenuData>();
		MenuData menudata = null;
		for (Map.Entry<Integer, List<MyMenu>> entry : mapObject.entrySet()) {
			menudata = new MenuData();
			menudata.setMenuId(entry.getValue().get(0).getMenuId());
			menudata.setMenuName(entry.getValue().get(0).getMenuName());
			menudata.setSubMenuList(entry.getValue());
			menuList.add(menudata);
		}
		return menuList;
	}

	/**
	 * @author rakesh 15 june 2020
	 * @param orgId
	 * @param branchId
	 * @param session
	 * @return
	 */
	private boolean setBranchRoleAccess(UserMenuAccess request, Session session,
			Map<Long, List<MenuData>> roleAccessList) {

		StringBuffer str = new StringBuffer(
				"select a.is_active as statusId,sub.id as subMenuId,sub.sub_menu_name as subMenuName,m.menu_name AS menuName,m.id as menuId,a.id as arid,a.role_id");
		str.append(
				" from role_menu_access  a,sub_menu sub,menu m where sub.id=  a.submenu_id and m.id=  a.menu_id and ");
		str.append(" a.org_id = :orgId and a.branch_id = :branchId and a.is_active=1");
		System.out.println(str.toString());

		List<MyMenu> list = new ArrayList<MyMenu>();

		Query query = session.createNativeQuery(str.toString()).setParameter("orgId", request.getOrgId())
				.setParameter("branchId", request.getBranchId());
		List<Object[]> rows = query.getResultList();

		if (rows != null && !rows.isEmpty()) {

			for (Object[] row : rows) {
				MyMenu data = new MyMenu();
				data.setStatusId(Integer.parseInt(row[0].toString()));
				data.setSubMenuId(Integer.parseInt(row[1].toString()));
				data.setSubMenuName(row[2].toString());
				data.setMenuName(row[3].toString());
				data.setMenuId(Integer.parseInt(row[4].toString()));
				data.setRoleAccessId(Integer.parseInt(row[5].toString()));
				data.setRoleId(CommonUtils.objectToLong(row[6]));
				data.setAccessType("ROLE");
				list.add(data);
			}

		}
		if (list != null && list.size() > 0) {
			Map<Long, List<MyMenu>> roleAccessGrouping = list.stream()
					.collect(Collectors.groupingBy(MyMenu::getRoleId));

			for (Map.Entry<Long, List<MyMenu>> entry : roleAccessGrouping.entrySet()) {
				List<MenuData> tmpMenuData = roleAccessConvertMaptoMenuList(entry.getKey(), entry.getValue());
				roleAccessList.put(entry.getKey(), tmpMenuData);
			}
		}
		return true;
	}

	/**
	 * @author rakesh 15 june 2020
	 * @param roleId
	 * @param accessList
	 * @return
	 */
	public List<MenuData> roleAccessConvertMaptoMenuList(long roleId, List<MyMenu> accessList) {
		List<MenuData> menuList = new ArrayList<MenuData>();

		Map<Integer, List<MyMenu>> menuAccessGrouping = accessList.stream()
				.collect(Collectors.groupingBy(MyMenu::getMenuId));

		MenuData menudata = null;
		for (Map.Entry<Integer, List<MyMenu>> entry : menuAccessGrouping.entrySet()) {
			menudata = new MenuData();
			menudata.setMenuId(entry.getValue().get(0).getMenuId());
			menudata.setMenuName(entry.getValue().get(0).getMenuName());
			menudata.setSubMenuList(entry.getValue());
			menuList.add(menudata);
		}
		return menuList;
	}

	/**
	 * @author rakesh 15 june 2020
	 * @param session
	 * @param tmpEmp
	 * @return
	 */
	private boolean setEmployeeRole(Session session, EmployeeDetailsBO tmpEmp,
			Map<Long, List<MenuData>> roleAccessList) {

		boolean flag = false;
		List<Role> roleList = new ArrayList<>();
		List<MenuData> empAccess = null;

		/**
		 * check employee role
		 */
		List<Object[]> objList = session.createNativeQuery("select emp_id,role_id  from emp_roles where emp_id=:empId")
				.setParameter("empId", tmpEmp.getEmpId()).getResultList();

		if (objList != null && !objList.isEmpty()) {

			for (Object[] obj : objList) {
				Role role = new Role();
				role.setId(CommonUtils.objectToLong(obj[1]));
				roleList.add(role);
			}
		}
		tmpEmp.setRoleList(roleList);

		/**
		 * Check If access list is blank then create new
		 */
		if (tmpEmp.getEmpAccess() == null || tmpEmp.getEmpAccess().isEmpty()) {
			empAccess = new ArrayList<>();
		} else {
			empAccess = tmpEmp.getEmpAccess();
		}

		/**
		 * If empployee have role then set role base access control
		 */
		for (Role role : roleList) {

			for (Map.Entry<Long, List<MenuData>> entry : roleAccessList.entrySet()) {
				if (entry.getKey() == role.getId()) {

					System.out.println(
							" IN setEmployeeRole | Match role id  " + role.getId() + " empId is " + tmpEmp.getEmpId());
					// empAccess.addAll(entry.getValue());
					setUniqueRoleAccessRecords(empAccess, entry.getValue());

				}
			}
		}
		tmpEmp.setEmpAccess(empAccess);

		return flag;
	}

	/**
	 * @author rakesh 15 june 2020
	 * @param menuList
	 * @param accessList
	 * @return
	 */
	private boolean setUniqueRoleAccessRecords(List<MenuData> menuList, List<MenuData> accessList) {

		boolean flag = false;
		if (accessList != null && !accessList.isEmpty()) {
			for (MenuData accessData : accessList) {
				boolean menuFlag = false;
				for (MenuData roleData : menuList) {
					if (roleData.getMenuId() == accessData.getMenuId()) {
						menuFlag = true;
						for (MyMenu accessMymenu : accessData.getSubMenuList()) {
							boolean submenuFlag = false;
							for (MyMenu roleMymenu : roleData.getSubMenuList()) {
								if (roleMymenu.getSubMenuId() == accessMymenu.getSubMenuId()) {
									submenuFlag = true;
								}
							}
							if (!submenuFlag) {
								roleData.getSubMenuList().add(accessMymenu);
							}
						}
					}
				}
				if (!menuFlag) {
					menuList.add(accessData);
				}
			}
		}
		flag = true;
		return flag;
	}

	/**
	 * @author rakesh 12 june 2020
	 * @param request
	 * @return
	 */
	@Override
	public boolean addOrUpdateUserAccess(MenuDTO request) {

		boolean flag = false;
		Session session = getCurrentSession();
		for (UserMenuAccess usrAccess : request.getUserAccessList()) {

			if (usrAccess.getAccess() != null && usrAccess.getAccess().equals(EnovationConstants.N)) {
				session.createQuery("delete from UserMenuAccess where id=:id").setParameter("id", usrAccess.getId())
						.executeUpdate();
			} else {
				session.save(usrAccess);
			}
		}
		flag = true;
		return flag;
	}
}
