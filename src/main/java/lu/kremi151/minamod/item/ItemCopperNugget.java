package lu.kremi151.minamod.item;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;

public class ItemCopperNugget extends Item{
	public ItemCopperNugget() {
		super();
		String name = "copper_nugget";
		setMaxStackSize(64);
		setUnlocalizedName(name);
		setCreativeTab(CreativeTabs.MATERIALS);
		}

}
