package lu.kremi151.minamod.packet.message.handler;

import lu.kremi151.minamod.MinaMod;
import lu.kremi151.minamod.packet.message.MessageSpawnParticleEffect;
import lu.kremi151.minamod.packet.message.handler.abstracts.AbstractClientMessageHandler;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class MessageSpawnParticleEffectHandler extends AbstractClientMessageHandler<MessageSpawnParticleEffect>{

	@Override
	public IMessage handleClientMessage(EntityPlayer player, MessageSpawnParticleEffect msg, MessageContext ctx) {
		MinaMod.getProxy().spawnParticleEffect(msg.getEffect(), player.world, msg.x(), msg.y(), msg.z(), msg.red(), msg.green(), msg.blue());
		return null;
	}

}
