package lu.kremi151.minamod.interfaces;

import net.minecraft.item.ItemStack;

public interface IEconomyValuable {

	/**
	 * Returns the value of this stack
	 * @param stack
	 * @return
	 */
	int getEconomyValue(ItemStack stack);
	
	/**
	 * Tries to shrink the value of this stack by the given amount
	 * @param stack
	 * @param amount
	 * @param true if this should only be simulated
	 * @return Returns the amount of value left which could not be consumed to shrink
	 */
	Result shrinkStackValue(ItemStack stack, int amount, boolean simulate);
	
	/**
	 * Tries to grow the value of this stack by the given amount
	 * @param stack
	 * @param amount
	 * @param true if this should only be simulated
	 * @return Returns the amount of value left which could not be consumed to grow
	 */
	Result growStackValue(ItemStack stack, int amount, boolean simulate);
	
	/**
	 * Returns true if this stack still has some value
	 * @param stack
	 * @return
	 */
	boolean hasEconomyValue(ItemStack stack);
	
	public static class Result{
		public final int notConsumed, newValue;
		
		public Result(int notConsumed, int newValue) {
			this.notConsumed = notConsumed;
			this.newValue = newValue;
		}
	}
	
}
