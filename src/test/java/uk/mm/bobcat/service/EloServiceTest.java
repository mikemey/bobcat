package uk.mm.bobcat.service;

import java.util.List;

import org.apache.commons.lang.ArrayUtils;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import uk.mm.bobcat.ARunningMongoDBTest;
import uk.mm.bobcat.domain.NamePair;
import uk.mm.bobcat.domain.Rating;
import uk.mm.bobcat.service.repos.RatingRepository;

public class EloServiceTest extends ARunningMongoDBTest {

	@Autowired
	private EloService eloService;
	@Autowired
	private RatingRepository ratingRepository;

	@Test
	public void testNoNamesAvailable() {
		NamePair result = eloService.getCompetingPair();
		Assert.assertFalse(result.isValid());
		Assert.assertEquals("Add more names to the name competition!", result.getFirst());
	}

	@Test
	public void testOnlyOneNameAvailable() {
		eloService.addNameInCompetition("blubl");
		NamePair result = eloService.getCompetingPair();
		Assert.assertFalse(result.isValid());
		Assert.assertEquals("Add more names to the name competition!", result.getFirst());
	}

	@Test
	public void testCompetingPair() {
		eloService.addNameInCompetition("Miriam");
		eloService.addNameInCompetition("Sandra");
		NamePair pair = eloService.getCompetingPair();
		Assert.assertNotNull(pair);
		Assert.assertTrue(pair.isValid());
		Assert.assertFalse(pair.getFirst().equals(pair.getSecond()));
		Assert.assertTrue(ArrayUtils.contains(new String[] { pair.getFirst(), pair.getSecond() }, "Miriam"));
		Assert.assertTrue(ArrayUtils.contains(new String[] { pair.getFirst(), pair.getSecond() }, "Sandra"));
	}

	@Test
	public void testDuplicate() {
		eloService.addNameInCompetition("Victoria");
		List<Rating> ratings = eloService.getRatings();
		Assert.assertEquals(1, ratings.size());
		Assert.assertEquals(1400, ratings.get(0).getPoints());
		ratings.get(0).setPoints(1500);
		ratingRepository.save(ratings);
		eloService.addNameInCompetition("Victoria");
		ratings = eloService.getRatings();
		Assert.assertEquals(1, ratings.size());
		Assert.assertEquals(1500, ratings.get(0).getPoints());
	}

	@Test
	public void testWinnerNotInCompetition() {
		try {
			eloService.newCompareResult("Joane", "Mary");
			Assert.fail();
		} catch (IllegalArgumentException iae) {
			Assert.assertEquals("name not found in competition: Joane", iae.getMessage());
		}
	}

	@Test
	public void testLoserNotInCompetition() {
		eloService.addNameInCompetition("Marianne");
		try {
			eloService.newCompareResult("Marianne", "Claudia");
			Assert.fail();
		} catch (IllegalArgumentException iae) {
			Assert.assertEquals("name not found in competition: Claudia", iae.getMessage());
		}
	}
}