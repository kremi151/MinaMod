package lu.kremi151.minamod.packet.message;

import io.netty.buffer.ByteBuf;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;

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

}
