package com.greenstone.app.web;

import java.util.Optional;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.greenstone.domain.Person;
import com.greenstone.services.PersonService;

@Controller
@ComponentScan("com.greenstone.services")
public class BirthdayReminderController /* implements WebMvcConfigurer */ {
	
	@Autowired
    private PersonService personService;
	
	@GetMapping(value={"/", "/personhome"})
	public ModelAndView showHome() {
		final ModelAndView modelAndView = new ModelAndView("personhome");
		modelAndView.addObject("persons", personService.findAll());
		return modelAndView;
	}
	
	@GetMapping("/addPerson")
	public String showForm(final Person person) {
		return "personform";
	}
	
	@PostMapping("/personview")
	public String checkPersonInfo(@Valid Person person, BindingResult bindingResult) {

		if (bindingResult.hasErrors()) {
			System.err.println("\n\n\n Errors!\n\n\n"+bindingResult.getFieldError().getCode());
			return "personform";
		}

		personService.updatePerson(person);
		return "personview";
	}
	
	@GetMapping("/update/person")
	public String showPerson(@RequestParam String id, Model model) {
		Optional<Person> person = personService.findById(Integer.valueOf(id));
	    model.addAttribute("person", person.get());
		return "personform";
	}
	
	@GetMapping("/delete/person")
	public ModelAndView deletePerson(@RequestParam String id) {
		personService.deletePerson(Integer.valueOf(id));
		final ModelAndView modelAndView = new ModelAndView("personhome");
		modelAndView.addObject("persons", personService.findAll());
		return modelAndView;
	}
	
}
