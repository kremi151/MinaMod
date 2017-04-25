package lu.kremi151.minamod.packet.message;

import io.netty.buffer.ByteBuf;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;

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

}
