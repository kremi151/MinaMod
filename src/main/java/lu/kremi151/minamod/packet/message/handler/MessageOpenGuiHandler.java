package lu.kremi151.minamod.packet.message.handler;

import lu.kremi151.minamod.MinaMod;
import lu.kremi151.minamod.packet.message.MessageOpenGui;
import lu.kremi151.minamod.packet.message.handler.abstracts.AbstractMessageHandler;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class MessageOpenGuiHandler extends AbstractMessageHandler<MessageOpenGui>{

	@Override
	public IMessage handleClientMessage(EntityPlayer player, MessageOpenGui message, MessageContext ctx) {
		player.openGui(MinaMod.getMinaMod(), message.getGuiId(), player.world, message.x(), message.y(), message.z());
		return null;
	}

	@Override
	public IMessage handleServerMessage(EntityPlayer player, MessageOpenGui message, MessageContext ctx) {
		player.openGui(MinaMod.getMinaMod(), message.getGuiId(), player.world, message.x(), message.y(), message.z());
		return null;
	}

}
