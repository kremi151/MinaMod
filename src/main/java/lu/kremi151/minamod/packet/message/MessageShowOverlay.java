package lu.kremi151.minamod.packet.message;

import java.util.Optional;

import io.netty.buffer.ByteBuf;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;

public class MessageShowOverlay implements IMessage{
	
	private String message;
	private int type;
	private long duration;

	public MessageShowOverlay(){}
	
	public MessageShowOverlay(String message, long duration){
		if(message == null)throw new NullPointerException();
		this.type = -1;
		this.message = message;
		this.duration = duration;
	}
	
	public MessageShowOverlay(int type, long duration){
		if(type < 0)throw new IllegalArgumentException();
		this.type = type;
		this.duration = duration;
	}
	
	@Override
	public void fromBytes(ByteBuf buf) {
		this.type = buf.readInt();
		this.duration = buf.readLong();
		if(type == -1)this.message = ByteBufUtils.readUTF8String(buf);
	}

	@Override
	public void toBytes(ByteBuf buf) {
		buf.writeInt(type);
		buf.writeLong(duration);
		if(type == -1)ByteBufUtils.writeUTF8String(buf, message);
	}
	
	public int getType(){
		return type;
	}
	
	public Optional<String> getMessage(){
		return (Optional<String>) ((type == -1 && message != null) ? Optional.of(message) : Optional.empty());
	}
	
	public long getDuration(){
		return duration;
	}

}
