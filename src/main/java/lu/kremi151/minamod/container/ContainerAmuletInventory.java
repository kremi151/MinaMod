package lu.kremi151.minamod.container;

import lu.kremi151.minamod.capabilities.amulets.CapabilityAmuletHolder;
import lu.kremi151.minamod.capabilities.amulets.IAmulet;
import lu.kremi151.minamod.capabilities.amulets.IAmuletHolder;
import lu.kremi151.minamod.inventory.BaseInventory;
import lu.kremi151.minamod.inventory.SlotSpecific;
import lu.kremi151.minamod.item.ItemAmulet;
import lu.kremi151.minamod.util.ShiftClickManager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;

public class ContainerAmuletInventory extends BaseContainer{
	
	private static final ShiftClickManager shiftMan = ShiftClickManager.builder()
			.addTransfer(0, 3, 3, 3 + PLAYER_INV_SLOT_COUNT)
			.addTransfer(3, 3 + PLAYER_INV_SLOT_COUNT, 0, 3, stack -> (!stack.isEmpty() && stack.hasCapability(IAmulet.CAPABILITY, null)))
			.build();
	
	private final EntityPlayer player;
	private final IAmuletHolder holder;
	private final IInventory internalInv;
	
	public ContainerAmuletInventory(EntityPlayer player){
		this.player = player;
		this.holder = player.getCapability(CapabilityAmuletHolder.CAPABILITY_AMULET_HOLDER, null);
		this.internalInv = new InternalInventory(holder);

		addSlotToContainer(new SlotSpecific(internalInv, 0, 52, 22));
		addSlotToContainer(new SlotSpecific(internalInv, 1, 70, 40));
		addSlotToContainer(new SlotSpecific(internalInv, 2, 88, 58));
		
		bindPlayerInventory(player.inventory, 8, 94);
	}

	@Override
	public boolean canInteractWith(EntityPlayer playerIn) {
		return true;
	}
	
	@Override
    public ItemStack transferStackInSlot(EntityPlayer player, int slot) {
		return shiftMan.handle(this, player, slot);
	}
	
	private class InternalInventory extends BaseInventory{

		public InternalInventory(IAmuletHolder holder) {
			super(holder.getAmulets());
		}
		
		@Override
		public int getInventoryStackLimit() {
			return 1;
		}

		@Override
		public void markDirty() {
			// TODO Auto-generated method stub
			
		}

		@Override
		public boolean isUsableByPlayer(EntityPlayer player) {
			return true;
		}

		@Override
		public boolean isItemValidForSlot(int index, ItemStack stack) {
			return stack.isEmpty() || stack.hasCapability(IAmulet.CAPABILITY, null);
		}

		@Override
		public String getName() {
			return "amuletInventory";
		}

		@Override
		public void onCraftMatrixChanged() {}
		
	}

}
