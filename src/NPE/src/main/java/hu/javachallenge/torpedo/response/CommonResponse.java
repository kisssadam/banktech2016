package hu.javachallenge.torpedo.response;

public class CommonResponse {

	private String message;
	private int code;

	public CommonResponse() {
		super();
	}

	public CommonResponse(String message, int code) {
		super();
		this.message = message;
		this.code = code;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public int getCode() {
		return code;
	}

	public void setCode(int code) {
		this.code = code;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("CommonResponse [message=");
		builder.append(message);
		builder.append(", code=");
		builder.append(code);
		builder.append("]");
		return builder.toString();
	}

}
