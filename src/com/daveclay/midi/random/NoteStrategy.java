package com.daveclay.midi.random;

import com.daveclay.midi.Note;

/**
*/
public interface NoteStrategy {
	public Note createNote(int[] notes);
}
