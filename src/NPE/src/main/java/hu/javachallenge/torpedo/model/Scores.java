package hu.javachallenge.torpedo.model;

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
	public int hashCode() {
		final int prime = 11321;
		int result = 1;
		result = prime * result + ((scores == null) ? 0 : scores.hashCode());
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
		if (!(obj instanceof Scores)) {
			return false;
		}
		Scores other = (Scores) obj;
		if (scores == null) {
			if (other.scores != null) {
				return false;
			}
		} else if (!scores.equals(other.scores)) {
			return false;
		}
		return true;
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
