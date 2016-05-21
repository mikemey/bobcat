package uk.mm.bobcat.service;

import java.util.List;
import java.util.Random;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import uk.mm.bobcat.domain.NamePair;
import uk.mm.bobcat.domain.Rating;
import uk.mm.bobcat.service.repos.RatingRepository;

/**
 * Service to keep track of Elo points of each name in the comparison competition. <br />
 * The k-factor used in the Elo calculation is depending on the number of comparison of the name
 * in question. <br /><br />
 * <table border="1" style="text-align: center;">
 *  <thead><tr><th># of compares</th><th>k-factor</th></tr></thead>
 * 	<tbody><tr><td>&lt;5</td><td>30</td></tr>
 * 		<tr><td>5 - 9</td><td>15</td></tr>
 * 		<tr><td>&gt;9</td><td>10</td></tr></tbody></table>
 */
@Service
public class EloService {
	private final Logger logger = LoggerFactory.getLogger(EloService.class);
	private static final Random random = new Random(System.nanoTime());

	@Autowired
	private RatingRepository ratingRepo;
	@Autowired
	private EloCalculator eloCalculator;

	public void newCompareResult(String winner, String loser) {
		if (!areValidNames(winner, loser)) {
			logger.warn("aborting elo-point calculations due to invalid names (not in repository): [{}] / [{}]",
					winner, loser);
			return;
		}
		Rating winnerRating = getRating(winner);
		Rating loserRating = getRating(loser);
		eloCalculator.calculateNewRatings(winnerRating, loserRating);
		ratingRepo.save(winnerRating);
		ratingRepo.save(loserRating);
	}

	private Rating getRating(String name) {
		Rating rating = ratingRepo.findByNameAndActive(name);
		if (rating == null) {
			throw new IllegalArgumentException("name not found in competition: " + name);
		}
		return rating;
	}

	public List<Rating> getRatings() {
		return ratingRepo.findActiveRanking(new Sort(Sort.Direction.DESC, "eloPoints"));
	}

	public NamePair getCompetingPair() {
		int repoCount = ratingRepo.findActiveRanking(null).size();
		if (repoCount < 2) {
			logger.info("not enough names for a competition!");
			return NamePair.NOT_ENOUGH_NAMES;
		}
		String firstName = getRandomName();
		String secondName = getRandomName();

		while (firstName.equals(secondName)) {
			secondName = getRandomName();
		}
		return new NamePair(firstName, secondName);
	}

	public void addNameInCompetition(String name) {
		Rating rating = ratingRepo.findByName(name);
		if (rating != null) {
			if (rating.isActive()) {
				logger.info("duplicate names submitted for competing: {}", name);
				return;
			}
			logger.info("re-activating name in competing: {}", name);
			rating.setActive(true);
		}
		if (rating == null) {
			rating = new Rating(ratingRepo.count(), name, Rating.START_ELO_POINTS, 0, true);
		}
		ratingRepo.save(rating);
	}

	public void modifyName(String originalName, String newName) {
		Rating rating = ratingRepo.findByName(originalName);
		if (rating == null) {
			throw new IllegalArgumentException("name not found in competition: " + originalName);
		}
		rating.setName(newName);
		ratingRepo.save(rating);
	}

	public void removeNameFromCompetition(String name) {
		Rating rating = ratingRepo.findByNameAndActive(name);
		if (rating == null) {
			throw new IllegalArgumentException("name not found in competition: " + name);
		}
		rating.setActive(false);
		ratingRepo.save(rating);
	}

	private boolean areValidNames(String... names) {
		for (String name : names) {
			if (NamePair.NOT_ENOUGH_NAMES.getFirst().equals(name)) {
				return false;
			}
		}
		return true;
	}

	private String getRandomName() {
		List<Rating> activeRatings = ratingRepo.findActiveRanking(null);
		int index = random.nextInt(activeRatings.size());
		return activeRatings.get(index).getName();
	}
}