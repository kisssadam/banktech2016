/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hu.javachallenge.torpedo.controller;

import hu.javachallenge.torpedo.exception.AccelerationIsTooBigException;
import hu.javachallenge.torpedo.exception.CallBeforeRefillException;
import hu.javachallenge.torpedo.exception.GameIsInProgressException;
import hu.javachallenge.torpedo.exception.NonexistantGameIdException;
import hu.javachallenge.torpedo.exception.TeamIsNotInvitedException;
import hu.javachallenge.torpedo.exception.TheGameIsNotInProgressException;
import hu.javachallenge.torpedo.exception.TheGivenShipHasAlreadyMovedException;
import hu.javachallenge.torpedo.exception.TheTeamHasNoAccessToHandleTheGivenSubmarineException;
import hu.javachallenge.torpedo.exception.TorpedoIsOnCooldown;
import hu.javachallenge.torpedo.exception.TurningIsTooBigException;
import hu.javachallenge.torpedo.exception.UnexpectedErrorCodeException;
import hu.javachallenge.torpedo.request.MoveRequest;
import hu.javachallenge.torpedo.response.CommonResponse;
import hu.javachallenge.torpedo.response.CreateGameResponse;
import hu.javachallenge.torpedo.response.ExtendSonarResponse;
import hu.javachallenge.torpedo.response.GameInfoResponse;
import hu.javachallenge.torpedo.response.GameListResponse;
import hu.javachallenge.torpedo.response.JoinGameResponse;
import hu.javachallenge.torpedo.response.MoveResponse;
import hu.javachallenge.torpedo.response.SonarResponse;
import hu.javachallenge.torpedo.response.SubmarinesResponse;
import hu.javachallenge.torpedo.service.ServiceGenerator;
import hu.javachallenge.torpedo.service.TorpedoApi;
import java.io.IOException;
import okhttp3.ResponseBody;
import okhttp3.logging.HttpLoggingInterceptor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import retrofit2.Call;
import retrofit2.Converter;
import retrofit2.Response;

/**
 *
 * @author Czuczi
 */
public class CallHandler {

	private static final Logger log = LoggerFactory.getLogger(CallHandler.class);
	private static final String TEAMTOKEN = "4906CD1A4718F0B4F315BDE34B5FE430";
	private String serverAddress;
	private TorpedoApi torpedoApi;
	ServiceGenerator serviceGenerator;
	Converter<ResponseBody, CommonResponse> converter;

	public CallHandler(String serverAddress) {
		this.serverAddress = serverAddress;
		serviceGenerator = new ServiceGenerator(serverAddress, TEAMTOKEN, HttpLoggingInterceptor.Level.BODY);

		torpedoApi = serviceGenerator.getTorpedoApi();
		converter = serviceGenerator.getConverter();
	}

	public CreateGameResponse createGame() throws Exception {
		Call<CreateGameResponse> createGameCall = torpedoApi.createGame();
		try {
			Response<CreateGameResponse> response = createGameCall.execute();
			log.trace("CreateGameResponse {}", response.raw());
			if (response.isSuccessful()) {
				return response.body();
			} else {
				CommonResponse commonResponse = converter.convert(response.errorBody());
				testCode(commonResponse);
			}

			log.trace("CreateGameResponse {}",
				response.isSuccessful() ? response.body() : converter.convert(response.errorBody()));
		} catch (IOException e) {
			log.error("CreateGameResponse {}", e.toString());
			e.printStackTrace();
		}
		return null;
	}

	public GameListResponse gameList() throws Exception {
		Call<GameListResponse> gameListCall = torpedoApi.gameList();
		try {
			Response<GameListResponse> response = gameListCall.execute();
			log.trace("GameListResponse {}", response.raw());
			if (response.isSuccessful()) {
				return response.body();
			} else {
				CommonResponse commonResponse = converter.convert(response.errorBody());
				testCode(commonResponse);
			}
			log.trace("GameListResponse {}",
				response.isSuccessful() ? response.body() : converter.convert(response.errorBody()));
		} catch (IOException e) {
			log.error("GameListResponse {}", e.toString());
			e.printStackTrace();
		}
		return null;
	}

	public JoinGameResponse joinGame(long gameId) throws Exception {
		Call<JoinGameResponse> joinGameCall = torpedoApi.joinGame(gameId);
		try {
			Response<JoinGameResponse> response = joinGameCall.execute();
			log.trace("JoinGameResponse {}", response.raw());
			if (response.isSuccessful()) {
				return response.body();
			} else {
				CommonResponse commonResponse = converter.convert(response.errorBody());
				testCode(commonResponse);
			}
			log.trace("JoinGameResponse {}",
				response.isSuccessful() ? response.body() : converter.convert(response.errorBody()));
		} catch (IOException e) {
			log.error("JoinGameResponse {}", e.toString());
			e.printStackTrace();
		}
		return null;
	}

	public GameInfoResponse gameInfo(long gameId) throws Exception {
		Call<GameInfoResponse> gameInfoCall = torpedoApi.gameInfo(gameId);
		try {
			Response<GameInfoResponse> response = gameInfoCall.execute();
			log.trace("GameInfoResponse {}", response.raw());
			if (response.isSuccessful()) {
				return response.body();
			} else {
				CommonResponse commonResponse = converter.convert(response.errorBody());
				testCode(commonResponse);
			}
			log.trace("GameInfoResponse {}",
				response.isSuccessful() ? response.body() : converter.convert(response.errorBody()));
		} catch (IOException e) {
			log.error("GameInfoResponse {}", e.toString());
			e.printStackTrace();
		}
		return null;
	}

	public SubmarinesResponse submarinesInGame(long gameId) throws Exception {
		Call<SubmarinesResponse> submarinesCall = torpedoApi.submarines(gameId);
		try {
			Response<SubmarinesResponse> response = submarinesCall.execute();
			log.trace("SubmarinesResponse {}", response.raw());
			if (response.isSuccessful()) {
				return response.body();
			} else {
				CommonResponse commonResponse = converter.convert(response.errorBody());
				testCode(commonResponse);
			}
			log.trace("SubmarinesResponse {}",
				response.isSuccessful() ? response.body() : converter.convert(response.errorBody()));
		} catch (IOException e) {
			log.error("SubmarinesResponse {}", e.toString());
			e.printStackTrace();
		}
		return null;
	}

	public MoveResponse move(long gameId, long submarineId, double speed, double turn) throws Exception {
		Call<MoveResponse> moveCall = torpedoApi.move(gameId, submarineId, new MoveRequest(speed, turn));
		try {
			Response<MoveResponse> response = moveCall.execute();
			log.trace("MoveResponse {}", response.raw());
			if (response.isSuccessful()) {
				return response.body();
			} else {
				CommonResponse commonResponse = converter.convert(response.errorBody());
				testCode(commonResponse);
			}
			log.trace("MoveResponse {}",
				response.isSuccessful() ? response.body() : converter.convert(response.errorBody()));
		} catch (IOException e) {
			log.error("MoveResponse {}", e.toString());
			e.printStackTrace();
		}
		return null;
	}

	public SonarResponse sonar(long gameId, long submarineId) throws Exception {
		Call<SonarResponse> sonarCall = torpedoApi.sonar(gameId, submarineId);
		try {
			Response<SonarResponse> response = sonarCall.execute();
			log.trace("SonarResponse {}", response.raw());
			if (response.isSuccessful()) {
				return response.body();
			} else {
				CommonResponse commonResponse = converter.convert(response.errorBody());
				testCode(commonResponse);
			}
			log.trace("SonarResponse {}",
				response.isSuccessful() ? response.body() : converter.convert(response.errorBody()));
		} catch (IOException e) {
			log.error("SonarResponse {}", e.toString());
			e.printStackTrace();
		}
		return null;
	}

	public ExtendSonarResponse extendSonar(long gameId, long submarineId) throws Exception {
		Call<ExtendSonarResponse> extendSonarCall = torpedoApi.extendSonar(gameId, submarineId);
		try {
			Response<ExtendSonarResponse> response = extendSonarCall.execute();
			log.trace("ExtendSonarResponse {}", response.raw());
			if (response.isSuccessful()) {
				return response.body();
			} else {
				CommonResponse commonResponse = converter.convert(response.errorBody());
				testCode(commonResponse);
			}
			log.trace("ExtendSonarResponse {}",
					response.isSuccessful() ? response.body() : converter.convert(response.errorBody()));
		} catch (IOException e) {
			log.error("ExtendSonarResponse {}", e.toString());
			e.printStackTrace();
		}
		return null;
	}
	
	private void testCode(CommonResponse commonResponse) throws Exception {
		switch (commonResponse.getCode()) {
			case 1:
				throw new TeamIsNotInvitedException(commonResponse.getMessage());
			case 2:
				throw new GameIsInProgressException(commonResponse.getMessage());
			case 3:
				throw new NonexistantGameIdException(commonResponse.getMessage());
			case 4:
				throw new TheTeamHasNoAccessToHandleTheGivenSubmarineException(commonResponse.getMessage());
			case 7:
				throw new TorpedoIsOnCooldown(commonResponse.getMessage());
			case 8:
				throw new CallBeforeRefillException(commonResponse.getMessage());
			case 9:
				throw new TheGameIsNotInProgressException(commonResponse.getMessage());
			case 10:
				throw new TheGivenShipHasAlreadyMovedException(commonResponse.getMessage());
			case 11:
				throw new AccelerationIsTooBigException(commonResponse.getMessage());
			case 12:
				throw new TurningIsTooBigException(commonResponse.getMessage());
			default:
				System.out.println("lefutott");
				throw new UnexpectedErrorCodeException(commonResponse.getMessage());
		}
	}
}
