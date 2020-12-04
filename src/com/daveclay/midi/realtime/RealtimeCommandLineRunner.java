package com.daveclay.midi.realtime;

import javax.sound.midi.InvalidMidiDataException;
import javax.sound.midi.MidiUnavailableException;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 */
public class RealtimeCommandLineRunner implements Runnable {

	private RealtimeSequencer sequencer;

	public static void main(String[] args) throws InvalidMidiDataException, MidiUnavailableException {
		RealtimeCommandLineRunner drumSolo = new RealtimeCommandLineRunner();
		Thread t = new Thread(drumSolo);
		t.start();
	}

	public void run() {
		try {
			sequencer = new RealtimeSequencer(120, 128);
		} catch (MidiUnavailableException e) {
			e.printStackTrace();
			return;
		}

		addInstruments(sequencer);

		Thread t = new Thread(sequencer);
		t.start();

		BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
		while (true) {
			try {
				String line = reader.readLine().trim();
				if (line.length() > 0) {
					String[] args = line.split(" ");
					handleCommands(args, line);
				}
			} catch (IOException e) {
				e.printStackTrace();
			} catch (InvalidMidiDataException e) {
				e.printStackTrace();
			}
		}
	}

	protected void addInstruments(RealtimeSequencer sequencer) {
	}

	private void handleCommands(String[] args, String line) throws InvalidMidiDataException {
		String arg = args[0];
		if (arg.equalsIgnoreCase("reset")) {
			reset(args);
		} else if (arg.equalsIgnoreCase("play")) {
			int bpm = 120;
			if (args.length > 1) {
				bpm = Integer.parseInt(args[1]);
			}
			sequencer.play(bpm);
		} else if (arg.equalsIgnoreCase("pause")) {
			sequencer.pause();
		} else if (arg.equalsIgnoreCase("stop")) {
			sequencer.stop();
		} else if (arg.equalsIgnoreCase("remove")) {
			remove(args);
		} else if (arg.equalsIgnoreCase("mute")) {
			mute(args);
		} else if (arg.equalsIgnoreCase("solo")) {
			solo(args);
		} else if (arg.equalsIgnoreCase("add")) {
			track(line);
		}
	}

	private void track(String line) {
		AddTrackParser addTrackParser = new AddTrackParser();
		addTrackParser.parse(line);
		
		RealtimeTrack track = addTrackParser.getTrack();
		sequencer.addTrack(track);
	}

	private void solo(String[] args) {
		if (args.length < 2) {
			throw new IllegalArgumentException("Not enough args for a solo: solo [name]");
		}

		String instrument = args[1];
		RealtimeTrack track = getTrack(instrument);
		sequencer.solo(track);
	}

	private void mute(String[] args) {
		if (args.length < 2) {
			throw new IllegalArgumentException("Not enough args for a mute: mute [name]");
		}

		String instrument = args[1];
		getTrack(instrument).toggleMute();
	}

	private RealtimeTrack getTrack(String name) {
		return sequencer.getTrack(name);
	}

	private void remove(String[] args) {
		if (args.length < 2) {
			throw new IllegalArgumentException("Not enough args for a remove: remove [name]");
		}

		String instrument = args[1];
		RealtimeTrack track = sequencer.getTrack(instrument);
		sequencer.removeTrack(track);
	}

	private void reset(String[] args) {
		if (args.length < 2) {
			throw new IllegalArgumentException("Not enough args for a reset: reset [name] [now|next]");
		}

		String instrument = args[1];
		boolean onNextRepeat = false;

		if (args.length == 3) {
			onNextRepeat = args[2].equalsIgnoreCase("next");
		}

		RealtimeTrack trackObj = sequencer.getTrack(instrument);
		if (trackObj != null) {
			TickRhythm tickRhythm = trackObj.getTickRhythm();
			if (onNextRepeat) {
				tickRhythm.resetLoopOnNextRepeat();
			} else {
				tickRhythm.resetLoopOnNextTick();
			}
		}
	}

}
