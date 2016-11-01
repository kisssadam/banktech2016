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
public class AimAtMovingTargetTest {

	private Position sourcePosition;
	private Position targetPosition;
	private double targetMovementAngle;
	private double targetVelocity;
	private double bulletVelocity;

	public AimAtMovingTargetTest(double srcX, double srcY, double destX, double destY, double targetMovementAngle,
			double targetVelocity, double bulletVelocity) {
		this(new Position(srcX, srcY), new Position(destX, destY), targetMovementAngle, targetVelocity, bulletVelocity);
	}

	private AimAtMovingTargetTest(Position sourcePosition, Position targetPosition, double targetMovementAngle,
			double targetVelocity, double bulletVelocity) {
		super();
		this.sourcePosition = sourcePosition;
		this.targetPosition = targetPosition;
		this.targetMovementAngle = targetMovementAngle;
		this.targetVelocity = targetVelocity;
		this.bulletVelocity = bulletVelocity;
	}

	@Parameters
	public static Collection<Object[]> generateData() {
		return Arrays.asList(
			new Object[][] {
				{ 1, 1, 4, 4, 45, 2, 5 }
			}
		);
	}
	
	@Test
	public void testAimAtMovingTarget() {
		double actual = Main.aimAtMovingTarget(sourcePosition, targetPosition, targetMovementAngle, targetVelocity, bulletVelocity);
		Assert.assertEquals(45.0, actual, TestConstants.DELTA);
	}

}
