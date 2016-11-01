package hu.javachallenge.torpedo.controller;

import java.math.BigDecimal;

import org.junit.Assert;
import org.junit.Test;

import hu.javachallenge.torpedo.model.Position;

public class MathTest {

	private static final double DELTA = 0.00000001;

	@Test
	public void distanceTests() {
		distanceTest(1, 1, 5, 1, 20, 6, 8);
		distanceTest(3, 1, 5, 1, 20, 6, 8.1049731745428001791682957524967);
	}

	private void distanceTest(double srcX, double srcY, double srcR, double destX, double destY, double destR,
			double expected) {
		distanceTest(BigDecimal.valueOf(srcX), BigDecimal.valueOf(srcY), srcR, BigDecimal.valueOf(destX),
				BigDecimal.valueOf(destY), destR, expected);
	}

	private void distanceTest(BigDecimal srcX, BigDecimal srcY, double srcR, BigDecimal destX, BigDecimal destY,
			double destR, double expected) {
		distanceTest(new Position(srcX, srcY), srcR, new Position(destX, destY), destR, expected);
	}

	private void distanceTest(Position srcPos, double srcR, Position destPos, double destR, double expected) {
		Assert.assertEquals(expected, Main.distance(srcPos, srcR, destPos, destR), DELTA);
	}

	@Test
	public void torpedoDestinationAngleTests() {
		torpedoDestinationAngleTest(0, 0, 1, 0, 0);
		torpedoDestinationAngleTest(0, 0, 0, 1, 90);
		torpedoDestinationAngleTest(0, 0, -1, 0, 180);
		torpedoDestinationAngleTest(0, 0, 0, -1, 270);

		// extreme test cases
		torpedoDestinationAngleTest(3, 1, 9, 3, 18.434948822922010648427806279547);
		torpedoDestinationAngleTest(0, 0, Math.sqrt(3) / 2, 1.0 / 2.0, 30.0);
		torpedoDestinationAngleTest(0, 1, 0, 11, 90.0);
		torpedoDestinationAngleTest(0, 0, -1.0 / 2.0, Math.sqrt(3) / 2, 120);
		torpedoDestinationAngleTest(0, 0, -Math.sqrt(3) / 2.0, -1.0 / 2.0, 210);
		torpedoDestinationAngleTest(0, 0, Math.sqrt(3) / 2.0, -1.0 / 2.0, 330);

		torpedoDestinationAngleTest(3 + 2, 1 + 3, 9 + 2, 3 + 3, 18.434948822922010648427806279547);
		torpedoDestinationAngleTest(0 + 2, 0 + 3, Math.sqrt(3) / 2 + 2, 1.0 / 2.0 + 3, 30.0);
		torpedoDestinationAngleTest(0 + 2, 1 + 3, 0 + 2, 11 + 3, 90.0);
		torpedoDestinationAngleTest(0 + 2, 0 + 3, -1.0 / 2.0 + 2, Math.sqrt(3) / 2 + 3, 120);
		torpedoDestinationAngleTest(0 + 2, 3 + 3, -Math.sqrt(3) / 2.0 + 2, -1.0 / 2.0 + 3 + 3, 210);
		torpedoDestinationAngleTest(0 + 2, 0 + 3, Math.sqrt(3) / 2.0 + 2, -1.0 / 2.0 + 3, 330);
	}

	private void torpedoDestinationAngleTest(double sourceX, double sourceY, double destX, double destY,
			double expected) {
		Position sourcePosition = new Position(BigDecimal.valueOf(sourceX), BigDecimal.valueOf(sourceY));
		Position destinationPosition = new Position(BigDecimal.valueOf(destX), BigDecimal.valueOf(destY));

		torpedoDestinationAngleTest(sourcePosition, destinationPosition, expected);
	}

	private void torpedoDestinationAngleTest(Position sourcePosition, Position destinationPosition, double expected) {
		Assert.assertEquals(expected, Main.torpedoDestinationAngle(sourcePosition, destinationPosition), DELTA);
	}

}
