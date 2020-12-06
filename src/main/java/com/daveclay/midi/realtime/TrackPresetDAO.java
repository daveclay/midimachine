package com.daveclay.midi.realtime;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TrackPresetDAO {

	// Todo: long-term storage?
	private Map<String, TrackPreset> savedTickRhythms = new HashMap<String, TrackPreset>();
	private Listener listener;

	public void setListener(Listener listener) {
		this.listener = listener;
	}

	public void save(TrackPreset trackPreset) {
		String name = trackPreset.getName();
		boolean newPreset = true;
		if (savedTickRhythms.containsKey(name)) {
			newPreset = false;
		}
		savedTickRhythms.put(name, trackPreset);

		if (newPreset && listener != null) {
			listener.presetSaved(name, trackPreset);
		}
	}

	public TrackPreset load(String name) {
		return savedTickRhythms.get(name);
	}

	public int getSize() {
		return savedTickRhythms.size();
	}

	public List<String> getNames() {
		List<String> names = new ArrayList<String>(savedTickRhythms.keySet());
		Collections.sort(names);
		return names;
	}

	public static interface Listener {
		public void presetSaved(String name, TrackPreset trackPreset);
	}
}
