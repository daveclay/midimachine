package com.daveclay.midi.sequence;

import javax.sound.midi.InvalidMidiDataException;
import javax.sound.midi.Track;
import java.util.ArrayList;
import java.util.List;

/**
*/
public class InstrumentTrack {

	private int channel;
	private int program;
	private List<Measure> measures = new ArrayList<Measure>();
	private int PPQ;
	private float shuffleFactor = 0.0f;

	public InstrumentTrack(int channel, int program, int PPQ) {
		this(channel,program,PPQ,0.0f);
	}
	
	public InstrumentTrack(int channel, int program, int PPQ, float shuffleFactor) {
		this.PPQ = PPQ;
		this.channel = channel;
		this.program = program;
		this.shuffleFactor = shuffleFactor;
	}

	public void addPhrase(Phrase phrase) {
		measures.addAll(phrase.getMeasures());
	}

	public void loopPhrase(Phrase phrase, int count) {
		for (int i = 0; i < count; i++) {
			addPhrase(phrase);
		}
	}

	public void addMeasure(Measure measure) {
		measures.add(measure);
	}

	public void addMIDIEvents(Track track) throws InvalidMidiDataException {
		int startTick = 0;
		track.add(MidiSequencer.createProgramChangeEvent(channel, program, startTick));
		for (Measure measure : measures) {
			measure.addMIDIEvents(track, channel, startTick);
			startTick += 16 * (PPQ/4);
		}
	}
	
	public Measure createMeasure() {
		return new Measure(PPQ,this.shuffleFactor);
	}
	
	public Measure createMeasure(float shuffleFactor) {
		return new Measure(PPQ,shuffleFactor);
	}

	public Phrase createPhrase() {
		return new Phrase();
	}
}
