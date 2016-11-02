package hu.javachallenge.torpedo.controller;

import java.util.Arrays;
import java.util.Collection;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

import hu.javachallenge.torpedo.model.Position;

@RunWith(Parameterized.class)
public class DistanceTest {

	private Position srcPos;
	private double srcR;
	private Position destPos;
	private double destR;
	private double expected;

	public DistanceTest(double srcX, double srcY, double srcR, double destX, double destY, double destR, double expected) {
		this(new Position(srcX, srcY), srcR, new Position(destX, destY), destR, expected);
	}

	private DistanceTest(Position srcPos, double srcR, Position destPos, double destR, double expected) {
		super();
		this.srcPos = srcPos;
		this.srcR = srcR;
		this.destPos = destPos;
		this.destR = destR;
		this.expected = expected;
	}

	@Parameters(name = "{index}: distance(srcPos:({0}, {1}), srcR: {2}, destPos:({3}, {4}), destR: {5})={6}")
	public static Collection<Object[]> generateData() {
		return Arrays.asList(
			new Object[][] {
				{ 1, 1, 5, 1, 20, 6, 8 },
				{ 3, 1, 5, 1, 20, 6, 8.1049731745428001791682957524967 },
				{ 0, 0, 100, 150, -150, 50, 62.132034356 }
			}
		);
	}

	@Test
	public void testDistance() {
		Assert.assertEquals(expected, Main.distance(srcPos, srcR, destPos, destR), TestConstants.DELTA);
	}

}
