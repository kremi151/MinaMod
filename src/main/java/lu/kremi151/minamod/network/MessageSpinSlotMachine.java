package lu.kremi151.minamod.network;

import java.util.Random;

import io.netty.buffer.ByteBuf;
import lu.kremi151.minamod.block.tileentity.TileEntitySlotMachine;
import lu.kremi151.minamod.network.abstracts.AbstractServerMessageHandler;
import lu.kremi151.minamod.util.slotmachine.SpinMode;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class MessageSpinSlotMachine implements IMessage{
	
	private BlockPos pos;
	private SpinMode mode;
	private boolean instant;
	
	public MessageSpinSlotMachine() {}
	
	public MessageSpinSlotMachine(SpinMode mode, BlockPos pos, boolean instant) {
		this.mode = mode;
		this.pos = pos;
		this.instant = instant;
	}

	@Override
	public void fromBytes(ByteBuf buf) {
		mode = SpinMode.values()[(int)buf.readByte()];
		pos = new BlockPos(
				ByteBufUtils.readVarInt(buf, 5),
				ByteBufUtils.readVarInt(buf, 5),
				ByteBufUtils.readVarInt(buf, 5)
				);
		instant = buf.readBoolean();
	}

	@Override
	public void toBytes(ByteBuf buf) {
		buf.writeByte((byte)mode.ordinal());
		ByteBufUtils.writeVarInt(buf, pos.getX(), 5);
		ByteBufUtils.writeVarInt(buf, pos.getY(), 5);
		ByteBufUtils.writeVarInt(buf, pos.getZ(), 5);
		buf.writeBoolean(instant);
	}
	
	public BlockPos getPos() {
		return pos;
	}
	
	public SpinMode getSpinMode(){
		return mode;
	}
	
	public boolean isInstant() {
		return instant;
	}
	
	public static class Handler extends AbstractServerMessageHandler<MessageSpinSlotMachine>{

		@Override
		public IMessage handleServerMessage(EntityPlayer player, MessageSpinSlotMachine message, MessageContext ctx) {
			TileEntitySlotMachine te = (TileEntitySlotMachine) player.world.getTileEntity(message.getPos());
			try {
				te.turnSlots(message.getSpinMode(), new Random(System.currentTimeMillis()), message.isInstant());
			}catch(IllegalStateException e) {}
			return null;
		}

	}

}
