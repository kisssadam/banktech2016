package hu.javachallenge.model;

public class IslandPosition {

	private double x;

	private double y;

	public IslandPosition() {
		super();
	}

	public IslandPosition(double x, double y) {
		this.x = x;
		this.y = y;
	}

	public double getX() {
		return x;
	}

	public void setX(double x) {
		this.x = x;
	}

	public double getY() {
		return y;
	}

	public void setY(double y) {
		this.y = y;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("IslandPosition [x=");
		builder.append(x);
		builder.append(", y=");
		builder.append(y);
		builder.append("]");
		return builder.toString();
	}

}
