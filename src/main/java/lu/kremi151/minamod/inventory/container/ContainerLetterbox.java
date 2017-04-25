package lu.kremi151.minamod.inventory.container;

import lu.kremi151.minamod.MinaPermissions;
import lu.kremi151.minamod.block.tileentity.TileEntityLetterbox;
import lu.kremi151.minamod.inventory.SlotReadOnly;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraftforge.server.permission.PermissionAPI;

public class ContainerLetterbox extends BaseContainer{
	
	private static int INPUT_SLOT = 0;
	private static int STORAGE_SLOT_START = 1;
	private static int PLAYER_INV_START = 7;

	EntityPlayer pl;
	TileEntityLetterbox te;
	boolean isOwner;

	public ContainerLetterbox(EntityPlayer player, TileEntityLetterbox te) {
		this.pl = player;
		this.te = te;
		
		isOwner = te.canAccess(player);
		
		addSlotToContainer(new Slot(te.getInputInventory(), 0, 44, 38));
		
		if(isOwner){
			for(int h = 0 ; h <3 ; h++){
				for(int v = 0 ; v < 2 ; v++){
					addSlotToContainer(new Slot(te.getStorageInventory(), (v*3)+h, 98 + (h * 18), 29 + (v * 18)));
				}
			}
		}

		bindPlayerInventory(pl.inventory, 8, 97);
	}

	@Override
	public boolean canInteractWith(EntityPlayer playerIn) {
		return true;
	}

	@Override
    public ItemStack transferStackInSlot(EntityPlayer player, int slot) {
		ItemStack stack = ItemStack.EMPTY;
		Slot _slot = this.getSlot(slot);
		if(_slot != null && _slot.getHasStack()){
			stack = _slot.getStack();
			ItemStack copy = stack.copy();
			
			if(slot < PLAYER_INV_START){
				if(!mergeItemStack(stack, PLAYER_INV_START, PLAYER_INV_START + PLAYER_INV_SLOT_COUNT, true)){
					return ItemStack.EMPTY;
				}
			}else{
				if(te.canAccess(player)){
					if(!mergeItemStack(stack, INPUT_SLOT, PLAYER_INV_START, true)){
						return ItemStack.EMPTY;
					}
				}else{
					if(!mergeItemStack(stack, INPUT_SLOT, STORAGE_SLOT_START, true)){
						return ItemStack.EMPTY;
					}
				}
			}
			
			_slot.onSlotChange(stack, copy);
			_slot.onTake(player, stack);
			return stack;
		}else{
			return ItemStack.EMPTY;
		}
	}
	
	public EntityPlayer getPlayer(){
		return pl;
	}
	
	public TileEntityLetterbox getLetterbox(){
		return te;
	}
	
	public boolean isOwner(){
		return isOwner;
	}

}
