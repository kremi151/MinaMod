package lu.kremi151.minamod.inventory.container;

import lu.kremi151.minamod.block.tileentity.TileEntityCollector;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IContainerListener;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ContainerCollector extends BaseContainer{

	EntityPlayer pl;
	TileEntityCollector te;
	
	int ticksLeft;
	int maxTicks = 1000;
	
	int lastTicksLeft;

	public ContainerCollector(EntityPlayer player, TileEntityCollector te) {
		this.pl = player;
		this.te = te;
		
		
		for(int i = 0 ; i < 9 ; i++){
			int x = i % 3;
			int y = i / 3;

			addSlotToContainer(new Slot(te, i, 62 + (x * 18), 17 + (y * 18)));
		}
		
		bindPlayerInventory(pl.inventory, 8, 84);
	}

	@Override
	public boolean canInteractWith(EntityPlayer playerIn) {
		return true;
	}

	@Override
	public ItemStack transferStackInSlot(EntityPlayer playerIn, int index)
    {
        ItemStack itemstack = ItemStack.EMPTY;
        Slot slot = (Slot)this.inventorySlots.get(index);

        if (slot != null && slot.getHasStack())
        {
            ItemStack itemstack1 = slot.getStack();
            itemstack = itemstack1.copy();

            if (index < 9)
            {
                if (!this.mergeItemStack(itemstack1, 9, 45, true))
                {
                    return ItemStack.EMPTY;
                }
            }
            else if (!this.mergeItemStack(itemstack1, 0, 9, false))
            {
                return ItemStack.EMPTY;
            }

            if (itemstack1.isEmpty())
            {
                slot.putStack(ItemStack.EMPTY);
            }
            else
            {
                slot.onSlotChanged();
            }

            if (itemstack1.getCount() == itemstack.getCount())
            {
                return ItemStack.EMPTY;
            }

            slot.onTake(playerIn, itemstack1);
        }

        return itemstack;
    }
	
	@Override
	@SideOnly(Side.CLIENT)
    public void updateProgressBar(int id, int data)
    {
		if(id == 0){
			this.ticksLeft = data;
		}else if(id == 1){
			this.maxTicks = data;
		}
    }
	
	@Override
	public void detectAndSendChanges(){
	    
        super.detectAndSendChanges();

        if(lastTicksLeft != te.getTicksLeft()){
        	lastTicksLeft = te.getTicksLeft();
        	int l = this.listeners.size();
        	for (int i = 0; i < l; ++i)
            {
        		IContainerListener icrafting = (IContainerListener)this.listeners.get(i);
                icrafting.sendWindowProperty(this, 0, te.getTicksLeft());
                icrafting.sendWindowProperty(this, 1, te.getMaxTicks());
            }
        }
        
	}
	
	public EntityPlayer getPlayer(){
		return pl;
	}
	
	public TileEntityCollector getCollector(){
		return te;
	}
	
	@SideOnly(Side.CLIENT)
	public int getTicksLeft(){
		return ticksLeft;
	}
	
	@SideOnly(Side.CLIENT)
	public int getMaxTicks(){
		return maxTicks;
	}

}
