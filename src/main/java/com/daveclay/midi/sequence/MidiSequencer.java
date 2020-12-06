package com.daveclay.midi.sequence;

import com.daveclay.midi.Note;
import com.daveclay.midi.Receivers;

import javax.sound.midi.InvalidMidiDataException;
import javax.sound.midi.MidiEvent;
import javax.sound.midi.MidiSystem;
import javax.sound.midi.MidiUnavailableException;
import javax.sound.midi.Receiver;
import javax.sound.midi.Sequence;
import javax.sound.midi.Sequencer;
import javax.sound.midi.ShortMessage;
import javax.sound.midi.Track;

import java.io.File;
import java.io.IOException;


/**
 */
public class MidiSequencer {

	public static void main(String[] args) throws InvalidMidiDataException, IOException, MidiUnavailableException {
		File outputFile = new File(args[0]);
		MidiSequencer midiSequencer = new MidiSequencer(outputFile);
		midiSequencer.play();
		midiSequencer.writeFile();
	}

	private File outputFile;
	private Sequence sequence;
	private Track track;
	private int PPQ;
	private float shuffleFactor = 0.0f;
	private Receiver receiver;

	public MidiSequencer(File outputFile, int PPQ) throws InvalidMidiDataException {
		this.outputFile = outputFile;
		this.PPQ = PPQ;
		sequence = new Sequence(Sequence.PPQ, PPQ);
		track = sequence.createTrack();		
	}
	
	public MidiSequencer(File outputFile) throws InvalidMidiDataException {
		this(outputFile,4);
	}

	public int getPPQ() {
		return PPQ;
	}
	
	public float getShuffleFactor() {
		return shuffleFactor;
	}
	
	public void setShuffleFactor(float shuffleFactor) {
		this.shuffleFactor = shuffleFactor;
	}
	
	public Receiver getReceiver() {
		return receiver;
	}

	public void setReceiver(Receiver receiver) {
		this.receiver = receiver;
	}

	public void writeFile() throws IOException {
		MidiSystem.write(sequence, 0, outputFile);
	}

	public Track getTrack() {
		return track;
	}

	public void play() throws MidiUnavailableException, InvalidMidiDataException {
		play(0);
	}
	
	public void play(float bpm) throws MidiUnavailableException, InvalidMidiDataException {
		Sequencer sequencer;
		if (receiver != null) {
			sequencer = MidiSystem.getSequencer(false);
			sequencer.getTransmitter().setReceiver(receiver);
		} else {
			sequencer = MidiSystem.getSequencer(true);
		}
		if (bpm != 0) {
			sequencer.setTempoInBPM(bpm);
		}
		sequencer.addMetaEventListener(new StopMetaEventListener(sequencer));
		sequencer.open();
		sequencer.setSequence(sequence);
		sequencer.start();
	}

	public void addNote(Note note, int channel, int tick) throws InvalidMidiDataException {
		note.addMIDIEvents(track, channel, tick);
	}

	public void addNoteEvent(int channel, int key, int velocity, int start, int duration) throws InvalidMidiDataException {
		track.add(createNoteOnEvent(channel, key, velocity, start));
		track.add(createNoteOffEvent(channel, key, velocity, (start + duration)));
	}

	public void addProgramChangeEvent(int channel, int program, int start) throws InvalidMidiDataException {
		track.add(createProgramChangeEvent(channel, program, start));
	}

	public static MidiEvent createProgramChangeEvent(int channel, int program, int start) throws InvalidMidiDataException {
		ShortMessage programChange = new ShortMessage();
		programChange.setMessage(ShortMessage.PROGRAM_CHANGE,channel,program,0);
		return new MidiEvent(programChange,start);	
	}
	
	public static MidiEvent createNoteOffEvent(int channel, int key, int velocity, int tick) throws InvalidMidiDataException {
		return createNoteEvent(ShortMessage.NOTE_OFF, channel, key, velocity, tick);
	}

	public static MidiEvent createNoteOnEvent(int channel, int key, int velocity, int tick) throws InvalidMidiDataException {
		return createNoteEvent(ShortMessage.NOTE_ON, channel, key, velocity, tick);
	}

	public static MidiEvent createNoteEvent(int command, int channel, int key, int velocity, int tick)
			throws InvalidMidiDataException {
		ShortMessage msg = new ShortMessage();
		msg.setMessage(command, channel, key, velocity);
		MidiEvent event = new MidiEvent(msg, tick);
		return event;
	}

	public InstrumentTrack createInstrumentTrack(int channel, int program) {
		return createInstrumentTrack(channel,program,this.shuffleFactor);
	}
	public InstrumentTrack createInstrumentTrack(int channel, int program,float shuffleFactor) {
		InstrumentTrack result = new InstrumentTrack(channel,program,getPPQ(),shuffleFactor);
		return result;
	}

	public InstrumentTrack createDrumTrack() {
		return createInstrumentTrack(9,0);
	}
	
	/* Utility for use by main methods of other classes until we consolidate them */
	public static Receiver determineDesiredReceiver(String[] args) throws MidiUnavailableException {
		return Receivers.determineDesiredReceiver(args);
	}
}

