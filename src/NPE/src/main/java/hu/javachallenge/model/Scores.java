package hu.javachallenge.model;

import java.util.HashMap;
import java.util.Map;

public class Scores {

	Map<String, Long> scores;

	public Scores() {
		this.scores = new HashMap<>();
	}

	public Map<String, Long> getScores() {
		return scores;
	}

	public void setScores(Map<String, Long> scores) {
		this.scores = scores;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("Scores [scores=");
		builder.append(scores);
		builder.append("]");
		return builder.toString();
	}

}
