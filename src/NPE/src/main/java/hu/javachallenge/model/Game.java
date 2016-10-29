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
	public int hashCode() {
		final int prime = 11321;
		int result = 1;
		result = prime * result + ((connectionStatus == null) ? 0 : connectionStatus.hashCode());
		result = prime * result + ((createdTime == null) ? 0 : createdTime.hashCode());
		result = prime * result + (int) (id ^ (id >>> 32));
		result = prime * result + ((mapConfiguration == null) ? 0 : mapConfiguration.hashCode());
		result = prime * result + (int) (round ^ (round >>> 32));
		result = prime * result + ((scores == null) ? 0 : scores.hashCode());
		result = prime * result + ((status == null) ? 0 : status.hashCode());
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
		if (connectionStatus == null) {
			if (other.connectionStatus != null) {
				return false;
			}
		} else if (!connectionStatus.equals(other.connectionStatus)) {
			return false;
		}
		if (createdTime == null) {
			if (other.createdTime != null) {
				return false;
			}
		} else if (!createdTime.equals(other.createdTime)) {
			return false;
		}
		if (id != other.id) {
			return false;
		}
		if (mapConfiguration == null) {
			if (other.mapConfiguration != null) {
				return false;
			}
		} else if (!mapConfiguration.equals(other.mapConfiguration)) {
			return false;
		}
		if (round != other.round) {
			return false;
		}
		if (scores == null) {
			if (other.scores != null) {
				return false;
			}
		} else if (!scores.equals(other.scores)) {
			return false;
		}
		if (status != other.status) {
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
