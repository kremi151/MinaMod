package lu.kremi151.minamod.packet.message.handler;

import lu.kremi151.minamod.MinaItems;
import lu.kremi151.minamod.packet.message.MessageCoinBag;
import lu.kremi151.minamod.packet.message.handler.abstracts.AbstractMessageHandler;
import lu.kremi151.minamod.util.MinaUtils;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumHand;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class MessageCoinBagHandler extends AbstractMessageHandler<MessageCoinBag>{

	@Override
	@SideOnly(Side.CLIENT)
	public IMessage handleClientMessage(EntityPlayer player, MessageCoinBag message, MessageContext ctx) {
		if(message.getType() != MessageCoinBag.Type.DENIED){
			if(net.minecraft.client.Minecraft.getMinecraft().currentScreen != null 
					&& net.minecraft.client.Minecraft.getMinecraft().currentScreen.getClass() == lu.kremi151.minamod.client.GuiCoinBag.class){
				lu.kremi151.minamod.client.GuiCoinBag gcb = (lu.kremi151.minamod.client.GuiCoinBag) net.minecraft.client.Minecraft.getMinecraft().currentScreen;
				if(message.getType() == MessageCoinBag.Type.UPDATE){
					gcb.update(message.getQuantity());
				}
			}
		}
		return null;
	}

	@Override
	public IMessage handleServerMessage(EntityPlayer player, MessageCoinBag message, MessageContext ctx) {
		ItemStack stack = player.getHeldItem(EnumHand.MAIN_HAND);
		if(!stack.isEmpty() && stack.getItem() == MinaItems.COIN_BAG){
			if(message.getType() == MessageCoinBag.Type.WITHDRAW){
				if(stack.getMetadata() >= message.getQuantity()){
					ItemStack coins = new ItemStack(MinaItems.GOLDEN_COIN, message.getQuantity());
					if(player.inventory.addItemStackToInventory(coins)){
						int nquantity = message.getQuantity() - coins.getCount();
						stack.setItemDamage(stack.getMetadata() - nquantity);
						return new MessageCoinBag(MessageCoinBag.Type.UPDATE, stack.getMetadata());
					}
				}
			}else if(message.getType() == MessageCoinBag.Type.DEPOSIT){
				if(stack.getMetadata() + message.getQuantity() <= Short.MAX_VALUE){
					if(MinaUtils.countItemsInInventory(player.inventory, MinaItems.GOLDEN_COIN) >= message.getQuantity()){
						MinaUtils.consumeInventoryItems(player.inventory, MinaItems.GOLDEN_COIN, message.getQuantity());
						stack.setItemDamage(stack.getMetadata() + message.getQuantity());
						return new MessageCoinBag(MessageCoinBag.Type.UPDATE, stack.getMetadata());
					}
				}
			}
		}
		return new MessageCoinBag(MessageCoinBag.Type.DENIED, -1);
	}

}
