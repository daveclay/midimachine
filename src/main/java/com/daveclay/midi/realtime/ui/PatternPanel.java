package com.daveclay.midi.realtime.ui;

import com.daveclay.midi.realtime.PositionNoteStrategy;
import com.daveclay.midi.realtime.TickRhythm;
import com.daveclay.midi.realtime.ui.util.Size;

import javax.swing.*;
import javax.swing.border.EtchedBorder;
import java.awt.*;
import java.util.*;
import java.util.List;

/**
*/
public class PatternPanel extends JPanel {

	private TrackPanel trackPanel;
	private BeatPanel[] beatPanels;
	private int patternLengthInBeats;
	private int ticksPerPosition;

	private BeatPanel currentMouseDownBeatPanel;

	public PatternPanel(int patternLengthInBeats, TrackPanel trackPanel) {
		this.patternLengthInBeats = patternLengthInBeats;
		this.trackPanel = trackPanel;
		buildGUI();
	}

	private void buildGUI() {
		setLayout(new BoxLayout(this, BoxLayout.LINE_AXIS));
		setBorder(BorderFactory.createEtchedBorder(EtchedBorder.LOWERED));
		updateTickRhythm();
	}

	public void updateTickRhythm() {
		if (beatPanels != null) {
			removeExistingBeatPanels();
		}
		this.ticksPerPosition = trackPanel.getTicksPerPosition();
		beatPanels = new BeatPanel[patternLengthInBeats];
		
		for (int position = 0; position < beatPanels.length; position++) {
			// Todo: do a scroll/move sort of thing in the UI for patterns and positions longer than 32.
			BeatPanel beatPanel = addBeatPanel(position);
			beatPanels[position] = beatPanel;
		}

		Dimension beatPanelSize = beatPanels[0].getPreferredSize();
		Size.size(this, beatPanels.length * (int)beatPanelSize.getWidth() + 4, (int) beatPanelSize.getHeight() + 2);
		
		updateBeatPanelsWithPositionNoteStrategies();
	}

	private void updateBeatPanelsWithPositionNoteStrategies() {
		Map<PositionNoteStrategy, List<Integer>> strategiesToPositions = mapStrategyToPositions();

		for (Map.Entry<PositionNoteStrategy, List<Integer>> strategyToPositions : strategiesToPositions.entrySet()) {
			List<Integer> positions = strategyToPositions.getValue();
			PositionNoteStrategyPanel positionNoteStrategyPanel = trackPanel.createPositionNoteStrategyPanel();
			positionNoteStrategyPanel.updateForStrategy(strategyToPositions.getKey());
			for (int position : positions) {
				trackPanel.associatePositionNoteStrategyToPosition(positionNoteStrategyPanel, position);
				beatPanels[position].beatAddedToPattern(positionNoteStrategyPanel);
				beatPanels[position].beatNotPartOfCurrentPattern();
			}
		}
	}

	// Todo: move this loading to another object, it's confusing to keeep track of the references.
	private Map<PositionNoteStrategy, List<Integer>> mapStrategyToPositions() {
		Map<PositionNoteStrategy, List<Integer>> positionToIndicies = new HashMap<PositionNoteStrategy, List<Integer>>();
		for (int position = 0; position < beatPanels.length; position++) {
			PositionNoteStrategy positionNoteStrategy = getPositionNoteStrategyAtBeatPosition(position);
			if (positionNoteStrategy != null) {
				List<Integer> indicies = positionToIndicies.get(positionNoteStrategy);
				if (indicies == null) {
					indicies = new ArrayList<Integer>();
					positionToIndicies.put(positionNoteStrategy, indicies);
				}

				indicies.addAll(Collections.singleton(position));
			}
		}

		return positionToIndicies;
	}

	private void removeExistingBeatPanels() {
		for (BeatPanel beatPanel : beatPanels) {
			remove(beatPanel);
		}
	}

	private TickRhythm getTickRhythm() {
		return trackPanel.getTickRhythm();
	}

	private BeatPanel addBeatPanel(int position) {
		BeatPanel beatPanel = new BeatPanel(position, trackPanel);
		add(beatPanel);

		return beatPanel;
	}

	private PositionNoteStrategy getPositionNoteStrategyAtBeatPosition(int position) {
		int tickIndex = trackPanel.getTickIndexForPosition(position);
		PositionNoteStrategy hi = getTickRhythm().getPositionNoteStrategyForTickIndex(tickIndex);
		return hi;
	}

	public BeatPanel[] getBeatPanels() {
		return beatPanels;
	}

	public void tick(int tick) {
		updateIndicatorForTick(tick, false);
	}

	public void triggeredNotes(int tick) {
		updateIndicatorForTick(tick, true);
	}

	private void updateIndicatorForTick(int tick, boolean triggered) {
		if (tickFallsOnBeat(tick)) {
			int onIndex = tick / ticksPerPosition;
			int offIndex = onIndex - 1;
			if (offIndex < 0) {
				offIndex = beatPanels.length - 1;
			}

			if (triggered) {
				beatPanels[onIndex].triggeredBeat();
			} else {
				beatPanels[onIndex].onTick();
			}
			beatPanels[offIndex].offTick();
		}
	}

	private boolean tickFallsOnBeat(int tick) {
		return tick % ticksPerPosition == 0; 
	}

	synchronized void setCurrentMouseDownBeatPanel(BeatPanel beatPanel) {
		this.currentMouseDownBeatPanel = beatPanel;
	}
}
