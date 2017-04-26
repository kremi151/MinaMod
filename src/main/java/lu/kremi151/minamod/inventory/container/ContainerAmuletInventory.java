package lu.kremi151.minamod.inventory.container;

import lu.kremi151.minamod.MinaItems;
import lu.kremi151.minamod.capabilities.amulets.CapabilityAmuletHolder;
import lu.kremi151.minamod.capabilities.amulets.IAmuletHolder;
import lu.kremi151.minamod.inventory.BaseInventory;
import lu.kremi151.minamod.inventory.SlotSpecific;
import lu.kremi151.minamod.item.amulet.AmuletBase;
import lu.kremi151.minamod.item.amulet.AmuletRegistry;
import lu.kremi151.minamod.item.amulet.AmuletStack;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;

public class ContainerAmuletInventory extends BaseContainer{
	
	private final EntityPlayer player;
	private final IAmuletHolder holder;
	private boolean invInitState = true;
	
	public ContainerAmuletInventory(EntityPlayer player){
		this.player = player;
		this.holder = player.getCapability(CapabilityAmuletHolder.CAPABILITY_AMULET_HOLDER, null);
		
		if(!player.world.isRemote){
			for(int n = 0 ; n <3 ; n++){
				AmuletStack amulet = holder.getAmuletAt(n);
				if(!amulet.isEmpty()){
					internalInv.setInventorySlotContents(n, amulet.toItemStack());
				}
			}
		}
		invInitState = false;

		addSlotToContainer(new SlotSpecific(internalInv, 0, 44, 34));
		addSlotToContainer(new SlotSpecific(internalInv, 1, 80, 34));
		addSlotToContainer(new SlotSpecific(internalInv, 2, 116, 34));
		
		bindPlayerInventory(player.inventory, 8, 84);
	}

	@Override
	public boolean canInteractWith(EntityPlayer playerIn) {
		return true;
	}
	
	private final BaseInventory internalInv = new BaseInventory(3){

		@Override
		public int getInventoryStackLimit() {
			return 1;
		}

		@Override
		public void markDirty() {
			// TODO Auto-generated method stub
			
		}

		@Override
		public boolean isUsableByPlayer(EntityPlayer player) {
			return true;
		}

		@Override
		public boolean isItemValidForSlot(int index, ItemStack stack) {
			return stack.isEmpty() || stack.getItem() == MinaItems.AMULET;
		}

		@Override
		public String getName() {
			return null;
		}

		@Override
		public void onCraftMatrixChanged() {
			if(!invInitState){
				for(int n = 0 ; n <3 ; n++){
					ItemStack stack = getStackInSlot(n);
					if(stack.isEmpty()){
						holder.setAmuletAt(n, AmuletStack.EMPTY);
					}else{
						holder.setAmuletAt(n, AmuletStack.fromItemStack(stack));
					}
				}
			}
		}
		
	};

}
