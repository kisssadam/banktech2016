package hu.javachallenge.model;

import java.time.LocalDateTime;

public class Game {

	private long id;
	private long round;
	private Scores scores;
	private ConnectionStatus connectionStatus;
	private MapConfiguration mapConfiguration;
	private Status status;
	private LocalDateTime createdTime;

	public Game() {
		this.connectionStatus = new ConnectionStatus();
		this.mapConfiguration = new MapConfiguration();
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public long getRound() {
		return round;
	}

	public void setRound(long round) {
		this.round = round;
	}

	public Scores getScores() {
		return scores;
	}

	public void setScores(Scores scores) {
		this.scores = scores;
	}

	public ConnectionStatus getConnectionStatus() {
		return connectionStatus;
	}

	public void setConnectionStatus(ConnectionStatus connectionStatus) {
		this.connectionStatus = connectionStatus;
	}

	public MapConfiguration getMapConfiguration() {
		return mapConfiguration;
	}

	public void setMapConfiguration(MapConfiguration mapConfiguration) {
		this.mapConfiguration = mapConfiguration;
	}

	public Status getStatus() {
		return status;
	}

	public void setStatus(Status status) {
		this.status = status;
	}

	public LocalDateTime getCreatedTime() {
		return createdTime;
	}

	public void setCreatedTime(LocalDateTime createdTime) {
		this.createdTime = createdTime;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("Game [id=");
		builder.append(id);
		builder.append(", round=");
		builder.append(round);
		builder.append(", scores=");
		builder.append(scores);
		builder.append(", connectionStatus=");
		builder.append(connectionStatus);
		builder.append(", mapConfiguration=");
		builder.append(mapConfiguration);
		builder.append(", status=");
		builder.append(status);
		builder.append(", createdTime=");
		builder.append(createdTime);
		builder.append("]");
		return builder.toString();
	}

}
