package lu.kremi151.minamod.network;

import io.netty.buffer.ByteBuf;
import lu.kremi151.minamod.MinaMod;
import lu.kremi151.minamod.capabilities.ICapabilityJetpack;
import lu.kremi151.minamod.network.abstracts.AbstractServerMessageHandler;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class MessageJetpack implements IMessage{
	
	private boolean activate;
	
	public MessageJetpack(boolean activate){
		this.activate = activate;
	}
	
	public MessageJetpack(){}

	@Override
	public void fromBytes(ByteBuf buf) {
		this.activate = buf.readBoolean();
	}

	@Override
	public void toBytes(ByteBuf buf) {
		buf.writeBoolean(activate);
	}
	
	public boolean shouldActivate(){
		return activate;
	}
	
	public static class Handler extends AbstractServerMessageHandler<MessageJetpack>{

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


}
