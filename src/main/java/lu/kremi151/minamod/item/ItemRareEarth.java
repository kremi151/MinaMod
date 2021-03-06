package lu.kremi151.minamod.item;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ItemRareEarth extends Item {
	
	public static final String VARIANT_NAMES[] = new String[] {"rare_earth_raw", "rare_earth"};

	public ItemRareEarth() {
		super();
		this.setHasSubtypes(true);
		this.setCreativeTab(CreativeTabs.MATERIALS);
	}

	@SideOnly(Side.CLIENT)
	@Override
	public void getSubItems(Item itemIn, CreativeTabs tab, NonNullList<ItemStack> subItems) {
		subItems.add(new ItemStack(itemIn, 1, 0));
		subItems.add(new ItemStack(itemIn, 1, 1));
	}
	
	@Override
	public String getUnlocalizedName(ItemStack stack)
    {
        return stack.getMetadata() == 1 ? super.getUnlocalizedName(stack) : super.getUnlocalizedName(stack) + ".raw";
    }
	
	
}
