package lu.kremi151.minamod.capabilities.stats;

import java.util.Collection;
import java.util.Random;

import lu.kremi151.minamod.capabilities.stats.types.StatType;
import net.minecraft.entity.EntityLivingBase;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;

public interface ICapabilityStats<E extends EntityLivingBase> {
	
	@CapabilityInject(ICapabilityStats.class)
	public static final Capability<ICapabilityStats> CAPABILITY = null;
	
	Collection<StatType> listSupportedStatTypes();
	Stat getStat(StatType type);
	
	void applyFrom(ICapabilityStats<E> source);
	void reset();
	void initAttributes();
	int distribute(Random rand, int amount);
	int pointsLeft();
	
	default Stat.Value getEffort(){
		return Stat.Value.ZERO;
	}
	
	default boolean supports(StatType type){
		return listSupportedStatTypes().contains(type);
	}

}
