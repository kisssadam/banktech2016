package hu.javachallenge.torpedo.response;

public class MoveResponse extends CommonResponse {

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("MoveResponse [getMessage()=");
		builder.append(getMessage());
		builder.append(", getCode()=");
		builder.append(getCode());
		builder.append("]");
		return builder.toString();
	}

}
