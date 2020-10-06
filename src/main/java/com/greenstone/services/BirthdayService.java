package com.greenstone.services;

import java.util.Optional;

import com.greenstone.domain.Birthday;
import com.greenstone.domain.Person;

public interface BirthdayService {

	Birthday generateBirthday(Person person);

	void updatePersonsBirthday(Person person);
	

}
