package lu.kremi151.minamod.attribute;

import java.util.UUID;

import lu.kremi151.minamod.capabilities.IPlayerStats;
import lu.kremi151.minamod.enums.EnumPlayerStat;
import net.minecraft.entity.ai.attributes.AttributeModifier;

public class AttributeModifierStatSpeed extends AttributeModifier{
	
	private final IPlayerStats stats;

	public AttributeModifierStatSpeed(UUID idIn, IPlayerStats stats) {
		super(idIn, "mina_stat_speed", 1d, 2);//Y = Y * (1 + Amount)
		setSaved(false);
		this.stats = stats;
	}
	
	@Override
    public double getAmount()
    {
        return ((double)stats.getStats(EnumPlayerStat.SPEED) / (double)127.5) - 1.0;
    }

}
