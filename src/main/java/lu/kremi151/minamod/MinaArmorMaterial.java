package lu.kremi151.minamod;

import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.util.EnumHelper;

public class MinaArmorMaterial {

	public static final ItemArmor.ArmorMaterial CITRIN = EnumHelper.addArmorMaterial(
			MinaMod.MODID + ":citrin_armor", MinaMod.MODID + ":citrin", 39, new int[] { 3, 10, 8, 4 }, 8, null, 1.5f);// TODO:
	// sound
	public static final ItemArmor.ArmorMaterial OBSIDIAN = EnumHelper.addArmorMaterial(
			MinaMod.MODID + ":obsidian_armor", MinaMod.MODID + ":obsidian", 40, new int[] { 7, 8, 8, 7 }, 4, null,
			4.0f);// TODO:
	// sound
	public static final ItemArmor.ArmorMaterial PLATINUM = EnumHelper.addArmorMaterial(
			MinaMod.MODID + ":platinum_armor", MinaMod.MODID + ":platinum", 45, new int[] { 2, 6, 5, 2 }, 6, null,
			2.0f);// TODO:
	// sound
	public static final ItemArmor.ArmorMaterial TURTLE_SHELL = EnumHelper.addArmorMaterial(
			MinaMod.MODID + ":turtle_shell", MinaMod.MODID + ":turtle", 13, new int[] { 2, 6, 5, 2 }, 6, null, 1.0f);// TODO:
	// sound
	
	static{
		/*CITRIN.customCraftingMaterial = MinaItems.CITRIN;
		OBSIDIAN.customCraftingMaterial = MinaItems.OBSIDIAN_FRAGMENT;
		PLATINUM.customCraftingMaterial = MinaItems.PLATINUM_INGOT;*/

		CITRIN.setRepairItem(new ItemStack(MinaItems.CITRIN, 1));
		OBSIDIAN.setRepairItem(new ItemStack(MinaItems.OBSIDIAN_FRAGMENT, 1));
		PLATINUM.setRepairItem(new ItemStack(MinaItems.PLATINUM_INGOT, 1));
	}

}
