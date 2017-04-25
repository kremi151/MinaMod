package lu.kremi151.minamod.packet.message.handler.abstracts;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public abstract class AbstractClientMessageHandler<T extends IMessage> extends AbstractMessageHandler<T> {

	// implementing a final version of the server message handler both prevents it from
	// appearing automatically and prevents us from ever accidentally overriding it
	@Override
	public final IMessage handleServerMessage(EntityPlayer player, T message, MessageContext ctx) {
		return null;
	}
	
}
