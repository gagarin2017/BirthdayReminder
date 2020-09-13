package com.greenstone.services;

import java.util.List;
import java.util.Optional;

import com.greenstone.domain.Person;

public interface PersonService {
	
    List<Person> findAll();
    
    Optional<Person> findById(Integer PersonId);
 
    Person savePerson(Person Person);
 
    Person updatePerson(Person Person);
 
    void deletePerson(Integer PersonId);

}
