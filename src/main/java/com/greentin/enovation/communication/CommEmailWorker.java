package com.greentin.enovation.communication;

import java.io.File;
import java.util.Properties;
import java.util.concurrent.Callable;

import javax.mail.Message;
import javax.mail.Multipart;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

import org.apache.commons.lang3.exception.ExceptionUtils;

import com.greentin.enovation.config.EnovationConfig;


public class CommEmailWorker implements Callable<NotificationProcess> {
   
	
	public NotificationProcess notificationMessage;
	
	public CommEmailWorker(NotificationProcess notificationMessage) {
	     this.notificationMessage=notificationMessage;  
	}
	
	public void sendMail() {
		boolean flag=false;
		
		try {
			Session session = null;
			session = createSession();
			Message message = new MimeMessage(session);
			message.addFrom(InternetAddress.parse("Team myeNovation<no-reply@myenovation.com>"));
			message.setFrom(new InternetAddress( EnovationConfig.buddyConfig.get("mailID"),"Team myeNovation"));
			message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(notificationMessage.getReceiverEmailId()));
			message.setSubject(notificationMessage.getMessageContext());
			System.out.println("Mail Sending TO : "+notificationMessage.getReceiverEmailId());
			
			if(notificationMessage.getAttachmentPath()!=null){
				Multipart multipart = new MimeMultipart();
				MimeBodyPart textBodyPart = new MimeBodyPart();
				textBodyPart.setContent(notificationMessage.getMessageText(), "text/html; charset=utf-8");
				MimeBodyPart attachmentBodyPart= new MimeBodyPart();
				attachmentBodyPart.attachFile(new File(notificationMessage.getAttachmentPath()));
				System.out.println("The file name is ="+attachmentBodyPart.getFileName());
				multipart.addBodyPart(textBodyPart);  
				multipart.addBodyPart(attachmentBodyPart );
				message.setContent(multipart);
			}else{
				message.setContent(notificationMessage.getMessageText(), "text/html; charset=utf-8");
			}
			Transport.send(message);
			notificationMessage.setConfirmation(NotificationConstants.MESSAGE_SENT);
			System.out.println("email sent");
			flag=true;	
		}catch(Exception e) {
			System.out.println(ExceptionUtils.getStackTrace(e));
			System.out.println(e.getMessage());
			notificationMessage.setConfirmation(e.getMessage());
		}
		if(flag){
			notificationMessage.setStatus(NotificationConstants.SUCCESS);
		}else {
			notificationMessage.setStatus(NotificationConstants.FAIL);
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
		props.put("mail.smtp.from",  EnovationConfig.buddyConfig.get("mailID"));
		/*ADDITIONAL ADDED*/
		props.put("mail.smtps.ssl.checkserveridentity", "false");
		props.put("mail.smtps.ssl.trust", "*");
		props.put("mail.transport.protocol", "smtps");
		return props;
	}
	
	
	@Override
	public NotificationProcess call() throws Exception {
		long threadId = Thread.currentThread().getId();
		System.out.println("**START*** of Thread Id: " + threadId);
		sendMail();
		System.out.println("**END*** of Thread Id: " + threadId);
		return notificationMessage;
	}

}
