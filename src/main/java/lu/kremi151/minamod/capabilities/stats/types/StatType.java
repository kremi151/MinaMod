package lu.kremi151.minamod.capabilities.stats.types;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.function.BiFunction;
import java.util.function.IntFunction;
import java.util.function.IntSupplier;

import lu.kremi151.minamod.capabilities.stats.ICapabilityStats;
import lu.kremi151.minamod.capabilities.stats.Stat;
import lu.kremi151.minamod.capabilities.stats.datasync.StatData;
import lu.kremi151.minamod.capabilities.stats.datasync.StatDataSerializers;
import lu.kremi151.minamod.util.DualKeyMap;
import lu.kremi151.minamod.util.MinaUtils;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.network.datasync.DataParameter;

public abstract class StatType {
	
	private static final Map<String, StatType> REGISTER = new HashMap<>();
	private static final DualKeyMap<StatType, Class<? extends EntityLivingBase>, DataParameter<StatData>> STAT_PARAMETER_MAP = new DualKeyMap<>();
	
	private final String id;
	private String name;
	private int color = MinaUtils.COLOR_GRAY;
	private float colorF[] = MinaUtils.convertDecimalToRGBFloat(color);

	public abstract <T extends EntityLivingBase> Stat buildStat(ICapabilityStats<T> stats, T entity);
	
	public StatType(String id){
		if(id == null)throw new NullPointerException("No null values as id for stat types are allowed");
		this.id = id;
		this.name = id;
		
		if(!REGISTER.containsKey(id)){
			REGISTER.put(id, this);
		}else{
			throw new RuntimeException("Duplicate id \"" + id + "\" for stat types detected");
		}
	}
	
	public String getId(){
		return id;
	}
	
	public StatType setUnlocalizedName(String name){
		this.name = name;
		return this;
	}
	
	public String getUnlocalizedName(){
		return "minastats." + name;
	}
	
	public StatType setColor(int color){
		this.color = color;
		this.colorF = MinaUtils.convertDecimalToRGBFloat(color);
		return this;
	}
	
	public int getColor(){
		return color;
	}
	
	public float[] getColorF(){
		return colorF;
	}
	
	@Override
	public boolean equals(Object obj){
		if(obj == null){
			return false;
		}else if(obj == this){
			return true;
		}else if(obj.getClass() == this.getClass()){
			return this.id.equals(((StatType)obj).id);
		}else{
			return false;
		}
	}
	
	@Override
	public int hashCode(){
		return id.hashCode();
	}
	
	protected Stat createDefaultStat(EntityLivingBase entity, IntSupplier pointsPool){
		return createDefaultStat(entity, pointsPool, 255);
	}
	
	protected Stat createDefaultStat(EntityLivingBase entity, IntSupplier pointsPool, int maxValue){
		return createDefaultStat(entity, pointsPool, maxValue, (a, t) -> new Stat(a, t));
	}
	
	protected Stat createDefaultStat(EntityLivingBase entity, IntSupplier pointsPool, int maxValue, BiFunction<Stat.Value, Stat.Value, Stat> builder){
		DataParameter<StatData> key_stat;
		if(!STAT_PARAMETER_MAP.containsKey(this, entity.getClass())){
			key_stat = entity.getDataManager().<StatData>createKey(entity.getClass(), StatDataSerializers.STAT_DATA);
			STAT_PARAMETER_MAP.put(this, entity.getClass(), key_stat);
		}else{
			key_stat = STAT_PARAMETER_MAP.get(this, entity.getClass());
		}
		//try{
			entity.getDataManager().register(key_stat, new StatData(128, 0));
		/*	System.out.println("Actual registration succeeded");
		}catch(IllegalArgumentException e){
			if(!e.getMessage().startsWith("Duplicate")){
				throw e;
			}
		}
		try{
			entity.getDataManager().register(key_training, 0);
			System.out.println("Training registration succeeded");
		}catch(IllegalArgumentException e){
			if(!e.getMessage().startsWith("Duplicate")){
				throw e;
			}
		}*/
		final Stat.Value val_actual = new Stat.Value(() -> entity.getDataManager().get(key_stat).getActual(), 
				val -> {
					StatData sd = entity.getDataManager().get(key_stat);
					int old = sd.getActual();
					sd.setActual(val);
					entity.getDataManager().set(key_stat, sd);
					return old;
				}, 128, 64, maxValue, () -> Math.min(maxValue - entity.getDataManager().get(key_stat).getActual(), pointsPool.getAsInt()));
		final Stat.Value val_training = new Stat.Value(() -> entity.getDataManager().get(key_stat).getTraining(), 
				val -> {
					StatData sd = entity.getDataManager().get(key_stat);
					int old = sd.getTraining();
					sd.setTraining(val);
					entity.getDataManager().set(key_stat, sd);
					return old;
				}, 0, 0, maxValue, () -> maxValue - entity.getDataManager().get(key_stat).getTraining() - val_actual.get());
		return builder.apply(val_actual, val_training);
	}
	
	public static Collection<StatType> getRegisteredTypes(){
		return Collections.unmodifiableCollection(REGISTER.values());
	}
	
	public static Optional<StatType> findStatType(String id){
		return Optional.ofNullable(REGISTER.get(id));
	}
	
}
