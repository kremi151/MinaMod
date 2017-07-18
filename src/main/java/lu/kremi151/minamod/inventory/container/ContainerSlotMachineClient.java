package lu.kremi151.minamod.inventory.container;

import lu.kremi151.minamod.MinaMod;
import lu.kremi151.minamod.block.tileentity.TileEntitySlotMachine;
import lu.kremi151.minamod.packet.message.MessageSpinSlotMachine;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class ContainerSlotMachineClient extends ContainerSlotMachine{
	
	private boolean isTurning = false;
	private final TileEntitySlotMachine.WheelManager wheels = new TileEntitySlotMachine.WheelManager(5, 3);

	public ContainerSlotMachineClient(EntityPlayer player, TileEntitySlotMachine slotMachine) {
		super(player, slotMachine);
	}
	
	@Override
	public boolean isTurning() {
		return this.isTurning;
	}
	
	@Override
	public void spin() {
		MinaMod.getMinaMod().getPacketDispatcher().sendToServer(new MessageSpinSlotMachine(slotMachine.getPos()));
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
			System.out.format("Update x=%d, y=%d => %d\n", unpackWheelIndex(data), unpackWheelPosition(data), unpackValue(data));
			break;
		case CMD_UPDATE_TURN_STATE:
			this.isTurning = (data & 1) == 1;
			break;
		}
    }

}
