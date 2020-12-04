package com.daveclay.midi;

/**
 */
public class NoteConstants {

	public static final int UNISON = 0;
	public static final int MINOR_SECOND = 1;
	public static final int MAJOR_SECOND = 2;
	public static final int MINOR_THIRD = 3;
	public static final int MAJOR_THIRD = 4;
	public static final int FOURTH = 5;
	public static final int FLAT_FIFTH = 6;
	public static final int FIFTH = 7;
	public static final int MINOR_SIXTH = 8;
	public static final int MAJOR_SIXTH = 9;
	public static final int MINOR_SEVENTH = 10;
	public static final int MAJOR_SEVENTH = 11;
	public static final int OCTAVE = 12;
	
	public static final int ROOT = UNISON;
	public static final int DIMINISHED_FIFTH = FLAT_FIFTH;
	public static final int TRITONE = FLAT_FIFTH;
	public static final int DIMINISHED_SEVENTH = MAJOR_SIXTH;

	public static final int[] UNISON_SCALE = new int[] { UNISON };
	
	public static final int[] PERFECT_SCALE = new int[] {
			UNISON, FOURTH, FIFTH
	};

	public static final int[] MINOR_PENTATONIC_SCALE = new int[] {
			UNISON, MINOR_THIRD, FOURTH, FIFTH, MINOR_SEVENTH
	};

	public static final int[] MAJOR_PENTATONIC_SCALE = new int[] {
			UNISON, MAJOR_THIRD, FOURTH, FIFTH, MAJOR_SEVENTH
	};

	public static final int[] BLUES_CHROMATIC_SCALE = new int[] {
			UNISON, MINOR_THIRD, MAJOR_THIRD, FOURTH, FLAT_FIFTH, FIFTH, MINOR_SEVENTH
	};

	public static final int[] BLUES_MAJOR_SCALE = new int[] {
			UNISON, MAJOR_THIRD, FOURTH, FLAT_FIFTH, FIFTH, MAJOR_SEVENTH
	};

	public static final int[] BLUES_MINOR_SCALE = new int[] {
			UNISON, MINOR_THIRD, FOURTH, FLAT_FIFTH, FIFTH, MINOR_SEVENTH
	};

	public static final int[] MINOR_SCALE = new int[] {
			UNISON, MAJOR_SECOND, MINOR_THIRD, FOURTH, FIFTH, MINOR_SIXTH, MINOR_SEVENTH
	};

	public static final int[] MAJOR_SCALE = new int[] {
			UNISON, MAJOR_SECOND, MAJOR_THIRD, FOURTH, FIFTH, MAJOR_SIXTH, MAJOR_SEVENTH
	};

	public static final int[][] MINOR_SCALES = new int[][] {
			MINOR_PENTATONIC_SCALE,
			MINOR_SCALE
	};

	public static final int[][] MAJOR_SCALES = new int[][] {
			MAJOR_PENTATONIC_SCALE,
			MAJOR_SCALE
	};

	public static boolean isMinor(int[] scale) {
		for (int[] aScale : MINOR_SCALES) {
			if (aScale == scale) {
				return true;
			}
		}
		return false;
	}

	public static boolean isMajor(int[] scale) {
		for (int[] aScale : MAJOR_SCALES) {
			if (aScale == scale) {
				return true;
			}
		}
		return false;
	}

	// hrm, if I'm on unison, minor scale, move to third, buildSequence major? chord change?


	public static final int C0 = 0;
	public static final int C_SHARP0 = 1;
	public static final int D_FLAT0 = 1;
	public static final int D0 = 2;
	public static final int D_SHARP0 = 3;
	public static final int E_FLAT0 = 3;
	public static final int E0 = 4;
	public static final int F0 = 5;
	public static final int F_SHARP0 = 6;
	public static final int G_FLAT0 = 6;
	public static final int G0 = 7;
	public static final int G_SHARP0 = 8;
	public static final int A_FLAT0 = 8;
	public static final int A0 = 9;
	public static final int A_SHARP0 = 10;
	public static final int B_FLAT0 = 10;
	public static final int B0 = 11;

	public static final int C1 = 12;
	public static final int C_SHARP1 = 13;
	public static final int D_FLAT1 = 13;
	public static final int D1 = 14;
	public static final int D_SHARP1 = 15;
	public static final int E_FLAT1 = 15;
	public static final int E1 = 16;
	public static final int F1 = 17;
	public static final int F_SHARP1 = 18;
	public static final int G_FLAT1 = 18;
	public static final int G1 = 19;
	public static final int G_SHARP1 = 20;
	public static final int A_FLAT1 = 20;
	public static final int A1 = 21;
	public static final int A_SHARP1 = 22;
	public static final int B_FLAT1 = 22;
	public static final int B1 = 23;

	public static final int C2 = 24;
	public static final int C_SHARP2 = 25;
	public static final int D_FLAT2 = 25;
	public static final int D2 = 26;
	public static final int D_SHARP2 = 27;
	public static final int E_FLAT2 = 27;
	public static final int E2 = 28;
	public static final int F2 = 29;
	public static final int F_SHARP2 = 30;
	public static final int G_FLAT2 = 30;
	public static final int G2 = 31;
	public static final int G_SHARP2 = 32;
	public static final int A_FLAT2 = 32;
	public static final int A2 = 33;
	public static final int A_SHARP2 = 34;
	public static final int B_FLAT2 = 34;
	public static final int B2 = 35;

	public static final int C3 = 36;
	public static final int C_SHARP3 = 37;
	public static final int D_FLAT3 = 37;
	public static final int D3 = 38;
	public static final int D_SHARP3 = 39;
	public static final int E_FLAT3 = 39;
	public static final int E3 = 40;
	public static final int F3 = 41;
	public static final int F_SHARP3 = 42;
	public static final int G_FLAT3 = 42;
	public static final int G3 = 43;
	public static final int G_SHARP3 = 44;
	public static final int A_FLAT3 = 44;
	public static final int A3 = 45;
	public static final int A_SHARP3 = 46;
	public static final int B_FLAT3 = 46;
	public static final int B3 = 47;

	public static final int C4 = 48;
	public static final int C_SHARP4 = 49;
	public static final int D_FLAT4 = 49;
	public static final int D4 = 50;
	public static final int D_SHARP4 = 51;
	public static final int E_FLAT4 = 51;
	public static final int E4 = 52;
	public static final int F4 = 53;
	public static final int F_SHARP4 = 54;
	public static final int G_FLAT4 = 54;
	public static final int G4 = 55;
	public static final int G_SHARP4 = 56;
	public static final int A_FLAT4 = 56;
	public static final int A4 = 57;
	public static final int A_SHARP4 = 58;
	public static final int B_FLAT4 = 58;
	public static final int B4 = 59;

	public static final int C5 = 60;
	public static final int C_SHARP5 = 61;
	public static final int D_FLAT5 = 61;
	public static final int D5 = 62;
	public static final int D_SHARP5 = 63;
	public static final int E_FLAT5 = 63;
	public static final int E5 = 64;
	public static final int F5 = 65;
	public static final int F_SHARP5 = 66;
	public static final int G_FLAT5 = 66;
	public static final int G5 = 67;
	public static final int G_SHARP5 = 68;
	public static final int A_FLAT5 = 68;
	public static final int A5 = 69;
	public static final int A_SHARP5 = 70;
	public static final int B_FLAT5 = 70;
	public static final int B5 = 71;

	public static final int C6 = 72;
	public static final int C_SHARP6 = 73;
	public static final int D_FLAT6 = 73;
	public static final int D6 = 74;
	public static final int D_SHARP6 = 75;
	public static final int E_FLAT6 = 75;
	public static final int E6 = 76;
	public static final int F6 = 77;
	public static final int F_SHARP6 = 78;
	public static final int G_FLAT6 = 78;
	public static final int G6 = 79;
	public static final int G_SHARP6 = 80;
	public static final int A_FLAT6 = 80;
	public static final int A6 = 81;
	public static final int A_SHARP6 = 82;
	public static final int B_FLAT6 = 82;
	public static final int B6 = 83;

	public static final int C7 = 84;
	public static final int C_SHARP7 = 85;
	public static final int D_FLAT7 = 85;
	public static final int D7 = 86;
	public static final int D_SHARP7 = 87;
	public static final int E_FLAT7 = 87;
	public static final int E7 = 88;
	public static final int F7 = 89;
	public static final int F_SHARP7 = 90;
	public static final int G_FLAT7 = 90;
	public static final int G7 = 91;
	public static final int G_SHARP7 = 92;
	public static final int A_FLAT7 = 92;
	public static final int A7 = 93;
	public static final int A_SHARP7 = 94;
	public static final int B_FLAT7 = 94;
	public static final int B7 = 95;

	public static final int C8 = 96;
	public static final int C_SHARP8 = 97;
	public static final int D_FLAT8 = 97;
	public static final int D8 = 98;
	public static final int D_SHARP8 = 99;
	public static final int E_FLAT8 = 99;
	public static final int E8 = 100;
	public static final int F8 = 101;
	public static final int F_SHARP8 = 102;
	public static final int G_FLAT8 = 102;
	public static final int G8 = 103;
	public static final int G_SHARP8 = 104;
	public static final int A_FLAT8 = 104;
	public static final int A8 = 105;
	public static final int A_SHARP8 = 106;
	public static final int B_FLAT8 = 106;
	public static final int B8 = 107;

	public static final int C9 = 108;
	public static final int C_SHARP9 = 109;
	public static final int D_FLAT9 = 109;
	public static final int D9 = 110;
	public static final int D_SHARP9 = 111;
	public static final int E_FLAT9 = 111;
	public static final int E9 = 112;
	public static final int F9 = 113;
	public static final int F_SHARP9 = 114;
	public static final int G_FLAT9 = 114;
	public static final int G9 = 115;
	public static final int G_SHARP9 = 116;
	public static final int A_FLAT9 = 116;
	public static final int A9 = 117;
	public static final int A_SHARP9 = 118;
	public static final int B_FLAT9 = 118;
	public static final int B9 = 119;

	public static final int C10 = 120;
	public static final int C_SHARP10 = 121;
	public static final int D_FLAT10 = 121;
	public static final int D10 = 122;
	public static final int D_SHARP10 = 123;
	public static final int E_FLAT10 = 123;
	public static final int E10 = 124;
	public static final int F10 = 125;
	public static final int F_SHARP10 = 126;
	public static final int G_FLAT10 = 126;
	public static final int G10 = 127;


	public static final int GM_ACOUSTIC_BASS_DRUM = 36;
	public static final int GM_BASS_DRUM_1 = 36;
	public static final int GM_SIDE_STICK = 37;
	public static final int GM_ACOUSTIC_SNARE = 38;
	public static final int GM_HAND_CLAP = 39;
	public static final int GM_ELECTRIC_SNARE = 40;
	public static final int GM_LOW_FLOOR_TOM = 41;
	public static final int GM_CLOSED_HI_HAT = 42;
	public static final int GM_HIGH_FLOOR_TOM = 43;
	public static final int GM_PEDAL_HI_HAT = 44;
	public static final int GM_LOW_TOM = 45;
	public static final int GM_OPEN_HI_HAT = 46;
	public static final int GM_LOW_MID_TOM = 47;
	public static final int GM_HI_MID_TOM = 48;
	public static final int GM_CRASH_CYMBAL_1 = 49;
	public static final int GM_HIGH_TOM = 50;
	public static final int GM_RIDE_CYMBAL_1 = 51;
	public static final int GM_CHINESE_CYMBAL = 52;
	public static final int GM_RIDE_BELL = 53;
	public static final int GM_TAMBOURINE = 54;
	public static final int GM_SPLASH_CYMBAL = 55;
	public static final int GM_COWBELL = 56;
	public static final int GM_CRASH_CYMBAL_2 = 57;
	public static final int GM_VIBRASLAP = 58;
	public static final int GM_RIDE_CYMBAL_2 = 59;
	public static final int GM_HI_BONGO = 60;
	public static final int GM_LOW_BONGO = 61;
	public static final int GM_MUTE_HI_CONGA = 62;
	public static final int GM_OPEN_HI_CONGA = 63;
	public static final int GM_LOW_CONGA = 64;
	public static final int GM_HIGH_TIMBALE = 65;
	public static final int GM_LOW_TIMBALE = 66;
	public static final int GM_HIGH_AGOGO = 67;
	public static final int GM_LOW_AGOGO = 68;
	public static final int GM_CABASA = 69;
	public static final int GM_MARACAS = 70;
	public static final int GM_SHORT_WHISTLE = 71;
	public static final int GM_LONG_WHISTLE = 72;
	public static final int GM_SHORT_GUIRO = 73;
	public static final int GM_LONG_GUIRO = 74;
	public static final int GM_CLAVES = 75;
	public static final int GM_HI_WOOD_BLOCK = 76;
	public static final int GM_LOW_WOOD_BLOCK = 77;
	public static final int GM_MUTE_CUICA = 78;
	public static final int GM_OPEN_CUICA = 79;
	public static final int GM_MUTE_TRIANGLE = 80;
	public static final int GM_OPEN_TRIANGLE = 81;


	
	public static final int GM_ACOUSTIC_GRAND_PIANO = 1 - 1;
	public static final int GM_BRIGHT_ACOUSTIC_PIANO = 2 - 1;
	public static final int GM_ELECTRIC_GRAND_PIANO = 3 - 1;
	public static final int GM_HONKY_TONK_PIANO = 4 - 1;
	public static final int GM_ELECTRIC_PIANO_1 = 5 - 1;
	public static final int GM_ELECTRIC_PIANO_2 = 6 - 1;
	public static final int GM_HARPSICHORD = 7 - 1;
	public static final int GM_CLAVINET = 8 - 1;
	public static final int GM_CELESTA = 9 - 1;
	public static final int GM_GLOCKENSPIEL = 10 - 1;
	public static final int GM_MUSIC_BOX = 11 - 1;
	public static final int GM_VIBRAPHONE = 12 - 1;
	public static final int GM_MARIMBA = 13 - 1;
	public static final int GM_XYLOPHONE = 14 - 1;
	public static final int GM_TUBULAR_BELLS = 15 - 1;
	public static final int GM_DULCIMER = 16 - 1;
	public static final int GM_DRAWBAR_ORGAN = 17 - 1;
	public static final int GM_PERCUSSIVE_ORGAN = 18 - 1;
	public static final int GM_ROCK_ORGAN = 19 - 1;
	public static final int GM_CHURCH_ORGAN = 20 - 1;
	public static final int GM_REED_ORGAN = 21 - 1;
	public static final int GM_ACCORDION = 22 - 1;
	public static final int GM_HARMONICA = 23 - 1;
	public static final int GM_TANGO_ACCORDION = 24 - 1;
	public static final int GM_ACOUSTIC_GUITAR_NYLON = 25 - 1;
	public static final int GM_ACOUSTIC_GUITAR_STEEL = 26 - 1;
	public static final int GM_ELECTRIC_GUITAR_JAZZ = 27 - 1;
	public static final int GM_ELECTRIC_GUITAR_CLEAN = 28 - 1;
	public static final int GM_ELECTRIC_GUITAR_MUTED = 29 - 1;
	public static final int GM_OVERDRIVEN_GUITAR = 30 - 1;
	public static final int GM_DISTORTION_GUITAR = 31 - 1;
	public static final int GM_GUITAR_HARMONICS = 32 - 1;
	public static final int GM_ACOUSTIC_BASS = 33 - 1;
	public static final int GM_ELECTRIC_BASS_FINGER = 34 - 1;
	public static final int GM_ELECTRIC_BASS_PICK = 35 - 1;
	public static final int GM_FRETLESS_BASS = 36 - 1;
	public static final int GM_SLAP_BASS_1 = 37 - 1;
	public static final int GM_SLAP_BASS_2 = 38 - 1;
	public static final int GM_SYNTH_BASS_1 = 39 - 1;
	public static final int GM_SYNTH_BASS_2 = 40 - 1;
	public static final int GM_VIOLIN = 41 - 1;
	public static final int GM_VIOLA = 42 - 1;
	public static final int GM_CELLO = 43 - 1;
	public static final int GM_CONTRABASS = 44 - 1;
	public static final int GM_TREMOLO_STRINGS = 45 - 1;
	public static final int GM_PIZZICATO_STRINGS = 46 - 1;
	public static final int GM_ORCHESTRAL_HARP = 47 - 1;
	public static final int GM_TIMPANI = 48 - 1;
	public static final int GM_STRING_ENSEMBLE_1 = 49 - 1;
	public static final int GM_STRING_ENSEMBLE_2 = 50 - 1;
	public static final int GM_SYNTH_STRINGS_1 = 51 - 1;
	public static final int GM_SYNTH_STRINGS_2 = 52 - 1;
	public static final int GM_CHOIR_AAHS = 53 - 1;
	public static final int GM_VOICE_OOHS = 54 - 1;
	public static final int GM_SYNTH_CHOIR = 55 - 1;
	public static final int GM_ORCHESTRA_HIT = 56 - 1;
	public static final int GM_TRUMPET = 57 - 1;
	public static final int GM_TROMBONE = 58 - 1;
	public static final int GM_TUBA = 59 - 1;
	public static final int GM_MUTED_TRUMPET = 60 - 1;
	public static final int GM_FRENCH_HORN = 61 - 1;
	public static final int GM_BRASS_SECTION = 62 - 1;
	public static final int GM_SYNTH_BRASS_1 = 63 - 1;
	public static final int GM_SYNTH_BRASS_2 = 64 - 1;
	public static final int GM_SOPRANO_SAX = 65 - 1;
	public static final int GM_ALTO_SAX = 66 - 1;
	public static final int GM_TENOR_SAX = 67 - 1;
	public static final int GM_BARITONE_SAX = 68 - 1;
	public static final int GM_OBOE = 69 - 1;
	public static final int GM_ENGLISH_HORN = 70 - 1;
	public static final int GM_BASSOON = 71 - 1;
	public static final int GM_CLARINET = 72 - 1;
	public static final int GM_PICCOLO = 73 - 1;
	public static final int GM_FLUTE = 74 - 1;
	public static final int GM_RECORDER = 75 - 1;
	public static final int GM_PAN_FLUTE = 76 - 1;
	public static final int GM_BLOWN_BOTTLE = 77 - 1;
	public static final int GM_SHAKUHACHI = 78 - 1;
	public static final int GM_WHISTLE = 79 - 1;
	public static final int GM_OCARINA = 80 - 1;
	public static final int GM_SYNTH_LEAD_1_SQUARE = 81 - 1;
	public static final int GM_SYNTH_LEAD_2_SAWTOOTH = 82 - 1;
	public static final int GM_SYNTH_LEAD_3_CALLIOPE = 83 - 1;
	public static final int GM_SYNTH_LEAD_4_CHIFF = 84 - 1;
	public static final int GM_SYNTH_LEAD_5_CHARANG = 85 - 1;
	public static final int GM_SYNTH_LEAD_6_VOICE = 86 - 1;
	public static final int GM_SYNTH_LEAD_7_FIFTHS = 87 - 1;
	public static final int GM_SYNTH_LEAD_8_BASS_AND_LEAD = 88 - 1;
	public static final int GM_SYNTH_PAD_1_NEW_AGE = 89 - 1;
	public static final int GM_SYNTH_PAD_2_WARM = 90 - 1;
	public static final int GM_SYNTH_PAD_3_POLYSYNTH = 91 - 1;
	public static final int GM_SYNTH_PAD_4_CHOIR = 92 - 1;
	public static final int GM_SYNTH_PAD_5_BOWED = 93 - 1;
	public static final int GM_SYNTH_PAD_6_METALLIC = 94 - 1;
	public static final int GM_SYNTH_PAD_7_HALO = 95 - 1;
	public static final int GM_SYNTH_PAD_8_SWEEP = 96 - 1;
	public static final int GM_SYNTH_FX_1_RAIN = 97 - 1;
	public static final int GM_SYNTH_FX_2_SOUNDTRACK = 98 - 1;
	public static final int GM_SYNTH_FX_3_CRYSTAL = 99 - 1;
	public static final int GM_SYNTH_FX_4_ATMOSPHERE = 100 - 1;
	public static final int GM_SYNTH_FX_5_BRIGHTNESS = 101 - 1;
	public static final int GM_SYNTH_FX_6_GOBLINS = 102 - 1;
	public static final int GM_SYNTH_FX_7_ECHOES = 103 - 1;
	public static final int GM_SYNTH_FX_8_SCI_FI = 104 - 1;
	public static final int GM_SITAR = 105 - 1;
	public static final int GM_BANJO = 106 - 1;
	public static final int GM_SHAMISEN = 107 - 1;
	public static final int GM_KOTO = 108 - 1;
	public static final int GM_KALIMBA = 109 - 1;
	public static final int GM_BAG_PIPE = 110 - 1;
	public static final int GM_FIDDLE = 111 - 1;
	public static final int GM_SHANAI = 112 - 1;
	public static final int GM_TINKLE_BELL = 113 - 1;
	public static final int GM_AGOGO = 114 - 1;
	public static final int GM_STEEL_DRUMS = 115 - 1;
	public static final int GM_WOODBLOCK = 116 - 1;
	public static final int GM_TAIKO_DRUM = 117 - 1;
	public static final int GM_MELODIC_TOM = 118 - 1;
	public static final int GM_SYNTH_DRUM = 119 - 1;
	public static final int GM_REVERSE_CYMBAL = 120 - 1;
	public static final int GM_GUITAR_FRET_NOISE = 121 - 1;
	public static final int GM_BREATH_NOISE = 122 - 1;
	public static final int GM_SEASHORE = 123 - 1;
	public static final int GM_BIRD_TWEET = 124 - 1;
	public static final int GM_TELEPHONE_RING = 125 - 1;
	public static final int GM_HELICOPTER = 126 - 1;
	public static final int GM_APPLAUSE = 127 - 1;
	public static final int GM_GUNSHOT = 128 - 1;
	
	/*
	private static FileWriter writer;

	public static void main(String[] args) throws IOException {
		writer = new FileWriter("/Users/dclay/notes.java");
		String[] notes = new String[] { "A", "A_SHARP", "B", "C", "C_SHARP", "D", "D_SHARP", "E", "F", "F_SHARP", "G", "G_SHARP"};

		int notesIdx = 3; // start at C
		int noteNumber = 0;
		for (int i = 0; i < 128; i++) {
			String note = notes[notesIdx];

			if (i > 0 && i % 12 == 0) {
				noteNumber++;
				writer.write("\n");
			}

			printNote(note, noteNumber, i);

			if (note.endsWith("SHARP")) {
				String nextNote;
				if (notesIdx == notes.length - 1) {
					nextNote = notes[0];
				} else {
					nextNote = notes[notesIdx+1];
				}
				printNote(nextNote + "_FLAT", noteNumber, i);
			}

			notesIdx++;
			if (notesIdx == notes.length) {
				notesIdx = 0;
			}
		}

		writer.flush();
		writer.close();
	}

	public static void printNote(String note, int noteNumber, int value) throws IOException {
		String noteAbove = "public static final int " + note + noteNumber + " = " + value;
		System.out.println(noteAbove);
		writer.write(noteAbove + "\n");
	}
	*/
}
