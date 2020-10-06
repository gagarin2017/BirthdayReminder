package com.greenstone.domain;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "Birthday")
public class Birthday {
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "id")
	private long id;
	
	@Column(name = "age")
	@NotNull
	private int age;
	
	@Column(name = "monthsUntil")
	@NotNull
	@Min(0)
	@Max(12)
	private int monthsUntilBirthday;

	@Column(name = "weeksUntil")
	@NotNull
	@Min(0)
	@Max(53)
	private int weeksUntilBirthday;

	@Column(name = "daysUntil")
	@NotNull
	@Min(0)
	@Max(31)
	private int daysUntilBirthday;

	@Column(name = "totalDaysUntil")
	@NotNull
	@Min(0)
	@Max(365)
	private int totalDaysUntilBirthday;
	
	@OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL, mappedBy = "birthday")
    private Person person;

}
