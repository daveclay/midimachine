package com.daveclay.midi.random;

import com.daveclay.midi.sequence.SequenceEvent;
import com.daveclay.midi.Key;
import com.daveclay.midi.sequence.Measure;

/**
 */
public abstract class AbstractSequenceRhythmStrategy implements SequenceRhythmStrategy {

	public void makeNotes(Measure measure, RandomInstrument randomInstrument) {
		for (int i = 0; i < measure.getNumberOfPositions(); i++) {
			if (shouldMakeNote(i)) {
				NoteEventStrategy noteEventStrategy = getNoteEventStrategy(randomInstrument, i);
				Key key = randomInstrument.getKey();
				SequenceEvent sequenceEvent = noteEventStrategy.createEvent(key);
				measure.setEvent(i, sequenceEvent);
			}
		}
	}

	protected NoteEventStrategy getNoteEventStrategy(RandomInstrument randomInstrument, int positionInMeasure) {
		NoteEventStrategy noteEventStrategy = randomInstrument.getNoteEventStrategy();
		return noteEventStrategy;
	}

	protected abstract boolean shouldMakeNote(int position);
}
