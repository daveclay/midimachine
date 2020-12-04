package com.daveclay.midi.random;

/**
*/
public class RandomSequenceRhythmStrategy extends AbstractSequenceRhythmStrategy {

	private PositionNoteStrategy[] likelyhoodOfNoteAtPosition = new PositionNoteStrategy[16];

	/**
	 * Depends on song setup - realtime songs go by ticks, sequence can be arbitrary. You should probably
	 * go ahead and call this method first, huh.
	 * @param length sets the array of bias positions
	 */
	public void setPhraseLength(int length) {
		likelyhoodOfNoteAtPosition = new PositionNoteStrategy[length];
	}

	public static class PositionNoteStrategy {
		private double likelyhoodOfNote;
		private NoteEventStrategy noteEventStrategy;

		public double getLikelyhoodOfNote() {
			return likelyhoodOfNote;
		}

		public void setLikelyhoodOfNote(double likelyhoodOfNote) {
			this.likelyhoodOfNote = likelyhoodOfNote;
		}

		public NoteEventStrategy getNoteEventStrategy() {
			return noteEventStrategy;
		}

		public void setNoteEventStrategy(NoteEventStrategy noteEventStrategy) {
			this.noteEventStrategy = noteEventStrategy;
		}
	}

	@Override
	protected NoteEventStrategy getNoteEventStrategy(RandomInstrument randomInstrument, int positionInMeasure) {
		PositionNoteStrategy positionNoteStrategy = likelyhoodOfNoteAtPosition[positionInMeasure];
		NoteEventStrategy noteEventStrategy = positionNoteStrategy.getNoteEventStrategy();
		if (noteEventStrategy == null) {
			noteEventStrategy = randomInstrument.getNoteEventStrategy();
		}
		return noteEventStrategy;
	}

	@Override
	protected boolean shouldMakeNote(int position) {
		PositionNoteStrategy strategy = likelyhoodOfNoteAtPosition[position];
		double likelyhood = strategy.getLikelyhoodOfNote();
		return likelyhood > RandyTheRandom.nextDouble();
	}

	public void setLikelyhoodOfNoteAtPosition(int position, double likelyhood, NoteEventStrategy noteEventStrategy) {
		PositionNoteStrategy positionNoteStrategy = new PositionNoteStrategy();
		positionNoteStrategy.setLikelyhoodOfNote(likelyhood);
		positionNoteStrategy.setNoteEventStrategy(noteEventStrategy);
		likelyhoodOfNoteAtPosition[position] = positionNoteStrategy;
	}

	public void setLikelyhoodOfNoteAtPosition(int position, double likelyhood) {
		setLikelyhoodOfNoteAtPosition(position, likelyhood, null);
	}

	public void setLikelyhoodOfEvent(double likelyhood) {
		for (int i = 0; i < likelyhoodOfNoteAtPosition.length; i++) {
			setLikelyhoodOfNoteAtPosition(i, likelyhood);
		}
	}
}
