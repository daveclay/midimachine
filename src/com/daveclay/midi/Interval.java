package com.daveclay.midi;

public enum Interval {

	UNISON("UNISON",0),
	MINOR_SECOND("MINOR_SECOND",1),
	MAJOR_SECOND("MAJOR_SECOND",2),
	MINOR_THIRD("MINOR_THIRD",3),
	MAJOR_THIRD("MAJOR_THIRD",4),
	FOURTH("FOURTH",5),
	FLAT_FIFTH("FLAT_FIFTH",6),
	FIFTH("FIFTH",7),
	MINOR_SIXTH("MINOR_SIXTH",8),
	MAJOR_SIXTH("MAJOR_SIXTH",9),
	MINOR_SEVENTH("MINOR_SEVENTH",10),
	MAJOR_SEVENTH("MAJOR_SEVENTH",11),
	OCTAVE("OCTAVE",12),
	ROOT("ROOT",UNISON.asInt()),
	DIMINISHED_FIFTH("DIMINISHED_FIFTH",FLAT_FIFTH.asInt()),
	TRITONE("TRITONE",FLAT_FIFTH.asInt()),
	DIMINISHED_SEVENTH("DIMINISHED_SEVENTH",MAJOR_SIXTH.asInt());

	private String name;
	private int intValue;

	private Interval(String name, int intValue) {
		this.name = name;
		this.intValue = intValue;
	}

	public NoteValue getNoteValue(NoteValue rootNoteValue) {
		int note = rootNoteValue.getIntValue();
		int newNote = note + intValue;
		return NoteValue.forInt(newNote);
	}

	public String getName() {
		return name;
	}

	public int asInt() {
		return intValue;
	}

	public NoteValue noteAbove(NoteValue note) {
		return NoteValue.forInt(note.getIntValue() + this.asInt());
	}

	public NoteValue noteBelow(NoteValue note) {
		// this isn't right, duh.
		return NoteValue.forInt(note.getIntValue() - this.asInt());
	}
}


