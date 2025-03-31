package com.greentin.enovation.audit;

import javax.transaction.Transactional;
import org.hibernate.Session;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import com.greentin.enovation.model.EmployeeDetails;
import com.greentin.enovation.utils.BaseRepository;
import com.greentin.enovation.utils.CommonUtils;
import com.greentin.enovation.utils.EnovationConstants;

@Component
public class AuditComponent extends BaseRepository {
	private static final Logger LOGGER = LoggerFactory.getLogger(AuditComponent.class);

	@Transactional
	public boolean insertBranchMasterAudit(Session session, String action, int actionById, String previousValue,
			String currentValue) {
		LOGGER.info("INSIDE insertBranchMasterAudit METHOD");
		boolean flag = false;
		try {
			BranchMasterAudit branch = new BranchMasterAudit();
			branch.setAction(action);
			branch.setActionBy(new EmployeeDetails(actionById));
			branch.setActionDate(CommonUtils.currentDate());
			branch.setPreviousValue(previousValue);
			branch.setCurrentValue(currentValue);
			session.save(branch);
			flag = true;
		} catch (Exception e) {
			LOGGER.info("ERROR INSIDE insertBranchMasterAudit METHOD");
			e.printStackTrace();
		}
		return flag;
	}

	@Transactional
	public boolean insertDepartmentMasterAudit(Session session, String action, int actionById, String previousValue,
			String currentValue) {
		LOGGER.info("INSIDE insertDepartmentMasterAudit METHOD");
		boolean flag = false;
		try {
			DepartmentMasterAudit dept = new DepartmentMasterAudit();
			dept.setAction(action);
			if (actionById != 0) {
				dept.setActionBy(new EmployeeDetails(actionById));
			}
			dept.setActionDate(CommonUtils.currentDate());
			dept.setPreviousValue(previousValue);
			dept.setCurrentValue(currentValue);
			session.save(dept);
			flag = true;
		} catch (Exception e) {
			LOGGER.info("ERROR INSIDE insertDepartmentMasterAudit METHOD");
			e.printStackTrace();
		}
		return flag;
	}

	@Transactional
	public boolean insertCategoryMasterAudit(Session session, String action, int actionBy, String previousValue,
			String currentValue) {
		LOGGER.info("INSIDE insertCategoryMasterAudit METHOD");
		boolean flag = false;
		try {
			CategoryMasterAudit category = new CategoryMasterAudit();
			category.setAction(action);
			if (actionBy != 0) {
				category.setActionBy(new EmployeeDetails(actionBy));
			}
			category.setActionDate(CommonUtils.currentDate());
			category.setPreviousValue(previousValue);
			category.setCurrentValue(currentValue);
			session.save(category);
			flag = true;
		} catch (Exception e) {
			LOGGER.info("ERROR INSIDE insertCategoryMasterAudit METHOD");
			e.printStackTrace();
		}
		return flag;
	}

	@Transactional
	public boolean insertEmpRolesAudit(Session session,String action, int actionBy, int previousValue, int currentValue,int deptLevelId) {
		LOGGER.info("INSIDE insertEmpRolesAudit METHOD");
		boolean flag = false;
		try {
			EmpRolesAudit role = new EmpRolesAudit();
			role.setAction(action);
			if (actionBy != 0) {
				role.setActionBy(new EmployeeDetails(actionBy));
			}
			role.setActionDate(CommonUtils.currentDate());
			if (previousValue != 0) {
				role.setPreviousValue(new EmployeeDetails(previousValue));
			}
			if (currentValue != 0) {
				role.setCurrentValue(new EmployeeDetails(currentValue));
			}
			if(deptLevelId!=0) {
				role.setLevelId(deptLevelId);
			}
			session.save(role);
			flag = true;
		} catch (Exception e) {
			LOGGER.info("ERROR INSIDE insertEmpRolesAudit METHOD");
			e.printStackTrace();
		}
		return flag;
	}

	@Transactional
	public boolean insertBenefitAudit(Session session,String action, int actionBy, String previousValue, String currentValue) {
		LOGGER.info("INSIDE insertEmpRolesAudit METHOD");
		boolean flag = false;
		try {
			BenefitMasterAudit benefit = new BenefitMasterAudit();
			benefit.setAction(action);
			if (actionBy != 0) {
				benefit.setActionBy(new EmployeeDetails(actionBy));
			}
			benefit.setActionDate(CommonUtils.currentDate());
			if (previousValue != null) {
				benefit.setPreviousValue(previousValue);
			}
			if (currentValue != null) {
				benefit.setCurrentValue(currentValue);
			}
			session.save(benefit);
			flag = true;
		} catch (Exception e) {
			LOGGER.info("ERROR INSIDE insertEmpRolesAudit METHOD");
			e.printStackTrace();
		}
		return flag;
	}
	
	@Transactional
	public boolean insertEmpDetailsAudit(Session session, String action, int actionBy, String previousValue,
			String currentValue, int empId) {
		LOGGER.info("INSIDE insertEmpRolesAudit METHOD");
		boolean flag = false;
		try {
			EmployeeDetailsAudit empAudit = new EmployeeDetailsAudit();
			empAudit.setAction(action);
			if (actionBy != 0) {
				empAudit.setActionBy(new EmployeeDetails(actionBy));
			}
			empAudit.setActionDate(CommonUtils.currentDate());
			empAudit.setPreviousValue(previousValue);
			empAudit.setCurrentValue(currentValue);
			empAudit.setEmpId(new EmployeeDetails(empId));
			session.save(empAudit);
			flag = true;
		} catch (Exception e) {
			LOGGER.info("ERROR INSIDE insertEmpRolesAudit METHOD");
			e.printStackTrace();
		}
		return flag;
	}

	@Transactional
	public int insertLoginAudit(int trackerId, int empId, String key,String userName, String comment ,String requestIp,int branchId,int orgId,String responseString) {
		LOGGER.info("INSIDE INSERTLOGINAUDIT METHOD");
		int transactionId = 0;
		try {
			Session session = getCurrentSession();
			if (trackerId == 0) {
				String platform = "";
				if(key != null) {
					platform=EnovationConstants.PLATFORM_MOBILE;
				}{
					platform = EnovationConstants.PLATFORM_PORTAL;
				}
				LoginAudit login = new LoginAudit();
				    login.setEmpId(empId);
					login.setUserIp(requestIp);
					login.setComment(comment);
					login.setUserName(userName);
					login.setPlatform(platform);
					login.setBranchId(branchId);
					login.setOrgId(orgId);
					//login.setResponseString(responseString);
				session.save(login);
				transactionId = login.getLoginTrackerId();
				return transactionId;
			} else {
				LoginAudit logout = (LoginAudit)session.get(LoginAudit.class, trackerId);
				if(logout.getComment() != null ) {
					logout.setComment(logout.getComment()+","+comment);
				}else {
				logout.setComment(comment);
				}
				logout.setLogoutDateTime(CommonUtils.currentDate());
				session.update(logout);
			}
		} catch (Exception e) {
			LOGGER.info("ERROR INSIDE insertLoginAudit METHOD");
			e.printStackTrace();
		}
		return transactionId;
	}
}
