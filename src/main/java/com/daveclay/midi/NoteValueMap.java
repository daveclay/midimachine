package com.daveclay.midi;

import java.util.HashMap;
import java.util.Map;

/**
 */
public class NoteValueMap {

	private static Map<NoteValueType, Map<Integer, NoteValue>> typeToIntToNoteValue = new HashMap<NoteValueType, Map<Integer, NoteValue>>();

	static void add(NoteValue noteValue) {
		NoteValueType type = noteValue.getNoteValueType();
		Map<Integer, NoteValue> intToNoteValue = typeToIntToNoteValue.get(type);
		if (intToNoteValue == null) {
			intToNoteValue = new HashMap<Integer, NoteValue>();
			typeToIntToNoteValue.put(type, intToNoteValue);
		}

		intToNoteValue.put(noteValue.getIntValue(), noteValue);
	}

	public static NoteValue get(int intValue) {
		final Map<Integer, NoteValue> noteValueMap = typeToIntToNoteValue.get(NoteValueType.NOTE);
		if (noteValueMap != null) {
			return noteValueMap.get(intValue);
		}

		return null;
	}
}
