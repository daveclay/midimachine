package com.daveclay.midi.realtime.ui;

import com.daveclay.midi.realtime.RealtimeSequencer;
import com.daveclay.midi.realtime.ui.util.Size;
import com.daveclay.midi.realtime.ui.util.Util;

import javax.sound.midi.InvalidMidiDataException;
import javax.swing.*;
import javax.swing.border.EtchedBorder;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

import static com.daveclay.midi.realtime.ui.util.Position.position;
import static com.daveclay.midi.realtime.ui.util.Size.*;

/**
*/
public class TransportPanel extends JPanel {

	public static final int BUTTON_WIDTH = 60;
	public static final int BUTTON_HEIGHT = 40;

	private RealtimeSequencer sequencer;
	private MainWindow mainWindow;
	private TransportButtonPanel pauseButton;
	private TransportButtonPanel playButton;
	private TransportButtonPanel stopButton;
	private JTextField bpmField;

	public TransportPanel(RealtimeSequencer sequencer, MainWindow mainWindow) {
		this.sequencer = sequencer;
		this.mainWindow = mainWindow;
		buildGUI();
	}

	private void buildGUI() {
		setBorder(BorderFactory.createEtchedBorder(EtchedBorder.LOWERED));
		buildPauseButton();
		buildPlayButton();
		buildStopButton();
		buildBPMField();

		positionThings();
	}

	private void positionThings() {
		position(pauseButton).atOrigin().in(this);
		position(playButton).toTheRightOf(pauseButton).withMargin(2).in(this);
		position(stopButton).toTheRightOf(playButton).withMargin(2).in(this);
		position(bpmField).toTheRightOf(stopButton).withMargin(2).in(this);
		fit(pauseButton, playButton, stopButton, bpmField).in(this);
	}
	
	private void buildStopButton() {
		stopButton = new StopButtonPanel();
		stopButton.setLayout(new FlowLayout(FlowLayout.CENTER));
		JLabel stopLabel = new JLabel("[ ]");
		stopButton.add(stopLabel);
		sizeComponent(stopButton).to(BUTTON_WIDTH, BUTTON_HEIGHT);
	}

	private void buildPlayButton() {
		playButton = new PlayButtonPanel();
		playButton.setLayout(new FlowLayout(FlowLayout.CENTER));
		JLabel playLabel = new JLabel(">");
		playButton.add(playLabel);
		sizeComponent(playButton).to(BUTTON_WIDTH, BUTTON_HEIGHT);
	}

	private void buildPauseButton() {
		this.pauseButton = new PauseButtonPanel();
		pauseButton.setLayout(new FlowLayout(FlowLayout.CENTER));
		JLabel pauseLabel = new JLabel("||");
		pauseButton.add(pauseLabel);
		sizeComponent(pauseButton).to(BUTTON_WIDTH, BUTTON_HEIGHT);
	}

	private void buildBPMField() {
		bpmField = new JTextField("120");
		sizeComponent(bpmField).to(60, 30);

		bpmField.addKeyListener(new KeyAdapter() {
			@Override
			public void keyTyped(KeyEvent keyEvent) {
				if (keyEvent.getKeyChar() == 10) {
					setBPM();
				}
			}
		});
	}

	private void setBPM() {
		int bpm = getBPM();
		try {
			getSequencer().play(bpm);
		} catch (InvalidMidiDataException e) {
			e.printStackTrace();
		}
	}

	private RealtimeSequencer getSequencer() {
		return mainWindow.getSequencer();
	}

	public int getBPM() {
		String text = bpmField.getText();
		int bpm;
		try {
			bpm = Integer.parseInt(text);
		} catch (Exception err) {
			bpm = getSequencer().getBPM();
			bpmField.setText(Integer.toString(bpm));
		}
		return bpm;
	}

	private class TransportButtonPanel extends ButtonPanel {

		@Override
		public void mouseEnteredWithButtonDown() {

		}

		@Override
		public void selectorPanelPressed() {
			setButtonPressedUI();
			repaint();
		}

		@Override
		public void deselectSelectorPanel() {
			setButtonSelectStateUI();
			repaint();
		}

		@Override
		public void selectorPanelSelected() {
			setButtonSelectStateUI();
			repaint();
		}
	}

	private class StopButtonPanel extends TransportButtonPanel {
		@Override
		public void selectorPanelSelected() {
			sequencer.stop();
			pauseButton.deselectSelectorPanel();
			playButton.deselectSelectorPanel();
			super.selectorPanelSelected();
		}
	}

	private class PauseButtonPanel extends TransportButtonPanel {

		@Override
		public void selectorPanelSelected() {
			sequencer.pause();
			playButton.deselectSelectorPanel();
			stopButton.deselectSelectorPanel();
			super.selectorPanelSelected();
		}
	}

	private class PlayButtonPanel extends TransportButtonPanel {
		@Override
		public void selectorPanelSelected() {
			try {
				sequencer.play(getBPM());
			} catch (InvalidMidiDataException e) {
				e.printStackTrace();
			}
			pauseButton.deselectSelectorPanel();
			stopButton.deselectSelectorPanel();
			super.selectorPanelSelected();
		}
	}

}
