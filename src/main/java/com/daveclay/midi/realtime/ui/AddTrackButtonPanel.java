package com.daveclay.midi.realtime.ui;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import static com.daveclay.midi.realtime.ui.util.Position.position;
import static com.daveclay.midi.realtime.ui.util.Size.*;

/**
*/
public class AddTrackButtonPanel extends JPanel implements ActionListener {

	private MainWindow mainWindow;

	public AddTrackButtonPanel(MainWindow mainWindow) {
		this.mainWindow = mainWindow;
		buildGUI();
	}

	private void buildGUI() {
		setLayout(null);
		JButton button = new JButton("Add Track");
		button.addActionListener(this);
		add(button);

		position(button).atOrigin().in(this);
		fit(button).in(this);
	}

	@Override
	public void actionPerformed(ActionEvent actionEvent) {
		mainWindow.displayTrackConfigForNewTrack();
	}
}
