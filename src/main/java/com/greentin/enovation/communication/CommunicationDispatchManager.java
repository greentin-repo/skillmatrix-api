package com.greentin.enovation.communication;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

import javax.transaction.Transactional;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.hibernate.Session;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.greentin.enovation.config.EnovationConfig;
import com.greentin.enovation.utils.BaseRepository;

@Component
@Transactional
public class CommunicationDispatchManager  extends BaseRepository {

	private static Logger logger = LoggerFactory.getLogger(CommunicationDispatchManager.class);

	ExecutorService executor = Executors.newFixedThreadPool(50);

	List<Future<NotificationProcess>> EMSentlist = new ArrayList<Future<NotificationProcess>>(); 
	List<Future<NotificationProcess>> SMSentlist = new ArrayList<Future<NotificationProcess>>(); 
	List<Future<NotificationProcess>> NTSentlist = new ArrayList<Future<NotificationProcess>>(); 

	@Scheduled(initialDelay=30000, fixedDelayString="${communication.EMScheduler.fixedDelayTime}")
	public void pollEmailQueue() {
		String isEmailUp = EnovationConfig.buddyConfig.get(NotificationConstants.NOTIFICATION_EMAIL_COMMUNICATION_UP);
		int threadTimeOutInSeconds=Integer.parseInt(EnovationConfig.buddyConfig.get(NotificationConstants.THREADTIMEOUT));
		
		if ((isEmailUp != null) && isEmailUp.equals(NotificationConstants.YES)){
			Session session = getCurrentSession();
			try{
				logger.info("pull the email from q and check emailQ size : "+ CommunicationGatewayImpl.emailQueueNew.size());
				int queueSize = CommunicationGatewayImpl.emailQueueNew.size();
				for (int i = 0; i < queueSize; i++) {
					Callable<NotificationProcess> callable = new CommSPEmailWorker(CommunicationGatewayImpl.emailQueueNew.poll());
					Future<NotificationProcess> future = executor.submit(callable);
					future.get(threadTimeOutInSeconds, TimeUnit.SECONDS); 
					EMSentlist.add(future);
				}
		     	Iterator<Future<NotificationProcess>> futureList=EMSentlist.iterator();
				while (futureList.hasNext()) {
					Future<NotificationProcess> future = (Future<NotificationProcess>) futureList.next();
					if(future.isDone()){
						try{
							// update notification_process table
							String sql = "update notification_process set status=:status, retry_attempts=:retryAttempts,confirmation=:confirmation where id =:id";
							session.createNativeQuery(sql)
							       .setParameter("status",future.get().getStatus())
							       .setParameter("retryAttempts",future.get().getRetryAttempts())
							       .setParameter("confirmation",future.get().getConfirmation())
							       .setParameter("id",future.get().getId())
							       .executeUpdate();
							if(future.get().getStatus()!=null && future.get().getStatus().equals(NotificationConstants.SUCCESS)) {
								
								session.createNativeQuery("update notification_process set message_text='' where id =:id")
								.setParameter("id",future.get().getId())
								.executeUpdate();
								
								if(future.get().getNotificationMessageId()>0) {
									session.createNativeQuery("update notification_message set message_text='' where id =:id")
									.setParameter("id",future.get().getNotificationMessageId())
									.executeUpdate();
								}
							}
							futureList.remove();
							logger.info("future object removed :" + future +" , EMSentlist.size() : "+EMSentlist.size());	
						}catch (Exception e) {
							e.printStackTrace();
							logger.info("Exception  "+e);
						}
					}
				}
			}catch (Exception e) {
				e.printStackTrace();
			}
		}else {
			logger.info("Email Communication is not up..");
		}
	}
	
	
	@Scheduled(initialDelay=30000, fixedDelayString="${communication.SMScheduler.fixedDelayTime}")
	public void pollSMSQueue() {
		int threadTimeOutInSeconds=Integer.parseInt(EnovationConfig.buddyConfig.get(NotificationConstants.THREADTIMEOUT));

		String isSmsUp =EnovationConfig.buddyConfig.get(NotificationConstants.NOTIFICATION_SMS_COMMUNICATION_UP);
		if ((isSmsUp != null) && isSmsUp.equals(NotificationConstants.YES)) {
			Session session = getCurrentSession();
			try {
				logger.info("pull the SMS Q,  invoke SMS client until SMSQueue size : "+ CommunicationGatewayImpl.smsQueueNew.size());
				int queueSize = CommunicationGatewayImpl.smsQueueNew.size();
			
				for (int i = 0; i < queueSize; i++) {
					Callable<NotificationProcess> callable = new CommSMSWorker(CommunicationGatewayImpl.smsQueueNew.poll());
					Future<NotificationProcess> future = executor.submit(callable);
					future.get(threadTimeOutInSeconds, TimeUnit.SECONDS); 
					SMSentlist.add(future);
				}

				Iterator<Future<NotificationProcess>> futureList=SMSentlist.iterator();

				while (futureList.hasNext()) {
					Future<NotificationProcess> future = (Future<NotificationProcess>) futureList.next();
					if (future.isDone()){
						try{
							String sql = "update notification_process set status=:Status, retry_attempts=:retryAttempts,confirmation=:Confirmation where id =:id  and notification_type=:notificationType";
							session.createNativeQuery(sql)
							       .setParameter("Status", future.get().getStatus())
							       .setParameter("retryAttempts", future.get().getRetryAttempts())
							       .setParameter("Confirmation",future.get().getConfirmation())
							       .setParameter("id",future.get().getId())
							       .setParameter("notificationType",future.get().getNotificationType())
							       .executeUpdate();
							futureList.remove();
							logger.info("future object removed :" + future +" , SMSentlist.size() : "+SMSentlist.size());
						}catch (Exception e){
							logger.info(ExceptionUtils.getStackTrace(e));
							logger.info("Exception  "+e);
						}
					}
				}
			}catch(Exception e) {
				e.printStackTrace();
			}
		}else{
			logger.info("SMS communciation is not up .....");
		}
	}
	
	
	@Scheduled(initialDelay=30000, fixedDelayString="${communication.NTScheduler.fixedDelayTime}")
	public void pollMobileNotificationQueue() {
		int threadTimeOutInSeconds=Integer.parseInt(EnovationConfig.buddyConfig.get(NotificationConstants.THREADTIMEOUT));

		String isMobNotificationUp =EnovationConfig.buddyConfig.get(NotificationConstants.NOTIFICATION_MNT_COMMUNICATION_UP);
		if ((isMobNotificationUp != null) && isMobNotificationUp.equals(NotificationConstants.YES)) {
			Session session = getCurrentSession();
			try {
				logger.info("pull the Mobile Notification Q,  invoke Mobile Notification size  : " + CommunicationGatewayImpl.ntQueueNew.size());
				int queueSize = CommunicationGatewayImpl.ntQueueNew.size();
				for (int i = 0; i < queueSize; i++) {

					Callable<NotificationProcess> callable = new CommMNTWorker(CommunicationGatewayImpl.ntQueueNew.poll());
					Future<NotificationProcess> future = executor.submit(callable);
					future.get(threadTimeOutInSeconds, TimeUnit.SECONDS); 
					NTSentlist.add(future);
				}
				
				Iterator<Future<NotificationProcess>> futureList=NTSentlist.iterator();

				while (futureList.hasNext()) {
					Future<NotificationProcess> future = (Future<NotificationProcess>) futureList.next();
					if (future.isDone()){
						try {
							String sql = "update notification_process set status=:Status, retry_attempts=:retryAttempts,confirmation=:confirmation where id =:id  and notification_type=:notificationType";
							
							session.createNativeQuery(sql)
						       .setParameter("Status", future.get().getStatus())
						       .setParameter("retryAttempts", future.get().getRetryAttempts())
						       .setParameter("confirmation",future.get().getConfirmation())
						       .setParameter("id",future.get().getId())
						       .setParameter("notificationType",future.get().getNotificationType())
						       .executeUpdate();
							futureList.remove();
							logger.info("future object removed :" + future +" , TSSentlist.size() : "+NTSentlist.size());
						}catch (Exception e) {
							e.printStackTrace();
							logger.info("Exception  "+e);
						}
					}
				}
			}catch(Exception e){
				e.printStackTrace();
			}
		} else {
			logger.info("Mobile Notification communciation is not up .... ");
		}
	}
}
