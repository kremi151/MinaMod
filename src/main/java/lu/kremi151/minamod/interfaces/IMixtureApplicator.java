package lu.kremi151.minamod.interfaces;

import lu.kremi151.minamod.capabilities.stats.types.StatType;

public interface IMixtureApplicator {

	int getStatEffect(StatType type);
	StatType [] getSupportedStats();
	
}
