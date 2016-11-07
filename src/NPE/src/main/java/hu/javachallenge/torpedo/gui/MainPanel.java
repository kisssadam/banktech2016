package hu.javachallenge.torpedo.gui;

import java.awt.Color;
import java.awt.Graphics;
import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;

import javax.swing.JPanel;

import hu.javachallenge.torpedo.model.Entity;
import hu.javachallenge.torpedo.model.Position;
import hu.javachallenge.torpedo.model.Submarine;
import hu.javachallenge.torpedo.response.GameInfoResponse;

public class MainPanel extends JPanel {

	private static final long serialVersionUID = 1L;
	private static final Color GROUND_COLOR = new Color(255, 255, 153);
	private static final Color TERRAIN_COLOR = new Color(0, 153, 0);
	private static final Color SONAR_COLOR = new Color(204, 229, 255);

	private GameInfoResponse gameInfo;
	private List<Submarine> submarines;
	private List<Submarine> enemySubmarines;
	private List<Position> torpedos;

	public MainPanel(GameInfoResponse gameInfo) {
		this.gameInfo = gameInfo;
		this.submarines = new ArrayList<>();
		this.enemySubmarines = new ArrayList<>();
		this.torpedos = new ArrayList<>();
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

	public void addSubmarines(List<Submarine> submarines) {
		this.submarines.clear();
		for (Submarine submarine : submarines) {
			addSubmarine(submarine);
		}
	}

	private void addSubmarine(Submarine submarine) {
		try {
			submarines.add(submarine);
		} catch (Exception e) {
		}
	}

	public void addEnemySubmarines(List<Submarine> enemySubmarines) {
		this.enemySubmarines.clear();
		for (Submarine submarine : enemySubmarines) {
			addEnemySubmarine(submarine);
		}
	}

	private void addEnemySubmarine(Submarine submarine) {
		try {
			enemySubmarines.add(submarine);
		} catch (Exception e) {
		}
	}

	public void addTorpedos(List<Entity> torpedos) {
		this.torpedos.clear();
		for (Entity entity : torpedos) {
			addTorpedo(entity.getPosition());
		}
	}

	private void addTorpedo(Position position) {
		try {
			torpedos.add(position);
		} catch (Exception e) {
		}
	}

	public void updateSubmarine(Submarine submarine) {
		try {
			for (ListIterator<Submarine> iterator = submarines.listIterator(); iterator.hasNext();) {
				Submarine s = iterator.next();
				if (s.getId() == submarine.getId()) {
					iterator.set(submarine);
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
		paintTorpedos(g, scale);
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
		for (Position islandPosition : gameInfo.getGame().getMapConfiguration().getIslandPositions()) {
			double x = islandPosition.getX().doubleValue() * scale;
			double y = getHeight() - islandPosition.getY().doubleValue() * scale;
			double islandSize = gameInfo.getGame().getMapConfiguration().getIslandSize() * scale;

			paintCircle(g, GROUND_COLOR, x, y, islandSize, scale);
			paintCircle(g, TERRAIN_COLOR, x, y, islandSize * 0.8, scale);
		}
	}

	private void paintSubmarineComponent(Graphics g, double scale) {
		for (Submarine submarine : submarines) {
			double x = submarine.getPosition().getX().doubleValue() * scale;
			double y = getHeight() - submarine.getPosition().getY().doubleValue() * scale;

			boolean hasExtSonar = submarine.getSonarExtended() > 0;
			double sonarRange = gameInfo.getGame().getMapConfiguration().getSonarRange();
			double extSonarRange = gameInfo.getGame().getMapConfiguration().getExtendedSonarRange();

			paintCircle(g, SONAR_COLOR, x, y, hasExtSonar ? extSonarRange : sonarRange, scale);
		}

		for (Submarine submarine : submarines) {
			double x = submarine.getPosition().getX().doubleValue() * scale;
			double y = getHeight() - submarine.getPosition().getY().doubleValue() * scale;
			double submarineSize = gameInfo.getGame().getMapConfiguration().getSubmarineSize() * scale;

			paintCircle(g, Color.BLUE, x, y, submarineSize, scale);
		}

		for (Submarine submarine : enemySubmarines) {
			double x = submarine.getPosition().getX().doubleValue() * scale;
			double y = getHeight() - submarine.getPosition().getY().doubleValue() * scale;
			double submarineSize = gameInfo.getGame().getMapConfiguration().getSubmarineSize() * scale;

			paintCircle(g, Color.RED, x, y, submarineSize, scale);
		}
	}

	private void paintTorpedos(Graphics g, double scale) {
		for (Position position : torpedos) {
			double x = position.getX().doubleValue() * scale;
			double y = getHeight() - position.getY().doubleValue() * scale;

			paintCircle(g, Color.BLACK, x, y, 5, scale);
		}
	}

	private void paintCircle(Graphics g, Color color, double x, double y, double size, double scale) {
		g.setColor(color);
		g.fillOval((int) (x - size), (int) (y - size), (int) size * 2, (int) size * 2);
	}

}
