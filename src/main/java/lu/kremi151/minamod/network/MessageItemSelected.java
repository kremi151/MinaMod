package lu.kremi151.minamod.network;

import io.netty.buffer.ByteBuf;
import lu.kremi151.minamod.container.ContainerSelectItem;
import lu.kremi151.minamod.network.abstracts.AbstractServerMessageHandler;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class MessageItemSelected implements IMessage{
	
	private int slot;
	
	public MessageItemSelected() {}
	
	public MessageItemSelected(int slot) {
		this.slot = slot;
	}

	@Override
	public void fromBytes(ByteBuf buf) {
		this.slot = buf.readInt();
	}

	@Override
	public void toBytes(ByteBuf buf) {
		buf.writeInt(slot);
	}
	
	public static class Handler extends AbstractServerMessageHandler<MessageItemSelected>{

		@Override
		public IMessage handleServerMessage(EntityPlayer player, MessageItemSelected message, MessageContext ctx) {
			ContainerSelectItem.itemSelected(player, message.slot);
			return null;
		}
		
	}

}
