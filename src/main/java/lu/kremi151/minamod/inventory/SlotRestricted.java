package lu.kremi151.minamod.inventory;

import lu.kremi151.minamod.inventory.SlotReadOnly.ISlotReadOnlyHandler;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;


public class SlotRestricted extends Slot{ // NO_UCD (unused code)
	
	ISlotRestrictionHandler l;

	public SlotRestricted(IInventory inv, int slot, int x, int y) {
		super(inv, slot, x, y);
	}
	
	@Override
    public boolean isItemValid(ItemStack is)
    {
       	if(l != null){
       		return l.canPut(inventory, this.getSlotIndex(), is);
       	}else{
       		return false;
       	}
    }
	
	@Override
	public boolean canTakeStack(EntityPlayer p)
    {
		if(l != null){
			return l.canPickup(inventory, this.getSlotIndex(), p);
		}else{
			return false;
		}
    }
	
	@Override
	public ItemStack onTake(EntityPlayer p, ItemStack is)
    {
        if(l!=null)l.onSlotPickedUp(inventory, this.getSlotIndex(), p, is);
        this.onSlotChanged();
        return is;
    }
	
	@Override
	public void onSlotChange(ItemStack a, ItemStack b)
    {
		super.onSlotChange(a, b);
    }
	
	public SlotRestricted setListener(ISlotRestrictionHandler l){
		this.l = l;
		return this;
	}
	
	public static interface ISlotRestrictionHandler extends ISlotReadOnlyHandler{
		public boolean canPut(IInventory inv, int slot, ItemStack is);
		
	}

}
