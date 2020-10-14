package com.greenstone.domain;

import java.time.LocalDate;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.validation.constraints.NotNull;

import org.springframework.format.annotation.DateTimeFormat;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@Entity(name = "Person")
@Table(name = "Person")
public class Person {
	
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE)
	private long id;
	
	@Column(name = "FIRSTNAME")
	@NotNull
	private String firstName;

	
	@Column(name = "LASTNAME")
	private String lastName;

	
	@DateTimeFormat(pattern = "dd/MM/yyyy")
	@Transient
	private LocalDate dateOfBirth;
	
	@Column(name = "DATEOFBIRTH")
	@NotNull
	private String encryptedDOB;

	
	@Override
	public String toString() {
		return "First Name: " + firstName + ", Last Name: " + lastName +", DOB: "+dateOfBirth;
	}
	
	@OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	@JoinColumn(name = "birthdayId")
	private Birthday birthday;

	public Person(String firstName, String lastName, LocalDate dateOfBirth) {
		super();
		this.firstName = firstName;
		this.lastName = lastName;
		this.dateOfBirth = dateOfBirth;
	}

}
