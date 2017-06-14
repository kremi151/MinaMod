package lu.kremi151.minamod.capabilities.stats.types;

import lu.kremi151.minamod.capabilities.stats.ICapabilityStats;
import lu.kremi151.minamod.capabilities.stats.Stat;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;

public class StatTypeDataManager<T extends EntityLivingBase> extends StatType<T>{

	@Override
	public Stat buildStat(ICapabilityStats<T> stats, T entity) {
		return this.createDefaultStat(entity, stats::pointsLeft);
	}

}
