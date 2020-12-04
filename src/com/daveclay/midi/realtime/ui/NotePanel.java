package com.daveclay.midi.realtime.ui;

import com.daveclay.midi.NoteValue;
import com.daveclay.midi.realtime.ui.util.EnumSelectorPanel;

/**
*/
public class NotePanel extends EnumSelectorPanel<NoteValue> {

	public NotePanel() {
		super(NoteValue.class);
	}

	public NoteValue getNoteValue() {
		return super.getSelectedValue();
	}

}
