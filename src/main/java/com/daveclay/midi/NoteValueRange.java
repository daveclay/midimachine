package com.daveclay.midi;

import java.util.List;

/**
 */
public interface NoteValueRange {

	List<NoteValue> getAvailableNotes(Key key);
	NoteValueRange copy();
}
