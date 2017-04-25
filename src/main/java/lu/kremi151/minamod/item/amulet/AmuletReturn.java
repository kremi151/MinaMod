package lu.kremi151.minamod.item.amulet;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumHand;
import net.minecraft.util.SoundCategory;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class AmuletReturn extends AmuletBase{

	@Override
	public boolean onUse(World world, EntityPlayer player, AmuletStack stack) {
		if(hasLocationSaved(stack)){
			if(tryTeleportToLocation(player, stack)){
				player.world.playSound((EntityPlayer)null, player.prevPosX, player.prevPosY, player.prevPosZ, SoundEvents.ENTITY_ENDERMEN_TELEPORT, SoundCategory.PLAYERS, 1.0F, 1.0F);
	            world.playSound((EntityPlayer)null, player.posX, player.posY, player.posZ, SoundEvents.ENTITY_ENDERMEN_TELEPORT, SoundCategory.PLAYERS, 1.0F, 1.0F);
			}else{
	            world.playSound((EntityPlayer)null, player.posX, player.posY, player.posZ, SoundEvents.BLOCK_NOTE_BASS, SoundCategory.PLAYERS, 1.0F, 1.0F);
			}
		}else{
			storeLocation(player, stack);
            world.playSound((EntityPlayer)null, player.posX, player.posY, player.posZ, SoundEvents.BLOCK_NOTE_PLING, SoundCategory.PLAYERS, 1.0F, 1.0F);
		}
		return true;
	}
	
	@Override
	public long getUseCooldown(){
		return 500L;
	}
	
	private void storeLocation(EntityPlayer player, AmuletStack stack){
		NBTTagCompound loc = new NBTTagCompound();
		loc.setDouble("x", player.posX);
		loc.setDouble("y", player.posY);
		loc.setDouble("z", player.posZ);
		loc.setString("world", player.world.getWorldInfo().getWorldName());
		stack.getData().setTag("loc", loc);
	}
	
	private boolean tryTeleportToLocation(EntityPlayer player, AmuletStack stack){
		if(stack.getData().hasKey("loc", 10)){
			NBTTagCompound loc = stack.getData().getCompoundTag("loc");
			String worldName = loc.getString("world");
			if(player.world.getWorldInfo().getWorldName().equals(worldName)){
				player.setPositionAndUpdate(loc.getDouble("x"), loc.getDouble("y"), loc.getDouble("z"));
				stack.getData().removeTag("loc");
				return true;
			}
		}
		return false;
	}
	
	private boolean hasLocationSaved(AmuletStack stack){
		return hasLocationSaved(stack.getData());
	}
	
	private boolean hasLocationSaved(NBTTagCompound data){
		return data.hasKey("loc", 10);
	}
	
	@SideOnly(Side.CLIENT)
	@Override
    public boolean hasEffect(NBTTagCompound data){
		return hasLocationSaved(data);
	}

	@Override
	public String getUnlocalizedName() {
		return "return";
	}

}
