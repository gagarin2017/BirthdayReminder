package com.greenstone.services;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.stereotype.Service;

import com.greenstone.domain.Birthday;
import com.greenstone.domain.Person;
import com.greenstone.repositories.PersonJPARepository;

@Service
@EnableJpaRepositories("com.greenstone.repositories")
public class PersonServiceImpl implements PersonService {
	
	@Value("${birthday.reminder.span}")
	private int reminderSpan;
	
	@Autowired
	private PersonJPARepository personRepository;
	
	@Autowired
	private BirthdayService birthdayService;

	@Override
	public List<Person> findAllPersons() {
		return personRepository.findAll();
	}

	@Override
	public Optional<Person> findPersonById(final Integer personId) {
		return personRepository.findById(Long.valueOf(personId));
	}

	@Override
	public Person savePerson(final Person person) {
		return savePersonWithBirthday(person);
	}

	private Person savePersonWithBirthday(final Person person) {
		final Person savedPerson;
		final Birthday birthday = birthdayService.generateBirthday(person);
		person.setBirthday(birthday);
		birthday.setPerson(person);
		savedPerson = personRepository.save(person);
		return savedPerson;
	}

	@Override
	public Person updatePerson(final Person formPerson) {
		Optional<Person> dbPersonToUpdate = findPersonById(Math.toIntExact(formPerson.getId()));
		
		if(dbPersonToUpdate.isPresent()) {
			dbPersonToUpdate.get().setFirstName(formPerson.getFirstName());
			dbPersonToUpdate.get().setLastName(formPerson.getLastName());
			
			if(isBirthDateChanged(formPerson, dbPersonToUpdate)) {
				
				dbPersonToUpdate.get().setDateOfBirth(formPerson.getDateOfBirth());
				
				birthdayService.updatePersonsBirthday(dbPersonToUpdate.get());
			}
		}
		
		return personRepository.save(dbPersonToUpdate.get());
	}
	
	private boolean isBirthDateChanged(final Person formPerson, Optional<Person> personToUpdate) {
		return !personToUpdate.get().getDateOfBirth().equals(formPerson.getDateOfBirth());
	}

	@Override
	public void deletePerson(Integer personId) {
		personRepository.deleteById(Long.valueOf(personId));
	}

	/**
	 * Method returns the list of persons whose birthday is due
	 *
	 */
	@Override
	public List<Person> personsWithBirthdaysDue(final List<Person> persons) {
		
		final List<Person> personsWithBirthdaysDue = new ArrayList<Person>();
		
		for (final Person person : persons) {
			
			if (person.getBirthday().getTotalDaysUntilBirthday() < reminderSpan) {
				personsWithBirthdaysDue.add(person);
			}
		}
		return personsWithBirthdaysDue;
	}

	@Override
	public List<Person> pullPersonsData() {
		
		final List<Person> persons = new ArrayList<Person>();
		
		for (final Person person : findAllPersons()) {
			
			if (person.getDateOfBirth() != null) {
				
				final Birthday birthday = person.getBirthday();
				
				if (birthday != null) {
					System.out.println("Juras output "+birthday.getId());
					if (birthday.getId() < 0) {
						savePersonWithBirthday(person);
					}
				}
			}

			persons.add(person);
		}
		
		return persons;
	}

}
