package com.daveclay.midi.realtime;

/**
*/
public class PositionNoteStrategy {
	
	private double probabilityOfNote;
	private RealtimeNotesStrategy realtimeNotesStrategy;

	public double getProbabilityOfNote() {
		return probabilityOfNote;
	}

	public void setProbabilityOfNote(double likelyhoodOfNote) {
		this.probabilityOfNote = likelyhoodOfNote;
	}

	public RealtimeNotesStrategy getRuntimeNotesStrategy() {
		return realtimeNotesStrategy;
	}

	public void setRuntimeNotesStrategy(RealtimeNotesStrategy realtimeNotesStrategy) {
		this.realtimeNotesStrategy = realtimeNotesStrategy;
	}

	public PositionNoteStrategy copy() {
		PositionNoteStrategy copy = new PositionNoteStrategy();

		// Note: this class is subclassed, so call the methods, not the instance fields.
		copy.probabilityOfNote = getProbabilityOfNote();
		RealtimeNotesStrategy realtimeNotesStrategy = getRuntimeNotesStrategy();
		if (realtimeNotesStrategy != null) {
			copy.realtimeNotesStrategy = realtimeNotesStrategy.copy();
		}
		return copy;
	}
}
