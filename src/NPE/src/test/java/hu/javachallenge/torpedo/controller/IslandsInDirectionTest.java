package hu.javachallenge.torpedo.controller;

import java.util.Arrays;
import java.util.Collection;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

import hu.javachallenge.torpedo.model.Game;
import hu.javachallenge.torpedo.model.MapConfiguration;
import hu.javachallenge.torpedo.model.Position;
import hu.javachallenge.torpedo.response.GameInfoResponse;
import hu.javachallenge.torpedo.util.MathUtil;

@RunWith(Parameterized.class)
public class IslandsInDirectionTest extends AbstractTest {
	
	private GameInfoResponse gameInfoResponse;
	private Position sourcePosition;
	private double angle;
	private int expected;
	
	public IslandsInDirectionTest(GameInfoResponse gameInfoResponse, Position sourcePosition, double angle, int expected) {
		super();
		this.gameInfoResponse = gameInfoResponse;
		this.sourcePosition = sourcePosition;
		this.angle = angle;
		this.expected = expected;
	}

	@Parameters(name = "{index}: islandsInDirection(gameInfoResponse:({0}), sourcePosition:({1}, {2}), angle: {3})={4}")
	public static Collection<Object[]> generateData() {
		return Arrays.asList(
			new Object[][] {
				{ fakeGameInfoResponse(10, new Position[] {new Position(250, 250)}), new Position(0, 0), 45, 1 },
				{ fakeGameInfoResponse(10, new Position[] {new Position(-250, 250)}), new Position(0, 0), 135, 1 },
				{ fakeGameInfoResponse(10, new Position[] {new Position(0, 250)}), new Position(0, 0), 90, 1 }
			}
		);
	}
	
	public static GameInfoResponse fakeGameInfoResponse(int islandSize, Position[] islandPositions) {
		GameInfoResponse gameInfoResponse = new GameInfoResponse();
		Game game = new Game();
		MapConfiguration mapConfiguration = new MapConfiguration();
		
		mapConfiguration.setIslandSize(islandSize);
		mapConfiguration.setIslandPositions(islandPositions);
		
		game.setMapConfiguration(mapConfiguration);
		gameInfoResponse.setGame(game);
		return gameInfoResponse;
	}
	
	@Test
	public void testIslandsInDirection() {
		Assert.assertEquals(expected, MathUtil.islandsInDirection(gameInfoResponse, sourcePosition, angle).size());
	}
}
