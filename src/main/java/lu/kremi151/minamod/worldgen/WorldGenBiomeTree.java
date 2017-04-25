package lu.kremi151.minamod.worldgen;

import net.minecraft.world.biome.Biome;
import net.minecraft.world.gen.feature.WorldGenAbstractTree;

public abstract class WorldGenBiomeTree extends WorldGenAbstractTree{

	public WorldGenBiomeTree(boolean notify) {
		super(notify);
	}
	
	abstract boolean canGenerateAtBiome(Biome biome);

}
