package com.daveclay.midi.random;

import java.util.ArrayList;
import java.util.Random;

/**
 */
public class RandyTheRandom {
	public static Random random = new Random(System.currentTimeMillis());

	public static int nextInt(int maxValue) {
		return random.nextInt(maxValue);
	}

	public static double nextDouble() {
		return random.nextDouble();
	}
	
	public static int randomIntBetweenInclusive(int lower, int upper) {
		int result = (int)Math.floor(Math.random() * (upper+1-lower)) + lower;
		return result;
	}
	
	public static int randomIntBetweenExclusive(int lower, int upper) {
		int result = (int)Math.floor(Math.random() * (upper - lower)) + lower;
		return result;
	}
	
	public static float randomFloatBetweenExclusive(float lower, float upper) {
		return (float)(Math.random() * (upper - lower)) + lower; 
	}
	
	public static <T> T randomElementFromArray(T[] array) {
		T result = array[randomIntBetweenExclusive(0, array.length)];
		return result;
	}
	
	public static int randomElementFromArray(int[] array) {
		int result = array[randomIntBetweenExclusive(0, array.length)];
		return result;
	}
	
	public static int randomElementFromArrayExcludingElements(int[] array, int[] exclusions) {
		ArrayList<Integer> acceptableOptions = new ArrayList<Integer>();
		for (int i = 0; i < array.length; i++) {
			boolean acceptable = true;
			for (int j = 0; j < exclusions.length; j++) {
				if (array[i] == exclusions[j]) {
					acceptable = false;
					break;
				}
			}
			if (acceptable) {
				acceptableOptions.add(array[i]);
			}
		}
		if (acceptableOptions.size() == 0) {
			throw new RuntimeException("Choosing an element excluding elements resulted in 0 acceptable options.");
		}
		int result;
		result = acceptableOptions.get(randomIntBetweenExclusive(0, acceptableOptions.size()));
		return result;
	}
	
	public static int randomIntFromMeanWithPercentageVariation(int mean, double variation) {
		int result = mean + mean * (int)Math.round((Math.random() * variation) - variation / 2.0);
		return result;
	}
}
