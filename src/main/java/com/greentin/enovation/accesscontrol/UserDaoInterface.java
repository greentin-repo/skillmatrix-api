package com.greentin.enovation.accesscontrol;

import java.util.HashMap;
import java.util.List;
import java.util.Set;

import com.greentin.enovation.beans.MenuData;
import com.greentin.enovation.bo.model.EmployeeDetailsBO;
import com.greentin.enovation.employee.EmployeeDTO;
import com.greentin.enovation.model.BranchAccessSetup;
import com.greentin.enovation.model.BranchMaster;
import com.greentin.enovation.model.EmployeeDetails;
import com.greentin.enovation.model.ModuleSubscription;
import com.greentin.enovation.model.MultiBranchAccess;
import com.greentin.enovation.model.RoleMenuAccess;
import com.greentin.enovation.model.SubscriptionType;
import com.greentin.enovation.model.UserMenuAccess;

public interface UserDaoInterface {

	boolean saveProduct(Product product);

	boolean isProductExists(Product product);

	boolean updateProduct(Product product);

	List<Product> getProductList();

	boolean isMenuExists(Menu menu);

	boolean saveMenu(Menu menu);

	boolean updateMenu(Menu menu);

	boolean isSubMenuExists(SubMenu submenu);

	boolean saveSubMenu(SubMenu submenu);

	boolean updateSubMenu(SubMenu submenu);

	List<Product> getActiveProductList();

	List<Menu> getMenuListByProductId(Integer productId);

	boolean disableProduct(Product product);

	boolean disableSubMenu(SubMenu submenu);

	List<EmployeeDetails> isUseremailExists(String email);

	boolean registration(Registration user);

	Registration verifyEmail(Registration user);

	boolean saveUser(Users user);

	List<Users> getRegisterUserList();

	List<Users> getGreentinUserList();

	boolean checkEmailVerified(Registration user);

	List<Users> login(Users user);

	boolean isEmailverified(Users user);

	List<Role> getListofRole();

	boolean saveRoleMenuAccess(RoleMenuAccess role);

	boolean updateRoleMenuAccess(RoleMenuAccess role);

	List<RoleMenuAccess> getListofRoleAccess(Integer orgId, Integer branchId, Integer roleId);

	boolean saveRoleAccessList(Set<RoleMenuAccess> role);
	
	List<MenuData> getMenuListByRoleId(Integer orgId, Integer branchId, Integer roleId);
	
	List<MenuData> getMenuListByRoleIdNew(Integer orgId, Integer branchId, Integer roleId, Integer productId);

	List<SubscriptionType> getListofSubscriptionType();

	boolean updateSetupComleted(ProductOrgConfig request);

	List<MenuData> getMenuListByMultipleRoleId(int orgId, int branchId, Set<Role> roles);

	String forgotPassword(EmployeeDetails request);

	boolean saveDemoRequest(DemoRequest request);

	List<DemoRequest> demoRequestList();

	boolean isDemoRequestEmailExists(String email);

	
	boolean isRegistrationemailExists(String email);

	boolean saveRegistration(Registration request);

	boolean isOrgDomaimExists(Registration user);

	boolean saveRegUser(EmployeeDetails registration, Registration user);

	List<EmployeeDetails> isEmployeeEmailExists(String email);

	List<EmployeeDetails> getEmpListByOrg(String orgDomain);

	ProductOrgConfig getProductOrgConfigDet(EmployeeDetails login);
    
	String getNoOfDaysRemaining(long id,int branchId);
	Registration checkLoginRegisdtration(EmployeeDetails employee);

	EmployeeDetails checkLoginEmployee(EmployeeDetails employee);

	public List<EmployeeDetails> getEmpListByOrg(String orgDomain,String emailId);
	
	public boolean reSendMail(Registration request);
	
	public List<Registration> isRegisterEmail(String email);
	
	public boolean reSendMailToEmployee(EmployeeDetails employee);

	void updateFCMKey(int empId, String fcmKey);

	boolean logout(EmployeeDetails emp);

	BranchMaster getBranchDetails(int branchId);

	List<MultiBranchAccess> getOrgExeBranchList(String email);

	List<ModuleSubscription> getMenuListByBranchIdOrOrgId(Integer branchId,Integer orgId);

	List<MenuData> getMenuListByModuleSubscription(int orgId, int branchId, Set<Role> roles, EmployeeDetails emp);

	boolean saveBranchAccessSetup(BranchAccessSetup request);
	
	List<BranchAccessSetup> getBranchAccessSetup(int orgId);
	
	boolean deleteBranchAccessSetup(int id);

	List<BranchAccessSetup> getBranchAccessSetupByEmpId(int orgId, int empId);
	
	List<EmployeeDetailsBO> getUserAccessList(UserMenuAccess request);
	
	boolean addOrUpdateUserAccess(MenuDTO request);

}
