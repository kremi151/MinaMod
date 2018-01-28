package lu.kremi151.minamod.item;

import lu.kremi151.minamod.MinaPotions;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.MobEffects;
import net.minecraft.item.ItemFood;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.NonNullList;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ItemBerry extends ItemFood{
	
	public static final String VARIANT_NAMES[] = new String[]{"doge_berry", "kevikus", "tracius"};
	
	public ItemBerry(int amount, float saturation, boolean isWolfFood) {
		super(amount, saturation, isWolfFood);
		this.setHasSubtypes(true);
		this.setAlwaysEdible();
	}
	
	@Override
	public String getUnlocalizedName(ItemStack stack)
    {
        return super.getUnlocalizedName() + "." + VARIANT_NAMES[stack.getMetadata()];
    }

	@Override
	protected void onFoodEaten(ItemStack stack, World worldIn, EntityPlayer player)
    {
		super.onFoodEaten(stack, worldIn, player);
		if(!worldIn.isRemote){
			switch(stack.getMetadata()){
			case 0:
				player.addPotionEffect(new PotionEffect(MinaPotions.DOGE, 1200));
				player.addPotionEffect(new PotionEffect(MobEffects.LUCK, 100));
				break;
			case 1:
				player.addPotionEffect(new PotionEffect(MobEffects.BLINDNESS, 1200));
				player.addPotionEffect(new PotionEffect(MobEffects.SLOWNESS, 1200));
				player.addPotionEffect(new PotionEffect(MobEffects.MINING_FATIGUE, 1200));
				player.addPotionEffect(new PotionEffect(MobEffects.NAUSEA, 1200));
				player.addPotionEffect(new PotionEffect(MobEffects.HUNGER, 1200));
				break;
			case 2:
				player.addPotionEffect(new PotionEffect(MobEffects.POISON, 1200));
				break;
			}
		}
    }
	
	@Override
	@SideOnly(Side.CLIENT)
    public void getSubItems(CreativeTabs tab, NonNullList<ItemStack> subItems)
    {
        if(isInCreativeTab(tab)) {
        	for(int i = 0 ; i < VARIANT_NAMES.length ; i++){
            	subItems.add(new ItemStack(this, 1, i));
            }
        }
    }

}
