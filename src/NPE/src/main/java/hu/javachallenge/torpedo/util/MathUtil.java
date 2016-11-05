package hu.javachallenge.torpedo.util;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import hu.javachallenge.torpedo.model.Position;
import hu.javachallenge.torpedo.model.Submarine;
import hu.javachallenge.torpedo.response.GameInfoResponse;

public class MathUtil {

	private static final Logger log = LoggerFactory.getLogger(MathUtil.class);
	
	public static double normalizeAngle(double angle) {
		while (angle >= 360) {
			angle -= 360;
		}
		
		while (angle < 0) {
			angle += 360;
		}
		
		return angle;
	}
	
	public static double normalizeVelocity(double velocity, double maxSpeed) {
		return velocity < 0 ? 0 : (velocity > maxSpeed ? maxSpeed : velocity);
	}
	
	public static double distance(Position sourcePosition, double sourceR, Position destinationPosition,
			double destinationR) {
		BigDecimal xSubtract = sourcePosition.getX().subtract(destinationPosition.getX());
		BigDecimal xSquare = xSubtract.multiply(xSubtract);

		BigDecimal ySubtract = sourcePosition.getY().subtract(destinationPosition.getY());
		BigDecimal ySquare = ySubtract.multiply(ySubtract);

		return Math.sqrt(xSquare.add(ySquare).doubleValue()) - (sourceR + destinationR);
	}

	public static double torpedoDistance(Position sourcePosition, Position destinationPosition, double destinationR) {
		return distance(sourcePosition, 0.0, destinationPosition, destinationR);
	}

	/**
	 * Milyen irányban kell kilőni a torpedót, hogy eltalálja a célt.
	 */
	public static double torpedoDestinationAngle(Position sourcePosition, Position destinationPosition) {
		double srcX = sourcePosition.getX().doubleValue();
		double srcY = sourcePosition.getY().doubleValue();

		double destX = destinationPosition.getX().doubleValue();
		double destY = destinationPosition.getY().doubleValue();

		double distX = destinationPosition.getX().subtract(sourcePosition.getX()).abs().doubleValue();
		double distY = destinationPosition.getY().subtract(sourcePosition.getY()).abs().doubleValue();

		double angle = Math.toDegrees(Math.atan(distY / distX));

		if (destX > srcX && destY == srcY) {
			return 0.0;
		}
		if (destX == srcX && destY > srcY) {
			return 90.0;
		}
		if (destX < srcX && destY == srcY) {
			return 180.0;
		}
		if (destX == srcX && destY < srcY) {
			return 270.0;
		}

		if (destX > srcX && destY > srcY) {
			return angle;
		}
		if (destX < srcX && destY > srcY) {
			return 180.0 - angle;
		}
		if (destX < srcX && destY < srcY) {
			return 180.0 + angle;
		}
		if (destX > srcX && destY < srcY) {
			return 360.0 - angle;
		}

		log.warn("None of the if statements were executed in torpedoDestinationAngle. Returning angle: {}", angle);
		return angle;
	}

	/**
	 *
	 * @param velocity
	 *            átfogó
	 * @param angle
	 *            szög
	 * @return szomszédos befogó
	 */
	public static double xMovement(double velocity, double angle) {
		return velocity * Math.cos(Math.toRadians(angle));
	}

	public static double yMovement(double velocity, double angle) {
		return velocity * Math.sin(Math.toRadians(angle));
	}

	// https://www.kirupa.com/forum/showthread.php?347334-Aiming-and-hitting-a-moving-target
	public static double aimAtMovingTarget(Position sourcePosition, Position targetPosition, double targetMovementAngle,
			double targetVelocity, double bulletVelocity) {
		double dX = targetPosition.getX().subtract(sourcePosition.getX()).doubleValue();
		double dY = targetPosition.getY().subtract(sourcePosition.getY()).doubleValue();

		double xMovementResult = xMovement(targetVelocity, targetMovementAngle);
		double yMovementResult = yMovement(targetVelocity, targetMovementAngle);

		double a = Math.pow(xMovementResult, 2) + Math.pow(yMovementResult, 2) - Math.pow(bulletVelocity, 2);
		double b = 2 * (xMovementResult * dX + yMovementResult * dY);
		double c = Math.pow(dX, 2) + Math.pow(dY, 2);

		// Check we're not breaking into complex numbers
		double q = Math.pow(b, 2) - 4 * a * c;
		if (q < 0.0) {
			log.error("Math.pow({}) cannot be interpreted on negative numbers.", q);
			return 0.0;
		}

		// The time that we will hit the target
		double time = ((a < 0.0 ? -1.0 : 1.0) * Math.sqrt(q) - b) / (2.0 * a);

		// Aim for where the target will be after time t
		dX += time * xMovementResult;
		dY += time * yMovementResult;

		double theta = Math.toDegrees(Math.atan2(dY, dX));
		return theta;
	}

	// http://stackoverflow.com/questions/1571294/line-equation-with-angle
	public static double meredekség(double angle) {
		return Math.tan(Math.toRadians(angle));
	}

	// amit célzunk és köztünk van-e sziget
	public static List<Submarine> getPossibleTargets(GameInfoResponse gameInfoResponse, Position[] islandPositions,
			Position sourcePosition, Submarine[] submarines, double bulletVelocity) {
		List<Submarine> possibleSubmarineTargets = new ArrayList<>();

		for (Submarine submarine : submarines) {
			double theta = aimAtMovingTarget(sourcePosition, submarine.getPosition(), submarine.getAngle(), submarine.getVelocity(), bulletVelocity);
			for (Position islandPosition : islandsInDirection(gameInfoResponse, sourcePosition, theta)) {
				double submarineSize = gameInfoResponse.getGame().getMapConfiguration().getSubmarineSize();
				double islandSize = gameInfoResponse.getGame().getMapConfiguration().getIslandSize();

				double islandDistance = distance(sourcePosition, submarineSize, islandPosition, islandSize);

				double submarineXMovement = xMovement(submarine.getVelocity(), submarine.getAngle());
				double submarineYMovement = yMovement(submarine.getVelocity(), submarine.getAngle());
				Position newSubmarinePosition = new Position(submarineXMovement, submarineYMovement);

				double submarineDistance = distance(sourcePosition, submarineSize, newSubmarinePosition, submarineSize);

				if (islandDistance > submarineDistance) {
					possibleSubmarineTargets.add(submarine);
				}
			}
		}

		return possibleSubmarineTargets;
	}
	
	public static Position getNearestIslandInDirection(List<Position> islandsInDirection) {
		return islandsInDirection.isEmpty() ? null : islandsInDirection.get(0);
	}
	
	private static class Island {
		private Position position;
		private double distance;
		
		public Island(Position position, double distance) {
			this.position = position;
			this.distance = distance;
		}
	}
	
	public static List<Position> islandsInDirection(GameInfoResponse gameInfoResponse, Position sourcePosition,
			double angle) {
		List<Island> islandsInDirection = new ArrayList<>();

		Position[] islandPositions = gameInfoResponse.getGame().getMapConfiguration().getIslandPositions();
		double islandSize = gameInfoResponse.getGame().getMapConfiguration().getIslandSize();
		double submarineSize = gameInfoResponse.getGame().getMapConfiguration().getSubmarineSize();

		for (Position islandPosition : islandPositions) {
			double meredekség = meredekség(angle);
			double a = Math.pow(meredekség, 2) + 1;

			double srcX = sourcePosition.getX().doubleValue();
			double srcY = sourcePosition.getY().doubleValue();

			double c2 = srcY - meredekség * srcX;

			double islandX = islandPosition.getX().doubleValue();
			double islandY = islandPosition.getY().doubleValue();

			double b = 2 * (meredekség * c2 - meredekség * islandY - islandX);
			double c1 = Math.pow(islandY, 2) - Math.pow(islandSize, 2) + Math.pow(islandX, 2) - 2 * c2 * islandY
					+ Math.pow(c2, 2);

			if (Math.pow(b, 2) - 4 * a * c1 >= 0) {
				double distance = distance(sourcePosition, submarineSize, islandPosition, islandSize);
				islandsInDirection.add(new Island(islandPosition, distance));
			}
		}

		Collections.sort(islandsInDirection, (island1, island2) -> island1.distance <= island2.distance ? -1 : 1);
		
		return islandsInDirection.stream().map(island -> island.position).collect(Collectors.toList());
	}
	
	public static boolean willTorpedoHitAnyIsland(Position[] islandPositions, double islandSize, Position torpedoPosition, double torpedoRange,
			double torpedoVelocity, double torpedoAngle, double torpedoRoundsMoved) {
		
		for (Position islandPosition : islandPositions) {
			Position collisionPosition = collisionPosition(islandSize, islandPosition, 0, 0, torpedoPosition, torpedoVelocity, torpedoAngle);
			if (collisionPosition == null) {
				continue;
			}
			double time = Math.abs(torpedoDistance(torpedoPosition, collisionPosition, 0)) / torpedoVelocity;
			if (time <= torpedoRange - torpedoRoundsMoved) {
				return true;
			}
		}
		
		return false;
	}
	
	public static boolean willTorpedoHitAnySubmarine(Submarine[] submarines, double submarineSize, Position torpedoPosition, double torpedoRange,
			double torpedoVelocity, double torpedoAngle, double torpedoRoundsMoved) {
		
		for (Submarine submarine : submarines) {
			Position collisionPosition = collisionPosition(submarineSize, submarine.getPosition(), submarine.getVelocity(), submarine.getAngle(), torpedoPosition, torpedoVelocity, torpedoAngle);
			if (collisionPosition == null) {
				continue;
			}
			double time = Math.abs(torpedoDistance(torpedoPosition, collisionPosition, 0)) / torpedoVelocity;
			if (time <= torpedoRange - torpedoRoundsMoved) {
				return true;
			}
		}
		
		return false;
	}
	
	public static boolean isSubmarineHeadingToIsland(Position islandPosition, double islandSize, Position submarinePosition, double submarineSize, 
			double submarineVelocity, double submarineAngle, double maxAccelerationPerRound) {
		if (islandPosition == null) {
			return false;
		}
		
		double mennyikoronbelultudmegallni = mennyikoronbelultudmegallni(maxAccelerationPerRound, submarineVelocity);
		
		Position newSubmarinePosition = new Position(
				submarinePosition.getX().doubleValue() + xMovement(submarineVelocity, submarineAngle),
				submarinePosition.getY().doubleValue() + yMovement(submarineVelocity, submarineAngle));
		
		Position collisionPosition = collisionPosition(islandSize, islandPosition, 0, 0, newSubmarinePosition, submarineVelocity , submarineAngle);
		if (collisionPosition == null) {
			return false;
		}
		double time = Math.abs(distance(newSubmarinePosition, submarineSize, collisionPosition, 0)) / (submarineVelocity / 2);
		if (time >= mennyikoronbelultudmegallni) {
			return true;
		}
		return false;
	}
	
	public static boolean isSubmarineLeavingSpace(Position submarinePosition, double submarineSize, double submarineVelocity, double submarineAngle,
			double width, double height, double maxAccelerationPerRound) {
		double mennyikoronbelultudmegallni = mennyikoronbelultudmegallni(maxAccelerationPerRound, submarineVelocity);
		
		Position newSubmarinePosition = new Position(
				submarinePosition.getX().doubleValue() + xMovement(submarineVelocity, submarineAngle),
				submarinePosition.getY().doubleValue() + yMovement(submarineVelocity, submarineAngle));
		
		Position position = new Position(
				newSubmarinePosition.getX().doubleValue() + mennyikoronbelultudmegallni * xMovement(submarineVelocity / 2, submarineAngle),
				newSubmarinePosition.getY().doubleValue() + mennyikoronbelultudmegallni * yMovement(submarineVelocity / 2, submarineAngle));
		
		return minDistanceFromEdge(position, submarineSize, width, height) < MathConstants.EPSILON;
	}
	
	public static double minDistanceFromEdge(Position submarinePosition, double submarineSize, double width, double height) {
		double egyik = width - submarinePosition.getX().doubleValue() - submarineSize;
		double masik = height - submarinePosition.getY().doubleValue() - submarineSize;
		double harmadik = submarinePosition.getX().doubleValue() - submarineSize;
		double negyedik = submarinePosition.getY().doubleValue() - submarineSize;
		
		return Math.min(egyik, Math.min(masik, Math.min(harmadik, negyedik)));
	}
	
	public static double mennyikoronbelultudmegallni(double maxAccelerationPerRound, double velocity) {
		return velocity / maxAccelerationPerRound;
	}
	
	public static Position collisionPosition(double targetSize, Position targetPosition, double targetVelocity, double targetAngle,
			Position torpedoPosition, double torpedoVelocity, double torpedoAngle) {
		
		return movingCircleCollisionDetection(torpedoPosition, torpedoVelocity, torpedoAngle, 0, targetPosition, targetVelocity, targetAngle, targetSize);
	}

	public static List<Position> islandsInSubmarineDirection(Position submarinePosition, double submarineVelocity, double submarineAngle,
			double submarineSize, Position[] islandPositions, double islandSize) {
		List<Position> islandsInSubmarineDirections = new ArrayList<>();
		
		for (Position islandPosition : islandPositions) {
			Position position = movingCircleCollisionDetection(submarinePosition, submarineVelocity, submarineAngle, submarineSize, islandPosition, 0, 0, islandSize);
			islandsInSubmarineDirections.add(position);
		}
		
		return islandsInSubmarineDirections;
	}
	
	public static boolean isDangerousToShoot(double torpedoHitPenalty, Position torpedoPosition, List<Submarine> submarines, double submarineSize,
			Position targetPosition, double targetVelocity, double targetAngle, double torpedoVelocity, double torpedoAngle, double torpedoRange) {
		if (torpedoHitPenalty == 0.0) {
			return false;
		}
		Position torpedoCollisionPosition = collisionPosition(submarineSize, targetPosition, targetVelocity, targetAngle, torpedoPosition, torpedoVelocity, torpedoAngle);
		if (torpedoCollisionPosition != null) {
			for (Submarine submarine : submarines) {
				Position detectedPosition = movingCircleCollisionDetection(submarine.getPosition(), submarine.getVelocity(), submarine.getAngle(), submarineSize, torpedoCollisionPosition, 0, 0, torpedoRange);
				if (detectedPosition != null) {
					return true;
				}
			}
		}
		return false;
	}
	
	private static Position movingCircleCollisionDetection(Position sourcePosition, double sourceVelocity, double sourceAngle, double sourceSize,
			Position targetPosition, double targetVelocity, double targetAngle, double targetSize) {
		
		Position Pab = subtract(sourcePosition, targetPosition);
		Position sourceVelocityVector = new Position(xMovement(sourceVelocity, sourceAngle), yMovement(sourceVelocity, sourceAngle));
		Position targetVelocityVector = new Position(xMovement(targetVelocity, targetAngle), yMovement(targetVelocity, targetAngle));
		
		Position Vab = subtract(sourceVelocityVector, targetVelocityVector);
		double a = Vab.getX().pow(2).add(Vab.getY().pow(2)).doubleValue();
		double b = 2 * (Vab.getX().multiply(Pab.getX()).add(Vab.getY().multiply(Pab.getY()))).doubleValue();
		double c = Pab.getX().pow(2).add(Pab.getY().pow(2)).doubleValue() - Math.pow(targetSize + sourceSize, 2);
		
		double q = Math.pow(b, 2) - 4 * a * c;
		if (q < 0.0) {
			log.warn("collisionPoisition q={}", q);
			return null;
		}
		
		double disc = Math.sqrt(q);
		if (disc < 0) {
			return null;
		}
		
		if (a == 0) {
			return null;
		}
		
		double root_1 = (-b + disc) / (2 * a);
		double root_2 = (-b - disc) / (2 * a);
		if (root_1 < 0.0 && root_2 < 0.0) {
			return null;
		}
		double min = root_1 <= root_2 ? root_1 : root_2;
		if (min < 0.0) {
			if (root_1 >= 0.0) {
				min = root_1;
			} else {
				min = root_2;
			}
		}
		
		Position newSourcePosition = new Position(
				sourcePosition.getX().add(sourceVelocityVector.getX().multiply(BigDecimal.valueOf(min))).doubleValue(),
				sourcePosition.getY().add(sourceVelocityVector.getY().multiply(BigDecimal.valueOf(min))).doubleValue());
		
		Position newTargetPosition = new Position(
				targetPosition.getX().add(targetVelocityVector.getX().multiply(BigDecimal.valueOf(min))).doubleValue(),
				targetPosition.getY().add(targetVelocityVector.getY().multiply(BigDecimal.valueOf(min))).doubleValue());
		
		if (targetSize == 0.0) {
			return newTargetPosition;
		}
		
		if (sourceSize == 0.0) {
			return newSourcePosition;
		}

		Position position = subtract(newSourcePosition, newTargetPosition);
		
		double pX = position.getX().doubleValue() * (sourceSize / targetSize);
		double pY = position.getY().doubleValue() * (sourceSize / targetSize);
		Position p = new Position(pX, pY);
		
		return subtract(newSourcePosition, p);
	}
	
	public static Position subtract(Position lhs, Position rhs) {
		return new Position(lhs.getX().subtract(rhs.getX()), lhs.getY().subtract(rhs.getY()));
	}
	
}
