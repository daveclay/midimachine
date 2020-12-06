package com.daveclay.midi;

import static com.daveclay.midi.Interval.*;

public enum Scale {

	UNISON_SCALE( "Unison", UNISON ),
	PERFECT_SCALE( "Perfect", UNISON, FOURTH, FIFTH),
	MINOR_PENTATONIC_SCALE( "Minor Pentatonic", UNISON, MINOR_THIRD, FOURTH, FIFTH, MINOR_SEVENTH),
	MAJOR_PENTATONIC_SCALE( "Major Pentatonic", UNISON, MAJOR_THIRD, FOURTH, FIFTH, MAJOR_SEVENTH),
	BLUES_CHROMATIC_SCALE( "Blues Chromatic", UNISON, MINOR_THIRD, MAJOR_THIRD, FOURTH, FLAT_FIFTH, FIFTH, MINOR_SEVENTH),
	BLUES_MAJOR_SCALE( "Blues Major", UNISON, MAJOR_THIRD, FOURTH, FLAT_FIFTH, FIFTH, MAJOR_SEVENTH),
	BLUES_MINOR_SCALE( "Blues Minor", UNISON, MINOR_THIRD, FOURTH, FLAT_FIFTH, FIFTH, MINOR_SEVENTH),
	MINOR_SCALE( "Minor", UNISON, MAJOR_SECOND, MINOR_THIRD, FOURTH, FIFTH, MINOR_SIXTH, MINOR_SEVENTH),
	MAJOR_SCALE( "Major", UNISON, MAJOR_SECOND, MAJOR_THIRD, FOURTH, FIFTH, MAJOR_SIXTH, MAJOR_SEVENTH);

	private String name;
	private Interval[] intervals;

	private Scale(String name, Interval... intervals) {
		this.name = name;
		this.intervals = intervals;
	}

	public static interface IntervalFunctor {
		public void interval(Interval interval);
	}

	public String getName() {
		return name;
	}

	public void forEachInterval(IntervalFunctor intervalFunctor) {
		for (Interval interval : intervals) {
			intervalFunctor.interval(interval);
		}
	}

	public Interval[] getIntervals() {
		return intervals;
	}

	public int[] asIntArray() {
		int[] ints = new int[intervals.length];
		for (int i = 0; i < intervals.length; i++) {
			ints[i] = intervals[i].asInt();
		}
		return ints;
	}

	public static final Scale[] MINOR_SCALES = new Scale[] {
			MINOR_PENTATONIC_SCALE,
			MINOR_SCALE
	};

	public static final Scale[] MAJOR_SCALES = new Scale[] {
			MAJOR_PENTATONIC_SCALE,
			MAJOR_SCALE
	};

	public static boolean isMinor(Scale scale) {
		for (Scale aScale : MINOR_SCALES) {
			if (aScale == scale) {
				return true;
			}
		}
		return false;
	}

	public static boolean isMajor(Scale scale) {
		for (Scale aScale : MAJOR_SCALES) {
			if (aScale == scale) {
				return true;
			}
		}
		return false;
	}

	public boolean isNoteInScale(NoteValue currentNote) {
		return false;
	}
}
