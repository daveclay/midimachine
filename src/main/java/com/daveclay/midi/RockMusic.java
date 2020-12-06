package com.daveclay.midi;

import com.daveclay.midi.random.RandomSequenceRhythmStrategy;
import com.daveclay.midi.sequence.*;
import com.daveclay.midi.random.PhraseBuildingStrategy;
import com.daveclay.midi.random.RandomInstrument;
import com.daveclay.midi.random.RandomSongBuilder;
import com.daveclay.midi.random.RandomNoteStrategy;

import javax.sound.midi.InvalidMidiDataException;
import javax.sound.midi.MidiUnavailableException;
import javax.sound.midi.Track;
import java.io.File;
import java.io.IOException;

import static com.daveclay.midi.NoteConstants.*;


/**
 */
public class RockMusic {

	public static void main(String[] args) throws InvalidMidiDataException, IOException, MidiUnavailableException {
		File outputFile = new File(args[0]);
		MidiSequencer midiSequencer = new MidiSequencer(outputFile);
		midiSequencer.setReceiver(MidiSequencer.determineDesiredReceiver(args));		
		midiSequencer.addProgramChangeEvent(0, GM_SLAP_BASS_1, 0);
		midiSequencer.addProgramChangeEvent(1, GM_ELECTRIC_GUITAR_CLEAN, 0);

		RandomSongBuilder randomSongBuilder = new RandomSongBuilder();
		RockMusic rockMusic = new RockMusic(midiSequencer, randomSongBuilder);
		rockMusic.buildSong();

		midiSequencer.play();
		midiSequencer.writeFile();
	}

	private final MidiSequencer midiSequencer;
	private final RandomSongBuilder songBuilder;
	private Track track;

	public RockMusic(MidiSequencer midiSequencer, RandomSongBuilder songBuilder) {
		this.midiSequencer = midiSequencer;
		this.track = midiSequencer.getTrack();
		this.songBuilder = songBuilder;
	}

	private void buildSong() throws MidiUnavailableException, InvalidMidiDataException {
		buildBassSequence();
		buildGuitarSequence();
		buildKickSequence();
		buildSnareSequence();
		buildHihatSequence();

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

		bassRandomInstrument.setNoteEventStrategy(bassNoteStrategy);

		RandomSequenceRhythmStrategy bassRhythmStrategy = new RandomSequenceRhythmStrategy();
		bassRhythmStrategy.setLikelyhoodOfEvent(.2);
		bassRhythmStrategy.setLikelyhoodOfNoteAtPosition(0, .8);
		bassRhythmStrategy.setLikelyhoodOfNoteAtPosition(8, .8);
		bassRandomInstrument.setRhythmStrategy(bassRhythmStrategy);

		PhraseBuildingStrategy phraseBuildingStrategy = new PhraseBuildingStrategy();
		phraseBuildingStrategy.setNumberOfMeasuresInPhrase(1);
		bassRandomInstrument.setPhraseBuildingStrategy(phraseBuildingStrategy);

		songBuilder.addInstrument(bassRandomInstrument);
	}

	public void buildGuitarSequence() throws InvalidMidiDataException, MidiUnavailableException {
		InstrumentTrack guitarTrack = midiSequencer.createInstrumentTrack(1, GM_ELECTRIC_GUITAR_JAZZ);
		RandomInstrument guitarRandomInstrument = new RandomInstrument(guitarTrack);

		RandomNoteStrategy guitarStrategy = new RandomNoteStrategy();
		guitarStrategy.setMaxDuration(8);
		guitarStrategy.setMinVelocity(60);
		guitarStrategy.setMaxVelocity(120);
		guitarStrategy.setMaxNumberOfNotes(3);
		guitarStrategy.setMinNumberOfNotes(1);

		InKeyNoteValueRange valueRange = new InKeyNoteValueRange();
		valueRange.setLowestRootNoteInKeyAsInt(1);
		valueRange.setHighestRootNoteInKeyAsInt(2);
		guitarStrategy.setNoteValueRange(valueRange);

		guitarRandomInstrument.setNoteEventStrategy(guitarStrategy);
		Key guitarKey = new Key(NoteValue.D4, Scale.MINOR_SCALE);
		guitarRandomInstrument.setKey(guitarKey);


		RandomSequenceRhythmStrategy guitarRhythmStrategy = new RandomSequenceRhythmStrategy();
		guitarRhythmStrategy.setLikelyhoodOfEvent(.1);
		guitarRhythmStrategy.setLikelyhoodOfNoteAtPosition(0, .8);
		guitarRhythmStrategy.setLikelyhoodOfNoteAtPosition(4, .8);
		guitarRhythmStrategy.setLikelyhoodOfNoteAtPosition(8, .8);
		guitarRhythmStrategy.setLikelyhoodOfNoteAtPosition(12, .8);

		guitarRandomInstrument.setRhythmStrategy(guitarRhythmStrategy);
		songBuilder.addInstrument(guitarRandomInstrument);
	}

	public void buildKickSequence() throws InvalidMidiDataException, MidiUnavailableException {
		RandomInstrument kick = buildDrumRandomInstrument(NoteValue.GM_BASS_DRUM_1);

		RandomSequenceRhythmStrategy kickRhythmStrategy = new RandomSequenceRhythmStrategy();
		kickRhythmStrategy.setLikelyhoodOfEvent(.2);
		kickRhythmStrategy.setLikelyhoodOfNoteAtPosition(0, .9);
		kickRhythmStrategy.setLikelyhoodOfNoteAtPosition(4, .7);
		kickRhythmStrategy.setLikelyhoodOfNoteAtPosition(8, .9);
		kickRhythmStrategy.setLikelyhoodOfNoteAtPosition(12, .7);

		kick.setRhythmStrategy(kickRhythmStrategy);
		songBuilder.addInstrument(kick);
	}

	public void buildSnareSequence() {
		RandomInstrument snare = buildDrumRandomInstrument(NoteValue.GM_ACOUSTIC_SNARE);

		RandomSequenceRhythmStrategy snareRhythmStrategy = new RandomSequenceRhythmStrategy();
		snareRhythmStrategy.setLikelyhoodOfEvent(.0);
		snareRhythmStrategy.setLikelyhoodOfNoteAtPosition(2, .2);
		snareRhythmStrategy.setLikelyhoodOfNoteAtPosition(4, .9);
		snareRhythmStrategy.setLikelyhoodOfNoteAtPosition(8, .2);
		snareRhythmStrategy.setLikelyhoodOfNoteAtPosition(12, .9);

		snare.setRhythmStrategy(snareRhythmStrategy);

		PhraseBuildingStrategy phraseBuildingStrategy = new PhraseBuildingStrategy();
		phraseBuildingStrategy.setNumberOfMeasuresInPhrase(2);
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

	public RandomInstrument buildDrumRandomInstrument(NoteValue sound) {
		InstrumentTrack hihatTrack = midiSequencer.createInstrumentTrack(9, 0);
		RandomInstrument hihat = new RandomInstrument(hihatTrack);
		hihat.setDrum(true);

		RandomNoteStrategy hihatNoteStrategy = new RandomNoteStrategy();
		hihatNoteStrategy.setMaxDuration(2);
		hihatNoteStrategy.setMinVelocity(85);
		hihatNoteStrategy.setMaxVelocity(100);
		hihatNoteStrategy.setMaxNumberOfNotes(1);
		hihatNoteStrategy.setMinNumberOfNotes(1);

		RootNoteValueRange valueRange = new RootNoteValueRange();
		hihatNoteStrategy.setNoteValueRange(valueRange);

		hihat.setNoteEventStrategy(hihatNoteStrategy);
		hihat.setKey(new Key(sound, Scale.UNISON_SCALE ));
		return hihat;
	}

	public void buildManualDrumSequence() throws InvalidMidiDataException, MidiUnavailableException {
		InstrumentTrack kickTrack = new InstrumentTrack(9,0,midiSequencer.getPPQ());
		Measure measure = new Measure();
		measure.setEvent(0,new Note(GM_BASS_DRUM_1,92,1));
		measure.setEvent(4,new Note(GM_BASS_DRUM_1,92,1));
		measure.setEvent(8,new Note(GM_BASS_DRUM_1,92,1));
		measure.setEvent(12,new Note(GM_BASS_DRUM_1,92,1));
		for (int i = 0; i < 32; i++) {
			kickTrack.addMeasure(measure);
		}
		kickTrack.addMIDIEvents(track);
		
		
		InstrumentTrack hatTrack = new InstrumentTrack(9,0,midiSequencer.getPPQ());
		measure = new Measure();
		for (int i = 0; i < 4; i++) {
			hatTrack.addMeasure(measure);
		}

		measure = new Measure();
		measure.setEvent(0,new Note(GM_CLOSED_HI_HAT,64,1));
		measure.setEvent(1,new Note(GM_CLOSED_HI_HAT,64,1));
		measure.setEvent(2,new Note(GM_OPEN_HI_HAT,64,1));

		measure.setEvent(4,new Note(GM_CLOSED_HI_HAT,64,1));
		measure.setEvent(5,new Note(GM_CLOSED_HI_HAT,64,1));
		measure.setEvent(6,new Note(GM_CLOSED_HI_HAT,64,1));
		measure.setEvent(7,new Note(GM_CLOSED_HI_HAT,64,1));

		measure.setEvent(8,new Note(GM_CLOSED_HI_HAT,64,1));
		measure.setEvent(9,new Note(GM_CLOSED_HI_HAT,64,1));
		measure.setEvent(10,new Note(GM_OPEN_HI_HAT,64,1));
		
		measure.setEvent(12,new Note(GM_CLOSED_HI_HAT,64,1));
		measure.setEvent(13,new Note(GM_CLOSED_HI_HAT,64,1));
		measure.setEvent(14,new Note(GM_CLOSED_HI_HAT,64,1));
		measure.setEvent(15,new Note(GM_CLOSED_HI_HAT,64,1));
		for (int i = 0; i < 16; i++) {
			hatTrack.addMeasure(measure);
		}

		measure = new Measure();
		for (int i = 0; i < 4; i++) {
			hatTrack.addMeasure(measure);
		}
		hatTrack.addMIDIEvents(track);

		
		InstrumentTrack snareTrack = new InstrumentTrack(9,0,midiSequencer.getPPQ());
		measure = new Measure();
		for (int i = 0; i < 8; i++) {
			snareTrack.addMeasure(measure);
		}
		measure = new Measure();
		measure.setEvent(4, new Note(GM_ACOUSTIC_SNARE,92,1));
		measure.setEvent(12, new Note(GM_ACOUSTIC_SNARE,92,1));
		for (int i = 0; i < 16; i++) {
			snareTrack.addMeasure(measure);
		}
		measure = new Measure();
		for (int i = 0; i < 8; i++) {
			snareTrack.addMeasure(measure);
		}
		snareTrack.addMIDIEvents(track);
		
		
		InstrumentTrack timbaleTrack = new InstrumentTrack(9,0,midiSequencer.getPPQ());
		measure = new Measure();
		for (int i = 0; i < 12; i++) {
			timbaleTrack.addMeasure(measure);
		}

		for (int i = 0; i < 8; i++) {
			if (i % 2 == 0) {
				measure = new Measure();
				measure.setEvent(2, new Note(GM_HIGH_TIMBALE,75,1));
				measure.setEvent(4, new Note(GM_HIGH_TIMBALE,75,1));
				measure.setEvent(7, new Note(GM_HIGH_TIMBALE,75,1));
				measure.setEvent(10, new Note(GM_LOW_TIMBALE,75,1));
				measure.setEvent(12, new Note(GM_LOW_TIMBALE,75,1));
				measure.setEvent(14, new Note(GM_LOW_TIMBALE,75,1));
				timbaleTrack.addMeasure(measure);
			} else {
				measure = new Measure();
				timbaleTrack.addMeasure(measure);
			}
		}
		timbaleTrack.addMIDIEvents(track);
		
		measure = new Measure();
		for (int i = 0; i < 12; i++) {
			timbaleTrack.addMeasure(measure);
		}
		
		InstrumentTrack crashTrack = new InstrumentTrack(9,0,midiSequencer.getPPQ());
		for (int j = 0; j < 9; j++) {
			measure = new Measure();
			measure.setEvent(0, new Note(GM_CRASH_CYMBAL_1,75,16));
			crashTrack.addMeasure(measure);
			for (int i = 0; i < 3; i++) {
				crashTrack.addMeasure(new Measure());
			}
		}
		crashTrack.addMIDIEvents(track);
	}
}

