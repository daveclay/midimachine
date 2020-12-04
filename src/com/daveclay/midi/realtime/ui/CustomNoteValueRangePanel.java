package com.daveclay.midi.realtime.ui;

import com.daveclay.midi.NoteValueRange;

import javax.swing.*;

/**
 */
public class CustomNoteValueRangePanel extends JPanel implements NoteValueRangePanel {

	@Override
	public NoteValueRange getNoteValueRange() {
		return null;
	}

	@Override
	public String toString() {
		return "Custom";
	}

	public void updateForRange(NoteValueRange noteValueRange) {
	}
}
