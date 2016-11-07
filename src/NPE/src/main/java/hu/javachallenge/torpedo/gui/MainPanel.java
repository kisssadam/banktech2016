package hu.javachallenge.torpedo.gui;

import java.awt.Color;
import java.awt.Graphics;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JPanel;

import hu.javachallenge.torpedo.model.Owner;
import hu.javachallenge.torpedo.model.Position;
import hu.javachallenge.torpedo.model.Submarine;
import hu.javachallenge.torpedo.response.GameInfoResponse;

public class MainPanel extends JPanel {

	private static final long serialVersionUID = 1L;
	private GameInfoResponse gameInfo;
	private String teamName;
	private List<SubmarineComponent> submarineComponents;

	public MainPanel(String teamName, GameInfoResponse gameInfo) {
		this.gameInfo = gameInfo;
		this.submarineComponents = new ArrayList<>();
		this.teamName = teamName;
	}

	private double getScale() {
		double mapWidth = gameInfo.getGame().getMapConfiguration().getWidth();
		double mapHeight = gameInfo.getGame().getMapConfiguration().getHeight();

		if (getWidth() / mapWidth < getHeight() / mapHeight) {
			return getWidth() / mapWidth;
		} else {
			return getHeight() / mapHeight;
		}
	}

	public void addSubmarine(Submarine submarine) {
		try {
			submarineComponents.add(new SubmarineComponent(submarine, teamName));
		} catch (Exception e) {
		}
	}

	public void updateSubmarine(Submarine submarine) {
		try {
			for (SubmarineComponent submarineComponent : submarineComponents) {
				if (submarineComponent.getSubmarine().getId() == submarine.getId()) {
					submarineComponent.setSubmarine(submarine);
					return;
				}
			}
		} catch (Exception e) {
		}
	}

	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		double scale = getScale();
		paintSubmarineComponent(g, scale);
		paintIslands(g, scale);
		paintCorners(g, scale);
	}

	private void paintCorners(Graphics g, double scale) {
		int size = 10;
		Color color = Color.BLACK;

		double mapWidth = gameInfo.getGame().getMapConfiguration().getWidth();
		double mapHeight = gameInfo.getGame().getMapConfiguration().getHeight();

		paintCircle(g, color, 0, 0, size, scale);
		paintCircle(g, color, mapWidth * scale, getHeight() - mapHeight * scale, size, scale);
		paintCircle(g, color, mapWidth * scale, mapHeight * scale, size, scale);
		paintCircle(g, color, 0, mapHeight * scale, size, scale);
	}

	private void paintIslands(Graphics g, double scale) {
		Color groundColor = new Color(255, 255, 153);
		Color terrainColor = new Color(0, 153, 0);
		
		for (Position islandPosition : gameInfo.getGame().getMapConfiguration().getIslandPositions()) {
			double x = islandPosition.getX().doubleValue() * scale;
			double y = getHeight() - islandPosition.getY().doubleValue() * scale;
			double islandSize = gameInfo.getGame().getMapConfiguration().getIslandSize() * scale;
			
			paintCircle(g, groundColor, x, y, islandSize, scale);
			paintCircle(g, terrainColor, x, y, islandSize * 0.8, scale);
		}
	}
	
	private void paintSubmarineComponent(Graphics g, double scale) {
		
		Color sonarColor = new Color(204, 229, 255);
		
		for (SubmarineComponent submarineComponent : submarineComponents) {			
			double x = submarineComponent.getSubmarine().getPosition().getX().doubleValue() * scale;
			double y = getHeight() - submarineComponent.getSubmarine().getPosition().getY().doubleValue() * scale;
			
			boolean hasExtSonar = submarineComponent.getSubmarine().getSonarExtended() > 0;
			double sonarRange = gameInfo.getGame().getMapConfiguration().getSonarRange();
			double extSonarRange = gameInfo.getGame().getMapConfiguration().getExtendedSonarRange();
			
			paintCircle(g, sonarColor, x, y, hasExtSonar ? extSonarRange : sonarRange, scale);
		}
		
		for (SubmarineComponent submarineComponent : submarineComponents) {
			double x = submarineComponent.getSubmarine().getPosition().getX().doubleValue() * scale;
			double y = getHeight() - submarineComponent.getSubmarine().getPosition().getY().doubleValue() * scale;
			double submarineSize = gameInfo.getGame().getMapConfiguration().getSubmarineSize() * scale;
			
			paintCircle(g, submarineComponent.getColor(), x, y, submarineSize, scale);
		}
	}

	private void paintCircle(Graphics g, Color color, double x, double y, double size, double scale) {
		g.setColor(color);
		g.fillOval((int) (x - size), (int) (y - size), (int) size * 2, (int) size * 2);
	}

}
