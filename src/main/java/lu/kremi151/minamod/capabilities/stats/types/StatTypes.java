package lu.kremi151.minamod.capabilities.stats.types;

import lu.kremi151.minamod.util.MinaUtils;
import net.minecraft.entity.EntityLivingBase;

public class StatTypes {

	public static final StatType ATTACK = new StatTypeDataManager("atk").setUnlocalizedName("attack").setColor(MinaUtils.convertRGBToDecimal(255, 128, 128));
	public static final StatType DEFENSE = new StatTypeDataManager("def").setUnlocalizedName("defense").setColor(MinaUtils.convertRGBToDecimal(128, 255, 128));
	public static final StatType SPEED = new StatTypeSpeed("spd").setUnlocalizedName("speed").setColor(MinaUtils.convertRGBToDecimal(128, 128, 255));
}
