package lu.kremi151.minamod.item;

import lu.kremi151.minamod.MinaBlocks;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.MobEffects;
import net.minecraft.item.ItemSeedFood;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ItemStrawberry extends ItemSeedFood{

	public ItemStrawberry() {
		super(1, 0.2f, MinaBlocks.STRAWBERRY_CROP, Blocks.FARMLAND);
		this.setHasSubtypes(true);
		this.setCreativeTab(CreativeTabs.FOOD);
	}

	@Override
	protected void onFoodEaten(ItemStack stack, World worldIn, EntityPlayer player)
    {
        super.onFoodEaten(stack, worldIn, player);
        switch(stack.getMetadata()) {
        case 1://Ruby
        	player.addPotionEffect(new PotionEffect(MobEffects.REGENERATION, 200));
        	break;
        case 2://Sapphire
        	player.addPotionEffect(new PotionEffect(MobEffects.SATURATION, 200));
        	break;
        case 3://Citrin
        	player.addPotionEffect(new PotionEffect(MobEffects.ABSORPTION, 200));
        	break;
        }
        
    }
	
	@SideOnly(Side.CLIENT)
	@Override
    public void getSubItems(CreativeTabs tab, NonNullList<ItemStack> subItems)
    {
        subItems.add(new ItemStack(this, 1, 0));//Default
        subItems.add(new ItemStack(this, 1, 1));//Ruby
        subItems.add(new ItemStack(this, 1, 2));//Sapphire
        subItems.add(new ItemStack(this, 1, 3));//Citrin
    }
	
	@Override
	public String getUnlocalizedName(ItemStack stack)
    {
        switch(stack.getMetadata()) {
        case 1:
        	return super.getUnlocalizedName(stack) + ".ruby";
        case 2:
        	return super.getUnlocalizedName(stack) + ".sapphire";
        case 3:
        	return super.getUnlocalizedName(stack) + ".citrin";
        default:
        	return super.getUnlocalizedName(stack);
        }
    }
	
	@Override
	public EnumActionResult onItemUse(EntityPlayer player, World worldIn, BlockPos pos, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ)
    {
        ItemStack itemstack = player.getHeldItem(hand);
		if(itemstack.getMetadata() == 0) {
			return super.onItemUse(player, worldIn, pos, hand, facing, hitX, hitY, hitZ);
		}else {
			return EnumActionResult.PASS;
		}
    }

}
