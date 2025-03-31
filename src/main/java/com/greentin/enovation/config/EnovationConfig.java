package com.greentin.enovation.config;

import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import javax.persistence.EntityManagerFactory;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.TaskExecutor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Component;
import com.greentin.enovation.model.EmailTemplateMaster;
import com.greentin.enovation.model.SystemConfig;

@Configuration
@Component
public class EnovationConfig implements ApplicationRunner  {

	private static Logger logger = LoggerFactory.getLogger(EnovationConfig.class);
	public static ConcurrentHashMap<String,String> buddyConfig=new ConcurrentHashMap<>();
	public static ConcurrentHashMap<String,String> documentMasterCount=new ConcurrentHashMap<>();
	@Autowired
	EntityManagerFactory em;

	@Override
	public void run(ApplicationArguments args) throws Exception {
		Session session=em.unwrap(SessionFactory.class).openSession();
		try {
			@SuppressWarnings("unchecked")
			List<SystemConfig> list=session.createQuery("from SystemConfig").getResultList();
			for(SystemConfig config:list) {
				buddyConfig.put(config.getConfig_name(),config.getConfig_value());
			}

		}catch (Exception e) {

			logger.error("Exception:  "+e);

		}finally {
			if(session!=null) {
				session.close();
			}
		}

	}



	@SuppressWarnings("unchecked")
	@Scheduled(initialDelay = 10000, fixedDelay = 10000)
	public void refreshSurveyTBConfig() throws Exception{

		Session session=em.unwrap(SessionFactory.class).openSession();
		try{

			List<SystemConfig> list=session.createQuery("from SystemConfig where config_name='refreshSystemConfig'").getResultList();

			for (SystemConfig systemConfig : list) {
				if ( systemConfig.getConfig_value() != null && systemConfig.getConfig_value().equals("Y") ){
					logger.info("In refreshSystemConfig of  HrBuddy ");
					run(null);

					session.beginTransaction();
					session.createQuery("update SystemConfig set config_value=:value where config_name='refreshSystemConfig'")  
					.setParameter("value","N").executeUpdate();
					session.getTransaction().commit();
				}

			}

		}catch (Exception e) {
			logger.info("Exception is::   "+e);
		}finally {
			if(session!=null) {
				session.close();
			}
		}

	}



	public EmailTemplateMaster getMessageContent(String name) {
		Session session=em.unwrap(SessionFactory.class).openSession();

		try {
			@SuppressWarnings("unchecked")
			List<EmailTemplateMaster> messageList=session.createQuery("from EmailTemplateMaster where name=:name")
			.setParameter("name",name)
			.getResultList();

			if(messageList.size()>0) {
				EmailTemplateMaster message_content=messageList.get(0);
				return message_content;
			}	
		}catch (Exception e) {
			logger.error("Exception:  "+e);
		}finally {
			if(session!=null) {
				session.close();
				
			}
		}

		return null;
	}
	
	

	@Bean
	@Qualifier("taskExecutor")
    public TaskExecutor threadPoolTaskExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(4);
        executor.setMaxPoolSize(4);
        executor.setThreadNamePrefix("default_task_executor_thread");
        executor.initialize();
        return executor;
    }
	
	
/*	 
     * Configure ContentNegotiatingViewResolver
     
    @Bean
    public ViewResolver contentNegotiatingViewResolver(ContentNegotiationManager manager) {
        ContentNegotiatingViewResolver resolver = new ContentNegotiatingViewResolver();
        resolver.setContentNegotiationManager(manager);
        // Define all possible view resolvers
        List<ViewResolver> resolvers = new ArrayList<>();
        //resolvers.add(csvViewResolver());
       resolvers.add(excelViewResolver());
       // resolvers.add(pdfViewResolver());
        resolver.setViewResolvers(resolvers);
        return resolver;
    }
   	
     * Configure View resolver to provide XLS output using Apache POI library to
     * generate XLS output for an object content
     
    @Bean
    public ViewResolver excelViewResolver() {
        return new ExcelViewResolver();
    }
*/
}
