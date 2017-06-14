package lu.kremi151.minamod.capabilities.stats.types;

import java.util.function.BiFunction;
import java.util.function.IntSupplier;

import lu.kremi151.minamod.capabilities.stats.ICapabilityStats;
import lu.kremi151.minamod.capabilities.stats.Stat;
import lu.kremi151.minamod.util.MinaUtils;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;

public abstract class StatType<T extends EntityLivingBase> {
	
	private String name;
	private int color = MinaUtils.COLOR_GRAY;

	public abstract Stat buildStat(ICapabilityStats<T> stats, T entity);
	
	public StatType<T> setUnlocalizedName(String name){
		this.name = name;
		return this;
	}
	
	public String getUnlocalizedName(){
		return "minastats." + name;
	}
	
	public StatType<T> setColor(int color){
		this.color = color;
		return this;
	}
	
	public int getColor(){
		return color;
	}
	
	public static Stat createDefaultStat(EntityLivingBase entity, IntSupplier pointsPool){
		return createDefaultStat(entity, pointsPool, 255);
	}
	
	public static Stat createDefaultStat(EntityLivingBase entity, IntSupplier pointsPool, int maxValue){
		return createDefaultStat(entity, pointsPool, maxValue, (a, t) -> new Stat(a, t));
	}
	
	public static Stat createDefaultStat(EntityLivingBase entity, IntSupplier pointsPool, int maxValue, BiFunction<Stat.Value, Stat.Value, Stat> builder){
		final DataParameter<Integer> key_actual = entity.getDataManager().<Integer>createKey(EntityLivingBase.class, DataSerializers.VARINT);
		final DataParameter<Integer> key_training = entity.getDataManager().<Integer>createKey(EntityLivingBase.class, DataSerializers.VARINT);
		final Stat.Value val_actual = new Stat.Value(() -> entity.getDataManager().get(key_actual), value -> {
			int old = entity.getDataManager().get(key_actual);
			entity.getDataManager().set(key_actual, value);
			return old;
		}, 128, 64, maxValue, () -> Math.min(maxValue - entity.getDataManager().get(key_actual), pointsPool.getAsInt()));
		final Stat.Value val_training = new Stat.Value(() -> entity.getDataManager().get(key_training), value -> {
			int old = entity.getDataManager().get(key_training);
			entity.getDataManager().set(key_training, value);
			return old;
		}, 0, 0, maxValue, () -> maxValue - entity.getDataManager().get(key_training) - val_actual.get());
		return builder.apply(val_actual, val_training);
	}
	
}
