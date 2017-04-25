package lu.kremi151.minamod.block;

import lu.kremi151.minamod.MinaItems;
import lu.kremi151.minamod.block.abstracts.BlockCustomCrops;
import net.minecraft.item.Item;

public class BlockChiliCrop extends BlockCustomCrops{

	@Override
    protected Item getSeed()
    {
        return MinaItems.CHILI;
    }

	@Override
    protected Item getCrop()
    {
        return MinaItems.CHILI;
    }

}
