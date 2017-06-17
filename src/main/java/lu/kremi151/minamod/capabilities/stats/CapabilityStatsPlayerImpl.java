package lu.kremi151.minamod.capabilities.stats;

import java.util.Optional;

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
	}
	
	/*@Override
	public <T> void offer(Key<T> key, T value){
		if(key.equals(DISTRIBUTION_MULTIPLICATOR)){
			
		}
	}
	
	@Override
	public <T> Optional<T> query(Key<T> key){
		
	}*/

}
