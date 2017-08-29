package lu.kremi151.minamod.inventory;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.InventoryCrafting;

public class PseudoInventoryCrafting extends InventoryCrafting{

	public PseudoInventoryCrafting(int width, int height) {
		super(new PseudoContainer(), width, height);
	}
	
	private static class PseudoContainer extends Container{

		@Override
		public boolean canInteractWith(EntityPlayer playerIn) {
			return true;
		}
		
	}

}
