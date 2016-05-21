package uk.mm.bobcat.service;

import java.util.Calendar;

import org.apache.commons.lang.math.RandomUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import uk.mm.bobcat.service.repos.NameCandidateRepository;

@Service
public class NameService {
	private static final String NO_NAMES_AVAILABLE = "No names available!";

	@Autowired
	private NameCandidateRepository nameRepo;
	@Autowired
	private RandomLongUtils nameUtils;

	public String getNameOfTheDay(Calendar currentDate) {
		// firstIndex, secondIndex and isDoubleName have to be functions of current date 
		// to generate a reproducable name for a given date:
		long nameCount = nameRepo.count();
		if (nameCount == 0) {
			return NO_NAMES_AVAILABLE;
		}
		int dateValue = extractRelevantDateValues(currentDate);
		long firstIndex = getBoundedValue(dateValue, nameCount);
		boolean isDoubleName = (dateValue % 3) == 0;
		if (isDoubleName) {
			long secondIndex = getBoundedValue(dateValue * 13, nameCount);
			String firstName = nameRepo.findByIndex(firstIndex).getName();
			String secondName = nameRepo.findByIndex(secondIndex).getName();
			return firstName + " - " + secondName;
		}
		return nameRepo.findByIndex(firstIndex).getName();
	}

	public String getRandomName() {
		long nameCount = nameRepo.count();
		if (nameCount == 0) {
			return NO_NAMES_AVAILABLE;
		}
		if (isDoubleName()) {
			return getSingleName(nameCount) + " - " + getSingleName(nameCount);
		}
		return getSingleName(nameCount);
	}

	private int extractRelevantDateValues(Calendar cal) {
		return cal.get(Calendar.YEAR) * 1000 + cal.get(Calendar.DAY_OF_YEAR);
	}

	public long getNameCount() {
		return nameRepo.count();
	}

	private long getBoundedValue(int inval, long nameCount) {
		return Math.abs(inval % nameCount);
	}

	private String getSingleName(long maxCount) {
		long randomIndex = nameUtils.randomLong(maxCount);
		return nameRepo.findByIndex(randomIndex).getName();
	}

	private boolean isDoubleName() {
		return RandomUtils.nextInt(10) <= 2;
	}
}