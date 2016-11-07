package hu.javachallenge.torpedo.controller;

import static hu.javachallenge.torpedo.util.MathUtil.aimAtMovingTarget;
import static hu.javachallenge.torpedo.util.MathUtil.isDangerousToShoot;
import static hu.javachallenge.torpedo.util.MathUtil.normalizeAngle;
import static hu.javachallenge.torpedo.util.MathUtil.normalizeVelocity;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.swing.JFrame;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import hu.javachallenge.torpedo.gui.MainPanel;
import hu.javachallenge.torpedo.model.Entity;
import hu.javachallenge.torpedo.model.Position;
import hu.javachallenge.torpedo.model.Status;
import hu.javachallenge.torpedo.model.Submarine;
import hu.javachallenge.torpedo.response.CreateGameResponse;
import hu.javachallenge.torpedo.response.GameInfoResponse;
import hu.javachallenge.torpedo.response.GameListResponse;
import hu.javachallenge.torpedo.response.SonarResponse;
import hu.javachallenge.torpedo.response.SubmarinesResponse;
import hu.javachallenge.torpedo.util.DangerType;
import hu.javachallenge.torpedo.util.MathConstants;
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

	public GameController(CallHandler callHandler, String teamName) {
		this.callHandler = callHandler;
		this.teamName = teamName;
		this.enemySubmarines = new ArrayList<>();
		this.torpedos = new ArrayList<>();
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

		Dimension dimension = new Dimension(1275, 600);
		mainPanel = new MainPanel(gameInfo);
		mainPanel.setLayout(new BorderLayout());
		mainPanel.setPreferredSize(dimension);

		JFrame mainFrame = new JFrame("NPE - BankTech Java Challenge 2016");
		mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		mainFrame.add(mainPanel);
		mainFrame.setResizable(false);
		mainFrame.setVisible(true);
		mainFrame.pack();

		submarinesInGame = callHandler.submarinesInGame(gameId);
		mainPanel.addSubmarines(Arrays.asList(submarinesInGame.getSubmarines()));

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
					mainPanel.updateSubmarine(submarine);
				}

				HashSet<Submarine> enemySubmarinesSet = new HashSet<>();
				HashSet<Entity> torpedosSet = new HashSet<>();

				for (Submarine submarine : submarinesInGame.getSubmarines()) {
					if (submarine.getSonarCooldown() == 0) {
						callHandler.extendSonar(gameId, submarine.getId());
					}
					SonarResponse sonar = callHandler.sonar(gameId, submarine.getId());

					for (Entity entity : sonar.getEntities()) {
						switch (entity.getType()) {
						case "Submarine":
							if (!entity.getOwner().getName().equals(teamName)) {
								enemySubmarinesSet.add(new Submarine("Submarine", entity.getId(), entity.getPosition(), entity.getOwner(), entity.getVelocity(), entity.getAngle(), 0, 0, 0, 0));
							}
							break;
						case "Torpedo":
							torpedosSet.add(entity);
							break;
						}
					}
				}
				enemySubmarines.addAll(enemySubmarinesSet);
				mainPanel.addEnemySubmarines(enemySubmarines);

				torpedos.addAll(torpedosSet);
				mainPanel.addTorpedos(torpedos);

				mainPanel.repaint();
				mainPanel.revalidate();
				
				log.trace("Detected enemy submarines: {}", enemySubmarines);
				log.trace("Detected torpedos: {}", torpedos);

				for (Submarine submarine : submarinesInGame.getSubmarines()) {
					for (Submarine enemySubmarine : enemySubmarines) {
						double theta = aimAtMovingTarget(submarine.getPosition(), enemySubmarine.getPosition(), enemySubmarine.getAngle(), enemySubmarine.getVelocity(), torpedoSpeed);
						if (!isDangerousToShoot(submarine.getPosition(), Arrays.asList(submarinesInGame.getSubmarines()), submarineSize, enemySubmarine.getPosition(), enemySubmarine.getVelocity(), enemySubmarine.getAngle(), torpedoSpeed, theta, torpedoExplosionRadius, islandPositions, islandSize)) {
							if (submarine.getTorpedoCooldown() == 0.0) {
								callHandler.shoot(gameId, submarine.getId(), normalizeAngle(theta));
								break;
							}
						}
					}

					List<MoveParameter> moveParameters = new ArrayList<>();

					boolean moved = false;

					Set<DangerType> dangerTypes = MathUtil.getDangerTypes(gameInfo, islandPositions, islandSize, submarine.getPosition(), submarineSize, submarine.getVelocity(), submarine.getAngle(), maxAccelerationPerRound, width, height, torpedos, enemySubmarines, torpedoExplosionRadius);
					if (!dangerTypes.isEmpty()) {
						double plusAngle = normalizeAngle(submarine.getAngle() + maxSteeringPerRound);
						double minusAngle = normalizeAngle(submarine.getAngle() - maxSteeringPerRound);
						double minusVelocity = normalizeVelocity(submarine.getVelocity() - maxAccelerationPerRound, maxSpeed);

						Position newSubmarinePosition = new Position(submarine.getPosition().getX().doubleValue() + MathUtil.xMovement(submarine.getVelocity(), plusAngle), submarine.getPosition().getY().doubleValue() + MathUtil.yMovement(submarine.getVelocity(), plusAngle));

						if (!moved) {
							dangerTypes = MathUtil.getDangerTypes(gameInfo, islandPositions, islandSize, newSubmarinePosition, submarineSize, submarine.getVelocity(), plusAngle, maxAccelerationPerRound, width, height, torpedos, enemySubmarines, torpedoExplosionRadius);
							if (dangerTypes.isEmpty()) {
								callHandler.move(gameId, submarine.getId(), 0, maxSteeringPerRound);
								moved = true;
							} else if (dangerTypes.contains(DangerType.HEADING_TO_TORPEDO_EXPLOSION)) {
								moveParameters.add(new MoveParameter(0, maxSteeringPerRound));
							}
						}

						newSubmarinePosition = new Position(submarine.getPosition().getX().doubleValue() + MathUtil.xMovement(submarine.getVelocity(), minusAngle), submarine.getPosition().getY().doubleValue() + MathUtil.yMovement(submarine.getVelocity(), minusAngle));

						if (!moved) {
							dangerTypes = MathUtil.getDangerTypes(gameInfo, islandPositions, islandSize, newSubmarinePosition, submarineSize, submarine.getVelocity(), minusAngle, maxAccelerationPerRound, width, height, torpedos, enemySubmarines, torpedoExplosionRadius);
							if (dangerTypes.isEmpty()) {
								callHandler.move(gameId, submarine.getId(), 0, -maxSteeringPerRound);
								moved = true;
							} else if (dangerTypes.contains(DangerType.HEADING_TO_TORPEDO_EXPLOSION)) {
								moveParameters.add(new MoveParameter(0, -maxSteeringPerRound));
							}
						}

						newSubmarinePosition = new Position(submarine.getPosition().getX().doubleValue() + MathUtil.xMovement(minusVelocity, minusAngle), submarine.getPosition().getY().doubleValue() + MathUtil.yMovement(minusVelocity, minusAngle));

						if (!moved) {
							dangerTypes = MathUtil.getDangerTypes(gameInfo, islandPositions, islandSize, newSubmarinePosition, submarineSize, minusVelocity, minusAngle, maxAccelerationPerRound, width, height, torpedos, enemySubmarines, torpedoExplosionRadius);
							if (dangerTypes.isEmpty()) {
								callHandler.move(gameId, submarine.getId(), minusVelocity - submarine.getVelocity(), -maxSteeringPerRound);
								moved = true;
							} else if (dangerTypes.contains(DangerType.HEADING_TO_TORPEDO_EXPLOSION)) {
								moveParameters.add(new MoveParameter(minusVelocity - submarine.getVelocity(), -maxSteeringPerRound));
							}
						}

						newSubmarinePosition = new Position(submarine.getPosition().getX().doubleValue() + MathUtil.xMovement(minusVelocity, plusAngle), submarine.getPosition().getY().doubleValue() + MathUtil.yMovement(minusVelocity, plusAngle));

						if (!moved) {
							dangerTypes = MathUtil.getDangerTypes(gameInfo, islandPositions, islandSize, newSubmarinePosition, submarineSize, minusVelocity, plusAngle, maxAccelerationPerRound, width, height, torpedos, enemySubmarines, torpedoExplosionRadius);
							if (dangerTypes.isEmpty()) {
								callHandler.move(gameId, submarine.getId(), minusVelocity - submarine.getVelocity(), maxSteeringPerRound);
								moved = true;
							} else if (dangerTypes.contains(DangerType.HEADING_TO_TORPEDO_EXPLOSION)) {
								moveParameters.add(new MoveParameter(minusVelocity - submarine.getVelocity(), maxSteeringPerRound));
							}
						}

						if (!moved && (dangerTypes.contains(DangerType.HEADING_TO_ISLAND) || dangerTypes.contains(DangerType.LEAVING_SPACE))) {
							if (submarine.getVelocity() < MathConstants.EPSILON) {
								callHandler.move(gameId, submarine.getId(), minusVelocity - submarine.getVelocity(), maxSteeringPerRound);
							} else {
								callHandler.move(gameId, submarine.getId(), minusVelocity - submarine.getVelocity(), 0);
							}
							moved = true;
						}
					}

					if (!moved) {
						if (!moveParameters.isEmpty()) {
							log.warn("Submarine {} is heading to torpedo.", submarine);
							MoveParameter moveParameter = moveParameters.get(0);
							callHandler.move(gameId, submarine.getId(), moveParameter.speed, moveParameter.angle);
							moved = true;
						} else {
							double newNormalizedVelocity = normalizeVelocity(submarine.getVelocity() + maxAccelerationPerRound, maxSpeed);
							if (MathUtil.isSubmarineMovingInLineWithOtherSubmarine(submarine, submarinesInGame.getSubmarines(), submarineSize, maxSpeed)) {
								callHandler.move(gameId, submarine.getId(), newNormalizedVelocity - submarine.getVelocity(), maxSteeringPerRound);
							} else {
								callHandler.move(gameId, submarine.getId(), newNormalizedVelocity - submarine.getVelocity(), 0);
							}
							moved = true;
						}
					}
				}
			}
			gameInfo = callHandler.gameInfo(gameId);
		}
	}

}
