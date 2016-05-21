package uk.mm.bobcat.domain;

public class NamePair {
	public static final NamePair NOT_ENOUGH_NAMES = new NamePair("Add more names to the name competition!");
	private final String first;
	private final String second;

	public NamePair(String first) {
		this.first = first;
		this.second = null;
	}

	public NamePair(String first, String second) {
		this.first = first;
		this.second = second;
	}

	public String getFirst() {
		return first;
	}

	public String getSecond() {
		return second;
	}

	public boolean isValid() {
		return second != null;
	}

	@Override
	public String toString() {
		return isValid() ? first + " - " + second : first;
	}
}