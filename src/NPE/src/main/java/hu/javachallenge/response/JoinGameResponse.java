package hu.javachallenge.response;

public class JoinGameResponse extends CommonResponse {

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("JoinGameResponse [getMessage()=");
		builder.append(getMessage());
		builder.append(", getCode()=");
		builder.append(getCode());
		builder.append("]");
		return builder.toString();
	}

}
