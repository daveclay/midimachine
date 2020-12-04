package com.daveclay.midi;

/**
*/
public enum NoteValueRangeType {

	IN_KEY("In Key") {
		@Override
		public NoteValueRange getNoteValueRange() {
			return new InKeyNoteValueRange();
		}
	},
	ROOT_NOTE_ONLY("Root Note Only") {
		@Override
		public NoteValueRange getNoteValueRange() {
			return new RootNoteValueRange();
		}
	},
	CUSTOM("Custom") {
		@Override
		public NoteValueRange getNoteValueRange() {
			return new CustomNoteValueRange();
		}
	};

	private String name;

	private NoteValueRangeType(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}

	public String toString() {
		return this.name;
	}

	public abstract NoteValueRange getNoteValueRange();
}
