package hu.javachallenge.torpedo.model;

import java.util.HashMap;
import java.util.Map;

public class ConnectionStatus {

	private Map<String, Boolean> connected;

	public ConnectionStatus() {
		this.connected = new HashMap<>();
	}

	public ConnectionStatus(Map<String, Boolean> connected) {
		super();
		this.connected = connected;
	}

	public Map<String, Boolean> getConnected() {
		return connected;
	}

	public void setConnected(Map<String, Boolean> connected) {
		this.connected = connected;
	}

	@Override
	public int hashCode() {
		final int prime = 11321;
		int result = 1;
		result = prime * result + ((connected == null) ? 0 : connected.hashCode());
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
		if (!(obj instanceof ConnectionStatus)) {
			return false;
		}
		ConnectionStatus other = (ConnectionStatus) obj;
		if (connected == null) {
			if (other.connected != null) {
				return false;
			}
		} else if (!connected.equals(other.connected)) {
			return false;
		}
		return true;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("ConnectionStatus [connected=");
		builder.append(connected);
		builder.append("]");
		return builder.toString();
	}

}
