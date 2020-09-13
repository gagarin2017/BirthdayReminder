package com.greenstone.app.web;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.greenstone.domain.Person;
import com.greenstone.services.PersonService;

@RestController
@RequestMapping("/api/persons")
@ComponentScan("com.greenstone.services")
public class BirthdayReminderController {
	
	@Autowired
    PersonService PersonService;
 
    @GetMapping
    List<Person> getAllPersonList(){
        return PersonService.findAll();
    }
 
    @PostMapping
    Person savePerson(@RequestBody Person person){
        return PersonService.savePerson(person);
    }
 
    @PutMapping
    Person updatePerson(@RequestBody Person person){
        return PersonService.updatePerson(person);
    }
 
    @DeleteMapping("/{personId}")
    void deletePerson(@PathVariable Integer personId){
        PersonService.deletePerson(personId);
    }
 
    @GetMapping("/{personId}")
    Optional<Person> getPersonById(@PathVariable Integer personId){
        return PersonService.findById(personId);
    }
}
