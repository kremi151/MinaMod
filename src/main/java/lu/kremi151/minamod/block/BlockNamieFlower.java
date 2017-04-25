package lu.kremi151.minamod.block;

import lu.kremi151.minamod.MinaItems;
import net.minecraft.block.BlockCrops;
import net.minecraft.item.Item;

public class BlockNamieFlower extends BlockCrops{

	@Override
	protected Item getSeed()
    {
        return MinaItems.NAMIE_FRUIT;
    }

	@Override
    protected Item getCrop()
    {
        return MinaItems.NAMIE_SEEDS;
    }

}
