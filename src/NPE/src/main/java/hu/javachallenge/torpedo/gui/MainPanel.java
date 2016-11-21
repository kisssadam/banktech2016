package hu.javachallenge.torpedo.gui;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JPanel;

import hu.javachallenge.torpedo.model.Entity;
import hu.javachallenge.torpedo.model.Position;
import hu.javachallenge.torpedo.model.Submarine;
import hu.javachallenge.torpedo.response.GameInfoResponse;
import hu.javachallenge.torpedo.util.MathUtil;

public class MainPanel extends JPanel {

	private static final long serialVersionUID = 1L;

	private static final Color GROUND_COLOR = new Color(255, 255, 153);
	private static final Color TERRAIN_COLOR = new Color(0, 153, 0);
	private static final Color SONAR_COLOR = new Color(205, 210, 255);
	private static final Color EXTENDED_SONAR_COLOR = new Color(204, 229, 255);
	private static final Color CORNER_COLOR = Color.BLACK;
	private static final Color SUBMARINE_COLOR = Color.BLUE;
	private static final Color ENEMY_SUBMARINE_COLOR = Color.RED;
	private static final Color TORPEDO_COLOR = Color.BLACK;

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

	public void updateSubmarines(Submarine[] submarines) {
		this.submarines.clear();
		if (submarines == null) {
			return;
		}
		for (Submarine submarine : submarines) {
			this.submarines.add(submarine);
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

		double mapWidth = gameInfo.getGame().getMapConfiguration().getWidth();
		double mapHeight = gameInfo.getGame().getMapConfiguration().getHeight();

		paintCircle(g, CORNER_COLOR, 0, 0, size);
		paintCircle(g, CORNER_COLOR, mapWidth * scale, getHeight() - mapHeight * scale, size);
		paintCircle(g, CORNER_COLOR, mapWidth * scale, mapHeight * scale, size);
		paintCircle(g, CORNER_COLOR, 0, mapHeight * scale, size);
	}

	private void paintIslands(Graphics g, double scale) {
		for (Position islandPosition : gameInfo.getGame().getMapConfiguration().getIslandPositions()) {
			double x = islandPosition.getX().doubleValue() * scale;
			double y = getHeight() - islandPosition.getY().doubleValue() * scale;
			double islandSize = gameInfo.getGame().getMapConfiguration().getIslandSize() * scale;

			paintCircle(g, GROUND_COLOR, x, y, islandSize);
			paintCircle(g, TERRAIN_COLOR, x, y, islandSize * 0.8);
		}
	}

	private void paintSubmarineComponent(Graphics g, double scale) {
		double sonarRange = gameInfo.getGame().getMapConfiguration().getSonarRange();
		double extendedSonarRange = gameInfo.getGame().getMapConfiguration().getExtendedSonarRange();
		double submarineSize = gameInfo.getGame().getMapConfiguration().getSubmarineSize() * scale;
		
		for (Submarine submarine : submarines) {
			double x = submarine.getPosition().getX().doubleValue() * scale;
			double y = getHeight() - submarine.getPosition().getY().doubleValue() * scale;

			if (submarine.getSonarExtended() > 0) {
				paintCircle(g, EXTENDED_SONAR_COLOR, x, y, extendedSonarRange);
			}
		}
		
		for (Submarine submarine : submarines) {
			double x = submarine.getPosition().getX().doubleValue() * scale;
			double y = getHeight() - submarine.getPosition().getY().doubleValue() * scale;
			
			paintCircle(g, SONAR_COLOR, x, y, sonarRange);
		}

		for (Submarine submarine : submarines) {
			double x = submarine.getPosition().getX().doubleValue() * scale;
			double y = getHeight() - submarine.getPosition().getY().doubleValue() * scale;

			paintCircle(g, SUBMARINE_COLOR, x, y, submarineSize);
			
			int dirX = (int) (x + MathUtil.xMovement(submarine.getVelocity(), submarine.getAngle()) * scale);
			int dirY = (int) (y - MathUtil.yMovement(submarine.getVelocity(), submarine.getAngle()) * scale);

			g.setColor(Color.RED);
			Graphics2D g2 = (Graphics2D) g;
			g2.setStroke(new BasicStroke(3.0f));
			g2.drawLine((int) x, (int) y, dirX, dirY);
		}
		
		for (Submarine submarine : submarines) {
			double x = submarine.getPosition().getX().doubleValue() * scale;
			double y = getHeight() - submarine.getPosition().getY().doubleValue() * scale;
			String title = String.valueOf(submarine.getHp());
			
			g.setColor(Color.GREEN);
			g.drawString(title, (int) x, (int) y);
		}

		for (Submarine submarine : enemySubmarines) {
			double x = submarine.getPosition().getX().doubleValue() * scale;
			double y = getHeight() - submarine.getPosition().getY().doubleValue() * scale;

			paintCircle(g, ENEMY_SUBMARINE_COLOR, x, y, submarineSize);
			g.drawString(String.valueOf(submarine.getId()), (int) (x - submarineSize * 1.5), (int) (y - submarineSize));
		}
	}

	private void paintTorpedos(Graphics g, double scale) {
		for (Position position : torpedos) {
			double x = position.getX().doubleValue() * scale;
			double y = getHeight() - position.getY().doubleValue() * scale;

			paintCircle(g, TORPEDO_COLOR, x, y, 5);
		}
	}
  
	private void paintCircle(Graphics g, Color color, double x, double y, double size) {
		g.setColor(color);
		g.fillOval((int) (x - size), (int) (y - size), (int) size * 2, (int) size * 2);
	}

}
