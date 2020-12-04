package com.daveclay.midi.realtime;

/**
*/
class RepeatedNotesLoop {

	private RepeatedNotes[] notesToBeRepeated;

	public void addNotes(Notes notes, int index) {
		RepeatedNotes repeatedNotes = new RepeatedNotes();
		repeatedNotes.notes = notes;
		this.notesToBeRepeated[index] = repeatedNotes;
	}

	public Notes getRepeatedNotesAtTick(int index) {
		Notes previousNotes = notesToBeRepeated[index].notes;
		return previousNotes;
	}

	public boolean containsNotesForIndex(int index) {
		return index >= notesToBeRepeated.length || // oops, you changed the length of the phrase, and the index is out of bounds...
				notesToBeRepeated[index] != null;
	}

	public void reset(int length) {
		this.notesToBeRepeated = new RepeatedNotes[length];
	}

	public void clearRepeatedNotes() {
		for (int i = 0; i < this.notesToBeRepeated.length; i++) {
			notesToBeRepeated[i] = null;
		}
	}

	public RepeatedNotesLoop copy() {
		RepeatedNotesLoop clone = new RepeatedNotesLoop();
		clone.notesToBeRepeated = copy(notesToBeRepeated);
		return clone;
	}

	private RepeatedNotes[] copy(RepeatedNotes[] notesToBeRepeated) {
		RepeatedNotes[] clone = new RepeatedNotes[notesToBeRepeated.length];
		for (int i = 0; i < notesToBeRepeated.length; i++) {
			RepeatedNotes repeatedNotes = notesToBeRepeated[i];
			if (repeatedNotes != null) {
				clone[i] = repeatedNotes.copy();
			}
		}

		return clone;
	}

	private static class RepeatedNotes {
		public Notes notes;

		protected RepeatedNotes copy() {
			RepeatedNotes copy = new RepeatedNotes();
			if (notes != null) {
				copy.notes = notes.copy();
			}
			return copy;
		}
	}
}
