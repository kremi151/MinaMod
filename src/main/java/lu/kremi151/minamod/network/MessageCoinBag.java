package lu.kremi151.minamod.network;

import io.netty.buffer.ByteBuf;
import lu.kremi151.minamod.MinaItems;
import lu.kremi151.minamod.network.abstracts.AbstractMessageHandler;
import lu.kremi151.minamod.util.MinaUtils;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumHand;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class MessageCoinBag implements IMessage{
	
	private int quantity;
	private Type type;
	
	public MessageCoinBag(){}
	
	public MessageCoinBag(Type type, int quantity){
		this.type = type;
		this.quantity = quantity;
	}
	
	public int getQuantity(){
		return quantity;
	}
	
	public Type getType(){
		return type;
	}

	@Override
	public void fromBytes(ByteBuf buf) {
		type = Type.values()[buf.readInt()];
		quantity = buf.readInt();
	}

	@Override
	public void toBytes(ByteBuf buf) {
		buf.writeInt(type.ordinal());
		buf.writeInt(quantity);
	}
	
	public static enum Type{
		UPDATE,
		WITHDRAW,
		DEPOSIT,
		DENIED;
	}
	
	public static class Handler extends AbstractMessageHandler<MessageCoinBag>{

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

}
