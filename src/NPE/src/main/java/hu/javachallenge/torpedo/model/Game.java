package hu.javachallenge.torpedo.model;

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

	public Game(long id, long round, Scores scores, ConnectionStatus connectionStatus,
			MapConfiguration mapConfiguration, Status status, LocalDateTime createdTime) {
		super();
		this.id = id;
		this.round = round;
		this.scores = scores;
		this.connectionStatus = connectionStatus;
		this.mapConfiguration = mapConfiguration;
		this.status = status;
		this.createdTime = createdTime;
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
	public int hashCode() {
		final int prime = 2593;
		int result = 1;
		result = prime * result + (int) (id ^ (id >>> 32));
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (!(obj instanceof Game)) {
			return false;
		}
		Game other = (Game) obj;
		if (id != other.id) {
			return false;
		}
		return true;
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
