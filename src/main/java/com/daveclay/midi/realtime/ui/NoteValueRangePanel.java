package com.daveclay.midi.realtime.ui;

import com.daveclay.midi.NoteValueRange;

public interface NoteValueRangePanel {
	NoteValueRange getNoteValueRange();
	public void setVisible(boolean visible);
}
