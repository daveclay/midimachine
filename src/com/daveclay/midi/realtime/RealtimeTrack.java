package com.daveclay.midi.realtime;

import com.daveclay.midi.Key;
import com.daveclay.midi.Note;

import javax.sound.midi.InvalidMidiDataException;
import javax.sound.midi.ShortMessage;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/*
// Todo: the order of this if block implicitly defines priority of when notes are triggered:
// the TickRhythm will never override the duration of the note; note off events will always be fired
// before note on events. Specifically, we could determine whether unison notes _should_ overlap, based on
// the TickRhythm implementation. For example:
//  tickRhythm.getNotesForTick(tick, lastNoteSent, lastNoteSentTick);
// or, more concisely:
//  tickRhythm.getNotesForTick(tick, thereIsAlreadyANotePlaying);
// or, more likely:
//  tickRhythm.getNotesForTick(tick, notesAlreadyPlaying);
// which might be a NoteEventsContext object
 */
public class RealtimeTrack implements Saveable {

	private int channel = 9;
	private String name;
	private Key key;
	private TickRhythm tickRhythm;
	private boolean mute;
	private TrackListener trackListener;

	// state
	private Map<Integer, LastNotesState> noteValueToLastNoteState = new HashMap<Integer, LastNotesState>();

	public RealtimeTrack() {
	}

	public RealtimeTrack(String name, Key key) {
		this(name, key, 9);
	}

	public RealtimeTrack(String name, Key key, int channel) {
		this.name = name;
		this.key = key;
		this.channel = channel;
	}

	public void setTrackListener(TrackListener trackListener) {
		this.trackListener = trackListener;
	}

	public List<ShortMessage> getShortMessagesForTick(long tick) throws InvalidMidiDataException {
		List<ShortMessage> messages = new ArrayList<ShortMessage>();

		// Calcualte the note on messages before the note off messages. This way the note on will
		// check to see if a note is already playing, and won't send a note on and note off on the same tick
		List<ShortMessage> noteOnMessages = getNoteOnMessages(tick);
		List<ShortMessage> noteOffMessages = getNoteOffMessages(tick);

		messages.addAll(noteOffMessages);
		messages.addAll(noteOnMessages);

		return messages;
	}

	private List<ShortMessage> getNoteOffMessages(long tick) throws InvalidMidiDataException {
		List<ShortMessage> noteOffMessages = new ArrayList<ShortMessage>();
		List<Integer> keysToRemove = new ArrayList<Integer>();
		for (LastNotesState lastNotesState : noteValueToLastNoteState.values()) {
			if (addNoteOffMessages(tick, lastNotesState, noteOffMessages)) {
				Note note = lastNotesState.note;
				keysToRemove.add(note.getKey());
			}
		}

		for (Integer keyToRemove: keysToRemove) {
			noteValueToLastNoteState.remove(keyToRemove);
		}
		return noteOffMessages;
	}

	private boolean addNoteOffMessages(long tick, LastNotesState lastNotesState, List<ShortMessage> noteOffMessages) throws InvalidMidiDataException {
		Note note = lastNotesState.note;
		if (isNoteOffEvent(note, lastNotesState.sentOnTick, tick)) {
			noteOffMessages.add(note.asNoteOffMessage(channel));
			return true;
		}
		return false;
	}

	private List<ShortMessage> getNoteOnMessages(long tick) throws InvalidMidiDataException {
		List<ShortMessage> noteOnMessages = new ArrayList<ShortMessage>();
		Notes notes = getNotes(tick);
		if (notes == null) {
			return noteOnMessages;
		}

		for (Note note : notes.getNotes()) {
			if ( ! isAlreadyPlaying(note)) {
				noteOnMessages.add(note.asNoteOnMessage(channel));
				noteValueToLastNoteState.put(note.getKey(), new LastNotesState(note, tick));
			}
		}

		if ( ! noteOnMessages.isEmpty()) {
			notifyListeners(tick, notes);
		}
		
		return noteOnMessages;
	}

	private void notifyListeners(long tick, Notes notes) {
		if (trackListener != null) {
			NotificationThread t = new NotificationThread(tick, notes, trackListener);
			t.start();
		}
	}

	public void toggleMute() {
		this.mute = !mute;
	}

	private class NotificationThread extends Thread {
		private long tick;
		private Notes notes;
		private TrackListener trackListener;
		public NotificationThread(long tick, Notes notes, TrackListener trackListener) {
			this.tick = tick;
			this.notes = notes;
			this.trackListener = trackListener;
		}

		@Override
		public void run() {
			trackListener.notesTriggered(tick, notes);
		}
	}

	private boolean isAlreadyPlaying(Note note) {
		return noteValueToLastNoteState.containsKey(note.getKey());
	}

	private Notes getNotes(long tick) {
		return tickRhythm.getNotesForTick(tick, key);
	}

	private boolean isNoteOffEvent(Note note, long lastNoteSentTick, long tick) {
		return lastNoteSentTick + note.getDuration() == tick;
	}

	public TickRhythm getTickRhythm() {
		return tickRhythm;
	}

	public void setTickRhythm(TickRhythm tickRhythm) {
		this.tickRhythm = tickRhythm;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Key getKey() {
		return key;
	}

	public void setKey(Key key) {
		this.key = key;
	}

	public int getChannel() {
		return channel;
	}

	public void setChannel(int channel) {
		this.channel = channel;
	}

	public void setMute(boolean mute) {
		this.mute = mute;
	}

	public boolean isMute() {
		return mute;
	}

	@Override
	public void saveTo(Preset preset) {
		preset.save(copy());
	}

	private RealtimeTrack copy() {
		RealtimeTrack copy = new RealtimeTrack(getName(), getKey());
		copy.channel = channel;
		copy.name = name;
		copy.key = key;
		copy.tickRhythm = tickRhythm.copy();
		
		// don't worry about mute or other state for a "currently playing" track, just keep the "config" of this track.
		// Todo: separate into objects that represent to avoid confusion

		return copy;
	}

	private static class LastNotesState {
		Note note;
		long sentOnTick;

		public LastNotesState(Note note, long sentOnTick) {
			this.note = note;
			this.sentOnTick = sentOnTick;
		}
	}
}
