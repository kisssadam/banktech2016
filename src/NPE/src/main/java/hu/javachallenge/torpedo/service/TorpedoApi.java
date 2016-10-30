package hu.javachallenge.torpedo.service;

import hu.javachallenge.torpedo.request.MoveRequest;
import hu.javachallenge.torpedo.request.ShootRequest;
import hu.javachallenge.torpedo.response.CreateGameResponse;
import hu.javachallenge.torpedo.response.ExtendSonarResponse;
import hu.javachallenge.torpedo.response.GameInfoResponse;
import hu.javachallenge.torpedo.response.GameListResponse;
import hu.javachallenge.torpedo.response.JoinGameResponse;
import hu.javachallenge.torpedo.response.MoveResponse;
import hu.javachallenge.torpedo.response.ShootResponse;
import hu.javachallenge.torpedo.response.SonarResponse;
import hu.javachallenge.torpedo.response.SubmarinesResponse;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface TorpedoApi {

	@POST("game")
	Call<CreateGameResponse> createGame();

	@GET("game")
	Call<GameListResponse> gameList();

	@POST("game/{gameId}")
	Call<JoinGameResponse> joinGame(@Path("gameId") long gameId);

	@GET("game/{gameId}")
	Call<GameInfoResponse> gameInfo(@Path("gameId") long gameId);

	@GET("game/{gameId}/submarine")
	Call<SubmarinesResponse> submarines(@Path("gameId") long gameId);

	@POST("game/{gameId}/submarine/{submarineId}/move")
	Call<MoveResponse> move(@Path("gameId") long gameId, @Path("submarineId") long submarineId,
			@Body MoveRequest moveRequest);

	@POST("game/{gameId}/submarine/{submarineId}/shoot")
	Call<ShootResponse> shoot(@Path("gameId") long gameId, @Path("submarineId") long submarineId,
			@Body ShootRequest shootRequest);

	@GET("game/{gameId}/submarine/{submarineId}/sonar")
	Call<SonarResponse> sonar(@Path("gameId") long gameId, @Path("submarineId") long submarineId);

	@POST("game/{gameId}/submarine/{submarineId}/sonar")
	Call<ExtendSonarResponse> extendSonar(@Path("gameId") long gameId, @Path("submarineId") long submarineId);

}
