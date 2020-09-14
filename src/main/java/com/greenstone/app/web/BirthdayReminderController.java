package com.greenstone.app.web;

import java.util.List;
import java.util.Optional;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import com.greenstone.domain.Person;
import com.greenstone.services.PersonService;

//@RestController
//public class HelloController {
//
//	@RequestMapping("/")
//	public String index() {
//		return "Greetings from Spring Boot!";
//	}
//
//}

@Controller
//@RequestMapping("/api/persons")
//@RequestMapping("/")
//@ComponentScan("com.greenstone.services")
public class BirthdayReminderController implements WebMvcConfigurer {
	
//	@Autowired
//    PersonService PersonService;
// 
//    @GetMapping
//    List<Person> getAllPersonList(){
//        return PersonService.findAll();
//    }
// 
//    @PostMapping
//    Person savePerson(@RequestBody Person person){
//        return PersonService.savePerson(person);
//    }
// 
//    @PutMapping
//    Person updatePerson(@RequestBody Person person){
//        return PersonService.updatePerson(person);
//    }
// 
//    @DeleteMapping("/{personId}")
//    void deletePerson(@PathVariable Integer personId){
//        PersonService.deletePerson(personId);
//    }
// 
//    @GetMapping("/{personId}")
//    Optional<Person> getPersonById(@PathVariable Integer personId){
//        return PersonService.findById(personId);
//    }
    
//    @RequestMapping("/")
//	public String index() {
//		return "Greetings from Spring Boot!";
//	}
    
//    @RequestMapping(value = "/", method = RequestMethod.GET)
//    public ModelAndView showForm() {
//    	System.out.println("\n\n\n Show form method ...");
//        return new ModelAndView("personhome", "person", new Person());
//    }
    
//    @RequestMapping(value = "/p", method = RequestMethod.GET)
//    public String showForm() {
//    	System.out.println("\n\n\n Show form method ...");
//    	return "personhome";
//    }
    
	@GetMapping("/")
	public String showForm(Person person) {
		return "personhome";
	}
	
	@PostMapping("/")
	public String checkPersonInfo(@Valid Person person, BindingResult bindingResult) {

		if (bindingResult.hasErrors()) {
			return "personhome";
		}

		return "personview";
	}
 
//    @RequestMapping(value = "/addPerson", method = RequestMethod.POST)
//    public String submit(@Valid @ModelAttribute("person")Person person, 
//      BindingResult result, ModelMap model) {
//        if (result.hasErrors()) {
//            return "error";
//        }
//        model.addAttribute("firstName", person.getFirstName());
//        return "personview";
//    }
}
