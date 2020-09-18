package com.greenstone.services;

import java.time.LocalDate;
import java.time.Period;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.stereotype.Service;

import com.greenstone.domain.Person;
import com.greenstone.repositories.PersonJPARepository;

@Service
@EnableJpaRepositories("com.greenstone.repositories")
public class PersonServiceImpl implements PersonService {
	
	@Autowired
	private PersonJPARepository personRepository;

	@Override
	public List<Person> findAll() {
		return personRepository.findAll();
	}

	@Override
	public Optional<Person> findById(Integer personId) {
		return personRepository.findById(Long.valueOf(personId));
	}

	@Override
	public Person savePerson(Person person) {
		return personRepository.save(person);
	}

	@Override
	public Person updatePerson(Person person) {
		return personRepository.save(person);
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
	public List<Person> personsWithBirthdaysDue() {
		
		final List<Person> personsWithBirthdaysDue = new ArrayList<Person>();
		
		for (final Person person : findAll()) {
			
			if (isBirthdayDue(person)) {
				personsWithBirthdaysDue.add(person);
			}
		}
		System.out.println("Persons with upcoming birthdays: "+personsWithBirthdaysDue.size());
		return personsWithBirthdaysDue;
	}

	/**
	 * Calculates the time difference in days between now and the date of birthday.
	 * 
	 * @param person
	 * @return true if the number of days before birthday is less than defined in params.
	 */
	// TODO; fix the limit. How soon before birthday user should be reminded of the upcoming birthday.
	private boolean isBirthdayDue(final Person person) {
		
		boolean isBirthdayDue = false;

		if (person.getDateOfBirth() != null) {

			LocalDate today = LocalDate.now();
			LocalDate birthday = person.getDateOfBirth();

			LocalDate nextBDay = birthday.withYear(today.getYear());

			// If your birthday has occurred this year already, add 1 to the year.
			if (nextBDay.isBefore(today) || nextBDay.isEqual(today)) {
				nextBDay = nextBDay.plusYears(1);
			}

			Period p = Period.between(today, nextBDay);
			long p2 = ChronoUnit.DAYS.between(today, nextBDay);
			System.out.println("There are " + p.getMonths() + " months, and " + p.getDays()
					+ " days until your next birthday. (" + p2 + " total)");

			isBirthdayDue = p2 == 53;
		}
		
		return isBirthdayDue;
	}

}
