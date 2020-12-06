package com.daveclay.midi;

import java.util.List;

/**
 */
public class InKeyNoteValueRange implements NoteValueRange {

	private NoteValue lowestNote = NoteValue.C0;
	private NoteValue highestNote = NoteValue.C5;

	public List<NoteValue> getAvailableNotes(Key key) {
		return key.getNotesBetween(lowestNote, highestNote.halfStepUp());
	}

	@Deprecated
	public void setLowestRootNoteInKeyAsInt(int lowestRootNoteInKey) {
		this.lowestNote = NoteValue.forInt(lowestRootNoteInKey);
	}

	@Deprecated
	public void setHighestRootNoteInKeyAsInt(int highestRootNoteInKey) {
		this.highestNote = NoteValue.forInt(highestRootNoteInKey);
	}

	public void setLowestNote(NoteValue lowestNote) {
		this.lowestNote = lowestNote;
	}

	public void setHighestNote(NoteValue highestNote) {
		this.highestNote = highestNote;
	}

	public NoteValue getLowestNote() {
		return lowestNote;
	}

	public NoteValue getHighestNote() {
		return highestNote;
	}

	@Override
	public NoteValueRange copy() {
		InKeyNoteValueRange copy = new InKeyNoteValueRange();
		copy.lowestNote = lowestNote;
		copy.highestNote = highestNote;
		return copy;
	}
}
