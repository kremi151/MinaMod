package lu.kremi151.minamod.enchantment;

import lu.kremi151.minamod.item.ItemSickle;
import net.minecraft.enchantment.EnumEnchantmentType;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;

public class EnchantmentHarvestRange extends CustomEnchantment{

	public EnchantmentHarvestRange(Rarity rarityIn, EnumEnchantmentType typeIn, EntityEquipmentSlot[] slots) {
		super(rarityIn, typeIn, slots);
	}
	
	@Override
	public boolean canApply(ItemStack stack)
    {
        return !stack.isEmpty() && stack.getItem() instanceof ItemSickle;
    }

}
