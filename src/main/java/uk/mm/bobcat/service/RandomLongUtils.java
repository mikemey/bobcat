package uk.mm.bobcat.service;

import java.util.Random;

import org.springframework.stereotype.Component;

@Component
public class RandomLongUtils {
	private static final Random random = new Random(System.nanoTime());

	public long randomLong(long maxValue) {
		if (maxValue <= 0) {
			return 0;
		}
		return Math.abs(random.nextLong() % maxValue);
	}
}
