package lu.kremi151.minamod.inventory.container;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.Slot;

public abstract class BaseContainer extends Container{
	
	protected static final int PLAYER_INV_SLOT_COUNT = 36;
	
	protected final void bindPlayerInventory(InventoryPlayer inventoryPlayer, int x, int y) {
        for (int i = 0; i < 3; i++) {
                for (int j = 0; j < 9; j++) {
                        addSlotToContainer(new Slot(inventoryPlayer, j + i * 9 + 9,
                                x + j * 18, y + i * 18));
                }
        }

        for (int i = 0; i < 9; i++) {
                addSlotToContainer(new Slot(inventoryPlayer, i, x + i * 18, y + (3 * 18) + 4));
        }
	}

}
