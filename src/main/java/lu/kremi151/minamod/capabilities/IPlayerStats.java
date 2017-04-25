package lu.kremi151.minamod.capabilities;

import java.util.Random;

import lu.kremi151.minamod.enums.EnumPlayerStat;

public interface IPlayerStats {

	int getStats(EnumPlayerStat stat);
	int getTrainingStats(EnumPlayerStat stat);
	int getMaxTrainingStats(EnumPlayerStat stat);
	int getMinTrainingStats(EnumPlayerStat stat);
	int getPointsLeft();
	int getMaxDistributablePoints();
	int getEffortBar();
	int getMaxEffortBar();
	void addEffort(int amount);
	void setEffort(int amount);
	
	void applyTraining(EnumPlayerStat stat, int amount);
	
	int distribute(Random rand, int amount);
	void resetStats();
	void copyFrom(IPlayerStats source);
	
	void initAttributes();
	
}
