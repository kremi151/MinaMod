package lu.kremi151.minamod.container;

import lu.kremi151.minamod.block.tileentity.TileEntityOven;
import lu.kremi151.minamod.container.utils.SlotItemHandlerReadOnly;
import lu.kremi151.minamod.util.ShiftClickManager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IContainerListener;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.items.SlotItemHandler;

public class ContainerOven extends BaseContainer{

	private static final int PROPERTY_COOK_PROGRESS = 0;
	private static final int PROPERTY_COOKING = 1;
	private static final int PROPERTY_ENERGY = 2;
	
	private static final ShiftClickManager shiftClick = ShiftClickManager.builder()
					.addTransfer(2, 2 + PLAYER_INV_SLOT_COUNT, 0, 1, true)
					.defaultTransfer(2, 2 + PLAYER_INV_SLOT_COUNT, true)
					.build();
	
	private final EntityPlayer player;
	private final TileEntityOven te;
	private int cookProgress = 0, energy = 0;
	private boolean isCooking = false;
	
	public ContainerOven(EntityPlayer player, TileEntityOven te) {
		super();
		this.player = player;
		this.te = te;

		addSlotToContainer(new SlotItemHandler(te.getInputInventory(), 0, 56, 17));
		addSlotToContainer(new SlotItemHandlerReadOnly(te.getOutputInventory(), 0, 116, 35));
		bindPlayerInventory(player.inventory, 8, 84);
	}

	@Override
	public boolean canInteractWith(EntityPlayer playerIn) {
		return true;
	}

	@Override
    public ItemStack transferStackInSlot(EntityPlayer player, int slot) {
		return shiftClick.handle(this, player, slot);
	}
	
	@SideOnly(Side.CLIENT)
	public int getCookProgress() {
		return cookProgress;
	}
	
	@SideOnly(Side.CLIENT)
	public int getEnergy() {
		return energy;
	}
	
	@SideOnly(Side.CLIENT)
	public boolean isCooking() {
		return isCooking;
	}
	
	@Override
	@SideOnly(Side.CLIENT)
    public void updateProgressBar(int id, int data)
    {
		switch(id){
		case PROPERTY_COOK_PROGRESS:
			this.cookProgress = data;
			break;
		case PROPERTY_COOKING:
			this.isCooking = data == 1;
			break;
		case PROPERTY_ENERGY:
			this.energy = data;
			break;
		}
    }
	
	@Override
	public void detectAndSendChanges(){
        super.detectAndSendChanges();
    	for (int i = 0; i < this.listeners.size(); ++i)
        {
    		IContainerListener icrafting = (IContainerListener)this.listeners.get(i);
            icrafting.sendWindowProperty(this, PROPERTY_COOK_PROGRESS, te.getCookProgress());
            icrafting.sendWindowProperty(this, PROPERTY_COOKING, te.isCooking()?1:0);
            icrafting.sendWindowProperty(this, PROPERTY_ENERGY, te.getEnergy());
        }
	}

}
