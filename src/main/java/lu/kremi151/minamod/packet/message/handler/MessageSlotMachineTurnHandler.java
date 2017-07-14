package lu.kremi151.minamod.packet.message.handler;

import java.util.Random;

import lu.kremi151.minamod.packet.message.MessageSlotMachineTurn;
import lu.kremi151.minamod.packet.message.handler.abstracts.AbstractMessageHandler;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class MessageSlotMachineTurnHandler extends AbstractMessageHandler<MessageSlotMachineTurn>{

	@Override
	public IMessage handleClientMessage(EntityPlayer player, MessageSlotMachineTurn message, MessageContext ctx) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public IMessage handleServerMessage(EntityPlayer player, MessageSlotMachineTurn message, MessageContext ctx) {
		long seed = System.currentTimeMillis() ^ System.nanoTime();
		
		Random rand = new Random(seed);
		int minTurns = 10 + rand.nextInt(10);
		int spacingTurns = 2 + rand.nextInt(3);
		
		
		return new MessageSlotMachineTurn(message.getId(), true, seed);
	}

}
