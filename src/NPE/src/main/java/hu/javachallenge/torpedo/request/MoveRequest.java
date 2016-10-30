package hu.javachallenge.torpedo.request;

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
	public int hashCode() {
		final int prime = 11321;
		int result = 1;
		long temp;
		temp = Double.doubleToLongBits(speed);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		temp = Double.doubleToLongBits(turn);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (!(obj instanceof MoveRequest)) {
			return false;
		}
		MoveRequest other = (MoveRequest) obj;
		if (Double.doubleToLongBits(speed) != Double.doubleToLongBits(other.speed)) {
			return false;
		}
		if (Double.doubleToLongBits(turn) != Double.doubleToLongBits(other.turn)) {
			return false;
		}
		return true;
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
