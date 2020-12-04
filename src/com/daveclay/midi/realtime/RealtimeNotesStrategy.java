package com.daveclay.midi.realtime;

import com.daveclay.midi.Key;

public interface RealtimeNotesStrategy {

	Notes getNotes(Key key);
	RealtimeNotesStrategy copy();
}
