package com.daveclay.midi.realtime.ui.util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
*/
class SingleGradientValueMap {

	List<GradientValuePoint> gradientValuePoints = new ArrayList<GradientValuePoint>();

	int getColorAtValue(double value) {
		GradientValuePoint[] points = getGradientValuePointForValue(value);

		// ratio = zero-based value / range
		// ratio = val - min / max - min;
		double min = points[0].value;
		double max = points[1].value;
		double ratio = (value - min) / (max - min);

		int minColor = points[0].colorValue;
		int maxColor = points[1].colorValue;

		int colorRange = maxColor - minColor;
		double colorValue =  colorRange * ratio;

		int color = minColor + (int) colorValue;
		if (color > 255) {
			return 255;
		} else if (color < 0) {
			return 0;
		}

		return color;
	}

	GradientValuePoint[] getGradientValuePointForValue(double value) {
		GradientValuePoint lowerPoint = null;
		for (GradientValuePoint point : gradientValuePoints) {
			if (value >= point.value) {
				lowerPoint = point;
			} else {
				if (lowerPoint == null) {
					return new GradientValuePoint[] { point, point };
				} else {
					return new GradientValuePoint[] { lowerPoint, point };
				}
			}
		}

		// max
		return new GradientValuePoint[] { gradientValuePoints.get(gradientValuePoints.size() - 2), gradientValuePoints.get(gradientValuePoints.size()- 1) };
	}

	void addPoint(double value, int color) {
		GradientValuePoint point = new GradientValuePoint();
		point.value = value;
		point.colorValue = color;
		gradientValuePoints.add(point);
	}

	public void setGradientValuePoints(GradientValuePoint[] points) {
		this.gradientValuePoints = Arrays.asList(points);
	}
}
