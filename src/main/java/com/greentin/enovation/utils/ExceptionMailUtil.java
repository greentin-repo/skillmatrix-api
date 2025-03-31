package com.greentin.enovation.utils;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import javax.mail.Message;
import javax.mail.Multipart;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

import org.apache.commons.lang.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.greentin.enovation.config.EnovationConfig;
import com.greentin.enovation.dto.MailDTO;

public class ExceptionMailUtil implements Runnable {
	/**
	 * Author-Rakesh
	 * Send Mail to Developer , if exception occur in Code
	 * */
	private static final Logger LOGGER = LoggerFactory.getLogger(ExceptionMailUtil.class);
	
	@Autowired
	EnovationConfig evonationConfig;
	private List<MailDTO> mlist = new ArrayList<>();
	private String toMail;
	private String subject;
	private String content;
	private String attachmentPath;

	public ExceptionMailUtil() {

	}

	public ExceptionMailUtil(String toMail, String subject,String content) {
		this.toMail =toMail;
		this.subject =subject;
		this.content =content;
	}

	public void  setContent(String toMail, String subject,String content) {
		this.toMail=toMail;
		this.subject=subject;
		this.content=content;
	}


	public ExceptionMailUtil(String toMail, String subject, String content, String attachmentPath) {
		this.toMail = toMail;
		this.subject = subject;
		this.content = content;
		this.attachmentPath = attachmentPath;
	}

	public ExceptionMailUtil(List<MailDTO> maillist) {

		this.mlist =maillist;
	}
	public void sendMail() {
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
						message.setFrom(new InternetAddress(EnovationConfig.buddyConfig.get("developerMailID"),EnovationConstants.EMAILSENDERNAME));
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
							message.setContent(mail.getContent(), "text/html; charset=utf-8");
						}

						Transport.send(message);
						System.out.println("email sent");	
						cntr++;
					}}//list end
			}else {
				Message message = new MimeMessage(session);
				message.addFrom(InternetAddress.parse(EnovationConstants.EMAILSENDERNAMEPLUSURL));
				message.setFrom(new InternetAddress(EnovationConfig.buddyConfig.get("developerMailID"),EnovationConstants.EMAILSENDERNAME));
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
					//e.printStackTrace();
					//LOGGER.error(ExceptionUtils.getStackTrace(e));
					//System.out.println(e.getMessage());
				}
			}
		}catch(Exception e) {
			System.out.println("Not Able to Send Mail="+toMail);
			LOGGER.error(ExceptionUtils.getStackTrace(e));
			//e.printStackTrace();
			//System.out.println(e.getMessage());
		}
	}
	private static Session createSession() {
		Properties props = getProperties();
		Session session = Session.getInstance(props, new javax.mail.Authenticator() {
			protected PasswordAuthentication getPasswordAuthentication() {
				return new PasswordAuthentication(EnovationConfig.buddyConfig.get("developerMailID"),EnovationConfig.buddyConfig.get("developerMailPass"));
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
		props.put("mail.smtp.from", EnovationConfig.buddyConfig.get("developerMailID"));
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
