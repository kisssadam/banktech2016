package hu.javachallenge.torpedo.response;

import java.util.Arrays;

import hu.javachallenge.torpedo.model.Entity;

public class SonarResponse extends CommonResponse {

	private Entity[] entities;

	public SonarResponse() {
		super();
	}

	public SonarResponse(Entity[] entities) {
		super();
		this.entities = entities;
	}

	public Entity[] getEntities() {
		return entities;
	}

	public void setEntities(Entity[] entities) {
		this.entities = entities;
	}

	@Override
	public int hashCode() {
		final int prime = 11321;
		int result = super.hashCode();
		result = prime * result + Arrays.hashCode(entities);
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
		if (!(obj instanceof SonarResponse)) {
			return false;
		}
		SonarResponse other = (SonarResponse) obj;
		if (!Arrays.equals(entities, other.entities)) {
			return false;
		}
		return true;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("SonarResponse [entities=");
		builder.append(Arrays.toString(entities));
		builder.append(", getMessage()=");
		builder.append(getMessage());
		builder.append(", getCode()=");
		builder.append(getCode());
		builder.append("]");
		return builder.toString();
	}

}
