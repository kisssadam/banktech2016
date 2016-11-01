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

	public static double valamivelBezartSzog(Position sourcePosition, Position destinationPosition) {
		double yDest = Math.abs(destinationPosition.getY().subtract(sourcePosition.getY()).doubleValue());
		double xDest = Math.abs(destinationPosition.getX().subtract(sourcePosition.getX()).doubleValue());
		return Math.toDegrees(Math.atan(yDest / xDest));
	}

}
