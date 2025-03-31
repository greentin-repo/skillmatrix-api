package com.greentin.enovation.utils;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Properties;
import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.mail.BodyPart;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import javax.mail.util.ByteArrayDataSource;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.apache.commons.lang.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.json.GsonJsonParser;
import com.google.gson.JsonObject;
import com.greentin.enovation.communication.ICommunication;
import com.greentin.enovation.communication.NotificationConstants;
import com.greentin.enovation.communication.NotificationMessage;
import com.greentin.enovation.config.EnovationConfig;
import com.greentin.enovation.dto.MailDTO;
import com.greentin.enovation.master.MasterDaoImpl;
import com.greentin.enovation.model.EmployeeDetails;
/*
import net.fortuna.ical4j.model.Calendar;
import net.fortuna.ical4j.model.PropertyList;
import net.fortuna.ical4j.model.component.VEvent;
import net.fortuna.ical4j.model.property.Clazz;
import net.fortuna.ical4j.model.property.Method;
import net.fortuna.ical4j.model.property.Priority;
import net.fortuna.ical4j.model.property.Version;*/

@SuppressWarnings("unused")
public class MailUtil implements Runnable {
	private static final Logger LOGGER = LoggerFactory.getLogger(MailUtil.class);


	@Autowired
	EnovationConfig evonationConfig;
	
	private ICommunication communication;

	
	private List<MailDTO> mlist = new ArrayList<>();
	private String toMail;
	private String subject;
	private String content;
	private String attachmentPath;
	
	private String mailType="";

	public MailUtil() {

	}

	public MailUtil(String toMail, String subject,String content,ICommunication communication) {
		this.toMail =toMail;
		this.subject =subject;
		this.content =content;
		this.communication=communication;
		mailType="Single";
	}
	


	public void  setContent(String toMail, String subject,String content,ICommunication communication) {
		this.toMail=toMail;
		this.subject=subject;
		this.content=content;
		this.communication=communication;
		mailType="Single";
	}


	public MailUtil(String toMail, String subject, String content, String attachmentPath,ICommunication communication) {
		this.toMail = toMail;
		this.subject = subject;
		this.content = content;
		this.attachmentPath = attachmentPath;
		this.communication=communication;
		mailType="attachment";
	}

	public MailUtil(List<MailDTO> maillist,ICommunication communication) {
		this.mlist =maillist;
		this.communication=communication;
		mailType="list";
	}
	
	public void sendMail() {
		
		LOGGER.info("Storing Mail Into Notification Message Table ");
		try {
			if(mailType.equals("list")) {
				for(MailDTO mail:mlist) {
					LOGGER.info("Initiating  mail for "+mail.getToMail());
					communication.saveNotificationMessage(new NotificationMessage(mail.getToMail(),mail.getSubject(),mail.getContent(),mail.getAttachmentPath(),NotificationConstants.NOTIFICATION_MESSAGE_EMAIL,NotificationConstants.NOTIFICATION_NEW_STATUS));	
				}
			}else if(mailType.equals("attachment") || mailType.equals("Single")) {
				communication.saveNotificationMessage(new NotificationMessage(toMail,subject,content,attachmentPath,NotificationConstants.NOTIFICATION_MESSAGE_EMAIL,NotificationConstants.NOTIFICATION_NEW_STATUS));
			}
			
		}catch(Exception e){
			LOGGER.info("Exception Occur in Send Mail for ");
			e.printStackTrace();
			//LOGGER.info(ExceptionUtils.getStackTrace(e));
		}
	}
	
	public void sendMail1() {
		try {
			//content=content.replaceAll(Pattern.quote("\n"));
			Session session = null;
			session = createSession();
			if(mlist != null && mlist.size() > 0) {
				int cntr=1;
				System.out.println("Size="+mlist.size());
				for(MailDTO mail:mlist) {
					System.out.println("Current Counter="+cntr);
					System.out.println("to mail is :"+mail.getToMail());
					if(mail.getToMail() != null) {
						Message message = new MimeMessage(session);
						message.addFrom(InternetAddress.parse(EnovationConstants.EMAILSENDERNAMEPLUSURL));
						message.setFrom(new InternetAddress(EnovationConfig.buddyConfig.get("mailID"),EnovationConstants.EMAILSENDERNAME));
						message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(mail.getToMail()));
						message.setSubject(mail.getSubject());
						System.out.println("Mail Sending TO : "+mail.getToMail());
						if( mail.getAttachmentPath() != null) {
							Multipart multipart = new MimeMultipart();
							MimeBodyPart textBodyPart = new MimeBodyPart();
							textBodyPart.setContent(mail.getContent(), "text/html; charset=utf-8");
							MimeBodyPart attachmentBodyPart= new MimeBodyPart();
							attachmentBodyPart.attachFile(new File(mail.getAttachmentPath()));
							//attachmentBodyPart.setFileName("test"); 
							System.out.println("The file name is ="+attachmentBodyPart.getFileName());
							multipart.addBodyPart(textBodyPart);  // add the text part
							multipart.addBodyPart(attachmentBodyPart ); // add the attachement part
							message.setContent(multipart);
						}else {
							/*Calendar calendar = new Calendar();
							PropertyList calendarProperties = calendar.getProperties();
							calendarProperties.add(Version.VERSION_2_0);
							calendarProperties.add(Method.REQUEST);
							// other properties ...
                            Date d =new Date();
							VEvent vEvent = new VEvent();
							PropertyList vEventProperties = vEvent.getProperties();
							vEventProperties.add(Priority.MEDIUM);
							vEventProperties.add(Clazz.PUBLIC);
							// other properties ...

							calendar.getComponents().add(vEvent);

							message.setDataHandler(new DataHandler(new ByteArrayDataSource(calendar.toString(), "text/calendar")));
							*/
							message.setContent(mail.getContent(), "text/html; charset=utf-8");
						}

						Transport.send(message);
						System.out.println("email sent");	
						cntr++;
					}}//list end
			}else {
				Message message = new MimeMessage(session);
				message.addFrom(InternetAddress.parse(EnovationConstants.EMAILSENDERNAMEPLUSURL));
				message.setFrom(new InternetAddress(EnovationConfig.buddyConfig.get("mailID"),EnovationConstants.EMAILSENDERNAME));
				message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(toMail));
				message.setSubject(subject);
				System.out.println("Mail Sending TO : "+toMail);
				if( attachmentPath!=null) {
					Multipart multipart = new MimeMultipart();
					MimeBodyPart textBodyPart = new MimeBodyPart();
					textBodyPart.setContent(content, "text/html; charset=utf-8");
					MimeBodyPart attachmentBodyPart= new MimeBodyPart();
					attachmentBodyPart.attachFile(new File(attachmentPath));
					//attachmentBodyPart.setFileName("test"); 
					System.out.println("The file name is ="+attachmentBodyPart.getFileName());
					multipart.addBodyPart(textBodyPart);  // add the text part
					multipart.addBodyPart(attachmentBodyPart ); // add the attachement part
					message.setContent(multipart);
				}else {
					message.setContent(content, "text/html; charset=utf-8");
				}
				try {   
					Transport.send(message);
					System.out.println("email sent");
				}catch(Exception e) {
					System.out.println("Not Able to Send Mail="+toMail);
					e.printStackTrace();
					//LOGGER.error(ExceptionUtils.getStackTrace(e));
					//System.out.println(e.getMessage());
				}
			}

		

		}catch(Exception e) {
			System.out.println("Not Able to Send Mail="+toMail);
			//LOGGER.error(ExceptionUtils.getStackTrace(e));
			e.printStackTrace();
			System.out.println(e.getMessage());
		}

	}

	private static Session createSession() {
		Properties props = getProperties();
		Session session = Session.getInstance(props, new javax.mail.Authenticator() {
			protected PasswordAuthentication getPasswordAuthentication() {
				return new PasswordAuthentication(EnovationConfig.buddyConfig.get("mailID"),EnovationConfig.buddyConfig.get("mailPass"));
			}
		});
		return session;
	}

	private static Properties getProperties() {
		Properties props = new Properties();
		props.put("mail.smtp.auth", "true");
		props.put("mail.smtp.starttls.enable", "true");
		/*props.put("mail.debug", "true");*/
		props.put("mail.smtp.host", "smtp.gmail.com");
		props.put("mail.smtp.port", "587");	
		props.put("mail.smtp.from", EnovationConfig.buddyConfig.get("mailID"));
		/*ADDITIONAL ADDED*/
		props.put("mail.smtps.ssl.checkserveridentity", "false");
		props.put("mail.smtps.ssl.trust", "*");
		props.put("mail.transport.protocol", "smtps");
		return props;
	}

	@Override
	public void run() {
		sendMail();
	}

}