package com.daveclay.midi.realtime.ui;

import com.daveclay.midi.NoteValueRange;
import com.daveclay.midi.RootNoteValueRange;

import javax.swing.*;

/**
 */
public class RootNoteValueRangePanel extends JPanel implements NoteValueRangePanel {

	private RootNoteValueRange rootNoteValueRange = new RootNoteValueRange();

	@Override
	public NoteValueRange getNoteValueRange() {
		return rootNoteValueRange;
	}

	@Override
	public String toString() {
		return "Root Note";
	}

	public void updateForRange(NoteValueRange noteValueRange) {
		// No state, yay.
	}
}
