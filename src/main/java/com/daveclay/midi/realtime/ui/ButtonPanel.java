package com.daveclay.midi.realtime.ui;

import javax.swing.*;
import javax.swing.border.EtchedBorder;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import static com.daveclay.midi.realtime.ui.util.MouseEventHelper.isMouseButtonDown;

public abstract class ButtonPanel extends JPanel implements MouseListener {

	public static final Color DEFAULT_SELECTED_COLOR = new Color(128, 128, 128);
	public static final Color DEFAULT_UNSELECTED_COLOR = new Color(230, 230, 230);
	private boolean selected;
	private boolean selectOnly;
	private Color unselectedColor = DEFAULT_UNSELECTED_COLOR;
	private Color selectedColor = DEFAULT_SELECTED_COLOR;

	public ButtonPanel() {
		setBorder(BorderFactory.createEtchedBorder(EtchedBorder.LOWERED));
		addMouseListener(this);
	}

	public void setSelectOnly(boolean selectable) {
		this.selectOnly = selectable;
	}

	@Override
	public void mouseEntered(MouseEvent mouseEvent) {
		if (isMouseButtonDown(mouseEvent)) {
			mouseEnteredWithButtonDown();
		}
	}

	public abstract void mouseEnteredWithButtonDown();

	@Override
	public void mousePressed(MouseEvent mouseEvent) {
		mousePressed();
	}

	@Override
	public void mouseReleased(MouseEvent mouseEvent) {
		mouseReleased();
	}

	@Override
	public void mouseClicked(MouseEvent mouseEvent) {
	}

	@Override
	public void mouseExited(MouseEvent mouseEvent) {
	}

	public void mousePressed() {
		if (!selectOnly || !selected) {
			selectorPanelPressed();
		}
	}

	public abstract void selectorPanelPressed();

	public void mouseReleased() {
		if (selected) {
			if (!selectOnly) {
				deselectSelectorPanel();
			}
		} else {
			selectorPanelSelected();
		}
	}

	public abstract void deselectSelectorPanel();

	public abstract void selectorPanelSelected();

	public void setSelected(boolean selected) {
		this.selected = selected;
	}

	public void setButtonPressedUI() {
		setBorder(BorderFactory.createEtchedBorder(EtchedBorder.RAISED));
		Color pressedColor = getColor().darker();
		setBackground(pressedColor);
	}

	public void setButtonSelectStateUI() {
		setBorder(BorderFactory.createEtchedBorder(EtchedBorder.LOWERED));
		Color color = getColor();
		setBackground(color);
	}

	private Color getColor() {
		if (selected) {
			return selectedColor;
		} else {
			return unselectedColor;
		}
	}

	public void setSelectedColor(Color selectedColor) {
		this.selectedColor = selectedColor;
	}

	public void setUnselectedColor(Color unselectedColor) {
		this.unselectedColor = unselectedColor;
	}

	@Override
	public String toString() {
		return "DefaultButtonPanel@" + System.identityHashCode(this);
	}
}
