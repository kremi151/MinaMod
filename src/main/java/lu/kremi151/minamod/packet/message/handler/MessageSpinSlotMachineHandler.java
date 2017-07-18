package lu.kremi151.minamod.packet.message.handler;

import java.util.Random;

import lu.kremi151.minamod.block.tileentity.TileEntitySlotMachine;
import lu.kremi151.minamod.packet.message.MessageSpinSlotMachine;
import lu.kremi151.minamod.packet.message.handler.abstracts.AbstractServerMessageHandler;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class MessageSpinSlotMachineHandler extends AbstractServerMessageHandler<MessageSpinSlotMachine>{

	@Override
	public IMessage handleServerMessage(EntityPlayer player, MessageSpinSlotMachine message, MessageContext ctx) {
		TileEntitySlotMachine te = (TileEntitySlotMachine) player.world.getTileEntity(message.getPos());
		try {
			te.turnSlots(new Random(System.currentTimeMillis()));
		}catch(IllegalStateException e) {}
		return null;
	}


}
