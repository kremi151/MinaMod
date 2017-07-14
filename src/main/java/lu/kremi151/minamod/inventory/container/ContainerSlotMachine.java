package lu.kremi151.minamod.inventory.container;

import lu.kremi151.minamod.block.tileentity.TileEntitySlotMachine;
import net.minecraft.entity.player.EntityPlayer;

public class ContainerSlotMachine extends BaseContainer{
	
	private final EntityPlayer player;
	private final TileEntitySlotMachine slotMachine;
	
	public ContainerSlotMachine(EntityPlayer player, TileEntitySlotMachine slotMachine) {
		this.player = player;
		this.slotMachine = slotMachine;
	}

	@Override
	public boolean canInteractWith(EntityPlayer playerIn) {
		return true;
	}

}
