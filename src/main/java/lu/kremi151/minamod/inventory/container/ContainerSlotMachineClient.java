package lu.kremi151.minamod.inventory.container;

import lu.kremi151.minamod.MinaMod;
import lu.kremi151.minamod.block.tileentity.TileEntitySlotMachine;
import lu.kremi151.minamod.packet.message.MessageReportSlotMachine;
import lu.kremi151.minamod.packet.message.MessageSpinSlotMachine;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class ContainerSlotMachineClient extends ContainerSlotMachine{
	
	private boolean isTurning = false;
	private final TileEntitySlotMachine.WheelManager wheels = new TileEntitySlotMachine.WheelManager(5, 3);
	private final int prices[] = new int[3];
	private int credits = 0;

	public ContainerSlotMachineClient(EntityPlayer player, TileEntitySlotMachine slotMachine) {
		super(player, slotMachine);
	}
	
	@Override
	public boolean isTurning() {
		return this.isTurning;
	}
	
	@Override
	public void spin(TileEntitySlotMachine.SpinMode mode) {
		MinaMod.getMinaMod().getPacketDispatcher().sendToServer(new MessageSpinSlotMachine(mode, slotMachine.getPos()));
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
	public Item getIcon(int wheelIdx, int wheelPos) {
		int id = wheels.getWheelValue(wheelIdx, wheelPos);
		return slotMachine.getItemIcon(id);
	}
	
	@Override
	@SideOnly(Side.CLIENT)
    public void updateProgressBar(int id, int data)
    {
		switch(id) {
		case CMD_UPDATE_WHEEL:
			wheels.setWheelContent(unpackWheelIndex(data), unpackWheelPosition(data), unpackValue(data));
			break;
		case CMD_UPDATE_TURN_STATE:
			this.isTurning = (data & 1) == 1;
			break;
		case CMD_UPDATE_PRICE:
			prices[0] = (data >> 16) & 255;
			prices[1] = (data >> 8) & 255;
			prices[2] = data & 255;
			break;
		case CMD_UPDATE_CREDITS:
			credits = data;
			break;
		}
    }
	
	@Override
	public void report() {
		MinaMod.getMinaMod().getPacketDispatcher().sendToServer(new MessageReportSlotMachine(slotMachine.getPos()));
	}

}
