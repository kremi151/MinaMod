package lu.kremi151.minamod.block;

import lu.kremi151.minamod.MinaItems;
import lu.kremi151.minamod.block.abstracts.BlockCustomCrops;
import net.minecraft.item.Item;

public class BlockNamieFlower extends BlockCustomCrops{
	
	public BlockNamieFlower() {
		this.honeycombRegenerationChance = 0.2f;
	}

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
