package lu.kremi151.minamod.item.amulet;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.SoundCategory;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class AmuletReturn extends AmuletBase{

	@Override
	public boolean onUse(World world, EntityPlayer player, ItemStack stack) {
		if(hasLocationSaved(stack)){
			spawnAmuletAura(player, 0.0f, 0.5f, 0.5f);
			if(tryTeleportToLocation(player, stack)){
				player.world.playSound((EntityPlayer)null, player.prevPosX, player.prevPosY, player.prevPosZ, SoundEvents.ENTITY_ENDERMEN_TELEPORT, SoundCategory.PLAYERS, 1.0F, 1.0F);
	            world.playSound((EntityPlayer)null, player.posX, player.posY, player.posZ, SoundEvents.ENTITY_ENDERMEN_TELEPORT, SoundCategory.PLAYERS, 1.0F, 1.0F);
			}else{
	            world.playSound((EntityPlayer)null, player.posX, player.posY, player.posZ, SoundEvents.BLOCK_NOTE_BASS, SoundCategory.PLAYERS, 1.0F, 1.0F);
			}
		}else{
			storeLocation(player, stack);
			spawnAmuletAura(player, 0.0f, 0.5f, 0.5f);
            world.playSound((EntityPlayer)null, player.posX, player.posY, player.posZ, SoundEvents.BLOCK_NOTE_PLING, SoundCategory.PLAYERS, 1.0F, 1.0F);
		}
		return true;
	}
	
	@Override
	public long getUseCooldown(){
		return 500L;
	}
	
	private void storeLocation(EntityPlayer player, ItemStack stack){
		NBTTagCompound loc = new NBTTagCompound();
		loc.setDouble("x", player.posX);
		loc.setDouble("y", player.posY);
		loc.setDouble("z", player.posZ);
		loc.setString("world", player.world.getWorldInfo().getWorldName());
		stack.getOrCreateSubCompound("warpData").setTag("loc", loc);
	}
	
	private boolean tryTeleportToLocation(EntityPlayer player, ItemStack stack){
		NBTTagCompound data = stack.getOrCreateSubCompound("warpData");
		if(data.hasKey("loc", 10)){
			NBTTagCompound loc = data.getCompoundTag("loc");
			String worldName = loc.getString("world");
			if(player.world.getWorldInfo().getWorldName().equals(worldName)){
				player.setPositionAndUpdate(loc.getDouble("x"), loc.getDouble("y"), loc.getDouble("z"));
				data.removeTag("loc");
				return true;
			}
		}
		return false;
	}
	
	private boolean hasLocationSaved(ItemStack stack){
		return hasLocationSaved(stack.getOrCreateSubCompound("warpData"));
	}
	
	private boolean hasLocationSaved(NBTTagCompound data){
		return data.hasKey("loc", 10);
	}
	
	@SideOnly(Side.CLIENT)
	@Override
    public boolean hasEffect(NBTTagCompound data){
		return hasLocationSaved(data);
	}

}
