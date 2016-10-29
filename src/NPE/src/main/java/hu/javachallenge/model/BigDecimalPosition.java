package hu.javachallenge.model;

import java.math.BigDecimal;

public class BigDecimalPosition {

	private BigDecimal x;
	private BigDecimal y;

	public BigDecimalPosition() {
		super();
	}

	public BigDecimalPosition(BigDecimal x, BigDecimal y) {
		super();
		this.x = x;
		this.y = y;
	}

	public BigDecimal getX() {
		return x;
	}

	public void setX(BigDecimal x) {
		this.x = x;
	}

	public BigDecimal getY() {
		return y;
	}

	public void setY(BigDecimal y) {
		this.y = y;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("BigDecimalPosition [x=");
		builder.append(x);
		builder.append(", y=");
		builder.append(y);
		builder.append("]");
		return builder.toString();
	}

}
