package hu.javachallenge.torpedo.response;

public class CreateGameResponse extends CommonResponse {

	private long id;

	public CreateGameResponse() {
		super();
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("CreateGameResponse [id=");
		builder.append(id);
		builder.append(", getMessage()=");
		builder.append(getMessage());
		builder.append(", getCode()=");
		builder.append(getCode());
		builder.append("]");
		return builder.toString();
	}

}
