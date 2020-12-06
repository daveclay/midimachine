package com.daveclay.midi.realtime.ui.util;

import java.awt.*;

/**
*/
public class ColorUtils {

	public static ColorUtils applyAlpha(int alpha) {
		ColorUtils hi = new ColorUtils();
		hi.alpha = alpha;
		return hi;
	}

	public static ColorUtils lighten(Color color) {
		ColorUtils hi = new ColorUtils();
		hi.lighten = true;
		hi.color = color;
		return hi;
	}

	public Color by(int amount) {
		int red = color.getRed();
		int green = color.getGreen();
		int blue = color.getBlue();

		if (lighten) {
			red += amount;
			green += amount;
			blue += amount;
		} else {
			red -= amount;
			green -= amount;
			blue -= amount;
		}

		red = limit(red);
		green = limit(green);
		blue = limit(blue);

		return new Color(red, green, blue);
	}

	private int limit(int red) {
		if (red > 255) return 255;
		if (red < 0) return 0;
		return red;
	}

	private boolean lighten;
	private Color color;

	private int alpha;

	public Color toColor(Color color) {
		int red = color.getRed();
		int green = color.getGreen();
		int blue = color.getBlue();
		return new Color(red, green, blue, alpha);
	}
}
