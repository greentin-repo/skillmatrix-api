package com.greentin.enovation.utils;

import java.util.List;
import java.util.Properties;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import com.greentin.enovation.config.EnovationConfig;
import com.greentin.enovation.dto.MailDTO;

public class MultipleMailUtil implements Runnable {
	@Autowired
	EnovationConfig evonationConfig;


	private List<MailDTO> mlist;

	public MultipleMailUtil() {

	}
	public MultipleMailUtil(List<MailDTO> maillist) {
		
		this.mlist =maillist;
	}

	public void sendMail() {
		String email=null;
		try {
			//content=content.replaceAll(Pattern.quote("\n"));
			Session session = null;
			session = createSession();
			int cntr=1;
			System.out.println("Size="+mlist.size());
			for(MailDTO mail:mlist) {
				System.out.println("Current Counter="+cntr);
				Message message = new MimeMessage(session);
				message.addFrom(InternetAddress.parse(EnovationConstants.EMAILSENDERNAMEPLUSURL));
				message.setFrom(new InternetAddress(EnovationConfig.buddyConfig.get("mailID"),EnovationConstants.EMAILSENDERNAME));
			   	message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(mail.getToMail()));
				message.setSubject(mail.getSubject());
			    System.out.println("Mail Sending TO : "+mail.getToMail());
			    email=mail.getToMail();
		        message.setContent(mail.getContent(), "text/html; charset=utf-8");
				Transport.send(message);
				System.out.println("email sent=");
				cntr++;
			}
		}catch(MessagingException e) {
			System.out.println("Not Able to Send Mail="+email);
			e.printStackTrace();
			System.out.println(e.getMessage());
		}catch(Exception e) {
			System.out.println("Not Able to Send Mail="+email);
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
		return props;
	}

	@Override
	public void run() {
		sendMail();
	}

}
