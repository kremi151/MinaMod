package lu.kremi151.minamod.capabilities.stats.types;

import lu.kremi151.minamod.capabilities.stats.ICapabilityStats;
import lu.kremi151.minamod.capabilities.stats.Stat;
import net.minecraft.entity.EntityLivingBase;

public class StatTypeDataManager extends StatType{

	public StatTypeDataManager(String id) {
		super(id);
	}

	@Override
	public <T extends EntityLivingBase> Stat buildStat(ICapabilityStats<T> stats, T entity) {
		return this.createDefaultStat(entity, stats::pointsLeft);
	}

}
