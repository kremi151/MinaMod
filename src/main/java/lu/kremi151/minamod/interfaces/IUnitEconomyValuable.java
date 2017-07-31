package lu.kremi151.minamod.interfaces;

import net.minecraft.item.ItemStack;

public interface IUnitEconomyValuable extends IEconomyValuable{

	/**
	 * Returns the value per unit of this stack
	 * @param stack
	 * @return
	 */
	int getUnitEconomyValue(ItemStack stack);
	
}
