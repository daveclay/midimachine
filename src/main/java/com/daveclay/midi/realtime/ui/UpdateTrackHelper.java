package com.daveclay.midi.realtime.ui;

import com.daveclay.midi.Key;
import com.daveclay.midi.realtime.RealtimeTrack;
import com.daveclay.midi.realtime.TickRhythm;

public class UpdateTrackHelper {

	public static void updateTrack(RealtimeTrack track, TrackConfig trackConfig) {
		String name = trackConfig.getName();
		Key key = trackConfig.getKey();
		int channel = trackConfig.getChannel();

		track.setChannel(channel);
		track.setName(name);
		track.setKey(key);

		TickRhythm tickRhythm = track.getTickRhythm();
		if (tickRhythm == null) {
			tickRhythm = new TickRhythm();
		}

		int numberOfTicksInPhrase = trackConfig.getNumberOfTicksInPhrase();
		tickRhythm.setNumberOfTicksInPhrase(numberOfTicksInPhrase);
		track.setTickRhythm(tickRhythm);
	}
}
