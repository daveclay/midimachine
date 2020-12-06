package com.daveclay.midi.realtime.ui.util;

/**
*/
public class ValueMap {
	private int min;
	private int max;

	public ValueMap(int min, int max) {
		this.min = min;
		this.max = max;
	}

	public int getValue(double internalValue) {
		int range = max - min;
		return min + ((int) (internalValue * range));
	}

	public double getInternalValue(int value) {
		int range = max - min;
		int adjusted = value - min;
		return (double)adjusted / (double)range;
	}
}
