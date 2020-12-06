package com.daveclay.midi.realtime.ui.util;

import javax.swing.*;
import java.awt.*;

public enum Fonts {

	BoldNormalFont {
		@Override
		public void applyTo(JComponent component) {
			component.setFont(boldNormal);
		}},
	NormalFont {
		@Override
		public void applyTo(JComponent component) {
			component.setFont(normal);
		}},
	MediumFont {
		@Override
		public void applyTo(JComponent component) {
			component.setFont(medium);
		}},
	TinyFont {
		@Override
		public void applyTo(JComponent component) {
			component.setFont(tiny);
		}};

	public abstract void applyTo(JComponent component);

	private static Font tiny = new Font("Arial", Font.PLAIN, 7);
	private static Font medium = new Font("Arial", Font.PLAIN, 9);
	private static Font normal = new Font("Arial", Font.PLAIN, 10);
	private static Font boldNormal =new Font("Arial", Font.BOLD, 10);
}
