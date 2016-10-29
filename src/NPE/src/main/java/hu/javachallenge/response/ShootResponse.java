package hu.javachallenge.response;

public class ShootResponse extends CommonResponse {

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("ShootResponse [getMessage()=");
		builder.append(getMessage());
		builder.append(", getCode()=");
		builder.append(getCode());
		builder.append("]");
		return builder.toString();
	}

}
