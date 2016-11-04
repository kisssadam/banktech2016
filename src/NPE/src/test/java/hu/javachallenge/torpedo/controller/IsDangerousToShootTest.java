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
public class IsDangerousToShootTest extends AbstractTest {

	private double torpedoHitPenalty;
	private Position submarinePosition;
	private double submarineSize;
	private double submarineVelocity;
	private double submarineAngle;
	private Position targetPosition;
	private double targetVelocity;
	private double targetAngle;
	private double torpedoVelocity;
	private double torpedoAngle;
	private double torpedoRange;
	private boolean expected;
	
	public IsDangerousToShootTest(double torpedoHitPenalty, Position submarinePosition, double submarineSize,
			double submarineVelocity, double submarineAngle, Position targetPosition, double targetVelocity,
			double targetAngle, double torpedoVelocity, double torpedoAngle, double torpedoRange, boolean expected) {
		super();
		this.torpedoHitPenalty = torpedoHitPenalty;
		this.submarinePosition = submarinePosition;
		this.submarineSize = submarineSize;
		this.submarineVelocity = submarineVelocity;
		this.submarineAngle = submarineAngle;
		this.targetPosition = targetPosition;
		this.targetVelocity = targetVelocity;
		this.targetAngle = targetAngle;
		this.torpedoVelocity = torpedoVelocity;
		this.torpedoAngle = torpedoAngle;
		this.torpedoRange = torpedoRange;
		this.expected = expected;
	}

	@Parameters(name = "{index}: isDangerousToShoot()={11}")
	public static Collection<Object[]> generateData() {
		return Arrays.asList(
			new Object[][] {
				{ 5, new Position(20, 20), 10, 5, 90, new Position(20, 50), 5, 90, 10, 90, 11, true },
				{ 5, new Position(20, 20), 10, 5, 90, new Position(20, 50), 5, 90, 5, 90, 11, false }
			}
		);
	}

	@Test
	public void testDistance() {
		boolean actual = Main.isDangerousToShoot(torpedoHitPenalty, submarinePosition, submarineSize, submarineVelocity, submarineAngle, targetPosition, targetVelocity, targetAngle, torpedoVelocity, torpedoAngle, torpedoRange);
		Assert.assertEquals(expected, actual);
	}
	
}
