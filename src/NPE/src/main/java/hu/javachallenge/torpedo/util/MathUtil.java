package hu.javachallenge.torpedo.util;

import static hu.javachallenge.torpedo.util.MathUtil.getNearestIslandInDirection;
import static hu.javachallenge.torpedo.util.MathUtil.islandsInDirection;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import hu.javachallenge.torpedo.model.Entity;
import hu.javachallenge.torpedo.model.Position;
import hu.javachallenge.torpedo.model.Submarine;
import hu.javachallenge.torpedo.response.GameInfoResponse;
import java.util.Arrays;
import java.util.LinkedList;

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
	
	public static boolean isPositionLeftToUs(Position s, Position e, Position p){
		return ((e.getX().doubleValue() - s.getX().doubleValue())*(p.getY().doubleValue() - s.getY().doubleValue()) - (e.getY().doubleValue() - s.getY().doubleValue())*(p.getX().doubleValue() - s.getX().doubleValue())) > 0;
   }
	
	private static double angleDifference(double alpha, double beta) {
        double phi = normalizeAngle(Math.abs(beta - alpha));       // This is either the distance or 360 - distance
        double distance = phi > 180 ? 360 - phi : phi;
        return distance;
    }
	
	public static double distanceOfCircles(Position sourcePosition, double sourceR, Position destinationPosition,
			double destinationR) {
		BigDecimal xSubtract = sourcePosition.getX().subtract(destinationPosition.getX());
		BigDecimal xSquare = xSubtract.multiply(xSubtract);

		BigDecimal ySubtract = sourcePosition.getY().subtract(destinationPosition.getY());
		BigDecimal ySquare = ySubtract.multiply(ySubtract);

		return Math.sqrt(xSquare.add(ySquare).doubleValue()) - (sourceR + destinationR);
	}

	public static double torpedoDistance(Position sourcePosition, Position destinationPosition, double destinationR) {
		return distanceOfCircles(sourcePosition, 0.0, destinationPosition, destinationR);
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
	public static Double aimAtMovingTarget(Position sourcePosition, Position targetPosition, double targetMovementAngle,
			double targetVelocity, double bulletVelocity) {
		double dX = targetPosition.getX().subtract(sourcePosition.getX()).doubleValue();
		double dY = targetPosition.getY().subtract(sourcePosition.getY()).doubleValue();

		double xMovementResult = xMovement(targetVelocity, targetMovementAngle);
		double yMovementResult = yMovement(targetVelocity, targetMovementAngle);

		double a = Math.pow(xMovementResult, 2) + Math.pow(yMovementResult, 2) - Math.pow(bulletVelocity, 2);
		double b = 2 * (xMovementResult * dX + yMovementResult * dY);
		double c = Math.pow(dX, 2) + Math.pow(dY, 2);
		
		if(Math.abs(a) < MathConstants.EPSILON) {
			return null;
		}
		
		// Check we're not breaking into complex numbers
		double q = Math.pow(b, 2) - 4 * a * c;
		if (q < MathConstants.EPSILON) {
			log.error("Math.pow({}) cannot be interpreted on negative numbers.", q);
			return null;
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

				double islandDistance = distanceOfCircles(sourcePosition, submarineSize, islandPosition, islandSize);

				double submarineXMovement = xMovement(submarine.getVelocity(), submarine.getAngle());
				double submarineYMovement = yMovement(submarine.getVelocity(), submarine.getAngle());
				Position newSubmarinePosition = new Position(submarineXMovement, submarineYMovement);

				double submarineDistance = distanceOfCircles(sourcePosition, submarineSize, newSubmarinePosition, submarineSize);

				if (islandDistance > submarineDistance) {
					possibleSubmarineTargets.add(submarine);
				}
			}
		}

		return possibleSubmarineTargets;
	}
	
	public static Position getNearestIsland(GameInfoResponse gameInfoResponse, Position sourcePosition) {
		List<Island> islands = new ArrayList<>();
		Position[] islandPositions = gameInfoResponse.getGame().getMapConfiguration().getIslandPositions();
		double islandSize = gameInfoResponse.getGame().getMapConfiguration().getIslandSize();
		double submarineSize = gameInfoResponse.getGame().getMapConfiguration().getSubmarineSize();
		double sonarRange = gameInfoResponse.getGame().getMapConfiguration().getSonarRange();

		for (Position islandPosition : islandPositions) {
			double distance = distanceOfCircles(sourcePosition, sonarRange, islandPosition, islandSize);
			if(distance < 0) {
				islands.add(new Island(islandPosition, distance));
			}
		}
		Collections.sort(islands, (island1, island2) -> island1.distance <= island2.distance ? -1 : 1);
		if(!islands.isEmpty()) {
			return islands.get(0).position;
		} else {
			return null;
		}
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
				double distance = distanceOfCircles(sourcePosition, submarineSize, islandPosition, islandSize);
				islandsInDirection.add(new Island(islandPosition, distance));
			}
		}

		Collections.sort(islandsInDirection, (island1, island2) -> island1.distance <= island2.distance ? -1 : 1);
		
		return islandsInDirection.stream().map(island -> island.position).collect(Collectors.toList());
	}
	
	public static List<Position> whereCouldTorpedoHitIslands(List<Position> islandPositions, double islandSize, Position torpedoPosition, double torpedoRange,
			double torpedoVelocity, double torpedoAngle, double torpedoRoundsMoved) {
		List<Position> collisionPositions = new LinkedList<>();
		for (Position islandPosition : islandPositions) {
			Position collisionPosition = collisionPosition(islandSize, islandPosition, 0, 0, torpedoPosition, torpedoVelocity, torpedoAngle);
			if (collisionPosition != null) {
				double time = Math.abs(torpedoDistance(torpedoPosition, collisionPosition, 0)) / torpedoVelocity;
				if (time <= torpedoRange - torpedoRoundsMoved) {
					collisionPositions.add(collisionPosition);
				}
			}
		}
		
		return collisionPositions;
	}
	
	public static List<Position> whereCouldTorpedoHitUs(List<Submarine> submarines, double submarineSize, Position torpedoPosition, double torpedoRange,
			double torpedoVelocity, double torpedoAngle, double torpedoRoundsMoved) {
		List<Position> collisionPositions = new LinkedList<>();
		for (Submarine submarine : submarines) {
			if(!submarine.getPosition().equals(torpedoPosition)) {
				Position collisionPosition = collisionPosition(submarineSize, submarine.getPosition(), submarine.getVelocity(), submarine.getAngle(), torpedoPosition, torpedoVelocity, torpedoAngle);
				if (collisionPosition != null) {
					double time = Math.abs(torpedoDistance(torpedoPosition, collisionPosition, 0)) / torpedoVelocity;
					if (time <= torpedoRange - torpedoRoundsMoved) {
						collisionPositions.add(collisionPosition);
					}
				}
			}
		}
		
		return collisionPositions;
	}
	
	public static Position whereCouldTorpedoHitAimedTarget(Submarine submarine, double submarineSize, Position torpedoPosition, double torpedoRange,
			double torpedoVelocity, double torpedoAngle, double torpedoRoundsMoved) {
		Position collisionPosition = collisionPosition(submarineSize, submarine.getPosition(), submarine.getVelocity(), submarine.getAngle(), torpedoPosition, torpedoVelocity, torpedoAngle);
		if (collisionPosition != null) {
			double time = Math.abs(torpedoDistance(torpedoPosition, collisionPosition, 0)) / torpedoVelocity;
			if (time <= torpedoRange - torpedoRoundsMoved) {
				return collisionPosition;
			}
		}
		return null;
	}
	
	public static Submarine getNearestSubmarineInOurWay(Submarine actualSubmarine, List<Submarine> allSubmarine, double submarineSize, double maxSpeed) {
		Submarine nearestSubmarine = null;
		for (Submarine submarine : allSubmarine) {
			if(!actualSubmarine.equals(submarine)) {
				Position p = movingCircleCollisionDetection(submarine.getPosition(), submarine.getVelocity(), submarine.getAngle(), submarineSize, actualSubmarine.getPosition(), maxSpeed, actualSubmarine.getAngle(), submarineSize);
				if(p != null) {
					if(nearestSubmarine == null) {
						nearestSubmarine = submarine;
					} else {
						if(distanceOfCircles(actualSubmarine.getPosition(), submarineSize, submarine.getPosition(), submarineSize) < distanceOfCircles(actualSubmarine.getPosition(), submarineSize, nearestSubmarine.getPosition(), submarineSize)) {
							nearestSubmarine = submarine;
						}
					}
				}
			}
		}
		return nearestSubmarine;
	}
	
	public static Submarine getBiggestSonarIntersectionSubmarine(Submarine submarine, Submarine[] submarines, double sonarRange, double extendedSonarRange) {
		double intersection = 0.0;
		Submarine biggestSonarIntersectionSubmarine = null;
		for (Submarine s : submarines) {
			if(!s.equals(submarine)) {
				double temp = intersectionOfCircles(s.getPosition(), submarine.getPosition(), s.getSonarExtended() > 0 ? extendedSonarRange : sonarRange, submarine.getSonarExtended() > 0 ? extendedSonarRange : sonarRange);
				if(temp > intersection) {
					intersection = temp;
					biggestSonarIntersectionSubmarine = s;
				}
			}
			
		}
		return biggestSonarIntersectionSubmarine;
	}
	
	public static double getSteeringBasedOnEnemyPosition(Submarine submarine1, Submarine[] enemySubmarines, double sonarRange, double maxSteering) {
		Submarine submarine2 = getBiggestSonarIntersectionSubmarine(submarine1, enemySubmarines, sonarRange, sonarRange);
		
		if(submarine2 == null) {
			return 0.0;
		}
		
		if(intersectionOfCirclesWithSameRadius(submarine1.getPosition(), submarine2.getPosition(), sonarRange) > 0){
			double xValue = submarine1.getPosition().getX().doubleValue() + xMovement(10, submarine1.getAngle());
			double yValue = submarine1.getPosition().getY().doubleValue() + yMovement(10, submarine1.getAngle());
			Position newPosition = new Position(xValue , yValue);
				if(isPositionLeftToUs(submarine1.getPosition(), newPosition, submarine2.getPosition())) {
				return maxSteering;
			} else {
				return -maxSteering;
			}
		} else {
			return 0.0;
		}
	}
	
	public static double getSteeringBasedOnSonars(Submarine submarine1, Submarine[] submarines, double sonarRange, double extendedSonarRange, double maxSteering) {
		Submarine submarine2 = getBiggestSonarIntersectionSubmarine(submarine1, submarines, sonarRange, extendedSonarRange);
		
		if(submarine2 == null) {
			return 0.0;
		}
		
		if(intersectionOfCircles(submarine1.getPosition(), submarine2.getPosition(), submarine1.getSonarExtended() > 0 ? extendedSonarRange : sonarRange, submarine2.getSonarExtended() > 0 ? extendedSonarRange : sonarRange) > 0){
			double xValue = submarine1.getPosition().getX().doubleValue() + xMovement(10, submarine1.getAngle());
			double yValue = submarine1.getPosition().getY().doubleValue() + yMovement(10, submarine1.getAngle());
			Position newPosition = new Position(xValue , yValue);
				if(isPositionLeftToUs(submarine1.getPosition(), newPosition, submarine2.getPosition())) {
				return -maxSteering;
			} else {
				return maxSteering;
			}
		} else {
			return 0.0;
		}
	}
	
	public static double getSteeringHeadingToIsland(Submarine submarine, Position islandPosition, double maxSteering, double submarineSize, double islandSize) {
		Position collisionPosition = movingCircleCollisionDetection(submarine.getPosition(), submarine.getVelocity(), submarine.getAngle(), submarineSize, islandPosition, 0, 0, islandSize);
		if(collisionPosition == null) {
			return 0.0;
		}
		double xValue = submarine.getPosition().getX().doubleValue() + xMovement(10, submarine.getAngle());
		double yValue = submarine.getPosition().getY().doubleValue() + yMovement(10, submarine.getAngle());
		Position newPosition = new Position(xValue , yValue);
		if(isPositionLeftToUs(submarine.getPosition(), newPosition, collisionPosition)) {
			return -maxSteering;
		} else {
			return maxSteering;
		}
	}
	
	public static double getSteeringHeadingToSubmarine(Submarine actualSubmarine, Submarine otherSubmarine, double maxSteering, double submarineSize) {
		Position collisionPosition = movingCircleCollisionDetection(actualSubmarine.getPosition(), actualSubmarine.getVelocity(), actualSubmarine.getAngle(), submarineSize, otherSubmarine.getPosition(), otherSubmarine.getVelocity(), otherSubmarine.getAngle(), submarineSize);
		if(collisionPosition == null) {
			return 0.0;
		}
		double xValue = actualSubmarine.getPosition().getX().doubleValue() + xMovement(10, actualSubmarine.getAngle());
		double yValue = actualSubmarine.getPosition().getY().doubleValue() + yMovement(10, actualSubmarine.getAngle());
		Position newPosition = new Position(xValue , yValue);
		if(isPositionLeftToUs(actualSubmarine.getPosition(), newPosition, collisionPosition)) {
			return -maxSteering;
		} else {
			return maxSteering;
		}
	}
	
	public static double getSteeringHeadingToEdge(Position submarinePosition, double submarineSize, double width, double height, double submarineAngle, double sonarRange, double maxSteering) {
		double fromRight = width - submarinePosition.getX().doubleValue() - submarineSize;
		double fromTop = height - submarinePosition.getY().doubleValue() - submarineSize;
		double fromLeft = submarinePosition.getX().doubleValue() - submarineSize;
		double fromBottom = submarinePosition.getY().doubleValue() - submarineSize;
		
		if (submarineAngle == 0.0) {
			if (fromRight < sonarRange) {
				if (fromTop < fromBottom) {
					return -maxSteering;
				} else {
					return maxSteering;
				}
			}
		} else if (submarineAngle == 90.0) {
			if (fromTop < sonarRange) {
				if (fromLeft < fromRight) {
					return -maxSteering;
				} else {
					return maxSteering;
				}
			}
		} else if (submarineAngle == 180.0) {
			if (fromLeft < sonarRange) {
				if (fromBottom < fromTop) {
					return -maxSteering;
				} else{
					return maxSteering;
				}
			}
		} else if (submarineAngle == 270.0) {
			if(fromBottom < sonarRange) {
				if (fromRight < fromLeft) {
					return -maxSteering;
				} else {
					return maxSteering;
				}
			}
		} else if (submarineAngle > 0.0 && submarineAngle < 90.0) {
			if (fromTop < sonarRange && fromRight < sonarRange) {
				if(distanceOfCircles(submarinePosition, submarineSize, new Position(width, height), 0) < sonarRange) {
					if(fromTop < fromRight) {
						return -maxSteering;
					} else {
						return maxSteering;
					}
				}
			} else if (fromTop < sonarRange) {
				return -maxSteering;
			} else if (fromRight < sonarRange) {
				return  maxSteering;
			}
		} else if (submarineAngle > 90.0 && submarineAngle < 180.0) {
			if (fromLeft < sonarRange && fromTop < sonarRange) {
				if(distanceOfCircles(submarinePosition, submarineSize, new Position(0, height), 0) < sonarRange) {
					if(fromLeft < fromTop) {
						return -maxSteering;
					} else {
						return maxSteering;
					}
				}
			} else if (fromLeft < sonarRange) {
				return -maxSteering;
			} else if (fromTop < sonarRange) {
				return  maxSteering;
			}
		} else if (submarineAngle > 180.0 && submarineAngle < 270.0) {
			if (fromBottom < sonarRange && fromLeft < sonarRange) {
				if(distanceOfCircles(submarinePosition, submarineSize, new Position(0, 0), 0) < sonarRange) {
					if(fromBottom < fromLeft) {
						return -maxSteering;
					} else {
						return maxSteering;
					}
				}
			} else if (fromBottom < sonarRange) {
				return -maxSteering;
			} else if (fromLeft < sonarRange) {
				return  maxSteering;
			}
		} else {
			if (fromRight < sonarRange && fromBottom < sonarRange) {
				if(distanceOfCircles(submarinePosition, submarineSize, new Position(0, width), 0) < sonarRange) {
					if(fromRight < fromBottom) {
						return -maxSteering;
					} else {
						return maxSteering;
					}
				}
			} else if (fromRight < sonarRange) {
				return -maxSteering;
			} else if (fromBottom < sonarRange) {
				return  maxSteering;
			}
		}
		
		return 0.0;
	}
        
	public static boolean isSubmarineHeadingToTorpedoExplosion(List<Entity> torpedos, Position submarinePosition, double submarineVelocity, double submarineAngle,
			double submarineSize, List<Submarine> enemySubmarines, double torpedoExplosionRadius, List<Position> islandPositions, double islandSize) {
		for (Entity torpedo : torpedos) {
			if (isSubmarineHeadingToTorpedoExplosion(torpedo.getPosition(), submarinePosition, submarineVelocity, submarineAngle, submarineSize, enemySubmarines, torpedo.getVelocity(), torpedo.getAngle(), torpedoExplosionRadius, islandPositions, islandSize)) {
				return true;
			}
		}
		return false;
	}
	
	public static boolean isSubmarineHeadingToTorpedoExplosion(Position torpedoPosition, Position submarinePosition,
			double submarineVelocity, double submarineAngle, double submarineSize, List<Submarine> enemySubmarines, double torpedoVelocity, double torpedoAngle,
			double torpedoExplosionRadius, List<Position> islandPositions, double islandSize) {
		Position newSubmarinePosition = new Position(
				submarinePosition.getX().doubleValue() + xMovement(submarineVelocity, submarineAngle),
				submarinePosition.getY().doubleValue() + yMovement(submarineVelocity, submarineAngle));
		Position torpedoSubmarineCollisionPosition = collisionPosition(submarineSize, newSubmarinePosition, submarineVelocity, submarineAngle, torpedoPosition, torpedoVelocity, torpedoAngle);
		if (torpedoSubmarineCollisionPosition != null) {
			return true;
		}
		for (Submarine enemy : enemySubmarines) {
			Position torpedoCollisionPosition = collisionPosition(submarineSize, enemy.getPosition(), enemy.getVelocity(), enemy.getAngle(), torpedoPosition, torpedoVelocity, torpedoAngle);
			if (torpedoCollisionPosition != null) {
				Position detectedPosition = movingCircleCollisionDetection(newSubmarinePosition, submarineVelocity, submarineAngle, submarineSize, torpedoCollisionPosition, 0, 0, torpedoExplosionRadius);
				if (detectedPosition != null) {
					return true;
				}
			}
		}
		for (Position islandPosition : islandPositions) {
			Position torpedoCollisionPosition = collisionPosition(islandSize, islandPosition, 0, 0, torpedoPosition, torpedoVelocity, torpedoAngle);
			if (torpedoCollisionPosition != null) {
				Position detectedPosition = movingCircleCollisionDetection(newSubmarinePosition, submarineVelocity, submarineAngle, submarineSize, torpedoCollisionPosition, 0, 0, torpedoExplosionRadius);
				if (detectedPosition != null) {
					return true;
				}
			}
		}

		return false;
	}
	
	public static boolean isSubmarineHeadingToIsland(Position islandPosition, double islandSize, Position submarinePosition, double submarineSize, 
			double submarineVelocity, double submarineAngle, double maxAccelerationPerRound) {
		if (islandPosition == null) {
			return false;
		}
		
		double mennyikoronbelultudmegallni = Math.ceil(mennyikoronbelultudmegallni(maxAccelerationPerRound, submarineVelocity)) + 1;
		
		Position newSubmarinePosition = new Position(
				submarinePosition.getX().doubleValue() + xMovement(submarineVelocity, submarineAngle),
				submarinePosition.getY().doubleValue() + yMovement(submarineVelocity, submarineAngle));
		
		Position collisionPosition = collisionPosition(islandSize, islandPosition, 0, 0, newSubmarinePosition, submarineVelocity , submarineAngle);
		if (collisionPosition == null) {
			return false;
		}
		double time = Math.abs(distanceOfCircles(newSubmarinePosition, submarineSize, collisionPosition, 0)) / (submarineVelocity / 2);
		if (time >= mennyikoronbelultudmegallni) {
			return true;
		}
		return false;
	}
	
	public static boolean isSubmarineLeavingSpace(Position submarinePosition, double submarineSize, double submarineVelocity, double submarineAngle,
			double width, double height, double maxAccelerationPerRound) {
		double mennyikoronbelultudmegallni = Math.ceil(mennyikoronbelultudmegallni(maxAccelerationPerRound, submarineVelocity)) + 1;
		
		Position newSubmarinePosition = new Position(
				submarinePosition.getX().doubleValue() + xMovement(submarineVelocity, submarineAngle),
				submarinePosition.getY().doubleValue() + yMovement(submarineVelocity, submarineAngle));
		
		Position position = new Position(
				newSubmarinePosition.getX().doubleValue() + mennyikoronbelultudmegallni * xMovement(submarineVelocity / 2, submarineAngle),
				newSubmarinePosition.getY().doubleValue() + mennyikoronbelultudmegallni * yMovement(submarineVelocity / 2, submarineAngle));
		
		return minDistanceFromEdgeInWay(position, submarineSize, width, height, submarineAngle) < 2 * submarineSize;
	}
	
	public static double minDistanceFromEdgeInWay(Position submarinePosition, double submarineSize, double width, double height, double submarineAngle) {
		double fromRight = width - submarinePosition.getX().doubleValue() - submarineSize;
		double fromTop = height - submarinePosition.getY().doubleValue() - submarineSize;
		double fromLeft = submarinePosition.getX().doubleValue() - submarineSize;
		double fromBottom = submarinePosition.getY().doubleValue() - submarineSize;
		
		if (submarineAngle == 0.0) {
			return fromRight;
		} else if (submarineAngle == 90.0) {
			return fromTop;
		} else if (submarineAngle == 180.0) {
			return fromLeft;
		} else if (submarineAngle == 270.0) {
			return fromBottom;
		} else if (submarineAngle > 0.0 && submarineAngle < 90.0) {
			return Math.min(fromRight, fromTop);
		} else if (submarineAngle > 90.0 && submarineAngle < 180.0) {
			return Math.min(fromLeft, fromTop);
		} else if (submarineAngle > 180.0 && submarineAngle < 270.0) {
			return Math.min(fromLeft, fromBottom);
		} else {
			return Math.min(fromRight, fromBottom);
		}
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
	
	public static boolean shouldWeShoot(Position torpedoPosition, List<Submarine> submarines, double submarineSize,
			Submarine targetSubmarine, double torpedoRange, double torpedoVelocity, double torpedoAngle, double torpedoExplosionRadius, List<Position> islandPositions, double islandSize) {
		Position aimedTargetCollisionPosition = whereCouldTorpedoHitAimedTarget(targetSubmarine, submarineSize, torpedoPosition, torpedoRange, torpedoVelocity, torpedoAngle, 0);
		List<Position> islandTorpedoCollisionPositions = whereCouldTorpedoHitIslands(islandPositions, islandSize, torpedoPosition, torpedoRange, torpedoVelocity, torpedoAngle, 0);
		List<Position> submarineTorpedoCollisionPositions = whereCouldTorpedoHitUs(submarines, submarineSize, torpedoPosition, torpedoRange, torpedoVelocity, torpedoAngle, 0);
		
		if(aimedTargetCollisionPosition != null)  {
			if(!islandTorpedoCollisionPositions.isEmpty()) {
				for (Position islandTorpedoCollisionPosition : islandTorpedoCollisionPositions) {
					if(distanceOfCircles(islandTorpedoCollisionPosition, 0, torpedoPosition, 0) < distanceOfCircles(aimedTargetCollisionPosition, 0, torpedoPosition, 0)) {
						return false;
					}
				}
			}
			if(!submarineTorpedoCollisionPositions.isEmpty()) {
				for (Position submarineTorpedoCollisionPosition : submarineTorpedoCollisionPositions) {
					if(distanceOfCircles(submarineTorpedoCollisionPosition, 0, torpedoPosition, 0) < distanceOfCircles(aimedTargetCollisionPosition, 0, torpedoPosition, 0)) {
						return false;
					}
				}
			}
			for (Submarine submarine : submarines) {
				Position detectedPosition = movingCircleCollisionDetection(submarine.getPosition(), submarine.getVelocity(), submarine.getAngle(), submarineSize, aimedTargetCollisionPosition, 0, 0, torpedoExplosionRadius);
				if (detectedPosition != null) {
					return false;
				}
			}
			return true;
		} else {
			return false;
		}
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
		
		if(Math.abs(a) < MathConstants.EPSILON) {
			return null;
		}
		
		double q = Math.pow(b, 2) - 4 * a * c;
		if (q < 0.0) {
			log.warn("collisionPoisition q={}", q);
				return null;
			}
		
		double disc = Math.sqrt(q);
		if (disc < MathConstants.EPSILON) {
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
	
	public static Set<DangerType> getDangerTypes(GameInfoResponse gameInfo, List<Position> islandPositions, double islandSize, Position submarinePosition,
			double submarineSize, double submarineVelocity, double submarineAngle, double maxAccelerationPerRound, double width, double height,
			List<Entity> torpedos, List<Submarine> enemySubmarines, double torpedoExplosionRadius) {
		Set<DangerType> dangerTypes = new HashSet<>();
		
		List<Position> islandsInDirection = islandsInDirection(gameInfo, submarinePosition, submarineAngle);
		Position nearestIslandInDirection = getNearestIslandInDirection(islandsInDirection);
		
		if (isSubmarineHeadingToIsland(nearestIslandInDirection, islandSize, submarinePosition, submarineSize, submarineVelocity, submarineAngle, maxAccelerationPerRound)) {
			dangerTypes.add(DangerType.HEADING_TO_ISLAND);
		}
		
		if (isSubmarineLeavingSpace(submarinePosition, submarineSize, submarineVelocity, submarineAngle, width, height, maxAccelerationPerRound)) {
			dangerTypes.add(DangerType.LEAVING_SPACE);
		}
		
		if (isSubmarineHeadingToTorpedoExplosion(torpedos, submarinePosition, submarineVelocity, submarineAngle, submarineSize, enemySubmarines, torpedoExplosionRadius, islandPositions, islandSize)) {
			dangerTypes.add(DangerType.HEADING_TO_TORPEDO_EXPLOSION);
		}

		return dangerTypes;
	}

	/**
	 * Source: http://jwilson.coe.uga.edu/EMAT6680Su12/Carreras/EMAT6690/Essay2/essay2.html
	 * Megmondja, hogy a paraméterül kapott két körnek mekkora területi átfedése van.
	 */
	public static double intersectionOfCirclesWithSameRadius(Position a, Position b, double r) {
		double distanceOfPoints = distanceOfCircles(a, 0.0, b, 0.0);
		if (distanceOfPoints >= 2.0 * r) {
			return 0.0;
		}
		
		double theta = 2.0 * Math.acos(distanceOfPoints / (2.0 * r));
		
		return r * r * (theta - Math.sin(theta));
	}
	
	/**
	 * Megmondja, hogy a paraméterül kapott két körnek mekkora területi átfedése van.
	 */
	public static double intersectionOfCircles(Position pa, Position pb, double ra, double rb) {
		double d = distanceOfCircles(pa, 0.0, pb, 0.0);
		double R = ra > rb ? ra : rb;
		double r = ra < rb ? ra : rb;
		if (d >= (R + r)) {
			return 0.0;
		}
		double a = (1.0 / d) * Math.sqrt(4 * d * d * R * R - Math.pow(d * d - r * r + R * R, 2.0));
		
		double alpha = Math.toDegrees(Math.asin(a / (2.0 * R)) * 2.0);
		double beta = Math.toDegrees(Math.asin(a / (2.0 * r)) * 2.0);
		
		double TC = (alpha / 360.0) * R * R * Math.PI;
		double tC = (beta / 360.0) * r * r * Math.PI;
		
		double x = Math.sqrt(R * R - Math.pow(a / 2.0, 2.0));
		double y = d - x;
		
		double TH = x * a;
		double tH = y * a;
		
		double TL = TC - TH;
		double tL = tC - tH;
		
		return TL + tL;
	}
	
	/**
	 * Megmondja, hogy ha adott két körgyűrű, akkor mekkora az általuk közösen fedett terület.
	 */
	public static double intersectionOfRings(Position pa, Position pb, double ra, double rb) {
		double d = distanceOfCircles(pa, 0.0, pb, 0.0);
		double R = ra > rb ? ra : rb;
		double r = ra < rb ? ra : rb;
		
		// A gyűrűk nem fedik egymást:
		if (d >= 2 * R) {
			return 0.0;
		}
		
		if (d >= R + r) {
			return intersectionOfCirclesWithSameRadius(pa, pb, R);
		} else if (d >= 2 * r) {
			double intOfDiffCircles = intersectionOfCircles(pa, pb, R, r);
			return intersectionOfCirclesWithSameRadius(pa, pb, R) - 2 * intOfDiffCircles; 
		} else if (d >= r) {
			double intOfLittles = intersectionOfCirclesWithSameRadius(pa, pb, r);
			double intOfDiffCircles = intersectionOfCircles(pa, pb, R, r);
			return intersectionOfCirclesWithSameRadius(pa, pb, R) - 2 * intOfDiffCircles - intOfLittles;
		} else {
			return 2 * r * r * Math.PI - intersectionOfCirclesWithSameRadius(pa, pb, r);
		}
	}
}
