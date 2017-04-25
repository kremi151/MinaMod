package lu.kremi151.minamod.packet.message;

import io.netty.buffer.ByteBuf;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;

public class MessageUpdateTileEntity implements IMessage{
	
	private BlockPos pos;
	private String worldName;
	private boolean update;
	private NBTTagCompound tnbt;
	
	public MessageUpdateTileEntity(){}
	
	public MessageUpdateTileEntity(World world, BlockPos pos, boolean update){
		TileEntity te = world.getTileEntity(pos);
		if(te != null){
			tnbt = te.getUpdateTag();
			this.update = update;
			this.pos = pos;
			this.worldName = world.getWorldInfo().getWorldName();
		}else{
			throw new IllegalArgumentException("No tile entity found at " + pos);
		}
	}

	@Override
	public void fromBytes(ByteBuf buf) {
		this.worldName = ByteBufUtils.readUTF8String(buf);
		this.pos = new BlockPos(buf.readInt(), buf.readInt(), buf.readInt());
		this.tnbt = ByteBufUtils.readTag(buf);
		this.update = buf.readBoolean();
	}

	@Override
	public void toBytes(ByteBuf buf) {
		ByteBufUtils.writeUTF8String(buf, worldName);
		buf.writeInt(pos.getX());
		buf.writeInt(pos.getY());
		buf.writeInt(pos.getZ());
		ByteBufUtils.writeTag(buf, tnbt);
		buf.writeBoolean(update);
	}
	
	public String getWorldName(){
		return worldName;
	}
	
	public BlockPos getPos(){
		return pos;
	}
	
	public NBTTagCompound getUpdateTag(){
		return tnbt;
	}
	
	public boolean shouldUpdate(){
		return update;
	}

}
