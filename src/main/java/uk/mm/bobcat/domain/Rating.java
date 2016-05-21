package uk.mm.bobcat.domain;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * @author michael
 *
 */
@Document(collection = "ratings")
public class Rating {
	public static final int START_ELO_POINTS = 1400;
	private static final int UNRATED = Integer.MIN_VALUE;

	@Id
	private String id;

	@Indexed(unique = true)
	private long index;
	private String name;
	private int eloPoints;
	private boolean active = true;
	private int matchCounter;

	public Rating() {
		this.eloPoints = UNRATED;
	}

	public Rating(long index, String name, int eloPoints, int cmpCount, boolean active) {
		this.index = index;
		this.name = name;
		this.eloPoints = eloPoints;
		this.matchCounter = cmpCount;
		this.active = active;
	}

	public String getId() {
		return id;
	}

	public long getIndex() {
		return index;
	}

	public void setIndex(long counter) {
		this.index = counter;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getMatchCounter() {
		return matchCounter;
	}

	public void setMatchCounter(int matchCounter) {
		this.matchCounter = matchCounter;
	}

	public int getPoints() {
		return eloPoints;
	}

	public int getRatingPoints() {
		return eloPoints - START_ELO_POINTS;
	}

	public void setPoints(int eloPoints) {
		matchCounter++;
		this.eloPoints = eloPoints;
	}

	public boolean isActive() {
		return active;
	}

	public void setActive(boolean active) {
		this.active = active;
	}

	@Override
	public String toString() {
		return "Rating [" + name + ", elo=" + eloPoints + ", match-count=" + matchCounter + ", active=" + active + "]";
	}
}