package lu.kremi151.minamod.packet.message;

import io.netty.buffer.ByteBuf;
import lu.kremi151.minamod.block.tileentity.TileEntitySlotMachine;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;

public class MessageReportSlotMachine implements IMessage{
	
	private BlockPos pos;
	
	public MessageReportSlotMachine() {}
	
	public MessageReportSlotMachine(BlockPos pos) {
		this.pos = pos;
	}

	@Override
	public void fromBytes(ByteBuf buf) {
		pos = new BlockPos(
				ByteBufUtils.readVarInt(buf, 5),
				ByteBufUtils.readVarInt(buf, 5),
				ByteBufUtils.readVarInt(buf, 5)
				);
	}

	@Override
	public void toBytes(ByteBuf buf) {
		ByteBufUtils.writeVarInt(buf, pos.getX(), 5);
		ByteBufUtils.writeVarInt(buf, pos.getY(), 5);
		ByteBufUtils.writeVarInt(buf, pos.getZ(), 5);
	}
	
	public BlockPos getPos() {
		return pos;
	}

}
