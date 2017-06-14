package lu.kremi151.minamod.capabilities.stats;

import java.util.Collection;
import java.util.Random;

import lu.kremi151.minamod.capabilities.stats.types.StatType;
import net.minecraft.entity.EntityLivingBase;

public interface ICapabilityStats<E extends EntityLivingBase> {
	
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
