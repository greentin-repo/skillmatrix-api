package com.greentin.enovation.communication;


import java.util.ArrayList;
import java.util.List;
import javax.transaction.Transactional;

import org.apache.commons.lang3.StringUtils;
import org.hibernate.Session;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.greentin.enovation.config.EnovationConfig;
import com.greentin.enovation.utils.BaseRepository;


@Component
@Transactional
public class CommunicationRegistrationManager extends BaseRepository {

	@Autowired
	CommunicationGatewayImpl notificationGatewayImpl;

	private static Logger logger = LoggerFactory.getLogger(CommunicationRegistrationManager.class);

	@SuppressWarnings("rawtypes")
	@Scheduled(initialDelay = 30000, fixedDelay = 20000)
	public void registerNotificationMessage() {
		
		try {
			
			String isEmailUp = EnovationConfig.buddyConfig.get(NotificationConstants.NOTIFICATION_EMAIL_COMMUNICATION_UP);
			String isMobNotificationUp =EnovationConfig.buddyConfig.get(NotificationConstants.NOTIFICATION_MNT_COMMUNICATION_UP);
			String isSmsUp =EnovationConfig.buddyConfig.get(NotificationConstants.NOTIFICATION_SMS_COMMUNICATION_UP);
			
			if(isEmailUp.equals(NotificationConstants.YES) || isMobNotificationUp.equals(NotificationConstants.YES) || isSmsUp.equals(NotificationConstants.YES) ) {
				
				Session session = getCurrentSession();
				 
				List<String> notificationTypes= new ArrayList<>();
				
				if(isEmailUp.equals(NotificationConstants.YES)) {
					notificationTypes.add(NotificationConstants.NOTIFICATION_MESSAGE_EMAIL);
				}
				
				if(isMobNotificationUp.equals(NotificationConstants.YES)) {
					notificationTypes.add(NotificationConstants.NOTIFICATION_MESSAGE_NT);
				}
				
				if(isSmsUp.equals(NotificationConstants.YES)) {
					notificationTypes.add(NotificationConstants.NOTIFICATION_MESSAGE_SMS);
				}
				
				List<NotificationMessage> newMessageList = fetchNewNotification(session,notificationTypes);
				
				if(!newMessageList.isEmpty()) {
					insertInNotificationProcess(session,newMessageList);
				}
				 
				List<NotificationProcess> messageListQueue=fetchFromNotificationProcess(session,NotificationConstants.NOTIFICATION_NEW_STATUS);
				for (int i = 0; i < messageListQueue.size(); i++) {
					NotificationProcess messageHolder =  messageListQueue.get(i);
					if (messageHolder.getNotificationType() != null){
						if(messageHolder.getNotificationType().equals(NotificationConstants.NOTIFICATION_MESSAGE_EMAIL)) {
							notificationGatewayImpl.registerMessageInQueue(messageHolder,NotificationConstants.NOTIFICATION_MESSAGE_EMAIL);
						}else if(messageHolder.getNotificationType().equals(NotificationConstants.NOTIFICATION_MESSAGE_SMS)) {
							notificationGatewayImpl.registerMessageInQueue(messageHolder,NotificationConstants.NOTIFICATION_MESSAGE_SMS);
						}else if(messageHolder.getNotificationType().equals(NotificationConstants.NOTIFICATION_MESSAGE_NT)) {
							notificationGatewayImpl.registerMessageInQueue(messageHolder,NotificationConstants.NOTIFICATION_MESSAGE_NT);
						}
					}
				}
			}
			
		}catch(Exception e){
			
		}
		
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	private List<NotificationMessage> fetchNewNotification(Session session,List<String> notificationTypes) {
		List<NotificationMessage> newMessageList = new ArrayList<NotificationMessage>();

		try {
			logger.info(" fetching new Notification from Notification Message and types are "+StringUtils.join(notificationTypes, ','));
			newMessageList=session.createQuery("from NotificationMessage where status=:status and notification_type IN (:notificationTypes)")
					.setParameter("status",NotificationConstants.NOTIFICATION_NEW_STATUS)
					.setParameter("notificationTypes",notificationTypes)
					.getResultList();
			for(NotificationMessage notificationMessage:newMessageList) {
				notificationMessage.setStatus(NotificationConstants.NOTIFICATION_READY_STATUS);
				session.update(notificationMessage);
			}
		}catch(Exception e){
			e.printStackTrace();
		}	
		return newMessageList;
	}
	
	
	private boolean insertInNotificationProcess(Session session,List<NotificationMessage> newMessageList) {
		
		logger.info("Insert into NotificationProcess table");
		boolean flag=false;
		try {
			if(!newMessageList.isEmpty()) {
				
				logger.info("NotificationMessage new Notification length :   "+newMessageList.size());
				
				for(NotificationMessage notificationMessage:newMessageList) {
					
					logger.info("New Notification Type : "+notificationMessage.getNotificationType()+" ApplicationCreation : "+notificationMessage.getCreationApplication());
					
					logger.info("Insert to notification Process Sender Emailid :"+notificationMessage.getSenderEmailId() +" Receiver Emailid :"+notificationMessage.getReceiverEmailId());
					
					NotificationProcess nProcess=new NotificationProcess(notificationMessage);
					nProcess.setStatus(NotificationConstants.NOTIFICATION_NEW_STATUS);
				    session.save(nProcess);
				}
			}else{
				logger.info("NotificationMessage new Notification length :  0 ");
			}
			flag=true;
		}catch(Exception e) {
			e.printStackTrace();
		}
		return flag;
	}
	
	@SuppressWarnings("unchecked")
	private List<NotificationProcess> fetchFromNotificationProcess(Session session,String statusType){
		
		logger.info("Fetch from NotificationProcess table for Notification type : "+statusType);
		List<NotificationProcess> nProcessList=new ArrayList<>();
		
		if(statusType.equals(NotificationConstants.NOTIFICATION_NEW_STATUS)){
			nProcessList=session.createQuery("from NotificationProcess where status=:status ")
					.setParameter("status",NotificationConstants.NOTIFICATION_NEW_STATUS)
					.setMaxResults(30)
					.getResultList();
			
			for(NotificationProcess nProcess:nProcessList) {
				nProcess.setStatus(NotificationConstants.NOTIFICATION_INPROCESS_STATUS);
				session.update(nProcess);
			}
		}else if(statusType.equals(NotificationConstants.NOTIFICATION_RETRY_STATUS)) {
			
			nProcessList=session.createNativeQuery(" select * from notification_process where status=:status and  created_date > (NOW()- INTERVAL 1 DAY) and retry_attempts< :retryAttempts ")
					.setParameter("status",NotificationConstants.NOTIFICATION_RETRY_STATUS)
					.setParameter("retryAttempts",3)
					.addEntity(NotificationProcess.class)
					.setMaxResults(30)
					.getResultList();
			for(NotificationProcess nProcess:nProcessList) {
				nProcess.setStatus(NotificationConstants.NOTIFICATION_INPROCESS_STATUS);
				session.update(nProcess);
			}
		}
		logger.info("New NotificationProcess table length : "+nProcessList.size());
		
		return nProcessList;
	}
	
	@SuppressWarnings("rawtypes")
	@Scheduled(initialDelay = 60000, fixedDelay = 10000)
	public void registerNotificationMessageForRetry() {
		
		String isEmailUp = EnovationConfig.buddyConfig.get(NotificationConstants.NOTIFICATION_EMAIL_COMMUNICATION_UP);
		String isMobNotificationUp =EnovationConfig.buddyConfig.get(NotificationConstants.NOTIFICATION_MNT_COMMUNICATION_UP);
		String isSmsUp =EnovationConfig.buddyConfig.get(NotificationConstants.NOTIFICATION_SMS_COMMUNICATION_UP);
		
		if(isEmailUp.equals(NotificationConstants.YES) || isMobNotificationUp.equals(NotificationConstants.YES) || isSmsUp.equals(NotificationConstants.YES) ) {
			
			Session session = getCurrentSession();
			List<NotificationProcess> messageListQueue=fetchFromNotificationProcess(session,NotificationConstants.NOTIFICATION_RETRY_STATUS);
			for (int i = 0; i < messageListQueue.size(); i++) {
				NotificationProcess messageHolder =  messageListQueue.get(i);
				if (messageHolder.getNotificationType() != null){
					if(messageHolder.getNotificationType().equals(NotificationConstants.NOTIFICATION_MESSAGE_EMAIL)) {
						notificationGatewayImpl.registerMessageInQueue(messageHolder,NotificationConstants.NOTIFICATION_MESSAGE_EMAIL);
					}else if(messageHolder.getNotificationType().equals(NotificationConstants.NOTIFICATION_MESSAGE_SMS)) {
						notificationGatewayImpl.registerMessageInQueue(messageHolder,NotificationConstants.NOTIFICATION_MESSAGE_SMS);
					}else if(messageHolder.getNotificationType().equals(NotificationConstants.NOTIFICATION_MESSAGE_NT)) {
						notificationGatewayImpl.registerMessageInQueue(messageHolder,NotificationConstants.NOTIFICATION_MESSAGE_NT);
					}
				}
			}
		}
	}

	
}
