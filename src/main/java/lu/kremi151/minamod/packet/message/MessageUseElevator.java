package lu.kremi151.minamod.packet.message;

import io.netty.buffer.ByteBuf;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;

public class MessageUseElevator implements IMessage{
	
	int relativeFloor;
	
	public MessageUseElevator(){}
	
	public MessageUseElevator(int relativeFloor){
		this.relativeFloor = relativeFloor;
	}

	@Override
	public void fromBytes(ByteBuf buf) {
		this.relativeFloor = buf.readInt();
	}

	@Override
	public void toBytes(ByteBuf buf) {
		buf.writeInt(relativeFloor);
	}
	
	public int getRelativeFloor(){
		return relativeFloor;
	}

}
