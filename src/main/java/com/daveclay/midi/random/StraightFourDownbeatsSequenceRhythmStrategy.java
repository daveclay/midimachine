package com.daveclay.midi.random;

import com.daveclay.midi.sequence.*;

public class StraightFourDownbeatsSequenceRhythmStrategy extends AbstractSequenceRhythmStrategy implements SequenceRhythmStrategy {

	protected boolean shouldMakeNote(int position) {
		return position % 4 == 0;
	}

}
