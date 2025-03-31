package com.greentin.enovation.setup;

import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.Tuple;
import javax.transaction.Transactional;

import org.apache.commons.collections.CollectionUtils;
import org.hibernate.Session;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import com.greentin.enovation.accesscontrol.ProductOrgConfig;
import com.greentin.enovation.model.BranchSetupConfig;
import com.greentin.enovation.model.NoticeSetup;
import com.greentin.enovation.model.SetupData;
import com.greentin.enovation.model.SetupMaster;
import com.greentin.enovation.model.SocialMedia;
import com.greentin.enovation.model.ThresholdSetup;
import com.greentin.enovation.utils.BaseRepository;
import com.greentin.enovation.utils.CommonUtils;
import com.greentin.enovation.utils.EnovationConstants;

@Repository
@SuppressWarnings("unchecked")
public class SetupConfigDaoImpl extends BaseRepository implements SetupConfigDao{

	@PersistenceContext
	private EntityManager entityManager;

	private static final Logger logger = LoggerFactory.getLogger(SetupConfigDaoImpl.class);

	@Override
	@Transactional
	public boolean saveSetupMaster(SetupMaster setupMaster) {
		boolean flag = false;
		logger.info("INSIDE SAVE SETUPMASTER API");
		Session session = entityManager.unwrap(Session.class);
		try {
			session.save(setupMaster);
			flag = true;
		} catch (Exception e) {
			logger.info("ERROR INSIDE SAVE SETUP MASTER API" + e.getMessage());
		}
		return flag;
	}

	@Override
	@Transactional
	public boolean updateSetupMaster(SetupMaster setupmaster) {
		boolean flag= false;
		logger.info("INSIDE UPDATE SETUPMASTER API");
		Session session = entityManager.unwrap(Session.class);
		try {
			session.createQuery("UPDATE SetupMaster set setup_name=:setupName  WHERE setup_id=:setupID")
			.setParameter("setupName", setupmaster.getSetupName())
			.setParameter("setupID", setupmaster.getSetupId()).executeUpdate();
			flag= true;
		}catch (Exception e) {
			logger.info("ERROR INSIDE UPDATE SETUPMASTR API"+e.getMessage());
		}
		return flag;
	}

	@Override
	@Transactional
	public boolean saveBranchSetupConfig(BranchSetupConfig branchSetupConfig) {
		boolean flag = false;
		logger.info("INSIDE SAVE BRANCHSETUPCONFIG API");
		Session session = entityManager.unwrap(Session.class);
		try {
			branchSetupConfig.setIsSetupCompleted(EnovationConstants.DEACTIVESTATUS);
			session.save(branchSetupConfig);
			flag = true;
		} catch (Exception e) {
			logger.info("ERROR INSIDE SAVE BRANCHSETUPCONFIG API" + e.getMessage());
		}
		return flag;
	}

	@Override
	@Transactional
	public boolean updateBranchSetupConfig(BranchSetupConfig bconfig) {
		boolean flag= false;
		logger.info("INSIDE UPDATE BRANCHSETUPCONFIG API");
		Session session = entityManager.unwrap(Session.class);
		try {
			session.createNativeQuery("update branch_setup_config set is_setup_completed =:is_setup_completed "
					+ " where id=:id")
			.setParameter("is_setup_completed", bconfig.getIsSetupCompleted())
			.setParameter("id", bconfig.getId()).executeUpdate();
			flag= true;
		}catch (Exception e) {
			logger.info("ERROR INSIDE UPDATE BRANCHSETUPCONFIG API"+e.getMessage());
		}
		return flag;
	}


	@Override
	public List<SetupData> listofSideMenu(Integer branchId){

		logger.info("INSIDE LISTOFSIDEMENU API");
		StringBuffer str= new StringBuffer("SELECT hs.setup_name,"
				+ " bs.is_setup_completed, bs.is_mandatory,bs.branch_id,bs.id FROM setupmaster hs,branch_setup_config bs WHERE bs.setup_id = hs.setup_id and bs.branch_id =:branchId");				
		Session session = entityManager.unwrap(Session.class);
		List<SetupData> list = new ArrayList<>();
		try {
			Query query = session.createNativeQuery(str.toString()).setParameter("branchId", branchId);
			List<Object[]> rows = query.getResultList();
			SetupData data = null;
			for(Object[] row : rows) {
				data = new SetupData();
				data.setSideMenuName(row[0].toString());
				data.setIsSetupCompleted(Integer.parseInt(row[1].toString()));
				data.setIsMandatory(Integer.parseInt(row[2].toString()));
				data.setBranchId(Integer.parseInt(row[3].toString()));
				data.setBranch_setupId(Integer.parseInt(row[4].toString()));
				list.add(data);
			}
		}catch (Exception e) {
			logger.info("ERROR INSIDE LISTOFSIDEMENU API"+e.getMessage());

		}
		return list;
	}

	/*
	 * Created By AKSHAY 29/11/2018
	 * */

	@Override
	public List<SetupMaster> listofSetupConfig() {
		logger.info("INSIDE LISTOFSETUPCONFIG API");
		Session session = getCurrentSession();
		List<SetupMaster> list = new ArrayList<>();
		try {
			list	= session.createQuery("FROM SetupMaster").getResultList();
		}catch (Exception e) {
			logger.error("#INSIDE EXCEPTION OCCURED IN  LISTOFSETUPCONFIG API"+e.getMessage());
		}
		return list;	
	}


	@Override
	@Transactional
	public boolean updateActivitySetup(ProductOrgConfig poc) {
		boolean flag= false;
		logger.info("INSIDE  UPDATEACTIVITYSETUP API");
		Session session = entityManager.unwrap(Session.class);
		try {
			session.createNativeQuery("update product_org_config set is_activity_on =:is_activity_on where branch_id=:branch_id")
			.setParameter("is_activity_on", poc.getIsActivityOn())
			.setParameter("branch_id", poc.getBranchId()).executeUpdate();
			flag= true;
		}catch (Exception e) {
			logger.error("ERROR INSIDE UPDATE ACTIVITYSETUP API"+e.getMessage());
		}
		return flag;
	}

	@Override
	@Transactional
	public boolean updateDocChangeSetup(ProductOrgConfig poc) {
		boolean flag= false;
		logger.info("INSIDE  UPDATEDOCCHANGESETUP API");
		Session session = entityManager.unwrap(Session.class);
		try {
			session.createNativeQuery(" update product_org_config set is_doc_change_on =:is_doc_change_on  where branch_id=:branch_id ")
			.setParameter("is_doc_change_on", poc.getIsDocChangeOn())
			.setParameter("branch_id", poc.getBranchId()).executeUpdate();
			flag= true;
		}catch (Exception e) {
			logger.error("ERROR INSIDE UPDATE DOCCHANGESETUP API"+e.getMessage());
		}
		return flag;
	}

	@Override
	@Transactional
	public boolean uploadImage(ProductOrgConfig poc) {
		boolean flag= false;
		logger.info("INSIDE  UPLOADIMAGE API");
		Session session = entityManager.unwrap(Session.class);
		try {	
			session.createNativeQuery(" update product_org_config set is_before_upload_image_on=:is_upload_image_on  where branch_id=:branch_id")
			.setParameter("is_upload_image_on", poc.getIsBeforeUploadImageOn())
			.setParameter("branch_id", poc.getBranchId())
			.executeUpdate();
			
			flag= true;
		}catch (Exception e) {
			logger.error("ERROR INSIDE UPLOADIMAGE API"+e.getMessage());
		}
		return flag;
	}

	@Override
	public boolean afterUploadImage(ProductOrgConfig poc) {
		boolean flag= false;
		logger.info("INSIDE  UPLOADIMAGE API");
		Session session = entityManager.unwrap(Session.class);
		try {	
			session.createNativeQuery(" update product_org_config set is_after_upload_image_on=:is_upload_image_on  where branch_id=:branch_id")
			.setParameter("is_upload_image_on", poc.getIsAfterUploadImageOn())
			.setParameter("branch_id", poc.getBranchId())
			.executeUpdate();
			
			flag= true;
		}catch (Exception e) {
			logger.error("ERROR INSIDE UPLOADIMAGE API"+e.getMessage());
		}
		return flag;
	}

	@Override
	public List<ProductOrgConfig> listOfProdOrgCofig(Integer branchId) {
		logger.info("INSIDE LIST_OF_PRODUCT_ORG_CONFIG API");
		Session session = getCurrentSession();
		StringBuffer str=new StringBuffer("from ProductOrgConfig poc where poc.branch.branchId =:branchId");
		List<ProductOrgConfig>list = new ArrayList<>();
		try {
		list  = session.createQuery(str.toString()).setParameter("branchId", branchId)
					.getResultList();
			/*
			 * List<Object[]> rows = query.getResultList(); ProductOrgConfig data = null;
			 * for(Object[] row : rows) { data = new ProductOrgConfig();
			 * data.setBranchId(Integer.parseInt(row[0].toString()));
			 * data.setIsAfterUploadImageOn(Integer.parseInt(row[1].toString()));
			 * data.setIsBeforeUploadImageOn(Integer.parseInt(row[2].toString()));
			 * list.add(data); }
			 */
		}
		catch (Exception e) {
			e.printStackTrace();
			logger.error("#INSIDE EXCEPTION OCCURED IN LIST_OF_PRODUCT_ORG_CONFIG API"+e.getMessage());
		}
		return list;	
		
	}

	@Override
	@Transactional
	public boolean setThreshold(ThresholdSetup thSetup) {
		
		boolean flag = false;
		logger.info("# Inside ThresholdSetup API ");
		Session session = getCurrentSession();
		try {
			session.save(thSetup);
			flag = true;
		}catch (Exception e) {
			logger.error("ERROR INSIDE SAVE Threshold API" +e.getMessage());
		}
		
		return flag;
	}

	@Override
	public List<ThresholdSetup> getThresholdList(Integer branchId,Integer deptId) {
		
		logger.info("# INSIDE GETTHRESHOLDDETAILS API");
		Session session = getCurrentSession();
		List<ThresholdSetup> list = new ArrayList<>();
		try {
			List<Object[]> resultList = session.createNativeQuery("SELECT ts.threshold_id,ms.threshold_key,ts.threshold_value,\r\n" + 
					"ts.dept_id,ts.branch_id,md.dept_name,ts.id\r\n" + 
					"FROM threshold_setup ts\r\n" + 
					"INNER JOIN master_threshold ms ON ts.threshold_id = ms.id\r\n" + 
					"LEFT JOIN master_department md ON ts.dept_id = md.dept_id\r\n" + 
					"WHERE ts.branch_id=:branchId AND ts.dept_id=:deptId")
					.setParameter("branchId", branchId)
					.setParameter("deptId", deptId)
					.getResultList();
			for(Object[] obj : resultList) {
				ThresholdSetup th = new ThresholdSetup();
				th.setThresholdId(Integer.parseInt(String.valueOf(obj[0])));
				th.setThresholdKey(String.valueOf(obj[1]));
				th.setThresholdValue(Integer.parseInt(String.valueOf(obj[2])));
				th.setDeptId(Integer.parseInt(String.valueOf(obj[3])));
				th.setBranchId(Integer.parseInt(String.valueOf(obj[4])));
				th.setDeptName(String.valueOf(obj[5]));
				th.setId(Integer.parseInt(String.valueOf(obj[6])));
				list.add(th);
			}	
		}catch (Exception e) {
			logger.error("# INSIDE EXCEPTION OCCURED IN  GETTHRESHOLDDETAILS DAO IMPL "+e.getMessage());
		}
		return list;
	}

	@Override
	@Transactional
	public int updateThreshold(ThresholdSetup thSetup) {
	
		int count=0;
		logger.info("# INSIDE UPDATETHRESHOLDDETAILS API");
		Session session = getCurrentSession();
		try {
		 count = session.createNativeQuery("UPDATE threshold_setup SET threshold_value=:thresholdValue,"
					+ "updated_date=:updatedDate\r\n" + 
					"WHERE branch_id=:branchId AND dept_id=:deptId AND id=:id")
			
			.setParameter("thresholdValue", thSetup.getThresholdValue())
			.setParameter("updatedDate", CommonUtils.currentDate())
			.setParameter("id", thSetup.getThreshold().getId())
			.setParameter("branchId", thSetup.getBranch().getBranchId())
			.setParameter("deptId", thSetup.getDeptId())
			.executeUpdate();			
		} catch (Exception e) {
			logger.error("# INSIDE EXCEPTION OCCURED IN  UPDATETHRESHOLDDETAILS "+e.getMessage());	
			e.printStackTrace();
		}
		return count;
	}

	
	@Override
	@Transactional
	public int deleteThreshold(ThresholdSetup thSetup) {
		int count = 0;
		Session session = getCurrentSession();
		logger.info("INSIDE DELETE Threshold API");
		try {
			
		count=	session.createNativeQuery("DELETE FROM threshold_setup WHERE branch_id =:branchId "
				+ "AND dept_id=:deptId AND id=:thresholdId")
			.setParameter("branchId", thSetup.getBranch().getBranchId())
			.setParameter("deptId", thSetup.getDeptId())
			.setParameter("thresholdId", thSetup.getThreshold().getId())
			.executeUpdate();
		} catch (Exception e) {
			logger.error("ERROR INSIDE DELETE THRESHOLD API"+e.getMessage());
		}
		
		return count;
	}
	
	
	@Override
	@Transactional
	public boolean addSocialMedia(SocialMedia socialMedia) {
	
		boolean flag = false;
		logger.info("# INSIDE SOCIAL MEDIA ADD API");
		Session session = getCurrentSession();
		try {
			session.save(socialMedia);
			flag = true;
		}catch (Exception e) {
			e.printStackTrace();

		}
		
		return flag;
	}

	@Override
	@Transactional
	public List<SocialMedia> getSocialMediaDetails(Integer branchId) {
		
		logger.info("# INSIDE SOCIAL MEDIA GET DETAILS API");
		List<SocialMedia> list = new ArrayList<>();
		Session session = getCurrentSession();
		try {
			
			List<Object[]> data = session.createNativeQuery("SELECT s.id,ms.media_name,s.media_url,s.branch_id,ms.icon_url "
					+ "FROM social_media s \r\n" + 
					"INNER JOIN master_social_media ms ON s.media_id = ms.id\r\n" + 
					"WHERE s.branch_id=:branchId")
					.setParameter("branchId", branchId)
					.getResultList();
		for(Object[] obj: data) {
			
			SocialMedia smlist = new SocialMedia();
			smlist.setId(Integer.parseInt(String.valueOf(obj[0])));
			smlist.setMediaName(String.valueOf(obj[1]));
			smlist.setMediaUrl(String.valueOf(obj[2]));
			smlist.setBranchId(Integer.parseInt(String.valueOf(obj[3])));
			smlist.setIconUrl(String.valueOf(obj[4]));
			list.add(smlist);
		}
			
		}catch (Exception e) {
			e.printStackTrace();
		}
		return list;
	}

	@Override
	@Transactional
	public int updateSocialMedia(SocialMedia socialMedia) {
		
		int count = 0;
		logger.info("# INSIDE SOCIAL MEDIA UPDATE API ");
		Session session = getCurrentSession();
		try {
			
			count = session.createNativeQuery("UPDATE social_media SET media_url=:mediaUrl,updated_date=:updatedDate "
					+ "WHERE id=:id AND branch_id=:branchId")
			.setParameter("id", socialMedia.getId())
			.setParameter("branchId", socialMedia.getBranch().getBranchId())
			.setParameter("mediaUrl", socialMedia.getMediaUrl())
			.setParameter("updatedDate", CommonUtils.currentDate())
			.executeUpdate();
			
			
		}catch (Exception e) {
			e.printStackTrace();
		}
		
		return count;
	}

	@Override
	@Transactional
	public boolean deleteSocialMedia(SocialMedia socialMedia) {
	
		boolean flag = false;
		logger.info("# INSIDE SOCIAL MEDIA DELETE API ");
		Session session = getCurrentSession();
		try {
			
			session.createNativeQuery("DELETE FROM social_media WHERE id=:id AND branch_id=:branchId")
			.setParameter("id", socialMedia.getId())
			.setParameter("branchId", socialMedia.getBranch().getBranchId())
			.executeUpdate();
			flag = true;
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return flag;
	}
	@Transactional
	@Override
	public boolean addNotice(NoticeSetup notice) {
		logger.info("# INSIDE ADD NOTICE API ");
		boolean flag = false;
		Session session = getCurrentSession();
			try {					
				
				notice.setCreatedDate(CommonUtils.currentDate());
				session.save(notice);	
				flag = true;
						
			} catch (Exception e) {
				e.printStackTrace();
			}
			
		return flag;
	}

	@Override
	public List<NoticeSetup> getNoticeDetails(Integer branchId) {
	
		logger.info("# INSIDE GET NOTICE DETAILS API ");
		Session session = getCurrentSession();
		List<NoticeSetup> list = new ArrayList<>();
		try {
			
		List<Object[]>	data =session.createNativeQuery("SELECT \r\n" + 
				"    s.notice_id,\r\n" + 
				"    group_concat(' ',d.dept_name) as intended_depts,\r\n" + 
				"    s.title,\r\n" + 
				"    s.notice_message,\r\n" + 
				"    DATE_FORMAT(s.notice_period, '%Y-%m-%d') AS notice_period,\r\n" + 
				"    e.first_name,\r\n" + 
				"    e.last_name,\r\n" + 
				"    DATE_FORMAT(s.created_date, '%Y-%m-%d') AS created_date\r\n" + 
				"FROM\r\n" + 
				"    notice_intended_for n\r\n" + 
				"        INNER JOIN\r\n" + 
				"    tbl_notice_setup s ON n.notice_id = s.notice_id\r\n" + 
				"        INNER JOIN\r\n" + 
				"    master_department d ON n.intended_for = d.dept_id\r\n" + 
				"        INNER JOIN\r\n" + 
				"    tbl_employee_details e ON e.emp_id = s.created_by\r\n" + 
				"WHERE\r\n" + 
				"    s.branch_id=:branchId\r\n" + 
				"GROUP BY s.notice_id\r\n" + 
				"ORDER BY DATE_FORMAT(s.created_date, '%Y-%m-%d') DESC\r\n" + 
				"")
					.setParameter("branchId", branchId)
					.getResultList();
			
			for(Object[] obj : data) {
				NoticeSetup notice = new NoticeSetup();
				notice.setNoticeId(Integer.parseInt(String.valueOf(obj[0])));
				notice.setIntendedDepts(String.valueOf(obj[1]));
				notice.setTitle(String.valueOf(obj[2]));
				notice.setNoticeMessage(String.valueOf(obj[3]));
				notice.setNotePeriod(String.valueOf(obj[4]));
				notice.setFirstName(String.valueOf(obj[5]));
				notice.setLastName(String.valueOf(obj[6]));
				notice.setCreationDate(String.valueOf(obj[7]));
				list.add(notice);
			}
			
		}catch (Exception e) {
			e.printStackTrace();
		}
		
		return list;
	}

	@Transactional
	@Override
	public boolean updateHappinessSetup(ProductOrgConfig poc) {
		boolean flag= false;
		logger.info("INSIDE UPDATE_HAPPINESS_SETUP API");
		Session session = entityManager.unwrap(Session.class);
		try {	
			session.createNativeQuery(" update product_org_config set is_happiness_on=:is_happiness where branch_id=:branch_id")
			.setParameter("is_happiness", poc.getIsHappinessOn())
			.setParameter("branch_id", poc.getBranchId())
			.executeUpdate();
			flag= true;
		}catch (Exception e) {
			logger.error("ERROR INSIDE UPDATE_HAPPINESS_SETUP API"+e.getMessage());
		}
		return flag;
	}

	@Override
	@Transactional
	public boolean deleteNotice(NoticeSetup notice) {
		boolean flag = false;
		logger.info("# INSIDE DELETE NOTICE API ");

		Session session = getCurrentSession();
		try {
/*			Object perInstance = session.load(notice, id);
			if (perInstance != null) {
			    session.delete(perInstance);
			    flag=true;
			}
*/		
			
// 			This way also we can do cascade delete. Just Method parameter to (NoticeSteup Notice)
			long id = notice.getNoticeId();
			Object persistentInstance = session.load(NoticeSetup.class, id);
			if (persistentInstance != null) {
			    session.delete(persistentInstance);
			    flag=true;
			}		
			
			
			
		} catch (Exception e) {
			logger.error("# EXCEPTION  INSIDE DELETE NOTICE API"+e.getMessage());
		}
		return flag;
	}

	@Override
	public boolean isSocialMediaExist(SocialMedia socialMedia) {
		
		boolean flag = false;
		Session session = getCurrentSession();
		try {
			
		List<Object[]> media =session.createNativeQuery("SELECT * FROM social_media WHERE media_id=:mediaId AND branch_id=:branchId")
			.setParameter("mediaId", socialMedia.getSocialMedia().getId())
			.setParameter("branchId", socialMedia.getBranch().getBranchId())
			.getResultList();
		
		flag = (media != null && !media.isEmpty() ? true : false);
		
//		if(media != null && !media.isEmpty()  ) {
//			flag = true;
//		}else {
//			flag = false;
//		}
			
		} catch (Exception e) {
			logger.error("# EXCEPTION INSIDE IS SOCIAL MEDIA EXIST"+e.getMessage());
		}
		return flag;
	}
	
	/**
	 * @author Vinay B Jan 8, 2020 2:52:29 PM
	 * @param poc
	 * @return
	 */
	@Override
	@Transactional
	public boolean updateMandatoryFieldDetails(ProductOrgConfig poc) {
		boolean flag = false;
		Session session = getCurrentSession();
//		ProductOrgConfig singleRecord = (ProductOrgConfig) session
//				.createQuery("FROM ProductOrgConfig WHERE branch.branchId=:branchId")
//				.setParameter("branchId", poc.getBranchId()).getSingleResult();
//		if (singleRecord != null) {
//			flag = updateSingleRecord(session, singleRecord, poc);
//		}
//		return flag;
		
		
		if (poc != null) {

			List<Tuple> list = session.createQuery(
					"select poc.isDocChangeOn as isDocChangeOn,poc.isSusAuditRequired as isSusAuditRequired,"
							+ " poc.isActivityOn as isActivityOn, poc.isHappinessOn as isHappinessOn,"
							+ " poc.isThemeCostMandate as isThemeCostMandate,poc.isSustenanceAuditRequired as isSustenanceAuditRequired,"
							+ " poc.isTierOneSelfImpl as isTierOneSelfImpl,poc.tierTwoSelfImpl as tierTwoSelfImpl,"
							+ " poc.isBeforeUploadImageOn as isBeforeUploadImageOn"
							+ " from ProductOrgConfig poc" + " where id=:id ",
					Tuple.class).setParameter("id", poc.getId()).getResultList();

			ProductOrgConfig singleRecord = (ProductOrgConfig) session
					.createQuery("FROM ProductOrgConfig WHERE branch.branchId=:branchId")
					.setParameter("branchId", poc.getBranchId()).getSingleResult();

			flag = updateSingleRecord(session, singleRecord, poc);
//			if (!CollectionUtils.isEmpty(list)) {
//				suggAudit.updateProductOrgConfigAudit(session, poc, list.get(0));
//			}

		}
		return flag;
	}

	/**
	 * @author Vinay B Jan 8, 2020 3:39:06 PM
	 * @param session
	 * @param singleRecord
	 * @param poc
	 * @return
	 */
	private boolean updateSingleRecord(Session session, ProductOrgConfig singleRecord, ProductOrgConfig poc) {
		boolean flag = false;
		System.err.println("Inside update single record");
		try {

			if (poc.getBranchId() > 0) {
			
				singleRecord.setIsDocChangeOn(poc.getIsDocChangeOn());
				singleRecord.setIsThemeCostMandate(poc.getIsThemeCostMandate());
				singleRecord.setIsHappinessOn(poc.getIsHappinessOn());
				singleRecord.setIsBeforeUploadImageOn(poc.getIsBeforeUploadImageOn());
				singleRecord.setIsActivityOn(poc.getIsActivityOn());
				singleRecord.setIsTierOneSelfImpl(poc.getIsTierOneSelfImpl());
				singleRecord.setIsSustenanceAuditRequired(poc.getIsSustenanceAuditRequired());
				singleRecord.setIsSusAuditRequired(poc.getIsSusAuditRequired());
				singleRecord.setTierTwoSelfImpl(poc.getTierTwoSelfImpl());
				
				System.out.println("parameters set");
				session.update(singleRecord);
				flag = true;
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		return flag;
	}

	
}
