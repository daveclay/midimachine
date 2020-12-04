package com.daveclay.midi.realtime;

import com.daveclay.midi.Receivers;

import javax.sound.midi.InvalidMidiDataException;
import javax.sound.midi.MidiUnavailableException;
import javax.sound.midi.Receiver;
import javax.sound.midi.ShortMessage;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class RealtimeSequencer implements Runnable {
	public static long NANOSECONDS_IN_A_SECOND = 1000 * 1000 * 1000;

	private int bpm;
	private long ticksPerBeat;
	private long nanosecondsPerTick;
	private ScheduledThreadPoolExecutor scheduledThreadPoolExecutor;
	private ScheduledFuture<?> future;
	private long tick;

	private Receiver receiver;

	private List<Command> commands = new ArrayList<Command>();
	private List<RealtimeTrack> realtimeTracks = new ArrayList<RealtimeTrack>();
	private Map<String, RealtimeTrack> nameToTrackMap = new HashMap<String, RealtimeTrack>();
	private RealtimeTrack soloTrack;
	private List<RealtimeSequencerListener> realtimeSequencerListeners = new ArrayList<RealtimeSequencerListener>();

	public RealtimeSequencer(long ticksPerBeat) throws MidiUnavailableException {
		this(120, ticksPerBeat);
	}

	public RealtimeSequencer(Integer bpm, long ticksPerBeat) throws MidiUnavailableException {
		receiver = Receivers.determineDesiredReceiver(1);
		this.ticksPerBeat = ticksPerBeat;
		if (bpm != null) {
			configureTicks(bpm);
		}

		scheduledThreadPoolExecutor = new ScheduledThreadPoolExecutor(1);
	}

	public void addListener(RealtimeSequencerListener realtimeSequencerListener) {
		this.realtimeSequencerListeners.add(realtimeSequencerListener);
	}

	public void addTrack(final RealtimeTrack realtimeTrack) {
		executeCommand(new Command() {
			@Override
			public void execute(RealtimeSequencer realtimeSequencer) {
				addTrackInstrumentCommand(realtimeTrack);
			}
		});
	}

	private void addTrackInstrumentCommand(RealtimeTrack track) {
		realtimeTracks.add(track);
		nameToTrackMap.put(track.getName(), track);
	}

	public RealtimeTrack getTrack(String name) {
		return nameToTrackMap.get(name);
	}

	public void removeTrack(final RealtimeTrack track) {
		executeCommand(new Command() {
			@Override
			public void execute(RealtimeSequencer realtimeSequencer) {
				executeRemoveInstrumentCommand(track);
			}
		});
	}

	private void executeRemoveInstrumentCommand(RealtimeTrack track) {
		realtimeTracks.remove(track);
		nameToTrackMap.remove(track.getName());
	}

	public int getBPM() {
		return bpm;
	}

	public void solo(RealtimeTrack track) {
		if (this.soloTrack != null && this.soloTrack.equals(track)) {
			this.soloTrack = null;
		} else {
			this.soloTrack = track;
		}
	}

	public void play(int bpm) throws InvalidMidiDataException {
		if (future != null && !future.isCancelled()) {
			removeScheduled();
		}
		configureTicks(bpm);
		start();
	}

	public void pause() {
		removeScheduled();
	}

	public void stop() {
		removeScheduled();
		this.tick = 0;
	}

	private void removeScheduled() {
		if (future != null) {
			future.cancel(true);
			scheduledThreadPoolExecutor.remove(this);
		}
	}

	private void configureTicks(int bpm) {
		this.bpm = bpm;
		double beatsPerSecond = bpm / 60.0;
		double nanosecondsPerBeat = NANOSECONDS_IN_A_SECOND / beatsPerSecond;
		this.nanosecondsPerTick = (long) (nanosecondsPerBeat / ticksPerBeat);
	}

	private void executeCommands() {
		synchronized (this) {
			Iterator<Command> iter = this.commands.iterator();
			while (iter.hasNext()) {
				Command command = iter.next();
				command.execute(this);
				iter.remove();
			}
		}
	}

	private void tick(long tick) throws InvalidMidiDataException {
		try {
			notifyListeners(tick);

			for (RealtimeTrack track : realtimeTracks) {
				List<ShortMessage> messagesForTrack = track.getShortMessagesForTick(tick);
				if (!shouldSendTrackData(track)) {
					continue;
				}
				send(messagesForTrack);
			}
		} catch (Exception err) {
			err.printStackTrace();
		}
	}

	private void notifyListeners(long tick) {
		if ( ! realtimeSequencerListeners.isEmpty()) {
			ListenerNotificationRunnable runnable = new ListenerNotificationRunnable(tick, realtimeSequencerListeners);
			new Thread(runnable).start();
		}
	}

	private boolean shouldSendTrackData(RealtimeTrack track) {
		if (this.soloTrack != null) {
			return this.soloTrack.equals(track);
		} else {
			return ! track.isMute();
		}
	}

	private void send(List<ShortMessage> messagesForTrack) {
		for (ShortMessage message : messagesForTrack) {
			receiver.send(message, -1);
		}
	}

	public void start() {
		future = scheduledThreadPoolExecutor.scheduleAtFixedRate(this, 0, nanosecondsPerTick, TimeUnit.NANOSECONDS);
	}

	@Override
	public void run() {
		try {
			executeCommands();
			tick(tick);
			tick++;
		} catch (InvalidMidiDataException e) {
			e.printStackTrace();
		}
	}

	private void executeCommand(Command command) {
		synchronized (this) {
			// commands are execute on loops to avoid (unlikely but real) instance/pointer problems. Running on a tick
			// ensures the track/rhythm/etc instances won't be in the middle of creating midi messages when they're
			// removed and possibly lose their reference (or duplicate messages, or lots of other goofy problems).
			//
			// Hey, it could also represent "undo" functionality.
			commands.add(command);
		}
	}

	private static class ListenerNotificationRunnable implements Runnable{

		private long tick;
		private List<RealtimeSequencerListener> realtimeSequencerListeners;

		public ListenerNotificationRunnable(long tick, List<RealtimeSequencerListener> realtimeSequencerListeners) {
			this.tick = tick;
			// don't want the instance of the list - the next tick may change the elements in the list, causing a concurrent mod exception
			this.realtimeSequencerListeners = new ArrayList<RealtimeSequencerListener>(realtimeSequencerListeners);
		}

		public void run() {
			for (RealtimeSequencerListener listener : realtimeSequencerListeners) {
				listener.tick(tick);
			}
		}
	}

	/**
	 * Used for internal command on tick
	 */
	private interface Command {
		public void execute(RealtimeSequencer realtimeSequencer);
	}
}
