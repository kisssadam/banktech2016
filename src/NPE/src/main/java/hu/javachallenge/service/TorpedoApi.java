package hu.javachallenge.service;

import hu.javachallenge.response.CreateGameResponse;
import hu.javachallenge.response.GameInfoResponse;
import hu.javachallenge.response.GameListResponse;
import hu.javachallenge.response.JoinGameResponse;
import retrofit2.Call;
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

}
