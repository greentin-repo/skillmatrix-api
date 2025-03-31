package com.greentin.enovation.communication;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.security.GeneralSecurityException;
import java.util.Collections;
import java.util.List;
import java.util.Properties;
import java.util.concurrent.Callable;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.Session;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

import org.apache.commons.lang3.exception.ExceptionUtils;

import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.extensions.java6.auth.oauth2.AuthorizationCodeInstalledApp;
import com.google.api.client.extensions.jetty.auth.oauth2.LocalServerReceiver;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.client.util.Base64;
import com.google.api.client.util.store.FileDataStoreFactory;
import com.google.api.services.gmail.Gmail;
import com.google.api.services.gmail.GmailScopes;
import com.google.api.services.gmail.model.Message;
import com.greentin.enovation.config.EnovationConfig;



public class CommOAuthEmailWorker implements Callable<NotificationProcess>{

	private static final String APPLICATION_NAME = "Gmail API Java Quickstart";
    private static final JsonFactory JSON_FACTORY = JacksonFactory.getDefaultInstance();
    private static final String TOKENS_DIRECTORY_PATH =EnovationConfig.buddyConfig.get("emailTokenPath");//"/Users/rakesh/myeNovtokens";
    private static final List<String> SCOPES = Collections.singletonList(GmailScopes.GMAIL_SEND);
    private static final String CREDENTIALS_FILE_PATH = "/credentials-enov.json";
    
    public NotificationProcess notificationMessage;
	
	public CommOAuthEmailWorker(NotificationProcess notificationMessage) {
	     this.notificationMessage=notificationMessage;  
	}
	
    
    private static Credential getCredentials(final NetHttpTransport HTTP_TRANSPORT) throws IOException {
        // Load client secrets.
        InputStream in = CommOAuthEmailWorker.class.getResourceAsStream(CREDENTIALS_FILE_PATH);
        if (in == null) {
            throw new FileNotFoundException("Resource not found: " + CREDENTIALS_FILE_PATH);
        }
        GoogleClientSecrets clientSecrets = GoogleClientSecrets.load(JSON_FACTORY, new InputStreamReader(in));

        // Build flow and trigger user authorization request.
        GoogleAuthorizationCodeFlow flow = new GoogleAuthorizationCodeFlow.Builder(
                HTTP_TRANSPORT, JSON_FACTORY, clientSecrets, SCOPES)
                .setDataStoreFactory(new FileDataStoreFactory(new java.io.File(TOKENS_DIRECTORY_PATH)))
                .setAccessType("offline")
                .build();
        LocalServerReceiver receiver = new LocalServerReceiver.Builder().setPort(8888).build();
        return new AuthorizationCodeInstalledApp(flow, receiver).authorize("user");
    }
	
	
	  public static MimeMessage createEmail(String to, String from,String subject, String bodyText)throws MessagingException {
			Properties props = new Properties();
			Session session = Session.getDefaultInstance(props, null);
			System.out.println("Mail Sending To : " +to);
			MimeMessage email = new MimeMessage(session);
			email.addFrom(InternetAddress.parse("Team myeNovation<no-reply@myenovation.com>"));
			try {
				email.setFrom(new InternetAddress(from,"Team myeNovation"));
			} catch (UnsupportedEncodingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			email.addRecipient(javax.mail.Message.RecipientType.TO,
			new InternetAddress(to));
			email.setSubject(subject);
			//email.setText(bodyText);
			email.setContent(bodyText,"text/html; charset=utf-8"); 
			return email;
	   }
		
	   public static MimeMessage createEmail(String to,String from,String subject, String bodyText,File file)throws MessagingException, IOException {
				Properties props = new Properties();
				Session session = Session.getDefaultInstance(props, null);
				 
				MimeMessage email = new MimeMessage(session);
				email.addFrom(InternetAddress.parse("Team myeNovation<no-reply@myenovation.com>"));
				email.setFrom(new InternetAddress(from,"Team myeNovation"));
				email.addRecipient(javax.mail.Message.RecipientType.TO,
				new InternetAddress(to));
				email.setSubject(subject);
				
				MimeBodyPart mimeBodyPart = new MimeBodyPart();
				mimeBodyPart.setContent(bodyText, "text/html; charset=utf-8");
				
				Multipart multipart = new MimeMultipart();
				multipart.addBodyPart(mimeBodyPart);
				
				mimeBodyPart = new MimeBodyPart();
				DataSource source = new FileDataSource(file);
				
				mimeBodyPart.setDataHandler(new DataHandler(source));
				mimeBodyPart.setFileName(file.getName());
				
				multipart.addBodyPart(mimeBodyPart);
				email.setContent(multipart);
				
				return email;
       }
	  
		public static Message createMessageWithEmail(MimeMessage emailContent)throws MessagingException, IOException {
	        ByteArrayOutputStream buffer = new ByteArrayOutputStream();
	        emailContent.writeTo(buffer);
	        byte[] bytes = buffer.toByteArray();
	        String encodedEmail = Base64.encodeBase64URLSafeString(bytes);
	        Message message = new Message();
	        message.setRaw(encodedEmail);
	        return message;
	    }
		
		
		 public static Message sendMessage(Gmail service, String userId, MimeMessage emailContent)throws MessagingException, IOException {
			Message message = createMessageWithEmail(emailContent);
			message = service.users().messages().send(userId, message).execute();
			
			System.out.println("Message id: " + message.getId());
			System.out.println(message.toPrettyString());
			
			
			return message;
		}
		 
		public void InitiateMailSend()throws IOException, GeneralSecurityException {
			
			boolean flag=false;
	        try {
	        	 final NetHttpTransport HTTP_TRANSPORT = GoogleNetHttpTransport.newTrustedTransport();
	             Gmail service = new Gmail.Builder(HTTP_TRANSPORT, JSON_FACTORY, getCredentials(HTTP_TRANSPORT)).setApplicationName(APPLICATION_NAME).build();
	             System.out.println("on main function");
	             MimeMessage mimeMessage=null;
	             if(notificationMessage.getAttachmentPath()!=null) {
	            	 File file =new File(notificationMessage.getAttachmentPath());
					 mimeMessage= createEmail(notificationMessage.getReceiverEmailId(),EnovationConfig.buddyConfig.get("mailID"),notificationMessage.getMessageContext(), notificationMessage.getMessageText(),file);
	             }else{
	            	 mimeMessage= createEmail(notificationMessage.getReceiverEmailId(),EnovationConfig.buddyConfig.get("mailID"),notificationMessage.getMessageContext(), notificationMessage.getMessageText());
	             }
	             
				 Message responseMessage=sendMessage(service,EnovationConfig.buddyConfig.get("mailID"),mimeMessage);
				 System.out.println("Email Response "+responseMessage.getRaw());
				 notificationMessage.setConfirmation(responseMessage.toPrettyString());
				 flag=true;
			}catch(MessagingException e) {
				System.out.println(ExceptionUtils.getStackTrace(e));
				System.out.println(e.getMessage());
				notificationMessage.setConfirmation(e.getMessage());
			}
	   
	        if(flag){
				notificationMessage.setStatus(NotificationConstants.SUCCESS);
			}else if (notificationMessage.getRetryAttempts() < (3-1)) {
				notificationMessage.setStatus(NotificationConstants.NOTIFICATION_RETRY_STATUS);
				notificationMessage.setRetryAttempts(notificationMessage.getRetryAttempts() + 1);
			}else {
				notificationMessage.setStatus(NotificationConstants.FAIL);
			}
	        
		}
		
		

		@Override
		public NotificationProcess call()  {
			long threadId = Thread.currentThread().getId();
			System.out.println("**START*** of Thread Id: " + threadId);
			
			try{
				InitiateMailSend();
			}catch(Exception e) {
				notificationMessage.setConfirmation(e.getMessage());
				notificationMessage.setRetryAttempts(notificationMessage.getRetryAttempts() + 1);
				notificationMessage.setStatus(NotificationConstants.FAIL);
			}
			
			System.out.println("**END*** of Thread Id: " + threadId);
			return notificationMessage;
		}
    
}
