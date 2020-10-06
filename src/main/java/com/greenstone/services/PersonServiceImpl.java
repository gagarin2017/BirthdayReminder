package com.greenstone.services;

import java.time.LocalDate;
import java.time.Period;
import java.time.temporal.ChronoUnit;
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
		return personRepository.save(person);
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
				
				//TODO: update Birthday too
//				Optional<Person> dbPersonToUpdate = personRepository.findById(formPerson.getId());
//				final Birthday newBirthday = generateBirthday(dbPersonToUpdate.get());
//				dbPersonToUpdate.get().setBirthday(newBirthday);
//				newBirthday.setPerson(formPerson);
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
			
//			if (person.getBirthday().getTotalDaysUntilBirthday() < reminderSpan) {
				if (person.getBirthday().getTotalDaysUntilBirthday() > 0) {
				personsWithBirthdaysDue.add(person);
			}
		}
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

			isBirthdayDue = p2 > 3;
		}
		
		return isBirthdayDue;
	}
	
	@Override
	public List<Person> pullPersonsData() {
		
		final List<Person> persons = new ArrayList<Person>();
		
		for (final Person person : findAllPersons()) {
			
			if (person.getDateOfBirth() != null) {
				final Birthday birthday = birthdayService.generateBirthday(person);
				
				person.setBirthday(birthday);
				birthday.setPerson(person);
				
				personRepository.save(person);
				persons.add(person);
			}
		}
		
		return persons;
	}

}
