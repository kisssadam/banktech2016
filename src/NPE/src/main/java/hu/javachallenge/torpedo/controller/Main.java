package hu.javachallenge.torpedo.controller;

import java.util.Arrays;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import hu.javachallenge.torpedo.service.ServiceGenerator;
import okhttp3.logging.HttpLoggingInterceptor;

public class Main {

	private static final Logger log = LoggerFactory.getLogger(Main.class);
	private static final String TEAMTOKEN = "4906CD1A4718F0B4F315BDE34B5FE430";
	private static final String TEAMNAME = "NPE";

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

		ServiceGenerator serviceGenerator = new ServiceGenerator(serverAddress, TEAMTOKEN, HttpLoggingInterceptor.Level.NONE);
		CallHandler callHandler = new CallHandler(serviceGenerator);

		Thread gameControllerThread = new Thread(new GameController(callHandler, TEAMNAME));
		gameControllerThread.start();
		try {
			gameControllerThread.join();
		} catch (Exception e) {
		} finally {
			System.exit(0);
		}
	}

}
