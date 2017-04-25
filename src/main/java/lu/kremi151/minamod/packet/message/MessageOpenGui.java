package lu.kremi151.minamod.packet.message;

import io.netty.buffer.ByteBuf;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;

public class MessageOpenGui implements IMessage{
	
	private int gui_id = -1, x, y, z;
	
	public MessageOpenGui(int gui_id){ // NO_UCD (unused code)
		this(gui_id, 0, 0, 0);
	}
	
	public MessageOpenGui(int gui_id, int x, int y, int z){ // NO_UCD (unused code)
		this.gui_id = gui_id;
		this.x = x;
		this.y = y;
		this.z = z;
	}
	
	public MessageOpenGui(){}

	@Override
	public void fromBytes(ByteBuf buf) {
		this.gui_id = buf.readInt();
		this.x = buf.readInt();
		this.y = buf.readInt();
		this.z = buf.readInt();
	}

	@Override
	public void toBytes(ByteBuf buf) {
		buf.writeInt(gui_id);
		buf.writeInt(x);
		buf.writeInt(y);
		buf.writeInt(z);
	}
	
	public int getGuiId(){
		return gui_id;
	}
	
	public int x(){
		return x;
	}
	
	public int y(){
		return y;
	}
	
	public int z(){
		return z;
	}

}
