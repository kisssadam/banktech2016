package hu.javachallenge.torpedo.controller;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Set;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

import hu.javachallenge.torpedo.model.Entity;
import hu.javachallenge.torpedo.model.Game;
import hu.javachallenge.torpedo.model.MapConfiguration;
import hu.javachallenge.torpedo.model.Position;
import hu.javachallenge.torpedo.model.Submarine;
import hu.javachallenge.torpedo.response.GameInfoResponse;
import hu.javachallenge.torpedo.util.DangerType;
import hu.javachallenge.torpedo.util.MathUtil;

import org.junit.Assert;

@RunWith(Parameterized.class)
public class GetDangerTypesTest extends AbstractTest {

	private GameInfoResponse gameInfo;
	private Position submarinePosition;
	private double submarineVelocity;
	private double submarineAngle;
	private List<Entity> torpedos;
	private List<Submarine> enemySubmarines;
	private int expectedSize;
	
	public GetDangerTypesTest(GameInfoResponse gameInfo, Position submarinePosition, double submarineVelocity,
			double submarineAngle, List<Entity> torpedos, List<Submarine> enemySubmarines, int expectedSize) {
		super();
		this.gameInfo = gameInfo;
		this.submarinePosition = submarinePosition;
		this.submarineVelocity = submarineVelocity;
		this.submarineAngle = submarineAngle;
		this.torpedos = torpedos;
		this.enemySubmarines = enemySubmarines;
		this.expectedSize = expectedSize;
	}

	private static GameInfoResponse fakeGameInfo() {
		MapConfiguration mapConfiguration = new MapConfiguration();
		mapConfiguration.setIslandPositions(new Position[] { new Position(450, 450), new Position(1275, 450) });
		mapConfiguration.setWidth(1700);
		mapConfiguration.setHeight(900);
		mapConfiguration.setIslandSize(100);
		mapConfiguration.setSubmarineSize(15);
		mapConfiguration.setTorpedoSpeed(40);
		mapConfiguration.setTorpedoExplosionRadius(50);
		mapConfiguration.setMaxAccelerationPerRound(5);
		mapConfiguration.setMaxSpeed(20);
		
		Game game = new Game();
		game.setMapConfiguration(mapConfiguration);
		
		GameInfoResponse gameInfoResponse = new GameInfoResponse();
		gameInfoResponse.setGame(game);
		
		return gameInfoResponse;
	}
	
	@Parameters(name = "{index}: getDangerTypeTest()")
	public static Collection<Object[]> generateData() {
		return Arrays.asList(
			new Object[][] {
				{ fakeGameInfo(), new Position(100, 1675), 0, 359, Collections.emptyList(), Collections.emptyList(), 0 }, 
				{ fakeGameInfo(), new Position(100, 1675), 0, 330, Collections.emptyList(), Collections.emptyList(), 0 },
				{ fakeGameInfo(), new Position(40, 1675), 0, 330, Collections.emptyList(), Collections.emptyList(), 0 },
				{ fakeGameInfo(), new Position(40, 1675), 0, 160, Collections.emptyList(), Collections.emptyList(), 1 }
			}
		);
	}
	
	@Test
	public void testGetDangerTypes() {
		Set<DangerType> dangerTypes = MathUtil.getDangerTypes(gameInfo, submarinePosition, submarineVelocity, submarineAngle, torpedos, enemySubmarines);
		Assert.assertEquals(expectedSize, dangerTypes.size());
	}
	
}
