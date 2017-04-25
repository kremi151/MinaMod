package lu.kremi151.minamod.packet.message;

import io.netty.buffer.ByteBuf;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;

public class MessageCoinBag implements IMessage{
	
	private int quantity;
	private Type type;
	
	public MessageCoinBag(){}
	
	public MessageCoinBag(Type type, int quantity){
		this.type = type;
		this.quantity = quantity;
	}
	
	public int getQuantity(){
		return quantity;
	}
	
	public Type getType(){
		return type;
	}

	@Override
	public void fromBytes(ByteBuf buf) {
		type = Type.values()[buf.readInt()];
		quantity = buf.readInt();
	}

	@Override
	public void toBytes(ByteBuf buf) {
		buf.writeInt(type.ordinal());
		buf.writeInt(quantity);
	}
	
	public static enum Type{
		UPDATE,
		WITHDRAW,
		DEPOSIT,
		DENIED;
	}

}
