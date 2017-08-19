package lu.kremi151.minamod.potion;

import lu.kremi151.minamod.MinaMod;
import lu.kremi151.minamod.network.MessageAddScreenLayer;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayerMP;

public class PotionDoge extends CustomPotion{
	
	public PotionDoge(boolean isBadEffect, int color) {
		super(isBadEffect, color);
	}

	@Override
	public boolean isReady(int duration, int amplifier)
    {
		return duration % 20 == 0;
    }
	
	@Override
    public void performEffect(EntityLivingBase entityLivingBaseIn, int amplifier)
    {
		if(entityLivingBaseIn instanceof EntityPlayerMP){
			MinaMod.getMinaMod().getPacketDispatcher().sendTo(MessageAddScreenLayer.fromId(MessageAddScreenLayer.DOGE), (EntityPlayerMP)entityLivingBaseIn);
		}
    }
}
