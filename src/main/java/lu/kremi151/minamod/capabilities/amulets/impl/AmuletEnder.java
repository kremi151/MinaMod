package lu.kremi151.minamod.capabilities.amulets.impl;

import lu.kremi151.minamod.capabilities.amulets.IAmulet;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.util.SoundCategory;
import net.minecraft.world.World;

public class AmuletEnder implements IAmulet{

	@Override
	public boolean onUse(World world, EntityPlayer player) {
		if(!world.isRemote){
			enderTeleport(player);
		}
		return true;
	}
	
	private void enderTeleport(EntityPlayer player){
		double x = player.posX + (player.getRNG().nextDouble() - 0.5D) * 64.0D;
        double y = player.posY + (double)(player.getRNG().nextInt(64) - 32);
        double z = player.posZ + (player.getRNG().nextDouble() - 0.5D) * 64.0D;
        
        boolean flag = player.attemptTeleport(x, y, z);
        if(flag){
        	player.world.playSound((EntityPlayer)null, player.prevPosX, player.prevPosY, player.prevPosZ, SoundEvents.ENTITY_ENDERMEN_TELEPORT, SoundCategory.PLAYERS, 1.0F, 1.0F);
            player.playSound(SoundEvents.ENTITY_ENDERMEN_TELEPORT, 1.0F, 1.0F);
        }
	}

}
