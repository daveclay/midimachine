package com.daveclay.midi.random;

import com.daveclay.midi.sequence.*;
import com.daveclay.midi.Key;

/**
*/
public interface NoteEventStrategy {

	SequenceEvent createEvent(Key key);
}
