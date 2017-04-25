package lu.kremi151.minamod.inventory.container;

import lu.kremi151.minamod.block.tileentity.TileEntityAutoFeeder;
import lu.kremi151.minamod.util.ShiftClickManager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IContainerListener;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ContainerAutoFeeder extends BaseContainer{
	
	private static final int FOOD_INV_START = 0;
	private static final int PLAYER_INV_START = FOOD_INV_START + 1;

	public static final int PB_TICKS_LEFT = 0;
	public static final int PB_MAX_TICKS = 1;
	public static final int PB_ENERGY = 2;
	
	private static final ShiftClickManager shiftClick = ShiftClickManager.builder()
					.addTransfer(FOOD_INV_START, FOOD_INV_START + 1, PLAYER_INV_START, PLAYER_INV_START + PLAYER_INV_SLOT_COUNT, true)
					.defaultTransfer(FOOD_INV_START, FOOD_INV_START + 1, true)
					.build();

	private final EntityPlayer pl;
	private final TileEntityAutoFeeder te;
	
	@SideOnly(Side.CLIENT)
	int ticksLeft;
	@SideOnly(Side.CLIENT)
	int maxTicks = 1000;
	@SideOnly(Side.CLIENT)
	int energy = 0;
	
	int lastTicksLeft;

	public ContainerAutoFeeder(EntityPlayer player, TileEntityAutoFeeder te) {
		this.pl = player;
		this.te = te;
		
		addSlotToContainer(new Slot(te, 0, 80, 32));
		
		bindPlayerInventory(pl.inventory, 8, 67);
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
		case PB_TICKS_LEFT:
			this.ticksLeft = data;
			break;
		case PB_MAX_TICKS:
			this.maxTicks = data;
			break;
		case PB_ENERGY:
			this.energy = data;
			break;
		}
    }
	
	@Override
	public void detectAndSendChanges(){
	    
        super.detectAndSendChanges();

        if(lastTicksLeft != te.getTicksLeft()){
        	lastTicksLeft = te.getTicksLeft();
        	int l = this.listeners.size();
        	for (int i = 0; i < l; ++i)
            {
        		IContainerListener icrafting = (IContainerListener)this.listeners.get(i);
                icrafting.sendProgressBarUpdate(this, PB_TICKS_LEFT, te.getTicksLeft());
                icrafting.sendProgressBarUpdate(this, PB_MAX_TICKS, te.getMaxTicks());
                icrafting.sendProgressBarUpdate(this, PB_ENERGY, te.getEnergy());
            }
        }
        
	}
	
	public EntityPlayer getPlayer(){
		return pl;
	}
	
	public TileEntityAutoFeeder getAutoFeeder(){
		return te;
	}
	
	@SideOnly(Side.CLIENT)
	public int getTicksLeft(){
		return ticksLeft;
	}
	
	@SideOnly(Side.CLIENT)
	public int getMaxTicks(){
		return maxTicks;
	}
	
	@SideOnly(Side.CLIENT)
	public int getEnergy(){
		return energy;
	}

}
