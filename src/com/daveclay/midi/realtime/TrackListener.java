package com.daveclay.midi.realtime;

/**
*/
public interface TrackListener {
	void notesTriggered(long tick, Notes notes);
}
