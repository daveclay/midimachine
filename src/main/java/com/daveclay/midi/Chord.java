package com.daveclay.midi;

/**
 */
public class Chord {
	private Note[] notes;

	public Chord(Note[] notes) {
		this.notes = notes;
	}

	public Note[] getNotes() {
		return notes;
	}

	public void setNotes(Note[] notes) {
		this.notes = notes;
	}
}
