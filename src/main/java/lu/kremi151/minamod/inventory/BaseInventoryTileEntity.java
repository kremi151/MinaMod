package lu.kremi151.minamod.inventory;

import net.minecraft.tileentity.TileEntity;

public class BaseInventoryTileEntity extends BaseInventoryImpl{
	
	private final TileEntity parent;

	public BaseInventoryTileEntity(TileEntity parent, String name, int size) {
		super(name, size);
		this.parent = parent;
	}
	
	@Override
	public void markDirty() {
		parent.markDirty();
	}

}
