package lu.kremi151.minamod.packet.message.handler;

import lu.kremi151.minamod.MinaMod;
import lu.kremi151.minamod.packet.message.MessageSetScreenLayer;
import lu.kremi151.minamod.packet.message.handler.abstracts.AbstractClientMessageHandler;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class MessageSetScreenLayerHandler extends AbstractClientMessageHandler<MessageSetScreenLayer>{

	@Override
	public IMessage handleClientMessage(EntityPlayer player, MessageSetScreenLayer message, MessageContext ctx) {
		MinaMod.getProxy().setScreenLayer(message.getScreenId(), message.isForced());
		return null;
	}

}
