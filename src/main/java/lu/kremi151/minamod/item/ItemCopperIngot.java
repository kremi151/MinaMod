package lu.kremi151.minamod.item;

import lu.kremi151.minamod.MinaMod;
import net.minecraft.client.Minecraft;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;

public class ItemCopperIngot extends Item {
	public ItemCopperIngot() {
		super();
		String name = "copper_ingot";
		setMaxStackSize(64);
		setUnlocalizedName(name);
		setCreativeTab(CreativeTabs.MATERIALS);
	}
}
