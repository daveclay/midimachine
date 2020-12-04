package com.daveclay.midi;

import javax.sound.midi.InvalidMidiDataException;
import javax.sound.midi.MidiDevice;
import javax.sound.midi.MidiEvent;
import javax.sound.midi.MidiSystem;
import javax.sound.midi.MidiUnavailableException;
import javax.sound.midi.Receiver;
import javax.sound.midi.Sequence;
import javax.sound.midi.Sequencer;
import javax.sound.midi.ShortMessage;
import javax.sound.midi.Track;
import javax.sound.midi.MidiDevice.Info;

import java.io.File;
import java.io.IOException;

/**
 */
public class Receivers {

	public static void main(String[] args) throws MidiUnavailableException {
		Receivers.determineDesiredReceiver(args);
	}

	/* Utility for use by main methods of other classes until we consolidate them */
	public static Receiver determineDesiredReceiver(String[] args) throws MidiUnavailableException {
		int desiredReceiverIndex = -1;
		if (args.length > 1) {
			desiredReceiverIndex = Integer.parseInt(args[1]);
		}

		return determineDesiredReceiver(desiredReceiverIndex);
	}

	public static Receiver determineDesiredReceiver(int desiredReceiverIndex) throws MidiUnavailableException {
		Receiver desiredReceiver = null;
		Info deviceInfos[] = MidiSystem.getMidiDeviceInfo();
		System.out.println("Available receivers:");
		for (int i = 0; i < deviceInfos.length; i++) {
			Info info = deviceInfos[i];
			MidiDevice device = MidiSystem.getMidiDevice(info);
			if (device.getMaxReceivers() == -1 || device.getMaxReceivers() > 0) {
				System.out.println("Device " + i + ": " + info);
				if (i == desiredReceiverIndex) {
					device.open();
					desiredReceiver = device.getReceiver();
				}
			}
		}
		if (desiredReceiverIndex > -1 && desiredReceiver == null) {
			throw new RuntimeException("Could not find receiver with index " + desiredReceiverIndex);
		}
		return desiredReceiver;
	}
}
