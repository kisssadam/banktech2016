package hu.javachallenge.torpedo.gui;

import java.awt.Color;

import hu.javachallenge.torpedo.model.Submarine;

public class SubmarineComponent {

	private Submarine submarine;
	private Color color;

	public SubmarineComponent(Submarine submarine, String teamName) {
		this.submarine = submarine;
		this.color = teamName.equals(submarine.getOwner().getName()) ? Color.BLUE : Color.RED;
	}

	public Submarine getSubmarine() {
		return submarine;
	}

	public void setSubmarine(Submarine submarine) {
		this.submarine = submarine;
	}

	public Color getColor() {
		return color;
	}

	public void setColor(Color color) {
		this.color = color;
	}

}
