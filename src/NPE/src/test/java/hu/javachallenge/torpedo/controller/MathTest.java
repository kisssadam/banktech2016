package hu.javachallenge.torpedo.controller;

import java.math.BigDecimal;

import org.junit.Assert;
import org.junit.Test;

import hu.javachallenge.torpedo.model.Position;

public class MathTest {

	@Test
	public void distanceTest1() {
		Position sourcePosition = new Position(BigDecimal.valueOf(1), BigDecimal.valueOf(1));
		double sourceR = 5.0;

		Position destinationPosition = new Position(BigDecimal.valueOf(1), BigDecimal.valueOf(20));
		double destinationR = 6.0;

		Assert.assertEquals(8, Main.distance(sourcePosition, sourceR, destinationPosition, destinationR), 0.00001);
	}
	
	@Test
	public void distanceTest2() {
		Position sourcePosition = new Position(BigDecimal.valueOf(3), BigDecimal.valueOf(1));
		double sourceR = 5.0;
		
		Position destinationPosition = new Position(BigDecimal.valueOf(1), BigDecimal.valueOf(20));
		double destinationR = 6.0;
		
		final double expected = 8.1049731745428001791682957524967;
		Assert.assertEquals(expected, Main.distance(sourcePosition, sourceR, destinationPosition, destinationR), 0.00001);
	}

}
