package lu.kremi151.minamod.capabilities.stats.types;

import net.minecraft.entity.EntityLivingBase;

public class StatTypes {

	public static final StatType<? extends EntityLivingBase> ATTACK = new StatTypeDataManager("atk");
	public static final StatType<? extends EntityLivingBase> DEFENSE = new StatTypeDataManager("def");
	public static final StatType<? extends EntityLivingBase> SPEED = new StatTypeSpeed("spd");
}
