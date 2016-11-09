package hu.javachallenge.torpedo.controller;

import static hu.javachallenge.torpedo.util.MathUtil.aimAtMovingTarget;
import static hu.javachallenge.torpedo.util.MathUtil.normalizeAngle;
import static hu.javachallenge.torpedo.util.MathUtil.normalizeVelocity;
import static hu.javachallenge.torpedo.util.MathUtil.shouldWeShoot;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.LinkedList;
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
import hu.javachallenge.torpedo.util.MathUtil;

public class GameController implements Runnable {

	private static final Logger log = LoggerFactory.getLogger(GameController.class);

	private CallHandler callHandler;
	private String teamName;
	private MainPanel mainPanel;

	private GameInfoResponse gameInfo;
	private long gameId;
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
	private double sonarRange;
	private double extendedSonarRange;
	private List<Position> islandPositions;

	private long actualRound;
	private long previousRound;

	private SubmarinesResponse submarinesInGame;

	private List<Submarine> enemySubmarines;
	private List<Entity> torpedos;
	private Set<Submarine> submarinesToSlow;
	private List<MoveParameter> moveParameters;
	private Set<Submarine> enemySubmarineSet;
	private Set<Entity> torpedoSet;
	private List<Long> shipsWithActivatedSonar;

	public GameController(CallHandler callHandler, String teamName) {
		this.callHandler = callHandler;
		this.teamName = teamName;
		this.enemySubmarines = new ArrayList<>();
		this.torpedos = new ArrayList<>();
		this.submarinesToSlow = new HashSet<>();
		this.moveParameters = new ArrayList<>();
		this.enemySubmarineSet = new HashSet<>();
		this.torpedoSet = new HashSet<>();
		this.shipsWithActivatedSonar = new ArrayList<>();
	}

	@Override
	public void run() {
		startGame();
		playGame();
	}

	private void startGame() {
		GameListResponse gameList = callHandler.gameList();
		long[] games = gameList.getGames();

		if (games == null || games.length == 0) {
			CreateGameResponse createGameResponse = callHandler.createGame();
			gameId = createGameResponse.getId();
		} else {
			gameId = games[0];
		}

		updateGameInfo();

		Map<String, Boolean> connected = gameInfo.getGame().getConnectionStatus().getConnected();
		if (connected.get(teamName) == false) {
			callHandler.joinGame(gameId);
		}

		do {
			updateGameInfo();
		} while (gameInfo.getGame().getStatus().equals(Status.WAITING));

		this.gameId = gameInfo.getGame().getId();
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
		this.submarinesInGame = callHandler.submarinesInGame(gameId);
		this.sonarRange = gameInfo.getGame().getMapConfiguration().getSonarRange();
		this.extendedSonarRange = gameInfo.getGame().getMapConfiguration().getExtendedSonarRange();

		mainPanel = new MainPanel(gameInfo);
		mainPanel.setLayout(new BorderLayout());
		mainPanel.setPreferredSize(new Dimension(1275, 600));
		mainPanel.addSubmarines(Arrays.asList(submarinesInGame.getSubmarines()));

		JFrame mainFrame = new JFrame("NPE - BankTech Java Challenge 2016");
		mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		mainFrame.add(mainPanel);
		mainFrame.setResizable(false);
		mainFrame.setVisible(true);
		mainFrame.pack();

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
				
				try {
					enemySubmarines.clear();
					torpedos.clear();
					submarinesToSlow.clear();
					enemySubmarineSet.clear();
					torpedoSet.clear();
					shipsWithActivatedSonar.clear();
					
					submarinesInGame = callHandler.submarinesInGame(gameId);
					
					for (Submarine submarine : submarinesInGame.getSubmarines()) {
						if (submarine.getSonarCooldown() == 0) {
							boolean useExtendedSonar = true;
							
							for (Submarine s : submarinesInGame.getSubmarines()) {
								if (!s.equals(submarine)) {
									if (s.getSonarExtended() > 0 || shipsWithActivatedSonar.contains(s.getId())) { 
										if (MathUtil.distanceOfCircles(submarine.getPosition(), 0.0, s.getPosition(), 0.0) < sonarRange) {
											useExtendedSonar = false;
											break;
										}
									}
								}
							}
							
							if (useExtendedSonar) {
								callHandler.extendSonar(gameId, submarine.getId());
								shipsWithActivatedSonar.add(submarine.getId());
							}
						}
						
						SonarResponse sonar = callHandler.sonar(gameId, submarine.getId());
						if (sonar != null) {
							for (Entity entity : sonar.getEntities()) {
								switch (entity.getType()) {
								case "Submarine":
									if (!entity.getOwner().getName().equals(teamName)) {
										Submarine enemySubmarine = new Submarine("Submarine", entity.getId(), entity.getPosition(), entity.getOwner(), entity.getVelocity(), entity.getAngle(), 0, 0, 0, 0);
										if (gameInfo.getGame().getRound() < 2) {
											enemySubmarine.setVelocity(enemySubmarine.getVelocity() + maxSpeed);
										}
										log.debug("Enemy submarine: {}.", enemySubmarine);
										enemySubmarineSet.add(enemySubmarine);
										submarinesToSlow.add(submarine);
									}
									break;
								case "Torpedo":
									torpedoSet.add(entity);
									break;
								}
							}
						}
					}
					
					enemySubmarines.addAll(enemySubmarineSet);
					torpedos.addAll(torpedoSet);
					
					for (Submarine submarine : submarinesInGame.getSubmarines()) {
						mainPanel.updateSubmarine(submarine);
					}
					mainPanel.addEnemySubmarines(enemySubmarines);
					mainPanel.addTorpedos(torpedos);
					
					mainPanel.repaint();
					mainPanel.revalidate();
	
					log.trace("Detected enemy submarines: {}", enemySubmarines);
					log.trace("Detected torpedos: {}", torpedos);
	
					for (Submarine submarine : submarinesInGame.getSubmarines()) {
						for (Submarine enemySubmarine : enemySubmarines) {
							Double theta = aimAtMovingTarget(submarine.getPosition(), enemySubmarine.getPosition(), enemySubmarine.getAngle(), enemySubmarine.getVelocity(), torpedoSpeed);
							if (theta != null && shouldWeShoot(submarine.getPosition(), Arrays.asList(submarinesInGame.getSubmarines()), submarineSize, enemySubmarine, torpedoRange, torpedoSpeed, theta, torpedoExplosionRadius, islandPositions, islandSize)) {
								if (submarine.getTorpedoCooldown() == 0.0) {
									callHandler.shoot(gameId, submarine.getId(), normalizeAngle(theta));
									break;
								}
							}
						}
	
						moveParameters.clear();
	
						boolean moved = false;
	
						Set<DangerType> dangerTypes = MathUtil.getDangerTypes(gameInfo, islandPositions, islandSize, submarine.getPosition(), submarineSize, submarine.getVelocity(), submarine.getAngle(), maxAccelerationPerRound, width, height, torpedos, enemySubmarines, torpedoExplosionRadius);
						if (!dangerTypes.isEmpty()) {
							double plusAngle = normalizeAngle(submarine.getAngle() + maxSteeringPerRound);
							double minusAngle = normalizeAngle(submarine.getAngle() - maxSteeringPerRound);
							double minusVelocity = normalizeVelocity(submarine.getVelocity() - maxAccelerationPerRound, maxSpeed);
							
							Set<DangerType> newDangerTypes;
							
							Position newSubmarinePosition = new Position(submarine.getPosition().getX().doubleValue() + MathUtil.xMovement(submarine.getVelocity(), plusAngle), submarine.getPosition().getY().doubleValue() + MathUtil.yMovement(submarine.getVelocity(), plusAngle));
							
							if (!moved) {
								newDangerTypes = MathUtil.getDangerTypes(gameInfo, islandPositions, islandSize, newSubmarinePosition, submarineSize, submarine.getVelocity(), plusAngle, maxAccelerationPerRound, width, height, torpedos, enemySubmarines, torpedoExplosionRadius);
								if (newDangerTypes.isEmpty()) {
									callHandler.move(gameId, submarine.getId(), 0, maxSteeringPerRound);
									moved = true;
								} else if (newDangerTypes.contains(DangerType.HEADING_TO_TORPEDO_EXPLOSION)) {
									moveParameters.add(new MoveParameter(0, maxSteeringPerRound));
								}
							}
	
							newSubmarinePosition = new Position(submarine.getPosition().getX().doubleValue() + MathUtil.xMovement(submarine.getVelocity(), minusAngle), submarine.getPosition().getY().doubleValue() + MathUtil.yMovement(submarine.getVelocity(), minusAngle));
	
							if (!moved) {
								newDangerTypes = MathUtil.getDangerTypes(gameInfo, islandPositions, islandSize, newSubmarinePosition, submarineSize, submarine.getVelocity(), minusAngle, maxAccelerationPerRound, width, height, torpedos, enemySubmarines, torpedoExplosionRadius);
								if (newDangerTypes.isEmpty()) {
									callHandler.move(gameId, submarine.getId(), 0, -maxSteeringPerRound);
									moved = true;
								} else if (newDangerTypes.contains(DangerType.HEADING_TO_TORPEDO_EXPLOSION)) {
									moveParameters.add(new MoveParameter(0, -maxSteeringPerRound));
								}
							}
	
							newSubmarinePosition = new Position(submarine.getPosition().getX().doubleValue() + MathUtil.xMovement(minusVelocity, minusAngle), submarine.getPosition().getY().doubleValue() + MathUtil.yMovement(minusVelocity, minusAngle));
	
							if (!moved) {
								newDangerTypes = MathUtil.getDangerTypes(gameInfo, islandPositions, islandSize, newSubmarinePosition, submarineSize, minusVelocity, minusAngle, maxAccelerationPerRound, width, height, torpedos, enemySubmarines, torpedoExplosionRadius);
								if (newDangerTypes.isEmpty()) {
									callHandler.move(gameId, submarine.getId(), minusVelocity - submarine.getVelocity(), -maxSteeringPerRound);
									moved = true;
								} else if (newDangerTypes.contains(DangerType.HEADING_TO_TORPEDO_EXPLOSION)) {
									moveParameters.add(new MoveParameter(minusVelocity - submarine.getVelocity(), -maxSteeringPerRound));
								}
							}
	
							newSubmarinePosition = new Position(submarine.getPosition().getX().doubleValue() + MathUtil.xMovement(minusVelocity, plusAngle), submarine.getPosition().getY().doubleValue() + MathUtil.yMovement(minusVelocity, plusAngle));
	
							if (!moved) {
								newDangerTypes = MathUtil.getDangerTypes(gameInfo, islandPositions, islandSize, newSubmarinePosition, submarineSize, minusVelocity, plusAngle, maxAccelerationPerRound, width, height, torpedos, enemySubmarines, torpedoExplosionRadius);
								if (newDangerTypes.isEmpty()) {
									callHandler.move(gameId, submarine.getId(), minusVelocity - submarine.getVelocity(), maxSteeringPerRound);
									moved = true;
								} else if (newDangerTypes.contains(DangerType.HEADING_TO_TORPEDO_EXPLOSION)) {
									moveParameters.add(new MoveParameter(minusVelocity - submarine.getVelocity(), maxSteeringPerRound));
								}
							}
							
							if (!moved && dangerTypes.contains(DangerType.LEAVING_SPACE)) {
								callHandler.move(gameId, submarine.getId(), minusVelocity - submarine.getVelocity(), MathUtil.getSteeringHeadingToEdge(submarine.getPosition(), submarineSize, width, height, submarine.getAngle(), sonarRange, maxSteeringPerRound));
								moved = true;
							}
							
							if (!moved && (dangerTypes.contains(DangerType.HEADING_TO_ISLAND))) {
								Position islandPosition = MathUtil.getNearestIslandInDirection(islandPositions);
								double steering = MathUtil.getSteeringHeadingToIsland(submarine, islandPosition, maxSteeringPerRound, submarineSize, islandSize);
								callHandler.move(gameId, submarine.getId(), minusVelocity - submarine.getVelocity(), steering);
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
								if (submarinesToSlow.contains(submarine)) {
									if(submarine.getVelocity() > maxSpeed * 0.5) {
										newNormalizedVelocity = normalizeVelocity(submarine.getVelocity() - maxAccelerationPerRound, maxSpeed);
									}
								}
								List<Submarine> allSubmarines = new LinkedList<>();
								allSubmarines.addAll(enemySubmarines);
								allSubmarines.addAll(Arrays.asList(submarinesInGame.getSubmarines()));
								Submarine submarineInOurWay = MathUtil.getNearestSubmarineInOurWay(submarine, allSubmarines, submarineSize, maxSpeed);
								if (submarineInOurWay != null) {
									callHandler.move(gameId, submarine.getId(), newNormalizedVelocity - submarine.getVelocity(), MathUtil.getSteeringHeadingToSubmarine(submarine, submarineInOurWay, maxSteeringPerRound, submarineSize));
								} else {
									double steering = MathUtil.getSteeringHeadingToEdge(submarine.getPosition(), submarineSize, width, height, submarine.getAngle(), sonarRange, maxSteeringPerRound);
									if (steering == 0.0) {
										Position nearestIslandPosition = MathUtil.getNearestIsland(gameInfo, submarine.getPosition());
										if (nearestIslandPosition != null) {
											steering = MathUtil.getSteeringHeadingToIsland(submarine, nearestIslandPosition, maxSteeringPerRound, submarineSize, islandSize);
										} else {
											steering = MathUtil.getSteeringBasedOnSonars(submarine, submarinesInGame.getSubmarines(), sonarRange, extendedSonarRange, maxSteeringPerRound);
										}
									} else {
										if(submarine.getVelocity() > maxSpeed * 0.25) {
											newNormalizedVelocity = normalizeVelocity(submarine.getVelocity() - maxAccelerationPerRound, maxSpeed);
										}
									}
									
									if (steering == 0.0) {
										Submarine[] enemies = new Submarine[enemySubmarines.size()];
										enemies = allSubmarines.toArray(enemies);
										steering = MathUtil.getSteeringBasedOnEnemyPosition(submarine, enemies, sonarRange, maxSteeringPerRound);
									}
									callHandler.move(gameId, submarine.getId(), newNormalizedVelocity - submarine.getVelocity(), steering);
								}
								moved = true;
							}
						}
					}
				} catch (Throwable t) {
					log.error(t.toString());
					t.printStackTrace();
				}
			}
			updateGameInfo();
		}
	}

	private void updateGameInfo() {
		GameInfoResponse gameInfo = callHandler.gameInfo(gameId);
		if (gameInfo != null) {
			this.gameInfo = gameInfo;
		}
	}

}
