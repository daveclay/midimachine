package com.daveclay.midi.random;

import com.daveclay.midi.Key;
import com.daveclay.midi.sequence.*;

import javax.sound.midi.InvalidMidiDataException;
import javax.sound.midi.Track;

/**
*/
public class RandomInstrument {

	private InstrumentTrack instrumentTrack;
	private NoteEventStrategy noteEventStrategy;
	private SequenceRhythmStrategy rhythmStrategy;
	private Key key;
	private PhraseBuildingStrategy phraseBuildingStrategy;
	private boolean drum;

	public RandomInstrument(InstrumentTrack instrumentTrack) {
		this.instrumentTrack = instrumentTrack;
		this.phraseBuildingStrategy = new PhraseBuildingStrategy();
	}

	public boolean isDrum() {
		return drum;
	}

	public void setDrum(boolean drum) {
		this.drum = drum;
	}

	public InstrumentTrack getInstrumentTrack() {
		return instrumentTrack;
	}

	public void setInstrumentTrack(InstrumentTrack instrumentTrack) {
		this.instrumentTrack = instrumentTrack;
	}

	public SequenceRhythmStrategy getRhythmStrategy() {
		return rhythmStrategy;
	}

	public void setRhythmStrategy(SequenceRhythmStrategy rhythmStrategy) {
		this.rhythmStrategy = rhythmStrategy;
	}

	public NoteEventStrategy getNoteEventStrategy() {
		return noteEventStrategy;
	}

	public void setNoteEventStrategy(NoteEventStrategy noteEventStrategy) {
		this.noteEventStrategy = noteEventStrategy;
	}

	public Key getKey() {
		return key;
	}

	public void setKey(Key key) {
		this.key = key;
	}

	public PhraseBuildingStrategy getPhraseBuildingStrategy() {
		return phraseBuildingStrategy;
	}

	public void setPhraseBuildingStrategy(PhraseBuildingStrategy phraseBuildingStrategy) {
		this.phraseBuildingStrategy = phraseBuildingStrategy;
	}

	public void addToMeasure(Measure measure) {
		rhythmStrategy.makeNotes(measure, this);
	}

	public void addSongPart(int numberOfMeasuresInSongPart) {
		Phrase phrase = instrumentTrack.createPhrase();
		for (int i = 0; i < numberOfMeasuresInSongPart; i++) {
			Measure measure = instrumentTrack.createMeasure();
			phraseBuildingStrategy.addToMeasure(this, phrase, i, numberOfMeasuresInSongPart, measure);
		}
		instrumentTrack.addPhrase(phrase);
	}

	public void addMidiEvents(Track track) throws InvalidMidiDataException {
		instrumentTrack.addMIDIEvents(track);
	}
}
