package com.daveclay.midi;

import java.util.Collections;
import java.util.List;

/**
 */
public class RootNoteValueRange implements NoteValueRange {

	@Override
	public List<NoteValue> getAvailableNotes(Key key) {
		return Collections.singletonList(key.getRootNote());
	}

	@Override
	public NoteValueRange copy() {
		return new RootNoteValueRange();
	}
}
