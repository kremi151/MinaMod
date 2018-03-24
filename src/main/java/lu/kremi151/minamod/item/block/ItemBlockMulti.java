package lu.kremi151.minamod.item.block;

import lu.kremi151.minamod.block.BlockTable;
import net.minecraft.block.Block;
import net.minecraft.item.ItemMultiTexture;

public class ItemBlockMulti extends ItemMultiTexture{

	public ItemBlockMulti(Block block, String... namesByMeta) {
		super(block, block, namesByMeta);
	}

}
