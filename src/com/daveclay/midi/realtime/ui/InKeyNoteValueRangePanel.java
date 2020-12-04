package com.daveclay.midi.realtime.ui;

import com.daveclay.midi.InKeyNoteValueRange;
import com.daveclay.midi.NoteValue;
import com.daveclay.midi.NoteValueRange;

import javax.swing.*;

import static com.daveclay.midi.realtime.ui.util.Position.position;
import static com.daveclay.midi.realtime.ui.util.Size.*;

/**
 */
public class InKeyNoteValueRangePanel extends JPanel implements NoteValueRangePanel {

	private NotePanel lowestNote;
	private NotePanel highestNote;
	private InKeyNoteValueRange inKeyNoteValueRange;

	public InKeyNoteValueRangePanel() {
		inKeyNoteValueRange = new InKeyNoteValueRange();
		buildGUI();
	}

	private void buildGUI() {
		JPanel highNoteContainer = new JPanel();
		JPanel lowNoteContainer = new JPanel();
		lowestNote = createShit("Lowest", lowNoteContainer);
		lowestNote.select(NoteValue.C0);
		highestNote = createShit("Highest", highNoteContainer);
		highestNote.select(NoteValue.C10);

		position(lowNoteContainer).atOrigin().in(this);
		position(highNoteContainer).below(lowNoteContainer).in(this);
		fit(lowNoteContainer, highNoteContainer).in(this);
	}

	private NotePanel createShit(String name, JPanel noteContainer) {
		NotePanel notePanel = new NotePanel();
		JLabel label = new JLabel(name);
		position(label).atOrigin().in(noteContainer);
		position(notePanel).toTheRightOf(label).in(noteContainer);
		fit(label, notePanel).in(noteContainer);
		return notePanel;
	}

	@Override
	public NoteValueRange getNoteValueRange() {
		// Woops, if I'm typing in a note, it's not "selected", and might be C0.
		inKeyNoteValueRange.setLowestNote(lowestNote.getNoteValue());
		inKeyNoteValueRange.setHighestNote(highestNote.getNoteValue());
		return inKeyNoteValueRange;
	}

	@Override
	public String toString() {
		return "In Key";
	}

	public void updateForRange(InKeyNoteValueRange noteValueRange) {
		lowestNote.select(noteValueRange.getLowestNote());
		highestNote.select(noteValueRange.getHighestNote());
	}
}
