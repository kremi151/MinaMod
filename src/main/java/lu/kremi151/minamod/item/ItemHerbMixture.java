package lu.kremi151.minamod.item;

import java.util.List;

import lu.kremi151.minamod.capabilities.stats.ICapabilityStats;
import lu.kremi151.minamod.capabilities.stats.types.StatType;
import lu.kremi151.minamod.capabilities.stats.util.Stat;
import net.minecraft.entity.EntityLivingBase;
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
        	ICapabilityStats<EntityPlayer> stats = player.getCapability(ICapabilityStats.CAPABILITY, null);
        	
        	stats.listSupportedStatTypes().forEach(type -> {
        		int amount = nbt.getInteger(type.getId());
        		Stat stat = stats.getStat(type);
        		if(stat.getTraining().remaining() > amount){
        			stat.getTraining().increment(amount);
        		}else{
        			stat.getTraining().increment(stat.getTraining().remaining());
        		}
        	});
        	
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
		
		for(String key : nbt.getKeySet()){
			StatType.findStatType(key).ifPresent(type -> {
				int amnt = nbt.getInteger(type.getId());
				if(amnt != 0)tooltip.add(I18n.translateToLocalFormatted("item.mixture." + type.getUnlocalizedName() + ".info", amnt));
			});
		}
		
		if(!hasEffect(stack, playerIn.getCapability(ICapabilityStats.CAPABILITY, null))){
			tooltip.add("");
			tooltip.add(TextFormatting.RED + I18n.translateToLocal("item.mixture.no_effect.info"));
		}
    }
	
	public static boolean hasEffect(ItemStack mixture, ICapabilityStats<? extends EntityLivingBase> stats){
		NBTTagCompound nbt = mixture.getOrCreateSubCompound("mixture");
		
		int sum = 0;
		
		for(StatType type : stats.listSupportedStatTypes()){
			Stat stat = stats.getStat(type);
			int training = nbt.getInteger(type.getId());
			int nval = stat.getTraining().get() + training;
			if(!(stat.getMinTrainingValue() <= nval && nval <= stat.getMaxTrainingValue())){
				return false;
			}
			sum += training;
		}
		
		return sum <= stats.pointsLeft();
	}

}
