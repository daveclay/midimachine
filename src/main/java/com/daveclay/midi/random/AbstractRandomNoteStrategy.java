package com.daveclay.midi.random;

import com.daveclay.midi.Key;
import com.daveclay.midi.Note;
import com.daveclay.midi.NoteValue;
import com.daveclay.midi.NoteValueRange;
import com.daveclay.midi.realtime.Notes;
import com.daveclay.midi.realtime.RealtimeNotesStrategy;

import java.util.List;

/**
 */
public abstract class AbstractRandomNoteStrategy implements RealtimeNotesStrategy {

	@Override
	public Notes getNotes(Key key) {
		int minNumberOfNotes = getMinNumberOfNotes();
		int maxNumberOfNotes = getMaxNumberOfNotes();
		int numberOfNotes = RandyTheRandom.randomIntBetweenExclusive(minNumberOfNotes, maxNumberOfNotes);
		Notes notes = new Notes();
		for (int i = 0; i < numberOfNotes ; i++) {
			Note note = createNote(key);
			notes.addNote(note);
		}

		return notes;
	}

	public Note createNote(Key key) {
		// Todo: does InKeyNoteValueRange want some context? Or does just the note strategy?
		NoteValueRange noteValueRange = getNoteValueRange();
		int minDuration = getMinDuration();
		int maxDuration = getMaxDuration();
		int minVelocity = getMinVelocity();
		int maxVelocity = getMaxVelocity();

		List<NoteValue> availableNotes = noteValueRange.getAvailableNotes(key);

		int duration = minDuration + (RandyTheRandom.nextInt(maxDuration - minDuration + 1));
		int size = availableNotes.size();
		int i = RandyTheRandom.nextInt(size);
		NoteValue note = availableNotes.get(i);

		// Todo: add smoothing of velocity based on duration (short loud notes are more abrupt)
		int velocity = RandyTheRandom.randomIntBetweenExclusive(minVelocity, maxVelocity);

		Note noteObj = new Note();
		noteObj.setKey(note.getIntValue());
		noteObj.setVelocity(velocity);
		noteObj.setDuration(duration);

		return noteObj;
	}

	public abstract int getMaxDuration();

	public abstract int getMinVelocity();

	public abstract int getMaxVelocity();

	public abstract int getMinNumberOfNotes();

	public abstract int getMaxNumberOfNotes();

	public abstract NoteValueRange getNoteValueRange();

	public abstract int getMinDuration();
}
