package com.daveclay.midi.realtime.ui;

import com.daveclay.midi.realtime.PositionNoteStrategy;
import com.daveclay.midi.realtime.ui.knob.DKnob;
import static com.daveclay.midi.realtime.ui.util.Size.*;

import javax.swing.*;
import javax.swing.border.EtchedBorder;

import static com.daveclay.midi.realtime.ui.util.Position.position;

/**
*/
public class PossibilityKnobPanel extends JPanel {
	public static final int WIDTH = 90;
	public static final int HEIGHT = MinMaxPanel.HEIGHT;

	private DKnob possibilityKnob;

	public PossibilityKnobPanel() {
		buildGUI();
	}

	public double getPossibilityValue() {
		return possibilityKnob.getInternalValue();
	}

	private void buildGUI() {
		setBorder(BorderFactory.createEtchedBorder(EtchedBorder.LOWERED));
		this.possibilityKnob = new DKnob();
		add(possibilityKnob);

		JLabel label = new JLabel("\u210f");

		sizeComponent(possibilityKnob).to(80, 80);
		sizeComponent(this).to(PossibilityKnobPanel.WIDTH, PossibilityKnobPanel.HEIGHT);

		position(possibilityKnob).atOrigin().in(this);
		position(label).below(possibilityKnob).in(this);
	}

	public void updateForStrategy(PositionNoteStrategy positionNoteStrategy) {
		possibilityKnob.setInternalValue(positionNoteStrategy.getProbabilityOfNote());
	}
}
