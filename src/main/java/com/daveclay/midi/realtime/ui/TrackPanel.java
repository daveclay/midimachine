package com.daveclay.midi.realtime.ui;

import com.daveclay.midi.Key;
import com.daveclay.midi.NoteValue;
import com.daveclay.midi.Scale;
import com.daveclay.midi.random.RandyTheRandom;
import com.daveclay.midi.realtime.Notes;
import com.daveclay.midi.realtime.PositionNoteStrategy;
import com.daveclay.midi.realtime.RealtimeSequencerListener;
import com.daveclay.midi.realtime.RealtimeTrack;
import com.daveclay.midi.realtime.TickRhythm;
import com.daveclay.midi.realtime.TrackPreset;
import com.daveclay.midi.realtime.TrackPresetDAO;
import com.daveclay.midi.realtime.TrackListener;
import com.daveclay.midi.realtime.ui.util.AutoComplete;
import com.daveclay.midi.realtime.ui.util.Gradient;
import com.daveclay.midi.realtime.ui.util.GradientValueMap;

import javax.swing.*;
import javax.swing.border.EtchedBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.peer.ComponentPeer;
import java.util.HashSet;
import java.util.Set;

import static com.daveclay.midi.realtime.ui.util.Fonts.*;
import static com.daveclay.midi.realtime.ui.util.Position.position;
import static com.daveclay.midi.realtime.ui.util.Size.fit;

/**
*/
public class TrackPanel extends JPanel implements RealtimeSequencerListener, TrackListener {

	private RealtimeTrack track;
	private int numberOfPositions;

	private MainWindow mainWindow;
	private JLabel trackInfoLabel;
	private PatternPanel patternPanel;

	private JButton editTrackConfigButton;
	private JButton removeTrackButton;
	private JButton saveTrackPresetButton;
	private JButton loadTrackPresetButton;
	private AutoComplete presetsSelection;
	private JLabel repeatField;
	private JRadioButton repeatLoopImmediatelyButton;
	private JRadioButton repeatLoopOnNextLoopButton;
	private JRadioButton neverRepeatButton;

	private TrackPresetDAO trackPresetDAO;

	private PositionNoteStrategyPanel[] positionNoteStrategyPanels;
	private PositionNoteStrategyPanel currentlySelectedPositionNoteStrategyPanel;
	private Set<Integer> colorPatternIndicies = new HashSet<Integer>();

	public TrackPanel(MainWindow mainWindow, RealtimeTrack track, int numberOfPositions) {
		this.mainWindow = mainWindow;
		this.trackPresetDAO = new TrackPresetDAO();
		this.numberOfPositions = numberOfPositions;
		setTrack(track);
		buildGUI();
	}

	private void setTrack(RealtimeTrack track) {
		this.track = track;
		track.setTrackListener(this);
	}

	@Override
	public void notesTriggered(long tick, Notes notes) {
		updatePatternPanelTickPosition(tick, true);
	}

	@Override
	public void tick(long tick) {
		updatePatternPanelTickPosition(tick, false);
	}

	private void updatePatternPanelTickPosition(final long tick, final boolean triggered) {
		final int position = (int) (tick % track.getTickRhythm().getNumberOfTicksInPhrase());
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				if (triggered) {
					patternPanel.triggeredNotes(position);
				} else {
					patternPanel.tick(position);
				}
			}
		});
	}

	public RealtimeTrack getTrack() {
		return track;
	}

	private void buildGUI() {
		setBorder(BorderFactory.createEtchedBorder(EtchedBorder.LOWERED));

		buildTrackInfoLabel();
		buildTrackButtons();

		// pattern positions in beats can be arbitrary...
		buildPositionNoteStrategyPanels();
		patternPanel = new PatternPanel(numberOfPositions, this);

		// create the first, new, default one
		setCurrentSelectedTickRhythmPanelForPosition(0);

		position(patternPanel).below(currentlySelectedPositionNoteStrategyPanel).in(this);
		fit(patternPanel, currentlySelectedPositionNoteStrategyPanel).withMargin(2).in(this);
	}

	private void buildPositionNoteStrategyPanels() {
		this.positionNoteStrategyPanels = new PositionNoteStrategyPanel[numberOfPositions];
	}

	private void buildTrackButtons() {
		buildEditTrackConfigButton();
		buildRemoveTrackConfigButton();
		buildSaveTickRhythmButton();
		buildLoadTickRhythmButton();
		buildPresetSelection();
		buildRepeatLoopButtonGroup();

		positionTrackButtons();
	}

	private void buildRepeatLoopButtonGroup() {
		buildRepeatLoopTextField();
		buildRepeatLoopImmediatelyButton();
		buildRepeatLoopOnNextLoopButton();
		buildNeverRepeatButton();
		buildRepeatGroup();
	}

	private void buildRepeatGroup() {
		ButtonGroup repeatButtonGroup = new ButtonGroup();
		repeatButtonGroup.add(neverRepeatButton);
		repeatButtonGroup.add(repeatLoopOnNextLoopButton);
		repeatButtonGroup.add(repeatLoopImmediatelyButton);
	}

	private void positionTrackButtons() {
		position(editTrackConfigButton).toTheRightOf(trackInfoLabel).in(this);
		position(removeTrackButton).toTheRightOf(editTrackConfigButton).in(this);
		position(saveTrackPresetButton).toTheRightOf(removeTrackButton).in(this);
		position(loadTrackPresetButton).toTheRightOf(saveTrackPresetButton).in(this);
		position(presetsSelection).toTheRightOf(loadTrackPresetButton).in(this);

		JPanel repeatPanel = new JPanel();
		position(repeatField).atOrigin().in(repeatPanel);
		position(neverRepeatButton).toTheRightOf(repeatField).in(repeatPanel);
		position(repeatLoopOnNextLoopButton).toTheRightOf(neverRepeatButton).in(repeatPanel);
		position(repeatLoopImmediatelyButton).toTheRightOf(repeatLoopOnNextLoopButton).in(repeatPanel);
		fit(repeatField, neverRepeatButton, repeatLoopOnNextLoopButton, repeatLoopImmediatelyButton).in(repeatPanel);

		position(repeatPanel).toTheRightOf(presetsSelection).in(this);
	}

	private void buildRepeatLoopTextField() {
		repeatField = new JLabel("repeat:");
		NormalFont.applyTo(repeatField);
	}

	private void buildNeverRepeatButton() {
		this.neverRepeatButton = new JRadioButton("no");
		neverRepeatButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent actionEvent) {
				TrackPanel.this.getTickRhythm().neverRepeat();
			}
		});
		NormalFont.applyTo(neverRepeatButton);
	}

	private void buildRepeatLoopOnNextLoopButton() {
		this.repeatLoopOnNextLoopButton = new JRadioButton("next");
		NormalFont.applyTo(repeatLoopOnNextLoopButton);
		repeatLoopOnNextLoopButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent actionEvent) {
				TrackPanel.this.getTickRhythm().repeatOnNextLoop();
			}
		});
	}

	private void buildRepeatLoopImmediatelyButton() {
		this.repeatLoopImmediatelyButton = new JRadioButton("now");
		NormalFont.applyTo(repeatLoopImmediatelyButton);
		repeatLoopImmediatelyButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent actionEvent) {
				TrackPanel.this.getTickRhythm().repeatImmediately();
			}
		});
	}

	private void buildPresetSelection() {
		presetsSelection = new AutoComplete(new String[0]);
		NormalFont.applyTo(presetsSelection);
		presetsSelection.setModel(new TrackPresetComboBoxModel(trackPresetDAO));
		presetsSelection.setEditable(true);
		presetsSelection.setEnableAddingValues(true);
	}

	private void buildSaveTickRhythmButton() {
		saveTrackPresetButton = new JButton("Save");
		NormalFont.applyTo(saveTrackPresetButton);
		saveTrackPresetButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent actionEvent) {
				saveTrackPreset();
			}
		});
	}

	private void buildLoadTickRhythmButton() {
		loadTrackPresetButton = new JButton("Load");
		NormalFont.applyTo(loadTrackPresetButton);
		loadTrackPresetButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent actionEvent) {
				loadTrackPreset();
			}
		});
	}

	private void buildRemoveTrackConfigButton() {
		removeTrackButton = new JButton("Remove");
		NormalFont.applyTo(removeTrackButton);
		removeTrackButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent actionEvent) {
				mainWindow.removeTrack(TrackPanel.this);
			}
		});

	}

	private void buildEditTrackConfigButton() {
		editTrackConfigButton = new JButton("Edit");
		NormalFont.applyTo(editTrackConfigButton);
		editTrackConfigButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent actionEvent) {
				showEditTrack();
			}
		});
	}

	private void loadTrackPreset() {
		try {
			RealtimeTrack saved = getSavedTrackInstance();
			if (saved != null) {
				this.track = saved;
				mainWindow.getSequencer().removeTrack(this.track);
				mainWindow.getSequencer().addTrack(saved);

				updateTrackGUI();
			}
		} catch (Exception err) {
			err.printStackTrace();
		}
	}

	private void updateTrackGUI() {
		updateTrackInfo();
		updateGUIForSavedTickRhythm();
		revalidate();
	}

	private void updateGUIForSavedTickRhythm() {
		buildPositionNoteStrategyPanels();
		this.patternPanel.updateTickRhythm();
	}

	private RealtimeTrack getSavedTrackInstance() {
		Object selectedItem = presetsSelection.getSelectedItem();
		if (selectedItem == null) {
			return null;
		}
		TrackPreset preset = trackPresetDAO.load(selectedItem.toString());
		if (preset == null) {
			return null;
		}

		return preset.getTrack();
	}

	private void saveTrackPreset() {
		DefaultComboBoxModel model = (DefaultComboBoxModel) presetsSelection.getModel();

		Object selectedItem = presetsSelection.getSelectedItem();
		if (selectedItem == null) {
			selectedItem = "Preset " + (model.getSize() + 1);
		}

		String name = selectedItem.toString();

		TrackPreset preset = new TrackPreset();
		preset.setName(name);

		track.saveTo(preset);
		trackPresetDAO.save(preset);
	}

	private void showEditTrack() {
		String name = track.getName();
		Key key = track.getKey();
		int channel = track.getChannel();
		int numberOfTicksPerPosition = track.getTickRhythm().getNumberOfTicksInPhrase() / numberOfPositions;
		TrackConfig trackConfig = new TrackConfig(name, key, channel, numberOfPositions, numberOfTicksPerPosition);

		TrackConfigPanel trackConfigPanel = mainWindow.getTrackConfigPanel();
		trackConfigPanel.forEditingTrack(trackConfig, new TrackConfigPanel.Listener() {
			@Override
			public void save(TrackConfig trackConfig) {
				updateTrack(trackConfig);
			}
		});
		trackConfigPanel.setVisible(true);
	}

	public void updateTrack(TrackConfig trackConfig) {
		UpdateTrackHelper.updateTrack(this.track, trackConfig);
		updateTrackGUI();
	}

	private void updateTrackInfo() {
		String name = track.getName();
		Key key = track.getKey();
		NoteValue root = key.getRootNote();
		Scale scale = key.getScale();

		StringBuilder info = new StringBuilder();
		info.append(name);
		info.append(" ");
		info.append(root.getNoteAndOctave());
		info.append(" ");
		info.append(scale.getName());

		trackInfoLabel.setText(info.toString());
	}

	private void buildTrackInfoLabel() {
		trackInfoLabel = new JLabel();
		updateTrackInfo();
		position(trackInfoLabel).atOrigin().in(this);
	}

	public void addPositionToCurrentRhythmPanel(int position) {
		positionNoteStrategyPanels[position] = currentlySelectedPositionNoteStrategyPanel;
		setPositionNoteStrategy(position, currentlySelectedPositionNoteStrategyPanel.asPositionNoteStrategy());
		updateBeatPanels();
	}

	public void removePositionFromRhythmPanel(int position) {
		positionNoteStrategyPanels[position] = null; // Todo: or replace with previous one?
		setPositionNoteStrategy(position, null);
		updateBeatPanels();
	}

	private void setPositionNoteStrategy(int position, PositionNoteStrategy positionNoteStrategy) {
		int tickIndex = getTickIndexForPosition(position);
		TickRhythm tickRhythm = getTickRhythm();
		tickRhythm.setPositionNoteStrategy(tickIndex, positionNoteStrategy);
	}

	public int getTickIndexForPosition(int position) {
		int ticksPerPosition = getTicksPerPosition();
		int tickIndex = position * ticksPerPosition;
		return tickIndex;
	}

	public int getTicksPerPosition() {
		TickRhythm tickRhythm = getTickRhythm();
		int ticksPerPosition = tickRhythm.getNumberOfTicksInPhrase() / numberOfPositions;
		return ticksPerPosition;
	}

	public TickRhythm getTickRhythm() {
		TickRhythm tickRhythm = track.getTickRhythm();
		return tickRhythm;
	}

	public PositionNoteStrategyPanel getTickRhythmPanelForPosition(int position) {
		return this.positionNoteStrategyPanels[position];
	}

	public void selectRhythmPanel(int position) {
		setCurrentSelectedTickRhythmPanelForPosition(position);
		updateBeatPanels();
		showCurrentTickRhythmPanel();
	}

	private void setCurrentSelectedTickRhythmPanelForPosition(int position) {
		PositionNoteStrategyPanel positionNoteStrategyPanel = getTickRhythmPanelForPosition(position);
		if (positionNoteStrategyPanel == null) {
			positionNoteStrategyPanel = createPositionNoteStrategyPanelAtPosition(position);
		}
		removeTheCurrentlySelectedPanel();
		this.currentlySelectedPositionNoteStrategyPanel = positionNoteStrategyPanel;
	}

	private void removeTheCurrentlySelectedPanel() {
		if (this.currentlySelectedPositionNoteStrategyPanel != null) {
			this.currentlySelectedPositionNoteStrategyPanel.setVisible(false);
		}
	}
	
	private void showCurrentTickRhythmPanel() {
		currentlySelectedPositionNoteStrategyPanel.setVisible(true);
		repaint();
	}

	public PositionNoteStrategyPanel createPositionNoteStrategyPanel() {
		Color color = getPatternColor();
		PositionNoteStrategyPanel panel = new PositionNoteStrategyPanel(track, color);
		position(panel).below(trackInfoLabel).in(this);
		return panel;
	}

	public PositionNoteStrategyPanel createPositionNoteStrategyPanelAtPosition(int position) {
		PositionNoteStrategyPanel panel = createPositionNoteStrategyPanel();
		associatePositionNoteStrategyToPosition(panel, position);
		return panel;
	}

	public void associatePositionNoteStrategyToPosition(PositionNoteStrategyPanel panel, int position) {
		this.positionNoteStrategyPanels[position] = panel;
	}

	private void updateBeatPanels() {
		BeatPanel[] beatPanels = patternPanel.getBeatPanels();
		for (int position = 0; position < beatPanels.length; position++) {
			BeatPanel beatPanel = beatPanels[position];
			PositionNoteStrategyPanel positionNoteStrategyPanel = getTickRhythmPanelForPosition(position);

			if (positionNoteStrategyPanel == currentlySelectedPositionNoteStrategyPanel) {
				beatPanel.beatAddedToPattern(positionNoteStrategyPanel);
			} else if (positionNoteStrategyPanel != null) {
				beatPanel.beatNotPartOfCurrentPattern();
			} else {
				beatPanel.beatRemovedFromPattern();
			}
		}
		repaint();
	}

	private Color getPatternColor() {
		GradientValueMap map = Gradient.getBasicGradient();
		int index;
		do {
			index = RandyTheRandom.randomIntBetweenInclusive(0, 31);
		} while (colorPatternIndicies.contains(index));
		colorPatternIndicies.add(index);

		double val = (double)index / 31d;
		return map.getColorForValue(val);
	}

	public PatternPanel getPatternPanel() {
		return patternPanel;
	}
}
