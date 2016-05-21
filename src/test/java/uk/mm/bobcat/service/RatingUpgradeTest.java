package uk.mm.bobcat.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import uk.mm.bobcat.ARunningMongoDBTest;
import uk.mm.bobcat.domain.Rating;

public class RatingUpgradeTest extends ARunningMongoDBTest {
	@Autowired
	private EloService eloService;

	@Test
	public void testReadOldRatingsForCompetition() {
		Map<String, Object> data = new HashMap<>();
		data.put("name", "name_0");
		data.put("index", 0);
		data.put("eloPoints", 1500);
		data.put("matchCounter", 3);
		mongoTemplate.save(data, "ratings");

		eloService.addNameInCompetition("name_1");
		eloService.addNameInCompetition("name_2");
		List<Rating> ratings = eloService.getRatings();
		Assert.assertEquals(3, ratings.size());
		Assert.assertEquals("name_0", ratings.get(0).getName());
	}
}