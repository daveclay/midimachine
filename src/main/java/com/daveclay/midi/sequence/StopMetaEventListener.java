package com.daveclay.midi.sequence;

import javax.sound.midi.MetaEventListener;
import javax.sound.midi.MetaMessage;
import javax.sound.midi.Sequencer;

/**
 */
public class StopMetaEventListener implements MetaEventListener {

	public static final int STOP_META_TYPE_VALUE = 47;

	private final Sequencer sequencer;

	public StopMetaEventListener(Sequencer sequencer) {
		this.sequencer = sequencer;
	}

	public void meta(MetaMessage meta) {
		if (isEndOfTrack(meta)) {
			stopSequencer();
		}
	}

	private boolean isEndOfTrack(MetaMessage meta) {
		return meta.getType() == STOP_META_TYPE_VALUE;
	}

	private void stopSequencer() {
		sequencer.close();
	}
}
