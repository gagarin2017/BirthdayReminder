package com.greenstone.app.web;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.ModelAndView;

import com.greenstone.domain.Person;
import com.greenstone.services.PersonService;

@Controller
@ComponentScan("com.greenstone.services")
public class BirthdayReminderController /* implements WebMvcConfigurer */ {
	
	@Autowired
    private PersonService personService;
	
	@GetMapping("/")
	public ModelAndView showHome() {
		final ModelAndView modelAndView = new ModelAndView("personhome");
		modelAndView.addObject("persons", personService.findAll());
		return modelAndView;
	}
	
	@GetMapping("/addPerson")
	public String showForm(Person person) {
		return "personform";
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
