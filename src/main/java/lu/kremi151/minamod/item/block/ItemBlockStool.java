package lu.kremi151.minamod.item.block;

import net.minecraft.block.Block;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.item.ItemCloth;
import net.minecraft.item.ItemStack;

public class ItemBlockStool extends ItemCloth{

	public ItemBlockStool(Block block) { // NO_UCD (unused code)
		super(block);
	}
	
	@Override
	public String getUnlocalizedName(ItemStack stack)
    {
        return super.getUnlocalizedName() + "." + EnumDyeColor.byMetadata(stack.getMetadata()).getName();
    }

}
