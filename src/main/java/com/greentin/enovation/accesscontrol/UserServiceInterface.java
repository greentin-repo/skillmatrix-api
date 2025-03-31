package com.greentin.enovation.accesscontrol;

import java.util.Set;

import com.greentin.enovation.employee.EmployeeDTO;
import com.greentin.enovation.model.BranchAccessSetup;
import com.greentin.enovation.model.EmployeeDetails;
import com.greentin.enovation.model.RoleMenuAccess;
import com.greentin.enovation.model.UserMenuAccess;

public interface UserServiceInterface {

	UserResponse saveProduct(Product product);

	UserResponse updateProduct(Product product);

	UserResponse getProductList();

	UserResponse saveMenu(Menu menu);

	UserResponse updateMenu(Menu menu);

	UserResponse saveSubMenu(SubMenu submenu);

	UserResponse updateSubMenu(SubMenu submenu);

	UserResponse getActiveProductList();

	UserResponse getMenuListByProductId(Integer productId);

	UserResponse disableSubMenu(SubMenu subMenu);

	UserResponse disableProduct(Product product);

	UserResponse registration(Registration user);

	UserResponse verifyEmail(Registration user);

	UserResponse saveUser(Users user);

	UserResponse getRegisterUserList();

	UserResponse getGreentinUserList();

	UserResponse login(EmployeeDetails user);

	UserResponse getListofRole();

	UserResponse saveRoleMenuAccess(RoleMenuAccess role);

	UserResponse updateRoleMenuAccess(RoleMenuAccess role);

	UserResponse getListofRoleAccess(Integer roleId, Integer branchId, Integer roleId2);
	
	UserResponse getListofRoleAccessNew(Integer orgId, Integer branchId, Integer roleId, Integer productId);

	UserResponse saveRoleAccessList(Set<RoleMenuAccess> role);

	UserResponse getListofSubscriptionType();

	UserResponse updateSetupComleted(ProductOrgConfig request);

	UserResponse isEmailVerify(String email);

	UserResponse forgotPassword(EmployeeDetails request);

	UserResponse saveDemoRequest(DemoRequest request);

	UserResponse demoRequestList();

	UserResponse saveRegistration(Registration request);

	UserResponse resendEmail(Registration user);
    
	UserResponse appVersion();

	UserResponse logout(EmployeeDetails emp);

	UserResponse getMenuListByBranchIdOrOrgId(Integer branchId,Integer orgId);
	
	UserResponse saveBranchAccessSetup(BranchAccessSetup request);
	
	UserResponse getBranchAccessSetup(int orgId);
	
	UserResponse deleteBranchAccessSetup(int id);

	UserResponse getBranchAccessSetupByEmpId(int orgId,int empId);
	
	UserResponse getUserAccessList(UserMenuAccess request);
	
	UserResponse addOrUpdateUserAccess(MenuDTO request);

}
