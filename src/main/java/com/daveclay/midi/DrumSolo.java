package com.daveclay.midi;

import com.daveclay.midi.random.RandomSequenceRhythmStrategy;
import com.daveclay.midi.sequence.*;
import com.daveclay.midi.random.PhraseBuildingStrategy;
import com.daveclay.midi.random.RandomInstrument;
import com.daveclay.midi.random.RandomNoteStrategy;
import com.daveclay.midi.random.RandomSongBuilder;
import com.daveclay.midi.random.RandyTheRandom;

import javax.sound.midi.InvalidMidiDataException;
import javax.sound.midi.MidiUnavailableException;
import javax.sound.midi.Track;
import java.io.File;
import java.io.IOException;

import static com.daveclay.midi.NoteConstants.*;


/**
 */
public class DrumSolo {

	public static void main(String[] args) throws InvalidMidiDataException, IOException, MidiUnavailableException {
		File outputFile = new File(args[0]);
		MidiSequencer midiSequencer = new MidiSequencer(outputFile);
		midiSequencer.setReceiver(MidiSequencer.determineDesiredReceiver(args));

		RandomSongBuilder randomSongBuilder = new RandomSongBuilder();
		DrumSolo drumSolo = new DrumSolo(midiSequencer, randomSongBuilder);
		drumSolo.buildSong();

		int temp = RandyTheRandom.randomIntBetweenExclusive(100, 140);
		midiSequencer.play(temp);
		midiSequencer.writeFile();
	}

	private final MidiSequencer midiSequencer;
	private final RandomSongBuilder songBuilder;
	private Track track;

	public DrumSolo(MidiSequencer midiSequencer, RandomSongBuilder songBuilder) {
		this.midiSequencer = midiSequencer;
		this.track = midiSequencer.getTrack();
		this.songBuilder = songBuilder;
	}

	private void buildSong() throws MidiUnavailableException, InvalidMidiDataException {
		buildKickSequence();
		buildSnareSequence();
		buildHihatSequence();
		buildCrash();
		buildTimbales();
		buildBassSequence();

		songBuilder.buildSong();
		songBuilder.addMidiEvents(track);
	}

	public void buildBassSequence() throws InvalidMidiDataException, MidiUnavailableException {
		InstrumentTrack bassTrack = midiSequencer.createInstrumentTrack(0, GM_SLAP_BASS_1);
		RandomInstrument bassRandomInstrument = new RandomInstrument(bassTrack);
		Key bassKey = new Key(NoteValue.D2, Scale.MINOR_PENTATONIC_SCALE);
		bassRandomInstrument.setKey(bassKey);

		RandomNoteStrategy bassNoteStrategy = new RandomNoteStrategy();
		bassNoteStrategy.setMaxDuration(4);
		bassNoteStrategy.setMinDuration(1);
		bassNoteStrategy.setMinVelocity(60);
		bassNoteStrategy.setMaxVelocity(80);
		bassNoteStrategy.setMaxNumberOfNotes(1);
		bassNoteStrategy.setMinNumberOfNotes(1);

		InKeyNoteValueRange valueRange = new InKeyNoteValueRange();
		valueRange.setLowestRootNoteInKeyAsInt(0);
		valueRange.setHighestRootNoteInKeyAsInt(0);
		bassNoteStrategy.setNoteValueRange(valueRange);

		RootNoteValueRange rootValueRange = new RootNoteValueRange();
		RandomNoteStrategy bassRootNoteStrategy = new RandomNoteStrategy();
		bassRootNoteStrategy.setMaxDuration(4);
		bassRootNoteStrategy.setMinDuration(1);
		bassRootNoteStrategy.setMinVelocity(60);
		bassRootNoteStrategy.setMaxVelocity(80);
		bassRootNoteStrategy.setMaxNumberOfNotes(1);
		bassRootNoteStrategy.setMinNumberOfNotes(1);
		bassRootNoteStrategy.setNoteValueRange(rootValueRange);

		bassRandomInstrument.setNoteEventStrategy(bassNoteStrategy);

		RandomSequenceRhythmStrategy bassRhythmStrategy = new RandomSequenceRhythmStrategy();
		bassRhythmStrategy.setLikelyhoodOfEvent(.2);
		bassRhythmStrategy.setLikelyhoodOfNoteAtPosition(0, .8, bassRootNoteStrategy);
		bassRhythmStrategy.setLikelyhoodOfNoteAtPosition(8, .8);
		bassRandomInstrument.setRhythmStrategy(bassRhythmStrategy);

		PhraseBuildingStrategy phraseBuildingStrategy = new PhraseBuildingStrategy();
		phraseBuildingStrategy.setNumberOfMeasuresInPhrase(1);
		bassRandomInstrument.setPhraseBuildingStrategy(phraseBuildingStrategy);

		songBuilder.addInstrument(bassRandomInstrument);
	}

	public void buildKickSequence() throws InvalidMidiDataException, MidiUnavailableException {
		RandomInstrument kick = buildDrumRandomInstrument(NoteValue.GM_BASS_DRUM_1);

		RandomSequenceRhythmStrategy kickRhythmStrategy = new RandomSequenceRhythmStrategy();
		kickRhythmStrategy.setLikelyhoodOfEvent(.2);
		kickRhythmStrategy.setLikelyhoodOfNoteAtPosition(0, .9);
		kickRhythmStrategy.setLikelyhoodOfNoteAtPosition(2, .4);
		kickRhythmStrategy.setLikelyhoodOfNoteAtPosition(4, .7);
		kickRhythmStrategy.setLikelyhoodOfNoteAtPosition(6, .4);
		kickRhythmStrategy.setLikelyhoodOfNoteAtPosition(8, .9);
		kickRhythmStrategy.setLikelyhoodOfNoteAtPosition(12, .7);

		kick.setRhythmStrategy(kickRhythmStrategy);

		PhraseBuildingStrategy phraseBuildingStrategy = new PhraseBuildingStrategy();
		phraseBuildingStrategy.setNumberOfMeasuresInPhrase(2);
		kick.setPhraseBuildingStrategy(phraseBuildingStrategy);

		songBuilder.addInstrument(kick);
	}

	public void buildSnareSequence() {
		RandomInstrument snare = buildDrumRandomInstrument(NoteValue.GM_ACOUSTIC_SNARE);

		RandomSequenceRhythmStrategy snareRhythmStrategy = new RandomSequenceRhythmStrategy();
		snareRhythmStrategy.setLikelyhoodOfEvent(.05);
		snareRhythmStrategy.setLikelyhoodOfNoteAtPosition(2, .2);
		snareRhythmStrategy.setLikelyhoodOfNoteAtPosition(4, .9);
		snareRhythmStrategy.setLikelyhoodOfNoteAtPosition(8, .2);
		snareRhythmStrategy.setLikelyhoodOfNoteAtPosition(12, .9);

		snare.setRhythmStrategy(snareRhythmStrategy);

		PhraseBuildingStrategy phraseBuildingStrategy = new PhraseBuildingStrategy();
		phraseBuildingStrategy.setNumberOfMeasuresInPhrase(4);
		snare.setPhraseBuildingStrategy(phraseBuildingStrategy);

		songBuilder.addInstrument(snare);
	}

	public void buildHihatSequence() {
		RandomInstrument hihat = buildDrumRandomInstrument(NoteValue.GM_CLOSED_HI_HAT);

		RandomSequenceRhythmStrategy hihatRhythmStrategy = new RandomSequenceRhythmStrategy();
		hihatRhythmStrategy.setLikelyhoodOfEvent(.8);

		hihat.setRhythmStrategy(hihatRhythmStrategy);
		songBuilder.addInstrument(hihat);
	}

	public void buildCrash() {
		RandomInstrument crash = buildDrumRandomInstrument(NoteValue.GM_CRASH_CYMBAL_1, NoteValue.GM_CRASH_CYMBAL_2);
		RandomSequenceRhythmStrategy crashRhythmStrategy = new RandomSequenceRhythmStrategy();
		crashRhythmStrategy.setLikelyhoodOfEvent(.0);
		crashRhythmStrategy.setLikelyhoodOfNoteAtPosition(0, .5);
		crashRhythmStrategy.setLikelyhoodOfNoteAtPosition(14, .5);

		crash.setRhythmStrategy(crashRhythmStrategy);
		songBuilder.addInstrument(crash);
	}

	public void buildTimbales() {
		RandomInstrument timbale = buildDrumRandomInstrument(NoteValue.GM_HIGH_TIMBALE, NoteValue.GM_LOW_TIMBALE);
		RandomSequenceRhythmStrategy crashRhythmStrategy = new RandomSequenceRhythmStrategy();
		crashRhythmStrategy.setLikelyhoodOfEvent(.0);
		crashRhythmStrategy.setLikelyhoodOfNoteAtPosition(10, .5);
		crashRhythmStrategy.setLikelyhoodOfNoteAtPosition(12, .5);
		crashRhythmStrategy.setLikelyhoodOfNoteAtPosition(14, .5);

		timbale.setRhythmStrategy(crashRhythmStrategy);
		songBuilder.addInstrument(timbale);
	}

	public RandomInstrument buildDrumRandomInstrument(NoteValue... notes) {
		InstrumentTrack hihatTrack = midiSequencer.createInstrumentTrack(9, 0);
		RandomInstrument hihat = new RandomInstrument(hihatTrack);
		hihat.setDrum(true);

		RandomNoteStrategy hihatNoteStrategy = new RandomNoteStrategy();
		hihatNoteStrategy.setMinDuration(1);
		hihatNoteStrategy.setMaxDuration(1);
		hihatNoteStrategy.setMinVelocity(85);
		hihatNoteStrategy.setMaxVelocity(100);
		hihatNoteStrategy.setMaxNumberOfNotes(1);
		hihatNoteStrategy.setMinNumberOfNotes(1);

		CustomNoteValueRange valueRange = new CustomNoteValueRange();
		int[] noteInts = new int[notes.length];
		for (int i = 0; i < notes.length; i++) {
			noteInts[i] = notes[i].getIntValue();
		}
		valueRange.setAvailableNotesAsInt(noteInts);
		hihatNoteStrategy.setNoteValueRange(valueRange);

		hihat.setNoteEventStrategy(hihatNoteStrategy);
		NoteValue root = notes[0];
		hihat.setKey(new Key(root, Scale.UNISON_SCALE));
		return hihat;
	}

}

