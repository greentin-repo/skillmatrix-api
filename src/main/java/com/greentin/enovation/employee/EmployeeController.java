package com.greentin.enovation.employee;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.greentin.enovation.accesscontrol.UserResponse;
import com.greentin.enovation.beans.SaveEmpRolesBean;
import com.greentin.enovation.beans.UpdateAPNKeyFCMKeyBean;
import com.greentin.enovation.dto.EmployeeTransferDTO;
import com.greentin.enovation.dto.EnovationDTO;
import com.greentin.enovation.model.EmployeeDetails;
import com.greentin.enovation.model.EmployeeHierarchy;
import com.greentin.enovation.model.Feedback;
import com.greentin.enovation.model.SuggestionTemplate;
import com.greentin.enovation.response.MasterResponse;

@CrossOrigin
@RestController
public class EmployeeController {
	
	
	@Autowired
	EmployeeDaoImple EmployeeDaoImple;

	@Autowired
	IEmployeeService iEmployeeService;

	@PostMapping(value ="/loginapp")
	public EmployeeDTO loginApp(@RequestBody EmployeeDetails employeeDetails){
		return iEmployeeService.loginApp(employeeDetails);
	}

	@GetMapping(value ="/getEmpListByTeamTypeId/{branchId}/{id}") ///records retrieve from team Details Table  //depricated
	public EmployeeDTO getEmpListByTeamTypeId(@PathVariable(name="branchId") Integer branchId,@PathVariable(name="id") Integer teamTypeId){
		return iEmployeeService.getEmpListByTeamTypeIdService(branchId,teamTypeId);
	}

	@GetMapping(value ="/getAllEmpList")   //depricated
	public EmployeeDTO getAllEmplyeeDetails(){
		return iEmployeeService.getAllEmplyeeDetailsService();
	}

	@PostMapping(value ="/importEmplyee") // depricated
	public EmployeeDTO importEmplyeeDetails(@ModelAttribute EmployeeDetails empdetails ){
		return iEmployeeService.importEmplyeeDetailsService(empdetails.getOrgId(),empdetails.getEmpId(),empdetails.getOrgName(),empdetails.getExcel());
	}
	
	//FE:=> orgId,branchId
	@PostMapping(value ="/importBulkEmplyee")
	public EmployeeDTO importBulkEmplyee(@ModelAttribute EmployeeDetails empdetails ){
		return iEmployeeService.importBulkEmplyee(empdetails);
	}
	
	@PostMapping(value ="/updateProfilePic")
	public EmployeeDTO updateProfilePic(@RequestParam(name="empId") Integer empId , @RequestParam(name="profilePic") MultipartFile profilePic){
		return iEmployeeService.updateProfilePicService(empId,profilePic);
	}
	@PostMapping(value ="/updateProfileDetails")
	public EmployeeDTO updateProfileDetails(@RequestBody EmployeeDetails empdetails){
		return iEmployeeService.updateProfileDetails(empdetails);
	}

	@PostMapping(value ="/removeemployee")
	public EmployeeDTO removeEmployee(@RequestBody EmployeeDetails empdetails){
		return iEmployeeService.removeEmployee(empdetails);
	}

	@PostMapping(value ="/updateAPNKey")
	public EmployeeDTO updateAPNKey(@RequestBody UpdateAPNKeyFCMKeyBean APNKeyDet){
		return iEmployeeService.updateAPNKeyService(APNKeyDet);
	}

	@GetMapping(value ="/getEmployeeByEmpId/{id}")
	public EmployeeDTO getEmployeeByEmpId(@PathVariable(name="id") Integer  empId){
		if(empId == null ) {empId = 0;}
		return iEmployeeService.getEmployeeByEmpId(empId);
	}

	@PostMapping(value ="/saveEmployee")
	public EmployeeDTO saveEmployee(@RequestBody EmployeeDetails  empDetails){
		return iEmployeeService.saveEmployee(empDetails);
	}

	@PostMapping(value ="/createemployee")//new API with role --users--employee details mapping
	public EmployeeDTO createEmployee(@RequestBody EmployeeDetails  empDetails){
		return iEmployeeService.createEmployee(empDetails);
	}

	@PostMapping(value ="/deactiveEmployee")
	public EmployeeDTO deactiveEmployee(@RequestBody EmployeeDetails emp){
		return iEmployeeService.deactiveEmployee(emp);
	}

	@PostMapping(value ="/updateemployeedetails")
	public EmployeeDTO updateEmployeeDetails(@RequestBody EmployeeDetails empdetails){
		return iEmployeeService.updateEmployeeDetails(empdetails);
	}

	@GetMapping(value ="/getAllEmpListNEW/{orgId}/{branchId}/{deptId}")//Depricated
	public EmployeeDTO getAllEmplyeeListByBranch(@PathVariable(name="orgId") Integer  orgId, @PathVariable(name="branchId") Integer  branchId, @PathVariable(name="deptId") Integer  deptId ){
		if(orgId==null) {orgId=0;}
		if(branchId==null) {branchId=0;}
		if(deptId==null) {deptId=0;}
		return iEmployeeService.getAllEmplyeeListByBranch(orgId,branchId,deptId);
	}
	
	@GetMapping(value ="/getAllEmpList/{orgId}/{branchId}/{deptId}")
	public EmployeeDTO getAllEmplyeeListByBranchNew(@PathVariable(name="orgId") Integer  orgId, @PathVariable(name="branchId") Integer  branchId, @PathVariable(name="deptId") Integer  deptId ){
		if(orgId==null) {orgId=0;}
		if(branchId==null) {branchId=0;}
		if(deptId==null) {deptId=0;}
		return iEmployeeService.getAllEmplyeeListByBranchNew(orgId,branchId,deptId);
	}
	
	@PostMapping(value ="/getAllEmpListByBrannchIds")
	public EmployeeDTO getAllEmpListByBrannchIds(@RequestBody EmployeeDTO request){
		return iEmployeeService.getAllEmpListByBrannchIds(request);
	}
	
	@GetMapping(value ="/getemplistrolewise/{branchId}/{roleId}")
	public EmployeeDTO getEmpListRolewise(@PathVariable(name="roleId") Integer  roleId, @PathVariable(name="branchId") Integer  branchId){
		if(branchId == null ) {branchId =0;}
		if(roleId == null) {roleId =0;}
		return iEmployeeService.getEmpListRolewise(branchId,roleId);
	}
	
	@GetMapping(value ="/getemplistrolewiseorglevel/{orgId}/{roleId}")
	public EmployeeDTO getEmpListRolewiseOrgLevel(@PathVariable(name="roleId") Integer  roleId, @PathVariable(name="orgId") Integer  orgId){
		if(orgId == null ) {orgId =0;}
		if(roleId == null) {roleId =0;}
		return iEmployeeService.getEmpListRolewiseOrgLevel(orgId,roleId);
	}
	
	@GetMapping(value ="/getAllEmpListForExecutor/{branchId}")
	public EmployeeDTO getAllEmpListForExecutor(@PathVariable(name="branchId") Integer  branchId){
		if(branchId == null ) {branchId =0;}
		return iEmployeeService.getAllEmpListForExecutor(branchId);
	}
	
	@PostMapping(value ="/employeeverify")
	public EmployeeDTO employeeVerify(@RequestBody EmployeeDetails emp){
		return iEmployeeService.employeeVerify(emp);
	}
	
	@PostMapping(value ="/employeeverifyforforgotpassword")
	public EmployeeDTO employeeverifyforforgotpassword(@RequestBody EmployeeDetails emp){
		return iEmployeeService.employeeVerifyforForgotPassword(emp);
	}
	
	@PostMapping(value ="/saveorupdateemproles")
	public EmployeeDTO saveOrUpdateEmpRoles(@RequestBody List<SaveEmpRolesBean> request){
		return iEmployeeService.saveOrUpdateEmpRoles(request);
	}
	
	
	@PostMapping(value ="/resetpassword")
	public EmployeeDTO resetPassword(@RequestBody EmployeeDetails request){
		return iEmployeeService.resetPassword(request);
	}
	
	@GetMapping(value ="/removeprofilepic/{empId}")
	public EmployeeDTO removeProfilePic(@PathVariable("empId") Integer empId){
		if(empId == null) {empId =0;}
		return iEmployeeService.removeProfilePic(empId);
	}
	
	@GetMapping(value ="/sendroleupdateemails/{branchId}")
	public EmployeeDTO sendRoleUpdateEmails(@PathVariable("branchId") Integer branchId){
		return iEmployeeService.sendRoleUpdateEmails(branchId);
	}
	
	@GetMapping(value ="/checkloginemail/{email}")
	public EmployeeDTO checkLoginEmail(@PathVariable("email") String email){
		return iEmployeeService.checkLoginEmail(email);
	}
	
	@PostMapping("/saveFeedback")
	public EmployeeDTO saveFeedback(@RequestBody Feedback feedback) {	
	return iEmployeeService.saveFeedback(feedback);
	}
	
	@GetMapping("/getFeedbackList")
	public EmployeeDTO getFeedbackList() {	
	return iEmployeeService.getFeedbackList();
	}
	
	@PostMapping("/resetpasswordbysuperadmin")
	public EmployeeDTO resetPasswordBySuperadmin(@RequestBody EmployeeDetails empdetails) {	
	return iEmployeeService.resetPasswordBySuperadmin(empdetails);
	}
	
	@GetMapping(value ="/getNoticeDetails/{branchId}/{deptId}")
	public EmployeeDTO getNoticeDetails(@PathVariable("branchId") Integer branchId,@PathVariable("deptId") Integer deptId){
		return iEmployeeService.getNoticeDetails(branchId,deptId);
	}
	
	@GetMapping(value ="/addSuggetionTemplate")
	public EmployeeDTO addSuggetionTemplate(@RequestBody SuggestionTemplate temp){
		return iEmployeeService.addSuggetionTemplate(temp);
	}
	
	@PostMapping("/getEmpRolesListNew")
	public EmployeeDTO getEmpRolesListNew(@RequestBody EmployeeDetails empdetails) {	
	return iEmployeeService.getEmpRolesListNew(empdetails);
	}
	//************************************************************************************************************
			//DON'T REMOVE test from /saveLoginFeedback API
	@PostMapping("test/saveLoginFeedback")
	public EmployeeDTO saveLoginFeedback(@RequestBody Feedback feedback) {	
	return iEmployeeService.saveLoginFeedback(feedback);
	}
	//*************************************************************************************************************
	
	@GetMapping("/getEmployeeLevelDetails/{branchId}")
	public EmployeeDTO getEmployeeLevelDetails(@PathVariable("branchId") Integer branchId) {
		return iEmployeeService.getEmployeeLevelDetails(branchId);
	}
	
	/**
	 * @author Vinay B. Dec 27, 2021 1:47:15 PM
	 */
	@GetMapping(value = "/getModuleWisePendingActions/{empId}")
	public UserResponse getModuleWisePendingActions(@PathVariable("empId") int empId) {
		return iEmployeeService.getModuleWisePendingActions(empId);
	}

	/**
	 * @author Vinay B. Dec 27, 2021 3:00:29 PM
	 */
	@PostMapping(value = "/transferModuleWisePendingActions")
	public UserResponse transferModuleWisePendingActions(@RequestBody EnovationDTO req) {
		return iEmployeeService.transferModuleWisePendingActions(req);
	}
	
	/**
	 * @author Vinay B. May 19, 2022 11:06:26 AM
	 */
	@GetMapping(value = "/deactivateEmployeeRecord/{empId}")
	public UserResponse deactivateEmployee(@PathVariable("empId") int empId) {
		return iEmployeeService.deactivateEmployee(empId);
	}

	@PostMapping(value = "/employeeTransfer")
	public UserResponse employeeTransfer(@RequestBody EmployeeTransferDTO request) {
		return iEmployeeService.employeeTransfer(request);
	}
	
	@PostMapping("/addUpdateEmployeeLevel")
	public EmployeeDTO addUpdateEmployeeLevel(@RequestBody EmployeeHierarchy request) {
		return iEmployeeService.addUpdateEmployeeLevel(request);
	}

	@PostMapping("/sendMailToSuperAdminForPasswordReset")
	public EmployeeDTO sendMailToSuperAdminForPasswordReset(@RequestBody EmployeeDTO request) {
		return iEmployeeService.sendMailToSuperAdminForPasswordReset(request);
	}

	/**
	 * @author Rakesh 07 sept 2020
	 * @param request
	 * @return
	 */
	@PostMapping(value = "/bulkEmployeeUpload")
	public MasterResponse bulkEmployeeUpload(@ModelAttribute EmployeeDTO request) {
		return iEmployeeService.bulkEmployeeUpload(request);
	}
	
	/**
	 * @author Aditi V. 17 feb 2021
	 * @param request
	 * @return
	 */
	@PostMapping(value = "/importBulkEmployeeData")
	public MasterResponse importBulkEmployeeData(@ModelAttribute EmployeeDTO request) {
		return iEmployeeService.importBulkEmployeeData(request);
	}
	
	/**
	 * @author Anant K. September 22, 2023 03:00:46 PM
	 */
	@PostMapping("/reActivationEmployee")
	public EmployeeDTO reActivationEmployee(@RequestBody EmployeeDTO request) {

		return iEmployeeService.reActivationEmployee(request);

	}
}
