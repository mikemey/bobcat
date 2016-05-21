package uk.mm.bobcat.service;

import java.util.List;

import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import uk.mm.bobcat.ARunningMongoDBTest;
import uk.mm.bobcat.domain.NamePair;
import uk.mm.bobcat.domain.Rating;
import uk.mm.bobcat.service.repos.RatingRepository;

public class EloServiceRemoveNameTest extends ARunningMongoDBTest {
	private static final String NAME = "test_";

	@Autowired
	private EloService eloService;
	@Autowired
	private RatingRepository ratingRepository;

	private void setupTestData(int nameCount) {
		String prevName = null;
		for (int i = 0; i < nameCount; i++) {
			String currentName = NAME + i;
			eloService.addNameInCompetition(currentName);
			if (prevName != null) {
				for (int j = 0; j < i; j += 2) {
					eloService.newCompareResult(prevName, currentName);
				}
			}
			prevName = currentName;
		}
	}

	@Test
	public void testRemoveNameFromCompetition() {
		setupTestData(20);
		eloService.removeNameFromCompetition(NAME + 10);
		List<Rating> ratings = eloService.getRatings();
		Assert.assertEquals(19, ratings.size());
		for (Rating rating : ratings) {
			Assert.assertFalse(rating.getName().equals(NAME + 10));
		}
	}

	@Test
	public void testOnlyOneNameLeft() {
		setupTestData(3);
		eloService.removeNameFromCompetition(NAME + 0);
		eloService.removeNameFromCompetition(NAME + 1);

		List<Rating> ratings = eloService.getRatings();
		Assert.assertEquals(1, ratings.size());
		NamePair invalidPair = eloService.getCompetingPair();
		Assert.assertEquals(NamePair.NOT_ENOUGH_NAMES, invalidPair);
	}

	@Test
	public void testAddAlreadyRemovedName() {
		setupTestData(3);
		Rating rating = ratingRepository.findAll().get(1);
		String oldName = rating.getName();
		int oldPoints = rating.getPoints();
		eloService.removeNameFromCompetition(oldName);
		Assert.assertEquals(3, ratingRepository.findAll().size());

		List<Rating> ratings = eloService.getRatings();
		Assert.assertEquals(2, ratings.size());
		eloService.addNameInCompetition(oldName);
		Assert.assertEquals(3, ratingRepository.findAll().size());
		ratings = eloService.getRatings();
		Assert.assertEquals(3, ratings.size());
		boolean found = false;
		for (Rating rat : ratings) {
			if (rat.getName().equals(oldName)) {
				found = true;
				Assert.assertEquals(oldPoints, rat.getPoints());
			}
		}
		Assert.assertTrue(found);
	}

	@Test
	public void testOnlyTwoNamesLeft() {
		setupTestData(4);
		eloService.removeNameFromCompetition(NAME + 0);
		eloService.removeNameFromCompetition(NAME + 3);

		List<Rating> ratings = eloService.getRatings();
		Assert.assertEquals(2, ratings.size());
		NamePair onlyPair = eloService.getCompetingPair();

		// check only the two available names are drawn:
		for (int i = 0; i < 10; i++) {
			onlyPair = eloService.getCompetingPair();
			Assert.assertTrue(onlyPair.isValid());
			String first = onlyPair.getFirst();
			String second = onlyPair.getSecond();

			Assert.assertFalse(first.equals(second));
			Assert.assertFalse(first.equals(NAME + 0));
			Assert.assertFalse(second.equals(NAME + 0));
			Assert.assertFalse(first.equals(NAME + 3));
			Assert.assertFalse(second.equals(NAME + 3));
			Assert.assertTrue(first.equals(NAME + 1) || first.equals(NAME + 2));
			Assert.assertTrue(second.equals(NAME + 1) || second.equals(NAME + 2));
		}
	}

	@Test
	public void testRemovedNameNotInCompetition() {
		setupTestData(4);
		try {
			eloService.removeNameFromCompetition(NAME + 20);
			Assert.fail();
		} catch (IllegalArgumentException iae) {
			Assert.assertEquals("name not found in competition: test_20", iae.getMessage());
		}
	}
}