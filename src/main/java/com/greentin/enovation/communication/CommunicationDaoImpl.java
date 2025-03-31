package com.greentin.enovation.communication;

import javax.transaction.Transactional;

import org.hibernate.Session;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.greentin.enovation.utils.BaseRepository;

@Component
@Transactional
public class CommunicationDaoImpl extends BaseRepository implements ICommunication {
 
	
	private static final Logger LOGGER = LoggerFactory.getLogger(CommunicationDaoImpl.class);
	
	@Override
	public void saveNotificationMessage(NotificationMessage notificationMessage) {
		
		try {
			Session session=getCurrentSession();
			session.save(notificationMessage);
		}catch(Exception e) {
			e.printStackTrace();
		}
	}

}
