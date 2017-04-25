package lu.kremi151.minamod.inventory;

import lu.kremi151.minamod.block.tileentity.TileEntityLetterbox;
import lu.kremi151.minamod.util.MinaUtils;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;

public class InventoryLetterboxIn extends BaseInventory{
	
	private TileEntityLetterbox te;
	
	public InventoryLetterboxIn(TileEntityLetterbox te){
		super(1);
		this.te = te;
	}

	@Override
	public int getInventoryStackLimit() {
		return 64;
	}

	@Override
	public void markDirty() {}

	@Override
	public boolean isUsableByPlayer(EntityPlayer player) {
		return true;
	}

	@Override
	public boolean isItemValidForSlot(int index, ItemStack stack) {
		return true;
	}

	@Override
	public String getName() {
		return "gui.letterbox.in";
	}

	@Override
	public void onCraftMatrixChanged() {
		te.markDirty();
	}

}
