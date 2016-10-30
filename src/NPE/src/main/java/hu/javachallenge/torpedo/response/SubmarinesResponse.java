package hu.javachallenge.torpedo.response;

import java.util.Arrays;

import hu.javachallenge.torpedo.model.Submarine;

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
	public int hashCode() {
		final int prime = 11321;
		int result = super.hashCode();
		result = prime * result + Arrays.hashCode(submarines);
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
		if (!(obj instanceof SubmarinesResponse)) {
			return false;
		}
		SubmarinesResponse other = (SubmarinesResponse) obj;
		if (!Arrays.equals(submarines, other.submarines)) {
			return false;
		}
		return true;
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
