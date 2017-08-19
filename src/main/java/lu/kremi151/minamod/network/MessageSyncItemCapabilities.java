package lu.kremi151.minamod.network;

import io.netty.buffer.ByteBuf;
import lu.kremi151.minamod.MinaMod;
import lu.kremi151.minamod.interfaces.ISyncCapabilitiesToClient;
import lu.kremi151.minamod.network.abstracts.AbstractClientMessageHandler;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class MessageSyncItemCapabilities implements IMessage{
	
	private int slot, windowId;
	private NBTTagCompound updateTag;
	
	public MessageSyncItemCapabilities(int windowId, int slot, NBTTagCompound updateTag) {
		this.windowId = windowId;
		this.slot = slot;
		this.updateTag = updateTag;
	}
	
	public MessageSyncItemCapabilities() {}

	@Override
	public void fromBytes(ByteBuf buf) {
		this.windowId = buf.readInt();
		this.slot = buf.readInt();
		this.updateTag = ByteBufUtils.readTag(buf);
	}

	@Override
	public void toBytes(ByteBuf buf) {
		buf.writeInt(windowId);
		buf.writeInt(slot);
		ByteBufUtils.writeTag(buf, updateTag);
	}
	
	public int getWindowId() {
		return windowId;
	}
	
	public int getSlot() {
		return slot;
	}
	
	public NBTTagCompound getTag() {
		return updateTag;
	}
	
	public static class Handler extends AbstractClientMessageHandler<MessageSyncItemCapabilities>{

		@Override
		public IMessage handleClientMessage(EntityPlayer player, MessageSyncItemCapabilities message, MessageContext ctx) {
			MinaMod.getProxy().getThreadListener(ctx).addScheduledTask(() -> {
				final Container container;
				if (message.getWindowId() == 0) {
					container = player.inventoryContainer;
				} else if (message.getWindowId() == player.openContainer.windowId) {
					container = player.openContainer;
				} else {
					return;
				}
				final ItemStack stack = container.getSlot(message.getSlot()).getStack();
				if(!stack.isEmpty() && stack.getItem() instanceof ISyncCapabilitiesToClient) {
					((ISyncCapabilitiesToClient)stack.getItem()).readSyncableData(stack, message.getTag());
				}
			});
			return null;
		}
		
	}

}
