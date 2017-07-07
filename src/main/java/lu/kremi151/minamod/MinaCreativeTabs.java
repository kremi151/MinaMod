package lu.kremi151.minamod;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemStack;

public class MinaCreativeTabs {

	public static final CreativeTabs FURNISHING = new CreativeTabs("furnishing") {

		@Override
		public ItemStack getTabIconItem() {
			return new ItemStack(MinaBlocks.OAK_TABLE, 1);
		}

	};
	public static final CreativeTabs TECHNOLOGY = new CreativeTabs("technology") {

		@Override
		public ItemStack getTabIconItem() {
			return new ItemStack(MinaItems.CHIP, 1);
		}

	};
	public static final CreativeTabs MIXTURES = new CreativeTabs("mixtures") {

		@Override
		public ItemStack getTabIconItem() {
			return new ItemStack(MinaItems.HERB);
		}

	};

}
