package com.daveclay.midi.realtime;

import com.daveclay.midi.Receivers;

import javax.sound.midi.InvalidMidiDataException;
import javax.sound.midi.MidiUnavailableException;
import javax.sound.midi.Receiver;
import javax.sound.midi.ShortMessage;

/**
 */
public class TestExternalReceiver {

	public static void main(String[] args) throws MidiUnavailableException, InvalidMidiDataException, InterruptedException {
		Receiver receiver = Receivers.determineDesiredReceiver(1);
		
		while (true) {
			ShortMessage message = new ShortMessage();
			message.setMessage(ShortMessage.NOTE_ON, 36, 100);
			receiver.send(message, -1);

			Thread.sleep(500);
		}
	}
}
