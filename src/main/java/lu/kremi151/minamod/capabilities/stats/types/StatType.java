package lu.kremi151.minamod.capabilities.stats.types;

import lu.kremi151.minamod.capabilities.stats.Stat;
import lu.kremi151.minamod.util.MinaUtils;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;

public abstract class StatType<T extends EntityLivingBase> {
	
	private String name;
	private int color = MinaUtils.COLOR_GRAY;

	public abstract Stat buildStat(T entity);
	
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
	
	public static Stat createDefaultStat(EntityLivingBase entity){
		return createDefaultStat(entity, 255);
	}
	
	public static Stat createDefaultStat(EntityLivingBase entity, int maxValue){
		final DataParameter<Integer> key_actual = entity.getDataManager().<Integer>createKey(EntityLivingBase.class, DataSerializers.VARINT);
		final DataParameter<Integer> key_training = entity.getDataManager().<Integer>createKey(EntityLivingBase.class, DataSerializers.VARINT);
		final Stat.Value val_actual = new Stat.Value(() -> entity.getDataManager().get(key_actual), value -> entity.getDataManager().set(key_actual, value), maxValue);
		final Stat.Value val_training = new Stat.Value(() -> entity.getDataManager().get(key_training), value -> entity.getDataManager().set(key_training, value), maxValue, () -> maxValue - entity.getDataManager().get(key_training) - val_actual.get());
		return new Stat(val_actual, val_training);
	}
	
}
