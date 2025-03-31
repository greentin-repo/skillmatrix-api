package com.greentin.enovation.schedulars;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;

import org.apache.commons.lang.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.task.TaskExecutor;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;
import com.greentin.enovation.config.EnovationConfig;
import com.greentin.enovation.dto.MailDTO;
import com.greentin.enovation.model.EmailTemplateMaster;
import com.greentin.enovation.model.EmployeeDetails;

@Component
@SuppressWarnings("unchecked")
@Repository
@Transactional
public class RegistrationEmailScheduler {
	@Autowired
	EnovationConfig enoConfig;
	
	@PersistenceContext
	EntityManager entityManager;

	@Autowired
	@Qualifier("taskExecutor")
	private TaskExecutor taskExecutor;
	
	//@Scheduled(fixedRate=20000)
	public void sendEmailsOnRegistration(Integer branchId){
		EmailTemplateMaster messageContent=null;
		List<MailDTO> mailList = new ArrayList<>();
		try {
			List<EmployeeDetails> empList=entityManager.createQuery("from EmployeeDetails where branch.branchId=:branchId and isDeactive=0 ")
					.setParameter("branchId", branchId)
					.getResultList();
			messageContent = enoConfig.getMessageContent("sampleRegistration");
			String mailContent="",subject="";;
			for(EmployeeDetails empDet:empList) {
				if(empDet.getCreatedBy() != null) {
				EmployeeDetails emp=(EmployeeDetails)entityManager.find(EmployeeDetails.class, empDet.getCreatedBy());
				if(messageContent.getBody()!= null && messageContent.getBody().contains("{name}")){
					
					mailContent = messageContent.getBody().replaceAll(Pattern.quote("{name}"),empDet.getFirstName().trim())
							.replaceAll(Pattern.quote("{password}"),"12345678")
							.replaceAll(Pattern.quote("{email}"),empDet.getEmailId())
							.replaceAll(Pattern.quote("{appLink}"),messageContent.getDesc())
							.replaceAll(Pattern.quote("{portalLink}"), empDet.getOrganization().getPortalLink()+"login.html")
					        .replaceAll(Pattern.quote("{superAdminName}"),emp.getFirstName().trim());
	               }
				subject=messageContent.getSubject();
				System.out.println("portal link:"+ empDet.getOrganization().getPortalLink());
				if(empDet.getEmailId() != null ) {
			//		taskExecutor.execute(new MailUtil(empDet.getEmailId(),subject,mailContent));	
					MailDTO mail =new MailDTO(empDet.getEmailId(),subject,mailContent);		
					mailList.add(mail);
                }
			}
			}
		}catch(Exception e) {
//			LOGGER.error("#INSIDE EXCEPTION OCCURED IN SENDEMAILSONREGISTRATION SCHEDULER :"+ExceptionUtils.getStackTrace(e));
		}
		}
}
