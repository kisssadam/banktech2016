package hu.javachallenge.torpedo.controller;

import java.util.Arrays;
import java.util.Collection;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

import hu.javachallenge.torpedo.model.Position;
import hu.javachallenge.torpedo.util.MathUtil;

@RunWith(Parameterized.class)
public class IsSubmarineLeavingSpaceTest extends AbstractTest {

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
	

	@Parameters(name = "{index}: isSubmarineLeavingSpace(submarinePos: ({0}) subSize: {1}, subVel: {2}, subAngle: {3}, width: {4}, height: {5}, maxAccPerRound: {6})={7}")
	public static Collection<Object[]> generateData() {
		return Arrays.asList(new Object[][] {
			{ new Position(200, 200), 10, 50, 180, 1200, 700, 5, true },
			{ new Position(200, 200), 10, 50, 270, 1200, 700, 5, true },
			{ new Position(200, 200), 10, 10, 270, 1200, 700, 5, false },
			{ new Position(20, 20), 10, 10, 270, 1200, 700, 10, true }
		});
	}

	@Test
	public void testIsSubmarineLeavingSpaceTest() {
		boolean actual = MathUtil.isSubmarineLeavingSpace(submarinePosition, submarineSize, submarineVelocity, submarineAngle, width, height, maxAccelerationPerRound); 
		Assert.assertEquals(expected, actual);
	}
	
}
