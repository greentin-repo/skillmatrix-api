package com.greentin.enovation.communication;

public class NotificationConstants {

	public final static String YES = "Y";
	public final static String NO = "N";
	
	public final static String PENDING = "PENDING";
	public final static String SUCCESS = "SUCCESS";
	public final static String FAIL = "FAIL";
	public final static String MESSAGE_SENT = "Message Sent";
	
	public final static String ISACCEPTED = "Y";
	public final static String NOTIFICATION_STATUS_ACCEPTED = "AC";
	public final static String NOTIFICATION_STATUS_NEW = "NE";
	public final static String NOTIFICATION_STATUS_READY = "RE";
	public final static String NOTIFICATION_STATUS_COMPLETED = "CO";
	public final static String NOTIFICATION_STATUS_RETRY = "RT";
	public final static String NOTIFICATION_STATUS_FAILED = "FL";
	public final static String NOTIFICATION_STATUS_PARTIALLYFAILED = "PF";
	public final static String NOTIFICATION_STATUS_PROCESSED = "PR";

	public final static String NOTIFICATION_MESSAGE_GROUPMESSAGE = "GM";
	public final static String NOTIFICATION_MESSAGE_INPROCESS = "IP";
	public final static String NOTIFICATION_RETURN_SMS = "RS";

	public final static String NOTIFICATION_MESSAGE_EMAIL = "EM";
	public final static String NOTIFICATION_MESSAGE_SMS = "SM";
	public final static String NOTIFICATION_MESSAGE_NT = "NT";

	public final static String NOTIFICATION_NEW_STATUS = "NEW";
	public final static String NOTIFICATION_READY_STATUS = "READY";
	public final static String NOTIFICATION_RETRY_STATUS = "RETRY";
	public final static String NOTIFICATION_INPROCESS_STATUS = "INPROCESS";
	
	
	public final static String NOTIFICATION_APP_DB_QUERY_NEW = "NE";
	public final static String NOTIFICATION_APP_DB_QUERY_RETRY = "RT";
	public final static String NOTIFICATION_APP_DB_QUERY_INPROCESS = "RT";
	
	public final static int NOTIFICATOIN_MAX_RETRY = 4;



	
	public final static String NOTIFICATION_EMAIL_COMMUNICATION_UP = "isEmailCommunicationUp";
	public final static String NOTIFICATION_SMS_COMMUNICATION_UP = "isSMSCommunicationUp";
	public final static String NOTIFICATION_MNT_COMMUNICATION_UP = "isMNTCommunicationUp";
	public final static String NOTIFICATION_REFRESH_SYSTEM_CONFIGRATION = "refreshSystemConfiguration";
	public final static String THREADTIMEOUT="threadTimeOut";

	public final static String NOTIFICATION_RESPONSE_STATUS_SUCCESS = "{\"status\":\"Success\"}";
	public final static String NOTIFICATION_RESPONSE_STATUS_FAILED = "{\"status\":\"Failed\"}";

}
