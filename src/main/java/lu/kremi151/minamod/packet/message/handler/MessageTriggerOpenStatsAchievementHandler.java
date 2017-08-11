package lu.kremi151.minamod.packet.message.handler;

import lu.kremi151.minamod.MinaAchievements;
import lu.kremi151.minamod.packet.message.MessageTriggerOpenStatsAchievement;
import lu.kremi151.minamod.packet.message.handler.abstracts.AbstractServerMessageHandler;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class MessageTriggerOpenStatsAchievementHandler extends AbstractServerMessageHandler<MessageTriggerOpenStatsAchievement>{

	@Override
	public IMessage handleServerMessage(EntityPlayer player, MessageTriggerOpenStatsAchievement message, MessageContext ctx) {
		player.addStat(MinaAchievements.OPEN_STATS, 1);
		return null;
	}

}
