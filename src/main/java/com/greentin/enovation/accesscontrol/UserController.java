package com.greentin.enovation.accesscontrol;

import java.util.Set;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.greentin.enovation.config.EnovationConfig;
import com.greentin.enovation.employee.EmployeeSendRegMail;
import com.greentin.enovation.model.BranchAccessSetup;
import com.greentin.enovation.model.EmployeeDetails;
import com.greentin.enovation.model.RoleMenuAccess;
import com.greentin.enovation.model.UserMenuAccess;
import com.greentin.enovation.schedulars.EventScheduler;
import com.greentin.enovation.schedulars.SkillMatrixSchedular;
import com.greentin.enovation.security.ExecuteApache;
import com.greentin.enovation.thirdPartyIntegrations.ExternalApiTriggers;

@RestController
@CrossOrigin
public class UserController {

	@Autowired
	UserServiceInterface userSvcInterface;

	@PersistenceContext
	EntityManager em;

	@Autowired
	ExecuteApache execute;

	@Autowired
	EmployeeSendRegMail empSendMail;

	@Autowired
	EventScheduler event;

	@Autowired
	ExternalApiTriggers extApi;

	@Autowired
	SkillMatrixSchedular skillmatrix;

	@CrossOrigin
	@GetMapping(value = "/test/mytest")
	public String mytest() {

		// kuberSche.assignReviewToPlantHead();
		/*
		 * String s=""; try { Random randomGenerator = new Random();
		 * List<DepartmentLevel> deptList=em.
		 * createQuery("FROM DepartmentLevel where level = 0 and branch.branchId = 51")
		 * .getResultList();
		 * System.out.println("stream deptList:"+deptList.get(0).getEmpDetails().
		 * getEmpId()); System.out.println("list size is :"+deptList.size());
		 * 
		 * int count = 1; for(int i=0 ; i<deptList.size() ; i++) { int index =
		 * randomGenerator.nextInt(deptList.size()); DepartmentLevel deptLevel =
		 * deptList.get(index); System.out.println("sr no. "+count
		 * +"random function empId is :"+deptLevel.getEmpDetails().getEmpId()); count
		 * ++; }
		 * 
		 * Random random = new Random(); List<DepartmentLevel> deptLevels =IntStream
		 * .generate(() -> random.nextInt(deptList.size())) .distinct() .limit(1)
		 * .mapToObj(deptList::get) .collect(Collectors.toList());
		 * System.out.println("stream :"+deptLevels.get(0).getEmpDetails().getEmpId());
		 * } catch (Exception e) { e.printStackTrace(); }
		 */
		// sche.evalSuggestionAssessment();
		// sche.reminderEmailForWhoAreNotSubmittedSuggestions();
		// sche.escalteUsersWhoAreSuggestionsNotSubmitted();
		extApi.getEmployeeDetails(); 
//		skillmatrix.assignPreAssessmentScheduler();
		// sche.assingnKaizenToAuditorForSusAudit();
		return "success";
	}


	@CrossOrigin
	@GetMapping(value = "/mytest")
	public String test() {
		String s = "";
		try {
			boolean status = execute.Writefile();
			if (status) {
				execute.executeFile(EnovationConfig.buddyConfig.get("apache_command"));
			}
			System.out.println(status);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return s;
	}

	@CrossOrigin
	@PostMapping(value = "/saveproduct")
	public UserResponse saveProduct(@RequestBody Product product) {
		return userSvcInterface.saveProduct(product);
	}

	@CrossOrigin
	@PostMapping(value = "/updateproduct")
	public UserResponse updateProduct(@RequestBody Product product) {
		return userSvcInterface.updateProduct(product);
	}

	@CrossOrigin
	@GetMapping(value = "/getproductlist")
	public UserResponse getProductList() {
		return userSvcInterface.getProductList();
	}

	@CrossOrigin
	@GetMapping(value = "/getactiveproductlist")
	public UserResponse getActiveProductList() {
		return userSvcInterface.getActiveProductList();
	}

	@CrossOrigin
	@GetMapping(value = "/getmenulistbyproductid/{productId}")
	public UserResponse getMenuListByProductId(@PathVariable("productId") Integer productId) {
		return userSvcInterface.getMenuListByProductId(productId);
	}

	/////////////////////////////////////////////////////////////////////////////////////////////////
	//
	// Function Name : getMenuListByBranchIdOrOrgId
	//
	// Input Parameter : Integer branchId / Integer OrgId
	//
	// Return Type : UserResponse
	//
	// Author & Time Stamp : Vinay B.
	//
	// Modified By & Time :
	//
	// Description : This method fetches list of module subscribed by user.
	//
	//////////////////////////////////////////////////////////////////////////////////////////////////

	@CrossOrigin
	@GetMapping(value = "/getMenuListByBranchIdOrOrgId/{branchId}/{orgId}")
	public UserResponse getMenuListByBranchIdOrOrgId(@PathVariable("branchId") Integer branchId,
			@PathVariable("orgId") Integer orgId) {
		return userSvcInterface.getMenuListByBranchIdOrOrgId(branchId, orgId);
	}

	@CrossOrigin
	@PostMapping(value = "/savemenu")
	public UserResponse saveMenu(@RequestBody Menu menu) {
		return userSvcInterface.saveMenu(menu);
	}

	@CrossOrigin
	@PostMapping(value = "/updatemenu")
	public UserResponse updateMenu(@RequestBody Menu menu) {
		return userSvcInterface.updateMenu(menu);
	}

	@CrossOrigin
	@PostMapping(value = "/savesubmenu")
	public UserResponse saveSubMenu(@RequestBody SubMenu submenu) {
		return userSvcInterface.saveSubMenu(submenu);
	}

	@CrossOrigin
	@PostMapping(value = "/updatesubmenu")
	public UserResponse updateSubMenu(@RequestBody SubMenu submenu) {
		return userSvcInterface.updateSubMenu(submenu);
	}

	@CrossOrigin
	@PostMapping(value = "/disablesubmenu")
	public UserResponse disableSubMenu(@RequestBody SubMenu subMenu) {
		return userSvcInterface.disableSubMenu(subMenu);
	}

	@CrossOrigin
	@PostMapping(value = "/disableproduct")
	public UserResponse disableProduct(@RequestBody Product product) {
		return userSvcInterface.disableProduct(product);
	}

	@CrossOrigin
	@PostMapping(value = "/register") // depricated
	public UserResponse registration(@RequestBody Registration user) {
		return userSvcInterface.registration(user);
	}

	@CrossOrigin
	@PostMapping(value = "/verifyemail")
	public UserResponse verifyEmail(@RequestBody Registration user) {
		return userSvcInterface.verifyEmail(user);
	}

	@CrossOrigin
	@PostMapping(value = "/saveuser")
	public UserResponse saveUser(@RequestBody Users user) {
		return userSvcInterface.saveUser(user);
	}

	@CrossOrigin
	@GetMapping(value = "/getregisteruserlist")
	public UserResponse getRegisterUserList() {
		return userSvcInterface.getRegisterUserList();
	}

	@CrossOrigin
	@GetMapping(value = "/getgreentinusers")
	public UserResponse getGreentinUserList() {
		return userSvcInterface.getGreentinUserList();
	}

	@CrossOrigin
	@PostMapping(value = "/login")
	public UserResponse login(@RequestBody EmployeeDetails user/* ,HttpServletRequest request */) {
		return userSvcInterface.login(user);
	}

	@CrossOrigin
	@GetMapping(value = "/getlistofrole")
	public UserResponse getListofRole() {
		return userSvcInterface.getListofRole();
	}

	@CrossOrigin
	@PostMapping(value = "/saverolemenuaccess")
	public UserResponse saveRoleMenuAccess(@RequestBody RoleMenuAccess role) {
		return userSvcInterface.saveRoleMenuAccess(role);
	}

	@CrossOrigin
	@PostMapping(value = "/saveroleaccess")
	public UserResponse saveRoleAccess(@RequestBody Set<RoleMenuAccess> role) {
		return userSvcInterface.saveRoleAccessList(role);
	}

	@CrossOrigin
	@PostMapping(value = "/updaterolemenuaccess")
	public UserResponse updateRoleMenuAccess(@RequestBody RoleMenuAccess role) {
		return userSvcInterface.updateRoleMenuAccess(role);
	}

	@CrossOrigin
	@GetMapping(value = "/getlistofroleaccess/{orgId}/{branchId}/{roleId}")
	public UserResponse getListofRoleAccess(@PathVariable("orgId") Integer orgId,
			@PathVariable("branchId") Integer branchId, @PathVariable("roleId") Integer roleId) {
		if (orgId == null) {
			orgId = 0;
		}
		if (branchId == null) {
			branchId = 0;
		}
		if (roleId == null) {
			roleId = 0;
		}
		return userSvcInterface.getListofRoleAccess(orgId, branchId, roleId);
	}

	@CrossOrigin
	@GetMapping(value = "/getlistofroleaccessnew/{orgId}/{branchId}/{roleId}/{productId}")
	public UserResponse getListofRoleAccessNew(@PathVariable("orgId") Integer orgId,
			@PathVariable("branchId") Integer branchId, @PathVariable("roleId") Integer roleId,
			@PathVariable("productId") Integer productId) {
		if (orgId == null) {
			orgId = 0;
		}
		if (branchId == null) {
			branchId = 0;
		}
		if (roleId == null) {
			roleId = 0;
		}
		return userSvcInterface.getListofRoleAccessNew(orgId, branchId, roleId, productId);
	}

	@CrossOrigin
	@GetMapping(value = "/getlistofsubscriptiontype")
	public UserResponse getListofSubscriptionType() {
		return userSvcInterface.getListofSubscriptionType();
	}

	@CrossOrigin
	@PostMapping(value = "/updatesetupcomleted")
	public UserResponse updateSetupComleted(@RequestBody ProductOrgConfig request) {
		return userSvcInterface.updateSetupComleted(request);
	}

	@CrossOrigin
	@GetMapping(value = "/isemailverify/{email}")
	public UserResponse isEmailVerify(@PathVariable("email") String email) {
		return userSvcInterface.isEmailVerify(email);
	}

	@CrossOrigin
	@PostMapping(value = "/forgotpassword")
	public UserResponse forgotPassword(@RequestBody EmployeeDetails request) {
		return userSvcInterface.forgotPassword(request);
	}

	@CrossOrigin
	@PostMapping(value = "/savedemorequest")
	public UserResponse saveDemoRequest(@RequestBody DemoRequest request) {
		return userSvcInterface.saveDemoRequest(request);
	}

	@CrossOrigin
	@GetMapping(value = "/demorequestlist")
	public UserResponse demoRequestList() {
		return userSvcInterface.demoRequestList();
	}

	@CrossOrigin
	@PostMapping(value = "/registration")
	public UserResponse saveRegistration(@RequestBody Registration request) {
		return userSvcInterface.saveRegistration(request);
	}

	@CrossOrigin
	@PostMapping(value = "/resendemail")
	public UserResponse resendEmail(@RequestBody Registration user) {
		return userSvcInterface.resendEmail(user);
	}

	@CrossOrigin
	@GetMapping(value = "/getAppVersion")
	public UserResponse getAppVersion() {
		return userSvcInterface.appVersion();
	}

	@CrossOrigin
	@PostMapping(value = "/logout")
	public UserResponse logout(@RequestBody EmployeeDetails emp /* ,HttpServletRequest request */) {
		return userSvcInterface.logout(emp);
	}

	@CrossOrigin
	@PostMapping(value = "/saveBranchAccessSetup")
	public UserResponse saveBranchAccessSetup(@RequestBody BranchAccessSetup request) {
		return userSvcInterface.saveBranchAccessSetup(request);
	}

	@CrossOrigin
	@GetMapping(value = "/deleteBranchAccessSetup/{id}")
	public UserResponse deleteBranchAccessSetup(@PathVariable("id") int id) {
		return userSvcInterface.deleteBranchAccessSetup(id);
	}

	@CrossOrigin
	@GetMapping(value = "/getBranchAccessSetup/{orgId}")
	public UserResponse getBranchAccessSetup(@PathVariable("orgId") int orgId) {
		return userSvcInterface.getBranchAccessSetup(orgId);
	}

	@CrossOrigin
	@GetMapping(value = "/getBranchAccessSetupByEmpId/{orgId}/{empId}")
	public UserResponse getBranchAccessSetupByEmpId(@PathVariable("orgId") int orgId,
			@PathVariable("empId") int empId) {
		return userSvcInterface.getBranchAccessSetupByEmpId(orgId, empId);
	}

	/**
	 * @author rakesh 12 june 2020
	 * @param request
	 * @return
	 */
	@PostMapping(value = "/getUserAccessList")
	public UserResponse getUserAccessList(@RequestBody UserMenuAccess request) {
		return userSvcInterface.getUserAccessList(request);
	}

	/**
	 * @author rakesh 12 june 2020
	 * @param request
	 * @return
	 */
	@PostMapping(value = "/addOrUpdateUserAccess")
	public UserResponse addOrUpdateUserAccess(@RequestBody MenuDTO request) {
		return userSvcInterface.addOrUpdateUserAccess(request);
	}

}
