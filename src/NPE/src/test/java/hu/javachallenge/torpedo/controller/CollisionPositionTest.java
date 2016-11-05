package hu.javachallenge.torpedo.controller;

import java.util.Arrays;
import java.util.Collection;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

import hu.javachallenge.torpedo.model.Position;
import hu.javachallenge.torpedo.util.MathConstants;

@RunWith(Parameterized.class)
public class CollisionPositionTest extends AbstractTest {

	private int submarineSize;
	private Position submarinePosition;
	private double submarineVelocity;
	private Position torpedoPosition;
	private double torpedoVelocity;
	private double torpedoAngle;
	private double submarineAngle;
	private Position expected;

	public CollisionPositionTest(int submarineSize, Position submarinePosition, double submarineVelocity, double submarineAngle,
			Position torpedoPosition, double torpedoVelocity, double torpedoAngle, Position expected) {
		super();
		this.submarineSize = submarineSize;
		this.submarinePosition = submarinePosition;
		this.submarineVelocity = submarineVelocity;
		this.submarineAngle = submarineAngle;
		this.torpedoPosition = torpedoPosition;
		this.torpedoVelocity = torpedoVelocity;
		this.torpedoAngle = torpedoAngle;
		this.expected = expected;
	}

	@Parameters(name = "{index}: collisionPosition(submarineSize: {0}, submarinePos: ({1}, {2}), submarineVelocity: {3}, submarineAngle: {4}, torpedoPos: ({5}, {6}), torpedoVelocity: {7}, torpedoAngle: {8})={9}")
	public static Collection<Object[]> generateData() {
		return Arrays.asList(new Object[][] {
			{ 1, new Position(2, 3), 1, 180, new Position(0, 0), 1, 90, new Position(0, 2) }
		});
	}

	@Test
	public void testCollisionPosition() {
		 Position actual = Main.collisionPosition(submarineSize, submarinePosition, submarineVelocity, submarineAngle, torpedoPosition, torpedoVelocity, torpedoAngle);
		 Assert.assertEquals(expected.getX().doubleValue(), actual.getX().doubleValue(), MathConstants.EPSILON);
		 Assert.assertEquals(expected.getY().doubleValue(), actual.getY().doubleValue(), MathConstants.EPSILON);
	}

}
