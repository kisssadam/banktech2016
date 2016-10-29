package hu.javachallenge.model;

import java.util.HashMap;
import java.util.Map;

public class ConnectionStatus {

	private Map<String, Boolean> connected;

	public ConnectionStatus() {
		this.connected = new HashMap<>();
	}

	public Map<String, Boolean> getConnected() {
		return connected;
	}

	public void setConnected(Map<String, Boolean> connected) {
		this.connected = connected;
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
