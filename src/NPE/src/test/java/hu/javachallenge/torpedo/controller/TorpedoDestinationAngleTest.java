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
public class TorpedoDestinationAngleTest extends AbstractTest {

	private Position sourcePosition;
	private Position destinationPosition;
	private double expected;

	public TorpedoDestinationAngleTest(double srcX, double srcY, double destX, double destY, double expected) {
		this(new Position(srcX, srcY), new Position(destX, destY), expected);
	}
	
	private TorpedoDestinationAngleTest(Position sourcePosition, Position destinationPosition, double expected) {
		this.sourcePosition = sourcePosition;
		this.destinationPosition = destinationPosition;
		this.expected = expected;
	}

	@Parameters(name = "{index}: torpedoDestinationAngle(srcPos:({0}, {1}), destPos:({2}, {3}))={4}")
	public static Collection<Object[]> generateData() {
		return Arrays.asList(
			new Object[][] {
				// basic test cases
				{ 0, 0, 1, 0, 0 },
				{ 0, 0, 0, 1, 90 },
				{ 0, 0, -1, 0, 180 },
				{ 0, 0, 0, -1, 270 },
				
				// extreme test cases
				{ 3, 1, 9, 3, 18.434948822922010648427806279547 },
				{ 0, 0, Math.sqrt(3) / 2, 1.0 / 2.0, 30.0 },
				{ 0, 1, 0, 11, 90.0 },
				{ 0, 0, -1.0 / 2.0, Math.sqrt(3) / 2, 120 },
				{ 0, 0, -Math.sqrt(3) / 2.0, -1.0 / 2.0, 210 },
				{ 0, 0, Math.sqrt(3) / 2.0, -1.0 / 2.0, 330 },
				
				// extreme test cases, with x += 2, y += 3
				{ 3 + 2, 1 + 3, 9 + 2, 3 + 3, 18.434948822922010648427806279547 },
				{ 0 + 2, 0 + 3, Math.sqrt(3) / 2 + 2, 1.0 / 2.0 + 3, 30.0 },
				{ 0 + 2, 1 + 3, 0 + 2, 11 + 3, 90.0 },
				{ 0 + 2, 0 + 3, -1.0 / 2.0 + 2, Math.sqrt(3) / 2 + 3, 120 },
				{ 0 + 2, 3 + 3, -Math.sqrt(3) / 2.0 + 2, -1.0 / 2.0 + 3 + 3, 210 },
				{ 0 + 2, 0 + 3, Math.sqrt(3) / 2.0 + 2, -1.0 / 2.0 + 3, 330 }
			}
		);
	}

	@Test
	public void testTorpedoDestinationAngle() {
		Assert.assertEquals(expected, Main.torpedoDestinationAngle(sourcePosition, destinationPosition), TestConstants.DELTA);
	}

}
