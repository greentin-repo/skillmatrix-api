package com.greentin.enovation.communication;

import java.util.concurrent.ConcurrentLinkedQueue;






public class CommunicationGateway {

	public static ConcurrentLinkedQueue<NotificationProcess> emailQueueNew = new ConcurrentLinkedQueue<NotificationProcess>();
	public static ConcurrentLinkedQueue<NotificationProcess> smsQueueNew = new ConcurrentLinkedQueue<NotificationProcess>();
	public static ConcurrentLinkedQueue<NotificationProcess> ntQueueNew = new ConcurrentLinkedQueue<NotificationProcess>();
	
}
