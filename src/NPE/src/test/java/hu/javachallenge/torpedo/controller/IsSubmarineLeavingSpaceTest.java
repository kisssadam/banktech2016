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
public class IsSubmarineLeavingSpaceTest {

	private Position submarinePosition;
	private double submarineSize;
	private double submarineVelocity;
	private double submarineAngle;
	private double width;
	private double height;
	private double maxAccelerationPerRound;
	private boolean expected;
	
	public IsSubmarineLeavingSpaceTest(Position submarinePosition, double submarineSize, double submarineVelocity,
			double submarineAngle, double width, double height, double maxAccelerationPerRound, boolean expected) {
		super();
		this.submarinePosition = submarinePosition;
		this.submarineSize = submarineSize;
		this.submarineVelocity = submarineVelocity;
		this.submarineAngle = submarineAngle;
		this.width = width;
		this.height = height;
		this.maxAccelerationPerRound = maxAccelerationPerRound;
		this.expected = expected;
	}
	

	@Parameters(name = "{index}: isSubmarineLeavingSpace(submarinePos: ({0}, {1}) subSize: {2}, subVel: {3}, subAngle: {4}, width: {5}, height: {6}, maxAccPerRound: {7})={8}")
	public static Collection<Object[]> generateData() {
		return Arrays.asList(new Object[][] {
			{ new Position(200, 200), 10, 50, 180, 1200, 700, 5, true },
			{ new Position(200, 200), 10, 50, 270, 1200, 700, 5, true },
			{ new Position(200, 200), 10, 10, 270, 1200, 700, 5, false }
		});
	}

	@Test
	public void testIsSubmarineLeavingSpaceTest() {
		boolean actual = Main.isSubmarineLeavingSpace(submarinePosition, submarineSize, submarineVelocity, submarineAngle, width, height, maxAccelerationPerRound); 
		Assert.assertEquals(expected, actual);
	}
	
}
