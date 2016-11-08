package hu.javachallenge.torpedo.controller;

import static hu.javachallenge.torpedo.util.MathUtil.aimAtMovingTarget;
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
import static hu.javachallenge.torpedo.util.MathUtil.shouldWeShoot;
import java.util.LinkedList;

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
		this.submarinesInGame = callHandler.submarinesInGame(gameId);

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
				enemySubmarines.clear();
				torpedos.clear();

				submarinesInGame = callHandler.submarinesInGame(gameId);

				Set<Submarine> enemySubmarinesSet = new HashSet<>();
				Set<Entity> torpedosSet = new HashSet<>();

				for (Submarine submarine : submarinesInGame.getSubmarines()) {
					
					/**
					 * Na ez az a pont, ami kicsit nehezebb lesz az extendedSonar-ral kapcsolatban.
					 * Most ide írom le a gondolataimat, mert már kibaszott fáradt vagyok. Délután, mire együtt belevágunk,
					 * már remélhetőleg itt a működő kód fog állni.
					 * 
					 * - Kész van a matematikai művelet arra, hogy kiszámítsuk a megnövelt szonárok átfedését.
					 * - Minden hajó esetén meg kell nézni, hogy van-e a közelében egy másik olyan hajó, aminek
					 * éppen aktív a kiterjesztett szonárja.
					 * - Ha igen, ki kell számítani az ő működő és a mi képzeletbeli kiterjesztett szonárjaink átfedését.
					 * - Ha nincs olyan hajó, amivel ez az (átfedés / kiterjesztett szonár területe) arány meghaladja
					 * a MathUtils-ben definiált küszöbértéket, akkor aktiváljuk az aktuális hajóét, különben nem
					 * (KIVÉTEL: az összes szignifikáns átfedéssel rendelkező hajó kiterjesztett szonárja 1, esetleg néhány
					 * körön belül lejár).
					 * - Probléma: tárolni kell, hogy mely hajók esetén hívtuk már meg az extendedSonar()-t, és azokat a
					 * kimaradt hajók vizsgálatakor már belekalkulálni az aktív szonárral rendelkező hajókba.
					 * Újabb for ciklus kerül be az alábbi if-be. Lehetséges ezt esetleg távolság alapján szűrni?
					 * Mindenképpen gyorsítsuk a működését az előbb említett tárolással.
					 * 
					 * Ha lehetséges az, hogy olyan hajópárokat ellenőrzünk, amik már szerepeltek, akkor ezt el kellene
					 * kerülni.
					 */
					
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
				torpedos.addAll(torpedosSet);
				
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
						double theta = aimAtMovingTarget(submarine.getPosition(), enemySubmarine.getPosition(), enemySubmarine.getAngle(), enemySubmarine.getVelocity(), torpedoSpeed);
						if (shouldWeShoot(submarine.getPosition(), Arrays.asList(submarinesInGame.getSubmarines()), submarineSize, enemySubmarine, torpedoRange, torpedoSpeed, theta, torpedoExplosionRadius, islandPositions, islandSize)) {
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
							if(!enemySubmarines.isEmpty()) {
								if(submarine.getVelocity() > maxSpeed * 0.5) {
									newNormalizedVelocity = normalizeVelocity(submarine.getVelocity() - maxAccelerationPerRound, maxSpeed);
								}
							}
							List<Submarine> everySubmarines = new LinkedList<>();
							everySubmarines.addAll(enemySubmarines);
							everySubmarines.addAll(Arrays.asList(submarinesInGame.getSubmarines()));
							Submarine submarineInOurWay = MathUtil.getNearestSubmarineInOurWay(submarine, everySubmarines, submarineSize, maxSpeed);
							if (submarineInOurWay != null) {
								callHandler.move(gameId, submarine.getId(), newNormalizedVelocity - submarine.getVelocity(), MathUtil.getSteeringHeadingToSubmarine(submarine, submarineInOurWay, maxSteeringPerRound));
							} else {
								callHandler.move(gameId, submarine.getId(), newNormalizedVelocity - submarine.getVelocity(), 0);
							}
							moved = true;
						}
					}
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
