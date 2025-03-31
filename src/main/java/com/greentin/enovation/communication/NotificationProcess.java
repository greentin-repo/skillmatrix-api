package com.greentin.enovation.communication;

import java.sql.Timestamp;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

@Entity
public class NotificationProcess {
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private long   id;
	private long   notificationMessageId;
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
	
	public NotificationProcess() {
		
	}
	
	public NotificationProcess(NotificationMessage nm){
		this.notificationMessageId=nm.getId();
		this.notificationId=nm.getNotificationId();
		this.senderEmailId=nm.getSenderEmailId();
		this.receiverEmailId=nm.getReceiverEmailId();
		this.templateId=nm.getTemplateId();
		this.notificationType=nm.getNotificationType();
		this.notificationDestination=nm.getNotificationDestination();
		this.messageText=nm.getMessageText();
		this.language=nm.getLanguage();
		this.retryAttempts=nm.getRetryAttempts();
		this.receiverName=nm.getReceiverName();
		this.senderName=nm.getSenderName();
		this.templateName=nm.getTemplateName();
		this.messageContext=nm.getMessageContext();
		this.channel=nm.getChannel();
		this.creationApplication=nm.getCreationApplication();
		this.confirmation=nm.getConfirmation();
		this.status=nm.getStatus();
		this.orgId=nm.getOrgId();
		this.branchId=nm.getBranchId();
		this.attachmentPath=nm.getAttachmentPath();
		this.contactNumber=nm.getContactNumber();
	}
	
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public long getNotificationMessageId() {
		return notificationMessageId;
	}
	public void setNotificationMessageId(long notificationMessageId) {
		this.notificationMessageId = notificationMessageId;
	}
	public long getNotificationId() {
		return notificationId;
	}
	public void setNotificationId(long notificationId) {
		this.notificationId = notificationId;
	}
	public String getSenderEmailId() {
		return senderEmailId;
	}
	public void setSenderEmailId(String senderEmailId) {
		this.senderEmailId = senderEmailId;
	}
	
	public String getTemplateId() {
		return templateId;
	}
	public void setTemplateId(String templateId) {
		this.templateId = templateId;
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
	public String getMessageText() {
		return messageText;
	}
	public void setMessageText(String messageText) {
		this.messageText = messageText;
	}
	public String getLanguage() {
		return language;
	}
	public void setLanguage(String language) {
		this.language = language;
	}
	public int getRetryAttempts() {
		return retryAttempts;
	}
	public void setRetryAttempts(int retryAttempts) {
		this.retryAttempts = retryAttempts;
	}
	public String getReceiverName() {
		return receiverName;
	}
	public void setReceiverName(String receiverName) {
		this.receiverName = receiverName;
	}
	public String getSenderName() {
		return senderName;
	}
	public void setSenderName(String senderName) {
		this.senderName = senderName;
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
