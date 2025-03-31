package com.greentin.enovation.utils;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import org.hibernate.Session;
import org.springframework.stereotype.Component;
@Component
public class BaseRepository  {

	@PersistenceContext
	EntityManager entityManager;//singleton

	protected Session getCurrentSession()
	{
		return entityManager.unwrap(Session.class);
	}

}
