package lu.kremi151.minamod.capabilities.stats;

import java.util.Collection;
import java.util.Optional;
import java.util.Random;

import lu.kremi151.minamod.capabilities.stats.types.StatType;
import lu.kremi151.minamod.capabilities.stats.util.Stat;
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
	
	default <T> void offer(Key<T> key, T value){}
	
	default <T> Optional<T> query(Key<T> key){
		return Optional.empty();
	}
	
	public static class Key<T>{
		private final String key;
		private final Class<T> valueClazz;
		
		public Key(String key, Class<T> valueClazz){
			this.key = key;
			this.valueClazz = valueClazz;
		}
		
		public String getKey(){
			return key;
		}
		
		@Override
		public boolean equals(Object obj){
			if(obj != null){
				if(obj == this){
					return true;
				}else if(obj instanceof Key){
					return ((Key)obj).valueClazz == this.valueClazz && ((Key)obj).key.equals(this.key);
				}
			}
			return false;
		}
		
		@Override
		public int hashCode(){
			return valueClazz.hashCode() ^ key.hashCode();
		}
	}

}
