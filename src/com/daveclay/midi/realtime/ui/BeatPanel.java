package com.daveclay.midi.realtime.ui;

import com.daveclay.midi.realtime.ui.util.Size;

import javax.swing.*;
import javax.swing.border.EtchedBorder;
import java.awt.*;

import static com.daveclay.midi.realtime.ui.util.Fonts.*;
import static com.daveclay.midi.realtime.ui.util.ColorUtils.*;
import static com.daveclay.midi.realtime.ui.util.Position.position;
import static com.daveclay.midi.realtime.ui.util.Size.fit;

/**
*/
public class BeatPanel extends JPanel {
	private static final int SELECTOR_PANEL_WIDTH = 30;
	private static final int SELECTOR_PANEL_HEIGHT = 50;
	private static final int ADD_REMOVE_PANEL_WIDTH = SELECTOR_PANEL_WIDTH;
	private static final int ADD_REMOVE_PANEL_HEIGHT = 20;

	private TrackPanel trackPanel; // Note: only used for events, not for getting midi logic info
	private ButtonPanel selectorPanel;
	private int patternPosition;
	private JLabel tickLabel;
	private JPanel indicatorPanel;
	private ButtonPanel addRemovePanel;
	private JLabel addRemoveLabel;

	// transient - this beat panel may be associated to different panels at different times, or to none.
	private PositionNoteStrategyPanel positionNoteStrategyPanel;

	public BeatPanel(int patternPosition, TrackPanel trackPanel) {
		this.trackPanel = trackPanel;
		this.patternPosition = patternPosition;
		buildGUI();
	}

	private void buildGUI() {
		setLayout(null);

		buildSelectorPanel();

		buildAddRemovePanel();

		add(selectorPanel);
		add(addRemovePanel);

		//Size.size(this, BEAT_PANEL_WIDTH, BEAT_PANEL_HEIGHT);
		position(selectorPanel).atOrigin().in(this);
		position(addRemovePanel).below(selectorPanel).withMargin(2).in(this);
		fit(selectorPanel, addRemovePanel).in(this);
	}

	private void buildAddRemovePanel() {
		buildAddRemoveLabel();

		addRemovePanel = new AddRemoveButtonPanel();
		addRemovePanel.setLayout(null);
		Size.size(addRemovePanel, ADD_REMOVE_PANEL_WIDTH, ADD_REMOVE_PANEL_HEIGHT);

		addRemovePanel.add(addRemoveLabel);
		position(addRemoveLabel).atOrigin().withMargin(2).in(addRemovePanel);
	}

	private void buildAddRemoveLabel() {
		addRemoveLabel = new JLabel("add");
		TinyFont.applyTo(addRemoveLabel);
		Size.size(addRemoveLabel, 16, 8);
	}

	private void buildSelectorPanel() {
		buildTickLabel();
		buildIndicatorPanel();

		selectorPanel = new SelectorButtonPanel();
		selectorPanel.add(indicatorPanel);
		selectorPanel.add(tickLabel);
		position(selectorPanel, indicatorPanel, 4, 4);
		position(selectorPanel, tickLabel, 20, 4);

		selectorPanel.setBorder(BorderFactory.createEtchedBorder(EtchedBorder.LOWERED));
		Size.size(selectorPanel, SELECTOR_PANEL_WIDTH, SELECTOR_PANEL_HEIGHT);
	}

	private void buildIndicatorPanel() {
		indicatorPanel = new JPanel();
		Size.size(indicatorPanel, 16, 5);
		indicatorPanel.setBorder(BorderFactory.createLineBorder(Color.BLACK));
		indicatorPanel.setBackground(Color.RED);
		indicatorPanel.setOpaque(true);
	}

	private void buildTickLabel() {
		tickLabel = new JLabel("" + (patternPosition + 1));
		TinyFont.applyTo(tickLabel);
	}

	public void selectSelectorPanel() {
		selectorPanel.mouseReleased();
	}

	public void selectAddRemovePanel() {
		addRemovePanel.mouseReleased();
	}

	public void beatAddedToPattern(PositionNoteStrategyPanel positionNoteStrategyPanel) {
		this.positionNoteStrategyPanel = positionNoteStrategyPanel;
		Color color = positionNoteStrategyPanel.getColor();

		selectorPanel.setSelectedColor(color);
		selectorPanel.setSelected(true);
		selectorPanel.setButtonSelectStateUI();

		setAddRemoveLabel("rem");
		addRemovePanel.setSelectedColor(color);
		addRemovePanel.setSelected(true);
		addRemovePanel.setButtonSelectStateUI();
	}

	public void beatRemovedFromPattern() {
		selectorPanel.setUnselectedColor(ButtonPanel.DEFAULT_UNSELECTED_COLOR);
		selectorPanel.setSelected(false);
		selectorPanel.setButtonSelectStateUI();

		setAddRemoveLabel("add");
		addRemovePanel.setUnselectedColor(ButtonPanel.DEFAULT_UNSELECTED_COLOR);
		addRemovePanel.setSelected(false);
		addRemovePanel.setButtonSelectStateUI();

		this.positionNoteStrategyPanel = null;
	}

	public void beatNotPartOfCurrentPattern() {
		if (positionNoteStrategyPanel != null) {
			Color color = positionNoteStrategyPanel.getColor();
			Color unselected = lighten(color).by(100);
			selectorPanel.setUnselectedColor(unselected);
		}

		selectorPanel.setSelected(false);
		selectorPanel.setButtonSelectStateUI();

		setAddRemoveLabel("rep");
		addRemovePanel.setUnselectedColor(ButtonPanel.DEFAULT_UNSELECTED_COLOR);
		addRemovePanel.setSelected(false);
		addRemovePanel.setButtonSelectStateUI();
	}

	private void setAddRemoveLabel(String label) {
		addRemoveLabel.setText(label);
	}

	public void onTick() {
		indicatorPanel.setBackground(Color.YELLOW);
	}

	public void offTick() {
		indicatorPanel.setBackground(Color.RED);
	}

	public void triggeredBeat() {
		indicatorPanel.setBackground(Color.GREEN);
	}

	private class AddRemoveButtonPanel extends ButtonPanel {

		@Override
		public void mouseEnteredWithButtonDown() {
		}

		@Override
		public void selectorPanelPressed() {
			setButtonPressedUI();
			selectorPanel.mousePressed();
			repaint();
		}

		@Override
		public void deselectSelectorPanel() {
			trackPanel.removePositionFromRhythmPanel(patternPosition);
		}

		@Override
		public void selectorPanelSelected() {
			trackPanel.addPositionToCurrentRhythmPanel(patternPosition);
		}
	}

	private class SelectorButtonPanel extends ButtonPanel {

		public SelectorButtonPanel() {
			setSelectOnly(true);
		}

		@Override
		public void mouseEnteredWithButtonDown() {

		}

		@Override
		public void selectorPanelPressed() {
			setButtonPressedUI();
			trackPanel.getPatternPanel().setCurrentMouseDownBeatPanel(BeatPanel.this);
		}

		@Override
		public void deselectSelectorPanel() {

		}

		@Override
		public void selectorPanelSelected() {
			trackPanel.selectRhythmPanel(patternPosition);
		}
	}
}
