package com.daveclay.midi;

import com.daveclay.midi.sequence.*;
import static com.daveclay.midi.random.RandyTheRandom.*;
import static com.daveclay.midi.NoteConstants.OCTAVE;

public class ChordStrummer {
	private int rootNote;
	private ChordType chordType;
	private int inversion = 0;
	private int meanDuration = 4;
	private double durationVariation = 0.2;
	private int strumTicks = 4;
	private int meanVelocity = 64;
	private double velocityVariation = 0.2;
	private int octaves = 1;
	private boolean upstroke = false;
	
	public ChordStrummer() {
	}

	public int getRootNote() {
		return rootNote;
	}

	public void setRootNote(int rootNote) {
		this.rootNote = rootNote;
	}

	public ChordType getChordType() {
		return chordType;
	}

	public void setChordType(ChordType chordType) {
		this.chordType = chordType;
	}

	public int getInversion() {
		return inversion;
	}

	public void setInversion(int inversion) {
		this.inversion = inversion;
	}

	public int getMeanDuration() {
		return meanDuration;
	}

	public void setMeanDuration(int meanDuration) {
		this.meanDuration = meanDuration;
	}

	public double getDurationVariation() {
		return durationVariation;
	}

	public void setDurationVariation(double durationVariation) {
		this.durationVariation = durationVariation;
	}

	public int getStrumTicks() {
		return strumTicks;
	}

	public void setStrumTicks(int strumTicks) {
		this.strumTicks = strumTicks;
	}

	public int getMeanVelocity() {
		return meanVelocity;
	}

	public void setMeanVelocity(int meanVelocity) {
		this.meanVelocity = meanVelocity;
	}

	public double getVelocityVariation() {
		return velocityVariation;
	}

	public void setVelocityVariation(double velocityVariation) {
		this.velocityVariation = velocityVariation;
	}
	
	public int getOctaves() {
		return octaves;
	}

	public void setOctaves(int octaves) {
		this.octaves = octaves;
	}

	public boolean isUpstroke() {
		return upstroke;
	}

	public void setUpstroke(boolean upstroke) {
		this.upstroke = upstroke;
	}

	public NotesSequenceEvent createStrumEvent() {
		NotesSequenceEvent event = new NotesSequenceEvent();
		event.setStrumTicks(strumTicks);
		int[] noteValues = chordType.getNoteValues(rootNote,inversion);
		int[] fullNoteValues = new int[noteValues.length * octaves];
		for (int i = 0; i < octaves; i++) {
			for (int j = 0; j < noteValues.length; j++) {
				fullNoteValues[j*(i + 1)] = noteValues[j] + (OCTAVE * i);
			}
		}
		for (int i = 0; i < fullNoteValues.length; i++) {
			int velocity = randomIntFromMeanWithPercentageVariation(meanVelocity,velocityVariation);
			int duration = randomIntFromMeanWithPercentageVariation(meanDuration, durationVariation);
			int noteIndex = upstroke ? (fullNoteValues.length - 1 - i) : i;
			event.addNote(new Note(fullNoteValues[noteIndex],velocity,duration));
		}
		return event;
	}
}
