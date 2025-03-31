package com.greentin.enovation.employee;

import static java.util.Comparator.comparingInt;
import static java.util.stream.Collectors.collectingAndThen;
import static java.util.stream.Collectors.toCollection;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.google.gson.Gson;
import com.greentin.enovation.accesscontrol.ProductOrgConfig;
import com.greentin.enovation.accesscontrol.Role;
import com.greentin.enovation.accesscontrol.RoleName;
import com.greentin.enovation.accesscontrol.UserDaoInterface;
import com.greentin.enovation.accesscontrol.UserResponse;
import com.greentin.enovation.beans.EmployeeDetailsBean;
import com.greentin.enovation.beans.ImportEmployeeErrorList;
import com.greentin.enovation.beans.SaveEmpRolesBean;
import com.greentin.enovation.beans.UpdateAPNKeyFCMKeyBean;
import com.greentin.enovation.dto.EmployeeTransferDTO;
import com.greentin.enovation.dto.EnovationDTO;
import com.greentin.enovation.master.IMasterDao;
import com.greentin.enovation.model.BranchMaster;
import com.greentin.enovation.model.DepartmentMaster;
import com.greentin.enovation.model.EmployeeDetails;
import com.greentin.enovation.model.EmployeeHierarchy;
import com.greentin.enovation.model.Feedback;
import com.greentin.enovation.model.FeedbackType;
import com.greentin.enovation.model.MoodIndicator;
import com.greentin.enovation.model.MultiBranchAccess;
import com.greentin.enovation.model.NoticeSetup;
import com.greentin.enovation.model.OrganizationMaster;
import com.greentin.enovation.model.SuggestionTemplate;
import com.greentin.enovation.model.TeamtypeMaster;
import com.greentin.enovation.response.MasterResponse;
import com.greentin.enovation.utils.BuildResponse;
import com.greentin.enovation.utils.CommonUtils;
import com.greentin.enovation.utils.Constants;
import com.greentin.enovation.utils.EnovationConstants;

@Service
public class EmployeeServiceImple implements IEmployeeService {
	@SuppressWarnings("unused")
	private Integer count;
	private static final Logger LOGGER = LoggerFactory.getLogger(EmployeeServiceImple.class);
	@Autowired
	IEmployeeDao iEmployeeDao;

	@Autowired
	IMasterDao iMasterDao;

	@Autowired
	PasswordEncoder password;

	@Autowired
	EmployeeDaoImple employeeDaoImp;

	@Autowired
	UserDaoInterface userdaoinfc;

	@Autowired
	CommonUtils commonUtils;

	@Override
	public EmployeeDTO loginApp(EmployeeDetails employeeDetails) {
		LOGGER.info("#INSIDE IN LOGINAPP API");
		EmployeeDTO employeeDTO = new EmployeeDTO();
		try {
			List<EmployeeDetails> enployeeList = iEmployeeDao.loginApp(employeeDetails);
			if (enployeeList != null && enployeeList.size() > 0) {
				boolean isDactiveLogin = iEmployeeDao.isDactiveLogin(employeeDetails);
				LOGGER.info("#INSIDE IN isDactiveLogin val is : t/f " + isDactiveLogin);
				if (isDactiveLogin) {
					employeeDTO.setStatus(Constants.NOT_EXIST);
					employeeDTO.setStatusCode(EnovationConstants.Code200);
					employeeDTO.setResult(EnovationConstants.ResultFalse);
					employeeDTO.setReason(EnovationConstants.DEACTIVATED_ACCOUNT);
				} else {
					EmployeeDetails tmp_emp = enployeeList.get(0);
					EmployeeDetailsBean empDetails = new EmployeeDetailsBean();
					if (password.matches(employeeDetails.getPassword(), tmp_emp.getPassword())) {
						/*
						 * if(employeeDetails.getFcmKey()!=null) {
						 * iEmployeeDao.updateFCMKey(tmp_emp.getEmpId(),employeeDetails.getFcmKey()); }
						 */
						// verify user in team management or not
						/*
						 * List<TeamtypeMaster>
						 * empTeamType=iEmployeeDao.checkEmpInWhichTeam(tmp_emp.getEmpId());
						 * 
						 * //verify user role is executive or not boolean
						 * executiveRole=iEmployeeDao.isUserIsExecutive(tmp_emp);
						 * 
						 * if(tmp_emp.getDepartmentLevel()!= null &&
						 * !tmp_emp.getDepartmentLevel().isEmpty()) {//verify user in department level
						 * or not
						 * if(tmp_emp.getDepartmentLevel().get(0).getIsExecutive()==EnovationConstants.
						 * DEPARTMENT_LEVEL_1) { empDetails.setRole(EnovationConstants.ROLECONTROLLER);
						 * }else { empDetails.setRole(EnovationConstants.DEPARTMENTLEVEL); } }else
						 * if(empTeamType!=null && empTeamType.size()>0) {
						 * empDetails.setTeamDetails(empTeamType);
						 * empDetails.setRole(empTeamType.get(0).getType()); }else if(executiveRole){
						 * empDetails.setRole(EnovationConstants.ROLEEXECUTIVE); }else {
						 * empDetails.setRole(EnovationConstants.ROLENOTFOUND); }
						 */

						employeeDTO.setStatus(EnovationConstants.statusSuccess);
						employeeDTO.setStatusCode(EnovationConstants.Code200);
						employeeDTO.setResult(EnovationConstants.ResultTrue);
						BeanUtils.copyProperties(tmp_emp, empDetails);
						if (tmp_emp.getProfilePic() != null) {
							empDetails.setProfilePic(tmp_emp.getProfilePic());
						}
						employeeDTO.setEmpDetails(empDetails);
					} else {
						employeeDTO.setStatus(Constants.FAIL);
						employeeDTO.setStatusCode(EnovationConstants.Code200);
						employeeDTO.setResult(EnovationConstants.ResultFalse);
						employeeDTO.setReason(EnovationConstants.PasswordIncorrect);
					}
				}
			} else {
				employeeDTO.setStatus(Constants.NOT_EXIST);
				employeeDTO.setStatusCode(EnovationConstants.Code200);
				employeeDTO.setResult(EnovationConstants.ResultFalse);
				employeeDTO.setReason(EnovationConstants.RecordsDoesNotExist);

			}

		} catch (Exception e) {
			employeeDTO.setStatus(Constants.FAIL);
			employeeDTO.setStatusCode(EnovationConstants.Code500);
			employeeDTO.setResult(EnovationConstants.ResultFalse);
			employeeDTO.setReason("EXCEPTION OCCUR IN LOGINAPP API :" + e.getMessage());
			LOGGER.error("# INSIDE EXCEPTION OCCERED IN LOGINAPP API" + e.getMessage());
		}
		return employeeDTO;
	}

	@Override
	public EmployeeDTO getAllEmplyeeDetailsService() {
		LOGGER.info("# INSIDE IN LOGIN API");
		EmployeeDTO emp_ListDto = new EmployeeDTO();
		List<EmployeeDetails> employeeDet = iEmployeeDao.getAllEmplyeeDetails();
		List<EmployeeDetailsBean> empList = new ArrayList<>();
		try {
			if (employeeDet != null && employeeDet.size() > 0) {
				emp_ListDto.setStatus(EnovationConstants.statusSuccess);
				emp_ListDto.setStatusCode(EnovationConstants.Code200);
				emp_ListDto.setResult(EnovationConstants.ResultTrue);
				for (EmployeeDetails employeeDetails : employeeDet) {
					EmployeeDetailsBean empListBean = new EmployeeDetailsBean();
					BeanUtils.copyProperties(employeeDetails, empListBean);
					if (employeeDetails.getProfilePic() != null) {
						empListBean.setProfilePic(employeeDetails.getProfilePic());
					}
					empList.add(empListBean);
				}
				emp_ListDto.setAllEmpDetails(empList);
			} else {
				emp_ListDto.setStatus(EnovationConstants.statusFail);
				emp_ListDto.setStatusCode(EnovationConstants.Code200);
				emp_ListDto.setResult(EnovationConstants.ResultFalse);
				emp_ListDto.setReason(EnovationConstants.RecordsDoesNotExist);

			}
		} catch (Exception e) {
			emp_ListDto.setStatus(EnovationConstants.statusFail);
			emp_ListDto.setStatusCode(EnovationConstants.Code500);
			emp_ListDto.setResult(EnovationConstants.ResultFalse);
			emp_ListDto.setReason("EXCEPTION OCCUR IN GETALLEMPLOYEE API : " + e.getMessage());
			LOGGER.error("# INSIDE EXCEPTION OCCERED IN GETALLEMPLOYEE " + e.getMessage());
			return emp_ListDto;
		}
		return emp_ListDto;
	}

	@Override
	public EmployeeDTO importEmplyeeDetailsService(Integer orgId, Integer empId, String orgName,
			MultipartFile empExcel) {
		LOGGER.info("#INSIDE IN IMPORTEMPLYEEDETAILSSERVICE API");
		EmployeeDTO empListDto = new EmployeeDTO();
		// boolean
		// checkExcelFormat=iEmployeeDao.checkExcelFormat(orgId,orgName,empExcel);
		// if(checkExcelFormat) {
		try {
			List<EmployeeDetails> empAllDetails = iEmployeeDao.retriveAllEmpDetFromExcel(orgId, orgName, empExcel);
			LOGGER.info("SIZE OF RETRIVEALLEMPDETFROMEXCEL : " + empAllDetails.size());
			if (empAllDetails != null && empAllDetails.size() > 0) {

				boolean VerifyEmpId = iEmployeeDao.checkEmpIdsfromExcel(empAllDetails);
				LOGGER.info("SIZE OF CHECKEMPIDSFROMEXCEL : " + VerifyEmpId);
				if (VerifyEmpId) {
					/* boolean saveEmp=iEmployeeDao.SaveAllEmpExcel(empId,empAllDetails);00 */
					boolean createEmp = iEmployeeDao.createEmployee(empAllDetails);
					LOGGER.info("SIZE OF SAVEALLEMPEXCEL : " + createEmp);
					if (createEmp) {
						empListDto.setStatus(EnovationConstants.statusSuccess);
						empListDto.setResult(EnovationConstants.ResultTrue);
						empListDto.setStatusCode(EnovationConstants.Code200);
						empListDto.setReason(EnovationConstants.ExcelImport);
						return empListDto;
					} else {
						empListDto.setStatus(EnovationConstants.statusFail);
						empListDto.setStatusCode(EnovationConstants.Code200);
						empListDto.setResult(EnovationConstants.ResultFalse);
						empListDto.setReason(EnovationConstants.ImportExcelFailedToSave);
						return empListDto;
					}
				} else {
					empListDto.setStatus(EnovationConstants.statusFail);
					empListDto.setStatusCode(EnovationConstants.Code200);
					empListDto.setResult(EnovationConstants.ResultFalse);
					empListDto.setReason(EnovationConstants.ExcelDuplicateEntry);
					return empListDto;
				}

			} else {
				empListDto.setStatus(EnovationConstants.statusFail);
				empListDto.setStatusCode(EnovationConstants.Code200);
				empListDto.setResult(EnovationConstants.ResultFalse);
				empListDto.setReason(EnovationConstants.ExcelDeptOrgMisMatch);
				return empListDto;
			}
		} catch (Exception e) {
			e.printStackTrace();
			empListDto.setStatus(EnovationConstants.statusFail);
			empListDto.setStatusCode(EnovationConstants.Code500);
			empListDto.setResult(EnovationConstants.ResultFalse);
			empListDto.setReason("EXCEPTION OCCURED IN IMPORTEXCELEMPDETAILS API : " + e.getMessage());
			LOGGER.error("# INSIDE EXCEPTION OCCERED IN IMPORTEXCELEMPDETAILS " + e.getMessage());
			return empListDto;
		}
		/*
		 * }else { empListDto.setStatus(EnovationConstants.statusFail);
		 * empListDto.setStatusCode(EnovationConstants.Code200);
		 * empListDto.setResult(EnovationConstants.ResultFalse);
		 * empListDto.setReason(EnovationConstants.ExcelFormatIncorrect); return
		 * empListDto; }
		 */
	}

	@Override
	public EmployeeDTO getEmpListByTeamTypeIdService(Integer branchId, Integer teamTypeId) {
		LOGGER.info("# INSIDE IN GETEMPLISTBYTEAMTYPEIDSERVICE ");
		EmployeeDTO empRes = null;
		List<EmployeeDetailsBean> empList = new ArrayList<>();
		try {
			if (teamTypeId != 0) {
				List<EmployeeDetails> empDetails = iEmployeeDao.getEmpTeamDetails(branchId, teamTypeId);
				if (empDetails != null && empDetails.size() > 0) {
					empRes = new EmployeeDTO();
					empRes.setStatus(EnovationConstants.statusSuccess);
					empRes.setStatusCode(EnovationConstants.Code200);
					empRes.setResult(EnovationConstants.ResultTrue);
					for (EmployeeDetails employeeDetails : empDetails) {
						EmployeeDetailsBean empListBean = new EmployeeDetailsBean();
						BeanUtils.copyProperties(employeeDetails, empListBean);
						if (employeeDetails.getProfilePic() != null) {
							empListBean.setProfilePic(employeeDetails.getProfilePic());
						}
						empList.add(empListBean);
					}
					empRes.setAllEmpDetails(empList);
					return empRes;
				} else {
					empRes = new EmployeeDTO();
					empRes.setStatus(EnovationConstants.statusFail);
					empRes.setStatusCode(EnovationConstants.Code200);
					empRes.setResult(EnovationConstants.ResultFalse);
					empRes.setReason(EnovationConstants.RecordsDoesNotExist);
					return empRes;
				}
			} else {
				List<EmployeeDetails> empDetailsList = iEmployeeDao.getEmpTeamList();
				empRes = new EmployeeDTO();
				empRes.setStatus(EnovationConstants.statusSuccess);
				empRes.setStatusCode(EnovationConstants.Code200);
				empRes.setResult(EnovationConstants.ResultTrue);
				BeanUtils.copyProperties(empDetailsList, empList);
				empRes.setAllEmpDetails(empList);
				return empRes;

			}

		} catch (Exception e) {
			e.printStackTrace();
			empRes.setStatus(EnovationConstants.statusFail);
			empRes.setStatusCode(EnovationConstants.Code500);
			empRes.setResult(EnovationConstants.ResultFalse);
			empRes.setReason("EXCEPTION OCCURED IN GETEMPLISTBYTEAMTYPEID API : " + e.getMessage());
			LOGGER.error("# INSIDE EXCEPTION OCCERED IN GETEMPLISTBYTEAMTYPEID " + e.getMessage());
			return empRes;
		}
	}

	@Override
	public EmployeeDTO updateProfilePicService(Integer empId, MultipartFile profilePic) {
		LOGGER.info("# INSIDE IN UPDATEPROFILEPICSERVICE ");
		EmployeeDTO empDTO = new EmployeeDTO();
		try {
			String updateProfilePic = iEmployeeDao.updateProfilePic(empId, profilePic);
			if (updateProfilePic != null) {
				empDTO.setEmpProfilePic(updateProfilePic);
				empDTO.setStatus(EnovationConstants.statusSuccess);
				empDTO.setStatusCode(EnovationConstants.Code200);
				empDTO.setResult(EnovationConstants.ResultTrue);
				empDTO.setReason("PROFILEPIC UPDATED");
				return empDTO;
			} else {
				empDTO.setStatus(EnovationConstants.statusFail);
				empDTO.setStatusCode(EnovationConstants.Code200);
				empDTO.setResult(EnovationConstants.ResultFalse);
				empDTO.setReason("PROFILEPIC NOT UPDATED");
				return empDTO;
			}

		} catch (Exception e) {
			empDTO.setStatus(EnovationConstants.statusFail);
			empDTO.setStatusCode(EnovationConstants.Code500);
			empDTO.setResult(EnovationConstants.ResultFalse);
			empDTO.setReason("EXCEPTION OCCURED IN UPDATEPROFILEPIC API : " + e.getMessage());
			LOGGER.error("# INSIDE EXCEPTION OCCERED IN UPDATEPROFILEPIC  " + e.getMessage());
			return empDTO;
		}
	}

	@Override
	public EmployeeDTO updateProfileDetails(EmployeeDetails empdetails) {
		LOGGER.info("# INSIDE IN UPDATEPROFILEDETAILS ");
		EmployeeDTO empDTO = new EmployeeDTO();
		try {
			boolean updateProfile = iEmployeeDao.updateProfileDetails(empdetails);
			if (updateProfile) {
				empDTO.setStatus(EnovationConstants.statusSuccess);
				empDTO.setStatusCode(EnovationConstants.Code200);
				empDTO.setResult(EnovationConstants.ResultTrue);
				empDTO.setReason("PROFILE DETAILS UPDATED");
				return empDTO;
			} else {
				empDTO.setStatus(EnovationConstants.statusFail);
				empDTO.setStatusCode(EnovationConstants.Code200);
				empDTO.setResult(EnovationConstants.ResultFalse);
				empDTO.setReason("PROFILE DETAILS NOT UPDATED");
				return empDTO;
			}

		} catch (Exception e) {
			empDTO.setStatus(EnovationConstants.statusFail);
			empDTO.setStatusCode(EnovationConstants.Code500);
			empDTO.setResult(EnovationConstants.ResultFalse);
			empDTO.setReason("EXCEPTION OCCURED IN UPDATEPROFILE API : " + e.getMessage());
			LOGGER.error("# INSIDE EXCEPTION OCCERED IN UPDATEPROFILE  " + e.getMessage());
			return empDTO;
		}
	}

	@Override
	public EmployeeDTO updateAPNKeyService(UpdateAPNKeyFCMKeyBean aPNKeyDet) {
		LOGGER.info("# INSIDE in updateAPNKeyService ");
		EmployeeDTO empDTO = new EmployeeDTO();
		try {
			boolean updateFCMKey = iEmployeeDao.updateAPNKey(aPNKeyDet);
			if (updateFCMKey != false) {
				empDTO.setStatus(EnovationConstants.statusSuccess);
				empDTO.setStatusCode(EnovationConstants.Code200);
				empDTO.setResult(EnovationConstants.ResultTrue);
				empDTO.setReason("APNKEY UPDATED");
				return empDTO;
			}

		} catch (Exception e) {
			empDTO.setStatus(EnovationConstants.statusFail);
			empDTO.setStatusCode(EnovationConstants.Code500);
			empDTO.setResult(EnovationConstants.ResultFalse);
			empDTO.setReason("EXCEPTION OCCURED IN UPDATEFCMKEYLIST API : " + e.getMessage());
			LOGGER.error("# INSIDE EXCEPTION OCCERED IN UPDATEFCMKEYLIST  " + e.getMessage());
			return empDTO;
		}
		return empDTO;
	}

	@Override
	public EmployeeDTO saveEmployee(EmployeeDetails empDetails) {
		LOGGER.info("# INSIDE IN SAVEEMPLOYEE ");
		EmployeeDTO empDTO = new EmployeeDTO();
		try {
			boolean isContactExist = iEmployeeDao.isContactExist(empDetails);
			boolean isEmailExist = iEmployeeDao.isEmailExist(empDetails);
			boolean isEmployeeIDExist = iEmployeeDao.isEmployeeIDExist(empDetails);
			if (isEmailExist) {
				if (isContactExist) {
					if (isEmployeeIDExist) {
						boolean saveEmp = iEmployeeDao.saveEmployee(empDetails);
						if (saveEmp) {
							empDTO.setStatus(EnovationConstants.statusSuccess);
							empDTO.setStatusCode(EnovationConstants.Code200);
							empDTO.setResult(EnovationConstants.ResultTrue);
							empDTO.setReason("EMPLOYEE RECORD INSERTED");
							return empDTO;
						} else {
							empDTO.setStatus(EnovationConstants.statusFail);
							empDTO.setStatusCode(EnovationConstants.Code200);
							empDTO.setResult(EnovationConstants.ResultFalse);
							empDTO.setReason("FAILED TO INSERT RECORD");
							return empDTO;
						}
					} else {
						empDTO.setStatus(EnovationConstants.statusSuccess);
						empDTO.setStatusCode(EnovationConstants.Code200);
						empDTO.setResult(EnovationConstants.ResultFalse);
						empDTO.setReason("Employee ID exists.");
						return empDTO;
					}
				} else {
					empDTO.setStatus(EnovationConstants.statusSuccess);
					empDTO.setStatusCode(EnovationConstants.Code200);
					empDTO.setResult(EnovationConstants.ResultFalse);
					empDTO.setReason("Contact number exists.");

					return empDTO;
				}
			} else {
				empDTO.setStatus(EnovationConstants.statusSuccess);
				empDTO.setStatusCode(EnovationConstants.Code200);
				empDTO.setResult(EnovationConstants.ResultFalse);
				empDTO.setReason("Email-ID exist.");

				return empDTO;
			}

		} catch (Exception e) {
			empDTO.setStatus(EnovationConstants.statusFail);
			empDTO.setStatusCode(EnovationConstants.Code500);
			empDTO.setResult(EnovationConstants.ResultFalse);
			empDTO.setReason("EXCEPTION OCCURED IN SAVEEMPLOYEE API : " + e.getMessage());
			LOGGER.error("# INSIDE EXCEPTION OCCERED IN SAVEEMPLOYEE  " + e.getMessage());
			return empDTO;
		}
	}

	@Override
	public EmployeeDTO getEmployeeByEmpId(Integer empId) {
		EmployeeDTO empDTO = new EmployeeDTO();
		LOGGER.info("# INSIDE IN GETEMPLOYEEBYEMPID ");
		try {
			if (empId != 0) {
				EmployeeDetails empDet = iEmployeeDao.getEmployeeByEmpId(empId);
				if (empDet != null) {
					ProductOrgConfig pconfig = userdaoinfc.getProductOrgConfigDet(empDet);
					List<MoodIndicator> mi = iEmployeeDao.getMoodRatingforToday(empDet.getEmpId(),
							empDet.getBranch().getBranchId());
					EmployeeDetailsBean empDetailsBean = new EmployeeDetailsBean();
					BeanUtils.copyProperties(empDet, empDetailsBean);
					if (empDet.getProfilePic() != null) {
						empDetailsBean.setProfilePic(empDet.getProfilePic());
					}
					if (pconfig != null) {
						empDetailsBean.setProductOrgConfigDet(pconfig);
					}
					if (!mi.isEmpty()) {
						empDetailsBean.setMoodIndicator(mi);
					}
					empDTO.setStatus(EnovationConstants.statusSuccess);
					empDTO.setStatusCode(EnovationConstants.Code200);
					empDTO.setResult(EnovationConstants.ResultTrue);
					empDTO.setEmpDetails(empDetailsBean);
					empDTO.setReason("RETRIVE DATA FROM GETEMPLOYEEBYEMPID");
					return empDTO;
				} else {
					empDTO.setStatus(EnovationConstants.statusFail);
					empDTO.setStatusCode(EnovationConstants.Code200);
					empDTO.setResult(EnovationConstants.ResultFalse);
					empDTO.setReason(EnovationConstants.RecordsDoesNotExist);
					return empDTO;
				}
			} else {
				empDTO.setStatus(EnovationConstants.statusSuccess);
				empDTO.setStatusCode(EnovationConstants.CODE400);
				empDTO.setReason(EnovationConstants.BAD_REQUEST);
				return empDTO;
			}
		} catch (Exception e) {
			empDTO.setStatus(EnovationConstants.statusFail);
			empDTO.setStatusCode(EnovationConstants.Code500);
			empDTO.setResult(EnovationConstants.ResultFalse);
			empDTO.setReason("EXCEPTION OCCURED IN GETEMPLOYEEBYEMPID API : " + e.getMessage());
			LOGGER.error("# INSIDE EXCEPTION OCCERED IN GETEMPLOYEEBYEMPID  " + e.getMessage());
			return empDTO;
		}
	}

	@Override
	public EmployeeDTO removeEmployee(EmployeeDetails empdetails) {
		LOGGER.info("# INSIDE IN REMOVEEMPLOYEE ");
		EmployeeDTO empDTO = new EmployeeDTO();
		try {
			boolean removeEmployee = iEmployeeDao.removeEmployee(empdetails);
			if (removeEmployee != false) {
				empDTO.setStatus(EnovationConstants.statusSuccess);
				empDTO.setStatusCode(EnovationConstants.Code200);
				empDTO.setResult(EnovationConstants.ResultTrue);
				empDTO.setReason("PROFILE DETAILS REMOVEEMPLOYEE");
				return empDTO;
			} else {
				empDTO.setStatus(EnovationConstants.statusFail);
				empDTO.setStatusCode(EnovationConstants.Code200);
				empDTO.setResult(EnovationConstants.ResultFalse);
				empDTO.setReason("PROFILE DETAILS NOT REMOVEEMPLOYEE");
				return empDTO;
			}

		} catch (Exception e) {
			empDTO.setStatus(EnovationConstants.statusFail);
			empDTO.setStatusCode(EnovationConstants.Code500);
			empDTO.setResult(EnovationConstants.ResultFalse);
			empDTO.setReason("EXCEPTION OCCURED IN REMOVEEMPLOYEE API : " + e.getMessage());
			LOGGER.error("# INSIDE EXCEPTION OCCERED IN REMOVEEMPLOYEE  " + e.getMessage());
			return empDTO;
		}
	}

	@Override
	public EmployeeDTO deactiveEmployee(EmployeeDetails emp) {
		LOGGER.info("#INSIDE IN deactiveEmployee ");
		EmployeeDTO empDTO = new EmployeeDTO();
		try {
			int deactiveEmployee = iEmployeeDao.deactiveEmployee(emp);
			System.out.println("return service value : " + deactiveEmployee);
			if (deactiveEmployee == 1) {
				empDTO.setStatus(EnovationConstants.statusSuccess);
				empDTO.setStatusCode(EnovationConstants.Code200);
				empDTO.setResult(EnovationConstants.ResultTrue);
				return empDTO;
			} else if (deactiveEmployee == 2) {
				System.out.println("equels 2");
				empDTO.setStatus(EnovationConstants.statusFail);
				empDTO.setStatusCode(EnovationConstants.Code100);
				empDTO.setResult(EnovationConstants.ResultFalse);
				empDTO.setReason(EnovationConstants.ROLE_POPUP);
				return empDTO;
			} else if (deactiveEmployee == 3) {
				empDTO.setStatus(EnovationConstants.statusFail);
				empDTO.setStatusCode(EnovationConstants.Code100);
				empDTO.setResult(EnovationConstants.ResultFalse);
				System.out.println("equals 3");
				empDTO.setReason(EnovationConstants.EMPLOYEE_PENDING);
				return empDTO;
			}

		} catch (Exception e) {
			empDTO.setStatus(EnovationConstants.statusFail);
			empDTO.setStatusCode(EnovationConstants.Code500);
			empDTO.setResult(EnovationConstants.ResultFalse);
			empDTO.setReason("EXCEPTION OCCURED IN deactiveEmployee API : " + e.getMessage());
			LOGGER.error("#INSIDE EXCEPTION OCCERED IN deactiveEmployee  " + e.getMessage());
			return empDTO;
		}
		return empDTO;
	}

	@Override
	public EmployeeDTO updateEmployeeDetails(EmployeeDetails empdetails) {
		LOGGER.info("# INSIDE IN UPDATEEMPLOYEEDETAILS ");
		EmployeeDTO empDTO = new EmployeeDTO();
		try {
			int updateEmployeeDetails = iEmployeeDao.updateEmployeeDetails(empdetails);
			if (updateEmployeeDetails == EnovationConstants.ONE) {
				empDTO.setStatus(EnovationConstants.statusSuccess);
				empDTO.setStatusCode(EnovationConstants.Code200);
				empDTO.setResult(EnovationConstants.ResultTrue);
				empDTO.setReason("EMPLOYEE DETAILS UPDATED");
				return empDTO;
			} else if (updateEmployeeDetails == EnovationConstants.TWO) {
				empDTO.setStatus(EnovationConstants.statusSuccess);
				empDTO.setStatusCode(EnovationConstants.Code200);
				empDTO.setResult(EnovationConstants.ResultFalse);
				empDTO.setReason("YOUR ENTERED EMAIL-ID ALREADY EXISTS.");
				return empDTO;
			}

		} catch (Exception e) {
			empDTO.setStatus(EnovationConstants.statusFail);
			empDTO.setStatusCode(EnovationConstants.Code500);
			empDTO.setResult(EnovationConstants.ResultFalse);
			empDTO.setReason("EXCEPTION OCCURED IN EMPLOYEEDETAILS API : " + e.getMessage());
			LOGGER.error("# INSIDE EXCEPTION OCCERED IN EMPLOYEEDETAILS  " + e.getMessage());
			return empDTO;
		}
		return empDTO;
	}

	@Override
	public EmployeeDTO getAllEmplyeeListByBranch(Integer orgId, Integer branchId, Integer deptId) {
		LOGGER.info("# INSIDE IN getAllEmplyeeListByBranch API");
		EmployeeDTO empDto = new EmployeeDTO();
		if (branchId != 0) {
			List<EmployeeDetails> employeeDet = iEmployeeDao.getAllEmplyeeListByBranch(orgId, branchId, deptId);
			List<EmployeeDetailsBean> empList = new ArrayList<>();

			try {
				if (empList != null && employeeDet.size() > 0) {
					empDto.setStatus(EnovationConstants.statusSuccess);
					empDto.setStatusCode(EnovationConstants.Code200);
					empDto.setResult(EnovationConstants.ResultTrue);
					for (EmployeeDetails employeeDetails : employeeDet) {
						EmployeeDetailsBean empListBean = new EmployeeDetailsBean();
						BeanUtils.copyProperties(employeeDetails, empListBean);
						if (employeeDetails.getProfilePic() != null) {
							empListBean.setProfilePic(employeeDetails.getProfilePic());
						}
						empList.add(empListBean);
					}
					empDto.setAllEmpDetails(empList);
				} else {
					empDto.setStatus(EnovationConstants.statusFail);
					empDto.setStatusCode(EnovationConstants.Code200);
					empDto.setResult(EnovationConstants.ResultFalse);
					empDto.setReason(EnovationConstants.RecordsDoesNotExist);

				}
			} catch (Exception e) {
				empDto.setStatus(EnovationConstants.statusFail);
				empDto.setStatusCode(EnovationConstants.Code500);
				empDto.setResult(EnovationConstants.ResultFalse);
				empDto.setReason("EXCEPTION OCCUR IN getAllEmplyeeListByBranch API : " + e.getMessage());
				LOGGER.error("# INSIDE EXCEPTION OCCERED IN getAllEmplyeeListByBranch " + e.getMessage());
				return empDto;
			}
		} else {
			empDto.setStatus(EnovationConstants.statusSuccess);
			empDto.setStatusCode(EnovationConstants.CODE400);
			empDto.setReason(EnovationConstants.BAD_REQUEST);
		}
		return empDto;

	}

	@Override
	public EmployeeDTO getAllEmpListForExecutor(Integer branchId) {
		LOGGER.info("# INSIDE IN GETALLEMPLISTFOREXECUTOR API");
		EmployeeDTO empDto = new EmployeeDTO();
		if (branchId != 0) {
			List<EmployeeDetails> employeeDet = iEmployeeDao.getAllEmpListForExecutor(branchId);
			List<EmployeeDetailsBean> empList = new ArrayList<>();
			try {
				if (employeeDet != null && employeeDet.size() > 0) {
					empDto.setStatus(EnovationConstants.statusSuccess);
					empDto.setStatusCode(EnovationConstants.Code200);
					empDto.setResult(EnovationConstants.ResultTrue);
					for (EmployeeDetails employeeDetails : employeeDet) {
						EmployeeDetailsBean empListBean = new EmployeeDetailsBean();
						BeanUtils.copyProperties(employeeDetails, empListBean);
						if (employeeDetails.getProfilePic() != null) {
							empListBean.setProfilePic(employeeDetails.getProfilePic());
						}
						List<TeamtypeMaster> empTeamType = iEmployeeDao.checkEmpInWhichTeam(employeeDetails.getEmpId());
						if (empTeamType != null && empTeamType.size() > 0) {
							empListBean.setTeamDetails(empTeamType);
							empListBean.setRole(empTeamType.get(0).getType());
						}
						empList.add(empListBean);
					}
					empDto.setAllEmpDetails(empList);
				} else {
					empDto.setStatus(EnovationConstants.statusFail);
					empDto.setStatusCode(EnovationConstants.Code200);
					empDto.setReason(EnovationConstants.RecordsDoesNotExist);

				}
			} catch (Exception e) {
				empDto.setStatus(EnovationConstants.statusFail);
				empDto.setStatusCode(EnovationConstants.Code500);
				empDto.setReason("EXCEPTION OCCUR IN GETALLEMPLISTFOREXECUTOR API : " + e.getMessage());
				LOGGER.error("# INSIDE EXCEPTION OCCERED IN GETALLEMPLISTFOREXECUTOR " + e.getMessage());
				return empDto;
			}
		} else {
			empDto.setStatus(EnovationConstants.statusSuccess);
			empDto.setStatusCode(EnovationConstants.CODE400);
			empDto.setReason(EnovationConstants.BAD_REQUEST);
		}
		return empDto;
	}

	@Override
	public EmployeeDTO createEmployee(EmployeeDetails empDetails) {
		LOGGER.info("# INSIDE IN CREATEEMPLOYEE ");
		System.out.println("registration on service :" + empDetails.getRegistrationByPass());
		EmployeeDTO empDTO = new EmployeeDTO();
		boolean isEmailExist = true;
		boolean isContactExist = true;
		try {
			// boolean isContactExist=iEmployeeDao.isContactExist(empDetails);
			if (empDetails.getEmailId() != null && !empDetails.getEmailId().isEmpty()) {
				isEmailExist = iEmployeeDao.isEmailExist(empDetails);
			}
			if (empDetails.getContactNo() != null && !empDetails.getContactNo().isEmpty()) {
				isContactExist = iEmployeeDao.isContactExist(empDetails);
			}

			if (isEmailExist && isContactExist) {
				/*
				 * boolean isEmployeeIDExist=iEmployeeDao.isEmployeeIDExist(empDetails);
				 * if(isEmployeeIDExist) {
				 */ List<EmployeeDetails> list = new ArrayList<>();
				list.add(empDetails);
				boolean createEmp = iEmployeeDao.createEmployee(list);
				if (createEmp) {
					empDTO.setStatus(EnovationConstants.statusSuccess);
					empDTO.setStatusCode(EnovationConstants.Code200);
					empDTO.setResult(EnovationConstants.ResultTrue);
					empDTO.setReason(EnovationConstants.DATA_SAVE);
					return empDTO;
				}
			} else {
				empDTO.setStatus(EnovationConstants.statusSuccess);
				empDTO.setStatusCode(EnovationConstants.Code200);
				empDTO.setResult(EnovationConstants.ResultFalse);
				if (!isEmailExist) {
					empDTO.setReason(EnovationConstants.EMAIL_EXITS);
				} else if (!isContactExist) {
					empDTO.setReason(EnovationConstants.MOBILE_EXITS);
				}
				return empDTO;
			}
		} catch (Exception e) {
			e.printStackTrace();
			LOGGER.error("#INSIDE EXCEPTION OCCURED IN CREATE_EMPLOYEE" + e.getMessage());
		}
		return empDTO;
	}

	@Override
	public EmployeeDTO employeeVerify(EmployeeDetails emp) {
		LOGGER.info("# INSIDE IN EMPLOYEEVERIFY API");
		EmployeeDTO empDto = new EmployeeDTO();
		try {
			int isTokenVerified = iEmployeeDao.isTokenVerified(emp.getToken());
			if (isTokenVerified == 2) {//
				EmployeeDetails employeeDet = iEmployeeDao.employeeVerify(emp.getToken());
				if (employeeDet != null) {
					empDto.setStatus(EnovationConstants.statusSuccess);
					empDto.setStatusCode(EnovationConstants.Code200);
					empDto.setResult(EnovationConstants.ResultTrue);
					empDto.setEmail(employeeDet.getEmailId());
					empDto.setToken(employeeDet.getToken());
					return empDto;
				}
			} else if (isTokenVerified == 1) {
				empDto.setStatus(EnovationConstants.statusFail);
				empDto.setStatusCode(EnovationConstants.Code100);
				empDto.setResult(EnovationConstants.ResultFalse);
				empDto.setReason(EnovationConstants.INVALID_VERIFICATION_TOKEN);
				return empDto;
			}
		} catch (Exception e) {
			empDto.setStatus(EnovationConstants.statusFail);
			empDto.setStatusCode(EnovationConstants.Code500);
			empDto.setResult(EnovationConstants.ResultFalse);
			empDto.setReason(EnovationConstants.ACCOUNT_ALREADY_VERIFIED);
			LOGGER.error("# INSIDE EXCEPTION OCCERED IN EMPLOYEEVERIFY " + e.getMessage());
			return empDto;
		}
		return empDto;

	}

	@Override
	public EmployeeDTO employeeVerifyforForgotPassword(EmployeeDetails emp) {
		LOGGER.info("# INSIDE IN EMPLOYEEVERIFYFORFORGOTPASSWORD API");
		EmployeeDTO empDto = new EmployeeDTO();
		try {
			int isTokenVerified = iEmployeeDao.isTokenVerified(emp.getToken());
			if (isTokenVerified == 2) {//
				EmployeeDetails employeeDet = iEmployeeDao.employeeVerify(emp.getToken());
				if (employeeDet != null) {
					empDto.setStatus(EnovationConstants.statusSuccess);
					empDto.setStatusCode(EnovationConstants.Code200);
					empDto.setResult(EnovationConstants.ResultTrue);
					empDto.setEmail(employeeDet.getEmailId());
					empDto.setToken(employeeDet.getToken());
					empDto.setPasswordPolicy(
							iMasterDao.getPasswordPolicyByOrgId(employeeDet.getOrganization().getOrgId()));
					return empDto;
				}
			} else if (isTokenVerified == 3) {
				empDto.setStatus(EnovationConstants.statusFail);
				empDto.setStatusCode(EnovationConstants.Code100);
				empDto.setResult(EnovationConstants.ResultFalse);
				empDto.setReason(EnovationConstants.ACCOUNT_NOT_VERIFIED);
				return empDto;
			} else if (isTokenVerified == 1) {
				empDto.setStatus(EnovationConstants.statusFail);
				empDto.setStatusCode(EnovationConstants.Code100);
				empDto.setResult(EnovationConstants.ResultFalse);
				empDto.setReason(EnovationConstants.INVALID_VERIFICATION_TOKEN);
				return empDto;
			}
		} catch (Exception e) {
			empDto.setStatus(EnovationConstants.statusFail);
			empDto.setStatusCode(EnovationConstants.Code500);
			empDto.setResult(EnovationConstants.ResultFalse);
			empDto.setReason(EnovationConstants.ACCOUNT_ALREADY_VERIFIED);
			LOGGER.error("# INSIDE EXCEPTION OCCERED IN EMPLOYEEVERIFYFORFORGOTPASSWORD " + e.getMessage());
			return empDto;
		}
		return empDto;
	}

	@Override
	public EmployeeDTO getEmpListRolewise(Integer branchId, Integer roleId) {
		LOGGER.info("# INSIDE IN getEmpListRolewise API");
		EmployeeDTO empDto = new EmployeeDTO();
		if (roleId != 0 && branchId != 0) {
			List<EmployeeDetails> employeeDet = iEmployeeDao.getEmpListRolewise(branchId, roleId);
			List<EmployeeDetailsBean> empList = new ArrayList<>();
			try {
				if (empList != null && employeeDet.size() > 0) {
					empDto.setStatus(EnovationConstants.statusSuccess);
					empDto.setStatusCode(EnovationConstants.Code200);
					empDto.setResult(EnovationConstants.ResultTrue);
					for (EmployeeDetails employeeDetails : employeeDet) {
						EmployeeDetailsBean empListBean = new EmployeeDetailsBean();

						BeanUtils.copyProperties(employeeDetails, empListBean);
						if (employeeDetails.getBranchId() > 0) {
							BranchMaster branch = new BranchMaster(employeeDetails.getBranchId(),
									employeeDetails.getBranchName());
							empListBean.setBranch(branch);
						}
						if (employeeDetails.getDeptId() > 0) {
							DepartmentMaster dept = new DepartmentMaster(employeeDetails.getDeptId(),
									employeeDetails.getDeptName());
							empListBean.setDept(dept);
						} /*
							 * if(employeeDetails.getProfilePic()!=null) {
							 * empListBean.setProfilePic(employeeDetails.getProfilePic()); }
							 */
						empList.add(empListBean);
					}
					empDto.setAllEmpDetails(empList);
				} else {
					empDto.setStatus(EnovationConstants.statusFail);
					empDto.setStatusCode(EnovationConstants.Code200);
					empDto.setResult(EnovationConstants.ResultFalse);
					empDto.setReason(EnovationConstants.RecordsDoesNotExist);

				}
			} catch (Exception e) {
				empDto.setStatus(EnovationConstants.statusFail);
				empDto.setStatusCode(EnovationConstants.Code500);
				empDto.setResult(EnovationConstants.ResultFalse);
				empDto.setReason("EXCEPTION OCCUR IN getEmpListRolewise API : " + e.getMessage());
				LOGGER.error("# INSIDE EXCEPTION OCCERED IN getEmpListRolewise " + e.getMessage());
				return empDto;
			}
		} else {
			empDto.setStatus(EnovationConstants.statusSuccess);
			empDto.setStatusCode(EnovationConstants.CODE400);
			empDto.setReason(EnovationConstants.BAD_REQUEST);
		}
		return empDto;

	}

	@Override
	public EmployeeDTO getEmpListRolewiseOrgLevel(Integer orgId, Integer roleId) {
		LOGGER.info("# INSIDE IN getEmpListRolewiseOrgLevel API");
		EmployeeDTO empDto = new EmployeeDTO();
		if (roleId != 0 && orgId != 0) {
			List<EmployeeDetails> employeeDet = iEmployeeDao.getEmpListRolewiseOrgLevel(orgId, roleId);
			List<EmployeeDetailsBean> empList = new ArrayList<>();
			try {
				if (empList != null && employeeDet.size() > 0) {
					empDto.setStatus(EnovationConstants.statusSuccess);
					empDto.setStatusCode(EnovationConstants.Code200);
					empDto.setResult(EnovationConstants.ResultTrue);
					for (EmployeeDetails employeeDetails : employeeDet) {
						EmployeeDetailsBean empListBean = new EmployeeDetailsBean();

						BeanUtils.copyProperties(employeeDetails, empListBean);
						if (employeeDetails.getBranchId() > 0) {
							BranchMaster branch = new BranchMaster(employeeDetails.getBranchId(),
									employeeDetails.getBranchName());
							empListBean.setBranch(branch);
						}
						if (employeeDetails.getDeptId() > 0) {
							DepartmentMaster dept = new DepartmentMaster(employeeDetails.getDeptId(),
									employeeDetails.getDeptName());
							empListBean.setDept(dept);
						} /*
							 * if(employeeDetails.getProfilePic()!=null) {
							 * empListBean.setProfilePic(employeeDetails.getProfilePic()); }
							 */
						empList.add(empListBean);
					}
					empDto.setAllEmpDetails(empList);
				} else {
					empDto.setStatus(EnovationConstants.statusFail);
					empDto.setStatusCode(EnovationConstants.Code200);
					empDto.setResult(EnovationConstants.ResultFalse);
					empDto.setReason(EnovationConstants.RecordsDoesNotExist);

				}
			} catch (Exception e) {
				empDto.setStatus(EnovationConstants.statusFail);
				empDto.setStatusCode(EnovationConstants.Code500);
				empDto.setResult(EnovationConstants.ResultFalse);
				empDto.setReason("EXCEPTION OCCUR IN getEmpListRolewise API : " + e.getMessage());
				LOGGER.error("# INSIDE EXCEPTION OCCERED IN getEmpListRolewise " + e.getMessage());
				return empDto;
			}
		} else {
			empDto.setStatus(EnovationConstants.statusSuccess);
			empDto.setStatusCode(EnovationConstants.CODE400);
			empDto.setReason(EnovationConstants.BAD_REQUEST);
		}
		return empDto;
	}

	@Override
	public EmployeeDTO saveOrUpdateEmpRoles(List<SaveEmpRolesBean> request) {
		LOGGER.info("# INSIDE IN getEmpListRolewise API");
		EmployeeDTO empDto = new EmployeeDTO();
		int save = iEmployeeDao.saveOrUpdateEmpRoles(request);
		try {
			if (save == 1) {
				empDto.setStatus(EnovationConstants.statusSuccess);
				empDto.setStatusCode(EnovationConstants.Code200);
				empDto.setResult(EnovationConstants.ResultTrue);
			} else if (save == 2) {
				empDto.setStatus(EnovationConstants.statusFail);
				empDto.setStatusCode(EnovationConstants.Code100);
				empDto.setResult(EnovationConstants.ResultFalse);
				empDto.setReason(EnovationConstants.ALREADY_ALIGNED);

			}
		} catch (Exception e) {
			empDto.setStatus(EnovationConstants.statusFail);
			empDto.setStatusCode(EnovationConstants.Code500);
			empDto.setResult(EnovationConstants.ResultFalse);
			empDto.setReason("EXCEPTION OCCUR IN SAVEORUPDATEEMPROLES API : " + e.getMessage());
			LOGGER.error(
					"#INSIDE EXCEPTION OCCERED IN SAVEORUPDATEEMPROLES(EVALUATOR AND IMPLEMENTOR) " + e.getMessage());
			return empDto;
		}
		return empDto;
	}

	@Override
	public EmployeeDTO importBulkEmplyee(EmployeeDetails empdetails) {
		LOGGER.info("#INSIDE IN IMPORTEMPLYEEDETAILSSERVICE API");
		EmployeeDTO empListDto = null;
		/*
		 * int execlFormatOrBlank=iEmployeeDao.checkExeclFormatOrBlank(empdetails.
		 * getOrgAlies(),empdetails.getExcel());; if(execlFormatOrBlank == 2) {
		 * empListDto =new EmployeeDTO();
		 * empListDto.setStatus(EnovationConstants.statusFail);
		 * empListDto.setStatusCode(EnovationConstants.Code100);
		 * empListDto.setResult(EnovationConstants.ResultFalse);
		 * empListDto.setReason(EnovationConstants.INVALID_FILE_FORMAT); return
		 * empListDto; }else if(execlFormatOrBlank == 4) { empListDto =new
		 * EmployeeDTO(); empListDto.setStatus(EnovationConstants.statusFail);
		 * empListDto.setStatusCode(EnovationConstants.Code100);
		 * empListDto.setResult(EnovationConstants.ResultFalse);
		 * empListDto.setReason(EnovationConstants.BLANK_EXCEL); return empListDto;
		 * }else if(execlFormatOrBlank == 3) {
		 */
		HashMap<String, List<ImportEmployeeErrorList>> checkExcelMap = iEmployeeDao
				.checkExcelFormat(empdetails.getOrgAlies(), empdetails.getExcel());
		;
		// System.out.println("check excel map :"+checkExcelMap);
		List<ImportEmployeeErrorList> errorList = checkExcelMap.get("errorList");
		List<ImportEmployeeErrorList> checkList = checkExcelMap.get("checkList");
		/*
		 * checkList.forEach(x -> {
		 * System.out.println("checklist Email Id :"+x.getEmailId()); });
		 */
		List<ImportEmployeeErrorList> allErrorList = new ArrayList<>();
		allErrorList.addAll(errorList);

		if (checkList != null && checkList.size() > 0) {
			// Set<ImportEmployeeErrorList>
			// DuplicateErrorList=checkDuplicateDataInExcel(checkList);
			// allErrorList.addAll(DuplicateErrorList);
			// if(status){
			empListDto = checkDept(checkList, empdetails, allErrorList);
			return empListDto;

		} else {
			empListDto = new EmployeeDTO();
			empListDto.setErrorList(errorList);
			empListDto.setStatus(EnovationConstants.statusSuccess);
			empListDto.setStatusCode(EnovationConstants.Code100);
			empListDto.setReason(EnovationConstants.ExcelFormatIncorrect);
			return empListDto;
		}
		// }
		// return empListDto;
	}

	@SuppressWarnings("unused")
	private Set<ImportEmployeeErrorList> checkDuplicateDataInExcel(List<ImportEmployeeErrorList> checkList) {
		int count = 1;
		LOGGER.info("#INSIDE IN CHECKDUPLICATEDATAINEXCEL");
		boolean flag = false;
		Set<ImportEmployeeErrorList> duplicates = new HashSet<>();
		Set<String> emailIds = new HashSet<String>();
		Set<String> MobileNumbers = new HashSet<String>();
		for (ImportEmployeeErrorList emaillist : checkList) {
			emailIds.add(emaillist.getEmailId());
			MobileNumbers.add(emaillist.getContactNumber());
		}
		// Set<ImportEmployeeErrorList> duplicated
		// =checkList.stream().distinct(2).toSet();
		// Set<ImportEmployeeErrorList> duplicated = checkList.stream().filter(n ->
		// checkList.stream().filter(x -> x.getEmailId() == n.getEmailId()).count() >=
		// 1).collect(Collectors.toSet());

		/*
		 * Set<ImportEmployeeErrorList> tempSet = new HashSet();
		 * Set<ImportEmployeeErrorList> duplicates = new HashSet();
		 * 
		 * checkList.forEach( item -> { // if (!tempSet.add(item))duplicates.add(item);
		 * if(Collections.frequency(checkList, item) > 1) { duplicates.add(item); } });
		 */
		for (int i = 1; i < checkList.size(); i++) {
			if (checkList.get(i - 1).getEmailId().equals(checkList.get(i).getEmailId())
					|| checkList.get(i - 1).getContactNumber().equals(checkList.get(i).getContactNumber())) {
				duplicates.add(checkList.get(i));
			}

		}
		System.out.println("uedcuf" + duplicates);
		duplicates.forEach(x -> {
			System.out.println("duplicated comment: " + x.getComment());
			x.setComment(EnovationConstants.ExcelDuplicateEntry);
			System.out.println("duplicated : " + x.getEmailId());
			System.out.println("duplicated comment1: " + x.getComment());
		});

		System.out.println("set size :" + duplicates.size());
		if (emailIds.size() == checkList.size()) {
			System.out.println("set emp list size : Success");

			return duplicates;
		}
		return duplicates;
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public EmployeeDTO checkDept(List<ImportEmployeeErrorList> list, EmployeeDetails empdetails,
			List<ImportEmployeeErrorList> allErrorList) {
		EmployeeDTO dto = new EmployeeDTO();
		Set<String> checkDeptNameSet = new HashSet<String>();
		Collection<ImportEmployeeErrorList> col1 = new ArrayList<ImportEmployeeErrorList>();
		List<ImportEmployeeErrorList> errorlist = new ArrayList<ImportEmployeeErrorList>();
		for (ImportEmployeeErrorList importEmp : list) {
			if (importEmp.getDepartment() != null) {
				checkDeptNameSet.add(String.valueOf((importEmp.getDepartment().toUpperCase())));
			}
			if (importEmp.getEmailId().length() > 2) {
				boolean flag = iEmployeeDao.isEmailExist(importEmp.getEmailId());
				if (!flag) {
					ImportEmployeeErrorList errorDetails = new ImportEmployeeErrorList(importEmp.getEmployeeId(),
							importEmp.getFirstName(), importEmp.getLastName(), importEmp.getEmailId(),
							importEmp.getContactNumber(), importEmp.getDepartment(), EnovationConstants.EMAIL_EXISTS,
							importEmp.getDesignation());
					errorlist.add(errorDetails);
				}
			}
			if (importEmp.getEmployeeId().length() > 2) {
				boolean flag = iEmployeeDao.isEmployeeIDExist(new EmployeeDetails(importEmp.getEmployeeId()));
				if (!flag) {
					ImportEmployeeErrorList errorDetails = new ImportEmployeeErrorList(importEmp.getEmployeeId(),
							importEmp.getFirstName(), importEmp.getLastName(), importEmp.getEmailId(),
							importEmp.getContactNumber(), importEmp.getDepartment(),
							EnovationConstants.EMPLOYEE_ID_EXISTS, importEmp.getDesignation());
					errorlist.add(errorDetails);
				}
			}
			if (importEmp.getContactNumber().length() > 2) {
				boolean flag = iEmployeeDao.isMobileNumberExist(importEmp.getContactNumber());
				if (!flag) {
					ImportEmployeeErrorList errorDetails = new ImportEmployeeErrorList(importEmp.getEmployeeId(),
							importEmp.getFirstName(), importEmp.getLastName(), importEmp.getEmailId(),
							importEmp.getContactNumber(), importEmp.getDepartment(),
							EnovationConstants.MOBILE_NUMBER_EXISTS, importEmp.getDesignation());
					errorlist.add(errorDetails);
				}
			}
		}
		/*
		 * for(String s:checkDeptNameSet) { System.out.println("deptList : "+s); }
		 */
		List<DepartmentMaster> listofDept = iMasterDao.getdepartmentlistbybranchid(empdetails.getBranchId());
		List<DepartmentMaster> listofDeptID = iMasterDao
				.getdepartmentlistbybranchidandDeptname(empdetails.getBranchId(), checkDeptNameSet);
		Map<String, Integer> mapOfDep = new HashMap<String, Integer>();
		for (DepartmentMaster departmentMaster : listofDeptID) {
			mapOfDep.put(departmentMaster.getDeptName(), departmentMaster.getDeptId());
		}

		Set<String> actualDept = new HashSet<String>();
		for (DepartmentMaster departmentMaster : listofDept) {
			actualDept.add(departmentMaster.getDeptName().toUpperCase());
		}
		checkDeptNameSet.removeAll(actualDept);
		/*
		 * for(String a:checkDeptNameSet) { List<ImportEmployeeErrorList> errorlist1 =
		 * list.stream() .filter(p -> p.getDepartment().toUpperCase().equals(a))
		 * .collect(Collectors.toList()); errorlist.addAll(errorlist1);
		 * 
		 * }
		 */

		StringBuffer str = new StringBuffer();
		if (checkDeptNameSet.size() > 0) {
			/*
			 * Using Stream
			 */
			List<ImportEmployeeErrorList> errorDEPTlist = list.stream()
					.filter(p -> (actualDept.stream()
							.filter(d -> d.equals(String.valueOf(p.getDepartment().toUpperCase()))).count()) < 1)
					.collect(Collectors.toList());
			errorDEPTlist.forEach(u -> u.setComment(EnovationConstants.DEPARTMENT_INCORRECT));
			errorlist.addAll(errorDEPTlist);

			System.out.println("LIST SIZE BEFORE BLANK DEPT REMOVE:" + errorDEPTlist.size());
			/*
			 * for(ImportEmployeeErrorList blankdeptList : errorDEPTlist) {
			 * System.out.println("inside remove function:"+blankdeptList.getDepartment().
			 * trim()); if(blankdeptList.getDepartment().equals(" ")) {
			 * System.out.println("inside remove function : 1");
			 * errorDEPTlist.remove(blankdeptList.getDepartment()); }
			 * 
			 * }
			 */
			errorlist.removeIf(hps -> hps.getDepartment().trim().equals(""));
			// errorDEPTlist.forEach(h -> errorDEPTlist.removeIf(hps ->
			// hps.getDepartment().equals("")));
			System.out.println("LIST SIZE AFTER BLANK DEPT REMOVE  :" + errorlist.size());
			// dto.setErrorList(errorlist);
			// dto.setDeptExist(checkDeptNameSet);
		}

		/*
		 * SAVE BULK EMPLOYEE
		 */
		List<EmployeeDetails> empListDetails = new ArrayList<EmployeeDetails>();
		System.out.println("SAVE BULK EMPLOYEE ERROR LIST :" + errorlist.size());
		if (errorlist.size() == 0) {
			EmployeeDetails emp = null;
			for (ImportEmployeeErrorList empList : list) {
				Integer deptId = mapOfDep.get(empList.getDepartment());
				emp = new EmployeeDetails();
				if (deptId != null) {
					emp.setDept(new DepartmentMaster(deptId));
				}
				emp.setFirstName(empList.getFirstName());
				emp.setLastName(empList.getLastName());
				emp.setContactNo(empList.getContactNumber());
				emp.setCmpyEmpId(empList.getEmployeeId());
				emp.setEmailId(empList.getEmailId());
				emp.setDesignation(empList.getDesignation());
				emp.setBranch(new BranchMaster(empdetails.getBranchId()));
				emp.setOrganization(new OrganizationMaster(empdetails.getOrgId()));
				emp.setIsSetupCompleted(empdetails.getIsSetupCompleted());
				emp.setCreatedBy(empdetails.getCreatedBy());
				if (empdetails.getRegistrationByPass() != null)
					emp.setRegistrationByPass(empdetails.getRegistrationByPass());
				if (empdetails.getRegistrationByPass().equalsIgnoreCase(EnovationConstants.Y)) {
					emp.setIsEmailVerified(EnovationConstants.ONE);
					emp.setPassword(password.encode(EnovationConstants.DEFAULT_PASSWORD));
				}
				empListDetails.add(emp);
			}
			boolean createEmp = iEmployeeDao.createBulkEmployee(empListDetails);
			if (createEmp) {
				dto.setStatus(EnovationConstants.statusSuccess);
				dto.setStatusCode(EnovationConstants.Code100);
				dto.setResult(EnovationConstants.ResultTrue);
				dto.setReason(str.toString());
			} else {
				dto.setStatus(EnovationConstants.statusSuccess);
				dto.setStatusCode(EnovationConstants.Code500);
				dto.setReason(str.toString());
			}
		} else {
			dto.setStatus(EnovationConstants.statusSuccess);
			dto.setStatusCode(EnovationConstants.Code100);
			/*
			 * Map<String,List<ImportEmployeeErrorList>> errorlist1 = errorlist.stream()
			 * .collect(Collectors.groupingBy(ImportEmployeeErrorList::getEmployeeId));
			 */
			col1 = errorlist.stream()
					.collect(Collectors.toMap(
							c -> (c.getEmployeeId().length() > 2) ? c.getEmployeeId()
									: ((c.getEmailId().length() > 2) ? c.getEmailId()
											: ((c.getContactNumber().length() > 2) ? c.getContactNumber() : null)),
							Function.identity(), (fg1, fg2) -> {
								// This will modify the original object(s). If you don't want that, then you'll
								// have to clone the object and set favorites.
								fg1.setComment(fg1.getComment() + ", " + fg2.getComment());
								return fg1;
							}))
					.values();
			if (col1 instanceof List)
				errorlist = (List) col1;
			else
				errorlist = new ArrayList(col1);
			allErrorList.addAll(errorlist);
			// dto.setErrorlist1(errorlist1);
			dto.setErrorList(allErrorList);
			dto.setReason("Employee data already exist");
			// dto.setReason(str.toString());
		}
		return dto;
	}

	@Override
	public EmployeeDTO resetPassword(EmployeeDetails request) {
		LOGGER.info("#INSIDE IN RESETPASSWORD API");
		EmployeeDTO empDto = new EmployeeDTO();
		int reset = iEmployeeDao.resetPassword(request);
		System.out.println("Flag:" + reset);
		try {
			if (reset == 1) {
				empDto.setStatus(EnovationConstants.statusFail);
				empDto.setStatusCode(EnovationConstants.Code100);
				empDto.setResult(EnovationConstants.ResultFalse);
				empDto.setReason(EnovationConstants.INVALID_PASS);

			} else if (reset == 2) {
				empDto.setStatus(EnovationConstants.statusSuccess);
				empDto.setStatusCode(EnovationConstants.Code100);
				empDto.setResult(EnovationConstants.resultFalse);
				empDto.setReason("Cannot use same password again please change your password");
			} else if (reset == 3) {
				empDto.setStatus(EnovationConstants.statusSuccess);
				empDto.setStatusCode(EnovationConstants.Code200);
				empDto.setResult(EnovationConstants.resultTrue);
				empDto.setReason("Password has been changed successfully ");
			}

		} catch (Exception e) {
			empDto.setStatus(EnovationConstants.statusFail);
			empDto.setStatusCode(EnovationConstants.Code500);
			empDto.setResult(EnovationConstants.ResultFalse);
			empDto.setReason("EXCEPTION OCCUR IN RESETPASSWORD API : " + e.getMessage());
			LOGGER.error("#INSIDE EXCEPTION OCCERED IN RESETPASSWORD " + e.getMessage());
			return empDto;
		}
		return empDto;
	}

	@Override
	public EmployeeDTO removeProfilePic(Integer empId) {
		LOGGER.info("#INSIDE IN REMOVEPROFILEPIC API");
		EmployeeDTO empDto = new EmployeeDTO();
		if (empId != 0) {
			boolean reset = iEmployeeDao.removeProfilePic(empId);
			try {
				if (reset) {
					empDto.setStatus(EnovationConstants.statusSuccess);
					empDto.setStatusCode(EnovationConstants.Code200);
					empDto.setResult(EnovationConstants.ResultTrue);
				} else {
					empDto.setStatus(EnovationConstants.statusFail);
					empDto.setStatusCode(EnovationConstants.Code200);
				}
			} catch (Exception e) {
				empDto.setStatus(EnovationConstants.statusFail);
				empDto.setStatusCode(EnovationConstants.Code500);
				empDto.setReason("EXCEPTION OCCUR IN REMOVEPROFILEPIC API : " + e.getMessage());
				LOGGER.error("#INSIDE EXCEPTION OCCERED IN REMOVEPROFILEPIC " + e.getMessage());
				return empDto;
			}
		} else {
			empDto.setStatus(EnovationConstants.statusSuccess);
			empDto.setStatusCode(EnovationConstants.CODE400);
			empDto.setReason(EnovationConstants.BAD_REQUEST);
		}
		return empDto;
	}

	@Override
	public EmployeeDTO sendRoleUpdateEmails(Integer branchId) {
		LOGGER.info("#INSIDE IN SENDROLEUPDATEEMAILS API");
		EmployeeDTO empDto = new EmployeeDTO();
		boolean sendRoleUpdateEmails = iEmployeeDao.sendRoleUpdateEmails(branchId);
		try {
			if (sendRoleUpdateEmails) {
				empDto.setStatus(EnovationConstants.statusSuccess);
				empDto.setStatusCode(EnovationConstants.Code200);
				empDto.setResult(EnovationConstants.ResultTrue);
			} else {
				empDto.setStatus(EnovationConstants.statusFail);
				empDto.setStatusCode(EnovationConstants.Code200);
				empDto.setResult(EnovationConstants.ResultFalse);
			}
		} catch (Exception e) {
			empDto.setStatus(EnovationConstants.statusFail);
			empDto.setStatusCode(EnovationConstants.Code500);
			empDto.setResult(EnovationConstants.ResultFalse);
			empDto.setReason("EXCEPTION OCCUR IN SENDROLEUPDATEEMAILS API : " + e.getMessage());
			LOGGER.error("#INSIDE EXCEPTION OCCERED IN SENDROLEUPDATEEMAILS " + e.getMessage());
			return empDto;
		}
		return empDto;
	}

	@Override
	public EmployeeDTO checkLoginEmail(String email) {
		LOGGER.info("#INSIDE IN CHECKLOGINEMAIL API");
		EmployeeDTO empDto = new EmployeeDTO();
		if (email != null) {
			EmployeeDetails emp = iEmployeeDao.checkLoginEmail(email);
			try {
				if (emp != null) {
					if (emp.getIsDeactive() == EnovationConstants.ZERO
							&& (emp.getIsEmailVerified() == EnovationConstants.ZERO && emp.getContactNo() == null)
							&& emp.getIsNotify() == EnovationConstants.ONE) {
						empDto.setResult(EnovationConstants.ResultFalse);
						empDto.setStatus(EnovationConstants.statusFail);
						List<EmployeeDetails> empList = new ArrayList<>();
						emp.setIsSetupCompleted(EnovationConstants.ONE);
						empList.add(emp);
						boolean flag = employeeDaoImp.sendBulkEmailtoEmployee(empList);
						if (flag) {
							empDto.setStatusCode(EnovationConstants.Code100);
							empDto.setReason(EnovationConstants.RESENDVERIFICATIONLINK);
						} else {
							empDto.setStatusCode(EnovationConstants.Code200);
						}
					} else {

						empDto.setStatus(EnovationConstants.statusSuccess);
						empDto.setStatusCode(EnovationConstants.Code200);
						empDto.setResult(EnovationConstants.ResultTrue);
						empDto.setPortalLink(emp.getOrganization().getPortalLink());
						List<MultiBranchAccess> orgExeList = iEmployeeDao.getOrgExeBranchList(email);
						if (!orgExeList.isEmpty()) {
							empDto.setMultiBranchList(orgExeList);
						}

					}
				} else {
					empDto.setStatus(EnovationConstants.statusFail);
					empDto.setStatusCode(EnovationConstants.Code200);
					empDto.setReason(EnovationConstants.RecordsDoesNotExist);
				}
			} catch (Exception e) {
				empDto.setStatus(EnovationConstants.statusFail);
				empDto.setStatusCode(EnovationConstants.Code500);
				empDto.setReason("EXCEPTION OCCERED IN CHECKLOGINEMAIL API : " + e.getMessage());
				LOGGER.error("#INSIDE EXCEPTION OCCERED IN CHECKLOGINEMAIL " + e.getMessage());
				return empDto;
			}
		} else {
			empDto.setStatus(EnovationConstants.statusSuccess);
			empDto.setStatusCode(EnovationConstants.CODE400);
			empDto.setReason(EnovationConstants.BAD_REQUEST);
		}
		return empDto;
	}

	@SuppressWarnings("unused")
	@Override
	public EmployeeDTO getAllEmplyeeListByBranchNew(Integer orgId, Integer branchId, Integer deptId) {
		LOGGER.info("# INSIDE IN GETALLEMPLYEELISTBYBRANCHNEW API");
		EmployeeDTO empDto = new EmployeeDTO();
		if (branchId != 0 || orgId != 0) {
			List<EmployeeDetails> employeeDet = iEmployeeDao.getAllEmplyeeListByBranchNew(orgId, branchId, deptId);
			try {
				if (employeeDet != null && employeeDet.size() > 0) {
					empDto.setStatus(EnovationConstants.statusSuccess);
					empDto.setStatusCode(EnovationConstants.Code200);
					empDto.setResult(EnovationConstants.ResultTrue);
					List<EmployeeDetailsBean> empSet = new ArrayList<EmployeeDetailsBean>();
					employeeDet.stream().forEach(employeeDetails -> {
						EmployeeDetailsBean empListBean = new EmployeeDetailsBean();
						BeanUtils.copyProperties(employeeDetails, empListBean);
						if (employeeDetails.getBranchId() > 0) {
							BranchMaster branch = new BranchMaster(employeeDetails.getBranchId(),
									employeeDetails.getBranchName());
							empListBean.setBranch(branch);
						}
						if (employeeDetails.getDeptId() > 0) {
							DepartmentMaster dept = new DepartmentMaster(employeeDetails.getDeptId(),
									employeeDetails.getDeptName());
							empListBean.setDept(dept);
						}
						employeeDet.stream().forEach(y -> {

							if (employeeDetails.getEmpId() == y.getEmpId()) {
								if (employeeDetails.getRoleId() > 0) {
									Role role = new Role(employeeDetails.getRoleId(),
											RoleName.valueOf(employeeDetails.getRoleName()));
									// y.getEmproles().stream().sorted(Comparator.comparingLong(Role::getId));
									y.getRoles().add(role);
								}
							}
						});
						empSet.add(empListBean);
					});
					List<EmployeeDetailsBean> list = empSet.stream()
							.collect(collectingAndThen(
									toCollection(() -> new TreeSet<>(comparingInt(EmployeeDetailsBean::getEmpId))),
									ArrayList::new));
					empDto.setAllEmpDetails(list);
					System.out.println("Employee count is : " + list.size());
				} else {
					empDto.setStatus(EnovationConstants.statusFail);
					empDto.setStatusCode(EnovationConstants.Code200);
					empDto.setResult(EnovationConstants.ResultFalse);
					empDto.setReason(EnovationConstants.RecordsDoesNotExist);

				}
			} catch (Exception e) {
				e.printStackTrace();
				empDto.setStatus(EnovationConstants.statusFail);
				empDto.setStatusCode(EnovationConstants.Code500);
				empDto.setResult(EnovationConstants.ResultFalse);
				empDto.setReason("EXCEPTION OCCUR IN GETALLEMPLYEELISTBYBRANCHNEW API : " + e.getMessage());
				LOGGER.error("# INSIDE EXCEPTION OCCERED IN GETALLEMPLYEELISTBYBRANCHNEW " + e.getMessage());
				return empDto;
			}
		} else {
			empDto.setStatus(EnovationConstants.statusSuccess);
			empDto.setStatusCode(EnovationConstants.CODE400);
			empDto.setReason(EnovationConstants.BAD_REQUEST);
		}
		return empDto;

	}

	@Override
	public EmployeeDTO saveFeedback(Feedback feedback) {
		EmployeeDTO empDTO = new EmployeeDTO();
		boolean savefeed = iEmployeeDao.saveFeedback(feedback);
		try {
			if (savefeed) {
				empDTO.setStatus(EnovationConstants.statusSuccess);
				empDTO.setStatusCode(EnovationConstants.Code200);
				empDTO.setResult(EnovationConstants.ResultTrue);
			} else {
				empDTO.setStatus(EnovationConstants.statusFail);
				empDTO.setStatusCode(EnovationConstants.Code200);
				empDTO.setResult(EnovationConstants.ResultFalse);
			}

		} catch (Exception e) {
			empDTO.setStatus(EnovationConstants.statusFail);
			empDTO.setStatusCode(EnovationConstants.Code500);
			empDTO.setResult(EnovationConstants.ResultFalse);
			empDTO.setReason("EXCEPTION OCCUR IN SAVEFEEDBACK API : " + e.getMessage());
			LOGGER.error("#INSIDE EXCEPTION OCCERED IN SAVEFEEDBACK " + e.getMessage());
			return empDTO;
		}
		return empDTO;
	}

	@Override
	public EmployeeDTO getFeedbackList() {
		EmployeeDTO empDTO = new EmployeeDTO();
		List<FeedbackType> list = iEmployeeDao.getFeedbackList();
		try {
			if (list != null && list.size() > 0) {
				empDTO.setStatus(EnovationConstants.statusSuccess);
				empDTO.setStatusCode(EnovationConstants.Code200);
				empDTO.setResult(EnovationConstants.ResultTrue);
				empDTO.setFeedBackTypeList(list);
			} else {
				empDTO.setStatus(EnovationConstants.statusFail);
				empDTO.setStatusCode(EnovationConstants.Code200);
				empDTO.setResult(EnovationConstants.ResultFalse);
			}

		} catch (Exception e) {
			empDTO.setStatus(EnovationConstants.statusFail);
			empDTO.setStatusCode(EnovationConstants.Code500);
			empDTO.setResult(EnovationConstants.ResultFalse);
			empDTO.setReason("EXCEPTION OCCUR IN getFeedbackList API : " + e.getMessage());
			LOGGER.error("#INSIDE EXCEPTION OCCERED IN getFeedbackList " + e.getMessage());
			return empDTO;
		}
		return empDTO;
	}

	@Override
	public EmployeeDTO resetPasswordBySuperadmin(EmployeeDetails empdetails) {
		LOGGER.info("#INSIDE IN RESETPASSWORD API");
		EmployeeDTO empDto = null;
		boolean reset = iEmployeeDao.resetPasswordBySuperadmin(empdetails);
		try {
			if (reset) {
				empDto = new EmployeeDTO();
				empDto.setStatus(EnovationConstants.statusSuccess);
				empDto.setStatusCode(EnovationConstants.Code200);
				empDto.setResult(EnovationConstants.ResultTrue);
			} else {
				empDto = new EmployeeDTO();
				empDto.setStatus(EnovationConstants.statusFail);
				empDto.setStatusCode(EnovationConstants.Code100);
				empDto.setResult(EnovationConstants.ResultFalse);
				empDto.setReason(EnovationConstants.INVALID_PASS);
			}
		} catch (Exception e) {
			empDto = new EmployeeDTO();
			empDto.setStatus(EnovationConstants.statusFail);
			empDto.setStatusCode(EnovationConstants.Code500);
			empDto.setResult(EnovationConstants.ResultFalse);
			empDto.setReason("EXCEPTION OCCUR IN RESETPASSWORDBYSUPERADMIN API : " + e.getMessage());
			LOGGER.error("#INSIDE EXCEPTION OCCERED IN RESETPASSWORDBYSUPERADMIN " + e.getMessage());
			return empDto;
		}
		return empDto;
	}

	@Override
	public EmployeeDTO getNoticeDetails(Integer branchId, Integer deptId) {

		EmployeeDTO empDTO = new EmployeeDTO();

		List<NoticeSetup> list = iEmployeeDao.getNoticeDetails(branchId, deptId);

		try {
			if (list != null && !list.isEmpty()) {
				empDTO.setStatus(EnovationConstants.statusSuccess);
				empDTO.setStatusCode(EnovationConstants.Code200);
				empDTO.setResult(EnovationConstants.ResultTrue);
				empDTO.setNoticeList(list);
				// empDTO.setFeedBackTypeList(list);
			} else {
				empDTO.setStatus(EnovationConstants.statusFail);
				empDTO.setStatusCode(EnovationConstants.Code100);
				empDTO.setResult(EnovationConstants.ResultFalse);
			}

		} catch (Exception e) {
			empDTO.setStatus(EnovationConstants.statusFail);
			empDTO.setStatusCode(EnovationConstants.Code500);
			empDTO.setResult(EnovationConstants.ResultFalse);
			empDTO.setReason("EXCEPTION OCCUR IN GETNOTICELIST API : " + e.getMessage());
			LOGGER.error("#INSIDE EXCEPTION OCCERED IN GETNOTICELIST " + e.getMessage());
			return empDTO;
		}
		return empDTO;

	}

	@Override
	public EmployeeDTO addSuggetionTemplate(SuggestionTemplate temp) {
		EmployeeDTO empDTO = new EmployeeDTO();
		Gson g = new Gson();
		LOGGER.info("#INSIDE  IN ADD SUGGETIONTEMPLATE : HEADER REQUEST :" + g.toJson(temp));
		boolean isSave = iEmployeeDao.addSuggetionTemplate(temp);
		try {
			if (isSave) {
				empDTO.setStatus(EnovationConstants.statusSuccess);
				empDTO.setStatusCode(EnovationConstants.Code200);
				empDTO.setResult(EnovationConstants.ResultTrue);
			} else {
				empDTO.setStatus(EnovationConstants.statusFail);
				empDTO.setStatusCode(EnovationConstants.Code200);
				empDTO.setResult(EnovationConstants.ResultFalse);
			}

		} catch (Exception e) {
			empDTO.setStatus(EnovationConstants.statusFail);
			empDTO.setStatusCode(EnovationConstants.Code500);
			empDTO.setResult(EnovationConstants.ResultFalse);
			empDTO.setReason("EXCEPTION OCCUR IN ADD SUGGETIONTEMPLATE API : " + e.getMessage());
			LOGGER.error("#INSIDE EXCEPTION OCCERED IN ADD SUGGETIONTEMPLATE " + e.getMessage());
			return empDTO;
		}
		return empDTO;
	}

	@Override
	public EmployeeDTO getEmpRolesListNew(EmployeeDetails empdetails) {
		EmployeeDTO empDTO = new EmployeeDTO();

		List<EmployeeDetails> list = iEmployeeDao.getEmpRolesListNew(empdetails);

		try {
			if (list != null && !list.isEmpty()) {
				empDTO.setStatus(EnovationConstants.statusSuccess);
				empDTO.setStatusCode(EnovationConstants.Code200);
				empDTO.setResult(EnovationConstants.ResultTrue);
				empDTO.setList(list);
			} else {
				empDTO.setStatus(EnovationConstants.statusFail);
				empDTO.setStatusCode(EnovationConstants.Code100);
				empDTO.setResult(EnovationConstants.ResultFalse);
			}

		} catch (Exception e) {
			empDTO.setStatus(EnovationConstants.statusFail);
			empDTO.setStatusCode(EnovationConstants.Code500);
			empDTO.setResult(EnovationConstants.ResultFalse);
			LOGGER.error("#INSIDE EXCEPTION OCCERED IN EMP ROLES LIST NEW " + e.getMessage());
			return empDTO;
		}
		return empDTO;

	}

	@Override
	public EmployeeDTO getAllEmpListByBrannchIds(EmployeeDTO request) {
		LOGGER.info("# INSIDE IN GETALLEMPLYEELISTBYBRANCHNEW API");
		EmployeeDTO empDto = new EmployeeDTO();
		if (!request.getBranchIds().isEmpty()) {
			List<EmployeeDetails> employeeDet = iEmployeeDao.getAllEmpListByBrannchIds(request);
			try {
				if (employeeDet != null && employeeDet.size() > 0) {
					empDto.setStatus(EnovationConstants.statusSuccess);
					empDto.setStatusCode(EnovationConstants.Code200);
					empDto.setResult(EnovationConstants.ResultTrue);
					List<EmployeeDetailsBean> empSet = new ArrayList<EmployeeDetailsBean>();
					employeeDet.stream().forEach(employeeDetails -> {
						EmployeeDetailsBean empListBean = new EmployeeDetailsBean();
						BeanUtils.copyProperties(employeeDetails, empListBean);
						if (employeeDetails.getBranchId() > 0) {
							BranchMaster branch = new BranchMaster(employeeDetails.getBranchId(),
									employeeDetails.getBranchName());
							empListBean.setBranch(branch);
						}
						if (employeeDetails.getDeptId() > 0) {
							DepartmentMaster dept = new DepartmentMaster(employeeDetails.getDeptId(),
									employeeDetails.getDeptName());
							empListBean.setDept(dept);
						}
						if (employeeDetails.getEmpLvlId() != 0) {
							EmployeeHierarchy empLvl = new EmployeeHierarchy(employeeDetails.getEmpLvlId(),
									employeeDetails.getEmpLvlType(), employeeDetails.getEmpLvlName());
							empListBean.setEmpLevelDetails(empLvl);
						}
						employeeDet.stream().forEach(y -> {

							if (employeeDetails.getEmpId() == y.getEmpId()) {
								if (employeeDetails.getRoleId() > 0) {
									Role role = new Role(employeeDetails.getRoleId(),
											RoleName.valueOf(employeeDetails.getRoleName()));
									// y.getEmproles().stream().sorted(Comparator.comparingLong(Role::getId));
									y.getRoles().add(role);
								}
							}
						});
						empSet.add(empListBean);
					});
					List<EmployeeDetailsBean> list = empSet.stream()
							.collect(collectingAndThen(
									toCollection(() -> new TreeSet<>(comparingInt(EmployeeDetailsBean::getEmpId))),
									ArrayList::new));
					empDto.setAllEmpDetails(list);
					System.out.println("Employee count is : " + list.size());
				} else {
					empDto.setStatus(EnovationConstants.statusFail);
					empDto.setStatusCode(EnovationConstants.Code200);
					empDto.setResult(EnovationConstants.ResultFalse);
					empDto.setReason(EnovationConstants.RecordsDoesNotExist);

				}
			} catch (Exception e) {
				e.printStackTrace();
				empDto.setStatus(EnovationConstants.statusFail);
				empDto.setStatusCode(EnovationConstants.Code500);
				empDto.setResult(EnovationConstants.ResultFalse);
				empDto.setReason("EXCEPTION OCCUR IN GETALLEMPLYEELISTBYBRANCHNEW API : " + e.getMessage());
				LOGGER.error("# INSIDE EXCEPTION OCCERED IN GETALLEMPLYEELISTBYBRANCHNEW " + e.getMessage());
				return empDto;
			}
		} else {
			empDto.setStatus(EnovationConstants.statusSuccess);
			empDto.setStatusCode(EnovationConstants.CODE400);
			empDto.setReason(EnovationConstants.BAD_REQUEST);
		}
		return empDto;
	}

	@Override
	public EmployeeDTO saveLoginFeedback(Feedback feedback) {
		EmployeeDTO empDTO = new EmployeeDTO();
		Map<String, Object> res = new HashMap<>();
		res = iEmployeeDao.saveLoginFeedback(feedback);
		try {
			if (res != null && !res.isEmpty()) {
				empDTO.setStatus(EnovationConstants.statusSuccess);
				empDTO.setStatusCode(EnovationConstants.Code200);
				empDTO.setResult(EnovationConstants.ResultTrue);
				empDTO.setSrNumber(String.valueOf(res.get("srNumber")));
			} else {
				empDTO.setStatus(EnovationConstants.statusFail);
				empDTO.setStatusCode(EnovationConstants.Code200);
				empDTO.setResult(EnovationConstants.ResultFalse);
			}
		} catch (Exception e) {
			empDTO.setStatus(EnovationConstants.statusFail);
			empDTO.setStatusCode(EnovationConstants.Code500);
			LOGGER.error("#INSIDE EXCEPTION OCCERED IN SAVELOGINFEEDBACK " + ExceptionUtils.getStackFrames(e));
			Gson gson = new Gson();
			String json = gson.toJson(feedback);
			commonUtils.sendAPIFailedExceptionMail("Exception Occured At - " + this.getClass().getSimpleName(),
					" <p><b> Method Name - </b>" + new Throwable().getStackTrace()[0].getMethodName() + " <br></p> "
							+ "<p><b> Request Body - </b>" + json + "<p><b> Exception - </b>"
							+ ExceptionUtils.getStackTrace(e));
			return empDTO;
		}
		return empDTO;
	}

	@Override
	public EmployeeDTO getEmployeeLevelDetails(int branchId) {
		LOGGER.info("# INSIDE IN get employee level API");
		EmployeeDTO empDto = new EmployeeDTO();

		List<EmployeeHierarchy> result = iEmployeeDao.getEmployeeLevelDetails(branchId);

		if (!result.isEmpty()) {
			empDto = new EmployeeDTO();
			empDto.setStatus(EnovationConstants.statusSuccess);
			empDto.setStatusCode(EnovationConstants.Code200);
			empDto.setResult(EnovationConstants.ResultTrue);
			empDto.setEmpLevelDetails(result);
		} else {
			empDto = new EmployeeDTO();
			empDto.setStatus(EnovationConstants.statusFail);
			empDto.setStatusCode(EnovationConstants.Code200);
			empDto.setResult(EnovationConstants.ResultFalse);
		}

		return empDto;
	}

	/**
	 * @author Vinay B. Dec 27, 2021 1:47:49 PM
	 */
	@Override
	public UserResponse getModuleWisePendingActions(int empId) {
		UserResponse response = new UserResponse();
		try {
			EmployeeDTO result = iEmployeeDao.getModuleWisePendingActions(empId);
			if (result != null) {
				response.setEmpDto(result);
				return BuildResponse.success(response);
			} else {
				return BuildResponse.failWithCode100(response);
			}

		} catch (Exception e) {
			e.printStackTrace();
			BuildResponse.internalServerError(response);
		}

		return response;

	}

	/**
	 * @author Vinay B. Dec 27, 2021 3:01:14 PM
	 */
	@Override
	public UserResponse transferModuleWisePendingActions(EnovationDTO req) {
		UserResponse response = new UserResponse();
		try {
			boolean flag = iEmployeeDao.transferModuleWisePendingActions(req);
			if (flag) {
				return BuildResponse.success(response);
			} else {
				return BuildResponse.fail(response);
			}
		} catch (Exception e) {
			e.printStackTrace();
			BuildResponse.internalServerError(response);
		}

		return response;

	}

	/**
	 * @author Vinay B. May 19, 2022 11:06:58 AM
	 */
	@Override
	public UserResponse deactivateEmployee(int empId) {
		LOGGER.error("# INSIDE ServiceImpl | deactivateEmployee");
		UserResponse response = new UserResponse();
		try {
			int res = iEmployeeDao.deactivateEmployee(empId);
			if (res == 1) {
				return BuildResponse.success(response);
			} else if (res == 2) {
				response.setReason("Employee has pending action, please complete the actions or transfer.");
				return BuildResponse.failWithCode100(response);
			} else {
				return BuildResponse.fail(response);
			}
		} catch (Exception e) {
			e.printStackTrace();
			BuildResponse.internalServerError(response);
		}

		return response;
	}

	/**
	 * @author Vinay B. May 19, 2022 11:05:53 PM
	 */
	@Override
	public UserResponse employeeTransfer(EmployeeTransferDTO request) {
		LOGGER.error("# INSIDE ServiceImpl | employeeTransfer");
		UserResponse response = new UserResponse();
		try {
			boolean flag = iEmployeeDao.employeeTransfer(request);
			if (flag) {
				return BuildResponse.success(response);
			} else {
				return BuildResponse.fail(response);
			}
		} catch (Exception e) {
			e.printStackTrace();
			BuildResponse.internalServerError(response);
		}

		return response;
	}
	
	@Override
	public EmployeeDTO addUpdateEmployeeLevel(EmployeeHierarchy request) {
		LOGGER.info("# INSIDE IN add update employee level API");
		EmployeeDTO empDto = new EmployeeDTO();
		boolean flag = false;
		if (request != null) {

			flag = iEmployeeDao.addUpdateEmployeeLevel(request);

			if (flag) {
				empDto = new EmployeeDTO();
				empDto.setStatus(EnovationConstants.statusSuccess);
				empDto.setStatusCode(EnovationConstants.Code200);
				empDto.setResult(EnovationConstants.ResultTrue);
			} else {
				empDto = new EmployeeDTO();
				empDto.setStatus(EnovationConstants.statusFail);
				empDto.setStatusCode(EnovationConstants.Code200);
				empDto.setResult(EnovationConstants.ResultFalse);
			}
		}

		return empDto;
	}

	@Override
	public EmployeeDTO sendMailToSuperAdminForPasswordReset(EmployeeDTO request) {
		LOGGER.info("# INSIDE IN EmployeeService | sendMailToSuperAdminForPasswordReset");
		EmployeeDTO empDto = new EmployeeDTO();
		boolean flag = false;
		if (request.getType().equals(EnovationConstants.SUPERADMIN_STRING)) {
			flag = iEmployeeDao.sendMailToSuperAdminForPasswordReset(request);
		} else {
			empDto = new EmployeeDTO();
			empDto.setReason(EnovationConstants.BAD_REQUEST);
		}
		if (flag) {
			empDto = new EmployeeDTO();
			empDto.setReason("Mail Sent To SUPERADMIN");
			empDto.setStatus(EnovationConstants.statusSuccess);
			empDto.setStatusCode(EnovationConstants.Code200);
			empDto.setResult(EnovationConstants.ResultTrue);
		} else {
			empDto = new EmployeeDTO();
			empDto.setStatus(EnovationConstants.statusFail);
			empDto.setStatusCode(EnovationConstants.Code200);
			empDto.setResult(EnovationConstants.ResultFalse);
		}
		return empDto;
	}

	/**
	 * @author shyam 20 June 2020
	 * @param request
	 * @return
	 */
	@Override
	public MasterResponse bulkEmployeeUpload(EmployeeDTO request) {
		MasterResponse response = new MasterResponse();
		try {
			String flag = iEmployeeDao.bulkEmployeeUpload(request, response);
			if (flag.equals(EnovationConstants.SUCCESS)) {
				BuildResponse.success(response);
			} else if (!flag.equals(EnovationConstants.statusFail)) {
				BuildResponse.fail100(response);
			} else {
				BuildResponse.fail(response);
			}
		} catch (Exception e) {
			e.printStackTrace();
			BuildResponse.internalServerError(response);
		}

		return response;

	}
	
	/**
	 * @author Aditi V. 17 feb 2021
	 */
	@Override
	public MasterResponse importBulkEmployeeData(EmployeeDTO request) {
		LOGGER.info("# In PMS Service | importBulkEmployeeData ");
		MasterResponse res = new MasterResponse();
		try {
			EmployeeDTO dto = iEmployeeDao.importBulkEmployeeData(request);
			if (dto.isErrorInSheet()) {
				res.setExcelUploadError(dto);
				return BuildResponse.failWithCode100(res);
			} else {
				return BuildResponse.success(res);
			}

		} catch (Exception e) {
			e.printStackTrace();
			res = BuildResponse.internalServerError(res);
			LOGGER.error("# EXCEPTION OCCURED INSIDE IMPORT employee  DATA API" + e.getMessage());
			return res;
		}
	}

	@Override
	public EmployeeDTO reActivationEmployee(EmployeeDTO request) {
		LOGGER.info("# INSIDE EMPLOYEE SERVICE IMPL | reActivationEmployee");
		EmployeeDTO response = new EmployeeDTO();
		try {
			boolean flag = iEmployeeDao.reActivationEmployee(request);
			if (flag) {
				BuildResponse.success(response);
			} else {
				BuildResponse.fail(response);
			}

		} catch (Exception e) {
			e.printStackTrace();
			BuildResponse.internalServerError(response);
			response.setReason("EXCEPTION OCCURED IN reActivationEmployee API : " + e.getMessage());
			LOGGER.error("# INSIDE EXCEPTION OCCERED IN reActivationEmployee  " + e.getMessage());
			commonUtils.sendAPIFailedExceptionMail("Exception Occured At - " + this.getClass().getSimpleName(),
					" <p><b> Method Name - </b>" + new Throwable().getStackTrace()[0].getMethodName() + " <br></p> "
							+ "<p><b> Path Variable - </b>" + request.getToken() + "<p><b> Exception - </b>"
							+ ExceptionUtils.getStackTrace(e));
		}

		return response;
	}
}
