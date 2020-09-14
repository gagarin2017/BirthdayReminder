package com.greenstone.domain;

import java.time.LocalDate;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "Person")
public class Person {
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long id;
	
	@Column(name = "FIRSTNAME")
	@NotNull
	@Size(min=2, max=30)
	private String firstName;

	@Column(name = "LASTNAME")
	private String lastName;
	
//	@Column(name = "DATEOFBIRTH")
//	@NotNull
//	private LocalDate dateOfBirth;

	@Override
	public String toString() {
		return "firstName: " + firstName + ", lastName: " + lastName;
	}

	public Person(String firstName, String lastName, LocalDate dateOfBirth) {
		super();
		this.firstName = firstName;
		this.lastName = lastName;
//		this.dateOfBirth = dateOfBirth;
	}

}
