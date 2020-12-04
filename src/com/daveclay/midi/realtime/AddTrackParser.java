package com.daveclay.midi.realtime;

import com.daveclay.midi.InKeyNoteValueRange;
import com.daveclay.midi.Key;
import com.daveclay.midi.NoteValue;
import com.daveclay.midi.Scale;
import com.daveclay.midi.random.RandomNoteStrategy;

import java.util.HashMap;
import java.util.Map;

/**
*/
public class AddTrackParser {

	private Map<String, RandomNoteStrategy> nameToStrategy = new HashMap<String, RandomNoteStrategy>();
	private TickRhythm tickRhythm;
	private String trackName;
	private RealtimeTrack track;

	public int parseChannel(String line) {
		String textToFind = "channel ";
		return parseInt(line, textToFind);
	}

	public String parseTrackName(String line) {
		String textToFind = "as ";
		return parseText(line, textToFind);
	}

	public boolean parseNeverRepeats(String line) {
		return line.indexOf("never repeat") > -1;
	}	

	public int parsePatternLength(String line) {
		String textToFind = "pattern length ";
		return parseInt(line, textToFind);
	}

	public int[] parseDuration(String line) {
		String textToFind = "duration ";
		return parseMinMax(line, textToFind);
	}

	public int[] parseVelocity(String line) {
		String textToFind = "velocity ";
		return parseMinMax(line, textToFind);
	}

	public int[] parseNotes(String line) {
		String textToFind = "notes ";
		return parseMinMax(line, textToFind);
	}

	public double parseBias(String line) {
		String textToFind = "bias ";
		String value = parseText(line, textToFind);
		return Double.parseDouble(value);
	}

	public int parseOnEvery(String rhythmLine) {
		String textToFind = "on every ";
		return parseInt(rhythmLine, textToFind);
	}

	public String parseUsingNotes(String line) {
		String textToFind = "using ";
		return parseText(line, textToFind);
	}

	private int[] parseMinMax(String line, String textToFind) {
		int min = parseInt(line, "min " + textToFind);
		int max = parseInt(line, "max " + textToFind);
		return new int[] { min, max };
	}

	public String parseNotesName(String line) {
		String textToFind = "as ";
		return parseText(line, textToFind);
	}

	private int parseInt(String line, String textToFind) {
		String value = parseText(line, textToFind);
		return Integer.parseInt(value);
	}

	private String parseText(String line, String textToFind) {
		int startIdx = line.indexOf(textToFind) + textToFind.length();
		int endIdx = line.indexOf(" ", startIdx);
		if (endIdx < 0) {
			endIdx = line.length();
		}
		return line.substring(startIdx, endIdx);
	}

	public RandomNoteStrategy parseNoteStrategy(String line) {
		RandomNoteStrategy strategy = new RandomNoteStrategy();

		int[] velocity = parseVelocity(line);
		strategy.setMinVelocity(velocity[0]);
		strategy.setMaxVelocity(velocity[1]);

		int[] duration = parseDuration(line);
		strategy.setMinDuration(duration[0]);
		strategy.setMaxDuration(duration[1]);

		int[] notes = parseNotes(line);
		strategy.setMinNumberOfNotes(notes[0]);
		strategy.setMaxNumberOfNotes(notes[1]);

		String name = parseNotesName(line);
		nameToStrategy.put(name, strategy);

		return strategy;
	}

	public RandomNoteStrategy getNotesStrategyByName(String name) {
		return nameToStrategy.get(name);
	}

	public void parse(String line) {
		tickRhythm = new TickRhythm();
		String[] lines = line.split(",");
		for (String part : lines) {
			if (isTrackLine(part)) {
				parseTrackConfig(part);
			} else if (isPatternLine(part)) {
				parsePatternConfig(part);
			} else if (isNotesLine(part)) {
				parseNoteStrategy(part);
			} else if (isRhythmLine(part)) {
				parseRhythmConfig(part);
			} else {
				throw new IllegalArgumentException("Unknown config line: " + part);
			}
		}
	}

	private void parseRhythmConfig(String part) {
		/*
	"add track on channel 9 as kick";
	"pattern length 2048 never repeat";
	"pattern length 2048";
	"with notes min velocity 100 max velocity 127 min duration 16 max duration 64 min notes 1 max notes 3 as hard";
	"with rhythm bias .4 on every 64 starting on 31 using soft notes";
		 */
		double bias = parseBias(part);
		int onEvery = parseOnEvery(part);
		int startingOn = parseStartingOn(part);
		String notes = parseUsingNotes(part);

		RandomNoteStrategy notesStrategy = getNotesStrategyByName(notes);
		if (notesStrategy == null) {
			throw new IllegalArgumentException("No notes configured name " + notes);
		}
		
		tickRhythm.setBiasForEveryNthTick(bias, onEvery, startingOn, notesStrategy);

		// Todo: configure this
		InKeyNoteValueRange valueRange = new InKeyNoteValueRange();
		valueRange.setLowestRootNoteInKeyAsInt(0);
		valueRange.setHighestRootNoteInKeyAsInt(0);
		notesStrategy.setNoteValueRange(valueRange);

		track.setTickRhythm(tickRhythm);
	}

	private void parsePatternConfig(String part) {
		int patternLength = parsePatternLength(part);
		boolean neverRepeats = parseNeverRepeats(part);
		tickRhythm.setNumberOfTicksInPhrase(patternLength);
		tickRhythm.setNeverRepeats(neverRepeats);
	}

	private void parseTrackConfig(String part) {
		trackName = parseTrackName(part);
		int value = parseTrackNote(part);
		NoteValue note = NoteValue.forInt(value);
		track = new RealtimeTrack(trackName, new Key(note, Scale.UNISON_SCALE));
	}

	public int parseTrackNote(String part) {
		String textToFind = "note ";
		return parseInt(part, textToFind);
	}

	public TickRhythm getTickRhythm() {
		return tickRhythm;
	}

	public boolean isTrackLine(String line) {
		return line.indexOf("channel") > -1;
	}

	public boolean isPatternLine(String line) {
		return line.indexOf("pattern") > -1;
	}

	public boolean isNotesLine(String line) {
		return line.indexOf("with notes") > -1;
	}

	public boolean isRhythmLine(String rhythmLine) {
		return rhythmLine.indexOf("with rhythm") > -1;
	}

	public String getTrackName() {
		return trackName;
	}

	public int parseStartingOn(String rhythmLine) {
		String textToFind = "starting on ";
		return parseInt(rhythmLine, textToFind);
	}

	public RealtimeTrack getTrack() {
		return track;
	}
}


/*
		 public static class DSL {

			 public static DSL addTrackAs(String trackName) {
				 return new DSL();
			 }

			 public static void hrm() {
				 addTrackAs("kick").repeatEvery(2048)
						 .withNotes(minVelocity(100).maxVelocity(127).as("hard"))
						 .withRythm(bias(.4).onEvery(64).using("soft"));

				 // add track as "kick",
				 // repeat every 2048,
				 // with notes min velocity 100 max velocity 127 as hard,
				 // with notes min velocity 20 max velocity 60 as soft,
				 // with rhythm bias .4 on every 64 using soft notes,
				 // with rhythm bias .9 every 256 using hard notes
			 }

			 public DSL repeatEvery(int i) {

				 return this;
			 }
		 }
		 */

