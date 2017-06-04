package lu.kremi151.minamod.capabilities.stats.types;

import lu.kremi151.minamod.capabilities.stats.Stat;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;

public class StatTypeDataManager extends StatType<EntityPlayer>{

	@Override
	public Stat buildStat(EntityPlayer entity) {
		return this.createDefaultStat(entity);
	}

}
