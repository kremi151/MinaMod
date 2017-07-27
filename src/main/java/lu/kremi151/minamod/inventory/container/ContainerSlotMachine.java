package lu.kremi151.minamod.inventory.container;

import java.util.Random;

import lu.kremi151.minamod.block.tileentity.TileEntitySlotMachine;
import lu.kremi151.minamod.capabilities.coinhandler.ICoinHandler;
import lu.kremi151.minamod.util.Task;
import lu.kremi151.minamod.util.slotmachine.SpinMode;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IContainerListener;
import net.minecraft.item.Item;

public class ContainerSlotMachine extends BaseContainer{

	protected static final int CMD_UPDATE_WHEEL = 0;
	protected static final int CMD_UPDATE_TURN_STATE = 1;
	protected static final int CMD_UPDATE_PRICE = 2;
	protected static final int CMD_UPDATE_CREDITS = 3;
	protected static final int CMD_UPDATE_SESSION_WIN = 4;
	
	private final EntityPlayer player;
	protected final TileEntitySlotMachine slotMachine;
	
	private boolean firstSync = true;
	
	public ContainerSlotMachine(EntityPlayer player, TileEntitySlotMachine slotMachine) {
		this.player = player;
		this.slotMachine = slotMachine;
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
	
	public String getCustomName() {
		return slotMachine.getCustomName();
	}
	
	public boolean isTurning() {
		return slotMachine.isTurning();
	}
	
	public int getCredits() {
		return slotMachine.getPlayingCredits();
	}
	
	public void spin(SpinMode mode) {
		slotMachine.turnSlots(mode, new Random(System.currentTimeMillis()));
	}
	
	public Item getIcon(int wheelIdx, int wheelPos) {
		int id = slotMachine.getWheelValue(wheelIdx, wheelPos);
		return slotMachine.getItemIcon(id);
	}
	
	public int getPriceFor1Spin() {
		return slotMachine.getPriceFor1Spin();
	}
	
	public int getPriceFor3Spins() {
		return slotMachine.getPriceFor3Spins();
	}
	
	public int getPriceFor5Spins() {
		return slotMachine.getPriceFor5Spins();
	}
	
	public int getSessionWin() {
		return slotMachine.getSessionWin();
	}
	
	public void report() {}//TODO: Remove when out of beta
	
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
        				icrafting.sendWindowProperty(this, CMD_UPDATE_WHEEL, packWheelUpdate(x, y, slotMachine.getWheelValue(x, y), slotMachine.isWinningSlot(x, y)));
        			}
        		}
        		
        		icrafting.sendWindowProperty(this, CMD_UPDATE_TURN_STATE, slotMachine.isTurning()?1:0);
        		icrafting.sendWindowProperty(this, CMD_UPDATE_PRICE, slotMachine.getPriceFor1Spin() & 255);
        		icrafting.sendWindowProperty(this, CMD_UPDATE_PRICE, 256 | (slotMachine.getPriceFor3Spins() & 255));
        		icrafting.sendWindowProperty(this, CMD_UPDATE_PRICE, 512 | (slotMachine.getPriceFor3Spins() & 255));
        		icrafting.sendWindowProperty(this, CMD_UPDATE_CREDITS, slotMachine.getPlayingCredits());
        		icrafting.sendWindowProperty(this, CMD_UPDATE_SESSION_WIN, slotMachine.getSessionWin());
            }
    		slotMachine.notifySynced();
    	}
	}
	
	public static int packWheelUpdate(int wheelIdx, int slot, int value, boolean winning) {
		return ((wheelIdx & 7) << 13) | ((slot & 3) << 11) | ((winning?1:0) << 10) | (value & 1023);
	}
	
	public static int unpackWheelIndex(int packed) {
		return (packed >> 13) & 7;
	}
	
	public static int unpackWheelPosition(int packed) {
		return (packed >> 11) & 3;
	}
	
	public static int unpackValue(int packed) {
		return packed & 1023;
	}
	
	public static boolean unpackWinning(int packed) {
		return ((packed >> 10) & 1) == 1;
	}

}
