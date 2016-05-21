package uk.mm.bobcat.service;

import java.util.List;

import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import uk.mm.bobcat.ARunningMongoDBTest;
import uk.mm.bobcat.domain.Rating;
import uk.mm.bobcat.service.repos.RatingRepository;

public class EloServiceCalculationTest extends ARunningMongoDBTest {

	@Autowired
	private EloService eloService;

	@Autowired
	private RatingRepository ratingRepo;

	@Test
	public void testEloPointsNewNames() {
		String winner = "Lucy";
		String loser = "Marta";
		eloService.addNameInCompetition(winner);
		eloService.addNameInCompetition(loser);
		eloService.newCompareResult(winner, loser);

		List<Rating> ratings = eloService.getRatings();
		Assert.assertEquals(2, ratings.size());
		Assert.assertEquals("Lucy", ratings.get(0).getName());
		Assert.assertEquals(1415, ratings.get(0).getPoints());
		Assert.assertEquals("Marta", ratings.get(1).getName());
		Assert.assertEquals(1385, ratings.get(1).getPoints());
	}

	@Test
	// k-factor 10 and 15 respectively
	public void testEloPointsOldNameVsMiddleOldName() {
		String winner = "Joane";
		String loser = "Mary";
		Rating winnerRating = new Rating(0, winner, 1200, 5, true);
		Rating loserRating = new Rating(1, loser, 1500, 10, true);

		ratingRepo.save(winnerRating);
		ratingRepo.save(loserRating);

		eloService.newCompareResult(winner, loser);

		List<Rating> ratings = eloService.getRatings();
		Assert.assertEquals(2, ratings.size());
		Assert.assertEquals(loser, ratings.get(0).getName());
		Assert.assertEquals(1492, ratings.get(0).getPoints());
		Assert.assertEquals(winner, ratings.get(1).getName());
		Assert.assertEquals(1213, ratings.get(1).getPoints());
	}
}