package lu.kremi151.minamod.capabilities.stats;

import java.util.Optional;
import java.util.Random;

import lu.kremi151.minamod.capabilities.stats.ICapabilityStats.Key;
import lu.kremi151.minamod.capabilities.stats.types.StatType;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;

public class CapabilityStatsPlayerImpl extends CapabilityStatsImpl<EntityPlayer>{
	
	public static final ICapabilityStats.Key<Integer> DISTRIBUTION_MULTIPLICATOR = new ICapabilityStats.Key<>("distr_multi", Integer.class);

	private static final DataParameter<Integer> distrMultiplicatorKey = EntityDataManager.createKey(EntityPlayer.class, DataSerializers.VARINT);
	
	public CapabilityStatsPlayerImpl(EntityPlayer entity, StatType[] statTypes) {
		super(entity, statTypes);
		entity.getDataManager().register(distrMultiplicatorKey, 1);
	}
	
	@Override
	public int distribute(Random rand, int amount) {
		int multi = Math.max(entity.getDataManager().get(distrMultiplicatorKey), 1);
		if(multi > 1 && rand.nextFloat() < 0.3f){
			entity.getDataManager().set(distrMultiplicatorKey, multi - 1);
		}
		return super.distribute(rand, amount * multi);
	}
	
	@Override
	public <T> void offer(Key<T> key, T value){
		if(key.equals(DISTRIBUTION_MULTIPLICATOR)){
			entity.getDataManager().set(distrMultiplicatorKey, Math.max((Integer)value, 1));
		}
	}
	
	@Override
	public <T> Optional<T> query(Key<T> key){
		if(key.equals(DISTRIBUTION_MULTIPLICATOR)){
			return (Optional<T>) Optional.of(Math.max(entity.getDataManager().get(distrMultiplicatorKey), 1));
		}else{
			return Optional.empty();
		}
	}

}
