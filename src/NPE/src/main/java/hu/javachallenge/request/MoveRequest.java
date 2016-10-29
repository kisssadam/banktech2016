package hu.javachallenge.request;

public class MoveRequest extends CommonRequest {

	private double speed;
	private double turn;

	public MoveRequest() {
		super();
	}

	public MoveRequest(double speed, double turn) {
		super();
		this.speed = speed;
		this.turn = turn;
	}

	public double getSpeed() {
		return speed;
	}

	public void setSpeed(double speed) {
		this.speed = speed;
	}

	public double getTurn() {
		return turn;
	}

	public void setTurn(double turn) {
		this.turn = turn;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("MoveRequest [speed=");
		builder.append(speed);
		builder.append(", turn=");
		builder.append(turn);
		builder.append("]");
		return builder.toString();
	}

}
