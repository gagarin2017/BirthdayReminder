package com.greenstone.repositories;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.greenstone.domain.Birthday;

@Repository
public interface BirthdayJPARepository extends CrudRepository<Birthday, Long>{

}
