package com.greenstone.services;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.greenstone.domain.Person;
import com.greenstone.repositories.PersonJPARepository;

@Service
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

}
