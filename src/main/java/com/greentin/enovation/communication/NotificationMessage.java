package com.greentin.enovation.communication;

import java.sql.Date;
import java.sql.Timestamp;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

@Entity
public class NotificationMessage {

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private long   id;
	private long   notificationId;
	private String senderEmailId;
	private String receiverEmailId;
	private String templateId;
	private String notificationType;
	private String notificationDestination;
	
	@Lob
	private String messageText;
	private String language;
	private int   retryAttempts;	
	private String receiverName;
	private String senderName;
	private String templateName;
	private String messageContext;
	private String channel;
	private String creationApplication;
	@Lob
	private String confirmation;
	@CreationTimestamp
	private Timestamp createdDate;
	@UpdateTimestamp
	private Timestamp updatedDate;
	private String createdBy;
	private String updatedBy;
	private String status;
	private int orgId;
	private int branchId;
	private String attachmentPath;
	
	private String contactNumber;
	
	
	public NotificationMessage() {
		
	}
	
	//For Notification
	public NotificationMessage(String messageText,String notificationType,String status) {		
    	this.messageText=messageText;
    	this.notificationType=notificationType;
    	this.status=status;		
   }
	
	//For SMS
	public NotificationMessage(String contactNumber, String messageText,String notificationType,String status) {	
	    	this.contactNumber=contactNumber;
	    	this.messageText=messageText;
	    	this.notificationType=notificationType;
	    	this.status=status;		
	}
	 
    //For Email	
    public NotificationMessage(String receiverEmailId,String messageContext, String messageText,String attachmentPath,String notificationType,String status) {
    	
    	this.receiverEmailId=receiverEmailId;
    	this.messageContext=messageContext;
    	this.messageText=messageText;
    	this.attachmentPath=attachmentPath;
    	this.notificationType=notificationType;
    	this.status=status;	
    	
	}
	
	public NotificationMessage(long id, long notificationId,String templateId, String createdBy,
			String updatedBy, String status,String notificationType, String notificationDestination, String language, String messageText,
			int retryAttempts,String receiverName, String senderName,String templateName, String messageContext,String channel, String creationApplication,String contactNumber) {
		super();
		this.id = id;
		this.notificationId = notificationId;
		this.templateId = templateId;
		this.createdBy = createdBy;
		this.updatedBy = updatedBy;
		this.status = status;
		this.notificationType = notificationType;
		this.notificationDestination = notificationDestination;
		this.language = language;
		this.messageText = messageText;
		this.retryAttempts = retryAttempts;
		this.receiverName = receiverName;
		this.senderName = senderName;
		this.templateName = templateName;
		this.messageContext = messageContext;
		this.channel = channel;
		this.creationApplication = creationApplication;
		this.contactNumber=contactNumber;
	}


	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}


	public long getNotificationId() {
		return notificationId;
	}

	public void setNotificationId(long notificationId) {
		this.notificationId = notificationId;
	}

	public String getTemplateId() {
		return templateId;
	}

	public void setTemplateId(String templateId) {
		this.templateId = templateId;
	}

	public Timestamp getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(Timestamp createdDate) {
		this.createdDate = createdDate;
	}

	public Timestamp getUpdatedDate() {
		return updatedDate;
	}

	public void setUpdatedDate(Timestamp updatedDate) {
		this.updatedDate = updatedDate;
	}

	public String getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}

	public String getUpdatedBy() {
		return updatedBy;
	}

	public void setUpdatedBy(String updatedBy) {
		this.updatedBy = updatedBy;
	}
	
	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}
	
	public String getNotificationType() {
		return notificationType;
	}

	public void setNotificationType(String notificationType) {
		this.notificationType = notificationType;
	}

	public String getNotificationDestination() {
		return notificationDestination;
	}

	public void setNotificationDestination(String notificationDestination) {
		this.notificationDestination = notificationDestination;
	}

	public String getLanguage() {
		return language;
	}

	public void setLanguage(String language) {
		this.language = language;
	}

	public String getMessageText() {
		return messageText;
	}

	public void setMessageText(String messageText) {
		this.messageText = messageText;
	}

	public int getRetryAttempts() {
		return retryAttempts;
	}

	public void setRetryAttempts(int retryAttempts) {
		this.retryAttempts = retryAttempts;
	}

	public String getSenderName() {
		return senderName;
	}

	public void setSenderName(String senderName) {
		this.senderName = senderName;
	}

	public String getReceiverName() {
		return receiverName;
	}

	public void setReceiverName(String receiverName) {
		this.receiverName = receiverName;
	}

	public String getTemplateName() {
		return templateName;
	}

	public void setTemplateName(String templateName) {
		this.templateName = templateName;
	}

	public String getMessageContext() {
		return messageContext;
	}

	public void setMessageContext(String messageContext) {
		this.messageContext = messageContext;
	}

	public String getChannel() {
		return channel;
	}

	public void setChannel(String channel) {
		this.channel = channel;
	}

	public String getCreationApplication() {
		return creationApplication;
	}

	public void setCreationApplication(String creationApplication) {
		this.creationApplication = creationApplication;
	}

	public String getConfirmation() {
		return confirmation;
	}

	public void setConfirmation(String confirmation) {
		this.confirmation = confirmation;
	}

	public String getSenderEmailId() {
		return senderEmailId;
	}

	public void setSenderEmailId(String senderEmailId) {
		this.senderEmailId = senderEmailId;
	}

	public String getReceiverEmailId() {
		return receiverEmailId;
	}

	public void setReceiverEmailId(String receiverEmailId) {
		this.receiverEmailId = receiverEmailId;
	}

	public int getOrgId() {
		return orgId;
	}

	public void setOrgId(int orgId) {
		this.orgId = orgId;
	}

	public int getBranchId() {
		return branchId;
	}

	public void setBranchId(int branchId) {
		this.branchId = branchId;
	}

	public String getAttachmentPath() {
		return attachmentPath;
	}

	public void setAttachmentPath(String attachmentPath) {
		this.attachmentPath = attachmentPath;
	}

	public String getContactNumber() {
		return contactNumber;
	}

	public void setContactNumber(String contactNumber) {
		this.contactNumber = contactNumber;
	}
	
	
}
