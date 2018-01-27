package lu.kremi151.minamod.network;

import io.netty.buffer.ByteBuf;
import lu.kremi151.minamod.MinaAchievements;
import lu.kremi151.minamod.MinaMod;
import lu.kremi151.minamod.advancements.triggers.MinaTriggers;
import lu.kremi151.minamod.network.abstracts.AbstractMessageHandler;
import lu.kremi151.minamod.util.IDRegistry;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

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
	
	public static class Handler extends AbstractMessageHandler<MessageOpenGui>{

		@Override
		public IMessage handleClientMessage(EntityPlayer player, MessageOpenGui message, MessageContext ctx) {
			player.openGui(MinaMod.getMinaMod(), message.getGuiId(), player.world, message.x(), message.y(), message.z());
			return null;
		}

		@Override
		public IMessage handleServerMessage(EntityPlayer player, MessageOpenGui message, MessageContext ctx) {
			if(message.getGuiId() == IDRegistry.guiIdAmulets) {
				MinaTriggers.TRIGGER_OPEN_AMULET_INVENTORY.trigger((EntityPlayerMP) player);
			}
			player.openGui(MinaMod.getMinaMod(), message.getGuiId(), player.world, message.x(), message.y(), message.z());
			return null;
		}

	}

}
