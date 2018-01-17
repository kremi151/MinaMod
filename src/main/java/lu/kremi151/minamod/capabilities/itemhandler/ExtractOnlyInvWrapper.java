package lu.kremi151.minamod.capabilities.itemhandler;

import javax.annotation.Nonnull;

import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraftforge.items.wrapper.InvWrapper;

public class ExtractOnlyInvWrapper extends InvWrapper{

	public ExtractOnlyInvWrapper(IInventory inv) {
		super(inv);
	}
	
	@Override
    @Nonnull
    public ItemStack insertItem(int slot, @Nonnull ItemStack stack, boolean simulate)
    {
		return stack;
    }

}
