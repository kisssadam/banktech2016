package hu.javachallenge.torpedo.controller;

import java.math.BigDecimal;
import java.util.Arrays;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import hu.javachallenge.torpedo.model.Position;
import hu.javachallenge.torpedo.model.Submarine;
import hu.javachallenge.torpedo.response.CreateGameResponse;
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
		double distY = Math.abs(destinationPosition.getY().subtract(sourcePosition.getY()).doubleValue());
		double distX = Math.abs(destinationPosition.getX().subtract(sourcePosition.getX()).doubleValue());

		double angle = Math.toDegrees(Math.atan(distY / distX));

		double destX = destinationPosition.getX().doubleValue();
		double destY = destinationPosition.getY().doubleValue();

		double srcX = sourcePosition.getX().doubleValue();
		double srcY = sourcePosition.getY().doubleValue();

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
			return 270;
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

		log.info("SHOULD NOT HAPPEN!");
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
		double q = Math.pow(b, 2) - 4 * a * c; // kvóciens
		if (q < 0.0) {
			log.error("Something went wrong.");
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

}
