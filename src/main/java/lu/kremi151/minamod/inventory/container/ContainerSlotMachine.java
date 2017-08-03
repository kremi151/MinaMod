package lu.kremi151.minamod.inventory.container;

import java.util.Random;

import lu.kremi151.minamod.block.tileentity.TileEntitySlotMachine;
import lu.kremi151.minamod.util.ValueObserver;
import lu.kremi151.minamod.util.slotmachine.Icon;
import lu.kremi151.minamod.util.slotmachine.SpinMode;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IContainerListener;
import net.minecraft.item.Item;

public class ContainerSlotMachine extends BaseContainer{

	protected static final int CMD_UPDATE_WHEEL = 0;
	protected static final int CMD_UPDATE_TURN_STATE = 1;
	protected static final int CMD_UPDATE_PRICE = 2;
	protected static final int CMD_UPDATE_CREDITS_LEAST = 3;
	protected static final int CMD_UPDATE_CREDITS_MOST = 4;
	protected static final int CMD_UPDATE_SESSION_WIN_LEAST = 5;
	protected static final int CMD_UPDATE_SESSION_WIN_MOST = 6;
	protected static final int CMD_UPDATE_ICON_ROW_VALUE_LEAST = 7;
	protected static final int CMD_UPDATE_ICON_ROW_VALUE_MOST = 8;
	
	private final EntityPlayer player;
	protected final TileEntitySlotMachine slotMachine;
	
	private boolean firstSync = true;
	
	private final ValueObserver<Integer> observerCredits, observerSessionWin, observerPriceA, observerPriceB, observerPriceC;
	private final ValueObserver<Boolean> observerTurning;
	
	public ContainerSlotMachine(EntityPlayer player, TileEntitySlotMachine slotMachine) {
		this.player = player;
		this.slotMachine = slotMachine;

		this.observerCredits = new ValueObserver<>(() -> slotMachine.getPlayingCredits());
		this.observerSessionWin = new ValueObserver<>(() -> slotMachine.getSessionWin());
		this.observerPriceA = new ValueObserver<>(() -> slotMachine.getPriceFor1Spin());
		this.observerPriceB = new ValueObserver<>(() -> slotMachine.getPriceFor3Spins());
		this.observerPriceC = new ValueObserver<>(() -> slotMachine.getPriceFor5Spins());
		
		this.observerTurning = new ValueObserver<>(() -> slotMachine.isTurning());
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
	
	public void spin(SpinMode mode, boolean instant) {
		slotMachine.turnSlots(mode, new Random(System.currentTimeMillis()), instant);
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
	
	public int getIconCount() {
		return slotMachine.getIconCount();
	}
	
	public Icon getIcon(int i) {
		return slotMachine.getIcon(i);
	}
	
	public void report() {}//TODO: Remove when out of beta
	
	@Override
	public void detectAndSendChanges(){
        super.detectAndSendChanges();
        
    	if(firstSync || slotMachine.needsSync()) {
    		
            boolean syncCredits = observerCredits.hasChangedSinceLastCheck();
            boolean syncSessionWin = observerSessionWin.hasChangedSinceLastCheck();
            boolean syncTurning = observerTurning.hasChangedSinceLastCheck();
    		
    		for (int i = 0; i < this.listeners.size(); ++i)
            {
        		IContainerListener icrafting = (IContainerListener)this.listeners.get(i);
                
        		for(int x = 0 ; x < slotMachine.getWheelCount() ; x++) {
        			for(int y = 0 ; y < slotMachine.getDisplayWheelSize() ; y++) {
        				icrafting.sendWindowProperty(this, CMD_UPDATE_WHEEL, packWheelUpdate(x, y, slotMachine.getWheelValue(x, y), slotMachine.isWinningSlot(x, y)));
        			}
        		}
        		
        		if(syncTurning) {
        			icrafting.sendWindowProperty(this, CMD_UPDATE_TURN_STATE, slotMachine.isTurning()?1:0);
        		}
        		
        		if(observerPriceA.hasChangedSinceLastCheck()) {
        			icrafting.sendWindowProperty(this, CMD_UPDATE_PRICE, slotMachine.getPriceFor1Spin() & 255);
        		}
        		if(observerPriceB.hasChangedSinceLastCheck()) {
        			icrafting.sendWindowProperty(this, CMD_UPDATE_PRICE, 256 | (slotMachine.getPriceFor3Spins() & 255));
        		}
        		if(observerPriceC.hasChangedSinceLastCheck()) {
        			icrafting.sendWindowProperty(this, CMD_UPDATE_PRICE, 512 | (slotMachine.getPriceFor5Spins() & 255));
        		}
        		
        		if(syncCredits) {
        			icrafting.sendWindowProperty(this, CMD_UPDATE_CREDITS_LEAST, (slotMachine.getPlayingCredits() >> 16) & 0xFFFF);
            		icrafting.sendWindowProperty(this, CMD_UPDATE_CREDITS_MOST, slotMachine.getPlayingCredits() & 0xFFFF);
        		}
        		
        		if(syncSessionWin) {
        			icrafting.sendWindowProperty(this, CMD_UPDATE_SESSION_WIN_LEAST, (slotMachine.getSessionWin() >> 16) & 0xFFFF);
            		icrafting.sendWindowProperty(this, CMD_UPDATE_SESSION_WIN_MOST, slotMachine.getSessionWin() & 0xFFFF);
        		}
        		
        		if(firstSync) {
        			for(int j = 0 ; j < slotMachine.getIconCount() ; j++) {
        				Icon icon = slotMachine.getIcon(j);
        				int rowValue;
        				if(icon.cherry) {
        					rowValue = slotMachine.evaluateCherryRowPrice(j);
        				}else {
        					rowValue = slotMachine.evaluateRowPrice(j);
        				}
            			icrafting.sendWindowProperty(this, CMD_UPDATE_ICON_ROW_VALUE_LEAST, (rowValue >> 16) & 0xFFFF);
                		icrafting.sendWindowProperty(this, CMD_UPDATE_ICON_ROW_VALUE_MOST, rowValue & 0xFFFF);
        			}
        		}
            }
    		slotMachine.notifySynced();
    		firstSync = false;
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
