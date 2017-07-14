package lu.kremi151.minamod.packet.message;

import java.util.UUID;

import io.netty.buffer.ByteBuf;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;

public class MessageSlotMachineTurn implements IMessage{
	
	private UUID id;
	private boolean response;
	private long seed;
	
	public MessageSlotMachineTurn() {}
	
	public MessageSlotMachineTurn(UUID id, boolean response, long seed) {
		this.id = id;
		this.response = response;
		this.seed = seed;
	}

	@Override
	public void fromBytes(ByteBuf buf) {
		id = new UUID(buf.readLong(), buf.readLong());
		response = buf.readBoolean();
		seed = buf.readLong();
	}

	@Override
	public void toBytes(ByteBuf buf) {
		buf.writeLong(id.getMostSignificantBits());
		buf.writeLong(id.getLeastSignificantBits());
		buf.writeBoolean(response);
		buf.writeLong(seed);
	}
	
	public UUID getId() {
		return id;
	}
	
	public boolean isResponse() {
		return response;
	}
	
	public long getSeed() {
		return seed;
	}

}
