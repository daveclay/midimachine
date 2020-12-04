package com.daveclay.midi;

import org.junit.Test;

import java.util.List;

import static org.junit.Assert.*;

public class KeyTest {
	private Key eMinor = new Key(NoteValue.E3, Scale.MINOR_SCALE);
	
	@Test
	public void testNotesBetweenAndDontCrash() {
		Key key = new Key(NoteValue.C0, Scale.MINOR_SCALE);
		key.getNotesBetween(NoteValue.C0, NoteValue.C5);
	}

	@Test
	public void testNotesBetween() {
		List<NoteValue> notes = eMinor.getNotesBetween(NoteValue.E1, NoteValue.E2);

		NoteValue[] expected = new NoteValue[] {
			NoteValue.E1,
			NoteValue.F_SHARP1,
			NoteValue.G1,
			NoteValue.A1,
			NoteValue.B1,
			NoteValue.C2,
			NoteValue.D2
		};

		assertEquals(notes.toArray(), expected);
	}

	@Test
	public void testNotes() {
		List<NoteValue> notes = eMinor.getNotesForOctaves(2);

		NoteValue[] expected = new NoteValue[] {
				NoteValue.E3,
				NoteValue.F_SHARP3,
				NoteValue.G3,
				NoteValue.A3,
				NoteValue.B3,
				NoteValue.C4,
				NoteValue.D4,
				NoteValue.E4,
				NoteValue.F_SHARP4,
				NoteValue.G4,
				NoteValue.A4,
				NoteValue.B4,
				NoteValue.C5,
				NoteValue.D5
		};

		assertEquals(notes.toArray(), expected);
	}
	
	@Test
	public void isNoteInKey() {
		assertTrue("Expcted root note to be in key", eMinor.isNoteInKey(NoteValue.E3));
		assertTrue("Expcted lower note to be in key", eMinor.isNoteInKey(NoteValue.F_SHARP1));
		assertTrue("Expcted higher note to be in key", eMinor.isNoteInKey(NoteValue.C9));
	}
}
