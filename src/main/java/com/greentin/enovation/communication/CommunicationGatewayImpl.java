package com.greentin.enovation.communication;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;





@Component
public class CommunicationGatewayImpl extends CommunicationGateway {
	private static Logger logger = LoggerFactory.getLogger(CommunicationGatewayImpl.class);


	public boolean registerMessageInQueue(NotificationProcess message, String messageType) {
		try {
			if (messageType.equals(NotificationConstants.NOTIFICATION_MESSAGE_EMAIL)) {
				emailQueueNew.add(message);
				logger.info("Email Added  in queue and size is  :" + emailQueueNew.size());
			}else if (messageType.equals(NotificationConstants.NOTIFICATION_MESSAGE_SMS)) {
				smsQueueNew.add(message);
				logger.info("SMS Added in SMS Queue and size is:" + smsQueueNew.size());
			}else if(messageType.equals(NotificationConstants.NOTIFICATION_MESSAGE_NT)) {
				ntQueueNew.add(message);
				logger.info("Mobile Notification Added in Notification Queue and size is:" + ntQueueNew.size());
			}
			return true;
		} catch (Exception e){
			logger.info("Inside registerMessageInEmailQueue, exception block ");
			return false;
		}
	}

	public boolean registerMessageInEmailQueue(NotificationProcess message) {
		try {
			emailQueueNew.add(message);
			return true;
		} catch (Exception e) {
			logger.info("Inside registerMessageInEmailQueue, exception block ");
			return false;
		}
	}
	

	public NotificationProcess getMessageFromEmailQueue() {
		
		try {
			return emailQueueNew.poll();
		} catch (Exception e) {
			return null;
		}
	}
	
	public boolean registerMessageInSMSQueue(NotificationProcess message) {
		
		try {
			smsQueueNew.add(message);
			return true;
		} catch (Exception e) {
			return false;
		}
	}

	
	public NotificationProcess getMessageFromSMSQueue() {
		
		try {
			return smsQueueNew.poll();
		} catch (Exception e) {
			return null;
		}
	}
	
	public boolean registerMessageInNotificationQueue(NotificationProcess message) {
		try {
			ntQueueNew.add(message);
			return true;
		} catch (Exception e) {
			return false;
		}
	}

	public NotificationProcess getMessageFromNotificationQueue() {
		try {
			return ntQueueNew.poll();
		} catch (Exception e) {
			return null;
		}
	}	
	
}
