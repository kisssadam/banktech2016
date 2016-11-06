package hu.javachallenge.torpedo.gui;

import java.awt.Color;

import hu.javachallenge.torpedo.model.Owner;
import hu.javachallenge.torpedo.model.Position;
import hu.javachallenge.torpedo.model.Submarine;

public class SubmarineComponent {

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
	private Color color;

	public SubmarineComponent(Submarine submarine, String teamName) {
		this.type = submarine.getType();
		this.id = submarine.getId();
		this.position = submarine.getPosition();
		this.owner = submarine.getOwner();
		this.velocity = submarine.getVelocity();
		this.angle = submarine.getAngle();
		this.hp = submarine.getHp();
		this.sonarCooldown = submarine.getSonarCooldown();
		this.torpedoCooldown = submarine.getTorpedoCooldown();
		this.sonarExtended = submarine.getSonarExtended();
		this.color = teamName.equals(submarine.getOwner().getName()) ? Color.BLUE : Color.RED;
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

	public Color getColor() {
		return color;
	}

	public void setColor(Color color) {
		this.color = color;
	}

}
