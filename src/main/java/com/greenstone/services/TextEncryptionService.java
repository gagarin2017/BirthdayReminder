package com.greenstone.services;

import java.time.LocalDate;

import org.jasypt.util.text.AES256TextEncryptor;
import org.springframework.stereotype.Service;

import com.greenstone.domain.Birthday;
import com.greenstone.domain.Person;

@Service
public class TextEncryptionService implements EncryptionService {

	private final static AES256TextEncryptor TEXT_ENCRYPTOR = new AES256TextEncryptor();
	
	public TextEncryptionService() {
		final String myEncryptionPassword = System.getenv("MY_ENCRYPTION_PASSWORD");
		TEXT_ENCRYPTOR.setPassword(myEncryptionPassword);
	}

	@Override
	public String encrypt(final String value) {
		return TEXT_ENCRYPTOR.encrypt(value);
	}

	@Override
	public String decrypt(String value) {
		return TEXT_ENCRYPTOR.decrypt(value);
	}

	@Override
	public String encrypt(final int value) {
		return encrypt(String.valueOf(value));
	}

	@Override
	public Person decrypt(final Person person) {
		final Person decryptedPerson = new Person();
		decryptedPerson.setFirstName(decrypt(person.getFirstName()));
		decryptedPerson.setLastName(decrypt(person.getLastName()));
		decryptedPerson.setDateOfBirth(LocalDate.parse(decrypt(person.getEncryptedDOB())));
		
		if (person.getBirthday() != null) {
			final Birthday decryptedBirthday = decrypt(person.getBirthday());
			decryptedPerson.setBirthday(decryptedBirthday);
		}
		return decryptedPerson;
	}

	private Birthday decrypt(final Birthday birthday) {
		final Birthday decryptedBirthday = new Birthday();
		decryptedBirthday.setAge(decrypt(birthday.getAge()));
		decryptedBirthday.setDaysUntilBirthday(decrypt(birthday.getDaysUntilBirthday()));
		decryptedBirthday.setMonthsUntilBirthday(decrypt(birthday.getMonthsUntilBirthday()));
		decryptedBirthday.setTotalDaysUntilBirthday(decrypt(birthday.getTotalDaysUntilBirthday()));
		decryptedBirthday.setWeeksUntilBirthday(decrypt(birthday.getWeeksUntilBirthday()));
		return decryptedBirthday;
	}

	@Override
	public Birthday encrypt(final Birthday birthday) {
		final Birthday encryptedBirthday = new Birthday();
		encryptedBirthday.setAge(encrypt(birthday.getAge()));
		encryptedBirthday.setDaysUntilBirthday(encrypt(birthday.getDaysUntilBirthday()));
		encryptedBirthday.setMonthsUntilBirthday(encrypt(birthday.getMonthsUntilBirthday()));
		encryptedBirthday.setTotalDaysUntilBirthday(encrypt(birthday.getTotalDaysUntilBirthday()));
		encryptedBirthday.setWeeksUntilBirthday(encrypt(birthday.getWeeksUntilBirthday()));
		return encryptedBirthday;
	}

	@Override
	public Person encrypt(final Person person) {
		final Person encryptedPerson = new Person();
		encryptedPerson.setFirstName(encrypt(person.getFirstName()));
		encryptedPerson.setLastName(encrypt(person.getLastName()));
		encryptedPerson.setEncryptedDOB(encrypt(person.getDateOfBirth().toString()));
		
		if (person.getBirthday() != null) {
			final Birthday encryptedBirthday = encrypt(person.getBirthday());
			encryptedPerson.setBirthday(encryptedBirthday);
		}
		return encryptedPerson;
	}

}
