package lu.kremi151.minamod.packet.message.handler;

import lu.kremi151.minamod.packet.message.MessageUpdateTileEntity;
import lu.kremi151.minamod.packet.message.handler.abstracts.AbstractClientMessageHandler;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class MessageUpdateTileEntityHandler extends AbstractClientMessageHandler<MessageUpdateTileEntity>{

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
