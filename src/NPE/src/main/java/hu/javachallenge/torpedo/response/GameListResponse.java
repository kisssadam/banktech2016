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
	public int hashCode() {
		final int prime = 11321;
		int result = super.hashCode();
		result = prime * result + Arrays.hashCode(games);
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (!super.equals(obj)) {
			return false;
		}
		if (!(obj instanceof GameListResponse)) {
			return false;
		}
		GameListResponse other = (GameListResponse) obj;
		if (!Arrays.equals(games, other.games)) {
			return false;
		}
		return true;
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
