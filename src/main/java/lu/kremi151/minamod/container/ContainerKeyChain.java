package lu.kremi151.minamod.container;

import lu.kremi151.minamod.inventory.SlotReadOnly;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumHand;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.SlotItemHandler;

public class ContainerKeyChain extends BaseContainer {

	private EntityPlayer player;
	private int slot = -1;

	public ContainerKeyChain(EntityPlayer player) {
		this.player = player;
		this.slot = player.inventory.currentItem;
		
		bindPlayerInventory(player.inventory, 8, 95);

		IItemHandler inv = getStack().getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, null);
		
		addSlotToContainer(new SlotItemHandler (inv, 0, 64, 11));
		addSlotToContainer(new SlotItemHandler (inv, 0, 95, 11));
		addSlotToContainer(new SlotItemHandler (inv, 0, 125, 36));
		addSlotToContainer(new SlotItemHandler (inv, 0, 125, 67));
		addSlotToContainer(new SlotItemHandler (inv, 0, 95, 92));
		addSlotToContainer(new SlotItemHandler (inv, 0, 64, 92));
		addSlotToContainer(new SlotItemHandler (inv, 0, 34, 67));
		addSlotToContainer(new SlotItemHandler (inv, 0, 34, 36));
		
		if(slot >= 0){
			int l = this.inventorySlots.size();
			for(int i = 0 ; i < l ;i++){
				Slot s = this.inventorySlots.get(i);
				if(s.getSlotIndex() == slot){
					this.inventorySlots.set(i, new SlotReadOnly(player.inventory, s.getSlotIndex(), s.xPos, s.yPos));
					break;
				}
			}
		}
	}

	@Override
	public boolean canInteractWith(EntityPlayer playerIn) {
		return true;
	}

	public ItemStack getStack() {
		return player.getHeldItem(EnumHand.MAIN_HAND);
	}

	@Override
    public ItemStack transferStackInSlot(EntityPlayer player, int slot) {
		return ItemStack.EMPTY;//TODO
	}

}
