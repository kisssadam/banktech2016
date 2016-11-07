package hu.javachallenge.torpedo.gui;

import java.awt.Graphics;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JPanel;

import hu.javachallenge.torpedo.model.Owner;
import hu.javachallenge.torpedo.model.Position;
import hu.javachallenge.torpedo.model.Submarine;

public class MainPanel extends JPanel {

	private static final long serialVersionUID = 1L;
	private double submarineSize;
        private double scale;
	private String teamName;
	private List<SubmarineComponent> submarineComponents;

	public MainPanel(double submarineSize, String teamName) {
		this.submarineComponents = new ArrayList<>();
		this.submarineSize = submarineSize;
		this.teamName = teamName;
	}
        
        public void scalingInit(double heigth, double width) {
            if(1275 / width < 600 / heigth) {
                    this.scale = 1275 / width;
                } else {
                    this.scale = 600 / heigth;
                }
            this.submarineSize = scale * submarineSize;
        }

	public void addSubmarine(Submarine submarine) {
		try {
                    SubmarineComponent submarineComponent = new SubmarineComponent(submarine, teamName);
                    submarineComponent.setPosition(new Position(submarineComponent.getPosition().getX().doubleValue() * scale, 600 - submarineComponent.getPosition().getY().doubleValue() * scale));
			submarineComponents.add(submarineComponent);
		} catch (Exception e) {
		}
	}

	public void updateSubmarine(String type, long submarineId, Position position, Owner owner, double velocity, double angle, int hp, int sonarCooldown, int torpedoCooldown, int sonarExtended) {
		try {
			for (SubmarineComponent submarineComponent : submarineComponents) {
				if (submarineComponent.getId() == submarineId) {
					submarineComponent.setAngle(angle);
					submarineComponent.setHp(hp);
					submarineComponent.setOwner(owner);
					submarineComponent.setPosition(new Position(position.getX().doubleValue() * scale, 600 - position.getY().doubleValue() * scale));
					submarineComponent.setSonarCooldown(sonarCooldown);
					submarineComponent.setSonarExtended(sonarExtended);
					submarineComponent.setTorpedoCooldown(torpedoCooldown);
					submarineComponent.setType(type);
					submarineComponent.setVelocity(velocity);
					return;
				}
			}
		} catch (Exception e) {
		}
	}

	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);

		for (SubmarineComponent submarineComponent : submarineComponents) {
			g.setColor(submarineComponent.getColor());

			double x = submarineComponent.getPosition().getX().doubleValue();
			double y = submarineComponent.getPosition().getY().doubleValue();
                        g.fillOval((int) (x - submarineSize), (int) (y - submarineSize), (int) submarineSize * 2, (int) submarineSize * 2);
//			g.fillOval((int) x / 2, (int) (getHeight() - (y / 2)), (int) submarineSize / 2, (int) submarineSize / 2);
		}
	}

}
