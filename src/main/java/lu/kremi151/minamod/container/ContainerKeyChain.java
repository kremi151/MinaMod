package lu.kremi151.minamod.container;

import lu.kremi151.minamod.capabilities.IKey;
import lu.kremi151.minamod.inventory.SlotReadOnly;
import lu.kremi151.minamod.util.ShiftClickManager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumHand;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.SlotItemHandler;

public class ContainerKeyChain extends BaseContainer {

	private final EntityPlayer player;
	private final int slot;
	
	private static final ShiftClickManager shiftClick = ShiftClickManager.builder()
					.addTransfer(8, 8 + PLAYER_INV_SLOT_COUNT, 0, 8, true, stack -> stack.hasCapability(IKey.CAPABILITY_KEY, null))
					.defaultTransfer(8, 8 + PLAYER_INV_SLOT_COUNT, true)
					.build();

	public ContainerKeyChain(EntityPlayer player, int blockedSlotIndex) {
		this.player = player;
		this.slot = blockedSlotIndex;
		
		bindPlayerInventory(player.inventory, 8, 125);

		IItemHandler inv = getStack().getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, null);
		
		addSlotToContainer(new SlotItemHandler (inv, 0, 64, 11));
		addSlotToContainer(new SlotItemHandler (inv, 1, 95, 11));
		addSlotToContainer(new SlotItemHandler (inv, 2, 125, 36));
		addSlotToContainer(new SlotItemHandler (inv, 3, 125, 67));
		addSlotToContainer(new SlotItemHandler (inv, 4, 95, 92));
		addSlotToContainer(new SlotItemHandler (inv, 5, 64, 92));
		addSlotToContainer(new SlotItemHandler (inv, 6, 34, 67));
		addSlotToContainer(new SlotItemHandler (inv, 7, 34, 36));
		
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
		return shiftClick.handle(this, player, slot);
	}

}
