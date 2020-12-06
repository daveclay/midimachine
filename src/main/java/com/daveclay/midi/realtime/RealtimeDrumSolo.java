package com.daveclay.midi.realtime;

import com.daveclay.midi.InKeyNoteValueRange;
import com.daveclay.midi.Key;
import com.daveclay.midi.NoteValue;
import com.daveclay.midi.Scale;
import com.daveclay.midi.random.RandomNoteStrategy;

import javax.sound.midi.InvalidMidiDataException;
import javax.sound.midi.MidiUnavailableException;

/**
 */
public class RealtimeDrumSolo extends RealtimeCommandLineRunner {

	private TickRhythm hiHatTickRhythm;
	private TickRhythm snareTickRhythm;
	private TickRhythm kickTickRhythm;

	public static void main(String[] args) throws InvalidMidiDataException, MidiUnavailableException {
		RealtimeDrumSolo drumSolo = new RealtimeDrumSolo();
		Thread t = new Thread(drumSolo);
		t.start();
	}

	@Override
	protected void addInstruments(RealtimeSequencer sequencer) {
		sequencer.addTrack(buildKick());
		sequencer.addTrack(buildSnare());
		sequencer.addTrack(buildHiHat());
	}

	private RealtimeTrack buildHiHat() {
		//RealtimeTrack realtimeTrack = new RealtimeTrack(new Key(71, UNISON_SCALE));
		RealtimeTrack realtimeTrack = new RealtimeTrack("hihat", new Key(NoteValue.GM_CLOSED_HI_HAT, Scale.UNISON_SCALE));

		InKeyNoteValueRange valueRange = new InKeyNoteValueRange();
		valueRange.setLowestRootNoteInKeyAsInt(0);
		valueRange.setHighestRootNoteInKeyAsInt(0);

		RandomNoteStrategy loudNoteStrategy = new RandomNoteStrategy();
		loudNoteStrategy.setMaxDuration(16);
		loudNoteStrategy.setMinDuration(16);
		loudNoteStrategy.setMinVelocity(127);
		loudNoteStrategy.setMaxVelocity(127);
		loudNoteStrategy.setMaxNumberOfNotes(1);
		loudNoteStrategy.setMinNumberOfNotes(1);
		loudNoteStrategy.setNoteValueRange(valueRange);

		RandomNoteStrategy softNoteStrategy = new RandomNoteStrategy();
		softNoteStrategy.setMaxDuration(16);
		softNoteStrategy.setMinDuration(16);
		softNoteStrategy.setMinVelocity(40);
		softNoteStrategy.setMaxVelocity(80);
		softNoteStrategy.setMaxNumberOfNotes(1);
		softNoteStrategy.setMinNumberOfNotes(1);
		softNoteStrategy.setNoteValueRange(valueRange);

		hiHatTickRhythm = new TickRhythm();
		hiHatTickRhythm.setNumberOfTicksInPhrase(512);
		hiHatTickRhythm.setBiasForEveryNthTick(.4, 64, 0, softNoteStrategy);
		hiHatTickRhythm.setBiasForEveryNthTick(.9, 256, 128, loudNoteStrategy);

		realtimeTrack.setTickRhythm(hiHatTickRhythm);
		return realtimeTrack;
	}

	private RealtimeTrack buildSnare() {
		//RealtimeTrack realtimeTrack = new RealtimeTrack(new Key(38, UNISON_SCALE));
		RealtimeTrack realtimeTrack = new RealtimeTrack("snare", new Key(NoteValue.GM_ACOUSTIC_SNARE, Scale.UNISON_SCALE));

		InKeyNoteValueRange valueRange = new InKeyNoteValueRange();
		valueRange.setLowestRootNoteInKeyAsInt(0);
		valueRange.setHighestRootNoteInKeyAsInt(0);

		RandomNoteStrategy loudNoteStrategy = new RandomNoteStrategy();
		loudNoteStrategy.setMaxDuration(16);
		loudNoteStrategy.setMinDuration(16);
		loudNoteStrategy.setMinVelocity(127);
		loudNoteStrategy.setMaxVelocity(127);
		loudNoteStrategy.setMaxNumberOfNotes(1);
		loudNoteStrategy.setMinNumberOfNotes(1);
		loudNoteStrategy.setNoteValueRange(valueRange);

		RandomNoteStrategy softNoteStrategy = new RandomNoteStrategy();
		softNoteStrategy.setMaxDuration(16);
		softNoteStrategy.setMinDuration(16);
		softNoteStrategy.setMinVelocity(40);
		softNoteStrategy.setMaxVelocity(80);
		softNoteStrategy.setMaxNumberOfNotes(1);
		softNoteStrategy.setMinNumberOfNotes(1);
		softNoteStrategy.setNoteValueRange(valueRange);

		snareTickRhythm = new TickRhythm();
		snareTickRhythm.setNumberOfTicksInPhrase(2048);
		snareTickRhythm.setBiasForEveryNthTick(.4, 64, 0, softNoteStrategy);
		snareTickRhythm.setBiasForEveryNthTick(.9, 256, 128, loudNoteStrategy);

		realtimeTrack.setTickRhythm(snareTickRhythm);
		return realtimeTrack;
	}

	private RealtimeTrack buildKick() {
		//RealtimeTrack realtimeTrack = new RealtimeTrack(new Key(36, UNISON_SCALE));
		RealtimeTrack realtimeTrack = new RealtimeTrack("kick", new Key(NoteValue.GM_BASS_DRUM_1, Scale.UNISON_SCALE));

		RandomNoteStrategy noteStrategy = new RandomNoteStrategy();
		noteStrategy.setMaxDuration(16);
		noteStrategy.setMinDuration(16);
		noteStrategy.setMinVelocity(100);
		noteStrategy.setMaxVelocity(127);
		noteStrategy.setMaxNumberOfNotes(1);
		noteStrategy.setMinNumberOfNotes(1);

		InKeyNoteValueRange valueRange = new InKeyNoteValueRange();
		valueRange.setLowestRootNoteInKeyAsInt(0);
		valueRange.setHighestRootNoteInKeyAsInt(0);
		noteStrategy.setNoteValueRange(valueRange);


		kickTickRhythm = new TickRhythm();
		kickTickRhythm.setNumberOfTicksInPhrase(1024);
		kickTickRhythm.setBiasForEveryNthTick(.4, 64, 0, noteStrategy);
		kickTickRhythm.setBiasForEveryNthTick(.9, 256, 0, noteStrategy);

		realtimeTrack.setTickRhythm(kickTickRhythm);
		return realtimeTrack;
	}

}
