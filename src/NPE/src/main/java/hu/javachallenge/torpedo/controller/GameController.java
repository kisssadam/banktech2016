package hu.javachallenge.torpedo.controller;

import static hu.javachallenge.torpedo.util.MathUtil.aimAtMovingTarget;
import static hu.javachallenge.torpedo.util.MathUtil.isDangerousToShoot;
import static hu.javachallenge.torpedo.util.MathUtil.normalizeAngle;
import static hu.javachallenge.torpedo.util.MathUtil.normalizeVelocity;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import hu.javachallenge.torpedo.gui.MainPanel;
import hu.javachallenge.torpedo.model.Entity;
import hu.javachallenge.torpedo.model.Owner;
import hu.javachallenge.torpedo.model.Position;
import hu.javachallenge.torpedo.model.Status;
import hu.javachallenge.torpedo.model.Submarine;
import hu.javachallenge.torpedo.response.CreateGameResponse;
import hu.javachallenge.torpedo.response.ExtendSonarResponse;
import hu.javachallenge.torpedo.response.GameInfoResponse;
import hu.javachallenge.torpedo.response.GameListResponse;
import hu.javachallenge.torpedo.response.MoveResponse;
import hu.javachallenge.torpedo.response.SonarResponse;
import hu.javachallenge.torpedo.response.SubmarinesResponse;
import hu.javachallenge.torpedo.util.DangerType;
import hu.javachallenge.torpedo.util.MathUtil;

public class GameController implements Runnable {

	private static final Logger log = LoggerFactory.getLogger(GameController.class);

	private CallHandler callHandler;
	private String teamName;
	private MainPanel mainPanel;

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
	private double torpedoExplosionRadius;
	private List<Position> islandPositions;

	private long actualRound;
	private long previousRound;

	private List<Submarine> enemySubmarines;
	private List<Entity> torpedos;

	private SubmarinesResponse submarinesInGame;

	public GameController(CallHandler callHandler, String teamName, MainPanel mainPanel) {
		this.callHandler = callHandler;
		this.teamName = teamName;
		this.enemySubmarines = new ArrayList<>();
		this.torpedos = new ArrayList<>();
		this.mainPanel = mainPanel;
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
		this.torpedoExplosionRadius = gameInfo.getGame().getMapConfiguration().getTorpedoExplosionRadius();
		this.islandPositions = Arrays.asList(gameInfo.getGame().getMapConfiguration().getIslandPositions());

		mainPanel.scalingInit(this.height, this.width, this.submarineSize);

		submarinesInGame = callHandler.submarinesInGame(gameId);
		for (Submarine submarine : submarinesInGame.getSubmarines()) {
			mainPanel.addSubmarine(submarine);
			mainPanel.addSubmarine(new Submarine("", 1, new Position(0, 0), new Owner(teamName), 0, 0, 0, 0, 0, 0));
			mainPanel.addSubmarine(new Submarine("", 2, new Position(1700, 0), new Owner(teamName), 0, 0, 0, 0, 0, 0));
			mainPanel.addSubmarine(new Submarine("", 2, new Position(0, 800), new Owner(teamName), 0, 0, 0, 0, 0, 0));
			mainPanel.addSubmarine(new Submarine("", 2, new Position(1700, 800), new Owner(teamName), 0, 0, 0, 0, 0, 0));
		}
		mainPanel.repaint();
		mainPanel.revalidate();
	}

	private static class MoveParameter {
		private double speed;
		private double angle;

		public MoveParameter(double speed, double angle) {
			this.speed = speed;
			this.angle = angle;
		}
	}

	private void playGame() {
		while (!gameInfo.getGame().getStatus().equals(Status.ENDED)) {
			actualRound = gameInfo.getGame().getRound();
			if (actualRound > previousRound) {
				previousRound = actualRound;
				enemySubmarines.clear();
				torpedos.clear();

				submarinesInGame = callHandler.submarinesInGame(gameId);
				for (Submarine submarine : submarinesInGame.getSubmarines()) {
					double newX = submarine.getPosition().getX().doubleValue() + MathUtil.xMovement(submarine.getVelocity(), submarine.getAngle());
					double newY = submarine.getPosition().getY().doubleValue() + MathUtil.yMovement(submarine.getVelocity(), submarine.getAngle());

					String type = submarine.getType();
					Position newPosition = new Position(newX, newY);
					Owner owner = submarine.getOwner();
					double newVelocity = submarine.getVelocity() + submarine.getVelocity();
					double newAngle = submarine.getAngle() + submarine.getAngle();
					int hp = submarine.getHp();
					int sonarCooldown = submarine.getSonarCooldown();
					int torpedoCooldown = submarine.getTorpedoCooldown();
					int sonarExtended = submarine.getSonarExtended();

					mainPanel.updateSubmarine(type, submarine.getId(), newPosition, owner, newVelocity, newAngle, hp, sonarCooldown, torpedoCooldown, sonarExtended);

					mainPanel.repaint();
					mainPanel.revalidate();
				}

				for (Submarine submarine : submarinesInGame.getSubmarines()) {
					if (submarine.getSonarCooldown() == 0) {
						ExtendSonarResponse extendSonarResponse = callHandler.extendSonar(gameId, submarine.getId());
					}
					SonarResponse sonar = callHandler.sonar(gameId, submarine.getId());

					for (Entity entity : sonar.getEntities()) {
						switch (entity.getType()) {
						case "Submarine":
							if (!entity.getOwner().getName().equals(teamName)) {
								enemySubmarines.add(new Submarine("Submarine", entity.getId(), entity.getPosition(), entity.getOwner(), entity.getVelocity(), entity.getAngle(), 0, 0, 0, 0));
							}
							break;
						case "Torpedo":
							torpedos.add(entity);
							break;
						}
					}
				}
				log.trace("Detected enemy submarines: {}", enemySubmarines);
				log.trace("Detected torpedos: {}", torpedos);

				for (Submarine submarine : submarinesInGame.getSubmarines()) {
					for (Submarine enemySubmarine : enemySubmarines) {
						double theta = aimAtMovingTarget(submarine.getPosition(), enemySubmarine.getPosition(), enemySubmarine.getAngle(), enemySubmarine.getVelocity(), torpedoSpeed);
						if (!isDangerousToShoot(submarine.getPosition(), Arrays.asList(submarinesInGame.getSubmarines()), submarineSize, enemySubmarine.getPosition(), enemySubmarine.getVelocity(), enemySubmarine.getAngle(), torpedoSpeed, theta, torpedoExplosionRadius)) {
							if (submarine.getTorpedoCooldown() == 0.0) {
								callHandler.shoot(gameId, submarine.getId(), normalizeAngle(theta));
								break;
							}
						}
					}

					List<MoveParameter> moveParameters = new ArrayList<>();

					boolean moved = false;

					if (!MathUtil.getDangerTypes(gameInfo, islandPositions, islandSize, submarine.getPosition(), submarineSize, submarine.getVelocity(), submarine.getAngle(), maxAccelerationPerRound, width, height, torpedos, enemySubmarines, torpedoExplosionRadius).isEmpty()) {
						double plusAngle = normalizeAngle(submarine.getAngle() + maxSteeringPerRound);
						double minusAngle = normalizeAngle(submarine.getAngle() - maxSteeringPerRound);
						double minusVelocity = normalizeVelocity(submarine.getVelocity() - maxAccelerationPerRound, maxSpeed);

						Position newSubmarinePosition = new Position(
								submarine.getPosition().getX().doubleValue() + MathUtil.xMovement(submarine.getVelocity(), plusAngle),
								submarine.getPosition().getY().doubleValue() + MathUtil.yMovement(submarine.getVelocity(), plusAngle));

						Set<DangerType> dangerTypes = MathUtil.getDangerTypes(gameInfo, islandPositions, islandSize, newSubmarinePosition, submarineSize, submarine.getVelocity(), plusAngle, maxAccelerationPerRound, width, height, torpedos, enemySubmarines, torpedoExplosionRadius);
						if (!moved) {
							if (dangerTypes.isEmpty()) {
								move(gameId, submarine.getId(), 0, maxSteeringPerRound);
								moved = true;
							} else if (dangerTypes.contains(DangerType.HEADING_TO_TORPEDO_EXPLOSION)) {
								moveParameters.add(new MoveParameter(0, maxSteeringPerRound));
							}
						}

						newSubmarinePosition = new Position(
								submarine.getPosition().getX().doubleValue() + MathUtil.xMovement(submarine.getVelocity(), minusAngle),
								submarine.getPosition().getY().doubleValue() + MathUtil.yMovement(submarine.getVelocity(), minusAngle));
						
						dangerTypes = MathUtil.getDangerTypes(gameInfo, islandPositions, islandSize, newSubmarinePosition, submarineSize, submarine.getVelocity(), minusAngle, maxAccelerationPerRound, width, height, torpedos, enemySubmarines, torpedoExplosionRadius);
						if (!moved) {
							if (dangerTypes.isEmpty()) {
								move(gameId, submarine.getId(), 0, -maxSteeringPerRound);
								moved = true;
							} else if (dangerTypes.contains(DangerType.HEADING_TO_TORPEDO_EXPLOSION)) {
								moveParameters.add(new MoveParameter(0, -maxSteeringPerRound));
							}
						}

						newSubmarinePosition = new Position(
								submarine.getPosition().getX().doubleValue() + MathUtil.xMovement(minusVelocity, minusAngle),
								submarine.getPosition().getY().doubleValue() + MathUtil.yMovement(minusVelocity, minusAngle));
						
						dangerTypes = MathUtil.getDangerTypes(gameInfo, islandPositions, islandSize, newSubmarinePosition, submarineSize, minusVelocity, minusAngle, maxAccelerationPerRound, width, height, torpedos, enemySubmarines, torpedoExplosionRadius);
						if (!moved) {
							if (dangerTypes.isEmpty()) {
								move(gameId, submarine.getId(), minusVelocity - submarine.getVelocity(), -maxSteeringPerRound);
								moved = true;
							} else if (dangerTypes.contains(DangerType.HEADING_TO_TORPEDO_EXPLOSION)) {
								moveParameters.add(new MoveParameter(minusVelocity - submarine.getVelocity(), -maxSteeringPerRound));
							}
						}

						newSubmarinePosition = new Position(
								submarine.getPosition().getX().doubleValue() + MathUtil.xMovement(minusVelocity, plusAngle),
								submarine.getPosition().getY().doubleValue() + MathUtil.yMovement(minusVelocity, plusAngle));

						dangerTypes = MathUtil.getDangerTypes(gameInfo, islandPositions, islandSize, newSubmarinePosition, submarineSize, minusVelocity, plusAngle, maxAccelerationPerRound, width, height, torpedos, enemySubmarines, torpedoExplosionRadius);
						if (!moved) {
							if (dangerTypes.isEmpty()) {
								move(gameId, submarine.getId(), minusVelocity - submarine.getVelocity(), maxSteeringPerRound);
								moved = true;
							} else if (dangerTypes.contains(DangerType.HEADING_TO_TORPEDO_EXPLOSION)) {
								moveParameters.add(new MoveParameter(minusVelocity - submarine.getVelocity(), maxSteeringPerRound));
							}
						}

						if (!moved && (dangerTypes.contains(DangerType.HEADING_TO_ISLAND) || dangerTypes.contains(DangerType.LEAVING_SPACE))) {
							move(gameId, submarine.getId(), minusVelocity - submarine.getVelocity(), 0);
							moved = true;
						}
					}

					if (!moved) {
						if (!moveParameters.isEmpty()) {
							log.warn("Submarine {} is heading to torpedo.", submarine);
							MoveParameter moveParameter = moveParameters.get(0);
							move(gameId, submarine.getId(), moveParameter.speed, moveParameter.angle);
							moved = true;
						} else {
							double newNormalizedVelocity = normalizeVelocity(submarine.getVelocity() + maxAccelerationPerRound, maxSpeed);
							if (MathUtil.isSubmarineMovingInLineWithOtherSubmarine(submarine, submarinesInGame.getSubmarines(), submarineSize, maxSpeed)) {
								move(gameId, submarine.getId(), newNormalizedVelocity - submarine.getVelocity(), maxSteeringPerRound);
							} else {
								move(gameId, submarine.getId(), newNormalizedVelocity - submarine.getVelocity(), 0);
							}
							moved = true;
						}
					}
				}
			}
			gameInfo = callHandler.gameInfo(gameId);
		}
	}

	private void move(long gameId, long submarineId, double velocity, double angle) {
		MoveResponse move = callHandler.move(gameId, submarineId, velocity, angle);
		if (move != null) {

		}
	}

}
