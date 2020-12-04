package com.daveclay.midi.realtime.ui.util;

/**
*/
public class Gradient {

	public static GradientValueMap getBasicGradient() {
		GradientValueMap gradientValueMap = new GradientValueMap();
		gradientValueMap.addRedPoint(0, 0);
		gradientValueMap.addGreenPoint(0, 0);
		gradientValueMap.addBluePoint(0, 255);

		gradientValueMap.addRedPoint(.2, 0);
		gradientValueMap.addGreenPoint(.2, 255);
		gradientValueMap.addBluePoint(.2, 255);

		gradientValueMap.addRedPoint(.4, 255);
		gradientValueMap.addGreenPoint(.4, 0);
		gradientValueMap.addBluePoint(.4, 255);

		gradientValueMap.addRedPoint(.6, 255);
		gradientValueMap.addGreenPoint(.6, 255);
		gradientValueMap.addBluePoint(.6, 0);

		gradientValueMap.addRedPoint(.8, 255);
		gradientValueMap.addGreenPoint(.8, 0);
		gradientValueMap.addBluePoint(.8, 0);

		gradientValueMap.addRedPoint(1, 0);
		gradientValueMap.addGreenPoint(1, 255);
		gradientValueMap.addBluePoint(1, 0);

		return gradientValueMap;
	}

	public static GradientValueMap getBlackToWhiteGradient() {
		GradientValueMap gradientValueMap = new GradientValueMap();
		gradientValueMap.addBluePoint(0, 0);
		gradientValueMap.addBluePoint(1, 255);
		gradientValueMap.addRedPoint(0, 0);
		gradientValueMap.addRedPoint(1, 255);
		gradientValueMap.addGreenPoint(0, 0);
		gradientValueMap.addGreenPoint(1, 255);
		return gradientValueMap;
	}
}
