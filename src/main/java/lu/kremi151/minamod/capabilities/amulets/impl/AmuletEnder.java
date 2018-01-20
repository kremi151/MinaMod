package lu.kremi151.minamod.capabilities.amulets.impl;

import net.minecraft.entity.monster.EntityEnderman;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.util.SoundCategory;
import net.minecraft.world.World;

public class AmuletEnder extends AmuletConsumable{

	public AmuletEnder(ItemStack itemStack) {
		super(itemStack, 8);
	}

	@Override
	public boolean onUse(World world, EntityPlayer player) {
		if(!world.isRemote && enderTeleport(player)){
			return super.onUse(world, player);
		}
		return true;
	}
	
	private boolean enderTeleport(EntityPlayer player){
		double prevX = player.posX;
		double prevY = player.posY;
		double prevZ = player.posZ;
		double x = player.posX + (player.getRNG().nextDouble() - 0.5D) * 64.0D;
        double y = player.posY + (double)(player.getRNG().nextInt(64) - 32);
        double z = player.posZ + (player.getRNG().nextDouble() - 0.5D) * 64.0D;
        
        boolean flag = player.attemptTeleport(x, y, z);
        if(flag){
        	player.world.playSound((EntityPlayer)null, player.prevPosX, player.prevPosY, player.prevPosZ, SoundEvents.ENTITY_ENDERMEN_TELEPORT, SoundCategory.PLAYERS, 1.0F, 1.0F);
            player.playSound(SoundEvents.ENTITY_ENDERMEN_TELEPORT, 1.0F, 1.0F);
            
            if(player.getRNG().nextInt(4) == 0) {
            	EntityEnderman enderman = new EntityEnderman(player.world);
            	enderman.setPosition(prevX, prevY, prevZ);
            	player.world.spawnEntity(enderman);
            }
            return true;
        }
        return false;
	}

}
