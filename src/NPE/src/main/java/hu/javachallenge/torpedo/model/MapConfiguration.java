package hu.javachallenge.torpedo.model;

import java.util.Arrays;

public class MapConfiguration {

	private double width;
	private double height;
	private Position[] islandPositions;
	private int teamCount;
	private int submarinesPerTeam;
	private int torpedoDamage;
	private int torpedoHitScore;
	private int torpedoHitPenalty;
	private int torpedoCooldown;
	private double sonarRange;
	private double extendedSonarRange;
	private int extendedSonarRounds;
	private int extendedSonarCooldown;
	private double torpedoSpeed;
	private double torpedoExplosionRadius;
	private int roundLength;
	private double islandSize;
	private double submarineSize;
	private int rounds;
	private double maxSteeringPerRound;
	private double maxAccelerationPerRound;
	private double maxSpeed;
	private double torpedoRange;
	private int rateLimitedPenalty;
	private int torpedoDestroyScore;

	public MapConfiguration() {
		this.islandPositions = new Position[0];
	}

	public MapConfiguration(double width, double height, Position[] islandPositions, int teamCount,
			int submarinesPerTeam, int torpedoDamage, int torpedoHitScore, int torpedoHitPenalty, int torpedoCooldown,
			double sonarRange, double extendedSonarRange, int extendedSonarRounds, int extendedSonarCooldown,
			double torpedoSpeed, double torpedoExplosionRadius, int roundLength, double islandSize,
			double submarineSize, int rounds, double maxSteeringPerRound, double maxAccelerationPerRound,
			double maxSpeed, double torpedoRange, int rateLimitedPenalty, int torpedoDestroyScore) {
		super();
		this.width = width;
		this.height = height;
		this.islandPositions = islandPositions;
		this.teamCount = teamCount;
		this.submarinesPerTeam = submarinesPerTeam;
		this.torpedoDamage = torpedoDamage;
		this.torpedoHitScore = torpedoHitScore;
		this.torpedoHitPenalty = torpedoHitPenalty;
		this.torpedoCooldown = torpedoCooldown;
		this.sonarRange = sonarRange;
		this.extendedSonarRange = extendedSonarRange;
		this.extendedSonarRounds = extendedSonarRounds;
		this.extendedSonarCooldown = extendedSonarCooldown;
		this.torpedoSpeed = torpedoSpeed;
		this.torpedoExplosionRadius = torpedoExplosionRadius;
		this.roundLength = roundLength;
		this.islandSize = islandSize;
		this.submarineSize = submarineSize;
		this.rounds = rounds;
		this.maxSteeringPerRound = maxSteeringPerRound;
		this.maxAccelerationPerRound = maxAccelerationPerRound;
		this.maxSpeed = maxSpeed;
		this.torpedoRange = torpedoRange;
		this.rateLimitedPenalty = rateLimitedPenalty;
		this.torpedoDestroyScore = torpedoDestroyScore;
	}

	public double getWidth() {
		return width;
	}

	public void setWidth(double width) {
		this.width = width;
	}

	public double getHeight() {
		return height;
	}

	public void setHeight(double height) {
		this.height = height;
	}

	public Position[] getIslandPositions() {
		return islandPositions;
	}

	public void setIslandPositions(Position[] islandPositions) {
		this.islandPositions = islandPositions;
	}

	public int getTeamCount() {
		return teamCount;
	}

	public void setTeamCount(int teamCount) {
		this.teamCount = teamCount;
	}

	public int getSubmarinesPerTeam() {
		return submarinesPerTeam;
	}

	public void setSubmarinesPerTeam(int submarinesPerTeam) {
		this.submarinesPerTeam = submarinesPerTeam;
	}

	public int getTorpedoDamage() {
		return torpedoDamage;
	}

	public void setTorpedoDamage(int torpedoDamage) {
		this.torpedoDamage = torpedoDamage;
	}

	public int getTorpedoHitScore() {
		return torpedoHitScore;
	}

	public void setTorpedoHitScore(int torpedoHitScore) {
		this.torpedoHitScore = torpedoHitScore;
	}

	public int getTorpedoHitPenalty() {
		return torpedoHitPenalty;
	}

	public void setTorpedoHitPenalty(int torpedoHitPenalty) {
		this.torpedoHitPenalty = torpedoHitPenalty;
	}

	public int getTorpedoCooldown() {
		return torpedoCooldown;
	}

	public void setTorpedoCooldown(int torpedoCooldown) {
		this.torpedoCooldown = torpedoCooldown;
	}

	public double getSonarRange() {
		return sonarRange;
	}

	public void setSonarRange(double sonarRange) {
		this.sonarRange = sonarRange;
	}

	public double getExtendedSonarRange() {
		return extendedSonarRange;
	}

	public void setExtendedSonarRange(double extendedSonarRange) {
		this.extendedSonarRange = extendedSonarRange;
	}

	public int getExtendedSonarRounds() {
		return extendedSonarRounds;
	}

	public void setExtendedSonarRounds(int extendedSonarRounds) {
		this.extendedSonarRounds = extendedSonarRounds;
	}

	public int getExtendedSonarCooldown() {
		return extendedSonarCooldown;
	}

	public void setExtendedSonarCooldown(int extendedSonarCooldown) {
		this.extendedSonarCooldown = extendedSonarCooldown;
	}

	public double getTorpedoSpeed() {
		return torpedoSpeed;
	}

	public void setTorpedoSpeed(double torpedoSpeed) {
		this.torpedoSpeed = torpedoSpeed;
	}

	public double getTorpedoExplosionRadius() {
		return torpedoExplosionRadius;
	}

	public void setTorpedoExplosionRadius(double torpedoExplosionRadius) {
		this.torpedoExplosionRadius = torpedoExplosionRadius;
	}

	public int getRoundLength() {
		return roundLength;
	}

	public void setRoundLength(int roundLength) {
		this.roundLength = roundLength;
	}

	public double getIslandSize() {
		return islandSize;
	}

	public void setIslandSize(double islandSize) {
		this.islandSize = islandSize;
	}

	public double getSubmarineSize() {
		return submarineSize;
	}

	public void setSubmarineSize(double submarineSize) {
		this.submarineSize = submarineSize;
	}

	public int getRounds() {
		return rounds;
	}

	public void setRounds(int rounds) {
		this.rounds = rounds;
	}

	public double getMaxSteeringPerRound() {
		return maxSteeringPerRound;
	}

	public void setMaxSteeringPerRound(double maxSteeringPerRound) {
		this.maxSteeringPerRound = maxSteeringPerRound;
	}

	public double getMaxAccelerationPerRound() {
		return maxAccelerationPerRound;
	}

	public void setMaxAccelerationPerRound(double maxAccelerationPerRound) {
		this.maxAccelerationPerRound = maxAccelerationPerRound;
	}

	public double getMaxSpeed() {
		return maxSpeed;
	}

	public void setMaxSpeed(double maxSpeed) {
		this.maxSpeed = maxSpeed;
	}

	public double getTorpedoRange() {
		return torpedoRange;
	}

	public void setTorpedoRange(double torpedoRange) {
		this.torpedoRange = torpedoRange;
	}

	public int getRateLimitedPenalty() {
		return rateLimitedPenalty;
	}

	public void setRateLimitedPenalty(int rateLimitedPenalty) {
		this.rateLimitedPenalty = rateLimitedPenalty;
	}

	public int getTorpedoDestroyScore() {
		return torpedoDestroyScore;
	}

	public void setTorpedoDestroyScore(int torpedoDestroyScore) {
		this.torpedoDestroyScore = torpedoDestroyScore;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + extendedSonarCooldown;
		long temp;
		temp = Double.doubleToLongBits(extendedSonarRange);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		result = prime * result + extendedSonarRounds;
		temp = Double.doubleToLongBits(height);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		result = prime * result + Arrays.hashCode(islandPositions);
		temp = Double.doubleToLongBits(islandSize);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		temp = Double.doubleToLongBits(maxAccelerationPerRound);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		temp = Double.doubleToLongBits(maxSpeed);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		temp = Double.doubleToLongBits(maxSteeringPerRound);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		result = prime * result + rateLimitedPenalty;
		result = prime * result + roundLength;
		result = prime * result + rounds;
		temp = Double.doubleToLongBits(sonarRange);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		temp = Double.doubleToLongBits(submarineSize);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		result = prime * result + submarinesPerTeam;
		result = prime * result + teamCount;
		result = prime * result + torpedoCooldown;
		result = prime * result + torpedoDamage;
		result = prime * result + torpedoDestroyScore;
		temp = Double.doubleToLongBits(torpedoExplosionRadius);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		result = prime * result + torpedoHitPenalty;
		result = prime * result + torpedoHitScore;
		temp = Double.doubleToLongBits(torpedoRange);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		temp = Double.doubleToLongBits(torpedoSpeed);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		temp = Double.doubleToLongBits(width);
		result = prime * result + (int) (temp ^ (temp >>> 32));
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
		if (!(obj instanceof MapConfiguration)) {
			return false;
		}
		MapConfiguration other = (MapConfiguration) obj;
		if (extendedSonarCooldown != other.extendedSonarCooldown) {
			return false;
		}
		if (Double.doubleToLongBits(extendedSonarRange) != Double.doubleToLongBits(other.extendedSonarRange)) {
			return false;
		}
		if (extendedSonarRounds != other.extendedSonarRounds) {
			return false;
		}
		if (Double.doubleToLongBits(height) != Double.doubleToLongBits(other.height)) {
			return false;
		}
		if (!Arrays.equals(islandPositions, other.islandPositions)) {
			return false;
		}
		if (Double.doubleToLongBits(islandSize) != Double.doubleToLongBits(other.islandSize)) {
			return false;
		}
		if (Double.doubleToLongBits(maxAccelerationPerRound) != Double
				.doubleToLongBits(other.maxAccelerationPerRound)) {
			return false;
		}
		if (Double.doubleToLongBits(maxSpeed) != Double.doubleToLongBits(other.maxSpeed)) {
			return false;
		}
		if (Double.doubleToLongBits(maxSteeringPerRound) != Double.doubleToLongBits(other.maxSteeringPerRound)) {
			return false;
		}
		if (rateLimitedPenalty != other.rateLimitedPenalty) {
			return false;
		}
		if (roundLength != other.roundLength) {
			return false;
		}
		if (rounds != other.rounds) {
			return false;
		}
		if (Double.doubleToLongBits(sonarRange) != Double.doubleToLongBits(other.sonarRange)) {
			return false;
		}
		if (Double.doubleToLongBits(submarineSize) != Double.doubleToLongBits(other.submarineSize)) {
			return false;
		}
		if (submarinesPerTeam != other.submarinesPerTeam) {
			return false;
		}
		if (teamCount != other.teamCount) {
			return false;
		}
		if (torpedoCooldown != other.torpedoCooldown) {
			return false;
		}
		if (torpedoDamage != other.torpedoDamage) {
			return false;
		}
		if (torpedoDestroyScore != other.torpedoDestroyScore) {
			return false;
		}
		if (Double.doubleToLongBits(torpedoExplosionRadius) != Double.doubleToLongBits(other.torpedoExplosionRadius)) {
			return false;
		}
		if (torpedoHitPenalty != other.torpedoHitPenalty) {
			return false;
		}
		if (torpedoHitScore != other.torpedoHitScore) {
			return false;
		}
		if (Double.doubleToLongBits(torpedoRange) != Double.doubleToLongBits(other.torpedoRange)) {
			return false;
		}
		if (Double.doubleToLongBits(torpedoSpeed) != Double.doubleToLongBits(other.torpedoSpeed)) {
			return false;
		}
		if (Double.doubleToLongBits(width) != Double.doubleToLongBits(other.width)) {
			return false;
		}
		return true;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("MapConfiguration [width=");
		builder.append(width);
		builder.append(", height=");
		builder.append(height);
		builder.append(", islandPositions=");
		builder.append(Arrays.toString(islandPositions));
		builder.append(", teamCount=");
		builder.append(teamCount);
		builder.append(", submarinesPerTeam=");
		builder.append(submarinesPerTeam);
		builder.append(", torpedoDamage=");
		builder.append(torpedoDamage);
		builder.append(", torpedoHitScore=");
		builder.append(torpedoHitScore);
		builder.append(", torpedoHitPenalty=");
		builder.append(torpedoHitPenalty);
		builder.append(", torpedoCooldown=");
		builder.append(torpedoCooldown);
		builder.append(", sonarRange=");
		builder.append(sonarRange);
		builder.append(", extendedSonarRange=");
		builder.append(extendedSonarRange);
		builder.append(", extendedSonarRounds=");
		builder.append(extendedSonarRounds);
		builder.append(", extendedSonarCooldown=");
		builder.append(extendedSonarCooldown);
		builder.append(", torpedoSpeed=");
		builder.append(torpedoSpeed);
		builder.append(", torpedoExplosionRadius=");
		builder.append(torpedoExplosionRadius);
		builder.append(", roundLength=");
		builder.append(roundLength);
		builder.append(", islandSize=");
		builder.append(islandSize);
		builder.append(", submarineSize=");
		builder.append(submarineSize);
		builder.append(", rounds=");
		builder.append(rounds);
		builder.append(", maxSteeringPerRound=");
		builder.append(maxSteeringPerRound);
		builder.append(", maxAccelerationPerRound=");
		builder.append(maxAccelerationPerRound);
		builder.append(", maxSpeed=");
		builder.append(maxSpeed);
		builder.append(", torpedoRange=");
		builder.append(torpedoRange);
		builder.append(", rateLimitedPenalty=");
		builder.append(rateLimitedPenalty);
		builder.append(", torpedoDestroyScore=");
		builder.append(torpedoDestroyScore);
		builder.append("]");
		return builder.toString();
	}

}
