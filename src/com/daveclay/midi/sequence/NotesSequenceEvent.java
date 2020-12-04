package com.daveclay.midi.sequence;

import com.daveclay.midi.Note;
import com.daveclay.midi.sequence.SequenceEvent;

import java.util.ArrayList;
import java.util.List;

import javax.sound.midi.InvalidMidiDataException;
import javax.sound.midi.Track;

public class NotesSequenceEvent implements SequenceEvent {

	private List<Note> notes = new ArrayList<Note>();
	private int strumTicks;
	private String displayName;
	
	public List<Note> getNotes() {
		return notes;
	}

	public void setNotes(List<Note> notes) {
		this.notes = notes;
	}

	public void addNote(Note note) {
		notes.add(note);
	}
	
	public int getStrumTicks() {
		return strumTicks;
	}

	public void setStrumTicks(int strumTicks) {
		this.strumTicks = strumTicks;
	}
	
	public String getDisplayName() {
		return displayName;
	}

	public void setDisplayName(String displayName) {
		this.displayName = displayName;
	}

	@Override
	public void addMIDIEvents(Track track, int channel, int tick)
			throws InvalidMidiDataException {
		int currentTick = tick;
		for (Note note: notes) {
			note.addMIDIEvents(track, channel, currentTick);
			currentTick += strumTicks;
		}
	}
	
	public String toString() {
		if (displayName != null) {
			return displayName;
		} else {
			return "mult";
		}
	}
}
