package lu.kremi151.minamod.packet.message.handler;

import lu.kremi151.minamod.MinaMod;
import lu.kremi151.minamod.capabilities.ICapabilityJetpack;
import lu.kremi151.minamod.packet.message.MessageJetpack;
import lu.kremi151.minamod.packet.message.handler.abstracts.AbstractServerMessageHandler;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class MessageJetpackHandler extends AbstractServerMessageHandler<MessageJetpack>{

	@Override
	public IMessage handleServerMessage(EntityPlayer player, MessageJetpack message, MessageContext ctx) {
		ICapabilityJetpack icj = player.getCapability(ICapabilityJetpack.CAPABILITY_JETPACK, null);
		if(icj != null){
			icj.setUsingJetpack(message.shouldActivate());
		}else{
			MinaMod.errorln("Expected jetpack capability for player %s, but there was none", player.getUniqueID().toString());
		}
		return null;
	}

}
