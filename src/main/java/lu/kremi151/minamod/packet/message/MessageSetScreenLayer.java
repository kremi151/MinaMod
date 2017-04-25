package lu.kremi151.minamod.packet.message;

import io.netty.buffer.ByteBuf;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;

public class MessageSetScreenLayer implements IMessage{

	public static final byte DOGE = 0;
	public static final byte DOGE_INFINITE = 1;
	
	private byte id;
	private boolean force = false;
	
	public MessageSetScreenLayer(){}
	
	private MessageSetScreenLayer(byte id){
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
	
	public MessageSetScreenLayer force(){
		this.force = true;
		return this;
	}
	
	public static MessageSetScreenLayer fromId(byte id){
		return new MessageSetScreenLayer(id);
	}
	
	public static MessageSetScreenLayer removeLayer(){
		return new MessageSetScreenLayer((byte)-1);
	}

}
