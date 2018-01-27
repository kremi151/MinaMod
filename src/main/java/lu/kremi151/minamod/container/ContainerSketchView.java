package lu.kremi151.minamod.container;

import lu.kremi151.minamod.capabilities.sketch.ISketch;
import lu.kremi151.minamod.inventory.BaseInventoryImpl;
import lu.kremi151.minamod.inventory.PseudoInventoryCrafting;
import lu.kremi151.minamod.inventory.SlotReadOnly;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.CraftingManager;
import net.minecraft.util.NonNullList;

public class ContainerSketchView extends BaseContainer{

	private final PseudoInventoryCrafting invCraft = new PseudoInventoryCrafting(3, 3);
	private final BaseInventoryImpl invRes = new BaseInventoryImpl("", 1);
	
	public ContainerSketchView(EntityPlayer player, int sketchPos) {
		ItemStack stack = player.inventory.getStackInSlot(sketchPos);
		if(stack.hasCapability(ISketch.CAPABILITY, null)) {
			ISketch sketch = stack.getCapability(ISketch.CAPABILITY, null);
			NonNullList<ItemStack> order = sketch.getOrder();
			for(int i = 0 ; i < Math.min(order.size(), 9) ; i++) {
				if(order.get(i).getCount() > 1) {
					System.out.println("WTF?!?");
				}
				invCraft.setInventorySlotContents(i, order.get(i).copy());
			}
			ItemStack result = CraftingManager.findMatchingResult(invCraft, player.world);
			invRes.setInventorySlotContents(0, result.copy());
		}
		
		addSlotToContainer(new SlotReadOnly (invCraft, 0, 58, 34));
		addSlotToContainer(new SlotReadOnly (invCraft, 1, 80, 34));
		addSlotToContainer(new SlotReadOnly (invCraft, 2, 102, 34));
		
		addSlotToContainer(new SlotReadOnly (invCraft, 3, 58, 56));
		addSlotToContainer(new SlotReadOnly (invCraft, 4, 80, 56));
		addSlotToContainer(new SlotReadOnly (invCraft, 5, 102, 56));
		
		addSlotToContainer(new SlotReadOnly (invCraft, 6, 58, 78));
		addSlotToContainer(new SlotReadOnly (invCraft, 7, 80, 78));
		addSlotToContainer(new SlotReadOnly (invCraft, 8, 102, 78));
		
		addSlotToContainer(new SlotReadOnly (invRes, 0, 80, 114));
	}

	@Override
    public ItemStack transferStackInSlot(EntityPlayer player, int slot) {
		return ItemStack.EMPTY;
	}

	@Override
	public boolean canInteractWith(EntityPlayer playerIn) {
		return true;
	}

}
