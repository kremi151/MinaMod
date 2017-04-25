package lu.kremi151.minamod.block;

import lu.kremi151.minamod.MinaItems;
import lu.kremi151.minamod.block.abstracts.BlockCustomCrops;
import net.minecraft.item.Item;

public class BlockStrawberryCrop extends BlockCustomCrops{

	@Override
    protected Item getSeed()
    {
        return MinaItems.STRAWBERRY;
    }

	@Override
    protected Item getCrop()
    {
        return MinaItems.STRAWBERRY;
    }
}
