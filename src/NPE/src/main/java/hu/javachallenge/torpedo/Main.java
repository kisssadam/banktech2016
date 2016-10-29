package hu.javachallenge.torpedo;

import java.io.IOException;
import java.util.Arrays;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import hu.javachallenge.response.CommonResponse;
import hu.javachallenge.response.GameInfoResponse;
import hu.javachallenge.response.GameListResponse;
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

//		Call<CreateGameResponse> call = torpedoApi.createGame();
//		try {
//			Response<CreateGameResponse> response = call.execute();
//			System.out.println(response.body());
//		} catch (IOException e) {
//			e.printStackTrace();
//		}

		Call<GameListResponse> gameList = torpedoApi.gameList();
		try {
			System.out.println(gameList.execute().body());
		} catch (IOException e) {
			e.printStackTrace();
		}
		
//		Call<JoinGameResponse> call = torpedoApi.joinGame(2067620462L);
//		try {
//			Response<JoinGameResponse> response = call.execute();
//			System.out.println(response.raw());
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
		
		Call<GameInfoResponse> gameInfoCall = torpedoApi.gameInfo(2067620462);
		try {
			Response<GameInfoResponse> response = gameInfoCall.execute();
			System.out.println(response.raw());
			System.out.println(response.body());
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
