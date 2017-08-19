package lu.kremi151.minamod.network;

import io.netty.buffer.ByteBuf;
import lu.kremi151.minamod.network.abstracts.AbstractClientMessageHandler;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

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
	
	public static class Handler extends AbstractClientMessageHandler<MessageUpdateTileEntity>{

		@Override
		public IMessage handleClientMessage(EntityPlayer player, MessageUpdateTileEntity msg, MessageContext ctx) {
			if(Minecraft.getMinecraft().player.world.getWorldInfo().getWorldName().equals(msg.getWorldName())){
				TileEntity te = Minecraft.getMinecraft().player.world.getTileEntity(msg.getPos());
				if(te != null){
					IBlockState state = Minecraft.getMinecraft().player.world.getBlockState(msg.getPos());
					te.handleUpdateTag(msg.getUpdateTag());
					if(msg.shouldUpdate())Minecraft.getMinecraft().player.world.notifyBlockUpdate(msg.getPos(), state, state.getActualState(Minecraft.getMinecraft().player.world, msg.getPos()), 3);
				}
			}
			return null;
		}

	}

}
