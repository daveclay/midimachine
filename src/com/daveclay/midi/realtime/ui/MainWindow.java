package com.daveclay.midi.realtime.ui;

import com.daveclay.midi.Key;
import com.daveclay.midi.NoteValue;
import com.daveclay.midi.Scale;
import com.daveclay.midi.realtime.RealtimeSequencer;
import com.daveclay.midi.realtime.RealtimeTrack;

import javax.sound.midi.MidiUnavailableException;
import javax.swing.*;
import java.awt.*;
import java.util.*;

import static com.daveclay.midi.realtime.ui.util.Position.position;
import static com.daveclay.midi.realtime.ui.util.Size.*;

/**
 */
public class MainWindow extends JFrame {

	public static final int NUMBER_OF_BEATS = 32;
	public static final int NUMBER_OF_TICKS_PER_BEAT = 128;

	// ticks per phrase / ticks per beat = pattern length. Got it?

	public static void main(String[] args) {
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				MainWindow mainWindow = new MainWindow();
				mainWindow.setVisible(true);
			}
		});
	}

	private RealtimeSequencer sequencer;

	private JSplitPane splitPane;
	private JScrollPane tracksScrollPane;
	private JPanel tracksPanel;
	private TrackConfigPanel trackConfigPanel;
	private AddTrackButtonPanel addTrackButtonPanel;
	private JFrame transportFrame;
	private TransportPanel transportPanel;
	private java.util.List<TrackPanel> trackPanels = new ArrayList<TrackPanel>();

	public MainWindow() {
		super("Random Music Generator");
		buildSequencer();
		buildGUI();
	}

	public java.util.List<TrackPanel> getTrackPanels() {
		return trackPanels;
	}

	public RealtimeSequencer getSequencer() {
		return sequencer;
	}

	private void buildSequencer() {
		try {
			sequencer = new RealtimeSequencer(NUMBER_OF_TICKS_PER_BEAT);
		} catch (MidiUnavailableException e) {
			e.printStackTrace();
		}
	}

	public void addTrackGUI(RealtimeTrack track, TrackConfig trackConfig) {
		TrackPanel trackPanel = new TrackPanel(this, track, trackConfig.getNumberOfPositions());
		sequencer.addListener(trackPanel);
		sequencer.addTrack(track);

		trackPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
		tracksPanel.add(trackPanel);

		trackPanels.add(trackPanel);
		Dimension individualTrackSize = trackPanel.getPreferredSize();
		int totalHeight = (int) individualTrackSize.getHeight() * trackPanels.size();
		int width = (int) individualTrackSize.getWidth();
		Dimension totalSize = new Dimension(width, totalHeight);
		tracksPanel.setPreferredSize(totalSize);

		updateMainWindow();
	}

	private void buildGUI() {
		buildTransportGUI();
		buildTracksGUI();

		trackConfigPanel = new TrackConfigPanel();

		updateMainWindow();
		resetTransportFrame();
	}

	private void resetTransportFrame() {
		Dimension size = getSize();
		transportFrame.setLocation(new Point(0, (int)size.getHeight() + 20));
		transportFrame.setVisible(true);
	}

	private void buildTracksGUI() {
		JPanel tracksConfigPanel = new JPanel();
		addTrackButtonPanel = new AddTrackButtonPanel(this);
		position(addTrackButtonPanel).atOrigin().in(tracksConfigPanel);
		fit(addTrackButtonPanel).in(tracksConfigPanel);

		tracksPanel = new JPanel(); // FYI, the scroll pane needs a single client, I think...
		tracksPanel.setLayout(new BoxLayout(tracksPanel, BoxLayout.PAGE_AXIS));

		tracksScrollPane = new JScrollPane(tracksPanel);
		splitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT, tracksConfigPanel, tracksScrollPane);
		splitPane.setDividerLocation((int)tracksConfigPanel.getPreferredSize().getHeight());
		setContentPane(splitPane);

		setPreferredSizeOf(splitPane).to(1000, 400);
		sizeComponent(this).to(1000, 400);
	}

	private void buildTransportGUI() {
		transportPanel = new TransportPanel(sequencer, this);
		transportFrame = new JFrame("Transport");
		position(transportPanel).atOrigin().in(transportFrame);
		fit(transportPanel).withHeightMargin(20).in(transportFrame);
		transportFrame.pack();
	}

	private void updateMainWindow() {
		tracksScrollPane.revalidate();
		splitPane.revalidate();
	}

	public void removeTrack(TrackPanel trackPanel) {
		this.trackPanels.remove(trackPanel);
		this.tracksPanel.remove(trackPanel);
		this.sequencer.removeTrack(trackPanel.getTrack());
		updateMainWindow();
		repaint();
	}

	public TrackConfigPanel getTrackConfigPanel() {
		return trackConfigPanel;
	}

	public void displayTrackConfigForNewTrack() {
		TrackConfig trackConfig = createNewTrackConfig();
		trackConfigPanel.forNewTrack(trackConfig, new TrackConfigPanel.Listener() {
			@Override
			public void save(TrackConfig trackConfig) {
				saveTrackConfig(trackConfig);
			}
		});
		trackConfigPanel.setVisible(true);
	}

	private TrackConfig createNewTrackConfig() {
		int index = getTrackPanels().size() + 1;
		String name = "Track " + index;
		int channel = 1;
		Key key = new Key(NoteValue.C3, Scale.UNISON_SCALE);
		TrackConfig newTrackConfig = new TrackConfig(name, key, channel, NUMBER_OF_BEATS, NUMBER_OF_TICKS_PER_BEAT);
		return newTrackConfig;
	}

	private void saveTrackConfig(TrackConfig trackConfig) {
		RealtimeTrack newTrack = new RealtimeTrack();
		UpdateTrackHelper.updateTrack(newTrack, trackConfig);
		addTrackGUI(newTrack, trackConfig);
	}
}
