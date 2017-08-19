package lu.kremi151.minamod.network;

import io.netty.buffer.ByteBuf;
import lu.kremi151.minamod.capabilities.amulets.CapabilityAmuletHolder;
import lu.kremi151.minamod.capabilities.amulets.IAmuletHolder;
import lu.kremi151.minamod.network.abstracts.AbstractServerMessageHandler;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class MessageUseAmulet implements IMessage{
	
	private int slot;
	
	public MessageUseAmulet(){}
	
	public MessageUseAmulet(int slot){
		this.slot = slot;
	}

	@Override
	public void fromBytes(ByteBuf buf) {
		this.slot = buf.readInt();
	}

	@Override
	public void toBytes(ByteBuf buf) {
		buf.writeInt(slot);
	}
	
	public int getSlot(){
		return slot;
	}
	
	public static class Handler extends AbstractServerMessageHandler<MessageUseAmulet>{

		@Override
		public IMessage handleServerMessage(EntityPlayer player, MessageUseAmulet message, MessageContext ctx) {
			if(message.getSlot() >= 0 && message.getSlot() < 3){
				IAmuletHolder holder = player.getCapability(CapabilityAmuletHolder.CAPABILITY_AMULET_HOLDER, null);
				holder.getAmuletAt(message.getSlot()).tryUse(player.world, player);
			}
			return null;
		}

	}


}
