package com.daveclay.midi;

import java.util.ArrayList;
import java.util.List;

/**
 */
public class CustomNoteValueRange implements NoteValueRange {

	private List<NoteValue> availableNotes;

	@Override
	public List<NoteValue> getAvailableNotes(Key key) {
		return availableNotes;
	}

	@Deprecated
	public void setAvailableNotesAsInt(int[] availableNotes) {
		List<NoteValue> notes = new ArrayList<NoteValue>(availableNotes.length);
		for (int availableNote : availableNotes) {
			notes.add(NoteValue.forInt(availableNote));
		}

		setAvailableNotes(notes);
	}

	@Deprecated
	public void setAvailableNoteAsInt(int note) {
		setAvailableNotesAsInt(new int[] { note } );
	}

	public void setAvailableNotes(List<NoteValue> availableNotes) {
		this.availableNotes = availableNotes;
	}

	@Override
	public NoteValueRange copy() {
		CustomNoteValueRange copy = new CustomNoteValueRange();
		copy.availableNotes = new ArrayList<NoteValue>(availableNotes);
		return copy;
	}
}
