package lu.kremi151.minamod.item;

import java.util.List;

import lu.kremi151.minamod.capabilities.CapabilityPlayerStats;
import lu.kremi151.minamod.capabilities.IPlayerStats;
import lu.kremi151.minamod.enums.EnumPlayerStat;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.EnumAction;
import net.minecraft.item.ItemFood;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.translation.I18n;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ItemHerbMixture extends ItemFood{

	public ItemHerbMixture() {
		super(0, false);
		this.setContainerItem(Items.GLASS_BOTTLE);
		this.setAlwaysEdible();
	}
	
	@Override
	protected void onFoodEaten(ItemStack stack, World worldIn, EntityPlayer player)
    {
		super.onFoodEaten(stack, worldIn, player);
        if (!worldIn.isRemote)
        {
    		NBTTagCompound nbt = stack.getOrCreateSubCompound("mixture");
        	IPlayerStats stats = player.getCapability(CapabilityPlayerStats.CAPABILITY, null);
        	
        	for(EnumPlayerStat stat : EnumPlayerStat.values()){
            	stats.applyTraining(stat, nbt.getInteger(stat.getId()));
        	}
        	
    		player.inventory.addItemStackToInventory(new ItemStack(Items.GLASS_BOTTLE,1));
        }
    }
	
	@Override
	public EnumAction getItemUseAction(ItemStack par1ItemStack)
    {
        return EnumAction.DRINK;
    }
	
	@SideOnly(Side.CLIENT)
    public void addInformation(ItemStack stack, EntityPlayer playerIn, List<String> tooltip, boolean advanced)
    {
		NBTTagCompound nbt = stack.getOrCreateSubCompound("mixture");
		tooltip.add(I18n.translateToLocalFormatted("item.mixture.attack.info", nbt.getInteger("attack")));
		tooltip.add(I18n.translateToLocalFormatted("item.mixture.defense.info", nbt.getInteger("defense")));
		tooltip.add(I18n.translateToLocalFormatted("item.mixture.speed.info", nbt.getInteger("speed")));
		if(!hasEffect(stack, playerIn.getCapability(CapabilityPlayerStats.CAPABILITY, null))){
			tooltip.add("");
			tooltip.add(TextFormatting.RED + I18n.translateToLocal("item.mixture.no_effect.info"));
		}
    }
	
	public static boolean hasEffect(ItemStack mixture, IPlayerStats stats){
		NBTTagCompound nbt = mixture.getOrCreateSubCompound("mixture");
		
		int sum = 0;
		
		for(EnumPlayerStat stat : EnumPlayerStat.values()){
			int training = nbt.getInteger(stat.getId());
			int nval = stats.getTrainingStats(stat) + training;
			if(!(stats.getMinTrainingStats(stat) <= nval && nval <= stats.getMaxTrainingStats(stat))){
				return false;
			}
			sum += training;
		}
		
		return sum <= stats.getPointsLeft();
	}

}
