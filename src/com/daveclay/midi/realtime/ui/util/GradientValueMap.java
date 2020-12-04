package com.daveclay.midi.realtime.ui.util;

import java.awt.*;

/**
*/
public class GradientValueMap {

	private SingleGradientValueMap redMap;
	private SingleGradientValueMap greenMap;
	private SingleGradientValueMap blueMap;

	public GradientValueMap() {
		redMap = new SingleGradientValueMap();
		greenMap = new SingleGradientValueMap();
		blueMap = new SingleGradientValueMap();
	}

	public void addRedPoint(double value, int color) {
		redMap.addPoint(value, color);
	}

	public void addGreenPoint(double value, int color) {
		greenMap.addPoint(value, color);
	}

	public void addBluePoint(double value, int color) {
		blueMap.addPoint(value, color);
	}

	public Color getColorForValue(double value) {
		int red = redMap.getColorAtValue(value);
		int green = greenMap.getColorAtValue(value);
		int blue = blueMap.getColorAtValue(value);
		return new Color(red, green, blue);
	}
}
