package uk.mm.bobcat.service;

import java.util.List;

import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import uk.mm.bobcat.ARunningMongoDBTest;
import uk.mm.bobcat.domain.Rating;
import uk.mm.bobcat.service.repos.RatingRepository;

public class EloServiceModifyNameTest extends ARunningMongoDBTest {
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
	public void testModifyNameFromCompetition() {
		setupTestData(3);
		List<Rating> ratings = eloService.getRatings();
		Rating rut = ratings.get(1);
		String ratingId = rut.getId();
		int ratingPoints = rut.getPoints();

		eloService.modifyName(rut.getName(), "newname");
		ratings = eloService.getRatings();
		Assert.assertEquals(3, ratings.size());

		boolean found = false;
		for (Rating rating : ratings) {
			if (ratingId.equals(rating.getId())) {
				found = true;
				Assert.assertEquals("newname", rating.getName());
				Assert.assertEquals(ratingPoints, rating.getPoints());
			}
		}
		Assert.assertTrue(found);
	}

	@Test
	public void testRemovingOldName() {
		setupTestData(10);
		eloService.modifyName(NAME + 2, NAME + 20);
		try {
			eloService.removeNameFromCompetition(NAME + 2);
			Assert.fail();
		} catch (IllegalArgumentException iae) {
			Assert.assertEquals("name not found in competition: test_2", iae.getMessage());
		}
	}

	@Test
	public void testRemovingModifiedName() {
		setupTestData(10);
		eloService.modifyName(NAME + 2, NAME + 20);
		eloService.removeNameFromCompetition(NAME + 20);
		Assert.assertEquals(9, eloService.getRatings().size());
	}
}