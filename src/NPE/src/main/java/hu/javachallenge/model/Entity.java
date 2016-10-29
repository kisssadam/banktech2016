package hu.javachallenge.model;

public class Entity {

	private String type;
	private long id;
	private BigDecimalPosition position;
	private Owner owner;
	private int velocity;
	private double angle;
	private Integer roundsMoved; // lehet null, ezért Integer, nem pedig int!

	public Entity() {
		super();
	}

	public Entity(String type, long id, BigDecimalPosition position, Owner owner, int velocity, double angle,
			Integer roundsMoved) {
		super();
		this.type = type;
		this.id = id;
		this.position = position;
		this.owner = owner;
		this.velocity = velocity;
		this.angle = angle;
		this.roundsMoved = roundsMoved;
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

	public BigDecimalPosition getPosition() {
		return position;
	}

	public void setPosition(BigDecimalPosition position) {
		this.position = position;
	}

	public Owner getOwner() {
		return owner;
	}

	public void setOwner(Owner owner) {
		this.owner = owner;
	}

	public int getVelocity() {
		return velocity;
	}

	public void setVelocity(int velocity) {
		this.velocity = velocity;
	}

	public double getAngle() {
		return angle;
	}

	public void setAngle(double angle) {
		this.angle = angle;
	}

	public Integer getRoundsMoved() {
		return roundsMoved;
	}

	public void setRoundsMoved(Integer roundsMoved) {
		this.roundsMoved = roundsMoved;
	}

	@Override
	public int hashCode() {
		final int prime = 11321;
		int result = 1;
		long temp;
		temp = Double.doubleToLongBits(angle);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		result = prime * result + (int) (id ^ (id >>> 32));
		result = prime * result + ((owner == null) ? 0 : owner.hashCode());
		result = prime * result + ((position == null) ? 0 : position.hashCode());
		result = prime * result + ((roundsMoved == null) ? 0 : roundsMoved.hashCode());
		result = prime * result + ((type == null) ? 0 : type.hashCode());
		result = prime * result + velocity;
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
		if (!(obj instanceof Entity)) {
			return false;
		}
		Entity other = (Entity) obj;
		if (Double.doubleToLongBits(angle) != Double.doubleToLongBits(other.angle)) {
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
		if (roundsMoved == null) {
			if (other.roundsMoved != null) {
				return false;
			}
		} else if (!roundsMoved.equals(other.roundsMoved)) {
			return false;
		}
		if (type == null) {
			if (other.type != null) {
				return false;
			}
		} else if (!type.equals(other.type)) {
			return false;
		}
		if (velocity != other.velocity) {
			return false;
		}
		return true;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("Entity [type=");
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
		builder.append(", roundsMoved=");
		builder.append(roundsMoved);
		builder.append("]");
		return builder.toString();
	}

}
