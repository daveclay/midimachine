/*  
 * DKnob.java
 * (c) 2000 by Joakim Eriksson
 *  
 * DKnob is a component similar to JSlider but with 
 * round "user interface", a knob. 
 */
package com.daveclay.midi.realtime.ui.knob;

import com.daveclay.midi.realtime.ui.util.GradientValueMap;
import com.daveclay.midi.realtime.ui.util.ValueMap;

import java.awt.*;
import java.awt.event.*;
import java.awt.geom.*;
import javax.swing.*;
import javax.swing.event.*;

public class DKnob extends JComponent {

	private final static float START = 225;
	private final static float LENGTH = 270;
	private final static float PI = (float) 3.1415;
	private final static double START_ANG = (START / 360) * PI * 2;
	private final static double LENGTH_ANG = (LENGTH / 360) * PI * 2;
	private final static float DRAG_RES = (float) 0.01;
	private final static float MULTIP = 180 / PI;
	private final static Color DEFAULT_FOCUS_COLOR = new Color(0x8080ff);

	private int SHADOWX = 1;
	private int SHADOWY = 1;
	private float DRAG_SPEED;
	private float CLICK_SPEED;
	private int size;
	private int middle;
	private ValueMap valueMap;
	private DKnob linkedKnob;
	private boolean enableLink;
	private boolean displayMakerLines;	

	public enum DragType {
		SIMPLE,
		ROUND
	}

	private DragType dragType = DragType.ROUND;

	private final static Dimension MIN_SIZE = new Dimension(60, 60);
	private final static Dimension PREF_SIZE = new Dimension(60, 60);

	// Set the antialiasing to get the right look!
	private final static RenderingHints AALIAS =
			new RenderingHints(RenderingHints.KEY_ANTIALIASING,
					RenderingHints.VALUE_ANTIALIAS_ON);

	private ChangeEvent changeEvent = null;
	private EventListenerList listenerList = new EventListenerList();

	private Arc2D hitArc = new Arc2D.Float(Arc2D.PIE);

	private double ang = START_ANG;
	private double val;
	private int dragpos = -1;
	private double startVal;
	private double lastAng;
	private GradientValueMap gradientValueMap;
	private boolean isMaxKnob;

	public DKnob() {
		this(null);
	}

	public DKnob(int min, int max) {
		this(new ValueMap(min, max));
	}

	public DKnob(ValueMap valueMap) {
		this.valueMap = valueMap;

		gradientValueMap = new GradientValueMap();
		gradientValueMap.addBluePoint(0, 0);
		gradientValueMap.addBluePoint(.7, 0);
		gradientValueMap.addBluePoint(.8, 180);
		gradientValueMap.addBluePoint(.9, 60);
		gradientValueMap.addBluePoint(1, 0);

		gradientValueMap.addRedPoint(0, 0);
		gradientValueMap.addRedPoint(.1, 160);
		gradientValueMap.addRedPoint(.2, 255);
		gradientValueMap.addRedPoint(.7, 200);
		gradientValueMap.addRedPoint(1, 0);

		gradientValueMap.addGreenPoint(0, 0);
		gradientValueMap.addGreenPoint(.2, 0);
		gradientValueMap.addGreenPoint(.9, 255);

		DRAG_SPEED = 0.01F;
		CLICK_SPEED = 0.01F;
		SHADOWX = 1;
		SHADOWY = 1;

		setPreferredSize(PREF_SIZE);
		hitArc.setAngleStart(235); // Degrees ??? Radians???
		addMouseListener(new MouseAdapter() {
			public void mousePressed(MouseEvent me) {
				dragpos = me.getX() + me.getY();
				startVal = val;

				// Fix last angle
				int xpos = middle - me.getX();
				int ypos = middle - me.getY();
				lastAng = Math.atan2(xpos, ypos);

				requestFocus();
			}

			public void mouseClicked(MouseEvent me) {
				hitArc.setAngleExtent(-(LENGTH + 20));
				if (hitArc.contains(me.getX(), me.getY())) {
					hitArc.setAngleExtent(MULTIP * (ang - START_ANG) - 10);
					if (hitArc.contains(me.getX(), me.getY())) {
						decValue();
					} else incValue();
				}
			}
		});

		// Let the user control the knob with the mouse
		addMouseMotionListener(new MouseMotionAdapter() {
			public void mouseDragged(MouseEvent me) {
				if (dragType == DragType.SIMPLE) {
					double f = DRAG_SPEED * (double) ((me.getX() + me.getY()) - dragpos);
					setInternalValue(startVal + f);
				} else if (dragType == DragType.ROUND) {
					// Measure relative the middle of the button!
					int xpos = middle - me.getX();
					int ypos = middle - me.getY();
					double ang = Math.atan2(xpos, ypos);
					double diff = lastAng - ang;
					setInternalValue((double) (getInternalValue() + (diff / LENGTH_ANG)));

					lastAng = ang;
				}
			}

			public void mouseMoved(MouseEvent me) {
			}
		});

		// Let the user control the knob with the keyboard
		addKeyListener(new KeyListener() {

			public void keyTyped(KeyEvent e) {
			}

			public void keyReleased(KeyEvent e) {
			}

			public void keyPressed(KeyEvent e) {
				int k = e.getKeyCode();
				if (k == e.VK_RIGHT)
					incValue();
				else if (k == e.VK_LEFT)
					decValue();
			}
		});

		// Handle focus so that the knob gets the correct focus highlighting.
		addFocusListener(new FocusListener() {
			public void focusGained(FocusEvent e) {
				repaint();
			}

			public void focusLost(FocusEvent e) {
				repaint();
			}
		});
	}

	public Color getColorForValue() {
		return gradientValueMap.getColorForValue(val);
	}

	public void setDragType(DragType type) {
		dragType = type;
	}

	public DragType getDragType() {
		return dragType;
	}

	public boolean isManagingFocus() {
		return true;
	}

	public boolean isFocusTraversable() {
		return true;
	}

	private void incValue() {
		setInternalValue(val + CLICK_SPEED);
	}

	private void decValue() {
		setInternalValue(val - CLICK_SPEED);
	}

	public double getInternalValue() {
		return val;
	}

	public int getMappedValue() {
		return valueMap.getValue(getInternalValue());
	}

	public void setMappedValue(int value) {
		setInternalValue(valueMap.getInternalValue(value));
	}

	public void setInternalValue(double val) {
		if (linkedKnob != null) {
			if (this.enableLink) {
				linkedKnob.setInternalValueAndRepaint(val);
			} else {
				if (isMaxKnob) {
					// then this can't be lower than the linked min knob
					if (val < linkedKnob.val) {
						linkedKnob.setInternalValueAndRepaint(val);
					}
				} else {
					// else, this is the min knob. the val can't be greater than the max.
					if (val > linkedKnob.val) {
						linkedKnob.setInternalValueAndRepaint(val);
					}
				}
			}

		}

		setInternalValueAndRepaint(val);
	}

	private void setInternalValueAndRepaint(double val) {
		if (val < 0) val = 0;
		if (val > 1) val = 1;
		this.val = val;
		ang = START_ANG - LENGTH_ANG * val;
		repaint();
		fireChangeEvent();
	}

	public void setIsMaxKnob(boolean max) {
		this.isMaxKnob = max;
	}

	public void linkKnob(DKnob knobToLink) {
		this.linkedKnob = knobToLink;
		knobToLink.linkedKnob = this;
	}

	public void link() {
		if (linkedKnob != null) {
			this.enableLink = true;
			this.linkedKnob.enableLink = true;
		}
	}

	public void unlink() {
		if (linkedKnob != null) {
			this.enableLink = false;
			this.linkedKnob.enableLink = false;
		}
	}


	public void addChangeListener(ChangeListener cl) {
		listenerList.add(ChangeListener.class, cl);
	}

	public void removeChangeListener(ChangeListener cl) {
		listenerList.remove(ChangeListener.class, cl);
	}

	public Dimension getMinimumSize() {
		return MIN_SIZE;
	}

	protected void fireChangeEvent() {
		// Guaranteed to return a non-null array
		Object[] listeners = listenerList.getListenerList();
		// Process the listeners last to first, notifying
		// those that are interested in this event
		for (int i = listeners.length - 2; i >= 0; i -= 2) {
			if (listeners[i] == ChangeListener.class) {
				// Lazily create the event:
				if (changeEvent == null)
					changeEvent = new ChangeEvent(this);
				((ChangeListener) listeners[i + 1]).stateChanged(changeEvent);
			}
		}
	}


	// Paint the DKnob
	public void paint(Graphics g) {
		int width = getWidth();
		int height = getHeight();
		size = Math.min(width, height) - 22;
		middle = 10 + size / 2;

		if (g instanceof Graphics2D) {
			Graphics2D g2d = (Graphics2D) g;
			g2d.setBackground(getParent().getBackground());
			g2d.addRenderingHints(AALIAS);

			// For the setAllSizes of the "mouse click" area
			hitArc.setFrame(4, 4, size + 12, size + 12);
		}

		// Paint the "markers"
		if (displayMakerLines) {
			for (double a2 = START_ANG; a2 >= START_ANG - LENGTH_ANG; a2 = a2 - (float)
					(LENGTH_ANG / 10.01)) {
				int x = 10 + size / 2 + (int) ((6 + size / 2) * Math.cos(a2));
				int y = 10 + size / 2 - (int) ((6 + size / 2) * Math.sin(a2));
				g.drawLine(10 + size / 2, 10 + size / 2, x, y);

			}
		}

		// Set the position of the Zero
		//g.drawString("0", 2, setAllSizes + 10);

		// Paint focus if in focus
		g.setColor(getColorForValue());
		/*
		if (hasFocus()) {
		} else {
			g.setColor(Color.white);
		}
		*/

		g.fillOval(10, 10, size, size);
		g.setColor(Color.gray);
		g.fillOval(14 + SHADOWX, 14 + SHADOWY, size - 8, size - 8);

		g.setColor(Color.black);
		g.drawArc(10, 10, size, size, 315, 270);
		g.fillOval(14, 14, size - 8, size - 8);
		g.setColor(Color.white);

		int x = 10 + size / 2 + (int) (size / 2 * Math.cos(ang));
		int y = 10 + size / 2 - (int) (size / 2 * Math.sin(ang));
		g.drawLine(10 + size / 2, 10 + size / 2, x, y);
		g.setColor(Color.gray);
		int s2 = (int) Math.max(size / 6, 6);
		g.drawOval(10 + s2, 10 + s2, size - s2 * 2, size - s2 * 2);

		int dx = (int) (2 * Math.sin(ang));
		int dy = (int) (2 * Math.cos(ang));
		g.drawLine(10 + dx + size / 2, 10 + dy + size / 2, x, y);
		g.drawLine(10 - dx + size / 2, 10 - dy + size / 2, x, y);
	}
}
