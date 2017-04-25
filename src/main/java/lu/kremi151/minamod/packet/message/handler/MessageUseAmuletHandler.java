package lu.kremi151.minamod.packet.message.handler;

import lu.kremi151.minamod.capabilities.amulets.CapabilityAmuletHolder;
import lu.kremi151.minamod.capabilities.amulets.IAmuletHolder;
import lu.kremi151.minamod.item.amulet.AmuletStack;
import lu.kremi151.minamod.packet.message.MessageUseAmulet;
import lu.kremi151.minamod.packet.message.handler.abstracts.AbstractServerMessageHandler;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class MessageUseAmuletHandler extends AbstractServerMessageHandler<MessageUseAmulet>{

	@Override
	public IMessage handleServerMessage(EntityPlayer player, MessageUseAmulet message, MessageContext ctx) {
		if(message.getSlot() >= 0 && message.getSlot() < 3){
			IAmuletHolder holder = player.getCapability(CapabilityAmuletHolder.CAPABILITY_AMULET_HOLDER, null);
			holder.getAmuletAt(message.getSlot()).tryUse(player.world, player);
		}
		return null;
	}

}
