package com.daveclay.midi.realtime;

import com.daveclay.midi.random.AbstractRandomNoteStrategy;
import org.junit.Before;
import org.junit.Test;

import static junit.framework.Assert.*;

public class AddInstrumentParserTest {

	public static String addTrackLine = "add track on channel 9 note 32 as kick";
	public static String neverRepeatsPatternLine = "pattern length 2048 never repeat";
	public static String repeatingPatternLine = "pattern length 2048";
	public static String notesLine = "with notes min velocity 100 max velocity 127 min duration 16 max duration 64 min notes 1 max notes 3 as hard";
	public static String rhythmLine = "with rhythm bias .4 on every 64 starting on 31 using hard notes";
	private String entireConfig;

	@Test
	public void wtf() {
		String hi = "add track on channel 9 note 46 as hihat, pattern length 512, with notes min velocity 100 max velocity 127 min duration 16 max duration 16 min notes 1 max notes 1 as hard, with notes min velocity 40 max velocity 60 min duration 16 max duration 16 min notes 1 max notes 1 as soft, with rhythm bias .2 on every 64 starting on 128 using soft notes, with rhythm bias .8 on every 128 starting on 128 using hard notes";
		addTrackParser.parse(hi);
	}

	@Test
	public void testError() {
		try {
			addTrackParser.parse(addTrackLine + ", this is an invalid line," + notesLine + ", " + rhythmLine);
			fail("Expected to get an error");
		} catch (IllegalArgumentException expectedError) {
		}
	}

	@Test
	public void testParsingEntireLineTrackName() {
		addTrackParser.parse(entireConfig);
		assertEquals("kick", addTrackParser.getTrackName());
	}

/*
 * Commenting out because PositionNoteStrategy is not found
 *

	@Test
	public void testRhythm() {
		addTrackParser.parse(entireConfig);
		TickRhythm tickRhythm = addTrackParser.getTickRhythm();
		assertNotNull(tickRhythm);
		assertEquals("should have loop setAllSizes", 2048, tickRhythm.getNumberOfTicksInPhrase());

		PositionNoteStrategy[] positions = tickRhythm.getAllProbabilityOfNoteAtAllTicks();
		PositionNoteStrategy pns = positions[31];
		assertEquals(.4, pns.getProbabilityOfNote());

		pns = positions[35];
		assertNull(pns);
	}
*/

	@Test
	public void testParseLines() {
		assertTrue(addTrackParser.isTrackLine(addTrackLine));
		assertFalse(addTrackParser.isTrackLine(notesLine));

		assertTrue(addTrackParser.isPatternLine(neverRepeatsPatternLine));
		assertTrue(addTrackParser.isPatternLine(repeatingPatternLine));
		assertFalse(addTrackParser.isPatternLine(notesLine));

		assertTrue(addTrackParser.isNotesLine(notesLine));
		assertFalse(addTrackParser.isNotesLine(rhythmLine));

		assertTrue(addTrackParser.isRhythmLine(rhythmLine));
		assertFalse(addTrackParser.isRhythmLine(notesLine));
	}

	@Test
	public void testNoteStrategy() {
		AbstractRandomNoteStrategy strategy = addTrackParser.parseNoteStrategy(notesLine);
		assertNotNull(strategy);
		assertEquals("expected 100", 100, strategy.getMinVelocity());
		assertEquals("expected 127", 127, strategy.getMaxVelocity());
		assertEquals("expected 16", 16, strategy.getMinDuration());
		assertEquals("expected 64", 64, strategy.getMaxDuration());
		assertEquals("expected 1", 1, strategy.getMinNumberOfNotes());
		assertEquals("expected 3", 3, strategy.getMaxNumberOfNotes());
		assertEquals("expected the named strategy", strategy, addTrackParser.getNotesStrategyByName("hard"));
	}

	@Test
	public void testParseChannelFromTrack() {
		int channel = addTrackParser.parseChannel(addTrackLine);
		assertEquals("Should have channel 9", 9, channel);
	}

	@Test
	public void testParseNote() {
		int note = addTrackParser.parseTrackNote(addTrackLine);
		assertEquals(32, note);
	}

	@Test
	public void testParseTrackName() {
		String name = addTrackParser.parseTrackName(addTrackLine);
		assertEquals("Should have track 'kick'", "kick", name);
	}

	@Test
	public void testParseRepeats() {
		boolean neverRepeats = addTrackParser.parseNeverRepeats(repeatingPatternLine);
		assertEquals("should be false", false, neverRepeats);
	}

	@Test
	public void testParseNeverRepeat() {
		boolean neverRepeats = addTrackParser.parseNeverRepeats(neverRepeatsPatternLine);
		assertEquals("should be true", true, neverRepeats);
	}

	@Test
	public void testParsePatternLength() {
		int length = addTrackParser.parsePatternLength(neverRepeatsPatternLine);
		assertEquals("should have 2048", 2048, length);
	}

	@Test
	public void testNotesMinMaxVelocity() {
		int[] velocity = addTrackParser.parseVelocity(notesLine);
		int min = velocity[0];
		int max = velocity[1];
		assertEquals("Should have 100", 100, min);
		assertEquals("Should have 127", 127, max);
	}

	@Test
	public void testNotesMinMaxDuration() {
		int[] duration = addTrackParser.parseDuration(notesLine);
		int min = duration[0];
		int max = duration[1];
		assertEquals("Should have 16", 16, min);
		assertEquals("Should have 64`", 64, max);
	}

	@Test
	public void testNotesMinMaxNotes() {
		int[] notes = addTrackParser.parseNotes(notesLine);
		int min = notes[0];
		int max = notes[1];
		assertEquals("Should have 1", 1, min);
		assertEquals("Should have 3", 3, max);
	}

	@Test
	public void testParseNotesName() {
		String name = addTrackParser.parseNotesName(notesLine);
		assertEquals("should have 'hard'", "hard", name);
	}

	@Test
	public void testParseOnEvery() {
		int every = addTrackParser.parseOnEvery(rhythmLine);
		assertEquals("Should have 64", 64, every);
	}

	@Test
	public void testParseUsingNotes() {
		String notes = addTrackParser.parseUsingNotes(rhythmLine);
		assertEquals("Should have 'hard'", "hard", notes);
	}

	@Test
	public void testParseStartingOn() {
		int startingOn = addTrackParser.parseStartingOn(rhythmLine);
		assertEquals("should have 31", 31, startingOn);
	}

	@Test
	public void testParseBias() {
		double bias = addTrackParser.parseBias(rhythmLine);
		assertEquals("should have .4", .4, bias);

		String line = "with rhythm bias .9 on every 64 using soft notes";
		bias = addTrackParser.parseBias(line);
		assertEquals("should have .9", .9, bias);

		line = "with rhythm bias 1 on every 64 using soft notes";
		bias = addTrackParser.parseBias(line);
		assertEquals("should have 1", 1.0, bias);

		line = "with rhythm bias 0 on every 64 using soft notes";
		bias = addTrackParser.parseBias(line);
		assertEquals("should have 0", 0.0, bias);
	}

	@Before
	public void setUp() {
		addTrackParser = new AddTrackParser();

		StringBuilder all = new StringBuilder();
		all.append(addTrackLine).append(", ");
		all.append(neverRepeatsPatternLine).append(", ");
		all.append(notesLine).append(", ");
		all.append(rhythmLine);

		entireConfig = all.toString();
	}

	private AddTrackParser addTrackParser;
}
