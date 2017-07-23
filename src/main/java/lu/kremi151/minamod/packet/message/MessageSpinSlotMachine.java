package lu.kremi151.minamod.packet.message;

import io.netty.buffer.ByteBuf;
import lu.kremi151.minamod.util.slotmachine.SpinMode;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;

public class MessageSpinSlotMachine implements IMessage{
	
	private BlockPos pos;
	private SpinMode mode;
	
	public MessageSpinSlotMachine() {}
	
	public MessageSpinSlotMachine(SpinMode mode, BlockPos pos) {
		this.mode = mode;
		this.pos = pos;
	}

	@Override
	public void fromBytes(ByteBuf buf) {
		mode = SpinMode.values()[(int)buf.readByte()];
		pos = new BlockPos(
				ByteBufUtils.readVarInt(buf, 5),
				ByteBufUtils.readVarInt(buf, 5),
				ByteBufUtils.readVarInt(buf, 5)
				);
	}

	@Override
	public void toBytes(ByteBuf buf) {
		buf.writeByte((byte)mode.ordinal());
		ByteBufUtils.writeVarInt(buf, pos.getX(), 5);
		ByteBufUtils.writeVarInt(buf, pos.getY(), 5);
		ByteBufUtils.writeVarInt(buf, pos.getZ(), 5);
	}
	
	public BlockPos getPos() {
		return pos;
	}
	
	public SpinMode getSpinMode(){
		return mode;
	}

}
