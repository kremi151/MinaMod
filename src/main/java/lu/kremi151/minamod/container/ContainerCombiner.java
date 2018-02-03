package lu.kremi151.minamod.container;

import lu.kremi151.minamod.inventory.BaseInventoryImpl;
import lu.kremi151.minamod.inventory.SlotReadOnly;
import lu.kremi151.minamod.inventory.SlotReadOnly.ISlotReadOnlyHandler;
import lu.kremi151.minamod.recipe.combiner.CombinerRecipeManager;
import lu.kremi151.minamod.util.ShiftClickManager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

public class ContainerCombiner extends BaseContainer implements ISlotReadOnlyHandler{

	private final EntityPlayer player;
	private final int slot;

	private final IInventory inputInv = new InventoryCrafting(this, 1, 3);
	private final IInventory outputInv = new BaseInventoryImpl("output", 1);
	
	private static final ShiftClickManager shiftClick = ShiftClickManager.builder()
					.addTransfer(4, 4 + PLAYER_INV_SLOT_COUNT, 0, 3, true)
					.defaultTransfer(4, 4 + PLAYER_INV_SLOT_COUNT, true)
					.build();

	public ContainerCombiner(EntityPlayer player, int combinerIndex) {
		this.player = player;
		this.slot = combinerIndex;

		addSlotToContainer(new Slot(inputInv, 0, 44, 17));
		addSlotToContainer(new Slot(inputInv, 1, 44, 35));
		addSlotToContainer(new Slot(inputInv, 2, 44, 53));
		addSlotToContainer(new SlotReadOnly(outputInv, 0, 116, 35).setListener(this));
		
		bindPlayerInventory(player.inventory, 8, 84);
	}

	@Override
	public boolean canInteractWith(EntityPlayer playerIn) {
		return true;
	}

	public ItemStack getStack() {
		return player.inventory.getStackInSlot(slot);
	}

	@Override
    public ItemStack transferStackInSlot(EntityPlayer player, int slot) {
		return shiftClick.handle(this, player, slot);
	}

	@Override
	public void onSlotPickedUp(IInventory inv, int slot, EntityPlayer p, ItemStack is) {
		for(int i = 0 ; i < 3 ; i++) {
			inputInv.decrStackSize(i, 1);
		}
	}

	@Override
	public boolean canPickup(IInventory inv, int slot, EntityPlayer p) {
		return true;
	}
	
	@Override
	public void onContainerClosed(EntityPlayer playerIn)
    {
        super.onContainerClosed(playerIn);
        for(int i = 0 ; i < inputInv.getSizeInventory() ; i++) {
        	ItemStack stack = inputInv.getStackInSlot(i);
        	if (!stack.isEmpty())
            {
                playerIn.dropItem(stack, false);
            }
        }
    }
	
	@Override
	public void onCraftMatrixChanged(IInventory inventoryIn)
    {
        super.onCraftMatrixChanged(inventoryIn);
        if(inventoryIn == inputInv) {
        	outputInv.setInventorySlotContents(0, CombinerRecipeManager.getResult((InventoryCrafting)inventoryIn));
        }
    }

}
