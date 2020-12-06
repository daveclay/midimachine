package com.daveclay.midi.ogws;

import com.daveclay.midi.*;
import com.daveclay.midi.sequence.*;
import static com.daveclay.midi.NoteConstants.*;
import static com.daveclay.midi.ChordType.*;
import static com.daveclay.midi.random.RandyTheRandom.*;
import java.io.File;
import java.io.IOException;

import javax.sound.midi.InvalidMidiDataException;
import javax.sound.midi.MidiUnavailableException;
import javax.sound.midi.Track;

public class TripHopMusic {
	
	public static void main(String[] args) throws InvalidMidiDataException, IOException, MidiUnavailableException {
		File outputFile = new File(args[0]);
				
		MidiSequencer midiSequencer = new MidiSequencer(outputFile,64);
		midiSequencer.setReceiver(MidiSequencer.determineDesiredReceiver(args));

		TripHopMusic tripHopMusic = new TripHopMusic(midiSequencer);
		NoteValue rootNote = NoteValue.randomNoteBetween(NoteValue.B_FLAT2, NoteValue.B_FLAT3);
		Key rootKey = new Key(rootNote, Scale.MINOR_SCALE);
		
		System.out.println("");
		System.out.println("Root note: " + rootKey);
		
		tripHopMusic.buildBasslineSequence(rootKey);

		NoteValue note = Interval.OCTAVE.noteAbove(rootKey.getRootNote());
		Key otherRootKey = new Key(note, Scale.MINOR_SCALE);
		tripHopMusic.buildPadSequence(otherRootKey);

		tripHopMusic.buildPianoSequence(otherRootKey);

		tripHopMusic.buildDrumSequence();

		int tempo = randomIntBetweenInclusive(65,90);
		System.out.println("");
		System.out.println("Tempo: " + tempo + " bpm");
		System.out.println("Shuffle factor: " + tripHopMusic.shuffleFactor);
		midiSequencer.play(tempo);
		midiSequencer.writeFile();
	}

	private final MidiSequencer midiSequencer;
	private Track track;
	private float shuffleFactor = 0.0f;

	public TripHopMusic(MidiSequencer midiSequencer) {
		this.midiSequencer = midiSequencer;
		this.track = midiSequencer.getTrack();
		shuffleFactor = randomFloatBetweenExclusive(0.0f,0.4f);
		midiSequencer.setShuffleFactor(shuffleFactor);
	}

	public void buildPadSequence(Key rootKey) throws InvalidMidiDataException, MidiUnavailableException {
		
		int padInstrument = randomIntBetweenInclusive(GM_SYNTH_PAD_1_NEW_AGE,GM_SYNTH_PAD_8_SWEEP);
		
		int[] scale = rootKey.getNotesForOctavesAsInt(1);

		InstrumentTrack padTrack = midiSequencer.createInstrumentTrack(2,padInstrument);
		
		double differentIntervalsThreshold = 0.65;
		int intervalToAdd = randomElementFromArrayExcludingElements(scale, new int[] { scale[0] });
		int intervalToAdd2;
		if (nextDouble() > differentIntervalsThreshold) {
			intervalToAdd2 = randomElementFromArrayExcludingElements(scale,new int[] { 0, intervalToAdd });
		} else {
			intervalToAdd2 = intervalToAdd;
		}

		NotesSequenceEvent event1 = new NotesSequenceEvent();
		event1.addNote(new Note(scale[0],40,16 * 16));
		event1.addNote(new Note(intervalToAdd, 40, 16 * 16));
		Measure intervalMeasure1 = padTrack.createMeasure();
		intervalMeasure1.setEvent(0, event1);
		
		NotesSequenceEvent event2 = new NotesSequenceEvent();
		event2.addNote(new Note(scale[0],40,16 * 16));
		event2.addNote(new Note(intervalToAdd2, 40, 16*16));
		Measure intervalMeasure2 = padTrack.createMeasure();
		intervalMeasure2.setEvent(0,event2);
		
		for (int i = 0; i < 32; i++) {
			if (i / 4 % 2 == 0) {
				padTrack.addMeasure(padTrack.createMeasure());
			} else {
				if (i % 2 == 0) {
					padTrack.addMeasure(intervalMeasure1);
				} else {
					padTrack.addMeasure(intervalMeasure2);
				}			}
		}
		padTrack.addMIDIEvents(midiSequencer.getTrack());
				
		System.out.println("");
		System.out.println("Pad measure 1:");
		System.out.println(intervalMeasure1);

		System.out.println("");

		System.out.println("Pad measure 2:");
		System.out.println(intervalMeasure2);
	}
	
	public void buildPianoSequence(Key rootKey) throws InvalidMidiDataException {
		int[] scale = rootKey.getNotesForOctavesAsInt(1);

		InstrumentTrack pianoTrack = midiSequencer.createInstrumentTrack(3, GM_ELECTRIC_GRAND_PIANO);
		
		Measure pianoMeasure = pianoTrack.createMeasure();
		ChordType[] chordTypeOptions = new ChordType[] {
			DIMINISHED_SEVENTH_CHORD, MINOR_TRIAD, MINOR_SEVENTH_CHORD
		};
		ChordType chordType = randomElementFromArray(chordTypeOptions);
		ChordStrummer strummer = new ChordStrummer();
		strummer.setRootNote(scale[0]);
		strummer.setChordType(chordType);
		SequenceEvent pianoChordSequenceEvent = strummer.createStrumEvent();
		pianoMeasure.setEvent(7, pianoChordSequenceEvent);
		
		Phrase pianoPhrase = new Phrase();
		pianoPhrase.loopMeasure(pianoTrack.createMeasure(), 3);
		pianoPhrase.loopMeasure(pianoMeasure,1);

		pianoTrack.loopPhrase(pianoPhrase, 8);
		pianoTrack.addMIDIEvents(midiSequencer.getTrack());
	}
	
	public void buildBasslineSequence(Key rootKey) throws InvalidMidiDataException, MidiUnavailableException {
		
		int[] bassProgramOptions = new int[] { GM_SYNTH_LEAD_8_BASS_AND_LEAD, GM_ACOUSTIC_BASS, GM_ELECTRIC_BASS_FINGER, GM_ELECTRIC_BASS_PICK, GM_SLAP_BASS_1, GM_SLAP_BASS_2 };
		int bassProgram = randomElementFromArray(bassProgramOptions);		
		
		InstrumentTrack track = midiSequencer.createInstrumentTrack(0,bassProgram);

		int[] bassNotes = new int[5];
		int[] scale = rootKey.getNotesForOctavesAsInt(1);
		bassNotes[0] = scale[0];
		for (int i = 1; i < bassNotes.length; i++) {
			bassNotes[i] = randomElementFromArray(scale);
		}
		
		double measure1NoteOccuranceThreshold = (nextDouble() * 0.2) + 0.35;
		Measure measure1 = track.createMeasure();
		boolean firstNotePlayed = false;
		for (int i = 0; i < 16; i++) {
			if (Math.random() < measure1NoteOccuranceThreshold) {
				int duration = randomIntBetweenExclusive(midiSequencer.getPPQ() / 4, midiSequencer.getPPQ());
				
				int note;
				double chancesOfRootAsFirstNote = 0.75;
				if (firstNotePlayed) {
					note = randomElementFromArray(bassNotes);
				} else {
					if (Math.random() < chancesOfRootAsFirstNote) {
						note = bassNotes[0];
					} else {
						note = randomElementFromArray(bassNotes);
					}
				}
				
				int velocity = (30 + (int) (nextDouble() * 45)) ; // add "smoothing" amount!
				if (velocity < 0) velocity = 0;
				measure1.setEvent(i, new Note(note,velocity,duration));
				firstNotePlayed = true;
			}
		}
		
		System.out.println("");
		System.out.println("Bassline measure 1:");
		System.out.println(measure1);
		
		double measure2NoteOccuranceThreshold = (nextDouble() * 0.3) + 0.25;
		Measure measure2 = track.createMeasure();
		for (int i = 0; i < 16; i++) {
			if (Math.random() < measure2NoteOccuranceThreshold) {
				int duration = randomIntBetweenExclusive(midiSequencer.getPPQ() / 4, midiSequencer.getPPQ());
				int note = randomElementFromArray(bassNotes);
				int velocity = (30 + (int) (nextDouble() * 45)); // add "smoothing" amount!
				if (velocity < 0) velocity = 0;
				measure2.setEvent(i, new Note(note,velocity,duration));
			}
		}
		System.out.println("");
		System.out.println("Bassline measure 2:");
		System.out.println(measure2);

		Phrase phrase = track.createPhrase();
		phrase.loopMeasure(measure1, 3);
		phrase.loopMeasure(measure2, 1);
		track.loopPhrase(phrase, 8);
		
		track.addMIDIEvents(midiSequencer.getTrack());
	}

	public void buildDrumSequence() throws InvalidMidiDataException, MidiUnavailableException {
		InstrumentTrack kickTrack = midiSequencer.createDrumTrack();
		int secondKickPosition = randomIntBetweenInclusive(5,7);
		int thirdKickPosition = randomIntBetweenInclusive(13, 15);
		
		double differentKickSoundsThreshold = 0.8;
		int[] kickNoteOptions = new int[] { GM_ACOUSTIC_BASS_DRUM, GM_BASS_DRUM_1, GM_LOW_FLOOR_TOM };
		int kickNoteValue1 = randomElementFromArray(kickNoteOptions);
		int kickNoteValue2;
		if (Math.random() > differentKickSoundsThreshold) {
			kickNoteValue2 = randomElementFromArrayExcludingElements(kickNoteOptions, new int[] { kickNoteValue1 });
		} else {
			kickNoteValue2 = kickNoteValue1;
		}

		
		for (int i = 0; i < 32; i++) {
			Measure measure = kickTrack.createMeasure();
			measure.setEvent(0,new Note(kickNoteValue1,112,1));
			measure.setEvent(secondKickPosition,new Note(kickNoteValue2,112,1));
			if (i % 2 == 1) {
				measure.setEvent(thirdKickPosition,new Note(kickNoteValue1,82,1));
			}
			kickTrack.addMeasure(measure);
		}
		kickTrack.addMIDIEvents(track);

		/*
		RandomInstrument kickPart = new RandomInstrument();
		kickPart.setAvailableNotesAsInt(new int[] { kickNoteValue1 });
		kickPart.setNoteEventStrategy(noteEventStrategy)
		*/

		InstrumentTrack hatTrack = midiSequencer.createDrumTrack();

		Measure measure = kickTrack.createMeasure();
		for (int i = 0; i < 16; i++) {
			double noteOccuranceThreshold = (i % 4 == 0) ? 0.65 : 0.5;
			if (nextDouble() >= noteOccuranceThreshold) {
				double openHiHatThreshold = 0.8;
				int noteValue = nextDouble() > openHiHatThreshold ? GM_OPEN_HI_HAT : GM_CLOSED_HI_HAT;
				int velocity = randomIntBetweenInclusive(20, 43);
				measure.setEvent(i,new Note(noteValue,velocity,1));
			}
		}

		for (int i = 0; i < 12; i++) {
			hatTrack.addMeasure(measure);
		}

		for (int i = 0; i < 4; i++) {
			hatTrack.addMeasure(hatTrack.createMeasure());
		}
		
		for (int i = 0; i < 8; i++) {
			hatTrack.addMeasure(measure);
		}
		
		hatTrack.addMIDIEvents(track);

		InstrumentTrack snareTrack = midiSequencer.createInstrumentTrack(9,0);
		
		int firstSnareOffsetPosition = randomIntBetweenInclusive(2,4);
		int secondSnareOffsetPosition = randomIntBetweenInclusive(10,12);
		
		int offsetDivisor = randomIntBetweenInclusive(1,2) * 2;
		int offestTriggerModulo = offsetDivisor - 1;
		
		double differentSnareSoundsThreshold = 0.7;
		int[] snareNoteOptions = new int[] { GM_ACOUSTIC_SNARE, GM_ELECTRIC_SNARE, GM_HAND_CLAP, GM_SIDE_STICK };
		int snareNoteValue1 = randomElementFromArray(snareNoteOptions);
		int snareNoteValue2;
		if (nextDouble() > differentSnareSoundsThreshold) {
			snareNoteValue2 = randomElementFromArrayExcludingElements(snareNoteOptions, new int[] { snareNoteValue1 });
		} else {
			snareNoteValue2 = snareNoteValue1;
		}
		
		
		for (int i = 0; i < 24; i++) {
			measure = snareTrack.createMeasure();
			int firstSnarePosition;
			int secondSnarePosition;
			if (i % offsetDivisor == offestTriggerModulo) {
				firstSnarePosition = firstSnareOffsetPosition;
				secondSnarePosition = secondSnareOffsetPosition;
			} else {
				firstSnarePosition = 4;
				secondSnarePosition = 12;
			}
			measure.setEvent(firstSnarePosition, new Note(snareNoteValue1,92,1));
			measure.setEvent(secondSnarePosition, new Note(snareNoteValue2,92,1));
			snareTrack.addMeasure(measure);
		}
		measure = snareTrack.createMeasure();
		for (int i = 0; i < 8; i++) {
			snareTrack.addMeasure(measure);
		}
		snareTrack.addMIDIEvents(track);
		
				
		InstrumentTrack crashTrack = midiSequencer.createInstrumentTrack(9,0);
		for (int j = 0; j < 9; j++) {
			measure = crashTrack.createMeasure();
			measure.setEvent(0, new Note(GM_CRASH_CYMBAL_1,75,16));
			crashTrack.addMeasure(measure);
			for (int i = 0; i < 3; i++) {
				crashTrack.addMeasure(crashTrack.createMeasure());
			}
		}
		crashTrack.addMIDIEvents(track);
	}
}
