package hu.javachallenge.torpedo.controller;

import static hu.javachallenge.torpedo.util.MathUtil.aimAtMovingTarget;
import static hu.javachallenge.torpedo.util.MathUtil.normalizeAngle;
import static hu.javachallenge.torpedo.util.MathUtil.normalizeVelocity;
import static hu.javachallenge.torpedo.util.MathUtil.shouldWeShoot;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
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
		callHandler.createGame();

		GameListResponse gameList = callHandler.gameList();
		long[] games = gameList.getGames();

		gameId = games[0];

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

	private void playGame() {
		while (!gameInfo.getGame().getStatus().equals(Status.ENDED)) {
			actualRound = gameInfo.getGame().getRound();
			if (actualRound > previousRound) {
				previousRound = actualRound;

				try {
					clearFields();
					playRound();
				} catch (Throwable t) {
					log.error(t.toString());
					t.printStackTrace();
				}
			}
			updateGameInfo();
		}
	}

	private void clearFields() {
		enemySubmarines.clear();
		torpedos.clear();
		submarinesToSlow.clear();
		enemySubmarineSet.clear();
		torpedoSet.clear();
		shipsWithActivatedSonar.clear();
	}

	private void playRound() {
		submarinesInGame = callHandler.submarinesInGame(gameId);

		for (Submarine submarine : submarinesInGame.getSubmarines()) {
			extendSonarIfNeccessary(submarine);

			SonarResponse sonar = callHandler.sonar(gameId, submarine.getId());
			processSonarResponse(submarine, sonar);
		}

		enemySubmarines.addAll(enemySubmarineSet);
		torpedos.addAll(torpedoSet);

		updateMainPanel();

		log.trace("Detected enemy submarines: {}", enemySubmarines);
		log.trace("Detected torpedos: {}", torpedos);

		for (Submarine submarine : submarinesInGame.getSubmarines()) {
			controlSubmarine(submarine);
		}
	}

	private void extendSonarIfNeccessary(Submarine submarine) {
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
	}

	private void processSonarResponse(Submarine submarine, SonarResponse sonar) {
		if (sonar == null) {
			return;
		}
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
					}/*
				// TODO czuczi
				// Ezt nezd meg, hogy mi a velemenyed! Szerintem rossz,
				// de most a hajok allandoan egyutt mennek sonar tavolsagon belul,
				// ezert probaltuk belerakni, de keves sikerrel jartunk el!
				else if (submarinesToSlow.isEmpty()) {
					submarinesToSlow.add(submarine);
				}*/
					break;
				case "Torpedo":
					torpedoSet.add(entity);
					break;
			}
		}
	}

	private void controlSubmarine(Submarine submarine) {
		shoot(submarine);

		moveParameters.clear();
		boolean moved = false;

		Set<DangerType> dangerTypes = MathUtil.getDangerTypes(gameInfo, submarine.getPosition(), submarine.getVelocity(), submarine.getAngle(), torpedos, enemySubmarines);
		if (!dangerTypes.isEmpty()) {
			double plusAngle = normalizeAngle(submarine.getAngle() + maxSteeringPerRound);
			double minusAngle = normalizeAngle(submarine.getAngle() - maxSteeringPerRound);
			double minusVelocity = normalizeVelocity(submarine.getVelocity() - maxAccelerationPerRound, maxSpeed);

			Set<DangerType> newDangerTypes;

			Position newSubmarinePosition = new Position(submarine.getPosition().getX().doubleValue() + MathUtil.xMovement(submarine.getVelocity(), plusAngle), submarine.getPosition().getY().doubleValue() + MathUtil.yMovement(submarine.getVelocity(), plusAngle));

			if (!moved) {
				newDangerTypes = MathUtil.getDangerTypes(gameInfo, newSubmarinePosition, submarine.getVelocity(), plusAngle, torpedos, enemySubmarines);
				if (newDangerTypes.isEmpty()) {
					callHandler.move(gameId, submarine.getId(), 0, maxSteeringPerRound);
					moved = true;
				} else if (newDangerTypes.contains(DangerType.HEADING_TO_TORPEDO_EXPLOSION)) {
					moveParameters.add(new MoveParameter(0, maxSteeringPerRound));
				}
			}

			newSubmarinePosition = new Position(submarine.getPosition().getX().doubleValue() + MathUtil.xMovement(submarine.getVelocity(), minusAngle), submarine.getPosition().getY().doubleValue() + MathUtil.yMovement(submarine.getVelocity(), minusAngle));

			if (!moved) {
				newDangerTypes = MathUtil.getDangerTypes(gameInfo, newSubmarinePosition, submarine.getVelocity(), minusAngle, torpedos, enemySubmarines);
				if (newDangerTypes.isEmpty()) {
					callHandler.move(gameId, submarine.getId(), 0, -maxSteeringPerRound);
					moved = true;
				} else if (newDangerTypes.contains(DangerType.HEADING_TO_TORPEDO_EXPLOSION)) {
					moveParameters.add(new MoveParameter(0, -maxSteeringPerRound));
				}
			}

			newSubmarinePosition = new Position(submarine.getPosition().getX().doubleValue() + MathUtil.xMovement(minusVelocity, minusAngle), submarine.getPosition().getY().doubleValue() + MathUtil.yMovement(minusVelocity, minusAngle));

			if (!moved) {
				newDangerTypes = MathUtil.getDangerTypes(gameInfo, newSubmarinePosition, minusVelocity, minusAngle, torpedos, enemySubmarines);
				if (newDangerTypes.isEmpty()) {
					callHandler.move(gameId, submarine.getId(), minusVelocity - submarine.getVelocity(), -maxSteeringPerRound);
					moved = true;
				} else if (newDangerTypes.contains(DangerType.HEADING_TO_TORPEDO_EXPLOSION)) {
					moveParameters.add(new MoveParameter(minusVelocity - submarine.getVelocity(), -maxSteeringPerRound));
				}
			}

			newSubmarinePosition = new Position(submarine.getPosition().getX().doubleValue() + MathUtil.xMovement(minusVelocity, plusAngle), submarine.getPosition().getY().doubleValue() + MathUtil.yMovement(minusVelocity, plusAngle));

			if (!moved) {
				newDangerTypes = MathUtil.getDangerTypes(gameInfo, newSubmarinePosition, minusVelocity, plusAngle, torpedos, enemySubmarines);
				if (newDangerTypes.isEmpty()) {
					callHandler.move(gameId, submarine.getId(), minusVelocity - submarine.getVelocity(), maxSteeringPerRound);
					moved = true;
				} else if (newDangerTypes.contains(DangerType.HEADING_TO_TORPEDO_EXPLOSION)) {
					moveParameters.add(new MoveParameter(minusVelocity - submarine.getVelocity(), maxSteeringPerRound));
				}
			}

			if (!moved && dangerTypes.contains(DangerType.LEAVING_SPACE)) {
				callHandler.move(gameId, submarine.getId(), minusVelocity - submarine.getVelocity(), MathUtil.getMoveParameterHeadingToEdge(submarine, submarineSize, width, height, extendedSonarRange, maxSteeringPerRound, maxAccelerationPerRound, maxSpeed).getSteering());
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
				System.out.println("Submarine: " + submarine + " HEADING TO TORPEDO acc: " + moveParameter.getAcceleration() + " steering: " + moveParameter.getSteering());
				callHandler.move(gameId, submarine.getId(), moveParameter.getAcceleration(), moveParameter.getSteering());
				moved = true;
			} else {
				List<Submarine> allSubmarines = new LinkedList<>();
				allSubmarines.addAll(enemySubmarines);
				allSubmarines.addAll(Arrays.asList(submarinesInGame.getSubmarines()));

				MoveParameter moveParameterBasedOnTorpedos;
				MoveParameter moveParameter;

				//Van-e ránk veszélyes torpedó?
				moveParameterBasedOnTorpedos = MathUtil.getMoveParameterBasedOnTorpedos(torpedos, allSubmarines, submarineSize, submarine, torpedoRange, torpedoSpeed, torpedoExplosionRadius, islandPositions, islandSize, maxSteeringPerRound, maxAccelerationPerRound, maxSpeed);
				//Ha nincs veszélyes torpedó
				if (moveParameterBasedOnTorpedos.getSteering() == 0.0) {
					moveParameter = getMoveParameterIfNotHeadingToTorpedo(submarine, allSubmarines);
				} else {
					moveParameter = moveParameterBasedOnTorpedos;
				}

				double angle = MathUtil.normalizeAngle(submarine.getAngle() + moveParameter.getSteering());
				double velocity = MathUtil.normalizeVelocity(submarine.getVelocity() + moveParameter.getAcceleration(), maxSpeed);
				Position newSubmarinePosition = new Position(submarine.getPosition().getX().doubleValue() + MathUtil.xMovement(submarine.getVelocity(), angle), submarine.getPosition().getY().doubleValue() + MathUtil.yMovement(submarine.getVelocity(), angle));
				Submarine newSubmarine = new Submarine(submarine.getType(), submarine.getId(), newSubmarinePosition, submarine.getOwner(), velocity, angle, submarine.getHp(), submarine.getSonarCooldown(), submarine.getTorpedoCooldown(), submarine.getSonarExtended());
				MoveParameter newMoveParameter = MathUtil.getMoveParameterBasedOnTorpedos(torpedos, allSubmarines, submarineSize, newSubmarine, torpedoRange, torpedoSpeed, torpedoExplosionRadius, islandPositions, islandSize, maxSteeringPerRound, maxAccelerationPerRound, maxSpeed);

				//Ha rá akarnánk fordulni egy torpedóra, az nem jó ötlet...
				if (newMoveParameter.getSteering() != 0) {
					if (moveParameter.getSteering() != 0) {
						moveParameter.setSteering(moveParameterBasedOnTorpedos.getSteering());
					}
				}
				callHandler.move(gameId, submarine.getId(), moveParameter.getAcceleration(), moveParameter.getSteering());
			}
		}
	}

	private MoveParameter getMoveParameterIfNotHeadingToTorpedo(Submarine submarine, List<Submarine> allSubmarines) {
		MoveParameter moveParameter;

		//Szonárnyi távolságon belül tartunk-e ki a pályáról?
		moveParameter = MathUtil.getMoveParameterHeadingToEdge(submarine, submarineSize, width, height, sonarRange, maxSteeringPerRound, maxAccelerationPerRound, maxSpeed);
		
		if (moveParameter.getSteering() == 0.0) {
			//Szonárnyi távolságon belül van-e sziget?
			Position nearestIslandPosition = MathUtil.getNearestIsland(gameInfo, submarine.getPosition());
			//Ha van sziget szonáron belül
			if (nearestIslandPosition != null) {
				moveParameter = MathUtil.getMoveParameterHeadingToIslandBasedOnSonar(submarine, nearestIslandPosition, maxSteeringPerRound, maxAccelerationPerRound, maxSpeed);
			}
		}

		if (moveParameter.getSteering() == 0.0) {
			//Van-e útban tengeralattjáró?
			Submarine submarineInOurWay = MathUtil.getNearestSubmarineInOurWay(submarine, allSubmarines, submarineSize, maxSpeed);
			//Ha van útban tengeralattjáró
			if (submarineInOurWay != null) {
				moveParameter = MathUtil.getMoveParameterHeadingToSubmarine(submarine, submarineInOurWay, submarineSize, maxSteeringPerRound, maxAccelerationPerRound, maxSpeed);
			}
		}

		if (moveParameter.getSteering() == 0.0) {
			//Ellenséges tengeralattjáró túl közel van-e, szonáron belül van-e, stb..
			moveParameter = MathUtil.getMoveParameterBasedOnEnemyPosition(submarine, enemySubmarines, sonarRange, maxSteeringPerRound, maxAccelerationPerRound, maxSpeed, allSubmarines, submarineSize, torpedoRange, torpedoSpeed, torpedoExplosionRadius, islandPositions, islandSize);
		}

		if (moveParameter.getSteering() == 0.0) {
			//Közel vannak-e a hajóink egymáshoz?
			moveParameter = MathUtil.getMoveParameterBasedOnSonars(submarine, Arrays.asList(submarinesInGame.getSubmarines()), sonarRange, extendedSonarRange, maxSteeringPerRound, maxAccelerationPerRound, maxSpeed);
		}
		
		//Ha hátul van a hajó, de bárrmi más miatt kanyarodik, akkor csak maradjon le a másik hajónktól.
		if(moveParameter.getAcceleration() != 0.0) {
			moveParameter.setAcceleration(MathUtil.getMoveParameterBasedOnSonars(submarine, Arrays.asList(submarinesInGame.getSubmarines()), sonarRange, extendedSonarRange, maxSteeringPerRound, maxAccelerationPerRound, maxSpeed).getAcceleration());
		}
		
		return moveParameter;
	}

	private void shoot(Submarine submarine) {
		Collections.sort(enemySubmarines, new Comparator<Submarine>() {

			@Override
			public int compare(Submarine lhs, Submarine rhs) {
				double lhsDistance = MathUtil.distanceOfCircles(submarine.getPosition(), submarineSize, lhs.getPosition(), submarineSize);
				double rhsDistance = MathUtil.distanceOfCircles(submarine.getPosition(), submarineSize, rhs.getPosition(), submarineSize);
				return Double.compare(lhsDistance, rhsDistance);
			}
		});
		
		for (Submarine enemySubmarine : enemySubmarines) {
			Double theta = aimAtMovingTarget(submarine.getPosition(), enemySubmarine.getPosition(), enemySubmarine.getAngle(), enemySubmarine.getVelocity(), torpedoSpeed);
			//boolean areWeInDanger = MathUtil.isSubmarineHeadingToTorpedoExplosion(torpedos, submarine.getPosition(), submarine.getVelocity(), submarine.getAngle(), submarineSize, enemySubmarines, torpedoExplosionRadius, islandPositions, islandSize);
			boolean shouldWeShoot = shouldWeShoot(submarine.getPosition(), Arrays.asList(submarinesInGame.getSubmarines()), submarineSize, enemySubmarine, torpedoRange, torpedoSpeed, theta, torpedoExplosionRadius, islandPositions, islandSize);
			if (theta != null && (/*areWeInDanger || */shouldWeShoot)) {
				if (submarine.getTorpedoCooldown() == 0.0) {
					callHandler.shoot(gameId, submarine.getId(), normalizeAngle(theta));
					break;
				}
			}
		}
	}

	private void updateMainPanel() {
		mainPanel.updateSubmarines(submarinesInGame.getSubmarines());

		mainPanel.addEnemySubmarines(enemySubmarines);
		mainPanel.addTorpedos(torpedos);

		mainPanel.repaint();
		mainPanel.revalidate();
	}

	private void updateGameInfo() {
		GameInfoResponse gameInfo = callHandler.gameInfo(gameId);
		if (gameInfo != null) {
			this.gameInfo = gameInfo;
		}
	}

}
