package com.greenstone.services;

import com.greenstone.domain.Birthday;
import com.greenstone.domain.Person;

public interface EncryptionService {

	String decrypt(String dateOfBirthAsString);

	String encrypt(String dateOfBirthAsString);

	String encrypt(int value);

	Person encrypt(Person person);

	Person decrypt(Person person);

	Birthday encrypt(Birthday birthday);
	
	Birthday decrypt(Birthday birthday);

}
