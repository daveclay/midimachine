package com.daveclay.midi.realtime.ui.util;

import javax.swing.*;

/**
 */
public class SwingUtil {

	public static void run(Runnable runnable) {
		SwingUtilities.invokeLater(runnable);
	}
}
