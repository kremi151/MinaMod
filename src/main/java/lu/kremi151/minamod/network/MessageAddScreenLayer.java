package lu.kremi151.minamod.network;

import io.netty.buffer.ByteBuf;
import lu.kremi151.minamod.MinaMod;
import lu.kremi151.minamod.network.abstracts.AbstractClientMessageHandler;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class MessageAddScreenLayer implements IMessage{

	public static final byte DOGE = 0;
	public static final byte DOGE_INFINITE = 1;
	
	private byte id;
	private boolean force = false;
	
	public MessageAddScreenLayer(){}
	
	private MessageAddScreenLayer(byte id){
		this.id = id;
	}

	@Override
	public void fromBytes(ByteBuf buf) {
		id = buf.readByte();
		force = buf.readBoolean();
	}

	@Override
	public void toBytes(ByteBuf buf) {
		buf.writeByte(id);
		buf.writeBoolean(force);
	}
	
	public byte getScreenId(){
		return id;
	}
	
	public boolean isForced(){
		return force;
	}
	
	public MessageAddScreenLayer force(){
		this.force = true;
		return this;
	}
	
	public static MessageAddScreenLayer fromId(byte id){
		return new MessageAddScreenLayer(id);
	}
	
	public static MessageAddScreenLayer removeLayer(){
		return new MessageAddScreenLayer((byte)-1);
	}
	
	public static class Handler extends AbstractClientMessageHandler<MessageAddScreenLayer>{

		@Override
		public IMessage handleClientMessage(EntityPlayer player, MessageAddScreenLayer message, MessageContext ctx) {
			MinaMod.getProxy().addScreenLayer(message.getScreenId(), message.isForced());
			return null;
		}

	}

}
