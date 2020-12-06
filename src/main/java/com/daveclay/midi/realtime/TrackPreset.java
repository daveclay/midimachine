package com.daveclay.midi.realtime;

public class TrackPreset implements Preset {

	private String name;
	private RealtimeTrack track;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public RealtimeTrack getTrack() {
		return track;
	}

	public void setTrack(RealtimeTrack track) {
		this.track = track;
	}

	@Override
	public void save(RealtimeTrack track) {
		setTrack(track);
	}
}
