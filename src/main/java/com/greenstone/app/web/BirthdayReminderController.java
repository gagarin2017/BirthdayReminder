package com.greenstone.app.web;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import com.greenstone.domain.Person;
import com.greenstone.services.PersonService;

@Controller
@ComponentScan("com.greenstone.services")
public class BirthdayReminderController /* implements WebMvcConfigurer */ {
	
	@Autowired
    private PersonService personService;
	
	@GetMapping("/")
	public String showForm(Person person) {
		return "personhome";
	}
	
	@PostMapping("/")
	public String checkPersonInfo(@Valid Person person, BindingResult bindingResult) {

		if (bindingResult.hasErrors()) {
			return "personhome";
		}

		personService.savePerson(person);
		return "personview";
	}
}
