package hu.javachallenge.torpedo.request;

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
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		long temp;
		temp = Double.doubleToLongBits(angle);
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
		if (!(obj instanceof ShootRequest)) {
			return false;
		}
		ShootRequest other = (ShootRequest) obj;
		if (Double.doubleToLongBits(angle) != Double.doubleToLongBits(other.angle)) {
			return false;
		}
		return true;
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
