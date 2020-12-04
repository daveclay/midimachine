package com.daveclay.midi;

import javax.sound.midi.InvalidMidiDataException;
import javax.sound.midi.ShortMessage;
import javax.sound.midi.Track;
import com.daveclay.midi.sequence.*;

/**
*/
public class Note implements SequenceEvent {
	private int key;
	private int velocity;
	private int duration;

	public Note() {
	}

	public Note(int key, int velocity, int duration) {
		this.key = key;
		this.velocity = velocity;
		this.duration = duration;
	}

	public int getKey() {
		return key;
	}

	public void setKey(int key) {
		this.key = key;
	}

	public int getVelocity() {
		return velocity;
	}

	public void setVelocity(int velocity) {
		this.velocity = velocity;
	}

	public int getDuration() {
		return duration;
	}

	public void setDuration(int duration) {
		this.duration = duration;
	}

	public ShortMessage asNoteOnMessage(int channel) throws InvalidMidiDataException {
		return buildNoteMessage(ShortMessage.NOTE_ON, channel);
	}

	public ShortMessage asNoteOffMessage(int channel) throws InvalidMidiDataException {
		return buildNoteMessage(ShortMessage.NOTE_OFF, channel);
	}

	private ShortMessage buildNoteMessage(int event, int channel) throws InvalidMidiDataException {
		ShortMessage msg = new ShortMessage();
		try {
			msg.setMessage(event, channel, getKey(), getVelocity());
		} catch (InvalidMidiDataException e) {
			System.out.println("Invalid note: " + this + " (note value: " + getKey() + ", velocity: " + getVelocity() + ")");
			throw e;
		}
		return msg;
	}

	public void addMIDIEvents(Track track, int channel, int tick) throws InvalidMidiDataException {
		track.add(MidiSequencer.createNoteOnEvent(channel, key, velocity, tick));
		track.add(MidiSequencer.createNoteOffEvent(channel, key, velocity, (tick + duration)));
	}
	
	private static final String[] toneNames = {
		"C", "C#", "D", "D#", "E", "F", "F#", "G", "G#", "A", "A#", "B"
	};

	public static String octavedNameForNote(NoteValue rootNote) {
		return rootNote.getNoteAndOctave();
	}

	public static String octavedNameForNote(int noteNumber) {
		int octave = noteNumber / 12;
		return nameForNote(noteNumber) + octave;	
	}
	public static String nameForNote(int noteNumber) {
		int tone = noteNumber % 12;
		return toneNames[tone];
	}
	public String toString() {
		return octavedNameForNote(key);
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;

		Note note = (Note) o;

		if (duration != note.duration) return false;
		if (key != note.key) return false;
		if (velocity != note.velocity) return false;

		return true;
	}

	@Override
	public int hashCode() {
		int result = key;
		result = 31 * result + velocity;
		result = 31 * result + duration;
		return result;
	}
}
