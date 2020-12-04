package com.daveclay.midi.realtime;

import com.daveclay.midi.Key;
import com.daveclay.midi.random.RandyTheRandom;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * There two ways in which to repeat a loop. The first is to updateTrack the specific notes and positions that WERE triggered.
 * This is the repeated notes type loop repeating.
 *
 * The second is to updateTrack this entire TickRhythm instance's state as a named thing.
 *
 * There is a further question of building songs: if I want to play a particular repeated-notes type loop, can I
 * resurrect the TickRhythm that created it? A user might say "ok, play Verse B repeated notes. Ok, now stop repeating
 * those notes and create a new setSize of notes with that same TickRhythm config."
 *
 */
public class TickRhythm {

	private enum ResetType {
		RESET_ON_NEXT_TICK,
		RESET_ON_NEXT_REPEAT
	}

	private enum RepeatType {
		REPEAT_ON_NEXT_TICK,
		REPEAT_ON_NEXT_LOOP
	}

	private String name;
	private int numberOfTicksInPhrase;
	private Map<Integer, PositionNoteStrategy> tickIndexToStrategy;
	private RepeatedNotesLoop currentRepeatedNotesLoop = new RepeatedNotesLoop();
	private RepeatType repeatType;
	private ResetType resetType = ResetType.RESET_ON_NEXT_TICK; // let it reset the first tick

	public void setPositionNoteStrategy(int tickIndex, PositionNoteStrategy positionNoteStrategy) {
		if (positionNoteStrategy == null) {
			// Todo: this isn't clear that null removes the map.. but only this class should care that the map even exists.
			// I dunno, it just bit me in the ass and I didn't like it.
			tickIndexToStrategy.remove(tickIndex);
		} else {
			tickIndexToStrategy.put(tickIndex, positionNoteStrategy);
		}
	}

	public void setNumberOfTicksInPhrase(int numberOfTicksInPhrase) {
		// todo: this should be done on a tick command
		this.numberOfTicksInPhrase = numberOfTicksInPhrase;
		// Todo: odd place to initialize this.
		this.tickIndexToStrategy = new HashMap<Integer, PositionNoteStrategy>();
	}

	public void resetLoopOnNextRepeat() {
		this.resetType = ResetType.RESET_ON_NEXT_REPEAT;
	}

	public void resetLoopOnNextTick() {
		this.resetType = ResetType.RESET_ON_NEXT_TICK;
	}

	public void neverRepeat() {
		this.repeatType = null;
	}

	public void repeatImmediately() {
		this.repeatType = RepeatType.REPEAT_ON_NEXT_TICK;
	}

	public void repeatOnNextLoop() {
		this.repeatType = RepeatType.REPEAT_ON_NEXT_LOOP;
	}

	@Deprecated
	public void setNeverRepeats(boolean neverRepeats) {
		if (neverRepeats) {
			repeatType = null;
		} else {
			repeatType = RepeatType.REPEAT_ON_NEXT_TICK;
		}
	}

	public Notes getNotesForTick(long tick, Key key) {
		Notes notes;

		initIfNeeded();

		int index = getIndexForTick(tick);

		resetIfRequested(index);

		if (isUsingRepeatedNotes(index)) {
			notes = currentRepeatedNotesLoop.getRepeatedNotesAtTick(index);
		} else {
			notes = createNewNotes(index, key);
		}

		return notes;
	}

	private void initIfNeeded() {
		if (this.tickIndexToStrategy == null) {
			resetLikelyhookOfNotesAtPosition();
		}
	}

	private Notes createNewNotes(int tickIndex, Key key) {
		Notes notes;
		PositionNoteStrategy strategy = getPositionNoteStrategyForTickIndex(tickIndex);
		if (strategy != null) {
			notes = getNotesForStrategy(strategy, key);
		} else {
			notes = null;
		}

		currentRepeatedNotesLoop.addNotes(notes, tickIndex);
		return notes;
	}

	private static Notes getNotesForStrategy(PositionNoteStrategy strategy, Key key) {
		double likelyhood = strategy.getProbabilityOfNote();
		boolean shouldMakeNote = likelyhood > RandyTheRandom.nextDouble();
		if (shouldMakeNote) {
			RealtimeNotesStrategy notesStrategy = strategy.getRuntimeNotesStrategy();
			if (notesStrategy == null) {
				// Todo: note used now, will we ever with the gui?
				// notesStrategy = this.defaultRuntimeNotesStrategy;
			}

			if (notesStrategy != null) {
				return notesStrategy.getNotes(key);
			}
		}

		return null;
	}

	private void resetIfRequested(int index) {
		if (isReset(index)) {
			resetLoop();
		}
	}

	private boolean isUsingRepeatedNotes(int index) {
		if (! isRepeat(index)) {
			return false;
		}

		return currentRepeatedNotesLoop.containsNotesForIndex(index);
	}

	private void resetLoop() {
		resetLikelyhookOfNotesAtPosition();
		currentRepeatedNotesLoop.clearRepeatedNotes();
		resetType = null;
	}

	private boolean isRepeat(int index) {
		return repeatType == RepeatType.REPEAT_ON_NEXT_TICK ||
				(repeatType == RepeatType.REPEAT_ON_NEXT_LOOP && index == 0);
	}

	private boolean isReset(int index) {
		return resetType == ResetType.RESET_ON_NEXT_TICK ||
				(resetType == ResetType.RESET_ON_NEXT_REPEAT && index == 0);
	}

	private void resetLikelyhookOfNotesAtPosition() {
		this.currentRepeatedNotesLoop.reset(this.numberOfTicksInPhrase);
	}

	public Map<Integer, PositionNoteStrategy> getTickIndexToStrategy() {
		return Collections.unmodifiableMap(tickIndexToStrategy);
	}

	public PositionNoteStrategy getPositionNoteStrategyForTickIndex(int tickIndex) {
		return this.tickIndexToStrategy.get(tickIndex);
	}

	private int getIndexForTick(long tick) {
		int index = (int) (tick % this.numberOfTicksInPhrase);
		return index;
	}

	public int getNumberOfTicksInPhrase() {
		return this.numberOfTicksInPhrase;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public RepeatedNotesLoop getCurrentRepeatedNotesLoop() {
		return currentRepeatedNotesLoop;
	}

	public void setCurrentRepeatedNotesLoop(RepeatedNotesLoop currentRepeatedNotesLoop) {
		this.currentRepeatedNotesLoop = currentRepeatedNotesLoop;
	}

	public RepeatType getRepeatType() {
		return repeatType;
	}

	public void setRepeatType(RepeatType repeatType) {
		this.repeatType = repeatType;
	}

	public ResetType getResetType() {
		return resetType;
	}

	public void setResetType(ResetType resetType) {
		this.resetType = resetType;
	}

	// convenience methods programatic configuration settings

	public void setBiasForEveryNthTick(double probability, int everyNth, int startAt, RealtimeNotesStrategy realtimeNotesStrategy) {
		for (int i = 0; i < numberOfTicksInPhrase; i++) {
			if (i % everyNth == startAt) {
				PositionNoteStrategy positionNoteStrategy = new PositionNoteStrategy();
				positionNoteStrategy.setProbabilityOfNote(probability);
				positionNoteStrategy.setRuntimeNotesStrategy(realtimeNotesStrategy);
				setPositionNoteStrategy(i, positionNoteStrategy);
			}
		}
	}

	TickRhythm copy() {
		TickRhythm copy = new TickRhythm();
		copy.name = name;
		copy.numberOfTicksInPhrase = numberOfTicksInPhrase;
		copy.tickIndexToStrategy = copy(this.tickIndexToStrategy);
		copy.currentRepeatedNotesLoop = currentRepeatedNotesLoop.copy();
		copy.repeatType = repeatType;
		copy.resetType = resetType;
		return copy;
	}

	private static Map<Integer, PositionNoteStrategy> copy(Map<Integer, PositionNoteStrategy> tickIndexToStrategy) {
		Map<PositionNoteStrategy, PositionNoteStrategy> copiedInstances = new HashMap<PositionNoteStrategy, PositionNoteStrategy>();
		Map<Integer, PositionNoteStrategy> copy = new HashMap<Integer, PositionNoteStrategy>();
		for (Map.Entry<Integer, PositionNoteStrategy> entry : tickIndexToStrategy.entrySet()) {
			Integer tickIndex = entry.getKey();
			PositionNoteStrategy positionNoteStrategy = entry.getValue();

			PositionNoteStrategy copyOfStrategy = copiedInstances.get(positionNoteStrategy);
			if (copyOfStrategy == null) {
				copyOfStrategy = positionNoteStrategy.copy();
				copiedInstances.put(positionNoteStrategy, copyOfStrategy);
			}

			copy.put(tickIndex, copyOfStrategy);
		}

		return copy;
	}
}
