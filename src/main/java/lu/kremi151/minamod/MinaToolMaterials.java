package lu.kremi151.minamod;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.util.EnumHelper;

public class MinaToolMaterials {

	public static final Item.ToolMaterial CITRIN = EnumHelper.addToolMaterial(MinaMod.MODID + ":citrin", 4, 1804,
			8.5F, 4.5F, 10);
	public static final Item.ToolMaterial BAMBUS = EnumHelper.addToolMaterial(MinaMod.MODID + ":bambus", 1, 4, 1F,
			6.0F, 25);
	public static final Item.ToolMaterial OBSIDIAN = EnumHelper.addToolMaterial(MinaMod.MODID + ":obsidian", 3,
			2214, 8.5F, 3.5F, 8);
	public static final Item.ToolMaterial ICE = EnumHelper.addToolMaterial(MinaMod.MODID + ":ice", 1, 5, 1F,
			40.0F, 1);
	public static final Item.ToolMaterial KATANA = EnumHelper.addToolMaterial(MinaMod.MODID + ":katana", 2, 128, 6.0F,
			2.0F, 14);
	
	static{
		CITRIN.setRepairItem(new ItemStack(MinaItems.CITRIN, 1));
		OBSIDIAN.setRepairItem(new ItemStack(MinaItems.OBSIDIAN_FRAGMENT, 1));
	}

}
