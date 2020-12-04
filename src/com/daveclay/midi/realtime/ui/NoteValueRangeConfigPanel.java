package com.daveclay.midi.realtime.ui;

import com.daveclay.midi.InKeyNoteValueRange;
import com.daveclay.midi.NoteValueRange;
import com.daveclay.midi.realtime.RealtimeTrack;

import javax.swing.*;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import static com.daveclay.midi.realtime.ui.util.Position.position;
import static com.daveclay.midi.realtime.ui.util.Size.fit;

/**
*/
public class NoteValueRangeConfigPanel extends JPanel {

	private RealtimeTrack track;
	private KeyPanel keyPanel;
	private JComboBox typeSelector;
	private InKeyNoteValueRangePanel inKeyNoteValueRangePanel;
	private CustomNoteValueRangePanel customNoteValueRangePanel;
	private RootNoteValueRangePanel rootNoteValueRangePanel;
	private NoteValueRangePanel currentNoteValueRangePanel;

	public NoteValueRangeConfigPanel(RealtimeTrack track) {
		this.track = track;
		buildGUI();
	}

	private void buildGUI() {

		inKeyNoteValueRangePanel = new InKeyNoteValueRangePanel();
		customNoteValueRangePanel = new CustomNoteValueRangePanel();
		rootNoteValueRangePanel = new RootNoteValueRangePanel();

		typeSelector = new JComboBox(new NoteValueRangePanel[] { inKeyNoteValueRangePanel, customNoteValueRangePanel, rootNoteValueRangePanel });
		typeSelector.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent actionEvent) {
				NoteValueRangeConfigPanel.this.updateSelection();
			}
		});

		keyPanel = new KeyPanel();
		keyPanel.select(track.getKey());

		currentNoteValueRangePanel = inKeyNoteValueRangePanel;
		position(typeSelector).atOrigin().in(this);
		position(inKeyNoteValueRangePanel).below(typeSelector).in(this);
		fit(typeSelector, inKeyNoteValueRangePanel).in(this);
	}

	public void updateForRange(NoteValueRange noteValueRange) {
		if (inKeyNoteValueRangePanel.getNoteValueRange().getClass().equals(noteValueRange.getClass())) {
			typeSelector.setSelectedItem(inKeyNoteValueRangePanel);
			inKeyNoteValueRangePanel.updateForRange((InKeyNoteValueRange) noteValueRange);
		} else if (customNoteValueRangePanel.getNoteValueRange().getClass().equals(noteValueRange.getClass())) {
			typeSelector.setSelectedItem(customNoteValueRangePanel);
			customNoteValueRangePanel.updateForRange(noteValueRange);
		} else if (rootNoteValueRangePanel.getNoteValueRange().getClass().equals(noteValueRange.getClass())) {
			typeSelector.setSelectedItem(rootNoteValueRangePanel);
			rootNoteValueRangePanel.updateForRange(noteValueRange);
		}
	}

	private void updateSelection() {
		NoteValueRangePanel noteValueRangePanel = getSelectedPanel();
		currentNoteValueRangePanel.setVisible(false);
		currentNoteValueRangePanel = noteValueRangePanel;
	 	currentNoteValueRangePanel.setVisible(true);
	}

	public NoteValueRange getNoteValueRange() {
		NoteValueRangePanel noteValueRangePanel = getSelectedPanel();
		return noteValueRangePanel.getNoteValueRange();
	}

	private NoteValueRangePanel getSelectedPanel() {
		NoteValueRangePanel noteValueRangePanel = (NoteValueRangePanel) typeSelector.getSelectedItem();
		return noteValueRangePanel;
	}
}
