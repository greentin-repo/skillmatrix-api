package com.greentin.enovation.employee;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import com.greentin.enovation.beans.ImportEmployeeErrorList;
import com.greentin.enovation.beans.SaveEmpRolesBean;
import com.greentin.enovation.beans.UpdateAPNKeyFCMKeyBean;
import com.greentin.enovation.dto.EmployeeTransferDTO;
import com.greentin.enovation.dto.EnovationDTO;
import com.greentin.enovation.model.EmployeeDetails;
import com.greentin.enovation.model.EmployeeHierarchy;
import com.greentin.enovation.model.Feedback;
import com.greentin.enovation.model.FeedbackType;
import com.greentin.enovation.model.MoodIndicator;
import com.greentin.enovation.model.MultiBranchAccess;
import com.greentin.enovation.model.NoticeSetup;
import com.greentin.enovation.model.SuggestionTemplate;
import com.greentin.enovation.model.TeamtypeMaster;
import com.greentin.enovation.response.MasterResponse;

public interface IEmployeeDao {

	List<EmployeeDetails> loginApp(EmployeeDetails employeeDetails);

	List<EmployeeDetails> getAllEmplyeeDetails();

	List<EmployeeDetails> retriveAllEmpDetFromExcel(Integer orgId, String orgName, MultipartFile empExcel);

	boolean checkEmpIdsfromExcel(List<EmployeeDetails> empAllDetails);

	boolean SaveAllEmpExcel(Integer empId, List<EmployeeDetails> empAllDetails);

	List<EmployeeDetails> getEmpTeamDetails(Integer branchId, Integer teamTypeId);

	List<EmployeeDetails> getEmpTeamList();

	HashMap<String, List<ImportEmployeeErrorList>> checkExcelFormat(String orgAlies, MultipartFile empExcel);

	boolean updateAPNKey(UpdateAPNKeyFCMKeyBean aPNKeyDet);

	String updateProfilePic(Integer empId, MultipartFile profilePic);

	List<TeamtypeMaster> checkEmpInWhichTeam(int i);

	boolean isContactExist(EmployeeDetails empDetails);

	boolean saveEmployee(EmployeeDetails empDetails);

	EmployeeDetails getEmployeeByEmpId(Integer empId);

	boolean updateProfileDetails(EmployeeDetails empdetails);

	boolean removeEmployee(EmployeeDetails empdetails);

	boolean isDactiveLogin(EmployeeDetails employeeDetails);

	boolean isEmailExist(EmployeeDetails empDetails);

	boolean isEmployeeIDExist(EmployeeDetails empDetails);

	boolean isUserIsExecutive(EmployeeDetails employeeDetails);

	int deactiveEmployee(EmployeeDetails emp);

	int updateEmployeeDetails(EmployeeDetails empdetails);

	List<EmployeeDetails> getAllEmplyeeListByBranch(Integer orgId, Integer branchId, Integer deptId);

	List<EmployeeDetails> getAllEmpListForExecutor(Integer branchId);

	boolean createEmployee(List<EmployeeDetails> list);

	EmployeeDetails employeeVerify(String token);

	List<EmployeeDetails> getEmpListRolewise(Integer branchId, Integer roleId);

	int saveOrUpdateEmpRoles(List<SaveEmpRolesBean> request);

	boolean isEmailExist(String empDetails);

	int resetPassword(EmployeeDetails request);

	boolean removeProfilePic(Integer empId);

	int checkExeclFormatOrBlank(String alies, MultipartFile excel);

	int isTokenVerified(String token);

	boolean sendRoleUpdateEmails(Integer branchId);

	EmployeeDetails checkLoginEmail(String email);

	int isTokenVerifiedForForgotPassword(String token);

	boolean isMobileNumberExist(String mobileNumber);

	List<EmployeeDetails> getAllEmplyeeListByBranchNew(Integer orgId, Integer branchId, Integer deptId);

	boolean createBulkEmployee(List<EmployeeDetails> empListDetails);

	boolean saveFeedback(Feedback feedback);

	List<FeedbackType> getFeedbackList();

	boolean resetPasswordBySuperadmin(EmployeeDetails empdetails);

	List<MultiBranchAccess> getOrgExeBranchList(String email);

	List<MoodIndicator> getMoodRatingforToday(int empId, int branchId);

	List<NoticeSetup> getNoticeDetails(Integer branchId, Integer deptId);

	boolean addSuggetionTemplate(SuggestionTemplate temp);

	List<EmployeeDetails> getEmpListRolewiseOrgLevel(Integer orgId, Integer roleId);

	List<EmployeeDetails> getEmpRolesListNew(EmployeeDetails empdetails);

	List<EmployeeDetails> getAllEmpListByBrannchIds(EmployeeDTO request);

	Map<String, Object> saveLoginFeedback(Feedback feedback);

	List<EmployeeHierarchy> getEmployeeLevelDetails(int branchId);

	EmployeeDTO getModuleWisePendingActions(int empId);

	boolean transferModuleWisePendingActions(EnovationDTO req);

	int deactivateEmployee(int empId);

	boolean employeeTransfer(EmployeeTransferDTO request);

	boolean addUpdateEmployeeLevel(EmployeeHierarchy request);

	boolean sendMailToSuperAdminForPasswordReset(EmployeeDTO request);

	String bulkEmployeeUpload(EmployeeDTO request, MasterResponse response) throws InvalidFormatException, IOException, org.apache.poi.openxml4j.exceptions.InvalidFormatException;

	EmployeeDTO importBulkEmployeeData(EmployeeDTO request);

	boolean reActivationEmployee(EmployeeDTO request);
}
