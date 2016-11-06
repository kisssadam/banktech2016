package hu.javachallenge.torpedo.controller;

import static hu.javachallenge.torpedo.util.MathUtil.aimAtMovingTarget;
import static hu.javachallenge.torpedo.util.MathUtil.getNearestIslandInDirection;
import static hu.javachallenge.torpedo.util.MathUtil.isDangerousToShoot;
import static hu.javachallenge.torpedo.util.MathUtil.isSubmarineHeadingToIsland;
import static hu.javachallenge.torpedo.util.MathUtil.isSubmarineLeavingSpace;
import static hu.javachallenge.torpedo.util.MathUtil.islandsInDirection;
import static hu.javachallenge.torpedo.util.MathUtil.normalizeAngle;
import static hu.javachallenge.torpedo.util.MathUtil.normalizeVelocity;
import static hu.javachallenge.torpedo.util.MathUtil.xMovement;
import static hu.javachallenge.torpedo.util.MathUtil.yMovement;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import hu.javachallenge.torpedo.model.Entity;
import hu.javachallenge.torpedo.model.Position;
import hu.javachallenge.torpedo.model.Status;
import hu.javachallenge.torpedo.model.Submarine;
import hu.javachallenge.torpedo.response.CreateGameResponse;
import hu.javachallenge.torpedo.response.GameInfoResponse;
import hu.javachallenge.torpedo.response.GameListResponse;
import hu.javachallenge.torpedo.response.SonarResponse;
import hu.javachallenge.torpedo.response.SubmarinesResponse;

public class GameController implements Runnable {

	private static final Logger log = LoggerFactory.getLogger(GameController.class);
	
	private CallHandler callHandler;
	private String teamName;
	private GameInfoResponse gameInfo;
	
	private long gameId;
	private int torpedoHitPenalty;
	private double submarineSize;
	private double torpedoSpeed;
	private double torpedoRange;
	private double width;
	private double height;
	private double maxAccelerationPerRound;
	private double maxSteeringPerRound;
	private double maxSpeed;
	private double islandSize;
	
	private long actualRound;
	private long previousRound;

	private List<Submarine> enemySubmarines;
	
	public GameController(CallHandler callHandler, String teamName) {
		this.callHandler = callHandler;
		this.teamName = teamName;
		this.enemySubmarines = new ArrayList<>();
	}

	@Override
	public void run() {
		startGame();
		playGame();
	}

	private void startGame() {
		GameListResponse gameList = callHandler.gameList();
		long[] games = gameList.getGames();
		
		Long gameId = null;
		
		if (games == null || games.length == 0) {
			CreateGameResponse createGameResponse = callHandler.createGame();
			gameId = createGameResponse.getId();
		} else {
			gameId = games[0];
		}
		
		gameInfo = callHandler.gameInfo(gameId);
		
		Map<String, Boolean> connected = gameInfo.getGame().getConnectionStatus().getConnected();
		if (connected.get(teamName) == false) {
			callHandler.joinGame(gameId);
		}
		
		do {
			gameInfo = callHandler.gameInfo(gameId);
		} while (gameInfo.getGame().getStatus().equals(Status.WAITING));
		
		this.gameId = gameInfo.getGame().getId();
		this.torpedoHitPenalty = gameInfo.getGame().getMapConfiguration().getTorpedoHitPenalty();
		this.submarineSize = gameInfo.getGame().getMapConfiguration().getSubmarineSize();
		this.torpedoSpeed = gameInfo.getGame().getMapConfiguration().getTorpedoSpeed();
		this.torpedoRange = gameInfo.getGame().getMapConfiguration().getTorpedoRange();
		this.width = gameInfo.getGame().getMapConfiguration().getWidth();
		this.height = gameInfo.getGame().getMapConfiguration().getHeight();
		this.maxAccelerationPerRound = gameInfo.getGame().getMapConfiguration().getMaxAccelerationPerRound();
		this.maxSteeringPerRound = gameInfo.getGame().getMapConfiguration().getMaxSteeringPerRound();
		this.maxSpeed = gameInfo.getGame().getMapConfiguration().getMaxSpeed();
		this.islandSize = gameInfo.getGame().getMapConfiguration().getIslandSize();
		this.actualRound = gameInfo.getGame().getRound();
		this.previousRound = actualRound - 1;
	}
	
	private void playGame() {
		while (!gameInfo.getGame().getStatus().equals(Status.ENDED)) {
			actualRound = gameInfo.getGame().getRound();
			if (actualRound > previousRound) {
				previousRound = actualRound;
				enemySubmarines.clear();

				SubmarinesResponse submarinesInGame = callHandler.submarinesInGame(gameId);
				for (Submarine submarine : submarinesInGame.getSubmarines()) {
					SonarResponse sonar = callHandler.sonar(gameId, submarine.getId());

					for (Entity entity : sonar.getEntities()) {
						switch (entity.getType()) {
						case "Submarine":
							if (!entity.getOwner().getName().equals(teamName)) {
								enemySubmarines.add(new Submarine("Submarine", entity.getId(), entity.getPosition(),
										entity.getOwner(), entity.getVelocity(), entity.getAngle(), 0, 0, 0, 0));
							}
							break;
						case "Torpedo":

							break;
						}
					}
				}
				log.trace("Enemy submarines: {}", enemySubmarines);
				
				for (Submarine submarine : submarinesInGame.getSubmarines()) {
					for (Submarine enemySubmarine : enemySubmarines) {
						double theta = aimAtMovingTarget(submarine.getPosition(), enemySubmarine.getPosition(),
								enemySubmarine.getAngle(), enemySubmarine.getVelocity(), torpedoSpeed);
						if (!isDangerousToShoot(torpedoHitPenalty, submarine.getPosition(), enemySubmarines,
								submarineSize, enemySubmarine.getPosition(), enemySubmarine.getVelocity(),
								enemySubmarine.getAngle(), torpedoSpeed, theta, torpedoRange)) {
							if (submarine.getTorpedoCooldown() == 0.0) {
								callHandler.shoot(gameId, submarine.getId(), normalizeAngle(theta));
								break;
							}
						}
					}
					
					boolean moved = false;
					
					if (isSubmarineLeavingSpace(submarine.getPosition(), submarineSize, submarine.getVelocity(), submarine.getAngle(), width, height, maxAccelerationPerRound)) {
						double plusAngle = normalizeAngle(submarine.getAngle() + maxSteeringPerRound);
						double minusAngle = normalizeAngle(submarine.getAngle() - maxSteeringPerRound);
						double minusVelocity = normalizeVelocity(submarine.getVelocity() - maxAccelerationPerRound, maxSpeed);
						
						if (!moved && !isSubmarineLeavingSpace(submarine.getPosition(), submarineSize, submarine.getVelocity(), plusAngle, width, height, maxAccelerationPerRound)) {
							Position newPosition = new Position(
									submarine.getPosition().getX().doubleValue() + xMovement(submarine.getVelocity(), plusAngle),
									submarine.getPosition().getY().doubleValue() + yMovement(submarine.getVelocity(), plusAngle));
							
							List<Position> islandsInDirection = islandsInDirection(gameInfo, newPosition, plusAngle);
							Position nearestIslandInDirection = getNearestIslandInDirection(islandsInDirection);
							
							if (!isSubmarineHeadingToIsland(nearestIslandInDirection, islandSize, newPosition, submarineSize, submarine.getVelocity(), plusAngle, maxAccelerationPerRound)) {
								callHandler.move(gameId, submarine.getId(), 0, maxSteeringPerRound);
								moved = true;
							}
						}
						if (!moved && !isSubmarineLeavingSpace(submarine.getPosition(), submarineSize, submarine.getVelocity(), minusAngle, width, height, maxAccelerationPerRound)) {
							Position newPosition = new Position(
									submarine.getPosition().getX().doubleValue() + xMovement(submarine.getVelocity(), minusAngle),
									submarine.getPosition().getY().doubleValue() + yMovement(submarine.getVelocity(), minusAngle));
							
							List<Position> islandsInDirection = islandsInDirection(gameInfo, newPosition, minusAngle);
							Position nearestIslandInDirection = getNearestIslandInDirection(islandsInDirection);
							
							if (!isSubmarineHeadingToIsland(nearestIslandInDirection, islandSize, newPosition, submarineSize, submarine.getVelocity(), minusAngle, maxAccelerationPerRound)) {
								callHandler.move(gameId, submarine.getId(), 0, -maxSteeringPerRound);
								moved = true;
							}
						}
						if (!moved && !isSubmarineLeavingSpace(submarine.getPosition(), submarineSize, minusVelocity, minusAngle, width, height, maxAccelerationPerRound)) {
							Position newPosition = new Position(
									submarine.getPosition().getX().doubleValue() + xMovement(minusVelocity, minusAngle),
									submarine.getPosition().getY().doubleValue() + yMovement(minusVelocity, minusAngle));
							
							List<Position> islandsInDirection = islandsInDirection(gameInfo, newPosition, minusAngle);
							Position nearestIslandInDirection = getNearestIslandInDirection(islandsInDirection);
							
							if (!isSubmarineHeadingToIsland(nearestIslandInDirection, islandSize, newPosition, submarineSize, minusVelocity, minusAngle, maxAccelerationPerRound)) {
								callHandler.move(gameId, submarine.getId(), minusVelocity - submarine.getVelocity(), -maxSteeringPerRound);
								moved = true;
							}
						}
						if (!moved && !isSubmarineLeavingSpace(submarine.getPosition(), submarineSize, minusVelocity, plusAngle, width, height, maxAccelerationPerRound)) {
							Position newPosition = new Position(
									submarine.getPosition().getX().doubleValue() + xMovement(minusVelocity, plusAngle),
									submarine.getPosition().getY().doubleValue() + yMovement(minusVelocity, plusAngle));
							
							List<Position> islandsInDirection = islandsInDirection(gameInfo, newPosition, plusAngle);
							Position nearestIslandInDirection = getNearestIslandInDirection(islandsInDirection);
							
							if (!isSubmarineHeadingToIsland(nearestIslandInDirection, islandSize, newPosition, submarineSize, minusVelocity, plusAngle, maxAccelerationPerRound)) {
								callHandler.move(gameId, submarine.getId(), minusVelocity - submarine.getVelocity(), maxSteeringPerRound);
								moved = true;
							}
						}
						if (!moved) {
							callHandler.move(gameId, submarine.getId(), minusVelocity - submarine.getVelocity(), 0);
							moved = true;
						}
					} else {
						List<Position> islandsInDirection = islandsInDirection(gameInfo, submarine.getPosition(), submarine.getAngle());
						Position nearestIslandInDirection = getNearestIslandInDirection(islandsInDirection);
						
						if (isSubmarineHeadingToIsland(nearestIslandInDirection, islandSize, submarine.getPosition(), submarineSize, submarine.getVelocity(), submarine.getAngle(), maxAccelerationPerRound)) {
							double plusAngle = normalizeAngle(submarine.getAngle() + maxSteeringPerRound);
							double minusAngle = normalizeAngle(submarine.getAngle() - maxSteeringPerRound);
							double minusVelocity = normalizeVelocity(submarine.getVelocity() - maxAccelerationPerRound, maxSpeed);
							
							if (!moved && !isSubmarineHeadingToIsland(nearestIslandInDirection, islandSize, submarine.getPosition(), submarineSize, submarine.getVelocity(), plusAngle, maxAccelerationPerRound)) {
								Position newPosition = new Position(
										submarine.getPosition().getX().doubleValue() + xMovement(submarine.getVelocity(), plusAngle),
										submarine.getPosition().getY().doubleValue() + yMovement(submarine.getVelocity(), plusAngle));
								
								if (!isSubmarineLeavingSpace(newPosition, submarineSize, submarine.getVelocity(), plusAngle, width, height, maxAccelerationPerRound)) {
									callHandler.move(gameId, submarine.getId(), 0, maxSteeringPerRound);
									moved = true;
								}
							}
							if (!moved && !isSubmarineHeadingToIsland(nearestIslandInDirection, islandSize, submarine.getPosition(), submarineSize, submarine.getVelocity(), minusAngle, maxAccelerationPerRound)) {
								Position newPosition = new Position(
										submarine.getPosition().getX().doubleValue() + xMovement(submarine.getVelocity(), minusAngle),
										submarine.getPosition().getY().doubleValue() + yMovement(submarine.getVelocity(), minusAngle));
								
								if (!isSubmarineLeavingSpace(newPosition, submarineSize, submarine.getVelocity(), minusAngle, width, height, maxAccelerationPerRound)) {
									callHandler.move(gameId, submarine.getId(), 0, -maxSteeringPerRound);
									moved = true;
								}
							}
							if (!moved && !isSubmarineHeadingToIsland(nearestIslandInDirection, islandSize, submarine.getPosition(), submarineSize, minusVelocity, minusAngle, maxAccelerationPerRound)) {
								Position newPosition = new Position(
										submarine.getPosition().getX().doubleValue() + xMovement(minusVelocity, minusAngle),
										submarine.getPosition().getY().doubleValue() + yMovement(minusVelocity, minusAngle));
								
								if (!isSubmarineLeavingSpace(newPosition, submarineSize, minusVelocity, minusAngle, width, height, maxAccelerationPerRound)) {
									callHandler.move(gameId, submarine.getId(), minusVelocity - submarine.getVelocity(), -maxSteeringPerRound);
									moved = true;
								}
							}
							if (!moved && !isSubmarineHeadingToIsland(nearestIslandInDirection, islandSize, submarine.getPosition(), submarineSize, minusVelocity, plusAngle, maxAccelerationPerRound)) {
								Position newPosition = new Position(
										submarine.getPosition().getX().doubleValue() + xMovement(minusVelocity, plusAngle),
										submarine.getPosition().getY().doubleValue() + yMovement(minusVelocity, plusAngle));
								
								if (!isSubmarineLeavingSpace(newPosition, submarineSize, minusVelocity, plusAngle, width, height, maxAccelerationPerRound)) {
									callHandler.move(gameId, submarine.getId(), minusVelocity - submarine.getVelocity(), maxSteeringPerRound);
									moved = true;
								}
							}
							if (!moved) {
								callHandler.move(gameId, submarine.getId(), minusVelocity - submarine.getVelocity(), 0);
								moved = true;
							}
						} else {
							// TODO valamit csinalni kell akkor is, ha nem hagyjuk el a helyet es nem megyunk szigetnek.
							if (!moved) {
								callHandler.move(gameId, submarine.getId(), normalizeVelocity(submarine.getVelocity() + maxAccelerationPerRound, maxSpeed) - submarine.getVelocity(), 0);
								moved = true;
							}
						}
					}
				}
			}
			gameInfo = callHandler.gameInfo(gameId);
		}
	}
	
}
