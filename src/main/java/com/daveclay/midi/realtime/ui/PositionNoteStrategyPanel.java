package com.daveclay.midi.realtime.ui;

import com.daveclay.midi.realtime.PositionNoteStrategy;
import com.daveclay.midi.realtime.RealtimeNotesStrategy;
import com.daveclay.midi.realtime.RealtimeTrack;

import javax.swing.*;
import java.awt.*;

import static com.daveclay.midi.realtime.ui.util.Position.position;
import static com.daveclay.midi.realtime.ui.util.Size.fit;

/**
*/
public class PositionNoteStrategyPanel extends JPanel {

	private RealtimeTrack track;
	private Color color;
	private RandomNoteStrategyPanel randomNoteStrategyPanel;
	private PossibilityKnobPanel possibilityPanel;

	public PositionNoteStrategyPanel(RealtimeTrack track, Color color) {
		this.track = track;
		this.color = color;
		track.getTickRhythm().setNeverRepeats(true);
		buildGUI();
	}

	public void updateForStrategy(PositionNoteStrategy positionNoteStrategy) {
		possibilityPanel.updateForStrategy(positionNoteStrategy);
		randomNoteStrategyPanel.updateForStrategy(positionNoteStrategy);
	}

	public Color getColor() {
		return color;
	}

	public PossibilityKnobPanel getPossibilityKnobPanel() {
		return possibilityPanel;
	}

	public RandomNoteStrategyPanel getRandomNoteStrategyPanel() {
		return randomNoteStrategyPanel;
	}

	public PositionNoteStrategy asPositionNoteStrategy() {
		return positionNoteStrategy;
	}

	private void buildGUI() {
		setBackground(color);

		possibilityPanel = new PossibilityKnobPanel();
		randomNoteStrategyPanel = new RandomNoteStrategyPanel(track);

		position(possibilityPanel).at(5, 5).in(this);
		position(randomNoteStrategyPanel).toTheRightOf(possibilityPanel).in(this);
		fit(possibilityPanel, randomNoteStrategyPanel).withMargin(5).in(this);
	}

	/**
	 * A PositionNoteStrategy instance that represents this PositionNoteStrategyPanel instance's configuration.
	 */
	private PositionNoteStrategy positionNoteStrategy = new PositionNoteStrategy() {

		@Override
		public double getProbabilityOfNote() {
			return getPossibilityKnobPanel().getPossibilityValue();
		}

		@Override
		public RealtimeNotesStrategy getRuntimeNotesStrategy() {
			return getRandomNoteStrategyPanel().asRandomNoteStrategy();
		}
	};
}
