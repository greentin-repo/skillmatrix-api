package com.greentin.enovation.skillmatrix;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.persistence.Tuple;
import javax.persistence.TypedQuery;
import javax.transaction.Transactional;

import org.hibernate.Session;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import com.greentin.enovation.audit.SkillMatrixAudit;
import com.greentin.enovation.dto.SkillMatrixRequest;
import com.greentin.enovation.utils.BaseRepository;
import com.greentin.enovation.utils.ColumnAscDescConstants;
import com.greentin.enovation.utils.CommonUtils;

@Component
@Transactional
public class SMAnalyticsDaoImpl extends BaseRepository implements SMAnalyticsIDao {
	private static final Logger LOGGER = LoggerFactory.getLogger(SMAnalyticsDaoImpl.class);

	@Autowired
	SkillMatrixAudit smAudit;

	@Autowired
	SkillMatrixWorker smWorker;

	@Autowired
	SkillMatrixUtils smUtils;

	@Override

	public HashMap<String, Object> getOrgLvlAnalytics(SkillMatrixRequest request) {
		LOGGER.info(" In getOrgLvlAnalytics | getGraphDetails");
		Session session = getCurrentSession();
		HashMap<String, Object> data = new HashMap<>();

		List<HashMap<String, Object>> orgLvlSkilling = getOrgSMLevelGraph(request, session);

		if (!CollectionUtils.isEmpty(orgLvlSkilling)) {
			data.put("orgLvlSkilling", orgLvlSkilling);
		}
		return data;
	}

	private List<HashMap<String, Object>> getOrgSMLevelGraph(SkillMatrixRequest request, Session session) {
		List<HashMap<String, Object>> dataList = new ArrayList<>();
		StringBuilder builder = new StringBuilder("SELECT IFNULL(COUNT(sm.id), 0) AS actualCount, \n"
				+ " ROUND(IFNULL(req.required, 0), 0) AS requiredCount, mb.branch_id AS branchId, \n"
				+ " mb.name AS branch,sm.skill_level_id AS Lvl,sl.level_name AS levelName FROM sm_skill_level sl \n"
				+ " INNER JOIN sm_emp_skill_matrix sm ON sm.skill_level_id = sl.id \n"
				+ " INNER JOIN sm_workstations sw ON sw.id = sm.workstations_id \n"
				+ " INNER JOIN master_branch mb ON mb.branch_id = sw.branch_id \n"
				+ " INNER JOIN master_organization o ON mb.org_id = o.org_id \n"
				+ " LEFT JOIN (SELECT ROUND(IFNULL(SUM(sw.required_workforce), 0), 0) AS required, \n"
				+ " mb.branch_id AS branchId,mb.name AS branch,sw.req_skill_level_id AS reqLvl FROM sm_skill_level sl \n"
				+ " INNER JOIN sm_workstations sw ON sl.id = sw.req_skill_level_id \n"
				+ " INNER JOIN master_branch mb ON mb.branch_id = sw.branch_id \n"
				+ " INNER JOIN master_organization o ON mb.org_id = o.org_id \n"
				+ " WHERE o.org_id = :orgId GROUP BY mb.branch_id, sw.req_skill_level_id \n"
				+ ") req ON req.reqLvl = sl.id AND mb.branch_id = req.branchId \n" + "WHERE o.org_id = :orgId \n"
				+ "GROUP BY mb.branch_id, sm.skill_level_id ");

		TypedQuery<Tuple> query = session.createNativeQuery(builder.toString(), Tuple.class).setParameter("orgId",
				request.getOrgId());

		List<Tuple> objList = query.getResultList();
		LOGGER.info(" objList:{}", objList.size());
		if (!CollectionUtils.isEmpty(objList)) {
			for (Tuple obj : objList) {
				HashMap<String, Object> data = new HashMap<>();
				data.put("branchId", CommonUtils.objectToInt(obj.get("branchId")));
				data.put("requiredCount", CommonUtils.objectToDouble(obj.get("requiredCount")));
				data.put("actualCount", CommonUtils.objectToInt(obj.get("actualCount")));
				data.put("branch", CommonUtils.objectToString(obj.get("branch")));
				data.put("levelName", CommonUtils.objectToString(obj.get("levelName")));
				data.put("levelId", CommonUtils.objectToLong(obj.get("Lvl")));
				dataList.add(data);
			}
		}
		return dataList;
	}

	@Override
	public HashMap<String, Object> getBranchLvlAnalytics(SkillMatrixRequest request) {
		LOGGER.info(" In getBranchLvlAnalytics | getGraphDetails");
		Session session = getCurrentSession();
		HashMap<String, Object> data = new HashMap<>();

		List<HashMap<String, Object>> branchSMLevelGraph = getBranchSMLevelGraph(request, session);

		if (!CollectionUtils.isEmpty(branchSMLevelGraph)) {
			data.put("branchSMLevelGraph", branchSMLevelGraph);
		}
		return data;
	}

	private List<HashMap<String, Object>> getBranchSMLevelGraph(SkillMatrixRequest request, Session session) {
		List<HashMap<String, Object>> dataList = new ArrayList<>();

		StringBuilder builder = new StringBuilder(" select ifnull(count.req,0) as requiredCount,\r\n"
				+ " tbl.actualCount,tbl.branchId as branchId,tbl.branch ,tbl.deptName,tbl.lineName, \r\n"
				+ " tbl.lineId,tbl.reqLvl,tbl.deptId,tbl.workId,tbl.levelName,tbl.lid \r\n"
				+ " from (SELECT round(IFNULL(SUM(sw.required_workforce), 0),0) AS requiredCount, \r\n"
				+ " IFNULL(COUNT(sm.id), 0) AS actualCount,  mb.branch_id AS branchId, \r\n"
				+ " mb.name AS branch , d.dept_name AS deptName, ln.name AS lineName, \r\n"
				+ " ln.id AS lineId, sw.req_skill_level_id AS reqLvl, d.dept_id AS deptId, \r\n"
				+ " sw.id AS workId, sl.level_name AS levelName ,sl.id as lid FROM sm_skill_level sl \r\n"
				+ " INNER JOIN sm_emp_skill_matrix sm  ON sm.skill_level_id = sl.id \r\n"
				+ " INNER JOIN sm_workstations sw   ON sw.id = sm.workstations_id  \r\n"
				+ " INNER JOIN  dwm_line ln ON ln.id = sw.line_id\r\n"
				+ " INNER JOIN  master_department d  ON d.dept_id=ln.dept_id  \r\n"
				+ " INNER JOIN  master_branch mb  ON mb.branch_id = d.branch_id \r\n"
				+ " inner join master_organization o  ON mb.org_id = o.org_id \r\n"
				+ " where  sw.is_active=1 and  mb.branch_id =:branchId  AND o.org_id =:orgId "
				+ " and (ln.is_active <>'N' or ln.is_active is null) GROUP BY ln.id, sl.id) tbl \r\n"
				+ " left join (select sum(sr.required_workforce) as req,sr.req_skill_level_id as level,sr.line_id as line \r\n"
				+ " from sm_workstations sr\r\n"
				+ " INNER JOIN  dwm_line ln ON ln.id = sr.line_id\r\n"
				+ " where sr.branch_id=:branchId and sr.is_active=1 and (ln.is_active <>'N' or ln.is_active is null)\r\n"
				+ " group by sr.req_skill_level_id,sr.line_id ) count on count.level=tbl.lid and count.line=tbl.lineId ");

		TypedQuery<Tuple> query = session.createNativeQuery(builder.toString(), Tuple.class)
				.setParameter("orgId", request.getOrgId()).setParameter("branchId", request.getBranchId());

		List<Tuple> objList = query.getResultList();

		if (!CollectionUtils.isEmpty(objList)) {
			for (Tuple obj : objList) {
				HashMap<String, Object> data = new HashMap<>();
				data.put("branchId", CommonUtils.objectToInt(obj.get("branchId")));

				data.put("deptId", CommonUtils.objectToInt(obj.get("lineId")));
				data.put("deptName", CommonUtils.objectToString(obj.get("lineName")));
//				data.put("lineId", CommonUtils.objectToInt(obj.get("lineId")));
//				data.put("lineName", CommonUtils.objectToString(obj.get("lineName")));
				data.put("requiredCount", CommonUtils.objectToDouble(obj.get("requiredCount")));
				data.put("actualCount", CommonUtils.objectToInt(obj.get("actualCount")));
				data.put("branch", CommonUtils.objectToString(obj.get("branch")));
				data.put("levelName", CommonUtils.objectToString(obj.get("levelName")));
				data.put("levelId", CommonUtils.objectToLong(obj.get("reqLvl")));
				dataList.add(data);
			}
		}
		return dataList;
	}

	public HashMap<String, Object> getDeptLvlAnalytics(SkillMatrixRequest request) {
		LOGGER.info("# SMAnalyticsDaoImpl || getDeptLvlAnalytics");

		Session session = getCurrentSession();
		HashMap<String, Object> data = new HashMap<String, Object>();

		List<HashMap<String, Object>> deptSMLevelGraph = getDeptLvlGraph(request, session);

		if (!CollectionUtils.isEmpty(deptSMLevelGraph)) {
			data.put("deptSMLevelGraph", deptSMLevelGraph);
		}
		return data;
	}

	private List<HashMap<String, Object>> getDeptLvlGraph(SkillMatrixRequest request, Session session) {
		LOGGER.info("# SMAnalyticsDaoImpl || getDeptLvlGraph");
		List<HashMap<String, Object>> dataList = new ArrayList<>();

		StringBuilder sb = new StringBuilder("SELECT Round(IFNULL(SUM(sw.required_workforce), 0),0) AS requiredCount, "
				+ " IFNULL(actual, 0) AS actualCount,mb.branch_id AS branchId, "
				+ " mb.name AS branchName,d.dept_name AS deptName,sw.req_skill_level_id AS reqLvl,"
				+ " d.dept_id AS deptId,sw.id AS workstationId, sw.workstation AS workstationName,ln.id AS lineId,ln.name AS lineName "
				+ " FROM master_organization o " + " INNER JOIN master_branch mb ON mb.org_id = o.org_id "
				+ " INNER JOIN master_department d ON mb.branch_id = d.branch_id "
				+ " INNER JOIN dwm_line ln ON ln.dept_id = d.dept_id "
				+ " INNER JOIN sm_workstations sw ON  ln.id = sw.line_id "
				+ " LEFT JOIN sm_skill_level sl ON sl.id = sw.req_skill_level_id "
				+ " LEFT JOIN (SELECT IFNULL(COUNT(sm.id), 0) AS actual, "
				+ " mb.branch_id AS branchId, mb.name AS branch, sm.skill_level_id AS Lvl,"
				+ " sm.workstations_id AS workId,ln.id AS lineId FROM master_organization o "
				+ " INNER JOIN master_branch mb ON mb.org_id = o.org_id "
				+ " INNER JOIN master_department d ON mb.branch_id = d.branch_id "
				+ " INNER JOIN dwm_line ln ON ln.dept_id = d.dept_id "
				+ " INNER JOIN sm_workstations sw ON  ln.id = sw.line_id "
				+ " INNER JOIN sm_emp_skill_matrix sm ON sw.id = sm.workstations_id "
				+ " LEFT JOIN sm_skill_level sl ON sl.id = sm.skill_level_id WHERE "
				+ " mb.branch_id = :branchId AND ln.id IN (:lineIds) GROUP BY "
				+ " sm.workstations_id,sl.id,Lvl) req ON req.workId = sw.id AND req.Lvl = sl.id "
				+ " WHERE mb.branch_id = :branchId AND ln.id IN (:lineIds) GROUP BY sw.id ");

		TypedQuery<Tuple> query = session.createNativeQuery(sb.toString(), Tuple.class)
				.setParameter("branchId", request.getBranchId()).setParameter("lineIds", request.getLineIds());

//		if (request.getDeptIds() != null && !request.getDeptIds().isEmpty()) {
//			query.setParameter("deptIds", request.getDeptIds());
//		}

		List<Tuple> tupleList = query.getResultList();
		LOGGER.info("# tupleList size:{}", tupleList.size());

		if (!CollectionUtils.isEmpty(tupleList)) {
			for (Tuple tupleObj : tupleList) {
				HashMap<String, Object> obj = new HashMap<>();
				obj.put("actualCount", CommonUtils.objectToInt(tupleObj.get("actualCount")));
				obj.put("requiredCount", CommonUtils.objectToDouble(tupleObj.get("requiredCount")));
				obj.put("branchId", CommonUtils.objectToInt(tupleObj.get("branchId")));
				obj.put("deptId", CommonUtils.objectToInt(tupleObj.get("lineId")));
				obj.put("branchName", CommonUtils.objectToString(tupleObj.get("branchName")));
				obj.put("deptName", CommonUtils.objectToString(tupleObj.get("lineName")));
				obj.put("workstationId", CommonUtils.objectToInt(tupleObj.get("workstationId")));
				obj.put("workstationName", CommonUtils.objectToString(tupleObj.get("workstationName")));
				obj.put("reqLvl", CommonUtils.objectToInt(tupleObj.get("reqLvl")));

				dataList.add(obj);
			}
		}
		return dataList;
	}

	@Override
	public List<HashMap<String, Object>> getAvgTimeToCompleteReport(SkillMatrixRequest request) {
		LOGGER.info("# SMAnalyticsDaoImpl || getAvgTimeToCompleteReport");
		Session session = getCurrentSession();
		List<HashMap<String, Object>> dataList = new ArrayList<>();

		StringBuilder builder = new StringBuilder(
				" select round((datediff(ojtas.updated_date,(select sa.created_date from sm_ojt_skilling sk \r\n"
				+ "inner join sm_ojt_skilling_audit sa on sa.skilling_id = sk.id where \r\n"
				+ "sk.ojt_regis_id = ojtr.id order by sa.created_date asc limit 1) ))/count(*),2)+1 as avgTime\r\n"
				+ ",ojtas.updated_date,sl.level_name as levelName,sl.id as levelId,ojtr.branch_id as branchId,mb.name as branchName,d.dept_name as deptName\r\n"
				+ ",d.dept_id as deptId,ojtr.line_id as lineId,ln.name as lineName from sm_ojt_assessment ojtas \r\n"
				+ "inner join sm_ojt_regis ojtr on ojtr.id=ojtas.ojt_regis_id\r\n"
				+ "inner join sm_assessment sa on sa.id=ojtas.assessment_id and sa.assessment_type='LEVEL'\r\n"
				+ "left join sm_skill_level sl on ojtr.desired_skill_level_id=sl.id \r\n"
				+ "left join master_branch mb on mb.branch_id=ojtr.branch_id\r\n"
				+ "left join master_department d on ojtr.dept_id=d.dept_id\r\n"
				+ "left join dwm_line ln on ln.id=ojtr.line_id  where ojtas.assessment_status=:status \r\n"
				+ "and ojtas.created_date between :fromDt and :toDt and mb.org_id=:orgId ");

		if (!CollectionUtils.isEmpty(request.getLineIds())) {
			builder.append(" and ojtr.line_id in (:lineIds) ");
		}

		if (request.getBranchId() > 0) {
			builder.append(" and ojtr.branch_id=:branchId ");
		}
		if (request.getSkillLevelId() > 0) {
			builder.append(" and ojtr.desired_skill_level_id=:levelId ");
		}
		builder.append("group by ojtr.desired_skill_level_id, ojtr.line_id  ");

		if (request.getColName() != null && request.getOrderType() != null) {
			builder.append(" order by " + ColumnAscDescConstants.AvgTimeReportColName.get(request.getColName()) + " "
					+ request.getOrderType());
		} else {

			builder.append(" order by ojtas.id DESC ");
		}

		TypedQuery<Tuple> query = session.createNativeQuery(builder.toString(), Tuple.class)
				.setParameter("orgId", request.getOrgId()).setParameter("status", SMConstant.PASS)
				.setParameter("fromDt", request.getFromDt()).setParameter("toDt", request.getToDt());

		if (!CollectionUtils.isEmpty(request.getLineIds())) {
			query.setParameter("lineIds", request.getLineIds());
		}

		if (request.getSkillLevelId() > 0) {
			query.setParameter("levelId", request.getSkillLevelId());
		}

		if (request.getBranchId() > 0) {
			query.setParameter("branchId", request.getBranchId());
		}

		List<Tuple> objList = query.getResultList();
		LOGGER.info(" objList:{}", objList.size());
		if (!CollectionUtils.isEmpty(objList)) {
			for (Tuple obj : objList) {
				HashMap<String, Object> data = new HashMap<>();
				data.put("branchId", CommonUtils.objectToInt(obj.get("branchId")));
				data.put("avgTime", CommonUtils.objectToDouble(obj.get("avgTime")));
				data.put("branchName", CommonUtils.objectToString(obj.get("branchName")));
				data.put("deptId", CommonUtils.objectToInt(obj.get("deptId")));
				data.put("deptName", CommonUtils.objectToString(obj.get("deptName")));
				data.put("levelId", CommonUtils.objectToInt(obj.get("levelId")));
				data.put("levelName", CommonUtils.objectToString(obj.get("levelName")));
				data.put("lineId", CommonUtils.objectToLong(obj.get("lineId")));
				data.put("lineName", CommonUtils.objectToString(obj.get("lineName")));
				dataList.add(data);
			}
		}
		return dataList;
	}

	@Override
	public List<HashMap<String, Object>> getSkillGapCellwise(SkillMatrixRequest request) {
		LOGGER.info("# SMAnalyticsDaoImpl || getSkillGapCellwise");
		Session session = getCurrentSession();
		List<HashMap<String, Object>> dataList = new ArrayList<>();
		StringBuilder builder = new StringBuilder(
				"select ifnull(sum(sw.required_workforce),0) as requiredCount,ifnull(actual,0) as actualCount,mb.branch_id as branchId,mb.name as branch, d.dept_name as deptName, \r\n"
						+ "sw.req_skill_level_id as reqLvl,d.dept_id as deptId,\r\n"
						+ "sw.id as workId,sl.level_name as levelName ,\r\n"
						+ "case when ifnull(ifnull(sum(sw.required_workforce),0)-ifnull(actual,0),0)<0 then 0\r\n"
						+ "else ifnull(ifnull(sum(sw.required_workforce),0)-ifnull(actual,0),0) end as totalGap ,\r\n"
						+ "case when ifnull(ifnull(sum(sw.required_workforce),0)-ifnull(actual,0),0) <0 then 0\r\n"
						+ " else round(ifnull(ifnull(sum(sw.required_workforce),0)-ifnull(actual,0),0)/ifnull(sum(sw.required_workforce),0),2)* 100 end as avgCount,\r\n"
						+ " ln.id AS lineId,ln.name AS lineName from master_organization o \r\n"
						+ "inner join master_branch mb on mb.org_id=o.org_id \r\n"
						+ "inner join master_department d on mb.branch_id=d.branch_id \r\n"
						+ "inner join dwm_line ln ON ln.dept_id=d.dept_id "
						+ "inner join sm_workstations sw on ln.id=sw.line_id  \r\n"
						+ "right join sm_skill_level sl on sl.id=sw.req_skill_level_id\r\n"
						+ "left join (select ifnull(count(sm.id),0) as actual,mb.branch_id as branchId,mb.name as branch, sm.skill_level_id as Lvl, d.dept_id as deptId \r\n"
						+ "from master_organization o  inner join master_branch mb on mb.org_id=o.org_id \r\n"
						+ "inner join master_department d on mb.branch_id=d.branch_id \r\n"
						+ "inner join dwm_line ln ON ln.dept_id=d.dept_id "
						+ "inner join sm_workstations sw on ln.id=sw.line_id  \r\n"
						+ "inner join sm_emp_skill_matrix sm on  sw.id=sm.workstations_id   \r\n"
						+ "right join sm_skill_level sl on  sm.skill_level_id = sl.id\r\n"
						+ "where o.org_id=:orgId and sm.created_date between :fromDt and :toDt \r\n");

		StringBuilder qry = new StringBuilder(" group by ln.id,sl.id ) req "
				+ "	on req.deptId=d.dept_id and req.Lvl= sl.id where o.org_id=:orgId  ");
		if (request.getBranchId() > 0) {
			builder.append(" and mb.branch_id=:branchId ");
			qry.append(" and mb.branch_id=:branchId ");
		}
		if (request.getDeptId() > 0) {
			builder.append(" and d.dept_id=:deptId ");
			qry.append(" and d.dept_id=:deptId ");
		}

//		if (!CollectionUtils.isEmpty(request.getLineIds()) )  {
//			builder.append(" and ln.id in (:lineIds) ");
//			qry.append(" and ln.id in (:lineIds) ");
//		}

		if (request.getSkillLevelId() > 0) {
			builder.append(" and sl.id=:levelId  ");
			qry.append(" and sl.id=:levelId  ");
		}
		builder.append(qry);
		builder.append(" group by ln.id,sl.id ");

		if (request.getColName() != null && request.getOrderType() != null) {
			builder.append(" order by " + ColumnAscDescConstants.skillGapReportColName.get(request.getColName())
					+ request.getOrderType());
		} else {

			builder.append(" order by d.dept_name DESC ");
		}
		TypedQuery<Tuple> query = session.createNativeQuery(builder.toString(), Tuple.class)
				.setParameter("orgId", request.getOrgId()).setParameter("fromDt", request.getFromDt())
				.setParameter("toDt", request.getToDt());

		if (request.getBranchId() > 0) {
			query.setParameter("branchId", request.getBranchId());
		}
		if (request.getDeptId() > 0) {
			query.setParameter("deptId", request.getDeptId());
		}
		if (request.getSkillLevelId() > 0) {
			query.setParameter("levelId", request.getSkillLevelId());
		}
//		if (!CollectionUtils.isEmpty(request.getLineIds()) )  {
//			query.setParameter("lineIds", request.getLineIds());
//		}

		List<Tuple> objList = query.getResultList();
		LOGGER.info(" objList:{}", objList.size());
		if (!CollectionUtils.isEmpty(objList)) {
			for (Tuple obj : objList) {
				HashMap<String, Object> data = new HashMap<>();
				data.put("branchId", CommonUtils.objectToInt(obj.get("branchId")));
				data.put("gapAvg", CommonUtils.objectToDouble(obj.get("avgCount")));
				data.put("levelId", CommonUtils.objectToInt(obj.get("reqLvl")));
				data.put("deptId", CommonUtils.objectToInt(obj.get("deptId")));
				data.put("gapCount", CommonUtils.objectToDouble(obj.get("totalGap")));
				data.put("deptName", CommonUtils.objectToString(obj.get("deptName")));
				data.put("branchName", CommonUtils.objectToString(obj.get("branch")));
				data.put("levelName", CommonUtils.objectToString(obj.get("levelName")));
				data.put("lineId", CommonUtils.objectToLong(obj.get("lineId")));
				data.put("lineName", CommonUtils.objectToString(obj.get("lineName")));
				data.put("actualCount", CommonUtils.objectToDouble(obj.get("actualCount")));
				data.put("requiredCount", CommonUtils.objectToDouble(obj.get("requiredCount")));

				dataList.add(data);
			}
		}
		return dataList;
	}

	@Override
	public List<HashMap<String, Object>> getOjtPlanAndActualReport(SkillMatrixRequest request) {
		LOGGER.info("# SMAnalyticsDaoImpl || getOjtPlanAndActualReport");
		Session session = getCurrentSession();
		List<HashMap<String, Object>> dataList = new ArrayList<>();

		StringBuilder sb = new StringBuilder(
				"SELECT sor.branch_id AS branchId,mb.name AS branchName,COUNT(sor.id) AS totalOjtCount,"
						+ " sor.dept_id AS deptId,md.dept_name AS deptName,sor.line_id AS lineId,"
						+ " ln.name AS lineName,sor.desired_skill_level_id AS levelId,smsl.level_name AS levelName,"
						+ " COUNT(CASE WHEN soa.assessment_status = 'PASS' THEN soa.id ELSE NULL END) AS OjtCompleteCount,"
						+ " ROUND(COALESCE((SUM(CASE WHEN soa.assessment_status = 'PASS' THEN 1 ELSE 0 END) / NULLIF(COUNT(sor.id), 0) * 100),0),2)"
						+ " AS OjtCompletePercentage FROM sm_ojt_regis sor"
						+ " LEFT JOIN master_branch mb ON sor.branch_id = mb.branch_id LEFT JOIN master_department md ON md.dept_id = sor.dept_id"
						+ " LEFT JOIN dwm_line ln ON ln.id = sor.line_id LEFT JOIN sm_ojt_assessment soa ON sor.id = soa.ojt_regis_id"
						+ " LEFT JOIN sm_skill_level smsl ON smsl.id = sor.desired_skill_level_id WHERE mb.org_id = :orgId AND sor.branch_id = :branchId"
						+ " AND soa.created_date BETWEEN :fromDt AND :toDt ");

		if (!CollectionUtils.isEmpty(request.getLineIds())) {
			sb.append(" and sor.line_id in (:lineIds) ");
		}
		if (request.getSkillLevelId() > 0) {
			sb.append(" and sor.desired_skill_level_id=:skillLevelId ");
		}
		sb.append(" GROUP BY sor.line_id,sor.desired_skill_level_id ");

		if (request.getColName() != null && request.getOrderType() != null) {
			sb.append(" order by " + ColumnAscDescConstants.PlanAndActualReportColName.get(request.getColName()) + " "
					+ request.getOrderType());
		} else {

			sb.append(" order by sor.id ASC ");
		}

		TypedQuery<Tuple> query = session.createNativeQuery(sb.toString(), Tuple.class)
				.setParameter("orgId", request.getOrgId()).setParameter("branchId", request.getBranchId())
				.setParameter("fromDt", request.getFromDt()).setParameter("toDt", request.getToDt());

		if (!CollectionUtils.isEmpty(request.getLineIds())) {
			query.setParameter("lineIds", request.getLineIds());
		}

		if (request.getSkillLevelId() > 0) {
			query.setParameter("skillLevelId", request.getSkillLevelId());
		}
		List<Tuple> objList = query.getResultList();

		if (!CollectionUtils.isEmpty(objList)) {
			for (Tuple obj : objList) {
				HashMap<String, Object> data = new HashMap<>();
				data.put("totalPlan", CommonUtils.objectToInt(obj.get("totalOjtCount")));
				data.put("actualCompletion", CommonUtils.objectToInt(obj.get("OjtCompleteCount")));
				data.put("completionPercentage", CommonUtils.objectToDouble(obj.get("OjtCompletePercentage")));
				data.put("levelId", CommonUtils.objectToInt(obj.get("levelId")));
				data.put("levelName", CommonUtils.objectToString(obj.get("levelName")));
				data.put("deptId", CommonUtils.objectToInt(obj.get("deptId")));
				data.put("deptName", CommonUtils.objectToString(obj.get("deptName")));
				data.put("branchId", CommonUtils.objectToInt(obj.get("branchId")));
				data.put("branchName", CommonUtils.objectToString(obj.get("branchName")));
				data.put("lineId", CommonUtils.objectToLong(obj.get("lineId")));
				data.put("lineName", CommonUtils.objectToString(obj.get("lineName")));

				dataList.add(data);
			}
		}

		return dataList;
	}

	public List<HashMap<String, Object>> getEmployeeWisePlanReport(SkillMatrixRequest request) {
		LOGGER.info("# SMAnalyticsDaoImpl || getEmployeeWisePlanReport");
		Session session = getCurrentSession();
		List<HashMap<String, Object>> dataList = new ArrayList<>();

		StringBuilder sb = new StringBuilder(
				"select data.deptName as deptName,data.lineName AS lineName,data.lineId AS lineId,"
						+ " data.levelId AS levelId,data.levelName AS levelName,data.branchName AS branchName,data.branchId AS branchId,data.deptId AS deptId,"
						+ " COALESCE(SUM( CASE WHEN data.registration>0  THEN 1 ELSE 0 END),0) AS plan,\r\n"
						+ " COALESCE(SUM( CASE WHEN data.skilling>0  THEN 1 ELSE 0 END),0) AS actual,\r\n"
						+ " COALESCE(SUM( CASE WHEN data.status ='PASS'  THEN 1 ELSE 0 END),0) AS passCount,\r\n"
						+ " COALESCE(SUM( CASE WHEN data.status ='FAIL'  THEN 1 ELSE 0 END),0) AS failCount\r\n"
						+ " from (select regi.id as registration,empsm.id as skilling,ojtAsses.assessment_status as status,"
						+ " regi.branch_id as branchId,b.name as branchName,md.dept_name as deptName,regi.dept_id as deptId,"
						+ " ln.name AS lineName,regi.line_id AS lineId,regi.desired_skill_level_id AS levelId,"
						+ " sl.level_name AS levelName from sm_ojt_regis regi \r\n"
						+ " left join sm_emp_skill_matrix empsm on regi.emp_id=empsm.emp_id and regi.workstation_id=empsm.workstations_id\r\n"
						+ " left join sm_ojt_assessment ojtAsses on ojtAsses.ojt_regis_id=regi.id\r\n"
						+ " left join master_branch b on b.branch_id=regi.branch_id"
						+ " left join master_department md on md.dept_id=regi.dept_id\r\n"
						+ " left JOIN dwm_line ln ON ln.id=regi.line_id "
						+ " left join sm_skill_level sl on regi.desired_skill_level_id=sl.id "
						+ " left join sm_assessment sa on sa.id=ojtAsses.assessment_id   \r\n"
						+ " where regi.created_date BETWEEN :fromDt AND :toDt and sa.assessment_type !='SAFETY' \r\n" + " and regi.branch_id=:branchId  ");

		if (!CollectionUtils.isEmpty(request.getLineIds())) {
			sb.append(" and regi.line_id IN (:lineIds) ");
		}

		if (request.getSkillLevelId() > 0) {
			sb.append(" and regi.desired_skill_level_id=:levelId ");
		}

		sb.append(" group by regi.emp_id,regi.workstation_id) as data ");

		if (request.getColName() != null && request.getOrderType() != null) {
			sb.append(" order by " + ColumnAscDescConstants.EmployeePlanColHeader.get(request.getColName()) + " "
					+ request.getOrderType());
		} else {
			sb.append(" order by actual DESC ");
		}

		TypedQuery<Tuple> query = session.createNativeQuery(sb.toString(), Tuple.class)
				.setParameter("branchId", request.getBranchId()).setParameter("fromDt", request.getFromDt())
				.setParameter("toDt", request.getToDt());

		if (!CollectionUtils.isEmpty(request.getLineIds())) {
			query.setParameter("lineIds", request.getLineIds());
		}

		if (request.getSkillLevelId() > 0) {
			query.setParameter("levelId", request.getSkillLevelId());
		}

		List<Tuple> tupleList = query.getResultList();
		LOGGER.info("# tupleList size: {}", tupleList.size());

		if (!CollectionUtils.isEmpty(tupleList)) {
			for (Tuple obj : tupleList) {
				HashMap<String, Object> mapObj = new HashMap<>();
				mapObj.put("deptName", CommonUtils.objectToString(obj.get("deptName")));
				mapObj.put("lineName", CommonUtils.objectToString(obj.get("lineName")));
				mapObj.put("lineId", CommonUtils.objectToLong(obj.get("lineId")));
				mapObj.put("planCount", CommonUtils.objectToInt(obj.get("plan")));
				mapObj.put("actulCount", CommonUtils.objectToInt(obj.get("actual")));
				mapObj.put("passCount", CommonUtils.objectToInt(obj.get("passCount")));
				mapObj.put("failCount", CommonUtils.objectToString(obj.get("failCount")));
				mapObj.put("levelId", CommonUtils.objectToInt(obj.get("levelId")));
				mapObj.put("levelName", CommonUtils.objectToString(obj.get("levelName")));
				mapObj.put("branchName", CommonUtils.objectToString(obj.get("branchName")));
				mapObj.put("branchId", CommonUtils.objectToInt(obj.get("branchId")));
				mapObj.put("deptId", CommonUtils.objectToInt(obj.get("deptId")));

				dataList.add(mapObj);

			}
		}
		return dataList;
	}

	@Override
	public List<HashMap<String, Object>> getSMCellWiseMonthWiseReport(SkillMatrixRequest request) { // lineId
		LOGGER.info("# SMAnalyticsDaoImpl || getSMCellWiseMonthWiseReport");
		Session session = getCurrentSession();
		List<HashMap<String, Object>> dataList = new ArrayList<>();

		StringBuilder sb = new StringBuilder(
				" select md.dept_name as deptName,r.dept_id as deptId,b.name AS branchName,r.branch_id as branchId,slvl.level_name as levelName,ws.workstation workstationName,\r\n"
						+ " ln.name AS lineName, r.line_id AS lineId,r.desired_skill_level_id AS levelId, "
						+ " count(empsm.id) as actualCount,DATE_FORMAT(r.created_date, '%b %Y') AS monthYear,\r\n"
						+ " count(r.id) as planCount,round(((count(empsm.id)/ count(r.id))*100),2) as percentage from sm_ojt_regis r \r\n"
						+ " left join  sm_emp_skill_matrix empsm on empsm.workstations_id=r.workstation_id and r.emp_id=empsm.emp_id \r\n"
						+ " left join sm_skill_level slvl on slvl.id=r.desired_skill_level_id\r\n"
						+ " inner join sm_workstations ws on ws.id=r.workstation_id\r\n"
						+ " left join master_department md on md.dept_id=r.dept_id"
						+ " left join master_branch b on b.branch_id=r.branch_id"
						+ " left JOIN dwm_line ln ON ln.id=r.line_id "
						+ " where r.created_date BETWEEN :fromDt AND :toDt and r.branch_id=:branchId ");

//		if (!CollectionUtils.isEmpty(request.getDeptIds())) {
//			sb.append(" and r.dept_id IN (:deptIds) ");
//		}

		if (!CollectionUtils.isEmpty(request.getLineIds())) {
			sb.append(" and r.line_id in (:lineIds) ");
		}
		if (request.getSkillLevelId() > 0) {
			sb.append(" and r.desired_skill_level_id=:levelId ");
		}

		sb.append(" group by r.line_id ,DATE_FORMAT(r.created_date, '%b %Y') ");

		if (request.getColName() != null && request.getOrderType() != null) {
			sb.append(" order by " + ColumnAscDescConstants.CellWiseReportColHeader.get(request.getColName()) + " "
					+ request.getOrderType());
		} else {
			sb.append(" order by actualCount DESC ");
		}

		TypedQuery<Tuple> query = session.createNativeQuery(sb.toString(), Tuple.class)
				.setParameter("branchId", request.getBranchId()).setParameter("fromDt", request.getFromDt())
				.setParameter("toDt", request.getToDt());

		if (!CollectionUtils.isEmpty(request.getLineIds())) {
			query.setParameter("lineIds", request.getLineIds());
		}

		if (request.getSkillLevelId() > 0) {
			query.setParameter("levelId", request.getSkillLevelId());
		}

//		if (!CollectionUtils.isEmpty(request.getDeptIds())) {
//			query.setParameter("deptIds", request.getDeptIds());
//		}

		List<Tuple> tupleList = query.getResultList();
		LOGGER.info("# tupleList size: {}", tupleList.size());

		if (!CollectionUtils.isEmpty(tupleList)) {
			for (Tuple obj : tupleList) {
				HashMap<String, Object> mapObj = new HashMap<>();
				mapObj.put("deptName", CommonUtils.objectToString(obj.get("deptName")));
				mapObj.put("lineName", CommonUtils.objectToString(obj.get("lineName")));
				mapObj.put("lineId", CommonUtils.objectToLong(obj.get("lineId")));
				mapObj.put("levelName", CommonUtils.objectToString(obj.get("levelName")));
				mapObj.put("workstationName", CommonUtils.objectToString(obj.get("workstationName")));
				mapObj.put("actualCount", CommonUtils.objectToInt(obj.get("actualCount")));
				mapObj.put("planCount", CommonUtils.objectToInt(obj.get("planCount")));
				mapObj.put("percentage", CommonUtils.objectToDouble(obj.get("percentage")));
				mapObj.put("monthYear", CommonUtils.objectToString(obj.get("monthYear")));
				mapObj.put("levelId", CommonUtils.objectToInt(obj.get("levelId")));
				mapObj.put("branchName", CommonUtils.objectToString(obj.get("branchName")));
				mapObj.put("branchId", CommonUtils.objectToInt(obj.get("branchId")));
				mapObj.put("deptId", CommonUtils.objectToInt(obj.get("deptId")));

				dataList.add(mapObj);
			}
		}
		return dataList;
	}

	@Override
	public List<HashMap<String, Object>> ojtPlanBranchWiseReport(SkillMatrixRequest request) {
		LOGGER.info("# SMAnalyticsDaoImpl || ojtPlanBranchWiseReport");
		Session session = getCurrentSession();
		List<HashMap<String, Object>> dataList = new ArrayList<>();

		StringBuilder sb = new StringBuilder(
				"SELECT b.name AS branchName,r.branch_id as branchId, r.dept_id AS deptId, d.dept_name AS deptName,ln.name AS lineName, "
						+ " r.line_id AS lineId,sl.level_name AS levelName,r.current_skill_level_id AS levelId, "
						+ " COALESCE(SUM(CASE WHEN r.id > 0 THEN 1 ELSE 0 END), 0) AS plan, "
						+ " COALESCE(SUM(CASE WHEN r.status = 'PENDING' THEN 1 ELSE 0 END), 0) as pendingCount, \r\n"
						+ "	COALESCE(SUM(CASE WHEN r.status = 'COMPLETED' THEN 1 ELSE 0 END), 0) as completeCount,"
						+ " ROUND(COALESCE(SUM(CASE WHEN r.status = 'PENDING' THEN 1 ELSE 0 END), 0) / COUNT(r.id) * 100,2) AS pendingPercentage, "
						+ " ROUND(COALESCE(SUM(CASE WHEN r.status = 'COMPLETED' THEN 1 ELSE 0 END), 0) / COUNT(r.id) * 100,2) AS completePercentage "
						+ " FROM sm_ojt_regis r " + " INNER JOIN master_branch b ON b.branch_id = r.branch_id "
						+ " LEFT JOIN master_organization o ON o.org_id = b.org_id "
						+ " LEFT JOIN master_department d ON d.dept_id = r.dept_id "
						+ " left JOIN dwm_line ln ON ln.id=r.line_id "
						+ " LEFT JOIN sm_skill_level sl ON sl.id = r.desired_skill_level_id "
						+ " WHERE o.org_id = :orgId AND r.created_date BETWEEN :fromDt AND :toDt ");

		if (request.getBranchId() > 0) {
			sb.append(" AND r.branch_id = :branchId ");
		}

		if (!CollectionUtils.isEmpty(request.getLineIds())) {
			sb.append(" and r.line_id in (:lineIds) ");
		}

		if (request.getSkillLevelId() > 0) {
			sb.append(" and r.desired_skill_level_id=:skillLevelId ");
		}

//		sb.append(" GROUP BY deptId, levelName ");

		sb.append(" GROUP BY r.line_id,r.desired_skill_level_id ");

		if (request.getColName() != null && request.getOrderType() != null) {
			sb.append(" order by " + ColumnAscDescConstants.OJTPlanBranchColHeader.get(request.getColName()) + " "
					+ request.getOrderType());
		} else {
			sb.append(" order by completeCount DESC ");
		}

		TypedQuery<Tuple> query = session.createNativeQuery(sb.toString(), Tuple.class)
				.setParameter("orgId", request.getBranchId()).setParameter("fromDt", request.getFromDt())
				.setParameter("toDt", request.getToDt());

		if (request.getBranchId() > 0) {
			query.setParameter("branchId", request.getBranchId());
		}

		if (!CollectionUtils.isEmpty(request.getLineIds())) {
			query.setParameter("lineIds", request.getLineIds());
		}

		if (request.getSkillLevelId() > 0) {
			query.setParameter("skillLevelId", request.getSkillLevelId());
		}

		List<Tuple> objList = query.getResultList();

		for (Tuple obj : objList) {
			HashMap<String, Object> hashMapObj = new HashMap<>();
			hashMapObj.put("branchName", CommonUtils.objectToString(obj.get("branchName")));
			hashMapObj.put("deptId", CommonUtils.objectToInt(obj.get("deptId")));
			hashMapObj.put("deptName", CommonUtils.objectToString(obj.get("deptName")));
			hashMapObj.put("lineId", CommonUtils.objectToLong(obj.get("lineId")));
			hashMapObj.put("lineName", CommonUtils.objectToString(obj.get("lineName")));
			hashMapObj.put("levelName", CommonUtils.objectToString(obj.get("levelName")));
			hashMapObj.put("plan", CommonUtils.objectToInt(obj.get("plan")));
			hashMapObj.put("pendingCount", CommonUtils.objectToInt(obj.get("pendingCount")));
			hashMapObj.put("completeCount", CommonUtils.objectToInt(obj.get("completeCount")));
			hashMapObj.put("pendingPercentage", CommonUtils.objectToDouble(obj.get("pendingPercentage")));
			hashMapObj.put("completePercentage", CommonUtils.objectToDouble(obj.get("completePercentage")));
			hashMapObj.put("levelId", CommonUtils.objectToInt(obj.get("levelId")));
			hashMapObj.put("branchId", CommonUtils.objectToInt(obj.get("branchId")));

			dataList.add(hashMapObj);
		}

		return dataList;
	}

	@Override
	public List<HashMap<String, Object>> getCellWiseMultiskillingReport(SkillMatrixRequest request) {
		LOGGER.info("# SMAnalyticsDaoImpl || getCellWiseMultiskillingReport");
		Session session = getCurrentSession();
		List<HashMap<String, Object>> dataList = new ArrayList<>();
		String lineIds = "";
		String lineQury = "";
		StringBuilder sb = new StringBuilder(
				"select ifnull(empCount.totalEmp,0) as totalEmployees  ,ifnull(skilling.skilled,0) as  employeeCount,\r\n"
				+ "ifnull(round((skilling.skilled/empCount.totalEmp)*100,2),0) as employeeCountPerc ,\r\n"
				+ "empCount.branchName as branchName, empCount.branchId as branchId,empCount.deptName as deptName,empCount.deptId as deptId,	\r\n"
				+ "empCount.lineId as lineId, empCount.lineName as lineName\r\n"
				+ "from (SELECT coalesce(count(ed.emp_id),0) as totalEmp,ed.line_id as lineId ,ln.name as lineName,\r\n"
				+ "d.dept_name as deptName,d.dept_id as deptId,mb.name as branchName,mb.branch_id as branchId\r\n"
				+ "FROM tbl_employee_details ed \r\n"
				+ "INNER JOIN master_organization mo on mo.org_id=ed.org_id\r\n"
				+ "INNER JOIN master_branch mb on mb.branch_id=ed.branch_id\r\n"
				+ "INNER JOIN master_department d ON ed.dept_id = d.dept_id\r\n"
				+ "INNER JOIN dwm_line ln ON ln.id = ed.line_id where ed.is_deactive=0 and ed.branch_id=:branchId and ed.org_id=:orgId group by ed.line_id) empCount\r\n"
				+ "left join (\r\n"
				+ "select count(*) as skilled,lineskill.lineId from (select count(*) as count ,uniqempWorkst.empId,uniqempWorkst.lineId as lineId \r\n"
				+ "from (select skill.emp_id as empId,skill.workstations_id,ed.line_id as lineId from sm_emp_skill_matrix skill \r\n"
				+ "inner join tbl_employee_details ed on ed.emp_id=skill.emp_id\r\n"
				+ " where ed.branch_id=:branchId and ed.org_id=:orgId AND skill.created_date BETWEEN :startDate AND :endDate and ed.line_id is not null and skill.skill_level_id is not null \r\n"
				+ " and skill.skill_level_id>0 group by skill.emp_id,skill.workstations_id) as uniqempWorkst group by uniqempWorkst.empId having count(*)>1) as lineskill\r\n"
				+ " group by lineskill.lineId) as skilling\r\n"
				+ " on skilling.lineId=empCount.lineId");

//{lineId}
//		if (!CollectionUtils.isEmpty(request.getLineIds())) {
//			lineIds = " AND ln.id IN (:lineIds) ";
//			lineQury = " AND lineCounts.lineId IN (:lineIds) ";
//		}

		if (request.getColName() != null && request.getOrderType() != null) {
			sb.append(" order by " + ColumnAscDescConstants.CellMultiReportColName.get(request.getColName()) + " "
					+ request.getOrderType());
		} else {

			sb.append("  order by lineId DESC ");
		}
		String qry = sb.toString().replace("{lineId}", lineIds).replace("{lineQury}", lineQury);
		TypedQuery<Tuple> query = session.createNativeQuery(qry, Tuple.class)

				.setParameter("orgId", request.getOrgId()).setParameter("branchId", request.getBranchId())
				.setParameter("startDate", request.getFromDt()).setParameter("endDate", request.getToDt());
//		if (!CollectionUtils.isEmpty(request.getLineIds())) {
//			query.setParameter("lineIds", request.getLineIds());
//		}
		List<Tuple> objList = query.getResultList();

		if (!CollectionUtils.isEmpty(objList)) {
			for (Tuple obj : objList) {
				HashMap<String, Object> list = new HashMap<>();
				list.put("EmpMultiskillingCount", CommonUtils.objectToInt(obj.get("employeeCount")));
				list.put("TotalEmp", CommonUtils.objectToInt(obj.get("totalEmployees")));
				list.put("MultiskillingPercentage", CommonUtils.objectToDouble(obj.get("employeeCountPerc")));
				list.put("deptName", CommonUtils.objectToString(obj.get("deptName")));
				list.put("deptId", CommonUtils.objectToInt(obj.get("deptId")));
				list.put("branchName", CommonUtils.objectToString(obj.get("branchName")));
				list.put("branchId", CommonUtils.objectToInt(obj.get("branchId")));
				list.put("lineName", CommonUtils.objectToString(obj.get("lineName")));
				list.put("lineId", CommonUtils.objectToLong(obj.get("lineId")));

				dataList.add(list);

			}
		}

		return dataList;
	}

	@Override
	public List<HashMap<String, Object>> getPlantWiseMultiskillingReport(SkillMatrixRequest request) {
		LOGGER.info("# SMAnalyticsDaoImpl || getPlantWiseMultiskillingReport");
		Session session = getCurrentSession();
		List<HashMap<String, Object>> dataList = new ArrayList<>();

		StringBuilder sb = new StringBuilder("select ifnull(empCount.totalEmp,0) as totalEmployees  ,ifnull(skilling.skilled,0) as  employeeCount,\r\n"
				+ "ifnull(round((skilling.skilled/empCount.totalEmp)*100,2),0) as employeeCountPerc ,\r\n"
				+ "empCount.branchName as branchName, empCount.branchId as branchId\r\n"
				+ "from (SELECT coalesce(count(ed.emp_id),0) as totalEmp,mb.name as branchName,mb.branch_id as branchId\r\n"
				+ "FROM tbl_employee_details ed \r\n"
				+ "INNER JOIN master_organization mo on mo.org_id=ed.org_id\r\n"
				+ "INNER JOIN master_branch mb on mb.branch_id=ed.branch_id\r\n"
				+ "where ed.is_deactive=0 and ed.emp_type='EMPLOYEE' \r\n"
				+ "and ed.org_id=:orgId group by ed.branch_id) empCount\r\n"
				+ "left join (\r\n"
				+ "select count(*) as skilled,lineskill.branchId from (select count(*) as count ,uniqempWorkst.empId,uniqempWorkst.branchId as branchId \r\n"
				+ "from (select skill.emp_id as empId,skill.workstations_id,ed.branch_id as branchId from sm_emp_skill_matrix skill \r\n"
				+ "inner join tbl_employee_details ed on ed.emp_id=skill.emp_id\r\n"
				+ " where ed.org_id=:orgId and skill.created_date BETWEEN :startDate AND :endDate and ed.branch_id is not null and skill.skill_level_id is not null \r\n"
				+ " and skill.skill_level_id>0 group by skill.emp_id,skill.workstations_id) as uniqempWorkst group by uniqempWorkst.empId having count(*)>1) as lineskill\r\n"
				+ " group by lineskill.branchId) as skilling \r\n"
				+ " on skilling.branchId=empCount.branchId ");

		if (request.getColName() != null && request.getOrderType() != null) {
			sb.append(" order by " + ColumnAscDescConstants.PlantMultiReportColName.get(request.getColName())
					+ request.getOrderType());
		} else {

			sb.append(" order by branchId DESC ");
		}

		TypedQuery<Tuple> query = session.createNativeQuery(sb.toString(), Tuple.class)

				.setParameter("orgId", request.getOrgId()).setParameter("startDate", request.getFromDt())
				.setParameter("endDate", request.getToDt());

		List<Tuple> objList = query.getResultList();

		if (!CollectionUtils.isEmpty(objList)) {
			for (Tuple obj : objList) {
				HashMap<String, Object> list = new HashMap<>();
				list.put("EmpMultiskillingCount", CommonUtils.objectToInt(obj.get("employeeCount")));
				list.put("TotalEmp", CommonUtils.objectToInt(obj.get("totalEmployees")));
				list.put("MultiskillingPercentage", CommonUtils.objectToDouble(obj.get("employeeCountPerc")));
				list.put("branchName", CommonUtils.objectToString(obj.get("branchName")));
				list.put("branchId", CommonUtils.objectToInt(obj.get("branchId")));
				dataList.add(list);

			}
		}

		return dataList;
	}

	@Override
	public List<HashMap<String, Object>> ojtPlanDeptWiseReport(SkillMatrixRequest request) {
		LOGGER.info("# SMAnalyticsDaoImpl || ojtPlanDeptWiseReport");
		Session session = getCurrentSession();
		List<HashMap<String, Object>> dataList = new ArrayList<>();

		StringBuilder sb = new StringBuilder(
				"SELECT  r.dept_id as deptId,d.dept_name AS deptName,r.workstation_id as workstationId, w.workstation as workstation,\r\n"
						+ " sl.level_name AS levelName,sl.id AS skillLvlId,r.branch_id as branchId,b.name as branchName,r.line_id AS lineId,ln.name AS lineName ,\r\n"
						+ " COALESCE(SUM(CASE WHEN r.id > 0 THEN 1 ELSE 0 END), 0) AS plan,\r\n"
						+ " COALESCE(SUM(CASE WHEN r.status = 'PENDING' THEN 1 ELSE 0 END), 0) as pendingCount,\r\n"
						+ "	COALESCE(SUM(CASE WHEN r.status = 'COMPLETED' THEN 1 ELSE 0 END), 0) as completeCount,"
						+ " ROUND(COALESCE(SUM(CASE WHEN r.status = 'PENDING' THEN 1 ELSE 0 END), 0) / COUNT(r.id) * 100, 2) AS pendingPercentage,\r\n"
						+ " ROUND(COALESCE(SUM(CASE WHEN r.status = 'COMPLETED' THEN 1 ELSE 0 END), 0) / COUNT(r.id) * 100, 2) AS completePercentage\r\n"
						+ " FROM sm_ojt_regis r \r\n" + "INNER JOIN master_branch b ON b.branch_id = r.branch_id\r\n"
						+ " left JOIN master_organization o ON o.org_id = b.org_id\r\n"
						+ " left JOIN master_department d ON d.dept_id = r.dept_id\r\n"
						+ " left JOIN dwm_line ln ON ln.id=r.line_id "
						+ " left JOIN sm_skill_level sl ON sl.id = r.desired_skill_level_id \r\n"
						+ " left join sm_workstations w on w.id =r.workstation_id\r\n"
						+ " WHERE o.org_id =:orgId  AND r.created_date BETWEEN :fromDt AND :toDt \r\n");

		if (request.getBranchId() > 0) {
			sb.append(" AND r.branch_id =:branchId ");
		}
//		if (request.getDeptId() > 0) {
//			sb.append(" AND r.dept_id=:deptId ");
//		}

		if (!CollectionUtils.isEmpty(request.getLineIds())) {
			sb.append(" AND r.line_id IN (:lineIds) ");
		}

		if (request.getSkillLevelId() > 0) {
			sb.append(" and r.desired_skill_level_id=:skillLevelId ");
		}

		sb.append(" GROUP BY workstationId, r.line_id ");

		if (request.getColName() != null && request.getOrderType() != null) {
			sb.append(" order by " + ColumnAscDescConstants.OJTPlanDeptColHeader.get(request.getColName()) + " "
					+ request.getOrderType());
		} else {
			sb.append(" order by completeCount DESC ");
		}

		TypedQuery<Tuple> query = session.createNativeQuery(sb.toString(), Tuple.class)
				.setParameter("orgId", request.getBranchId()).setParameter("fromDt", request.getFromDt())
				.setParameter("toDt", request.getToDt());

		if (request.getBranchId() > 0) {
			query.setParameter("branchId", request.getBranchId());
		}

//		if (request.getDeptId() > 0) {
//			query.setParameter("deptId", request.getDeptId());
//		}
		if (!CollectionUtils.isEmpty(request.getLineIds())) {
			query.setParameter("lineIds", request.getLineIds());
		}
		if (request.getSkillLevelId() > 0) {
			query.setParameter("skillLevelId", request.getSkillLevelId());
		}

		List<Tuple> objList = query.getResultList();

		if (!CollectionUtils.isEmpty(objList)) {
			for (Tuple obj : objList) {

				HashMap<String, Object> hashMapObj = new HashMap<String, Object>();
				hashMapObj.put("workstationId", CommonUtils.objectToInt(obj.get("workstationId")));
				hashMapObj.put("workstation", CommonUtils.objectToString(obj.get("workstation")));
				hashMapObj.put("deptName", CommonUtils.objectToString(obj.get("deptName")));
				hashMapObj.put("lineName", CommonUtils.objectToString(obj.get("lineName")));
				hashMapObj.put("lineId", CommonUtils.objectToLong(obj.get("lineId")));
				hashMapObj.put("branchName", CommonUtils.objectToString(obj.get("branchName")));
				hashMapObj.put("levelName", CommonUtils.objectToString(obj.get("levelName")));
				hashMapObj.put("plan", CommonUtils.objectToInt(obj.get("plan")));
				hashMapObj.put("pendingCount", CommonUtils.objectToInt(obj.get("pendingCount")));
				hashMapObj.put("completeCount", CommonUtils.objectToInt(obj.get("completeCount")));
				hashMapObj.put("pendingPercentage", CommonUtils.objectToDouble(obj.get("pendingPercentage")));
				hashMapObj.put("completePercentage", CommonUtils.objectToDouble(obj.get("completePercentage")));
				hashMapObj.put("levelId", CommonUtils.objectToLong(obj.get("skillLvlId")));
				hashMapObj.put("deptId", CommonUtils.objectToInt(obj.get("deptId")));
				hashMapObj.put("branchId", CommonUtils.objectToInt(obj.get("branchId")));

				dataList.add(hashMapObj);
			}
		}
		return dataList;
	}

	@Override
	public List<HashMap<String, Object>> getAssesmentReport(SkillMatrixRequest request) {
		LOGGER.info("# SMAnalyticsDaoImpl || getAssesmentReport");
		Session session = getCurrentSession();
		List<HashMap<String, Object>> dataList = new ArrayList<>();

		StringBuilder sb = new StringBuilder(
				"SELECT smsl.level_name AS levelName, sor.desired_skill_level_id as levelId,sor.line_id AS lineId,ln.name AS lineName,"
						+ " COALESCE(SUM(CASE WHEN soa.assessment_status in('FAIL','PASS') THEN 1 ELSE 0 END),0)AS totalCount,"
						+ " COALESCE(SUM(CASE WHEN soa.assessment_status = 'PASS' THEN 1 ELSE 0 END),0) AS passCount,"
						+ " IFNULL(ROUND(COALESCE(SUM(CASE WHEN soa.assessment_status = 'PASS' THEN 1 ELSE 0 END), 0) /"
						+ " COALESCE(SUM(CASE WHEN soa.assessment_status in('FAIL','PASS') THEN 1 ELSE 0 END),0) * 100,2),0) AS passPercentage,"
						+ " COALESCE(SUM(CASE WHEN soa.assessment_status = 'FAIL' THEN 1 ELSE 0 END),0)AS failCount,"
						+ " IFNULL(ROUND(COALESCE(SUM(CASE WHEN soa.assessment_status = 'FAIL' THEN 1 ELSE 0 END), 0) /"
						+ " COALESCE(SUM(CASE WHEN soa.assessment_status in('FAIL','PASS') THEN 1 ELSE 0 END), 0) * 100,2),0) AS failPercentage,"
						+ " sor.dept_id AS deptId, md.dept_name AS deptName,sor.branch_id AS branchId,mb.name AS branchName FROM sm_ojt_assessment soa"
						+ " INNER JOIN sm_ojt_regis sor ON soa.ojt_regis_id = sor.id"
						+ " INNER JOIN sm_skill_level smsl ON smsl.id = sor.desired_skill_level_id"
						+ " INNER JOIN master_branch mb ON sor.branch_id = mb.branch_id"
						+ " INNER JOIN master_department md ON md.dept_id = sor.dept_id"
						+ " INNER JOIN dwm_line ln ON ln.id=sor.line_id"
						+ " INNER JOIN sm_workstations smw ON smw.id =  sor.workstation_id"
						+ " LEFT join sm_assessment sa on sa.id=soa.assessment_id  "
						+ " WHERE mb.org_id =:orgId AND sor.branch_id =:branchId"
						+ " AND soa.created_date BETWEEN :fromDt AND :toDt and sa.assessment_type !='SAFETY' ");

		if (!CollectionUtils.isEmpty(request.getLineIds())) {
			sb.append(" and sor.line_id in (:lineIds) ");
		}

		if (request.getSkillLevelId() > 0) {
			sb.append(" and sor.desired_skill_level_id=:levelId ");
		}

		sb.append(" GROUP BY sor.line_id, sor.desired_skill_level_id ");

		if (request.getColName() != null && request.getOrderType() != null) {
			sb.append(" order by " + ColumnAscDescConstants.AssesmentReportColHeader.get(request.getColName()) + " "
					+ request.getOrderType());
		} else {
			sb.append(" order by passCount DESC ");
		}

		TypedQuery<Tuple> query = session.createNativeQuery(sb.toString(), Tuple.class)
				.setParameter("orgId", request.getOrgId()).setParameter("branchId", request.getBranchId())
				.setParameter("fromDt", request.getFromDt()).setParameter("toDt", request.getToDt());

		if (!CollectionUtils.isEmpty(request.getLineIds())) {
			query.setParameter("lineIds", request.getLineIds());
		}

		if (request.getSkillLevelId() > 0) {
			query.setParameter("levelId", request.getSkillLevelId());
		}
		;

		List<Tuple> objList = query.getResultList();
		if (!CollectionUtils.isEmpty(objList)) {
			for (Tuple obj : objList) {
				HashMap<String, Object> list = new HashMap<>();
				list.put("deptId", CommonUtils.objectToInt(obj.get("deptId")));
				list.put("totalCount", CommonUtils.objectToInt(obj.get("totalCount")));
				list.put("deptName", CommonUtils.objectToString(obj.get("deptName")));
				list.put("passPercentage", CommonUtils.objectToDouble(obj.get("passPercentage")));
				list.put("passCount", CommonUtils.objectToInt(obj.get("passCount")));
				list.put("failPercentage", CommonUtils.objectToDouble(obj.get("failPercentage")));
				list.put("failCount", CommonUtils.objectToInt(obj.get("failCount")));
				list.put("levelId", CommonUtils.objectToInt(obj.get("levelId")));
				list.put("levelName", CommonUtils.objectToString(obj.get("levelName")));
				list.put("lineId", CommonUtils.objectToLong(obj.get("lineId")));
				list.put("lineName", CommonUtils.objectToString(obj.get("lineName")));
				list.put("branchId", CommonUtils.objectToInt(obj.get("branchId")));
				list.put("branchName", CommonUtils.objectToString(obj.get("branchName")));

				dataList.add(list);
			}
		}
		return dataList;
	}

}
