package com.daveclay.midi.random;

import com.daveclay.midi.realtime.Notes;
import com.daveclay.midi.realtime.RealtimeNotesStrategy;
import com.daveclay.midi.sequence.*;
import com.daveclay.midi.Key;
import com.daveclay.midi.NoteValueRange;

public class RandomNoteStrategy extends AbstractRandomNoteStrategy implements NoteEventStrategy {

	private int minDuration = 0;
	private int maxDuration = 20;
	private int minVelocity = 10;
	private int maxVelocity = 127;
	private int smoothDurationByVelocity = 20;
	private int maxNumberOfNotes = 1;
	private int minNumberOfNotes = 1;
	public NoteValueRange noteValueRange;

	@Override
	public RealtimeNotesStrategy copy() {
		RandomNoteStrategy copy = new RandomNoteStrategy();
		copy.minDuration = getMinDuration();
		copy.maxDuration = getMaxDuration();
		copy.minVelocity = getMinVelocity();
		copy.maxVelocity = getMaxVelocity();
		copy.smoothDurationByVelocity = getSmoothDurationByVelocity();
		copy.maxNumberOfNotes = getMaxNumberOfNotes();
		copy.minNumberOfNotes = getMinNumberOfNotes();
		copy.noteValueRange = getNoteValueRange().copy();
		return copy;
	}

	public SequenceEvent createEvent(Key key) {
		Notes notes = getNotes(key);
		NotesSequenceEvent event = new NotesSequenceEvent();
		event.setNotes(notes.getNotes());
		return event;
	}

	@Override
	public int getMaxDuration() {
		return maxDuration;
	}

	public void setMaxDuration(int maxDuration) {
		this.maxDuration = maxDuration;
	}

	@Override
	public int getMinVelocity() {
		return minVelocity;
	}

	public void setMinVelocity(int minVelocity) {
		this.minVelocity = minVelocity;
	}

	@Override
	public int getMaxVelocity() {
		return maxVelocity;
	}

	public void setMaxVelocity(int maxVelocity) {
		this.maxVelocity = maxVelocity;
	}

	public int getSmoothDurationByVelocity() {
		return smoothDurationByVelocity;
	}

	public void setSmoothDurationByVelocity(int smoothDurationByVelocity) {
		this.smoothDurationByVelocity = smoothDurationByVelocity;
	}

	@Override
	public int getMinNumberOfNotes() {
		return minNumberOfNotes;
	}

	public void setMinNumberOfNotes(int minNumberOfNotes) {
		this.minNumberOfNotes = minNumberOfNotes;
	}

	@Override
	public int getMaxNumberOfNotes() {
		return maxNumberOfNotes;
	}

	public void setMaxNumberOfNotes(int maxNumberOfNotes) {
		this.maxNumberOfNotes = maxNumberOfNotes;
	}

	public void setMinDuration(int minDuration) {
		this.minDuration = minDuration;
	}

	@Override
	public NoteValueRange getNoteValueRange() {
		return noteValueRange;
	}

	public void setNoteValueRange(NoteValueRange noteValueRange) {
		this.noteValueRange = noteValueRange;
	}

	@Override
	public int getMinDuration() {
		return minDuration;
	}
}
