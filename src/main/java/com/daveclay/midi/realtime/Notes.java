package com.daveclay.midi.realtime;

import com.daveclay.midi.Note;

import java.util.ArrayList;
import java.util.List;

/**
 */
public class Notes {

	public List<Note> notes = new ArrayList<Note>();

	public List<Note> getNotes() {
		return notes;
	}

	public void addNote(Note note) {
		notes.add(note);
	}

	public void setNotes(List<Note> notes) {
		this.notes = notes;
	}

	public Notes copy() {
		Notes copy = new Notes();
		copy.notes = new ArrayList<Note>(notes);
		return copy;
	}
}
