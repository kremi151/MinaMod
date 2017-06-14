package lu.kremi151.minamod.attribute;

import java.util.UUID;
import java.util.function.IntSupplier;

import net.minecraft.entity.ai.attributes.AttributeModifier;

public class AttributeModifierStatSpeed extends AttributeModifier{
	
	private final IntSupplier speedGetter;

	public AttributeModifierStatSpeed(UUID idIn, IntSupplier speedGetter) {
		super(idIn, "mina_stat_speed", 1d, 2);//Y = Y * (1 + Amount)
		setSaved(false);
		this.speedGetter = speedGetter;
	}
	
	@Override
    public double getAmount()
    {
        return ((double)speedGetter.getAsInt() / (double)127.5) - 1.0;
    }

}
