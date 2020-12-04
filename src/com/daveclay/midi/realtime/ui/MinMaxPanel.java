package com.daveclay.midi.realtime.ui;

import com.daveclay.midi.realtime.ui.knob.DKnob;
import com.daveclay.midi.realtime.ui.util.Util;

import javax.swing.*;
import javax.swing.border.EtchedBorder;
import java.awt.*;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

import static com.daveclay.midi.realtime.ui.util.Position.position;

/**
*/
public class MinMaxPanel extends JPanel {
	public static final int WIDTH = 150;
	public static final int HEIGHT = 100;

	private String name;
	private DKnob minKnob;
	private DKnob maxKnob;
	private JCheckBox linkedCheckbox;

	public MinMaxPanel(String name, DKnob minKnob, DKnob maxKnob) {
		this.name = name;
		this.minKnob = minKnob;
		this.maxKnob = maxKnob;
		buildGUI();
	}

	public void buildGUI() {
		setLayout(null);
		Util.setAllSizes(this, MinMaxPanel.WIDTH, MinMaxPanel.HEIGHT);
		setBorder(BorderFactory.createEtchedBorder(EtchedBorder.LOWERED));

		JLabel minLabel = new JLabel("min");
		minLabel.setFont(new Font("Arial", Font.PLAIN, 10));
		minLabel.setLabelFor(this.minKnob);

		linkedCheckbox = new JCheckBox();
		linkedCheckbox.addItemListener(new ItemListener() {
			@Override
			public void itemStateChanged(ItemEvent itemEvent) {
				if (itemEvent.getStateChange() == ItemEvent.SELECTED) {
					link();
				} else {
					unlink();
				}
			}
		});

		JLabel linkLabel = new JLabel("link");
		linkLabel.setFont(new Font("Arial", Font.PLAIN, 10));
		linkLabel.setLabelFor(linkedCheckbox);

		add(minLabel);
		add(minKnob);
		add(linkLabel);
		add(linkedCheckbox);

		JLabel maxLabel = new JLabel("max");
		maxLabel.setFont(new Font("Arial", Font.PLAIN, 10));
		maxLabel.setLabelFor(this.maxKnob);

		JLabel label = new JLabel(this.name);
		label.setFont(new Font("Arial", Font.PLAIN, 10));
		Util.setAllSizes(label, 100, 60);

		add(label);
		add(maxKnob);
		add(maxLabel);

		maxKnob.setIsMaxKnob(true);
		maxKnob.linkKnob(minKnob);

		position(this, minLabel, 2, 22);
		position(this, minKnob, 20, 0);
		position(this, linkLabel, 90, 22);
		position(this, linkedCheckbox, 105, 16);
		position(this, label, 2, 40);
		position(this, maxKnob, 60, 40);
		position(this, maxLabel, 120, 60);
	}

	public void unlink() {
		this.maxKnob.unlink();
	}

	public void link() {
		this.maxKnob.link();
	}
}
