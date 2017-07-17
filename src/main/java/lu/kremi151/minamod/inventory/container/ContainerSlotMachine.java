package lu.kremi151.minamod.inventory.container;

import lu.kremi151.minamod.block.tileentity.TileEntitySlotMachine;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IContainerListener;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ContainerSlotMachine extends BaseContainer{

	private static final int CMD_UPDATE_WHEEL = 0;
	private static final int CMD_UPDATE_TURN_STATE = 1;
	
	private final EntityPlayer player;
	private final TileEntitySlotMachine slotMachine;
	
	private boolean syncUpdate = false;
	
	public ContainerSlotMachine(EntityPlayer player, TileEntitySlotMachine slotMachine) {
		this.player = player;
		this.slotMachine = slotMachine;
	}

	@Override
	public boolean canInteractWith(EntityPlayer playerIn) {
		return true;
	}
	
	@Override
	public void detectAndSendChanges(){
        super.detectAndSendChanges();
        
    	if(syncUpdate) {
    		for (int i = 0; i < this.listeners.size(); ++i)
            {
        		IContainerListener icrafting = (IContainerListener)this.listeners.get(i);
                
        		for(int x = 0 ; x < slotMachine.getWheelCount() ; x++) {
        			int data[] = slotMachine.getWheelData(x);
        			for(int y = 0 ; y < slotMachine.getDisplayWheelSize() ; y++) {
        				icrafting.sendWindowProperty(this, packWheelCoordinate(x, y), data[y]);
        			}
        		}
        		
        		icrafting.sendWindowProperty(this, CMD_UPDATE_TURN_STATE, 0);//TODO
            }
    	}
	}
	
	@Override
	@SideOnly(Side.CLIENT)
    public void updateProgressBar(int id, int data)
    {
		final int cmd = unpackCommand(id);
		switch(cmd) {
		case CMD_UPDATE_WHEEL:
			final int x = unpackWheelIndex(id);
			final int y = unpackWheelPosition(id);
			slotMachine.getWheelData(x)[y] = data;
			break;
		}
    }
	
	private int packWheelCoordinate(int wheelIdx, int pos) {
		return ((wheelIdx & 15) << 8) | ((pos << 4) & 15);
	}
	
	private int unpackWheelIndex(int packed) {
		return (packed >> 8) & 15;
	}
	
	private int unpackWheelPosition(int packed) {
		return (packed >> 4) & 15;
	}
	
	private int unpackCommand(int packed) {
		return packed & 15;
	}

}
