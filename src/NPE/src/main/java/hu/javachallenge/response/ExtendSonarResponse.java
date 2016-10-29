package hu.javachallenge.response;

public class ExtendSonarResponse extends CommonResponse {

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("ExtendSonarResponse [getMessage()=");
		builder.append(getMessage());
		builder.append(", getCode()=");
		builder.append(getCode());
		builder.append("]");
		return builder.toString();
	}

}
