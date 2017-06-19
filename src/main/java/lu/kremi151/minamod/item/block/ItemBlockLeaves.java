package lu.kremi151.minamod.item.block;

import lu.kremi151.minamod.block.BlockMinaLeaf;
import net.minecraft.item.ItemStack;

public class ItemBlockLeaves extends ItemBlockMulti<BlockMinaLeaf> {

	public ItemBlockLeaves(BlockMinaLeaf block, String[] namesByMeta) {
		super(block, namesByMeta);
	}
	
	@Override
    public String getUnlocalizedName(ItemStack stack)
    {
        return "tile.leaves_" + this.nameFunction.apply(stack);
    }

}
