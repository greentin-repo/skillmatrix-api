package com.greentin.enovation.accesscontrol;

import java.util.HashMap;
import java.util.List;

import com.greentin.enovation.beans.EmployeeDetailsBean;
import com.greentin.enovation.beans.MenuData;
import com.greentin.enovation.bo.model.EmployeeDetailsBO;
import com.greentin.enovation.employee.EmployeeDTO;
import com.greentin.enovation.model.BranchAccessSetup;
import com.greentin.enovation.model.ModuleSubscription;
import com.greentin.enovation.model.MultiBranchAccess;
import com.greentin.enovation.model.RoleMenuAccess;
import com.greentin.enovation.model.SubscriptionType;

public class UserResponse {

	private String status;

	private Integer statusCode;

	private boolean result;

	private String reason;

	private UsersBean userDetails;

	private List<Product> productList;

	private List<Menu> menuList;

	private List<Users> usersList;

	private List<Role> roleList;

	private List<RoleMenuAccess> roleAccessList;

	private List<MenuData> menuDataList;

	private List<SubscriptionType> listofSubscriptionType;

	private List<DemoRequest> demoRequestList;

	private List<MultiBranchAccess> multiBranchList;

	private String authToken;

	private String masterStatus;

	private String email;

	private String portalLink;

	private EmployeeDetailsBean employeeDetails;

	private String noOFDaysRemaining;

	private boolean resendEmail;

	private String appVersion;

	private int transactionId;

	private List<ModuleSubscription> moduleList;

	private List<BranchAccessSetup> branchAccessList;

	private List<EmployeeDetailsBO> empList;

	private EmployeeDTO empDto;
	
	private HashMap<String, Object> data;

	public int getTransactionId() {
		return transactionId;
	}

	public void setTransactionId(int transactionId) {
		this.transactionId = transactionId;
	}

	public boolean isResendEmail() {
		return resendEmail;
	}

	public void setResendEmail(boolean resendEmail) {
		this.resendEmail = resendEmail;
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

	public List<Product> getProductList() {
		return productList;
	}

	public void setProductList(List<Product> productList) {
		this.productList = productList;
	}

	public List<Menu> getMenuList() {
		return menuList;
	}

	public void setMenuList(List<Menu> menuList) {
		this.menuList = menuList;
	}

	public List<Users> getUsersList() {
		return usersList;
	}

	public void setUsersList(List<Users> usersList) {
		this.usersList = usersList;
	}

	public UsersBean getUserDetails() {
		return userDetails;
	}

	public void setUserDetails(UsersBean userDetails) {
		this.userDetails = userDetails;
	}

	public List<Role> getRoleList() {
		return roleList;
	}

	public void setRoleList(List<Role> roleList) {
		this.roleList = roleList;
	}

	public List<RoleMenuAccess> getRoleAccessList() {
		return roleAccessList;
	}

	public void setRoleAccessList(List<RoleMenuAccess> roleAccessList) {
		this.roleAccessList = roleAccessList;
	}

	public List<MenuData> getMenuDataList() {
		return menuDataList;
	}

	public void setMenuDataList(List<MenuData> menuDataList) {
		this.menuDataList = menuDataList;
	}

	public List<SubscriptionType> getListofSubscriptionType() {
		return listofSubscriptionType;
	}

	public void setListofSubscriptionType(List<SubscriptionType> listofSubscriptionType) {
		this.listofSubscriptionType = listofSubscriptionType;
	}

	public String getAuthToken() {
		return authToken;
	}

	public void setAuthToken(String authToken) {
		this.authToken = authToken;
	}

	public String getMasterStatus() {
		return masterStatus;
	}

	public void setMasterStatus(String masterStatus) {
		this.masterStatus = masterStatus;
	}

	public List<DemoRequest> getDemoRequestList() {
		return demoRequestList;
	}

	public void setDemoRequestList(List<DemoRequest> demoRequestList) {
		this.demoRequestList = demoRequestList;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public EmployeeDetailsBean getEmployeeDetails() {
		return employeeDetails;
	}

	public void setEmployeeDetails(EmployeeDetailsBean employeeDetails) {
		this.employeeDetails = employeeDetails;
	}

	public String getNoOFDaysRemaining() {
		return noOFDaysRemaining;
	}

	public void setNoOFDaysRemaining(String noOFDaysRemaining) {
		this.noOFDaysRemaining = noOFDaysRemaining;
	}

	public String getPortalLink() {
		return portalLink;
	}

	public void setPortalLink(String portalLink) {
		this.portalLink = portalLink;
	}

	public String getAppVersion() {
		return appVersion;
	}

	public void setAppVersion(String appVersion) {
		this.appVersion = appVersion;
	}

	public List<MultiBranchAccess> getMultiBranchList() {
		return multiBranchList;
	}

	public void setMultiBranchList(List<MultiBranchAccess> multiBranchList) {
		this.multiBranchList = multiBranchList;
	}

	public List<ModuleSubscription> getModuleList() {
		return moduleList;
	}

	public void setModuleList(List<ModuleSubscription> moduleList) {
		this.moduleList = moduleList;
	}

	public List<BranchAccessSetup> getBranchAccessList() {
		return branchAccessList;
	}

	public void setBranchAccessList(List<BranchAccessSetup> branchAccessList) {
		this.branchAccessList = branchAccessList;
	}

	public List<EmployeeDetailsBO> getEmpList() {
		return empList;
	}

	public void setEmpList(List<EmployeeDetailsBO> empList) {
		this.empList = empList;
	}

	public EmployeeDTO getEmpDto() {
		return empDto;
	}

	public void setEmpDto(EmployeeDTO empDto) {
		this.empDto = empDto;
	}

	public HashMap<String, Object> getData() {
		return data;
	}

	public void setData(HashMap<String, Object> data) {
		this.data = data;
	}

}
