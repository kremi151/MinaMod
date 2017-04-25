package lu.kremi151.minamod.inventory;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;


public class SlotSpecific extends Slot{ // NO_UCD (unused code)

	public SlotSpecific(IInventory inv, int slot, int x, int y) {
		super(inv, slot, x, y);
	}
	
	@Override
    public boolean isItemValid(ItemStack is)
    {
        return this.inventory.isItemValidForSlot(this.slotNumber, is);
    }
	
	@Override
	public boolean canTakeStack(EntityPlayer p)
    {
		return this.inventory.isUsableByPlayer(p);
    }
	
	@Override
	public ItemStack onTake(EntityPlayer p, ItemStack is)
    {
        return super.onTake(p, is);
    }

}
