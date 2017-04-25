package lu.kremi151.minamod.inventory;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

public class SlotReadOnly extends Slot{
	
	ISlotReadOnlyHandler l;

	public SlotReadOnly(IInventory inv, int slot, int x, int y) {
		super(inv, slot, x, y);
	}
	
	@Override
    public boolean isItemValid(ItemStack is)
    {
        return false;
    }
	
	@Override
	public boolean canTakeStack(EntityPlayer p)
    {
		if(l != null){
			return l.canPickup(inventory, slotNumber, p);
		}else{
			return false;
		}
    }
	
	@Override
	public ItemStack onTake(EntityPlayer p, ItemStack is)
    {
        if(l!=null)l.onSlotPickedUp(inventory, slotNumber, p, is);
        this.onSlotChanged();
        return is;
    }
	
	public SlotReadOnly setListener(ISlotReadOnlyHandler l){
		this.l = l;
		return this;
	}
	
	public static interface ISlotReadOnlyHandler{
		public void onSlotPickedUp(IInventory inv, int slot, EntityPlayer p, ItemStack is);
		public boolean canPickup(IInventory inv, int slot, EntityPlayer p);
	}

}
