package lu.kremi151.minamod.container.listener;

import lu.kremi151.minamod.MinaMod;
import lu.kremi151.minamod.interfaces.ISyncCapabilitiesToClient;
import lu.kremi151.minamod.network.MessageSyncItemCapabilities;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IContainerListener;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.NonNullList;

public class SyncItemCapabilitiesListener implements IContainerListener{
	
	private final EntityPlayerMP player;
	
	public SyncItemCapabilitiesListener(EntityPlayerMP player) {
		this.player = player;
	}

	@Override
	public void sendAllContents(Container containerToSend, NonNullList<ItemStack> itemsList) {
		for (int i = 0; i < itemsList.size(); i++) {
			final ItemStack stack = itemsList.get(i);
			if(isSyncable(stack)) {
				MessageSyncItemCapabilities msg = new MessageSyncItemCapabilities(containerToSend.windowId, i, ((ISyncCapabilitiesToClient)stack.getItem()).writeSyncableData(stack, new NBTTagCompound()));
				MinaMod.getMinaMod().getPacketDispatcher().sendTo(msg, player);
			}
		}
	}

	@Override
	public void sendSlotContents(Container containerToSend, int slotInd, ItemStack stack) {
		if(isSyncable(stack)) {
			MessageSyncItemCapabilities msg = new MessageSyncItemCapabilities(containerToSend.windowId, slotInd, ((ISyncCapabilitiesToClient)stack.getItem()).writeSyncableData(stack, new NBTTagCompound()));
			MinaMod.getMinaMod().getPacketDispatcher().sendTo(msg, player);
		}
	}
	
	private boolean isSyncable(ItemStack stack) {
		return !stack.isEmpty() && stack.getItem() instanceof ISyncCapabilitiesToClient && ((ISyncCapabilitiesToClient)stack.getItem()).canSyncCapabilitiesToClient(stack);
	}

	@Override
	public void sendWindowProperty(Container containerIn, int varToUpdate, int newValue) {}

	@Override
	public void sendAllWindowProperties(Container containerIn, IInventory inventory) {}

}
