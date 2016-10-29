package hu.javachallenge.request;

public class ShootRequest extends CommonRequest {

	private double angle;

	public ShootRequest() {
		super();
	}

	public ShootRequest(double angle) {
		super();
		this.angle = angle;
	}

	public double getAngle() {
		return angle;
	}

	public void setAngle(double angle) {
		this.angle = angle;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("ShootRequest [angle=");
		builder.append(angle);
		builder.append("]");
		return builder.toString();
	}

}
