package com.daveclay.midi.realtime.ui;

import com.daveclay.midi.Key;

import static com.daveclay.midi.realtime.ui.util.Size.*;
import static com.daveclay.midi.realtime.ui.util.Position.*;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class TrackConfigPanel extends JDialog {

	public static interface Listener {
		public void save(TrackConfig trackConfig);
	}

	private Listener listener;

	private KeyPanel keyPanel;
	private JTextField trackNameField;
	private JComboBox channelField;
	private JButton saveButton;
	private JTextField numberOfPositionsField;
	private JTextField numberOfTicksPerPosition;

	public TrackConfigPanel() {
		buildGUI();
	}

	public void forEditingTrack(TrackConfig track, Listener listener) {
		setTrackFields(track, listener);
		saveButton.setText("updateTrack");
	}

	private void setTrackFields(TrackConfig trackConfig, Listener listener) {
		this.listener = listener;

		trackNameField.setText(trackConfig.getName());
		keyPanel.select(trackConfig.getKey());
		channelField.setSelectedItem(trackConfig.getChannel());
		numberOfPositionsField.setText(Integer.toString(trackConfig.getNumberOfPositions()));
		numberOfTicksPerPosition.setText(Integer.toString(trackConfig.getNumberOfTicksPerPosition()));
	}

	public void forNewTrack(TrackConfig track, Listener listener) {

		setTrackFields(track, listener);
		saveButton.setText("add");
	}

	private void buildGUI() {
		trackNameField = new JTextField("", 15);
		keyPanel = new KeyPanel();
		numberOfPositionsField = new JTextField("", 4);
		numberOfTicksPerPosition = new JTextField("", 4);

		buildChannelField();
		buildSaveButton();
		positionThings();
	}

	private void positionThings() {
		position(trackNameField).atOrigin().in(this);
		position(channelField).toTheRightOf(trackNameField).in(this);
		position(keyPanel).below(trackNameField).withMargin(2).in(this);
		position(numberOfPositionsField).below(keyPanel).withMargin(2).in(this);
		position(numberOfTicksPerPosition).toTheRightOf(numberOfPositionsField).withMargin(2).in(this);
		position(saveButton).below(numberOfTicksPerPosition).in(this);

		fit(trackNameField, keyPanel, numberOfPositionsField, numberOfTicksPerPosition, saveButton, channelField)
				.withHeightMargin(32).withWidthMargin(2).in(this);
	}

	private JButton buildSaveButton() {
		saveButton = new JButton("");
		saveButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent actionEvent) {
				saveTrackConfig();
				TrackConfigPanel.this.setVisible(false);
			}
		});

		return saveButton;
	}

	private void buildChannelField() {
		Integer[] channels = new Integer[32];
		for (int i = 0; i < 32; i++) {
			channels[i] = i + 1;
		}

		channelField = new JComboBox(channels);
		channelField.setEditable(false);
	}

	private void saveTrackConfig() {
		String name = trackNameField.getText();
		Key key = keyPanel.getKey();
		Integer channel = (Integer) channelField.getSelectedItem() - 1;
		int numberOfPositions = parse(numberOfPositionsField, 32);
		int numberOfTicksPerPosition = parse(this.numberOfTicksPerPosition, 128);

		TrackConfig trackConfig = new TrackConfig(name, key, channel, numberOfPositions, numberOfTicksPerPosition);

		if (listener != null) {
			listener.save(trackConfig);
		}
	}

	private static int parse(JTextField field, int defaultValue) {
		String s = field.getText();
		try {
			return Integer.parseInt(s);
		} catch (Exception err) {
			return defaultValue;
		}
	}
}
