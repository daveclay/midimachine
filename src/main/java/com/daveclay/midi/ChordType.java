package com.daveclay.midi;

import static com.daveclay.midi.NoteConstants.*;

public class ChordType {
	
	public static ChordType MAJOR_TRIAD = new ChordType(
		"Major Triad", "", new int[] { UNISON, MAJOR_THIRD, FIFTH });
	
	public static ChordType MAJOR_SUSPENDED_FOURTH_TRIAD = new ChordType(
		"Major Suspended Fourth Triad", "sus4", new int[] { UNISON, FOURTH, FIFTH} );

	public static ChordType MAJOR_SUSPENDED_SECOND_TRIAD = new ChordType(
			"Major Suspended Second Triad", "sus2", new int[] { UNISON, MAJOR_SECOND, FIFTH} );

	public static ChordType MINOR_TRIAD = new ChordType(
		"Minor Triad", "m", new int[] { UNISON, MINOR_THIRD, FIFTH });

	public static ChordType DOMINANT_SEVENTH_CHORD = new ChordType(
		"Dominant 7th Chord", "7", new int[] { UNISON, MAJOR_THIRD, FIFTH, MINOR_SEVENTH });

	public static ChordType MAJOR_SEVENTH_CHORD = new ChordType(
		"Major 7th Chord", "M7", new int[] { UNISON, MAJOR_THIRD, FIFTH, MAJOR_SEVENTH });
	
	public static ChordType MINOR_SEVENTH_CHORD = new ChordType(
		"Minor 7th Chord", "m7", new int[] { UNISON, MINOR_THIRD, FIFTH, MINOR_SEVENTH });
	
	public static ChordType DIMINISHED_TRIAD = new ChordType(
		"Diminished Triad", "dim", new int[] { UNISON, MINOR_THIRD, DIMINISHED_FIFTH });
	
	public static ChordType DIMINISHED_SEVENTH_CHORD = new ChordType(
		"Diminished 7th Chord", "dim7", new int[] { UNISON, MINOR_THIRD, DIMINISHED_FIFTH, DIMINISHED_SEVENTH });
	
	public static ChordType HALF_DIMINISHED_SEVENTH_CHORD = new ChordType(
		"Half-Diminished 7th Chord", "7b5", new int[] { UNISON, MINOR_THIRD, DIMINISHED_FIFTH, MINOR_SEVENTH });
	
	private String name;
	private String symbol;
	private int[] intervals;
	
	public ChordType(String name, String symbol, int[] intervals) {
		this.name = name;
		this.symbol = symbol;
		this.intervals = intervals;
	}

	public String getName() {
		return name;
	}
	
	public String getSymbol() {
		return symbol;
	}

	public int[] getIntervals() {
		return intervals;
	}
	
	public int[] getNoteValues(int rootNoteValue) {
		return getNoteValues(rootNoteValue,0);
	}
	
	public int[] getNoteValues(int rootNoteValue, int inversion) {
		int[] result = new int[getIntervals().length];
		int resultPos = 0;
		for (int i = inversion; i < getIntervals().length; i++) {
			result[resultPos++] = rootNoteValue + getIntervals()[i];
		}
		for (int i = 0; i < inversion; i++) {
			result[resultPos++] = rootNoteValue + getIntervals()[i] + OCTAVE;
		}
		return result;
	}
}
