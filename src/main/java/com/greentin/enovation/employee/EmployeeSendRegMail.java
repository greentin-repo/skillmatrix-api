package com.greentin.enovation.employee;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;
import javax.transaction.Transactional;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.Session;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.task.TaskExecutor;
import org.springframework.stereotype.Component;
import com.greentin.enovation.accesscontrol.Role;
import com.greentin.enovation.config.EnovationConfig;
import com.greentin.enovation.dto.MailDTO;
import com.greentin.enovation.model.EmailTemplateMaster;
import com.greentin.enovation.model.EmployeeDetails;
import com.greentin.enovation.utils.BaseRepository;
import com.greentin.enovation.utils.EnovationConstants;
import com.greentin.enovation.utils.MultipleMailUtil;

@Component
@Transactional
public class EmployeeSendRegMail  extends BaseRepository{
	
	private static final Logger LOGGER = LoggerFactory.getLogger(EmployeeSendRegMail.class);
	@Autowired
	@Qualifier("taskExecutor")
	private TaskExecutor taskExecutor;
	
	@Autowired
	EnovationConfig enoConfig;

	
	@SuppressWarnings("unchecked")
	public boolean sendMail() {
		
		String verifyLink = null, role = null, newRemove = null;
		String prtalLink = "https://gabriel.myenovation.com/";
		String superAdmin = "Srirupa";
		int branchid=2;
		EmailTemplateMaster messageContent = null;
		boolean flag = false;
		LOGGER.info("#INSIDE IN UPDATESETUPCOMLETED");
	
		List<MailDTO> mailList=new ArrayList<>();
		try {
			Session session=getCurrentSession();
			List<EmployeeDetails> sendMailEmployeeList=session.createQuery("FROM EmployeeDetails WHERE isNotify=:notify and branch.branchId=:branchId and isDeactive=0 ")
					.setParameter("notify", EnovationConstants.ZERO)
					.setParameter("branchId",branchid)
					.getResultList();
			for (EmployeeDetails employee : sendMailEmployeeList) {
					String roles = "";
					List<String> roleName = new ArrayList<>();
					if (employee.getRoles() != null && employee.getRoles().size() > 0) {
						for (Role r : employee.getRoles()) {
							roleName.add((r.getName()).toString());
						}
						roles = StringUtils.join(roleName, ',');
					}
					
					System.out.println("email ID :" + employee.getEmailId());
					
					verifyLink = String.valueOf(EnovationConfig.buddyConfig.get("verifyEmployeeEmailLink"));
					messageContent = enoConfig.getMessageContent(EnovationConstants.sendEmailNewRegistedEmpSubject);
					EmailTemplateMaster emailTemplateImg = enoConfig.getMessageContent(EnovationConstants.EmailTemplateImg);

					String mailContent = null, subject = messageContent.getSubject();
					String fname = "";
					if (employee.getFirstName() != null) {
						fname = employee.getFirstName();
					}
				
					if (messageContent.getBody() != null && messageContent.getBody().contains("{name}")) {
						mailContent = messageContent.getBody().replaceAll(Pattern.quote("{name}"), fname.trim())
								.replaceAll(Pattern.quote("{vlink}"), verifyLink + employee.getToken())
								.replaceAll(Pattern.quote("{email}"), employee.getEmailId())
								.replaceAll(Pattern.quote("{bckImg}"),
										String.valueOf(EnovationConfig.buddyConfig.get("ProfilePicPathUrl")
												+ emailTemplateImg.getFcmBody()))
								.replaceAll(Pattern.quote("{role}"), String.valueOf(role))
								.replaceAll(Pattern.quote("{new}"), newRemove)
								.replaceAll(Pattern.quote("{appLink}"), emailTemplateImg.getDesc())
								.replaceAll(Pattern.quote("{superAdminName}"), superAdmin)
								.replaceAll(Pattern.quote("{portalLink}"), prtalLink)
								.replaceAll(Pattern.quote("{roles}"), roles);

						// .replaceAll(Pattern.quote("{playstoreicon}"),String.valueOf(EnovationConfig.buddyConfig.get("ProfilePicPathUrl")+messageContent.getDesc()));
					}
					MailDTO mail=new MailDTO();
					mail.setToMail(employee.getEmailId());//employee.getEmailId()
					mail.setSubject(subject);
					mail.setContent(mailContent);
					mailList.add(mail);
					/*session.createNativeQuery(" update tbl_employee_details set is_notify=:isNotify where emp_id=:empId ")
							.setParameter("isNotify", EnovationConstants.ONE).setParameter("empId", employee.getEmpId())
							.executeUpdate();*/
					
			}
			taskExecutor.execute(new MultipleMailUtil(mailList));
			
		}catch(Exception e) {
			LOGGER.error("#INSIDE EXCEPTION OCCURED IN UPDATESETUPCOMLETED "+e.getMessage());
		}
		return flag;
	}
}
