package lu.kremi151.minamod.container;

import lu.kremi151.minamod.block.tileentity.TileEntityAutoCrafter;
import lu.kremi151.minamod.capabilities.sketch.ISketch;
import lu.kremi151.minamod.util.ShiftClickManager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IContainerListener;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ContainerAutoCrafter extends BaseContainer{
	
	private static final int PB_UPDATE_ENERGY = 0;
	
	private static final ShiftClickManager shiftClick = ShiftClickManager.builder()
					.addTransfer(15, 15 + PLAYER_INV_SLOT_COUNT, 9, 10, true, stack -> stack.hasCapability(ISketch.CAPABILITY, null))
					.addTransfer(15, 15 + PLAYER_INV_SLOT_COUNT, 0, 9, true)
					.defaultTransfer(15, 15 + PLAYER_INV_SLOT_COUNT, true)
					.build();
	
	private final TileEntityAutoCrafter te;
	private int energy = 0;

	public ContainerAutoCrafter(EntityPlayer player, TileEntityAutoCrafter te) {
		this.te = te;

		for(int y = 0 ; y < 3 ; y++) {
			for(int x = 0 ; x < 3 ; x++) {
				addSlotToContainer(new Slot(te, (y * 3) + x, 27 + (x * 18), 32 + (y * 18)));
			}
		}
		
		addSlotToContainer(new Slot(te.sketchInv, 0, 123, 32));
		
		for(int i = 0 ; i < 5 ; i++) {
			addSlotToContainer(new Slot(te.resultsInv, i, 141 + (i * 18), 86));
		}
		
		bindPlayerInventory(player.inventory, 39, 114);
	}
	
	public TileEntityAutoCrafter getAutoCrafter() {
		return te;
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
	@SideOnly(Side.CLIENT)
    public void updateProgressBar(int id, int data)
    {
		switch(id){
		case PB_UPDATE_ENERGY:
			this.energy = data;
			break;
		}
    }
	
	@Override
	public void detectAndSendChanges(){
        super.detectAndSendChanges();

        int l = this.listeners.size();
    	for (int i = 0; i < l; ++i)
        {
    		IContainerListener icrafting = (IContainerListener)this.listeners.get(i);
            icrafting.sendWindowProperty(this, PB_UPDATE_ENERGY, te.getCapability(CapabilityEnergy.ENERGY, null).getEnergyStored());
        }
	}
	
	@SideOnly(Side.CLIENT)
	public int getEnergy() {
		return energy;
	}

}
