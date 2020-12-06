package com.daveclay.midi.realtime.ui.util;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import static junit.framework.Assert.assertEquals;
import static org.junit.Assert.fail;

/**
 */
public class GradientValueMapTest {

	@Test
	public void exceedsValue() {
		GradientValuePoint[] points = singleGradientValueMap.getGradientValuePointForValue(51);
		assertPoints(new GradientValuePoint[] { d, e }, points);
	}

	@Test
	public void fallsOnA() {
		GradientValuePoint[] points = singleGradientValueMap.getGradientValuePointForValue(0);
		assertPoints(new GradientValuePoint[] { a, b }, points);
	}

	@Test
	public void fallsBetweenAandB() {
		GradientValuePoint[] points = singleGradientValueMap.getGradientValuePointForValue(4);
		assertPoints(new GradientValuePoint[] { a, b }, points);
	}

	@Test
	public void fallsOnB() {
		GradientValuePoint[] points = singleGradientValueMap.getGradientValuePointForValue(10);
		assertPoints(new GradientValuePoint[] { b, c }, points);

	}

	@Test
	public void fallsOnD() {
		GradientValuePoint[] points = singleGradientValueMap.getGradientValuePointForValue(50);
		assertPoints(new GradientValuePoint[] { d, e }, points);
	}

	@Test
	public void fallsBetweenC() {
		GradientValuePoint[] points = singleGradientValueMap.getGradientValuePointForValue(21);
		assertPoints(new GradientValuePoint[] { c, d }, points);
	}

	private void assertPoints(GradientValuePoint[] expected, GradientValuePoint[] actual) {
		Assert.assertEquals(expected.length, actual.length);
		for (int i = 0; i < expected.length; i++) {
			Assert.assertEquals("wrong point at " + i, expected[i], actual[i]);
		}
	}

	@Test
	public void test() {
		int color = singleGradientValueMap.getColorAtValue(0);
		Assert.assertEquals(0, color);
	}

	@Test
	public void testMiddle() {
		int color = singleGradientValueMap.getColorAtValue(5);
		Assert.assertEquals(10, color);
	}

	@Test
	public void testInterp() {
		int color = singleGradientValueMap.getColorAtValue(9);
		Assert.assertEquals(18, color);
	}

	@Test
	public void testInterp2() {
		int color = singleGradientValueMap.getColorAtValue(30);
		assertEquals(60, color);
	}

	@Test
	public void testInterpDown() {
		int color = singleGradientValueMap.getColorAtValue(45);
		Assert.assertEquals(40, color);
	}

	@Test
	public void testInterpToZero() {
		int color = singleGradientValueMap.getColorAtValue(50);
		Assert.assertEquals(0, color);
	}

	@Test
	public void testOnlyTwoValues() {
		singleGradientValueMap = new SingleGradientValueMap();
		singleGradientValueMap.addPoint(.3, 0);
		singleGradientValueMap.addPoint(.6, 255);
		
		int color = singleGradientValueMap.getColorAtValue(0);
		assertEquals(0, color);

		color = singleGradientValueMap.getColorAtValue(.8);
		assertEquals(255, color);
	}

	@Before
	public void setUp() throws Exception {
		singleGradientValueMap = new SingleGradientValueMap();

		a.value = 0;
		a.colorValue = 0;

		b.value = 10;
		b.colorValue = 20;

		c.value = 20;
		c.colorValue = 40;

		d.value = 40;
		d.colorValue = 80;

		e.value = 50;
		e.colorValue = 0;

		singleGradientValueMap.setGradientValuePoints(new GradientValuePoint[] { a, b, c, d, e });
	}

	SingleGradientValueMap singleGradientValueMap;

	GradientValuePoint a = new GradientValuePoint();
	GradientValuePoint b = new GradientValuePoint();
	GradientValuePoint c = new GradientValuePoint();
	GradientValuePoint d = new GradientValuePoint();
	GradientValuePoint e = new GradientValuePoint();
}
