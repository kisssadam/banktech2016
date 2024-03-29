package hu.javachallenge.torpedo.response;

import hu.javachallenge.torpedo.model.Game;

public class GameInfoResponse extends CommonResponse {

	private Game game;

	public Game getGame() {
		return game;
	}

	public void setGame(Game game) {
		this.game = game;
	}

	@Override
	public int hashCode() {
		final int prime = 11321;
		int result = super.hashCode();
		result = prime * result + ((game == null) ? 0 : game.hashCode());
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
		if (!(obj instanceof GameInfoResponse)) {
			return false;
		}
		GameInfoResponse other = (GameInfoResponse) obj;
		if (game == null) {
			if (other.game != null) {
				return false;
			}
		} else if (!game.equals(other.game)) {
			return false;
		}
		return true;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("GameInfoResponse [game=");
		builder.append(game);
		builder.append(", getMessage()=");
		builder.append(getMessage());
		builder.append(", getCode()=");
		builder.append(getCode());
		builder.append("]");
		return builder.toString();
	}

}
