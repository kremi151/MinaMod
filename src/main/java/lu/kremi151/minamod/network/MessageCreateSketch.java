package lu.kremi151.minamod.network;

import io.netty.buffer.ByteBuf;
import lu.kremi151.minamod.MinaItems;
import lu.kremi151.minamod.network.abstracts.AbstractServerMessageHandler;
import lu.kremi151.minamod.util.MinaUtils;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.inventory.ContainerWorkbench;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemHandlerHelper;

public class MessageCreateSketch implements IMessage{

	@Override
	public void fromBytes(ByteBuf buf) {}

	@Override
	public void toBytes(ByteBuf buf) {}
	
	public static class Handler extends AbstractServerMessageHandler<MessageCreateSketch>{

		@Override
		public IMessage handleServerMessage(EntityPlayer player, MessageCreateSketch message, MessageContext ctx) {
			if(player.openContainer instanceof ContainerWorkbench) {
				ContainerWorkbench crafting = (ContainerWorkbench) player.openContainer;
				if(!crafting.craftResult.isEmpty() && player.inventory.hasItemStack(new ItemStack(Items.PAPER)) && MinaUtils.consumeInventoryItems(player.inventory, Items.PAPER, 1)) {
					ItemStack sketch = new ItemStack(MinaItems.SKETCH);
					
					//TODO
					
					sketch = ItemHandlerHelper.insertItem(player.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, null), sketch, false);
					if(!sketch.isEmpty()) {
						player.dropItem(sketch, true);
					}
				}
			}
			return null;
		}
		
	}

}
