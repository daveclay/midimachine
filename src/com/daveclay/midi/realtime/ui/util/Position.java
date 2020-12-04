package com.daveclay.midi.realtime.ui.util;

import java.awt.*;

/**
*/
public class Position {

	private enum Direction {
		RIGHT {
			@Override
			public Point getPoint(Position position) {
				Point point = position.relativeToComponent.getLocation();
				Dimension dimension = position.relativeToComponent.getPreferredSize();
				Rectangle bounds = position.relativeToComponent.getBounds();
				return new Point(bounds.x + bounds.width + position.margin, bounds.y);
			}},
		LEFT {
			@Override
			public Point getPoint(Position position) {
				Rectangle bounds = position.relativeToComponent.getBounds();
				int x = bounds.x - (int) position.component.getPreferredSize().getWidth();
				return new Point(x + position.margin, bounds.y);
			}},
		ABOVE {
			@Override
			public Point getPoint(Position position) {
				Rectangle bounds = position.relativeToComponent.getBounds();
				int y = bounds.y - (int) position.component.getPreferredSize().getHeight();
				return new Point(bounds.x, y - position.margin);
			}},
		BELOW {
			@Override
			public Point getPoint(Position position) {
				Rectangle bounds = position.relativeToComponent.getBounds();
				int y = bounds.height + bounds.y;

				return new Point(bounds.x, y + position.margin);
			}};

		public abstract Point getPoint(Position position);
	}

	private Container component;
	private Container relativeToComponent;
	private Direction direction;
	private Point point;
	private int margin;

	private Position() {
	}

	public static Position position(Container component) {
		Position position = new Position();
		position.component = component;
		return position;
	}

	public Position below(Container otherComponent) {
		return relativeTo(otherComponent, Direction.BELOW);
	}

	public Position toTheLeftOf(Container otherComponent) {
		return relativeTo(otherComponent, Direction.LEFT);
	}

	public Position above(Container otherComponent) {
		return relativeTo(otherComponent, Direction.ABOVE);
	}

	public Position toTheRightOf(Container otherComponent) {
		return relativeTo(otherComponent, Direction.RIGHT);
	}

	private Position relativeTo(Container otherComponent, Direction direction) {
		this.relativeToComponent = otherComponent;
		this.direction = direction;
		return this;
	}

	public Position withMargin(int margin) {
		this.margin = margin;
		return this;
	}

	public void in(Container container) {
		Point loc;
		if (point != null) {
			loc = point;
		} else if (direction != null) {
			loc = direction.getPoint(this);
			// adjust for container insets - this will be re-applied after.
			Insets insets = container.getInsets();
			loc = new Point(loc.x - insets.left, loc.y - insets.top);
		} else {
			throw new IllegalStateException("I don't know what you want.");
		}

		container.setLayout(null);
		container.add(component);
		position(container, component, loc.x, loc.y);
	}

	public Position at(int x, int y) {
		this.point = new Point(x, y);
		return this;
	}

	public Position atOrigin() {
		return at(0, 0);
	}

	public static void position(Container container, Container component, int x, int y) {
		Insets insets = container.getInsets();
		Dimension size = component.getPreferredSize();
		component.setBounds(x + insets.left, y + insets.top, size.width, size.height);
	}
}
