package com.greenstone.test

import static org.assertj.core.api.InstanceOfAssertFactories.LIST

import com.greenstone.services.PersonServiceImpl

import spock.lang.Specification

class SpockTest extends Specification {
	
	def input1 = 50
	def input2 = 10
	def result = 0
//	PersonServiceImpl myClass = new PersonServiceImpl(personRepository: Mock(PersonJPARepository))


	def "two plus two should equal four"() {
		given:
		int left = 2
		int right = 2

		when:
		int result = left + right

		then:
		result == 5
	}
	
//	def "test my method"() {
//		when:
//		myClass.pullPersonsData()
//	  
//		then:
//		1 * myClass.personRepository.findAll() >> Collections.emptyList()
////		1 * myClass.serviceA.someFunction([]) >> 'A string'
//	  }
}


