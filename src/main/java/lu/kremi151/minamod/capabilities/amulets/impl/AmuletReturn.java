package lu.kremi151.minamod.capabilities.amulets.impl;

import lu.kremi151.minamod.capabilities.amulets.IAmulet;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.SoundCategory;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class AmuletReturn implements IAmulet{
	
	private double x, y, z;
	private String world;

	@Override
	public boolean onUse(World world, EntityPlayer player) {
		if(isSet()){
			IAmulet.spawnAmuletAura(player, 0.0f, 0.5f, 0.5f);
			if(tryTeleportToLocation(player)){
				player.world.playSound((EntityPlayer)null, player.prevPosX, player.prevPosY, player.prevPosZ, SoundEvents.ENTITY_ENDERMEN_TELEPORT, SoundCategory.PLAYERS, 1.0F, 1.0F);
	            world.playSound((EntityPlayer)null, player.posX, player.posY, player.posZ, SoundEvents.ENTITY_ENDERMEN_TELEPORT, SoundCategory.PLAYERS, 1.0F, 1.0F);
			}else{
	            world.playSound((EntityPlayer)null, player.posX, player.posY, player.posZ, SoundEvents.BLOCK_NOTE_BASS, SoundCategory.PLAYERS, 1.0F, 1.0F);
			}
		}else{
			this.x = player.posX;
			this.y = player.posY;
			this.z = player.posZ;
			this.world = player.world.getWorldInfo().getWorldName();
			IAmulet.spawnAmuletAura(player, 0.0f, 0.5f, 0.5f);
            world.playSound((EntityPlayer)null, player.posX, player.posY, player.posZ, SoundEvents.BLOCK_NOTE_PLING, SoundCategory.PLAYERS, 1.0F, 1.0F);
		}
		return true;
	}
	
	private boolean isSet() {
		return world != null;
	}
	
	private boolean tryTeleportToLocation(EntityPlayer player){
		if(isSet()){
			if(player.world.getWorldInfo().getWorldName().equals(world)){
				player.setPositionAndUpdate(x, y, z);
				world = null;
				return true;
			}
		}
		return false;
	}
	
	@SideOnly(Side.CLIENT)
	@Override
    public boolean hasEffect(){
		return isSet();
	}

	@Override
	public NBTTagCompound saveData(NBTTagCompound nbt) {
		if(isSet()) {
			NBTTagCompound loc = new NBTTagCompound();
			loc.setString("World", world);
			loc.setDouble("x", x);
			loc.setDouble("y", y);
			loc.setDouble("z", z);
			nbt.setTag("Warp", loc);
		}
		return nbt;
	}
	
	@Override
	public void loadData(NBTTagCompound nbt) {
		if(nbt.hasKey("Warp", 10)) {
			NBTTagCompound loc = nbt.getCompoundTag("Warp");
			this.world = loc.getString("World");
			this.x = loc.getDouble("x");
			this.y = loc.getDouble("y");
			this.z = loc.getDouble("z");
		}
	}

}
