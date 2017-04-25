package lu.kremi151.minamod.item.amulet;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumHand;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class AmuletExperience extends AmuletBase{

	@Override
	public boolean onUse(World world, EntityPlayer player, AmuletStack stack) {
		if(!world.isRemote){
			int exp = getStoredExperience(stack);
			if(exp == 0){
				exp = player.experienceTotal;
				player.removeExperienceLevel(player.experienceLevel + 1);
				storeExperience(stack, exp);
			}else{
				applyExpToPlayer(player, exp);
				storeExperience(stack, 0);
			}
		}
		return true;
	}

	@Override
	public String getUnlocalizedName() {
		return "experience";
	}
	
	@SideOnly(Side.CLIENT)
	@Override
    public boolean hasEffect(NBTTagCompound data){
		return getStoredExperience(data) > 0;
	}
	
	private void applyExpToPlayer(EntityPlayer player, int exp){
		player.addExperience(exp);
	}
	
	private int getStoredExperience(AmuletStack stack){
		return getStoredExperience(stack.getData());
	}
	
	private int getStoredExperience(NBTTagCompound data){
		if(data.hasKey("exp", 3)){
			return data.getInteger("exp");
		}else{
			return 0;
		}
	}
	
	private void storeExperience(AmuletStack stack, int amount){
		stack.getData().setInteger("exp", amount);
	}

}
