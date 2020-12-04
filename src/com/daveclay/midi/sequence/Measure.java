package com.daveclay.midi.sequence;

import javax.sound.midi.InvalidMidiDataException;
import javax.sound.midi.Track;

/**
*/
public class Measure {
	private SequenceEvent[] sequenceEvents = new SequenceEvent[16];
	private int PPQ;
	private float shuffleFactor = 0.0f;
	
	public Measure() {
		this(4,0.0f);
	}
	
	public Measure(int PPQ) {
		this(4,0.0f);
	}
	
	public Measure(int PPQ, float shuffleFactor) {
		this.PPQ = PPQ;
		this.shuffleFactor = shuffleFactor;
	}

	public int getNumberOfPositions() {
		return sequenceEvents.length;
	}

	public void setEvent(int position, SequenceEvent sequenceEvent) {
		if (position >= 0 && position < sequenceEvents.length) {
			sequenceEvents[position] = sequenceEvent;
		} else {
			throw new RuntimeException("Position must be between 0 and " + sequenceEvents.length);
		}
	}

	public void addMIDIEvents(Track track, int channel, int tick) throws InvalidMidiDataException {
		for (int i = 0; i < sequenceEvents.length; i++) {
			if (sequenceEvents[i] != null) {
				int offset = (i % 2 == 0) ? 0 : (int)(PPQ/4 * shuffleFactor);
				int nextTick = tick + (i * PPQ/4);
				nextTick += offset;
				sequenceEvents[i].addMIDIEvents(track, channel, nextTick);
			}
		}
	}
	
	public String toString() {
		StringBuffer output = new StringBuffer();
		int durationRemain = 0;
		for (int i = 0; i < sequenceEvents.length; i++) {
			String noteString;
			if (sequenceEvents[i] == null) {
				if (durationRemain > 0) {
					noteString = "....";
					durationRemain--;
				} else {
					noteString = "";
				}
			} else {
				noteString = sequenceEvents[i].toString();
				//TODO: Can we do durationRemain with SequenceEvent?
				//durationRemain = sequenceEvents[i].getDuration();
			}
			output.append(String.format("| %5s ", noteString));
		}
		output.append("|");
		return output.toString();
	}
}
