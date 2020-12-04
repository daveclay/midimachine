package com.daveclay.midi.realtime.ui.util;

import java.awt.*;

/**
*/
public class Size {

	private Container[] components;
	// or
	private Container component;

	private int heightMargin;
	private int widthMargin;
	private boolean preferredSize;

	Size() {
	}

	public static Size fit(Container... components) {
		Size size = new Size();
		size.components = components;
		return size;
	}

	public Size withMargin(int margin) {
		this.heightMargin = margin;
		this.widthMargin = margin;
		return this;
	}

	public Size withWidthMargin(int widthMargin) {
		this.widthMargin = widthMargin;
		return this;
	}

	public Size withHeightMargin(int heightMargin) {
		this.heightMargin = heightMargin;
		return this;
	}

	public void in(Container container) {
		int width = 0;
		int height = 0;

		Insets insets = container.getInsets();
		for (Container component : components) {
			Point location = component.getLocation();
			Dimension size = component.getPreferredSize();

			int componentWidth = location.x + size.width + insets.left;
			if (componentWidth > width) {
				width = componentWidth;
			}

			int componentHeight = location.y + size.height + insets.top;
			if (componentHeight > height) {
				height = componentHeight;
			}
		}

		Util.setAllSizes(container, width + widthMargin, height + heightMargin);
	}

	public static Size setPreferredSizeOf(Container component) {
		Size size = new Size();
		size.component = component;
		size.preferredSize = true;
		return size;
	}

	public static Size sizeComponent(Container component) {
		Size size = new Size();
		size.component = component;
		return size;
	}

	public void to(int width, int height) {
		if (preferredSize) {
			setPreferredSize(this.component, width, height);
		} else {
			size(this.component, width, height);
		}
	}

	public static void size(Container component, int width, int height) {
		setPreferredSize(component, width, height);
		setMinSize(component, width, height);
		setMaxSize(component, width, height);
	}

	public static void setMinSize(Container component, int width, int height) {
		component.setMinimumSize(asDimension(width, height));
	}

	public static void setMaxSize(Container component, int width, int height) {
		component.setMaximumSize(asDimension(width, height));
	}

	public static void setPreferredSize(Container component, int width, int height) {
		component.setPreferredSize(asDimension(width, height));
	}

	public static Dimension asDimension(int width, int height) {
		return new Dimension(width, height);
	}
}
