package com.greentin.enovation;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.web.server.ErrorPage;
import org.springframework.boot.web.server.WebServerFactoryCustomizer;
import org.springframework.boot.web.servlet.server.ConfigurableServletWebServerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpStatus;
import org.springframework.jmx.export.MBeanExporter;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.EnableTransactionManagement;



@EnableScheduling
@SpringBootApplication(exclude = {SecurityAutoConfiguration.class })
//@EnableCaching
@EnableTransactionManagement
public class EnovationApiApplication implements WebServerFactoryCustomizer<ConfigurableServletWebServerFactory> {

	public static void main(String[] args) {
		SpringApplication.run(EnovationApiApplication.class, args);
	}
	@Bean
	public MBeanExporter exporter(){
		final MBeanExporter exporter = new MBeanExporter();
		exporter.setAutodetect(true);
		exporter.setExcludedBeans("dataSource");
		return exporter;
	}
	@Bean
	PasswordEncoder getEncoder() {
		return new BCryptPasswordEncoder();
	}
	@Override
	public void customize(ConfigurableServletWebServerFactory factory) {
		factory.addErrorPages(new ErrorPage(HttpStatus.UNAUTHORIZED, "/404.html")
				);
		factory.addErrorPages(new ErrorPage(HttpStatus.NOT_FOUND, "/404.html")
				);
	}

}
