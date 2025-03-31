package com.greentin.enovation.utils;

import com.greentin.enovation.model.EmployeeDetails;
import java.util.List;

import javax.persistence.Tuple;
import javax.persistence.TypedQuery;
import javax.transaction.Transactional;

import org.hibernate.Session;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import com.greentin.enovation.model.BranchMaster;
import com.greentin.enovation.model.CWAgency;
import com.greentin.enovation.model.OrganizationMaster;

@Component
@Transactional
public class DBUtils {

	private static final Logger LOGGER = LoggerFactory.getLogger(DBUtils.class);

	public EmployeeDetails getCWEmpDetailByMobile(Session session, String mob, int orgId) {
		LOGGER.info("Inside SuggestionDaoImple | getEmpDetailByMobile");
		EmployeeDetails emp = null;

		List<Tuple> objList = session.createNativeQuery(
				"select emp_id as empId, emp_level_id as levelId,dept_id as deptId,line_id as lineId,org_id as orgId,branch_id as branchId  "
						+ " from tbl_employee_details where contact_no=:mob and org_id=:orgId and emp_type='CW' LIMIT 1 ",
				Tuple.class).setParameter("mob", mob).setParameter("orgId", orgId).getResultList();

		if (!CollectionUtils.isEmpty(objList)) {
			emp = new EmployeeDetails();
			Tuple obj = objList.get(0);
			emp.setEmpId(CommonUtils.objectToInt(obj.get("empId")));
			emp.setOrgId(CommonUtils.objectToInt(obj.get("orgId")));
			emp.setBranchId(CommonUtils.objectToInt(obj.get("branchId")));
		}

		return emp;
	}

	public boolean checkAndAddEmpRole(Session session, long empId, long roleId) {
		LOGGER.info("Inside DBUtils | checkAndAddEmpRole");
		boolean flag = false;

		TypedQuery<Tuple> query = session.createNativeQuery(
				"select emp_id,role_id from emp_roles where emp_id=:empId and role_id=:roleId ", Tuple.class);
		query.setParameter("empId", empId).setParameter("roleId", roleId);
		List<Tuple> list = query.getResultList();
		if (CollectionUtils.isEmpty(list)) {
			session.createNativeQuery("insert into emp_roles(emp_id,role_id) values(:empId,:roleId) ")
					.setParameter("empId", empId).setParameter("roleId", roleId).executeUpdate();
		}else {
			System.out.println("Employee Role Already Exist");
		}

		flag = true;
		return flag;
	}

	public boolean removeEmpRole(Session session, long empId, long roleId) {
		LOGGER.info("Inside DBUtils | removeEmpRole");
		System.err.println("empId=>"+empId + " |roleId=>" + roleId);
		return session.createNativeQuery("delete from emp_roles where emp_id=:emp_id and role_id=:roleId")
				.setParameter("emp_id", empId).setParameter("roleId", roleId).executeUpdate() > 0 ? true : false;

	}

}
