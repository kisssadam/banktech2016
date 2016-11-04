package hu.javachallenge.torpedo.controller;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import hu.javachallenge.torpedo.model.Position;
import hu.javachallenge.torpedo.model.Submarine;
import hu.javachallenge.torpedo.response.CreateGameResponse;
import hu.javachallenge.torpedo.response.GameInfoResponse;
import hu.javachallenge.torpedo.response.SubmarinesResponse;
import hu.javachallenge.torpedo.service.ServiceGenerator;
import okhttp3.logging.HttpLoggingInterceptor;

public class Main {

	private static final Logger log = LoggerFactory.getLogger(Main.class);
	private static final String TEAMTOKEN = "4906CD1A4718F0B4F315BDE34B5FE430";

	public static void main(String[] args) {
		log.debug("Commad line arguments: {}", Arrays.toString(args));

		if (args.length != 1) {
			log.info("Usage: java -jar NPE.jar \"<server-address>\"");
			log.info("Example usage: java -jar NPE.jar \"localhost:8080\"");
			log.error("Missing or illegal command line argument(s), exiting...");
			System.exit(1);
		}

		String serverAddress = args[0];
		log.debug("serverAddress: '{}'", serverAddress);

		if (!serverAddress.endsWith("/")) {
			log.warn("Server address doesn't end with '/' character, it will be automatically appended to it!");
			serverAddress = serverAddress + '/';
			log.debug("New server address: '{}'", serverAddress);
		}

		ServiceGenerator serviceGenerator = new ServiceGenerator(serverAddress, TEAMTOKEN,
				HttpLoggingInterceptor.Level.BODY);
		CallHandler callHandler = new CallHandler(serviceGenerator);

		try {
			CreateGameResponse game = callHandler.createGame();
			long gameId = game.getId();
			// callHandler.gameList();
			// callHandler.joinGame(gameId);
			callHandler.gameInfo(gameId);
			SubmarinesResponse submarinesResponse = callHandler.submarinesInGame(gameId);
			Submarine[] submarines = submarinesResponse.getSubmarines();
			callHandler.shoot(gameId, submarines[0].getId(), 0.0);
			callHandler.sonar(gameId, submarines[0].getId());
			// callHandler.gameInfo(gameId);
			// double speed = 0.5;
			// double turn = -15.0;
			// callHandler.move(gameId, submarines[0].getId(), speed, turn);
			// callHandler.sonar(gameId, submarines[0].getId());
			// callHandler.extendSonar(gameId, submarines[0].getId());
		} catch (Exception e) {
			log.error(e.toString());
		}
	}

	public static double distance(Position sourcePosition, double sourceR, Position destinationPosition,
			double destinationR) {
		BigDecimal xSubtract = sourcePosition.getX().subtract(destinationPosition.getX());
		BigDecimal xSquare = xSubtract.multiply(xSubtract);

		BigDecimal ySubtract = sourcePosition.getY().subtract(destinationPosition.getY());
		BigDecimal ySquare = ySubtract.multiply(ySubtract);

		return Math.sqrt(xSquare.add(ySquare).doubleValue()) - (sourceR + destinationR);
	}

	public static double torpedoDistance(Position sourcePosition, Position destinationPosition, double destinationR) {
		return distance(sourcePosition, 0.0, destinationPosition, destinationR);
	}

	public static double torpedoDestinationAngle(Position sourcePosition, Position destinationPosition) {
		double srcX = sourcePosition.getX().doubleValue();
		double srcY = sourcePosition.getY().doubleValue();

		double destX = destinationPosition.getX().doubleValue();
		double destY = destinationPosition.getY().doubleValue();

		double distX = destinationPosition.getX().subtract(sourcePosition.getX()).abs().doubleValue();
		double distY = destinationPosition.getY().subtract(sourcePosition.getY()).abs().doubleValue();

		double angle = Math.toDegrees(Math.atan(distY / distX));

		if (destX > srcX && destY == srcY) {
			return 0.0;
		}
		if (destX == srcX && destY > srcY) {
			return 90.0;
		}
		if (destX < srcX && destY == srcY) {
			return 180.0;
		}
		if (destX == srcX && destY < srcY) {
			return 270.0;
		}

		if (destX > srcX && destY > srcY) {
			return angle;
		}
		if (destX < srcX && destY > srcY) {
			return 180.0 - angle;
		}
		if (destX < srcX && destY < srcY) {
			return 180.0 + angle;
		}
		if (destX > srcX && destY < srcY) {
			return 360.0 - angle;
		}

		log.warn("None of the if statements were executed in torpedoDestinationAngle. Returning angle: {}", angle);
		return angle;
	}

	/**
	 *
	 * @param velocity
	 *            átfogó
	 * @param angle
	 *            szög
	 * @return szomszédos befogó
	 */
	public static double xMovement(double velocity, double angle) {
		return velocity * Math.cos(angle);
	}

	public static double yMovement(double velocity, double angle) {
		return velocity * Math.sin(angle);
	}

	// https://www.kirupa.com/forum/showthread.php?347334-Aiming-and-hitting-a-moving-target
	public static double aimAtMovingTarget(Position sourcePosition, Position targetPosition, double targetMovementAngle,
			double targetVelocity, double bulletVelocity) {
		double dX = targetPosition.getX().subtract(sourcePosition.getX()).doubleValue();
		double dY = targetPosition.getY().subtract(sourcePosition.getY()).doubleValue();

		double xMovementResult = xMovement(targetVelocity, Math.toRadians(targetMovementAngle));
		double yMovementResult = yMovement(targetVelocity, Math.toRadians(targetMovementAngle));

		double a = Math.pow(xMovementResult, 2) + Math.pow(yMovementResult, 2) - Math.pow(bulletVelocity, 2);
		double b = 2 * (xMovementResult * dX + yMovementResult * dY);
		double c = Math.pow(dX, 2) + Math.pow(dY, 2);

		// Check we're not breaking into complex numbers
		double q = Math.pow(b, 2) - 4 * a * c;
		if (q < 0.0) {
			log.error("Math.pow({}) cannot be interpreted on negative numbers.", q);
			return 0.0;
		}

		// The time that we will hit the target
		double time = ((a < 0.0 ? -1.0 : 1.0) * Math.sqrt(q) - b) / (2.0 * a);

		// Aim for where the target will be after time t
		dX += time * xMovementResult;
		dY += time * yMovementResult;

		double theta = Math.toDegrees(Math.atan2(dY, dX));
		return theta;
	}

	// http://stackoverflow.com/questions/1571294/line-equation-with-angle
	public static double meredekség(double angle) {
		return Math.tan(Math.toRadians(angle));
	}

	// amit célzunk és köztünk van-e sziget
	public static List<Submarine> getPossibleTargets(GameInfoResponse gameInfoResponse, Position[] islandPositions,
			Position sourcePosition, Submarine[] submarines, double bulletVelocity) {
		List<Submarine> possibleSubmarineTargets = new ArrayList<>();

		for (Submarine submarine : submarines) {
			double theta = aimAtMovingTarget(sourcePosition, submarine.getPosition(), submarine.getAngle(), submarine.getVelocity(), bulletVelocity);
			for (Position islandPosition : islandsInDirection(gameInfoResponse, sourcePosition, theta)) {
				double submarineSize = gameInfoResponse.getGame().getMapConfiguration().getSubmarineSize();
				double islandSize = gameInfoResponse.getGame().getMapConfiguration().getIslandSize();

				double islandDistance = distance(sourcePosition, submarineSize, islandPosition, islandSize);

				double submarineXMovement = xMovement(submarine.getVelocity(), submarine.getAngle());
				double submarineYMovement = yMovement(submarine.getVelocity(), submarine.getAngle());
				Position newSubmarinePosition = new Position(submarineXMovement, submarineYMovement);

				double submarineDistance = distance(sourcePosition, submarineSize, newSubmarinePosition, submarineSize);

				if (islandDistance > submarineDistance) {
					possibleSubmarineTargets.add(submarine);
				}
			}
		}

		return possibleSubmarineTargets;
	}
	
	// TODO mi veszélyben vagyunk-e ha célzunk-e valamit, azaz, magunkat lőjük-e.
//	public static boolean isTorpedoDangerous(GameInfoResponse gameInfoResponse, Position submarinePosition, double submarineAngle, double submarineVelocity, Position torpedoPosition, double torpedoAngle) {
//		double torpedoSpeed = gameInfoResponse.getGame().getMapConfiguration().getTorpedoSpeed();
//		double theta = aimAtMovingTarget(torpedoPosition, submarinePosition, submarineAngle, submarineVelocity, torpedoSpeed);
//		
//		return false;
//	}
	
	
	public static Position getNearestIslandInDirection(List<Position> islandsInDirection) {
		return islandsInDirection.isEmpty() ? null : islandsInDirection.get(0);
	}
	
	private static class Island {
		private Position position;
		private double distance;
		
		public Island(Position position, double distance) {
			this.position = position;
			this.distance = distance;
		}
	}
	
	public static List<Position> islandsInDirection(GameInfoResponse gameInfoResponse, Position sourcePosition,
			double angle) {
		List<Island> islandsInDirection = new ArrayList<>();

		Position[] islandPositions = gameInfoResponse.getGame().getMapConfiguration().getIslandPositions();
		double islandSize = gameInfoResponse.getGame().getMapConfiguration().getIslandSize();
		double submarineSize = gameInfoResponse.getGame().getMapConfiguration().getSubmarineSize();

		for (Position islandPosition : islandPositions) {
			double meredekség = meredekség(angle);
			double a = Math.pow(meredekség, 2) + 1;

			double srcX = sourcePosition.getX().doubleValue();
			double srcY = sourcePosition.getY().doubleValue();

			double c2 = srcY - meredekség * srcX;

			double islandX = islandPosition.getX().doubleValue();
			double islandY = islandPosition.getY().doubleValue();

			double b = 2 * (meredekség * c2 - meredekség * islandY - islandX);
			double c1 = Math.pow(islandY, 2) - Math.pow(islandSize, 2) + Math.pow(islandX, 2) - 2 * c2 * islandY
					+ Math.pow(c2, 2);

			if (Math.pow(b, 2) - 4 * a * c1 >= 0) {
				double distance = distance(sourcePosition, submarineSize, islandPosition, islandSize);
				islandsInDirection.add(new Island(islandPosition, distance));
			}
		}

		Collections.sort(islandsInDirection, (island1, island2) -> island1.distance <= island2.distance ? -1 : 1);
		
		return islandsInDirection.stream().map(island -> island.position).collect(Collectors.toList());
	}
	
	public static Position collisionPosition(double submarineSize, Position submarinePosition, double submarineVelocity, double submarineAngle, Position torpedoPosition, double torpedoVelocity, double torpedoAngle) {
		return movingCircleCollisionDetection(torpedoPosition, torpedoVelocity, torpedoAngle, 0, submarinePosition, submarineVelocity, submarineAngle, submarineSize);
	}

	public static List<Position> islandsInSubmarineDirection(Position submarinePosition, double submarineVelocity, double submarineAngle,
			double submarineSize, Position[] islandPositions, double islandSize) {
		List<Position> islandsInSubmarineDirections = new ArrayList<>();
		
		for (Position islandPosition : islandPositions) {
			Position position = movingCircleCollisionDetection(submarinePosition, submarineVelocity, submarineAngle, submarineSize, islandPosition, 0, 0, islandSize);
			islandsInSubmarineDirections.add(position);
		}
		
		return islandsInSubmarineDirections;
	}
	
	private static Position movingCircleCollisionDetection(Position sourcePosition, double sourceVelocity, double sourceAngle, double sourceSize,
			Position targetPosition, double targetVelocity, double targetAngle, double targetSize) {
		
		Position Pab = subtract(sourcePosition, targetPosition);
		Position sourceVelocityVector = new Position(xMovement(sourceVelocity, Math.toRadians(sourceAngle)), yMovement(sourceVelocity, Math.toRadians(sourceAngle)));
		Position targetVelocityVector = new Position(xMovement(targetVelocity, Math.toRadians(targetAngle)), yMovement(targetVelocity, Math.toRadians(targetAngle)));
		
		Position Vab = subtract(sourceVelocityVector, targetVelocityVector);
		double a = Vab.getX().pow(2).add(Vab.getY().pow(2)).doubleValue();
		double b = 2 * (Vab.getX().multiply(Pab.getX()).add(Vab.getY().multiply(Pab.getY()))).doubleValue();
		double c = Pab.getX().pow(2).add(Pab.getY().pow(2)).doubleValue() - Math.pow(targetSize + sourceSize, 2);
		
		double q = Math.pow(b, 2) - 4 * a * c;
		if (q < 0.0) {
			log.warn("collisionPoisition q={}", q);
			return null;
		}
		
		double disc = Math.sqrt(q);
		if (disc < 0) {
			return null;
		}
		
		double root_1 = (-b + disc) / (2 * a);
		double root_2 = (-b - disc) / (2 * a);
		if (root_1 < 0.0 && root_2 < 0.0) {
			return null;
		}
		double min = root_1 <= root_2 ? root_1 : root_2;
		if (min < 0.0) {
			if (root_1 >= 0.0) {
				min = root_1;
			} else {
				min = root_2;
			}
		}
		
		Position newSourcePosition = new Position(
				sourcePosition.getX().add(sourceVelocityVector.getX().multiply(BigDecimal.valueOf(min))).doubleValue(),
				sourcePosition.getY().add(sourceVelocityVector.getY().multiply(BigDecimal.valueOf(min))).doubleValue());
		
		Position newTargetPosition = new Position(
				targetPosition.getX().add(targetVelocityVector.getX().multiply(BigDecimal.valueOf(min))).doubleValue(),
				targetPosition.getY().add(targetVelocityVector.getY().multiply(BigDecimal.valueOf(min))).doubleValue());
		
		if (targetSize == 0.0) {
			return newTargetPosition;
		}
		
		if (sourceSize == 0.0) {
			return newSourcePosition;
		}

		Position position = subtract(newSourcePosition, newTargetPosition);
		
		double pX = position.getX().doubleValue() * (sourceSize / targetSize);
		double pY = position.getY().doubleValue() * (sourceSize / targetSize);
		Position p = new Position(pX, pY);
		
		return subtract(newSourcePosition, p);
	}
	
	public static Position subtract(Position lhs, Position rhs) {
		return new Position(lhs.getX().subtract(rhs.getX()), lhs.getY().subtract(rhs.getY()));
	}

}
