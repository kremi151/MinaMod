package lu.kremi151.minamod.container;

import lu.kremi151.minamod.MinaItems;
import lu.kremi151.minamod.MinaMod;
import lu.kremi151.minamod.inventory.SlotReadOnly;
import lu.kremi151.minamod.packet.message.MessageCoinBag;
import lu.kremi151.minamod.util.TextHelper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumHand;

public class ContainerCoinBag extends BaseContainer{
	
	private EntityPlayer player;
	private int slot = -1;
	
	public ContainerCoinBag(EntityPlayer player) {
		this.player = player;
		this.slot = player.inventory.currentItem;
		
		bindPlayerInventory(player.inventory, 8, 95);
		
		if(slot >= 0){
			int l = this.inventorySlots.size();
			for(int i = 0 ; i < l ;i++){
				Slot s = this.inventorySlots.get(i);
				if(s.getSlotIndex() == slot){
					this.inventorySlots.set(i, new SlotReadOnly(player.inventory, s.getSlotIndex(), s.xPos, s.yPos));
					break;
				}
			}
		}
	}

	@Override
	public boolean canInteractWith(EntityPlayer playerIn) {
		return true;
	}
	
	public ItemStack getStack(){
		return player.getHeldItem(EnumHand.MAIN_HAND);
	}

	@Override
    public ItemStack transferStackInSlot(EntityPlayer player, int slot) {
		ItemStack stack = ItemStack.EMPTY;
		if(this.slot < 0 || this.slot != slot){
			Slot _slot = this.getSlot(slot);
			if(_slot != null && _slot.getHasStack()){
				stack = _slot.getStack();
				if(!stack.isEmpty() && stack.getItem() == MinaItems.GOLDEN_COIN){
					ItemStack copy = stack.copy();
					final int amount = MinaItems.GOLDEN_COIN.getCoinValue(stack);
					final int unit = MinaItems.GOLDEN_COIN.getUnitCoinValue(stack);
					ItemStack coinBag = getStack();
					int maxMeta = coinBag.getMetadata() + amount;
					int newMeta = (maxMeta <= Short.MAX_VALUE)?maxMeta:Short.MAX_VALUE;
					int rest = maxMeta - newMeta;
					if(rest > 0){
						if(!player.world.isRemote)TextHelper.sendChatMessage(player, "Due to a known intern bug, this transaction will be blocked because it would exceed the maximum capacity of the coin bag. A bug fix will soon be available.");
						return ItemStack.EMPTY;
					}
					coinBag.setItemDamage(newMeta);
					final int unitsRest = (rest / unit) + ((rest % unit > 0) ? 1 : 0);
					stack.setCount(unitsRest);
					_slot.onSlotChange(stack, copy);
					_slot.onTake(player, stack);
					
					if(player instanceof EntityPlayerMP){
						MinaMod.getMinaMod().getPacketDispatcher().sendTo(new MessageCoinBag(MessageCoinBag.Type.UPDATE, coinBag.getMetadata()), (EntityPlayerMP)player);
					}
				}else{
					return ItemStack.EMPTY;
				}
			}
		}
		return stack;
	}
		
}
