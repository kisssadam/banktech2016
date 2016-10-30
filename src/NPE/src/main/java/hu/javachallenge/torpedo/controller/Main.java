package hu.javachallenge.torpedo.controller;

import java.io.IOException;
import java.util.Arrays;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import hu.javachallenge.torpedo.request.MoveRequest;
import hu.javachallenge.torpedo.request.ShootRequest;
import hu.javachallenge.torpedo.response.CommonResponse;
import hu.javachallenge.torpedo.response.CreateGameResponse;
import hu.javachallenge.torpedo.response.ExtendSonarResponse;
import hu.javachallenge.torpedo.response.GameInfoResponse;
import hu.javachallenge.torpedo.response.GameListResponse;
import hu.javachallenge.torpedo.response.JoinGameResponse;
import hu.javachallenge.torpedo.response.MoveResponse;
import hu.javachallenge.torpedo.response.ShootResponse;
import hu.javachallenge.torpedo.response.SonarResponse;
import hu.javachallenge.torpedo.response.SubmarinesResponse;
import hu.javachallenge.torpedo.service.ServiceGenerator;
import hu.javachallenge.torpedo.service.TorpedoApi;
import okhttp3.ResponseBody;
import okhttp3.logging.HttpLoggingInterceptor.Level;
import retrofit2.Call;
import retrofit2.Converter;
import retrofit2.Response;

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

		ServiceGenerator serviceGenerator = new ServiceGenerator(serverAddress, TEAMTOKEN, Level.BODY);

		TorpedoApi torpedoApi = serviceGenerator.getTorpedoApi();
		Converter<ResponseBody, CommonResponse> converter = serviceGenerator.getConverter();

		Call<CreateGameResponse> createGameCall = torpedoApi.createGame();
		try {
			Response<CreateGameResponse> response = createGameCall.execute();
			log.trace("CreateGameResponse {}", response.raw());
			log.trace("CreateGameResponse {}",
					response.isSuccessful() ? response.body() : converter.convert(response.errorBody()));
		} catch (IOException e) {
			log.error("CreateGameResponse {}", e.toString());
			e.printStackTrace();
		}

		Call<GameListResponse> gameListCall = torpedoApi.gameList();
		try {
			Response<GameListResponse> response = gameListCall.execute();
			log.trace("GameListResponse {}", response.raw());
			log.trace("GameListResponse {}",
					response.isSuccessful() ? response.body() : converter.convert(response.errorBody()));
		} catch (IOException e) {
			log.error("GameListResponse {}", e.toString());
			e.printStackTrace();
		}

		Call<JoinGameResponse> joinGameCall = torpedoApi.joinGame(2067620462);
		try {
			Response<JoinGameResponse> response = joinGameCall.execute();
			log.trace("JoinGameResponse {}", response.raw());
			log.trace("JoinGameResponse {}",
					response.isSuccessful() ? response.body() : converter.convert(response.errorBody()));
		} catch (IOException e) {
			log.error("JoinGameResponse {}", e.toString());
			e.printStackTrace();
		}

		Call<GameInfoResponse> gameInfoCall = torpedoApi.gameInfo(2067620462);
		try {
			Response<GameInfoResponse> response = gameInfoCall.execute();
			log.trace("GameInfoResponse {}", response.raw());
			log.trace("GameInfoResponse {}",
					response.isSuccessful() ? response.body() : converter.convert(response.errorBody()));
		} catch (IOException e) {
			log.error("GameInfoResponse {}", e.toString());
			e.printStackTrace();
		}

		Call<SubmarinesResponse> submarinesCall = torpedoApi.submarines(2067620462);
		try {
			Response<SubmarinesResponse> response = submarinesCall.execute();
			log.trace("SubmarinesResponse {}", response.raw());
			log.trace("SubmarinesResponse {}",
					response.isSuccessful() ? response.body() : converter.convert(response.errorBody()));
		} catch (IOException e) {
			log.error("SubmarinesResponse {}", e.toString());
			e.printStackTrace();
		}

		double speed = 0.5;
		double turn = -15.0;
		Call<MoveResponse> moveCall = torpedoApi.move(2067620462, 786, new MoveRequest(speed, turn));
		try {
			Response<MoveResponse> response = moveCall.execute();
			log.trace("MoveResponse {}", response.raw());
			log.trace("MoveResponse {}",
					response.isSuccessful() ? response.body() : converter.convert(response.errorBody()));
		} catch (IOException e) {
			log.error("MoveResponse {}", e.toString());
			e.printStackTrace();
		}

		double angle = 1.1;
		Call<ShootResponse> shootCall = torpedoApi.shoot(2067620462, 786, new ShootRequest(angle));
		try {
			Response<ShootResponse> response = shootCall.execute();
			log.trace("ShootResponse {}", response.raw());
			log.trace("ShootResponse {}",
					response.isSuccessful() ? response.body() : converter.convert(response.errorBody()));
		} catch (IOException e) {
			log.error("ShootResponse {}", e.toString());
			e.printStackTrace();
		}

		Call<SonarResponse> sonarCall = torpedoApi.sonar(2067620462, 786);
		try {
			Response<SonarResponse> response = sonarCall.execute();
			log.trace("SonarResponse {}", response.raw());
			log.trace("SonarResponse {}",
					response.isSuccessful() ? response.body() : converter.convert(response.errorBody()));
		} catch (IOException e) {
			log.error("SonarResponse {}", e.toString());
			e.printStackTrace();
		}

		Call<ExtendSonarResponse> extendSonarCall = torpedoApi.extendSonar(2067620462, 786);
		try {
			Response<ExtendSonarResponse> response = extendSonarCall.execute();
			log.trace("ExtendSonarResponse {}", response.raw());
			log.trace("ExtendSonarResponse {}",
					response.isSuccessful() ? response.body() : converter.convert(response.errorBody()));
		} catch (IOException e) {
			log.error("ExtendSonarResponse {}", e.toString());
			e.printStackTrace();
		}
	};

}
