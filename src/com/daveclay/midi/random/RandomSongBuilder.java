package com.daveclay.midi.random;

import com.daveclay.midi.Interval;
import static com.daveclay.midi.Interval.*;
import com.daveclay.midi.Key;
import com.daveclay.midi.Scale;
import static com.daveclay.midi.Scale.*;
import com.daveclay.midi.NoteValue;
import com.daveclay.midi.Note;

import javax.sound.midi.InvalidMidiDataException;
import javax.sound.midi.Track;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

/**
 */
public class RandomSongBuilder {

	private List<RandomInstrument> randomInstruments = new ArrayList<RandomInstrument>();
	private Stack<SongPart> songParts = new Stack<SongPart>();

	public void buildSong() throws InvalidMidiDataException {
		int numberOfParts = getNumberOfSongParts();
		int numberOfMeasuresInSongPart = getNumberOfMeasuresInSongPart();
		for (int songParts = 0; songParts < numberOfParts; songParts++) {
			Interval intervalToModulateTo = getIntervalToModulateTo();
			for (RandomInstrument randomInstrument : randomInstruments) {
				randomInstrument.addSongPart(numberOfMeasuresInSongPart);

				if ( ! randomInstrument.isDrum()) {
					Key rootKey = randomInstrument.getKey();
					Key key = modulateFromKey(rootKey, intervalToModulateTo);
					randomInstrument.setKey(key);
				}
			}
		}
	}

	public static class SongPart {
		private String name;
		private Key key;
		private int measuresInPart;

		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}

		public Key getKey() {
			return key;
		}

		public void setKey(Key key) {
			this.key = key;
		}

		public int getMeasuresInPart() {
			return measuresInPart;
		}

		public void setMeasuresInPart(int measuresInPart) {
			this.measuresInPart = measuresInPart;
		}
	}

	private int getNumberOfSongParts() {
		return RandyTheRandom.randomIntBetweenInclusive(2, 4);
	}

	private int getNumberOfMeasuresInSongPart() {
		return RandyTheRandom.randomIntBetweenInclusive(2, 8);
	}

	public void modulateKey() {
		Interval intervalToModulateTo = getIntervalToModulateTo();
		for (RandomInstrument randomInstrument : randomInstruments) {
			modulateKeyInPart(randomInstrument, intervalToModulateTo);
		}
	}

	private void modulateKeyInPart(RandomInstrument randomInstrument, Interval intervalToModulateTo) {
		Key rootKey = randomInstrument.getKey();

		Key key = modulateFromKey(rootKey, intervalToModulateTo);
		randomInstrument.setKey(key);
	}

	public Key modulateFromKey(Key key, Interval intervalToModulateTo) {
		// Todo: someday, take in the melody context
		NoteValue noteForInterval = key.getNoteForInterval(intervalToModulateTo);
		NoteValue note = noteForInterval;


		if (note.isLower(NoteValue.C2)) {
			note = Interval.OCTAVE.noteAbove(note);
		} else if (note.isHigher(NoteValue.C3)) {
			note = Interval.OCTAVE.noteBelow(note);
		}

		Scale scale = key.getScale();
		if (intervalToModulateTo == MINOR_THIRD && isMinor(scale)) {
			System.out.println("modulating to major from minor");
			if (scale == MINOR_PENTATONIC_SCALE) {
				scale = MAJOR_PENTATONIC_SCALE;
			} else {
				scale = MAJOR_SCALE;
			}
		} else if (intervalToModulateTo == MAJOR_SIXTH && isMajor(scale)) {
			System.out.println("modulatin gto minor from major");
			if (scale == MAJOR_PENTATONIC_SCALE) {
				scale = MINOR_PENTATONIC_SCALE;
			} else {
				scale = MINOR_SCALE;
			}
		}

		System.out.println("modulating from " + Note.octavedNameForNote(key.getRootNote()) + " to " + Note.octavedNameForNote(noteForInterval));
		return new Key(note, scale);
	}

	private Interval getIntervalToModulateTo() {
		if (RandyTheRandom.nextInt(10) > 2) {
			return FIFTH;
		} else {
			return MINOR_THIRD;
		}
	}

	public void addInstrument(RandomInstrument randomInstrument) {
		randomInstruments.add(randomInstrument);
	}

	public void addMidiEvents(Track track) throws InvalidMidiDataException {
		for (RandomInstrument randomInstrument : randomInstruments) {
			randomInstrument.addMidiEvents(track);
		}
	}
}

