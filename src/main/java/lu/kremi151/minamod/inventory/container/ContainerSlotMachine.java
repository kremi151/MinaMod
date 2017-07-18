package lu.kremi151.minamod.inventory.container;

import java.util.Random;

import lu.kremi151.minamod.block.tileentity.TileEntitySlotMachine;
import lu.kremi151.minamod.util.Task;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IContainerListener;
import net.minecraft.item.Item;

public class ContainerSlotMachine extends BaseContainer{

	public static final int CMD_UPDATE_WHEEL = 0;
	public static final int CMD_UPDATE_TURN_STATE = 1;
	
	private final EntityPlayer player;
	protected final TileEntitySlotMachine slotMachine;
	
	private boolean firstSync = true;
	
	public ContainerSlotMachine(EntityPlayer player, TileEntitySlotMachine slotMachine) {
		this.player = player;
		this.slotMachine = slotMachine;
		slotMachine.registerListeningContainer(this);
	}
	
	@Override
	public void onContainerClosed(EntityPlayer playerIn)
    {
		super.onContainerClosed(playerIn);
		EntityPlayer playing = slotMachine.getPlaying();
		if(playing == playerIn) {
			slotMachine.setCurrentPlayer(null);
		}
    }

	@Override
	public boolean canInteractWith(EntityPlayer playerIn) {
		return true;
	}
	
	public boolean isTurning() {
		return slotMachine.isTurning();
	}
	
	public void spin() {
		slotMachine.turnSlots(new Random(System.currentTimeMillis()));
	}
	
	public Item getIcon(int wheelIdx, int wheelPos) {
		int id = slotMachine.getWheelValue(wheelIdx, wheelPos);
		return slotMachine.getItemIcon(id);
	}
	
	@Override
	public void detectAndSendChanges(){
        super.detectAndSendChanges();
        
    	if(firstSync || slotMachine.needsSync()) {
    		firstSync = false;
    		for (int i = 0; i < this.listeners.size(); ++i)
            {
        		IContainerListener icrafting = (IContainerListener)this.listeners.get(i);
                
        		for(int x = 0 ; x < slotMachine.getWheelCount() ; x++) {
        			for(int y = 0 ; y < slotMachine.getDisplayWheelSize() ; y++) {
        				icrafting.sendWindowProperty(this, CMD_UPDATE_WHEEL, packWheelUpdate(x, y, slotMachine.getWheelValue(x, y)));
        			}
        		}
        		
        		icrafting.sendWindowProperty(this, CMD_UPDATE_TURN_STATE, slotMachine.isTurning()?1:0);
            }
    		slotMachine.notifySynced();
    	}
	}
	
	public static int packWheelUpdate(int wheelIdx, int pos, int value) {
		return ((wheelIdx & 255) << 24) | ((pos & 255) << 16) | (value & 65535);
	}
	
	public static int unpackWheelIndex(int packed) {
		return (packed >> 24) & 255;
	}
	
	public static int unpackWheelPosition(int packed) {
		return (packed >> 16) & 255;
	}
	
	public static int unpackValue(int packed) {
		return packed & 65535;
	}

}
