package hu.javachallenge.torpedo.response;

import java.util.Arrays;

public class GameListResponse extends CommonResponse {

	private long[] games;

	public GameListResponse() {
		this(new long[0]);
	}

	public GameListResponse(long[] games) {
		super();
		this.games = games;
	}

	public long[] getGames() {
		return games;
	}

	public void setGames(long[] games) {
		this.games = games;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("GameListResponse [games=");
		builder.append(Arrays.toString(games));
		builder.append(", getMessage()=");
		builder.append(getMessage());
		builder.append(", getCode()=");
		builder.append(getCode());
		builder.append("]");
		return builder.toString();
	}

}
