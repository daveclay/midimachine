package com.daveclay.midi.realtime.ui;

import com.daveclay.midi.Scale;
import com.daveclay.midi.realtime.ui.util.EnumSelectorPanel;

/**
*/
public class ScalePanel extends EnumSelectorPanel<Scale> {

	public ScalePanel() {
		super(Scale.class);
	}

	public Scale getScale() {
		return getSelectedValue();
	}
}
