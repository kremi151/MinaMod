package lu.kremi151.minamod.packet.message.handler;

import lu.kremi151.minamod.MinaMod;
import lu.kremi151.minamod.packet.message.MessageShowCustomAchievement;
import lu.kremi151.minamod.packet.message.handler.abstracts.AbstractClientMessageHandler;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class MessageShowCustomAchievementHandler extends AbstractClientMessageHandler<MessageShowCustomAchievement>{

	@Override
	public IMessage handleClientMessage(EntityPlayer player, MessageShowCustomAchievement msg, MessageContext ctx) {
		MinaMod.getProxy().showAchievementOverlay(msg.getTitle(), msg.getDescription(), msg.getDuration(), msg.getIcon());
		return null;
	}

}
