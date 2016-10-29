package hu.javachallenge.model;

import org.apache.commons.lang3.StringUtils;

public class Owner {

	private String name;

	public Owner() {
		this.name = StringUtils.EMPTY;
	}

	public Owner(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("Owner [name=");
		builder.append(name);
		builder.append("]");
		return builder.toString();
	}

}
