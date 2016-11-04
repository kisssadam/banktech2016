package hu.javachallenge.torpedo.model;

public class Submarine {

	private String type;
	private long id;
	private Position position;
	private Owner owner;
	private double velocity;
	private double angle;
	private int hp;
	private int sonarCooldown;
	private int torpedoCooldown;
	private int sonarExtended;

	public Submarine() {
		super();
		this.position = new Position();
		this.owner = new Owner();
	}

	public Submarine(String type, long id, Position position, Owner owner, double velocity, double angle, int hp,
			int sonarCooldown, int torpedoCooldown, int sonarExtended) {
		super();
		this.type = type;
		this.id = id;
		this.position = position;
		this.owner = owner;
		this.velocity = velocity;
		this.angle = angle;
		this.hp = hp;
		this.sonarCooldown = sonarCooldown;
		this.torpedoCooldown = torpedoCooldown;
		this.sonarExtended = sonarExtended;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public Position getPosition() {
		return position;
	}

	public void setPosition(Position position) {
		this.position = position;
	}

	public Owner getOwner() {
		return owner;
	}

	public void setOwner(Owner owner) {
		this.owner = owner;
	}

	public double getVelocity() {
		return velocity;
	}

	public void setVelocity(double velocity) {
		this.velocity = velocity;
	}

	public double getAngle() {
		return angle;
	}

	public void setAngle(double angle) {
		this.angle = angle;
	}

	public int getHp() {
		return hp;
	}

	public void setHp(int hp) {
		this.hp = hp;
	}

	public int getSonarCooldown() {
		return sonarCooldown;
	}

	public void setSonarCooldown(int sonarCooldown) {
		this.sonarCooldown = sonarCooldown;
	}

	public int getTorpedoCooldown() {
		return torpedoCooldown;
	}

	public void setTorpedoCooldown(int torpedoCooldown) {
		this.torpedoCooldown = torpedoCooldown;
	}

	public int getSonarExtended() {
		return sonarExtended;
	}

	public void setSonarExtended(int sonarExtended) {
		this.sonarExtended = sonarExtended;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		long temp;
		temp = Double.doubleToLongBits(angle);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		result = prime * result + hp;
		result = prime * result + (int) (id ^ (id >>> 32));
		result = prime * result + ((owner == null) ? 0 : owner.hashCode());
		result = prime * result + ((position == null) ? 0 : position.hashCode());
		result = prime * result + sonarCooldown;
		result = prime * result + sonarExtended;
		result = prime * result + torpedoCooldown;
		result = prime * result + ((type == null) ? 0 : type.hashCode());
		temp = Double.doubleToLongBits(velocity);
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
		if (!(obj instanceof Submarine)) {
			return false;
		}
		Submarine other = (Submarine) obj;
		if (Double.doubleToLongBits(angle) != Double.doubleToLongBits(other.angle)) {
			return false;
		}
		if (hp != other.hp) {
			return false;
		}
		if (id != other.id) {
			return false;
		}
		if (owner == null) {
			if (other.owner != null) {
				return false;
			}
		} else if (!owner.equals(other.owner)) {
			return false;
		}
		if (position == null) {
			if (other.position != null) {
				return false;
			}
		} else if (!position.equals(other.position)) {
			return false;
		}
		if (sonarCooldown != other.sonarCooldown) {
			return false;
		}
		if (sonarExtended != other.sonarExtended) {
			return false;
		}
		if (torpedoCooldown != other.torpedoCooldown) {
			return false;
		}
		if (type == null) {
			if (other.type != null) {
				return false;
			}
		} else if (!type.equals(other.type)) {
			return false;
		}
		if (Double.doubleToLongBits(velocity) != Double.doubleToLongBits(other.velocity)) {
			return false;
		}
		return true;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("Submarine [type=");
		builder.append(type);
		builder.append(", id=");
		builder.append(id);
		builder.append(", position=");
		builder.append(position);
		builder.append(", owner=");
		builder.append(owner);
		builder.append(", velocity=");
		builder.append(velocity);
		builder.append(", angle=");
		builder.append(angle);
		builder.append(", hp=");
		builder.append(hp);
		builder.append(", sonarCooldown=");
		builder.append(sonarCooldown);
		builder.append(", torpedoCooldown=");
		builder.append(torpedoCooldown);
		builder.append(", sonarExtended=");
		builder.append(sonarExtended);
		builder.append("]");
		return builder.toString();
	}

}
