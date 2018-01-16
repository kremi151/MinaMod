package lu.kremi151.minamod.container;

import lu.kremi151.minamod.block.tileentity.TileEntityCoalCompressor;
import lu.kremi151.minamod.inventory.BaseInventoryImpl;
import lu.kremi151.minamod.inventory.SlotReadOnly;
import lu.kremi151.minamod.inventory.SlotReadOnly.ISlotReadOnlyHandler;
import lu.kremi151.minamod.util.ShiftClickManager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

public class ContainerCoalCompressor extends BaseContainer implements ISlotReadOnlyHandler{

	private final EntityPlayer player;
	private final TileEntityCoalCompressor compressor;
	
	private static final ShiftClickManager shiftClick = ShiftClickManager.builder()
					.addTransfer(2, 2 + PLAYER_INV_SLOT_COUNT, 0, 1, true)
					.defaultTransfer(2, 2 + PLAYER_INV_SLOT_COUNT, true)
					.build();

	public ContainerCoalCompressor(EntityPlayer player, TileEntityCoalCompressor compressor) {
		this.player = player;
		this.compressor = compressor;

		addSlotToContainer(new Slot(compressor.getInputInventory(), 0, 44, 35));
		addSlotToContainer(new SlotReadOnly(compressor.getOutputInventory(), 0, 116, 35).setListener(this));
		
		bindPlayerInventory(player.inventory, 8, 84);
	}
	
	public TileEntityCoalCompressor getCompressor() {
		return compressor;
	}

	@Override
	public boolean canInteractWith(EntityPlayer playerIn) {
		return true;
	}

	@Override
    public ItemStack transferStackInSlot(EntityPlayer player, int slot) {
		return shiftClick.handle(this, player, slot);
	}

	@Override
	public void onSlotPickedUp(IInventory inv, int slot, EntityPlayer p, ItemStack is) {
		//TODO
	}

	@Override
	public boolean canPickup(IInventory inv, int slot, EntityPlayer p) {
		return true;
	}

}
