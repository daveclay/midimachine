package com.daveclay.midi;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
*/
public class Key {

	private final NoteValue rootNote;
	private final Scale scale;
	private final Set<String> noteNamesInKey;

	public Key(final NoteValue rootNote, Scale scale) {
		this.rootNote = rootNote;
		this.scale = scale;
		this.noteNamesInKey = new HashSet<String>();
		noteNamesInKey.add(rootNote.getSimpleNote());
		scale.forEachInterval(new Scale.IntervalFunctor() {
			@Override
			public void interval(Interval interval) {
				NoteValue next = interval.noteAbove(rootNote);
				noteNamesInKey.add(next.getSimpleNote());
			}
		});
	}

	public NoteValue getRootNote() {
		return rootNote;
	}

	public Scale getScale() {
		return scale;
	}

	public List<NoteValue> getNotesBetween(NoteValue startNoteInclusive, NoteValue endNoteExclusive) {
		NoteValue currentNote = getNearestRootNoteAbove(startNoteInclusive);
		List<NoteValue> notes = new ArrayList<NoteValue>();
		try {
			while (currentNote != null && currentNote.isLower(endNoteExclusive)) {
				if (isNoteInKey(currentNote)) {
					notes.add(currentNote);
				}
				currentNote = currentNote.halfStepUp();
			}
		} catch (Throwable uh) {
			uh.printStackTrace();
		}

		return notes;
	}

	boolean isNoteInKey(NoteValue currentNote) {
		return noteNamesInKey.contains(currentNote.getSimpleNote());
	}

	private NoteValue getNearestRootNoteAbove(NoteValue startNote) {
		if (startNote == rootNote) {
			return rootNote;
		}

		while ( ! startNote.isOctave(rootNote)) {
			startNote = startNote.halfStepUp();
		}

		return startNote;
	}

	public List<NoteValue> getNotesForOctaves(int numberOfOctaves) {
		List<NoteValue> all = new ArrayList<NoteValue>();
		NoteValue start = getRootNote();
		for (int i = 0; i < numberOfOctaves; i++) {
			NoteValue octave = Interval.OCTAVE.noteAbove(start);
			all.addAll(getNotesBetween(start, octave));
			start = octave;
		}

		return all;
	}

	@Deprecated
	public int[] getNotesForOctavesAsInt(int numberOfOctaves) {
		List<NoteValue> all = getNotesForOctaves(numberOfOctaves);
		int[] yoyoyoMTVRaps = new int[all.size()];
		for (int i = 0; i < yoyoyoMTVRaps.length; i++) {
			yoyoyoMTVRaps[i] = all.get(i).getIntValue();
		}

		return yoyoyoMTVRaps;
	}

	public NoteValue getNoteForInterval(Interval interval) {
		return interval.noteAbove(rootNote);
	}
}
