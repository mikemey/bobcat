package uk.mm.bobcat.controller;

import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import uk.mm.bobcat.AStringInputValuesTest;

public class ControllerInputValuesTest extends AStringInputValuesTest {

	private static final String DEFAULT_ERROR_MESSAGE = "Name can't be null or empty!";
	private static CompetitionController competitionController;
	private static RankingController rankingController;

	@BeforeClass
	public static void setupControllers() {
		competitionController = new CompetitionController(null);
		rankingController = new RankingController(null);
	}

	public ControllerInputValuesTest(String inputValue) {
		super(inputValue);
	}

	@Test
	public void testCompetitionControllerAddMatchResultLoserNull() {
		try {
			competitionController.postPreferredName("winner", inputValue);
			Assert.fail("expected exception not thrown!");
		} catch (IllegalArgumentException iae) {
			Assert.assertEquals("Loser name can't be null or empty!", iae.getMessage());
		}
	}

	@Test
	public void testCompetitionControllerAddMatchResultWinnerNull() {
		try {
			competitionController.postPreferredName(inputValue, "loser");
			Assert.fail("expected exception not thrown!");
		} catch (IllegalArgumentException iae) {
			Assert.assertEquals("Winner name can't be null or empty!", iae.getMessage());
		}
	}

	@Test
	public void testRankingControllerAddName() {
		try {
			rankingController.postAddNameToCompetition(inputValue);
			Assert.fail("expected exception not thrown!");
		} catch (IllegalArgumentException iae) {
			Assert.assertEquals(DEFAULT_ERROR_MESSAGE, iae.getMessage());
		}
	}

	@Test
	public void testRankingControllerRemoveName() {
		try {
			rankingController.postRemoveNameFromCompetition(inputValue);
			Assert.fail("expected exception not thrown!");
		} catch (IllegalArgumentException iae) {
			Assert.assertEquals(DEFAULT_ERROR_MESSAGE, iae.getMessage());
		}
	}

	@Test
	public void testRankingControllerModifyNameOldNameNull() {
		try {
			rankingController.postModifyNameInCompetition(inputValue, "newname");
			Assert.fail("expected exception not thrown!");
		} catch (IllegalArgumentException iae) {
			Assert.assertEquals("Original name can't be null or empty!", iae.getMessage());
		}
	}

	@Test
	public void testRankingControllerModifyNameNewNameNull() {
		try {
			rankingController.postModifyNameInCompetition("oldname", inputValue);
			Assert.fail("expected exception not thrown!");
		} catch (IllegalArgumentException iae) {
			Assert.assertEquals("New name can't be null or empty!", iae.getMessage());
		}
	}
}
