package lu.kremi151.minamod.container;

import lu.kremi151.minamod.MinaMod;
import lu.kremi151.minamod.block.tileentity.TileEntitySlotMachine;
import lu.kremi151.minamod.network.MessageSpinSlotMachine;
import lu.kremi151.minamod.util.slotmachine.SpinMode;
import lu.kremi151.minamod.util.slotmachine.WheelManager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class ContainerSlotMachineClient extends ContainerSlotMachine{
	
	private boolean isTurning = false;
	private final WheelManager wheels = new WheelManager(5, 3);
	private final int prices[] = new int[3];
	private int credits = 0, sessionWin = 0, creditsBuffer = 0, sessionWinBuffer = 0, rowValueBuffer = 0, rowValueIdx = 0, energyPercentage = 0;
	
	private final int iconRowValues[];

	public ContainerSlotMachineClient(EntityPlayer player, TileEntitySlotMachine slotMachine) {
		super(player, slotMachine);
		
		this.iconRowValues = new int[slotMachine.getIconCount()];
	}
	
	@Override
	public boolean isTurning() {
		return this.isTurning;
	}
	
	@Override
	public void spin(SpinMode mode, boolean instant) {
		MinaMod.getMinaMod().getPacketDispatcher().sendToServer(new MessageSpinSlotMachine(mode, slotMachine.getPos(), instant));
	}

	@Override
	public int getPriceFor1Spin() {
		return prices[0];
	}

	@Override
	public int getPriceFor3Spins() {
		return prices[1];
	}

	@Override
	public int getPriceFor5Spins() {
		return prices[2];
	}
	
	@Override
	public int getCredits() {
		return credits;
	}

	@Override
	public int getSessionWin() {
		return sessionWin;
	}
	
	@Override
	public Item getIcon(int wheelIdx, int wheelPos) {
		int id = wheels.getWheelValue(wheelIdx, wheelPos);
		return slotMachine.getItemIcon(id);
	}
	
	@Override
	public int getEnergyPercentage(){
		return energyPercentage;
	}
	
	public int getWheelCount() {
		return wheels.getWheelCount();
	}
	
	public int getDisplayWheelSize() {
		return wheels.getDisplayWheelSize();
	}
	
	public boolean isWinning(int wheelIdx, int wheelPos) {
		return wheels.isWinning(wheelIdx, wheelPos);
	}
	
	public int getIconRowValue(int i) {
		return iconRowValues[i];
	}
	
	@Override
	@SideOnly(Side.CLIENT)
    public void updateProgressBar(int id, int data)
    {
		switch(id) {
		case 999:
			rowValueIdx = 0;
			break;
		case CMD_UPDATE_WHEEL:
			int wheelIdx = unpackWheelIndex(data);
			int wheelPos = unpackWheelPosition(data);
			wheels.setWheelContent(wheelIdx, wheelPos, unpackValue(data));
			wheels.setWheelWinning(wheelIdx, wheelPos, unpackWinning(data));
			break;
		case CMD_UPDATE_TURN_STATE:
			this.isTurning = (data & 1) == 1;
			break;
		case CMD_UPDATE_PRICE:
			int pos = (data >> 8) & 255;
			prices[pos] = data & 255;
			break;
		case CMD_UPDATE_CREDITS_LEAST:
			creditsBuffer = (data & 0xFFFF) << 16;
			break;
		case CMD_UPDATE_CREDITS_MOST:
			credits = creditsBuffer | (data & 0xFFFF);
			break;
		case CMD_UPDATE_SESSION_WIN_LEAST:
			sessionWinBuffer = (data & 0xFFFF) << 16;
			break;
		case CMD_UPDATE_SESSION_WIN_MOST:
			sessionWin = sessionWinBuffer | (data & 0xFFFF);
			break;
		case CMD_UPDATE_ICON_ROW_VALUE_LEAST:
			rowValueBuffer = (data & 0xFFFF) << 16;
			break;
		case CMD_UPDATE_ICON_ROW_VALUE_MOST:
			iconRowValues[rowValueIdx++] = rowValueBuffer | (data & 0xFFFF);
			break;
		case CMD_UPDATE_ENERGY_PERCENTAGE:
			energyPercentage = data;
			break;
		}
    }

}
