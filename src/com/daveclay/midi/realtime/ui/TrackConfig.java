package com.daveclay.midi.realtime.ui;

import com.daveclay.midi.Key;

public class TrackConfig {

	private String name;
	private int channel;
	private Key key;
	private int numberOfPositions;
	private int numberOfTicksPerPosition;

	public TrackConfig(String name, Key key, int channel, int numberOfPositions, int numberOfTicksPerPosition) {
		this.name = name;
		this.key = key;
		this.channel = channel;
		this.numberOfPositions = numberOfPositions;
		this.numberOfTicksPerPosition = numberOfTicksPerPosition;
	}

	public String getName() {
		return name;
	}

	public int getChannel() {
		return channel;
	}

	public Key getKey() {
		return key;
	}

	public int getNumberOfPositions() {
		return numberOfPositions;
	}

	public int getNumberOfTicksPerPosition() {
		return numberOfTicksPerPosition;
	}

	public int getNumberOfTicksInPhrase() {
		int numberOfTicksInPhrase = getNumberOfPositions() * getNumberOfTicksPerPosition();
		return numberOfTicksInPhrase;
	}
}
