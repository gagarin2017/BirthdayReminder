package com.greenstone.app;

import java.time.LocalDate;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

import com.greenstone.domain.Person;
import com.greenstone.repositories.PersonJPARepository;

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
@EnableJpaRepositories("com.greenstone.repositories")
@EntityScan("com.greenstone.domain")
public class BirthdayReminderApplication implements CommandLineRunner {

	@Autowired
	private PersonJPARepository repository;
	
    private static final Logger log = LoggerFactory.getLogger(BirthdayReminderApplication.class);

	
	public static void main(String[] args) {
		SpringApplication.run(BirthdayReminderApplication.class, args);
	}

	@Override
    public void run(String... args) {

        log.info("StartApplication...");

        // save a single Person
 		repository.save(new Person("Ivan","Petrov", LocalDate.now()));

// 		log.info("Nobodys birthdays in near future. Exiting");
// 		System.exit(0);
    }

}
