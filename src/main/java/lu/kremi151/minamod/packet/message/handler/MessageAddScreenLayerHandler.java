package lu.kremi151.minamod.packet.message.handler;

import lu.kremi151.minamod.MinaMod;
import lu.kremi151.minamod.packet.message.MessageAddScreenLayer;
import lu.kremi151.minamod.packet.message.handler.abstracts.AbstractClientMessageHandler;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class MessageAddScreenLayerHandler extends AbstractClientMessageHandler<MessageAddScreenLayer>{

	@Override
	public IMessage handleClientMessage(EntityPlayer player, MessageAddScreenLayer message, MessageContext ctx) {
		MinaMod.getProxy().addScreenLayer(message.getScreenId(), message.isForced());
		return null;
	}

}
