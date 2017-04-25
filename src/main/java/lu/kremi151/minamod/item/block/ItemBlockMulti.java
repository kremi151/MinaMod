package lu.kremi151.minamod.item.block;

import lu.kremi151.minamod.block.BlockTable;
import net.minecraft.block.Block;
import net.minecraft.item.ItemMultiTexture;

public class ItemBlockMulti<A extends Block> extends ItemMultiTexture{

	public ItemBlockMulti(A block, String... namesByMeta) { // NO_UCD (unused code)
		super(block, block, namesByMeta);
	}

}
