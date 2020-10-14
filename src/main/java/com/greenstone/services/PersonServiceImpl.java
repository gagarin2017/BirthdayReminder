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
	
	@Autowired
	private EncryptionService encryptionService;

	@Override
	public List<Person> findAllPersons() {
		
		final List<Person> persons = new ArrayList<>();
		
		for (final Person person : personRepository.findAll()) {
			persons.add(encryptionService.decrypt(person));
		}
		
		return persons;
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
		
		final Person encryptedPerson = encryptionService.encrypt(person);
		
		final Birthday birthday = birthdayService.generateBirthday(person);
		final Birthday encryptedBirthday = encryptionService.encrypt(birthday);
		
		encryptedPerson.setBirthday(encryptedBirthday);
		encryptedBirthday.setPerson(encryptedPerson);
		
		return personRepository.save(encryptedPerson);
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
			
			final Birthday birthday = person.getBirthday();
			final int totalDaysUntilBirthday = Integer.valueOf(encryptionService.decrypt(birthday.getTotalDaysUntilBirthday()));
			
			if (totalDaysUntilBirthday < reminderSpan) {
				personsWithBirthdaysDue.add(person);
			}
		}
		return personsWithBirthdaysDue;
	}

	@Override
	public List<Person> pullPersonsData() {
		
		final List<Person> persons = new ArrayList<Person>();
		
		for (final Person person : findAllPersons()) {
			
			final Person decryptedPerson = encryptionService.decrypt(person);
			
			if (decryptedPerson.getDateOfBirth() != null) {
				
				final Birthday birthday = decryptedPerson.getBirthday();
				
				if (birthday == null || birthday.getId() < 0) {
					savePersonWithBirthday(person);
				}
			}

			persons.add(person);
		}
		
		return persons;
	}

}
