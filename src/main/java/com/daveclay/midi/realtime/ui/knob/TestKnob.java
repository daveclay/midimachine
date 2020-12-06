package com.daveclay.midi.realtime.ui.knob;
/* 
 * DKnob example
 * (c) 2000, Joakim Eriksson, 
 * Instructions at:  
 * http://www.dreamfabric.com/java/dknob/knob.html
 * Please e-mail joakim@dreamfabric.com for comments or
 * questions.
 */

import com.daveclay.midi.realtime.ui.util.ValueMap;

import java.applet.Applet;
import java.awt.*;
import javax.swing.*;
import javax.swing.event.*;

public class TestKnob extends Applet
{
	private DKnob dKnob;

	public void start() {
	    DKnob knob;
	    JLabel jl;

	    setLayout(new BorderLayout());	    
	    JPanel jp = new JPanel(new BorderLayout());
		dKnob = new DKnob(new ValueMap(0, 100));
		jp.add(knob = dKnob, BorderLayout.CENTER);
	    jp.add(jl = new JLabel("Value: 0"), BorderLayout.NORTH);
	    add(jp, BorderLayout.CENTER);
	    final JLabel jla = jl;

	    // Add a change listener to the knob
	    knob.addChangeListener(new ChangeListener() {
		    public void stateChanged(ChangeEvent e) {
			DKnob t = (DKnob) e.getSource();
			jla.setText("Value: " + 
				    ((int)(100 * t.getInternalValue()))/100.0 );
		    }
		});
	}
}
