package hu.javachallenge.torpedo;

import java.io.IOException;
import java.util.Arrays;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import hu.javachallenge.request.MoveRequest;
import hu.javachallenge.request.ShootRequest;
import hu.javachallenge.response.ExtendSonarResponse;
import hu.javachallenge.response.GameInfoResponse;
import hu.javachallenge.response.GameListResponse;
import hu.javachallenge.response.JoinGameResponse;
import hu.javachallenge.response.ShootResponse;
import hu.javachallenge.response.SonarResponse;
import hu.javachallenge.response.SubmarinesResponse;
import hu.javachallenge.service.MoveResponse;
import hu.javachallenge.service.ServiceGenerator;
import hu.javachallenge.service.TorpedoApi;
import retrofit2.Call;
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

		TorpedoApi torpedoApi = ServiceGenerator.getClient(serverAddress, TEAMTOKEN);

		System.out.println();

		// Call<CreateGameResponse> call = torpedoApi.createGame();
		// try {
		// Response<CreateGameResponse> response = call.execute();
		// System.out.println(response.body());
		// } catch (IOException e) {
		// e.printStackTrace();
		// }

		Call<GameListResponse> gameList = torpedoApi.gameList();
		try {
			Response<GameListResponse> response = gameList.execute();
			System.out.println("GameListResponse " + response.raw());
			System.out.println("GameListResponse " + response.body());
			System.out.println();
		} catch (IOException e) {
			e.printStackTrace();
		}

		Call<JoinGameResponse> call = torpedoApi.joinGame(2067620462);
		try {
			Response<JoinGameResponse> response = call.execute();
			System.out.println("JoinGameResponse " + response.raw());
			System.out.println("JoinGameResponse " + response.body());
			System.out.println();
		} catch (IOException e) {
			e.printStackTrace();
		}

		Call<GameInfoResponse> gameInfoCall = torpedoApi.gameInfo(2067620462);
		try {
			Response<GameInfoResponse> response = gameInfoCall.execute();
			System.out.println("GameInfoResponse " + response.raw());
			System.out.println("GameInfoResponse " + response.body());
			System.out.println();
		} catch (IOException e) {
			e.printStackTrace();
		}

		Call<SubmarinesResponse> submarinesCall = torpedoApi.submarines(2067620462);
		try {
			Response<SubmarinesResponse> response = submarinesCall.execute();
			System.out.println("SubmarinesResponse " + response.raw());
			System.out.println("SubmarinesResponse " + response.body());
			System.out.println();
		} catch (IOException e) {
			e.printStackTrace();
		}

		// double speed = 0.5;
		// double turn = -15.0;
		// Call<MoveResponse> moveCall = torpedoApi.move(2067620462, 786, new
		// MoveRequest(speed, turn));
		// try {
		// Response<MoveResponse> response = moveCall.execute();
		// System.out.println("MoveResponse " + response.raw());
		// System.out.println("MoveResponse " + response.body());
		// System.out.println();
		// } catch (IOException e) {
		// e.printStackTrace();
		// }

		double angle = 1.1;
		Call<ShootResponse> shootCall = torpedoApi.shoot(2067620462, 786, new ShootRequest(angle));
		try {
			Response<ShootResponse> response = shootCall.execute();
			System.out.println("ShootResponse " + response.raw());
			System.out.println("ShootResponse " + response.body());
			System.out.println();
		} catch (IOException e) {
			e.printStackTrace();
		}

		Call<SonarResponse> sonarCall = torpedoApi.sonar(2067620462, 786);
		try {
			Response<SonarResponse> response = sonarCall.execute();
			System.out.println("SonarResponse " + response.raw());
			System.out.println("SonarResponse " + response.body());
			System.out.println();
		} catch (IOException e) {
			e.printStackTrace();
		}

		Call<ExtendSonarResponse> extendSonarCall = torpedoApi.extendSonar(2067620462, 786);
		try {
			Response<ExtendSonarResponse> response = extendSonarCall.execute();
			System.out.println("ExtendSonarResponse " + response.raw());
			System.out.println("ExtendSonarResponse " + response.body());
			System.out.println();
		} catch (IOException e) {
			e.printStackTrace();
		}

		// call.enqueue(new Callback<CreateGameResponse>() {
		//
		// @Override
		// public void onResponse(Call<CreateGameResponse> call,
		// Response<CreateGameResponse> response) {
		// log.trace("onResponse call: {}, response: {}", call, response);
		// }
		//
		// @Override
		// public void onFailure(Call<CreateGameResponse> call, Throwable
		// throwable) {
		// log.trace("onFailure call: {}, throwable: {}", call, throwable);
		// }
		// });
	};

}
