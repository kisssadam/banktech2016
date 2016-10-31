package hu.javachallenge.torpedo.controller;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Converter;
import retrofit2.Response;

public class CallHandler {

	private static final Logger log = LoggerFactory.getLogger(CallHandler.class);
	
	private TorpedoApi torpedoApi;
	private Converter<ResponseBody, CommonResponse> converter;

	public CallHandler(ServiceGenerator serviceGenerator) {
		this.torpedoApi = serviceGenerator.getTorpedoApi();
		this.converter = serviceGenerator.getConverter();
	}

	public CreateGameResponse createGame() throws Exception {
		Call<CreateGameResponse> createGameCall = torpedoApi.createGame();
		try {
			Response<CreateGameResponse> response = createGameCall.execute();
			log.trace("CreateGameResponse {}", response.raw());
			if (response.isSuccessful()) {
				log.trace("CreateGameResponse {}", response.body());
				return response.body();
			} else {
				CommonResponse commonResponse = converter.convert(response.errorBody());
				log.trace("CreateGameResponse {}", commonResponse);
				testCode(commonResponse);
			}
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
				log.trace("GameListResponse {}", response.body());
				return response.body();
			} else {
				CommonResponse commonResponse = converter.convert(response.errorBody());
				log.trace("GameListResponse {}", commonResponse);
				testCode(commonResponse);
			}
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
				log.trace("JoinGameResponse {}", response.body());
				return response.body();
			} else {
				CommonResponse commonResponse = converter.convert(response.errorBody());
				log.trace("JoinGameResponse {}", commonResponse);
				testCode(commonResponse);
			}
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
				log.trace("GameInfoResponse {}", response.body());
				return response.body();
			} else {
				CommonResponse commonResponse = converter.convert(response.errorBody());
				log.trace("GameInfoResponse {}", commonResponse);
				testCode(commonResponse);
			}
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
				log.trace("SubmarinesResponse {}", response.body());
				return response.body();
			} else {
				CommonResponse commonResponse = converter.convert(response.errorBody());
				log.trace("SubmarinesResponse {}", commonResponse);
				testCode(commonResponse);
			}
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
				log.trace("MoveResponse {}", response.body());
				return response.body();
			} else {
				CommonResponse commonResponse = converter.convert(response.errorBody());
				log.trace("MoveResponse {}", commonResponse);
				testCode(commonResponse);
			}
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
				log.trace("SonarResponse {}", response.body());
				return response.body();
			} else {
				CommonResponse commonResponse = converter.convert(response.errorBody());
				log.trace("SonarResponse {}", commonResponse);
				testCode(commonResponse);
			}
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
				log.trace("ExtendSonarResponse {}", response.body());
				return response.body();
			} else {
				CommonResponse commonResponse = converter.convert(response.errorBody());
				log.trace("ExtendSonarResponse {}", commonResponse);
				testCode(commonResponse);
			}
		} catch (IOException e) {
			log.error("ExtendSonarResponse {}", e.toString());
			e.printStackTrace();
		}
		return null;
	}
	
	private void testCode(CommonResponse commonResponse) throws Exception {
		switch (commonResponse.getCode()) {
			case 0:
				return;
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
				log.error("Unhandled error: {}", commonResponse);
				throw new UnexpectedErrorCodeException(commonResponse.getMessage());
		}
	}
}
