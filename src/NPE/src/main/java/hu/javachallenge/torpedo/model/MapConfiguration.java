package hu.javachallenge.torpedo.model;

import java.util.Arrays;

public class MapConfiguration {

	private int width;
	private int height;
	private IslandPosition[] islandPositions;
	private int teamCount;
	private int submarinesPerTeam;
	private int torpedoDamage;
	private int torpedoHitScore;
	private int torpedoHitPenalty;
	private int torpedoCooldown;
	private int sonarRange;
	private int extendedSonarRange;
	private int extendedSonarRounds;
	private int extendedSonarCooldown;
	private int torpedoSpeed;
	private int torpedoExplosionRadius;
	private int roundLength;
	private int islandSize;
	private int submarineSize;
	private int rounds;
	private int maxSteeringPerRound;
	private int maxAccelerationPerRound;
	private int maxSpeed;
	private int torpedoRange;
	private int rateLimitedPenalty;
	private int torpedoDestroyScore;

	public MapConfiguration() {
		this.islandPositions = new IslandPosition[0];
	}

	public MapConfiguration(int width, int height, IslandPosition[] islandPositions, int teamCount,
			int submarinesPerTeam, int torpedoDamage, int torpedoHitScore, int torpedoHitPenalty, int torpedoCooldown,
			int sonarRange, int extendedSonarRange, int extendedSonarRounds, int extendedSonarCooldown,
			int torpedoSpeed, int torpedoExplosionRadius, int roundLength, int islandSize, int submarineSize,
			int rounds, int maxSteeringPerRound, int maxAccelerationPerRound, int maxSpeed, int torpedoRange,
			int rateLimitedPenalty, int torpedoDestroyScore) {
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

	public int getWidth() {
		return width;
	}

	public void setWidth(int width) {
		this.width = width;
	}

	public int getHeight() {
		return height;
	}

	public void setHeight(int height) {
		this.height = height;
	}

	public IslandPosition[] getIslandPositions() {
		return islandPositions;
	}

	public void setIslandPositions(IslandPosition[] islandPositions) {
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

	public int getSonarRange() {
		return sonarRange;
	}

	public void setSonarRange(int sonarRange) {
		this.sonarRange = sonarRange;
	}

	public int getExtendedSonarRange() {
		return extendedSonarRange;
	}

	public void setExtendedSonarRange(int extendedSonarRange) {
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

	public int getTorpedoSpeed() {
		return torpedoSpeed;
	}

	public void setTorpedoSpeed(int torpedoSpeed) {
		this.torpedoSpeed = torpedoSpeed;
	}

	public int getTorpedoExplosionRadius() {
		return torpedoExplosionRadius;
	}

	public void setTorpedoExplosionRadius(int torpedoExplosionRadius) {
		this.torpedoExplosionRadius = torpedoExplosionRadius;
	}

	public int getRoundLength() {
		return roundLength;
	}

	public void setRoundLength(int roundLength) {
		this.roundLength = roundLength;
	}

	public int getIslandSize() {
		return islandSize;
	}

	public void setIslandSize(int islandSize) {
		this.islandSize = islandSize;
	}

	public int getSubmarineSize() {
		return submarineSize;
	}

	public void setSubmarineSize(int submarineSize) {
		this.submarineSize = submarineSize;
	}

	public int getRounds() {
		return rounds;
	}

	public void setRounds(int rounds) {
		this.rounds = rounds;
	}

	public int getMaxSteeringPerRound() {
		return maxSteeringPerRound;
	}

	public void setMaxSteeringPerRound(int maxSteeringPerRound) {
		this.maxSteeringPerRound = maxSteeringPerRound;
	}

	public int getMaxAccelerationPerRound() {
		return maxAccelerationPerRound;
	}

	public void setMaxAccelerationPerRound(int maxAccelerationPerRound) {
		this.maxAccelerationPerRound = maxAccelerationPerRound;
	}

	public int getMaxSpeed() {
		return maxSpeed;
	}

	public void setMaxSpeed(int maxSpeed) {
		this.maxSpeed = maxSpeed;
	}

	public int getTorpedoRange() {
		return torpedoRange;
	}

	public void setTorpedoRange(int torpedoRange) {
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
		final int prime = 11321;
		int result = 1;
		result = prime * result + extendedSonarCooldown;
		result = prime * result + extendedSonarRange;
		result = prime * result + extendedSonarRounds;
		result = prime * result + height;
		result = prime * result + Arrays.hashCode(islandPositions);
		result = prime * result + islandSize;
		result = prime * result + maxAccelerationPerRound;
		result = prime * result + maxSpeed;
		result = prime * result + maxSteeringPerRound;
		result = prime * result + rateLimitedPenalty;
		result = prime * result + roundLength;
		result = prime * result + rounds;
		result = prime * result + sonarRange;
		result = prime * result + submarineSize;
		result = prime * result + submarinesPerTeam;
		result = prime * result + teamCount;
		result = prime * result + torpedoCooldown;
		result = prime * result + torpedoDamage;
		result = prime * result + torpedoDestroyScore;
		result = prime * result + torpedoExplosionRadius;
		result = prime * result + torpedoHitPenalty;
		result = prime * result + torpedoHitScore;
		result = prime * result + torpedoRange;
		result = prime * result + torpedoSpeed;
		result = prime * result + width;
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
		if (extendedSonarRange != other.extendedSonarRange) {
			return false;
		}
		if (extendedSonarRounds != other.extendedSonarRounds) {
			return false;
		}
		if (height != other.height) {
			return false;
		}
		if (!Arrays.equals(islandPositions, other.islandPositions)) {
			return false;
		}
		if (islandSize != other.islandSize) {
			return false;
		}
		if (maxAccelerationPerRound != other.maxAccelerationPerRound) {
			return false;
		}
		if (maxSpeed != other.maxSpeed) {
			return false;
		}
		if (maxSteeringPerRound != other.maxSteeringPerRound) {
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
		if (sonarRange != other.sonarRange) {
			return false;
		}
		if (submarineSize != other.submarineSize) {
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
		if (torpedoExplosionRadius != other.torpedoExplosionRadius) {
			return false;
		}
		if (torpedoHitPenalty != other.torpedoHitPenalty) {
			return false;
		}
		if (torpedoHitScore != other.torpedoHitScore) {
			return false;
		}
		if (torpedoRange != other.torpedoRange) {
			return false;
		}
		if (torpedoSpeed != other.torpedoSpeed) {
			return false;
		}
		if (width != other.width) {
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
