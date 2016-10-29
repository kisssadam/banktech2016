package hu.javachallenge.response;

import java.util.Arrays;

import hu.javachallenge.model.Submarine;

public class SubmarinesResponse extends CommonResponse {

	private Submarine[] submarines;

	public SubmarinesResponse() {
		this(new Submarine[0]);
	}

	public SubmarinesResponse(Submarine[] submarines) {
		super();
		this.submarines = submarines;
	}

	public Submarine[] getSubmarines() {
		return submarines;
	}

	public void setSubmarines(Submarine[] submarines) {
		this.submarines = submarines;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("SubmarinesResponse [submarines=");
		builder.append(Arrays.toString(submarines));
		builder.append(", getMessage()=");
		builder.append(getMessage());
		builder.append(", getCode()=");
		builder.append(getCode());
		builder.append("]");
		return builder.toString();
	}

}
