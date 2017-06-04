package lu.kremi151.minamod.capabilities.stats;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import lu.kremi151.minamod.MinaMod;
import lu.kremi151.minamod.capabilities.stats.types.StatType;
import lu.kremi151.minamod.packet.message.MessageShowOverlay;
import lu.kremi151.minamod.util.BlissHelper;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;

public class CapabilityStatsImpl<E extends EntityLivingBase> implements ICapabilityStats<E>{
	
	private final HashMap<StatType, Stat> statMap;
	private final Stat.Value effort;
	private final int maxEffortVal = 60;
	private final DataParameter<Integer> effortKey;
	private final E entity;
	
	public CapabilityStatsImpl(E entity, StatType statTypes[]){
		this.entity = entity;
		this.statMap = new HashMap<>();
		for(StatType type : statTypes){
			statMap.put(type, type.buildStat(entity));
		}
		this.effortKey = entity.getDataManager().createKey(EntityLivingBase.class, DataSerializers.VARINT);
		this.effort = new Stat.Value(() -> entity.getDataManager().get(effortKey), value -> entity.getDataManager().set(effortKey, value), maxEffortVal)
				.setListener(newValue -> {
					int exp = Math.max(0, newValue);
					boolean changed = false;
					while(exp > maxEffortVal){
						exp -= maxEffortVal;
						changed = true;
						onLevelUp();
					}
					if(changed)getEffort().set(exp);
				});
	}

	@Override
	public Collection<StatType> listSupportedStatTypes() {
		return Collections.unmodifiableSet(statMap.keySet());
	}
	
	@Override
	public boolean supports(StatType type){
		return statMap.containsKey(type);
	}

	@Override
	public Stat getStat(StatType type) {
		return statMap.get(type);
	}

	@Override
	public Stat.Value getEffort(){
		return effort;
	}

	@Override
	public void applyFrom(ICapabilityStats<E> source) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void reset() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void initAttributes() {
		// TODO Auto-generated method stub
		
	}
	
	private void onLevelUp(){
		final int points = BlissHelper.getBliss(entity).chanceOneIn(10) ? 5 : 3;
		if(distribute(entity.getRNG(), points) > 0 && entity instanceof EntityPlayerMP){
			MinaMod.getMinaMod().getPacketDispatcher().sendTo(new MessageShowOverlay(0, 1000), (EntityPlayerMP) entity);
		}
	}

	@Override
	public int distribute(Random rand, int amount) {
		ArrayList<Stat> avd = new ArrayList<>();
		int res = 0;
		for(int i = 0 ; i < amount ; i++){
			avd.clear();
			
			for(Map.Entry<StatType, Stat> e : statMap.entrySet()){
				if(e.getValue().getTraining().get() > 0){
					avd.add(e.getValue());
				}
			}
			
			final int size = avd.size();
			if(size == 0){
				return res;
			}else{
				Stat stat = avd.get(rand.nextInt(size));
				int val = stat.getTraining().get();
				if(val > 0){
					stat.getActual().increment(1);
					stat.getTraining().decrement(1);
					res++;
				}else if(val < 0){
					stat.getActual().decrement(1);
					stat.getTraining().increment(1);
					res++;
				}
			}
		}
		return res;
	}

}
