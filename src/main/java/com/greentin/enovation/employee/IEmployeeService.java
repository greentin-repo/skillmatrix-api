package com.greentin.enovation.employee;

import java.util.List;

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

public interface IEmployeeService {

	EmployeeDTO getAllEmplyeeDetailsService();

	EmployeeDTO importEmplyeeDetailsService(Integer orgId, Integer empId, String orgName, MultipartFile empExcel);

	EmployeeDTO getEmpListByTeamTypeIdService(Integer branchId, Integer teamTypeId);

	EmployeeDTO updateAPNKeyService(UpdateAPNKeyFCMKeyBean aPNKeyDet);

	EmployeeDTO updateProfilePicService(Integer empId, MultipartFile profilePic);

	EmployeeDTO saveEmployee(EmployeeDetails empDetails);

	EmployeeDTO getEmployeeByEmpId(Integer empId);

	EmployeeDTO updateProfileDetails(EmployeeDetails empdetails);

	EmployeeDTO loginApp(EmployeeDetails employeeDetails);

	EmployeeDTO removeEmployee(EmployeeDetails empdetails);

	EmployeeDTO deactiveEmployee(EmployeeDetails emp);

	EmployeeDTO updateEmployeeDetails(EmployeeDetails empdetails);

	EmployeeDTO getAllEmplyeeListByBranch(Integer orgId, Integer branchId, Integer deptId);

	EmployeeDTO getAllEmpListForExecutor(Integer branchId);

	EmployeeDTO createEmployee(EmployeeDetails empDetails);

	EmployeeDTO employeeVerify(EmployeeDetails emp);

	EmployeeDTO getEmpListRolewise(Integer branchId, Integer roleId);

	EmployeeDTO saveOrUpdateEmpRoles(List<SaveEmpRolesBean> request);

	EmployeeDTO importBulkEmplyee(EmployeeDetails empdetails);

	EmployeeDTO resetPassword(EmployeeDetails request);

	EmployeeDTO removeProfilePic(Integer empId);

	EmployeeDTO sendRoleUpdateEmails(Integer branchId);

	EmployeeDTO checkLoginEmail(String email);

	EmployeeDTO employeeVerifyforForgotPassword(EmployeeDetails emp);

	EmployeeDTO getAllEmplyeeListByBranchNew(Integer orgId, Integer branchId, Integer deptId);

	EmployeeDTO saveFeedback(Feedback feedback);

	EmployeeDTO getFeedbackList();

	EmployeeDTO resetPasswordBySuperadmin(EmployeeDetails empdetails);

	EmployeeDTO getNoticeDetails(Integer branchId, Integer deptId);

	EmployeeDTO addSuggetionTemplate(SuggestionTemplate temp);

	EmployeeDTO getEmpListRolewiseOrgLevel(Integer orgId, Integer roleId);

	EmployeeDTO getEmpRolesListNew(EmployeeDetails empdetails);

	EmployeeDTO getAllEmpListByBrannchIds(EmployeeDTO request);

	EmployeeDTO saveLoginFeedback(Feedback feedback);

	EmployeeDTO getEmployeeLevelDetails(int branchId);

	UserResponse getModuleWisePendingActions(int empId);

	UserResponse transferModuleWisePendingActions(EnovationDTO req);

	UserResponse deactivateEmployee(int empId);

	UserResponse employeeTransfer(EmployeeTransferDTO request);

	EmployeeDTO addUpdateEmployeeLevel(EmployeeHierarchy request);

	EmployeeDTO sendMailToSuperAdminForPasswordReset(EmployeeDTO request);

	/**
	 * @author shyam 20 June 2020
	 * @param request
	 * @return
	 */
	MasterResponse bulkEmployeeUpload(EmployeeDTO request);

	MasterResponse importBulkEmployeeData(EmployeeDTO request);

	EmployeeDTO reActivationEmployee(EmployeeDTO request);

}
