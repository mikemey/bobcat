package uk.mm.bobcat.service;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;

import org.apache.commons.lang.math.RandomUtils;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import uk.mm.bobcat.ARunningMongoDBTest;
import uk.mm.bobcat.domain.NameCandidate;
import uk.mm.bobcat.service.repos.NameCandidateRepository;

public class NameServiceTest extends ARunningMongoDBTest {
	private static final String noNamesIndicator = "No names available!";

	@Autowired
	private NameService nameService;

	@Autowired
	private NameCandidateRepository nameRepository;

	@Override
	public void beforeTest() {
		insertTestData();
	}

	@Test
	public void testNoNamesAvailable() {
		nameRepository.deleteAll();
		String notd = nameService.getNameOfTheDay(new GregorianCalendar());
		Assert.assertEquals(noNamesIndicator, notd);
	}

	@Test
	public void testSameSingleNameOfTheDay() {
		Calendar singleDate = findDate(true);
		String notd = nameService.getNameOfTheDay(singleDate);
		Assert.assertFalse(noNamesIndicator.equals(notd));
		singleDate.add(Calendar.HOUR, 11);
		Assert.assertEquals(notd, nameService.getNameOfTheDay(singleDate));
	}

	@Test
	public void testNotSameSingleNameOfTheDay() {
		Calendar singleDate = findDate(true);
		String notd = nameService.getNameOfTheDay(singleDate);

		Calendar otherDate = findDifferentDate(notd, true);
		String notdNotSame = nameService.getNameOfTheDay(otherDate);
		Assert.assertFalse("Same name: " + notd, notd.equals(notdNotSame));
	}

	@Test
	public void testSameDoubleNameOfTheDay() {
		Calendar doubleDate = findDate(false);
		String notd = nameService.getNameOfTheDay(doubleDate);
		doubleDate.add(Calendar.HOUR, 23);
		Assert.assertEquals(notd, nameService.getNameOfTheDay(doubleDate));
	}

	@Test
	public void testNotSameDoubleNameOfTheDay() {
		Calendar doubleDate = findDate(false);
		String notd = nameService.getNameOfTheDay(doubleDate);

		Calendar otherDate = findDifferentDate(notd, false);
		String notdNotSame = nameService.getNameOfTheDay(otherDate);
		Assert.assertFalse("Same name: " + notd, notd.equals(notdNotSame));
	}

	//	------------------------
	//	test helpers:
	//	------------------------
	private Calendar findDate(boolean single) {
		String result = single ? "-" : "";
		Calendar currDate = null;
		while (result.contains("-") == single) {
			currDate = getRandomDate();
			result = nameService.getNameOfTheDay(currDate);
			if (noNamesIndicator.equals(result) || result == null) {
				throw new IllegalStateException("service returned: " + result);
			}
		}
		return currDate;
	}

	private Calendar getRandomDate() {
		int day = RandomUtils.nextInt(31);
		int month = RandomUtils.nextInt(12);
		int year = RandomUtils.nextInt(2010);
		return new GregorianCalendar(year, month, day);
	}

	private Calendar findDifferentDate(String otherName, boolean single) {
		Calendar newSingleDate = findDate(single);
		int counter = 0;
		while (otherName.equals(nameService.getNameOfTheDay(newSingleDate))) {
			newSingleDate = findDate(single);
			if (++counter > 100) {
				throw new IllegalStateException("trying to find a date which results in a different name for a 100 times - "
						+ "there seems to be something wrong...");
			}
		}
		return newSingleDate;
	}

	private void insertTestData() {
		List<NameCandidate> names = new ArrayList<>();
		names.add(new NameCandidate(0, "Joane"));
		names.add(new NameCandidate(1, "Mary"));
		names.add(new NameCandidate(2, "Luise"));
		names.add(new NameCandidate(3, "Paula"));
		names.add(new NameCandidate(4, "Sue"));
		names.add(new NameCandidate(5, "Ionna"));
		names.add(new NameCandidate(6, "Charlotte"));
		names.add(new NameCandidate(7, "Julia"));
		names.add(new NameCandidate(8, "Barbara"));
		names.add(new NameCandidate(9, "Christine"));
		names.add(new NameCandidate(10, "Laura"));
		names.add(new NameCandidate(11, "Liliane"));
		names.add(new NameCandidate(12, "Lucy"));
		names.add(new NameCandidate(13, "Karen"));
		names.add(new NameCandidate(14, "Maria"));
		names.add(new NameCandidate(15, "Amelie"));
		names.add(new NameCandidate(16, "Melanie"));
		names.add(new NameCandidate(17, "Anita"));
		names.add(new NameCandidate(18, "Chiara"));
		names.add(new NameCandidate(19, "Alexandra"));
		names.add(new NameCandidate(20, "Fiona"));
		names.add(new NameCandidate(21, "Walter"));
		nameRepository.save(names);
	}
}
