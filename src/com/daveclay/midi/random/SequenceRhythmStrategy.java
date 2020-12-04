package com.daveclay.midi.random;

import com.daveclay.midi.sequence.*;

public interface SequenceRhythmStrategy {
	void makeNotes(Measure measure, RandomInstrument randomInstrument);
}
