package com.greentin.enovation.schedulars;

import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.regex.Pattern;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;

import org.apache.commons.lang.exception.ExceptionUtils;
import org.hibernate.Session;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.env.Environment;
import org.springframework.core.task.TaskExecutor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

import com.greentin.enovation.communication.ICommunication;
import com.greentin.enovation.config.EnovationConfig;
import com.greentin.enovation.dto.MailDTO;
import com.greentin.enovation.model.BranchMaster;
import com.greentin.enovation.model.EmailTemplateMaster;
import com.greentin.enovation.model.EmployeeCount;
import com.greentin.enovation.model.EmployeeDetails;
import com.greentin.enovation.utils.BaseRepository;
import com.greentin.enovation.utils.CommonUtils;
import com.greentin.enovation.utils.EnovationConstants;
import com.greentin.enovation.utils.MailUtil;
import com.ibm.icu.text.SimpleDateFormat;


@Component
@Repository
@SuppressWarnings("unchecked")
@Transactional
public class EventScheduler  extends BaseRepository  {

	private static final Logger LOGGER = LoggerFactory.getLogger(EventScheduler.class);

	@Autowired
	Environment env;

	@Autowired
	EnovationConfig enoConfig;

	@PersistenceContext
	EntityManager entityManager;

	@Autowired
	@Qualifier("taskExecutor")
	private TaskExecutor taskExecutor;
	
	@Autowired
	private ICommunication communication;

	//@Scheduled(cron = "0 15 10 * * ?")
	@Scheduled(cron = "${birthdayWishScheduler}")
	public void wishOnBirthday() {
		LOGGER.info("# Birthday Wish Scheduler");
		List<MailDTO> mailList = new ArrayList<>();
		String startDate = String.valueOf(new Date());
		int count = 0;
		try {
			List<BranchMaster> branchMaster = entityManager.createQuery("FROM BranchMaster").getResultList();
			for(BranchMaster branch : branchMaster) {
				List<EmployeeDetails> empList = entityManager.createQuery("from EmployeeDetails  where isDeactive= 0 and loggedIn = 0 and branch.branchId =:branchId ")
						.setParameter("branchId", branch.getBranchId())
						.getResultList();

				if(empList != null && empList.size() > 0) {

					EmailTemplateMaster messageContent=null;
					//					List<EmployeeDetails> empList = entityManager.createQuery("FROM EmployeeDetails WHERE branch.branchId=:branchId AND isDeactive=0")
					//							.setParameter("branchId",1)
					//							.getResultList();

					SimpleDateFormat timeFormat = new SimpleDateFormat("M-d");
					Date date = new Date();
					//System.out.println("Full formatted Date : " +today);
					//System.out.println("Today's Date " +timeFormat.format(today));
					String today = timeFormat.format(date);

					//System.out.println("=== List of Employees having birthday today ===");


					for(EmployeeDetails list : empList) {
						String dob = null;
						if(list.getDOB() != null) {
							dob = timeFormat.format(list.getDOB());
							if(today.equals(dob)) {						
								messageContent = enoConfig.getMessageContent(EnovationConstants.EVENT_BIRTHDAY);
								String sub = messageContent.getSubject();
								String body = messageContent.getBody().replaceAll(Pattern.quote("{name}"),list.getFirstName())
										.replaceAll(Pattern.quote("{orgName}"), list.getOrganization().getName())
										.replaceAll(Pattern.quote("{portalLink}"), list.getOrganization().getPortalLink());

								MailDTO mail = new MailDTO(list.getEmailId(),sub,body);
								mailList.add(mail);
								count++;
							}
						}
					}
					System.out.println("Number of Employees having birthday today : " +count);

				}
			}
			
			
			mailList.stream().forEach(x->{
				System.out.println("Email ===> " + x.getToMail());
			});
			if(count > 0)
			{
				taskExecutor.execute(new MailUtil(mailList,communication));
			}	

		}catch (Exception e) {
			e.printStackTrace();
		}
		finally {
			System.out.println("Finally block executed");
			SchedularAudit audit = new SchedularAudit("birthdayWishScheduler",startDate,String.valueOf(new Date()));
			Object o = entityManager.merge(audit);
			if(o != null) {
				System.out.println("Record saved successfully");
			}
			entityManager.close();
			if(o != null) {
				System.out.println("Record saved successfully");
				
			}
		}
	}

	@Scheduled(cron = "${anniversaryWishScheduler}")
	public void wishOnAnniversary() {
		LOGGER.info("# Wedding Anniversary Wish Scheduler");
		int count = 0;
		List<MailDTO> mailList = new ArrayList<>();
		String startDate = String.valueOf(new Date());
		System.out.println("Printing Date : " +startDate);
		try {

			List<BranchMaster> branchMaster = entityManager.createQuery("FROM BranchMaster").getResultList();
			for(BranchMaster branch : branchMaster) {
				List<EmployeeDetails> empList = entityManager.createQuery("from EmployeeDetails  where isDeactive= 0 and loggedIn = 0 and branch.branchId =:branchId ")
						.setParameter("branchId", branch.getBranchId())
						.getResultList();

				if(empList != null && empList.size() > 0) {

					EmailTemplateMaster messageContent=null;
					/*List<EmployeeDetails> empList = entityManager.createQuery("FROM EmployeeDetails WHERE branch.branchId=:branchId AND isDeactive=0")
					.setParameter("branchId",1)
					.getResultList();*/

					SimpleDateFormat timeFormat = new SimpleDateFormat("M-d");
					Date date = new Date();
					//System.out.println("Full formatted Date : " +today);
					//System.out.println("Today's Date " +timeFormat.format(today));
					String today = timeFormat.format(date);

					//System.out.println("=== List of Employees having Anniversary today ===");

					for(EmployeeDetails list : empList) {
						String doa = null;
						if(list.getDOA() != null) {
							doa = timeFormat.format(list.getDOA());
							if(today.equals(doa)) {
								/*				 		
						System.out.println("Emp ID : " +list.getEmpId());
						System.out.println("Emp first Name : " + list.getFirstName());
						System.out.println("Emp Last Name : " + list.getLastName());
						System.out.println("Emp Email : " + list.getEmailId());
						System.out.println("Emp Mobile : " +list.getContactNo());
						System.out.println("Emp Birthdate : " +list.getDOA());
						System.out.println("Formatted Emp Birthdate : " + timeFormat.format(list.getDOA()));
						System.out.println("Organisation Name 1: " + list.getOrgName());
						System.out.println("Organisation Name 2: " + list.getOrganization().getName());
						System.out.println("Portal Link : " +list.getOrganization().getPortalLink());

								 */						messageContent = enoConfig.getMessageContent(EnovationConstants.EVENT_ANNIVERSARY);
								 //System.out.println("\n ----------------------------------------");
								 String sub = messageContent.getSubject();
								 String body = messageContent.getBody().replaceAll(Pattern.quote("{name}"),list.getFirstName())
										 .replaceAll(Pattern.quote("{orgName}"), list.getOrganization().getName())
										 .replaceAll(Pattern.quote("{portalLink}"), list.getOrganization().getPortalLink());

								 MailDTO mail = new MailDTO(list.getEmailId(),sub,body);
								 mailList.add(mail);
								 count++;
							}
						}
					}
					System.out.println("Number of Employees having Anniversary today : " +count);

				}
			}
			
			mailList.stream().forEach(x->{
				System.out.println("Email ===> " + x.getToMail());
			});

			if(count > 0) {
				taskExecutor.execute(new MailUtil(mailList,communication));
			}

		}catch (Exception e) {
			LOGGER.error("#INSIDE EXCEPTION OCCURED IN ANNIVERSARY WISH SCHEDULER :"+ExceptionUtils.getStackTrace(e));
		}
		finally {
			SchedularAudit audit = new SchedularAudit("AnniversaryWishScheduler",startDate,String.valueOf(new Date()));
			entityManager.merge(audit);
			entityManager.close();
		}


	}
	
	
   @Scheduled(cron="0 0 1 1 * ?")
   @Transactional
   public void employeeCountScheduler() {
		
		LOGGER.info(" In EventScheduler | employeeCountScheduler Start ");
		int currentMonth=0,currentYear=0;
		
		String startDate = String.valueOf(new Date());
		
		try {
			
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			Date date = new Date();
			String curDate = sdf.format(date);
			
			Session session=getCurrentSession();
			currentMonth = ZonedDateTime.now().getMonthValue();
			currentYear = ZonedDateTime.now().getYear();
			
			
			LOGGER.info("In EventScheduler | employeeCountScheduler , current month "+currentMonth);
			LOGGER.info("In EventScheduler | employeeCountScheduler , current year "+currentYear);
			
			List<Object[]> orgObjList=session.createNativeQuery("select org_id,name from  master_organization").getResultList();
			
			
			if(orgObjList!=null && !orgObjList.isEmpty()) {
				
				for(Object[] obj:orgObjList) {
					int orgId=CommonUtils.objectToInt(obj[0]);
					System.out.println("orgId "+orgId);
					if(orgId>0) {
						
					   List<Object[]> tmpOrgCount=session.createNativeQuery("SELECT emp_id,branch_id FROM tbl_employee_details WHERE org_id=:orgId and is_deactive=0 and emp_type not in ('CW')")
						.setParameter("orgId", orgId).getResultList();
					   
					   if(tmpOrgCount!=null && !tmpOrgCount.isEmpty()) {
						   
						   /**
						    * Add or update employee count for current month
						    */
						   checkUpdateOrgCount(currentMonth,currentYear,session, orgId,tmpOrgCount);
						  
					   }
					
					}
					
				}
				
			}
		}catch(Exception e) {
			e.printStackTrace();
		}finally {
			SchedularAudit audit = new SchedularAudit("employeeCountScheduler", startDate,
					String.valueOf(new Date()));
			entityManager.merge(audit);
			entityManager.close();
		}
		
	}
	
	/**
	 * @author rakesh 28 dec 2020
	 * @param month
	 * @param year
	 * @param session
	 * @param orgId
	 * @param tmpOrgCount
	 * @return
	 */
	private boolean checkUpdateOrgCount(int month, int year, Session session,int orgId,List<Object[]> tmpOrgCount ) {
		boolean flag=false;
		
		List<EmployeeCount> tmpList=session.createQuery("FROM EmployeeCount WHERE orgId =:orgId and countType='ORG' and monthValue=:month and year=:year")
		       .setParameter("orgId", orgId).setParameter("month", month).setParameter("year", year).getResultList();
		 
		 if(tmpList!=null && !tmpList.isEmpty()) {
			 EmployeeCount  count=tmpList.get(0);
			 count.setCount(tmpOrgCount.size());
		 }else {
			 EmployeeCount count =new EmployeeCount(); 
			 count.setOrgId(orgId);
			 count.setMonthValue(month);
			 count.setYear(year);
			 count.setCountType("ORG");
			 count.setCount(tmpOrgCount.size());
			 session.save(count);
		 }
		 
		 LOGGER.info("In EventScheduler | employeeCountScheduler , org Id "+orgId);
		 LOGGER.info("In EventScheduler | employeeCountScheduler , Org Level employee count "+tmpOrgCount.size());
		 
		 /**
		   * Get branch list
		   */
		   List<Object[]> branchObjList=session.createNativeQuery("select branch_id,name from master_branch where org_id=:orgId")
				   .setParameter("orgId", orgId).getResultList();
		   
         if(branchObjList!=null && !branchObjList.isEmpty()) {
      	   
      	   for(Object[] branchObj:branchObjList) {
      		   
      		   int branchId=CommonUtils.objectToInt(branchObj[0]);
      		   
      		   if(branchId>0) {
      			   
      			   List<Object[]> tmpBranchCount=session.createNativeQuery("SELECT emp_id,branch_id FROM tbl_employee_details WHERE org_id =:orgId and branch_id=:branchId and is_deactive=0 and emp_type not in ('CW') ")
      						.setParameter("orgId", orgId).setParameter("branchId", branchId).getResultList();
      			   if(tmpBranchCount!=null && !tmpBranchCount.isEmpty()){
      				  
					   /**
					    * Add or update branch level employee count for current month
					    */
      				 checkUpdateBranchCount(month,year,session, orgId,branchId,tmpBranchCount);
      			   } 
      		   }   
      	   }
      	   
         }
         
		flag=true;
		return flag;
	}
	
	/**
	 * @author rakesh 28 dec 2020
	 * @param month
	 * @param year
	 * @param session
	 * @param orgId
	 * @param branchId
	 * @param tmpBranchCount
	 * @return
	 */
	private boolean checkUpdateBranchCount(int month, int year, Session session,int orgId,int branchId,List<Object[]> tmpBranchCount ) {
		boolean flag=false;
		
		List<EmployeeCount> tmpList=session.createQuery("FROM EmployeeCount WHERE orgId =:orgId and branchId=:branchId and countType='BRANCH' and monthValue=:month and year=:year")
			       .setParameter("orgId", orgId).setParameter("branchId", branchId)
			       .setParameter("month", month).setParameter("year", year).getResultList();
		 
		 if(tmpList!=null && !tmpList.isEmpty()) {
			 EmployeeCount  count=tmpList.get(0);
			 count.setCount(tmpBranchCount.size());
		 }else {
			 EmployeeCount count =new EmployeeCount(); 
			 count.setOrgId(orgId);
			 count.setBranchId(branchId);
			 count.setMonthValue(month);
			 count.setYear(year);
			 count.setCountType("BRANCH");
			 count.setCount(tmpBranchCount.size());
			 session.save(count);
		 }
		 
		 LOGGER.info("In EventScheduler | employeeCountScheduler , branch Id "+branchId);
		 LOGGER.info("In EventScheduler | employeeCountScheduler , branch Level employee count "+tmpBranchCount.size());
		
		 /**
		   * Get Dept list
		   */
		   List<Object[]> deptObjList=session.createNativeQuery("select dept_id,dept_name from master_department where org_id=:orgId and branch_id=:branchId")
				   .setParameter("orgId", orgId).setParameter("branchId", branchId)
				   .getResultList();
		   
		  if(deptObjList!=null && !deptObjList.isEmpty()) {
			 
			  for(Object[] deptObj:deptObjList) {
				  
				  int deptId=CommonUtils.objectToInt(deptObj[0]);
				  if(deptId>0) {
					  
					  List<Object[]> tmpDeptCount=session.createNativeQuery("SELECT emp_id,branch_id FROM tbl_employee_details WHERE org_id =:orgId and branch_id=:branchId and  dept_id=:deptId and  is_deactive=0 and emp_type not in ('CW') ")
	      						.setParameter("orgId", orgId).setParameter("branchId", branchId)
	      						.setParameter("deptId", deptId).getResultList();
					
					  if(tmpDeptCount!=null && !tmpDeptCount.isEmpty()) {
						  
						  /**
						    * Add or update dept level employee count for current month
						    */
						  checkUpdateDeptCount(month,year,session, orgId,branchId,deptId,tmpDeptCount);
					  }
					 
				  }
				  
			  }
		  }
		  
		flag=true;
		return flag;
	}
	
	/**
	 * @author rakesh 28 dec 2020
	 * @param month
	 * @param year
	 * @param session
	 * @param orgId
	 * @param branchId
	 * @param deptId
	 * @param tmpDeptCount
	 * @return
	 */
	private boolean checkUpdateDeptCount(int month, int year, Session session,int orgId,int branchId,int deptId,List<Object[]> tmpDeptCount ) {
		boolean flag=false;
		
		List<EmployeeCount> tmpList=session.createQuery("FROM EmployeeCount WHERE orgId =:orgId and branchId=:branchId  and deptId=:deptId and countType='DEPT' and monthValue=:month and year=:year")
			       .setParameter("orgId", orgId).setParameter("branchId", branchId).setParameter("deptId", deptId)
			       .setParameter("month", month).setParameter("year", year).getResultList();
		
		 if(tmpList!=null && !tmpList.isEmpty()) {
			 EmployeeCount  count=tmpList.get(0);
			 count.setCount(tmpDeptCount.size());
		 }else {
			 EmployeeCount count =new EmployeeCount(); 
			 count.setOrgId(orgId);
			 count.setBranchId(branchId);
			 count.setDeptId(deptId);
			 count.setMonthValue(month);
			 count.setYear(year);
			 count.setCountType("DEPT");
			 count.setCount(tmpDeptCount.size());
			 session.save(count);
		 }
		 
		 LOGGER.info("In EventScheduler | employeeCountScheduler , dept Id "+deptId);
		 LOGGER.info("In EventScheduler | employeeCountScheduler , dept Level employee count "+tmpDeptCount.size());
		 
		 /**
		   * Get Line list
		   */
		   List<Object[]> lineObjList=session.createNativeQuery("select id,name from dwm_line where dept_id=:deptId")
				   .setParameter("deptId", deptId)
				   .getResultList();
		   
		  if(lineObjList!=null && !lineObjList.isEmpty()) {
			 
			  for(Object[] lineObj:lineObjList) {
				  
				  long lineId=CommonUtils.objectToLong(lineObj[0]);
				  
				  if(lineId>0) {
					  
					 List<Object[]> tmpLineCount=session.createNativeQuery("SELECT emp_id,branch_id FROM tbl_employee_details WHERE org_id =:orgId and branch_id=:branchId and  dept_id=:deptId and line_id=:lineId and  is_deactive=0 and emp_type not in ('CW') ")
	      						.setParameter("orgId", orgId).setParameter("branchId", branchId)
	      						.setParameter("deptId", deptId).setParameter("lineId", lineId)
	      						.getResultList();
					
					 if(tmpLineCount!=null && !tmpLineCount.isEmpty()) {
						 
						 /**
						   * Add or update line level employee count for current month
						   */
						 checkUpdateLineCount(month,year,session, orgId,branchId,deptId,lineId,tmpLineCount);
					 }
					 
				  }  
			  }
		  }
		flag=true;
		return flag;
	}
	
	/**
	 * @author rakesh 28 dec 2020
	 * @param month
	 * @param year
	 * @param session
	 * @param orgId
	 * @param branchId
	 * @param deptId
	 * @param lineId
	 * @param tmpLineCount
	 * @return
	 */
	private boolean checkUpdateLineCount(int month, int year, Session session,int orgId,int branchId,int deptId,long lineId,List<Object[]> tmpLineCount ) {
		boolean flag=false;
		
		List<EmployeeCount> tmpList=session.createQuery("FROM EmployeeCount WHERE orgId =:orgId and branchId=:branchId  and deptId=:deptId and lineId=:lineId and  countType='LINE' and monthValue=:month and year=:year")
			       .setParameter("orgId", orgId).setParameter("branchId", branchId).setParameter("deptId", deptId)
			       .setParameter("month", month).setParameter("year", year).setParameter("lineId", lineId)
			       .getResultList();
		
		 if(tmpList!=null && !tmpList.isEmpty()) {
			 EmployeeCount  count=tmpList.get(0);
			 count.setCount(tmpLineCount.size());
		 }else {
			 EmployeeCount count =new EmployeeCount(); 
			 count.setOrgId(orgId);
			 count.setBranchId(branchId);
			 count.setDeptId(deptId);
			 count.setLineId(lineId);
			 count.setMonthValue(month);
			 count.setYear(year);
			 count.setCountType("LINE");
			 count.setCount(tmpLineCount.size());
			 session.save(count);
		 }
		 
		 LOGGER.info("In EventScheduler | employeeCountScheduler , line Id "+lineId);
		 LOGGER.info("In EventScheduler | employeeCountScheduler , line Level employee count "+tmpLineCount.size());
		 
		flag=true;
		return flag;
	}
	


}
