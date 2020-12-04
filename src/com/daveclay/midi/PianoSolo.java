package com.daveclay.midi;

import com.daveclay.midi.sequence.*;
import com.daveclay.midi.random.PhraseBuildingStrategy;
import com.daveclay.midi.random.RandomInstrument;
import com.daveclay.midi.random.RandomSongBuilder;
import com.daveclay.midi.random.RandomNoteStrategy;
import com.daveclay.midi.random.RandomSequenceRhythmStrategy;
import com.daveclay.midi.random.RandyTheRandom;

import javax.sound.midi.InvalidMidiDataException;
import javax.sound.midi.MidiUnavailableException;
import javax.sound.midi.Track;

import java.io.File;
import java.io.IOException;

/**
*/
public class PianoSolo {


	public static void main(String[] args) throws InvalidMidiDataException, IOException, MidiUnavailableException {
		File outputFile = new File(args[0]);

		MidiSequencer midiSequencer = new MidiSequencer(outputFile);
		midiSequencer.setReceiver(MidiSequencer.determineDesiredReceiver(args));

		PianoSolo pianoSolo = new PianoSolo(midiSequencer);
		pianoSolo.buildSequence();
		pianoSolo.play();
	}

	private final MidiSequencer midiSequencer;
	private final RandomSongBuilder songBuilder;

	public PianoSolo(MidiSequencer midiSequencer) {
		this.midiSequencer = midiSequencer;
		this.songBuilder = new RandomSongBuilder();
	}

	private NoteValue getRandomNote(NoteValue lowNote, NoteValue highNote) {
		int value = RandyTheRandom.randomIntBetweenInclusive(lowNote.getIntValue(), highNote.getIntValue());
		return NoteValue.forInt(value);
	}

	public void play() throws InvalidMidiDataException, IOException, MidiUnavailableException {
		Track track = midiSequencer.getTrack();
		songBuilder.addMidiEvents(track);
		midiSequencer.play(80);
		midiSequencer.writeFile();
	}

	public void buildSequence() throws MidiUnavailableException, InvalidMidiDataException, IOException {
		NoteValue rootNote = getRandomNote(NoteValue.C3, NoteValue.G3);

		createBass(rootNote);
		createMelody(rootNote);
		buildKickSequence();
		buildSnareSequence();
		buildHihatSequence();

		songBuilder.buildSong();
		songBuilder.modulateKey();
	}

	public void createMelody(NoteValue note) {
		InstrumentTrack melodyTrack = midiSequencer.createInstrumentTrack(3, 0);
		RandomInstrument melody = new RandomInstrument(melodyTrack);

		RandomNoteStrategy melodyNoteStrategy = new RandomNoteStrategy();
		melodyNoteStrategy.setMaxDuration(20);
		melodyNoteStrategy.setMinVelocity(10);
		melodyNoteStrategy.setMaxVelocity(60);
		melodyNoteStrategy.setMaxNumberOfNotes(4);
		melodyNoteStrategy.setMinNumberOfNotes(1);

		InKeyNoteValueRange valueRange = new InKeyNoteValueRange();
		valueRange.setLowestRootNoteInKeyAsInt(2);
		valueRange.setHighestRootNoteInKeyAsInt(3);
		melodyNoteStrategy.setNoteValueRange(valueRange);

		melody.setNoteEventStrategy(melodyNoteStrategy);
		Key key = new Key(note, Scale.MINOR_SCALE);
		melody.setKey(key);

		RandomSequenceRhythmStrategy melodyRhythmStrategy = new RandomSequenceRhythmStrategy();
		melodyRhythmStrategy.setLikelyhoodOfEvent(.7);
		melodyRhythmStrategy.setLikelyhoodOfNoteAtPosition(1, .1);
		melodyRhythmStrategy.setLikelyhoodOfNoteAtPosition(3, .3);
		melodyRhythmStrategy.setLikelyhoodOfNoteAtPosition(5, .2);
		melodyRhythmStrategy.setLikelyhoodOfNoteAtPosition(7, .1);
		melodyRhythmStrategy.setLikelyhoodOfNoteAtPosition(9, .3);
		melodyRhythmStrategy.setLikelyhoodOfNoteAtPosition(11, .1);
		melody.setRhythmStrategy(melodyRhythmStrategy);

		PhraseBuildingStrategy phraseBuildingStrategy = new PhraseBuildingStrategy();
		phraseBuildingStrategy.setNumberOfMeasuresInPhrase(-1);
		melody.setPhraseBuildingStrategy(phraseBuildingStrategy);

		songBuilder.addInstrument(melody);
	}

	public void createBass(NoteValue note) {
		InstrumentTrack bassTrack = midiSequencer.createInstrumentTrack(3, 0);
		RandomInstrument bass = new RandomInstrument(bassTrack);
		Key key = new Key(note, Scale.PERFECT_SCALE);
		bass.setKey(key);

		RandomNoteStrategy bassNoteStrategy = new RandomNoteStrategy();
		bassNoteStrategy.setMinDuration(8);
		bassNoteStrategy.setMaxDuration(16);
		bassNoteStrategy.setMinVelocity(30);
		bassNoteStrategy.setMaxVelocity(60);
		bassNoteStrategy.setMaxNumberOfNotes(1);

		InKeyNoteValueRange valueRange = new InKeyNoteValueRange();
		valueRange.setLowestRootNoteInKeyAsInt(0);
		valueRange.setHighestRootNoteInKeyAsInt(0);
		bassNoteStrategy.setNoteValueRange(valueRange);

		bass.setNoteEventStrategy(bassNoteStrategy);

		RandomSequenceRhythmStrategy bassRhythmStrategy = new RandomSequenceRhythmStrategy();
		bassRhythmStrategy.setLikelyhoodOfNoteAtPosition(0, .7);
		bassRhythmStrategy.setLikelyhoodOfNoteAtPosition(1, .1);
		bassRhythmStrategy.setLikelyhoodOfNoteAtPosition(2, .5);
		bassRhythmStrategy.setLikelyhoodOfNoteAtPosition(3, .1);
		bassRhythmStrategy.setLikelyhoodOfNoteAtPosition(4, .5);
		bassRhythmStrategy.setLikelyhoodOfNoteAtPosition(5, .1);
		bassRhythmStrategy.setLikelyhoodOfNoteAtPosition(6, .3);
		bassRhythmStrategy.setLikelyhoodOfNoteAtPosition(7, .1);
		bassRhythmStrategy.setLikelyhoodOfNoteAtPosition(8, .7);
		bassRhythmStrategy.setLikelyhoodOfNoteAtPosition(9, .1);
		bassRhythmStrategy.setLikelyhoodOfNoteAtPosition(10, .3);
		bassRhythmStrategy.setLikelyhoodOfNoteAtPosition(11, .1);
		bassRhythmStrategy.setLikelyhoodOfNoteAtPosition(12, .5);
		bassRhythmStrategy.setLikelyhoodOfNoteAtPosition(13, .1);
		bassRhythmStrategy.setLikelyhoodOfNoteAtPosition(14, .3);
		bassRhythmStrategy.setLikelyhoodOfNoteAtPosition(15, .1);
		bass.setRhythmStrategy(bassRhythmStrategy);

		PhraseBuildingStrategy phraseBuildingStrategy = new PhraseBuildingStrategy();
		phraseBuildingStrategy.setNumberOfMeasuresInPhrase(4);
		bass.setPhraseBuildingStrategy(phraseBuildingStrategy);

		songBuilder.addInstrument(bass);
	}

	public void buildKickSequence() throws InvalidMidiDataException, MidiUnavailableException {
		RandomInstrument kick = buildDrumRandomInstrument(NoteValue.GM_BASS_DRUM_1);

		RandomSequenceRhythmStrategy kickRhythmStrategy = new RandomSequenceRhythmStrategy();
		kickRhythmStrategy.setLikelyhoodOfEvent(.1);
		kickRhythmStrategy.setLikelyhoodOfNoteAtPosition(0, .9);
		kickRhythmStrategy.setLikelyhoodOfNoteAtPosition(2, .2);
		kickRhythmStrategy.setLikelyhoodOfNoteAtPosition(4, .7);
		kickRhythmStrategy.setLikelyhoodOfNoteAtPosition(6, .2);
		kickRhythmStrategy.setLikelyhoodOfNoteAtPosition(8, .9);
		kickRhythmStrategy.setLikelyhoodOfNoteAtPosition(12, .7);

		kick.setRhythmStrategy(kickRhythmStrategy);
		songBuilder.addInstrument(kick);
	}

	public void buildSnareSequence() {
		RandomInstrument snare = buildDrumRandomInstrument(NoteValue.GM_ACOUSTIC_SNARE);

		RandomNoteStrategy hardHittingSnareNoteStrategy = drumRandomNoteStrategy();
		hardHittingSnareNoteStrategy.setMinVelocity(80);
		hardHittingSnareNoteStrategy.setMaxVelocity(120);

		RandomNoteStrategy gentleSnareNoteStrategy = drumRandomNoteStrategy();
		gentleSnareNoteStrategy.setMinVelocity(40);
		gentleSnareNoteStrategy.setMaxVelocity(60);

		RandomSequenceRhythmStrategy snareRhythmStrategy = new RandomSequenceRhythmStrategy();
		snareRhythmStrategy.setLikelyhoodOfEvent(.0);
		snareRhythmStrategy.setLikelyhoodOfNoteAtPosition(2, .9, gentleSnareNoteStrategy);
		snareRhythmStrategy.setLikelyhoodOfNoteAtPosition(4, .9, hardHittingSnareNoteStrategy);
		snareRhythmStrategy.setLikelyhoodOfNoteAtPosition(8, .9, gentleSnareNoteStrategy);
		snareRhythmStrategy.setLikelyhoodOfNoteAtPosition(12, .9, hardHittingSnareNoteStrategy);

		snare.setRhythmStrategy(snareRhythmStrategy);

		PhraseBuildingStrategy phraseBuildingStrategy = new PhraseBuildingStrategy();
		phraseBuildingStrategy.setNumberOfMeasuresInPhrase(2);
		snare.setPhraseBuildingStrategy(phraseBuildingStrategy);

		songBuilder.addInstrument(snare);
	}

	public void buildHihatSequence() {
		RandomInstrument hihat = buildDrumRandomInstrument(NoteValue.GM_CLOSED_HI_HAT);

		RandomNoteStrategy openHihatNoteStrategy = drumRandomNoteStrategy(NoteValue.GM_OPEN_HI_HAT);
		openHihatNoteStrategy.setMinVelocity(80);
		openHihatNoteStrategy.setMaxVelocity(100);

		RandomNoteStrategy closedHihatNoteStrategy = drumRandomNoteStrategy(NoteValue.GM_CLOSED_HI_HAT);
		closedHihatNoteStrategy.setMinVelocity(60);
		closedHihatNoteStrategy.setMaxVelocity(80);

		RandomSequenceRhythmStrategy hihatRhythmStrategy = new RandomSequenceRhythmStrategy();

		hihatRhythmStrategy.setLikelyhoodOfNoteAtPosition(0, .8, closedHihatNoteStrategy);
		hihatRhythmStrategy.setLikelyhoodOfNoteAtPosition(1, .8, closedHihatNoteStrategy);
		hihatRhythmStrategy.setLikelyhoodOfNoteAtPosition(2, .8, closedHihatNoteStrategy);
		hihatRhythmStrategy.setLikelyhoodOfNoteAtPosition(3, .8, closedHihatNoteStrategy);
		hihatRhythmStrategy.setLikelyhoodOfNoteAtPosition(4, .8, closedHihatNoteStrategy);
		hihatRhythmStrategy.setLikelyhoodOfNoteAtPosition(5, .8, closedHihatNoteStrategy);
		hihatRhythmStrategy.setLikelyhoodOfNoteAtPosition(6, .8, closedHihatNoteStrategy);
		hihatRhythmStrategy.setLikelyhoodOfNoteAtPosition(7, .8, openHihatNoteStrategy);
		hihatRhythmStrategy.setLikelyhoodOfNoteAtPosition(8, .8, closedHihatNoteStrategy);
		hihatRhythmStrategy.setLikelyhoodOfNoteAtPosition(9, .8, closedHihatNoteStrategy);
		hihatRhythmStrategy.setLikelyhoodOfNoteAtPosition(10, .8, closedHihatNoteStrategy);
		hihatRhythmStrategy.setLikelyhoodOfNoteAtPosition(11, .8, closedHihatNoteStrategy);
		hihatRhythmStrategy.setLikelyhoodOfNoteAtPosition(12, .8, closedHihatNoteStrategy);
		hihatRhythmStrategy.setLikelyhoodOfNoteAtPosition(13, .8, closedHihatNoteStrategy);
		hihatRhythmStrategy.setLikelyhoodOfNoteAtPosition(14, .8, closedHihatNoteStrategy);
		hihatRhythmStrategy.setLikelyhoodOfNoteAtPosition(15, .8, openHihatNoteStrategy);

		hihat.setRhythmStrategy(hihatRhythmStrategy);
		songBuilder.addInstrument(hihat);
	}

	public RandomInstrument buildDrumRandomInstrument(NoteValue sound) {
		InstrumentTrack hihatTrack = midiSequencer.createInstrumentTrack(9, 0);
		RandomInstrument hihat = new RandomInstrument(hihatTrack);
		hihat.setDrum(true);

		RandomNoteStrategy drumNoteStrategy = drumRandomNoteStrategy(sound);
		drumNoteStrategy.setMinVelocity(85);
		drumNoteStrategy.setMaxVelocity(100);
		hihat.setNoteEventStrategy(drumNoteStrategy);

		hihat.setKey(new Key(sound, Scale.UNISON_SCALE));
		return hihat;
	}

	private RandomNoteStrategy drumRandomNoteStrategy() {
		return drumRandomNoteStrategy(null);
	}

	public RandomNoteStrategy drumRandomNoteStrategy(NoteValue noteValue) {
		RandomNoteStrategy drumNoteStrategy = new RandomNoteStrategy();
		drumNoteStrategy.setMaxDuration(2);
		drumNoteStrategy.setMaxNumberOfNotes(1);
		drumNoteStrategy.setMinNumberOfNotes(1);

		if (noteValue != null) {
			CustomNoteValueRange noteValueRange = new CustomNoteValueRange();
			noteValueRange.setAvailableNoteAsInt(noteValue.getIntValue());
			drumNoteStrategy.setNoteValueRange(noteValueRange);
		} else {
			RootNoteValueRange noteValueRange = new RootNoteValueRange();
			drumNoteStrategy.setNoteValueRange(noteValueRange);
		}

		return drumNoteStrategy;
	}
}
