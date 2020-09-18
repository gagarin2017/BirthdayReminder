package com.greenstone.app;

import java.awt.Desktop;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Bean;
import org.thymeleaf.extras.java8time.dialect.Java8TimeDialect;

import com.greenstone.services.PersonService;

/**
 * need @EnableJpaRepositories if repository package is different to the current class package
 * ref: https://stackoverflow.com/questions/29221645/cant-autowire-repository-annotated-interface-in-spring-boot
 * 
 * need @EntityScan if domain package is different 
 * ref: https://stackoverflow.com/questions/28664064/spring-boot-not-an-managed-type
 * 
 * @author yuriy
 *
 */
@SpringBootApplication
@EntityScan("com.greenstone.domain")
public class BirthdayReminderApplication implements CommandLineRunner {

	private static final String LOCAL_URL = "http://localhost:8080";

	@Autowired
	private PersonService personService;
	
    private static final Logger log = LoggerFactory.getLogger(BirthdayReminderApplication.class);

    @Bean
    public Java8TimeDialect java8TimeDialect() {
        return new Java8TimeDialect();
    }
	
	public static void main(String[] args) {
		SpringApplication.run(BirthdayReminderApplication.class, args);
	}

	@Override
    public void run(String... args) {

        log.info("StartApplication...");
        log.info("Checking upcoming birthdays ...");
        
		if (personService.personsWithBirthdaysDue().isEmpty()) {
			log.info("Nobodys birthdays in near future. Exiting");
			System.exit(0);
		} else {
			browseToWebapp();
		}
    }

	private void browseToWebapp() {
		
		if(Desktop.isDesktopSupported()){
		    Desktop desktop = Desktop.getDesktop();
		    try {
				desktop.browse((new URI(LOCAL_URL)));
		    } catch (IOException | URISyntaxException e) {
		        e.printStackTrace();
		    }
		}else{
		    Runtime runtime = Runtime.getRuntime();
		    try {
		        runtime.exec("rundll32 url.dll,FileProtocolHandler " + LOCAL_URL);
		    } catch (IOException e) {
		        e.printStackTrace();
		    }
		}
	}
}
