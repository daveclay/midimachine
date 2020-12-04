package com.daveclay.midi.sequence;

import javax.sound.midi.InvalidMidiDataException;
import javax.sound.midi.Track;

public interface SequenceEvent {
	void addMIDIEvents(Track track, int channel, int tick) throws InvalidMidiDataException;
}
