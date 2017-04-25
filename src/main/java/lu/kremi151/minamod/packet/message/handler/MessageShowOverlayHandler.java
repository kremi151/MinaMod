package lu.kremi151.minamod.packet.message.handler;

import java.util.Optional;

import lu.kremi151.minamod.MinaMod;
import lu.kremi151.minamod.packet.message.MessageShowOverlay;
import lu.kremi151.minamod.packet.message.handler.abstracts.AbstractClientMessageHandler;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class MessageShowOverlayHandler extends AbstractClientMessageHandler<MessageShowOverlay>{

	@Override
	public IMessage handleClientMessage(EntityPlayer player, MessageShowOverlay message, MessageContext ctx) {
		if(message.getType() == -1){
			Optional<String> msg = message.getMessage();
			if(msg.isPresent())MinaMod.getProxy().addStringOverlay(msg.get(), message.getDuration());
		}else{
			MinaMod.getProxy().addOverlay(message.getType(), message.getDuration());
		}
		return null;
	}

}
