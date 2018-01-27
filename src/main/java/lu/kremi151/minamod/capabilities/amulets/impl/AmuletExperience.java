package lu.kremi151.minamod.capabilities.amulets.impl;

import lu.kremi151.minamod.capabilities.amulets.IAmulet;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class AmuletExperience implements IAmulet{
	
	private int experience = 0;

	@Override
	public boolean onUse(World world, EntityPlayer player) {
		if(!world.isRemote){
			if(experience == 0){
				experience = player.experienceTotal;
				player.addExperienceLevel(-(player.experienceLevel + 1));
			}else{
				applyExpToPlayer(player, experience);
				experience = 0;
			}
		}
		return true;
	}
	
	@SideOnly(Side.CLIENT)
	@Override
    public boolean hasEffect(){
		return experience > 0;
	}
	
	@Override
    public NBTTagCompound saveData(NBTTagCompound nbt) {
		nbt.setInteger("Experience", experience);
		return nbt;
	}
	
	@Override
    public void loadData(NBTTagCompound nbt) {
		this.experience = nbt.getInteger("Experience");
	}
	
	private void applyExpToPlayer(EntityPlayer player, int exp){
		player.addExperience(exp);
	}

}
