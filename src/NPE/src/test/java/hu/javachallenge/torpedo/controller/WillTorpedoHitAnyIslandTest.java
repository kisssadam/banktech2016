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
public class WillTorpedoHitAnyIslandTest extends AbstractTest {

	private Position[] islandPositions;
	private double islandSize;
	private Position torpedoPosition;
	private double torpedoRange;
	private double torpedoVelocity;
	private double torpedoAngle;
	private boolean expected;

	public WillTorpedoHitAnyIslandTest(Position[] islandPositions, double islandSize, Position torpedoPosition,
			double torpedoRange, double torpedoVelocity, double torpedoAngle, boolean expected) {
		super();
		this.islandPositions = islandPositions;
		this.islandSize = islandSize;
		this.torpedoPosition = torpedoPosition;
		this.torpedoRange = torpedoRange;
		this.torpedoVelocity = torpedoVelocity;
		this.torpedoAngle = torpedoAngle;
		this.expected = expected;
	}

	@Parameters(name = "{index}: willTorpedoHitAnyIsland(islandPositions:[{0}], islandSize: {1}, torpedoPos:({2}, {3}), torpedoRange: {4}, torpedoAngle: {5})={6}")
	public static Collection<Object[]> generateData() {
		return Arrays.asList(
			new Object[][] {
				{ new Position[] { new Position(20, 20) }, 10, new Position(20, 40), 1, 21, 270, true },
				{ new Position[] { new Position(20, 20) }, 10, new Position(20, 40), 1, 20, 270, true },
				{ new Position[] { new Position(20, 20) }, 10, new Position(20, 40), 1, 19, 270, true },
				{ new Position[] { new Position(20, 20) }, 10, new Position(20, 40), 1, 10.01, 270, true },
				{ new Position[] { new Position(20, 20) }, 10, new Position(20, 40), 2, 10, 270, true },
				{ new Position[] { new Position(20, 20) }, 10, new Position(20, 40), 2, 10, 90, false },
				{ new Position[] { new Position(20, 20) }, 10, new Position(2000, 4000), 2, 10, 270, false },
			}
		);
	}

	@Test
	public void testDistance() {
		boolean actual = Main.willTorpedoHitAnyIsland(islandPositions, islandSize, torpedoPosition, torpedoRange, torpedoVelocity, torpedoAngle);
		Assert.assertEquals(expected, actual);
	}
	
}
