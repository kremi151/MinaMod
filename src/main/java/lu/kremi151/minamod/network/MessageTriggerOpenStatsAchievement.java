package lu.kremi151.minamod.network;

import io.netty.buffer.ByteBuf;
import lu.kremi151.minamod.MinaMod;
import lu.kremi151.minamod.advancements.triggers.MinaTriggers;
import lu.kremi151.minamod.network.abstracts.AbstractServerMessageHandler;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class MessageTriggerOpenStatsAchievement implements IMessage{
	
	public MessageTriggerOpenStatsAchievement() {}

	@Override
	public void fromBytes(ByteBuf buf) {}

	@Override
	public void toBytes(ByteBuf buf) {}
	
	public static class Handler extends AbstractServerMessageHandler<MessageTriggerOpenStatsAchievement>{

		@Override
		public IMessage handleServerMessage(EntityPlayer player, MessageTriggerOpenStatsAchievement message, MessageContext ctx) {
			MinaMod.getProxy().getThreadListener(ctx)
				.addScheduledTask(() -> MinaTriggers.TRIGGER_OPEN_STATS.trigger((EntityPlayerMP) player));
			return null;
		}

	}

}
