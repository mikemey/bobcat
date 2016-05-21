package uk.mm.bobcat.service;

import org.springframework.stereotype.Component;

import uk.mm.bobcat.domain.Rating;

@Component
public class EloCalculator {

	/**
	 * Calculates the new rating for both {@link Rating}s provided, assuming first rating parameter is represented the
	 * winner and the second parameter the loser respectively. <br />
	 * <b>Note:</b> New ratings will be set in the parameter-objects.
	 * @param winnerRating
	 * @param loserRating
	 */
	public void calculateNewRatings(Rating winnerRating, Rating loserRating) {
		int newWinnerRating = getNewCalculatedRating(winnerRating, loserRating, 1);
		int newLoserRating = getNewCalculatedRating(loserRating, winnerRating, 0);
		winnerRating.setPoints(newWinnerRating);
		loserRating.setPoints(newLoserRating);
	}

	/**
	 * result: 1 --> won, 0 --> lost
	 */
	private int getNewCalculatedRating(Rating ownRating, Rating otherRating, int result) {
		double expectation = calculateExpectation(ownRating, otherRating);
		double k = getKFactor(ownRating.getMatchCounter());
		return (int) Math.round(ownRating.getPoints() + k * (result - expectation));
	}

	private double calculateExpectation(Rating ownRating, Rating otherRating) {
		double diff = Math.pow(10, (otherRating.getPoints() - ownRating.getPoints()) / 400.0);
		return 1.0 / (1.0 + diff);
	}

	private double getKFactor(int i) {
		if (i < 5) {
			return 30;
		}
		if (i < 10) {
			return 15;
		}
		return 10;
	}
}
