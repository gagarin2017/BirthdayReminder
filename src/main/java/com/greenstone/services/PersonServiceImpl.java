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
	public Person findPersonById(final Integer personId) {
		final Optional<Person> dbPerson = personRepository.findById(Long.valueOf(personId));
		return dbPerson.isEmpty() ? null : encryptionService.decrypt(dbPerson.get());
	}

	@Override
	public Person savePerson(final Person person) {
		return savePersonWithBirthday(person);
	}

	/**
	 * Method persists {@link Person} to the database.
	 * In cases when {@link Birthday} does#t exist for the person, then {@link Birthday is created}
	 * 
	 * Method encrypts all the data entities to be persisted on the database.
	 * 
	 * @param person - decrypted plain POJO {@link Person}
	 * @return encrypted {@link Person}
	 */
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
		final Person dbPersonToUpdate = findPersonById(Math.toIntExact(formPerson.getId()));
		
		if(dbPersonToUpdate != null) {
			dbPersonToUpdate.setFirstName(formPerson.getFirstName());
			dbPersonToUpdate.setLastName(formPerson.getLastName());
			
			if(isBirthDateChanged(formPerson, dbPersonToUpdate)) {
				
				dbPersonToUpdate.setDateOfBirth(formPerson.getDateOfBirth());
				dbPersonToUpdate.setBirthday(null);
				final Birthday updatedBirthday = birthdayService.updatePersonsBirthday(dbPersonToUpdate);
				dbPersonToUpdate.setBirthday(updatedBirthday);
			}
		}
		
		System.out.println("Person bd to update: "+ encryptionService.decrypt(dbPersonToUpdate.getBirthday()));
		return personRepository.save(encryptionService.encrypt(dbPersonToUpdate));
	}
	
	/**
	 * When {@link Person} update is executed, we check if we need to update {@link Birthday} entity too.
	 * 
	 * @param formPerson - {@link Person} object from the update form
	 * @param personToUpdate - the database object {@Person} which we are updating on the database.
	 * @return true if date of Birth is changed and we need to update {@Birthday} entity
	 */
	private boolean isBirthDateChanged(final Person formPerson, Person personToUpdate) {
		return !personToUpdate.getDateOfBirth().equals(formPerson.getDateOfBirth());
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

	/**
	 * Starter method. Pulls the data from the database and builds the {@link Birthday}s for each {@link Person}
	 * if it doesn't exist.
	 *
	 */
	@Override
	public List<Person> pullPersonsData() {
		
		final List<Person> persons = new ArrayList<Person>();
		
		for (final Person person : findAllPersons()) {
			
			// Assume all persons on the database are encrypted
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
