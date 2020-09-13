package com.greenstone.repositories;

import java.util.List;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.greenstone.domain.Person;

@Repository
public interface PersonJPARepository extends CrudRepository<Person, Long> {

	List<Person> findAll();
}
