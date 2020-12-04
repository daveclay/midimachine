package com.daveclay.midi;

import com.daveclay.midi.sequence.*;

import static com.daveclay.midi.ChordType.*;
import static com.daveclay.midi.NoteConstants.*;
import static com.daveclay.midi.random.RandyTheRandom.*;

import java.io.File;
import java.io.IOException;

import javax.sound.midi.InvalidMidiDataException;
import javax.sound.midi.MidiUnavailableException;
import javax.sound.midi.Track;

public class FolkMusic {
	public static void main(String[] args) throws InvalidMidiDataException, IOException, MidiUnavailableException {
		File outputFile = new File(args[0]);
		MidiSequencer midiSequencer = new MidiSequencer(outputFile,64);
		midiSequencer.setReceiver(MidiSequencer.determineDesiredReceiver(args));		

		FolkMusic folkMusic = new FolkMusic(midiSequencer);
		NoteValue rootNote = NoteValue.randomNoteBetween(NoteValue.G2, NoteValue.G3);
		Key rootKey = new Key(rootNote, Scale.MAJOR_SCALE);
		
		System.out.println("");
		System.out.println("Root note: " + rootKey.getRootNote());
		
		folkMusic.buildBasslineSequence(rootKey);
		folkMusic.buildRhythmGuitarSequence(rootKey);
		folkMusic.buildDrumSequence();

		NoteValue note = Interval.OCTAVE.noteAbove(rootKey.getRootNote());
		Key otherRootKey = new Key(note, Scale.MAJOR_SCALE);

		int tempo = randomIntBetweenInclusive(85,115);
		System.out.println("");
		System.out.println("Tempo: " + tempo + " bpm");
		System.out.println("Shuffle factor: " + folkMusic.shuffleFactor);
		midiSequencer.play(tempo);
		midiSequencer.writeFile();
	}

	private final MidiSequencer midiSequencer;
	private Track track;
	private float shuffleFactor = 0.0f;

	public FolkMusic(MidiSequencer midiSequencer) {
		this.midiSequencer = midiSequencer;
		this.track = midiSequencer.getTrack();
		shuffleFactor = randomFloatBetweenExclusive(0.0f,0.4f);
		midiSequencer.setShuffleFactor(shuffleFactor);
	}

	public void buildRhythmGuitarSequence(Key rootKey) throws InvalidMidiDataException {
		int[] scale = rootKey.getNotesForOctavesAsInt(1);

		int[] guitarProgramOptions = new int[] { GM_ACOUSTIC_GUITAR_NYLON, GM_ACOUSTIC_GUITAR_STEEL, GM_ELECTRIC_GUITAR_CLEAN, GM_ELECTRIC_GUITAR_JAZZ };
		int guitarProgram = randomElementFromArray(guitarProgramOptions);
		InstrumentTrack guitarTrack = midiSequencer.createInstrumentTrack(3, guitarProgram);
		
		int[] chordRootOptions = new int[] { scale[0], scale[0] + MAJOR_SECOND, scale[0] + MAJOR_THIRD, scale[0] + FOURTH, scale[0] + FIFTH, scale[0] + MAJOR_SIXTH, scale[0] + MAJOR_SEVENTH };
		ChordType[][] chordTypeOptions = new ChordType[][] { 
				new ChordType[] {MAJOR_TRIAD, DOMINANT_SEVENTH_CHORD, MAJOR_SUSPENDED_FOURTH_TRIAD, MAJOR_SUSPENDED_SECOND_TRIAD},
				new ChordType[] {MINOR_TRIAD, MINOR_SEVENTH_CHORD}, 
				new ChordType[] {MINOR_TRIAD, MINOR_SEVENTH_CHORD}, 
				new ChordType[] {MAJOR_TRIAD, DOMINANT_SEVENTH_CHORD, MAJOR_SUSPENDED_FOURTH_TRIAD, MAJOR_SUSPENDED_SECOND_TRIAD},
				new ChordType[] {MAJOR_TRIAD, DOMINANT_SEVENTH_CHORD, MAJOR_SUSPENDED_FOURTH_TRIAD, MAJOR_SUSPENDED_SECOND_TRIAD},
				new ChordType[] {MINOR_TRIAD, MINOR_SEVENTH_CHORD},
				new ChordType[] {DIMINISHED_TRIAD, DIMINISHED_SEVENTH_CHORD}
		};

		Phrase guitarPhrase = new Phrase();
		for (int i = 0; i < 4; i++) {
			Measure guitarMeasure = guitarTrack.createMeasure();
			int lastStrummedPosition = -1;
			int lastChordRootValue = -1;
			int lastChordRootIndex = -1;
			double[] weights = new double[] { 0.8, 0.1, 0.3, 0.1, 0.8, 0.1, 0.5, 0.2, 0.5, 0.3, 0.7, 0.2, 0.8, 0.5, 0.8, 0.2 };
			for (int j = 0; j < 16; j++) {
				double restFactor = 0.0;
				if (lastStrummedPosition > -1) {
					restFactor = (6-(j - lastStrummedPosition)) * 0.1;
				}
				if (nextDouble() < (weights[j] - restFactor)) {
					int chordRootValue; 
					int chordRootIndex;
					if (lastChordRootValue != -1 && nextDouble() > 0.20) {
						chordRootValue = lastChordRootValue;
						chordRootIndex = lastChordRootIndex;
					} else {
						chordRootIndex = randomIntBetweenExclusive(0, chordRootOptions.length);
						chordRootValue = chordRootOptions[chordRootIndex];
					}
					ChordType chordType = randomElementFromArray(chordTypeOptions[chordRootIndex]);
					ChordStrummer strummer = new ChordStrummer();
					strummer.setStrumTicks(2);
					strummer.setRootNote(chordRootValue);
					strummer.setChordType(chordType);
					strummer.setMeanDuration(64);
					strummer.setOctaves(2);
					if (j % 4 != 0) {
						strummer.setUpstroke(true);
					}
					NotesSequenceEvent guitarChordEvent = strummer.createStrumEvent();
					guitarChordEvent.setDisplayName(Note.nameForNote(chordRootValue) + chordType.getSymbol());
					guitarMeasure.setEvent(j, guitarChordEvent);
					lastStrummedPosition = j;
					lastChordRootValue = chordRootValue;
					lastChordRootIndex = chordRootIndex;
				}
			}
			guitarPhrase.loopMeasure(guitarMeasure, 1);
			System.out.println("Guitar measure " + (i + 1) + ":");
			System.out.println(guitarMeasure);
		}
		guitarTrack.loopPhrase(guitarPhrase, 8);
		guitarTrack.addMIDIEvents(midiSequencer.getTrack());
	}
	
	public void buildDrumSequence() throws InvalidMidiDataException, MidiUnavailableException {
		InstrumentTrack kickTrack = new InstrumentTrack(9,0,midiSequencer.getPPQ());
		Measure kickMeasure = kickTrack.createMeasure();
		kickMeasure.setEvent(0,new Note(GM_ACOUSTIC_BASS_DRUM,92,1));
		kickMeasure.setEvent(8,new Note(GM_ACOUSTIC_BASS_DRUM,92,1));
		kickMeasure.setEvent(10,new Note(GM_ACOUSTIC_BASS_DRUM,67,1));
		
		for (int i = 0; i < 32; i++) {
			kickTrack.addMeasure(kickMeasure);
		}
		kickTrack.addMIDIEvents(track);
		
		InstrumentTrack snareTrack = new InstrumentTrack(9,0,midiSequencer.getPPQ());
		Measure snareMeasure = snareTrack.createMeasure();
		snareMeasure.setEvent(4,new Note(GM_ACOUSTIC_SNARE,62,1));
		snareMeasure.setEvent(12,new Note(GM_ACOUSTIC_SNARE,62,1));
		for (int i = 0; i < 32; i++) {
			snareTrack.addMeasure(snareMeasure);
		}
		snareTrack.addMIDIEvents(track);
		
		InstrumentTrack tambourineTrack = new InstrumentTrack(9,0,midiSequencer.getPPQ());
		Measure tambourineMeasure = tambourineTrack.createMeasure();
		for (int i = 0; i < 16; i++) {
			int meanVelocity = (i % 4 == 0) ? 32 : 10;
			double velocityVariation = 0.2;
			int velocity = randomIntFromMeanWithPercentageVariation(meanVelocity, velocityVariation);
			int noteValue = GM_TAMBOURINE;
			
			tambourineMeasure.setEvent(i,new Note(noteValue,velocity,1));
		}
		Phrase tambourinePhrase = new Phrase();
		tambourinePhrase.loopMeasure(tambourineMeasure, 1);
		tambourineTrack.loopPhrase(tambourinePhrase, 32);
		tambourineTrack.addMIDIEvents(track);
	}
	
	public void buildBasslineSequence(Key rootKey) throws InvalidMidiDataException, MidiUnavailableException {
		
		int[] bassProgramOptions = new int[] { GM_ACOUSTIC_BASS, GM_ELECTRIC_BASS_FINGER, GM_ELECTRIC_BASS_PICK };
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
				int duration = randomIntBetweenExclusive(0, 4);
				
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
				
				int velocity = (90 + (int) (nextDouble() * 45)) - (40 - duration); // add "smoothing" amount!
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
				int duration = (int) (Math.random() * 4);
				int note = randomElementFromArray(bassNotes);
				int velocity = (70 + (int) (nextDouble() * 45)) - (40 - duration); // add "smoothing" amount!
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
	
}
