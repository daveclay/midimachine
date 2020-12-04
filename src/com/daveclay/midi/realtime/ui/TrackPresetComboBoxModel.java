package com.daveclay.midi.realtime.ui;

import com.daveclay.midi.realtime.TrackPreset;
import com.daveclay.midi.realtime.TrackPresetDAO;

import javax.swing.*;

public class TrackPresetComboBoxModel extends DefaultComboBoxModel {

	private TrackPresetDAO trackPresetDAO;

	public TrackPresetComboBoxModel(TrackPresetDAO trackPresetDAO) {
		this.trackPresetDAO = trackPresetDAO;
		trackPresetDAO.setListener(new TrackPresetDAO.Listener() {
			@Override
			public void presetSaved(String name, TrackPreset trackPreset) {
				updateModel(name);
			}
		});
	}

	private void updateModel(String name) {
		addElement(name);
	}

	@Override
	public int getSize() {
		return trackPresetDAO.getSize();
	}

	@Override
	public Object getElementAt(int i) {
		java.util.List<String> names = trackPresetDAO.getNames();
		return names.get(i);
	}
}
