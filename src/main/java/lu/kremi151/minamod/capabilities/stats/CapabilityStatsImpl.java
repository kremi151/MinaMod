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
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;

public class CapabilityStatsImpl<E extends EntityLivingBase> implements ICapabilityStats<E>{
	
	private static Map<Class<? extends EntityLivingBase>, DataParameter<Integer>> EFFORT_PARAMETER_MAP = new HashMap<>();
	
	private final HashMap<StatType<E>, Stat> statMap;
	private final Stat.Value effort;
	private final int maxEffortVal = 60;
	private final DataParameter<Integer> effortKey;
	private final E entity;
	
	public CapabilityStatsImpl(E entity, StatType<E> statTypes[]){
		this.entity = entity;
		this.statMap = new HashMap<>();
		for(StatType<E> type : statTypes){
			statMap.put(type, type.buildStat(this, entity));
		}
		if(!EFFORT_PARAMETER_MAP.containsKey(entity.getClass())){
			this.effortKey = entity.getDataManager().createKey(entity.getClass(), DataSerializers.VARINT);
			EFFORT_PARAMETER_MAP.put(entity.getClass(), effortKey);
		}else{
			this.effortKey = EFFORT_PARAMETER_MAP.get(entity.getClass());
		}
		entity.getDataManager().register(effortKey, 0);
		this.effort = new Stat.Value(() -> entity.getDataManager().get(effortKey), value -> {
			int old = entity.getDataManager().get(effortKey);
			entity.getDataManager().set(effortKey, value);
			return old;
		}, 0, 0, maxEffortVal)
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
		reset();
		statMap.forEach((type, stat) -> {
			if(source.supports(type)){
				Stat srcStat = source.getStat(type);
				stat.getActual().set(srcStat.getActual().get());
				stat.getTraining().set(srcStat.getTraining().get());
			}
		});
		effort.set(source.getEffort().get());
	}

	@Override
	public void reset() {
		statMap.forEach((type, stat) -> {
			stat.getActual().setToDefault();
			stat.getTraining().setToDefault();
		});
		effort.setToDefault();
	}

	@Override
	public void initAttributes() {
		statMap.values().forEach(stat -> stat.initialize());
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
			
			for(Map.Entry<StatType<E>, Stat> e : statMap.entrySet()){
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

	@Override
	public int pointsLeft() {
		return Math.max(0, 510 - statMap.values().stream().mapToInt(stat -> stat.getActual().get()).sum());
	}
	
	public static class Storage implements Capability.IStorage<ICapabilityStats>{

		@Override
		public NBTBase writeNBT(Capability<ICapabilityStats> capability, ICapabilityStats instance, EnumFacing side) {
			NBTTagCompound nbt = new NBTTagCompound();
			instance.listSupportedStatTypes().forEach(type -> {
				Stat stat = instance.getStat((StatType) type);
				nbt.setIntArray(((StatType)type).getId(), new int[]{
						stat.getActual().get(),
						stat.getTraining().get()
				});
				
			});
			nbt.setInteger("effort", instance.getEffort().get());
			return nbt;
		}

		@Override
		public void readNBT(Capability<ICapabilityStats> capability, ICapabilityStats instance, EnumFacing side, NBTBase nbt_) {
			if(nbt_ instanceof NBTTagCompound){
				NBTTagCompound nbt = (NBTTagCompound) nbt_;
				
				instance.listSupportedStatTypes().forEach(type -> {
					int array[] = nbt.getIntArray(((StatType)type).getId());
					if(array != null && array.length >= 2){
						Stat stat = instance.getStat((StatType)type);
						stat.getActual().set(array[0]);
						stat.getTraining().set(array[1]);
					}
				});
				
				instance.getEffort().set(nbt.getInteger("effort"));
			}
		}
		
	}

}
