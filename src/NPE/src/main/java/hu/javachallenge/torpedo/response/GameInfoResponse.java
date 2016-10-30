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
