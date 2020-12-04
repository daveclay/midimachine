package com.daveclay.midi.realtime.ui;

import com.daveclay.midi.Key;
import com.daveclay.midi.NoteValue;
import com.daveclay.midi.Scale;

import javax.swing.*;
import javax.swing.border.EtchedBorder;

import static com.daveclay.midi.realtime.ui.util.Position.position;
import static com.daveclay.midi.realtime.ui.util.Size.*;

/**
*/
public class KeyPanel extends JPanel {

	private NotePanel notePanel;
	private ScalePanel scalePanel;

	public KeyPanel() {
		buildGUI();
	}

	private void buildGUI() {
		setBorder(BorderFactory.createEtchedBorder(EtchedBorder.LOWERED));

		notePanel = new NotePanel();
		scalePanel = new ScalePanel();

		position(notePanel).atOrigin().in(this);
		position(scalePanel).below(notePanel).in(this);
		fit(notePanel, scalePanel).in(this);
	}

	public Key getKey() {
		NoteValue rootNote = notePanel.getNoteValue();
		Scale scale = scalePanel.getScale();
		return new Key(rootNote, scale);
	}

	public void select(Key key) {
		notePanel.select(key.getRootNote());
		scalePanel.select(key.getScale());
	}
}
