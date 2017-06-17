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
import lu.kremi151.minamod.util.DualKeyMap;
import lu.kremi151.minamod.util.MinaUtils;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;

public abstract class StatType {
	
	private static final Map<String, StatType> REGISTER = new HashMap<>();
	private static final DualKeyMap<StatType, Class<? extends EntityLivingBase>, DataParameter<Integer>> ACTUAL_PARAMETER_MAP = new DualKeyMap<>();
	private static final DualKeyMap<StatType, Class<? extends EntityLivingBase>, DataParameter<Integer>> TRAINING_PARAMETER_MAP = new DualKeyMap<>();
	
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
		DataParameter<Integer> key_actual, key_training;
		if(!ACTUAL_PARAMETER_MAP.containsKey(this, entity.getClass())){
			key_actual = entity.getDataManager().<Integer>createKey(entity.getClass(), DataSerializers.VARINT);
			ACTUAL_PARAMETER_MAP.put(this, entity.getClass(), key_actual);
		}else{
			key_actual = ACTUAL_PARAMETER_MAP.get(this, entity.getClass());
		}
		if(!TRAINING_PARAMETER_MAP.containsKey(this, entity.getClass())){
			key_training = entity.getDataManager().<Integer>createKey(entity.getClass(), DataSerializers.VARINT);
			TRAINING_PARAMETER_MAP.put(this, entity.getClass(), key_training);
		}else{
			key_training = TRAINING_PARAMETER_MAP.get(this, entity.getClass());
		}
		//try{
			entity.getDataManager().register(key_actual, 128);
		/*	System.out.println("Actual registration succeeded");
		}catch(IllegalArgumentException e){
			if(!e.getMessage().startsWith("Duplicate")){
				throw e;
			}
		}
		try{*/
			entity.getDataManager().register(key_training, 0);
		/*	System.out.println("Training registration succeeded");
		}catch(IllegalArgumentException e){
			if(!e.getMessage().startsWith("Duplicate")){
				throw e;
			}
		}*/
		final Stat.Value val_actual = new Stat.Value(new DataParameterSupplier(entity, key_actual), 
				new DataParameterFunction(entity, key_actual), 128, 64, maxValue, () -> Math.min(maxValue - entity.getDataManager().get(key_actual), pointsPool.getAsInt()));
		final Stat.Value val_training = new Stat.Value(new DataParameterSupplier(entity, key_training), 
				new DataParameterFunction(entity, key_training), 0, 0, maxValue, () -> maxValue - entity.getDataManager().get(key_training) - val_actual.get());
		return builder.apply(val_actual, val_training);
	}
	
	public static Collection<StatType> getRegisteredTypes(){
		return Collections.unmodifiableCollection(REGISTER.values());
	}
	
	public static Optional<StatType> findStatType(String id){
		return Optional.ofNullable(REGISTER.get(id));
	}
	
	private static final class DataParameterSupplier implements IntSupplier{
		
		private final EntityLivingBase entity;
		private final DataParameter<Integer> dataParameter;
		
		private DataParameterSupplier(EntityLivingBase entity, DataParameter<Integer> dataParameter){
			this.entity = entity;
			this.dataParameter = dataParameter;
		}

		@Override
		public int getAsInt() {
			return entity.getDataManager().get(dataParameter);
		}
		
	}
	
	private static final class DataParameterFunction implements IntFunction<Integer>{
		
		private final EntityLivingBase entity;
		private final DataParameter<Integer> dataParameter;
		
		private DataParameterFunction(EntityLivingBase entity, DataParameter<Integer> dataParameter){
			this.entity = entity;
			this.dataParameter = dataParameter;
		}

		@Override
		public Integer apply(int arg0) {
			int old = entity.getDataManager().get(dataParameter);
			entity.getDataManager().set(dataParameter, arg0);
			return old;
		}
		
	}
	
}
