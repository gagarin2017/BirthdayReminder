package com.greenstone.services;

import java.time.LocalDate;
import java.time.Period;
import java.time.temporal.ChronoUnit;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.stereotype.Service;

import com.greenstone.domain.Birthday;
import com.greenstone.domain.Person;
import com.greenstone.repositories.BirthdayJPARepository;

@Service
@EnableJpaRepositories("com.greenstone.repositories.BirthdayJPARepository")
public class BirthdayServiceImpl implements BirthdayService {
	
	@Autowired
	private BirthdayJPARepository birthdayJPARepository;

	@Override
	public Birthday generateBirthday(final Person person) {
		final LocalDate dateOfBirth = person.getDateOfBirth();
		final Birthday birthday = new Birthday();
		
		birthday.setPerson(person);
		
		final LocalDate today = LocalDate.now();
		LocalDate nextBDay = dateOfBirth.withYear(today.getYear());

		final Period age = Period.between(dateOfBirth, today);
		birthday.setAge(String.valueOf(age.getYears()));

		// If your birthday has occurred this year already, add 1 to the year.
		if (nextBDay.isBefore(today) || nextBDay.isEqual(today)) {
			nextBDay = nextBDay.plusYears(1);
		}

		final Period periodTillBirthday = Period.between(today, nextBDay);
		final long daysTillBirthday = ChronoUnit.DAYS.between(today, nextBDay);
		
		birthday.setMonthsUntilBirthday(String.valueOf(periodTillBirthday.getMonths()));
		birthday.setDaysUntilBirthday(String.valueOf(periodTillBirthday.getDays()));
		birthday.setWeeksUntilBirthday(String.valueOf((int) (daysTillBirthday / 7)));
		birthday.setTotalDaysUntilBirthday(String.valueOf((int) daysTillBirthday));
		
//		System.out.println("There are " + periodTillBirthday.getMonths() + " months, and " + periodTillBirthday.getDays()
//				+ " days until your next birthday. (" + daysTillBirthday + " total)");
		
		return birthday;
	}

	@Override
	public void updatePersonsBirthday(final Person person) {
		Optional<Birthday> dbBirthdayToUpdate = findBirthdayById(Math.toIntExact(person.getBirthday().getId()));
		
		final Birthday newBirthday = generateBirthday(person);
		
		if(dbBirthdayToUpdate.isPresent()) {
			dbBirthdayToUpdate.get().setAge(newBirthday.getAge());
			dbBirthdayToUpdate.get().setDaysUntilBirthday(newBirthday.getDaysUntilBirthday());
			dbBirthdayToUpdate.get().setMonthsUntilBirthday(newBirthday.getMonthsUntilBirthday());
			dbBirthdayToUpdate.get().setTotalDaysUntilBirthday(newBirthday.getTotalDaysUntilBirthday());
			dbBirthdayToUpdate.get().setWeeksUntilBirthday(newBirthday.getWeeksUntilBirthday());
			birthdayJPARepository.save(dbBirthdayToUpdate.get());
		}		
	}
	
	@Override
	public Optional<Birthday> findBirthdayById(final Integer birthdayId) {
		return birthdayJPARepository.findById(Long.valueOf(birthdayId));
	}

}
