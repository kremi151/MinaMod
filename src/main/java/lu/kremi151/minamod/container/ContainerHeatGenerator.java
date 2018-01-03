package lu.kremi151.minamod.container;

import lu.kremi151.minamod.block.tileentity.TileEntityHeatGenerator;
import lu.kremi151.minamod.util.ShiftClickManager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

public class ContainerHeatGenerator extends BaseContainer{
	
	private static final int FILTER_INV_START = 0;
	private static final int PLAYER_INV_START = FILTER_INV_START + 1;

	private final EntityPlayer pl;
	private final TileEntityHeatGenerator te;
	
	private static final ShiftClickManager shiftClick = ShiftClickManager.builder()
					.addTransfer(FILTER_INV_START, FILTER_INV_START + 1, PLAYER_INV_START, PLAYER_INV_START + PLAYER_INV_SLOT_COUNT, true)
					.defaultTransfer(FILTER_INV_START, FILTER_INV_START + 1, true)
					.build();
	
	public ContainerHeatGenerator(EntityPlayer player, TileEntityHeatGenerator te) {
		this.pl = player;
		this.te = te;
		
		addSlotToContainer(new Slot(te, 0, 80, 20));
		
		bindPlayerInventory(pl.inventory, 8, 51);
	}

	@Override
	public boolean canInteractWith(EntityPlayer playerIn) {
		return true;
	}

	@Override
    public ItemStack transferStackInSlot(EntityPlayer player, int slot) {
		return shiftClick.handle(this, player, slot);
	}
	
	public TileEntityHeatGenerator getGenerator() {
		return te;
	}

}
